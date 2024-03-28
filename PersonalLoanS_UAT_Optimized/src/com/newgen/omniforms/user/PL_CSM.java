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

import java.util.HashMap;

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
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		PersonalLoanS.mLogger.info("PL CSM insidde-->Inside formLoaded()" + pEvent.getSource().getName());

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
			formObject.setEnabled("Customer_Reference_Add",true);
			formObject.setEnabled("Customer_Reference__modify",true);
			formObject.setEnabled("Customer_Reference_delete",true);
			
			// Below code added by abhishek on 08/11/2017
			formObject.setVisible("Customer_Label22", false);
			formObject.setVisible("cmplx_Customer_apptype", false);
			formObject.setVisible("Customer_Label55", true);
			formObject.setVisible("cmplx_Customer_marsoomID", true);
			// Above code added by abhishek on 08/11/2017

			formObject.setLocked("FetchDetails",true);
			formObject.setLocked("Customer_Button1",true);
			PersonalLoanS.mLogger.info("Column Added in Repeater"+" for eida");
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
			PersonalLoanS.mLogger.info("Column Added in Repeater"+" after eida");

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
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
			formObject.setHeight("Incomedetails", 630);
			formObject.setHeight("IncomeDetails_Frame1", 605);                      
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			
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
            //below code added by abhishek for CSM point 9 on 7/11/2017
			if("true".equalsIgnoreCase(formObject.getNGValue("ExtLiability_Takeoverindicator"))){
				formObject.setEnabled("ExtLiability_takeoverAMount", true);
			}else{
				formObject.setEnabled("ExtLiability_takeoverAMount", false);
			}
			//above code added by abhishek for CSM point 9 on 7/11/2017
			//below code added by abhishek for CSM point 8 on 7/11/2017
			if("true".equalsIgnoreCase(formObject.getNGValue("ExtLiability_CACIndicator"))){
				formObject.setEnabled("ExtLiability_QCAmt", true);
				formObject.setEnabled("ExtLiability_QCEMI", true);
			}else{
				formObject.setEnabled("ExtLiability_QCAmt", false);
				formObject.setEnabled("ExtLiability_QCEMI", false);
			}
			//above code added by abhishek for CSM point 8 on 7/11/2017
			
			  LoadPickList("ExtLiability_contractType", "select '--Select--' union select convert(varchar, description) from ng_master_contract_type with (nolock)");
			// Added by aman to load the picklist	
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//disha FSD
			PersonalLoanS.mLogger.info( "Inside value empdet" + pEvent.getSource().getName());

			String empid="AVI,MED,EDU,HOT,PROM";	
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
			formObject.setTop("EMploymentDetails_Button1",440);
			formObject.setVisible("cmplx_EmploymentDetails_employer_type",false);
			formObject.setVisible("EMploymentDetails_Label29",false);												

			loadPicklist4();
			Fields_ApplicationType_Employment();

			if("NEP".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))){
				formObject.setVisible("EMploymentDetails_Label25",true);
				formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
			}

			

			else if("TEN".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",true);
				formObject.setVisible("EMploymentDetails_Label7",true);
			}

			else if("Surrogate".equals(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")) && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
				formObject.setVisible("EMploymentDetails_Label59",true);

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
			
			String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			PersonalLoanS.mLogger.info("appTyp value is: "+appType);
			
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
			
			formObject.setLeft("ELigibiltyAndProductInfo_Label10", formObject.getLeft("ELigibiltyAndProductInfo_Label5"));
			formObject.setTop("ELigibiltyAndProductInfo_Label10", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getLeft("ELigibiltyAndProductInfo_Label10"));
			formObject.setTop("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
			
			formObject.setLeft("ELigibiltyAndProductInfo_Label9", formObject.getLeft("ELigibiltyAndProductInfo_Label6"));
			formObject.setTop("ELigibiltyAndProductInfo_Label9", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getLeft("ELigibiltyAndProductInfo_Label9"));
			formObject.setTop("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
			//above code added by abhishek for CSM point 15 on 7/11/2017
			//below code by saurabh on 11 nov 17.
			if(appType.contains("TKO")){
				PersonalLoanS.mLogger.info("inside take condition");
				formObject.setVisible("ELigibiltyAndProductInfo_Label10", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label9", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
			}
			else{
				PersonalLoanS.mLogger.info("inside else take condition");
				formObject.setVisible("ELigibiltyAndProductInfo_Label10", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label9", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
			}
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
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			
			formObject.setEnabled("FATCA_Save",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			
			formObject.setEnabled("KYC_Save",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){

			
			formObject.setEnabled("OECD_Save",true);
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

			formObject.setVisible("DecisionHistory_chqbook",false);
			loadPicklist1();
			//disha FSD
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
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
			formObject.setVisible("DecisionHistory_Label26",true);
			formObject.setVisible("cmplx_Decision_AppID",true);

			formObject.setLocked("cmplx_Decision_IBAN",true);
			formObject.setLocked("cmplx_Decision_AppID",true);
			formObject.setLocked("cmplx_Decision_AccountNo",true);
			formObject.setTop("DecisionHistory_Label7", 8);
			formObject.setTop("cmplx_Decision_AccountNo", 23);
			formObject.setTop("Decision_Label3", 56);
			formObject.setTop("cmplx_Decision_Decision", 72);
			formObject.setTop("Decision_Label4", 56);
			formObject.setTop("cmplx_Decision_REMARKS", 72);

			formObject.setTop("Decision_Label4", 104);
			formObject.setTop("cmplx_Decision_REMARKS", 120);

			formObject.setTop("DecisionHistory_Label6", 8);
			formObject.setTop("cmplx_Decision_IBAN", 23);						
			formObject.setTop("DecisionHistory_Label26", 56);
			formObject.setTop("cmplx_Decision_AppID", 92);				
			formObject.setTop("Decision_ListView1", 226);		
			formObject.setTop("DecisionHistory_save", 400);	

			formObject.setTop("Decision_Label1", 8);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);

			formObject.setLeft("DecisionHistory_Label26", 1000);
			formObject.setLeft("cmplx_Decision_AppID", 1000);
			formObject.setLeft("Decision_Label4", 352);
			formObject.setLeft("cmplx_Decision_REMARKS", 352);
			formObject.setLeft("Decision_Label4", 672);
			formObject.setLeft("cmplx_Decision_REMARKS", 672);					

			formObject.setLeft("Decision_Label1", 1000);
			formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 1000);

			formObject.setLeft("Decision_Label4", 352);
			formObject.setLeft("cmplx_Decision_REMARKS", 352);

			formObject.setTop("Decision_Label4", 56);
			formObject.setTop("cmplx_Decision_REMARKS", 72);
			//Common function for decision fragment textboxes and combo visibility
			//Below code added by aman for positioning of decision fragment
			formObject.setVisible("DecisionHistory_Label1", true);
			formObject.setLeft("DecisionHistory_Label1", 655);
			formObject.setLeft("DecisionHistory_ReferTo", 655);
			formObject.setTop("DecisionHistory_Label1", 55);
			formObject.setTop("DecisionHistory_ReferTo", 75);
			
			
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

	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());

	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

		if("Parallel".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("parallel_sequential","P");
		}
		else
		{
			formObject.setNGValue("parallel_sequential","S");
		}
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
		//saveIndecisionGridCSM();//Arun (01-12-17) for Decision history to appear in the grid
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		
		//empty method
	}

}
