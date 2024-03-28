

/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Cad Analyst1

File Name                                                         : CC_CAD_Analyst1

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/





package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;




import com.newgen.omniforms.util.SKLogger_CC;


import com.newgen.omniforms.component.IRepeater;
import javax.faces.application.*;

public class CC_CAD_Analyst_1 extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	boolean executeFlag = false;
	String queryData_load="";
	FormReference formObject = null;
	CC_Integration_Input Intgration_input = new CC_Integration_Input();
	CC_Integration_Output Intgration_Output = new CC_Integration_Output();
	public void formLoaded(FormEvent pEvent)
	{
		
		SKLogger_CC.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());

		makeSheetsInvisible(tabName, "9,11,12,13,14,15,16,17");
		formObject.setVisible("SmartCheck_Label2",true);
		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);
		if(formObject.getNGValue("cmplx_DEC_Decision").equalsIgnoreCase("Refer to Credit"))
		{
			formObject.setEnabled("cmplx_DEC_ReferTo",true);
		}//Arun (07/10)

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			
			new CC_CommonCode().setFormHeader(pEvent);
			//++ Below Code added By Yash on Oct 12, 2017  to fix : 34-"Office verification tab will not be visible as the case is for self employed customer " : Reported By Shashank on Oct 09, 2017++

			enable_cad1();
			//++ Above Code added By Yash on Oct 12, 2017  to fix : 34-"Office verification tab will not be visible as the case is for self employed customer " : Reported By Shashank on Oct 09, 2017++

		}catch(Exception e)
		{
			new CC_CommonCode();
			SKLogger_CC.writeLog("RLOS Initiation", "Exception:"+printException(e));
		}
	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String alert_msg="";
		String outputResponse = "";
		String SystemErrorCode="";

		SKLogger_CC.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			SKLogger_CC.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().FrameExpandEvent(pEvent);						
			break;

			/*
					if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
	        			hm.put("CustomerDetails","Clicked");
						popupFlag="N";
	    				formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
	    				loadPicklistCustomer();

	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}

	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")){
						SKLogger_CC.writeLog("Credit Card product","Inside CC_CommonCode-->FrameExpandEvent()");
						hm.put("ProductContainer","Clicked");
						popupFlag="N";
						formObject.fetchFragment("ProductContainer", "Product", "q_product");
						hide_sheet_employee();
						hide_sheet_company();
						SKLogger_CC.writeLog("Credit Card product grid","Inside CC_CommonCode-->FrameExpandEvent()");
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
						// added by yash on 6/8/2017 for disabling fields for IM Subproduct 
						int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						for(int i=0;i<n;i++){
							SKLogger_CC.writeLog("CC", "Grid Data[1][2] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2));
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Instant Money")){
								SKLogger_CC.writeLog("CC @AKSHAY", "Grid Data[1][2] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2));
								formObject.setLocked("Product_Frame1",true);


					}
							else {
								formObject.setLocked("Product_Frame1",false);
							}
						}
						// ended by yash on 6/8/2017

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			}
						if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {

	        			hm.put("EmploymentDetails","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");



	        			//loadPicklist_Employment();
	        			loadPicklist4();
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
						//added by yash on 21/8/2017
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
						hm.put("IncomeDEtails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
						int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						for(int i=0;i<n;i++){
							SKLogger_CC.writeLog("CC", "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6).equalsIgnoreCase("Salaried")){
								//SKLogger_CC.writeLog("CC @yash", "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));

								formObject.setVisible("IncomeDetails_Frame3", false);
								formObject.setHeight("Incomedetails", 730);
								formObject.setHeight("IncomeDetails_Frame1", 680);	



					}

							else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6).equalsIgnoreCase("Self Employed")){

							{
								formObject.setVisible("IncomeDetails_Frame2", false);
								formObject.setTop("IncomeDetails_Frame3",40);
								formObject.setHeight("Incomedetails", 300);
								formObject.setHeight("IncomeDetails_Frame1", 340);

							}
								//makeSheetsVisible(tabName, "3");
							}
						}

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}

					//ended by yash on 21/8/2017

					// added by yash on 17 may 2017
					//comapny details
					if (pEvent.getSource().getName().equalsIgnoreCase("CompDetails")) {
						hm.put("CompDetails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
						LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
		                LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		                LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		                LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		                LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
		                LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		                LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
		                //added by yash on 12/8/2017
		                formObject.setLocked("CompanyDetails_Button3",true);
		                formObject.setLocked("cif",true);

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Auth_Sign_Details")) {
						hm.put("Auth_Sign_Details","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
						//added by yash on 12/8/2017
						formObject.setLocked("AuthorisedSignDetails_Button4",true);
						formObject.setLocked("AuthorisedSignDetails_CIFNo",true);
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Partner_Details")) {
						hm.put("Partner_Details","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Partner_Details", "PartnerDetails", "q_PartnerDetails");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
						hm.put("Reference_Details","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Reference_Details", "ReferenceDetailVerification", "q_ReferenceDetails");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Card_Details")) {
						hm.put("Card_Details","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Card_Details", "CardDetails", "q_cardDetails");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Supplementary_Cont")) {
						hm.put("Supplementary_Cont","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Supplementary_Cont", "SupplementCardDetails", "q_SuppCardDetails");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Details")) {
						hm.put("Details","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")) {
						hm.put("Part_Match","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Part_Match", "PartMatch", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_Incidents")) {
						hm.put("Finacle_CRM_Incidents","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Finacle_CRM_Incidents", "FinacleCRMIncident", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_CustomerInformation")) {
						hm.put("Finacle_CRM_CustomerInformation","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Finacle_CRM_CustomerInformation", "FinacleCRMCustInfo", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
						hm.put("External_Blacklist","Clicked");
						popupFlag="N";
						formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")) {
						hm.put("Finacle_Core","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("MOL")) {
						hm.put("MOL","Clicked");
						popupFlag="N";
						formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("World_Check")) {
						hm.put("World_Check","Clicked");
						popupFlag="N";
						formObject.fetchFragment("World_Check", "WorldCheck1", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enquiry")) {
						hm.put("Reject_Enquiry","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Reject_Enquiry", "RejectEnq", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Salary_Enquiry")) {
						hm.put("Salary_Enquiry","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Salary_Enquiry", "SalaryEnq", "q_CC_Loan");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Frame4")) {
						hm.put("Frame4","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Frame4", "IncomingDocument", "q_IncDoc");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
						hm.put("PostDisbursal","Clicked");
						popupFlag="N";
						formObject.fetchFragment("PostDisbursal", "PostDisbursal", "q_PostDisbursal");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
						if (pEvent.getSource().getName().equalsIgnoreCase("Incomedetails")) 
						{
							hm.put("Incomedetails","Clicked");
							popupFlag="N";
							formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");

							int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
							for(int i=0;i<n;i++){
								SKLogger_CC.writeLog("CC", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
								if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
									SKLogger_CC.writeLog("CC @AKSHAY", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
									formObject.setVisible("IncomeDetails_Label12", true);
									formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);	
									formObject.setVisible("IncomeDetails_Label14", true);
									formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", true);	
								}	
								else{
									formObject.setVisible("IncomeDetails_Label12", false);
									formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);	
									formObject.setVisible("IncomeDetails_Label14", false);
									formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", false);			
								}
							}

							if(n>0)
							{					
								String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
								SKLogger_CC.writeLog("CC", "Emp Type Value is:"+EmpType);

								if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
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

								else if(EmpType.equalsIgnoreCase("Self Employed"))
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
										formObject.setVisible("IncomeDetails_Label36", false);
										formObject.setVisible("cmplx_IncomeDetails_TotalAvgOther",false);
										formObject.setVisible("IncomeDetails_Label15",false);
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
										formObject.setVisible("cmplx_IncomeDetails_TotalAvgOther",false);
										formObject.setVisible("IncomeDetails_Label15",false);
									}	
								}
							}

							//IMFields_Income();
							//BTCFields_Income();
							//LimitIncreaseFields_Income();
							//ProductUpgrade_Income();

							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}					
						}					
						if (pEvent.getSource().getName().equalsIgnoreCase("Internal_External_Liability")) {
		        			hm.put("Internal_External_Liability","Clicked");
							popupFlag="N";

		        			formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");

		        			try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
		        		}	
					if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
	        			hm.put("MiscFields","Clicked");
						popupFlag="N";

	        			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");

	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}

	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation")) {
	        			hm.put("EligibilityAndProductInformation","Clicked");
						popupFlag="N";

	        			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
	        			// added by yash on 6/8/2017 for disabling field
	        			Eligibilityfields();



						int m=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						for(int i=0;i<m;i++){
							SKLogger_CC.writeLog("CC", "Grid Data[1][2] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2));
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Instant Money")){
								SKLogger_CC.writeLog("CC @AKSHAY", "Grid Data[1][2] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2));
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTai",true);
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);

								formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);

					}
							else {
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTai",true);
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",false);

							}


	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}	
					}

					//ended by yash 


	        		if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
	        			hm.put("Address_Details_container","Clicked");
						popupFlag="N";

	        			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
	        			loadPicklist_Address();

	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}

	        		if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) {
	        			hm.put("Alt_Contact_container","Clicked");
						popupFlag="N";

	        			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");

	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}

	        		if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
					{
						hm.put("FATCA","Clicked");
						popupFlag="N";
						formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");

						loadPicklist_Fatca();


						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}

					}	

					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
					{
						hm.put("KYC","Clicked");
						popupFlag="N";
						formObject.fetchFragment("KYC", "KYC", "q_KYC");

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}

					}	

					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
					{
						hm.put("OECD","Clicked");
						popupFlag="N";
						formObject.fetchFragment("OECD", "OECD", "q_OECD");

						loadPicklist_oecd();

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}

					}



					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
	        			hm.put("DecisionHistory","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
	        			loadPicklist3();
	        			loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")) {
	        			hm.put("Part_Match","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Card_Details")) {
	        			hm.put("Card_Details","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Card_Details", "CardDetails", "q_cardDetails");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Supplementary_Cont")) {
	        			hm.put("Supplementary_Cont","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Supplementary_Cont", "SupplementCardDetails", "q_SuppCardDetails");

	        			loadPicklist_suppCard();

						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Partner_Details")) {
	        			hm.put("Partner_Details","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Partner_Details", "PartnerDetails", "q_PartnerDetails");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}

					if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")) {

	        			formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_FinacleCore");

	        			fetchfinacleTORepeater();
	        			fetchfinacleDocRepeater();
	        			//loadPicklist3();
	        			//loadInDecGrid();
	        			// code for lien and si details grid populate start


	        		}

					if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enquiry")) {
	        			hm.put("Reject_Enquiry","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Reject_Enquiry", "RejectEnq", "q_RejectEnq");

	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}

					if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_Incidents")) {
	        			hm.put("Finacle_CRM_Incidents","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Finacle_CRM_Incidents", "FinacleCRMIncident", "q_FinacleCRMIncident");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_CustomerInformation")) {
	        			hm.put("Finacle_CRM_CustomerInformation","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Finacle_CRM_CustomerInformation", "FinacleCRMCustInfo", "q_FinacleCRMCustInfo");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}

					if (pEvent.getSource().getName().equalsIgnoreCase("World_Check")) {
	        			hm.put("World_Check","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("MOL")) {
	        			hm.put("MOL","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("MOL", "MOL1", "q_Mol1");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Details")) {
	        			hm.put("Details","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}


					if (pEvent.getSource().getName().equalsIgnoreCase("Salary_Enquiry")) {
	        			hm.put("Salary_Enquiry","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Salary_Enquiry", "SalaryEnq", "q_SalaryEnq");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("CompDetails")) {
	        			hm.put("CompDetails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");

						int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						for(int i=0;i<n;i++){
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
								formObject.setVisible("CompanyDetails_Label8", true);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", true);//Effective length of buss
								break;
							}

							else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
								formObject.setVisible("CompanyDetails_Label27", true);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", true);//Emirate of work
								formObject.setVisible("CompanyDetails_Label29", true);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", true);
								formObject.setVisible("CompanyDetails_Label28", true);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_VisaSponsor", true);
								break;
							}
							else{
								formObject.setVisible("CompanyDetails_Label27", false);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", false);//Emirate of work
								formObject.setVisible("CompanyDetails_Label29", false);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", false);
								formObject.setVisible("CompanyDetails_Label28", false);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_VisaSponsor", false);
								formObject.setVisible("CompanyDetails_Label8", false);
								formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", false);
							}

						}


	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Auth_Sign_Details")) {
	        			hm.put("Auth_Sign_Details","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					 if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
		        			hm.put("Reference_Details","Clicked");
							popupFlag="N";
		        			formObject.fetchFragment("Reference_Details", "Reference_Details", "q_ReferenceDetails");
		        			//loadPicklist3();
		        			//loadInDecGrid();
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
		        		}
					 if (pEvent.getSource().getName().equalsIgnoreCase("Frame4"))
	                    {
	                        hm.put("IncomingDocuments","Clicked");
	                        popupFlag="N";
	                        SKLogger_CC.writeLog("RLOS Initiation Inside ","IncomingDocuments");
	                        //frame,fragment,q_variable
	                        formObject.fetchFragment("Frame4", "IncomingDocument", "q_IncDoc");
	                        SKLogger_CC.writeLog("RLOS Initiation Inside ","fetchIncomingDocRepeater");

	                        fetchIncomingDocRepeater();
	                        SKLogger_CC.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1");
	                        try{

	                            throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
	                        }
	                        finally{
	                            hm.clear();
	                        }
	                    }
					 //ended by yash on 7/8/2017
					 if (pEvent.getSource().getName().equalsIgnoreCase("CAD")) {
		        			hm.put("CAD","Clicked");
							popupFlag="N";
		        			formObject.fetchFragment("CAD", "CAD", "");
		        			//loadPicklist3();
		        			//loadInDecGrid();
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
		        		}
					 if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
		        			hm.put("CAD_Decision","Clicked");
							popupFlag="N";
		        			formObject.fetchFragment("CAD_Decision", "CAD_Decision", "q_CadDecision");
		        			//loadPicklist3();
		        			//loadInDecGrid();
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
		        		}
						if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
							hm.put("PostDisbursal","Clicked");
							popupFlag="N";
							formObject.fetchFragment("PostDisbursal", "PostDisbursal", "q_PostDisbursal");

							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CAD")) {
							hm.put("CAD","Clicked");
							popupFlag="N";
							formObject.fetchFragment("CAD", "NotepadDetails", "q_NotepadDetails");

							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
							SKLogger_CC.writeLog("CC CAD Analyst1", "Inside CAD_Decision frameexpand event");
							hm.put("CAD_Decision","Clicked");
							popupFlag="N";
							formObject.fetchFragment("CAD_Decision", "CAD_Decision", "q_CadDecision");
							loadPicklist3();
		        			loadInDecGrid();
							SKLogger_CC.writeLog("CC CAD Analyst1", "Inside CAD_Decision frameexpand event end");
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}

							finally{hm.clear();
							}
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("Smart_Check1")) {
		        			hm.put("Smart_Check1","Clicked");
							popupFlag="N";
		        			formObject.fetchFragment("Smart_Check1", "SmartCheck1", "q_SmartCheckFCU");
		        			//loadPicklist3();
		        			//loadInDecGrid();
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
		        		}
						if (pEvent.getSource().getName().equalsIgnoreCase("CreditCard_Enq")) {
							hm.put("CreditCard_Enq","Clicked");
							popupFlag="N";

							formObject.fetchFragment("CreditCard_Enq", "CreditCardEnq", "q_CCenq");
							fetchoriginalDocRepeater();
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("Case_hist")) {
							hm.put("Case_hist","Clicked");
							popupFlag="N";

							formObject.fetchFragment("Case_hist", "CaseHistoryReport", "q_CaseHist");
							fetchoriginalDocRepeater();
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
							hm.put("LOS","Clicked");
							popupFlag="N";

							formObject.fetchFragment("LOS", "LOS", "q_LOS");
							fetchoriginalDocRepeater();
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
						}

						//added by yash
						if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Values")) {
							hm.put("Notepad_Values","Clicked");
							popupFlag="N";
							formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_NotepadDetails");
							LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");


							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
						}
						//ended 6th sept

					  break;
			 */					  

		case FRAGMENT_LOADED:
			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

				}*/
			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName: before CPV tab validations");
			if (pEvent.getSource().getName().equalsIgnoreCase("Business_Verification")) {
				hm.put("Business_Verification","Clicked");
				formObject.fetchFragment("Business_Verification", "BussinessVerification", "q_BussVerification");
				LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				formObject.setLocked("BussinessVerification_Frame1", true);
				//++ Below Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment " : Reported By Shashank on Oct 09, 2017++

				enable_busiVerification();
				//++ Above Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment" : Reported By Shashank on Oct 09, 2017++

				
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountry_Verification")) {
				hm.put("HomeCountry_Verification","Clicked");
				formObject.fetchFragment("HomeCountry_Verification", "HomeCountryVerification", "q_HCountryVerification");
				LoadPickList("cmplx_HCountryVerification_Hcountrytelverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				formObject.setLocked("HomeCountryVerification_Frame1", true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {
				hm.put("ResidenceVerification","Clicked");
				formObject.fetchFragment("ResidenceVerification", "ResidenceVerification", "q_ResiVerification");
				LoadPickList("cmplx_ResiVerification_Telnoverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				formObject.setLocked("ResidenceVerification_Frame1", true);	
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Detail_Verification")) {
				hm.put("Reference_Detail_Verification","Clicked");
				formObject.fetchFragment("Reference_Detail_Verification", "ReferenceDetailVerification", "q_RefDetailVerification");
				LoadPickList("cmplx_RefDetVerification_ref1cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				LoadPickList("cmplx_RefDetVerification_ref2cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");

			}

			if (pEvent.getSource().getName().equalsIgnoreCase("LoanCard_Details_Check")) {
				hm.put("LoanCard_Details_Check","Clicked");
				formObject.fetchFragment("LoanCard_Details_Check", "LoanandCard", "q_LoanandCard");
				loadPicklist_loancardverification();
				formObject.setLocked("LoanandCard_Frame1", true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Details")) {
				hm.put("Notepad_Details","Clicked");
				formObject.fetchFragment("Notepad_Details", "NotepadDetails", "q_NotepadDetails");

				formObject.setVisible("NotepadDetails_Frame1", false);
				/*String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
				formObject.setNGValue("NotepadDetails_notecode",notepad_desc);
			*/}
			if (pEvent.getSource().getName().equalsIgnoreCase("Smart_Check")) {
				hm.put("Smart_Check","Clicked");
				formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");
				formObject.setLocked("SmartCheck_Frame1", true);

			}
			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName: after CPV tab validations");
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
				formObject.setLocked("Customer_Frame1",true);
				loadPicklistCustomer();
				/*formObject.setLocked("Customer_FetchDetails",true);
							formObject.setLocked("Customer_save",true);*/

				//setDisabled();
			}	

			//added by yash fro CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
				formObject.setLocked("Product_Frame1",true);
				int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if(prd_count>0){
					String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
				}
				//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
				/*
							LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
							LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				 */
				/*formObject.setLocked("Product_Frame1",true);
							formObject.setLocked("Add",true);
							formObject.setLocked("Modify",true);
							formObject.setLocked("Delete",true);
							formObject.setLocked("Product_Save",true);
							formObject.setLocked("cmplx_Product_cmplx_ProductGrid_table",true);
							formObject.setLocked("Customer_save",true);
							formObject.setLocked("Customer_save",true);*/


			}
			//added by yash for CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
				//formObject.setLocked("IncomeDetails_Frame1",true);
				LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
				formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			//	formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_Flying_Avg",true);
				formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);
				formObject.setVisible("IncomeDetails_ListView1",true);
				formObject.setVisible("IncomeDetails_Button1",true);
				formObject.setVisible("IncomeDetails_Button2",true);
				formObject.setVisible("IncomeDetails_Button3",true);
				String EmpType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
				if(EmpType.equalsIgnoreCase("Self Employed") )
				{
					formObject.setVisible("cmplx_IncomeDetails_TotalAvgOther",false);
					formObject.setVisible("IncomeDetails_Label15",false);
				}

			}
			// ended by yash for CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
				
				loadPicklist_CompanyDet();
				//loadPicklist_Address();
				//formObject.setLocked("CompanyDetails_Frame1",true);
				formObject.setVisible("CompanyDetails_CheckBox1",true);
				formObject.setVisible("CompanyDetails_Label17",true);
				formObject.setVisible("CompanyDetails_highdelin",true);
				formObject.setVisible("CompanyDetails_Text1",true);
				formObject.setVisible("CompanyDetails_Label14",true);
				formObject.setVisible("CompanyDetails_currempcateg",true);
				formObject.setVisible("CompanyDetails_Text2",true);							
				formObject.setVisible("CompanyDetails_Label16",true);
				formObject.setVisible("CompanyDetails_categcards",true);
				formObject.setVisible("CompanyDetails_Text3",true);
				formObject.setVisible("CompanyDetails_Label12",true);
				formObject.setVisible("CompanyDetails_categexpat",true);
				formObject.setVisible("CompanyDetails_Text4",true);
				formObject.setVisible("CompanyDetails_Label15",true);
				formObject.setVisible("CompanyDetails_categnational",true);
				formObject.setVisible("CompanyDetails_Text5",true);							
				formObject.setLocked("CompanyDetails_Text1",false);
				formObject.setLocked("CompanyDetails_Text2",true);
				formObject.setLocked("CompanyDetails_Text3",true);
				formObject.setLocked("CompanyDetails_Text4",true);
				formObject.setLocked("CompanyDetails_Text5",true);
				formObject.setLocked("estbDate",false);
				formObject.setEnabled("CompanyDetails_DatePicker1",true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
				//loadPicklist_Address();
				//commented by Akshay on 7/9/2017 for firing this call on CAD
				//formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
				formObject.setLocked("AuthorisedSignDetails_CIFNo", true);
				formObject.setLocked("AuthorisedSignDetails_Button4", true);
				LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
				LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				
			}
			//added by yash for CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
				LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
				formObject.setEnabled("PartnerDetails_Dob",true);
				//loadPicklist_Address();
				//formObject.setLocked("PartnerDetails_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
				
				//loadPicklist_Address();
				//formObject.setLocked("ExtLiability_Frame1",true);
				formObject.setVisible("Liability_New_Label1",true);
				formObject.setVisible("Liability_New_MOB",true);
				formObject.setVisible("Liability_New_Label2",true);
				formObject.setVisible("Liability_New_Utilization",true);
				formObject.setVisible("Liability_New_Label3",true);
				formObject.setVisible("Liability_New_Outstanding",true);
				formObject.setVisible("Liability_New_Delinkinlast3months",true);
				formObject.setVisible("Liability_New_DPDinlast18months",true);
				formObject.setVisible("Liability_New_DPDinlast6",true);
				formObject.setVisible("Liability_New_Label4",true);
				formObject.setVisible("Liability_New_writeOfAmount",true);
				formObject.setVisible("Liability_New_Label5",true);
				formObject.setVisible("Liability_New_worstStatusInLast24",true);
				LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");
						
				formObject.setVisible("ExtLiability_Label15",true);
				formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
				formObject.setVisible("Liability_New_Label6",true);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
				formObject.setVisible("Liability_New_Label8",true);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
				formObject.setVisible("Liability_New_Label7",true);
				formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
				/*formObject.setVisible("cmplx_Liability_New_DBR",false);
							formObject.setVisible("ExtLiability_Label9",false);
							formObject.setVisible("cmplx_Liability_New_TAI",false);
							formObject.setVisible("ExtLiability_Label25",false);
							formObject.setVisible("cmplx_Liability_New_DBRNet",false);
							formObject.setVisible("ExtLiability_Label14",false);*/

			}
			//added by yash for CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
				formObject.setLocked("Compliance_Frame1",true);

			}
			//ended by yash for CC FSD

			if (pEvent.getSource().getName().equalsIgnoreCase("Liability Addition")) {
				//loadPicklist_Address();
				//formObject.setLocked("ExtLiability_Frame4",true);		
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
				//formObject.setLocked("EMploymentDetails_Frame1", true);

				//loadPicklist4();
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
				//added 6th sept for proc 1926
				if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
				{
					formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", false);//total final limit
					formObject.setLocked("ELigibiltyAndProductInfo_Button1", false);//calculate re-eligibility
				}
				//ended 6th sept for proc 1926
				//loadPicklist_Address();
				//formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				/*formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label3",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTai",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label4",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label6",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label7",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label11",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label13",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label12",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label14",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate",false);
							formObject.setVisible("ELigibiltyAndProductInfo_Label23",false);
							formObject.setLeft("ELigibiltyAndProductInfo_Label5",16);
							formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit",16);
							formObject.setTop("ELigibiltyAndProductInfo_Save",1000);*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("AddressDetails_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("AltContactDetails_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {
				//loadPicklist_Address();
				formObject.setLocked("ReferenceDetailVerification_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CardCollection")) {
				//loadPicklist_Address();
				formObject.setLocked("CardDetails_Frame1",true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
				//loadPicklist_Address();
				formObject.setLocked("FATCA_Frame6",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
				//loadPicklist_Address();
				formObject.setLocked("CardDetails_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
				//loadPicklist_Address();
				formObject.setLocked("KYC_Frame1",true);

			}
			//added by yash for CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
				formObject.setLocked("Reference_Details_ReferenceRelationship",true);
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
				//loadPicklist_Address();
				formObject.setLocked("OECD_Frame8",true);

			}

			if (pEvent.getSource().getName().equalsIgnoreCase("Details")) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
				//loadPicklist_Address();
				formObject.setLocked("PartMatch_Frame1",true);
				//change by saurabh for JIRA - 2592
				LoadPickList("PartMatch_nationality","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock) order by code"); //Arun (10/10)
				//change by saurabh for JIRA - 2592 end.
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMIncident_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
				//loadPicklist_Address();
				formObject.setLocked("ExternalBlackList_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
				LoadPickList("FinacleCore_ChequeType", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_AccountType with (nolock)");
				//loadPicklist_Address();
				//formObject.setLocked("FinacleCore_Frame1",true);

			}

			if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
				//loadPicklist_Address();
				formObject.setLocked("MOL1_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
				//loadPicklist_Address();
				formObject.setLocked("WorldCheck1_Frame1",true);

			}

			if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {
				//formObject.setLocked("SmartCheck_Frame1",true);
				formObject.setVisible("SmartCheck_Label2",true);
				formObject.setVisible("SmartCheck_CPVRemarks",true);
				formObject.setVisible("SmartCheck_Label4",false);
				formObject.setVisible("SmartCheck_FCURemarks",false);
				
				formObject.setLocked("SmartCheck_CPVRemarks",true);
				//++ Above Code added By Yash on Oct 12, 2017  to fix : 33-"CPV remarks should be disabled" : Reported By Shashank on Oct 09, 2017++

				//formObject.setLocked("SmartCheck1_Modify",true);


			}
			if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) {
				//formObject.setLocked("SmartCheck_Frame1",true);
				formObject.setVisible("SmartCheck1_Label2",false);
				formObject.setVisible("SmartCheck1_CPVRemarks",false);
				formObject.setVisible("SmartCheck1_Label4",false);
				formObject.setVisible("SmartCheck1_FCURemarks",false);

				//formObject.setLocked("SmartCheck1_Modify",true);


			}
			if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
				//loadPicklist_Address();
				//formObject.setLocked("RejectEnq_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
				//loadPicklist_Address();
				//formObject.setLocked("SalaryEnq_Frame1",true);

			}
			/*if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
							//loadPicklist_Address();
							formObject.setLocked("ExternalBlackList_Frame1",true);

						}*/
			/*if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
							//loadPicklist_Address();
							formObject.setLocked("IncomingDocument_Frame",true);

						}*/
			if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
				//loadPicklist_Address();
				formObject.setLocked("PostDisbursal_Frame2",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")) {
				//loadPicklist_Address();
				formObject.setLocked("CC_Loan_Frame1",true);

			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Fraud Control Unit")) {
				LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
			}

			//added by abhishek for CC FSD
			if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
				notepad_withoutTelLog();

				formObject.setTop("NotepadDetails_savebutton",410);
				notepad_load();
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer_Details_Verification")) {
				formObject.setLocked("CustDetailVerification_Frame1", true);
			}
			//ended by yash

			if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
				//added by Akshay on 5/9/17
				Decision_cadanalyst1();
				formObject.setEnabled("cmplx_DEC_Manual_Deviation", true);
			}
				//loadPicklist1();
				break;
			

		case MOUSE_CLICKED:
			SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
				//GenerateXML();
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_Product_cmplx_ProductGrid")){
				String ReqProd=formObject.getNGValue("ReqProd");
				SKLogger_CC.writeLog("CC CSM grid row click", "Value of ReqProd is:"+ReqProd);
				loadPicklistProduct(ReqProd);
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
				formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("BT_Add")){
				formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC",formObject.getNGValue("CC_Loan_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
				SKLogger_CC.writeLog("CC", "Inside add button33: add of btc details");

			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("BT_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
				SKLogger_CC.writeLog("CC", "Inside add button33: modify of btc details");

			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("BT_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
				SKLogger_CC.writeLog("CC", "Inside add button33: delete of btc details");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck_Add")){
				formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck_SmartCheckGrid");
				SKLogger_CC.writeLog("CC", "Inside add button33: add of SmartCheck_Add details");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck_Modify")){
				formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck_SmartCheckGrid");
				SKLogger_CC.writeLog("CC", "Inside add button33: modify of SmartCheck_Modify details");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck_SmartCheckGrid");
				SKLogger_CC.writeLog("CC", "Inside add button33: delete of SmartCheck_Delete details");

			}
			if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck_Button1")){
				formObject.saveFragment("Smart_Check");
				alert_msg="Smart Check Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
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
			else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Add"))	
		 	{
			formObject.setNGValue("Inc_winame",formObject.getWFWorkitemName());
			SKLogger_CC.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("IncomeDetails_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
			}
		else 	if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
			}
		else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Delete"))
			{
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
				SKLogger_CC.writeLog("PL_Initiation", "Inside Customer_save button: ");
				formObject.saveFragment("CustomerDetails");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
				formObject.saveFragment("ProductContainer");
			}
			//added by yash
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_Button1")){
				formObject.saveFragment("FinacleCRMCustInfo_Frame1");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
				formObject.saveFragment("GuarantorDetails");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
				formObject.saveFragment("IncomeDetails");
				alert_msg="Income Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
				formObject.saveFragment("IncomeDetails");
				alert_msg="Income Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
				formObject.saveFragment("CompanyDetails");
				alert_msg="Company Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));

			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Save")){
				formObject.saveFragment("PartnerDetails");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button1")){
				formObject.saveFragment("AuthorisedSignDetails_ShareHolding");
				alert_msg="Authorised Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
				formObject.saveFragment("Liability_container");
			}
			//added by yash on 21/8/2017
			else if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Save")){
				formObject.saveFragment("Supplementary_Container");
				alert_msg="Supplementary Card Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button4")){
				formObject.saveFragment("World_Check");
				alert_msg="World Check Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//added by yash on 21/8/2017
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
			else if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_add"))
			{
				formObject.setNGValue("PartnerDetails_partner_winame",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("PartnerDetails_partner_winame"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_delete"))
			{

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_add"))
			{
				formObject.setNGValue("AuthorisedSignDetails_AuthorisedSign_wiName",formObject.getWFWorkitemName());
				SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("AuthorisedSignDetails_AuthorisedSign_wiName"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_modify"))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_delete"))
			{

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			}

			//added by Akshay on 7/9/2017 for Fetch Company Details call on CAD

			else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
				SKLogger_CC.writeLog("CC value of ReturnCode","CompanyDetails_Button3");
				outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
				String ReturnCode = "";
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("CC value of ReturnCode",ReturnCode);
			
				
			
				String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
				SKLogger_CC.writeLog("CC value of BusinessIncDate",BusinessIncDate);
				
				if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
					formObject.setNGValue("Is_Customer_Details","Y");

					SKLogger_CC.writeLog("CC value of EID_Genuine","value of company Details corporation"+formObject.getNGValue("Is_Customer_Details"));

					Intgration_Output.valueSetCustomer(outputResponse,"Corporation_CIF");  
					
					try{

						//String Date1=BusinessIncDate;
						//Comman method written to convert date.
						formObject.setNGValue("estbDate",formatDateFromOnetoAnother(BusinessIncDate,"yyyy-mm-dd","dd/mm/yyyy"),false);
						formObject.setNGValue("CompanyDetails_DatePicker1",formatDateFromOnetoAnother(formObject.getNGValue("CompanyDetails_DatePicker1"),"yyyy-mm-dd","dd/mm/yyyy"),false);
						formObject.setNGValue("TLExpiry",formatDateFromOnetoAnother(formObject.getNGValue("TLExpiry"),"yyyy-mm-dd","dd/mm/yyyy"),false);
						//SKLogger_CC.writeLog("CC value of Date1111",Date1);
						// ++ below code commented at offshore - 06-10-2017 date format different
						//SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
						/*SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
						String Datechanged=sdf2.format(sdf1.parse(Date1));
						SKLogger_CC.writeLog("CC value ofDatechanged",Datechanged);
						formObject.setNGValue("estbDate",Datechanged,false);*/

						/*Date1 = formObject.getNGValue("CompanyDetails_DatePicker1");
						Datechanged=sdf2.format(sdf1.parse(Date1));
						SKLogger_CC.writeLog("CC value ofDatechanged",Datechanged);
						formObject.setNGValue("CompanyDetails_DatePicker1",Datechanged,false);*/

						/*Date1 = formObject.getNGValue("TLExpiry");
						Datechanged=sdf2.format(sdf1.parse(Date1));
						SKLogger_CC.writeLog("CC value ofDatechanged",Datechanged);
						formObject.setNGValue("TLExpiry",Datechanged,false);*/
						String lob = getYearsDifference(formObject,"CompanyDetails_DatePicker1");
						formObject.setNGValue("lob", lob);
						String corpName = formObject.getNGValue("compName");
						if(corpName!=null && !corpName.equalsIgnoreCase("") && !corpName.equalsIgnoreCase(" ")){
							getDataFromALOC(formObject,corpName);
						}
					}
					catch(Exception ex){
						SKLogger_CC.writeLog("CC value of Customer Details",printException(ex));
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

			//ended by Akshay on 7/9/2017 for Fetch Company Details call on CAD

			//added by Akshay on 7/9/2017 for Fetch Authorised Details call on CAD
			else if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button4")){

				outputResponse = Intgration_input.GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
				String ReturnCode = "";
				String ReturnDesc = "";
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SKLogger_CC.writeLog("CC value of ReturnCode",ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				SKLogger_CC.writeLog("CC value of ReturnDesc",ReturnDesc);
				if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
					formObject.setNGValue("Is_Customer_Details","Y");
					SKLogger_CC.writeLog("CC value of EID_Genuine","value of Authorised_CIF"+formObject.getNGValue("Is_Customer_Details"));
					Intgration_Output.valueSetCustomer(outputResponse,"Signatory_CIF");    
					SKLogger_CC.writeLog("CC value of Customer Details","Authorised_CIF is generated");
					SKLogger_CC.writeLog("CC value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
					//Comman method written to convert date.
					formObject.setNGValue("AuthorisedSignDetails_DOB",formatDateFromOnetoAnother(formObject.getNGValue("AuthorisedSignDetails_DOB"),"yyyy-mm-dd","dd/mm/yyyy"),false);
					formObject.setNGValue("AuthorisedSignDetails_PassportExpiryDate",formatDateFromOnetoAnother(formObject.getNGValue("AuthorisedSignDetails_PassportExpiryDate"),"yyyy-mm-dd","dd/mm/yyyy"),false);
					formObject.setNGValue("AuthorisedSignDetails_VisaExpiryDate",formatDateFromOnetoAnother(formObject.getNGValue("AuthorisedSignDetails_VisaExpiryDate"),"yyyy-mm-dd","dd/mm/yyyy"),false);
					/*String str_dob=formObject.getNGValue("AuthorisedSignDetails_DOB");

					String str_passExpDate=formObject.getNGValue("AuthorisedSignDetails_PassportExpiryDate");
					String str_visaExpDate=formObject.getNGValue("AuthorisedSignDetails_VisaExpiryDate");
					try{
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
						if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
							String n_str_dob=sdf2.format(sdf1.parse(str_dob));
							SKLogger_CC.writeLog("converting date entered",n_str_dob+"asd");
							formObject.setNGValue("AuthorisedSignDetails_DOB",n_str_dob,false);
						}
						if(str_passExpDate!=null&&!str_passExpDate.equalsIgnoreCase("")){
							formObject.setNGValue("AuthorisedSignDetails_PassportExpiryDate",sdf2.format(sdf1.parse(str_passExpDate)),false);
						}
						if(str_visaExpDate!=null&&!str_visaExpDate.equalsIgnoreCase("")){
							formObject.setNGValue("AuthorisedSignDetails_VisaExpiryDate",sdf2.format(sdf1.parse(str_visaExpDate)),false);
						}
					}catch(Exception ex){
						SKLogger_CC.writeLog("Exception in converting date entered",printException(ex));
					}*/
				}
				else{
					SKLogger_CC.writeLog("Customer Details","Customer Details is not generated");
					formObject.setNGValue("Is_Customer_Details","N");
				}
				SKLogger_CC.writeLog("CC value of Authorised_CIF",formObject.getNGValue("Is_Customer_Details"));
			}

			//ended by Akshay on 7/9/2017 for Fetch Authorised Details call on CAD


			else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
			{		formObject.setNGValue("DecCallFired","Eligibility");
			String Emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
			if (Emp_type.equalsIgnoreCase("Salaried")){
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
				emp_details();
			}
			else if (Emp_type.equalsIgnoreCase("Self Employed")){
				formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				formObject.setTop("Auth_Sign_Details", formObject.getTop("CompDetails")+formObject.getHeight("CompDetails")+25);
				
				
				formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
				formObject.setTop("Partner_Details", formObject.getTop("Auth_Sign_Details")+formObject.getHeight("Auth_Sign_Details")+25);
    			
			}
			SKLogger_CC.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			income();
			formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");
			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
			formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
			formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
			formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
			formObject.setTop("CreditCard_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+55);
			formObject.setTop("Case_hist", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
			formObject.setTop("LOS", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+95);
			
			formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");
			outputResponse = Intgration_input.GenerateXML("DECTECH","");
			//SKLogger.writeLog("$$After Generatexml for CallFired : "+CallFired,"");
			SKLogger_CC.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");
			try{
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
				
				String EMI=getEMI(LoanAmount,RateofInt,tenor);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
				
			}
			catch(Exception e){
				SKLogger_CC.writeLog("Exception Occured in RLOS Iniitation : "," Exception in EMI Generation");
			}
			//EMI Calcuation logic added below 24-Sept-2017 End
			

			SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
			SKLogger_CC.writeLog("RLOS value of ReturnCode",SystemErrorCode);
			if(SystemErrorCode.equalsIgnoreCase("")){
				Intgration_Output.valueSetCustomer(outputResponse,"");   
				alert_msg="Decision engine integration successful";
				SKLogger_CC.writeLog("after value set customer for dectech call","");
				//formObject.setNGFrameState("EligibilityAndProductInformation", 1);
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));


			}
			else{
				alert_msg="Critical error occurred Please contact administrator";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse,"");


			//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse,"");

			}

			//added By Akshay on 7/9/2017 for Dectech call on CAD decision	
			else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_calReElig")){	
				formObject.setNGValue("DecCallFired","Decision");
				SKLogger_CC.writeLog("$$Before Generatexml for dectech call..outputResponse is : "+outputResponse,"");
				String Emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
				if (Emp_type.equalsIgnoreCase("Salaried")){
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
					emp_details();
				}
				else if (Emp_type.equalsIgnoreCase("Self Employed")){
					formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
					formObject.setTop("Auth_Sign_Details", formObject.getTop("CompDetails")+formObject.getHeight("CompDetails")+25);
					
					
					formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
					formObject.setTop("Partner_Details", formObject.getTop("Auth_Sign_Details")+formObject.getHeight("Auth_Sign_Details")+25);
	    			
				}
				formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				income();
				formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");
				formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
				formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
				formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
				formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
				formObject.setTop("CreditCard_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+55);
				formObject.setTop("Case_hist", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
				formObject.setTop("LOS", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+95);
				outputResponse = Intgration_input.GenerateXML("DECTECH","");
				SKLogger_CC.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");

				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
				SKLogger_CC.writeLog("RLOS value of ReturnCode",SystemErrorCode);
				if(SystemErrorCode.equalsIgnoreCase("")){
					Intgration_Output.valueSetCustomer(outputResponse,"");   
					alert_msg="Decision engine integration successful";
					SKLogger_CC.writeLog("after value set customer for dectech call","");
					formObject.RaiseEvent("WFSave");
					throw new ValidatorException(new FacesMessage(alert_msg));				
				}
				else{
					alert_msg="Critical error occurred Please contact administrator";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}	
			//ended By Akshay on 7/9/2017 for Dectech call on CAD decision	

			else if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
				formObject.saveFragment("InternalExternalContainer");
			}
			//++ Below Code added By Yash on Oct 9, 2017  to fix : "to save liability" : Reported By Shashank on Oct 09, 2017++
			else if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Save")){
				formObject.saveFragment("Internal_External_Liability");
				alert_msg="External Liabilities Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//++ Above Code added By Yash on Oct 9, 2017  to fix : "to save liability" : Reported By Shashank on Oct 09, 2017++
			else if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
				formObject.saveFragment("EmploymentDetails");
			}
			//++ Below Code added By Yash on Oct 10, 2017  to fix : 10-"There should be only 1 save button below Partner details fragment.Similar to how it is at initiation" : Reported By Shashank on Oct 09, 2017++

			else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
				formObject.saveFragment("CompDetails");
				alert_msg="Company Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//++ Above Code added By Yash on Oct 10, 2017  to fix : 10-"There should be only 1 save button below Partner details fragment.Similar to how it is at initiation" : Reported By Shashank on Oct 09, 2017++
			
			else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
				formObject.saveFragment("EligibilityAndProductInformation");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
				formObject.saveFragment("MiscFields");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
				formObject.saveFragment("Address_Details_container");
			}
			// ++ below code not commented at offshore - 06-10-2017 name change of button
			else if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
				formObject.saveFragment("Alt_Contact_container");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
				formObject.saveFragment("Supplementary_Container");
			}


			else if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
				formObject.saveFragment("FATCA");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
				formObject.saveFragment("KYC");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
				formObject.saveFragment("OECD");
			}

			//added by yash on 8/8/2017
			else if(pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq_Button1")){
				formObject.saveFragment("SalaryEnq_Frame1");
				alert_msg="Salary Inquiry Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_Button1")){
				formObject.saveFragment("FinacleCRMCustInfo_Frame1");
				alert_msg="FinacleCRMCustInfo Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("RejectEnq_Save")){
				formObject.saveFragment("RejectEnq_Frame1");
				alert_msg="Reject Enquiry Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList_Save")){
				formObject.saveFragment("ExternalBlackList_Frame1");
				alert_msg="ExternalBlackList Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//ended by yash on 8/8/2017


			//added by yash on 21/8/2017
			else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
				formObject.saveFragment("DecisionHistory");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Save")){
				formObject.saveFragment("Smart_Check");
			}

			if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
				formObject.saveFragment("Notepad_Values");
				alert_msg="Notepad Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//below one is in use
			else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
				formObject.saveFragment("Notepad_Values");
				alert_msg="Notepad Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_save")){
				formObject.saveFragment("DecisionHistory");
				alert_msg="Decision History Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button1")){
				//++Below code added by  nikhil 10/10/17
				formObject.setNGValue("FinacleCore_DDSWiname",formObject.getWFWorkitemName());
				//-- Above code added by  nikhil 10/10/17

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_DDSgrid");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button2")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_DDSgrid");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button3")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_DDSgrid");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Add")){
				//++Below code added by  nikhil 10/10/17
				formObject.setNGValue("SmartCheck1_WiName",formObject.getWFWorkitemName());
				//-- Above code added by  nikhil 10/10/17

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Add")){
				//++Below code added by  nikhil 10/10/17
				formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
				//-- Above code added by  nikhil 10/10/17

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
				//++Below code added by  nikhil 10/10/17
				formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
				//-- Above code added by  nikhil 10/10/17

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_add();
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_modify();
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_delete();
			}
			// added by abhishek as per CC FSD
			else if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){

				Notepad_grid();
			} 
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button4")){
				SKLogger_CC.writeLog("cc_yash", "Inside Customer_finacle_add button: ");
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_inwardtt");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button5")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_inwardtt");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button6")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_inwardtt");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button7")){
				formObject.ExecuteExternalCommand("NGClearRow", "cmplx_FinacleCore_inwardtt");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");


			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			}

			// added by abhishek for repeater start
			else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTotal")){
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

			else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTurnover")){
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
			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button8")){
				 // ++ below code not commented at offshore - 06-10-2017
				//added by nikhil for repeater of avg nbc last 1 year
							formObject.clear("cmplx_FinacleCore_total_avg_last13");
							formObject.clear("cmplx_FinacleCore_total_avg_last_16");
							formObject.clear("cmplx_FinacleCore_toal_accounts_last1");
							//ended by nikhil
				try{
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_JAN2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_FEB2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_MAR2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_APR2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_MAY2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_JUN2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_JUL2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_AUG2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_SEP2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_OCT2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_NOV2");
					CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_DEC2");
					CalculateAvgTotalthree();
					//CalculateAvgTotalsix();
				} 
				catch (Exception e) {
					SKLogger_CC.writeLog("test ", e.toString());
				}
			}


			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToAverage")){
				IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame10");
				if(!repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN").equals("")){
					addToAvgRepeater(repObj);
				}
				else{
					alert_msg="Please calculate and then click on add to average button";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToTurnover")){
				IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame11");
				if(!repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1").equals("")){
					addToTrnoverGrid(repObj);
				}
				else{
					alert_msg="Please calculate and then click on add to Turnover button";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button9")){
				int n = formObject.getLVWRowCount("cmplx_FinacleCore_credturn_grid");
				if(formObject.getNGValue("FinacleCore_Consider_obli").equalsIgnoreCase("true")){
					if(formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")>0 || formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")==0){
						formObject.setNGValue("cmplx_FinacleCore_credturn_grid",formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"),4,"true");
						for(int i = 0 ; i<n ; i++){
							if(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", i, 0).equals(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"), 0))){
								formObject.setNGValue("cmplx_FinacleCore_credturn_grid",i,4,"true");
							}
						}
					}
					else{
						alert_msg="Please select one row and then Modify";

						throw new ValidatorException(new FacesMessage(alert_msg));
					}


				}
				else{
					/*alert_msg="Please check consider for obligation and then Modify";

								throw new ValidatorException(new FacesMessage(alert_msg));*/

					if(formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")>0 || formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")==0){
						formObject.setNGValue("cmplx_FinacleCore_credturn_grid",formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"),4,"false");
						for(int i = 0 ; i<n ; i++){
							if(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", i, 0).equals(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"), 0))){
								formObject.setNGValue("cmplx_FinacleCore_credturn_grid",i,4,"false");
							}
						}
					}
					else{
						alert_msg="Please select one row and then Modify";

						throw new ValidatorException(new FacesMessage(alert_msg));
					}


				}
			}
			// added for calculation on turnover grid fragment
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_ReCalculate")){
				int n = formObject.getLVWRowCount("cmplx_FinacleCore_credturn_grid");
				if (n>0)
				{				
				calculateTrnovrGrid();
				}
				else{
					alert_msg="Please select one row and then Calculate";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}
			// added by  abhishek to modify the avg nbc repeater data 
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Repeater_modify")){
				boolean flag = false;
				IRepeater repObj1 = formObject.getRepeaterControl("FinacleCore_Frame9");
				String Accno = formObject.getNGValue("FinacleCore_acc_modify");
				for( int i=0 ; i<6 ; i++){
					if(repObj1.getValue(i,"cmplx_FinacleCore_cmplx_avgbalance_new_Account").equalsIgnoreCase(Accno)){
						flag=true;
					}
				}
				if(flag==true){
					ModifyAvgNBCData(Accno,repObj1);
				}
				else{
					alert_msg="Please enter Account number from Repeater";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				}

			// added by  abhishek to modify the turnover repeater data 
			else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_TrnRepeater_modify")){
				if(formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")>0 || formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")==0){
					String Accno = formObject.getNGValue("cmplx_FinacleCore_credturn_grid", formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"), 0);
					ModifyTrnOverData(Accno);
					/// clear grid
					int n = formObject.getLVWRowCount("cmplx_FinacleCore_credturn_grid");

					int[] arrOfIndexes =  new int[n];
					int x = 0;
					SKLogger_CC.writeLog("CC> ","turnover grid after select query for turnover row count value is::"+n);
					for(int i=0;i<n;i++){

						if(formObject.getNGValue("cmplx_FinacleCore_credturn_grid",i,0).equalsIgnoreCase(Accno) ){
							arrOfIndexes[x] = i;
							x++;

						}
					}
					SKLogger_CC.writeLog("CC> ","turnover grid after select query for turnover arrray value is::"+arrOfIndexes);
					formObject.setSelectedIndices("cmplx_FinacleCore_credturn_grid", arrOfIndexes);
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_credturn_grid");
				}

				else{
					alert_msg="Please select one row and then Modify Data";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


			}
			else{
				SKLogger_CC.writeLog("CC Cad analyist 1 "," no condition meet in Cad click event");
			}
			break;

			// added by abhishek for repeater end

			//ended by yash 
		case VALUE_CHANGED:
			SKLogger_CC.writeLog(" In PL_Initiationasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
			//code added for LOB
			if (pEvent.getSource().getName().equalsIgnoreCase("TLIssueDate")){
				SKLogger_CC.writeLog("RLOS val change ", "Value of TLIssueDate is:"+formObject.getNGValue("TLIssueDate"));
				getAge(formObject.getNGValue("TLIssueDate"),"lob");
			}
			//code added for LOB
			if (pEvent.getSource().getName().equalsIgnoreCase("indusSector")){
				
				LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y'");
			}

			if (pEvent.getSource().getName().equalsIgnoreCase("indusMAcro")){
				LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("indusMAcro")+"' and IsActive='Y'");
			}
			
			else if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
				SKLogger_CC.writeLog("CC val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
				getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_DOJ")){
				//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
				getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_EmpIndusSector")){

				LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
			}
			//added by yashfor CC FSD
			else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
				String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
				 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
				 String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
				 SKLogger_CC.writeLog(" query is ",sQuery);
				 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
				 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
				 { SKLogger_CC.writeLog(" notepad details workstep value ",recordList.get(0).get(0));
					 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
					 SKLogger_CC.writeLog(" notepad details workstep value ",recordList.get(0).get(1));
					 formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
				 }
				//LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
				/*String sQuery = "select code,workstep from ng_master_notedescription where Code='" +  notepad_desc + "'";
				SKLogger_CC.writeLog(" query is ",sQuery);
				List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
				if(recordList!=null && recordList.get(0)!=null && recordList.get(0).get(0)!= null && !recordList.get(0).get(0).equalsIgnoreCase("") )
				{
					formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
					SKLogger_CC.writeLog(" notepad details workstep value ",recordList.get(0).get(1));
					formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
				}*/
			//	formObject.setNGValue("NotepadDetails_notecode",notepad_desc);
			}
			else if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_Indus_Macro")){
				LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
			} 
			else if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_total_deduction_3month")){
				float totalCredit = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_credit_3month"));
				float deduction = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_deduction_3month"));
				if(totalCredit>deduction){
				float avgCredit = (totalCredit - deduction)/3;

					formObject.setNGValue("cmplx_FinacleCore_avg_credit_3month",convertFloatToString(avgCredit));
			} 
				else{
					alert_msg="Deduction value should be less than Total Credit";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			} 
			else if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_FinacleCore_total_deduction_6month")){
				float totalCredit = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_credit_6month"));
				float deduction = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_deduction_6month"));
				if(totalCredit>deduction){
				float avgCredit = (totalCredit - deduction)/6;

					formObject.setNGValue("cmplx_FinacleCore_avg_credit_6month",convertFloatToString(avgCredit));
				}
				else{
					alert_msg="Deduction value should be less than Total Credit";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}
			else{
				SKLogger_CC.writeLog("CC Cad Analyst  "," ");
			}
		default: break;

		}
	}


	public void getDataFromALOC(FormReference formObject2, String corpName) {
		try{
			String query = "select INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL_EXPAT from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME = '"+corpName+"'";
			List<List<String>> result = formObject2.getDataFromDataSource(query);
			if(result!=null && result.size()>0){
				formObject2.setNGValue("indusSector", result.get(0).get(0));
				formObject2.setNGValue("indusMAcro", result.get(0).get(1));
				formObject2.setNGValue("indusMicro", result.get(0).get(2));
				formObject2.setNGValue("CompanyDetails_highdelin", result.get(0).get(3));
				formObject2.setNGValue("CompanyDetails_currempcateg", result.get(0).get(4));
			}
		}
		catch(Exception ex){
			SKLogger_CC.writeLog(" Cc_CAD getDataFromALOC function exception", printException(ex));
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
		SKLogger_CC.writeLog("CC","inside submitformcompleted: ");
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	    String Decision= formObject.getNGValue("cmplx_DEC_Decision");
	    if (Decision.equalsIgnoreCase("Approve")){
			List<String> objInput=new ArrayList<String>();
			//disha FSD cad delegation procedure changes
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			objInput.add("Text:"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));
			SKLogger_CC.writeLog("PL","objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3));
			formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);
	    }

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		// below line commented because now we have one common decision fragment
		//formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_CADDecision_Decision"));
		formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_DEC_Decision"));

		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (07/10)


		if(formObject.getNGValue("cmplx_DEC_Decision").equalsIgnoreCase("Refer to Smart CPV"))
		{
			saveSmartCheckGrid();
		}
		if(formObject.getNGValue("cmplx_DEC_Decision").equalsIgnoreCase("Refer to Credit"))
		{
			//++ below code added 10-10-2017 - to save cad decision tray in external table
            SKLogger_CC.writeLog("CC","inside submitFormStarted value of ReferTo : "+ formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
            formObject.setNGValue("Cad_Deviation_Tray",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
            formObject.setNGValue("q_Emailto",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
		}
		saveIndecisionGrid();
	}

}

