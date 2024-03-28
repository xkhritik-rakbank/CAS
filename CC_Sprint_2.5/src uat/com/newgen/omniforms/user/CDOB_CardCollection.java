 
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Card Collection

File Name                                                         : CC_CardCollection

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


import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


//Changes - 1. On Sales coordinator Incoming Document frameCardCollection
public class CDOB_CardCollection extends CDOB_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	  
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              
ReferTo
	 ***********************************************************************************  */
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
		DigitalOnBoarding.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,17");
		
	}
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header             

	 ***********************************************************************************  */
	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 new CDOB_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            DigitalOnBoarding.mLogger.info( "Exception:"+e.getMessage());
	        }
	        
	    }
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	  FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
			//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());			
				new CDOB_CommonCode().FrameExpandEvent(pEvent);						
        		
       break;
                
          case FRAGMENT_LOADED:
        	//  CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				fragment_loaded(pEvent,formObject);
			
			  break;
			  
			case MOUSE_CLICKED:
			//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CDOB_CommonCode().mouse_clicked(pEvent);
				break;
			
			 case VALUE_CHANGED:
				//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CDOB_CommonCode().value_changed(pEvent);
					break;
                  default: 
                	  break;
	     
	     }

	}	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              :               
	Description                         :  Product Default      

	 ***********************************************************************************  */
	
	public void initialize() {
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS initialize()" );
		  

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		DigitalOnBoarding.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		CustomSaveForm();
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
		//formObject.setNGValue("cmplx_DEC_Remarks","");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              :               
	Description                         :  Product Default      

	 ***********************************************************************************  */
	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			
		}*/
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			//	formObject.setLocked("Customer_Frame1",true);
				LoadView(pEvent.getSource().getName());
				formObject.setLocked("cmplx_Customer_DOb",true);
				formObject.setLocked("cmplx_Customer_IdIssueDate",true);
				formObject.setLocked("cmplx_Customer_EmirateIDExpiry",true);
				formObject.setLocked("cmplx_Customer_VisaExpiry",true);
				formObject.setLocked("cmplx_Customer_PassportIssueDate",true);
				formObject.setLocked("cmplx_Customer_PassPortExpiry",true);
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
			
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
				/*
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				*/
				formObject.setLocked("Product_Frame1",true);
				int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if(prd_count>0){
					String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
				}
				/*formObject.setEnabled("Product_Save",true);
				formObject.setEnabled("Product_Add",true);
				formObject.setEnabled("Product_Modify",true);
				formObject.setEnabled("Product_Delete",true);*/
			}
			
			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
				loadpicklist_Income();
			}
			
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

				/*formObject.setEnabled("Liability_New_AECBReport",true);
				formObject.setEnabled("Liability_New_Save",true);*/
			}
			
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//formObject.setLocked("EMploymentDetails_Frame1",true);
				LoadView(pEvent.getSource().getName());
				//loadPicklistEmployment();
				loadPicklistEmployment();
			}
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				/*formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);*/
			}
			
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Address();
				//formObject.setLocked("AddressDetails_Frame1",true)
				LoadView(pEvent.getSource().getName());
				//loadPicklist_Address();
				/*formObject.setEnabled("AddressDetails_Save",true);
				formObject.setEnabled("AddressDetails_addr_Add",true);
				formObject.setEnabled("AddressDetails_addr_Modify",true);
				formObject.setEnabled("AddressDetails_addr_Delete",true);*/
			}
			
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
				//change by saurabh on 7th Dec
				//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
				//change by saurabh for air arabia functionality.
				AirArabiaValid();
				//formObject.setLocked("AltContactDetails_Frame1",true);
				LoadView(pEvent.getSource().getName());
				/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
			} 
			
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("FATCA_Frame1",true);
				/*formObject.setEnabled("FATCA_Save",true);*/
				loadPicklist_Fatca();
			}
			
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("KYC_Frame1",true);
				/*formObject.setEnabled("KYC_Save",true);*/
				loadPicklist_KYC();
			}
			
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				LoadView(pEvent.getSource().getName());
				//formObject.setLocked("OECD_Frame1",true);
				/*formObject.setEnabled("OECD_Save",true);*/
				loadPicklist_oecd();
			}
			else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("IncomingDocument_Frame",false);
				
			}
			else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Reference_Details_Frame1",true);
				
			}
			
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("SupplementCardDetails_Frame1",true);
				formObject.setEnabled("SupplementCardDetails_Frame1", false);
				loadPicklist_suppCard();
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//++below code added by nikhil for Self-Supp CR
				Load_Self_Supp_Data();
				//--above code added by nikhil for Self-Supp CR
				//loadPicklist_Address();
				formObject.setLocked("CardDetails_Frame1",true);
				//LoadPickList("CardDetails_BankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
				formObject.setVisible("CardDetails_Label13", false);
				formObject.setVisible("cmplx_CardDetails_CustClassification", false);
				//added by nikhil for disbursal showstopper
				Loadpicklist_CardDetails_Frag();
			}
			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
	               formObject.setLocked("CompanyDetails_Frame1", true);
	               /*LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	               LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	               LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMacro with (nolock)");
	               LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMicro with (nolock)");
	               LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	               LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	               LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	               LoadPickList("EOW", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
	               LoadPickList("headOffice", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
					*/
	               loadPicklist_CompanyDet();
            }
			
			else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
            	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
	            // formObject.setLocked("AuthorisedSignDetails_Button4", true);
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				
			}
			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("PartMatch_Frame1",true);
				formObject.setEnabled("PartMatch_Frame1", false);
				formObject.setLocked("PartMatch_Dob",true);
				formObject.setLocked("PartMatch_Frame1",true);
				LoadView(pEvent.getSource().getName());
				//LoadPickList("PartMatch_nationality","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock) order by code"); //Arun (10/10)
					}
			else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
				LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

				formObject.setLocked("FinacleCore_Frame1",true);
				//added by saurabh on13th Oct for JIRA-2517
				formObject.setLocked("FinacleCore_Frame9", true);
				formObject.setLocked("FinacleCore_ReturnDate", true);
				formObject.setLocked("cmplx_FinacleCore_edit_date_avg_nbc", true);
				formObject.setLocked("cmplx_FinacleCore_edit_date_turn_statistics", true);
				formObject.setLocked("InwardTT_date", true);
				
				
				
				
			}
			else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				loadPicklist_Mol();
				formObject.setLocked("MOL1_Frame1",true);
				formObject.setLocked("cmplx_MOL_molexp",true);
				formObject.setLocked("cmplx_MOL_molissue",true);
				formObject.setLocked("cmplx_MOL_ctrctstart",true);
				formObject.setLocked("cmplx_MOL_ctrctend",true);
				
			}
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);
				formObject.setEnabled("CC_Loan_Frame1",false);
				loadPicklist_ServiceRequest();
				
			}
			else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("PartnerDetails_Frame1", true);
                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            }
			
			
			//added by yash on 22/8/2017
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 DigitalOnBoarding.mLogger.info( "Activity name is:" + sActivityName);
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

			
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				//Arun added on (31/08/17)
				//for decision fragment made changes 8th dec 2017
				/*formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
				formObject.setVisible("DecisionHistory_Decision_Label1",false);
                formObject.setVisible("cmplx_DEC_VerificationRequired",false);
                formObject.setVisible("DecisionHistory_chqbook",false);
                formObject.setVisible("DecisionHistory_Label1",false);
                formObject.setVisible("cmplx_DEC_ReferReason",false);
                formObject.setVisible("DecisionHistory_Label6",false);
                formObject.setVisible("cmplx_DEC_IBAN_No",false);
                formObject.setVisible("DecisionHistory_Label7",false);
                formObject.setVisible("cmplx_DEC_NewAccNo",false);
                formObject.setVisible("DecisionHistory_Label8",false);
                formObject.setVisible("cmplx_DEC_ChequebookRef",false);
                formObject.setVisible("DecisionHistory_Label9",false);
                formObject.setVisible("cmplx_DEC_DCR_Refno",false);
                formObject.setVisible("DecisionHistory_Label5",false);
                formObject.setVisible("cmplx_DEC_Description",false);
                formObject.setVisible("DecisionHistory_Label3",false);
                formObject.setVisible("cmplx_DEC_Strength",false);
                formObject.setVisible("DecisionHistory_Label4",false);
                formObject.setVisible("cmplx_DEC_Weakness",false);
                formObject.setVisible("DecisionHistory_Label27",false);
                formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
                
                formObject.setVisible("DecisionHistory_Label28",true);
				formObject.setVisible("DecisionHistory_CardRecvBranch",true);
                formObject.setVisible("DecisionHistory_Label29",true);
                formObject.setVisible("DecisionHistory_CardRecvDate",true);
                formObject.setVisible("DecisionHistory_Label30",true);
                formObject.setVisible("DecisionHistory_CardDeliverCust",true);
                formObject.setVisible("DecisionHistory_Label31",true);
                formObject.setVisible("DecisionHistory_DeliveryDate",true);
                formObject.setVisible("DecisionHistory_Button5",true);
                formObject.setVisible("cmplx_DEC_cmplx_gr_CardCollec",true);
                
                formObject.setTop("DecisionHistory_Label28",10);
                formObject.setTop("DecisionHistory_CardRecvBranch",25);
                formObject.setTop("DecisionHistory_Label29",10);
                formObject.setTop("DecisionHistory_CardRecvDate",25);
                formObject.setTop("DecisionHistory_Label30",10);
                formObject.setTop("DecisionHistory_CardDeliverCust",25);
                formObject.setTop("DecisionHistory_Label31",10);
                formObject.setTop("DecisionHistory_DeliveryDate",25);                        
                formObject.setTop("DecisionHistory_Button5",58);
                formObject.setTop("cmplx_DEC_cmplx_gr_CardCollec",106);                        
                formObject.setTop("DecisionHistory_Decision_Label3",280);
                formObject.setTop("cmplx_DEC_Decision",296);
                formObject.setTop("DecisionHistory_Decision_Label4",280);
                formObject.setTop("cmplx_DEC_Remarks",296);
                formObject.setTop("cmplx_DEC_Gr_DecisonHistory",370);
                formObject.setTop("DecisionHistory_save",550);
                
                formObject.setHeight("DecisionHistory_Frame1",600);
                
                formObject.setLeft("DecisionHistory_Decision_Label4",352);
                formObject.setLeft("cmplx_DEC_Remarks",352);
				
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
                
                formObject.setVisible("cmplx_DEC_ContactPointVeri",false);*/ //Arun Commented on 31/8/17
                
                loadPicklist3();
               // formObject.setLocked("DecisionHistory_dec_reason_code",false); // As the fields is locked not disabled
 		 	//	formObject.setLocked("DecisionHistory_ReferTo",true);
               
                fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
                formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				//for decision fragment made changes 8th dec 2017
		 } 	
	
	}

	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

