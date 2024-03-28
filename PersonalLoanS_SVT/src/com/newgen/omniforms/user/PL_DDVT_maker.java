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

import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
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
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
			formObject.setVisible("Card_Details", true);
			}

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
			//below code added by nikhil 08/12/17
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_Customer_MobNo",false);
			}
			else
			{
				formObject.setLocked("cmplx_Customer_MobNo",true);
			}
			//below code added by nikhil 
			 formObject.setLocked("cmplx_Customer_IsGenuine",true);
			 if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
					formObject.setVisible("cmplx_Customer_EIDARegNo",true);
					formObject.setVisible("Customer_Label56",true);
					//formObject.setLocked("cmplx_Customer_EIDARegNo",true);
				}
				else {
					formObject.setVisible("Customer_Label56",false);
					formObject.setVisible("cmplx_Customer_EIDARegNo",false);
				}
			 if(formObject.getNGValue("cmplx_Customer_age")!=null && !formObject.getNGValue("cmplx_Customer_age").equals("") && Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"))<21){
				 formObject.setNGValue("cmplx_Customer_minor", true);
			 }
		}

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {                                                                                                
			//LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			formObject.setLocked("EmpType", true);
			formObject.setLocked("Scheme", true);
			//loadPicklistProduct("Personal Loan");
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
			
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			formObject.setVisible("cmplx_IncomeDetails_compaccAmt", false);//IncomeDetails_Label16
			formObject.setVisible("IncomeDetails_Label16", false);
			formObject.setVisible("cmplx_IncomeDetails_Totavgother", false);
			formObject.setVisible("IncomeDetails_Label15", false);
		}

		else if ("IncomeDEtails".equalsIgnoreCase(pEvent.getSource().getName())) {

			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
			PersonalLoanS.mLogger.info( "Emp Type Value is:"+EmpType);

			if("Salaried".equalsIgnoreCase(EmpType)|| NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(EmpType))
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

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(EmpType))
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
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			

		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			 formObject.setLocked("Liability_New_fetchLiabilities",true);
			 formObject.setLocked("takeoverAMount",true);
			 formObject.setLocked("cmplx_Liability_New_DBR",true);
			 formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			 formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			 formObject.setLocked("cmplx_Liability_New_TAI",true);
			 formObject.setLocked("ExtLiability_Frame1", true);
			 LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			 LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_contract_type with (nolock) where isactive='Y' order by code");

		 }

		/* else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
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
		 }*/


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
			 formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
				
			 formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
				
			 formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			 formObject.setLocked("cmplx_EligibilityAndProductInfo_InstrumentType",true);
			 formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
				
			 formObject.setEnabled("EMploymentDetails_Save",true);
			 if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
				 formObject.setVisible("cmplx_EmploymentDetails_NepType",false);	
				 formObject.setVisible("EMploymentDetails_Label25",false);
			 }
			 if(!NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
				 formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				 formObject.setVisible("EMploymentDetails_Label36",false);
			 }
			 formObject.setLocked("EMploymentDetails_Text21", false);
			 formObject.setLocked("EMploymentDetails_Text22", false);
			 formObject.setEnabled("EMploymentDetails_Text21", true);
			 formObject.setEnabled("EMploymentDetails_Text22", true);
			 formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
				formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
			 loadPicklist4();
		 }

		

		 else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			 LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
			 LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", "select convert(varchar, description),code from NG_MASTER_InstrumentType with (nolock) where isactive = 'Y'  order by code");
				 LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
			 formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","M");
			 //change by saurabh on 11 nov 2017.
			 formObject.setEnabled("ELigibiltyAndProductInfo_Button1", false);
			 //++below code added by nikhil 5/12/17 for ddvt issues
			 formObject.setTop("ELigibiltyAndProductInfo_Save", 150);
			 formObject.setLeft("ELigibiltyAndProductInfo_Button1", 54);
			 String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			 if(appType.contains("TKO")){
					PersonalLoanS.mLogger.info("inside take condition");
					formObject.setVisible("ELigibiltyAndProductInfo_Label10", true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", true);
					formObject.setVisible("ELigibiltyAndProductInfo_Label9", true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
					/*formObject.setLocked("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
					formObject.setEnabled("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
					
					formObject.setLocked("cmplx_EligibilityAndProductInfo_takeoverBank", false);
					formObject.setEnabled("cmplx_EligibilityAndProductInfo_takeoverBank", true);*/
					
					formObject.setLeft("ELigibiltyAndProductInfo_Label10", formObject.getLeft("ELigibiltyAndProductInfo_Label5"));
					formObject.setTop("ELigibiltyAndProductInfo_Label10", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
					formObject.setLeft("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getLeft("ELigibiltyAndProductInfo_Label10"));
					formObject.setTop("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
					formObject.setLeft("ELigibiltyAndProductInfo_Label9", formObject.getLeft("ELigibiltyAndProductInfo_Label6"));
					formObject.setTop("ELigibiltyAndProductInfo_Label9", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
					formObject.setLeft("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getLeft("ELigibiltyAndProductInfo_Label9"));
					formObject.setTop("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
					
				}
				else{
					PersonalLoanS.mLogger.info("inside else take condition");
					formObject.setVisible("ELigibiltyAndProductInfo_Label10", false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label9", false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
				}
			 	
			 
			 //below code added by jnikhil 12/12/17
			 formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			 formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalDBR", false);
			 formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalTAI", false);
			 formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalLimit", false);
			 formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestRate", false);
			 formObject.setEnabled("cmplx_EligibilityAndProductInfo_EMI", false);
			 formObject.setEnabled("cmplx_EligibilityAndProductInfo_Tenor", false);
			 formObject.setVisible("ELigibiltyAndProductInfo_Refresh",false);

		 }


		 else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//below code commented by nikhil 06/12/17
			// loanvalidate();//Arun (21/09/17)
			//formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
			 loadPicklist_LoanDetails();
			//added By Tarang for drop 4 point 8 started on 08/03/2018
			String qry = "select * from ng_MASTER_LoanVAT with (nolock)";
			PersonalLoanS.mLogger.info("query is"+qry +"");
			List<List<String>> record = formObject.getDataFromDataSource(qry);
			if(record!=null && record.get(0)!=null && !record.isEmpty()){
				if("Loan Processing VAT".equalsIgnoreCase(record.get(0).get(0))){
					int loanProcPercent=Integer.parseInt(record.get(0).get(1));
					formObject.setNGValue("cmplx_LoanDetails_LoanProcessingVATPercent", loanProcPercent);
				}
				if("Insurance VAT".equalsIgnoreCase(record.get(1).get(0))){
					int insurancePercent=Integer.parseInt(record.get(1).get(1));
					formObject.setNGValue("cmplx_LoanDetails_InsuranceVATPercent", insurancePercent);
				}
			}
			loanvalidate();//modified by akshay on 14/10/17 for displaying values in loan details
			//added By Tarang for drop 4 point 8 ended on 08/03/2018
					String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");//Arun (21/09/17)
			String mName = formObject.getNGValue("cmplx_Customer_MiddleName");//Arun (21/09/17)
			String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");//Arun (21/09/17)
			String fullName = fName+" "+mName+" "+lName; //Arun (21/09/17)
			formObject.setNGValue("cmplx_LoanDetails_name",fullName);//Arun (21/09/17)
			formObject.setLocked("cmplx_LoanDetails_fdisbdate",true);//Arun (22/09/17)
			formObject.setLocked("cmplx_LoanDetails_frepdate",true);//Arun (22/09/17)
			
			//below code added by nikhil 12/12/17
			formObject.setNGValue("cmplx_LoanDetails_status", "R");
			formObject.setNGValue("cmplx_LoanDetails_paymode", "T");
			//below code added by nikhil 20/12/17
			formObject.setNGValue("cmplx_LoanDetails_insplan", "E");
			formObject.setLocked("cmplx_LoanDetails_maturitydate", true);
			/*formObject.setLocked("LoanDetails_disbdate", true);
			formObject.setLocked("LoanDetails_payreldate", true);
			formObject.setLocked("cmplx_LoanDetails_paidon", true);
			formObject.setLocked("cmplx_LoanDetails_trandate", true);
			formObject.setLocked("cmplx_LoanDetails_pdtpref", true);*/
			formObject.setLocked("cmplx_LoanDetails_ageatmaturity", true);
			formObject.setLocked("cmplx_LoanDetails_insuramt", true);
			formObject.setLocked("cmplx_LoanDetails_baserate", true);
			formObject.setLocked("cmplx_LoanDetails_repfreq", true);
			formObject.setNGValue("cmplx_LoanDetails_repfreq", "M");
			
			if("Q".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDetails_paymode")))
			{
				formObject.setLocked("cmplx_LoanDetails_chqno", false);
				formObject.setLocked("cmplx_LoanDetails_chqdat", false);
			}
			else
			{
				formObject.setLocked("cmplx_LoanDetails_chqno", true);
				formObject.setLocked("cmplx_LoanDetails_chqdat", true);
			}
			formObject.setLocked("cmplx_LoanDetails_LoanProcessingVATPercent", true);//added By Tarang for drop 4 point 8 on 08/03/2018
			formObject.setLocked("cmplx_LoanDetails_InsuranceVATPercent", true);//added By Tarang for drop 4 point 8 on 08/03/2018

			}
		//shifted from PLCOmmonCode to here by akshay on 17/1/18
		 else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("RLOS Initiation Inside IncomingDocuments");                                                            
			formObject.setVisible("IncomingDoc_UploadSig",false);
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomingDoc_ViewSIgnature",true);
				formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
			}
			else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
				
			}
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
			LoadpicklistAltcontactDetails();
			
		}
		//below code added by nikhil for proc-3304
		else if("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferenceDetails_Frame1", false);
		}
		

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");
			//added By Tarang for drop 4 point 3 started on 21/02/2018
			LoadPickList("SupplementCardDetails_CardProduct","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
			LoadPickList("SupplementCardDetails_EmploymentStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmploymentStatus with (nolock)");
			LoadPickList("SupplementCardDetails_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			//added By Tarang for drop 4 point 3 ended on 21/02/2018
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			Loadpicklistfatca();	
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_KYC();}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPickListOECD();}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			int framestate_Alt_Contact = formObject.getNGFrameState("Alt_Contact_container");
			if(framestate_Alt_Contact == 0){
				//CreditCard.mLogger.info("Alternate details alread fetched");
			}
			else {
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				//CreditCard.mLogger.info("fetched Alternate Contact details");
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+20);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
	
			}
			partMatchValues();
			//added by akshay on 27/12/17
			if(!"Y".equals(formObject.getNGValue("Is_PartMatchSearch"))){
				formObject.setLocked("PartMatch_Blacklist", true);
			}
		}

		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setVisible("IncomingDoc_UploadSig",false);
		}----Duplicate event*/
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			
	}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadpicklistCardDetails();
			IslamicFieldsvisibility();
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist1();
			fetch_desesionfragment();
			
			//for decision fragment made changes 8th dec 2017
	
			PersonalLoanS.mLogger.info("***********Inside decision history");
			fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
			
		/*	formObject.setNGValue("cmplx_Decision_Decision", "--Select--");  
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
			formObject.setTop("DecisionHistory_save",360);*/
			
			//for decision fragment made changes 8th dec 2017
			
			//Arun (11/09/17)
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_New_CIFNo"))) 
			{  //commented because this should't be visible at dddvt maker
				//formObject.setVisible("DecisionHistory_Button2",true);
				//formObject.setLeft("DecisionHistory_Button2",1000);
				
				//for decision fragment made changes 8th dec 2017
			/*	formObject.setVisible("DecisionHistory_Label6",true);
				formObject.setVisible("cmplx_Decision_IBAN",true);
				formObject.setVisible("DecisionHistory_Label10",true);
				formObject.setVisible("cmplx_Decision_New_CIFNo",true);
				formObject.setVisible("DecisionHistory_Label7",true);
				formObject.setVisible("cmplx_Decision_AccountNo",true);*/
				
				PersonalLoanS.mLogger.info("***********Inside decision history");
				fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#DecisionHistory_Button2#\n#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
				PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
				formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			/*	formObject.setTop("DecisionHistory_Label6",56);
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
				formObject.setTop("DecisionHistory_save",410);  */ //Arun (11/09/17)
				
				//for decision fragment made changes 8th dec 2017
			}
		}             

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("WorldCheck1_age",true);
			// Changes done to load master values in world check birthcountry and residence country
			loadPicklist_WorldCheck();
		}
		//            started merged code
		else if ("Finacle_Core".equalsIgnoreCase(pEvent.getSource().getName())){
			//below code added by nikhil for ddvt issues
			formObject.setLocked("FinacleCore_Frame6",true);
			formObject.setLocked("FinacleCore_Frame6",true);

			/*try{
				//nelow query changed by nikhil 12/12/17
				String query="select AcctType,'',AcctId,AcctNm,AccountOpenDate,AcctStat,'',AvailableBalance,'','','' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getNGDataFromDataCache(query);
				for(List<String> mylist : list_acc)
				{
					PersonalLoanS.mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
				}

				query="select '',LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_lien=formObject.getNGDataFromDataCache(query);
				for(List<String> mylist : list_lien)
				{
					PersonalLoanS.mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
				}
				query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_SIDet=formObject.getNGDataFromDataCache(query);
				for(List<String> mylist : list_SIDet)
				{
					PersonalLoanS.mLogger.info("PL DDVT Maker-->Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
				}

			}

			catch(Exception e){
				printException(e);
			}*/
		} 
		//below code added by nikhil 12/12/17
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//below code added by nikhil for ddvt issues
			formObject.setLocked("FinacleCore_Frame6",true);
			formObject.setLocked("FinacleCore_Frame6",true);
		}
		
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNO");
			String DOB=formObject.getNGValue("cmplx_Customer_DOb");
			String passport=formObject.getNGValue("cmplx_Customer_PAssportNo");
			try{
				String queryForRejectCases="select isnull(customername,''),isnull(employer,''),passportNo,FORMAT(uploaddate,'dd/MM/yyyy'),isnull(category,''),remarks from ng_cas_rejected_table where reject_system='CAS' and cif='"+customer_cif+"'and PassportNo='"+passport+"' and FORMAT(dob,'dd/MM/yyyy')='"+DOB+"'";
				mLogger.info("queryForRejectCases: "+queryForRejectCases);
				List<List<String>> records=formObject.getNGDataFromDataCache(queryForRejectCases);
				mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_RejectEnq_RejectEnqGrid",record);
					}
				}
			}catch(Exception e)
			{
				mLogger.info(e.getMessage());
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

		if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED"))||NGFUserResourceMgr_PL.getGlobalVar("PL_Yes").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) )
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
		LoadReferGrid();//added by akshay on 9/12/17
		
		saveIndecisionGrid();
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

