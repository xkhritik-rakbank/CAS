/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CSM.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.                 12-6-2017     Disha	       Common function for decision fragment textboxes and combo visibility
------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_CSM extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	//Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		
		PersonalLoanS.mLogger.info("PL CSM insidde-->Inside formLoaded()" + pEvent.getSource().getName());
		
		
	}


	public void formPopulated(FormEvent pEvent) 
	{
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		
		
		try{

			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			formObject.setVisible("ReferHistory",false);
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

	public void fragment_loaded(ComponentEvent pEvent)
	{	
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();

		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistCustomer();

			formObject.setEnabled("Customer_save",true);
			formObject.setLocked("cmplx_Customer_ReferrorCode",false);
			formObject.setLocked("cmplx_Customer_ReferrorName",false);
			formObject.setLocked("cmplx_Customer_AppType",false);
			formObject.setVisible("cmplx_Customer_corpcode",false);
			formObject.setVisible("cmplx_Customer_bankwithus",false);
			formObject.setVisible("cmplx_Customer_noofdependent",false);
			formObject.setVisible("cmplx_Customer_guardian",false);
			formObject.setVisible("cmplx_Customer_minor",false);
			formObject.setVisible("cmplx_Customer_guarname",false);
			formObject.setVisible("cmplx_Customer_guarcif",false);
			formObject.setVisible("Customer_Label32",false);
			formObject.setVisible("Customer_Label33",false);
			formObject.setVisible("Customer_Label34",false);
			formObject.setVisible("Customer_Label37",false);
			formObject.setVisible("Customer_Label35",false);
			formObject.setVisible("Customer_Label44",false);
			/*formObject.setEnabled("Customer_Reference_Add",true);
			formObject.setEnabled("Customer_Reference__modify",true);
			formObject.setEnabled("Customer_Reference_delete",true);
*/
			// Below code added by abhishek on 08/11/2017
			formObject.setVisible("Customer_Label22", false);
			formObject.setVisible("cmplx_Customer_apptype", false);
			formObject.setVisible("Customer_Label55", true);
			formObject.setVisible("cmplx_Customer_marsoomID", true);
			// Above code added by abhishek on 08/11/2017

			formObject.setLocked("FetchDetails",true);
			formObject.setLocked("Customer_Button1",true);
			PersonalLoanS.mLogger.info("Column Added in Repeater"+" for eida");
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				//formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
			formObject.setEnabled("cmplx_Customer_IsGenuine",false);
			formObject.setEnabled("cmplx_Customer_NTB",false);
			PersonalLoanS.mLogger.info("Column Added in Repeater"+" after eida");

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			//LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");

			formObject.setEnabled("Product_Save",true);
			//	formObject.setEnabled("Product_Add",true);
			formObject.setEnabled("Product_Modify",true);
			//	formObject.setEnabled("Product_Delete",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setEnabled("IncomeDetails_Salaried_Save",true);	
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setLocked("IncomeDetails_FinacialSummarySelf",true);		

			formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);

			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Is_tenancy_contract", true);//added by akshay on 8/1/17
			formObject.setHeight("Incomedetails", 700);
			formObject.setHeight("IncomeDetails_Frame1", 720);     
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setVisible("cmplx_Liability_New_overrideAECB",false);//arun 20/12/17
			formObject.setVisible("cmplx_Liability_New_overrideIntLiab",false);//arun 20/12/17
			formObject.setEnabled("Liability_New_AECBReport",true);
			formObject.setEnabled("Liability_New_Save",true);
			//added by saurabh on 11 nov 17
			formObject.setLocked("ExtLiability_AECBReport",true);
			formObject.setLocked("ExtLiability_Button1",true);	
			formObject.setLocked("Liability_New_Button1",true);
			formObject.setLocked("cmplx_Liability_New_overrideIntLiab", true);
			formObject.setLocked("cmplx_Liability_New_overrideAECB", true);
			formObject.setVisible("Liability_New_Label6",false);
			formObject.setVisible("cmplx_Liability_New_noofpaidinstallments",false);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			formObject.setVisible("ExtLiability_AECBReport",false);
			//below code added by abhishek for CSM point 9 on 7/11/2017
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("ExtLiability_Takeoverindicator"))){
				formObject.setEnabled("ExtLiability_takeoverAMount", true);
			}else{
				formObject.setEnabled("ExtLiability_takeoverAMount", false);
			}
			//above code added by abhishek for CSM point 9 on 7/11/2017
			//below code added by abhishek for CSM point 8 on 7/11/2017
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("ExtLiability_CACIndicator"))){
				formObject.setEnabled("ExtLiability_QCAmt", true);
				formObject.setEnabled("ExtLiability_QCEMI", true);
			}else{
				formObject.setEnabled("ExtLiability_QCAmt", false);
				formObject.setEnabled("ExtLiability_QCEMI", false);
			}
			//above code added by abhishek for CSM point 8 on 7/11/2017

			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");
			// Added by aman to load the picklist	
		}
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//disha FSD
			PersonalLoanS.mLogger.info( "Inside value empdet" + pEvent.getSource().getName());

			//String empid="AVI,MED,EDU,HOT,PROM";	
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);

			formObject.setVisible("EMploymentDetails_Label10",false);
			formObject.setVisible("cmplx_EmploymentDetails_marketcode",false);

			formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",false);
			formObject.setVisible("EMploymentDetails_Label7",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
			formObject.setVisible("EMploymentDetails_Label27",false);
			formObject.setVisible("cmplx_EmploymentDetails_MIS",false);
			formObject.setVisible("EMploymentDetails_Label28",false);
			formObject.setVisible("cmplx_EmploymentDetails_collectioncode",false);
			formObject.setVisible("EMploymentDetails_Label22",false);
			formObject.setVisible("cmplx_EmploymentDetails_PromotionCode",false);

			formObject.setEnabled("cmplx_EmploymentDetails_Others", true);
			 formObject.setLocked("cmplx_EmploymentDetails_Others",false);
			formObject.setVisible("EMploymentDetails_Label4",false);
			formObject.setVisible("cmplx_EmploymentDetails_StaffID",false);
			formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("cmplx_EmploymentDetails_Dept",false);
			formObject.setVisible("EMploymentDetails_Label6",false);
			formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate",false);
			formObject.setVisible("EMploymentDetails_Label15",false);
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt",false);
			formObject.setVisible("EMploymentDetails_Label12",false);
			formObject.setVisible("cmplx_EmploymentDetails_categexpat",false);
			formObject.setVisible("EMploymentDetails_Label13",false);
			formObject.setVisible("cmplx_EmploymentDetails_categnational",false);
			formObject.setVisible("EMploymentDetails_Label14",false);
			formObject.setVisible("cmplx_EmploymentDetails_categcards",false);
			formObject.setVisible("EMploymentDetails_Label18",false);
			formObject.setVisible("cmplx_EmploymentDetails_ownername",false);
			formObject.setVisible("EMploymentDetails_Label9",false);
			formObject.setVisible("cmplx_EmploymentDetails_NOB",false);
			formObject.setVisible("cmplx_EmploymentDetails_accpvded",false);
			formObject.setVisible("EMploymentDetails_Label17",false);
			formObject.setVisible("cmplx_EmploymentDetails_authsigname",false);
			formObject.setVisible("cmplx_EmploymentDetails_highdelinq",false);
			formObject.setVisible("EMploymentDetails_Label20",false);
			formObject.setVisible("cmplx_EmploymentDetails_dateinPL",false);
			formObject.setVisible("EMploymentDetails_Label21",false);
			formObject.setVisible("cmplx_EmploymentDetails_dateinCC",false);
			formObject.setVisible("EMploymentDetails_Label26",false);
			formObject.setVisible("cmplx_EmploymentDetails_remarks",false);
			formObject.setVisible("EMploymentDetails_Label16",false);
			formObject.setVisible("cmplx_EmploymentDetails_Remarks_PL",false);
			formObject.setVisible("EMploymentDetails_Label11",false);												

			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			formObject.setVisible("EMploymentDetails_Label36",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
			formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			formObject.setLocked("EMploymentDetails_Button1",true);
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);

			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
			formObject.setLocked("cmplx_EmploymentDetails_Freezone",true); //Arun (26/09/17)
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true); //Arun (26/09/17)
			formObject.setTop("EMploymentDetails_Frame2",54);
			formObject.setTop("EMploymentDetails_Label71",240);
			formObject.setTop("cmplx_EmploymentDetails_EmpContractType",256);
			formObject.setTop("cmplx_EmploymentDetails_Kompass",256);
			formObject.setLeft("cmplx_EmploymentDetails_Kompass",1066);								

			formObject.setHeight("EMploymentDetails_Frame2",382);
			formObject.setTop("EMploymentDetails_Save",500);//Arun (24/09/17)
			formObject.setTop("EMploymentDetails_Button1",450);
			formObject.setVisible("cmplx_EmploymentDetails_employer_type",true);
			formObject.setVisible("EMploymentDetails_Label29",true);												
			formObject.setTop("EMploymentDetails_Label29",110);
			formObject.setTop("cmplx_EmploymentDetails_employer_type",125);
			formObject.setLeft("EMploymentDetails_Label29",815);	
			formObject.setLeft("cmplx_EmploymentDetails_employer_type",815);	
			
			loadPicklist4();
			Fields_ApplicationType_Employment();

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_NEP").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))){
				formObject.setVisible("EMploymentDetails_Label25",true);
				formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
			}



			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_TEN").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",true);
				formObject.setVisible("EMploymentDetails_Label7",true);
			}

			else if("S".equals(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")) )
			{
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
				formObject.setVisible("EMploymentDetails_Label59",true);
				formObject.setLeft("cmplx_EmploymentDetails_IndusSeg",1066);
				formObject.setLeft("EMploymentDetails_Label59",1066);

			}
			String requested_application;
			PersonalLoanS.mLogger.info("inside Employment Details of CSM");
			requested_application= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			PersonalLoanS.mLogger.info("inside Employment Details of CSM"+requested_application);
			if("Reschedulment".equalsIgnoreCase(requested_application)){
				formObject.setVisible("EMploymentDetails_Label36", true);
				formObject.setTop("EMploymentDetails_Label36", 54);
				formObject.setLeft("cmplx_EmploymentDetails_channelcode", 555);
				formObject.setLeft("EMploymentDetails_Label36", 555);
				formObject.setTop("cmplx_EmploymentDetails_channelcode", 70);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode", true);
				formObject.setEnabled("cmplx_EmploymentDetails_channelcode", true);
			}
			PersonalLoanS.mLogger.info("inside Channel code visibility at CSM");


		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("EMI value is: "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
			String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			PersonalLoanS.mLogger.info("appTyp value is: "+appType);
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));

			//formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
			//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
			formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestRate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_Tenor", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", false);
			//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
			formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
			formObject.setTop("ELigibiltyAndProductInfo_Frame7",648);//Arun (24/09/17)
			formObject.setTop("ELigibiltyAndProductInfo_Save",830);//Arun (24/09/17)
			formObject.setHeight("ELigibiltyAndProductInfo_Frame1", 1350);
			//below code added by abhishek for CSM point 16 on 7/11/2017
			//changes done by saurabh on 11 nov17
			formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestType",true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_RepayFreq",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_Moratorium", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_Moratorium", true);
			//above code added by abhishek for CSM point 16 on 7/11/2017

			//below code added by abhishek for CSM point 15 on 7/11/2017
			formObject.setLeft("ELigibiltyAndProductInfo_Label8", formObject.getLeft("ELigibiltyAndProductInfo_Label4"));
			formObject.setTop("ELigibiltyAndProductInfo_Label8", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_NetPayout", formObject.getLeft("ELigibiltyAndProductInfo_Label4"));
			formObject.setTop("cmplx_EligibilityAndProductInfo_NetPayout", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
			//by saurabh on 11 nov 17.
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType2 cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));

			if(appType.contains("TOP")){
				PersonalLoanS.mLogger.info("inside top condition");
				formObject.setVisible("ELigibiltyAndProductInfo_Label8", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", true);
			}
			else{
				PersonalLoanS.mLogger.info("inside else top condition");
				formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
			}
			
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType3 cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));

			formObject.setLeft("ELigibiltyAndProductInfo_Label10", formObject.getLeft("ELigibiltyAndProductInfo_Label5"));
			formObject.setTop("ELigibiltyAndProductInfo_Label10", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getLeft("ELigibiltyAndProductInfo_Label10"));
			formObject.setTop("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
			loadEligibilityData();
			formObject.setLeft("ELigibiltyAndProductInfo_Label9", formObject.getLeft("ELigibiltyAndProductInfo_Label6"));
			formObject.setTop("ELigibiltyAndProductInfo_Label9", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getLeft("ELigibiltyAndProductInfo_Label9"));
			formObject.setTop("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
			//above code added by abhishek for CSM point 15 on 7/11/2017
			//below code by saurabh on 11 nov 17.
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType 4cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
			
			
			if(appType.contains("TKO")){
				PersonalLoanS.mLogger.info("inside take condition");
				formObject.setVisible("ELigibiltyAndProductInfo_Label10", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label9", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
				formObject.setEnabled("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
				
				formObject.setLocked("cmplx_EligibilityAndProductInfo_takeoverBank", false);
				formObject.setEnabled("cmplx_EligibilityAndProductInfo_takeoverBank", true);
			}
			else{
				PersonalLoanS.mLogger.info("inside else take condition");
				formObject.setVisible("ELigibiltyAndProductInfo_Label10", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label9", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
			}
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType6 cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
			PersonalLoanS.mLogger.info("EMI value is: "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
			//formObject.setLocked("cmplx_EligibilityAndProductInfo_NetPayout", false);
			//formObject.setEnabled("cmplx_EligibilityAndProductInfo_NetPayout", true);
			formObject.setVisible("ELigibiltyAndProductInfo_Refresh",false);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();

			formObject.setEnabled("AddressDetails_Save",true);
			formObject.setEnabled("AddressDetails_addr_Add",true);
			formObject.setEnabled("AddressDetails_addr_Modify",true);
			formObject.setEnabled("AddressDetails_addr_Delete",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){


			formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
			LoadpicklistAltcontactDetails();
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			Loadpicklistfatca();
			formObject.setEnabled("FATCA_Save",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){


			formObject.setEnabled("KYC_Save",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			loadPickListOECD();

			formObject.setEnabled("OECD_Save",true);
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//disableButtonsCC("SupplementCardDetails");
			//added by akshay on 9/1/18 for proc 3507
			formObject.setVisible("SupplementCardDetails_Label10",false);
			formObject.setVisible("SupplementCardDetails_Title",false);
			formObject.setVisible("SupplementCardDetails_Label11",false);
			formObject.setVisible("SupplementCardDetails_PassportExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label21",false);
			formObject.setVisible("SupplementCardDetails_NonResident",false);
			formObject.setVisible("SupplementCardDetails_Label8",false);
			formObject.setVisible("SupplementCardDetails_PersonalInfo",false);
			formObject.setVisible("SupplementCardDetails_Label17",false);
			formObject.setVisible("SupplementCardDetails_EmiratesIDExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label19",false);
			formObject.setVisible("SupplementCardDetails_Profession",false);
			formObject.setVisible("SupplementCardDetails_Label20",false);
			formObject.setVisible("SupplementCardDetails_DLNo",false);
			formObject.setVisible("SupplementCardDetails_Label26",false);
			formObject.setVisible("SupplementCardDetails_EmailID",false);
			formObject.setVisible("SupplementCardDetails_Label27",false);
			formObject.setVisible("SupplementCardDetails_VisaNo",false);
			formObject.setVisible("SupplementCardDetails_Label23",false);
			formObject.setVisible("SupplementCardDetails_VisaExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label24",false);
			formObject.setVisible("SupplementCardDetails_AppRefNo",false);
			formObject.setVisible("SupplementCardDetails_Label25",false);
			formObject.setVisible("SupplementCardDetails_ReceivedDate",false);
			formObject.setVisible("SupplementCardDetails_Label29",false);
			formObject.setVisible("SupplementCardDetails_MarketingCode",false);
			formObject.setVisible("SupplementCardDetails_Label30",false);
			formObject.setVisible("SupplementCardDetails_ApprovalLevelCode",false);
			formObject.setVisible("SupplementCardDetails_Label28",false);
			formObject.setVisible("SupplementCardDetails_FeeProfile",false);
			formObject.setVisible("SupplementCardDetails_Label32",false);
			formObject.setVisible("SupplementCardDetails_SendSMS",false);
			formObject.setVisible("SupplementCardDetails_Label33",false);
			formObject.setVisible("SupplementCardDetails_PartnerRefNo",false);
			formObject.setVisible("SupplementCardDetails_Label31",false);
			formObject.setVisible("SupplementCardDetails_ApprovedLimit",false);
			formObject.setVisible("SupplementCardDetails_Label12",false);
			formObject.setVisible("SupplementCardDetails_CRN",false);
			formObject.setVisible("SupplementCardDetails_Label16",false);
			formObject.setVisible("SupplementCardDetails_ECRN",false);
			formObject.setTop("SupplementCardDetails_Add", formObject.getTop("SupplementCardDetails_CompEmbName")+formObject.getHeight("SupplementCardDetails_CompEmbName")+30);
			formObject.setTop("SupplementCardDetails_Modify", formObject.getTop("SupplementCardDetails_CompEmbName")+formObject.getHeight("SupplementCardDetails_CompEmbName")+30);
			formObject.setTop("SupplementCardDetails_Delete", formObject.getTop("SupplementCardDetails_CompEmbName")+formObject.getHeight("SupplementCardDetails_CompEmbName")+30);
			formObject.setTop("SupplementCardDetails_cmplx_supplementGrid", formObject.getTop("SupplementCardDetails_Add")+formObject.getHeight("SupplementCardDetails_Add")+20);
			formObject.setTop("SupplementCardDetails_Save", formObject.getTop("SupplementCardDetails_cmplx_supplementGrid")+formObject.getHeight("SupplementCardDetails_cmplx_supplementGrid")+20);
			//added by akshay on 9/1/18 for proc 3507
			formObject.setVisible("SupplementCardDetails_Label10",false);
			formObject.setVisible("SupplementCardDetails_Title",false);
			formObject.setVisible("SupplementCardDetails_Label11",false);
			formObject.setVisible("SupplementCardDetails_PassportExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label21",false);
			formObject.setVisible("SupplementCardDetails_NonResident",false);
			formObject.setVisible("SupplementCardDetails_Label8",false);
			formObject.setVisible("SupplementCardDetails_PersonalInfo",false);
			formObject.setVisible("SupplementCardDetails_Label17",false);
			formObject.setVisible("SupplementCardDetails_EmiratesIDExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label19",false);
			formObject.setVisible("SupplementCardDetails_Profession",false);
			formObject.setVisible("SupplementCardDetails_Label20",false);
			formObject.setVisible("SupplementCardDetails_DLNo",false);
			formObject.setVisible("SupplementCardDetails_Label26",false);
			formObject.setVisible("SupplementCardDetails_EmailID",false);
			formObject.setVisible("SupplementCardDetails_Label27",false);
			formObject.setVisible("SupplementCardDetails_VisaNo",false);
			formObject.setVisible("SupplementCardDetails_Label23",false);
			formObject.setVisible("SupplementCardDetails_VisaExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label24",false);
			formObject.setVisible("SupplementCardDetails_AppRefNo",false);
			formObject.setVisible("SupplementCardDetails_Label25",false);
			formObject.setVisible("SupplementCardDetails_ReceivedDate",false);
			formObject.setVisible("SupplementCardDetails_Label29",false);
			formObject.setVisible("SupplementCardDetails_MarketingCode",false);
			formObject.setVisible("SupplementCardDetails_Label30",false);
			formObject.setVisible("SupplementCardDetails_ApprovalLevelCode",false);
			formObject.setVisible("SupplementCardDetails_Label28",false);
			formObject.setVisible("SupplementCardDetails_FeeProfile",false);
			formObject.setVisible("SupplementCardDetails_Label32",false);
			formObject.setVisible("SupplementCardDetails_SendSMS",false);
			formObject.setVisible("SupplementCardDetails_Label33",false);
			formObject.setVisible("SupplementCardDetails_PartnerRefNo",false);
			formObject.setVisible("SupplementCardDetails_Label31",false);
			formObject.setVisible("SupplementCardDetails_ApprovedLimit",false);
			formObject.setVisible("SupplementCardDetails_Label12",false);
			formObject.setVisible("SupplementCardDetails_CRN",false);
			formObject.setVisible("SupplementCardDetails_Label16",false);
			formObject.setVisible("SupplementCardDetails_ECRN",false);
			formObject.setTop("SupplementCardDetails_Add", formObject.getTop("SupplementCardDetails_CompEmbName")+formObject.getHeight("SupplementCardDetails_CompEmbName")+30);
			formObject.setTop("SupplementCardDetails_Modify", formObject.getTop("SupplementCardDetails_CompEmbName")+formObject.getHeight("SupplementCardDetails_CompEmbName")+30);
			formObject.setTop("SupplementCardDetails_Delete", formObject.getTop("SupplementCardDetails_CompEmbName")+formObject.getHeight("SupplementCardDetails_CompEmbName")+30);
			formObject.setTop("SupplementCardDetails_cmplx_supplementGrid", formObject.getTop("SupplementCardDetails_Add")+formObject.getHeight("SupplementCardDetails_Add")+20);
			formObject.setTop("SupplementCardDetails_Save", formObject.getTop("SupplementCardDetails_cmplx_supplementGrid")+formObject.getHeight("SupplementCardDetails_cmplx_supplementGrid")+20);
			formObject.setHeight("SupplementCardDetails_Frame1", formObject.getTop("SupplementCardDetails_Save")+formObject.getHeight("SupplementCardDetails_Save")+20);
			formObject.setHeight("Supplementary_Cont",formObject.getHeight("SupplementCardDetails_Frame1")+20);
			//adjustFrameTops("Card_Details,Supplementary_Cont,FATCA,KYC,OECD,Reference_Details");//added by akshay on 9/1/17
			
		}
		else if("CardDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setVisible("CardDetails_BankName", false);
			formObject.setVisible("CardDetails_Label9", false);
			formObject.setVisible("CardDetails_ChequeNumber", false);
			formObject.setVisible("CardDetails_Label10", false);
			formObject.setVisible("CardDetails_Amount", false);
			formObject.setVisible("CardDetails_Label11", false);
			formObject.setVisible("CardDetails_Date", false);
			formObject.setVisible("CardDetails_Label13", false);
			formObject.setVisible("cmplx_CardDetails_CustClassification", false);
			formObject.setVisible("CardDetails_Button2", false);
			formObject.setVisible("CardDetails_Button3", false);
			formObject.setVisible("CardDetails_Button4", false);
			formObject.setVisible("cmplx_CardDetails_cmpmx_gr_cardDetails", false);
			formObject.setVisible("CardDetails_CardProduct", false);
			formObject.setVisible("CardDetails_Label14", false);
			formObject.setVisible("CardDetails_ECRN", false);
			formObject.setVisible("CardDetails_Label15", false);
			formObject.setVisible("CardDetails_Label16", false);
			formObject.setVisible("CardDetails_CRN", false);
			formObject.setVisible("CardDetails_TransactionFP", false);
			formObject.setVisible("CardDetails_Label17", false);
			formObject.setVisible("CardDetails_InterestFP", false);
			formObject.setVisible("CardDetails_Label18", false);
			formObject.setVisible("CardDetails_Label19", false);
			formObject.setVisible("CardDetails_FeeProfile", false);
			formObject.setVisible("CardDetails_Button1", false);
			formObject.setVisible("CardDetails_Button5", false);
			formObject.setVisible("CardDetails_Label8", false);
			formObject.setVisible("CardDetails_Label4", false);
			formObject.setVisible("CardDetails_add", false);
			formObject.setVisible("CardDetails_modify", false);
			formObject.setVisible("CardDetails_delete", false);
			formObject.setVisible("cmplx_CardDetails_charityamt", false);
			formObject.setVisible("cmplx_CardDetails_suppcardreq", false);
			formObject.setVisible("CardDetails_Label5", false);
			formObject.setVisible("cmplx_CardDetails_smsopt", false);
			formObject.setVisible("CardDetails_Label7", false);
			formObject.setVisible("cmplx_CardDetails_statcycle", false);
			formObject.setVisible("cmplx_CardDetails_Security_Check_Held", false);
			formObject.setVisible("CardDetails_Label12", false);
			formObject.setVisible("cmplx_CardDetails_MarketCode", false);
			formObject.setVisible("cmplx_CardDetails_cmplx_CardCRNDetails", false);
			formObject.setTop("CardDetails_Save", 100);
			formObject.setHeight("CardDetails_Frame1", formObject.getTop("CardDetails_Save")+formObject.getHeight("CardDetails_Save")+20);
			formObject.setHeight("Card_Details", formObject.getHeight("CardDetails_Frame1")+20);
		}

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}
		//disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//below code added by nikhil 11/11/17
			notepad_load();
			notepad_withoutTelLog();

		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

			//	formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setLocked("cmplx_Decision_New_CIFNo",true);
			formObject.setLocked("cmplx_Decision_IBAN",true);

			//disha FSD
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			//changes by saurabh on 19th Dec #DecisionHistory_Button2 and DecisionHistory_Label26 removed from below list
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#MultipleApplicantsGrid#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#\n#cmplx_Decision_AppID#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);

			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history of csm");

			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			loadPicklist1();
			//formObject.setVisible("cmplx_Decision_Referralreason",false);
		
         	
			PersonalLoanS.mLogger.info("***********Inside after making field visible");
			try{
				if(formObject.getLVWRowCount("cmplx_Decision_MultipleApplicantsGrid")==0){
					List<List<String>> mylist=new ArrayList<List<String>>();
					List<String> subList=new ArrayList<String>();
					subList.add("Primary");
					subList.add(formObject.getNGValue("CUSTOMERNAME"));	
					subList.add("");
					mylist.add(new ArrayList<String>(subList));
					PersonalLoanS.mLogger.info("mylist 1: "+mylist);
					subList.clear();
					int m=formObject.getLVWRowCount("cmplx_Guarantror_GuarantorDet");
					int n=formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");
					PersonalLoanS.mLogger.info("m:"+m+" n:"+n);
					for(int i=0;i<m;i++){
						subList.clear();
						subList.add("Guarantor");
						subList.add(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",i,7)+" "+formObject.getNGValue("cmplx_Guarantror_GuarantorDet",i,9));
						subList.add("");
						mylist.add(new ArrayList<String>(subList));//each time adding we need to give a new list reference or it will pick up the old one and uon clear it will get deleted as well 
						PersonalLoanS.mLogger.info("Inside loop:mylist "+i+" :"+mylist);
					}
					
					PersonalLoanS.mLogger.info("mylist 2: "+mylist);
					for(int i=0;i<n;i++){
						subList.clear();
						subList.add("Supplement Card Holder");
						subList.add(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,7)+" "+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,9));
						subList.add("");
						mylist.add(new ArrayList<String>(subList));//each time adding we need to give a new list reference or it will pick up the old one and uon clear it will get deleted as well 
						
					}
					PersonalLoanS.mLogger.info("mylist 3: "+mylist);
					if(mylist.size()>0){
						for(List<String> temp:mylist){
							formObject.addItemFromList("cmplx_Decision_MultipleApplicantsGrid", temp);
						}
					}
				}		
			}catch(Exception e){printException(e);}
		} 	


	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {

		PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType()) {

		case FRAME_EXPANDED:
			PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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


	public void initialize() {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS initialize()" );

	}


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());

	}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		IncomingDoc();
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "Base Rate Type" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
//++ below code added by abhishek on 23/01/2018 for Reject data functionality
		
		PersonalLoanS.mLogger.info("Inside PL PROCESS ddvtchecker submitFormCompleted()" + pEvent.getSource()); 
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();
	
		objInput.add("Text:"+formObject.getWFWorkitemName());
		
		PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0));

		
			formObject.getDataFromStoredProcedure("ng_CAS_RejectData", objInput);

			//++ above code added by abhishek on 23/01/2018 for Reject data functionality

	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		try{
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		if("Approve-parallel processing".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			PersonalLoanS.mLogger.info( "Inside IF");
			
			formObject.setNGValue("parallel_sequential","P");
			PersonalLoanS.mLogger.info( "Inside IF" +formObject.getNGValue("parallel_sequential"));
		}
		else
		{

			PersonalLoanS.mLogger.info( "Inside else");
		
			formObject.setNGValue("parallel_sequential","S");
			PersonalLoanS.mLogger.info( "Inside IF" +formObject.getNGValue("parallel_sequential"));

		}
		
		//change by saurabh on 29th Jan
		//IncomingDoc();
		/*if("Parallel".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("parallel_sequential","P");
		}
		else
		{
			formObject.setNGValue("parallel_sequential","S");
		}*/
		String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
		String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
		String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");
		String emiratesId = formObject.getNGValue("cmplx_Customer_EmiratesID");
		String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");
		String fullName = fName+" "+mName+" "+lName; 
		PersonalLoanS.mLogger.info("Final val of fullName:"+ fullName);
		formObject.setNGValue("DecisionHistory_Customer_Name",fullName);
		formObject.setNGValue("DecisionHistory_CIFid",cifId);
		formObject.setNGValue("DecisionHistory_Emiratesid",emiratesId);
		validateStatusForSupplementary();
		saveIndecisionGridCSM();//Arun (01-12-17) for Decision history to appear in the grid
		checkForDeferredWaivedDocs();
		LoadReferGrid();
		//below code added by nikhil 8/12/17
		
		PersonalLoanS.mLogger.info( "Base Rate Type" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
	}
		catch(Exception e){
			PersonalLoanS.mLogger.info( "IException is submit form completedeasdasdasdasdasdas");
			printException(e);

		}
	}	


	public void continueExecution(String arg0, HashMap<String, String> arg1) {

		//empty method
	}

}

