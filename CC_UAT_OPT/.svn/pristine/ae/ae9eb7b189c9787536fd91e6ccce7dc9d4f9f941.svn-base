
package com.newgen.omniforms.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
	 
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              

	 ***********************************************************************************  */
	 public void formLoaded(FormEvent pEvent)
	{
		
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		makeSheetsInvisible(tabName, "8,9,11,13,14,15,16,17");
		
	}
	
	 
	 
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header             

	 ***********************************************************************************  */
	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	    	 CreditCard.mLogger.info("Inside CC_Disbursal CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	    	//added by saurabh on 31th Dec
	    	 String subProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
	    	 if(subProd.equalsIgnoreCase("LI") || subProd.equalsIgnoreCase("PU") || subProd.equalsIgnoreCase("PULI")){
	    		formObject.setVisible("Limit_Increase", true);
	    		formObject.setVisible("CC_Creation", false);
	    		
	    	 }
	    	 else{
	    		 formObject.setVisible("Limit_Increase", false);
	    		 formObject.setVisible("CC_Creation", true);
	    	 }
	        }catch(Exception e)
	        {
	            CreditCard.mLogger.info( "Exception:"+e.getMessage());
	        }
	        
	    }
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
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


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
		formObject.setNGValue("cmplx_DEC_Remarks","");
		
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		CreditCard.mLogger.info("fragemnt to be loaded is: "+pEvent.getSource().getName());
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			/*if (!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setNGValue("cmplx_Customer_EmirateIDExpiry", "");
			}*/
			//setDisabled();
		}	
		
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			
			formObject.setLocked("Product_Frame1",true);
		}
		
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AddressDetails_Frame1",true);
			loadPicklist_Address();
		}
		
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			
		}
		//added by yash for displaying cifid and customer name in Limit increase fragment on 19/6/2017
		//COMMENTED BY ABHISHEK AS PER cc fsd
		
		
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			/*formObject.setVisible("DecisionHistory_CheckBox1",false);
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
            formObject.setVisible("DecisionHistory_Button4", true);*/
			
           
            fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
            formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
            loadPicklist3();
		} 	
		
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OECD_Frame8",true);
			
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CardDetails_Frame1",true);
			
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			loadpicklist_Income();
			}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
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
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
        	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
            
        	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
            LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
            LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
        }
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1", true);
			
			loadPicklist4();
			Fields_ApplicationType_Employment();
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
           
         if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
					if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
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
         		LoadPickList("FinacleCore_ChequeType", "select '--Select--' union select convert(varchar, description) from ng_MASTER_Cheque_Type with (nolock)");
    			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

			
        }
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
			LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			
			formObject.setLocked("PartMatch_Frame1", true);
        								
        }
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();
		}		
		else if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			//code by saurah on 17th dec
			LoadPickList("CC_Creation_Product","select '--Select--' as description,'' as code union select distinct convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
			String subProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			if(!"PU".equalsIgnoreCase(subProd) && !"LI".equalsIgnoreCase(subProd) && !"PULI".equalsIgnoreCase(subProd)){
			loadInCCCreationGrid();
			}
			else{
				formObject.setLocked("CC_Creation_Frame3",true);
			}
		}
		else if ("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		else if ("Limit_Inc".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			// added by abhishek as per CC FSD
			/*if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_LimitIncrease_Code").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))
					||	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_ProductUpgrade_Code").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))
					||NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PULI_Code").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))
					||	("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) && "TOPIM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4)))){
			*/	
				
				formObject.setLocked("Limit_Inc_Frame4",true);
				formObject.setLocked("cmplx_LimitInc_DetailsUploadedd",false);
				formObject.setLocked("Limit_Inc_Save",false);
				formObject.setLocked("Limit_Inc_Button1",false);
				
				CreditCard.mLogger.info("before calling loadDataInLimitInc");
				loadDataInLimitInc();
				CreditCard.mLogger.info("after calling loadDataInLimitInc");
			//}
			/*else{
				formObject.setLocked("Limit_Inc_Frame4",true);
			}*/
		}
		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FCU_Decision_Frame1",true);
			
		}
		else if ("CAD".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CAD_Frame1",true);
			
		}
		else if ("CAD_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CAD_Decision_Frame1",true);
			
		}
		
		else if ("OriginalValidation".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OriginalValidation_Frame1",true);
			
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMIncident_Frame1",true);
			
		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("MOL1_Frame1",true);
			
			loadPicklist_Mol();
			
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification_Frame1",true);
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");

			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");

		}
		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);
			
		}
		// added by abhishek as per CC FSd
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			notepad_withoutTelLog();
			
			
		}
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck_Frame1",true);
			
		}
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Compliance_Frame1",true);
			
		}
		else if ("CardCollection".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CardCollection_Frame1",true);
			
		}
		
		// fcu fragments start
		else if ("CustDetailVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification1_Frame1",true);
			
		}
		else if ("BussinessVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification1_Frame1",true);
			
		}
		else if ("EmploymentVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EmploymentVerification_Frame1",true);
			
		}
		else if ("BankingCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BankingCheck_IFrame1",true);
			
		}
		//commented by abhishek as per CC FSD
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
			formObject.setLocked("NotepadDetails_Frame1",true);
			
		}*/
		else if ("supervisorsection".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("supervisorsection_Frame1",true);
			
		}
		
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck1_Frame1",true);
			
		}
		
		// fcu fragments end
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
		}

	}
	
	

}

