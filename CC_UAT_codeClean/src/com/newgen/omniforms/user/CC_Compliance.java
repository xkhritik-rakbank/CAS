
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Compliance

File Name                                                         : CC_Compliance

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
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_Compliance extends CC_Common implements FormListener
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
		
		SKLogger_CC.writeLog("CC Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "8,9,11,12,14,15,16,17");
		formObject.setVisible("Compliance_Frame2",false);
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 SKLogger_CC.writeLog("CC_Common","Inside CC_Hold_CPV CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            SKLogger_CC.writeLog("RLOS Initiation", "Exception:"+e.getMessage());
	        }
	        
	    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		new HashMap<String,String>();
		String alert_msg="";
		SKLogger_CC.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	  formObject =FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				SKLogger_CC.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().FrameExpandEvent(pEvent);						
				
				break;
                
          case FRAGMENT_LOADED:
        	  SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
      			
				}*/
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						formObject.setLocked("Customer_Frame1",true);
						//formObject.setLocked("Product_Frame1",true);
						/*formObject.setEnabled("Customer_save",true);
						formObject.setLocked("cmplx_Customer_ReferrorCode",false);
						formObject.setLocked("cmplx_Customer_ReferrorName",false);
						formObject.setLocked("cmplx_Customer_AppType",false);
						formObject.setLocked("cmplx_Customer_corporateCode",false);
						formObject.setLocked("cmplx_Customer_Bankingwithus",false);
						formObject.setLocked("cmplx_Customer_noofDependent",false);
						formObject.setLocked("cmplx_Customer_guardian",false);
						formObject.setLocked("cmplx_Customer_minor",false);
						formObject.setEnabled("Customer_Reference_Add",true);
						formObject.setEnabled("Customer_Reference__modify",true);
						formObject.setEnabled("Customer_Reference_delete",true);*/
						loadPicklistCustomer();
						
					}	
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
						/*
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
						*/
						formObject.setLocked("Product_Frame1",true);
						/*formObject.setEnabled("Product_Save",true);
						formObject.setEnabled("Product_Add",true);
						formObject.setEnabled("Product_Modify",true);
						formObject.setEnabled("Product_Delete",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
						formObject.setLocked("IncomeDetails_Frame1",true);
						//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
						loadpicklist_Income();
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						formObject.setLocked("ExtLiability_Frame1",true);
						/*formObject.setEnabled("Liability_New_AECBReport",true);
						formObject.setEnabled("Liability_New_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						formObject.setLocked("EMploymentDetails_Frame1",true);
						loadPicklistEmployment();
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
						formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
						/*formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
						loadPicklist_Address();
						formObject.setLocked("AddressDetails_Frame1",true);
						//loadPicklist_Address();
						/*formObject.setEnabled("AddressDetails_Save",true);
						formObject.setEnabled("AddressDetails_addr_Add",true);
						formObject.setEnabled("AddressDetails_addr_Modify",true);
						formObject.setEnabled("AddressDetails_addr_Delete",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
						
						formObject.setLocked("AltContactDetails_Frame1",true);
						/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
					} 
					
					if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
						
						formObject.setLocked("FATCA_Frame1",true);
						//12thsept
						formObject.setLocked("FATCA_Frame6",true);
						//12thsept
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){
						
						formObject.setLocked("KYC_Frame1",true);
						/*formObject.setEnabled("KYC_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
						//12th sept
						//formObject.setLocked("OECD_Frame1",true);
						formObject.setLocked("OECD_Frame8",true);
						//12th sept
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")){
						
						formObject.setLocked("IncomingDocument_Frame",true);
						/*formObject.setEnabled("OECD_Save",true);*/
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
						//12thsept
						//formObject.setLocked("Reference_Details_Frame1",true);
						formObject.setLocked("Reference_Details_ReferenceRelationship",true);
						//12th sept
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
						formObject.setLocked("SupplementCardDetails_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
						formObject.setLocked("CardDetails_Frame1",true);
						
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
			                
			            */	loadPicklist_CompanyDet();
			               }
					if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
		            	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			            // formObject.setLocked("AuthorisedSignDetails_Button4", true);
		            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						}
					if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
						formObject.setLocked("PartnerDetails_Frame1", true);
		                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		            }
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						formObject.setLocked("ExtLiability_Frame1",true);
						
					}
					//added by yash on 22/8/2017
					if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
						//12th sept
						 notepad_load();
						 notepad_withoutTelLog();
						 //12th sept
						 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
						 SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName);
				        formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
				        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
				    	
						formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
						formObject.setLocked("NotepadDetails_noteDate",true);
						formObject.setLocked("NotepadDetails_Actusername",true);
						formObject.setLocked("NotepadDetails_user",true);
						formObject.setLocked("NotepadDetails_insqueue",true);
						formObject.setLocked("NotepadDetails_Actdate",true);
						formObject.setVisible("NotepadDetails_SaveButton",true);
						formObject.setTop("NotepadDetails_SaveButton",400);
					}
					//ended by yash
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						
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
	                        //12th september
	                        formObject.setVisible("cmplx_DEC_IBAN_No",false);
                    		formObject.setVisible("DecisionHistory_Label5",false);
            				formObject.setVisible("cmplx_DEC_NewAccNo",false);
    						formObject.setVisible("DecisionHistory_Decision_Label1",false);
							formObject.setVisible("DecisionHistory_Label10",false);
							formObject.setVisible("cmplx_DEC_New_CIFID",false);
							formObject.setVisible("DecisionHistory_chqbook",false);
							formObject.setVisible("cmplx_DEC_ChequebookRef",false);
							formObject.setVisible("DecisionHistory_Label4",false);
							formObject.setVisible("DecisionHistory_Label27",false);
							formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
							formObject.setVisible("DecisionHistory_Label9",false);
							formObject.setVisible("cmplx_DEC_DCR_Refno",false);
							formObject.setVisible("DecisionHistory_Decision_Label4",true);
							formObject.setVisible("DecisionHistory_Label10",false);
							formObject.setVisible("cmplx_DEC_New_CIFID",false);
							formObject.setVisible("cmplx_DEC_Remarks",true);
							formObject.setTop("DecisionHistory_Decision_Label4",70);
							formObject.setLeft("DecisionHistory_Decision_Label4",480);
							formObject.setTop("cmplx_DEC_Remarks",84);
							formObject.setLeft("cmplx_DEC_Remarks",480);
							formObject.setTop("DecisionHistory_save",200);
							formObject.setTop("DecisionHistory_Decision_Label3",70);
							formObject.setTop("cmplx_DEC_Decision",84);
							 //12th september
	                        formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
	                    	formObject.setVisible("DecisionHistory_Label10", false);
	        				formObject.setVisible("cmplx_DEC_New_CIFID", false);
	        				formObject.setTop("DecisionHistory_Decision_ListView1",180);
	        				formObject.setTop("DecisionHistory_save",250);
	        				formObject.setTop("cmplx_DEC_Decision_Reasoncode",72);//rsn code
	               		 	formObject.setTop("DecisionHistory_Label11",56);//rsn code label
	                        
	                        loadPicklist3();
					 }
				 } 	
					if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
						formObject.setLocked("WorldCheck1_Frame1",true);
						loadInWorldGrid();
					}
					//12th sept
					if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
						//loadPicklist_Address();
						formObject.setLocked("PartMatch_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
						//loadPicklist_Address();
						formObject.setLocked("FinacleCRMIncident_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
						//loadPicklist_Address();
						formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
						//loadPicklist_Address();
						formObject.setLocked("ExternalBlackList_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
						//loadPicklist_Address();
						formObject.setLocked("FinacleCore_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
						//loadPicklist_Address();
						formObject.setLocked("MOL1_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
						//loadPicklist_Address();
						formObject.setLocked("RejectEnq_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
						formObject.setLocked("SalaryEnq_Frame1",true);
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")) {
						formObject.setLocked("CC_Loan_Frame1",true);
						formObject.setLocked("CC_Loan_Frame2",true);
						formObject.setLocked("CC_Loan_Frame3",true);
						formObject.setLocked("totBalTransfer",true);
					}
					
					//12th sept
				if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
                    
                    formObject.setLocked("Compliance_Frame1",true);
                    formObject.setLocked("Compliance_ComplianceRemarks",false);
                    //12th september
                    formObject.setLocked("Compliance_Remarks",true);
                    formObject.setLocked("Compliance_Decision",true);
                    //12th september
                   // formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);
                    formObject.setLocked("Compliance_Save",false); 
                    formObject.setLocked("Compliance_Button2",false); 
                    
                    formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
        			
       			 formObject.setNGFrameState("World_Check",0);
       			 int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
       			 SKLogger_CC.writeLog("CC value of world check row","value of n "+n);
       			 if(n>0)
       			 { 
       				 SKLogger_CC.writeLog("CC value of world check row grid to get values","inside world check grid--- ");
       				String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,12);
       				// String UID =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),12);
       				SKLogger_CC.writeLog("CC value of world check UID","UID "+UID);
       				formObject.setNGValue("Compliance_UID",UID);
       				
       				String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,15);
       				// String EI =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),15);
       				 SKLogger_CC.writeLog("CC value of world check EI ","EI "+EI);
       				formObject.setNGValue("Compliance_EI",EI);
       				// String Name =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),0);
       				String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,0);
       				 SKLogger_CC.writeLog("CC value of world check Name","Name "+Name);	
       				formObject.setNGValue("Compliance_Name",Name);
       				 //String Dob =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),2);
       				String Dob = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,2);
       				 SKLogger_CC.writeLog("CC value of world check Dob","Dob "+Dob);
       				formObject.setNGValue("Compliance_DOB",Dob);
       				String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,7);
       				// String Citizenship =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),7);
       				 SKLogger_CC.writeLog("CC value of world check Citizenship","Citizenship"+Citizenship);	
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
       				
       				formObject.setNGValue("Compliance_Winame",formObject.getWFWorkitemName());
       				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");
   				 	 formObject.RaiseEvent("WFSave");
       			 }
       			formObject.setNGFrameState("World_Check",1);
       			//12th september
       			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "befor locking frame2");
       			formObject.setLocked("Compliance_Frame2",true);
       			formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);
       			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "after locking frame 2");
       			
       			//12th september
                    
                }
			
			
			  break;
			  
			case MOUSE_CLICKED:
				SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
					//GenerateXML();
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("Compliance_Button2"))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Compliance_cmplx_gr_compliance");
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
					formObject.saveFragment("Supplementary_Cont");
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
					formObject.saveFragment("DecisionHistoryContainer");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("Compliance_Save")){
					formObject.saveFragment("Compliance");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
					formObject.saveFragment("DecisionHistory");
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
					//12th sept
					Notepad_add();
					//12th sept
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
					//12th sept
					Notepad_modify();
					//12th sept
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
					//12th sept
					Notepad_delete();
					//12th sept
				}
				//12th sept
				if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
					
					Notepad_grid();
					
				} 
				//12th sept
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
					formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				//ended by yash on 24/8/2017
				
			
			 case VALUE_CHANGED:
					SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_DEC_Decision")) {
						 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
						 {
							 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_DEC_Decision"));
							SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("cmplx_DEC_Decision"));

						 }
						 
						 else{
							 
							formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
							SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("cmplx_DEC_Decision"));
						 	  }
					 	}
          
					 //added by yash on 30/8/2017
					 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
						 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
						 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
						 String sQuery = "select code from ng_master_notedescription where Description='" +  notepad_desc + "'";
						 SKLogger_CC.writeLog(" query is ",sQuery);
						 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
						 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
						 {
							 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
						 }
						 
					
						 
					 }
          
         
                  default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		  formObject =FormContext.getCurrentInstance().getFormReference();
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		SKLogger_CC.writeLog("PersonnalLoanS","Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
	}

}

