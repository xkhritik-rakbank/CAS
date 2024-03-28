
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Sales Coordinator CSM

File Name                                                         : CC_SalesCoordinatorCSM

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

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;



// Changes - 1. On Sales coordinator Incoming Document frame is editable 
public class CC_SalesCoordinatorCSM extends CC_Common implements FormListener
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
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		
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
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

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

	}	
	
	
	public void initialize() {
		CreditCard.mLogger.info("Inside PL PROCESS initialize()" );
		

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info("Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		CustomSaveForm();

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info("Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info("Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info("Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
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
				LoadView(pEvent.getSource().getName());
			loadPicklistCustomer();

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			 */
			formObject.setLocked("Product_Frame1",true);
			/*formObject.setEnabled("Product_Save",true);
				formObject.setEnabled("Product_Add",true);
				formObject.setEnabled("Product_Modify",true);
				formObject.setEnabled("Product_Delete",true);*/
		}

		//added by yash for CC FSD
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			disableButtonsCC("IncomeDetails");
			loadpicklist_Income();
		}
		//added by yash for CC FSD

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			/*formObject.setEnabled("Liability_New_AECBReport",true);
				formObject.setEnabled("Liability_New_Save",true);*/
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		//	formObject.setLocked("EMploymentDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			//loadPicklistEmployment();
			loadPicklist4();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			/*formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);*/
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			LoadView(pEvent.getSource().getName());
		//	formObject.setLocked("AddressDetails_Frame1",true);
			//loadPicklist_Address();
			/*formObject.setEnabled("AddressDetails_Save",true);
				formObject.setEnabled("AddressDetails_addr_Add",true);
				formObject.setEnabled("AddressDetails_addr_Modify",true);
				formObject.setEnabled("AddressDetails_addr_Delete",true);*/
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
		//	LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			LoadView(pEvent.getSource().getName());
		//	formObject.setLocked("AltContactDetails_Frame1",true);
			/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
		} 
		//added by yash for CC FSD
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("FATCA_Frame6",true);
			/*formObject.setEnabled("FATCA_Save",true);*/
		}
		//added by yash for CC FSD
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("KYC_Frame1",true);
			/*formObject.setEnabled("KYC_Save",true);*/
		}
		//added by yash for CC FSD
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadView(pEvent.getSource().getName());
			//formObject.setLocked("OECD_Frame8",true);
			/*formObject.setEnabled("OECD_Save",true);*/
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){

			//formObject.setLocked("IncomingDocument_Frame",false);

		}
		// added by yash CC FSD
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();

		}
		//added by yash for CC FSD
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);

		}
		//added by yash for CC FSD
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);

		}
		//added by yash for CC FSD
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CardDetails_Frame1",true);

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
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");*/
			loadPicklist_CompanyDet();
		}
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			// formObject.setLocked("AuthorisedSignDetails_Button4", true);
			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}
		
		//added by yash on 30/8/2017
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			notepad_withoutTelLog();
			formObject.setLocked("NotepadDetails_Frame2",true);
			formObject.setVisible("NotepadDetails_SaveButton",true);
			formObject.setLocked("NotepadDetails_SaveButton",true);
			formObject.setTop("NotepadDetails_savebutton",410);

		}
		//added by yash for CC FSD
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

			/*formObject.setVisible("DecisionHistory_CheckBox1",false);
			formObject.setVisible("DecisionHistory_Label1",false);
			formObject.setVisible("cmplx_DEC_VerificationRequired",true);
			formObject.setVisible("DecisionHistory_Label3",false);
			formObject.setVisible("DecisionHistory_Combo3",false);
			formObject.setVisible("DecisionHistory_Label6",true);
			formObject.setVisible("DecisionHistory_Combo6",false);
			formObject.setVisible("DecisionHistory_Decision_Label4",false);
			formObject.setVisible("cmplx_DEC_Remarks",false);
			formObject.setVisible("DecisionHistory_Label8",false);
			formObject.setVisible("DecisionHistory_Text4",false);
			formObject.setVisible("DecisionHistory_Label7",true);
			formObject.setVisible("DecisionHistory_Text3",false);
			formObject.setVisible("DecisionHistory_Text2",false);
			formObject.setVisible("cmplx_DEC_ReferReason",false);
			formObject.setVisible("cmplx_DEC_Description",false);
			formObject.setVisible("cmplx_DEC_Strength",false);
			formObject.setVisible("cmplx_DEC_Weakness",false);
			formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
			formObject.setVisible("DecisionHistory_Text15",false);
			formObject.setVisible("DecisionHistory_Text16",false);
			formObject.setVisible("DecisionHistory_Text17",true);
			formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setVisible("DecisionHistory_Label27",false);
			formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
			formObject.setVisible("DecisionHistory_Label4",false);
			formObject.setVisible("DecisionHistory_Label5",false);
			formObject.setVisible("DecisionHistory_Label4",false);
			//formObject.setVisible("DecisionHistory_Label4",false);
			formObject.setVisible("cmplx_DEC_ChequebookRef",false);	
			formObject.setVisible("cmplx_DEC_DCR_Refno",false);	
			formObject.setVisible("DecisionHistory_Label9",false);	
			formObject.setVisible("DecisionHistory_Text17",false);	
			formObject.setVisible("DecisionHistory_Text17",false);
			formObject.setTop("DecisionHistory_Label10",7);
			formObject.setLeft("DecisionHistory_Label10",24);
			formObject.setTop("cmplx_DEC_New_CIFID",23);
			formObject.setLeft("cmplx_DEC_New_CIFID",24);
			formObject.setTop("DecisionHistory_Label7",7);
			formObject.setLeft("DecisionHistory_Label7",297);
			formObject.setTop("cmplx_DEC_NewAccNo",23);
			formObject.setLeft("cmplx_DEC_NewAccNo",297);
			formObject.setTop("DecisionHistory_Label6",7);
			formObject.setLeft("DecisionHistory_Label6",562);
			formObject.setTop("cmplx_DEC_IBAN_No",23);
			formObject.setLeft("cmplx_DEC_IBAN_No",562);
			//formObject.setEnabled("cmplx_DEC_IBAN_No",false); //Arun (07/10)
			formObject.setEnabled("DecisionHistory_Button2",false);
			formObject.setVisible("DecisionHistory_Button2",true);
			formObject.setVisible("DecisionHistory_Button4",false);
			formObject.setVisible("DecisionHistory_Label11",true);
			formObject.setVisible("DecisionHistory_dec_reason_code",true);
			formObject.setVisible("DecisionHistory_Decision_Label4",true); 
			formObject.setVisible("cmplx_DEC_Remarks",true);
			formObject.setVisible("DecisionHistory_Decision_Label3",true);
			//formObject.setVisible("DecisionHistory_dec_reason_code",true);
			formObject.setVisible("DecisionHistory_save",true);
			formObject.setVisible("DecisionHistory_Decision_ListView1",true);
			formObject.setTop("DecisionHistory_Button2",7);
			formObject.setLeft("DecisionHistory_Button2",820);
			formObject.setTop("DecisionHistory_Decision_Label1",7);
			formObject.setLeft("DecisionHistory_Decision_Label1",1045);
			formObject.setTop("cmplx_DEC_VerificationRequired",23);
			formObject.setLeft("cmplx_DEC_VerificationRequired",1045);
			formObject.setTop("DecisionHistory_Decision_Label3",85);
			formObject.setLeft("DecisionHistory_Decision_Label3",24);
			//formObject.setTop("DecisionHistory_Decision_Label3",95);
			//formObject.setLeft("DecisionHistory_Decision_Label3",24);
			formObject.setTop("cmplx_DEC_Decision",97);
			formObject.setLeft("cmplx_DEC_Decision",24);
			formObject.setTop("DecisionHistory_Label11",85);
			formObject.setLeft("DecisionHistory_Label11",297);
			formObject.setTop("DecisionHistory_dec_reason_code",97);
			formObject.setLeft("DecisionHistory_dec_reason_code",297);
			formObject.setTop("DecisionHistory_Decision_Label4",97);
			formObject.setLeft("DecisionHistory_Decision_Label4",562);
			formObject.setTop("cmplx_DEC_Remarks",119);
			formObject.setLeft("cmplx_DEC_Remarks",562);
			formObject.setTop("cmplx_DEC_Remarks",109);
			formObject.setLeft("cmplx_DEC_Remarks",562);
			formObject.setTop("DecisionHistory_save",165);
			formObject.setLeft("DecisionHistory_save",24);
			formObject.setTop("DecisionHistory_Decision_ListView1",215);*/
			//formObject.setLeft("DecisionHistory_Decision_ListView1",24);
			  fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			loadPicklist3();
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

