/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_DDVT_Checker.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.util.HashMap;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_DDVT_Checker extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info( "Exception:"+e.getMessage());
			printException(e);
		}
	}

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		mLogger.info("In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

		}*/
		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			//setDisabled();
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();								
		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			formObject.setLocked("Product_Frame1",true);
			loadPicklistProduct("Personal Loan");
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			formObject.setLocked("IncomeDetails_Frame1",true);

			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			formObject.setLocked("ExtLiability_Frame1",true);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
			formObject.setLocked("EMploymentDetails_Frame1",true);

			formObject.setVisible("EMploymentDetails_Label36",false);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);

			loadPicklist4();
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
			formObject.setLocked("cmplx_MiscFields_School",true);
			formObject.setLocked("cmplx_MiscFields_PropertyType",true);
			formObject.setLocked("cmplx_MiscFields_RealEstate",true);
			formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);

			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
			LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' union select convert(varchar, description) from NG_MASTER_instrumentType");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");

			formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
			formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);

			/*formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_Insurance",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_NumberOfInstallment",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BAseRate",true);	
				formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);*/

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
			formObject.setLocked("LoanDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
			formObject.setVisible("AlternateContactDetails_custdomicile",false);
			formObject.setVisible("AltContactDetails_Label14",false);
			formObject.setLocked("AltContactDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {

			formObject.setLocked("ReferenceDetails_Frame1",true);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
			formObject.setLocked("FATCA_Frame6",true);
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {

			formObject.setLocked("KYC_Frame1",true);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
			LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
			LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			formObject.setLocked("OECD_Frame8",true);
		}
		// disha FSD
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

			//formObject.setLocked("NotepadDetails_Frame1",true);

			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info("PL notepad-->Activity name is:" + sActivityName);
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
			formObject.setVisible("NotepadDetails_save",true);
			formObject.setLocked("NotepadDetails_notecode",true);

			//formObject.setHeight("NotepadDetails_Frame1",450);//Arun (23/09/17)
			formObject.setTop("NotepadDetails_save",440);
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");


		}
		//Update Customer call(Tanshu Aggarwal-29/05/2017) 
		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {

			formObject.setVisible("cmplx_Decision_waiveoffver",false);  // disha FSD
			formObject.setVisible("DecisionHistory_Button3",true);
			formObject.setVisible("DecisionHistory_updcust",true);
			formObject.setVisible("DecisionHistory_Rejreason",false);
			formObject.setVisible("cmplx_Decision_rejreason",false);
			formObject.setVisible("DecisionHistory_Button1",false);								
			formObject.setVisible("DecisionHistory_Label6",true);
			formObject.setVisible("cmplx_Decision_IBAN",true);
			formObject.setVisible("DecisionHistory_Label7",true);
			formObject.setVisible("cmplx_Decision_AccountNo",true);
			formObject.setVisible("DecisionHistory_Label8",true);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber",true);
			formObject.setVisible("DecisionHistory_Label9",true);
			formObject.setVisible("cmplx_Decision_DebitcardNumber",true);

			formObject.setLeft("cmplx_Decision_waiveoffver",24);
			formObject.setLeft("DecisionHistory_Button3",250);
			formObject.setLeft("DecisionHistory_updcust",390);
			formObject.setLeft("DecisionHistory_chqbook",560);
			formObject.setLeft("Decision_Label1",24);
			formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED",24);
			formObject.setLeft("Decision_Label3",297);
			formObject.setLeft("cmplx_Decision_Decision",297);
			formObject.setLeft("DecisionHistory_Label1",555);
			formObject.setLeft("cmplx_Decision_refereason",555);								
			formObject.setLeft("DecisionHistory_Label6",24);
			formObject.setLeft("cmplx_Decision_IBAN",24);
			formObject.setLeft("DecisionHistory_Label7",297);
			formObject.setLeft("cmplx_Decision_AccountNo",297);
			formObject.setLeft("DecisionHistory_Label8",555);
			formObject.setLeft("cmplx_Decision_ChequeBookNumber",555);
			formObject.setLeft("DecisionHistory_Label9",813);
			formObject.setLeft("cmplx_Decision_DebitcardNumber",813);

			formObject.setTop("cmplx_Decision_waiveoffver",10);
			formObject.setTop("DecisionHistory_Button3",10);
			formObject.setTop("DecisionHistory_updcust",10);
			formObject.setTop("DecisionHistory_chqbook",10);
			formObject.setTop("Decision_Label1",60);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",76);
			formObject.setTop("Decision_Label3",60);
			formObject.setTop("cmplx_Decision_Decision",76);
			formObject.setTop("DecisionHistory_Label1",60);
			formObject.setTop("cmplx_Decision_refereason",76);								
			formObject.setTop("DecisionHistory_Label6",110);
			formObject.setTop("cmplx_Decision_IBAN",126);
			formObject.setTop("DecisionHistory_Label7",110);
			formObject.setTop("cmplx_Decision_AccountNo",126);
			formObject.setTop("DecisionHistory_Label8",110);
			formObject.setTop("cmplx_Decision_ChequeBookNumber",126);
			formObject.setTop("DecisionHistory_Label9",110);
			formObject.setTop("cmplx_Decision_DebitcardNumber",126);								
			formObject.setTop("DecisionHistory_Label5",168);
			formObject.setTop("cmplx_Decision_desc",184);
			formObject.setTop("DecisionHistory_Label3",168);
			formObject.setTop("cmplx_Decision_strength",184);
			formObject.setTop("DecisionHistory_Label4",168);
			formObject.setTop("cmplx_Decision_weakness",184);
			formObject.setTop("Decision_Label4",168);
			formObject.setTop("cmplx_Decision_REMARKS",184);
			formObject.setTop("Decision_ListView1",270);
			formObject.setTop("DecisionHistory_save",460);  

			String retainCheckboxValue = formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq");
			mLogger.info("value of retain checkbox before if is: "+retainCheckboxValue);
			if(retainCheckboxValue != null){// || retainCheckboxValue.equalsIgnoreCase("null")){
				//Fragment not opened.
				mLogger.info("inside else of retain checkbox");
			}
			else{

				mLogger.info("inside if of retain checkbbox");
				int framestate=formObject.getNGFrameState("Alt_Contact_container");
				mLogger.info("framestate is: "+framestate);
				if(framestate == 1){
					mLogger.info("PL_DDVT_CHECKER alternate contact details framestate is 1");
					//formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					//formObject.setNGFrameState("Alt_Contact_container", 0);
					new PersonalLoanSCommonCode().alignfragmentsafterfetch(formObject);

				}

			}
			if(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq").equalsIgnoreCase("true")){
				mLogger.info( "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
				//formObject.setVisible("DecisionHistory_Button3", true); //Arun(12/09/17)
				//formObject.setVisible("DecisionHistory_updcust", true); //Arun(12/09/17)
				//formObject.setVisible("DecisionHistory_chqbook",true); //Arun(12/09/17)
				mLogger.info( "after making buttons visible");
			}
			else
			{
				//formObject.setVisible("DecisionHistory_Button3", false); //Arun(12/09/17)
				//formObject.setVisible("DecisionHistory_updcust", false); //Arun(12/09/17)
				//formObject.setVisible("DecisionHistory_chqbook",false); //Arun(12/09/17)
				mLogger.info( "after making buttons invisible");
			}

			if(formObject.getNGValue("InitiationType").equalsIgnoreCase("Reschedulment"))
			{
				//loadPicklist3();
				loadPicklist1();
			}
			else
			{
				loadPicklistChecker();
			}
			//formObject.setLocked("DecisionHistory_Frame1",true);

		} 	
		//Update Customer call(Tanshu Aggarwal-29/05/2017)  	

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferHistory")) {
			if(formObject.getNGValue("decision").contains("Compliance")|| formObject.getNGValue("decision").contains("FCU"))
				AddInReferGrid();
		} //Added By Akshay for Multiple Refer	


	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());



		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			mLogger.info(" In PL_DDVT Checker eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
			break;

		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;

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
		//changes by saurabh on 20th july 17.
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}

