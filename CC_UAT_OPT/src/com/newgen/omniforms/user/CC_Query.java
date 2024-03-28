package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_Query extends CC_Common implements FormListener{

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
	//	CreditCard.mLogger.info("Inside initiation PL");
		//CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
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
            //loadPicklistCustomer();
            //formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
            //formObject.setNGFrameState("Part_Match",0);
            //loadPicklist_PartMatch();
			new CC_CommonCode().setFormHeader(pEvent);
            CreditCard.mLogger.info("Inside Part_Match");            
            //LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			//LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where Code='PL' and ActivityName='Branch_Init'");
			
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
		// TODO Auto-generated method stub
		
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
			  
			default: break;
	     
	     }

	}	
	
	
	public void initialize() {/*
		CreditCard.mLogger.info( "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	*/}

	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {/*
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	*/}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {/*
		  formObject =FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	*/}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {/*
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	*/}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {/*
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		 
		 if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Parallel"))
		 {
			 formObject.setNGValue("parallel_sequential","P");
		 }
		 else
		 {
			 formObject.setNGValue("parallel_sequential","S");
		 }
		saveIndecisionGrid();
	*/}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
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

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1", true);
			loadPicklistCustomer();
			
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1", true);
			//loadProductCombo();
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1", true);	
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 630);
			formObject.setHeight("IncomeDetails_Frame1", 605); 
			loadpicklist_Income();
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1", true);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();

		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			//	formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AddressDetails_Frame1", true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("AltContactDetails_Frame1",true);
			//	formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
		} 


		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("FATCA_Frame6", true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("KYC_Frame1",true);
			//	formObject.setEnabled("KYC_Save",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("OECD_Frame8",true);
			//	formObject.setEnabled("OECD_Save",true);
		}

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("IncomingDoc_Frame1", true);
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
			LoadPickList("PartMatch_nationality", "select '--Select--' union select convert(varchar, Description) from ng_MASTER_Country with (nolock)");
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("FinacleCore_ChequeType", "select '--Select--' union select convert(varchar, description) from ng_MASTER_Cheque_Type with (nolock)");
			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Mol();
			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}
		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}

		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("NotepadDetails_Frame1",true);
			formObject.setLocked("NotepadDetails_Frame2",true);
			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			CreditCard.mLogger.info( "Activity name is:" + sActivityName);
			int user_id = formObject.getUserId();
			String user_name = formObject.getUserName();
			user_name = user_name+"-"+user_id;					
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
			formObject.setNGValue("NotepadDetails_Actusername",user_name); 
			formObject.setNGValue("NotepadDetails_user",user_name); 
			formObject.setLocked("NotepadDetails_noteDate",true);
			formObject.setLocked("NotepadDetails_Actusername",true);
			formObject.setLocked("NotepadDetails_user",true);
			formObject.setLocked("NotepadDetails_insqueue",true);
			formObject.setLocked("NotepadDetails_Actdate",true);
			formObject.setVisible("NotepadDetails_save",true);
			formObject.setLocked("NotepadDetails_notecode",true);

			//formObject.setHeight("NotepadDetails_Frame1",450);//Arun (23/09/17)
			formObject.setTop("NotepadDetails_save",440);//Arun (23/09/17)
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017	
			fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			  formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				//for decision fragment made changes 8th dec 2017	
			loadPicklist1();

			//disha FSD --Select-- come at top of drop down
			//for decision fragment made changes 8th dec 2017	
		/*	formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");

			formObject.setVisible("DecisionHistory_Label8", false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
			formObject.setVisible("DecisionHistory_Label9", false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
			formObject.setVisible("DecisionHistory_Label5", false);
			formObject.setVisible("cmplx_Decision_desc", false);
			formObject.setVisible("DecisionHistory_Label3", false);
			formObject.setVisible("cmplx_Decision_strength", false);
			formObject.setVisible("DecisionHistory_Label4", false);
			formObject.setVisible("cmplx_Decision_weakness", false);

			formObject.setVisible("DecisionHistory_Button4",false);
			formObject.setVisible("cmplx_Decision_Deviationcode",false);
			formObject.setVisible("DecisionHistory_Label14",false);
			formObject.setVisible("cmplx_Decision_Dectech_decsion",false);
			formObject.setVisible("DecisionHistory_Label15",false);
			formObject.setVisible("cmplx_Decision_score_grade",false);
			formObject.setVisible("DecisionHistory_Label16",false);
			formObject.setVisible("cmplx_Decision_Highest_delegauth",false);
			formObject.setVisible("cmplx_Decision_Manual_Deviation",false);
			formObject.setVisible("DecisionHistory_Button6",false);
			formObject.setVisible("cmplx_Decision_Manual_deviation_reason",false);
			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setVisible("cmplx_Decision_refereason",false);
			formObject.setVisible("DecisionHistory_Label1",false);
			formObject.setVisible("DecisionHistory_Label26",true);
			formObject.setVisible("cmplx_Decision_AppID",true);

			formObject.setLocked("cmplx_Decision_IBAN",true);
			formObject.setLocked("cmplx_Decision_AppID",true);
			formObject.setLocked("cmplx_Decision_AccountNo",true);
			formObject.setTop("DecisionHistory_Label7", 8);
			formObject.setTop("cmplx_Decision_AccountNo", 23);
			formObject.setTop("Decision_Label3", 56);
			formObject.setTop("cmplx_Decision_Decision", 72);
			formObject.setTop("Decision_Label4", 56);
			formObject.setTop("cmplx_Decision_REMARKS", 72);

			formObject.setTop("Decision_Label4", 104);
			formObject.setTop("cmplx_Decision_REMARKS", 120);

			formObject.setTop("DecisionHistory_Label6", 8);
			formObject.setTop("cmplx_Decision_IBAN", 23);						
			formObject.setTop("DecisionHistory_Label26", 56);
			formObject.setTop("cmplx_Decision_AppID", 92);				
			formObject.setTop("cmplx_Decision_cmplx_gr_decision", 226);		
			formObject.setTop("DecisionHistory_save", 400);	

			formObject.setTop("Decision_Label1", 8);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);

			formObject.setLeft("DecisionHistory_Label26", 1000);
			formObject.setLeft("cmplx_Decision_AppID", 1000);
			formObject.setLeft("Decision_Label4", 352);
			formObject.setLeft("cmplx_Decision_REMARKS", 352);
			formObject.setLeft("Decision_Label4", 672);
			formObject.setLeft("cmplx_Decision_REMARKS", 672);					

			formObject.setLeft("Decision_Label1", 1000);
			formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 1000);

			formObject.setLeft("Decision_Label4", 352);
			formObject.setLeft("cmplx_Decision_REMARKS", 352);

			formObject.setTop("Decision_Label4", 56);
			formObject.setTop("cmplx_Decision_REMARKS", 72);
			
			formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
			formObject.setLocked("cmplx_Decision_Decision",true);
			formObject.setLocked("cmplx_Decision_REMARKS",true);
			formObject.setLocked("DecisionHistory_Frame1",true);*/
			//for decision fragment made changes 8th dec 2017	
			//Common function for decision fragment textboxes and combo visibility
			//decisionLabelsVisibility();
		} 	
		//code by saurabh gupta on 27th June 17.
		/*String hiddenFieldString = formObject.getNGValue("fields_string");
		if(hiddenFieldString!=null && !hiddenFieldString.equalsIgnoreCase("") && !hiddenFieldString.equalsIgnoreCase(" ")){
			String[] fieldNames = hiddenFieldString.split(",");
			setChangedFieldsColor(fieldNames);
		}*/

	}
	


}
