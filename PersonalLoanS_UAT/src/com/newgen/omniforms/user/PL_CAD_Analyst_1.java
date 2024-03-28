/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CAD_Analyst_1.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.                 12-6-2017     Disha	       Common function for decision fragment textboxes and combo visibility
------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.text.DateFormat;
import java.util.Date;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

import com.newgen.omniforms.util.PL_SKLogger;



import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.excp.CustomExceptionHandler;








import javax.faces.application.*;

public class PL_CAD_Analyst_1 extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation RLOS");
		PL_SKLogger.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
     try{
            System.out.println("Inside PL_CAD_Analyst_1 PL");
            new PersonalLoanSCommonCode().setFormHeader(pEvent);
        }catch(Exception e)
        {
            PL_SKLogger.writeLog("RLOS Initiation", "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String alert_msg="";
		String outputResponse = "";
		String SystemErrorCode="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
					
					break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/
							if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
								//setDisabled();
								loadPicklistCustomer();
								formObject.setLocked("Customer_Frame1",true);
								formObject.setEnabled("Customer_save",true);
							
							
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
								
								 formObject.setLocked("cmplx_CADDecision_ScoreGrade",true);
								 formObject.setLocked("cmplx_CADDecision_LoanApprovalAuthority",true);
								
	                             String query1="Select Product_Type,AgreementId,TotalOutstandingAmt,NextInstallmentAmt,Consider_For_Obligations,wi_name from ng_RLOS_CUSTEXPOSE_LoanDetails where wi_name='"+formObject.getWFWorkitemName()+"'";
	                             String query2="Select Deviation_Code_Refer,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'";
	                             //String query3="Select Delegation_Authorithy,Score_Grade,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'";
	 
	                             List<List<String>> mylist=formObject.getDataFromDataSource(query1);
	                             PL_SKLogger.writeLog("PL","Inside CAD_Decision: mylist is:"+mylist.toString());
	                             for(List<String> a:mylist)
	                                 formObject.addItemFromList("cmplx_CADDecision_cmplx_gr_exposureDetails",a);
	                             
	                             List<List<String>> mylist1=formObject.getDataFromDataSource(query2);
	                             PL_SKLogger.writeLog("PL","Inside CAD_Decision: mylist1 is:"+mylist.toString());
	                             for(List<String> a:mylist1)
	                                 formObject.addItemFromList("cmplx_CADDecision_cmplx_gr_DeviationDetails",a);
	                             
	                             formObject.setNGValue("cmplx_CADDecision_ScoreGrade",formObject.getNGValue("Select Score_Grade,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'"));
	                             formObject.setNGValue("cmplx_CADDecision_LoanApprovalAuthority",formObject.getNGValue("Select Delegation_Authorithy,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'"));
	                         }

							
							if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
								formObject.setLocked("Product_Frame1",true);
								//loadProductCombo();
								LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
							//	LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
								/*String ReqProd=formObject.getNGValue("ReqProd");
								PL_SKLogger.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
								loadPicklistProduct("Personal Loan");*/
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
								
					 			formObject.setLocked("GuarantorDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
								
								 String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
			                        PL_SKLogger.writeLog("PL", "Emp Type Value is:"+EmpType);

			                        if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
			                        {
			                        	formObject.setVisible("IncomeDetails_Frame3", false);
			                            formObject.setHeight("Incomedetails", 630);
			                            formObject.setHeight("IncomeDetails_Frame1", 605);  
			                        }
			                        else if(EmpType.equalsIgnoreCase("Self Employed"))
			                        {                                                                                                              
				                        formObject.setVisible("IncomeDetails_Frame2", false);
				                        formObject.setTop("IncomeDetails_Frame3",40);
				                        formObject.setHeight("Incomedetails", 300);
				                        formObject.setHeight("IncomeDetails_Frame1", 280);
			                        }
								
					 			//formObject.setLocked("IncomeDetails_Frame1",true);					 			
					 			formObject.setVisible("IncomeDetails_Label13",false);
								formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
								formObject.setVisible("IncomeDetails_Label15",true); 
								formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
								formObject.setVisible("IncomeDetails_Label16",true);
								formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
								// disha FSD
								formObject.setEnabled("cmplx_IncomeDetails_grossSal",true);
								//formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
								//formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
								//formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
								//formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
								//formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
								//formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
								//formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
								//formObject.setLocked("cmplx_IncomeDetails_Flying_Avg",true);
								formObject.setLocked("cmplx_IncomeDetails_totSal",true);
								formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);
								
								if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true"))
								{
									formObject.setLocked("cmplx_IncomeDetails_DurationOfBanking",true);
									formObject.setLocked("cmplx_IncomeDetails_NoOfMonthsOtherbankStat",true);
								}
								formObject.setVisible("IncomeDetails_Frame3",false);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
								String App_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
								formObject.setNGValue("cmplx_Liability_New_overrideIntLiab",true);
								formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",true);
								
					 			formObject.setLocked("ExtLiability_takeoverAMount",true);//Arun (12/08/17)
								formObject.setLocked("ExtLiability_QCAmt",true);//Arun (12/08/17)
								formObject.setLocked("ExtLiability_QCEMI",true);//Arun (12/08/17)
								formObject.setLocked("cmplx_Liability_New_DBR",true);
								formObject.setLocked("cmplx_Liability_New_TAI",true);
								formObject.setLocked("cmplx_Liability_New_DBRNet",true);
								formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
								formObject.setVisible("ExtLiability_Label15",true);
								formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
								
								/*formObject.setVisible("Liability_New_Label1",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_MOB",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_Label3",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_Utilization",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_Label5",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_Outstanding",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_delinin3",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_DPD30inlast6",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_DPD30inlast18",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_Label2",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_writeoff",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_Label4",false);//Arun (16/08/17)
								formObject.setVisible("Liability_New_worststatuslast24",false);//Arun (16/08/17)*/  //Arun (09/10)
								
								formObject.setNGValue("cmplx_Liability_New_overrideIntLiab",true);//Arun (27/09/17)
								formObject.setNGLVWRowHeight("Liability_New_Label8", 152);
								formObject.setNGLVWRowHeight("Liability_New_Text3", 168);
								if (App_Type != "RESCH"){
									formObject.setVisible("Liability_New_Label6", false);
									formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
								}
								
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
								
					 			//formObject.setLocked("EMploymentDetails_Frame1",true);
								if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="true" || formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="true")
								{
									formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
									formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
									formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
									formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
									formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
									formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
									formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
									formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
									formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
									formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
									formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
									formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
									formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
									formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
									formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
									formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
									formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
									formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
									
									formObject.setLocked("cmplx_EmploymentDetails_EmpStatus",false);
									formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
									formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
									formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
									formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
									//formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
									
									formObject.setLocked("cmplx_EmploymentDetails_LOS",false);								
									
									formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
									formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);

									formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
									formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
									formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
									formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
									formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
									formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
									formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
								}
								if((formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="false") && (formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="false"))
								{
									// disha FSD
									formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL","CNOAL");
									formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CNOAL ( disabled) /Awaiting FVR");
									formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
									formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
									formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
									formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
									formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
									formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
									formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
									formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
									formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
									formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
									formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
									
									formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",false);
									formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",false);
									formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",false);
									formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
									formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
									formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
									formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
									formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
									//formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
									formObject.setLocked("cmplx_EmploymentDetails_LOS",false);
									formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",false);
									formObject.setLocked("cmplx_EmploymentDetails_EmirateOfWork",false);
									formObject.setLocked("cmplx_EmploymentDetails_HeadOfficeEmirate",false);
									formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);
									formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
									formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
									formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
									formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
									formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
									formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
									formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
								}
								loadPicklist4();
								
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
								
					 			//formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					 			formObject.setVisible("ELigibiltyAndProductInfo_Frame5",false);
					 			
					 			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
								formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
								
								
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
								
					 			formObject.setLocked("LoanDetails_Frame1",true);
					 			
					 			//formObject.setEnabled("LoanDetails_Button1",true);
					 			/*LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_Master_InstallmentPlan with (nolock) order by Code");
				                LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_COLLECTIONBRANCH with (nolock) order by code");
				                LoadPickList("cmplx_LoanDetails_ddastatus", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_DDASTATUS with (nolock) order by code");
				                LoadPickList("LoanDetails_modeofdisb", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ModeofDisbursal with (nolock) order by code");
				                LoadPickList("LoanDetails_disbto", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
				                LoadPickList("LoanDetails_holdcode", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_HoldCode with (nolock) order by code");
				                LoadPickList("cmplx_LoanDetails_paymode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_PAYMENTMODE with (nolock)");
				                LoadPickList("cmplx_LoanDetails_status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_STATUS with (nolock)");
				                LoadPickList("cmplx_LoanDetails_bankdeal", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
				                LoadPickList("cmplx_LoanDetails_city", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
				               commented by akshay on 14/10/17---called from common file*/
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
								loadPicklist_Address();
								formObject.setLocked("AddressDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
								
					 			formObject.setLocked("AltContactDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
								
					 			formObject.setLocked("CardDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
								
					 			formObject.setLocked("ReferenceDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
								
					 			formObject.setLocked("SupplementCardDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
								
					 			formObject.setLocked("FATCA_Frame6",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
								
					 			formObject.setLocked("KYC_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
								
					 			formObject.setLocked("OECD_Frame8",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
								
					 			formObject.setLocked("PartMatch_Frame1",true);
					 			LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
								
					 			formObject.setLocked("FinacleCRMIncident_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
								
					 			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
								
					 			//formObject.setLocked("FinacleCore_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
								
					 			formObject.setLocked("MOL1_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
								
					 			formObject.setLocked("WorldCheck1_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
								
					 			//formObject.setLocked("RejectEnq_Frame1",true);
							}

							if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
	
								formObject.setLocked("SalaryEnq_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {
								
					 			formObject.setLocked("CustDetailVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {
								
					 			formObject.setLocked("BussinessVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {
								
					 			formObject.setLocked("HomeCountryVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {
								
					 			formObject.setLocked("ResidenceVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification")) {
								
					 			formObject.setLocked("GuarantorVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {
								
					 			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {
								
					 			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {
								
					 			formObject.setLocked("LoanandCard_Frame1",true);
							}
							

							if (pEvent.getSource().getName().equalsIgnoreCase("ReferHistory"))
							{
								formObject.setLocked("ReferHistory_Frame1",true);
							}
							//disha FSD
							if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
								
					 			//formObject.setLocked("NotepadDetails_Frame1",true);
								formObject.setVisible("NotepadDetails_Frame3",false);
								String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
								PL_SKLogger.writeLog("PL notepad ", "Activity name is:" + sActivityName);
								int user_id = formObject.getUserId();
								String user_name = formObject.getUserName();
								user_name = user_name+"-"+user_id;					
								formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
								formObject.setNGValue("NotepadDetails_Actusername",user_name); 
								formObject.setNGValue("NotepadDetails_user",user_name); 
								formObject.setLocked("NotepadDetails_noteDate",true);
								formObject.setLocked("NotepadDetails_Actusername",true);
								formObject.setLocked("NotepadDetails_user",true);
								formObject.setLocked("NotepadDetails_insqueue",true);
								formObject.setLocked("NotepadDetails_Actdate",true);
								formObject.setVisible("NotepadDetails_save",true);
								formObject.setLocked("NotepadDetails_notecode",true);
								formObject.setHeight("NotepadDetails_Frame1",450);
								formObject.setTop("NotepadDetails_save",400);
								LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
							}
								//disha FSD
							if (pEvent.getSource().getName().equalsIgnoreCase("RiskRating")) {
								
								formObject.setLocked("RiskRating_Frame1",true);
								loadPicklistRiskRating(); //Arun (10/10)
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) {
	
								formObject.setVisible("SmartCheck1_Label2",false);
								formObject.setVisible("SmartCheck1_CPVrem",false);
								formObject.setVisible("SmartCheck1_Label4",false);
								formObject.setVisible("SmartCheck1_FCUrem",false);
								formObject.setLocked("SmartCheck1_Modify",true);
							}
							//disha FSD
							if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList"))
							{
								formObject.setLocked("ExternalBlackList_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	loadPicklist1();
								
								formObject.setVisible("DecisionHistory_Button5",true);
								formObject.setVisible("DecisionHistory_CheckBox1",true);
								formObject.setVisible("DecisionHistory_Label26",true);
								formObject.setVisible("DecisionHistory_Combo13",true);
			                	formObject.setLocked("cmplx_Decision_ReferTo", true);
								//changes by Arun on 07-10-2017 to hide the label Cad Decision Tray at cad analyist 1 WS
			                	//formObject.setVisible("DecisionHistory_Label18",true);
								formObject.setVisible("cmplx_Decision_ReferTo",true);
			                	formObject.setVisible("DecisionHistory_Button4",true);
								formObject.setVisible("DecisionHistory_Label13",true);
								formObject.setVisible("cmplx_Decision_Deviationcode",true);
								formObject.setVisible("DecisionHistory_Label14",true);
								formObject.setVisible("cmplx_Decision_Dectech_decsion",true);
								formObject.setVisible("DecisionHistory_Label15",true);
								formObject.setVisible("cmplx_Decision_score_grade",true);
								formObject.setVisible("DecisionHistory_Label16",true);
								formObject.setVisible("cmplx_Decision_Highest_delegauth",true);
								formObject.setVisible("cmplx_Decision_Manual_Deviation",true);
								formObject.setVisible("DecisionHistory_Button6",true);
								formObject.setVisible("cmplx_Decision_Manual_deviation_reason",true);
								
								formObject.setVisible("DecisionHistory_Label18",true);
								formObject.setVisible("cmplx_Decision_CADDecisiontray",true);
								
								formObject.setVisible("DecisionHistory_Label5",false);
								formObject.setVisible("cmplx_Decision_desc",false);
								formObject.setVisible("DecisionHistory_chqbook",false);						
								formObject.setVisible("DecisionHistory_Label6",false);
								formObject.setVisible("cmplx_Decision_IBAN",false);
								formObject.setVisible("DecisionHistory_Label7",false);
								formObject.setVisible("cmplx_Decision_AccountNo",false);
								formObject.setVisible("DecisionHistory_Label8",false);
								formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
								formObject.setVisible("DecisionHistory_Label9",false);
								formObject.setVisible("cmplx_Decision_DebitcardNumber",false);
								
								formObject.setVisible("DecisionHistory_Label26",false);
								
								formObject.setVisible("cmplx_Decision_rejreason",false);
								formObject.setVisible("DecisionHistory_Button1",false);
								formObject.setVisible("DecisionHistory_Rejreason",false);
								
								
								formObject.setLocked("cmplx_Decision_Deviationcode",true);
								formObject.setLocked("cmplx_Decision_Dectech_decsion",true);
								formObject.setLocked("cmplx_Decision_score_grade",true);
								formObject.setLocked("cmplx_Decision_Highest_delegauth",true);
								
								formObject.setLocked("cmplx_Decision_Manual_deviation_reason",true);
								formObject.setLocked("DecisionHistory_Button6",true);
								
								 formObject.setLocked("cmplx_Decision_CADDecisiontray",true);//Arun (08/10)
								// formObject.setLocked("cmplx_Decision_Manual_Deviation",true);//Arun (08/10)
								formObject.setEnabled("cmplx_Decision_Manual_Deviation", true);	
								formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
								//commented by saurabh for testing.
								formObject.setLeft("DecisionHistory_Label3", 24);
								formObject.setLeft("cmplx_Decision_strength", 24);						
								formObject.setLeft("DecisionHistory_Label4", 302);
								formObject.setLeft("cmplx_Decision_weakness", 302);
								formObject.setLeft("Decision_Label4", 590);
								formObject.setLeft("cmplx_Decision_REMARKS", 590);						
								formObject.setLeft("DecisionHistory_Label13", 24);
								formObject.setLeft("cmplx_Decision_Deviationcode", 24);						
								formObject.setLeft("DecisionHistory_Label14", 302);
								formObject.setLeft("cmplx_Decision_Dectech_decsion", 302);
								formObject.setLeft("DecisionHistory_Label15", 590);
								formObject.setLeft("cmplx_Decision_score_grade", 590);
								formObject.setLeft("DecisionHistory_Label16", 822);
								formObject.setLeft("cmplx_Decision_Highest_delegauth", 822);
								formObject.setLeft("DecisionHistory_Button4", 674);
								formObject.setLeft("DecisionHistory_Button5", 1074);
								formObject.setLeft("DecisionHistory_Label18", 590);
								formObject.setLeft("cmplx_Decision_ReferTo", 590);
								formObject.setLeft("DecisionHistory_CheckBox1", 560);
								formObject.setLeft("DecisionHistory_Label26", 813);
								formObject.setLeft("DecisionHistory_Combo13", 813);
								
								formObject.setLeft("DecisionHistory_Label18",1074);
								formObject.setLeft("cmplx_Decision_CADDecisiontray",1074);
								formObject.setLeft("Decision_Label1",297);
								formObject.setTop("Decision_Label1",8);
								formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED",297);
								formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",30);
								formObject.setTop("DecisionHistory_Label18",150);								
								formObject.setTop("cmplx_Decision_CADDecisiontray",190);							
								
								formObject.setLeft("DecisionHistory_Button6", 1074);
								formObject.setLeft("cmplx_Decision_Manual_deviation_reason", 1074);
								formObject.setLeft("cmplx_Decision_Manual_Deviation", 900);
								formObject.setTop("DecisionHistory_Button5", 72);
								formObject.setTop("DecisionHistory_Button6", 120);
								formObject.setTop("cmplx_Decision_Manual_Deviation", 104);
								formObject.setTop("cmplx_Decision_Manual_deviation_reason", 124);
								formObject.setTop("DecisionHistory_save",400);
								formObject.setTop("Decision_Label3", 56);
								formObject.setTop("cmplx_Decision_Decision", 72);
								formObject.setTop("DecisionHistory_Label1", 56);
								formObject.setTop("cmplx_Decision_refereason", 72);								
								//formObject.setTop("DecisionHistory_Label18", 56);
								formObject.setTop("cmplx_Decision_ReferTo", 72);
								formObject.setTop("DecisionHistory_Label3", 104);
								formObject.setTop("cmplx_Decision_strength", 120);
								formObject.setTop("DecisionHistory_Label4", 104);
								formObject.setTop("cmplx_Decision_weakness", 120);
								formObject.setTop("Decision_Label4", 104);
								formObject.setTop("cmplx_Decision_REMARKS", 120);						
								formObject.setTop("DecisionHistory_Label13", 172);
								formObject.setTop("cmplx_Decision_Deviationcode", 188);						
								formObject.setTop("DecisionHistory_Label14", 172);
								formObject.setTop("cmplx_Decision_Dectech_decsion", 188);
								formObject.setTop("DecisionHistory_Label15", 172);
								formObject.setTop("cmplx_Decision_score_grade", 188);
								formObject.setTop("DecisionHistory_Label16", 172);
								formObject.setTop("cmplx_Decision_Highest_delegauth", 188);
								formObject.setTop("Decision_ListView1", 226);								
								formObject.setTop("DecisionHistory_CheckBox1", 15);
								formObject.setTop("DecisionHistory_Label26", 8);
								formObject.setTop("DecisionHistory_Combo13", 23);
								
								formObject.setTop("cmplx_Decision_Manual_Deviation", 120);
								
								formObject.setTop("DecisionHistory_Button4", 8);
								
								
								LoadPickList("cmplx_Decision_CADDecisiontray", "select '--Select--' union select convert(varchar, Refer_Credit) from ng_master_ReferCredit");
								//condition added by saurabh on 15th Oct.
								if(!formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Approve")){
									
									 formObject.setLocked("cmplx_Decision_CADDecisiontray",false);
								}
			                	//Common function for decision fragment textboxes and combo visibility
			                	//decisionLabelsVisibility();
						 } 	
					
					
					  break;
					  
					case MOUSE_CLICKED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
							//GenerateXML();
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
							formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						//below else if added by saurabh on 9th Oct for saving liability in CAD1. JIRA-2790.
						else if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Save")){
							formObject.saveFragment("InternalExternalLiability");
							alert_msg="Liability Details Saved";
                			throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

						}
						if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Add")){
							formObject.setNGValue("Liability_Wi_Name",formObject.getWFWorkitemName());
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Add")){
							//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							//SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_DECCAD_cmplx_gr_DECCAD");

						}
						//disha FSD
						else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
							formObject.setNGValue("Notepad_wi_name",formObject.getWFWorkitemName());
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
							
							String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
							PL_SKLogger.writeLog("PL notepad ", "Activity name is:" + sActivityName);
							int user_id = formObject.getUserId();
							String user_name = formObject.getUserName();
							user_name = user_name+"-"+user_id;					
							formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
							formObject.setNGValue("NotepadDetails_Actusername",user_name,false); 
							formObject.setNGValue("NotepadDetails_user",user_name,false);
							
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							 Date date = new Date();
							 formObject.setNGValue("NotepadDetails_noteDate",dateFormat.format(date),false); 
							 formObject.setNGValue("NotepadDetails_Actdate",dateFormat.format(date),false); 

						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

						}	
							
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button1")){
							formObject.setNGValue("Dds_wi_name",formObject.getWFWorkitemName());
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_DDSgrid");
						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button3")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_DDSgrid");

						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button2")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_DDSgrid");
						}
						

						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button4")){
							formObject.setNGValue("Inward_wi_name",formObject.getWFWorkitemName());
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_inwardtt");
						}
					
						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button6")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_inwardtt");

						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button5")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_inwardtt");
						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button7")){
							formObject.ExecuteExternalCommand("NGClearRow", "cmplx_FinacleCore_inwardtt");
						}
						
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Add")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
							PL_SKLogger.writeLog("PL_Initiation", "Inside Customer_save button: ");
							formObject.saveFragment("CustomerDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
							formObject.saveFragment("ProductContainer");
						}
						//code by saurabh on 3rd oct.
						else if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
							formObject.saveFragment("EmploymentDetails");
							
                			alert_msg="Employment Details Saved";
                			throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
							formObject.saveFragment("GuarantorDet");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
							formObject.saveFragment("IncomeDEtails");
							alert_msg="Income Details  Saved";

							throw new ValidatorException(new FacesMessage(alert_msg));
						
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
							formObject.saveFragment("IncomeDEtails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
							formObject.saveFragment("CompanyDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Save")){
							formObject.saveFragment("PartnerDetails");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
							formObject.saveFragment("Liability_container");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
							formObject.saveFragment("InternalExternalContainer");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
							formObject.saveFragment("EmploymentDetails");
						}
						
						else  if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
                            String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
                            String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
                            PL_SKLogger.writeLog("PL", "EMpName$"+EmpName+"$");
                            String query=null;
							//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017 Disha
                            if(EmpName.trim().equalsIgnoreCase(""))
                                            query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

                            else
                                            query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";

                            PL_SKLogger.writeLog("PL", "query is: "+query);
                            populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,Nature Of Business,EMPLOYER CATEGORY PL NATIONAL,EMPLOYER CATEGORY CARDS,EMPLOYER CATEGORY PL EXPAT,INCLUDED IN PL ALOC,DOI IN PL ALOC,INCLUDED IN CC ALOC,DATE OF INCLUSION IN CC ALOC,NAME OF AUTHORIZED PERSON FOR ISSUING SC/STL/PAYSLIP,ACCOMMODATION PROVIDED,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,OWNER/PARTNER/SIGNATORY NAMES AS PER TL,ALOC REMARKS,HIGH DELINQUENCY EMPLOYER,MAIN EMPLOYER CODE", true, 20);

						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
							formObject.saveFragment("EligibilityAndProductInformation");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
							formObject.saveFragment("MiscFields");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
							formObject.saveFragment("Address_Details_container");
						}
						//disha FSD
						else if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
							formObject.saveFragment("Alt_Contact_container");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_save")){
							formObject.saveFragment("ReferenceDetails");
					    }
					 	
						else if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
							formObject.saveFragment("Card_Details");
						}
						
						//Supplementary_Card_Details
						else if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
							formObject.saveFragment("FATCA");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
							formObject.saveFragment("KYC");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
							formObject.saveFragment("OECD");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Save")){
							formObject.saveFragment("Part_Match");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident_save")){
							formObject.saveFragment("FinacleCRM_Incidents");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_save")){
							formObject.saveFragment("FinacleCRM_CustInfo");
						}						
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_save")){
							formObject.saveFragment("Finacle_Core");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("MOL1_Save")){
							formObject.saveFragment("MOL");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Save")){
							formObject.saveFragment("World_Check");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq_Save")){
							formObject.saveFragment("Sal_Enq");
						}
							
						else if(pEvent.getSource().getName().equalsIgnoreCase("CreditCardEnq_Save")){
							formObject.saveFragment("Credit_card_Enq");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("CaseHistoryReport_Save")){
							formObject.saveFragment("Case_History");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("LOS_Save")){
							formObject.saveFragment("LOS");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("RejectEnq_Save")){
							formObject.saveFragment("Reject_Enq");
						}
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("CAD_save")){
							formObject.saveFragment("Notepad_Details");
						}

						else if(pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision_save")){
							formObject.saveFragment("Dec");
						}
						//disha FSD
						else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_save")){
							formObject.saveFragment("DecisionHistory");
                            alert_msg="Decision History Details Saved";
                			throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
						}
						else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Add")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						//Code to add the dectech call on PL
						 if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
							{		//formObject.setNGValue("DecCallFired","Eligibility");
							try{
								double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
								double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
								double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
								PL_SKLogger.writeLog("$$LoanAmount ==== "+LoanAmount,"");
								PL_SKLogger.writeLog("$$tenor ==== "+tenor,"");
								PL_SKLogger.writeLog("$$RateofInt ==== "+RateofInt,"");
								String EMI=getEMI(LoanAmount,RateofInt,tenor);
								PL_SKLogger.writeLog("$$EMI ==== "+EMI,"");
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
								
							}
							catch(Exception e){
								PL_SKLogger.writeLog("Exception Occured in RLOS Iniitation : "," Exception in EMI Generation");
							}
									PL_SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
									formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
									income_Dectech();
									//formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
									formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
									employment_dectech();
									formObject.fetchFragment("MOL", "MOL1", "q_MOL");
									formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
									formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
									formObject.setTop("Reject_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
									formObject.setTop("Sal_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
									loadPicklistMol();
									outputResponse = GenerateXML("DECTECH","");
									//SKLogger.writeLog("$$After Generatexml for CallFired : "+CallFired,"");
									PL_SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");
									

									SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
									PL_SKLogger.writeLog("RLOS value of ReturnCode",SystemErrorCode);
									if(SystemErrorCode.equalsIgnoreCase("")){
										valueSetCustomer(outputResponse);   
										alert_msg="Decision engine integration successful";
										PL_SKLogger.writeLog("after value set customer for dectech call","");
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
							//Code to addd dechtech call for pl end 
						
						 /*if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
							{		popupFlag="Y";
									//formObject.setNGValue("DecCallFired","Eligibility");
									PL_SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
									formObject.fetchFragment("MOL", "MOL1", "q_MOL");
									formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
									formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
									formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
									formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
									loadPicklistMol();
									outputResponse = GenerateXML("DECTECH","");
									//SKLogger.writeLog("$$After Generatexml for CallFired : "+CallFired,"");
									PL_SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");
									

									SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
									PL_SKLogger.writeLog("RLOS value of ReturnCode",SystemErrorCode);
									if(SystemErrorCode.equalsIgnoreCase("")){
										valueSetCustomer(outputResponse);   
										alert_msg="Decision engine integration successful";
										PL_SKLogger.writeLog("after value set customer for dectech call","");
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
							//Code to addd dechtech call for pl end 
*/						 
						//Code to addd dechtec call on Decision for pl start 
							if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button5")){	
								formObject.setNGValue("DecCallFired","Decision");
								PL_SKLogger.writeLog("$$Before Generatexml for dectech call..outputResponse is : "+outputResponse,"");
								formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
								income_Dectech();
								formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
								employment_dectech();
								formObject.fetchFragment("MOL", "MOL1", "q_MOL");
								formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
								formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
								formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
								formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
								loadPicklistMol();
								outputResponse = GenerateXML("DECTECH","");
								PL_SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");
								
								SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
								PL_SKLogger.writeLog("RLOS value of ReturnCode",SystemErrorCode);
								if(SystemErrorCode.equalsIgnoreCase("")){
									valueSetCustomer(outputResponse);   
									alert_msg="Decision engine integration successful";
									PL_SKLogger.writeLog("after value set customer for dectech call","");
									formObject.RaiseEvent("WFSave");
									throw new ValidatorException(new FacesMessage(alert_msg));				
								}
								else{
									alert_msg="Critical error occurred Please contact administrator";
									throw new ValidatorException(new FacesMessage(alert_msg));
							}
						}	
							//Code to addd dechtec call on Decision for pl end  
					
					 case VALUE_CHANGED:
							PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
								 	  }
							 	}
							 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
									//SKLogger_CC.writeLog("CC val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
									getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
								}
								if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_DOJ")){
									//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
									getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
								}
							 // disha FSD
							 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
								 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
								 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
								 String sQuery = "select code from ng_master_notedescription where Description='" +  notepad_desc + "'";
								 PL_SKLogger.writeLog(" query is ",sQuery);
								 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
								 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
								 {
									 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
									 formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
								 }
							 }
							if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_Decision")){
								formObject.setEnabled("cmplx_Decision_Manual_Deviation", true);	
								//below condition changed by saurabh on 15th Oct for JIRA-2901.
							 if(!formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Approve")){
									
									 formObject.setLocked("cmplx_Decision_CADDecisiontray",false);
								}
								else{
									//formObject.setEnabled("cmplx_Decision_Manual_Deviation", false);	
									 formObject.setLocked("cmplx_Decision_CADDecisiontray",true);
								}
							}	 
								 
								 
								 //Arun (08/10)
								 
							
								 
							 
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


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource()); 
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
            List<String> objInput=new ArrayList<String> ();
			//disha FSD cad delegation procedure changes
            objInput.add("Text:"+formObject.getWFWorkitemName());
            objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
            objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
            objInput.add("Text:"+formObject.getNGValue("cmplx_Decision_Highest_delegauth"));
            PL_SKLogger.writeLog("PL","objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3));
            formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);
		
	}


	/*public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}*/
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	         formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
	         formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
			 ////Arun (07/10/2017) to save decesion tray data in hidden to save in Exttable.
	         formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_Decision_CADDecisiontray")); 
	         //disha FSD
	         if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Refer to Credit"))
	         {
	             formObject.setNGValue("q_MailTo",formObject.getNGValue("cmplx_Decision_ReferTo"));
	         }
	        saveIndecisionGrid();
	    }

}

