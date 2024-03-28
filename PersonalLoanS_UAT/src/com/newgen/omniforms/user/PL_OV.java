/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_OV.java
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;


import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.excp.CustomExceptionHandler;








import javax.faces.application.*;

public class PL_OV extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL_OV");
		PL_SKLogger.writeLog("PL_OV Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
            new PersonalLoanSCommonCode().setFormHeader(pEvent);
        }catch(Exception e)
        {
            PL_SKLogger.writeLog("RLOS Initiation", "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
					
						break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
							//setDisabled();
							formObject.setLocked("Customer_Frame1",true);
						}	

						if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
							formObject.setLocked("Product_Frame1",true);
							LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
	
							formObject.setLocked("GuarantorDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
	
							formObject.setLocked("IncomeDetails_Frame1",true);
						} 

						if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
	
							formObject.setLocked("ExtLiability_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
	
							formObject.setLocked("EMploymentDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
	
							formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
	
							formObject.setLocked("LoanDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
							loadPicklist_Address();
							formObject.setLocked("AddressDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
	
							formObject.setLocked("AltContactDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
	
							formObject.setLocked("CardDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
	
							formObject.setLocked("ReferenceDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
	
							formObject.setLocked("SupplementCardDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
	
							formObject.setLocked("FATCA_Frame6",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
	
							formObject.setLocked("KYC_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
							
							formObject.setLocked("OECD_Frame8",true);
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
							
							formObject.setVisible("PostDisbursal",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
							loadPicklist1();
							//formObject.setVisible("DecisionHistory_Modify", true);
							//formObject.setVisible("cmplx_Decision_Status", true);
							//formObject.setVisible("DecisionHistory_Label2", true);
							formObject.setTop("Decision_ListView1",250);
							formObject.setTop("DecisionHistory_save",450);
							formObject.setTop("DecisionHistory_Label1",104);
							formObject.setTop("cmplx_Decision_refereason",120);
							formObject.setLeft("Decision_Label1", 297);
							formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 297);
							formObject.setTop("Decision_Label1",7);
							formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",22);
							//Common function for decision fragment textboxes and combo visibility
							//decisionLabelsVisibility();
							loadPicklist3();
						}	
						// disha FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
							
				 			formObject.setLocked("NotepadDetails_Frame1",true);
				 			formObject.setVisible("NotepadDetails_Frame3",false);
							String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
							PL_SKLogger.writeLog("PL notepad ", "Activity name is:" + sActivityName);
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
					
					
					  break;
					  
					case MOUSE_CLICKED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
							//GenerateXML();
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
							formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

						}

					
						if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
							PL_SKLogger.writeLog("PL_Initiation", "Inside Customer_save button: ");
							formObject.saveFragment("CustomerDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
							formObject.saveFragment("ProductContainer");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
							formObject.saveFragment("GuarantorDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
							formObject.saveFragment("IncomeDEtails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
							formObject.saveFragment("Incomedetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
							formObject.saveFragment("CompanyDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Save")){
							formObject.saveFragment("PartnerDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
							formObject.saveFragment("Liability_container");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
							formObject.saveFragment("InternalExternalContainer");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
							formObject.saveFragment("EmploymentDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
							formObject.saveFragment("EligibilityAndProductInformation");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
							formObject.saveFragment("MiscFields");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
							formObject.saveFragment("Address_Details_container");
						}
						// disha FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
							formObject.saveFragment("Alt_Contact_container");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
							formObject.saveFragment("Supplementary_Container");
						}
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
							formObject.saveFragment("FATCA");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
							formObject.saveFragment("KYC");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
							formObject.saveFragment("OECD");
						}
						
						// disha FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
							formObject.setNGValue("Notepad_wi_name",formObject.getWFWorkitemName());
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
							
							String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
							PL_SKLogger.writeLog("PL notepad ", "Activity name is:" + sActivityName);
							int user_id = formObject.getUserId();
							String user_name = formObject.getUserName();
							user_name = user_name+"-"+user_id;					
							formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
							formObject.setNGValue("NotepadDetails_Actusername",user_name,false); 
							formObject.setNGValue("NotepadDetails_user",user_name,false);
							
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							 Date date = new Date();
							 formObject.setNGValue("NotepadDetails_noteDate",dateFormat.format(date),false); 
							 formObject.setNGValue("NotepadDetails_Actdate",dateFormat.format(date),false); 

						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}
				 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

				}
					
					 case VALUE_CHANGED:
							PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
								 	  }
							 	}
							 // disha FSd
							 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
								 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
								 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
								 String sQuery = "select code from ng_master_notedescription where Description='" +  notepad_desc + "'";
								 PL_SKLogger.writeLog(" query is  NotepadDetails_notedesc ",sQuery);
								 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
								 PL_SKLogger.writeLog(" query is  recordList 12345 ",recordList.get(0).get(0));
								 
									 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
									 formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
							 }
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
		 formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		 formObject.setNGValue("ov_validation", formObject.getNGValue("Yes"));
		 
		saveIndecisionGrid();
	}

}

