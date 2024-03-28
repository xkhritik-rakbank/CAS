/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_FCU.java
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

public class PL_FCU extends PLCommon implements FormListener
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
        //makeSheetsInvisible("Tab1", "2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17");
		makeSheetsInvisible("Tab1", "8,11,12,13,14,15,16");

		//BussVerVisible();
		
	}
	
//EmploymentVerification_s2_Button1
	public void formPopulated(FormEvent pEvent) 
	{	//Changes done by shweta to show mandatory alerts before taking decision on FPU step
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		FormConfig formobject1=FormContext.getCurrentInstance().getFormConfig();

        try{
            new PersonalLoanSCommonCode().setFormHeader(pEvent);
            if(!"R".equalsIgnoreCase(formobject1.getConfigElement("Mode"))) 
			{
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("FCU_SAL_Frame_Name"));
			//formObject.setNGValue("readonlycheck","ReadonlyFPU");
			}
            else if("R".equalsIgnoreCase(formobject1.getConfigElement("Mode")))
            	formObject.setNGValue("readonlycheck", "R");
			mLogger.info("Mandatory_Frames of FPU --"+formObject.getNGValue("Mandatory_Frames") );
        }catch(Exception e)
        {
            mLogger.info("PL_FCU-->"+ "Exception:"+e.getMessage());
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
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	 	
		
			 if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadView(pEvent.getSource().getName());
				formObject.setLocked("Customer_Frame1",true);
				formObject.setLocked("cmplx_Customer_NEP", true);
				formObject.setLocked("cmplx_Customer_DOb", true);
				formObject.setLocked("cmplx_Customer_IdIssueDate", true);
				formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
				formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
				formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
				formObject.setLocked("cmplx_Customer_VIsaExpiry", true);
				formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
				loadPicklistCustomer();
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
				LoadView(pEvent.getSource().getName());

	 			formObject.setLocked("EMploymentDetails_Frame1",true);
	 			/*formObject.setVisible("EMploymentDetails_Label33",false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);*/
				formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
	 			loadPicklist4();//Arun (04-12-17) to load the masters in Emoloyment details
			}
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklistELigibiltyAndProductInfo();

	 			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
	 			formObject.setEnabled("ELigibiltyAndProductInfo_Refresh",true); //added by Imran
				loadEligibilityData();
				
			}
			
			else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_LoanDetails();
	
				
	 			formObject.setLocked("LoanDetails_Frame1",true);
	 			
	 			formObject.setLocked("cmplx_LoanDetails_fdisbdate",true);
				formObject.setLocked("cmplx_LoanDetails_frepdate",true);
				formObject.setLocked("cmplx_LoanDetails_maturitydate",true);
				formObject.setEnabled("LoanDetails_duedate", false);
				formObject.setEnabled("LoanDetails_disbdate", false);
				formObject.setEnabled("LoanDetails_payreldate", false);
				formObject.setEnabled("cmplx_LoanDetails_paidon", false);
				formObject.setEnabled("cmplx_LoanDetails_trandate", false);
				formObject.setEnabled("cmplx_LoanDetails_chqdat", false);
			}
			
			else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("RiskRating_Frame1",true);
				loadPicklistRiskRating(); //jahnavi
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
				LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
				
			}
			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("PartMatch_Frame1",true);
				formObject.setLocked("PartMatch_Dob",true);
				//LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			}

			else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			}
			else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("MOL1_Frame1",true);
				formObject.setLocked("cmplx_MOL_molexp", true);
				formObject.setLocked("cmplx_MOL_molissue", true);
				formObject.setLocked("cmplx_MOL_ctrctstart", true);
				formObject.setLocked("cmplx_MOL_ctrctend", true);
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
			 
			 //code changes by bandana start
			else if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);
				
			}
			 //code changes by bandana ends
			
			else if ("EmploymentVerification_s2".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				List<String> LoadPicklist_Verification= Arrays.asList("cmplx_EmploymentVerification_fixedsal_ver","cmplx_EmploymentVerification_AccomProvided_ver","cmplx_EmploymentVerification_Desig_ver","cmplx_EmploymentVerification_doj_ver","cmplx_EmploymentVerification_confirmedinJob_ver","cmplx_EmploymentVerification_LoanFromCompany_ver","cmplx_EmploymentVerification_PermanentDeductSal_ver","cmplx_EmploymentVerification_SmartCheck_ver","cmplx_EmploymentVerification_FiledVisitedInitiated_ver");
				LoadPicklistVerification(LoadPicklist_Verification);			
				formObject.setLocked("cmplx_emp_ver_sp2_FPU_remarks_comp_mismatch",false);
				formObject.setEnabled("cmplx_emp_ver_sp2_FPU_remarks_comp_mismatch", true);
				formObject.setLocked("EmploymentVerification_s2_comp_save",false);
				formObject.setEnabled("EmploymentVerification_s2_comp_save",true);
			    formObject.setLocked("EmploymentVerification_s2_comp_emp_name",false);
			    formObject.setEnabled("EmploymentVerification_s2_comp_emp_name",true);
			    formObject.setLocked("EmploymentVerification_s2_comp_tl_no",false);
			    formObject.setEnabled("EmploymentVerification_s2_comp_tl_no",true);
			    formObject.setLocked("EmploymetlntVerification_s2_comp_emirate",false);
			    formObject.setEnabled("EmploymetlntVerification_s2_comp_emirate",true);
			    formObject.setLocked("EmploymentVerification_s2_search_TL_number",false);
			    formObject.setEnabled("EmploymentVerification_s2_search_TL_number",true);
			    formObject.setLocked("EmploymentVerification_s2_download_comp_report",false);
			    formObject.setEnabled("EmploymentVerification_s2_download_comp_report",true);
			   
			    
			}
			//EmploymentVerification_s2_MOL_salary
			else if ("BussinessVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("cmplx_BussVerification1_TradeLicenseName",true);
	 			formObject.setLocked("cmplx_BussVerification1_Signatory_Name",true);
	 			formObject.setLocked("cmplx_BussVerification1_ContactTelephone",true);
	 			formObject.setNGValue("cmplx_BussVerification1_ContactTelephone",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
	 			LoadPickList("cmplx_BussVerification1_CompanyEmirate", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_master_emirate with (nolock)");
	 		
			}//Arun (16/09/17)
			//Below code added by nikhil 13/11/2017 for Code merge
			
			else if ("BankingCheck".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setVisible("cmplx_BankingCheck_Decision", false);
				formObject.setVisible("BankingCheck_Label28", false);
			}
		//added by rishabh
			else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setLocked("SmartCheck_Frame1",true);
				//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
				 formObject.setVisible("SmartCheck1_Label2",true);
				 formObject.setVisible("SmartCheck1_CPVRemarks",true);
				 formObject.setVisible("SmartCheck1_Label1",true);
				 formObject.setVisible("SmartCheck1_CreditRemarks",true);
				 formObject.setLocked("SmartCheck1_Add",false);
				 formObject.setLocked("SmartCheck1_Modify",false);
				 formObject.setHeight("SmartCheck1_Label4", 16);
				 formObject.setHeight("SmartCheck1_FCURemarks", 55);
				 formObject.setLocked("SmartCheck1_CPVRemarks",true);
				 formObject.setLocked("SmartCheck1_CreditRemarks",true);
				//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
			}
//commented by rishabh
			//--Above code added by nikhil 13/11/2017 for Code merge
/*			else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				
								//Below code added by  nikhil 31/10/17 as per CC FSD 2.7
								formObject.setEnabled("SmartCheck1_credrem", false);
								formObject.setEnabled("SmartCheck1_CPVrem", false);
								formObject.setVisible("SmartCheck1_Label2",true);
								formObject.setVisible("SmartCheck1_CPVrem",true);
								formObject.setLocked("SmartCheck1_Modify",false);
								//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
							}
*/			
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//Below code added by nikhil 13/11/2017 for Code merge
				
				notepad_load();
				formObject.setVisible("NotepadDetails_Frame3",true);
				//--Above code added by nikhil 13/11/2017 for Code merge
			
				}
			 /*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
				 fetchIncomingDocRepeater();
			 }*/
				else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
					// added by abhishek as per CC FSD
					formObject.setLocked("WorldCheck1_Frame1",true);
					formObject.setLocked("WorldCheck1_Dob",true);
					formObject.setLocked("WorldCheck1_entdate",true);
					formObject.setLocked("WorldCheck1_upddate",true);
					//formObject.setLocked("WorldCheck1_Frame1",false);
					}
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				//for decision fragment made changes 8th dec 2017
				PersonalLoanS.mLogger.info("***********Inside decision history of csm");

				fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Label19,cmplx_Decision_ALOCcompany#DecisionHistory_Label20,cmplx_Decision_ALOCtype#\n#DecisionHistory_Label21,cmplx_Decision_Referralreason#DecisionHistory_Label22,cmplx_Decision_Referralsubreason#DecisionHistory_Label23,cmplx_Decision_Feedbackstatus#DecisionHistory_Label24,cmplx_Decision_subfeedback#DecisionHistory_Label25,cmplx_Decision_Furtherreview#\n#DecisionHistory_EFMS_Status#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

				formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
            	loadPicklist1();
				formObject.setNGValue("Decision_Label4", "Summary");

				LoadPickList("cmplx_Decision_Referralreason", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
     			LoadPickList("cmplx_Decision_Referralsubreason", "select '--Select--' as description,'' as code select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");
     			
			// below code already exist - 10-10-2017
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			//Below code added by nikhil 13/11/2017 for Code merge
			
			//formObject.setNGValue("Decision_Label4", "Summary");
			formObject.setTop("ReferHistory",680);
			
			String cc_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");
			String pl_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");
			if("true".equalsIgnoreCase(cc_aloc) && "true".equalsIgnoreCase(pl_aloc))
			{
				//formObject.setNGValue("cmplx_Decision_ALOCcompany", "Yes");
				formObject.setNGValue("cmplx_Decision_ALOCtype", "Both");
			}
			else if("true".equalsIgnoreCase(cc_aloc) && "false".equalsIgnoreCase(pl_aloc))
			{
				//formObject.setNGValue("cmplx_Decision_ALOCcompany", "Yes");
				formObject.setNGValue("cmplx_Decision_ALOCtype", "CC Aloc");
			}
			else if("false".equalsIgnoreCase(cc_aloc) && "true".equalsIgnoreCase(pl_aloc))
			{
				//formObject.setNGValue("cmplx_Decision_ALOCcompany", "Yes");
				formObject.setNGValue("cmplx_Decision_ALOCtype", "PL Aloc");
			}
			else
			{
				//formObject.setNGValue("cmplx_Decision_ALOCcompany", "No");
			}
			
			formObject.setLocked("cmplx_Decision_ALOCcompany",true);
			formObject.setLocked("cmplx_Decision_ALOCtype",true);
			if("Fraud".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_subfeedback")))
			{
				formObject.setLocked("cmplx_Decision_AlocCompany",false);
			}
			
        
		 } 
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_ServiceRequest();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())){
				 String gridName="cmplx_ReferHistory_cmplx_GR_ReferHistory";
				for(int i=0;i<formObject.getLVWRowCount(gridName);i++){						
					if((formObject.getNGValue(gridName,i,5).equalsIgnoreCase("FCU")||formObject.getNGValue(gridName,i,5).equalsIgnoreCase("FPU")) && formObject.getNGValue(gridName,i,6).equalsIgnoreCase("Complete")){
						formObject.setLocked("Generate_FPU_Report", false);
						PersonalLoanS.mLogger.info("Unlocking fpu");
					}
					else if((formObject.getNGValue(gridName,i,5).equalsIgnoreCase("FCU")||formObject.getNGValue(gridName,i,5).equalsIgnoreCase("FPU")) && formObject.getNGValue(gridName,i,6)!=("Complete")){
						formObject.setLocked("Generate_FPU_Report", true);
						PersonalLoanS.mLogger.info("locking fpu");
					}
				}
			}
			else if ("Fpu_Grid".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("cmplx_FPU_Grid_Officer_Name", false);
				LoadPickList("cmplx_FPU_Grid_Officer_Name", "select ' --Select-- 'as UserName union select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
			}
			
			 
	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		
		//mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	

				switch(pEvent.getType())
				{	

				case FRAME_EXPANDED:
					//mLogger.info(" In PL_DDVT Checker eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		// empty method
		
	}


	public void initialize() {
		// empty method
		
	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// empty method
		try{//code sync with CC
			CustomSaveForm();
			
		}catch(Throwable th){
			PersonalLoanS.mLogger.info("getttttttt"+th.getMessage());
		}
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// empty method
		
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		// empty method
		try{//code sync with CC
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("Inside PL FCU submitFormCompleted()" + pEvent.getSource()); 
			formObject.setNGValue("FCU_DeC", formObject.getNGValue("cmplx_Decision_Decision"));
			saveIndecisionGrid();
			List<String> objInput=new ArrayList<String> ();
			//disha FSD cad delegation procedure changes
			//change by aurabh for JIRA - 12666
			formObject.setNGValue("IsFCUChild_Exists","N");
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:FPU");
			
			PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
			List<Object> objOutput=new ArrayList<Object>();
			
			objOutput.add("Text");
			PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
			objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput,objOutput);
		} catch(Exception e) {
			PersonalLoanS.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		} catch(Throwable th) {
			PersonalLoanS.mLogger.info("getttttttt"+th.getMessage());
		}
		
	}



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{	
			CustomSaveForm();
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
			formObject.setNGValue("FCU_DeC", formObject.getNGValue("cmplx_Decision_Decision"));
			formObject.setNGValue("IsFCUChild_Exists", "N");
		//	CustomSaveForm();//code sync with CC
			LoadReferGrid();		
		}
		
		catch(Exception e)
		{
			PersonalLoanS.mLogger.info("Exception occured in submit form started method" +e.getMessage());
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

