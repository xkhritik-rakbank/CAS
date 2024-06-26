
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : OV

File Name                                                         : CC_OV

Author                                                            : Disha

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.                 12-6-2017     Disha		   Changes done to hide OV tab
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


public class CC_OV extends CC_Common implements FormListener
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
		
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		// Changes done to hide OV tab
		makeSheetsInvisible("Tab1", "7,8,9,12,13,14,15,16,17");
		CreditCard.mLogger.info( "Inside formLoaded() of CC_OV");
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
        	CreditCard.mLogger.info("Inside CC_OV CC");
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
		//OriginalDocs();
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
		saveIndecisionGrid();
		
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
		//below code commented by nikhil 7/12/17
		//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_DEC_Decision"));
		
		//modified by kashay on 4/1/17
		if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
			formObject.setNGValue("ORIGINAL_VALIDATION", "Yes");
		}
		else if("Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
			formObject.setNGValue("ORIGINAL_VALIDATION", "No");
		}
		LoadReferGrid();
		/*String query;
		String OVDecision;
		List<List<String>> docName;
		
		query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
		CreditCard.mLogger.info(""+query);
		docName = formObject.getNGDataFromDataCache(query);
		CreditCard.mLogger.info("docname query123"+docName);
		CreditCard.mLogger.info( "add row ov size of doc name123 "+docName.size()+",docName"+docName);
		for(int i=0;i<docName.size();i++ )
		{
			CreditCard.mLogger.info("inside loops");
			OVDecision = docName.get(i).get(13);
			CreditCard.mLogger.info("OVDecision"+OVDecision);
			if("--Select--".equalsIgnoreCase(OVDecision)){
				throw new ValidatorException(new FacesMessage("Please Select OV Decision"));
			}
			else{
				CreditCard.mLogger.info("move out of the loop");
			}
			
		}*/
		OriginalDocs();
		//12th sept
		
			////formObject.setNGValue("cmplx_DEC_Remarks","");
		
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) 
	{
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_DEC_Decision"));
		
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	
	 ***********************************************************************************  */
	
	//Below code added by yash on 15/12/2017 as per fsd 2.7
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				
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
				
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			fetchIncomingDocRepeater();
			}
		

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
	
			loadpicklist_Income();
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklistEmployment();
			loadPicklist4();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			/*formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);*/
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
			//loadPicklist_Address();
			
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			
			formObject.setLocked("AltContactDetails_Frame1",true);
		
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			//12thsept
			//formObject.setLocked("FATCA_Frame1",true);
			formObject.setLocked("FATCA_Frame6",true);
			//12thsept
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("KYC_Frame1",true);
			
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			
			formObject.setLocked("OECD_Frame8",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){

			//formObject.setLocked("IncomingDocument_Frame",true);
			
		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//12th sept
			//formObject.setLocked("Reference_Details_Frame1",true);
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);

			//12th sept
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);

		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CardDetails_Frame1",true);

		}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CompanyDetails_Frame1", true);
        loadPicklist_CompanyDet();
		}
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
//			formObject.setLocked("CompanyDetails_Frame2", true);
				formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		//	formObject.setLocked("CompanyDetails_Frame3", true);
		formObject.setLocked("PartnerDetails_Frame1", true);
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}
		
		//added by yash on 24/8/2017
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//12th sept
			//formObject.setLocked("NotepadDetails_Frame2",true);
			notepad_load();
			notepad_withoutTelLog();
			//12th sept
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			CreditCard.mLogger.info( "Activity name is:" + sActivityName);
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
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName()) && "DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

			//for decision fragment made changes 8th dec 2017	
		/*	formObject.setVisible("DecisionHistory_CheckBox1",false);
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
			formObject.setVisible("cmplx_DEC_ContactPointVeri",false);	                        


			
			formObject.setVisible("DecisionHistory_chqbook",false);	
			formObject.setVisible("DecisionHistory_Label6",false);	
			formObject.setVisible("cmplx_DEC_IBAN_No",false);	
			formObject.setVisible("DecisionHistory_Label5",false);	
			formObject.setVisible("DecisionHistory_Button2",false);	
			formObject.setVisible("cmplx_DEC_NewAccNo",false);	
			formObject.setVisible("cmplx_DEC_ChequebookRef",false);	
			formObject.setVisible("DecisionHistory_Label4",false);	
			formObject.setVisible("DecisionHistory_Label10",false);	
			formObject.setVisible("cmplx_DEC_New_CIFID",false);	
			formObject.setVisible("DecisionHistory_chqbook",false);	
			formObject.setVisible("DecisionHistory_Label27",false);	
			formObject.setVisible("cmplx_DEC_Cust_Contacted",false);	
			formObject.setVisible("DecisionHistory_Label9",false);	
			formObject.setVisible("cmplx_DEC_DCR_Refno",false);	
			formObject.setVisible("DecisionHistory_Decision_ListView1",true);
			formObject.setVisible("DecisionHistory_Decision_Label4",true);
			formObject.setVisible("cmplx_DEC_Remarks",true);
			formObject.setTop("DecisionHistory_save",335);
			formObject.setTop("DecisionHistory_Decision_ListView1",250);
			formObject.setLeft("DecisionHistory_Decision_Label4",280);
			formObject.setLeft("cmplx_DEC_Remarks",280);
			formObject.setTop("cmplx_DEC_Remarks",90);
			formObject.setTop("DecisionHistory_Decision_Label4",74);
			formObject.setTop("DecisionHistory_Decision_Label3",74);
			formObject.setTop("cmplx_DEC_Decision",90);
			
			formObject.setVisible("DecisionHistory_chqbook",false);	
			formObject.setVisible("DecisionHistory_Label6",false);	
			formObject.setVisible("cmplx_DEC_IBAN_No",false);	
			formObject.setVisible("DecisionHistory_Label5",false);	
			formObject.setVisible("DecisionHistory_Button2",false);	
			formObject.setVisible("cmplx_DEC_NewAccNo",false);	
			formObject.setVisible("cmplx_DEC_ChequebookRef",false);	
			formObject.setVisible("DecisionHistory_Label4",false);	
			formObject.setVisible("DecisionHistory_Label10",false);	
			formObject.setVisible("cmplx_DEC_New_CIFID",false);	
			formObject.setVisible("DecisionHistory_chqbook",false);	
			formObject.setVisible("DecisionHistory_Label27",false);	
			formObject.setVisible("cmplx_DEC_Cust_Contacted",false);	
			formObject.setVisible("DecisionHistory_Label9",false);	
			formObject.setVisible("cmplx_DEC_DCR_Refno",false);	
			formObject.setVisible("DecisionHistory_Decision_ListView1",true);
			formObject.setVisible("DecisionHistory_Decision_Label4",true);
			formObject.setVisible("cmplx_DEC_Remarks",true);
			formObject.setTop("DecisionHistory_save",150);
			formObject.setTop("DecisionHistory_Decision_ListView1",190);
			formObject.setLeft("DecisionHistory_Decision_Label4",280);
			formObject.setLeft("cmplx_DEC_Remarks",280);
			formObject.setTop("cmplx_DEC_Remarks",34);
			formObject.setTop("DecisionHistory_Decision_Label4",20);//remarks
			formObject.setTop("DecisionHistory_Decision_Label3",20);//decision
			formObject.setTop("cmplx_DEC_Decision",34);*/
			//12th sept
			  fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			  formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				//for decision fragment made changes 8th dec 2017	
			  loadPicklist3();
			  

		} 	
		//12th sept
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CC_Loan_Frame1",true);
			formObject.setLocked("CC_Loan_Frame2",true);
			formObject.setLocked("CC_Loan_Frame3",true);
			formObject.setLocked("totBalTransfer",true);
			loadPicklist_ServiceRequest();
		}
		
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("PartMatch_Frame1",true);

		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FinacleCRMIncident_Frame1",true);

		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);

		}
		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("ExternalBlackList_Frame1",true);

		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FinacleCore_Frame1",true);
			LoadPickList("FinacleCore_ChequeType", "select '--Select--' union select convert(varchar, description) from ng_MASTER_Cheque_Type with (nolock)");
			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("MOL1_Frame1",true);
			loadPicklist_Mol();
		}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("RejectEnq_Frame1",true);

		}
		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SalaryEnq_Frame1",true);
		}
		//12th sept

	}





}

