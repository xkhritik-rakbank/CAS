package com.newgen.omniforms.user;
import com.newgen.custom.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;

import com.newgen.omniforms.util.Common;
import com.newgen.omniforms.util.PL_SKLogger;
import com.newgen.omniforms.util.PL_SKLogger;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import com.newgen.omniforms.util.CommonUtilityMethods;
public class PLCommon extends Common{

	public void getAge(String dateBirth){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("RLOS_Common", "Inside getAge(): "); 
		String parts[] = dateBirth.split("/");
		Calendar dob = Calendar.getInstance();
		Calendar today = Calendar.getInstance();

		dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

		Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
			age--; 
		}
		PL_SKLogger.writeLog("RLOS_Common", "Values are: "+parts[2]+parts[1]+parts[0]+age); 


		formObject.setNGValue("cmplx_Customer_age",age.toString(),false); 
	}
	public void setcustomer_enable(){
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

	public int primary_cif_identify(Map<String, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry = new HashMap<String, String>();
			Map<String, String> curr_entry = new HashMap<String, String>();


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

		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
		}

		return primary_cif;
	}


	public void save_cif_data(Map<String, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			PL_SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 


			String WI_Name = formObject.getWFWorkitemName();
			PL_SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos wi_name: "+WI_Name );
			Map<String, String> curr_entry = new HashMap<String, String>();
			Iterator<Map.Entry<String, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();

			while(itr.hasNext()){
				List<String> Cif_data=new ArrayList<String>();
				Map.Entry<String, HashMap<String, String>> entry =  itr.next();
				curr_entry = entry.getValue();

				Cif_data.add(curr_entry.get("SearchType"));
				Cif_data.add(curr_entry.get("CustId"));
				Cif_data.add(curr_entry.get("PassportNum"));
				Cif_data.add(curr_entry.get("BlacklistFlag"));
				Cif_data.add(curr_entry.get("DuplicationFlag"));
				Cif_data.add(curr_entry.get("NegatedFlag"));
				Cif_data.add(curr_entry.get("Products"));
				if(curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")){
					Cif_data.add("Y");
				}
				else{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItem("q_cif_detail", Cif_data);

				PL_SKLogger.writeLog("RLSOCommon", "data to dave in cif details grid: "+ Cif_data);
				curr_entry=null;
				Cif_data=null;
			}
		}
		catch(Exception e){
			PL_SKLogger.writeLog("Exception occured while saving data for Customer Eligibility : ", e.getMessage());
		}


	}
	public void set_nonprimaryPassport(int cif_id, String pass_list){
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

	public void parse_cif_eligibility(String output){

		try{
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			PL_SKLogger.writeLog("inside parse_cif_eligibility","");
			Map<String, HashMap<String, String> > Cus_details = new HashMap<String, HashMap<String, String>>();
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
					Cus_details.put(cif_details.get("CustId"), (HashMap<String, String>) cif_details) ;
				}
			}
			int Prim_cif = primary_cif_identify(Cus_details);
			save_cif_data(Cus_details,Prim_cif);
			if(Prim_cif!=0){
				PL_SKLogger.writeLog("parse_cif_eligibility Primary CIF: ",Prim_cif+"");
				Map<String, String> prim_entry = new HashMap<String, String>();
				prim_entry = Cus_details.get(Prim_cif+"");
				String primary_pass = prim_entry.get("PassportNum");
				passport_list = passport_list.replace(primary_pass, "");
				set_nonprimaryPassport(Prim_cif,passport_list);
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
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklistCustomer: ");
		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_Country order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state order by Code");
	}
	public void loadPicklist_LoanDetails()  
	{
		LoadPickList("cmplx_LoanDetails_repfreq", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_RepaymentFrequency with (nolock) where isactive = 'Y'  order by code");
		LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Collection_Branch with (nolock) order by Code");
		LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstallmentType with (nolock) order by Code");
		LoadPickList("cmplx_LoanDetails_inttype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Intrest_type with (nolock) order by Code");
	}
	public void loadPicklistELigibiltyAndProductInfo()  
	{
		LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
		LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
		LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstrumentType with (nolock) where isactive = 'Y'  order by code");
	}
	public void loadPicklistProduct(String ReqProd)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklistProduct$"+ReqProd+"$");
		String ProdType=formObject.getNGValue("Product_type");

		if(ReqProd.equalsIgnoreCase("Personal Loan")){
			PL_SKLogger.writeLog("RLOS_Common", "Inside equalsIgnoreCase()"); 
			formObject.setVisible("Scheme", true);
			formObject.setLeft("Scheme", 555);
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme", "--Select--");
			LoadPickList("SubProd", "select '--select--',''as 'code' union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL   where WorkstepName = '"+formObject.getWFActivityName()+"' order by code");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) where SchemeDesc like 'P_%' order by SCHEMEID");
			//added by abhishek
			LoadPickList("EmpType", " select '--Select--' union select convert(varchar, description) from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"'");
		}
		else{
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
		}
	}

	public void loadPicklist_Employment()
	{
		PL_SKLogger.writeLog("RLOSCommon", "Inside loadpicklist4:"); 
		LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) order by code");
		//LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--','' as code union select  convert(varchar,description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancntrct", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_marketcode", "select '--Select--' as description,'A50' as code union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) order by code");
	}

	public void Field_employment()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String empid="AVI,MED,EDU,HOT,PROM";
		if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("NEP")){

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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZD") ||formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZE")){
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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("TEN"))
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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg").equalsIgnoreCase("Surrogate") && empid.indexOf(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))>-1)
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
			String Query = "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and workstepname='"+formObject.getWFActivityName()+"'";
			PL_SKLogger.writeLog("RLOSCommon Load desison Drop down: ",Query );
			LoadPickList("cmplx_Decision_Decision", Query);
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

	public void loadPicklist_PartMatch()
	{
		PL_SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_PartMatch: ");
		LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}


	public void loadInDecGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String query="select FORMAT(datelastchanged,'dd-MM-yyyy hh:mm'),userName,workstepName,Decisiom,remarks,wi_nAme from ng_rlos_gr_Decision with (nolock) where wi_nAme='"+ParentWI_Name+"' or wi_nAme='"+formObject.getWFWorkitemName()+"'";
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			PL_SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
			/* try{
			 String date=list.get(0).get(0);
			 Date d=new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
			 SKLogger.writeLog("PersonnalLoanS>","value of date is:"+d.toString());*/
			for (List<String> a : list) 
			{
				/*List<String> mylist=new ArrayList<String>();
				 mylist.add(d.toString());
				 mylist.add(list.get(0).get(1));
				 mylist.add(list.get(0).get(2));
				 mylist.add(list.get(0).get(3));
				 mylist.add(list.get(0).get(4));
				 mylist.add(list.get(0).get(5));*/
				formObject.addItem("Decision_ListView1", a);
			}
		}catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadInDecGrid :"+e.getMessage());}	
	}

	public void saveIndecisionGrid(){

		PL_SKLogger.writeLog("PL_Common", "Inside saveIndecisionGrid: "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks,status, dec_wi_name) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getNGValue("cmplx_Decision_Status")+"','"+formObject.getWFWorkitemName()+"')";
		PL_SKLogger.writeLog("PL_Common","Query is"+query);
		formObject.saveDataIntoDataSource(query);

	}	


	public void  loadPicklist1()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PL_SKLogger.writeLog("PL","Inside PLCommon ->loadPicklist1()"+ActivityName);

			LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");
		}
		catch(Exception e){  PL_SKLogger.writeLog("PLCommon","Exception Occurred loadPicklist1 :"+e.getMessage());}	
	}

	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize)
	{
		PL_SKLogger.writeLog("PL","Inside populatePickListWindow");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);

		if (sControlName.equalsIgnoreCase("EMploymentDetails_Button2")) 
			objPickList.setWindowTitle("Search Employer");

		objPickList.setHeight(600);
		objPickList.setWidth(800);
		objPickList.setVisible(true);
		objPickList.setSearchEnabled(true);
		objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));
		objPickList.populateData(sQuery);
		formObject = null;
		for(int i=0;i<20;i++)
			PL_SKLogger.writeLog("PL","Column "+i+"is: "+ objPickList.toString());
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
			List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);
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

							if(tag_name.equalsIgnoreCase("AddressDetails") &&( Call_name.equalsIgnoreCase("DEDUP_SUMMARY")||Call_name.equalsIgnoreCase("BLACKLIST_DETAILS") || Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")|| Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ"))){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getCustAddress_details(Call_name);
									PL_SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							//code change for Customer update to add address tag.
							else if(tag_name.equalsIgnoreCase("ApplicationID") && Call_name.equalsIgnoreCase("NEW_LOAN_REQ")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = "<"+tag_name+">"+formObject.getWFWorkitemName().substring(6,15)+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationID:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("Host") && Call_name.equalsIgnoreCase("NEW_LOAN_REQ")){
								PL_SKLogger.writeLog("inside 1st if","inside New Loan request for host name");
								String loantype = (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0).equalsIgnoreCase("Conventional")?"RLS":"IBS");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+"<"+tag_name+">"+loantype+"</"+ tag_name+">";
								PL_SKLogger.writeLog("PL COMMON"," after adding Host name in New Loan Req:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("ApplicationNumber") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationNumber:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else if(tag_name.equalsIgnoreCase("IsNTB") && Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
								PL_SKLogger.writeLog("inside 1st if","inside NEW_CREDITCARD_REQ");
								String xml_str = int_xml.get(parent_tag);
								String ntb="N";
								if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
									ntb="Y";
								}
								xml_str = xml_str+"<"+tag_name+">"+ntb+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding NEW_CREDITCARD_REQ:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							/*	else if(tag_name.equalsIgnoreCase("ProfileSerNo") && Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
								String squery="select TEMPLATEID from ng_MASTER_CardProduct where code='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, arg2)+"'";	
								PL_SKLogger.writeLog("inside 1st if","inside NEW_CREDITCARD_REQ");
									String xml_str = int_xml.get(parent_tag);
									String ntb="N";
									if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
										ntb="Y";
									}
									xml_str = xml_str+"<"+tag_name+">"+ntb+"</"+ tag_name+">";

									PL_SKLogger.writeLog("PL COMMON"," after adding NEW_CREDITCARD_REQ:  "+xml_str);
									int_xml.put(parent_tag, xml_str);	                            	
								}*/ //for template
							else if(tag_name.equalsIgnoreCase("MaritalStatus") && Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
								PL_SKLogger.writeLog("inside 1st if","inside maritial NEW_CREDITCARD_REQ");
								String xml_str = int_xml.get(parent_tag);
								String marital="Others";
								if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("M")){
									marital="Married";
								}
								else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("S")){
									marital="Single";
								}
								else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("D")){
									marital="Divorced";
								}
								else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("W")){
									marital="Widower";
								}
								xml_str = xml_str+"<"+tag_name+">"+marital+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding maritia] NEW_CREDITCARD_REQ:  "+xml_str);
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
							//	                            else if(tag_name.equalsIgnoreCase("PhnDet") && Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
							//                                    PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
							//                                    String xml_str = int_xml.get(parent_tag);
							//                                     xml_str = xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet>";
							//                                                       
							//                                         PL_SKLogger.writeLog("PL COMMON"," SourcingDate: "+xml_str);
							//                                         int_xml.put(parent_tag, xml_str);                                    
							//                                 }
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
							else if(tag_name.equalsIgnoreCase("PhnLocalCode") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
								PL_SKLogger.writeLog("inside PL Common generate xml","PhnLocalCode to substring");
								String xml_str = int_xml.get(parent_tag);
								String phn_no = formObject.getNGValue(form_control);
								if((!phn_no.equalsIgnoreCase("")) && phn_no.indexOf("00971")>-1){
									phn_no = phn_no.substring(5);
								}

								xml_str = xml_str+"<"+tag_name+">"+phn_no+"</"+ tag_name+">";

								PL_SKLogger.writeLog("PL COMMON"," after adding ApplicationID:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							/*	else if(tag_name.equalsIgnoreCase("SourcingDate") && Call_name.equalsIgnoreCase("NEW_LOAN_REQ")){
									PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<SourcingDate>2018-04-15</SourcingDate>";

									PL_SKLogger.writeLog("PL COMMON"," SourcingDate: "+xml_str);
									int_xml.put(parent_tag, xml_str);	                            	
								}*/
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
							else if(tag_name.equalsIgnoreCase("SchemeCode") && Call_name.equalsIgnoreCase("NEW_CARD_REQ")){
								
								String xml_str = int_xml.get(parent_tag);
								String SchemeCode = (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0).equalsIgnoreCase("Conventional")?"ACNP1":"GBNP1");
								xml_str = xml_str + "<"+tag_name+">"+SchemeCode+"</"+ tag_name+">";

								PL_SKLogger.writeLog("RLOS COMMON"," after adding SchemeCode+ "+xml_str);
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
								xml_str = xml_str + "<"+tag_name+">"+"MSTANDARD-EXPAT"
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

											//added by Akshay on 3/7/2017 to remove commas from numbers
											if(form_control_val.contains(",")){
												form_control_val.replaceAll(",","");
											}
											//ended by Akshay 3/7/2017 to remove commas from numbers

											if(!data_format12.equalsIgnoreCase("text")){
												String[] format_arr = data_format12.split(":");
												PL_SKLogger.writeLog("","format_arr"+format_arr);
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
										else if(Call_name.equalsIgnoreCase("CUSTOMER_SEARCH_REQUEST")&&(tag_name.equalsIgnoreCase("PhoneValue") && parent_tag.equalsIgnoreCase("PhoneFax") && form_control_val.equalsIgnoreCase(""))){
											if(xml_str.contains("</PhoneFax>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhoneFax>"));
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
						System.out.println("final_xml: "+final_xml);
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
			System.out.println("Exception occured in main thread: "+ e.getMessage());
			return "0";
		}    
		return "";
	} 

	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuffer final_xml) 
	{
		FormContext.getCurrentInstance().getFormConfig( );

		StringBuffer strBuff=new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>"+sessionID+"</SessionId>");
		strBuff.append("<EngineName>"+cabinetName+"</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_PL_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
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
													else{
														Grid_row.add(childnode.item(child_node_len).getTextContent());
														flaga="Y";
														break;
													}
												}																							
											}

											if(flaga.equalsIgnoreCase("N") ){
												PL_SKLogger.writeLog("child_node_len ","Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												// SKLogger.writeLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getTextContent());
												Grid_row.add("NA");

											}
											flaga="N";

										}

										Grid_row.add(formObject.getWFWorkitemName());

										//code to add row in grid. and pass Grid_row in that.
										PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added in address grid: "+ Grid_row.toString());

										if(fielddbxml_tag.equalsIgnoreCase("AddrDet")){ 
											formObject.addItem("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
										}
										// added	 by yash on 10/7/2017 for getting values in worldcheck grid 
										if(fielddbxml_tag.equalsIgnoreCase("CustomerDetails")){

											formObject.addItem("cmplx_WorldCheck_WorldCheck_Grid",Grid_row);

											PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()"," Grid data to be added for World check: "+ Grid_row);
											PL_SKLogger.writeLog("of Part match grid","after WFS Save inside World Check grid");
										}
										//ended 10/07/2017 for worldcheck grid
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
																for (int child_node_len = 2 ;child_node_len< childnode.getLength();child_node_len++){
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

		formObject.setNGValue("PartMatch_CIFID",formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("PartMatch_fname",formObject.getNGValue("cmplx_Customer_FIrstNAme"));
		formObject.setNGValue("PartMatch_lname",formObject.getNGValue("cmplx_Customer_LAstNAme"));
		formObject.setNGValue("PartMatch_newpass",formObject.getNGValue("cmplx_Customer_Passport2"));
		formObject.setNGValue("PartMatch_visafno",formObject.getNGValue("cmplx_Customer_VisaNo"));
		formObject.setNGValue("PartMatch_mno1",formObject.getNGValue("cmplx_Customer_MobNo"));
		formObject.setNGValue("PartMatch_Dob",formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("PartMatch_EID",formObject.getNGValue("cmplx_Customer_EmiratesID"));
		formObject.setNGValue("PartMatch_nationality",formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("PartMatch_drno",formObject.getNGValue("cmplx_Customer_DLNo"));
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
				lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
				lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><Amount>"+Lien_amount+"</Amount>";
				lien_xml_str = lien_xml_str+ "<MaturityDate>"+Lien_maturity_date+"</MaturityDate>";
				lien_xml_str = lien_xml_str+ "</LienDetails>";
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
				/*if (Address_type.equalsIgnoreCase("HOME")){
					Address_type="Home Country";
				}--Commented on 31/7/17 by Akshay as told by Rachit*/
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
					if(preferrd.equalsIgnoreCase("Y")){
						add_xml_str = add_xml_str + "<AddressDetails><AddrType>Statement</AddrType><UseExistingAddress>"+"N"+"</UseExistingAddress><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
					}
					else{
						add_xml_str = add_xml_str + "<AddressDetails><AddrType>"+Address_type+"</AddrType><UseExistingAddress>"+"N"+"</UseExistingAddress><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
					}
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
					dOB = Convert_dateFormat(col_val[i],"yyyy-MM-dd","dd/MM/yyyy");
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
			Grid_row.add(dOB);
			Grid_row.add(Eid);
			Grid_row.add(DL);
			Grid_row.add(nationality);
			Grid_row.add(company_name);
			Grid_row.add(TL_no);
			Grid_row.add(formObject.getWFWorkitemName());
			PL_SKLogger.writeLog("PL common parse dedupe summary", Grid_row.toString());
			formObject.addItem("cmplx_PartMatch_cmplx_Partmatch_grid", Grid_row);
		}

	}

	public static String Convert_dateFormat(String date,String Old_Format,String new_Format)
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
	public void fetchIncomingDocRepeater(){

		PL_SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName = formobject.getConfigElement("ActivityName");
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"");
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

		String query = "";

		IRepeater repObj=null;
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"after creating the object for repeater");

		int repRowCount = 0;

		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");

		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,""+repObj.toString());


		// query = "SELECT distinct DocName,Mandatory,Status,Remarks, FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
		//query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='LE-0000000007308-RLOS'";
		query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
		PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT Query is: " ,query);

		docName = formObject.getNGDataFromDataCache(query);


		//docName = formObject.getNGDataFromDataCache(query);
		//        SKLogger.writeLog("Incomingdoc",""+ docName);

		try{


			repObj.setRepeaterHeaders(repeaterHeaders);

			if (sActivityName.equalsIgnoreCase("Original_Validation") )
			{
				PL_SKLogger.writeLog("Repeater Row Count to add row ov", "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);
					statusValue = docName.get(i).get(3);

					PL_SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					PL_SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
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
					repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);	          

					PL_SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					PL_SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);

					repRowCount = repObj.getRepeaterRowCount();

					PL_SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);
				}
			}
			else
			{
				PL_SKLogger.writeLog("Repeater Row Count to add row maker csm", "add row CSm maker ");


				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();

					repObj.setColumnVisible(12, false);
					repObj.setColumnVisible(13, false);
					repObj.setColumnVisible(14, false);

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);

					PL_SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					PL_SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory); 	     

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

	public void setFragments_Top(String fragmentList) 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("PL","Inside setFragments_Top: Fragments List is:"+fragmentList);
		String Fragment_arr[]=fragmentList.split(",");
		if(Fragment_arr.length==0 || Fragment_arr.length==1){
			return;
		}

		else if(Fragment_arr.length==2){
			formObject.setTop(Fragment_arr[0], formObject.getTop(Fragment_arr[1])+formObject.getHeight(Fragment_arr[0])+10);
			return;
		}
		else{	
			for(int i=0;i<Fragment_arr.length-2;i++)
			{
				formObject.setTop(Fragment_arr[i], formObject.getTop(Fragment_arr[i+1])+formObject.getHeight(Fragment_arr[i])+10);
				if(formObject.getNGFrameState(Fragment_arr[i+1])==0){
					formObject.setTop(Fragment_arr[i+2], formObject.getTop(Fragment_arr[i+1])+formObject.getHeight(Fragment_arr[i+1])+10);
				}
				else{
					formObject.setTop(Fragment_arr[i+2], formObject.getTop(Fragment_arr[i+1])+30);
				}
				
			}
		}
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
		String Existingcust_NTB = formObject.getNGValue("cmplx_Customer_NTB");
		String Existingcust_NEP = formObject.getNGValue("cmplx_Customer_NEP");

		PL_SKLogger.writeLog("PL DDVT Vhecker", "EXISTING CUST : "+ Existingcust_NTB);
		if(!(Existingcust_NTB.equalsIgnoreCase("true") || Existingcust_NEP.equalsIgnoreCase("true"))){			
			cif_verf_status = "Y";
			PL_SKLogger.writeLog("PL DDVT Checker", "cif_verf_status set to Y ");

		}
		String Cif_lock_status = formObject.getNGValue("Is_CustLock");
		//String Cif_unlock_status = formObject.getNGValue("is_cust_verified");
		PL_SKLogger.writeLog("PL DDVT Vhecker", "cif_verf_status : "+ cif_verf_status);
		PL_SKLogger.writeLog("PL DDVT Vhecker", "cif_Lock_status : "+ Cif_lock_status);
		if (cif_verf_status.equalsIgnoreCase("")||cif_verf_status.equalsIgnoreCase("N") || cif_verf_status==null || cif_verf_status.equals("NULL")){
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
						alert_msg = "Customer Updation Successful!";
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

	//ended here for fetchingDocRepeater


	public String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;

	}
}

