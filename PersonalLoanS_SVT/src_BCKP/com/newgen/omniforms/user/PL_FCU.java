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
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
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
		mLogger.info("PL_FCU Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());
		
		BussVerVisible();
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
            new PersonalLoanSCommonCode().setFormHeader(pEvent);
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
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
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
	 			formObject.setVisible("EMploymentDetails_Label36",false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
	 			loadPicklist4();//Arun (04-12-17) to load the masters in Emoloyment details
			}
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
				loadEligibilityData();
			}
			
			else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_LoanDetails();
				formObject.setLocked("cmplx_LoanDetails_fdisbdate",true);
				formObject.setLocked("cmplx_LoanDetails_frepdate",true);
				formObject.setLocked("cmplx_LoanDetails_maturitydate",true);
				formObject.setLocked("LoanDetails_disbdate",true);
				formObject.setLocked("LoanDetails_payreldate",true);
				formObject.setLocked("cmplx_LoanDetails_paidon",true);
				formObject.setLocked("cmplx_LoanDetails_trandate",true);
				
	 			formObject.setLocked("LoanDetails_Frame1",true);
			}
			
			else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("RiskRating_Frame1",true);
			}//Arun (15/09/2017)
			
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Address();
				formObject.setLocked("AddressDetails_Frame1",true);
			}
			
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("AltContactDetails_Frame1",true);
	 			LoadpicklistAltcontactDetails();
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
			}
			
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
				Loadpicklistfatca();
	 			formObject.setLocked("FATCA_Frame6",true);
			}
			
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("KYC_Frame1",true);
			}
			
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPickListOECD();
	 			formObject.setLocked("OECD_Frame8",true);
			}
			
			else if ("CustDetailVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//Below code added by nikhil 13/11/2017 for Code merge
								//Below code added by  nikhil 31/10/17 as per CC FSD 2.7
								new PersonalLoanSCommonCode().custdet1values();
								//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
	 			formObject.setLocked("cmplx_CustDetailverification1_MobNo1_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_MobNo2_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_DOB_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_PO_Box_Value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_Emirates_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_Off_Telephone_value",true);
	 			//--Above code added by nikhil 13/11/2017 for Code merge
			}
			
			else if ("EmploymentVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
				//Below code added by nikhil 13/11/2017 for Code merge
								//Below code added by  nikhil 31/10/17 as per CC FSD 2.7
								new PersonalLoanSCommonCode().autopopulateValuesEmployeesVeri();
								//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
					 			//below code commented by nikhil 31/10/17
				//below code added by nikhil 4/12/17 to load office no. validated from field
				LoadPickList("cmplx_EmploymentVerification_OffTelnoValidatedfrom","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_offNoValidatedFrom with (nolock) where IsActive = 'Y' order by code");
												
	 			LoadPickList("cmplx_EmploymentVerification_salaryTransferBank", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");//Arun (16/09/17)
	 			//--Above code added by nikhil 13/11/2017 for Code merge
	 			//below code added by nikhil 4/12/17 for employment verification
	 			//formObject.setTop("Bank_Check", formObject.getTop("EmploymentVerification_Frame1"+formObject.getHeight("EmploymentVerification_Frame1")+20));
	 			//formObject.setTop("Smart_chk", formObject.getTop("EmploymentVerification_Frame1"+formObject.getHeight("EmploymentVerification_Frame1")+60));
			}
			
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
			//--Above code added by nikhil 13/11/2017 for Code merge
			else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				
								//Below code added by  nikhil 31/10/17 as per CC FSD 2.7
								formObject.setEnabled("SmartCheck1_credrem", false);
								formObject.setEnabled("SmartCheck1_CPVrem", false);
								formObject.setVisible("SmartCheck1_Label2",true);
								formObject.setVisible("SmartCheck1_CPVrem",true);
								formObject.setLocked("SmartCheck1_Modify",false);
								//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
							}
			
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//Below code added by nikhil 13/11/2017 for Code merge
				
				notepad_load();
				formObject.setVisible("NotepadDetails_Frame3",true);
				//--Above code added by nikhil 13/11/2017 for Code merge
			
				}
			 else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
				 fetchIncomingDocRepeater();
			 }
			
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				//for decision fragment made changes 8th dec 2017
				PersonalLoanS.mLogger.info("***********Inside decision history of csm");
				fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Label19,cmplx_Decision_ALOCcompany#DecisionHistory_Label20,cmplx_Decision_ALOCtype#\n#DecisionHistory_Label21,cmplx_Decision_Referralreason#DecisionHistory_Label22,cmplx_Decision_Referralsubreason#DecisionHistory_Label23,cmplx_Decision_Feedbackstatus#DecisionHistory_Label24,cmplx_Decision_subfeedback#DecisionHistory_Label25,cmplx_Decision_Furtherreview#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
				formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				LoadPickList("cmplx_Decision_Referralreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
     			LoadPickList("cmplx_Decision_Referralsubreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");
     			
            	loadPicklist1();
			// below code already exist - 10-10-2017
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			//Below code added by nikhil 13/11/2017 for Code merge
			
			formObject.setNGValue("Decision_Label4", "Summary");
			formObject.setTop("ReferHistory",680);
			/*formObject.setTop("cmplx_Decision_Referralreason", 25);
			formObject.setTop("DecisionHistory_Label21", formObject.getTop("DecisionHistory_Label20"));
			
		formObject.setLeft("DecisionHistory_DecisionReasonCode", formObject.getLeft("cmplx_Decision_Referralsubreason")+225);
		formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("cmplx_Decision_Referralsubreason")+225);*/
	
			//--Above code added by nikhil 13/11/2017 for Code merge
			
			//for decision fragment made changes 8th dec 2017
        
		 } 
	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		
		mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	

				switch(pEvent.getType())
				{	

				case FRAME_EXPANDED:
					mLogger.info(" In PL_DDVT Checker eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// empty method
		
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		// empty method
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("Inside PL FCU submitFormCompleted()" + pEvent.getSource()); 
		List<String> objInput=new ArrayList<String> ();
		//disha FSD cad delegation procedure changes
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:FCU");
		PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
		formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput);
		
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		 saveIndecisionGrid();
	}

}

