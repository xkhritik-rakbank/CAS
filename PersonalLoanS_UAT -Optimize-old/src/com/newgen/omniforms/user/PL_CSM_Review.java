/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CSM_Review.java
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


public class PL_CSM_Review extends PLCommon implements FormListener
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
			mLogger.info("Exception:"+e.getMessage());
			printException(e);
		}
	}

	public void fragment_loaded(ComponentEvent pEvent)
	{
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
		}

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

		else if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {	

			formObject.setLocked("Compliance_Frame1",true);
			formObject.setLocked("Compliance_CompRemarks",false);			            
			formObject.setLocked("Compliance_Modify",false); 
			formObject.setLocked("Compliance_Save",false);
			formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);

			/*LoadPickList("cmplx_Compliance_EMpType", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_MASTER_EmploymentType with (nolock) order by code");
                LoadPickList("cmplx_Compliance_CustType", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_CustomerType with (nolock) order by code");
                LoadPickList("cmplx_Compliance_BussSegment", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_BusinessSegment with (nolock) order by code");
                LoadPickList("cmplx_Compliance_subSegment", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_SubSegment with (nolock) order by code");
                LoadPickList("cmplx_Compliance_demographic", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Demographics with (nolock) order by code");
                LoadPickList("cmplx_Compliance_industry", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Industry with (nolock) order by code");
                LoadPickList("cmplx_Compliance_nationality_companyDomicile", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Nationality with (nolock) order by code");
                LoadPickList("cmplx_Compliance_product", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_product with (nolock) order by code");
                LoadPickList("cmplx_Compliance_currency", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Currency with (nolock) order by code");
                LoadPickList("cmplx_Compliance_custCategory", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_CustomerCategory with (nolock) order by code");
                LoadPickList("cmplx_Compliance_ExposedPerson", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_PoliticalExposed with (nolock) order by code");*/


			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");

			formObject.setNGFrameState("World_Check",0);
			int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
			mLogger.info("CC value of world check row--->value of n "+n);
			if(n>0)
			{ 
				String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,12);
				// String UID =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),12);
				mLogger.info("CC value of world check UID"+UID);
				formObject.setNGValue("Compliance_UID",UID);

				String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,15);
				// String EI =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),15);
				mLogger.info("CC value of world check EI "+EI);
				formObject.setNGValue("Compliance_EI",EI);
				// String Name =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),0);
				String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,0);
				mLogger.info("CC value of world check Name"+Name);	
				formObject.setNGValue("Compliance_Name",Name);
				//String Dob =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),2);
				String Dob = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,2);
				mLogger.info("CC value of world check Dob"+Dob);
				formObject.setNGValue("Compliance_DOB",Dob);
				String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,7);
				// String Citizenship =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),7);
				mLogger.info("Citizenship"+Citizenship);	
				formObject.setNGValue("Compliance_Citizenship",Citizenship);
				String Remarks = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,16);
				// String Remarks =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),16);
				formObject.setNGValue("Compliance_Remarks",Remarks);
				String Id_No = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,1);
				formObject.setNGValue("Compliance_IdentificationNumber",Id_No);
				//String Id_No =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),1);
				String Age = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,3);
				formObject.setNGValue("Compliance_Age",Age);
				//String Age =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),3);

				String Position = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,4);
				formObject.setNGValue("Compliance_Positon",Position);
				//String Position =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),4);
				String Deceased = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,5); 
				formObject.setNGValue("Compliance_Deceased",Deceased);
				//String Deceased =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),5);
				String Category = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,6);
				formObject.setNGValue("Compliance_Category",Category);
				//String Category =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),6);
				String Location = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,8);
				formObject.setNGValue("Compliance_Location",Location);
				//String Location =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),8);
				String Identification = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,9); 
				formObject.setNGValue("Compliance_Identification",Identification);
				//String Identification =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),9);
				String Biography = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,10); 
				formObject.setNGValue("Compliance_Biography",Biography);
				//String Biography =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),10);
				String Reports = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,11); 
				formObject.setNGValue("Compliance_Reports",Reports);
				//String Reports =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),11);
				String Entered_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,13);
				formObject.setNGValue("Compliance_EntertedDate",Entered_Date);
				//String Entered_Date =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),13);
				String Updated_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,14);
				formObject.setNGValue("Compliance_UpdatedDate",Updated_Date);
				//String Updated_Date =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),14);
				//String Match_Found = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,17); 

				//String Match_Found =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),17);
				String Document = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,18);  
				formObject.setNGValue("Compliance_Document",Document);
				//String Document =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),18);
				String Decision = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,19);
				formObject.setNGValue("Compliance_Decision",Decision);
				//String Decision =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),19);
				String Match_Rank = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,20);
				formObject.setNGValue("Compliance_MatchRank",Match_Rank);
				//String Match_Rank =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),20);
				String Alias = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,21);
				formObject.setNGValue("Compliance_Alias",Alias);
				//String Alias =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),21);
				String birth_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,22);
				formObject.setNGValue("Compliance_BirthCountry",birth_Country);
				//String birth_Country =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),22);
				String resident_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,23);
				formObject.setNGValue("Compliance_ResidenceCountry",resident_Country);
				//String resident_Country =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),23);
				String Address = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,24);
				formObject.setNGValue("Compliance_Address",Address);
				//String Address =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),24);
				String Gender = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,25);
				formObject.setNGValue("Compliance_Gender",Gender);
				//String Gender =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),25);
				String Listed_On = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,26);
				formObject.setNGValue("Compliance_ListedOn",Listed_On);
				//String Listed_On =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),26);
				String Program = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,27);
				formObject.setNGValue("Compliance_Program",Program);
				//String Program =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),27);
				String external_ID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,28);
				formObject.setNGValue("Compliance_ExternalID",external_ID);
				//String external_ID =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),28);
				String data_Source = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,29);
				//String data_Source =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),29);
				formObject.setNGValue("Compliance_DataSource",data_Source);

				formObject.setNGValue("Compliance_winame",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");

				formObject.RaiseEvent("WFSave");
			}
			formObject.setNGFrameState("World_Check",1);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			loadPicklist1();

			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setVisible("Decision_Label1",false);
			formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
			formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setVisible("DecisionHistory_Label1",false);
			formObject.setVisible("cmplx_Decision_refereason",false);
			formObject.setVisible("DecisionHistory_Label6",false);
			formObject.setVisible("cmplx_Decision_IBAN",false);
			formObject.setVisible("DecisionHistory_Label7",false);
			formObject.setVisible("cmplx_Decision_AccountNo",false);
			formObject.setVisible("DecisionHistory_Label8",false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
			formObject.setVisible("DecisionHistory_Label9",false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber",false);
			formObject.setVisible("DecisionHistory_Label5",false);
			formObject.setVisible("cmplx_Decision_desc",false);
			formObject.setVisible("DecisionHistory_Label3",false);
			formObject.setVisible("cmplx_Decision_strength",false);
			formObject.setVisible("DecisionHistory_Label4",false);
			formObject.setVisible("cmplx_Decision_weakness",false);

			formObject.setTop("Decision_Label3",7);
			formObject.setTop("cmplx_Decision_Decision",22);							
			formObject.setTop("Decision_Label4",7);
			formObject.setTop("cmplx_Decision_REMARKS",22);
			//formObject.setTop("DecisionHistory_Label11",7);
			//formObject.setTop("cmplx_Decision_Decreasoncode",22);
			formObject.setTop("Decision_ListView1",96);							
			formObject.setTop("DecisionHistory_save",288);

			formObject.setLeft("Decision_Label4",555);
			formObject.setLeft("cmplx_Decision_REMARKS",555);	
			formObject.setLeft("DecisionHistory_Label11",297);
			formObject.setLeft("cmplx_Decision_Decreasoncode",297);

			//formObject.setVisible("DecisionHistory_Modify", true);
			//formObject.setVisible("cmplx_Decision_Status", true);
			//formObject.setVisible("DecisionHistory_Label2", true);

			//Common function for decision fragment textboxes and combo visibility
			//decisionLabelsVisibility();
		} 	
		// disha FSD
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

			//formObject.setLocked("NotepadDetails_Frame1",true);
			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info("PL notepad--->Activity name is:" + sActivityName);
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


	}


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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

