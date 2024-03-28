/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : DDVT Maker

File Name                                                         : CC_DDVT_Maker

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Reference No/CR No   Change Date   Changed By    Change Description
1. 				   9-6-2017      Disha         Changes done to load master values in world check birthcountry and residence country
2.					14-9-2017	Saurabh			commented else if condition which was firing customer details at part match.
1005				17-9-2017	Saurabh			added else condition for making TL no in partmatch invisible for Salaried.
1006				17-9-2017	Saurabh			change for overlapping fragments after add to application in Part match
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;


import java.util.List;
import java.util.HashMap;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;

import com.newgen.omniforms.listener.FormListener;


public class CC_DDVT_maker extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	String ReqProd=null;
	
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
		makeSheetsInvisible(tabName, "8,9,11,12,13,14,15,16,17");		

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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			CreditCard.mLogger.info("Inside CC_DDVT_maker CC");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");//added By Akshay on 1/8/2017 to set Mob No 2 in part match
			
			

			//formObject.fetchFragment("ProductContainer", "Product", "q_product");
			//formObject.setNGFrameState("ProductContainer",0);
			//formObject.setNGValue("QueueLabel","CC_DDVT_maker");

			new CC_CommonCode().setFormHeader(pEvent);
			
			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			formObject.setNGFrameState("Part_Match",0);
			String loanAmt = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3);
			formObject.setNGValue("LoanLabel",loanAmt);

			//partMatchValues();


			/*if(formObject.getNGValue("cmplx_PartMatch_partmatch_flag").equalsIgnoreCase("false")){
				formObject.setLocked("PartMatch_Search", true);
			}*/

			//formObject.setNGValue("AppRefNo", formObject.getWFFolderId());



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

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		switch(pEvent.getType())
		{	

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


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub

	}


	public void initialize() {
		// TODO Auto-generated method stub

	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */

	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		//++Below code added by nikhil 11/11/17
		saveIndecisionGrid();
		//saveIndecisionGridCSM(); //Added by arun (22/11/17) to include ddvt maker record in decision grid
		
		//--above code added by nikhil 11//11/17
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
		
		LoadReferGrid();
		////formObject.setNGValue("cmplx_DEC_Remarks","");
		//Tanshu changed for mandatory document check
		//++Below code added by nikhil 11/11/17
		formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));
		formObject.setNGValue("CRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,2));
		try
		{
		CreditCard.mLogger.info("CC val change "+ "befor incomingdoc");
		IncomingDoc();
		CreditCard.mLogger.info("CC val change "+ "after incomingdoc");
		
		
		}
		catch(Exception ex)
		{
			CreditCard.mLogger.info(ex);
			printException(ex);
		}
		//--above code added by nikhil 11//11/17

		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_ContactPointVeri")) || "null".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_ContactPointVeri")) || "false".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_ContactPointVeri")) )
		{
			CreditCard.mLogger.info("CC val change "+ "Inside Y of CPV required");
			formObject.setNGValue("CPV_REQUIRED","Y");
		}
		else
		{
			CreditCard.mLogger.info("CC val change "+ "Inside N of CPV required");
			formObject.setNGValue("CPV_REQUIRED","N");
		}

		
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

			if("".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setLocked("Customer_Frame1",true);
				formObject.setLocked("Customer_save",false);					
				formObject.setLocked("Customer_Frame3",false);
				formObject.setVisible("Customer_Frame2", false);
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
				// ++ below code not commented at offshore - 06-10-2017
				formObject.setLocked("cmplx_Customer_MobileNo",true);
			}
			else{
				//++below code commemted by nikhil 08/12/17
				//formObject.setNGValue("cmplx_Customer_NTB","true");
				// added by abhishek as per CC FSD
				//formObject.setLocked("cmplx_Customer_MobileNo",false);
			}
			formObject.setLocked("cmplx_Customer_CARDNOTAVAIL",true);
			formObject.setLocked("Customer_ReadFromCard",false);
			formObject.setLocked("cmplx_Customer_NEP",true);
			formObject.setLocked("cmplx_Customer_DLNo",false);
			formObject.setLocked("cmplx_Customer_PAssport2",false);
			formObject.setLocked("cmplx_Customer_Passport3",false);
			formObject.setLocked("cmplx_Customer_PASSPORT4",false);
			formObject.setLocked("Customer_Button1", false);
			//formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
			loadPicklistCustomer();

			//added by abhishek on 29may2017

			formObject.setLocked("cmplx_Customer_EmiratesID",false);
			formObject.setLocked("cmplx_Customer_FirstNAme",false);
			formObject.setLocked("cmplx_Customer_MiddleNAme",false);
			formObject.setLocked("cmplx_Customer_LastNAme",false);
			// ++ below code not commented at offshore - 06-10-2017
			//formObject.setLocked("cmplx_Customer_DOb",false);
			formObject.setEnabled("cmplx_Customer_DOb",true);
			formObject.setLocked("cmplx_Customer_Nationality",false);
			// ++ below code commented at offshore - 06-10-2017
			formObject.setLocked("cmplx_Customer_MobileNo",false);
			formObject.setLocked("cmplx_Customer_PAssportNo",false);
			formObject.setLocked("cmplx_Customer_card_id_available",false);
			formObject.setLocked("Customer_FetchDetails",false);
			formObject.setLocked("cmplx_Customer_CIFID_Available",false);
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFID_Available"))){
			formObject.setLocked("cmplx_Customer_CIFNo",false);
			}
			//++Below code added by nikhil 22/11/2017 
			
			formObject.setLocked("cmplx_Customer_IdIssueDate", false);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", false);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", false);
			formObject.setLocked("cmplx_Customer_VisaExpiry", false);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", false);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
			formObject.setLocked("cmplx_Customer_MotherName", false);
			formObject.setLocked("cmplx_Customer_IsGenuine",true);
			//--Above code added by nikhil 22/11/2017 
			// added for point 28
			formObject.setVisible("Customer_ReadFromCard",false);
			// added by abhishek acc. to new FSD for CC
			formObject.setLocked("Customer_FetchDetails",false);	
			setcustomer_enable();
			//below code added by nikhil 08/12/17
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_Customer_MobileNo",false);
			}
			else
			{
				formObject.setLocked("cmplx_Customer_MobileNo",true);
			}
			formObject.setLocked("cmplx_Customer_NTB",true);
			
			if(formObject.getNGValue("cmplx_Customer_age")!=null && !formObject.getNGValue("cmplx_Customer_age").equals("") && Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"))>18){
				 formObject.setNGValue("cmplx_Customer_minor", true);
			 }
		}	
		else if ("Internal_External_Liability".equalsIgnoreCase(pEvent.getSource().getName())) {
			//changes for fragments disabled done by saurabh
			formObject.setLocked("ExtLiability_Frame1", true);
		}
		else if ("Reject_Enquiry".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1", true);
		}
		else if ("CreditCard_Enq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CreditCard_Enq", true);
		}
		else if ("Case_hist".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Case_hist", true);
		}
		else if ("LOS".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LOS", true);
		}
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			//++Below code added by nikhil 7/11/17 as per CC issues sheet
			formObject.setEnabled("ReqProd", false);
			formObject.setEnabled("Product_type",false);
			formObject.setVisible("CardProd",true);
			formObject.setEnabled("CardProd",false);
			//--Above code added by nikhil 7/11/17 as per CC issues sheet
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
		//	formObject.setLocked("AddressDetails_Frame1",true);
			formObject.setLocked("AddressDetails_Frame1",false);
			loadPicklist_Address();
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			formObject.setLocked("AltContactDetails_Frame1",false);
			LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
            

		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			
			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			formObject.setVisible("ELigibiltyAndProductInfo_Refresh", false);
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalInterestRate", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate", true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadInDecGrid();
				LoadReferGrid();
			
			
			formObject.setLocked("cmplx_DEC_New_CIFID",true);
			CreditCard.mLogger.info( "inside decision history fragment load");
          
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Telesales_Init").equalsIgnoreCase(formObject.getNGValue("InitiationType")))
			{
				loadPicklist3();
			}
			else
			{
				loadPicklistChecker();
			}
			
			fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
			//formObject.setTop("DecisionHistory_Decision_ListView1",330);
			//formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+20);
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017
			//Ref. 1007 end.
			//below code added by nikhil
			
			formObject.setLocked("cmplx_DEC_VerificationRequired", true);
		} 	

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			// commented by abhishek as per the CC FSD
			//formObject.setLocked("OECD_Frame8",true);
			loadPicklist_oecd();

		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//formObject.setLocked("KYC_Frame1",true);
			formObject.setLocked("KYC_Frame1",false);

		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("FATCA_Frame6",false);
			// ++ below code not commented at offshore - 06-10-2017
			formObject.setVisible("cmplx_FATCA_DocsCollec", true);
			formObject.setVisible("cmplx_FATCA_TypeOFRelation", true);
			loadPicklist_Fatca();

		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame",false);
			//shifted from CommonCode by akshay on 17/1/18
			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
				formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
			else if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
				//formObject.setVisible("IncomingDoc_UploadSig",true);
				//formObject.setEnabled("IncomingDoc_UploadSig",false);
			}
		}
		else if("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			// added by abhishek as per CC FSD
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)) && "yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Supplementary_Card"))){
				formObject.setLocked("SupplementCardDetails_Frame1",false);
			}
			else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){
				formObject.setLocked("SupplementCardDetails_Frame1",true);
			}
			LoadPickList("SupplementCardDetails_MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode with (nolock) where isActive='Y' order by code");

			
		}
		
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//++Below code added by nikhil 13/11/2017 for Code merge
			//LoadPickList("cmplx_CardDetails_Statement_cycle", "select '--Select--' as Description , '' as code union select convert(varchar, Description),code from NG_MASTER_StatementCycle with (nolock)");
			LoadPickList("CardDetails_BankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
			
			loadDataInCRNGrid();
			
			
			
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		
		
		
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		//added 9/08/2017 Tanshu 
			
			String subprod =formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			CreditCard.mLogger.info( "subprod"+subprod+",emptype:"+empType);
			if("Business titanium Card".equalsIgnoreCase(subprod)&& "Self Employed".equalsIgnoreCase(empType)){
				CreditCard.mLogger.info( "inside if condition");
				formObject.setEnabled("IncomeDEtails", false);
				
			}
			//added 9/08/2017 Tanshu
			formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
			//onsite the field name is cmplx_IncomeDetails_totSal and in offshore file cmplx_IncomeDetails_TotSal
			formObject.setLocked("cmplx_IncomeDetails_totSal", true);
			formObject.setLocked("cmplx_IncomeDetails_netSal1", true);
			formObject.setLocked("cmplx_IncomeDetails_netSal2", true);
			formObject.setLocked("cmplx_IncomeDetails_netSal3", true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal", true);
			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Commission_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Flying_Avg", true);
			/*LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			*/
			loadpicklist_Income();
			//changes for fragments by saurabh
			formObject.setLocked("IncomeDetails_Frame1", true);
			formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
			
		}
		
		else if ("Finacle_CRM_Incidents".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Finacle_CRM_Incidents", true);
		}



		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			CreditCard.mLogger.info( "Fter Load Pick Company: 1");
			//formObject.setLocked("cmplx_CompanyDetails_cmplx_CompanyGrid_CompanyCIF", true);
			formObject.setLocked("CompanyDetails_Button3", true);
			
			CreditCard.mLogger.info( "Fter Load Pick Company: 2");
			//below code added by nikhil 08/12/17
			formObject.setLocked("CompanyDetails_Frame2",true);
			formObject.setLocked("CompanyDetails_Frame3",true);
			//below code added by nikhil
			formObject.setLocked("CompanyDetails_EffectiveLOB", true);
		/*	LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
		*/	loadPicklist_CompanyDet();
		CreditCard.mLogger.info( "Fter Load Pick Company: ");
		//formObject.setLocked("MarketingCode",false);
		//formObject.setLocked("ClassificationCode",false);
		//formObject.setLocked("PromotionCode",false);
		//formObject.setLocked("CompanyDetails_Frame1",true);--commented by akshay for proc 4195
		formObject.setLocked("CompanyDetails_Frame2",true);
		formObject.setLocked("CompanyDetails_Frame3",true);
		formObject.setLocked("AuthorisedSignDetails_DOB",true);
		formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",true);
		formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",true);
		formObject.setLocked("PartnerDetails_Dob",true);
		formObject.setLocked("TLIssueDate",true);
		formObject.setLocked("TLExpiry",true);
		formObject.setLocked("estbDate",true);
			
			}

		/*else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails_AuthSignCIFNo", true);
			formObject.setLocked("AuthorisedSignDetails_Button4", true);
			//LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
			//LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			// added by abhishek as per CC FSD
			formObject.setLocked("AuthorisedSignDetails_DOB",true);
			formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",true);
			formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",true);
		}

		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartnerDetails_Frame1",true);
			formObject.setLocked("PartnerDetails_Dob",true);
		}
*/
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {


			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("EMploymentDetails_Combo5",false);
			formObject.setVisible("EMploymentDetails_Label33",false);
			formObject.setVisible("cmplx_EmploymentDetails_Collectioncode",false);
			formObject.setVisible("EMploymentDetails_Label37",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
			formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
			/*	formObject.setVisible("EMploymentDetails_Label62",false);
					formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			 */  //Code commented because freezone need to be present on DDVT Maker 31/10
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
			//formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			formObject.setVisible("EMploymentDetails_Label36",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
			formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
			loadPicklist4();
			formObject.setLocked("EMploymentDetails_Frame2", true);
			formObject.setLocked("EMploymentDetails_Text21", false);
			formObject.setLocked("EMploymentDetails_Text22", false);
			formObject.setLocked("EMploymentDetails_Button2", false);
			/*// aqdded by abhishek as per CC FSD
			formObject.setVisible("EMploymentDetails_Label25",true);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",true);s
			formObject.setVisible("EMploymentDetails_Label36",true);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",true);
			*/
			//++Below code added by nikhil 7/11/17 as per CC issues sheet
			//below if condition added by 08/12/17
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)))
			{
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt",true);
			formObject.setVisible("EMploymentDetails_Label15",true);
			formObject.setVisible("EMploymentDetails_Label32",true);
			formObject.setVisible("EMploymentDetails_Label36",true);
			formObject.setVisible("cmplx_EmploymentDetails_FieldVisitDone",true);
			}
			//--Above code added by nikhil 7/11/17 as per CC issues sheet
			//++Below code added by nikhil 8/11/17 as per CC issues sheet
			//below code modified by nikhil 08/12/17
			formObject.setLocked("cmplx_EmploymentDetails_empmovemnt",true);
			formObject.setLocked("cmplx_EmploymentDetails_FieldVisitDone",true);
			//--Above code added by nikhil 8/11/17 as per CC issues sheet
			

		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			ChangeRepeater();
			ChangeRepeaterTrnover();
			formObject.setLocked("FinacleCore_Frame9", true);
			formObject.setLocked("FinacleCore_Frame10", true);
			formObject.setLocked("FinacleCore_Frame11", true);
			// added by abhishek as per the CC FSD
			formObject.setLocked("FinacleCore_Frame1", true);
			LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Cheque_Type with (nolock)");
			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' as description union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock) order by description desc");
			//++below code added by nikhil for ddvt CC issues
			formObject.setLocked("FinacleCore_Frame7", true);	
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			try{
				//partMatchValues();
				CreditCard.mLogger.info("CC"+ "Inside partMatch frag loaded!!");
				String Fullname="";
				LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");

				int framestate_Alt_Contact = formObject.getNGFrameState("Alt_Contact_container");
				if(framestate_Alt_Contact == 0){
					CreditCard.mLogger.info("Alternate details alread fetched");
				}
				else {
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					CreditCard.mLogger.info("fetched Alternate Contact details");
				}

				int framestate_CompDetails = formObject.getNGFrameState("CompDetails");
				if(framestate_CompDetails == 0){
					CreditCard.mLogger.info("CompDetails already fetched");
				}
				else {
					formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
					CreditCard.mLogger.info("fetched CompDetails details");
				}
				
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");

			formObject.setNGValue("PartMatch_fname", formObject.getNGValue("cmplx_Customer_FirstNAme"));
			CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_FirstNAme')"+ formObject.getNGValue("cmplx_Customer_FirstNAme"));
			formObject.setNGValue("PartMatch_lname", formObject.getNGValue("cmplx_Customer_LastNAme"));
			CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_LastNAme')"+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			if(formObject.getNGValue("cmplx_Customer_MiddleNAme")!=null || formObject.getNGValue("cmplx_Customer_MiddleNAme").equals("")){
				Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
			}
			else{
				Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_MiddleNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
			}
				
			formObject.setNGValue("PartMatch_funame", Fullname);
			formObject.setNGValue("PartMatch_newpass", formObject.getNGValue("cmplx_Customer_PAssportNo"));
			formObject.setNGValue("PartMatch_oldpass", formObject.getNGValue("cmplx_Customer_FirstNAme")); // old passport no. field to be populated
			formObject.setNGValue("PartMatch_visafno", formObject.getNGValue("cmplx_Customer_VisaNo"));
			formObject.setNGValue("PartMatch_mno1", formObject.getNGValue("AlternateContactDetails_MobileNo1"));
			formObject.setNGValue("PartMatch_mno2", formObject.getNGValue("AlternateContactDetails_MobNo2"));
			formObject.setNGValue("PartMatch_Dob", formObject.getNGValue("cmplx_Customer_DOb"));
			formObject.setNGValue("PartMatch_EID", formObject.getNGValue("cmplx_Customer_EmiratesID"));
			formObject.setNGValue("PartMatch_drno", formObject.getNGValue("cmplx_Customer_DLNo"));
			formObject.setNGValue("PartMatch_nationality", formObject.getNGValue("cmplx_Customer_Nationality"));
			formObject.setNGValue("PartMatch_CIFID", formObject.getNGValue("cmplx_Customer_CIFNo"));
			//added by akshay on 27/12/17
			if(!"Y".equals(formObject.getNGValue("Is_PartMatchSearch"))){
				formObject.setLocked("PartMatch_Blacklist", true);
			}
				if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
					formObject.setVisible("PartMatch_Label10", true);
					formObject.setVisible("PartMatch_compname", true);
					formObject.setVisible("PartMatch_Label9", true);
					formObject.setVisible("PartMatch_TLno", true);
					//Deepak 12Nov2017 changes done to Set comp details from Grid. Set value of First Row.
					int comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					if(comp_row_count>1){
						formObject.setNGValue("PartMatch_compname",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));
						formObject.setNGValue("PartMatch_TLno",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,6));
						/*formObject.setNGValue("PartMatch_compname", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid_companyName"));
						formObject.setNGValue("PartMatch_TLno", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid_TLNO"));*/
					}
				}
				//Ref. 1005
				else{
					
					formObject.setVisible("PartMatch_Label9", false);
					formObject.setVisible("PartMatch_TLno", false);
				}
				//Ref. 1005 end.
			}catch(Exception e){
				CreditCard.mLogger.info("eXCEPTION"+ printException(e));	
			}

		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);

		}
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			loadPicklist_ServiceRequest();
			//below code added by nikhil 19/1/18
			String dds_mode=formObject.getNGValue("cmplx_CC_Loan_DDSMode");
			//below code also fix point "30-Service Details#Validations not as per FSD."
			if("F".equalsIgnoreCase(dds_mode))
			//above code also fix point "30-Service Details#Validations not as per FSD."
			
			{
				
				formObject.setLocked("cmplx_CC_Loan_Percentage",true);
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);
			}
			//below code also fix point "30-Service Details#Validations not as per FSD."
			else if("P".equalsIgnoreCase(dds_mode))
			//above code also fix point "30-Service Details#Validations not as per FSD."
			
			{
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
				formObject.setLocked("cmplx_CC_Loan_Percentage",false);
			}
			else
			{
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);
				formObject.setLocked("cmplx_CC_Loan_Percentage",false);
			}
			 //++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
			//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
			//formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
			//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

		}
		
		else if ("Salary_Enquiry".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Salary_Enquiry", true);
		}
		
		//++ Below Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			notepad_withoutTelLog();
		}
		//++ Above Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("WorldCheck1_age",true);
			 //++ Below Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017++
			loadPicklist_WorldCheck();
			//-- Above Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017--
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())){
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab"))){

				formObject.setVisible("Liability_New_Overwrite", true);

			}
			else{
				formObject.setVisible("Liability_New_Overwrite", false);
			}
			//below code added by nikhil 09/12/17
			if("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))
			{
				formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);
			}
			else
			{
				formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", true);
			}
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");
}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNo");
			String DOB=formObject.getNGValue("cmplx_Customer_DOb");
			String passport=formObject.getNGValue("cmplx_Customer_PAssportNo");
			try{
				String queryForRejectCases="select isnull(customername,''),isnull(employer,''),passportNo,FORMAT(uploaddate,'dd/MM/yyyy'),isnull(category,''),remarks from ng_cas_rejected_table where reject_system='CAS' and cif='"+customer_cif+"'and PassportNo='"+passport+"' and FORMAT(dob,'dd/MM/yyyy')='"+DOB+"'";
				CreditCard.mLogger.info("queryForRejectCases: "+queryForRejectCases);
				List<List<String>> records=formObject.getNGDataFromDataCache(queryForRejectCases);
				CreditCard.mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_RejectEnq_RejectEnqGrid",record);
					}
				}
			}catch(Exception e)
			{
				CreditCard.mLogger.info(e.getMessage());
				printException(e);
			}
		}
	}
	
	


}
