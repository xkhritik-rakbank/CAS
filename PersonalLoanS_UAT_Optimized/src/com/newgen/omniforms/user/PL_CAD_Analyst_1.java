/*------------------------------------------------------------------------------------------------------


Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CAD_Analyst_1.java
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

public class PL_CAD_Analyst_1 extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		PersonalLoanS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception:"+e.getMessage());
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
		PersonalLoanS.mLogger.info("EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			//change by saurabh on 7th Dec
			formObject.setLocked("cmplx_Customer_NEP",true);
			
			formObject.setEnabled("Customer_save",true);


		}

		else if ("CAD_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_CADDecision_ScoreGrade",true);
			formObject.setLocked("cmplx_CADDecision_LoanApprovalAuthority",true);

			String query1="Select Product_Type,AgreementId,TotalOutstandingAmt,NextInstallmentAmt,Consider_For_Obligations,wi_name from ng_RLOS_CUSTEXPOSE_LoanDetails where wi_name='"+formObject.getWFWorkitemName()+"'";
			String query2="Select Deviation_Code_Refer,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'";
			

			List<List<String>> mylist=formObject.getDataFromDataSource(query1);
			PersonalLoanS.mLogger.info("Inside CAD_Decision: mylist is:"+mylist.toString());
			for(List<String> a:mylist)
				formObject.addItemFromList("cmplx_CADDecision_cmplx_gr_exposureDetails",a);

			List<List<String>> mylist1=formObject.getDataFromDataSource(query2);
			PersonalLoanS.mLogger.info("Inside CAD_Decision: mylist1 is:"+mylist.toString());
			for(List<String> a:mylist1)
				formObject.addItemFromList("cmplx_CADDecision_cmplx_gr_DeviationDetails",a);

			formObject.setNGValue("cmplx_CADDecision_ScoreGrade",formObject.getNGValue("Select Score_Grade,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'"));
			formObject.setNGValue("cmplx_CADDecision_LoanApprovalAuthority",formObject.getNGValue("Select Delegation_Authorithy,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'"));
		}
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
			PersonalLoanS.mLogger.info( "Emp Type Value is:"+EmpType);

			if("Salaried".equalsIgnoreCase(EmpType)|| "Salaried Pensioner".equalsIgnoreCase(EmpType))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 630);
				formObject.setHeight("IncomeDetails_Frame1", 605);  
			}
			else if("Self Employed".equalsIgnoreCase(EmpType))
			{                                                                                                              
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 300);
				formObject.setHeight("IncomeDetails_Frame1", 280);
			}

			
			formObject.setVisible("IncomeDetails_Label13",false);
			formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
			formObject.setVisible("IncomeDetails_Label15",true); 
			formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
			formObject.setVisible("IncomeDetails_Label16",true);
			formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
			// disha FSD	
			formObject.setEnabled("cmplx_IncomeDetails_grossSal",true);
			
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);

			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_IncomeDetails_DurationOfBanking",true);
				formObject.setLocked("cmplx_IncomeDetails_NoOfMonthsOtherbankStat",true);
			}
			formObject.setVisible("IncomeDetails_Frame3",false);
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			String App_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			formObject.setNGValue("cmplx_Liability_New_overrideIntLiab",true);
			formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",true);

			formObject.setLocked("ExtLiability_takeoverAMount",true);//Arun (12/08/17)
			formObject.setLocked("ExtLiability_QCAmt",true);//Arun (12/08/17)
			formObject.setLocked("ExtLiability_QCEMI",true);//Arun (12/08/17)

			formObject.setLocked("cmplx_Liability_New_DBR",true);
			formObject.setLocked("cmplx_Liability_New_TAI",true);
			formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			formObject.setVisible("ExtLiability_Label15",true);
			formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
			//commented by saurabh on 4th Dec.
			/*formObject.setVisible("Liability_New_Label1",false);//Arun (16/08/17)
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
			formObject.setNGValue("cmplx_Liability_New_overrideIntLiab",true);//Arun (27/09/17)
			formObject.setNGLVWRowHeight("Liability_New_Label8", 152);
			formObject.setNGLVWRowHeight("Liability_New_Text3", 168);										
			if (App_Type != "RESCH"){
				formObject.setVisible("Liability_New_Label6", false);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
			}
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			
			if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="true" || formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="true")
			{
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
				formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
				formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
				formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
				formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
				formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
				formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
				formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
				formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
				formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);

				formObject.setLocked("cmplx_EmploymentDetails_EmpStatus",false);
				formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
				formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
				formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
				formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
				
				formObject.setLocked("cmplx_EmploymentDetails_LOS",false);								

				formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
				formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);

				formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
				formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
				formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
				formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
				formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
			}
			if((formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="false") && (formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="false"))
			{
				// disha FSD
				formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL","CNOAL");
				formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CNOAL ( disabled) /Awaiting FVR");
				formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
				formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
				formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
				formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
				formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
				formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
				formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
				formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);

				formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
				formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
				formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
				formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
				formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
				

				formObject.setLocked("cmplx_EmploymentDetails_LOS",false);
				formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",false);
				formObject.setLocked("cmplx_EmploymentDetails_EmirateOfWork",false);
				formObject.setLocked("cmplx_EmploymentDetails_HeadOfficeEmirate",false);
				formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);
				formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
				formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
				formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
				formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
				formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
			}
			loadPicklist4();

		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			
			formObject.setVisible("ELigibiltyAndProductInfo_Frame5",false);

			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);


		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanDetails_Frame1",true);
			
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
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

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			//empty else if
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			//empty else if
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


		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}
		//disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			}
		//disha FSD
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
			loadPicklistRiskRating(); //Arun (10/10)
		}

		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setVisible("SmartCheck1_Label2",false);
			formObject.setVisible("SmartCheck1_CPVrem",false);
			formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setVisible("SmartCheck1_FCUrem",false);
			formObject.setLocked("SmartCheck1_Modify",true);
		}
		//disha FSD
		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist1();
			//  below code already exist - 10-10-2017
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			formObject.setVisible("DecisionHistory_Button5",true);
			formObject.setVisible("DecisionHistory_CheckBox1",true);
			formObject.setVisible("DecisionHistory_Label26",true);
			formObject.setVisible("DecisionHistory_Combo13",true);
			formObject.setLocked("cmplx_Decision_ReferTo", true);
			//changes by Arun on 07-10-2017 to hide the label Cad Decision Tray at cad analyist 1 WS
			
			formObject.setVisible("cmplx_Decision_ReferTo",true);
			formObject.setVisible("DecisionHistory_Button4",true);
			formObject.setVisible("DecisionHistory_Label13",true);
			formObject.setVisible("cmplx_Decision_Deviationcode",true);
			formObject.setVisible("DecisionHistory_Label14",true);
			formObject.setVisible("cmplx_Decision_Dectech_decsion",true);
			formObject.setVisible("DecisionHistory_Label15",true);
			formObject.setVisible("cmplx_Decision_score_grade",true);
			formObject.setVisible("DecisionHistory_Label16",true);
			formObject.setVisible("cmplx_Decision_Highest_delegauth",true);
			formObject.setVisible("cmplx_Decision_Manual_Deviation",true);
			formObject.setVisible("DecisionHistory_Button6",true);
			formObject.setVisible("cmplx_Decision_Manual_deviation_reason",true);

			formObject.setVisible("DecisionHistory_Label18",true);
			formObject.setVisible("cmplx_Decision_CADDecisiontray",true);

			formObject.setVisible("DecisionHistory_Label5",false);
			formObject.setVisible("cmplx_Decision_desc",false);
			formObject.setVisible("DecisionHistory_chqbook",false);						
			formObject.setVisible("DecisionHistory_Label6",false);
			formObject.setVisible("cmplx_Decision_IBAN",false);
			formObject.setVisible("DecisionHistory_Label7",false);
			formObject.setVisible("cmplx_Decision_AccountNo",false);
			formObject.setVisible("DecisionHistory_Label8",false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
			formObject.setVisible("DecisionHistory_Label9",false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber",false);

			formObject.setVisible("DecisionHistory_Label26",false);

			formObject.setVisible("cmplx_Decision_rejreason",false);
			formObject.setVisible("DecisionHistory_Button1",false);
			formObject.setVisible("DecisionHistory_Rejreason",false);


			formObject.setLocked("cmplx_Decision_Deviationcode",true);
			formObject.setLocked("cmplx_Decision_Dectech_decsion",true);
			formObject.setLocked("cmplx_Decision_score_grade",true);
			formObject.setLocked("cmplx_Decision_Highest_delegauth",true);

			formObject.setLocked("cmplx_Decision_Manual_deviation_reason",true);
			formObject.setLocked("DecisionHistory_Button6",true);

			formObject.setLocked("cmplx_Decision_CADDecisiontray",true);//Arun (08/10)
			// formObject.setLocked("cmplx_Decision_Manual_Deviation",true);//Arun (08/10)			
			formObject.setEnabled("cmplx_Decision_Manual_Deviation", true);	
			formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
			//commented by saurabh for testing.
			formObject.setLeft("DecisionHistory_Label3", 24);
			formObject.setLeft("cmplx_Decision_strength", 24);						
			formObject.setLeft("DecisionHistory_Label4", 302);
			formObject.setLeft("cmplx_Decision_weakness", 302);
			formObject.setLeft("Decision_Label4", 590);
			formObject.setLeft("cmplx_Decision_REMARKS", 590);						
			formObject.setLeft("DecisionHistory_Label13", 24);
			formObject.setLeft("cmplx_Decision_Deviationcode", 24);						
			formObject.setLeft("DecisionHistory_Label14", 302);
			formObject.setLeft("cmplx_Decision_Dectech_decsion", 302);
			formObject.setLeft("DecisionHistory_Label15", 590);
			formObject.setLeft("cmplx_Decision_score_grade", 590);
			formObject.setLeft("DecisionHistory_Label16", 822);
			formObject.setLeft("cmplx_Decision_Highest_delegauth", 822);
			formObject.setLeft("DecisionHistory_Button4", 674);
			formObject.setLeft("DecisionHistory_Button5", 1074);
			formObject.setLeft("DecisionHistory_Label18", 590);
			formObject.setLeft("cmplx_Decision_ReferTo", 590);
			formObject.setLeft("DecisionHistory_CheckBox1", 560);
			formObject.setLeft("DecisionHistory_Label26", 813);
			formObject.setLeft("DecisionHistory_Combo13", 813);
			formObject.setLeft("DecisionHistory_Label1",297);//Arun (03-12-17) to align left
			formObject.setLeft("DecisionHistory_ReferTo",297);//Arun (03-12-17) to align left

			formObject.setLeft("DecisionHistory_Label18",1074);
			formObject.setLeft("cmplx_Decision_CADDecisiontray",1074);
			formObject.setLeft("Decision_Label1",297);
			formObject.setTop("Decision_Label1",8);
			formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED",297);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",30);
			formObject.setTop("DecisionHistory_Label18",150);								
			formObject.setTop("cmplx_Decision_CADDecisiontray",190);							

			formObject.setTop("DecisionHistory_Label11",60); //Arun (03-12-17) to align it at correct top
			formObject.setTop("DecisionHistory_DecisionReasonCode",76); //Arun (03-12-17) to align it at correct top
			formObject.setLeft("DecisionHistory_Button6", 1074);
			formObject.setLeft("cmplx_Decision_Manual_deviation_reason", 1074);
			formObject.setLeft("cmplx_Decision_Manual_Deviation", 900);
			formObject.setTop("DecisionHistory_Button5", 72);
			formObject.setTop("DecisionHistory_Button6", 120);
			formObject.setTop("cmplx_Decision_Manual_Deviation", 104);
			formObject.setTop("cmplx_Decision_Manual_deviation_reason", 124);
			formObject.setTop("DecisionHistory_save",400);
			formObject.setTop("Decision_Label3", 56);
			formObject.setTop("cmplx_Decision_Decision", 72);
			formObject.setTop("DecisionHistory_Label1", 56);
			formObject.setTop("DecisionHistory_ReferTo",72);//Arun (01-12-17) to align top of this field
			formObject.setTop("cmplx_Decision_refereason", 72);								
			
			formObject.setTop("cmplx_Decision_ReferTo", 72);
			formObject.setTop("DecisionHistory_Label3", 104);
			formObject.setTop("cmplx_Decision_strength", 120);
			formObject.setTop("DecisionHistory_Label4", 104);
			formObject.setTop("cmplx_Decision_weakness", 120);
			formObject.setTop("Decision_Label4", 104);
			formObject.setTop("cmplx_Decision_REMARKS", 120);						
			formObject.setTop("DecisionHistory_Label13", 172);
			formObject.setTop("cmplx_Decision_Deviationcode", 188);						
			formObject.setTop("DecisionHistory_Label14", 172);
			formObject.setTop("cmplx_Decision_Dectech_decsion", 188);
			formObject.setTop("DecisionHistory_Label15", 172);
			formObject.setTop("cmplx_Decision_score_grade", 188);
			formObject.setTop("DecisionHistory_Label16", 172);
			formObject.setTop("cmplx_Decision_Highest_delegauth", 188);
			formObject.setTop("Decision_ListView1", 226);								
			formObject.setTop("DecisionHistory_CheckBox1", 15);
			formObject.setTop("DecisionHistory_Label26", 8);
			formObject.setTop("DecisionHistory_Combo13", 23);

			formObject.setTop("cmplx_Decision_Manual_Deviation", 120);

			formObject.setTop("DecisionHistory_Button4", 8);

			LoadPickList("cmplx_Decision_CADDecisiontray", "select '--Select--' union select convert(varchar, Refer_Credit) from ng_master_ReferCredit");
			//condition added by saurabh on 15th Oct.
			if(!"Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){

				formObject.setLocked("cmplx_Decision_CADDecisiontray",false);
			}
			//Common function for decision fragment textboxes and combo visibility
		
		} 	


	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		PersonalLoanS.mLogger.info("EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		
		PersonalLoanS.mLogger.info("Inside PL PROCESS submitFormCompleted()" + pEvent.getSource()); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String> ();
		//disha FSD cad delegation procedure changes
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
		objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
		objInput.add("Text:"+formObject.getNGValue("cmplx_Decision_Highest_delegauth"));
		PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3));

		//  below code already exist - 10-10-2017
		// disha CAD deligation hierarchy should not work when decision selected if 'Refer to Credit'
		if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{  
			formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);

		}

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		//disha FSD
		if("Refer to Credit".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("q_MailTo",formObject.getNGValue("cmplx_Decision_ReferTo"));
		}
		saveIndecisionGrid();
	}

}

