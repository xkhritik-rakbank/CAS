
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Reject Queue

File Name                                                         : CC_Reject_Queue

Author                                                            : Disha

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.HashMap;


import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_Reject_Queue extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	  CC_Integration_Input Intgration_input = new CC_Integration_Input();
		CC_Integration_Output Intgration_Output = new CC_Integration_Output();
	public void formLoaded(FormEvent pEvent)
	{
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		makeSheetsInvisible(tabName, "6,7,8,9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
        	CreditCard.mLogger.info( "Inside Reject CC");
           new CC_CommonCode().setFormHeader(pEvent);
        }catch(Exception e)
        {
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

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
	
	
	public void initialize() {
		CreditCard.mLogger.info( "Inside PL PROCESS initialize()" );
		 

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		 
	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
	}
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			
		}*/
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				//formObject.setLocked("Product_Frame1",true);
				/*formObject.setEnabled("Customer_save",true);
				formObject.setLocked("cmplx_Customer_ReferrorCode",false);
				formObject.setLocked("cmplx_Customer_ReferrorName",false);
				formObject.setLocked("cmplx_Customer_AppType",false);
				formObject.setLocked("cmplx_Customer_corporateCode",false);
				formObject.setLocked("cmplx_Customer_Bankingwithus",false);
				formObject.setLocked("cmplx_Customer_noofDependent",false);
				formObject.setLocked("cmplx_Customer_guardian",false);
				formObject.setLocked("cmplx_Customer_minor",false);
				formObject.setEnabled("Customer_Reference_Add",true);
				formObject.setEnabled("Customer_Reference__modify",true);
				formObject.setEnabled("Customer_Reference_delete",true);*/
				loadPicklistCustomer();
				
			}	
			
			if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				formObject.setLocked("Product_Frame1",true);
				/*formObject.setEnabled("Product_Save",true);
				formObject.setEnabled("Product_Add",true);
				formObject.setEnabled("Product_Modify",true);
				formObject.setEnabled("Product_Delete",true);*/
			}
			
			if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
				loadpicklist_Income();
			}
			
			if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				/*formObject.setEnabled("Liability_New_AECBReport",true);
				formObject.setEnabled("Liability_New_Save",true);*/
			}
			
			if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("EMploymentDetails_Frame1",true);
				loadPicklistEmployment();
			}
			
			if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				/*formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);*/
			}
			
			if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Address();
				formObject.setLocked("AddressDetails_Frame1",true);
				//loadPicklist_Address();
				/*formObject.setEnabled("AddressDetails_Save",true);
				formObject.setEnabled("AddressDetails_addr_Add",true);
				formObject.setEnabled("AddressDetails_addr_Modify",true);
				formObject.setEnabled("AddressDetails_addr_Delete",true);*/
			}
			
			if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("AltContactDetails_Frame1",true);
				/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
			} 
			
			if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("FATCA_Frame1",true);
				/*formObject.setEnabled("FATCA_Save",true);*/
			}
			
			if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("KYC_Frame1",true);
				/*formObject.setEnabled("KYC_Save",true);*/
			}
			
			if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("OECD_Frame1",true);
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("IncomingDocument_Frame",true);
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Reference_Details_Frame1",true);
				
			}
			if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SupplementCardDetails_Frame1",true);
				
			}
			if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CardDetails_Frame1",true);
				
			}
			if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
	               formObject.setLocked("CompanyDetails_Frame1", true);
	               
					/*LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");*/
	               loadPicklist_CompanyDet();
	            }
			if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
            	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
	            // formObject.setLocked("AuthorisedSignDetails_Button4", true);
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
            }
			if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("PartnerDetails_Frame1", true);
                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            }
			if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				
			}
			
			//12th sept
			if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("NotepadDetails_Frame2",true);
				 notepad_load();
				 notepad_withoutTelLog();
				 
			}
			//12th sept
			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setVisible("DecisionHistory_CheckBox1",false);
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
                
                //12th sept
                formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
                formObject.setVisible("DecisionHistory_chqbook",false);
				formObject.setVisible("DecisionHistory_Text15",false);
				formObject.setVisible("DecisionHistory_Text16",false);
				formObject.setVisible("DecisionHistory_Text17",false);
				formObject.setVisible("DecisionHistory_Label9",false);
				formObject.setVisible("cmplx_DEC_DCR_Refno",false);
				formObject.setVisible("DecisionHistory_Label27",false);
				formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
				formObject.setVisible("cmplx_DEC_ChequebookRef",false);
				formObject.setVisible("DecisionHistory_Label4",false);
				formObject.setVisible("DecisionHistory_Label5",false);
                
                formObject.setVisible("DecisionHistory_Label10",true);//cif
                formObject.setVisible("cmplx_DEC_New_CIFID",true);//cif
                formObject.setVisible("DecisionHistory_Label7",true);//account
                formObject.setVisible("cmplx_DEC_NewAccNo",true);//account
                formObject.setVisible("DecisionHistory_Label6",true);//iban
                formObject.setVisible("cmplx_DEC_IBAN_No",true);//iban
                formObject.setVisible("DecisionHistory_Button2",true);//create cif
                formObject.setEnabled("DecisionHistory_Button2",false);
                formObject.setVisible("DecisionHistory_Decision_Label1",true);//contact pt veri
                formObject.setVisible("cmplx_DEC_VerificationRequired",true);//contact pt veri
                formObject.setVisible("DecisionHistory_Decision_Label3",true);//dec
                formObject.setVisible("cmplx_DEC_Decision",true);//dec
                formObject.setVisible("DecisionHistory_Rejreason",true);//used as remarks
                formObject.setVisible("cmplx_DEC_RejectReason",true);//rem
                formObject.setVisible("DecisionHistory_Button4",true);//generate button need to ask
                formObject.setEnabled("DecisionHistory_Button4",false);
                
                formObject.setTop("DecisionHistory_Label10",10);
                formObject.setTop("cmplx_DEC_New_CIFID",25);
                formObject.setTop("DecisionHistory_Label7",10);
                formObject.setTop("cmplx_DEC_NewAccNo",25);
                formObject.setTop("DecisionHistory_Label6",10);
                formObject.setTop("cmplx_DEC_IBAN_No",25);
                formObject.setTop("DecisionHistory_Button2",15);
                formObject.setTop("DecisionHistory_Decision_Label1",10);
                formObject.setTop("cmplx_DEC_VerificationRequired",25);
                formObject.setTop("DecisionHistory_Rejreason",104);
                formObject.setTop("cmplx_DEC_RejectReason",120);
                formObject.setLeft("DecisionHistory_Rejreason",560);
                formObject.setLeft("cmplx_DEC_RejectReason",560);
                formObject.setLeft("DecisionHistory_Label7",304);
                formObject.setLeft("cmplx_DEC_NewAccNo",304);
                formObject.setLeft("DecisionHistory_Label6",560);
                formObject.setLeft("cmplx_DEC_IBAN_No",560);
                formObject.setLeft("DecisionHistory_Button2",813);
                formObject.setLeft("DecisionHistory_Decision_Label1",1074);
                formObject.setLeft("cmplx_DEC_VerificationRequired",1074);
                formObject.setTop("DecisionHistory_Button4",104);
                formObject.setLeft("DecisionHistory_Button4",813);
                formObject.setLeft("DecisionHistory_Label10",23);
                formObject.setLeft("cmplx_DEC_New_CIFID",23);
                formObject.setTop("DecisionHistory_save",250);
    			formObject.setTop("DecisionHistory_Decision_ListView1",280);
    			formObject.setEnabled("cmplx_DEC_RejectReason",true);
    			formObject.setEnabled("cmplx_DEC_IBAN_No",false);
				formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
				if(!("DDVT_Maker".equalsIgnoreCase(formObject.getNGValue("PREVIOUS_WS"))||"CSM".equalsIgnoreCase(formObject.getNGValue("PREVIOUS_WS")))){
					 formObject.setEnabled("DecisionHistory_Button2",true);
			}
				
                //12th sept
                loadPicklist3();
		 } 	
	
	
	}
	
	

}

