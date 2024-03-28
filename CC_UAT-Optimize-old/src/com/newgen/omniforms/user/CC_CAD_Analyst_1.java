/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Cad Analyst1

File Name                                                         : CC_CAD_Analyst1

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_CAD_Analyst_1 extends CC_Common implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	boolean executeFlag = false;
	String queryData_load="";

	public void formLoaded(FormEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

		makeSheetsInvisible(tabName, "9,11,12,13,14,15,16,17");
		formObject.setVisible("SmartCheck_Label2",true);
		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);
		if("Refer to Credit".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		{
			formObject.setEnabled("cmplx_DEC_ReferTo",true);
		}//Arun (07/10)

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{

			new CC_CommonCode().setFormHeader(pEvent);
			//++ Below Code added By Yash on Oct 12, 2017  to fix : 34-"Office verification tab will not be visible as the case is for self employed customer " : Reported By Shashank on Oct 09, 2017++

			enable_cad1();
			//++ Above Code added By Yash on Oct 12, 2017  to fix : 34-"Office verification tab will not be visible as the case is for self employed customer " : Reported By Shashank on Oct 09, 2017++

		}catch(Exception e)
		{
			new CC_CommonCode();
			CreditCard.mLogger.info( "Exception:"+CC_Common.printException(e));
		}
	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap

		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().FrameExpandEvent(pEvent);						
			break;

		case FRAGMENT_LOADED:
			CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			fragment_loaded(pEvent,hm,formObject);
			//loadPicklist1();
			break;

		case MOUSE_CLICKED:
			CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().mouse_clicked(pEvent);
			break;
			// added by abhishek for repeater end
			//ended by yash 
		case VALUE_CHANGED:
			CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().value_changed(pEvent);
			break;
		default: break;

		}
	}


	public void getDataFromALOC(FormReference formObject2, String corpName) {
		try{
			String query = "select INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL_EXPAT from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME = '"+corpName+"'";
			List<List<String>> result = formObject2.getDataFromDataSource(query);
			if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
			{
				formObject2.setNGValue("indusSector", result.get(0).get(0));
				formObject2.setNGValue("indusMAcro", result.get(0).get(1));
				formObject2.setNGValue("indusMicro", result.get(0).get(2));
				formObject2.setNGValue("CompanyDetails_highdelin", result.get(0).get(3));
				formObject2.setNGValue("CompanyDetails_currempcateg", result.get(0).get(4));
			}
		}
		catch(Exception ex){
			CreditCard.mLogger.info( printException(ex));
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
		CreditCard.mLogger.info("inside submitformcompleted: ");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Decision= formObject.getNGValue("cmplx_DEC_Decision");
		if ("Approve".equalsIgnoreCase(Decision)){
			List<String> objInput=new ArrayList<String>();
			//disha FSD cad delegation procedure changes
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			objInput.add("Text:"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));
			CreditCard.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3));
			formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);
		}

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		// below line commented because now we have one common decision fragment
		//formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_CADDecision_Decision"));
		formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_DEC_Decision"));

		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (07/10)


		if("Refer to Smart CPV".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		{
			saveSmartCheckGrid();
		}
		if("Refer to Credit".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		{
			//++ below code added 10-10-2017 - to save cad decision tray in external table
			CreditCard.mLogger.info("inside submitFormStarted value of ReferTo : "+ formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
			formObject.setNGValue("Cad_Deviation_Tray",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
			formObject.setNGValue("q_Emailto",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
		}
		saveIndecisionGrid();
	}
	private void fragment_loaded(FormEvent pEvent,HashMap<String,String> hm,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

		}*/
		CreditCard.mLogger.info( "EventName: before CPV tab validations");
		if ("Business_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Business_Verification","Clicked");
			formObject.fetchFragment("Business_Verification", "BussinessVerification", "q_BussVerification");
			LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			formObject.setLocked("BussinessVerification_Frame1", true);
			//++ Below Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment " : Reported By Shashank on Oct 09, 2017++

			enable_busiVerification();
			//++ Above Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment" : Reported By Shashank on Oct 09, 2017++


		}
		else if ("HomeCountry_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("HomeCountry_Verification","Clicked");
			formObject.fetchFragment("HomeCountry_Verification", "HomeCountryVerification", "q_HCountryVerification");
			LoadPickList("cmplx_HCountryVerification_Hcountrytelverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			formObject.setLocked("HomeCountryVerification_Frame1", true);
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ResidenceVerification","Clicked");
			formObject.fetchFragment("ResidenceVerification", "ResidenceVerification", "q_ResiVerification");
			LoadPickList("cmplx_ResiVerification_Telnoverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			formObject.setLocked("ResidenceVerification_Frame1", true);	
		}
		else if ("Reference_Detail_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Reference_Detail_Verification","Clicked");
			formObject.fetchFragment("Reference_Detail_Verification", "ReferenceDetailVerification", "q_RefDetailVerification");
			LoadPickList("cmplx_RefDetVerification_ref1cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			LoadPickList("cmplx_RefDetVerification_ref2cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");

		}

		else if ("LoanCard_Details_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LoanCard_Details_Check","Clicked");
			formObject.fetchFragment("LoanCard_Details_Check", "LoanandCard", "q_LoanandCard");
			loadPicklist_loancardverification();
			formObject.setLocked("LoanandCard_Frame1", true);
		}
		else if ("Notepad_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Details","Clicked");
			formObject.fetchFragment("Notepad_Details", "NotepadDetails", "q_NotepadDetails");

			formObject.setVisible("NotepadDetails_Frame1", false);
			/*String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
		formObject.setNGValue("NotepadDetails_notecode",notepad_desc);
			 */}
		else if ("Smart_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_Check","Clicked");
			formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");
			formObject.setLocked("SmartCheck_Frame1", true);

		}
		
		else if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();
			/*formObject.setLocked("Customer_FetchDetails",true);
					formObject.setLocked("Customer_save",true);*/

			//setDisabled();
		}	

		//added by yash fro CC FSD
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
					LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
					LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			 */
			/*formObject.setLocked("Product_Frame1",true);
					formObject.setLocked("Add",true);
					formObject.setLocked("Modify",true);
					formObject.setLocked("Delete",true);
					formObject.setLocked("Product_Save",true);
					formObject.setLocked("cmplx_Product_cmplx_ProductGrid_table",true);
					formObject.setLocked("Customer_save",true);
					formObject.setLocked("Customer_save",true);*/


		}
		//added by yash for CC FSD
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			//	formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Flying_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);
			formObject.setVisible("IncomeDetails_ListView1",true);
			formObject.setVisible("IncomeDetails_Button1",true);
			formObject.setVisible("IncomeDetails_Button2",true);
			formObject.setVisible("IncomeDetails_Button3",true);
			String EmpType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			if("Self Employed".equalsIgnoreCase(EmpType) )
			{
				formObject.setVisible("cmplx_IncomeDetails_TotalAvgOther",false);
				formObject.setVisible("IncomeDetails_Label15",false);
			}

		}
		// ended by yash for CC FSD
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			loadPicklist_CompanyDet();
			//loadPicklist_Address();
			//formObject.setLocked("CompanyDetails_Frame1",true);
			formObject.setVisible("CompanyDetails_CheckBox1",true);
			formObject.setVisible("CompanyDetails_Label17",true);
			formObject.setVisible("CompanyDetails_highdelin",true);
			formObject.setVisible("CompanyDetails_Text1",true);
			formObject.setVisible("CompanyDetails_Label14",true);
			formObject.setVisible("CompanyDetails_currempcateg",true);
			formObject.setVisible("CompanyDetails_Text2",true);							
			formObject.setVisible("CompanyDetails_Label16",true);
			formObject.setVisible("CompanyDetails_categcards",true);
			formObject.setVisible("CompanyDetails_Text3",true);
			formObject.setVisible("CompanyDetails_Label12",true);
			formObject.setVisible("CompanyDetails_categexpat",true);
			formObject.setVisible("CompanyDetails_Text4",true);
			formObject.setVisible("CompanyDetails_Label15",true);
			formObject.setVisible("CompanyDetails_categnational",true);
			formObject.setVisible("CompanyDetails_Text5",true);							
			formObject.setLocked("CompanyDetails_Text1",false);
			formObject.setLocked("CompanyDetails_Text2",true);
			formObject.setLocked("CompanyDetails_Text3",true);
			formObject.setLocked("CompanyDetails_Text4",true);
			formObject.setLocked("CompanyDetails_Text5",true);
			formObject.setLocked("estbDate",false);
			formObject.setEnabled("CompanyDetails_DatePicker1",true);
		}
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//commented by Akshay on 7/9/2017 for firing this call on CAD
			//formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			formObject.setLocked("AuthorisedSignDetails_CIFNo", true);
			formObject.setLocked("AuthorisedSignDetails_Button4", true);
			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");

		}
		//added by yash for CC FSD
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			formObject.setEnabled("PartnerDetails_Dob",true);
			//loadPicklist_Address();
			//formObject.setLocked("PartnerDetails_Frame1",true);

		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			//loadPicklist_Address();
			//formObject.setLocked("ExtLiability_Frame1",true);
			formObject.setVisible("Liability_New_Label1",true);
			formObject.setVisible("Liability_New_MOB",true);
			formObject.setVisible("Liability_New_Label2",true);
			formObject.setVisible("Liability_New_Utilization",true);
			formObject.setVisible("Liability_New_Label3",true);
			formObject.setVisible("Liability_New_Outstanding",true);
			formObject.setVisible("Liability_New_Delinkinlast3months",true);
			formObject.setVisible("Liability_New_DPDinlast18months",true);
			formObject.setVisible("Liability_New_DPDinlast6",true);
			formObject.setVisible("Liability_New_Label4",true);
			formObject.setVisible("Liability_New_writeOfAmount",true);
			formObject.setVisible("Liability_New_Label5",true);
			formObject.setVisible("Liability_New_worstStatusInLast24",true);
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			formObject.setVisible("ExtLiability_Label15",true);
			formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
			formObject.setVisible("Liability_New_Label6",true);
			formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
			formObject.setVisible("Liability_New_Label8",true);
			formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
			formObject.setVisible("Liability_New_Label7",true);
			formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
			/*formObject.setVisible("cmplx_Liability_New_DBR",false);
					formObject.setVisible("ExtLiability_Label9",false);
					formObject.setVisible("cmplx_Liability_New_TAI",false);
					formObject.setVisible("ExtLiability_Label25",false);
					formObject.setVisible("cmplx_Liability_New_DBRNet",false);
					formObject.setVisible("ExtLiability_Label14",false);*/

		}
		//added by yash for CC FSD
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Compliance_Frame1",true);

		}
		//ended by yash for CC FSD

		else if ("Liability Addition".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//formObject.setLocked("ExtLiability_Frame4",true);		
		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("EMploymentDetails_Frame1", true);

			//loadPicklist4();
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			//added 6th sept for proc 1926
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
			{

				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", false);//total final limit
				formObject.setLocked("ELigibiltyAndProductInfo_Button1", false);//calculate re-eligibility
			}
			//ended 6th sept for proc 1926

		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);

		}
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("AltContactDetails_Frame1",true);

		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);

		}
		else if ("CardCollection".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CardDetails_Frame1",true);
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FATCA_Frame6",true);

		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CardDetails_Frame1",true);

		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("KYC_Frame1",true);

		}
		//added by yash for CC FSD
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("OECD_Frame8",true);

		}

		else if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);

		}
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("PartMatch_Frame1",true);
			//change by saurabh for JIRA - 2592
			LoadPickList("PartMatch_nationality","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock) order by code"); //Arun (10/10)
			//change by saurabh for JIRA - 2592 end.
		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FinacleCRMIncident_Frame1",true);

		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}
		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("ExternalBlackList_Frame1",true);
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("FinacleCore_ChequeType", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_AccountType with (nolock)");
			//loadPicklist_Address();
			//formObject.setLocked("FinacleCore_Frame1",true);
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("MOL1_Frame1",true);

		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("WorldCheck1_Frame1",true);

		}

		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck_Frame1",true);
			formObject.setVisible("SmartCheck_Label2",true);
			formObject.setVisible("SmartCheck_CPVRemarks",true);
			formObject.setVisible("SmartCheck_Label4",false);
			formObject.setVisible("SmartCheck_FCURemarks",false);

			formObject.setLocked("SmartCheck_CPVRemarks",true);
			//++ Above Code added By Yash on Oct 12, 2017  to fix : 33-"CPV remarks should be disabled" : Reported By Shashank on Oct 09, 2017++

			//formObject.setLocked("SmartCheck1_Modify",true);


		}
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck_Frame1",true);
			formObject.setVisible("SmartCheck1_Label2",false);
			formObject.setVisible("SmartCheck1_CPVRemarks",false);
			formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setVisible("SmartCheck1_FCURemarks",false);
		}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//formObject.setLocked("RejectEnq_Frame1",true);

		}
		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//formObject.setLocked("SalaryEnq_Frame1",true);

		}
		/*if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
					//loadPicklist_Address();
					formObject.setLocked("ExternalBlackList_Frame1",true);

				}*/
		/*if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
					//loadPicklist_Address();
					formObject.setLocked("IncomingDocument_Frame",true);

				}*/
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("PostDisbursal_Frame2",true);

		}
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);

		}
		else if ("Fraud Control Unit".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
		}

		//added by abhishek for CC FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_withoutTelLog();

			formObject.setTop("NotepadDetails_savebutton",410);
			notepad_load();
		}
		else if ("Customer_Details_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification_Frame1", true);
		}
		//ended by yash

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//added by Akshay on 5/9/17
			Decision_cadanalyst1();
			formObject.setEnabled("cmplx_DEC_Manual_Deviation", true);
		}
	}
}

