package com.newgen.omniforms.user;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;

@SuppressWarnings("serial")

public class RLOS_Initiation extends RLOSCommon implements FormListener
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	
	
	//RLOS.mLogger.info("Inside initiation RLOS");
	public void formLoaded(FormEvent pEvent)
	{
		RLOS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
	}

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
			new RLOSCommonCode().CustomerFragment_Load();

			RLOS.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			RLOS.mLogger.info( "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));//added By akshay on 25/9/17 as per point 14
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Branch Initiation");
			formObject.setNGValue("Created_By",formObject.getUserName());
			formObject.setNGValue("Cust_Name",formObject.getNGValue("Customer_Name"));

			formObject.setNGValue("lbl_init_channel_val","Branch_Init");

			formObject.setNGValue("lbl_curr_date_val",currDate);
			formObject.setNGValue("ApplicationRefNo", formObject.getWFFolderId());
			formObject.setNGValue("lbl_user_name_val",formObject.getUserName());
			formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
			formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Subproduct_productGrid"));
			formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));

			//added by Akshay on 25/9/17 as exception ws thrown when loading for first time/product grid empty
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n>0){
				formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
			}
			//ended by Akshay on 25/9/17 as exception ws thrown when loading for first time/product grid empty

			formObject.setNGValue("RM_Name",getRMName());
			formObject.setNGValue("lbl_TL_Name_val",formObject.getNGValue("RM_Name"));

			String init_Channel=formObject.getNGValue("initiationChannel");
			if(init_Channel.equals(""))
				formObject.setNGValue("initiationChannel","Branch_Init");
			if(formObject.getNGValue("empType").contains("Salaried")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				formObject.setSheetVisible("ParentTab",1, false);
			}

			else if(formObject.getNGValue("empType").contains("Self Employed")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
				formObject.setSheetVisible("ParentTab",3, false);
			}


			if(formObject.getNGValue("Product_Type").contains("Personal Loan") && Integer.parseInt(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",1200);
			}
			if(formObject.getNGValue("PrimaryProduct").equals("Credit Card"))
				formObject.setSheetVisible("ParentTab",7, true);


			RLOS.mLogger.info("Value Of Init Channel:"+init_Channel);



		}catch(Exception e)

		{
			RLOS.mLogger.info( "Exception occurred in form populated: "+e.getMessage()+printException(e));

		}
	}
	
	public void call_Fragement_Loaded(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
		if(formObject.getNGValue("initiationChannel").equalsIgnoreCase("M") ){
			RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
			new RLOSCommonCode().DisableFragmentsOnLoad(pEvent);
		}
		else{
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
				formObject.setLocked("Customer_Frame1",true);
				formObject.setHeight("Customer_Frame1", 510);
				formObject.setHeight("CustomerDetails", 530);
				formObject.setLocked("Customer_save",false);
				formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
				formObject.setEnabled("cmplx_Customer_card_id_available",true);
				formObject.setLocked("cmplx_Customer_NEP",false);
				formObject.setLocked("ReadFromCard",false);

				if(formObject.getNGValue("cmplx_Customer_CardNotAvailable").equals("true") || formObject.getNGValue("cmplx_Customer_card_id_available").equals("true") || formObject.getNGValue("cmplx_Customer_NEP").equals("true"))
				{
					if (formObject.getNGValue("cmplx_Customer_card_id_available").equals("true")){
						formObject.setLocked("ReadFromCard",true);
						formObject.setLocked("cmplx_Customer_NEP",true);
						formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
					}
					else if (formObject.getNGValue("cmplx_Customer_CardNotAvailable").equals("true")){
						formObject.setVisible("Customer_Frame2", true);
						formObject.setLocked("Customer_Frame2",false);
						formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700);
						formObject.setLocked("cmplx_Customer_NEP",true);
						formObject.setLocked("ReadFromCard",true);
						formObject.setEnabled("cmplx_Customer_card_id_available",false);

					}
					else if(formObject.getNGValue("cmplx_Customer_NEP").equals("true")){						
						formObject.setLocked("Customer_Frame1",false);
						formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
						formObject.setLocked("ReadFromCard",true);
						formObject.setEnabled("cmplx_Customer_card_id_available",false);

					}
				}
				formObject.setLocked("cmplx_Customer_DLNo",false);
				formObject.setLocked("cmplx_Customer_Passport2",false);
				formObject.setLocked("cmplx_Customer_Passport3",false);
				formObject.setLocked("cmplx_Customer_PAssport4",false);
				RLOS.mLogger.info( "");
				//formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);


				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				String NEP = formObject.getNGValue("cmplx_Customer_NEP");
				String card_not_avl = formObject.getNGValue("cmplx_Customer_CardNotAvailable");
				if(EID.equalsIgnoreCase("") && NEP.equalsIgnoreCase("false") && card_not_avl.equalsIgnoreCase("false")){
					RLOS.mLogger.info(" In RLOS inside if customer in fragmnt loaded");
					//formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);
				}
				else{
					String is_fetched_details = formObject.getNGValue("cmplx_Customer_ISFetchDetails");
					if(!"Y".equalsIgnoreCase(is_fetched_details)){
						formObject.setLocked("cmplx_Customer_FIrstNAme",false);
						formObject.setLocked("cmplx_Customer_MiddleName",false);
						formObject.setLocked("cmplx_Customer_LAstNAme",false);
						formObject.setEnabled("cmplx_Customer_DOb",true);
						formObject.setLocked("cmplx_Customer_PAssportNo",false);
						formObject.setLocked("cmplx_Customer_Nationality",false);
						formObject.setLocked("cmplx_Customer_MobNo",false);
						formObject.setLocked("FetchDetails",true);
						formObject.setVisible("Customer_Label56", true);
						formObject.setVisible("cmplx_Customer_EIDARegNo", true);
						//		formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);
						RLOS.mLogger.info(" In RLOS inside else customer in fragmnt loaded");
					}
				}
				formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
				formObject.setEnabled("cmplx_Customer_OTPValidation", false);
				loadPicklistCustomer();
				RLOS.mLogger.info("Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));

				try{
					String if_cif_available = formObject.getNGValue("cmplx_Customer_CIFNO");
					RLOS.mLogger.info("CIF is: "+if_cif_available);
					if(if_cif_available !=null && !if_cif_available.equalsIgnoreCase("") && !if_cif_available.equalsIgnoreCase(" ")){
						setcustomer_enable();
						RLOS.mLogger.info("Saurabh12345");
					}
					if (formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
						setcustomer_enable();
						RLOS.mLogger.info("Check for cmplx_Customer_NTB"+"asd");
					}
					if(formObject.getNGValue("cmplx_Customer_card_id_available").equalsIgnoreCase("true") && "".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility"))){
						formObject.setLocked("Customer_Button1", false);
					}
					if(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable").equals("No")){
						formObject.setLocked("cmplx_Customer_SecNationality", true);
					}
				}catch(Exception ex){
					RLOS.mLogger.info( printException(ex));
				}

			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				formObject.setNGValue("ReqProd","--Select--");
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {

				formObject.setNGValue("gender", "");
				formObject.setNGValue("nationality", "--Select--");
				// formObject.setNGValue("SignStatus", "--Select--");
				LoadPickList("title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
				formObject.setNGValue("title", "");
				//LoadPickList("gender", "select convert(varchar, description) from NG_MASTER_gender with (nolock)");
				LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
				LoadPickList("nationality", "select convert(varchar, description) from NG_MASTER_country with (nolock)");
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
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
				if(formObject.getNGValue("cmplx_IncomeDetails_Accomodation").equals("Yes"))
					formObject.setLocked("cmplx_IncomeDetails_AccomodationValue", false);

				LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
				//change by saurabh on 9th July 17.
				if(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail").equalsIgnoreCase("true")){
					formObject.setLocked("Liability_New_fetchLiabilities",false);
				}
				else{
					formObject.setLocked("Liability_New_fetchLiabilities",true);
				}

				//added by abhishek
				int count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				for(int i =0;i<count;i++){
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("BTC") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("SE")){
						formObject.setVisible("Label9", false);
						formObject.setVisible("cmplx_Liability_New_DBR", false);
						formObject.setVisible("Label15", false);
						formObject.setVisible("cmplx_Liability_New_TAI", false);
						formObject.setVisible("Liability_New_Label14", false);
						formObject.setVisible("cmplx_Liability_New_DBRNet", false);
						formObject.setVisible("Button2", false);
						formObject.setLeft("Liability_New_Label15", 7);
						formObject.setLeft("cmplx_Liability_New_AggrExposure", 7);

					}

				}
			if((formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6).equalsIgnoreCase("Self Employed"))||(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6).equalsIgnoreCase("SE"))){
					
					formObject.setVisible("FetchWorldCheck_SE", true);
					formObject.setVisible("CheckEligibility_SE", true);
					formObject.setVisible("Button2", false);
					

				}
			else{
				formObject.setVisible("FetchWorldCheck_SE", false);
				formObject.setVisible("CheckEligibility_SE", false);
			}
				//formObject.setLocked("Liability_New_fetchLiabilities",true);
				//formObject.setLocked("takeoverAMount",true);
				formObject.setLocked("cmplx_Liability_New_DBR",true);
				formObject.setLocked("cmplx_Liability_New_DBRNet",true);
				formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
				formObject.setLocked("cmplx_Liability_New_TAI",true);
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).equalsIgnoreCase("RESCE")){
					formObject.setVisible("Liability_New_Label25", true);
					formObject.setVisible("cmplx_Liability_New_paid_installments", true);
				}
				else{
					formObject.setVisible("Liability_New_Label25", false);
					formObject.setVisible("cmplx_Liability_New_paid_installments", false);
				}
			}


			else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
				RLOS.mLogger.info(" COMPANY FRAGMENT LOADED");
				formObject.setLocked("cif", true);
				formObject.setLocked("CompanyDetails_Button3", true);
				LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
				LoadPickList("NepType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_NEPType with (nolock) order by code");
				LoadPickList("ApplicationCategory", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");
				LoadPickList("indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_IndustrySector with (nolock) order by code");
				LoadPickList("indusMAcro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMacro with (nolock) order by code");
				LoadPickList("indusMicro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMicro with (nolock) order by code");
				LoadPickList("legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_LegalEntity with (nolock) order by code");
				LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("EOW", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
				LoadPickList("headOffice", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
				LoadPickList("TradeLicencePlace", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from ng_master_TradeLicensePlace with (nolock) order by code");
				LoadPickList("CompanyDetails_POAHolder", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_POAHolder with (nolock) order by code");

				try{
					String customerName = formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
					if(customerName!=null && !customerName.equalsIgnoreCase("") && !customerName.equalsIgnoreCase(" ")){
						String Executexml=RLOSCommonCode.makeInputForGrid(customerName);
						RLOS.mLogger.info("Grid Data is"+Executexml);
						formObject.NGAddListItem("cmplx_CompanyDetails_cmplx_CompanyGrid",Executexml);
						formObject.RaiseEvent("WFSave");
						
					}
				}catch(Exception ex){
					RLOS.mLogger.info("Exception: "+printException(ex));
				}
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
				formObject.setLocked("AuthorisedSignDetails_CIFNo", true);
				formObject.setLocked("AuthorisedSignDetails_FetchDetails", true);
				formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
				//formObject.setNGValue("AuthorisedSignDetails_nationality", "");
				loadPicklist_AuthSign();
				String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
				String name = formObject.getNGValue("cmplx_Customer_FIrstNAme");
				String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
				String dob = formObject.getNGValue("cmplx_Customer_DOb");
				String visanum = formObject.getNGValue("cmplx_Customer_VisaNo");
				String visaexp = formObject.getNGValue("cmplx_Customer_VIsaExpiry");
				String pass = formObject.getNGValue("cmplx_Customer_PAssportNo");
				String passexp = formObject.getNGValue("cmplx_Customer_PassPortExpiry");

				String Executexml= "<ListItem><SubItem>"+cif+"</SubItem>" +
						"<SubItem>"+name+"</SubItem>"+
						"<SubItem>"+nationality+"</SubItem>"+
						"<SubItem>"+dob+"</SubItem>"+
						"<SubItem>"+visanum+"</SubItem>"+
						"<SubItem>"+visaexp+"</SubItem>"+
						"<SubItem></SubItem>"+
						"<SubItem>"+pass+"</SubItem>"+
						"<SubItem>"+passexp+"</SubItem>"+
						"<SubItem></SubItem>"+
						"<SubItem></SubItem>"+
						"<SubItem>"+formObject.getWFWorkitemName()+"</SubItem></ListItem>";
				RLOS.mLogger.info("Grid Data is"+Executexml);
				formObject.NGAddListItem("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",Executexml);
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
				LoadPickList("PartnerDetails_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
			}


			else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
				RLOS.mLogger.info("Inside EMploymentDetails fragment load");
				if(formObject.getNGValue("cmplx_Customer_NEP").equals("false")){
					formObject.setLocked("cmplx_EmploymentDetails_NepType",true);//Added by Akshay on 9/9/17 for enabling NEP type as per FSD							
				}
				//Added by Akshay on 25/9/17 for hiding emp contract type
				if(formObject.getNGValue("PrimaryProduct").equals("Credit Card")){
					formObject.setVisible("cmplx_EmploymentDetails_EmpContractType",false);
					formObject.setVisible("EMploymentDetails_Label71",false);
				}
				//ended by Akshay on 25/9/17 for hiding emp contract type
				
				//added by akshay on 11/10/17 for disabling alloc fields 
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
				formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
				//ended by akshay on 11/10/17 for disabling alloc fields 

				setALOCListed();
				formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
				formObject.setVisible("EMploymentDetails_Label5",false);

				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				formObject.setVisible("EMploymentDetails_Label36",false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
				formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
				formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
				loadPicklist4();
				Fields_ApplicationType_Employment();
				//added By Akshay on 12/9/2017 to set this field as CAC if CAC is true
				if(formObject.getNGValue("Is_CAC_Checked").equals("Y"))
				{
					formObject.setNGValue("cmplx_EmploymentDetails_targetSegCode", "CAC");
				}
				//ended By Akshay on 12/9/2017 to set this field as CAC if CAC is true


				//Added by Akshay on 25/9/17 for enabling indus Segment for surrogate
				if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("EMPID") && formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg").equals("Surrogate")){
					formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);

				}
				else{
					formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",true);

				}
				//ended by Akshay on 25/9/17 for enabling indus Segment for surrogate



			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
				formObject.setLocked("cmplx_MiscFields_School",true);
				formObject.setLocked("cmplx_MiscFields_PropertyType",true);
				formObject.setLocked("cmplx_MiscFields_RealEstate",true);
				formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
				RLOS.mLogger.info("Saurabh ELPINFO,Fragment Loaded");
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");

				//added By Akshay on 29/9/17 for point 59-----No Of installment field empty
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,7));
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment", formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				//ended By Akshay on 29/9/17 for point 59-----No Of installment field empty

				formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestType","E");//added By Akshay on 28/9/17 for point 53 on consolidated sheet
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
				//formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
				//formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);

				formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_Insurance",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_NumberOfInstallment",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BAseRate",true);	
				//formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);
				//Code to set the values in Elig and prod info grid from scheme
				if(formObject.getNGValue("Product_Type").contains("Personal Loan")){
					String query="Select PRIME_TYPE,LSM_PRODDIFFRATE,prime_type_rate from NG_master_Scheme where SCHEMEId='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
					RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+query);
					List<List<String>> marginRate=formObject.getDataFromDataSource(query);
					if(!marginRate.isEmpty())
					{
						String baseRateType=marginRate.get(0).get(0).equalsIgnoreCase("")?"0":marginRate.get(0).get(0);
						String baseRate=marginRate.get(0).get(1).equals("")?"0":marginRate.get(0).get(1);
						String ProdprefRate=marginRate.get(0).get(2).equals("")?"0":marginRate.get(0).get(2);
						RLOS.mLogger.info("List is:"+marginRate);
						String netRate=formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_BAseRate", baseRate);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_BaseRateType", baseRateType);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate", ProdprefRate);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",netRate);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_MArginRate", Float.parseFloat(netRate)-Float.parseFloat(baseRate)-Float.parseFloat(ProdprefRate));
						}
				}	
				//Code to set the values in Elig and prod info grid from scheme end
			}


			else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {

				loadPicklist_Address();
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
				//added By Akshay on 28/9/17 for hiding retain account when product is CC
				/*if(!formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Personal Loan")){
					formObject.setVisible("AlternateContactDetails_custdomicile",false);
					formObject.setVisible("AltContactDetails_Label14",false);
					formObject.setVisible("AlternateContactDetails_retainAccount",true);

				}*/
				RLOS.mLogger.info("Saurabh COMPANY FRAGMENT LOADED"+formObject.getNGValue("PrimaryProduct"));
				if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Credit Card")){
					formObject.setVisible("AlternateContactDetails_retainAccount",false);
					formObject.setVisible("AlternateContactDetails_custdomicile",false);
					formObject.setVisible("AltContactDetails_Label14",false);
				}
				else{
					formObject.setVisible("AlternateContactDetails_retainAccount",true);
					formObject.setVisible("AlternateContactDetails_custdomicile",true);
					formObject.setVisible("AltContactDetails_Label14",true);
				}
				//ended By Akshay on 28/9/17 for hiding retain account when product is CC
			}


			else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
				formObject.setLeft("CardDetails_Label5",288);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq",288);
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
				LoadPickList("ResdCountry", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by code");
				LoadPickList("relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
				LoadPickList("nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
				LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
				LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--' as description,'' as code union select convert(varchar, description) from NG_MASTER_Relationship with (nolock) order by code");
				formObject.setNGValue("ReferenceDetails_ref_Relationship", "FRE");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
				LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
				//LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
				LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");

				String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
				if(usRelation.equalsIgnoreCase("O")){
					formObject.setEnabled("FATCA_Frame6", false);
					formObject.setEnabled("cmplx_FATCA_USRelation",true);
					formObject.setEnabled("FATCA_Save", true);
				}
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
				LoadPickList("OECD_CountryBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				LoadPickList("OECD_townBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
				LoadPickList("OECD_CountryTaxResidence", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				LoadPickList("OECD_CRSFlagReason", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by code");
				formObject.setLocked("OECD_CRSFlagReason",true);
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
				formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
				formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
				formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
				loadPicklist_ServiceRequest();
				//added By Akshay on 15/9/17 for disabling DDS tab if DDSFlag is false
				if(formObject.getNGValue("cmplx_CC_Loan_DDSFlag").equals("false"))
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
				}
				else if(formObject.getNGValue("cmplx_CC_Loan_DDSMode").equals("flat"))
				{
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);

				}

				else if(formObject.getNGValue("cmplx_CC_Loan_DDSMode").equals("Per"))
				{
					formObject.setLocked("cmplx_CC_Loan_Percentage",false);

				}
				else{
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
					formObject.setLocked("cmplx_CC_Loan_Percentage",true);
				}
				//ended By Akshay on 15/9/17 for disabling DDS tab if DDSFlag is false

			}
			//added by yash for CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

				notepad_load();
				//formObject.setTop("NotepadDetails_savebutton",410);
			}
			//ended by yash
			if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
				loadPicklist3();
				formObject.setNGValue("cmplx_DecisionHistory_Decision", "--Select--");
				formObject.setLocked("cmplx_DecisionHistory_DecisionReasonCode",true);
				formObject.setLocked("cmplx_DecisionHistory_VerificationRequired",true);//added by Akshay on 28/9/17 for disabling CPV

				String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
				try{
					List<List<String>> AccountCount= formObject.getDataFromDataSource(query);
					RLOS.mLogger.info( "Query is: "+query+" Value In AccountCount is "+AccountCount);
					RLOS.mLogger.info( "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
					formObject.setNGValue("AccountCount", AccountCount.get(0).get(0));
					if((formObject.getNGValue("cmplx_Customer_NTB").equals("true") || AccountCount.get(0).get(0).equals("0")) && formObject.getNGValue("Product_Type").equals("Personal Loan")){
						RLOS.mLogger.info(" Product Type Is: "+formObject.getNGValue("Product_Type"));
						formObject.setVisible("DecisionHistory_Button3",true);
					}

					else
					{
						if (formObject.getNGValue("Product_Type").equalsIgnoreCase("Credit Card")){
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

						}
						formObject.setVisible("DecisionHistory_Button3",false);//create CIF Acc
					}
				}
				catch(Exception e)
				{
					RLOS.mLogger.info( "Exception occurred in Decision History tab:"+e.getMessage()+printException(e));
				}

			}
		}


	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		
			switch (pEvent.getType()) 
			{		
			
			case FRAME_EXPANDED:
				new RLOSCommonCode().call_Frame_Expanded(pEvent);
				break;

			case MOUSE_CLICKED:
				new RLOSCommonCode().call_Mouse_Clicked(pEvent);
				break;

			case TAB_CLICKED:

				RLOS.mLogger.info(formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName())));
				//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded
				break;

			case FRAGMENT_LOADED:
				call_Fragement_Loaded(pEvent);
				break;

			case VALUE_CHANGED:				
				new RLOSCommonCode().call_Value_Changed(pEvent);
				 break;
			
			default:
				break;

			}
		
	}	
	
	
	public void saveFormStarted(FormEvent pEvent) 
	{
		RLOS.mLogger.info( "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");
		formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
		formObject.setNGValue("priority_ProdGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,9));
		formObject.setNGValue("Subproduct_productGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2));
		formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By Akshay on 16/9/17 to set header
		formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
		formObject.setNGValue("Tl_No", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,5));//added By Akshay on 16/9/17 to set value i ext table column
		formObject.setNGValue("PassportNo",formObject.getNGValue("cmplx_Customer_PAssportNo"));
		formObject.setNGValue("MobileNo",formObject.getNGValue("cmplx_Customer_MobNo"));
		if(formObject.getNGValue("cmplx_Customer_MiddleName").equals(""))
		{
			formObject.setNGValue("Customer_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
		}

		else{
			formObject.setNGValue("Customer_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
		}

		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		formObject.setNGValue("NewApplicationNo", formObject.getWFFolderId());
	}

	public void saveFormCompleted(FormEvent pEvent) {
		RLOS.mLogger.info( "Inside saveFormCompleted()" + pEvent);


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");


		int itemIndex=formObject.getWFFolderId();
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);


	}



	public void submitFormStarted(FormEvent pEvent)
			throws ValidatorException {
		RLOS.mLogger.info( "Inside submitFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	
		formObject.setNGValue("loan_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
		formObject.setNGValue("email_id", formObject.getUserName()+"@rlos.com");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		saveIndecisionGrid();
		//added for document function
		int itemIndex=formObject.getWFFolderId();
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);
		//IncomingDoc();
		RLOS.mLogger.info( "after calling incomingdoc function");
		//added for document function

		RLOS.mLogger.info( "Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));


		TypeOfProduct();//Loan type
		
		//++ below code added 10-10-2017 - to save Interest rate approval flag
		if(formObject.getNGValue("Application_Type").equalsIgnoreCase("Reschedulment"))
		{
			formObject.setNGValue("Interest_Rate_App_req", "Y");
			RLOS.mLogger.info("Interest_Rate_App_req"+formObject.getNGValue("Interest_Rate_App_req"));    
		}

	}


	public void submitFormCompleted(FormEvent pEvent)
			throws ValidatorException {
		RLOS.mLogger.info( "Inside submitFormCompleted()" + pEvent);

	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		RLOS.mLogger.info( "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		RLOS.mLogger.info("Inside initialize() method");
		RLOS.mLogger.info("Inside initialize()");
		RLOS.mLogger.info("Alert Message :: "+NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL001"));
		RLOS.mLogger.info("IF Condition Value :: "+NGFUserResourceMgr_RLOS.getResourceString_rlosIfConditions("IF001"));
			
		//throw new ValidatorException(new FacesMessage("Hello"));
	}
}

