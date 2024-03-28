/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_SmartCPV.java
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

import java.util.Arrays;
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

public class PL_SmartCPV extends PLCommon implements FormListener 
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 9, true);		
        makeSheetsInvisible("Tab1", "16");	//Hide Dispatch
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			enable_CPV();
		}catch(Exception e)
		{
			mLogger.info("PL SmartCPV"+ "Exception:"+e.getMessage());
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			//below code added by nikhil 11/12/17
			LoadView(pEvent.getSource().getName());

			loadPicklistCustomer();
			
			formObject.setLocked("Customer_Frame1",true);
			// hritik 22.6.21 PCASI 3424 
			formObject.setLocked("cmplx_Customer_NEP",true);
		}	
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("***********Inside Smart_check");
			//formObject.setLocked("SmartCheck_Frame1",true);
			formObject.setLocked("SmartCheck_creditrem", true);
			//formObject.setLocked("SmartCheck_Add", true); line commented for PCASI-3500
		}
		
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		} 

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");

		}
		
		//code changes by bandana starts
		//added by nikhil as per CC FSD
				else if("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					formObject.setLocked("Details", true);
					loadPicklist_ServiceRequest();
				}
		//code changes by bandana ends
		
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			loadPicklistELigibiltyAndProductInfo();

			loadEligibilityData();
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanDetails_Frame1",true);
		}
		
		//hritik 22.6.21 PCASI 3497
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("PartMatch_Frame1",true);
		}
		else if ("EmploymentVerification_s2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			
			formObject.setLocked("EmploymentVerification_s2_Frame3",true);
			formObject.setLocked("EmploymentVerification_s2_search_TL_number",true);
			
		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("SmartCheck1_CreditRemarks",true);
			formObject.setLocked("SmartCheck1_FCURemarks",true);
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("FinacleCore_Frame6",true);
			formObject.setLocked("FinacleCore_Frame5",true);
			formObject.setLocked("FinacleCore_save",true);
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("MOL1_Frame1",true);
			formObject.setLocked("MOL1_Save",true);
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			formObject.setLocked("WorldCheck1_Save",true);
		}
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
			loadPicklistRiskRating();
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			String Activity=formObject.getWFActivityName();
			if("Smart_CPV".equalsIgnoreCase(Activity)){
				formObject.setLocked("CardDetails_Frame1",true);
				formObject.setLocked("cmplx_CardDetails_CustClassification",true);
			}	// PCASI 3495
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			//Code ended by siva on 01112019 for PCAS-1401
		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadpicklistCardDetails();
			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			Loadpicklistfatca();
			formObject.setLocked("FATCA_Frame6",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame7",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
		}
		//changes done by shweta for jira# PCAS-2372
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");//pcasi-1003
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			formObject.setLocked("CustDetailVerification_Frame1",true);
			//below code added by nikhil 16/12/17
			enable_custVerification();
			//done by nikhil for PCAS-2358
		}

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();
		}

		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("HomeCountryVerification_Frame1",true);
			enable_homeVerification();
		}

		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ResidenceVerification_Frame1",true);
			enable_ResVerification();
		}

		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
			
		}

		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();
		}							

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 and Sno !=4 order by code"); //Modified by Rajan for PCASP-2216
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			formObject.setVisible("OfficeandMobileVerification_Enable", false);//arun 14/12/17
			//below code added by nikhil 16/12/17
			//enable_officeVerification();
			formObject.setLocked("cmplx_OffVerification_fxdsal_override", true);
			formObject.setLocked("cmplx_OffVerification_accprovd_override", true);
			formObject.setLocked("cmplx_OffVerification_desig_override", true);
			formObject.setLocked("cmplx_OffVerification_doj_override", true);
			formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", true);
				
			
			
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanandCard_Frame1",true);
			enable_loanCard();

			
		}

		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		//	formObject.setLocked("NotepadDetails_Frame1",true);
								//++ below code added by abhishek to load notepad details as per FSD 2.7.3
								notepad_load();
						    	formObject.setVisible("NotepadDetails_Frame3",true);
								//-- Above code added by abhishek to load notepad details as per FSD 2.7.3
							}

								

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist1();
								// ++ below code already exist - 10-10-2017
								formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			                	//disha FSD P1 - CPV required field to be disabled at Decision tab
			                	formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
								// ++ above code already exist - 10-10-2017
			//Common function for decision fragment textboxes and combo visibility
		
			                	//++ below code added by abhishek to enable/disable/hide/unhide fileds as per FSD 2.7.3
								//for decision fragment made changes 8th dec 2017             
								/*	formObject.setVisible("DecisionHistory_CheckBox1",false);
		                        formObject.setVisible("DecisionHistory_Label1",false);
		                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);
		                        formObject.setVisible("DecisionHistory_Label3",false);
		                        formObject.setVisible("DecisionHistory_Combo3",false);
		                        formObject.setVisible("DecisionHistory_Label6",false);
		                        formObject.setVisible("DecisionHistory_Combo6",false);
		                        formObject.setVisible("DecisionHistory_Decision_Label4",false);
		                        formObject.setVisible("cmplx_DEC_Remarks",false);
		                        formObject.setVisible("DecisionHistory_Label8",false);
		                        formObject.setVisible("DecisionHistory_Text4",false);
		                        formObject.setVisible("DecisionHistory_Label7",false);
		                        formObject.setVisible("DecisionHistory_Text3",false);
		                        formObject.setVisible("DecisionHistory_Label2",false);
		                        formObject.setVisible("DecisionHistory_Text2",false);
		                        formObject.setVisible("cmplx_DEC_ReferReason",false);
		                        formObject.setVisible("cmplx_DEC_Description",false);
		                        formObject.setVisible("cmplx_DEC_Strength",false);
		                        formObject.setVisible("cmplx_DEC_Weakness",false);
		                        formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
								
								
		                       
		                        formObject.setVisible("DecisionHistory_Decision_Label1",false);
		                        formObject.setVisible("DecisionHistory_Label10",false);
		                        formObject.setVisible("cmplx_DEC_New_CIFID",false);
		                        formObject.setVisible("DecisionHistory_Button2",false);
		                        formObject.setVisible("DecisionHistory_chqbook",false);
		                        formObject.setVisible("DecisionHistory_Label6",false);
		                        formObject.setVisible("cmplx_DEC_IBAN_No",false);
		                        formObject.setVisible("cmplx_DEC_NewAccNo",false);
		                        formObject.setVisible("cmplx_DEC_ChequebookRef",false);
		                        formObject.setVisible("DecisionHistory_Label9",false);
		                        formObject.setVisible("cmplx_DEC_DCR_Refno",false);
		                        formObject.setVisible("DecisionHistory_Label5",false);
		                        formObject.setVisible("DecisionHistory_Label4",false);
		                        formObject.setVisible("DecisionHistory_Label27",false);
		                        formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
		                        formObject.setVisible("DecisionHistory_Decision_Label4",true);
		                        formObject.setVisible("cmplx_DEC_Remarks",true);
		                        formObject.setVisible("DecisionHistory_nonContactable",true);
		                        formObject.setVisible("DecisionHistory_cntctEstablished",true);
		                        formObject.setVisible("DecisionHistory_Label11",true);
		                        formObject.setVisible("cmplx_DEC_Decision_Reasoncode",true);
		                        formObject.setVisible("DecisionHistory_Label12",true);
		                        formObject.setVisible("cmplx_DEC_NoofAttempts",true);
		                        
		                        formObject.setEnabled("DecisionHistory_cntctEstablished", false);*/
		                        
		                        PersonalLoanS.mLogger.info("***********Inside decision history");
		                   
		                    	fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label37,cmplx_Decision_NoOfAttempts_Smart#DecisionHistory_Label36,cmplx_Decision_SetReminder_Smart#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#DecisionHistory_nonContactable#DecisionHistory_cntctEstablished#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
		                    	
		                    	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
		                    	
		                    	formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
		                    	formObject.setTop("DecisionHistory_save",500);
		                    	PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
		                    	//for decision fragment made changes 8th dec 2017
		            			// ++ below code already exist - 10-10-2017
								formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			                	//disha FSD P1 - CPV required field to be disabled at Decision tab
			                	formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
								// ++ above code already exist - 10-10-2017
		                        loadPicklist3();
			            		//-- Above code added by abhishek to enable/disable/hide/unhide fileds  as per FSD 2.7.3
		                        //below code added by nikhil PCAS-2490
		                        formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
		} 	

	
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		//mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

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


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		//empty method

	}


	public void initialize() {
		//empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method
		CustomSaveForm();
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
			//empty method

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

			if(formObject.isVisible("SmartCheck")==false){ 
				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
				}
			/*int n=formObject.getLVWRowCount("cmplx_SmartCheck_SmartCheckGrid");
			for(int i=0;i<n;i++){
				formObject.setNGValue("cmplx_SmartCheck_SmartCheckGrid", i, 3, "N");
				PersonalLoanS.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_SmartCheck_SmartCheckGrid",i,1)+formObject.getNGValue("cmplx_SmartCheck_SmartCheckGrid",i,3));
			}*/
			formObject.saveFragment("Smart_check");

			formObject.setNGValue("Smart_CPV_DeC", formObject.getNGValue("cmplx_Decision_Decision"));
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_SetReminder_Smart")) && "Smart CPV Hold".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
				SetReminder_Smart(formObject);
			}
			//Changed by aman for Decision Name change
			saveIndecisionGrid();
		CustomSaveForm();
		//formObject.setNGValue("CPV_dec", formObject.getNGValue("cmplx_DecisionHistory_Decision"));
	
		//loadInDecGrid();--commented by akshay on 10/1/18
		}
		catch (Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
		}
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
