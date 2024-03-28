/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : DDVT Checker

File Name                                                         : CC_DDVT_Checker

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1. 				   9-6-2017      Disha         Change done to Load decisions on the basis of Initiation type 
2.                 9-6-2017      Disha         Changes done for testing purpose EMPLOYMENT_DETAILS_MATCH set to 'Yes'
1007				17-9-2017	Saurabh			changes in Decision History fragment.
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
											 

public class CC_DDVT_Checker extends CC_Common implements FormListener
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
		
		makeSheetsInvisible(tabName, "8,9,11,12,13,14,15,16,17");
		
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
	    	 CreditCard.mLogger.info("Inside CC_DDVT CHECKER CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
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
	Description                         : For Fetching the Fragments in DDVT Checker         

	***********************************************************************************  */
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String alert_msg="";
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();
              //try{

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
				
              /*
               * }
             
              catch(ValidatorException ex)
              {
            	  HashMap<String,String> hm1=new HashMap<String,String>();
					hm1.put("Error","Checked");
            	   CreditCard.mLogger.info("popupFlag value: "+ popupFlag);
					if(popupFlag.equalsIgnoreCase("Y"))
					{
						CreditCard.mLogger.info("Inside popup msg through Exception "+ popupFlag);
						if(popUpControl.equals(""))
						{
							CreditCard.mLogger.info("Before show Exception at front End "+ popupFlag);
							throw new ValidatorException(new FacesMessage(alert_msg));
							//try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));}finally{hm1.clear();}
						}else
						{
							throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

						}
						
					}
					else{
					
					if(!popUpMsg.equals("")) {
						try{
							throw new ValidatorException(new CustomExceptionHandler("Details Fetched", popUpMsg,"EventType", hm1));
							}
						finally{
								hm1.clear();
								}
						
					}
				else {
						try{ 
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));
							}finally
						{
								hm1.clear();
								}
						
					}
					
					}
				
              }
              catch(Exception ex)
				{
					 CreditCard.mLogger.info("Inside Exception to show msg at front end");
					 HashMap<String,String> hm1=new HashMap<String,String>();
						hm1.put("Error","Checked");
						
					 
						CreditCard.logException(ex);
					CreditCard.mLogger.info("exception in eventdispatched="+ ex);
					
				}	*/
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
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		
		
		CreditCard.mLogger.info("Inside PL PROCESS ddvtchecker submitFormCompleted()" + pEvent.getSource()); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();
	
		objInput.add("Text:"+formObject.getWFWorkitemName());
		
		CreditCard.mLogger.info("objInput args are: "+objInput.get(0));

		
			formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput);

			//++ above code added by abhishek on 04/01/2018 for EFMS refresh functionality
		
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
		saveIndecisionGrid();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("EMPLOYMENT_DETAILS_MATCH","Yes");
		//below code commeneted by nikhil
		//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
		formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_DEC_Decision"));		
		formObject.setNGValue("ReferTo", formObject.getNGValue("DecisionHistory_ReferTo"));
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
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_NEP", true);
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_VisaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
			}	
			
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
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
				//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
				formObject.setLocked("IncomeDetails_BankStatFromDate",true);
				formObject.setLocked("IncomeDetails_BankStatToDate",true);
				
				
				loadpicklist_Income();
			}

		//21/08/2017 it should be disabled added
			else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info("inside locking of reference details");
				formObject.setLocked("Reference_Details_Frame1",true);
				
			}
			else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info("inside locking of reject enquiry");
				formObject.setLocked("RejectEnq_Frame1",true);
				
			}
			else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info("inside locking of Salary Enquiry");
				formObject.setLocked("SalaryEnq_Frame1",true);
				
			}
			else if ("CreditCardEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info("inside locking of Credit Card");
				formObject.setLocked("CreditCardEnq_Frame1",true);
				
			}
			else if ("LOS".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info("inside locking of LOS_Frame1");
				formObject.setLocked("LOS_Frame1",true);
				
			}
			else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info("inside locking of External Blacklist");
				formObject.setLocked("ExternalBlackList_Frame1",true);
				
			}
			//21/08/2017 it should be disabled added 
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			}
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				formObject.setVisible("ELigibiltyAndProductInfo_Refresh", false);
			}

			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("EMploymentDetails_Frame1", true);
				loadPicklistEmployment();
				
				loadPicklist4();
				// ++ below code already present - 06-10-2017
				// added on 25septfor bug list
				formObject.setVisible("EMploymentDetails_Label33",false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			}
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("AddressDetails_Frame1",true);
				loadPicklist_Address();
			}
			
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
				//change by saurabh on 7th Dec
				LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
				
				formObject.setLocked("AltContactDetails_Frame1",true);
				
			}
			
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("OECD_Frame8",true);
				
			}
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("KYC_Frame1",true);
				
			}
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("FATCA_Frame6",true);
				loadPicklist_Fatca();
			}

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
	               
	            /*   
					LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
	            */	loadPicklist_CompanyDet();}
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
			

			//21/08/2017 it should be enabled 
			else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
				// added by abhishek as per CC FSD
				formObject.setLocked("WorldCheck1_Frame1",true);
				//formObject.setLocked("WorldCheck1_Frame1",false);
				}
			//21/08/2017 it should be enabled 
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
				loadPicklist_ServiceRequest();
				formObject.setLocked("CC_Loan_Frame5",true);
				formObject.setLocked("CC_Loan_Frame4",true);
				formObject.setLocked("CC_Loan_Frame2",true);
				formObject.setLocked("CC_Loan_Frame3",true);
				// added by abhishek as per CC FSD
				formObject.setLocked("CC_Loan_Frame1",true);
				//loadPicklist_ServiceRequest();
			}
			
			
			else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())){
				LoadPickList("FinacleCore_ChequeType", "select '--Select--' union select convert(varchar, description) from ng_MASTER_Cheque_Type with (nolock)");
				LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

				formObject.setLocked("FinacleCore_Frame1",true);
				ChangeRepeater();
				ChangeRepeaterTrnover();
				/*formObject.setEnabled("OECD_Save",true);*/
			}

			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setNGFrameState("ProductContainer", 0);			
				LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
				formObject.setLocked("PartMatch_Frame1", true);
            }
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SupplementCardDetails_Frame1",true);
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CardDetails_Frame1",true);
			}
			//shifted here by akshay on 17/1/18
			else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setLocked("IncomingDocument_Frame1",true);
				if(	"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
					formObject.setVisible("IncomingDoc_UploadSig",false);
				else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					//formObject.setVisible("IncomingDoc_ViewSIgnature",false);
					formObject.setVisible("IncomingDoc_UploadSig",true);
					//formObject.setEnabled("IncomingDoc_UploadSig",false);
				}
			}
			//++ Below Code added By Yash on Oct 6, 2017  to fix : "3-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				notepad_load();
				notepad_withoutTelLog();
			}
			//ended by yash
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setEnabled("cmplx_DEC_Strength",false);
				formObject.setEnabled("cmplx_DEC_Weakness",false);
				formObject.setLocked("cmplx_DEC_New_CIFID",true);
				formObject.setLocked("cmplx_DEC_VerificationRequired",true);
				CreditCard.mLogger.info( "inside decision history fragment load");
				//change by saurabh for Drop 4
				  if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_New_CIFID"))){
	                	formObject.setVisible("DecisionHistory_Button2",true);
	                	//fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Button2#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
	                	fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#cmplx_DEC_MultipleApplicantsGrid#\n#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Button2#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
	                }
	                else{
	                	//fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
	                	fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#cmplx_DEC_MultipleApplicantsGrid#\n#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
	                	formObject.setVisible("DecisionHistory_Button2",false);
	                }
              
    			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Telesales_Init").equalsIgnoreCase(formObject.getNGValue("InitiationType")))
    			{
    				loadPicklist3();
    			}
    			else
    			{
    				loadPicklistChecker();
    			}
    			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				//added by akshay on 20/2/18
				try{
					/*if(formObject.isVisible("cmplx_DEC_MultipleApplicantsGrid")){
						formObject.clear("cmplx_DEC_MultipleApplicantsGrid");
						}*/
					if(formObject.getLVWRowCount("cmplx_DEC_MultipleApplicantsGrid")==0){
						List<List<String>> mylist=new ArrayList<List<String>>();
						List<String> subList=new ArrayList<String>();
						subList.add("Primary");
						subList.add(formObject.getNGValue("CUSTOMERNAME"));	
						subList.add(formObject.getNGValue("cmplx_Customer_PAssportNo"));
						subList.add(formObject.getNGValue("cmplx_DEC_New_CIFID"));
						//subList.add("");//CIF created						
						subList.add(formObject.getWFWorkitemName());
						if(formObject.getNGValue("is_cust_verified").equals("Y")){
							subList.add(formObject.getNGValue("is_cust_verified"));
						}
						else if(formObject.getNGValue("cmplx_Customer_CIFID_Available").equalsIgnoreCase("true")){
							subList.add("Y");
						}
						else{
							subList.add("");
						}
						if(formObject.getNGValue("Is_CustLock").equals("Y")){
							subList.add(formObject.getNGValue("Is_CustLock"));
						}
						else{
							subList.add("");
						}
						mylist.add(new ArrayList<String>(subList));
						CreditCard.mLogger.info("mylist 1: "+mylist);
						subList.clear();
						int n=formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");
						
							List<String> suppPassAdded = new ArrayList<String>();
							CreditCard.mLogger.info("supplementary grid row count: "+n);
						for(int i=0;i<n;i++){
							subList.clear();
							String statusSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,35);//.equals("Active");
							String passSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3);
							String cardProdSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,30);
							
							if(!suppPassAdded.contains(passSupp) && "Active".equals(statusSupp)){
							subList.add("Supplement");
							subList.add(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0)+" "+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2));
							subList.add(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3));
							subList.add("");
							/*subList.add("");//CIF verified
							subList.add("");//cif locked
							subList.add("");//cif update
							*/
							subList.add(formObject.getWFWorkitemName());
							subList.add("");//cif verified
							subList.add("");//cif locked
							mylist.add(new ArrayList<String>(subList));//each time adding we need to give a new list reference or it will pick up the old one and uon clear it will get deleted as well
							suppPassAdded.add(passSupp);
						}
						}
						CreditCard.mLogger.info("mylist 3: "+mylist);
						if(mylist.size()>0){
							for(List<String> temp:mylist){
								formObject.addItemFromList("cmplx_DEC_MultipleApplicantsGrid", temp);
							}
						}
					}		
				}catch(Exception e){printException(e);}
				//for decision fragment made changes 8th dec 2017
				//Ref. 1007 end.
				//for decision fragment made changes 8th dec 2017
				//Ref. 1007 end.
		 } 	
	
//added by abhishek as per CC FSD
			
			else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("FinacleCRMIncident_Frame1", true);
			}
			
			else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("FinacleCRMCustInfo_Frame1", true);
			}
			
			else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("MOL1_Frame1", true);
				loadPicklist_Mol();
			}
			//added on 10/12/17 by akshay
			else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadReferGrid();
			}
	}

	

}

