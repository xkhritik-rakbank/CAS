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
import com.newgen.omniforms.FormConfig;import com.newgen.omniforms.FormReference;
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

		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");

		}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED

	Date Modified                       : 6/08/2017              
	Author                              : Akshay Gupta             
	Description                         : Form Populate         
branch
	 ***********************************************************************************  */

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Common_Utils common=new Common_Utils(RLOS.mLogger);
		try{
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_RLOS.getGlobalVar("Init_Mandatory_Frame"));
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
			new RLOSCommonCode().CustomerFragment_Load();
			//for sprint-1
			//formObject.setVisible("Customer_Frame2",false);
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			//RLOS.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
			//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = common.Convert_dateFormat("", "", "dd/MM/yyyy");
			//RLOS.mLogger.info( "currTime:" + currDate);
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
				formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("Final_Limit"));
			}
			//ended by Akshay on 25/9/17 as exception ws thrown when loading for first time/product grid empty
			formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNO"));
			formObject.setNGValue("RM_Name",getDataFromSourceCodeMaster("supervisor_name"));
			//formObject.setNGValue("BRANCH", getDataFromSourceCodeMaster("Branch"));
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
			if(formObject.getNGValue("EmploymentType").contains("Salaried") || formObject.getNGValue("EmploymentType").contains("Pensioner") ){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				formObject.setSheetVisible("ParentTab",1, false);
			}
			else if(formObject.getNGValue("EmploymentType").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed"))){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("COmpany_Name"));
				formObject.setSheetVisible("ParentTab",3, false);
			}
			/*if(formObject.getNGValue("Product_Type").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan")) && Float.parseFloat(formObject.getNGValue("Age"))<21f)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",1130);
			}
			else
			{
				formObject.setVisible("GuarantorDetails", false);
			}*/
		
			if(formObject.getNGValue("Product_Type").equals(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard"))){
				formObject.setSheetVisible("ParentTab",7, true);
			}
			//added by akshay on 29/1/18
			if("true".equals(formObject.getNGValue("existingOldCustomer"))){
				formObject.setLocked("existingOldCustomer",true);
				formObject.setLocked("OldApplicationNo",true);
				formObject.setLocked("Fetch_existing_cas",true);
			}
			//RLOS.mLogger.info("Value Of Init Channel:"+init_Channel);
			//change by saurabh for SOLID on form on 8th Feb 19.
			String Branch="";
			String strSolId = "";
			//LoadPickList("BRANCH", "select branch from NG_MASTER_SourceCode where UserId='"+formObject.getUserName()+"'");
			String Query="select branch,SOL_ID from NG_MASTER_SourceCode where UserId='"+formObject.getUserName()+"'";
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
		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		//RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_M").equalsIgnoreCase(formObject.getNGValue("initiationChannel")) ){
			//RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
			new RLOSCommonCode().DisableFragmentsOnLoad(pEvent);
		}
		else{
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				/*formObject.setHeight("Customer_Frame1", 790);
				formObject.setHeight("CustomerDetails", 800);*/
				formObject.setLocked("Customer_save",false);
				
				loadPicklistCustomer();
				formObject.setLocked("cmplx_Customer_CardNotAvailable",false);  
				/*formObject.setLocked("cmplx_Customer_EmiratesID",false);
				formObject.setLocked("cmplx_Customer_FIrstNAme",false);
				formObject.setLocked("cmplx_Customer_MiddleName",false);*/
				formObject.setLocked("ReadFromCard",false);
			/*	formObject.setLocked("cmplx_Customer_Title",false);
				formObject.setLocked("cmplx_Customer_LAstNAme",false);
				formObject.setLocked("cmplx_Customer_DOb",false);
				formObject.setLocked("cmplx_Customer_Nationality",false);
				formObject.setLocked("cmplx_Customer_MobNo",false);
				formObject.setLocked("cmplx_Customer_PAssportNo",false);
				formObject.setLocked("cmplx_Customer_gender",false);*/
				formObject.setLocked("cmplx_Customer_Aecb_Consent_Recieved",false);
				formObject.setLocked("cmplx_Customer_Email_ID",false);
				formObject.setLocked("cmplx_Customer_Basic_Sal",false);				
				formObject.setLocked("Customer_Check",false);
				formObject.setVisible("FetchDetails", false);
				formObject.setVisible("Customer_Button1", false);
				formObject.setLocked("cmplx_Customer_Confirmed_in_Job",false);	
				formObject.setLocked("Designation_button1",false);
				formObject.setLocked("cmplx_Customer_NEP",false);//enable
				formObject.setLocked("cmplx_Customer_VIPFlag", false);
				/*formObject.setLocked("cmplx_Customer_card_id_available",false);*/
				if("true".equals(formObject.getNGValue("cmplx_Customer_CardNotAvailable")) || "true".equals(formObject.getNGValue("cmplx_Customer_card_id_available")) || (!"".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null))
				{
					if ("true".equals(formObject.getNGValue("cmplx_Customer_card_id_available"))){
						/*formObject.setHeight("Customer_Frame1", 620);*/
						formObject.setLocked("ReadFromCard",true);
						formObject.setLocked("cmplx_Customer_NEP",true);
						//commented for sprint-1
						//formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
						formObject.setHeight("CustomerDetails", 700);
						//below lines added by akshay on 15/4/18
						/*formObject.setVisible("Customer_Frame2", true);
						formObject.setLocked("Customer_Frame2",false);
						formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700);*/
					}
					else if ("true".equals(formObject.getNGValue("cmplx_Customer_CardNotAvailable"))){
						//commented for sprint-1
						//formObject.setVisible("Customer_Frame2", true);
						formObject.setLocked("Customer_Frame2",false);
						/*formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700)*/;
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
					
				}
				



				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				//String NEP = formObject.getNGValue("cmplx_Customer_NEP");
				String card_not_avl = formObject.getNGValue("cmplx_Customer_CardNotAvailable");
				if("".equalsIgnoreCase(EID)  && "false".equalsIgnoreCase(card_not_avl)){
					//RLOS.mLogger.info(" In RLOS inside if customer in fragmnt loaded");

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

						//RLOS.mLogger.info(" In RLOS inside else customer in fragmnt loaded");
					}
				}
				//Added by aman to make the button non editable 7th Dec
				formObject.setLocked("Customer_Button1",true);
				//Added by aman to make the button non editable 7th Dec
				formObject.setLocked("cmplx_Customer_OTPValidation", true);
				formObject.setLocked("OTP_Mobile_NO", true);
				//RLOS.mLogger.info("Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
				if("Telesales_Init".equalsIgnoreCase(formObject.getWFActivityName()))
				{
					formObject.setLocked("cmplx_Customer_NEP",true);
				}

				try{
					String if_cif_available = formObject.getNGValue("cmplx_Customer_CIFNO");
					//RLOS.mLogger.info("CIF is: "+if_cif_available);
					if(if_cif_available !=null && !"".equalsIgnoreCase(if_cif_available) && !" ".equalsIgnoreCase(if_cif_available)){
						setcustomer_enable();
						//added by nikhil for OTP Validation
						formObject.setVisible("Customer_Frame2", true);
					}
					if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
						setcustomer_enable();
						//RLOS.mLogger.info("Check for cmplx_Customer_NTB"+"asd");
					}
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_card_id_available")) && "".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility"))){
						formObject.setLocked("Customer_Button1", false);
					}
					if("No".equals(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable"))){
						formObject.setLocked("cmplx_Customer_SecNationality", true);
						formObject.setLocked("SecNationality_Button", true);
					}
					//changed by nikhil PCSP-201
					else{
						formObject.setLocked("cmplx_Customer_SecNationality", true);
						formObject.setLocked("SecNationality_Button", false);	
					}
					//below code commented b nikhil as loadpicklist of applicant type fields does not happen 22nd november
					//openDemographicTabs();
					if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ISFetchDetails")))
					{
						formObject.setLocked("Customer_Check", true);
						formObject.setLocked("Eligibility_Check", false);
						formObject.setLocked("cmplx_Customer_DLNo", false);
						formObject.setLocked("cmplx_Customer_Passport2", false);
						formObject.setLocked("cmplx_Customer_Passport3", false);
						formObject.setLocked("cmplx_Customer_PAssport4", false);
						formObject.setLocked("cmplx_Customer_Employer_name", false);
						formObject.setLocked("cmplx_Customer_Employer_code", false);
						formObject.setLocked("Customer_search", false);
						formObject.setLocked("Customer_CheckBox7", false);
					}
					formObject.setHeight("Customer_Frame1", 790);
					formObject.setHeight("CustomerDetails", 800);
					
					
				}catch(Exception ex){
					RLOS.mLogger.info( printException(ex));
				} 
				if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO")))
				{
					formObject.setVisible("Customer_Frame2", true);
					formObject.setLocked("cmplx_Customer_MobNo", true);
				}
				else
				{
					formObject.setVisible("Customer_Frame2", false);
				}

			}

			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("Product_type", "select '--Select--' union select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock) where   IsActive='Y'");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				//formObject.setNGValue("ReqProd","--Select--");				
				formObject.setLocked("LastPermanentLimit",true);//Arun (07/12/17) as data will come from migration in this field				
				formObject.setLocked("LastTemporaryLimit",true);//Arun (07/12/17) as data will come from migration in this field
				formObject.setNGValue("Product_type", "Conventional");
				formObject.setNGValue("subProd", "SAL");
				formObject.setNGValue("AppType", "NEWE");
				formObject.setLocked("AppType", true);
				formObject.setNGValue("EmpType", "Salaried");
				formObject.setNGValue("ReqLimit", "5,000");
				/*formObject.setNGValue("", "");*/
			}

			else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				LoadPickList("title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
				formObject.setNGValue("title", "");
				formObject.setLocked("age_gua",true);//added for proc 10582
				//added by aman for Drop4 on 4th April
				LoadPickList("Guarantor_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
				LoadPickList("GuarantorDetails_empType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
				LoadPickList("EmploymentStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
				LoadPickList("GuarantorDetails_MaritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus with (nolock) order by Code");
				//added by aman for Drop4 on 4th April
				
				LoadPickList("GuarantorDetails_gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
				LoadPickList("GuarantorDetails_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
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
				int prod_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				String EmploymentType="";
				if(prod_count>0){
					EmploymentType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
				}//IncomeDetails_Frame3
				if (EmploymentType.equalsIgnoreCase("Salaried")||EmploymentType.equalsIgnoreCase("Salaried Pensioner")){
					formObject.setEnabled("cmplx_IncomeDetails_RentalIncome", true);
					formObject.setEnabled("cmplx_IncomeDetails_EducationalAllowance", true);
				
				}
				RLOSCommonCode.visibilityFrameIncomeDetails(formObject);
				 if("PA".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))) {
				
							RLOS.mLogger.info("RLOS Drop5 Type Value is isnisde:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2));
							formObject.setVisible("IncomeDetails_Frame2", true);
							formObject.setVisible("IncomeDetails_Frame3", true);
						//	formObject.setHeight("Incomedetails",1000);
						//	formObject.setHeight("IncomeDetails_Frame1",1700);
						}
					
				
				//added by akshay on 16/10/17 for point 6 in PL_NTB sheet-By default Value of accomodation should be disabled 
				if("Yes".equals(formObject.getNGValue("cmplx_IncomeDetails_Accomodation")))
				{
					formObject.setLocked("cmplx_IncomeDetails_AccomodationValue", false);
				}
				else
				{
					formObject.setLocked("cmplx_IncomeDetails_AccomodationValue", true);
				}
				//added by nikhil for Rachit PA CR
				if("PA".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
				{
					LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select 'Quarterly' union all select '--Select--' union all select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select 'Quarterly' union all select '--Select--' union all select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select 'Quarterly' union all select '--Select--' union all select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select 'Quarterly' union all select '--Select--' union all select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");

				}
				else
				{
					LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union all select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' all union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' all union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union all select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");

				}
				LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union all select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
				LoadPickList("cmplx_IncomeDetails_StatementCycle2", "select '--Select--' union all select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");

			/*	LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
*/
				//below query changed by saurabh on 12th Oct to populate correct masters for bank stat from.
				LoadPickList("IncomeDetails_BankStatFrom", "select '--Select--' union select convert(varchar, description) from NG_MASTER_othBankCAC with (nolock) where isactive='Y'");
				
				//below code added by nikhil for proc-3530
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equals(formObject.getNGValue("cmplx_Customer_CIFNO")))
				{
					formObject.setEnabled("IncomeDetails_FinacialSummarySelf", false);
				}
				else
				{
					formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
				}
				formObject.setNGValue("cmplx_IncomeDetails_Basic",formObject.getNGValue("cmplx_Customer_Basic_Sal"));
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank")) )
				{
				formObject.setNGValue("cmplx_IncomeDetails_SalaryXferToBank", "N");
				}
				 

			}

			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				//change by saurabh on 9th July 17.
				try{
					formObject.setNGValue("cmplx_Liability_New_AECBconsentAvail", formObject.getNGValue("cmplx_Customer_Aecb_Consent_Recieved"));
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
						if("CAC".equalsIgnoreCase(formObject.getNGValue("TargetSegmentCode")))
						{
						formObject.setVisible("cmplx_Liability_New_CACBankName", true);
						formObject.setVisible("Liability_New_Label5", true);
						}
						else 
						{
							formObject.setVisible("cmplx_Liability_New_CACBankName", false);
							formObject.setVisible("Liability_New_Label5", false);
						}

					}
					else{
						formObject.setVisible("FetchWorldCheck_SE", false);
						formObject.setVisible("CheckEligibility_SE", false);
						//change by saurabh on 8th Nov 2017.
						formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);
						if(formObject.getNGValue("Product_Type").contains("Credit Card")){
							formObject.setVisible("cmplx_Liability_New_CACBankName", true);
							formObject.setVisible("Liability_New_Label5", true);
							
							
						}
						if("CAC".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
						{
						formObject.setVisible("cmplx_Liability_New_CACBankName", true);
						formObject.setVisible("Liability_New_Label5", true);
						}
						else 
						{
							formObject.setVisible("cmplx_Liability_New_CACBankName", false);
							formObject.setVisible("Liability_New_Label5", false);
						}
						
					}
					LoadPickList("cmplx_Liability_New_CACBankName", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) order by code");
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
					LoadPickList("contractType","Select '--Select--' as Description,'' as code,'' as app_type union select convert(varchar,Description),code,app_type from ng_master_contract_type with(nolock) order by code'");
					}catch(Exception ex){
					printException(ex);
				}
			}


			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//RLOS.mLogger.info(" COMPANY FRAGMENT LOADED");
				try{
					formObject.setLocked("CompanyDetails_CIF", true);
					formObject.setLocked("CompanyDetails_Button3", true);
					LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
					LoadPickList("POAHolders", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_POAHolder with (nolock) where isactive='Y' order by code");
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
					LoadPickList("AuthorisedSignDetails_Status", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) where isActive='Y' order by code");
					//added by akshay on 15/1/18
					//need to be checked 
					/*int prd_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
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
					*/
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
							AddInAuthorisedGrid(formObject, customerName);
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

			
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//changes done to load Employer details. method added in common file.
				employment_details_load();
				
			}


			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				//RLOS.mLogger.info("Saurabh ELPINFO,Fragment Loaded");
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) where isactive = 'Y'  order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_takeoverBank", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) order by code");

				//added By Akshay on 29/9/17 for point 59-----No Of installment field empty
				int prd_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if(prd_rowCount>0){
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,7));
				}
				
				if(formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate").equals("")){
						calculateMaturityValues();
				}
				
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment", formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				//ended By Akshay on 29/9/17 for point 59-----No Of installment field empty
				//E modified to Equated by Saurabh on 12th Oct.
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestType","Equated");//added By Akshay on 28/9/17 for point 53 on consolidated sheet
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","M"); //added by aman 08/12
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_instrumenttype","S"); //added by aman 08/12
				//
				//
				/*formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);*/

				//formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
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
				formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq",true);
				//for TakeoverAmount by aman
				String queryTakeOver="select Sum(isNull(convert(float,Take_Amount),0)) from ng_rlos_cust_extexpo_LoanDetails  with (nolock) where   Wi_Name='"+formObject.getWFWorkitemName()+"'";
				//RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+queryTakeOver);
				List<List<String>> TakeOver=formObject.getNGDataFromDataCache(queryTakeOver);
				if(!TakeOver.isEmpty())
				{
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_TakeoverAMount",TakeOver.get(0).get(0));
				}
				//for TakeoverAmount by aman
				//Code to set the values in Elig and prod info grid from scheme
				if(formObject.getNGValue("Product_Type").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan"))){
					formObject.setVisible("ELigibiltyAndProductInfo_Label9", true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_EFCHidden", true);
					
					String query="Select PRIME_TYPE,LSM_PRODDIFFRATE,prime_type_rate from NG_master_Scheme where SCHEMEId='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
					//RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+query);
					List<List<String>> marginRate=formObject.getNGDataFromDataCache(query);
					if(!marginRate.isEmpty())
					{
						try{
							String baseRateType=marginRate.get(0).get(0).equalsIgnoreCase("")?"0":marginRate.get(0).get(0);
							String ProdprefRate=marginRate.get(0).get(1).equals("")?"0":marginRate.get(0).get(1);
							String baseRate=marginRate.get(0).get(2).equals("")?"0":marginRate.get(0).get(2);
							//RLOS.mLogger.info("List is:"+marginRate);
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
							int i= java.lang.Double.compare(marginrate, 0);// added for code optimization
							if(i!=0){
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
							//RLOS.mLogger.info("net payout query:"+outBal);
							List<List<String>> record = formObject.getNGDataFromDataCache(outBal);
							//RLOS.mLogger.info("List is:"+record);
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
								//RLOS.mLogger.info("lim is:"+lim);
								//RLOS.mLogger.info("bal is:"+bal);
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
			}//CardProd

			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//	LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to					
				//Deepak Code change to add requested product filter
				String TypeOfProd=formObject.getNGValue("LoanType_Primary");
				formObject.setNGValue("AlternateContactDetails_carddispatch","998");//sagarika CR
				LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					
				formObject.setVisible("AltContactDetails_Label7",false);
				formObject.setVisible("AlternateContactDetails_AirArabia",false);
				 AirArabiaValid();//sagarika
				
				//condition added by akshay on 26/3/18 for proc 7061
				if("false".equals(formObject.getNGValue("cmplx_Customer_NTB"))){
					 formObject.setLocked("AlternateContactDetails_MobileNo1", true);
						//added by nikhil for Sprint-2 Sanity!
						formObject.setNGValue("AlternateContactDetails_MobileNo1", formObject.getNGValue("cmplx_Customer_MobNo"));
					//formObject.setLocked("AlternateContactDetails_MobNo2",true);
					checkforCurrentAccounts();
				}
				formObject.setNGValue("AlternateContactDetails_MobileNo1", formObject.getNGValue("cmplx_Customer_MobNo"));
				//RLOS.mLogger.info("Saurabh COMPANY FRAGMENT LOADED"+formObject.getNGValue("PrimaryProduct"));
				
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
						formObject.setLocked("CardDispatchToButton", true);
						
					}
					else
					{
						formObject.setLocked("CardDispatchToButton", false);
					}
					

				}
				formObject.setNGValue("AlternateContactDetails_Email1", formObject.getNGValue("cmplx_Customer_Email_ID"));
				formObject.setNGValue("AlternateContactDetails_Email2", formObject.getNGValue("cmplx_Customer_Email_ID"));
				formObject.setNGValue("AlternateContactDetails_MobileNo1", formObject.getNGValue("cmplx_Customer_MobNo"));
				//ADDED BY NIKHIL FOR PCAS-3257
				formObject.setNGValue("AlternateContactDetails_MobNo2", formObject.getNGValue("cmplx_Customer_MobNo"));
				//ended By Akshay on 28/9/17 for hiding retain account when product is CC
			}


			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				try{
					LoadPickList("cmplx_CardDetails_statCycle","select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
					//if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salaried").equalsIgnoreCase(formObject.getNGValue("empType")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("empType"))){
					FieldsVisibility_CardDetails(formObject);//changed by akshay for proc 6462
					if(formObject.getNGValue("cmplx_CardDetails_SuppCardReq").equalsIgnoreCase("Yes") || formObject.getNGValue("cmplx_CardDetails_SuppCardReq").equalsIgnoreCase("Y") ){
						formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");
						formObject.setNGFrameState("Supplementary_Container",0);
						if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("employmentType")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salaried").equalsIgnoreCase(formObject.getNGValue("employmentType")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Pensioner").equalsIgnoreCase(formObject.getNGValue("employmentType"))){
							formObject.setVisible("CompEmbName", false);
							formObject.setVisible("SupplementCardDetails_Label7", false);
						}
						adjustFrameTops("Supplementary_Container,FATCA_container,KYC_container,OECD_container,ReferenceDetails_container");

					}
					if(formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")>0){
						if ("LI".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
						{
							formObject.setLocked("CardDetails_Frame1",true);
						}
					}
					//below code done for 8816
					if("Telesales_Init".equalsIgnoreCase(formObject.getWFActivityName()) && "IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
					{
						formObject.setVisible("CardDetails_Label5", false);
						formObject.setVisible("cmplx_CardDetails_SuppCardReq", false);
					}
					
				}catch(Exception ex){
					printException(ex);
				}
			}

			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				openDemographicTabs();//added by akshay on 13/5/18 for proc 8638

				LoadPickList("ResdCountry", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by code");
				LoadPickList("relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
				LoadPickList("nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
				LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
				//complex query to load supplementary card products by saurabh
				LoadPickList("SupplementCardDetails_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
				LoadPickList("SupplementCardDetails_EmploymentStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmploymentStatus with (nolock)");
				LoadPickList("SupplementCardDetails_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("SupplementCardDetails_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus with (nolock) order by Code");
				formObject.setLocked("SupplementCardDetails_PassportIssueDate",false);
				formObject.setLocked("SupplementCardDetails_VisaIssueDate",false);
				formObject.setLocked("SupplementCardDetails_IDIssueDate",false);
				formObject.setEnabled("SupplementCardDetails_PassportIssueDate",true);
				formObject.setEnabled("SupplementCardDetails_VisaIssueDate",true);
				formObject.setEnabled("SupplementCardDetails_IDIssueDate",true);
				formObject.setLocked("DOB",false);
				formObject.setEnabled("DOB",true);
			}
			else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from NG_MASTER_Relationship with (nolock) order by code");
				formObject.setNGValue("ReferenceDetails_ref_Relationship", "FRE");
			}
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

				//RLOS.mLogger.info("Inside Fatca Initiation");
				loadPicklist_Fatca();//added by prabhakar drop-4 point-3
				/*LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
				LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");*/
				FATCA_Validations(formObject);
			}

			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

				//RLOS.mLogger.info("Inside KYC Initiation");
				loadPicklist_Kyc();//added by prabhakar drop-4 point-3
			}
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {

				//RLOS.mLogger.info("Inside Fatca Initiation");
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
				try{
				//RLOS.mLogger.info( "Inside Decision Fragfetch" );
				loadPicklist3();
				if("false".equals(formObject.getNGValue("cmplx_Customer_NTB")) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
					checkforCurrentAccounts();
				}
				formObject.setLocked("DecisionHistory_DecisionReasonCode",true);
				formObject.setLocked("cmplx_DecisionHistory_VerificationRequired",true);//added by Akshay on 28/9/17 for disabling CPV
				
				/*---commented by akshay on 19/4/18 as shifted to grid row click
				 * String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where (AcctType = 'CURRENT ACCOUNT' or AcctType = 'AMAL CURRENT ACCOUNT') AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
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
					{*/
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
					//}
					//code by saurabh on 5th Dec.
					if(formObject.getNGValue("freeze_Form").equalsIgnoreCase("Y")){
						formObject.setNGValue("cmplx_DecisionHistory_Decision", "Reject");
						formObject.setNGFocus("Modify");
						formObject.setLocked("cmplx_DecisionHistory_Decision", true);
					}
					formObject.setVisible("DecisionHistory_Button3",false);//create CIF Acc
					
					
				}
				catch(Exception e)
				{
					RLOS.mLogger.info( "Exception occurred in Decision History tab:"+e.getMessage()+printException(e));
				}
				finally{
					setDataInMultipleAppGrid();

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

		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		RLOSCommonCode CommonCode=new RLOSCommonCode();
		
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


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */


	public void saveFormStarted(FormEvent pEvent) 
	{
		//RLOS.mLogger.info( "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Cif_Id", formObject.getNGValue("cmplx_Customer_CIFNO"));
		//RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		if(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")!=null){
		formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		}
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
		formObject.setNGValue("COmpany_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
		formObject.setNGValue("Tl_No", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,5));//added By Akshay on 16/9/17 to set value i ext table column
		formObject.setNGValue("PassportNo",formObject.getNGValue("cmplx_Customer_PAssportNo"));
		formObject.setNGValue("MobileNo",formObject.getNGValue("cmplx_Customer_MobNo"));
		
		if("".equals(formObject.getNGValue("cmplx_Customer_MiddleName")))
		{
			formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			//changed from customer_name to customerName by akshay on 17/4/18
		}

		else{
			formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			//changed from customer_name to customerName by akshay on 17/4/18
		}

		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		formObject.setNGValue("NewApplicationNo", formObject.getWFFolderId());
		//added by akshay on 6/12/17
		formObject.setSelectedAllIndices("cmplx_FATCA_ListedReason");
		formObject.setSelectedAllIndices("cmplx_FATCA_SelectedReason");
		//Deepak Changes for PCAS-3033
		if(formObject.isVisible("DecisionHistory_Frame1")){
			Custom_fragmentSave("DecisionHistoryContainer");
		}
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void saveFormCompleted(FormEvent pEvent) {
		//RLOS.mLogger.info( "Inside saveFormCompleted()" + pEvent);


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info("Before custom save form");
		CustomSaveForm();
		RLOS.mLogger.info("After custom save form");
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		//RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
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
		
		
		//added by saurabh on 23rd May.
		formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		if("RESC".equalsIgnoreCase(formObject.getNGValue("Application_Type")) || "RESN".equalsIgnoreCase(formObject.getNGValue("Application_Type")) || "RESR".equalsIgnoreCase(formObject.getNGValue("Application_Type")))
			formObject.setNGValue("initiationChannel","Reschedulment");
		else{
			formObject.setNGValue("initiationChannel","Branch_Init");//added by akshay on 17/4/18			
		}
			
        formObject.setNGValue("email_id", formObject.getNGValue("AlternateContactDetails_Email1"));
        formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
        //added by nikhil for PCAS-1222
        formObject.setNGValue("DSA_Name", formObject.getUserName());
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		formObject.setNGValue("CPV_Required", formObject.getNGValue("cmplx_DecisionHistory_VerificationRequired"));
		//code by saurbh on 7th Dec
		String  tai = formObject.getNGValue("cmplx_Liability_New_TAI");
		float taiVal=0;
		if(!"".equals(tai) && null!=tai){
			taiVal = Float.parseFloat(tai);
		}
		if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag")) || taiVal>=20000){
			formObject.setNGValue("VIPFlag", "Y");
		}
		RLOS.mLogger.info("Before Product_DedupeCheck");
		Product_DedupeCheck();
		RLOS.mLogger.info("After Product_DedupeCheck");
		//formObject.setNGValue("CUSTOMERNAME", formObject.getNGValue("cmplx_Customer_EmiratesID"));
		//added by nikhil for PCSP-427
		RLOS.mLogger.info("Before EFC Check");
		Check_EFC_Limit();
		//added by nikhil for PCAS-2312
		setDataInMultipleAppGrid();
		//added by aman for Drop4 on 4th April
		multipleLiability();
		//added by aman for Drop4 on 4th April
		
		formObject.setNGValue("Application_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,4));

		
		try{
			int itemIndex=formObject.getWFFolderId();
			formObject.setNGValue("NewApplicationNo", itemIndex);
			formObject.setNGValue("ApplicationRefNo", itemIndex);
			if(!formObject.getNGValue("cmplx_DecisionHistory_Decision").equalsIgnoreCase("Reject") && formObject.isVisible("IncomingDoc_Frame")){
				IncomingDoc();
			}
			validateStatusForSupplementary();
			
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("DecisionHistoryContainer");//added by akshay on 18/9/18
			//RLOS.mLogger.info("after calling incomingdoc function");
			//added for document function
			//RLOS.mLogger.info("Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));
			//temporary addition by saurabh on 29th Dec
			//formObject.saveFragment("OECD");
			CustomSaveForm();
			TypeOfProduct();//Loan type
			
		}
		catch(Exception ex){
			RLOS.mLogger.info("Exception in submitformStarted: "+printException(ex));
		}
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */
//before function def
	public void submitFormCompleted(FormEvent pEvent)
	throws ValidatorException {
		//RLOS.mLogger.info( "Inside submitFormCompleted()" + pEvent);
		saveIndecisionGrid();
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		//RLOS.mLogger.info( "Inside continueExecution()" + eventHandler);
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : akshay             
	Description                         : Default product function         

	 ***********************************************************************************  */

	public void initialize() {
	/*	RLOS.mLogger.info("Inside initialize() method");
		RLOS.mLogger.info("Inside initialize()");
		RLOS.mLogger.info("Alert Message :: "+NGFUserResourceMgr_RLOS.getAlert("VAL001"));
		RLOS.mLogger.info("IF Condition Value :: "+NGFUserResourceMgr_RLOS.getGlobalVar("IF001"));
*/
		//throw new ValidatorException(new FacesMessage("Hello"));
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
