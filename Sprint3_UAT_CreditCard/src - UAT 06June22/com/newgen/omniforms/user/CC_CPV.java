
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CPV

File Name                                                         : CC_CPV

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_CPV extends CC_Common implements FormListener
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
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
				
		makeSheetsInvisible(tabName, "11,12,13,14,15,16,17");
		
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
	    	 CreditCard.mLogger.info("Inside CC_Hold_CPV CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	    	 enable_CPV();
	        }catch(Exception e)
	        {
	            CreditCard.mLogger.info( "Exception:"+e.getMessage());
	        }
	     CheckforRejects("CPV");
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
		
		
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  //formObject =FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info(" Inside event disp CAD1: ");
		
		serverSide(pEvent);

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					new CC_CommonCode().FrameExpandEvent(pEvent);
									
					 		break;
					
						case FRAGMENT_LOADED:
								//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
								fragment_loaded(pEvent);
								
								
						
							
					
					  break;
					  
					case MOUSE_CLICKED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						
						new CC_CommonCode().mouse_clicked(pEvent);
						break;
						
						
				
					
					 case VALUE_CHANGED:
							//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new CC_CommonCode().value_changed(pEvent);
						break;	 
					
					 default: break;
					
				}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}


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
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		String Query ="Select CA_Refer_DDVT from NG_CC_EXTTABLE where CC_Wi_Name ='"+formObject.getWFWorkitemName()+"'";
		int count=0;
		try
		{
		//CreditCard.mLogger.info("query name :"+Query);
		List<List<String>> result=formObject.getDataFromDataSource(Query);
		if(!result.isEmpty()){
			if("Y".equalsIgnoreCase(result.get(0).get(0)))
			{
				count++;
			}
		}
		}
		catch(Exception ex)
		{
			CreditCard.mLogger.info("Exception occured in Check_CPV_Refer_DDVT" +ex.getMessage());
		}
		if(count>0)
		{
		String AlertMsg="Workitem Referred to DDVT by UW Unit!";
		throw new ValidatorException(new FacesMessage(AlertMsg));
		}
		
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
		//PCSP-459
		//commented by nikhil for PCAS-2814
		/*if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		{
		Other_Detail_Match_check();
		}*/
		try
		{
			//PCSP-459
			if( !"CPV Checker".equalsIgnoreCase(formObject.getNGValue("DecisionHistory_ReferTo")))
			{
				formObject.setNGValue("IS_CPV", "N");
			}
			formObject.setNGValue("CPV_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			//code changed for for PCSP - 78 - deepak
			if(formObject.isVisible("OfficeandMobileVerification_Frame1")==true && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				CreditCard.mLogger.info("Inside submitFormstarted to validate office verification");

				/*if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_upd")) )
				{
					formObject.setNGValue("cmplx_OffVerification_desig_upd", formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				}
				//commeted as the same is not in use any more.
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")))
			{
				formObject.setNGValue("cmplx_OffVerification_doj_upd", formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			}
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")))
				{
					formObject.setNGValue("cmplx_OffVerification_cnfrminjob_upd", formObject.getNGValue("cmplx_OffVerification_cnfrminjob_val"));
				}*/
				//added by nikhil top save office verification
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Office_Mob_Verification");
			}

			formObject.setNGValue("CPV_DECISION", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("Mail_Priority", formObject.getUserName());
			//formObject.setNGValue("VAR_STR4", formObject.getUserName());
			//commented by nikhil for CPV changes 17-04
			/*if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setNGValue("IS_Approve_Cif","Y");
			}*/
			if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				CreditCard.mLogger.info("Inside  Approve CPV");
				formObject.setNGValue("q_hold1",1);
				formObject.setNGValue("VAR_INT3",1);
				formObject.setNGValue("CPV_Analyst_dec", "");
				CreditCard.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
				//Deepak Changes done for PCAS-2926
				if("Re-Initiate".equalsIgnoreCase(formObject.getNGValue("Reject_DeC"))){
					formObject.setNGValue("Reject_DeC","");
				}
			}
					
			formObject.setNGValue("CPV_User", formObject.getUserName());
			//cmplx_CustDetailVerification_email1_ver
			
			CustomSaveForm();
			LoadReferGrid();
			saveIndecisionGrid();
			//Done By aman for Sprint2
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%CPV%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + FCURefer);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
				}
			if("FPU".equalsIgnoreCase(referto)){		
				formObject.setNGValue("RefFrmCPV", "Y");
			}
			
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_SetReminder")) && "CPV Hold".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){	String Email_sub="REMINDER- case no "+formObject.getWFWorkitemName()+" pending for the action of "+formObject.getUserName();
			//CreditCard.mLogger.info("Email_sub: "+ Email_sub);
			String Email_message="<html><body>Dear User, \n Kindly call the HR/office back. \n \n Remarks: "+formObject.getNGValue("cmplx_OffVerification_reamrks")+". \n Decision Remarks � "+formObject.getNGValue("cmplx_DEC_Remarks")+". </body></html>";
			//CreditCard.mLogger.info("Email_sub: "+ Email_message);
			String query = "select count(WI_Name) from NG_RLOS_EmailReminder with(nolock) where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CPV'";
			List<List<String>> Emailrecords = formObject.getDataFromDataSource(query);
			String emailTo = "select  ConstantValue from CONSTANTDEFTABLE where constantName='CONST_cpv_mailTo'";
			List<List<String>> mailTo = formObject.getNGDataFromDataCache(emailTo);
			String mailto_cpv=mailTo.get(0).get(0);
			//CreditCard.mLogger.info("@sagarika"+ " mail to" + mailto_cpv);
			String email_From = "select  ConstantValue from CONSTANTDEFTABLE where constantName='CONST_cpv_mailFrom'";
			List<List<String>> mail_From = formObject.getNGDataFromDataCache(email_From);
			String mail_from_cpv=mail_From.get(0).get(0);
			//CreditCard.mLogger.info("@sagarika"+ " mail fromssss" + mail_from_cpv);
			//CreditCard.mLogger.info("query NG_RLOS_EmailReminder count is: "+query);
			//CreditCard.mLogger.info("query NG_RLOS_EmailReminder count result: "+Emailrecords);
		
			try{
				String reminderInsertQuery="";
				if(Emailrecords!=null && Emailrecords.size()>0 && Emailrecords.get(0)!=null ){
					String EmailRow_count=Emailrecords.get(0).get(0);
					CreditCard.mLogger.info("query NG_RLOS_EmailReminder count EmailRow_count: "+EmailRow_count);
					if("0".equalsIgnoreCase(EmailRow_count)){
						reminderInsertQuery="insert into NG_RLOS_EmailReminder(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,SetReminder,WI_Name,Workstep_Name)"
								+" values('CPV','P','"+mailto_cpv+"','"+mail_from_cpv+"','"+Email_sub+"','"+Email_message+"','"+formObject.getNGValue("cmplx_DEC_SetReminder")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
					}
					else{
						reminderInsertQuery="update NG_RLOS_EmailReminder set Email_Status='P' where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CPV'";
					}
					//CreditCard.mLogger.info("Query to insert NG_RLOS_EmailReminder: "+ reminderInsertQuery);
					formObject.saveDataIntoDataSource(reminderInsertQuery);
				}
			}
			catch(Exception e){
				CreditCard.mLogger.info("qException occured in CPV reminder Email insert:"+query);
			}}
			
		}
		catch(Exception ex)
		{
			CreditCard.mLogger.info("Submit form Started exception CPV"+ex.getStackTrace());
		}
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent)
	{
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
		//	formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_VisaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
				loadPicklistCustomer();
			//setDisabled();
		}	
		
		if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			*/
			//code changed to load master's of product based on requested product.
			formObject.setLocked("Product_Frame1",true);
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
		}
		
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			loadpicklist_Income();}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
               formObject.setLocked("CompanyDetails_Frame1", true);
               formObject.setLocked("CompanyDetails_Frame2", true);
               formObject.setLocked("CompanyDetails_Frame3", true);
               formObject.setLocked("TLIssueDate", true);
               formObject.setLocked("TLExpiry", true);
               formObject.setLocked("estbDate", true);
               formObject.setLocked("AuthorisedSignDetails_DOB", true);
               formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate", true);
               formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate", true);
               formObject.setLocked("PartnerDetails_Dob",true);
               formObject.setLocked("cmplx_IncomeDetails_DurationOfBanking",true); //hritik ppct14
             //For PCASP-2530
               String subProduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
   			if(subProduct.equalsIgnoreCase("SE")|| subProduct.equalsIgnoreCase("BTC")){
   				formObject.setVisible("CompanyDetails_Label5", false);
   				formObject.setVisible("PromotionCode", false);
   			}
         
/*				               LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
               LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
               LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMacro with (nolock)");
               LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMicro with (nolock)");
               LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
               LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
               LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
               LoadPickList("EOW", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
               LoadPickList("headOffice", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
*/								loadPicklist_CompanyDet();
	formObject.setLocked("CompanyDetails_Save", true);
        }
		
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			//formObject.setLocked("AddressDetails_Frame1",true);
			loadPicklist_Address();
		}
		
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			//formObject.setLocked("AltContactDetails_Frame1",true);
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			//change by saurabh for air arabia functionality.
			AirArabiaValid();
			enrollrewardvalid();//added by saurabh1 for enroll
		}
		
		
		
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("OECD_Frame8",true);
			
		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//12thsept
			//formObject.setLocked("Reference_Details_Frame1",true);
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);
			//12th sept
			
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("FATCA_SignedDate",true);
			formObject.setLocked("FATCA_ExpiryDate",true);
			loadPicklist_Fatca();
			
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++below code added by nikhil for Self-Supp CR
			Load_Self_Supp_Data();
			//--above code added by nikhil for Self-Supp CR
			formObject.setLocked("CardDetails_Frame1",true);
			Loadpicklist_CardDetails_Frag();
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}

		
		/*else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
        	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
            
        	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
            LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			}
		
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
            LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
        }*/
		
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("EMploymentDetails_Frame1", true);
			
			formObject.setEnabled("EMploymentDetails_Frame1", false);
			LoadView(pEvent.getSource().getName());
			loadPicklist4();
			
		//loadPicklistEmployment();
		}
		
		if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
           
         if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("FinacleCore_avgbal", true);
				CreditCard.mLogger.info( "set visible FinacleCore_Frame8 else if condition ");
				formObject.setVisible("FinacleCore_Frame8", true);
				CreditCard.mLogger.info( "after set visible FinacleCore_Frame8 else if condition ");
				formObject.setVisible("FinacleCore_Frame2", false);
				formObject.setVisible("FinacleCore_Frame3", false);
				CreditCard.mLogger.info( "Inside fianacle core fragment else if condition ");
				
				}
			else{
					formObject.setVisible("FinacleCore_Frame2", true);
					formObject.setVisible("FinacleCore_Frame3", true);
					formObject.setVisible("FinacleCore_avgbal", false);
					CreditCard.mLogger.info( "ELSE set visible FinacleCore_Frame8 ELSE condition ");
					formObject.setVisible("FinacleCore_Frame8", false);
					CreditCard.mLogger.info( "AFTER  Inside fianacle core fragment else condition ");
				}
			
			}
         		formObject.setLocked("FinacleCore_Frame1", true);
			
        }
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
			//SLoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			
			formObject.setLocked("PartMatch_Frame1", true);
			formObject.setLocked("Nationality_Button1_View", false);
        								
        }
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
			
		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMIncident_Frame1",true);
			
		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("MOL1_Frame1",true);
			loadPicklist_Mol();
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SalaryEnq_Frame1",true);
			
		}
		//++Below code added by nikhil 13/11/2017 for Code merge
		
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
			loadPicklist_ServiceRequest();
			//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			
		}
		//--Above code added by nikhil 13/11/2017 for Code merge
		// added by abhishek as per CC FSD
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//done by nikhil for PCAS-2358
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			//code changed by nikhil for CPV Changes 16-04-2019
			formObject.setLocked("CustDetailVerification_Frame1",true);
			//enable_custVerification();

			//LoadPicklistVerification(LoadPicklist_Verification);
			//LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			enable_custVerification();
			
			if("Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")))
			{
				formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
			}//sagarika for PCAS-2577
			
			if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
				formObject.setVisible("CustDetailVerification_Label3",false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_Mob_No2_val", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_mobno2_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_mobno2_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label12", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_email2_val", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_email2_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_email2_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label9", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_val", false); //Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label10", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label17", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_Resno_val", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_resno_ver", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_resno_upd", false);//Added by Rajan
                
                        
             																	
			}
			if("IM".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{   formObject.setVisible("CustDetailVerification_Label8", false);//Added by Rajan
		        formObject.setVisible("cmplx_CustDetailVerification_Offtelno_val",false);//Added by Rajan
		   	    formObject.setVisible("cmplx_CustDetailVerification_Offtelno_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label9", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_val", false); //Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label10", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);//Added by Rajan

			}
			formObject.setNGValue("cmplx_CustDetailVerification_POBoxNo_val", "NA");
			formObject.setNGValue("cmplx_CustDetailVerification_emirates_val", "NA");
			formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_val", "NA");
		}
		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();
			if("BTC".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2))
				|| "SE".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))// Added by Rajan
			{
				formObject.setVisible("cmplx_BussVerification_Office_Extension", false);// Added by Rajan
				formObject.setVisible("BussinessVerification_Label5", false); // Added by Rajan
			}	
						
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("HomeCountryVerification_Frame1",true);
			enable_homeVerification();
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("ResidenceVerification_Frame1",true);
			enable_ResVerification();
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			CreditCard.mLogger.info( "set visible OfficeandMobileVerification inside condition ");
			//nikhil code moved from frame expand event for PCAS-2239
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 and Sno !=4 order by code"); //Modified by Rajan for PCASP-2216
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_designation with (nolock) order by code");
			//cmplx_OffVerification_fxdsal_val
			//enable_officeVerification();
			// added by abhishek to disable office verification
			formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			formObject.setEnabled("OfficeandMobileVerification_Enable", true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			//++Below code added by nikhil 13/11/2017 for Code merge
			//LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
			
			//--Above code added by nikhil 13/11/2017 for Code merge
			formObject.setLocked("cmplx_OffVerification_fxdsal_override", true);
			formObject.setLocked("cmplx_OffVerification_accprovd_override", true);
			formObject.setLocked("cmplx_OffVerification_desig_override", true);
			formObject.setLocked("cmplx_OffVerification_doj_override", true);
			formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", true);
				if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
							{
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
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("LoanandCard_Frame1",true);
			enable_loanCard();
			
		}
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
	    	
			//formObject.setLocked("NotepadDetails_Frame1",true);
			
			// added by abhishek as per CC FSD
			formObject.setVisible("NotepadDetails_Frame3",true);
			
			//formObject.setLocked("NotepadDetails_Frame1",true);
			
		}
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++ below code added by abhishek as per CC FSD 2.7.3
			
			formObject.setLocked("SmartCheck_Frame1",true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			
			
		}
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
		}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
            cpv_Decision();
            
            loadPicklist3();
            openCPVtabs(formObject);
          //for decision fragment made changes 8th dec 2017
            CreditCard.mLogger.info("***********Inside decision history");
        	fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label41,cmplx_DEC_NoofAttempts#DecisionHistory_Label12,cmplx_DEC_SetReminder#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
        	
        	//below code added by nikhil for PCSP-232
        	formObject.setWidth("DecisionHistory_dec_reason_code", 210);
        	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
        	CreditCard.mLogger.info("***********Inside after fragment alignment decision history");

        	//for decision fragment made changes 8th dec 2017
        	formObject.setLocked("cmplx_DEC_VerificationRequired", true);
          
            	

            	//loadPicklist1();
		 } 	
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck_Frame1",true);
			//formObject.setVisible("SmartCheck1_Label1",false);
			formObject.setLocked("SmartCheck1_CreditRemarks",true);
			//formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setLocked("SmartCheck1_FCURemarks",true);
			formObject.setLocked("SmartCheck1_Modify",true);
		}
		else if ("Fpu_Grid".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
			LoadPickList("cmplx_FPU_Grid_Officer_Name", "select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName='FPU'))");
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

