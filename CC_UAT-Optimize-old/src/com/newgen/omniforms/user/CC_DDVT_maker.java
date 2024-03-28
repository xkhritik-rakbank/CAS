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
	Description                         : For Fetching the Fragments              

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
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		//Tanshu changed for mandatory document check
		CreditCard.mLogger.info("CC val change "+ "befor incomingdoc");
		IncomingDoc();
		CreditCard.mLogger.info("CC val change "+ "after incomingdoc");
		saveIndecisionGrid();


		if(formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("null") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("false") )
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
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {

			if(formObject.getNGValue("cmplx_Customer_NEP").equals("false")){
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
				formObject.setNGValue("cmplx_Customer_NTB","true");
				// added by abhishek as per CC FSD
				formObject.setLocked("cmplx_Customer_MobileNo",false);
			}
			formObject.setLocked("cmplx_Customer_CARDNOTAVAIL",true);
			formObject.setLocked("Customer_ReadFromCard",false);
			formObject.setLocked("cmplx_Customer_NEP",true);
			formObject.setLocked("cmplx_Customer_DLNo",false);
			formObject.setLocked("cmplx_Customer_PAssport2",false);
			formObject.setLocked("cmplx_Customer_Passport3",false);
			formObject.setLocked("cmplx_Customer_PASSPORT4",false);
			//	formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
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
			if(formObject.getNGValue("cmplx_Customer_CIFID_Available").equalsIgnoreCase("true")){
				formObject.setLocked("cmplx_Customer_CIFNo",false);
			}

			// added for point 28
			formObject.setVisible("Customer_ReadFromCard",false);
			// added by abhishek acc. to new FSD for CC
			formObject.setLocked("Customer_FetchDetails",false);	
			setcustomer_enable();
		}	
		else if (pEvent.getSource().getName().equalsIgnoreCase("Internal_External_Liability")) {
			//changes for fragments disabled done by saurabh
			formObject.setLocked("ExtLiability_Frame1", true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enquiry")) {
			formObject.setLocked("RejectEnq_Frame1", true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("CreditCard_Enq")) {
			formObject.setLocked("CreditCard_Enq", true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("Case_hist")) {
			formObject.setLocked("Case_hist", true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
			formObject.setLocked("LOS", true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//	formObject.setLocked("AddressDetails_Frame1",true);
			formObject.setLocked("AddressDetails_Frame1",false);
			loadPicklist_Address();
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			formObject.setLocked("AltContactDetails_Frame1",false);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			//formObject.setVisible("DecisionHistory_Label6",false);
			//formObject.setVisible("DecisionHistory_Label11",false);
			//formObject.setVisible("DecisionHistory_Combo6",false);
			formObject.setVisible("DecisionHistory_Button2",false);
			loadPicklist1();
			// added by abhishek as per CC fSD
			// hide
			formObject.setVisible("DecisionHistory_Text15",false);
			formObject.setVisible("DecisionHistory_Text16",false);
			formObject.setVisible("DecisionHistory_Text17",false);
			formObject.setVisible("cmplx_DEC_ContactPointVeri", false);
			formObject.setVisible("cmplx_DEC_ChequebookRef",false);
			formObject.setVisible("cmplx_DEC_DCR_Refno",false);
			formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
			formObject.setVisible("DecisionHistory_chqbook", false);
			formObject.setVisible("DecisionHistory_Label8",false);
			formObject.setVisible("DecisionHistory_Label9",false);
			formObject.setVisible("DecisionHistory_Label27",false);
			formObject.setVisible("DecisionHistory_Label6", false);
			formObject.setVisible("cmplx_DEC_IBAN_No", false);
			formObject.setVisible("DecisionHistory_Label7", false);
			formObject.setVisible("cmplx_DEC_NewAccNo", false);


			// show
			formObject.setVisible("DecisionHistory_Rejreason",true);
			formObject.setVisible("cmplx_DEC_RejectReason",true);

			// set alignment of fields
			maker_Decision();

			formObject.setTop("DecisionHistory_Decision_ListView1",330);
			//++ Below Code added By abhishek on Oct 6, 2017  to fix : "31-CPV required field to be disabled" : Reported By Shashank on Oct 05, 2017++
			formObject.setEnabled("cmplx_DEC_VerificationRequired", false);
			//-- Above Code added By abhishek on Oct 6, 2017  to fix : "31-CPV required field to be disabled" : Reported By Shashank on Oct 05, 2017--
		} 	

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
			// commented by abhishek as per the CC FSD
			//formObject.setLocked("OECD_Frame8",true);
			loadPicklist_oecd();

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//formObject.setLocked("KYC_Frame1",true);
			formObject.setLocked("KYC_Frame1",false);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("FATCA_Frame6",false);
			// ++ below code not commented at offshore - 06-10-2017
			loadPicklist_Fatca();

		}
		if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
			// added by abhishek as per CC FSD
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_CardDetails_Supplementary_Card").equalsIgnoreCase("yes")){
				formObject.setLocked("SupplementCardDetails_Frame1",false);
			}
			else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Personal Loan")){
				formObject.setLocked("SupplementCardDetails_Frame1",true);
			}

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//formObject.setLocked("CardDetails_Frame1",true);
			formObject.setLocked("CardDetails_Frame1",false);
			// added by abhishek as per CC FSD
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("salaried")){
				formObject.setVisible("CardDetails_Label2",false);
				formObject.setVisible("CardDetails_Text4",false);
			}
			else{
				formObject.setVisible("CardDetails_Label2",true);
				formObject.setVisible("CardDetails_Text4",true);
			}
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0).equalsIgnoreCase("Islamic")){
				formObject.setLocked("CardDetails_Text2",true);
				formObject.setLocked("CardDetails_Text3",true);
			}
			else{
				formObject.setLocked("CardDetails_Text2",false);
				formObject.setLocked("CardDetails_Text3",false);
			}
			formObject.setLocked("CardDetails_Text6",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {

			//added 9/08/2017 Tanshu 

			String subprod =formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			CreditCard.mLogger.info( "subprod"+subprod+",emptype:"+empType);
			if(subprod.equalsIgnoreCase("Business titanium Card")&& empType.equalsIgnoreCase("Self Employed")){
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
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");

			//changes for fragments by saurabh
			formObject.setLocked("IncomeDetails_Frame1", true);
			formObject.setLocked("IncomeDetails_FinacialSummarySelf", false);

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_Incidents")) {
			formObject.setLocked("Finacle_CRM_Incidents", true);
		}



		else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
			formObject.setLocked("cmplx_CompanyDetails_cmplx_CompanyGrid_CompanyCIF", true);
			formObject.setLocked("CompanyDetails_Button3", true);
			/*	LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
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

		else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {

			formObject.setLocked("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails_AuthSignCIFNo", true);
			formObject.setLocked("AuthorisedSignDetails_Button4", true);
			//LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
			//LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			// added by abhishek as per CC FSD
			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {

			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			formObject.setLocked("PartnerDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {


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



		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {


			/*if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){
			    					if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
			    						formObject.setVisible("FinacleCore_avgbal", true);
			    						CreditCard.mLogger.info( "set visible FinacleCore_Frame8 if condition ");
			    						formObject.setVisible("FinacleCore_Frame8", true);
			    						CreditCard.mLogger.info( "after set visible FinacleCore_Frame8 if condition ");
			    						formObject.setVisible("FinacleCore_Frame2", false);
			    						formObject.setVisible("FinacleCore_Frame3", false);
			    						CreditCard.mLogger.info( "Inside fianacle core fragment if condition ");
			    					}
			    					else{
			    						formObject.setVisible("FinacleCore_Frame2", true);
			    						formObject.setVisible("FinacleCore_Frame3", true);
			    						formObject.setVisible("FinacleCore_avgbal", false);
			    						CreditCard.mLogger.info( "ELSE set visible FinacleCore_Frame8 ELSE condition ");
			    						formObject.setVisible("FinacleCore_Frame8", false);
			    						CreditCard.mLogger.info( "AFTER  Inside fianacle core fragment else condition ");
			    					}
			    				}*/

			ChangeRepeater();
			ChangeRepeaterTrnover();
			formObject.setLocked("FinacleCore_Frame9", true);
			formObject.setLocked("FinacleCore_Frame10", true);
			formObject.setLocked("FinacleCore_Frame11", true);
			// added by abhishek as per the CC FSD
			formObject.setLocked("FinacleCore_Frame1", true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
			//formObject.setNGFrameState("ProductContainer", 0);
			try{
				//partMatchValues();
				CreditCard.mLogger.info("CC"+ "Inside partMatch frag loaded!!");
				String Fullname="";
				LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");

				formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");

				formObject.setNGValue("PartMatch_fname", formObject.getNGValue("cmplx_Customer_FirstNAme"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_FirstNAme')"+ formObject.getNGValue("cmplx_Customer_FirstNAme"));
				formObject.setNGValue("PartMatch_lname", formObject.getNGValue("cmplx_Customer_LastNAme"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_LastNAme')"+ formObject.getNGValue("cmplx_Customer_LastNAme"));
				if(formObject.getNGValue("cmplx_Customer_MiddleNAme")!=null || formObject.getNGValue("cmplx_Customer_MiddleNAme").equals("")){
					Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
				}
				else
					Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_MiddleNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
				formObject.setNGValue("PartMatch_funame", Fullname);
				formObject.setNGValue("PartMatch_newpass", formObject.getNGValue("cmplx_Customer_PAssportNo"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_PAssportNo')"+ formObject.getNGValue("cmplx_Customer_PAssportNo"));
				formObject.setNGValue("PartMatch_oldpass", formObject.getNGValue("cmplx_Customer_FirstNAme")); // old passport no. field to be populated
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_FirstNAme')"+ formObject.getNGValue("cmplx_Customer_FirstNAme"));
				formObject.setNGValue("PartMatch_visafno", formObject.getNGValue("cmplx_Customer_VisaNo"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_VisaNo')"+ formObject.getNGValue("cmplx_Customer_VisaNo"));
				formObject.setNGValue("PartMatch_mno1", formObject.getNGValue("AltContactDetails_Text4"));
				CreditCard.mLogger.info("formObject.getNGValue('AltContactDetails_Text4')"+ formObject.getNGValue("AltContactDetails_Text4"));
				formObject.setNGValue("PartMatch_mno2", formObject.getNGValue("AltContactDetails_Text11"));
				CreditCard.mLogger.info("formObject.getNGValue('AltContactDetails_Text11')"+ formObject.getNGValue("AltContactDetails_Text11"));
				formObject.setNGValue("PartMatch_Dob", formObject.getNGValue("cmplx_Customer_DOb"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_DOb')"+ formObject.getNGValue("cmplx_Customer_DOb"));
				formObject.setNGValue("PartMatch_EID", formObject.getNGValue("cmplx_Customer_EmiratesID"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_EmiratesID')"+ formObject.getNGValue("cmplx_Customer_EmiratesID"));
				formObject.setNGValue("PartMatch_drno", formObject.getNGValue("cmplx_Customer_DLNo"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_DLNo')"+ formObject.getNGValue("cmplx_Customer_DLNo"));
				formObject.setNGValue("PartMatch_nationality", formObject.getNGValue("cmplx_Customer_Nationality"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_Nationality')"+ formObject.getNGValue("cmplx_Customer_Nationality"));
				formObject.setNGValue("PartMatch_CIFID", formObject.getNGValue("cmplx_Customer_CIFNo"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_CIFNo')"+ formObject.getNGValue("cmplx_Customer_CIFNo"));


				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("SE")){

					formObject.setVisible("PartMatch_Label10", true);
					formObject.setVisible("PartMatch_compname", true);
					formObject.setVisible("PartMatch_Label9", true);
					formObject.setVisible("PartMatch_TLno", true);

					formObject.setNGValue("PartMatch_compname", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid_companyName"));
					formObject.setNGValue("PartMatch_TLno", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid_TLNO"));


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
		else if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
			formObject.setLocked("Reference_Details_Frame1",true);

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			loadPicklist_ServiceRequest();

			//++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
			//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
			formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
			//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Salary_Enquiry")) {
			formObject.setLocked("Salary_Enquiry", true);
		}

		//++ Below Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++
		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
			notepad_load();
			notepad_withoutTelLog();
		}
		//++ Above Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++

		else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")){
			formObject.setLocked("WorldCheck1_age",true);
			//++ Below Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017++
			loadPicklist_WorldCheck();
			//-- Above Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017--
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")){
			LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")){
			if(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab").equalsIgnoreCase("true")){

				formObject.setVisible("Liability_New_Overwrite", true);

			}
			else{
				formObject.setVisible("Liability_New_Overwrite", false);
			}
		}
	}




}

