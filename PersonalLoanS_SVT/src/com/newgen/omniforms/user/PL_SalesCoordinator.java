/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_SalesCoordinator.java
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


public class PL_SalesCoordinator extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("PL Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{

		try{
			
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			mLogger.info("PL Part_Match"+"Inside Part_Match");            
			

		}catch(Exception e)
		{
			mLogger.info("PL Initiation"+ "Exception:"+e.getMessage());
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
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject =FormContext.getCurrentInstance().getFormReference();
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistCustomer();
			
			formObject.setEnabled("Customer_save",true);
			formObject.setLocked("cmplx_Customer_ReferrorCode",false);
			formObject.setLocked("cmplx_Customer_ReferrorName",false);
			formObject.setLocked("cmplx_Customer_AppType",false);
			formObject.setLocked("cmplx_Customer_corporateCode",false);
			formObject.setLocked("cmplx_Customer_Bankingwithus",false);
			formObject.setLocked("cmplx_Customer_noofDependent",false);
			formObject.setLocked("cmplx_Customer_guardian",false);
			formObject.setLocked("cmplx_Customer_minor",false);
			formObject.setEnabled("Customer_Reference_Add",true);
			formObject.setEnabled("Customer_Reference__modify",true);
			formObject.setEnabled("Customer_Reference_delete",true);

			formObject.setLocked("FetchDetails",true);
			formObject.setLocked("Customer_Button1",true);

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			
			formObject.setEnabled("Product_Save",true);
			formObject.setEnabled("Product_Add",true);
			formObject.setEnabled("Product_Modify",true);
			formObject.setEnabled("Product_Delete",true);
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
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setEnabled("Liability_New_AECBReport",true);
			formObject.setEnabled("Liability_New_Save",true);
			formObject.setLocked("ExtLiability_Button1",true);	
			formObject.setLocked("Liability_New_Button1",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");

		}
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//disha FSD
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

			formObject.setTop("EMploymentDetails_Frame2",54);
			formObject.setTop("EMploymentDetails_Label71",240);
			formObject.setTop("cmplx_EmploymentDetails_EmpContractType",256);
			formObject.setTop("cmplx_EmploymentDetails_Kompass",256);
			formObject.setLeft("cmplx_EmploymentDetails_Kompass",1066);								

			formObject.setHeight("EMploymentDetails_Frame2",382);
			formObject.setTop("EMploymentDetails_Save",400);
			formObject.setTop("EMploymentDetails_Button1",440);

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

			else if("S".equals(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")) && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
				formObject.setVisible("EMploymentDetails_Label59",true);

			}


		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
			formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);	
			loadEligibilityData();

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

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}
		//disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
	}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setVisible("DecisionHistory_chqbook",false);
			loadPicklist1();
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside checker decision history");
        	fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Button2#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Button7#\n#DecisionHistory_Label26,cmplx_Decision_AppID#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
        	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
    		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
    		
        	PersonalLoanS.mLogger.info("***********Inside checker after fragment alignment decision history");
			
			//disha FSD
			/*formObject.setVisible("DecisionHistory_Label8", false);
			 * 	formObject.setVisible("DecisionHistory_chqbook",false);
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
			formObject.setTop("cmplx_Decision_cmplx_gr_decision", 226);		
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
			formObject.setTop("cmplx_Decision_REMARKS", 72);*/
        	//for decision fragment made changes 8th dec 2017
			
		} 	
		
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {

		mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType()) {

		case FRAME_EXPANDED:
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
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS initialize()" );

	}


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());

	}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());

	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());

	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
/*
		if(NGFUserResourceMgr_PL.getResourceString_plIfConditions("PL_Parallel").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("parallel_sequential","P");
		}
		else
		{
			formObject.setNGValue("parallel_sequential","S");
		}*/
		//below code added by nikhil 08/12/17
		if("Approve-parallel processing".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
	    {
	        formObject.setNGValue("parallel_sequential","P");
	    }
	    else
	    {
	        formObject.setNGValue("parallel_sequential","S");
	    }
		saveIndecisionGrid();
		loadInDecGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
	//empty method

	}

}

