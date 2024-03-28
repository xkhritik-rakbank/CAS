
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Smart CPV

File Name                                                         : Smart_CPV

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


public class CC_Smart_CPV extends CC_Common implements FormListener
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
		
		
		makeSheetsInvisible(tabName, "9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
        	
           new CC_CommonCode().setFormHeader(pEvent);
         //++ below code added by abhishek as per CC FSD 2.7.3
	    	 enable_CPV();
	    	//-- Above code added by abhishek as per CC FSD 2.7.3
        }catch(Exception e)
        {
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

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


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			// ++ below code not commented at offshore - 06-10-2017
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			//setDisabled();
		}	
		
		if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			*/
			formObject.setLocked("Product_Frame1",true);
		}
		
		if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AddressDetails_Frame1",true);
			loadPicklist_Address();
		}
		
		if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AltContactDetails_Frame1",true);
			
		}
		
		if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		
		if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OECD_Frame8",true);
			// ++ below code not commented at offshore - 06-10-2017
			loadPicklist_oecd();
		}
		if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			// ++ below code not commented at offshore - 06-10-2017
			loadPicklist_Fatca();
			
		}
		if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			
		}
		if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CardDetails_Frame1",true);
			
		}
		if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
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
            
        	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
            LoadPickList("AuthorisedSignDetails_Combo1", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
            LoadPickList("AuthorisedSignDetails_Combo2", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
        }
		if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
            LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
        }
		if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1", true);
			
			loadPicklistEmployment();
		}
		if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
           
         if("Business titanium Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || "Self Employed Credit Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
					if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
						formObject.setVisible("FinacleCore_avgbal", true);
						CreditCard.mLogger.info( "set visible FinacleCore_Frame8 if condition ");
						formObject.setVisible("FinacleCore_Frame8", true);
						CreditCard.mLogger.info( "after set visible FinacleCore_Frame8 if condition ");
						formObject.setVisible("FinacleCore_Frame2", false);
						formObject.setVisible("FinacleCore_Frame3", false);
						CreditCard.mLogger.info( "Inside fianacle core fragment if condition ");
					}
					else{
						formObject.setVisible("FinacleCore_Frame2", true);
						formObject.setVisible("FinacleCore_Frame3", true);
						formObject.setVisible("FinacleCore_avgbal", false);
						CreditCard.mLogger.info( "ELSE set visible FinacleCore_Frame8 ELSE condition ");
						formObject.setVisible("FinacleCore_Frame8", false);
						CreditCard.mLogger.info( "AFTER  Inside fianacle core fragment else condition ");
					}
				}
         		formObject.setLocked("FinacleCore_Frame1", true);
				// ++ below code not commented at offshore - 06-10-2017
				LoadPickList("FinacleCore_ChequeType", "select '--Select--' union select convert(varchar, description) from ng_MASTER_Cheque_Type with (nolock)");
         		LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");
			
        }
		if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
			LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			
			formObject.setLocked("PartMatch_Frame1", true);
        								
        }
		if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			// ++ below code already present - 06-10-2017 - Reference_Details_Frame1 to Reference_Details_ReferenceRelationship
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);
			
		}
		if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			
		}
		if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
		}
		// ++ below code already present - 06-10-2017
		//added by nikhil as per CC FSD
		if("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("Details", true);
		}
		if("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("External_Blacklist", true);
		}
		//ended by nikhil
		// ++ above code already present - 06-10-2017
		if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMIncident_Frame1",true);
			
		}
		if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			
		}
		if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("MOL1_Frame1",true);
			
		}
		if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification_Frame1",true);
			
		}
		if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			
		}
		if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			
		}
		if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			
		}
		if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			
		}
		if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			
		}
		if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);
			
		}
		//added by yash on 30/7/2017
		if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
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
		// ++ below code not commented at offshore - 06-10-2017	
		notepad_load();
		formObject.setVisible("NotepadDetails_Frame3", true);
		}
		if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck_Frame1",true);
			 formObject.setVisible("SmartCheck1_Label4",false);
			 formObject.setVisible("SmartCheck1_FCURemarks",false);
			 formObject.setVisible("SmartCheck1_Label1",false);
			 formObject.setVisible("SmartCheck1_CreditRemarks",false);
			 
			 formObject.setLocked("SmartCheck1_Add",true);
			 formObject.setLocked("SmartCheck1_Delete",true);
			 
			 formObject.setHeight("SmartCheck1_Label2", 16);
			 formObject.setHeight("SmartCheck1_CPVRemarks", 16);
		}
		// ++ below code not commented at offshore - 06-10-2017
		//added by abhishek as per CC FSD
		if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck_CR_Remarks",true);
			
		}
		if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
		}
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
            formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
			// ++ below code not commented at offshore - 06-10-2017
			//added by nikhil as CC FSD
            CreditCard.mLogger.info( "Inside Decision history Load");
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
            
            loadPicklist3();
            	//loadPicklist1();
		 } 	
			
	
	}
	


}

