
package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_Disbursal extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("PL Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		FormReference formObject= FormContext.getCurrentInstance().getFormReference(); 
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			//added by yash
			formObject.setNGValue("cmplx_LimitInc_CIF",formObject.getNGValue("cmplx_Customer_CIFNO"));
			formObject.setNGValue("cmplx_LimitInc_CustomerName",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			//ended by yash

			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setNGValue("Customer_Type","NEP new");
			}
			else if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setNGValue("Customer_Type","NTB new");
			}
			else{
				formObject.setNGValue("Customer_Type","Existing");
			}
			PersonalLoanS.mLogger.info("PL Initiation"+ "value of los:"+formObject.getNGValue("Customer_Type"));

			String Current_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
			String Previous_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious");

			String JobDuration = Current_LOS+" "+Previous_LOS; 
			formObject.setNGValue("Emp_JobDuration",JobDuration); 
			PersonalLoanS.mLogger.info("PL Initiation"+ "value of los:"+formObject.getNGValue("Emp_JobDuration"));

		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info("PL Initiation"+ "Exception:"+e.getMessage());
			printException(e);
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		
		
		
	
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n>0){
				for(int i=0;i<n;i++)
				{
					if("Limit Increase".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2)))
					{
						
						formObject.setVisible("cmplx_LimitInc_CurrentLimit", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
						formObject.setVisible("cmplx_LimitInc_LimitExpiryDate_button", false);
						

					}
					if("Product Upgrade".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2)))
					{
						formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
					}
					if("Product Upgrade with Limit inc".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2)))
						
					{
						formObject.setVisible("cmplx_LimitInc_CurrentLimit", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
						formObject.setVisible("cmplx_LimitInc_LimitExpiryDate_button", false);
						formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);

					}	
				}

			}
		}
		// ended by yash

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("IncomeDetails_Frame1",true);
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExtLiability_Frame1",true);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			loadPicklistELigibiltyAndProductInfo();
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanDetails_Frame1",true);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("AltContactDetails_Frame1",true);
		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FATCA_Frame6",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame1",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("OECD_Frame8",true);
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CustDetailVerification_Frame1",true);
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

		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
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
		// disha FSD
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

		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FCU_Decision_Frame1",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist1();
			if("false".equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))){
				formObject.setVisible("DecisionHistory_Button3", true);
				formObject.setVisible("DecisionHistory_updcust", true);
				formObject.setVisible("DecisionHistory_chqbook", false);
			}
			else{
				formObject.setVisible("DecisionHistory_chqbook", false);
			}
		} 	
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			PersonalLoanS.mLogger.info(" In PL_Iniation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);					
			break;

		case FRAGMENT_LOADED:						
			fragment_loaded(pEvent);
			break;

		case MOUSE_CLICKED:						
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;
			//ended code merged

		case VALUE_CHANGED:
			new PersonalLoanSCommonCode().value_Change(pEvent);
			break;

		default: break;

		}


	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		
		//empty method
	}


	public void initialize() {
		
		//empty method
	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		
		//empty method
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}

