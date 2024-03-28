
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : RM CSO Review

File Name                                                         : CC_RM_CSO_Review

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


public class CC_RM_CSO_Review extends CC_Common implements FormListener
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
		
		 
		makeSheetsInvisible(tabName, "6,7,8,9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
        
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
		 if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
   		  formObject.setLocked("Customer_Frame1",true);
				disableButtonsCC("Customer");
				loadPicklistCustomer();
				
			}	
			
			if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Product_Frame1",true);
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				disableButtonsCC("Product");
			}
			
			if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				disableButtonsCC("IncomeDetails");
				loadpicklist_Income();
			}
			
			if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				disableButtonsCC("Liability_New");
			}
			
			if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("EMploymentDetails_Frame1", true);	
				disableButtonsCC("EMploymentDetails");
				loadPicklistEmployment();
			}
			
			if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				disableButtonsCC("ELigibiltyAndProductInfo");
			}
			
			if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("AddressDetails_Frame1",true);
				loadPicklist_Address();
				disableButtonsCC("AddressDetails");
			}
			
			if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setLocked("AltContactDetails_Frame1",true);
				disableButtonsCC("AltContactDetails");
			} 
			
			if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setLocked("FATCA_Frame6",true);
				disableButtonsCC("FATCA");
			}
			
			if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("KYC_Frame1",true);
			}
			
			if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setLocked("OECD_Frame8",true);
				disableButtonsCC("OECD");
			}
			if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setLocked("IncomingDocument_Frame",true);
				disableButtonsCC("IncomingDocument");
			}
			if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Reference_Details_ReferenceRelationship",true);
				disableButtonsCC("Reference_Details");
				
			}
			if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SupplementCardDetails_Frame1",true);
				disableButtonsCC("SupplementCardDetails");
				
			}
			if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CardDetails_Frame1",true);
				disableButtonsCC("CardDetails");
				
			}
			if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CompanyDetails_Frame1",true);
	               disableButtonsCC("CompanyDetails");
	               
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
				formObject.setLocked("AuthorisedSignDetails_ShareHolding",true);
           	 disableButtonsCC("AuthorisedSignDetails");
           	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
               LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
           }
			//added by yash for CC FSD
			if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Compliance_Frame1",true);
				//disableButtonsCC("SupplementCardDetails");
				
			}
			//ended by yash for CC FSD
			if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("PartnerDetails_Frame1",true);
				 disableButtonsCC("PartnerDetails");
               LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
           }
			//added by yash on 30/8/2017
			if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("NotepadDetails_Frame2",true);
				formObject.setTop("NotepadDetails_savebutton",410);
				notepad_load();
				notepad_withoutTelLog();
			
			
		}
			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setVisible("DecisionHistory_CheckBox1",false);
				formObject.setVisible("DecisionHistory_Label1",false);
				formObject.setVisible("cmplx_DEC_VerificationRequired",false);
				formObject.setVisible("DecisionHistory_Label3",false);
				formObject.setVisible("DecisionHistory_Combo3",false);
				formObject.setVisible("DecisionHistory_Label6",false);
				formObject.setVisible("DecisionHistory_Combo6",false);
				formObject.setVisible("Decision_Label4",false);
				formObject.setVisible("cmplx_DEC_Remarks",false);
				formObject.setVisible("DecisionHistory_Label8",false);
				formObject.setVisible("DecisionHistory_Text4",false);
				formObject.setVisible("DecisionHistory_Label7",false);
				formObject.setVisible("DecisionHistory_Text3",false);
				formObject.setVisible("DecisionHistory_Label2",false);
				formObject.setVisible("DecisionHistory_Text2",false);
				formObject.setVisible("cmplx_DEC_Description",false);
				formObject.setVisible("cmplx_DEC_Strength",false);
				formObject.setVisible("cmplx_DEC_Weakness",false);
				formObject.setVisible("cmplx_DEC_ContactPointVeri",false);  
				//added byas yash for CC FSD
				formObject.setVisible("DecisionHistory_chqbook",false);
				formObject.setVisible("cmplx_DEC_NewAccNo",false);
				formObject.setVisible("cmplx_DEC_IBAN_No",false);
				formObject.setVisible("cmplx_DEC_ChequebookRef",false);
				formObject.setVisible("DecisionHistory_Label9",false);
				formObject.setVisible("cmplx_DEC_DCR_Refno",false);
				formObject.setVisible("DecisionHistory_Text15",false);
				formObject.setVisible("DecisionHistory_Text16",false);
				formObject.setVisible("DecisionHistory_Text17",false);
				formObject.setVisible("DecisionHistory_Label27",false);
				formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
				formObject.setVisible("DecisionHistory_Label5",false);
				formObject.setVisible("DecisionHistory_Decision_Label4",false);
				formObject.setVisible("DecisionHistory_Decision_Label1",false);
				formObject.setVisible("DecisionHistory_Label4",false);
				formObject.setTop("DecisionHistory_Decision_Label3",7);
				
				formObject.setTop("cmplx_DEC_Decision",23);
				formObject.setLeft("cmplx_DEC_Decision",23);
				formObject.setVisible("DecisionHistory_Label1",false);
				formObject.setVisible("DecisionHistory_Label11",true);
				formObject.setTop("DecisionHistory_Label11",7);
				formObject.setLeft("DecisionHistory_Label11",304);
				formObject.setVisible("cmplx_DEC_ReferReason",false);
				formObject.setVisible("cmplx_DEC_Decision_Reasoncode",true);
				formObject.setTop("cmplx_DEC_Decision_Reasoncode",23);
				formObject.setLeft("cmplx_DEC_Decision_Reasoncode",304);
				formObject.setVisible("DecisionHistory_Rejreason",true);
				formObject.setTop("DecisionHistory_Rejreason",7);
				formObject.setLeft("DecisionHistory_Rejreason",586);	
				formObject.setVisible("cmplx_DEC_RejectReason",true);
				formObject.setTop("cmplx_DEC_RejectReason",23);
				formObject.setLeft("cmplx_DEC_RejectReason",586);
				formObject.setLocked("cmplx_DEC_RejectReason",false);
				formObject.setVisible("DecisionHistory_Decision_ListView1",true);
				formObject.setTop("DecisionHistory_Decision_ListView1",65);
				formObject.setVisible("DecisionHistory_save",true);
				formObject.setTop("DecisionHistory_save",240);
				
				
				disableButtonsCC("DecisionHistory");
				
         	loadPicklist3();
		 } 	
	
		
		
	}
	
	


}

