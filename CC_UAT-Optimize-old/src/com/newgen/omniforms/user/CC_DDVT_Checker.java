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

import java.util.HashMap;

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

	public void formLoaded(FormEvent pEvent)
	{

		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

		makeSheetsInvisible(tabName, "8,9,11,12,13,14,15,16,17");

	}


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
		try{

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

		formObject.setNGValue("EMPLOYMENT_DETAILS_MATCH","Yes");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_DEC_Decision"));		
		saveIndecisionGrid();
	}
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

		}*/
		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
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
		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");
			LoadPickList("cmplx_IncomeDetails_CredTurnoverFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");

			loadpicklist_Income();
		}

		//21/08/2017 it should be disabled added
		else if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
			CreditCard.mLogger.info("inside locking of reference details");
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
			CreditCard.mLogger.info("inside locking of reject enquiry");
			formObject.setLocked("RejectEnq_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
			CreditCard.mLogger.info("inside locking of Salary Enquiry");
			formObject.setLocked("SalaryEnq_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("CreditCardEnq")) {
			CreditCard.mLogger.info("inside locking of Credit Card");
			formObject.setLocked("CreditCardEnq_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
			CreditCard.mLogger.info("inside locking of LOS_Frame1");
			formObject.setLocked("LOS_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
			CreditCard.mLogger.info("inside locking of External Blacklist");
			formObject.setLocked("ExternalBlackList_Frame1",true);

		}
		//21/08/2017 it should be disabled added 
		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			formObject.setLocked("ExtLiability_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
			formObject.setLocked("EMploymentDetails_Frame1", true);
			loadPicklistEmployment();

			loadPicklist4();
			// ++ below code already present - 06-10-2017
			// added on 25septfor bug list
			formObject.setVisible("EMploymentDetails_Label33",false);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			formObject.setLocked("AddressDetails_Frame1",true);
			loadPicklist_Address();
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
			formObject.setLocked("AltContactDetails_Frame1",true);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
			formObject.setLocked("OECD_Frame8",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
			formObject.setLocked("KYC_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
			formObject.setLocked("FATCA_Frame6",true);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
			formObject.setLocked("CompanyDetails_Frame1", true);
			/*   
					LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
			 */	loadPicklist_CompanyDet();}
		else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
			formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);

			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
			formObject.setLocked("PartnerDetails_Frame1", true);
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")){

			formObject.setLocked("SalaryEnq_Frame1",true);
			/*formObject.setEnabled("OECD_Save",true);*/
		}
		//21/08/2017 it should be enabled 
		else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
			// added by abhishek as per CC FSD
			formObject.setLocked("WorldCheck1_Frame1",true);
			//formObject.setLocked("WorldCheck1_Frame1",false);
		}
		//21/08/2017 it should be enabled 
		else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
			loadPicklist_ServiceRequest();
			formObject.setLocked("CC_Loan_Frame5",true);
			formObject.setLocked("CC_Loan_Frame4",true);
			formObject.setLocked("CC_Loan_Frame2",true);
			formObject.setLocked("CC_Loan_Frame3",true);
			// added by abhishek as per CC FSD
			formObject.setLocked("CC_Loan_Frame1",true);
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")){

			formObject.setLocked("FinacleCore_Frame1",true);
			ChangeRepeater();
			ChangeRepeaterTrnover();
			/*formObject.setEnabled("OECD_Save",true);*/
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
			//formObject.setNGFrameState("ProductContainer", 0);			

			LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");

			formObject.setLocked("PartMatch_Frame1", true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
			formObject.setLocked("CardDetails_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
			formObject.setLocked("IncomingDocument_Frame1",true);

		}
		//++ Below Code added By Yash on Oct 6, 2017  to fix : "3-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
			notepad_load();
			notepad_withoutTelLog();
		}
		//ended by yash
		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {

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
                formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
                formObject.setVisible("DecisionHistory_Label11",false);

                formObject.setVisible("DecisionHistory_Button3",true);
			 */
			// ++ below code not commented at offshore - 06-10-2017
			formObject.setVisible("DecisionHistory_CheckBox1",false);
			formObject.setVisible("DecisionHistory_Label1",true);
			formObject.setVisible("cmplx_DEC_VerificationRequired",true);
			formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
			formObject.setVisible("cmplx_DEC_ContactPointVeri",true);
			formObject.setVisible("DecisionHistory_Label3",true);
			formObject.setVisible("DecisionHistory_Combo3",true);
			formObject.setVisible("DecisionHistory_Label6",true);
			formObject.setVisible("DecisionHistory_Combo6",true);
			formObject.setVisible("DecisionHistory_Decision_Label4",true);
			formObject.setVisible("cmplx_DEC_Remarks",true);
			formObject.setVisible("DecisionHistory_Label8",true);
			formObject.setVisible("DecisionHistory_Text4",true);
			formObject.setVisible("DecisionHistory_Label7",true);
			formObject.setVisible("DecisionHistory_Text3",true);
			formObject.setVisible("DecisionHistory_Label2",true);

			formObject.setVisible("DecisionHistory_Text2",true);
			formObject.setVisible("cmplx_DEC_ReferReason",true);
			formObject.setVisible("cmplx_DEC_RejectReason",true);
			// ++ above code not commented at offshore - 06-10-2017

			CreditCard.mLogger.info( "inside decision history fragment load");
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true") && formObject.getNGValue("cmplx_DEC_New_CIFID").equalsIgnoreCase("")){
				formObject.setVisible("DecisionHistory_Button2",true);	
			}
			else{
				formObject.setVisible("DecisionHistory_Button2",false);
			}
			formObject.setVisible("cmplx_DEC_Description",true);
			formObject.setVisible("cmplx_DEC_Strength",true);
			formObject.setVisible("cmplx_DEC_Weakness",true);
			// ++ below code not commented at offshore - 06-10-2017
			formObject.setVisible("DecisionHistory_chqbook",true);			

			formObject.setEnabled("DecisionHistory_Button3",false);
			//Change done to Load decisions on the basis of Initiation type 
			if(formObject.getNGValue("InitiationType").equalsIgnoreCase("Telesales_Init"))
			{
				loadPicklist3();
			}
			else
			{
				loadPicklistChecker();
			}
			//Ref. 1007.
			formObject.setVisible("cmplx_DEC_ContactPointVeri", false);
			CreditCard.mLogger.info( "1");

			formObject.setVisible("DecisionHistory_chqbook", false);
			formObject.setVisible("DecisionHistory_Label6", false);
			formObject.setVisible("cmplx_DEC_IBAN_No", false);
			formObject.setVisible("DecisionHistory_Label7", false);
			formObject.setVisible("cmplx_DEC_NewAccNo", false);
			CreditCard.mLogger.info( "2");
			formObject.setVisible("DecisionHistory_Label8", false);
			formObject.setVisible("cmplx_DEC_ChequebookRef", false);
			formObject.setVisible("DecisionHistory_Label9", false);
			formObject.setVisible("cmplx_DEC_DCR_Refno", false);
			formObject.setVisible("DecisionHistory_Label27", false);
			CreditCard.mLogger.info( "3");
			formObject.setVisible("cmplx_DEC_Cust_Contacted", false);

			formObject.setTop("DecisionHistory_Decision_ListView1",330);
			//Ref. 1007 end.
			CreditCard.mLogger.info( "end of decision history fragment load");
		} 	

		//added by abhishek as per CC FSD

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
			formObject.setLocked("FinacleCRMIncident_Frame1", true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1", true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
			formObject.setLocked("MOL1_Frame1", true);
		}
	}



}

