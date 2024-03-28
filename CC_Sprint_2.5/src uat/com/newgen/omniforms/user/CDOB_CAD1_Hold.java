
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Digital On Boarding

Module                                                            : CAD1 Hold

File Name                                                         : CDOB_CAD1_Hold

Author                                                            : Sajan

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1. 				   12-6-2017	 Disha 		   Changes done to hide OV tab
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CDOB_CAD1_Hold extends CDOB_Common implements FormListener
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
	public void formLoaded(FormEvent pEvent){
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
		DigitalOnBoarding.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		// Changes done to hide OV tab
		makeSheetsInvisible("Tab1", "1,6,8,11,12,13,14,15,16,17");//pcasf-136 pcasf-392
		DigitalOnBoarding.mLogger.info( "Saurabh FCU");
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header             

	 ***********************************************************************************  */
	//changed by nikhil for PCSP-629
	public void formPopulated(FormEvent pEvent) {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		FormConfig formobject1=FormContext.getCurrentInstance().getFormConfig();
		try{
			DigitalOnBoarding.mLogger.info("Inside CDOB Hold1");
			formObject.setVisible("Product_Frame1",true);//ADDED BY SAURABH1 FOR PCASF-463
			new CDOB_CommonCode().setFormHeader(pEvent);
			//updated for PCSP-629
			//again updated as per Manish understanding for PCSP-629
			/*if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType")) && !"R".equalsIgnoreCase(formobject1.getConfigElement("Mode"))) 
			{
				formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_CreditCard.getGlobalVar("FCU_SAL_Frame_Name"));
			}*/
				
			
		}catch(Exception e)
		{
			DigitalOnBoarding.mLogger.info( "Exception:"+e.getMessage());
		}

	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().FrameExpandEvent(pEvent);

			break;

		case FRAGMENT_LOADED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			fragment_loaded(pEvent,formObject);

			break;

		case MOUSE_CLICKED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().mouse_clicked(pEvent);
			break;

		case VALUE_CHANGED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().value_changed(pEvent);
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
		try{
		CustomSaveForm();}
		catch(Throwable th)
		{
			DigitalOnBoarding.mLogger.info("getttttttt"+th.getMessage());
		}
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
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException 
	{
		//added by akshay on 9/12/17 for multiple refer
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			DigitalOnBoarding.mLogger.info("Value of Submit decision is "+formObject.getNGValue("cmplx_DEC_Decision"));
			//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("FCU_DeC", formObject.getNGValue("cmplx_DEC_Decision"));
			//CHanged by aman for Different Decision Name
			DigitalOnBoarding.mLogger.info("Value of Submit decision is "+formObject.getNGValue("FCU_DeC"));
			//changes done by nikhil for PCSP-546
		
			saveIndecisionGrid();
			//change by aurabh for JIRA - 12666
			//formObject.setNGValue("IsFCUChild_Exists","N");
			DigitalOnBoarding.mLogger.info("Inside PL FCU submitFormCompleted()" + pEvent.getSource()); 
			List<String> objInput=new ArrayList<String> ();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:FCU");
			DigitalOnBoarding.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
			List<Object> objOutput=new ArrayList<Object>();
			objOutput.add("Text");
			//objOutput= formObject.getDataFromStoredProcedure("NG_RLOS_MULTIPLEREFER", objInput,objOutput);
		}
		catch(Exception e){
			DigitalOnBoarding.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}
		catch(Throwable th)
		{
			DigitalOnBoarding.mLogger.info("getttttttt"+th.getMessage());
		}

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
		try{
			CustomSaveForm();
			DigitalOnBoarding.mLogger.info("Value of decision is "+formObject.getNGValue("cmplx_DEC_Decision"));
			//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("FCU_DeC", formObject.getNGValue("cmplx_DEC_Decision"));
			//CHanged by aman for Different Decision Name
			DigitalOnBoarding.mLogger.info("Value of decision is "+formObject.getNGValue("FCU_DeC"));
			//changes done by nikhil for PCSP-546
			//formObject.setNGValue("IsFCUChild_Exists", "N");
			LoadReferGrid();
			formObject.setNGValue("is_CA_Hold_Done", "Y");
			
			
		}
		catch(Exception ex)
		{
			DigitalOnBoarding.mLogger.info(ex.getMessage());
		}
		catch(Throwable th)
		{
			DigitalOnBoarding.mLogger.info("getttttttt"+th.getMessage());
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
				//formObject.setLocked("Customer_Frame1",true);
				LoadView(pEvent.getSource().getName());
				formObject.setLocked("cmplx_Customer_DOb", true);
				formObject.setLocked("cmplx_Customer_IdIssueDate", true);
				formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
				formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
				formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
				formObject.setLocked("cmplx_Customer_VisaExpiry", true);
				formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
				
				formObject.setLocked("Customer_FetchDetails",true);
				formObject.setLocked("Customer_save",true);
				loadPicklistCustomer();
				//setDisabled();
			}	
			
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
				/*
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
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
			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				loadpicklist_Income();
				}
			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("CompanyDetails_Frame1",true);
				formObject.setLocked("CompanyDetails_Frame2",true);
				formObject.setLocked("CompanyDetails_Frame3",true);
				formObject.setLocked("CompanyDetails_Add",true);
				formObject.setLocked("CompanyDetails_Modify",true);
				formObject.setLocked("CompanyDetails_delete",true);
				formObject.setLocked("CompanyDetails_Button1",true);
				formObject.setEnabled("CompanyDetails_Save",false);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_CompanyDet();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			/*else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
	            
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				 }
			else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("PartnerDetails_Frame1",true);
				
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}*/
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame1",true);
				LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			}
			/*else if ("Internal Liabilities".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame2",true);
				
			}
			else if ("External Liabilities".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame3",true);
				
				
			}*/
			
			else if ("Liability Addition".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExtLiability_Frame4",true);		
			}
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//	formObject.setLocked("EMploymentDetails_Frame1", true);
				LoadView(pEvent.getSource().getName());
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklistEmployment();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
				
				
				//loadPicklist4();
			}
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				//Aman CR Changes 061218
				formObject.setEnabled("ELigibiltyAndProductInfo_Refresh",true);
				//Aman CR Changes 061218
			}
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
			//	formObject.setLocked("AddressDetails_Frame1",true);
				LoadView(pEvent.getSource().getName());
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_Address();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
	
			}
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
			//	formObject.setLocked("AltContactDetails_Frame1",true);
				LoadView(pEvent.getSource().getName());
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
				//change by saurabh on 7th Dec
			//	LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
				//change by saurabh for air arabia functionality.
				AirArabiaValid();
			
			}
			else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			}
			//12th sept
			else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Reference_Details_Frame1",true);
			}
			//12th sept
			else if ("CardCollection".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("CardDetails_Frame1",true);
			}
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("SupplementCardDetails_Frame1",true);
			}
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FATCA_Frame6",true);
				formObject.setLocked("FATCA_SignedDate",true);
				formObject.setLocked("FATCA_ExpiryDate",true);
				
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_Fatca();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//++below code added by nikhil for Self-Supp CR
				Load_Self_Supp_Data();
				//--above code added by nikhil for Self-Supp CR
				//loadPicklist_Address();
				formObject.setLocked("CardDetails_Frame1",true);
				Loadpicklist_CardDetails_Frag();
			}
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("KYC_Frame1",true);
				
			}
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				//formObject.setLocked("OECD_Frame8",true);
				LoadView(pEvent.getSource().getName());
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_oecd();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			
			else if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);
				
			}
			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("PartMatch_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				//LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMIncident_Frame1",true);
				
			}
			else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
				
			}
			else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExternalBlackList_Frame1",true);
				
			}
			else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCore_Frame1",true);
				LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
				LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

			}
			else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("MOL1_Frame1",true);
				loadPicklist_Mol();
				
			}
			else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("WorldCheck1_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_WorldCheck();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			}
			else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("RejectEnq_Frame1",true);
				
			}
			else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("SalaryEnq_Frame1",true);
				
			}
			/*else if ("External_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExternalBlackList_Frame1",true);

			}*/
			/*else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("IncomingDocument_Frame",true);
				
			}*/
			
			else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("PostDisbursal_Frame2",true);
				
			}
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);
				//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
				loadPicklist_ServiceRequest();
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				
			}
			else if ("Fraud Control Unit".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
			}
			
			
			
			else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setLocked("SmartCheck_Frame1",true);
				//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
				/*// formObject.setVisible("SmartCheck1_Label2",true);
				 formObject.setLocked("SmartCheck1_CPVRemarks",true);
				// formObject.setVisible("SmartCheck1_Label1",true);
				 formObject.setVisible("SmartCheck1_CreditRemarks",true);*/
				 formObject.setLocked("SmartCheck1_Add",true);
				 formObject.setLocked("SmartCheck1_Modify",true);
				/* formObject.setHeight("SmartCheck1_Label4", 16);
				 formObject.setHeight("SmartCheck1_FCURemarks", 55);*/
				
				 formObject.setLocked("SmartCheck1_CPVRemarks",true);
				 formObject.setLocked("SmartCheck1_CreditRemarks",true);
				//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
				 
			}
			//added by yash on 30/8/2017
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				//++Below code added by  nikhil 12/10/17 as per FSD 2.2	
				notepad_load();
			    formObject.setVisible("NotepadDetails_Frame3",true);
			 
				
			}
				else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
					

					//below line commented by Deepak as the same is called form fram expand method.
					//loadInDecGrid();
					LoadReferGrid();
					if(!formObject.isVisible("ExtLiability_Frame1")){
						formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
					}
					//changed by akshay on 6/12/17 for decision alignment
					Decision_cadanalyst1();
					formObject.setLocked("cmplx_DEC_Manual_Deviation", true);//added by saurabh1 for pcasf-463
					formObject.setEnabled("cmplx_DEC_Manual_Deviation", false);//added by saurabh1 for pcasf-463
					//for decision fragment made changes 8th dec 2017
					//fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Button4#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label13,cmplx_DEC_DeviationCode#DecisionHistory_Label14,cmplx_DEC_DectechDecision#DecisionHistory_Label1,DecisionHistory_NewStrength#DecisionHistory_AddStrength#DecisionHistory_Label3,cmplx_DEC_Strength#\n#DecisionHistory_Label34,DecisionHistory_NewWeakness#DecisionHistory_AddWeakness#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Label15,cmplx_DEC_ScoreGrade#DecisionHistory_Label16,cmplx_DEC_HighDeligatinAuth#DecisionHistory_Label18,cmplx_DEC_ReferTo#DecisionHistory_calReElig#\n#cmplx_DEC_Manual_Deviation#DecisionHistory_ManualDevReason#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
					fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Button4#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label13,cmplx_DEC_DeviationCode#DecisionHistory_Label14,cmplx_DEC_DectechDecision#DecisionHistory_Label1,DecisionHistory_NewStrength#DecisionHistory_AddStrength#DecisionHistory_Label3,cmplx_DEC_Strength#\n#DecisionHistory_Label34,DecisionHistory_NewWeakness#DecisionHistory_AddWeakness#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Label15,cmplx_DEC_ScoreGrade#DecisionHistory_Label16,cmplx_DEC_HighDeligatinAuth#DecisionHistory_calReElig#\n#cmplx_DEC_Manual_Deviation#DecisionHistory_ManualDevReason#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
					formObject.setWidth("DecisionHistory_dec_reason_code", 210);
					formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
					formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
					//for decision fragment made changes 8th dec 2017
					DigitalOnBoarding.mLogger.info( "value of manual deviation is:" + formObject.getNGValue("cmplx_DEC_Manual_Deviation"));
					if ("false".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Manual_Deviation"))){
						formObject.setEnabled("DecisionHistory_ManualDevReason", false);
						formObject.setEnabled("DecisionHistory_calReElig", false);
					}
					else {
						formObject.setEnabled("DecisionHistory_ManualDevReason", true);
						formObject.setEnabled("DecisionHistory_calReElig", true);
					}
					//below code added by nikhil for PCSP-46
					formObject.setLocked("cmplx_DEC_Strength", true);
					formObject.setLocked("cmplx_DEC_Weakness", true);
					formObject.setVisible("DecisionHistory_CifLock",false);
					formObject.setVisible("DecisionHistory_CifUnlock",false);
					formObject.setNGValue("cmplx_DEC_Decision", "Send Back to CA1");
					formObject.setLocked("cmplx_DEC_Decision",false);//showstopper Sagarika 788
					formObject.setLocked("DecisionHistory_NewStrength", true);
					formObject.setLocked("DecisionHistory_AddStrength",true);
					formObject.setLocked("DecisionHistory_NewWeakness", true);
					formObject.setLocked("DecisionHistory_AddWeakness", true);

					
                    
			 }//loadPicklist1();
		  	
	//12th sept
				else if ("BussinessVerification1".equalsIgnoreCase(pEvent.getSource().getName())){
					 if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
						 DigitalOnBoarding.mLogger.info( "Inside add button: business verification1");
						 formObject.setLocked("BussinessVerification1_Frame1",false);
					 }
					 else{
						 DigitalOnBoarding.mLogger.info( "Inside add button: business verification2");
						 formObject.setLocked("BussinessVerification1_Frame1",true);
						 formObject.setLocked("cmplx_BussVerification1_ActualDOB",true);
						 
					 }
				}
				else if ("EmploymentVerification".equalsIgnoreCase(pEvent.getSource().getName())){
					 DigitalOnBoarding.mLogger.info( "employment verification");
					formObject.setLocked("EmploymentVerification_s2",false);
					 DigitalOnBoarding.mLogger.info( "out employment verification");
					/* if("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
						 CreditCard.mLogger.info( "Inside add button: employment verification");
						 formObject.setLocked("EmploymentVerification_Frame1",false);
					 }
					 else{
						 CreditCard.mLogger.info( "Inside add button: business verification2");
						 formObject.setLocked("EmploymentVerification_Frame1",true);
					 }*/
					//++Below code added by  nikhil 13/10/17 as per CC FSD 2.2
				//	 LoadPickList("cmplx_EmploymentVerification_OffTelnoValidatedfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
					 
					//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
					 
				}
	//12th sept
			
			
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
