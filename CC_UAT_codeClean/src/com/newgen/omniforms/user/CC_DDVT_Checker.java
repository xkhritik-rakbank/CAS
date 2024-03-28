/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : DDVT Checker

File Name                                                         : CC_DDVT_Checker

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1. 				   9-6-2017      Disha         Change done to Load decisions on the basis of Initiation type 
2.                 9-6-2017      Disha         Changes done for testing purpose EMPLOYMENT_DETAILS_MATCH set to 'Yes'
1007				17-9-2017	Saurabh			changes in Decision History fragment.
------------------------------------------------------------------------------------------------------*/

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

public class CC_DDVT_Checker extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	FormReference formObject = null;
	CC_Integration_Output Intgration_Output = new CC_Integration_Output();
	public void formLoaded(FormEvent pEvent)
	{
		
		SKLogger_CC.writeLog(" CC_DDVT CHECKER", "Inside formLoaded()" + pEvent.getSource().getName());
		
		makeSheetsInvisible(tabName, "8,9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 SKLogger_CC.writeLog("CC_DDVT CHECKER","Inside CC_DDVT CHECKER CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            SKLogger_CC.writeLog("RLOS Initiation", "Exception:"+e.getMessage());
	        }
	        
	    }

/*          Function Header:

	**********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
			 
	             
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments in DDVT Checker         

	***********************************************************************************  */
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String outputResponse = "";
        String    ReturnCode="";
        String alert_msg="";
		SKLogger_CC.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();
              try{

				switch(pEvent.getType())
				{	

				case FRAME_EXPANDED:
					SKLogger_CC.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);						
					break;
					
					case FRAGMENT_LOADED:
						SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
							loadPicklistCustomer();
							formObject.setLocked("Customer_Frame1",true);
							}	
							
							if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
								//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
								/*
								LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
								LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
								LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
								*/
								//code changed to load master's of product based on requested product.
								formObject.setLocked("Product_Frame1",true);
								int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
								if(prd_count>0){
									String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
									loadPicklistProduct(ReqProd);
								}
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
								formObject.setLocked("IncomeDetails_Frame1",true);
								//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
								LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");
								LoadPickList("cmplx_IncomeDetails_CredTurnoverFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");
								LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");
								LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_Frequency");
								
								loadpicklist_Income();
							}

						//21/08/2017 it should be disabled added
							if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
								SKLogger_CC.writeLog("","inside locking of reference details");
								formObject.setLocked("Reference_Details_ReferenceRelationship",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
								SKLogger_CC.writeLog("","inside locking of reject enquiry");
								formObject.setLocked("RejectEnq_Frame1",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
								SKLogger_CC.writeLog("","inside locking of Salary Enquiry");
								formObject.setLocked("SalaryEnq_Frame1",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("CreditCardEnq")) {
								SKLogger_CC.writeLog("","inside locking of Credit Card");
								formObject.setLocked("CreditCardEnq_Frame1",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
								SKLogger_CC.writeLog("","inside locking of LOS_Frame1");
								formObject.setLocked("LOS_Frame1",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
								SKLogger_CC.writeLog("","inside locking of External Blacklist");
								formObject.setLocked("ExternalBlackList_Frame1",true);
								
							}
							
							//21/08/2017 it should be disabled added 

























							if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
								formObject.setLocked("ExtLiability_Frame1",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
								formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
								
							}

							if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
								formObject.setLocked("EMploymentDetails_Frame1", true);
								loadPicklistEmployment();
								
								loadPicklist4();
								// ++ below code already present - 06-10-2017
								// added on 25septfor bug list
								formObject.setVisible("EMploymentDetails_Label33",false);
								formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
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

							if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
					               formObject.setLocked("CompanyDetails_Frame1", true);
					            /*   
									LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
					                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
					            */	loadPicklist_CompanyDet();}
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
							
							if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")){
								
								formObject.setLocked("SalaryEnq_Frame1",true);
								/*formObject.setEnabled("OECD_Save",true);*/
							}
							//21/08/2017 it should be enabled 
							if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
								// added by abhishek as per CC FSD
								formObject.setLocked("WorldCheck1_Frame1",true);
								//formObject.setLocked("WorldCheck1_Frame1",false);
								}
							//21/08/2017 it should be enabled 
									if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
								loadPicklist_ServiceRequest();
								formObject.setLocked("CC_Loan_Frame5",true);
								formObject.setLocked("CC_Loan_Frame4",true);
								formObject.setLocked("CC_Loan_Frame2",true);
								formObject.setLocked("CC_Loan_Frame3",true);
								// added by abhishek as per CC FSD
								formObject.setLocked("CC_Loan_Frame1",true);
							}
							
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")){
								
								formObject.setLocked("FinacleCore_Frame1",true);
								ChangeRepeater();
								ChangeRepeaterTrnover();
								/*formObject.setEnabled("OECD_Save",true);*/







							}

							if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
								//formObject.setNGFrameState("ProductContainer", 0);
								
								
								LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
								
								formObject.setLocked("PartMatch_Frame1", true);
			                								
				            }










							if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
								formObject.setLocked("SupplementCardDetails_Frame1",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
								formObject.setLocked("CardDetails_Frame1",true);
								
							}
							if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
								formObject.setLocked("IncomingDocument_Frame1",true);
								
							}
							//++ Below Code added By Yash on Oct 6, 2017  to fix : "3-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++
							if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
								notepad_load();
								notepad_withoutTelLog();
							}
							//ended by yash
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	
								/*formObject.setVisible("DecisionHistory_CheckBox1",false);
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
		                        formObject.setVisible("DecisionHistory_Label11",false);
		                        
		                        formObject.setVisible("DecisionHistory_Button3",true);
		                       */
							   // ++ below code not commented at offshore - 06-10-2017
								formObject.setVisible("DecisionHistory_CheckBox1",false);
		                        formObject.setVisible("DecisionHistory_Label1",true);
		                        formObject.setVisible("cmplx_DEC_VerificationRequired",true);
		                        formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
		                        formObject.setVisible("cmplx_DEC_ContactPointVeri",true);
		                        formObject.setVisible("DecisionHistory_Label3",true);
		                        formObject.setVisible("DecisionHistory_Combo3",true);
		                        formObject.setVisible("DecisionHistory_Label6",true);
		                        formObject.setVisible("DecisionHistory_Combo6",true);
		                        formObject.setVisible("DecisionHistory_Decision_Label4",true);
		                        formObject.setVisible("cmplx_DEC_Remarks",true);
		                        formObject.setVisible("DecisionHistory_Label8",true);
		                        formObject.setVisible("DecisionHistory_Text4",true);
		                        formObject.setVisible("DecisionHistory_Label7",true);
		                        formObject.setVisible("DecisionHistory_Text3",true);
								formObject.setVisible("DecisionHistory_Label2",true);
		                        
		                        formObject.setVisible("DecisionHistory_Text2",true);
		                        formObject.setVisible("cmplx_DEC_ReferReason",true);
		                        formObject.setVisible("cmplx_DEC_RejectReason",true);
								// ++ above code not commented at offshore - 06-10-2017
								
								SKLogger_CC.writeLog("CC_DDVT_CHECKER", "inside decision history fragment load");
		                        if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true") && formObject.getNGValue("cmplx_DEC_New_CIFID").equalsIgnoreCase("")){
		                        	formObject.setVisible("DecisionHistory_Button2",true);	
		                        }
		                        else{
		                        	formObject.setVisible("DecisionHistory_Button2",false);
		                        }
		                        formObject.setVisible("cmplx_DEC_Description",true);
		                        formObject.setVisible("cmplx_DEC_Strength",true);
		                        formObject.setVisible("cmplx_DEC_Weakness",true);
								// ++ below code not commented at offshore - 06-10-2017
								 formObject.setVisible("DecisionHistory_chqbook",true);			
		                       
							   formObject.setEnabled("DecisionHistory_Button3",false);
								//Change done to Load decisions on the basis of Initiation type 
			        			if(formObject.getNGValue("InitiationType").equalsIgnoreCase("Telesales_Init"))
			        			{
			        				loadPicklist3();
			        			}
			        			else
			        			{
			        				loadPicklistChecker();
			        			}
			        			//Ref. 1007.
			        			formObject.setVisible("cmplx_DEC_ContactPointVeri", false);
			        			SKLogger_CC.writeLog("CC_DDVT_CHECKER", "1");
			    				
			    				formObject.setVisible("DecisionHistory_chqbook", false);
			    				formObject.setVisible("DecisionHistory_Label6", false);
			    				formObject.setVisible("cmplx_DEC_IBAN_No", false);
			    				formObject.setVisible("DecisionHistory_Label7", false);
			    				formObject.setVisible("cmplx_DEC_NewAccNo", false);
			    				SKLogger_CC.writeLog("CC_DDVT_CHECKER", "2");
			    				formObject.setVisible("DecisionHistory_Label8", false);
			    				formObject.setVisible("cmplx_DEC_ChequebookRef", false);
			    				formObject.setVisible("DecisionHistory_Label9", false);
			    				formObject.setVisible("cmplx_DEC_DCR_Refno", false);
			    				formObject.setVisible("DecisionHistory_Label27", false);
			    				SKLogger_CC.writeLog("CC_DDVT_CHECKER", "3");
			    				formObject.setVisible("cmplx_DEC_Cust_Contacted", false);
			    				
			    				formObject.setTop("DecisionHistory_Decision_ListView1",330);
			    				//Ref. 1007 end.
			    				SKLogger_CC.writeLog("CC_DDVT_CHECKER", "end of decision history fragment load");
						 } 	
					
// added by abhishek as per CC FSD
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
								formObject.setLocked("FinacleCRMIncident_Frame1", true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
								formObject.setLocked("FinacleCRMCustInfo_Frame1", true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
								formObject.setLocked("MOL1_Frame1", true);
							}
					  break;
					  
					case MOUSE_CLICKED:
						SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
							SKLogger_CC.writeLog("PL_Initiation", "Inside Customer_save button: ");
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
						//added by yash on 24/8/2017
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
							formObject.saveFragment("Notepad_Values");
							popupFlag = "Y";
							alert_msg="Notepad Details Saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
						formObject.saveFragment("Notepad_Values");
						popupFlag = "Y";
						alert_msg="Notepad Details Saved";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_save")){
						formObject.saveFragment("DecisionHistory");
						popupFlag = "Y";
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
					// added by abhishek as per CC FSD
					if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
						Notepad_grid();
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
							formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
							formObject.saveFragment("DecisionHistory");
						}
						
						//ended by yash on 24/8/2017
						
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
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
							formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						//started code merged
						// commented by abhishek 26may2017
						/*if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3")){
                            SKLogger_CC.writeLog("PL value of ReturnDesc","Inside  New Customer Req");

                           if((formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")) && ((formObject.getNGValue("cmplx_Customer_CIFNO").equalsIgnoreCase("")))||(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true")) && ((formObject.getNGValue("cmplx_Customer_CIFNO").equalsIgnoreCase(""))))

                           {
                               outputResponse = GenerateXML("NEW_CUSTOMER_REQ","");
                               ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                               SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                               ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                               SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                               if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                   valueSetCustomer(outputResponse);    
                                   SKLogger_CC.writeLog("PL value of ReturnDesc","Inside if of New customer Req");
                               //    formObject.setNGValue("Is_Customer_Create","Y");
                                   outputResponse = GenerateXML("CUSTOMER_DETAILS","Inquire");
                                   ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                  SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                  ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                  SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                  if(ReturnCode.equalsIgnoreCase("0000")){
                                      SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
                                     formObject.setNGValue("Is_CustInquiry","Y"); 
                                     SKLogger_CC.writeLog("","Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry")); 
                                      SKLogger_CC.writeLog("","inside Update_Customer");  
                                   String cif_status = (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
                                    if(cif_status.equalsIgnoreCase("ACTVE")){
                                   outputResponse = GenerateXML("CUSTOMER_DETAILS","Lock");
                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                       SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                       SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                       if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                       SKLogger_CC.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
                                         formObject.setNGValue("Is_CustLock","Y");
                                           
                                           SKLogger_CC.writeLog("RLOS value of Customer_Details","Customer_Details is generated"+formObject);
                                      
                                            outputResponse = GenerateXML("CUSTOMER_DETAILS","UnLock");
                                            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                               SKLogger_CC.writeLog("RLOS value of ReturnCode","inside unlock");
                                               SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
                                               ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                               SKLogger_CC.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                               if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")  ){
                                                   SKLogger_CC.writeLog("Customer Details","Successfully Unlock !!!!!!!!!!");
                                               }
                                               else
                                                   SKLogger_CC.writeLog("Customer Details","Failed Unlock !!!!!!!!!!");
                                               
                                       }
                                       else{
                                           SKLogger_CC.writeLog("Customer Details","Customer_Details lock is not generated");
                                          
                                       }
                                       //added
                                       
                                        }
                                         else{
                                             SKLogger_CC.writeLog("DDVT Checker Customer Update operation: ", "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
                                             
                                             popupFlag = "Y";
                                            throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
                                         }
                                    }
                                     else{
                                         SKLogger_CC.writeLog("RLOS value of Customer_Details","Account is not retained");
                                    }
                                        
                                   
                               }
                               else{
                                   SKLogger_CC.writeLog("PL value of ReturnDesc","Inside else of New Customer Req");
                                   //formObject.setNGValue("Is_Customer_Create","N");
                               }
                           }
                        }*/
						
						
						 if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button2"))
		                    {
							 CC_Integ.getCustAddress_details();
		                        hm.put("Liability_New_Button1","Clicked");
		                        popupFlag="N";
		                        //added
		                        String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
		                        String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
		                        String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNo");
		                        SKLogger_CC.writeLog("CC_Initiation: ", "inside create Account/Customer NTB value: "+NTB_flag );
		                        if(NTB_flag.equalsIgnoreCase("true") || NEP_flag.equalsIgnoreCase("true")||CIF_no.equalsIgnoreCase("")){
		                        	formObject.setNGValue("curr_user_name",formObject.getUserName());
		                        	int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		                        	//Changes Done for Create Customer self Employed Case
		                        	
		                        	String Emp_type = "SAL";
		            				if(prd_count>0){
		            					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)!=null &&( formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed")||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("SE")) ){
		            						Emp_type = "SE";
		            					}
		            				}
		                             outputResponse = CC_Integ.GenerateXML("NEW_CUSTOMER_REQ",Emp_type);
		                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
		                            SKLogger_CC.writeLog("CC value of ReturnCode",ReturnCode);
		                            if(ReturnCode.equalsIgnoreCase("0000")){
		                                Intgration_Output.valueSetCustomer(outputResponse,"");   
		                                formObject.setNGValue("cmplx_DEC_New_CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		                                SKLogger_CC.writeLog("CC value of ReturnDesc","Inside if of New customer Req");
		                                formObject.RaiseEvent("WFSave");

			                        	//outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
			 	                       // ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			 	                        SKLogger_CC.writeLog("CC value of ReturnCode",ReturnCode);
			 	                        
			 	                        // if(ReturnCode.equalsIgnoreCase("0000") ){
			 	                            // valueSetCustomer(outputResponse);    
			 	                            // formObject.setNGValue("Is_Account_Create","Y");
			 	                            // formObject.setNGValue("EligibilityStatus","Y");
			 	                            // formObject.setNGValue("EligibilityStatusCode","Y");
			 	                            // formObject.setNGValue("EligibilityStatusDesc","Y");
			 	                            // }
			 	                            // else{
			 	                                // formObject.setNGValue("Is_Account_Create","N");
			 	                            // }
			 	                        SKLogger_CC.writeLog("CC value of Account Request",formObject.getNGValue("Is_Account_Create"));
			 	                        SKLogger_CC.writeLog("CC value of EligibilityStatus",formObject.getNGValue("EligibilityStatus"));
			 	                        SKLogger_CC.writeLog("CC value of EligibilityStatusCode",formObject.getNGValue("EligibilityStatusCode"));
			 	                        SKLogger_CC.writeLog("CC value of EligibilityStatusDesc",formObject.getNGValue("EligibilityStatusDesc"));
			 	                       alert_msg = "Create Cif ID Successful";
			                        
		                                
		                                }
		                                else{
		                                    SKLogger_CC.writeLog("CC value of ReturnDesc","Inside else of New Customer Req");
		                                    alert_msg = "Create Cif ID failed. Contact Administrator";
		                                }
		                        }
		                        
		                      
		                        popupFlag = "Y";
		                           
	                            throw new ValidatorException(new FacesMessage(alert_msg));
		                }

						
						
						//Ended code merged
						break;
					 case VALUE_CHANGED:
							SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
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
				
              }catch(Exception ex)
				{
					 SKLogger_CC.writeLog("CC_DDVT CHECKER","Inside Exception to show msg at front end");
					 HashMap<String,String> hm1=new HashMap<String,String>();
						hm1.put("Error","Checked");
					 if(ex instanceof ValidatorException)
						{   SKLogger_CC.writeLog("CC_DDVT CHECKER","popupFlag value: "+ popupFlag);
							if(popupFlag.equalsIgnoreCase("Y"))
							{
								SKLogger_CC.writeLog("CC_DDVT CHECKER","Inside popup msg through Exception "+ popupFlag);
								if(popUpControl.equals(""))
								{
									SKLogger_CC.writeLog("CC DDVY maker","Before show Exception at front End "+ popupFlag);
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
					SKLogger_CC.writeLog("CC_DDVT CHECKER","exception in eventdispatched="+ ex);
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

		formObject.setNGValue("EMPLOYMENT_DETAILS_MATCH","Yes");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_DEC_Decision"));		
		saveIndecisionGrid();
	}

}

