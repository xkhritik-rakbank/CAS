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
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
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
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	     try{
	    	 CreditCard.mLogger.info("Inside CC_DDVT CHECKER CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	    	 //below code added by nikhil 18/10 for mandatory frames at CreateCIF
	    	  //Done By aman for Sprint2
	    		if (formObject.getNGValue("RefFrmDDVT").equals("Y") ){
	    			formObject.setSheetVisible(tabName,9,true);
	    		}
	    	 if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
	    	 {
	    	 formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_CreditCard.getGlobalVar("DDVT_Frame_Name"));
	    	//Added by Rajan for PCASP-1914
	    	 formObject.setNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,0, formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",0,3));
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
	Description                         : For Fetching the Fragments in DDVT Checker         

	***********************************************************************************  */
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String alert_msg="";
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();
              //try{

				switch(pEvent.getType())
				{	

				case FRAME_EXPANDED:
					//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);						
					break;
					
					case FRAGMENT_LOADED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						fragment_loaded(pEvent,formObject);
					  break;
					  
					case MOUSE_CLICKED:
					//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().mouse_clicked(pEvent);
						break;
					 case VALUE_CHANGED:
						//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		CustomSaveForm();
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
		//Code added by Deepak on 16 Dec to save customer full name & CIF in Ext table
		try{
			CustomSaveForm();
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if("".equalsIgnoreCase(formObject.getNGValue("CUSTOMERNAME"))){
				if("".equals(formObject.getNGValue("cmplx_Customer_MiddleNAme")))
				{
					formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme"));
					//changed from customer_name to customerName by akshay on 17/4/18
				}

				else{
					formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme"));
					//changed from customer_name to customerName by akshay on 17/4/18
				}
			}
			if("".equalsIgnoreCase(formObject.getNGValue("CIF_ID"))){
				formObject.setNGValue("CIF_ID",formObject.getNGValue("cmplx_Customer_CIFNo"));
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured while setting Customer name & CIf in ext table at DDVT checker"+ e.getMessage());
		}
		
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
		try{
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		CreditCard.mLogger.info("Inside PL PROCESS ddvtchecker submitFormCompleted()" + pEvent.getSource()); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Deepak changes done for PCAS-2982
		String dec = formObject.getNGValue("cmplx_DEC_Decision");
			if("Approve".equalsIgnoreCase(dec)){
				List<String> objInput=new ArrayList<String>();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				//Deepak Chnages for PCAS-2983
				if("Y".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif"))){
					objInput.add("Text:"+"ASTC");
				}
				else{
					objInput.add("Text:"+"Waiting for Approval");
				}
				
				CreditCard.mLogger.info("objInput args are: "+objInput.get(0)+"::: "+objInput.get(1));
				List<Object> objOutput=new ArrayList<Object>();
					objOutput.add("Text");
					objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
					//CreditCard.mLogger.info("objOutput args are: "+objOutput.toString());
					//++ above code added by abhishek on 04/01/2018 for EFMS refresh functionality
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED

CPV_REQUIRED
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		//Deepak Changes done to validate Air Arabia Identifier at DDVT cheker stage as well. 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String cardProd = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"), 0);
		if(null!=cardProd && !"".equals(cardProd)){
			if("Invalid".equals(checkDedupAirArabia(cardProd))){
				String alertMsg="Kindly fill Air Arabia Identifier for this card";
				throw new ValidatorException(new FacesMessage(alertMsg));	
			}
			//added by saurabh1 for enroll
			if("Invalid".equals(checkDedupenrollreward(cardProd))){
				String alertMsg="Kindly fill Ecrn Identifier for this card";
				throw new ValidatorException(new FacesMessage(alertMsg));	
			}
			//end by saurabh1 for enroll
		}
		
		try{
			Data_reset("DecisionHistory");
			saveIndecisionGrid();
			CustomSaveForm();
			//Added by shivang for PCASP-1914
			updateCifInAuthorizedGrid();
			if(!NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployed").equals(formObject.getNGValue("EmploymentType"))){
				formObject.setNGValue("cmplx_IncomeDetails_totSal", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai"));
			}//added by akshay for proc 9940
			formObject.setNGValue("EMPLOYMENT_DETAILS_MATCH","Yes");
			//below code commeneted by nikhil
			//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			//deepak Changes done to handle Approve subject to Cif case if CAD case already processed: start
			if("Y".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif")) && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				CreditCard.mLogger.info("Submit form started of DDVT checker case of Approve Sub to CIF :: Winame: "+ formObject.getWFWorkitemName()+" user name: "+ formObject.getUserName());
				formObject.setNGValue("q_Hold2", 1);
				formObject.setNGValue("q_PrevHold", "Hold2");
				
			}
			
			//deepak Changes done to handle Approve subject to Cif case if CAD case already processed: End
			formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_DEC_Decision"));		
			//below code added by nikhil to handle refer to DDVT CPV child Scenario
			formObject.setNGValue("CA_Refer_DDVT", "N");
			formObject.setNGValue("IS_CPV", "N");
			formObject.setNGValue("CIF_ID",formObject.getNGValue("cmplx_Customer_CIFNo"));
			if(Check_CPV_Refer_DDVT())
			{
				formObject.setNGValue("IS_CPV", "Y");
			}
			//++below code added by nikhil for PCAS-2140 CR
			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
			List<String> objInput_Liab=new ArrayList<String>();
			 List<Object> objOutput_Liab=new ArrayList<Object>();
			 objInput_Liab.add("Text:"+formObject.getNGValue("parent_WIName"));
			 CreditCard.mLogger.info("objOutput_Liab args are: "+objInput_Liab.get(0));
			 objOutput_Liab.add("Text");
			 objOutput_Liab=formObject.getDataFromStoredProcedure("ng_RLOS_ClearLiability", objInput_Liab,objOutput_Liab);
			}			
			//formObject.setNGValue("ReferTo", formObject.getNGValue("DecisionHistory_ReferTo"));
		//	formObject.setNGValue("q_MailSubject", formObject.getNGValue("DecisionHistory_ReferTo")); //temp commented by aman
			//changed by aman for name change
			formObject.setNGValue("NEXT_workstep","");
			LoadReferGrid();
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%DDVT_Checker%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + FCURefer);
			
			//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + squeryloan);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
				}
			if("FPU".equalsIgnoreCase(referto)){		
				formObject.setNGValue("RefFrmDDVT", "Y");
			}
			////formObject.setNGValue("cmplx_DEC_Remarks","");
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}
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
			//formObject.setLocked("Customer_Frame1",true);
			LoadView(pEvent.getSource().getName());
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
				//formObject.setLocked("CreditCardEnq_Frame1",true);
				formObject.setLocked("CreditCard_Enq", true);
				
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
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
			}

			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//	formObject.setLocked("EMploymentDetails_Frame1", true);
				LoadView(pEvent.getSource().getName());
				//loadPicklistEmployment();
				
				loadPicklist4();
				// ++ below code already present - 06-10-2017
				// added on 25septfor bug list
				formObject.setVisible("EMploymentDetails_Label33",false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				//below code added by akshay for proc 10260
				formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
				formObject.setVisible("EMploymentDetails_Label5", false);
				formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate", false);
				formObject.setVisible("EMploymentDetails_Label6", false);
			}
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setLocked("AddressDetails_Frame1",true);
				LoadView(pEvent.getSource().getName());
				loadPicklist_Address();
			}
			
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
				//change by saurabh on 7th Dec
				//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
				LoadView(pEvent.getSource().getName());
				//formObject.setLocked("AltContactDetails_Frame1",true);
				//change by saurabh for air arabia functionality.
				AirArabiaValid();
				enrollrewardvalid();//added by saurabh1 for enroll
			}
			
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setLocked("OECD_Frame8",true);
				LoadView(pEvent.getSource().getName());
				
			}
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("KYC_Frame1",true);
				
			}
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("FATCA_Frame6",true);
				formObject.setLocked("FATCA_SignedDate",true);
				formObject.setLocked("FATCA_ExpiryDate",true);
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
	            */	loadPicklist_CompanyDet();
	            /* PCASP-3002
	            String customerName = formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
	        	new CC_CommonCode().AddInAuthorisedGrid(formObject,customerName);
	    		new CC_CommonCode().AddInCompanyGrid(formObject, customerName);
	    		*/
	    		}
			/*else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			*/

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
				LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
				LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

				formObject.setLocked("FinacleCore_Frame1",true);
				ChangeRepeater();
				ChangeRepeaterTrnover();
				/*formObject.setEnabled("OECD_Save",true);*/
			}

			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setNGFrameState("ProductContainer", 0);			
				//SLoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
				formObject.setLocked("PartMatch_Frame1", true);
            }
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SupplementCardDetails_Frame1",true);
				formObject.setVisible("SupplementCardDetails_Button1",true);
				formObject.setEnabled("SupplementCardDetails_Button1",true);
				formObject.setLocked("SupplementCardDetails_Button1",false);
				loadPicklist_suppCard();
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//++below code added by nikhil for Self-Supp CR
				Load_Self_Supp_Data();
				//--above code added by nikhil for Self-Supp CR
				formObject.setLocked("CardDetails_Frame1",true);
				//added by nikhil for disbursal showstopper
				Loadpicklist_CardDetails_Frag();
				formObject.sortNGListview("cmplx_CardDetails_cmplx_CardCRNDetails", 9, false);
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
				//notepad_withoutTelLog();
				//change done by nikhil for PCAS-2356
				formObject.setVisible("NotepadDetails_Frame3",true);
				formObject.setLocked("NotepadDetails_Frame3",true);
			}
			//ended by yash
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				//code added by nkhil for supplementary issue in case of existuing
				 if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
		    	 {
					 openDemographicTabs();
		    	 }
				
				formObject.setEnabled("cmplx_DEC_Strength",false);
				formObject.setEnabled("cmplx_DEC_Weakness",false);
				formObject.setLocked("cmplx_DEC_New_CIFID",true);
				formObject.setLocked("cmplx_DEC_VerificationRequired",true);
				CreditCard.mLogger.info( "inside decision history fragment load");
				//change by saurabh for Drop 4
				 /* if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_New_CIFID"))){
	                	formObject.setVisible("DecisionHistory_Button2",true);
	                	//fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Button2#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
	                	fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#cmplx_DEC_MultipleApplicantsGrid#\n#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Button2#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
	                }
	                else{
	                	*/
	                	fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#cmplx_DEC_MultipleApplicantsGrid#\n#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
	                	formObject.setWidth("DecisionHistory_dec_reason_code", 210);
	                	//formObject.setVisible("DecisionHistory_Button2",false);
	                //}
              
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
				CreditCard.mLogger.info("Before adding in Multiple applicants Grid!!!");

				try{
					/*if(formObject.isVisible("cmplx_DEC_MultipleApplicantsGrid")){
						formObject.clear("cmplx_DEC_MultipleApplicantsGrid");
						}*/
					//if(formObject.getLVWRowCount("cmplx_DEC_MultipleApplicantsGrid")>0){
						String grid_applicantType="";
						List<List<String>> mylist=new ArrayList<List<String>>();
						List<String> subList=new ArrayList<String>();
						//Deepak condition added for PCAS-2473
						if("Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",0,0))
								&& "".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",0,3))
										&& (!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNo")))
										&& "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
							formObject.setNGValue("cmplx_DEC_MultipleApplicantsGrid",0,3,formObject.getNGValue("cmplx_Customer_CIFNo"));
						}
						
						for(int i=0;i<formObject.getLVWRowCount("cmplx_DEC_MultipleApplicantsGrid");i++){
							grid_applicantType+=formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",i,0)+",";
						}
						CreditCard.mLogger.info("Applicant types in Grid is: "+grid_applicantType);

				/*		if(!grid_applicantType.contains("Primary")){
							
							subList.add("Primary");
							String fname = formObject.getNGValue("cmplx_Customer_FirstNAme");
							String lname = formObject.getNGValue("cmplx_Customer_LastNAme");
							subList.add(fname+" "+lname);	
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
						}*/
						
						if(!grid_applicantType.contains("Supplement")){
							int n=formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");
							
								List<String> suppPassAdded = new ArrayList<String>();
								
								CreditCard.mLogger.info("supplementary grid row count: "+n);
							for(int i=0;i<n;i++){
								subList.clear();
								Map<String,String> suppGridColumnsToValues = initializeGridMap("SupplementCardDetails_cmplx_supplementGrid",i);
								String statusSupp = suppGridColumnsToValues.get(getValueOfConstant("Supp_Status"));//getValueOfConstant(constName)//formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,35);//.equals("Active");
								String passSupp = suppGridColumnsToValues.get(getValueOfConstant("Supp_Passport_No"));//formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3);
								//String cardProdSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,30);
								
								if(!suppPassAdded.contains(passSupp) && "Active".equals(statusSupp)){
								subList.add("Supplement");
								subList.add(suppGridColumnsToValues.get(getValueOfConstant("Supp_First_Name"))+" "+suppGridColumnsToValues.get(getValueOfConstant("Supp_Last_Name")));
								subList.add(suppGridColumnsToValues.get(getValueOfConstant("Supp_Passport_No")));
								subList.add(suppGridColumnsToValues.get(getValueOfConstant("Supp_CIF_ID")));
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
						}
						CreditCard.mLogger.info("mylist 3: "+mylist);
						if(mylist.size()>0){
							for(List<String> temp:mylist){
								formObject.addItemFromList("cmplx_DEC_MultipleApplicantsGrid", temp);
							}
						}
					//}		
				}catch(Exception e){CreditCard.mLogger.info(printException(e));}
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



	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}



	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	

}

