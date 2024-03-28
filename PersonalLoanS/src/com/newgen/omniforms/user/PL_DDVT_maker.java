package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
//import com.newgen.omniforms.skutil.AesUtil;
import com.newgen.omniforms.util.PL_SKLogger;


import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.excp.CustomExceptionHandler;




import javax.faces.application.*;

public class PL_DDVT_maker extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	String ReqProd=null;
	 FormReference formObject = null;
	 public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL");
		PL_SKLogger.writeLog("PL DDVT MAKER", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	 

	public void formPopulated(FormEvent pEvent) 
	{
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        try{
            System.out.println("Inside DDVT MAKER PL");
            PL_SKLogger.writeLog("PL DDVT MAKER", "Inside formPopulated()" + pEvent.getSource());
            formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
            formObject.setNGValue("QueueLabel","PL_DDVT_maker");
            formObject.setNGValue("User_Name",formObject.getUserName()); 
            formObject.setNGValue("Introduce_date",formObject.getNGValue("CreatedDate"));
			partMatchValues();
        }catch(Exception e)
        {
            PL_SKLogger.writeLog("PL DDVT MAKER", "Exception:"+e.getMessage());
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

					if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
	        			hm.put("CustomerDetails","Clicked");
						popupFlag="N";
	    				formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
	    				
	        		}
					else if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
	        			hm.put("ProductContainer","Clicked");
						popupFlag="N";
	    				formObject.fetchFragment("ProductContainer", "Product", "q_Product");
	    				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						//LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");

	        			}
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDet")) {
						hm.put("GuarantorDet","Clicked");
						popupFlag="N";
						formObject.fetchFragment("GuarantorDet", "GuarantorDetails", "q_Guarantor");
						
						}
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("Self_Employed")) {
						hm.put("Self_Employed","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
						
						}				
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {
	        			
	        			hm.put("EmploymentDetails","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
		
	        			loadPicklist_Employment();
	        			
	        		}
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
						
						formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
										
							String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
							PL_SKLogger.writeLog("PL", "Emp Type Value is:"+EmpType);
		
							if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
							{
								formObject.setVisible("IncomeDetails_Frame3", false);
								//formObject.setHeight("Incomedetails", 630);
								//formObject.setHeight("IncomeDetails_Frame1", 605);	
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
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
						
					
					}		
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("InternalExternalLiability")) {
	        		
	        			
	        			formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
	
	        		      String sQuery="select Reporturl from nr_rlos_cust_extexpo_LoanDetails where child_wi='"+formObject.getWFWorkitemName()+"'";
                          List<List<String>> ReportURL=null;
                         
                          ReportURL= formObject.getDataFromDataSource(sQuery);
							PL_SKLogger.writeLog("PL", "Inside InternalExternalLiability-->ReportURL:"+ReportURL);

                          if(ReportURL!=null && ReportURL.size()>0){
                        	  formObject.setNGValue("Liability_New_ReportURL", ReportURL.get(0).get(0));
                          }

	        		}	
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
	        			hm.put("MiscFields","Clicked");
						popupFlag="N";
		
	        			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");

	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			
	        		}
					else if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation")) {
	        			hm.put("EligibilityAndProductInformation","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			
	        				        		}	
	        		
					else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
						PL_SKLogger.writeLog("Inside PL DDVT Fragment Expand", "LoanDetails");
            			hm.put("LoanDetails","Clicked");
    					popupFlag="N";
            			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
            			loadPicklist_LoanDetails();
            			try
    					{
    						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    					}
    					finally{hm.clear();}
            		}
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
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
	        		
					else if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) {
	        			hm.put("Alt_Contact_container","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
	        			
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
	        		
					else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
					{
						hm.put("FATCA","Clicked");
						popupFlag="N";
						formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
	       			
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}	
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
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
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
					{
						hm.put("OECD","Clicked");
						popupFlag="N";
						formObject.fetchFragment("OECD", "OECD", "q_OECD");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) 
					{
					
						formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
						LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Relationship  order by Code");

					}
					else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						
						hm.put("OECD","Clicked");
						popupFlag="N";
						fetch_desesionfragment();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					else if (pEvent.getSource().getName().equalsIgnoreCase("Inc_Doc")) {
	        			hm.put("Inc_Doc","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
	        			fetchIncomingDocRepeater();
	        			formObject.setVisible("IncomingDoc_UploadSig", false);
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		} 
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")){
						PL_SKLogger.writeLog("Inside PL DDVT Fragment Expand", "Part_Match");
						popupFlag="N";
	        			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
	        			loadPicklist_PartMatch();
	        			formObject.setNGValue("PartMatch_fname", formObject.getNGValue("cmplx_Customer_FIrstNAme"));
	        			formObject.setNGValue("PartMatch_lname", formObject.getNGValue("cmplx_Customer_LAstNAme"));
	        			formObject.setNGValue("PartMatch_funame", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
	        			formObject.setNGValue("PartMatch_nationality", formObject.getNGValue("cmplx_Customer_Nationality").subSequence(formObject.getNGValue("cmplx_Customer_Nationality").length()-2, formObject.getNGValue("cmplx_Customer_Nationality").length()));
	        			formObject.setNGValue("PartMatch_Dob", formObject.getNGValue("cmplx_Customer_DOb"));
	        			formObject.setNGValue("PartMatch_CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
	        			formObject.setNGValue("PartMatch_visafno", formObject.getNGValue("cmplx_Customer_VisaNo"));
	        			formObject.setNGValue("PartMatch_EID", formObject.getNGValue("cmplx_Customer_EmiratesID"));
	        			formObject.setNGValue("PartMatch_newpass", formObject.getNGValue("cmplx_Customer_PAssportNo"));//passport
	        			formObject.setNGValue("PartMatch_mno1", formObject.getNGValue("cmplx_Customer_MobNo"));//mobile
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_Incidents"))
	        			formObject.fetchFragment("FinacleCRM_Incidents", "FinacleCRMIncident", "q_FinIncident");
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_CustInfo"))
	        			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")){
	        			formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_Finaclecore");
	        			// code for lien and si details grid populate start
	        			// Finacle Core
	        			//Deepak Code change to Fetch Accoutn/Lien Data from grid data. 
	        			PL_SKLogger.writeLog("PL DDVT Maker", "fragment loaded: ");
	        			try{
	        			String query="select AcctType,'',AcctId,AcctNm,AccountOpenDate,AcctStat,'',AvailableBalance,'','','' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
	        			List<List<String>> list_acc=formObject.getDataFromDataSource(query);
	        			for(List<String> mylist : list_acc)
	        			 {
	        				PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid account Grid: "+mylist.toString());
	        				formObject.addItem("cmplx_FinacleCore_FinaclecoreGrid", mylist);
	        			 }
	        			
	        			query="select AcctId,LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
						//changed ended
	        			List<List<String>> list_lien=formObject.getDataFromDataSource(query);
	        			for(List<String> mylist : list_lien)
	        			 {
	        				PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
	        				formObject.addItem("cmplx_FinacleCore_liendet_grid", mylist);
	        			 }
						 //changed here in this query
						query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
	        			List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
						//changed ended
	        			
	        			for(List<String> mylist : list_SIDet)
	        			 {
	        				PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
	        				
	        				formObject.addItem("cmplx_FinacleCore_sidet_grid", mylist);
	        			 }
	        			}
	        			catch(Exception e){
	        				PL_SKLogger.writeLog("PL DDVT Maker", "Exception while setting data in grid:"+e.getMessage());
	        				popupFlag = "Y";
	        				alert_msg="Error while setting data in account grid";
	                        throw new ValidatorException(new FacesMessage(alert_msg));
	        			}
	        			popupFlag = "Y";
        				alert_msg="";
                        throw new ValidatorException(new FacesMessage(alert_msg));
					}
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("MOL"))
	        			formObject.fetchFragment("MOL", "MOL1", "q_MOL");
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("World_Check"))
	        			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enq"))
	        			formObject.fetchFragment("Reject_Enq", "RejectEnq", "q_RejectEnq");
					
					else if (pEvent.getSource().getName().equalsIgnoreCase("Sal_Enq"))
	        			formObject.fetchFragment("Sal_Enq", "SalaryEnq", "q_SalaryEnq");
					else{
						break;
						//SKLogger.writeLog("PL Fragment load event","event case not discribed in code fontrol name: "+pEvent.getSource().getName());
					}
					
					break;
					
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL DDVT MAKER FRAGMENT_LOADED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
							if(formObject.getNGValue("cmplx_Customer_NEP")=="false"){
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
									}
							//formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
							formObject.setLocked("ReadFromCard",false);
							//formObject.setLocked("cmplx_Customer_NEP",false);
							formObject.setLocked("Customer_CheckBox6",false);//CIF ID available
							formObject.setLocked("cmplx_Customer_referrorcode",false);
							formObject.setLocked("cmplx_Customer_referrorname",true);
							formObject.setLocked("cmplx_Customer_apptype",false);
							formObject.setLocked("cmplx_Customer_corpcode",false);
							formObject.setLocked("cmplx_Customer_bankwithus",false);
							formObject.setLocked("cmplx_Customer_noofdependent",false);
							formObject.setLocked("cmplx_Customer_minor",false);
							formObject.setLocked("cmplx_Customer_guarname",false);
							formObject.setLocked("cmplx_Customer_guarcif",false);
							loadPicklistCustomer();
							PL_SKLogger.writeLog("PL","Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
							LoadPickList("title", "select '--Select--' union select convert(varchar, description) from NG_MASTER_title with (nolock)");
							LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
							LoadPickList("nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_country with (nolock)");
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
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
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
							
							
							formObject.setLocked("Liability_New_fetchLiabilities",true);
							formObject.setLocked("takeoverAMount",true);
							formObject.setLocked("cmplx_Liability_New_DBR",true);
							formObject.setLocked("cmplx_Liability_New_DBRNet",true);
							formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
							formObject.setLocked("cmplx_Liability_New_TAI",true);
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
			            	LoadPickList("nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
			                LoadPickList("SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			            }
			            
						else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
			                LoadPickList("PartnerDetails_nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
			            }

			            
						else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
							
							Field_employment();
							formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
							formObject.setVisible("EMploymentDetails_Label36",false);
							formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
							formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
							formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
							formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
							loadPicklist_Employment();
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
							formObject.setLocked("cmplx_MiscFields_School",true);
							formObject.setLocked("cmplx_MiscFields_PropertyType",true);
							formObject.setLocked("cmplx_MiscFields_RealEstate",true);
							formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
						}
						
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
							
							loadPicklistELigibiltyAndProductInfo();
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
							formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
							formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
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
							formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);
							formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
							formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
							formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
							formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);

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
							 LoadPickList("nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
				             LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
							 LoadPickList("ResdCountry", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
							LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

						}
						else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
							LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
						}
							
						else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
							 LoadPickList("OECD_CountryBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
				                LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
								 LoadPickList("OECD_CountryTaxResidence", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
						}
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	loadPicklist1();
			                	formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
		                        if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true") ||formObject.getNGValue("cmplx_Customer_CIFNO").equalsIgnoreCase("")){
		                        	formObject.setVisible("DecisionHistory_Button2", true);
		                        }
						 }
						else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
			                	PL_SKLogger.writeLog("PL DDVT MAKER", "inside finacle core fragment loaded");
						 }
					
					
					  break;
					  
					case MOUSE_CLICKED:
						PL_SKLogger.writeLog(" In PL DDVT MAKER MOUSE_CLICKED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
							//GenerateXML();
						}
						
						//new code added
						//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)
						
						else if(pEvent.getSource().getName().equalsIgnoreCase("FetchDetails"))
						{
							popupFlag="Y";
							if(formObject.getNGValue("cmplx_Customer_card_id_available").equalsIgnoreCase("false")){
								outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","");
								PL_SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
								ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

								if(ReturnCode.equalsIgnoreCase("0000")){
									popupFlag="Y";
									valueSetCustomer(outputResponse);    
									formObject.setNGValue("Is_Customer_Eligibility","Y");
									parse_cif_eligibility(outputResponse);
									String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");

									if(NTB_flag.equalsIgnoreCase("true")){
										formObject.setNGValue("cmplx_Customer_CIFNO", "");
										formObject.setNGValue("cmplx_Customer_NTB", true);
										fetch_desesionfragment();
										formObject.setVisible("DecisionHistory_Button2", true);
										formObject.setLocked("DecisionHistory_Button2",false);
										PL_SKLogger.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
										alert_msg = "Customer is a New to Bank Customer.";
									}
									else{
										alert_msg = "Existing Customer details fetched Sucessfully";
										outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
										PL_SKLogger.writeLog("RLOS value of ReturnCode","Inside Customer");
										if(ReturnCode.equalsIgnoreCase("0000")){
											formObject.setNGValue("Is_Customer_Details","Y");
											PL_SKLogger.writeLog("RLOS value of Is_Customer_Details",formObject.getNGValue("Is_Customer_Details"));

											valueSetCustomer(outputResponse);
											try{

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
											alert_msg = "Error in fetch Customer details operation";
											PL_SKLogger.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
										}

										formObject.RaiseEvent("WFSave");
									}

								}
								else{
									PL_SKLogger.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
									alert_msg = "Error in Customer Eligibility operation";
								}
							}
							else{
								outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
								PL_SKLogger.writeLog("RLOS value of ReturnCode","Inside Customer");
								ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
								if(ReturnCode.equalsIgnoreCase("0000")){
									alert_msg= "Existing Customer details fetched Sucessfully";
									formObject.setLocked("Customer_Button1", false);
									formObject.setNGValue("Is_Customer_Details","Y");
									PL_SKLogger.writeLog("RLOS value of Is_Customer_Details",formObject.getNGValue("Is_Customer_Details"));
									formObject.setNGValue("cmplx_Customer_NTB","false");

									valueSetCustomer(outputResponse);
									try{
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
									alert_msg = "Error in Customer details operation";
									PL_SKLogger.writeLog("PL DDVT Maker value of ReturnCode","Error in Customer Eligibility: "+ ReturnCode);
								}
							}
							formObject.RaiseEvent("WFSave");
							throw new ValidatorException(new FacesMessage(alert_msg));
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
		       		//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)
						
						 else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button2")) {
							  PL_SKLogger.writeLog("","inside create cif button");
			                        hm.put("Liability_New_Button1","Clicked");
			                        popupFlag="N";
			                        //added
			                        String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
			                        String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
			                        String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			                        PL_SKLogger.writeLog("RLOS_Initiation: ", "inside create Account/Customer NTB value: "+NTB_flag );
			                        if(NTB_flag.equalsIgnoreCase("true") || NEP_flag.equalsIgnoreCase("true")||CIF_no.equalsIgnoreCase("")){
			                        	formObject.setNGValue("curr_user_name",formObject.getUserName());
			                             outputResponse = GenerateXML("NEW_CUSTOMER_REQ","");
			                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			                            PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
			                            if(ReturnCode.equalsIgnoreCase("0000")){
			                                valueSetCustomer(outputResponse);   
			                                formObject.setNGValue("cmplx_DecisionHistory_CifNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
			                                PL_SKLogger.writeLog("RLOS value of ReturnDesc","Inside if of New customer Req");
			                                

				                        	outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
				 	                        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				 	                        PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
				 	                        
				 	                        if(ReturnCode.equalsIgnoreCase("0000") ){
				 	                        	valueSetCustomer(outputResponse);
				 	                        	String acc_no = (outputResponse.contains("<NewAcid>")) ? outputResponse.substring(outputResponse.indexOf("<NewAcid>")+"</NewAcid>".length()-1,outputResponse.indexOf("</NewAcid>")):"";
					 	                        formObject.setNGValue("Account_Number",acc_no);
				 	                            formObject.setNGValue("Is_Account_Create","Y");
				 	                            formObject.setNGValue("EligibilityStatus","Y");
				 	                            formObject.setNGValue("EligibilityStatusCode","Y");
				 	                            formObject.setNGValue("EligibilityStatusDesc","Y");
				 	                            }
				 	                            else{
				 	                                formObject.setNGValue("Is_Account_Create","N");
				 	                            }
				 	                        PL_SKLogger.writeLog("RLOS value of Account Request",formObject.getNGValue("Is_Account_Create"));
				 	                        PL_SKLogger.writeLog("RLOS value of EligibilityStatus",formObject.getNGValue("EligibilityStatus"));
				 	                        PL_SKLogger.writeLog("RLOS value of EligibilityStatusCode",formObject.getNGValue("EligibilityStatusCode"));
				 	                        PL_SKLogger.writeLog("RLOS value of EligibilityStatusDesc",formObject.getNGValue("EligibilityStatusDesc"));
				 	                        
				                        
			                                
			                                }
			                                else{
			                                    PL_SKLogger.writeLog("RLOS value of ReturnDesc","Inside else of New Customer Req");
			                                }
			                        }
			                    	formObject.RaiseEvent("WFSave");
			                  try
			                    {
			                        throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			                    }
			                    finally{hm.clear();}

						  }
						 else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Add")){
							
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
		                      outputResponse = GenerateXML("DEDUP_SUMMARY","");
		                      ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
		  					PL_SKLogger.writeLog("PL value of ReturnCode",ReturnCode);
		  					if(ReturnCode.equalsIgnoreCase("0000") ){
								//valueSetCustomer(outputResponse);	
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
								formObject.setEnabled("PartMatch_Search", false);
							}
							else{
								formObject.setEnabled("PartMatch_Search", true);
							//	throw new ValidatorException(new CustomExceptionHandler("Dedupe Summary Fail!!","PartMatch_Search#Dedupe Summary Fail!!","",hm));
							}
							popupFlag = "Y";
                           
                            throw new ValidatorException(new FacesMessage(alert_msg));
						}
						//for BlackList Call added on 3rd May 2017
					        if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Blacklist")){
			                    PL_SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111");
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
			                    String ExtBlackDate="";
			                    String ExtBlackReason="";
								//added here
								 String WhiteListCode="";
			                    String Applicant="";
								//ended here
			                    PL_SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match1111 after initializing strings");
			                    outputResponse =GenerateXML("BLACKLIST_DETAILS","");
			                    PL_SKLogger.writeLog("PL value of ReturnDesc","Blacklist Details part Match");
			                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			                    PL_SKLogger.writeLog("PL value of ReturnCode part Match",ReturnCode);
			                   	                            
			                    if(ReturnCode.equalsIgnoreCase("0000") ){
			                    	alert_msg = "BlackList Check Sucessfull: " ;
			                        formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
			                        PL_SKLogger.writeLog("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType",formObject.getNGValue("Is_Customer_Eligibility_Part"));
			                        StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
			                        PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusType"+StatusType);
			                        if(StatusType.equalsIgnoreCase("Black List")){
			                            StatusFlag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
			                            PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusFlag"+StatusFlag);
			                            Reason= (outputResponse.contains("<StatusReason>")) ? outputResponse.substring(outputResponse.indexOf("<StatusReason>")+"</StatusReason>".length()-1,outputResponse.indexOf("</StatusReason>")):"";
			                            PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted Reason"+Reason);
			                            StatusCode= (outputResponse.contains("<StatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<StatusCode>")+"</StatusCode>".length()-1,outputResponse.indexOf("</StatusCode>")):"";
			                            PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusCode"+StatusCode);
			                            alert_msg = alert_msg+StatusType+": "+StatusFlag;
			                        }
			                        //added
			                        outputResponse = (outputResponse.contains("<StatusInfo>")) ? outputResponse.substring(outputResponse.indexOf("</StatusInfo>"),outputResponse.length()-1):"";
			                       // SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is BlacklistedoutputResponse111 outputResponse"+outputResponse);
			                        if(outputResponse.contains(StatusType)){
			            			//	 SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted outputResponse"+outputResponse);
			            				StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
			                			StatusFlag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
			                			//SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusCode");
			                			alert_msg = alert_msg+", "+StatusType+": "+StatusFlag;
			                			PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is StatusType StatusFlag"+StatusType+","+StatusFlag);
			            			}
			                        
			                        //ended
			                    }
			                    else{
			                        formObject.setNGValue("Is_Customer_Eligibility_Part","N"); 
			                        alert_msg = "BlackList Check failed Please contact administrator";
			                        formObject.setNGValue("Is_Customer_Eligibility_Part","N");    
			                        PL_SKLogger.writeLog("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType",formObject.getNGValue("Is_Customer_Eligibility_Part"));
			                    }
			                    
			                    
			                    popupFlag = "Y";
		                        
		                        throw new ValidatorException(new FacesMessage(alert_msg));
		                        
		                        /*
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
									  //added here
			                          Finacle_CRM.add(Applicant); 
									   //ended here
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
									  //added here
			                          Finacle_CRM.add(WhiteListCode);
									  //ended here
			                         SKLogger.writeLog("PL value of BlacklistFlag_Part","after adding in the grid");
			                           SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added inFinacle CRM grid: "+ Finacle_CRM.toString());
			                         
			                          formObject.addItem("cmplx_FinacleCRMCustInfo_FincustGrid",Finacle_CRM);
			                          SKLogger.writeLog("PL value of BlacklistFlag_Part","after adding in the grid11111");
			                           formObject.RaiseEvent("WFSave");
			                          */       
			                             
									                            
					        }
                    //ended here for BlackList Call
					 else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button1")){
		                    formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
		                    String BlacklistFlag_Part = "";
		                    String BlacklistFlag_reason = "";
		                    String BlacklistFlag_code = "";
		                    String NegativeListFlag = "";
		                    
		                    outputResponse =GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
		                    PL_SKLogger.writeLog("PL value of ReturnDesc","Customer Details part Match");
		                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
		                    BlacklistFlag_Part =  (outputResponse.contains("<BlackListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"NA";
		                    BlacklistFlag_reason =  (outputResponse.contains("<BlackListReason>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";
		                    BlacklistFlag_code =  (outputResponse.contains("<BlackListReasonCode>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";
		                    NegativeListFlag =  (outputResponse.contains("<NegativeListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
		                    PL_SKLogger.writeLog("PL value of ReturnCode part Match",ReturnCode);
		                    if(ReturnCode.equalsIgnoreCase("0000")){
		                    
		                        BlacklistFlag_Part =  (outputResponse.contains("<BlackListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"";                                  
		                        formObject.setNGValue("Is_Customer_Details_Part","Y");    
		
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
		                 // changed by abhishek start for populating cutomer info grid 22may2017
                            try{
                                
                                            List<String> list_custinfo = new ArrayList<String>();
                                            String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
                                            String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);
                                            
                                            list_custinfo.add(CIFID);  // cif id from partmatch
                                            list_custinfo.add("");
                                            list_custinfo.add(PASSPORTNO); // passport
                                            list_custinfo.add("");
                                            list_custinfo.add("");
                                            list_custinfo.add(NegativeListFlag);
                                            list_custinfo.add("");
                                            list_custinfo.add(BlacklistFlag_Part); // blacklist flag
                                            list_custinfo.add("");
                                            list_custinfo.add(BlacklistFlag_code);
                                            list_custinfo.add(BlacklistFlag_reason);
                                            list_custinfo.add("");
                                            list_custinfo.add("");
                                            
                                            PL_SKLogger.writeLog("PL DDVT Maker", "list_custinfo list values"+list_custinfo);
                                            formObject.addItem("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
                                            
                                            
                                }catch(Exception e){
                                            PL_SKLogger.writeLog("PL DDVT Maker", "Exception while setting data in grid:"+e.getMessage());
                                            popupFlag = "Y";
                                            alert_msg="Error while setting data in finacle customer info grid";
                                      throw new ValidatorException(new FacesMessage(alert_msg));
                                      }
                            //changed by abhishek end for populating cutomer info grid 22may2017

                                
		                     formObject.RaiseEvent("WFSave");          
		                }
                //changes done as said by Deepak Sir To call Customer_Details call ended
					        
					        // added yash for world_check call
							 if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_fetch")) {
								 PL_SKLogger.writeLog("","inside worldcheck"); 
								  outputResponse = GenerateXML("CUSTOMER_SEARCH_REQUEST","");
								  ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								  PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
								   
								 if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
										valueSetCustomer(outputResponse);	
										formObject.setNGValue("IS_WORLD_CHECK","Y");
										PL_SKLogger.writeLog("PL value of WORLD CHECKt","inside if of WORLDCHECK");
										alert_msg= "WORLD CHECK sucessfull";
										formObject.RaiseEvent("WFSave");
										}
										else{
											formObject.setNGValue("IS_WORLD_CHECK","N");
											PL_SKLogger.writeLog("PL value of WORLD CHECK","inside else of WORLD CHECK");
											alert_msg= "Error while performing WORLD CHECK";
										}
								 popupFlag = "Y";
								 throw new ValidatorException(new FacesMessage(alert_msg));
	                      	   }
							 	// ended yash for world_check call
							 
                
					 else if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Button2")){

									outputResponse = GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
									ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
									PL_SKLogger.writeLog("PL value of ReturnCode",ReturnCode);
									if(ReturnCode.equalsIgnoreCase("0000") ){
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
						
							 
							 else if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button2")){//Liability_addition Add
									formObject.setNGValue("liabilityAddition_winame",formObject.getWFWorkitemName());
									PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("liabilityAddition_winame"));
									formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
								}
								
								 else if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button3")){//Liability_addition modify
									formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
								}
								
								 else if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button4")){//Liability_addition delete
									formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");

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

						 else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_Add")){
								formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
								PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("ReferenceDetails_reference_wi_name"));
								formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
							}
							
							 else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference__modify")){
								formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
							}
							
							 else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_delete")){
								formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");

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
								if(EmpName.trim().equalsIgnoreCase(""))
									query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";
				               
								else
									query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";
				
								 PL_SKLogger.writeLog("PL", "query is: "+query);
								 populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,Nature Of Business,EMPLOYER CATEGORY PL NATIONAL,EMPLOYER CATEGORY CARDS,EMPLOYER CATEGORY PL EXPAT,INCLUDED IN PL ALOC,DOI IN PL ALOC,INCLUDED IN CC ALOC,DATE OF INCLUSION IN CC ALOC,NAME OF AUTHORIZED PERSON FOR ISSUING SC/STL/PAYSLIP,ACCOMMODATION PROVIDED,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,OWNER/PARTNER/SIGNATORY NAMES AS PER TL,ALOC REMARKS,HIGH DELINQUENCY EMPLOYER", true, 20);
					             
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
						
						 else if(pEvent.getSource().getName().equalsIgnoreCase("14.AltContactDetails_ContactDetails_Save")){
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
						
						
						 else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistoryContainer");
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
						
						 else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button1"))
						 {
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
						 }
						 
						 else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button2"))
						 {
							 
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
						 }
						 
						 else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button3"))
						 {
							 
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
						 }
						 else{
							 break;
							// SKLogger.writeLog("PL DDVT Click event", "FOrm control not defined: "+pEvent.getSource().getName());
						 }
						break;
					
					 case VALUE_CHANGED:
							PL_SKLogger.writeLog(" In PL DDVT MAKER eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							
							if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
								PL_SKLogger.writeLog("PL val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
								getAge(formObject.getNGValue("cmplx_Customer_DOb"));
							}	
							else if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
								ReqProd=formObject.getNGValue("ReqProd");
								PL_SKLogger.writeLog("PL val change ", "Value of ReqProd is:"+ReqProd);
								loadPicklistProduct(ReqProd);
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
							
							else if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_Decision")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
									PL_SKLogger.writeLog(" In PL DDVT MAKER VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("cmplx_Decision_Decision"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
									PL_SKLogger.writeLog(" In PL DDVT MAKER VALChanged---New Value of decision is: ", formObject.getNGValue("cmplx_Decision_Decision"));
								 	  }
							 	}
							else{
								break;
							//SKLogger.writeLog("In side PL DDVT change value event", "Control name not defined: "+pEvent.getSource().getName());
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
					System.out.println("exception in eventdispatched="+ ex);
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
		//saveIndecisionGrid();
		//code for IncomingDocument started here
			String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
			List<List<String>> outputindex = null;
			List<List<String>> secondquery=null;
			outputindex = formObject.getNGDataFromDataCache(squery);
			PL_SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);
			String itemIndex =outputindex.get(0).get(0);
			formObject.setNGValue("NewApplicationNo", itemIndex);
			formObject.setNGValue("ApplicationRefNo", itemIndex);
			PL_SKLogger.writeLog("RLOS Initiation", "Inside submitFormStarted() after setting the value of itemindex and itemtype");
		
			String requested_product="";
			String requested_subproduct="";
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,"valu of row count"+n);
			if(n>0)
			{
				for(int i=0;i<n;i++)
				{
					requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
					PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT requested_product:" ,requested_product);
					requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
					PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT requested_subproduct:" ,requested_subproduct);

				}
			}
			String sQuery="SELECT Name,DocumentIndex FROM PDBDocument with(nolock) WHERE DocumentIndex IN (SELECT DocumentIndex FROM PDBDocumentContent with(nolock) WHERE ParentFolderIndex= '"+itemIndex+"')";
			
			
			outputindex = null;
			PL_SKLogger.writeLog("PL DDVT Maker", "sQuery for document name is:" +  sQuery);
			outputindex = formObject.getDataFromDataSource(sQuery);
		//	 outputindex=outputindex.get(0).get(0);
			
			
			PL_SKLogger.writeLog("PL DDVT Maker", "outputItemindex is:" +  outputindex);
			
			
			
			if(outputindex==null || outputindex.size()==0) {
				PL_SKLogger.writeLog("","output index is blank");
				String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"'";
				PL_SKLogger.writeLog("PL DDVT Maker", "sQuery for document name is:" +  query);
				secondquery = formObject.getNGDataFromDataCache(query);
				for(int j = 0; j < secondquery.size(); j++) {
					if("Y".equalsIgnoreCase(secondquery.get(j).get(1))) {
						//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents"));
					}
				}
			}
			
			List<List<String>> outputupdateindex = null;
			String Document_Name =outputindex.get(0).get(0);
			String DocIndex=outputindex.get(0).get(1);
			if(Document_Name.equalsIgnoreCase("License")){
				formObject.setNGValue("cmplx_DocName_DocIndex", DocIndex);
				
			}
			PL_SKLogger.writeLog("PL DDVT Maker", "Document_Index Document_Name is:" + Document_Name);
			PL_SKLogger.writeLog("PL DDVT Maker", "DocIndex is:" +  DocIndex);
			if(outputindex != null && outputindex.size() != 0)
			{
				for(int i = 0; i < outputindex.size(); i++)
				{
						if(outputindex.get(i).get(0).equalsIgnoreCase("License"))
							formObject.setNGValue("sig_docindex", (outputindex.get(i).get(1)));
				}
					
			}
			String[] arrval=new String[outputindex.size()];
			String[] arrdocIndex=new String[outputindex.size()];
			IRepeater repObj=null;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			PL_SKLogger.writeLog("CC DDVT Maker","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
			if(outputindex != null && outputindex.size() != 0)
			{
				System.out.println("Staff List "+outputindex);
				for(int i = 0; i < outputindex.size(); i++)
				{
					
					//arrval[i].equalsIgnoreCase(repObj.getValue(i, "cmplx_DocName_DocName"));
					arrval[i]=outputindex.get(i).get(0);
					arrdocIndex[i]=outputindex.get(i).get(1);
				
					PL_SKLogger.writeLog("","docIndex array is :"+arrdocIndex[i]);
					
					
					PL_SKLogger.writeLog("PL DDVT Maker", " doc index sMap is:" +  outputindex.get(i).get(1));
					//String supdateQuery="UPDATE ng_rlos_incomingDoc SET DocInd='"+outputindex.get(i).get(1)+"' WHERE wi_name='"+formObject.getWFWorkitemName()+"' AND DocName='"+arrval[i]+"'";
					////outputupdateindex = null;
					//SKLogger.writeLog("RLOS Initiation", "update sQuery for document name is:" +  supdateQuery);
					//outputupdateindex = formObject.getDataFromDataSource(supdateQuery);
					PL_SKLogger.writeLog("PL DDVT Maker", "outputItemindex of update is:" +  outputupdateindex);
					
				}
			}
		
			for(int k=0;k<arrval.length;k++)
			{
				PL_SKLogger.writeLog("PL DDVT Maker", " arrval is:" +  arrval[k]);
			}
			
			PL_SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" ,requested_product);
			String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"'";
			outputindex = null;
			PL_SKLogger.writeLog("PL DDVT Maker", "sQuery for document name is:" +  query);
			outputindex = formObject.getNGDataFromDataCache(query);
			PL_SKLogger.writeLog("PL DDVT Maker", "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());
			//IRepeater repObj=null;
		//	repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		//	SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
			String [] misseddoc=new String[outputindex.size()];
			int count=0;
			for(int j = 0; j < outputindex.size(); j++)
		
			{
				String DocName =outputindex.get(j).get(0);
				String Mandatory =outputindex.get(j).get(1);
				PL_SKLogger.writeLog("PL DDVT Maker", "Document_Index Document_Name is:" + DocName+","+Mandatory);
				
				if (repObj.getRepeaterRowCount() != 0) {
					if(Mandatory.equalsIgnoreCase("Y")){

						int l=0;
						
						while(l<arrval.length)
						{
							PL_SKLogger.writeLog("","DocName::"+DocName+":str:"+arrval[l]);

							if(arrval[l].equalsIgnoreCase(DocName))
							{
								PL_SKLogger.writeLog("","document is present in the list");
								PL_SKLogger.writeLog("DocIndex","DocIndex value for the corrseponding document"+arrdocIndex[l]);
								repObj.setValue(j, "cmplx_DocName_DocIndex",arrdocIndex[l]);
								
								 String StatusValue=repObj.getValue(j,"cmplx_DocName_Status");
								 PL_SKLogger.writeLog("","StatusValue::"+StatusValue);
			        			 if(!StatusValue.equalsIgnoreCase("Recieved")){
			        				repObj.setValue(j,"cmplx_DocName_Status","Recieved");
			        				repObj.setEditable(j, "cmplx_DocName_Status", false);
			        				 PL_SKLogger.writeLog("","StatusValue::123final"+StatusValue);
			        			}
		        			 	count=1;
		        			 	PL_SKLogger.writeLog("","StatusValue::123final count value:"+count);
								//continue;
							}
							
							
					//	count=0;
						l++;
						 PL_SKLogger.writeLog("","StatusValue::123final count value:"+l);
						}
						if(count==0){
							PL_SKLogger.writeLog("","Document is not present in the list");
							misseddoc[j]=DocName;
							//l++;
							PL_SKLogger.writeLog("PL DDVT Maker", " misseddoc is in j is:" +  misseddoc[j]);

							String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
		        			PL_SKLogger.writeLog("","StatusValue::"+StatusValue);
		        			String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
		        			PL_SKLogger.writeLog("","Remarks::"+Remarks);
		        			if(!(StatusValue.equalsIgnoreCase("Recieved")||StatusValue.equalsIgnoreCase("Deferred"))){
		        				if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
		        				PL_SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document fill the Remarks");
		        			//	throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));
		        				}
		        				else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
		        					PL_SKLogger.writeLog("You may proceed further","Proceed further");
		        				}
		        			}
						}
					}
			
				}
				count=0;
			}
			StringBuilder mandatoryDocName = new StringBuilder("");
			for(int k=0;k<misseddoc.length;k++)
			{
				if(null != misseddoc[k]) {
					mandatoryDocName.append(misseddoc[k]).append(",");
				}
				PL_SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +misseddoc[k]);
			}
			mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
			PL_SKLogger.writeLog("CC DDVT Maker", "misseddoc is:" +mandatoryDocName.toString());
			//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
			//code for IncomingDocument ended here

	}
	public void fetch_desesionfragment()throws ValidatorException{
		formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("PL DDVT Maker", "inside fetch fragment DecisionHistory");
	    formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
        formObject.setVisible("DecisionHistory_Button3",false);
        formObject.setVisible("DecisionHistory_updcust",false);
        formObject.setVisible("DecisionHistory_chqbook",false);
        loadPicklist3();
        loadInDecGrid();
        PL_SKLogger.writeLog("PL DDVT Maker", "inside fetch fragment DecisionHistory END");
	}  

}

