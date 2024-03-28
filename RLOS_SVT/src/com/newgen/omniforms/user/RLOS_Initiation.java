//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOS_Initiation.java
//Author					: Akshay Gupta
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------


package com.newgen.omniforms.user;
import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

@SuppressWarnings("serial")

public class RLOS_Initiation extends RLOSCommon implements FormListener
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	String ReqProd=null;


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void formLoaded(FormEvent pEvent)
	{
		RLOS.mLogger.info("Inside initiation RLOS");
		RLOS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED

	Date Modified                       : 6/08/2017              
	Author                              : Akshay Gupta             
	Description                         : Form Populate         

	 ***********************************************************************************  */

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Common_Utils common=new Common_Utils(RLOS.mLogger);
		try{
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
			new RLOSCommonCode().CustomerFragment_Load();
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			RLOS.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
			//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = common.Convert_dateFormat("", "", "dd/MM/yyyy");
			RLOS.mLogger.info( "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));//added By akshay on 25/9/17 as per point 14
			formObject.setNGValue("WI_name",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Branch Initiation");
			formObject.setNGValue("Created_By",formObject.getUserName());
			//formObject.setNGValue("Cust_Name",formObject.getNGValue("Customer_Name"));//Tarang to be removed on friday(1/19/2018)
			//formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("Customer_Name"));//Tarang to be removed on friday(1/19/2018)
			/*formObject.setNGValue("Cust_Name",formObject.getNGValue("CustomerName"));
			formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("CustomerName"));*/
			formObject.setNGValue("lbl_init_channel_val","Branch_Init");

			formObject.setNGValue("lbl_curr_date_val",currDate);
			formObject.setNGValue("ApplicationRefNo", formObject.getWFFolderId());
			formObject.setNGValue("lbl_user_name_val",formObject.getUserName());
			formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
			formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Sub_Product"));
			formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));

			//added by Akshay on 25/9/17 as exception ws thrown when loading for first time/product grid empty
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n>0){
				formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
			}
			//ended by Akshay on 25/9/17 as exception ws thrown when loading for first time/product grid empty
			formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNO"));
			formObject.setNGValue("RM_Name",getRMName());
			formObject.setNGValue("lbl_TL_Name_val",formObject.getNGValue("RM_Name"));

			String init_Channel=formObject.getNGValue("initiationChannel");
			if(init_Channel!=null && "".equalsIgnoreCase(init_Channel))
				formObject.setNGValue("initiationChannel","Branch_Init");
			/*if(formObject.getNGValue("empType").contains("Salaried")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				formObject.setSheetVisible("ParentTab",1, false);
			}
			else if(formObject.getNGValue("empType").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed"))){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
				formObject.setSheetVisible("ParentTab",3, false);
			}*/
			if(formObject.getNGValue("EmploymentType").contains("Salaried")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				formObject.setSheetVisible("ParentTab",1, false);
			}
			else if(formObject.getNGValue("EmploymentType").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed"))){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
				formObject.setSheetVisible("ParentTab",3, false);
			}
			if(formObject.getNGValue("Product_Type").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan")) && Float.parseFloat(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",1130);
			}
			if(formObject.getNGValue("PrimaryProduct").equals(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard"))){
				formObject.setSheetVisible("ParentTab",7, true);
			}
			//added by akshay on 29/1/18
			if("true".equals(formObject.getNGValue("existingOldCustomer"))){
				formObject.setLocked("existingOldCustomer",true);
				formObject.setLocked("OldApplicationNo",true);
				formObject.setLocked("Fetch_existing_cas",true);
			}
			RLOS.mLogger.info("Value Of Init Channel:"+init_Channel);
		}catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in form populated: "+e.getMessage()+printException(e));
		}
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Loads fragment data         

	 ***********************************************************************************  */

	public void call_Fragement_Loaded(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_M").equalsIgnoreCase(formObject.getNGValue("initiationChannel")) ){
			RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
			new RLOSCommonCode().DisableFragmentsOnLoad(pEvent);
		}
		else{
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				formObject.setHeight("Customer_Frame1", 510);
				formObject.setHeight("CustomerDetails", 530);
				formObject.setLocked("Customer_save",false);
				formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
				formObject.setEnabled("cmplx_Customer_card_id_available",true);
				formObject.setLocked("cmplx_Customer_NEP",false);
				formObject.setLocked("ReadFromCard",false);
				formObject.setNGValue("", "");
				formObject.setLocked("cmplx_Customer_Employer_Name",false);
				formObject.setLocked("cmplx_Customer_Employer_code",false);
				formObject.setLocked("Customer_search",false);
				formObject.setLocked("cmplx_Customer_Othersflag",false);
				loadPicklistCustomer();

				if("true".equals(formObject.getNGValue("cmplx_Customer_CardNotAvailable")) || "true".equals(formObject.getNGValue("cmplx_Customer_card_id_available")) || (!"".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null))
				{
					if ("true".equals(formObject.getNGValue("cmplx_Customer_card_id_available"))){
						formObject.setHeight("Customer_Frame1", 620);
						formObject.setLocked("ReadFromCard",true);
						formObject.setLocked("cmplx_Customer_NEP",true);
						formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
						formObject.setHeight("CustomerDetails", 700);	
					}
					else if ("true".equals(formObject.getNGValue("cmplx_Customer_CardNotAvailable"))){
						formObject.setVisible("Customer_Frame2", true);
						formObject.setLocked("Customer_Frame2",false);
						formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700);
						formObject.setLocked("cmplx_Customer_NEP",true);
						formObject.setLocked("ReadFromCard",true);
						formObject.setEnabled("cmplx_Customer_card_id_available",false);
						formObject.setVisible("Customer_Label55", true);
						formObject.setVisible("cmplx_Customer_marsoomID", true);

					}
					else if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
						formObject.setVisible("Customer_Label56", true);
						formObject.setVisible("cmplx_Customer_EIDARegNo", true);

						formObject.setLocked("Customer_Frame1",false);
						formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
						formObject.setLocked("ReadFromCard",true);
						formObject.setEnabled("cmplx_Customer_card_id_available",false);
						//below code added by nikhil 17/1/18
						if("NEWC".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
							formObject.setLocked("cmplx_Customer_EmiratesID",true);
							formObject.setLocked("cmplx_Customer_EmirateIDExpiry",true);
							formObject.setLocked("cmplx_Customer_IdIssueDate",true);
						}
						if("RWEID".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
							formObject.setVisible("Customer_Label56", true);
							formObject.setVisible("cmplx_Customer_EIDARegNo", true);
						}

					}
					//added by akshay on 19/3/18 for proc 6645
					if("true".equals(formObject.getNGValue("existingOldCustomer"))){
						formObject.setLocked("FetchDetails", false);
					}
				}
				formObject.setLocked("cmplx_Customer_DLNo",false);
				formObject.setLocked("cmplx_Customer_Passport2",false);
				formObject.setLocked("cmplx_Customer_Passport3",false);
				formObject.setLocked("cmplx_Customer_PAssport4",false);
				RLOS.mLogger.info( "");



				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				//String NEP = formObject.getNGValue("cmplx_Customer_NEP");
				String card_not_avl = formObject.getNGValue("cmplx_Customer_CardNotAvailable");
				if("".equalsIgnoreCase(EID)  && "false".equalsIgnoreCase(card_not_avl)){
					RLOS.mLogger.info(" In RLOS inside if customer in fragmnt loaded");

				}
				else{
					String is_fetched_details = formObject.getNGValue("Is_Customer_Details");
					if(!"Y".equalsIgnoreCase(is_fetched_details)){
						formObject.setLocked("cmplx_Customer_FIrstNAme",false);
						formObject.setLocked("cmplx_Customer_MiddleName",false);
						formObject.setLocked("cmplx_Customer_LAstNAme",false);
						formObject.setEnabled("cmplx_Customer_DOb",true);
						formObject.setLocked("cmplx_Customer_PAssportNo",false);
						formObject.setLocked("cmplx_Customer_Nationality",false);
						formObject.setLocked("cmplx_Customer_MobNo",false);
						//formObject.setLocked("FetchDetails",true);
						formObject.setVisible("Customer_Label56", true);
						formObject.setVisible("cmplx_Customer_EIDARegNo", true);

						RLOS.mLogger.info(" In RLOS inside else customer in fragmnt loaded");
					}
				}
				//Added by aman to make the button non editable 7th Dec
				formObject.setLocked("Customer_Button1",true);
				//Added by aman to make the button non editable 7th Dec
				formObject.setEnabled("cmplx_Customer_OTPValidation", false);
				RLOS.mLogger.info("Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
				if("Telesales_Init".equalsIgnoreCase(formObject.getWFActivityName()))
				{
					formObject.setLocked("cmplx_Customer_NEP",true);
				}

				try{
					String if_cif_available = formObject.getNGValue("cmplx_Customer_CIFNO");
					RLOS.mLogger.info("CIF is: "+if_cif_available);
					if(if_cif_available !=null && !"".equalsIgnoreCase(if_cif_available) && !" ".equalsIgnoreCase(if_cif_available)){
						setcustomer_enable();
					}
					if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
						setcustomer_enable();
						RLOS.mLogger.info("Check for cmplx_Customer_NTB"+"asd");
					}
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_card_id_available")) && "".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility"))){
						formObject.setLocked("Customer_Button1", false);
					}
					if("No".equals(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable"))){
						formObject.setLocked("cmplx_Customer_SecNationality", true);
					}
				}catch(Exception ex){
					RLOS.mLogger.info( printException(ex));
				}

			}

			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where processname='"+formObject.getWFProcessName()+"'");//changed by akshay on 3/1/17
				formObject.setNGValue("ReqProd","--Select--");				
				formObject.setLocked("LastPermanentLimit",true);//Arun (07/12/17) as data will come from migration in this field				
				formObject.setLocked("LastTemporaryLimit",true);//Arun (07/12/17) as data will come from migration in this field
			}

			else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setNGValue("gender", "");
				formObject.setNGValue("nationality", "--Select--");

				LoadPickList("title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
				formObject.setNGValue("title", "");
				//added by aman for Drop4 on 4th April
				LoadPickList("Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
				LoadPickList("GuarantorDetails_empType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
				LoadPickList("EmploymentStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
				LoadPickList("GuarantorDetails_MaritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus with (nolock) order by Code");
				//added by aman for Drop4 on 4th April
				
				LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
				LoadPickList("nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
			}

			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
				formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
				formObject.setLocked("cmplx_IncomeDetails_netSal1", false);
				formObject.setLocked("cmplx_IncomeDetails_netSal2", false);
				formObject.setLocked("cmplx_IncomeDetails_netSal3", false);
				formObject.setLocked("cmplx_IncomeDetails_AvgNetSal", true);
				formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Commission_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Other_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Flying_Avg", true);
				
				RLOSCommonCode.visibilityFrameIncomeDetails(formObject);

				//added by akshay on 16/10/17 for point 6 in PL_NTB sheet-By default Value of accomodation should be disabled 
				if("Yes".equals(formObject.getNGValue("cmplx_IncomeDetails_Accomodation")))
					formObject.setLocked("cmplx_IncomeDetails_AccomodationValue", false);

				LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
				LoadPickList("cmplx_IncomeDetails_StatementCycle2", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");

				LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");

				//below query changed by saurabh on 12th Oct to populate correct masters for bank stat from.
				LoadPickList("IncomeDetails_BankStatFrom", "select '--Select--' union select convert(varchar, description) from NG_MASTER_othBankCAC with (nolock) where isactive='Y'");
				
				//below code added by nikhil for proc-3530
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
				{
					formObject.setEnabled("IncomeDetails_FinacialSummarySelf", false);
				}
				else
				{
					formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
				}

			}

			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				//change by saurabh on 9th July 17.
				try{
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail"))){
						formObject.setLocked("Liability_New_fetchLiabilities",false);
					}
					else{
						formObject.setLocked("Liability_New_fetchLiabilities",true);
					}

					//added by abhishek
				
					if((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("EmploymentType")))||(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("EmploymentType")))){
						
						formObject.setVisible("Label9", false);
						formObject.setVisible("cmplx_Liability_New_DBR", false);
						formObject.setVisible("Label15", false);
						formObject.setVisible("cmplx_Liability_New_TAI", false);
						formObject.setVisible("Liability_New_Label14", false);
						formObject.setVisible("cmplx_Liability_New_DBRNet", false);
						formObject.setVisible("Button2", false);
						formObject.setLeft("Liability_New_Label15", 7);
						formObject.setLeft("cmplx_Liability_New_AggrExposure", 7);
						formObject.setTop("Liability_New_Label15", 15); //Arun (12-11-17) to align the fields
						formObject.setTop("cmplx_Liability_New_AggrExposure", 31); //Arun (12-11-17) to align the fields
						formObject.setVisible("FetchWorldCheck_SE", true);
						formObject.setVisible("CheckEligibility_SE", true);
						formObject.setVisible("Button2", false);
						//change by saurabh on 8th Nov 2017.
						formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", true);

					}
					else{
						formObject.setVisible("FetchWorldCheck_SE", false);
						formObject.setVisible("CheckEligibility_SE", false);
						//change by saurabh on 8th Nov 2017.
						formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);
					}

					//formObject.setLocked("takeoverAMount",true);
					formObject.setLocked("cmplx_Liability_New_DBR",true);
					formObject.setLocked("cmplx_Liability_New_DBRNet",true);
					formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
					formObject.setLocked("cmplx_Liability_New_TAI",true);
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
						formObject.setVisible("Liability_New_Label25", true);
						formObject.setVisible("cmplx_Liability_New_paid_installments", true);
					}
					else{
						formObject.setVisible("Liability_New_Label25", false);
						formObject.setVisible("cmplx_Liability_New_paid_installments", false);
					}
				}catch(Exception ex){
					printException(ex);
				}
			}


			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				RLOS.mLogger.info(" COMPANY FRAGMENT LOADED");
				try{
					formObject.setLocked("cif", true);
					formObject.setLocked("CompanyDetails_Button3", true);
					LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
					//LoadPickList("NepType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_NEPType with (nolock) order by code");
					LoadPickList("ApplicationCategory", "select '--Select--' union all select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");
					LoadPickList("indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_IndustrySector with (nolock) order by code");
					//LoadPickList("indusMAcro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMacro with (nolock) order by code");
					//LoadPickList("indusMicro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMicro with (nolock) order by code");
					LoadPickList("legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_LegalEntity with (nolock) order by code");
					LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
					LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
					LoadPickList("EOW", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
					LoadPickList("headOffice", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
					LoadPickList("TradeLicencePlace", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from ng_master_TradeLicensePlace with (nolock) order by code");
					LoadPickList("CompanyDetails_POAHolder", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_POAHolder with (nolock) order by code");
					LoadPickList("compIndus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_company_industry with (nolock) order by code");//Arun (09/10)
					LoadPickList("PartnerDetails_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
					LoadPickList("indusMAcro", " Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
					LoadPickList("indusMicro", "Select micro from (select '--Select--' as micro union select micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when micro ='--Select--' then 0 else 1 end");
					//added by akshay on 15/1/18
					//need to be checked 
					int prd_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					if(prd_rowCount>0){
						String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
						if(subprod!=null && "IM".equalsIgnoreCase(subprod)){
							LoadPickList("MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = 'IM' order by code");	
						}
						else{
							LoadPickList("MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = '!IM' order by code");
						}
					}

					LoadPickList("PromotionCode","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y' order by code");
					LoadPickList("ClassificationCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' order by code");
					LoadPickList("NEPType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_NEPType with (nolock) order by code");
					LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select description,code from NG_MASTER_TargetSegmentCode with (nolock) order by code");
					/*	if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC")){
					formObject.setVisible("CompanyDetails_Label8", true);
					formObject.setVisible("CompanyDetails_effecLOB", true);	

				}--Commented on 6/10/17 as discussed with shashank-Future CR*/
					//need to be checked 

					/*if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
						formObject.setVisible("CompanyDetails_Label27", true);
						formObject.setVisible("EOW", true);
						formObject.setVisible("CompanyDetails_Label29", true);
						formObject.setVisible("headOffice", true);
						formObject.setVisible("CompanyDetails_Label28", true);
						formObject.setVisible("visaSponsor", true);

					}
					else{
						formObject.setVisible("CompanyDetails_Label27", false);
						formObject.setVisible("EOW", false);//Emirate of work
						formObject.setVisible("CompanyDetails_Label29", false);
						formObject.setVisible("headOffice", false);
						formObject.setVisible("CompanyDetails_Label28", false);
						formObject.setVisible("visaSponsor", false);
						//formObject.setVisible("CompanyDetails_Label8", false);
						//formObject.setVisible("CompanyDetails_effecLOB", false);
					}*/
					loadPicklist_AuthSign();


					//changes done by saurabh on 8th nov 2017.
					int rows = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					boolean flag = true;
					if(rows>0){
						for(int i=0;i<rows;i++){
							if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 2).equalsIgnoreCase("Primary")){
								flag = false;
							}
						}
					}
					if(flag){
						String customerName = formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
						if(customerName!=null && !"".equalsIgnoreCase(customerName) && !" ".equalsIgnoreCase(customerName)){
							/*String Executexml= makeInputForGrid(customerName);
							RLOS.mLogger.info("Grid Data is"+Executexml);
							formObject.NGAddListItem("cmplx_CompanyDetails_cmplx_CompanyGrid",Executexml);
							 */
							AddInAuthorisedGrid(formObject);
							AddInCompanyGrid(formObject, customerName);
						}
					}//added by akshay on 18/1/18
					//else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("AuthorisedSignDetails_CIFNo", true);
					formObject.setLocked("AuthorisedSignDetails_FetchDetails", true);
					formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
					formObject.setNGValue("AuthorisedSignDetails_nationality", "");
					//changed by akshay on 15Th dec 2017 for NEPTYpe check.
					if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
						//formObject.setLocked("EMploymentDetails_Label25",true);//Added by Akshay on 9/9/17 for enabling NEP type as per FSD
						formObject.setLocked("NepType",true);
					}
					else if("NEWJ".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || "NEWC".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
						formObject.setNGValue("NepType",formObject.getNGValue("cmplx_Customer_NEP"));
					}
					//RLOS.mLogger.info("Grid Data is"+Executexml);
					//formObject.NGAddListItem("cmplx_CompanyDetails_cmplx_CompanyGrid_cmplx_GR_AuthorizedSignDetails",Executexml);

				}catch(Exception ex){
					RLOS.mLogger.info("Exception: "+printException(ex));
				}
			}

			/*else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			}

			 */
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				//changes done to load Employer details. method added in common file.
				employment_details_load();

			}


			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				RLOS.mLogger.info("Saurabh ELPINFO,Fragment Loaded");
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) where isactive = 'Y'  order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_takeoverBank", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) order by code");

				//added By Akshay on 29/9/17 for point 59-----No Of installment field empty
				int prd_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if(prd_rowCount>0){
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,7));
				}

				formObject.setNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment", formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				//ended By Akshay on 29/9/17 for point 59-----No Of installment field empty
				//E modified to Equated by Saurabh on 12th Oct.
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestType","Equated");//added By Akshay on 28/9/17 for point 53 on consolidated sheet
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Mon"); //added by aman 08/12
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_instrumenttype","Sal"); //added by aman 08/12
				//
				//
				/*formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);*/

				formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",false);//Drop 4 point 21 changed by Tarang on 29/01/2018  
				
				//commented by akshay on 28/12/17
				//formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
				//formObject.setLocked("cmplx_EligibilityAndProductInfo_Insurance",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_NumberOfInstallment",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BAseRate",true);	
				formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);	
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);
				//for TakeoverAmount by aman
				String queryTakeOver="select Sum(isNull(convert(float,Take_Amount),0)) from ng_rlos_cust_extexpo_LoanDetails  with (nolock) where   Wi_Name='"+formObject.getWFWorkitemName()+"'";
				RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+queryTakeOver);
				List<List<String>> TakeOver=formObject.getNGDataFromDataCache(queryTakeOver);
				if(!TakeOver.isEmpty())
				{
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_TakeoverAMount",TakeOver.get(0).get(0));
				}
				//for TakeoverAmount by aman
				//Code to set the values in Elig and prod info grid from scheme
				if(formObject.getNGValue("Product_Type").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan"))){
					String query="Select PRIME_TYPE,LSM_PRODDIFFRATE,prime_type_rate from NG_master_Scheme where SCHEMEId='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
					RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+query);
					List<List<String>> marginRate=formObject.getNGDataFromDataCache(query);
					if(!marginRate.isEmpty())
					{
						try{
							String baseRateType=marginRate.get(0).get(0).equalsIgnoreCase("")?"0":marginRate.get(0).get(0);
							String baseRate=marginRate.get(0).get(1).equals("")?"0":marginRate.get(0).get(1);
							String ProdprefRate=marginRate.get(0).get(2).equals("")?"0":marginRate.get(0).get(2);
							RLOS.mLogger.info("List is:"+marginRate);
							String netRate=formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate");
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_BAseRate", baseRate);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_BaseRateType", baseRateType);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate", ProdprefRate);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",netRate);
							String mgRate = "0.00";

							double nR =  netRate.equalsIgnoreCase("")?0.0:Float.parseFloat(netRate);
							double bR =  baseRate.equalsIgnoreCase("")?0.0:Float.parseFloat(baseRate);
							double pR =  ProdprefRate.equalsIgnoreCase("")?0.0:Float.parseFloat(ProdprefRate);
							double marginrate = nR-bR-pR;
							if(marginrate!=0){
								DecimalFormat df = new DecimalFormat("#");
								df.setMaximumFractionDigits(9);
								mgRate = df.format(marginrate);
							}
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_MArginRate",mgRate);
						}catch(Exception e){
							RLOS.mLogger.info("Exception in calculating margin rate: "+printException(e));
						}
					}
					try{
						 prd_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						if(prd_rowCount>0){
							String application_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
						
						if(application_type.contains("TOP")){
							String outBal = "select liab.TotalOutstandingAmt,elig.Final_Limit from ng_RLOS_CUSTEXPOSE_LoanDetails liab,ng_rlos_EligAndProdInfo elig with (nolock) where elig.wi_name = '"+formObject.getWFWorkitemName()+"' and elig.wi_name=liab.wi_name and Limit_Increase = 'true'";
							RLOS.mLogger.info("net payout query:"+outBal);
							List<List<String>> record = formObject.getNGDataFromDataCache(outBal);
							RLOS.mLogger.info("List is:"+record);
							if(record.size()>0){
								String outBalance = record.get(0).get(0);
								String finalLimit = record.get(0).get(1);
								float bal=0;
								float lim=0;
								if(!"".equalsIgnoreCase(outBalance)){
									bal = Float.parseFloat(outBalance);
								}
								if(!"".equalsIgnoreCase(finalLimit)){
									lim = Float.parseFloat(finalLimit);
								}
								RLOS.mLogger.info("lim is:"+lim);
								RLOS.mLogger.info("bal is:"+bal);
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_NetPayout", lim-bal);
							}
							else{
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_NetPayout", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
							}
						}
						}
					}
					catch(Exception ex){
						RLOS.mLogger.info("Exception in calculating net payout: "+printException(ex));
					}
				}

				//Code to set the values in Elig and prod info grid from scheme end
				//below code added 28/12/17

			}


			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("state",true);
				formObject.setLocked("city",true);
				loadPicklist_Address();
			}

			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to					
				//Deepak Code change to add requested product filter
				String TypeOfProd=formObject.getNGValue("LoanType_Primary");
				LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					
				//condition added by akshay on 26/3/18 for proc 7061
				if("false".equals(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setLocked("AlternateContactDetails_MobileNo1", true);
					//formObject.setLocked("AlternateContactDetails_MobNo2",true);
				}
				RLOS.mLogger.info("Saurabh COMPANY FRAGMENT LOADED"+formObject.getNGValue("PrimaryProduct"));
				
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
					formObject.setVisible("AlternateContactDetails_retainAccount",false);
					formObject.setVisible("AlternateContactDetails_custdomicile",false);
					formObject.setVisible("AltContactDetails_Label14",false);
				}
				else{
					formObject.setVisible("AlternateContactDetails_retainAccount",true);
					formObject.setVisible("AlternateContactDetails_custdomicile",true);
					formObject.setVisible("AltContactDetails_Label14",true);
					if("Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")))
					{
						formObject.setLocked("AlternateContactDetails_carddispatch", true);
					}
					else
					{
						formObject.setLocked("AlternateContactDetails_carddispatch", false);
					}
				}
				//ended By Akshay on 28/9/17 for hiding retain account when product is CC
				checkforCurrentAccounts();
			}


			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				try{
					LoadPickList("cmplx_CardDetails_statCycle","select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
					//if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salaried").equalsIgnoreCase(formObject.getNGValue("empType")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("empType"))){
					FieldsVisibility_CardDetails(formObject);//changed by akshay for proc 6462
					
					if(formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")>0){
						if ("LI".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
						{
							formObject.setLocked("CardDetails_Frame1",true);
						}
					}
				}catch(Exception ex){
					printException(ex);
				}
			}

			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("ResdCountry", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by code");
				LoadPickList("relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
				LoadPickList("nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
				LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
				//complex query to load supplementary card products by saurabh
				LoadPickList("SupplementCardDetails_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
				
			}
			else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from NG_MASTER_Relationship with (nolock) order by code");
				formObject.setNGValue("ReferenceDetails_ref_Relationship", "FRE");
			}
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

				RLOS.mLogger.info("Inside Fatca Initiation");
				loadPicklist_Fatca();//added by prabhakar drop-4 point-3
				/*LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
				LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");*/
				FATCA_Validations(formObject);
			}

			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {

				RLOS.mLogger.info("Inside Fatca Initiation");
				loadPicklist_OECD();//added by prabhakar drop-4 point-3
				/*LoadPickList("OECD_CountryBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				LoadPickList("OECD_townBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
				LoadPickList("OECD_CountryTaxResidence", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				LoadPickList("OECD_CRSFlagReason", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by code");*/
				formObject.setLocked("OECD_CRSFlagReason",true);
			}

			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
				formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
				formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
				//formObject.setNGValue("benificiaryName", formObject.getNGValue("Customer_Name"));//added by akshay on 21/11/17 as per FSD 2.7  //Tarang to be removed on friday(1/19/2018)
				formObject.setNGValue("benificiaryName", formObject.getNGValue("CUSTOMERNAME"));
				String accnum = formObject.getNGValue("Account_Number");
				formObject.setNGValue("creditcardNo", accnum);
				//Positioning changed by aman
				loadPicklist_ServiceRequest();
				//added By Akshay on 15/9/17 for disabling DDS tab if DDSFlag is false
				formObject.setNGValue("AppStatus", "act");
				if("false".equals(formObject.getNGValue("cmplx_CC_Loan_DDSFlag")))
				{
					formObject.setLocked("cmplx_CC_Loan_DDSMode",true);
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
					formObject.setLocked("cmplx_CC_Loan_Percentage",true);
					formObject.setLocked("cmplx_CC_Loan_DDSExecDay",true);
					formObject.setLocked("cmplx_CC_Loan_AccType",true);
					formObject.setLocked("cmplx_CC_Loan_DDSIBanNo",true);
					formObject.setLocked("cmplx_CC_Loan_DDSStartdate",true);
					formObject.setLocked("cmplx_CC_Loan_DDSBankAName",true);
					formObject.setLocked("cmplx_CC_Loan_DDSEntityNo",true);
					formObject.setLocked("DDS_save",true);
				}
				else if("flat".equals(formObject.getNGValue("cmplx_CC_Loan_DDSMode")))
				{
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);

				}

				else if("Per".equals(formObject.getNGValue("cmplx_CC_Loan_DDSMode")))
				{
					formObject.setLocked("cmplx_CC_Loan_Percentage",false);

				}
				else{
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
					formObject.setLocked("cmplx_CC_Loan_Percentage",true);
				}
				//ended By Akshay on 15/9/17 for disabling DDS tab if DDSFlag is false

				//code by saurabh on 8th Nov 2017
				if(formObject.getNGValue("cmplx_CC_Loan_HoldType").equalsIgnoreCase("T")){
					formObject.setLocked("cmplx_CC_Loan_HoldFrom_Date",false);
					formObject.setLocked("cmplx_CC_Loan_HOldTo_Date",false);
				}
				else{
					formObject.setLocked("cmplx_CC_Loan_HoldFrom_Date",true);
					formObject.setLocked("cmplx_CC_Loan_HOldTo_Date",true);
				}
				//code added by akshay on 11/11/17
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_F").equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_ModeOfSI"))){
					formObject.setLocked("cmplx_CC_Loan_FlatAMount",false);
					formObject.setLocked("cmplx_CC_Loan_SI_Percentage",true);

				}
				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_P").equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_ModeOfSI"))){
					formObject.setLocked("cmplx_CC_Loan_FlatAMount",true);
					formObject.setLocked("cmplx_CC_Loan_SI_Percentage",false);

				}
				else{
					formObject.setLocked("cmplx_CC_Loan_FlatAMount",true);
					formObject.setLocked("cmplx_CC_Loan_SI_Percentage",true);
				}
				accnum = formObject.getNGValue("Account_Number");
				formObject.setNGValue("creditcardNo", accnum);
			}
			//added by yash for CC FSD
			if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				notepad_load();
				//formObject.setTop("NotepadDetails_savebutton",410);
			}
			//ended by yash
			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				RLOS.mLogger.info( "Inside Decision Fragfetch" );
				loadPicklist3();
				formObject.setLocked("DecisionHistory_DecisionReasonCode",true);
				formObject.setLocked("cmplx_DecisionHistory_VerificationRequired",true);//added by Akshay on 28/9/17 for disabling CPV

				String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where (AcctType = 'CURRENT ACCOUNT' or AcctType = 'AMAL CURRENT ACCOUNT') AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
				try{
					List<List<String>> AccountCount= formObject.getNGDataFromDataCache(query);
					RLOS.mLogger.info( "Query is: "+query+" Value In AccountCount is "+AccountCount);
					RLOS.mLogger.info( "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
					formObject.setNGValue("AccountCount", AccountCount.get(0).get(0));
					if(("true".equals(formObject.getNGValue("cmplx_Customer_NTB")) || "0".equals(AccountCount.get(0).get(0))) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equals(formObject.getNGValue("Product_Type"))){
						RLOS.mLogger.info(" Product Type Is: "+formObject.getNGValue("Product_Type"));
						formObject.setVisible("DecisionHistory_Button3",true);
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
					//code by saurabh on 5th Dec.
					if(formObject.getNGValue("freeze_Form").equalsIgnoreCase("Y")){
						formObject.setNGValue("cmplx_DecisionHistory_Decision", "Reject");
						formObject.setNGFocus("Modify");
						formObject.setLocked("cmplx_DecisionHistory_Decision", true);
					}
					formObject.setVisible("DecisionHistory_Button3",false);//create CIF Acc
					
					setDataInMultipleAppGrid();
					
				}
				catch(Exception e)
				{
					RLOS.mLogger.info( "Exception occurred in Decision History tab:"+e.getMessage()+printException(e));
				}

			}
		}


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		RLOSCommonCode CommonCode=new RLOSCommonCode();
		try
		{
			switch (pEvent.getType()) 
			{		
			case FRAME_EXPANDED:
				CommonCode.call_Frame_Expanded(pEvent);
				break;

			case FRAME_COLLAPSED: {
				break;
			}

			case MOUSE_CLICKED:
				CommonCode.call_Mouse_Clicked(pEvent);
				break;

			case TAB_CLICKED:

				/*RLOS.mLogger.info(formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName())));
				//String SheetCaption=formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName()));
				if(formObject.isVisible("AltContactDetails_Frame1")==false){
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.setNGFrameState("Alt_Contact_container", 0);
					LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,1000 as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code desc ");// Load picklist added by aman to load the picklist in card dispatch to					
					//Deepak Code change to add requested product filter
					String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
					LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					

				}
				if(formObject.isVisible("CardDetails_Frame1")==false){
					formObject.fetchFragment("CardDetails", "CardDetails", "q_CardDetails");
					formObject.setNGFrameState("CardDetails", 0);
					if(NGFUserResourceMgr_RLOS.getResourceString_rlosIfConditions("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
						formObject.setVisible("CardDetails_Label7", true);
						formObject.setVisible("cmplx_CardDetails_statCycle", true);
						formObject.setVisible("CardDetails_Label3", false);
						formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", false);//added by akshay on 11/9/17
						formObject.setLeft("CardDetails_Label5", 288);
						formObject.setLeft("cmplx_CardDetails_SuppCardReq", 288);
					}	
					else if(NGFUserResourceMgr_RLOS.getResourceString_rlosIfConditions("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
						formObject.setLeft("CardDetails_Label5", 552);
						formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);
					}
					if(NGFUserResourceMgr_RLOS.getResourceString_rlosIfConditions("RLOS_Islamic").equalsIgnoreCase(formObject.getNGValue("LoanType_Primary"))){
						formObject.setVisible("CardDetails_Label2", true);
						formObject.setVisible("cmplx_CardDetails_CharityOrg", true);
						formObject.setVisible("CardDetails_Label4", true);
						formObject.setVisible("cmplx_CardDetails_CharityAmount", true);
						formObject.setLeft("CardDetails_Label5", 1059);
						formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);
					}	
				}
				frame_ALignment("Alt_Contact_container,CardDetails,FATCA,KYC,OECD,ReferenceDetails",formObject);
				 *///Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded

				break;

			case FRAGMENT_LOADED:
				call_Fragement_Loaded(pEvent);
				break;

			case VALUE_CHANGED:				
				CommonCode.call_Value_Changed(pEvent);
				break;



			default:
				break;

			}
		}

		finally{RLOS.mLogger.info( "Inside finally after try catch");}

	}	


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */


	public void saveFormStarted(FormEvent pEvent) 
	{
		RLOS.mLogger.info( "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		//formObject.setNGValue("initiationChannel", "Branch_Init");
		String init_Channel=formObject.getNGValue("initiationChannel");
		if(init_Channel!=null && "".equalsIgnoreCase(init_Channel)){
			formObject.setNGValue("initiationChannel","Branch_Init");
		}
		if(formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")>0){
			formObject.setNGValue("EmploymentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
			formObject.setNGValue("priority_ProdGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,9));
			formObject.setNGValue("Subproduct_productGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2));
		}
		formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By Akshay on 16/9/17 to set header
		formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
		formObject.setNGValue("Tl_No", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,5));//added By Akshay on 16/9/17 to set value i ext table column
		formObject.setNGValue("PassportNo",formObject.getNGValue("cmplx_Customer_PAssportNo"));
		formObject.setNGValue("MobileNo",formObject.getNGValue("cmplx_Customer_MobNo"));
		if("".equals(formObject.getNGValue("cmplx_Customer_MiddleName")))
		{
			formObject.setNGValue("Customer_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
		}

		else{
			formObject.setNGValue("Customer_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
		}

		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		formObject.setNGValue("NewApplicationNo", formObject.getWFFolderId());
		//added by akshay on 6/12/17
		formObject.setSelectedAllIndices("cmplx_FATCA_ListedReason");
		formObject.setSelectedAllIndices("cmplx_FATCA_SelectedReason");
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void saveFormCompleted(FormEvent pEvent) {
		RLOS.mLogger.info( "Inside saveFormCompleted()" + pEvent);


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		//formObject.setNGValue("initiationChannel", "Branch_Init");
		String init_Channel=formObject.getNGValue("initiationChannel");
		if(init_Channel!=null && "".equalsIgnoreCase(init_Channel)){
			formObject.setNGValue("initiationChannel","Branch_Init");
		}
		int itemIndex=formObject.getWFFolderId();
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);


	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void submitFormStarted(FormEvent pEvent)
	throws ValidatorException {
		RLOS.mLogger.info("Inside submitFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setNGValue("email_id", formObject.getUserName()+"@rlos.com");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		
		//formObject.setNGValue("CUSTOMERNAME", formObject.getNGValue("cmplx_Customer_EmiratesID"));

		//added by aman for Drop4 on 4th April
		multipleLiability();
		//added by aman for Drop4 on 4th April
		
		formObject.setNGValue("Application_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,4));

		
		try{
			int itemIndex=formObject.getWFFolderId();
			formObject.setNGValue("NewApplicationNo", itemIndex);
			formObject.setNGValue("ApplicationRefNo", itemIndex);
			if(!formObject.getNGValue("cmplx_DecisionHistory_Decision").equalsIgnoreCase("Reject")){
				IncomingDoc();
			}
validateStatusForSupplementary();
			RLOS.mLogger.info("after calling incomingdoc function");
			//added for document function
saveIndecisionGrid();
			RLOS.mLogger.info("Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));
			//temporary addition by saurabh on 29th Dec
			//formObject.saveFragment("OECD");

			TypeOfProduct();//Loan type
		}
		catch(Exception ex){
			RLOS.mLogger.info("Exception in submitformStarted: "+printException(ex));
		}
		finally{
			saveIndecisionGrid();
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void submitFormCompleted(FormEvent pEvent)
	throws ValidatorException {
		RLOS.mLogger.info( "Inside submitFormCompleted()" + pEvent);
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		RLOS.mLogger.info( "Inside continueExecution()" + eventHandler);
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void initialize() {
		RLOS.mLogger.info("Inside initialize() method");
		RLOS.mLogger.info("Inside initialize()");
		RLOS.mLogger.info("Alert Message :: "+NGFUserResourceMgr_RLOS.getAlert("VAL001"));
		RLOS.mLogger.info("IF Condition Value :: "+NGFUserResourceMgr_RLOS.getGlobalVar("IF001"));

		//throw new ValidatorException(new FacesMessage("Hello"));
	}
}
