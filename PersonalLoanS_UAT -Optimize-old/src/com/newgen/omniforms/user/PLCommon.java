package com.newgen.omniforms.user;
import com.newgen.omniforms.FormConfig;
import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;
import com.newgen.omniforms.util.PL_EventListenerHandler;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class PLCommon extends Common{
	private static final long serialVersionUID = 1L;
	static Logger mLogger=PersonalLoanS.mLogger;
	final static double epsilon=0.0000001;

	public void getAge(String dateBirth,String controlName){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			mLogger.info("RLOS_Common"+ "Inside getAge(): "); 
			Calendar dob = Calendar.getInstance();
			Calendar today = Calendar.getInstance();

			mLogger.info("today"+dob+today);
			String[] parts;
			if (dateBirth.contains("/")){
				parts= dateBirth.split("/");
				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 
			}

			else if(dateBirth.contains("-")) {
				parts = dateBirth.split("-");
				dob.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])); 
			}
			Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			mLogger.info("age=="+age);
			int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
			mLogger.info("month=="+month);
			if (month<0){
				age--; 
				month= 12-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);
				if(today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
			}
			else if(month == 0 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)) {
				age--;
				month= 11-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);//If month is same as current no need to count it
			}
			else if(1<=month && month<=11 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
				month=month-1;
			}
			mLogger.info("Age is====== "+age+"."+month);

			formObject.setNGValue(controlName,(age+"."+month).toString(),false); 


		}

		catch(Exception e){
			mLogger.info("Exception:"+e);

		}
	}


	/*//Changes done to auto populate age in world check fragment
	public void getAgeWorldCheck(String dateBirth){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info("CC_Common"+ "Inside getAge(): "); 
		String[] parts = dateBirth.split("/");
		Calendar dob = Calendar.getInstance();
		Calendar today = Calendar.getInstance();

		dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

		Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
			age--; 
		}
		mLogger.info("CC_Common"+ "Values are: "+parts[2]+parts[1]+parts[0]+age); 


		formObject.setNGValue("WorldCheck1_age",age.toString(),false); 
	}

	// Commented on 31/10/17 by Akshay as using generalised getAge now
	 */

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
			mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}


	}

	public void LoadpicklistFCU()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info("PL_Common"+ "Inside loadPicklist");
		String Feedback=formObject.getNGValue("cmplx_Decision_feedbackstatus");
		mLogger.info("PL_Common"+ "Inside loadPicklist::"+Feedback);

		if("Positive".equalsIgnoreCase(Feedback))
		{
			formObject.setEnabled("cmplx_Decision_subfeedback", true);
			formObject.setNGValue("cmplx_Decision_subfeedback", "");
			LoadPickList("cmplx_Decision_subfeedback", "select '--Select--','--Select--' union select convert(varchar, Description) from NG_Master_Possubfeedbackstatus with (nolock)");
		}
		else if("Negative".equalsIgnoreCase(Feedback))
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
			mLogger.info("RLSOCommon"+ "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 


			String WI_Name = formObject.getWFWorkitemName();
			mLogger.info("RLSOCommon"+ "inside save_cif_data methos wi_name: "+WI_Name );
			Map<String, String> curr_entry;
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
				if(curr_entry.get("CustId").equalsIgnoreCase(Integer.toString(prim_cif))){//curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")

					Cif_data.add("Y");
				}
				else{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItemFromList("q_cif_detail", Cif_data);

				mLogger.info("RLSOCommon"+ "data to dave in cif details grid: "+ Cif_data);
				//curr_entry=null;----Useless assignment
				//Cif_data=null;----Useless assignment
			}
		}
		catch(Exception e){
			mLogger.info("Exception occured while saving data for Customer Eligibility : "+ e.getMessage());
			printException(e);
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
			mLogger.info("Exception occured while seting non primary CIF: "+ e.getMessage());
			printException(e);
		}

	}
	public void Fields_ApplicationType_Employment()
	{
		mLogger.info("RLOSCommon"+ "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if("RESCE".equalsIgnoreCase(formObject.getNGValue("Application_Type")) || "RESCN".equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
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
			mLogger.info("inside parse_cif_eligibility"+"");
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
			String passport_list = "";
			//Map<String, String> cif_details = new HashMap<String, String>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
			mLogger.info("name is doc : "+ doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			for (int nodelen=0; nodelen<nl.getLength();nodelen++){
				Map<String, String> cif_details = new HashMap<String, String>();
				nl.item(nodelen);
				if("CustomerDetails".equalsIgnoreCase(nl.item(nodelen).getNodeName()))//nl.item(nodelen).getNodeName().equalsIgnoreCase("CustomerDetails")
				{
					int no_of_product = 0;
					NodeList childnode  = nl.item(nodelen).getChildNodes();
					for (int childnodelen= 0;childnodelen<childnode.getLength();childnodelen++){
						String tag_name = childnode.item(childnodelen).getNodeName();
						String tag_value=childnode.item(childnodelen).getTextContent();
						if(tag_name!=null && tag_value!=null){
							if("Products".equalsIgnoreCase(tag_name)){
								++no_of_product;
								cif_details.put(tag_name, Integer.toString(no_of_product)+"");
							}else{
								if("PassportNum".equalsIgnoreCase(tag_name))
									passport_list = tag_value+ ","+passport_list;
								cif_details.put(tag_name, tag_value);
							}

						}
						else{
							mLogger.info("tag name and tag value can not be null:: tag name: "+tag_name+ " tag value: " +tag_value);
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
				mLogger.info("parse_cif_eligibility Primary CIF: "+Prim_cif+"");
				Map<String, String> prim_entry;
				prim_entry = Cus_details.get(Prim_cif);
				String primary_pass = prim_entry.get("PassportNum");
				passport_list = passport_list.replace(primary_pass, "");
				mLogger.info("Prim_cif: "+prim_entry.get("CustId"));
				set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
			}

			mLogger.info("Prim_cif: "+ Prim_cif+"");

		}
		catch(Exception e){
			mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}

	}
	//functions added with respect to change in Customer_Details call(Tanshu Aggarwal 29/05/2017)


	public void loadPicklistCustomer()  
	{
		// disha FSD
		mLogger.info("RLOS_Common"+ "Inside loadPicklistCustomer: ");

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

	}

	public void loadPicklistRiskRating()
	{
		mLogger.info("RLOS_Common"+ "Inside loadPicklistRiskRating: ");
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
		mLogger.info("RLOS_Common"+ "Inside loadPicklistProduct$"+ReqProd+"$");
		//String ProdType=formObject.getNGValue("Product_type");

		if(ReqProd!=null && "Personal Loan".equalsIgnoreCase(ReqProd)){
			mLogger.info("RLOS_Common"+ "Inside equalsIgnoreCase()"); 
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
	}

	//incoming doc function
	public void IncomingDoc(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		IRepeater repObj;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");

		String [] finalmisseddoc=new String[70];
		int rowRowcount=repObj.getRepeaterRowCount();
		mLogger.info("RLOS Initiation"+ "sQuery for document name is: rowRowcount" +  rowRowcount);
		if (repObj.getRepeaterRowCount() != 0) {

			for(int j = 0; j < rowRowcount; j++)
			{
				String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
				mLogger.info("RLOS Initiation"+ "sQuery for document name is: DocName" +  DocName);

				String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
				mLogger.info("RLOS Initiation"+ "sQuery for document name is: Mandatory" +  Mandatory);

				if("Y".equalsIgnoreCase(Mandatory))
				{
					String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex")==null?"":repObj.getValue(j,"cmplx_DocName_DocIndex");
					mLogger.info(""+"DocIndex"+DocIndex);
					String StatusValue=repObj.getValue(j,"cmplx_DocName_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Status");
					mLogger.info(""+"StatusValue"+StatusValue);
					String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks")==null?"":repObj.getValue(j, "cmplx_DocName_Remarks");
					mLogger.info(""+"Remarks"+Remarks);

					if(DocIndex==null || "".equalsIgnoreCase(DocIndex)){
						mLogger.info(""+"StatusValue inside DocIndex"+DocIndex);
						if("Received".equalsIgnoreCase(StatusValue)){
							mLogger.info(""+"StatusValue inside DocIndex recieved");
							//below line commented as this mandatory document is already received. 
							// finalmisseddoc[j]=DocName;
						}

						else if("Deferred".equalsIgnoreCase(StatusValue)){
							formObject.setNGValue("is_deferral_approval_require","Y");
							formObject.RaiseEvent("WFSave");
							mLogger.info("Deferred flag value inside no document"+formObject.getNGValue("is_deferral_approval_require"));
							if("".equalsIgnoreCase(Remarks)){
								mLogger.info("It is Mandatory to fill Remarks"+"As you have not attached the Mandatory Document and the status is Deferred");
								throw new ValidatorException(new FacesMessage("As you have Deferred "+DocName+" Document,So kindly fill the Remarks"));
							}
							else if(!"".equalsIgnoreCase(Remarks)|| Remarks==null){
								mLogger.info("You may proceed further"+"Proceed further");
							}
						}
						else if("Waived".equalsIgnoreCase(StatusValue)){
							formObject.setNGValue("is_waiver_approval_require","Y");
							formObject.RaiseEvent("WFSave");
							mLogger.info("waived flag value inside no document"+formObject.getNGValue("is_waiver_approval_require"));
							if("".equalsIgnoreCase(Remarks)){
								mLogger.info("It is Mandatory to fill Remarks"+"As you have not attached the Mandatory Document and the status is Waived");
								throw new ValidatorException(new FacesMessage("As you have Waived "+DocName+" Document,So kindly fill the Remarks"));
							}
							else if(!"".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){
								mLogger.info("You may proceed further"+"Proceed further");
							}
						}
						else if("--Select--".equalsIgnoreCase(StatusValue)||("".equalsIgnoreCase(StatusValue))){
							mLogger.info(""+"StatusValue inside DocIndex is blank");
							finalmisseddoc[j]=DocName;
						}
						else if("Pending".equalsIgnoreCase(StatusValue)){
							mLogger.info(""+"StatusValue of doc is Pending");

						}
					}
					else{
						if(!("".equalsIgnoreCase(DocIndex))){
							if(!"Received".equalsIgnoreCase(StatusValue)){
								repObj.setValue(j,"cmplx_DocName_Status","Received");
								repObj.setEditable(j, "cmplx_DocName_Status", false);
								mLogger.info(""+"StatusValue::123final"+StatusValue);
							}
							else {

								mLogger.info(""+"StatusValue::123final status is already received");
							}
						}
					}

				}
			}
		}
		StringBuilder mandatoryDocName = new StringBuilder("");

		mLogger.info(""+"length of missed document"+finalmisseddoc.length);
		mLogger.info(""+"length of missed document mandatoryDocName.length"+mandatoryDocName.length());

		for(int k=0;k<finalmisseddoc.length;k++)
		{
			if(null != finalmisseddoc[k]) {
				mandatoryDocName.append(finalmisseddoc[k]).append(",");
			}
			mLogger.info("RLOS Initiation"+ "finalmisseddoc is:" +finalmisseddoc[k]);
			mLogger.info(""+"length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
		mLogger.info("RLOS Initiation"+ "misseddoc is:" +mandatoryDocName.toString());

		if(mandatoryDocName.length()<=0){

			mLogger.info("RLOS Initiation"+ "misseddoc is: inside if condition");

		}
		else{
			mLogger.info("RLOS Initiation"+ "misseddoc is: inside if condition");
			mLogger.info(""+"length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
			throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
		}
	}
	//incomingdoc function


	// disha FSd
	public void loadPickListOECD()
	{
		LoadPickList("OECD_CRSFlagReason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by Code");
		LoadPickList("OECD_CountryBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("OECD_townBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by Code");
		LoadPickList("OECD_CountryTaxResidence", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");

	}


	public void setDisabled()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->setDisabled()");
		String fields="cmplx_Customer_FirstNAme,cmplx_Customer_MiddleNAme,cmplx_Customer_LastNAme,cmplx_Customer_Age,cmplx_Customer_DOb,cmplx_Customer_gender,cmplx_Customer_EmiratesID,cmplx_Customer_IdIssueDate,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_MotherName,cmplx_Customer_PAssportNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VisaNo,cmplx_Customer_VisaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_MobileNo,cmplx_Customer_CIFNo";
		String[] array=fields.split(",");
		for(int i=0;array[i]!=null;i++)
		{
			mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->setDisabled()"+array[i]);

			formObject.setEnabled(array[i], false);

		}
	}


	public void loadPicklist3()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try
		{
			mLogger.info("RLOSCommon"+ "Inside loadpicklist3:");
			LoadPickList("cmplx_Decision_refereason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock) order by code");
			formObject.clear("cmplx_Decision_Decision");
			String Query = "select '--Select--' as Decision union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and workstepname='"+formObject.getWFActivityName()+"' order by Decision desc";
			mLogger.info("RLOSCommon Load desison Drop down: "+Query );
			LoadPickList("cmplx_Decision_Decision", Query);
			LoadPickList("cmplx_Decision_CADDecisiontray", "select ' --Select--' as Refer_Credit union select Refer_Credit from NG_Master_ReferCredit"); //Arun (12/10)			
			LoadPickList("cmplx_Decision_Decreasoncode", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock) order by code");
		}
		catch(Exception e){ 
			mLogger.info("PLCommon"+"Exception Occurred loadPicklist3 :"+e.getMessage());
			printException(e);
		}
	}


	public void loadPicklist_Address()
	{
		try
		{
			mLogger.info("RLOS_Common"+ "Inside loadPicklist_Address: "); 
			LoadPickList("AddressDetails_addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock) order by code");
			LoadPickList("AddressDetails_city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
			LoadPickList("AddressDetails_state", "select '--Select--'as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
			LoadPickList("AddressDetails_country", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		}	
		catch(Exception e){ 
			mLogger.info("PLCommon"+"Exception Occurred loadPicklist_Address :"+e.getMessage());
			printException(e);
		}
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
		mLogger.info("RLOS_Common"+ "Inside loadPicklist_PartMatch: ");
		LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}

	//tanshu started

	// Changes done for OV - Same incoming doc repeater will be visible on OV now with columns visible and editable on basis of workstep

	public void fetchIncomingDocRepeater(){

		mLogger.info(" Inside loadAllCombo_LeadManagement_Documents_Deferral"+ "inside fetchIncomingDocRepeater");
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName = formobject.getConfigElement("ActivityName");
		mLogger.info("INSIDE INCOMING DOCUMENT:" +"");
		String Username=formObject.getUserName();//Arun (22/09/17)
		String requested_product ="";
		String requested_subproduct;
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +"valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +requested_subproduct);

			}    
		}
		mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" +requested_product);

		//Disha started (10/6/17)

		List<String> repeaterHeaders = new ArrayList<String>();
		mLogger.info("INSIDE INCOMING DOCUMENT value sActivityName for:" +sActivityName);
		if ("Original_Validation".equalsIgnoreCase(sActivityName) || "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
		{
			mLogger.info("INSIDE INCOMING DOCUMENT value sActivityName for:" +"OV activity");
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
			mLogger.info("INSIDE INCOMING DOCUMENT value sActivityName for:" +" non OV activity");
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
		mLogger.info("INSIDE INCOMING DOCUMENT:" +"after making headers");

		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> docName ;
		String documentName = null;
		String documentNameMandatory=null;
		String statusValue = null;
		String expiryDate = null;//Arun (22/09/17)
		String Remarks= null;//Arun (22/09/17)
		String DocInd= null;//Arun (22/09/17)

		String query ;

		IRepeater repObj;
		mLogger.info("INSIDE INCOMING DOCUMENT:" +"after creating the object for repeater");

		int repRowCount = 0;

		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");

		mLogger.info("INSIDE INCOMING DOCUMENT:" +""+repObj.toString());


		// query = "SELECT distinct DocName,Mandatory,Status,Remarks, FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
		//query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='LE-0000000007308-RLOS'";
		query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getNGValue("Parent_WIName")+"'";
		docName = formObject.getNGDataFromDataCache(query);


		//docName = formObject.getNGDataFromDataCache(query);
		//        SKLogger.writeLog("Incomingdoc",""+ docName);

		try{

			/*if (repObj.getRepeaterRowCount() == 0) {

            mLogger.info("RLOS incoming document"+ "when row count is zero");
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"'";
            //query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='Personal Loan' and SubProductName='Expat Personal Loans'";
            docName = formObject.getNGDataFromDataCache(query);
            mLogger.info("Incomingdoc"+""+ docName);
        //    repObj.clear();

        }
        if (repObj.getRepeaterRowCount() != 0) {

            mLogger.info("RLOS incoming document"+ "when row count is not zero");
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"' and ProcessName='RLOS'";
            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"'";
            //query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='Personal Loan' and SubProductName='Expat Personal Loans' and  wi_name='"+formObject.getWFWorkitemName()+"'";
            docName = formObject.getNGDataFromDataCache(query);
            mLogger.info("Incomingdoc"+""+ docName);


        }*/
			repObj.setRepeaterHeaders(repeaterHeaders);

			if ("Original_Validation".equalsIgnoreCase(sActivityName) )
			{
				mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);
					statusValue = docName.get(i).get(3);
					expiryDate = docName.get(i).get(1);//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

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
					if("Recieved".equalsIgnoreCase(statusValue))
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

					mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
			}
			if ( "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
			{
				mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);	          
					statusValue = docName.get(i).get(3);
					expiryDate = docName.get(i).get(1);//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);//Arun (22/09/17)
					repObj.setValue(i,3,statusValue);//Arun (22/09/17)
					repObj.setValue(i,4,Remarks);//Arun (22/09/17)
					repObj.setValue(i,11,DocInd);//Arun (22/09/17)
					repObj.setColumnDisabled(0, true);//Arun (22/09/17)
					repObj.setColumnDisabled(2, true);//Arun (22/09/17)

					repRowCount = repObj.getRepeaterRowCount();

					mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
			}
			else
			{
				mLogger.info("Repeater Row Count to add row maker csm"+ "add row CSm maker ");


				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();

					repObj.setColumnVisible(12, false);
					repObj.setColumnVisible(13, false);
					repObj.setColumnVisible(14, false);

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);
					expiryDate = docName.get(i).get(1);//Arun (22/09/17)
					statusValue = docName.get(i).get(3);//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory); 	     
					repObj.setValue(i,1,expiryDate);//Arun (22/09/17)
					repObj.setValue(i,3,statusValue);//Arun (22/09/17)
					repObj.setValue(i,4,Remarks);//Arun (22/09/17)
					repObj.setValue(i,11,DocInd);//Arun (22/09/17)
					repObj.setColumnDisabled(0, true);//Arun (22/09/17)
					repObj.setColumnDisabled(2, true);//Arun (22/09/17)
					repObj.setDisabled(i, "cmplx_DocName_DocName", true);//Arun (22/09/17)
					if(statusValue == null){
						statusValue = "--Select--"; 
					}//Arun (22/09/17)
					repObj.setValue(i, 3, statusValue);//Arun (22/09/17)
					repObj.setValue(i, 4, Remarks);//Arun (22/09/17)

					repRowCount = repObj.getRepeaterRowCount();

					mLogger.info("Repeater Row Count "+ " " + repRowCount);

				} //Disha ended (10/6/17)
			}
		}
		catch (Exception e) {

			mLogger.info("EXCEPTION    :    "+ " " + e.toString());
			printException(e);

		} 
	}

	public void loadInDecGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String listviewName ="Decision_ListView1";//Arun (23/09/17)
			String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm:ss'),userName,workstepName,Decisiom,remarks,'',dec_wi_name from ng_rlos_gr_Decision with (nolock) where dec_wi_name='"+ParentWI_Name+"' or dec_wi_name='"+formObject.getWFWorkitemName()+"' order by datelastchanged desc";
			mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
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
				mLogger.info("CC> "+"Inside CCCommon ->loadInDecGrid()saurabh arraylist"+mylist);
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
		}catch(Exception e){ 
			mLogger.info("PLCommon"+"Exception Occurred loadInDecGrid :"+e.getMessage());
			printException(e);
		}	
	}


	public void loadInReferGrid()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm'),userName,workstepName,Decisiom,remarks,TargetDecision,workitemID,history_wi_nAme from ng_rlos_gr_ReferHistory with (nolock) where  history_wi_nAme='"+formObject.getWFWorkitemName()+"'";
		mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->loadInReferGrid()"+query);
		List<List<String>> list=formObject.getDataFromDataSource(query);
		mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->Query Result is: "+list.toString());
		for (List<String> a : list) 
		{

			formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory", a);
		}

	}

	public void loadPicklist4()    
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info("RLOSCommon"+ "Inside loadpicklist4:"); 
		String reqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");
		if("Personal Loan".equalsIgnoreCase(reqProd)){
			if("BAU".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
			}
			else if("Surrogate".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'PL' or product='B') order by code");	
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
		LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMacro with (nolock) order by code");
		//below query changed bby saurabh on 22nd Oct as wrong masters were being loaded.
		LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select Micro from(select '--Select--' as micro union select distinct MICRO from NG_MASTER_EmpIndusMacroAndMicro) new_table order by case when micro='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType","select '--Select--' union select convert(varchar, Description) from NG_MASTER_EmpContractType where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_PromotionCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_collectioncode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CollectionCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL where isActive='Y'  order by code");


		LoadPickList("cmplx_EmploymentDetails_marketcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' and Product = 'PL' order by code");	
	}

	public void Eligibilityfields()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info("RLOS_Common"+ "Inside loadPicklist_Address: "); 


		mLogger.info("CC"+ "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+6));
		if("SE".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || "BTC".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))){
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

			mLogger.info("CC @yash"+ "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+6));


		}
		else if(("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) || "Salaried Pensioner".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) ) && !"IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
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

		mLogger.info("PL_Common"+ "Inside saveIndecisionGrid: "); 
		String entrydate ; //Arun (23/09/17)
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String EntrydateTime = formObject.getNGValue("Entry_date_time") ; //Arun (23/09/17)
		String[] parts = EntrydateTime.split("/"); //Arun (23/09/17)
		entrydate = parts[1]+"/"+parts[0]+"/"+parts[2]; //Arun (23/09/17)
		mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+EntrydateTime); 
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		mLogger.info("PL_Common"+ "Inside saveIndecisionGrid...currDate "+currDate); 
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks, dec_wi_name,Entry_Date) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+entrydate+"' as datetime),102))";
		mLogger.info("PL_Common"+"Query is"+query);
		formObject.saveDataIntoDataSource(query);

	}	

	public void saveIndecisionGridCSM(){
		mLogger.info("PL_Common"+ "Inside saveIndecisionGrid: "); 
		String entrydate ;       
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Cifid=formObject.getNGValue("DecisionHistory_CIFid");
		String emiratesid=formObject.getNGValue("DecisionHistory_Emiratesid");
		String custName=formObject.getNGValue("DecisionHistory_Customer_Name");
		mLogger.info("PersonnalLoanS"+"Final val of custName:"+ custName);
		String EntrydateTime = formObject.getNGValue("Entry_date_time") ;
		String[] parts = EntrydateTime.split("/");
		entrydate = parts[1]+"/"+parts[0]+"/"+parts[2]; 
		mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+EntrydateTime); 
		mLogger.info("PL_Common"+ "Inside  saveIndecisionGrid entrydate "+entrydate); 
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime()); //Arun (16/10) included HH:mm to show the time as well in the grid
		mLogger.info("PL_Common"+ "Inside saveIndecisionGrid...currDate "+currDate); 
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks, dec_wi_name,Entry_Date,CifId,EmiratesId,CustName) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+entrydate+"' as datetime),102),'"+Cifid+"','"+emiratesid+"','"+custName+"')";
		mLogger.info("PL_Common"+"Query is"+query);
		formObject.saveDataIntoDataSource(query);
	}

	public void AddInReferGrid()
	{
		mLogger.info("PL_Common"+ "Inside AddInReferGrid: "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		String decision=formObject.getNGValue("cmplx_Decision_Decision");

		String query1="select workitemid from wfinstrumenttable where processinstanceid='"+formObject.getWFWorkitemName()+"' and activityname='"+formObject.getWFActivityName()+"'";
		List<List<String>> mylist=formObject.getDataFromDataSource(query1);
		mLogger.info("PL_Common"+"Query1 is"+query1);
		mLogger.info("PL_Common"+"Query1 Result is"+mylist.toString());

		/*if(decision.contains("Refer")){
    		String query2="insert into NG_RLOS_ReferHistory(dateLastChanged, userName, workstepName, Decisiom, remarks,wi_name) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+decision+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','" +formObject.getWFWorkitemName()+"')";
        	formObject.saveDataIntoDataSource(query2);
        	mLogger.info("PL_Common"+"Query is:"+query2);
    	}*/

		List<String> Referlist=new ArrayList<String>();
		Referlist.add(currDate);
		Referlist.add(formObject.getUserName());
		Referlist.add(formObject.getWFActivityName());
		Referlist.add(decision);
		Referlist.add(formObject.getNGValue("cmplx_Decision_REMARKS"));
		Referlist.add("");
		Referlist.add(mylist.get(0).get(0));
		Referlist.add(formObject.getWFWorkitemName());

		formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory",Referlist);
		mLogger.info("PL_Common"+"ReferList is:"+Referlist.toString());
	}

	public void  loadPicklist1()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			mLogger.info("PL"+"Inside PLCommon ->loadPicklist1()"+ActivityName);

			LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");
			LoadPickList("cmplx_Decision_CADDecisiontray", "select ' --Select--' as Refer_Credit union select Refer_Credit from NG_Master_ReferCredit"); //Arun (12/10)
		}
		catch(Exception e){ 
			mLogger.info("PLCommon"+"Exception Occurred loadPicklist1 :"+e.getMessage());
			printException(e);
		}	
	}



	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize)
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);

		if ("EMploymentDetails_Button2".equalsIgnoreCase(sControlName)) 
			objPickList.setWindowTitle("Search Employer");

		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
		if(!result.isEmpty()){//result.size()>0
			objPickList.setHeight(600);
			objPickList.setWidth(800);
			objPickList.setVisible(true);
			objPickList.setSearchEnabled(true);
			objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));
			mLogger.info("EventListenerHandler: Result Of Query:"+result.toString());   
			objPickList.populateData(result);

		}
		else{
			throw new ValidatorException(new FacesMessage("No Data Found"));
		}
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
		formObject.setNGValue("PartMatch_mno1",formObject.getNGValue("cmplx_Customer_MobNo"));
		formObject.setNGValue("PartMatch_mno2",formObject.getNGValue("AlternateContactDetails_MobileNo2"));//Arun (23/10) to autopopulate values here
		formObject.setNGValue("PartMatch_Dob",formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("PartMatch_EID",formObject.getNGValue("cmplx_Customer_EmiratesID"));
		formObject.setNGValue("PartMatch_nationality",formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("PartMatch_drno",formObject.getNGValue("cmplx_Customer_DLNo"));
		formObject.setNGValue("PartMatch_oldpass",formObject.getNGValue("cmplx_Customer_PAssportNo")); //Arun (07/09/17)

	}

	//moved to integration_output.java




	//moved to integration_output.java

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
		mLogger.info("RLOS_Common"+ "Inside LoadPicklistVerification(): "+mylist);

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
			mLogger.info("PL"+"Inside PLCommon ->loadPicklistChecker()"+ActivityName);

			LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"' and Initiation_Type NOT LIKE  '%Reschedulment%'");
		}
		catch(Exception e){ 
			mLogger.info("PLCommon"+"Exception Occurred loadPicklistChecker :"+e.getMessage());
			printException(e);
		}	
	}	
	//Method changed to handle cif with 0
	public int primary_cif_identify(Map<Integer, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry ;
			Map<String, String> curr_entry ;


			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();

				curr_entry = entry.getValue();
				if("Internal".equalsIgnoreCase(curr_entry.get("SearchType")))
				{
					if(primary_cif==0 && curr_entry.containsKey("Products")){
						primary_cif = entry.getKey();
					}
					else if(curr_entry.containsKey("Products"))
					{
						prim_entry = cusDetails.get(Integer.toString(primary_cif)+"");
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
			mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}

		return primary_cif;
	}

	//Code for Customer Updated.
	public String CustomerUpdate(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg = "";
		String outputResponse;
		String cif_verf_status;
		String   ReturnCode;

		cif_verf_status = formObject.getNGValue("is_cust_verified");
		String Existingcust = formObject.getNGValue("cmplx_Customer_NTB");
		mLogger.info("PL DDVT Vhecker"+ "EXISTING CUST : "+ Existingcust);
		if("false".equalsIgnoreCase(Existingcust)){
			cif_verf_status = "Y";
		}
		String Cif_lock_status = formObject.getNGValue("Is_CustLock");
		//String Cif_unlock_status = formObject.getNGValue("is_cust_verified");
		mLogger.info("PL DDVT Vhecker"+ "cif_verf_status : "+ cif_verf_status);
		mLogger.info("PL DDVT Vhecker"+ "cif_Lock_status : "+ Cif_lock_status);
		if ("".equalsIgnoreCase(cif_verf_status)||"N".equalsIgnoreCase(cif_verf_status)){
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_verify");

			ReturnCode = outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if("0000".equalsIgnoreCase(ReturnCode)){
				formObject.setNGValue("is_cust_verified", "Y");
				cif_verf_status="Y";
				alert_msg="Customer verified Sucessfully";
			}
			else{
				formObject.setNGValue("is_cust_verified", "N");
				mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Enquiry operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";

				// throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}

		if ("Y".equalsIgnoreCase(cif_verf_status)&&("".equalsIgnoreCase(Cif_lock_status)||"N".equalsIgnoreCase(Cif_lock_status)))
		{
			mLogger.info("PL DDVT Checker"+ "Inside Lock and Update Customer");
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
			ReturnCode = outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if("0000".equalsIgnoreCase(ReturnCode))
			{
				Cif_lock_status="Y";
				mLogger.info("PL DDVT Checker"+ "Locked sucessfully and now Unlock and update customer");
				formObject.setNGValue("Is_CustLock", "Y");
				outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
				ReturnCode =  outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

				if("0000".equalsIgnoreCase(ReturnCode))
				{
					formObject.setNGValue("Is_CustLock", "N");
					Cif_lock_status="N";
					mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock sucessfull");

					outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					mLogger.info("RLOS value of ReturnCode"+ReturnCode);

					if("0000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
						mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						PL_Integration_Output.valueSetIntegration(outputResponse);    
						formObject.setEnabled("DecisionHistory_Button3", false);
						mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
						mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						alert_msg = "Customer Updated Successful!";
					}
					else{
						alert_msg= "Customer Update operation failed, Please try after some time or contact administrator";
						formObject.setEnabled("DecisionHistory_Button3", true);
						mLogger.info("Customer Details"+"Customer Update operation Failed");
						formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
					}
					mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					formObject.RaiseEvent("WFSave");
					mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");

					//throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock failed return code: "+ReturnCode);
					alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";

					// throw new ValidatorException(new FacesMessage(alert_msg));
				}

			}
			else{
				formObject.setNGValue("Is_CustLock", "N");
				mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Lock operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";

				// throw new ValidatorException(new FacesMessage(alert_msg));
			}

		}
		else if("Y".equalsIgnoreCase(cif_verf_status)&& "Y".equalsIgnoreCase(Cif_lock_status))
		{
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if("0000".equalsIgnoreCase(ReturnCode))
			{
				formObject.setNGValue("Is_CustLock", "N");
				Cif_lock_status="N";
				mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock sucessfull");

				outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if("0000".equalsIgnoreCase(ReturnCode)){
					formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
					mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					PL_Integration_Output.valueSetIntegration(outputResponse);    
					mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
					mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				}
				else{
					mLogger.info("Customer Details"+"ACCOUNT_MAINTENANCE_REQ is not generated");
					formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
				}
				mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				formObject.RaiseEvent("WFSave");
				mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");
				if("Y".equalsIgnoreCase(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ")))
				{ 
					mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition");
					formObject.setEnabled("DecisionHistory_Button3", false); 
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","FetchDetails#Customer Updated Successful!!","",hm));
				}
				else{
					formObject.setEnabled("DecisionHistory_Button3", true);
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","FetchDetails#Customer Updated Fail!!","",hm));
				}

			}
			else{
				mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock failed return code: "+ReturnCode);
				alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";

				//throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}
		return alert_msg;
	}


	public void fetchoriginalDocRepeater(){

		//SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		mLogger.info("INSIDE OriginalDocument:" +"");

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

		mLogger.info("INSIDE Original Validation:" +"after making headers");



		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> Day = null;

		String documentName = null;


		IRepeater repObj;

		int repRowCount = 0;



		repObj = formObject.getRepeaterControl("OriginalValidation_Frame1");

		mLogger.info("INSIDE Original Validation:" +""+repObj.toString());



		//query = "SELECT distinct DAY FROM NG_RLOS_FinacleCore WHERE ProcessName='CreditCard'";

		/*ProcessDefId IN" + "(SELECT ProcessDefId FROM PROCESSDEFTABLE WHERE ProcessName ='"+processName+"')";*/



		//Day = //formObject.getNGDataFromDataCache(query);

		mLogger.info("docName"+""+ Day);





		try{

			if (repObj.getRepeaterRowCount() != 0) {

				mLogger.info("RLOS Original Validation"+ "CLeared repeater object");

				repObj.clear();

			}
			repObj.setRepeaterHeaders(repeaterHeaders);

			for(int i=0;i<4;i++ ){
				repObj.addRow();

				//documentName = Day.get(i).get(0);

				mLogger.info("Column Added in Repeater"+" "+ documentName);

				//repObj.setValue(i, 0, documentName);

				repRowCount = repObj.getRepeaterRowCount();

				mLogger.info("Repeater Row Count "+ " " + repRowCount);

			}

		}

		catch (Exception e) {

			mLogger.info("EXCEPTION    :    "+ " " + e.toString());
			printException(e);

		} 

	}
	// Aman Changes done for internal and external liability for the mapping on 18th sept 2017 starts


	/*	
	//Code Changes to custom coding for dectech code
	public String getYearsDifference(FormReference formObject,String controlName) {
		int MON;
		int Year;
		String age="";
		String DOB=formObject.getNGValue(controlName);
		mLogger.info("RLOS COMMON"+" Inside age + "+DOB);
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
----Unused code....use generalised getAge anyway!!!!!*/

	// Deepak Change to calculate EMI start
	public  String getEMI(double loanAmount,double rate,double tenureMonths)
	{       
		String loanAmt_DaysDiff="";
		try{
			if(Math.abs(loanAmount)<epsilon ||Math.abs(rate)<epsilon || Math.abs(tenureMonths)<epsilon){
				return "0";
			}
			BigDecimal B_intrate=  BigDecimal.valueOf(rate);
			BigDecimal B_tenure=  BigDecimal.valueOf(tenureMonths);
			BigDecimal B_loamamount= BigDecimal.valueOf(loanAmount);
			loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);

		}
		catch(Exception e){
			mLogger.info("RLOSCommon"+ "Exception occured in getEMI() : ");
			printException(e);
			loanAmt_DaysDiff = "0";
		}
		return loanAmt_DaysDiff;
	}	
	public  BigDecimal calcEMI(BigDecimal P, BigDecimal N, BigDecimal ROI) {
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
			mLogger.info("RLOSCommon"+ "Exception occured in calcEMI() : ");
			printException(e);
		}
		return emi;
	}
	public  String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);

			double seedvalue=Math.round(PMTEMI.doubleValue());
			double loamamount=B_loamamount.doubleValue();
			int tenure=B_tenure.intValue();
			double intrate=(B_intrate.intValue())/100.0;	
			mLogger.info("seedvalue  **************"+seedvalue);
			mLogger.info("loamamount  **************"+loamamount);
			mLogger.info("tenure=  **************"+tenure);
			mLogger.info("intrate  **************"+intrate);

			int iterations=2*(int)Math.round(PMTEMI.intValue()*.10);
			mLogger.info("PMTEMI   **"+PMTEMI+"  for intrate @"+intrate+ " iterations"+iterations);
			loanAmt_DaysDiff=Double.toString(seedvalue)+"";
		}
		catch(Exception e){
			mLogger.info("RLOSCommon"+ "Exception occured in calcgoalseekEMI() : ");
			printException(e);
			loanAmt_DaysDiff="0";
		}

		return loanAmt_DaysDiff;
	}
	//moved to separate file

	//Akshay for comma Change
	//moved to separate file

	public  static void printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		mLogger.info("Inside exception :"+"\n"+e.getMessage()+" : \n"+exception);	

	}

	public static String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		mLogger.info("RLOS Common "+ "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date==null || date.equals(""))
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
			mLogger.info("RLOS Common "+ "Exception occurred in parsing date:"+e.getMessage());
			printException(e);
		}
		return new_date;
	}

	public void income_Dectech(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();


		String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
		mLogger.info("PL"+ "Emp Type Value is:"+EmpType);

		if("Salaried".equalsIgnoreCase(EmpType)|| "Salaried Pensioner".equalsIgnoreCase(EmpType))
		{
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 630);
			formObject.setHeight("IncomeDetails_Frame1", 605);  
		}
		else if("Self Employed".equalsIgnoreCase(EmpType))
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

		if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
		{
			formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking",true);
			formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat",true);
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
}




