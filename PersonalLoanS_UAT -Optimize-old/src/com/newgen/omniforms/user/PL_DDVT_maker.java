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
		mLogger.info("PL DDVT MAKER  Inside formLoaded()" + pEvent.getSource().getName());

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

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		mLogger.info("In PL DDVT MAKER FRAGMENT_LOADED eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {

			formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
			formObject.setLocked("cmplx_Customer_NEP",true);
			formObject.setLocked("cmplx_Customer_marsoomID",true);
			formObject.setLocked("Customer_Button1",true);
			formObject.setLocked("cmplx_Customer_age",true);
			formObject.setLocked("cmplx_Customer_SecNationality",true);
			formObject.setLocked("cmplx_Customer_GCCNational",true);

			loadPicklistCustomer();
			mLogger.info("Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {                                                                                                
			//LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			loadPicklistProduct("Personal Loan");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
			LoadPickList("title", "select '--Select--' union select convert(varchar, description) from NG_MASTER_title with (nolock)");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			formObject.setLocked("IncomeDetails_Frame1", true);//Arun (11/9/17)
			formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
			formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
			formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
			//formObject.setLocked("cmplx_IncomeDetails_netSal1", true);
			//formObject.setLocked("cmplx_IncomeDetails_netSal2", true);
			//formObject.setLocked("cmplx_IncomeDetails_netSal3", true);
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

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {

			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
			mLogger.info( "Emp Type Value is:"+EmpType);

			if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
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

			else if(EmpType.equalsIgnoreCase("Self Employed"))
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

		//changes for fragments disabled on liability
		/*else  if (pEvent.getSource().getName().equalsIgnoreCase("InternalExternalLiability")) {
            		mLogger.info("Inside PL DDVT MAKER eventDispatched()"+ "inside intextliac");
    				formObject.setLocked("ExtLiability_Frame1", true);
    			} 
                //changes for fragments disabled on liability    
		 */                
		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			 formObject.setLocked("Liability_New_fetchLiabilities",true);
			 formObject.setLocked("takeoverAMount",true);
			 formObject.setLocked("cmplx_Liability_New_DBR",true);
			 formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			 formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			 formObject.setLocked("cmplx_Liability_New_TAI",true);
			 formObject.setLocked("ExtLiability_Frame1", true);
		 }

		 else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
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

		 else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
			 formObject.setLocked("CIFNo", true);
			 formObject.setLocked("AuthorisedSignDetails_Button4", true);
			 LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			 LoadPickList("SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		 }

		 else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
			 LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		 }


		 else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {

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
			 if(!formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true")){
				 formObject.setVisible("cmplx_EmploymentDetails_NepType",false);	
				 formObject.setVisible("EMploymentDetails_Label25",false);
			 }
			 if(!formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCE") && !formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCN")){
				 formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				 formObject.setVisible("EMploymentDetails_Label36",false);
			 }

			 /*formObject.setVisible("EMploymentDetails_Label25",false);
            formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
            formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
            formObject.setVisible("EMploymentDetails_Label62",false);
            formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
            formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
            formObject.setVisible("EMploymentDetails_Label5",false);
            formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
            formObject.setVisible("EMploymentDetails_Label59",false);
            formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
            formObject.setVisible("EMploymentDetails_Label36",false);
            formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
            formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
            formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
            formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);*/
			 //loadPicklist_Employment();
			 loadPicklist4();
		 }

		 else if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
			 formObject.setLocked("cmplx_MiscFields_School",true);
			 formObject.setLocked("cmplx_MiscFields_PropertyType",true);
			 formObject.setLocked("cmplx_MiscFields_RealEstate",true);
			 formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
		 }


		 else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {

			 LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
			 LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' union select convert(varchar, description) from NG_MASTER_instrumentType");
			 LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
			 formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
			 //formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);//Arun (11/08/17)
			 //formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);//Arun (11/08/17)
			 /*formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
            formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
            formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
            formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);

            formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_Insurance",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_NumberOfInstallment",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_BAseRate",true);         
            formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
            formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);*/

		 }


		 else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
			loanvalidate();//Arun (21/09/17)
			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
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

		 else if (pEvent.getSource().getName().equalsIgnoreCase("Inc_Doc")) {
			mLogger.info("RLOS Initiation Inside IncomingDocuments");                                                            
			mLogger.info("RLOS Initiation Inside fetchIncomingDocRepeater");
			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			mLogger.info("RLOS Initiation eventDispatched() formObject.fetchFragment1 after fetching");
			fetchIncomingDocRepeater();
			mLogger.info("RLOS Initiation eventDispatched() formObject.fetchFragment1");

		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			mLogger.info("PL DDVT MAKER  Inside load Address details");
			//loadAddressDetails();
			loadPicklist_Address();
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
			formObject.setVisible("AlternateContactDetails_custdomicile",false);
			formObject.setVisible("AltContactDetails_Label14",false);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
			LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
			LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch"))
		{
			LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			partMatchValues();
			/*formObject.setLocked("PartMatch_fname",true);
            formObject.setLocked("PartMatch_lname",true);
            formObject.setLocked("PartMatch_funame",true);
            formObject.setLocked("PartMatch_newpass",true);
            formObject.setLocked("PartMatch_oldpass",true);
            formObject.setLocked("PartMatch_visafno",true);
            formObject.setLocked("PartMatch_Dob",true);
            formObject.setLocked("PartMatch_nationality",true);*/
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc")) {
			formObject.setVisible("IncomingDoc_UploadSig",false);
		}
		// disha FSD
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

			formObject.setLocked("NotepadDetails_Frame1",true);
			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info("PL notepad Activity name is:" + sActivityName);
			int user_id = formObject.getUserId();
			String user_name = formObject.getUserName();
			user_name = user_name+"-"+user_id;					
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
			formObject.setNGValue("NotepadDetails_Actusername",user_name); 
			formObject.setNGValue("NotepadDetails_user",user_name); 
			formObject.setLocked("NotepadDetails_noteDate",true);
			formObject.setLocked("NotepadDetails_Actusername",true);
			formObject.setLocked("NotepadDetails_user",true);
			formObject.setLocked("NotepadDetails_insqueue",true);
			formObject.setLocked("NotepadDetails_Actdate",true);
			formObject.setVisible("NotepadDetails_save",true);
			formObject.setLocked("NotepadDetails_notecode",true);
			formObject.setHeight("NotepadDetails_Frame1",450);
			formObject.setTop("NotepadDetails_save",400);
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
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
			formObject.setLeft("DecisionHistory_Label1",555);
			formObject.setLeft("cmplx_Decision_refereason",555);
			formObject.setLeft("DecisionHistory_Rejreason",813);
			formObject.setLeft("cmplx_Decision_rejreason",813);
			formObject.setLeft("DecisionHistory_Button1",1000);

			formObject.setTop("Decision_Label1",8);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",24);
			formObject.setTop("Decision_Label3",8);
			formObject.setTop("cmplx_Decision_Decision",24);
			formObject.setTop("DecisionHistory_Label1",8);
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
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")) 
			{
				formObject.setVisible("DecisionHistory_Button2",true);
				formObject.setLeft("DecisionHistory_Button2",1000);
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

		else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")){
			formObject.setLocked("WorldCheck1_age",true);
			// Changes done to load master values in world check birthcountry and residence country
			loadPicklist_WorldCheck();
		}
		//            started merged code
		else if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")){

			try{
				String query="select AcctType,'',AcctId,AcctNm,AccountOpenDate,AcctStat,'',AvailableBalance,'','','' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
				}

				query="select '',LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_lien=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_lien)
				{
					mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
				}
				query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_SIDet)
				{
					mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
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
		mLogger.info("Inside PL DDVT MAKER eventDispatched() EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());		

		switch(pEvent.getType())
		{              
		case FRAME_EXPANDED:
			mLogger.info("In PL_Iniation FRAME_EXPANDED eventDispatched() "+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		// TODO Auto-generated method stub

	}


	public void initialize() {
		// TODO Auto-generated method stub

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

		if(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED").equalsIgnoreCase("Yes") )
		{
			mLogger.info("CC val change "+ "Inside Y of CPV required");
			formObject.setNGValue("cpv_required","Y");
		}
		else
		{
			mLogger.info("CC val change "+ "Inside N of CPV required");
			formObject.setNGValue("cpv_required","N");
		}
		//incoming doc function
		IncomingDoc();
		//incoming doc function
		saveIndecisionGrid();
	}


	public void fetch_desesionfragment()throws ValidatorException{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setVisible("DecisionHistory_Button3",false);
		formObject.setVisible("DecisionHistory_updcust",false);
		formObject.setVisible("DecisionHistory_chqbook",false);
		formObject.setVisible("cmplx_Decision_waiveoffver",false);
		loadPicklist3();
		//commented by Saurabh on 15th Oct as func was being called twice.
		//loadInDecGrid();
		mLogger.info("PL DDVT Maker  inside fetch fragment DecisionHistory END");
	}  

}

