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
	
	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	 	
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			
		}*/
			 if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
				//setDisabled();
				formObject.setLocked("Customer_Frame1",true);
			}	
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
				formObject.setLocked("Product_Frame1",true);
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
				
	 			formObject.setLocked("GuarantorDetails_Frame1",true);
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
				
	 			formObject.setLocked("IncomeDetails_Frame1",true);
			} 
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
				
	 			formObject.setLocked("ExtLiability_Frame1",true);
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
				
	 			formObject.setLocked("EMploymentDetails_Frame1",true);
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
				
	 			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
				
	 			formObject.setLocked("LoanDetails_Frame1",true);
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("RiskRating")) {
				
	 			formObject.setLocked("RiskRating_Frame1",true);
			}//Arun (15/09/2017)
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
				loadPicklist_Address();
				formObject.setLocked("AddressDetails_Frame1",true);
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
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1")) {
				
				custdet1values();
	 			formObject.setLocked("cmplx_CustDetailverification1_MobNo1_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_MobNo2_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_DOB_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_PO_Box_Value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_Emirates_value",true);
	 			formObject.setLocked("cmplx_CustDetailverification1_Off_Telephone_value",true);
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification")) {
				
				EmpVervalues();
	 			formObject.setLocked("cmplx_EmploymentVerification_fixedsal_val",true);
	 			formObject.setLocked("cmplx_EmploymentVerification_AccomProvided_val",true);
	 			formObject.setLocked("cmplx_EmploymentVerification_Desig_val",true);
	 			formObject.setLocked("cmplx_EmploymentVerification_doj_val",true);
	 			formObject.setLocked("cmplx_EmploymentVerification_confirmedinJob_val",true);
	 			formObject.setLocked("cmplx_EmploymentVerification_OffTelNo",true);//Arun (16/09/17)
	 			LoadPickList("cmplx_EmploymentVerification_salaryTransferBank", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");//Arun (16/09/17)
	 		
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1")) {
				
	 			formObject.setLocked("cmplx_BussVerification1_TradeLicenseName",true);
	 			formObject.setLocked("cmplx_BussVerification1_Signatory_Name",true);
	 			formObject.setLocked("cmplx_BussVerification1_ContactTelephone",true);
	 			formObject.setNGValue("cmplx_BussVerification1_ContactTelephone",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
	 			LoadPickList("cmplx_BussVerification1_CompanyEmirate", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_master_emirate with (nolock)");
	 		
			}//Arun (16/09/17)
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) 
			{
				//formObject.setVisible("SmartCheck1_Label1",false);//Arun (07/09/17)
				//formObject.setVisible("SmartCheck1_credrem",false);//Arun (07/09/17)
				formObject.setVisible("SmartCheck1_Label2",false);
				formObject.setVisible("SmartCheck1_CPVrem",false);
				formObject.setLocked("SmartCheck1_Modify",true);
			}
			// disha FSD
			else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
				
	 			formObject.setLocked("NotepadDetails_Frame1",true);
	 			formObject.setVisible("NotepadDetails_Frame3",false);
				String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				mLogger.info("PL notepad "+ "Activity name is:" + sActivityName);
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
				
				formObject.setHeight("NotepadDetails_Frame1",450);
				LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
            	loadPicklist1();
            	LoadpicklistFCU();
            	formObject.setVisible("cmplx_Decision_waiveoffver", false);
            	formObject.setVisible("Decision_Label1", false);
            	formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED", false);
            	formObject.setVisible("DecisionHistory_chqbook", false);
            	formObject.setVisible("DecisionHistory_Label1", false);
            	formObject.setVisible("cmplx_Decision_refereason", false);
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
            	formObject.setVisible("DecisionHistory_Button5",false);//Arun
            	formObject.setVisible("DecisionHistory_Button6",false);//Arun
            	
            	formObject.setVisible("DecisionHistory_Label19",true);
            	formObject.setVisible("cmplx_Decision_ALOCcompany",true);
            	formObject.setVisible("DecisionHistory_Label20",true);
            	formObject.setVisible("cmplx_Decision_ALOCType",true);
            	formObject.setVisible("DecisionHistory_Label21",true);
            	formObject.setVisible("cmplx_Decision_Referralreason",true);
            	formObject.setVisible("DecisionHistory_Label22",true);
            	formObject.setVisible("cmplx_Decision_Referralsubreason",true);
            	formObject.setVisible("DecisionHistory_Label23",true);
            	formObject.setVisible("cmplx_Decision_feedbackstatus",true);
            	formObject.setVisible("DecisionHistory_Label24",true);
            	formObject.setVisible("cmplx_Decision_subfeedback",true);
            	formObject.setVisible("DecisionHistory_Label25",true);
            	formObject.setVisible("cmplx_Decision_Furtherreview",true);	
            	
            	formObject.setNGValue("Decision_Label3","Final Decision");
            	formObject.setNGValue("Decision_Label4","Summary");
            	
            	LoadPickList("cmplx_Decision_Referralreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
    			LoadPickList("cmplx_Decision_Referralsubreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");
    			
    			formObject.setHeight("DecisionHistory_Frame1",446);
    			
    			formObject.setTop("DecisionHistory_Label19",10);
    			formObject.setTop("cmplx_Decision_ALOCcompany",25);
    			formObject.setTop("DecisionHistory_Label20",10);
    			formObject.setTop("cmplx_Decision_ALOCType",25);
    			formObject.setTop("DecisionHistory_Label21",10);
    			formObject.setTop("cmplx_Decision_Referralreason",50);
    			formObject.setTop("DecisionHistory_Label22",10);
    			formObject.setTop("cmplx_Decision_Referralsubreason",25);
    			formObject.setTop("DecisionHistory_Label23",10);
    			formObject.setTop("cmplx_Decision_feedbackstatus",25);			        			
    			formObject.setTop("DecisionHistory_Label24",60);
    			formObject.setTop("cmplx_Decision_subfeedback",76);
    			formObject.setTop("DecisionHistory_Label25",60);
    			formObject.setTop("cmplx_Decision_Furtherreview",76);
    			formObject.setLeft("DecisionHistory_Label25",528);
    			formObject.setLeft("cmplx_Decision_Furtherreview",528);
    			formObject.setLeft("Decision_Label3",272);
    			formObject.setLeft("cmplx_Decision_Decision",272);
    			formObject.setLeft("Decision_Label4",850);
    			formObject.setLeft("cmplx_Decision_REMARKS",850);
    			formObject.setTop("Decision_Label3",60);
    			formObject.setTop("cmplx_Decision_Decision",76);
    			formObject.setTop("Decision_Label4",60);
    			formObject.setTop("cmplx_Decision_REMARKS",76);
    			
    			formObject.setTop("DecisionHistory_save",120);
    			formObject.setTop("cmplx_Decision_cmplx_gr_decision",168);
            	//formObject.setVisible("DecisionHistory_Modify", true);
            	//formObject.setVisible("cmplx_Decision_Status", true);
            	//formObject.setVisible("DecisionHistory_Label2", true);
            	
            	//Common function for decision fragment textboxes and combo visibility
            	//decisionLabelsVisibility();
		 } 	//Arun (07/09/17)
	
	
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

