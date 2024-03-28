/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_DDVT_maker.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                                                                                                                        : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

1.                 9-6-2017      Disha         Changes done to auto populate age in world check fragment
2.                                                               9-6-2017      Disha         Changes done to load master values in world check birthcountry and residence country
------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.util.HashMap;
import java.util.List;


import org.apache.log4j.Logger;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_DDVT_maker extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	
	Logger mLogger=PersonalLoanS.mLogger;
	

	public void formLoaded(FormEvent pEvent)
	{
		PersonalLoanS.mLogger.info("PL DDVT MAKER  Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{  

		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);  

		}catch(Exception e)
		{
			printException(e);
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("In PL DDVT MAKER FRAGMENT_LOADED eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
			formObject.setLocked("cmplx_Customer_NEP",true);
			formObject.setLocked("cmplx_Customer_marsoomID",true);
			formObject.setLocked("Customer_Button1",true);
			formObject.setLocked("cmplx_Customer_age",true);
			formObject.setLocked("cmplx_Customer_SecNationality",true);
			formObject.setLocked("cmplx_Customer_GCCNational",true);
			//below code by saurabh on 11 nov 2017.
			formObject.setVisible("Customer_Label32",false);
			formObject.setVisible("cmplx_Customer_corpcode",false);
			loadPicklistCustomer();
			PersonalLoanS.mLogger.info("Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
		}

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {                                                                                                
			//LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			loadPicklistProduct("Personal Loan");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("title", "select '--Select--' union select convert(varchar, description) from NG_MASTER_title with (nolock)");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1", true);//Arun (11/9/17)
			formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
			formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
			formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
			
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal", true);
			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Commission_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Flying_Avg", true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		}

		else if ("IncomeDEtails".equalsIgnoreCase(pEvent.getSource().getName())) {

			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
			PersonalLoanS.mLogger.info( "Emp Type Value is:"+EmpType);

			if("Salaried".equalsIgnoreCase(EmpType)|| "Salaried Pensioner".equalsIgnoreCase(EmpType))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 630);
				formObject.setHeight("IncomeDetails_Frame1", 605);      
				if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
					formObject.setVisible("IncomeDetails_Label11", false);
					formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", false);
					formObject.setVisible("IncomeDetails_Label13", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", false);
					formObject.setVisible("IncomeDetails_Label3", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", false);
				}              
				else{
					formObject.setVisible("IncomeDetails_Label11", true);
					formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", true);
					formObject.setVisible("IncomeDetails_Label13", true);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", true);
					formObject.setVisible("IncomeDetails_Label3", true);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", true);
				}              
			}

			else if("Self Employed".equalsIgnoreCase(EmpType))
			{                                                                                                              
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 300);
				formObject.setHeight("IncomeDetails_Frame1", 280);
				if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
					formObject.setVisible("IncomeDetails_Label20", false);
					formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", false);
					formObject.setVisible("IncomeDetails_Label22", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", false);
					formObject.setVisible("IncomeDetails_Label35", false);
					formObject.setVisible("IncomeDetails_Label5", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", false);
					formObject.setVisible("IncomeDetails_Label36", true);
				}              
				else{
					formObject.setVisible("IncomeDetails_Label20", true);
					formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", true);
					formObject.setVisible("IncomeDetails_Label22", true);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", true);
					formObject.setVisible("IncomeDetails_Label35", true);
					formObject.setVisible("IncomeDetails_Label5", true);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", true);
					formObject.setVisible("IncomeDetails_Label36", true);
				}              
			}

		}                              

		
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			 formObject.setLocked("Liability_New_fetchLiabilities",true);
			 formObject.setLocked("takeoverAMount",true);
			 formObject.setLocked("cmplx_Liability_New_DBR",true);
			 formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			 formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			 formObject.setLocked("cmplx_Liability_New_TAI",true);
			 formObject.setLocked("ExtLiability_Frame1", true);
		 }

		 else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			 formObject.setLocked("cif", true);
			 formObject.setLocked("CompanyDetails_Button3", true);
			 LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
			 LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			 LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			 LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			 LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
			 LoadPickList("desig", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Designation with (nolock)");
			 LoadPickList("desigVisa", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Designation with (nolock)");
			 LoadPickList("EOW", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
			 LoadPickList("headOffice", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
		 }

		 else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			 formObject.setLocked("CIFNo", true);
			 formObject.setLocked("AuthorisedSignDetails_Button4", true);
			 LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			 LoadPickList("SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		 }

		 else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			 LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		 }


		 else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			 formObject.setLocked("EMploymentDetails_Frame1",true);
			 formObject.setLocked("cmplx_EmploymentDetails_ApplicationCateg",false);
			 formObject.setLocked("cmplx_EmploymentDetails_targetSegCode",false);
			 formObject.setLocked("cmplx_EmploymentDetails_marketcode",false);
			 formObject.setLocked("cmplx_EmploymentDetails_collectioncode",false);
			 formObject.setLocked("cmplx_EmploymentDetails_PromotionCode",false);
			 formObject.setLocked("cmplx_EmploymentDetails_MIS",false);
			 formObject.setEnabled("EMploymentDetails_Button2",true);
			 formObject.setLocked("cmplx_EmploymentDetails_NepType",false);
			 formObject.setLocked("cmplx_EmploymentDetails_channelcode",false);
			 //change by saurabh on 11 nov 17.
			 formObject.setEnabled("EMploymentDetails_Save",true);
			 if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
				 formObject.setVisible("cmplx_EmploymentDetails_NepType",false);	
				 formObject.setVisible("EMploymentDetails_Label25",false);
			 }
			 if(!"RESCE".equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !"RESCN".equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
				 formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				 formObject.setVisible("EMploymentDetails_Label36",false);
			 }

			
			 loadPicklist4();
		 }

		 else if ("MiscellaneousFields".equalsIgnoreCase(pEvent.getSource().getName())) {
			 formObject.setLocked("cmplx_MiscFields_School",true);
			 formObject.setLocked("cmplx_MiscFields_PropertyType",true);
			 formObject.setLocked("cmplx_MiscFields_RealEstate",true);
			 formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
		 }


		 else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			 LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
			 LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' union select convert(varchar, description) from NG_MASTER_instrumentType");
			 LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
			 formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
			 //change by saurabh on 11 nov 2017.
			 formObject.setEnabled("ELigibiltyAndProductInfo_Button1", false);
			 //++below code added by nikhil 5/12/17 for ddvt issues
			 formObject.setTop("ELigibiltyAndProductInfo_Save", 150);

		 }


		 else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			 //Below code commented by nikhil 06/12/17
			//loanvalidate();//Arun (21/09/17)
			//formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
			LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_Master_InstallmentPlan with (nolock) order by Code");
			LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_COLLECTIONBRANCH with (nolock) order by code");
			LoadPickList("cmplx_LoanDetails_ddastatus", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_DDASTATUS with (nolock) order by code");
			LoadPickList("LoanDetails_modeofdisb", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ModeofDisbursal with (nolock) order by code");
			LoadPickList("LoanDetails_disbto", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
			LoadPickList("LoanDetails_holdcode", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_HoldCode with (nolock) order by code");
			LoadPickList("cmplx_LoanDetails_paymode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_PAYMENTMODE with (nolock)");
			LoadPickList("cmplx_LoanDetails_status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_STATUS with (nolock)");
			LoadPickList("cmplx_LoanDetails_bankdeal", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
			LoadPickList("cmplx_LoanDetails_city", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
			String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");//Arun (21/09/17)
			String mName = formObject.getNGValue("cmplx_Customer_MiddleName");//Arun (21/09/17)
			String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");//Arun (21/09/17)
			String fullName = fName+" "+mName+" "+lName; //Arun (21/09/17)
			formObject.setNGValue("cmplx_LoanDetails_name",fullName);//Arun (21/09/17)
			formObject.setLocked("cmplx_LoanDetails_chqno",true);//Arun (22/09/17)
			formObject.setLocked("cmplx_LoanDetails_chqdat",true);//Arun (22/09/17)

		}

		 else if ("Inc_Doc".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("RLOS Initiation Inside IncomingDocuments");                                                            
			PersonalLoanS.mLogger.info("RLOS Initiation Inside fetchIncomingDocRepeater");
			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			PersonalLoanS.mLogger.info("RLOS Initiation eventDispatched() formObject.fetchFragment1 after fetching");
			fetchIncomingDocRepeater();
			PersonalLoanS.mLogger.info("RLOS Initiation eventDispatched() formObject.fetchFragment1");

		}


		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("PL DDVT MAKER  Inside load Address details");
			//loadAddressDetails();
			loadPicklist_Address();
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//commented by saurabh on 10th nov
			
		}
		

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
			LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			partMatchValues();
			
		}

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setVisible("IncomingDoc_UploadSig",false);
		}
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			
	}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist1();
			fetch_desesionfragment();
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");  
			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setVisible("DecisionHistory_Label9",false);
			formObject.setVisible("DecisionHistory_Label8",false);
			formObject.setVisible("DecisionHistory_Label7",false);
			formObject.setVisible("DecisionHistory_Label6",false);
			formObject.setVisible("cmplx_Decision_IBAN",false);
			formObject.setVisible("cmplx_Decision_AccountNo",false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber",false);
			formObject.setLeft("Decision_Label1",24);
			formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED",24);
			formObject.setLeft("Decision_Label3",297);
			formObject.setLeft("cmplx_Decision_Decision",297);
			formObject.setLeft("DecisionHistory_Label1",950);
			formObject.setLeft("DecisionHistory_ReferTo",950);//Arun (03-12-17) to align left
			formObject.setLeft("cmplx_Decision_refereason",555);
			formObject.setLeft("DecisionHistory_Rejreason",813);
			formObject.setLeft("cmplx_Decision_rejreason",813);
			formObject.setLeft("DecisionHistory_Button1",1000);

			formObject.setTop("Decision_Label1",8);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",24);
			formObject.setTop("Decision_Label3",8);
			formObject.setTop("cmplx_Decision_Decision",24);
			formObject.setTop("DecisionHistory_Label1",8);
			formObject.setTop("DecisionHistory_ReferTo",24);//Arun (01-12-17) to align top of this field
			formObject.setTop("cmplx_Decision_refereason",24);
			formObject.setTop("DecisionHistory_Rejreason",8);
			formObject.setTop("cmplx_Decision_rejreason",24);
			formObject.setTop("DecisionHistory_Button1",8);                                                                                                	
			formObject.setTop("DecisionHistory_Label5",56);
			formObject.setTop("cmplx_Decision_desc",72);
			formObject.setTop("DecisionHistory_Label3",56);
			formObject.setTop("cmplx_Decision_strength",72);
			formObject.setTop("DecisionHistory_Label4",56);
			formObject.setTop("cmplx_Decision_weakness",72);
			formObject.setTop("Decision_Label4",56);
			formObject.setTop("cmplx_Decision_REMARKS",72);                                                                                                	
			formObject.setTop("Decision_ListView1",170);
			formObject.setTop("DecisionHistory_save",360);//Arun (11/09/17)
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))) 
			{  //commented because this should't be visible at dddvt maker
				//formObject.setVisible("DecisionHistory_Button2",true);
				//formObject.setLeft("DecisionHistory_Button2",1000);
				formObject.setVisible("DecisionHistory_Label6",true);
				formObject.setVisible("cmplx_Decision_IBAN",true);
				formObject.setVisible("DecisionHistory_Label10",true);
				formObject.setVisible("cmplx_Decision_New_CIFNo",true);
				formObject.setVisible("DecisionHistory_Label7",true);
				formObject.setVisible("cmplx_Decision_AccountNo",true);
				formObject.setTop("DecisionHistory_Label6",56);
				formObject.setTop("cmplx_Decision_IBAN",72);
				formObject.setTop("DecisionHistory_Label10",56);
				formObject.setTop("cmplx_Decision_New_CIFNo",72);
				formObject.setTop("DecisionHistory_Label7",56);
				formObject.setTop("cmplx_Decision_AccountNo",72);
				formObject.setTop("DecisionHistory_Label5",104);
				formObject.setTop("cmplx_Decision_desc",120);
				formObject.setTop("DecisionHistory_Label3",104);
				formObject.setTop("cmplx_Decision_strength",120);
				formObject.setTop("DecisionHistory_Label4",104);
				formObject.setTop("cmplx_Decision_weakness",120);
				formObject.setTop("Decision_Label4",104);
				formObject.setTop("cmplx_Decision_REMARKS",120); 
				formObject.setTop("Decision_ListView1",220);
				formObject.setTop("DecisionHistory_save",410);//Arun (11/09/17)
			}
		}             

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("WorldCheck1_age",true);
			// Changes done to load master values in world check birthcountry and residence country
			loadPicklist_WorldCheck();
		}
		//            started merged code
		else if ("Finacle_Core".equalsIgnoreCase(pEvent.getSource().getName())){

			try{
				String query="select AcctType,'',AcctId,AcctNm,AccountOpenDate,AcctStat,'',AvailableBalance,'','','' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					PersonalLoanS.mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
				}

				query="select '',LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_lien=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_lien)
				{
					PersonalLoanS.mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
				}
				query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_SIDet)
				{
					PersonalLoanS.mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
				}

			}

			catch(Exception e){
				printException(e);
			}
		}              

	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		PersonalLoanS.mLogger.info("Inside PL DDVT MAKER eventDispatched() EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());		

		switch(pEvent.getType())
		{              
		case FRAME_EXPANDED:
			PersonalLoanS.mLogger.info("In PL_Iniation FRAME_EXPANDED eventDispatched() "+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
			break;

		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;


		case VALUE_CHANGED:
			new PersonalLoanSCommonCode().value_Change(pEvent);					
			break;

		default: break;

		}

	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// empty method

	}


	public void initialize() {
		// empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// empty method

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// empty method

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// empty method
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

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
		//incoming doc function
		IncomingDoc();
		//incoming doc function
		saveIndecisionGrid();
		//saveIndecisionGridCSM();//Arun (01-12-17) for Decision history to appear in the grid
	}


	public void fetch_desesionfragment()throws ValidatorException{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setVisible("DecisionHistory_Button3",false);
		formObject.setVisible("DecisionHistory_updcust",false);
		formObject.setVisible("DecisionHistory_chqbook",false);
		formObject.setVisible("cmplx_Decision_waiveoffver",false);
		loadPicklist3();
	
		PersonalLoanS.mLogger.info("PL DDVT Maker  inside fetch fragment DecisionHistory END");
	}  

}

