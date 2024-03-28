/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_DDVT_maker.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                                                                                                                        : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

1.                 9-6-2017      Disha         Changes done to auto populate age in world check fragment
2.                                                               9-6-2017      Disha         Changes done to load master values in world check birthcountry and residence country
------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_DDVT_maker extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;

	Logger mLogger=PersonalLoanS.mLogger;


	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
		objConfig.getM_objConfigMap().put("PartialSave", "true");
		makeSheetsInvisible("Tab1", "8,9,13,14,15,16");//Changes done by shweta for jira #1768 to hide CPV tab
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setSheetVisible(tabName, 10, true);	// Show Document Tab
	
	}


	public void formPopulated(FormEvent pEvent) 
	{  
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent); 

			//code changes by bandana starts
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");//added By Akshay on 1/8/2017 to set Mob No 2 in part match
			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			formObject.setNGFrameState("Part_Match",0);
			formObject.setLocked("PartMatch_mno1",true);// PCASi 3245 start
			formObject.setLocked("PartMatch_mno2",true);// PCASi 3245
			formObject.setLocked("PartMatch_fname",true);
			formObject.setLocked("PartMatch_lname",true);
			formObject.setLocked("PartMatch_funame",true);
			formObject.setLocked("PartMatch_newpass",true);
			formObject.setLocked("PartMatch_oldpass",true);
			formObject.setLocked("PartMatch_visafno",true);
			formObject.setLocked("PartMatch_EID",true);
			formObject.setLocked("PartMatch_drno",true);
			formObject.setLocked("PartMatch_CIFID",true);
			formObject.setLocked("PartMatch_nationality",true);// end
			//code changes by bandana ends
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("DDVT_Frame_Name"));
			new PersonalLoanSCommonCode().setFormHeader(pEvent);

			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
				formObject.setVisible("Card_Details", true);
				}
			PersonalLoanS.mLogger.info("value of NTB "+formObject.getNGValue("NTB"));
			if (formObject.getNGValue("NTB").equalsIgnoreCase("true")){
				formObject.setNGValue("cmplx_Customer_NTB", "true");
				formObject.setEnabled("cmplx_Customer_card_id_available", false);
				formObject.setLocked("cmplx_Customer_card_id_available", true);
			}
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Constitution"))||"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Constitution")) || null == (formObject.getNGValue("cmplx_Customer_Constitution")) ){
				//cmplx_Customer_Constitution: Individuals - Residents
				formObject.setNGValue("cmplx_Customer_Constitution", "Individuals - Residents");
			}//By Alok for Constitution on 28/10/21
			formObject.setEnabled("cmplx_Customer_RM_TL_NAME", true);	//PCASI - 2694
			formObject.setEnabled("TLRM_Button", true);	//PCASI - 2694
			formObject.setEnabled("cmplx_Customer_Sourcing_Branch_Code", true);//PCASI-3589 by Alok
			formObject.setEnabled("Sourcing_Branch_Code_button", true);//PCASI-3589 by Alok
			//PCASI - 2694
			String sRMNameHeader = formObject.getNGValue("RM_Name");
			String cmplxRMName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			if("".equalsIgnoreCase(cmplxRMName) || cmplxRMName == null){
				formObject.setNGValue("cmplx_Customer_RM_TL_NAME", sRMNameHeader);
			}
			PersonalLoanS.mLogger.info("DDVT Maker sRMNameHeader :: "+sRMNameHeader+" cmplxRMName :: "+cmplxRMName);
			//PCASI - 3553
			if("AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")) || "AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_SecNationality")))
			{
				formObject.setLocked("cmplx_Customer_VisaNo",false);
				formObject.setLocked("cmplx_Customer_VisaIssueDate",false);
				formObject.setLocked("cmplx_Customer_VIsaExpiry",false);
				formObject.setLocked("cmplx_Customer_marsoomID",false);
				formObject.setEnabled("cmplx_Customer_marsoomID",true); //2666
				formObject.setEnabled("cmplx_Customer_VisaNo",true);
				formObject.setEnabled("cmplx_Customer_VisaIssueDate",true);
				formObject.setEnabled("cmplx_Customer_VIsaExpiry",true);
			}//by Alok on 01/11/2021 to make editable visa details

			//String gcc = "BH,IQ,KW,OM,QA,SA,AE"; PCASI GCC - 3518 Hritik 
			String nation = formObject.getNGValue("cmplx_Customer_Nationality");
			PersonalLoanS.mLogger.info("Nation:"+nation);
			if("SA".equalsIgnoreCase(nation) ||"QA".equalsIgnoreCase(nation)
					||"OM".equalsIgnoreCase(nation)||"KW".equalsIgnoreCase(nation)||"IQ".equalsIgnoreCase(nation)
					||"BH".equalsIgnoreCase(nation))
			{
				PersonalLoanS.mLogger.info("Inside if of GCC");
				formObject.setLocked("cmplx_Customer_VisaNo",false);
				formObject.setLocked("cmplx_Customer_VisaIssueDate",false);
				formObject.setLocked("cmplx_Customer_VIsaExpiry",false);
				PersonalLoanS.mLogger.info("Visa disabled");
			}



		}catch(Exception e)
		{
			printException(e);
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      

	 ***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("In PL DDVT MAKER FRAGMENT_LOADED eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			//code merge with CC bandana starts
			if("".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				LoadView(pEvent.getSource().getName());
				//formObject.setLocked("Customer_Frame1",true);
				formObject.setLocked("Customer_save",false);					
				formObject.setLocked("Customer_Frame3",false);
				formObject.setVisible("Customer_Frame2", false);
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
				// ++ below code not commented at offshore - 06-10-2017
				formObject.setLocked("cmplx_Customer_MobNo",true);

				//code merge with CC bandana ends
			}
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Constitution"))||"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Constitution"))){
				//cmplx_Customer_Constitution: Individuals - Residents
				formObject.setNGValue("cmplx_Customer_Constitution", "Individuals - Residents");
			}//By Alok for Constitution on 02/11/21
			
			formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
			formObject.setLocked("cmplx_Customer_NEP",true);
			//formObject.setLocked("cmplx_Customer_marsoomID",true);
			formObject.setLocked("Customer_Button1",true);
			formObject.setLocked("cmplx_Customer_age",true);
			formObject.setLocked("cmplx_Customer_SecNationality", true);
			formObject.setLocked("cmplx_Customer_Third_Nationaity", true);
			if("No".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable"))){
				formObject.setLocked("SecNationality_Button",true);
			}
			else{
				formObject.setLocked("SecNationality_Button",true);
			}
			if("No".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Third_Nationaity_Applicable"))){
				formObject.setLocked("Third_Nationality_Button",true);
			}
			else{
				formObject.setLocked("Third_Nationality_Button",false);
			}

			//By Alok for Third Nationality
			formObject.setLocked("cmplx_Customer_GCCNational",true);
			//below code by saurabh on 11 nov 2017.
			formObject.setVisible("Customer_Label32",false);
			formObject.setVisible("cmplx_Customer_corpcode",false);
			loadPicklistCustomer();
			//code merge with CC bandana starts
			//added by abhishek on 29may2017

			formObject.setLocked("cmplx_Customer_EmiratesID",false);
			formObject.setLocked("cmplx_Customer_FirstNAme",false);
			formObject.setLocked("cmplx_Customer_MiddleNAme",false);
			formObject.setLocked("cmplx_Customer_LastNAme",false);
			// ++ below code not commented at offshore - 06-10-2017
			//formObject.setLocked("cmplx_Customer_DOb",false);
			formObject.setEnabled("cmplx_Customer_DOb",true);
			formObject.setLocked("Nationality_Button",false);
			// ++ below code commented at offshore - 06-10-2017
			formObject.setLocked("cmplx_Customer_MobNo",true);
			formObject.setLocked("cmplx_Customer_PAssportNo",false);
			formObject.setLocked("cmplx_Customer_card_id_available",false);
			formObject.setLocked("Customer_FetchDetails",false);
			formObject.setLocked("cmplx_Customer_card_id_available",false);
			if(	NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_card_id_available"))){
				formObject.setLocked("cmplx_Customer_CIFNO",false);
			}
			//++Below code added by nikhil 22/11/2017 
			formObject.setLocked("cmplx_Customer_DOb",false);
			formObject.setLocked("cmplx_Customer_IdIssueDate", false);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", false);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", false);
			formObject.setLocked("cmplx_Customer_VIsaExpiry", false);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", false);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
			formObject.setLocked("cmplx_Customer_MotherName", false);
			formObject.setLocked("cmplx_Customer_IsGenuine",true);
			//--Above code added by nikhil 22/11/2017 
			// added for point 28
			formObject.setVisible("Customer_ReadFromCard",false);
			// added by abhishek acc. to new FSD for CC
			setcustomer_enable();
			//below code added by nikhil 08/12/17

			// hritik 20.6.21 PCASI3417
			if("AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality"))){
				formObject.setLocked("cmplx_Customer_marsoomID",false);
			}

			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_Customer_MobNo",true);
				formObject.setLocked("FetchDetails",true);
				// hritik 20.6.21 PCASI3366
				formObject.setNGValue("cmplx_Customer_bankwithus","No");
			}
			else
			{
				formObject.setLocked("cmplx_Customer_MobNo",true);
				formObject.setLocked("FetchDetails",false);	

			}
			formObject.setLocked("FetchDetails",true);	
			formObject.setLocked("cmplx_Customer_NTB",true);
			formObject.setVisible("Customer_FetchFirco", true);// for pcasi-3355
			//code merge with CC bandana ends
			PersonalLoanS.mLogger.info("Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
			//below code added by nikhil 08/12/17
			//below condition commented by deepak for PCASI-3245
			/*if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_Customer_MobNo",false);
			}
			else
			{*/
			formObject.setLocked("cmplx_Customer_MobNo",true);
			formObject.setLocked("cmplx_Customer_CIFNO",true);

			//}
			//below code added by nikhil 
			formObject.setLocked("cmplx_Customer_IsGenuine",true);
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				//formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
			if(isCustomerMinor(formObject)){
				formObject.setNGValue("cmplx_Customer_minor", true);
				//commented by bandana as minor not allowed for PL
				//un-commented by shweta as minors are allowed in PL
				formObject.setVisible("Customer_Label37",true);
				formObject.setVisible("Customer_Label35",true);
				formObject.setVisible("cmplx_Customer_guarname",true);
				formObject.setVisible("cmplx_Customer_guarcif",true);
			} else {
				formObject.setNGValue("cmplx_Customer_minor", false);
			}
			formObject.setLocked("cmplx_Customer_minor", true);

			if(formObject.getNGValue("sig_docindex").equals("")){
				String query="select isnull(DocIndex,'') from ng_rlos_gr_incomingDocument with(nolock) where DocumentName='Signature' and IncomingDocGR_Winame = '"+formObject.getWFWorkitemName()+"'";
				List<List<String>> signature_index=formObject.getDataFromDataSource(query);
				formObject.setNGValue("sig_docindex", signature_index.get(0).get(0));
			}
			formObject.setVisible("Customer_FetchFirco", true);//pcasi-3355

		}
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {                                                                                                
			LoadPickList("Product_type", "select '--Select--' union select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with(nolock) where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"' order by code");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			formObject.setLocked("EmpType", true);
			formObject.setLocked("Scheme", true);
			formObject.setEnabled("subProd",true);
			formObject.setLocked("subProd",true);
			formObject.setEnabled("AppType",true);
			formObject.setLocked("AppType",true);
			formObject.setVisible("CardProd",true);
			formObject.setEnabled("CardProd",true);
			formObject.setVisible("EmpType",true);
			formObject.setEnabled("EmpType",true);

			formObject.setEnabled("Product_type",false);//added by bandana for code merge
			//loadPicklistProduct("Personal Loan");
			//added by bandana for code merge
			int prod_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prod_count>0){
				String sub_prd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2);
				if(("SEC").equalsIgnoreCase(sub_prd)){
					formObject.setVisible("Product_Label8",true);
					formObject.setVisible("FDAmount",true);
					//formObject.setVisible("Product_Label6",true);//Arun 18/12/17
					//formObject.setVisible("CardProd",true);//Arun 18/12/17

				}
			}
			formObject.setLocked("Product_Frame1",true);
			//added by bandana for code merge end
		}


		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Guarantor();
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1", true);//Arun (11/9/17)
			formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
			formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
			formObject.setLocked("cmplx_IncomeDetails_TotSal", true);

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

			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			formObject.setVisible("cmplx_IncomeDetails_compaccAmt", false);//IncomeDetails_Label16
			formObject.setVisible("IncomeDetails_Label16", false);
			formObject.setVisible("cmplx_IncomeDetails_Totavgother", false);
			formObject.setVisible("IncomeDetails_Label15", false);
		}

		else if ("IncomeDEtails".equalsIgnoreCase(pEvent.getSource().getName())) {

			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
			PersonalLoanS.mLogger.info( "Emp Type Value is:"+EmpType);

			if("Salaried".equalsIgnoreCase(EmpType)|| "Pensioner".equalsIgnoreCase(EmpType)|| NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(EmpType))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 630);
				formObject.setHeight("IncomeDetails_Frame1", 605);      
				if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
					formObject.setVisible("IncomeDetails_Label11", false);
					formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", false);
					formObject.setVisible("IncomeDetails_Label13", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", false);
					formObject.setVisible("IncomeDetails_Label3", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", false);
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

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(EmpType))
			{                                                                                                              
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 300);
				formObject.setHeight("IncomeDetails_Frame1", 280);
				if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
					formObject.setVisible("IncomeDetails_Label20", false);
					formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", false);
					formObject.setVisible("IncomeDetails_Label22", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", false);
					formObject.setVisible("IncomeDetails_Label35", false);
					formObject.setVisible("IncomeDetails_Label5", false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", false);
					formObject.setVisible("IncomeDetails_Label36", true);
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

		}                              
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");


		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1", true);
			formObject.setEnabled("ExtLiability_Button1",true);
			formObject.setLocked("ExtLiability_Button1",false);
			formObject.setLocked("takeoverAMount",true);
			formObject.setLocked("cmplx_Liability_New_DBR",true);
			formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			formObject.setLocked("cmplx_Liability_New_TAI",true);
			formObject.setVisible("cmplx_Liability_New_overrideAECB", false);//need to check
			formObject.setVisible("cmplx_Liability_New_overrideIntLiab", false);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_contract_type with (nolock) where isactive='Y' order by code");
			formObject.setEnabled("ExtLiability_Takeoverindicator",true);
			formObject.setLocked("ExtLiability_Takeoverindicator",false);
			formObject.setLocked("ExtLiability_AECBReport",false); //hritik natesh point
		}



		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
			formObject.setLocked("cmplx_EmploymentDetails_ApplicationCateg",false);
			formObject.setLocked("cmplx_EmploymentDetails_targetSegCode",false);
			formObject.setLocked("cmplx_EmploymentDetails_marketcode",false);
			formObject.setLocked("cmplx_EmploymentDetails_PromotionCode",false);
			formObject.setLocked("cmplx_EmploymentDetails_MIS",false);
			formObject.setEnabled("EMploymentDetails_Button2",true);
			formObject.setLocked("cmplx_EmploymentDetails_NepType",false);
			formObject.setLocked("cmplx_EmploymentDetails_actualworkemp", false);
			// hritik 17.6.21 -3069
			formObject.setLocked("cmplx_EmploymentDetails_channelcode",false);


			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){

				PersonalLoanS.mLogger.info("inside NEP if condition");
				formObject.setLocked("EMploymentDetails_Label25", false);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", false);
			}
			else{
				PersonalLoanS.mLogger.info("inside NEP else condition");
				formObject.setLocked("EMploymentDetails_Label25", true);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
			}

			//change by saurabh on 11 nov 17.
			//Change by Vishu PCASI-2695 on 25th april 21.
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",false); 
			formObject.setLocked("cmplx_EmploymentDetails_EmpName",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatus",false);
			formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
			formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
			formObject.setLocked("EMploymentDetails_Designation_button",false);
			formObject.setLocked("EMploymentDetails_Designation_button_View",false);
			formObject.setEnabled("cmplx_EmploymentDetails_Designation", true);
			formObject.setEnabled("EMploymentDetails_Designation_button", true);
			formObject.setEnabled("EMploymentDetails_Designation_button_View", true);//done by Alok on 28/10/21
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);

			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);

			formObject.setEnabled("EMploymentDetails_Save",true);
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setVisible("cmplx_EmploymentDetails_NepType",false);	
				formObject.setVisible("EMploymentDetails_Label25",false);
			}
			Fields_ApplicationType_Employment();
			formObject.setLocked("EMploymentDetails_Text21", false);
			formObject.setLocked("EMploymentDetails_Text22", false);
			formObject.setEnabled("EMploymentDetails_Text21", true);
			formObject.setEnabled("EMploymentDetails_Text22", true);
			formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
			formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
			formObject.setEnabled("cmplx_EmploymentDetails_Remarks_PL",false);
			formObject.setEnabled("cmplx_EmploymentDetails_remarks",false);



			//below code added for proc-8095
			formObject.setVisible("EMploymentDetails_Label28",true);
			formObject.setVisible("EMploymentDetails_Label4",true);
			formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("EMploymentDetails_Label6",false);
			formObject.setVisible("cmplx_EmploymentDetails_StaffID",false);
			formObject.setVisible("cmplx_EmploymentDetails_Dept",false);
			formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate",false);
			loadPicklist4();

			//code sync
			//formObject.setVisible("WorldCheck_status",true);
			//formObject.setVisible("WorldCheck_fetch",true);
			AECBHistMonthCnt();
		}



		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadEligibilityData();
			loadPicklistELigibiltyAndProductInfo();

			loadEligibilityData();
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
			//change by saurabh on 11 nov 2017.
			formObject.setEnabled("ELigibiltyAndProductInfo_Button1", false);
			//++below code added by nikhil 5/12/17 for ddvt issues
			formObject.setTop("ELigibiltyAndProductInfo_Save", 150);
			formObject.setLeft("ELigibiltyAndProductInfo_Button1", 54);
			formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EFCHidden", false);

			//below code added by jnikhil 12/12/17
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalDBR", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalTAI", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalLimit", false);

			formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestRate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_promo_interest_rate", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_EMI", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_Tenor", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Refresh",false);

			//Changes done by vishu for PCASI-3252
			formObject.setVisible("ELigibiltyAndProductInfo_Label10", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_takeoverBank",false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_takeoverBank",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InstrumentType",false); //pcasi3365

		}

		//account_number
		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();	
			Common_Utils common=new Common_Utils(mLogger);
			formObject.setLocked("LoanDeatils_calculateemi",true);
			formObject.setEnabled("LoanDeatils_calculateemi", false);
			//added By Tarang for drop 4 point 8 started on 08/03/2018
			//changed by akshay on 9/5/18 for proc 9240
			//String qry = "select VATDESC,VATRATE from  ng_MASTER_LoanVAT union select SCHEMEDESC,MAXAMTFIN from NG_master_Scheme where SCHEMEID='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,7)+"' order by VATDESC";
			String qry = "select VATDESC,VATRATE from  ng_MASTER_LoanVAT with(nolock) union SELECT distinct mincap,maxcap FROM NG_MASTER_Charges with(nolock) WHERE CHARGEDESC='Loan Processing Fee' union select SCHEMEDESC,MAXAMTFIN from NG_master_Scheme with(nolock) where SCHEMEID='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,7)+"'";
			PersonalLoanS.mLogger.info("query to load VAT and Max Loan AMount is"+qry +"");
			List<List<String>> record = formObject.getNGDataFromDataCache(qry);
			PersonalLoanS.mLogger.info("Result is: "+record);
			if( record.get(0)!=null && !record.isEmpty()){

				for(int i=0;i<record.size();i++){
					PersonalLoanS.mLogger.info("Value of record.get(i).get(0) "+record.get(i).get(0));
					if("Loan Processing VAT".equalsIgnoreCase(record.get(i).get(0))){
						int loanProcPercent=Integer.parseInt(record.get(i).get(1));
						formObject.setNGValue("cmplx_LoanDetails_LoanProcessingVATPercent", loanProcPercent);
					}
					else if("Insurance VAT".equalsIgnoreCase(record.get(i).get(0))){
						int insurancePercent=Integer.parseInt(record.get(i).get(1));
						formObject.setNGValue("cmplx_LoanDetails_InsuranceVATPercent", insurancePercent);
					}
					else if(isNumeric(record.get(i).get(0))){
						formObject.setNGValue("LoanDetails_MinLPF", record.get(i).get(0));
						formObject.setNGValue("LoanDetails_MaxLPF", record.get(i).get(1));
					}
					else {
						formObject.setNGValue("LoanDetails_MaxLoanAmount", record.get(i).get(1));//added by akshay on 9/5/18 for proc 9240
					}
				}
			}

			//loanvalidate();//modified by akshay on 14/10/17 for displaying values in loan details
			//added By Tarang for drop 4 point 8 ended on 08/03/2018
			String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");//Arun (21/09/17)
			String mName = formObject.getNGValue("cmplx_Customer_MiddleName");//Arun (21/09/17)
			String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");//Arun (21/09/17)
			String fullName = fName+" "+mName+" "+lName; //Arun (21/09/17)
			formObject.setNGValue("cmplx_LoanDetails_name",fullName);//Arun (21/09/17)
			formObject.setLocked("cmplx_LoanDetails_name",true);//Arun (21/09/17)
			formObject.setLocked("cmplx_LoanDetails_fdisbdate",false);//Arun (22/09/17)
			formObject.setLocked("cmplx_LoanDetails_frepdate",false);//Arun (22/09/17)
			formObject.setNGValue("LoanDetails_benefname", fullName);
			formObject.setLocked("LoanDetails_benefname",true);
			formObject.setEnabled("LoanDetails_disbdate",true);
			formObject.setLocked("LoanDetails_disbdate",false);
			formObject.setLocked("LoanDetails_payreldate",false);
			formObject.setEnabled("LoanDetails_payreldate",true);
			formObject.setLocked("cmplx_LoanDetails_lpfamt",false);//jahnavi 10-05-2021
			formObject.setEnabled("cmplx_LoanDetails_lpfamt",true);

			//below code added by nikhil 12/12/17
			if("".equals(formObject.getNGValue("cmplx_LoanDetails_status"))){
				formObject.setNGValue("cmplx_LoanDetails_status", "R");
			}
			if("".equals(formObject.getNGValue("cmplx_LoanDetails_paymode"))){
				formObject.setNGValue("cmplx_LoanDetails_paymode", "T");
			}
			//below code added by nikhil 20/12/17
			formObject.setNGValue("cmplx_LoanDetails_insplan", "E");
			formObject.setLocked("cmplx_LoanDetails_maturitydate", true);
			formObject.setNGValue("LoanDetails_modeofdisb","T");
			formObject.setLocked("cmplx_LoanDetails_ageatmaturity", true);
			formObject.setLocked("cmplx_LoanDetails_insuramt", true);
			formObject.setLocked("cmplx_LoanDetails_baserate", true);
			formObject.setLocked("cmplx_LoanDetails_repfreq", true);
			formObject.setNGValue("cmplx_LoanDetails_repfreq", "Monthly");

			if("Q".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDetails_paymode")))
			{
				formObject.setLocked("cmplx_LoanDetails_chqno", false);
				formObject.setLocked("cmplx_LoanDetails_chqdat", false);
			}
			else
			{
				formObject.setLocked("cmplx_LoanDetails_chqno", true);
				formObject.setLocked("cmplx_LoanDetails_chqdat", true);
			}
			formObject.setLocked("cmplx_LoanDetails_insplan",true);
			formObject.setLocked("cmplx_LoanDetails_netrate",true);
			formObject.setLocked("cmplx_LoanDetails_pdtpref",true);
			formObject.setLocked("cmplx_LoanDetails_basetype",true);
			formObject.setLocked("cmplx_LoanDetails_paidon",false);
			formObject.setLocked("cmplx_LoanDetails_trandate",false);



			formObject.setLocked("cmplx_LoanDetails_LoanProcessingVATPercent", true);//added By Tarang for drop 4 point 8 on 08/03/2018
			formObject.setLocked("cmplx_LoanDetails_InsuranceVATPercent", true);//added By Tarang for drop 4 point 8 on 08/03/2018

			formObject.setNGValue("cmplx_LoanDetails_basetype", "PLRP");
			formObject.setNGValue("cmplx_LoanDetails_paidon",formObject.getNGValue("cmplx_LoanDetails_cmplx_LoanGrid",0,2));
			formObject.setNGValue("cmplx_LoanDetails_fdisbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

			loanvalidate();
			formObject.setNGValue("cmplx_LoanDetails_trandate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			formObject.setNGValue("cmplx_LoanDetails_fdisbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			formObject.setNGValue("LoanDetails_payreldate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			formObject.setNGValue("LoanDetails_disbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			formObject.setNGValue("cmplx_LoanDetails_paidon",formObject.getNGValue("cmplx_LoanDetails_cmplx_LoanGrid",0,2));
			formObject.setNGValue("cmplx_LoanDetails_cmplx_LoanGrid",0,2, new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			formObject.setNGValue("cmplx_LoanDetails_cmplx_LoanGrid",0,1,formObject.getNGValue("cmplx_LoanDetails_frepdate") );
			formObject.setLocked("cmplx_LoanDetails_lpfamt",false);

		}
		/*	else if ("Risk_Rating".equalsIgnoreCase(pEvent.getSource().getName())) {
				PersonalLoanS.mLogger.info("query to set EMPLOYED INDIVIDUAL as a default value");
				formObject.setNGValue("cmplx_RiskRating_Industry", "EMPLOYED INDIVIDUAL");	
				PersonalLoanS.mLogger.info("set value of industry: "+formObject.getNGValue("cmplx_RiskRating_Industry"));

			}//by Alok for RISK RATING fragment under industry field  on 22/09/2021
		 */
		//shifted from PLCOmmonCode to here by akshay on 17/1/18
		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("RLOS Initiation Inside IncomingDocuments");                                                            
			formObject.setVisible("IncomingDoc_UploadSig",false);
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomingDoc_ViewSIgnature",true);
				formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
			}
			else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setEnabled("IncomingDoc_ViewSIgnature",false);

			}
			fetchIncomingDocRepeater();
			PersonalLoanS.mLogger.info("RLOS Initiation eventDispatched() formObject.fetchFragment1");
		}
		 */

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("PL DDVT MAKER  Inside load Address details");
			//loadAddressDetails();
			loadPicklist_Address();
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//commented by saurabh on 10th nov
			LoadpicklistAltcontactDetails();

			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			enrollrewardvalid();
			formObject.setLocked("AlternateContactDetails_RetainAccIfLoanReq",false);
			if(formObject.isVisible("AlternateContactDetails_EnrollRewardsIdentifier")){
				formObject.setLocked("AlternateContactDetails_EnrollRewardsIdentifier",false);
				formObject.setEnabled("AlternateContactDetails_EnrollRewardsIdentifier",true);
			}
			if("false".equals(formObject.getNGValue("cmplx_Customer_NTB"))){
				checkforCurrentAccounts();
			}
			//change by saurabh for Air Arabia functionality.
			if(formObject.isVisible("AlternateContactDetails_AirArabiaIdentifier")){
				formObject.setLocked("AlternateContactDetails_AirArabiaIdentifier",false);
				formObject.setEnabled("AlternateContactDetails_AirArabiaIdentifier",true);
			}
			//code ended by siva on 01112019 for PCAS-1401

			//below code modified by siva on 01112019 for PCAS-1401
			if(null!=formObject.getNGValue("cmplx_Customer_CIFNO") && !"".equals(formObject.getNGValue("cmplx_Customer_CIFNO")))
			{
				String squeryair="select AIR_ARABIA_IDENTIFIER from CAPS_MAIN_MIG_DATA where CIF='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"'";
				List<List<String>> airarabia=formObject.getNGDataFromDataCache(squeryair);
				PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE squeryair+ "+squeryair);

				if (airarabia!=null && !airarabia.isEmpty()){
					formObject.setNGValue("AlternateContactDetails_AirArabiaIdentifier",airarabia.get(0).get(0));
				}
				else
				{//Commented for sonar
					String queryair="select con.AIR_ARABIA_IDENTIFIER from ng_RLOS_AltContactDetails con inner join ng_rlos_customer cus on cus.wi_name = con.wi_name where cus.PassportNo='"+formObject.getNGValue("cmplx_Customer_PAssportNo")+"' and con.AIR_ARABIA_IDENTIFIER is not null";
					List<List<String>> airarabia1=formObject.getNGDataFromDataCache(queryair);
					if (null!=airarabia1 && !airarabia1.isEmpty()){
						formObject.setNGValue("AlternateContactDetails_AirArabiaIdentifier",airarabia1.get(0).get(0));
					}
				}
			}
			else{
				PersonalLoanS.mLogger.info("CIF is empty for Air Arabia logic to work");
			}
			//code ended by siva on 01112019 for PCAS-1401

		}
		//below code added by nikhil for proc-3304
		//Changes done for code optimization 25/07(else if removed because duplicate)


		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");
			//added By Tarang for drop 4 point 3 started on 21/02/2018
			LoadPickList("SupplementCardDetails_CardProduct","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
			LoadPickList("SupplementCardDetails_EmploymentStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmploymentStatus with (nolock)");
			LoadPickList("SupplementCardDetails_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			//added By Tarang for drop 4 point 3 ended on 21/02/2018
			SupplementaryenrollrewardValid();
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			Loadpicklistfatca();
			formObject.setNGValue("cmplx_FATCA_SignedDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			//By Alok Tiwari for fetching current date
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_KYC();}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPickListOECD();}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			int framestate_Alt_Contact = formObject.getNGFrameState("Alt_Contact_container");
			if(framestate_Alt_Contact == 0){
				//mLogger.info("Alternate details alread fetched");
			}
			else {
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setNGFrameState("Alt_Contact_container", 0);
				alignDemographiTab(formObject);
				//end for kyc alignment
			}
			partMatchValues();
			//added by akshay on 27/12/17
			if(!"Y".equals(formObject.getNGValue("Is_PartMatchSearch"))){
				formObject.setLocked("PartMatch_Blacklist", true);
			}
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadinFinacleCRNGrid(formObject);
			if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
			{
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
			}
			else
			{
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Corporate", "Corporate");
			}
		}

		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			notepad_withoutTelLog();

		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			LoadpicklistCardDetails();
			loadDataInCRNGrid();
			IslamicFieldsvisibility();
			Load_Self_Supp_Data();
			if(formObject.getNGValue("cmplx_CardDetails_MarketCode")==null || "".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_MarketCode"))
					||"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_MarketCode")))//PCASI-2617
			{
				formObject.setNGValue("cmplx_CardDetails_MarketCode","BAU");
			}
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist1();
			//by shweta
			LoadReferGrid();
			formObject.setLocked("cmplx_Decision_New_CIFNo",true);
			fetch_desesionfragment();
			if(formObject.isVisible("FinacleCRMCustInfo_Frame1")==false){
				formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
				formObject.setNGFrameState("FinacleCRM_CustInfo", 0);
				adjustFrameTops("FinacleCRM_CustInfo,External_Blacklist,Finacle_Core,MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");
			}
			if(formObject.isVisible("LoanDetails_Frame1")==false){
				formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
				formObject.setNGFrameState("LoanDetails", 0);
				adjustFrameTops("LoanDetails,Risk_Rating");
			}
			//for decision fragment made changes 8th dec 2017

			PersonalLoanS.mLogger.info("***********Inside decision history");
			//fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);

			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");



			//Arun (11/09/17)
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_New_CIFNo"))) 
			{  //

				PersonalLoanS.mLogger.info("***********Inside decision history");
				fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
				PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
				formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);

				//for decision fragment made changes 8th dec 2017
			}
			// bandana for showing cif in ddvt_maker
			if ("".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_New_CIFNo"))){
				formObject.setNGValue("cmplx_Decision_New_CIFNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
			}
			// bandana for showing cif in ddvt_maker ends
		}             

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("WorldCheck1_age",true);
			// Changes done to load master values in world check birthcountry and residence country
			loadPicklist_WorldCheck();
		}

		//code changes by bandana starts
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			/*	if(cardProduct.equalsIgnoreCase("LOC STANDARD") || cardProduct.equalsIgnoreCase("LOC PREFERRED")){
				formObject.setNGValue("transtype","LOC");
			}*/
			//Added by shivang for PCASP-2891
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
				formObject.setNGValue("transtype","LOC");
				formObject.setLocked("transtype", true);
			}
			loadPicklist_ServiceRequest();
			//below code added by nikhil 19/1/18
			String dds_mode=formObject.getNGValue("cmplx_CC_Loan_DDSMode");
			//below code also fix point "30-Service Details#Validations not as per FSD."
			if("F".equalsIgnoreCase(dds_mode))
				//above code also fix point "30-Service Details#Validations not as per FSD."
//loadpicklist3
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
			if(formObject.getNGValue("FircoStatusLabel").equalsIgnoreCase("Hit"))
			{
				PersonalLoanS.mLogger.info("checking activityname for decsion"+formObject.getWFActivityName()); 
				 formObject.setNGValue("cmplx_Decision_Decision","Refer");
				 PersonalLoanS.mLogger.info("checking why not refer"+formObject.getNGValue("cmplx_Decision_Decision")); 
					
				 
				 formObject.setEnabled("cmplx_Decision_Decision",false);
			}
			else
			{
				PersonalLoanS.mLogger.info("checking activityname for decsion"+formObject.getWFActivityName()); 
				 formObject.setNGValue("cmplx_Decision_Decision","");
				 formObject.setEnabled("cmplx_Decision_Decision",true);
			}
		}
		//code changes by bandana ends

		//            started merged code
		//below code added by nikhil 12/12/17
		//added by shweta
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("FinacleCore_Frame6",true);
			formObject.setLocked("FinacleCore_Frame5",true);
			formObject.setLocked("FinacleCore_Frame6",true);
			// added by abhishek as per the CC FSD
			formObject.setLocked("FinacleCore_Frame1", true);
			LoadPickList("FinacleCore_cheqtype", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Cheque_Type with (nolock)");
			LoadPickList("FinacleCore_typeofret", "select '--Select--' as description union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock) order by description desc");
			//++below code added by nikhil for ddvt CC issues
			//formObject.setLocked("FinacleCore_Frame7", true);	
		}

		else if("RiskRating".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			loadPicklistRiskRating();
			/*	PersonalLoanS.mLogger.info("query to set EMPLOYED INDIVIDUAL as a default value");
			if("--Select--".equals(formObject.getNGValue("cmplx_RiskRating_Industry")) || "".equals(formObject.getNGValue("cmplx_RiskRating_Industry"))){
			formObject.setNGValue("cmplx_RiskRating_Industry", "EMPLOYED INDIVIDUAL");	
			} //alok
			PersonalLoanS.mLogger.info("set value of industry: "+formObject.getNGValue("cmplx_RiskRating_Industry"));
			 */	
		}
		//by vishu for hide the CIF Lock and CIF Unlock button
		formObject.setVisible("DecisionHistory_CifLock",false);
		formObject.setVisible("DecisionHistory_CifUnlock",false);

	}




	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		//PersonalLoanS.mLogger.info("Inside PL DDVT MAKER eventDispatched() EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		switch(pEvent.getType())
		{              
		case FRAME_EXPANDED:
			//PersonalLoanS.mLogger.info("In PL_Iniation FRAME_EXPANDED eventDispatched() "+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
			break;

		case FRAGMENT_LOADED:
			if("Y".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif")))
			{
				new PersonalLoanSCommonCode().LockFragmentsOnLoad(pEvent);
				Unlock_ATC_Fields();
			}
			else
			{
				fragment_loaded(pEvent);
			}
			break;

		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;


		case VALUE_CHANGED:
			new PersonalLoanSCommonCode().value_Change(pEvent);					
			break;

		default: break;

		}

	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// empty method

	}


	public void initialize() {
		// empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// empty method
		CustomSaveForm();

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		String CRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0, 2);

		String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
		String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
		String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");
		String fullName = ""; 
		if("".equals(mName)){
			fullName = fName+" "+lName;
		}
		else{
			fullName = fName+" "+mName+" "+lName;
		}
		formObject.setNGValue("CUSTOMERNAME",fullName);
		PersonalLoanS.mLogger.info("Final val of fullName:"+ fullName);

		for(int i=1;i<formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
		{
			CRN=CRN+","+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
		}
		formObject.setNGValue("CRN", CRN);
		formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));

		try
		{
			PersonalLoanS.mLogger.info("Before Nmber format @cad1 :");
			NumberFormat fin_lim = NumberFormat.getNumberInstance(Locale.US);
			String FL = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit");
			double FL_1 = Double.parseDouble(FL);
			PersonalLoanS.mLogger.info("Before NUmber format : FL :"+FL_1);
			String new_format = fin_lim.format(FL_1);
			PersonalLoanS.mLogger.info("Before NUmber format new_format::"+new_format);
			formObject.setNGValue("loan_amount_1",new_format);
			PersonalLoanS.mLogger.info("After set NUmber format new_format:::"+new_format);
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
		} //Hritik for communication

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		UpdatePrimaryCif();
	}

	//removed by shweta, method implemented in common file
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// empty method
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
		if(cc_waiv_flag.equalsIgnoreCase("N")){
			Check_selectedCard();
			Check_All_Limits();
		}

		try{
			Data_reset("DecisionHistory");
			PersonalLoanS.mLogger.info("outside Acc No. at Submitform ");
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_AccountNo"))){
				PersonalLoanS.mLogger.info("Inside Acc No. at Submitform ");
				String acc_currflag = getCurrentAccountNumber("ENTITY_MAINTENANCE_REQ");
				PersonalLoanS.mLogger.info("Acc No. at Submitform"+acc_currflag);
				formObject.setNGValue("cmplx_Decision_AccountNo", acc_currflag.split("!")[0]);
				PersonalLoanS.mLogger.info("set value of Acc No. at Submitform"+ formObject.getNGValue("cmplx_Decision_AccountNo"));
			} // hritik
			CustomSaveForm();

			//SetAccount();
			formObject.setNGValue("email_id",formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI"));
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));//added by akshay for proc 10104
			String CRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0, 2);
			for(int i=1;i<formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
			{
				CRN=CRN+","+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
			}
			formObject.setNGValue("CRN", CRN);
			formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));
			Save_Card_desc();

		}

		catch(Exception Ex)
		{
			PersonalLoanS.mLogger.info(Ex.getMessage());
		}
		finally
		{
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED"))|| NGFUserResourceMgr_PL.getGlobalVar("PL_Yes").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) )
			{
				PersonalLoanS.mLogger.info("CC val change "+ "Inside Y of CPV required");
				formObject.setNGValue("cpv_required","Y");
			}
			else
			{
				PersonalLoanS.mLogger.info("CC val change "+ "Inside N of CPV required");
				formObject.setNGValue("cpv_required","N");
			}
			//incoming doc function
			IncomingDoc();
			//incoming doc function
			//added by akshay on 9/12/17

			saveIndecisionGrid();
			String dec = formObject.getNGValue("cmplx_Decision_Decision");
			if(dec!=null && !"".equals(dec) && "Submit".equalsIgnoreCase(dec)){
				formObject.setNGValue("decision","Approve" );
				Update_customerName();
			}
			else{
				formObject.setNGValue("decision",dec);
			}
			//below code added by nikhil to handle stage reversal cases
			LoadReferGrid();//by shweta , method should be called after setting decision value
			if(!"CAD_Analyst1".equalsIgnoreCase(formObject.getNGValue("IS_Stage_Reversal"))){
				formObject.setNGValue("IS_Stage_Reversal", "N");
			} 
			formObject.setNGValue("CAD_ANALYST1_DECISION", "");
			formObject.setNGValue("CAD_Analyst2_Dec", "");
			formObject.setNGValue("CPV_DECISION", "");
			formObject.setNGValue("CPV_Analyst_dec", "");		
			validateStatusForSupplementary();
		}
	}


	public void Check_selectedCard() {
		// TODO Auto-generated method stub
	}

	public void Update_customerName() {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String pLname = formObject.getNGValue("cmplx_Customer_LAstNAme");
			String pFname = formObject.getNGValue("cmplx_Customer_FIrstNAme");
			String pvalue=pFname+" "+pLname;
			String Wi_name= formObject.getWFWorkitemName();
			String multiple_app_name = formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",0,1);
			PersonalLoanS.mLogger.info(" inside Update_customerName multiple_app_name: "+ multiple_app_name + "pvalue: "+pvalue);
			if(!multiple_app_name.equalsIgnoreCase(pvalue)){

				PersonalLoanS.mLogger.info(" inside Update_customerName name mismatch condition: ");

				String mutipleapp_Query="update ng_rlos_gr_MultipleApplicants set applicantName ='"+pvalue+"' where MultipleApplicants_winame='"+Wi_name+"' and ApplicantType like 'Primary'";
				PersonalLoanS.mLogger.info(" inside Update_customerName final update mutipleapp_Query: "+ mutipleapp_Query);
				formObject.saveDataIntoDataSource(mutipleapp_Query);
				pvalue="P-"+pFname+" "+pLname;

				String AddressDetails_Query="update ng_RLOS_GR_AddressDetails set CustomerType ='"+pvalue+"' where addr_wi_name='"+Wi_name+"' and CustomerType like 'P-%'";
				PersonalLoanS.mLogger.info(" inside Update_customerName final update AddressDetails_Query: "+ AddressDetails_Query);
				formObject.saveDataIntoDataSource(AddressDetails_Query);

				String kyc_Query="update NG_RLOS_GR_KYC set CustomerType ='"+pvalue+"' where KYC_wi_name='"+Wi_name+"' and CustomerType like 'P-%'";
				PersonalLoanS.mLogger.info(" inside Update_customerName final update kyc_Query: "+ kyc_Query);
				formObject.saveDataIntoDataSource(kyc_Query);

				String Oecd_Query="update ng_rlos_GR_OecdDetails set CustomerType ='"+pvalue+"' where oecd_wi_name='"+Wi_name+"' and CustomerType like 'P-%'";
				PersonalLoanS.mLogger.info(" inside Update_customerName final update Oecd_Query: "+ Oecd_Query);
				formObject.saveDataIntoDataSource(Oecd_Query);

				String FATCA_Query="update NG_RLOS_GR_FATCA set CustomerType ='"+pvalue+"' where FATCA_wi_name='"+Wi_name+"' and CustomerType like 'P-%'";
				PersonalLoanS.mLogger.info(" inside Update_customerName final update FATCA_Query: "+ FATCA_Query);
				formObject.saveDataIntoDataSource(FATCA_Query);
			}
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info(" Exception inside Update_customerName ex: "+ ex.getMessage());
		}

	}

	// By shweta Commenting below method  as it is implemented in CC
	/*public void Save_Card_desc() {
		// TODO Auto-generated method stub

	}*/


	public void fetch_desesionfragment()throws ValidatorException{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setVisible("DecisionHistory_Button3",false);
		formObject.setVisible("DecisionHistory_updcust",false);
		formObject.setVisible("DecisionHistory_chqbook",false);
		formObject.setVisible("cmplx_Decision_waiveoffver",false);
		loadPicklist3();

		PersonalLoanS.mLogger.info("PL DDVT Maker  inside fetch fragment DecisionHistory END");
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

