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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_DDVT_maker extends CC_Common implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	FormReference formObject = null;
	String ReqProd=null;
	
	
	public void formLoaded(FormEvent pEvent)
	{
	
		SKLogger_CC.writeLog("CC_DDVT_maker", "Inside formLoaded()" + pEvent.getSource().getName());
		makeSheetsInvisible(tabName, "8,9,11,12,13,14,15,16,17");		

	}



	public void formPopulated(FormEvent pEvent) 

	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			SKLogger_CC.writeLog("CC_DDVT_maker","Inside CC_DDVT_maker CC");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");//added By Akshay on 1/8/2017 to set Mob No 2 in part match
			
			new CC_CommonCode().setFormHeader(pEvent);
			
			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			formObject.setNGFrameState("Part_Match",0);
			String loanAmt = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3);
			formObject.setNGValue("LoanLabel",loanAmt);

		}catch(Exception e)
		{
			SKLogger_CC.writeLog("CC_DDVT_maker", "Exception:"+e.getMessage());
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
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String outputResponse = "";
		String ReturnCode="";
		String ReturnDesc="";
		String alert_msg="";
		
		SKLogger_CC.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			SKLogger_CC.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().FrameExpandEvent(pEvent);						
			break;

		case FRAGMENT_LOADED:
			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
				formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
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
			if (pEvent.getSource().getName().equalsIgnoreCase("Internal_External_Liability")) {
				//changes for fragments disabled done by saurabh
				formObject.setLocked("ExtLiability_Frame1", true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enquiry")) {
				formObject.setLocked("RejectEnq_Frame1", true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CreditCard_Enq")) {
				formObject.setLocked("CreditCard_Enq", true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Case_hist")) {
				formObject.setLocked("Case_hist", true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
				formObject.setLocked("LOS", true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
				//Tanshu Aggarwal changed as per the conversation with Shashank
			//	formObject.setLocked("AddressDetails_Frame1",true);
				formObject.setLocked("AddressDetails_Frame1",false);
				loadPicklist_Address();
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
				//Tanshu Aggarwal changed as per the conversation with Shashank
				formObject.setLocked("AltContactDetails_Frame1",false);

			}

			if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
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

			if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
				// commented by abhishek as per the CC FSD
				//formObject.setLocked("OECD_Frame8",true);
				loadPicklist_oecd();

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
				//Tanshu Aggarwal changed as per the conversation with Shashank
				//formObject.setLocked("KYC_Frame1",true);
				formObject.setLocked("KYC_Frame1",false);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
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
			if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
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
			if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			
			//added 9/08/2017 Tanshu 
				
				String subprod =formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
				String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
				SKLogger_CC.writeLog("CC", "subprod"+subprod+",emptype:"+empType);
				if(subprod.equalsIgnoreCase("Business titanium Card")&& empType.equalsIgnoreCase("Self Employed")){
					SKLogger_CC.writeLog("CC", "inside if condition");
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
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_Incidents")) {
				formObject.setLocked("Finacle_CRM_Incidents", true);
			}



			if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
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

			if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {

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

			if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {

				LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
				formObject.setLocked("PartnerDetails_Frame1",true);
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {


				formObject.setVisible("EMploymentDetails_Label25",false);
				formObject.setVisible("EMploymentDetails_Combo5",false);
				formObject.setVisible("EMploymentDetails_Label33",false);
				formObject.setVisible("cmplx_EmploymentDetails_Collectioncode",false);
				formObject.setVisible("EMploymentDetails_Label37",false);
				formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
				formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
				formObject.setVisible("EMploymentDetails_Label62",false);
				formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
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

			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {


				/*if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){
				    					if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
				    						formObject.setVisible("FinacleCore_avgbal", true);
				    						SKLogger_CC.writeLog("CC", "set visible FinacleCore_Frame8 if condition ");
				    						formObject.setVisible("FinacleCore_Frame8", true);
				    						SKLogger_CC.writeLog("CC", "after set visible FinacleCore_Frame8 if condition ");
				    						formObject.setVisible("FinacleCore_Frame2", false);
				    						formObject.setVisible("FinacleCore_Frame3", false);
				    						SKLogger_CC.writeLog("CC", "Inside fianacle core fragment if condition ");
				    					}
				    					else{
				    						formObject.setVisible("FinacleCore_Frame2", true);
				    						formObject.setVisible("FinacleCore_Frame3", true);
				    						formObject.setVisible("FinacleCore_avgbal", false);
				    						SKLogger_CC.writeLog("CC", "ELSE set visible FinacleCore_Frame8 ELSE condition ");
				    						formObject.setVisible("FinacleCore_Frame8", false);
				    						SKLogger_CC.writeLog("CC", "AFTER  Inside fianacle core fragment else condition ");
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

			if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
				//formObject.setNGFrameState("ProductContainer", 0);
				try{
					partMatchValues();
					SKLogger_CC.writeLog("CC", "Inside partMatch frag loaded!!");

					LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");

					/*	formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");

				formObject.setNGValue("PartMatch_fname", formObject.getNGValue("cmplx_Customer_FirstNAme"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_FirstNAme')", formObject.getNGValue("cmplx_Customer_FirstNAme"));
				formObject.setNGValue("PartMatch_lname", formObject.getNGValue("cmplx_Customer_LastNAme"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_LastNAme')", formObject.getNGValue("cmplx_Customer_LastNAme"));
				if(formObject.getNGValue("cmplx_Customer_MiddleNAme")!=null || formObject.getNGValue("cmplx_Customer_MiddleNAme").equals("")){
					Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
				}
				else
					Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_MiddleNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
				formObject.setNGValue("PartMatch_funame", Fullname);
				formObject.setNGValue("PartMatch_newpass", formObject.getNGValue("cmplx_Customer_PAssportNo"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_PAssportNo')", formObject.getNGValue("cmplx_Customer_PAssportNo"));
				formObject.setNGValue("PartMatch_oldpass", formObject.getNGValue("cmplx_Customer_FirstNAme")); // old passport no. field to be populated
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_FirstNAme')", formObject.getNGValue("cmplx_Customer_FirstNAme"));
				formObject.setNGValue("PartMatch_visafno", formObject.getNGValue("cmplx_Customer_VisaNo"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_VisaNo')", formObject.getNGValue("cmplx_Customer_VisaNo"));
				formObject.setNGValue("PartMatch_mno1", formObject.getNGValue("AltContactDetails_Text4"));
				SKLogger_CC.writeLog("formObject.getNGValue('AltContactDetails_Text4')", formObject.getNGValue("AltContactDetails_Text4"));
				formObject.setNGValue("PartMatch_mno2", formObject.getNGValue("AltContactDetails_Text11"));
				SKLogger_CC.writeLog("formObject.getNGValue('AltContactDetails_Text11')", formObject.getNGValue("AltContactDetails_Text11"));
				formObject.setNGValue("PartMatch_Dob", formObject.getNGValue("cmplx_Customer_DOb"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_DOb')", formObject.getNGValue("cmplx_Customer_DOb"));
				formObject.setNGValue("PartMatch_EID", formObject.getNGValue("cmplx_Customer_EmiratesID"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_EmiratesID')", formObject.getNGValue("cmplx_Customer_EmiratesID"));
				formObject.setNGValue("PartMatch_drno", formObject.getNGValue("cmplx_Customer_DLNo"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_DLNo')", formObject.getNGValue("cmplx_Customer_DLNo"));
				formObject.setNGValue("PartMatch_nationality", formObject.getNGValue("cmplx_Customer_Nationality"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_Nationality')", formObject.getNGValue("cmplx_Customer_Nationality"));
				formObject.setNGValue("PartMatch_CIFID", formObject.getNGValue("cmplx_Customer_CIFNo"));
				SKLogger_CC.writeLog("formObject.getNGValue('cmplx_Customer_CIFNo')", formObject.getNGValue("cmplx_Customer_CIFNo"));
					 */

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
					SKLogger_CC.writeLog("eXCEPTION", printException(e));	
				}

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
				formObject.setLocked("Reference_Details_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
				formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
				formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
				formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
				loadPicklist_ServiceRequest();
				
				 //++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
				//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
				formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
				//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Salary_Enquiry")) {
				formObject.setLocked("Salary_Enquiry", true);
			}
			
			//++ Below Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++
			if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
				notepad_load();
				notepad_withoutTelLog();
			}
			//++ Above Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++

			if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")){
				formObject.setLocked("WorldCheck1_age",true);
				 //++ Below Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017++
				loadPicklist_WorldCheck();
				//-- Above Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017--
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")){
				LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")){
				if(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab").equalsIgnoreCase("true")){

					formObject.setVisible("Liability_New_Overwrite", true);

				}
				else{
					formObject.setVisible("Liability_New_Overwrite", false);
				}
			}






			break;

		case MOUSE_CLICKED:
			
			CC_Integration_Input Intgration_input = new CC_Integration_Input();
			CC_Integration_Output Intgration_Output = new CC_Integration_Output();
			
			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
				//GenerateXML();
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
				formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			}

			// added by yash
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTotal")){
				try {
					SKLogger_CC.writeLog("PL_Initiationavgbal", "Inside Customer_save button: ");
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC");	


				} catch (Exception e) {
					SKLogger_CC.writeLog("test ", e.toString());
				}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTurnover")){
				try {
					SKLogger_CC.writeLog("PL_Initiationavgbal", "Inside Customer_save button: ");
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_APR1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1");	


				} catch (Exception e) {
					SKLogger_CC.writeLog("test ", e.toString());
				}


				/*CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
							CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
							CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
							CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
							CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
							CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
							CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
							CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	*/

			}
			// ended by yash

			//Added to add modify delete in the Liability grid
			if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button2"))
			{
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button3"))
			{
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button4"))
			{
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
			}
			//Added to add modify delete in the Liability grid
			
			if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
				SKLogger_CC.writeLog("PL_Initiation", "Inside Customer_save button: ");
				formObject.saveFragment("CustomerDetails");
			}

			/*
						if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
							formObject.saveFragment("ProductContainer");
						}
			 */
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
				formObject.saveFragment("Supplementary_Container");
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



			// added by abhishek

			/*if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button1")){
							formObject.saveFragment("Part_Match");

							//throw new ValidatorException(new CustomExceptionHandler("", "Customer_save#Customer Save Successful","", hm));
						}*/
			if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Reference_Add"))
			{
				formObject.setNGValue("reference_wi_name",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("reference_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Customer_cmplx_ReferenceGrid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Reference__modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Customer_cmplx_ReferenceGrid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Reference_delete"))
			{

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Customer_cmplx_ReferenceGrid");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Add")){

				formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");

				formObject.setNGValue("Product_type","Conventional",false);
				formObject.setNGValue("ReqProd","--Select--",false);
				formObject.setNGValue("AppType","--Select--",false);
				formObject.setNGValue("EmpType","--Select--",false);
				formObject.setNGValue("Priority","Primary",false);
				//formObject.setVisible("cmplx_Product_cmplx_ProductGrid_Priority",false); //tenor
				formObject.setVisible("Product_Label3",false); // scheme lable
				//formObject.setVisible("Product_Label6",false); // card prod
				//formObject.setVisible("Product_Label10",false);
				//formObject.setVisible("Product_Label12",false);
				//formObject.setVisible("CardProd",false);
				formObject.setVisible("Scheme",false);
				formObject.setVisible("ReqTenor",false);
				formObject.setVisible("Product_Label15",false); // type of req
				//formObject.setVisible("Product_Label17",false); 
				formObject.setVisible("Product_Label16",false); // new limit value
				formObject.setVisible("Product_Label18",false); // limit exp
				//formObject.setVisible("Product_Label21",false); 
				//formObject.setVisible("Product_Label22",false); 
				//formObject.setVisible("Product_Label23",false); 
				//formObject.setVisible("Product_Label24",false);
				formObject.setVisible("typeReq",false);
				formObject.setVisible("LimitAcc",false); 
				formObject.setVisible("LimitExpiryDate",false);
				formObject.setNGFrameState("Incomedetails", 1);
				formObject.setNGFrameState("EmploymentDetails", 1);
				formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				formObject.setNGFrameState("Alt_Contact_container", 1);
				formObject.setNGFrameState("CC_Loan_container", 1);
				formObject.setNGFrameState("CompanyDetails", 1);
				formObject.setNGFrameState("CardDetails", 1);
				
				//09/08/2017 to make company details invisible
				SKLogger_CC.writeLog("CC", "Inside add button:value of emp "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
					formObject.setSheetVisible("Tab1", 1, false);
				}
				//09/08/2017 to make company details invisible 
			}	
			if (pEvent.getSource().getName().equalsIgnoreCase("Modify")){

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
				formObject.setNGFrameState("Incomedetails", 1);
				formObject.setNGValue("Product_type","Conventional",false);
				formObject.setNGValue("ReqProd","--Select--",false);
				formObject.setNGValue("AppType","--Select--",false);
				formObject.setNGValue("EmpType","--Select--",false);
				formObject.setNGValue("Priority","Primary",false);
				formObject.setVisible("Product_Label5",false); //tenor
				formObject.setVisible("Product_Label3",false); // scheme lable
				//formObject.setVisible("Product_Label6",false); // card prod
				//formObject.setVisible("Product_Label10",false);
				//formObject.setVisible("Product_Label12",false);
				//formObject.setVisible("CardProd",false);
				formObject.setVisible("Scheme",false);
				formObject.setVisible("ReqTenor",false);
				formObject.setVisible("Product_Label15",false); // type of req
				//formObject.setVisible("Product_Label17",false); 
				formObject.setVisible("Product_Label16",false); // new limit value
				formObject.setVisible("Product_Label18",false); // limit exp
				//formObject.setVisible("Product_Label21",false); 
				//formObject.setVisible("Product_Label22",false); 
				//formObject.setVisible("Product_Label23",false); 
				//formObject.setVisible("Product_Label24",false);
				formObject.setVisible("typeReq",false);
				formObject.setVisible("LimitAcc",false); 
				formObject.setVisible("LimitExpiryDate",false);
				formObject.setNGFrameState("Incomedetails", 1);
				formObject.setNGFrameState("EmploymentDetails", 1);
				formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				formObject.setNGFrameState("Alt_Contact_container", 1);
				formObject.setNGFrameState("CC_Loan_container", 1);
				formObject.setNGFrameState("CompanyDetails", 1);
				formObject.setNGFrameState("CardDetails", 1);
				
				//09/08/2017 to make company details invisible
				SKLogger_CC.writeLog("CC", "Inside add button:value of emp "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
					formObject.setSheetVisible("Tab1", 1, false);
					SKLogger_CC.writeLog("CC", "Inside add button:value of emp inside if of modify");
				}
				else{
					formObject.setSheetVisible("Tab1", 1, true);
					SKLogger_CC.writeLog("CC", "Inside add button:value of emp inside else of modify");
				}
				//09/08/2017 to make company details invisible 
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

				formObject.setNGValue("Product_type","Conventional",false);
				formObject.setNGValue("ReqProd","--Select--",false);
				formObject.setNGValue("AppType","--Select--",false);
				formObject.setNGValue("EmpType","--Select--",false);
				formObject.setNGValue("Priority","Primary",false);
				formObject.setVisible("Product_Label5",false); //tenor
				formObject.setVisible("Product_Label3",false); // scheme lable
				//formObject.setVisible("Product_Label6",false); // card prod
				//formObject.setVisible("Product_Label10",false);
				//formObject.setVisible("Product_Label12",false);
				//formObject.setVisible("CardProd",false);
				formObject.setVisible("Scheme",false);
				formObject.setVisible("ReqTenor",false);
				formObject.setVisible("Product_Label15",false); // type of req
				//formObject.setVisible("Product_Label17",false); 
				formObject.setVisible("Product_Label16",false); // new limit value
				formObject.setVisible("Product_Label18",false); // limit exp
				//formObject.setVisible("Product_Label21",false); 
				//formObject.setVisible("Product_Label22",false); 
				//formObject.setVisible("Product_Label23",false); 
				//formObject.setVisible("Product_Label24",false);
				formObject.setVisible("typeReq",false);
				formObject.setVisible("LimitAcc",false); 
				formObject.setVisible("LimitExpiryDate",false);
				formObject.setNGFrameState("Incomedetails", 1);
				formObject.setNGFrameState("EmploymentDetails", 1);
				formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				formObject.setNGFrameState("Alt_Contact_container", 1);
				formObject.setNGFrameState("CC_Loan_container", 1);
				formObject.setNGFrameState("CompanyDetails", 1);
				formObject.setNGFrameState("CardDetails", 1);
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
				SKLogger_CC.writeLog("CC Product Save111111111111111111", "");
				formObject.saveFragment("ProductContainer");
				SKLogger_CC.writeLog("CC Product Save222222222222222222", "");
				throw new ValidatorException(new CustomExceptionHandler("", "Product_Save#Product Save Successful","", hm));
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
				formObject.saveFragment("IncomeDetails");
				throw new ValidatorException(new CustomExceptionHandler("", "IncomeDetails_Salaried_Save#IncomeDetails_Salaried Save Successful","", hm));
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
				formObject.saveFragment("Incomedetails");
				throw new ValidatorException(new CustomExceptionHandler("", "IncomeDetails_SelfEmployed_Save#IncomeDetails_Salaried Save Successful","", hm));
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
				String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
				String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
				SKLogger_CC.writeLog("RLOS", "EMpName$"+EmpName+"$");
				String query=null;
				//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017
				if(EmpName.trim().equalsIgnoreCase(""))
					query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";


				else
					query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";


				SKLogger_CC.writeLog("RLOS", "query is: "+query);
				populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,MAIN EMPLOYER CODE", true, 20);			     
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
				formObject.saveFragment("EmploymentDetails");
				throw new ValidatorException(new CustomExceptionHandler("", "EMploymentDetails_Save#EMploymentDetails Save Successful","", hm));
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button1")){
				formObject.saveFragment("Auth_Sign_Details");
				throw new ValidatorException(new CustomExceptionHandler("", "AuthorisedSignDetails_Button1#AuthorisedSignDetails_Button1 Save Successful","", hm));
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("Button6")){
				SKLogger_CC.writeLog("CC Current Selected Sheet is",formObject.getSelectedSheet(pEvent.getSource().getName())+"");
				formObject.setSelectedSheet("ParentTab",3);		
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligProd");
				formObject.setNGFrameState("EligibilityAndProductInformation", 0);
				formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				formObject.setNGFrameState("EligibilityAndProductInformation", 0);
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
				formObject.saveFragment("EligibilityAndProductInformation");
				throw new ValidatorException(new CustomExceptionHandler("", "ELigibiltyAndProductInfo_Save#ELigibiltyAndProductInfo Save Successful","", hm));
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1")){
				formObject.saveFragment("EligibilityAndProductInformation");
				throw new ValidatorException(new CustomExceptionHandler("", "ELigibiltyAndProductInfo_Button1#ELigibiltyAndProductInfo  Successful","", hm));
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Add"))
			{
				formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("company_winame"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_delete"))
			{

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_add"))
			{
				formObject.setNGValue("PartnerDetails_partner_winame",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("PartnerDetails_partner_winame"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_delete"))
			{

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_add"))
			{
				formObject.setNGValue("AuthorisedSignDetails_AuthorisedSign_wiName",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("AuthorisedSignDetails_AuthorisedSign_wiName"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_delete"))
			{

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			}
			
			//Tanshu Aggarwal Reference Details Add,Delete,Modify functioning (17/08/2017)
			if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference_Add"))
			{
				formObject.setNGValue("Reference_Details_reference_wi_name",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("Reference_Details_reference_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference__modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference_delete"))
			{
				
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
				 String query="DELETE  FROM ng_rlos_ReferenceDetails WHERE wi_name='"+formObject.getWFWorkitemName()+"'";
				 SKLogger_CC.writeLog("CC","query"+query);
				 List<List<String>> list=formObject.getDataFromDataSource(query);
				 SKLogger_CC.writeLog("CC","list"+list);
			}
			
			//Tanshu Aggarwal Reference Details Add,Delete,Modify functioning (17/08/2017)
			if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
				SKLogger_CC.writeLog("CC value of ReturnCode","CompanyDetails_Button3");
				/* outputResponse = GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
	                       ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                       SKLogger.writeLog("CC value of ReturnCode",ReturnCode);
	                       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                       SKLogger.writeLog("CC value of ReturnDesc",ReturnDesc);
	                       String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
	                       SKLogger.writeLog("CC value of CustId",CustId);
	                       String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
	                       SKLogger.writeLog("CC value of CorpName",CorpName);
	                       String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
	                       SKLogger.writeLog("CC value of BusinessIncDate",BusinessIncDate);
	                       String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
	                       SKLogger.writeLog("CC value of LegEnt",LegEnt);
	                       if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
	                           formObject.setNGValue("Is_Customer_Details","Y");

	                           SKLogger.writeLog("CC value of EID_Genuine","value of company Details corporation"+formObject.getNGValue("Is_Customer_Details"));

	                           valueSetCustomer(outputResponse);  
	                            try{

	                                String Date1=BusinessIncDate;
	                                SKLogger.writeLog("CC value of Date1111",Date1);
	                                 SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
	                                 SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
	                                 String Datechanged=sdf2.format(sdf1.parse(Date1));
	                                 SKLogger.writeLog("CC value ofDatechanged",Datechanged);
	                                 formObject.setNGValue("cmplx_CompanyDetails_DateOfEstb",Datechanged);
	                                }
	                                catch(Exception ex){

	                                }
	                           SKLogger.writeLog("CC value of Customer Details","corporation cif");
	                           SKLogger.writeLog("CC value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
	                           }
	                           else{
	                               SKLogger.writeLog("Customer Details","Customer Details Corporation CIF is not generated");
	                               formObject.setNGValue("Is_Customer_Details","N");
	                           }*/
				SKLogger_CC.writeLog("CC value of  Corporation CIF",formObject.getNGValue("Is_Customer_Details"));
			}

			/* if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button2"))
						 {
							 SKLogger_CC.writeLog("Inside search button part match","");
						 }*/
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button1"))
			{
				SKLogger_CC.writeLog("Inside Add button FinacleCore_Button1 core","");
				formObject.ExecuteExternalCommand("NGAddRow", "FinacleCore_ListView2");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button2"))
			{
				SKLogger_CC.writeLog("Inside modify button FinacleCore_Button2 core","");
				formObject.ExecuteExternalCommand("NGModifyRow", "FinacleCore_ListView2");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button3"))
			{
				SKLogger_CC.writeLog("Inside delete button Finacle core","");
				formObject.ExecuteExternalCommand("NGDeleteRow", "FinacleCore_ListView2");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button4"))
			{
				SKLogger_CC.writeLog("Inside Add button Finacle core","");
				formObject.ExecuteExternalCommand("NGAddRow", "FinacleCore_ListView8");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button5"))
			{
				SKLogger_CC.writeLog("Inside modify button Finacle core","");
				formObject.ExecuteExternalCommand("NGModifyRow", "FinacleCore_ListView8");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button6"))
			{
				SKLogger_CC.writeLog("Inside delete button Finacle core","");
				formObject.ExecuteExternalCommand("NGDeleteRow", "FinacleCore_ListView8");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button1"))
			{
				SKLogger_CC.writeLog("Inside Add button WorldCheck1_Button1 ","");
				formObject.setNGValue("WorldCheck1_WiName",formObject.getWFWorkitemName());
				formObject.setNGValue("Is_WorldCheckAdd","Y");

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button2"))
			{
				SKLogger_CC.writeLog("Inside modify button WorldCheck1_Button2 core","");
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button3"))
			{
				SKLogger_CC.writeLog("Inside delete button WorldCheck1_Button3 core","");

				formObject.setNGValue("Is_WorldCheckAdd","N");
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident_Button1")){
				formObject.saveFragment("Finacle_CRM_Incidents");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_Button1")){
				formObject.saveFragment("Finacle_CRM_CustomerInformation");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button8")){
				formObject.saveFragment("Finacle_Core");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("MOL1_Button1")){
				formObject.saveFragment("MOL");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button3")){
				formObject.saveFragment("Part_Match");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button4")){
				formObject.saveFragment("World_Check");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq_Button1")){
				formObject.saveFragment("Salary_Enquiry");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("RejectEnq_Save")){
				formObject.saveFragment("Reject_Enquiry");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button1")){
				formObject.saveFragment("CompDetails");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Button1")){
				formObject.saveFragment("Partner_Details");
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
			// added by abhishek as per CC FSD 
			if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_add();
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_modify();
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_delete();
			}
			// added by abhishek as per CC FSD
			if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
				Notepad_grid();
			} 
			if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
				formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
			}
			//ended by aysh on24/8/2017
			if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
				formObject.saveFragment("DecisionHistory");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_FetchDetails"))
			{
				if(formObject.getNGValue("cmplx_Customer_CIFID_Available").equalsIgnoreCase("false")){
					outputResponse =Intgration_input.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
					SKLogger_CC.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						Intgration_Output.valueSetCustomer(outputResponse,"");    
						formObject.setNGValue("Is_Customer_Eligibility","Y");
						parse_cif_eligibility(outputResponse,"Primary_CIF");
						String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
						if(NTB_flag.equalsIgnoreCase("true")){
							SKLogger_CC.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
							alert_msg = "Customer is a New to Bank Customer.";
						}
						else{
							alert_msg = "Existing Customer details fetched Successfully";
							outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
							SKLogger_CC.writeLog("RLOS value of ReturnCode","Inside Customer");
							if(ReturnCode.equalsIgnoreCase("0000")){
								formObject.setNGValue("Is_Customer_Details","Y");
								SKLogger_CC.writeLog("RLOS value of Is_Customer_Details",formObject.getNGValue("Is_Customer_Details"));
								formObject.setEnabled("Customer_Button1",true);
								Intgration_Output.valueSetCustomer(outputResponse,"Primary_CIF");
								formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);
							/*	try{
									String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
									SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
									SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
									if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
										String n_str_dob=sdf2.format(sdf1.parse(str_dob));
										SKLogger_CC.writeLog("converting date entered",n_str_dob+"asd");
										formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
									}
								}
								catch(Exception e){
									SKLogger_CC.writeLog("Exception Occur while converting date",e+"");
								} */    
							}
							else{
								alert_msg = "Error in fetch Customer details operation";
								SKLogger_CC.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
							}
							formObject.RaiseEvent("WFSave");
						}
					}
					else{
						SKLogger_CC.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
						alert_msg = "Error in Customer Eligibility operation";
					}
				}
				else{
					/*formObject.setNGValue("Is_Customer_Eligibility","N");
					popupFlag="Y";
					// alert_msg = "Dedupe check failed.";
					SKLogger_CC.writeLog("Dedupe check failed.","");
					//  throw new ValidatorException(new FacesMessage("Dedupe check failed."));
					try{
						//  throw new ValidatorException(new FacesMessage(alert_msg));
					}
					finally{hm.clear();}*/
					// customer details called by saurabh on 23rd Sept for Demo findings.
					if(!formObject.getNGValue("cmplx_Customer_CIFNo").equalsIgnoreCase("") && !formObject.getNGValue("cmplx_Customer_CIFNo").equalsIgnoreCase(" ")){
					outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
					SKLogger_CC.writeLog("RLOS value of ReturnCode","Inside Customer");
					if(ReturnCode.equalsIgnoreCase("0000")){
						formObject.setNGValue("Is_Customer_Details","Y");
						SKLogger_CC.writeLog("RLOS value of Is_Customer_Details",formObject.getNGValue("Is_Customer_Details"));
						formObject.setEnabled("Customer_Button1",true);
						Intgration_Output.valueSetCustomer(outputResponse,"Primary_CIF");
						formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);
					/*	try{
							String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
							SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
							SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
							if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
								String n_str_dob=sdf2.format(sdf1.parse(str_dob));
								SKLogger_CC.writeLog("converting date entered",n_str_dob+"asd");
								formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
							}
						}
						catch(Exception e){
							SKLogger_CC.writeLog("Exception Occur while converting date",e+"");
						}*/     
					}
					else{
						alert_msg = "Error in fetch Customer details operation";
						SKLogger_CC.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
					}



					formObject.RaiseEvent("WFSave");
					}
					// customer details called by saurabh on 23rd Sept for Demo findings end.
				}
				//added
			}
			//ended

			//added
			/*else if(formObject.getNGValue("cmplx_Customer_CIFID_Available")=="true"){
				SKLogger_CC.writeLog("RLOS value of Customer Details ----1234567",formObject.getNGValue("cmplx_Customer_CIFID_Available"));
				SKLogger_CC.writeLog("RLOS value of Customer Details ----1234567","inside true");
				outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
				try
				{
					String Date1=formObject.getNGValue("cmplx_Customer_DOb");
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
					SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
					String Datechanged=sdf2.format(sdf1.parse(Date1));
					SKLogger_CC.writeLog("RLOS value ofDatechanged",Datechanged);
					formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
				}        
				catch(Exception ex){                            
				}


				SKLogger_CC.writeLog("RLOS value of ReturnCode","Inside Customer");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){
					alert_msg= "Existing Customer details fetched Sucessfully";



					formObject.setLocked("Customer_Button1", false);


					formObject.setLocked("Customer_FetchDetails", true);


					formObject.setNGValue("Is_Customer_Details","Y");
					formObject.RaiseEvent("WFSave");

					SKLogger_CC.writeLog("RLOS value of Is_Customer_Details",formObject.getNGValue("Is_Customer_Details"));


					formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
					formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
					formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
					formObject.setEnabled("Customer_Button1", true);	
					SKLogger_CC.writeLog("year difference:","diffdays difference after button is enabled");
					if(formObject.isVisible("Supplementary_Cont")){
						formObject.setTop("Supplementary_Cont",formObject.getTop("Card_Details")+25);
						formObject.setTop("FATCA",formObject.getTop("Supplementary_Cont")+25);
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
						//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded
					}
					else{
						formObject.setTop("FATCA",formObject.getTop("Card_Details")+25);
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
					}
					valueSetCustomer(outputResponse,"Primary_CIF");
					//code to set Emirates of residence start.
					int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					if(n>0){
						for(int i=0;i<n;i++){
							SKLogger_CC.writeLog("selecting Emirates of residence: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
							if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("RESIDENCE")){
								SKLogger_CC.writeLog("selecting Emirates of residence: settign value: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
								formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
							}
						}
						//code to set Emirates of residence End.
						formObject.setEnabled("Customer_FetchDetails", false); 
						throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));


					}
					//code change to save the date in desired format by AMAN	
					try{
						String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
						SKLogger_CC.writeLog("converting date entered",str_dob+"");
						String str_IDissuedate=formObject.getNGValue("cmplx_Customer_IdIssueDate");
						String str_passExpDate=formObject.getNGValue("cmplx_Customer_PassPortExpiry");
						String str_visaExpDate=formObject.getNGValue("cmplx_Customer_VIsaExpiry");

						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
						if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
							String n_str_dob=sdf2.format(sdf1.parse(str_dob));
							SKLogger_CC.writeLog("converting date entered",n_str_dob+"asd");

							formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
						}


					}
					catch(Exception e){
						SKLogger_CC.writeLog("Exception Occur while converting date",e+"");
					}     
				}
				else{
					alert_msg = "Error in Customer details operation";
					formObject.RaiseEvent("WFSave");
					SKLogger_CC.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
				}

				formObject.RaiseEvent("WFSave");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}*/

			else if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_FetchDetails")){
				
				String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text6");
				SKLogger_CC.writeLog("CC_DDVT_MAker ", "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
				String primaryCif = null;
				boolean isEntityDetailsSuccess = false;
				
				if( EmiratesID!=null && !EmiratesID.equalsIgnoreCase("")){
					outputResponse = Intgration_input.GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger_CC.writeLog("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: ",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						//valueSetCustomer(outputResponse , "Supplementary_Card_Details");
						primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
						formObject.setNGValue("Supplementary_CIFNO",primaryCif);
						isEntityDetailsSuccess = true;
						alert_msg = fetch_cust_details_supplementary();
					}

					SKLogger_CC.writeLog("CC_DDVT_MAker value of Primary Cif",primaryCif);
				}
				if(!isEntityDetailsSuccess || (primaryCif==null || primaryCif.equalsIgnoreCase(""))){
					outputResponse =Intgration_input.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
					SKLogger_CC.writeLog("CC_DDVT_MAker value of ReturnDesc for Supplementary Cards: ","Customer Eligibility");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger_CC.writeLog("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: ",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") )
					{
						Intgration_Output.valueSetCustomer(outputResponse ,"Supplementary_Card_Details");    
						parse_cif_eligibility(outputResponse,"Supplementary_Card_Details");
						alert_msg = fetch_cust_details_supplementary();
						
					}
				}
				
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Button1"))
			{
				//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
				String NegatedFlag="";
				String BlacklistFlag="";
				String DuplicationFlag="";
				outputResponse =Intgration_input.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				SKLogger_CC.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("RLOS value of ReturnCode",ReturnCode);
				formObject.setNGValue("Is_Customer_Eligibility","Y");


				if(ReturnCode.equalsIgnoreCase("0000")){
					Intgration_Output.valueSetCustomer(outputResponse,""); 
					parse_cif_eligibility(outputResponse,"Primary_CIF");
					BlacklistFlag = (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
					SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is BlacklistedFlag"+BlacklistFlag);
					DuplicationFlag = (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
					SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is DuplicationFlag"+DuplicationFlag);
					NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
					SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is NegatedFlag"+NegatedFlag);
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					formObject.RaiseEvent("WFSave");
					SKLogger_CC.writeLog("RLOS value of ReturnDesc Is_Customer_Eligibility",formObject.getNGValue("Is_Customer_Eligibility"));
					formObject.setNGValue("BlacklistFlag",BlacklistFlag);
					formObject.setNGValue("DuplicationFlag",DuplicationFlag);
					formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					if(NTB_flag.equalsIgnoreCase("true")){
						SKLogger_CC.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = "Customer is a New to Bank Customer.";
					}
					else{
						alert_msg = "Customer is an Existing Customer.";
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					formObject.RaiseEvent("WFSave");
				}
				SKLogger_CC.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Eligibility"));
				SKLogger_CC.writeLog("RLOS value of BlacklistFlag",formObject.getNGValue("BlacklistFlag"));
				SKLogger_CC.writeLog("RLOS value of DuplicationFlag",formObject.getNGValue("DuplicationFlag"));
				SKLogger_CC.writeLog("RLOS value of IsAcctCustFlag",formObject.getNGValue("IsAcctCustFlag"));
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));
				//}
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Search")){
				//GenerateXML();
				SKLogger_CC.writeLog("PL PartMatch_Search", "Inside PartMatch_Search button: ");

				//HashMap<String,String> hm1= new HashMap<String,String>(); // not nullable HashMap

				//hm1.put("PartMatch_Search","Clicked");
				// popupFlag="N";
				outputResponse = Intgration_input.GenerateXML("DEDUP_SUMMARY","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("PL value of ReturnCode",ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
				SKLogger_CC.writeLog("PL value of ReturnDesc",ReturnDesc);
				if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
					//valueSetCustomer(outputResponse);	
					formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
					parseDedupe_summary(outputResponse);	
					formObject.setNGValue("Is_PartMatchSearch","Y");
					SKLogger_CC.writeLog("PL value of Part Match Request","inside if of partmatch");
					alert_msg= "Part match sucessfull";
				}
				else{
					formObject.setNGValue("Is_PartMatchSearch","N");
					SKLogger_CC.writeLog("PL value of Part Match Request","inside else of partmatch");
					alert_msg= "Error while performing Part match";
				}
				formObject.RaiseEvent("WFSave");
				SKLogger_CC.writeLog("PL value of Part Match Request",formObject.getNGValue("Is_PartMatchSearch"));
				if((formObject.getNGValue("Is_PartMatchSearch").equalsIgnoreCase("Y")))
				{ 
					SKLogger_CC.writeLog("RLOS value of Is_CUSTOMER_UPDATE_REQ","inside if condition of disabling the button");
				}
				else{
					formObject.setEnabled("PartMatch_Search", true);
					//	throw new ValidatorException(new CustomExceptionHandler("Dedupe Summary Fail!!","PartMatch_Search#Dedupe Summary Fail!!","",hm));
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_Button2")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button1")){
				formObject.fetchFragment("Finacle_CRM_CustomerInformation", "FinacleCRMCustInfo", "q_FinacleCRMCustInfo");
				formObject.setTop("External_Blacklist",formObject.getTop("Finacle_CRM_CustomerInformation")+formObject.getHeight("Finacle_CRM_CustomerInformation")+20);
				formObject.setTop("Finacle_Core",formObject.getTop("External_Blacklist")+30);
				formObject.setTop("MOL",formObject.getTop("Finacle_Core")+30);
				formObject.setTop("World_Check",formObject.getTop("MOL")+30);
				formObject.setTop("Reject_Enquiry",formObject.getTop("World_Check")+30);
				formObject.setTop("Salary_Enquiry",formObject.getTop("Reject_Enquiry")+30);
				//Ref. 1006
				formObject.setTop("CreditCard_Enq",formObject.getTop("Salary_Enquiry")+30);
				formObject.setTop("Case_hist",formObject.getTop("CreditCard_Enq")+30);
				formObject.setTop("LOS",formObject.getTop("Case_hist")+30);
				//Ref. 1006 end.
		
				String BlacklistFlag_Part = "";
				String BlacklistFlag_reason = "";
				String BlacklistFlag_code = "";
				String NegativeListFlag = "";



				outputResponse =Intgration_input.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
				SKLogger_CC.writeLog("CC value of ReturnDesc","Customer Details part Match");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("CC value of ReturnCode part Match",ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){

					BlacklistFlag_Part =  (outputResponse.contains("<BlackListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"";                                  
					formObject.setNGValue("Is_Customer_Details_Part","Y"); 
					BlacklistFlag_reason =  (outputResponse.contains("<BlackListReason>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";
					BlacklistFlag_code =  (outputResponse.contains("<BlackListReasonCode>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";



					NegativeListFlag =  (outputResponse.contains("<NegativeListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
					SKLogger_CC.writeLog("CC value of ReturnCode part Match",ReturnCode);
					SKLogger_CC.writeLog("CC","Value of NegativeListFlag: "+NegativeListFlag+" BlacklistFlag_code:Blacklist Reason: "+BlacklistFlag_reason+": "+BlacklistFlag_code);



					if(BlacklistFlag_Part.equalsIgnoreCase("Y"))
					{

						SKLogger_CC.writeLog("CC value of BlacklistFlag_Part","Customer is Blacklisted");    
					}
					else{
						BlacklistFlag_Part="N";


						SKLogger_CC.writeLog("CC value of BlacklistFlag_Part","Customer is not Blacklisted");    
					}
					//added by Akshay
					if(NegativeListFlag.equalsIgnoreCase("Y"))
					{
						SKLogger_CC.writeLog("CC value of NegativeListFlag","Customer is Negative");    
					}
					else{
						NegativeListFlag="N";
						SKLogger_CC.writeLog("CC value of BlacklistFlag_Part","Customer is not Negative");    
					}//ended By Akshay
				}
				else{


					formObject.setNGValue("Is_Customer_Details_Part","N");
				}
				SKLogger_CC.writeLog("CC value of BlacklistFlag_Part flag"+BlacklistFlag_Part,"");   
				// changed by abhishek start for populating cutomer info grid 22may2017
				try{
					SKLogger_CC.writeLog("CC value of BlacklistFlag_Part flag inside try"+BlacklistFlag_Part,"");    
					List<String> list_custinfo = new ArrayList<String>();
					String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
					String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);

					list_custinfo.add(CIFID);  // cif id from partmatch
					list_custinfo.add("");
					list_custinfo.add(PASSPORTNO); // passport
					list_custinfo.add(NegativeListFlag);
					list_custinfo.add("");
					list_custinfo.add("");
					list_custinfo.add("");
					list_custinfo.add(BlacklistFlag_Part); // blacklist flag
					list_custinfo.add("");
					list_custinfo.add(BlacklistFlag_code);
					list_custinfo.add(BlacklistFlag_reason);
					list_custinfo.add("");
					list_custinfo.add("");
					list_custinfo.add("");
					list_custinfo.add("");
					
					SKLogger_CC.writeLog("CC DDVT Maker", "list_custinfo list values"+list_custinfo);
					formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
				}catch(Exception e){
					SKLogger_CC.writeLog("PL DDVT Maker", "Exception while setting data in grid:"+e.getMessage());
					alert_msg="Error while setting data in finacle customer info grid";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				formObject.RaiseEvent("WFSave");          
			}	 /*if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Button2")){

								outputResponse = GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
								ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								SKLogger.writeLog("CC value of ReturnCode",ReturnCode);
								ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
								SKLogger.writeLog("CC value of ReturnDesc",ReturnDesc);
								if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
									formObject.setNGValue("Is_Customer_Details","Y");
									SKLogger.writeLog("CC value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
									valueSetCustomer(outputResponse);    
									SKLogger.writeLog("CC value of Customer Details","Guarantor_CIF is generated");
									SKLogger.writeLog("CC value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
								}
								else{
									SKLogger.writeLog("Customer Details","Customer Details is not generated");
									formObject.setNGValue("Is_Customer_Details","N");
								}
								SKLogger.writeLog("CC value of Guarantor_CIF",formObject.getNGValue("Is_Customer_Details"));
							}*/
			if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
				SKLogger_CC.writeLog("CC value of ReturnCode","CompanyDetails_Button3");
				outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("CC value of ReturnCode",ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				SKLogger_CC.writeLog("CC value of ReturnDesc",ReturnDesc);
				String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
				SKLogger_CC.writeLog("CC value of CustId",CustId);
				String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
				SKLogger_CC.writeLog("CC value of CorpName",CorpName);
				String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
				SKLogger_CC.writeLog("CC value of BusinessIncDate",BusinessIncDate);
				String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
				SKLogger_CC.writeLog("CC value of LegEnt",LegEnt);
				if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
					formObject.setNGValue("Is_Customer_Details","Y");

					SKLogger_CC.writeLog("CC value of EID_Genuine","value of company Details corporation"+formObject.getNGValue("Is_Customer_Details"));

					Intgration_Output.valueSetCustomer(outputResponse,"Corporation_CIF");  
					try{

						String Date1=BusinessIncDate;
						SKLogger_CC.writeLog("CC value of Date1111",Date1);
						SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
						String Datechanged=sdf2.format(sdf1.parse(Date1));
						SKLogger_CC.writeLog("CC value ofDatechanged",Datechanged);
						formObject.setNGValue("cmplx_CompanyDetails_DateOfEstb",Datechanged);
					}
					catch(Exception ex){

					}
					SKLogger_CC.writeLog("CC value of Customer Details","corporation cif");
					SKLogger_CC.writeLog("CC value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
				}
				else{
					SKLogger_CC.writeLog("Customer Details","Customer Details Corporation CIF is not generated");
					formObject.setNGValue("Is_Customer_Details","N");
				}
				SKLogger_CC.writeLog("CC value of  Corporation CIF",formObject.getNGValue("Is_Customer_Details"));
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button4")){

				outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Authorised_CIF");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("CC value of ReturnCode",ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				SKLogger_CC.writeLog("CC value of ReturnDesc",ReturnDesc);
				if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
					formObject.setNGValue("Is_Customer_Details","Y");
					SKLogger_CC.writeLog("CC value of EID_Genuine","value of Authorised_CIF"+formObject.getNGValue("Is_Customer_Details"));
					Intgration_Output.valueSetCustomer(outputResponse,"Authorised_CIF");    
					SKLogger_CC.writeLog("CC value of Customer Details","Authorised_CIF is generated");
					SKLogger_CC.writeLog("CC value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
				}
				else{
					SKLogger_CC.writeLog("Customer Details","Customer Details is not generated");
					formObject.setNGValue("Is_Customer_Details","N");
				}
				SKLogger_CC.writeLog("CC value of Authorised_CIF",formObject.getNGValue("Is_Customer_Details"));
			}


			//tanshu ended

			if(pEvent.getSource().getName().equalsIgnoreCase("BTC_save") || pEvent.getSource().getName().equalsIgnoreCase("DDS_save") || pEvent.getSource().getName().equalsIgnoreCase("SI_save") || pEvent.getSource().getName().equalsIgnoreCase("RVC_Save")){
				formObject.saveFragment("Details");
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Save")){
				formObject.saveFragment("Part_Match");
			}
			//for BlackList Call added on 3rd May 2017
			if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Blacklist")){
				SKLogger_CC.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111");
				formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
				String StatusType="";
				String StatusFlag="";
				String Reason="";
				String StatusCode="";
				
				// added on 11may 2017
				
				SKLogger_CC.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111 after initializing strings");
				outputResponse =Intgration_input.GenerateXML("BLACKLIST_DETAILS","");
				SKLogger_CC.writeLog("PL value of ReturnDesc","Blacklist Details part Match");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("PL value of ReturnCode part Match",ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				SKLogger_CC.writeLog("PL value of ReturnDesc part Match",ReturnDesc);

				if(ReturnCode.equalsIgnoreCase("0000") ){
					alert_msg = "BlackList Check Successfully: " ;
					formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
					SKLogger_CC.writeLog("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType",formObject.getNGValue("Is_Customer_Eligibility_Part"));
					StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
					SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusType"+StatusType);
					if(StatusType.equalsIgnoreCase("Black List")){
						StatusFlag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
						SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusFlag"+StatusFlag);
						Reason= (outputResponse.contains("<StatusReason>")) ? outputResponse.substring(outputResponse.indexOf("<StatusReason>")+"</StatusReason>".length()-1,outputResponse.indexOf("</StatusReason>")):"";
						SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted Reason"+Reason);
						StatusCode= (outputResponse.contains("<StatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<StatusCode>")+"</StatusCode>".length()-1,outputResponse.indexOf("</StatusCode>")):"";
						SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusCode"+StatusCode);
						alert_msg = alert_msg+StatusType+": "+StatusFlag;
					}
					//added
					outputResponse = (outputResponse.contains("<StatusInfo>")) ? outputResponse.substring(outputResponse.indexOf("</StatusInfo>"),outputResponse.length()-1):"";
					// SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is BlacklistedoutputResponse111 outputResponse"+outputResponse);
					if(outputResponse.contains(StatusType)){
						//	 SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted outputResponse"+outputResponse);
						StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
						StatusFlag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
						//SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusCode");
						alert_msg = alert_msg+", "+StatusType+": "+StatusFlag;
						SKLogger_CC.writeLog("PL value of BlacklistFlag_Part","Customer is StatusType StatusFlag"+StatusType+","+StatusFlag);
					}

					//ended
				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility_Part","N"); 
					alert_msg = "BlackList Check failed Please contact administrator";
					formObject.setNGValue("Is_Customer_Eligibility_Part","N");    
					SKLogger_CC.writeLog("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType",formObject.getNGValue("Is_Customer_Eligibility_Part"));
				}


				formObject.RaiseEvent("WFSave");	

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//ended here for BlackList Call
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToAverage")){
				//addToAvgRepeater();
			}
			if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToTurnover")){
				//addToTrnoverGrid();
			}


			if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_DEC_ContactPointVeri"))
			{
				SKLogger_CC.writeLog("CC val cmplx_DEC_ContactPointVeri ", "Value of cmplx_DEC_ContactPointVeri is:"+formObject.getNGValue("cmplx_DEC_ContactPointVeri"));
				if(formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("null") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("false") )
				{
					SKLogger_CC.writeLog("CC val change ", "Inside Y of CPV required");
					formObject.setNGValue("CPV_REQUIRED","Y");
				}
				else
				{
					SKLogger_CC.writeLog("CC val change ", "Inside N of CPV required");
					formObject.setNGValue("CPV_REQUIRED","N");
				}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTotal")){
				try {
					SKLogger_CC.writeLog("PL_Initiationavgbal", "Inside Customer_save button: ");
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV");	
					CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC");	


				} catch (Exception e) {
					SKLogger_CC.writeLog("test ", e.toString());
				}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTurnover")){
				try {
					SKLogger_CC.writeLog("PL_Initiationavgbal", "Inside Customer_save button: ");
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_APR1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1");	
					CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1");	


				} catch (Exception e) {
					SKLogger_CC.writeLog("test ", e.toString());
				}


				/*CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
									CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
									CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
									CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
									CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
									CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
									CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
									CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	*/

			}
			
			if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Save")){
				formObject.saveFragment("Reference_Details");
			}
		
		
			if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button2")){
				formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: add of card details");
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button3")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button4")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
			}
			
			// added by abhishek on 17 august 2017
				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
					String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
					SKLogger_CC.writeLog("RLOS", "Inside FATCA_Button1 "+text);
					formObject.addItem("cmplx_FATCA_selectedreason", text);
					try {
						formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						printException(e);
					}

				}

				
				 if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button2")){
					SKLogger_CC.writeLog("RLOS", "Inside FATCA_Button2 ");
					formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
					try {
						formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						printException(e);
					}
				}
				
				 if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
					formObject.saveFragment("FATCA");


					alert_msg="FATCA Fields Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				 
				  if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
						SKLogger_CC.writeLog("RLOs", "Inside KYC save button");
						formObject.saveFragment("KYC");
						alert_msg="KYC Fields Saved";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				 
				 if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
						formObject.saveFragment("OECD");
						alert_msg="OECD Fields Saved";

						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD_add")){					
						formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
						SKLogger_CC.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("OECD_winame"));
						//below code by saurabh on 22md july 17.
						formObject.setEnabled("cmplx_OECD_cmplx_GR_OecdDetails_TinReason",true);
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
					}


				  if (pEvent.getSource().getName().equalsIgnoreCase("OECD_modify")){
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
					}


				 if (pEvent.getSource().getName().equalsIgnoreCase("OECD_delete")){
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
					}
				 if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Add"))
					{

						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
					}
					if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Modify"))
					{

						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
					}
					if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Delete"))
					{

						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
					}
					// added by abhishek as per CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("BT_Add")){
						//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
						//SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("BT_Modify")){
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("BT_Delete")){
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Add")){
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Modify")){
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Delete")){
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Save")){
						formObject.saveFragment("WorldCheck1_Frame1");
					}

			break;




		case VALUE_CHANGED:
			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
				if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
				{
					formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
					SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

				}

				else{

					formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
					SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
				}
			}
			 //added by yash on 30/8/2017
			// Code added to Load targetSegmentCode on basis of code
			if (pEvent.getSource().getName().equalsIgnoreCase("ApplicationCategory")){
				SKLogger_CC.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
				String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
				String appCategory=formObject.getNGValue("ApplicationCategory");
				SKLogger_CC.writeLog("RLOS val change ", "Value of subproduct is:"+formObject.getNGValue("subproduct"));
				if(appCategory!=null &&  appCategory.equalsIgnoreCase("BAU")){
					LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
				}
				else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
					LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
				}
				else{
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
				}
			}
			// Code added to Load targetSegmentCode on basis of code
		 
			if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
				 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
				 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
				 String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
				 SKLogger_CC.writeLog(" query is ",sQuery);
				 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
				 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
				 {
					 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
					 SKLogger_CC.writeLog(" notepad details workstep value ",recordList.get(0).get(1));
					 formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
				 }
				 
			
				 
			 }
			if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
				ReqProd=formObject.getNGValue("ReqProd");
				SKLogger_CC.writeLog("CC val change ", "Value of ReqProd is:"+ReqProd);
				loadPicklistProduct(ReqProd);
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("subProd")){
				SKLogger_CC.writeLog("CC val change ", "Value of SubProd is:"+formObject.getNGValue("subProd"));
				formObject.clear("AppType");
				formObject.setNGValue("AppType","--Select--");
				if(formObject.getNGValue("subProd").equalsIgnoreCase("Business titanium Card")){
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
					formObject.setNGValue("EmpType","Self Employed");
				}	
				else if(formObject.getNGValue("subProd").equalsIgnoreCase("Instant Money")){
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");
					formObject.setNGValue("EmpType","Salaried");
				}

				else if(formObject.getNGValue("subProd").equalsIgnoreCase("Limit Increase"))
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");

				else if(formObject.getNGValue("subProd").equalsIgnoreCase("Salaried Credit Card"))
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");

				else if(formObject.getNGValue("subProd").equalsIgnoreCase("Self Employed Credit Card"))
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");

				else if(formObject.getNGValue("subProd").equalsIgnoreCase("Expat Personal Loans"))
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");

				else if(formObject.getNGValue("subProd").equalsIgnoreCase("National Personal Loans"))
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");

				else if(formObject.getNGValue("subProd").equalsIgnoreCase("Pre-Approved"))
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");

				else if(formObject.getNGValue("subProd").equalsIgnoreCase("Product Upgrade"))
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Product_type")){

				String ProdType=formObject.getNGValue("Product_type");
				SKLogger_CC.writeLog("CC Value Change Prod_Type",ProdType);
				formObject.clear("CardProd");
				formObject.setNGValue("CardProd","--Select--");
				if(ProdType.equalsIgnoreCase("Conventional"))
					LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' ORDER BY description");

				if(ProdType.equalsIgnoreCase("Islamic"))
					LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' ORDER BY description");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
				SKLogger_CC.writeLog("CC val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
				getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_DOJ")){
				//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
				getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_WorldCheck_WorldCheck_Grid_dob")){
				SKLogger_CC.writeLog("RLOS val change ", "Value of cmplx_WorldCheck_WorldCheck_Grid_dob is:"+formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid_dob"));
				getAgeWorldCheck(formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid_dob"));
			}
			/* if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_CIFNo")){
									new AesUtil();
									String encryp_CIF=AesUtil.Encrypt(formObject.getNGValue("cmplx_Customer_CIFNO"));
									SKLogger.writeLog("CC val change ", "Value of encryp_CIF is:"+encryp_CIF);
									formObject.setNGValue("encrypt_CIF",encryp_CIF);
								}	*/

			/* if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED")){
									SKLogger.writeLog("CC val change ", "Value of months is:"+formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));
									ChangeRepeater();
								}	
							 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_BS_ANALYSED")){
									SKLogger.writeLog("CC val change ", "Value of months is:"+formObject.getNGValue("cmplx_FinacleCore_cmplx_TurnoverNBC_BS_ANALYSED"));
									ChangeRepeaterTrnover();

								}*/	

			/* if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV") ||
										pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC")){
											//SKLogger_CC.writeLog("CC val change ", "Value of months is:"+formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB"));
											CalculateRepeater(pEvent.getSource().getName(),formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));
										}


							 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_APR1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1") ||
								pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1")){
									//SKLogger_CC.writeLog("CC val change ", "Value of months is:"+formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB"));
									CalculateRepeaterTrnover(pEvent.getSource().getName(),formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));
								}	*/
			if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Liability_New_overrideIntLiab")){
				if(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab").equalsIgnoreCase("true")){

					formObject.setVisible("Liability_New_Overwrite", true);

				}
				else{
					formObject.setVisible("Liability_New_Overwrite", false);
				}
			}
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
		SKLogger_CC.writeLog("CC val change ", "befor incomingdoc");
		IncomingDoc();
		SKLogger_CC.writeLog("CC val change ", "after incomingdoc");
		saveIndecisionGrid();


		if(formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("null") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("false") )
		{
			SKLogger_CC.writeLog("CC val change ", "Inside Y of CPV required");
			formObject.setNGValue("CPV_REQUIRED","Y");
		}
		else
		{
			SKLogger_CC.writeLog("CC val change ", "Inside N of CPV required");
			formObject.setNGValue("CPV_REQUIRED","N");
		}

		
	}


}

