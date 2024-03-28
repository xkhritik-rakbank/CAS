
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

import java.util.HashMap;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


//Changes - 1. On Sales coordinator Incoming Document frame is editable 

public class CC_Sales_coordinator extends CC_Common implements FormListener
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
		
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
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
		
	//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		//CreditCard.mLogger.info( "eventdispatched inside");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info(" Inside event disp CAD1: ");
		
		serverSide(pEvent);

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
			//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().FrameExpandEvent(pEvent);
        break;
                
          case FRAGMENT_LOADED:
        	 // CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
        	  fragment_loaded(pEvent,formObject);
        	  
			  break;
			  
			case MOUSE_CLICKED:
				//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().mouse_clicked(pEvent);
	        	 break;
			 case VALUE_CHANGED:
				//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		CustomSaveForm();

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
		try{
			CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
			CreditCard.mLogger.info( "Inside submitFormStarted()");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("DecisionHistory_Cif_ID",formObject.getNGValue("cmplx_Customer_CIFNo"));
			formObject.setNGValue("DecisionHistory_Emirates_Id",formObject.getNGValue("cmplx_Customer_CIFNo"));
			String fName = formObject.getNGValue("cmplx_Customer_FirstNAme");
	        String mName = formObject.getNGValue("cmplx_Customer_MiddleNAme");
	        String lName = formObject.getNGValue("cmplx_Customer_LastNAme");
	        String fullName = fName+" "+mName+" "+lName; 
	        formObject.setNGValue("DecisionHistory_Customer_Name",fullName);
	      //Added by shivanshi on 17-05-2021
	        CreditCard.mLogger.info( "Before if Salary check");
			CreditCard.mLogger.info( "Before if salary check employment type-->"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
	        if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) ){
	        	CreditCard.mLogger.info( "Inside if employment type income-->"+formObject.getNGValue("cmplx_IncomeDetails_AvgBal"));
	        	if(formObject.getNGValue("cmplx_IncomeDetails_AvgBal") != null ){
	        		float avgBal = Float.valueOf(formObject.getNGValue("cmplx_IncomeDetails_AvgBal").replace(",", ""));
	        		if(avgBal >= 50000.00){
	        			CreditCard.mLogger.info( "Inside if Income Credit Card");
	        			formObject.setNGValue("Refer_Color","IN_G");
	        		}
				}
			}else{
				CreditCard.mLogger.info( "Inside else employment type income-->"+formObject.getNGValue("cmplx_IncomeDetails_totSal"));
				if(formObject.getNGValue("cmplx_IncomeDetails_totSal") != null){
					if(formObject.getNGValue("cmplx_IncomeDetails_totSal") != null ){
		        		float sal = Float.valueOf(formObject.getNGValue("cmplx_IncomeDetails_totSal").replace(",", ""));
		        		if(sal >= 25000.00){
		        			CreditCard.mLogger.info( "Inside if Income Credit Card");
		        			formObject.setNGValue("Refer_Color","IN_G");
		        		}
					}
				}
			}
	      //Added by shivanshi on 17-05-2021
			saveIndecisionGridCSM();
		}catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormStarted"+e.toString()+"["+e.getStackTrace()[0].getLineNumber()+"]");
		}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		//saveIndecisionGrid();
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
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info( "start of customer");
				
				try{
					
					if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
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

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");

			disableButtonsCC("Product");

		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			disableButtonsCC("IncomeDetails");


			loadpicklist_Income();
		}

		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {


			disableButtonsCC("AuthorisedSignDetails");

			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		}

		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			disableButtonsCC("PartnerDetails");

			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("PartnerDetails_signcapacity", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SigningCapacity with (nolock) where isActive='Y' order by code");

		}

		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			disableButtonsCC("CompanyDetails");
			loadPicklist_CompanyDet();
			hideAtCSMCompany();
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

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
			formObject.setVisible("Liability_New_Delinkinlast3months",true);
			formObject.setVisible("Liability_New_DPDinlast6",true);
			formObject.setVisible("Liability_New_DPDinlast18months",true);
			formObject.setVisible("Liability_New_Label4",true);
			formObject.setVisible("Liability_New_writeOfAmount",true);
			formObject.setVisible("Liability_New_Label5",true);
			formObject.setVisible("Liability_New_worstStatusInLast24",true);
			formObject.setVisible("Liability_New_Label10",true);
			formObject.setVisible("Liability_New_Text2",true);
			CreditCard.mLogger.info("after liability Details of CSM123");						
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {


			disableButtonsCC("EMploymentDetails");
			
			//loadPicklistEmployment();
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
			//p1-7,ALOC fields should be disabled for CSM user
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
			///formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
			//formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			//formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
			formObject.setLocked("cmplx_EmploymentDetails_Freezone",true); 
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
			formObject.setLocked("EMploymentDetails_Button2",false);
			//p1-7,ALOC fields should be disabled for CSM user
			// Ref. 1003 end.
			CreditCard.mLogger.info( "sagarika sales");
			//sagarika 28/07
			if((!("false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL")))))
		{
				CreditCard.mLogger.info( "sagarika if sales");
				
                formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", true);
				}
			else
			{
				CreditCard.mLogger.info( "sagarika else sales");
				formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
			}
			
			//sagarika 28/07
			
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			disableButtonsCC("ELigibiltyAndProductInfo");


		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			disableButtonsCC("AddressDetails");
			loadPicklist_Address();



		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			disableButtonsCC("AltContactDetails");

			/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			disableButtonsCC("FATCA");

			/*formObject.setEnabled("FATCA_Save",true);*/
		}



		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){

			disableButtonsCC("OECD");

			/*formObject.setEnabled("OECD_Save",true);*/
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){

			disableButtonsCC("IncomingDocument");
			//++ Below Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
			CreditCard.mLogger.info("before incomingdoc");

			//fetchIncomingDocRepeater();//Commented for sonar
			CreditCard.mLogger.info("after incomingdoc");
			//++ Above Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++

		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {

			disableButtonsCC("Reference_Details");
			LoadPickList("Reference_Details_ReferenceRelatiomship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");

		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			disableButtonsCC("SupplementCardDetails");


		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			disableButtonsCC("CardDetails");
			//Ref. 1002
			LoadPickList("CardDetails_Combo2","select '--Select--'  as description union select convert(varchar,description) from NG_MASTER_MarketingCode with (nolock) ");
			formObject.setVisible("cmplx_CardDetails_Security_Check_Held", false);
			LoadPickList("CardDetails_FeeProfile","select '--Select--'  as description union select convert(varchar,description) from ng_master_feeProfile with (nolock) ");
			formObject.setVisible("CardDetails_Label12", false);
			LoadPickList("CardDetails_InterestFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_InterestProfile with (nolock) ");
			formObject.setVisible("cmplx_CardDetails_MarketingCode", false);
			LoadPickList("CardDetails_TransactionFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_TransactionFeeProfile with (nolock) ");
			formObject.setVisible("CardDetails_Label8", false);

			formObject.setVisible("cmplx_CardDetails_Bank_Name", false);
			formObject.setVisible("CardDetails_Label9", false);
			formObject.setVisible("cmplx_CardDetails_Cheque_Number", false);
			formObject.setVisible("CardDetails_Label10", false);
			formObject.setVisible("cmplx_CardDetails_Amount", false);
			formObject.setVisible("CardDetails_Label11", false);
			formObject.setVisible("cmplx_CardDetails_Date", false);
			formObject.setVisible("CardDetails_Label13", false);
			formObject.setVisible("cmplx_CardDetails_CustClassification", false);
			//Ref. 1002 end.
		}
		//added by yash on 22/8/2017
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
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


		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Ref 1000. changes done by saurabh on 17th Sept for keeping sync in decision history of CSM and initiation.
			  //for decision fragment made changes 8th dec 2017
			/*	formObject.setVisible("DecisionHistory_CheckBox1",false);
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
			
			formObject.setVisible("cmplx_DEC_ChequebookRef",false);
			
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
			formObject.setVisible("DecisionHistory_dec_reason_code",true);
			formObject.setEnabled("DecisionHistory_dec_reason_code",true);
			formObject.setVisible("DecisionHistory_Label11",true);
			formObject.setVisible("DecisionHistory_Rejreason",true);
			formObject.setVisible("cmplx_DEC_RejectReason",true);
			formObject.setEnabled("cmplx_DEC_RejectReason",true);*/
			//12th sept
			CreditCard.mLogger.info("***********Inside checker decision history");
        	fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Label7,cmplx_DEC_NewAccNo#DecisionHistory_Label6,cmplx_DEC_IBAN_No#DecisionHistory_Button2#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
        	  formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
        	CreditCard.mLogger.info("***********Inside checker after fragment alignment decision history");
        	  //for decision fragment made changes 8th dec 2017
			disableButtonsCC("DecisionHistory");
			//formObject.setTop("DecisionHistory_save",240);
			//formObject.setTop("DecisionHistory_Decision_ListView1",300);
			//Ref 1000. changes done by saurabh on 17th Sept for keeping sync in decision history of CSM and initiation end.



			loadPicklist3();
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

