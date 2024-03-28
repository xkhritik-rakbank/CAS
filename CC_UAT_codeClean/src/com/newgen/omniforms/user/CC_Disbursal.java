
package com.newgen.omniforms.user;

import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_Disbursal extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	 CC_Integration_Input Intgration_input = new CC_Integration_Input();
	CC_Integration_Output Intgration_Output = new CC_Integration_Output();
	public void formLoaded(FormEvent pEvent)
	{
		
		SKLogger_CC.writeLog("CC CC_Disbursal", "Inside formLoaded()" + pEvent.getSource().getName());
		
		makeSheetsInvisible(tabName, "8,9,11,13,14,15,16,17");
		
	}
	
	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 SKLogger_CC.writeLog("CC_Disbursal","Inside CC_Disbursal CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            SKLogger_CC.writeLog("RLOS CC_Disbursal", "Exception:"+e.getMessage());
	        }
	        
	    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String alert_msg="";
		String outputResponse = "";
		String ReturnCode="";
        String ReturnDesc="";
        SKLogger_CC.writeLog("Inside CC_Disbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
						SKLogger_CC.writeLog(" In CC_Disbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().FrameExpandEvent(pEvent);						
						
						break;
					
					case FRAGMENT_LOADED:
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
							loadPicklistCustomer();
							formObject.setLocked("Customer_Frame1",true);
							if ((formObject.getNGValue("cmplx_Customer_NEP"))=="true"){
								formObject.setNGValue("cmplx_Customer_EmirateIDExpiry", "");
							}
							//setDisabled();
						}	
						
						if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
							//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
							/*
							LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
							LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
							*/
							formObject.setLocked("Product_Frame1",true);
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
							formObject.setLocked("AddressDetails_Frame1",true);
							loadPicklist_Address();
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
							formObject.setLocked("AltContactDetails_Frame1",true);
							
						}
						//added by yash for displaying cifid and customer name in Limit increase fragment on 19/6/2017
						//COMMENTED BY ABHISHEK AS PER cc fsd
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc")) 
						{
							formObject.setLocked("Limit_Inc_Frame4",false);
                              formObject.setNGValue("cmplx_LimitInc_CIF",formObject.getNGValue("cmplx_Customer_CIFNO"));
                              
                              SKLogger_CC.writeLog("cifid", "set visible FinacleCore_Frame8 if condition ");
				              formObject.setNGValue("cmplx_LimitInc_CustomerName",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
				              int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
								if(n>0){
									for(int i=0;i<n;i++)
									{

										String s = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
										SKLogger_CC.writeLog("s", s);
										if((s.equalsIgnoreCase("Limit Increase")))
										{
											SKLogger_CC.writeLog("cifid1", "set visible FinacleCore_Frame8 if condition ");
											//PL_SKLogger.writeLog(" In PL_CC_Disbursal eventDispatched()", "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
											formObject.setVisible("cmplx_LimitInc_CurrentLimit", true);
											formObject.setNGValue("cmplx_LimitInc_CurrentLimit",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3));
											formObject.setVisible("cmplx_LimitInc_New_Limit", true);
											formObject.setVisible("cmplx_LimitInc_LimitExpiryDate", true);
											formObject.setVisible("Limit_Inc_Label19", true);
											formObject.setVisible("Limit_Inc_Label20", true);
											formObject.setVisible("Limit_Inc_Label23", true);
											formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", false);
											formObject.setVisible("cmplx_LimitInc_NewCardProduct", false);
											formObject.setVisible("Limit_Inc_Label24", false);
											formObject.setVisible("Limit_Inc_Label25", false);
											//formObject.setVisible("DecisionHistory_updcust", true);
											//PL_SKLogger.writeLog(" In PL_CC_Disbursal eventDispatched()", "after making buttons visible");

										}
										else if(s.equalsIgnoreCase("Product Upgrade"))
										{
											formObject.setVisible("cmplx_LimitInc_CurrentLimit", false);
											formObject.setVisible("cmplx_LimitInc_New_Limit", false);
											formObject.setVisible("cmplx_LimitInc_LimitExpiryDate", false);
											formObject.setVisible("Limit_Inc_Label19", false);
											formObject.setVisible("Limit_Inc_Label20", false);
											formObject.setVisible("Limit_Inc_Label23", false);
											formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", true);
											formObject.setVisible("cmplx_LimitInc_NewCardProduct", true);
											formObject.setVisible("Limit_Inc_Label24", true);
											formObject.setVisible("Limit_Inc_Label25", true);

										}
										else if((s.equalsIgnoreCase("Product Upgrade with Limit inc")))
										{
											formObject.setVisible("cmplx_LimitInc_CurrentLimit", true);
											formObject.setNGValue("cmplx_LimitInc_CurrentLimit",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3));
											formObject.setVisible("cmplx_LimitInc_New_Limit", true);
											formObject.setVisible("cmplx_LimitInc_LimitExpiryDate", true);
											formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", true);
											formObject.setVisible("cmplx_LimitInc_NewCardProduct", true);
											formObject.setVisible("Limit_Inc_Label19", true);
											formObject.setVisible("Limit_Inc_Label20", true);
											formObject.setVisible("Limit_Inc_Label23", true);
											formObject.setVisible("Limit_Inc_Label25", true);
											formObject.setVisible("Limit_Inc_Label24", true);
										}
									}
							
								}
							
						}*/
						//ended by yash on 19/6/2017
						
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
	                       
	                        formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
	                        formObject.setVisible("DecisionHistory_Button4", true);
	                        loadPicklist3();
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
						if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
							formObject.setLocked("CardDetails_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
							formObject.setLocked("WorldCheck1_Frame1",true);
							
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
			                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
							LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
							}
						if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
							formObject.setLocked("PartnerDetails_Frame1", true);
			                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			            }
						if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
							formObject.setLocked("EMploymentDetails_Frame1", true);
							
							loadPicklist4();
							Fields_ApplicationType_Employment();
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
						if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
							formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
							formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
							formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
							formObject.setLocked("CC_Loan_Frame1",true);
							loadPicklist_ServiceRequest();
						}		
						if (pEvent.getSource().getName().equalsIgnoreCase("CC_Creation")) {
							//formObject.setLocked("Reference_Details_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal")) {
							//formObject.setLocked("Reference_Details_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc")) {
							//formObject.setLocked("Reference_Details_Frame1",true);
							// added by abhishek as per CC FSD
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Limit Increase")
									||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Product Upgrade")
									||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Product Upgrade with Limit increase")
									||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM topup")){
								
								formObject.setLocked("Limit_Inc_Frame4",false);
							}
							else{
								formObject.setLocked("Limit_Inc_Frame4",true);
							}
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("FCU_Decision")) {
							formObject.setLocked("FCU_Decision_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CAD")) {
							formObject.setLocked("CAD_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
							formObject.setLocked("CAD_Decision_Frame1",true);
							
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("OriginalValidation")) {
							formObject.setLocked("OriginalValidation_Frame1",true);
							
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
						// added by abhishek as per CC FSd
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
							notepad_load();
							notepad_withoutTelLog();
							
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {
							formObject.setLocked("SmartCheck_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
							formObject.setLocked("Compliance_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CardCollection")) {
							formObject.setLocked("CardCollection_Frame1",true);
							
						}
						
						// fcu fragments start
						if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1")) {
							formObject.setLocked("CustDetailVerification1_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1")) {
							formObject.setLocked("BussinessVerification1_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification")) {
							formObject.setLocked("EmploymentVerification_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("BankingCheck")) {
							formObject.setLocked("BankingCheck_IFrame1",true);
							
						}
						//commented by abhishek as per CC FSD
						/*if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
							formObject.setLocked("NotepadDetails_Frame1",true);
							
						}*/
						if (pEvent.getSource().getName().equalsIgnoreCase("supervisorsection")) {
							formObject.setLocked("supervisorsection_Frame1",true);
							
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) {
							formObject.setLocked("SmartCheck1_Frame1",true);
							
						}
						
						// fcu fragments end
						if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
							formObject.setLocked("PostDisbursal_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
							formObject.setLocked("IncomingDocument_Frame1",true);
							
						}

					  break;
					  
					case MOUSE_CLICKED:
						SKLogger_CC.writeLog(" In CC_Disbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
							SKLogger_CC.writeLog("CC_Disbursal", "Inside Customer_save button: ");
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
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
							alert_msg="Notepad Details Saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						// added by abhishek as per CC FSd
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
							formObject.saveFragment("Notepad_Values");
							alert_msg="Notepad Details Saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
							formObject.saveFragment("DecisionHistory");
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
						// added by abhishek as per CC FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
							Notepad_add();
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
							Notepad_modify();
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
							Notepad_delete();
						}
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
							
							Notepad_grid();
							
						} 
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
							formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						//ended by yash on 24/8/2017
						if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
							formObject.saveFragment("OECD");
						}
						//added by yash
						if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Card_Services"))
						{
                            formObject.RaiseEvent("WFSave");
                            SKLogger_CC.writeLog("RLOS value of ReturnCode","inside button click");
                            String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
                            SKLogger_CC.writeLog("RLOS value of Product_Name",""+Product_Name);
                            if(Product_Name.equalsIgnoreCase("Limit Change Request")){
                                outputResponse = Intgration_input.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                    SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                                    formObject.setNGValue("Is_CardServices","Y");
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServices"));
                                    SKLogger_CC.writeLog("","CARD SERVICES RUNNING Limit Change Request");
                                    outputResponse = Intgration_input.GenerateXML("CARD_NOTIFICATION","Limit Change Request");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                    SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                        SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                        formObject.setNGValue("Is_CardNotifiaction","Y");
                                        SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiaction"));
                                    }
                                }
                            }
                            //for another product
                            if(Product_Name.equalsIgnoreCase("Financial Services Request")){
                                outputResponse = Intgration_input.GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                    SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                                    formObject.setNGValue("Is_CardServices","Y"); 
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServices"));
                                    SKLogger_CC.writeLog("","CARD SERVICES RUNNING Financial Services Request");
                                    outputResponse = Intgration_input.GenerateXML("CARD_NOTIFICATION","Financial Services Request");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                    SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                        SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                        formObject.setNGValue("Is_CardNotifiaction","Y");
                                        SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiaction"));
                                    }
                                }
                            }
                            //for another product
                            if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
                                outputResponse = Intgration_input.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                    SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                                    formObject.setNGValue("Is_CardServices","Y"); 
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServices"));
                                    SKLogger_CC.writeLog("","CARD SERVICES RUNNING Product Upgrade Request");
                                    outputResponse = Intgration_input.GenerateXML("CARD_NOTIFICATION","Product Upgrade Request");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                    SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                        SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                        formObject.setNGValue("Is_CardNotifiaction","Y");
                                        SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiaction"));
                                    }
                                }
                            }
                            formObject.RaiseEvent("WFSave");
                        }
                    // ended by yash
						if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_Save")){
							formObject.saveFragment("Loan_Disbursal");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Save")){
							formObject.saveFragment("CC_Creation");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc_Save")){
							formObject.saveFragment("Limit_Inc");
						}
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistoryContainer");
						}
					    if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button4")){
							String message = CustomerUpdate();
                            throw new ValidatorException(new FacesMessage(message));
						}
						//started code merged
						if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Button2")){
                            formObject.RaiseEvent("WFSave");
                            SKLogger_CC.writeLog("RLOS value of ReturnCode","inside button click");
                            String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
                            SKLogger_CC.writeLog("RLOS value of Product_Name",""+Product_Name);
                            if(Product_Name.equalsIgnoreCase("Limit Change Request")){
                                outputResponse = Intgration_input.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                    SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                                    formObject.setNGValue("Is_CardServices","Y");
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServices"));
                                    SKLogger_CC.writeLog("","CARD SERVICES RUNNING Limit Change Request");
                                    outputResponse = Intgration_input.GenerateXML("CARD_NOTIFICATION","Limit Change Request");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                    SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                        SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                        formObject.setNGValue("Is_CardNotifiaction","Y");
                                        SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiaction"));
                                    }
                                }
                            }
                            //for another product
                            if(Product_Name.equalsIgnoreCase("Financial Services Request")){
                                outputResponse = Intgration_input.GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                    SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                                    formObject.setNGValue("Is_CardServices","Y"); 
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServices"));
                                    SKLogger_CC.writeLog("","CARD SERVICES RUNNING Financial Services Request");
                                    outputResponse = Intgration_input.GenerateXML("CARD_NOTIFICATION","Financial Services Request");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                    SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                        SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                        formObject.setNGValue("Is_CardNotifiaction","Y");
                                        SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiaction"));
                                    }
                                }
                            }
                            //for another product
                            if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
                                outputResponse = Intgration_input.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                    SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                                    formObject.setNGValue("Is_CardServices","Y"); 
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServices"));
                                    SKLogger_CC.writeLog("","CARD SERVICES RUNNING Product Upgrade Request");
                                    outputResponse = Intgration_input.GenerateXML("CARD_NOTIFICATION","Product Upgrade Request");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                    SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                    SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                        SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                        formObject.setNGValue("Is_CardNotifiaction","Y");
                                        SKLogger_CC.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiaction"));
                                    }
                                }
                            }
                            formObject.RaiseEvent("WFSave");
                        }
                    
						if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Update_Customer")){
                            SKLogger_CC.writeLog("","inside Update_Customer");  
                            outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Inquire");
                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                           SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                           SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                           if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                               SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                              formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
                              SKLogger_CC.writeLog("","Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
                               SKLogger_CC.writeLog("","inside Update_Customer");  
                               String cif_status = (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
                               if(cif_status.equalsIgnoreCase("ACTVE")){
                            outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Lock");
                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                           SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                           SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                           if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                           SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                             formObject.setNGValue("Is_CustLock_Disbursal","Y");
                             SKLogger_CC.writeLog("","Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 
                               
                               SKLogger_CC.writeLog("RLOS value of Customer_Details","Customer_Details is generated");
                           //    SKLogger_CC.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
                                outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","UnLock");
                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                   SKLogger_CC.writeLog("RLOS value of ReturnCode","inside unlock");
                                   SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                   formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
                                   SKLogger_CC.writeLog("","Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
                                   ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                   SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                   if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                        outputResponse = Intgration_input.GenerateXML("CUSTOMER_UPDATE_REQ","");
                                        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                           SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                           ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                           SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                         //  Message =(outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    
                                         //  SKLogger_CC.writeLog("RLOS value of Message",Message);
                                           
                                           if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                               formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
                                               SKLogger_CC.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
                                               Intgration_Output.valueSetCustomer(outputResponse,"");    
                                               SKLogger_CC.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","CUSTOMER_UPDATE_REQ is generated");
                                               SKLogger_CC.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
                                           }
                                           else{
                                               SKLogger_CC.writeLog("Customer Details","CUSTOMER_UPDATE_REQ is not generated");
                                               formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","N");
                                           }
                                           SKLogger_CC.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
                                           formObject.RaiseEvent("WFSave");
                                           SKLogger_CC.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","after saving the flag");
                                           if((formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal").equalsIgnoreCase("Y")))
                                           { 
                                               SKLogger_CC.writeLog("RLOS value of Is_CUSTOMER_UPDATE_REQ","inside if condition");
                                               formObject.setEnabled("Update_Customer", false); 
                                               throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","Update_Customer#Customer Updated Successful!!","",hm));
                                           }
                                           else{
                                               formObject.setEnabled("DecisionHistory_Button3", true);
                                               throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","Update_Customer#Customer Updated Fail!!","",hm));
                                           }
                                   }
                                   else{
                                       SKLogger_CC.writeLog("Customer Details","Customer_Details unlock is not generated");
                                       
                                   }
                                   }
                           else{
                               SKLogger_CC.writeLog("Customer Details","Customer_Details lock is not generated");
                               }
                       }
                               else {
                                 SKLogger_CC.writeLog("DDVT Checker Customer Update operation: ", "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
                                 throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
                             }
                           //added one more here
                 }
               else{
                           SKLogger_CC.writeLog("Customer Details","Customer_Details Inquiry is not generated");
                   }
                }

                    //Ended code merged

						

						break;
					
					 case VALUE_CHANGED:
							SKLogger_CC.writeLog(" In CC_Disbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In CC_Disbursal VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In CC_Disbursal VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
								 	  }
							 	}
							 //added by yash on 30/8/2017
							// Code added to Load targetSegmentCode on basis of code
								if (pEvent.getSource().getName().equalsIgnoreCase("ApplicationCategory")){
									SKLogger_CC.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
									String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
									String appCategory=formObject.getNGValue("ApplicationCategory");
									SKLogger_CC.writeLog("RLOS val change ", "Value of subproduct is:"+formObject.getNGValue("subproduct"));
									if(appCategory!=null &&  appCategory.equalsIgnoreCase("BAU")){
										LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
									}
									else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
										LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
									}
									else{
									LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
									}
								}
								// Code added to Load targetSegmentCode on basis of code
							 
							 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
								 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
								 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
								 String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
								 SKLogger_CC.writeLog(" query is ",sQuery);
								 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
								 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
								 {
									 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
									 SKLogger_CC.writeLog(" notepad details workstep value ",recordList.get(0).get(1));
									 formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
								 }
								 
							
								 
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
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
		
	}

}

