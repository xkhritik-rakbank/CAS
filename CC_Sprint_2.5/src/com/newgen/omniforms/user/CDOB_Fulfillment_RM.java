
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Fullfillment RM

File Name                                                         : CC_Fullfillment_RM

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


public class CDOB_Fulfillment_RM extends CDOB_Common implements FormListener
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

	 ***********************************************************************************  */
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
		DigitalOnBoarding.mLogger.info("CC Fullfillment"+ "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		
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
	    	 DigitalOnBoarding.mLogger.info("CC Fullfillment"+"Inside CC_Fullfillment CC");
	    	 new CDOB_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            DigitalOnBoarding.mLogger.info("RLOS Initiation"+ "Exception:"+e.getMessage());
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
		
		//CreditCard.mLogger.info("Inside CC_Fullfillment eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				//CreditCard.mLogger.info(" In CC_Fullfillment eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CDOB_CommonCode().FrameExpandEvent(pEvent);						

        		
       break;
                
          case FRAGMENT_LOADED:
        	 // CreditCard.mLogger.info(" In CC_Fullfillment eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
        	  fragment_loaded(pEvent,formObject);
        	  
        	  
				
			  break;
			  
			case MOUSE_CLICKED:
				//CreditCard.mLogger.info(" In CC_Fullfillment eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CDOB_CommonCode().mouse_clicked(pEvent);
				break;
			
			 case VALUE_CHANGED:
					//CreditCard.mLogger.info(" In CC_Fullfillment eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CDOB_CommonCode().value_changed(pEvent);
					break;
          
          
         
                  default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		DigitalOnBoarding.mLogger.info("PersonnalLoanS>  CC_Fullfillment"+ "Inside PL PROCESS initialize()" );
		}

	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		DigitalOnBoarding.mLogger.info("PersonnalLoanS>  CC_Fullfillment"+ "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
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
		DigitalOnBoarding.mLogger.info("PersonnalLoanS>  CC_Fullfillment"+"Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		DigitalOnBoarding.mLogger.info("PersonnalLoanS"+"Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		DigitalOnBoarding.mLogger.info("PersonnalLoanS>  CC_Fullfillment"+ "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		DigitalOnBoarding.mLogger.info("PersonnalLoanS>  CC_Fullfillment"+ "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		CustomSaveForm();
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
		//formObject.setNGValue("cmplx_DEC_Remarks","");
	}


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
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			
		}*/
		//added by yash on 12/11/2017 for fulfillment_RM
	  
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				formObject.setLocked("Customer_Frame1",true);
				formObject.setLocked("cmplx_Customer_DOb", true);
				formObject.setLocked("cmplx_Customer_IdIssueDate", true);
				formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
				formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
				formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
				formObject.setLocked("cmplx_Customer_VisaExpiry", true);
				formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
				
				formObject.setLocked("Customer_FetchDetails",true);
				formObject.setLocked("Customer_save",true);
				//formObject.setLocked("Product_Frame1",true);
				
				loadPicklistCustomer();
				
			}	
			
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Product_Frame1",true);
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				/*formObject.setEnabled("Product_Save",true);
				formObject.setEnabled("Product_Add",true);
				formObject.setEnabled("Product_Modify",true);
				formObject.setEnabled("Product_Delete",true);*/
			}
			
			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
				loadpicklist_Income();
			
				//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
				loadpicklist_Income();
			}
			
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				if(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid_TakeOverIndicator")=="true"){
					formObject.setLocked("cmplx_Liability_New_cmplx_LiabilityAdditionGrid_TakeOverAmount",false);
				}
					else{
						formObject.setLocked("cmplx_Liability_New_cmplx_LiabilityAdditionGrid_TakeOverAmount",true);	
					}
				
				formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
				//formObject.setVisible("cmplx_Liability_New_DBR",false);
				//formObject.setVisible("cmplx_Liability_New_TAI",false);
				//formObject.setVisible("cmplx_Liability_New_DBRNet",false);
			
				LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//++below code added by nikhil for Self-Supp CR
				Load_Self_Supp_Data();
				//--above code added by nikhil for Self-Supp CR
				formObject.setLocked("CardDetails_Frame1",true);
			}
			
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("EMploymentDetails_Frame1",true);
				//loadPicklistEmployment();
				loadPicklistEmployment();
			}
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",false);
				//formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate",false);
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
				formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
			}
			
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("AddressDetails_Frame1",true);
				loadPicklist_Address();
				//loadPicklist_Address();
				/*formObject.setEnabled("AddressDetails_Save",true);
				formObject.setEnabled("AddressDetails_addr_Add",true);
				formObject.setEnabled("AddressDetails_addr_Modify",true);
				formObject.setEnabled("AddressDetails_addr_Delete",true);*/
			}
			
			
			
			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				 formObject.setLocked("CompanyDetails_Frame1", true);
				formObject.setLocked("cif",true);
				formObject.setLocked("CompanyDetails_Button3",true);
	               /*
					LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");*/
				loadPicklist_CompanyDet();
	            }
		/*	else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				 formObject.setLocked("CompanyDetails_Frame2", true);
				
				
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				 }
			else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CompanyDetails_Frame3", true);
                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            }*/
			
	
				else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("CC_Loan_Frame1",true);
					formObject.setLocked("CC_Loan_Frame2",true);
					formObject.setLocked("CC_Loan_Frame3",true);
					formObject.setLocked("totBalTransfer",true);
					loadPicklist_ServiceRequest();
				
				
			}	
			if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
				
				formObject.setLocked("AltContactDetails_Frame1",true);
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
				//change by saurabh on 7th Dec
			//	LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
				
			} 
			
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

				
				//formObject.setLocked("FATCA_Frame1",true);
				formObject.setLocked("FATCA_Frame6",true);
				
			}

			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

				formObject.setLocked("KYC_Frame1",true);
				
			}

			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				
				//formObject.setLocked("OECD_Frame1",true);
				formObject.setLocked("OECD_Frame8",true);
			
			}
			
			else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("IncomingDocument");
				formObject.setLocked("IncomingDocument_Frame1",true);

				//++ Below Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
				DigitalOnBoarding.mLogger.info("before incomingdoc");
				//Commented for sonar
				//fetchIncomingDocRepeater();
				DigitalOnBoarding.mLogger.info("after incomingdoc");
				//++ Above Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
			
			}
			
			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("PartMatch_Frame1",true);

			}
			else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setLocked("FinacleCRMIncident_Frame1",true);

			}
			else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);

			}
			else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setLocked("ExternalBlackList_Frame1",true);

			}
			else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setLocked("FinacleCore_Frame1",true);
				LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
				LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

			}
			else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Mol();
				formObject.setLocked("MOL1_Frame1",true);

			}
			else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setLocked("RejectEnq_Frame1",true);

			}
			else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SalaryEnq_Frame1",true);
			}
			

			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
					 
					 formObject.setLocked("NotepadDetails_Frame2",false);
					 formObject.setLocked("NotepadDetails_Frame1",false);
notepad_load();					/* formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			    	
					formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
					formObject.setLocked("NotepadDetails_noteDate",true);
					formObject.setLocked("NotepadDetails_Actusername",true);
					formObject.setLocked("NotepadDetails_user",true);
					formObject.setLocked("NotepadDetails_insqueue",true);
					formObject.setLocked("NotepadDetails_Actdate",true);
					formObject.setVisible("NotepadDetails_Frame3",true);
					formObject.setVisible("NotepadDetails_save",false);
					formObject.setHeight("NotepadDetails_Frame3",260);
					formObject.setVisible("NotepadDetails_SaveButton",true);
					formObject.setTop("NotepadDetails_SaveButton",400);*/
					
					
				
				
			}
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				//for decision fragment made changes 8th dec 2017	
			/*	formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
				formObject.setVisible("DecisionHistory_Label1",false);
                formObject.setVisible("cmplx_DEC_VerificationRequired",false);
                formObject.setVisible("cmplx_DEC_ReferReason",false);
                formObject.setVisible("DecisionHistory_Label3",false);
                formObject.setVisible("DecisionHistory_Label8",false);
                formObject.setVisible("DecisionHistory_Label7",false);
                formObject.setVisible("DecisionHistory_Label2",false);
                formObject.setVisible("cmplx_DEC_Description",false);
                formObject.setVisible("cmplx_DEC_Strength",false);
                formObject.setVisible("cmplx_DEC_Weakness",false);
                
                formObject.setVisible("cust_cont_lbl",true);
                formObject.setVisible("cmplx_DEC_Cust_Contacted",true);*/
                
				
            	/*formObject.setVisible("DecisionHistory_CheckBox1",false);                       
                formObject.setVisible("DecisionHistory_Combo3",false);
                formObject.setVisible("DecisionHistory_Label6",false);
                formObject.setVisible("DecisionHistory_Combo6",false);
                formObject.setVisible("DecisionHistory_Decision_Label4",false);
                formObject.setVisible("cmplx_DEC_Remarks",false);                        
                formObject.setVisible("DecisionHistory_Text4",false);                        
                formObject.setVisible("DecisionHistory_Text3",false);                        
                formObject.setVisible("DecisionHistory_Text2",false); */ //Arun(14-jun)                       
                
				   
	               
                
                loadPicklist3();
                fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label38,cmplx_DEC_Area#DecisionHistory_Label39,cmplx_DEC_Location#CustomerMetBy,cmplx_DEC_Customer_Met_By#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
            	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				//formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				//for decision fragment made changes 8th dec 2017	
            	loadInDecGrid();
            	//formObject.setVisible("DecisionHistory_Label35",true);
			   // formObject.setVisible("DecisionHistory_Label36",true);
			    //formObject.setVisible("DecisionHistory_Label37",true);
			    formObject.setVisible("DecisionHistory_Label38",true);
			    formObject.setVisible("DecisionHistory_Label39",true);
			    formObject.setVisible("cmplx_DEC_Location",true);
			    formObject.setVisible("cmplx_DEC_Area",true);
			    //CustomerMetBy
			   // DecisionHistory_Text23
			   // formObject.setVisible("CustomerMetBy",true);
			    formObject.setVisible("cmplx_DEC_Customer_Met_By",true);
			    formObject.setVisible("cmplx_DEC_RM_Ext",true);
			    LoadPickList("cmplx_DEC_Area","select  '--Select--' union all select distinct convert(varchar, AREA) from NG_MASTER_AREA_LOCATION with (nolock)");
               
			} 	
	
	
	}
	//ended by byash on 12/11/2017 for fulfillmetc_RM


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

