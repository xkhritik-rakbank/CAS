/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CPV.java
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

public class PL_CPV extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL");
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
		String alert_msg="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);

					/*if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
	        			hm.put("CustomerDetails","Clicked");
						popupFlag="N";
	    				formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
	    				loadPicklistCustomer();
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
	        			hm.put("ProductContainer","Clicked");
						popupFlag="N";
	    				formObject.fetchFragment("ProductContainer", "Product", "q_Product");
	    				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
	        	
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			}
					if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {
	        			
	        			hm.put("EmploymentDetails","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
	        			
	        			loadPicklist_Employment();
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
						hm.put("IncomeDEtails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("InternalExternalLiability")) {
	        			hm.put("InternalExternalLiability","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("InternalExternalLiability", "ExternalLiabilities", "q_ExtLiabilities");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}	
					if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
	        			hm.put("MiscFields","Clicked");
						popupFlag="N";
		
	        			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation")) {
	        			hm.put("EligibilityAndProductInformation","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
	
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}	
	        		
					
	        		if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
	        			hm.put("Address_Details_container","Clicked");
						popupFlag="N";
		
	        			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
	        			loadPicklist_Address();
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
	        		
	        		if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) {
	        			hm.put("Alt_Contact_container","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");	
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
	        		
	        		if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
					{
						hm.put("FATCA","Clicked");
						popupFlag="N";
						formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}	
					
					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
					{
						hm.put("KYC","Clicked");
						popupFlag="N";
						formObject.fetchFragment("KYC", "KYC", "q_KYC");
	      			
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}	
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
					{
						hm.put("OECD","Clicked");
						popupFlag="N";
						formObject.fetchFragment("OECD", "OECD", "q_OECD");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Self_Employed")) {
            			hm.put("Self_Employed","Clicked");
    					popupFlag="N";
            			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
            			try
    					{
    						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    					}
    					finally{hm.clear();}
            		}
					
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
            			hm.put("LoanDetails","Clicked");
    					popupFlag="N";
            			
            			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
            			
            			try
    					{
    						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    					}
    					finally{hm.clear();}
            		}
	        		
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
	        			hm.put("DecisionHistory","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
	        			loadPicklist3();
	        			loadInDecGrid();
	        			formObject.setVisible("DecisionHistory_chqbook",false);
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Inc_Doc")) {
	        			hm.put("Inc_Doc","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Cust_Detail_verification")) {
	        			hm.put("Cust_Detail_verification","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Cust_Detail_verification", "CustDetailVerification", "q_CustDetVer");
	        			custdetvalues();
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Business_verification")) {
	        			hm.put("Business_verification","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Business_verification", "BussinessVerification", "q_businessverification");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Home_country_verification")) {
	        			hm.put("Home_country_verification","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Home_country_verification", "HomeCountryVerification", "q_HomeCountryVeri");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Residence_verification")) {
	        			hm.put("Residence_verification","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Residence_verification", "ResidenceVerification", "q_ResiVerification");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Guarantor_verification")) {
	        			hm.put("Guarantor_verification","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Guarantor_verification", "GuarantorVerification", "q_GuarantorVer");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Reference_detail_verification")) {
	        			hm.put("Reference_detail_verification","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Reference_detail_verification", "ReferenceDetailVerification", "q_RefDetVer");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Office_verification")) {
	        			hm.put("Office_verification","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
	        			OfficeVervalues();
	        			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Loan_card_details")) {
	        			hm.put("Loan_card_details","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Loan_card_details", "LoanandCard", "q_LoanandCard");
	        			loancardvalues();
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_details")) {
	        			hm.put("Notepad_details","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Notepad_details", "NotepadDetails", "q_Note");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Smart_check")) {
	        			hm.put("Smart_check","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}*/ //Arun	(12/09/17)
					
					break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
								//setDisabled();
					 			formObject.setLocked("Customer_Frame1",true);
					 			loadPicklistCustomer();
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
					 			loadPicklist4();
					 			//loadPicklist_Employment();
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
							
							if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {
								
					 			formObject.setLocked("cmplx_CustDetailVerification_Mob_No1_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_Mob_No2_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_dob_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_POBoxNo_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_emirates_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_persorcompPOBox_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_Resno_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_Offtelno_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_hcountryaddr_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_email1_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_email2_val",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {
								
					 			formObject.setLocked("cmplx_OffVerification_fxdsal_val",true);
					 			formObject.setLocked("cmplx_OffVerification_accprovd_val",true);
					 			formObject.setLocked("cmplx_OffVerification_desig_val",true);
					 			formObject.setLocked("cmplx_OffVerification_doj_val",true);
					 			formObject.setLocked("cmplx_OffVerification_cnfrminjob_val",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {
								
					 			formObject.setLocked("cmplx_LoanandCard_loanamt_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_tenor_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_emi_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_islorconv_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_firstrepaydate_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_cardtype_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_cardlimit_val",true);
							}
							
							/*          Function Header:

							**********************************************************************************

							         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
									 
							             
							Date Modified                       : 19/0612017              
							Author                              : Arun              
							Description                         : setVisible true or false in Decision fragment for CPV as per requirement              

							***********************************************************************************  */							
							
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	loadPicklist1();
			                	
			                	formObject.setVisible("Decision_Label1",false);
			                	formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
			                	formObject.setVisible("DecisionHistory_chqbook",false);
			                	formObject.setVisible("DecisionHistory_Label1",false);
			                	formObject.setVisible("cmplx_Decision_refereason",false);
			                	formObject.setVisible("DecisionHistory_Rejreason",false);
			                	formObject.setVisible("cmplx_Decision_rejreason",false);
			                	formObject.setVisible("DecisionHistory_Button1",false);
			                	formObject.setVisible("DecisionHistory_Label5",false);
			                	formObject.setVisible("cmplx_Decision_desc",false);
			                	formObject.setVisible("DecisionHistory_Label3",false);
			                	formObject.setVisible("cmplx_Decision_strength",false);
			                	formObject.setVisible("DecisionHistory_Label4",false);
			                	formObject.setVisible("cmplx_Decision_weakness",false);			                	
			                	formObject.setVisible("DecisionHistory_Label11",true);
			                	formObject.setVisible("cmplx_Decision_Decreasoncode",true);
			                	formObject.setVisible("DecisionHistory_Label12",true);
			                	formObject.setVisible("cmplx_Decision_NoofAttempts",true);
			                	
			                	formObject.setTop("Decision_Label3",10);
			                	formObject.setTop("cmplx_Decision_Decision",24);
			                	formObject.setTop("DecisionHistory_Label11",10);
			                	formObject.setTop("cmplx_Decision_Decreasoncode",24);
			                	formObject.setTop("DecisionHistory_Label12",10);
			                	formObject.setTop("cmplx_Decision_NoofAttempts",24);
			                	formObject.setTop("Decision_Label4",58);
			                	formObject.setTop("cmplx_Decision_REMARKS",72);			                	
			                	formObject.setTop("Decision_ListView1",200);
			                	formObject.setTop("DecisionHistory_save",400);
			                	
			                	formObject.setLeft("Decision_Label4",24);
			                	formObject.setLeft("cmplx_Decision_REMARKS",24);
			                	formObject.setLeft("Decision_Label3",24);
			                	formObject.setLeft("cmplx_Decision_Decision",24);
			                	formObject.setLeft("DecisionHistory_Label11",297);
			                	formObject.setLeft("cmplx_Decision_Decreasoncode",297);
			                	formObject.setLeft("DecisionHistory_Label12",555);
			                	formObject.setLeft("cmplx_Decision_NoofAttempts",555);		                	
			                
			                	
			                	/*formObject.setVisible("cmplx_Decision_waiveoffver",false);
			                	formObject.setVisible("DecisionHistory_chqbook",false);
			                	formObject.setVisible("Decision_Label1",false);
			                	formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
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
			                	formObject.setVisible("cmplx_Decision_weakness",false);*/
			                	
			                	//Common function for decision fragment textboxes and combo visibility
			                	//decisionLabelsVisibility();
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
						//Arun (16-jun)
						/*if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1"))
						{
							Date dNow = new Date();
							System.out.println("Inside time");
							SimpleDateFormat ft = new SimpleDateFormat ("E hh:mm:ss a zzz");
							formObject.setNGValue("cmplx_NotepadDetails_cmplx_Telloggrid",0,2,ft.format(dNow));
						}*/
						//Arun (16-jun)
						
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
						// disha FSD
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
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
							//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							//SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_Telloggrid");

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
						if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification_save")){
							formObject.saveFragment("Cust_Detail_verification");
							alert_msg="Customer detail verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification_save")){
							formObject.saveFragment("Business_verification");
							alert_msg="Business detail verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification_save")){
							formObject.saveFragment("Home_country_verification");
							alert_msg="Home country verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification_save")){
							formObject.saveFragment("Residence_verification");
							alert_msg="Residence verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification_save")){
							formObject.saveFragment("Guarantor_verification");
							alert_msg="Guarantor verification   saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification_save")){
							formObject.saveFragment("Reference_detail_verification");
							alert_msg="Reference detail verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_save")){
							formObject.saveFragment("Office_verification");
							alert_msg="Office verification details saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("LoanandCard_save")){
							formObject.saveFragment("Loan_card_details");
							alert_msg="Loan and Card details saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
							alert_msg="Notepad details saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck_save")){
							formObject.saveFragment("Smart_check");
							alert_msg="Smart check details saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
				
						if(pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification_Button1")){
							formObject.saveFragment("Frame7");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification_Button1")){
							formObject.saveFragment("Frame8");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification_Button1")){
							formObject.saveFragment("Frame6");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_Button1")){
							formObject.saveFragment("Frame11");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification_Button1")){
							formObject.saveFragment("Frame9");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification_Button1")){
							formObject.saveFragment("Frame10");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification_Button1")){
							formObject.saveFragment("Frame5");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("LoanandCard_Button1")){
							formObject.saveFragment("Frame13");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button3")){
							formObject.saveFragment("Frame14");
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
							 // disha FSD
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
		 formObject.setNGValue("CPV_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}

