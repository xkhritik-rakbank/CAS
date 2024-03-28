
package com.newgen.omniforms.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
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

public class PL_Disbursal extends PLCommon implements FormListener
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
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
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
            PL_SKLogger.writeLog("PL Initiation", "value of los:"+formObject.getNGValue("Customer_Type"));
           
            String Current_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
            String Previous_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious");
            
            String JobDuration = Current_LOS+" "+Previous_LOS; 
            formObject.setNGValue("Emp_JobDuration",JobDuration); 
            PL_SKLogger.writeLog("PL Initiation", "value of los:"+formObject.getNGValue("Emp_JobDuration"));
            
        }catch(Exception e)
        {
            PL_SKLogger.writeLog("PL Initiation", "Exception:"+e.getMessage());
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
		String alert_msg="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();
		  try{
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
								int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
								if(n>0){
									for(int i=0;i<n;i++)
									{
								if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("Limit Increase"))
								{
									//PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
									formObject.setVisible("cmplx_LimitInc_CurrentLimit", false);
									formObject.setVisible("cmplx_LimitInc_New_Limit", false);
									formObject.setVisible("cmplx_LimitInc_LimitExpiryDate_button", false);
									//formObject.setVisible("DecisionHistory_updcust", true);
									//PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "after making buttons visible");
								
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
					 			loadPicklistELigibiltyAndProductInfo();
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
					
					
					  break;
					  
					case MOUSE_CLICKED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
							//GenerateXML();
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
						// 	disha FSD
						else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
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
						
						else	if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}
						else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

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
						
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_save")){
							formObject.saveFragment("Loan_Disbursal");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_save")){
							formObject.saveFragment("CC_Creation");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc_save")){
							formObject.saveFragment("Limit_Inc");
						}
						else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
						}
						
                        //started code merged
						else  if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Update_Customer")){
	                            PL_SKLogger.writeLog("","inside Update_Customer");  
	                            outputResponse = GenerateXML("CUSTOMER_DETAILS","Inquire");
	                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                           PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                          
	                           if(ReturnCode.equalsIgnoreCase("0000")){
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
						else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_updcust")){
							
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
	                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                                PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
	                                    formObject.setNGValue("ACCOUNT_MAINTENANCE_REQDisbursal","Y");
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
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3")){
							popupFlag = "Y";
							String message = CustomerUpdate();
                            throw new ValidatorException(new FacesMessage(message));
						}
						//code added for card_services call(Tanshu Aggarwal-15/06/2017)
						else if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Card_Services")){
							 
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
					         else if(Product_Name.equalsIgnoreCase("Financial Services Request")){
					             outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
					             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					              if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
					                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
					                 formObject.setNGValue("Is_CardServicesPL","Y"); 
					                 PL_SKLogger.writeLog("RLOS value of Customer_Details for financial product",formObject.getNGValue("Is_CardServicesPL"));
					             }
					         }
					         else if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
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
	                //end of entity main and account mainitainence call
	                    
	                     //code for New Card and Notification and New Loan Request call Tanshu Aggarwal
					else if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Button2")){
	                         formObject.RaiseEvent("WFSave");
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
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServicesPL"));
	                                 PL_SKLogger.writeLog("","CARD SERVICES RUNNING Limit Change Request");
	                         outputResponse = GenerateXML("CARD_NOTIFICATION","");
                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                             if(ReturnCode.equalsIgnoreCase("0000")){
                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                 formObject.setNGValue("Is_CardNotifiactionPL","Y");
                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
                             }
                         
	                       /*  if(Product_Name.equalsIgnoreCase("Limit Change Request")){
	                             outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
	                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                             if(ReturnCode.equalsIgnoreCase("0000")){
	                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
	                                 formObject.setNGValue("Is_CardServicesPL","Y");
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServicesPL"));
	                                 PL_SKLogger.writeLog("","CARD SERVICES RUNNING Limit Change Request");
	                                 outputResponse = GenerateXML("CARD_NOTIFICATION","Limit Change Request");
	                                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                 if(ReturnCode.equalsIgnoreCase("0000")){
	                                     PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
	                                     formObject.setNGValue("Is_CardNotifiactionPL","Y");
	                                     PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
	                                 }
	                             }
	                         }
	                         //for another product
	                         if(Product_Name.equalsIgnoreCase("Financial Services Request")){
	                             outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
	                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                             if(ReturnCode.equalsIgnoreCase("0000")){
	                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
	                                 formObject.setNGValue("Is_CardServicesPL","Y"); 
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServicesPL"));
	                                 PL_SKLogger.writeLog("","CARD SERVICES RUNNING Financial Services Request");
	                                 outputResponse = GenerateXML("CARD_NOTIFICATION","Financial Services Request");
	                                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                 if(ReturnCode.equalsIgnoreCase("0000")){
	                                     PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
	                                     formObject.setNGValue("Is_CardNotifiactionPL","Y");
	                                     PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
	                                 }
	                             }
	                         }
	                         //for another product
	                         if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
	                             outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
	                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                             if(ReturnCode.equalsIgnoreCase("0000")){
	                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
	                                 formObject.setNGValue("Is_CardServicesPL","Y"); 
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServicesPL"));
	                                 PL_SKLogger.writeLog("","CARD SERVICES RUNNING Product Upgrade Request");
	                                 outputResponse = GenerateXML("CARD_NOTIFICATION","Product Upgrade Request");
	                                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                 if(ReturnCode.equalsIgnoreCase("0000")){
	                                     PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
	                                     formObject.setNGValue("Is_CardNotifiactionPL","Y");
	                                     PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
	                                 }
	                             }
	                         }*/
	                         formObject.RaiseEvent("WFSave");
	                     } 
	                    }
	                  }
	                     //ended    for New Card and Notification call    
	                    
	                    //loan call
					else if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_Button1")){
                            List<List<String>> output = null;
                            String scheme="";
                            String acc_brnch="";
                            //added by abhishek started
                            PL_SKLogger.writeLog("PL Disbursal: " ,"inside Loan Creation");
                            formObject.fetchFragment("ProductContainer", "Product", "q_Product");
                            int n = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
                            PL_SKLogger.writeLog("PL Disbursal: ", "Row count: "+n);
                            for(int i=0;i<n;i++)
                                  {
                                        PL_SKLogger.writeLog("value of requested type is"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1) ,"");
                                        formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
                                        if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("PL") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("Personal Loan")){
                                          scheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);
                                              break;                                                          
                                        }
                                  }
                            if(!scheme.equalsIgnoreCase("") && !scheme.equalsIgnoreCase("null")){
                            	try{
                            		String Query = "select S.SCHEMEID,S.SCHEMEDESC,S.LSM_PRODDIFFRATE,P.Code , P.grace_days,P.description,S.PRIME_TYPE from ng_MASTER_Scheme S join ng_master_product_parameter P on S.PRODUCTFLAG = P.CODE and S.SCHEMEDESC = '"+scheme+"'";
                            		PL_SKLogger.writeLog("Query to fetch from ng_master_product_parameter"+Query ,"");
                            		output = formObject.getNGDataFromDataCache(Query);
                            		PL_SKLogger.writeLog("value ofscheme is"+scheme ,"");
                            		if(output!=null && !output.isEmpty()){
                            			formObject.setNGValue("scheme_code","CNP1");
                            			formObject.setNGValue("Scheme_desc","PL");
                            			formObject.setNGValue("cmplx_LoanDisb_prod_diff_rate",output.get(0).get(2) );
                            			formObject.setNGValue("cmplx_LoanDisb_product_code",output.get(0).get(3) );
                            			formObject.setNGValue("cmplx_LoanDisb_gracedays", output.get(0).get(4));
                            			formObject.setNGValue("cmplx_LoanDisb_product_desc",output.get(0).get(5) );
                            			formObject.setNGValue("cmplx_LoanDetails_basetype",output.get(0).get(6) );
                            			
                            			if(formObject.isVisible("DecisionHistory_Frame1")==false){
                            				PL_SKLogger.writeLog("PL" ,"Inside IsVisible check for Decision History!!");
                            				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
                            			}
                            			
                            			String acc_no = formObject.getNGValue("cmplx_Decision_AccountNo");
                        				PL_SKLogger.writeLog("cmplx_Decision_AccountNo: "+acc_no ,"");
                            			if(acc_no.equalsIgnoreCase("") || acc_no == null ){

                            				String qry = "select AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType = 'CURRENT ACCOUNT' and ( Child_Wi = '"+formObject.getWFWorkitemName()+"' or Wi_Name ='"+formObject.getWFWorkitemName()+"')";
                            				PL_SKLogger.writeLog("query is"+qry ,"");
                            				List<List<String>> record = formObject.getDataFromDataSource(qry);
                            				if(record!=null && record.get(0)!=null && record.size()>0){
                            					acc_no = record.get(0).get(0);
                            					//started By Akshay
                            					if(record.get(0).get(1).equalsIgnoreCase("RAKislamic CURRENT ACCOUNT")){
                                					acc_brnch=acc_no.substring(0,3);

                            					}
                            					else{
                            						acc_brnch=acc_no.substring(1,4);
                            					}
                            					//ended By Akshay
                            					PL_SKLogger.writeLog("value of accno is"+acc_no ,"");
                                				//formObject.setNGValue("Account_Number", record.get(0).get(0));
                            				}
                            				else{
                            					alert_msg="Loan can't be created as No current account is maintained for this Customer";
                            					throw new ValidatorException(new FacesMessage(alert_msg));
                            				}	
                            			}
                            			//added By Akshay-21/6/2017	
                            			else{
                        					acc_brnch=acc_no.substring(0,3);
                            			}
                            			if(acc_no!=null && !acc_no.equalsIgnoreCase("")){
                            				formObject.setNGValue("Account_Number", acc_no);//added By Akshay
                            				formObject.setNGValue("acct_brnch",acc_brnch);
                            				formObject.setNGValue("Loan_Disbursal_SourcingBranch",acc_no.substring(0,4));  

                            			}
                           			
                            			
                            			
                            			outputResponse = GenerateXML("NEW_LOAN_REQ","");
                            			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                            			PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                        ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                        PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                        String ContractID =  (outputResponse.contains("<contractID>")) ? outputResponse.substring(outputResponse.indexOf("<contractID>")+"</contractID>".length()-1,outputResponse.indexOf("</contractID>")):"";    
                            			PL_SKLogger.writeLog("RLOS value of ContractID",ContractID);
                            			if(ReturnCode.equalsIgnoreCase("0000")){
                            				PL_SKLogger.writeLog("RLOS value of Loan Request","value of Loan_Req inside");
                            				//valueSetCustomer(outputResponse);  
                            				formObject.setNGValue("Is_LoanReq","Y");
                            				formObject.setNGValue("Contract_id",ContractID);
                            				alert_msg = "Contract created sucessfully with contract id: "+ContractID;
                            				PL_SKLogger.writeLog("PL Disbursal ", "message need to be displayed: "+alert_msg);
                            			} 
                            			else{
                            				alert_msg = "Error in Contract creation operation, kindly try after some time or contact administrator.";
                            				PL_SKLogger.writeLog("PL Disbursal: ", "Error while generating new loan: "+ ReturnCode);
                            			}
                            		}
                            		else{
                            			alert_msg = "Error in Contract creation operation, kindly try after some time or contact administrator.";
                            			PL_SKLogger.writeLog("PL Disbursal ","ng_master_product_parameter is not miantained for scheme: "+scheme );
                            		}
                            		formObject.RaiseEvent("WFSave");
                            	}
                            	catch(Exception e){
                            		popupFlag = "Y";
                            		if(alert_msg.equalsIgnoreCase("")){
                            			alert_msg = "Error in Contract creation operation, kindly try after some time or contact administrator.";
                            		}
                            		PL_SKLogger.writeLog("PL Disbursal: ", "Exception occured while generating new loan: "+ e.toString()+" desc: "+e.getMessage()+" stack: "+e.getStackTrace());
                            		throw new ValidatorException(new FacesMessage(alert_msg));	
                            	}
                            	popupFlag = "Y";
                        		throw new ValidatorException(new FacesMessage(alert_msg));
                            }
                            else{
                              PL_SKLogger.writeLog("PL Disbursal ","Scheme code is not maintained for this case");
                            }
						  }  
	                         
					
	                    //ended code merged
	                    
                        
                       
                        
					
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
		  catch(Exception ex)
			{
				 PL_SKLogger.writeLog("PL Disbursal","Inside Exception to show msg at front end");
				 HashMap<String,String> hm1=new HashMap<String,String>();
					hm1.put("Error","Checked");
				 if(ex instanceof ValidatorException)
					{   PL_SKLogger.writeLog("PL Disbursal","popupFlag value: "+ popupFlag);
						if(popupFlag.equalsIgnoreCase("Y"))
						{
							PL_SKLogger.writeLog("PL Disbursal","Inside popup msg through Exception "+ popupFlag);
							if(popUpControl.equals(""))
							{
								PL_SKLogger.writeLog("PL Disbursal","Before show Exception at front End "+ popupFlag);
								throw new ValidatorException(new FacesMessage(alert_msg));
								//try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));}finally{hm1.clear();}
							}else
							{
								throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

							}
							
						}
						else{
						
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
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}

