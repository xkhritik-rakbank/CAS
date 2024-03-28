package com.newgen.omniforms.user;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.newgen.custom.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;
import com.newgen.omniforms.util.EventListenerHandler;
import com.newgen.omniforms.util.PL_SKLogger;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
public class PLCommon extends Common{

	public void getAge(String dateBirth,String controlName){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PL_SKLogger.writeLog("RLOS_Common", "Inside getAge(): "); 

			if (dateBirth.contains("/")){
				String parts[] = dateBirth.split("/");
				Calendar dob = Calendar.getInstance();
				Calendar today = Calendar.getInstance();
				System.out.println("today"+dob+today);
				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				System.out.println("age=="+age);
				int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
				System.out.println("month=="+month);
				if (month<0){
					age--; 
					month=today.get(Calendar.MONTH);
					month= 12-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);
					if(today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
						month=month-1;
					}
				}
				else if(month == 0 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)) {
					age--;
					month= 11-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);//If month is same as current no need to count it
				}
				else if(month == 1 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 2 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 3 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 4 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 5 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 6 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 7 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 8 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 9 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 10 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 11 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				//SKLogger.writeLog("RLOS_Common", "Values are with /: "+parts[2]+parts[1]+parts[0]+" age: "+age); 
				System.out.println("Age is====== "+age+"."+month);

				formObject.setNGValue(controlName,(age+"."+month).toString(),false); 
			}
			else if(dateBirth.contains("-")) {
				String parts[] = dateBirth.split("-");
				Calendar dob = Calendar.getInstance();
				Calendar today = Calendar.getInstance();

				dob.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])); 

				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

				if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
					age--; 
				}
                    PL_SKLogger.writeLog("RLOS_Common", "Values are with-: "+parts[2]+parts[1]+parts[0]+" age: "+age); 


				formObject.setNGValue("cmplx_Customer_age",age.toString(),false); 
			}
		}

		catch(Exception e){
			//try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));}finally{hm.clear();}
		}
}


	//Changes done to auto populate age in world check fragment
	public void getAgeWorldCheck(String dateBirth){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("CC_Common", "Inside getAge(): "); 
		String parts[] = dateBirth.split("/");
		Calendar dob = Calendar.getInstance();
		Calendar today = Calendar.getInstance();

		dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

		Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
			age--; 
		}
		PL_SKLogger.writeLog("CC_Common", "Values are: "+parts[2]+parts[1]+parts[0]+age); 


		formObject.setNGValue("WorldCheck1_age",age.toString(),false); 
	}

	// Changes done to load master values in world check birthcountry and residence country


	//functions added with respect to change in Customer_Details call(Tanshu Aggarwal 29/05/2017)
	public void setcustomer_enable(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setLocked("cmplx_Customer_Title", false);
			formObject.setLocked("cmplx_Customer_ResidentNonResident", false);
			formObject.setLocked("cmplx_Customer_gender", false);
			formObject.setLocked("cmplx_Customer_MotherName", false);
			formObject.setLocked("cmplx_Customer_VisaNo", false);
			formObject.setLocked("cmplx_Customer_MAritalStatus", false);
			formObject.setLocked("cmplx_Customer_COuntryOFResidence", false);
			formObject.setLocked("cmplx_Customer_SecNAtionApplicable", false);
			formObject.setLocked("cmplx_Customer_SecNationality", false);
			formObject.setLocked("cmplx_Customer_EMirateOfVisa", false);
			formObject.setLocked("cmplx_Customer_EmirateOfResidence", false);
			formObject.setLocked("cmplx_Customer_yearsInUAE", false);
			formObject.setLocked("cmplx_Customer_CustomerCategory", false);
			formObject.setLocked("cmplx_Customer_GCCNational", false);
			formObject.setLocked("cmplx_Customer_VIPFlag", false);
			//formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
			//formObject.setLocked("cmplx_Customer_VIsaExpiry", false);
		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
		}


	}
	
	public void LoadpicklistFCU()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PL_SKLogger.writeLog("PL_Common", "Inside loadPicklist");
			String Feedback=formObject.getNGValue("cmplx_Decision_feedbackstatus");
			PL_SKLogger.writeLog("PL_Common", "Inside loadPicklist::"+Feedback);
			
			if(Feedback.equalsIgnoreCase("Positive"))
			{
				formObject.setEnabled("cmplx_Decision_subfeedback", true);
				formObject.setNGValue("cmplx_Decision_subfeedback", "");
				LoadPickList("cmplx_Decision_subfeedback", "select '--Select--','--Select--' union select convert(varchar, Description) from NG_Master_Possubfeedbackstatus with (nolock)");
			}
			else if(Feedback.equalsIgnoreCase("Negative"))
			{
				formObject.setEnabled("cmplx_Decision_subfeedback", true);
				formObject.setNGValue("cmplx_Decision_subfeedback", "");
				LoadPickList("cmplx_Decision_subfeedback", "select '--Select--','--Select--' union select convert(varchar, Description) from NG_Master_subfeedbackstatus with (nolock)");
			}
			
			else{
				//formObject.setNGValue("cmplx_DEC_SubFeedbackStatus", " ");
				formObject.setEnabled("cmplx_Decision_subfeedback", false);
				formObject.setNGValue("cmplx_Decision_subfeedback", " ");
			}
		}//Arun (07/09/17)
	
	public void Field_employment()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			 //if(pId =="cmplx_EmploymentDetails_targetSegCode"){
				//alert(getNGValue('cmplx_EmploymentDetails_targetSegCode'));
					String empid="AVI,MED,EDU,HOT,PROM";
					if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")=="NEP"){
						
						formObject.setVisible("EMploymentDetails_Label25",true);
						formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
						formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
						formObject.setVisible("EMploymentDetails_Label62",false);
						formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
						formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
						formObject.setVisible("EMploymentDetails_Label5",false);
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
						formObject.setVisible("EMploymentDetails_Label59",false);
					}

					else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")=="FZD" ||formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")=="FZE"){
						formObject.setVisible("cmplx_EmploymentDetails_Freezone",true);
						formObject.setVisible("EMploymentDetails_Label62",true);
						formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",true);
						formObject.setVisible("EMploymentDetails_Label25",false);
						formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
						formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
						formObject.setVisible("EMploymentDetails_Label5",false);
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
						formObject.setVisible("EMploymentDetails_Label59",false);
					}
					
					else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")=="TEN")
					{
						formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",true);
						formObject.setVisible("EMploymentDetails_Label5",true);
						formObject.setVisible("EMploymentDetails_Label25",false);
						formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
						formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
						formObject.setVisible("EMploymentDetails_Label62",false);
						formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
						formObject.setVisible("EMploymentDetails_Label59",false);
					}
					
					else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")=="Surrogate" && empid.indexOf(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))>-1)
					{
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
						formObject.setVisible("EMploymentDetails_Label59",true);
						formObject.setVisible("EMploymentDetails_Label25",false);
						formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
						formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
						formObject.setVisible("EMploymentDetails_Label62",false);
						formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
						formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
						formObject.setVisible("EMploymentDetails_Label5",false);
					}
					else{
						formObject.setVisible("EMploymentDetails_Label25",false);
						formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
						formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
						formObject.setVisible("EMploymentDetails_Label62",false);
						formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
						formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
						formObject.setVisible("EMploymentDetails_Label5",false);
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
						formObject.setVisible("EMploymentDetails_Label59",false);
					}
				}

		// ended by yash on 27/7/2017 

	public void BussVerVisible()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		if(formObject.getNGValue("EmpType")=="Self Employed"){
			
			formObject.setVisible("Business_Verif",true);
			
		}
	}
	
	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			PL_SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 


			String WI_Name = formObject.getWFWorkitemName();
			PL_SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos wi_name: "+WI_Name );
			Map<String, String> curr_entry = new HashMap<String, String>();
			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();

			while(itr.hasNext()){
				List<String> Cif_data=new ArrayList<String>();
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();
				curr_entry = entry.getValue();

				Cif_data.add(curr_entry.get("SearchType")!=null?curr_entry.get("SearchType"):"");
				Cif_data.add(curr_entry.get("CustId")!=null?curr_entry.get("CustId"):"");
				Cif_data.add(curr_entry.get("PassportNum")!=null?curr_entry.get("PassportNum"):"");
				Cif_data.add(curr_entry.get("BlacklistFlag")!=null?curr_entry.get("BlacklistFlag"):"");
				Cif_data.add(curr_entry.get("DuplicationFlag")!=null?curr_entry.get("DuplicationFlag"):"");
				Cif_data.add(curr_entry.get("NegatedFlag")!=null?curr_entry.get("NegatedFlag"):"");
				Cif_data.add(curr_entry.get("Products")!=null?curr_entry.get("Products"):"");
				//Chnages done to add blacklist reasone code, data Negated Reasoncode and date start
				Cif_data.add(curr_entry.get("BlacklistReasonCode")!=null?curr_entry.get("BlacklistReasonCode"):"");
				Cif_data.add(curr_entry.get("BlacklistDate")!=null?curr_entry.get("BlacklistDate"):"");
				Cif_data.add(curr_entry.get("NegatedReasonCode")!=null?curr_entry.get("NegatedReasonCode"):"");
				Cif_data.add(curr_entry.get("NegatedDate")!=null?curr_entry.get("NegatedDate"):"");
				//Chnages done to add blacklist reasone code, data Negated Reasoncode and date end
				if(curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")){
					Cif_data.add("Y");
				}
				else{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItemFromList("q_cif_detail", Cif_data);

				PL_SKLogger.writeLog("RLSOCommon", "data to dave in cif details grid: "+ Cif_data);
				curr_entry=null;
				Cif_data=null;
			}
		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception occured while saving data for Customer Eligibility : ", e.getMessage());
		}


	}
	public void set_nonprimaryPassport(String cif_id, String pass_list){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.setNGValue("cmplx_Customer_CIFNO",cif_id);
			if(pass_list.contains(",")){
				String[] pass_list_arr = pass_list.split(",");
				for(int i= 0; i<pass_list_arr.length && i<4 ; i++){
					formObject.setNGValue("cmplx_Customer_Passport"+(i+2),pass_list_arr[i]);
				}
			}

		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception occured while seting non primary CIF: ", e.getMessage());
		}

	}
	public void Fields_ApplicationType_Employment()
	{
		PL_SKLogger.writeLog("RLOSCommon", "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				if(formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCE") || formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCN")){
					formObject.setVisible("EMploymentDetails_Label36",true); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",true); 
				}
				else{
					formObject.setVisible("EMploymentDetails_Label36",false); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",false); 
				}
	}
//method changed to handle cif without 0
	public void parse_cif_eligibility(String output){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		try{
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			PL_SKLogger.writeLog("inside parse_cif_eligibility","");
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
			String passport_list = "";
			//Map<String, String> cif_details = new HashMap<String, String>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
			PL_SKLogger.writeLog("name is doc : ", doc+"");
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
							PL_SKLogger.writeLog("tag name and tag value can not be null:: tag name: ",tag_name+ " tag value: " +tag_value);
						}

					}
					//changes done to remove internal blacklist without cif
					if(cif_details.containsKey("CustId")){
						Cus_details.put(Integer.parseInt(cif_details.get("CustId")), (HashMap<String, String>) cif_details) ;
					}
				}
			}
			formObject.clear("q_CIFDetail");
			int Prim_cif = primary_cif_identify(Cus_details);
			save_cif_data(Cus_details,Prim_cif);
			if(Prim_cif!=0){
				PL_SKLogger.writeLog("parse_cif_eligibility Primary CIF: ",Prim_cif+"");
				Map<String, String> prim_entry = new HashMap<String, String>();
					prim_entry = Cus_details.get(Prim_cif);
				String primary_pass = prim_entry.get("PassportNum");
				passport_list = passport_list.replace(primary_pass, "");
					PL_SKLogger.writeLog("Prim_cif: ",prim_entry.get("CustId"));
					set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
			}

			PL_SKLogger.writeLog("Prim_cif: ", Prim_cif+"");

		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
		}

	}
	//functions added with respect to change in Customer_Details call(Tanshu Aggarwal 29/05/2017)


	public void loadPicklistCustomer()  
	{
		// disha FSD
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklistCustomer: ");
		
		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_Country order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus order by Code");
		LoadPickList("ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state order by Code");
		LoadPickList("cmplx_Customer_referrorcode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ReferrorCode with (nolock)"); //Arun
		LoadPickList("cmplx_Customer_referrorname", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ReferrorCode with (nolock)"); //Arun
		//LoadPickList("cmplx_Customer_apptype", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_AppType with (nolock)"); //Arun
		LoadPickList("cmplx_Customer_corpcode", "select '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CollectionCode with (nolock)"); //Arun		
		LoadPickList("cmplx_Customer_CustomerCategory", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CustomerCategory order by Code");
		
	}
	
	public void loadPicklistRiskRating()
	{
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklistRiskRating: ");
		LoadPickList("cmplx_RiskRating_BusinessSeg", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Business_Segment with (nolock) order by Code");
		LoadPickList("cmplx_RiskRating_SubSeg", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Business_SubSegment with (nolock) order by Code");
		LoadPickList("cmplx_RiskRating_Demographics", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Demographics with (nolock) order by Code");
	}//Arun (10/10)
	public void loanvalidate()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("cmplx_LoanDetails_basetype",formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
		formObject.setNGValue("cmplx_LoanDetails_baserate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate"));
		formObject.setNGValue("cmplx_LoanDetails_marginrate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_MArginRate"));
		formObject.setNGValue("cmplx_LoanDetails_pdtpref",formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate"));
		formObject.setNGValue("cmplx_LoanDetails_netrate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_NetRate"));
		formObject.setNGValue("cmplx_LoanDetails_tenor",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
		formObject.setNGValue("cmplx_LoanDetails_repfreq",formObject.getNGValue("cmplx_EligibilityAndProductInfo_RepayFreq"));
		formObject.setNGValue("cmplx_LoanDetails_insplan",formObject.getNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment"));
		formObject.setNGValue("cmplx_LoanDetails_loanemi",formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
		formObject.setNGValue("cmplx_LoanDetails_moratorium",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"));
		formObject.setNGValue("cmplx_LoanDetails_frepdate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate"));
		formObject.setNGValue("cmplx_LoanDetails_maturitydate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"));
		formObject.setNGValue("cmplx_LoanDetails_ageatmaturity",formObject.getNGValue("cmplx_EligibilityAndProductInfo_AgeAtMaturity"));
		formObject.setNGValue("cmplx_LoanDetails_lpf",formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPF"));
		formObject.setNGValue("cmplx_LoanDetails_lpfamt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount"));
		formObject.setNGValue("cmplx_LoanDetails_insur",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Insurance"));
		formObject.setNGValue("cmplx_LoanDetails_insuramt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount"));		
		formObject.setNGValue("LoanDetails_duedate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate"));
		formObject.setNGValue("LoanDetails_amt",formObject.getNGValue("LoanDetails_loanamt"));
		
		String lpf = formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount");
		String insurance = formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount");
		String Amount = lpf + insurance;		
		formObject.setNGValue("cmplx_LoanDetails_amt",Amount);
		
		formObject.setNGValue("cmplx_LoanDetails_currency","AED");
		formObject.setNGValue("cmplx_LoanDetails_favourof","RAK");
		formObject.setNGValue("cmplx_LoanDetails_paidon",formObject.getNGValue("LoanDetails_disbdate"));
	}//Arun (21/09/17)

	public void loadPicklistELigibiltyAndProductInfo()  
	{
		LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
		LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
		LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstrumentType with (nolock) where isactive = 'Y'  order by code");
		//query added by saurabh on 22nd Oct for loading base rate type.
		LoadPickList("cmplx_EligibilityAndProductInfo_BaseRateType", "select distinct PRIME_TYPE from NG_master_Scheme order by PRIME_TYPE");
	}
	public void loadPicklistProduct(String ReqProd)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklistProduct$"+ReqProd+"$");
		//String ProdType=formObject.getNGValue("Product_type");

		if(ReqProd!=null && ReqProd.equalsIgnoreCase("Personal Loan")){
			PL_SKLogger.writeLog("RLOS_Common", "Inside equalsIgnoreCase()"); 
			formObject.setVisible("Scheme", true);
			formObject.setLeft("Scheme", 555);
			formObject.clear("SubProd");
			//formObject.setNGValue("SubProd", "--Select--");
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme", "--Select--");
			LoadPickList("SubProd", "select '--select--',''as 'code' union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL   order by code");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) where SchemeDesc like 'P_%' order by SCHEMEID");
			//added by abhishek
			LoadPickList("AppType", " select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("EmpType", " select '--Select--' union select convert(varchar, description) from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"'");
		}
		/*else{
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
		}*/
	}

	/*public void loadPicklist_Employment()
	{
		// disha FSD
		PL_SKLogger.writeLog("PLCommon", "Inside loadpicklist4:"); 
		LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) order by code");
		//LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--','' as code union select  convert(varchar,description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancycntrctemirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
        LoadPickList("cmplx_EmploymentDetails_marketcode", "select '--Select--' as description,'A50' as code union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) order by code");
        LoadPickList("cmplx_EmploymentDetails_channelcode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ChannelCode with (nolock) order by code");        
        LoadPickList("cmplx_EmploymentDetails_EmpIndusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock) order by code");        
        //LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMacro with (nolock) order by code");
        //LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMicro with (nolock) order by code");        
        LoadPickList("cmplx_EmploymentDetails_NOB", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_NATUREOFBUSINESS with (nolock) order by code");        
        LoadPickList("cmplx_EmploymentDetails_CurrEmployer", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmployerCategory_PL with (nolock) order by code");
        LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusCC where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMacro with (nolock) order by code");
	    LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMicro with (nolock) order by code");
	    LoadPickList("cmplx_EmploymentDetails_IndusSeg", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment  where isActive='Y'  order by code");
	    LoadPickList("cmplx_EmploymentDetails_FreezoneName","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_freezoneName where isActive='Y' order by code");	    
	    LoadPickList("cmplx_EmploymentDetails_categexpat","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_categnational","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType","select '--Select--' union select convert(varchar, Description) from NG_MASTER_EmpContractType where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_PromotionCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_collectioncode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CollectionCode where isActive='Y'  order by code");
	}*/

		public void loadCustomerCombo()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadCustomerCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadCustomerCombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        		"SELECT CardNotAvail,NEP,IsGenuine,NTB, EmiratesID,LAstName,MobileNo,PassportNo,FirstName,MiddleName,mothersName,VisaNo,age,EmirateIdExpiry,IdIssueDate,PassportExpiry,VIsaExpiry,yearsINUAE,CustCateg,GCC_National,dob,CIFNO FROM ng_RLOS_Customer with (nolock)"
	        		+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        		"cmplx_Customer_CARDNOTAVAIL;cmplx_Customer_NEP;cmplx_Customer_IsGenuine;cmplx_Customer_NTB;cmplx_Customer_EmiratesID;cmplx_Customer_LastNAme;cmplx_Customer_MobileNo;cmplx_Customer_PAssportNo;cmplx_Customer_FirstNAme;cmplx_Customer_MiddleNAme;cmplx_Customer_MotherName;cmplx_Customer_VisaNo;cmplx_Customer_Age;cmplx_Customer_EmirateIDExpiry;cmplx_Customer_IdIssueDate;cmplx_Customer_PassPortExpiry;cmplx_Customer_VisaExpiry;cmplx_Customer_yearsInUAE;cmplx_Customer_CustomerCategory;cmplx_Customer_GCCNational,cmplx_Customer_DOb,cmplx_Customer_CIFNo");
		
	        LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
	        //formObject.getNGDataFromDataCache("Select Title FROM ng_RLOS_Customer with (nolock) "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_Title");
	        formObject.getNGDataFromDataCache("Select gender FROM ng_RLOS_Customer with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_gender");
	        //formObject.getNGDataFromDataCache("Select EmirateVisa FROM ng_RLOS_Customer with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_EMirateOfVisa");
	        formObject.getNGDataFromDataCache("Select ResidentNonResident FROM ng_RLOS_Customer with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_RESIDENTNONRESIDENT");       
	        formObject.getNGDataFromDataCache("Select SecNationalityApplicable FROM ng_RLOS_Customer with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_SecNAtionApplicable");
	        
	        LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
	        //formObject.getNGDataFromDataCache("Select SecNationality FROM ng_RLOS_Customer with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_SecNationality");
	        LoadPickList("cmplx_Customer_COuntryOFResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_Country order by Code");
	        //formObject.getNGDataFromDataCache("Select CountryOfResidence FROM ng_RLOS_Customer with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_COUNTRYOFRESIDENCE");
	        //formObject.getNGDataFromDataCache("Select emirateOfResidence FROM ng_RLOS_Customer with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_EmirateOfResidence");
	        LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state order by Code");
			LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state order by Code");
		}
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadCustomerCombo :"+e.getMessage());}

	}

	public void loadProductCombo()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadProductCombo()");
		try 
		{
			 String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		     PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadFragment2Combo() ParentWI_NAme is "+ParentWI_Name);
			 String query="Select 	prodType,ReqProduct,SubProduct,ReqLimit,ApplicantType, CardProduct,EmpType,ReqTenor,Schem,Priority,RequestType,NewLimitValue,LimitExpDate  from ng_RLOS_GR_Product with (nolock) where 	prod_WIname ='"+ ParentWI_Name+"'" ;    
			 PL_SKLogger.writeLog("PersonnalLoanS> Query is:",query);
			 List<List<String>> list=formObject.getDataFromDataSource(query);
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadProductCombo()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4)+list.get(0).get(5));
			for (List<String> a : list) 
			{
				PL_SKLogger.writeLog("PersonnalLoanS> ","For LOOP:  "+a.get(0)+a.get(1)+a.get(2)+a.get(3)+a.get(4)+a.get(5));
				formObject.addItemFromList("cmplx_Product_cmplx_ProductGrid", a);
			}
		}
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadProductCombo :"+e.getMessage());}	
		//SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadProductCombo(): New WI_name :"+list.get(0).get(6));

	}
	
	public void loadEmpDetailsCombo()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadEmpDetailsCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadEmpDetailsCombo() ParentWI_NAme is "+ParentWI_Name);
	        String QUERY="SELECT EmpName,EmpCode,freezoneName,designation,ConfirmedInJob,DOJ,DOLPrev,DOJPrev,LOS,LOSPrevious,Freezone,Kompass,VisaSponser,IncludedPL,IncludedCC FROM ng_RLOS_EmpDetails with (nolock)"
	       		+ " WHERE  wi_name='"+ ParentWI_Name+"'";
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadEmpDetailsCombo() Query is "+QUERY);
	
	        formObject.getNGDataFromDataCache(QUERY,
	        		"cmplx_EmploymentDetails_EmpName;cmplx_EmploymentDetails_EMpCode;cmplx_EmploymentDetails_FreezoneName;cmplx_EmploymentDetails_Designation;cmplx_EmploymentDetails_JobConfirmed;cmplx_EmploymentDetails_DOJ;cmplx_EmploymentDetails_DOLPrev;cmplx_EmploymentDetails_DOJPrev;cmplx_EmploymentDetails_LOS;cmplx_EmploymentDetails_LOSPrevious;cmplx_EmploymentDetails_Freezone;cmplx_EmploymentDetails_Kompass;cmplx_EmploymentDetails_VisaSponser;cmplx_EmploymentDetails_IncInPL;cmplx_EmploymentDetails_IncInCC");
	        formObject.getNGDataFromDataCache("Select NepType FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_NepType");
	        formObject.getNGDataFromDataCache("Select AppCateg FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_ApplicationCateg");
	        formObject.getNGDataFromDataCache("Select TargetSegCode FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_targetSegCode");
	        formObject.getNGDataFromDataCache("Select empContractType FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmpContractType");       
	        formObject.getNGDataFromDataCache("Select EMpstatus FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmpStatus");
	        formObject.getNGDataFromDataCache("Select EmpType FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_Emp_Type");
	        formObject.getNGDataFromDataCache("Select CurrEmployer FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_CurrEmployer");
	        formObject.getNGDataFromDataCache("Select IndusSeg FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_IndusSeg");
	        formObject.getNGDataFromDataCache("Select emirateOfWork FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmirateOfWork");
	        formObject.getNGDataFromDataCache("Select headOfficeEmirate FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_HeadOfficeEmirate");
	        formObject.getNGDataFromDataCache("Select EmpIndustry FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmpIndusSector");
	        formObject.getNGDataFromDataCache("Select Emply_Industry_Macro FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_Emply_Industry_Macro");
	        formObject.getNGDataFromDataCache("Select Emply_Industry_Micro FROM ng_RLOS_EmpDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_Emply_Industry_Micro");
	        

	   }
        catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadEmpDetailsCombo :"+e.getMessage());}	
	}
	public void loadIncomeDetailsCombo()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadIncomeDetailsCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadIncomeDetailsCombo() ParentWI_NAme is "+ParentWI_Name);
	        String query=" SELECT basic,housing,transport,costOfliving,fixedOT,other,grossSal,overtime_Month1,overtime_Month2,overtime_Month3,overtime_avg,commission_Month1,commission_Month2,commission_Month3,commission_avg,foodAllow_Month1,foodAllow_Month2,foodAllow_Month3,foodAllow_avg,phoneAllow_Month1,phoneAllow_Month2,phoneAllow_Month3,phoneAllow_avg,serviceAllow_Month1,serviceAllow_Month2,serviceAllow_Month3,serviceAllow_avg,bonus_month1,bonus_month2,bonus_month3,bonus_avg,other_Month1,other_Month2,other_Month3,Other_avg,flying_Month1,flying_Month2,flying_Month3,flying_avg,totalSal,netSal1,netSal2,netSal3,avgNetSal,SalaryDay,SalaryXferToBank,DurationOfBanking,NoOfMonthsRakbankStat,AvgBal,CredTurnover,AvgCredTurnover,AnnualRent,bankStatFrom,bankStatFromDate,bankStatToDate,DurationOfBanking2,NoOfMonthsRakbankStat2,NoOfMonthsOtherkbankStat2,Statement_Cycle FROM ng_RLOS_IncomeDetails with (nolock)"
	    	        + " WHERE  wi_name='"+ ParentWI_Name+"' ";
	        formObject.getNGDataFromDataCache(query, "cmplx_IncomeDetails_Basic;cmplx_IncomeDetails_housing;cmplx_IncomeDetails_transport;cmplx_IncomeDetails_CostOfLiving;cmplx_IncomeDetails_FixedOT;cmplx_IncomeDetails_Other;cmplx_IncomeDetails_grossSal;cmplx_IncomeDetails_Overtime_Month1;cmplx_IncomeDetails_Overtime_Month2;cmplx_IncomeDetails_Overtime_Month3;cmplx_IncomeDetails_Overtime_Avg;cmplx_IncomeDetails_Commission_Month1;cmplx_IncomeDetails_Commission_Month2;cmplx_IncomeDetails_Commission_Month3;cmplx_IncomeDetails_Commission_Avg;cmplx_IncomeDetails_FoodAllow_Month1;cmplx_IncomeDetails_FoodAllow_Month2;cmplx_IncomeDetails_FoodAllow_Month3;cmplx_IncomeDetails_FoodAllow_Avg;cmplx_IncomeDetails_PhoneAllow_Month1;cmplx_IncomeDetails_PhoneAllow_Month2;cmplx_IncomeDetails_PhoneAllow_Month3;cmplx_IncomeDetails_PhoneAllow_Avg;cmplx_IncomeDetails_serviceAllow_Month1;cmplx_IncomeDetails_serviceAllow_Month2;cmplx_IncomeDetails_serviceAllow_Month3;cmplx_IncomeDetails_serviceAllow_Avg;cmplx_IncomeDetails_Bonus_Month1;cmplx_IncomeDetails_Bonus_Month2;cmplx_IncomeDetails_Bonus_Month3;cmplx_IncomeDetails_Bonus_Avg;cmplx_IncomeDetails_Other_Month1;cmplx_IncomeDetails_Other_Month2;cmplx_IncomeDetails_Other_Month3;cmplx_IncomeDetails_Other_Avg;cmplx_IncomeDetails_Flying_Month1;cmplx_IncomeDetails_Flying_Month2;cmplx_IncomeDetails_Flying_Month3;cmplx_IncomeDetails_Flying_Avg;cmplx_IncomeDetails_TotSal;cmplx_IncomeDetails_netSal1;cmplx_IncomeDetails_netSal2;cmplx_IncomeDetails_netSal3;cmplx_IncomeDetails_AvgNetSal;cmplx_IncomeDetails_SalaryDay;cmplx_IncomeDetails_SalaryXferToBank;cmplx_IncomeDetails_DurationOfBanking;cmplx_IncomeDetails_NoOfMonthsRakbankStat;cmplx_IncomeDetails_AvgBal;cmplx_IncomeDetails_CredTurnover;cmplx_IncomeDetails_AvgCredTurnover;cmplx_IncomeDetails_AnnualRent;cmplx_IncomeDetails_BankStatFrom;cmplx_IncomeDetails_BankStatFromDate;cmplx_IncomeDetails_BankStatToDate;cmplx_IncomeDetails_DurationOfBanking2;cmplx_IncomeDetails_NoOfMonthsRakbankStat2;cmplx_IncomeDetails_NoOfMonthsOtherbankStat2;cmplx_IncomeDetails_statement_cycle");
	        formObject.getNGDataFromDataCache("Select Accomodation FROM ng_RLOS_IncomeDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_Accomodation");
	        formObject.getNGDataFromDataCache("Select AvgBalFreq FROM ng_RLOS_IncomeDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_AvgBalFreq");
	        formObject.getNGDataFromDataCache("Select CreditTurnoverFreq FROM ng_RLOS_IncomeDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_CreditTurnoverFreq");
	        formObject.getNGDataFromDataCache("Select AnnualRentFreq FROM ng_RLOS_IncomeDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_AnnualRentFreq");       
	    }
	    catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadIncomeDetailsCombo :"+e.getMessage());}
	}
	
	public void loadMiscFieldsCombo() 
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadMiscFieldsCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadMiscFieldsCombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        "SELECT School,RealEstate  FROM ng_rlos_MiscFields with (nolock)"
	        + " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        "cmplx_MiscFields_School;cmplx_MiscFields_RealEstate"
	        + ";");
	        formObject.getNGDataFromDataCache("Select propertyType FROM ng_rlos_MiscFields with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_MiscFields_PropertyType");
	  }
	  catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadMiscFieldsCombo :"+e.getMessage());}
	}
	
	public void loadLiabilityNewCombo() 
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadLiabilityNewCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadLiabilityNewCombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        "SELECT DBR,TAI,DBRNet,aecbconsent  FROM ng_RLOS_Liability_New with (nolock)"
	        + " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        "cmplx_ExternalLiabilities_DBR;cmplx_ExternalLiabilities_TAI;cmplx_ExternalLiabilities_DBRNetSalary;cmplx_ExternalLiabilities_AECBConsent"
	        + ";");
	        
		}
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadLiabilityNewCombo :"+e.getMessage());}
	        
	}
	
	public void loadEligAndProductInfoCombo() 
	{
		try
		{
		   FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		   PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadEligAndProductInfoCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadEligAndProductInfoCombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        "SELECT FinalDBR,FinalLimit,InterestRate,EMI,Tenor,Netpayout,ISCalculatereeligibility,Moratorium,FirstRepaymentDate,MarginRate,BaseRate,ProdprefRate,NetRate,MaturityDate,AgeAtMaturity,NumberOfInstallment,LPF,LPFAmt,Insurance,InsuranceAmt FROM ng_rlos_EligAndProdInfo with (nolock)"
	        + " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        "cmplx_EligibilityAndProductInfo_FinalDBR;cmplx_EligibilityAndProductInfo_FinalLimit;cmplx_EligibilityAndProductInfo_InterestRate;cmplx_EligibilityAndProductInfo_EMI;cmplx_EligibilityAndProductInfo_Tenor;cmplx_EligibilityAndProductInfo_NetPayout;cmplx_EligibilityAndProductInfo_ISReeligibility;cmplx_EligibilityAndProductInfo_Moratorium;cmplx_EligibilityAndProductInfo_FirstRepayDate;cmplx_EligibilityAndProductInfo_MArginRate;cmplx_EligibilityAndProductInfo_BAseRate;cmplx_EligibilityAndProductInfo_ProdPrefRate;cmplx_EligibilityAndProductInfo_NetRate;cmplx_EligibilityAndProductInfo_MaturityDate;cmplx_EligibilityAndProductInfo_AgeAtMaturity;cmplx_EligibilityAndProductInfo_NoOfInstallments;cmplx_EligibilityAndProductInfo_LPF;cmplx_EligibilityAndProductInfo_LPFAmount;cmplx_EligibilityAndProductInfo_Insurance;cmplx_EligibilityAndProductInfo_InsuranceAmount"
	        + ";");
	        formObject.getNGDataFromDataCache("Select InstrumentType FROM ng_rlos_EligAndProdInfo with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_InstrumentType");
	        formObject.getNGDataFromDataCache("Select RepaymentFreq FROM ng_rlos_EligAndProdInfo with (nolock) "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_RepayFreq");
	        formObject.getNGDataFromDataCache("Select BaseRateType FROM ng_rlos_EligAndProdInfo with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_BaseRateType");
	        formObject.getNGDataFromDataCache("Select InterestType FROM ng_rlos_EligAndProdInfo with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_InterestType");       
	    }
	 catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadEligAndProductInfoCombo :"+e.getMessage());}
	
	}
	
	public void loadAddressDetails()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadAddressDetails()");
		try{
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	     PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadAddressDetails() ParentWI_NAme is "+ParentWI_Name);
		 String query="Select addressType,houseNo,buildingName,StreetNAme,landmark, city,state,country,po_box,yearsinCurrAddr,addr_wi_name  from ng_RLOS_gr_AddressDetails with (nolock) where 	addr_wi_name ='"+ ParentWI_Name+"' or addr_wi_name='"+formObject.getWFWorkitemName()+"'" ;    
		 PL_SKLogger.writeLog("PersonnalLoanS> Query is:",query);
		 List<List<String>> list=formObject.getDataFromDataSource(query);
		PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadAddressDetails()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4)+list.get(0).get(5));
		for (List<String> a : list) 
		{
			PL_SKLogger.writeLog("PersonnalLoanS> ","For LOOP:  "+a.get(0)+a.get(1)+a.get(2)+a.get(3)+a.get(4)+a.get(5));
			formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", a);
		}
		
	}catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadAddressDetails :"+e.getMessage());}	
		//SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadProductCombo(): New WI_name :"+list.get(0).get(6));

	}
	
	public void loadAltContactDetailsCombo() 
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadAltContactDetailsCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadAltContactDetailsCombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        "SELECT MobileNo_pri,MobileNo_sec,HomeCountryNo,OfficeNo,ResidenceNo,Email1_pri,Email2_sec,RetainAccIfLoanReq FROM ng_RLOS_AltContactDetails with (nolock)"
	        + " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        "AlternateContactDetails_MobileNo1;AlternateContactDetails_MobileNo2;AlternateContactDetails_HOMECOUNTRYNO;AlternateContactDetails_OFFICENO;AlternateContactDetails_RESIDENCENO;AlternateContactDetails_EMAIL1_PRI;AlternateContactDetails_EMAIL2_SEC;AlternateContactDetails_RetainAccIfLoanReq"
	        + ";");
	        formObject.getNGDataFromDataCache("Select estatementflag FROM ng_RLOS_AltContactDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","AlternateContactDetails_ESTATEMENTFLAG");
	        formObject.getNGDataFromDataCache("Select CustomerDomicileBranch FROM ng_RLOS_AltContactDetails with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","AlternateContactDetails_CustomerDomicileBranch");

		}
	catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadAltContactDetailsCombo :"+e.getMessage());}	
	}
	
	public void loadFATCACombo() 
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadFATCACombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadFATCACombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        "SELECT USRelation,TINNo,SignedDate,ExpiryDate FROM ng_rlos_FATCA"
	        + " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        "cmplx_FATCA_USRelation;cmplx_FATCA_TINNo;cmplx_FATCA_SignedDate;cmplx_FATCA_ExpiryDate"
	        + ";");
	        formObject.getNGDataFromDataCache("Select TypeOFRelation FROM ng_rlos_FATCA with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_TypeOFRelation");
	        formObject.getNGDataFromDataCache("Select DocsAvail FROM ng_rlos_FATCA with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_DocsAvail");
	        formObject.getNGDataFromDataCache("Select DocsCollected FROM ng_rlos_FATCA with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_DocsCollec");
	        formObject.getNGDataFromDataCache("Select COntrollingPersonHasUS FROM ng_rlos_FATCA with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_ControllingPersonUSRel");
	        formObject.getNGDataFromDataCache("Select Category FROM ng_rlos_FATCA with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_Category");
	 }
	catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadFATCACombo :"+e.getMessage());}	
	}
	
	//incoming doc function
	public void IncomingDoc(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
	
		String [] finalmisseddoc=new String[70];
		int rowRowcount=repObj.getRepeaterRowCount();
		 PL_SKLogger.writeLog("RLOS Initiation", "sQuery for document name is: rowRowcount" +  rowRowcount);
			if (repObj.getRepeaterRowCount() != 0) {
			
				for(int j = 0; j < rowRowcount; j++)
				{
					String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
					 PL_SKLogger.writeLog("RLOS Initiation", "sQuery for document name is: DocName" +  DocName);
				
					String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
					 PL_SKLogger.writeLog("RLOS Initiation", "sQuery for document name is: Mandatory" +  Mandatory);
						
					if("Y".equalsIgnoreCase(Mandatory))
						{
							 String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex")==null?"":repObj.getValue(j,"cmplx_DocName_DocIndex");
							 PL_SKLogger.writeLog("","DocIndex"+DocIndex);
							 String StatusValue=repObj.getValue(j,"cmplx_DocName_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Status");
							 PL_SKLogger.writeLog("","StatusValue"+StatusValue);
							 String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks")==null?"":repObj.getValue(j, "cmplx_DocName_Remarks");
							 PL_SKLogger.writeLog("","Remarks"+Remarks);
							
								 if(DocIndex==null||DocIndex.equalsIgnoreCase("")||DocIndex.equalsIgnoreCase("null")){
									 PL_SKLogger.writeLog("","StatusValue inside DocIndex"+DocIndex);
									 if(StatusValue.equalsIgnoreCase("Received")){
										 PL_SKLogger.writeLog("","StatusValue inside DocIndex recieved");
										 //below line commented as this mandatory document is already received. 
										// finalmisseddoc[j]=DocName;
									 }
								
					                  else if(StatusValue.equalsIgnoreCase("Deferred")){
					                      formObject.setNGValue("is_deferral_approval_require","Y");
					                      formObject.RaiseEvent("WFSave");
					                      PL_SKLogger.writeLog("Deferred flag value inside no document",formObject.getNGValue("is_deferral_approval_require"));
										  if(Remarks.equalsIgnoreCase("")){
											  PL_SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document and the status is Deferred");
												throw new ValidatorException(new FacesMessage("As you have Deferred "+DocName+" Document,So kindly fill the Remarks"));
											}
											else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
												 PL_SKLogger.writeLog("You may proceed further","Proceed further");
											}
					                 }
					                  else if(StatusValue.equalsIgnoreCase("Waived")){
					                      formObject.setNGValue("is_waiver_approval_require","Y");
					                      formObject.RaiseEvent("WFSave");
					                      PL_SKLogger.writeLog("waived flag value inside no document",formObject.getNGValue("is_waiver_approval_require"));
											if(Remarks.equalsIgnoreCase("")){
												 PL_SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document and the status is Waived");
												throw new ValidatorException(new FacesMessage("As you have Waived "+DocName+" Document,So kindly fill the Remarks"));
											}
											else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
												 PL_SKLogger.writeLog("You may proceed further","Proceed further");
											}
									  }
					                  else if((StatusValue.equalsIgnoreCase("--Select--"))||(StatusValue.equalsIgnoreCase(""))){
					                	  PL_SKLogger.writeLog("","StatusValue inside DocIndex is blank");
					                	  finalmisseddoc[j]=DocName;
					                  }
					                  else if((StatusValue.equalsIgnoreCase("Pending"))){
					                	  PL_SKLogger.writeLog("","StatusValue of doc is Pending");
					                	  
					                  }
									}
									else{
										if(!(DocIndex.equalsIgnoreCase(""))){
											if(!StatusValue.equalsIgnoreCase("Received")){
												repObj.setValue(j,"cmplx_DocName_Status","Received");
												repObj.setEditable(j, "cmplx_DocName_Status", false);
												 PL_SKLogger.writeLog("","StatusValue::123final"+StatusValue);
											}
											else {
												
												 PL_SKLogger.writeLog("","StatusValue::123final status is already received");
											}
										}
									}
									
								}
							}
						}
						StringBuilder mandatoryDocName = new StringBuilder("");
						
						 PL_SKLogger.writeLog("","length of missed document"+finalmisseddoc.length);
						 PL_SKLogger.writeLog("","length of missed document mandatoryDocName.length"+mandatoryDocName.length());
					
						for(int k=0;k<finalmisseddoc.length;k++)
						{
							if(null != finalmisseddoc[k]) {
								mandatoryDocName.append(finalmisseddoc[k]).append(",");
							}
							 PL_SKLogger.writeLog("RLOS Initiation", "finalmisseddoc is:" +finalmisseddoc[k]);
							 PL_SKLogger.writeLog("","length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
						}
						mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
						 PL_SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +mandatoryDocName.toString());
					
						if(mandatoryDocName.length()<=0){
						
							 PL_SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");
						
						}
						else{
							 PL_SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");
							 PL_SKLogger.writeLog("","length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
							throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
						}
					}
	//incomingdoc function
	
	public void loadKYCCombo() 
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadKYCCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadKYCCombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        "SELECT PEP  FROM ng_rlos_KYC"
	        + " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        "cmplx_KYC_PEP"
	        + ";");
	        formObject.getNGDataFromDataCache("Select KYCHeld FROM ng_rlos_KYC with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_KYC_KYCHeld");
		}
	    catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadKYCCombo :"+e.getMessage());}	

	}
	// disha FSd
	public void loadPickListOECD()
	{
		LoadPickList("OECD_CRSFlagReason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by Code");
		LoadPickList("OECD_CountryBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("OECD_townBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by Code");
		LoadPickList("OECD_CountryTaxResidence", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		
	}
	public void loadOECDCombo() 
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadOECDCombo()");
	        String ParentWI_Name = formObject.getNGValue("Parent_WIName");
	        PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadOECDCombo() ParentWI_NAme is "+ParentWI_Name);
	        formObject.getNGDataFromDataCache(
	        "SELECT CRSundocumentedFlag,CRSundocumentedFlagReason  FROM ng_rlos_OECD with (nolock)"
	        + " WHERE  wi_name='"+ ParentWI_Name+"' ",
	        "cmplx_OECD_CRS_Flag;cmplx_OECD_CRSFlagReason"
	        + ";");
	        formObject.getNGDataFromDataCache("Select city FROM ng_rlos_OECD with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_Town");
	        formObject.getNGDataFromDataCache("Select CountryTaxResidence FROM ng_rlos_OECD with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_CountryOfTaxResd");
	        formObject.getNGDataFromDataCache("Select country FROM ng_rlos_OECD with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_Country");
	        formObject.getNGDataFromDataCache("Select TaxpayerID FROM ng_rlos_OECD with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_taxpayerID");
	        formObject.getNGDataFromDataCache("Select NoTINReason FROM ng_rlos_OECD with (nolock)"+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_NoTINReason");
		}
	    catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadOECDCombo :"+e.getMessage());}
	}


	public void setDisabled()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->setDisabled()");
		String fields="cmplx_Customer_FirstNAme,cmplx_Customer_MiddleNAme,cmplx_Customer_LastNAme,cmplx_Customer_Age,cmplx_Customer_DOb,cmplx_Customer_gender,cmplx_Customer_EmiratesID,cmplx_Customer_IdIssueDate,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_MotherName,cmplx_Customer_PAssportNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VisaNo,cmplx_Customer_VisaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_MobileNo,cmplx_Customer_CIFNo";
		String array[]=fields.split(",");
		for(int i=0;array[i]!=null;i++)
		{
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->setDisabled()"+array[i]);

			formObject.setEnabled(array[i], false);

		}
	}


	public void loadPicklist3()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try
		{
			PL_SKLogger.writeLog("RLOSCommon", "Inside loadpicklist3:");
			LoadPickList("cmplx_Decision_refereason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock) order by code");
			formObject.clear("cmplx_Decision_Decision");
			String Query = "select '--Select--' as Decision union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and workstepname='"+formObject.getWFActivityName()+"' order by Decision desc";
			PL_SKLogger.writeLog("RLOSCommon Load desison Drop down: ",Query );
			LoadPickList("cmplx_Decision_Decision", Query);
			LoadPickList("cmplx_Decision_CADDecisiontray", "select ' --Select--' as Refer_Credit union select Refer_Credit from NG_Master_ReferCredit"); //Arun (12/10)			
			LoadPickList("cmplx_Decision_Decreasoncode", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock) order by code");
		}
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadPicklist3 :"+e.getMessage());}
	}


	public void loadPicklist_Address()
	{
		try
		{
			PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_Address: "); 
			LoadPickList("AddressDetails_addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock) order by code");
			LoadPickList("AddressDetails_city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
			LoadPickList("AddressDetails_state", "select '--Select--'as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
			LoadPickList("AddressDetails_country", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		}	
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadPicklist_Address :"+e.getMessage());}
	}

	public void loadPicklist_LoanDetails()
	{
		LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstallmentType with (nolock) order by Code");
		LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_COLLECTIONBRANCH with (nolock)");
		LoadPickList("cmplx_LoanDetails_ddastatus", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_DDASTATUS with (nolock)");
		LoadPickList("LoanDetails_modeofdisb", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ModeOfDisbursal with (nolock)");
		LoadPickList("LoanDetails_disbto", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
		LoadPickList("LoanDetails_holdcode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HoldCase with (nolock)");
		LoadPickList("cmplx_LoanDetails_paymode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_PAYMENTMODE with (nolock)");
		LoadPickList("cmplx_LoanDetails_status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_STATUS with (nolock)");
		LoadPickList("cmplx_LoanDetails_bankdeal", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
		LoadPickList("cmplx_LoanDetails_city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
	}

	public void loadPicklist_PartMatch()
	{
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_PartMatch: ");
		LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}

	//tanshu started

	// Changes done for OV - Same incoming doc repeater will be visible on OV now with columns visible and editable on basis of workstep

	public void fetchIncomingDocRepeater(){

		PL_SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName = formobject.getConfigElement("ActivityName");
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"");
		String Username=formObject.getUserName();//Arun (22/09/17)
		String requested_product="";
		String requested_subproduct="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,"valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,requested_subproduct);

			}    
		}
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" ,requested_product);

		//Disha started (10/6/17)

		List<String> repeaterHeaders = new ArrayList<String>();
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value sActivityName for:" ,sActivityName);
		if (sActivityName.equalsIgnoreCase("Original_Validation") || sActivityName.equalsIgnoreCase("Dispatch") || sActivityName.equalsIgnoreCase("Post_Disbursal") || sActivityName.equalsIgnoreCase("Disbursal") || sActivityName.equalsIgnoreCase("CC_Disbursal") || sActivityName.equalsIgnoreCase("Collection_User"))
		{
			PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value sActivityName for:" ,"OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expire Date");
			repeaterHeaders.add("Mandatory");
			//repeaterHeaders.add("Deferred/Waived");
			repeaterHeaders.add("Status");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");

			repeaterHeaders.add("OV Remarks");
			repeaterHeaders.add("OV Decision");
			repeaterHeaders.add("Approved By");
		}
		else
		{
			PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value sActivityName for:" ," non OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expire Date");
			repeaterHeaders.add("Mandatory");
			//repeaterHeaders.add("Deferred/Waived");
			repeaterHeaders.add("Status");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");

		}
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"after making headers");

		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> docName = null;
		String documentName = null;
		String documentNameMandatory=null;
		String ovRemarks=null;
		String ovdecision = null;
		String approvedBy=null;
		String statusValue = null;
		String expiryDate = null;//Arun (22/09/17)
        String Remarks= null;//Arun (22/09/17)
        String DocInd= null;//Arun (22/09/17)

		String query = "";

		IRepeater repObj=null;
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"after creating the object for repeater");

		int repRowCount = 0;

		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");

		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,""+repObj.toString());


		// query = "SELECT distinct DocName,Mandatory,Status,Remarks, FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
		//query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='LE-0000000007308-RLOS'";
		query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getNGValue("Parent_WIName")+"'";
		docName = formObject.getNGDataFromDataCache(query);


		//docName = formObject.getNGDataFromDataCache(query);
		//        SKLogger.writeLog("Incomingdoc",""+ docName);

		try{

			/*if (repObj.getRepeaterRowCount() == 0) {

            PL_SKLogger.writeLog("RLOS incoming document", "when row count is zero");
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"'";
            //query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='Personal Loan' and SubProductName='Expat Personal Loans'";
            docName = formObject.getNGDataFromDataCache(query);
            PL_SKLogger.writeLog("Incomingdoc",""+ docName);
        //    repObj.clear();

        }
        if (repObj.getRepeaterRowCount() != 0) {

            PL_SKLogger.writeLog("RLOS incoming document", "when row count is not zero");
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"' and ProcessName='RLOS'";
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"'";
            //query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='Personal Loan' and SubProductName='Expat Personal Loans' and  wi_name='"+formObject.getWFWorkitemName()+"'";
            docName = formObject.getNGDataFromDataCache(query);
            PL_SKLogger.writeLog("Incomingdoc",""+ docName);


        }*/
			repObj.setRepeaterHeaders(repeaterHeaders);

			if (sActivityName.equalsIgnoreCase("Original_Validation") )
			{
				PL_SKLogger.writeLog("Repeater Row Count to add row ov", "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);
					statusValue = docName.get(i).get(3);
					expiryDate = docName.get(i).get(1);//Arun (22/09/17)
	                  Remarks = docName.get(i).get(4);//Arun (22/09/17)
	                  DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PL_SKLogger.writeLog("Column Added in Repeater Username"," "+ Username);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					PL_SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);
					PL_SKLogger.writeLog("Column Added in Repeater expiryDate"," "+ expiryDate);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater Status"," "+ statusValue);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater Remarks"," "+ Remarks);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater DocInd"," "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);//Arun (22/09/17)
					repObj.setValue(i,3,statusValue);//Arun (22/09/17)
	                 repObj.setValue(i,4,Remarks);//Arun (22/09/17)
	                 repObj.setValue(i,11,DocInd);//Arun (22/09/17)
	                 repObj.setColumnDisabled(0, true);//Arun (22/09/17)
	     			 repObj.setColumnDisabled(2, true);//Arun (22/09/17)
	     			repObj.setValue(i,14,Username);//Arun (22/09/17)
	     			 repObj.setColumnDisabled(1, true);//Arun (22/09/17)
	     			 repObj.setColumnDisabled(3, true);//Arun (22/09/17)
	     			repObj.setColumnDisabled(14, true);//Arun (22/09/17)
					if(statusValue.equalsIgnoreCase("Recieved"))
					{
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , true);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , true);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , true);
					}
					else
					{
						/*repObj.setEditable(i,"IncomingDoc_Frame2_Reprow"+i+"_Repcolumn12" , false);
	            	repObj.setEditable(i,"IncomingDoc_Frame2_Reprow"+i+"_Repcolumn13" , false);
	            	repObj.setEditable(i,"IncomingDoc_Frame2_Reprow"+i+"_Repcolumn14" , false);*/
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , false);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , false);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , false);
					}
					repRowCount = repObj.getRepeaterRowCount();

					PL_SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);
				}
			}
			if ( sActivityName.equalsIgnoreCase("Dispatch") || sActivityName.equalsIgnoreCase("Post_Disbursal") || sActivityName.equalsIgnoreCase("Disbursal") || sActivityName.equalsIgnoreCase("CC_Disbursal") || sActivityName.equalsIgnoreCase("Collection_User"))
			{
				PL_SKLogger.writeLog("Repeater Row Count to add row ov", "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);	          
					statusValue = docName.get(i).get(3);
					expiryDate = docName.get(i).get(1);//Arun (22/09/17)
	                  Remarks = docName.get(i).get(4);//Arun (22/09/17)
	                  DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PL_SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					PL_SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);
					PL_SKLogger.writeLog("Column Added in Repeater expiryDate"," "+ expiryDate);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater Status"," "+ statusValue);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater Remarks"," "+ Remarks);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater DocInd"," "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					 repObj.setValue(i,1,expiryDate);//Arun (22/09/17)
	                 repObj.setValue(i,3,statusValue);//Arun (22/09/17)
	                 repObj.setValue(i,4,Remarks);//Arun (22/09/17)
	                 repObj.setValue(i,11,DocInd);//Arun (22/09/17)
	                 repObj.setColumnDisabled(0, true);//Arun (22/09/17)
	     			 repObj.setColumnDisabled(2, true);//Arun (22/09/17)

					repRowCount = repObj.getRepeaterRowCount();

					PL_SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);
				}
			}
			else
			{
				PL_SKLogger.writeLog("Repeater Row Count to add row maker csm", "add row CSm maker ");


				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();

					repObj.setColumnVisible(12, false);
					repObj.setColumnVisible(13, false);
					repObj.setColumnVisible(14, false);

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);
	                 expiryDate = docName.get(i).get(1);//Arun (22/09/17)
	                 statusValue = docName.get(i).get(3);//Arun (22/09/17)
	                  Remarks = docName.get(i).get(4);//Arun (22/09/17)
	                  DocInd = docName.get(i).get(5);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					PL_SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);
					PL_SKLogger.writeLog("Column Added in Repeater expiryDate"," "+ expiryDate);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater Status"," "+ statusValue);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater Remarks"," "+ Remarks);//Arun (22/09/17)
					PL_SKLogger.writeLog("Column Added in Repeater DocInd"," "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory); 	     
					 repObj.setValue(i,1,expiryDate);//Arun (22/09/17)
	                 repObj.setValue(i,3,statusValue);//Arun (22/09/17)
	                 repObj.setValue(i,4,Remarks);//Arun (22/09/17)
	                 repObj.setValue(i,11,DocInd);//Arun (22/09/17)
	                 repObj.setColumnDisabled(0, true);//Arun (22/09/17)
	     			 repObj.setColumnDisabled(2, true);//Arun (22/09/17)
	     			repObj.setDisabled(i, "cmplx_DocName_DocName", true);//Arun (22/09/17)
					if(statusValue == null || statusValue.equalsIgnoreCase("null") || statusValue.equalsIgnoreCase(null)){
						statusValue = "--Select--"; 
					}//Arun (22/09/17)
					repObj.setValue(i, 3, statusValue);//Arun (22/09/17)
					repObj.setValue(i, 4, Remarks);//Arun (22/09/17)

					repRowCount = repObj.getRepeaterRowCount();

					PL_SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);

				} //Disha ended (10/6/17)
			}
		}
		catch (Exception e) {

			PL_SKLogger.writeLog("EXCEPTION    :    ", " " + e.toString());

		} finally {

			repObj = null;

			repeaterHeaders = null;         
		}
	}

	public void loadInDecGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String activityName = formObject.getWFActivityName();//Arun (23/09/17)
		    String listviewName ="Decision_ListView1";//Arun (23/09/17)
		    String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm:ss'),userName,workstepName,Decisiom,remarks,'',dec_wi_name from ng_rlos_gr_Decision with (nolock) where dec_wi_name='"+ParentWI_Name+"' or dec_wi_name='"+formObject.getWFWorkitemName()+"' order by datelastchanged desc";
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
			/* try{
			 String date=list.get(0).get(0);
			 Date d=new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
			 SKLogger.writeLog("PersonnalLoanS>","value of date is:"+d.toString());*/
			 for (int i=0 ; i<list.size();i++) 
			{
	                List<String> mylist=new ArrayList<String>();
	                 mylist.add(list.get(i).get(0));
	                 mylist.add(list.get(i).get(1));
	                 mylist.add(list.get(i).get(2));
	                 mylist.add(list.get(i).get(3));
	                 mylist.add(list.get(i).get(4));
	                 mylist.add(list.get(i).get(5));
	                 mylist.add("");
	                 PL_SKLogger.writeLog("CC> ","Inside CCCommon ->loadInDecGrid()saurabh arraylist"+mylist);
	                formObject.addItemFromList(listviewName, mylist);
	                mylist.clear();
	            } //Arun (23/09/17)
				/*List<String> mylist=new ArrayList<String>();
				 mylist.add(d.toString());
				 mylist.add(list.get(0).get(1));
				 mylist.add(list.get(0).get(2));
				 mylist.add(list.get(0).get(3));
				 mylist.add(list.get(0).get(4));
				 mylist.add(list.get(0).get(5));
				formObject.addItemFromList("DecisionHistory_Decision_ListView1", a);
			}*///Arun (23/09/17)
		}catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadInDecGrid :"+e.getMessage());}	
	}


	public void loadInReferGrid()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm'),userName,workstepName,Decisiom,remarks,TargetDecision,workitemID,history_wi_nAme from ng_rlos_gr_ReferHistory with (nolock) where  history_wi_nAme='"+formObject.getWFWorkitemName()+"'";
		PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInReferGrid()"+query);
		List<List<String>> list=formObject.getDataFromDataSource(query);
		PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->Query Result is: "+list.toString());
		for (List<String> a : list) 
		{

			formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory", a);
		}

	}
	
	public void loadPicklist4()    
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("RLOSCommon", "Inside loadpicklist4:"); 
		String reqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
		String applicationtype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4); //Arun (31/10/17) as masters change
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");
	if(reqProd.equalsIgnoreCase("Personal Loan")){
		if(appCategory.equalsIgnoreCase("BAU")){
			LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Application_type = '"+applicationtype+"' and BAU='Y' and (product = 'PL' or product='B') order by code"); //Arun (31/10/17) as masters change
		}
		else if(appCategory.equalsIgnoreCase("Surrogate")){
			LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Application_type = '"+applicationtype+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code"); //Arun (31/10/17) as masters change
		}
		else{
			LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Application_type = '"+applicationtype+"' and (product = 'PL' or product='B') order by code");	//Arun (31/10/17) as masters change
		}
	}
	/*else if(reqProd.equalsIgnoreCase("Credit Card")){
		LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'CC' or product='B') order by code");	
	}*/
		//LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancycntrctemirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpIndusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmpContractType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_IndusSeg", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_channelcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ChannelCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusCC where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Categ_PL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_FreezoneName","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_freezoneName where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Status_PLExpact","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Status_PLNational","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancycntrctemirate","select '--Select--' union select convert(varchar, Description) from NG_MASTER_othBankCAC where isActive='Y'");
		//Code changes by aman to correctly load picklist
		LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select * from (select '--Select--' as macro union select distinct (convert(varchar(100), macro)) from NG_MASTER_EmpIndusMacroAndMicro  with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y')t order by case when t.macro = '--Select--' then 0 else 1 end"); 
		//below query changed bby saurabh on 22nd Oct as wrong masters were being loaded.
		LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select * from (select '--Select--' as micro union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro  with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y')t order by case when t.micro = '--Select--' then 0 else 1 end"); //Arun (30/10) to load masters --Select-- at top
		//Code changes by aman to correctly load picklist
		LoadPickList("cmplx_EmploymentDetails_EmpContractType","select '--Select--' union select convert(varchar, Description) from NG_MASTER_EmpContractType where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_PromotionCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_collectioncode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CollectionCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL where isActive='Y'  order by code");
		
		
		LoadPickList("cmplx_EmploymentDetails_marketcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' and Product = 'PL' order by code");	
	}
	
	public void Eligibilityfields()
	{
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_Address: "); 
		
		
		PL_SKLogger.writeLog("CC", "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("SE") || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC")) || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed"))){
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label3", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label4", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTai", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label6", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label11", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label13", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label12", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate", false);
				formObject.setTop("ELigibiltyAndProductInfo_Save", 650);
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
				formObject.setLeft("ELigibiltyAndProductInfo_Label5", 16);

				PL_SKLogger.writeLog("CC @yash", "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
				
				
	}
			else if((formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried Pensioner") ) && !formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM")){
				formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label6", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label11", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label13", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label12", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate", false);
				formObject.setTop("ELigibiltyAndProductInfo_Save", 650);
				//formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
				//formObject.setLeft("ELigibiltyAndProductInfo_Label5", 16);
			}
		
}


	public void saveIndecisionGrid(){

		PL_SKLogger.writeLog("PL_Common", "Inside saveIndecisionGrid: "); 
		String entrydate = ""; //Arun (23/09/17)
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String EntrydateTime = formObject.getNGValue("Entry_date_time") ; //Arun (23/09/17)
        String[] parts = EntrydateTime.split("/"); //Arun (23/09/17)
        entrydate = parts[1]+"/"+parts[0]+"/"+parts[2]; //Arun (23/09/17)
	        PL_SKLogger.writeLog("PL_Common", "Inside saveIndecisionGrid:EntrydateTime "+EntrydateTime); 
	        String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
	        PL_SKLogger.writeLog("PL_Common", "Inside saveIndecisionGrid...currDate "+currDate); 
	           String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks, dec_wi_name,Entry_Date) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+entrydate+"' as datetime),102))";
		PL_SKLogger.writeLog("PL_Common","Query is"+query);
		formObject.saveDataIntoDataSource(query);

	}	

		public void saveIndecisionGridCSM(){
			PL_SKLogger.writeLog("PL_Common", "Inside saveIndecisionGrid: "); 
	        String entrydate = "";       
	        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	        String Cifid=formObject.getNGValue("DecisionHistory_CIFid");
	        String emiratesid=formObject.getNGValue("DecisionHistory_Emiratesid");
	        String custName=formObject.getNGValue("DecisionHistory_Customer_Name");
	        PL_SKLogger.writeLog("PersonnalLoanS","Final val of custName:"+ custName);
	        String EntrydateTime = formObject.getNGValue("Entry_date_time") ;
	        String[] parts = EntrydateTime.split("/");
	        entrydate = parts[1]+"/"+parts[0]+"/"+parts[2]; 
	        PL_SKLogger.writeLog("PL_Common", "Inside saveIndecisionGrid:EntrydateTime "+EntrydateTime); 
	        PL_SKLogger.writeLog("PL_Common", "Inside  saveIndecisionGrid entrydate "+entrydate); 
	        String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime()); //Arun (16/10) included HH:mm to show the time as well in the grid
	        PL_SKLogger.writeLog("PL_Common", "Inside saveIndecisionGrid...currDate "+currDate); 
	           String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks, dec_wi_name,Entry_Date,CifId,EmiratesId,CustName) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+entrydate+"' as datetime),102),'"+Cifid+"','"+emiratesid+"','"+custName+"')";
	           PL_SKLogger.writeLog("PL_Common","Query is"+query);
	        formObject.saveDataIntoDataSource(query);
	        }
	public void AddInReferGrid()
	{
		PL_SKLogger.writeLog("PL_Common", "Inside AddInReferGrid: "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		String decision=formObject.getNGValue("cmplx_Decision_Decision");

		String query1="select workitemid from wfinstrumenttable where processinstanceid='"+formObject.getWFWorkitemName()+"' and activityname='"+formObject.getWFActivityName()+"'";
		List<List<String>> mylist=formObject.getDataFromDataSource(query1);
		PL_SKLogger.writeLog("PL_Common","Query1 is"+query1);
		PL_SKLogger.writeLog("PL_Common","Query1 Result is"+mylist.toString());

		/*if(decision.contains("Refer")){
    		String query2="insert into NG_RLOS_ReferHistory(dateLastChanged, userName, workstepName, Decisiom, remarks,wi_name) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+decision+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','" +formObject.getWFWorkitemName()+"')";
        	formObject.saveDataIntoDataSource(query2);
        	PL_SKLogger.writeLog("PL_Common","Query is:"+query2);
    	}*/

		List<String> Referlist=new ArrayList();
		Referlist.add(currDate);
		Referlist.add(formObject.getUserName());
		Referlist.add(formObject.getWFActivityName());
		Referlist.add(decision);
		Referlist.add(formObject.getNGValue("cmplx_Decision_REMARKS"));
		Referlist.add("");
		Referlist.add(mylist.get(0).get(0));
		Referlist.add(formObject.getWFWorkitemName());

		formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory",Referlist);
		PL_SKLogger.writeLog("PL_Common","ReferList is:"+Referlist.toString());
	}

	public void  loadPicklist1()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PL_SKLogger.writeLog("PL","Inside PLCommon ->loadPicklist1()"+ActivityName);

			LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");
			LoadPickList("cmplx_Decision_CADDecisiontray", "select ' --Select--' as Refer_Credit union select Refer_Credit from NG_Master_ReferCredit"); //Arun (12/10)
		}
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadPicklist1 :"+e.getMessage());}	
	}





	public String getCustAddress_details(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+add_row_count);
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
			String years_in_current_add=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 9);//9
			PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
			int years=Integer.parseInt(years_in_current_add);
			String preferrd="";
			//added
			String EffectiveFrom="";
			String EffectiveTo="";
			int DateEffectiveFromYears ;
			Calendar cal = Calendar.getInstance();
			int CurrentYear=cal.get(Calendar.YEAR);
			int CurrentMonth=cal.get(Calendar.MONTH)+1;
			int CurrentDate=cal.get(Calendar.DATE);
			PL_SKLogger.writeLog("RLOS value of CurrentDate",""+CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);
			EffectiveTo=CurrentYear+"-"+CurrentMonth+"-"+CurrentDate;
			PL_SKLogger.writeLog("RLOS value of CurrentDate EffectiveTo",""+EffectiveTo);
			DateEffectiveFromYears=CurrentYear-years;
			EffectiveFrom=DateEffectiveFromYears+"-"+CurrentMonth+"-"+CurrentDate;
			PL_SKLogger.writeLog("RLOS value of EffectiveFromDate",""+EffectiveFrom);

			//ended

			if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10).equalsIgnoreCase("true"))//10
				preferrd = "Y";
			else
				preferrd = "N";



			add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType><AddrPrefFlag>"+preferrd+"</AddrPrefFlag><PrefFormat>STRUCTURED_FORMAT</PrefFormat>";
			//added
			add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
			add_xml_str = add_xml_str + "<EffectiveTo>"+EffectiveTo+"</EffectiveTo>";
			//ended
			add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
			add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
			add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
			add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
			add_xml_str = add_xml_str + "<City>"+city+"</City>";
			add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode>";
			add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
			add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
		}
		PL_SKLogger.writeLog("RLOSCommon", "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String GenerateXML(String callName,String Operation_name)
	{
		PL_SKLogger.writeLog("RLOSCommon", "Inside GenerateXML():");

		StringBuffer final_xml= new StringBuffer("");
		String header ="";
		String footer = "";
		String parentTagName="";
		Socket socket = null;
		OutputStream out = null;
		InputStream socketInputStream = null;
		DataOutputStream dout = null;
		DataInputStream din = null;
		String mqOutputResponse=null;
		String mqInputRequest=null;
		String cabinetName=null;
		String wi_name=null;
		String ws_name=null;
		String sessionID=null;
		String userName=null;
		String socketServerIP;
		String sQuery=null;
		Integer socketServerPort;
		String fin_call_name="Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY";
		PL_SKLogger.writeLog("$$outputgGridtXML ","before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			PL_SKLogger.writeLog("RLOSCommon", "sQuery"+sQuery_header);
			List<List<String>> OutputXML_header=formObject.getNGDataFromDataCache(sQuery_header);
			if(!OutputXML_header.isEmpty()){
				PL_SKLogger.writeLog("RLOSCommon header: ",OutputXML_header.get(0).get(0)+" footer: "+OutputXML_header.get(0).get(1)+" parenttagname: "+OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);
				String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
				// String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val";
				// String sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive FROM NG_Integration_PL_field_Mapping where Call_name='"+callName+"'ORDER BY tag_seq ASC" ;
				if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
					PL_SKLogger.writeLog("inside if of operation","operation111"+Operation_name);
					PL_SKLogger.writeLog("inside if of operation","callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_PL_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					PL_SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
				}
				else{
					PL_SKLogger.writeLog("inside else of operation","operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_PL_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					PL_SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
				}

				//List<List<String>> OutputXML=formObject.getNGDataFromDataCache(sQuery);
				List<List<String>> OutputXML=formObject.getDataFromDataSource(sQuery);//chnage to get data from DB directly
				PL_SKLogger.writeLog("OutputXML","OutputXML"+OutputXML);
				if(!OutputXML.isEmpty()){
					//SKLogger.writeLog("$$AKSHAY",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					PL_SKLogger.writeLog("GenerateXML Integration field mapping table",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3)+OutputXML.get(0).get(4));
					PL_SKLogger.writeLog("GenerateXML Integration field mapping table",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					int n=OutputXML.size();
					System.out.println(n);


					if( n> 0)
					{

						PL_SKLogger.writeLog("","column length"+col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();
						Map<String, String> recordFileMap = new HashMap<String, String>();

						for(List<String> mylist:OutputXML)
						{
							// for(int i=0;i<col_n.length();i++)
							for(int i=0;i<8;i++)
							{
								//System.out.println("rec: "+records.item(rec));
								PL_SKLogger.writeLog("","column length values"+col_n);
								String[] col_name = col_n.split(",");
								recordFileMap.put(col_name[i],mylist.get(i));
							}
							recordFileMap.get("call_type");
							String Call_name = (String) recordFileMap.get("Call_name");
							String form_control = (String) recordFileMap.get("form_control");
							String parent_tag = (String) recordFileMap.get("parent_tag_name");
							String tag_name = (String) recordFileMap.get("xmltag_name");
							String is_repetitive = (String) recordFileMap.get("is_repetitive");
							String default_val = (String) recordFileMap.get("default_val");
							String data_format12 = (String) recordFileMap.get("data_format");
							PL_SKLogger.writeLog("#RLOS COmmonm inside generate XML: ","tag_name : "+tag_name +" valuie of default_val: "+default_val+" Call_name: "+Call_name+" parent_tag"+ parent_tag);
							String form_control_val="";
							java.util.Date startDate;

							if(tag_name.equalsIgnoreCase("AddressDetails") &&( Call_name.equalsIgnoreCase("DEDUP_SUMMARY")||Call_name.equalsIgnoreCase("BLACKLIST_DETAILS") || Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ"))){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getCustAddress_details(Call_name);
									PL_SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							// Code Changes to custom coding for dectech code
							 //FOR dectech
	                         
	                         else if(tag_name.equalsIgnoreCase("Channel") && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");
									
										String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
										
										String xml_str = int_xml.get(parent_tag);
										xml_str =  "<"+tag_name+">"+(ReqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding channelcode+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
											                            	
								}
								
								else if(tag_name.equalsIgnoreCase("emp_type") && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");
									
										String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
									if(empttype!=null){	
										if (empttype.equalsIgnoreCase("Salaried")){
											empttype="S";
										}
										else if (empttype.equalsIgnoreCase("Salaried Pensioner")){
											empttype="SP";
										}
										else {
											empttype="SE";
										}
									}
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding channelcode+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
											                            	
								}
								else if(tag_name.equalsIgnoreCase("world_check") && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE world_check+ ");
									
										String world_check=formObject.getNGValue("IS_WORLD_CHECK");
										PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
										if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid")==0){
											world_check="Negative";
										}
										else {
											world_check="Positive";
										}
										
									
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding world_check+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
											                            	
								}
								else if(tag_name.equalsIgnoreCase("current_emp_catogery") && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE current_emp_catogery+ ");

									String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
									PL_SKLogger.writeLog("RLOS COMMON"," value of current_emp_catogery "+current_emp_catogery);
									String squerycurremp="select Description from NG_MASTER_EmployerCategory_PL where isActive='Y' and code='"+current_emp_catogery+"'";
									PL_SKLogger.writeLog("RLOS COMMON"," query is "+squerycurremp);
									List<List<String>> squerycurrempXML=formObject.getDataFromDataSource(squerycurremp);
									PL_SKLogger.writeLog("RLOS COMMON"," query is "+squerycurrempXML);
								if(!squerycurrempXML.isEmpty()){
									if (squerycurrempXML.get(0).get(0)!=null){
										PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
										current_emp_catogery=squerycurrempXML.get(0).get(0);
									}
								}

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

									PL_SKLogger.writeLog("RLOS COMMON"," after adding current_emp_catogery+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}
								else if((tag_name.equalsIgnoreCase("prev_loan_dbr")||tag_name.equalsIgnoreCase("prev_loan_tai")||tag_name.equalsIgnoreCase("prev_loan_multiple")||tag_name.equalsIgnoreCase("prev_loan_employer")) && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE prev_loan_dbr+ ");
									String Limit_Increase="";
									String PreviousLoanDBR="";
									String PreviousLoanEmp="";
									String PreviousLoanMultiple="";
									String PreviousLoanTAI="";
									
									String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails where Request_Type='CollectionsSummary' and Limit_Increase='true'  and Child_wi= '"+formObject.getWFWorkitemName()+"'";
									List<List<String>> prevLoan=formObject.getDataFromDataSource(squeryloan);
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE prev_loan_dbr+ "+squeryloan);
									
									if (prevLoan!=null && prevLoan.size()>0){
										 PreviousLoanDBR=prevLoan.get(0).get(0);
										 PreviousLoanEmp=prevLoan.get(0).get(1);
										 PreviousLoanMultiple=prevLoan.get(0).get(2);
										 PreviousLoanTAI=prevLoan.get(0).get(3);
									 }
										
									
										String xml_str = int_xml.get(parent_tag);
										if(tag_name.equalsIgnoreCase("prev_loan_dbr")){
											xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanDBR+"</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("prev_loan_tai")){
											xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanTAI+"</"+ tag_name+">";
										}
										else if(tag_name.equalsIgnoreCase("prev_loan_multiple")){
											xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanMultiple+"</"+ tag_name+">";
										}
										else if(tag_name.equalsIgnoreCase("prev_loan_employer")){
											xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanEmp+"</"+ tag_name+">";
										}
										
										
									
										PL_SKLogger.writeLog("RLOS COMMON"," after adding world_check+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
											                            	
								}
								
								else if(tag_name.equalsIgnoreCase("no_of_cheque_bounce_int_3mon_Ind") && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
									String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
									List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
									if (NOC!=null && NOC.size()>0){
												String xml_str =  int_xml.get(parent_tag);
										xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
								
									}
									
								}
								else if(tag_name.equalsIgnoreCase("no_of_DDS_return_int_3mon_Ind") && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
									String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
									List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
									if (NOC!=null && NOC.size()>0){
												String xml_str =  int_xml.get(parent_tag);
										xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
								
									}
									
								}
								else if(((tag_name.equalsIgnoreCase("blacklist_cust_type"))||(tag_name.equalsIgnoreCase("internal_blacklist"))||(tag_name.equalsIgnoreCase("internal_blacklist_date"))||(tag_name.equalsIgnoreCase("internal_blacklist_code"))||(tag_name.equalsIgnoreCase("negative_cust_type"))||(tag_name.equalsIgnoreCase("internal_negative_flag"))||(tag_name.equalsIgnoreCase("internal_negative_date"))||(tag_name.equalsIgnoreCase("internal_negative_code"))) && Call_name.equalsIgnoreCase("DECTECH")){
										PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");
									String ParentWI_Name = formObject.getNGValue("Parent_WIName");
									String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail where cif_wi_name='"+ParentWI_Name+"' and cif_searchType = 'Internal'";
									List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
									String internal_blacklist =  "";
									String internal_blacklist_date =  "";
									String internal_blacklist_code =  "";
									String internal_negative_flag =  "";
									String internal_negative_date =  "";
									String internal_negative_code =  "";
									
									if (Blacklist!=null && Blacklist.size()>0){		
										internal_blacklist =  Blacklist.get(0).get(0);
										internal_blacklist_date =  Blacklist.get(0).get(1);
										internal_blacklist_code =  Blacklist.get(0).get(2);
										internal_negative_flag =  Blacklist.get(0).get(3);
										internal_negative_date =  Blacklist.get(0).get(4);
										internal_negative_code =  Blacklist.get(0).get(5);
										}
									String xml_str =  int_xml.get(parent_tag);
									if(tag_name.equalsIgnoreCase("blacklist_cust_type")){
										xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
										}
										else if(tag_name.equalsIgnoreCase("internal_blacklist")){
											xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist+"</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("internal_blacklist_date")){
											xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_date+"</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("internal_blacklist_code")){
											xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_code+"</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("negative_cust_type")){
											xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("internal_negative_flag")){
											xml_str = xml_str+ "<"+tag_name+">"+internal_negative_flag+"</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("internal_negative_date")){
											xml_str = xml_str+ "<"+tag_name+">"+internal_negative_date+"</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("internal_negative_code")){
											xml_str = xml_str+ "<"+tag_name+">"+internal_negative_code+"</"+ tag_name+">";
											}
									PL_SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
								
												                            	
									}
								
								else if((tag_name.equalsIgnoreCase("external_blacklist_flag")||tag_name.equalsIgnoreCase("external_blacklist_date")||tag_name.equalsIgnoreCase("external_blacklist_code")) && Call_name.equalsIgnoreCase("DECTECH")){
										PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");
									String ParentWI_Name = formObject.getNGValue("Parent_WIName");
									String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where (cif_wi_name='"+ParentWI_Name+"' or cif_wi_name='"+formObject.getWFWorkitemName()+"') and cif_searchType = 'External'";
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ "+squeryBlacklist);
									List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
									String External_blacklist =  "";
									String External_blacklist_date =  "";
									String External_blacklist_code =  "";
										
									if (Blacklist!=null && Blacklist.size()>0){		
										External_blacklist =  Blacklist.get(0).get(0);
										External_blacklist_date =  Blacklist.get(0).get(1);
										External_blacklist_code =  Blacklist.get(0).get(2);
									}
									String xml_str =  int_xml.get(parent_tag);
									if(tag_name.equalsIgnoreCase("external_blacklist_flag")){
										xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
										}
										else if(tag_name.equalsIgnoreCase("external_blacklist_date")){
											xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_date+"</"+ tag_name+">";
											}
										else if(tag_name.equalsIgnoreCase("external_blacklist_code")){
											xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_code+"</"+ tag_name+">";
											}
									
									PL_SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								
											                            	
								}
							      else if(tag_name.equalsIgnoreCase("ApplicationDetails") && (Call_name.equalsIgnoreCase("DECTECH"))){
							    	  PL_SKLogger.writeLog("inside 1st if","inside DECTECH req1");
	                               
							    	  PL_SKLogger.writeLog("inside 1st if","inside customer update req2");
	                                     String xml_str = int_xml.get(parent_tag);
	                                     PL_SKLogger.writeLog("RLOS COMMON"," before adding product+ "+xml_str);
	                                     xml_str = xml_str + getProduct_details();
	                                     PL_SKLogger.writeLog("RLOS COMMON"," after adding product+ "+xml_str);
	                                     int_xml.put(parent_tag, xml_str);
	                                                                         
	                         }

							   /*   else if(tag_name.equalsIgnoreCase("age") && Call_name.equalsIgnoreCase("DECTECH")){
							    	  PL_SKLogger.writeLog("RLOS COMMON"," Inside age + ");
										if(int_xml.containsKey(parent_tag))
										{
											//by aman for getting difference between 2 dates in YY.MM format.
											String DOb=formObject.getNGValue("cmplx_Customer_DOb");
											String age = getYearsDifference(formObject,"cmplx_Customer_DOb");		
											String xml_str = int_xml.get(parent_tag);
											xml_str = xml_str + "<"+tag_name+">"+age
											+"</"+ tag_name+">";

											PL_SKLogger.writeLog("RLOS COMMON"," after adding age+ "+xml_str);
											int_xml.put(parent_tag, xml_str);
										}		                            	
									}
								*/
							//Code Commented to directly take the value from the textbox itself
							
							      /*			else if(tag_name.equalsIgnoreCase("LOS") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
										//PL_SKLogger.writeLog("RLOS COMMON"," LOS length+ "+LOS.length());
										PL_SKLogger.writeLog("RLOS COMMON"," value of LOS "+LOS);
									try{	
									if (LOS!=null){
											PL_SKLogger.writeLog("RLOS COMMON"," after adding los+ "+LOS);
										if (LOS.length()!=0){
											if (LOS.length()==1){
												LOS="0"+LOS+".00";
											}
											else{
												LOS=LOS+".00";
											}
										}
									
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+LOS
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding los+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
									}	
									catch (Exception e){
										PL_SKLogger.writeLog("RLOS COMMON"," after adding los+ "+new PersonalLoanSCommonCode().printException(e));
										
										
									}
								}
								}
								else if(tag_name.equalsIgnoreCase("resident_flag") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String resident_flag=formObject.getNGValue("cmplx_Customer_RESIDENTNONRESIDENT");
											
										String xml_str = int_xml.get(parent_tag);
										if (resident_flag!=null){
											if (resident_flag.equalsIgnoreCase("Resident")){
												resident_flag="Y";
											}
											else{
												resident_flag="N";
											}
										}
										xml_str = xml_str + "<"+tag_name+">"+resident_flag
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_RESIDENTNONRESIDENT+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
										
									}		                            	
								}*/
								else if(tag_name.equalsIgnoreCase("cust_name") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
										String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
										String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");
										
										String full_name=first_name+" "+middle_name+""+last_name;
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+full_name
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
										
									}		                            	
								}
	                         	
								else if(tag_name.equalsIgnoreCase("ref_phone_no") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										PL_SKLogger.writeLog("RLOS COMMON"," INSIDE ref_phone_no+ ");
										int count=formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
										String ref_phone_no="";
										String ref_relationship="";
										PL_SKLogger.writeLog("RLOS COMMON"," INSIDE ref_phone_no+ "+count);
										if (count != 0){
										ref_phone_no=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,4);
										ref_relationship=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,2);
										PL_SKLogger.writeLog("RLOS COMMON"," after adding ref_phone_no+ "+ref_phone_no);
										PL_SKLogger.writeLog("RLOS COMMON"," after adding ref_relationship+ "+ref_relationship);
										}
										
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+ref_phone_no
										+"</"+ tag_name+"><ref_relationship>"+ref_relationship+"</ref_relationship>";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
										
									}		                            	
								}
	                         
								else if(tag_name.equalsIgnoreCase("confirmed_in_job") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");
										
										if (confirmedinjob!=null){
											if (confirmedinjob.equalsIgnoreCase("true")){
												confirmedinjob="Y";
											}
											else{
												confirmedinjob="N";
											}
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+confirmedinjob
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
										}
									}		                            	
								}
								else if(tag_name.equalsIgnoreCase("included_pl_aloc") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String included_pl_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");
										
										if (included_pl_aloc!=null){
											if (included_pl_aloc.equalsIgnoreCase("true")){
												included_pl_aloc="Y";
											}
											else{
												included_pl_aloc="N";
											}
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+included_pl_aloc
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding included_pl_aloc+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
									}
								}
								else if(tag_name.equalsIgnoreCase("included_cc_aloc") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String included_cc_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");
										
										if (included_cc_aloc!=null){
											if (included_cc_aloc.equalsIgnoreCase("true")){
												included_cc_aloc="Y";
											}
											else{
												included_cc_aloc="N";
											}
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+included_cc_aloc
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
									}
								}
								else if(tag_name.equalsIgnoreCase("vip_flag") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String vip_flag=formObject.getNGValue("cmplx_Customer_VIPFlag");
										
										
											if (vip_flag.equalsIgnoreCase("true")){
												vip_flag="Y";
											}
											else{
												vip_flag="N";
											}
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+vip_flag
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}		                            	
								}
								else if(tag_name.equalsIgnoreCase("standing_instruction") && Call_name.equalsIgnoreCase("DECTECH")){
									PL_SKLogger.writeLog("RLOS COMMON"," iNSIDE standing_instruction+ ");
									String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE Child_wi='"+formObject.getWFWorkitemName()+"'";
									List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
									PL_SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ "+squerynoc);
									String standing_instruction="";
									standing_instruction=NOC.get(0).get(0);
									if (NOC!=null && NOC.size()>0){
											String xml_str =  int_xml.get(parent_tag);
										if (standing_instruction.equalsIgnoreCase("0")){
											standing_instruction="N";
										}
										else{
											standing_instruction="Y";
										}
										
										xml_str = xml_str+ "<"+tag_name+">"+standing_instruction
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding standing_instruction+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
								
									}
									
								}
							/*	else if(tag_name.equalsIgnoreCase("avg_credit_turnover_6") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{	
										int avg_credit_turnover6th=0;
										String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
										String avg_credit_turnover6=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
										if ((!avg_credit_turnover6.equalsIgnoreCase(null))&&(!avg_credit_turnover6.equalsIgnoreCase(""))){
										 avg_credit_turnover6th=Integer.parseInt(avg_credit_turnover6);
										}
										//String avg_credit_turnover3="";
										String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
										String avg_bal6=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
										int avg_bal6th=Integer.parseInt(avg_bal6);
										if (avg_credit_turnover_freq.equalsIgnoreCase("Annually")){
											avg_credit_turnover6th=avg_credit_turnover6th/2;
										}
										else if (avg_credit_turnover_freq.equalsIgnoreCase("Monthly")){
											avg_credit_turnover6th=6*avg_credit_turnover6th;
										}
										else if (avg_credit_turnover_freq.equalsIgnoreCase("Quarterly")){
											avg_credit_turnover6th=2*avg_credit_turnover6th;
										}
										if (avg_bal_freq.equalsIgnoreCase("Annually")){
											avg_bal6th=avg_bal6th/2;
										}
										else if (avg_bal_freq.equalsIgnoreCase("Monthly")){
											avg_bal6th=6*avg_bal6th;
										}
										else if (avg_bal_freq.equalsIgnoreCase("Quarterly")){
											avg_bal6th=2*avg_bal6th;
										}	
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover6th
										+"</"+ tag_name+"><avg_credit_turnover_3>"+avg_credit_turnover6th/2+"</avg_credit_turnover_3><avg_bal_3>"+avg_bal6th+"</avg_bal_3><avg_bal_6>"+avg_bal6th/2+"</avg_bal_6>";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}		                            	
								}
					*/		/*	else if(tag_name.equalsIgnoreCase("aggregate_exposed") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String aeQuery = "SELECT A.CreditLimit, B.TotalOutstandingAmt,C.TotalAmount,D.TotalAmt FROM ng_RLOS_CUSTEXPOSE_CardDetails a FULL OUTER JOIN ng_RLOS_CUSTEXPOSE_LoanDetails B ON A.Wi_Name=B.Wi_Name FULL OUTER JOIN ng_rlos_cust_extexpo_CardDetails C ON C.Wi_Name=A.Wi_Name FULL OUTER JOIN ng_rlos_cust_extexpo_LoanDetails D ON D.Wi_Name=A.Wi_Name WHERE A.Wi_Name = '"+formObject.getWFWorkitemName()+"'";
										PL_SKLogger.writeLog("aggregate_exposed sQuery"+aeQuery, "");
										List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);
										PL_SKLogger.writeLog("aggregate_exposed list size"+aggregate_exposed.size(), "");
										float CreditLimit;
										float TotalOutstandingAmt;
										float TotalAmount;
										float TotalAmt;
										PL_SKLogger.writeLog("outsidefor list ", "values");
										CreditLimit=0.0f;
										TotalOutstandingAmt=0.0f;
										TotalAmount=0.0f;
										TotalAmt=0.0f;
										float Total;
										PL_SKLogger.writeLog("outsidefor list ", "values");
										Total=0.0f;
										
										for (int i = 0; i<aggregate_exposed.size();i++){
											
											
											if(aggregate_exposed.get(i).get(0)!=null && !aggregate_exposed.get(i).get(0).isEmpty() &&  !aggregate_exposed.get(i).get(0).equals("") && !aggregate_exposed.get(i).get(0).equalsIgnoreCase("null") ){
													CreditLimit = CreditLimit+  Float.parseFloat(aggregate_exposed.get(i).get(0));
											}
											if(aggregate_exposed.get(i).get(1)!=null && !aggregate_exposed.get(i).get(1).isEmpty() &&  !aggregate_exposed.get(i).get(1).equals("") && !aggregate_exposed.get(i).get(1).equalsIgnoreCase("null") ){
													TotalOutstandingAmt =TotalOutstandingAmt + Float.parseFloat(aggregate_exposed.get(i).get(1));
											}
											if(aggregate_exposed.get(i).get(2)!=null && !aggregate_exposed.get(i).get(2).isEmpty() &&  !aggregate_exposed.get(i).get(2).equals("") && !aggregate_exposed.get(i).get(2).equalsIgnoreCase("null") ){
													TotalAmount =TotalAmount+  Float.parseFloat(aggregate_exposed.get(i).get(2));
											}
											if(aggregate_exposed.get(i).get(3)!=null && !aggregate_exposed.get(i).get(3).isEmpty() &&  !aggregate_exposed.get(i).get(3).equals("") && !aggregate_exposed.get(i).get(3).equalsIgnoreCase("null") ){
													TotalAmt = TotalAmt+ Float.parseFloat(aggregate_exposed.get(i).get(3));
											}
											
										}
										Total=	CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt;
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+Total
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding aggregate_exposed+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}		                            	
								}
					*/			
								else if(tag_name.equalsIgnoreCase("accomodation_provided") && Call_name.equalsIgnoreCase("DECTECH")){
									if(int_xml.containsKey(parent_tag))
									{
										String accomodation_provided=formObject.getNGValue("cmplx_IncomeDetails_Accomodation");
										
										if (accomodation_provided!=null){
											if (accomodation_provided.equalsIgnoreCase("Yes")){
												accomodation_provided="Y";
											}
											else{
												accomodation_provided="N";
											}
										
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+accomodation_provided
										+"</"+ tag_name+">";

										PL_SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
									}
								}
								else if(tag_name.equalsIgnoreCase("AccountDetails") && Call_name.equalsIgnoreCase("DECTECH")){
									String squeryLoan="select count(*) from ng_rlos_custexpose_loandetails where Wi_Name='"+formObject.getWFWorkitemName()+"'";
									List<List<String>> LoanCount=formObject.getDataFromDataSource(squeryLoan);
									String squeryCard="select count(*) from ng_rlos_custexpose_Carddetails where Wi_Name='"+formObject.getWFWorkitemName()+"'";
									List<List<String>> CardCount=formObject.getDataFromDataSource(squeryCard);
										if(int_xml.containsKey(parent_tag))
									{
										String xml_str = int_xml.get(parent_tag);
										PL_SKLogger.writeLog("RLOS COMMON"," before adding internal liability+ "+xml_str);
										xml_str = xml_str + getInternalLiabDetails();
										PL_SKLogger.writeLog("RLOS COMMON"," after internal liability+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
									
								}
								else if(tag_name.equalsIgnoreCase("InternalBureau") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauData+ "+xml_str);
									String temp = InternalBureauData();
									if(!temp.equalsIgnoreCase("")){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str+ temp;
										PL_SKLogger.writeLog("RLOS COMMON"," after InternalBureauData+ "+xml_str);
										int_xml.get(parent_tag);
										int_xml.put(parent_tag, xml_str);
									}


								}
								else if(tag_name.equalsIgnoreCase("InternalBouncedCheques") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding InternalBouncedCheques+ "+xml_str);
									String temp = InternalBouncedCheques();
									if(!temp.equalsIgnoreCase("")){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str+ temp;
										PL_SKLogger.writeLog("RLOS COMMON"," after InternalBouncedCheques+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}

								}
								else if(tag_name.equalsIgnoreCase("InternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauIndividualProducts+ "+xml_str);
									String temp = InternalBureauIndividualProducts();
									if(!temp.equalsIgnoreCase("")){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str+ temp;
										PL_SKLogger.writeLog("RLOS COMMON"," after InternalBureauIndividualProducts+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}

								}
								else if(tag_name.equalsIgnoreCase("InternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauPipelineProducts+ "+xml_str);
									String temp = InternalBureauPipelineProducts();
									if(!temp.equalsIgnoreCase("")){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str+ temp;
										PL_SKLogger.writeLog("RLOS COMMON"," after InternalBureauPipelineProducts+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}

								}
								else if(tag_name.equalsIgnoreCase("ExternalBureau") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureau+ "+xml_str);
									String temp = ExternalBureauData();
									if(!temp.equalsIgnoreCase("")){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str+ temp;
										PL_SKLogger.writeLog("RLOS COMMON"," after ExternalBureau+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}

								}
								else if(tag_name.equalsIgnoreCase("ExternalBouncedCheques") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding ExternalBouncedCheques+ "+xml_str);
									String temp = ExternalBouncedCheques();
									if(!temp.equalsIgnoreCase("")){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str+ temp;
										PL_SKLogger.writeLog("RLOS COMMON"," after ExternalBouncedCheques+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}                    	
								}
								else if(tag_name.equalsIgnoreCase("ExternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureauIndividualProducts+ "+xml_str);
									String temp =  ExternalBureauIndividualProducts();
									PL_SKLogger.writeLog("RLOS COMMON"," value of temp to be adding temp+ "+temp);
									String Manual_add_Liab =  ExternalBureauManualAddIndividualProducts();

									if((!temp.equalsIgnoreCase("")) || (!Manual_add_Liab.equalsIgnoreCase(""))){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str + temp + Manual_add_Liab;
										PL_SKLogger.writeLog("RLOS COMMON"," after ExternalBureauIndividualProducts+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	                            	
								}
								else if(tag_name.equalsIgnoreCase("ExternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureauPipelineProducts+ "+xml_str);
									String temp =  ExternalBureauPipelineProducts();
									if(!temp.equalsIgnoreCase("")){
										if (xml_str==null){
											PL_SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
											xml_str="";
										}
										xml_str =  xml_str+ temp;
										PL_SKLogger.writeLog("RLOS COMMON"," after ExternalBureauPipelineProducts+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}                        	
								}
							//Aman Code changes ended for custom coding of dectech code
							//code change for Customer update to add address tag.
							else if(tag_name.equalsIgnoreCase("ApplicationID") && Call_name.equalsIgnoreCase("NEW_LOAN_REQ")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = "<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationID:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("ApplicationNumber") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationNumber:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("ApplicationDate") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = "<"+tag_name+">"+"2017-06-06"+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationDate:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("VIPFlag") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");

								String vip_flag="N";
								if(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")){
									vip_flag="Y";
								}	          	

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+vip_flag+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding VIP flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("PhnDet") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet>";

								PL_SKLogger.writeLog("PL COMMON"," SourcingDate: "+xml_str);
								int_xml.put(parent_tag, xml_str);                                    
							}
							else if(tag_name.equalsIgnoreCase("OECDDet") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
								PL_SKLogger.writeLog("PL Common","inside OECDDet inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + getCustOECD_details(Call_name);
								PL_SKLogger.writeLog("PL COMMON"," after adding OECDDet: "+xml_str);
								int_xml.put(parent_tag, xml_str);                                    
							}
							else if(tag_name.equalsIgnoreCase("PhnDet") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet>";

								PL_SKLogger.writeLog("PL COMMON"," SourcingDate: "+xml_str);
								int_xml.put(parent_tag, xml_str);                                    
							}
							else if(tag_name.equalsIgnoreCase("DSAId") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str =xml_str+ "<"+tag_name+">"+"99D1243"+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding DSAId:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
/*							else if(tag_name.equalsIgnoreCase("PhnLocalCode") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
							PL_SKLogger.writeLog("inside PL Common generate xml","PhnLocalCode to substring");
								String xml_str = int_xml.get(parent_tag);
								String phn_no = formObject.getNGValue(form_control);
								if((!phn_no.equalsIgnoreCase("")) && phn_no.indexOf("00971")>-1){
									phn_no = phn_no.substring(5);
								}

								xml_str = xml_str+"<"+tag_name+">"+phn_no+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationID:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}*/
							else if(tag_name.equalsIgnoreCase("SourcingDate") && Call_name.equalsIgnoreCase("NEW_LOAN_REQ")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<SourcingDate>2015-04-15</SourcingDate>";

								PL_SKLogger.writeLog("PL COMMON"," SourcingDate: "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("AddrDet") && (Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ") || Call_name.equalsIgnoreCase("CARD_NOTIFICATION"))){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									PL_SKLogger.writeLog("inside 1st if","inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getCustAddress_details(Call_name);
									PL_SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("LienDetails") && (Call_name.equalsIgnoreCase("CARD_NOTIFICATION")||Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST"))){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									PL_SKLogger.writeLog("inside 1st if","inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getLien_details(Call_name);
									PL_SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("ContactDetails") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									PL_SKLogger.writeLog("inside 1st if","inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before contact details+ "+xml_str);
									xml_str = xml_str + getcontact_details();
									PL_SKLogger.writeLog("RLOS COMMON"," after adding contact details+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							//
							//code change for Customer update to add address tag.
							else if(tag_name.equalsIgnoreCase("MaritalStatus") && (Call_name.equalsIgnoreCase("DEDUP_SUMMARY")||Call_name.equalsIgnoreCase("BLACKLIST_DETAILS"))){
								String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+marrital_code
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("VIPFlg") && Call_name.equalsIgnoreCase("NEW_CARD_REQ")){
								String vip_flag="N";
								if(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")){
									vip_flag="Y";
								}
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+vip_flag
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding VIP flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("ProcessingUserId") && Call_name.equalsIgnoreCase("NEW_CARD_REQ")){
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("ProcessingDate") && Call_name.equalsIgnoreCase("NEW_CARD_REQ")){
								String xml_str = int_xml.get(parent_tag);
								SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
								xml_str = xml_str + "<"+tag_name+">"+sdf1.format(new Date())
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("SalaryDate") && (Call_name.equalsIgnoreCase("CARD_NOTIFICATION") || Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST"))){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
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

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationID:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("ProcessDate") && Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
								String xml_str = int_xml.get(parent_tag);
								SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
								xml_str = xml_str + "<"+tag_name+">"+sdf1.format(new Date())
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("MonthlyCrTrnOvrAmt") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+"1000"
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("CardNumber") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+"5239266299926203"
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("CardProductType") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+"KALYAN-SEC"
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("DispatchMode") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								String xml_str = int_xml.get(parent_tag);
								xml_str = "<"+tag_name+">"+"ByCourier"
								+"</"+ tag_name+">";	          	
								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("ProcessedBy") && Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
								String xml_str = int_xml.get(parent_tag);

								xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
								+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("MinorFlag") && ((Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails"))||(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")))){
								if(int_xml.containsKey(parent_tag))
								{
									int Age = Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"));
									String age_flag = "N";
									if(Age<18)
										age_flag="Y";
									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+age_flag
									+"</"+ tag_name+">";

									PL_SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("NonResidentFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									String res_flag ="N";

									if(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")){
										res_flag="Y";
									}

									xml_str = xml_str + "<"+tag_name+">"+res_flag
									+"</"+ tag_name+">";

									PL_SKLogger.writeLog("RLOS COMMON"," after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("RecipientAddress") && Call_name.equalsIgnoreCase("CHEQUE_BOOK_ELIGIBILITY") ){
								int add_len=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
								String add_res_val ="";
								String xml_str = int_xml.get(parent_tag);
								if(add_len>0){
									for(int i=0;i<add_len;i++){
										PL_SKLogger.writeLog("selecting Emirates of residence: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
										if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("Home")){
											formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
											add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+" "+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);
										}
									}
									xml_str = xml_str + "<"+tag_name+">"+add_res_val
									+"</"+ tag_name+">";

									PL_SKLogger.writeLog("RLOS COMMON"," after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}
							}
							// changes merged by yash on 5/7/2017
							else if(tag_name.equalsIgnoreCase("AddrDet") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									PL_SKLogger.writeLog("inside 1st if","inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getCustAddress_details();
									PL_SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                                       
							}

							else if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
							{
								PL_SKLogger.writeLog("inside 1st if","inside 1st if");
								if(int_xml.containsKey(parent_tag))
								{
									PL_SKLogger.writeLog("inside 1st if","inside 2nd if");
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("inside 1st if","inside 2nd if xml string"+xml_str);
									if(is_repetitive.equalsIgnoreCase("Y") && int_xml.containsKey(tag_name)){
										PL_SKLogger.writeLog("inside 1st if","inside 3rd if xml string");
										xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
										System.out.println("value after adding "+ Call_name+": "+xml_str);
										PL_SKLogger.writeLog("inside 1st if","inside 3rd if xml string xml string"+xml_str);
										int_xml.remove(tag_name);
										int_xml.put(tag_name, xml_str);
										PL_SKLogger.writeLog("inside 1st if","inside 3rd if xml string xml string int_xml");
									}
									else{
										PL_SKLogger.writeLog("inside else of parent tag","value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
										PL_SKLogger.writeLog("","valuie of form control: "+formObject.getNGValue(form_control));
										if(form_control.trim().equalsIgnoreCase("") && default_val.trim().equalsIgnoreCase("")){
											PL_SKLogger.writeLog("inside if added by me","inside");
											xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											PL_SKLogger.writeLog("added by xml","xml_str"+xml_str);
										}
										else if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).trim().equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null")))
										{
											PL_SKLogger.writeLog("inside else of parent tag 1","form_control_val"+ form_control_val);
											if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
												form_control_val = formObject.getNGValue(form_control).toUpperCase();
											}
											else
												form_control_val = formObject.getNGValue(form_control);

											if(!data_format12.equalsIgnoreCase("text")){
												String[] format_arr = data_format12.split(":");
												String format_name = format_arr[0];
												String format_type = format_arr[1];
												PL_SKLogger.writeLog("","format_name"+format_name);
												PL_SKLogger.writeLog("","format_type"+format_type);

												if(format_name.equalsIgnoreCase("date")){
													DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
													DateFormat df_new = new SimpleDateFormat(format_type);

													try {
														startDate = df.parse(form_control_val);
														form_control_val = df_new.format(startDate);
														PL_SKLogger.writeLog("RLOSCommon#Create Input"," date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

													} catch (ParseException e) {
														PL_SKLogger.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+e.getMessage());
														e.printStackTrace();
													}
													catch (Exception e) {
														PL_SKLogger.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+e.getMessage());
														e.printStackTrace();
													}
												}
												else if(format_name.equalsIgnoreCase("number")){
													if(form_control_val.contains(",")){
														form_control_val = form_control_val.replace(",", "");
													}
													
												}
												//change here for other input format

											}
											PL_SKLogger.writeLog("inside else of parent tag","form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											PL_SKLogger.writeLog("inside else of parent tag xml_str","xml_str"+ xml_str);
										}

										else if(default_val==null || default_val.trim().equalsIgnoreCase("")){
											PL_SKLogger.writeLog("#RLOS Common GenerateXML IF part","no value found for tag name: "+ tag_name);
										}
										else{
											PL_SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","");

											form_control_val = default_val;

											PL_SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											PL_SKLogger.writeLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1","xml_str"+ xml_str);

										}
										//code change for to remove docdect incase ref no is not present start	                                       
										if(tag_name.equalsIgnoreCase("DocumentRefNumber") && parent_tag.equalsIgnoreCase("Document") && form_control_val.trim().equalsIgnoreCase("")){
											if(xml_str.contains("</Document>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</Document>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if(tag_name.equalsIgnoreCase("DocRefNum") && parent_tag.equalsIgnoreCase("DocDetails") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</DocDetails>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDetails>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if(tag_name.equalsIgnoreCase("DocNo") && parent_tag.equalsIgnoreCase("DocDet") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</DocDet>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDet>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										/*else if(tag_name.equalsIgnoreCase("PhnLocalCode") && parent_tag.equalsIgnoreCase("PhnDetails") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</PhnDetails>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}*/
										
										else if(tag_name.equalsIgnoreCase("PhnLocalCode") && parent_tag.equalsIgnoreCase("PhnDetails") && form_control_val.equalsIgnoreCase("")){
                                            if(xml_str.contains("</PhnDetails>")){
                                            	PL_SKLogger.writeLog("PL_common", "Inside PhnDetails condition to remove RVC tag </PhnDetails> contanied");
                                                  xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
                                                  int_xml.put(parent_tag, xml_str);
                                            }
                                            else{
                                            	PL_SKLogger.writeLog("PL_common", "Inside PhnDetails condition to remove PhnDetails tag <PhnDetails> tag not contanied");
                                                  int_xml.remove(parent_tag);
                                                  Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
                                                  PL_SKLogger.writeLog("itr of hashmap","itr"+itr);
                                                  while (itr.hasNext())
                                                  {
                                                        Map.Entry<String, String> entry =  itr.next();
                                                        PL_SKLogger.writeLog("entry of hashmap","entry"+entry+ " entry.getValue(): "+ entry.getValue());
                                                        if(entry.getValue().contains("PhnDetails")){
                                                              String key_val = entry.getValue();
                                                              key_val = key_val.replace("<PhnDetails>", "");
                                                              key_val = key_val.replace("</PhnDetails>", "");
                                                              int_xml.put(entry.getKey(), key_val);
                                                              PL_SKLogger.writeLog("PL_common","KEY: entry.getKey()" + " Updated value: "+key_val);
                                                              //final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
                                                              PL_SKLogger.writeLog("PL_common ","PhnDetails removed from parent key");
                                                              break;
                                                        }

                                                  } 
                                            }
                                      }
										//for email for CIF update
										
										else if(tag_name.equalsIgnoreCase("Email") && parent_tag.equalsIgnoreCase("EmlDet") && form_control_val.equalsIgnoreCase("")){
                                            if(xml_str.contains("</EmlDet>")){
                                            	PL_SKLogger.writeLog("PL_common", "Inside EmlDet condition to remove RVC tag </EmlDet> contanied");
                                                  xml_str = xml_str.substring(0, xml_str.lastIndexOf("</EmlDet>"));
                                                  int_xml.put(parent_tag, xml_str);
                                            }
                                            else{
                                            	PL_SKLogger.writeLog("PL_common", "Inside EmlDet condition to remove PhnDetails tag <EmlDet> tag not contanied");
                                                  int_xml.remove(parent_tag);
                                                  Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
                                                  PL_SKLogger.writeLog("itr of hashmap","itr"+itr);
                                                  while (itr.hasNext())
                                                  {
                                                        Map.Entry<String, String> entry =  itr.next();
                                                        PL_SKLogger.writeLog("entry of hashmap","entry"+entry+ " entry.getValue(): "+ entry.getValue());
                                                        if(entry.getValue().contains("EmlDet")){
                                                              String key_val = entry.getValue();
                                                              key_val = key_val.replace("<EmlDet>", "");
                                                              key_val = key_val.replace("</EmlDet>", "");
                                                              int_xml.put(entry.getKey(), key_val);
                                                              PL_SKLogger.writeLog("PL_common","KEY: entry.getKey()" + " Updated value: "+key_val);
                                                              //final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
                                                              PL_SKLogger.writeLog("PL_common ","EmlDet removed from parent key");
                                                              break;
                                                        }

                                                  } 
                                            }
                                      }

										else if(tag_name.equalsIgnoreCase("Email") && parent_tag.equalsIgnoreCase("EmlDet") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</EmlDet>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</EmlDet>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if(tag_name.equalsIgnoreCase("IncomeAmount") && parent_tag.equalsIgnoreCase("OtherIncomeDetails") && form_control_val.equalsIgnoreCase("")){
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
										PL_SKLogger.writeLog("else of generatexml","RLOSCommon"+"inside else"+xml_str);

									}

								}
								else{
									String new_xml_str ="";
									PL_SKLogger.writeLog("inside else of parent tag main 2","value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
									PL_SKLogger.writeLog("","valuie of form control: "+formObject.getNGValue(form_control));
									if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).trim().equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null"))){
										PL_SKLogger.writeLog("inside else of parent tag 1","form_control_val"+ form_control_val);
										if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
											form_control_val = formObject.getNGValue(form_control).toUpperCase();
										}
										else
											form_control_val = formObject.getNGValue(form_control);
										if(!data_format12.equalsIgnoreCase("text")){
											String[] format_arr = data_format12.split(":");
											String format_name = format_arr[0];
											String format_type = format_arr[1];
											if(format_name.equalsIgnoreCase("date")){
												DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
												DateFormat df_new = new SimpleDateFormat(format_type);
												// java.util.Date startDate;
												try {
													startDate = df.parse(form_control_val);
													form_control_val = df_new.format(startDate);
													PL_SKLogger.writeLog("RLOSCommon#Create Input"," date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

												} catch (ParseException e) {
													PL_SKLogger.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+e.getMessage());
													e.printStackTrace();
												}
												catch (Exception e) {
													PL_SKLogger.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+e.getMessage());
													e.printStackTrace();
												}
											}
											else if(format_name.equalsIgnoreCase("number")){
												if(form_control_val.contains(",")){
													form_control_val = form_control_val.replace(",", "");
												}

											}
											//change here for other input format

										}
										PL_SKLogger.writeLog("inside else of parent tag","form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										PL_SKLogger.writeLog("inside else of parent tag xml_str","new_xml_str"+ new_xml_str);
									}

									else if(default_val==null || default_val.trim().equalsIgnoreCase("")){
										if(int_xml.containsKey(parent_tag)|| is_repetitive.equalsIgnoreCase("Y")){
											new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
										}
										PL_SKLogger.writeLog("#RLOS Common GenerateXML Inside Else Part","no value found for tag name: "+ tag_name);
									}
									else{
										PL_SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","");
										form_control_val = default_val;
										PL_SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										PL_SKLogger.writeLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1","xml_str"+ new_xml_str);

									}
									int_xml.put(parent_tag, new_xml_str);
									PL_SKLogger.writeLog("else of generatexml","RLOSCommon"+"inside else"+new_xml_str);

								}
							}

						}


						final_xml=final_xml.append("<").append(parentTagName).append(">");
						PL_SKLogger.writeLog("RLOS","Final XMLold--"+final_xml);

						Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
						PL_SKLogger.writeLog("itr of hashmap","itr"+itr);
						while (itr.hasNext())
						{
							Map.Entry<String, String> entry =  itr.next();
							PL_SKLogger.writeLog("entry of hashmap","entry"+entry);
							if(final_xml.indexOf((entry.getKey()))>-1){
								PL_SKLogger.writeLog("RLOS","itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
								PL_SKLogger.writeLog("value of final xml","final_xml"+final_xml);
								itr.remove();
							}

						}    
						final_xml=final_xml.append("</").append(parentTagName).append(">");
						PL_SKLogger.writeLog("RLOS Common before clean xml",final_xml.toString());
						final_xml = new StringBuffer( Clean_Xml(final_xml.toString()));
						PL_SKLogger.writeLog("RLOS Common After clean xml",final_xml.toString());
						
						PL_SKLogger.writeLog("FInal XMLnew is: ", final_xml.toString());
						final_xml.insert(0, header);
						final_xml.append(footer);
						PL_SKLogger.writeLog("FInal XMLnew with header: ", final_xml.toString());
						formObject.setNGValue("Is_"+callName,"Y");
						PL_SKLogger.writeLog("value of "+callName+" Flag: ",formObject.getNGValue("Is_"+callName));

						//added above code
						try{


						}
						catch(Exception e){

						}
						cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
						PL_SKLogger.writeLog("$$outputgGridtXML ","cabinetName "+cabinetName);
						wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
						ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
						PL_SKLogger.writeLog("$$outputgGridtXML ","ActivityName "+ws_name);
						sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
						userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
						PL_SKLogger.writeLog("$$outputgGridtXML ","userName "+userName);
						PL_SKLogger.writeLog("$$outputgGridtXML ","sessionID "+sessionID);



						String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
						List<List<String>> outputMQXML=formObject.getNGDataFromDataCache(sMQuery);
						PL_SKLogger.writeLog("$$outputgGridtXML ","sMQuery "+sMQuery);
						if(!outputMQXML.isEmpty()){
							PL_SKLogger.writeLog("$$outputgGridtXML ",outputMQXML.get(0).get(0)+","+outputMQXML.get(0).get(1));
							socketServerIP =  outputMQXML.get(0).get(0);
							PL_SKLogger.writeLog("$$outputgGridtXML ","socketServerIP "+socketServerIP);
							socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
							PL_SKLogger.writeLog("$$outputgGridtXML ","socketServerPort "+socketServerPort);
							if(!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0)))
							{
								socket = new Socket(socketServerIP, socketServerPort);
								out = socket.getOutputStream();
								socketInputStream = socket.getInputStream();
								dout = new DataOutputStream(out);
								din = new DataInputStream(socketInputStream);
								mqOutputResponse="";	     
								mqInputRequest= getMQInputXML(sessionID,cabinetName,wi_name,ws_name,userName,final_xml);
								PL_SKLogger.writeLog("$$outputgGridtXML ","mqInputRequest "+mqInputRequest);

								if (mqInputRequest != null && mqInputRequest.length() > 0) 
								{
									int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
									PL_SKLogger.writeLog("Final XML output len: ",outPut_len+"");
									mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
									PL_SKLogger.writeLog("MqInputRequest","Input Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
									dout.write(mqInputRequest.getBytes("UTF-16LE"));
									dout.flush();                
								}
								byte[] readBuffer = new byte[50000];
								int num = din.read(readBuffer);
								boolean wait_flag = true;
								int out_len=0;

								if (num > 0) 
								{
									while(wait_flag){
										PL_SKLogger.writeLog("MqOutputRequest","num :"+num);
										byte[] arrayBytes = new byte[num];
										System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
										mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
										PL_SKLogger.writeLog("MqOutputRequest","inside loop output Response :\n"+mqOutputResponse);
										if(mqOutputResponse.contains("##8##;")){
											String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
											mqOutputResponse = mqOutputResponse_arr[1];
											out_len = Integer.parseInt(mqOutputResponse_arr[0]);
											PL_SKLogger.writeLog("MqOutputRequest","First Output Response :\n"+mqOutputResponse);
											PL_SKLogger.writeLog("MqOutputRequest","Output length :\n"+out_len);
										}
										if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
											wait_flag=false;
										}
										Thread.sleep(100);
										num = din.read(readBuffer);

									}
									// Aman Code added for dectech to replace the &lt and &gt start 13 sept 2017
		             				if(mqOutputResponse.contains("&lt;")){
		             					PL_SKLogger.writeLog("MqOutputRequest","inside for Dectech :\n"+mqOutputResponse);
		                                mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
		                                PL_SKLogger.writeLog("MqOutputRequest","after replacing lt :\n"+mqOutputResponse);
		                                mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
		                                PL_SKLogger.writeLog("MqOutputRequest","after replacing gt :\n"+mqOutputResponse);
		             				}
		             				// Aman Code added for dectech to replace the &lt and &gt end 13 sept 2017
		             					
									PL_SKLogger.writeLog("MqOutputRequest","Final Output Response :\n"+mqOutputResponse);
									socket.close();
									return mqOutputResponse;
								}


							}
							else{
								PL_SKLogger.writeLog("SocketServerIp and SocketServerPort is not maintained ","");
								PL_SKLogger.writeLog("SocketServerIp is not maintained ",socketServerIP);
								PL_SKLogger.writeLog(" SocketServerPort is not maintained ",socketServerPort.toString());
								return "MQ details not maintained";
							}
						}
						else{
							PL_SKLogger.writeLog("SOcket details are not maintained in NG_RLOS_MQ_TABLE table","");
							return "MQ details not maintained";
						}
					}

				}
				else {
					PL_SKLogger.writeLog("Genrate XML: ","Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}


			}
			else{
				PL_SKLogger.writeLog("Genrate XML: ","Entry is not maintained in Master table for : "+callName);
				return "Call not maintained";
			}
		}
		catch (UnknownHostException e) 
		{        
			e.printStackTrace();
			return "0";
		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception ocurred: ",e.getLocalizedMessage());
			System.out.println("$$final_xml: "+final_xml);
			System.out.println("Exception occured in main thread: "+ new PersonalLoanSCommonCode().printException(e));
			return "0";
		}    
		return "";

	}
	
	
	
	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize)
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);

		if (sControlName.equalsIgnoreCase("EMploymentDetails_Button2")) 
			objPickList.setWindowTitle("Search Employer");

		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
		if(result.size()>0){
		objPickList.setHeight(600);
		objPickList.setWidth(800);
		objPickList.setVisible(true);
		objPickList.setSearchEnabled(true);
		objPickList.addPickListListener(new EventListenerHandler(objPickList.getClientId()));
		PL_SKLogger.writeLog("EventListenerHandler: Result Of Query:",result.toString());   
		objPickList.populateData(result);
		formObject = null;
		}
		else{
			throw new ValidatorException(new FacesMessage("No Data Found"));
		}
	}       

	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuffer final_xml) 
	{
		FormContext.getCurrentInstance().getFormConfig( );

		StringBuffer strBuff=new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>"+sessionID+"</SessionId>");
		strBuff.append("<EngineName>"+cabinetName+"</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_pl_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>"+wi_name+"</WI_NAME>");
		strBuff.append("<WS_NAME>"+ws_name+"</WS_NAME>");
		strBuff.append("<USER_NAME>"+userName+"</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);		
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");	
		PL_SKLogger.writeLog("inside getOutputXMLValues","getMQInputXML"+strBuff.toString());
		return strBuff.toString();
	}


	public void valueSetCustomer(String outputResponse)
	{
		PL_SKLogger.writeLog("RLOSCommon valueSetCustomer", "Inside valueSetCustomer():");
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		String Operation_name="";
		String Call_type="";
		XMLParser objXMLParser = new XMLParser();
		try
		{
			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
				PL_SKLogger.writeLog("RLOSCommon valueSetCustomer", "outputXMLHead");
			}
			objXMLParser.setInputXML(outputXMLHead);
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				PL_SKLogger.writeLog("$$response ",response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				PL_SKLogger.writeLog("$$returnDesc ",returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				PL_SKLogger.writeLog("$$returnCode ",returnCode);
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
				//SKLogger.writeLog("$$outputXMLMsg ",outputXMLMsg);
				PL_SKLogger.writeLog("$$outputXMLMsg getOutputXMLValues","check inside getOutputXMLValues");
				//getOutputXMLValues(outputXMLMsg,response);
				getOutputXMLValues(outputXMLMsg,response,Operation_name);
				PL_SKLogger.writeLog("$$outputXMLMsg ","outputXMLMsg");
			}
			//ended by me
			// Changes done to set the value coming from dectech in the grid
			 Call_type= outputResponse.substring(outputResponse.indexOf("<CallType>")+10,outputResponse.indexOf("</CallType>"));
				if	(Call_type!= null){
					if (Call_type.equals("CA")){
						 dectechfromeligbility(outputResponse);
					}
					if (Call_type.equals("PM")){
						 dectechfromeligbility(outputResponse);
					}
				}
				// Changes done to set the value coming from dectech in the grid end 
		}
		catch(Exception e)
		{            
			PL_SKLogger.writeLog("Exception occured in valueSetCustomer method:  ",e.getMessage());
			System.out.println("Exception occured in valueSetCustomer method: "+ e.getMessage());
		}
	}

	public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		try
		{   
			PL_SKLogger.writeLog("inside getOutputXMLValues","inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";

			//  sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"'";
			//code added here
			if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
				PL_SKLogger.writeLog("inside if of operation","operation111"+Operation_name);
				PL_SKLogger.writeLog("inside if of operation","callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"'" ;
				PL_SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
			}
			else{
				sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"'";
			}

			//ended here
			List<List<String>> outputTableXML=formObject.getNGDataFromDataCache(sQuery);


			String[] col_name_arr = col_name.split(",");

			PL_SKLogger.writeLog("$$outputTableXML",outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
			int n=outputTableXML.size();
			PL_SKLogger.writeLog("outputTableXML size: " , n+"");

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
			PL_SKLogger.writeLog("name is doc : ", doc+"");
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
					String form_control = (String) responseFileMap.get("Form_Control");
					String parent_tag = (String) responseFileMap.get("Parent_Tag_Name");
					String fielddbxml_tag = (String) responseFileMap.get("XmlTag_Name");
					String Grid_col_tag = (String) responseFileMap.get("grid_table_xml_tags");
					PL_SKLogger.writeLog(" Grid_col_tag","Grid_col_tag"+Grid_col_tag);
					if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
					{                    
						for (int i = 0; i < nl.getLength(); i++)
						{
							nl.item(i);
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
								PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()"," fielddbxml_tag: "+fielddbxml_tag);
								String indirectMapping = (String) responseFileMap.get("InDirect_Mapping");
								String gridMapping = (String) responseFileMap.get("Grid_Mapping");

								if(gridMapping.equalsIgnoreCase("Y"))
								{
									PL_SKLogger.writeLog("Grid_col_tag_arr","inside indirect mapping");
									if(Grid_col_tag.contains(",")){
										String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
										//String grid_detail_str = nl.item(i).getNodeValue();
										NodeList childnode  = nl.item(i).getChildNodes();
										PL_SKLogger.writeLog("Grid_col_tag_arr","Grid_col_tag_arr: "+Grid_col_tag);   
										PL_SKLogger.writeLog("childnode","childnode"+childnode); 
										List<String> Grid_row = new ArrayList<String>(); 
										Grid_row.clear();

										String flaga="N";
										for(int k = 0;k<Grid_col_tag_arr.length;k++){

											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
													PL_SKLogger.writeLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getNodeName());
													PL_SKLogger.writeLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getTextContent());
													if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("AddrPrefFlag"))
													{
														if(childnode.item(child_node_len).getTextContent().equalsIgnoreCase("Y"))
															Grid_row.add("true");

														else if(childnode.item(child_node_len).getTextContent().equalsIgnoreCase("N"))
															Grid_row.add("false");

													}
													else
														Grid_row.add(childnode.item(child_node_len).getTextContent());
													flaga="Y";
													break;
												}                                            
											}

											if(flaga.equalsIgnoreCase("N") ){
												PL_SKLogger.writeLog("child_node_len ","Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												// SKLogger.writeLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getTextContent());
												Grid_row.add("NA");

											}
											flaga="N";

										}
										//Grid_row.add("true");
										Grid_row.add(formObject.getWFWorkitemName());
										//Grid_col_tag_arr[k]
										//code to add row in grid. and pass Grid_row in that.
										PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added in address grid: "+ Grid_row.toString());

										if(fielddbxml_tag.equalsIgnoreCase("AddrDet")){ 
											formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
											formObject.RaiseEvent("WFSave");
											PL_SKLogger.writeLog("of Part match grid","after WFS Save");
										}
										PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added in Lien details grid: "+ Grid_row.toString());

										PL_SKLogger.writeLog("PL Common# getOutputXMLValues()", "$$AKSHAY$$List to be added : in PartMatch Details"+ Grid_row.toString());
										if(fielddbxml_tag.equalsIgnoreCase("PersonDetails")){ 
											formObject.addItemFromList("cmplx_PartMatch_cmplx_Partmatch_grid", Grid_row);
											formObject.RaiseEvent("WFSave");
											PL_SKLogger.writeLog("of Part match grid","after WFS Save");
											//   	formObject.clear("AppType");
										}
									}
									else{
										PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
									}

								}
								else if(indirectMapping.equalsIgnoreCase("Y")){
									PL_SKLogger.writeLog("Grid_col_tag_arr","inside indirect mapping");
									String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
									//code added
									if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
										PL_SKLogger.writeLog("inside if of operation","operation111"+Operation_name);
										PL_SKLogger.writeLog("inside if of operation","callName111"+col_name);
										sQuery = "SELECT "+col_list+" FROM NG_PL_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
										PL_SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
									}

									else{
										sQuery = "SELECT "+col_list+" FROM NG_PL_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
										PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "query: "+sQuery);
									}
									List<List<String>> outputindirect=formObject.getNGDataFromDataCache(sQuery);
									PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping test tanshu ", "1");
									String col_list_arr[] = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
										PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping test tanshu ", "inside list loop");

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
											PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "inside put map"+x);
										}
										String xmltag_name = gridResponseMap.get("xmltag_name").toString();
										String tag_value = gridResponseMap.get("tag_value").toString();
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
										String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list").toString();
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
										String indirect_form_control = gridResponseMap.get("form_control").toString();
										PL_SKLogger.writeLog("indirect_form_control in string",indirect_form_control );
										String indirect_val = gridResponseMap.get("indirect_val").toString();
										String IS_Master = gridResponseMap.get("IS_Master").toString();
										PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "IS_Master"+IS_Master);
										String Master_Name = gridResponseMap.get("Master_Name").toString();
										PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "Master_Name"+Master_Name);
										PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "all details fetched");
										if(IS_Master.equalsIgnoreCase("Y")){
											String code = nl.item(i).getTextContent();
											PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping code:",code);
											sQuery= "Select description from "+Master_Name+" where Code='"+code+"'";
											PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping code:","Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getNGDataFromDataCache(sQuery);
											String value=query.get(0).get(0);
											PL_SKLogger.writeLog("#query.get(0).get(0)",value );
											PL_SKLogger.writeLog("#RLOS Common Inside indirectMapping code:","Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value,false);
											PL_SKLogger.writeLog("indirect_form_control value",formObject.getNGValue(indirect_form_control));
										}

										else if(indirect_form_control!=null && !indirect_form_control.equalsIgnoreCase("")){
											if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
												PL_SKLogger.writeLog("RLOS common: getOutputXMLValues","Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
												formObject.setNGValue(indirect_form_control,indirect_val,false);
											}
											// System.out.println("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);

										}

										else{
											PL_SKLogger.writeLog("Grid_col_tag_arr","inside indirect mapping part2 else");
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
												PL_SKLogger.writeLog("childnode","childnode"+childnode);
												if(indirect_tag_list.contains(",")){
													PL_SKLogger.writeLog("#RLOS common indirect field values","inside indirect mapping part2 indirect_tag_list with ,");
													String indirect_tag_list_arr[] = indirect_tag_list.split(",");
													String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
															PL_SKLogger.writeLog("#RLOS Common inside child node 1 ", "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(1).getTextContent());
															if(tag_value.equalsIgnoreCase(childnode.item(1).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(1).getNodeName())){
																//for (int child_node_len = 2 ;child_node_len< childnode.getLength();child_node_len++){
																//code changes to parse indirect output tag from 1st index rather than 2
																for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){
															
																	PL_SKLogger.writeLog("#RLOS common: ", "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

																		PL_SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getNodeName());
																		PL_SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getTextContent());
																		PL_SKLogger.writeLog("RLOS common: getOutputXMLValues","");
																		PL_SKLogger.writeLog(""+indirect_formfield_list_arr[k]," :"+childnode.item(child_node_len).getTextContent());
																		formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																	}
																}

															}
														}
													}
													else{
														PL_SKLogger.writeLog("RLOS common: getOutputXMLValues","Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
													} 
												}
												else{
													PL_SKLogger.writeLog("#RLOS common indirect field values","inside indirect mapping part2 indirect_tag_list without ,");
													for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
														if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
																PL_SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getNodeName());
																PL_SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getTextContent());
																PL_SKLogger.writeLog("RLOS common: getOutputXMLValues","");
																PL_SKLogger.writeLog(""+indirect_formfield_list," :"+childnode.item(child_node_len).getTextContent());
																formObject.setNGValue(indirect_formfield_list,childnode.item(child_node_len).getTextContent(),false);
															}
														}

													}
												}


											}
										}     
									}
									//List<List<String>> outputIndirectXML=formObject.getNGDataFromDataCache(sQuery);
									//System.out.println("$$outputIndirectXML "+outputIndirectXML.get(0).get(0)+outputIndirectXML.get(0).get(1)+outputIndirectXML.get(0).get(2));

								}
								if(indirectMapping.equalsIgnoreCase("N") && gridMapping.equalsIgnoreCase("N"))
								{    
									PL_SKLogger.writeLog("check14 " ,"check");
									tagValue = getTagValue(outputXMLMsg,nl.item(i).getNodeName());
									PL_SKLogger.writeLog("Node value ","tagValue:"+tagValue);
									PL_SKLogger.writeLog("Node form_control ","form_control:"+ form_control);

									PL_SKLogger.writeLog("$$tagValue NN  ",tagValue);
									PL_SKLogger.writeLog("$$form_control  NN ",form_control);
									formObject.setNGValue(form_control,tagValue,false);
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
			PL_SKLogger.writeLog("Exception occured in getOutputXMLValues:  ",e.getMessage());

		}
	}

	public static String getTagValue(String xml, String tag) throws ParserConfigurationException, SAXException, IOException 
	{   
		//System.out.println("Tag:"+tag+" XML:"+xml);
		Document doc = getDocument(xml);
		NodeList nodeList = doc.getElementsByTagName(tag);

		int length = nodeList.getLength();
		//System.out.println("NodeList Length: " + length);

		if (length > 0) {
			Node node =  nodeList.item(0);
			//System.out.println("Node : " + node);
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

	public void partMatchValues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
        String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
        String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");
        
        String fullName = fName+" "+mName+" "+lName; 
        formObject.setNGValue("PartMatch_funame",fullName);

		formObject.setNGValue("PartMatch_CIFID",formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("PartMatch_fname",formObject.getNGValue("cmplx_Customer_FIrstNAme"));
		formObject.setNGValue("PartMatch_lname",formObject.getNGValue("cmplx_Customer_LAstNAme"));
		formObject.setNGValue("PartMatch_newpass",formObject.getNGValue("cmplx_Customer_PAssportNo"));
		
		formObject.setNGValue("PartMatch_visafno",formObject.getNGValue("cmplx_Customer_VisaNo"));
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		loadAltContactDetailsCombo();
		
		formObject.setNGValue("PartMatch_mno1",formObject.getNGValue("AlternateContactDetails_MobileNo1"));//Arun (23/10) to autopopulate values here
		formObject.setNGValue("PartMatch_mno2",formObject.getNGValue("AlternateContactDetails_MobileNo2"));//Arun (23/10) to autopopulate values here
		formObject.setNGValue("PartMatch_Dob",formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("PartMatch_EID",formObject.getNGValue("cmplx_Customer_EmiratesID"));
		formObject.setNGValue("PartMatch_nationality",formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("PartMatch_drno",formObject.getNGValue("cmplx_Customer_DLNo"));
		formObject.setNGValue("PartMatch_oldpass",formObject.getNGValue("cmplx_Customer_PAssportNo")); //Arun (07/09/17)

	}

	public static Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException
	{
		// Step 1: create a DocumentBuilderFactory
		DocumentBuilderFactory dbf =
			DocumentBuilderFactory.newInstance();

		// Step 2: create a DocumentBuilder
		DocumentBuilder db = dbf.newDocumentBuilder();

		// Step 3: parse the input file to get a Document object
		Document doc = db.parse(new InputSource(new StringReader(xml)));
		return doc;
	} 
	public String getLien_details(String Call_name){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside getLienDetails_details : ");
		String  lien_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_FinacleCore_liendet_grid");
			PL_SKLogger.writeLog("PL Common java file", "inside Lien details add_row_count+ : "+add_row_count);

			if (add_row_count>=0){
				String Lien_no = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 1); //0
				String Lien_amount = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 2); //0
				String Lien_maturity_date = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 6); //0
				
				if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
				lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
				lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><Amount>"+Lien_amount+"</Amount>";
				lien_xml_str = lien_xml_str+ "<MaturityDate>"+Lien_maturity_date+"</MaturityDate>";
				lien_xml_str = lien_xml_str+ "</LienDetails>";
				}
				else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
				lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
				lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><LienAmount>"+Lien_amount+"</LienAmount>";
				lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
				lien_xml_str = lien_xml_str+ "</LienDetails>";
				}
			}
		}
		catch(Exception e){
			PL_SKLogger.writeLog("PL Common java file", "Exception occured in get lien details method : "+e.getMessage());
		}

		return lien_xml_str;

	}
	public String getcontact_details(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside getContact_details : ");
		String  Contactdetails_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Contactdetails_xml_str = "<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>MobileNumber1</PhoneType><PhnCountryCode>971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH2</PhoneType><PhnCountryCode>00971</PhnCountryCode>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_OFFICENO")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";

		}
		catch(Exception e){
			PL_SKLogger.writeLog("PL Common java file", "Exception occured in get lien details method : "+e.getMessage());
		}

		return Contactdetails_xml_str;

   	 }
   	 
	//added 30/08/2017
	public String getCustOECD_details(String call_name){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+add_row_count);
			if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
				for (int i = 0; i<add_row_count;i++){
					String City_of_Birth = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 3); //0
					String Country_of_Birth=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 2);//1
					String Undocumented_Flag=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 0);//2
					String UndocumentedFlag_Reason=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 1);//3
					add_xml_str = add_xml_str + "<OECDDet><CityOfBirth>"+City_of_Birth+"</CityOfBirth> ";
					add_xml_str = add_xml_str + "<CountryOfBirth>"+Country_of_Birth+"</CountryOfBirth>";
					add_xml_str = add_xml_str + "<CRSUnDocFlg>"+Undocumented_Flag+"</CRSUnDocFlg>";
					add_xml_str = add_xml_str + "<CRSUndocFlgReason>"+UndocumentedFlag_Reason+"</CRSUndocFlgReason></OECDDet>";

				}

			}
			PL_SKLogger.writeLog("RLOSCommon", "OECD tag creation "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			PL_SKLogger.writeLog("PLCommon getCustAddress_details method", "Exception Occure in generate Address XMl"+e.getMessage());
			return add_xml_str;
		}

	}
   	 
   	 //ended 30/08/2017
       	public String getCustAddress_details(String call_name){
    		PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details : ");
    		String  add_xml_str ="";
    		try{
    			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
        		PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+add_row_count);
        		
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
        			PL_SKLogger.writeLog("PLCommon java file", "ADD Type: "+Address_type);
        			if (Address_type.equalsIgnoreCase("HOME")){
        				Address_type="Home Country";
        			}
        			PL_SKLogger.writeLog("PLCommon java file", "ADD Type after: "+Address_type);
        			//added here
        			PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
        			int years=Integer.parseInt(years_in_current_add);
        			//ended here
        			
        			String preferrd="";
        			//Code change to added Effective from and to start
        			String EffectiveFrom="";
        			String EffectiveTo="";
        			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
        			Calendar cal = Calendar.getInstance();
                     EffectiveTo=sdf1.format(cal.getTime());
                     cal.add(Calendar.YEAR, -years);
                     EffectiveFrom=sdf1.format(cal.getTime());
                     PL_SKLogger.writeLog("RLOS value of CurrentDate EffectiveTo",""+EffectiveTo);
                     PL_SKLogger.writeLog("RLOS value of EffectiveFromDate",""+EffectiveFrom);
                    //Code change to added Effective from and to End
                     PL_SKLogger.writeLog("RLOS value of prefered Add: Address Type: "+Address_type," Address pref flag: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11));
        			if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11).equalsIgnoreCase("true"))//10
        				preferrd = "Y";
        			else
        				preferrd = "N";
        			 
        			
        			if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
        				add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
        				//Code change to added Effective from and to start
            			add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
            			add_xml_str = add_xml_str + "<EffectiveTo>"+EffectiveTo+"</EffectiveTo>";
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
        			else if(call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
        				add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
        				add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
            			add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
            			add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
            			add_xml_str = add_xml_str + "<AddressLine4>"+Landmard+"</AddressLine4>";
            			add_xml_str = add_xml_str + "<City>"+city+"</City>";
            			add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
            			add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
                		add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
                		add_xml_str = add_xml_str + "<EffectiveFromDate>"+EffectiveFrom+"</EffectiveFromDate>";
            			add_xml_str = add_xml_str + "<EffectiveToDate>"+EffectiveTo+"</EffectiveToDate>";
            			add_xml_str = add_xml_str + "<NumberOfYears>"+years+"</NumberOfYears>";
            			add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>"
                				+ "</AddrDet>";
            			//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
        			}
        			else if (call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
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
        			//10/08/2017 added for the respective call
        			else if(call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
        				add_xml_str = add_xml_str + "<AddressDetails><AddrType>"+Address_type+"</AddrType><UseExistingAddress>"+"N"+"</UseExistingAddress><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
        				add_xml_str = add_xml_str + "<Addr1>"+flat_Villa+"</Addr1>";
        				add_xml_str = add_xml_str + "<Addr2>"+Building_name+"</Addr2>";
        				add_xml_str = add_xml_str + "<Addr3>"+street_name+"</Addr3>";
        				add_xml_str = add_xml_str + "<Addr4>"+Landmard+"</Addr4>";
        				add_xml_str = add_xml_str + "<Addr5>"+city+"</Addr5>";
        				add_xml_str = add_xml_str + "<PostalCode>"+country+"</PostalCode>";
        				add_xml_str = add_xml_str + "<ZipCode>"+Emirates+"</ZipCode>";
        				add_xml_str = add_xml_str + "<City>"+city+"</City>";
        				add_xml_str = add_xml_str + "<StateProv>"+city+"</StateProv>";
        				add_xml_str = add_xml_str + "<County>"+city+"</County>";
        				add_xml_str = add_xml_str + "<Country>"+country+"</Country>"+ "</AddressDetails>";
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
        		PL_SKLogger.writeLog("RLOSCommon", "Address tag Cration: "+ add_xml_str);
        		return add_xml_str;
    		}
    		catch(Exception e){
    			PL_SKLogger.writeLog("PLCommon getCustAddress_details method", "Exception Occure in generate Address XMl"+e.getMessage());
    			return add_xml_str;
    		}
    		
    		
    	}
       	public static void parseDedupe_summary(String outxml){
       		
       		outxml=outxml.substring(outxml.indexOf("<MQ_RESPONSE_XML>")+17,outxml.indexOf("</MQ_RESPONSE_XML>"));
         //SKLogger.writeLog("$$outputXMLMsg ",outputXMLMsg);
         
       		String tagName= "Customer";		
    		String subTagName= "Document,EmailAddress,PhoneFax,StatusInfo";
    		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
    		int dedupe_row_count = formObject.getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
    		if(dedupe_row_count>0){
    			for(int i=0;i<dedupe_row_count;i++)
					{
    				try {
    					formObject.removeItem("cmplx_PartMatch_cmplx_Partmatch_grid", dedupe_row_count);
					} catch (Exception e) {
						PL_SKLogger.writeLog("PL Common parseDedupe_summary method", "Exception occured while removing data from Grid"+e.getMessage());
					}
					}
    				
    		}
    		
    		String [] valueArr=null;
    		 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
    		 tagValuesMap=getTagData_dedupeSummary(outxml,tagName,subTagName);
    		 Map<String, String> map = tagValuesMap;
    			  for (Map.Entry<String, String> entry : map.entrySet())
    			  {
    				  valueArr=entry.getValue().split("~");
    				  String[] col_name = valueArr[0].split(":,#:");
    				  String[] col_val = valueArr[1].split(":,#:");
    				  String cif_id = "NA";
    				  String CIF_Status= "NA";
    				  String First_name= "NA";
    				  String Last_name= "NA";
    				  String Full_name= "NA";
    				  String Passport_no= "NA";
    				  String old_passport_no= "NA";
    				  String visa_no= "NA";
    				  String mobile_no1= "NA";
    				  String mobile_no2= "NA";
    				  String dOB= "NA";
    				  String Eid= "NA";
    				  String DL= "NA";
    				  String nationality= "NA";
    				  String company_name= "NA";
    				  String TL_no= "NA";
    				  List<String> Grid_row = new ArrayList<String>();
    				  for(int i =0; i<col_name.length;i++){
    					  if(col_name[i].equalsIgnoreCase("CIFID"))
    						  cif_id = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("Suspended")){
    						  CIF_Status = col_val[i];
    						  
    						  if(CIF_Status.equalsIgnoreCase("Y"))
    							  CIF_Status="N";
    						  else
    							  CIF_Status="Y";
    					  }
    					  else if(col_name[i].equalsIgnoreCase("FirstName"))
    						  First_name = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("LastName"))
    						  Last_name = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("FullName"))
    						  Full_name = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("PPT"))
    						  Passport_no = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("OPPT"))
    						  old_passport_no = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("VISA"))
    						  visa_no = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("CELLPH1"))
    						  mobile_no1 = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("HOMEPH1"))
    						  mobile_no2 = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("DateOfBirth")){
    						  dOB = "";
    						  if (col_val[i]!=null && !col_val[i].equalsIgnoreCase("")){
    							  SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
									SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
									try{
										dOB = sdf2.format(sdf1.parse(col_val[i]));
										PL_SKLogger.writeLog("PLCommon ", "Dedupe Summary Date chabge: "+dOB);
									}
									catch(Exception e){
										PL_SKLogger.writeLog("PL Common", "Exception while parsing DOB in Dedupe summary"+e.getMessage());
										dOB="";
									}
    						  }
    						  else{
    							  dOB="";
    						  }
    					  }
    					  else if(col_name[i].equalsIgnoreCase("EMID"))
    						  Eid = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("DRILV"))
    						  DL = col_val[i];
    					  else if(col_name[i].equalsIgnoreCase("Nationality"))
    						  nationality = col_val[i];
    					 /* else if(col_name[i].equalsIgnoreCase("Suspended"))
    						  company_name = col_val[i];*/
				else if(col_name[i].equalsIgnoreCase("TDLIC"))
					TL_no = col_val[i];
			}
			Grid_row.add(cif_id);
			Grid_row.add(CIF_Status);
			Grid_row.add(First_name);
			Grid_row.add(Last_name);
			Grid_row.add(Full_name);
			Grid_row.add(Passport_no);
			Grid_row.add(old_passport_no);
			Grid_row.add(visa_no);
			Grid_row.add(mobile_no1);
			Grid_row.add(mobile_no2);
			//Grid_row.add("");
			Grid_row.add(dOB);		// disha P2 - 26 - dob missing in part match grid
			Grid_row.add(Eid);
			Grid_row.add(DL);
			Grid_row.add(nationality);
			Grid_row.add(company_name);
			Grid_row.add(TL_no);
			Grid_row.add(formObject.getWFWorkitemName());
			PL_SKLogger.writeLog("PL common parse dedupe summary", Grid_row.toString());
			formObject.addItemFromList("cmplx_PartMatch_cmplx_Partmatch_grid", Grid_row);
		}

	}

	public static Map<String, String> getTagData_dedupeSummary(String parseXml,String tagName,String sub_tag){

		Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
		try {
			PL_SKLogger.writeLog("getTagDataParent_deep jsp: parseXml: ",parseXml);
			PL_SKLogger.writeLog("getTagDataParent_deep jsp: tagName: ",tagName);
			PL_SKLogger.writeLog("getTagDataParent_deep jsp: subTagName: ",sub_tag);
			String tag_notused = "BankId,OperationDesc,TxnSummary";

			InputStream is = new ByteArrayInputStream(parseXml.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList_loan = doc.getElementsByTagName(tagName);
			for(int i = 0 ; i<nList_loan.getLength();i++){
				String col_name = "";
				String col_val ="";
				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				String id = ch_nodeList.item(0).getTextContent();
				for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					if(sub_tag.contains(ch_nodeList.item(ch_len).getNodeName())){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						if(col_name.equalsIgnoreCase("")){
							col_name = sub_ch_nodeList.item(0).getTextContent();
							col_val = sub_ch_nodeList.item(1).getTextContent();
						}
						else{
							col_name = col_name+":,#:"+sub_ch_nodeList.item(0).getTextContent();
							col_val = col_val+":,#:"+sub_ch_nodeList.item(1).getTextContent();
						}

					}
					else if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("PersonDetails")){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						for (int k =0;k<sub_ch_nodeList.getLength();k++){
							col_name = col_name+":,#:"+sub_ch_nodeList.item(k).getNodeName();
							col_val = col_val+":,#:"+sub_ch_nodeList.item(k).getTextContent()+"";
						}
					}
					else if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("ContactDetails")){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						for (int k =0;k<sub_ch_nodeList.getLength();k++){
							NodeList contact_sub_ch_nodeList =  sub_ch_nodeList.item(k).getChildNodes();
							for (int con_ch_len=0; con_ch_len<contact_sub_ch_nodeList.getLength();con_ch_len++){
								col_name = col_name+":,#:"+contact_sub_ch_nodeList.item(0).getTextContent();
								col_val = col_val+":,#:"+contact_sub_ch_nodeList.item(1).getTextContent();
							}
						}
					} 
					else{
						if(col_name.equalsIgnoreCase("")){
							col_name = ch_nodeList.item(ch_len).getNodeName();
							col_val = ch_nodeList.item(ch_len).getTextContent();
						}
						else{
							col_name = col_name+":,#:"+ch_nodeList.item(ch_len).getNodeName();
							col_val = col_val+":,#:"+ch_nodeList.item(ch_len).getTextContent();
						}

					}

				}
				PL_SKLogger.writeLog("insert/update for id: ",id);
				PL_SKLogger.writeLog("insert/update cal_name: ",col_name);
				PL_SKLogger.writeLog("insert/update col_val: ",col_val);
				if(!col_name.equalsIgnoreCase(""))
					tagValuesMap.put(id, col_name+"~"+col_val);	
			}

		} catch (Exception e) {

			e.printStackTrace();
			PL_SKLogger.writeLog("Exception occured in getTagDataParent method:  ",e.getMessage());
		}
		return tagValuesMap;
	}

	//added here for fetchingDocRepeater


	public void custdet1values()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("cmplx_CustDetailverification1_MobNo1_value",formObject.getNGValue("AlternateContactDetails_MobileNo1"));
		formObject.setNGValue("cmplx_CustDetailverification1_MobNo2_value",formObject.getNGValue("AlternateContactDetails_MobileNo2"));
		formObject.setNGValue("cmplx_CustDetailverification1_DOB_value",formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("cmplx_CustDetailverification1_PO_Box_Value",formObject.getNGValue("AddressDetails_pobox"));
		formObject.setNGValue("cmplx_CustDetailverification1_Emirates_value",formObject.getNGValue("cmplx_Customer_EmiratesID"));
		formObject.setNGValue("cmplx_CustDetailverification1_Off_Telephone_value",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
	}

	public void EmpVervalues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("cmplx_EmploymentVerification_fixedsal_val",formObject.getNGValue("cmplx_IncomeDetails_FixedOT"));
		formObject.setNGValue("cmplx_EmploymentVerification_AccomProvided_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		formObject.setNGValue("cmplx_EmploymentVerification_Desig_val",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		formObject.setNGValue("cmplx_EmploymentVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));
		formObject.setNGValue("cmplx_EmploymentVerification_OffTelNo",formObject.getNGValue("AlternateContactDetails_OFFICENO"));

	}
	public void LoadPicklistVerification(List<String> mylist){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("RLOS_Common", "Inside LoadPicklistVerification(): "+mylist);

		for(String control_name:mylist)
		{
			formObject.setVisible(control_name, true);
			LoadPickList(control_name, "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri order by code");
		}
	}
	public void custdetvalues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("cmplx_CustDetailVerification_Mob_No1_val",formObject.getNGValue("AlternateContactDetails_MobileNo1"));
		formObject.setNGValue("cmplx_CustDetailVerification_Mob_No2_val",formObject.getNGValue("AlternateContactDetails_MobileNo2"));
		formObject.setNGValue("cmplx_CustDetailVerification_dob_val",formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("cmplx_CustDetailVerification_POBoxNo_val",formObject.getNGValue("AddressDetails_pobox"));
		formObject.setNGValue("cmplx_CustDetailVerification_emirates_val",formObject.getNGValue("cmplx_Customer_EmiratesID"));
		formObject.setNGValue("cmplx_CustDetailVerification_Resno_val",formObject.getNGValue("AlternateContactDetails_RESIDENCENO"));
		formObject.setNGValue("cmplx_CustDetailVerification_Offtelno_val",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
		formObject.setNGValue("cmplx_CustDetailVerification_hcountrytelno_val",formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO"));
		formObject.setNGValue("cmplx_CustDetailVerification_email1_val",formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI"));
		formObject.setNGValue("cmplx_CustDetailVerification_email2_val",formObject.getNGValue("AlternateContactDetails_EMAIL2_SEC"));
	}
	public void loadPicklist_WorldCheck()
	{
		LoadPickList("WorldCheck1_BirthCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
		LoadPickList("WorldCheck1_ResidentCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}
	// disha FSD
	public void loadPicklistMol()
	{
		 LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
	}

	// disha FSD
	public void loadPicklistDDSReturn()
	{
		LoadPickList("FinacleCore_Combo3", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Cheque_Type with (nolock) order by Code");
		LoadPickList("FinacleCore_Combo4", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_TypeOfReturn with (nolock) order by Code");
		LoadPickList("FinacleCore_Combo2", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) order by Code");
	}
	public void OfficeVervalues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("cmplx_OffVerification_fxdsal_val",formObject.getNGValue("cmplx_IncomeDetails_FixedOT"));
		formObject.setNGValue("cmplx_OffVerification_accprovd_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		formObject.setNGValue("cmplx_OffVerification_desig_val",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		formObject.setNGValue("cmplx_OffVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		formObject.setNGValue("cmplx_OffVerification_cnfrminjob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));    	   		 
	}

	public void loancardvalues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("cmplx_LoanandCard_loanamt_val",formObject.getNGValue("cmplx_LoanDetails_lonamt"));
		formObject.setNGValue("cmplx_LoanandCard_tenor_val",formObject.getNGValue("cmplx_LoanDetails_tenor"));
		formObject.setNGValue("cmplx_LoanandCard_emi_val",formObject.getNGValue("cmplx_LoanDetails_loanemi"));
		formObject.setNGValue("cmplx_LoanandCard_islorconv_val",formObject.getNGValue("Product_type"));
		formObject.setNGValue("cmplx_LoanandCard_firstrepaydate_val",formObject.getNGValue("cmplx_LoanDetails_frepdate"));
		formObject.setNGValue("cmplx_LoanandCard_cardtype_val",formObject.getNGValue("ReqProd"));
		formObject.setNGValue("cmplx_LoanandCard_cardlimit_val",formObject.getNGValue("ReqLimit"));
	}

	//ended here for fetchingDocRepeater
	public void  loadPicklistChecker()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PL_SKLogger.writeLog("PL","Inside PLCommon ->loadPicklistChecker()"+ActivityName);

			LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"' and Initiation_Type NOT LIKE  '%Reschedulment%'");
		}
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadPicklistChecker :"+e.getMessage());}	
	}	
//Method changed to handle cif with 0
	public int primary_cif_identify(Map<Integer, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry = new HashMap<String, String>();
			Map<String, String> curr_entry = new HashMap<String, String>();


			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();

				curr_entry = entry.getValue();
				if(curr_entry.get("SearchType").equalsIgnoreCase("Internal"))
				{
					if(primary_cif==0 && curr_entry.containsKey("Products")){
						primary_cif = entry.getKey();
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
								primary_cif = curr_cif_no;

						}

					}
				}

			}

		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
		}

		return primary_cif;
	}

	//Code for Customer Updated.
	public String CustomerUpdate(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg = "";
		String outputResponse = "";
		String cif_verf_status="N";
		String   ReturnCode="";
		String popupFlag="N";
		cif_verf_status = formObject.getNGValue("is_cust_verified");
		String Existingcust = formObject.getNGValue("cmplx_Customer_NTB");
		PL_SKLogger.writeLog("PL DDVT Vhecker", "EXISTING CUST : "+ Existingcust);
		if(Existingcust.equalsIgnoreCase("false")){
			cif_verf_status = "Y";
		}
		String Cif_lock_status = formObject.getNGValue("Is_CustLock");
		//String Cif_unlock_status = formObject.getNGValue("is_cust_verified");
		PL_SKLogger.writeLog("PL DDVT Vhecker", "cif_verf_status : "+ cif_verf_status);
		PL_SKLogger.writeLog("PL DDVT Vhecker", "cif_Lock_status : "+ Cif_lock_status);
		if (cif_verf_status.equalsIgnoreCase("")||cif_verf_status.equalsIgnoreCase("N")){
			outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","CIF_verify");

			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(ReturnCode.equalsIgnoreCase("0000")){
				formObject.setNGValue("is_cust_verified", "Y");
				cif_verf_status="Y";
				alert_msg="Customer verified Sucessfully";
			}
			else{
				formObject.setNGValue("is_cust_verified", "N");
				PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Error in Cif Enquiry operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";
				popupFlag = "Y";
				// throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}

		if (cif_verf_status.equalsIgnoreCase("Y")&&(Cif_lock_status.equalsIgnoreCase("")||Cif_lock_status.equalsIgnoreCase("N")))
		{
			PL_SKLogger.writeLog("PL DDVT Checker", "Inside Lock and Update Customer");
			outputResponse = GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(ReturnCode.equalsIgnoreCase("0000"))
			{
				Cif_lock_status="Y";
				PL_SKLogger.writeLog("PL DDVT Checker", "Locked sucessfully and now Unlock and update customer");
				formObject.setNGValue("Is_CustLock", "Y");
				outputResponse = GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

				if(ReturnCode.equalsIgnoreCase("0000"))
				{
					formObject.setNGValue("Is_CustLock", "N");
					Cif_lock_status="N";
					PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Cif UnLock sucessfull");

					outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

					if(ReturnCode.equalsIgnoreCase("0000")){
						formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
						PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						valueSetCustomer(outputResponse);    
						formObject.setEnabled("DecisionHistory_Button3", false);
						PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","CUSTOMER_UPDATE_REQ is generated");
						PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						alert_msg = "Customer Updated Successful!";
					}
					else{
						alert_msg= "Customer Update operation failed, Please try after some time or contact administrator";
						formObject.setEnabled("DecisionHistory_Button3", true);
						PL_SKLogger.writeLog("Customer Details","Customer Update operation Failed");
						formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
					}
					PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					formObject.RaiseEvent("WFSave");
					PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","after saving the flag");
					popupFlag = "Y";
					//throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Cif UnLock failed return code: "+ReturnCode);
					alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";
					popupFlag = "Y";
					// throw new ValidatorException(new FacesMessage(alert_msg));
				}

			}
			else{
				formObject.setNGValue("Is_CustLock", "N");
				PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Error in Cif Lock operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";
				popupFlag = "Y";
				// throw new ValidatorException(new FacesMessage(alert_msg));
			}

		}
		else if(cif_verf_status.equalsIgnoreCase("Y")&& Cif_lock_status.equalsIgnoreCase("Y"))
		{
			outputResponse = GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(ReturnCode.equalsIgnoreCase("0000"))
			{
				formObject.setNGValue("Is_CustLock", "N");
				Cif_lock_status="N";
				PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Cif UnLock sucessfull");

				outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

				if(ReturnCode.equalsIgnoreCase("0000")){
					formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
					PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					valueSetCustomer(outputResponse);    
					PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","CUSTOMER_UPDATE_REQ is generated");
					PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				}
				else{
					PL_SKLogger.writeLog("Customer Details","ACCOUNT_MAINTENANCE_REQ is not generated");
					formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
				}
				PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				formObject.RaiseEvent("WFSave");
				PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","after saving the flag");
				if(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ").equalsIgnoreCase("Y"))
				{ 
					PL_SKLogger.writeLog("RLOS value of Is_CUSTOMER_UPDATE_REQ","inside if condition");
					formObject.setEnabled("DecisionHistory_Button3", false); 
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","FetchDetails#Customer Updated Successful!!","",hm));
				}
				else{
					formObject.setEnabled("DecisionHistory_Button3", true);
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","FetchDetails#Customer Updated Fail!!","",hm));
				}

			}
			else{
				PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Cif UnLock failed return code: "+ReturnCode);
				alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";
				popupFlag = "Y";
				//throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}
		return alert_msg;
	}


	public void fetchoriginalDocRepeater(){

		//SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		PL_SKLogger.writeLog("INSIDE OriginalDocument:" ,"");

		List<String> repeaterHeaders = new ArrayList<String>();

		repeaterHeaders.add("Document List");

		repeaterHeaders.add("Status");
		repeaterHeaders.add("Received Date");
		repeaterHeaders.add("Expected Date");
		repeaterHeaders.add("Remarks");
		repeaterHeaders.add("OV Decision");
		repeaterHeaders.add("Approved By");



		/*repeaterHeaders.add("Jun-16");

	 		    repeaterHeaders.add("May-16");
	 		    repeaterHeaders.add("Apr-16");

	 		    repeaterHeaders.add("Mar-16");

	 		    repeaterHeaders.add("Feb-16");
	 		    repeaterHeaders.add("Jan-16");*/

		PL_SKLogger.writeLog("INSIDE Original Validation:" ,"after making headers");



		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> Day = null;

		String documentName = null;

		String query = "";

		IRepeater repObj=null;

		int repRowCount = 0;



		repObj = formObject.getRepeaterControl("OriginalValidation_Frame1");

		PL_SKLogger.writeLog("INSIDE Original Validation:" ,""+repObj.toString());



		//query = "SELECT distinct DAY FROM NG_RLOS_FinacleCore WHERE ProcessName='CreditCard'";

		/*ProcessDefId IN" + "(SELECT ProcessDefId FROM PROCESSDEFTABLE WHERE ProcessName ='"+processName+"')";*/



		//Day = //formObject.getNGDataFromDataCache(query);

		PL_SKLogger.writeLog("docName",""+ Day);





		try{

			if (repObj.getRepeaterRowCount() != 0) {

				PL_SKLogger.writeLog("RLOS Original Validation", "CLeared repeater object");

				repObj.clear();

			}
			repObj.setRepeaterHeaders(repeaterHeaders);

			for(int i=0;i<4;i++ ){
				repObj.addRow();

				//documentName = Day.get(i).get(0);

				PL_SKLogger.writeLog("Column Added in Repeater"," "+ documentName);

				//repObj.setValue(i, 0, documentName);

				repRowCount = repObj.getRepeaterRowCount();

				PL_SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);

			}

		}

		catch (Exception e) {

			PL_SKLogger.writeLog("EXCEPTION    :    ", " " + e.toString());

		} finally {

			repObj = null;

			repeaterHeaders = null;         
		}

	}
	// Aman Changes done for internal and external liability for the mapping on 18th sept 2017 starts
	public String  getInternalLiabDetails(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		PL_SKLogger.writeLog("getInternalLiabDetails sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("getInternalLiabDetails list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String accountType = "";
			String role = "";
			String accNumber = "";
			String acctOpenDate = ""; 
			String acctStatus = "";
			String acctSegment = "";
			String acctSubSegment = "";
			String acctCreditGrade = "";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				accountType = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				role = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				accNumber = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				acctOpenDate = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				acctStatus = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				acctSegment = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				acctSubSegment = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				acctCreditGrade = OutputXML.get(i).get(7).toString();
			}
			if(accNumber!=null && !accNumber.equalsIgnoreCase("") && !accNumber.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<AccountDetails><type_of_account>"+accountType+"</type_of_account>";
				add_xml_str = add_xml_str + "<role>"+role+"</role>";
				add_xml_str = add_xml_str + "<account_number>"+accNumber+"</account_number>";
				add_xml_str = add_xml_str + "<acct_open_date>"+acctOpenDate+"</acct_open_date>";
				add_xml_str = add_xml_str + "<acct_status>"+acctStatus+"</acct_status>";
				add_xml_str = add_xml_str + "<account_segment>"+acctSegment+"</account_segment>";
				add_xml_str = add_xml_str + "<account_sub_segment>"+acctSubSegment+"</account_sub_segment>";
				add_xml_str = add_xml_str + "<credit_grate_code>"+acctCreditGrade+"</credit_grate_code>";
				add_xml_str = add_xml_str + "<cust_type>"+role+"</cust_type></AccountDetails>";
			}
		}
		PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String InternalBureauData(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauData : ");
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
			String sQuery = "SELECT OutstandingAmt,OverdueAmt,CreditLimit FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Child_Wi = '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   TotalOutstandingAmt ,OverdueAmt,TotalLoanAmount FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			PL_SKLogger.writeLog("InternalBureauData list size"+OutputXML.size(), "");
			//SKLogger.writeLog("InternalBureauData list "+OutputXML, "");
			PL_SKLogger.writeLog("obefor list ", "values");
			float TotOutstandingAmt;
			float TotOverdueAmt;
		
			PL_SKLogger.writeLog("outsidefor list ", "values");
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;
	
			PL_SKLogger.writeLog("outsidefor2 list ", "values");
			for(int i = 0; i<OutputXML.size();i++){
				PL_SKLogger.writeLog("insidefor list "+i, "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					PL_SKLogger.writeLog("Totaloutstanding"+i, "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(0));
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					PL_SKLogger.writeLog("TotOverdueAmt"+i, "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}
				
			}
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLDerived = formObject.getDataFromDataSource(sQueryDerived);
			if(OutputXMLDerived!=null && OutputXMLDerived.size()>0 && OutputXMLDerived.get(0)!=null){
			if(!(OutputXMLDerived.get(0).get(0)==null || OutputXMLDerived.get(0).get(0).equals("")) ){
				NoOfContracts= OutputXMLDerived.get(0).get(0).toString();
			}
			if(!(OutputXMLDerived.get(0).get(1)==null || OutputXMLDerived.get(0).get(1).equals("")) ){
				Total_Exposure= OutputXMLDerived.get(0).get(1).toString();
			}
			if(!(OutputXMLDerived.get(0).get(2)==null || OutputXMLDerived.get(0).get(2).equals("")) ){
			     WorstCurrentPaymentDelay= OutputXMLDerived.get(0).get(2).toString();
			}
			if(!(OutputXMLDerived.get(0).get(3)==null || OutputXMLDerived.get(0).get(3).equals("")) ){
			     Worst_PaymentDelay_Last24Months= OutputXMLDerived.get(0).get(3).toString();
			}
			if(!(OutputXMLDerived.get(0).get(4)==null || OutputXMLDerived.get(0).get(4).equals("")) ){
			      Nof_Records= OutputXMLDerived.get(0).get(4).toString();
			}
			}
			add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";

			add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
			add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>"; // to be populated later
			add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; // to be populated later
			add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
			String sQuerycheque = "SELECT 'DDS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'DDS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLcheque = formObject.getDataFromDataSource(sQuerycheque);
		
			
			add_xml_str = add_xml_str + "<cheque_return_3mon>"+OutputXMLcheque.get(2).get(1)+"</cheque_return_3mon>";
			add_xml_str = add_xml_str + "<dds_return_3mon>"+OutputXMLcheque.get(0).get(1)+"</dds_return_3mon>";
			add_xml_str = add_xml_str + "<cheque_return_6mon>"+OutputXMLcheque.get(3).get(1)+"</cheque_return_6mon>";
			add_xml_str = add_xml_str + "<dds_return_6mon>"+OutputXMLcheque.get(1).get(1)+"</dds_return_6mon>";
			add_xml_str = add_xml_str + "<internal_charge_off>"+"N"+"</internal_charge_off><company_flag>N</company_flag></InternalBureau>";// to be populated later
		
			
			
			PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
			}
			catch(Exception e)
			{
				PL_SKLogger.writeLog("RLOSCommon", "Exception occurred in InternalBureauData()"+e.getMessage()+new PersonalLoanSCommonCode().printException(e));
				return "";
			}
		
	}

	public String InternalBouncedCheques(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"'";
		PL_SKLogger.writeLog("InternalBouncedCheques sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("InternalBouncedCheques list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq="";
			String amount = "";
			String reason = ""; 
			String returnDate = "";
			

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				internal_bounced_cheques_id = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				bouncedCheq = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				chequeNo = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				amount = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				reason = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				returnDate = OutputXML.get(i).get(6).toString();
			}


			if(applicantID!=null && !applicantID.equalsIgnoreCase("") && !applicantID.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>"+applicantID+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>"+internal_bounced_cheques_id+"</internal_bounced_cheques_id>";
				if (bouncedCheq.equalsIgnoreCase("ICCS")){
				add_xml_str = add_xml_str + "<bounced_cheque>"+"1"+"</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>"+chequeNo+"</cheque_no>";
				add_xml_str = add_xml_str + "<amount>"+amount+"</amount>";
				add_xml_str = add_xml_str + "<reason>"+reason+"</reason>";
				add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; 
				add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
				if (bouncedCheq.equalsIgnoreCase("DDS")){
				add_xml_str = add_xml_str + "<bounced_cheque_dds>"+"1"+"</bounced_cheque_dds>"; 
				}
				add_xml_str=  add_xml_str+"<company_flag>N</company_flag></InternalBouncedCheques>";
				}

		}
		PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String InternalBureauIndividualProducts(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,PaymentsAmount,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_CardDetails where 	Child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type In ('InternalExposure','CollectionsSummary')";
		//Query Changed on 1st Nov
		PL_SKLogger.writeLog("InternalBureauIndividualProducts sQuery"+sQuery, "");
		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Child_wi = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("InternalBureauIndividualProducts list size"+OutputXML.size(), "");
		PL_SKLogger.writeLog("InternalBureauIndividualProducts list "+OutputXML, "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String cardprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5);
		
		String mol_sal_var = "";
		PL_SKLogger.writeLog("InternalBureauIndividualProducts outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar"), "");
		if(formObject.getNGValue("cmplx_MOL_molsalvar")!= null){
			PL_SKLogger.writeLog("InternalBureauIndividualProducts  outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar"), "");
			mol_sal_var = formObject.getNGValue("cmplx_MOL_molsalvar");
		}
      try{
		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String agreementId = "";
			String product_type = "";
			String contractType = ""; 
			String custroletype = "";
			String loan_start_date = "";
			String loanmaturitydate = "";
			String lastupdatedate = "";

			String outstandingamt = "";
			String totalloanamount = "";
			String Emi = "";
			String paymentmode = ""; 
			String totalnoofinstalments = "";
			String remaininginstalments = "";
			String CashLimit = "";
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
			String EmployerType=formObject.getNGValue("EMploymentDetails_Combo5");
			String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
			String paid_installment="";
			String Internal_WriteOff_Check="";
			PL_SKLogger.writeLog("Inside for", "asdasdasd");
			
			
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				agreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				product_type = OutputXML.get(i).get(2).toString();
				if (product_type.equalsIgnoreCase("Home In One")){
					product_type="HIO";
				}
				else{
					product_type = OutputXML.get(i).get(63).toString();
				}
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				contractType = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				custroletype = OutputXML.get(i).get(4).toString();
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				loan_start_date = OutputXML.get(i).get(5).toString();
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
						loanmaturitydate = OutputXML.get(i).get(6).toString();
			}
			if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") ){
						lastupdatedate = OutputXML.get(i).get(7).toString();
			}				
			if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") ){
						outstandingamt = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				totalloanamount = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				Emi = OutputXML.get(i).get(10).toString();
			}				
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				paymentmode = OutputXML.get(i).get(11).toString();
			}
			if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") ){
				//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				remaininginstalments = OutputXML.get(i).get(13).toString();
			}				
			if(!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals("")) ){
				CashLimit = OutputXML.get(i).get(14).toString();
			}
			if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
				overdueamt = OutputXML.get(i).get(15).toString();
			}
			if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
				nofdayspmtdelay = OutputXML.get(i).get(16).toString();
			}				
			if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
				monthsonbook = OutputXML.get(i).get(17).toString();
			}
			if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) ){
				currentlycurrent = OutputXML.get(i).get(18).toString();
			}
			if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) ){
				currmaxutil = OutputXML.get(i).get(19).toString();
			}				
			if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) ){
				DPD_30_in_last_6_months = OutputXML.get(i).get(20).toString();
			}
			if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) ){
				DPD_60_in_last_18_months = OutputXML.get(i).get(21).toString();
			}
			if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) ){
				propertyvalue = OutputXML.get(i).get(22).toString();
			}				
			if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) ){
				loan_disbursal_date = OutputXML.get(i).get(23).toString();
			}
			if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) ){
				marketingcode = OutputXML.get(i).get(24).toString();
			}
			if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) ){
				DPD_30_in_last_3_months = OutputXML.get(i).get(25).toString();
			}				
			if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) ){
				DPD_30_in_last_9_months	 = OutputXML.get(i).get(26).toString();
			}
			if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) ){
				DPD_30_in_last_12_months = OutputXML.get(i).get(27).toString();
			}
			if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) ){
				DPD_30_in_last_18_months = OutputXML.get(i).get(28).toString();
			}				
			if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
				DPD_30_in_last_24_months = OutputXML.get(i).get(29).toString();
			}
			if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
				DPD_60_in_last_3_months = OutputXML.get(i).get(30).toString();
			}
			if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
				DPD_60_in_last_6_months = OutputXML.get(i).get(31).toString();
			}				
			if(!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals("")) ){
				DPD_60_in_last_9_months = OutputXML.get(i).get(32).toString();
			}
			if(!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals("")) ){
				DPD_60_in_last_12_months = OutputXML.get(i).get(33).toString();
			}
			if(!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals("")) ){
				DPD_60_in_last_24_months = OutputXML.get(i).get(34).toString();
			}				
			if(!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals("")) ){
				DPD_90_in_last_3_months = OutputXML.get(i).get(35).toString();
			}
			if(!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals("")) ){
				DPD_90_in_last_6_months = OutputXML.get(i).get(36).toString();
			}
			if(!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals("")) ){
				DPD_90_in_last_9_months = OutputXML.get(i).get(37).toString();
			}				
			if(!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals("")) ){
				DPD_90_in_last_12_months = OutputXML.get(i).get(38).toString();
			}
			if(!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals("")) ){
				DPD_90_in_last_18_months = OutputXML.get(i).get(39).toString();
			}
			if(!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals("")) ){
				DPD_90_in_last_24_months = OutputXML.get(i).get(40).toString();
			}				
			if(!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals("")) ){
				DPD_120_in_last_3_months = OutputXML.get(i).get(41).toString();
			}
			if(!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals("")) ){
				DPD_120_in_last_6_months = OutputXML.get(i).get(42).toString();
			}
			if(!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals("")) ){
				DPD_120_in_last_9_months = OutputXML.get(i).get(43).toString();
			}				
			if(!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals("")) ){
				DPD_120_in_last_12_months = OutputXML.get(i).get(44).toString();
			}
			if(!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals("")) ){
				DPD_120_in_last_18_months = OutputXML.get(i).get(45).toString();
			}
			if(!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals("")) ){
				DPD_120_in_last_24_months = OutputXML.get(i).get(46).toString();
			}				
			if(!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals("")) ){
				DPD_150_in_last_3_months = OutputXML.get(i).get(47).toString();
			}
			if(!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals("")) ){
				DPD_150_in_last_6_months = OutputXML.get(i).get(48).toString();
			}
			if(!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals("")) ){
				DPD_150_in_last_9_months = OutputXML.get(i).get(49).toString();
			}				
			if(!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals("")) ){
				DPD_150_in_last_12_months = OutputXML.get(i).get(50).toString();
			}
			if(!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals("")) ){
				DPD_150_in_last_18_months = OutputXML.get(i).get(51).toString();
			}
			if(!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals("")) ){
				DPD_150_in_last_24_months = OutputXML.get(i).get(52).toString();
			}				
			if(!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals("")) ){
				DPD_180_in_last_3_months = OutputXML.get(i).get(53).toString();
			}
			if(!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals("")) ){
				DPD_180_in_last_6_months = OutputXML.get(i).get(54).toString();
			}
			if(!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals("")) ){
				DPD_180_in_last_9_months = OutputXML.get(i).get(55).toString();
			}				
			if(!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals("")) ){
				DPD_180_in_last_12_months = OutputXML.get(i).get(56).toString();
			}
			if(!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals("")) ){
				DPD_180_in_last_24_months = OutputXML.get(i).get(57).toString();
			}
			if(!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals("")) ){
				CardExpiryDate = OutputXML.get(i).get(60).toString();
			}
			
			if(!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals("")) ){
				Consider_For_Obligations = OutputXML.get(i).get(61).toString();
				if (Consider_For_Obligations.equalsIgnoreCase("false")){
					Consider_For_Obligations="N";
				}
				else {
					Consider_For_Obligations="Y";
				}
			}
			
			if(!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals("")) ){
				phase = OutputXML.get(i).get(62).toString();
				if (phase.startsWith("C")){
					phase="C";
				}
				else {
					phase="A";
				}
			}
			if(!(OutputXML.get(i).get(64) == null || OutputXML.get(i).get(64).equals("")) ){
				writeoffStat = OutputXML.get(i).get(64).toString();
			}
			if(!(OutputXML.get(i).get(65) == null || OutputXML.get(i).get(65).equals("")) ){
				writeoffstatdt = OutputXML.get(i).get(65).toString();
			}
			if(!(OutputXML.get(i).get(66) == null || OutputXML.get(i).get(66).equals("")) ){
				lastrepmtdt = OutputXML.get(i).get(66).toString();
			}
			if(!(OutputXML.get(i).get(67) == null || OutputXML.get(i).get(67).equals("")) ){
				Limit_increase = OutputXML.get(i).get(67).toString();
				if (Limit_increase.equalsIgnoreCase("false")){
					Limit_increase="Y";
				}
				else{
					Limit_increase="N";
				}
			}
			if(!(OutputXML.get(i).get(68) == null || OutputXML.get(i).get(68).equals("")) ){
				part_settlement_date = OutputXML.get(i).get(67).toString();
				String abc=OutputXML.get(i).get(68).toString();
				abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
				String abcsa[]=abc.split("split");
				part_settlement_date = abcsa[0];
				part_settlement_amount = abcsa[1];
			}
			if(!(OutputXML.get(i).get(69) == null || OutputXML.get(i).get(69).equals("")) ){
				SchemeCardProduct = OutputXML.get(i).get(69).toString();
			}
			if(!(OutputXML.get(i).get(70) == null || OutputXML.get(i).get(70).equals("")) ){
				General_Status = OutputXML.get(i).get(70).toString();
			}
			if(!(OutputXML.get(i).get(71) == null || OutputXML.get(i).get(71).equals("")) ){
				Internal_WriteOff_Check = OutputXML.get(i).get(71).toString();
			}
			String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='"+SchemeCardProduct+"'";
			List<List<String>> sQueryCombinedLimitXML = formObject.getDataFromDataSource(sQueryCombinedLimit);
			if (sQueryCombinedLimitXML!= null){
				Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
			}
			String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='"+SchemeCardProduct+"'  and subproduct='SEC'";
			List<List<String>> sQuerySecuredCardXML = formObject.getDataFromDataSource(sQuerySecuredCard);
			if (sQuerySecuredCardXML!= null){
				SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
			}
			
			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				PL_SKLogger.writeLog("Inside if", "asdasdasd");
				add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
				if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){					
				add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
				}
				if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
					add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
				}	
				add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
				add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>Primary</role_of_customer>"; 

				add_xml_str = add_xml_str + "<start_date>"+loan_start_date+"</start_date>"; 
				add_xml_str = add_xml_str + "<close_date>"+loanmaturitydate+"</close_date>"; 
				add_xml_str = add_xml_str + "<date_last_updated>"+lastupdatedate+"</date_last_updated>"; 
				add_xml_str = add_xml_str + "<outstanding_balance>"+outstandingamt+"</outstanding_balance>"; 
				if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
				add_xml_str = add_xml_str + "<total_amount>"+totalloanamount+"</total_amount>";
				}
				add_xml_str = add_xml_str + "<payments_amount>"+Emi+"</payments_amount>"; 
				add_xml_str = add_xml_str + "<method_of_payment>"+paymentmode+"</method_of_payment>"; 
				add_xml_str = add_xml_str + "<total_no_of_instalments>"+totalnoofinstalments+"</total_no_of_instalments>"; 
				add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>"; 
				add_xml_str = add_xml_str + "<worst_status>"+writeoffStat+"</worst_status>"; 
				add_xml_str = add_xml_str + "<worst_status_date>"+writeoffstatdt+"</worst_status_date>"; 
				if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){
					
				add_xml_str = add_xml_str + "<credit_limit>"+totalloanamount+"</credit_limit>"; 
				}
				add_xml_str = add_xml_str + "<overdue_amount>"+overdueamt+"</overdue_amount>"; 
				add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+nofdayspmtdelay+"</no_of_days_payment_delay>"; 
				add_xml_str = add_xml_str + "<mob>"+monthsonbook+"</mob>"; 
				add_xml_str = add_xml_str + "<last_repayment_date>"+lastrepmtdt+"</last_repayment_date>"; 
				add_xml_str = add_xml_str + "<currently_current>"+currentlycurrent+"</currently_current>"; 
				add_xml_str = add_xml_str + "<current_utilization>"+currmaxutil+"</current_utilization>"; 
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD_30_in_last_6_months+"</dpd_30_last_6_mon>"; 
				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD_60_in_last_18_months+"</dpd_60p_in_last_18_mon>"; 

				
				add_xml_str = add_xml_str + "<card_product>"+SchemeCardProduct+"</card_product>"; 
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
				add_xml_str = add_xml_str + "<mol_salary_variance>"+mol_sal_var+"</mol_salary_variance>";
				if(Kompass!=null){
				if(Kompass.equalsIgnoreCase("true")){
				add_xml_str = add_xml_str + "<kompass>"+"Y"+"</kompass>";
				}
				else{
					add_xml_str = add_xml_str + "<kompass>"+"N"+"</kompass>";
				}
				}
				add_xml_str = add_xml_str + "<employer_type>"+EmployerType+"</employer_type>"; 


				if (totalnoofinstalments!=null && remaininginstalments!=null && !totalnoofinstalments.equals("") &&  !remaininginstalments.equals("")) {
					paid_installment= Integer.toString(Integer.parseInt(totalnoofinstalments) -Integer.parseInt(remaininginstalments));
					PL_SKLogger.writeLog("Inside paid_installment", "paid_installment"+paid_installment);

				}
				if (ReqProd.equalsIgnoreCase("Credit Card")){

					add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><company_flag>N</company_flag></InternalBureauIndividualProducts>";
				}
				else{
					add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag><type_of_od>"+""+"</type_of_od><amt_paid_last6mnths>"+""+"</amt_paid_last6mnths></InternalBureauIndividualProducts>";

				}
			}	

		}
      }
      catch(Exception e){
    	  PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ new PersonalLoanSCommonCode().printException(e));
      }
      PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
		PL_SKLogger.writeLog("InternalBureauPipelineProducts sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("InternalBureauPipelineProducts list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String Product = "";
			String role = "";
			String lastUpdateDate = ""; 
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";
			String agreementId = "";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				Product = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				role = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				lastUpdateDate = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				TotAmount = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				TotNoOfInstlmnt = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				TotLoanAmt = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				agreementId = OutputXML.get(i).get(7).toString();
			}
			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";// to be populated later
				add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+agreementId+"</internal_bureau_pipeline_products_id>";// to be populated later
				add_xml_str = add_xml_str + "<ppl_provider_no>"+""+"</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_product>"+Product+"</ppl_product>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>"+""+"</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_phase>"+""+"</ppl_phase>"; // to be populated later

				add_xml_str = add_xml_str + "<ppl_role>"+Product+"</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmount+"</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+TotNoOfInstlmnt+"</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>"+TotLoanAmt+"</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+""+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></InternalBureauPipelineProducts>"; // to be populated later
			}

		}
		PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauData(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		PL_SKLogger.writeLog("ExternalBureauData sQuery"+sQuery, "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  wi_name  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("ExternalBureauData list size"+OutputXML.size(), "");
		List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
/*
		float TotOutstandingAmt = 0.0f;
		float TotOverdueAmt = 0.0f;
		float TotalExposure = 0.0f;
		for(int i = 0; i<OutputXML.size();i++){
			if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(1));
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(2));
			}
			if(OutputXML.get(i).get(3)!=null && !OutputXML.get(i).get(3).isEmpty() &&  !OutputXML.get(i).get(3).equals("") && !OutputXML.get(i).get(3).equalsIgnoreCase("null") ){
					TotalExposure = TotalExposure + Float.parseFloat(OutputXML.get(i).get(3));
			}
		}*/
		if (AecbHistQueryData.get(0).get(0).equalsIgnoreCase("0")){


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
			String Internal_WriteOff_Check="";



			add_xml_str = add_xml_str + "<ExternalBureau>"; 
			add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";

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

			add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";



			PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		else{
		for (int i = 0; i<OutputXML.size();i++){

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
			String Internal_WriteOff_Check="";
			
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				fullnm = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				TotalOutstanding = OutputXML.get(i).get(2).toString();
				
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				TotalOverdue = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				NoOfContracts = OutputXML.get(i).get(4).toString();
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				Total_Exposure = OutputXML.get(i).get(5).toString();
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
				WorstCurrentPaymentDelay = OutputXML.get(i).get(6).toString();
			}
			if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") ){
				Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7).toString();
			}				
			if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") ){
				Worst_Status_Last24Months = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				Nof_Records = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10).toString();
			}				
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11).toString();
			}
			if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") ){
				//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
				Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				DPD30_Last6Months = OutputXML.get(i).get(13).toString();
			}
			
				add_xml_str = add_xml_str + "<ExternalBureau>"; 
				add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";
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

				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";
			
		}
		PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
		}
		}
		catch(Exception e)
		{
			PL_SKLogger.writeLog("RLOSCommon", "Exception occurred in externalBureauData()"+new PersonalLoanSCommonCode().printException(e));
			return null;
		}
	}

	public String ExternalBouncedCheques(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		PL_SKLogger.writeLog("ExternalBouncedCheques sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("ExternalBouncedCheques list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = ""; 
			String returnDate = "";
			String providerNo = "";

			
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				chqNo = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				Amount = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				Reason = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				providerNo = OutputXML.get(i).get(5).toString();
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
		PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauIndividualProducts(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_amt,'' as qc_emi,'' as Cac_indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where child_wi= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,QC_Amt,QC_EMI,CAC_Indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' ";
		//Query Changed on 1st Nov
		PL_SKLogger.writeLog("ExternalBureauIndividualProducts sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("ExternalBureauIndividualProducts list size"+OutputXML.size(), "");
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
			String DPD60Last18Months = "";
			String AECBHistMonthCnt = "";
			String DPD30Last6Months = "";
			String currently_current = "";
			String current_utilization = "";
			String delinquent_in_last_3months = "";
			String QC_Amt = "";
			String QC_emi = "";
			String CAC_Indicator = "";
			String consider_for_obligation="";
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				AgreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				ContractType = OutputXML.get(i).get(2).toString();
				try{
					String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
					PL_SKLogger.writeLog("ExternalBureauIndividualProducts sQuery"+sQuery, "");
					List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
					ContractType=cardqueryXML.get(0).get(0);
					PL_SKLogger.writeLog("ExternalBureauIndividualProducts ContractType"+ContractType, "ContractType");
					}
					catch(Exception e){
						PL_SKLogger.writeLog("ExternalBureauIndividualProducts ContractType Exception"+e, "Exception");
						
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
				DPD60Last18Months = OutputXML.get(i).get(23).toString();
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
				if (CAC_Indicator != null && !(CAC_Indicator.equalsIgnoreCase(""))){
					if (CAC_Indicator.equalsIgnoreCase("true")){
						CAC_Indicator="Y";
					}
					else {
						CAC_Indicator="N";
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
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
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
				add_xml_str = add_xml_str + "<current_utilization>"+current_utilization+"</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD30Last6Months+"</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD60Last18Months+"</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history>"+AECBHistMonthCnt+"</no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>"+CAC_Indicator+"</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>"+QC_emi+"</qc_emi>";
				if (ReqProd.equalsIgnoreCase("Credit Card")){
					add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name></ExternalBureauIndividualProducts>";
				}
				else{
					add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";
				}

			}
			PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		public String ExternalBureauManualAddIndividualProducts(){
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PL_SKLogger.writeLog("ExternalBureauManualAddIndividualProducts sQuery", "");
			int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
			String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
			String  add_xml_str ="";
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			PL_SKLogger.writeLog("ExternalBureauIndividualProducts list size"+Man_liab_row_count, "");
			if (Man_liab_row_count !=0){
				for (int i = 0; i<Man_liab_row_count;i++){
					String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
					String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
					String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
					String Take_over_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 3); //0
					String Take_over_amount = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 4); //0
					String cac_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5); //0
					String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
					String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
					if (cac_Indicator.equalsIgnoreCase("true")){
						cac_Indicator="Y";
					}
					else {
						cac_Indicator="N";
					}
					String consider_for_obligation = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8).equalsIgnoreCase("true")?"Y":"N"); //0
					String remarks = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
					//String MOB = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
					String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
					String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
					String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
					String delinquent_in_last_3months = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13).equalsIgnoreCase("true")?"1":"0");
					String dpd_30_last_6_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14).equalsIgnoreCase("true")?"1":"0");
					String dpd_60p_in_last_18_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15).equalsIgnoreCase("true")?"1":"0");
					add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+applicant_id+"</applicant_id>";
					add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<contract_type>"+Type_of_Contract+"</contract_type>";
					add_xml_str = add_xml_str + "<provider_no></provider_no>";
					add_xml_str = add_xml_str + "<phase>A</phase>";
					add_xml_str = add_xml_str + "<role_of_customer>Main</role_of_customer>";
					add_xml_str = add_xml_str + "<start_date>"+modifiedDate+"</start_date>"; 

					add_xml_str = add_xml_str + "<close_date></close_date>";
					add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding+"</outstanding_balance>";
					add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
					add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
					add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
					add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<worst_status></worst_status>";
					add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";

					add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
					add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
					add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
					add_xml_str = add_xml_str + "<mob>"+mob+"</mob>";
					add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

					add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

					add_xml_str = add_xml_str + "<current_utilization>"+Utilization+"</current_utilization>";
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+dpd_30_last_6_mon+"</dpd_30_last_6_mon>";

					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+dpd_60p_in_last_18_mon+"</dpd_60p_in_last_18_mon>";
					add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
					add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
					add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
					add_xml_str = add_xml_str + "<cac_indicator>"+cac_Indicator+"</cac_indicator>";
					add_xml_str = add_xml_str + "<qc_emi>"+Qc_Emi+"</qc_emi>";
					add_xml_str = add_xml_str + "<qc_amount>"+Qc_amt+"</qc_amount><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";

				}

			}
			PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}

	public String ExternalBureauPipelineProducts(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true')  from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
		PL_SKLogger.writeLog("ExternalBureauPipelineProducts sQuery"+sQuery, "");
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PL_SKLogger.writeLog("ExternalBureauPipelineProducts list size"+OutputXML.size(), "");
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

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					contractType = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				productType = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				role = OutputXML.get(i).get(4).toString();
			}
			if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !OutputXML.get(i).get(5).equals("") && !OutputXML.get(i).get(5).equalsIgnoreCase("null") ){
						lastUpdateDate = OutputXML.get(i).get(5).toString();
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
						TotAmt = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				noOfInstalmnt = OutputXML.get(i).get(7).toString();
			}
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				creditLimit = OutputXML.get(i).get(8).toString();
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !OutputXML.get(i).get(9).equals("") && !OutputXML.get(i).get(9).equalsIgnoreCase("null") ){
					noOfDayinPpl = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(10).toString();
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
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

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauPipelineProducts>"; // to be populated later

			


		}
		PL_SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	// Aman Changes done for internal and external liability for the mapping on 13th sept 2017 ends
	public String getProduct_details(){
		PL_SKLogger.writeLog("RLOSCommon java file", "inside getProduct_details : ");
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
        PL_SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+prod_row_count);
        String FinalLimitPLQuery="select isnull(FinalLoanAmount,0) from ng_rlos_IGR_Eligibility_PersonalLoan where Child_wi='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitPLXML = formObject.getDataFromDataSource(FinalLimitPLQuery);
	    String finalLimit="";
        if (FinalLimitPLXML.size()!= 0){
			finalLimit=FinalLimitPLXML.get(0).get(0);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit", finalLimit);
		}
        String  prod_xml_str ="";
        String Manual_Dev=formObject.getNGValue("cmplx_DEC_Manual_Deviation");

        for (int i = 0; i<prod_row_count;i++){
        		PL_SKLogger.writeLog("PL_SKLogger java file", "inside getProduct_details add_row_count+ : "+prod_row_count);
            
              String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
              String priority = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);
              String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
              String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
              String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3);//3
              reqLimit=reqLimit.replaceAll(",", "");
              String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4
              String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
              String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//6
              String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 6);//7
             // String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 12);//8
              String ApplicationDate=formObject.getNGValue("CreatedDate");
              if((ApplicationDate != null)||(ApplicationDate != "")){
              	ApplicationDate=Convert_dateFormat(ApplicationDate, "dd/MM/yyyy", "yyyy-MM-dd");
              }
              PL_SKLogger.writeLog("PL_SKLogger java file", "inside ApplicationDate ApplicationDate+ : "+ApplicationDate);
              
             // limitExpiry=Convert_dateFormat(limitExpiry, "dd/MM/yyyy", "yyyy-MM-dd");
              String EMI="";
              EMI=formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI");
              if(EMI == null){
            	  EMI=""; 
              }
            
              if (priority.equalsIgnoreCase("Primary")){
              prod_xml_str = prod_xml_str + "<ApplicationDetails><product_type>"+(prod_type.equalsIgnoreCase("Conventional")?"CON":"ISL")+"</product_type>";
              prod_xml_str = prod_xml_str + "<app_category>"+formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")+"</app_category>";
              prod_xml_str = prod_xml_str + "<requested_product>"+(reqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")+"</requested_product>";
              prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
              prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
              prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
              prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
              prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
              prod_xml_str = prod_xml_str + "<tenure>"+tenure+"</tenure>";
              prod_xml_str = prod_xml_str + "<customer_type>"+(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"NTB":"Existing")+"</customer_type>";
              if(Manual_Dev!=null){ 
              		if(Manual_Dev.equalsIgnoreCase("true")){
	              prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation>Y</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
	              }
	              else{
	            	  prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation>N</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
	              	}   
	              }
              else {
            	  prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation></manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
      	          
              }
              }
              
        }
        PL_SKLogger.writeLog("RLOSCommon", "Address tag Cration: "+ prod_xml_str);
        return prod_xml_str;
  }
	
	//Code Changes to custom coding for dectech code
	public String getYearsDifference(FormReference formObject,String controlName) {
		int MON;
		int Year;
		String age="";
		String DOB=formObject.getNGValue(controlName);
		PL_SKLogger.writeLog("RLOS COMMON"," Inside age + "+DOB);
		String CurrDate= new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	if (DOB!=null){	
		String[] Dob=DOB.split("/");
		String[] CurreDate=CurrDate.split("/");
		int monthbirthDate=Integer.parseInt(Dob[1]);
		int monthcurrDate=Integer.parseInt(CurreDate[1]);
		int YearbirthDate=Integer.parseInt(Dob[2]);
		int yearcurrDate=Integer.parseInt(CurreDate[2]);
			if (monthcurrDate<monthbirthDate){
			yearcurrDate=yearcurrDate-1;
			Year=yearcurrDate-YearbirthDate;
			MON=monthcurrDate-monthbirthDate;
			MON=12+MON;
			if ((MON==10)||(MON==11)){
				age=Year+"."+MON;}
				else{
					age=Year+".0"+MON;
				}
			
			}
		else if (monthcurrDate>monthbirthDate){
			Year=yearcurrDate-YearbirthDate;
			MON=monthcurrDate-monthbirthDate;
			if ((MON==10)||(MON==11)){
				age=Year+"."+MON;}
			else{
				age=Year+".0"+MON;
				}
		}
		else {
			Year=yearcurrDate-YearbirthDate;
			if(Year<10){
			age="0"+Year+".00";
			}
			else{
				age=Year+".00";
			}
					
		}
	}
	return age;
}

	//Setting the value in the grid for DECTECH
	public static void dectechfromeligbility(String outputResponse){
		try{
		if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
			String squery="";
			String Output_TAI="";
			String Output_Final_DBR="";
			String Output_Decision="";
			String Output_Existing_DBR  ="";
			String Output_Eligible_Amount="";
			String Output_Delegation_Authority="";
			String Grade="";
			String Output_Interest_Rate="";
			String Output_Net_Salary_DBR="";
			String Output_Affordable_EMI="";
			double cac_calc_limit=0.0;
			String ReasonCode="";
			String DeviationCode="";
			String Output_Accommodation_Allowance="";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String strOutputXml = "";
			String sGeneralData = formObject.getWFGeneralData();
			PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get sGeneralData"+sGeneralData);
			String output_accomodation="";
			String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
			String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
			String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>")); ;
			String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>")); ;

			//Setting the value in ELIGANDPROD info
			if (outputResponse.contains("Output_TAI")){
				Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
		    	if (Output_TAI!=null){
		    		try{
		    		formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
		    		formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI);
		    		
		    		}
		    		catch (Exception e){
		    			PL_SKLogger.writeLog("Dectech error", "Exception:"+e.getMessage());
		    		}
		    			
		    		
		    	}
		    	PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_TAI"+Output_TAI);
			}
			if (outputResponse.contains("Output_Delegation_Authority")){
				Output_Delegation_Authority = outputResponse.substring(outputResponse.indexOf("<Output_Delegation_Authority>")+29,outputResponse.indexOf("</Output_Delegation_Authority>")); ;
				PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Delegation_Authority"+Output_Delegation_Authority);
				
				}
			boolean DecFragVis=formObject.isVisible("DecisionHistory_Frame1");
			PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_TAI"+DecFragVis);
			if (DecFragVis==false){
				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
				formObject.setNGValue("cmplx_Decision_Highest_delegauth", Output_Delegation_Authority);
			}
			if (outputResponse.contains("Output_Decision")){
				Output_Decision = outputResponse.substring(outputResponse.indexOf("<Output_Decision>")+17,outputResponse.indexOf("</Output_Decision>"));
		    	if (Output_Decision!=null){
		    		try{
		    		if (Output_Decision.equalsIgnoreCase("D")){
		    			Output_Decision="Declined";
		    		}	
		    		else if (Output_Decision.equalsIgnoreCase("A")){
		    			Output_Decision="Approve";
		    		}	
		    		else if (Output_Decision.equalsIgnoreCase("R")){
		    			Output_Decision="Refer";
		    		}	
		    		formObject.setNGValue("cmplx_Decision_Dectech_decsion", Output_Decision);
		    		formObject.setNGValue("cmplx_Decision_Highest_delegauth", Output_Delegation_Authority);
		    		
		    		}
		    		catch (Exception e){
		    			PL_SKLogger.writeLog("Dectech error", "Exception:"+e.getMessage());
		    		}
		    			
		    		
		    	}
		    	PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Decision"+Output_Decision);
			}
				
            if (outputResponse.contains("output_accomodation")){
                  output_accomodation = outputResponse.substring(outputResponse.indexOf("<output_accomodation>")+20,outputResponse.indexOf("</output_accomodation>")); ;
                  PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
                  if (output_accomodation!=null){
                        formObject.setNGValue("cmplx_IncomeDetails_compaccAmt", output_accomodation);
                  }
            }
          //Added by aman for PROC-2535
			if (outputResponse.contains("Output_Accommodation_Allowance")){
				Output_Accommodation_Allowance = outputResponse.substring(outputResponse.indexOf("<Output_Accommodation_Allowance>")+32,outputResponse.indexOf("</Output_Accommodation_Allowance>")); ;
				PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Accommodation_Allowance"+Output_Accommodation_Allowance);
				formObject.setNGValue("cmplx_IncomeDetails_compaccAmt", Output_Accommodation_Allowance);
			}
			//Added by aman for PROC-2535
			
			if (outputResponse.contains("Output_Eligible_Cards")){
	    		try{
	    		String Output_Eligible_Cards = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Cards>")+23,outputResponse.indexOf("</Output_Eligible_Cards>"));
	    		Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
	    		 String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				  strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
				 
				  PL_SKLogger.writeLog("Output_Eligible_Cards", "after removing the starting and Trailing:"+Output_Eligible_Cards);
	    		String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
	    			for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
	    				  String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
	    				  PL_SKLogger.writeLog("Output_Eligible_Cards", "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array);
	    				  String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
	    				  String[] Limit=Output_Eligible_Cards_Array[1].split(":");
	    				  String[] flag=Output_Eligible_Cards_Array[2].split(":");
	    				  String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
	    				  String LIMIT= Limit[1];
	    				  String FLAG= flag[1].substring(1,flag[1].length()-1);
	    				  
	    				  PL_SKLogger.writeLog("Output_Eligible_Cards", "Card_Prod:"+Card_Prod[1]);
	    				  PL_SKLogger.writeLog("Output_Eligible_Cards", "Limit:"+Limit[1]);
	    				  PL_SKLogger.writeLog("Output_Eligible_Cards", "flag:"+flag[1]);
	    				  String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit VALUES ('"+Card_Product+"','"+LIMIT+"','','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"')";
	    				  PL_SKLogger.writeLog("Output_Eligible_Cards", "QUERY:"+query);
    			    	  formObject.saveDataIntoDataSource(query);
    				
	    				 /* for (int j=0; j<Output_Eligible_Cards_Array.length;j++){
	    					 String[] values=Output_Eligible_Cards_Array[j].split(":");
	    					 SKLogger_CC.writeLog("Output_Eligible_Cards", "values:"+values);
	    					  if(values[0].contains("\"")){
	    						  values[0]=values[0].substring(1, values[0].length()-1);
	    				    	}
	    					  if(values[1].contains("\"")){
	    						  values[1]=values[1].substring(1, values[1].length()-1);
	    				    	}
	    					  String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
	    					  strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
	    					  String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit("+values[0]+") VALUES ('"+values[1]+"')";
	    					  SKLogger_CC.writeLog("Output_Eligible_Cards", "QUERY:"+query);
	    			    	  formObject.saveDataIntoDataSource(query);
	    				  }*/
	    			}
	    		}
	    		catch(Exception e){
	    			PL_SKLogger.writeLog("RLOSCommon", "Exception occurred in elig dectech"+printException(e));
	    			
	    		}
	    	}	
			if (outputResponse.contains("Output_Final_DBR")){
				
			 Output_Final_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Final_DBR>")+18,outputResponse.indexOf("</Output_Final_DBR>"));
				if (Output_Final_DBR!=null){
		    		formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
		    	}
				PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Final_DBR"+Output_Final_DBR);
			}
			if (outputResponse.contains("Output_Interest_Rate")){
				Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("<Output_Interest_Rate>")+22,outputResponse.indexOf("</Output_Interest_Rate>")); ;
				if (Output_Interest_Rate!=null){
		    		formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
		    	}
				PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Interest_Rate"+Output_Interest_Rate);
				
			}
			//Setting the value in ELIGANDPROD info
			//Setting the value in lIABILITY info
			if (outputResponse.contains("Output_Existing_DBR")){
			 Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>")); ;
			 if (Output_Existing_DBR!=null){
		    		formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
		    	}
			 PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Existing_DBR"+Output_Existing_DBR);
			
			}
			if (outputResponse.contains("Output_Affordable_EMI")){
				Output_Affordable_EMI = outputResponse.substring(outputResponse.indexOf("<Output_Affordable_EMI>")+23,outputResponse.indexOf("</Output_Affordable_EMI>")); ;
				PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

			}
			try{
			double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
			double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
			double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
			cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
			PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get sGeneralData"+tenor+" "+RateofInt+" "+out_aff_emi+" "+cac_calc_limit);
			}
			catch(Exception e){}
			
			if (outputResponse.contains("Output_Net_Salary_DBR")){
				Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>")); ;
				 if (Output_Net_Salary_DBR!=null){
			    		formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
			    	}
				 PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);
				
				}
			//Setting the value in lIABILITY info
			//Setting the value in creditCard iFrame
			
			if (outputResponse.contains("Grade")){
				Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>")); ;
				if (Grade!=null){
		    		formObject.setNGValue("cmplx_Decision_score_grade", Grade);
		    	}
				PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Grade"+Grade);
				
				}
			if (outputResponse.contains("Output_Eligible_Amount")){
				Output_Eligible_Amount = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Amount>")+24,outputResponse.indexOf("</Output_Eligible_Amount>")); ;
				PL_SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
				
				}
			//Setting the value in creditCard iFrame

				
				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//SKLogger_CC.writeLog("$$Value set for DECTECH->>","UpdateinputXML is:"+UpdateinputXML);
				String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_PersonalLoan","Child_WI ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				
				strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
				PL_SKLogger.writeLog("$$Value set for DECTECH->>","strOutputXml is:"+strOutputXml);

				String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
				String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
				PL_SKLogger.writeLog("$$Value set for DECTECH->>","mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);
				
			outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
			outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
			outputResponse = outputResponse+"</Response>";
			PL_SKLogger.writeLog("$$outputResponse ","inside outpute get outputResponse"+outputResponse);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(outputResponse));
		    
		    Document doc = builder.parse(is);
		    doc.getDocumentElement().normalize();
		    
		    PL_SKLogger.writeLog("Root element :" ,doc.getDocumentElement().getNodeName());
		    
		    NodeList nList = doc.getElementsByTagName("PM_Reason_Codes");
		    System.out.println("----------------------------");
		    
		    for (int temp = 0; temp < nList.getLength(); temp++) {
		    	String Reason_Decision="";
		    	Node nNode = nList.item(temp);
		          System.out.println("\nCurrent Element :" + nNode.getNodeName());
		            
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		    	Element eElement = (Element) nNode;
		        //System.out.println("Student roll no : " + eElement.getAttribute("rollno"));
		    	PL_SKLogger.writeLog("$$outputResponse ","inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
		    	PL_SKLogger.writeLog("$$outputResponse ","inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
		    	PL_SKLogger.writeLog("$$outputResponse ","inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());
				
		    	PL_SKLogger.writeLog("$$outputResponse ","inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());
		    	String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);//2
		    	String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
		    
		    		String subprodquery="Select Description from ng_MASTER_SubProduct_PL where code='"+subProd+"'";
		    		String apptypequery="Select Description from ng_master_applicationtype where code='"+appType+"'";
		    		List<List<String>> subprodqueryXML = formObject.getDataFromDataSource(subprodquery);
		    		List<List<String>> apptypequeryXML = formObject.getDataFromDataSource(apptypequery);
		    		ReasonCode= eElement.getElementsByTagName("Reason_Code").item(0).getTextContent();
					Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
					try{
						if(temp==0){
							
							DeviationCode=ReasonCode;}
						else{
							DeviationCode=DeviationCode+","+ReasonCode;}
						}
					catch(Exception e){
						
					}
					PL_SKLogger.writeLog("$$outputResponse ","Value of Reason_Decision"+Reason_Decision);
					if (temp==0){
						if (Reason_Decision.equalsIgnoreCase("D")){
							squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
						}
						else if (Reason_Decision.equalsIgnoreCase("R")){
							squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
						}
						else if (Reason_Decision.equalsIgnoreCase("A")){
							squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
						}
					}
					else{
						if (Reason_Decision.equalsIgnoreCase("D")){
							squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
						}
						else if (Reason_Decision.equalsIgnoreCase("R")){
							squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
						}
						else if (Reason_Decision.equalsIgnoreCase("A")){
							squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
						}
					}
					
					
		    	
		    	PL_SKLogger.writeLog("$$outputResponse ","Squery is"+squery);
				formObject.saveDataIntoDataSource(squery);
				
			}
			}
		    formObject.setNGValue("cmplx_Decision_Deviationcode", DeviationCode); 
			}
		}
		catch(Exception e){
			PL_SKLogger.writeLog("Dectech error", "Exception:"+e.getMessage());
    		
		}
		
	}
	
//Setting the value in the grid for DECTECH
	// Deepak Change to calculate EMI start
	public static String getEMI(double loanAmount,double rate,double tenureMonths)
	{       
		String loanAmt_DaysDiff="";
		try{
			if(loanAmount==0 ||rate==0||tenureMonths==0){
				return "0";
			}
			BigDecimal B_intrate= new BigDecimal(rate);
			BigDecimal B_tenure= new BigDecimal(tenureMonths);
			BigDecimal B_loamamount= new BigDecimal(loanAmount);
			loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);

		}
		catch(Exception e){
			PL_SKLogger.writeLog("RLOSCommon", "Exception occured in getEMI() : ");
			loanAmt_DaysDiff = "0";
		}
		return loanAmt_DaysDiff;
	}	
	public static BigDecimal calcEMI(BigDecimal P, BigDecimal N, BigDecimal ROI) {
		BigDecimal emi = new BigDecimal(0) ;
		try{
			MathContext mc = MathContext.DECIMAL128;     
			BigDecimal R = ROI.divide(new BigDecimal(1200),mc);
			BigDecimal nemi1 = P.multiply(R,mc);
			BigDecimal npower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal dpower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal denominator = dpower1.subtract(BigDecimal.ONE);
			BigDecimal numerator = nemi1.multiply(npower1);
			emi = numerator.divide(denominator,0,RoundingMode.UP);

		}
		catch(Exception e){
			PL_SKLogger.writeLog("RLOSCommon", "Exception occured in calcEMI() : ");
		}
		return emi;
	}
	public static String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);

			double seedvalue=Math.round(PMTEMI.doubleValue());
			double loamamount=B_loamamount.doubleValue();;
			int tenure=B_tenure.intValue();;
			double intrate=(B_intrate.intValue())/100.0;	
			System.out.println("seedvalue  **************"+seedvalue);
			System.out.println("loamamount  **************"+loamamount);
			System.out.println("tenure=  **************"+tenure);
			System.out.println("intrate  **************"+intrate);

			int iterations=2*(int)Math.round(PMTEMI.intValue()*.10);
			System.out.println("PMTEMI   **"+PMTEMI+"  for intrate @"+intrate+ " iterations"+iterations);
			loanAmt_DaysDiff=seedvalue+"";
		}
		catch(Exception e){
			PL_SKLogger.writeLog("RLOSCommon", "Exception occured in calcgoalseekEMI() : ");
			loanAmt_DaysDiff="0";
		}

		return loanAmt_DaysDiff;
	}
	public static double Cas_Limit(double aff_emi,double rate,double tenureMonths)
	{
		double pmt;
		try{
			double new_rate = (rate/100)/12;
			 pmt = (aff_emi)*(1-Math.pow(1+new_rate,-tenureMonths))/new_rate;
			System.out.println("final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			pmt=0;
		}
		return pmt;
		
	}
	//Akshay for comma Change
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
	 
	 public static String printException(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exception = sw.toString();
			return exception;
			
		}
	public String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		PL_SKLogger.writeLog("RLOS Common ", "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date.equals(null) || date.equals(""))
		{
			return new_date;
		}

		try{
			SimpleDateFormat sdf_old=new SimpleDateFormat(Old_Format);
			SimpleDateFormat sdf_new=new SimpleDateFormat(new_Format);
			new_date=sdf_new.format(sdf_old.parse(date));
		}
		catch(Exception e)
		{
			PL_SKLogger.writeLog("RLOS Common ", "Exception occurred in parsing date:"+e.getMessage());
		}
		return new_date;
	}
	
	public void income_Dectech(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		
		 String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5); // Changed because Emptype comes at 5
         PL_SKLogger.writeLog("PL", "Emp Type Value is:"+EmpType);

         if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
         {
         	formObject.setVisible("IncomeDetails_Frame3", false);
             formObject.setHeight("Incomedetails", 630);
             formObject.setHeight("IncomeDetails_Frame1", 605);  
         }
         else if(EmpType.equalsIgnoreCase("Self Employed"))
         {                                                                                                              
             formObject.setVisible("IncomeDetails_Frame2", false);
             formObject.setTop("IncomeDetails_Frame3",40);
             formObject.setHeight("Incomedetails", 300);
             formObject.setHeight("IncomeDetails_Frame1", 280);
         }
		
		//formObject.setLocked("IncomeDetails_Frame1",true);					 			
		formObject.setVisible("IncomeDetails_Label13",false);
		formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
		formObject.setVisible("IncomeDetails_Label15",true); 
		formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
		formObject.setVisible("IncomeDetails_Label16",true);
		formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
		// disha FSD
		formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
		formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_Flying_Avg",true);
		formObject.setLocked("cmplx_IncomeDetails_totSal",true);
		formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);
		
		if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true"))
		{
			formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking",false); //Arun (30/10) not to display this field incase of NTB customer
			formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat",false); //Arun (30/10) not to display this field incase of NTB customer
		}
		formObject.setVisible("IncomeDetails_Frame3",false);
	}
	
	public void employment_dectech(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="true" || formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="true")
		{
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
			formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
			formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
			formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
			formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
			formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
			formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
			formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
			
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatus",false);
			formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
			formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
			formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
			formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
			//formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
			
			formObject.setLocked("cmplx_EmploymentDetails_LOS",false);								
			
			formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
			formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);

			formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
			formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
			formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
		}
		if((formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="false") && (formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="false"))
		{
			// disha FSD
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL","CNOAL");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CNOAL ( disabled) /Awaiting FVR");
			formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
			formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
			formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
			formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
			formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
			formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
			formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
			formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
			
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",false);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",false);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
			formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
			formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
			formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
			formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
			//formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
			formObject.setLocked("cmplx_EmploymentDetails_LOS",false);
			formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmirateOfWork",false);
			formObject.setLocked("cmplx_EmploymentDetails_HeadOfficeEmirate",false);
			formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);
			formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
			formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
			formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
		}
		loadPicklist4();
		
	}
	public void decisionLabelsVisibility()	
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		//formObject.setVisible("cmplx_Decision_waiveoffver",false);
		formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
		formObject.setVisible("Decision_Label1",false);
		formObject.setVisible("DecisionHistory_Label1",false);
		formObject.setVisible("cmplx_Decision_refereason",false);
		formObject.setVisible("cmplx_Decision_IBAN",false);
		formObject.setVisible("DecisionHistory_Label6",false);
		formObject.setVisible("cmplx_Decision_AccountNo",false);
		formObject.setVisible("DecisionHistory_Label7",false);
		formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
		formObject.setVisible("DecisionHistory_Label8",false);
		formObject.setVisible("cmplx_Decision_DebitcardNumber",false);
		formObject.setVisible("DecisionHistory_Label9",false);
		formObject.setVisible("cmplx_Decision_desc",false);
		formObject.setVisible("DecisionHistory_Label5",false);
		formObject.setVisible("cmplx_Decision_strength",false);
		formObject.setVisible("DecisionHistory_Label3",false);
		formObject.setVisible("cmplx_Decision_weakness",false);
		formObject.setVisible("DecisionHistory_Label4",false);
		formObject.setVisible("cmplx_Decision_REMARKS",false);
		formObject.setVisible("DecisionHistory_chqbook",false);

		formObject.setVisible("DecisionHistory_Label2", false);
		formObject.setVisible("cmplx_Decision_Status", false);
	}
	//Deepak New method to remove blank Tag
	public  String Clean_Xml(String InputXml){
		String Output_Xml="";
		try{
			Document doc = getDocument(InputXml);
				removeEmptyNodes(doc);
				DOMSource domSource = new DOMSource(doc);
		       StringWriter writer = new StringWriter();
		       StreamResult result = new StreamResult(writer);
		       TransformerFactory tf = TransformerFactory.newInstance();
		       Transformer transformer = tf.newTransformer();
		       transformer.transform(domSource, result);
		      Output_Xml = writer.toString().substring(38);
		      //PL_SKLogger.writeLog("RLSO Common Clean_xml","final1: "+ Output_Xml);
			
		}
		catch(Exception e){
			
		}
		return Output_Xml;
	}
	public  void removeEmptyNodes(Node node) {
	    NodeList list = node.getChildNodes();
	    List<Node> nodesToRecursivelyCall = new LinkedList<Node>();
	    for (int i = 0; i < list.getLength(); i++) {
	        nodesToRecursivelyCall.add(list.item(i));
	    }
	    for(Node tempNode : nodesToRecursivelyCall) {
	        removeEmptyNodes(tempNode);
	    }
	    boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
	    boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
	    boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null"));
	    if (emptyElement || emptyText || selectText) {
	        if(!node.hasAttributes()) {
	            node.getParentNode().removeChild(node);
	        }
	    }
	  //  return node;
	}
}




