
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CPV

File Name                                                         : CC_CPV

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

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


public class CC_CPV extends CC_Common implements FormListener
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
		
		
		makeSheetsInvisible(tabName, "9,11,12,13,14,15,16,17");
		
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
	    	 CreditCard.mLogger.info("Inside CC_Hold_CPV CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	    	 enable_CPV();
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
		
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  //formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					new CC_CommonCode().FrameExpandEvent(pEvent);
									
					 		break;
					
						case FRAGMENT_LOADED:
								CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
								fragment_loaded(pEvent);
								
								
						
							
					
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
		
		 
		saveIndecisionGrid();
		
		
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
		formObject.setNGValue("CPV_DECISION", formObject.getNGValue("cmplx_DEC_Decision"));
		//below code commeneted by nikhil 07/12/17
		/* formObject.setNGValue("CPV_DECISION", formObject.getNGValue("cmplx_DEC_Decision")); 
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));*/
		//below code added by nikhil
		  if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		    {
		        formObject.setNGValue("IS_Approve_Cif","Y");
		    }
		  if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		  {
			  CreditCard.mLogger.info("Inside  Approve CPV");
			  formObject.setNGValue("q_hold1",1);
			  formObject.setNGValue("VAR_INT3",1);
			  CreditCard.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
		  }

		LoadReferGrid();
		////formObject.setNGValue("cmplx_DEC_Remarks","");

	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent)
	{
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();
			//setDisabled();
		}	
		
		if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			*/
			//code changed to load master's of product based on requested product.
			formObject.setLocked("Product_Frame1",true);
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
		}
		
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			loadpicklist_Income();}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
               formObject.setLocked("CompanyDetails_Frame1", true);
               formObject.setLocked("CompanyDetails_Frame2", true);
               formObject.setLocked("CompanyDetails_Frame3", true);
               formObject.setLocked("TLIssueDate", true);
               formObject.setLocked("TLExpiry", true);
               formObject.setLocked("estbDate", true);
               formObject.setLocked("AuthorisedSignDetails_DOB", true);
               formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate", true);
               formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate", true);
               
               
               formObject.setLocked("PartnerDetails_Dob",true);
         
/*				               LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
               LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
               LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMacro with (nolock)");
               LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMicro with (nolock)");
               LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
               LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
               LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
               LoadPickList("EOW", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
               LoadPickList("headOffice", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
*/								loadPicklist_CompanyDet();
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
		
		
		
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OECD_Frame8",true);
			
		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//12thsept
			//formObject.setLocked("Reference_Details_Frame1",true);
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);
			//12th sept
			
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			loadPicklist_Fatca();
			
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
			
		//loadPicklistEmployment();
		}
		
		if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
           
         if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("FinacleCore_avgbal", true);
				CreditCard.mLogger.info( "set visible FinacleCore_Frame8 else if condition ");
				formObject.setVisible("FinacleCore_Frame8", true);
				CreditCard.mLogger.info( "after set visible FinacleCore_Frame8 else if condition ");
				formObject.setVisible("FinacleCore_Frame2", false);
				formObject.setVisible("FinacleCore_Frame3", false);
				CreditCard.mLogger.info( "Inside fianacle core fragment else if condition ");
				
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
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
			LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			
			formObject.setLocked("PartMatch_Frame1", true);
        								
        }
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);
			
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
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		//++Below code added by nikhil 13/11/2017 for Code merge
		
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
			loadPicklist_ServiceRequest();
			//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			
		}
		//--Above code added by nikhil 13/11/2017 for Code merge
		// added by abhishek as per CC FSD
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("CustDetailVerification_Frame1",true);
			//enable_custVerification();

			//LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			enable_custVerification();
		}
		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();
			
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("HomeCountryVerification_Frame1",true);
			enable_homeVerification();
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("ResidenceVerification_Frame1",true);
			enable_ResVerification();
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			CreditCard.mLogger.info( "set visible OfficeandMobileVerification inside condition ");
			
			//enable_officeVerification();
			// added by abhishek to disable office verification
			formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			formObject.setEnabled("OfficeandMobileVerification_Enable", true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			//++Below code added by nikhil 13/11/2017 for Code merge
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
			
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("LoanandCard_Frame1",true);
			enable_loanCard();
			
		}
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
	    	
			//formObject.setLocked("NotepadDetails_Frame1",true);
			
			// added by abhishek as per CC FSD
			formObject.setVisible("NotepadDetails_Frame3",true);
			
			//formObject.setLocked("NotepadDetails_Frame1",true);
			
		}
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++ below code added by abhishek as per CC FSD 2.7.3
			formObject.setLocked("SmartCheck_Frame1",true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			
			
		}
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
		}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017
		/*	formObject.setVisible("DecisionHistory_CheckBox1",false);
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
           
            // added by abhishek as per CC FSD
            // hide fields
            formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
            formObject.setVisible("DecisionHistory_Decision_Label1",false);
            formObject.setVisible("cmplx_DEC_VerificationRequired",false);
            formObject.setVisible("DecisionHistory_Label10",false);
            formObject.setVisible("cmplx_DEC_New_CIFID",false);
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
            formObject.setVisible("cmplx_DEC_NewAccNo",false);
            formObject.setVisible("cmplx_DEC_IBAN_No",false);
            formObject.setVisible("DecisionHistory_Label5",false);
            
            // show fields
            formObject.setVisible("DecisionHistory_Label11",true);
            formObject.setVisible("DecisionHistory_dec_reason_code",true);
            formObject.setVisible("DecisionHistory_Label12",true);
            formObject.setVisible("cmplx_DEC_NoofAttempts",true);
            formObject.setVisible("DecisionHistory_Decision_Label4",true);
            formObject.setVisible("cmplx_DEC_RejectReason",true);
            formObject.setEnabled("cmplx_DEC_RejectReason",true);*/
			//for decision fragment made changes 8th dec 2017
            //formObject.setVisible("cmplx_DEC_Remarks",true);
            cpv_Decision();
            loadPicklist3();
          //for decision fragment made changes 8th dec 2017
            CreditCard.mLogger.info("***********Inside decision history");
        	fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
        	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
        	CreditCard.mLogger.info("***********Inside after fragment alignment decision history");
			
        	//for decision fragment made changes 8th dec 2017
        	formObject.setLocked("cmplx_DEC_VerificationRequired", true);
          
            	

            	//loadPicklist1();
		 } 	
	}
	
	

}

