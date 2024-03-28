
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Post Disbursal

File Name                                                         : CC_Post_Disbursal

Author                                                            : Disha

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

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


public class CC_Post_Disbursal extends CC_Common implements FormListener
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());


		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		formObject.setVisible("PostDisbursal",true);
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
			CreditCard.mLogger.info("Inside CC_PostDisbursal CC");
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
		//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		switch(pEvent.getType()) {

		case FRAME_EXPANDED:
			//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			new CC_CommonCode().FrameExpandEvent(pEvent);						


			break;

		case FRAGMENT_LOADED:
			// CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			fragment_loaded(pEvent,formObject);


			break;

		case MOUSE_CLICKED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		CreditCard.mLogger.info( "Inside PL PROCESS initialize()" );


	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());


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
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
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
		try{
			CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
			//++ below code added by Deepak on 19/03/2018 for EFMS refresh functionality
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Submit_Desc").equalsIgnoreCase(formObject.getNGValue("Decision"))){
				List<String> objInput=new ArrayList<String>();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:"+"Approve");
				CreditCard.mLogger.info("objInput args are: "+objInput.get(0));
				List<Object> objOutput=new ArrayList<Object>();

				objOutput.add("Text");


				objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);

			}
			//++ above code added by Deepak on 19/03/2018 for EFMS refresh functionality
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
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		try{
			CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			saveIndecisionGrid();
			CustomSaveForm();
			//formObject.setNGValue("cmplx_DEC_Remarks","");
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormStarted"+e.getStackTrace());
		}
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
		//	formObject.setLocked("Customer_Frame1",true);
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("Customer_Frame1",true);
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

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			loadpicklist_Income();
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			/*formObject.setEnabled("Liability_New_AECBReport",true);
				formObject.setEnabled("Liability_New_Save",true);*/
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("EMploymentDetails_Frame1",true);
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
		//	formObject.setLocked("AddressDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			//loadPicklist_Address();
			/*formObject.setEnabled("AddressDetails_Save",true);
				formObject.setEnabled("AddressDetails_addr_Add",true);
				formObject.setEnabled("AddressDetails_addr_Modify",true);
				formObject.setEnabled("AddressDetails_addr_Delete",true);*/
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			//change by saurabh for air arabia functionality.
			AirArabiaValid();
			enrollrewardvalid();//added by saurabh1 for enroll
		//	formObject.setLocked("AltContactDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("FATCA_Frame1",true);
			/*formObject.setEnabled("FATCA_Save",true);*/
			loadPicklist_Fatca();
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("KYC_Frame1",true);
			/*formObject.setEnabled("KYC_Save",true);*/
			loadPicklist_KYC();
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadView(pEvent.getSource().getName());
		//	formObject.setLocked("OECD_Frame1",true);
			/*formObject.setEnabled("OECD_Save",true);*/
			loadPicklist_oecd();
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("IncomingDocument_Frame",true);
			/*formObject.setEnabled("OECD_Save",true);*/
		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);

		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			formObject.setEnabled("SupplementCardDetails_Frame1", false);
			loadPicklist_suppCard();
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CardDetails_Frame1",true);
			//LoadPickList("CardDetails_BankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
			formObject.setVisible("CardDetails_Label13", false);
			formObject.setVisible("cmplx_CardDetails_CustClassification", false);
		}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CompanyDetails_Frame1", true);
			loadPicklist_CompanyDet();
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			loadPicklist_Mol();
			formObject.setLocked("MOL1_Frame1",true);
			formObject.setLocked("cmplx_MOL_molexp",true);
			formObject.setLocked("cmplx_MOL_molissue",true);
			formObject.setLocked("cmplx_MOL_ctrctstart",true);
			formObject.setLocked("cmplx_MOL_ctrctend",true);

		}

		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			formObject.setEnabled("CC_Loan_Frame1",false);
			loadPicklist_ServiceRequest();

		}

		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			// formObject.setLocked("AuthorisedSignDetails_Button4", true);
			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("PartnerDetails_signcapacity", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SigningCapacity with (nolock) where isActive='Y' order by code");
		}


		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

			fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017	
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

