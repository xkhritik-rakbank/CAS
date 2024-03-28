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

import java.util.HashMap;
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
			} 
			
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("ExtLiability_Frame1",true);
			}
			
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("EMploymentDetails_Frame1",true);
	 			loadPicklist4();//Arun (04-12-17) to load the masters in Emoloyment details
			}
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				
	 			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			}
			
			else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				
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
			
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
            	loadPicklist1();
			// below code already exist - 10-10-2017
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			//Below code added by nikhil 13/11/2017 for Code merge
			
			formObject.setNGValue("Decision_Label4", "Summary");
			formObject.setTop("cmplx_Decision_Referralreason", 25);
			formObject.setTop("DecisionHistory_Label21", formObject.getTop("DecisionHistory_Label20"));
			
		formObject.setLeft("DecisionHistory_DecisionReasonCode", formObject.getLeft("cmplx_Decision_Referralsubreason")+225);
		formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("cmplx_Decision_Referralsubreason")+225);
			//--Above code added by nikhil 13/11/2017 for Code merge
        
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


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// empty method
		
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		 saveIndecisionGrid();
	}

}

