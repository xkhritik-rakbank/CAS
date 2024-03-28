/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Hold_CPV.java
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

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_CPV_Analyst extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 9, true);
        makeSheetsInvisible("Tab1", "16"); //Hide Dispatch Tab
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
				formObject.setVisible("Card_Details", true);	
			}
			formObject.setVisible("ReferHistory",true);
			enable_CPV();
		}catch(Exception e)
		{
			printException(e);        
		}
		 CheckforRejects("CPV_Analyst");
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

		PersonalLoanS.mLogger.info(" In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			
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

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistELigibiltyAndProductInfo();

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
			loadEligibilityData();
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			
			formObject.setLocked("LoanDetails_Frame1",true);
			//below code added by nikhil 18/1/18
			formObject.setEnabled("cmplx_LoanDetails_fdisbdate", false);
			formObject.setEnabled("cmplx_LoanDetails_frepdate", false);
			formObject.setEnabled("cmplx_LoanDetails_maturitydate", false);
			//below code added 12/3 proc-6149
			//PROC-7659 AMan 
			formObject.setEnabled("cmplx_LoanDetails_paidon", false);
			formObject.setEnabled("cmplx_LoanDetails_trandate", false);
			formObject.setEnabled("cmplx_LoanDetails_chqdat", false);
			//PROC-7659 AMan
			//PROC-7658 AMan 
			formObject.setEnabled("LoanDetails_duedate", false);
			formObject.setEnabled("LoanDetails_disbdate", false);
			formObject.setEnabled("LoanDetails_payreldate", false);
			//PROC-7658 AMan
			
			formObject.setLocked("LoanDetails_Button1", false);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

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
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
			formObject.setLocked("SupplementCardDetails_DOB",true);
			formObject.setLocked("SupplementCardDetails_idIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaIssueDate",true);
			formObject.setLocked("SupplementCardDetails_PassportIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaExpiry",true);
			formObject.setLocked("SupplementCardDetails_EmiratesIDExpiry",true);
			formObject.setLocked("SupplementCardDetails_PassportExpiry",true);
			
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("cmplx_FATCA_SignedDate",true);
			formObject.setLocked("cmplx_FATCA_ExpiryDate",true);
			Loadpicklistfatca();
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame7",true);
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
		}//by shweta
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())){
			enable_homeVerification();
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			enable_ResVerification();
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();
			
		}
		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
			
		}
		
		

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//code changed by nikhil for CPV Changes 16-04-2019
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");//pcasi-1003
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			//code changed by nikhil for CPV Changes 16-04-2019
			formObject.setLocked("CustDetailVerification_Frame1",true);
			enable_custVerification();
		}

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//nikhil code moved from frame expand event for PCAS-2239
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 and Sno !=4 order by code"); //Modified by Rajan for PCASP-2216
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			//++ below code added by abhishek as per CC FSD 2.7.3
			formObject.setEnabled("OfficeandMobileVerification_Enable", true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
			formObject.setLocked("cmplx_OffVerification_fxdsal_override", true);
			formObject.setLocked("cmplx_OffVerification_accprovd_override", true);
			formObject.setLocked("cmplx_OffVerification_desig_override", true);
			formObject.setLocked("cmplx_OffVerification_doj_override", true);
			formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", true);
			
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_LoanandCard_loanamt_val",true);
			formObject.setLocked("cmplx_LoanandCard_tenor_val",true);
			formObject.setLocked("cmplx_LoanandCard_emi_val",true);
			formObject.setLocked("cmplx_LoanandCard_islorconv_val",true);
			formObject.setLocked("cmplx_LoanandCard_firstrepaydate_val",true);
			formObject.setLocked("cmplx_LoanandCard_cardtype_val",true);
			formObject.setLocked("cmplx_LoanandCard_cardlimit_val",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//for decision fragment made changes 8th dec 2017
			loadPicklist1();
			openCPVtabs(formObject);
			// ++ below code already exist - 10-10-2017
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			
			PersonalLoanS.mLogger.info("***********Inside checker decision history");
         	fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label12,cmplx_Decision_NoofAttempts#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
 			PersonalLoanS.mLogger.info("***********Inside checker after fragment alignment decision history");
 			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//added by shweta
			formObject.setVisible("ReferHistory",true);
			formObject.setTop("ReferHistory", 500);
			
								
			                
        	/*formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
        	
        	formObject.setVisible("cmplx_Decision_waiveoffver",false);

        	
        	formObject.setTop("Decision_Label1", 8);
        	formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);


        	formObject.setTop("cmplx_Decision_cmplx_gr_decision", 226);		
        	formObject.setTop("DecisionHistory_save", 400);	*/
			
			//for decision fragment made changes 8th dec 2017
			
        	//Common function for decision fragment textboxes and combo visibility
        	//decisionLabelsVisibility();
		} 	
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			formObject.setVisible("NotepadDetails_Frame3",true);
			
		}
		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			 fetchIncomingDocRepeater();
			 }*/
		else if ("Fpu_Grid".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
			LoadPickList("cmplx_FPU_Grid_Officer_Name", "select ' --Select-- 'as UserName union select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
		}
		
	}

	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		//PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()--->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		CustomSaveForm();
		//empty method
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		CustomSaveForm();
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "PL_CPV_Analyst--->"+"Inside submitFormCompleted()"+formObject.getWFWorkitemName());

		String Query ="Select CA_Refer_DDVT from NG_PL_EXTTABLE where Pl_Wi_Name ='"+formObject.getWFWorkitemName()+"'";
		int count=0;
		try
		{
			PersonalLoanS.mLogger.info("query name :"+Query);
			List<List<String>> result=formObject.getDataFromDataSource(Query);
			if(!result.isEmpty()){
				if("Y".equalsIgnoreCase(result.get(0).get(0)))
				{
					count++;
				}
			}
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info("Exception occured in Check_CPV_Refer_DDVT" +ex.getMessage());
		}
		if(count>0)
		{
			String AlertMsg="Workitem Referred to DDVT by UW Unit!";
			throw new ValidatorException(new FacesMessage(AlertMsg));
		}
	}
	



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		//added by isha for PCSP-459
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			Other_Detail_Match_check();
		}//code sync with CC
		formObject.setNGValue("IS_CPV", "N");

		try{
			//below code added by nikhil 7/12/17
			//formObject.setNGValue("CPV_dec", formObject.getNGValue("cmplx_Decision_Decision"));
			CustomSaveForm();
			LoadReferGrid();
			//added by isha for jira PCSP-649 
			//added by aastha for jira Sprint 4- WI ALlocation Logic
			//formObject.setNGValue("cpv_user", formObject.getUserName());
			//changes end for jira sprint 4 for jira Sprint 4- WI ALlocation Logic
			formObject.setNGValue("CPV_Analyst_dec", formObject.getNGValue("cmplx_Decision_Decision"));//Done by aman 17/12
			formObject.setNGValue("Mail_Priority", formObject.getUserName());
			formObject.setNGValue("CPV_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			PersonalLoanS.mLogger.info( "cmplx_Decision_Decision:" +  formObject.getNGValue("cmplx_Decision_Decision")+ "#ControlName#");


			//formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
			//Commented by shweta for seq 138
			/*if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				formObject.setNGValue("IS_Approve_Cif","Y");
			}*/
			if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				PersonalLoanS.mLogger.info("Inside  Approve CPV");
				formObject.setNGValue("q_hold1",1);
				formObject.setNGValue("VAR_INT4",1);
				PersonalLoanS.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
			}
			
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%CPV%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + squeryloan);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
				}
			if("FPU".equalsIgnoreCase(referto)){		
				formObject.setNGValue("RefFrmCPV", "Y");
			}
			
			//below code added by siva on 04112019 for PCAS-1268
			 if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_SetReminder")) && "CPV Hold".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			 {
					String Email_sub="REMINDER- case no "+formObject.getWFWorkitemName()+" pending for the action of "+formObject.getUserName();
					//CreditCard.mLogger.info("Email_sub: "+ Email_sub);
					String Email_message="<html><body>Dear User, \n Kindly call the HR/office back. \n \n Remarks: "+formObject.getNGValue("cmplx_OffVerification_reamrks")+". \n Decision Remarks � "+formObject.getNGValue("cmplx_Decision_REMARKS")+". </body></html>";
					//CreditCard.mLogger.info("Email_sub: "+ Email_message);
					String query = "select count(WI_Name) from NG_RLOS_EmailReminder with(nolock) where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CPV'";
					List<List<String>> Emailrecords = formObject.getDataFromDataSource(query);
					PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count is: "+query);
					PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count result: "+Emailrecords);
					try{
						String reminderInsertQuery="";
						if(Emailrecords!=null && Emailrecords.size()>0 && Emailrecords.get(0)!=null ){
							String EmailRow_count=Emailrecords.get(0).get(0);
							PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count EmailRow_count: "+EmailRow_count);
							if("0".equalsIgnoreCase(EmailRow_count)){
								reminderInsertQuery="insert into NG_RLOS_EmailReminder(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,SetReminder,WI_Name,Workstep_Name)"
										+" values('CPV','P','','test5@rakbanktst.ae','"+Email_sub+"','"+Email_message+"','"+formObject.getNGValue("cmplx_Decision_SetReminder")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
							}
							else{
								reminderInsertQuery="update NG_RLOS_EmailReminder set Email_Status='P' where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CPV'";
							}
							PersonalLoanS.mLogger.info("Query to insert NG_RLOS_EmailReminder: "+ reminderInsertQuery);
							formObject.saveDataIntoDataSource(reminderInsertQuery);
						}
					}
					catch(Exception e){
						PersonalLoanS.mLogger.info("qException occured in CPV reminder Email insert:"+query);
					}
				}
			 //code ended by siva on 04112019 for PCAS-1268

			
			

		}catch(Exception e){
			PersonalLoanS.mLogger.info("PL CPV submitFormStarted()-->Exception occurred");
			printException(e);
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
