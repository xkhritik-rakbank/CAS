
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Cad Analyst2

File Name                                                         : CC_CAD_Analyst2

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_CAD_Analyst_2 extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	 
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              

	 ***********************************************************************************  */
	public void formLoaded(FormEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();												 
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		makeSheetsInvisible(tabName, "9,11,12,13,14,15,16,17");
		formObject.setVisible("SmartCheck_Label2",true);
		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);
	}
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header             

	 ***********************************************************************************  */
	public void formPopulated(FormEvent pEvent) 
	{
		try{
           new CC_CommonCode().setFormHeader(pEvent);
           enable_CPV();
        }catch(Exception e)
        {
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
        }
    }
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

				case FRAME_EXPANDED:
					CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);						
					break;
					
					  
					case FRAGMENT_LOADED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
					  fragment_loaded(pEvent,formObject);
					  break;
					  
					  
					case MOUSE_CLICKED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().mouse_clicked(pEvent);
						break;
					
					 case VALUE_CHANGED:
							CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new CC_CommonCode().value_changed(pEvent);
							 break;
					default: break;
					
				}
	}


	public void setDeviationGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		//String query = "select Deviation_Code_Refer from ng_rlos_IGR_Eligibility_CardProduct where Deviation_Code_Refer is not null and Child_Wi like '"+wi_name+"'";
		String query="select case when Decision='Declined' then Declined_Reason  when Decision='Refer' then Deviation_Code_Refer when Decision='Approve' then Deviation_Code_Refer end as Deviation,Decision from ng_rlos_IGR_Eligibility_CardProduct with (nolock) where  Child_Wi ='"+wi_name+"'";
		List<List<String>> record = formObject.getNGDataFromDataCache(query);
		CreditCard.mLogger.info("Deviation query formulated is: "+query);
		if(record !=null && record.get(0)!=null && !record.isEmpty())  //if(record !=null && record.get(0)!=null && record.size()>0)
		{
			for(List<String> row:record){
				List<String> temp = new ArrayList<String>();
				int separatorIndex = row.get(0).lastIndexOf('-');
				temp.add(row.get(0).substring(0, separatorIndex));
				CreditCard.mLogger.info("Description is: "+row.get(0).substring(0, separatorIndex));
				temp.add(row.get(0).substring(separatorIndex+1, row.get(0).length()));
				CreditCard.mLogger.info("Code is: "+row.get(0).substring(separatorIndex+1, row.get(0).length()));
				temp.add(formObject.getWFWorkitemName());
				formObject.addItemFromList("cmplx_DEC_cmplx_gr_DeviaitonDetails", temp);//
			}
		}
	}


	public void setExposureGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		float totalOut = 0;
		float totalEMI = 0;
		String query = "select LoanType as Scheme,Liability_type,AgreementId as Agreement,isnull(TotalOutstandingAmt,OutstandingAmt) as OutBal,PaymentsAmt as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+wi_name+"' and LoanStat not in ('Pipeline','CAS-Pipeline','C') union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,isnull(TotalOutstandingAmt,OutstandingAmt) as OutBal,PaymentsAmount as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_Wi = '"+wi_name+"' and CardStatus not in ('Pipeline','CAS-Pipeline','C')  union select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+wi_name+"' and LoanStat not in  ('Pipeline','Closed') union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,CurrentBalance as OutBal,PaymentsAmount as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_Wi = '"+wi_name+"' and CardStatus not in  ('Pipeline','Closed') union select (select Description from NG_MASTER_contract_type where code = TypeOfContract) as Scheme,'' as Liability_type,'' as Agreement,OutstandingAmt as OutBal,EMI,Consider_For_Obligations  from ng_rlos_gr_LiabilityAddition with (nolock) where LiabilityAddition_wiName = '"+wi_name+"'";
		CreditCard.mLogger.info("Query for exposure grid is: "+query);
		List<List<String>> record = formObject.getNGDataFromDataCache(query);
		if(record !=null && record.size()>0 && record.get(0)!=null)   //if(record !=null && record.size()>0 && record.get(0)!=null)
		{
			for(List<String> row:record){
				CreditCard.mLogger.info("CFO received is: "+row.get(5));
				if( row.get(5) == null ||"null".equalsIgnoreCase(row.get(5))|| "true".equalsIgnoreCase(row.get(5)) || row.get(5).equalsIgnoreCase(null) ||"".equalsIgnoreCase(row.get(5))){
					CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
					row.set(5, "Yes");
					CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
				}
				else if("false".equalsIgnoreCase(row.get(5))){
					CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
					row.set(5, "No");
					CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
				}
				CreditCard.mLogger.info("row.get(3) is: "+row.get(3));
				CreditCard.mLogger.info("row.get(4) is: "+row.get(4));
				if(row.get(3)!=null && !row.get(3).equalsIgnoreCase("null")){
				totalOut += Float.parseFloat(row.get(3));
				}
				if(row.get(4)!=null && !row.get(4).equalsIgnoreCase("null")){
				totalEMI += Float.parseFloat(row.get(4));
				}
				row.add(formObject.getWFWorkitemName());
				formObject.addItemFromList("cmplx_DEC_cmplx_gr_ExpoDetails", row);
			}
		}
		/*if(formObject.isVisible("ExtLiability_Frame4")){
		if(formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid")>0){
		for(int i=0;i<formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");i++){
			totalOut+=Float.parseFloat(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 12));
			totalEMI+=Float.parseFloat(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 2));
		}
		}
		}*/
		CreditCard.mLogger.info("Total Out is: "+String.valueOf(totalOut));
		CreditCard.mLogger.info("Total EMI is: "+String.valueOf(totalEMI));
		formObject.setNGValue("cmplx_DEC_TotalOutstanding", String.valueOf(totalOut));
		formObject.setNGValue("cmplx_DEC_TotalEMI", String.valueOf(totalEMI));
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              :               
	Description                         :  Product Default      

	 ***********************************************************************************  */
	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              :               
	Description                         :  Product Default      

	 ***********************************************************************************  */
	public void initialize() {
		// TODO Auto-generated method stub
		
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		CreditCard.mLogger.info("inside submitformcompleted: ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		saveIndecisionGrid();
		
		 String Decision= formObject.getNGValue("cmplx_DEC_Decision");
		 if ("Approve".equalsIgnoreCase(Decision) || "SENDBACK".equalsIgnoreCase(Decision)){
			 List<String> objInput=new ArrayList<String>();
			 //disha FSD cad delegation procedure changes
			 objInput.add("Text:"+formObject.getWFWorkitemName());
			 objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			 objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			 objInput.add("Text:"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));
			 //change by saurabh on 7th Dec
			 objInput.add("Text:"+Decision);
			 CreditCard.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3)+objInput.get(4));
			 if ("".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))||"Select".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))){
					formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);
					}
			 }
		 //below code added by nikhil
		
		 
		 //formObject.setNGValue("CAD_Analyst2_Dec", formObject.getNGValue("cmplx_DEC_Decision")); 
	}


	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		  formObject.setNGValue("CAD_Analyst2_Dec", formObject.getNGValue("cmplx_DEC_Decision"));  
		
		if("Refer to Smart CPV".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		{
			saveSmartCheckGrid();
		}
		
		//++ below code aded by disha on 27-3-2018 to set value of var_int3
		
		 if("Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		  {
			  CreditCard.mLogger.info("Inside  Approve CPV");
			  formObject.setNGValue("q_hold1",1);
			  formObject.setNGValue("VAR_INT3",1);
			  CreditCard.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
		  }
		 CreditCard.mLogger.info("Outside NA ");
		  if("NA".equalsIgnoreCase(formObject.getNGValue("cadNExt")) || "NA".equalsIgnoreCase(formObject.getNGValue("cadlist")))
		  { 
			  CreditCard.mLogger.info("Inside NA ");
			  if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			  {
				  CreditCard.mLogger.info("Inside  Approve CPV");
				  formObject.setNGValue("q_hold1",1);
				  formObject.setNGValue("VAR_INT3",1);
				  CreditCard.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
			  }
		  }
		  CreditCard.mLogger.info("Outside NA 2");
		//++ above code aded by disha on 27-3-2018 to set value of var_int3
		LoadReferGrid();
		////formObject.setNGValue("cmplx_DEC_Remarks","");
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/
				//added by yash for CC FSD
				if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("Customer_Frame1",true);
					loadPicklistCustomer();
				
				}	
				//added by yash for  CC FSD
				else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("Product_Frame1",true);
					int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					if(prd_count>0){
						String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
					loadPicklistProduct(ReqProd);
					}
					//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
					/*
					LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
					LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
					
					//change by saurabh on 7tb oct for JIRA- 2561. If condition added.
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM")){
					formObject.setVisible("Product_Label5",true);
					formObject.setVisible("ReqTenor",true);
					}
					formObject.setLocked("Product_Frame1",true);
					formObject.setLocked("Add",true);
					formObject.setLocked("Modify",true);
					formObject.setLocked("Delete",true);
					formObject.setLocked("Product_Save",true);
					formObject.setLocked("cmplx_Product_cmplx_ProductGrid_table",true);
					formObject.setLocked("Customer_save",true);
					formObject.setLocked("Customer_save",true);*/
					
				}
				//added by yash for CC FSD
				else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("IncomeDetails_Frame1",true);
					loadpicklist_Income();
					formObject.setLocked("IncomeDetails_Text2",true);
					formObject.setVisible("IncomeDetails_Label22",false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2",false);
					/*formObject.setVisible("IncomeDetails_ListView1",true);
					formObject.setVisible("IncomeDetails_Button1",true);
					formObject.setVisible("IncomeDetails_Button2",true);
					formObject.setVisible("IncomeDetails_Button3",true);*/
				}
				//added by yash for CC FSD
				else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					loadPicklist_CompanyDet();
					formObject.setLocked("CompanyDetails_Frame1",true);
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
					//formObject.setVisible("CompanyDetails_Label8",false);
					formObject.setLocked("estbDate",false);
					formObject.setEnabled("CompanyDetails_DatePicker1",true);
				}
				//added by yash fro CC FSD
				else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
		            
					LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
	                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
	                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
					LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
					}
				//added by yash for CC FSD
				else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					 LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
					formObject.setLocked("PartnerDetails_Frame1",true);
					
				}
				//added by yash for CC FSD
				else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExtLiability_Frame1",true);
					//formObject.setVisible("Liability_New_Label1",true);
					formObject.setVisible("Liability_New_Text1",true);
					formObject.setVisible("Liability_New_Label2",true);
					formObject.setVisible("Liability_New_Text3",true);
					//formObject.setVisible("Liability_New_Label3",true);
					formObject.setVisible("Liability_New_Text2",true);
					formObject.setVisible("Liability_New_CheckBox1",true);
					formObject.setVisible("Liability_New_CheckBox2",true);
					formObject.setVisible("Liability_New_CheckBox3",true);
					formObject.setVisible("Liability_New_Label4",true);
					formObject.setVisible("Liability_New_Text4",true);
					formObject.setVisible("Liability_New_Label5",true);
					formObject.setVisible("Liability_New_Combo1",true);
					
					formObject.setVisible("ExtLiability_Label15",true);
					formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
					formObject.setVisible("Liability_New_Label6",true);
					formObject.setVisible("Liability_New_Text5",true);
					formObject.setVisible("Liability_New_Label8",true);
					formObject.setVisible("Liability_New_Text6",true);
					formObject.setVisible("Liability_New_Label7",true);
					formObject.setVisible("Liability_New_Text7",true);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
					formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
				//code by aman 17/12	
					formObject.setVisible("Liability_New_Label1",false);
					formObject.setVisible("Liability_New_MOB",false);
					LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

					formObject.setVisible("Liability_New_Label3",false);
					formObject.setVisible("Liability_New_Outstanding",false);
					formObject.setVisible("Liability_New_Delinkinlast3months",false);
					formObject.setVisible("Liability_New_DPDinlast18months",false);
					formObject.setVisible("Liability_New_DPDinlast6",false);
					formObject.setVisible("Liability_New_Label4",false);
					formObject.setVisible("Liability_New_writeOfAmount",false);
					formObject.setVisible("Liability_New_Label5",false);
					formObject.setVisible("Liability_New_worstStatusInLast24",false);
					formObject.setVisible("Liability_New_Label2",false);
					formObject.setVisible("Liability_New_Utilization",false);
					formObject.setVisible("Liability_New_Label10",false);
					formObject.setVisible("Liability_New_rejApplinlast3months",false);
					//code by aman 17/12	
				}
				else if ("Internal Liabilities".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExtLiability_Frame2",true);
					
				}
				else if ("External Liabilities".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExtLiability_Frame3",true);
					
					
				}
				
				else if ("Liability Addition".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExtLiability_Frame4",true);		
				}
				//added by yash for CC FSD
				else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("EMploymentDetails_Frame1", true);							
					loadPicklist4();
					formObject.setVisible("EMploymentDetails_Label19",false);
					formObject.setVisible("cmplx_EmploymentDetails_funded_faci",false);
					formObject.setVisible("EMploymentDetails_Label16",false);
					formObject.setVisible("cmplx_EmploymentDetails_borrowing_rel",false);
					formObject.setVisible("EMploymentDetails_Label11",false);
					formObject.setVisible("cmplx_EmploymentDetails_head_office",false);
					
					
				}
				else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
					//added 6th sept for proc 1926
					// ++ below code not commented at offshore - 06-10-2017
					formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
					{
					formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", false);//total final limit
					formObject.setLocked("ELigibiltyAndProductInfo_Button1", false);//calculate re-eligibilit
					LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
					
					LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
				
					//loadPicklist_Address();
					//formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					}
					//ended 6th sept for proc 1926
				}
				else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("AddressDetails_Frame1",true);
		
				}
				else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
					//change by saurabh on 7th Dec
					LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
					
					formObject.setLocked("AltContactDetails_Frame1",true);
				
				}
				//added by yash for CC FSD
				else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("Reference_Details_Frame1",true);
					
				}
				else if ("CardCollection".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("CardDetails_Frame1",true);
				}
				else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("FATCA_Frame6",true);
					loadPicklist_Fatca();
					
				}
				else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("CardDetails_Frame1",true);
					
				}
				else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("KYC_Frame1",true);
					
				}
				else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("OECD_Frame8",true);
					
				}
				// ++ below code already present - 06-10-2017 name change from Details to CC_Loan
				else if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("CC_Loan_Frame1",true);
					
				}
				else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("PartMatch_Frame1",true);
					
				}
				else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("FinacleCRMIncident_Frame1",true);
					
				}
				else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
					
				}
				else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExternalBlackList_Frame1",true);
					
				}
				else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					LoadPickList("FinacleCore_ChequeType", "select '--Select--' union select convert(varchar, description) from ng_MASTER_Cheque_Type with (nolock)");
					LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

					formObject.setLocked("FinacleCore_Frame1",true);
					//added by saurabh on13th Oct for JIRA-2517
					formObject.setLocked("FinacleCore_Frame9", true);
				}
				
				else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					loadPicklist_Mol();
					formObject.setLocked("MOL1_Frame1",true);
					
				}
				else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("WorldCheck1_Frame1",true);
					
				}
				else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("IncomingDocument_Frame",false);
				}
				//added by yash for CC FSD
				else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("SmartCheck_Frame1",true);
					 formObject.setVisible("SmartCheck1_Label2",false);
					 formObject.setVisible("SmartCheck1_CPVRemarks",false);
					 formObject.setVisible("SmartCheck1_Label4",false);
					 formObject.setVisible("SmartCheck1_FCURemarks",false);
					 
					 //formObject.setLocked("SmartCheck1_Modify",true);
					 
					
				}
				else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("RejectEnq_Frame1",true);
					
				}
				else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("SalaryEnq_Frame1",true);
					
				}
				/*if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
					//loadPicklist_Address();
					formObject.setLocked("ExternalBlackList_Frame1",true);
					
				}*/
				else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("IncomingDocument_Frame",true);
					
				}
				else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("PostDisbursal_Frame2",true);
					
				}
				//added by yash for CC FSD
				else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
					formObject.setLocked("Compliance_Frame1",true);
				}
				//ended by yash for CC FSD
				else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("CC_Loan_Frame1",true);
					loadPicklist_ServiceRequest();
					
				}
				//added by yash for CC FSD
				else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					notepad_withoutTelLog();
					notepad_load();
			        
					formObject.setTop("NotepadDetails_savebutton",410);
				}
				//ended by yash
				else if ("Fraud Control Unit".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
				}
//added by yash for CC FSD
					else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
						try{
						// Ref 1 changes by saurabh on 8th nov shifting tops of controls down by 50 to adjust TOtal Out and TOTAL emi below grid.	
							//for decision fragment made changes 8th dec 2017
							/*	formObject.setTop("DecisionHistory_Frame2",6);
						formObject.setTop("DecisionHistory_Frame3",224); 
						formObject.setTop("DecisionHistory_Decision_ListView1",565);
						formObject.setTop("DecisionHistory_save",740);
						formObject.setVisible("cmplx_DEC_ReferReason",false);
						formObject.setVisible("DecisionHistory_dec_reason_code",true);
						formObject.setTop("DecisionHistory_dec_reason_code",461);
						formObject.setLeft("DecisionHistory_dec_reason_code",330);
						formObject.setVisible("DecisionHistory_Label33",true);
						formObject.setVisible("cmplx_DEC_TotalOutstanding",true);
						formObject.setLeft("DecisionHistory_Label33", 650);
						formObject.setTop("DecisionHistory_Label33", 180); 
						formObject.setLeft("cmplx_DEC_TotalOutstanding", 650);
						formObject.setTop("cmplx_DEC_TotalOutstanding", 195);
						formObject.setVisible("DecisionHistory_Label32",true);
						formObject.setVisible("cmplx_DEC_TotalEMI",true);
						formObject.setLeft("DecisionHistory_Label32", 850);
						formObject.setTop("DecisionHistory_Label32", 180);
						formObject.setLeft("cmplx_DEC_TotalEMI", 850);
						formObject.setTop("cmplx_DEC_TotalEMI", 195);*/
						/*formObject.setVisible("DecisionHistory_CheckBox1",false);
                        formObject.setVisible("DecisionHistory_Label1",false);
                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);
                        formObject.setVisible("DecisionHistory_Label3",false);
                        formObject.setVisible("DecisionHistory_Combo3",false);
                        formObject.setVisible("DecisionHistory_Label6",false);
                        formObject.setVisible("DecisionHistory_Combo6",false);
                        formObject.setVisible("DecisionHistory_Decision_Label4",false);
                        formObject.setVisible("cmplx_DEC_Remarks",false);
                        formObject.setVisible("DecisionHistory_Label8",false);
                        formObject.setVisible("DecisionHistory_Text4",false);
                        formObject.setVisible("DecisionHistory_Label7",false);
                        formObject.setVisible("DecisionHistory_Text3",false);*/
                        //formObject.setVisible("DecisionHistory_Label2",false);
                        /*formObject.setVisible("DecisionHistory_Text2",false);
                        formObject.setVisible("cmplx_DEC_ReferReason",false);*/
                        //formObject.setVisible("cmplx_DEC_Description",false);
                        /*formObject.setVisible("cmplx_DEC_Strength",false);
                        formObject.setVisible("cmplx_DEC_Weakness",false);
                        formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
                        formObject.setVisible("cmplx_DEC_ContactPointVeri",false);*/
					/*	formObject.setVisible("DecisionHistory_Label15",true);
                        formObject.setTop("DecisionHistory_Label15",400); 
                        formObject.setLeft("DecisionHistory_Label15",24);
                        formObject.setLeft("DecisionHistory_Label5",24);
                        formObject.setVisible("DecisionHistory_Label5",true);
                        formObject.setVisible("cmplx_DEC_ScoreGrade",true);
                        formObject.setTop("DecisionHistory_Label5",400);
                        formObject.setTop("cmplx_DEC_ScoreGrade",416);
                        formObject.setLeft("DecisionHistory_Label5",24);
                        formObject.setLeft("cmplx_DEC_ScoreGrade",24);
                        formObject.setTop("DecisionHistory_Label17",400);
                        formObject.setTop("cmplx_DEC_LoanApprovalAuth",416);
                        formObject.setLeft("DecisionHistory_Label17",330);
                        formObject.setLeft("cmplx_DEC_LoanApprovalAuth",330);
                        formObject.setTop("DecisionHistory_Button4",400); 
                        formObject.setLeft("DecisionHistory_Button4",650);
                        formObject.setVisible("DecisionHistory_Decision_Label3",true);
                        formObject.setVisible("cmplx_DEC_Decision",true);
                        formObject.setTop("DecisionHistory_Decision_Label3",444); 
                        formObject.setLeft("DecisionHistory_Decision_Label3",24);
                        formObject.setTop("cmplx_DEC_Decision",461); 
                        formObject.setLeft("cmplx_DEC_Decision",24);
                        formObject.setVisible("DecisionHistory_Label1",false);
                        formObject.setTop("DecisionHistory_Label11",444);
                        formObject.setLeft("DecisionHistory_Label11",330);
                        formObject.setVisible("DecisionHistory_Label3",true);
                        formObject.setTop("DecisionHistory_Label3",490);
                        formObject.setLeft("DecisionHistory_Label3",24);
                        formObject.setVisible("cmplx_DEC_Strength",true);
                        formObject.setTop("cmplx_DEC_Strength",507); 
                        formObject.setLeft("cmplx_DEC_Strength",24);
                        formObject.setVisible("DecisionHistory_Label4",true);
                        formObject.setTop("DecisionHistory_Label4",490); 
                        formObject.setLeft("DecisionHistory_Label4",330);
                        formObject.setVisible("cmplx_DEC_Weakness",true);
                        formObject.setTop("cmplx_DEC_Weakness",507); 
                        formObject.setLeft("cmplx_DEC_Weakness",330);
                        formObject.setVisible("DecisionHistory_Decision_Label4",true);
                        formObject.setTop("DecisionHistory_Decision_Label4",490); 
                        formObject.setLeft("DecisionHistory_Decision_Label4",650);
                        formObject.setVisible("cmplx_DEC_Remarks",true);
                        formObject.setTop("cmplx_DEC_Remarks",507); 
                        formObject.setLeft("cmplx_DEC_Remarks",650);
                        formObject.setVisible("DecisionHistory_Rejreason",false);
                        formObject.setTop("DecisionHistory_Rejreason",444); 
                        formObject.setLeft("DecisionHistory_Rejreason",650);
                        formObject.setVisible("cmplx_DEC_RejectReason",false);
                        formObject.setTop("cmplx_DEC_RejectReason",461); 
                        formObject.setLeft("cmplx_DEC_RejectReason",650);
                        formObject.setVisible("DecisionHistory_Combo4",true);
                        formObject.setVisible("DecisionHistory_Label10",true);
                        formObject.setVisible("DecisionHistory_Text6",true);*/
                        
                       // formObject.setVisible("DecisionHistory_Combo5",true);
                        
                    /*    formObject.setVisible("DecisionHistory_Button4",true);
                        formObject.setVisible("DecisionHistory_Frame2",true);
                        formObject.setVisible("DecisionHistory_Frame3",true);
                        formObject.setVisible("DecisionHistory_Label17",true);
                        formObject.setVisible("cmplx_DEC_LoanApprovalAuth",true);
                        formObject.setVisible("DecisionHistory_Label11",true);
                        formObject.setVisible("DecisionHistory_dec_reason_code",true);
                        formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
                        formObject.setVisible("DecisionHistory_Label27",false);
                        formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
                        formObject.setVisible("DecisionHistory_Decision_Label1",false);
                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);
                        formObject.setVisible("DecisionHistory_Label6",false);
                        formObject.setVisible("cmplx_DEC_IBAN_No",false);
                        formObject.setVisible("DecisionHistory_Label7",false);
                        formObject.setVisible("cmplx_DEC_NewAccNo",false);
                        formObject.setVisible("DecisionHistory_Label8",false);
                        formObject.setVisible("cmplx_DEC_ChequebookRef",false);
                        formObject.setVisible("DecisionHistory_Label9",false);
                        formObject.setVisible("cmplx_DEC_DCR_Refno",false);
                        formObject.setVisible("DecisionHistory_Label5",false);
                        formObject.setVisible("cmplx_DEC_Description",false);
                        formObject.setVisible("DecisionHistory_Label5",false);
                        formObject.setVisible("cmplx_DEC_Description",false);
                        formObject.setVisible("DecisionHistory_Label27",false);
                        formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
                        formObject.setVisible("DecisionHistory_Label10",false);
                        formObject.setVisible("cmplx_DEC_New_CIFID",false);
                        formObject.setVisible("DecisionHistory_Decision_Label1",false);
                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);*/
                   //     formObject.setEnabled("cmplx_DEC_ScoreGrade",true);
                        
                        
                        loadPicklist3();
                        
                        CreditCard.mLogger.info("***********Inside decision history");
                        
            			fragment_ALign("DecisionHistory_Frame2#\n#DecisionHistory_Label33,cmplx_DEC_TotalOutstanding#DecisionHistory_Label32,cmplx_DEC_TotalEMI#\n#DecisionHistory_Frame3#\n#DecisionHistory_Label17,cmplx_DEC_LoanApprovalAuth#DecisionHistory_Button4#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label14,cmplx_DEC_DectechDecision#\n#DecisionHistory_Label1,DecisionHistory_NewStrength#DecisionHistory_AddStrength#DecisionHistory_Label3,cmplx_DEC_Strength#\n#DecisionHistory_Label34,DecisionHistory_NewWeakness#DecisionHistory_AddWeakness#DecisionHistory_Label4,cmplx_DEC_Weakness#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
            			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
        				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
        				
            			CreditCard.mLogger.info("***********Inside after fragment alignment decision history");
            			//for decision fragment made changes 8th dec 2017
                        int rowsExposure = formObject.getLVWRowCount("cmplx_DEC_cmplx_gr_ExpoDetails");
                        int rowsDeviation = formObject.getLVWRowCount("cmplx_DEC_cmplx_gr_DeviaitonDetails");
                        if(rowsExposure!=0){
                        formObject.clear("cmplx_DEC_cmplx_gr_ExpoDetails");
                        }
                        setExposureGridData();
                        if(rowsDeviation!=0){
                        formObject.clear("cmplx_DEC_cmplx_gr_DeviaitonDetails");
                        }
                        setDeviationGridData();
                      //Code Change By aman to save Highest Delegation Auth
                		String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct with (nolock) where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
                		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
                		CreditCard.mLogger.info( "value is "+OutputXML.get(0).get(0));
                		CreditCard.mLogger.info( "value is ");
                		if(!OutputXML.isEmpty()){
                			String HighDel=OutputXML.get(0).get(0);
                			if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !"null".equalsIgnoreCase(OutputXML.get(0).get(0)) ){
                				{
                					CreditCard.mLogger.info( "value is ");	
                					formObject.setNGValue("cmplx_DEC_LoanApprovalAuth", HighDel);
                				}
                			}
                		}
                		formObject.RaiseEvent("WFSave");
						}//Code Change By aman to save Highest Delegation Auth
                        catch(Exception ex){
                        	CreditCard.mLogger.info(printException(ex));
                        }
				 }//loadPicklist1();
				 //below code added by nikhil
					else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("CustDetailVerification_Frame1",true);
						enable_custVerification();
						List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");

						LoadPicklistVerification(LoadPicklist_Verification);
						LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");

					}
					else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("BussinessVerification_Frame1",true);
						enable_busiVerification();
						
					}
					else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("HomeCountryVerification_Frame1",true);
						enable_homeVerification();
						
					}
					else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("ResidenceVerification_Frame1",true);
						enable_ResVerification();
						
					}
					else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("ReferenceDetailVerification_Frame1",true);
						enable_ReferenceVerification();
						
					}
					else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("OfficeandMobileVerification_Frame1",true);
						CreditCard.mLogger.info( "set visible OfficeandMobileVerification inside condition ");
						
						//enable_officeVerification();
						// added by abhishek to disable office verification
						formObject.setLocked("OfficeandMobileVerification_Frame1", true);
						//formObject.setEnabled("OfficeandMobileVerification_Enable", true);
						//-- Above code added by abhishek as per CC FSD 2.7.3
						//++Below code added by nikhil 13/11/2017 for Code merge
						LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
						loadPicklist_officeverification();
						//--Above code added by nikhil 13/11/2017 for Code merge
					}
					else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("LoanandCard_Frame1",true);
						enable_loanCard();
						
					}
	}
	
	


}

