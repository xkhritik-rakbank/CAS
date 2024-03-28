//----------------------------------------------------------------------------------------------------

//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOSCommonCode.java
//Author					: Deepak Kumar
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------
//--Historty--
//Deepak Code changes to calculate LPF amount and % 08-nov-2017 start
//----------------------------------------------------------------------------------------------------
package com.newgen.omniforms.user;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.io.NGFReader;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RLOSCommonCode extends RLOSCommon{

	private static final long serialVersionUID = 1L;
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED



	Date Modified                       : 6/08/2017              
	Author                              : Akshay Gupta             
	Description                         :To fetch fragments on form        

	***********************************************************************************  */


	public void call_Frame_Expanded(ComponentEvent pEvent)
	{
		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setVisible("GuarantorDetails", true);
			formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
			formObject.setNGFrameState("GuarantorDetails", 0);

		}

		else if("CustomerDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info("Inside CustomerDetails frame expand");	
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			//formObject.setNGFrameState("CustomerDetails", 0);
			//for sprint-1
			//formObject.setVisible("Customer_Frame2",false);
		}

		else if ("ProductDetailsLoader".equalsIgnoreCase(pEvent.getSource().getName())) {

			//RLOS.mLogger.info("Inside ProductDetailsLoader ");	
			//added Tanshu Aggarwal(23/06/2017)
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			adjustFrameTops("CustomerDetails,ProductDetailsLoader,Incomedetails");
		}

		else if ("EmploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{				

			//RLOS.mLogger.info( "Inside EmploymentDetails before fragment load ");	

			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);//forDOJ enable
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
			formObject.setNGValue("cmplx_EmploymentDetails_Designation",formObject.getNGValue("cmplx_Customer_Designation"));
			formObject.setLocked("cmplx_EmploymentDetails_Designation",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",false);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",false);
			if(formObject.getNGValue("cmplx_EmploymentDetails_Others")=="true"){
				formObject.setVisible("EMploymentDetails_Label72",false);
				formObject.setVisible("cmplx_EmploymentDetails_EMpCode",false);
			}
			else 
			{
				formObject.setVisible("EMploymentDetails_Label72",true);
				formObject.setVisible("cmplx_EmploymentDetails_EMpCode",true);
			}
			
			//sagarika NG_MASTER_TargetSegmentCode
		
			
			if("S".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg"))){
				formObject.setVisible("EMploymentDetails_Label59", true);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
				RLOS.mLogger.info("sssagarik@a emp-tierno");
				if("EKTMC-EXPAT".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKTMC-UAE".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKWEC-EXPAT".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKWEC-UAE".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKWEI-EXPAT".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary")))
				{
				    RLOS.mLogger.info("sssagarik@a emp");
					LoadPickList("cmplx_EmploymentDetails_tierNo", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_TierNumber with (nolock) order by code");
					formObject.setVisible("cmplx_EmploymentDetails_tierno", true);
					formObject.setVisible("EMploymentDetails_Label17", true);
					String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
					formObject.setVisible("cmplx_EmploymentDetails_tierno", true);
					LoadPickList("cmplx_EmploymentDetails_tierno", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_TierNumber with (nolock) order by code");
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CDOB') order by code");
					formObject.setVisible("EMploymentDetails_Label63", true);
					String sQuery = "select tierno from ng_rlos_empDetails with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'";

					RLOS.mLogger.info("RLOS val change Value of dob is:"+sQuery);

					List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
					RLOS.mLogger.info("RLOS val change Vasdfsdflue of dob is:"+recordList.get(0).get(0));
					if(recordList.get(0).get(0)!= null)
					{
						formObject.setNGValue("cmplx_EmploymentDetails_tierno",recordList.get(0).get(0));
					}
						}
				
					else{
						String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");	
						RLOS.mLogger.info("else sagarika yy");
						formObject.setVisible("cmplx_EmploymentDetails_tierno", false);
						formObject.setVisible("EMploymentDetails_Label17", false);
					}//
				
				if("LIFSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
				{
					RLOS.mLogger.info( "Inside EmploymentDetails after fragment load @sagarika1");
					formObject.setVisible("cmplx_EmploymentDetails_lifeInsurance", true);
					formObject.setVisible("cmplx_EmploymentDetails_lifeInsurance", true);
					formObject.setVisible("EMploymentDetails_Label10", true);
					formObject.setVisible("EMploymentDetails_Label11", true);
					formObject.setVisible("cmplx_EmploymentDetails_PremAmt", true);
					formObject.setVisible("EMploymentDetails_Label12", true);
					formObject.setVisible("cmplx_EmploymentDetails_NoPremPaid", true);
					formObject.setVisible("EMploymentDetails_Label13", true);
					formObject.setVisible("cmplx_EmploymentDetails_PremiumType", true);
					formObject.setVisible("EMploymentDetails_Label14", true);
					formObject.setVisible("cmplx_EmploymentDetails_RegPay", true);
					formObject.setVisible("EMploymentDetails_Label11", true);
					formObject.setVisible("EMploymentDetails_Label15", true);
					formObject.setVisible("cmplx_EmploymentDetails_Withinminwait", true);
					 formObject.setVisible("cmplx_EmploymentDetails_Is_life_insur_lessthan_5", true);
					 formObject.setVisible("cmplx_EmploymentDetails_Is_group_insur_policy", true);

				}
				else if("MOTOUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
				{
					//cmplx_EmploymentDetails_MotorInsurance
					formObject.setVisible("cmplx_EmploymentDetails_MotorInsurance", true);
					formObject.setVisible("EMploymentDetails_Label16", true);
					//EMploymentDetails_Label16
				}
			}
			else
			{
				formObject.setVisible("EMploymentDetails_Label59", false);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
			}
			
			
			//RLOS.mLogger.info( "Inside EmploymentDetails after fragment load ");	


			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
				formObject.setVisible("EMploymentDetails_Label71", false);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);
				formObject.setVisible("cmplx_EmploymentDetails_TL_Number", false);
				formObject.setVisible("EMploymentDetails_Label20", false);
				formObject.setVisible("EMploymentDetails_Label21", false);
				formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", false);
			}
			else{
				formObject.setVisible("EMploymentDetails_Label71", true);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
				
				if( ("false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInPL"))))
				{
					formObject.setVisible("cmplx_EmploymentDetails_TL_Number", true);
					formObject.setVisible("EMploymentDetails_Label20", true);
					formObject.setVisible("EMploymentDetails_Label21", true);
					formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", true);
					formObject.setEnabled("cmplx_EmploymentDetails_TL_Number", true);
					formObject.setEnabled("EMploymentDetails_Label20", true);
					formObject.setEnabled("EMploymentDetails_Label21", true);
					formObject.setEnabled("cmplx_EmploymentDetails_TL_Emirate", true);
				}
				else
				{
					formObject.setVisible("cmplx_EmploymentDetails_TL_Number", false);
					formObject.setVisible("EMploymentDetails_Label20", false);
					formObject.setVisible("EMploymentDetails_Label21", false);
					formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", false);
				}
			}
			//Added by shivang for PCASP-1823
			String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
			String subProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			if(emptype.equalsIgnoreCase("Salaried") && !(subProd.equalsIgnoreCase("IM"))){
				formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness",true);
			}else{
				formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness",true);
			}

		}		 

		else if ("Incomedetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
			if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)) && isCustomerMinor(formObject)){
					adjustFrameTops("ProductDetailsLoader,CustomerDetails,GuarantorDetails,Incomedetails");	
			} else{
				adjustFrameTops("ProductDetailsLoader,CustomerDetails,Incomedetails");

			}
		}

		else if ("ReferenceDetails_container".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.fetchFragment("ReferenceDetails_container", "ReferenceDetails", "q_ReferenceDetails");
		}
		//Deepak New condition added for Customer_Consent for IM cases
		else if ("Customer_Consents_container".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.fetchFragment("Customer_Consents_container", "CustomerConsent", "q_customerConsents");
		}

		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			RLOS.mLogger.info("@Shivang:: COMPANY FRAGMENT LOADED "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BPA")){
				formObject.setEnabled("bvr", false);
				formObject.setLocked("bvr", true);
			}
			//RLOS.mLogger.info( "CompanyDetailse1:");
			
		}

		//Modified by akshay for NTB/NEP case only
		else if ("DecisionHistoryContainer".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			expandDecision();
		}


		else if ("EligibilityAndProductInformation".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			fetch_EligPrd_frag();
		}	

		else if ("Liability_container".equalsIgnoreCase(pEvent.getSource().getName()))
		{                           
			fetch_Liability_frag(formObject);
		}
		else if ("CC_Loan_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
			formObject.setLocked("bankCode", true);
			formObject.setNGValue("cmplx_CC_Loan_StartMonth", "1"); //PCASP-3186
			formObject.setLocked("cmplx_CC_Loan_StartMonth", true);
		}	
		else if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName()))
		{	
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");	
			formObject.setNGValue("AddressDetails_address_Wi_name", formObject.getWFWorkitemName());
		}	
		else if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) 				
		{
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			//added by prabhakar as per FLV 6.13
			//formObject.setVisible("AltContactDetails_Label6", false);
			//formObject.setVisible("AlternateContactDetails_OfficeExt", false);
			formObject.setNGValue("AlternateContactDetails_MobileNo1",formObject.getNGValue("cmplx_Customer_MobNo"));
			formObject.setNGValue("AlternateContactDetails_carddispatch","998");
			AirArabiaValid();//sagarika
			enrollrewardValid();//saurabh1 for enroll
	
			try{
				if(null!=formObject.getNGValue("AlternateContactDetails_eStatementFlag") && "".equals(formObject.getNGValue("AlternateContactDetails_eStatementFlag")))
				{//change by saurabh for testing point on 15/4/19.
					formObject.setNGValue("AlternateContactDetails_eStatementFlag","Yes");
				}
			}catch(Exception ex){
				RLOS.mLogger.info("Exception in setting Estatement flag as default yes:"+printException(ex));	
			}
			//Employment
		}
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {       	
			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
		}*/
		else if ("CardDetails_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("CardDetails_container", "CardDetails", "q_CardDetails"); //Arun (30/11/17) modified container name of card details	
		}
		else if ("Supplementary_Container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");
			if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("employmentType")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salaried").equalsIgnoreCase(formObject.getNGValue("employmentType"))){
				formObject.setVisible("CompEmbName", false);
				formObject.setVisible("SupplementCardDetails_Label7", false);
				 SupplementaryenrollrewardValid();	
			}
			/*PCASP-592
			 * formObject.setNGValue("ResdCountry","AE");
			formObject.setLocked("ResdCountry",true);*/
			
		}
		
		else if ("FATCA_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("FATCA_container", "FATCA", "q_FATCA");
		}	
		else if ("KYC_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("KYC_container", "KYC", "q_KYC");
			loadPicklist_Kyc();
		}	
		else if ("OECD_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("OECD_container", "OECD", "q_OECD");
		}	

		else if ("IncomingDocuments".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				//RLOS.mLogger.info("Incomedetails");
			}
			else {
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				//RLOS.mLogger.info("fetched income details");
				//formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				visibilityFrameIncomeDetails(formObject);
			}
			//added by akshay on 20/12/17
			int framestate2=formObject.getNGFrameState("Liability_container");
			if(framestate2 == 0){
				//RLOS.mLogger.info("Liability");
			}
			else {
				fetch_Liability_frag(formObject);

			}
			//RLOS.mLogger.info("IncomingDocuments");
			//formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
			//change by saurabh for new incoming docs.
			formObject.fetchFragment("IncomingDocuments","IncomingDocNew","q_incomingDocNew");
			//RLOS.mLogger.info("fetchIncomingDocRepeater");
			//fetchIncomingDocRepeater();
			//for fetching new Incoming doc fragment. By Saurabh
			fetchIncomingDocRepeaterNew();
			//RLOS.mLogger.info("formObject.fetchFragment1");
		}
		//added by yash on 23/8/2017
		if ("Notepad_Values".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Values","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' as description union select  description from ng_master_notedescription  with(nolock) order by description");
			formObject.setNGValue("NotepadDetails_notedesc", "--Select--");
		}
		//ended by yash on 23/8/2017
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Saurabh Gupta             
	Description                         :To load income Details        

	 ***********************************************************************************  */

	private void setCardProdDropdown() {
		LoadPickList("SupplementCardDetails_CardProduct", "SELECT '--Select--' union SELECT DISTINCT card_product FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name ='"+formObject.getWFWorkitemName()+"' AND Cardproductselect='true'");
	}


	public static void visibilityFrameIncomeDetails(FormReference formObject) {

		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct")) /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
			//SKLogger.writeLog("RLOS @AKSHAY", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
			formObject.setVisible("IncomeDetails_Label12", true);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);	
			formObject.setVisible("IncomeDetails_Label14", true);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", true);	
		}	
		/*else{
			formObject.setVisible("IncomeDetails_Label12", false);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);	
			formObject.setVisible("IncomeDetails_Label14", false);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", false);			
		}*/
		//}

		//if(n>0)
		//{					
		//String EmpType=formObject.getNGValue("empType");
		String EmpType=formObject.getNGValue("EmploymentType");
		//RLOS.mLogger.info("RLOS Emp Type Value is:"+EmpType);
		String appType=formObject.getNGValue("Application_Type");
		String reqProd = formObject.getNGValue("PrimaryProduct");

		
		if("Salaried Pensioner".contains(EmpType))
		{
			if(!"RSEL".equals(appType) && !"RELT".equals(appType) && !"RELTN".equals(appType))
			{
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 730);
			formObject.setHeight("IncomeDetails_Frame1", 680);
			
			int prod_row_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prod_row_count>0){
				//Done By aman for Drop5 Point
				//RLOS.mLogger.info("RLOS Drop5 Type Value is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2));
				//RLOS.mLogger.info("RLOS Drop5 Type Value is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4));
				if( (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2).equalsIgnoreCase("PA")) && (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).equalsIgnoreCase("RELTN")||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).equalsIgnoreCase("RSEL")||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).equalsIgnoreCase("RELT")) )
				{
					//RLOS.mLogger.info("RLOS Drop5 Type Value is isnisde:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2));

					formObject.setVisible("IncomeDetails_Frame3", true);
					formObject.setHeight("Incomedetails",1000);
					formObject.setHeight("IncomeDetails_Frame1",1700);
				}
			}
			//commented for proc 12230
			/*if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomeDetails_Label11", false);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", false);

				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", false);
				formObject.setVisible("IncomeDetails_Label3", false);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", false);
		//COMMENTED BY AMAN FOR PROC6508	
				}	
			else{
				formObject.setVisible("IncomeDetails_Label11", true);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", true);
				formObject.setVisible("IncomeDetails_Label13", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", true);
				formObject.setVisible("IncomeDetails_Label3", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", true);
			}*/
		  }	
		}

		else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(EmpType))
		{							
			formObject.setVisible("IncomeDetails_Frame2", false);
			formObject.setTop("IncomeDetails_Frame3",40);
			formObject.setHeight("Incomedetails", 430);
			formObject.setHeight("IncomeDetails_Frame1", 400);
			
			/*if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomeDetails_Label20", false);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", false);
				formObject.setVisible("IncomeDetails_Label22", false);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", false);
				formObject.setVisible("IncomeDetails_Label35", false);
				formObject.setVisible("IncomeDetails_Label5", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", true);
				formObject.setVisible("IncomeDetails_Label36", false);
			}	
			else{
				formObject.setVisible("IncomeDetails_Label20", true);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", true);
				formObject.setVisible("IncomeDetails_Label22", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", true);
				formObject.setVisible("IncomeDetails_Label35", true);
				formObject.setVisible("IncomeDetails_Label5", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", true);
				formObject.setVisible("IncomeDetails_Label36", true);
			}	*/
		}
		//code added by bandana for PL form distorted
		else if(("Salaried").equalsIgnoreCase(EmpType) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProd))
		{							
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setTop("IncomeDetails_Frame2",40);
			formObject.setHeight("Incomedetails", 430);
			formObject.setHeight("IncomeDetails_Frame1", 400);
			
		}
		//code added by bandana for PL ends
			formObject.setVisible("IncomeDetails_Label14", true);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", true);
		
	
		
		
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProd)){
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);
			formObject.setVisible("IncomeDetails_Label12",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType", false);//hidden by Imran for iteartion 6
			formObject.setVisible("EMploymentDetails_Label25", false);//hidden by Imran for iteartion 6
		}
		else{
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);
			formObject.setVisible("IncomeDetails_Label12",true);

		}

	}

	/*Function Header:

	 **********************************************************************************

         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


Date Modified                       : 6/08/2017              
Author                              : Akshay Gupta             
Description                         :To handle mouse click events        

	 ***********************************************************************************  */


	public void call_Mouse_Clicked(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS_IntegrationInput GenXml=new RLOS_IntegrationInput();
		Common_Utils common=new Common_Utils(RLOS.mLogger);
		String outputResponse = "";
		String ReturnCode="" ;
		String alert_msg="";
		String OTPStatus;
		String SystemErrorCode;
		String BlacklistFlag;
		String DuplicationFlag;



		//added by akshay on 3/1/17
		if("cmplx_Product_cmplx_ProductGrid".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			//RLOS.mLogger.info( "Index of Selected row is: "+formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"));
			if(formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid")>-1){//Grid row is selected
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),1).equalsIgnoreCase("Personal Loan")){
					formObject.setVisible("Product_Label3",true);
					formObject.setVisible("Product_Label5",true);
					formObject.setVisible("ReqTenor",true);				
					formObject.setVisible("Scheme",true);
					formObject.setVisible("CardProd",false);
					formObject.setVisible("Product_Label6",false);
					formObject.setLeft("Product_Label5",822);
					formObject.setLeft("ReqTenor",822);

				}	

				//below code added by Arun (06/12/17) for user cannot modify the added data in the product grid
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("IM")){
					formObject.setVisible("Product_Label5",true);
					formObject.setVisible("ReqTenor",true);
					formObject.setVisible("Product_Label6",true);
					formObject.setVisible("CardProd",true);
					formObject.setLeft("Product_Label5",822);
					formObject.setLeft("ReqTenor",822);
				}

				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("SAL")){
					formObject.setVisible("Product_Label6",true);
					formObject.setVisible("CardProd",true);

				}

				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("BPA")){
					formObject.setVisible("Product_Label6",true);
					formObject.setVisible("CardProd",true);

				}

				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("PULI")){
					formObject.setVisible("Product_Label6",true);//Arun (06/12/17) to show this field
					formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
				}

				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("SE")){
					formObject.setVisible("Product_Label6",true);//Arun 18/12/17
					formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("BTC")){
					formObject.setVisible("Product_Label6",true);//Arun 18/12/17
					formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),4).equalsIgnoreCase("SMES"))
					{
						formObject.setVisible("Product_Label8",true);
						formObject.setVisible("FDAmount",true);
					}
				}

				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("SEC")){
					formObject.setVisible("Product_Label8",true);
					formObject.setVisible("FDAmount",true);
					formObject.setVisible("Product_Label6",true);//Arun 18/12/17
					formObject.setVisible("CardProd",true);//Arun 18/12/17

				}

				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("LI")){
					formObject.setVisible("Product_Label8",false);
					formObject.setVisible("FDAmount",false);
					formObject.setVisible("Product_Label6",false);//Arun 18/12/17
					formObject.setVisible("CardProd",false);//Arun 18/12/17

				}
				/* else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("PULI")){
					formObject.setVisible("Product_Label8",false);
					formObject.setVisible("FDAmount",false);
				} */ //commented because same else if present on line number 443 so flow of code not reaching to this Else If

				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("PU")){
					formObject.setVisible("Product_Label6",false);//Arun (06/12/17) to hide this field
					formObject.setVisible("CardProd",false);//Arun (06/12/17) to hide this field
					//formObject.setVisible("Product_Label16",false);//Arun (06/12/17) to hide this field
					//formObject.setVisible("LimitAcc",false);//Arun (06/12/17) to hide this field
					formObject.setVisible("CardDetails_container",false);//Arun (06/12/17) to hide this fragment
				}
			}	
			else{//Grid row is un selected
				formObject.setVisible("Product_Label8",false);
				formObject.setVisible("FDAmount",false);
				formObject.setVisible("Product_Label3",false);
				formObject.setVisible("Scheme",false);
				formObject.setVisible("Product_Label5",false);
				formObject.setVisible("ReqTenor",false);
				formObject.setVisible("Product_Label6",false);	
				formObject.setVisible("CardProd", false);
				formObject.setNGValue("ReqProd","--Select--",false);
				formObject.setNGValue("Product_type","Conventional",false);
				formObject.setNGValue("Priority","Primary",false);
				formObject.setNGValue("EmpType","");
				formObject.setNGValue("AppType","");

			}		
		}
		//ended by akshay on 3/1/17

		//added by akshay on 18/1/18 to not allow to modify primary row  in company
		else if("cmplx_CompanyDetails_cmplx_CompanyGrid".equalsIgnoreCase(pEvent.getSource().getName())){

			if(formObject.getSelectedIndex("cmplx_CompanyDetails_cmplx_CompanyGrid")==0){
				formObject.setLocked("CompanyDetails_Frame1",true);
				formObject.setLocked("CompanyDetails_Frame3",true);//added by akshay on 24/3/18
				formObject.setLocked("CompanyDetails_Modify",false);
				formObject.setLocked("CompanyDetails_Add",false);
			}
			else{
				formObject.setLocked("CompanyDetails_Frame1",false);
				formObject.setLocked("CompanyDetails_Frame3",false);//added by akshay on 24/3/18
				formObject.setLocked("CompanyDetails_Modify",false);
				formObject.setLocked("CompanyDetails_Add",false);
				formObject.setLocked("NepType",true);
				formObject.setEnabled("CompanyDetails_Button3",false);
				formObject.setLocked("cif",true);	
				formObject.setLocked("EffectiveLOB", true);
				formObject.setLocked("TLIssueDate", false);
				formObject.setLocked("TLExpiry", false);
				formObject.setEnabled("TLIssueDate", true);
				formObject.setEnabled("TLExpiry", true);
				formObject.setEnabled("estbDate", true);
				formObject.setLocked("estbDate", false);
				//Deepak Changes done for BusinessElite -- July 2022
				formObject.setLocked("CompanyDetails_BusinessElite",true);
				formObject.setEnabled("CompanyDetails_BusinessElite", false);

			}	
		}
		//ended by akshay

		//below code Hritik JS -> JAVA migration for risk mgmnt: 16/03/2022

		else if("cmplx_OECD_cmplx_GR_OecdDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info("below code Hritik JS -> JAVA migration: 16/03/2022 Inside click event");

			int selectedRow = formObject.getSelectedIndex("cmplx_OECD_cmplx_GR_OecdDetails");
			RLOS.mLogger.info("selectedRow : "+selectedRow);
			if(selectedRow != -1){
				if(formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails",selectedRow,5)!=null && formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails",selectedRow,5)!="")
				{
					RLOS.mLogger.info("Inside condition");
					formObject.setEnabled("OECD_noTinReason",false);
				}
			}
		}

		else if("cmplx_Customer_NTB".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info("cmplx_Customer_NTB : "+formObject.getNGValue("cmplx_Customer_NTB"));
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true"))
			{
				SetEnableCustomer(formObject);
			}
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
			{
				SetdisableCustomer(formObject);
			}
		}

		else if("cmplx_Customer_CardNotAvailable".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info("cmplx_Customer_CardNotAvailable : ");
			if(formObject.getNGValue("cmplx_Customer_CardNotAvailable").equalsIgnoreCase("true"))
			{
				RLOS.mLogger.info("cmplx_Customer_CardNotAvailable : "+formObject.getNGValue("cmplx_Customer_CardNotAvailable"));
				formObject.setEnabled("ReadFromCard",false);
				formObject.setEnabled("cmplx_Customer_DOb",true);
				formObject.setEnabled("cmplx_Customer_card_id_available",false);
				formObject.setEnabled("cmplx_Customer_NEP",false);
				formObject.setEnabled("cmplx_Customer_VIsaExpiry",true);
			}
			else
			{
				SetdisableCustomer(formObject);
				formObject.setEnabled("ReadFromCard",true);
				formObject.setEnabled("FetchDetails",false);
				formObject.setEnabled("cmplx_Customer_NEP",true);
				formObject.setEnabled("cmplx_Customer_SecNAtionApplicable",false);
				formObject.setEnabled("cmplx_Customer_card_id_available",false);
			}
		}

		else if("cmplx_Customer_card_id_available".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getNGValue("cmplx_Customer_card_id_available").equalsIgnoreCase("true"))
			{
				RLOS.mLogger.info("cmplx_Customer_card_id_available :"+formObject.getNGValue("cmplx_Customer_card_id_available"));
				formObject.setEnabled("cmplx_Customer_CIFNO",true);
				formObject.setEnabled("ReadFromCard",false);
				formObject.setEnabled("cmplx_Customer_CardNotAvailable",false);
				formObject.setEnabled("FetchDetails",true);
			}
			else
			{
				formObject.setEnabled("ReadFromCard",true);
				formObject.setEnabled("cmplx_Customer_CardNotAvailable",true);
				formObject.setEnabled("cmplx_Customer_NEP",true);
				formObject.setEnabled("FetchDetails",false);
			}
		}
		/*else if("FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info("FetchDetails:");
			formObject.setEnabled("cmplx_Customer_VIsaExpiry",true);
		}*/

		else if("cmplx_GuarantorDetails_cmplx_GuarantorGrid".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getNGValue("GuarantorDetails_nationality").equalsIgnoreCase("AE"))
			{	
				RLOS.mLogger.info("GuarantorDetails_nationality :"+formObject.getNGValue("GuarantorDetails_nationality"));
				formObject.setEnabled("GuarantorDetails_VisaISsueDate",false);
				formObject.setEnabled("visaExpiry",false);
			}
			else 
			{
				formObject.setEnabled("GuarantorDetails_VisaISsueDate",true);
				formObject.setEnabled("visaExpiry",true);
			}
		}

		/*else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info("Customer_Button1 :");
			formObject.setEnabled("Customer_Button1",false);
		}*/

		/*else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getNGValue("cmplx_EmploymentDetails_Others").equalsIgnoreCase("true"))
			{
				RLOS.mLogger.info("cmplx_EmploymentDetails_Others :"+formObject.getNGValue("cmplx_EmploymentDetails_Others"));
				formObject.setEnabled("EMploymentDetails_Button2",false);
				formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",true);
			}
			else 
			{
				formObject.setEnabled("cmplx_EmploymentDetails_IncInPL",false);
				formObject.setEnabled("cmplx_EmploymentDetails_Freezone",true);
				formObject.setEnabled("cmplx_EmploymentDetails_IncInCC",false);
				formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",true);
			}
		} */


		else if("Liability_New_Frame4".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			int frame_Liability_New_Frame4 = formObject.getNGFrameState("Liability_New_Frame4");
			RLOS.mLogger.info("frame_Liability_New_Frame4: "+frame_Liability_New_Frame4);
			if(frame_Liability_New_Frame4 == 0)
			{
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4).indexOf("TKO")>-1)
				{
					RLOS.mLogger.info(" Inside : cmplx_Product_cmplx_ProductGrid,0,4");
					formObject.setEnabled("takeOverIndicator",true);
				}
				else
				{
					formObject.setEnabled("takeOverIndicator",false);	
				}
			}
		}

		/*else if("Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info(" Add: click event : ReqProd : "+formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"));
			if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"))
			{
				RLOS.mLogger.info(" Subproduct_productGrid : "+formObject.getNGValue("Subproduct_productGrid"));
				if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
				{
					RLOS.mLogger.info("Inside Subproduct_productGrid");
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", true); 
				}
				if(!formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
				{
					RLOS.mLogger.info("Inside Subproduct_productGrid");
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", false); 
				}
			}
		}*/
		/*else if("Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info(" Modify: click event : ReqProd : "+formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"));
			if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"))
			{
				RLOS.mLogger.info(" Subproduct_productGrid : "+formObject.getNGValue("Subproduct_productGrid"));
				if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
				{
					RLOS.mLogger.info("Inside Subproduct_productGrid");
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", true); 
				}
				if(!formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
				{
					RLOS.mLogger.info("Inside Subproduct_productGrid");
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", false); 
				}
			}
		}
		 */

		// end hritik js->java

		else if("ReferenceDetails_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("reference_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if("ReferenceDetails_Reference__modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if("ReferenceDetails_Reference_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if("GuarantorDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("guarantor_WIName",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("guarantor_WIName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
			AddFromHiddenList("GuarantorDetails_AddressList","cmplx_AddressDetails_cmplx_AddressGrid");
			AddFromHiddenList("GuarantorDetails_FatcaList","cmplx_FATCA_cmplx_GR_FatcaDetails");
			AddFromHiddenList("GuarantorDetails_KYCList","cmplx_KYC_cmplx_KYCGrid");
			AddFromHiddenList("GuarantorDetails_OecdList","cmplx_OECD_cmplx_GR_OecdDetails");
			loadDynamicPickList();
			Custom_fragmentSave("GuarantorDetails");




		}
		else  if("GuarantorDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
			if(formObject.isLocked("ReadFromCIF")==false){
				AddFromHiddenList("GuarantorDetails_AddressList","cmplx_AddressDetails_cmplx_AddressGrid");
				AddFromHiddenList("GuarantorDetails_FatcaList","cmplx_FATCA_cmplx_GR_FatcaDetails");
				AddFromHiddenList("GuarantorDetails_KYCList","cmplx_KYC_cmplx_KYCGrid");
				AddFromHiddenList("GuarantorDetails_OecdList","cmplx_OECD_cmplx_GR_OecdDetails");
			}
			loadDynamicPickList();
			Custom_fragmentSave("GuarantorDetails");



		}
		else  if("GuarantorDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
			loadDynamicPickList();
			setDataInMultipleAppGrid();
			/*clearSelectiveRows("Guarantor_CIF","cmplx_AddressDetails_cmplx_AddressGrid");
			clearSelectiveRows("Guarantor_CIF","cmplx_FATCA_cmplx_GR_FatcaDetails");
			clearSelectiveRows("Guarantor_CIF","cmplx_KYC_cmplx_KYCGrid");
			clearSelectiveRows("Guarantor_CIF","cmplx_OECD_cmplx_GR_OecdDetails");*/
		}

		else if("IncomeDetails_FinacialSummarySelf".equalsIgnoreCase(pEvent.getSource().getName())){

			if("Self Employed".equals(formObject.getNGValue("EmploymentType")) || "PA".equals(formObject.getNGValue("Subproduct_productGrid"))){
				try{
					List<String> objInput = new ArrayList();
					List<Object> objOutput = new ArrayList();
					RLOS.mLogger.info("IncomeDetails_FinacialSummarySelf button");

					objInput.add("Text:" + formObject.getWFWorkitemName());
					objOutput.add("Text");
					objOutput = formObject.getDataFromStoredProcedure("getFinancialSummaryData", objInput, objOutput);
					//RLOS.mLogger.info("getFinancialSummaryData Output: "+objOutput.toString());
					String data = (String)objOutput.get(0);

					if(!"".equals(data)){
						String data_array[]=data.split(",");


						if(data_array[0]!=null && data_array[0]!=""){
							//RLOS.mLogger.info("before setting avg bal"+(data_array[0]));
							formObject.setNGValue("cmplx_IncomeDetails_AvgBal",data_array[0]);
							//CreditCard.mLogger.info("value after set: "+formObject.getNGValue("cmplx_IncomeDetails_AvgBal"));
						}
						if(1<data_array.length && data_array[1]!=null){
							//RLOS.mLogger.info("before setting tot turnover"+(data_array[1]));
							formObject.setNGValue("cmplx_IncomeDetails_CredTurnover",(data_array[1]));
						}
						if(2<data_array.length && data_array[2]!=null){
							//RLOS.mLogger.info("before setting avg credturnover"+(data_array[2]));
							formObject.setNGValue("cmplx_IncomeDetails_AvgCredTurnover", (data_array[2]));
						}
					}




					//fetchFinacleCoreFrag();
				}catch(Exception ex){
					RLOS.mLogger.info( "Exception in setting finan summ data: "+printException(ex));
				}
			}
		}

		else if ("Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			try{
				// hritik 31.3.22 JS->JAVA
				RLOS.mLogger.info(" Add: click event : ReqProd : "+formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"));
				if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"))
				{
					RLOS.mLogger.info(" Subproduct_productGrid : "+formObject.getNGValue("Subproduct_productGrid"));
					if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
					{
						RLOS.mLogger.info("Inside Subproduct_productGrid");
						formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", true); 
					}
					if(!formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
					{
						RLOS.mLogger.info("Inside Subproduct_productGrid");
						formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", false); 
					}
				}
				//below code by saurabh on 21st Nov 18 for validating user with case from source code master.
				String userid = formObject.getNGValue("lbl_user_name_val");
				//RLOS.mLogger.info( "user id is:"+userid);
				RLOS.mLogger.info( "alert_msg is:"+alert_msg);
				if(null==userid || "".equals(userid)){
					alert_msg = "Cannot initiate case as User Id is not valid";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					String validUserQuery = "select Product_type from NG_MASTER_SourceCode where UserId='"+userid+"'";
					List<List<String>> result = formObject.getNGDataFromDataCache(validUserQuery);
					RLOS.mLogger.info( "result from DB is:"+result);
					if(null!=result && result.size()>0 && null!= result.get(0) && null!=result.get(0).get(0) && !"".equalsIgnoreCase(result.get(0).get(0))){
						String allowedVal = result.get(0).get(0);
						//RLOS.mLogger.info( "allowedVal is:"+allowedVal);
						String currVal = formObject.getNGValue("ReqProd");
						RLOS.mLogger.info( "currVal is:"+currVal);
						if(("CC".equalsIgnoreCase(allowedVal)||"Credit Card".equalsIgnoreCase(allowedVal)) && (!"CC".equalsIgnoreCase(currVal) && !"Credit Card".equalsIgnoreCase(currVal))){
							alert_msg = "User "+userid+" not allowed to initiate "+currVal;
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else if(("PL".equalsIgnoreCase(allowedVal)||"Personal Loan".equalsIgnoreCase(allowedVal)) && (!"PL".equalsIgnoreCase(currVal) && !"Personal Loan".equalsIgnoreCase(currVal))){
							alert_msg = "User "+userid+" not allowed to initiate "+currVal;
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
					}
					else{
						alert_msg = "User or associated Product not found";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}

				}
				//RLOS.mLogger.info( "Inside add button:"+formObject.getNGValue("ReqProd"));
				formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
				//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");
				formObject.setNGValue("SOLID", getDataFromSourceCodeMaster("SOL_ID"));
				formObject.setNGValue("q_SOLID", getDataFromSourceCodeMaster("SOL_ID"));

				AddProducts();
				AddPrimaryData();
				ParentToChild();
				Aecb_checks(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2),formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6),formObject);
				//tanshu aggarwal for documents(1/06/2017)
				//changes for Incoming Doc by saurbh on 7th Jan
				boolean Visibility=formObject.isVisible("IncomingDocNew_Frame1");
				//RLOS.mLogger.info("before clear"+Visibility);
				if(Visibility == true){

					fetchIncomingDocRepeaterNew();
					//RLOS.mLogger.info("after doc fun");
				}

				/*else {
				String query = "Delete FROM ng_rlos_gr_incomingDocument WHERE  IncomingDocGR_Winame='"+formObject.getWFWorkitemName()+"'";
				//RLOS.mLogger.info( "when row count is  zero inside else"+query);
				//RLOS.mLogger.info( "when row count is  zero inside else after deleting the values");

			}*/
				//RLOS.mLogger.info("after refresh");
				//tanshu aggarwal for documents(1/06/2017)
				//added by akshay on 28/5/18 for proc 9582

				formObject.setNGValue("EmploymentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
				formObject.setNGValue("Product_type",NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional"),false);
				formObject.setNGValue("ReqProd","--Select--",false);
				formObject.setNGValue("AppType","",false);
				formObject.setNGValue("EmpType","",false);
				formObject.setNGValue("Priority","Primary",false);
				formObject.setVisible("Product_Label5",false);
				formObject.setVisible("Product_Label3",false);
				formObject.setVisible("Product_Label6",false);
				formObject.setVisible("Product_Label10",false);
				formObject.setVisible("Product_Label12",false);
				formObject.setVisible("CardProd",false);
				formObject.setVisible("Scheme",false);
				formObject.setVisible("ReqTenor",false);
				formObject.setVisible("Product_Label15",false); 
				formObject.setVisible("Product_Label17",false); 

				formObject.setVisible("Product_Label18",false);
				formObject.setVisible("Product_Label21",false); 
				formObject.setVisible("Product_Label22",false); 
				formObject.setVisible("Product_Label23",false); 
				formObject.setVisible("Product_Label24",false);	
				formObject.setVisible("Product_Label16",false); 
				//formObject.setVisible("LimitAcc",false); 		//changed by akshay on 3/1/17....++below lines commented too							
				formObject.setVisible("Product_Label10", false);
				formObject.setVisible("Product_Label11", false);
				formObject.setVisible("Product_Label9", false);
				formObject.setVisible("Product_Label7", false);
				formObject.setVisible("LastPermanentLimit", false);
				formObject.setVisible("LastTemporaryLimit", false);
				//formObject.setVisible("ExistingTempLimit", false);
				//formObject.setVisible("Limit_Expdate", false);
				formObject.setVisible("Product_Label8", false);
				//formObject.setVisible("typeReq", false);
				formObject.setVisible("FDAmount", false);
				formObject.setVisible("NoOfMonths", false);

				formObject.setVisible("Product_Label15", false);


				formObject.setNGFrameState("Incomedetails", 1);
				//formObject.setNGFrameState("EmploymentDetails", 1);
				formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				//commented by Deepak for QA automation testing 06 march 2018
				//	formObject.setNGFrameState("Alt_Contact_container", 1);
				formObject.setNGFrameState("CC_Loan_container", 1);
				formObject.setNGFrameState("CompanyDetails", 1);
				//commented by Deepak for QA automation testing 06 march 2018
				//formObject.setNGFrameState("CardDetails_container", 1);
				formObject.setNGFrameState("Liability_container", 1);
				Fields_ApplicationType_Employment();
				loadPicklist_TargetSegmentCode();
				if(formObject.isVisible("AltContactDetails_Frame1")){
					String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
					LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
				}
				//commented by saurabh on 21st May as after modify liability button gets disabled which should not be the case.
				/*if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2))){
				if(formObject.isVisible("Liability_New_Frame1")){
					formObject.setLocked("Liability_New_fetchLiabilities", true);
				}
			}IncomingDocNew_DocType*/
				FieldsVisibility_CardDetails(formObject);//added by akshay
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
				{
					//RLOS.mLogger.info( "before clearing EmploymentDetails-->"+formObject.getNGValue("EmploymentType"));
					//formObject.clear("EmploymentDetails");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpName", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EMpCode", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "");
					formObject.setNGValue("cmplx_EmploymentDetails_Others",false);
					formObject.setNGValue("cmplx_EmploymentDetails_Emp_Type", "");
					formObject.setNGValue("cmplx_EmploymentDetails_CurrEmployer", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpIndusSector", "");
					formObject.setNGValue("cmplx_EmploymentDetails_Indus_Macro", "");
					formObject.setNGValue("cmplx_EmploymentDetails_Indus_Micro", "");
					formObject.setNGValue("cmplx_EmploymentDetails_DOJ", "");
					formObject.setNGValue("cmplx_EmploymentDetails_LOS", "");
					formObject.setNGValue("cmplx_EmploymentDetails_FreezoneName", "");
					formObject.setNGValue("cmplx_EmploymentDetails_IncInCC",false);
					formObject.setNGValue("cmplx_EmploymentDetails_IncInPL",false);
					formObject.setLocked("Eligibility_Check", true);
				}
				else{
					formObject.setLocked("Eligibility_Check", false);
					//RLOS.mLogger.info( "before clearing CompanyDetails-->"+formObject.getNGValue("EmploymentType"));
					formObject.clear("CompanyDetails");
				}
				Checkcustomersectionfields(formObject);
			}
			catch(Exception e){RLOS.mLogger.info(printException(e));

			if(!"".equals(alert_msg)){
				RLOS.mLogger.info("value of alert_msg is :"+alert_msg);
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			}
			//below code by nikhil for PCSP-706 
			finally
			{
				formObject.RaiseEvent("WFSave");
			}
		}	




		else if ("Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				// hritik 31.3.22  JS-> JAVA
				RLOS.mLogger.info(" Modify: click event : ReqProd : "+formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"));
				if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card"))
				{
					RLOS.mLogger.info(" Subproduct_productGrid : "+formObject.getNGValue("Subproduct_productGrid"));
					if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
					{
						RLOS.mLogger.info("Inside Subproduct_productGrid");
						formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", true); 
					}
					if(!formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC"))
					{
						RLOS.mLogger.info("Inside Subproduct_productGrid");
						formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", false); 
					}
				}


				//RLOS.mLogger.info( "Inside add button: aman123"+formObject.getNGValue("ReqProd"));

				//below code by saurabh on 21st Nov 18 for validating user with case from source code master.
				String userid = formObject.getNGValue("lbl_user_name_val");
				RLOS.mLogger.info( "user id is:"+userid);
				RLOS.mLogger.info( "alert_msg is:"+alert_msg);
				if(null==userid || "".equals(userid)){
					alert_msg = "Cannot initiate case as User Id is not valid";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					String validUserQuery = "select Product_type from NG_MASTER_SourceCode where UserId='"+userid+"'";
					List<List<String>> result = formObject.getNGDataFromDataCache(validUserQuery);
					RLOS.mLogger.info( "result from DB is:"+result);
					if(null!=result && result.size()>0 && null!= result.get(0) && null!=result.get(0).get(0) && !"".equalsIgnoreCase(result.get(0).get(0))){
						String allowedVal = result.get(0).get(0);
						RLOS.mLogger.info( "allowedVal is:"+allowedVal);
						String currVal = formObject.getNGValue("ReqProd");
						RLOS.mLogger.info( "currVal is:"+currVal);
						if(("CC".equalsIgnoreCase(allowedVal)||"Credit Card".equalsIgnoreCase(allowedVal)) && (!"CC".equalsIgnoreCase(currVal) && !"Credit Card".equalsIgnoreCase(currVal))){
							alert_msg = "User "+userid+" not allowed to initiate "+currVal;
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else if(("PL".equalsIgnoreCase(allowedVal)||"Personal Loan".equalsIgnoreCase(allowedVal)) && (!"PL".equalsIgnoreCase(currVal) && !"Personal Loan".equalsIgnoreCase(currVal))){
							alert_msg = "User "+userid+" not allowed to initiate "+currVal;
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
					}
					else{
						alert_msg = "User or associated Product not found";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}

				}

				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("ReqProd"))){
					formObject.setNGValue("Scheme","");
				}
				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("ReqProd"))){
					formObject.setNGValue("CardProd","");
				}

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
				formObject.setNGValue("SOLID", getDataFromSourceCodeMaster("SOL_ID"));
				formObject.setNGValue("q_SOLID", getDataFromSourceCodeMaster("SOL_ID"));

				AddProducts();
				AddPrimaryData();
				ParentToChild();

				//tanshu aggarwal for documents(1/06/2017)
				//RLOS.mLogger.info("after creating repeater object00");
				/*IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
			//RLOS.mLogger.info("before clear"+Visibility);
			if(Visibility == true){
				repObj.clear();
				repObj.refresh();
				fetchIncomingDocRepeater();
				//RLOS.mLogger.info("after doc fun");
				//formObject.setNGFrameState("IncomingDocuments", 1);


			}

			else {
				String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
				//RLOS.mLogger.info( "when row count is  zero inside else"+query);
				formObject.getNGDataFromDataCache(query);
				//RLOS.mLogger.info( "when row count is  zero inside else after deleting the values123");


			}*/

				//RLOS.mLogger.info("after creating repeater object00");
				//tanshu aggarwal for documents(1/06/2017)
				//changes for incoming doc new by saurabh on 7th Jan
				boolean Visibility=formObject.isVisible("IncomingDocNew_Frame1");
				//RLOS.mLogger.info("before clear"+Visibility);
				if(Visibility == true){

					fetchIncomingDocRepeaterNew();
					//RLOS.mLogger.info("after doc fun");
				}
				Aecb_checks(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2),formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6),formObject);
				//setting insurance fields by saurabh on 11th oct.
				//setChargesFields();//Commented by Deepak new line of code written in check re-eligibility click event.
				formObject.setNGValue("EmploymentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
				//formObject.setNGFrameState("Incomedetails", 1);
				formObject.setNGValue("Product_type",NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional"),false);
				formObject.setNGValue("ReqProd","--Select--",false);
				formObject.setNGValue("AppType","",false);
				formObject.setNGValue("EmpType","",false);
				formObject.setNGValue("Priority","Primary",false);
				formObject.setVisible("Product_Label5",false);
				formObject.setVisible("Product_Label3",false);
				formObject.setVisible("Product_Label6",false);
				formObject.setVisible("Product_Label10",false);
				formObject.setVisible("Product_Label12",false);
				formObject.setVisible("CardProd",false);
				formObject.setVisible("Scheme",false);
				formObject.setVisible("ReqTenor",false);



				formObject.setVisible("Product_Label18",false);
				formObject.setVisible("Product_Label21",false); 
				formObject.setVisible("Product_Label22",false); 
				formObject.setVisible("Product_Label23",false); 
				formObject.setVisible("Product_Label24",false);

				formObject.setVisible("Product_Label16",false); 
				//formObject.setVisible("LimitAcc",false); 		//changed by akshay on 3/1/17....++below lines commented too							
				formObject.setVisible("Product_Label10", false);
				formObject.setVisible("Product_Label11", false);
				formObject.setVisible("Product_Label9", false);
				formObject.setVisible("Product_Label7", false);
				formObject.setVisible("LastPermanentLimit", false);
				formObject.setVisible("LastTemporaryLimit", false);
				//formObject.setVisible("ExistingTempLimit", false);
				//formObject.setVisible("Limit_Expdate", false);
				formObject.setVisible("Product_Label8", false);
				//formObject.setVisible("typeReq", false);
				formObject.setVisible("FDAmount", false);
				formObject.setVisible("NoOfMonths", false);


				formObject.setVisible("Product_Label15", false);
				formObject.setNGFrameState("Incomedetails", 1);
				formObject.setNGFrameState("EmploymentDetails", 1);
				formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				formObject.setNGFrameState("Alt_Contact_container", 1);
				formObject.setNGFrameState("CC_Loan_container", 1);
				formObject.setNGFrameState("CompanyDetails", 1);
				formObject.setNGFrameState("CardDetails_container", 1);
				formObject.setNGFrameState("Liability_container", 1);
				Fields_ApplicationType_Employment();
				loadPicklist_TargetSegmentCode();//added by akshay on 24/11/17

				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1))){
					//	Eligibility_UnHide();
					Eligibilityfields();
				}
				else{
					Eligibilityfields();
				}
				formObject.setNGFocus("Modify");//set focus to the same button which was clicked
				//code by saurabh on 8th nov 2017.		
				if(formObject.isVisible("Liability_New_Frame1")){
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6))){
						formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", true);
					}
					else{
						formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);
					}
				}
				//code by saurabh on 10th nov 2017.
				if(formObject.isVisible("AltContactDetails_Frame1")){
					String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
					LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
				}

				//added by akshay on 28/5/18 for proc 9582
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
				{
					//formObject.clear("EmploymentDetails");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpName", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EMpCode", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "");
					formObject.setNGValue("cmplx_EmploymentDetails_Others",false);
					formObject.setNGValue("cmplx_EmploymentDetails_Emp_Type", "");
					formObject.setNGValue("cmplx_EmploymentDetails_CurrEmployer", "");
					formObject.setNGValue("cmplx_EmploymentDetails_EmpIndusSector", "");
					formObject.setNGValue("cmplx_EmploymentDetails_Indus_Macro", "");
					formObject.setNGValue("cmplx_EmploymentDetails_Indus_Micro", "");
					formObject.setNGValue("cmplx_EmploymentDetails_DOJ", "");
					formObject.setNGValue("cmplx_EmploymentDetails_LOS", "");
					formObject.setNGValue("cmplx_EmploymentDetails_FreezoneName", "");
					formObject.setNGValue("cmplx_EmploymentDetails_IncInCC",false);
					formObject.setNGValue("cmplx_EmploymentDetails_IncInPL",false);
				}
				else{
					formObject.clear("CompanyDetails");
				}
				//commented by saurabh on 21st May as after modify liability button gets disabled which should not be the case.
				/*if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2))){
				if(formObject.isVisible("Liability_New_Frame1")){
					formObject.setLocked("Liability_New_fetchLiabilities", true);
				}
			}*/
				Checkcustomersectionfields(formObject);
			}catch(Exception e){
				//RLOS.mLogger.info("Product Modify Event--->"+"exception occurred!!!");
				printException(e);
			}
			if (!(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor") != null && formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")!=""))		
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,7));
		}

		else if ("Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

				//tanshu aggarwal for documents(1/06/2017)
				//RLOS.mLogger.info("before creating repeater object22");

				formObject.setNGValue("Product_type", "--Select--");
				//RLOS.mLogger.info("before creating repeater object22 after saving the fragment12");
				IRepeater repObj;
				repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
				boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
				//RLOS.mLogger.info("before clear"+Visibility);
				if(Visibility == true){
					repObj.clear();
					repObj.refresh();
					formObject.setNGFrameState("IncomingDocuments", 1);
					//RLOS.mLogger.info("after doc fun");
					//RLOS.mLogger.info("before clear setframestate");
				}


				else {
					String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
					//RLOS.mLogger.info( "when row count is  zero inside else"+query);
					formObject.getNGDataFromDataCache(query);
					//RLOS.mLogger.info( "when row count is  zero inside else after deleting the values456");
				}
				//RLOS.mLogger.info("after creating repeater object22");
				//tanshu aggarwal for documents(1/06/2017)

				AddProducts();
				AddPrimaryData();
				ParentToChild();
				//formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
				formObject.setNGValue("employmentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));

				formObject.setNGValue("Product_type","--Select",false);
				//formObject.setNGValue("ReqProd","--Select--",false);
				formObject.setNGValue("AppType","",false);
				//formObject.setNGValue("EmpType","",false);
				formObject.setNGValue("EmploymentType","",false);
				formObject.setNGValue("Priority","Primary",false);
				formObject.setVisible("Product_Label5",false);
				formObject.setVisible("Product_Label3",false);
				formObject.setVisible("Product_Label6",false);
				formObject.setVisible("Product_Label10",false);
				formObject.setVisible("Product_Label12",false);
				formObject.setVisible("CardProd",false);
				formObject.setVisible("Scheme",false);
				formObject.setVisible("ReqTenor",false);
				formObject.setVisible("Product_Label15",false); 
				formObject.setVisible("Product_Label17",false); 

				formObject.setVisible("Product_Label18",false);
				formObject.setVisible("Product_Label21",false); 
				formObject.setVisible("Product_Label22",false); 
				formObject.setVisible("Product_Label23",false); 
				formObject.setVisible("Product_Label24",false);
				formObject.setVisible("Product_Label18",false);
				formObject.setVisible("Product_Label21",false); 
				formObject.setVisible("Product_Label22",false); 
				formObject.setVisible("Product_Label23",false); 
				formObject.setVisible("Product_Label24",false);

				formObject.setVisible("Product_Label16",false); 
				//formObject.setVisible("LimitAcc",false); 		//changed by akshay on 3/1/17....++below lines commented too							
				formObject.setVisible("Product_Label10", false);
				formObject.setVisible("Product_Label11", false);
				formObject.setVisible("Product_Label9", false);
				formObject.setVisible("Product_Label7", false);
				formObject.setVisible("LastPermanentLimit", false);
				formObject.setVisible("LastTemporaryLimit", false);
				//formObject.setVisible("ExistingTempLimit", false);
				//formObject.setVisible("Limit_Expdate", false);
				formObject.setVisible("Product_Label8", false);
				//formObject.setVisible("typeReq", false);
				formObject.setVisible("FDAmount", false);

				formObject.setNGFrameState("Incomedetails", 1);
				formObject.setNGFrameState("EmploymentDetails", 1);
				formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				formObject.setNGFrameState("Alt_Contact_container", 1);
				formObject.setNGFrameState("CC_Loan_container", 1);
				formObject.setNGFrameState("CompanyDetails", 1);
				formObject.setNGFrameState("CardDetails_container", 1);
			}

			catch(Exception e){RLOS.mLogger.info(printException(e));}
		}
		else if("IncomeDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))	
		{
			formObject.setNGValue("IncomeDetails_wi_name",formObject.getWFWorkitemName());
			//RLOS.mLogger.info("RLO Inside add button: "+formObject.getNGValue("IncomeDetails_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
		else 	if("IncomeDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
		else if("IncomeDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
		else if("Liability_New_fetchLiabilities".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getNGValue("cmplx_Customer_NTB").equals("false")){
				List<String> objInput=new ArrayList<String>();
				List<Object> objOutput=new ArrayList<Object>();
				objInput.add("Text:" + formObject.getWFWorkitemName());

				int Prod_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				String SubProd="";
				String AppType="";
				if(Prod_rowCount>0){
					SubProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
					AppType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
				}	
				if((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPN").equalsIgnoreCase(AppType))||(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPE").equalsIgnoreCase(AppType))||
						(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_LimitIncrease_code").equalsIgnoreCase(SubProd))||
						(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM").equalsIgnoreCase(SubProd)&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM_Additional").equalsIgnoreCase(AppType)||NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM_TopUp").equalsIgnoreCase(AppType)))){
					RLOS.mLogger.info("RLOS Comman: Product dedupe will not run as SubProd: "+SubProd+" AppType: "+AppType);
				}
				else{

					objOutput.add("Text");
					//RLOS.mLogger.info("objInput args is: " + formObject.getWFWorkitemName());

					objOutput = formObject.getDataFromStoredProcedure("ng_RLOS_DedupeCheck", objInput, objOutput);
					//	RLOS.mLogger.info("objOutput args is: " + (String) objOutput.get(0));
					if(!"IM".equalsIgnoreCase(SubProd))
					{
						formObject.setNGValue("dedupecheck", (String) objOutput.get(0));
					}
					if (formObject.getNGValue("dedupecheck").equalsIgnoreCase("Y")){
						throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL057")));
					}
				}
				//RLOS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
				List<Object> objOutput1=new ArrayList<Object>();

				objOutput1.add("Text");

				objOutput1= formObject.getDataFromStoredProcedure("ng_RLOS_CheckWriteOff", objInput,objOutput);
			}

		}
		else if("CompanyDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("company_winame"));
			int row_count=formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
			int columns=0;
			/*UIComponent pComp =formObject.getComponent("cmplx_CompanyDetails_cmplx_CompanyGrid");

			if( pComp != null && pComp instanceof ListView )
			{			
				ListView objListView = ( ListView )pComp;
				columns  = objListView.getChildCount();
			}*/
			//RLOS.mLogger.info( "company add button: row count : "+row_count);
			//RLOS.mLogger.info( "company add button: cloumn count : "+columns);
			if(row_count>=2)
			{
				/*List<String> company_row=new ArrayList<String>();
				List<List<String>> company_data=new ArrayList<List<String>>();
				for(int i=0;i<row_count;i++)
				{
					for(int j=0;j<columns;j++)
					{
						company_row.add(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, j));
					}
					RLOS.mLogger.info( "company add button: list1  : "+company_row);
					company_data.add(company_row);
					company_row.clear();
					RLOS.mLogger.info( "company add button: list1  : "+company_data);	
				}*/
				formObject.clear("cmplx_PartnerDetails_cmplx_partnerGrid");
				formObject.clear("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
				//formObject.clear("CompanyDetails_Frame3");
				/*for(int i=0;i<row_count;i++)
				{
					formObject.addItemFromList("cmplx_CompanyDetails_cmplx_CompanyGrid", company_data.get(i));
				}*/
				throw new ValidatorException(new FacesMessage("Only One secondary Applicant type can be added!"));
			}
			else
			{
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}
			formObject.setLocked("CompanyDetails_Frame1",false);
			formObject.setLocked("cif",true);
			//added by akshay on 18/1/18 for unlocking frame which was locked when primary row was clicked
			//pcasp-2832
			//Deepak Changes done for BusinessElite -- July 2022
			formObject.setLocked("CompanyDetails_BusinessElite",true);
			formObject.setEnabled("CompanyDetails_BusinessElite", false);
			formObject.setNGValue("COmpany_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
			formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));
			formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("COmpany_Name"));

			//formObject.setLocked("lob",true);
		}

		else  if("CompanyDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			//added by akshay on 18/1/18 for unlocking frame which was locked when primary row was clicked
			//pcasp-2832 by shweta
			formObject.setLocked("CompanyDetails_Frame1",false);
			formObject.setLocked("cif",true);
			//Deepak Changes done for BusinessElite -- July 2022
			formObject.setLocked("CompanyDetails_BusinessElite",true);
			formObject.setEnabled("CompanyDetails_BusinessElite", false);
			formObject.setNGValue("COmpany_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
			formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));
			formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("COmpany_Name"));


			//formObject.setLocked("lob",true);
		}

		else  if("CompanyDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");


		}

		else  if("PartnerDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("partner_winame",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("partner_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");


		}

		else  if("PartnerDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		else  if("PartnerDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		else  if("AuthorisedSignDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("AuthorisedSign_wiName",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("AuthorisedSign_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			//added by akshay on 18/1/18 for enabling fields
			formObject.setLocked("AuthorisedSignDetails_CheckBox1",false);
			//formObject.setLocked("AuthorisedSignDetails_CIFNo",false);
			formObject.setLocked("AuthorisedSignDetails_FetchDetails",false);
			formObject.setLocked("AuthorisedSignDetails_Name",false);
			formObject.setLocked("AuthorisedSignDetails_DOB",false);
			formObject.setLocked("AuthorisedSignDetails_VisaNumber",false);
			formObject.setLocked("AuthorisedSignDetails_PassportNo",false);
			formObject.setLocked("AuthorisedSignDetails_nationality",false);
			formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_Status",false);
			formObject.setLocked("AuthorisedSignDetails_delete",false);
			formObject.setNGValue("AuthorisedSignDetails_nationality", "");
			formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
			formObject.setNGValue("AuthorisedSignDetails_SoleEmployee", "--Select--");
			formObject.setNGValue("Designation", "");
			formObject.setNGValue("DesignationAsPerVisa", "");
			if(formObject.isLocked("CompanyDetails_Frame1")){
				formObject.setLocked("CompanyDetails_Frame1",false);
				formObject.setLocked("cif",true);
				formObject.setLocked("lob",true);

			}

		}
		/*	else if ("WorldCheck_fetch".equalsIgnoreCase(pEvent.getSource().getName()) || "WorldCheck_fetch".equalsIgnoreCase(pEvent.getSource().getName())){
			String popupFlag="Y";
			String outputResponseFIRCO="";
			String ReturnCodeFIRCO="";
			String ReturnDescFIRCO = "";
			String requestDate=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

			outputResponseFIRCO=  GenXml.GenerateXML("COMPLIANCE_CHECK","");
			RLOS.mLogger.info("NEW Shweta @@@@-" +outputResponseFIRCO);
			//outputResponseFIRCO="";
			//outputResponseFIRCO="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"  encoding=\"utf-8\"?> <EE_EAI_MESSAGE> <EE_EAI_HEADER> <MsgFormat>COMPLIANCE_CHECK</MsgFormat> <MsgVersion>0001</MsgVersion> <RequestorChannelId>CAS</RequestorChannelId> <RequestorUserId>RAKUSER</RequestorUserId> <RequestorLanguage>E</RequestorLanguage> <RequestorSecurityInfo>secure</RequestorSecurityInfo> <ReturnCode>0000</ReturnCode> <ReturnDesc>Success</ReturnDesc> <MessageId>123123453</MessageId><Extra1>REP||SHELL.JOHN</Extra1> <Extra2>YYYY-MM-DDThh:mm:ss.mmm+hh:mm</Extra2> </EE_EAI_HEADER> <ComplianceCheckResponse> <SystemID>String</SystemID><FilterationDate>2018/09/11 22:17:56 </FilterationDate><StatusBehavior>1</StatusBehavior><StatusName>FALSE</StatusName><AlertDetails><![CDATA[Suspect(s) detected by OFAC-Agent:3SystemId: Associate: =============================Suspect detected #1OFAC ID:AS04979205MATCH: 0.00TAG: NAMMATCHINGTEXT: MANOJ KUMAR, RESULT: (0)NAME: KUMAR, MONOJ Synonyms: noneADDRESS: Synonyms: noneCITY: https://accuity.worldcompliance.com/signin.aspx?ent=158f588d-2b86-41e8-8432-5b7663dbf90aTYS: 1ISN: 0=============================   *** INTERNAL OFAC DETAILS ***HasSndRcvInLimited: 0|AS04979205|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1||AS08151740|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1||AS05752051|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1|]]></AlertDetails></ComplianceCheckResponse> </EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			ReturnCodeFIRCO = outputResponseFIRCO.contains("<ReturnCode>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponseFIRCO.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info("RLOS value of ReturnCode"+ReturnCodeFIRCO);
			ReturnDescFIRCO =  outputResponseFIRCO.contains("<ReturnDesc>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponseFIRCO.indexOf("</ReturnDesc>")):"";    
			RLOS.mLogger.info("RLOS value of ReturnDesc"+ReturnDescFIRCO);
			if("0000".equalsIgnoreCase(ReturnCodeFIRCO) || "FFF002".equalsIgnoreCase(ReturnCodeFIRCO)||"FFFPEN".equalsIgnoreCase(ReturnCodeFIRCO))
			{
				String SystemID =  outputResponseFIRCO.contains("<SystemID>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<SystemID>")+"</SystemID>".length()-1,outputResponseFIRCO.indexOf("</SystemID>")):"";    
				RLOS.mLogger.info("RLOS value of ContractID"+SystemID);					
				String FilterationDate =  outputResponseFIRCO.contains("<FilterationDate>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<FilterationDate>")+"</FilterationDate>".length()-1,outputResponseFIRCO.indexOf("</FilterationDate>")):"";    
				String StatusBehavior =  outputResponseFIRCO.contains("<StatusBehavior>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusBehavior>")+"</StatusBehavior>".length()-1,outputResponseFIRCO.indexOf("</StatusBehavior>")):"";    
				String StatusName =  outputResponseFIRCO.contains("<StatusName>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusName>")+"</StatusName>".length()-1,outputResponseFIRCO.indexOf("</StatusName>")):"";    
				String AlertDetails =  outputResponseFIRCO.contains("<AlertDetails>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<AlertDetails>")+"</AlertDetails>".length()-1,outputResponseFIRCO.indexOf("</AlertDetails>")):"";    
				String NewgenStatus="Pending";
				if (StatusBehavior.equalsIgnoreCase("1") || StatusBehavior.equalsIgnoreCase("")){	
					NewgenStatus="Done";	
				} else if(StatusBehavior.equalsIgnoreCase("2")){
					NewgenStatus="Rejected";
				} else {
					NewgenStatus="Pending";
				}
				//check if WI is present in table
				String 	PassportNumber =formObject.getNGValue("cmplx_Customer_PAssportNo");
				String UpdatedDateAndTime=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());



				String updateOldRecord="update ng_rlos_FIRCO with (nolock) set call_valid='N' where Workitem_no='"+ formObject.getWFWorkitemName()+"' and call_type='Primary'";
				formObject.saveDataIntoDataSource(updateOldRecord);
				RLOS.mLogger.info("RLOS value of deleteQuery "+updateOldRecord);

				String query="INSERT INTO NG_RLOS_FIRCO (Process_name,Workitem_no,Firco_ID,Request_datatime,Workstep_name,Newgen_status,StatusBehavior,StatusName,AlertDetails,Updated_Datetime,Call_type,call_valid,passport) "
						+ "VALUES('"+formObject.getWFProcessName()+"','"+ formObject.getWFWorkitemName()+"','"+SystemID+"','"+requestDate+"','"+formObject.getWFActivityName()+"','"+NewgenStatus+"','"+StatusBehavior+"','"+StatusName+"','"+AlertDetails+"','"+UpdatedDateAndTime+"','Primary','Y','"+PassportNumber+"')";
				RLOS.mLogger.info("RLOS value of InsertQuery "+query);	
				formObject.saveDataIntoDataSource(query);

				formObject.setNGValue("FIRCO_dec",NewgenStatus);
				formObject.setVisible("Label5", true);
				formObject.setVisible("FircoStatusLabel", true);
				formObject.setNGValue("FircoStatusLabel", NewgenStatus);
				String 	Gender =formObject.getNGValue("cmplx_Customer_gender");
				String 	Nationality =formObject.getNGValue("cmplx_Customer_Nationality");
				String 	CountryOfResidence =formObject.getNGValue("cmplx_Customer_COUNTRYOFRESIDENCE");
				String 	PassportExpiryDate  =formObject.getNGValue("cmplx_Customer_PassPortExpiry");
				String 	PassportIssuingCountry =formObject.getNGValue("cmplx_Customer_Nationality");
				String  FirstName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
				String 	MiddleName= formObject.getNGValue("cmplx_Customer_MiddleName");
				String 	LastName =formObject.getNGValue("cmplx_Customer_LAstNAme");
				String 	DateOfBirthOrIncorporation   =formObject.getNGValue("cmplx_Customer_DOb");
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					Date new_exp = formatter.parse(PassportExpiryDate);
					Date DOB = formatter.parse(DateOfBirthOrIncorporation);
					PassportExpiryDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new_exp);
					DateOfBirthOrIncorporation = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(DOB);
				} catch (ParseException e) {
					PassportExpiryDate= common.Convert_dateFormat(PassportExpiryDate, "dd/MM/yyyy", "MM/dd/yyyy");
					DateOfBirthOrIncorporation= common.Convert_dateFormat(PassportExpiryDate, "dd/MM/yyyy", "MM/dd/yyyy");
					e.printStackTrace();
				}

				String archiFirco="INSERT INTO NG_FIRCO_ARCHIVAL (FirstName,MiddleName,LastName,wi_Name,Gender,DateOfBirthOrIncorporation,Nationality,CountryOfResidence,PassportNumber,PassportExpiryDate,PassportIssuingCountry)"
					+ "VALUES('"+FirstName+"','"+MiddleName+"','"+LastName+"','"+ formObject.getWFWorkitemName()+"','"+ Gender+"','"+DateOfBirthOrIncorporation+"','"+Nationality+"','"+CountryOfResidence+"','"+PassportNumber+"','"+PassportExpiryDate+"','"+PassportIssuingCountry+"')";				
				formObject.saveDataIntoDataSource(archiFirco);

				if("".equalsIgnoreCase(StatusBehavior) || StatusBehavior.equalsIgnoreCase("1") || StatusBehavior.equalsIgnoreCase("0") || StatusBehavior.equalsIgnoreCase("4")){
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL015");
					formObject.setNGValue("IS_WORLD_CHECK","Y");

				}
			   else {
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL016");
					formObject.setNGValue("IS_WORLD_CHECK","YES");
				}
			} 
			else {
				formObject.setNGValue("IS_WORLD_CHECK","N");
				alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL017");
			}

		throw new ValidatorException(new FacesMessage(alert_msg));			
		}*/
		// FIRCO-end

		else  if("AuthorisedSignDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			//added by akshay on 18/1/18 for enabling fields
			formObject.setLocked("AuthorisedSignDetails_CheckBox1",false);
			//formObject.setLocked("AuthorisedSignDetails_CIFNo",false);
			formObject.setLocked("AuthorisedSignDetails_FetchDetails",false);
			formObject.setLocked("AuthorisedSignDetails_Name",false);
			formObject.setLocked("AuthorisedSignDetails_DOB",false);
			formObject.setLocked("AuthorisedSignDetails_VisaNumber",false);
			formObject.setLocked("AuthorisedSignDetails_PassportNo",false);
			formObject.setLocked("AuthorisedSignDetails_nationality",false);
			formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_Status",false);
			formObject.setLocked("AuthorisedSignDetails_delete",false);
			formObject.setNGValue("AuthorisedSignDetails_nationality", "");
			formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
			formObject.setNGValue("AuthorisedSignDetails_SoleEmployee", "--Select--");
			formObject.setNGValue("Designation", "");
			formObject.setNGValue("DesignationAsPerVisa", "");
			if(formObject.isLocked("CompanyDetails_Frame1")){
				formObject.setLocked("CompanyDetails_Frame1",false);
				formObject.setLocked("cif",true);
				formObject.setLocked("lob",true);

			}
		}

		else  if("AuthorisedSignDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			formObject.setNGValue("AuthorisedSignDetails_nationality", "");
			formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
			formObject.setNGValue("AuthorisedSignDetails_SoleEmployee", "--Select--");
			formObject.setNGValue("Designation", "");
			formObject.setNGValue("DesignationAsPerVisa", "");
		}

		else if ("Liability_New_Add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("LiabilityAddition_wiName",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("LiabilityAddition_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if ("Liability_New_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if ("Liability_New_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}	


		else if ("addr_Add".equalsIgnoreCase(pEvent.getSource().getName())  ){
			//RLOS.mLogger.info( "Inside addredd grid add button");
			//	boolean flag_addressType= Address_Validate();
			//if(flag_addressType){sdfsd
			formObject.setNGValue("AddressDetails_address_Wi_name", formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside address button: "+formObject.getNGValue("address_Wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			//}


		}

		else if ("addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			boolean flag_addressType= Address_Validate();
			if(flag_addressType)
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");

		}


		else if ("addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}


		else if ("SupplementCardDetails_add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("supplement_WiName",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("supplement_WiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");
			AddFromHiddenList("SupplementCardDetails_AddressList","cmplx_AddressDetails_cmplx_AddressGrid");
			AddFromHiddenList("SupplementCardDetails_FatcaList","cmplx_FATCA_cmplx_GR_FatcaDetails");
			AddFromHiddenList("SupplementCardDetails_KYCList","cmplx_KYC_cmplx_KYCGrid");
			AddFromHiddenList("SupplementCardDetails_OecdList","cmplx_OECD_cmplx_GR_OecdDetails");
			//commented by nikhil for PCAS-2312
			//setDataInMultipleAppGrid();
			loadDynamicPickList();
			/* PCASP-592
			 * formObject.setNGValue("ResdCountry","AE");
			formObject.setLocked("ResdCountry",true);*/
		}


		else if ("SupplementCardDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "SupplementCardDetails_cmplx_SupplementGrid");
			//commneted by nikhil for PCAS-2312
			//setDataInMultipleAppGrid();
			loadDynamicPickList();
			if(formObject.isLocked("SupplementCardDetails_FetchDetails")==false){
				AddFromHiddenList("SupplementCardDetails_AddressList","cmplx_AddressDetails_cmplx_AddressGrid");
				AddFromHiddenList("SupplementCardDetails_FatcaList","cmplx_FATCA_cmplx_GR_FatcaDetails");
				AddFromHiddenList("SupplementCardDetails_KYCList","cmplx_KYC_cmplx_KYCGrid");
				AddFromHiddenList("SupplementCardDetails_OecdList","cmplx_OECD_cmplx_GR_OecdDetails");
			}
			/* PCASP-592
			 * formObject.setNGValue("ResdCountry","AE");
			formObject.setLocked("ResdCountry",true);*/
		}


		else if ("SupplementCardDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "SupplementCardDetails_cmplx_SupplementGrid");
			//setDataInMultipleAppGrid();
			loadDynamicPickList();
			/*clearSelectiveRows("Supplementary_Card_Details","cmplx_AddressDetails_cmplx_AddressGrid");
			clearSelectiveRows("Supplementary_Card_Details","cmplx_FATCA_cmplx_GR_FatcaDetails");
			clearSelectiveRows("Supplementary_Card_Details","cmplx_KYC_cmplx_KYCGrid");
			clearSelectiveRows("Supplementary_Card_Details","cmplx_OECD_cmplx_GR_OecdDetails");*/
		}	


		else if ("OECD_add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("OECD_winame"));
			//below code by saurabh on 22md july 17.
			formObject.setEnabled("OECD_noTinReason",true);
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
			//temporary addition by saurabh on 29th Dec
			//formObject.saveFragment("OECD");
		}


		else if ("OECD_modify".equalsIgnoreCase(pEvent.getSource().getName()) || "OECD_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
			//temporary addition by saurabh on 29th Dec
			//formObject.saveFragment("OECD");
		}


		else if ("OECD_delete".equalsIgnoreCase(pEvent.getSource().getName()) || "OECD_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
			//temporary addition by saurabh on 29th Dec
			//formObject.saveFragment("OECD");
		}	
		//Below code added by shweta 
		else if ("DDS_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_dds");//cmplx_CC_Loan_cmplx_btc
			RLOS.mLogger.info( "Inside add button33: add of btc details");
			//formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
		}

		else if ("DDS_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_dds");
			RLOS.mLogger.info( "Inside add button33: modify of btc details");

		}
		else if ("DDS_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_dds");
			RLOS.mLogger.info( "Inside add button33: delete of btc details");
		}
		else if ("SI_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_si");//cmplx_CC_Loan_cmplx_btc
			RLOS.mLogger.info( "Inside add button33: add of btc details");
			//formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
		}

		else if ("SI_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_si");
			RLOS.mLogger.info( "Inside add button33: modify of btc details");
			formObject.setNGValue("cmplx_CC_Loan_StartMonth", "1"); //PCASP-3186
		}
		else if ("SI_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_si");
			RLOS.mLogger.info( "Inside add button33: delete of btc details");
			formObject.setNGValue("cmplx_CC_Loan_StartMonth", "1"); //PCASP-3186
		}
		else if ("RVC_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_rvc");//cmplx_CC_Loan_cmplx_btc
			RLOS.mLogger.info( "Inside add button33: add of btc details");
			//formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
		}

		else if ("RVC_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_rvc");
			RLOS.mLogger.info( "Inside add button33: modify of btc details");

		}
		else if ("RVC_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_rvc");
			RLOS.mLogger.info( "Inside add button33: delete of btc details");
		}
		//Above Code added by shweta
		//added by akshay on 15/9/17 to add data in BT Grid
		else if ("BT_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("CC_Loan_wi_name"));
			Double availBal = maxAmountAllowed(formObject);//PCASP-3191
			if(Double.parseDouble(formObject.getNGValue("amount"))>availBal){
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL073")+" "+availBal;
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
			//formObject.setNGValue("benificiaryName", formObject.getNGValue("Customer_Name"),false);//Tarang to be removed on friday(1/19/2018)//added by akshay on 21/11/17 as per FSD 2.7
			formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"), false);
			//RLOS.mLogger.info( "Inside add button: benificiaryName "+formObject.getNGValue("benificiaryName"));
			String accnum = formObject.getNGValue("Account_Number");
			//formObject.setNGValue("creditcardNo", accnum,false);
			//RLOS.mLogger.info( "Inside add button: Account_Number "+formObject.getNGValue("Account_Number"));
			//Added by aman on 08/12

		}

		else if ("BT_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			Double availBal = maxAmountAllowed(formObject);//PCASP-3191
			if(Double.parseDouble(formObject.getNGValue("amount"))>availBal){
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL073")+" "+availBal;
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
			//formObject.setNGValue("benificiaryName", formObject.getNGValue("Customer_Name"));//Tarang to be removed on friday(1/19/2018)//added by akshay on 21/11/17 as per FSD 2.7
			formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme") , false);
			String accnum = formObject.getNGValue("Account_Number");
			//formObject.setNGValue("creditcardNo", accnum);
			//Added by aman on 08/12
			formObject.setLocked("transtype", false);
			formObject.setLocked("transferMode", false);
		}

		else if ("BT_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
			formObject.setLocked("transtype", false);
			formObject.setLocked("transferMode", false);
		}
		else if("cmplx_CC_Loan_cmplx_btc".equalsIgnoreCase(pEvent.getSource().getName())){
			String transferMode = formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", formObject.getSelectedIndex("cmplx_CC_Loan_cmplx_btc"),2);
			String transtype = formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", formObject.getSelectedIndex("cmplx_CC_Loan_cmplx_btc"),0);

			LoadPickList("marketingCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MarketingCodeService with (nolock) where TransactionType = '"+transtype+"' and IsActive = 'Y' order by code");

			if(transferMode.equalsIgnoreCase("C") && "SC".equalsIgnoreCase(transtype))
			{
				formObject.setLocked("dispatchChannel",false);
				formObject.setNGValue("dispatchChannel","998");
				formObject.setLocked("chequeNo",false);
				formObject.setLocked("chequeDate",false);
				formObject.setLocked("chequeStatus",false);
				formObject.setLocked("tenor",true);
				formObject.setLocked("creditcardNo",true);
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				formObject.setVisible("CC_Loan_Label23", false);
				formObject.setVisible("Account_No_for_Swift", false);
			}
			else if(transferMode.equalsIgnoreCase("S") && "SC".equalsIgnoreCase(transtype))
			{
				formObject.setLocked("chequeNo",true);
				formObject.setLocked("chequeDate",true);
				formObject.setLocked("chequeStatus",true);
				formObject.setLocked("dispatchChannel",true);
				formObject.setLocked("tenor",true);
				formObject.setLocked("creditcardNo",true);
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				formObject.setVisible("CC_Loan_Label23", true);
				formObject.setVisible("Account_No_for_Swift", true);
			}
			if(transferMode.equalsIgnoreCase("A") && transtype!="BT")
			{
				formObject.setVisible("CC_Loan_Label23", false);
				formObject.setVisible("Account_No_for_Swift", false);
				formObject.setVisible("CC_Loan_Label24", true);
				formObject.setVisible("Account_No_for_AT", true);
			}
			else if(transferMode.equalsIgnoreCase("S") && transtype!="BT")
			{
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				formObject.setVisible("CC_Loan_Label23", true);
				formObject.setVisible("Account_No_for_Swift", true);
			}
			else{
				formObject.setVisible("CC_Loan_Label23", false);
				formObject.setVisible("Account_No_for_Swift", false);
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				if("BT".equalsIgnoreCase(transtype)){
					formObject.setLocked("tenor",true);

				}
			}
		}
		//ended by akshay on 15/9/17 to add data in BT Grid




		else if("ReadFromCard".equalsIgnoreCase(pEvent.getSource().getName())){
			popupFlag = "Y";
			outputResponse = GenXml.GenerateXML("EID_Genuine","");
			ReturnCode =  (outputResponse.contains("<ns3:ServiceStatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusCode>")+"</ns3:ServiceStatusCode>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusCode>")):"";
			String Returndesc = (outputResponse.contains("<ns3:ServiceStatusDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusDesc>")+"</ns3:ServiceStatusDesc>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusDesc>")):"";
			//  ReturnCode="123";
			//RLOS.mLogger.info(ReturnCode);
			//RLOS.mLogger.info(Returndesc);
			if("s".equalsIgnoreCase(ReturnCode) && "Valid".equalsIgnoreCase(Returndesc)){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse, "");  
				formObject.setNGValue("Is_EID_Genuine", "Y");
				formObject.setNGValue("cmplx_Customer_IsGenuine", true);
				//RLOS.mLogger.info("EID is generated");
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL002");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}   
			else{
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL003");
				formObject.setNGValue("Is_EID_Genuine", "N");
				//RLOS.mLogger.info("EID is not generated");
			}

			//RLOS.mLogger.info(formObject.getNGValue("Is_EID_Genuine"));					
		}    

		else if("FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{  
			formObject = FormContext.getCurrentInstance().getFormReference();
			RLOS.mLogger.info("Inside FetchDetails click:: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
			formObject.setNGValue("cmplx_Customer_IscustomerSave", "N");//added by akshay on 19/3/18
			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
			//RLOS.mLogger.info( "Fetch Detail Started");
			String cif_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			//RLOS.mLogger.info( "Value of cif_id_avail"+cif_no);

			//Hritik 31.2.22 JS->JAVA
			RLOS.mLogger.info("FetchDetails:");
			formObject.setEnabled("cmplx_Customer_VIsaExpiry",true);

			if("".equalsIgnoreCase(cif_no)){ 				
				RLOS.mLogger.info( "Value of cif_id_avail is false"+cif_no);
				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				//RLOS.mLogger.info( "EID value for Entity Details: "+EID );
				String ReadFrom_card_exc = formObject.getNGValue("cmplx_Customer_readfromcardflag");
				if( EID!=null && !"".equalsIgnoreCase(EID)&& "Y".equalsIgnoreCase(ReadFrom_card_exc))
				{							
					outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Primary_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					//RLOS.mLogger.info(ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("Is_Entity_Details","Y");
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");

					}
					else{
						formObject.setNGValue("Is_Entity_Details","N");

					}
					//RLOS.mLogger.info(formObject.getNGValue("Is_Entity_Details"));							
				}
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				//RLOS.mLogger.info("Customer Eligibility");
				ReturnCode = getTagValue(outputResponse, "ReturnCode");  
				//(outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				//RLOS.mLogger.info("Post new change CUSTOMER_ELIGIBILITY: "+ ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					setcustomer_enable();
					popupFlag="Y";
					//Deepak below line commented as Customer eligibility called before Parse Cif Eligibility 26Dec2017 
					//Deepak below line again uncommented to set NTB 27Dec2017
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");
					parse_cif_eligibility(outputResponse,"Primary_CIF");
					String cifi = formObject.getNGValue("cmplx_Customer_CIFNO");
					if("".equalsIgnoreCase(cifi))
					{
						formObject.setNGValue("NTB","true");//added by Akshay
					}
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					//Customer_enable();
					// if(NTB_flag.equalsIgnoreCase("true")){
					//  Customer_enable();

					//}
					if("false".equalsIgnoreCase(NTB_flag)){
						// parse_cif_eligibility(outputResponse,"Primary_CIF");
						/*	formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700);*/	
						//Code change to run Customer details if customer is existing customer in Customer Eligibility start (27-sept-2017)
						String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
						if(cif!=null && !"".equalsIgnoreCase(cif)){
							//changed for PCSP-488
							alert_msg =  fetch_cust_details_primary();
						}
						//Code change to run Customer details if customer is existing customer in Customer Eligibility End (27-sept-2017)
						//Deepak changes check supplementary coming for Primary Card PCAS-1911
						if(supplementry_to_PrimaryCheck()){
							alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL066");
						}
					}

					else if("true".equalsIgnoreCase(NTB_flag)){
						formObject.setVisible("Customer_Frame2", false);
						formObject.setEnabled("cmplx_Customer_VIsaExpiry", true);
						formObject.setLocked("cmplx_Customer_VisaNo", false);
						formObject.setLocked("IncomeDetails_FinacialSummarySelf", true);
						formObject.setNGValue("cmplx_Customer_CIFNO","");//added by akshay for proc 11596

						//RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");
						throw new ValidatorException(new FacesMessage(alert_msg));
						//added By Akshay
					}

					//@@@@Prabhakar
					/*formObject.addComboItem("Customer_Type", "xyc", "xzzzzz");
					formObject.addComboItem("cmplx_FATCA_CustomerType", "", pvalue);
					formObject.addComboItem("KYC_CustomerType", "xyzzzzz", "xyzzzz");
					formObject.addComboItem("OECD_CustomerType", pvalue, pvalue);*/
					formObject.addItem("Customer_Type", "zyxxxxx");
					formObject.addItem("cmplx_FATCA_CustomerType", "zyxxxxx");
					formObject.addItem("KYC_CustomerType", "zyxxxxx");
					formObject.addItem("OECD_CustomerType", "zyxxxxx");

					//ended By akshay		

					/*
				else{
					alert_msg = fetch_cust_details_primary();
				}
					 */


					//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
					//RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
					//RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
					//RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));

					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility")) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details")))
					{ 
						//RLOS.mLogger.info("inside if condition");
						//RLOS.mLogger.info( "Customer Eligibility and Customer details fetched sucessfully");
						formObject.setEnabled("FetchDetails", false); 
						formObject.setEnabled("Customer_Button1", false);
						//alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL006");
					}
					else{
						//RLOS.mLogger.info( "Customer Eligibility and Customer details failed");
						//alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL007");
						formObject.setEnabled("FetchDetails", true);
						formObject.setEnabled("Customer_Button1", false);
					}
					//RLOS.mLogger.info("");
					/*try{Thread.sleep(1000);}catch(Exception e){}
					formObject.RaiseEvent("WFSave");*/

					popupFlag="Y";
					//RLOS.mLogger.info( "Alert msg to be displayed on screen: "+alert_msg);
					loadDynamicPickList();
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					popupFlag="Y";
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL005");
					loadDynamicPickList();
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}


			else{
				alert_msg = fetch_cust_details_primary();
				loadDynamicPickList();
				formObject.RaiseEvent("WFSave");
				RLOS.mLogger.info("inside fetch cif details before CIFno: ");
				//				formObject.setNGValue("cmplx_Customer_age","")
				formObject.setNGValue("cmplx_Customer_Email_ID", formObject.getNGValue("AlternateContactDetails_Email1"));
				common.getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
				formObject.setEnabled("cmplx_EmploymentDetails_Designation", true);
				formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
				String desg = formObject.getNGValue("cmplx_EmploymentDetails_Designation");
				RLOS.mLogger.info("inside fetch cif details desg: "+ desg);
				formObject.setNGValue("cmplx_Customer_Designation", desg);
				formObject.setEnabled("cmplx_EmploymentDetails_Designation", false);


				throw new ValidatorException(new FacesMessage(alert_msg));
			}

		}


		else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
			// hritik 31.2.22 JS->JAVA
			RLOS.mLogger.info("Customer_Button1 :");
			formObject.setEnabled("Customer_Button1",false);

			String NegatedFlag;
			popupFlag="Y";
			outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
			//RLOS.mLogger.info("Customer Eligibility");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);
			formObject.setNGValue("Is_Customer_Eligibility","Y");


			if("0000".equalsIgnoreCase("0000")){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"Primary_CIF"); 
				parse_cif_eligibility(outputResponse,"Primary_CIF");
				BlacklistFlag= (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				//RLOS.mLogger.info("Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag= (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				//RLOS.mLogger.info("Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				//RLOS.mLogger.info("Customer is NegatedFlag"+NegatedFlag);
				formObject.setNGValue("Is_Customer_Eligibility","Y");
				//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
				formObject.setNGValue("BlacklistFlag",BlacklistFlag);
				formObject.setNGValue("DuplicationFlag",DuplicationFlag);
				formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
				String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
				if("true".equalsIgnoreCase(NTB_flag)){
					//RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
					formObject.setNGValue("cmplx_Customer_CIFNO","");//added by akshay for proc 11596
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");
				}
				else{
					//Deepak changes check supplementary coming for Primary Card PCAS-1911

					/*	formObject.setHeight("Customer_Frame1", 620);
					formObject.setHeight("CustomerDetails", 700);*/
					//change by saurabh on 7may
					/*formObject.setTop("ProductDetailsLoader",formObject.getTop("CustomerDetails")+formObject.getHeight("CustomerDetails"));
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));*/
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL008");
					//Deepak changes check supplementary coming for Primary Card PCAS-1911
					if(supplementry_to_PrimaryCheck()){
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL066");
					}
				}


			}
			else{
				formObject.setNGValue("Is_Customer_Eligibility","N");
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL005");

			}
			//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
			//RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
			//RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
			//RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));

			try{Thread.sleep(1000);}catch(Exception e){}
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//}
		}
		//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)

		//Fetch Details call in Supplementary details tab by saurabh on 11th September.
		else if("SupplementCardDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text1");
			//RLOS.mLogger.info( "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
			String primaryCif = null;
			boolean isEntityDetailsSuccess = false;
			//cmplx_Customer_card_id_available
			if( EmiratesID!=null && !"".equalsIgnoreCase(EmiratesID)){
				outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				//RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					//RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Supplementary_Card_Details");
					primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
					formObject.setNGValue("Supplementary_CIFNO",primaryCif);
					isEntityDetailsSuccess = true;
					RLOS.mLogger.info(primaryCif+"primary cif inside else"+formObject.getNGValue("SupplementCardDetails_CIF"));

					String Query_suppCif = "select COUNT(cif) as CIF_count from CAPS_MAIN_MIG_DATA where ECRN in (select  distinct ECRN from CAPS_MAIN_MIG_DATA where CIF ='"+formObject.getNGValue("cmplx_Customer_CIFNo")+"') and CIF = '"+primaryCif+"' and substring(crn,8,2)!='00';";
					List<List<String>> result = formObject.getNGDataFromDataCache(Query_suppCif);
					if(result.get(0).get(0)!=null && Integer.parseInt(result.get(0).get(0))>0){
						formObject.setNGValue("SupplementCardDetails_CIF",primaryCif);
						alert_msg = fetch_cust_details_supplementary();
					}
					else{
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004"); 
						formObject.setNGValue("SupplementCardDetails_CIF","");
						formObject.setNGValue("Supplementary_CIFNO","");
						formObject.setLocked("SupplementCardDetails_FetchDetails", true);
					}


					outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
					//RLOS.mLogger.info("Customer Eligibility");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					//RLOS.mLogger.info(ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode) )
					{
						formObject.setNGValue("SupplementCardDetails_linked_product", getLinkedProduct(outputResponse));
						alert_msg = fetch_cust_details_supplementary();
					}
					//alert_msg = fetch_cust_details_supplementary();
				}

				//RLOS.mLogger.info(primaryCif);
			}
			if(!isEntityDetailsSuccess || (primaryCif==null || "".equalsIgnoreCase(primaryCif))){
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
				//RLOS.mLogger.info("Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				//RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					RLOS.mLogger.info("inside success line 2148");
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse ,"Supplementary_Card_Details");    
					parse_cif_eligibility(outputResponse,"Supplementary_Card_Details");
					String NTBFlag=(outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
					if("Y".equals(NTBFlag)){
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");	
						formObject.setLocked("SupplementCardDetails_FetchDetails", true);
						RLOS.mLogger.info("NTB FLAG ");
					}
					else{
						String Query_suppCif = "select COUNT(cif) as CIF_count from CAPS_MAIN_MIG_DATA where ECRN in (select  distinct ECRN from CAPS_MAIN_MIG_DATA where CIF ='"+formObject.getNGValue("cmplx_Customer_CIFNo")+"') and CIF = '"+primaryCif+"' and substring(crn,8,2)!='00';";
						List<List<String>> result = formObject.getNGDataFromDataCache(Query_suppCif);
						if(result.get(0).get(0)!=null && Integer.parseInt(result.get(0).get(0))>0){
							RLOS.mLogger.info(primaryCif+"primary cif");
							formObject.setNGValue("SupplementCardDetails_CIF",primaryCif);
							alert_msg = fetch_cust_details_supplementary();
						}
						else{
							alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004"); 
							formObject.setNGValue("SupplementCardDetails_CIF","");
							formObject.setNGValue("Supplementary_CIFNO",primaryCif);
							RLOS.mLogger.info(primaryCif+"primary cif inside else"+formObject.getNGValue("SupplementCardDetails_CIF"));
							formObject.setLocked("SupplementCardDetails_FetchDetails", true);
						}
						formObject.setNGValue("SupplementCardDetails_linked_product", getLinkedProduct(outputResponse));
						//alert_msg = fetch_cust_details_supplementary();
					}


				}
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("Send_OTP_Btn".equalsIgnoreCase(pEvent.getSource().getName()))
		{	
			if(RLOS_Initiation.otp_count>=5)
			{
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL071");
				formObject.setLocked("Send_OTP_Btn",true);
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			RLOS_Initiation.otp_count++;

			formObject.setNGValue("otp_ref_no", formObject.getWFFolderId());
			//RLOS.mLogger.info( formObject.getWFFolderId()+"");
			hm.put("Send_OTP_Btn","Clicked");
			popupFlag="Y";
			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","GenerateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				//RLOS.mLogger.info("OTP is generated");
				formObject.setEnabled("OTP_No",true);
				formObject.setLocked("OTP_No",false);
				formObject.setEnabled("Validate_OTP_Btn",true);
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL009");
			}
			else{
				//formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL010");
				formObject.setEnabled("OTP_No",false);
				formObject.setEnabled("Validate_OTP_Btn",false);

			}
			//RLOS.mLogger.info("OTP generation");
			try
			{
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			finally{hm.clear();}
		}



		else   if("AuthorisedSignDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){


			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				formObject.setNGValue("Is_Customer_Details_AuthSign","Y");
				//RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_AuthSign"));
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Signatory_CIF");    
				//RLOS.mLogger.info("authorised sign is generated");
				//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
				//below code added by Arun (06/12/17) to make editable after fetching details successfully 
				formObject.setLocked("AuthorisedSignDetails_Name",false);
				formObject.setLocked("AuthorisedSignDetails_nationality",false);
				formObject.setLocked("AuthorisedSignDetails_DOB",false);
				formObject.setLocked("AuthorisedSignDetails_VisaNumber",false);
				formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",false);
				formObject.setLocked("AuthorisedSignDetails_Status",false);
				formObject.setLocked("AuthorisedSignDetails_PassportNo",false);
				formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",false);
				formObject.setLocked("AuthorisedSignDetails_shareholding",false);
				formObject.setLocked("AuthorisedSignDetails_SoleEmployee",false);
				//above code added by Arun (06/12/17) to make editable after fetching details successfully
				//added by akshay on 17/3/18 for proc 6434
				//formObject.setNGValue("estbDate",Convert_dateFormat(estbDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				formObject.setNGValue("AuthorisedSignDetails_DOB",Convert_dateFormat(formObject.getNGValue("AuthorisedSignDetails_DOB"),"yyyy-MM-dd","dd/MM/yyyy"),false);
				formObject.setNGValue("AuthorisedSignDetails_VisaExpiryDate",Convert_dateFormat(formObject.getNGValue("AuthorisedSignDetails_VisaExpiryDate"),"yyyy-MM-dd","dd/MM/yyyy"),false);
				formObject.setNGValue("AuthorisedSignDetails_PassportExpiryDate",Convert_dateFormat(formObject.getNGValue("AuthorisedSignDetails_PassportExpiryDate"),"yyyy-MM-dd","dd/MM/yyyy"),false);

			}
			else{
				//RLOS.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details_AuthSign","N");
			}
			//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
		}


		else   if("DecisionHistory_SendSMS".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			//RLOS.mLogger.info( "");

			outputResponse = GenXml.GenerateXML("SEND_ADHOC_ALERT","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase("0000") ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				//RLOS.mLogger.info("SMS is send");
				//formObject.setEnabled("OTP_No",true);
				//formObject.setEnabled("Validate_OTP_Btn",true);
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL011");
			}
			else{
				//formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL012");
				//RLOS.mLogger.info("Error while sending SMS");
			}

		}




		else  if("Validate_OTP_Btn".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			if(RLOS_Initiation.validate_count>=5) //for SVT Point
			{
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL072");
				formObject.setLocked("Validate_OTP_Btn",true);
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			RLOS_Initiation.validate_count++;
			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","ValidateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);
			OTPStatus=(outputResponse.contains("<OTPStatus>")) ? outputResponse.substring(outputResponse.indexOf("<OTPStatus>")+"</OTPStatus>".length()-1,outputResponse.indexOf("</OTPStatus>")):"";    
			//RLOS.mLogger.info(OTPStatus);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				formObject.setNGValue("cmplx_Customer_OTPValidation","true");
				formObject.setLocked("OTP_No",true);
				formObject.setLocked("Validate_OTP_Btn",true);

				//RLOS.mLogger.info("OTP is generated");
				formObject.setNGValue("OTPStatus",OTPStatus);
				//RLOS.mLogger.info(formObject.getNGValue("OTPStatus"));

				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL013");

			}
			else{
				formObject.setNGValue("OTP_Generation","OTP is not generated");
				formObject.setLocked("Send_OTP_Btn",false);
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL014");
			}
			//RLOS.mLogger.info("OTP generation");
			try
			{
				popupFlag="Y";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			finally{hm.clear();}
		}


		else  if("CompanyDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info("CompanyDetails_Button3");
			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);
			/*	String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
		//RLOS.mLogger.info(CustId);
		String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
		//RLOS.mLogger.info(CorpName);
		String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
		//RLOS.mLogger.info(BusinessIncDate);
		String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
		//RLOS.mLogger.info(LegEnt);
			 */
			if("0000".equalsIgnoreCase(ReturnCode) ){
				formObject.setNGValue("Is_Customer_Details_CompanyCIF","Y");

				//RLOS.mLogger.info("value of company Details corporation"+formObject.getNGValue("Is_Customer_Details_CompanyCIF"));

				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Corporation_CIF");  
				String query="Select INDUSTRY_SECTOR,INDUSTRY_MACRO, INDUSTRY_MICRO from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME='"+formObject.getNGValue("compName")+"'";
				//RLOS.mLogger.info("Query to load alloc details: "+query);
				List<List<String>> output=formObject.getDataFromDataSource(query);
				if(output!=null && !output.isEmpty()){
					if(output.get(0).get(0)!=null && !"".equals(output.get(0).get(0))){
						formObject.setNGValue("indusSector",output.get(0).get(0));
					}
					if(output.get(0).get(1)!=null && !"".equals(output.get(0).get(1))){
						formObject.setNGValue("indusMAcro",output.get(0).get(1));
					}
					if(output.get(0).get(0)!=null && !"".equals(output.get(0).get(0))){
						formObject.setNGValue("indusMicro",output.get(0).get(2));
					}	
				}

				//	String Date1=BusinessIncDate;
				String estbDate = formObject.getNGValue("estbDate");
				String TLExpiry = formObject.getNGValue("TLExpiry");
				String TLIssueDate = formObject.getNGValue("TLIssueDate");

				if(estbDate!=null&&!"".equalsIgnoreCase(estbDate)){
					formObject.setNGValue("estbDate",Convert_dateFormat(estbDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					//RLOS.mLogger.info(formObject.getNGValue("estbDate"));
				}	
				if(TLIssueDate!=null&&!"".equalsIgnoreCase(TLIssueDate)){
					formObject.setNGValue("TLIssueDate",Convert_dateFormat(TLIssueDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					//RLOS.mLogger.info(formObject.getNGValue("TLIssueDate"));
				}
				if(TLExpiry!=null&&!"".equalsIgnoreCase(TLExpiry)){
					formObject.setNGValue("TLExpiry",Convert_dateFormat(TLExpiry, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					//RLOS.mLogger.info(formObject.getNGValue("TLExpiry"));
				}
				formObject.setLocked("appType", false);
				formObject.setLocked("compIndus", false);
				formObject.setLocked("TLIssueDate", false);
				formObject.setLocked("desig", false);
				formObject.setLocked("desigVisa", false);
				formObject.setLocked("indusSector", false);
				formObject.setLocked("indusMAcro", false);
				formObject.setLocked("indusMicro", false);
				//Deepak 12Nov2017 added to disbale the Cif IF button.
				formObject.setEnabled("CompanyDetails_Button3", false); 
				formObject.setEnabled("cif", false); 
				//RLOS.mLogger.info("corporation cif");
				//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
				String AECBheld =  (outputResponse.contains("<AECBConsentHeld>")) ? outputResponse.substring(outputResponse.indexOf("<AECBConsentHeld>")+"</AECBConsentHeld>".length()-1,outputResponse.indexOf("</AECBConsentHeld>")):"";
				if ("Y".equalsIgnoreCase(AECBheld))

				{
					//RLOS.mLogger.info( "Inside AECB Consent true check!!");
					if(!formObject.isVisible("Liability_New_Frame1")){
						formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
					}
					formObject.setNGValue("cmplx_Liability_New_AECBCompanyconsentAvail", "true");
					formObject.setLocked("Liability_New_fetchLiabilities",false);
				}
				else 
				{
					formObject.setNGValue("cmplx_Liability_New_AECBCompanyconsentAvail", "false");
					formObject.setLocked("Liability_New_fetchLiabilities",true);
				}
			}
			else{
				//RLOS.mLogger.info("Customer Details Corporation CIF is not generated");
				formObject.setNGValue("Is_Customer_Details_CompanyCIF","N");
			}
			//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
		}



		else  if ("Button9".equalsIgnoreCase(pEvent.getSource().getName())){

			if(!formObject.isVisible("IncomeDetails_Frame1")){
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				formObject.setNGFrameState("Incomedetails", 0);
				formObject.setNGFrameState("Incomedetails", 1);

				hm.put("Button9","Clicked");
				popupFlag="N";
				outputResponse = GenXml.GenerateXML("ACCOUNT_SUMMARY","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				//RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) ){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");
					formObject.setNGValue("Is_Account_Summary","Y");
				}
				else{
					formObject.setNGValue("Is_Account_Summary","N");
				}
				//RLOS.mLogger.info(formObject.getNGValue("Is_Account_Summary"));
				//ended


			}

			IMFields_Income();
			BTCFields_Income();
			LimitIncreaseFields_Income();
			ProductUpgrade_Income();

			if(!formObject.isVisible("EMploymentDetails_Frame1")){
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);//forDOJ enable
				formObject.setNGFrameState("EmploymentDetails", 0);
				formObject.setNGFrameState("EmploymentDetails", 1);


			}
			IMFields_Employment();
			BTCFields_Employment();
			LimitIncreaseFields_Employment();
			ProductUpgrade_Employment();


			if(!formObject.isVisible("Liability_New_Frame1")){
				formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
				formObject.setNGFrameState("Liability_New_Frame1", 0);
				formObject.setNGFrameState("Liability_New_Frame1", 1);
			}

		}


		else   if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName()) || "Customer_Search".equalsIgnoreCase(pEvent.getSource().getName())) {
			//RLOS.mLogger.info("Prabhakar +++ "+pEvent.getSource().getName());
			String EmpName="";
			String EmpCode="";
			String buttonName="";
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when micro ='--Select--' then 0 else 1 end");
			if(!formObject.isVisible("EMploymentDetails_Frame1")){
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);//forDOJ enable

			}//---added by akshay for proc 10423
			if("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				EmpName=formObject.getNGValue("EMploymentDetails_Text21");
				EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
				buttonName="EMploymentDetails_Button2";
			}
			//added by prabhakar for drop-4 point 10
			else
			{
				EmpName=formObject.getNGValue("cmplx_Customer_Employer_name");
				EmpCode=formObject.getNGValue("cmplx_Customer_Employer_code");
				buttonName="Customer_Search";
			}
			//added by prabhakar for drop-4 point 10 end
			//RLOS.mLogger.info( "EMpName$"+EmpName+"$");
			//changed by nikhil for PAYROLL FLAG Change PCASP-12
			String query;
			if("".equalsIgnoreCase(EmpName.trim())){
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPLOYER_CODE Like '"+EmpCode+"%'";
			}
			else if("".equalsIgnoreCase(EmpCode.trim())){
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME Like '"+EmpName + "%'";
			}
			else{
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME Like '"+EmpName + "%' or EMPLOYER_CODE Like '"+EmpCode+"%'";
			}
			//RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,buttonName, "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,Employer Category PL, Employer Status CC,Employer Status PL,MAIN EMPLOYER CODE,PAYROLL_FLAG,TYPE OF COMPANY,AUTHORIZED PERSON NAME AND DESIGNATION", true, 20,"Employer");

		}
		else if("CompanyDetails_SearchAloc".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			String EmpName="";
			String EmpCode="";
			String buttonName="";
			LoadPickList("indusMicro", "Select micro from (select '--Select--' as micro union select micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when micro ='--Select--' then 0 else 1 end");
			if("CompanyDetails_SearchAloc".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				EmpName=formObject.getNGValue("compName");
				//EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
				buttonName="CompanyDetails_SearchAloc";
			}

			//RLOS.mLogger.info( "EMpName$"+EmpName+"$");
			String query;

			query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME Like '"+EmpName + "%'";


			//RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,buttonName, "Company Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,Employer Category PL, Employer Status CC,Employer Status PL,MAIN EMPLOYER CODE", true, 20,"Search Company");

			//formObject.setLocked("compName", true);

			formObject.setLocked("indusSector", true);

			formObject.setLocked("indusMAcro", true);
			formObject.setLocked("indusMicro", true);

		}

		else if("cmplx_ExternalLiabilities_AECBConsent".equalsIgnoreCase(pEvent.getSource().getName())){
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_ExternalLiabilities_AECBConsent")))
				formObject.setEnabled("ExternalLiabilities_Button1",true);
			else
				formObject.setEnabled("ExternalLiabilities_Button1",false);
		}


		else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				//hritik 31.3.22 JS->jAVA
				if(formObject.getNGValue("cmplx_EmploymentDetails_Others").equalsIgnoreCase("true"))
				{
					RLOS.mLogger.info("cmplx_EmploymentDetails_Others :"+formObject.getNGValue("cmplx_EmploymentDetails_Others"));
					formObject.setEnabled("EMploymentDetails_Button2",false);
					formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",true);
				}
				else 
				{
					formObject.setEnabled("cmplx_EmploymentDetails_IncInPL",false);
					formObject.setEnabled("cmplx_EmploymentDetails_Freezone",true);
					formObject.setEnabled("cmplx_EmploymentDetails_IncInCC",false);
					formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",true);
				} //end



				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
					//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					//formObject.setNGValue("cmplx_EmploymentDetails_ApplicationCateg","CN");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					//formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",false);//added By Tarang started on 22/02/2018 as per drop 4 point 20
					LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusCC with(nolock) where isActive='Y' and product='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard")+"' order by code" );					
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",false);
					if("IM".equals(formObject.getNGValue("Subproduct_productGrid"))){
						formObject.removeItem("cmplx_EmploymentDetails_EmpStatusCC",2);
						formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CN");//was not happening from js so done from here
					}
				}
				else{
					//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) where code NOT LIKE '%CN%' order by code");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					//formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",true);//added By Tarang started on 22/02/2018 as per drop 4 point 20
					LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusCC with(nolock) where isActive='Y'  order by code" );					
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);//was not happening from js so done from here

				}
			}
			catch(Exception e)
			{
				RLOS.mLogger.info( "Exception occurred in removing item from cmplx_EmploymentDetails_EmpStatusCC"+printException(e));}
		}

		else if ("SupplementCardDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.setNGValue("supplement_WIname",formObject.getWFWorkitemName());	
			//RLOS.mLogger.info( "Inside Supplement_add button: "+formObject.getNGValue("supplement_WIname"));
			formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");		
		}

		/*else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
	{
		if(formObject.getNGValue("Product_Type").contains("Personal Loan")){
			String query="Select PRIME_TYPE,LSM_PRODDIFFRATE,prime_type_rate from NG_master_Scheme where SCHEMEId='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
			RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+query);
			List<List<String>> marginRate=formObject.getNGDataFromDataCache(query);
			if(marginRate.get(0).get(0)!=null && marginRate.get(0).get(0)!="" && marginRate.size()>0){
				String baseRateType=marginRate.get(0).get(0);
				String baseRate=marginRate.get(0).get(1);
				String ProdprefRate=marginRate.get(0).get(2);
				RLOS.mLogger.info("List is:"+marginRate);
				String netRate=formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate");
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_BAseRate", baseRate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_BaseRateType", baseRateType);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate", ProdprefRate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",netRate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_MArginRate", Float.parseFloat(netRate)-Float.parseFloat(baseRate)-Float.parseFloat(ProdprefRate));
			}
		}	
		//RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1->MarginRate:"+MarginRate);
		List objInput=new ArrayList();
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:CIF_ID");
		RLOS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
		formObject.getDataFromStoredProcedure("ng_RLOS_CASProductDedupeCheck", objInput);
		RLOS.mLogger.info("ng_RLOS_CASProductDedupeCheck Procedure Executed!!");
	}*/

		//Arun (01/12/17) modified below code of control name of listed reason & selected reason
		//changed by akshay on 5/12/17
		//Modified by prabhakar drop-4point-3
		else if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			String text=formObject.getNGItemText("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason"));
			//RLOS.mLogger.info( "Inside FATCA_Button1 text is "+text);
			//RLOS.mLogger.info( "Inside FATCA_Button1--->"+formObject.getNGValue("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason")));
			String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
			//RLOS.mLogger.info("hiddenText is: "+hiddenText);
			Map<String,String> ReasonMap =getFatcaReasons();
			if(hiddenText.endsWith(",")|| hiddenText.equalsIgnoreCase(""))
			{
				hiddenText=hiddenText+ReasonMap.get(text)+",";
			}
			else
			{
				hiddenText=hiddenText+","+ReasonMap.get(text)+",";
			}
			formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
			//RLOS.mLogger.info( "Inside FATCA_Button1 hidden text is after Concat: "+hiddenText);

			formObject.addComboItem("cmplx_FATCA_SelectedReason",text, ReasonMap.get(text));
			try {
				formObject.removeItem("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_ListedReason", -1);

			}catch (Exception e) {

				RLOS.logException(e);
			}

		}
		//Modified by prabhakar drop-4point-3
		else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			try {
				//RLOS.mLogger.info( "Inside FATCA_Button2 ");
				Map<String,String> ReasonMap =getFatcaReasons();
				String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
				//RLOS.mLogger.info( "Inside FATCA_Button1 hidden text is "+hiddenText);
				String hiddenTextRemove=ReasonMap.get(formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
				//RLOS.mLogger.info( "hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length(), hiddenText.length() "+hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length()+","+hiddenText.length());
				if((hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length())<hiddenText.length() && hiddenText.charAt(hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length())==','){
					hiddenText=hiddenText.replace(hiddenTextRemove+"," , "");
				}
				else{
					hiddenText=hiddenText.replace(hiddenTextRemove , "");
				}
				formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
				//RLOS.mLogger.info( "Inside FATCA_Button1 hidden text is after deletion: "+hiddenText);
				formObject.addComboItem("cmplx_FATCA_ListedReason",formObject.getNGItemText("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason")),hiddenTextRemove);

				formObject.removeItem("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_SelectedReason", -1);
			} catch (Exception e) {

				RLOS.logException(e);
			}
		}
		//++below code added by nikhil for Self-Supp CR
		else if("CardDetails_Self_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			String Selected_Cards=formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product");
			if(formObject.getSelectedIndex("CardDetails_Avl_Card_Product")==-1)
			{
				throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL063")));
			}
			else
			{
				String Curr_Card=formObject.getNGValue("CardDetails_Avl_Card_Product");
				if(Selected_Cards.contains(Curr_Card))
				{
					throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL064")));
				}
				if("".equalsIgnoreCase(Selected_Cards))
				{
					Selected_Cards=Curr_Card;					
				}
				else
				{
					Selected_Cards+=","+Curr_Card;					
				}
				formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", Selected_Cards);
				formObject.addItem("CardDetails_Sel_Card_Product", Curr_Card);
				formObject.setSelectedIndex("CardDetails_Avl_Card_Product", -1);
			}
		}
		else if("CardDetails_Self_remove".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			String Selected_Cards=formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product");
			if(formObject.getSelectedIndex("CardDetails_Sel_Card_Product")==-1)
			{
				throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL063")));
			}
			else
			{
				String Curr_Card=formObject.getNGValue("CardDetails_Sel_Card_Product");
				try {
					RLOS.mLogger.info("selected card::"+Selected_Cards);
					RLOS.mLogger.info("Curr_Card card::"+Curr_Card);
					formObject.removeItem("CardDetails_Sel_Card_Product", formObject.getSelectedIndex("CardDetails_Sel_Card_Product"));
					//RLOS.mLogger.info("item removed::");
					Selected_Cards=Selected_Cards.replace(Curr_Card, "");
					//	RLOS.mLogger.info("Selected_Cards::"+Selected_Cards);
					Selected_Cards=Selected_Cards.replace(",,",",");
					if(",".equalsIgnoreCase(Selected_Cards))
					{
						Selected_Cards="";
					}
					RLOS.mLogger.info("Selected_Cards::"+Selected_Cards);
					formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", Selected_Cards);
					formObject.setSelectedIndex("CardDetails_Sel_Card_Product", -1);

				} catch (Exception e) {
					RLOS.mLogger.info("Exception in remove"+e.getMessage());

				}

			}
		}
		//--above code added by nikhil for Self-Supp CR
		//added by prabhakar drop-4 point-3
		else if("cmplx_FATCA_cmplx_GR_FatcaDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			try{
				Map<String,String> ReasonMap =getFatcaReasons();
				List<String> selectedReasonList= new ArrayList<String>();
				//String selectedreasons="";
				for (String reason : ReasonMap.values()) {
					selectedReasonList.add(reason);
				}
				//RLOS.mLogger.info("Inside FatcaGrid Click");
				//RLOS.mLogger.info("Index is"+formObject.getSelectedIndex("cmplx_FATCA_cmplx_GR_FatcaDetails"));
				String hiddenText="";
				if(formObject.getSelectedIndex("cmplx_FATCA_cmplx_GR_FatcaDetails")>-1){
					hiddenText =formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails", formObject.getSelectedIndex("cmplx_FATCA_cmplx_GR_FatcaDetails"), 11);
					//RLOS.mLogger.info("HiddenField is: "+hiddenText);
					if(hiddenText.equalsIgnoreCase("NA")){
						hiddenText="";
					}
					if(hiddenText!=null && !"".equalsIgnoreCase(hiddenText) && !"NA".equals(hiddenText)){
						List<String> listedReasonList=Arrays.asList(hiddenText.split(","));	
						if(formObject.getItemCount("cmplx_FATCA_SelectedReason")==0)
						{
							formObject.clear("cmplx_FATCA_listedreason");
							for (String string : listedReasonList)
							{
								selectedReasonList.remove(string);
								//RLOS.mLogger.info("cmplx_FATCA_SelectedReason"+selectedReasonList);
								formObject.addComboItem("cmplx_FATCA_SelectedReason",getKeyByValue(ReasonMap, string), string);

							}
							for (String string : selectedReasonList)
							{
								//RLOS.mLogger.info("cmplx_FATCA_ListedReason"+selectedReasonList);
								formObject.addComboItem("cmplx_FATCA_ListedReason", getKeyByValue(ReasonMap, string),string);
								//formObject.addi
							}
						}
						//formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", selectedreasons);
					}
				}
				else {
					//RLOS.mLogger.info("Inside de-select FATCA row");
					int itemCount = formObject.getItemCount("cmplx_FATCA_SelectedReason");
					//RLOS.mLogger.info("itemCount: "+itemCount);
					for(int i=0;i<itemCount;i++){
						try {
							formObject.removeItem("cmplx_FATCA_SelectedReason", (itemCount-1)-i);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//RLOS.mLogger.info("Exception in removing row from listbox: "+printException(e));
						}
					}
				}
				//RLOS.mLogger.info("HIDDEN VALUE IS"+hiddenText);
			}catch(Exception e){
				RLOS.mLogger.info("Exception in FATCA grid click: "+printException(e));	
			}
		}

		else if ("FATCA_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("FATCA_Wi_Name",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("FATCA_Wi_Name"));


			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FATCA_cmplx_GR_FatcaDetails");
			//added by prabhakar
			formObject.clear("cmplx_FATCA_listedreason");
			formObject.clear("cmplx_FATCA_SelectedReason");
			LoadPickList("cmplx_FATCA_ListedReason", "Select description,code from ng_master_fatcaReasons");


			//RLOS.mLogger.info("FATCA_Button3");
			formObject.setNGValue("cmplx_FATCA_SelectedReasonhidden", "");
		}
		//Added By Prabhakar Drop-4 point-3
		else if ("FATCA_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			try{

				//RLOS.mLogger.info( "Inside Fatca Modify>>>cmplx_FATCA_SelectedReasonhidden: "+formObject.getNGValue("cmplx_FATCA_SelectedReasonhidden"));
				String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
				hiddenText=hiddenText.replace("NA,", "");
				formObject.setNGValue("cmplx_FATCA_selectedreasonhidden",hiddenText);
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FATCA_cmplx_GR_FatcaDetails");
				formObject.clear("cmplx_FATCA_listedreason");
				formObject.clear("cmplx_FATCA_SelectedReason");
				LoadPickList("cmplx_FATCA_ListedReason", "Select description,code from ng_master_fatcaReasons");

				formObject.setNGValue("cmplx_FATCA_SelectedReasonhidden", "");
				//RLOS.mLogger.info( "Inside add FATCA_Modify: modify of FATCA details");
			}catch(Exception e){
				RLOS.mLogger.info("Exception in FATCA modify: "+printException(e));	
			}
		}
		//Added By Prabhakar Drop-4 point-3
		else if ("FATCA_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FATCA_cmplx_GR_FatcaDetails");
			formObject.clear("cmplx_FATCA_listedreason");
			formObject.clear("cmplx_FATCA_SelectedReason");
			LoadPickList("cmplx_FATCA_ListedReason", "Select description,code from ng_master_fatcaReasons");
			//RLOS.mLogger.info( "Inside FATCA_Delete: delete of FATCA details");
			formObject.setNGValue("cmplx_FATCA_SelectedReasonhidden", "");
		}
		//changed by akshay on 5/12/17


		//Arun (01/12/17) modified above code of control name of listed reason & selected reason


		else if ("IncomingDoc_AddFromPCButton".equalsIgnoreCase(pEvent.getSource().getName())){
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
			//RLOS.mLogger.info("value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));


		}    


		else if("Reject".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
			formObject.setSelectedSheet("ParentTab",8);		
			formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
			formObject.setNGFrameState("DecisionHistoryContainer", 0);
			formObject.setNGFrameState("DecisionHistoryContainer", 1);
			formObject.setNGFrameState("DecisionHistoryContainer", 0);
			formObject.setNGFocus("DecisionHistory_Button4");
		}


		else  if("Liability_New_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			hm.put("Liability_New_Button1","Clicked");
			popupFlag="N";


			//added

			outputResponse = GenXml.GenerateXML("AECB","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");
				formObject.setNGValue("IS_AECB","Y");
			}
			else{
				formObject.setNGValue("IS_AECB","Y");
			}
			//RLOS.mLogger.info(formObject.getNGValue("IS_AECB"));
			//ended
		}

		else  if("DecisionHistory_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS_IntegrationInput.getCustAddress_details();
			String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
			//String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
			String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			RLOS.mLogger.info( "inside create Account/Customer NTB value: "+CIF_no);
			if("Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0))){
				if("true".equalsIgnoreCase(NTB_flag) || "".equalsIgnoreCase(CIF_no)){
					if(!"Y".equals(formObject.getNGValue("Is_Customer_Req")))
					{
						formObject.setNGValue("curr_user_name",formObject.getUserName());
						outputResponse = GenXml.GenerateXML("NEW_CUSTOMER_REQ","PRIMARY_CIF");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						//RLOS.mLogger.info(ReturnCode);
						if("0000".equalsIgnoreCase(ReturnCode)){
							RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
							formObject.setNGValue("Is_Customer_Req","Y");
							formObject.setNGValue("cmplx_DecisionHistory_CifNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
							formObject.setNGValue("cmplx_Customer_CIFNO",formObject.getNGValue("cmplx_Customer_CIFNO"));
							RLOS.mLogger.info("cmplx_Customer_CIFNO--> " + formObject.getNGValue("cmplx_Customer_CIFNO"));
							//added by aman for drop4
							String CIf_value=outputResponse.contains("<CIFId>") ? outputResponse.substring(outputResponse.indexOf("<CIFId>")+"</CIFId>".length()-1,outputResponse.indexOf("</CIFId>")):"";
							formObject.setNGValue("cmplx_DecisionHistory_CifNo", CIf_value);
							formObject.setNGValue("cmplx_Customer_CIFNO",CIf_value);
							RLOS.mLogger.info("CIFID 1" + formObject.getNGValue("cmplx_Customer_CIFNO"));
							formObject.setNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 3, CIf_value);
							//below code by saurabh on 2nd Aug.
							//RLOS.mLogger.info("CIFID 2" + CIf_value);
							//formObject.setNGValue("cmplx_Customer_CIFNO",CIf_value);

							formObject.setLocked("DecisionHistory_Button2", true);
							//added by aman for drop4
							//RLOS.mLogger.info("Inside if of New customer Req");
							outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							//RLOS.mLogger.info(ReturnCode);

							if("0000".equalsIgnoreCase(ReturnCode) ){
								RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");  

								formObject.setNGValue("Is_Account_Create","Y");
								formObject.setNGValue("EligibilityStatus","Y");
								formObject.setNGValue("EligibilityStatusCode","Y");
								formObject.setNGValue("EligibilityStatusDesc","Y");
								formObject.setLocked("DecisionHistory_Button3", true);    //added by akshay on 31/1/18 
								alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL055");

							}
							else{
								formObject.setNGValue("Is_Account_Create","N");
								//RLOS.mLogger.info("New account Req for NTB failed");
								alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL054");

							}
							//RLOS.mLogger.info(formObject.getNGValue("Is_Account_Create"));
							//RLOS.mLogger.info(formObject.getNGValue("EligibilityStatus"));
							//RLOS.mLogger.info(formObject.getNGValue("EligibilityStatusCode"));
							//RLOS.mLogger.info(formObject.getNGValue("EligibilityStatusDesc"));

						}
						else{
							//RLOS.mLogger.info("New Customer Req for NTB failed");
							alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL053");
						}
					}
					else if("Y".equals(formObject.getNGValue("Is_Customer_Req")) && !"Y".equals(formObject.getNGValue("Is_Account_Create")))
					{
						outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						//RLOS.mLogger.info(ReturnCode);

						if("0000".equalsIgnoreCase(ReturnCode) ){
							RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
							formObject.setNGValue("Is_Account_Create","Y");
							formObject.setNGValue("EligibilityStatus","Y");
							formObject.setNGValue("EligibilityStatusCode","Y");
							formObject.setNGValue("EligibilityStatusDesc","Y");
							formObject.setLocked("DecisionHistory_Button3", true);    //added by akshay on 31/1/18 
							alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL048");
						}
						else{
							formObject.setNGValue("Is_Account_Create","N");
							//RLOS.mLogger.info("New account Req for NTB failed");
							alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL054");
						}
					}
				}
				else
				{
					//RLOS.mLogger.info("customer is an existing customer!!");

					/*String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> AccountCount= formObject.getNGDataFromDataCache(query);
					RLOS.mLogger.info( "Query is: "+query+" currValue In AccountCount is "+AccountCount);
					RLOS.mLogger.info( "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"))----commented by akshay on 15/5/18*/;
					int countCurrentAccount =  Integer.parseInt(formObject.getNGValue("AccountCount"));

					if((!"Y".equals(formObject.getNGValue("Is_Account_Create")) && "false".equals(formObject.getNGValue("cmplx_Customer_NTB")) && countCurrentAccount==0)){
						//RLOS.mLogger.info("NTB: "+formObject.getNGValue("cmplx_Customer_NTB")+" Account Count:"+formObject.getNGValue("AccountCount"));

						outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						//RLOS.mLogger.info(ReturnCode);
						if("0000".equalsIgnoreCase(ReturnCode) ){
							RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
							formObject.setNGValue("Is_Account_Create","Y");
							alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL048");
							formObject.setLocked("DecisionHistory_Button3", true);    //added by akshay on 31/1/18 
						}
						else{
							formObject.setNGValue("Is_Account_Create","N");
							alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL054");
						}
						//RLOS.mLogger.info(formObject.getNGValue("Is_Account_Create"));
						if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Account_Create"))){ 
							//RLOS.mLogger.info("inside if condition");
							formObject.setEnabled("DecisionHistory_Button5", false);     
						}
						else{
							formObject.setEnabled("DecisionHistory_Button5", true);
						}
					}


				}
				Custom_fragmentSave("CustomerDetails");
				Custom_fragmentSave("DecisionHistoryContainer");
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));//changed by aman for drop4
			}
			else if("Guarantor".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0))){
				//RLOS.mLogger.info("Select row data is: "+formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0));
				outputResponse = GenXml.GenerateXML("NEW_CUSTOMER_REQ","GUARANTOR_CIF");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				//RLOS.mLogger.info( "Guarantor CIf ReturnCode: "+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode))
				{
					//RLOS.mLogger.info("PL DDVT Checker"+ "Guarantor CIf created sucessfully!!!");
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL056");
					String CIf_value=outputResponse.contains("<CIFId>") ? outputResponse.substring(outputResponse.indexOf("<CIFId>")+"</CIFId>".length()-1,outputResponse.indexOf("</CIFId>")):"";
					formObject.setNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 3, CIf_value);

					formObject.setVisible("GuarantorDetails", true);
					int framestate1=formObject.getNGFrameState("GuarantorDetails");
					if(framestate1 == 0){
					} else {
						formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
						formObject.setNGFrameState("GuarantorDetails", 0);
					}
					formObject.setNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0, 1, CIf_value);

					formObject.setLocked("DecisionHistory_Button2", true);
					RLOS.mLogger.info("DecisionHistoryContainer "+ "Guarantor CIf created sucessfully!!!");

					Custom_fragmentSave("DecisionHistoryContainer");
					RLOS.mLogger.info("GuarantorDetails "+ "Guarantor CIf created sucessfully!!!");

					Custom_fragmentSave("GuarantorDetails");
					RLOS.mLogger.info("GuarantorDetails "+ "Guarantor CIf created sucessfully!!!");

					formObject.RaiseEvent("WFSave");
					RLOS.mLogger.info("WFSave "+ "Guarantor CIf created sucessfully!!!");

				}
				else{

					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL019");
				}
				throw new ValidatorException(new FacesMessage(alert_msg));

			}


		}


		else   if("ReadFromCIF".equalsIgnoreCase(pEvent.getSource().getName())){
			if(formObject.getNGValue("cif")==null || formObject.getNGValue("cif")=="" || formObject.getNGValue("cif").equalsIgnoreCase(NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_DummyCIF"))) {
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Guarantor_CIF");
				ReturnCode = getTagValue(outputResponse, "ReturnCode");  
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					parse_cif_eligibility(outputResponse,"Guarantor_CIF");					
					formObject.setNGValue("Is_Customer_Eligibility_Guarantor","Y");
					//RLOS.mLogger.info("Check Guarator CIF"+formObject.getNGValue("cif"));

					if(formObject.getNGValue("cif")=="" || formObject.getNGValue("cif").equalsIgnoreCase(NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_DummyCIF"))){
						setGuarantor_enable();
						try
						{
							RLOS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
							formObject.RaiseEvent("WFSave");
							throw new ValidatorException(new FacesMessage("Guarantor is a New to Bank Customer."));
						}
						finally{
							hm.clear();
						}

					}else {
						outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						//RLOS.mLogger.info(ReturnCode);
						if("0000".equalsIgnoreCase(ReturnCode)){
							formObject.setNGValue("Is_Customer_Details_Guarantor","Y");
							formObject.setLocked("ReadFromCIF",true);
							openDemographicTabs();
							RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Guarantor_CIF");    
							formObject.setNGValue("passExpiry", Convert_dateFormat(formObject.getNGValue("passExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
							formObject.setNGValue("dob", Convert_dateFormat(formObject.getNGValue("dob"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
							formObject.setNGValue("eidExpiry", Convert_dateFormat(formObject.getNGValue("eidExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
							formObject.setNGValue("visaExpiry", Convert_dateFormat(formObject.getNGValue("visaExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
							//code added by saurabh on 22nd July 17.
							if(formObject.getNGValue("dob")!=null && !"".equalsIgnoreCase(formObject.getNGValue("dob")) && !" ".equalsIgnoreCase(formObject.getNGValue("dob"))){
								common.getAge(formObject.getNGValue("dob"), "age_gua");	
							}								
							formObject.RaiseEvent("WFSave");							

						} else {
							formObject.setNGValue("Is_Customer_Details_Guarantor","N");
							throw new ValidatorException(new FacesMessage("Error in fetch Customer details operation."));

						}
					}

				} else {
					formObject.setNGValue("Is_Customer_Eligibility_Guarantor","N");
				}
			}
			else {
				outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				//RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					formObject.setNGValue("Is_Customer_Details_Guarantor","Y");
					formObject.setLocked("ReadFromCIF",true);
					//RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_Guarantor"));
					openDemographicTabs();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Guarantor_CIF");    
					formObject.setNGValue("passExpiry", Convert_dateFormat(formObject.getNGValue("passExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					formObject.setNGValue("dob", Convert_dateFormat(formObject.getNGValue("dob"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					formObject.setNGValue("eidExpiry", Convert_dateFormat(formObject.getNGValue("eidExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					formObject.setNGValue("visaExpiry", Convert_dateFormat(formObject.getNGValue("visaExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					//code added by saurabh on 22nd July 17.
					if(formObject.getNGValue("dob")!=null && !"".equalsIgnoreCase(formObject.getNGValue("dob")) && !" ".equalsIgnoreCase(formObject.getNGValue("dob"))){
						common.getAge(formObject.getNGValue("dob"), "age_gua");	
					}
					formObject.RaiseEvent("WFSave");

					throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));
				}
				else{
					//RLOS.mLogger.info("Customer Details is not generated");
					formObject.setNGValue("Is_Customer_Details_Guarantor","N");
					throw new ValidatorException(new FacesMessage("Error in fetch Guarantor details operation."));

				}
			}
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility_Guarantor")) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details_Guarantor")))
			{ 
				RLOS.mLogger.info("RLOS value of Guarator Details"+"inside if condition");
				formObject.setEnabled("ReadFromCIF", false); 
				throw new ValidatorException(new FacesMessage("Customer information fetched sucessfully"));
			}
			else{
				formObject.setEnabled("ReadFromCIF", true);
				throw new ValidatorException(new FacesMessage("Error in fetch Guarantor details operation."));

			}
			//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_Guarantor"));
		}
		//Added for the World check call in case of self employed  
		else if ("FetchWorldCheck_SE".equalsIgnoreCase(pEvent.getSource().getName())) {
			popupFlag="Y";		
			//	columnValues=columnValues.join(",",columnValues_arr);

			//RLOS.mLogger.info("inside worldcheck"); 

			outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				//RLOS.mLogger.info("inside if of WORLDCHECK");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				//Deepak 16/07/2019 changes done to clear Worldcheck grid before adding new data. PCAS-2154
				formObject.clear("cmplx_WorldCheck_WorldCheck_Grid");
				formObject.setVisible("WorldCheck", false);
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"");	
				formObject.setNGValue("IS_WORLD_CHECK","Y");
				alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL015");
				//below code added by nikhuil for PCSP-416
				formObject.saveFragment("WorldCheck");
			}
			else if("9999".equalsIgnoreCase(ReturnCode)){
				alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL016");
				formObject.setNGValue("IS_WORLD_CHECK","YES");
			}
			else{
				formObject.setNGValue("IS_WORLD_CHECK","N");
				//RLOS.mLogger.info("inside else of WORLD CHECK");
				alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL017");
			}
			//RLOS.mLogger.info("alert: "+ alert_msg);
			/*try{
				Thread.sleep(1000);}catch(Exception e){

				}
				formObject.RaiseEvent("WFSave");*/
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//Added for the World check call in case of self employed

		// added by Akshay for world_check on initiation  
		else if ("WorldCheck_fetch".equalsIgnoreCase(pEvent.getSource().getName())) {
			popupFlag="Y";
			//	columnValues=columnValues.join(",",columnValues_arr);
			//RLOS.mLogger.info("inside worldcheck"); 
			outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				//RLOS.mLogger.info("inside if of WORLDCHECK");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");
				//Deepak 16/07/2019 changes done to clear Worldcheck grid before adding new data. PCAS-2154
				formObject.clear("cmplx_WorldCheck_WorldCheck_Grid");
				formObject.setVisible("WorldCheck", false);
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"");	
				formObject.setNGValue("IS_WORLD_CHECK","Y");
				alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL015");
				//below code added by nikhil for PCSP-387
				Custom_fragmentSave("WorldCheck");
			}
			else if("9999".equalsIgnoreCase(ReturnCode)){
				alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL016");
				formObject.setNGValue("IS_WORLD_CHECK","YES");
			}
			else{
				formObject.setNGValue("IS_WORLD_CHECK","N");
				//RLOS.mLogger.info("inside else of WORLD CHECK");
				alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL017");
			}
			//RLOS.mLogger.info("alert: "+ alert_msg);
			/*try{
					Thread.sleep(1000);}catch(Exception e){

					}
				formObject.RaiseEvent("WFSave");*/
			//changes done to set focus on the same button.
			formObject.setNGFocus("WorldCheck_fetch");
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		// ended Akshay for world_check initiation


		else if("ELigibiltyAndProductInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{		
			RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1 click:: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getNGValue("lbl_user_name_val"));
			popupFlag="Y";
			formObject.setNGValue("DecCallFired","Eligibility");
			promorate(); //hritik
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)))
			{
				if(getnoactivecards()==0)
				{
					alert_msg="Customer Does not have a active card, Not applicable for IM!";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}

			// position of EMI logic shifted to make it run for 1st time
			try{
				//below condition added by akshay on 23/9/18 forproc 12824
				if("IM".equals(formObject.getNGValue("Subproduct_productGrid")))
				{
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					int Mono_day = Integer.parseInt(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"));
					//String First_emi_Date = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate");				
					/*	Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DATE,30);
						Date date = cal.getTime();
						String First_emi_Date= new SimpleDateFormat("dd-MM-yyyy").format(date);*/
					String EMI=getEMI(LoanAmount,RateofInt,tenor);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI",  EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
					RLOS.mLogger.info("Post EMI value set: "+ formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
				}
				if("Personal Loan".equalsIgnoreCase(RequiredProd))
				{
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					int Mono_day = Integer.parseInt(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"));

					String First_emi_Date = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate");				
					String EMI=getEMI_new(LoanAmount,RateofInt,tenor,First_emi_Date);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI",  EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
					RLOS.mLogger.info("Post EMI value set: "+ formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
				}
			}
			catch(Exception e){
				RLOS.logException(e);
				//RLOS.mLogger.info(" Exception in EMI Generation");
			}
			if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){

				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
					/*try{Thread.sleep(1000);}catch(Exception e){}
						formObject.RaiseEvent("WFSave");*/
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
				}

			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
				}

			}


			//EMI Calcuation logic added below 24-Sept-2017 End
			//deepak Code changes to calculate LPF amount and % 08-nov-2017 start
			try{
				if("Personal Loan".equalsIgnoreCase(RequiredProd))
				{
					RLOS.mLogger.info("RLOS_Common"+"Inside set event of LPF Data");
					double LPF_charge = 0;
					double Insur_charge = 0;
					//query modified by saurabh on 13th Mar
					List<List<String>> result=formObject.getNGDataFromDataCache("select distinct c.CHARGERATE,I.Insur_chargeRate from NG_MASTER_Charges c with (nolock) left join NG_master_Scheme S with (nolock) on c.ChargeID=S.LPF_ChargeID  left join NG_MASTER_InsuranceCharges I with (nolock) on I.Insur_chargeId= S.Insur_chargeID where S.SCHEMEID='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'");
					RLOS.mLogger.info("RLOS_Common"+ "result of fetch RMname query: "+result); 
					if(result==null ||  result.isEmpty()){//result.equals("") || removed in code Optimization 25-07-2018
						LPF_charge=1;
						Insur_charge=1;
					}
					else{
						if(result.get(0).get(0)!=null && !"".equals(result.get(0).get(0))){
							LPF_charge = Double.parseDouble(result.get(0).get(0));
						}
						if(result.get(0).get(1)!=null && !"".equals(result.get(0).get(1))){
							Insur_charge = Double.parseDouble(result.get(0).get(1));
						}
					}
					//RLOS.mLogger.info("RLOS_Common code "+ "result LPF_charge: "+LPF_charge);
					//RLOS.mLogger.info("RLOS_Common code "+ "result Insur_charge: "+Insur_charge);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPF", LPF_charge);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Insurance", Insur_charge);
					//formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
					//double LPF_amount;
					double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));// For Proc-8201
					//LPF_amount = (final_Loan_amount*LPF_charge)/100;
					//below code by saurabh on 14th June for JIRA - 11017
					//LPF_amount = ((double)(Math.round((LPF_amount*100)))/100);//round to 2 decimal places.
					//RLOS.mLogger.info("RLOS_Common code "+ "result LPF_amount: "+LPF_amount);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",false);
					//added b y akshay on 15/3/18 for proc 6396
					double Insuranceamount=(Insur_charge*final_Loan_amount)/100;
					//below code by saurabh on 14th June for JIRA - 11017
					Insuranceamount = ((double)(Math.round((Insuranceamount*100)))/100);//round to 2 decimal places.
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount", Insuranceamount);
					formObject.setEnabled("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
					String qry = "select VATDESC,VATRATE from  ng_MASTER_LoanVAT with(nolock) union SELECT distinct mincap,maxcap FROM NG_MASTER_Charges with(nolock) WHERE CHARGEDESC='Loan Processing Fee' union select SCHEMEDESC,MAXAMTFIN from NG_master_Scheme with(nolock) where SCHEMEID='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
					RLOS.mLogger.info("query to load VAT and Max Loan AMount is"+qry +"");
					List<List<String>> record1 = formObject.getNGDataFromDataCache(qry);
					RLOS.mLogger.info("Result is: "+record1);
					if( record1.get(0)!=null && !record1.isEmpty()){

						for(int i=0;i<record1.size();i++){
							RLOS.mLogger.info("Value of record.get(i).get(0) "+record1.get(i).get(0));
							if("Loan Processing VAT".equalsIgnoreCase(record1.get(i).get(0))){
								int loanProcPercent=Integer.parseInt(record1.get(i).get(1));
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_LoanProcessingVATPercent", loanProcPercent);
							}
							else if("Insurance VAT".equalsIgnoreCase(record1.get(i).get(0))){
								int insurancePercent=Integer.parseInt(record1.get(i).get(1));
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceVATPercent", insurancePercent);
							}
							/*else if(isNumeric(record1.get(i).get(0))){
								double LoanDetails_MaxLPF=Double.parseDouble(record1.get(i).get(1));
								double LoanDetails_MinLPF=Double.parseDouble(record1.get(i).get(0));
								if(LPF_amount> LoanDetails_MaxLPF)
									LPF_amount= LoanDetails_MaxLPF;
								else if (LPF_amount<LoanDetails_MinLPF)
									LPF_amount= LoanDetails_MinLPF;

							}*/
						}
					}

					//PCASI - 3647
					double LPF_amount_new = 0.01 * final_Loan_amount;
					if(LPF_amount_new < 500)
						LPF_amount_new = 500;
					else if(LPF_amount_new > 2500)
						LPF_amount_new = 2500;
					RLOS.mLogger.info("setting LPF_amount new in RLOS ::"+LPF_amount_new);

					//formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPFAmount", LPF_amount);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPFAmount", LPF_amount_new);
					RLOS.mLogger.info("After setting value of LFP amount:: "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount"));


					float lpfVatAmt=0;
					float insuranceVatAmt=0;

					lpfVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LoanProcessingVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount")))/100f;
					insuranceVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount")))/100f;
					lpfVatAmt = ((float)(Math.round((lpfVatAmt*100)))/100);
					insuranceVatAmt = ((float)(Math.round((insuranceVatAmt*100)))/100);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_LoanProcessingVat", lpfVatAmt);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceVat", insuranceVatAmt);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_LoanProcessingVat", true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceVat", true);
					formObject.RaiseEvent("WFSave");
				}

			}
			catch(Exception e){
				RLOS.logException(e);
				//RLOS.mLogger.info(" Exception in EMI Generation");
			}
			// Hritik 20.6.21 PCASI 3258
			RLOS.mLogger.info("PCASI 3258 ");

			String Apptype="";
			//String alert_msg="";
			String query1="";
			String query2="";
			int rowcount = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			RLOS.mLogger.info("Row count"+rowcount);
			if(rowcount>0){
				Apptype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
			}
			RLOS.mLogger.info("Application Type : "+Apptype);

			if (Apptype.equalsIgnoreCase("TOPE") ||Apptype.equalsIgnoreCase("TOPIM") ||Apptype.equalsIgnoreCase("TOPN") )
			{
				query1="select isnull(Settlement_Flag,'') as settle from ng_RLOS_CUSTEXPOSE_LoanDetails where Wi_Name ='"+formObject.getWFWorkitemName()+"' UNION select isnull(Settlement_Flag,'') from ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name ='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> Settleflag = formObject.getDataFromDataSource(query1);
				RLOS.mLogger.info("query1:: "+Settleflag);
				RLOS.mLogger.info("settleflag:: "+Settleflag);
				try{
					if(Settleflag.size()>0)
					{
						String var_settleflagalert="Y";
						for(int i=0;i<Settleflag.size();i++)
						{
							RLOS.mLogger.info("Settleflag : "+Settleflag.get(i).get(0));
							if("true".equalsIgnoreCase(Settleflag.get(i).get(0))){
								RLOS.mLogger.info("Settleflag : "+Settleflag.get(i).get(0));
								var_settleflagalert="N";
								RLOS.mLogger.info(" inside var_settleflagaler:: "+var_settleflagalert);
							}
						}
						RLOS.mLogger.info("var_settleflagaler:: "+var_settleflagalert);

						if(var_settleflagalert.equalsIgnoreCase("Y"))
						{
							alert_msg="settlement flag to be ticked";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						RLOS.mLogger.info("settleflag:: "+alert_msg);
					}
				}
				catch(Exception e){
					RLOS.logException(e);
				}
				calculateNetPayout(formObject);
			}
			else if (Apptype.equalsIgnoreCase("TKON") || Apptype.equalsIgnoreCase("TKOE")){
				query2="select isnull(Take_Over_Indicator,'')as Take_Over_indi from ng_rlos_cust_extexpo_LoanDetails where Wi_Name ='"+formObject.getWFWorkitemName()+"' UNION select isnull(Take_Over_Indicator,'') from ng_rlos_cust_extexpo_CardDetails where Wi_Name ='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> takeoverindicator = formObject.getDataFromDataSource(query2);
				RLOS.mLogger.info("query2:: "+takeoverindicator);
				RLOS.mLogger.info("takeoverindicator:: "+takeoverindicator);
				try{
					if(takeoverindicator.size()>0)
					{
						String var_takeover="Y";
						for(int i=0;i<takeoverindicator.size();i++){
							RLOS.mLogger.info("takeover : "+takeoverindicator.get(i).get(0));
							if("true".equalsIgnoreCase(takeoverindicator.get(i).get(0))){
								var_takeover="N";
							}
						}
						RLOS.mLogger.info("var_takeover:: "+var_takeover);
						if(var_takeover.equalsIgnoreCase("Y"))
						{
							alert_msg="Take over Indicator to be ticked";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						RLOS.mLogger.info("takeover::: "+alert_msg);
					}
				}
				catch(Exception e)
				{
					RLOS.logException(e);
				}
			}


			IslamicFieldsvisibility();
			//deepak Code changes to calculate LPF amount and % 08-nov-2017 End
			//change to set focus on the same button.
			/*try{
				Thread.sleep(1000);}
				catch(Exception e){}
				formObject.RaiseEvent("WFSave");*/
			//++below code added by nikhil for Self-Supp CR
			Refresh_self_supp_data();
			formObject.setNGValue("cmplx_CardDetails_SelfSupp_required", "--Select--");
			//--above code added by nikhil for Self-Supp CR
			formObject.setNGFocus("ELigibiltyAndProductInfo_Button1");
			//RLOS.mLogger.info(" Final alert msg for front end at ELigibiltyAndProductInfo_Button1 click event: "+alert_msg );
			throw new ValidatorException(new FacesMessage(alert_msg));
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

		}
		else if("Eligibility_Emp".equalsIgnoreCase(pEvent.getSource().getName()))
		{	
			RLOS.mLogger.info("Inside Eligibility_Emp click:: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getNGValue("lbl_user_name_val"));
			popupFlag="Y";
			RLOS.mLogger.info("@life"+formObject.getNGValue("cmplx_EmploymentDetails_RegPay"));
			//below code added by nikhil to Run Pipeline Check

			fetchPipeline();

			formObject.setNGValue("DecCallFired","Eligibility");

			String Employment_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			//Not required for PersonalLoan
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)) && ("Salaried Pensioner".equals(Employment_type) || "Salaried".equals(Employment_type) || "Pensioner".equals(Employment_type)))
			{
				formObject.setNGValue("Is_Principal_Approval", "Y");
			}
			else
			{
				formObject.setNGValue("Is_Principal_Approval", "N");
			}

			/*if("SAL".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid")))
					{
						formObject.setNGValue("Is_Principal_Approval", "Y");
					}
					else
					{
						formObject.setNGValue("Is_Principal_Approval", "N");
					}*/
			Acct_num_visibility();
			//chnages done by shivang for 2.1 started
			Set_score_range();
			//chnages done by shivang for 2.1 end
			if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
				aecbAndReferenceDetail();
			}
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);



			//Change Done to fetch Income Details
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//	ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					//RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					//formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					//String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					////RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
					//formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);//it is dere at fragment load
					fetch_EligPrd_frag();

					//formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					//formObject.setNGFrameState("EligibilityAndProductInformation", 0);

					//Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
					checkforBPACase();
					/*try{
						Thread.sleep(1000);}catch(Exception e){}
						formObject.RaiseEvent("WFSave");*/
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");

				}
			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(outputResponse);

				if(("".equalsIgnoreCase(SystemErrorCode))&& !"0".equalsIgnoreCase(ReturnCode)){
					//RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					//formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					//String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					//formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
					fetch_EligPrd_frag();
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					//Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
					/*try{
							Thread.sleep(1000);}catch(Exception e){}
						formObject.RaiseEvent("WFSave");*/

				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");

				}

			}
			//change to set focus on the same button.
			try{
				if("Personal Loan".equalsIgnoreCase(RequiredProd))
				{//by shweta
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3)==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

					int Mono_day = Integer.parseInt(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"));

					String First_emi_Date = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate");				
					String EMI=getEMI_new(LoanAmount,RateofInt,tenor,First_emi_Date);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
				}

			}
			catch(Exception e){
				RLOS.mLogger.info("Exception Occured in RLOS Iniitation :  Exception in EMI Generation");
			}
			//EMI Calcuation logic added below 06-nov-2017 End

			formObject.setNGFocus("Customer_Check");
			//change by saurabh on 29th nov 2017 as elig can only be run once.
			//++below code added by nikhil for Self-Supp CR
			Refresh_self_supp_data();
			formObject.setNGValue("cmplx_CardDetails_SelfSupp_required", "--Select--");
			//--above code added by nikhil for Self-Supp CR
			//formObject.setEnabled("Eligibility_Emp",false);
			formObject.RaiseEvent("WFSave");				
			alert_msg="Principal Approval : "+formObject.getNGValue("Is_Principal_Approval")+" || " +alert_msg;
			throw new ValidatorException(new FacesMessage(alert_msg));
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

		}
		//Added for Dectech Call on Self Employed Case
		else if("CheckEligibility_SE".equalsIgnoreCase(pEvent.getSource().getName()))
		{	
			RLOS.mLogger.info("Inside CheckEligibility_SE click:: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getNGValue("lbl_user_name_val"));
			popupFlag="Y";
			formObject.setNGValue("DecCallFired","Eligibility");
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
			if(formObject.isVisible("CompanyDetails_Frame1") ==false && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equals(formObject.getNGValue("EmploymentType"))){
				formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			}	
			//formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			//formObject.setTop("AuthorisedSignatoryDetails", formObject.getTop("CompanyDetails")+formObject.getHeight("CompanyDetails")+25);
			//formObject.setTop("PartnerDetails", formObject.getTop("AuthorisedSignatoryDetails")+formObject.getHeight("AuthorisedSignatoryDetails")+25);
			//Change Done to fetch Income Details

			//Change Done to fetch Income Details
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					//RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					//formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					fetch_EligPrd_frag();
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
					if (!(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor") != null && formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")!=""))		
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);

					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);

					Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
					/*try{Thread.sleep(1000);}catch(Exception e){}
						formObject.RaiseEvent("WFSave");*/



				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");

				}
			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					//RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					if (!(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor") != null && formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")!=""))		
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					//Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
					/*try{Thread.sleep(1000);}catch(Exception e){}
						formObject.RaiseEvent("WFSave");*/



				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");

				}

			}
			//change to set focus on the same button.
			formObject.setNGFocus("CheckEligibility_SE");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);


			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

		}
		//Added for Dectech Call on Self Employed Case
		else if("Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{	
			RLOS.mLogger.info("Inside Button2 click:: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getNGValue("lbl_user_name_val"));
			popupFlag="Y";
			//code added by nikhil for PCAS-2422
			if(formObject.getNGValue("cmplx_Customer_NTB").equals("false")){
				List<String> objInput=new ArrayList<String>();
				List<Object> objOutput=new ArrayList<Object>();
				objInput.add("Text:" + formObject.getWFWorkitemName());

				int Prod_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				String SubProd="";
				String AppType="";
				if(Prod_rowCount>0){
					SubProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
					AppType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
				}    
				if((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPN").equalsIgnoreCase(AppType))||(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPE").equalsIgnoreCase(AppType))||
						(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_LimitIncrease_code").equalsIgnoreCase(SubProd))||
						(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM").equalsIgnoreCase(SubProd)&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM_Additional").equalsIgnoreCase(AppType)||NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM_TopUp").equalsIgnoreCase(AppType)))){
					RLOS.mLogger.info("RLOS Comman: Product dedupe will not run as SubProd: "+SubProd+" AppType: "+AppType);
				}
				else{

					objOutput.add("Text");
					//RLOS.mLogger.info("objInput args is: " + formObject.getWFWorkitemName());

					objOutput = formObject.getDataFromStoredProcedure("ng_RLOS_DedupeCheck", objInput, objOutput);
					RLOS.mLogger.info("objOutput args is: " + (String) objOutput.get(0));
					if(!"IM".equalsIgnoreCase(SubProd))
					{
						formObject.setNGValue("dedupecheck", (String) objOutput.get(0));
					}
					if (formObject.getNGValue("dedupecheck").equalsIgnoreCase("Y")){
						throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL057")));
					}
				}

			}

			formObject.setNGValue("DecCallFired","CalculateDBR");

			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);
				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
					/*try{Thread.sleep(1000);}catch(Exception e){}
						formObject.RaiseEvent("WFSave");*/
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
				}
			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				//RLOS.mLogger.info(SystemErrorCode);
				//RLOS.mLogger.info(ReturnCode);
				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
					//RLOS.mLogger.info("after value set customer for dectech call");
					//formObject.RaiseEvent("WFSave");
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
				}

			}
			//below code added by nikhil for PCSP-513
			CustomSaveForm();
			//below code change to set focus and use single through.  
			formObject.setNGFocus("Button2");
			throw new ValidatorException(new FacesMessage(alert_msg));


			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);


			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);




		}



		else if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("MobileNo",formObject.getNGValue("cmplx_Customer_MobNo"));
			formObject.setNGValue("EmirateID", formObject.getNGValue("cmplx_Customer_EmiratesID"));
			formObject.setNGValue("PassportNo", formObject.getNGValue("cmplx_Customer_PAssportNo"));
			formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNo"));
			formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));
			RLOS.mLogger.info( "Inside Customer_save button: ");
			//PCASI - 3444
			String sRMTLName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			RLOS.mLogger.info("sRMTLName :: "+sRMTLName);
			if(!(sRMTLName.equalsIgnoreCase(null) || sRMTLName.isEmpty())){
				formObject.setNGValue("RM_Name", sRMTLName);
				formObject.setNGValue("lbl_TL_Name_val", sRMTLName);
				RLOS.mLogger.info("RMTLName :: "+formObject.getNGValue("RM_Name")+ " RmTlNameLabel :: "+formObject.getNGValue("RmTlNameLabel"));
			}

			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_MiddleName")))
			{
				//below id changed
				formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}

			else{
				//below id changed
				formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
			//Deepak 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CustomerDetails");
			/*String return_Arr[] = formObject.saveFragment("CustomerDetails");
				if(return_Arr.length>0 && !"0".equalsIgnoreCase(return_Arr[0])){
					throw new ValidatorException(new FacesMessage("Invalid session"));
				}	
			 */				
			popupFlag = "Y";
			//changed by nikhil to save employer search data ReferTo
			Custom_fragmentSave("EmploymentDetails");
			//++below code added by nikhil for PCAS-1212 CR
			Update_Office_Address();
			//--above code added by nikhil for PCAS-1212 CR
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL020");
			formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName()))
		{			
			formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
			formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Subproduct_productGrid"));//pcasp-2373 
			formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));
			formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("ProductDetailsLoader");
			/*try{Thread.sleep(1000);}catch(Exception e){}
				formObject.RaiseEvent("WFSave");*/
			//below line added by nikhil for PCSP-13
			formObject.RaiseEvent("WFSave");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL021");
			throw new ValidatorException(new FacesMessage(alert_msg));

		}


		else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("GuarantorDetails");
			Custom_fragmentSave("Alt_Contact_container");
			Custom_fragmentSave("FATCA_container");
			Custom_fragmentSave("KYC_container");
			Custom_fragmentSave("OECD_container");

			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL022");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Incomedetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL023");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("IncomeDetails_SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Incomedetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL024");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("CompanyDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info("Company Details---->"+formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));
			formObject.setNGValue("COmpany_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
			formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));
			formObject.setNGValue("Tl_No", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,6));//added By Akshay on 16/9/17 to set value i ext table column
			formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("COmpany_Name"));
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CompanyDetails");
			Custom_fragmentSave("AuthorisedSignDetails");
			Custom_fragmentSave("PartnerDetails");
			popupFlag = "Y";
			Update_Office_Address();
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL025");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			Product_DedupeCheck();
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Liability_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL026");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		//PCASI - 2694 - start
		if("TLRM_Button".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info("Inside TLRM Button click event");
			String query;
			query="select description,code from NG_MASTER_RM_TL with (nolock)  where isActive='Y'";
			RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"cmplx_Customer_RM_TL_NAME", "Description,Code", true, 20,"RMTLName");			     

		}
		//PCASI - 2694 - end
		//PCASI - 2694
		else if("TLRM_Button".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info("Inside change event for RM TL Name");
			String rmtlname = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			if(rmtlname != ""){
				formObject.setNGValue("RM_Name", rmtlname);
				formObject.setNGValue("lbl_TL_Name_val", rmtlname);
			}
			RLOS.mLogger.info( "RM_Name :: "+formObject.getNGValue("RM_Name"));
		}


		//below code by saurabh for new Incoming Doc.
		else if("IncomingDocNew_Addbtn".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setNGValue("IncomingDocNew_winame", formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomingDocNew_IncomingDocGrid");
			//below code added by nikhil for PCASP-39
			formObject.setNGValue("IncomingDocNew_Username",formObject.getUserName());
			formObject.setNGValue("IncomingDocNew_Workstep",formObject.getWFActivityName());
			//to set expiry date by default nikhil 30/1
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			//formObject.setNGValue("IncomingDocNew_ExpiryDate",modifiedDate,false); pcasi3667
		}
		else if("IncomingDocNew_Modifybtn".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setNGValue("IncomingDocNew_winame", formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomingDocNew_IncomingDocGrid");
			//below code added by nikhil for PCASP-39
			formObject.setNGValue("IncomingDocNew_Username",formObject.getUserName());
			formObject.setNGValue("IncomingDocNew_Workstep",formObject.getWFActivityName());
			//to set expiry date by default nikhil 30/1
			RLOS.mLogger.info("rlos modify btn inc doc");
			RLOS.mLogger.info("formObject.isLocked(\"IncomingDocNew_DocType\")"+formObject.isLocked("IncomingDocNew_DocType"));
			RLOS.mLogger.info("formObject.isLocked(\"IncomingDocNew_DocName\")"+formObject.isLocked("IncomingDocNew_DocName"));
			//changed by nikhil for PCSP-742
			formObject.setLocked("IncomingDocNew_DocType", false);				
			formObject.setLocked("IncomingDocNew_DocName", false);
			//to set expiry date by default nikhil 30/1
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			//formObject.setNGValue("IncomingDocNew_ExpiryDate",modifiedDate,false);
		}
		else if("cmplx_IncomingDocNew_IncomingDocGrid".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getSelectedIndex("cmplx_IncomingDocNew_IncomingDocGrid")==-1)
			{
				//below code added by nikhil for PCASP-39
				formObject.setNGValue("IncomingDocNew_Username",formObject.getUserName());
				formObject.setNGValue("IncomingDocNew_Workstep",formObject.getWFActivityName());
				//to set expiry date by default nikhil 30/1
				//to set expiry date by default nikhil 30/1
				//Date date = new Date();
				//	String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				//	formObject.setNGValue("IncomingDocNew_ExpiryDate",modifiedDate,false);
			}
		}


		else if("EMploymentDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Added by aman for PCSP-147
			if (formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("CAC")){
				String query = "select ((select count(*) from ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName='"+formObject.getWFWorkitemName()+"' and CAC_Indicator='true') + (select count(*) from ng_rlos_cust_extexpo_CardDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' and CAC_Indicator='true'))";
				List<List<String>> result = formObject.getDataFromDataSource(query);
				if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
				{RLOS.mLogger.info("document name is locked"+result.get(0).get(0));

				if (result.get(0).get(0).equalsIgnoreCase("0")){
					RLOS.mLogger.info("Inside 0 name is locked");

					alert_msg="Please select CAC Indicator from manual Liability or External Liability";
				}

				else{
					RLOS.mLogger.info("Inside else name is locked");

					formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By akshay on 16/9/17 to set header
					formObject.setNGValue("COmpany_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//for PCASP-2538
					formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
					//Added by aman for CR 0712
					formObject.setNGValue("employercode",formObject.getNGValue("cmplx_EmploymentDetails_EMpCode"));
					//Added by aman for CR 0712
					//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
					Custom_fragmentSave("EmploymentDetails");
					popupFlag = "Y";
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL027");
				}
				}
			}
			else{
				formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By akshay on 16/9/17 to set header
				formObject.setNGValue("COmpany_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//for PCASP-2538
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				//Added by aman for CR 0712
				formObject.setNGValue("employercode",formObject.getNGValue("cmplx_EmploymentDetails_EMpCode"));
				//Added by aman for CR 0712
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("EmploymentDetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL027");

			}
			//++below code added by nikhil for PCAS-1212 CR
			Update_Office_Address();
			//--above code added by nikhil for PCAS-1212 CR
			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		else if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("Final_Limit"));
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("EligibilityAndProductInformation");
			popupFlag = "Y";
			enrollrewardValid();
			AirArabiaValid();
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL028");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else 	if("MiscellaneousFields_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("MiscFields");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL029");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Address_Details_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL030");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("ContactDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Alt_Contact_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL031");
			//commented by nikhil for wrong code. 31/10
			//formObject.setNGValue("BRANCH", formObject.getNGValue("AlternateContactDetails_custdomicile"));
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else 	if("CardDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CardDetails_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL032");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("SupplementCardDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Supplementary_Container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL033");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//formObject.setsel ("cmplx_FATCA_ListedReason");
			formObject.setSelectedAllIndices("cmplx_FATCA_SelectedReason");

			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("FATCA_container");


			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL034");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		//Added by Prabhakar drop-4 point-3
		else if("KYC_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("kyc_Wi_Name",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside KYC add button: "+formObject.getNGValue("kyc_Wi_Name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_KYC_cmplx_KYCGrid");
		}
		//Added by Prabhakar drop-4 point-3
		else if("KYC_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_KYC_cmplx_KYCGrid");
			//RLOS.mLogger.info("Inside KYC Modify Button"+formObject.getNGValue("kyc_Wi_Name"));
		}
		//Added by Prabhakar drop-4 point-3
		else if("KYC_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_KYC_cmplx_KYCGrid");
			//RLOS.mLogger.info("Inside KYC Modify Button"+formObject.getNGValue("kyc_Wi_Name"));
		}
		//Modified by Prabhakar drop-4 point-3
		else if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info( "Inside KYC save button");
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("KYC_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL035");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("OECD_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL036");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("ReferenceDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("ReferenceDetails_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL037");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("ServiceRequest_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL038");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("BTC_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL039");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("CC_Loan_DDS_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL040");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("CC_Loan_SI_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL041");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("CC_Loan_RVC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL042");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("IncomingDocNew_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info( "TANSHU Inside IncomingDoc_Save button!!");
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("IncomingDocuments");
			popupFlag = "Y";
			//IncomingDoc();
			//RLOS.mLogger.info( "TANSHU Inside IncomingDoc_Save button!! after incoming doc function");
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL043");
			/*try{Thread.sleep(1000);}catch(Exception e){}
				formObject.RaiseEvent("WFSave");// added by Aman to use the product save because custom save is not working
			 */				throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("DecisionHistory_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("DecisionHistoryContainer");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL044");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if ("OECD_Button1".equalsIgnoreCase(pEvent.getSource().getName())){					

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}

		else if ("OECD_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if ("OECD_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}



		//added by yash for RLOS FSD
		else if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Notepad_Values");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL045");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		// added by abhishek as per rlos FSD 


		else if("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("NotepadDetails_WiNote",formObject.getWFWorkitemName());
			//RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("NotepadDetails_WiNote"));					
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_add();
		}
		else if("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			int rowindex = formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid");
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_modify(rowindex);
		}
		else if("NotepadDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_delete();
		}

		// ended by abhishek as per rlos FSD
		else if("cmplx_NotepadDetails_cmplx_notegrid".equalsIgnoreCase(pEvent.getSource().getName())){
			Notepad_grid();

		} 

		else if ("Button_State".equalsIgnoreCase(pEvent.getSource().getName())){
			String query;
			String filter_value="";
			filter_value = formObject.getNGValue("state");
			if(!"".equalsIgnoreCase(filter_value)){
				query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";	
			}
			else{
				query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y'";
			}


			//RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"state", "Description,Code", true, 20,"City/State");			     

		}
		else if ("Button_City".equalsIgnoreCase(pEvent.getSource().getName())){
			String query;
			String filter_value="";
			filter_value = formObject.getNGValue("city");
			if(!"".equalsIgnoreCase(filter_value)){
				query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";	
			}
			else{
				query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y'";
			}


			//RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"city", "Description,Code", true, 20,"City/State");			     

		}
		/*	----commented by akshay on 19/4/18----code added at last---not working---proc 8407
		 * else if ("cmplx_DecisionHistory_MultipleApplicantsGrid".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "Appliocant is is is:"+formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0));
			RLOS.mLogger.info( "Appliocant is is is1:"+formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 1));
			RLOS.mLogger.info( "Appliocant is is is1:"+formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 2));
			RLOS.mLogger.info( "Appliocant getSelectedIndex is is:"+formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"));
			if("Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0))){
				String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where (AcctType = 'CURRENT ACCOUNT' or AcctType = 'AMAL CURRENT ACCOUNT') AND Wi_Name='"+formObject.getWFWorkitemName()+"'";

				try{
					List<List<String>> AccountCount= formObject.getNGDataFromDataCache(query);
					RLOS.mLogger.info( "Query is: "+query+" Value In AccountCount is "+AccountCount);
					RLOS.mLogger.info( "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
					if(AccountCount.size()>0){
					formObject.setNGValue("AccountCount", AccountCount.get(0).get(0));
					if(("true".equals(formObject.getNGValue("cmplx_Customer_NTB")) || "0".equals(AccountCount.get(0).get(0))) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equals(formObject.getNGValue("Product_Type"))){
						RLOS.mLogger.info(" Product Type Is: "+formObject.getNGValue("Product_Type"));
						//formObject.setVisible("DecisionHistory_Button3",true);
						if("Y".equalsIgnoreCase(formObject.getNGValue("Is_Account_Create"))){
							formObject.setLocked("DecisionHistory_Button3", true);
						}
					}

					else
					{
						if (formObject.getNGValue("Product_Type").equalsIgnoreCase(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard"))){
							formObject.setVisible("cmplx_DecisionHistory_AccountNumber",false );
							formObject.setVisible("cmplx_DecisionHistory_IBAN",false );
							formObject.setVisible("DecisionHistory_Label3",false );
							formObject.setVisible("AccountNum",false );

						}
						else {
							formObject.setVisible("cmplx_DecisionHistory_AccountNumber",true );
							formObject.setVisible("cmplx_DecisionHistory_IBAN",true );
							formObject.setVisible("DecisionHistory_Label3",true );
							formObject.setVisible("AccountNum",true );
							formObject.setLocked("cmplx_DecisionHistory_AccountNumber",true);

						}
						formObject.setVisible("DecisionHistory_Button3",false);//create CIF Acc
					}
					}
				}
				catch(Exception e)
				{
					RLOS.mLogger.info( "Exception occurred in Decision History tab:"+e.getMessage()+printException(e));

				}
			}


			else if("Guarantor".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0))){
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 3))){
					formObject.setVisible("DecisionHistory_Button3",true);//create CIF Acc
				}
				}
			else {
				formObject.setVisible("DecisionHistory_Button3",false);//create CIF Acc

			}
		}*/

		else if ("ButtonOECD_State".equalsIgnoreCase(pEvent.getSource().getName())){
			String query;
			String filter_value="";
			filter_value = formObject.getNGValue("OECD_townBirth");
			if(!"".equalsIgnoreCase(filter_value)){
				query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";	
			}
			else{
				query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y'";
			}

			//RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"OECD_townBirth", "Description,Code", true, 20,"City/State");			     

		}
		else if ("Customer_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			String query;
			String filter_value="";
			filter_value = formObject.getNGValue("Customer_Button2");
			if(!"".equalsIgnoreCase(filter_value)){
				query="select UserId , UserId from NG_MASTER_SourceCode with (nolock) and UserId like'%"  + filter_value + "%'";	
			}
			query="select UserId , UserId from NG_MASTER_SourceCode with (nolock)";
			RLOS.mLogger.info( "query is for dsa code button: "+query);
			populatePickListWindow(query,"Customer_Button2", "UserId,UserId", true, 20,"DSA");			     

		}//by alok
		else if ("Sourcing_Branch_Code_button".equalsIgnoreCase(pEvent.getSource().getName())){
			String query;
			String filter_value="";
			filter_value = formObject.getNGValue("Sourcing_Branch_Code_button");
			if(!"".equalsIgnoreCase(filter_value)){
				query="select code,description from ng_master_source_branch_code with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";	
			}
			query="select code,description from ng_master_source_branch_code with (nolock)";
			RLOS.mLogger.info( "query is for source_branch_code button: "+query);
			populatePickListWindow(query,"Sourcing_Branch_Code_button", "code,description", true, 20,"Source Branch");			     

		}//by Alok

		else if ("CardDispatchToButton".equalsIgnoreCase(pEvent.getSource().getName())
				||"SecNationality_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"Third_Nationality_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"BirthCountry_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"ResidentCountry_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"AddressDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName())
				||"Nationality_ButtonPartMatch".equalsIgnoreCase(pEvent.getSource().getName())
				||"MOL_Nationality_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"CardDetails_bankName_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"EmploymentDetails_Bank_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"DesignationAsPerVisa_button".equalsIgnoreCase(pEvent.getSource().getName())
				||"FreeZone_Button".equalsIgnoreCase(pEvent.getSource().getName())
				||"Designation_button1".equalsIgnoreCase(pEvent.getSource().getName())
				||"Sourcing_Branch_Code_button".equalsIgnoreCase(pEvent.getSource().getName())
				||"Customer_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			//Removed for Sonar
			pickListMasterLoad(pEvent.getSource().getName());

		}
		//below code added by akshay on 19/4/18
		else if("cmplx_DecisionHistory_MultipleApplicantsGrid".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info( "Inside mouse click for  cmplx_DecisionHistory_MultipleApplicantsGrid--->Selected row is"+formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"));

			if(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0).equals("Guarantor"))
			{
				if((formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),3).equals(NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_DummyCIF")) || formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),3).equals("")) && !formObject.getNGValue("cmplx_DecisionHistory_Decision").equalsIgnoreCase("reject")){
					formObject.setVisible("DecisionHistory_Button3",true);
				}
				else{
					formObject.setVisible("DecisionHistory_Button3",false);
				}
			}
			else{
				try{

					if(("true".equals(formObject.getNGValue("cmplx_Customer_NTB")) || "0".equals(formObject.getNGValue("AccountCount"))) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equals(formObject.getNGValue("Product_Type")) && !formObject.getNGValue("cmplx_DecisionHistory_Decision").equalsIgnoreCase("reject")){
						formObject.setVisible("DecisionHistory_Button3",true);
						if("Y".equalsIgnoreCase(formObject.getNGValue("Is_Account_Create"))){
							formObject.setLocked("DecisionHistory_Button3", true);
						}
					}
					else{
						formObject.setVisible("DecisionHistory_Button3",false);
					}
				}
				catch(Exception e)
				{
					RLOS.mLogger.info( "Exception occurred in Decision History tab:"+e.getMessage()+printException(e));
				}
			}
		}
		else if("Customer_Check".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject = FormContext.getCurrentInstance().getFormReference();
			RLOS.mLogger.info("Inside FetchDetails click:: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
			formObject.setNGValue("cmplx_Customer_IscustomerSave", "N");//added by akshay on 19/3/18
			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
			String cif_no = formObject.getNGValue("cmplx_Customer_CIFNO");

			if("".equalsIgnoreCase(cif_no)){ 
				RLOS.mLogger.info(cif_no+"test for cifff");
				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				String ReadFrom_card_exc = formObject.getNGValue("cmplx_Customer_readfromcardflag");
				if( EID!=null && !"".equalsIgnoreCase(EID)&& "Y".equalsIgnoreCase(ReadFrom_card_exc))
				{							
					outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Primary_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					if("0000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("Is_Entity_Details","Y");
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");

					}
					else{
						formObject.setNGValue("Is_Entity_Details","N");

					}
					//RLOS.mLogger.info(formObject.getNGValue("Is_Entity_Details"));							
				}
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				ReturnCode = getTagValue(outputResponse, "ReturnCode");  
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					setcustomer_enable();
					popupFlag="Y";
					//Deepak below line commented as Customer eligibility called before Parse Cif Eligibility 26Dec2017 
					//Deepak below line again uncommented to set NTB 27Dec2017
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");
					parse_cif_eligibility(outputResponse,"Primary_CIF");
					RLOS.mLogger.info("return code if block for empty cif");
					String cif_noI = formObject.getNGValue("cmplx_Customer_CIFNO");
					if("".equalsIgnoreCase(cif_noI))
					{
						formObject.setNGValue("NTB","true");
						RLOS.mLogger.info(cif_noI+"cif"+formObject.getNGValue("NTB"));//added by Akshay
					}

					RLOS.mLogger.info("return code if block for empty cif after setting ntb true"+formObject.getNGValue("NTB"));
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					//Customer_enable();
					// if(NTB_flag.equalsIgnoreCase("true")){
					//  Customer_enable();

					//}
					if("false".equalsIgnoreCase(NTB_flag)){
						// parse_cif_eligibility(outputResponse,"Primary_CIF");
						RLOS.mLogger.info("if ntb is false");
						//Code change to run Customer details if customer is existing customer in Customer Eligibility start (27-sept-2017)
						String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
						if(cif!=null && !"".equalsIgnoreCase(cif)){
							//changed for PCSP-488
							alert_msg =  fetch_cust_details_primary();
							if(!"Telesales_Init".equalsIgnoreCase(formObject.getWFActivityName()) && !"Paperbased_Init".equalsIgnoreCase(formObject.getWFActivityName()))
							{
								formObject.setVisible("Customer_Frame2", true);
							}
							formObject.setLocked("cmplx_Customer_MobNo", true);
							if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)) && isCustomerMinor(formObject)){
								MinorFieldVisibility(formObject);
								formObject.setTop("GuarantorDetails",formObject.getTop("CustomerDetails")+formObject.getHeight("CustomerDetails")+10);
								formObject.setTop("Incomedetails",formObject.getTop("GuarantorDetails")+formObject.getHeight("GuarantorDetails")+10);
							}else {
								formObject.setHeight("Customer_Frame1", 790);
								formObject.setHeight("CustomerDetails", 800);
								formObject.setTop("Incomedetails", 1290);

							}
							//done for CR PCS-2251
							if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")))
							{
								formObject.setNGValue("cmplx_Customer_Employer_code", formObject.getNGValue("cmplx_EmploymentDetails_EMpCode"));
								formObject.setNGValue("cmplx_Customer_Employer_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
							}
							Customer_Bscore(cif,formObject);
							/*formObject.setHeight("Customer_Frame1", 790);*/
						}
						//Code change to run Customer details if customer is existing customer in Customer Eligibility End (27-sept-2017)
						//Deepak changes check supplementary coming for Primary Card PCAS-1911
						/*if(supplementry_to_PrimaryCheck()){
							alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL066");
						}*/
					}   

					else if("true".equalsIgnoreCase(NTB_flag)){
						formObject.setVisible("Customer_Frame2", false);
						RLOS.mLogger.info("if NTB is true");
						boolean framestate=formObject.isVisible("IncomeDetails_Frame1");

						if(!framestate)
						{
							formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
							//adjustFrameTops("GuarantorDetails,Incomedetails");
							//formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
							RLOSCommonCode.visibilityFrameIncomeDetails(formObject);
						}

						if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)) && isCustomerMinor(formObject)){
							MinorFieldVisibility(formObject);
							formObject.setTop("GuarantorDetails",formObject.getTop("CustomerDetails")+formObject.getHeight("CustomerDetails")+10);
							formObject.setTop("Incomedetails",formObject.getTop("GuarantorDetails")+formObject.getHeight("GuarantorDetails")+10);
						} else {
							formObject.setHeight("Customer_Frame1", 790);
							formObject.setHeight("CustomerDetails", 800);
							formObject.setTop("Incomedetails", 1290);
						}

						/*formObject.setEnabled("cmplx_Customer_VIsaExpiry", true);
						formObject.setLocked("cmplx_Customer_VisaNo", false);
						formObject.setLocked("IncomeDetails_FinacialSummarySelf", true);
						formObject.setNGValue("cmplx_Customer_CIFNO","");//added by akshay for proc 11596
						 */
						//RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");

						//throw new ValidatorException(new FacesMessage(alert_msg));
						//added By Akshay
					}

					formObject.addItem("Customer_Type", "zyxxxxx");
					formObject.addItem("cmplx_FATCA_CustomerType", "zyxxxxx");
					formObject.addItem("KYC_CustomerType", "zyxxxxx");
					formObject.addItem("OECD_CustomerType", "zyxxxxx");



					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility")) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details")))
					{ 

						formObject.setEnabled("FetchDetails", false); 
						formObject.setEnabled("Customer_Button1", false);

					}
					else{
						;
						formObject.setEnabled("FetchDetails", true);
						formObject.setEnabled("Customer_Button1", false);
					}


					popupFlag="Y";

					loadDynamicPickList();
					formObject.setLocked("cmplx_Customer_NEP", false);
					formObject.setLocked("cmplx_Customer_Employer_name", false);
					formObject.setLocked("cmplx_Customer_Employer_code", false);
					formObject.setLocked("Customer_search", false);
					formObject.setLocked("Customer_CheckBox7", false);
					formObject.setLocked("SecNationality_Button", false);
					formObject.setLocked("Third_Nationality_Button", false);
					formObject.setLocked("cmplx_Customer_DLNo", false);
					formObject.setLocked("cmplx_Customer_Passport2", false);
					formObject.setLocked("cmplx_Customer_Passport3", false);
					formObject.setLocked("cmplx_Customer_PAssport4", false);
					if(!"Self Employed".equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
					{
						formObject.setLocked("Eligibility_Check", false);
					}
					formObject.setLocked("Customer_Check", true);

				}

				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					popupFlag="Y";
					//formObject.setNGValue("Java_success_return","N");
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL005");
					loadDynamicPickList();
					throw new ValidatorException(new FacesMessage(alert_msg));

				}
			}
			else
			{
				RLOS.mLogger.info("else of not null CIF");


				alert_msg = fetch_cust_details_primary();
				String NegatedFlag;
				popupFlag="Y";
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				//RLOS.mLogger.info("Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				//RLOS.mLogger.info(ReturnCode);
				formObject.setNGValue("Is_Customer_Eligibility","Y");


				if("0000".equalsIgnoreCase("0000")){
					setcustomer_enable();
					popupFlag="Y";
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"Primary_CIF"); 
					parse_cif_eligibility(outputResponse,"Primary_CIF");
					BlacklistFlag= (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
					//RLOS.mLogger.info("Customer is BlacklistedFlag"+BlacklistFlag);
					DuplicationFlag= (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
					//RLOS.mLogger.info("Customer is DuplicationFlag"+DuplicationFlag);
					NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
					//RLOS.mLogger.info("Customer is NegatedFlag"+NegatedFlag);
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
					formObject.setNGValue("BlacklistFlag",BlacklistFlag);
					formObject.setNGValue("DuplicationFlag",DuplicationFlag);
					formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					if("true".equalsIgnoreCase(NTB_flag)){
						//RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						formObject.setNGValue("cmplx_Customer_CIFNO","");//added by akshay for proc 11596
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");
					}
					else{

						String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
						if(cif!=null && !"".equalsIgnoreCase(cif)){
							//changed for PCSP-488
							alert_msg =  fetch_cust_details_primary();
							if(!"Telesales_Init".equalsIgnoreCase(formObject.getWFActivityName()) && !"Paperbased_Init".equalsIgnoreCase(formObject.getWFActivityName()))
							{
								formObject.setVisible("Customer_Frame2", true);
							}
							formObject.setLocked("cmplx_Customer_MobNo", true);
							if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)) && isCustomerMinor(formObject)){
								MinorFieldVisibility(formObject);
								formObject.setTop("GuarantorDetails",formObject.getTop("CustomerDetails")+formObject.getHeight("CustomerDetails")+10);
								formObject.setTop("Incomedetails",formObject.getTop("GuarantorDetails")+formObject.getHeight("GuarantorDetails")+10);
							}else {
								formObject.setHeight("Customer_Frame1", 790);
								formObject.setHeight("CustomerDetails", 800);
								formObject.setTop("Incomedetails", 1290);

							}
							//done for CR PCS-2251
							if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")))
							{
								formObject.setNGValue("cmplx_Customer_Employer_code", formObject.getNGValue("cmplx_EmploymentDetails_EMpCode"));
								formObject.setNGValue("cmplx_Customer_Employer_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
							}
							Customer_Bscore(cif,formObject);
							/*formObject.setHeight("Customer_Frame1", 790);*/
						}

						/*formObject.setVisible("Customer_Frame2", true);
						formObject.setTop("Incomedetails", 1290);
						formObject.setHeight("Customer_Frame1", 790);
						formObject.setHeight("CustomerDetails", 800);
						if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")))
						{
							formObject.setNGValue("cmplx_Customer_Employer_code", formObject.getNGValue("cmplx_EmploymentDetails_EMpCode"));
							formObject.setNGValue("cmplx_Customer_Employer_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
						}
						if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")))
						{
							formObject.setNGValue("cmplx_Customer_Employer_code", formObject.getNGValue("cmplx_EmploymentDetails_EMpCode"));
							formObject.setNGValue("cmplx_Customer_Employer_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
						}
						formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700);
						//change by saurabh on 7may
						formObject.setTop("ProductDetailsLoader",formObject.getTop("CustomerDetails")+formObject.getHeight("CustomerDetails"));
						formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL008");*/
						formObject.addItem("Customer_Type", "zyxxxxx");
						formObject.addItem("cmplx_FATCA_CustomerType", "zyxxxxx");
						formObject.addItem("KYC_CustomerType", "zyxxxxx");
						formObject.addItem("OECD_CustomerType", "zyxxxxx");



						if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility")) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details")))
						{ 

							formObject.setEnabled("FetchDetails", false); 
							formObject.setEnabled("Customer_Button1", false);

						}
						else{
							;
							formObject.setEnabled("FetchDetails", true);
							formObject.setEnabled("Customer_Button1", false);
						}


						popupFlag="Y";

						loadDynamicPickList();
						formObject.setLocked("cmplx_Customer_NEP", false);
						formObject.setLocked("cmplx_Customer_Employer_name", false);
						formObject.setLocked("cmplx_Customer_Employer_code", false);
						formObject.setLocked("Customer_search", false);
						formObject.setLocked("Customer_CheckBox7", false);
						formObject.setLocked("SecNationality_Button", false);
						formObject.setLocked("Third_Nationality_Button", false);
						formObject.setLocked("cmplx_Customer_DLNo", false);
						formObject.setLocked("cmplx_Customer_Passport2", false);
						formObject.setLocked("cmplx_Customer_Passport3", false);
						formObject.setLocked("cmplx_Customer_PAssport4", false);
						if(!"Self Employed".equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
						{
							formObject.setLocked("Eligibility_Check", false);
						}
						formObject.setLocked("Customer_Check", true);
					}


				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL005");

				}
				//RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
				//RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
				//RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
				//RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));
				//try{Thread.sleep(1000);}catch(Exception e){}
				formObject.RaiseEvent("WFSave");
				loadDynamicPickList();
				throw new ValidatorException(new FacesMessage(alert_msg));


			}
			//Deepak changes check supplementary coming for Primary Card PCAS-3524
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO"))){
				if(supplementry_to_PrimaryCheck()){
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL066");
				}
			}
			//Added By Shivang
			RLOS.mLogger.info("@Shivang :: Nationality :: "+formObject.getNGValue("cmplx_Customer_Nationality"));
			if(formObject.getNGValue("cmplx_Customer_Nationality").equalsIgnoreCase("AE")){
				formObject.setLocked("cmplx_Customer_VisaNo",true);
				formObject.setLocked("cmplx_Customer_VisaIssuedate",true);
				formObject.setLocked("cmplx_Customer_VIsaExpiry",true);
			}

			formObject.RaiseEvent("WFSave");	

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("Eligibility_Check".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("Eligibility_Check", true);
		}
		//Deepak Changes for IM APP form new fields 06-July-22
		else if("cmplx_customerConsents_Consent_for_Release_of_Statement_account".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(("true").equalsIgnoreCase(formObject.getNGValue("cmplx_customerConsents_Consent_for_Release_of_Statement_account"))){
				formObject.setEnabled("cmplx_customerConsents_iban_number", true);
				formObject.setEnabled("cmplx_customerConsents_StatementPeriod", true);
			}
			else{
				formObject.setEnabled("cmplx_customerConsents_iban_number", false);
				formObject.setEnabled("cmplx_customerConsents_StatementPeriod", false);
				formObject.setNGValue("cmplx_customerConsents_iban_number", "");
				formObject.setNGValue("cmplx_customerConsents_StatementPeriod", "");
				
			}
			
		}
		//Deepak Changes for IM APP form new fields 06-July-22
		else if("cmplx_customerConsents_ContactMe".equalsIgnoreCase(pEvent.getSource().getName()))
		{ 
			if(("true").equalsIgnoreCase(formObject.getNGValue("cmplx_customerConsents_ContactMe"))){
				formObject.setEnabled("cmplx_customerConsents_Sms", true);
				formObject.setEnabled("cmplx_customerConsents_Email", true);
				formObject.setEnabled("cmplx_customerConsents_TelephoneCalls", true);
			}
			else{
				formObject.setEnabled("cmplx_customerConsents_Sms", false);
				formObject.setEnabled("cmplx_customerConsents_Email", false);
				formObject.setEnabled("cmplx_customerConsents_TelephoneCalls", false);
				formObject.setNGValue("cmplx_customerConsents_Sms", false);
				formObject.setNGValue("cmplx_customerConsents_Email", false);
				formObject.setNGValue("cmplx_customerConsents_TelephoneCalls", false);
			}
		}
		else if("CustomerConsent_save".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			Custom_fragmentSave("Customer_Consents_container");
			alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL075");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		
		else{
			//RLOS.mLogger.info( " No condition reached in mouse click event");
		}

	}

		/*          Function Header:

		 **********************************************************************************

         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


Date Modified                       : 6/08/2017              
Author                              : Akshay Gupta             
Description                         :To handle value change events        

		 ***********************************************************************************  */


		public void call_Value_Changed(ComponentEvent pEvent)
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			Common_Utils common=new Common_Utils(RLOS.mLogger);

			/*if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName()))
	{
		RLOS.mLogger.info("RLOS val ..!. Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
		common.getAge(formObject.setNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
	}
			// Code added to Load targetSegmentCode on basis of code
			 */	
			//change for refreshing incoming doc repeater.
			if("cmplx_EmploymentDetails_targetSegCode".equalsIgnoreCase(pEvent.getSource().getName()) || "cmplx_Customer_Nationality".equalsIgnoreCase(pEvent.getSource().getName())){
				fetchIncomingDocRepeaterNew();
			}
			
// Changes done by aman for PCSP-67
			 if ("contractType".equalsIgnoreCase(pEvent.getSource().getName())){
				

				String sQuery = "select app_type from ng_master_contract_type with (nolock) where code='" +  formObject.getNGValue("contractType") + "'";

				RLOS.mLogger.info("RLOS val change Value of dob is:"+sQuery);
				
				List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
				RLOS.mLogger.info("RLOS val change Vasdfsdflue of dob is:"+recordList.get(0).get(0));
				if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)) && recordList!=null)
				{
					formObject.setNGValue("Contract_App_Type",recordList.get(0).get(0));
							        

				}
			}
			 
			//below code Hritik JS -> JAVA migration for risk mgmnt: 21/03/2022
			 
			 if ("GuarantorDetails_nationality".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info("GuarantorDetails_nationality : ");
				 RLOS.mLogger.info(" GuarantorDetails_nationality : "+formObject.getNGValue("GuarantorDetails_nationality"));
				 if(formObject.getNGValue("GuarantorDetails_nationality").equalsIgnoreCase("AE"))
				 {
					 formObject.setEnabled("GuarantorDetails_VisaISsueDate", false);
					 formObject.setEnabled("visaExpiry", false);
				 }
				 else
				 {
					 formObject.setEnabled("GuarantorDetails_VisaISsueDate", true);
					 formObject.setEnabled("visaExpiry", true);
				 }
			 }
			 
			 if ("cmplx_EmploymentDetails_targetSegCode".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info("cmplx_EmploymentDetails_targetSegCode : ");
				 RLOS.mLogger.info(" cmplx_EmploymentDetails_targetSegCode : "+formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"));
				 if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("LIFSUR"))
				 {
					 formObject.setEnabled("cmplx_EmploymentDetails_Withinminwait", true);
					 formObject.setEnabled("cmplx_EmploymentDetails_NoPremPaid", true);
					 formObject.setEnabled("cmplx_EmploymentDetails_RegPay", true);
					 formObject.setEnabled("cmplx_EmploymentDetails_PremiumType", true);
					 formObject.setEnabled("cmplx_EmploymentDetails_lifeInsurance", true);
					 formObject.setEnabled("cmplx_EmploymentDetails_PremAmt", true);
				 }
				 else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("MOTSUR"))
				 {
					 formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance", true);
				 }
			 }
			 
			 if ("cmplx_EmploymentDetails_PremiumType".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info("cmplx_EmploymentDetails_PremiumType : ");
				 RLOS.mLogger.info(" cmplx_EmploymentDetails_PremiumType : "+formObject.getNGValue("cmplx_EmploymentDetails_PremiumType"));

				 if(formObject.getNGValue("cmplx_EmploymentDetails_PremiumType").equalsIgnoreCase("Half yearly") || formObject.getNGValue("cmplx_EmploymentDetails_PremiumType").equalsIgnoreCase("Annually"))
				 {
					 formObject.setEnabled("cmplx_EmploymentDetails_Withinminwait", true);
				 }
				 else
				 {
					 formObject.setEnabled("cmplx_EmploymentDetails_Withinminwait", false);
				 }
			 }
			 
			 if ("cmplx_Customer_NEP".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info("cmplx_Customer_NEP : ");
				 RLOS.mLogger.info(" cmplx_Customer_NEP : "+formObject.getNGValue("cmplx_Customer_NEP"));
				 String NEP = formObject.getNGValue("cmplx_Customer_NEP");
				 if(!NEP.equalsIgnoreCase(""))
				 {
					 formObject.setEnabled("FetchDetails", true);
					 formObject.setEnabled("Customer_Check", true);
					 formObject.setEnabled("ReadFromCard", false);
					 formObject.setEnabled("Validate_OTP_Btn", false);
					 formObject.setEnabled("cmplx_Customer_DOb", true);
					 formObject.setEnabled("cmplx_Customer_card_id_available", false);
					 formObject.setEnabled("cmplx_Customer_CardNotAvailable", false);
				 }
				 else
				 {
					 formObject.setEnabled("FetchDetails", false);
					 formObject.setEnabled("ReadFromCard", true);
					 formObject.setEnabled("cmplx_Customer_DOb", false);
					 formObject.setEnabled("cmplx_Customer_card_id_available", false);
					 formObject.setEnabled("cmplx_Customer_CardNotAvailable", true);
				 }
			 }
			 
			 if ("OECD_tinNo".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info(" OECD_tinNo : "+formObject.getNGValue("OECD_tinNo"));
				 String tin = formObject.getNGValue("OECD_tinNo");
				 if(!tin.isEmpty())
				 {
					 formObject.setEnabled("OECD_noTinReason", false); 
				 }
				 else
				 {
					 formObject.setEnabled("OECD_noTinReason", true); 
				 }
			 }
			 
			 if ("OECD_noTinReason".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info(" OECD_noTinReason : "+formObject.getNGValue("OECD_noTinReason"));
				 String tin_reas = formObject.getNGValue("OECD_noTinReason");
				 if(tin_reas.equalsIgnoreCase("") || tin_reas.equalsIgnoreCase("--Select--"))
				 {
					 formObject.setEnabled("OECD_tinNo", true);  
				 }
				 else 
				 {
					 formObject.setEnabled("OECD_tinNo", false);
				 }
			 }
			 
			 if ("cmplx_EligibilityAndProductInfo_InterestRate".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info(" cmplx_EligibilityAndProductInfo_InterestRate :");
				 formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", true);
				 formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			 }
			 
			 if ("cmplx_CardDetails_SelfSupp_required".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info(" cmplx_CardDetails_SelfSupp_required :"+formObject.getNGValue("cmplx_CardDetails_SelfSupp_required"));
				 if(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required").equalsIgnoreCase("Yes"))
				 {
					 formObject.setEnabled("CardDetails_Avl_Card_Product", true);
					 formObject.setEnabled("CardDetails_Sel_Card_Product", true);
					 formObject.setEnabled("CardDetails_Self_add", true);
					 formObject.setEnabled("CardDetails_Self_remove", true);
				 }
				 else
				 {
					 formObject.setEnabled("CardDetails_Avl_Card_Product", false);
					 formObject.setEnabled("CardDetails_Sel_Card_Product", false);
					 formObject.setEnabled("CardDetails_Self_add", false);
					 formObject.setEnabled("CardDetails_Self_remove", false);
				 }
			 }
			 
			 if ("FATCA_USRelaton".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 RLOS.mLogger.info(" FATCA_USRelaton :"+formObject.getNGValue("FATCA_USRelaton"));
				 if(formObject.getNGValue("FATCA_USRelaton").equalsIgnoreCase("R") || formObject.getNGValue("FATCA_USRelaton").equalsIgnoreCase("N"))
				 {
					 Fatca_Enable(formObject); 
				 }
			 }
			 
			 //end
			 
			 
			 
			 
			// Changes done by aman for PCSP-67
			if ("ApplicationCategory".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				//RLOS.mLogger.info("RLOS val change Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
				String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
				String appCategory=formObject.getNGValue("ApplicationCategory");
				String reqProd = formObject.getNGValue("PrimaryProduct");
				//SKLogger.writeLog("RLOS val change ", "Value of subproduct is:"+formObject.getNGValue("subproduct"));			
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProd)){
					if(appCategory!=null && "BAU".equalsIgnoreCase(appCategory)){
						LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
					}
					else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
						LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
					}
					else{
						LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");	
					}
				}
				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(reqProd)){
					if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
						LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
					}
					else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
						LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
					}
					else{
						LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
					}
				} //Arun added on 10/12/17 as the dropdown loading incorrectly in company details

				/*if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory))
				{
					LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
				}
				else if(appCategory!=null &&  "Surrogate".equalsIgnoreCase(appCategory))
				{
					LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
				}
				else
				{
					LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
				}*/

			}
			
			else if("cmplx_DecisionHistory_Decision".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				//RLOS.mLogger.info("sagarika @telesales1"+);			
				//uncommentd by nikhil for Decision reason code Loadpicklist 26/11/18
				String dec=formObject.getNGValue("cmplx_DecisionHistory_Decision");
				RLOS.mLogger.info("sagarika @telesales1"+dec);	
				formObject.setLocked("DecisionHistory_DecisionReasonCode", false);
				if("Customer Hold-Document requested".equalsIgnoreCase(dec))
				{
					dec="Document Required";
				}
				LoadPickList("DecisionHistory_DecisionReasonCode", "select description,code from (SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason  with(nolock) where workstep='Branch_Init' and decision='"+dec+"') as new  order by  case when code='' then code else description end");
				//formObject.setLocked("DecisionHistory_DecisionReasonCode", false);
			/*changes made by sagarika for CAS | | Addition in Decision Reason Master for reports [RAKBANK-Internal]- 22/10/2018

				LoadPickList("DecisionHistory_DecisionReasonCode", "select description,code from (SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason  with(nolock) where workstep='Branch Initiation' and decision='"+formObject.getNGValue("cmplx_DecisionHistory_Decision")+"') as new  order by  case when code='' then code else description end");
				
				
				if("Reschedulment".equalsIgnoreCase(formObject.getNGValue("initiationChannel")))
				{
					LoadPickList("DecisionHistory_DecisionReasonCode", "select description,code from (SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason where workstep='Branch Initiation' and Initiation_Type = '"+formObject.getNGValue("initiationChannel")+"' decision='"+formObject.getNGValue("cmplx_DecisionHistory_Decision")+"') as new  order by  case when code='' then code else description end");
				}
				*/											
			if("Customer Hold".equalsIgnoreCase(dec))
			{
				RLOS.mLogger.info("sagarika @telesales");
				formObject.setLocked("DecisionHistory_DecisionReasonCode", false);
				LoadPickList("DecisionHistory_DecisionReasonCode", "select description from (SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason  with(nolock) where workstep='Telesales_Init' and decision='"+dec+"') as new  order by  case when code='' then code else description end");
			}
			}
			// Code added to Load targetSegmentCode on basis of code

			//code added for LOB
			else if ("estbDate".equalsIgnoreCase(pEvent.getSource().getName())){
				//change by saurabh on 29th Nov 17 for Drop2 CR.
				//RLOS.mLogger.info( "Value of estbDate is:"+formObject.getNGValue("estbDate"));
				common.getAge(formObject.getNGValue("estbDate"),"lob");
			}
			//code added for LOB
			
			//beow code by saurabh for new incoming doc.
			//changed again on 4th Jan
			else if("IncomingDocNew_DocType".equalsIgnoreCase(pEvent.getSource().getName())) {
				String docType = formObject.getNGValue("IncomingDocNew_DocType");
				String requested_product;
				String requested_subproduct;
				String application_type;
				String product_type;
				product_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0);
				RLOS.mLogger.info(product_type);
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);
//ButtonOECD_State
				RLOS.mLogger.info(requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
				RLOS.mLogger.info(requested_subproduct);
				application_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
				RLOS.mLogger.info(application_type);
				RLOS.mLogger.info(requested_product);
				String query="";
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(requested_product)){
					
					query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='"+formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")+"' or Designation='All') ";
					RLOS.mLogger.info( "when row count is  zero inside if"+query);
					LoadPickList("IncomingDocNew_DocName", query);
					
				}
				else{
					//Query corrected by Deepak.
					//change in queries by Saurabh on 4th Jan 19.
					String targetsegCode="",emptype="";
					if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType"))){
						targetsegCode = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
						emptype="SAL";
					}
					else{
						int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
						for(int i=0;i<rowCount;i++){
							if("Secondary".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,2))){
								targetsegCode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,22);
							}
						}
					}
					RLOS.mLogger.info("EmploymentType: "+formObject.getNGValue("EmploymentType") );
					RLOS.mLogger.info("TagertSegmentCode: "+targetsegCode );
					String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
					
					if("CAC".equalsIgnoreCase(targetsegCode)){
						//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='CAC' ";
						query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='CAC' or Designation='All') ";
					}
					else if("DOC".equalsIgnoreCase(targetsegCode)){
						//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='DOC' ";
						query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='DOC' or Designation='All') ";
					}
					else if("EMPID".equalsIgnoreCase(targetsegCode)){
						//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='EMPID' ";
						query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='EMPID' or Designation='All') ";
					}
					else if("NEPALO".equalsIgnoreCase(targetsegCode) || "NEPNAL".equalsIgnoreCase(targetsegCode)){
						//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='NEP' ";
						query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='NEP' or Designation='All') ";
					}
					else if("VIS".equalsIgnoreCase(targetsegCode)){
						query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='VC' or Designation='All') ";
					}
					else{
						query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='"+targetsegCode+"' or Designation='"+emptype+"' or Designation='All') ";
						
					}
				
					
					RLOS.mLogger.info( "when row count is  zero inside else"+query);
					LoadPickList("IncomingDocNew_DocName", query);
					

					
				}
				
				
				//LoadPickList("IncomingDocNew_DocName", "SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='VC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') ");
				formObject.setNGValue("IncomingDocNew_mandatory", "");
			}
			
			//code by saurabh on 28th Nov 2017 for calculating EMI after change in interest rate or tenor
			else if("cmplx_EligibilityAndProductInfo_InterestRate".equalsIgnoreCase(pEvent.getSource().getName()) || "cmplx_EligibilityAndProductInfo_Tenor".equalsIgnoreCase(pEvent.getSource().getName())|| "cmplx_EligibilityAndProductInfo_Moratorium".equalsIgnoreCase(pEvent.getSource().getName())){
				String RequiredProd="";
				try{
					int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					if(n>0)
					{
						RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
					}
					if("Personal Loan".equalsIgnoreCase(RequiredProd))
					{//by shweta

					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					//double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));

					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					double cac_calc_limit=0.0;
					String cac_calc_limit_str = null;
					int Mono_day = Integer.parseInt(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"));
					
					String First_emi_Date = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate");				
					String EMI=getEMI_new(LoanAmount,RateofInt,tenor,First_emi_Date);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
					if(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")!=null && !formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					}
					
					double out_aff_emi=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_OutAffEmi")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_OutAffEmi"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_OutAffEmi"));
					cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
					cac_calc_limit_str = String.format("%.0f", cac_calc_limit);
					String query="update  ng_rlos_IGR_Eligibility_PersonalLoan set CACCalculatedLimit='"+cac_calc_limit_str+"' where wi_name= '"+ formObject.getWFWorkitemName() +"'";
					//			RLOS.mLogger.info( "QUERY:"+query);
					formObject.saveDataIntoDataSource(query);
								
					}
					if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)))
					{
						double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
						double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
						double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
						double cac_calc_limit=0.0;
						String cac_calc_limit_str = null;
						int Mono_day = Integer.parseInt(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"));
						
						String First_emi_Date = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate");				
						String EMI=getEMI(LoanAmount,RateofInt,tenor);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
					}
					
				}
				catch(Exception e){
					RLOS.logException(e);
					//RLOS.mLogger.info(" Exception in EMI Generation");
				}
			}
			//change by saurabh on 9th Jan
			else if ("indusSector".equalsIgnoreCase(pEvent.getSource().getName())){					
				LoadPickList("indusMAcro", "Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
			}

			else if ("indusMAcro".equalsIgnoreCase(pEvent.getSource().getName())){
				LoadPickList("indusMicro","Select micro from (select '--Select--' as micro union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("indusMAcro")+"' and IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y' ) new_table order by case  when micro ='--Select--' then 0 else 1 end");
			}

			/*	else if ("cmplx_EmploymentDetails_DOJ".equalsIgnoreCase(pEvent.getSource().getName())){
		RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		common.getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
	}*/
			/*else if ("cmplx_EligibilityAndProductInfo_FirstRepayDate".equalsIgnoreCase(pEvent.getSource().getName())){
				RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"));
				String AgeAtMAturity=getYearsDifference(formObject,"cmplx_Customer_DOb","cmplx_EligibilityAndProductInfo_MaturityDate");
				RLOS.mLogger.info( "Value of dob is:"+AgeAtMAturity);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_ageAtMaturity", AgeAtMAturity);

			}----commented by akshay on 10/4/18*/
			/*else if ("cmplx_Customer_Nationality".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			RLOS.mLogger.info( "iNSIDE cmplx_Customer_Nationality CASE-->akShay "+formObject.getNGValue("cmplx_Customer_Nationality"));

			String  GCC="BH,IQ,KW,OM,QA,SA,AE";

			if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
			{
				RLOS.mLogger.info("Inside GCC for Nationality");
				formObject.setNGValue("cmplx_Customer_GCCNational","Y");
			}
			else
			{
				formObject.setNGValue("cmplx_Customer_GCCNational","N");
			}
		}*/
			else if("cmplx_EmploymentDetails_ApplicationCateg".equalsIgnoreCase(pEvent.getSource().getName())){
				if("S".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg"))){
					if("EKTMC-EXPAT".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKTMC-UAE".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKWEC-EXPAT".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKWEC-UAE".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKWEC-EXPAT".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary"))||"EKWEI-EXPAT".equalsIgnoreCase(formObject.getNGValue("CardProduct_Primary")))
					{
						RLOS.mLogger.info( "sagarika tier");
						formObject.setVisible("EMploymentDetails_Label18", true);
						formObject.setVisible("cmplx_EmploymentDetails_tierno", true);	
					}
					
					formObject.setVisible("EMploymentDetails_Label59", true);
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
				}
				else{
					formObject.setVisible("EMploymentDetails_Label18", false);
					formObject.setVisible("cmplx_EmploymentDetails_tierno", false);	
					formObject.setVisible("EMploymentDetails_Label59", false);
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
				}
				
				loadPicklist_TargetSegmentCode();
			}

			else if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
				String ReqProd=formObject.getNGValue("ReqProd");
				//RLOS.mLogger.info( "Value of ReqProd is:"+ReqProd);
				loadPicklistProduct(ReqProd);
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){
					formObject.setVisible("AlternateContactDetails_retainAccount",false);
					
				}
				else{
					formObject.setVisible("AlternateContactDetails_retainAccount",true);
					
				}
			}

			else if ("cmplx_EmploymentDetails_EmpStatus".equalsIgnoreCase(pEvent.getSource().getName())){
				LoadPickList("cmplx_EmploymentDetails_EmpContractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmpContractType with (nolock)  where isActive='Y' and EmpStatus='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus")+"' order by code");
			}
			
			
			else if ("subProd".equalsIgnoreCase(pEvent.getSource().getName())){
				//RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("subProd"));
				formObject.clear("AppType");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("subProd")+"' and isActive='Y' order by code");
				if("SAL".equalsIgnoreCase(formObject.getNGValue("subProd")))
				{
					formObject.setNGValue("AppType", "NEWE");
					formObject.setLocked("AppType", true);
				}
				else 
				{
					formObject.setLocked("AppType", false);
				}

				String subprod=formObject.getNGValue("subProd");
				String VIPFlag="true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))?"Y":"N";
				String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
				String req_prod=formObject.getNGValue("ReqProd");
				String TypeOfProd=formObject.getNGValue("Product_type");
				//RLOS.mLogger.info( "Value of SubProd is:"+subprod);
				//RLOS.mLogger.info( "Value of vip is:"+VIPFlag);
				//RLOS.mLogger.info( "Value of Nationality is:"+Nationality);
				//RLOS.mLogger.info( "Value of req_prod is:"+req_prod);
				//RLOS.mLogger.info( "Value of TypeOfProd is:"+TypeOfProd);

				if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(req_prod)){
					if("PU".equalsIgnoreCase(subprod) || "BTC".equalsIgnoreCase(subprod) || "SE".equalsIgnoreCase(subprod) || "SEC".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("Product_Label6",true);//Arun (06/12/17) to hide this field
						formObject.setVisible("CardProd",true);//Arun (06/12/17) to hide this field
						//formObject.setVisible("CardDetails_container",false);//Arun (06/12/17) to hide this fragment
						//formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable

						formObject.clear("CardProd");
						//RLOS.mLogger.info( " is  BTC/PU :"+subprod);
						fetch_cardProductMasters(formObject);
						//Deepak Code change to load card product with new master.
						//LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");
						/*if("Conventional".equalsIgnoreCase(TypeOfProd)){
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%' order by code");
					}
					else{
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%' order by code");
					}*/
					}

					else if("LI".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("Product_Label6",false);//Arun (06/12/17) to hide this field
						formObject.setVisible("CardProd",false);//Arun (06/12/17) to hide this field

						formObject.setVisible("Product_Label10",true);//Arun (19/12/17) to hide this field
						formObject.setVisible("NoOfMonths",true);//Arun (19/12/17) to hide this field
						formObject.setVisible("Product_Label7",true);
						formObject.setVisible("LastPermanentLimit",true);
						formObject.setVisible("Product_Label9",true);
						formObject.setVisible("LastTemporaryLimit",true);
						//formObject.setVisible("Product_Label16",false);//Arun (06/12/17) to hide this field
						//formObject.setVisible("LimitAcc",false);//Arun (06/12/17) to hide this field
						//formObject.setVisible("CardDetails_container",false);//Arun (06/12/17) to hide this fragment
						//formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable

						formObject.setNGValue("CardProd","");
						//RLOS.mLogger.info( " is not BTC ,PA :"+subprod);

						//Deepak Code change to load card product with new master.
						//LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");
						/*if("Conventional".equalsIgnoreCase(TypeOfProd)){
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%' order by code");
					}
					else{
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%' order by code");
					}*/
					}

					else if("PULI".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("Product_Label7",true);
						formObject.setVisible("LastPermanentLimit",true);
						formObject.setVisible("Product_Label9",true);
						formObject.setVisible("LastTemporaryLimit",true);

						formObject.setVisible("Product_Label6",true);//Arun (06/12/17) to show this field
						formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
						//formObject.setVisible("Product_Label16",false);//Arun (06/12/17) to hide this field
						//formObject.setVisible("LimitAcc",false);//Arun (06/12/17) to hide this field
						//formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable

						formObject.clear("CardProd");
						//RLOS.mLogger.info( " is not BTC ,PA :"+subprod);
						fetch_cardProductMasters(formObject);
						//Deepak Code change to load card product with new master.
						//LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");

					}

					/*	else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(subprod))
					{
						formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",false);
					}//Arun (06/12/17) to make editable 

					else if("SEC".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("Product_Label11",true);//Arun (06/12/17) to show this field
						formObject.setVisible("CardProd",true);

					}//Arun 19/12/17
					 */


					else{
						//added by akshay on 20/11/17 so select sal or SALP in emptype

						//RLOS.mLogger.info( "subProd is not PU or LI"); 
						if("IM".equalsIgnoreCase(subprod) || "SAL".equalsIgnoreCase(subprod) || "BPA".equalsIgnoreCase(subprod))
						{
							//RLOS.mLogger.info("subProd is IM/SAL/BPA");
							//formObject.setVisible("Product_Label11",true);//Arun (06/12/17) to show this field
							//formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
							formObject.clear("EmpType");
							fetch_cardProductMasters(formObject);
							RLOS.mLogger.info("subProd is BPA s");

							LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where code  like 'sal%' and IsActive='Y' order by code");
							RLOS.mLogger.info("subProd is BPA v");
							//formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable
						}
						else{
							//LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where code not like 'sal%' order by code");
							LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where IsActive='Y' order by code"); //Arun (05/12/17) modified to load the emplyment type masters correctly

							//formObject.setNGValue("EmpType", NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed"));
						}

						fetch_cardProductMasters(formObject);//code replaced by function on 16/4/18 by akshay
					}
					if("PA".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("Product_Label6",true);
						formObject.setVisible("CardProd",true);
					}//Arun 19/12/17
				}

				else
				{
					formObject.setVisible("LastPermanentLimit",false);	


				}//Arun 19/12/17
				LoadPickList("EmpType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where Sub_Product = '"+subprod+"'  and IsActive='Y' order by code");
			}

			else if ("AppType".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				String subprod=formObject.getNGValue("SubProd");
				String apptype=formObject.getNGValue("AppType");
				String TypeofProduct=formObject.getNGValue("Product_type");
				String reqProduct=formObject.getNGValue("ReqProd");
				//RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
				//RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("AppType"));
				//RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("Product_type"));

				if ("LI".equalsIgnoreCase(subprod) || "PULI".equalsIgnoreCase(subprod)){

					//RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.setVisible("Product_Label9",true);
					formObject.setVisible("LastTemporaryLimit",true);
					formObject.setVisible("Product_Label7",true);
					formObject.setVisible("LastPermanentLimit",true);

					if("T".equalsIgnoreCase(apptype)){
						formObject.setVisible("Product_Label10",true);
						formObject.setVisible("NoOfMonths",true);
					}
					else{
						formObject.setVisible("Product_Label10",false);
						formObject.setVisible("NoOfMonths",false);
					}
				}//changed by akshay on 3/1/17

				
				 

				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& "IM".equalsIgnoreCase(subprod)&&("TOPIM".equalsIgnoreCase(apptype))){
					//RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.setVisible("Product_Label9",false);
					formObject.setVisible("LastTemporaryLimit",false);
					formObject.setVisible("Product_Label7",false);
					formObject.setVisible("LastPermanentLimit",false);
				} 


				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProduct)){
					
					fetch_SchemeMasters(formObject);
				}
				
				else
				{
					formObject.setVisible("Product_Label9",false);
					formObject.setVisible("LastTemporaryLimit",false);
					formObject.setVisible("Product_Label7",false);
					formObject.setVisible("LastPermanentLimit",false);
					formObject.setVisible("Product_Label10",false);//added by akshay on 3/1/17
					formObject.setVisible("NoOfMonths",false);
				}
			}
			//added by akshay on 12/15/17
			else if ("Scheme".equalsIgnoreCase(pEvent.getSource().getName())){
				if(!"".equalsIgnoreCase(formObject.getNGValue("Scheme")) && !"--Select--".equalsIgnoreCase(formObject.getNGValue("Scheme"))){
					String query="select top 1 mintenure,maxtenure from NG_master_Scheme  with (nolock) where schemeid='"+formObject.getNGValue("Scheme")+"'";
					List<String> control_list=Arrays.asList("minTenor","maxTenor");
					formObject.getDataFromDataSource(query, control_list);
				}
			}
			//changed by akshay on 8th nov 2017 for loadpicklist.
			else if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName()) && ("true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others")) || "Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval")))){

				if(!"Y".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Is_Employer_Search")) || "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))  )
				{
				//RLOS.mLogger.info( "$Indus Sector$:" +formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector"));
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);//it is unlocked from js but its instance state is saved as locked as it was locked on fragment load
				LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
				}
			}

			else if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName()) && ("true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others")) || "Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval")))){
				//RLOS.mLogger.info( "$Indus Macro$:" +formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
				if(!"Y".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Is_Employer_Search")) || "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others")) )
				{
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
				LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y' ) new_table order by case  when micro ='--Select--' then 0 else 1 end");
				}
			}
			//commented by saurabh on 10th nov 2017.
			/*	else if ("AlternateContactDetails_carddispatch".equalsIgnoreCase(pEvent.getSource().getName())){
		String cardDisp=formObject.getNGSelectedItemText("AlternateContactDetails_carddispatch");
		RLOS.mLogger.info(cardDisp);
		//changes by saurabh on 10th nov 2017.
		if (!"COURIER".equalsIgnoreCase(cardDisp)){
			formObject.setVisible("AltContactDetails_Label14",true);
			formObject.setVisible("AlternateContactDetails_custdomicile",true);
			//Deepak Code change to add requested product filter

			String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					
		}

	}*/
			else if ("Product_type".equalsIgnoreCase(pEvent.getSource().getName())){
					if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){
					formObject.setVisible("Product_Label6", false);
					formObject.setNGValue("ReqProd" , "Personal Loan");
					} else if ("Credit Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){					
					formObject.setNGValue("ReqProd" , "Credit Card");
					}	else{
						formObject.setLocked("ReqProd" , false);
						formObject.setNGValue("ReqProd" , "--Select--");
						formObject.setVisible("Product_Label6", false);
					}
					String req_prod=formObject.getNGValue("ReqProd");
					
					
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(req_prod)){
						loadPicklistProduct(req_prod);
						fetch_cardProductMasters(formObject);

					}
					else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(req_prod)){
						loadPicklistProduct(req_prod);
						fetch_SchemeMasters(formObject);
					}
				//String ProdType=formObject.getNGValue("Product_type");
				//RLOS.mLogger.info(ProdType);
				//formObject.clear("CardProd");
				//formObject.setNGValue("CardProd","--Select--");
				//Deepak Code change to load card product with new master.
				//LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+ProdType+"' order by code");
			}
			else if("EmpType".equalsIgnoreCase(pEvent.getSource().getName())){
				fetch_cardProductMasters(formObject);
			}//EmpType
			//added by yash on 30/8/2017
			else if ("NotepadDetails_notedesc".equalsIgnoreCase(pEvent.getSource().getName())){
				String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");

				String sQuery = "select code,workstep from ng_master_notedescription where description='" +  notepad_desc + "'";


				List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
				if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)) && recordList!=null)
				{
					formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
					formObject.setNGValue("NotepadDetails_WorkstepName",recordList.get(0).get(1));		        

				}
				formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName());



			}
			else if("cmplx_Customer_RM_TL_NAME".equalsIgnoreCase(pEvent.getSource().getName()) ){
				RLOS.mLogger.info("Inside change event for RM TL Name");
				String rmtlname = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
				if(rmtlname != ""){
					formObject.setNGValue("RM_Name", rmtlname); 
					formObject.setNGValue("lbl_TL_Name_val", rmtlname);
				}
				RLOS.mLogger.info( "RM_Name :: "+formObject.getNGValue("RM_Name"));
			}

			
			else if("cmplx_CardDetails_SuppCardReq".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_Frame1")+30);
			}
			//++below code added by nikhil for Self-Supp CR
			//change in self-supp 20-08-2019
			else if("cmplx_CardDetails_SelfSupp_required".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				List<List<String>> Avl_Cards = get_Avl_Cards();
				if(Avl_Cards==null || Avl_Cards.size()==0)
				{
					if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required")))
					{	
						Refresh_self_supp_data();
						formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", "--Select--");
						throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL062")));
					}
				}
				if(!"Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required")))
				{
					Refresh_self_supp_data();
				}
				else
				{
					formObject.setEnabled("cmplx_CardDetails_Selected_Card_Product", true);
					formObject.setEnabled("cmplx_CardDetails_SelfSupp_Limit", true);
					formObject.setEnabled("cmplx_CardDetails_Self_card_embossing", true);
					LoadPickList("cmplx_CardDetails_Selected_Card_Product", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
				}
			}
			else if("cmplx_EmploymentDetails_RegPay".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.RaiseEvent("WFSave");
				RLOS.mLogger.info( "Value of reg is:"+formObject.getNGValue("cmplx_EmploymentDetails_RegPay"));
			}
			//--above code added by nikhil for Self-Supp CR
			else if("cmplx_CC_Loan_DDSBankAName".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				String query = "select top 1 ENTITY_NO from NG_MASTER_BankName where code='"+formObject.getNGValue("cmplx_CC_Loan_DDSBankAName")+"'";
				List<List<String>> result = formObject.getDataFromDataSource(query);
				
				if(result!=null && !result.isEmpty()){
					formObject.setNGValue("cmplx_CC_Loan_DDSEntityNo", result.get(0).get(0));
				}
				else{
					formObject.setNGValue("cmplx_CC_Loan_DDSEntityNo","");
				}
			}
			else if ("cmplx_Customer_Dsa_NAme".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				String Branch="";
				String strSolId = "";
				//LoadPickList("BRANCH", "select branch from NG_MASTER_SourceCode where UserId='"+formObject.getUserName()+"'");
				String Query="select branch,SOL_ID from NG_MASTER_SourceCode where UserId='"+formObject.getNGValue("cmplx_Customer_Dsa_NAme")+"'";
				List<List<String>> result=formObject.getDataFromDataSource(Query);
				RLOS.mLogger.info( "result of fetch RMname query: "+result); 

				if(result==null  || result.isEmpty()){
					Branch= "";
					strSolId="";
				}
				else{
					Branch= result.get(0).get(0);
					strSolId= result.get(0).get(1);
				}
				formObject.setNGValue("BRANCH", Branch);
				formObject.setNGValue("SOLID", strSolId);
				//added by nikhil for Branch Filter CR
				formObject.setNGValue("q_SOLID", strSolId);
				formObject.setNGValue("DSA_Name", formObject.getNGValue("cmplx_Customer_Dsa_NAme"));
				formObject.RaiseEvent("WFSave");
				formObject.setVisible("cmplx_Customer_Dsa_NAme",true);
				formObject.setEnabled("cmplx_Customer_Dsa_NAme",true);
			}
			
			else if("transtype".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				LoadPickList("marketingCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MarketingCodeService with (nolock) where TransactionType = '"+formObject.getNGValue("transtype")+"' and IsActive = 'Y' order by code");
			}
		
		}

		/*          Function Header:

		 **********************************************************************************

         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


Date Modified                       : 6/08/2017              
Author                              : Akshay Gupta             
Description                         :To make fragments locked on load       

		 ***********************************************************************************  */


		public void DisableFragmentsOnLoad(ComponentEvent pEvent)//Except Decision history
		{
			//RLOS.mLogger.info("Inside RLOSCommoncode-->DisableFragmentsOnLoad()");
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("Customer_Frame1",true);
				loadPicklistCustomer();
			}
			else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("GuarantorDetails_Frame1",true);
			}
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				//String ReqProd=formObject.getNGValue("ReqProd");
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				//loadPicklistProduct(ReqProd);--commented on 3/1/18 by akshay
				formObject.setLocked("Product_Frame1",true);
			}
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("EMploymentDetails_Frame1",true);
				loadPicklist4();
			}
			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{	LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			LoadPickList("cmplx_IncomeDetails_StatementCycle2", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			//commnetd by nikhil for rahcit cr
			/*LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
*/
			//below query changed by saurabh on 12th Oct to populate correct masters for bank stat from.
			LoadPickList("IncomeDetails_BankStatFrom", "select '--Select--' union select convert(varchar, description) from NG_MASTER_othBankCAC with (nolock)");

			formObject.setLocked("IncomeDetails_Frame1",true);			
			}
			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("CompanyDetails_Frame1",true);//changed by akshay on 3/1/17
				formObject.setLocked("CompanyDetails_Frame2",true);
				formObject.setLocked("CompanyDetails_Frame3",true);
				loadPicklist_AuthSign();
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("CardDetails_Frame1",true);
				if(!(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC"))){
					formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName", true);
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", false);
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC"))
				{
					formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName", false);
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossingName", true);
				}
			}
			/*else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("PartnerDetails_Frame1",true);
		}
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			loadPicklist_AuthSign();
			formObject.setLocked("AuthorisedSignDetails_Frame1", true);
		}*/

			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			}
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("Liability_New_Frame1",true);
			}
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("CC_Loan_Frame1",true);
				loadPicklist_ServiceRequest();
			}
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("AddressDetails_Frame1",true);
				loadPicklist_Address();
			}
			else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("ReferenceDetails_Frame1",true);
				LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from NG_MASTER_Relationship with (nolock) order by code");

			}
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
			//	LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to					
				//Deepak Code change to add requested product filter
				String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
				LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					

				formObject.setLocked("AltContactDetails_Frame1",true);
			}
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				formObject.setLocked("SupplementCardDetails_Frame1",true);
			}
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
			{	
				
				loadPicklist_Fatca();//added by prabhakar drop-4 point-3
				//++ below code changed by abhishek on 13/11/2017
				formObject.setLocked("FATCA_Frame6",true);
				//++ Above code changed by abhishek on 13/11/2017
			}
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				loadPicklist_Kyc();//added by prabhakar drop-4 point-3
				//++ below code changed by abhishek on 13/11/2017
				formObject.setLocked("KYC_Frame7",true);
				//++ Above code changed by abhishek on 13/11/2017
			}
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				loadPicklist_OECD();//added by prabhakar drop-4 point-3
				//++ below code changed by abhishek on 13/11/2017
				formObject.setLocked("OECD_Frame8",true);
				//++ Above code changed by abhishek on 13/11/2017
			}
			else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setLocked("IncomingDoc_Frame1",true);

			}		
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				//RLOS.mLogger.info("loadInDecGrid");
				loadInDecGrid();
				formObject.setLocked("DecisionHistory_Frame1", true);
				formObject.setLocked("cmplx_DecisionHistory_Decision",false);
				formObject.setLocked("cmplx_DecisionHistory_Remarks",false);
				formObject.setLocked("DecisionHistory_Save",false);
				formObject.setVisible("DecisionHistory_SendSMS",false);
				formObject.setVisible("DecisionHistory_Button3",false);


				loadPicklist3();
				//++ below code added by abhishek on 13/11/2017
				formObject.setVisible("Decision_ListView1", true);
				formObject.setTop("Decision_ListView1", formObject.getTop("DecisionHistory_Save")+40);
				formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("Decision_ListView1") + formObject.getHeight("Decision_ListView1") + 20);
				formObject.setHeight("DecisionHistoryContainer",formObject.getHeight("DecisionHistory_Frame1") + 10 );
				//++ Above code added by abhishek on 13/11/2017

			}	
			//++ below code changed by abhishek on 13/11/2017
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setLocked("NotepadDetails_Frame1",true);

			}	
			//++ Above code changed by abhishek on 13/11/2017

		}

		/*          Function Header:

		 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Akshay Gupta             
	Description                         :To fetch Customer fragment       

		 ***********************************************************************************  */


		public void CustomerFragment_Load()
		{		
			formObject = FormContext.getCurrentInstance().getFormReference();
			//changed done to handel Exception in this method, Only try catch block is added along with some null pointer check
			try{
				formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
				formObject.setNGFrameState("CustomerDetails", 0);

				//String cardnotavailable=formObject.getNGValue("cmplx_Customer_CardNotAvailable");
				String MobNo=formObject.getNGValue("cmplx_Customer_MobNo");
				//RLOS.mLogger.info("MobNo"+MobNo);
				String OTPMobNo=formObject.getNGValue("OTP_Mobile_NO");
				//RLOS.mLogger.info("OTPMobNo"+OTPMobNo);
				if ("".equalsIgnoreCase(OTPMobNo) && OTPMobNo!=null){
					formObject.setNGValue("OTP_Mobile_NO", MobNo);


				}

				String userName = formObject.getUserName();
				if(formObject.getNGValue("processby_email")==null && userName!=null)

				{
					setMailId(userName);

				}
				/*if (cardnotavailable.equalsIgnoreCase("true")){
				SKLogger.writeLog("RLOS", "CardNotavail"+cardnotavailable);
				formObject.setLocked("ReadFromCard", true);
			}*/
				if	(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Req"))){
					formObject.setLocked("cmplx_Customer_FIrstNAme", true);
					formObject.setLocked("cmplx_Customer_MiddleName", true);
					formObject.setLocked("cmplx_Customer_LAstNAme", true);
					formObject.setLocked("cmplx_Customer_Nationality", true);
					formObject.setLocked("cmplx_Customer_DOb", true);
					formObject.setLocked("cmplx_Customer_PAssportNo", true);
					formObject.setLocked("cmplx_Customer_MobNo", true);
					formObject.setLocked("cmplx_Customer_CardNotAvailable", true);
					formObject.setLocked("cmplx_Customer_NEP", false);
					formObject.setLocked("ReadFromCard", true);
					formObject.setLocked("Customer_CheckBox6", true);
					formObject.setLocked("cmplx_Customer_CIFNO", true);
					formObject.setLocked("FetchDetails", true);
					//RLOS.mLogger.info("Value of customer_Req after locking all the fields");	
				}
				//Added By Shivang
				RLOS.mLogger.info("@Shivang :: Nationality :: "+formObject.getNGValue("cmplx_Customer_Nationality"));
				if( formObject.getNGValue("cmplx_Customer_Nationality") !=null && formObject.getNGValue("cmplx_Customer_Nationality").equalsIgnoreCase("AE")){
					formObject.setLocked("cmplx_Customer_VisaNo",true);
					formObject.setLocked("cmplx_Customer_VisaIssuedate",true);
					formObject.setLocked("cmplx_Customer_VIsaExpiry",true);
				}
				//String nep=formObject.getNGValue("cmplx_Customer_NEP");
				/*String card_not_avail=formObject.getNGValue("cmplx_Customer_card_id_available");
			if ("true".equalsIgnoreCase(card_not_avail))
			{
				formObject.setLocked("Customer_Button1", false);
			}
			else {
				formObject.setLocked("Customer_Button1", true);
			}	
				 */
				//Commented by aman becauser the same is handled in initiation file 08/12
			}
			catch(Exception e){
				RLOS.mLogger.info("RLOS CommonCOde CustomerFragment_Load() Exception occured: "+printException(e));
			}
		}
		
		public void fetchPipeline(){
			try{
				RLOS.mLogger.info("Inside fetchPipeline::");
				formObject = FormContext.getCurrentInstance().getFormReference();

				List<String> objInput_ppl=new ArrayList<String>();

				objInput_ppl.add("Text:"+formObject.getWFWorkitemName());					
				//objInput_ppl.add("Text");
				RLOS.mLogger.info("Before executing procedure ng_RLOS_CASPipelineCheck::"+objInput_ppl.toString());
				List<Object> objOutput_ppl=new ArrayList<Object>();
				objOutput_ppl=formObject.getDataFromStoredProcedure("ng_RLOS_CASPipelineCheck", objInput_ppl,objOutput_ppl);
				RLOS.mLogger.info("after executing procedure ng_RLOS_CASPipelineCheck::"+objOutput_ppl.toString());
				
				if(formObject.getNGValue("cmplx_Customer_NTB").equals("false")){
					List<String> objInput=new ArrayList<String>();				
					objInput.add("Text:" + formObject.getWFWorkitemName());		
					List<Object> objOutput=new ArrayList<Object>();

					objOutput.add("Text");
					RLOS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
					objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_CheckWriteOff", objInput,objOutput);
					RLOS.mLogger.info("after executing procedure ng_RLOS_CheckWriteOff::"+objOutput.toString());
				}

			}
			catch(Exception e){
				RLOS.mLogger.info("Exception occured inside fetchPipeline"+e.getMessage());
			}

		}
	
		private Double maxAmountAllowed(FormReference formObject) {
			String typeOfProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			String transtype= formObject.getNGValue("transtype");
			String paypurpose = formObject.getNGValue("PaymentPurpose");
			String cardProduct = formObject.getNGValue("cmplx_CC_Loan_BTC_CardProduct");
			String query;
			Double balTaken=0.0,availBal=0.0;
			int rowSelected = formObject.getSelectedIndex("cmplx_CC_Loan_cmplx_btc");
			try{
			if(!("Conventional".equalsIgnoreCase(typeOfProd)&&"CCC".equalsIgnoreCase(transtype))){
				paypurpose="NA";
			}
			
			if("Conventional".equalsIgnoreCase(typeOfProd)){
				query="select top 1 cast(cast(Final_Limit As float)*(select Percentage from NG_MASTER_ServiceMaxAmount where CardProduct='"+typeOfProd+"' and TransactionType='"+transtype+"' and PaymentPurpose='"+paypurpose+"')/100 AS float)from ng_rlos_IGR_Eligibility_CardLimit where wi_name='"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'";		
				for(int i=0;i<formObject.getLVWRowCount("cmplx_CC_Loan_cmplx_btc");i++){
					if(rowSelected!=i)
					balTaken += Double.parseDouble(formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", i,6));
				}
			}
			else{
				query="select top 1 cast(cast(Final_Limit As float)*(select Percentage from NG_MASTER_ServiceMaxAmount where CardProduct='"+typeOfProd+"' and TransactionType='"+transtype+"' and PaymentPurpose='"+paypurpose+"')/100 AS float)from ng_rlos_IGR_Eligibility_CardLimit where wi_name='"+formObject.getWFWorkitemName()+"' and Card_Product='"+cardProduct+"' and Cardproductselect='true'";
				for(int i=0;i<formObject.getLVWRowCount("cmplx_CC_Loan_cmplx_btc");i++){
					if(formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", i,21).equalsIgnoreCase(cardProduct)&& rowSelected!=i)
					balTaken += Double.parseDouble(formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", i,6));
				}
			}
			RLOS.mLogger.info("Query to Calculate Amount:"+query);
			List<List<String>> result =	formObject.getDataFromDataSource(query);
			availBal = Double.parseDouble(result.get(0).get(0))-balTaken; 
			}catch (Exception e) {
				RLOS.mLogger.info("Available Balance:"+e.getMessage());
			}
			RLOS.mLogger.info("Available Balance:"+availBal);
			return availBal;	
		}
	
	}



