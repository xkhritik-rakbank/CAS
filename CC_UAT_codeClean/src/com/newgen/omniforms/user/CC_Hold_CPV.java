
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects
Project/Product                                                   : Rakbank  
Application                                                       : Credit Card
Module                                                            : Hold CPV
File Name                                                         : CC_Hold_CPV
Author                                                            : Disha 
Date (DD/MM/YYYY)                                                 : 
Description                                                       : 
-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_Hold_CPV extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		
		SKLogger_CC.writeLog("CC HOLD_CPV", "Inside formLoaded()" + pEvent.getSource().getName());
		
		
		makeSheetsInvisible(tabName, "9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
     try{
    	 SKLogger_CC.writeLog("CC HOLD_CPV","Inside CC_Hold_CPV CC");
    	 new CC_CommonCode().setFormHeader(pEvent);
        }catch(Exception e)
        {
            SKLogger_CC.writeLog("CC HOLD_CPV", "Exception:"+e.getMessage());
        }
        
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg="";
		SKLogger_CC.writeLog("Inside CC HOLD_CPV eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					SKLogger_CC.writeLog(" In CC HOLD_CPV eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);						

					 		break;
					
						case FRAGMENT_LOADED:
								SKLogger_CC.writeLog(" In CC HOLD_CPV eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
							
							formObject.setLocked("Customer_Frame1",true);
							//setDisabled();
						}	
						
						if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
							LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
							LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
							formObject.setLocked("Product_Frame1",true);
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
							formObject.setLocked("AddressDetails_Frame1",true);
							loadPicklist_Address();
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
							formObject.setLocked("AltContactDetails_Frame1",true);
							
						}
						
						
						
						if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
							formObject.setLocked("OECD_Frame8",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
							formObject.setLocked("KYC_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
							formObject.setLocked("FATCA_Frame6",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
							formObject.setLocked("SupplementCardDetails_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
							formObject.setLocked("WorldCheck1_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
							formObject.setLocked("CardDetails_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
							formObject.setLocked("IncomeDetails_Frame1",true);
							LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
				               formObject.setLocked("CompanyDetails_Frame1", true);
				               
								/*LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
				                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");*/
				               loadPicklist_CompanyDet();
				            }
						if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
			            	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
				            
			            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			            }
						if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
							formObject.setLocked("PartnerDetails_Frame1", true);
			                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			            }
						if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
							formObject.setLocked("EMploymentDetails_Frame1", true);
							
							loadPicklistEmployment();
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
			               
			             if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){
			    					if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
			    						formObject.setVisible("FinacleCore_avgbal", true);
			    						SKLogger_CC.writeLog("CC", "set visible FinacleCore_Frame8 if condition ");
			    						formObject.setVisible("FinacleCore_Frame8", true);
			    						SKLogger_CC.writeLog("CC", "after set visible FinacleCore_Frame8 if condition ");
			    						formObject.setVisible("FinacleCore_Frame2", false);
			    						formObject.setVisible("FinacleCore_Frame3", false);
			    						SKLogger_CC.writeLog("CC", "Inside fianacle core fragment if condition ");
			    					}
			    					else{
			    						formObject.setVisible("FinacleCore_Frame2", true);
			    						formObject.setVisible("FinacleCore_Frame3", true);
			    						formObject.setVisible("FinacleCore_avgbal", false);
			    						SKLogger_CC.writeLog("CC", "ELSE set visible FinacleCore_Frame8 ELSE condition ");
			    						formObject.setVisible("FinacleCore_Frame8", false);
			    						SKLogger_CC.writeLog("CC", "AFTER  Inside fianacle core fragment else condition ");
			    					}
			    				}
			             		formObject.setLocked("FinacleCore_Frame1", true);
			    			
			            }
						if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
							//formObject.setNGFrameState("ProductContainer", 0);
							
							
							LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
							
							formObject.setLocked("PartMatch_Frame1", true);
		                								
			            }
						if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
							formObject.setLocked("Reference_Details_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
							formObject.setLocked("ExtLiability_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
							formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
							formObject.setLocked("FinacleCRMIncident_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
							formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
							
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
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {
							formObject.setLocked("SmartCheck_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
							formObject.setLocked("PostDisbursal_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
							formObject.setLocked("IncomingDocument_Frame1",true);
							
						}
						//added by yash on 24/8/2017
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
							formObject.setVisible("NotepadDetails_SaveButton",true);
							formObject.setTop("NotepadDetails_SaveButton",400);
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
								
							formObject.setVisible("DecisionHistory_CheckBox1",false);
	                        formObject.setVisible("DecisionHistory_Label1",false);
	                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);
	                        formObject.setVisible("DecisionHistory_Label3",false);
	                        formObject.setVisible("DecisionHistory_Combo3",false);
	                        formObject.setVisible("DecisionHistory_Label6",false);
	                        formObject.setVisible("DecisionHistory_Combo6",false);
	                        formObject.setVisible("DecisionHistory_Decision_Label4",false);
	                        formObject.setVisible("cmplx_DEC_Remarks",false);
	                        formObject.setVisible("DecisionHistory_Label8",false);
	                        formObject.setVisible("DecisionHistory_Text4",false);
	                        formObject.setVisible("DecisionHistory_Label7",false);
	                        formObject.setVisible("DecisionHistory_Text3",false);
	                        formObject.setVisible("DecisionHistory_Label2",false);
	                        formObject.setVisible("DecisionHistory_Text2",false);
	                        formObject.setVisible("cmplx_DEC_ReferReason",false);
	                        formObject.setVisible("cmplx_DEC_Description",false);
	                        formObject.setVisible("cmplx_DEC_Strength",false);
	                        formObject.setVisible("cmplx_DEC_Weakness",false);
	                        formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
	                       
	                        
	                        loadPicklist3();
			                	//loadPicklist1();
						 } 	
							
					
					  break;
					  
					case MOUSE_CLICKED:
						SKLogger_CC.writeLog(" In CC HOLD_CPV eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
							//GenerateXML();
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
							formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							SKLogger_CC.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

						}

					
						if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
							SKLogger_CC.writeLog("CC HOLD_CPV", "Inside Customer_save button: ");
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
						// ++ below code not commented at offshore - 06-10-2017
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
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
							formObject.saveFragment("DecisionHistory");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification_Button1")){
							formObject.saveFragment("HomeCountry_Verification");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification_Button1")){
							formObject.saveFragment("Customer_Details_Verification");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification_Button1")){
							formObject.saveFragment("Business_Verification");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification_Button1")){
							formObject.saveFragment("ResidenceVerification");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification_Button1")){
							formObject.saveFragment("Guarantor_Verification");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification_Button1")){
							formObject.saveFragment("Reference_Detail_Verification");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_Button1")){
							formObject.saveFragment("Office_Mob_Verification");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("LoanandCard_Button1")){
							formObject.saveFragment("LoanCard_Details_Check");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button3")){
							formObject.saveFragment("Notepad_Details");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck_Button1")){
							formObject.saveFragment("Smart_Check");
						}
						//added by yash on 24/8/2017
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
							formObject.saveFragment("Notepad_Values");
							alert_msg="Notepad Details Saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
							alert_msg="Notepad Details Saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_save")){
							formObject.saveFragment("DecisionHistory");
							alert_msg="Decision History Details Saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
							formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						//ended by yash on 24/8/2017
						
						
						
				
					
					 case VALUE_CHANGED:
							SKLogger_CC.writeLog(" In CC HOLD_CPV eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In CC HOLD_CPV VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In CC HOLD_CPV VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
								 	  }
							 	}
							 
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
		
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}

}

