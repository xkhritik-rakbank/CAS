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

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.text.DateFormat;
import java.util.Date;
import javax.print.PrintException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
//import com.newgen.omniforms.skutil.AesUtil;
import com.newgen.omniforms.util.PL_SKLogger;


import com.newgen.omniforms.util.PL_SKLogger;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.excp.CustomExceptionHandler;








import javax.faces.application.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class PL_DDVT_maker extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL");
		PL_SKLogger.writeLog("PL DDVT MAKER", "Inside formLoaded()" + pEvent.getSource().getName());
                                
	}
                

	public void formPopulated(FormEvent pEvent) 
	{  
		try{
			formObject = FormContext.getCurrentInstance().getFormReference();
            new PersonalLoanSCommonCode().setFormHeader(pEvent);  
            /*String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
			List<List<String>> outputindex = null;
			outputindex = formObject.getNGDataFromDataCache(squery);
			PL_SKLogger.writeLog("PL_DDVT_MAKER", "outputItemindex is:" +  outputindex);
			String itemIndex =outputindex.get(0).get(0);*/
		}catch(Exception e)
        	{
            	PL_SKLogger.writeLog("PL DDVT MAKER", "Exception:"+new PersonalLoanSCommonCode().printException(e));
        	}
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String outputResponse = "";
		String ReturnCode="";
		String ReturnDesc="";
		String Gender="";
		String buttonClickFlag="";


		//String ReturnCode="";
		String alert_msg="";
		String BlacklistFlag="";
		String DuplicationFlag="";
		PL_SKLogger.writeLog("Inside PL DDVT MAKER eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();


		try{
			switch(pEvent.getType())
			{              

				case FRAME_EXPANDED:
					PL_SKLogger.writeLog(" In PL_Iniation FRAME_EXPANDED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
					break;
                                                                                                                                                                
				case FRAGMENT_LOADED:
					PL_SKLogger.writeLog(" In PL DDVT MAKER FRAGMENT_LOADED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
                                                                                               
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
	                    /*if(formObject.getNGValue("cmplx_Customer_NEP")=="false"){
	                    formObject.setLocked("Customer_Frame1",true);
	                    formObject.setLocked("cmplx_Customer_EmiratesID",false);
	                    formObject.setLocked("cmplx_Customer_FIrstNAme",false);
	                    formObject.setLocked("cmplx_Customer_MiddleNAme",false);
	                    formObject.setLocked("cmplx_Customer_LastNAme",false);
	                    formObject.setLocked("cmplx_Customer_DOb",false);
	                    
	                    formObject.setLocked("cmplx_Customer_Nationality",false);
	                    formObject.setLocked("cmplx_Customer_MobNo",false);
	                    formObject.setLocked("cmplx_Customer_PAssportNo",false);
	                    formObject.setLocked("cmplx_Customer_card_id_available",false);
	                    formObject.setLocked("cmplx_Customer_CIFNO",false);
	
	                    formObject.setEnabled("FetchDetails",false);
	                    formObject.setHeight("Customer_Frame1", 640);
	                    formObject.setHeight("CustomerDetails", 650);
	                    formObject.setLocked("Customer_save",false);
	                    formObject.setLocked("FetchDetails",false);
	                    formObject.setLocked("Customer_Button1",true);
	                    }
	                    if(formObject.getNGValue("cmplx_Customer_card_id_available")=="true"){
	                                    formObject.setEnabled("cmplx_Customer_CIFNO",true);
	          
	                                    
	                                    }              
	                                    
	                    else{
	                                    formObject.setEnabled("cmplx_Customer_CIFNO",false);
	                                                    }*/ //Arun (09/09/17)
	                    //formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
	                    //formObject.setLocked("ReadFromCard",false); //Arun (09/09/17)
	                    //formObject.setLocked("cmplx_Customer_NEP",false);
	                    /*formObject.setLocked("Customer_CheckBox6",false);//CIF ID available
	                    formObject.setLocked("cmplx_Customer_referrorcode",false);
	                    formObject.setLocked("cmplx_Customer_referrorname",true);
	                    formObject.setLocked("cmplx_Customer_apptype",false);
	                    formObject.setLocked("cmplx_Customer_corpcode",false);
	                    formObject.setLocked("cmplx_Customer_bankwithus",false);
	                    formObject.setLocked("cmplx_Customer_noofdependent",false);
	                    formObject.setLocked("cmplx_Customer_minor",false);
	                    formObject.setLocked("cmplx_Customer_guarname",false);
	                    formObject.setLocked("cmplx_Customer_guarcif",false);*/ //Arun (09/09/17)
	    	
	    				formObject.setLocked("cmplx_Customer_CardNotAvailable",true);
	    				formObject.setLocked("cmplx_Customer_NEP",true);
	    				formObject.setLocked("cmplx_Customer_marsoomID",true);
	    				formObject.setLocked("Customer_Button1",true);
	    				formObject.setLocked("cmplx_Customer_age",true);
	    				formObject.setLocked("cmplx_Customer_SecNationality",true);
	    				formObject.setLocked("cmplx_Customer_GCCNational",true);
	
	                    loadPicklistCustomer();
	                    PL_SKLogger.writeLog("PL","Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
					}
                                                                                                
                    else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {                                                                                                
                    	//LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
                    	loadPicklistProduct("Personal Loan");
                    }
                    
                    else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
                        LoadPickList("title", "select '--Select--' union select convert(varchar, description) from NG_MASTER_title with (nolock)");
                        LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
                        LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                    }
                                                                                                
                    else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
        				formObject.setLocked("IncomeDetails_Frame1", true);//Arun (11/9/17)
        				formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
                        formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
                        formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
                        //formObject.setLocked("cmplx_IncomeDetails_netSal1", true);
                        //formObject.setLocked("cmplx_IncomeDetails_netSal2", true);
                        //formObject.setLocked("cmplx_IncomeDetails_netSal3", true);
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
                    }
                                                                                                
                    if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
                                                    
                        String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
                        PL_SKLogger.writeLog("PL", "Emp Type Value is:"+EmpType);

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
                                                
                  //changes for fragments disabled on liability
                    /*else  if (pEvent.getSource().getName().equalsIgnoreCase("InternalExternalLiability")) {
                    		PL_SKLogger.writeLog("Inside PL DDVT MAKER eventDispatched()", "inside intextliac");
            				formObject.setLocked("ExtLiability_Frame1", true);
            			} 
                        //changes for fragments disabled on liability    
*/                else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
                                
                    formObject.setLocked("Liability_New_fetchLiabilities",true);
                    formObject.setLocked("takeoverAMount",true);
                    formObject.setLocked("cmplx_Liability_New_DBR",true);
                    formObject.setLocked("cmplx_Liability_New_DBRNet",true);
                    formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
                    formObject.setLocked("cmplx_Liability_New_TAI",true);
                    formObject.setLocked("ExtLiability_Frame1", true);
                }
                  
                else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
                	formObject.setLocked("cif", true);
                	formObject.setLocked("CompanyDetails_Button3", true);
                    LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
                    LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                    LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                    LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                    LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
                    LoadPickList("desig", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Designation with (nolock)");
                    LoadPickList("desigVisa", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Designation with (nolock)");
                    LoadPickList("EOW", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
                    LoadPickList("headOffice", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
                }
                                                                                                
                else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
                	formObject.setLocked("CIFNo", true);
                    formObject.setLocked("AuthorisedSignDetails_Button4", true);
                    LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                    LoadPickList("SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
                }
                                                            
                else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
                	LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                }

                                                            
                else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
                                                                                                                
                    formObject.setLocked("EMploymentDetails_Frame1",true);
                    formObject.setLocked("cmplx_EmploymentDetails_ApplicationCateg",false);
                    formObject.setLocked("cmplx_EmploymentDetails_targetSegCode",false);
                    formObject.setLocked("cmplx_EmploymentDetails_marketcode",false);
                    formObject.setLocked("cmplx_EmploymentDetails_collectioncode",false);
                    formObject.setLocked("cmplx_EmploymentDetails_PromotionCode",false);
                    formObject.setLocked("cmplx_EmploymentDetails_MIS",false);
                    formObject.setEnabled("EMploymentDetails_Button2",true);
                    formObject.setLocked("cmplx_EmploymentDetails_NepType",false);
                    formObject.setLocked("cmplx_EmploymentDetails_channelcode",false);
                    if(!formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true")){
                    	formObject.setVisible("cmplx_EmploymentDetails_NepType",false);	
                    	formObject.setVisible("EMploymentDetails_Label25",false);
                    }
                    if(!formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCE") && !formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCN")){
                    	formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
                    	formObject.setVisible("EMploymentDetails_Label36",false);
                    }
                    
                    /*formObject.setVisible("EMploymentDetails_Label25",false);
                    formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
                    formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
                    formObject.setVisible("EMploymentDetails_Label62",false);
                    formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
                    formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
                    formObject.setVisible("EMploymentDetails_Label5",false);
                    formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
                    formObject.setVisible("EMploymentDetails_Label59",false);
                    formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
                    formObject.setVisible("EMploymentDetails_Label36",false);
                    formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
                    formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
                    formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
                    formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);*/
                    //loadPicklist_Employment();
                    loadPicklist4();
                }
                                                                                                
                else if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
	                formObject.setLocked("cmplx_MiscFields_School",true);
	                formObject.setLocked("cmplx_MiscFields_PropertyType",true);
	                formObject.setLocked("cmplx_MiscFields_RealEstate",true);
	                formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
                }
                                                                                                
                                                                                                
                else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
                                
                    LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
                    LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' union select convert(varchar, description) from NG_MASTER_instrumentType");
                    LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
                    formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
                    //formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);//Arun (11/08/17)
                    //formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);//Arun (11/08/17)
                    /*formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
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
                    formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);
                    formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
                    formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
                    formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
                    formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);*/

                }
                                                                                                
                                                                                                
                if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {

                	loanvalidate();//Arun (21/09/17)
	                formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
	                LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_Master_InstallmentPlan with (nolock) order by Code");
	                LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_COLLECTIONBRANCH with (nolock) order by code");
	                LoadPickList("cmplx_LoanDetails_ddastatus", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_DDASTATUS with (nolock) order by code");
	                LoadPickList("LoanDetails_modeofdisb", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ModeofDisbursal with (nolock) order by code");
	                LoadPickList("LoanDetails_disbto", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
	                LoadPickList("LoanDetails_holdcode", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_HoldCode with (nolock) order by code");
	                LoadPickList("cmplx_LoanDetails_paymode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_PAYMENTMODE with (nolock)");
	                LoadPickList("cmplx_LoanDetails_status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_STATUS with (nolock)");
	                LoadPickList("cmplx_LoanDetails_bankdeal", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
	                LoadPickList("cmplx_LoanDetails_city", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
	                String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");//Arun (21/09/17)
	                String mName = formObject.getNGValue("cmplx_Customer_MiddleName");//Arun (21/09/17)
	                String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");//Arun (21/09/17)
	                String fullName = fName+" "+mName+" "+lName; //Arun (21/09/17)
	                formObject.setNGValue("cmplx_LoanDetails_name",fullName);//Arun (21/09/17)
	                formObject.setLocked("cmplx_LoanDetails_chqno",true);//Arun (22/09/17)
	                formObject.setLocked("cmplx_LoanDetails_chqdat",true);//Arun (22/09/17)
								                                                                
                }
                                                                                                
                if (pEvent.getSource().getName().equalsIgnoreCase("Inc_Doc")) {
			        hm.put("Inc_Doc","Clicked");
			        popupFlag="N";
			        PL_SKLogger.writeLog("RLOS Initiation Inside ","IncomingDocuments");                                                            
			        PL_SKLogger.writeLog("RLOS Initiation Inside ","fetchIncomingDocRepeater");
			        formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			        PL_SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1 after fetching");
			        fetchIncomingDocRepeater();
			        PL_SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1");

                }


                else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
                    PL_SKLogger.writeLog("PL DDVT MAKER", "Inside load Address details");
                    //loadAddressDetails();
                    loadPicklist_Address();
                }
                
                else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
                    formObject.setVisible("AlternateContactDetails_custdomicile",false);
                    formObject.setVisible("AltContactDetails_Label14",false);
                }
                                                                                                
                else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
					LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
					LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
				    LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
					LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

                }
                else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
                	LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
                }
                                
                else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
                	LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                	LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
                	LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                }
                                                                                                
                if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch"))
                {
                    LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                    partMatchValues();
                    /*formObject.setLocked("PartMatch_fname",true);
                    formObject.setLocked("PartMatch_lname",true);
                    formObject.setLocked("PartMatch_funame",true);
                    formObject.setLocked("PartMatch_newpass",true);
                    formObject.setLocked("PartMatch_oldpass",true);
                    formObject.setLocked("PartMatch_visafno",true);
                    formObject.setLocked("PartMatch_Dob",true);
                    formObject.setLocked("PartMatch_nationality",true);*/
                }
                                                                                                
	            if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc")) {
	            	formObject.setVisible("IncomingDoc_UploadSig",false);
	            }
	            // disha FSD
	            if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
					
		 			formObject.setLocked("NotepadDetails_Frame1",true);
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
	                            
	            if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
	            	//loadPicklist1();
	            	fetch_desesionfragment();
	            	formObject.setNGValue("cmplx_Decision_Decision", "--Select--");  
	                formObject.setVisible("cmplx_Decision_waiveoffver",false);
	                formObject.setVisible("DecisionHistory_Label9",false);
	                formObject.setVisible("DecisionHistory_Label8",false);
	                formObject.setVisible("DecisionHistory_Label7",false);
	                formObject.setVisible("DecisionHistory_Label6",false);
	                formObject.setVisible("cmplx_Decision_IBAN",false);
	                formObject.setVisible("cmplx_Decision_AccountNo",false);
	                formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
	                formObject.setVisible("cmplx_Decision_DebitcardNumber",false);
	            	formObject.setLeft("Decision_Label1",24);
	            	formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED",24);
	            	formObject.setLeft("Decision_Label3",297);
	            	formObject.setLeft("cmplx_Decision_Decision",297);
	            	formObject.setLeft("DecisionHistory_Label1",555);
	            	formObject.setLeft("cmplx_Decision_refereason",555);
	            	formObject.setLeft("DecisionHistory_Rejreason",813);
	            	formObject.setLeft("cmplx_Decision_rejreason",813);
	            	formObject.setLeft("DecisionHistory_Button1",1000);
	            	
	            	formObject.setTop("Decision_Label1",8);
	            	formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",24);
	            	formObject.setTop("Decision_Label3",8);
	            	formObject.setTop("cmplx_Decision_Decision",24);
	            	formObject.setTop("DecisionHistory_Label1",8);
	            	formObject.setTop("cmplx_Decision_refereason",24);
	            	formObject.setTop("DecisionHistory_Rejreason",8);
	            	formObject.setTop("cmplx_Decision_rejreason",24);
	            	formObject.setTop("DecisionHistory_Button1",8);                                                                                                	
	            	formObject.setTop("DecisionHistory_Label5",56);
	            	formObject.setTop("cmplx_Decision_desc",72);
	            	formObject.setTop("DecisionHistory_Label3",56);
	            	formObject.setTop("cmplx_Decision_strength",72);
	            	formObject.setTop("DecisionHistory_Label4",56);
	            	formObject.setTop("cmplx_Decision_weakness",72);
	            	formObject.setTop("Decision_Label4",56);
	            	formObject.setTop("cmplx_Decision_REMARKS",72);                                                                                                	
	            	formObject.setTop("Decision_ListView1",170);
	            	formObject.setTop("DecisionHistory_save",360);//Arun (11/09/17)
	            	 if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")) 
		                {
		                	formObject.setVisible("DecisionHistory_Button2",true);
		                	formObject.setLeft("DecisionHistory_Button2",1000);
		                	formObject.setVisible("DecisionHistory_Label6",true);
		                	formObject.setVisible("cmplx_Decision_IBAN",true);
		                	formObject.setVisible("DecisionHistory_Label10",true);
		                	formObject.setVisible("cmplx_Decision_New_CIFNo",true);
		                	formObject.setVisible("DecisionHistory_Label7",true);
		                	formObject.setVisible("cmplx_Decision_AccountNo",true);
		                	formObject.setTop("DecisionHistory_Label6",56);
			            	formObject.setTop("cmplx_Decision_IBAN",72);
			            	formObject.setTop("DecisionHistory_Label10",56);
			            	formObject.setTop("cmplx_Decision_New_CIFNo",72);
			            	formObject.setTop("DecisionHistory_Label7",56);
			            	formObject.setTop("cmplx_Decision_AccountNo",72);
		                	formObject.setTop("DecisionHistory_Label5",104);
			            	formObject.setTop("cmplx_Decision_desc",120);
			            	formObject.setTop("DecisionHistory_Label3",104);
			            	formObject.setTop("cmplx_Decision_strength",120);
			            	formObject.setTop("DecisionHistory_Label4",104);
			            	formObject.setTop("cmplx_Decision_weakness",120);
			            	formObject.setTop("Decision_Label4",104);
			            	formObject.setTop("cmplx_Decision_REMARKS",120); 
			            	formObject.setTop("Decision_ListView1",220);
			            	formObject.setTop("DecisionHistory_save",410);//Arun (11/09/17)
		                }
	            }             
                                                                                
                if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")){
                	formObject.setLocked("WorldCheck1_age",true);
                // Changes done to load master values in world check birthcountry and residence country
                	loadPicklist_WorldCheck();
                }
                //            started merged code
                if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")){
                                                
                	try{
                		String query="select AcctType,'',AcctId,AcctNm,AccountOpenDate,AcctStat,'',AvailableBalance,'','','' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
                		List<List<String>> list_acc=formObject.getDataFromDataSource(query);
                		for(List<String> mylist : list_acc)
                		{
                			PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
                			formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
                		}
                
                		query="select '',LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
                		List<List<String>> list_lien=formObject.getDataFromDataSource(query);
					for(List<String> mylist : list_lien)
					 {
			                PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
			                formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
					 }
						query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
					for(List<String> mylist : list_SIDet)
					{
                            PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
                            formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
					}
                                                
                }
                                                
                	catch(Exception e){
                		PL_SKLogger.writeLog("PL DDVT Maker", "Exception while setting data in grid:"+e.getMessage());
                	}
                }              
                                
                break;
                                                                                  
				case MOUSE_CLICKED:
                    PL_SKLogger.writeLog(" In PL DDVT MAKER MOUSE_CLICKED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
                    if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
                                    //GenerateXML();
                    }
                  
                   
                    //new code added
                    //Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)
                    
                    if(pEvent.getSource().getName().equalsIgnoreCase("FetchDetails"))
                    {
                                                                                                                
                    	String ResideSince="";
                    	//String Gender="";
                                                                                                                
	                     HashMap<String,String> hm1= new HashMap<String,String>(); // not nullable HashMap
	                     //if(formObject.getNGValue("cmplx_Customer_CardNotAvailable")=="true")                                    
	                     hm.put("FetchDetails","Clicked");
	                     popupFlag="Y";
	                     formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
	                   
	                     //Deepak Code change for Entity Details
	                     //if("Is_EID_Genuine".equalsIgnoreCase("Y") && "cmplx_Customer_EmiratesID" != ""){
	                     String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
	                     PL_SKLogger.writeLog("RLOS_Initiation ", "EID value for Entity Details: "+EID );
	                   
	                     if(formObject.getNGValue("cmplx_Customer_card_id_available")=="false"){
	                         PL_SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility inside checkbox false"); 
	                         PL_SKLogger.writeLog("RLOS value of Customer Details ----1234567",formObject.getNGValue("cmplx_Customer_card_id_available"));
	                 outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","");
	                 PL_SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
	                     ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                     PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                     
	                      if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
	                                   popupFlag="Y";
	                                  valueSetCustomer(outputResponse);    
	                         formObject.setNGValue("Is_Customer_Eligibility","Y");
	                         parse_cif_eligibility(outputResponse);
	                         String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
	                         //Customer_enable();
	                        // if(NTB_flag.equalsIgnoreCase("true")){
	                                //  Customer_enable();
	                         //}
	                         if(NTB_flag.equalsIgnoreCase("true")){
	                        	 setcustomer_enable();
	                        	 try
	                        	 {
	                        		 PL_SKLogger.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
	                        		 throw new ValidatorException(new FacesMessage("Customer is a New to Bank Customer."));
                                                                                                                
	                        	 }
	                        	 finally{hm.clear();}
	                         }
	                             else{
	                                      outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
	                                 try
	                                 {
	                                     String Date1=formObject.getNGValue("cmplx_Customer_DOb");
	                                     SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
	                                     SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
	                                     String Datechanged=sdf2.format(sdf1.parse(Date1));
	                                     PL_SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
	                                     formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
	                                 }        
	                                 catch(Exception ex){                            
	                                 }
	
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode","Inside Customer");
	                                 Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
	                                 //PL_SKLogger.writeLog("RLOS value of Gender",Gender);
	                                 String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of Marital_Status",Marital_Status);
	                                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                 ResideSince =  (outputResponse.contains("<ResideSince>")) ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
	                                 PL_SKLogger.writeLog("RLOS value of ResideSince",ResideSince);
                                                            
                                          

                                     if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
                                         formObject.setNGValue("Is_Customer_Details","Y");
                                         formObject.RaiseEvent("WFSave");
                                         PL_SKLogger.writeLog("RLOS value of Is_Customer_Details",formObject.getNGValue("Is_Customer_Details"));
                                        
                         
                                          formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
                                        formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
                                                                                                                        formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
                                        formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
                                     formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
                                    if(formObject.isVisible("Supplementary_Container")){
                                                    formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails")+25);
                                                    formObject.setTop("FATCA",formObject.getTop("Supplementary_Container")+25);
                                                                    formObject.setTop("KYC", formObject.getTop("FATCA")+25);
                                                                    formObject.setTop("OECD", formObject.getTop("KYC")+25);
                                                    //Java takes height of entire container in getHeight while js takes current height         i.e collapsed/expanded
                                                    }
                                    else{
                                                    formObject.setTop("FATCA",formObject.getTop("CardDetails")+25);
                                                    formObject.setTop("KYC", formObject.getTop("FATCA")+25);
                                                    formObject.setTop("OECD", formObject.getTop("KYC")+25);
                                    }
	                                  valueSetCustomer(outputResponse);
	                                 //code to set Emirates of residence start.
	                                  int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
	                                  if(n>0){
	                                	  for(int i=0;i<n;i++){
	                                		  PL_SKLogger.writeLog("selecting Emirates of residence: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
	                                		  if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("RESIDENCE")){
	                                			  PL_SKLogger.writeLog("selecting Emirates of residence: settign value: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
	                                              formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
	                                		  }
	                                	  }
                                                                  //code to set Emirates of residence End.
                                                                  
                                                                throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));
                                                             }
                                                                //code change to save the date in desired format by AMAN        
                                                                try{
                                                                        PL_SKLogger.writeLog("converting date entered",formObject.getNGValue("cmplx_Customer_DOb")+"");
                                                                        PL_SKLogger.writeLog("converting date enteredID",formObject.getNGValue("cmplx_Customer_IdIssueDate")+"");
                                                                        PL_SKLogger.writeLog("converting date enteredPASS",formObject.getNGValue("cmplx_Customer_PassPortExpiry")+"");
                                                                        PL_SKLogger.writeLog("converting date enteredVISA",formObject.getNGValue("cmplx_Customer_VIsaExpiry")+"");

                                                                        String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
                                                                        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
                                                                        SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");

                                                                        if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
                                                                                        String n_str_dob=sdf2.format(sdf1.parse(str_dob));

                                                                                        PL_SKLogger.writeLog("converting date entered",n_str_dob+"asd");
                                                                                        formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);

                                                                        }

                                                        }
                                                        catch(Exception e){
                                                                        PL_SKLogger.writeLog("Exception Occur while converting date",e+"");

                                                        }              
                                     }
                                        else{
                                        	formObject.setNGValue("Is_Customer_Details","N");

                                            alert_msg = "Error in fetch Customer details operation";
                                            PL_SKLogger.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
                            }

                            formObject.RaiseEvent("WFSave");
                                                                                                                                                }
                                                         if(formObject.getNGValue("Is_Customer_Eligibility").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Customer_Details").equalsIgnoreCase("Y"))
                                                         { 
                                                             PL_SKLogger.writeLog("RLOS value of Customer Details","inside if condition");
                                                             formObject.setEnabled("FetchDetails", false); 
                                                             popupFlag = "Y";
                                                          
                                                             throw new ValidatorException(new FacesMessage("Customer information fetched sucessfully"));
                                                         }
                                                         else{
                                                             formObject.setEnabled("FetchDetails", true);
                                                         }
                                                         PL_SKLogger.writeLog("RLOS value of Customer Details ----1234","");
                                                         //formObject.RaiseEvent("WFSave");
                                                     try
                                                     {
                                                        // throw new ValidatorException(new FacesMessage(alert_msg));
                                                        // throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
                                                     }
                                                     finally{hm.clear();}
                                       
                                                      }
                                                      else{
                                                             formObject.setNGValue("Is_Customer_Eligibility","N");
                                                             popupFlag="Y";
                                                            // alert_msg = "Dedupe check failed.";
                                                             PL_SKLogger.writeLog("Dedupe check failed.","");
                                                           //  throw new ValidatorException(new FacesMessage("Dedupe check failed."));
                                                             try{
                                                           //  throw new ValidatorException(new FacesMessage(alert_msg));
                                                             }
                                                             finally{hm.clear();}
                                                         }
                                                         //added
                                                      }
                                                      //ended
                                                         
                                                     //added
                                                     else if(formObject.getNGValue("cmplx_Customer_card_id_available")=="true"){
                                                       PL_SKLogger.writeLog("RLOS value of Customer Details ----1234567",formObject.getNGValue("cmplx_Customer_card_id_available"));
                                                       PL_SKLogger.writeLog("RLOS value of Customer Details ----1234567","inside true");
                                                       outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
                                              /*         try
                                                       {
                                                                       String Date1=formObject.getNGValue("cmplx_Customer_DOb");
                                                                       SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
                                                                       SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
                                                                       String Datechanged=sdf2.format(sdf1.parse(Date1));
                                                                       PL_SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
                                                                       formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
                                                       }        
                                                        catch(Exception ex){                            
                                                        }
*/  //Code commented because here it is of no use
                                                       PL_SKLogger.writeLog("RLOS value of ReturnCode","Inside Customer");
                                                       ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                       PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                                                       
                                                        ResideSince =  (outputResponse.contains("<ResideSince>")) ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
                                                        PL_SKLogger.writeLog("RLOS value of ResideSince",ResideSince);
                                                                                                                               
                                                        if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
                                                                                                                                                
                                                                formObject.setNGValue("Is_Customer_Details","Y");
                                                                 formObject.RaiseEvent("WFSave");
                                                                PL_SKLogger.writeLog("RLOS value of CurrentDate Is_Customer_Details",formObject.getNGValue("Is_Customer_Details"));
                                                    

                                                                  formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
                                                                formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
                                                                                                                                formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
                                                                formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
                                                       formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
                                                                                                                                formObject.setEnabled("Customer_Button1", true);      
                                                                                                                                PL_SKLogger.writeLog("year difference:","diffdays difference after button is enabled");
                                                       if(formObject.isVisible("Supplementary_Container")){
                                                        formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails")+25);
                                                        formObject.setTop("FATCA",formObject.getTop("Supplementary_Container")+25);
                                                                        formObject.setTop("KYC", formObject.getTop("FATCA")+25);
                                                                        formObject.setTop("OECD", formObject.getTop("KYC")+25);
                                                        //Java takes height of entire container in getHeight while js takes current height  i.e collapsed/expanded
                                                        }
                                        else{
                                                        formObject.setTop("FATCA",formObject.getTop("CardDetails")+25);
                                                        formObject.setTop("KYC", formObject.getTop("FATCA")+25);
                                                        formObject.setTop("OECD", formObject.getTop("KYC")+25);
                                        }
                                                                  valueSetCustomer(outputResponse);
                                                             //code added here to change the DOB in DD/MM/YYYY format     
                                                                  try
                                                                  {
                                                                                  String Date1=formObject.getNGValue("cmplx_Customer_DOb");
                                                                                  SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
                                                                                  SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
                                                                                  String Datechanged=sdf2.format(sdf1.parse(Date1));
                                                                                  PL_SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
                                                                                  formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
                                                                  }        
                                                                   catch(Exception ex){                            
                                                                   }
                                                         //         code added here to change the DOB in DD/MM/YYYY format    
                                                                 //code to set Emirates of residence start.
                                                                  int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
                                                                  if(n>0){
                                                                                                                for(int i=0;i<n;i++){
                                                                                                                                 PL_SKLogger.writeLog("selecting Emirates of residence: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
                                                                                                                                if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("RESIDENCE")){
                                                                                                                                                PL_SKLogger.writeLog("selecting Emirates of residence: settign value: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
                                                                                                                                                formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
                                                                                                                                }
                                                                                                                }
                                                                  //code to set Emirates of residence End.
                                                                                                                formObject.setEnabled("FetchDetails", false); 
                                                                 throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));
                                                                  
                                                       }
                                                                //code change to save the date in desired format by AMAN        
                                                                try{
                                                                                PL_SKLogger.writeLog("converting date entered",formObject.getNGValue("cmplx_Customer_DOb")+"");
                                                                                PL_SKLogger.writeLog("converting date enteredID",formObject.getNGValue("cmplx_Customer_IdIssueDate")+"");
                                                                                PL_SKLogger.writeLog("converting date enteredPASS",formObject.getNGValue("cmplx_Customer_PassPortExpiry")+"");
                                                                                PL_SKLogger.writeLog("converting date enteredVISA",formObject.getNGValue("cmplx_Customer_VIsaExpiry")+"");

                                                                                String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
                                                                                PL_SKLogger.writeLog("converting date entered",str_dob+"");
                                                                                                                String str_IDissuedate=formObject.getNGValue("cmplx_Customer_IdIssueDate");
                                                                                                                String str_passExpDate=formObject.getNGValue("cmplx_Customer_PassPortExpiry");
                                                                                                                String str_visaExpDate=formObject.getNGValue("cmplx_Customer_VIsaExpiry");
                                                                                                                SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
                                                                                                                                                                SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
                                                                                                                                                                String n_str_dob=sdf2.format(sdf1.parse(str_dob));
                                                                                                                                                                PL_SKLogger.writeLog("converting date entered",n_str_dob+"");
                                                                                                                                                                
                                                                                                                                                                PL_SKLogger.writeLog("value ofDatechanged DOB: ",n_str_dob);
                                                                                                                                                                formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
                                                                                                                                                
                                                                                                                                                
                                                                                                                                                 
                                                                }
                                                                catch(Exception e){
                                                                                PL_SKLogger.writeLog("Exception Occur while converting date",e+"");
                                                                }
                                                                                                                                                //code change end to save the date in desired format by AMAN             
                                                      
                                                    
                                                                                                                                                
                                                                                                                                }
                                                                                                                               else{
                                                                                                                                               formObject.setNGValue("Is_Customer_Details","N");
                                                                                                                                                formObject.RaiseEvent("WFSave");
                                                                                                                                                PL_SKLogger.writeLog("RLOS value of Customer Details456",formObject.getNGValue("Is_Customer_Details"));
                                                                                                                               }
                                                                                                                                PL_SKLogger.writeLog("RLOS value of Customer Details789",formObject.getNGValue("Is_Customer_Details"));
                                                                                                               }
                                                     formObject.RaiseEvent("WFSave");
                                                     //ended
                                                 }
                                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Button1"))
                                                                {
                                                                    //if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
                                                                                                                String NegatedFlag="";
                                                                                                                popupFlag="Y";
                                                                                                                outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","");
                                                                                                                PL_SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
                                                                                                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";












































                                                                                                                PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);




















                                                                                                                formObject.setNGValue("Is_Customer_Eligibility","Y");











































































                                                                                                                                




















                                                                                                                if(ReturnCode.equalsIgnoreCase("0000")){
                                                                                                                                valueSetCustomer(outputResponse); 
                                                                                                                                 parse_cif_eligibility(outputResponse);
                                                                                                                                BlacklistFlag= (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
                                                                                                                                PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is BlacklistedFlag"+BlacklistFlag);
                                                                                                                                DuplicationFlag= (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
                                                                                                                                PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is DuplicationFlag"+DuplicationFlag);
                                                                                                                                NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
                                                                                                                                PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is NegatedFlag"+NegatedFlag);
                                                                        if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
                                                                            valueSetCustomer(outputResponse);    
                                                                                                                                 formObject.setNGValue("Is_Customer_Eligibility","Y");
                                                                                                                                formObject.RaiseEvent("WFSave");
                                                                                                                                PL_SKLogger.writeLog("RLOS value of ReturnDesc Is_Customer_Eligibility",formObject.getNGValue("Is_Customer_Eligibility"));
                                                                                                                                formObject.setNGValue("BlacklistFlag",BlacklistFlag);
                                                                                                                                formObject.setNGValue("DuplicationFlag",DuplicationFlag);
                                                                                                                                formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
                                                                                                                                String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
                                                                                                                                if(NTB_flag.equalsIgnoreCase("true")){
                                                                                                                                                                PL_SKLogger.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
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
                                                                                                                PL_SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Eligibility"));
                                                                                                                PL_SKLogger.writeLog("RLOS value of BlacklistFlag",formObject.getNGValue("BlacklistFlag"));
                                                                                                                PL_SKLogger.writeLog("RLOS value of DuplicationFlag",formObject.getNGValue("DuplicationFlag"));
                                                                                                                PL_SKLogger.writeLog("RLOS value of IsAcctCustFlag",formObject.getNGValue("IsAcctCustFlag"));
                                                                                                                formObject.RaiseEvent("WFSave");
                                                                                                                throw new ValidatorException(new FacesMessage(alert_msg));
                                                                                                                //}
                                                                                                }

                                                                }
                                                               //Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)
                                                                                                

                if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button2")) {

                                  PL_SKLogger.writeLog("","inside create cif button");
                                  outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
                                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                                PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                                String NewAcid= (outputResponse.contains("<NewAcid>")) ? outputResponse.substring(outputResponse.indexOf("<NewAcid>")+"</NewAcid>".length()-1,outputResponse.indexOf("</NewAcid>")):"";
                                                String IBANNumber= (outputResponse.contains("<IBANNumber>")) ? outputResponse.substring(outputResponse.indexOf("<IBANNumber>")+"</IBANNumber>".length()-1,outputResponse.indexOf("</IBANNumber>")):"";
                                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                                                valueSetCustomer(outputResponse);    
                                                                PL_SKLogger.writeLog("RLOS value of Account Request","after valuesetcudtomer");
                                                                formObject.setNGValue("Is_Account_Create","Y");
                                                                formObject.setNGValue("EligibilityStatus","Y");
                                                                formObject.setNGValue("EligibilityStatusCode","Y");
                                                                formObject.setNGValue("EligibilityStatusDesc","Y");
                                                                formObject.setNGValue("Account_Number",NewAcid);
                                                                formObject.setNGValue("IBAN_Number",IBANNumber);
                                                                //formObject.setNGValue("cmplx_Decision_IBAN",IBANNumber);
                                                                //formObject.setNGValue("cmplx_Decision_AccountNo",NewAcid);
                                                                PL_SKLogger.writeLog("RLOS value of Account Request NewAcid",formObject.getNGValue("Account_Number"));
                                                                PL_SKLogger.writeLog("RLOS value of Account Request IBANNumber",formObject.getNGValue("IBAN_Number"));
                                                                PL_SKLogger.writeLog("RLOS value of Account Request cmplx_Decision_IBAN",formObject.getNGValue("cmplx_Decision_IBAN"));
                                                                PL_SKLogger.writeLog("RLOS value of Account Request cmplx_Decision_AccountNo",formObject.getNGValue("cmplx_Decision_AccountNo"));
                                                }
                                                else{
                                                                formObject.setNGValue("Is_Account_Create","N");
                                                }
                                                
                                                PL_SKLogger.writeLog("RLOS value of Account Request",formObject.getNGValue("Is_Account_Create"));
                                                PL_SKLogger.writeLog("RLOS value of EligibilityStatus",formObject.getNGValue("EligibilityStatus"));
                                                PL_SKLogger.writeLog("RLOS value of EligibilityStatusCode",formObject.getNGValue("EligibilityStatusCode"));
                                                PL_SKLogger.writeLog("RLOS value of EligibilityStatusDesc",formObject.getNGValue("EligibilityStatusDesc"));
                                                PL_SKLogger.writeLog("RLOS value of Account Request NewAcid111",formObject.getNGValue("Account_Number"));
                                                PL_SKLogger.writeLog("RLOS value of Account Request IBANNumber111",formObject.getNGValue("IBAN_Number"));
                                                
                                                outputResponse = GenerateXML("NEW_CUSTOMER_REQ","");
                                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
                                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                                PL_SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
                                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                                                valueSetCustomer(outputResponse);    
                                                                PL_SKLogger.writeLog("PL value of ReturnDesc","Inside if of New customer Req");
                                                formObject.setNGValue("Is_Customer_Create","Y");
                                                                
                                                }
                                                else{
                                                                PL_SKLogger.writeLog("PL value of ReturnDesc","Inside else of New Customer Req");
                                                                formObject.setNGValue("Is_Customer_Create","N");
                                                }
                                                formObject.RaiseEvent("WFSave");
                  
                  }

                                                                                                                                
                                if (pEvent.getSource().getName().equalsIgnoreCase("Product_Add")){
                                
                                formObject.setNGValue("Product_wi_name",formObject.getWFWorkitemName());
                                PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Product_wi_name"));
                                formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");
                                
                                formObject.setNGValue("Product_type","Conventional",false);
                                formObject.setNGValue("ReqProd","--Select--",false);
                                formObject.setNGValue("AppType","--Select--",false);
                                formObject.setNGValue("EmpType","--Select--",false);
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
                                formObject.setVisible("Product_Label16",false); 
                                formObject.setVisible("Product_Label18",false);
                                formObject.setVisible("Product_Label21",false); 
                                formObject.setVisible("Product_Label22",false); 
                                formObject.setVisible("Product_Label23",false); 
                                formObject.setVisible("Product_Label24",false);
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
                                                                                                
                                                                                                
                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Modify")){
                                                                                                                
                                                                                                                formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
                                                                                                                formObject.setNGFrameState("Incomedetails", 1);
                                                                                                                formObject.setNGValue("Product_type","Conventional",false);
                                                                                                                formObject.setNGValue("ReqProd","--Select--",false);
                                                                                                                formObject.setNGValue("AppType","--Select--",false);
                                                                                                                formObject.setNGValue("EmpType","--Select--",false);
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
                                                                                                                formObject.setVisible("Product_Label16",false); 
                                                                                                                formObject.setVisible("Product_Label18",false);
                                                                                                                formObject.setVisible("Product_Label21",false); 
                                                                                                                formObject.setVisible("Product_Label22",false); 
                                                                                                                formObject.setVisible("Product_Label23",false); 
                                                                                                                formObject.setVisible("Product_Label24",false);
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
                                                                                                
                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Delete")){
                                                                                                                formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");
                                                                                                                
                                                                                                                formObject.setNGValue("Product_type","Conventional",false);
                                                                                                                formObject.setNGValue("ReqProd","--Select--",false);
                                                                                                                formObject.setNGValue("AppType","--Select--",false);
                                                                                                                formObject.setNGValue("EmpType","--Select--",false);
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
                                                                                                                formObject.setVisible("Product_Label16",false); 
                                                                                                                formObject.setVisible("Product_Label18",false);
                                                                                                                formObject.setVisible("Product_Label21",false); 
                                                                                                                formObject.setVisible("Product_Label22",false); 
                                                                                                                formObject.setVisible("Product_Label23",false); 
                                                                                                                formObject.setVisible("Product_Label24",false);
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
                                                                                                
                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Search")){
                                                                                                                //GenerateXML();
                                                                                                                PL_SKLogger.writeLog("PL PartMatch_Search", "Inside PartMatch_Search button: ");
                                                                                                                
                                                                                                                //HashMap<String,String> hm1= new HashMap<String,String>(); // not nullable HashMap
                                                                                   
                                                                                                                //hm1.put("PartMatch_Search","Clicked");
                                                                                                                // popupFlag="N";
                                                                                                                formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
                                                                                                                outputResponse = GenerateXML("DEDUP_SUMMARY","");
                                                                                                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                PL_SKLogger.writeLog("PL value of ReturnCode",ReturnCode);
                                                                                                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";          
                                                                                                                PL_SKLogger.writeLog("PL value of ReturnDesc",ReturnDesc);
                                                                                                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                                                                                                                                //valueSetCustomer(outputResponse);
                                                                                                                                //change by saurabh on 11th July 17.
                                                                                                                                parseDedupe_summary(outputResponse);
                                                                                                                                formObject.setNGValue("Is_PartMatchSearch","Y");
                                                                                                                                PL_SKLogger.writeLog("PL value of Part Match Request","inside if of partmatch");
                                                                                                                                alert_msg= "Part match sucessfull";
                                                                                                                                }
                                                                                                                                else{
                                                                                                                                                formObject.setNGValue("Is_PartMatchSearch","N");
                                                                                                                                                PL_SKLogger.writeLog("PL value of Part Match Request","inside else of partmatch");
                                                                                                                                                alert_msg= "Error while performing Part match";
                                                                                                                                }
                                                                                                                                formObject.RaiseEvent("WFSave");
                                                                                                                PL_SKLogger.writeLog("PL value of Part Match Request",formObject.getNGValue("Is_PartMatchSearch"));
                                                                                                                if((formObject.getNGValue("Is_PartMatchSearch").equalsIgnoreCase("Y")))
                                                                                                                { 
                                                                                                                                PL_SKLogger.writeLog("PL value of Is_CUSTOMER_UPDATE_REQ","inside if condition of disabling the button");
                                                                                                                                //formObject.setEnabled("PartMatch_Search", false);
                                                                                                                                buttonClickFlag="PartMatch_Search";

                                                                                                                }
                                                                                                                else{
                                                                                                                                formObject.setEnabled("PartMatch_Search", true);
                                                                                                                //            throw new ValidatorException(new CustomExceptionHandler("Dedupe Summary Fail!!","PartMatch_Search#Dedupe Summary Fail!!","",hm));
                                                                                                                }
                                                                                                                popupFlag = "Y";
                           
                            throw new ValidatorException(new FacesMessage(alert_msg));
                                                                                                }


                                                                                                //for BlackList Call added on 3rd May 2017
                              //Changes done by aman to correctly save the value in the grid
                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Blacklist")){
                                                                    PL_SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111");
                                                               //     formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
                                                                    String CifID="";
                                                                    String CustomerStatus="";
                                                                    String firstName="";
                                                                    String LastName="";
                                                                    String Negatedflag="";
                                                                    String Negatednote="";
                                                                    String NegatedCode="";
                                                                    String NegatedReason="";
                                                                    String Blacklistflag="";
                                                                    String Blacklistnote="";
                                                                    String BlacklistCode="";
                                                                    String BlacklistReason="";
                                                                    String OldPassportNumber=""; 
                                                                    String PassportNumber="";
                                                                    String Visa="";
                                                                    String EmirateID="";
                                                                    String PhoneNo="";
                                                                    //added here
                                                                    String StatusType="";
                                                                    String Applicant="";
                                                                    //ended here
                                                                    PL_SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111 after initializing strings");
                                                                    outputResponse =GenerateXML("BLACKLIST_DETAILS","");
                                                                    PL_SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match");
                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                    PL_SKLogger.writeLog("PL value of ReturnCode part Match",ReturnCode);
                                                                                                                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                                                                                                    PL_SKLogger.writeLog("PL value of ReturnDesc part Match",ReturnDesc);
                                                                                                                                                    
                                                                                                                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                                                                                                        formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
                                                                                                                        alert_msg="Blacklist check successfull";
                                                                                                                        PL_SKLogger.writeLog("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType",formObject.getNGValue("Is_Customer_Eligibility_Part"));
                                                                                                                        StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
                                                                                                                        PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusType"+StatusType);
                                                                                                                        outputResponse = outputResponse.substring(outputResponse.indexOf("<CustomerBlackListResponse>")+27, outputResponse.indexOf("</CustomerBlackListResponse>"));
                                                                                                                		System.out.println(outputResponse);
                                                                                                                		outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
                                                                                                                		outputResponse = outputResponse+"</Response>";
                                                                                                                		
                                                                                                                		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                                                                                                		DocumentBuilder builder = factory.newDocumentBuilder();
                                                                                                                		InputSource is = new InputSource(new StringReader(outputResponse));

                                                                                                                		Document doc = builder.parse(is);
                                                                                                                		doc.getDocumentElement().normalize();

                                                                                                                		System.out.println("Root element :"+doc.getDocumentElement().getNodeName());

                                                                                                                		NodeList nList = doc.getElementsByTagName("StatusInfo");
                                                                                                                		  

                                                                                                                		for (int temp = 0; temp < nList.getLength(); temp++) {
                                                                                                                			
                                                                                                                			Node nNode = nList.item(temp);
                                                                                                                			

                                                                                                                			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                                                                                                				Element eElement = (Element) nNode;
                                                                                                                				StatusType = eElement.getElementsByTagName("StatusType").item(0).getTextContent() ;
                                                                                                                				System.out.println("PL value of BlacklistFlag_PartCustomer is Blacklisted StatusType"+StatusType);
                                                                                                                				if(StatusType.equalsIgnoreCase("Black List")){
                                                                                                									//	Blacklistflag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
                                                                                                									Blacklistflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
                                                                                                									BlacklistReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
                                                                                                									BlacklistCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;

                                                                                                									System.out.println("PL value of BlacklistFlag_Part Customer is Blacklisted StatusCode"+BlacklistCode);
                                                                                                								}
                                                                                                								if(StatusType.equalsIgnoreCase("Negative List")){
                                                                                                									Negatedflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
                                                                                                									NegatedReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
                                                                                                									NegatedCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;
                                                                                                									}
                                                                                                                			}
                                                                                                                        //added
                                                                                                                        CustomerStatus =  (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
                                                                                                                        CifID=formObject.getNGValue("PartMatch_CIFID");
                                                                                                                        firstName=formObject.getNGValue("PartMatch_fname");
                                                                                                                        LastName=formObject.getNGValue("PartMatch_lname");
                                                                                                                        OldPassportNumber=formObject.getNGValue("PartMatch_oldpass");
                                                                                                                        PassportNumber=formObject.getNGValue("PartMatch_newpass");
                                                                                                                        Visa=formObject.getNGValue("PartMatch_visafno");
                                                                                                                        EmirateID=formObject.getNGValue("PartMatch_EID");
                                                                                                                        PhoneNo=formObject.getNGValue("PartMatch_mno1");
                                                                                                                        PL_SKLogger.writeLog("PL value of CustomerStatus","Customer is Blacklisted StatusType"+CustomerStatus);
                                                                                                                        
                                                                                                                      
                                                                                                                       // Finacle_CRM.add(ExtBlackDate);
                                                                                                                       // Finacle_CRM.add(ExtBlackReason);
                                                                                                                            
                                                                                                                        /*outputResponse = (outputResponse.contains("<StatusInfo>")) ? outputResponse.substring(outputResponse.indexOf("</StatusInfo>"),outputResponse.length()-1):"";
                                                                                                                       // SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is BlacklistedoutputResponse111 outputResponse"+outputResponse);
                                                                                                                        if(outputResponse.contains(StatusType)){
                                                                                                                        //     SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted outputResponse"+outputResponse);
                                                                                                                            StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
                                                                                                                            StatusFlag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
                                                                                                                            //SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusCode");
                                                                                                                            PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is StatusType StatusFlag"+StatusType+","+StatusFlag);
                                                                                                                        }*/
                                                                                                                        
                                                                                                                        //ended
                                                                                                                		}
                                                                                                                    }		
                                                                                                                    else{
                                                                                                                        formObject.setNGValue("Is_Customer_Eligibility_Part","N");    
                                                                                                                        PL_SKLogger.writeLog("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType",formObject.getNGValue("Is_Customer_Eligibility_Part"));
                                                                                                                    }
                                                                                                                  /*  String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
                                                                                                                                formObject.setNGValue("cmplx_Customer_CIFNO",CIFID);
                                                                                                                    String FirstName =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),2);
                                                                                                                                formObject.setNGValue("cmplx_Customer_FIrstNAme",FirstName);
                                                                                                                    String LastName =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),3);
                                                                                                                                formObject.setNGValue("cmplx_Customer_LAstNAme",LastName);
                                                                                                                    String Passport =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);
                                                                                                                                formObject.setNGValue("cmplx_Customer_Passport2",Passport);
                                                                                                                    String VisaNo =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),7);
                                                                                                                                formObject.setNGValue("cmplx_Customer_VisaNo",VisaNo);
                                                                                                                    String Mobile =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),8);
                                                                                                                                formObject.setNGValue("cmplx_Customer_MobNo",Mobile);
                                                                                                                    String DOB =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),10);
                                                                                                                                
                                                                                                                    String Emirates =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),11);
                                                                                                                                formObject.setNGValue("cmplx_Customer_EmiratesID",Emirates);
                                                                                                                    String Nationality =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),13);
                                                                                                                                formObject.setNGValue("cmplx_Customer_Nationality",Nationality);
                                                                                                                    String Driving_License =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),12);
                                                                                                                                formObject.setNGValue("cmplx_Customer_DLNo",Driving_License);
                                                                                                                        
                                                                                                                        
                                                                                                                        try{
                                                                                                                            String Date1=DOB;
                                                                                                                            PL_SKLogger.writeLog("PL value of Date1111",Date1);
                                                                                                                             SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
                                                                                                                             SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yy");
                                                                                                                             String Datechanged=sdf2.format(sdf1.parse(Date1));
                                                                                                                             PL_SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
                                                                                                                             formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
                                                                                                                            }
                                                                                                                            catch(Exception ex){
                                                                                                                                
                                                                                                                            }
                                                                                                                        
                                                                                                                         List<String> Finacle_CRM = new ArrayList<String>(); 
                                                                                                                          Finacle_CRM.add(CIFID);
                                                                                                                          Finacle_CRM.add(Applicant); 
                                                                                                                          Finacle_CRM.add(Passport);
                                                                                                                         Finacle_CRM.add(Negated);
                                                                                                                          Finacle_CRM.add(NegatedDate);
                                                                                                                          Finacle_CRM.add(NegatedReasonCode);
                                                                                                                          Finacle_CRM.add(NegatedReasons);
                                                                                                                          Finacle_CRM.add(StatusFlag);
                                                                                                                          Finacle_CRM.add(BlackListedDate);
                                                                                                                          Finacle_CRM.add(StatusCode);
                                                                                                                          Finacle_CRM.add(Reason);
                                                                                                                          Finacle_CRM.add(Alerts);
                                                                                                                          Finacle_CRM.add(WhiteListCode);
                                                                                                                         // Finacle_CRM.add(ExtBlackDate);
                                                                                                                         // Finacle_CRM.add(ExtBlackReason);
                                                                                                                          PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","after adding in the grid");
                                                                                                                           PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added inFinacle CRM grid: "+ Finacle_CRM.toString());
                                                                                                                         
                                                                                                                          formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid",Finacle_CRM);
                                                                                                                          PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","after adding in the grid11111");       
                                                                                                                          formObject.RaiseEvent("WFSave");    
                                                                                                                    */
                                                                                                                   /* ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                  String CustomerStatus =  (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                    */
                                                                                                                    List<String> BlacklistGrid = new ArrayList<String>(); 
                                                                                                                    BlacklistGrid.add(CifID);
                                                                                                                    BlacklistGrid.add(CustomerStatus); 
                                                                                                                    BlacklistGrid.add(firstName);
                                                                                                                    BlacklistGrid.add(LastName);
                                                                                                                    BlacklistGrid.add(Blacklistflag);
                                                                                                                    BlacklistGrid.add(Blacklistnote);
                                                                                                                    BlacklistGrid.add(BlacklistReason);
                                                                                                                    BlacklistGrid.add(BlacklistCode);
                                                                                                                    BlacklistGrid.add(Negatedflag);
                                                                                                                    BlacklistGrid.add(Negatednote);
                                                                                                                    BlacklistGrid.add(NegatedReason);
                                                                                                                    BlacklistGrid.add(NegatedCode);
                                                                                                                    BlacklistGrid.add(PassportNumber);
                                                                                                                    BlacklistGrid.add(OldPassportNumber);
                                                                                                                    BlacklistGrid.add(EmirateID);
                                                                                                                    BlacklistGrid.add(Visa);
                                                                                                                    BlacklistGrid.add(PhoneNo);
                                                                                                                    PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","after adding in the grid");
                                                                                                                    PL_SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added inFinacle CRM grid: "+ BlacklistGrid.toString());
                                                                                                                  
                                                                                                                   formObject.addItemFromList("cmplx_PartMatch_cmplx_PartBlacklistGrid",BlacklistGrid);
                                                                                                                   PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","after adding in the grid11111");       
                                                                                                                   formObject.RaiseEvent("WFSave");  
                                                                                                                   
                                                                                                                   throw new ValidatorException(new FacesMessage(alert_msg));
                                                                                      
                                                                                                                }
                              //Changes done by aman to correctly save the value in the grid
                                                                                                //for BlackList Call added on 3rd May 2017
              /*  if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Blacklist")){
                    SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111");
                    formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
                    String StatusType="";
                    String StatusFlag="";
                    String Reason="";
                    String StatusCode="";
                    String Negated="";
                    String NegatedDate="";
                    String NegatedReasonCode="";
                    String NegatedReasons="";
                    String BlackListedDate="";
                    String Alerts="";
                    String ExtBlacklist="";
                    String ExtBlackDate="";
                    String ExtBlackReason="";
                    SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111 after initializing strings");
                    outputResponse =GenerateXML("BLACKLIST_DETAILS","");
                    SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match");
                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                    SKLogger.writeLog("PL value of ReturnCode part Match",ReturnCode);
                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                    SKLogger.writeLog("PL value of ReturnDesc part Match",ReturnDesc);
                                                                                                                            
                    if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
                        formObject.setNGValue("Is_Customer_Eligibility_Part","Y");    
                        StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
                        SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusType"+StatusType);
                        if(StatusType.equalsIgnoreCase("Black List")){
                            StatusFlag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
                            SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusFlag"+StatusFlag);
                            Reason= (outputResponse.contains("<StatusReason>")) ? outputResponse.substring(outputResponse.indexOf("<StatusReason>")+"</StatusReason>".length()-1,outputResponse.indexOf("</StatusReason>")):"";
                            SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted Reason"+Reason);
                            StatusCode= (outputResponse.contains("<StatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<StatusCode>")+"</StatusCode>".length()-1,outputResponse.indexOf("</StatusCode>")):"";
                            SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusCode"+StatusCode);
                        }
                        
                    }
                    else{
                        formObject.setNGValue("Is_Customer_Eligibility_Part","N");    
                    }
                                String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
                                            formObject.setNGValue("cmplx_Customer_CIFNO",CIFID);
                                String FirstName =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),2);
                                            formObject.setNGValue("cmplx_Customer_FIrstNAme",FirstName);
                                String LastName =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),3);
                                            formObject.setNGValue("cmplx_Customer_LAstNAme",LastName);
                                String Passport =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);
                                            formObject.setNGValue("cmplx_Customer_Passport2",Passport);
                                String VisaNo =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),7);
                                            formObject.setNGValue("cmplx_Customer_VisaNo",VisaNo);
                                String Mobile =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),8);
                                            formObject.setNGValue("cmplx_Customer_MobNo",Mobile);
                                String DOB =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),10);
                                            
                                String Emirates =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),11);
                                            formObject.setNGValue("cmplx_Customer_EmiratesID",Emirates);
                                String Nationality =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),13);
                                            formObject.setNGValue("cmplx_Customer_Nationality",Nationality);
                                String Driving_License =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),12);
                                            formObject.setNGValue("cmplx_Customer_DLNo",Driving_License);
                        
                        
                        try{
                            String Date1=DOB;
                            SKLogger.writeLog("PL value of Date1111",Date1);
                             SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
                             SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yy");
                             String Datechanged=sdf2.format(sdf1.parse(Date1));
                             SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
                             formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
                            }
                            catch(Exception ex){
                                
                            }
                        
                         List<String> Finacle_CRM = new ArrayList<String>(); 
                          Finacle_CRM.add(CIFID);
                          Finacle_CRM.add(Passport);
                          Finacle_CRM.add(Negated);
                          Finacle_CRM.add(NegatedDate);
                          Finacle_CRM.add(NegatedReasonCode);
                          Finacle_CRM.add(NegatedReasons);
                          Finacle_CRM.add(StatusFlag);
                          Finacle_CRM.add(BlackListedDate);
                          Finacle_CRM.add(StatusCode);
                          Finacle_CRM.add(Reason);
                          Finacle_CRM.add(Alerts);
                          Finacle_CRM.add(ExtBlacklist);
                          Finacle_CRM.add(ExtBlackDate);
                          Finacle_CRM.add(ExtBlackReason);
                          SKLogger.writeLog("PL value of BlacklistFlag_Part","after adding in the grid");
                           SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added inFinacle CRM grid: "+ Finacle_CRM.toString());
                         
                          formObject.addItem("cmplx_FinacleCRMCustInfo_FincustGrid",Finacle_CRM);
                                      
                          formObject.RaiseEvent("WFSave");    
                                                                                                                            
                                        }*/
                    //ended here for BlackList Call


                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button1")){
                                                    formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
                                                    formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
                                                    formObject.setTop("External_Blacklist",formObject.getTop("FinacleCRM_CustInfo")+formObject.getHeight("FinacleCRM_CustInfo")+20);
                                                    formObject.setTop("Finacle_Core",formObject.getTop("External_Blacklist")+30);
                                                    formObject.setTop("MOL",formObject.getTop("Finacle_Core")+30);
                                                    formObject.setTop("World_Check",formObject.getTop("MOL")+30);
                                                    formObject.setTop("Sal_Enq",formObject.getTop("World_Check")+30);
                                                    formObject.setTop("Reject_Enq",formObject.getTop("Sal_Enq")+30);

                                                    String BlacklistFlag_Part = "";













                                                    String BlacklistFlag_reason = "";
                                                    String BlacklistFlag_code = "";
                                                    String NegativeListFlag = "";
                                                    //String ReturnDesc = "";
                                                    outputResponse =GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
                                                    PL_SKLogger.writeLog("PL value of ReturnDesc","Customer Details part Match");
                                                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

                                                    BlacklistFlag_Part =  (outputResponse.contains("<BlackListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"NA";



                                                    BlacklistFlag_reason =  (outputResponse.contains("<BlackListReason>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";


























































                                                    BlacklistFlag_code =  (outputResponse.contains("<BlackListReasonCode>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";
























                                                    NegativeListFlag =  (outputResponse.contains("<NegativeListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
                                                    PL_SKLogger.writeLog("PL value of ReturnCode part Match",ReturnCode);
                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                    //PL_SKLogger.writeLog("PL value of ReturnDesc part Match",ReturnDesc);
                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                    
                        BlacklistFlag_Part =  (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";                                  
                        formObject.setNGValue("Is_Customer_Details_Part","Y");    
                        formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
                        if(BlacklistFlag_Part.equalsIgnoreCase("Y"))
                        {
                            PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted");    
                        }
                        else
                            PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is not Blacklisted");    
                    }
                    else{
                        formObject.setNGValue("Is_Customer_Details_Part","N");
                    }
                    try{
                        PL_SKLogger.writeLog("CC value of BlacklistFlag_Part flag inside try"+BlacklistFlag_Part,"");    
                        List<String> list_custinfo = new ArrayList<String>();
                        String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
                        String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);

                        list_custinfo.add(CIFID);  // cif id from partmatch
                        list_custinfo.add("Individual");
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
                        list_custinfo.add(formObject.getWFWorkitemName());
                        
                        PL_SKLogger.writeLog("CC DDVT Maker", "list_custinfo list values"+list_custinfo);
                        formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
                  }catch(Exception e){
                        PL_SKLogger.writeLog("PL DDVT Maker", "Exception while setting data in grid:"+e.getMessage());
                        popupFlag = "Y";
                        alert_msg="Error while setting data in finacle customer info grid";
                        throw new ValidatorException(new FacesMessage(alert_msg));
                  }

                     formObject.RaiseEvent("WFSave");          
                }
                //changes done as said by Deepak Sir To call Customer_Details call ended
                
                
                                                                                 else if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Button2")){

                                                                                                                                                outputResponse = GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
                                                                                                                                                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                                                                                                                                                PL_SKLogger.writeLog("PL value of ReturnCode",ReturnCode);
                                                                                                                                                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                                                                                                                                                PL_SKLogger.writeLog("PL value of ReturnDesc",ReturnDesc);
                                                                                                                                                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                                                                                                                                                                formObject.setNGValue("Is_Customer_Details","Y");
                                                                                                                                                                PL_SKLogger.writeLog("PL value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
                                                                                                                                                                valueSetCustomer(outputResponse);    
                                                                                                                                                                PL_SKLogger.writeLog("PL value of Customer Details","Guarantor_CIF is generated");
                                                                                                                                                                PL_SKLogger.writeLog("PL value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
                                                                                                                                                }
                                                                                                                                                else{
                                                                                                                                                                PL_SKLogger.writeLog("Customer Details","Customer Details is not generated");
                                                                                                                                                                formObject.setNGValue("Is_Customer_Details","N");
                                                                                                                                                }
                                                                                                                                                PL_SKLogger.writeLog("PL value of Guarantor_CIF",formObject.getNGValue("Is_Customer_Details"));
                                                                                                                                                formObject.RaiseEvent("WFSave");
                                                                                                                                }
                                                                                                
                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
                                                                                                                formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
                                                                                                                PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
                                                                                                                formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
                                                                                                }
                                                                                                
                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
                                                                                                                formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
                                                                                                }
                                                                                                
                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
                                                                                                                formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

                                                                                                }

                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
                                                                                                                PL_SKLogger.writeLog("PL DDVT MAKER", "Inside Customer_save button: ");
                                                                                                                formObject.saveFragment("CustomerDetails");
                                                                                                                throw new ValidatorException(new FacesMessage("Customer Save Successful"));
                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
                                                                                                                formObject.saveFragment("ProductContainer");
                                                                                                                throw new ValidatorException(new FacesMessage("Product Save Successful"));

                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
                                                                                                                formObject.saveFragment("GuarantorDetails");
                                                                                                                throw new ValidatorException(new FacesMessage("Income Save Successful"));


                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
                                                                                                                formObject.saveFragment("IncomeDEtails");

                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
                                                                                                                formObject.saveFragment("Incomedetails");
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
                                                                                                
                                                                                else        if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Save")){
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
                                                                                                
                                                                                else        if(pEvent.getSource().getName().equalsIgnoreCase("MOL1_Save")){
                                                                                                                formObject.saveFragment("MOL");
                                                                                                }
                                                                                                
                                                                                else                if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Save")){
                                                                                                                formObject.saveFragment("World_Check");
                                                                                                }
                                                                                                
                                                                                else        if(pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq_Save")){
                                                                                                                formObject.saveFragment("Sal_Enq");
                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("RejectEnq_Save")){
                                                                                                                formObject.saveFragment("Reject_Enq");
                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
                                                                                                                formObject.saveFragment("Address_Details_container");
                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
                                                                                                                formObject.saveFragment("Alt_Contact_container");
                                                                                                }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_save")){
                                                                                                                formObject.saveFragment("ReferenceDetails");
                                                                                    }
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
                                                                                                                formObject.saveFragment("Supplementary_Container");
                                                                                                }
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetails_Save")){
                                                                                                					formObject.saveFragment("LoanDetails");
                                                                                                					popupFlag = "Y";
                                                                                                					alert_msg="Loan Details Saved";

                                                                                                					throw new ValidatorException(new FacesMessage(alert_msg));
                                                                                                				}
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetaisDisburs_Save")){
                                                                                					formObject.saveFragment("LoanDetails");
                                                                                					popupFlag = "Y";
                                                                                					alert_msg="Disbursal Details Saved";

                                                                                					throw new ValidatorException(new FacesMessage(alert_msg));
                                                                                				}
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetails_Button3")){
                                                                                                	formObject.setNGValue("LoanDetails_winame",formObject.getWFWorkitemName());
                                                                                                    PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("LoanDetails_winame"));                                                                                				
                                                                                                	formObject.ExecuteExternalCommand("NGAddRow", "cmplx_LoanDetails_cmplx_LoanGrid");
                                                                                                }                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetails_Button4")){
                                                                                    				
                                                                                                	formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_LoanDetails_cmplx_LoanGrid");
                                                                                                }
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetails_Button5")){
                                                                                    				
                                                                                                	formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_LoanDetails_cmplx_LoanGrid");
                                                                                                }
                                
                                if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
                					String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
                					PL_SKLogger.writeLog("RLOS", "Inside FATCA_Button1 "+text);
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
                					 PL_SKLogger.writeLog("RLOS", "Inside FATCA_Button2 ");
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
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
                                                                                                                //formObject.saveFragment("DecisionHistoryContainer");
                				saveIndecisionGrid();//Arun (23/09/17)
                                                                                                                formObject.saveFragment("DecisionHistory");
                                popupFlag = "Y";//Arun (23/09/17)//Arun (23/09/17)
                    			alert_msg="Decision History Details Saved";
                    			throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
                                                                                                }
                                                                                                    
                                                                                                else  if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
																        							formObject.saveFragment("Notepad_Values");
					  popupFlag = "Y";//Arun (23/09/17)
                      alert_msg="Notepad Details Saved";//Arun (23/09/17)
                      throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
																        						}
        						
                                                                                                
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button4"))
                                                                                                {
                                                                                                                formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
                                                                                                }
                                                                                                
                                                                                                 else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button5"))
                                                                                                {
                                                                                                                
                                                                                                                formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
                                                                                                }
                                                                                                
                                                                                                 else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button6"))
                                                                                                {
                                                                                                                
                                                                                                                formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
                                                                                                }
                                                                                                //++ below code changed by abhishek on 25th oct 2017 to change button id
                                                                                                else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Add"))
                                                                                                {
                                                                                                                formObject.setNGValue("WorldCheck1_winame",formObject.getWFWorkitemName());
                                                                                                                formObject.setNGValue("Is_WorldCheckAdd","Y");
                                                                                                                formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
                                                                                                }
                				 																//-- Above code changed by abhishek on 25th oct 2017 to change button id
                                                                                                 else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button2"))
                                                                                                {
                                                                                                                
                                                                                                                formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
                                                                                                }
                                                                                                
                                                                                                 else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button3"))
                                                                                                {
                                                                                                                formObject.setNGValue("Is_World_Check_Add","N");
                                                                                                                formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
                                                                                                }
                                                                                                
                                                                                                 else if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_VERIFICATIONREQUIRED"))
                                                                                                  {
                                                                                                                PL_SKLogger.writeLog("CC val cmplx_Decision_VERIFICATIONREQUIRED ", "Value of cmplx_Decision_VERIFICATIONREQUIRED is:"+formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED"));
                                                                                                                if(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED").equalsIgnoreCase("Yes") )
                                                                                                                {
                                                                                                                                PL_SKLogger.writeLog("CC val change ", "Inside Y of CPV required");
                                                                                                                                formObject.setNGValue("cpv_required","Y");
                                                                                                                }
                                                                                                                else
                                                                                                                {
                                                                                                                                PL_SKLogger.writeLog("CC val change ", "Inside N of CPV required");
                                                                                                                                formObject.setNGValue("cpv_required","N");
                                                                                                                }
                                                                                                       }
                				 // disha FSD
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
                                                                         						else  if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_Modify")){
                                      //GenerateXML();
                                  	 formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
                                  }
                                                                            
                                                                                                break;
                                                                            
                                                                        
                                                                        
                                                                                
                                                                                 case VALUE_CHANGED:
                                                                                                                PL_SKLogger.writeLog(" In PL DDVT MAKER eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
                                                                                                                
                                                                                                                if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
                                                                                									//SKLogger_CC.writeLog("CC val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
                                                                                									getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
                                                                                								}
                                                                                								if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_DOJ")){
                                                                                									//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
                                                                                									getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
                                                                                								}             
                                                                                                                
                                                                                                                if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Dob")){
                                                                                                                                PL_SKLogger.writeLog("RLOS val change ", "Value of WorldCheck1_Dob is:"+formObject.getNGValue("WorldCheck1_Dob"));
                                                                                                                                //Changes done to auto populate age in world check fragment             
                                                                                                                                getAgeWorldCheck(formObject.getNGValue("WorldCheck1_Dob"));
                                                                                                                                }
                                                                                                                
                                                                                                                
                                                                                                                if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
                                                                                                                	int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
                                                                                                    				if(prd_count>0){
                                                                                                    					String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
                                                                                                    					loadPicklistProduct(ReqProd);
                                                                                                    				}
                                                                                                                }

                                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
                                                                                                                                PL_SKLogger.writeLog("PL val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
                                                                                                                                formObject.clear("AppType");
                                                                                                                                formObject.setNGValue("AppType","--Select--");
                                                                                                                                if(formObject.getNGValue("SubProd").equalsIgnoreCase("Business titanium Card")){
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
                                                                                                                                                formObject.setNGValue("EmpType","Self Employed");
                                                                                                                                }              
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Instant Money"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");
                                                                                                                                
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Limit Increase"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");
                                                                                                                                
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Salaried Credit Card"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");
                                                                                                                                
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Self Employed Credit Card"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");
                                                                                                                                
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Expat Personal Loans"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");
                                                                                                                                
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("National Personal Loans"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");
                                                                                                                
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Pre-Approved"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");
                                                                                                                                
                                                                                                                                else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Product Upgrade"))
                                                                                                                                                LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
                                                                                                                }
                                                                                                                
                                                                                                                if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_EmpIndusSector")){
                                                                                                                                
                                                                                                                                LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
                                                                                                                }

                                                                                                                if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_Indus_Macro")){
                                                                                                                                LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
                                                                                                                }
                                                                                                                
                                                                                                                else if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
                                                                                                                                if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))           
                                                                                                                                {
                                                                                                                                                formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
                                                                                                                                                PL_SKLogger.writeLog(" In PL DDVT MAKER VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

                                                                                                                                }
                                                                                                                                
                                                                                                                                 else{
                                                                                                                                                
                                                                                                                                                formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
                                                                                                                                                PL_SKLogger.writeLog(" In PL DDVT MAKER VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
                                                                                                                                                 }
                                                                                                                               }
                                                                                                                
                                                                                                              //Added by Arun (21/09/17)
                                                                                   							 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
                                                                                   								 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
                                                                                   								 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
                                                                                   								 String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
                                                                                   								 PL_SKLogger.writeLog(" query is ",sQuery);
                                                                                   								 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
                                                                                   								 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
                                                                                   								 {
                                                                                   									 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
                                                                                   									 formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
                                                                                   								 }
                                                                                   								 
                                                                                   							
                                                                                   								 
                                                                                   							 }	


                                                                                                                
                                                                                                                break;
                                                                                default: break;
                                                                                
                                                                }
                                  }            
                                                                catch(Exception ex)
                                                                {
                                                                                PL_SKLogger.writeLog("PL DDVY maker","Inside Exception to show msg at front end");
                                                                                HashMap<String,String> hm1=new HashMap<String,String>();
                                                                                                hm1.put("Error","Checked");
                                                                                if(ex instanceof ValidatorException)
                                                                                                {   PL_SKLogger.writeLog("PL DDVY maker","popupFlag value: "+ popupFlag);
                                                                                                                if(popupFlag.equalsIgnoreCase("Y"))
                                                                                                                {
                                                                                                                                PL_SKLogger.writeLog("PL DDVY maker","Inside popup msg through Exception "+ popupFlag);
                                                                                                                                if(popUpControl.equals(""))
                                                                                                                                {
                                                                                                                                                PL_SKLogger.writeLog("PL DDVY maker","Before show Exception at front End "+ popupFlag);
                                                                                                                                                throw new ValidatorException(new FacesMessage(alert_msg));
                                                                                                                                                //try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));}finally{hm1.clear();}
                                                                                                                                }else
                                                                                                                                {
                                                                                                                                                throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

                                                                                                                                }
                                                                                                                                
                                                                                                                }
                                                                                                                else{
                                                                                                                
                                                                                                                if(!popUpMsg.equals("")) {
                                                                                                                                try{ throw new ValidatorException(new CustomExceptionHandler("Details Fetched", popUpMsg,"EventType", hm1));}finally{hm1.clear();}
                                                                                                                                
                                                                                                                }
                                                                                                else {
                                                                                                                                try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));}finally{hm1.clear();}
                                                                                                                                
                                                                                                                }
                                                                                                                
                                                                                                                }
                                                                                                }
                                                                                else
                                                                                {
                                                                                ex.printStackTrace();
                                                                                PL_SKLogger.writeLog("exception in eventdispatched="+ ex,"");
                                                                                }
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

                                formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
                                
                                if(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED").equalsIgnoreCase("Yes") )
                                {
                                                PL_SKLogger.writeLog("CC val change ", "Inside Y of CPV required");
                                                formObject.setNGValue("cpv_required","Y");
                                }
                                else
                                {
                                                PL_SKLogger.writeLog("CC val change ", "Inside N of CPV required");
                                                formObject.setNGValue("cpv_required","N");
                                }
                              //incoming doc function
                                IncomingDoc();
                              //incoming doc function
                                saveIndecisionGrid();



                
                }
                public void fetch_desesionfragment()throws ValidatorException{
                                formObject = FormContext.getCurrentInstance().getFormReference();
                                
                    formObject.setVisible("DecisionHistory_Button3",false);
        formObject.setVisible("DecisionHistory_updcust",false);
        formObject.setVisible("DecisionHistory_chqbook",false);
        formObject.setVisible("cmplx_Decision_waiveoffver",false);
        loadPicklist3();
        //commented by Saurabh on 15th Oct as func was being called twice.
        //loadInDecGrid();
        PL_SKLogger.writeLog("PL DDVT Maker", "inside fetch fragment DecisionHistory END");
                }  

}

