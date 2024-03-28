/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Compliance.java
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


public class PL_Compliance extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

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

	public void fragment_loaded(ComponentEvent pEvent)
	{
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("IncomeDetails_Frame1",true);
							// ++ below code added - 16-10-2017 self employed frame is made invisible
							formObject.setVisible("IncomeDetails_Frame3", false);
							formObject.setHeight("Incomedetails", 630);
	                        formObject.setHeight("IncomeDetails_Frame1", 605);
	                     // ++ above code added - 16-10-2017 self employed frame is made invisible
						} 

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExtLiability_Frame1",true);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

							formObject.setLocked("EMploymentDetails_Frame1",true);
							// ++ below code already exist - 10-10-2017
							loadPicklist4();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
							// ++ below code added - 16-10-2017 PL FSD 2.7.3
							LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
		                    LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' union select convert(varchar, description) from NG_MASTER_instrumentType");
		                    LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
		                    // ++ above code added - 16-10-2017 PL FSD 2.7.3
		
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanDetails_Frame1",true);
			//++below code added by nikhil to set loan details alignment
			formObject.setHeight("LoanDetails_Frame1", 450);
			formObject.setTop("Risk_Rating",470);
			
							// ++ below code added - 16-10-2017 PL FSD 2.7.3
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
			             // ++ above code added - 16-10-2017 PL FSD 2.7.3
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
							// ++ below code added - 16-10-2017 PL FSD 2.7.3
							LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
							// ++ above code added - 16-10-2017 PL FSD 2.7.3
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame1",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("OECD_Frame8",true);
							// ++ below code added - 16-10-2017 PL FSD 2.7.3
							LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                	LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
		                	LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                	// ++ above code added - 16-10-2017 PL FSD 2.7.3
		}

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {	

			formObject.setLocked("Compliance_Frame1",true);
			//++Below code added by nikhil 13/11/2017 for Code merge
			
			int framestate=formObject.getNGFrameState("Risk_Rating");
			if(framestate!=0)
			{
			formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
			}
			autopopulate_Compliance_risk(formObject);
			//Change by prateek to handle null before converting to float 
			float risk_rating=0F;
			float risk_rating_limit;
			try
			{
			String riskRatingRiskScore = formObject.getNGValue("cmplx_RiskRating_Total_riskScore");
			
			if(!"".equals(riskRatingRiskScore))
			{
				risk_rating = Float.parseFloat(riskRatingRiskScore);
			}	
			
			risk_rating_limit=Float.parseFloat(NGFUserResourceMgr_PL.getResourceString_plValidations("Risk_rating_limit"));
			PersonalLoanS.mLogger.info("risk rating limit :::"+risk_rating_limit);
			PersonalLoanS.mLogger.info("risk rating :::"+risk_rating);
			if(risk_rating > risk_rating_limit)
			{
				PersonalLoanS.mLogger.info("inside rr>limit");
				formObject.setLocked("cmplx_Compliance_ComplianceRemarks", false);
			}
			else
			{
				PersonalLoanS.mLogger.info("inside rr<limit");
				formObject.setLocked("cmplx_Compliance_ComplianceRemarks", true);
				
			}
			
			
			
			//--Above code added by nikhil 13/11/2017 for Code merge
			
			//++ Below Code added By Yash on Oct 24, 2017  to fix : "To populate the description of country in residence country and birth country" : Reported By Shashank on Oct 09, 2017++
			 
            loadInComplianceGrid();
            
          //++ Above Code added By Yash on Oct 24, 2017  to fix : "To populate the description of country in residence country and birth country" : Reported By Shashank on Oct 09, 2017++
            formObject.setLocked("Compliance_CompRemarks",false);			            
			formObject.setLocked("Compliance_Modify",false); 
			formObject.setLocked("Compliance_Save",false);
			//formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);		//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.

			

			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");

			formObject.setNGFrameState("World_Check",0);
			int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
			PersonalLoanS.mLogger.info("CC value of world check row--->value of n "+n);
			//below code added to load compliance grid with world check rows
			//++below code added by nikhil 5/12/17 for compliance issues
			int compliance_row_count=formObject.getLVWRowCount("cmplx_Compliance_cmplx_gr_compliance");
			if(n>compliance_row_count)
			{
			for(int i=0;i<n;i++)
			{ 
				String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,12);
			
				PersonalLoanS.mLogger.info("CC value of world check UID"+UID);
				formObject.setNGValue("Compliance_UID",UID);

				String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,15);
				
				PersonalLoanS.mLogger.info("CC value of world check EI "+EI);
				formObject.setNGValue("Compliance_EI",EI);
				
				String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,0);
				PersonalLoanS.mLogger.info("CC value of world check Name"+Name);	
				formObject.setNGValue("Compliance_Name",Name);
			
				String Dob = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,2);
				PersonalLoanS.mLogger.info("CC value of world check Dob"+Dob);
				formObject.setNGValue("Compliance_DOB",Dob);
				String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,7);
				
				PersonalLoanS.mLogger.info("Citizenship"+Citizenship);	
				formObject.setNGValue("Compliance_Citizenship",Citizenship);
				String Remarks = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,16);
				
				formObject.setNGValue("Compliance_Remarks",Remarks);
				String Id_No = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,1);
				formObject.setNGValue("Compliance_IdentificationNumber",Id_No);
				
				String Age = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,3);
				formObject.setNGValue("Compliance_Age",Age);
				

				String Position = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,4);
				formObject.setNGValue("Compliance_Positon",Position);
				
				String Deceased = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,5); 
				formObject.setNGValue("Compliance_Deceased",Deceased);
				
				String Category = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,6);
				formObject.setNGValue("Compliance_Category",Category);
				
				String Location = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,8);
				formObject.setNGValue("Compliance_Location",Location);
				
				String Identification = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,9); 
				formObject.setNGValue("Compliance_Identification",Identification);
				
				String Biography = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,10); 
				formObject.setNGValue("Compliance_Biography",Biography);
				
				String Reports = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,11); 
				formObject.setNGValue("Compliance_Reports",Reports);
				
				String Entered_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,13);
				formObject.setNGValue("Compliance_EntertedDate",Entered_Date);
				
				String Updated_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,14);
				formObject.setNGValue("Compliance_UpdatedDate",Updated_Date);
				
				 

				
				String Document = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,18);  
				formObject.setNGValue("Compliance_Document",Document);
				
				String Decision = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,19);
				formObject.setNGValue("Compliance_Decision",Decision);
				
				String Match_Rank = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,20);
				formObject.setNGValue("Compliance_MatchRank",Match_Rank);
				
				String Alias = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,21);
				formObject.setNGValue("Compliance_Alias",Alias);
				
				String birth_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,22);
				formObject.setNGValue("Compliance_BirthCountry",birth_Country);
				
				String resident_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,23);
				formObject.setNGValue("Compliance_ResidenceCountry",resident_Country);
				
				String Address = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,24);
				formObject.setNGValue("Compliance_Address",Address);
				
				String Gender = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,25);
				formObject.setNGValue("Compliance_Gender",Gender);
				
				String Listed_On = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,26);
				formObject.setNGValue("Compliance_ListedOn",Listed_On);
				
				String Program = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,27);
				formObject.setNGValue("Compliance_Program",Program);
				
				String external_ID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,28);
				formObject.setNGValue("Compliance_ExternalID",external_ID);
				
				String data_Source = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,29);
				
				formObject.setNGValue("Compliance_DataSource",data_Source);

				formObject.setNGValue("Compliance_winame",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");

				
			}
			}
			//++below code commented by nikhil 05/12/17
			//formObject.RaiseEvent("WFSave");
			formObject.setNGFrameState("World_Check",1);
			}
			catch(Exception ex)
			{
				printException(ex);
			}
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			formObject.setVisible("DecisionHistory_Label3",false);
			formObject.setVisible("cmplx_Decision_strength",false);
			formObject.setVisible("DecisionHistory_Label4",false);
			formObject.setVisible("cmplx_Decision_weakness",false);

			formObject.setTop("Decision_Label3",7);
			formObject.setTop("cmplx_Decision_Decision",22);							
			formObject.setTop("Decision_Label4",7);
			formObject.setTop("cmplx_Decision_REMARKS",22);
			
			formObject.setTop("Decision_ListView1",96);							
			formObject.setTop("DecisionHistory_save",288);

			formObject.setLeft("Decision_Label4",555);
			formObject.setLeft("cmplx_Decision_REMARKS",555);	
			formObject.setLeft("DecisionHistory_Label11",297);
			formObject.setLeft("DecisionHistory_DecisionReasonCode",297);

							// ++ below code already exist - 10-10-2017
							//disha FSD P1 - CPV required field to be disabled at Decision tab
		                	formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
							
							
							//formObject.setVisible("DecisionHistory_Modify", true);
							//formObject.setVisible("cmplx_Decision_Status", true);
							//formObject.setVisible("DecisionHistory_Label2", true);
							
							//Common function for decision fragment textboxes and combo visibility
							//decisionLabelsVisibility();
		} 	
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();

			}
						// ++ below code added - 16-10-2017 frame lock code
						if (pEvent.getSource().getName().equalsIgnoreCase("RiskRating")) {
							
				 			formObject.setLocked("RiskRating_Frame1",true);
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
							
							formObject.setLocked("FinacleCRMIncident_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
		
							formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
		
							formObject.setLocked("FinacleCore_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
		
							formObject.setLocked("MOL1_Frame1",true);
							loadPicklistMol();
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
							
							formObject.setLocked("WorldCheck1_Frame1",true);
							loadPicklist_WorldCheck();
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
							
							formObject.setLocked("PartMatch_Frame1",true);
							LoadPickList("PartMatch_nationality", "select '--Select--' union select convert(varchar, Description) from ng_MASTER_Country with (nolock)");
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
							
							formObject.setLocked("RejectEnq_Frame1",true);
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
							
							formObject.setLocked("ExternalBlackList_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {

							formObject.setLocked("SalaryEnq_Frame1",true);
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc")){
							formObject.setLocked("IncomingDoc_Frame1", true);
							
							fetchIncomingDocRepeater();
							formObject.setVisible("cmplx_DocName_OVRemarks", false);
					        formObject.setVisible("cmplx_DocName_OVDec",false);
					        formObject.setVisible("cmplx_DocName_Approvedby",false);
						}
						// ++ above code added - 16-10-2017 frame lock code
	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
		saveIndecisionGrid();
	}

}

