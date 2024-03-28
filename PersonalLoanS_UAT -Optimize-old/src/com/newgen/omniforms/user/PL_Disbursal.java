
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

			if(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true")){
				formObject.setNGValue("Customer_Type","NEP new");
			}
			else if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
				formObject.setNGValue("Customer_Type","NTB new");
			}
			else{
				formObject.setNGValue("Customer_Type","Existing");
			}
			mLogger.info("PL Initiation"+ "value of los:"+formObject.getNGValue("Customer_Type"));

			String Current_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
			String Previous_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious");

			String JobDuration = Current_LOS+" "+Previous_LOS; 
			formObject.setNGValue("Emp_JobDuration",JobDuration); 
			mLogger.info("PL Initiation"+ "value of los:"+formObject.getNGValue("Emp_JobDuration"));

		}catch(Exception e)
		{
			mLogger.info("PL Initiation"+ "Exception:"+e.getMessage());
			printException(e);
		}
	}

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

		}*/
		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			//setDisabled();
			formObject.setLocked("Customer_Frame1",true);
		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n>0){
				for(int i=0;i<n;i++)
				{
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("Limit Increase"))
					{
						//mLogger.info(" In PL_Initiation eventDispatched()"+ "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
						formObject.setVisible("cmplx_LimitInc_CurrentLimit", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
						formObject.setVisible("cmplx_LimitInc_LimitExpiryDate_button", false);
						//formObject.setVisible("DecisionHistory_updcust", true);
						//mLogger.info(" In PL_Initiation eventDispatched()"+ "after making buttons visible");

					}
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("Product Upgrade"))
					{
						formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
					}
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("Product Upgrade with Limit inc"))
						//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("RESIDENCE"))
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

		else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {

			formObject.setLocked("IncomeDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {

			formObject.setLocked("ExtLiability_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			loadPicklistELigibiltyAndProductInfo();
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {

			formObject.setLocked("LoanDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {

			formObject.setLocked("AltContactDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {

			formObject.setLocked("FATCA_Frame6",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {

			formObject.setLocked("KYC_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {

			formObject.setLocked("OECD_Frame8",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {

			formObject.setLocked("PartMatch_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {

			formObject.setLocked("CustDetailVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {

			formObject.setLocked("BussinessVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {

			formObject.setLocked("HomeCountryVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {

			formObject.setLocked("ResidenceVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification")) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {

			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {

			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}
		// disha FSD
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

			formObject.setLocked("NotepadDetails_Frame1",true);

			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info("PL notepad "+ "Activity name is:" + sActivityName);
			int user_id = formObject.getUserId();
			String user_name = formObject.getUserName();
			user_name = user_name+"-"+user_id;					
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
			formObject.setNGValue("NotepadDetails_Actusername",user_name); 
			formObject.setNGValue("NotepadDetails_user",user_name); 
			formObject.setLocked("NotepadDetails_noteDate",true);
			formObject.setLocked("NotepadDetails_Actusername",true);
			formObject.setLocked("NotepadDetails_user",true);
			formObject.setLocked("NotepadDetails_insqueue",true);
			formObject.setLocked("NotepadDetails_Actdate",true);
			formObject.setLocked("NotepadDetails_notecode",true);
			formObject.setVisible("NotepadDetails_save",true);

			formObject.setHeight("NotepadDetails_Frame1",450);
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {

			formObject.setLocked("SmartCheck_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {

			formObject.setLocked("Compliance_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FCU_Decision")) {

			formObject.setLocked("FCU_Decision_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			loadPicklist1();
			if(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq").equalsIgnoreCase("false")){
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
		mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			mLogger.info(" In PL_Iniation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}

