
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Fraud Control Unit

File Name                                                         : CC_FCU

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1. 				   12-6-2017	 Disha 		   Changes done to hide OV tab
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;



import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_FCU extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	public void formLoaded(FormEvent pEvent)
	{
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		// Changes done to hide OV tab
		makeSheetsInvisible("Tab1", "7,8,11,12,13,14,15,16,17");
		CreditCard.mLogger.info( "Saurabh FCU");
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 CreditCard.mLogger.info("Inside CC_FCU CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            CreditCard.mLogger.info( "Exception:"+e.getMessage());
	        }
	        
	    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		 

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);
					
					break;
					
					case FRAGMENT_LOADED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						fragment_loaded(pEvent,formObject);
										
					  break;
							
					case MOUSE_CLICKED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().mouse_clicked(pEvent);
						break;
				
					 case VALUE_CHANGED:
							CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new CC_CommonCode().value_changed(pEvent);
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
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
				formObject.setLocked("Customer_Frame1",true);
				formObject.setLocked("Customer_FetchDetails",true);
				formObject.setLocked("Customer_save",true);
				
				//setDisabled();
			}	
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
				//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
				/*
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				*/
				formObject.setLocked("Product_Frame1",true);
				formObject.setLocked("Add",true);
				formObject.setLocked("Modify",true);
				formObject.setLocked("Delete",true);
				formObject.setLocked("Product_Save",true);
				formObject.setLocked("cmplx_Product_cmplx_ProductGrid_table",true);
				formObject.setLocked("Customer_save",true);
				formObject.setLocked("Customer_save",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("CompanyDetails_Frame1",true);
				formObject.setLocked("CompanyDetails_Add",true);
				formObject.setLocked("CompanyDetails_Modify",true);
				formObject.setLocked("CompanyDetails_delete",true);
				formObject.setLocked("CompanyDetails_Button1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_CompanyDet();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
	            
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				 }
			if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("PartnerDetails_Frame1",true);
				
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame1",true);
			
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Internal Liabilities")) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame2",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("External Liabilities")) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame3",true);
				
				
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Liability Addition")) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame4",true);		
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
				formObject.setLocked("EMploymentDetails_Frame1", true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist4();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
				
				
				//loadPicklist4();
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
				//loadPicklist_Address();
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("AddressDetails_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_Address();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
	
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("AltContactDetails_Frame1",true);
			
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {
				//loadPicklist_Address();
				formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			}
			//12th sept
			if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
				formObject.setLocked("Reference_Details_ReferenceRelationship",true);
			}
			//12th sept
			if (pEvent.getSource().getName().equalsIgnoreCase("CardCollection")) {
				//loadPicklist_Address();
				formObject.setLocked("CardDetails_Frame1",true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("SupplementCardDetails_Frame1",true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
				//loadPicklist_Address();
				formObject.setLocked("FATCA_Frame6",true);
				
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_Fatca();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("CardDetails_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
				//loadPicklist_Address();
				formObject.setLocked("KYC_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
				//loadPicklist_Address();
				formObject.setLocked("OECD_Frame8",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_oecd();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Details")) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
				//loadPicklist_Address();
				formObject.setLocked("PartMatch_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMIncident_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
				//loadPicklist_Address();
				formObject.setLocked("ExternalBlackList_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCore_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
				//loadPicklist_Address();
				formObject.setLocked("MOL1_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
				//loadPicklist_Address();
				formObject.setLocked("WorldCheck1_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_WorldCheck();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
				//loadPicklist_Address();
				formObject.setLocked("RejectEnq_Frame1",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
				//loadPicklist_Address();
				formObject.setLocked("SalaryEnq_Frame1",true);
				
			}
			/*if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
				//loadPicklist_Address();
				formObject.setLocked("ExternalBlackList_Frame1",true);
				
			}*/
			if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
				//loadPicklist_Address();
				formObject.setLocked("IncomingDocument_Frame",true);
				
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
				//loadPicklist_Address();
				formObject.setLocked("PostDisbursal_Frame2",true);
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_ServiceRequest();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Fraud Control Unit")) {
			LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
			}
			
			
			
			if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) {
				//formObject.setLocked("SmartCheck_Frame1",true);
				//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
				 formObject.setVisible("SmartCheck1_Label2",true);
				 formObject.setVisible("SmartCheck1_CPVRemarks",true);
				 formObject.setVisible("SmartCheck1_Label1",true);
				 formObject.setVisible("SmartCheck1_CreditRemarks",true);
				
				 formObject.setLocked("SmartCheck1_Modify",true);
				 formObject.setHeight("SmartCheck1_Label4", 16);
				 formObject.setHeight("SmartCheck1_FCURemarks", 55);
				//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
			}
			//added by yash on 30/8/2017
			if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
				//12th sept
				 notepad_load();
				 notepad_withoutTelLog();
				 //12th sept
				 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 CreditCard.mLogger.info( "Activity name is:" + sActivityName);
		        formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		    	
				formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setVisible("NotepadDetails_Frame3",true);
				formObject.setVisible("NotepadDetails_save",false);
				formObject.setHeight("NotepadDetails_Frame3",260);
				
				//formObject.setLocked("NotepadDetails_Frame1",true);
				
			}
				if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
					//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
					formObject.setVisible("DecisionHistory_CheckBox1",false);
	                formObject.setVisible("DecisionHistory_Label1",false);
	                formObject.setVisible("cmplx_DEC_VerificationRequired",false);
	                formObject.setVisible("DecisionHistory_Label3",false);
	                formObject.setVisible("DecisionHistory_Combo3",false);
	                formObject.setVisible("DecisionHistory_Label6",false);
	                formObject.setVisible("DecisionHistory_Combo6",false);
	                formObject.setVisible("DecisionHistory_Decision_Label4",false);
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
	                formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
	        		formObject.setVisible("cmplx_DEC_IBAN_No",false);
					formObject.setVisible("DecisionHistory_Label5",false);
					formObject.setVisible("cmplx_DEC_NewAccNo",false);
					formObject.setVisible("DecisionHistory_chqbook",false);
					formObject.setVisible("DecisionHistory_Text15",false);
					formObject.setVisible("DecisionHistory_Text16",false);
					formObject.setVisible("DecisionHistory_Text17",false);
					formObject.setVisible("cmplx_DEC_ChequebookRef",false);
					formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
					formObject.setVisible("DecisionHistory_Label27",false);
					formObject.setVisible("DecisionHistory_Label9",false);
					formObject.setVisible("cmplx_DEC_DCR_Refno",false);
					formObject.setVisible("DecisionHistory_Decision_Label1",false);
					formObject.setVisible("DecisionHistory_Label4",false);
					
	                CreditCard.mLogger.info( "Decion history FCU code here");
	                formObject.setVisible("DecisionHistory_Label19",true);//aloc company
	                formObject.setVisible("cmplx_DEC_AlocCompany",true);//aloccompany
	        		formObject.setVisible("DecisionHistory_Label20",true);//aloc type
					formObject.setVisible("cmplx_DEC_AlocType",true); //aloc type
					formObject.setVisible("DecisionHistory_Label21",true); //ref rsn
					formObject.setVisible("cmplx_DEC_ReferralReason",true); //ref rsn
					formObject.setVisible("DecisionHistory_Label22",true);//ref sub rsn
					formObject.setVisible("cmplx_DEC_ReferralSubReason",true);//ref sub rsn
					formObject.setVisible("DecisionHistory_Label23",true);//feedbk status
					formObject.setVisible("cmplx_DEC_FeedbackStatus",true);//feedbk status
					formObject.setVisible("DecisionHistory_Label24",true);//ref sub feedbk
					formObject.setVisible("cmplx_DEC_SubFeedbackStatus",true);//ref subfeedbk
					formObject.setVisible("DecisionHistory_Label25",true);//further review
					formObject.setVisible("cmplx_DEC_FurtherReview",true);//further review
					formObject.setVisible("DecisionHistory_Decision_ListView1",true);//listview
					formObject.setVisible("DecisionHistory_save",true);//save
					formObject.setVisible("DecisionHistory_Decision_Label3",true);//decision
					formObject.setVisible("cmplx_DEC_Decision",true);//dec
					formObject.setVisible("DecisionHistory_Rejreason",true);//remarks
					formObject.setVisible("cmplx_DEC_RejectReason",false);//remarks
					formObject.setEnabled("cmplx_DEC_RejectReason",false);
					formObject.setVisible("cmplx_DEC_Remarks",true);
					LoadPickList("cmplx_DEC_ReferralReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
	    			LoadPickList("cmplx_DEC_ReferralSubReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");
	    			formObject.setNGValue("DecisionHistory_Decision_Label3","Decision");
	            	formObject.setNGValue("DecisionHistory_Decision_Label4","Summary");
					//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
					         	
                    //12th sept
                    loadPicklist3();
			 }//loadPicklist1();
		  	
	//12th sept
				if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1")){
					 if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed")){
						 CreditCard.mLogger.info( "Inside add button: business verification1");
						 formObject.setLocked("BussinessVerification1_Frame1",false);
					 }
					 else{
						 CreditCard.mLogger.info( "Inside add button: business verification2");
						 formObject.setLocked("BussinessVerification1_Frame1",true);
					 }
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification")){
					 if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
						 CreditCard.mLogger.info( "Inside add button: employment verification");
						 formObject.setLocked("EmploymentVerification_Frame1",false);
					 }
					 else{
						 CreditCard.mLogger.info( "Inside add button: business verification2");
						 formObject.setLocked("EmploymentVerification_Frame1",true);
					 }
					//++Below code added by  nikhil 13/10/17 as per CC FSD 2.2
					 LoadPickList("cmplx_EmploymentVerification_OffTelnoValidatedfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
					 
					//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
					 
				}
	//12th sept
	}
	
	

}

