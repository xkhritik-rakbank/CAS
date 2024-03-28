/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Disbursal.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

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

public class PL_CC_Disbursal extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL");
		PL_SKLogger.writeLog("PL Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
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
		String outputResponse="";
		String ReturnCode="";
		String ReturnDesc="";
		String buttonClickFlag="";
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
							
							if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
								
					 			formObject.setLocked("PartMatch_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
								
					 			formObject.setLocked("FinacleCRMIncident_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
								
					 			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
								
					 			formObject.setLocked("FinacleCore_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
								
					 			formObject.setLocked("MOL1_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
								
					 			formObject.setLocked("WorldCheck1_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
								
					 			formObject.setLocked("RejectEnq_Frame1",true);
							}

							if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
	
								formObject.setLocked("SalaryEnq_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {
								
					 			formObject.setLocked("CustDetailVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {
								
					 			formObject.setLocked("BussinessVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {
								
					 			formObject.setLocked("HomeCountryVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {
								
					 			formObject.setLocked("ResidenceVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification")) {
								
					 			formObject.setLocked("GuarantorVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {
								
					 			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {
								
					 			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {
								
					 			formObject.setLocked("LoanandCard_Frame1",true);
							}
							
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

							if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {
	
								formObject.setLocked("SmartCheck_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
								
								formObject.setLocked("Compliance_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FCU_Decision")) {
								
								formObject.setLocked("FCU_Decision_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
								if(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq").equalsIgnoreCase("false")){
									PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
									formObject.setVisible("CC_Creation_Update_Customer", true);
									//formObject.setVisible("DecisionHistory_updcust", true);
									PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "after making buttons visible");
								}
			                	loadPicklist1();
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
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");							
							
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_save")){
							formObject.saveFragment("Loan_Disbursal");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_save")){
							formObject.saveFragment("CC_Creation");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc_save")){
							formObject.saveFragment("Limit_Inc");
						}
						
                        //started code merged
						 //customer updated call(Tanshu Aggarwal-29/05/2017)
						if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Update_Customer")){
                            PL_SKLogger.writeLog("","inside Update_Customer");  
                            outputResponse = GenerateXML("CUSTOMER_DETAILS","Inquire");
                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                           PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                           PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                           if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                               PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                              formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
                              PL_SKLogger.writeLog("","Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
                               PL_SKLogger.writeLog("","inside Update_Customer");  
                               String cif_status = (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
                               if(cif_status.equalsIgnoreCase("ACTVE")){
                            outputResponse = GenerateXML("CUSTOMER_DETAILS","Lock");
                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                           PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                           PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                           if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
                           PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                             formObject.setNGValue("Is_CustLock_Disbursal","Y");
                             PL_SKLogger.writeLog("","Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 
                               
                               PL_SKLogger.writeLog("RLOS value of Customer_Details","Customer_Details is generated");
                           //    SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
                                outputResponse = GenerateXML("CUSTOMER_DETAILS","UnLock");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                   PL_SKLogger.writeLog("RLOS value of ReturnCode","inside unlock");
                                   PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                                   formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
                                   PL_SKLogger.writeLog("","Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
                                   ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                   PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                   if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
                                        outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","");
                                        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                           PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                           PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                         //  Message =(outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    
                                         //  SKLogger.writeLog("RLOS value of Message",Message);
                                           
                                           if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
                                               formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
                                               PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
                                               valueSetCustomer(outputResponse);    
                                               PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","CUSTOMER_UPDATE_REQ is generated");
                                               PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
                                           }
                                           else{
                                               PL_SKLogger.writeLog("Customer Details","CUSTOMER_UPDATE_REQ is not generated");
                                               formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","N");
                                           }
                                           PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
                                           formObject.RaiseEvent("WFSave");
                                           PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","after saving the flag");
                                           if((formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal").equalsIgnoreCase("Y")))
                                           { 
                                               PL_SKLogger.writeLog("RLOS value of Is_CUSTOMER_UPDATE_REQ","inside if condition");
                                               formObject.setEnabled("Update_Customer", false); 
                                               buttonClickFlag="CC_Creation_Update_Customer";
                                               throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","Update_Customer#Customer Updated Successful!!","",hm));
                                           }
                                           else{
                                               formObject.setEnabled("CC_Creation_Update_Customer", true);
                                               throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","Update_Customer#Customer Updated Fail!!","",hm));
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
                           //added one more here
                 }
               else{
                           PL_SKLogger.writeLog("Customer Details","Customer_Details Inquiry is not generated");
                   }
                }
	 
						 //customer updated call(Tanshu Aggarwal-29/05/2017)
	 
	if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_updcust")){
	                         PL_SKLogger.writeLog("RLOS value of ENTITY_MAINTENANCE_REQ","inside ENTITY_MAINTENANCE_REQ is generated");
	                         outputResponse = GenerateXML("ENTITY_MAINTENANCE_REQ","");
	                         ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                            ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                            PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                        String    OperDesc =  (outputResponse.contains("<OperDesc>")) ? outputResponse.substring(outputResponse.indexOf("<OperDesc>")+"</OperDesc>".length()-1,outputResponse.indexOf("</OperDesc>")):"";    
	                            PL_SKLogger.writeLog("RLOS value of OperDesc",""+OperDesc);
	                            
	                            if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
	                                formObject.setNGValue("Is_Entity_Maintainenece_ReqDisbursal","Y");
	                                PL_SKLogger.writeLog("RLOS value of ENTITY_MAINTENANCE_REQ","value of ENTITY_MAINTENANCE_REQ"+formObject.getNGValue("Is_Entity_Maintainenece_ReqDisbursal"));
	                                valueSetCustomer(outputResponse);    
	                                PL_SKLogger.writeLog("RLOS value of ENTITY_MAINTENANCE_REQ","ENTITY_MAINTENANCE_REQ is generated");
	                                PL_SKLogger.writeLog("RLOS value of Entity_Maintainenece_Req flag value",formObject.getNGValue("Is_Entity_Maintainenece_ReqDisbursal"));
	                            }
	                            else{
	                                PL_SKLogger.writeLog("Customer Details","ENTITY_MAINTENANCE_REQ is not generated");
	                                formObject.setNGValue("Is_Entity_Maintainenece_ReqDisbursal","N");
	                            }
	                            PL_SKLogger.writeLog("RLOS value of Entity_Maintainenece_Req",formObject.getNGValue("Is_Entity_Maintainenece_ReqDisbursal"));
	                            if(formObject.getNGValue("Is_Entity_Maintainenece_ReqDisbursal").equalsIgnoreCase("Y")){
	                            PL_SKLogger.writeLog("RLOS value of Entity_Maintainenece_Req","after if condition of is entity maintainence is Y");
	                            outputResponse = GenerateXML("ACCOUNT_MAINTENANCE_REQ","");
	                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                                PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
	                                    formObject.setNGValue("ACCOUNT_MAINTENANCE_REQDisbursal","Y");
	                                    PL_SKLogger.writeLog("RLOS value of ACCOUNT_MAINTENANCE_REQ","value of ACCOUNT_MAINTENANCE_REQ"+formObject.getNGValue("ACCOUNT_MAINTENANCE_REQDisbursal"));
	                                    valueSetCustomer(outputResponse);    
	                                    PL_SKLogger.writeLog("RLOS value of ACCOUNT_MAINTENANCE_REQ","ACCOUNT_MAINTENANCE_REQ is generated");
	                                    PL_SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("ACCOUNT_MAINTENANCE_REQDisbursal"));
	                                    throw new ValidatorException(new CustomExceptionHandler("Account Updated Successful!!","DecisionHistory_updcust#Account Updated Successful!!","",hm));
	                                }
	                                else{
	                                    PL_SKLogger.writeLog("Customer Details","ACCOUNT_MAINTENANCE_REQ is not generated");
	                                    formObject.setNGValue("ACCOUNT_MAINTENANCE_REQDisbursal","N");
	                                }
	                                PL_SKLogger.writeLog("RLOS value of Guarantor_CIF",formObject.getNGValue("ACCOUNT_MAINTENANCE_REQDisbursal"));
	                                formObject.RaiseEvent("WFSave");
	                                PL_SKLogger.writeLog("RLOS value of Guarantor_CIF","after saving the flag");
	                            }
	                    
	                
	                 }
	                //end of entity main and account mainitainence call
	
	
	//code added for card_services call(Tanshu Aggarwal-15/06/2017)
	
	 if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Card_Services")){
		 
		 PL_SKLogger.writeLog("RLOS value of ReturnCode","inside button click");
        String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
        PL_SKLogger.writeLog("RLOS value of Product_Name",""+Product_Name);
        if(Product_Name.equalsIgnoreCase("Limit Change Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
          
            if(ReturnCode.equalsIgnoreCase("0000")){
                PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServicesPL","Y");
                PL_SKLogger.writeLog("RLOS value of Customer_Details for limit change",formObject.getNGValue("Is_CardServicesPL"));
            }
        }
        if(Product_Name.equalsIgnoreCase("Financial Services Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
             if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServicesPL","Y"); 
                PL_SKLogger.writeLog("RLOS value of Customer_Details for financial product",formObject.getNGValue("Is_CardServicesPL"));
            }
        }
        if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
            if(ReturnCode.equalsIgnoreCase("0000")){
                PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServicesPL","Y"); 
                PL_SKLogger.writeLog("RLOS value of Customer_Details for product upgrade",formObject.getNGValue("Is_CardServicesPL"));
            }
        }
}
	//code added for card_services call(Tanshu Aggarwal-15/06/2017)
    //code for New Card and Notification and New Loan Request call Tanshu Aggarwal
		if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Button2")){
        
		         PL_SKLogger.writeLog("RLOS value of ReturnCode","inside button click");
		         String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
		         PL_SKLogger.writeLog("RLOS value of Product_Name",""+Product_Name);
		         outputResponse = GenerateXML("CARD_NOTIFICATION","");
                 PL_SKLogger.writeLog("RLOS value of ReturnCode","inside card notification");
                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                 if(ReturnCode.equalsIgnoreCase("0000")){
                     PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                     formObject.setNGValue("Is_CardNotifiactionPL","Y");
                     PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
                     throw new ValidatorException(new CustomExceptionHandler("Customer is Notified","CC_Creation_Button2#Customer is Notified","",hm));
                 }
       		} 
	                    //loan call
	                    if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_Button2")){
	                        outputResponse = GenerateXML("NEW_LOAN_REQ","");
	                        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                        PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                        ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                        PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                        String ContractID =  (outputResponse.contains("<contractID>")) ? outputResponse.substring(outputResponse.indexOf("<contractID>")+"</contractID>".length()-1,outputResponse.indexOf("</contractID>")):"";    
	                        PL_SKLogger.writeLog("RLOS value of ContractID",ContractID);
	                        if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
	                            PL_SKLogger.writeLog("RLOS value of Loan Request","value of Loan_Req inside");
	                            valueSetCustomer(outputResponse);  
	                            formObject.setNGValue("Is_LoanReq","Y");
	                            formObject.setNGValue("Contract_ID",ContractID);
	                            PL_SKLogger.writeLog("RLOS value of ReturnCode Contract_ID",""+formObject.getNGValue("Contract_ID"));
	                            PL_SKLogger.writeLog("RLOS value of ReturnCode Is_LoanReq",""+formObject.getNGValue("Is_LoanReq"));
	                        
	                        }  
	                        formObject.RaiseEvent("WFSave");
	                    }
	                    //ended code merged
	                    
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
		saveIndecisionGrid();
	}

}

