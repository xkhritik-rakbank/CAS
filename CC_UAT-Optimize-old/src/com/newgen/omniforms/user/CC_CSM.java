
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CSM

File Name                                                         : CC_CSM

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Reference No/CR No   Change Date   Changed By    Change Description
1. 					12-6-2016	 Disha		   Code commented saveIndecisionGrid() for continueExecution
1003				17-9-2017	Saurabh			Making CSM employment Details same as initiation.
1000				17-9-2017	Saurabh			Making decision history fragment same as initiation.
1002				17-9-2017	Saurabh			Hiding DDVT related fields in CSM. 
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.io.Serializable;
import java.util.HashMap;



import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


//Changes - 1. On Sales coordinator Incoming Document frame is editable 

public class CC_CSM extends CC_Common implements FormListener,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	 
	public void formLoaded(FormEvent pEvent)
	{
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 CreditCard.mLogger.info("Inside CC_Hold_CPV CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            CreditCard.mLogger.info( "Exception:"+e.getMessage());
	        }
	        
	    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		CreditCard.mLogger.info( "eventdispatched inside");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().FrameExpandEvent(pEvent);
        break;
                
          case FRAGMENT_LOADED:
        	  CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
        	  fragment_loaded(pEvent,formObject);
        	  
				
			  break;
			  
			case MOUSE_CLICKED:
				CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().mouse_clicked(pEvent);
				break;
			
			 case VALUE_CHANGED:
					CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().value_changed(pEvent);
					break;
					 
          
         
                  default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		CreditCard.mLogger.info( "Inside PL PROCESS initialize()" );
		 

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  

	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("DecisionHistory_Cif_ID",formObject.getNGValue("cmplx_Customer_CIFNo"));
		formObject.setNGValue("DecisionHistory_Emirates_Id",formObject.getNGValue("cmplx_Customer_CIFNo"));
		String fName = formObject.getNGValue("cmplx_Customer_FirstNAme");
        String mName = formObject.getNGValue("cmplx_Customer_MiddleNAme");
        String lName = formObject.getNGValue("cmplx_Customer_LastNAme");
        String fullName = fName+" "+mName+" "+lName; 
        formObject.setNGValue("DecisionHistory_Customer_Name",fullName);
		saveIndecisionGridCSM();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		//saveIndecisionGrid();
	}
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			
		}*/
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
				CreditCard.mLogger.info( "start of customer");
				
				try{
					
					if(formObject.getNGValue("cmplx_Customer_NEP").equals("true")){
						formObject.setVisible("cmplx_Customer_EIDARegNo",true);
					}
					else {
						formObject.setVisible("cmplx_Customer_EIDARegNo",false);
					}
					if(formObject.isEnabled("cmplx_Customer_CIFNo"))
					formObject.setEnabled("cmplx_Customer_CIFNo",false);
				disableButtonsCC("Customer");
				
				loadPicklistCustomer();
				
				}
				// added for point 22
				catch(Exception ex){
					CreditCard.mLogger.info( "Exception is: "+printException(ex));
				}
				CreditCard.mLogger.info( "end of customer");
			}	
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				
				disableButtonsCC("Product");
				
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
				
				disableButtonsCC("IncomeDetails");
				formObject.setLocked("cmplx_IncomeDetails_CompanyAcc", true);
				
				loadpicklist_Income();
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
            	 
				 
				 disableButtonsCC("AuthorisedSignDetails");
				 
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
			    disableButtonsCC("PartnerDetails");
				
                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                
                
            }
			
			if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
					disableButtonsCC("CompanyDetails");
					loadPicklist_CompanyDet();
					hideAtCSMCompany();
				}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			
				disableButtonsCC("Liability_New");
			// ++ below code not commented at offshore - 06-10-2017
				formObject.setVisible("Liability_New_Label6",false);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",false);
				formObject.setVisible("Liability_New_Label8",false);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",false);
				//++ Below Code already exists for : "18-CSM-Liability addition-" Incorrect masters in type of contract field" : Reported By Shashank on Oct 05, 2017++
				CreditCard.mLogger.info("before loadng picklist");
				  LoadPickList("ExtLiability_contractType", "select '--Select--' union select convert(varchar, description) from ng_master_contract_type with (nolock)");
				  CreditCard.mLogger.info("after loadng picklist");
				//++ Above Code already exists for : "18-CSM-Liability addition-" Incorrect masters in type of contract field" : Reported By Shashank on Oct 05, 2017++
				  CreditCard.mLogger.info("after liability Details of CSM456");
				//++ Below Code already exists for : "16-CSM-Liability Details-" Only aggregate exposure fiel shoul be visible in the bottom section in Liability details" : Reported By Shashank on Oct 05, 2017++
				  formObject.setVisible("ExtLiability_Label9",false);
				  formObject.setVisible("cmplx_Liability_New_DBR",false);
				  formObject.setVisible("ExtLiability_Label25",false);
				  formObject.setVisible("cmplx_Liability_New_TAI",false);
				  formObject.setVisible("ExtLiability_Label14",false);
				  formObject.setVisible("cmplx_Liability_New_DBRNet",false);
				  formObject.setVisible("ExtLiability_AECBReport",false);
				//++ Above Code already exists for : "16-CSM-Liability Details-" Only aggregate exposure fiel shoul be visible in the bottom section in Liability details" : Reported By Shashank on Oct 05, 2017++
				
				 CreditCard.mLogger.info("after liability Details of CSM123");						
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
				
				
				disableButtonsCC("EMploymentDetails");
				
				loadPicklistEmployment();
				loadPicklist4();
				Fields_ApplicationType_Employment();
				// Ref. 1003
				formObject.setVisible("EMploymentDetails_Label7", false);
				formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate", false);
				formObject.setVisible("EMploymentDetails_Label28", false);
				formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
				formObject.setVisible("EMploymentDetails_Label5", false);
				formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
				formObject.setVisible("EMploymentDetails_Label6", false);
				formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate", false);
				formObject.setVisible("EMploymentDetails_Label29", false);
				formObject.setVisible("cmplx_EmploymentDetails_Status_PLExpact", false);
				formObject.setVisible("EMploymentDetails_Label30", false);
				formObject.setVisible("cmplx_EmploymentDetails_Status_PLNational", false);
				formObject.setVisible("EMploymentDetails_Label71", false);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
				formObject.setVisible("EMploymentDetails_Label18", false);
				formObject.setVisible("cmplx_EmploymentDetails_ownername", false);
				formObject.setVisible("EMploymentDetails_Label9", false);
				formObject.setVisible("cmplx_EmploymentDetails_NOB", false);
				formObject.setVisible("EMploymentDetails_Label31", false);
				formObject.setVisible("EMploymentDetails_Combo34", false);
				formObject.setVisible("EMploymentDetails_Label17", false);
				formObject.setVisible("cmplx_EmploymentDetails_authsigname", false);
				formObject.setVisible("cmplx_EmploymentDetails_highdelinq", false);
				formObject.setVisible("EMploymentDetails_Label20", false);
				formObject.setVisible("cmplx_EmploymentDetails_dateinPL", false);
				formObject.setVisible("EMploymentDetails_Label21", false);
				formObject.setVisible("cmplx_EmploymentDetails_dateinCC", false);
				formObject.setVisible("EMploymentDetails_Label15", false);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
				formObject.setVisible("EMploymentDetails_Label32", false);
				formObject.setVisible("EMploymentDetails_Combo35", false);
				formObject.setVisible("EMploymentDetails_Label26", false);
				formObject.setVisible("cmplx_EmploymentDetails_remarks", false);
				formObject.setVisible("EMploymentDetails_Label27", false);
				formObject.setVisible("cmplx_EmploymentDetails_Aloc_RemarksPL", false);
				formObject.setVisible("EMploymentDetails_Label10", false);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
				formObject.setVisible("EMploymentDetails_Label4", false);
				formObject.setVisible("cmplx_EmploymentDetails_PromotionCode", false);
				formObject.setVisible("EMploymentDetails_Label10", false);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
				formObject.setTop("EMploymentDetails_Save", 260);
				
				//added by akshay on 13/10/17
				formObject.setVisible("EMploymentDetails_Label32", false);
				formObject.setVisible("cmplx_EmploymentDetails_FieldVisitDone", false);
			    formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			    if(formObject.getNGValue("cmplx_Customer_NEP").equals("false")){
			    	formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
			    }
					//p1-7,ALOC fields should be disabled for CSM user
				    formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
                    formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
                    formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
                    formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
                    formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
                    formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
                    formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
                    formObject.setLocked("cmplx_EmploymentDetails_Freezone",true); 
                    formObject.setLocked("cmplx_EmploymentDetails_Kompass",true); 
					//p1-7,ALOC fields should be disabled for CSM user
				// Ref. 1003 end.
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
			
				disableButtonsCC("ELigibiltyAndProductInfo");
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestRate", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_EMI", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_Tenor", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", false);

			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			
				disableButtonsCC("AddressDetails");
				loadPicklist_Address();
				
				
				
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
				
				disableButtonsCC("AltContactDetails");
				
				/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
			} 
			
			if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
				
				disableButtonsCC("FATCA");
				
				/*formObject.setEnabled("FATCA_Save",true);*/
			}
			
			
			if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
				
				disableButtonsCC("OECD");
				
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")){
				
				disableButtonsCC("IncomingDocument");
				
				//++ Below Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
				CreditCard.mLogger.info("before incomingdoc");
				fetchIncomingDocRepeater();
				CreditCard.mLogger.info("after incomingdoc");
				//++ Above Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
			
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
			
				disableButtonsCC("Reference_Details");
				LoadPickList("Reference_Details_ReferenceRelatiomship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
			
				disableButtonsCC("SupplementCardDetails");
				
				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
			
				disableButtonsCC("CardDetails");
				//Ref. 1002
				//changed by akshay on 13/10
				LoadPickList("cmplx_CardDetails_MarketingCode","select '--Select--'  as description union select convert(varchar,description) from NG_MASTER_MarketingCode with (nolock) ");
				formObject.setVisible("cmplx_CardDetails_Security_Check_Held", false);
				LoadPickList("CardDetails_FeeProfile","select '--Select--'  as description union select convert(varchar,description) from ng_master_feeProfile with (nolock) ");
				formObject.setVisible("CardDetails_Label12", false);
				LoadPickList("CardDetails_InterestFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_InterestProfile with (nolock) ");
				formObject.setVisible("cmplx_CardDetails_MarketingCode", false);
				LoadPickList("CardDetails_TransactionFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_TransactionFeeProfile with (nolock) ");
				formObject.setVisible("CardDetails_Label8", false);

				
				/*formObject.setVisible("cmplx_CardDetails_Bank_Name", false);
				formObject.setVisible("CardDetails_Label9", false);
				formObject.setVisible("cmplx_CardDetails_Cheque_Number", false);
				formObject.setVisible("CardDetails_Label10", false);
				formObject.setVisible("cmplx_CardDetails_Amount", false);
				formObject.setVisible("CardDetails_Label11", false);
				formObject.setVisible("cmplx_CardDetails_Date", false);
				formObject.setVisible("CardDetails_Label13", false);
				formObject.setVisible("cmplx_CardDetails_CustClassification", false);*/
				//Ref. 1002 end.
			}
			//added by yash on 22/8/2017
			if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
				//12th sept
				 notepad_load();
				 notepad_withoutTelLog();
				 //12th sept
				 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 CreditCard.mLogger.info( "Activity name is:" + sActivityName);
		        formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		    	//Ref. 1001.
				formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setVisible("NotepadDetails_SaveButton",true);
				formObject.setVisible("NotepadDetails_Frame3", false);
				formObject.setTop("NotepadDetails_SaveButton",400);
				//Ref. 1001.
			}
			//ended by yash
			
			
			if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
				//Ref 1000. changes done by saurabh on 17th Sept for keeping sync in decision history of CSM and initiation.
				formObject.setVisible("DecisionHistory_CheckBox1",false);
                formObject.setVisible("DecisionHistory_Label1",true);
                formObject.setVisible("cmplx_DEC_VerificationRequired",true);
                formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
                formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
                formObject.setVisible("DecisionHistory_Label3",false);
                formObject.setVisible("DecisionHistory_Combo3",true);
                formObject.setVisible("DecisionHistory_Label6",true);
                formObject.setVisible("DecisionHistory_Combo6",true);
                formObject.setVisible("DecisionHistory_Decision_Label4",true);
                formObject.setVisible("cmplx_DEC_Remarks",true);
                formObject.setVisible("DecisionHistory_Label8",false);
                formObject.setVisible("DecisionHistory_Text4",true);
                //formObject.setVisible("DecisionHistory_Label7",false);
                formObject.setVisible("cmplx_DEC_ChequebookRef",false);
                //12th sept
                formObject.setVisible("DecisionHistory_Label6",false);
                formObject.setVisible("cmplx_DEC_IBAN_No",false);
        		formObject.setVisible("DecisionHistory_Label5",false);
				formObject.setVisible("DecisionHistory_Decision_Label1",false);
				formObject.setVisible("cmplx_DEC_VerificationRequired",false);
				formObject.setVisible("DecisionHistory_Label10",false);
				formObject.setVisible("cmplx_DEC_New_CIFID",false);
				formObject.setVisible("cmplx_DEC_NewAccNo",false);
				formObject.setVisible("DecisionHistory_chqbook",false);
				formObject.setVisible("cmplx_DEC_ChequebookRef",false);
				formObject.setVisible("DecisionHistory_Label4",false);
				formObject.setVisible("DecisionHistory_Label27",false);
				formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
				formObject.setVisible("DecisionHistory_Label9",false);
				formObject.setVisible("cmplx_DEC_DCR_Refno",false);
				formObject.setVisible("DecisionHistory_Decision_Label4",false);
				formObject.setVisible("cmplx_DEC_Remarks",false);
				
				 formObject.setVisible("cmplx_DEC_Description",false);
                 formObject.setVisible("cmplx_DEC_Strength",false);
                 formObject.setVisible("cmplx_DEC_Weakness",false);
                 formObject.setVisible("DecisionHistory_Label2",false);
                 formObject.setVisible("DecisionHistory_Label7",false);
                 formObject.setVisible("DecisionHistory_Label8",false);
                 formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
                 formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
                 formObject.setVisible("DecisionHistory_Label3",false);
                 formObject.setVisible("DecisionHistory_CheckBox1",false);
                 formObject.setVisible("cmplx_DEC_Decision_Reasoncode",true);
                 formObject.setEnabled("cmplx_DEC_Decision_Reasoncode",true);
        		 formObject.setVisible("DecisionHistory_Label11",true);
        		 formObject.setVisible("DecisionHistory_Rejreason",true);//rej resn
 		 		 formObject.setVisible("cmplx_DEC_RejectReason",true);//rej rsn field treated as remarks
 		 		formObject.setEnabled("cmplx_DEC_RejectReason",true);
                //12th sept
				disableButtonsCC("DecisionHistory");
				formObject.setTop("DecisionHistory_save",240);
				formObject.setTop("DecisionHistory_Decision_ListView1",300);
				//Ref 1000. changes done by saurabh on 17th Sept for keeping sync in decision history of CSM and initiation end.
				
               
                
                loadPicklist3();
		 } 	
	
	
	}


}

