
/*----------------------------------------------------------------------------------------------------------------

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

import com.newgen.omniforms.FormConfig;
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
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();												 
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		makeSheetsInvisible(tabName, "11,12,13,14,15,16,17");
		
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();												 

		try{
			//Deepak below commented to show CPV table in CA1 stage for all cases PCAS-2447
/*			if("N".equalsIgnoreCase(formObject.getNGValue("CPV_REQUIRED"))){
				formObject.setSheetVisible(tabName,8,false);
			}*/
			//below code added by nikhil PCSP-23 14th Jan
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_CreditCard.getGlobalVar("CSM_Frame_Name"));
			//below code added by nikhil for PCSP-504 Sprint-2
			formObject.setNGValue("Is_CAM_generated","N");
           new CC_CommonCode().setFormHeader(pEvent);
           CreditCard.mLogger.info( "@sagarika CAD2");
           enable_CPV();
        }catch(Exception e)
        {
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
        }
		CheckforRejects("CAD_Analyst2");
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
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

				case FRAME_EXPANDED:
				//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);						
					break;
					
					  
					case FRAGMENT_LOADED:
					//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
					  fragment_loaded(pEvent,formObject);
					  break;
					  
					  
					case MOUSE_CLICKED:
				//		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().mouse_clicked(pEvent);
						break;
					
					 case VALUE_CHANGED:
					//		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new CC_CommonCode().value_changed(pEvent);
							 break;
					default: break;
					
				}
	}


	public void setDeviationGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		//String query = "select Deviation_Code_Refer from ng_rlos_IGR_Eligibility_CardProduct where Deviation_Code_Refer is not null and Child_Wi like '"+wi_name+"'";
		//Changed by shivang fro PCASP-2611
		String query="select distinct case when Decision='Declined' then Declined_Reason  when Decision='Refer' then Deviation_Code_Refer when Decision='Approve' then Deviation_Code_Refer end as Deviation,Decision from ng_rlos_IGR_Eligibility_CardProduct with (nolock) where  Child_Wi ='"+wi_name+"' order by Decision asc";
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
		//Changed by shivang for PCASP-2611
		String query =	"select LoanType as Scheme,Liability_type,AgreementId as Agreement,isnull(TotalOutstandingAmt,isnull(OutstandingAmt,0)) as OutBal,isnull(PaymentsAmt,0) as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+wi_name+"' and LoanStat not in ('Pipeline','CAS-Pipeline','C','CLOSED') union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,isnull(TotalOutstandingAmt,isnull(OutstandingAmt,0)) as OutBal,isnull(PaymentsAmount,0) as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_Wi = '"+wi_name+"' and CardStatus not in ('Pipeline','CAS-Pipeline','C') and custroleType!='Secondary'  union select LoanType as Scheme,Liability_type,AgreementId as Agreement,isnull(OutstandingAmt,'0') as OutBal,isnull(PaymentsAmt,'0') as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+wi_name+"' and ProviderNo != 'B01' and LoanStat not in  ('Pipeline','Closed') union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,isnull(CurrentBalance,'0') as OutBal,isnull(PaymentsAmount,0) as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_Wi = '"+wi_name+"' and ProviderNo != 'B01' and CardStatus not in  ('Pipeline','Closed') and custRoleType!='Secondary' union select (select Description from NG_MASTER_contract_type with (nolock) where code = TypeOfContract) as Scheme,'' as Liability_type,'' as Agreement,isnull(OutstandingAmt,0) as OutBal,isnull(cast(EMI as float),0),Consider_For_Obligations  from ng_rlos_gr_LiabilityAddition with (nolock) where LiabilityAddition_wiName = '"+wi_name+"' union select accttype as scheme,acctId,providerNo,isnull(outstandingbalance,0),isnull(paymentsamount,0),Consider_For_Obligations from ng_rlos_cust_extexpo_AccountDetails with (nolock) where  child_wi = '"+wi_name+"' and ProviderNo != 'B01' and AcctStat not in  ('Pipeline','Closed') union select 'Overdraft' as scheme,account_type,AcctId,isnull(sanctionlimit,0),0,consider_for_obligations from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi= '"+wi_name+"' and  ODType != '' and CustRoleType='Main'";
		CreditCard.mLogger.info("Query for exposure grid is: "+query);
		List<List<String>> record = formObject.getDataFromDataSource(query);
		CreditCard.mLogger.info("Query data is: "+record);

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
				if(row.get(3)!=null && !row.get(3).equalsIgnoreCase("null") && !"".equals(row.get(3))){
				totalOut += Float.parseFloat(row.get(3));
				}
				if(row.get(4)!=null && !row.get(4).equalsIgnoreCase("null") && !"".equals(row.get(3))){
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
		formObject.setNGValue("cmplx_DEC_TotalOutstanding","-");
		formObject.setNGValue("cmplx_DEC_TotalEMI", "-");
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
		CustomSaveForm();
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
		try{
			CreditCard.mLogger.info("inside submitformcompleted: ");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			
			
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
				 List<Object> objOutput=new ArrayList<Object>();
					
					objOutput.add("Text");
				 CreditCard.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3)+objInput.get(4));
				 if ("".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))|| "Select".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))){
					 objOutput=formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput,objOutput);
						}
				 }
			 //below code added by nikhil
			 //Deepak changes done for PCAS-3523
			 if ("Approve".equalsIgnoreCase(Decision)){
				 String PrimLimitUpdateQuery="update ng_rlos_gr_cardDetailsCRN set ng_rlos_gr_cardDetailsCRN.final_limit=cardgrid.final_limit from ng_rlos_gr_cardDetailsCRN CRNGrid inner join ng_rlos_IGR_Eligibility_CardLimit cardgrid on CRNGrid.CRN_winame=cardgrid.Child_Wi and CRNGrid.Card_Product=cardgrid.Card_Product and CRNGrid.applicantType='Primary' and  CRNGrid.CRN_winame='"+formObject.getWFWorkitemName()+"'";
				 CreditCard.mLogger.info("PrimLimitUpdateQuery: "+ PrimLimitUpdateQuery);
				 formObject.saveDataIntoDataSource(PrimLimitUpdateQuery);
				 String SuppLimitUpdateQuery="update ng_rlos_gr_cardDetailsCRN set ng_rlos_gr_cardDetailsCRN.final_limit=suppgrid.approvedLimit from ng_rlos_gr_cardDetailsCRN CRNGrid inner join ng_RLOS_GR_SupplementCardDetails suppgrid on CRNGrid.CRN_winame=suppgrid.supplement_Winame and CRNGrid.Card_Product=suppgrid.CardProduct and CRNGrid.passportNo=suppgrid.PassportNo  and  CRNGrid.applicantType='Supplement' and CRNGrid.CRN_winame='"+formObject.getWFWorkitemName()+"'";
				 CreditCard.mLogger.info("PrimLimitUpdateQuery: "+ SuppLimitUpdateQuery);
				 formObject.saveDataIntoDataSource(SuppLimitUpdateQuery);
				 String SelfLimitUpdateQuery="update ng_rlos_gr_cardDetailsCRN set final_Limit=(select SelfSupp_Limit from NG_RLOS_CardDetails where winame='"+formObject.getWFWorkitemName()+"') where CRN_winame='"+formObject.getWFWorkitemName()+"' and applicantType='Supplement' and passportNo=(select passportNo from ng_rlos_customer where wi_name='"+formObject.getWFWorkitemName()+"')";
				 CreditCard.mLogger.info("PrimLimitUpdateQuery: "+ SelfLimitUpdateQuery);
				 formObject.saveDataIntoDataSource(SelfLimitUpdateQuery);
			 }
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		} 
	}


	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          
insert
	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			
			//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("CAD_Analyst2_Dec", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("Mail_CC", formObject.getUserName());
			saveIndecisionGrid();
			//formObject.setNGValue("VAR_STR6", formObject.getUserName());
			//below code added by nikhil for PCSP-460
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				CreditCard.mLogger.info("submitFormStarted CAD 2 Refer desc");
				//formObject.setNGValue("ReferTo", formObject.getNGValue("DecisionHistory_ReferTo"));
				CreditCard.mLogger.info("submitFormStarted CAD 2 ReferTo value : "+ formObject.getNGValue("ReferTo"));
				formObject.setNGValue("CAD2_User", formObject.getUserName());

			}
			CustomSaveForm();
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
				if("NA".equalsIgnoreCase(formObject.getNGValue("CADList")))
				{
					formObject.setNGValue("q_Hold2", 1);
				}
				formObject.setNGValue("IS_CA", "N");
				CreditCard.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
				//Deepak Changes done for PCAS-2926
				
			}
			if("Re-Initiate".equalsIgnoreCase(formObject.getNGValue("Reject_DeC"))){
				formObject.setNGValue("Reject_DeC","");
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
					formObject.setNGValue("IS_CA", "N");
				}
			}
			CreditCard.mLogger.info("Outside NA 2");
			formObject.setNGValue("CAD_dec_tray", "Select");
			if("NA".equalsIgnoreCase(formObject.getNGValue("cadlist")) && ("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))))
			{
				formObject.setNGValue("q_Hold2", 1);
			}
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) && "DDVT_Maker".equalsIgnoreCase(formObject.getNGValue("DecisionHistory_ReferTo") ))
			{
				formObject.setNGValue("q_hold1",1);
				formObject.setNGValue("VAR_INT3",1);

				formObject.setNGValue("CA_Refer_DDVT", "Y");
			}
			if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_CustDetailVerification_Decision")) && !"C".equalsIgnoreCase("IS_Approve_Cif") && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setNGValue("IS_Approve_Cif", "Y");
			}
			else
			{
				formObject.setNGValue("IS_Approve_Cif", "N");
			}
			//++ above code aded by disha on 27-3-2018 to set value of var_int3
			LoadReferGrid();
			////formObject.setNGValue("cmplx_DEC_Remarks","");

			//Done  by aman for Sprint 2
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%Cad_analyst%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + FCURefer);
			
			//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + squeryloan);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
				}
			if("FPU".equalsIgnoreCase(referto))
			{		
				formObject.setNGValue("RefFrmCAD", "Y");
			}
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_SetReminder_CA")) && "CA_HOLD".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				SetReminder_CA(formObject);
			}

		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormStarted"+e.getStackTrace());
		} 
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
		CreditCard.mLogger.info("Inside CC_CAD2-->frag loaded() for "+pEvent.getSource().getName());
				if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
					//formObject.setLocked("Customer_Frame1",true);
					LoadView(pEvent.getSource().getName());
					formObject.setLocked("cmplx_Customer_DOb", true);
					formObject.setLocked("cmplx_Customer_IdIssueDate", true);
					formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
					formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
					formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
					formObject.setLocked("cmplx_Customer_VisaExpiry", true);
					formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
					formObject.setLocked("cmplx_Customer_VisaIssueDate",true);
						loadPicklistCustomer();
					formObject.setNGValue("CAD_dec_tray", "Select");
					formObject.setLocked("cmplx_Customer_SecNationality", true);
					formObject.setLocked("SecNationality_Button", true);
					formObject.setLocked("",true);
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
					/*LoadPickList("cmplx_IncomeDetails_Account_num_sal", "select Acctid as Account_ID from  ng_RLOS_CUSTEXPOSE_AcctDetails where child_wi ='"+formObject.getWFWorkitemName()+"' and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' order by case when accttype='CURRENT ACCOUNT' then '1'  when accttype='FAST SAVER' then '2' else accttype end asc");
					LoadPickList("cmplx_IncomeDetails_Account_self_num", "select Acctid as Account_ID from  ng_RLOS_CUSTEXPOSE_AcctDetails where  child_wi ='"+formObject.getWFWorkitemName()+"' and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' order by case when accttype='CURRENT ACCOUNT' then '1'  when accttype='FAST SAVER' then '2' else accttype end asc");
					*/formObject.setLocked("IncomeDetails_Text2",true);
					formObject.setVisible("IncomeDetails_Label22",false);
					formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2",false);
					/*formObject.setVisible("IncomeDetails_ListView1",true);
					formObject.setVisible("IncomeDetails_Button1",true);
					formObject.setVisible("IncomeDetails_Button2",true);
					formObject.setVisible("IncomeDetails_Button3",true);*/
					formObject.setLocked("IncomeDetails_BankStatFromDate",true);
					formObject.setLocked("IncomeDetails_BankStatToDate",true);
					/// Added by Rajan for PCASP-2123
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BPA") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried") )
					{
						formObject.setVisible("IncomeDetails_Label22", false);
						formObject.setVisible("cmplx_IncomeDetails_Account_num_sal",false);
					}
				}
				//added by yash for CC FSD
				else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					loadPicklist_CompanyDet();
					formObject.setLocked("CompanyDetails_Frame1",true);
					//formObject.setEnabled("CompanyDetails_Frame1",false);
					formObject.setLocked("CompanyDetails_Frame2",true);
					//formObject.setEnabled("CompanyDetails_Frame2",false);
					formObject.setLocked("CompanyDetails_Frame3",true);
					formObject.setEnabled("CompanyDetails_Frame3",false);
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
					formObject.setEnabled("CompanyDetails_Button3", false); // Added by Rajan for PCASP- 1609
					

					//formObject.setLocked("CompanyDetails_Text1",false);
					/*formObject.setLocked("CompanyDetails_Text2",true);
					formObject.setLocked("CompanyDetails_Text3",true);
					formObject.setLocked("CompanyDetails_Text4",true);
					formObject.setLocked("CompanyDetails_Text5",true);*/
					//formObject.setVisible("CompanyDetails_Label8",false);
					//formObject.setLocked("estbDate",false);
					//formObject.setEnabled("CompanyDetails_DatePicker1",true);
					//formObject.setLocked("TLIssueDate",false);
					//formObject.setLocked("TLExpiry",false);
					//formObject.setLocked("estbDate",false);
					//formObject.setLocked("CompanyDetails_EffectiveLOB",false);
					/*
					formObject.setLocked("CompanyDetails_Frame3",true);
					formObject.setLocked("PartnerDetails_Dob",true);
					formObject.setLocked("CompanyDetails_Frame2",true);
					formObject.setLocked("AuthorisedSignDetails_DOB",true);
					formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",true);
					formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",true);
					formObject.setEnabled("CompanyDetails_Save",false);*/
				}
				/*//added by yash fro CC FSD
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
					
				}*/
				//added by yash for CC FSD
				else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
					if(("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2))))
							{
						formObject.setVisible("ExtLiability_Takeoverindicator", false); //Added by Rajan
						formObject.setVisible("ExtLiability_takeoverAMount", false);//Added by Rajan
						formObject.setVisible("ExtLiability_QCAmt", false);//Added by Rajan
						formObject.setVisible("ExtLiability_QCEMI",false);//Added by Rajan
						formObject.setVisible("ExtLiability_CACIndicator",false);//Added by Rajan
						formObject.setVisible("ExtLiability_Label23",false);//Added by Rajan
						formObject.setVisible("ExtLiability_Label26" , false); //Added by Rajan
						formObject.setVisible("ExtLiability_Label20",false);// Added by Rajan
						formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail_cont",false);
						formObject.setVisible("Liability_New_Label11",false);
						//Added by shivang for pcasp-1515
						formObject.setVisible("ExtLiability_Label15",true);
						formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
						formObject.setLocked("ExtLiability_Label15",true);
						formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
							}
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
					
					//Below code commented by shivang for hiding Aggregate Exposure
					//formObject.setVisible("ExtLiability_Label15",true);
					//formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
					
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
					//below code added by nikhil for PCSP-247
					if("Self Employed".equalsIgnoreCase( formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))
					{
						formObject.setVisible("cmplx_Liability_New_DBR",false);
						formObject.setVisible("ExtLiability_Label9",false);
						formObject.setVisible("cmplx_Liability_New_TAI",false);
						formObject.setVisible("ExtLiability_Label25",false);
						formObject.setVisible("cmplx_Liability_New_DBRNet",false);
						formObject.setVisible("ExtLiability_Label14",false);
						formObject.setVisible("Liability_New_Label6",true);
						formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
						formObject.setVisible("Liability_New_Label8",true);
						formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
						formObject.setVisible("Liability_New_Label7",true);
						formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
						//below code added by nikhil for PCSP-138
						if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,22).equals("CAC" ) ){
							formObject.setVisible("Liability_New_Label12",true);
							formObject.setVisible("cmplx_Liability_New_CACBankName",true);
						}
						formObject.setVisible("Liability_New_Label14",true);
						formObject.setVisible("cmplx_Liability_New_Aecb_Score_Company",true);
						formObject.setVisible("Liability_New_Label15",true);
						formObject.setVisible("cmplx_Liability_New_Aecb_range_Company",true);
					}
					if(("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2))))
					{
						formObject.setVisible("Liability_New_Label6",false);
						formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",false);
						formObject.setVisible("Liability_New_Label8",false);
						formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",false);
						formObject.setVisible("Liability_New_Label7",false);
						formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",false);
						formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail_cont",false);
						formObject.setVisible("Liability_New_Label11",false);
					}
					//Done for PCSP-348 by aman
					formObject.setLocked("ExtLiability_AECBReport",false);
					//Done for PCSP-348 by aman
					
				}
				
				
				/*else if ("Internal Liabilities".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExtLiability_Frame2",true);
					
				}
				else if ("External Liabilities".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExtLiability_Frame3",true);
					
					
				}
				cmplx_EmploymentDetails_NepType
				else if ("Liability Addition".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExtLiability_Frame4",true);		
				}*/
				//added by yash for CC FSD
				else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//formObject.setLocked("EMploymentDetails_Frame1", true);
					LoadView(pEvent.getSource().getName());
					//Added by shivang for PCASP-887
					if("SAL".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))//sagarika for PCASP-2348
					{
					formObject.setVisible("EMploymentDetails_Label64", true);
					formObject.setVisible("cmplx_EmploymentDetails_OtherDesign", true);
					formObject.setLocked("EMploymentDetails_Label64", true);
					formObject.setLocked("cmplx_EmploymentDetails_OtherDesign", true);
					}
					else
					{
					formObject.setVisible("EMploymentDetails_Label64", false);
					formObject.setVisible("cmplx_EmploymentDetails_OtherDesign", false);
				
					}//pcasp-2348 sagarika
					//below code commented by nikhil for PCSP-482
					//formObject.setEnabled("EMploymentDetails_Frame1", false);
					loadPicklist4();
					formObject.setVisible("EMploymentDetails_Label19",false);
					formObject.setVisible("cmplx_EmploymentDetails_funded_faci",false);
					formObject.setVisible("EMploymentDetails_Label16",false);
					formObject.setVisible("cmplx_EmploymentDetails_borrowing_rel",false);
					formObject.setVisible("EMploymentDetails_Label11",false);
					formObject.setVisible("cmplx_EmploymentDetails_head_office",false);
					/*formObject.setVisible("EMploymentDetails_Label32",true);//added by akshay on 26/5/18 for proc 9811
					formObject.setVisible("cmplx_EmploymentDetails_FieldVisitDone",true);*/
					formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
					formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
					if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
					{
						formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
						formObject.setVisible("EMploymentDetails_Label15", true);
						formObject.setVisible("EMploymentDetails_Label59", true);//Added by Rajan
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);// Added by Rajan
						//changed by shivang for PCASP-1511
						formObject.setLocked("cmplx_EmploymentDetails_EmployerType", true);
						
						//changed by shivang for PCASP-1533
						if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_EmploymentDetails_IncInPL").equalsIgnoreCase("false")){
							formObject.setVisible("EMploymentDetails_Label41", true);
							formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
							formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
						}else{
							formObject.setVisible("EMploymentDetails_Label41", false);
							formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", false);
							
						}
					}
					else
					{
						formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
						formObject.setVisible("EMploymentDetails_Label15", false);
					}
					//Added by shivang for PCASP-1823
					String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
					if(("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))||"SAL".equals(subProd))&& 
							!subProd.equalsIgnoreCase("SEC") && !subProd.equalsIgnoreCase("BPA") && !subProd.equalsIgnoreCase("PA") && !subProd.equalsIgnoreCase("IM")){
						if((formObject.getNGValue("cmplx_EmploymentDetails_IncInCC").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_EmploymentDetails_IncInPL").equalsIgnoreCase("false"))
								|| (formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer").equalsIgnoreCase("CN")
									&& (formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("OP")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("RS"))
									&& (formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("CN")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("--Select--")))
								||(subProd.equalsIgnoreCase("SAL") && formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer").equalsIgnoreCase("CN") && 
										((formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("NM")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("CN")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("")) 
												&& (formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("NM")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("CN")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("--Select--"))))){
							formObject.setVisible("EMploymentDetails_Label41", true);
							formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
							//formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", false);
						}else{
							formObject.setVisible("EMploymentDetails_Label41", true);
							formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
							formObject.setNGValue("cmplx_EmploymentDetails_LengthOfBusiness", "");
							formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
						}
					}
					if(subProd.equalsIgnoreCase("IM")){
						if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_EmploymentDetails_IncInPL").equalsIgnoreCase("false"))
							{					
							CreditCard.mLogger.info("@Shivang ::Employment type :: "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
							formObject.setVisible("EMploymentDetails_Label41", true);
							formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
							formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
						}else{
							formObject.setVisible("EMploymentDetails_Label41", false);
							formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", false);
						}
					}
					if("BPA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
					{
			          formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
					}//PCASP-2479
					
				}
				else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
					//added 6th sept for proc 1926
					// ++ below code not commented at offshore - 06-10-2017
					LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
					formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					formObject.setEnabled("ELigibiltyAndProductInfo_Frame1", false);
					if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
					{
					formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);//total final limit
					formObject.setLocked("ELigibiltyAndProductInfo_Button1", false);//calculate re-eligibilit
					LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
					
					LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
				
					//loadPicklist_Address();
					//formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					}
					//ended 6th sept for proc 1926
					if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
					{
						formObject.setVisible("ELigibiltyAndProductInfo_Label9", true);
						formObject.setVisible("cmplx_EligibilityAndProductInfo_Max_threshold_exposure", true);
					}
				}
				else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					loadPicklist_Address();
					//formObject.setLocked("AddressDetails_Frame1",true);
					LoadView(pEvent.getSource().getName());
		
				}
				else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
					//change by saurabh on 7th Dec
				//	LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
					//change by saurabh for air arabia functionality.
					AirArabiaValid();
					enrollrewardvalid();//added by saurabh1 for enroll
				//	formObject.setLocked("AltContactDetails_Frame1",true);
					LoadView(pEvent.getSource().getName());
				
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
					formObject.setEnabled("FATCA_Frame6", false);
					formObject.setLocked("FATCA_SignedDate",true);
					formObject.setLocked("FATCA_ExpiryDate",true);
					loadPicklist_Fatca();
					
				}
				else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//++below code added by nikhil for Self-Supp CR
					Load_Self_Supp_Data();
					//--above code added by nikhil for Self-Supp CR
					//loadPicklist_Address();
					formObject.setLocked("CardDetails_Frame1",true);
					//LoadPickList("CardDetails_BankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
					formObject.setVisible("CardDetails_Label13", false);
					formObject.setVisible("cmplx_CardDetails_CustClassification", false);
					Loadpicklist_CardDetails_Frag();
				}
				else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("SupplementCardDetails_Frame1",true);
					formObject.setEnabled("SupplementCardDetails_Frame1", false);
					loadPicklist_suppCard();
				}
				
				else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
					loadPicklist_KYC();
					formObject.setLocked("KYC_Frame1",true);
					//formObject.setLocked("KYC_Frame7",true);
					
				}
				else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
					loadPicklist_oecd();
					//formObject.setLocked("OECD_Frame8",true);
					LoadView(pEvent.getSource().getName());
					
				}
				
				/*else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("Reference_Details_Frame1",true);
					
				}*/ //Commented for sonar
				
				
				else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("PartMatch_Frame1",true);
					formObject.setEnabled("PartMatch_Frame1", false);
					formObject.setLocked("PartMatch_Dob",true);
					//sLoadPickList("PartMatch_nationality","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock) order by code"); //Arun (10/10)
						}
				else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("FinacleCRMIncident_Frame1",true);
					
				}
				else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					//Deepak changes done for PCSP-83 if no data is present in FinacleCRMCustInfo then to load from DB.
					loadinFinacleCRNGrid(formObject);
					formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
					
				}
				else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("ExternalBlackList_Frame1",true);
					
				}
				else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
					LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union all select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");
					formObject.addComboItem("FinacleCore_acctypeIn", "Individual_CIF", "Individual_CIF");
					formObject.addComboItem("FinacleCore_acctypeIn", "Corporate_CIF", "Corporate_CIF");
					formObject.addComboItem("FinacleCore_TypeOfRetutn", "ICCS", "ICCS");
					formObject.addComboItem("FinacleCore_TypeOfRetutn", "DDS", "DDS");
					if("SEC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) || ("BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) && "SMES".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4))))
					{
			formObject.setVisible("FinacleCore_Label31", true);
			formObject.setVisible("cmplx_FinacleCore_Account_FD", true);
			//formObject.setVisible("FinacleCore_Cal_FD_Amount", true);
			formObject.setVisible("FinacleCore_Label27", true);
			formObject.setVisible("cmplx_FinacleCore_Final_FD_Amount", true);
			String Lien_Grid = "cmplx_FinacleCore_liendet_grid";
			for(int i=0;i<formObject.getLVWRowCount(Lien_Grid);i++)
			{
				if("true".equals(formObject.getNGValue(Lien_Grid,i,7)))
				{
					formObject.setNGValue("cmplx_FinacleCore_Account_FD", formObject.getNGValue(Lien_Grid,i,0));
					formObject.setNGValue("cmplx_FinacleCore_Final_FD_Amount", formObject.getNGValue(Lien_Grid,i,2));
					break;
				}
			}
					}
					formObject.setLocked("FinacleCore_Frame1",true);
					//added by saurabh on13th Oct for JIRA-2517
					formObject.setLocked("FinacleCore_Frame9", true);
					formObject.setLocked("FinacleCore_ReturnDate", true);
					formObject.setLocked("cmplx_FinacleCore_edit_date_avg_nbc", true);
					formObject.setLocked("cmplx_FinacleCore_edit_date_turn_statistics", true);
					formObject.setLocked("InwardTT_date", true);
					//Added by Rajan for PCASP-1926 for cheque DDs return date
					formObject.setNGValue("FinacleCore_TypeOfRetutn", formObject.getNGValue("cmplx_FinacleCore_DDSgrid_table",0,8));
					formObject.setNGValue("FinacleCore_acctypeIn", formObject.getNGValue("cmplx_FinacleCore_DDSgrid_table",0,0));					
					
					
				}
				
				else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					loadPicklist_Mol();
					formObject.setLocked("MOL1_Frame1",true);
					formObject.setLocked("cmplx_MOL_molexp",true);
					formObject.setLocked("cmplx_MOL_molissue",true);
					formObject.setLocked("cmplx_MOL_ctrctstart",true);
					formObject.setLocked("cmplx_MOL_ctrctend",true);
					
					
					
					
					
				}
				else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("WorldCheck1_Frame1",true);
					formObject.setLocked("WorldCheck1_Dob",true);
					formObject.setLocked("WorldCheck1_entdate",true);
					formObject.setLocked("WorldCheck1_upddate",true);
					
					
					
					
				}
				else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("IncomingDocument_Frame",false);
					formObject.setEnabled("IncomingDocument_Save", false);
				}
				//added by yash for CC FSD
				else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
					//formObject.setLocked("SmartCheck_Frame1",true);
					//formObject.setVisible("SmartCheck1_Label2",false);
					formObject.setLocked("SmartCheck1_CPVRemarks",true);
					//formObject.setVisible("SmartCheck1_Label4",false);
					formObject.setLocked("SmartCheck1_FCURemarks",true);
					formObject.setLocked("SmartCheck1_Modify",true);
				}
				
				else if("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())){
					formObject.setLocked("SmartCheck_Frame1",true);
					 formObject.setVisible("SmartCheck1_Label2",false);
					 formObject.setVisible("SmartCheck1_CPVRemarks",false);
					 formObject.setVisible("SmartCheck1_Label4",false);
					 formObject.setVisible("SmartCheck1_FCURemarks",false);
				}
				
				else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("RejectEnq_Frame1",true);
					//Added by Shivang for PCASP-1390
					formObject.setLocked("RejectEnq_Save",true);
					
				}
				else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
					//loadPicklist_Address();
					formObject.setLocked("SalaryEnq_Frame1",true);
					//Added by Shivang for PCASP-1390
					formObject.setLocked("SalaryEnq_Button1",true);
					
				}
				/*if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
					//loadPicklist_Address();
					formObject.setLocked("ExternalBlackList_Frame1",true);
					
				}*/
				
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
					formObject.setEnabled("CC_Loan_Frame1",false);
					loadPicklist_ServiceRequest();
					
				}
				//added by yash for CC FSD
				else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					//notepad_withoutTelLog();
					notepad_load();
					//change done by nikhil for PCAS-2356
					formObject.setVisible("NotepadDetails_Frame3",true);
					formObject.setLocked("NotepadDetails_Frame3",true);
			        
					//formObject.setTop("NotepadDetails_savebutton",410);
				}
				//ended by yash
				else if ("Fraud Control Unit".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
				}
//added by yash for CC FSD
				else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
						try{
							loadPicklist3();
                        if(!formObject.isVisible("ExtLiability_Frame1")){
            				formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
            				}
                        CreditCard.mLogger.info("***********Inside decision history");
                        
            			fragment_ALign("DecisionHistory_Frame2#\n#DecisionHistory_Label33,cmplx_DEC_TotalOutstanding#DecisionHistory_Label32,cmplx_DEC_TotalEMI#\n#DecisionHistory_Frame3#\n#DecisionHistory_Label17,cmplx_DEC_LoanApprovalAuth#DecisionHistory_Button4#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label14,cmplx_DEC_DectechDecision#\n#DecisionHistory_Label1,DecisionHistory_NewStrength#DecisionHistory_AddStrength#DecisionHistory_Label3,cmplx_DEC_Strength#\n#DecisionHistory_Label34,DecisionHistory_NewWeakness#DecisionHistory_AddWeakness#DecisionHistory_Label4,cmplx_DEC_Weakness#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Label42,cmplx_DEC_SetReminder_CA#DecisionHistory_Label43,cmplx_DEC_NoOfAttempts_CA#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
            			formObject.setWidth("DecisionHistory_dec_reason_code", 210);
            			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
        				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
        				formObject.setLocked("cmplx_DEC_Strength",true);
        				formObject.setLocked("cmplx_DEC_Weakness",true);
        				formObject.setLocked("cmplx_DEC_SetReminder_CA",true);
        				formObject.setLocked("cmplx_DEC_NoOfAttempts_CA",true);
        				
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
                		formObject.setNGValue("CAD_dec_tray", "Select");
                		//below code added by nikhil for PCSP-522
                		if("".equalsIgnoreCase(formObject.getNGValue("CAD_Analyst2_Dec")) && "Refer".equalsIgnoreCase(formObject.getNGValue("CAD_ANALYST1_DECISION")))
                		{
                			formObject.setNGValue("cmplx_DEC_Decision", "Refer");
                		}
                		//formObject.RaiseEvent("WFSave");
						}//Code Change By aman to save Highest Delegation Auth
                        catch(Exception ex){
                        	CreditCard.mLogger.info(printException(ex));
                        }
				 }//loadPicklist1();
				 //below code added by nikhil
					else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						
						
						//done by nikhil for PCAS-2358
						LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
						List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");
						LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
						LoadPicklistVerification(LoadPicklist_Verification);
						  formObject.setVisible("CustDetailVerification_Label10", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);   //Added by Rajan   
												
						if("IM".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
						{
						formObject.setVisible("CustDetailVerification_Label7", false);// Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_persorcompPOBox_val",false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_persorcompPOBox_ver", false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_persorcompPOBox_upd", false);//Added by Rajan
						formObject.setVisible("CustDetailVerification_Label8", false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_Offtelno_val", false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_offtelno_ver", false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_offtelno_upd", false);//Added by Rajan
	                    formObject.setVisible("CustDetailVerification_Label19", true);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_mother_name_val", true);// Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_mother_name_ver", true);// Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_mother_name_upd", true);// Added by Rajan
						formObject.setVisible("CustDetailVerification_Label12", false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_email2_val", false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_email2_ver", false);//Added by Rajan
						formObject.setVisible("cmplx_CustDetailVerification_email2_upd", false);//Added by Rajan
						
						
						}
						if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"LI".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"PU".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"PULI".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"SEC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
						{
							formObject.setVisible("CustDetailVerification_Label3",false);//Added by Rajan
							formObject.setVisible("cmplx_CustDetailVerification_Mob_No2_val", false);//Added by Rajan
							formObject.setVisible("cmplx_CustDetailVerification_mobno2_ver", false);//Added by Rajan
							formObject.setVisible("cmplx_CustDetailVerification_mobno2_upd", false);//Added by Rajan
							formObject.setVisible("CustDetailVerification_Label12", false);//Added by Rajan
							formObject.setVisible("cmplx_CustDetailVerification_email2_val", false);//Added by Rajan
							formObject.setVisible("cmplx_CustDetailVerification_email2_ver", false);//Added by Rajan
							formObject.setVisible("cmplx_CustDetailVerification_email2_upd", false);//Added by Rajan
							formObject.setVisible("CustDetailVerification_Label17", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_Resno_val", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_resno_ver", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_resno_upd", false);//Added by Rajan
			                formObject.setVisible("CustDetailVerification_Label9", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_val", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_ver", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_upd", false);//Added by Rajan
			                formObject.setVisible("CustDetailVerification_Label10", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);//Added by Rajan
			                formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);   //Added by Rajan        
			                
						}
						//2481 jira
						/*formObject.setNGValue("cmplx_CustDetailVerification_POBoxNo_val", "NA");// Added by Rajan
						formObject.setNGValue("cmplx_CustDetailVerification_emirates_val", "NA");// Added by Rajan
						formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_val", "NA");// Added by Rajan
						if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) ||"SAL".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))|| "BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
						{
							formObject.setNGValue("cmplx_CustDetailVerification_POBoxno_ver", "NA");// Added by Rajan
							formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_ver", "NA");// Added by Rajan
							formObject.setNGValue("cmplx_CustDetailVerification_emirates_ver", "NA");// Added by Rajan
						}
						else
						{
							formObject.setNGValue("cmplx_CustDetailVerification_POBoxno_ver", "--Select--");// Added by Rajan
							formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_ver", "--Select--");// Added by Rajan
							formObject.setNGValue("cmplx_CustDetailVerification_emirates_ver", "--Select--");// Added by Rajan
						}*/
						
						enable_custVerification();
						if("PA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
						{
							formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
							formObject.setLocked("CustDetailVerification_Frame1",true);
						}
						formObject.setLocked("CustDetailVerification_Frame1",true);
					}
					else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("BussinessVerification_Frame1",true);
						enable_busiVerification();
						if(("BTC".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2))))
						{
							formObject.setVisible("cmplx_BussVerification_Office_Extension", false);// Added by Rajan
							formObject.setVisible("BussinessVerification_Label5", false); // Added by Rajan
						}	
						
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
						//nikhil code moved from frame expand event for PCAS-2239
						List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
						LoadPicklistVerification(LoadPicklist_Verification);
						LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
						//changed by nikhil for CPV changes 17-04
						LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
						//below code by saurabh on 28th nov 2017.
						LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
						LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
						LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_designation with (nolock) order by code");
						formObject.setLocked("OfficeandMobileVerification_Frame1",true);
						CreditCard.mLogger.info( "set visible OfficeandMobileVerification inside condition ");
						
						//enable_officeVerification();
						// added by abhishek to disable office verification
						formObject.setLocked("OfficeandMobileVerification_Frame1", true);
						if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
							{
						//Added by Rajan for SE CPV FLV
						formObject.setLocked("cmplx_OffVerification_fxdsal_override", true);
						formObject.setLocked("cmplx_OffVerification_accprovd_override", true);
						formObject.setLocked("cmplx_OffVerification_desig_override", true);
						formObject.setLocked("cmplx_OffVerification_doj_override", true);
						formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", true);
						formObject.setVisible("OfficeandMobileVerification_Label12", false);
			            formObject.setVisible("cmplx_OffVerification_offtelnovalidtdfrom", false);
			            formObject.setVisible("OfficeandMobileVerification_Label2", false);
			            formObject.setVisible("cmplx_OffVerification_hrdnocntctd", false);
			            formObject.setVisible("OfficeandMobileVerification_Label7",false);
			            formObject.setVisible("cmplx_OffVerification_hrdcntctno", false);
			            formObject.setVisible("OfficeandMobileVerification_Label25", false);
			            formObject.setVisible("cmplx_OffVerification_Office_Extension", false);
			            formObject.setVisible("OfficeandMobileVerification_Label3", false);
			            formObject.setVisible("cmplx_OffVerification_hrdcntctname", false);
			            formObject.setVisible("OfficeandMobileVerification_Label13", false);
			            formObject.setVisible("cmplx_OffVerification_hrdcntctdesig", false);
			            formObject.setVisible("OfficeandMobileVerification_Label4", false);
			            formObject.setVisible("cmplx_OffVerification_hrdemailverified", false);
			            formObject.setVisible("OfficeandMobileVerification_Label8", false);
			            formObject.setVisible("cmplx_OffVerification_hrdemailid", false);
			            formObject.setVisible("OfficeandMobileVerification_Label5", false);
			            formObject.setVisible("cmplx_OffVerification_colleaguenoverified", false);
			            formObject.setVisible("OfficeandMobileVerification_Label9", false);
			            formObject.setVisible("cmplx_OffVerification_colleagueno", false);
			            formObject.setVisible("OfficeandMobileVerification_Label15", false);
			            formObject.setVisible("OfficeandMobileVerification_Label21", false);
			            formObject.setVisible("OfficeandMobileVerification_Label22", false);
			            formObject.setVisible("OfficeandMobileVerification_Label23", false);
			            formObject.setVisible("OfficeandMobileVerification_Label26", false);
			            formObject.setVisible("OfficeandMobileVerification_Label16", false);
			            formObject.setVisible("cmplx_OffVerification_fxdsal_val", false);
			            formObject.setVisible("cmplx_OffVerification_fxdsal_ver", false);									
			            formObject.setVisible("cmplx_OffVerification_fxdsal_upd", false);	
			            formObject.setVisible("cmplx_OffVerification_fxdsal_override", false);	
			            formObject.setVisible("OfficeandMobileVerification_Label17", false);	
			            formObject.setVisible("cmplx_OffVerification_accprovd_val", false);	
			            formObject.setVisible("cmplx_OffVerification_accprovd_ver", false);	
			            formObject.setVisible("cmplx_OffVerification_accprovd_upd", false);	
			            formObject.setVisible("cmplx_OffVerification_accprovd_override", false);	
			            formObject.setVisible("OfficeandMobileVerification_Label18", false);	
			            formObject.setVisible("cmplx_OffVerification_desig_val", false);	
			            formObject.setVisible("cmplx_OffVerification_desig_ver", false);	
			            formObject.setVisible("cmplx_OffVerification_desig_upd", false);
			            formObject.setVisible("cmplx_OffVerification_OthDesign_Upt", false);
			            formObject.setVisible("cmplx_OffVerification_desig_override", false);	
			            formObject.setVisible("OfficeandMobileVerification_Label19", false);	
			            formObject.setVisible("cmplx_OffVerification_doj_val", false);	
			            formObject.setVisible("cmplx_OffVerification_doj_ver", false);
			            formObject.setVisible("cmplx_OffVerification_doj_upd", false);
			            formObject.setVisible("cmplx_OffVerification_doj_override", false);
			            formObject.setVisible("OfficeandMobileVerification_Label20", false);
			            formObject.setVisible("cmplx_OffVerification_cnfminjob_val", false);
			            formObject.setVisible("cmplx_OffVerification_cnfminjob_ver", false);
			            formObject.setVisible("cmplx_OffVerification_cnfminjob_upd", false);
			            formObject.setVisible("cmplx_OffVerification_cnfminjob_override", false);
			            formObject.setVisible("cmplx_OffVerification_accpvded_ver", false);
			            formObject.setVisible("cmplx_OffVerification_accpvded_upd", false);
			            formObject.setVisible("cmplx_OffVerification_cnfrminjob_upd", false);
			            formObject.setVisible("cmplx_OffVerification_cnfrminjob_val", false);
			            formObject.setVisible("cmplx_OffVerification_cnfrmjob_override", false);
					}
						//formObject.setEnabled("OfficeandMobileVerification_Enable", true);
						//-- Above code added by abhishek as per CC FSD 2.7.3
						//++Below code added by nikhil 13/11/2017 for Code merge
						/*LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
						loadPicklist_officeverification();*/
						//--Above code added by nikhil 13/11/2017 for Code merge
					}
					else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("LoanandCard_Frame1",true);
						enable_loanCard();
						
					}
					else if ("Fpu_Grid".equalsIgnoreCase(pEvent.getSource().getName())) {
						formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
						LoadPickList("cmplx_FPU_Grid_Officer_Name", "select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName='FCU'))");
					}
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

