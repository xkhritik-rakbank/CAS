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
		PersonalLoanS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception:"+e.getMessage());
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
		PersonalLoanS.mLogger.info("In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();								
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			loadPicklistProduct("Personal Loan");
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);

			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);

			formObject.setVisible("EMploymentDetails_Label36",false);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);

			loadPicklist4();
		}

		else if ("MiscellaneousFields".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("cmplx_MiscFields_School",true);
			formObject.setLocked("cmplx_MiscFields_PropertyType",true);
			formObject.setLocked("cmplx_MiscFields_RealEstate",true);
			formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
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

			

		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanDetails_Frame1",true);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//commented by saurabh on 10th Nov 2017.
			
			formObject.setLocked("AltContactDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);

		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame1",true);

		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
			LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			formObject.setLocked("OECD_Frame8",true);
		}
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();

			
		}
		//Update Customer call(Tanshu Aggarwal-29/05/2017) 
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

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
			formObject.setLeft("DecisionHistory_Label1",950);
			formObject.setLeft("DecisionHistory_ReferTo",950);//Arun (03-12-17) to align left
			formObject.setLeft("cmplx_Decision_refereason",555);								
			formObject.setLeft("DecisionHistory_Label6",24);
			formObject.setLeft("cmplx_Decision_IBAN",24);
			formObject.setLeft("DecisionHistory_Label7",297);
			formObject.setLeft("cmplx_Decision_AccountNo",297);
			formObject.setLeft("DecisionHistory_Label8",555);
			formObject.setLeft("cmplx_Decision_ChequeBookNumber",555);
			formObject.setLeft("DecisionHistory_Label9",813);
			formObject.setLeft("cmplx_Decision_DebitcardNumber",813);

			formObject.setTop("DecisionHistory_Label11",60); //Arun (03-12-17) to align it at correct top
			formObject.setTop("DecisionHistory_DecisionReasonCode",76); //Arun (03-12-17) to align it at correct top
			formObject.setTop("cmplx_Decision_waiveoffver",10);
			formObject.setTop("DecisionHistory_Button3",10);
			formObject.setTop("DecisionHistory_updcust",10);
			formObject.setTop("DecisionHistory_chqbook",10);
			formObject.setTop("Decision_Label1",60);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",76);
			formObject.setTop("Decision_Label3",60);
			formObject.setTop("cmplx_Decision_Decision",76);
			formObject.setTop("DecisionHistory_Label1",60);
			formObject.setTop("DecisionHistory_ReferTo",76);//Arun (01-12-17) to align top of this field
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
			// Added by aman for create cif visibility 
			if(("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))&& "".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_New_CIFNo"))) 
			{
				formObject.setVisible("DecisionHistory_Button2",true);
				formObject.setLeft("DecisionHistory_Button2",1000);
			}
			
			String retainCheckboxValue = formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq");
			PersonalLoanS.mLogger.info("value of retain checkbox before if is: "+retainCheckboxValue);
			if(retainCheckboxValue != null){
				//Fragment not opened.
				PersonalLoanS.mLogger.info("inside else of retain checkbox");
			}
			else{

				PersonalLoanS.mLogger.info("inside if of retain checkbbox");
				int framestate=formObject.getNGFrameState("Alt_Contact_container");
				PersonalLoanS.mLogger.info("framestate is: "+framestate);
				if(framestate == 1){
					PersonalLoanS.mLogger.info("PL_DDVT_CHECKER alternate contact details framestate is 1");
					
					new PersonalLoanSCommonCode().alignfragmentsafterfetch(formObject);

				}

			}
			if("true".equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))){
				PersonalLoanS.mLogger.info( "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
				
				PersonalLoanS.mLogger.info( "after making buttons visible");
			}
			else
			{
				
				PersonalLoanS.mLogger.info( "after making buttons invisible");
			}

			if("Reschedulment".equalsIgnoreCase(formObject.getNGValue("InitiationType")))
			{
				
				loadPicklist1();
			}
			else
			{
				loadPicklistChecker();
			}
			

		} 	
		//Update Customer call(Tanshu Aggarwal-29/05/2017)  	

		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(formObject.getNGValue("decision").contains("Compliance")|| formObject.getNGValue("decision").contains("FCU"))
				AddInReferGrid();
		} //Added By Akshay for Multiple Refer	


	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());



		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			PersonalLoanS.mLogger.info(" In PL_DDVT Checker eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		
		//changes by saurabh on 20th july 17.
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
		//saveIndecisionGridCSM();//Arun (01-12-17) for Decision history to appear in the grid
	}

}
