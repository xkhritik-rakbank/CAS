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
		PersonalLoanS.mLogger.info("Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info("Exception:"+e.getMessage());
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
		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();


		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			loadPicklistProduct("Personal Loan");
			
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			
			

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

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExtLiability_Frame1",true);
			/*commented by saurabh on 4th Dec
			formObject.setVisible("Liability_New_Label1",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_MOB",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Label3",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Utilization",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Label5",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Outstanding",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_delinin3",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_DPD30inlast6",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_DPD30inlast18",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Label2",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_writeoff",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Label4",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_worststatuslast24",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Label8",false);
			formObject.setVisible("Liability_New_rejAppsinlast3months",false);
			*/
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

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
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
		}


		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			notepad_load();
			 notepad_withoutTelLog();
		
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
			LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
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

		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExternalBlackList_Frame1",true);
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

	

		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

			//empty else if

		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}
		// disha FSD
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist1();

			//  below code already exist - 10-10-2017
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");

			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setVisible("Decision_Label1",false);
			formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
			formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setVisible("DecisionHistory_Label1",false);
			formObject.setVisible("DecisionHistory_ReferTo",false);//arun (03-12-17) to hide this field
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

			//  below code already exist - 10-10-2017 this is uncommented at onsite but we have change it according to offshore
			formObject.setVisible("cmplx_Decision_LoanApprovalAuthority",true);			                	

			formObject.setVisible("DecisionHistory_Button4",true);			                	
			formObject.setVisible("DecisionHistory_Label18",true);
			formObject.setVisible("cmplx_Decision_CADDecisiontray",true);
			
			formObject.setVisible("DecisionHistory_Label11",true);
			formObject.setVisible("DecisionHistory_DecisionReasonCode",true);
			formObject.setVisible("DecisionHistory_Frame2",true);
			formObject.setVisible("DecisionHistory_Frame3",true);

			formObject.setVisible("cmplx_Decision_LoanApprovalAuthority",true);
			//change by saurabh on 8th nov 2017.
			formObject.setVisible("DecisionHistory_Label27",true);
			formObject.setVisible("cmplx_Decision_TotalOutstanding",true);
			formObject.setVisible("DecisionHistory_Label28",true);
			formObject.setVisible("cmplx_Decision_TotalEMI",true);
			
			formObject.setTop("DecisionHistory_Label27",385);
			formObject.setTop("cmplx_Decision_TotalOutstanding",400);
			formObject.setTop("DecisionHistory_Label28",385);
			formObject.setTop("cmplx_Decision_TotalEMI",400);
			
			formObject.setLeft("DecisionHistory_Label27",620);
			formObject.setLeft("cmplx_Decision_TotalOutstanding",620);
			formObject.setLeft("DecisionHistory_Label28",840);
			formObject.setLeft("cmplx_Decision_TotalEMI",840);
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

			
			formObject.setTop("DecisionHistory_Label11",56);
			formObject.setTop("DecisionHistory_DecisionReasonCode",72);
			formObject.setLeft("DecisionHistory_Label11",528);
			formObject.setLeft("DecisionHistory_DecisionReasonCode",528);
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

			//top changes done by saurabh on 8th nov 2017.
			formObject.setTop("DecisionHistory_Frame2",190);
			formObject.setTop("DecisionHistory_Frame3",425);
			formObject.setTop("DecisionHistory_Modify",458);
			formObject.setTop("Decision_ListView1",625);
			formObject.setTop("DecisionHistory_save",790);

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
			
		} 	


	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		//  below code already exist - 10-10-2017
		PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()--->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

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
		//empty method

	}


	public void initialize() {
		
		//empty method
	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		
		//empty method
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		
		//empty method
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		
		PersonalLoanS.mLogger.info("PersonnalLoanS>  PL_Iniation--->Inside PL PROCESS submitFormCompleted()" + pEvent.getSource()); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:CAD_Analyst2");
		PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
		formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);

	}
	public void setExposureGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		//below code was present in Saurabh PC without comment but still added.
		float totalOut = 0;
		float totalEMI = 0;
		String query = "select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi like '"+wi_name+"' and LoanStat!='Pipeline' union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,OutstandingAmt as OutBal,PaymentsAmount as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi like '"+wi_name+"' and CardStatus !='Pipeline' union select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails where Child_Wi like '"+wi_name+"' and LoanStat!='Pipeline' union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,CurrentBalance as OutBal,PaymentsAmount as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails where Child_Wi like '"+wi_name+"' and CardStatus!='Pipeline'";
		PersonalLoanS.mLogger.info("Query for exposure grid is: "+query);
	
		List<List<String>> record = formObject.getDataFromDataSource(query);
		if(record !=null && record.size()>0 && record.get(0)!=null)  
		{
			for(List<String> row:record){
				PersonalLoanS.mLogger.info("CFO received is: "+row.get(5));
				if( row.get(5) == null ||"null".equalsIgnoreCase(row.get(5))|| "true".equalsIgnoreCase(row.get(5)) || row.get(5).equalsIgnoreCase(null) ||"".equalsIgnoreCase(row.get(5))){
					PersonalLoanS.mLogger.info("row.get(5) is: "+row.get(5));
					row.set(5, "Yes");
					PersonalLoanS.mLogger.info("row.get(5) is: "+row.get(5));
				}
				else if("false".equalsIgnoreCase(row.get(5))){
					PersonalLoanS.mLogger.info("row.get(5) is: "+row.get(5));
					row.set(5, "No");
					PersonalLoanS.mLogger.info("row.get(5) is: "+row.get(5));
				}
				PersonalLoanS.mLogger.info("row.get(3) is: "+row.get(3));
				PersonalLoanS.mLogger.info("row.get(4) is: "+row.get(4));
				if(row.get(3)!=null && !"null".equalsIgnoreCase(row.get(3))){
				totalOut += Float.parseFloat(row.get(3));
				}
				if(row.get(4)!=null && !"null".equalsIgnoreCase(row.get(4))){
				totalEMI += Float.parseFloat(row.get(4));
				}
				row.add(formObject.getWFWorkitemName());
				formObject.addItemFromList("cmplx_Decision_ExposureDetails_grid", row);
			}
		}
		if(formObject.isVisible("ExtLiability_Frame4")){
		if(formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid")>0){
		for(int i=0;i<formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");i++){
			totalOut+=Float.parseFloat(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 12));
			totalEMI+=Float.parseFloat(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 2));
		}
		}
		}
		PersonalLoanS.mLogger.info("Total Out is: "+String.valueOf(totalOut));
		PersonalLoanS.mLogger.info("Total EMI is: "+String.valueOf(totalEMI));
		formObject.setNGValue("cmplx_Decision_TotalOutstanding", String.valueOf(totalOut));
		formObject.setNGValue("cmplx_Decision_TotalEMI", String.valueOf(totalEMI));
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

		saveIndecisionGrid();
	}

}

