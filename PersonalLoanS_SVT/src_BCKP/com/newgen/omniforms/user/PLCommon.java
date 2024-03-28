package com.newgen.omniforms.user;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;

import com.newgen.omniforms.util.Common;
import com.newgen.omniforms.util.PL_EventListenerHandler;



public class PLCommon extends Common{
	private static final long serialVersionUID = 1L;
	static Logger mLogger=PersonalLoanS.mLogger;
	final static double epsilon=0.0000001;

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to calculate Age from DOB

	 ***********************************************************************************  */

	public void getAge(String dateBirth,String controlName){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("RLOS_Common123"+ "Inside getAge(): "); 
			Calendar dob = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
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
				PersonalLoanS.mLogger.info("age=="+age);
				int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
				PersonalLoanS.mLogger.info("month=="+month);
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
				
				String month1=Integer.toString(month);
				if (month1.length()==1){
					month1="0"+month1;
					if (month1.equalsIgnoreCase("00")){
						month1="01";
					}
				}
				PersonalLoanS.mLogger.info("Age is====== "+age+"."+month);

				formObject.setNGValue(controlName,(age+"."+month1).toString(),false); 
			
			
		}

		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception:"+e.getMessage());
			printException(e);
		}
}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 29/05/2017)             
	Author                              : Tanshu Aggarwal              
	Description                         : Function added with respect to change in Customer_Details call     

	 ***********************************************************************************  */



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

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}


	}
	
	/*public void loadDataInBlacklistgrid(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int rowCount = formObject.getLVWRowCount("cmplx_ExtBlackList_cmplx_gr_ExtBlackList");
		if(rowCount==0){
			String query = "select CONCAT(cust.FirstName,' ',cust.MiddleName,' ',cust.LAstName) as Fullname,cust.PassportNo,cif.CustId,cif.BlacklistFlag,cif.BlacklistDate,cif.BlacklistReasonCode from ng_rlos_cif_detail cif,ng_RLOS_Customer cust where cif_wi_name ='"+formObject.getNGValue("Parent_WIName")+"' and cif_wi_name=wi_name and cif_SearchType = 'External'";
			PersonalLoanS.mLogger.info("Query for External Blacklist is : "+query );
			List<List<String>> records = formObject.getNGDataFromDataCache(query);
			for(List<String> record : records){
				formObject.addItemFromList("cmplx_ExtBlackList_cmplx_gr_ExtBlackList", record);
			}
		}
		 
	}*/

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to load combos on FCU Fragment    

	 ***********************************************************************************  */
	public void LoadpicklistAltcontactDetails()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside loadPicklist");
		
	LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");
	String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
	LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
	
	}
	
	public void LoadpicklistCardDetails()
	{
	LoadPickList("cmplx_CardDetails_statCycle","select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
	LoadPickList("cmplx_CardDetails_MarketCode", " select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' order by code");
	LoadPickList("cmplx_CardDetails_CustClassification","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' order by code");
	
	}
	
	public void Loadpicklistfatca()
	{
	LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");
	LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	LoadPickList("cmplx_FATCA_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
	}
	public void LoadpicklistFCU()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside loadPicklist");
		String Feedback=formObject.getNGValue("cmplx_Decision_feedbackstatus");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside loadPicklist::"+Feedback);

		if("Positive".equalsIgnoreCase(Feedback))
		{
			formObject.setEnabled("cmplx_Decision_subfeedback", true);
			formObject.setNGValue("cmplx_Decision_subfeedback", "");
			LoadPickList("cmplx_Decision_subfeedback", "select '--Select--' union all select convert(varchar, Description) from NG_Master_Possubfeedbackstatus with (nolock) where IsActive='Y'");
		}
		else if("Negative".equalsIgnoreCase(Feedback))
		{
			formObject.setEnabled("cmplx_Decision_subfeedback", true);
			formObject.setNGValue("cmplx_Decision_subfeedback", "");
			LoadPickList("cmplx_Decision_subfeedback", "select '--Select--' union all select convert(varchar, Description) from NG_Master_subfeedbackstatus with (nolock) where IsActive='Y'");
		}

		else{

			formObject.setEnabled("cmplx_Decision_subfeedback", false);
			formObject.setNGValue("cmplx_Decision_subfeedback", " ");
		}
	}//Arun (07/09/17)

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to Hide/Unhide Employment fields   

	 ***********************************************************************************  */

	public void Field_employment()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		String empid="AVI,MED,EDU,HOT,PROM";
		if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase(NGFUserResourceMgr_PL.getGlobalVar("PL_NEP"))){

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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")==NGFUserResourceMgr_PL.getGlobalVar("PL_FZD") ||formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")==NGFUserResourceMgr_PL.getGlobalVar("PL_FZE")){
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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")==NGFUserResourceMgr_PL.getGlobalVar("PL_TEN"))
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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")=="S" && empid.indexOf(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))>-1)
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

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to make business verification visible   

	 ***********************************************************************************  */

	public void BussVerVisible()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if(formObject.getNGValue("EmpType").equalsIgnoreCase(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed"))){

			formObject.setVisible("Business_Verif",true);

		}
	}
	
	public static String plusyear(String currentDate,int i,int j,int k){
		DateTime dt = new DateTime();
		DateTimeFormatter fp = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime cd = dt.parse(currentDate,fp).plusYears(i).plusMonths(j).plusDays(k); 
		String close_date = cd.getYear()+"-"+(cd.getMonthOfYear()<10?"0"+cd.getMonthOfYear():cd.getMonthOfYear())+"-"+(cd.getDayOfMonth()<10?"0"+cd.getDayOfMonth():cd.getDayOfMonth());
		return close_date;
	}
	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to save CIF DATA

	 ***********************************************************************************  */

	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			PersonalLoanS.mLogger.info("RLSOCommon"+ "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 


			String WI_Name = formObject.getWFWorkitemName();
			PersonalLoanS.mLogger.info("RLSOCommon"+ "inside save_cif_data methos wi_name: "+WI_Name );
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

				PersonalLoanS.mLogger.info("RLSOCommon"+ "data to dave in cif details grid: "+ Cif_data);

			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while saving data for Customer Eligibility : "+ e.getMessage());
			printException(e);
		}


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to set non Primary Passport 

	 ***********************************************************************************  */

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
			PersonalLoanS.mLogger.info("Exception occured while seting non primary CIF: "+ e.getMessage());
			printException(e);
		}

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function for Application Type Employment    

	 ***********************************************************************************  */

	public void Fields_ApplicationType_Employment()
	{
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
			formObject.setVisible("EMploymentDetails_Label36",true); 
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",true); 
		}
		else{
			formObject.setVisible("EMploymentDetails_Label36",false); 
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false); 
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to parse cif eligibility  

	 ***********************************************************************************  */

	public void parse_cif_eligibility(String output){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		try{
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			PersonalLoanS.mLogger.info("inside parse_cif_eligibility"+"");
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
			String passport_list = "";

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
			PersonalLoanS.mLogger.info("name is doc : "+ doc+"");
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
							PersonalLoanS.mLogger.info("tag name and tag value can not be null:: tag name: "+tag_name+ " tag value: " +tag_value);
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
				PersonalLoanS.mLogger.info("parse_cif_eligibility Primary CIF: "+Prim_cif+"");
				Map<String, String> prim_entry;
				prim_entry = Cus_details.get(Prim_cif);
				String primary_pass = prim_entry.get("PassportNum");
				passport_list = passport_list.replace(primary_pass, "");
				PersonalLoanS.mLogger.info("Prim_cif: "+prim_entry.get("CustId"));
				set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
			}

			PersonalLoanS.mLogger.info("Prim_cif: "+ Prim_cif+"");

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}

	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Customer Picklist      

	 ***********************************************************************************  */


	public void loadPicklistCustomer()  
	{
		// disha FSD
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklistCustomer: ");

		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_Country order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus order by Code");
		//LoadPickList("ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		LoadPickList("cmplx_Customer_referrorcode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ReferrorCode with (nolock)"); //Arun
		LoadPickList("cmplx_Customer_referrorname", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ReferrorCode with (nolock)"); //Arun
		//LoadPickList("cmplx_Customer_NEP", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_NEPType order by Code");
		//change by saurabh on 7th Dec
		LoadPickList("cmplx_Customer_NEP","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_NEPType where isActive='Y' order by code");
		LoadPickList("cmplx_Customer_corpcode", "select '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CollectionCode with (nolock)"); //Arun
		LoadPickList("cmplx_Customer_CustomerCategory", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CustomerCategory order by Code");


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load RiskRating Picklist 

	 ***********************************************************************************  */

	public void loadPicklistRiskRating()
	{
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklistRiskRating: ");
		LoadPickList("cmplx_RiskRating_BusinessSeg", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Business_Segment with (nolock) order by Code");
		LoadPickList("cmplx_RiskRating_SubSeg", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Business_SubSegment with (nolock) order by Code");
		LoadPickList("cmplx_RiskRating_Demographics", "select demographic from (select '--Select--'as demographic,'0' as sno  union select demographic,Sno from NG_MASTER_Demographics) as q order by Sno");//Load Picklist changed by aman in risk fragment
		LoadPickList("cmplx_RiskRating_Industry","select Industry from (select '--Select--'as Industry,'0' as sno  union select Industry,Sno from NG_MASTER_Risk_Industry) as q order by Sno"); //Load Picklist added by aman in risk fragment

	}//Arun (10/10)

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Validate Loan   

	 ***********************************************************************************  */

	public void loanvalidate()
	{
		//below try catch added by nikhil 9/12/17
		try
		{
			
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info(""+"cmplx_LoanDetails_moratorium"+formObject.getNGValue("cmplx_LoanDetails_moratorium"));
		
		//below code modified by nikhil and if conditions  changed 06/12/17
		//if added by nikhil
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_lonamt")))
			formObject.setNGValue("cmplx_LoanDetails_lonamt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_basetype")))
			formObject.setNGValue("cmplx_LoanDetails_basetype",formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_baserate")))			
			formObject.setNGValue("cmplx_LoanDetails_baserate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_MArginRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_marginrate")))
			formObject.setNGValue("cmplx_LoanDetails_marginrate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_MArginRate"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_pdtpref")))
			formObject.setNGValue("cmplx_LoanDetails_pdtpref",formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate"));
		//if condition changed
		//change by saurabh on 2nd Jan
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_netrate")))
			formObject.setNGValue("cmplx_LoanDetails_netrate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_tenor")))
			formObject.setNGValue("cmplx_LoanDetails_tenor",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_RepayFreq")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_repfreq")))
			formObject.setNGValue("cmplx_LoanDetails_repfreq",formObject.getNGValue("cmplx_EligibilityAndProductInfo_RepayFreq"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_insplan")))
			formObject.setNGValue("cmplx_LoanDetails_insplan",formObject.getNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_loanemi")))
			formObject.setNGValue("cmplx_LoanDetails_loanemi",formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
		//if condition changed
		PersonalLoanS.mLogger.info(""+"cmplx_LoanDetails_moratorium"+formObject.getNGValue("cmplx_LoanDetails_moratorium"));
		
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_moratorium"))){
			PersonalLoanS.mLogger.info(""+"cmplx_LoanDetails_moratorium"+formObject.getNGValue("cmplx_LoanDetails_moratorium"));
		
			formObject.setNGValue("cmplx_LoanDetails_moratorium",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"));
		}
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate")))
			formObject.setNGValue("cmplx_LoanDetails_frepdate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate")))
			formObject.setNGValue("cmplx_LoanDetails_maturitydate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_AgeAtMaturity")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_ageatmaturity")))
			formObject.setNGValue("cmplx_LoanDetails_ageatmaturity",formObject.getNGValue("cmplx_EligibilityAndProductInfo_AgeAtMaturity"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPF")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_lpf")))
			formObject.setNGValue("cmplx_LoanDetails_lpf",formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPF"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount")) && !"".equals(formObject.getNGValue("cmplx_LoanDetails_lpfamt")))
			formObject.setNGValue("cmplx_LoanDetails_lpfamt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Insurance")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_insur")))
			formObject.setNGValue("cmplx_LoanDetails_insur",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Insurance"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_insuramt")))
			formObject.setNGValue("cmplx_LoanDetails_insuramt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate")) && "".equals(formObject.getNGValue("LoanDetails_duedate")))
			formObject.setNGValue("LoanDetails_duedate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate"));
		//if condition changed
		if(!"".equals(formObject.getNGValue("cmplx_LoanDetails_lonamt")))
		{
			formObject.setNGValue("LoanDetails_loanamt",formObject.getNGValue("cmplx_LoanDetails_lonamt"));
			formObject.setNGValue("LoanDetails_amt", formObject.getNGValue("cmplx_LoanDetails_lonamt"));
		}
		//added By Tarang for drop 4 point 8 started on 08/03/2018
		float lpfVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_LoanProcessingVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_lpfamt")))/100f;
		float insuranceVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_InsuranceVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_insuramt")))/100f;
		
		formObject.setNGValue("cmplx_LoanDetails_LoanProcessingVat", lpfVatAmt);
		formObject.setNGValue("cmplx_LoanDetails_InsuranceVat", insuranceVatAmt);
		formObject.setLocked("cmplx_LoanDetails_LoanProcessingVat", true);
		formObject.setLocked("cmplx_LoanDetails_InsuranceVat", true);
		//added By Tarang for drop 4 point 8 started on 08/03/2018
		String lpf = formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount");
		String insurance = formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount");
		float Amount = Float.parseFloat(lpf) + Float.parseFloat(insurance);		
		formObject.setNGValue("cmplx_LoanDetails_amt",Amount);
		//below code added by nikhil 
		/*String LoanAmt=formObject.getNGValue("cmplx_LoanDetails_lonamt");
		String lpfpercent=formObject.getNGValue("cmplx_LoanDetails_lpf");
		String inspercent=formObject.getNGValue("cmplx_LoanDetails_insur");
		double lpfamt= (Double.parseDouble(lpfpercent)*0.01)*;*/
		formObject.setLocked("cmplx_LoanDetails_lpfamt", true);
		//formObject.setLocked("cmplx_LoanDetails_inttype", true);
		formObject.setNGValue("cmplx_LoanDetails_currency","AED");
		formObject.setNGValue("cmplx_LoanDetails_favourof","RAK");
		
		//id changes by nikhil 9/12/17
		if(!"".equals(formObject.getNGValue("LoanDetails_disbdate")))
			formObject.setNGValue("cmplx_LoanDetails_paidon",formObject.getNGValue("LoanDetails_disbdate"));
		//++below code added by nikhil 9/12/17
		if(!"".equals(formObject.getNGValue("cmplx_LoanDetails_frepdate")))
		{
			formObject.setNGValue("LoanDetails_duedate", formObject.getNGValue("cmplx_LoanDetails_frepdate"));
		}
		if("".equals(formObject.getNGValue("cmplx_LoanDetails_paidon")))
		{
			
			formObject.setNGValue("cmplx_LoanDetails_paidon",formObject.getNGValue("cmplx_LoanDetails_cmplx_LoanGrid",0,2));
		//as per PROC-7103
		formObject.setNGValue("cmplx_LoanDetails_basetype", "PLRP");
		formObject.setLocked("cmplx_LoanDetails_basetype", true);
		}
		formObject.setNGValue("cmplx_LoanDetails_trandate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		}
		
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
		}
		
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for ELigibiltyAndProductInfo    

	 ***********************************************************************************  */

	public void loadPicklistELigibiltyAndProductInfo()  
	{
		LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
		LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
		LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstrumentType with (nolock) where isactive = 'Y'  order by code");
		//query added by saurabh on 22nd Oct for loading base rate type.
		LoadPickList("cmplx_EligibilityAndProductInfo_BaseRateType", "select distinct PRIME_TYPE from NG_master_Scheme order by PRIME_TYPE");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Product    

	 ***********************************************************************************  */


	public void loadPicklistProduct(String ReqProd)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklistProduct$"+ReqProd+"$");


		if(ReqProd!=null && "Personal Loan".equalsIgnoreCase(ReqProd)){
			PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside equalsIgnoreCase()"); 
			formObject.setVisible("Scheme", true);
			formObject.setLeft("Scheme", 555);
			formObject.clear("SubProd");
			String TypeofProduct=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),0);
			String subprod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2);
			String apptype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),4);
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme", "--Select--");
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
			LoadPickList("SubProd", "select '--select--',''as 'code' union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL   order by code");
			//LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			//added by abhishek
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("EmpType", " select '--Select--' union select convert(varchar, description) from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='"+getSubproduct()+"' and Applicationtype = '"+getApplicationType()+"'  and TypeofProduct='"+getTypeProd()+"' order by SCHEMEID");
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Incoming Document Repeater  

	 ***********************************************************************************  */

	public String getTypeProd() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		String typeProd = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),0);
		
		return typeProd;
	}

	public String getApplicationType() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		String apptype = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),4);
		if(apptype.contains("TOP")){
		return "Top up";
		}
		else if(apptype.contains("TKO")){
			return "Take Over";
		}
		else if(apptype.contains("NEW")){
			return "New";
		}
		else if(apptype.contains("RES")){
			return "Reschedulment";
		}
		return "";
	}

	public String getSubproduct() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		String subprod = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),2);
		
		return NGFUserResourceMgr_PL.getGlobalVar(subprod);
	}

	public void IncomingDoc()
	{
		PersonalLoanS.mLogger.info("Inside IncomingDoc() method ...");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");

			String [] finalmisseddoc=new String[70];
			int rowRowcount=repObj.getRepeaterRowCount();
			PersonalLoanS.mLogger.info("RLOS Initiation"+ "sQuery for document name is: rowRowcount" +  rowRowcount);
			if (repObj.getRepeaterRowCount() != 0) {

				for(int j = 0; j < rowRowcount; j++)
				{
					String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
					PersonalLoanS.mLogger.info("RLOS Initiation"+ "sQuery for document name is: DocName" +  DocName);

					String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
					PersonalLoanS.mLogger.info("RLOS Initiation"+ "sQuery for document name is: Mandatory" +  Mandatory);
					String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex")==null?"":repObj.getValue(j,"cmplx_DocName_DocIndex");

					//Belwo code added by Deepak to save document index of signature to use the same in upload signature call.--start 09Jan2018
					if("Signature".equalsIgnoreCase(DocName)){
						formObject.setNGValue("sig_docindex", DocIndex);
					}
					//Belwo code added by Deepak to save document index of signature to use the same in upload signature call.--end
					if("Y".equalsIgnoreCase(Mandatory))
					{
						
						PersonalLoanS.mLogger.info(""+"DocIndex"+DocIndex);
						String StatusValue=repObj.getValue(j,"cmplx_DocName_Doc_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Doc_Status");
						PersonalLoanS.mLogger.info(""+"StatusValue"+StatusValue);
						String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks")==null?"":repObj.getValue(j, "cmplx_DocName_Remarks");
						PersonalLoanS.mLogger.info(""+"Remarks"+Remarks);

						if(DocIndex==null || "".equalsIgnoreCase(DocIndex)){
							PersonalLoanS.mLogger.info(""+"StatusValue inside DocIndex"+DocIndex);
							if("Received".equalsIgnoreCase(StatusValue)){
								PersonalLoanS.mLogger.info(""+"StatusValue inside DocIndex recieved");
								//below line commented as this mandatory document is already received. 
								// finalmisseddoc[j]=DocName;
							}

							else if("Deferred".equalsIgnoreCase(StatusValue)){
								formObject.setNGValue("is_deferral_approval_require","Y");
								formObject.RaiseEvent("WFSave");
								PersonalLoanS.mLogger.info("Deferred flag value inside no document"+formObject.getNGValue("is_deferral_approval_require"));
								if("".equalsIgnoreCase(Remarks)){
									PersonalLoanS.mLogger.info("It is Mandatory to fill Remarks"+"As you have not attached the Mandatory Document and the status is Deferred");
									throw new ValidatorException(new FacesMessage("As you have Deferred "+DocName+" Document,So kindly fill the Remarks"));
								}
								else if(!"".equalsIgnoreCase(Remarks)|| Remarks==null){
									PersonalLoanS.mLogger.info("You may proceed further"+"Proceed further");
								}
							}
							else if("Waived".equalsIgnoreCase(StatusValue)){
								formObject.setNGValue("is_waiver_approval_require","Y");
								formObject.RaiseEvent("WFSave");
								PersonalLoanS.mLogger.info("waived flag value inside no document"+formObject.getNGValue("is_waiver_approval_require"));
								if("".equalsIgnoreCase(Remarks)){
									PersonalLoanS.mLogger.info("It is Mandatory to fill Remarks"+"As you have not attached the Mandatory Document and the status is Waived");
									throw new ValidatorException(new FacesMessage("As you have Waived "+DocName+" Document,So kindly fill the Remarks"));
								}
								else if(!"".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){
									PersonalLoanS.mLogger.info("You may proceed further"+"Proceed further");
								}
							}
							else if("--Select--".equalsIgnoreCase(StatusValue)||("".equalsIgnoreCase(StatusValue))){
								PersonalLoanS.mLogger.info(""+"StatusValue inside DocIndex is blank");
								finalmisseddoc[j]=DocName;
							}
							else if("Pending".equalsIgnoreCase(StatusValue)){
								PersonalLoanS.mLogger.info(""+"StatusValue of doc is Pending");

							}
						}
						else{
							if(!("".equalsIgnoreCase(DocIndex))){
								if(!"Received".equalsIgnoreCase(StatusValue)){
									repObj.setValue(j,"cmplx_DocName_Status","Received");
									repObj.setEditable(j, "cmplx_DocName_Status", false);
									PersonalLoanS.mLogger.info(""+"StatusValue::123final"+StatusValue);
								}
								else {

									PersonalLoanS.mLogger.info(""+"StatusValue::123final status is already received");
								}
							}
						}

					}
				}
			}
			StringBuilder mandatoryDocName = new StringBuilder("");

			PersonalLoanS.mLogger.info(""+"length of missed document"+finalmisseddoc.length);
			PersonalLoanS.mLogger.info(""+"length of missed document mandatoryDocName.length"+mandatoryDocName.length());

			for(int k=0;k<finalmisseddoc.length;k++)
			{
				if("AECBconsentform".equalsIgnoreCase(finalmisseddoc[k]) && "true".equals(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail"))){
					k++;
				}//added by akshay on 16/1/18 for proc 3667
				else if(null != finalmisseddoc[k]) {
					mandatoryDocName.append(finalmisseddoc[k]).append(",");
				}
				PersonalLoanS.mLogger.info("RLOS Initiation"+ "finalmisseddoc is:" +finalmisseddoc[k]);
				PersonalLoanS.mLogger.info(""+"length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
			}
			mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
			PersonalLoanS.mLogger.info("RLOS Initiation"+ "misseddoc is:" +mandatoryDocName.toString());

			if(mandatoryDocName.length()<=0){

				PersonalLoanS.mLogger.info("RLOS Initiation"+ "misseddoc is: inside if condition");

			}
			else{
				PersonalLoanS.mLogger.info("RLOS Initiation"+ "misseddoc is: inside if condition");
				PersonalLoanS.mLogger.info(""+"length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
				throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
			}
		}
		catch(Exception excp)
		{
			PersonalLoanS.mLogger.info("Exception occurred in IncomingDoc method :"+excp);
			PersonalLoanS.printException(excp);
		}
	}



	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for OECD    

	 ***********************************************************************************  */

	public void loadPickListOECD()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("OECD_CRSFlagReason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by Code");
		LoadPickList("OECD_CountryBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		//LoadPickList("OECD_townBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by Code");
		LoadPickList("OECD_CountryTaxResidence", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		//added by prabhakar
		LoadPickList("OECD_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Disable Customer Fragment fields  

	 ***********************************************************************************  */


	public void setDisabled()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->setDisabled()");
		String fields="cmplx_Customer_FirstNAme,cmplx_Customer_MiddleNAme,cmplx_Customer_LastNAme,cmplx_Customer_Age,cmplx_Customer_DOb,cmplx_Customer_gender,cmplx_Customer_EmiratesID,cmplx_Customer_IdIssueDate,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_MotherName,cmplx_Customer_PAssportNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VisaNo,cmplx_Customer_VisaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_MobileNo,cmplx_Customer_CIFNo";
		String[] array=fields.split(",");
		for(int i=0;array[i]!=null;i++)
		{
			PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->setDisabled()"+array[i]);

			formObject.setEnabled(array[i], false);

		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Decision

	 ***********************************************************************************  */


	public void loadPicklist3()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try
		{
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Inside loadpicklist3:");
			//LoadPickList("cmplx_Decision_refereason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock) order by code");
			formObject.clear("cmplx_Decision_Decision");
			String Query = "select * from (select '--Select--' as decision union select decision from NG_MASTER_Decision where ProcessName='PersonalLoanS' and workstepname='"+formObject.getWFActivityName()+"')t order by case when decision = '--Select--' then 0 else 1 end";
			PersonalLoanS.mLogger.info("RLOSCommon Load desison Drop down: "+Query );
			LoadPickList("cmplx_Decision_Decision", Query);
			LoadPickList("cmplx_Decision_CADDecisiontray", "select 'Select' as Refer_Credit  union select convert(varchar, Refer_Credit)  from NG_Master_ReferCredit with (nolock) order by Refer_Credit desc"); //Arun (12/10)			
			//LoadPickList("DecisionHistory_DecisionReasonCode", "select * from (select '--Select--' as description union select description from ng_MASTER_RejectReasons  with (nolock) where IsActive='Y')t order by case when description = '--Select--' then 0 else 1 end");
			//LoadPickList("DecisionHistory_DecisionReasonCode", "SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason where workstep='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' order by code");
		}
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklist3 :"+e.getMessage());
			printException(e);
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Address

	 ***********************************************************************************  */


	public void loadPicklist_Address()
	{
		try
		{
			//AddressDetails_city
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklist_Address: "); 
			LoadPickList("AddressDetails_addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock) order by code");
			//LoadPickList("AddressDetails_city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
			//LoadPickList("AddressDetails_state", "select '--Select--'as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
			LoadPickList("AddressDetails_country", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
			LoadPickList("AddressDetails_ResidenceAddrType", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_ResidAddressType with (nolock)  where isActive='Y' order by code");
			LoadPickList("AddressDetails_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
		}	
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklist_Address :"+e.getMessage());
			printException(e);
		}
	}
	
	/*public void loadPicklist_FATCA()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			LoadPickList("cmplx_FATCA_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
			LoadPickList("FATCA_USRelaton", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation with (nolock) order by code");
			PersonalLoanS.mLogger.info("FATCA LoadPickList");
			//FATCA_USRelaton
		}
		catch (Exception e)
		{
			PersonalLoanS.mLogger.info("Fatca LoadPickList"+e);
		}
	}*/
	//added by prabhakar
		public void loadPicklist_KYC()
		{
			try
			{
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				LoadPickList("KYC_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
				PersonalLoanS.mLogger.info("KYC LoadPickList");
			}
			catch (Exception e)
			{
				PersonalLoanS.mLogger.info("KYC LoadPickList"+e);
			}
		}
		//added by prabhakar
		/*public void loadPicklist_OECD()
		{
			try
			{
				PersonalLoanS.mLogger.info("OECD LoadPickList");
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				LoadPickList("OECD_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
			}
			catch (Exception e)
			{
				PersonalLoanS.mLogger.info("OECD LoadPickList"+e);
			}
		}*/
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Loan Details  

	 ***********************************************************************************  */

	public void loadPicklist_LoanDetails()
	{
		LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstallmentType with (nolock) order by Code");
		LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_COLLECTIONBRANCH with (nolock)");
		LoadPickList("cmplx_LoanDetails_ddastatus", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_DDASTATUS with (nolock)");
		LoadPickList("LoanDetails_modeofdisb", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ModeOfDisbursal with (nolock)");
		//LoadPickList("LoanDetails_disbto", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
		LoadPickList("LoanDetails_holdcode", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_HoldCode with (nolock) order by code");
		LoadPickList("cmplx_LoanDetails_paymode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_PAYMENTMODE with (nolock)");
		LoadPickList("cmplx_LoanDetails_status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_STATUS with (nolock)");
		LoadPickList("cmplx_LoanDetails_bankdeal", "select '--Select--','' as code union select Description,code from NG_MASTER_Dealing_Bank with(nolock) order by code");
		//LoadPickList("cmplx_LoanDetails_city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Part Match 

	 ***********************************************************************************  */

	public void loadPicklist_PartMatch()
	{
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklist_PartMatch: ");
		LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}



	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Populate the Incoming Document Repeater   

	 ***********************************************************************************  */

	public void fetchIncomingDocRepeater(){

		PersonalLoanS.mLogger.info(" Inside loadAllCombo_LeadManagement_Documents_Deferral"+ "inside fetchIncomingDocRepeater");
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName = formobject.getConfigElement("ActivityName");
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +"");
		String Username=formObject.getUserName();//Arun (22/09/17)
		String requested_product ="";
		String requested_subproduct;
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +"valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +requested_subproduct);

			}    
		}
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" +requested_product);

		//Disha started (10/6/17)

		List<String> repeaterHeaders = new ArrayList<String>();
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value sActivityName for:" +sActivityName);
		if ("Original_Validation".equalsIgnoreCase(sActivityName) || "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
		{
			PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value sActivityName for:" +"OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expire Date");
			repeaterHeaders.add("Mandatory");
			repeaterHeaders.add("Status");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");

			/*repeaterHeaders.add("OV Remarks");
			repeaterHeaders.add("OV Decision");
			repeaterHeaders.add("Approved By");*/
			//added by yash on 19/12/2017
			repeaterHeaders.add("Deferred Until");
		}
		else 
		{
			PersonalLoanS.mLogger.info("INSIDE other INCOMING DOCUMENT value sActivityName for:" +" non OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expire Date");
			repeaterHeaders.add("Mandatory");
			repeaterHeaders.add("Status");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");
			/*repeaterHeaders.add("OV Remarks");
			repeaterHeaders.add("OV Decision");
			repeaterHeaders.add("Approved By");*/
			//added by yash on 19/12/2017
			repeaterHeaders.add("Deferred Until");
			

		}
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +"after making headers");

		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> docName ;
		String documentName = null;
		String documentNameMandatory=null;
		String statusValue = null;
		String expiryDate = null;
		String Remarks= null;
		String DocInd= null;

		String query ;

		IRepeater repObj;
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +"after creating the object for repeater");

		int repRowCount = 0;

		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");

		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +""+repObj.toString());



		query = "SELECT distinct DocName,ExpiryDate,Mandatory,DocSta,Remarks,DocInd,DeferredUntilDate FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"' order by Mandatory desc";
		PersonalLoanS.mLogger.info("query INSIDE INCOMING DOCUMENT:"+query);
		docName = formObject.getDataFromDataSource(query);
		PersonalLoanS.mLogger.info("Docname:"+docName);
		try{
			repObj.setRepeaterHeaders(repeaterHeaders);

			/*if ("Original_Validation".equalsIgnoreCase(sActivityName) )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);
					repObj.setColumnDisabled(1, true);
					repObj.setColumnDisabled(3, true);
					repObj.setColumnDisabled(14, true);
					//below code modified by nikhil
					if("Received".equalsIgnoreCase(statusValue))
					{
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , true);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , true);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , true);
					}
					else
					{
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , false);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , false);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , false);
					}
					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);

			}*/
				repObj.setColumnVisible(12, false);
				repObj.setColumnVisible(13, false);
				repObj.setColumnVisible(14, false);
			

			//added by yash on 26/12/2017 or making documents non-editable
			 if ("ToTeam".equalsIgnoreCase(sActivityName) )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);
					
					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, false);
					repObj.setColumnDisabled(2, false);
					repObj.setColumnDisabled(1, false);
					repObj.setColumnDisabled(3, false);
					//repObj.setColumnDisabled(14, false);
					if("LIABILITY_CERTIFICATE".contains(documentName)||("LIABILITY_CERTIFICATE4".contains(documentName))||"LIABILITY_CERTIFICATE1".contains(documentName)||"LIABILITY_CERTIFICATE2".contains(documentName)||"LIABILITY_CERTIFICATE3".contains(documentName)
							||"LIABILITY_CERTIFICATE5".contains(documentName)||"NO_LIABILITY_CERTIFICATE".contains(documentName)||"SALARY_TRANSFER_LETTER".contains(documentName))
					{
						
						
						repObj.setRowEditable(i, true);
						
					}
					else
					{
						repObj.setRowEditable(i, false);
					}
					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
				
			}
			//added by yash on 21/12/2017
			else if ( "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);	          
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;
					Remarks = docName.get(i).get(4);
					DocInd = docName.get(i).get(5);

					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);

					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
			}
			//below code added by nikhil for ddvt
			else if ("DDVT_maker".equalsIgnoreCase(sActivityName) || "DDVT_Checker".equalsIgnoreCase(sActivityName) )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				PersonalLoanS.mLogger.info("Repeater Headers list: "+repeaterHeaders);
				
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);
					
					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					//repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, false);
					repObj.setColumnDisabled(1, false);
					repObj.setColumnDisabled(3, false);
					//repObj.setColumnDisabled(14, false);
					repObj.setColumnDisabled(0, true);
					
					if("DDVT_maker".equalsIgnoreCase(sActivityName) || "Other1".equalsIgnoreCase(documentName) || ("Other2".equalsIgnoreCase(documentName)) || ("Other3".equalsIgnoreCase(documentName))
							|| ("Other4".equalsIgnoreCase(documentName)) || ("Other5".equalsIgnoreCase(documentName)))
						
					{
						repObj.setRowEditable(i, true);
						repObj.setEditable(i, "IncomingDoc_AddFromDMSButton", true);
						repObj.setEditable(i, "IncomingDoc_AddFromPCButton", true);
						repObj.setEditable(i, "IncomingDoc_ScanButton", true);
						repObj.setEditable(i, "IncomingDoc_ViewButton", true);
						repObj.setEditable(i, "IncomingDoc_PrintButton", true);
					}
					else if("DDVT_Checker".equalsIgnoreCase(sActivityName))
					{
						repObj.setRowEditable(i, false);
						repObj.setEditable(i, "IncomingDoc_AddFromDMSButton", false);
						repObj.setEditable(i, "IncomingDoc_AddFromPCButton", false);
						repObj.setEditable(i, "IncomingDoc_ScanButton", false);
						repObj.setEditable(i, "IncomingDoc_ViewButton", false);
						repObj.setEditable(i, "IncomingDoc_PrintButton", false);			
					}
						
						
					
					
					repRowCount = repObj.getRepeaterRowCount();
					
					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				/*repObj.setColumnDisabled(0, true);
				repObj.setColumnDisabled(5, true);
				repObj.setColumnDisabled(6, true);
				repObj.setColumnDisabled(7, true);
				repObj.setColumnDisabled(8, true);
				repObj.setColumnDisabled(9, true);*/
				repObj.setColumnVisible(11, false);
				repObj.setColumnVisible(12, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
				//below cpde added y nikhil 29/1/18
				repObj.setColumnDisabled(2, true);
				
			}
			 //added by nikhil for fcu 9/2/18
			
			 if ("FCU".equalsIgnoreCase(sActivityName)|| "CAD_Analyst2".equalsIgnoreCase(sActivityName) )
				{
				 PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
					for(int i=0;i<docName.size();i++ )
					{
						//repObj.addRow();                    

						documentName = docName.get(i).get(0);
						PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);
						
						documentNameMandatory = docName.get(i).get(2);
						statusValue = docName.get(i).get(3);
						expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
						Remarks = docName.get(i).get(4);//Arun (22/09/17)
						DocInd = docName.get(i).get(5);//Arun (22/09/17)

						PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
						PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
						PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
						PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
						PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
						PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
						PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

						repObj.setValue(i, 0, documentName);
						repObj.setValue(i, 2, documentNameMandatory);
						repObj.setValue(i,1,expiryDate);
						repObj.setValue(i,3,statusValue);
						repObj.setValue(i,4,Remarks);
						repObj.setRowEditable(i, true);
						repRowCount = repObj.getRepeaterRowCount();
						
						PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
					}
					
					repObj.setColumnVisible(11, false);
				
					repObj.setColumnDisabled(2, true);
					repObj.setColumnDisabled(1, true);
					repObj.setColumnDisabled(3, true);
					repObj.setColumnDisabled(4, true);
					repObj.setColumnDisabled(0, true);
					
				}
			else if ("CAD_Analyst1".equalsIgnoreCase(sActivityName)   )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);
					
					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					//repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);
					repObj.setColumnDisabled(1, false);
					repObj.setColumnDisabled(3, false);
					//repObj.setColumnDisabled(14, false);
					repObj.setColumnDisabled(0, true);
					/*repObj.setColumnDisabled(5, true);
					repObj.setColumnDisabled(6, true);
					repObj.setColumnDisabled(7, true);
					repObj.setColumnDisabled(8, true);
					repObj.setColumnDisabled(9, true);*/
									
						repObj.setRowEditable(i, true);
						
					
					repRowCount = repObj.getRepeaterRowCount();
					
					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				/*repObj.setColumnDisabled(0, true);
				repObj.setColumnDisabled(5, true);
				repObj.setColumnDisabled(6, true);
				repObj.setColumnDisabled(7, true);
				repObj.setColumnDisabled(8, true);
				repObj.setColumnDisabled(9, true);*/
				repObj.setColumnDisabled(0, true);
				repObj.setColumnVisible(11, false);
				repObj.setColumnVisible(12, true);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
				
			}
			//added by yash on 21/12/2017
			else if ( "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);	          
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;
					Remarks = docName.get(i).get(4);
					DocInd = docName.get(i).get(5);

					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);

					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
			}
			else
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row maker csm"+ "add row CSm maker ");


				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();

					//repObj.setColumnVisible(12, false);
					//repObj.setColumnVisible(13, false);
					//repObj.setColumnVisible(14, false);

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;
					
					statusValue = docName.get(i).get(3);
					Remarks = docName.get(i).get(4);
					DocInd = docName.get(i).get(5);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory); 	     
					repObj.setValue(i,1,expiryDate);
					/*repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);*/
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);
					repObj.setDisabled(i, "cmplx_DocName_DocName", true);
					
					
					if(statusValue == null){
						statusValue = "--Select--"; 
					}
					repObj.setValue(i, 3, statusValue);
					repObj.setValue(i, 4, Remarks);
					/*List<String> man=new ArrayList<String>(Arrays.asList("--Select--", "Pending", "Received", "Deferred", "Waived"));
					List<String> nonman=new ArrayList<String>(Arrays.asList("--Select--", "Pending", "Received")); 
					if(documentNameMandatory.equalsIgnoreCase("Y"))
					{
						formObject.addItem("cmplx_DocName_Doc_Status", man);
					}
					else
					{
						formObject.addItem("cmplx_DocName_Doc_Status", nonman);
					}*/
					/*repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);*/

				} 
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
			}
		}
		catch (Exception e) {

			PersonalLoanS.mLogger.info("EXCEPTION    :    "+ " " + e.toString());
			printException(e);

		} 
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Populate Existing Data in Decision Grid

	 ***********************************************************************************  */

	public void loadInDecGrid()
	{
		PersonalLoanS.mLogger.info("Inside loadInDecGrid() method to load WI history");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			//String listviewName ="Decision_ListView1";
			String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm:ss'),userName,workstepName,isnull(Decision,'NA'),isnull(remarks,'NA'),entry_date,dec_wi_name,decisionreasonCode,referTo from ng_rlos_gr_Decision with (nolock) where dec_wi_name='"+ParentWI_Name+"' or dec_wi_name='"+formObject.getWFWorkitemName()+"' order by datelastchanged desc";
			PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			PersonalLoanS.mLogger.info("List is: "+list);
			//added by akshay on 6/12/17
			if(list!=null && !list.isEmpty()){
				for (List<String> a : list) 
				{
					PersonalLoanS.mLogger.info("list to be added in Grid :-:"+a);
					formObject.addItemFromList("Decision_ListView1", a);
					a.clear();
				}
			}
		}catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadInDecGrid :"+e.getMessage());
			printException(e);
		}     
	}



	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to load data in Refer grid

	 ***********************************************************************************  */


	/*public void loadInReferGrid()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm'),userName,workstepName,Decisiom,remarks,TargetDecision,workitemID,history_wi_nAme from ng_rlos_gr_ReferHistory with (nolock) where  history_wi_nAme='"+formObject.getWFWorkitemName()+"'";
		PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->loadInReferGrid()"+query);
		List<List<String>> list=formObject.getNGDataFromDataCache(query);
		PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->Query Result is: "+list.toString());
		for (List<String> a : list) 
		{

			formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory", a);
		}

	}*/

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Employment Details    

	 ***********************************************************************************  */

	public void loadPicklist4()    
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Inside loadpicklist4:"); 
		String reqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");
		
		if("Personal Loan".equalsIgnoreCase(reqProd)){
			if("BAU".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
			}
			else if("S".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'PL' or product='B') order by code");	
			}
		}
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		//LoadPickList("cmplx_EmploymentDetails_tenancycntrctemirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) order by code");
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
		LoadPickList("cmplx_EmploymentDetails_OtherBankCAC","select '--Select--' union select convert(varchar, Description) from NG_MASTER_othBankCAC where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_Indus_Macro", " Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when micro ='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType","select '--Select--' union select convert(varchar, Description) from NG_MASTER_EmpContractType where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_PromotionCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_collectioncode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CollectionCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_employer_type","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerType where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_empmovemnt", "select '--Select--' union all select convert(varchar, Description) from NG_MASTER_EMPLOYERMOVEMENT with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_Field_visit_done", "select '--Select--' union all select convert(varchar, Description) from ng_master_fieldvisitDone with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_marketcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' and Product = 'PL' order by code");	
	}
//
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Hide/Unhide fields for ELigibiltyAndProductInfo    

	 ***********************************************************************************  */

	public void Eligibilityfields()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklist_Address: "); 


		PersonalLoanS.mLogger.info("CC"+ "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+6));
		if(NGFUserResourceMgr_PL.getGlobalVar("CC_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_PL.getGlobalVar("PL_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || (NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))){
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

			PersonalLoanS.mLogger.info("CC @yash"+ "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+6));


		}
		else if((NGFUserResourceMgr_PL.getGlobalVar("PL_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) || NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) ) && !NGFUserResourceMgr_PL.getGlobalVar("PL_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
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

		}

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Save Data in Decision Grid   

	 ***********************************************************************************  */


	public void saveIndecisionGrid(){

		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid: "); 
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//String entrydate = formObject.getNGValue("Entry_date_time"); //Tarang to be removed on friday(1/19/2018)
		String entrydate = formObject.getNGValue("Entry_date");
		//String[] parts = EntrydateTime.split("/"); 
		entrydate =common.Convert_dateFormat(entrydate, "dd/MM/yyyy","MM/dd/yyyy");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+entrydate); 
		String currDate=common.Convert_dateFormat("", "","MM/dd/yyyy HH:mm");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid...currDate "+currDate);
		String CIF_id=formObject.getNGValue("cmplx_Customer_CIFNO");
		String Emirate_id=formObject.getNGValue("cmplx_Customer_EmiratesID");
		//below query changed by nikhil 3/1/18
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decision, remarks, dec_wi_name,Entry_Date,DecisionReasonCode,referTo,CIF_ID,EmirateID) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+entrydate+"' as datetime),102),'"+formObject.getNGValue("DecisionHistory_DecisionReasonCode")+"','"+formObject.getNGValue("DecisionHistory_ReferTo")+"','"+CIF_id+"','"+Emirate_id+"')";
		PersonalLoanS.mLogger.info("PL_Common"+"Query is"+query);
		formObject.saveDataIntoDataSource(query);

	}	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Save Data in Decision grid for CSM

	 ***********************************************************************************  */

	public void saveIndecisionGridCSM(){
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid: "); 
		String entrydate="" ;       
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Cifid=formObject.getNGValue("DecisionHistory_CIFid");
		String emiratesid=formObject.getNGValue("DecisionHistory_Emiratesid");
		String custName=formObject.getNGValue("DecisionHistory_Customer_Name");
		PersonalLoanS.mLogger.info("PersonnalLoanS"+"Final val of custName:"+ custName);
		//String EntrydateTime = formObject.getNGValue("Entry_date_time");//Tarang to be removed on friday(1/19/2018)
		String EntrydateTime = formObject.getNGValue("Entry_date");
		if(EntrydateTime!=null && EntrydateTime.equalsIgnoreCase("") && EntrydateTime.contains("/")){
		String[] parts = EntrydateTime.split("/");
		entrydate = parts[1]+"/"+parts[0]+"/"+parts[2];
		}
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+EntrydateTime); 
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside  saveIndecisionGrid entrydate "+entrydate); 
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid...currDate "+currDate);
		//below query changed by nikhil 20/12/17
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decision, remarks, dec_wi_name,Entry_Date,CIF_ID,EmirateID,CustomerNAme) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+entrydate+"' as datetime),102),'"+Cifid+"','"+emiratesid+"','"+custName+"')";
		PersonalLoanS.mLogger.info("PL_Common"+"Query is"+query);
		formObject.saveDataIntoDataSource(query);
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              fetch
	Author                              : Disha              
	Description                         : Function to Save data in Refer grid   

	 ***********************************************************************************  */
	//added by akshay on 10/12/17
	public void LoadReferGrid()
	{
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside AddInReferGrid: "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		/*	if(!formObject.isVisible("ReferHistory_Frame1")){
			formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
			formObject.setNGFrameState("ReferHistory", 0);
		}*/
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		//String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		String decision=formObject.getNGValue("cmplx_Decision_Decision");
		String currDate=common.Convert_dateFormat("", "","MM/dd/yyyy HH:mm");
		if("Refer".equalsIgnoreCase(decision)){
			
			List<String> Referlist=new ArrayList<String>();
	
			String ReferTo=formObject.getNGValue("DecisionHistory_ReferTo");
			PersonalLoanS.mLogger.info("PL_Common"+ "ReferTo: "+ReferTo); 
			String[] ReferTo_array=ReferTo.split(";");
			for(int i=0;i<ReferTo_array.length;i++)
			{
				Referlist.clear();
				Referlist.add(currDate);
				Referlist.add(formObject.getUserName());
				Referlist.add(formObject.getWFActivityName());
				Referlist.add(decision);
				Referlist.add(formObject.getNGValue("cmplx_Decision_REMARKS"));
				if("CSO (for documents)".equalsIgnoreCase(ReferTo_array[i]) || "Source".equalsIgnoreCase(ReferTo_array[i])){
					Referlist.add("DSA_CSO_Review");
				}
				else{
					Referlist.add(ReferTo_array[i]);
				}
				Referlist.add("");
				Referlist.add("");
				Referlist.add(formObject.getWFWorkitemName());
				formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory",Referlist);
	
			}
			//formObject.saveFragment("ReferHistory");
			PersonalLoanS.mLogger.info("PL_Common"+"ReferList is:"+Referlist.toString());
		}	//formObject.RaiseEvent("WFSave");
	}
	//added by akshay on 10/12/17


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Decision Fields  

	 ***********************************************************************************  */

	public void  loadPicklist1()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PersonalLoanS.mLogger.info("PL"+"Inside PLCommon ->loadPicklist1()"+ActivityName);

			//LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");
			LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");
			//LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");

			LoadPickList("cmplx_Decision_CADDecisiontray", "select 'Select' as Refer_Credit  union select convert(varchar, Refer_Credit)  from NG_Master_ReferCredit with (nolock) order by Refer_Credit desc"); //Arun (12/10)
		}
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklist1 :"+e.getMessage());
			printException(e);
		}	
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to populate PickList Window 

	 ***********************************************************************************  */


	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize)
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);

		if ("EMploymentDetails_Button2".equalsIgnoreCase(sControlName)) 
			objPickList.setWindowTitle("Search Employer");
		else 
			objPickList.setWindowTitle("Search City/State");
		

/*		List<List<String>> result=formObject.getNGDataFromDataCache(sQuery);
		if(!result.isEmpty()){//result.size()>0
			objPickList.setHeight(600);
			objPickList.setWidth(1000);
			objPickList.setVisible(true);
			objPickList.setSearchEnabled(true);
			objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));
			PersonalLoanS.mLogger.info("EventListenerHandler: Result Of Query:"+result.toString());   
			objPickList.populateData(result);

		}
		else{
			throw new ValidatorException(new FacesMessage("No Data Found"));
		}*/
		
		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
		if (sControlName.equalsIgnoreCase("EMploymentDetails_Button2")){
			if(result.isEmpty())

			{
				throw new ValidatorException(new FacesMessage("No Data Found"));
			}
			else
			{
				objPickList.setHeight(600);
				objPickList.setWidth(800);
				objPickList.setVisible(true);
				objPickList.setSearchEnabled(true);
				objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));

				PersonalLoanS.mLogger.info(result.toString());   

				objPickList.populateData(result);			
			}
		}	
		else{
			if(result.isEmpty())

			{
				throw new ValidatorException(new FacesMessage("No Data Found"));
			}
			else
			{
				objPickList.setHeight(400);
				objPickList.setWidth(400);
				objPickList.setVisible(true);
				objPickList.setSearchEnabled(true);
				objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));

				PersonalLoanS.mLogger.info("Aman "+result.toString());   

				objPickList.populateData(result);			
			}
		}	
	}       


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Set Values on Part Match Fragment

	 ***********************************************************************************  */




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
		//below code added by nikhil 04/01/17
		formObject.setNGValue("PartMatch_oldpass",formObject.getNGValue("cmplx_Customer_Passport2"));
		
		formObject.setNGValue("PartMatch_visafno",formObject.getNGValue("cmplx_Customer_VisaNo"));
		formObject.setNGValue("PartMatch_mno1",formObject.getNGValue("AlternateContactDetails_MobileNo1"));//modified by nikhil
		formObject.setNGValue("PartMatch_mno2",formObject.getNGValue("AlternateContactDetails_MobileNo2"));//Arun (23/10) to autopopulate values here
		formObject.setNGValue("PartMatch_Dob",formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("PartMatch_EID",formObject.getNGValue("cmplx_Customer_EmiratesID"));
		formObject.setNGValue("PartMatch_nationality",formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("PartMatch_drno",formObject.getNGValue("cmplx_Customer_DLNo"));
		//formObject.setNGValue("PartMatch_oldpass",formObject.getNGValue("cmplx_Customer_PAssportNo")); //Arun (07/09/17)

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set Customer Detail Verification Values

	 ***********************************************************************************  */


	//++Below code added by nikhil 13/11/2017 for Code merge

	public void custdet1values()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailverification1_MobNo1_veri","cmplx_CustDetailverification1_MobNo2_veri","cmplx_CustDetailverification1_DOB_ver","cmplx_CustDetailverification1_PO_Box_Veri","cmplx_CustDetailverification1_Emirates_veri","cmplx_CustDetailverification1_Off_Telephone_veri");
		LoadPicklistVerification(LoadPicklist_Verification);
		openFrags(formObject);
		autopopulateValuesFCU(formObject);
	}
	//--Above code added by nikhil 13/11/2017 for Code merge

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set Employment Verification Values

	 ***********************************************************************************  */

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

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Picklist for Verification

	 ***********************************************************************************  */

	public void LoadPicklistVerification(List<String> mylist){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside LoadPicklistVerification(): "+mylist);

		for(String control_name:mylist)
		{
			formObject.setVisible(control_name, true);
			LoadPickList(control_name, "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri order by code");
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set Customer Detail Verification Values

	 ***********************************************************************************  */


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

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load picklist for worldcheck

	 ***********************************************************************************  */

	public void loadPicklist_WorldCheck()
	{
		LoadPickList("WorldCheck1_BirthCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
		LoadPickList("WorldCheck1_ResidentCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
		//below code added by niukhil 20/12/17
		LoadPickList("WorldCheck1_Gender","select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_master_gender with (nolock) order by Code");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load MOL Picklist

	 ***********************************************************************************  */

	public void loadPicklistMol()
	{
		LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Picklist for DDS Return

	 ***********************************************************************************  */


	public void loadPicklistDDSReturn()
	{
		LoadPickList("FinacleCore_Combo3", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Cheque_Type with (nolock) order by Code");
		LoadPickList("FinacleCore_Combo4", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_TypeOfReturn with (nolock) order by Code");
		LoadPickList("FinacleCore_Combo2", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) order by Code");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set Office Detail Verification Values

	 ***********************************************************************************  */

	public void OfficeVervalues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("cmplx_OffVerification_fxdsal_val",formObject.getNGValue("cmplx_IncomeDetails_AvgNetSal"));
		formObject.setNGValue("cmplx_OffVerification_accprovd_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		formObject.setNGValue("cmplx_OffVerification_desig_val",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		formObject.setNGValue("cmplx_OffVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		formObject.setNGValue("cmplx_OffVerification_cnfrminjob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));    	   		 
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set Loan Card Verification Values

	 ***********************************************************************************  */

	public void loancardvalues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
		//int framestate=formObject.getNGFrameState("LoanDetails");
		if(!formObject.isVisible("LoanDetails_Frame1"))
		formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
		formObject.setNGValue("cmplx_LoanandCard_loanamt_val",formObject.getNGValue("cmplx_LoanDetails_lonamt"));
		formObject.setNGValue("cmplx_LoanandCard_tenor_val",formObject.getNGValue("cmplx_LoanDetails_tenor"));
		formObject.setNGValue("cmplx_LoanandCard_emi_val",formObject.getNGValue("cmplx_LoanDetails_loanemi"));
		formObject.setNGValue("cmplx_LoanandCard_islorconv_val",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
		formObject.setNGValue("cmplx_LoanandCard_firstrepaydate_val",formObject.getNGValue("cmplx_LoanDetails_frepdate"));
		String CardType="";
		String CardLimit="";
		String CardTypeandLimit="select Card_Product,Final_Limit from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi='"+formObject.getWFWorkitemName()+"' and Cardproductselect='True'";
		List<List<String>> CardTypeandLimitXML = formObject.getNGDataFromDataCache(CardTypeandLimit);
		if (CardTypeandLimitXML != null){
			for (int i = 0; i<CardTypeandLimitXML.size();i++){
				if (i == 0){
					CardType=CardTypeandLimitXML.get(i).get(0);
					CardLimit=CardTypeandLimitXML.get(i).get(1);
				
					}
				else {
					CardType=CardType+","+CardTypeandLimitXML.get(i).get(0);
					CardLimit=CardLimit+","+CardTypeandLimitXML.get(i).get(1);
				}
			}
		}
		formObject.setNGValue("cmplx_LoanandCard_cardtype_val",CardType);
		formObject.setNGValue("cmplx_LoanandCard_cardlimit_val",CardLimit);
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Picklist checker

	 ***********************************************************************************  */


	public void  loadPicklistChecker()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PersonalLoanS.mLogger.info("PL"+"Inside PLCommon ->loadPicklistChecker()"+ActivityName);

			LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"' and Initiation_Type NOT LIKE  '%Reschedulment%'");
		}
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklistChecker :"+e.getMessage());
			printException(e);
		}	
	}	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for primary cif Identify

	 ***********************************************************************************  */

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
			PersonalLoanS.mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}

		return primary_cif;
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for Customer Update

	 ***********************************************************************************  */

	public String CustomerUpdate(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg = "";
		String outputResponse;
		String cif_verf_status;
		String   ReturnCode;
		PL_Integration_Output PL_Integ_out  = new PL_Integration_Output();
		cif_verf_status = formObject.getNGValue("is_cust_verified");
		String Existingcust = formObject.getNGValue("cmplx_Customer_NTB");
		PersonalLoanS.mLogger.info("PL DDVT Vhecker"+ "EXISTING CUST : "+ Existingcust);
		if("false".equalsIgnoreCase(Existingcust)){
			cif_verf_status = "Y";
		}
		String Cif_lock_status = formObject.getNGValue("Is_CustLock");
		PersonalLoanS.mLogger.info("PL DDVT Vhecker"+ "cif_verf_status : "+ cif_verf_status);
		PersonalLoanS.mLogger.info("PL DDVT Vhecker"+ "cif_Lock_status : "+ Cif_lock_status);
		if ("".equalsIgnoreCase(cif_verf_status)||"N".equalsIgnoreCase(cif_verf_status)){
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_verify");

			ReturnCode = outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
				//below line added by akshay on 6/3/18 for drop 4
				if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
				{
					formObject.setNGValue("is_cust_verified", "Y");
				}
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),5,"Y");
				cif_verf_status="Y";
				alert_msg="Customer verified Successfully";
			}
			else{
				if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
				{
					formObject.setNGValue("is_cust_verified", "N");
				}				
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),5,"N");
				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Enquiry operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";


			}
		}

		if ("Y".equalsIgnoreCase(cif_verf_status)&&("".equalsIgnoreCase(Cif_lock_status)||"N".equalsIgnoreCase(Cif_lock_status)))
		{
			PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Inside Lock and Update Customer");
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
			ReturnCode = outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode))
			{
				Cif_lock_status="Y";
				PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Locked sucessfully and now Unlock and update customer");
				if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
				{
				formObject.setNGValue("Is_CustLock", "Y");
				}
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"Y");
				outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
				ReturnCode =  outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode))
				{
					PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock sucessfull");
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"N");
					//below if else added by akshay on 6/3/18 for drop 4
					if("GUARANTOR".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
					{
					outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","SUPPLEMENT_CIF_UPDATE");
					}
					else{
						formObject.setNGValue("Is_CustLock", "N");
						Cif_lock_status="N";
						outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
					}
					
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
						{
						formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
						}
						//below line added by akshay on 6/3/18 for drop 4
						formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"Y");
						PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						PL_Integ_out.valueSetIntegration(outputResponse);    
						//formObject.setEnabled("DecisionHistory_updcust_loan", false);
						PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
						PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						alert_msg = "Customer Updated Successful!";
						formObject.setEnabled("DecisionHistory_updacct_loan", true);
						formObject.setNGValue("cmplx_LoanDisb_updateCustomerFlag", "Y");
						formObject.saveFragment("Loan_Disbursal_Frame2");
					}
					else{
						alert_msg= "Customer Update operation failed, Please try after some time or contact administrator";
						formObject.setEnabled("DecisionHistory_Button3", true);
						PersonalLoanS.mLogger.info("Customer Details"+"Customer Update operation Failed");
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
						{
							formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
						}
						formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"N");

					}
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					formObject.RaiseEvent("WFSave");
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");

				}
				else{
					PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock failed return code: "+ReturnCode);
					alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";
				}

			}
			else{
				if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
				{
				formObject.setNGValue("Is_CustLock", "N");
				}
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"Y");
				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Lock operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";
			}

		}
		else if("Y".equalsIgnoreCase(cif_verf_status)&& "Y".equalsIgnoreCase(Cif_lock_status))
		{
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode))
			{
				
				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock sucessfull");
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"N");

				//below if else added by akshay on 6/3/18 for drop 4
				if("GUARANTOR".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
				{
				outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","SUPPLEMENT_CIF_UPDATE");
				}
				else{
					formObject.setNGValue("Is_CustLock", "N");
					Cif_lock_status="N";
					outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
				}
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
					if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
					{
					formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
					}
					//below line added by akshay on 6/3/18 for drop 4
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"Y");
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					PL_Integ_out.valueSetIntegration(outputResponse);    
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				}
				else{
					PersonalLoanS.mLogger.info("Customer Details"+"ACCOUNT_MAINTENANCE_REQ is not generated");
					if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
					{
						formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
					}
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"N");

				}
				PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				formObject.RaiseEvent("WFSave");
				PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ")))
				{ 
					PersonalLoanS.mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition");
					formObject.setEnabled("DecisionHistory_Button3", false); 
				}
				else{
					formObject.setEnabled("DecisionHistory_Button3", true);
				}

			}
			else{
				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock failed return code: "+ReturnCode);
				alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";

			}
		}
		return alert_msg;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to fetch Document Repeater

	 ***********************************************************************************  */


	public void fetchoriginalDocRepeater(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		PersonalLoanS.mLogger.info("INSIDE OriginalDocument:" +"");
		String query = "SELECT distinct DocName,DocSta,Remarks,OVRemarks,OVDec FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getNGValue("Parent_WIName")+"' order by DocName";
		List<String> repeaterHeaders = new ArrayList<String>();
		PersonalLoanS.mLogger.info("query"+""+ query);
		repeaterHeaders.add("Document List");

		repeaterHeaders.add("Status");
		repeaterHeaders.add("Received Date");
		repeaterHeaders.add("Expected Date");
		repeaterHeaders.add("Remarks");
		repeaterHeaders.add("OV Decision");
		repeaterHeaders.add("Approved By");
		PersonalLoanS.mLogger.info("INSIDE Original Validation:" +"after making headers");
		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");
		List<List<String>> documents = formObject.getNGDataFromDataCache(query);
		String documentName = null;
		IRepeater repObj;
		int repRowCount = 0;
		repObj = formObject.getRepeaterControl("OriginalValidation_Frame");
		PersonalLoanS.mLogger.info("INSIDE Original Validation:" +""+repObj.toString());
		PersonalLoanS.mLogger.info("docName"+""+ documents);

		try{
			if (documents.size()> 0) {
				PersonalLoanS.mLogger.info("RLOS Original Validation"+ "CLeared repeater object");
				repObj.clear();
				
				repObj.setRepeaterHeaders(repeaterHeaders);
				for(int i=0;i<documents.size();i++){
					repObj.addRow();
					PersonalLoanS.mLogger.info("document Added in Repeater"+" "+ documents.get(i).get(0));
					repObj.setValue(i, 0, documents.get(i).get(0));
					repObj.setValue(i, 1, documents.get(i).get(1));
					repObj.setValue(i, 4, documents.get(i).get(2));
					repRowCount = repObj.getRepeaterRowCount();
					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnEditable(0, false);
				repObj.setColumnEditable(1, false);
				repObj.setColumnEditable(6, false);
			}
			
		}
		catch (Exception e) {
			PersonalLoanS.mLogger.info("EXCEPTION    :    "+ " " + e.toString());
			printException(e);
		} 

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Calculate EMI

	 ***********************************************************************************  */

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
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occured in getEMI() : ");
			printException(e);
			loanAmt_DaysDiff = "0";
		}
		return loanAmt_DaysDiff;
	}	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Calculate EMI

	 ***********************************************************************************  */

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
			//emi = numerator.divide(denominator,0,RoundingMode.UP);
			emi = numerator.divide(denominator,0);

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occured in calcEMI() : ");
			printException(e);
		}
		return emi;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Calculate goalseekEMI

	 ***********************************************************************************  */

	public  String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);
			PMTEMI = PMTEMI.setScale(2,BigDecimal.ROUND_HALF_EVEN);
			
			//RLOS.mLogger.info("PMTEMI  **************"+PMTEMI);
			double seedvalue=PMTEMI.doubleValue();
				  		
			
			loanAmt_DaysDiff=Double.toString(seedvalue);
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occured in calcgoalseekEMI() : ");
			printException(e);
			loanAmt_DaysDiff="0";
		}

		return loanAmt_DaysDiff;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to print Exception


	 ***********************************************************************************  */


	public  static void printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		PersonalLoanS.mLogger.info("Inside exception :"+"\n"+e.getMessage()+" : \n"+exception);	

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Convert Date Format 


	 ***********************************************************************************  */

	public static String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		PersonalLoanS.mLogger.info("RLOS Common "+ "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date==null || "".equals(date))
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
			PersonalLoanS.mLogger.info("RLOS Common "+ "Exception occurred in parsing date:"+e.getMessage());
			printException(e);
		}
		return new_date;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function For Dectech Income


	 ***********************************************************************************  */

	public void income_Dectech(){
		try{
			PersonalLoanS.mLogger.info("PL"+ "Emp Type Value is:inside income");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();


			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
			PersonalLoanS.mLogger.info("PL"+ "Emp Type Value is:"+EmpType);

			if("Salaried".equalsIgnoreCase(EmpType)|| NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(EmpType))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 700);
				formObject.setHeight("IncomeDetails_Frame1", 720);  
			}
			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(EmpType))
			{                                                                                                              
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 300);
				formObject.setHeight("IncomeDetails_Frame1", 280);
			}

			formObject.setVisible("IncomeDetails_Label13",false);
			formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
			if(!"CSM".equals(formObject.getWFActivityName())){
			formObject.setVisible("IncomeDetails_Label15",true); 
			formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
			formObject.setVisible("IncomeDetails_Label16",true);
			formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
			}
			else{
				formObject.setVisible("IncomeDetails_Label15",false); 
				formObject.setVisible("cmplx_IncomeDetails_Totavgother",false);
				formObject.setVisible("IncomeDetails_Label16",false);
				formObject.setVisible("cmplx_IncomeDetails_compaccAmt",false);	
			}
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
			//Code added by aman to load the picklist at CAD
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking",true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat",true);
			}
			formObject.setVisible("IncomeDetails_Frame3",false);
		}
		catch(Exception e){
			System.out.println("Exceptiion in income"+e.getMessage());
			PersonalLoanS.mLogger.info("PL"+ "Emp Type Value is:inside income"+e.getMessage());
		}	
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for Dectech Employment


	 ***********************************************************************************  */

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

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to HIde/Unhide Decision Fields


	 ***********************************************************************************  */

	public void decisionLabelsVisibility()	
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();


		formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
		formObject.setVisible("Decision_Label1",false);
		formObject.setVisible("DecisionHistory_Label1",false);
		formObject.setVisible("DecisionHistory_ReferTo", false); //Arun (01-12-17) to hide this combo
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
	//below code added by nikhil 11/11/17
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Load      

	 ***********************************************************************************  */
	public void notepad_load(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		PersonalLoanS.mLogger.info( "Activity name is:" + sActivityName);

		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName());
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate);

		formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);
		formObject.setTop("NotepadDetails_SaveButton",200);
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Addition     

	 ***********************************************************************************  */
	public void Notepad_add(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");

		PersonalLoanS.mLogger.info(""+formObject.getUserId()+"-"+formObject.getUserName());

		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);

		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		// added by abhishek as per CC FSD
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);


		// to make frame in Add state
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
		formObject.setNGValue("NotepadDetails_notedesc", "--Select--",false);
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Modify      

	 ***********************************************************************************  */
	public void Notepad_modify(){
		// added by abhishek as per CC FSD
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		PersonalLoanS.mLogger.info( "Activity name is:" + sActivityName);

		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);

		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);

		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
		//below code added by abhishek on 10/11/2017
		formObject.setEnabled("NotepadDetails_Add", true);
		formObject.setEnabled("NotepadDetails_Delete", true);
		formObject.setNGValue("NotepadDetails_notedesc", "--Select--",false);

		//--Above code added by abhishek on 10/11/2017
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Grid        

	 ***********************************************************************************  */
	public void Notepad_grid(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");

		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		//++below code added by abhishek on 10/11/2017
		String gridValue = formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 5);
		String targetWorkstep =  formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 11);
		PersonalLoanS.mLogger.info("gridValue is: "+gridValue);
		PersonalLoanS.mLogger.info("sActivityName is: "+sActivityName);

		if(sActivityName.equalsIgnoreCase(gridValue)){
			formObject.setEnabled("NotepadDetails_Add", false);
			formObject.setEnabled("NotepadDetails_Delete", true);
			formObject.setLocked("NotepadDetails_notedesc", false);
			formObject.setLocked("NotepadDetails_notedetails", false);
			formObject.setEnabled("NotepadDetails_inscompletion",false);
			formObject.setLocked("NotepadDetails_ActuserRemarks",true);
		}
		//added by akshay on 15/1/18 for proc 3450
		else if(sActivityName.equalsIgnoreCase(targetWorkstep)){
			formObject.setEnabled("NotepadDetails_Add", false);
			formObject.setEnabled("NotepadDetails_Delete", false);
			formObject.setLocked("NotepadDetails_notedesc", true);
			formObject.setLocked("NotepadDetails_notedetails", true);
			formObject.setEnabled("NotepadDetails_inscompletion",true);
			formObject.setLocked("NotepadDetails_ActuserRemarks",false);
			formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName(),false);
			formObject.setNGValue("NotepadDetails_Actdate",modifiedDate,false);

		}
		else{
			formObject.setEnabled("NotepadDetails_Add", false);
			formObject.setEnabled("NotepadDetails_Delete", false);
			formObject.setEnabled("NotepadDetails_Modify", false);
			formObject.setLocked("NotepadDetails_notedesc", true);
			formObject.setLocked("NotepadDetails_notedetails", true);
			//formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
			formObject.setEnabled("NotepadDetails_inscompletion",false);
			formObject.setLocked("NotepadDetails_ActuserRemarks",true);
		}
		//ended by akshay on 15/1/18 for proc 3450

		formObject.setLocked("NotepadDetails_notecode",true);

		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);

		//--Above code added by abhishek on 10/11/2017
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Delete

	 ***********************************************************************************  */
	public void Notepad_delete(){
		// added by abhishek as per CC FSD
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		PersonalLoanS.mLogger.info( "Activity name is:" + sActivityName);

		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);


		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);


		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
		formObject.setNGValue("NotepadDetails_notedesc", "--Select--",false);
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Details without telloging frame        

	 ***********************************************************************************  */
	public void notepad_withoutTelLog(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setVisible("NotepadDetails_Frame3", false);
		formObject.setTop("NotepadDetails_save", formObject.getHeight("NotepadDetails_Frame2")+50);
		formObject.setHeight("NotepadDetails_Frame1", formObject.getTop("NotepadDetails_save")+100);
		formObject.setHeight("Notepad_Values", formObject.getHeight("NotepadDetails_Frame1")+5);
		formObject.setTop("DecisionHistory", formObject.getHeight("Notepad_Values")+20);

	}
	//--above code added by nikhil 11/11/17
	//++Below code added by nikhil 13/11/2017 for Code merge

	public void enable_busiVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();





		formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");

		formObject.setNGValue("cmplx_BussVerification_TradeLicName",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,6));
		formObject.setNGValue("cmplx_BussVerification_signame",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0));


		// disable fields

		formObject.setEnabled("cmplx_BussVerification_TradeLicName",false);
		formObject.setEnabled("cmplx_BussVerification_signame",false);




	}

	public void enable_homeVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		//below id changed by nikhil 10/12/17
			formObject.setNGValue("cmplx_HCountryVerification_hcountrytel",formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO"));

		formObject.setEnabled("cmplx_HCountryVerification_Hcountrytelverified",true);
		formObject.setEnabled("cmplx_HCountryVerification_hcountrytel",false);
		formObject.setEnabled("cmplx_HCountryVerification_personcontctd",true);
		formObject.setEnabled("cmplx_HCountryVerification_Relwithpersoncntcted",true);
		formObject.setEnabled("cmplx_HCountryVerification_Remarks",true);
		formObject.setEnabled("cmplx_HCountryVerification_Decision",true);

	}

	public void enable_ResVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		formObject.setNGValue("cmplx_ResiVerification_cntcttelno",formObject.getNGValue("AlternateContactDetails_ResidenceNo"));

		formObject.setEnabled("cmplx_ResiVerification_Telnoverified",true);
		formObject.setEnabled("cmplx_ResiVerification_cntcttelno",false);
		formObject.setEnabled("cmplx_ResiVerification_Remarks",true);
		formObject.setEnabled("cmplx_ResiVerification_Decision",true);

	}


	public void enable_ReferenceVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
		//below code added by nikhil 10/12/17
		if(formObject.getLVWRowCount("cmplx_ReferenceDetails_cmplx_ReferenceGrid")>0)
		{
		//below id changed by nikhil 10/12/17
			formObject.setNGValue("cmplx_RefDetVerification_ref1cntctno",formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,1));
			formObject.setNGValue("cmplx_RefDetVerification_ref1name",formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,0));
			formObject.setNGValue("cmplx_RefDetVerification_ref1phone",formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,2));
		}
		formObject.setLocked("cmplx_RefDetVerification_ref1cntctno",true);
		formObject.setLocked("cmplx_RefDetVerification_ref1name",true);
		formObject.setLocked("cmplx_RefDetVerification_ref1phone",true);



	}
	public void enable_officeVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
		formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");

		formObject.setNGValue("cmplx_OffVerification_offtelno",formObject.getNGValue("AlternateContactDetails_OfficeNo"));
		formObject.setNGValue("cmplx_OffVerification_fxdsal_val",formObject.getNGValue("cmplx_IncomeDetails_AvgNetSal"));
		formObject.setNGValue("cmplx_OffVerification_accprovd_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		//below code commented
		//formObject.setNGValue("cmplx_OffVerification_desig_val",formObject.getNGSelectedItemText("cmplx_EmploymentDetails_Designation"));
		formObject.setNGValue("cmplx_OffVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		formObject.setNGValue("cmplx_OffVerification_cnfrminjob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));
		String desig=formObject.getNGValue("cmplx_EmploymentDetails_Designation");
		//below code added by nikhil
		try
		{
			//below query changed by nikhil for cpv issued on 19/12/17
			List<List<String>> db=formObject.getNGDataFromDataCache("select Description from NG_MASTER_Designation where Code='"+desig+"'") ;
			if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
				desig=db.get(0).get(0); 
				PersonalLoanS.mLogger.info("fcu @ emirate"+desig);
			}
		}
		catch(Exception ex)
		{
			desig="";
		}
		formObject.setNGValue("cmplx_OffVerification_desig_val",desig);
		formObject.setEnabled("cmplx_OffVerification_offtelno",false);
		formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
		formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
		formObject.setEnabled("cmplx_OffVerification_desig_val",false);
		formObject.setEnabled("cmplx_OffVerification_doj_val",false);
		formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);




	}
	public void enable_loanCard(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();


		formObject.setEnabled("cmplx_LoanandCard_loanamt_val",false);
		formObject.setEnabled("cmplx_LoanandCard_tenor_val",false);
		formObject.setEnabled("cmplx_LoanandCard_emi_val",false);
		formObject.setEnabled("cmplx_LoanandCard_islorconv_val",false);
		formObject.setEnabled("cmplx_LoanandCard_firstrepaydate_val",false);
		formObject.setEnabled("cmplx_LoanandCard_cardtype_val",false);
		formObject.setEnabled("cmplx_LoanandCard_cardlimit_val",false);





	}
	public void loadInComplianceGrid()
	{

	       // FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PersonalLoanS.mLogger.info("CC_Common"+"Query is loadInWorldGrid ");
	        //Below code modified by nikhil 07/12/17
	        LoadPickList("Compliance_BirthCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country where isActive='Y' order by code");
	        LoadPickList("Compliance_ResidenceCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country where isActive='Y' order by code");

	}
	public void openFrags(FormReference formObject){

		int framestate0=formObject.getNGFrameState("Address_Details_container");
		if(framestate0 != 0){

			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
		}
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate1 != 0){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");

		}
		int framestate4=formObject.getNGFrameState("CustomerDetails");
		if(framestate4 != 0)
		{
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			}
			
		}
	  //function by saurabh on 10th dec
	  public void openDemographicTabs(){
		  FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//below code added by Arun (27/11/17) to fetch the fragments when Decision fragment is fetched.
			
		  if(!formObject.isVisible("AddressDetails_Frame1")){
				formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");				
				formObject.setNGFrameState("Address_Details_container", 0);

				PersonalLoanS.mLogger.info(" fetched address details");
			}

			if(!formObject.isVisible("AltContactDetails_Frame1")){
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setNGFrameState("Alt_Contact_container", 0);

				PersonalLoanS.mLogger.info("fetched address details");
			}
			
			if(!formObject.isVisible("CardDetails_Frame1")){
				formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");
				formObject.setNGFrameState("Card_Details", 0);

				PersonalLoanS.mLogger.info("fetched card details");
			}
			
			if(!formObject.isVisible("SupplementCardDetails_Frame1") && "Y".equals(formObject.getNGValue("IS_SupplementCard_Required"))){
				formObject.setVisible("Supplementary_Card_Details", true);
				formObject.fetchFragment("Supplementary_Card_Details", "SupplementCardDetails", "q_SuppCardDetails");
				formObject.setNGFrameState("Supplementary_Card_Details", 0);
				PersonalLoanS.mLogger.info("fetched Supplementary details");
			}
			//condition added by akshay on 30/1/18 to handle supplement tab on decision tab
			if(!formObject.isVisible("FATCA_Frame6")){
				formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
				formObject.setNGFrameState("FATCA", 0);
				//formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_container")+10);
				//formObject.setTop("FATCA", formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_container")+10);
				PersonalLoanS.mLogger.info("fetched FATCA details top: "+formObject.getTop("FATCA"));
				PersonalLoanS.mLogger.info("fetched FATCA details");
			}

			if(!formObject.isVisible("KYC_Frame7")){
				formObject.fetchFragment("KYC", "KYC", "q_KYC");
				formObject.setNGFrameState("KYC", 0);

				PersonalLoanS.mLogger.info("fetched address details");
			}


			if(!formObject.isVisible("OECD_Frame8")){
				formObject.fetchFragment("OECD", "OECD", "q_OECD");
				formObject.setNGFrameState("OECD", 0);
				//formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				PersonalLoanS.mLogger.info("fetched address details");
			}

			if(!formObject.isVisible("ReferenceDetails_Frame1")){
				formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
				formObject.setNGFrameState("ReferenceDetails", 0);
				PersonalLoanS.mLogger.info("fetched reference details");
			}
			if(formObject.isVisible("SupplementCardDetails_Frame1")){
				adjustFrameTops("Address_Details_container,Alt_Contact_container,Card_Details,Supplementary_Card_Details,FATCA,KYC,OECD,ReferenceDetails");
			}
			else{
				adjustFrameTops("Address_Details_container,Alt_Contact_container,Card_Details,FATCA,KYC,OECD,ReferenceDetails");
			}
			
		  /*
		  Below code commented by akshay on 9/3/18
		  
		  
		  int framestate2=formObject.getNGFrameState("Address_Details_container");
			if(framestate2 == 0){
				PersonalLoanS.mLogger.info(" Address_Details_container");
			}
			//changes made to code by saurabh on 10th Dec
			else {
				formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");				
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_container")+30);
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
				formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+50);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				
				
				formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				
				PersonalLoanS.mLogger.info(" fetched address details");
			}
			
			int framestate3=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate3 == 0){
				PersonalLoanS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")+30);
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
				formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+50);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				
				
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")+70);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+20);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				
				PersonalLoanS.mLogger.info("fetched address details");
			}
			
			int framestate3a =formObject.getNGFrameState("ReferenceDetails");
			if(framestate3a == 0){
				
			} 
			else{
				formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
				formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+50);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
			}
			if(formObject.isVisible("Card_Details")){
				int framestate3b =formObject.getNGFrameState("Card_Details");
				if(framestate3b == 0){
					
				} 
				else{
					formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");
					loadDataInCRNGrid();
					formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
					formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+50);
					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				}
			}
			
			int framestate4=formObject.getNGFrameState("FATCA");
			if(framestate4 == 0){
				PersonalLoanS.mLogger.info("FATCA");
			}
			else {
				formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+50);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				
				formObject.setTop("FATCA", formObject.getHeight("CardDetails")+formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getHeight("FATCA")+formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getHeight("KYC")+formObject.getTop("KYC")+20);
				
				PersonalLoanS.mLogger.info("fetched FATCA details");
			}
			//above code added for card & FATCA to be fetched while opening decision fragment

			int framestate5=formObject.getNGFrameState("KYC");
			if(framestate5 == 0){
				PersonalLoanS.mLogger.info("KYC");
			}
			else {
				formObject.fetchFragment("KYC", "KYC", "q_KYC");
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				
				formObject.setTop("KYC", formObject.getTop("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				
				PersonalLoanS.mLogger.info("fetched address details");
			}

			int framestate6=formObject.getNGFrameState("OECD");
			if(framestate6 == 0){
				PersonalLoanS.mLogger.info("OECD");
			}
			else {
				formObject.fetchFragment("OECD", "OECD", "q_OECD");
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				PersonalLoanS.mLogger.info("fetched address details");
			}
			adjustFrameTops("Address_Details_container,Alt_Contact_container,Card_Details,Supplementary_Card_Details,FATCA,KYC,OECD");
		*/	
			
			loadDemographicsData();
			if(!formObject.isVisible("ELigibiltyAndProductInfo_Frame1")){
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
				//loadEligibilityData();	
			}
			if(!formObject.isVisible("IncomeDetails_Frame1")){
				formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				income_Dectech();
			}
			
			if(!formObject.isVisible("EMploymentDetails_Frame1")){
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			}
			else {
				PersonalLoanS.mLogger.info("employment already loaded");
			}
			
			formObject.fetchFragment("Limit_Inc", "Limit_Inc", "q_FinIncident");
			//above code added by Arun (27/11/17) to fetch the fragments when Decision fragment is fetched.
	  }
	  public void FetchingDecision(){
		  FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			try{
				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
				//change by saurabh on 10th Dec
				openDemographicTabs();
				formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");//added by akshay on 17/1/18
				formObject.setVisible("cmplx_Decision_Manual_deviation_reason", false);
				//added by akshay on 12/12/17
				 List<String> referWorksteps_List=Arrays.asList("CSM","DDVT_maker","DDVT_Checker","CAD_Analyst1","CAD_Analyst2","CPV","CPV_Analyst","FCU","Compliance","DSA_CSO_Review");
				 if(referWorksteps_List.contains(formObject.getWFActivityName())){
						if(!formObject.isVisible("ReferHistory_Frame1")){
							formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
							formObject.setNGFrameState("ReferHistory", 0);
						}
				 }
				//ended by akshay on 12/12/17
				 
				loadInDecGrid();
				//saveIndecisionGrid();//arun 25/12/17
				loadPicklist3();
				//below code added by yash on 15/12/2017 for toteam 
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_Reject").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"REFER".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"Pending for documentation".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
					{
					formObject.setLocked("DecisionHistory_DecisionReasonCode",true);
					}
				//above code added by yash on 15/12/2017 for toteam
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
					formObject.setLocked("DecisionHistory_Button6",false);
					formObject.setEnabled("cmplx_Decision_CADDecisiontray", false);        
				}
				else{
					formObject.setLocked("DecisionHistory_Button6",true);
					formObject.setEnabled("cmplx_Decision_CADDecisiontray", true);         
				}
				
				String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
				PersonalLoanS.mLogger.info("sQuery list size"+sQuery);
				
				if(!OutputXML.isEmpty()){
					String HighDel=OutputXML.get(0).get(0);
					if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !OutputXML.get(0).get(0).equalsIgnoreCase("null") ){

						PersonalLoanS.mLogger.info("Inside the if dectech"+HighDel+" value is ");	
						formObject.setNGValue("cmplx_Decision_Highest_delegauth", HighDel);

					}
				}	
				//Code Change By aman to save Highest Delegation Auth
				

			}
			catch(Exception ex){
				printException(ex);
				}

			//added Tanshu Aggarwal(22/06/2017)
			if	(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")) && NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Create")))
			{
				PersonalLoanS.mLogger.info("inside both req Y");
				formObject.setLocked("DecisionHistory_Button2", true);
			}
			else{
				PersonalLoanS.mLogger.info( "inside both req N");
				formObject.setLocked("DecisionHistory_Button2", false);
			}
			//added Tanshu Aggarwal(22/06/2017)	
			//++Below code added by nikhil 13/11/2017 for Code merge
			
			//++ below code added by abhishek  to check workitem on CPV as per FSD 2.7.3

			
			 if (formObject.getWFActivityName().equalsIgnoreCase("Cad_Analyst1")){
					//for decision fragment made changes 8th dec 2017
 				/*formObject.setVisible("DecisionHistory_Label10", false);
 				formObject.setVisible("cmplx_DEC_New_CIFID", false);*/
					//for decision fragment made changes 8th dec 2017
 				
 			
 				
				
					String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
					boolean flag = false;
					/*StringBuilder values = new StringBuilder();
			        values.append(",'" + sProcessName + "'");*/

					String listValues ="";
					List<List<String>> list=formObject.getNGDataFromDataCache(sQuery);
					PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "Done button click::query result is::"+list );
					for(int i =0;i<list.size();i++ ){
						if(i==0){
							listValues += list.get(i).get(0);
							// values.append(list.get(i).get(0));
						}else{
							listValues += "|"+list.get(i).get(0);
							 //values.append(",'" + sProcessName + "'");
						}
						
					}
					
					sQuery = "Select cpv_decision FROM NG_CC_EXTTABLE with (nolock) WHERE cc_wi_name='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> list1=formObject.getNGDataFromDataCache(sQuery);
					PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "cpv dec value is::"+list1 );
					listValues +="#"+list1.get(0).get(0);
					PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "list value is::"+listValues );
					formObject.setNGValue("DecisionHistory_CadTempField",listValues);
					
				
			
				
                }
				//for decision fragment made changes 8th dec 2017
			
			//for decision fragment made changes 8th dec 2017
			 else if(formObject.getWFActivityName().equalsIgnoreCase("Smart_CPV")){
        	  
				//for decision fragment made changes 8th dec 2017
              	 if(formObject.getNGValue("cmplx_Decision_contactableFlag").equalsIgnoreCase("true")){
       				formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
       				formObject.setVisible("NotepadDetails_Label6", true);
       				formObject.setVisible("NotepadDetails_insqueue", true);
       				formObject.setVisible("NotepadDetails_inscompletion", true);
       				formObject.setVisible("NotepadDetails_Label10", true);
       				formObject.setVisible("NotepadDetails_Actdate", true);
       				formObject.setVisible("NotepadDetails_Label11", true);
       				formObject.setVisible("NotepadDetails_Actusername", true);
       				formObject.setVisible("NotepadDetails_Label9", true);
       				formObject.setVisible("NotepadDetails_ActuserRemarks", true);
       				LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription");
       				formObject.setNGValue("NotepadDetails_notedesc","--Select--");
       				
       				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
       				formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
       				
            	//	formObject.setEnabled("DecisionHistory_nonContactable", false);
       				formObject.setEnabled("NotepadDetails_Frame1", false);
       			//	formObject.setEnabled("DecisionHistory_Frame1", false);
       				formObject.setEnabled("SmartCheck_Frame1", false);
       				formObject.setEnabled("OfficeandMobileVerification_Frame1", false);
       				
       			//	formObject.setEnabled("DecisionHistory_cntctEstablished", true);
       				
       				formObject.setNGValue("cmplx_Decision_Decision","Smart CPV Hold");
       				
       				formObject.setNGFrameState("Office_verification", 1);
       				formObject.setNGFrameState("Smart_check", 1);
       				formObject.setNGFrameState("Notepad_Values", 1);
            	}else{
            		//for decision fragment made changes 8th dec 2017
            		
       				/*formObject.setEnabled("DecisionHistory_Frame1", true);
       				formObject.setEnabled("DecisionHistory_cntctEstablished", false);
       				formObject.setEnabled("DecisionHistory_nonContactable", true);
       				formObject.setNGValue("cmplx_Decision_Decision","--Select--");*/
            		//for decision fragment made changes 8th dec 2017
       				
       				
            	}
    	          	
           }
	  }
	  public void loadEligibilityData(){
		  FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		  PersonalLoanS.mLogger.info("inside loadEligibilityData"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));

		  PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType5asd cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));

		  LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", " select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) where isactive = 'Y'  ");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
			//query added by saurabh on 22nd Oct for loading base rate type.
			LoadPickList("cmplx_EligibilityAndProductInfo_BaseRateType", "select distinct PRIME_TYPE from NG_master_Scheme order by PRIME_TYPE");
		  
			if("CSM".equalsIgnoreCase(formObject.getWFActivityName())){
				setLoanFieldsVisible();
			}
			Eligibilityfields();
	  }
	  /*          Function Header:
		 
		**********************************************************************************
		 
		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
		 
		 
		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Function to make Loan fields Visible     
		 
		***********************************************************************************  */


		public void setLoanFieldsVisible() {
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setVisible("ELigibiltyAndProductInfo_Label11",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label31",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label12",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label14",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label15",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label17",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label16",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label18",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label23",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label13",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label30",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_AgeAtMaturity",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label32",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NoOfInstallments",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label19",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label20",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label21",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label22",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Text9",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Text8",true);
			//added by saurabh on 2nd Jan
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", true);
		}
	  //function by saurabh on 10th Dec
	  public void loadDemographicsData(){
		  FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		  if(!formObject.isVisible("AddressDetails_Frame1")){
		  loadPicklist_Address();
		  }
		  if(!formObject.isVisible("AltContactDetails_Frame1")){
		  LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");
			String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
		  }
		  if(!formObject.isVisible("ReferenceDetails_Frame1")){
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
		  }
		  if(!formObject.isVisible("FATCA_Frame6")){
			 PersonalLoanS.mLogger.info("***********cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation");
			LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
		  }
			String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
			
			if("O".equalsIgnoreCase(usRelation)){
				//changed from setenabled to setlocked by saurabh on 11 nov 17.
				formObject.setLocked("FATCA_Frame6", true);
				formObject.setLocked("cmplx_FATCA_USRelation",false);
				formObject.setEnabled("FATCA_Save", true);
			}
			if(!formObject.isVisible("OECD_Frame8")){
			loadPickListOECD();
			}
	  }
	  

	  public void loadDataInCRNGrid(){

			try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query = "select cardlimit.Card_Product AS cardProd,cardlimit.Final_Limit AS finalLim,'Primary' AS card_type,cust.passportNo AS passport from ng_rlos_IGR_Eligibility_CardLimit cardlimit, ng_RLOS_Customer cust WITH (nolock) where Cardproductselect = 'true' and ( cardlimit.wi_name = '"+formObject.getNGValue("parent_WIName")+"' or cardlimit.Child_Wi = '"+formObject.getWFWorkitemName()+"' ) AND cardlimit.Child_Wi = cust.wi_name UNION SELECT cardProduct AS cardProd,approvedlimit AS finalLim,'Supplementary' AS card_type,PassportNo AS passport FROM ng_RLOS_GR_SupplementCardDetails WHERE supplement_WIname = '"+formObject.getWFWorkitemName()+"' AND status1= 'Active' ORDER BY card_type";
			List<List<String>> records = formObject.getNGDataFromDataCache(query);
			if(records!=null && records.size()>0){
			UIComponent pComp = formObject.getComponent("cmplx_CardDetails_cmplx_CardCRNDetails");
			ListView objListView = ( ListView )pComp;
			//List<String> myList = new ArrayList<String>();
			
			int gridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
			List<String> gridrows = new ArrayList<String>();
			List<String> crnGridColumns = new ArrayList<String>();
			if(gridRowCount>0){
				for(int i=0;i<gridRowCount;i++){
					gridrows.add(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0)+":"+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,9));
				}
			}
			int columns = objListView.getChildCount();
			for(int j=0;j<columns;j++){
				crnGridColumns.add(((Column)(pComp.getChildren().get(j))).getName());
			}
			for(List<String> record:records){
				List<String> newRecord = new ArrayList<String>();
				String dbValue = record.get(0)+":"+record.get(2);
				if(!gridrows.contains(dbValue)){
					newRecord.add(record.get(0));
					for(int i=1;i<columns;i++){
						if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("wi_name")){
							newRecord.add(formObject.getWFWorkitemName());
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("final_limit")){
							if(record.get(1)!=null && !record.get(1).equalsIgnoreCase("null") && !record.get(1).equalsIgnoreCase("")){
								newRecord.add(record.get(1));
							}
							else{
								newRecord.add("0");
							}
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("Applicant Type")){
							newRecord.add(record.get(2));
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("PassportNo")){
							newRecord.add(record.get(3));
						}
						else{
						newRecord.add("");
						}
					}
					//String finalLimit = formObject.getNGValue("CardDetails_FinalLimit");
					formObject.addItemFromList("cmplx_CardDetails_cmplx_CardCRNDetails", newRecord);
				}
				else{
					if(gridrows.indexOf(record.get(0))>-1){
						String finalLimitInGrid = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",gridrows.indexOf(record.get(0)),crnGridColumns.indexOf("final_limit"));
						if(!finalLimitInGrid.equalsIgnoreCase(record.get(1))){
							formObject.setNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", gridrows.indexOf(record.get(0)), crnGridColumns.indexOf("final_limit"), record.get(1));
						}
					}
					formObject.addItemFromList("cmplx_CardDetails_cmplx_CardCRNDetails", newRecord);
				}
			}
			}
			}catch(Exception ex){
				PersonalLoanS.mLogger.info("CC Common Exception Inside loadDataInCRNGrid()");
		  		printException(ex);
			}
}


public void loadInCCCreationGrid(){
	try{
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	//String cif = formObject.getNGValue("cmplx_Customer_CIFNo");
	//String custname = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
	int rowCount = formObject.getLVWRowCount("cmplx_CCCreation_cmplx_CCCreationGrid");
	List<String> currentCRN=null;
	 
	if(rowCount>0){
		currentCRN= new ArrayList<String>();
		for(int i=0;i<rowCount;i++){
			String cardProdPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", i,5)+":"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", i,14); 
			currentCRN.add(cardProdPassport);
		}
	}
	int crngridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");//cmplx_CardDetails_cmplx_CardCRNDetails
	PersonalLoanS.mLogger.info("CC Common crngridRowCount is:"+crngridRowCount);
	PersonalLoanS.mLogger.info("CC Common rowCount is:"+rowCount);
	PersonalLoanS.mLogger.info("CC Common currentCRN is:"+currentCRN);
	if(crngridRowCount>0){
		String prod = "";
		List<List<String>> ccCreationgridRows = new ArrayList<List<String>>();
		Map<String,String> combinedlimit = setCombineLimitHashmapvalues();
		Map<String,String> crnGridColumnsToValues ;
		for(int i=0;i<crngridRowCount;i++){
			crnGridColumnsToValues = initializeCRNGridMap(i);
			String newCardProdPassport = crnGridColumnsToValues.get(getValueOfConstant("CRN_cardProduct"))+":"+crnGridColumnsToValues.get(getValueOfConstant("CRN_Passport"));
			if(currentCRN==null || !currentCRN.contains(newCardProdPassport)){
				PersonalLoanS.mLogger.info("CC Common inside for loop is:");
			String cardprod = crnGridColumnsToValues.get(getValueOfConstant("CRN_cardProduct"));
			String passport = crnGridColumnsToValues.get(getValueOfConstant("CRN_Passport"));
			List<String> gridRows = new ArrayList<String>();
			gridRows.add(formObject.getWFWorkitemName());
			gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_CRN")));
			gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_ECRN")));
			gridRows.add(getCustomerDetails(passport,true));
			gridRows.add(getCustomerDetails(passport,false));
			gridRows.add(cardprod);
			gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_FinalLimit")));
			if(combinedlimit==null || combinedlimit.isEmpty()){
				gridRows.add("");//combined limit
				}
				else{
					gridRows.add(combinedlimit.get(cardprod));//combined limit
				}
			gridRows.add("");
			gridRows.add("");
			gridRows.add("");
			gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_IntFeeProf")));
			gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_AppType")));
			gridRows.add(passport);
			//gridRows.add(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,9));//added by akshay on 27/2/18 for drop 4
			if(i==crngridRowCount-1){
			prod+="'"+cardprod+"'";
			}
			else{
				prod+="'"+cardprod+"',";
			}
			PersonalLoanS.mLogger.info("CC Common gridRow list is:"+gridRows);
			ccCreationgridRows.add(gridRows);
			
		}
		}
		ccCreationgridRows=addTemplateIdtoEveryRow(prod,ccCreationgridRows);
		for(List<String> row:ccCreationgridRows){
			formObject.addItemFromList("cmplx_CCCreation_cmplx_CCCreationGrid", row);
		}
			
	}
	
	}catch(Exception ex){
		PersonalLoanS.mLogger.info("CC Common Exception Inside loadInCCCreationGrid()");
		printException(ex);
	}
}

public  List<List<String>> addTemplateIdtoEveryRow(String prod, List<List<String>> ccCreationgridRows){
	try{
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	String templateId = "";
	if(!"".equalsIgnoreCase(prod)){
		String query = "select distinct templateId,CODE from ng_master_cardProduct where CODE in ("+prod+") and Subproduct='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"' and EmployerCategory = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)+"'";
		PersonalLoanS.mLogger.info("query for templateId is:"+query);
		List<List<String>> records = formObject.getNGDataFromDataCache(query);
		PersonalLoanS.mLogger.info("records received is:"+records);
		for(List<String> record: records){
			for(List<String> gridrow:ccCreationgridRows){
				PersonalLoanS.mLogger.info("CC Common gridrow.get(5) is:"+gridrow.get(5));
				PersonalLoanS.mLogger.info("CC Common record.get(1) is:"+record.get(1));
				if(gridrow.get(5).equalsIgnoreCase(record.get(1))){
					gridrow.add(record.get(0));
					PersonalLoanS.mLogger.info("final gridrow is:"+gridrow);
					//formObject.addItemFromList("cmplx_CCCreation_cmplx_CCCreationGrid", gridrow);
					//break;
				}
			}
		}
	}
	return ccCreationgridRows;
	}catch(Exception ex){
		PersonalLoanS.mLogger.info("Exception in addTemplateIdtoEveryRow is: ");
		printException(ex);
		return null;
	}
	
}

public Map<String,String> setCombineLimitHashmapvalues(){
	try{
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	Map<String,String> temp = new HashMap<String,String>();
	String query = "select distinct cl.Card_Product,tempTble.limit from (select max(cast(Final_Limit as float)) as limit,max(combined_limit) as val,max(Child_Wi) as child_wi from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'  group by case when combined_limit=1 then combined_limit else Card_Product end) as tempTble join ng_rlos_IGR_Eligibility_CardLimit cl on tempTble.val = cl.combined_limit and cl.Cardproductselect='true' and cl.Child_Wi= tempTble.child_wi";
	PersonalLoanS.mLogger.info("query for combined limit is:"+query);
	List<List<String>> records = formObject.getDataFromDataSource(query);
	PersonalLoanS.mLogger.info("query output for combined limit is:"+records);
	if(records!=null && records.size()>0){
		for(List<String> record:records){
			temp.put(record.get(0), record.get(1));
		}
	}
	return temp;
	}catch(Exception e){
		PersonalLoanS.mLogger.info("CC Common Exception Inside setCombineLimitHashmapvalues()");
		printException(e);
		return null;
	}
	
}
public Map<String,String> initializeCRNGridMap(int crnRowNum){
	try{
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	Map<String,String> temp = new HashMap<String,String>();
	UIComponent pComp = formObject.getComponent("cmplx_CardDetails_cmplx_CardCRNDetails");
	ListView objListView = ( ListView )pComp;
	int columns = objListView.getChildCount();
	for(int j=0;j<columns;j++){
		temp.put(((Column)(pComp.getChildren().get(j))).getName(),formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",crnRowNum,j));
	}
	return temp;
	}catch(Exception ex){
		PersonalLoanS.mLogger.info("Exception in initializeCRNGridMap is: ");
		printException(ex);
		return null;
	}
	
}
public String getValueOfConstant(String constName){
	return NGFUserResourceMgr_PL.getGlobalVar(constName);
}


public String getCustomerDetails(String passport,boolean forCif){
	try{
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	String listViewName = "cmplx_Decision_MultipleApplicantsGrid";
	String retValue = "";
	int multipleApplGirRowCount = formObject.getLVWRowCount(listViewName);
	for(int i=0;i<multipleApplGirRowCount;i++){
		if(passport.equals(formObject.getNGValue(listViewName, i, 2))){
			if(forCif){
				retValue = formObject.getNGValue(listViewName, i, 3);
				break;
			}	
			else if(!forCif){
				retValue = formObject.getNGValue(listViewName, i, 1);
				break;
			}
		}
		
	}
	return retValue;
	}catch(Exception ex){
		PersonalLoanS.mLogger.info("Exception in getCustomerDetails is: ");
		printException(ex);
	return "";
	}
}

	  public void loadDataInLimitInc(){
	  	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	  	formObject.setNGValue("cmplx_LimitInc_CIF", formObject.getNGValue("cmplx_Customer_CIFNo"));
	  	formObject.setNGValue("cmplx_LimitInc_CustomerName", formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme"));
	  	formObject.setNGValue("cmplx_LimitInc_Permanant",getFromproductGrid(4));
	  	String query = "select top 1 c.ECRN,c.General_Status,c.SchemeCardProd,d.Final_limit from ng_RLOS_CUSTEXPOSE_CardDetails c,ng_rlos_IGR_Eligibility_CardProduct d where Child_Wi = '"+formObject.getWFWorkitemName()+"' and c.Limit_Increase = 'true'";
	  	List<List<String>> record = formObject.getNGDataFromDataCache(query);
	  	String ecrn ="";
	  	String general_status ="";
	  	String cardProd ="";
	  	String finalLimit ="";
	  	if(record!=null && record.size()>0){
	  		ecrn = record.get(0).get(0);
	  		general_status = record.get(0).get(1);
	  		cardProd = record.get(0).get(2);
	  		finalLimit = record.get(0).get(3);
	  	}
	  	if("IM".equalsIgnoreCase(getFromproductGrid(2)) || "PU".equalsIgnoreCase(getFromproductGrid(2))){
	  		formObject.setNGValue("cmplx_LimitInc_NewCardProduct", getFromproductGrid(5));
	  	}
	  	else if("LI".equalsIgnoreCase(getFromproductGrid(2))){
	  		formObject.setNGValue("cmplx_LimitInc_NewCardProduct",cardProd);
	  	}
	  	formObject.setNGValue("cmplx_LimitInc_CRN", ecrn);
	  	formObject.setNGValue("cmplx_LimitInc_ECRN", ecrn);
	  	formObject.setNGValue("cmplx_LimitInc_New_Limit", finalLimit);
	  	
	  	
	  }

	  public String getFromproductGrid(int index){
	  	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	  	return formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,index);
	  }

	  
	  
	  public void autopopulateValuesFCU(FormReference formObject) {
			
			String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
			String mobNo2 = formObject.getNGValue("AlternateContactDetails_MobileNo2");
			String dob = formObject.getNGValue("cmplx_Customer_DOb");
			int adressrowcount = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			String poboxResidence = "";
			String emirate = "";
			
			for(int i=0;i<adressrowcount;i++)
			{
				String addType = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				if(addType.equalsIgnoreCase("Home"))
				{
					poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
					emirate = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
				}			
				}
			
			try
			{
				//below query changed by nikhil
				if("NA".equals(emirate))
				{
			List<List<String>> db = formObject.getNGDataFromDataCache("select Description from ng_master_state where Code='"+emirate+"'") ;
			if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
				emirate= db.get(0).get(0); 
			}
				}
		}
		catch(Exception ex)
		{
			emirate="";
		}


		String officeNo = formObject.getNGValue("AlternateContactDetails_OFFICENO");

		String emirateid=formObject.getNGValue("cmplx_Customer_EmiratesID");
		String cifid=formObject.getNGValue("cmplx_Customer_CIFNO");
		formObject.setNGValue("cmplx_CustDetailverification1_EmiratesId", emirateid);
		formObject.setNGValue("cmplx_CustDetailverification1_CIF_ID", cifid);
		formObject.setLocked("cmplx_CustDetailverification1_EmiratesId", true);
		formObject.setLocked("cmplx_CustDetailverification1_CIF_ID", true);

		setValuesFCU(formObject,mobNo1,mobNo2,dob,poboxResidence,emirate,officeNo);


	}
	public void setValuesFCU(FormReference formObject,String...values) {
		String[] controls = new String[]{"cmplx_CustDetailverification1_MobNo1_value","cmplx_CustDetailverification1_MobNo2_value","cmplx_CustDetailverification1_DOB_value",
				"cmplx_CustDetailverification1_PO_Box_Value","cmplx_CustDetailverification1_Emirates_value","cmplx_CustDetailverification1_Off_Telephone_value"
		};
		int i=0;
		for(String str : values){
			if(checkValue(str)){
				formObject.setNGValue(controls[i], str);

				PersonalLoanS.mLogger.info("FCU@nikhil"+controls[i]+"::"+ str);

			}
			else{
				PersonalLoanS.mLogger.info("CC Initiation"+ "value received at index "+i+" is: "+str);
			}
			formObject.setLocked(controls[i], true);
			i++;
		}
	}
	public boolean checkValue(String ngValue){
		if(ngValue==null ||ngValue.equalsIgnoreCase("") ||ngValue.equalsIgnoreCase(" ") || ngValue.equalsIgnoreCase("--Select--") || ngValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}
	//++ below code added by abhishek as per FSD 2.7.3
	public void enable_custVerification()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		// auto populaate fields from prev tabs
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			formObject.setNGValue("cmplx_CustDetailVerification_Mob_No1_val", formObject.getNGValue("AlternateContactDetails_MobileNo1"));
			formObject.setNGValue("cmplx_CustDetailVerification_Mob_No2_val", formObject.getNGValue("AlternateContactDetails_MobileNo2"));
			formObject.setNGValue("cmplx_CustDetailVerification_email1_val", formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI"));
			formObject.setNGValue("cmplx_CustDetailVerification_email2_val", formObject.getNGValue("AlternateContactDetails_EMAIL2_SEC"));
			formObject.setNGValue("cmplx_CustDetailVerification_dob_val", formObject.getNGValue("cmplx_Customer_DOb"));
			formObject.setNGValue("cmplx_CustDetailVerification_Resno_val", formObject.getNGValue("AlternateContactDetails_ResidenceNo"));
			formObject.setNGValue("cmplx_CustDetailVerification_Offtelno_val", formObject.getNGValue("AlternateContactDetails_OfficeNo"));
			formObject.setNGValue("cmplx_CustDetailVerification_hcountrytelno_val", formObject.getNGValue("AlternateContactDetails_HomeCOuntryNo"));
			// formObject.setNGValue("", formObject.getNGValue(""));
			//below code added
			String home_country="";
			int address_row_count=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			for(int i=0;i<address_row_count;i++)
			{
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,11).equalsIgnoreCase("true"))
				{
					formObject.setNGValue("cmplx_CustDetailVerification_POBoxNo_val", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8));
					formObject.setNGValue("cmplx_CustDetailVerification_emirates_val", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,6));
					//formObject.setNGValue("cmplx_CustDetailVerification_POBoxNo_val", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0));
					
				}
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0).equalsIgnoreCase("Home"))
				{
					home_country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+" , "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+" , "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+" , "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,5);
				}
				if("OFFICE".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0)))
				{
					formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_val", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8));
				}
				
				
			}
			
		formObject.setNGValue("cmplx_CustDetailVerification_hcountryaddr_val", home_country);	

		formObject.setEnabled("cmplx_CustDetailVerification_Mob_No1_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_Mob_No2_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_email1_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_email2_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_dob_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_POBoxNo_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_emirates_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_persorcompPOBox_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_Offtelno_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_Resno_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_hcountrytelno_val",false);
		formObject.setEnabled("cmplx_CustDetailVerification_hcountryaddr_val",false);
		//below code added by nikhil 29/12/17 for CPV CR
		formObject.setVisible("cmplx_CustDetailVerification_Resno_val", false);
		formObject.setVisible("CustDetailVerification_Label17", false);
		formObject.setVisible("cmplx_CustDetailVerification_resno_ver", false);
		formObject.setVisible("cmplx_CustDetailVerification_resno_upd", false);
		formObject.setVisible("CustDetailVerification_Label8", false);
		formObject.setVisible("cmplx_CustDetailVerification_Offtelno_val", false);
		formObject.setVisible("cmplx_CustDetailVerification_offtelno_ver", false);
		formObject.setVisible("cmplx_CustDetailVerification_offtelno_upd", false);
		



	}

	public void autopopulate_Compliance_risk(FormReference formObject)
	{
		formObject.setNGValue("cmplx_Compliance_CustomerName", formObject.getNGValue("CustomerLabel"));
		formObject.setNGValue("cmplx_Compliance_CifId", formObject.getNGValue("CifLabel"));
		formObject.setNGValue("cmplx_Compliance_RMName", formObject.getNGValue("RmTlNameLabel"));
		formObject.setNGValue("cmplx_Compliance_EMpType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5));
		formObject.setNGValue("cmplx_Compliance_BussSegment", formObject.getNGValue("cmplx_RiskRating_BusinessSeg"));
		formObject.setNGValue("cmplx_Compliance_subSegment", formObject.getNGValue("cmplx_RiskRating_SubSeg"));
		formObject.setNGValue("cmplx_Compliance_demographic", formObject.getNGValue("cmplx_RiskRating_Demographics"));
		formObject.setNGValue("cmplx_Compliance_industry", formObject.getNGValue("cmplx_RiskRating_Industry"));
		formObject.setNGValue("cmplx_Compliance_riskRating", formObject.getNGValue("cmplx_RiskRating_Riskrating"));
		formObject.setNGValue("cmplx_Compliance_totRiskScore", formObject.getNGValue("cmplx_RiskRating_Total_riskScore"));
		formObject.setNGValue("cmplx_Compliance_nationality_companyDomicile", formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("cmplx_Compliance_product", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
		formObject.setNGValue("cmplx_Compliance_custCategory", formObject.getNGValue("cmplx_Customer_CustomerCategory"));
		formObject.fetchFragment("KYC", "KYC", "q_KYC");
		formObject.setNGValue("cmplx_Compliance_ExposedPerson", formObject.getNGValue("cmplx_KYC_PEP"));

	}

	//added by yash on 19/12/2017 for toteam
	public void ToTeam_AddtoGrids(FormReference formObject,String lc_doc_name,String bank_name,String bank_status,String bank_type)
	{
		int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
		String status=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate", row_count-1, 7);
		if("Active".equalsIgnoreCase(status))
		{
			PersonalLoanS.mLogger.info("LC Document Name"+formObject.getNGValue("PostDisbursal_LC_LC_Doc_Name"));
			formObject.setLocked("PostDisbursal_Frame3", false);
			formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);
			formObject.setLocked("PostDisbursal_Frame6", false);
			formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
			formObject.setLocked("PostDisbursal_Frame4", false);
			formObject.setLocked("PostDisbursal_Frame5", true);
			formObject.setNGValue("cmplx_PostDisbursal_STLReceivedDate", "");
			formObject.setNGValue("PostDisbursal_MCQ_Doc_Name", lc_doc_name);
			formObject.setNGValue("PostDisbursal_BG_LC_Doc_Name", lc_doc_name);
			formObject.setNGValue("PostDisbursal_NLC_LC_Doc_name", lc_doc_name);
			formObject.setNGValue("PostDisbursal_NLC_Bank_Name", bank_name);
			//added by yash on 19/12/2017 for to team
			formObject.setNGValue("PostDisbursal_NLC_Bank_Type", bank_type);

			formObject.setNGValue("PostDisbursal_BG_Status", bank_status);
			formObject.setNGValue("PostDisbursal_NLC_Status", "Awaited");
			//ended by yash on 19/12/2017
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
		}
		

	}
	//added by byash on 22/12/2017
	public void ToTeamStl(FormReference formObject)
	{
		int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cpmlx_gr_NLC");
		String status=formObject.getNGValue("cmplx_PostDisbursal_cpmlx_gr_NLC", row_count-1, 2);
		
			if("Customer".equalsIgnoreCase(status))
			{
				formObject.setLocked("PostDisbursal_Frame5", false);
			}
		
	}

	//added by akshay on 8/12/17 for alignment
	public void fragment_ALign(String field_list,String fragment_Name)
	{
		PersonalLoanS.mLogger.info("CC_Common"+" Inside fragment_ALign(): List of fields is-->"+field_list);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String field_list_array[] = field_list.split("#");
			int fieldsInARow_count=0;
			int current_top = 10;
			int current_left = 10;
			int top_diff=40;
			int left_diff = 60;
			String lable_name,field_name="";
			for(int field_list_count = 0 ;field_list_count<field_list_array.length;field_list_count++ ){
				lable_name="";

				if(field_list_array[field_list_count].split(",").length>1){
					lable_name =field_list_array[field_list_count].split(",")[0];
					field_name = field_list_array[field_list_count].split(",")[1];
					formObject.setVisible(lable_name, true);
					formObject.setVisible(field_name, true);

				}
				else{
					field_name = field_list_array[field_list_count].split("#")[0];//for checkbox or button
					formObject.setVisible(field_name, true);
				}

				PersonalLoanS.mLogger.info("**********************"+field_list_count+"***********");
				PersonalLoanS.mLogger.info(lable_name+" lable_name top: "+current_top+ " lable_name left: "+current_left );
				PersonalLoanS.mLogger.info(field_name + " field_name top: "+(current_top+formObject.getHeight(lable_name)+2)+" lable_name left: "+current_left );

				if("\n".equals(field_name)){
					continue;
				}
				if(!"".equals(lable_name)){//for normal case
					formObject.setTop(lable_name, current_top);
					if((current_left+formObject.getWidth(field_name))<formObject.getWidth(fragment_Name+"_Frame1")){
						formObject.setLeft(lable_name, current_left);
					}
					formObject.setTop(field_name, (current_top+formObject.getHeight(lable_name)+2));

				}
				else{//for button or chckbox or grid when there is no label
					formObject.setTop(field_name, current_top);
				}
				if((current_left+formObject.getWidth(field_name))<formObject.getWidth(fragment_Name+"_Frame1")){
					formObject.setLeft(field_name, current_left);
					if(!"".equals(lable_name) && formObject.getWidth(lable_name)>formObject.getWidth(field_name)){//if width of label is more
						current_left = formObject.getLeft(lable_name)+formObject.getWidth(lable_name)+left_diff;
					}
					else{
						current_left = formObject.getLeft(field_name)+formObject.getWidth(field_name)+left_diff;
					}
				}

				fieldsInARow_count++;
				if(field_list_count<field_list_array.length-1){//check if next element exist as checking (i+1)th element---to avoid arrayOutOfBOund exception
					if((fieldsInARow_count)/5>0 || "\n".equals(field_list_array[field_list_count+1]) || (current_left+formObject.getWidth(field_name))>formObject.getWidth("DecisionHistory_Frame1")){
						PersonalLoanS.mLogger.info("***********Inside /5 IF***********"+(fieldsInARow_count)/4);
						current_top=formObject.getTop(field_name)+formObject.getHeight(field_name)+top_diff;
						current_left=10;
						fieldsInARow_count=0;
					}
				}
				PersonalLoanS.mLogger.info("**********************");

			}    
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception Inside fragment_Align()");
			printException(e);

		}
	}
	//ended by akshay on 8/12/17 for alignment
	
	public void	loadInExtBlacklistGrid(FormReference formObject){

		//String cust_name=formObject.getNGValue("CustomerLabel");
		String query="Select a.customername,a.passportNo, b.custid,b.BlacklistFlag,b.BlacklistDate,b.BlacklistReasonCode from ng_pl_exttable a JOIN ng_rlos_cif_detail b on  a.parent_WIName=b.cif_wi_name WHERE b.cif_wi_name='"+formObject.getNGValue("parent_WIName")+"' and cif_SearchType = 'External'";

		PersonalLoanS.mLogger.info("Inside loadInExtBlacklistGrid()--Query is: "+query);
		List<List<String>> outputList=formObject.getNGDataFromDataCache(query);
		PersonalLoanS.mLogger.info("Ext Blacklist Output List is:"+outputList);
		for(List a:outputList){
			formObject.addItem("cmplx_ExtBlackList_cmplx_gr_ExtBlackList",a);
		}
	}
	
	public void fetch_CardDetails_frag(FormReference formObject){
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n==0){
			formObject.setNGFrameState("Card_Details",1);
			throw new ValidatorException(new FacesMessage("cmplx_Product_cmplx_ProductGrid#Please Add a product first"));
		}
		if(n>0){
			formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");

			LoadPickList("CardDetails_TransactionFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_TransactionFeeProfile where isActive='Y' order by code");
			LoadPickList("CardDetails_InterestFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_InterestProfile where isActive='Y' order by code");
			LoadPickList("CardDetails_FeeProfile","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_feeprofile where isActive='Y' order by code");
		
		/*	String Product_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_Islamic").equalsIgnoreCase(Product_Type)){
				formObject.setEnabled("cmplx_CardDetails_charity_amount", true);
				formObject.setEnabled("cmplx_CardDetails_Charity_org", true);
			}
			else{
				formObject.setNGValue("cmplx_CardDetails_charity_amount","");
				formObject.setNGValue("cmplx_CardDetails_Charity_org","");
				formObject.setEnabled("cmplx_CardDetails_charity_amount", false);
				formObject.setEnabled("cmplx_CardDetails_Charity_org", false);
			}*/
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) ){
				formObject.setEnabled("cmplx_CardDetails_CompanyEmbossing_name", true);
			}
			else{
				formObject.setNGValue("cmplx_CardDetails_CompanyEmbossing_name","");
				formObject.setEnabled("cmplx_CardDetails_CompanyEmbossing_name", false);
			}
			if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Supplementary_Card"))){
				PersonalLoanS.mLogger.info("Is Supp req is yes: ");
				formObject.setVisible("Supplementary_Cont",true);
				if(formObject.getWFActivityName().equalsIgnoreCase("DDVT_Checker") || formObject.getWFActivityName().equalsIgnoreCase("Disbursal")){
					PersonalLoanS.mLogger.info("activity is checker: ");
					formObject.fetchFragment("Supplementary_Cont", "SupplementCardDetails", "q_SuppCardDetails");//q_SuppCardDetails
					loadPicklist_suppCard();
					formObject.setNGFrameState("Supplementary_Cont",0);
					adjustFrameTops("Card_Details,Supplementary_Cont,FATCA,KYC,OECD,Reference_Details");
				}
				
			}
		}

		LoadPickList("cmplx_CardDetails_MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' order by code");
		LoadPickList("cmplx_CardDetails_CustClassification","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' order by code");
		//++ Below Code added By abhishek on Oct 9, 2017  to fix : "42,43,44-Transaction fee profile masters are incorrect,intetrest fee profile masters are incorrect,fee profile masters are incorrect" : Reported By Shashank on Oct 05, 2017++

		LoadPickList("CardDetails_TransactionFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_TransactionFeeProfile where isActive='Y' order by code");
		LoadPickList("CardDetails_InterestFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_InterestProfile where isActive='Y' order by code");
		LoadPickList("CardDetails_FeeProfile","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_feeprofile where isActive='Y' order by code");
		//change by saurabh on 4th Jan
		loadDataInCRNGrid();
		if(!"Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")))
		{
			formObject.setVisible("CardDetails_Label2",false);
			formObject.setVisible("cmplx_CardDetails_compemboss",false);
		}
		//if("Islamic".equalsIgnoreCase(arg0))

	}
	
	
	public void loadPicklist_suppCard(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("SupplementCardDetails_Nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SupplementCardDetails_Gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
		LoadPickList("SupplementCardDetails_ResidentCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SupplementCardDetails_Relationship", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock)");
		LoadPickList("SupplementCardDetails_MarketingCode", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock)");
		LoadPickList("SupplementCardDetails_Title", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_title with (nolock)");
		LoadPickList("SupplementCardDetails_Designation", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_designation with (nolock)");
		//LoadPickList("SupplementCardDetails_CardProduct","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
		LoadPickList("SupplementCardDetails_EmploymentStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock) where isactive='Y'");
		LoadPickList("SupplementCardDetails_maritalStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Maritalstatus with (nolock) where isactive='Y'");
		//complex query to load supplementary card products by saurabh
		LoadPickList("SupplementCardDetails_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
				
	}
	//added by saurabh for Active/inactive status for supplementary.
		public void validateStatusForSupplementary(){
			try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(formObject.isVisible("SupplementCardDetails_Frame1")){
				int supplementaryRows = formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");
				PersonalLoanS.mLogger.info("RLOS supplementary row count: "+supplementaryRows);
				Map<Integer, String> suppGridCardsSelected = new HashMap<Integer, String>();
				if(supplementaryRows>0){
						List<String>selectedCardProds = new ArrayList<String>();
						String query = "SELECT distinct card_product FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name='"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true'";
						PersonalLoanS.mLogger.info("RLOS supplementary query: "+query);
						List<List<String>> records = formObject.getDataFromDataSource(query);
						PersonalLoanS.mLogger.info("RLOS supplementary records: "+records);
						if(records!=null && records.size()>0){
							if(records.get(0) != null){
								for(List<String> cardProd : records){
									selectedCardProds.add(cardProd.get(0));
								}
							}
						}
						PersonalLoanS.mLogger.info("RLOS supplementary selected card prods: "+selectedCardProds);
						for(int j=0;j<supplementaryRows;j++){
							PersonalLoanS.mLogger.info("value for cardproduct for row: "+j+" is: "+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 22));
							if(selectedCardProds.contains(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",j,22))){
								//suppGridCardsSelected.put(j, "Active");
								formObject.setNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 27, "Active");
							}
							else{
								formObject.setNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 27, "InActive");
							}
							PersonalLoanS.mLogger.info("value for status for row: "+j+" is: "+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 27));
						}
					}
			}
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception occurred in validateStatusForSupplementary function:"+e.getMessage());
			printException(e);
		}
		}
	//added by akshay on 9/1/17
	public void adjustFrameTops(String frameList){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
		String[] framelist_array=frameList.split(",");
		if(framelist_array.length<2)
			return;
		
		for(int i=1;i<framelist_array.length;i++){
			PersonalLoanS.mLogger.info("Frame state for "+framelist_array[i]+": "+formObject.getNGFrameState(framelist_array[i]));
			if(formObject.getNGFrameState(framelist_array[i-1])==0 || formObject.getNGFrameState(framelist_array[i-1])==-1 ){//frame expanded
				formObject.setTop(framelist_array[i], formObject.getTop(framelist_array[i-1])+formObject.getHeight(framelist_array[i-1])+30);
			}
			else{//frame not expanded
				formObject.setTop(framelist_array[i], formObject.getTop(framelist_array[i-1])+30);
			}
		}
	}catch(Exception e){
		PersonalLoanS.mLogger.info("Exception occurred in adjustFrameTops()"+e.getMessage());
		printException(e);
	}
	}
	
	//added By Tarang for drop 4 point 5 started on 20/02/2018
	public void  loadPicklistDisbursal()
    {
        try
        {
            FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
            String ActivityName=formObject.getWFActivityName();
            PersonalLoanS.mLogger.info("PL"+"Inside PLCommon ->loadPicklistDisbursal()"+ActivityName);
 
            LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"' and Initiation_Type IS NULL");
        }
        catch(Exception e){ 
            PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklistDisbursal :"+e.getMessage());
            printException(e);
        }    
    } 
	//added By Tarang for drop 4 point 5 ended on 20/02/2018
	
	public void checkForDeferredWaivedDocs(){
	 	   FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	 	   try{
	 	   IRepeater repObj= formObject.getRepeaterControl("IncomingDoc_Frame2");
	 	   int repRows = repObj.getRepeaterRowCount();
	 	   if(repRows>0){
	 		   String defdocs = "";
	 		   String waivDocs = "";
	 		   boolean setWaiverExtTableFlag = false;
	 		  boolean setDeferExtTableFlag = false;
	 		   for(int i=0;i<repRows;i++){
	 			   String status = repObj.getValue(i, "cmplx_DocName_Doc_Status");
	 			   if("Deferred".equalsIgnoreCase(status)){
	 				   if(i==repRows-1){
	 				   defdocs+=repObj.getValue(i, "cmplx_DocName_DocName");
	 				   }
	 				   else{
	 					   defdocs+=repObj.getValue(i, "cmplx_DocName_DocName")+",";
	 				   }
	 				   setDeferExtTableFlag = true;
	 			   }
	 			   else if("Waived".equalsIgnoreCase(status)){
	 				   if(i==repRows-1){
	 					   waivDocs+=repObj.getValue(i, "cmplx_DocName_DocName");
		    				}
		    				else{
		    					waivDocs+=repObj.getValue(i, "cmplx_DocName_DocName")+",";
		    				}
	 				  setWaiverExtTableFlag = true;
	 			   }
	 		   }
	 		   formObject.setNGValue("DeferredDocsList", defdocs);
	 		   formObject.setNGValue("waivedDocsList", waivDocs);
	 		   if(setDeferExtTableFlag){
	 			   formObject.setNGValue("Deferral_req", "Y");
	 		   }
	 		  if(setWaiverExtTableFlag){
				   formObject.setNGValue("Waiver_req", "Y");
			   }
	 	   }
	 	   }catch(Exception ex){
	 		  PersonalLoanS.mLogger.info("Exception in checkForDeferredWaivedDocs function: ");
	 		 printException(ex);
	 		   
	 	   }
	    }
		
	public String getCurrentAccountNumber(String callname){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String acc_no= formObject.getNGValue("cmplx_Decision_AccountNo");
		String currAccflag="N";
		if(acc_no == null || "".equalsIgnoreCase(acc_no )){
		String qry = "select AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType like '%CURRENT ACCOUNT%' and ( Child_Wi = '"+formObject.getWFWorkitemName()+"' or Wi_Name ='"+formObject.getWFWorkitemName()+"') and FundingAccount='true'";
		PersonalLoanS.mLogger.info("query is"+qry +"");
		List<List<String>> record = formObject.getDataFromDataSource(qry);
		if(record!=null && record.get(0)!=null && !record.isEmpty()){
			acc_no = record.get(0).get(0);
			currAccflag="Y";
		}
		}
		if(callname.equals("ENTITY_MAINTENANCE_REQ")){
		return acc_no+"!"+currAccflag;
		}
		return acc_no;
		
	}
	
	public void IslamicFieldsvisibility() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query = "select distinct limit.Card_Product,limit.CC_Waiver,mstr.ReqProduct from ng_rlos_IGR_Eligibility_CardLimit limit, ng_master_cardProduct mstr where child_wi = '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true' and limit.Card_Product=mstr.CODE";
		List<List<String>> records = formObject.getDataFromDataSource(query);
		PersonalLoanS.mLogger.info("query produced is:"+query);
		try{
			boolean cc_waiver = false;
			boolean islamic_visible = false;
			boolean islamic_mandatory = false;
			boolean kalyanCompany = false;
			if(records!=null && records.size()>0){
				PersonalLoanS.mLogger.info("records list is:"+records);
				for(List<String> row:records){
					PersonalLoanS.mLogger.info("row list is:"+records);
					if( row.get(2)!=null && row.get(2).equalsIgnoreCase("Islamic")){
						islamic_visible=true;
						islamic_mandatory =true;
					}
					else if( row.get(0)!=null && row.get(0).contains("KALYAN")){
						kalyanCompany = true;
					}
					if(row.get(1)!=null  && row.get(1).equalsIgnoreCase("true")){
						cc_waiver =true;
					}
				}
			}
			if(islamic_visible){
				formObject.setVisible("CardDetails_Label3", true);
				formObject.setVisible("cmplx_CardDetails_Charity_org", true);
				formObject.setVisible("CardDetails_Label4", true);
				formObject.setVisible("cmplx_CardDetails_charity_amount", true);
				formObject.setLeft("CardDetails_Label5", 1059);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);
			}
			else
			{
				formObject.setVisible("CardDetails_Label3", false);
				formObject.setVisible("cmplx_CardDetails_Charity_org", false);
				formObject.setVisible("CardDetails_Label4", false);
				formObject.setVisible("cmplx_CardDetails_charity_amount", false);
				
			}
			if(islamic_mandatory){
				formObject.setNGValue("CardDetails_Islamic_mandatory","Y");
			}
			if(kalyanCompany){
				formObject.setVisible("CardDetails_Label2", true);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossing_name", true);
			}
			/*if(!cc_waiver){
				formObject.setVisible("CardDetails_Label7", true);
				formObject.setVisible("cmplx_CardDetails_statCycle", true);
			}*/
		}catch(Exception ex){
			PersonalLoanS.mLogger.info( "Exception in IslamicFieldsVisibility Function");
			printException(ex);
		}
	}
}




