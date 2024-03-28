/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CAD_Analyst_2.java
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



public class PL_CAD_Analyst_2 extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("Exception:"+e.getMessage());
			printException(e);
		}
	}

	public void fragment_loaded(ComponentEvent pEvent){
		mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();


		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			//setDisabled();
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();
		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			formObject.setLocked("Product_Frame1",true);
			loadPicklistProduct("Personal Loan");
			//loadProductCombo();
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			//LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			/*String ReqProd=formObject.getNGValue("ReqProd");
		mLogger.info("Value of ReqProd is:"+ReqProd);
		loadPicklistProduct(ReqProd);
	}

	if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {

		formObject.setLocked("GuarantorDetails_Frame1",true);
	}

	if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
		/*
		String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
        mLogger.info( "Emp Type Value is:"+EmpType);

		if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
        {
        	formObject.setVisible("IncomeDetails_Frame3", false);
            formObject.setHeight("Incomedetails", 630);
            formObject.setHeight("IncomeDetails_Frame1", 605);  
        }
        else if(EmpType.equalsIgnoreCase("Self Employed"))
        {                                                                                                              
            formObject.setVisible("IncomeDetails_Frame2", false);
            formObject.setTop("IncomeDetails_Frame3",40);
            formObject.setHeight("Incomedetails", 300);
            formObject.setHeight("IncomeDetails_Frame1", 280);
        }

		formObject.setLocked("IncomeDetails_Frame1",true);
		formObject.setLocked("IncomeDetails_Frame2",true);
		formObject.setVisible("IncomeDetail_Label13",false);
		formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
		formObject.setVisible("IncomeDetail_Label15",true); 
		formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
		formObject.setVisible("IncomeDetail_Label16",true);
		formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);*/

			formObject.setLocked("IncomeDetails_Frame1",true);
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 630);
			formObject.setHeight("IncomeDetails_Frame1", 605);  
			formObject.setVisible("IncomeDetails_Label15",true); 
			formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
			formObject.setEnabled("cmplx_IncomeDetails_Totavgother",false);
			formObject.setVisible("IncomeDetails_Label16",true);
			formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
			formObject.setEnabled("cmplx_IncomeDetails_compaccAmt",false);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {

			formObject.setLocked("ExtLiability_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {

			formObject.setLocked("LoanDetails_Frame1",true);
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

		}
		// disha FSD
		else if (pEvent.getSource().getName().equalsIgnoreCase("RiskRating")) {

			formObject.setLocked("RiskRating_Frame1",true);
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails"))
		{
			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info( "Activity name is:" + sActivityName);
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

			// ++ below code already exist - 10-10-2017 this is uncommented at onsite but we have change it according to offshore
			formObject.setHeight("NotepadDetails_Frame1",450);
			formObject.setTop("NotepadDetails_save",400);
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {

			formObject.setLocked("AltContactDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {

			formObject.setLocked("FATCA_Frame6",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {

			formObject.setLocked("KYC_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {

			formObject.setLocked("OECD_Frame8",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {

			formObject.setLocked("PartMatch_Frame1",true);
			LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {

			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {

			formObject.setLocked("CustDetailVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {

			formObject.setLocked("BussinessVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {

			formObject.setLocked("HomeCountryVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {

			formObject.setLocked("ResidenceVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification")) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {

			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {

			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

			//formObject.setLocked("NotepadDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {

			//formObject.setLocked("SmartCheck_Frame1",true);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) 
		{
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferHistory"))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}
		// disha FSD
		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			loadPicklist1();

			// ++ below code already exist - 10-10-2017
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");

			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setVisible("Decision_Label1",false);
			formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
			formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setVisible("DecisionHistory_Label1",false);
			formObject.setVisible("cmplx_Decision_refereason",false);			                	
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
			formObject.setVisible("DecisionHistory_Label3",true);
			formObject.setVisible("cmplx_Decision_strength",true);
			formObject.setVisible("DecisionHistory_Label4",true);
			formObject.setVisible("cmplx_Decision_weakness",true);

			formObject.setVisible("DecisionHistory_Label15",true);
			formObject.setVisible("cmplx_Decision_score_grade",true);
			formObject.setVisible("DecisionHistory_Label17",true);

			// ++ below code already exist - 10-10-2017 this is uncommented at onsite but we have change it according to offshore
			formObject.setVisible("cmplx_Decision_LoanApprovalAuthority",true);			                	

			formObject.setVisible("DecisionHistory_Button4",true);			                	
			formObject.setVisible("DecisionHistory_Label18",true);
			formObject.setVisible("cmplx_Decision_CADDecisiontray",true);
			//formObject.setVisible("cmplx_Decision_ReferTo",true);
			formObject.setVisible("DecisionHistory_Label11",true);
			formObject.setVisible("cmplx_Decision_DecReasonCode",true);
			formObject.setVisible("DecisionHistory_Frame2",true);
			formObject.setVisible("DecisionHistory_Frame3",true);

			formObject.setVisible("cmplx_Decision_LoanApprovalAuthority",true);

			formObject.setTop("DecisionHistory_Label15",10);
			formObject.setTop("cmplx_Decision_score_grade",25);
			formObject.setLeft("DecisionHistory_Label15",272);
			formObject.setLeft("cmplx_Decision_score_grade",272);
			formObject.setTop("DecisionHistory_Label17",10);
			formObject.setTop("cmplx_Decision_LoanApprovalAuthority",25);

			formObject.setTop("DecisionHistory_Button4",10);
			formObject.setLeft("DecisionHistory_Button4",528);

			formObject.setTop("Decision_Label3",56);
			formObject.setTop("cmplx_Decision_Decision",72);
			formObject.setTop("DecisionHistory_Label18",56);
			formObject.setTop("cmplx_Decision_CADDecisiontray",92);

			//formObject.setTop("cmplx_Decision_ReferTo",72);
			formObject.setTop("DecisionHistory_Label11",56);
			formObject.setTop("cmplx_Decision_DecReasonCode",72);
			formObject.setLeft("DecisionHistory_Label11",528);
			formObject.setLeft("cmplx_Decision_DecReasonCode",528);
			formObject.setTop("Decision_Label4",118);
			formObject.setTop("cmplx_Decision_REMARKS",134);
			formObject.setLeft("Decision_Label4",776);
			formObject.setLeft("cmplx_Decision_REMARKS",776);

			formObject.setLeft("DecisionHistory_Label3",24);
			formObject.setLeft("cmplx_Decision_strength",24);

			formObject.setLeft("DecisionHistory_Label4",325);
			formObject.setLeft("cmplx_Decision_weakness",325);


			formObject.setTop("DecisionHistory_Label3",118);
			formObject.setTop("cmplx_Decision_strength",134);
			formObject.setTop("DecisionHistory_Label4",118);
			formObject.setTop("cmplx_Decision_weakness",134);


			formObject.setTop("DecisionHistory_Frame2",190);
			formObject.setTop("DecisionHistory_Frame3",375);
			formObject.setTop("DecisionHistory_Modify",458);
			formObject.setTop("Decision_ListView1",575);
			formObject.setTop("DecisionHistory_save",740);

			formObject.setNGValue("cmplx_Decision_LoanApprovalAuthority",formObject.getNGValue("cmplx_Decision_Highest_delegauth")==null?"":formObject.getNGValue("cmplx_Decision_Highest_delegauth") );

			formObject.setHeight("DecisionHistory_Frame1",1000);

			LoadPickList("cmplx_Decision_CADDecisiontray", "select '--Select--' union select convert(varchar, Refer_Credit) from ng_master_ReferCredit");
			int rowsExposure = formObject.getLVWRowCount("cmplx_Decision_ExposureDetails_grid");
			int rowsDeviation = formObject.getLVWRowCount("cmplx_Decision_DeviationDetails_grid");
			if(rowsExposure!=0){
				formObject.clear("cmplx_Decision_ExposureDetails_grid");
			}
			setExposureGridData();
			if(rowsDeviation!=0){
				formObject.clear("cmplx_Decision_DeviationDetails_grid");
			}
			setDeviationGridData();

			//Common function for decision fragment textboxes and combo visibility
			//decisionLabelsVisibility();
		} 	


	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		// ++ below code already exist - 10-10-2017
		mLogger.info("Inside PL_Initiation eventDispatched()--->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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


	public void setDeviationGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		String query = "select ReferReasoncode from ng_rlos_IGR_Eligibility_PersonalLoan where ReferReasoncode is not null and Child_Wi like '"+wi_name+"'";
		List<List<String>> record = formObject.getDataFromDataSource(query);
		if(record !=null && record.get(0)!=null && !record.isEmpty()){
			for(List<String> row:record){
				List<String> temp = new ArrayList<String>();
				int separatorIndex = row.get(0).lastIndexOf('-');
				temp.add(row.get(0).substring(0, separatorIndex));
				temp.add(row.get(0).substring(separatorIndex+1, row.get(0).length()));
				formObject.addItemFromList("cmplx_Decision_DeviationDetails_grid", temp);
			}
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


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		mLogger.info("PersonnalLoanS>  PL_Iniation--->Inside PL PROCESS submitFormCompleted()" + pEvent.getSource()); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:CAD_Analyst2");
		mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
		formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);

	}
	public void setExposureGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		String query = "select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi like '"+wi_name+"' and LoanStat='A' union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,OutstandingAmt as OutBal,PaymentsAmount as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi like '"+wi_name+"' and CardStatus ='A' union select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails where Child_Wi like '"+wi_name+"' and LoanStat='Active' union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,CurrentBalance as OutBal,PaymentsAmount as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails where Child_Wi like '"+wi_name+"' and CardStatus = 'Active'";
		List<List<String>> record = formObject.getDataFromDataSource(query);
		if(record !=null && record.get(0)!=null && record.isEmpty()){
			for(List<String> row:record){
				formObject.addItemFromList("cmplx_Decision_ExposureDetails_grid", row);
			}
		}
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

		saveIndecisionGrid();
	}

}

