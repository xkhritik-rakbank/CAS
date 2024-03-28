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
import java.util.List;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormConfig;
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
	String queryData_load="";
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "5,6,7,8,9,11,12,13,14,15,16");//Changes done by shweta for jira #1768 to hide CPV tab
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 10, true);	//Show services Request

        


	}


	public void formPopulated(FormEvent pEvent) 
	{
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();


		try{
			
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("Cad1_Frame_Name"));
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			formObject.setVisible("ReferHistory",false);
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
			formObject.setVisible("Card_Details", true);
			}          
			

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
			LoadView(pEvent.getSource().getName());

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

			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_DSA_NAme"))){
				formObject.setNGValue("cmplx_Customer_DSA_NAme", formObject.getNGValue("DSA_Name"));
				formObject.setNGValue("cmplx_Customer_DSA_Code", formObject.getNGValue("DSA_Name"));

			}
			formObject.setLocked("cmplx_Customer_DSA_NAme",false);
			formObject.setLocked("cmplx_Customer_DSA_Code",false);

			formObject.setEnabled("cmplx_Customer_DSA_NAme", true);
			formObject.setEnabled("cmplx_Customer_DSA_Code", true);


		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
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
			formObject.setLocked("cmplx_IncomeDetails_RentalIncome",false);
			formObject.setEnabled("cmplx_IncomeDetails_RentalIncome",true);
			formObject.setLocked("cmplx_IncomeDetails_EducationalAllowance",false);
			formObject.setEnabled("cmplx_IncomeDetails_EducationalAllowance",true);
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
			LoadView(pEvent.getSource().getName());

			String empid="AVI,MED,EDU,HOT,PROM";	
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);

			formObject.setVisible("EMploymentDetails_Label10",false);
			formObject.setVisible("cmplx_EmploymentDetails_marketcode",false);

			formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",false);
			formObject.setVisible("EMploymentDetails_Label7",false);//pcasi-1079
			formObject.setVisible("EmploymentDetails_Bank_Button",false);
			formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC",false);

			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
			formObject.setVisible("EMploymentDetails_Label27",false);
			formObject.setVisible("cmplx_EmploymentDetails_MIS",false);
			formObject.setVisible("EMploymentDetails_Label28",false);
			formObject.setVisible("EMploymentDetails_Label22",false);
			formObject.setVisible("cmplx_EmploymentDetails_PromotionCode",false);
			formObject.setVisible("cmplx_EmploymentDetails_Status_PLExpact", false);
			formObject.setVisible("cmplx_EmploymentDetails_Status_PLNational", false);
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

			/*	formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			formObject.setVisible("EMploymentDetails_Label33",false);*/
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

			/*else if(NGFUserResourceMgr_PL.getGlobalVar("PL_TEN").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",true);
				formObject.setVisible("EMploymentDetails_Label7",true);
			}*/

			else if("S".equals(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")) && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
				formObject.setVisible("EMploymentDetails_Label59",true);

			}



		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
			formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
			loadPicklistELigibiltyAndProductInfo();

			loadEligibilityData();

		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

			loadPicklist_Address();

			formObject.setEnabled("AddressDetails_Save",true);
			formObject.setEnabled("AddressDetails_addr_Add",true);
			formObject.setEnabled("AddressDetails_addr_Modify",true);
			formObject.setEnabled("AddressDetails_addr_Delete",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			LoadView(pEvent.getSource().getName());

			formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			enrollrewardvalid();
			//Code ended by siva on 01112019 for PCAS-1401
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			Loadpicklistfatca();
			formObject.setEnabled("FATCA_Save",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){


			formObject.setEnabled("KYC_Save",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadView(pEvent.getSource().getName());

			loadPickListOECD();
			formObject.setEnabled("OECD_Save",true);
		}

		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}*/
		else if ("IncomingDocNew".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			PersonalLoanS.mLogger.info("***********Inside IncomingDocNew of csm");
			formObject.setHeight("IncomeDetails_Frame1", 850);
			formObject.setHeight("Incomedetails", 870);
			formObject.setHeight("Inc_Doc",850);

		}
		//disha FSD
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			disableButtonsPL("CardDetails");
			//Ref. 1002
			LoadPickList("CardDetails_Combo2","select '--Select--'  as description union select convert(varchar,description) from NG_MASTER_MarketingCode with (nolock) where isActive='Y' order by code ");
			formObject.setVisible("cmplx_CardDetails_securitycheck", false);
			LoadPickList("CardDetails_FeeProfile","select '--Select--'  as description union select convert(varchar,description) from ng_master_feeProfile with (nolock) ");
			formObject.setVisible("CardDetails_Label12", false);
			LoadPickList("CardDetails_InterestFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_InterestProfile with (nolock) ");
			formObject.setVisible("cmplx_CardDetails_MarketCode", false);
			LoadPickList("CardDetails_TransactionFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_TransactionFeeProfile with (nolock) ");
			formObject.setVisible("CardDetails_Label8", false);

			formObject.setVisible("cmplx_CardDetails_Bank_Name", false);
			formObject.setVisible("CardDetails_Label9", false);
			formObject.setVisible("cmplx_CardDetails_Cheque_Number", false);
			formObject.setVisible("CardDetails_Label10", false);
			formObject.setVisible("cmplx_CardDetails_Amount", false);
			formObject.setVisible("CardDetails_Label11", false);
			formObject.setVisible("cmplx_CardDetails_Date", false);
			formObject.setVisible("CardDetails_Label13", false);
			formObject.setVisible("cmplx_CardDetails_CustClassification", false);
			//Ref. 1002 end.
		}
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//12th sept
			notepad_load();
			notepad_withoutTelLog();
			//12th sept
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			PersonalLoanS.mLogger.info( "Activity name is:" + sActivityName);
			formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			//Ref. 1001.
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
			formObject.setLocked("NotepadDetails_noteDate",true);
			formObject.setLocked("NotepadDetails_Actusername",true);
			formObject.setLocked("NotepadDetails_user",true);
			formObject.setLocked("NotepadDetails_insqueue",true);
			formObject.setLocked("NotepadDetails_Actdate",true);
			formObject.setVisible("NotepadDetails_SaveButton",true);
			formObject.setVisible("NotepadDetails_Frame3", false);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadReferGrid();
			formObject.setLocked("DecisionHistory_DecisionReasonCode",false); // As the fields is locked not disabled
			formObject.setLocked("cmplx_Decision_ReferTo",true);
			//	formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setLocked("cmplx_Decision_New_CIFNo",true);
			formObject.setLocked("cmplx_Decision_IBAN",true);
			formObject.setVisible("DecisionHistory_chqbook",false);
			loadPicklist1();
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside checker decision history");
			//fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Button2#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Button7#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#cmplx_Decision_MultipleApplicantsGrid#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Button7#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);

			PersonalLoanS.mLogger.info("***********Inside checker after fragment alignment decision history");
			//setDataInMultipleAppGrid();

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
		CustomSaveForm();
	}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));

	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());

	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		mLogger.info("PersonnalLoanS>  PL_Iniation"+ "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
		CustomSaveForm();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		//Deepak Changes for Loan amount
				formObject.setNGValue("Loan_Amount", formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
				//below code by jahnavi to map SCHEM with scheme picklist
				String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
				int prd_count=formObject.getLVWRowCount(prodGridId);
				String scheme = "";
				if(prd_count>0){
					scheme = formObject.getNGValue(prodGridId,0,7);
				}
				String map_scheme="select top 1 SCHEMEDESC from NG_master_Scheme where SCHEMeID = '"+scheme+"'";
				List<List<String>> result = formObject.getDataFromDataSource(map_scheme);
				PersonalLoanS.mLogger.info("scheme query: "+map_scheme);
				if(result!=null && !result.isEmpty()){
						formObject.setNGValue("SCHEM", result.get(0).get(0));
						formObject.setNGValue("SCHEME", result.get(0).get(0));
						PersonalLoanS.mLogger.info("scheme"+result.get(0).get(0));
					
				}
			
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
			PersonalLoanS.mLogger.info("Inside  approve DDVT checker");
			formObject.setNGValue("q_parllel_seq", 1);
			PersonalLoanS.mLogger.info( "Inside IF" +formObject.getNGValue("parallel_sequential"));

		}
		String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
		String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
		String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");
		String emiratesId = formObject.getNGValue("cmplx_Customer_EmiratesID");
		String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");
		String fullName = ""; 
		if("".equals(mName)){
			fullName = fName+" "+lName;
		}
		else{
			fullName = fName+" "+mName+" "+lName;
		}
		PersonalLoanS.mLogger.info("Final val of fullName:"+ fullName);
		formObject.setNGValue("DecisionHistory_Customer_Name",fullName);
		formObject.setNGValue("DecisionHistory_CIFid",cifId);
		formObject.setNGValue("DecisionHistory_Emiratesid",emiratesId);
		formObject.setNGValue("CUSTOMERNAME",fullName);
		
		saveIndecisionGridCSM();
		LoadReferGrid();
		
		//below code added by nikhil 8/12/17
		IncomingDoc();
		}
		catch (Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
		}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
	//empty method

	}


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
