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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class PL_DDVT_Checker extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	String ReqProd=null;
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation RLOS");
		PL_SKLogger.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
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
		String outputResponse = "";
		String	ReturnCode="";
		String	ReturnDesc="";
		String Gender="";
		String OperDesc="";
		String buttonClickFlag="";
		String Message="";
		String alert_msg="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

			try{
				
			
		  switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					PL_SKLogger.writeLog(" In PL_DDVT Checker eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
					break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/
							if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
								//setDisabled();
								formObject.setLocked("Customer_Frame1",true);
								loadPicklistCustomer();
								
								/*if(formObject.getNGValue("cmplx_Customer_NEP")=="false"){


									formObject.setLocked("Customer_Frame1",true);
									formObject.setHeight("Customer_Frame1", 640);
									formObject.setHeight("CustomerDetails", 650);

									formObject.setLocked("Customer_save",false);
									formObject.setLocked("FetchDetails",false);
									}
									if(formObject.getNGValue("cmplx_Customer_NEP")=="true"){
										formObject.setVisible("cmplx_Customer_EIDARegNo",true);
										formObject.setVisible("Customer_Label56", true);
									}*/
									/*formObject.setVisible("cmplx_Customer_EIDARegNo",true);
									formObject.setVisible("Customer_Label56", true);*/ //Arun (12/09/17)
									//Code change Deepak for NEP
									//formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
									/*formObject.setLocked("ReadFromCard",false);*/ //Arun (12/09/17)
									//formObject.setLocked("cmplx_Customer_NEP",false);
									/*formObject.setLocked("Customer_CheckBox6",false);//CIF ID available
									formObject.setLocked("cmplx_Customer_referrorcode",false);
									formObject.setLocked("cmplx_Customer_referrorname",true);
									formObject.setLocked("cmplx_Customer_apptype",false);
									formObject.setLocked("cmplx_Customer_corpcode",false);
									formObject.setLocked("cmplx_Customer_bankwithus",false);
									formObject.setLocked("cmplx_Customer_noofdependent",false);
									formObject.setLocked("cmplx_Customer_minor",false);
									formObject.setLocked("cmplx_Customer_guarname",false);
									formObject.setLocked("cmplx_Customer_guarcif",false);
									
									
									formObject.setEnabled("Customer_ReadFromCard",true);
									formObject.setEnabled("Customer_save",true);
									formObject.setEnabled("Customer_Reference_Add",true);
									formObject.setEnabled("Customer_Reference_modify",true);
									formObject.setEnabled("Customer_Reference_delete",true);*/ //Arun (12/09/17)
									//formObject.setLocked("cmplx_Customer_CARDNOTAVAIL",false);
									//formObject.setLocked("cmplx_Customer_NEP",false);
									/*formObject.setLocked("cmplx_Customer_ReferrorCode",false);
									formObject.setLocked("cmplx_Customer_ReferrorName",false);
									formObject.setLocked("cmplx_Customer_AppType",false);
									formObject.setLocked("cmplx_Customer_corporateCode",false);
									formObject.setLocked("cmplx_Customer_Bankingwithus",false);
									formObject.setLocked("cmplx_Customer_noofDependent",false);
									formObject.setLocked("cmplx_Customer_guardian",false);
									formObject.setLocked("cmplx_Customer_minor",false);
									formObject.setLocked("cmplx_Customer_DLNo", false);
									formObject.setLocked("cmplx_Customer_PAssport2", false);
									formObject.setLocked("cmplx_Customer_Passport3", false);
									formObject.setLocked("cmplx_Customer_PASSPORT4", false);*/ //Arun (12/09/17)
			
								
							}	
							
							if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
								formObject.setLocked("Product_Frame1",true);
								loadPicklistProduct("Personal Loan");
								//LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
								//LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
								formObject.setLocked("IncomeDetails_Frame1",true);
								
								/*formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
								formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
								//formObject.setLocked("cmplx_IncomeDetails_netSal1", true);
								//formObject.setLocked("cmplx_IncomeDetails_netSal2", true);
								//formObject.setLocked("cmplx_IncomeDetails_netSal3", true);
								formObject.setLocked("cmplx_IncomeDetails_AvgNetSal", true);
								formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg", true);
								formObject.setLocked("cmplx_IncomeDetails_Commission_Avg", true);
								formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg", true);
								formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg", true);
								formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg", true);
								formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg", true);
								formObject.setLocked("cmplx_IncomeDetails_Other_Avg", true);
								formObject.setLocked("cmplx_IncomeDetails_Flying_Avg", true);*/
								LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
								LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
								LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
								LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
								formObject.setLocked("ExtLiability_Frame1",true);
								
								/*formObject.setLocked("Liability_New_fetchLiabilities",true);
								formObject.setLocked("takeoverAMount",true);
								formObject.setLocked("cmplx_Liability_New_DBR",true);
								formObject.setLocked("cmplx_Liability_New_DBRNet",true);
								formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
								formObject.setLocked("cmplx_Liability_New_TAI",true);*/
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
								formObject.setLocked("EMploymentDetails_Frame1",true);
								
								formObject.setVisible("EMploymentDetails_Label36",false);
								formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
								/*formObject.setVisible("EMploymentDetails_Label25",false);
								formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
								formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
								formObject.setVisible("EMploymentDetails_Label62",false);
								formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
								formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
								formObject.setVisible("EMploymentDetails_Label5",false);
								formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
								formObject.setVisible("EMploymentDetails_Label59",false);
								formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
								formObject.setVisible("EMploymentDetails_Label36",false);
								formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
								formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
								formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
								formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);*/
								//loadPicklist_Employment();
								loadPicklist4();
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
								formObject.setLocked("cmplx_MiscFields_School",true);
								formObject.setLocked("cmplx_MiscFields_PropertyType",true);
								formObject.setLocked("cmplx_MiscFields_RealEstate",true);
								formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
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
							
							if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
								
								formObject.setLocked("LoanDetails_Frame1",true);
								
							}

							if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
								loadPicklist_Address();
								formObject.setLocked("AddressDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
								formObject.setVisible("AlternateContactDetails_custdomicile",false);
								formObject.setVisible("AltContactDetails_Label14",false);
								formObject.setLocked("AltContactDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
								
								formObject.setLocked("ReferenceDetails_Frame1",true);
								
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
								 LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
					             LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
								 LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
								 LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
								formObject.setLocked("FATCA_Frame6",true);
								LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
								
								formObject.setLocked("KYC_Frame1",true);
								
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
								 LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
					             LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
								 LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
								 formObject.setLocked("OECD_Frame8",true);
							}
							// disha FSD
							if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
								 
								//formObject.setLocked("NotepadDetails_Frame1",true);
															
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
									formObject.setVisible("NotepadDetails_save",true);
									formObject.setLocked("NotepadDetails_notecode",true);
									
								//formObject.setHeight("NotepadDetails_Frame1",450);//Arun (23/09/17)
								formObject.setTop("NotepadDetails_save",440);
									LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
								
								
							}
							//Update Customer call(Tanshu Aggarwal-29/05/2017) 
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
								
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
								PL_SKLogger.writeLog("PL ddvt_checker","value of retain checkbox before if is: "+retainCheckboxValue);
								if(retainCheckboxValue != null){// || retainCheckboxValue.equalsIgnoreCase("null")){
									//Fragment not opened.
									PL_SKLogger.writeLog("PL ddvt_checker","inside else of retain checkbox");
								}
								else{
									
									PL_SKLogger.writeLog("PL ddvt_checker","inside if of retain checkbbox");
									int framestate=formObject.getNGFrameState("Alt_Contact_container");
									PL_SKLogger.writeLog("PL ddvt_checker","framestate is: "+framestate);
									if(framestate == 1){
										PL_SKLogger.writeLog("PL ddvt_checker","PL_DDVT_CHECKER alternate contact details framestate is 1");
										//formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
										//formObject.setNGFrameState("Alt_Contact_container", 0);
										new PersonalLoanSCommonCode().alignfragmentsafterfetch(formObject);
										
									}
									
								}
								if(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq").equalsIgnoreCase("true")){
									PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
									//formObject.setVisible("DecisionHistory_Button3", true); //Arun(12/09/17)
									//formObject.setVisible("DecisionHistory_updcust", true); //Arun(12/09/17)
									//formObject.setVisible("DecisionHistory_chqbook",true); //Arun(12/09/17)
									PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "after making buttons visible");
								}
								else
									{
									//formObject.setVisible("DecisionHistory_Button3", false); //Arun(12/09/17)
									//formObject.setVisible("DecisionHistory_updcust", false); //Arun(12/09/17)
									//formObject.setVisible("DecisionHistory_chqbook",false); //Arun(12/09/17)
									PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "after making buttons invisible");
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
					
							if (pEvent.getSource().getName().equalsIgnoreCase("ReferHistory")) {
								if(formObject.getNGValue("decision").contains("Compliance")|| formObject.getNGValue("decision").contains("FCU"))
									 AddInReferGrid();
							} //Added By Akshay for Multiple Refer	
					
					  break;
					  
					case MOUSE_CLICKED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
							//GenerateXML();
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Add")){
							
							formObject.setNGValue("Product_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("Product_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");
							
							formObject.setNGValue("Product_type","Conventional",false);
							formObject.setNGValue("ReqProd","--Select--",false);
							formObject.setNGValue("AppType","--Select--",false);
							formObject.setNGValue("EmpType","--Select--",false);
							formObject.setNGValue("Priority","Primary",false);
							formObject.setVisible("Product_Label5",false);
							formObject.setVisible("Product_Label3",false);
							formObject.setVisible("Product_Label6",false);
							formObject.setVisible("Product_Label10",false);
							formObject.setVisible("Product_Label12",false);
							formObject.setVisible("CardProd",false);
							formObject.setVisible("Scheme",false);
							formObject.setVisible("ReqTenor",false);
							formObject.setVisible("Product_Label15",false); 
							formObject.setVisible("Product_Label17",false); 
							formObject.setVisible("Product_Label16",false); 
							formObject.setVisible("Product_Label18",false);
							formObject.setVisible("Product_Label21",false); 
							formObject.setVisible("Product_Label22",false); 
							formObject.setVisible("Product_Label23",false); 
							formObject.setVisible("Product_Label24",false);
							formObject.setVisible("typeReq",false);
							formObject.setVisible("LimitAcc",false); 
							formObject.setVisible("LimitExpiryDate",false);
							formObject.setNGFrameState("Incomedetails", 1);
							formObject.setNGFrameState("EmploymentDetails", 1);
							formObject.setNGFrameState("EligibilityAndProductInformation", 1);
							formObject.setNGFrameState("Alt_Contact_container", 1);
							formObject.setNGFrameState("CC_Loan_container", 1);
							formObject.setNGFrameState("CompanyDetails", 1);
							formObject.setNGFrameState("CardDetails", 1);
						}	
						
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Modify")){
							
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
							formObject.setNGFrameState("Incomedetails", 1);
							formObject.setNGValue("Product_type","Conventional",false);
							formObject.setNGValue("ReqProd","--Select--",false);
							formObject.setNGValue("AppType","--Select--",false);
							formObject.setNGValue("EmpType","--Select--",false);
							formObject.setNGValue("Priority","Primary",false);
							formObject.setVisible("Product_Label5",false);
							formObject.setVisible("Product_Label3",false);
							formObject.setVisible("Product_Label6",false);
							formObject.setVisible("Product_Label10",false);
							formObject.setVisible("Product_Label12",false);
							formObject.setVisible("CardProd",false);
							formObject.setVisible("Scheme",false);
							formObject.setVisible("ReqTenor",false);
							formObject.setVisible("Product_Label15",false); 
							formObject.setVisible("Product_Label17",false); 
							formObject.setVisible("Product_Label16",false); 
							formObject.setVisible("Product_Label18",false);
							formObject.setVisible("Product_Label21",false); 
							formObject.setVisible("Product_Label22",false); 
							formObject.setVisible("Product_Label23",false); 
							formObject.setVisible("Product_Label24",false);
							formObject.setVisible("typeReq",false);
							formObject.setVisible("LimitAcc",false); 
							formObject.setVisible("LimitExpiryDate",false);
							formObject.setNGFrameState("Incomedetails", 1);
							formObject.setNGFrameState("EmploymentDetails", 1);
							formObject.setNGFrameState("EligibilityAndProductInformation", 1);
							formObject.setNGFrameState("Alt_Contact_container", 1);
							formObject.setNGFrameState("CC_Loan_container", 1);
							formObject.setNGFrameState("CompanyDetails", 1);
							formObject.setNGFrameState("CardDetails", 1);
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");
							
							formObject.setNGValue("Product_type","Conventional",false);
							formObject.setNGValue("ReqProd","--Select--",false);
							formObject.setNGValue("AppType","--Select--",false);
							formObject.setNGValue("EmpType","--Select--",false);
							formObject.setNGValue("Priority","Primary",false);
							formObject.setVisible("Product_Label5",false);
							formObject.setVisible("Product_Label3",false);
							formObject.setVisible("Product_Label6",false);
							formObject.setVisible("Product_Label10",false);
							formObject.setVisible("Product_Label12",false);
							formObject.setVisible("CardProd",false);
							formObject.setVisible("Scheme",false);
							formObject.setVisible("ReqTenor",false);
							formObject.setVisible("Product_Label15",false); 
							formObject.setVisible("Product_Label17",false); 
							formObject.setVisible("Product_Label16",false); 
							formObject.setVisible("Product_Label18",false);
							formObject.setVisible("Product_Label21",false); 
							formObject.setVisible("Product_Label22",false); 
							formObject.setVisible("Product_Label23",false); 
							formObject.setVisible("Product_Label24",false);
							formObject.setVisible("typeReq",false);
							formObject.setVisible("LimitAcc",false); 
							formObject.setVisible("LimitExpiryDate",false);
							formObject.setNGFrameState("Incomedetails", 1);
							formObject.setNGFrameState("EmploymentDetails", 1);
							formObject.setNGFrameState("EligibilityAndProductInformation", 1);
							formObject.setNGFrameState("Alt_Contact_container", 1);
							formObject.setNGFrameState("CC_Loan_container", 1);
							formObject.setNGFrameState("CompanyDetails", 1);
							formObject.setNGFrameState("CardDetails", 1);
						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
							formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

						}

					
						else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
							PL_SKLogger.writeLog("PL_Initiation", "Inside Customer_save button: ");
							formObject.saveFragment("CustomerDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
							formObject.saveFragment("ProductContainer");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
							formObject.saveFragment("GuarantorDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
							formObject.saveFragment("IncomeDEtails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
							formObject.saveFragment("Incomedetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
							formObject.saveFragment("CompanyDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Save")){
							formObject.saveFragment("PartnerDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
							formObject.saveFragment("Liability_container");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
							formObject.saveFragment("InternalExternalContainer");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
							formObject.saveFragment("EmploymentDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
							formObject.saveFragment("EligibilityAndProductInformation");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
							formObject.saveFragment("MiscFields");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
							formObject.saveFragment("Address_Details_container");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
							formObject.saveFragment("Alt_Contact_container");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
							formObject.saveFragment("Supplementary_Container");
						}

						else if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
							formObject.saveFragment("FATCA");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
							formObject.saveFragment("KYC");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
							formObject.saveFragment("OECD");
						}
						
						// disha FSD
						else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
						
						//tanshu started
						else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_updcust")){
							try{
							 PL_SKLogger.writeLog("RLOS value of ENTITY_MAINTENANCE_REQ","inside ENTITY_MAINTENANCE_REQ is generated");
							 String acc_veri= (formObject.getNGValue("Is_Acc_verified")!=null) ?formObject.getNGValue("Is_Acc_verified"):"";
							 String acc_mant_flag = formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ");
							 PL_SKLogger.writeLog("PL checker Account Update call", "entity_flag : "+acc_veri);
							 
							 if(acc_veri == null || acc_veri.equalsIgnoreCase("")||acc_veri.equalsIgnoreCase("N")){
								 outputResponse = GenerateXML("ENTITY_MAINTENANCE_REQ","AcctVerification");
								 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								 PL_SKLogger.writeLog("PL DDVT checker value of ReturnCode AcctVerification: ",ReturnCode);
								 if(ReturnCode.equalsIgnoreCase("0000")){
											formObject.setNGValue("Is_Acc_verified","Y");
											acc_veri="Y";
											PL_SKLogger.writeLog("PL DDVT checker","account Verified successfully");
								 }
								else{
										PL_SKLogger.writeLog("PL DDVT CHECKER : ","account Verified failed ReturnCode: "+ReturnCode );
										formObject.setNGValue("Is_Acc_verified","N");
										alert_msg= "Account Verification operation Failed, Please try after some time or contact administrator";
										 popupFlag = "Y";
			                            throw new ValidatorException(new FacesMessage(alert_msg));
									}
							 }
							 
								String acc_acti= (formObject.getNGValue("Is_Acc_Active")!=null) ? formObject.getNGValue("Is_Acc_Active"):"";
								if(acc_veri.equalsIgnoreCase("Y")&&(acc_acti.equalsIgnoreCase("")||acc_acti.equalsIgnoreCase("N"))){
									outputResponse = GenerateXML("ENTITY_MAINTENANCE_REQ","AcctActivation");
									 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
									 PL_SKLogger.writeLog("PL DDVT checker value of ReturnCode for AcctActivation: ",ReturnCode);
									 if(ReturnCode.equalsIgnoreCase("0000")){
												formObject.setNGValue("Is_Acc_Active","Y");
												acc_acti="Y";
												PL_SKLogger.writeLog("PL DDVT checker","account Activation successfully");
									 }
									 else{
											PL_SKLogger.writeLog("PL DDVT CHECKER : ","ENTITY_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
											formObject.setNGValue("Is_Acc_Active","N");
											alert_msg= "Account Activation operation Failed, Please try after some time or contact administrator";
											 popupFlag = "Y";
				                            throw new ValidatorException(new FacesMessage(alert_msg));
										}
									 
								}
								
							 if(acc_veri.equalsIgnoreCase("Y")&&acc_acti.equalsIgnoreCase("Y")) {
								 outputResponse = GenerateXML("ACCOUNT_MAINTENANCE_REQ","");
								 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
									PL_SKLogger.writeLog("PL DDVT checker value of ReturnCode",ReturnCode);
									if(ReturnCode.equalsIgnoreCase("0000") ){
										formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","Y");
										PL_SKLogger.writeLog("PL DDVT checker value of ENTITY_MAINTENANCE_REQ","value of ACCOUNT_MAINTENANCE_REQ"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
										valueSetCustomer(outputResponse);    
										PL_SKLogger.writeLog("PL DDVT checker value of ACCOUNT_MAINTENANCE_REQ","ACCOUNT_MAINTENANCE_REQ is generated");
										PL_SKLogger.writeLog("PL DDVT checker value of Customer Details",formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
										formObject.RaiseEvent("WFSave");
										alert_msg= "Account Updated Successfully !";
										 popupFlag = "Y";
			                            throw new ValidatorException(new FacesMessage(alert_msg));
									}
									else{
										PL_SKLogger.writeLog("PL DDVT CHECKER :","ACCOUNT_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
										formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","N");
										alert_msg= "Account Update operation Failed, Please try after some time or contact administrator";
										 popupFlag = "Y";
			                            throw new ValidatorException(new FacesMessage(alert_msg));
									}
							 }										
					 }
							catch(Exception ex){
								PL_SKLogger.writeLog("PL DDVT CHECKER :","Exception in update account: "+new PersonalLoanSCommonCode().printException(ex));
							}
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button4"))
						{
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
							 
						else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button5"))
						{
								 
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button6"))
						{
								 
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						
					     // disha FSD
                        else  if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3")){
							popupFlag = "Y";
							String message = CustomerUpdate();
							throw new ValidatorException(new FacesMessage(message));
						}
						
					/*	//started merged code
						//Update Customer call(Tanshu Aggarwal-29/05/2017) 
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3")){ 
							
							if(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq").equalsIgnoreCase("true"))
							{
							
								PL_SKLogger.writeLog("","inside Decision history button");  
							 outputResponse = GenerateXML("CUSTOMER_DETAILS","Inquire");
                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                           PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                         //Modified condition Tanshu Aggarwal(7-06-2017)
                           if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000"))){
                        	 //Modified condition Tanshu Aggarwal(7-06-2017)
                        	   PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                              formObject.setNGValue("Is_CustInquiry","Y"); 
                              PL_SKLogger.writeLog("","Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry")); 
                              PL_SKLogger.writeLog("","inside Update_Customer");  
                            String cif_status = (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
							 if(cif_status.equalsIgnoreCase("ACTVE")){
	                            outputResponse = GenerateXML("CUSTOMER_DETAILS","Lock");
	                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                               ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                               PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                             //Modified condition Tanshu Aggarwal(7-06-2017)
                               if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000"))){
                            	 //Modified condition Tanshu Aggarwal(7-06-2017)
                            	   PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                 formObject.setNGValue("Is_CustLock","Y");
                                   
                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","Customer_Details is generated");
                             
                                    outputResponse = GenerateXML("CUSTOMER_DETAILS","UnLock");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                    PL_SKLogger.writeLog("RLOS value of ReturnCode","inside unlock");
                                       PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                                       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                       PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                       //Modified condition Tanshu Aggarwal(7-06-2017)
                                       if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000"))){
                                    	   //Modified condition Tanshu Aggarwal(7-06-2017)
                                    	   outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","");
                                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                                               ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                               PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                             
                                               //Modified condition Tanshu Aggarwal(7-06-2017)
                                               if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000"))){
                                            	   //Modified condition Tanshu Aggarwal(7-06-2017)
                                                   formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","Y");
                                                   PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
                                                   valueSetCustomer(outputResponse);    
                                                   PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","CUSTOMER_UPDATE_REQ is generated");
                                                   PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
                                               }
                                               else{
                                            	   PL_SKLogger.writeLog("Customer Details","CUSTOMER_UPDATE_REQ is not generated");
                                                   formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
                                               }
                                               PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
                                               formObject.RaiseEvent("WFSave");
                                               PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","after saving the flag");
                                               if((formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ").equalsIgnoreCase("Y")))
                                               { 
                                            	   PL_SKLogger.writeLog("RLOS value of Is_CUSTOMER_UPDATE_REQ","inside if condition");
                                                   formObject.setEnabled("DecisionHistory_Button3", false); 
                                                   buttonClickFlag="DecisionHistory_Button3";
                                                   throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","FetchDetails#Customer Updated Successful!!","",hm));
                                               }
                                               else{
                                                   formObject.setEnabled("DecisionHistory_Button3", true);
                                                   throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","FetchDetails#Customer Updated Fail!!","",hm));
                                               }
                                       }
                                       else{
                                    	   PL_SKLogger.writeLog("Customer Details","Customer_Details unlock is not generated");
                                           
                                       }
                               }
                               else{
                            	   PL_SKLogger.writeLog("Customer Details","Customer_Details lock is not generated");
                                   
                               }
                               
							}
							 else {
								 PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
								
								 popupFlag = "Y";
	                            throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
							 }
                       //one more added for if condition of status
					 }
                           //ended
							}
							else
								PL_SKLogger.writeLog("RLOS value of Customer_Details","Account is not retained");
                        }
						
						//Update Customer call(Tanshu Aggarwal-29/05/2017) 
*/						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_chqbook")){
						PL_SKLogger.writeLog("RLOS value of CheckBook","inside CheckBook is generated");
                        outputResponse = GenerateXML("CHEQUE_BOOK_ELIGIBILITY","");
                        	ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                        	PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                           PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                           String ReferenceId=(outputResponse.contains("<ReferenceId>")) ? outputResponse.substring(outputResponse.indexOf("<ReferenceId>")+"</ReferenceId>".length()-1,outputResponse.indexOf("</ReferenceId>")):"";    
                           PL_SKLogger.writeLog("RLOS value of ReferenceId",ReferenceId);
                           //Modified condition Tanshu Aggarwal(7-06-2017)
                           if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000"))){
                        	   //Modified condition Tanshu Aggarwal(7-06-2017)
                               formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","Y");
                               PL_SKLogger.writeLog("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY","value of ENTITY_MAINTENANCE_REQ"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
                               valueSetCustomer(outputResponse);    
                               PL_SKLogger.writeLog("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY","Is_CHEQUE_BOOK_ELIGIBILITY is generated");
                               PL_SKLogger.writeLog("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value",formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
                               formObject.setNGValue("cmplx_Decision_ChequeBookNumber",ReferenceId);
                               PL_SKLogger.writeLog("ReferenceId",""+formObject.getNGValue("cmplx_Decision_ChequeBookNumber"));
                               formObject.RaiseEvent("WFSave");
                           }
                           else{
                        	   PL_SKLogger.writeLog("Customer Details","Is_CHEQUE_BOOK_ELIGIBILITY is not generated");
                               formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","N");
                           }
                           outputResponse = GenerateXML("NEW_CARD_REQ","");
                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                               ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                               PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                               String RequestId=(outputResponse.contains("<RequestId>")) ? outputResponse.substring(outputResponse.indexOf("<RequestId>")+"</RequestId>".length()-1,outputResponse.indexOf("</RequestId>")):"";    
                               PL_SKLogger.writeLog("RLOS value of RequestId",RequestId);
                               //Modified condition Tanshu Aggarwal(7-06-2017)
                               if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000"))){
                            	   //Modified condition Tanshu Aggarwal(7-06-2017)
                                   formObject.setNGValue("Is_NEW_CARD_REQ","Y");
                                   PL_SKLogger.writeLog("RLOS value of NEW_CARD_REQ","value of NEW_CARD_REQ"+formObject.getNGValue("Is_NEW_CARD_REQ"));
                                   valueSetCustomer(outputResponse);    
                                   PL_SKLogger.writeLog("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY","Is_NEW_CARD_REQ is generated");
                                   PL_SKLogger.writeLog("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value",formObject.getNGValue("Is_NEW_CARD_REQ"));
                                   formObject.setNGValue("cmplx_Decision_DebitcardNumber",RequestId);
                                   PL_SKLogger.writeLog("NEW_CARD_REQ",""+formObject.getNGValue("cmplx_Decision_DebitcardNumber"));
                                   formObject.RaiseEvent("WFSave");
                                }
                               else{
                            	   PL_SKLogger.writeLog("Customer Details","Is_NEW_CARD_REQ is not generated");
                                   formObject.setNGValue("Is_NEW_CARD_REQ","N");
                               }
                               
                               formObject.RaiseEvent("WFSave");
                               PL_SKLogger.writeLog("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value",formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
                               PL_SKLogger.writeLog("RLOS value of Is_NEW_CARD_REQ flag123 value",formObject.getNGValue("Is_NEW_CARD_REQ"));
                                  String ChequeBook=formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY");
                                  String NewCardReq=formObject.getNGValue("Is_NEW_CARD_REQ");
                                  if((ChequeBook.equalsIgnoreCase("Y")) && (NewCardReq.equalsIgnoreCase("Y"))){
								  popupFlag = "Y";
								PL_SKLogger.writeLog("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value","inside if"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
                                  
								 throw new ValidatorException(new CustomExceptionHandler("Debit Card and Cheque Book created successfully!!","DecisionHistory_chqbook#Debit Card and Cheque Book created successfully","",hm));
                                  }
                                  else if((ChequeBook.equalsIgnoreCase("N")) && (NewCardReq.equalsIgnoreCase("Y"))){
								 popupFlag = "Y";
								PL_SKLogger.writeLog("RLOS value of Is_NEW_CARD_REQ flag123 value","inside elseif 111"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
								 throw new ValidatorException(new CustomExceptionHandler("Debit Card created sucessfully, But Cheque Book created Failed.!!","DecisionHistory_chqbook#Debit Card created sucessfully, But Cheque Book created Failed.","",hm));
                                  }
                                  else if((ChequeBook.equalsIgnoreCase("Y")) && (NewCardReq.equalsIgnoreCase("N"))){
								 popupFlag = "Y";
								PL_SKLogger.writeLog("RLOS value of Is_NEW_CARD_REQ flag123 value","inside elseif12"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
								 throw new ValidatorException(new CustomExceptionHandler("Cheque Book created sucessfully, But Debit Card created Failed.!!","DecisionHistory_chqbook#Cheque Book created sucessfully, But Debit Card created Failed.","",hm));
                                  }
                                  else{
								 popupFlag = "Y";
								PL_SKLogger.writeLog("RLOS value of Is_NEW_CARD_REQ flag123 value","inside else"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
								throw new ValidatorException(new CustomExceptionHandler("Cheque Book created and Debit Card created Failed, Please try after time or contact administrator!!","DecisionHistory_chqbook#Cheque Book created and Debit Card created Failed, Please try after time or contact administrator.","",hm));
                                  }
				 			}
						//ended merged code
                        
                        if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button1"))
						{
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
						}
							 
						else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button2"))
						{
								 
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
						}
							 
						else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button3"))
						{
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
						}
						else{
							PL_SKLogger.writeLog("PL DDVT Checker", "This control is not maintained in code control name:"+pEvent.getSource().getName());
						}
							
						//tanshu ended
				 break;
					
					 case VALUE_CHANGED:
							PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
 
							/*if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
								PL_SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
								getAge(formObject.getNGValue("cmplx_Customer_DOb"));
							}
							*/
							if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
								ReqProd=formObject.getNGValue("ReqProd");
								PL_SKLogger.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
								loadPicklistProduct(ReqProd);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
								PL_SKLogger.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
								formObject.clear("AppType");
								formObject.setNGValue("AppType","--Select--");
								if(formObject.getNGValue("SubProd").equalsIgnoreCase("Business titanium Card")){
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
									formObject.setNGValue("EmpType","Self Employed");
								}	
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Instant Money"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");
								
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Limit Increase"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");
								
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Salaried Credit Card"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");
								
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Self Employed Credit Card"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");
								
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Expat Personal Loans"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");
								
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("National Personal Loans"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");
							
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Pre-Approved"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");
								
								else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Product Upgrade"))
									LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
							}
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
					default: break;
					
				}
			}
			catch(Exception ex)
			{
				 PL_SKLogger.writeLog("PL DDVY maker","Inside Exception to show msg at front end");
				 if(ex instanceof ValidatorException)
					{   PL_SKLogger.writeLog("PL DDVY maker","popupFlag value: "+ popupFlag);
						if(popupFlag.equalsIgnoreCase("Y"))
						{
							PL_SKLogger.writeLog("PL DDVY maker","Inside popup msg through Exception "+ popupFlag);
							if(popUpControl.equals(""))
							{
								PL_SKLogger.writeLog("PL DDVY maker","Before show Exception at front End "+ popupFlag);
								throw new ValidatorException(new FacesMessage(alert_msg));
							}else
							{
								throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

							}
							
						}
						else{
						HashMap<String,String> hm1=new HashMap<String,String>();
						hm1.put("Error","Checked");
						if(!popUpMsg.equals("")) {
							try{ throw new ValidatorException(new CustomExceptionHandler("Details Fetched", popUpMsg,"EventType", hm1));}finally{hm1.clear();}
							
						}
					else {
							try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));}finally{hm1.clear();}
							
						}
						
						}
					}
				else
				{
				ex.printStackTrace();
				System.out.println("exception in eventdispatched="+ ex);
				}
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

