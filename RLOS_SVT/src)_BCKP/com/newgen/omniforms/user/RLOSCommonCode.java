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
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
		}

		else if("CustomerDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info("Inside CustomerDetails frame expand");	
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			//formObject.setNGFrameState("CustomerDetails", 0);
		}

		else if ("ProductDetailsLoader".equalsIgnoreCase(pEvent.getSource().getName())) {

			RLOS.mLogger.info("Inside ProductDetailsLoader ");	
			//added Tanshu Aggarwal(23/06/2017)
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			adjustFrameTops("CustomerDetails,ProductDetailsLoader,Incomedetails");
		}

		else if ("EmploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{				

			RLOS.mLogger.info( "Inside EmploymentDetails before fragment load ");	

			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
			if(formObject.getNGValue("cmplx_EmploymentDetails_Others")=="true"){
				formObject.setVisible("EMploymentDetails_Label72",false);
				formObject.setVisible("cmplx_EmploymentDetails_EMpCode",false);
			}
			else 
			{
				formObject.setVisible("EMploymentDetails_Label72",true);
				formObject.setVisible("cmplx_EmploymentDetails_EMpCode",true);
			}
			RLOS.mLogger.info( "Inside EmploymentDetails after fragment load ");	

			/*if(!"SAL".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				formObject.setVisible("EMploymentDetails_Label59", false);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
			}
			else{
				formObject.setVisible("EMploymentDetails_Label59", true);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
			}---Indus Seg is only visible for surrogate case*/

			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
				formObject.setVisible("EMploymentDetails_Label71", false);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);
			}
			else{
				formObject.setVisible("EMploymentDetails_Label71", true);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
			}

		}		 

		else if ("Incomedetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
			if(Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"))<= 21f)
				adjustFrameTops("CustomerDetails,ProductDetailsLoader,GuarantorDetails,Incomedetails");	
			else
			adjustFrameTops("CustomerDetails,ProductDetailsLoader,Incomedetails");
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
		}

		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			//below code shifted to rlos_Init by akshay on 15/8/17

			RLOS.mLogger.info( "CompanyDetailse1:");
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
		}	
		else if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName()))
		{	
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");	
		}	
		else if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) 				
		{
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			//added by prabhakar as per FLV 6.13
			formObject.setVisible("AltContactDetails_Label6", false);
			formObject.setVisible("AlternateContactDetails_OfficeExt", false);
			
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
			}
			setCardProdDropdown();
		}	
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
		}	
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("KYC", "KYC", "q_KYC");
			loadPicklist_Kyc();
		}	
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("OECD", "OECD", "q_OECD");
		}	

		else if ("IncomingDocuments".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				RLOS.mLogger.info("Incomedetails");
			}
			else {
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				RLOS.mLogger.info("fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				visibilityFrameIncomeDetails(formObject);
			}
			//added by akshay on 20/12/17
			int framestate2=formObject.getNGFrameState("Liability_container");
			if(framestate2 == 0){
				RLOS.mLogger.info("Liability");
			}
			else {
				fetch_Liability_frag(formObject);

			}
			RLOS.mLogger.info("IncomingDocuments");
			formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
			RLOS.mLogger.info("fetchIncomingDocRepeater");
			fetchIncomingDocRepeater();
			RLOS.mLogger.info("formObject.fetchFragment1");
		}
		//added by yash on 23/8/2017
		if ("Notepad_Values".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Values","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' as description union select  description from ng_master_notedescription order by description");
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
		RLOS.mLogger.info("RLOS Emp Type Value is:"+EmpType);

		if("Salaried".equalsIgnoreCase(EmpType)|| "Salaried Pensioner".equalsIgnoreCase(EmpType))
		{
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 730);
			formObject.setHeight("IncomeDetails_Frame1", 680);	
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomeDetails_Label11", false);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", false);

				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", false);
			/*	formObject.setVisible("IncomeDetails_Label3", false);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", false);
		*///COMMENTED BY AMAN FOR PROC6508	
				}	
			else{
				formObject.setVisible("IncomeDetails_Label11", true);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", true);
				formObject.setVisible("IncomeDetails_Label13", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", true);
				formObject.setVisible("IncomeDetails_Label3", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", true);
			}	
		}

		else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(EmpType))
		{							
			formObject.setVisible("IncomeDetails_Frame2", false);
			formObject.setTop("IncomeDetails_Frame3",40);
			formObject.setHeight("Incomedetails", 430);
			formObject.setHeight("IncomeDetails_Frame1", 400);
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
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
			}	
		}
		String reqProd = formObject.getNGValue("PrimaryProduct");
		
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProd)){
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);
			formObject.setVisible("IncomeDetails_Label12",false);
		}
		else{
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);
			formObject.setVisible("IncomeDetails_Label12",true);

		}

	}

	/*          Function Header:

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

			RLOS.mLogger.info( "Index of Selected row is: "+formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"));
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
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2).equalsIgnoreCase("PULI")){
					formObject.setVisible("Product_Label8",false);
					formObject.setVisible("FDAmount",false);
				}

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
				//formObject.setNGValue("EmpType","");
				formObject.setNGValue("EmploymentType","");
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
			}	
		}
		//ended by akshay

		else if("ReferenceDetails_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("reference_wi_name"));
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
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("guarantor_WIName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}
		else  if("GuarantorDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}
		else  if("GuarantorDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}

		else if("IncomeDetails_FinacialSummarySelf".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("cmplx_IncomeDetails_AvgBalFreq", "Half Yearly");
			formObject.setNGValue("cmplx_IncomeDetails_CredTurnoverFreq", "Half Yearly");
			formObject.setNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq", "Half Yearly");

			String query = "select isNull(((Sum(convert(float,replace([jan],'NA','0'))))+(Sum(convert(float,replace([feb],'NA','0'))))+(Sum(convert(float,replace([mar],'NA','0'))))+(Sum(convert(float,replace([apr],'NA','0'))))+(Sum(convert(float,replace([may],'NA','0'))))+(Sum(convert(float,replace([jun],'NA','0'))))+(Sum(convert(float,replace([jul],'NA','0'))))+(Sum(convert(float,replace([aug],'NA','0'))))+(Sum(convert(float,replace([sep],'NA','0'))))+(Sum(convert(float,replace([oct],'NA','0'))))+(Sum(convert(float,replace([nov],'NA','0')))) +(Sum(convert(float,replace([dec],'NA','0'))))),0) as AVGBALDET from ng_rlos_FinancialSummary_AvgBalanceDtls where WI_Name ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> result = formObject.getNGDataFromDataCache(query);
			if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
			{
				formObject.setNGValue("cmplx_IncomeDetails_AvgBal", result.get(0).get(0));

			}
			String query2 = "select isNull((Sum(convert(float,replace([TotalCrAmt],'NA','0')))),0) as TotalCreditTurnover, case when isNull(max(AvgCrTurnOver),0) like '' then '0' else isNull(max(AvgCrTurnOver),0) end  as AvgCrTurnOver from ng_rlos_FinancialSummary_TxnSummary where WI_Name ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> result2 = formObject.getNGDataFromDataCache(query2);
			if(result2!=null && !result2.isEmpty())  //if(result!=null && result.size()>0)
			{
				formObject.setNGValue("cmplx_IncomeDetails_CredTurnover", result2.get(0).get(0));
				formObject.setNGValue("cmplx_IncomeDetails_AvgCredTurnover", result2.get(0).get(1));

			}

		}	

		else if ("Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			RLOS.mLogger.info( "Inside add button: aman123"+formObject.getNGValue("ReqProd"));

			formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");

			//tanshu aggarwal for documents(1/06/2017)
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear");
			boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear"+Visibility);
			if(Visibility == true){
				repObj.clear();
				repObj.refresh();
				//formObject.setNGFrameState("IncomingDocuments", 1);
				fetchIncomingDocRepeater();
				RLOS.mLogger.info("after doc fun");


			}

			else {
				String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
				RLOS.mLogger.info( "when row count is  zero inside else"+query);
				RLOS.mLogger.info( "when row count is  zero inside else after deleting the values");

			}
			RLOS.mLogger.info("after refresh");
			//tanshu aggarwal for documents(1/06/2017)

			AddProducts();
			AddPrimaryData();
			ParentToChild();
			formObject.setNGValue("EmploymentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
			formObject.setNGValue("Product_type",NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional"),false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","",false);
			//formObject.setNGValue("EmpType","",false);
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
			formObject.setNGFrameState("EmploymentDetails", 1);
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
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2))){
				if(formObject.isVisible("Liability_New_Frame1")){
					formObject.setLocked("Liability_New_fetchLiabilities", true);
				}
			}
			FieldsVisibility_CardDetails(formObject);//added by akshay
		}	




		else if ("Modify".equalsIgnoreCase(pEvent.getSource().getName())){

			RLOS.mLogger.info( "Inside add button: aman123"+formObject.getNGValue("ReqProd"));

			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("ReqProd"))){
				formObject.setNGValue("Scheme","");
			}
			else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("ReqProd"))){
				formObject.setNGValue("CardProd","");
			}

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");



			//tanshu aggarwal for documents(1/06/2017)
			RLOS.mLogger.info("after creating repeater object00");
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear"+Visibility);
			if(Visibility == true){
				repObj.clear();
				repObj.refresh();
				fetchIncomingDocRepeater();
				RLOS.mLogger.info("after doc fun");
				//formObject.setNGFrameState("IncomingDocuments", 1);


			}

			else {
				String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
				RLOS.mLogger.info( "when row count is  zero inside else"+query);
				formObject.getNGDataFromDataCache(query);
				RLOS.mLogger.info( "when row count is  zero inside else after deleting the values123");


			}

			RLOS.mLogger.info("after creating repeater object00");
			//tanshu aggarwal for documents(1/06/2017)


			AddProducts();
			AddPrimaryData();
			ParentToChild();
			//setting insurance fields by saurabh on 11th oct.
			//setChargesFields();//Commented by Deepak new line of code written in check re-eligibility click event.
			formObject.setNGValue("EmploymentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGValue("Product_type",NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional"),false);
			formObject.setNGValue("ReqProd","--Select--",false);
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
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2))){
				if(formObject.isVisible("Liability_New_Frame1")){
					formObject.setLocked("Liability_New_fetchLiabilities", true);
				}
			}
		}

		else if ("Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

			//tanshu aggarwal for documents(1/06/2017)
			RLOS.mLogger.info("before creating repeater object22");


			RLOS.mLogger.info("before creating repeater object22 after saving the fragment12");
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear"+Visibility);
			if(Visibility == true){
				repObj.clear();
				repObj.refresh();
				formObject.setNGFrameState("IncomingDocuments", 1);
				RLOS.mLogger.info("after doc fun");
				RLOS.mLogger.info("before clear setframestate");
			}


			else {
				String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
				RLOS.mLogger.info( "when row count is  zero inside else"+query);
				formObject.getNGDataFromDataCache(query);
				RLOS.mLogger.info( "when row count is  zero inside else after deleting the values456");
			}
			RLOS.mLogger.info("after creating repeater object22");
			//tanshu aggarwal for documents(1/06/2017)

			AddProducts();
			AddPrimaryData();
			ParentToChild();
			//formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
			formObject.setNGValue("employmentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));

			formObject.setNGValue("Product_type",NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional"),false);
			formObject.setNGValue("ReqProd","--Select--",false);
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

		else if("IncomeDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))	
		{
			formObject.setNGValue("IncomeDetails_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info("RLO Inside add button: "+formObject.getNGValue("IncomeDetails_wi_name"));
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
		else if("CompanyDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("company_winame"));
			int row_count=formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
			int columns=0;
			/*UIComponent pComp =formObject.getComponent("cmplx_CompanyDetails_cmplx_CompanyGrid");

			if( pComp != null && pComp instanceof ListView )
			{			
				ListView objListView = ( ListView )pComp;
				columns  = objListView.getChildCount();
			}*/
			RLOS.mLogger.info( "company add button: row count : "+row_count);
			RLOS.mLogger.info( "company add button: cloumn count : "+columns);
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
			//added by akshay on 18/1/18 for unlocking frame which was locked when primary row was clicked
			formObject.setLocked("CompanyDetails_Frame1",false);
			formObject.setLocked("cif",true);
			//formObject.setLocked("lob",true);
		}

		else  if("CompanyDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			//added by akshay on 18/1/18 for unlocking frame which was locked when primary row was clicked
			formObject.setLocked("CompanyDetails_Frame1",false);
			formObject.setLocked("cif",true);
			//formObject.setLocked("lob",true);
		}

		else  if("CompanyDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");


		}

		else  if("PartnerDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("partner_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("partner_winame"));
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
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("AuthorisedSign_wiName"));
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
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("LiabilityAddition_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if ("Liability_New_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if ("Liability_New_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}	


		else if ("addr_Add".equalsIgnoreCase(pEvent.getSource().getName())  ){
			RLOS.mLogger.info( "Inside addredd grid add button");
			//	boolean flag_addressType= Address_Validate();
			//if(flag_addressType){
			formObject.setNGValue("address_Wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside address button: "+formObject.getNGValue("address_Wi_name"));
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
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("supplement_WiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");
			setDataInMultipleAppGrid();
		}


		else if ("SupplementCardDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "SupplementCardDetails_cmplx_SupplementGrid");
			setDataInMultipleAppGrid();
		}


		else if ("SupplementCardDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "SupplementCardDetails_cmplx_SupplementGrid");
			setDataInMultipleAppGrid();
		}	


		else if ("OECD_add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("OECD_winame"));
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

		//added by akshay on 15/9/17 to add data in BT Grid
		else if ("BT_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
			//formObject.setNGValue("benificiaryName", formObject.getNGValue("Customer_Name"),false);//Tarang to be removed on friday(1/19/2018)//added by akshay on 21/11/17 as per FSD 2.7
			formObject.setNGValue("benificiaryName", formObject.getNGValue("CustomerName"),false);
			RLOS.mLogger.info( "Inside add button: benificiaryName "+formObject.getNGValue("benificiaryName"));
			String accnum = formObject.getNGValue("Account_Number");
			formObject.setNGValue("creditcardNo", accnum,false);
			RLOS.mLogger.info( "Inside add button: Account_Number "+formObject.getNGValue("Account_Number"));
			//Added by aman on 08/12

		}

		else if ("BT_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
			//formObject.setNGValue("benificiaryName", formObject.getNGValue("Customer_Name"));//Tarang to be removed on friday(1/19/2018)//added by akshay on 21/11/17 as per FSD 2.7
			formObject.setNGValue("benificiaryName", formObject.getNGValue("CustomerName"));
			String accnum = formObject.getNGValue("Account_Number");
			formObject.setNGValue("creditcardNo", accnum);
			//Added by aman on 08/12
		}

		else if ("BT_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
		}

		//ended by akshay on 15/9/17 to add data in BT Grid




		else if("ReadFromCard".equalsIgnoreCase(pEvent.getSource().getName())){
			popupFlag = "Y";
			outputResponse = GenXml.GenerateXML("EID_Genuine","");
			ReturnCode =  (outputResponse.contains("<ns3:ServiceStatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusCode>")+"</ns3:ServiceStatusCode>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusCode>")):"";
			String Returndesc = (outputResponse.contains("<ns3:ServiceStatusDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusDesc>")+"</ns3:ServiceStatusDesc>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusDesc>")):"";
			//  ReturnCode="123";
			RLOS.mLogger.info(ReturnCode);
			RLOS.mLogger.info(Returndesc);
			if("s".equalsIgnoreCase(ReturnCode) && "Valid".equalsIgnoreCase(Returndesc)){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse, "");  
				formObject.setNGValue("Is_EID_Genuine", "Y");
				formObject.setNGValue("cmplx_Customer_IsGenuine", true);
				RLOS.mLogger.info("EID is generated");
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL002");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}   
			else{
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL003");
				formObject.setNGValue("Is_EID_Genuine", "N");
				RLOS.mLogger.info("EID is not generated");
			}

			RLOS.mLogger.info(formObject.getNGValue("Is_EID_Genuine"));					
		}    


		else if("FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{  
			formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("cmplx_Customer_IscustomerSave", "N");//added by akshay on 19/3/18
			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
			RLOS.mLogger.info( "Fetch Detail Started");
			String cif_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			RLOS.mLogger.info( "Value of cif_id_avail"+cif_no);
			if("".equalsIgnoreCase(cif_no)){ 				
				RLOS.mLogger.info( "Value of cif_id_avail is false"+cif_no);
				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				RLOS.mLogger.info( "EID value for Entity Details: "+EID );
				String ReadFrom_card_exc = formObject.getNGValue("cmplx_Customer_readfromcardflag");
				if( EID!=null && !"".equalsIgnoreCase(EID)&& "Y".equalsIgnoreCase(ReadFrom_card_exc))
				{							
					outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Primary_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("Is_Entity_Details","Y");
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");

					}
					else{
						formObject.setNGValue("Is_Entity_Details","N");

					}
					RLOS.mLogger.info(formObject.getNGValue("Is_Entity_Details"));							
				}
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				RLOS.mLogger.info("Customer Eligibility");
				ReturnCode = getTagValue(outputResponse, "ReturnCode");  
				//(outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info("Post new change CUSTOMER_ELIGIBILITY: "+ ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					setcustomer_enable();
					popupFlag="Y";
					//Deepak below line commented as Customer eligibility called before Parse Cif Eligibility 26Dec2017 
					//Deepak below line again uncommented to set NTB 27Dec2017
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");
					parse_cif_eligibility(outputResponse,"Primary_CIF");					
					formObject.setNGValue("NTB","true");//added by Akshay
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					//Customer_enable();
					// if(NTB_flag.equalsIgnoreCase("true")){
					//  Customer_enable();

					//}
					if("false".equalsIgnoreCase(NTB_flag)){
						// parse_cif_eligibility(outputResponse,"Primary_CIF");
						formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700);	
						//Code change to run Customer details if customer is existing customer in Customer Eligibility start (27-sept-2017)
						String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
						if(cif!=null && !"".equalsIgnoreCase(cif)){
							alert_msg =  fetch_cust_details_primary();
						}
						//Code change to run Customer details if customer is existing customer in Customer Eligibility End (27-sept-2017)
					}

					else if("true".equalsIgnoreCase(NTB_flag)){
						formObject.setVisible("Customer_Frame2", false);
						formObject.setEnabled("cmplx_Customer_VIsaExpiry", true);
						formObject.setLocked("cmplx_Customer_VisaNo", false);
						formObject.setLocked("IncomeDetails_FinacialSummarySelf", true);

						RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");
						throw new ValidatorException(new FacesMessage(alert_msg));
						//added By Akshay
					}

					//ended By akshay		

					/*
				else{
					alert_msg = fetch_cust_details_primary();
				}
					 */


					RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
					RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
					RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
					RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));

					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility")) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details")))
					{ 
						RLOS.mLogger.info("inside if condition");
						RLOS.mLogger.info( "Customer Eligibility and Customer details fetched sucessfully");
						formObject.setEnabled("FetchDetails", false); 
						formObject.setEnabled("Customer_Button1", false);
						//alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL006");
					}
					else{
						RLOS.mLogger.info( "Customer Eligibility and Customer details failed");
						//alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL007");
						formObject.setEnabled("FetchDetails", true);
						formObject.setEnabled("Customer_Button1", false);
					}
					RLOS.mLogger.info("");
					formObject.RaiseEvent("WFSave");

					popupFlag="Y";
					RLOS.mLogger.info( "Alert msg to be displayed on screen: "+alert_msg);
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					popupFlag="Y";
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL005");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}


			else{
				alert_msg = fetch_cust_details_primary();
				throw new ValidatorException(new FacesMessage(alert_msg));

			}

		}


		else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
			String NegatedFlag;
			popupFlag="Y";
			outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
			RLOS.mLogger.info("Customer Eligibility");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			formObject.setNGValue("Is_Customer_Eligibility","Y");


			if("0000".equalsIgnoreCase("0000")){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"Primary_CIF"); 
				parse_cif_eligibility(outputResponse,"Primary_CIF");
				BlacklistFlag= (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				RLOS.mLogger.info("Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag= (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				RLOS.mLogger.info("Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				RLOS.mLogger.info("Customer is NegatedFlag"+NegatedFlag);
				formObject.setNGValue("Is_Customer_Eligibility","Y");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
				formObject.setNGValue("BlacklistFlag",BlacklistFlag);
				formObject.setNGValue("DuplicationFlag",DuplicationFlag);
				formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
				String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
				if("true".equalsIgnoreCase(NTB_flag)){
					RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");
				}
				else{
					formObject.setHeight("Customer_Frame1", 620);
					formObject.setHeight("CustomerDetails", 700);
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL008");
				}


			}
			else{
				formObject.setNGValue("Is_Customer_Eligibility","N");
				formObject.RaiseEvent("WFSave");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
			RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
			RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
			RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//}
		}
		//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)

		//Fetch Details call in Supplementary details tab by saurabh on 11th September.
		else if("SupplementCardDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text1");
			RLOS.mLogger.info( "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
			String primaryCif = null;
			boolean isEntityDetailsSuccess = false;

			if( EmiratesID!=null && !"".equalsIgnoreCase(EmiratesID)){
				outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					//RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Supplementary_Card_Details");
					primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
					formObject.setNGValue("Supplementary_CIFNO",primaryCif);
					isEntityDetailsSuccess = true;
					alert_msg = fetch_cust_details_supplementary();
				}

				RLOS.mLogger.info(primaryCif);
			}
			if(!isEntityDetailsSuccess || (primaryCif==null || "".equalsIgnoreCase(primaryCif))){
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
				RLOS.mLogger.info("Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse ,"Supplementary_Card_Details");    
					parse_cif_eligibility(outputResponse,"Supplementary_Card_Details");
					String NTBFlag=(outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
					if("Y".equals(NTBFlag)){
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL004");	
					}
					else{
						alert_msg = fetch_cust_details_supplementary();
					}
				}
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else   if("Send_OTP_Btn".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("otp_ref_no", formObject.getWFFolderId());
			RLOS.mLogger.info( formObject.getWFFolderId()+"");
			hm.put("Send_OTP_Btn","Clicked");
			popupFlag="Y";
			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","GenerateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				RLOS.mLogger.info("OTP is generated");
				formObject.setEnabled("OTP_No",true);
				formObject.setEnabled("Validate_OTP_Btn",true);
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL009");
			}
			else{
				//formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL010");
				formObject.setEnabled("OTP_No",false);
				formObject.setEnabled("Validate_OTP_Btn",false);

			}
			RLOS.mLogger.info("OTP generation");
			try
			{
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			finally{hm.clear();}
		}

		else   if("AuthorisedSignDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){


			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				formObject.setNGValue("Is_Customer_Details_AuthSign","Y");
				RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_AuthSign"));
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Signatory_CIF");    
				RLOS.mLogger.info("authorised sign is generated");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
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
				formObject.setNGValue("AuthorisedSignDetails_DOB",common.Convert_dateFormat(formObject.getNGValue("AuthorisedSignDetails_DOB"),"yyyy-MM-dd","dd/MM/yyyy"));
				formObject.setNGValue("AuthorisedSignDetails_VisaExpiryDate",common.Convert_dateFormat(formObject.getNGValue("AuthorisedSignDetails_VisaExpiryDate"),"yyyy-MM-dd","dd/MM/yyyy"));
				formObject.setNGValue("AuthorisedSignDetails_PassportExpiryDate",common.Convert_dateFormat(formObject.getNGValue("AuthorisedSignDetails_PassportExpiryDate"),"yyyy-MM-dd","dd/MM/yyyy"));

			}
			else{
				RLOS.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details_AuthSign","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
		}


		else   if("DecisionHistory_SendSMS".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			RLOS.mLogger.info( "");

			outputResponse = GenXml.GenerateXML("SEND_ADHOC_ALERT","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase("0000") ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				RLOS.mLogger.info("SMS is send");
				//formObject.setEnabled("OTP_No",true);
				//formObject.setEnabled("Validate_OTP_Btn",true);
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL011");
			}
			else{
				//formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL012");
				RLOS.mLogger.info("Error while sending SMS");
			}

		}




		else  if("Validate_OTP_Btn".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","ValidateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			OTPStatus=(outputResponse.contains("<OTPStatus>")) ? outputResponse.substring(outputResponse.indexOf("<OTPStatus>")+"</OTPStatus>".length()-1,outputResponse.indexOf("</OTPStatus>")):"";    
			RLOS.mLogger.info(OTPStatus);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				formObject.setNGValue("cmplx_Customer_OTPValidation","true");

				RLOS.mLogger.info("OTP is generated");
				formObject.setNGValue("OTPStatus",OTPStatus);
				RLOS.mLogger.info(formObject.getNGValue("OTPStatus"));

				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL013");

			}
			else{
				formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL014");
			}
			RLOS.mLogger.info("OTP generation");
			try
			{
				popupFlag="Y";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			finally{hm.clear();}
		}


		else  if("CompanyDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info("CompanyDetails_Button3");
			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			/*	String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
		//RLOS.mLogger.info(CustId);
		String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
		//RLOS.mLogger.info(CorpName);
		String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
		//RLOS.mLogger.info(BusinessIncDate);
		String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
		//RLOS.mLogger.info(LegEnt);
			 */
			if("0000".equalsIgnoreCase("0000") ){
				formObject.setNGValue("Is_Customer_Details_CompanyCIF","Y");

				RLOS.mLogger.info("value of company Details corporation"+formObject.getNGValue("Is_Customer_Details_CompanyCIF"));

				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Corporation_CIF");  
				String query="Select INDUSTRY_SECTOR,INDUSTRY_MACRO, INDUSTRY_MICRO from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME='"+formObject.getNGValue("compName")+"'";
				RLOS.mLogger.info("Query to load alloc details: "+query);
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
					formObject.setNGValue("estbDate",common.Convert_dateFormat(estbDate, "yyyy-mm-dd", "dd/mm/yyyy"));
					RLOS.mLogger.info(formObject.getNGValue("estbDate"));
				}	
				if(TLIssueDate!=null&&!"".equalsIgnoreCase(TLIssueDate)){
					formObject.setNGValue("TLIssueDate",common.Convert_dateFormat(TLIssueDate, "yyyy-mm-dd", "dd/mm/yyyy"));
					RLOS.mLogger.info(formObject.getNGValue("TLIssueDate"));
				}
				if(TLExpiry!=null&&!"".equalsIgnoreCase(TLExpiry)){
					formObject.setNGValue("TLExpiry",common.Convert_dateFormat(TLExpiry, "yyyy-mm-dd", "dd/mm/yyyy"));
					RLOS.mLogger.info(formObject.getNGValue("TLExpiry"));
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
				RLOS.mLogger.info("corporation cif");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
				String AECBheld =  (outputResponse.contains("<AECBConsentHeld>")) ? outputResponse.substring(outputResponse.indexOf("<AECBConsentHeld>")+"</AECBConsentHeld>".length()-1,outputResponse.indexOf("</AECBConsentHeld>")):"";
				if ("Y".equalsIgnoreCase(AECBheld))

				{
					RLOS.mLogger.info( "Inside AECB Consent true check!!");
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
				RLOS.mLogger.info("Customer Details Corporation CIF is not generated");
				formObject.setNGValue("Is_Customer_Details_CompanyCIF","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
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
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) ){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");
					formObject.setNGValue("Is_Account_Summary","Y");
				}
				else{
					formObject.setNGValue("Is_Account_Summary","N");
				}
				RLOS.mLogger.info(formObject.getNGValue("Is_Account_Summary"));
				//ended


			}

			IMFields_Income();
			BTCFields_Income();
			LimitIncreaseFields_Income();
			ProductUpgrade_Income();

			if(!formObject.isVisible("EMploymentDetails_Frame1")){
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
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
			RLOS.mLogger.info("Prabhakar +++ "+pEvent.getSource().getName());
			String EmpName="";
			String EmpCode="";
			String buttonName="";
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
			RLOS.mLogger.info( "EMpName$"+EmpName+"$");
			String query;
			if("".equalsIgnoreCase(EmpName.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

			else if("".equalsIgnoreCase(EmpCode.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%'";

			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"%'";

			RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,buttonName, "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,Employer Category PL, Employer Status CC,Employer Status PL,MAIN EMPLOYER CODE", true, 20);
			
		}

		else if("cmplx_ExternalLiabilities_AECBConsent".equalsIgnoreCase(pEvent.getSource().getName())){
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_ExternalLiabilities_AECBConsent")))
				formObject.setEnabled("ExternalLiabilities_Button1",true);
			else
				formObject.setEnabled("ExternalLiabilities_Button1",false);
		}


		else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
					LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					formObject.setNGValue("cmplx_EmploymentDetails_ApplicationCateg","CN");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",false);//added By Tarang started on 22/02/2018 as per drop 4 point 20
					LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusCC where isActive='Y' and product='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard")+"' order by code" );					
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",false);
					if("IM".equals(formObject.getNGValue("Subproduct_productGrid"))){
						formObject.removeItem("cmplx_EmploymentDetails_EmpStatusCC",2);
						formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CN");//was not happening from js so done from here
					}
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) where code NOT LIKE '%CN%' order by code");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",true);//added By Tarang started on 22/02/2018 as per drop 4 point 20
					LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusCC where isActive='Y'  order by code" );					
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
			RLOS.mLogger.info( "Inside Supplement_add button: "+formObject.getNGValue("supplement_WIname"));
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
			RLOS.mLogger.info( "Inside FATCA_Button1 text is "+text);
			String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
			RLOS.mLogger.info("hiddenText is: "+hiddenText);
			if(hiddenText.endsWith(",")|| hiddenText.equalsIgnoreCase(""))
			{
				hiddenText=hiddenText+text+",";
			}
			else
			{
				hiddenText=hiddenText+","+text+",";
			}
			formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
			RLOS.mLogger.info( "Inside FATCA_Button1 hidden text is after Concat: "+hiddenText);
			formObject.addItem("cmplx_FATCA_SelectedReason", text);
			try {
				formObject.removeItem("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_ListedReason", -1);

			}catch (Exception e) {

				RLOS.logException(e);
			}

		}
		//Modified by prabhakar drop-4point-3
		else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "Inside FATCA_Button2 ");
			String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
			String hiddenTextRemove=formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"))+",";
			hiddenText=hiddenText.replace(hiddenTextRemove, "");
			formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
			formObject.addItem("cmplx_FATCA_ListedReason", formObject.getNGItemText("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason")));
			try {
				formObject.removeItem("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_SelectedReason", -1);
			} catch (Exception e) {

				RLOS.logException(e);
			}
		}
		//added by prabhakar drop-4point-3
		else if("cmplx_FATCA_cmplx_GR_FatcaDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			List<String> selectedReasonList= new ArrayList<String>();
			selectedReasonList.add("CORPORATE SHARE HOLDER");
			selectedReasonList.add("CURRENT US PHONE NO");
			selectedReasonList.add("SIGNATORY HAS A US ADDRESS");
			selectedReasonList.add("US PLACE OF BIRTH");
			selectedReasonList.add("CURRENT US MAILING ADDRESS OR RESIDENCE ADDRESS");
			selectedReasonList.add("POA/SIGNATORY HAS A US ADDRESS");
			
			RLOS.mLogger.info("Inside FatcaGrid Click");
			RLOS.mLogger.info("Index is"+formObject.getSelectedIndex("cmplx_FATCA_cmplx_GR_FatcaDetails"));
			String hiddenText=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails", formObject.getSelectedIndex("cmplx_FATCA_cmplx_GR_FatcaDetails"), 12);
			RLOS.mLogger.info("HiddenField is: "+hiddenText);
			List<String> listedReasonList=Arrays.asList(hiddenText.split(","));	
			if(formObject.getNGValue("cmplx_FATCA_SelectedReason").equalsIgnoreCase(""))
			{
				formObject.clear("cmplx_FATCA_listedreason");
				for (String string : listedReasonList)
				{
					selectedReasonList.remove(string);
				}
				RLOS.mLogger.info("cmplx_FATCA_SelectedReason"+listedReasonList);
			formObject.addItem("cmplx_FATCA_SelectedReason", listedReasonList);
			RLOS.mLogger.info("cmplx_FATCA_ListedReason"+selectedReasonList);
			formObject.addItem("cmplx_FATCA_ListedReason", selectedReasonList);
			}
			RLOS.mLogger.info("HIDDEN VALUE IS"+hiddenText);
		}
		else if ("FATCA_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("FATCA_Wi_Name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("FATCA_Wi_Name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FATCA_cmplx_GR_FatcaDetails");
			//added by prabhakar
			formObject.clear("cmplx_FATCA_listedreason");
			formObject.clear("cmplx_FATCA_SelectedReason");
			formObject.addItem("cmplx_FATCA_listedreason", "CORPORATE SHARE HOLDER");
			formObject.addItem("cmplx_FATCA_listedreason", "CURRENT US PHONE NO");
			formObject.addItem("cmplx_FATCA_listedreason", "SIGNATORY HAS A US ADDRESS");
			formObject.addItem("cmplx_FATCA_listedreason", "US PLACE OF BIRTH");
			formObject.addItem("cmplx_FATCA_listedreason", "CURRENT US MAILING ADDRESS OR RESIDENCE ADDRESS");
			formObject.addItem("cmplx_FATCA_listedreason", "POA/SIGNATORY HAS A US ADDRESS");
			RLOS.mLogger.info("FATCA_Button3");
			formObject.setNGValue("cmplx_FATCA_SelectedReasonhidden", "");
		}
		//Added By Prabhakar Drop-4 point-3
		else if ("FATCA_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FATCA_cmplx_GR_FatcaDetails");
			formObject.clear("cmplx_FATCA_listedreason");
			formObject.clear("cmplx_FATCA_SelectedReason");
			formObject.addItem("cmplx_FATCA_listedreason", "CORPORATE SHARE HOLDER");
			formObject.addItem("cmplx_FATCA_listedreason", "CURRENT US PHONE NO");
			formObject.addItem("cmplx_FATCA_listedreason", "SIGNATORY HAS A US ADDRESS");
			formObject.addItem("cmplx_FATCA_listedreason", "US PLACE OF BIRTH");
			formObject.addItem("cmplx_FATCA_listedreason", "CURRENT US MAILING ADDRESS OR RESIDENCE ADDRESS");
			formObject.addItem("cmplx_FATCA_listedreason", "POA/SIGNATORY HAS A US ADDRESS");
			formObject.setNGValue("cmplx_FATCA_SelectedReasonhidden", "");
			RLOS.mLogger.info( "Inside add FATCA_Modify: modify of FATCA details");

		}
		//Added By Prabhakar Drop-4 point-3
		else if ("FATCA_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FATCA_cmplx_GR_FatcaDetails");
			formObject.clear("cmplx_FATCA_listedreason");
			formObject.clear("cmplx_FATCA_SelectedReason");
			formObject.addItem("cmplx_FATCA_listedreason", "CORPORATE SHARE HOLDER");
			formObject.addItem("cmplx_FATCA_listedreason", "CURRENT US PHONE NO");
			formObject.addItem("cmplx_FATCA_listedreason", "SIGNATORY HAS A US ADDRESS");
			formObject.addItem("cmplx_FATCA_listedreason", "US PLACE OF BIRTH");
			formObject.addItem("cmplx_FATCA_listedreason", "CURRENT US MAILING ADDRESS OR RESIDENCE ADDRESS");
			formObject.addItem("cmplx_FATCA_listedreason", "POA/SIGNATORY HAS A US ADDRESS");
			RLOS.mLogger.info( "Inside FATCA_Delete: delete of FATCA details");
			formObject.setNGValue("cmplx_FATCA_SelectedReasonhidden", "");
		}
		//changed by akshay on 5/12/17


		//Arun (01/12/17) modified above code of control name of listed reason & selected reason


		else if ("IncomingDoc_AddFromPCButton".equalsIgnoreCase(pEvent.getSource().getName())){
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
			RLOS.mLogger.info("value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));


		}    


		else if("Reject".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
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
			RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");
				formObject.setNGValue("IS_AECB","Y");
			}
			else{
				formObject.setNGValue("IS_AECB","Y");
			}
			RLOS.mLogger.info(formObject.getNGValue("IS_AECB"));
			//ended
		}

		else  if("DecisionHistory_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS_IntegrationInput.getCustAddress_details();
			String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
			//String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
			String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			RLOS.mLogger.info( "inside create Account/Customer NTB value: "+NTB_flag );
if("Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0))){
			if("true".equalsIgnoreCase(NTB_flag) || "".equalsIgnoreCase(CIF_no)){
				if(!"Y".equals(formObject.getNGValue("Is_Customer_Req")))
				{
					formObject.setNGValue("curr_user_name",formObject.getUserName());
					outputResponse = GenXml.GenerateXML("NEW_CUSTOMER_REQ","PRIMARY_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						formObject.setNGValue("Is_Customer_Req","Y");
						formObject.setNGValue("cmplx_DecisionHistory_CifNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
						RLOS.mLogger.info("Inside if of New customer Req");
						outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						RLOS.mLogger.info(ReturnCode);

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
							RLOS.mLogger.info("New account Req for NTB failed");
							alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL054");

						}
						RLOS.mLogger.info(formObject.getNGValue("Is_Account_Create"));
						RLOS.mLogger.info(formObject.getNGValue("EligibilityStatus"));
						RLOS.mLogger.info(formObject.getNGValue("EligibilityStatusCode"));
						RLOS.mLogger.info(formObject.getNGValue("EligibilityStatusDesc"));
					}
					else{
						RLOS.mLogger.info("New Customer Req for NTB failed");
						alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL053");
					}
				}
				else if("Y".equals(formObject.getNGValue("Is_Customer_Req")) && !"Y".equals(formObject.getNGValue("Is_Account_Create")))
				{
					outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);

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
						RLOS.mLogger.info("New account Req for NTB failed");
						alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL054");
					}
				}
			}
				else
				{
					RLOS.mLogger.info("customer is an existing customer!!");

					String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> AccountCount= formObject.getNGDataFromDataCache(query);
					RLOS.mLogger.info( "Query is: "+query+" currValue In AccountCount is "+AccountCount);
					RLOS.mLogger.info( "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
					int countCurrentAccount =  Integer.parseInt(AccountCount.get(0).get(0));

					if((!"Y".equals(formObject.getNGValue("Is_Account_Create")) && "false".equals(formObject.getNGValue("cmplx_Customer_NTB")) && countCurrentAccount==0)){
						RLOS.mLogger.info("NTB: "+formObject.getNGValue("cmplx_Customer_NTB")+" Account Count:"+formObject.getNGValue("AccountCount"));
					
						outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						RLOS.mLogger.info(ReturnCode);
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
						RLOS.mLogger.info(formObject.getNGValue("Is_Account_Create"));
						if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Account_Create"))){ 
							RLOS.mLogger.info("inside if condition");
							formObject.setEnabled("DecisionHistory_Button5", false);     
						}
						else{
							formObject.setEnabled("DecisionHistory_Button5", true);
						}
					}
					
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}
			else if("Guarantor".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0))){
				RLOS.mLogger.info("Select row data is: "+formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 0));
				outputResponse = GenXml.GenerateXML("NEW_CUSTOMER_REQ","GUARANTOR_CIF");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info( "Guarantor CIf ReturnCode: "+ReturnCode);
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IntegrationSuccess").equalsIgnoreCase(ReturnCode))
				{
					RLOS.mLogger.info("PL DDVT Checker"+ "Guarantor CIf created sucessfully!!!");
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL053");
					String CIf_value=outputResponse.contains("<CIFId>") ? outputResponse.substring(outputResponse.indexOf("<CIFId>")+"</CIFId>".length()-1,outputResponse.indexOf("</CIFId>")):"";
					formObject.setNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 3, CIf_value);
					formObject.setLocked("DecisionHistory_Button2", true);
				}
				else{
				
					alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL019");
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			}


			else   if("ReadFromCIF".equalsIgnoreCase(pEvent.getSource().getName())){

				outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					formObject.setNGValue("Is_Customer_Details_Guarantor","Y");
					RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_Guarantor"));
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Guarantor_CIF");    
					RLOS.mLogger.info("Guarantor_CIF is generated");
					RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_Guarantor"));
					formObject.setNGValue("passExpiry", Convert_dateFormat(formObject.getNGValue("passExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					formObject.setNGValue("dob", Convert_dateFormat(formObject.getNGValue("dob"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					formObject.setNGValue("eidExpiry", Convert_dateFormat(formObject.getNGValue("eidExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					formObject.setNGValue("visaExpiry", Convert_dateFormat(formObject.getNGValue("visaExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
					//code added by saurabh on 22nd July 17.
					if(formObject.getNGValue("dob")!=null && !"".equalsIgnoreCase(formObject.getNGValue("dob")) && !" ".equalsIgnoreCase(formObject.getNGValue("dob"))){
						common.getAge(formObject.getNGValue("dob"), "age");	
					}
				}
				else{
					RLOS.mLogger.info("Customer Details is not generated");
					formObject.setNGValue("Is_Customer_Details_Guarantor","N");
				}
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_Guarantor"));
			}
			//Added for the World check call in case of self employed  
			else if ("FetchWorldCheck_SE".equalsIgnoreCase(pEvent.getSource().getName())) {
				popupFlag="Y";		
				//	columnValues=columnValues.join(",",columnValues_arr);
				formObject.clear("cmplx_WorldCheck_WorldCheck_Grid");
				RLOS.mLogger.info("inside worldcheck"); 
				outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);

				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					RLOS.mLogger.info("inside if of WORLDCHECK");
					formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
					formObject.setVisible("WorldCheck", false);
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"");	
					formObject.setNGValue("IS_WORLD_CHECK","Y");
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL015");
				}
				else if("9999".equalsIgnoreCase(ReturnCode)){
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL016");
					formObject.setNGValue("IS_WORLD_CHECK","Y");
				}
				else{
					formObject.setNGValue("IS_WORLD_CHECK","N");
					RLOS.mLogger.info("inside else of WORLD CHECK");
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL017");
				}
				RLOS.mLogger.info("alert: "+ alert_msg);
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));

			}
			//Added for the World check call in case of self employed

			// added by Akshay for world_check on initiation  
			else if ("WorldCheck_fetch".equalsIgnoreCase(pEvent.getSource().getName())) {
				popupFlag="Y";
				//	columnValues=columnValues.join(",",columnValues_arr);
				formObject.clear("cmplx_WorldCheck_WorldCheck_Grid");
				RLOS.mLogger.info("inside worldcheck"); 
				outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);

				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					RLOS.mLogger.info("inside if of WORLDCHECK");
					formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
					formObject.setVisible("WorldCheck", false);
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"");	
					formObject.setNGValue("IS_WORLD_CHECK","Y");
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL015");
				}
				else if("9999".equalsIgnoreCase(ReturnCode)){
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL016");
					formObject.setNGValue("IS_WORLD_CHECK","Y");
				}
				else{
					formObject.setNGValue("IS_WORLD_CHECK","N");
					RLOS.mLogger.info("inside else of WORLD CHECK");
					alert_msg= NGFUserResourceMgr_RLOS.getAlert("VAL017");
				}
				RLOS.mLogger.info("alert: "+ alert_msg);
				formObject.RaiseEvent("WFSave");
				//changes done to set focus on the same button.
				formObject.setNGFocus("WorldCheck_fetch");
				throw new ValidatorException(new FacesMessage(alert_msg));

			}
			// ended Akshay for world_check initiation


			else if("ELigibiltyAndProductInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
			{		
				popupFlag="Y";
				formObject.setNGValue("DecCallFired","Eligibility");
				String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				int framestate10=formObject.getNGFrameState("WorldCheck");
				if(framestate10 == 0){
					RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
				}
				else {
					formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
					formObject.setVisible("WorldCheck", false);
				}

				int framestate1=formObject.getNGFrameState("Incomedetails");
				if(framestate1 == 0){
					RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
				}
				else {
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
					RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					visibilityFrameIncomeDetails(formObject);
				}
				int framestate2=formObject.getNGFrameState("EmploymentDetails");
				if(framestate2 == 0){
					RLOS.mLogger.info(" EmploymentDetails");
				}
				else {
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
					//change by saurabh on 1St Dec for Tanshu points.
					formObject.setTop("Eligibility_Emp", 460);
					formObject.setTop("WorldCheck_fetch", 500);
					RLOS.mLogger.info(" fetched employment details");
				}
				int framestate3=formObject.getNGFrameState("Liability_container");
				if(framestate3 == 0){
					RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
				}
				else {
					formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
				}

				if ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6))){
					int framestate4=formObject.getNGFrameState("CompanyDetails");
					if(framestate4 == 0){
						RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
					}
					else {
						formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
					}
				}
				if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
					formObject.setNGValue("Winame", formObject.getWFWorkitemName());
				}
				// position of EMI logic shifted to make it run for 1st time
				try{
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

					String EMI=getEMI(LoanAmount,RateofInt,tenor);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI",  EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
					RLOS.mLogger.info("Post EMI value set: "+ formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));

				}
				catch(Exception e){
					RLOS.logException(e);
					RLOS.mLogger.info(" Exception in EMI Generation");
				}
				if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){
					outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(outputResponse);

					if("".equalsIgnoreCase(SystemErrorCode)){
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
						RLOS.mLogger.info("after value set customer for dectech call");
						formObject.RaiseEvent("WFSave");
					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
					}
				}
				else{
					outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

					//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(outputResponse);

					if("".equalsIgnoreCase(SystemErrorCode)){
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
						RLOS.mLogger.info("after value set customer for dectech call");
					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
					}

				}


				//EMI Calcuation logic added below 24-Sept-2017 End
				//deepak Code changes to calculate LPF amount and % 08-nov-2017 start
				try{
					RLOS.mLogger.info("RLOS_Common"+"Inside set event of LPF Data");
					double LPF_charge = 0;
					double Insur_charge = 0;
					//query modified by saurabh on 13th Mar
					List<List<String>> result=formObject.getNGDataFromDataCache("select distinct c.CHARGERATE,I.Insur_chargeRate from NG_MASTER_Charges c with (nolock) left join NG_master_Scheme S with (nolock) on c.ChargeID=S.LPF_ChargeID  left join NG_MASTER_InsuranceCharges I with (nolock) on I.Insur_chargeId= S.Insur_chargeID where S.SCHEMEID='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'");
					RLOS.mLogger.info("RLOS_Common"+ "result of fetch RMname query: "+result); 
					if(result==null || result.equals("") || result.isEmpty()){
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
					RLOS.mLogger.info("RLOS_Common code "+ "result LPF_charge: "+LPF_charge);
					RLOS.mLogger.info("RLOS_Common code "+ "result Insur_charge: "+Insur_charge);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPF", LPF_charge);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Insurance", Insur_charge);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
					double LPF_amount;
					double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					LPF_amount = (final_Loan_amount*LPF_charge)/100;
					RLOS.mLogger.info("RLOS_Common code "+ "result LPF_amount: "+LPF_amount);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPFAmount", LPF_amount);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",false);
					//added b y akshay on 15/3/18 for proc 6396
					double Insuranceamount=(Insur_charge*LPF_amount)/100;
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount", Insuranceamount);
					formObject.setEnabled("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);

				}
				catch(Exception e){
					RLOS.logException(e);
					RLOS.mLogger.info(" Exception in EMI Generation");
				}
				IslamicFieldsvisibility();
				//deepak Code changes to calculate LPF amount and % 08-nov-2017 End
				//change to set focus on the same button.	
				formObject.RaiseEvent("WFSave");
				formObject.setNGFocus("ELigibiltyAndProductInfo_Button1");
				RLOS.mLogger.info(" Final alert msg for front end at ELigibiltyAndProductInfo_Button1 click event: "+alert_msg );
				throw new ValidatorException(new FacesMessage(alert_msg));
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

			}
			else if("Eligibility_Emp".equalsIgnoreCase(pEvent.getSource().getName()))
			{		
				popupFlag="Y";
				formObject.setNGValue("DecCallFired","Eligibility");
				String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				formObject.setVisible("WorldCheck", false);
				//Change Done to fetch Income Details
				int framestate1=formObject.getNGFrameState("Incomedetails");
				if(framestate1 == 0){
					RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
				}
				else {
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
					RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					visibilityFrameIncomeDetails(formObject);
				}
				//Change Done to fetch Income Details
				if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
					formObject.setNGValue("Winame", formObject.getWFWorkitemName());
				}
				if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){
					outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

					//	ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(outputResponse);

					if("".equalsIgnoreCase(SystemErrorCode)){
						RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
						formObject.setSelectedSheet("ParentTab",3);		
						//formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
						//String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
						//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
						//formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);//it is dere at fragment load
						fetch_EligPrd_frag();
						formObject.setNGFrameState("EligibilityAndProductInformation", 0);
						//formObject.setNGFrameState("EligibilityAndProductInformation", 1);
						formObject.setNGFrameState("EligibilityAndProductInformation", 0);

						//Eligibilityfields();
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
						RLOS.mLogger.info("after value set customer for dectech call");
						checkforBPACase();
						formObject.RaiseEvent("WFSave");
					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");

					}
				}
				else{
					outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

					//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(outputResponse);

					if(("".equalsIgnoreCase(SystemErrorCode))&& !"0".equalsIgnoreCase(ReturnCode)){
						RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
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
						RLOS.mLogger.info("after value set customer for dectech call");
						formObject.RaiseEvent("WFSave");

					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");

					}

				}
				//change to set focus on the same button.
				try{
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3)==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

					String EMI=getEMI(LoanAmount,RateofInt,tenor);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);

				}
				catch(Exception e){
					RLOS.mLogger.info("Exception Occured in RLOS Iniitation :  Exception in EMI Generation");
				}
				//EMI Calcuation logic added below 06-nov-2017 End
				formObject.setNGFocus("Eligibility_Emp");
				//change by saurabh on 29th nov 2017 as elig can only be run once.
				formObject.setEnabled("Eligibility_Emp",false);
				throw new ValidatorException(new FacesMessage(alert_msg));
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

			}
			//Added for Dectech Call on Self Employed Case
			else if("CheckEligibility_SE".equalsIgnoreCase(pEvent.getSource().getName()))
			{		
				popupFlag="Y";
				formObject.setNGValue("DecCallFired","Eligibility");
				String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
				formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
				formObject.setTop("AuthorisedSignatoryDetails", formObject.getTop("CompanyDetails")+formObject.getHeight("CompanyDetails")+25);
				formObject.setTop("PartnerDetails", formObject.getTop("AuthorisedSignatoryDetails")+formObject.getHeight("AuthorisedSignatoryDetails")+25);
				//Change Done to fetch Income Details
				int framestate1=formObject.getNGFrameState("Incomedetails");
				if(framestate1 == 0){
					RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
				}
				else {
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
					RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					visibilityFrameIncomeDetails(formObject);
				}
				//Change Done to fetch Income Details
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				formObject.setVisible("WorldCheck", false);
				if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
					formObject.setNGValue("Winame", formObject.getWFWorkitemName());
				}
				if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){
					outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

					//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(outputResponse);

					if("".equalsIgnoreCase(SystemErrorCode)){
						RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
						formObject.setSelectedSheet("ParentTab",3);		
						formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
						String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
						//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);

						formObject.setNGFrameState("EligibilityAndProductInformation", 0);
						formObject.setNGFrameState("EligibilityAndProductInformation", 1);
						formObject.setNGFrameState("EligibilityAndProductInformation", 0);

						Eligibilityfields();
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
						RLOS.mLogger.info("after value set customer for dectech call");
						formObject.RaiseEvent("WFSave");



					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");

					}
				}
				else{
					outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

					//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(outputResponse);

					if("".equalsIgnoreCase(SystemErrorCode)){
						RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
						formObject.setSelectedSheet("ParentTab",3);		
						formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
						String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
						formObject.setNGFrameState("EligibilityAndProductInformation", 0);
						formObject.setNGFrameState("EligibilityAndProductInformation", 1);
						formObject.setNGFrameState("EligibilityAndProductInformation", 0);
						//Eligibilityfields();
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
						RLOS.mLogger.info("after value set customer for dectech call");
						formObject.RaiseEvent("WFSave");



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
				popupFlag="Y";
				formObject.setNGValue("DecCallFired","CalculateDBR");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				formObject.setVisible("WorldCheck", false);
				String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				RLOS.mLogger.info("$$After RequiredProd for dectech call..outputResponse is : "+RequiredProd);
				//Change Done to fetch Income Details
				int framestate1=formObject.getNGFrameState("Incomedetails");
				if(framestate1 == 0){
					RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
				}
				else {
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
					RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					visibilityFrameIncomeDetails(formObject);
				}
				//Change Done to fetch Income Details

				//Change Done to fetch Employment Details on Calculate DBR button
				int EmpDetailframestate=formObject.getNGFrameState("EMploymentDetails_Frame1");
				if(EmpDetailframestate == 0)
				{
					RLOS.mLogger.info("EMploymentDetails_Frame1 frame is expanded");				
				}
				else
				{
					RLOS.mLogger.info("EMploymentDetails_Frame1 frame is not expanded");
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
					//change by akshay on 1St Dec for Tanshu points.
					formObject.setTop("Eligibility_Emp", formObject.getTop("EmploymentDetails")+formObject.getHeight("EmploymentDetails")+20);
					formObject.setTop("WorldCheck_fetch",  formObject.getTop("Eligibility_Emp")+formObject.getHeight("Eligibility_Emp")+20);

				}
				if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
					formObject.setNGValue("Winame", formObject.getWFWorkitemName());
				}
				if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(RequiredProd)){
					outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);
					//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(outputResponse);

					if("".equalsIgnoreCase(SystemErrorCode)){
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
						RLOS.mLogger.info("after value set customer for dectech call");
						formObject.RaiseEvent("WFSave");
					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
					}
				}
				else{
					outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
					RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

					//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
					RLOS.mLogger.info(SystemErrorCode);
					RLOS.mLogger.info(ReturnCode);
					if("".equalsIgnoreCase(SystemErrorCode)){
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL018");
						RLOS.mLogger.info("after value set customer for dectech call");
						formObject.RaiseEvent("WFSave");
					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL019");
					}

				}
				//below code change to set focus and use single through.  
				formObject.setNGFocus("Button2");
				throw new ValidatorException(new FacesMessage(alert_msg));


				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);


				//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);




			}



			else if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("MobileNo",formObject.getNGValue("cmplx_Customer_MobNo"));
				formObject.setNGValue("EmirateID", formObject.getNGValue("cmplx_Customer_EmiratesID"));
				formObject.setNGValue("PassportNo", formObject.getNGValue("cmplx_Customer_PAssportNo"));
				formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNo"));
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_MiddleName")))
				{
					//below id changed
					formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
				}

				else{
					//below id changed
					formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
				}
				formObject.saveFragment("CustomerDetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL020");
				formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName()))
			{			
				formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
				formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Sub_Product"));
				formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));
				formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));

				formObject.saveFragment("ProductDetailsLoader");
				formObject.RaiseEvent("WFSave");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL021");
				throw new ValidatorException(new FacesMessage(alert_msg));

			}


			else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("GuarantorDetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL022");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("Incomedetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL023");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("IncomeDetails_SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("Incomedetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL024");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("CompanyDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
				RLOS.mLogger.info("Company Details---->"+formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,30));
				formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
				formObject.setNGValue("Tl_No", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,6));//added By Akshay on 16/9/17 to set value i ext table column
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
				formObject.saveFragment("CompanyDetails");
				formObject.saveFragment("AuthorisedSignDetails");
				formObject.saveFragment("PartnerDetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL025");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("Liability_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL026");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("EMploymentDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By akshay on 16/9/17 to set header
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));

				formObject.saveFragment("EmploymentDetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL027");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("EligibilityAndProductInformation");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL028");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else 	if("MiscellaneousFields_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("MiscFields");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL029");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("Address_Details_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL030");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("ContactDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("Alt_Contact_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL031");
				formObject.setNGValue("BRANCH", formObject.getNGValue("AlternateContactDetails_custdomicile"));
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else 	if("CardDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("CardDetails_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL032");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("SupplementCardDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("Supplementary_Container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL033");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//formObject.setsel ("cmplx_FATCA_ListedReason");
				formObject.setSelectedAllIndices("cmplx_FATCA_SelectedReason");

				formObject.saveFragment("FATCA");


				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL034");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

		//Added by Prabhakar drop-4 point-3
			else if("KYC_Add".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setNGValue("kyc_Wi_Name",formObject.getWFWorkitemName());
				RLOS.mLogger.info( "Inside KYC add button: "+formObject.getNGValue("kyc_Wi_Name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_KYC_cmplx_KYCGrid");
			}
			//Added by Prabhakar drop-4 point-3
			else if("KYC_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_KYC_cmplx_KYCGrid");
				RLOS.mLogger.info("Inside KYC Modify Button"+formObject.getNGValue("kyc_Wi_Name"));
			}
			//Added by Prabhakar drop-4 point-3
			else if("KYC_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_KYC_cmplx_KYCGrid");
				RLOS.mLogger.info("Inside KYC Modify Button"+formObject.getNGValue("kyc_Wi_Name"));
			}
			//Modified by Prabhakar drop-4 point-3
			else if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				RLOS.mLogger.info( "Inside KYC save button");
				formObject.saveFragment("KYC");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL035");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("OECD");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL036");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("ReferenceDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("ReferenceDetails");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL037");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("ServiceRequest_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("CC_Loan_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL038");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("BTC_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
				formObject.saveFragment("CC_Loan_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL039");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if("CC_Loan_DDS_save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("CC_Loan_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL040");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if("CC_Loan_SI_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
				formObject.saveFragment("CC_Loan_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL041");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if("CC_Loan_RVC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("CC_Loan_container");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL042");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("IncomingDoc_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				RLOS.mLogger.info( "TANSHU Inside IncomingDoc_Save button!!");
				formObject.saveFragment("IncomingDocuments");
				popupFlag = "Y";
				//IncomingDoc();
				RLOS.mLogger.info( "TANSHU Inside IncomingDoc_Save button!! after incoming doc function");
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL043");
				formObject.RaiseEvent("WFSave");// added by Aman to use the product save because custom save is not working
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("DecisionHistory_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.saveFragment("DecisionHistoryContainer");
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
				formObject.saveFragment("Notepad_Values");
				popupFlag = "Y";
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL045");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			// added by abhishek as per rlos FSD 


			else if("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("NotepadDetails_WiNote",formObject.getWFWorkitemName());
				RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("NotepadDetails_WiNote"));					
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_add();
			}
			else if("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_modify();
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


				RLOS.mLogger.info( "query is: "+query);
				populatePickListWindow(query,"state", "Description,Code", true, 20);			     

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


				RLOS.mLogger.info( "query is: "+query);
				populatePickListWindow(query,"city", "Description,Code", true, 20);			     

		}
		else if ("cmplx_DecisionHistory_MultipleApplicantsGrid".equalsIgnoreCase(pEvent.getSource().getName())){
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
		}
		
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

				RLOS.mLogger.info( "query is: "+query);
				populatePickListWindow(query,"OECD_townBirth", "Description,Code", true, 20);			     

			}


			else{
				RLOS.mLogger.info( " No condition reached in mouse click event");
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
			RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			Common_Utils common=new Common_Utils(RLOS.mLogger);

			/*if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName()))
	{
		RLOS.mLogger.info("RLOS val ..!. Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
		common.getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
	}
			// Code added to Load targetSegmentCode on basis of code
			 */	



			if ("ApplicationCategory".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				RLOS.mLogger.info("RLOS val change Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
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
			// Code added to Load targetSegmentCode on basis of code

			//code added for LOB
			if ("estbDate".equalsIgnoreCase(pEvent.getSource().getName())){
				//change by saurabh on 29th Nov 17 for Drop2 CR.
				RLOS.mLogger.info( "Value of estbDate is:"+formObject.getNGValue("estbDate"));
				common.getAge(formObject.getNGValue("estbDate"),"lob");
			}
			//code added for LOB
			//code by saurabh on 28th Nov 2017 for calculating EMI after change in interest rate or tenor
			else if("cmplx_EligibilityAndProductInfo_InterestRate".equalsIgnoreCase(pEvent.getSource().getName()) || "cmplx_EligibilityAndProductInfo_Tenor".equalsIgnoreCase(pEvent.getSource().getName())){

				try{
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

					String EMI=getEMI(LoanAmount,RateofInt,tenor);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
					if(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")!=null && !formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					}
				}
				catch(Exception e){
					RLOS.logException(e);
					RLOS.mLogger.info(" Exception in EMI Generation");
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
			else if ("cmplx_EligibilityAndProductInfo_FirstRepayDate".equalsIgnoreCase(pEvent.getSource().getName())){
				RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"));
				String AgeAtMAturity=getYearsDifference(formObject,"cmplx_Customer_DOb","cmplx_EligibilityAndProductInfo_MaturityDate");
				RLOS.mLogger.info( "Value of dob is:"+AgeAtMAturity);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_ageAtMaturity", AgeAtMAturity);

			}
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
				loadPicklist_TargetSegmentCode();
			}

			else if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
				String ReqProd=formObject.getNGValue("ReqProd");
				RLOS.mLogger.info( "Value of ReqProd is:"+ReqProd);
				loadPicklistProduct(ReqProd);
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){
					formObject.setVisible("AlternateContactDetails_retainAccount",false);
				}
				else{
					formObject.setVisible("AlternateContactDetails_retainAccount",true);
				}
			}


			else if ("subProd".equalsIgnoreCase(pEvent.getSource().getName())){
				RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("subProd"));
				formObject.clear("AppType");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("subProd")+"' order by code");
				String subprod=formObject.getNGValue("subProd");
				String VIPFlag=formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N";
				String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
				String req_prod=formObject.getNGValue("ReqProd");
				String TypeOfProd=formObject.getNGValue("Product_type");
				RLOS.mLogger.info( "Value of SubProd is:"+subprod);
				RLOS.mLogger.info( "Value of vip is:"+VIPFlag);
				RLOS.mLogger.info( "Value of Nationality is:"+Nationality);
				RLOS.mLogger.info( "Value of req_prod is:"+req_prod);
				RLOS.mLogger.info( "Value of TypeOfProd is:"+TypeOfProd);

				if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(req_prod)){
					if("PU".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("Product_Label6",true);//Arun (06/12/17) to hide this field
						formObject.setVisible("CardProd",true);//Arun (06/12/17) to hide this field
						formObject.setVisible("CardDetails_container",false);//Arun (06/12/17) to hide this fragment
						formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable

						formObject.clear("CardProd");
						RLOS.mLogger.info( " is not BTC ,PA :"+subprod);

						//Deepak Code change to load card product with new master.
						LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");
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
						formObject.setVisible("CardDetails_container",false);//Arun (06/12/17) to hide this fragment
						formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable

						formObject.clear("CardProd");
						RLOS.mLogger.info( " is not BTC ,PA :"+subprod);

						//Deepak Code change to load card product with new master.
						LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");
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
						RLOS.mLogger.info( " is not BTC ,PA :"+subprod);

						//Deepak Code change to load card product with new master.
						LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");
						/*if("Conventional".equalsIgnoreCase(TypeOfProd)){
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%' order by code");
					}
					else{
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%' order by code");
					}*/
					}

					else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(subprod))
					{
						formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",false);
					}//Arun (06/12/17) to make editable 

					else if("SEC".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("CardProd",true);

					}//Arun 19/12/17

					/*else if("IM".equalsIgnoreCase(subprod))
				{
					formObject.setVisible("CardProd",true);
				}//Arun 19/12/17
					 */
					else if("PA".equalsIgnoreCase(subprod))
					{
						formObject.setVisible("Product_Label6",true);
						formObject.setVisible("CardProd",true);
					}//Arun 19/12/17

					else{
						//added by akshay on 20/11/17 so select sal or SALP in emptype

						RLOS.mLogger.info( "subProd is not PU or LI"); 
						if("IM".equalsIgnoreCase(subprod) || "SAL".equalsIgnoreCase(subprod) || "BPA".equalsIgnoreCase(subprod))
						{
							RLOS.mLogger.info("subProd is IM/SAL/BPA");
							//formObject.setVisible("Product_Label6",true);//Arun (06/12/17) to show this field
							//formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
							formObject.clear("EmpType");
							LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where code  like 'sal%' and IsActive='Y' order by code");
							formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable
						}
						else{
							//LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where code not like 'sal%' order by code");
							LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where IsActive='Y' order by code"); //Arun (05/12/17) modified to load the emplyment type masters correctly

							formObject.setNGValue("EmpType", NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed"));
						}

						formObject.clear("CardProd");
						String query1="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE' order by code";	
						String query2="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality is null order by code"; //Arun (05/12/17) modified to load the corret masters if nationality is null
						RLOS.mLogger.info( ": "+Nationality);
						RLOS.mLogger.info(query1);
						if("AE".equalsIgnoreCase(Nationality)){
							RLOS.mLogger.info(query1);
							LoadPickList("CardProd",query1);
						}
						else{
							RLOS.mLogger.info(query2);
							LoadPickList("CardProd",query2);
						}
					}
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
				RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
				RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("AppType"));
				RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("Product_type"));

				if ("LI".equalsIgnoreCase(subprod) || "PULI".equalsIgnoreCase(subprod)){

					RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
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

				/*else if ("PULI".equalsIgnoreCase(subprod)&&("NPULI".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.setVisible("Product_Label9",true);
				formObject.setVisible("LastTemporaryLimit",true);
				formObject.setVisible("Product_Label7",true);
				formObject.setVisible("LastPermanentLimit",true);
				//changed by akshay on 2/1/18
				formObject.setVisible("Product_Label10",true);
				formObject.setVisible("NoOfMonths",true);
			} //Arun (06/12/17) for Application type validation in product


				else if (("Conventional".equalsIgnoreCase(TypeofProduct))&& "IM".equalsIgnoreCase(subprod)){
				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.setVisible("Product_Label9",false);
				formObject.setVisible("LastTemporaryLimit",false);
				formObject.setVisible("Product_Label7",false);
				formObject.setVisible("LastPermanentLimit",false);
			} 

			else if (("Conventional".equalsIgnoreCase(TypeofProduct))&& "IM".equalsIgnoreCase(subprod)){
				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.setVisible("Product_Label9",false);
				formObject.setVisible("LastTemporaryLimit",false);
				formObject.setVisible("Product_Label7",false);
				formObject.setVisible("LastPermanentLimit",false);
			}	
			//modified by aksgay on 2/1/18
				 */

				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& "IM".equalsIgnoreCase(subprod)&&("TOPIM".equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.setVisible("Product_Label9",false);
					formObject.setVisible("LastTemporaryLimit",false);
					formObject.setVisible("Product_Label7",false);
					formObject.setVisible("LastPermanentLimit",false);
				} 


				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("TKOE".equalsIgnoreCase(apptype))){


					RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");




				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));




					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");




				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));

					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");
				}

				//changed by akshay for rechedulement case on 7/2/18	
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));

					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPN").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TKON").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NEWN").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod) &&("TKOE".equalsIgnoreCase(apptype))){


					RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");				
				}

				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");				
				}

				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPN").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TKON").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NEWN").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


				}
				else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
					RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
					formObject.clear("Scheme");
					formObject.setNGValue("Scheme","--Select--");
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");

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
				if(!"".equalsIgnoreCase(formObject.getNGValue("Scheme"))){
					String query="select top 1 mintenure,maxtenure from NG_master_Scheme  with (nolock) where schemeid='"+formObject.getNGValue("Scheme")+"'";
					List<String> control_list=Arrays.asList("minTenor","maxTenor");
					formObject.getDataFromDataSource(query, control_list);
				}
			}
			//changed by akshay on 8th nov 2017 for loadpicklist.
			else if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){

				RLOS.mLogger.info( "$Indus Sector$:" +formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector"));
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);//it is unlocked from js but its instance state is saved as locked as it was locked on fragment load
				LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
			}

			else if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
				RLOS.mLogger.info( "$Indus Macro$:" +formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
				LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y' ) new_table order by case  when micro ='--Select--' then 0 else 1 end");
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

				String ProdType=formObject.getNGValue("Product_type");
				RLOS.mLogger.info(ProdType);
				formObject.clear("CardProd");
				formObject.setNGValue("CardProd","--Select--");
				//Deepak Code change to load card product with new master.
				LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+ProdType+"' order by code");
				/*if("Conventional".equalsIgnoreCase(ProdType))
				 * LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' order by code");
		else if("Islamic".equalsIgnoreCase(ProdType))
			LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");*/
			}

			// added by abhishek point 44
			/*else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others")))
			{
				formObject.setLocked("EMploymentDetails_Text21", true);
				formObject.setLocked("EMploymentDetails_Text22", true);

			}
			else
			{
				formObject.setLocked("EMploymentDetails_Text21", false);
				formObject.setLocked("EMploymentDetails_Text22", false);
			}
		}*/

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



			}
			
			else if("cmplx_CardDetails_SuppCardReq".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_Frame1")+30);
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
			RLOS.mLogger.info("Inside RLOSCommoncode-->DisableFragmentsOnLoad()");
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

			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");

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
				LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to					
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
				RLOS.mLogger.info("loadInDecGrid");
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
			//changed done to handel Exception in this method, Only try catch block is added along with some null pointer check
			try{
				formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
				formObject.setNGFrameState("CustomerDetails", 0);

				//String cardnotavailable=formObject.getNGValue("cmplx_Customer_CardNotAvailable");
				String MobNo=formObject.getNGValue("cmplx_Customer_MobNo");
				RLOS.mLogger.info("MobNo"+MobNo);
				String OTPMobNo=formObject.getNGValue("OTP_Mobile_NO");
				RLOS.mLogger.info("OTPMobNo"+OTPMobNo);
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
					formObject.setLocked("cmplx_Customer_NEP", true);
					formObject.setLocked("ReadFromCard", true);
					formObject.setLocked("Customer_CheckBox6", true);
					formObject.setLocked("cmplx_Customer_CIFNO", true);
					formObject.setLocked("FetchDetails", true);
					RLOS.mLogger.info("Value of customer_Req after locking all the fields");	
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
	}



