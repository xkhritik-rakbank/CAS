
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Reject Queue

File Name                                                         : CC_Reject_Queue

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

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_Reject_Queue extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	  CC_Integration_Input Intgration_input = new CC_Integration_Input();
		CC_Integration_Output Intgration_Output = new CC_Integration_Output();
		
		
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              

	 ***********************************************************************************  */
	public void formLoaded(FormEvent pEvent)
	{
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		makeSheetsInvisible(tabName, "6,7,8,9,11,12,13,14,15,16,17");
		
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
        	CreditCard.mLogger.info( "Inside Reject CC");
           new CC_CommonCode().setFormHeader(pEvent);
        }catch(Exception e)
        {
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
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
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				
				new CC_CommonCode().FrameExpandEvent(pEvent);						

				break;
                
          case FRAGMENT_LOADED:
        	  CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
        	  new CC_CommonCode().LockFragmentsOnLoad(pEvent);
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

	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		 
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
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
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
	/*private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			
		}
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				//formObject.setLocked("Product_Frame1",true);
				formObject.setEnabled("Customer_save",true);
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
				formObject.setEnabled("Customer_Reference_delete",true);
				loadPicklistCustomer();
				
			}	
			
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				formObject.setLocked("Product_Frame1",true);
				formObject.setEnabled("Product_Save",true);
				formObject.setEnabled("Product_Add",true);
				formObject.setEnabled("Product_Modify",true);
				formObject.setEnabled("Product_Delete",true);
			}
			
			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
				loadpicklist_Income();
			}
			
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				formObject.setEnabled("Liability_New_AECBReport",true);
				formObject.setEnabled("Liability_New_Save",true);
			}
			
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("EMploymentDetails_Frame1",true);
				loadPicklistEmployment();
				loadPicklist4();
			}
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
			}
			
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Address();
				formObject.setLocked("AddressDetails_Frame1",true);
				//loadPicklist_Address();
				formObject.setEnabled("AddressDetails_Save",true);
				formObject.setEnabled("AddressDetails_addr_Add",true);
				formObject.setEnabled("AddressDetails_addr_Modify",true);
				formObject.setEnabled("AddressDetails_addr_Delete",true);
			}
			
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("AltContactDetails_Frame1",true);
				formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
			} 
			
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("FATCA_Frame1",true);
				formObject.setEnabled("FATCA_Save",true);
			}
			
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("KYC_Frame1",true);
				formObject.setEnabled("KYC_Save",true);
			}
			
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("OECD_Frame1",true);
				formObject.setEnabled("OECD_Save",true);
			}
			else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("IncomingDocument_Frame",true);
				formObject.setEnabled("OECD_Save",true);
			}
			else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Reference_Details_Frame1",true);
				
			}
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SupplementCardDetails_Frame1",true);
				
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CardDetails_Frame1",true);
				
			}
			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
	               formObject.setLocked("CompanyDetails_Frame1", true);
	               
					LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
	               loadPicklist_CompanyDet();
	            }
			else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
            	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
	            // formObject.setLocked("AuthorisedSignDetails_Button4", true);
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
            }
			else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("PartnerDetails_Frame1", true);
                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            }
						
			//12th sept
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("NotepadDetails_Frame2",true);
				 notepad_load();
				 notepad_withoutTelLog();
				 
			}
			//12th sept
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				//for decision fragment made changes 8th dec 2017
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
                formObject.setVisible("DecisionHistory_chqbook",false);
				formObject.setVisible("DecisionHistory_Text15",false);
				formObject.setVisible("DecisionHistory_Text16",false);
				formObject.setVisible("DecisionHistory_Text17",false);
				formObject.setVisible("DecisionHistory_Label9",false);
				formObject.setVisible("cmplx_DEC_DCR_Refno",false);
				formObject.setVisible("DecisionHistory_Label27",false);
				formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
				formObject.setVisible("cmplx_DEC_ChequebookRef",false);
				formObject.setVisible("DecisionHistory_Label4",false);
				formObject.setVisible("DecisionHistory_Label5",false);
                
                formObject.setVisible("DecisionHistory_Label10",true);
                formObject.setVisible("cmplx_DEC_New_CIFID",true);
                formObject.setVisible("DecisionHistory_Label7",true);
                formObject.setVisible("cmplx_DEC_NewAccNo",true);
                formObject.setVisible("DecisionHistory_Label6",true);
                formObject.setVisible("cmplx_DEC_IBAN_No",true);
                formObject.setVisible("DecisionHistory_Button2",true);
                formObject.setEnabled("DecisionHistory_Button2",false);
                formObject.setVisible("DecisionHistory_Decision_Label1",true);
                formObject.setVisible("cmplx_DEC_VerificationRequired",true);
                formObject.setVisible("DecisionHistory_Decision_Label3",true);
                formObject.setVisible("cmplx_DEC_Decision",true);//dec
                formObject.setVisible("DecisionHistory_Rejreason",true);
                formObject.setVisible("cmplx_DEC_RejectReason",true);
                formObject.setVisible("DecisionHistory_Button4",true);
                formObject.setEnabled("DecisionHistory_Button4",false);
                
                formObject.setTop("DecisionHistory_Label10",10);
                formObject.setTop("cmplx_DEC_New_CIFID",25);
                formObject.setTop("DecisionHistory_Label7",10);
                formObject.setTop("cmplx_DEC_NewAccNo",25);
                formObject.setTop("DecisionHistory_Label6",10);
                formObject.setTop("cmplx_DEC_IBAN_No",25);
                formObject.setTop("DecisionHistory_Button2",15);
                formObject.setTop("DecisionHistory_Decision_Label1",10);
                formObject.setTop("cmplx_DEC_VerificationRequired",25);
                formObject.setTop("DecisionHistory_Rejreason",104);
                formObject.setTop("cmplx_DEC_RejectReason",120);
                formObject.setLeft("DecisionHistory_Rejreason",560);
                formObject.setLeft("cmplx_DEC_RejectReason",560);
                formObject.setLeft("DecisionHistory_Label7",304);
                formObject.setLeft("cmplx_DEC_NewAccNo",304);
                formObject.setLeft("DecisionHistory_Label6",560);
                formObject.setLeft("cmplx_DEC_IBAN_No",560);
                formObject.setLeft("DecisionHistory_Button2",813);
                formObject.setLeft("DecisionHistory_Decision_Label1",1074);
                formObject.setLeft("cmplx_DEC_VerificationRequired",1074);
                formObject.setTop("DecisionHistory_Button4",104);
                formObject.setLeft("DecisionHistory_Button4",813);
                formObject.setLeft("DecisionHistory_Label10",23);
                formObject.setLeft("cmplx_DEC_New_CIFID",23);
                formObject.setTop("DecisionHistory_save",250);
    			formObject.setTop("DecisionHistory_Decision_ListView1",280);
    			formObject.setEnabled("cmplx_DEC_RejectReason",true);
    			formObject.setEnabled("cmplx_DEC_IBAN_No",false);
				formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
				CreditCard.mLogger.info("***********Inside decision history");
				fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Label7,cmplx_DEC_NewAccNo#DecisionHistory_Label6,cmplx_DEC_IBAN_No#DecisionHistory_Button2#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
				formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				
				CreditCard.mLogger.info("***********Inside after fragment alignment decision history");
				//for decision fragment made changes 8th dec 2017
				
				if(!("DDVT_Maker".equalsIgnoreCase(formObject.getNGValue("PREVIOUS_WS"))||NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CSM").equalsIgnoreCase(formObject.getNGValue("PREVIOUS_WS")))){
					 formObject.setEnabled("DecisionHistory_Button2",true);
			}
				
                //12th sept
                loadPicklist3();
		 } 	
	
	
	}*/
	
	

}

