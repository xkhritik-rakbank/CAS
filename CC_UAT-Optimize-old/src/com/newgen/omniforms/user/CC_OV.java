
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : OV

File Name                                                         : CC_OV

Author                                                            : Disha

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.                 12-6-2017     Disha		   Changes done to hide OV tab
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_OV extends CC_Common implements FormListener
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
		makeSheetsInvisible("Tab1", "7,8,9,11,12,13,14,15,16,17");
		CreditCard.mLogger.info( "Inside formLoaded() of CC_OV");
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        
        try{
        	CreditCard.mLogger.info("Inside CC_OV CC");
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
		formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_DEC_Decision"));
		
		formObject.setNGValue("ORIGINAL_VALIDATION", formObject.getNGValue("VALIDATED"));
		String query;
		String OVDecision;
		List<List<String>> docName;
		
		query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
		CreditCard.mLogger.info(""+query);
		docName = formObject.getDataFromDataSource(query);
		CreditCard.mLogger.info("docname query123"+docName);
		CreditCard.mLogger.info( "add row ov size of doc name123 "+docName.size()+",docName"+docName);
		for(int i=0;i<docName.size();i++ )
		{
			CreditCard.mLogger.info("inside loops");
			OVDecision = docName.get(i).get(13);
			CreditCard.mLogger.info("OVDecision"+OVDecision);
			if("--Select--".equalsIgnoreCase(OVDecision)){
				throw new ValidatorException(new FacesMessage("Please Select OV Decision"));
			}
			else{
				CreditCard.mLogger.info("move out of the loop");
			}
			
		}
		//12th sept
		saveIndecisionGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) 
	{
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_DEC_Decision"));
		
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
				//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
				/*
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				*/
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
				
				//12thsept
				//formObject.setLocked("FATCA_Frame1",true);
				formObject.setLocked("FATCA_Frame6",true);
				//12thsept
			}
			
			if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("KYC_Frame1",true);
				/*formObject.setEnabled("KYC_Save",true);*/
			}
			
			if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				//12th sept
				//formObject.setLocked("OECD_Frame1",true);
				formObject.setLocked("OECD_Frame8",true);
				//12th sept
				
				
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("IncomingDocument_Frame",true);
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				//12th sept
				//formObject.setLocked("Reference_Details_Frame1",true);
				formObject.setLocked("Reference_Details_ReferenceRelationship",true);
				
				//12th sept
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
			//added by yash on 24/8/2017
			if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//12th sept
				formObject.setLocked("NotepadDetails_Frame2",true);
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
				formObject.setVisible("NotepadDetails_SaveButton",true);
				formObject.setTop("NotepadDetails_SaveButton",400);
			}
			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName()) && "DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				
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
                    formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
                    formObject.setVisible("cmplx_DEC_ContactPointVeri",false);	                        
                    

                    //12th sept
                    formObject.setVisible("DecisionHistory_chqbook",false);	
            		formObject.setVisible("DecisionHistory_Label6",false);	
    				 formObject.setVisible("cmplx_DEC_IBAN_No",false);	
					 formObject.setVisible("DecisionHistory_Label5",false);	
					 formObject.setVisible("DecisionHistory_Button2",false);	
					 formObject.setVisible("cmplx_DEC_NewAccNo",false);	
					 formObject.setVisible("cmplx_DEC_ChequebookRef",false);	
					 formObject.setVisible("DecisionHistory_Label4",false);	
					 formObject.setVisible("DecisionHistory_Label10",false);	
					 formObject.setVisible("cmplx_DEC_New_CIFID",false);	
					 formObject.setVisible("DecisionHistory_chqbook",false);	
					 formObject.setVisible("DecisionHistory_Label27",false);	
					 formObject.setVisible("cmplx_DEC_Cust_Contacted",false);	
					 formObject.setVisible("DecisionHistory_Label9",false);	
					 formObject.setVisible("cmplx_DEC_DCR_Refno",false);	
					 formObject.setVisible("DecisionHistory_Decision_ListView1",true);
					 formObject.setVisible("DecisionHistory_Decision_Label4",true);
					 formObject.setVisible("cmplx_DEC_Remarks",true);
					 formObject.setTop("DecisionHistory_save",335);
					 formObject.setTop("DecisionHistory_Decision_ListView1",250);
					 formObject.setLeft("DecisionHistory_Decision_Label4",280);
					 formObject.setLeft("cmplx_DEC_Remarks",280);
					formObject.setTop("cmplx_DEC_Remarks",90);
					formObject.setTop("DecisionHistory_Decision_Label4",74);
					formObject.setTop("DecisionHistory_Decision_Label3",74);
					formObject.setTop("cmplx_DEC_Decision",90);
                    //12th sept
                    

                    //12th sept
                    formObject.setVisible("DecisionHistory_chqbook",false);	
            		formObject.setVisible("DecisionHistory_Label6",false);	
    				 formObject.setVisible("cmplx_DEC_IBAN_No",false);	
					 formObject.setVisible("DecisionHistory_Label5",false);	
					 formObject.setVisible("DecisionHistory_Button2",false);	
					 formObject.setVisible("cmplx_DEC_NewAccNo",false);	
					 formObject.setVisible("cmplx_DEC_ChequebookRef",false);	
					 formObject.setVisible("DecisionHistory_Label4",false);	
					 formObject.setVisible("DecisionHistory_Label10",false);	
					 formObject.setVisible("cmplx_DEC_New_CIFID",false);	
					 formObject.setVisible("DecisionHistory_chqbook",false);	
					 formObject.setVisible("DecisionHistory_Label27",false);	
					 formObject.setVisible("cmplx_DEC_Cust_Contacted",false);	
					 formObject.setVisible("DecisionHistory_Label9",false);	
					 formObject.setVisible("cmplx_DEC_DCR_Refno",false);	
					 formObject.setVisible("DecisionHistory_Decision_ListView1",true);
					 formObject.setVisible("DecisionHistory_Decision_Label4",true);
					 formObject.setVisible("cmplx_DEC_Remarks",true);
					 formObject.setTop("DecisionHistory_save",150);
					 formObject.setTop("DecisionHistory_Decision_ListView1",190);
					 formObject.setLeft("DecisionHistory_Decision_Label4",280);
					 formObject.setLeft("cmplx_DEC_Remarks",280);
					formObject.setTop("cmplx_DEC_Remarks",34);
					formObject.setTop("DecisionHistory_Decision_Label4",20);//remarks
					formObject.setTop("DecisionHistory_Decision_Label3",20);//decision
					formObject.setTop("cmplx_DEC_Decision",34);
                    //12th sept
                    
                    loadPicklist3();
			 
		 } 	
			//12th sept
			if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CC_Loan_Frame1",true);
				formObject.setLocked("CC_Loan_Frame2",true);
				formObject.setLocked("CC_Loan_Frame3",true);
				formObject.setLocked("totBalTransfer",true);
			}
			if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("KYC_Frame1",true);
				/*formObject.setEnabled("KYC_Save",true);*/
			}
			if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("PartMatch_Frame1",true);
				
			}
			if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMIncident_Frame1",true);
				
			}
			if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
				
			}
			if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExternalBlackList_Frame1",true);
				
			}
			if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCore_Frame1",true);
				
			}
			if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("MOL1_Frame1",true);
				
			}
			if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("RejectEnq_Frame1",true);
				
			}
			if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SalaryEnq_Frame1",true);
			}
			//12th sept
	
	}
	
	



}

