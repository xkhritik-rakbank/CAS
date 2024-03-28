/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_HR.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.                 12-6-2017     Disha	       Common function for decision fragment textboxes and combo visibility
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


public class PL_HR extends PLCommon implements FormListener
{

	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("PL_HR Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("PL_HR-->"+ "Exception:"+e.getMessage());
			printException(e);

		}
	}

	public void fragment_loaded(ComponentEvent pEvent)
	{	
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();

		mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			loadPicklistCustomer();
			//formObject.setLocked("Customer_Frame1",true);
			formObject.setEnabled("Customer_save",true);
			formObject.setLocked("cmplx_Customer_ReferrorCode",false);
			formObject.setLocked("cmplx_Customer_ReferrorName",false);
			formObject.setLocked("cmplx_Customer_AppType",false);
			formObject.setVisible("cmplx_Customer_corpcode",false);
			formObject.setVisible("cmplx_Customer_bankwithus",false);
			formObject.setVisible("cmplx_Customer_noofdependent",false);
			formObject.setVisible("cmplx_Customer_guardian",false);
			formObject.setVisible("cmplx_Customer_minor",false);
			formObject.setVisible("cmplx_Customer_guarname",false);
			formObject.setVisible("cmplx_Customer_guarcif",false);
			formObject.setVisible("Customer_Label32",false);
			formObject.setVisible("Customer_Label33",false);
			formObject.setVisible("Customer_Label34",false);
			formObject.setVisible("Customer_Label37",false);
			formObject.setVisible("Customer_Label35",false);
			formObject.setEnabled("Customer_Reference_Add",true);
			formObject.setEnabled("Customer_Reference__modify",true);
			formObject.setEnabled("Customer_Reference_delete",true);

			formObject.setLocked("FetchDetails",true);
			formObject.setLocked("Customer_Button1",true);
			mLogger.info("Column Added in Repeater"+" for eida");
			if(formObject.getNGValue("cmplx_Customer_NEP").equals("true")){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
			mLogger.info("Column Added in Repeater"+" after eida");

		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			//formObject.setLocked("Product_Frame1",true);
			formObject.setEnabled("Product_Save",true);
			formObject.setEnabled("Product_Add",true);
			formObject.setEnabled("Product_Modify",true);
			formObject.setEnabled("Product_Delete",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			//formObject.setLocked("IncomeDetails_Frame2",true);
			formObject.setEnabled("IncomeDetails_Salaried_Save",true);	
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setLocked("IncomeDetails_FinacialSummarySelf",true);		

			formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);

			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
			formObject.setHeight("Incomedetails", 630);
			formObject.setHeight("IncomeDetails_Frame1", 605);                      
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			//formObject.setLocked("Liability_New_Frame1",true);
			formObject.setEnabled("Liability_New_AECBReport",true);
			formObject.setEnabled("Liability_New_Save",true);
			formObject.setLocked("ExtLiability_Button1",true);	
			formObject.setLocked("Liability_New_Button1",true);
			formObject.setLocked("cmplx_Liability_New_overrideIntLiab", true);
			formObject.setLocked("cmplx_Liability_New_overrideAECB", true);
			formObject.setVisible("Liability_New_Label6",false);
			formObject.setVisible("cmplx_Liability_New_noofpaidinstallments",false);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
			//formObject.setLocked("EMploymentDetails_Frame1",true);
			//disha FSD
			mLogger.info( "Inside value empdet" + pEvent.getSource().getName());

			String empid="AVI,MED,EDU,HOT,PROM";	
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
			//formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setVisible("EMploymentDetails_Label10",false);
			formObject.setVisible("cmplx_EmploymentDetails_marketcode",false);
			//formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",false);
			formObject.setVisible("EMploymentDetails_Label7",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
			formObject.setVisible("EMploymentDetails_Label27",false);
			formObject.setVisible("cmplx_EmploymentDetails_MIS",false);
			formObject.setVisible("EMploymentDetails_Label28",false);
			formObject.setVisible("cmplx_EmploymentDetails_collectioncode",false);
			formObject.setVisible("EMploymentDetails_Label22",false);
			formObject.setVisible("cmplx_EmploymentDetails_PromotionCode",false);

			formObject.setVisible("EMploymentDetails_Label4",false);
			formObject.setVisible("cmplx_EmploymentDetails_StaffID",false);
			formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("cmplx_EmploymentDetails_Dept",false);
			formObject.setVisible("EMploymentDetails_Label6",false);
			formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate",false);
			formObject.setVisible("EMploymentDetails_Label15",false);
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt",false);
			formObject.setVisible("EMploymentDetails_Label12",false);
			formObject.setVisible("cmplx_EmploymentDetails_categexpat",false);
			formObject.setVisible("EMploymentDetails_Label13",false);
			formObject.setVisible("cmplx_EmploymentDetails_categnational",false);
			formObject.setVisible("EMploymentDetails_Label14",false);
			formObject.setVisible("cmplx_EmploymentDetails_categcards",false);
			formObject.setVisible("EMploymentDetails_Label18",false);
			formObject.setVisible("cmplx_EmploymentDetails_ownername",false);
			formObject.setVisible("EMploymentDetails_Label9",false);
			formObject.setVisible("cmplx_EmploymentDetails_NOB",false);
			formObject.setVisible("cmplx_EmploymentDetails_accpvded",false);
			formObject.setVisible("EMploymentDetails_Label17",false);
			formObject.setVisible("cmplx_EmploymentDetails_authsigname",false);
			formObject.setVisible("cmplx_EmploymentDetails_highdelinq",false);
			formObject.setVisible("EMploymentDetails_Label20",false);
			formObject.setVisible("cmplx_EmploymentDetails_dateinPL",false);
			formObject.setVisible("EMploymentDetails_Label21",false);
			formObject.setVisible("cmplx_EmploymentDetails_dateinCC",false);
			formObject.setVisible("EMploymentDetails_Label26",false);
			formObject.setVisible("cmplx_EmploymentDetails_remarks",false);
			formObject.setVisible("EMploymentDetails_Label16",false);
			formObject.setVisible("cmplx_EmploymentDetails_Remarks_PL",false);
			formObject.setVisible("EMploymentDetails_Label11",false);												

			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			formObject.setVisible("EMploymentDetails_Label36",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
			formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			formObject.setLocked("EMploymentDetails_Button1",true);
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);

			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
			formObject.setLocked("cmplx_EmploymentDetails_Freezone",true); //Arun (26/09/17)
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true); //Arun (26/09/17)
			formObject.setTop("EMploymentDetails_Frame2",54);
			formObject.setTop("EMploymentDetails_Label71",240);
			formObject.setTop("cmplx_EmploymentDetails_EmpContractType",256);
			formObject.setTop("cmplx_EmploymentDetails_Kompass",256);
			formObject.setLeft("cmplx_EmploymentDetails_Kompass",1066);								

			formObject.setHeight("EMploymentDetails_Frame2",382);
			formObject.setTop("EMploymentDetails_Save",500);//Arun (24/09/17)
			formObject.setTop("EMploymentDetails_Button1",440);

			loadPicklist4();
			Fields_ApplicationType_Employment();

			if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("NEP")){
				formObject.setVisible("EMploymentDetails_Label25",true);
				formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
			}

			/*else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZD") || formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZE")){
					formObject.setVisible("cmplx_EmploymentDetails_Freezone",true);
					formObject.setVisible("EMploymentDetails_Label62",true);
					formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",true);
				}*/

			else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("TEN"))
			{
				formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",true);
				formObject.setVisible("EMploymentDetails_Label7",true);
			}

			else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg").equals("Surrogate") && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
				formObject.setVisible("EMploymentDetails_Label59",true);

			}
			String requested_application;
			mLogger.info("inside Employment Details of CSM");
			requested_application= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			mLogger.info("inside Employment Details of CSM"+requested_application);
			if(requested_application.equalsIgnoreCase("Reschedulment")){
				formObject.setVisible("EMploymentDetails_Label36", true);
				formObject.setTop("EMploymentDetails_Label36", 54);
				formObject.setLeft("cmplx_EmploymentDetails_channelcode", 555);
				formObject.setLeft("EMploymentDetails_Label36", 555);
				formObject.setTop("cmplx_EmploymentDetails_channelcode", 70);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode", true);
				formObject.setEnabled("cmplx_EmploymentDetails_channelcode", true);
			}
			mLogger.info("inside Channel code visibility at CSM");


		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
			//formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
			formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
			formObject.setTop("ELigibiltyAndProductInfo_Frame7",648);//Arun (24/09/17)
			formObject.setTop("ELigibiltyAndProductInfo_Save",830);//Arun (24/09/17)

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			loadPicklist_Address();
			//formObject.setLocked("AddressDetails_Frame1",true);
			formObject.setEnabled("AddressDetails_Save",true);
			formObject.setEnabled("AddressDetails_addr_Add",true);
			formObject.setEnabled("AddressDetails_addr_Modify",true);
			formObject.setEnabled("AddressDetails_addr_Delete",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){

			//formObject.setLocked("AltContactDetails_Frame1",true);
			formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
		} 

		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){

			//formObject.setLocked("FATCA_Frame1",true);
			formObject.setEnabled("FATCA_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){

			//formObject.setLocked("KYC_Frame1",true);
			formObject.setEnabled("KYC_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){

			//formObject.setLocked("OECD_Frame1",true);
			formObject.setEnabled("OECD_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc")){
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}
		//disha FSD
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

			//formObject.setLocked("NotepadDetails_Frame1",true);
			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info("PL notepad -->Activity name is:" + sActivityName);
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

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {

			formObject.setVisible("DecisionHistory_chqbook",false);
			loadPicklist1();
			//disha FSD
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			formObject.setVisible("DecisionHistory_Label8", false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
			formObject.setVisible("DecisionHistory_Label9", false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
			formObject.setVisible("DecisionHistory_Label5", false);
			formObject.setVisible("cmplx_Decision_desc", false);
			formObject.setVisible("DecisionHistory_Label3", false);
			formObject.setVisible("cmplx_Decision_strength", false);
			formObject.setVisible("DecisionHistory_Label4", false);
			formObject.setVisible("cmplx_Decision_weakness", false);

			formObject.setVisible("DecisionHistory_Button4",false);
			formObject.setVisible("cmplx_Decision_Deviationcode",false);
			formObject.setVisible("DecisionHistory_Label14",false);
			formObject.setVisible("cmplx_Decision_Dectech_decsion",false);
			formObject.setVisible("DecisionHistory_Label15",false);
			formObject.setVisible("cmplx_Decision_score_grade",false);
			formObject.setVisible("DecisionHistory_Label16",false);
			formObject.setVisible("cmplx_Decision_Highest_delegauth",false);
			formObject.setVisible("cmplx_Decision_Manual_Deviation",false);
			formObject.setVisible("DecisionHistory_Button6",false);
			formObject.setVisible("cmplx_Decision_Manual_deviation_reason",false);
			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setVisible("cmplx_Decision_refereason",false);
			formObject.setVisible("DecisionHistory_Label1",false);
			formObject.setVisible("DecisionHistory_Label26",true);
			formObject.setVisible("cmplx_Decision_AppID",true);

			formObject.setLocked("cmplx_Decision_IBAN",true);
			formObject.setLocked("cmplx_Decision_AppID",true);
			formObject.setLocked("cmplx_Decision_AccountNo",true);
			formObject.setTop("DecisionHistory_Label7", 8);
			formObject.setTop("cmplx_Decision_AccountNo", 23);
			formObject.setTop("Decision_Label3", 56);
			formObject.setTop("cmplx_Decision_Decision", 72);
			formObject.setTop("Decision_Label4", 56);
			formObject.setTop("cmplx_Decision_REMARKS", 72);

			formObject.setTop("Decision_Label4", 104);
			formObject.setTop("cmplx_Decision_REMARKS", 120);

			formObject.setTop("DecisionHistory_Label6", 8);
			formObject.setTop("cmplx_Decision_IBAN", 23);						
			formObject.setTop("DecisionHistory_Label26", 56);
			formObject.setTop("cmplx_Decision_AppID", 92);				
			formObject.setTop("Decision_ListView1", 226);		
			formObject.setTop("DecisionHistory_save", 400);	

			formObject.setTop("Decision_Label1", 8);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);

			formObject.setLeft("DecisionHistory_Label26", 1000);
			formObject.setLeft("cmplx_Decision_AppID", 1000);
			formObject.setLeft("Decision_Label4", 352);
			formObject.setLeft("cmplx_Decision_REMARKS", 352);
			formObject.setLeft("Decision_Label4", 672);
			formObject.setLeft("cmplx_Decision_REMARKS", 672);					

			formObject.setLeft("Decision_Label1", 1000);
			formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 1000);

			formObject.setLeft("Decision_Label4", 352);
			formObject.setLeft("cmplx_Decision_REMARKS", 352);

			formObject.setTop("Decision_Label4", 56);
			formObject.setTop("cmplx_Decision_REMARKS", 72);
			//Common function for decision fragment textboxes and combo visibility
			//decisionLabelsVisibility();
		} 	
		//code by saurabh gupta on 27th June 17.
		/*String hiddenFieldString = formObject.getNGValue("fields_string");
			if(hiddenFieldString!=null && !hiddenFieldString.equalsIgnoreCase("") && !hiddenFieldString.equalsIgnoreCase(" ")){
				String[] fieldNames = hiddenFieldString.split(",");
				setChangedFieldsColor(fieldNames);
			}*/

	}


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}

