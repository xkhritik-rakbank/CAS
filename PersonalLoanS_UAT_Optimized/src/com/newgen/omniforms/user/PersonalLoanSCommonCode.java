//----------------------------------------------------------------------------------------------------
				//--Historty--
//Deepak Code changes to calculate LPF amount and % 08-nov-2017 start
//----------------------------------------------------------------------------------------------------


package com.newgen.omniforms.user;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;




public class PersonalLoanSCommonCode extends PLCommon implements Serializable{

	private static final long serialVersionUID = 1L;
	Logger mLogger=PersonalLoanS.mLogger;
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Expand fragments on click   
	 
	***********************************************************************************  */

	public void FrameExpandEvent(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("PersonalLoanS---Inside PLCommoncode-->FrameExpandEvent()");
		PersonalLoanS.mLogger.info("PersonalLoanS---Inside CUSTOME!!!!!!!!!!!!!!!!!!!!!!!");
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap

		

		if ("EmploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			hm.put("EmploymentDetails","Clicked");
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");

			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	

		else if ("IncomeDEtails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("IncomeDEtails","Clicked");
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			income_Dectech();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	

		else if ("InternalExternalLiability".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("InternalExternalLiability","Clicked");

			formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
			String App_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			if (App_Type != "RESCH"){
				formObject.setVisible("Liability_New_Label6", false);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
			}
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");

			String activity = formObject.getWFActivityName();
			if("CSM".equalsIgnoreCase(activity)){
				//empty  if
				
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	


		else if ("MiscFields".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("MiscFields","Clicked");

			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("EligibilityAndProductInformation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("EligibilityAndProductInformation","Clicked");
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
			//query added by saurabh on 22nd Oct for loading base rate type.
			LoadPickList("cmplx_EligibilityAndProductInfo_BaseRateType", "select distinct PRIME_TYPE from NG_master_Scheme order by PRIME_TYPE");
			
			if("CSM".equalsIgnoreCase(formObject.getWFActivityName())){
				setLoanFieldsVisible();
			}
			Eligibilityfields();

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	


		else if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Address_Details_container","Clicked");


			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			loadPicklist_Address();

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	


		else if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Alt_Contact_container","Clicked");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			
			LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,1000 as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code desc ");
			String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ReferenceDetails","Clicked");


			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("FATCA","Clicked");

			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
			LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
			String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
			
			if("O".equalsIgnoreCase(usRelation)){
				//changed from setenabled to setlocked by saurabh on 11 nov 17.
				formObject.setLocked("FATCA_Frame6", true);
				formObject.setEnabled("cmplx_FATCA_USRelation",true);
				formObject.setEnabled("FATCA_Save", true);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("KYC","Clicked");

			formObject.fetchFragment("KYC", "KYC", "q_KYC");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("OECD","Clicked");

			formObject.fetchFragment("OECD", "OECD", "q_OECD");
			loadPickListOECD();


			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LoanDetails","Clicked");
			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");

			formObject.setNGValue("LoanDetails_winame", formObject.getWFWorkitemName());
			loadPicklist_LoanDetails();
			//if condition added by saurabh on 16th Oct.
			if(formObject.isVisible("ELigibiltyAndProductInfo_Frame1")){
				loanvalidate();//modified by akshay on 14/10/17 for displaying values in loan details
			}
			//else condition added by saurabh to auto fetch EligProdInfo.
			else{
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
				
				if("CSM".equalsIgnoreCase(formObject.getWFActivityName())){
					setLoanFieldsVisible();
				}
				Eligibilityfields();
				loanvalidate();//modified by akshay on 14/10/17 for displaying values in loan details
				//++below code added by nikhil to set loan details alignment
				formObject.setHeight("LoanDetails", 450);
				formObject.setTop("Risk_Rating",470);
			
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
					hm.clear();
				}
		}

		else if ("Self_Employed".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Self_Employed","Clicked");

			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("DecisionHistory","Clicked");

			try{
				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
				//below code added by Arun (27/11/17) to fetch the fragments when Decision fragment is fetched.
				int framestate2=formObject.getNGFrameState("Address_Details_container");
				if(framestate2 == 0){
					PersonalLoanS.mLogger.info(" Address_Details_container");
				}
				else {
					formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");				
					formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_ontainer")+30);
					//formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
					//formObject.setTop("CardDetails", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
					formObject.setTop("FATCA", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails")+30);
					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
					
					/*
					formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
					formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
					formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
					formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
					formObject.setTop("KYC", formObject.getTop("FATCA")+20);
					formObject.setTop("OECD", formObject.getTop("KYC")+20);
					*/
					PersonalLoanS.mLogger.info(" fetched address details");
				}
				
				int framestate3=formObject.getNGFrameState("Alt_Contact_container");
				if(framestate3 == 0){
					PersonalLoanS.mLogger.info("Alt_Contact_container");
				}
				else {
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")+30);
					//formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
					//formObject.setTop("CardDetails", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
					formObject.setTop("FATCA", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails")+30);
					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
					
					/*
					formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")+70);
					formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
					formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+20);
					formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
					formObject.setTop("KYC", formObject.getTop("FATCA")+20);
					formObject.setTop("OECD", formObject.getTop("KYC")+20);
					*/
					PersonalLoanS.mLogger.info("fetched address details");
				}
				
				int framestate4=formObject.getNGFrameState("FATCA");
				if(framestate4 == 0){
					PersonalLoanS.mLogger.info("FATCA");
				}
				else {
					formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
					formObject.setTop("FATCA", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
					/*
					formObject.setTop("FATCA", formObject.getHeight("CardDetails")+formObject.getTop("CardDetails")+30);
					formObject.setTop("KYC", formObject.getHeight("FATCA")+formObject.getTop("FATCA")+20);
					formObject.setTop("OECD", formObject.getHeight("KYC")+formObject.getTop("KYC")+20);
					*/
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
					/*
					formObject.setTop("KYC", formObject.getTop("FATCA")+30);
					formObject.setTop("OECD", formObject.getTop("KYC")+20);
					*/
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
				//above code added by Arun (27/11/17) to fetch the fragments when Decision fragment is fetched.
				formObject.setVisible("cmplx_Decision_Manual_deviation_reason", false);
				loadInDecGrid();
				loadPicklist3();
				if(!"Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"REFER TO SOURCE".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
					{
					formObject.setLocked("DecisionHistory_DecisionReasonCode",true);
					}
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
					formObject.setLocked("DecisionHistory_Button6",false);
					formObject.setEnabled("cmplx_Decision_CADDecisiontray", false);        
				}
				else{
					formObject.setLocked("DecisionHistory_Button6",true);
					formObject.setEnabled("cmplx_Decision_CADDecisiontray", true);         
				}
				//Code Change By aman to save Highest Delegation Auth
				String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
				PersonalLoanS.mLogger.info("sQuery list size"+sQuery);
				//PersonalLoanS.mLogger.info("cmplx_DEC_HighDeligatinAuth list size"+OutputXML.size(), "value is "+OutputXML.get(0).get(0));
				//PersonalLoanS.mLogger.info("cmplx_DEC_HighDeligatinAuth list size"+OutputXML.size(), "value is ");
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
			if	("Y".equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")) && "Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Create")))
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
   				/*formObject.setVisible("DecisionHistory_Label10", false);
   				formObject.setVisible("cmplx_DEC_New_CIFID", false);*/
   				
   			
   				
				
					String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
					boolean flag = false;
					/*StringBuilder values = new StringBuilder();
			        values.append(",'" + sProcessName + "'");*/

					String listValues ="";
					List<List<String>> list=formObject.getDataFromDataSource(sQuery);
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
					List<List<String>> list1=formObject.getDataFromDataSource(sQuery);
					PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "cpv dec value is::"+list1 );
					listValues +="#"+list1.get(0).get(0);
					PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "list value is::"+listValues );
					formObject.setNGValue("DecisionHistory_CadTempField",listValues);
					
				
			
                  }
			 
			 else if(formObject.getWFActivityName().equalsIgnoreCase("Smart_CPV")){
          	   
				 formObject.setVisible("cmplx_Decision_IBAN", false);
				 formObject.setVisible("cmplx_Decision_desc", false);
				 formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED", false);
				 formObject.setVisible("Decision_Label1", false);
				 formObject.setVisible("cmplx_Decision_waiveoffver", false);
				 formObject.setVisible("cmplx_Decision_refereason", false);
				 formObject.setVisible("cmplx_Decision_AccountNo", false);
				 formObject.setVisible("cmplx_Decision_strength", false);
				 formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
				 formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
				 formObject.setVisible("cmplx_Decision_weakness", false);
				 formObject.setVisible("DecisionHistory_DecisionReasonCode", true);
				 formObject.setVisible("cmplx_Decision_NoofAttempts", true);
				
				 
          	   formObject.setTop("Decision_Label3", 10);
                	formObject.setTop("cmplx_Decision_Decision", formObject.getHeight("Decision_Label3")+10);
                	formObject.setTop("DecisionHistory_Label11", 10);
                	formObject.setTop("DecisionHistory_DecisionReasonCode", formObject.getHeight("DecisionHistory_Label11")+10);
                	formObject.setTop("DecisionHistory_Label12", 10);
                	formObject.setTop("cmplx_Decision_NoofAttempts", formObject.getHeight("DecisionHistory_Label12")+10);
                	formObject.setTop("Decision_Label4", formObject.getHeight("cmplx_Decision_Decision")+formObject.getTop("cmplx_Decision_Decision")+20);
                	formObject.setTop("cmplx_Decision_REMARKS", formObject.getHeight("DecisionHistory_Label12")+formObject.getTop("Decision_Label4"));
                	formObject.setTop("DecisionHistory_nonContactable", formObject.getTop("cmplx_Decision_REMARKS"));
                	formObject.setTop("DecisionHistory_cntctEstablished", formObject.getTop("cmplx_Decision_REMARKS"));
                	formObject.setLeft("Decision_Label3", 10);
                	formObject.setLeft("cmplx_Decision_Decision", 10);
                	formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("Decision_Label3")+275);
                	formObject.setLeft("DecisionHistory_DecisionReasonCode", formObject.getLeft("Decision_Label3")+275);
                	formObject.setLeft("DecisionHistory_Label12", formObject.getLeft("DecisionHistory_Label11")+275);
                	formObject.setLeft("cmplx_Decision_NoofAttempts", formObject.getLeft("DecisionHistory_Label11")+275);
                	formObject.setLeft("Decision_Label4", 10);
                	formObject.setLeft("cmplx_Decision_REMARKS", 10);
                	formObject.setLeft("DecisionHistory_nonContactable",formObject.getLeft("cmplx_Decision_REMARKS")+275);
                	formObject.setLeft("DecisionHistory_cntctEstablished",formObject.getLeft("DecisionHistory_nonContactable")+275);
                	formObject.setTop("Decision_ListView1", formObject.getTop("cmplx_Decision_REMARKS")+formObject.getHeight("cmplx_Decision_REMARKS")+10);
               // 	formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+20);
                	formObject.setTop("DecisionHistory_save", formObject.getTop("Decision_ListView1")+200);
                	formObject.setVisible("DecisionHistory_save", true);
                //	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+formObject.getHeight("DecisionHistory_save")+50);
                	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("Decision_ListView1")+300);
                	formObject.setHeight("DecisionHistory", formObject.getTop("Decision_ListView1")+310);
                	
               
                	formObject.setNGValue("cmplx_DEC_Decision_Reasoncode", "--Select--");
           
          	   
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
         				/*formObject.setNGFrameState("Office_verification", 0);
        				formObject.setNGFrameState("Smart_check", 0);
        				formObject.setNGFrameState("Notepad_Values", 0);*/
              		formObject.setEnabled("DecisionHistory_nonContactable", false);
         				formObject.setEnabled("NotepadDetails_Frame1", false);
         				formObject.setEnabled("DecisionHistory_Frame1", false);
         				formObject.setEnabled("SmartCheck_Frame1", false);
         				formObject.setEnabled("OfficeandMobileVerification_Frame1", false);
         				
         				formObject.setEnabled("DecisionHistory_cntctEstablished", true);
         				
         				formObject.setNGValue("cmplx_Decision_Decision","Smart CPV Hold");
         				
         				formObject.setNGFrameState("Office_verification", 1);
         				formObject.setNGFrameState("Smart_check", 1);
         				formObject.setNGFrameState("Notepad_Values", 1);
              	}else{
              		
              		
         				formObject.setEnabled("DecisionHistory_Frame1", true);
         				formObject.setEnabled("DecisionHistory_cntctEstablished", false);
         				formObject.setEnabled("DecisionHistory_nonContactable", true);
         				formObject.setNGValue("cmplx_Decision_Decision","--Select--");
         				
         				
              	}
      	          	
             }
			 
			 else if (formObject.getWFActivityName().equalsIgnoreCase("CPV")){
				//++ below code added by abhishek to hide fields as per FSD 2.7.3
         		formObject.setVisible("DecisionHistory_Label6", false);
         		formObject.setVisible("cmplx_Decision_IBAN", false);
         		formObject.setVisible("DecisionHistory_Label7", false);
         		formObject.setVisible("cmplx_Decision_AccountNo", false);
         		formObject.setVisible("DecisionHistory_Label8", false);
         		formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
         		formObject.setVisible("DecisionHistory_Label9", false);
         		formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
         		
         		// to set alignment
         		
         		formObject.setTop("DecisionHistory_Decision_Label3", 10);
        		formObject.setLeft("DecisionHistory_Decision_Label3", 25);
        		formObject.setTop("cmplx_DEC_Decision", formObject.getTop("DecisionHistory_Decision_Label3")+formObject.getHeight("DecisionHistory_Decision_Label3"));
        		formObject.setLeft("cmplx_DEC_Decision", 25);

        		formObject.setTop("DecisionHistory_Label11", 10);
        		formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
        		formObject.setTop("cmplx_DEC_Decision_Reasoncode",formObject.getTop("DecisionHistory_Label11")+formObject.getHeight("DecisionHistory_Label11"));
        		formObject.setLeft("cmplx_DEC_Decision_Reasoncode",formObject.getLeft("DecisionHistory_Decision_Label3")+275);

        		formObject.setTop("DecisionHistory_Label12",10);
        		formObject.setLeft("DecisionHistory_Label12",formObject.getLeft("DecisionHistory_Label11")+275);
        		formObject.setTop("cmplx_DEC_NoofAttempts", formObject.getTop("DecisionHistory_Label12")+formObject.getHeight("DecisionHistory_Label12"));
        		formObject.setLeft("cmplx_DEC_NoofAttempts", formObject.getLeft("DecisionHistory_Label11")+275);

        		formObject.setTop("Decision_Label4",formObject.getTop("DecisionHistory_Label12")+60);
        		formObject.setLeft("Decision_Label4",25);
        		formObject.setTop("cmplx_Decision_REMARKS", formObject.getTop("Decision_Label4")+formObject.getHeight("Decision_Label4"));
        		formObject.setLeft("cmplx_Decision_REMARKS",25);

        		formObject.setTop("Decision_ListView1", formObject.getTop("Decision_Label4") +80 );
        		formObject.setLeft("Decision_ListView1", 25);

        		formObject.setVisible("DecisionHistory_save", true);
        		formObject.setTop("DecisionHistory_save", formObject.getTop("Decision_ListView1")+formObject.getHeight("Decision_ListView1")+20);
        		formObject.setLeft("DecisionHistory_save", 25);

        		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("Decision_ListView1") +300);
        		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1") +5);
     		//-- Above code added by abhishek to hide fields  as per FSD 2.7.3
                 }
			 
			//-- Above code added by abhishek to check workitem on CPV as per FSD 2.7.3
			 
			 else if (formObject.getWFActivityName().equalsIgnoreCase("FCU")){
				
				 PersonalLoanS.mLogger.info(" In PL_FCU eventDispatched()"+ "Activityname:" + formObject.getWFActivityName());
				
             	
             	formObject.setVisible("cmplx_Decision_waiveoffver", false);
             	formObject.setVisible("Decision_Label1", false);
             	formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED", false);
             	formObject.setVisible("DecisionHistory_chqbook", false);
             	formObject.setVisible("DecisionHistory_Label1", false);
             	formObject.setVisible("DecisionHistory_ReferTo", false); //Arun (01-12-17) to hide this combo
             	formObject.setVisible("cmplx_Decision_refereason", false);
             	formObject.setVisible("DecisionHistory_Label6",false);
             	formObject.setVisible("cmplx_Decision_IBAN",false);
             	formObject.setVisible("DecisionHistory_Label7",false);
             	formObject.setVisible("cmplx_Decision_AccountNo",false);
             	formObject.setVisible("DecisionHistory_Label8",false);
             	formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
             	formObject.setVisible("DecisionHistory_Label9",false);
             	formObject.setVisible("cmplx_Decision_DebitcardNumber",false);			                	
             	formObject.setVisible("DecisionHistory_Label5",false);
             	formObject.setVisible("cmplx_Decision_desc",false);
             	formObject.setVisible("DecisionHistory_Label3",false);
             	formObject.setVisible("cmplx_Decision_strength",false);
             	formObject.setVisible("DecisionHistory_Label4",false);
             	formObject.setVisible("cmplx_Decision_weakness",false);
             	formObject.setVisible("DecisionHistory_Button5",false);//Arun
             	formObject.setVisible("DecisionHistory_Button6",false);//Arun
             	
             	formObject.setVisible("DecisionHistory_Label19",true);
             	formObject.setVisible("cmplx_Decision_ALOCcompany",true);
             	formObject.setVisible("DecisionHistory_Label20",true);
             	formObject.setVisible("cmplx_Decision_ALOCType",true);
             	formObject.setVisible("DecisionHistory_Label21",true);
             	formObject.setVisible("cmplx_Decision_Referralreason",true);
             	formObject.setVisible("DecisionHistory_Label22",true);
             	formObject.setVisible("cmplx_Decision_Referralsubreason",true);
             	formObject.setVisible("DecisionHistory_Label23",true);
             	formObject.setVisible("cmplx_Decision_feedbackstatus",true);
             	formObject.setVisible("DecisionHistory_Label24",true);
             	formObject.setVisible("cmplx_Decision_subfeedback",true);
             	formObject.setVisible("DecisionHistory_Label25",true);
             	formObject.setVisible("cmplx_Decision_Furtherreview",true);	
             	
            
             	
             	
     			
     			formObject.setHeight("DecisionHistory_Frame1",446);
     			
				formObject.setTop("DecisionHistory_Label19",10);
    			formObject.setTop("cmplx_Decision_ALOCcompany",25);
    			formObject.setTop("DecisionHistory_Label20",10);
    			formObject.setTop("cmplx_Decision_ALOCtype",25);
    			formObject.setTop("DecisionHistory_Label21", 10);
    			formObject.setTop("cmplx_Decision_Referralreason", 25);
    		
    			formObject.setTop("DecisionHistory_Label22",10);
    			formObject.setTop("cmplx_Decision_Referralsubreason",25);
    			formObject.setTop("DecisionHistory_Label23",60);
    			formObject.setTop("cmplx_Decision_Feedbackstatus",76);			        			
    			formObject.setTop("DecisionHistory_Label24",60);
    			formObject.setTop("cmplx_Decision_subfeedback",76);
    			formObject.setTop("DecisionHistory_Label25",60);
    			formObject.setTop("cmplx_Decision_Furtherreview",76);
    			formObject.setLeft("Decision_Label4",776);
    			formObject.setLeft("cmplx_Decision_REMARKS",776);
    			formObject.setTop("Decision_Label3",60);
    			formObject.setTop("cmplx_Decision_Decision",76);
    			formObject.setTop("Decision_Label4",60);
    			formObject.setTop("cmplx_Decision_REMARKS",76);
    			formObject.setTop("DecisionHistory_Label25",110);
    			formObject.setTop("cmplx_Decision_Furtherreview",126);
    			formObject.setLeft("DecisionHistory_Label25",23);
    			formObject.setLeft("cmplx_Decision_Furtherreview",23);
    			formObject.setLeft("DecisionHistory_Label23",23);
    			formObject.setLeft("cmplx_Decision_Feedbackstatus",23);	
    			formObject.setLeft("Decision_Label3",528);
    			formObject.setLeft("cmplx_Decision_Decision",528);
    			formObject.setLeft("DecisionHistory_Label24",272);
    			formObject.setLeft("cmplx_Decision_subfeedback",272);				        			
    			
    			formObject.setTop("Decision_ListView1",280);
    			formObject.setTop("Decision_Label4", formObject.getTop("Decision_Label3"));
    			formObject.setLeft("Decision_Label4", formObject.getLeft("DecisionHistory_Label22"));
    			formObject.setTop("cmplx_Decision_REMARKS", formObject.getTop("cmplx_Decision_Decision"));
    			formObject.setLeft("cmplx_Decision_REMARKS", formObject.getLeft("cmplx_Decision_Referralsubreason"));
    			
    			formObject.setTop("DecisionHistory_save",225);	
				formObject.setLeft("DecisionHistory_save", 23);
				LoadPickList("cmplx_Decision_Referralreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
     			LoadPickList("cmplx_Decision_Referralsubreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");
     			PersonalLoanS.mLogger.info(" In PL_FCU eventDispatched()"+ "After aligmnment code");
     			//++below code added to solve fcu issue
     			formObject.setVisible("DecisionHistory_ReferTo", false);
     			formObject.setVisible("Decision_Label4", true);
     			formObject.setTop("DecisionHistory_dec_reason_code", 76);
     			formObject.setLeft("DecisionHistory_dec_reason_code", 776);
     			formObject.setTop("DecisionHistory_Label11", 91);
     			formObject.setLeft("DecisionHistory_Label11", 776);
     			formObject.setTop("cmplx_DEC_Remarks", 76);
     			formObject.setLeft("cmplx_DEC_Remarks", 1001);
     			formObject.setTop("Decision_Label4", 91);
     			formObject.setLeft("Decision_Label4", 1001);
     			
     			//--above code added to solve fcu issue
     			

			 }
			//--Above code added by nikhil 13/11/2017 for Code merge
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Inc_Doc".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Inc_Doc","Clicked");


			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			//commented by saurabh on 11th nov 17.
			
			String activName = formObject.getWFActivityName();
			if("DDVT_Checker".equalsIgnoreCase(activName)){
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
					formObject.setVisible("IncomingDoc_UploadSig",true);
				else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDoc_ViewSIgnature",false);
					formObject.setVisible("IncomingDoc_UploadSig",true);
				
				}
			}
			else if("DDVT_Maker".equalsIgnoreCase(activName)){
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDoc_ViewSIgnature",true);
					formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
				}
				else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
					
				}
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}
		//Below code added by nikhil 13/11/2017 for Code merge
		
		else if ("Post_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("Post_Disbursal","Clicked");



			PersonalLoanS.mLogger.info("Inside PostDisbursal-->FrameExpandEvent()");

			formObject.fetchFragment("Post_Disbursal", "PostDisbursal", "q_PostDisbursal");
			formObject.setNGValue("PostDisbursal_Winame", formObject.getWFWorkitemName());
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}
		//++Below code added by  Yash on  2/11/17 as per CC FSD 2.7
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Dispatch_Details")) 
		{
			hm.put("Dispatch_Details","Clicked");
			
			//popupFlag="N";
			
			PersonalLoanS.mLogger.info("PersonalLoanS"+"Inside PostDisbursal-->FrameExpandEvent()");
			
			formObject.fetchFragment("Dispatch_Details", "DispatchFrag", "q_Dispatch");
			
			LoadPickList("DispatchFrag_Transtype", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_TransactionType with (nolock)");
			LoadPickList("DispatchFrag_Emirates", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_Emirate with (nolock)");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Postdisbursal_Checklist")) 
		{
			hm.put("Postdisbursal_Checklist","Clicked");
			
		
			
			//PL_SKLogger.writeLog("PersonalLoanS","Inside PostDisbursal-->FrameExpandEvent()");
			
			formObject.fetchFragment("Postdisbursal_Checklist", "PostDisbursal_Checklist", "q_PostDisbursal_Checklist");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		//++Above code added by  Yash on  2/11/17 as per CC FSD 2.7
		else if ("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Loan_Disbursal","Clicked");


			formObject.fetchFragment("Loan_Disbursal", "Loan_Disbursal", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("CC_Creation","Clicked");


			formObject.fetchFragment("CC_Creation", "CC_Creation", "q_FinIncident");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Limit_Inc".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Limit_Inc","Clicked");


			formObject.fetchFragment("Limit_Inc", "Limit_Inc", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Cust_Det_Ver".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Cust_Det_Ver","Clicked");


			formObject.fetchFragment("Cust_Det_Ver", "CustDetailVerification1", "q_CustDetailVeriFCU");
			//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
			int framestate0=formObject.getNGFrameState("Address_Details_container");
			if(framestate0 != 0){
				formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
				//formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")); 
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+25);
				//formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
				formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+25);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
			}
			int framestate1=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate1 != 0){
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				//formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+20);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
			}
			autopopulateValues(formObject);
			//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
			formObject.setVisible("Credit_card_Enq", false);
			formObject.setVisible("Case_History", false);
			formObject.setVisible("LOS", false);

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Business_Verif".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Business_Verif","Clicked");


			formObject.fetchFragment("Business_Verif", "BussinessVerification1", "q_bussverification1");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Emp_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Emp_Verification","Clicked");


			formObject.fetchFragment("Emp_Verification", "EmploymentVerification", "q_EmpVerification");
			//Below code added by nikhil 13/11/2017 for Code merge
			//below code commented by nikhil 31/10/17
			//EmpVervalues();
			//Above code added by nikhil 13/11/2017 for Code merge
			//LoadPickList("cmplx_EmploymentVerification_OffTelnoValidatedfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Bank_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Bank_Check","Clicked");


			formObject.fetchFragment("Bank_Check", "BankingCheck", "q_BankingCheck");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Note_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Note_Details","Clicked");

			PersonalLoanS.mLogger.info("inside NOte_Details frame expanded event");  			
			formObject.fetchFragment("Note_Details", "NotepadDetailsFCU", "q_Note");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Supervisor_section".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Supervisor_section","Clicked");


			formObject.fetchFragment("Supervisor_section", "supervisorsection", "q_Supervisor");
			LoadPickList("cmplx_FcuDecision_ReferralReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
			LoadPickList("cmplx_FcuDecision_ReferralSubReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		} 

		else if ("Smart_chk".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_chk","Clicked");


			formObject.fetchFragment("Smart_chk", "SmartCheck1", "q_SmartCheckFCU");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Part_Match".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Part_Match","Clicked");


			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			//added by saurabh on 10th July 17 for DDVT_MAKER.
			partMatchValues();
			LoadPickList("PartMatch_nationality", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FinacleCRM_Incidents".equalsIgnoreCase(pEvent.getSource().getName())){					
			hm.put("FinacleCRM_Incidents","Clicked");

			formObject.fetchFragment("FinacleCRM_Incidents", "FinacleCRMIncident", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FinacleCRM_CustInfo".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("FinacleCRM_CustInfo","Clicked");

			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Finacle_Core".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Finacle_Core","Clicked");
			formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_Finaclecore");
			loadPicklistDDSReturn();
			try{
				String query="select AcctType,'NA',AcctId,AcctNm,AccountOpenDate,AcctStat,'NA',AvailableBalance,'NA','NA','NA' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					PersonalLoanS.mLogger.info( "Data to be added in cmplx_FinacleCore_FinaclecoreGrid Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
				}

				query="select AcctId,LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				//changed ended
				List<List<String>> list_lien=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_lien)
				{
					PersonalLoanS.mLogger.info( "Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
				}
				//changed here in this query
				query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
				//changed ended

				for(List<String> mylist : list_SIDet)
				{
					PersonalLoanS.mLogger.info("Data to be added in Grid: "+mylist.toString());

					formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
				}
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception while setting data in grid:"+e.getMessage()+" "+e);
				throw new ValidatorException(new FacesMessage("Error while setting data in account grid"));
			}

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}


		}

		else if ("MOL".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("MOL","Clicked");

			formObject.fetchFragment("MOL", "MOL1", "q_MOL");
			loadPicklistMol();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("World_Check".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("World_Check","Clicked");

			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
			loadPicklist_WorldCheck();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Reject_Enq".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Reject_Enq","Clicked");

			formObject.fetchFragment("Reject_Enq", "RejectEnq", "q_RejectEnq");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Sal_Enq".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Sal_Enq","Clicked");

			formObject.fetchFragment("Sal_Enq", "SalaryEnq", "q_SalaryEnq");
			try{
				String query="select SalCreditDate,SalCreditMonth,AcctId,SalAmount from ng_rlos_FinancialSummary_SalTxnDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					PersonalLoanS.mLogger.info( "Data to be added in account Grid account Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_SalaryEnq_SalGrid", mylist);
				}

			}
			finally{
				hm.clear();
			}

		}				


		

		else if ("Dec".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Dec","Clicked");


			formObject.fetchFragment("Dec", "CAD_Decision", "q_CadDecision");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ReferHistory","Clicked");


			formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Cust_Detail_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Cust_Detail_verification","Clicked");

			formObject.fetchFragment("Cust_Detail_verification", "CustDetailVerification", "q_CustDetVer");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");

			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

			custdetvalues();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Business_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Business_verification","Clicked");


			formObject.fetchFragment("Business_verification", "BussinessVerification", "q_businessverification");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Home_country_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Home_country_verification","Clicked");


			formObject.fetchFragment("Home_country_verification", "HomeCountryVerification", "q_HomeCountryVeri");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_HCountryVerification_Hcountrytelverified");
			LoadPicklistVerification(LoadPicklist_Verification);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Residence_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Residence_verification","Clicked");


			formObject.fetchFragment("Residence_verification", "ResidenceVerification", "q_ResiVerification");
			LoadPickList("cmplx_ResiVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");


			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Guarantor_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Guarantor_verification","Clicked");


			formObject.fetchFragment("Guarantor_verification", "GuarantorVerification", "q_GuarantorVer");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_GuarantorVerification_verdoneonmob");
			LoadPicklistVerification(LoadPicklist_Verification);

			LoadPickList("cmplx_GuarantorVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Reference_detail_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Reference_detail_verification","Clicked");


			formObject.fetchFragment("Reference_detail_verification", "ReferenceDetailVerification", "q_RefDetVer");
			LoadPickList("cmplx_RefDetVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Office_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Office_verification","Clicked");

			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
				formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
				List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_hrdemailverified","cmplx_OffVerification_colleaguenoverified","cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
				LoadPicklistVerification(LoadPicklist_Verification);
				OfficeVervalues();
				LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

				LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");

				//below code added by abhishek to check for enable/disable of offverification frag as per FSD 2.7.3
				if("Smart_CPV".equalsIgnoreCase(formObject.getWFActivityName())){


					String EnableFlagValue = formObject.getNGValue("cmplx_OffVerification_EnableFlag");
					String sQuery =" SELECT ProcessInstanceID,lockStatus FROM WFINSTRUMENTTABLE WHERE activityname = 'CPV' AND ProcessInstanceID ='"+formObject.getWFWorkitemName()+"'";
					PersonalLoanS.mLogger.info("Ccinside office verification load in smart cpv workstep-->query is ::"+sQuery);
					List<List<String>> list=formObject.getDataFromDataSource(sQuery);
					PersonalLoanS.mLogger.info("Ccinside office verification load in smart cpv workstep-->list is ::"+list+"size is::"+list.size());

					if(list.isEmpty()){

						formObject.setLocked("OfficeandMobileVerification_Frame1",true);
					}
					else{
						String lockStatus = list.get(0).get(1);
						if("Y".equalsIgnoreCase(lockStatus)){
							if("true".equalsIgnoreCase(EnableFlagValue)){
								formObject.setLocked("OfficeandMobileVerification_Frame1",true);
							}else{
								formObject.setLocked("OfficeandMobileVerification_Frame1",false);
								formObject.setEnabled("OfficeandMobileVerification_Enable", false);
								formObject.setEnabled("cmplx_OffVerification_offtelno",false);
								formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
								formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
								formObject.setEnabled("cmplx_OffVerification_desig_val",false);
								formObject.setEnabled("cmplx_OffVerification_doj_val",false);
								formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);

							}
						}else{
							formObject.setLocked("OfficeandMobileVerification_Frame1",false);
							formObject.setEnabled("OfficeandMobileVerification_Enable", false);
							formObject.setEnabled("cmplx_OffVerification_offtelno",false);
							formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
							formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
							formObject.setEnabled("cmplx_OffVerification_desig_val",false);
							formObject.setEnabled("cmplx_OffVerification_doj_val",false);
							formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);
						}
					}


				}
			}else{
				formObject.setEnabled("OfficeandMobileVerification_Frame1", false);
			}

			//-- Above code added by abhishek to check for enable/disable of offverification frag as per FSD 2.7.3
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Loan_card_details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Loan_card_details","Clicked");


			formObject.fetchFragment("Loan_card_details", "LoanandCard", "q_LoanandCard");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_LoanandCard_loanamt_ver","cmplx_LoanandCard_tenor_ver","cmplx_LoanandCard_emi_ver","cmplx_LoanandCard_islorconv_ver","cmplx_LoanandCard_firstrepaydate_ver","cmplx_LoanandCard_cardtype_ver","cmplx_LoanandCard_cardlimit_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_LoanandCard_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

			loancardvalues();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Notepad_details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_details","Clicked");


			formObject.fetchFragment("Notepad_details", "NotepadDetails", "q_Note");
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Smart_check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_check","Clicked");

			// below code added by abhishek to load smartcheck and check for button flag as per FSD 2.7.3
			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");

			}
			else{
				formObject.setEnabled("SmartCheck_Frame1", false);
			}
			//below code by saurabh on 10th nov 2017.
			String activName = formObject.getWFActivityName();
			if("CAD_Analyst1".equalsIgnoreCase(activName)){
				formObject.setLocked("SmartCheck_CPVrem",true);
			}
			else if("CPV".equalsIgnoreCase(activName)){
				formObject.setLocked("SmartCheck_creditrem",true);
			}
			//-- Above code added by abhishek to load smartcheck and check for button flag as per FSD 2.7.3
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Orig_validation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Orig_validation","Clicked");


			formObject.fetchFragment("Orig_validation", "OriginalValidation", "q_OrigVal");
			fetchoriginalDocRepeater();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		//New fragment added by Arun on 17/7/17 for new DDVT changes on screen by shashank 
		else if ("Risk_Rating".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Risk_Rating","Clicked");


			formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
			loadPicklistRiskRating(); //Added by aman to load the picklist of risk rating
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Notepad_Values".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Values","Clicked");

			fetch_NotepadDetails();
		}

		else if ("Card_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Card_Details","Clicked");


			formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Credit_card_Enq".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Credit_card_Enq","Clicked");


			formObject.fetchFragment("Credit_card_Enq", "CreditCardEnq", "q_CCEnq");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Case_History".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Case_History","Clicked");


			formObject.fetchFragment("Case_History", "CaseHistoryReport", "q_CaseHist");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("LOS".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LOS","Clicked");


			formObject.fetchFragment("LOS", "LOS", "q_LOS");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("External_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("External_Blacklist","Clicked");


			formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Compliance","Clicked");


			formObject.fetchFragment("Compliance", "Compliance", "q_compliance");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         :Function to be called on change Event    
	 
	***********************************************************************************  */

	public void value_Change(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		if ("cmplx_Decision_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("REJECT")  || "REFER".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setLocked("DecisionHistory_DecisionReasonCode", false);
				LoadPickList("DecisionHistory_DecisionReasonCode", "SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason where workstep='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' order by code");
			}
			 if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("REFER"))
		        {
				formObject.setLocked("DecisionHistory_ReferTo", false);
				LoadPickList("DecisionHistory_ReferTo", "SELECT '--Select--' union select referTo from ng_MASTER_DecisionReason where workstep='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"'");
		        }
			
			else{
				formObject.setVisible("DecisionHistory_Button1", false);
				formObject.setLocked("DecisionHistory_ReferTo", true);
				formObject.setLocked("DecisionHistory_DecisionReasonCode", true);
		        }
		}

		 if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName())){
		
			getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
		}
		else if ("cmplx_EmploymentDetails_DOJ".equalsIgnoreCase(pEvent.getSource().getName())){
		
			getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
		}             

		else if ("WorldCheck1_Dob".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("RLOS val change "+ "Value of WorldCheck1_Dob is:"+formObject.getNGValue("WorldCheck1_Dob"));
			//Changes done to auto populate age in world check fragment             
			getAge(formObject.getNGValue("WorldCheck1_Dob"),"WorldCheck1_age");
		}
		 
		//code by saurabh on 28th Nov 2017 for calculating EMI after change in interest rate or tenor
		else if("cmplx_EligibilityAndProductInfo_InterestRate".equalsIgnoreCase(pEvent.getSource().getName()) || "cmplx_EligibilityAndProductInfo_Tenor".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

				String EMI=getEMI(LoanAmount,RateofInt,tenor);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);

			}
			catch(Exception e){
				//PersonalLoanS.logException(e);
				PersonalLoanS.mLogger.info(" Exception in EMI Generation");
			}
		}


		else if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
		}

		else if ("SubProd".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("PL val change "+ "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			formObject.clear("AppType");
			formObject.setNGValue("AppType","--Select--");
			if("Business titanium Card".equalsIgnoreCase(formObject.getNGValue("SubProd"))){
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
				formObject.setNGValue("EmpType","Self Employed");
			}              
			else if("Instant Money".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");

			else if("Limit Increase".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");

			else if("Salaried Credit Card".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");

			else if("Self Employed Credit Card".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");

			else if("Expat Personal Loans".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");

			else if("National Personal Loans".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");

			else if("Pre-Approved".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");

			else if("Product Upgrade".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
		}

		else if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName())){

			LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
		}

		else if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
		}

		//Added by Arun (21/09/17)
		else if ("NotepadDetails_notedesc".equalsIgnoreCase(pEvent.getSource().getName())){
			String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
			
			String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
			PersonalLoanS.mLogger.info(" query is "+sQuery);
			List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
			if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)))
			{
				formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
				formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
			}

		}

		else if("cmplx_Decision_Feedbackstatus".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getWFActivityName().equalsIgnoreCase("FCU"))
			{
				PersonalLoanS.mLogger.info(" queryarun is "+"inside feedback status");
				LoadpicklistFCU();
				PersonalLoanS.mLogger.info(" queryarun is "+"inside feedback status after loadpicklist");
			} 
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Add")){
			formObject.setNGValue("WorldCheck1_winame",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info("PL"+ "Inside add button: "+formObject.getNGValue("WorldCheck1_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Modify")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");

		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Modify"))
		{
			formObject.ExecuteExternalCommand("NGModifyRow","cmplx_Decision_cmplx_gr_decision");
	
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Compliance_Modify"))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Compliance_cmplx_gr_compliance");
		}
		// Below code added by nikhil 13/11/2017 for Code merge
		
		 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
			 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
			 {
				 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
				PersonalLoanS.mLogger.info(" In PL_Initiation VALChanged---New Value of CAD_dec is: "+ formObject.getNGValue("Decision_Combo2"));
				

			 }
			 
			 else{
				 
				formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
				PersonalLoanS.mLogger.info(" In PL_Initiation VALChanged---New Value of decision is: "+ formObject.getNGValue("Decision_Combo2"));
			 	  }
		 	}
		//--Above code added by nikhil 13/11/2017 for Code merge

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to be called on Click Event     
	 
	***********************************************************************************  */

	public void mouse_Clicked(ComponentEvent pEvent){

		PersonalLoanS.mLogger.info( "PersonalLoanSCommonCode--->Inside mouse_Clicked()");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String outputResponse = "";
		String ReturnCode="";
		String ReturnDesc="";
		String BlacklistFlag="";
		String DuplicationFlag="";
		String SystemErrorCode="";
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		PL_Integration_Input genX=new PL_Integration_Input();
		PL_Integration_Output ReadXml = new PL_Integration_Output();
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		String alert_msg="";

		//disha FSD
		if ("AddressDetails_addr_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info( "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		else if ("AddressDetails_addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		else if ("AddressDetails_addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

		}

		//Arun added (14-jun)
		else if ("ExtLiability_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Liability_Wi_Name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		else if ("ExtLiability_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		else if ("ExtLiability_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		else if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
			PersonalLoanS.mLogger.info( "Inside FATCA_Button1 "+text);
			formObject.addItem("cmplx_FATCA_selectedreason", text);
			try {
				formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

			}catch (Exception e) {
				printException(e);
			}

		}
		else if ("SmartCheck_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck_SmartCheckGrid");
			PersonalLoanS.mLogger.info( "Inside add button33: add of SmartCheck_Add details");
		}
		else if ("SmartCheck_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck_SmartCheckGrid");
			
				formObject.setEnabled("SmartCheck_Add",true);
				formObject.setEnabled("SmartCheck_Modify",false);
			
			PersonalLoanS.mLogger.info( "Inside add button33: modify of SmartCheck_Modify details");
		}
		if("SmartCheck_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Smart_Check");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val015");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info( "Inside FATCA_Button2 ");
			formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
			try {
				formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
			} catch (Exception e) {
				
				printException(e);
			}
		}
		else if("ReferenceDetails_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");			
		}

		else if ("ReferenceDetails_Reference__modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
		}

		else if ("ReferenceDetails_Reference_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
		}

		else if("OECD_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");			
		}

		else if ("OECD_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}

		else if ("OECD_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}

		else if("GuarantorDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("GuarantorDetails_guarantor_WIName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Guarantror_GuarantorDet");			
		}

		else if ("GuarantorDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Guarantror_GuarantorDet");
		}

		else if ("GuarantorDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Guarantror_GuarantorDet");
		}
		
		else if ("IncomingDoc_AddFromPCButton".equalsIgnoreCase(pEvent.getSource().getName())){
			IRepeater repObj=null;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
			repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
			PersonalLoanS.mLogger.info("value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));
		}

		else if("FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			String ResideSince="";
			                             

			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");

			//Deepak Code change for Entity Details
			//if("Is_EID_Genuine".equalsIgnoreCase("Y") && "cmplx_Customer_EmiratesID" != ""){
			String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
			PersonalLoanS.mLogger.info( "EID value for Entity Details: "+EID );

			if(formObject.getNGValue("cmplx_Customer_card_id_available")=="false"){
				PersonalLoanS.mLogger.info("RLOS value of Customer Details"+formObject.getNGValue("cmplx_Customer_card_id_available"));
				outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
					ReadXml.valueSetIntegration(outputResponse);    
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					parse_cif_eligibility(outputResponse);
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					
					if("true".equalsIgnoreCase(NTB_flag)){
						setcustomer_enable();
						try
						{
							PersonalLoanS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
							throw new ValidatorException(new FacesMessage("Customer is a New to Bank Customer."));

						}
						finally{
							hm.clear();
						}
					}
					else{
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
						
							String DOB=formObject.getNGValue("cmplx_Customer_DOb");
							
			
			
							formObject.setNGValue("cmplx_Customer_DOb",common.Convert_dateFormat(DOB, "yyyy-MM-dd","dd/MM/yyyy"));
						

			
						String  Marital_Status =  outputResponse.contains("<MaritialStatus>") ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
						PersonalLoanS.mLogger.info("RLOS value of Marital_Status"+Marital_Status);
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						ResideSince =  outputResponse.contains("<ResideSince>") ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
						PersonalLoanS.mLogger.info("RLOS value of ResideSince"+ResideSince);



						if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
							formObject.setNGValue("Is_Customer_Details","Y");
							formObject.RaiseEvent("WFSave");
							PersonalLoanS.mLogger.info("RLOS value of Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));


							formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
							formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
							formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
							formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
							formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
							if(formObject.isVisible("Supplementary_Container")){
								formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails")+25);
								formObject.setTop("FATCA",formObject.getTop("Supplementary_Container")+25);
								formObject.setTop("KYC", formObject.getTop("FATCA")+25);
								formObject.setTop("OECD", formObject.getTop("KYC")+25);
								//Java takes height of entire container in getHeight while js takes current height         i.e collapsed/expanded
							}
							else{
								formObject.setTop("FATCA",formObject.getTop("CardDetails")+25);
								formObject.setTop("KYC", formObject.getTop("FATCA")+25);
								formObject.setTop("OECD", formObject.getTop("KYC")+25);
							}
							ReadXml.valueSetIntegration(outputResponse);
							//code to set Emirates of residence start.
							int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
							if(n>0){
								for(int i=0;i<n;i++){
									PersonalLoanS.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
									if("RESIDENCE".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0))){
										PersonalLoanS.mLogger.info("selecting Emirates of residence: settign value: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
										formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
									}
								}
								//code to set Emirates of residence End.

								throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));
							}
							//code change to save the date in desired format by AMAN        
							
								PersonalLoanS.mLogger.info("converting date entered"+formObject.getNGValue("cmplx_Customer_DOb")+"");						
								String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
								//SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
								//SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");

								if(str_dob!=null&&!"".equalsIgnoreCase(str_dob)){
									String n_str_dob=common.Convert_dateFormat(str_dob, "yyyy-mm-dd", "dd/mm/yyyy");							
									PersonalLoanS.mLogger.info("converting date entered"+n_str_dob);
									formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
								}			     
						}
						else{
							formObject.setNGValue("Is_Customer_Details","N");

							alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val029");//"Error in fetch Customer details operation";
							PersonalLoanS.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
						}

						formObject.RaiseEvent("WFSave");
					}
					if("Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility")) && "Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details")))
					{ 
						PersonalLoanS.mLogger.info("RLOS value of Customer Details"+"inside if condition");
						formObject.setEnabled("FetchDetails", false); 
						throw new ValidatorException(new FacesMessage("Customer information fetched sucessfully"));
					}
					else{
						formObject.setEnabled("FetchDetails", true);
					}
					PersonalLoanS.mLogger.info("RLOS value of Customer Details ----1234");
					//formObject.RaiseEvent("WFSave");
					try
					{//empty try
					}
					finally{
						hm.clear();
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");

					PersonalLoanS.mLogger.info("Dedupe check failed.");

					try{
//empty try
					}
					finally{
						hm.clear();
					}
				}
				//added
			}
			
			else if(formObject.getNGValue("cmplx_Customer_card_id_available")=="true"){
				PersonalLoanS.mLogger.info("RLOS value of Customer Details ----1234567"+formObject.getNGValue("cmplx_Customer_card_id_available"));
				PersonalLoanS.mLogger.info("RLOS value of Customer Details ----1234567-->inside true");
				outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
				
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				ResideSince =  outputResponse.contains("<ResideSince>") ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
				PersonalLoanS.mLogger.info("RLOS value of ResideSince"+ResideSince);

				if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){

					formObject.setNGValue("Is_Customer_Details","Y");
					formObject.RaiseEvent("WFSave");
					PersonalLoanS.mLogger.info("RLOS value of CurrentDate Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));


					formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
					formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
					formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
					formObject.setEnabled("Customer_Button1", true);      
					PersonalLoanS.mLogger.info("year difference:"+"diffdays difference after button is enabled");
					if(formObject.isVisible("Supplementary_Container")){
						formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails")+25);
						formObject.setTop("FATCA",formObject.getTop("Supplementary_Container")+25);
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
						//Java takes height of entire container in getHeight while js takes current height  i.e collapsed/expanded
					}
					else{
						formObject.setTop("FATCA",formObject.getTop("CardDetails")+25);
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
					}
					ReadXml.valueSetIntegration(outputResponse);
					//code added here to change the DOB in DD/MM/YYYY format     
					
						String DOB=formObject.getNGValue("cmplx_Customer_DOb");
						formObject.setNGValue("cmplx_Customer_DOb",common.Convert_dateFormat(DOB, "yyyy-MM-dd", "dd/MM/yyyy"));
					
					// code added here to change the DOB in DD/MM/YYYY format    
					//code to set Emirates of residence start.
					int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					if(n>0){
						for(int i=0;i<n;i++){
							PersonalLoanS.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
							if("RESIDENCE".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0))){
								PersonalLoanS.mLogger.info("selecting Emirates of residence: settign value: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
								formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
							}
						}
						//code to set Emirates of residence End.
						formObject.setEnabled("FetchDetails", false); 
						throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));

					}
					//code change to save the date in desired format by AMAN        
					try{
						PersonalLoanS.mLogger.info("converting date entered"+formObject.getNGValue("cmplx_Customer_DOb")+"");
						
						String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
						PersonalLoanS.mLogger.info("converting date entered"+str_dob+"");
					
					}
					catch(Exception e){
						PersonalLoanS.mLogger.info("Exception Occur while converting date"+e+"");
					}
					//code change end to save the date in desired format by AMAN             



				}
				else{
					formObject.setNGValue("Is_Customer_Details","N");
					formObject.RaiseEvent("WFSave");
					PersonalLoanS.mLogger.info("RLOS value of Customer Details456"+formObject.getNGValue("Is_Customer_Details"));
				}
				PersonalLoanS.mLogger.info("RLOS value of Customer Details789"+formObject.getNGValue("Is_Customer_Details"));
			}
			formObject.RaiseEvent("WFSave");
			
		}

		else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			
			String NegatedFlag="";
			outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","");
			PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+"Customer Eligibility");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";


			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

			formObject.setNGValue("Is_Customer_Eligibility","Y");

			if("0000".equalsIgnoreCase(ReturnCode)){
				ReadXml.valueSetIntegration(outputResponse); 
				parse_cif_eligibility(outputResponse);
				BlacklistFlag= outputResponse.contains("<BlacklistFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag= outputResponse.contains("<DuplicationFlag>") ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= outputResponse.contains("<NegatedFlag>") ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is NegatedFlag"+NegatedFlag);
				if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
					ReadXml.valueSetIntegration(outputResponse);    
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					formObject.RaiseEvent("WFSave");
					PersonalLoanS.mLogger.info("RLOS value of ReturnDesc Is_Customer_Eligibility"+formObject.getNGValue("Is_Customer_Eligibility"));
					formObject.setNGValue("BlacklistFlag",BlacklistFlag);
					formObject.setNGValue("DuplicationFlag",DuplicationFlag);
					formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					if("true".equalsIgnoreCase(NTB_flag)){
						PersonalLoanS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val030");//"Customer is a New to Bank Customer.";
					}
					else{
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val031");//"Customer is an Existing Customer.";
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					formObject.RaiseEvent("WFSave");
				}
				PersonalLoanS.mLogger.info("RLOS value of Customer Details"+formObject.getNGValue("Is_Customer_Eligibility"));
				PersonalLoanS.mLogger.info("RLOS value of BlacklistFlag"+formObject.getNGValue("BlacklistFlag"));
				PersonalLoanS.mLogger.info("RLOS value of DuplicationFlag"+formObject.getNGValue("DuplicationFlag"));
				PersonalLoanS.mLogger.info("RLOS value of IsAcctCustFlag"+formObject.getNGValue("IsAcctCustFlag"));
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));
			
			}

		}
		//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)


		else if ("DecisionHistory_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {

			PersonalLoanS.mLogger.info(""+"inside create cif button");
			outputResponse = genX.GenerateXML("NEW_ACCOUNT_REQ","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			String NewAcid= outputResponse.contains("<NewAcid>") ? outputResponse.substring(outputResponse.indexOf("<NewAcid>")+"</NewAcid>".length()-1,outputResponse.indexOf("</NewAcid>")):"";
			String IBANNumber= outputResponse.contains("<IBANNumber>") ? outputResponse.substring(outputResponse.indexOf("<IBANNumber>")+"</IBANNumber>".length()-1,outputResponse.indexOf("</IBANNumber>")):"";
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				ReadXml.valueSetIntegration(outputResponse);    
				PersonalLoanS.mLogger.info("RLOS value of Account Request"+"after valuesetcudtomer");
				formObject.setNGValue("Is_Account_Create","Y");
				formObject.setNGValue("EligibilityStatus","Y");
				formObject.setNGValue("EligibilityStatusCode","Y");
				formObject.setNGValue("EligibilityStatusDesc","Y");
				formObject.setNGValue("Account_Number",NewAcid);
				formObject.setNGValue("IBAN_Number",IBANNumber);
			
				PersonalLoanS.mLogger.info("RLOS value of Account Request NewAcid"+formObject.getNGValue("Account_Number"));
				PersonalLoanS.mLogger.info("RLOS value of Account Request IBANNumber"+formObject.getNGValue("IBAN_Number"));
				PersonalLoanS.mLogger.info("RLOS value of Account Request cmplx_Decision_IBAN"+formObject.getNGValue("cmplx_Decision_IBAN"));
				PersonalLoanS.mLogger.info("RLOS value of Account Request cmplx_Decision_AccountNo"+formObject.getNGValue("cmplx_Decision_AccountNo"));
			}
			else{
				formObject.setNGValue("Is_Account_Create","N");
			}

			PersonalLoanS.mLogger.info("RLOS value of Account Request"+formObject.getNGValue("Is_Account_Create"));
			PersonalLoanS.mLogger.info("RLOS value of EligibilityStatus"+formObject.getNGValue("EligibilityStatus"));
			PersonalLoanS.mLogger.info("RLOS value of EligibilityStatusCode"+formObject.getNGValue("EligibilityStatusCode"));
			PersonalLoanS.mLogger.info("RLOS value of EligibilityStatusDesc"+formObject.getNGValue("EligibilityStatusDesc"));
			PersonalLoanS.mLogger.info("RLOS value of Account Request NewAcid111"+formObject.getNGValue("Account_Number"));
			PersonalLoanS.mLogger.info("RLOS value of Account Request IBANNumber111"+formObject.getNGValue("IBAN_Number"));

			outputResponse = genX.GenerateXML("NEW_CUSTOMER_REQ","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				ReadXml.valueSetIntegration(outputResponse);    
				PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Inside if of New customer Req");
				formObject.setNGValue("Is_Customer_Create","Y");

			}
			else{
				PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Inside else of New Customer Req");
				formObject.setNGValue("Is_Customer_Create","N");
			}
			formObject.RaiseEvent("WFSave");

		}


		else if ("Product_Add".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setNGValue("Product_wi_name",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info( "Inside add button: "+formObject.getNGValue("Product_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
		}              


		else if ("Product_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			
			
			

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
			
			//Arun (12/11/17) CardProd chages to change the header for Application Type  
			String cardprodval = formObject.getNGSelectedItemText("CardProd"); 
			formObject.setNGValue("CardLabel",cardprodval);
		}

		else if ("Product_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
		}

		else if ("PartMatch_Search".equalsIgnoreCase(pEvent.getSource().getName())){
			
			PersonalLoanS.mLogger.info("PL PartMatch_Search"+ "Inside PartMatch_Search button: ");

			
			formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
			outputResponse = genX.GenerateXML("DEDUP_SUMMARY","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("PL value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";          
			PersonalLoanS.mLogger.info("PL value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				
				//change by saurabh on 11th July 17.
				PL_Integration_Output.parseDedupe_summary(outputResponse);
				formObject.setNGValue("Is_PartMatchSearch","Y");
				PersonalLoanS.mLogger.info("PL value of Part Match Request"+"inside if of partmatch");
				alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val0032");//"Part match sucessfull";
			}
			else{
				formObject.setNGValue("Is_PartMatchSearch","N");
				PersonalLoanS.mLogger.info("PL value of Part Match Request"+"inside else of partmatch");
				alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val033");//"Error while performing Part match";
			}
			formObject.RaiseEvent("WFSave");
			PersonalLoanS.mLogger.info("PL value of Part Match Request"+formObject.getNGValue("Is_PartMatchSearch"));
			if("Y".equalsIgnoreCase(formObject.getNGValue("Is_PartMatchSearch")))
			{ 
				PersonalLoanS.mLogger.info("PL value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition of disabling the button");

			}
			else{
				formObject.setEnabled("PartMatch_Search", true);
			
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		//for BlackList Call added on 3rd May 2017
		else if ("PartMatch_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match1111");
			
			String CifID="";
			String CustomerStatus="";
			String firstName="";
			String LastName="";
			String Negatedflag="";
			String Negatednote="";
			String NegatedCode="";
			String NegatedReason="";
			String Blacklistflag="";
			String Blacklistnote="";
			String BlacklistCode="";
			String BlacklistReason="";
			String OldPassportNumber=""; 
			String PassportNumber="";
			String Visa="";
			String EmirateID="";
			String PhoneNo="";
			//added here
			String StatusType="";
			//ended here
			PersonalLoanS.mLogger.info("Blacklist Details part Match1111 after initializing strings");
			outputResponse =genX.GenerateXML("BLACKLIST_DETAILS","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			PersonalLoanS.mLogger.info("PL value of ReturnDesc part Match"+ReturnDesc);

			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){

				formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
				alert_msg="Blacklist check successfull";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
				StatusType= outputResponse.contains("<StatusType>") ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted StatusType"+StatusType);
				outputResponse = outputResponse.substring(outputResponse.indexOf("<CustomerBlackListResponse>")+27, outputResponse.indexOf("</CustomerBlackListResponse>"));
				PersonalLoanS.mLogger.info(outputResponse);
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";

				Document doc = ReadXml.getDocument(outputResponse);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("StatusInfo");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);


					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						StatusType = eElement.getElementsByTagName("StatusType").item(0).getTextContent() ;
						if("Black List".equalsIgnoreCase(StatusType)){

							Blacklistflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
							BlacklistReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
							BlacklistCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;

						}
						if("Negative List".equalsIgnoreCase(StatusType)){
							Negatedflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
							NegatedReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
							NegatedCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;
						}
					}
					//added
					CustomerStatus =  outputResponse.contains("<CustomerStatus>") ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
					CifID=formObject.getNGValue("PartMatch_CIFID");
					firstName=formObject.getNGValue("PartMatch_fname");
					LastName=formObject.getNGValue("PartMatch_lname");
					OldPassportNumber=formObject.getNGValue("PartMatch_oldpass");
					PassportNumber=formObject.getNGValue("PartMatch_newpass");
					Visa=formObject.getNGValue("PartMatch_visafno");
					EmirateID=formObject.getNGValue("PartMatch_EID");
					PhoneNo=formObject.getNGValue("PartMatch_mno1");
					PersonalLoanS.mLogger.info("PL value of CustomerStatus"+"Customer is Blacklisted StatusType"+CustomerStatus);

				}
			}		
			else{
				formObject.setNGValue("Is_Customer_Eligibility_Part","N");    
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
			}
			
			List<String> BlacklistGrid = new ArrayList<String>(); 
			BlacklistGrid.add(CifID);
			BlacklistGrid.add(CustomerStatus); 
			BlacklistGrid.add(firstName);
			BlacklistGrid.add(LastName);
			BlacklistGrid.add(Blacklistflag);
			BlacklistGrid.add(Blacklistnote);
			BlacklistGrid.add(BlacklistReason);
			BlacklistGrid.add(BlacklistCode);
			BlacklistGrid.add(Negatedflag);
			BlacklistGrid.add(Negatednote);
			BlacklistGrid.add(NegatedReason);
			BlacklistGrid.add(NegatedCode);
			BlacklistGrid.add(PassportNumber);
			BlacklistGrid.add(OldPassportNumber);
			BlacklistGrid.add(EmirateID);
			BlacklistGrid.add(Visa);
			BlacklistGrid.add(PhoneNo);
			PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid");
			PersonalLoanS.mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added inFinacle CRM grid: "+ BlacklistGrid.toString());

			formObject.addItemFromList("cmplx_PartMatch_cmplx_PartBlacklistGrid",BlacklistGrid);
			PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid11111");       
			formObject.RaiseEvent("WFSave");  

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//Changes done by aman to correctly save the value in the grid


		else if ("PartMatch_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			formObject.setTop("External_Blacklist",formObject.getTop("FinacleCRM_CustInfo")+formObject.getHeight("FinacleCRM_CustInfo")+20);
			formObject.setTop("Finacle_Core",formObject.getTop("External_Blacklist")+30);
			formObject.setTop("MOL",formObject.getTop("Finacle_Core")+30);
			formObject.setTop("World_Check",formObject.getTop("MOL")+30);
			formObject.setTop("Sal_Enq",formObject.getTop("World_Check")+30);
			formObject.setTop("Reject_Enq",formObject.getTop("Sal_Enq")+30);

			String BlacklistFlag_Part = "";
			String BlacklistFlag_reason = "";
			String BlacklistFlag_code = "";
			String NegativeListFlag = "";

			outputResponse =genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Customer Details part Match");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

			BlacklistFlag_Part =  outputResponse.contains("<BlackListFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"NA";



			BlacklistFlag_reason =  outputResponse.contains("<BlackListReason>") ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";


			BlacklistFlag_code =  outputResponse.contains("<BlackListReasonCode>") ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";

			NegativeListFlag =  outputResponse.contains("<NegativeListFlag>") ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
			PersonalLoanS.mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){

				BlacklistFlag_Part =  outputResponse.contains("<BlacklistFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";                                  
				formObject.setNGValue("Is_Customer_Details_Part","Y");    
				formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
				if("Y".equalsIgnoreCase(BlacklistFlag_Part))
				{
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted");    
				}
				else
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is not Blacklisted");    
			}
			else{
				formObject.setNGValue("Is_Customer_Details_Part","N");
			}
			try{
				PersonalLoanS.mLogger.info("CC value of BlacklistFlag_Part flag inside try"+BlacklistFlag_Part+"");    
				List<String> list_custinfo = new ArrayList<String>();
				String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
				String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);

				list_custinfo.add(CIFID);  // cif id from partmatch
				list_custinfo.add("Individual");
				list_custinfo.add(PASSPORTNO); // passport
				list_custinfo.add(NegativeListFlag);
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add(BlacklistFlag_Part); // blacklist flag
				list_custinfo.add("");
				list_custinfo.add(BlacklistFlag_code);
				list_custinfo.add(BlacklistFlag_reason);
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add(formObject.getWFWorkitemName());

				PersonalLoanS.mLogger.info("CC DDVT Maker"+ "list_custinfo list values"+list_custinfo);
				formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
			}catch(Exception e){
				PersonalLoanS.mLogger.info("PL DDVT Maker"+ "Exception while setting data in grid:"+e.getMessage());
				PLCommon.printException(e);			
				alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val034");//"Error while setting data in finacle customer info grid";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			formObject.RaiseEvent("WFSave");          
		}
		//changes done as said by Deepak Sir To call Customer_Details call ended


		else if("GuarantorDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())){

			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("PL value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			PersonalLoanS.mLogger.info("PL value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				formObject.setNGValue("Is_Customer_Details","Y");
				PersonalLoanS.mLogger.info("PL value of EID_Genuine"+"value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
				ReadXml.valueSetIntegration(outputResponse);    
				PersonalLoanS.mLogger.info("PL value of Customer Details"+"Guarantor_CIF is generated");
				PersonalLoanS.mLogger.info("PL value of Customer Details"+formObject.getNGValue("Is_Customer_Details"));
			}
			else{
				PersonalLoanS.mLogger.info("Customer Details"+"Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details","N");
			}
			PersonalLoanS.mLogger.info("PL value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
			formObject.RaiseEvent("WFSave");
		}

		else if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			PersonalLoanS.mLogger.info( "EMpName$"+EmpName+"$");
			String query=null;
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017 Disha
			if("".equalsIgnoreCase(EmpName.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";

			PersonalLoanS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,Nature Of Business,EMPLOYER CATEGORY PL NATIONAL,EMPLOYER CATEGORY CARDS,EMPLOYER CATEGORY PL EXPAT,INCLUDED IN PL ALOC,DOI IN PL ALOC,INCLUDED IN CC ALOC,DATE OF INCLUSION IN CC ALOC,NAME OF AUTHORIZED PERSON FOR ISSUING SC/STL/PAYSLIP,ACCOMMODATION PROVIDED,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,OWNER/PARTNER/SIGNATORY NAMES AS PER TL,ALOC REMARKS,HIGH DELINQUENCY EMPLOYER,MAIN EMPLOYER CODE", true, 20);

		}


		else if("LoanDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("LoanDetails_winame",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info( "Inside add button: "+formObject.getNGValue("LoanDetails_winame"));                                                                                				
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_LoanDetails_cmplx_LoanGrid");
		}                                
		else if("LoanDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_LoanDetails_cmplx_LoanGrid");
		}
		else if("LoanDetails_Button5".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_LoanDetails_cmplx_LoanGrid");
		}

		else if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info( "Inside Customer_save button: ");
			formObject.saveFragment("CustomerDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val017");//"Customer Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ProductContainer");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val018");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("GuarantorDetails");
			throw new ValidatorException(new FacesMessage("Guarantor Save Successful"));


		}

		else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("IncomeDEtails");

		}

		else if("IncomeDetails_SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Incomedetails");
		}

		else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CompanyDetails");
		}

		else if("PartnerDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("PartnerDetails");
		}

		else if("SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Liability_container");
		}

		else if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("InternalExternalContainer");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val019");//"Liability Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//change by saurabh on 7th Dec
		else if("EMploymentDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EmploymentDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val020");//"Employment Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}



		else if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EligibilityAndProductInformation");
			alert_msg="Eligibility Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		
		else if("LoanDetailsRepay_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanDetails");
		}

		else if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Address_Details_container");
		}
		//disha FSD
		else if("AltContactDetails_ContactDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Alt_Contact_container");
		}

		else if("ReferenceDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ReferenceDetails");
			//below code by saurabh on 11th nov
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val045");//"Reference Details Saved";

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("CardDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supplementary_Container");
		}


		else if("LoanDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val022");//"Loan Details Saved";

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("LoanDetaisDisburs_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val035");//"Disbursal Details Saved";

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		
		//Below code added by Arun (23/11/17) for IMD save button in loan details
		else if("LoanDetails_IMD_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val046");//"Initial Money Deposit Saved";

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//Above code added by Arun (23/11/17) for IMD save button in loan details

		else if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FATCA");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val023");//"FATCA Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("KYC");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val024");//"KYC Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("OECD");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val016");//"OECD Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}	
		
		else if("RiskRating_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("Inside risk rating");
			formObject.saveFragment("Risk_Rating");
			PersonalLoanS.mLogger.info("risk rating saved");
		}

		else if("PartMatch_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Part_Match");
		}

		else if("FinacleCRMIncident_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FinacleCRM_Incidents");
		}

		else if("FinacleCRMCustInfo_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FinacleCRM_CustInfo");
		}                                                                                              

		else if("FinacleCore_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Finacle_Core");
		}

		else if("MOL1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("MOL");
		}

		else if("WorldCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("World_Check");
		}

		else if("SalaryEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Sal_Enq");
		}

		else if("CreditCardEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Credit_card_Enq");
		}

		else if("CaseHistoryReport_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Case_History");
		}

		else if("LOS_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LOS");
		}

		else if("RejectEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reject_Enq");
		}                                                                                              

		else if("DecisionHistory_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			
			saveIndecisionGrid();//Arun (23/09/17)
			formObject.saveFragment("DecisionHistory");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val002");//"Decision History Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
		}

		else  if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//formObject.saveFragment("Notepad_Values");
			formObject.saveFragment("NotepadDetails_Frame1");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val003");//"Notepad Details Saved";//Arun (23/09/17)
			throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
		}


		else if("CAD_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Details");
		}

		else if("CAD_Decision_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Dec");
		}


		else if("Loan_Disbursal_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Loan_Disbursal");
		}

		else if("CC_Creation_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CC_Creation");
		}

		else if("Limit_Inc_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Limit_Inc");
		}

		else if("CustDetailVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Cust_Detail_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val007");//"Customer detail verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("BussinessVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Business_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val008");//"Business detail verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("HomeCountryVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Home_country_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val009");//"Home country verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("ResidenceVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Residence_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val010");//"Residence verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("GuarantorVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Guarantor_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val011");//"Guarantor verification   saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("ReferenceDetailVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reference_detail_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val012");//"Reference detail verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("OfficeandMobileVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Office_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val013");//"Office verification details saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("LoanandCard_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Loan_card_details");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val014");//"Loan and Card details saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("Compliance_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Compliance");
		}

		
		else if("supervisorsection_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supervisor_section");
		}

		else if("NotepadDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}

		else if("NotepadDetails_Button5".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}

		else if("NotepadDetails_Button6".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}

		// below code changed by abhishek on 25th oct 2017 to change button id
		else if("WorldCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("WorldCheck1_winame",formObject.getWFWorkitemName());
			formObject.setNGValue("Is_WorldCheckAdd","Y");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		//-- Above code changed by abhishek on 25th oct 2017 to change button id
		//Code to add the dectech call on PL
		else if("ELigibiltyAndProductInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			PersonalLoanS.mLogger.info("Inside button");		
			formObject.setNGValue("DecCallFired","Eligibility");
			try{
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
				
				String EMI=getEMI(LoanAmount,RateofInt,tenor);
		
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||"".equalsIgnoreCase(EMI)?"0":EMI);
				
			}
			catch(Exception e){
				//empty catch
			}
				
					formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
					PersonalLoanS.mLogger.info("Inside beforer income_Dectech ");
					income_Dectech();
					PersonalLoanS.mLogger.info("Inside after income_Dectech ");
			
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
					PersonalLoanS.mLogger.info("Inside beforer EmploymentDetails ");
					
			
					PersonalLoanS.mLogger.info("Inside after EmploymentDetails ");
					
					
					formObject.fetchFragment("MOL", "MOL1", "q_MOL");
					formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
					formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
					formObject.setTop("Reject_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
					formObject.setTop("Sal_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
			
					outputResponse = genX.GenerateXML("DECTECH","");
					
					

			SystemErrorCode =  outputResponse.contains("<SystemErrorCode>") ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";

			if("".equalsIgnoreCase(SystemErrorCode)){
				ReadXml.valueSetIntegration(outputResponse);   
				alert_msg="Decision engine integration successful";
				formObject.RaiseEvent("WFSave");
			}
			else{
				alert_msg="Critical error occurred Please contact administrator";

			}
			
			//deepak Code changes to calculate LPF amount and % 08-nov-2017 start
			try{
				PersonalLoanS.mLogger.info("RLOS_Common"+"Inside set event of LPF Data");
				double LPF_charge;
				List<List<String>> result=formObject.getDataFromDataSource("select CHARGERATE from NG_MASTER_Charges where ChargeID = ( select distinct LPF_ChargeID from NG_master_Scheme where schemeid = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"' )");
				PersonalLoanS.mLogger.info("RLOS_Common"+ "result of fetch RMname query: "+result); 
				if(result==null  || result.isEmpty()){
					LPF_charge=1;
				}
				else{
					LPF_charge = Double.parseDouble(result.get(0).get(0));
				}
				PersonalLoanS.mLogger.info("RLOS_Common code "+ "result LPF_charge: "+LPF_charge);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPF", LPF_charge);
				double LPF_amount;
				double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				LPF_amount = (final_Loan_amount*LPF_charge)/100;
				PersonalLoanS.mLogger.info("RLOS_Common code "+ "result LPF_amount: "+LPF_amount);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPFAmount", LPF_amount);
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info(e);
				PersonalLoanS.mLogger.info(" Exception in EMI Generation");
			}
			//deepak Code changes to calculate LPF amount and % 08-nov-2017 End
			
			throw new ValidatorException(new FacesMessage(alert_msg));
		 }
			//Code to addd dechtech call for pl end 
	 
	
		else if("WorldCheck1_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}

		else if("WorldCheck1_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("Is_World_Check_Add","N");
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}

		else if("cmplx_Decision_VERIFICATIONREQUIRED".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			PersonalLoanS.mLogger.info("CC val cmplx_Decision_VERIFICATIONREQUIRED "+ "Value of cmplx_Decision_VERIFICATIONREQUIRED is:"+formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED"));
			if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) )
			{
				PersonalLoanS.mLogger.info("CC val change "+ "Inside Y of CPV required");
				formObject.setNGValue("cpv_required","Y");
			}
			else
			{
				PersonalLoanS.mLogger.info("CC val change "+ "Inside N of CPV required");
				formObject.setNGValue("cpv_required","N");
			}
		}
		// disha FSD
		else if ("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			// Below code added by  nikhil 11/11/17
			formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
			

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_add();
					}
		else if ("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			//++Below code added by  nikhil 11/11/17
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_modify();
			//-- Above code added by  nikhil 11/11/17
		}
		else if ("NotepadDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			//Below code added by  nikhil 11/11/17
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_delete();
			//-- Above code added by  nikhil 11/11/17
		}
		//Below code added by  nikhil 11/11/17
		else if("cmplx_NotepadDetails_cmplx_notegrid".equalsIgnoreCase(pEvent.getSource().getName())){

			Notepad_grid();
		} 
		//-- Above code added by  nikhil 11/11/17
		else  if ("FinacleCRMCustInfo_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
		}

		else if("DecisionHistory_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			String message = CustomerUpdate();
			throw new ValidatorException(new FacesMessage(message));
		}

		//tanshu started
		else if("DecisionHistory_updcust".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				PersonalLoanS.mLogger.info("inside ENTITY_MAINTENANCE_REQ is generated");
				String acc_veri= (formObject.getNGValue("Is_Acc_verified")!=null) ?formObject.getNGValue("Is_Acc_verified"):"";
				PersonalLoanS.mLogger.info("PL checker Account Update call"+ "entity_flag : "+acc_veri);

				if(acc_veri == null || "".equalsIgnoreCase(acc_veri)||"N".equalsIgnoreCase(acc_veri)){
					outputResponse = genX.GenerateXML("ENTITY_MAINTENANCE_REQ","AcctVerification");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("PL DDVT checker value of ReturnCode AcctVerification: "+ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("Is_Acc_verified","Y");
						acc_veri="Y";
						PersonalLoanS.mLogger.info("account Verified successfully");
					}
					else{
						PersonalLoanS.mLogger.info("account Verified failed ReturnCode: "+ReturnCode );
						formObject.setNGValue("Is_Acc_verified","N");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val025");//"Account Verification operation Failed, Please try after some time or contact administrator";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}

				String acc_acti= formObject.getNGValue("Is_Acc_Active")!=null ? formObject.getNGValue("Is_Acc_Active"):"";
				if("Y".equalsIgnoreCase(acc_veri)&&("".equalsIgnoreCase(acc_acti)||"N".equalsIgnoreCase(acc_acti))){
					outputResponse = genX.GenerateXML("ENTITY_MAINTENANCE_REQ","AcctActivation");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("PL DDVT checker value of ReturnCode for AcctActivation: "+ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("Is_Acc_Active","Y");
						acc_acti="Y";
						PersonalLoanS.mLogger.info("account Activation successfully");
					}
					else{
						PersonalLoanS.mLogger.info("ENTITY_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
						formObject.setNGValue("Is_Acc_Active","N");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val026");//"Account Activation operation Failed, Please try after some time or contact administrator";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}

				}

				if("Y".equalsIgnoreCase(acc_veri)&&"Y".equalsIgnoreCase(acc_acti)) {
					outputResponse = genX.GenerateXML("ACCOUNT_MAINTENANCE_REQ","");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("PL DDVT checker value of ReturnCode"+ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode) ){
						formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","Y");
						PersonalLoanS.mLogger.info("value of ACCOUNT_MAINTENANCE_REQ"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
						ReadXml.valueSetIntegration(outputResponse);    
						PersonalLoanS.mLogger.info("ACCOUNT_MAINTENANCE_REQ is generated");
						PersonalLoanS.mLogger.info("PL DDVT checker value of Customer Details"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
						formObject.RaiseEvent("WFSave");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val027");//"Account Updated Successfully !";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					else{
						PersonalLoanS.mLogger.info("ACCOUNT_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
						formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","N");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val028");//"Account Update operation Failed, Please try after some time or contact administrator";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}										
			}
			catch(Exception ex){
				PersonalLoanS.mLogger.info("Exception in update account: ");
				printException(ex);
			}
		}

		else if("DecisionHistory_chqbook".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("RLOS value of CheckBook");
			outputResponse = genX.GenerateXML("CHEQUE_BOOK_ELIGIBILITY","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			String ReferenceId=outputResponse.contains("<ReferenceId>") ? outputResponse.substring(outputResponse.indexOf("<ReferenceId>")+"</ReferenceId>".length()-1,outputResponse.indexOf("</ReferenceId>")):"";    
			PersonalLoanS.mLogger.info("RLOS value of ReferenceId"+ReferenceId);
			//Modified condition Tanshu Aggarwal(7-06-2017)
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				//Modified condition Tanshu Aggarwal(7-06-2017)
				formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","Y");
				PersonalLoanS.mLogger.info("value of ENTITY_MAINTENANCE_REQ"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
				ReadXml.valueSetIntegration(outputResponse);    
				PersonalLoanS.mLogger.info("Is_CHEQUE_BOOK_ELIGIBILITY is generated");
				PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
				formObject.setNGValue("cmplx_Decision_ChequeBookNumber",ReferenceId);
				PersonalLoanS.mLogger.info("ReferenceId"+formObject.getNGValue("cmplx_Decision_ChequeBookNumber"));
				formObject.RaiseEvent("WFSave");
			}
			else{
				PersonalLoanS.mLogger.info("Is_CHEQUE_BOOK_ELIGIBILITY is not generated");
				formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","N");
			}
			outputResponse = genX.GenerateXML("NEW_CARD_REQ","");
			ReturnCode =  outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			String RequestId=outputResponse.contains("<RequestId>") ? outputResponse.substring(outputResponse.indexOf("<RequestId>")+"</RequestId>".length()-1,outputResponse.indexOf("</RequestId>")):"";    
			PersonalLoanS.mLogger.info("RLOS value of RequestId"+RequestId);
			//Modified condition Tanshu Aggarwal(7-06-2017)
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				//Modified condition Tanshu Aggarwal(7-06-2017)
				formObject.setNGValue("Is_NEW_CARD_REQ","Y");
				PersonalLoanS.mLogger.info("value of NEW_CARD_REQ"+formObject.getNGValue("Is_NEW_CARD_REQ"));
				ReadXml.valueSetIntegration(outputResponse);    
				PersonalLoanS.mLogger.info("Is_NEW_CARD_REQ is generated");
				PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value"+formObject.getNGValue("Is_NEW_CARD_REQ"));
				formObject.setNGValue("cmplx_Decision_DebitcardNumber",RequestId);
				PersonalLoanS.mLogger.info("NEW_CARD_REQ"+formObject.getNGValue("cmplx_Decision_DebitcardNumber"));
				formObject.RaiseEvent("WFSave");
			}
			else{
				PersonalLoanS.mLogger.info("Is_NEW_CARD_REQ is not generated");
				formObject.setNGValue("Is_NEW_CARD_REQ","N");
			}

			formObject.RaiseEvent("WFSave");
			PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
			PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_NEW_CARD_REQ"));
			String ChequeBook=formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY");
			String NewCardReq=formObject.getNGValue("Is_NEW_CARD_REQ");
			if(("Y".equalsIgnoreCase(ChequeBook)) && ("Y".equalsIgnoreCase(NewCardReq))){
				PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));

				throw new ValidatorException(new CustomExceptionHandler("Debit Card and Cheque Book created successfully!!","DecisionHistory_chqbook#Debit Card and Cheque Book created successfully","",hm));
			}
			else if(("N".equalsIgnoreCase(ChequeBook)) && ("Y".equalsIgnoreCase(NewCardReq))){
				PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
				throw new ValidatorException(new CustomExceptionHandler("Debit Card created sucessfully, But Cheque Book created Failed.!!","DecisionHistory_chqbook#Debit Card created sucessfully, But Cheque Book created Failed.","",hm));
			}
			else if(("Y".equalsIgnoreCase(ChequeBook)) && ("N".equalsIgnoreCase(NewCardReq))){
				PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
				throw new ValidatorException(new CustomExceptionHandler("Cheque Book created sucessfully, But Debit Card created Failed.!!","DecisionHistory_chqbook#Cheque Book created sucessfully, But Debit Card created Failed.","",hm));
			}
			else{
				PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
				throw new ValidatorException(new CustomExceptionHandler("Cheque Book created and Debit Card created Failed, Please try after time or contact administrator!!","DecisionHistory_chqbook#Cheque Book created and Debit Card created Failed, Please try after time or contact administrator.","",hm));
			}
		}
		//ended merged code


		//started code merged
		else  if("CC_Creation_Update_Customer".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info(""+"inside Update_Customer");  
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Inquire");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode)){
				PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
				formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
				PersonalLoanS.mLogger.info(""+"Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
				PersonalLoanS.mLogger.info(""+"inside Update_Customer");  
				String cif_status = outputResponse.contains("<CustomerStatus>") ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
				if("ACTVE".equalsIgnoreCase(cif_status)){
					outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Lock");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CustLock_Disbursal","Y");
						PersonalLoanS.mLogger.info(""+"Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 

						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"Customer_Details is generated");
						
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","UnLock");
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+"inside unlock");
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
						PersonalLoanS.mLogger.info(""+"Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
						ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
						PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
						if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
							outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","");
							ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
							ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
							PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
							//  Message =outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    
							

							if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
								formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
								PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
								ReadXml.valueSetIntegration(outputResponse);    
								PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
								PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
							}
							else{
								PersonalLoanS.mLogger.info("Customer Details"+"CUSTOMER_UPDATE_REQ is not generated");
								formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","N");
							}
							PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
							formObject.RaiseEvent("WFSave");
							PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");
							if("Y".equalsIgnoreCase(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal")))
							{ 
								PersonalLoanS.mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition");
								formObject.setEnabled("Update_Customer", false); 
								throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","Update_Customer#Customer Updated Successful!!","",hm));
							}
							else{
								formObject.setEnabled("CC_Creation_Update_Customer", true);
								throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","Update_Customer#Customer Updated Fail!!","",hm));
							}
						}
						else{
							PersonalLoanS.mLogger.info("Customer Details"+"Customer_Details unlock is not generated");

						}
					}
					else{
						PersonalLoanS.mLogger.info("Customer Details"+"Customer_Details lock is not generated");
					}
				}
				else {
					PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
					throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
				}
				//added one more here
			}
			else{
				PersonalLoanS.mLogger.info("Customer Details"+"Customer_Details Inquiry is not generated");
			}
		}

		else if("CC_Creation_Card_Services".equalsIgnoreCase(pEvent.getSource().getName())){

			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+"inside button click");
			String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
			PersonalLoanS.mLogger.info("RLOS value of Product_Name"+""+Product_Name);
			if("Limit Change Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if("0000".equalsIgnoreCase(ReturnCode)){
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y");
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details for limit change"+formObject.getNGValue("Is_CardServicesPL"));
				}
			}
			else if("Financial Services Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y"); 
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details for financial product"+formObject.getNGValue("Is_CardServicesPL"));
				}
			}
			else if("Product Upgrade Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y"); 
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details for product upgrade"+formObject.getNGValue("Is_CardServicesPL"));
				}
			}
		}
		//end of entity main and account mainitainence call

		//code for New Card and Notification and New Loan Request call Tanshu Aggarwal
		else if("CC_Creation_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.RaiseEvent("WFSave");
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+"inside button click");
			String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
			PersonalLoanS.mLogger.info("RLOS value of Product_Name"+""+Product_Name);
			if("Limit Change Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y");
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode Is_CardServices"+""+formObject.getNGValue("Is_CardServicesPL"));
					PersonalLoanS.mLogger.info(""+"CARD SERVICES RUNNING Limit Change Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiactionPL","Y");
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode Is_CardNotifiactionPL"+""+formObject.getNGValue("Is_CardNotifiactionPL"));
					}


					formObject.RaiseEvent("WFSave");
				} 
			}
		}
		//ended    for New Card and Notification call    

		//loan call
		else if("Loan_Disbursal_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			List<List<String>> output = null;
			String scheme="";
			String acc_brnch="";
			//added by abhishek started
			PersonalLoanS.mLogger.info("PL Disbursal: " +"inside Loan Creation");
			formObject.fetchFragment("ProductContainer", "Product", "q_Product");
			int n = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			PersonalLoanS.mLogger.info("PL Disbursal: "+ "Row count: "+n);
			for(int i=0;i<n;i++)
			{
				PersonalLoanS.mLogger.info("value of requested type is"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+ 0+ 1) +"");
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				if("PL".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1)) || "Personal Loan".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1))){
					scheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);
					break;                                                          
				}
			}
			if(!"".equalsIgnoreCase(scheme) && !"null".equalsIgnoreCase(scheme)){
				try{
					String Query = "select S.SCHEMEID,S.SCHEMEDESC,S.LSM_PRODDIFFRATE,P.Code , P.grace_days,P.description,S.PRIME_TYPE from ng_MASTER_Scheme S join ng_master_product_parameter P on S.PRODUCTFLAG = P.CODE and S.SCHEMEDESC = '"+scheme+"'";
					PersonalLoanS.mLogger.info("Query to fetch from ng_master_product_parameter"+Query +"");
					output = formObject.getNGDataFromDataCache(Query);
					PersonalLoanS.mLogger.info("value ofscheme is"+scheme +"");
					if(output!=null && !output.isEmpty()){
						formObject.setNGValue("scheme_code","CNP1");
						formObject.setNGValue("Scheme_desc","PL");
						formObject.setNGValue("cmplx_LoanDisb_prod_diff_rate",output.get(0).get(2) );
						formObject.setNGValue("cmplx_LoanDisb_product_code",output.get(0).get(3) );
						formObject.setNGValue("cmplx_LoanDisb_gracedays", output.get(0).get(4));
						formObject.setNGValue("cmplx_LoanDisb_product_desc",output.get(0).get(5) );
						formObject.setNGValue("cmplx_LoanDetails_basetype",output.get(0).get(6) );

						if(formObject.isVisible("DecisionHistory_Frame1")==false){
							PersonalLoanS.mLogger.info("PL" +"Inside IsVisible check for Decision History!!");
							formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
						}

						String acc_no = formObject.getNGValue("cmplx_Decision_AccountNo");
						PersonalLoanS.mLogger.info("cmplx_Decision_AccountNo: "+acc_no +"");
						if("".equalsIgnoreCase(acc_no) || acc_no == null ){

							String qry = "select AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType = 'CURRENT ACCOUNT' and ( Child_Wi = '"+formObject.getWFWorkitemName()+"' or Wi_Name ='"+formObject.getWFWorkitemName()+"')";
							PersonalLoanS.mLogger.info("query is"+qry +"");
							List<List<String>> record = formObject.getDataFromDataSource(qry);
							if(record!=null && record.get(0)!=null && !record.isEmpty()){
								acc_no = record.get(0).get(0);
								//started By Akshay
								if("RAKislamic CURRENT ACCOUNT".equalsIgnoreCase(record.get(0).get(1))){
									acc_brnch=acc_no.substring(0,3);

								}
								else{
									acc_brnch=acc_no.substring(1,4);
								}
								//ended By Akshay
								PersonalLoanS.mLogger.info("value of accno is"+acc_no +"");
		
							}
							else{
								alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val036");//"Loan can't be created as No current account is maintained for this Customer";
								throw new ValidatorException(new FacesMessage(alert_msg));
							}	
						}
						//added By Akshay-21/6/2017	
						else{
							acc_brnch=acc_no.substring(0,3);
						}
						if(acc_no!=null && !"".equalsIgnoreCase(acc_no)){
							formObject.setNGValue("Account_Number", acc_no);//added By Akshay
							formObject.setNGValue("acct_brnch",acc_brnch);
							formObject.setNGValue("Loan_Disbursal_SourcingBranch",acc_no.substring(0,4));  

						}



						outputResponse = genX.GenerateXML("NEW_LOAN_REQ","");
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
						PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
						String ContractID =  outputResponse.contains("<contractID>") ? outputResponse.substring(outputResponse.indexOf("<contractID>")+"</contractID>".length()-1,outputResponse.indexOf("</contractID>")):"";    
						PersonalLoanS.mLogger.info("RLOS value of ContractID"+ContractID);
						if("0000".equalsIgnoreCase(ReturnCode)){
							PersonalLoanS.mLogger.info("RLOS value of Loan Request"+"value of Loan_Req inside");
  
							formObject.setNGValue("Is_LoanReq","Y");
							formObject.setNGValue("Contract_id",ContractID);
							alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val037")+ContractID; //"Contract created sucessfully with contract id: "+ContractID;
							PersonalLoanS.mLogger.info("PL Disbursal "+ "message need to be displayed: "+alert_msg);
						} 
						else{
							alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
							PersonalLoanS.mLogger.info("PL Disbursal: "+ "Error while generating new loan: "+ ReturnCode);
						}
					}
					else{
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
						PersonalLoanS.mLogger.info("PL Disbursal "+"ng_master_product_parameter is not miantained for scheme: "+scheme );
					}
					formObject.RaiseEvent("WFSave");
				}
				catch(Exception e){
					if(alert_msg.equalsIgnoreCase("")){
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
					}
					PersonalLoanS.mLogger.info("PL Disbursal: "+ "Exception occured while generating new loan: "+ e.toString()+" desc: "+e.getMessage()+" stack: "+e.getStackTrace());
					printException(e);
					throw new ValidatorException(new FacesMessage(alert_msg));	
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else{
				PersonalLoanS.mLogger.info("PL Disbursal "+"Scheme code is not maintained for this case");
			}
		}  

		else if ("CAD_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
		}

		else if ("CAD_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
		}

		else if ("CAD_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_DECCAD_cmplx_gr_DECCAD");

		}

		else if ("FinacleCore_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Dds_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_DDSgrid");
		}
		else if ("FinacleCore_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_DDSgrid");

		}
		else if ("FinacleCore_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_DDSgrid");
		}


		else if ("FinacleCore_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Inward_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_inwardtt");
		}

		else if ("FinacleCore_Button6".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_inwardtt");

		}
		else if ("FinacleCore_Button5".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_inwardtt");
		}
		else if ("FinacleCore_Button7".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGClearRow", "cmplx_FinacleCore_inwardtt");
		}

		else if("SmartCheck1_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}

		else if("SmartCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}
		else if("SmartCheck1_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}

		//Code to addd dechtec call on Decision for pl start 
		else if("DecisionHistory_Button5".equalsIgnoreCase(pEvent.getSource().getName())){	
			formObject.setNGValue("DecCallFired","Decision");
			PersonalLoanS.mLogger.info("$$Before genX.GenerateXML for dectech call..outputResponse is : "+outputResponse);
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			income_Dectech();
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			employment_dectech();
			formObject.fetchFragment("MOL", "MOL1", "q_MOL");
			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
			formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
			formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
			formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
			loadPicklistMol();
			outputResponse = genX.GenerateXML("DECTECH","");
			PersonalLoanS.mLogger.info("$$After genX.GenerateXML for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

			SystemErrorCode = outputResponse.contains("<SystemErrorCode>") ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+SystemErrorCode);
			if("".equalsIgnoreCase(SystemErrorCode)){
				ReadXml.valueSetIntegration(outputResponse);   
				alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val006");//"Decision engine integration successful";
				PersonalLoanS.mLogger.info("after value set customer for dectech call");
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));				
			}
			else{
				alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val005");//"Critical error occurred Please contact administrator";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}	
		//Code to addd dechtec call on Decision for pl end  



		else if("HomeCountryVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame7");
		}
		else if("ResidenceVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame8");
		}
		else if("BussinessVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame6");
		}
		else if("OfficeandMobileVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame11");
		}
		else if("GuarantorVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame9");
		}
		else if("ReferenceDetailVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame10");
		}
		else if("CustDetailVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame5");
		}
		else if("LoanandCard_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame13");
		}



		else if ("CustDetailVerification1_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmiratesID=formObject.getNGValue("cmplx_CustDetailverification1_EmiratesId");
			String CIFID=formObject.getNGValue("cmplx_CustDetailverification1_CIF_ID");
			PersonalLoanS.mLogger.info("PL"+ "EmiratesID$"+EmiratesID+"$");
			String query=null;
			if("".equalsIgnoreCase(EmiratesID.trim()))

				query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where CifId Like '%"+CIFID+"%'";

			else

				query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where EmiratesID Like '%"+EmiratesID + "%' or CifId Like '%"+CIFID+"'";

			PersonalLoanS.mLogger.info("PL"+ "query is: "+query);

			List<List<String>> list=formObject.getDataFromDataSource(query);

			PersonalLoanS.mLogger.info("PL"+"##Arun");
			for (List<String> a : list) 
			{

				formObject.addItemFromList("cmplx_CustDetailverification1_CustDetVer_GRID", a);
			}


		}//Arun (15/09/2017)
		//++Below code added by nikhil 13/11/2017 for Code merge
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
			//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			//SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			//formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
			//++ below code added by abhishek to add in telecom grid as per FSD 2.7.3
//++Below code added by nikhil 13/11/2017 for Code merge
			
			//++Below code added by  nikhil 10/10/17 winame added in ngaddrow
			formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
			//++above code added by  nikhil 10/10/17 winame added in ngaddrow
			Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		        PersonalLoanS.mLogger.info(" inside tele calling frame in hold_CPV"+""+ sdf.format(cal.getTime()) );
		        formObject.setNGValue("NotepadDetails_username", formObject.getUserName());
		        formObject.setNGValue("NotepadDetails_time",sdf.format(cal.getTime()) );
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
			//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			//--Above code added by nikhil 13/11/2017 for Code merge
		

			/* Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		        PersonalLoanS.mLogger.info(" inside tele calling frame in CPV" );
		        formObject.setNGValue("Tele_wi_name",formObject.getWFWorkitemName());
		        formObject.setNGValue("NotepadDetails_username", formObject.getUserName());
		        formObject.setNGValue("NotepadDetails_time",sdf.format(cal.getTime()) );
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
			//-- Above code added by abhishek to add in telecom grid as per FSD 2.7.3
*/		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
			//++ below code changed by abhishek as per FSD 2.7.3

			

			formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
			//-- Above code changed by abhishek as per FSD 2.7.3
			//++Below code added by nikhil 13/11/2017 for Code merge
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			formObject.setNGValue("NotepadDetails_Dateofcalling", modifiedDate);
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		//++ below code added by abhishek as per FSD 2.7.3
		else if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_Enable")){
			String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> list=formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info(" In CC CPV workstep eventDispatched()"+ "Enable button click::query result is::"+list );
			for(int i =0;i<list.size();i++ ){
				if(list.get(i).get(0).equalsIgnoreCase("Smart_CPV")){
					formObject.setLocked("OfficeandMobileVerification_Frame1", true);
					alert_msg = "Cannot enable as one Instance is Already at Smart CPV";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					formObject.setLocked("OfficeandMobileVerification_Frame1", false);
					formObject.setEnabled("OfficeandMobileVerification_Enable", false);
				}
			}
			//disable values fields
			formObject.setEnabled("cmplx_OffVerification_offtelno",false);
			formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
			formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
			formObject.setEnabled("cmplx_OffVerification_desig_val",false);
			formObject.setEnabled("cmplx_OffVerification_doj_val",false);
			formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);
			
			formObject.setNGValue("cmplx_OffVerification_EnableFlag", true);
			formObject.RaiseEvent("WFSave");
			
		}
		//-- Above code added by abhishek as per FSD 2.7.3
		else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button3")){
			formObject.saveFragment("Frame14");
		}
		
		//++ below code added by abhishek for non contactable button as per FSD 2.7.3

		

		else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_nonContactable")){
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
			formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
			formObject.setNGFrameState("Office_verification", 1);
			formObject.setNGFrameState("Smart_check", 1);
			formObject.setNGFrameState("Notepad_Values", 1);
			formObject.setEnabled("DecisionHistory_nonContactable", false);
			formObject.setEnabled("NotepadDetails_Frame1", false);
			formObject.setEnabled("DecisionHistory_Frame1", false);
			formObject.setEnabled("SmartCheck_Frame1", false);
			formObject.setEnabled("OfficeandMobileVerification_Frame1", false);
			
			formObject.setEnabled("DecisionHistory_cntctEstablished", true);
			
			formObject.setNGValue("cmplx_Decision_Decision","Smart CPV Hold");
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			String currDate = dateFormat.format(date);
			PersonalLoanS.mLogger.info(" In CC smartCPV.... value of current date time for non contactable is :: "+ ""+currDate);
			
			
			
			String sQuery = "insert into ng_rlos_SmartCPV_datetime (wi_name,datetime_nonContactable) values('"+formObject.getWFWorkitemName()+"','"+currDate+"')" ;
			PersonalLoanS.mLogger.info(" In CC smartCPV.... value of query for non contactable is :: "+ ""+sQuery);
			formObject.saveDataIntoDataSource(sQuery);
			formObject.setNGValue("cmplx_Decision_contactableFlag", true);
			formObject.RaiseEvent("WFSave");
			
		}
		//-- Above code added by abhishek  for non contactable button  as per FSD 2.7.3
				
		//++ below code added by abhishek for contact established button as per FSD 2.7.3
		else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_cntctEstablished")){
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
			formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
			formObject.setNGFrameState("Office_verification", 1);
			formObject.setNGFrameState("Smart_check", 1);
			formObject.setNGFrameState("Notepad_Values", 1);
			formObject.setEnabled("NotepadDetails_Frame1", true);
			formObject.setEnabled("DecisionHistory_Frame1", true);
			formObject.setEnabled("SmartCheck_Frame1", true);
			formObject.setEnabled("OfficeandMobileVerification_Frame1", true);
			
			formObject.setEnabled("DecisionHistory_cntctEstablished", false);
			formObject.setEnabled("DecisionHistory_nonContactable", true);
			formObject.setNGValue("cmplx_Decision_Decision","--Select--");
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			String currDate = dateFormat.format(date);
			PersonalLoanS.mLogger.info(" In CC smartCPV.... value of current date time for contact estblished is :: "+ ""+currDate);
			
			
			String sQuery = "insert into ng_rlos_SmartCPV_datetime (wi_name,datetime_ContactEstablished) values('"+formObject.getWFWorkitemName()+"','"+currDate+"')" ;
			PersonalLoanS.mLogger.info(" In CC smartCPV.... value of query for contactable is :: "+ ""+sQuery);
			formObject.saveDataIntoDataSource(sQuery);
			formObject.setNGValue("cmplx_Decision_contactableFlag", false);
			formObject.RaiseEvent("WFSave");
		}
		
		//-- Above code added by abhishek for contact established button as per FSD 2.7.3
		else if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1_save")){
			formObject.saveFragment("Cust_Det_Ver");
			 alert_msg="Customer Verification details saved";

			throw new ValidatorException(new FacesMessage(alert_msg));

			
		}
		
		else if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1_save")){
			formObject.saveFragment("Business_Verif");
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification_save")){
			
			formObject.saveFragment("Emp_Verification");
			//below code added by nikhil 4/12/17
			 alert_msg="Employment Verification details saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
		}
		
		else if(pEvent.getSource().getName().equalsIgnoreCase("BankingCheck_save")){
			formObject.saveFragment("Bank_Check");
			//below code added by nikhil 4/12/17
			 alert_msg="Banking Check details saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_save")){
			//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7
			
			formObject.saveFragment("Smart_chk");
			//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
			//below code added by nikhil 4/12/17
			 alert_msg="Smart Check details saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
		}
		
		//--Above code added by nikhil 13/11/2017 for Code merge
		//++Below code added by nikhil for Toteam
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_LC_Add"))
				{
					
					String lc_doc_name=formObject.getNGValue("PostDisbursal_LC_LC_Doc_Name");
					String bank_name=formObject.getNGValue("PostDisbursal_Bank_Name");
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
					ToTeam_AddtoGrids(formObject,lc_doc_name,bank_name);
				
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_LC_Modify"))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
				
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_LC_Delete"))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
				
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_MCQ_Add"))
				{
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_MCQ_Modify"))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_MCQ_Delete"))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_BG_Add"))
				{
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_BG_Modify"))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_BG_Delete"))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_NLC_Add"))
				{
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_NLC_Modify"))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_NLC_Delete"))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_Save"))
				{
					formObject.saveFragment("Post_Disbursal");
				}
				//--Above code added by nikhil for Toteam
		
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
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to autopopulate values     
	 
	***********************************************************************************  */

	public void autopopulateValues(FormReference formObject) {

		String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
		String mobNo2 = formObject.getNGValue("AlternateContactDetails_MobileNo2");
		String dob = formObject.getNGValue("cmplx_Customer_DOb");
		int adressrowcount = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		String poboxResidence = "";
		
		for(int i=0;i<adressrowcount;i++){
			String addType = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
			if("RESIDENCE".equalsIgnoreCase(addType)){
				poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
		}
		
		String officeNo = formObject.getNGValue("AlternateContactDetails_OFFICENO");
		

		setValues(formObject,mobNo1,mobNo2,dob,poboxResidence,officeNo);
		

	}

	public void setValues(FormReference formObject,String...values) {
		String[] controls = new String[]{"cmplx_CustDetailverification1_MobNo1_value","cmplx_CustDetailverification1_MobNo2_value","cmplx_CustDetailverification1_DOB_value",
				"cmplx_CustDetailverification1_PO_Box_Value","cmplx_CustDetailverification1_Off_Telephone_value"};
		int i=0;
		for(String str : values){
			if(checkValue(str)){
				formObject.setNGValue(controls[i], str);
				formObject.setLocked(controls[i], true);

			}
			else{
				PersonalLoanS.mLogger.info("value received at index "+i+" is: "+str);
			}
			i++;
		}
	}

	public boolean checkValue(String ngValue){
		if(ngValue==null ||"".equalsIgnoreCase(ngValue) ||" ".equalsIgnoreCase(ngValue) || "--Select--".equalsIgnoreCase(ngValue) || "false".equalsIgnoreCase(ngValue)){
			return false;
		}
		return true;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to populate header Fields  
	 
	***********************************************************************************  */


	public void setFormHeader(FormEvent pEvent){

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		String activityName = formObject.getWFActivityName();
		
		try{
			PersonalLoanS.mLogger.info("Inside formPopulated()" + pEvent.getSource());
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails",0);
			formObject.fetchFragment("ProductContainer", "Product", "q_Product");
			formObject.setNGFrameState("ProductContainer",0);    
			// disha FSD
			fetch_NotepadDetails();//added by akshay on 11/9/17 for fetching Notepad on form load(for validation on done click)	
			formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
			formObject.setNGValue("UserLabel",formObject.getUserName()); 

			formObject.setNGValue("IntroDateLabel",formObject.getNGValue("IntroDateTime"));
			formObject.setNGValue("InitChannelLabel",formObject.getNGValue("InitiationType"));

			formObject.setNGValue("CifLabel",formObject.getNGValue("cmplx_Customer_CIFNO"));

			formObject.setNGValue("CRNLabel",formObject.getNGValue("CRN")); 

		
			formObject.setNGValue("CurrentDateLabel",common.Convert_dateFormat("", "", "dd/MM/yyyy")); 

			String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
			PersonalLoanS.mLogger.info("Nationality is$$$$: "+nationality);
			if(!"AE".equalsIgnoreCase(nationality)){
				formObject.setLocked("cmplx_Customer_marsoomID",true);
			}
			String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
			String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
			String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");

			String fullName = fName+" "+mName+" "+lName; 
			formObject.setNGValue("CustomerLabel",fullName);     
			//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7
            try
            {
            String loanAmt = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3);
            if(loanAmt!=null)
            {
            formObject.setNGValue("LoanLabel",loanAmt);
            }
            else
            {
            	formObject.setNGValue("LoanLabel","");	
            }
            }
            catch(Exception ex)
            {
            	PersonalLoanS.mLogger.info(ex);
            }
            //--Above code added by nikhil 31/10/17 as per CC FSD 2.7

			String product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			formObject.setNGValue("ProductLabel",product);
			String sub_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			formObject.setNGValue("SubProductLabel",sub_product);
			String card_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
			formObject.setNGValue("CardLabel",card_product);

			PersonalLoanS.mLogger.info("Itemindex is:" +  formObject.getWFFolderId());
			formObject.setNGValue("AppRefLabel", formObject.getNGValue("AppRefNo"));

			//  below code already exist - 10-10-2017
			//disha FSD P1 - Top section is blank partly
			formObject.setNGValue("RmTlNameLabel", formObject.getNGValue("RM_Name")); 
			formObject.setNGValue("Cmp_Emp_Label",formObject.getNGValue("Employer_Name"));
			List<String> activity_openFragmentsonLoad = Arrays.asList("CAD_Analyst1","CAD_Analyst2","CC_Disbursal","CC_Waiver","Collection_Agent_Review","Collection_User",
					"Compliance","CPV_Analyst","CPV","CSM_Review","CSM","DDVT_Checker","Deferral_Authority","Dispatch","FCU","Hold_CPV","HR","Interest_Rate_Approval"
					,"Manager","Original_Validation","Post_Disbursal","Reject_Queue","Rejected_Application","Relationship_Manager"
					,"DSA_CSO_Review","RM_Review","Smart_CPV","Telesales_Agent_Review","ToTeam","Waiver_Authority","DDVT_maker");

			List<String> activity_setPartMatch = Arrays.asList("DDVT_maker");
			if(activity_openFragmentsonLoad.contains(activityName)){
				setActivityname(formObject,activityName);
			}
			if(activity_setPartMatch.contains(activityName)){
				setPartMatch(formObject);
			}
			

		}catch(Exception e)
		{
			printException(e);
		}
		finally
		{
 //++Below code added by  nikhil 31/10/17 as per CC FSD 2.7
            
            if(activityName.equalsIgnoreCase("FCU"))
            {
            	formObject.setVisible("Supervisor_section", false);
            	formObject.setVisible("Postdisbursal_Checklist", false);
            }
            if(activityName.equalsIgnoreCase("ToTeam"))
            {
            	formObject.setVisible("Post_Disbursal", true);
            	PersonalLoanS.mLogger.info("In TO Team Post disbursal");
            }
            else
            {
            	formObject.setVisible("Post_Disbursal", false);
            	PersonalLoanS.mLogger.info("In other than TO Team Post disbursal");
            }
          //--Above code added by nikhil 31/10/17 as per CC FSD 2.7
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set activity name      
	 
	***********************************************************************************  */

	public void setActivityname(FormReference formObject , String activityName){
		try{
			formObject.setNGValue("QueueLabel","PL_"+activityName);
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception:"+ex.getMessage());
			PLCommon.printException(ex);			
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to align the Fragments after Fetch   
	 
	***********************************************************************************  */

	public void alignfragmentsafterfetch(FormReference formObject){

		
		formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");
		if(formObject.getNGFrameState("Alt_Contact_container") == 1)
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		

		formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
		PersonalLoanS.mLogger.info("height of address is: "+formObject.getTop("Address_Details_Container")+"!!!"+formObject.getHeight("Address_Details_Container"));
		
		formObject.setTop("ReferenceDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
		PersonalLoanS.mLogger.info("height of reference is: "+formObject.getTop("ReferenceDetails")+"!!!"+formObject.getHeight("ReferenceDetails"));

		formObject.setTop("FATCA", formObject.getTop("ReferenceDetails")+30);
		PersonalLoanS.mLogger.info("height of FATCA is: "+formObject.getTop("FATCA")+"!!!"+formObject.getHeight("FATCA"));

		formObject.setTop("KYC", formObject.getTop("FATCA")+30);
		PersonalLoanS.mLogger.info("height of KYC is: "+formObject.getTop("KYC")+"!!!"+formObject.getHeight("KYC"));

		formObject.setTop("OECD", formObject.getTop("KYC")+30);
		PersonalLoanS.mLogger.info("height of OECD is: "+formObject.getTop("OECD")+"!!!"+formObject.getHeight("OECD"));

		formObject.setTop("Credit_card_Enq", formObject.getTop("OECD")+30);
		PersonalLoanS.mLogger.info("height of CreditCardEnq is: "+formObject.getTop("CreditCardEnq")+"!!!"+formObject.getHeight("CreditCardEnq"));

		formObject.setTop("Case_History", formObject.getTop("Credit_card_Enq")+30);
		PersonalLoanS.mLogger.info("height of CaseHistoryReport is: "+formObject.getTop("CaseHistoryReport")+"!!!"+formObject.getHeight("CaseHistoryReport"));

		formObject.setTop("LOS", formObject.getTop("Case_History")+30);//formObject.getHeight("Case_History")+20);
		PersonalLoanS.mLogger.info("height of LOS is: "+formObject.getTop("LOS")+"!!!"+formObject.getHeight("LOS"));

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set PartMatch     
	 
	***********************************************************************************  */

	public void setPartMatch(FormReference formObject){
		try{
			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			formObject.setNGFrameState("Part_Match",0);

			PersonalLoanS.mLogger.info("Inside Part_Match");  

		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info( "Exception:"+ex.getMessage());
			PLCommon.printException(ex);			
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Hide Liability Fields     
	 
	***********************************************************************************  */

	public void hideLiabilityAddFields(FormReference formObject) {

		formObject.setVisible("Liability_New_delinin3",false);
		formObject.setVisible("Liability_New_DPD30inlast6",false);
		formObject.setVisible("Liability_New_DPD30inlast18",false);
		formObject.setVisible("Liability_New_Label2",false);
		formObject.setVisible("Liability_New_writeoff",false);
		formObject.setVisible("Liability_New_Label4",false);
		formObject.setVisible("Liability_New_worststatuslast24",false);
		formObject.setVisible("Liability_New_Label8",false);
		formObject.setVisible("Liability_New_Text3",false);
	}

	
	public void fetch_NotepadDetails()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");

		if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
			formObject.setVisible("NotepadDetails_Label6", true);
			formObject.setVisible("NotepadDetails_insqueue", true);
			formObject.setVisible("NotepadDetails_inscompletion", true);
			formObject.setVisible("NotepadDetails_Label10", true);
			formObject.setVisible("NotepadDetails_Actdate", true);
			formObject.setVisible("NotepadDetails_Label11", true);
			formObject.setVisible("NotepadDetails_Actusername", true);
			formObject.setVisible("NotepadDetails_Label9", true);
			formObject.setVisible("NotepadDetails_ActuserRemarks", true);
			//formObject.setHeight("NotepadDetails_Frame1", 400);//Arun (11/9/17)
			//formObject.setTop("NotepadDetails_save",420);//Arun (11/9/17)
			LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription");
			formObject.setNGValue("NotepadDetails_notedesc","--Select--");
			formObject.setLocked("NotepadDetails_notecode", true);

		}else{
			formObject.setEnabled("NotepadDetails_Frame1", false);
		}
	}
	public void autopopulateValuesEmployeesVeri(){
		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("PL"+ "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
		List<String> LoadPicklist_Verification= Arrays.asList("cmplx_EmploymentVerification_fixedsal_ver","cmplx_EmploymentVerification_AccomProvided_ver","cmplx_EmploymentVerification_Desig_ver","cmplx_EmploymentVerification_doj_ver","cmplx_EmploymentVerification_confirmedinJob_ver","cmplx_EmploymentVerification_LoanFromCompany_ver","cmplx_EmploymentVerification_PermanentDeductSal_ver","cmplx_EmploymentVerification_SmartCheck_ver","cmplx_EmploymentVerification_FiledVisitedInitiated_ver");
	 	LoadPicklistVerification(LoadPicklist_Verification);
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate1 != 0){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		}
		int framestate2=formObject.getNGFrameState("IncomeDEtails");
		if(framestate2 != 0){
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
		}
		int framestate3=formObject.getNGFrameState("EmploymentDetails");
		if(framestate3 != 0){
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
		}
		int framestate4=formObject.getNGFrameState("Cust_Det_Ver");
		if(framestate4 !=0){
			formObject.fetchFragment("Cust_Det_Ver", "CustDetailVerification1", "q_CustDetailVeriFCU");
			}
		formObject.setNGValue("cmplx_EmploymentVerification_OffTelNo",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
		formObject.setLocked("cmplx_EmploymentVerification_OffTelNo", true);
		formObject.setNGValue("cmplx_EmploymentVerification_fixedsal_val",formObject.getNGValue("cmplx_IncomeDetails_netSal1"));
		formObject.setLocked("cmplx_EmploymentVerification_fixedsal_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_AccomProvided_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		formObject.setLocked("cmplx_EmploymentVerification_AccomProvided_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		formObject.setLocked("cmplx_EmploymentVerification_doj_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_Desig_val",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		formObject.setLocked("cmplx_EmploymentVerification_Desig_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_FiledVisitedInitiated_value",formObject.getNGValue("cmplx_CustDetailverification1_Filed_Visit_value"));
		formObject.setLocked("cmplx_EmploymentVerification_FiledVisitedInitiated_value", true);
		String ConfirmedInJob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");
		if(ConfirmedInJob.equalsIgnoreCase("false")){
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val","No");
			PersonalLoanS.mLogger.info("PL"+"in false");
		}
		else{
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val","Yes");
			PersonalLoanS.mLogger.info("PL"+"in true");
		}
		formObject.setLocked("cmplx_EmploymentVerification_confirmedinJob_val", true);
		if(framestate4==1)
		{
		formObject.setNGFrameState("Cust_Det_Ver", 1);
		formObject.setTop("Bank_Check", formObject.getTop("EmploymentVerification_Frame1")+formObject.getHeight("EmploymentVerification_Frame1")+10);
		}
}

	public void LockFragmentsOnLoad(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("Inside LockFragmentsOnLoad()");
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1",true);
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("IncomeDetails_Frame1",true);
		} 

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExtLiability_Frame1",true);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanDetails_Frame1",true);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("AltContactDetails_Frame1",true);
		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FATCA_Frame6",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame1",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("OECD_Frame8",true);
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CustDetailVerification_Frame1",true);
		}

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("BussinessVerification_Frame1",true);
		}

		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("HomeCountryVerification_Frame1",true);
		}

		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ResidenceVerification_Frame1",true);
		}

		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
		}

		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
		}

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}
		
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {	

			formObject.setLocked("Compliance_Frame1",true);
			formObject.setLocked("Compliance_CompRemarks",false);			            
			formObject.setLocked("Compliance_Modify",false); 
			formObject.setLocked("Compliance_Save",false);
			formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);



			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");

			formObject.setNGFrameState("World_Check",0);
			int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
			PersonalLoanS.mLogger.info("CC value of world check row--->value of n "+n);
			if(n>0)
			{ 
				String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,12);
				PersonalLoanS.mLogger.info("CC value of world check UID"+UID);
				formObject.setNGValue("Compliance_UID",UID);
				String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,15);
				PersonalLoanS.mLogger.info("CC value of world check EI "+EI);
				formObject.setNGValue("Compliance_EI",EI);
				String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,0);
				PersonalLoanS.mLogger.info("CC value of world check Name"+Name);	
				formObject.setNGValue("Compliance_Name",Name);
				String Dob = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,2);
				PersonalLoanS.mLogger.info("CC value of world check Dob"+Dob);
				formObject.setNGValue("Compliance_DOB",Dob);
				String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,7);
				PersonalLoanS.mLogger.info("Citizenship"+Citizenship);	
				formObject.setNGValue("Compliance_Citizenship",Citizenship);
				String Remarks = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,16);
				formObject.setNGValue("Compliance_Remarks",Remarks);
				String Id_No = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,1);
				formObject.setNGValue("Compliance_IdentificationNumber",Id_No);
				String Age = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,3);
				formObject.setNGValue("Compliance_Age",Age);
				String Position = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,4);
				formObject.setNGValue("Compliance_Positon",Position);
				String Deceased = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,5); 
				formObject.setNGValue("Compliance_Deceased",Deceased);
				String Category = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,6);
				formObject.setNGValue("Compliance_Category",Category);
				String Location = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,8);
				formObject.setNGValue("Compliance_Location",Location);
				String Identification = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,9); 
				formObject.setNGValue("Compliance_Identification",Identification);
				String Biography = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,10); 
				formObject.setNGValue("Compliance_Biography",Biography);
				String Reports = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,11); 
				formObject.setNGValue("Compliance_Reports",Reports);
				String Entered_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,13);
				formObject.setNGValue("Compliance_EntertedDate",Entered_Date);
				String Updated_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,14);
				formObject.setNGValue("Compliance_UpdatedDate",Updated_Date);
				String Document = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,18);  
				formObject.setNGValue("Compliance_Document",Document);
				String Decision = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,19);
				formObject.setNGValue("Compliance_Decision",Decision);
				String Match_Rank = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,20);
				formObject.setNGValue("Compliance_MatchRank",Match_Rank);
				String Alias = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,21);
				formObject.setNGValue("Compliance_Alias",Alias);
				String birth_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,22);
				formObject.setNGValue("Compliance_BirthCountry",birth_Country);
				String resident_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,23);
				formObject.setNGValue("Compliance_ResidenceCountry",resident_Country);
				String Address = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,24);
				formObject.setNGValue("Compliance_Address",Address);
				String Gender = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,25);
				formObject.setNGValue("Compliance_Gender",Gender);
				String Listed_On = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,26);
				formObject.setNGValue("Compliance_ListedOn",Listed_On);
				String Program = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,27);
				formObject.setNGValue("Compliance_Program",Program);
				String external_ID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,28);
				formObject.setNGValue("Compliance_ExternalID",external_ID);
				String data_Source = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,29);
				formObject.setNGValue("Compliance_DataSource",data_Source);
				formObject.setNGValue("Compliance_winame",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");
				formObject.RaiseEvent("WFSave");
			}
			formObject.setNGFrameState("World_Check",1);
		}
		
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SmartCheck_Frame1",true);
		}
		//below code added by yash for PL - POST DISBURSAL on 27/11/2017
		else if ("DispatchFrag".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("DispatchFrag_Frame1",true);
		}
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
		}
		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanDetails_Frame1",true);
		}
		
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Compliance_Frame1",true);
		}

		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FCU_Decision_Frame1",true);
		}
		
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("NotepadDetails_Frame1",true);
			notepad_load();
			notepad_withoutTelLog();
		}
		
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			formObject.setVisible("DecisionHistory_Label8", false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
			formObject.setVisible("DecisionHistory_Label9", false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
			formObject.setVisible("DecisionHistory_Label5", false);
			formObject.setVisible("cmplx_Decision_desc", false);
			formObject.setVisible("DecisionHistory_Label3", false);
			formObject.setVisible("cmplx_Decision_strength", false);
			formObject.setVisible("DecisionHistory_Label4", false);
			formObject.setVisible("cmplx_Decision_weakness", false);
			formObject.setVisible("cmplx_Decision_IBAN", false);
			formObject.setVisible("cmplx_Decision_AccountNo", false);
			formObject.setVisible("DecisionHistory_chqbook", false);
			formObject.setVisible("DecisionHistory_Label6", false);
			formObject.setVisible("DecisionHistory_Label7", false);
			formObject.setVisible("DecisionHistory_Label11", true);
			formObject.setVisible("DecisionHistory_DecisionReasonCode", true);

			formObject.setVisible("DecisionHistory_Button4",false);
			formObject.setVisible("cmplx_Decision_Deviationcode",false);
			formObject.setVisible("DecisionHistory_Label14",false);
			formObject.setVisible("cmplx_Decision_Dectech_decsion",false);
			formObject.setVisible("DecisionHistory_Label15",false);
			formObject.setVisible("cmplx_Decision_score_grade",false);
			formObject.setVisible("DecisionHistory_Label16",false);
			formObject.setVisible("cmplx_Decision_Highest_delegauth",false);
			formObject.setVisible("cmplx_Decision_Manual_Deviation",false);
			formObject.setVisible("DecisionHistory_Button6",false);
			formObject.setVisible("cmplx_Decision_Manual_deviation_reason",false);
			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setVisible("cmplx_Decision_refereason",false);
			formObject.setVisible("DecisionHistory_Label1",false);
			formObject.setVisible("DecisionHistory_ReferTo", false); //Arun (01-12-17) to hide this combo
			formObject.setVisible("Decision_Label1",false);
			formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
			
			//formObject.setTop("Decision_Label1", 10);
			//formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 25);
			formObject.setTop("Decision_Label3", 10);
			formObject.setLeft("Decision_Label3", 25);
			formObject.setLeft("cmplx_Decision_Decision", 25);
			formObject.setTop("cmplx_Decision_Decision", 25);
			formObject.setTop("DecisionHistory_Label11", 10);
			formObject.setTop("DecisionHistory_DecisionReasonCode",25);
			formObject.setLeft("DecisionHistory_Label11", 350);
			formObject.setLeft("DecisionHistory_DecisionReasonCode", 350);
			formObject.setTop("Decision_Label4",10);
			formObject.setTop("cmplx_Decision_REMARKS", 25);
			formObject.setLeft("Decision_Label4", 672);
			formObject.setLeft("cmplx_Decision_REMARKS", 672);
			formObject.setTop("Decision_ListView1", 100);
			formObject.setTop("DecisionHistory_save", 280);
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory",formObject.getHeight("DecisionHistory_Frame1")+30);

		}
	}

}