
package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;

import com.newgen.omniforms.listener.FormListener;


public class CC_Disbursal extends CC_Common implements FormListener
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
		
		makeSheetsInvisible(tabName, "8,9,11,13,14,15,16,17");
		
	}
	
	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 CreditCard.mLogger.info("Inside CC_Disbursal CC");
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
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().FrameExpandEvent(pEvent);						
						
						break;
					
					case FRAGMENT_LOADED:
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
		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			if ((formObject.getNGValue("cmplx_Customer_NEP"))=="true"){
				formObject.setNGValue("cmplx_Customer_EmirateIDExpiry", "");
			}
			//setDisabled();
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			
			formObject.setLocked("Product_Frame1",true);
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			formObject.setLocked("AddressDetails_Frame1",true);
			loadPicklist_Address();
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
			formObject.setLocked("AltContactDetails_Frame1",true);
			
		}
		//added by yash for displaying cifid and customer name in Limit increase fragment on 19/6/2017
		//COMMENTED BY ABHISHEK AS PER cc fsd
		
		
		if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			
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
           
            formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
            formObject.setVisible("DecisionHistory_Button4", true);
            loadPicklist3();
	 } 	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
			formObject.setLocked("OECD_Frame8",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
			formObject.setLocked("FATCA_Frame6",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
			formObject.setLocked("CardDetails_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
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
		if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
        	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
            
        	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
            LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			}
		if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
			formObject.setLocked("PartnerDetails_Frame1", true);
            LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
        }
		if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
			formObject.setLocked("EMploymentDetails_Frame1", true);
			
			loadPicklist4();
			Fields_ApplicationType_Employment();
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
           
         if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){
					if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
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
			
        }
		if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
			LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			
			formObject.setLocked("PartMatch_Frame1", true);
        								
        }
		if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
			formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();
		}		
		if (pEvent.getSource().getName().equalsIgnoreCase("CC_Creation")) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal")) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc")) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			// added by abhishek as per CC FSD
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Limit Increase")
					||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Product Upgrade")
					||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Product Upgrade with Limit increase")
					||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM topup")){
				
				formObject.setLocked("Limit_Inc_Frame4",false);
			}
			else{
				formObject.setLocked("Limit_Inc_Frame4",true);
			}
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FCU_Decision")) {
			formObject.setLocked("FCU_Decision_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CAD")) {
			formObject.setLocked("CAD_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
			formObject.setLocked("CAD_Decision_Frame1",true);
			
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("OriginalValidation")) {
			formObject.setLocked("OriginalValidation_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			formObject.setLocked("ExtLiability_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
			formObject.setLocked("FinacleCRMIncident_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
			formObject.setLocked("MOL1_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {
			formObject.setLocked("CustDetailVerification_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {
			formObject.setLocked("LoanandCard_Frame1",true);
			
		}
		// added by abhishek as per CC FSd
		if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
			notepad_load();
			notepad_withoutTelLog();
			
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {
			formObject.setLocked("SmartCheck_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
			formObject.setLocked("Compliance_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CardCollection")) {
			formObject.setLocked("CardCollection_Frame1",true);
			
		}
		
		// fcu fragments start
		if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1")) {
			formObject.setLocked("CustDetailVerification1_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1")) {
			formObject.setLocked("BussinessVerification1_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification")) {
			formObject.setLocked("EmploymentVerification_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("BankingCheck")) {
			formObject.setLocked("BankingCheck_IFrame1",true);
			
		}
		//commented by abhishek as per CC FSD
		/*if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
			formObject.setLocked("NotepadDetails_Frame1",true);
			
		}*/
		if (pEvent.getSource().getName().equalsIgnoreCase("supervisorsection")) {
			formObject.setLocked("supervisorsection_Frame1",true);
			
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) {
			formObject.setLocked("SmartCheck1_Frame1",true);
			
		}
		
		// fcu fragments end
		if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
		}

	}
	
	

}

