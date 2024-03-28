
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CSM

File Name                                                         : CC_CSM

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Reference No/CR No   Change Date   Changed By    Change Description
1. 					12-6-2016	 Disha		   Code commented saveIndecisionGrid() for continueExecution
1003				17-9-2017	Saurabh			Making CSM employment Details same as initiation.
1000				17-9-2017	Saurabh			Making decision history fragment same as initiation.
1002				17-9-2017	Saurabh			Hiding DDVT related fields in CSM. 
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


//Changes - 1. On Sales coordinator Incoming Document frame is editable 

public class CC_CSM extends CC_Common implements FormListener,Serializable
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
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		
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
	    	 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	    	 CreditCard.mLogger.info("Inside CC_Hold_CPV CC");
	    	// FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_CreditCard.getGlobalVar("CSM_Frame_Name"));
	    	 new CC_CommonCode().setFormHeader(pEvent);
	    	 formObject.setVisible("ReferHistory",false);
	    	 try
				{
	    			String Employment_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
	                if("Salaried Pensioner".equals(Employment_type) || "Salaried".equals(Employment_type) || "Pensioner".equals(Employment_type))
	                {
	                       formObject.setNGValue("Is_Principal_Approval", "Y");
	                       formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", false);
	                }
	                else
	                {
	                       formObject.setNGValue("Is_Principal_Approval", "N");
	                }
	    		 /*
	    		 String Wi_Name=formObject.getWFWorkitemName();
					String AecbHistQuery = "select COUNT(AECBHistMonthCnt) from  ( select cast(isnull(AECBHistMonthCnt,'0') as int) as AECBHistMonthCnt,CustRoleType as CustRoleType,CardType as Liability_type,ProviderNo as ProviderNo  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"+Wi_Name+"'  union all   select cast(isnull(AECBHistMonthCnt,'0') as int) as AECBHistMonthCnt,CustRoleType as CustRoleType,LoanType as Liability_type,ProviderNo as ProviderNo from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+Wi_Name+"'  union all   select cast(isnull(AECBHistMonthCnt,'0') as int) as AECBHistMonthCnt,CustRoleType as CustRoleType,ServiceID as Liability_type,ProviderNo as ProviderNo from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where Child_wi  = '"+Wi_Name+"'  ) as ext_expo where Liability_type not in ( '85','99','Communication Services','TelCo-Mobile Prepaid','104','51','Cheque Purchase','Cheque Book Issuance','101','Current/Saving Account with negative Balance')   and ProviderNo<>'B01'   and CustRoleType in ('A','Main Contract Holder')";
					//String  add_xml_str ="";					
						
						List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
						if(!AecbHistQueryData.isEmpty() && AecbHistQueryData!=null )
						{
							//formObject.setNGValue("AECBHistMonthCnt", AecbHistQueryData.get(0).get(0));
							if(Integer.parseInt(AecbHistQueryData.get(0).get(0))==0)
							{
								formObject.setNGValue("Is_Principal_Approval", "Y");
							}
							else if(Integer.parseInt(AecbHistQueryData.get(0).get(0))>0)
							{
								formObject.setNGValue("Is_Principal_Approval", "Y");
								formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", false);
							}
						}
						else
						{
							//formObject.setNGValue("AECBHistMonthCnt", "0");
							formObject.setNGValue("Is_Principal_Approval", "Y");
						}
						
				*/}
					catch(Exception ex)
					{
						//formObject.setNGValue("AECBHistMonthCnt", "0");
						formObject.setNGValue("Is_Principal_Approval", "Y");
						CreditCard.mLogger.info("exception in calculate aecb month count"+ex.getMessage());
					}
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
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		//CreditCard.mLogger.info( "eventdispatched inside");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().FrameExpandEvent(pEvent);
        break;
                
          case FRAGMENT_LOADED:
        	 // CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
        	  if("Y".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif")))
        	  {
        		  new CC_CommonCode().LockFragmentsOnLoad(pEvent);
        		  Unlock_ATC_Fields();
        	  }
        	  else
        	  {
        	  fragment_loaded(pEvent,formObject);
        	  }
        	  break;
			  
			case MOUSE_CLICKED:
				//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().mouse_clicked(pEvent);
				break;
			
			 case VALUE_CHANGED:
					//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().value_changed(pEvent);
					break;
			/* case FRAME_COLLAPSED:
					CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().framecollapsed(pEvent);
					break;*/
					 
          
         
                  default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		CreditCard.mLogger.info("Inside PL PROCESS initialize()");
		 

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		CustomSaveForm();

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		try{
			CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
			//++ below code added by abhishek on 23/01/2018 for Reject data functionality
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<String> objInput=new ArrayList<String>();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			CreditCard.mLogger.info("objInput args are: "+objInput.get(0));
			List<Object> objOutput=new ArrayList<Object>();
			objOutput.add("Text");
			objOutput= formObject.getDataFromStoredProcedure("ng_CAS_RejectData", objInput,objOutput);
			//++ above code added by abhishek on 23/01/2018 for Reject data functionality
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

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		try{
			CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
			saveIndecisionGrid();//changed by akshay on 23/11/18
			CustomSaveForm();
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("DecisionHistory_Cif_ID",formObject.getNGValue("cmplx_Customer_CIFNo"));
			formObject.setNGValue("DecisionHistory_Emirates_Id",formObject.getNGValue("cmplx_Customer_CIFNo"));
			String fName = formObject.getNGValue("cmplx_Customer_FirstNAme");
			String mName = formObject.getNGValue("cmplx_Customer_MiddleNAme");
			String lName = formObject.getNGValue("cmplx_Customer_LastNAme");
			String fullName = "";
			//Deepak below code moved from save form to submit form started.
			//DONE BY AMAN BECAUSE FORWARD MAPPING NOT WORKING FOR CUSTOMER NAME
			if("".equals(mName)){
				fullName = fName+" "+lName;
			}
			else{
				fullName = fName+" "+mName+" "+lName;
			}
			//DONE BY AMAN BECAUSE FORWARD MAPPING NOT WORKING FOR CUSTOMER NAME
			formObject.setNGValue("DecisionHistory_Customer_Name",fullName);
			formObject.setNGValue("CUSTOMERNAME",fullName);
			LoadReferGrid();
			validateStatusForSupplementary();
			IncomingDoc();
			//saveIndecisionGrid();
			////formObject.setNGValue("cmplx_DEC_Remarks","");--commented by akshay on 10/1/18 as its mapping is removed from  process now
			
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormStarted"+e.getStackTrace());
		}
	}
//EMployment


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		//saveIndecisionGrid();
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
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info( "start of customer");
				loadPicklistCustomer();
				try{
					
					/*if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
						formObject.setVisible("cmplx_Customer_EIDARegNo",true);
						formObject.setVisible("Customer_Label56",true);
						formObject.setEnabled("cmplx_Customer_EIDARegNo",true);
					}
					else {
						formObject.setVisible("cmplx_Customer_EIDARegNo",false);
						formObject.setVisible("Customer_Label56",false);
						formObject.setEnabled("cmplx_Customer_EIDARegNo",false);

					}*/
					formObject.setVisible("cmplx_Customer_EIDARegNo",true);
					formObject.setVisible("Customer_Label56",true);
				formObject.setEnabled("cmplx_Customer_CIFNo",false);
				disableButtonsCC("Customer");
				formObject.setLocked("cmplx_Customer_NEP", false);
				formObject.setLocked("cmplx_Customer_EIDARegNo", false);
				if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
				{
				 formObject.setLocked("cmplx_Customer_MobileNo", true);
				}
				}
				// added for point 22
				catch(Exception ex){
					CreditCard.mLogger.info( "Exception is: "+printException(ex));
				}
				CreditCard.mLogger.info( "end of customer");
			}	
			
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				*/
				formObject.setVisible("CardProd",true);
				//Added by Aman to disable add and delete button at CSM
				formObject.setLocked("Add", true);
				formObject.setLocked("Delete", true);
				formObject.setLocked("Product_type", true);
				//Added by Aman to disable add and delete button at CSM
				disableButtonsCC("Product");
				formObject.setLocked("Modify", true);// PCASP-2287
			}
			
			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadpicklist_Income();
				disableButtonsCC("IncomeDetails");
				//++Below code added by nikhil 7/11/17 as per CC issues sheet
				formObject.setVisible("cmplx_IncomeDetails_CompanyAcc", false);
				formObject.setVisible("IncomeDetails_Label30", false);
				//--Above code added by nikhil 7/11/17 as per CC issues sheet
				//change by saurabh for mobiulity jira.
				formObject.setVisible("IncomeDetails_Label15",false);
				formObject.setVisible("cmplx_IncomeDetails_TotalAvgOther",false);
				LoadPickList("BankStatFrom", "select '--Select--' union select convert(varchar, description) from NG_MASTER_othBankCAC with (nolock)");
				
			}
			
			/*else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
            	 
            		}
			
			else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				
                
                
            }*/
			
			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
					disableButtonsCC("CompanyDetails");
					disableButtonsCC("AuthorisedSignDetails");
					disableButtonsCC("PartnerDetails");

					loadPicklist_CompanyDet();
					LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
	                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
	                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
					LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
	                LoadPickList("PartnerDetails_signcapacity", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SigningCapacity with (nolock) where isActive='Y' order by code");
	                formObject.setEnabled("CompanyDetails_EffectiveLOB",true);
	                formObject.setLocked("CompanyDetails_EffectiveLOB",false);
					hideAtCSMCompany();
					//++below code added by nikhil 11/11/17
					//formObject.setVisible("EmployerStatusPLExpact", false);//commented by akshay on 1/12/17
					//--above code added by nikhil 11/11/17
				}
			
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			
				disableButtonsCC("Liability_New");
			// ++ below code not commented at offshore - 06-10-2017
				formObject.setVisible("Liability_New_Label6",false);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",false);
				formObject.setVisible("Liability_New_Label8",false);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",false);
				//++ Below Code already exists for : "18-CSM-Liability addition-" Incorrect masters in type of contract field" : Reported By Shashank on Oct 05, 2017++
				CreditCard.mLogger.info("before loadng picklist");
				  LoadPickList("ExtLiability_contractType", "select '--Select--' union select convert(varchar, description) from ng_master_contract_type with (nolock)");
				  CreditCard.mLogger.info("after loadng picklist");
				//++ Above Code already exists for : "18-CSM-Liability addition-" Incorrect masters in type of contract field" : Reported By Shashank on Oct 05, 2017++
				  CreditCard.mLogger.info("after liability Details of CSM456");
				  formObject.setVisible("ExtLiability_AECBReport",false);
				  formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",true);
				  formObject.setLocked("cmplx_Liability_New_AECBCompanyconsentAvail",true);
				//++ Above Code already exists for : "16-CSM-Liability Details-" Only aggregate exposure fiel shoul be visible in the bottom section in Liability details" : Reported By Shashank on Oct 05, 2017++
				formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);
				formObject.setEnabled("ExtLiability_Takeoverindicator", false);
				 CreditCard.mLogger.info("after liability Details of CSM123");
				 //below code added by nikhil 20/12/17
				 formObject.setVisible("cmplx_Liability_New_overrideIntLiab", false);
				 formObject.setVisible("cmplx_Liability_New_overrideAECB", false);
				// formObject.setVisible("cmplx_Liability_New_DBRNet", false);
				// formObject.setVisible("ExtLiability_Label9", false);
				 //formObject.setVisible("ExtLiability_Label25", false);
				 //formObject.setVisible("ExtLiability_Label14", false);
				 LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			}
			
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				
				CreditCard.mLogger.info("before disableButtonsCC"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				disableButtonsCC("EMploymentDetails");
				CreditCard.mLogger.info("before setALOCListed"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				setALOCListed();
				CreditCard.mLogger.info("before loadPicklistEmployment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				//loadPicklistEmployment();
				CreditCard.mLogger.info("after loadPicklistEmployment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				loadPicklist4();
				CreditCard.mLogger.info("after loadPicklist4"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				Fields_ApplicationType_Employment();
				CreditCard.mLogger.info("after Fields_ApplicationType_Employment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				formObject.setTop("EMploymentDetails_Label41",307);
				formObject.setTop("cmplx_EmploymentDetails_LengthOfBusiness",322);
				
				// Ref. 1003
				//formObject.setVisible("EMploymentDetails_Label7", false); EMploymentDetails_Label43
				
				formObject.setVisible("EmploymentDetails_Bank_Button", false);
				formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate", false);
				formObject.setVisible("EMploymentDetails_Label28", false);
				formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
				formObject.setVisible("EMploymentDetails_Label5", false);
				formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
				formObject.setVisible("EMploymentDetails_Label6", false);
				formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate", false);
				formObject.setVisible("EMploymentDetails_Label29", false);
				formObject.setVisible("cmplx_EmploymentDetails_Status_PLExpact", false);
				formObject.setVisible("EMploymentDetails_Label30", false);
				formObject.setVisible("cmplx_EmploymentDetails_Status_PLNational", false);
				formObject.setVisible("EMploymentDetails_Label71", false);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
				formObject.setVisible("EMploymentDetails_Label18", false);
				formObject.setVisible("cmplx_EmploymentDetails_ownername", false);
				formObject.setVisible("EMploymentDetails_Label9", false);
				formObject.setVisible("cmplx_EmploymentDetails_NOB", false);
				formObject.setVisible("EMploymentDetails_Label31", false);
				formObject.setVisible("EMploymentDetails_Combo34", false);
				formObject.setVisible("EMploymentDetails_Label17", false);
				formObject.setVisible("cmplx_EmploymentDetails_authsigname", false);
				formObject.setVisible("cmplx_EmploymentDetails_highdelinq", false);
				formObject.setVisible("EMploymentDetails_Label20", false);
				formObject.setVisible("cmplx_EmploymentDetails_dateinPL", false);
				formObject.setVisible("EMploymentDetails_Label21", false);
				formObject.setVisible("cmplx_EmploymentDetails_dateinCC", false);
				formObject.setVisible("EMploymentDetails_Label15", false);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
				formObject.setVisible("EMploymentDetails_Label32", false);
				formObject.setVisible("EMploymentDetails_Combo35", false);
				formObject.setVisible("EMploymentDetails_Label26", false);
				formObject.setVisible("cmplx_EmploymentDetails_remarks", false);
				formObject.setVisible("EMploymentDetails_Label27", false);
				formObject.setVisible("cmplx_EmploymentDetails_Aloc_RemarksPL", false);
				formObject.setVisible("EMploymentDetails_Label10", false);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
				formObject.setVisible("EMploymentDetails_Label4", false);
				formObject.setVisible("cmplx_EmploymentDetails_PromotionCode", false);
				formObject.setVisible("EMploymentDetails_Label10", false);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
				/*formObject.setTop("EMploymentDetails_Save", 360);
				formObject.setLeft("EMploymentDetails_Save", 10);*/
				formObject.setVisible("EMploymentDetails_Save", true);
				formObject.setTop("EMploymentDetails_Label71", 108);
				formObject.setTop("cmplx_EmploymentDetails_EmpContractType", 124);
				formObject.setLeft("EMploymentDetails_Label71",1066);
				formObject.setLeft("cmplx_EmploymentDetails_EmpContractType",1066);
				CreditCard.mLogger.info("after all setto and get top"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				if("LIFSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
				{//EMploymentDetails_Label41
					CreditCard.mLogger.info("@sagarika emp");
				//EMploymentDetails_Label43
                 formObject.setEnabled("cmplx_EmploymentDetails_InsuranceValue", true);
                 formObject.setVisible("EMploymentDetails_Label41",false);
                 formObject.setVisible("EMploymentDetails_Label43",true);
 				formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness",false);
                 formObject.setEnabled("cmplx_EmploymentDetails_PremAmt",true);
                // formObject.setVisible("EMploymentDetails_Label44", true);
                // formObject.setVisible("cmplx_EmploymentDetails_PremPaid",true);
                 formObject.setEnabled("cmplx_EmploymentDetails_PremPaid",true);
                // formObject.setVisible("EMploymentDetails_Label46", true);
                // formObject.setVisible("cmplx_EmploymentDetails_PremType", true);
                 formObject.setEnabled("cmplx_EmploymentDetails_PremType", true);
                // formObject.setVisible("EMploymentDetails_Label47", true);
                // formObject.setVisible("cmplx_EmploymentDetails_RegPayment",true);
                 formObject.setEnabled("cmplx_EmploymentDetails_RegPayment",true);
                 formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance",false);
                 formObject.setVisible("cmplx_EmploymentDetails_Is_life_insur_lessthan_5", true);
                 formObject.setVisible("cmplx_EmploymentDetails_Is_group_insur_policy", true);

                // formObject.setVisible("EMploymentDetails_Label52", true);
               //  formObject.setVisible("cmplx_EmploymentDetails_MinimumWait", true);
                 formObject.setTop("EMploymentDetails_Label42",282);
                 formObject.setTop("cmplx_EmploymentDetails_InsuranceValue", 297);
                 formObject.setTop("EMploymentDetails_Label43", 282);
                 formObject.setTop("cmplx_EmploymentDetails_PremAmt", 297);
                 formObject.setTop("EMploymentDetails_Label44", 282);//FinacleCore_TypeOfRetutn
                 formObject.setTop("cmplx_EmploymentDetails_PremPaid",297);
                 formObject.setTop("EMploymentDetails_Label46", 327);//cmplx_emp_ver_sp2_empstatus_remarks
                 formObject.setTop("cmplx_EmploymentDetails_PremType", 342);
                 formObject.setTop("EMploymentDetails_Label47", 327);
                 formObject.setTop("cmplx_EmploymentDetails_RegPayment",327);
                 formObject.setTop("EMploymentDetails_Label52", 327);
                 formObject.setTop("cmplx_EmploymentDetails_MinimumWait", 342);
				}//Initiation Channel
				if("MOTSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
				{//cmplx_EmploymentDetails_MotorInsurance
					CreditCard.mLogger.info("@sagarika motsur");
				//EMploymentDetails_Frame2	
                // formObject.setVisible("EMploymentDetails_Label54", true);
                 
               //  formObject.setVisible("cmplx_EmploymentDetails_MotorInsurance", true);
                 formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance", true);
                 formObject.setEnabled("cmplx_EmploymentDetails_InsuranceValue", false);
                
                  formObject.setEnabled("cmplx_EmploymentDetails_PremAmt",false);
                 // formObject.setVisible("EMploymentDetails_Label44", true);
                 // formObject.setVisible("cmplx_EmploymentDetails_PremPaid",true);
                  formObject.setEnabled("cmplx_EmploymentDetails_PremPaid",false);
                 // formObject.setVisible("EMploymentDetails_Label46", true);
                 // formObject.setVisible("cmplx_EmploymentDetails_PremType", true);
                  formObject.setEnabled("cmplx_EmploymentDetails_PremType", false);
                 // formObject.setVisible("EMploymentDetails_Label47", true);
                 // formObject.setVisible("cmplx_EmploymentDetails_RegPayment",true);
                  formObject.setEnabled("cmplx_EmploymentDetails_RegPayment",false);
              //    formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance",true);
                 formObject.setTop("EMploymentDetails_Label54", 327);
                 
                 formObject.setTop("cmplx_EmploymentDetails_MotorInsurance",  342);
                 
				}
				//added by akshay on 13/10/17
				
				formObject.setEnabled("cmplx_EmploymentDetails_Others", true);
				 formObject.setLocked("cmplx_EmploymentDetails_Others",false);
				formObject.setVisible("EMploymentDetails_Label32", false);
				formObject.setVisible("cmplx_EmploymentDetails_FieldVisitDone", false);
			    formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			    if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
					formObject.setLocked("cmplx_EmploymentDetails_NepType",true);
				}
					//p1-7,ALOC fields should be disabled for CSM user
				    formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
                    formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
                    formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
                    formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
                    formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
                    formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
                    formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
                    formObject.setLocked("cmplx_EmploymentDetails_Freezone",true); 
                    formObject.setLocked("cmplx_EmploymentDetails_Kompass",true); 
					//p1-7,ALOC fields should be disabled for CSM user
				// Ref. 1003 end. EMploymentDetails_Frame2 cmplx_EmploymentDetails_InsuranceValue
                  //++Below code added by nikhil 7/11/17 as per CC issues sheet
                    CreditCard.mLogger.info("ssss-emp");
                    formObject.setTop("EMploymentDetails_Save",400);
                 //   formObject.setTop("EMploymentDetails_Label54",  327);
                   //Initiation Channel
                   // formObject.setTop("cmplx_EmploymentDetails_MotorInsurance",  342);
                  //cmplx_EmploymentDetails_IndusSeg
                    formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
    				formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
    				formObject.setVisible("EMploymentDetails_Label28", false);
    				formObject.setVisible("EMploymentDetails_Label5", false);
    				formObject.setVisible("cmplx_EmploymentDetails_ClassificationCode", false);
    				formObject.setVisible("EMploymentDetails_Label36", false);
    				formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
    				formObject.setVisible("EMploymentDetails_Label10", false);
    				//--Above code added by nikhil 7/11/17 as per CC issues sheet
    				formObject.setVisible("EMploymentDetails_Label37", false);
    				formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", false);
    				//Added by aman to not show collection code at CSM
    				String reqProd = formObject.getNGValue("PrimaryProduct");
    				String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
    				String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
    				//change by saurabh for PCSP-23 full target seg code description not appearing on field. 7th Feb 19.
    				if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PersonalLoan").equalsIgnoreCase(reqProd)){
    					if(appCategory!=null && "BAU".equalsIgnoreCase(appCategory)){
    						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
    					}
    					else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
    						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
    					}
    					else{
    						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");	
    					}
    				}
    				else if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(reqProd)){
    					if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
    						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
    					}
    					else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
    						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
    					}
    					else{
    						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
    					}
    				}
    				
    				if(formObject.getNGValue("cmplx_EmploymentDetails_Others")=="true"){
    					formObject.setVisible("EMploymentDetails_Label72",false);
    					formObject.setVisible("cmplx_EmploymentDetails_EMpCode",false);
    				}
    				else 
    				{
    					formObject.setVisible("EMploymentDetails_Label72",true);
    					formObject.setVisible("cmplx_EmploymentDetails_EMpCode",true);
    				}
    				
    				CreditCard.mLogger.info("end of fetchfrag of employment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
    				formObject.setHeight("EMploymentDetails_Frame2",800);
    			
    			
    				
			}//Designation_button7
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			
				disableButtonsCC("ELigibiltyAndProductInfo");
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestRate", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_EMI", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_Tenor", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				formObject.setVisible("ELigibiltyAndProductInfo_Refresh", false);
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
				if(formObject.isVisible("CardDetails_Frame1")==false){
					formObject.fetchFragment("Card_Details", "CardDetails", "q_cardDetails");
					formObject.setTop("Card_Details", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
					formObject.setNGFrameState("Card_Details", 0);
					if(!formObject.isVisible("SupplementCardDetails_Frame1") && "Yes".equals(formObject.getNGValue("cmplx_CardDetails_SuppCardReq"))){
						formObject.setVisible("Supplementary_Container", true);
					}
					formObject.setTop("Supplementary_Container", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+30);
					formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+60);
					formObject.setTop("KYC", formObject.getTop("FATCA")+30);
					formObject.setTop("OECD", formObject.getTop("KYC")+30);
					formObject.setTop("ReferenceDetails", formObject.getTop("OECD")+30);

				}
			}
			
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
				disableButtonsCC("AddressDetails");
				loadPicklist_Address();
				
			}
			
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
				//change by saurabh on 7th Dec
				//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
				formObject.setVisible("AlternateContactDetails_AirArabiaIdentifier", false);
				formObject.setVisible("AlternateContactDetails_EnrollRewardsIdentifier", false);//added by saurabh1 for enroll
				
				//formObject.setVisible("AltContactDetails_Label7", false);
				//change by saurabh for air arabia functionality.
				AirArabiaValid();
				enrollrewardvalid();//added by saurabh1 for enroll
				disableButtonsCC("AltContactDetails");
				    
				
				/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
			} 
			
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("FATCA");
				//LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");
				/*formObject.setEnabled("FATCA_Save",true);*/
				loadPicklist_Fatca();
			}
			
			//changes by saurabh for DROP-4
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				disableButtonsCC("KYC");
				loadPicklist_KYC();
				
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("OECD");
				loadPicklist_oecd();
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("IncomingDocument");
				
				//++ Below Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
				CreditCard.mLogger.info("before incomingdoc");
				//added by nikhil 05/01/2018
				fetchIncomingDocRepeaterNew();
				CreditCard.mLogger.info("after incomingdoc");
				//++ Above Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
			
			}
			else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			
				disableButtonsCC("Reference_Details");
				LoadPickList("Reference_Details_ReferenceRelatiomship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
				
			}
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
				disableButtonsCC("SupplementCardDetails");
				//added by akshay on 9/1/18 for proc 3507
				formObject.setVisible("SupplementCardDetails_Label19",false);
				formObject.setVisible("SupplementCardDetails_Profession",false);
				formObject.setVisible("SupplementCardDetails_Label20",false);
				formObject.setVisible("SupplementCardDetails_DLNo",false);
				formObject.setVisible("SupplementCardDetails_Label27",false);
				formObject.setVisible("SupplementCardDetails_VisaNo",false);
				formObject.setVisible("SupplementCardDetails_Label11",false);
				formObject.setVisible("SupplementCardDetails_PassportExpiry",false);
				formObject.setVisible("SupplementCardDetails_Label23",false);
				formObject.setVisible("SupplementCardDetails_VisaExpiry",false);
				formObject.setVisible("SupplementCardDetails_Label17",false);
				formObject.setVisible("SupplementCardDetails_EmiratesIDExpiry",false);
				formObject.setVisible("SupplementCardDetails_Label25",false);
				formObject.setVisible("SupplementCardDetails_ReceivedDate",false);
				formObject.setVisible("SupplementCardDetails_Label29",false);
				formObject.setVisible("SupplementCardDetails_MarketingCode",false);
				formObject.setVisible("SupplementCardDetails_Label30",false);
				formObject.setVisible("SupplementCardDetails_ApprovalLevelCode",false);
				formObject.setVisible("SupplementCardDetails_Label24",false);
				formObject.setVisible("SupplementCardDetails_AppRefNo",false);
				formObject.setVisible("SupplementCardDetails_Label31",false);
				formObject.setVisible("SupplementCardDetails_FeeProfile",false);
				formObject.setVisible("SupplementCardDetails_Label10",false);
				formObject.setVisible("SupplementCardDetails_Title",false);
				formObject.setVisible("SupplementCardDetails_Label8",false);
				formObject.setVisible("SupplementCardDetails_PersonalInfo",false);
				formObject.setVisible("SupplementCardDetails_Non_Resident",false);
				formObject.setVisible("SupplementCardDetails_Label32",false);
				formObject.setVisible("SupplementCardDetails_EmpType",false);
				formObject.setVisible("SupplementCardDetails_Label33",false);
				formObject.setVisible("SupplementCardDetails_IDIssueDate",false);
				formObject.setVisible("SupplementCardDetails_Label35",false);
				formObject.setVisible("SupplementCardDetails_PassportIssueDate",false);
				formObject.setVisible("SupplementCardDetails_Label36",false);
				formObject.setVisible("SupplementCardDetails_VisaIssueDate",false);
				formObject.setVisible("SupplementCardDetails_Label37",false);
				formObject.setVisible("SupplementCardDetails_MaritalStatus",false);
				
				formObject.setTop("SupplementCardDetails_Add", formObject.getTop("SupplementCardDetails_CardProduct")+formObject.getHeight("SupplementCardDetails_CardProduct")+30);
				formObject.setTop("SupplementCardDetails_Modify", formObject.getTop("SupplementCardDetails_CardProduct")+formObject.getHeight("SupplementCardDetails_CardProduct")+30);
				formObject.setTop("SupplementCardDetails_Delete", formObject.getTop("SupplementCardDetails_CardProduct")+formObject.getHeight("SupplementCardDetails_CardProduct")+30);
				formObject.setTop("SupplementCardDetails_cmplx_supplementGrid", formObject.getTop("SupplementCardDetails_Add")+formObject.getHeight("SupplementCardDetails_Add")+20);
				formObject.setTop("SupplementCardDetails_Save", formObject.getTop("SupplementCardDetails_cmplx_supplementGrid")+formObject.getHeight("SupplementCardDetails_cmplx_supplementGrid")+20);
				formObject.setHeight("SupplementCardDetails_Frame1", formObject.getTop("SupplementCardDetails_Save")+formObject.getHeight("SupplementCardDetails_Save")+20);
				formObject.setHeight("Supplementary_Cont",formObject.getHeight("SupplementCardDetails_Frame1")+20);
				adjustFrameTops("Card_Details,Supplementary_Cont,FATCA,KYC,OECD,Reference_Details");//added by akshay on 9/1/17
				loadPicklist_suppCard();
				//formObject.setNGValue("SupplementCardDetails_ResidentCountry","AE"); PCASP-592
				formObject.setLocked("SupplementCardDetails_ResidentCountry",true);
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
				disableButtonsCC("CardDetails");
				//Ref. 1002
				/*//changed by akshay on 13/10
				LoadPickList("cmplx_CardDetails_MarketingCode","select '--Select--'  as description union select convert(varchar,description) from NG_MASTER_MarketingCode with (nolock) ");
				formObject.setVisible("cmplx_CardDetails_Security_Check_Held", false);
				LoadPickList("CardDetails_FeeProfile","select '--Select--'  as description union select convert(varchar,description) from ng_master_feeProfile with (nolock) ");
				formObject.setVisible("CardDetails_Label12", false);
				LoadPickList("CardDetails_InterestFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_InterestProfile with (nolock) ");
				formObject.setVisible("cmplx_CardDetails_MarketingCode", false);
				LoadPickList("CardDetails_TransactionFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_TransactionFeeProfile with (nolock) ");
				formObject.setVisible("CardDetails_Label8", false);
				//++Below code added by nikhil 6/11/17
				LoadPickList("cmplx_CardDetails_Statement_cycle","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_StatementCycle with (nolock) where isActive='Y' order by code");
				//--Above Code added by nikhil 6/11/17
				*/
				//added by nikhil for disbursal showstopper
				Loadpicklist_CardDetails_Frag();
				if(	"Salaried".equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
					formObject.setVisible("CardDetails_Label7", true);
					formObject.setVisible("cmplx_CardDetails_statCycle", true);
					formObject.setVisible("CardDetails_Label3", false);
					formObject.setVisible("CardDetails_Label2", false);//corrected by saurabh1 for founder member
					formObject.setVisible("cmplx_CardDetails_CompanyEmbossing_name", false);//added by akshay on 11/9/17 //corrected by saurabh1 for founder member
					formObject.setLeft("CardDetails_Label5", 288);
					formObject.setLeft("cmplx_CardDetails_SuppCardReq", 288);
					
				}	
				else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
					formObject.setVisible("CardDetails_Label2", false);
					formObject.setVisible("cmplx_CardDetails_CharityOrg", false);
					formObject.setVisible("CardDetails_Label4", false);
					formObject.setVisible("cmplx_CardDetails_CharityAmount", false);
					formObject.setLeft("CardDetails_Label5", 552);
					formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);
					
				}
				if("Islamic".equalsIgnoreCase(formObject.getNGValue("LoanType_Primary"))){
					formObject.setVisible("CardDetails_Label2", true);
					formObject.setVisible("cmplx_CardDetails_CharityOrg", true);
					formObject.setVisible("CardDetails_Label4", true);
					formObject.setVisible("cmplx_CardDetails_CharityAmount", true);
					formObject.setLeft("CardDetails_Label5", 1059);
					formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);
				}
				/*formObject.setVisible("cmplx_CardDetails_Bank_Name", false);
				formObject.setVisible("CardDetails_Label9", false);
				formObject.setVisible("cmplx_CardDetails_Cheque_Number", false);
				formObject.setVisible("CardDetails_Label10", false);
				formObject.setVisible("cmplx_CardDetails_Amount", false);
				formObject.setVisible("CardDetails_Label11", false);
				formObject.setVisible("cmplx_CardDetails_Date", false);
				formObject.setVisible("CardDetails_Label13", false);
				formObject.setVisible("cmplx_CardDetails_CustClassification", false);*/
				//Ref. 1002 end.
				IslamicFieldsvisibility();
				if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) ){
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossing_name", true);
				}
				else{
					formObject.setNGValue("cmplx_CardDetails_CompanyEmbossing_name","");
					formObject.setEnabled("cmplx_CardDetails_CompanyEmbossing_name", false);
				}
				
				if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Supplementary_Card"))){
					CreditCard.mLogger.info("Is Supp req is yes: ");
					formObject.setVisible("Supplementary_Cont",true);
					if(formObject.getWFActivityName().equalsIgnoreCase("DDVT_Checker") || formObject.getWFActivityName().equalsIgnoreCase("Disbursal")){
						CreditCard.mLogger.info("activity is checker: ");
						formObject.fetchFragment("Supplementary_Cont", "SupplementCardDetails", "q_SuppCardDetails");//q_SuppCardDetails
						loadPicklist_suppCard();
						formObject.setNGFrameState("Supplementary_Cont",0);
						adjustFrameTops("Card_Details,Supplementary_Cont,FATCA,KYC,OECD,Reference_Details");
					}
					
				}
				//Code added by aman to not show the fields that aRE not visible in CSO
				
				formObject.setVisible("CardDetails_bankName_Button", false);
				formObject.setVisible("CardDetails_BankName", false);
				formObject.setVisible("CardDetails_Label9", false);
				formObject.setVisible("CardDetails_ChequeNumber", false);
				formObject.setVisible("CardDetails_Label10", false);
				formObject.setVisible("CardDetails_Amount", false);
				formObject.setVisible("CardDetails_Label11", false);
				formObject.setVisible("CardDetails_Date", false);
				formObject.setVisible("CardDetails_Label13", false);
				formObject.setVisible("cmplx_CardDetails_CustClassification", false);
				formObject.setVisible("CardDetails_Button2", false);
				formObject.setVisible("CardDetails_Button3", false);
				formObject.setVisible("CardDetails_Button4", false);
				formObject.setVisible("cmplx_CardDetails_cmpmx_gr_cardDetails", false);
				formObject.setVisible("CardDetails_CardProduct", false);
				formObject.setVisible("CardDetails_Label14", false);
				formObject.setVisible("CardDetails_ECRN", false);
				formObject.setVisible("CardDetails_Label15", false);
				formObject.setVisible("CardDetails_Label16", false);
				formObject.setVisible("CardDetails_CRN", false);
				formObject.setVisible("CardDetails_TransactionFP", false);
				formObject.setVisible("CardDetails_Label17", false);
				formObject.setVisible("CardDetails_InterestFP", false);
				formObject.setVisible("CardDetails_Label18", false);
				formObject.setVisible("CardDetails_Label19", false);
				formObject.setVisible("CardDetails_FeeProfile", false);
				formObject.setVisible("CardDetails_Button1", false);
				formObject.setVisible("CardDetails_Button5", false);
				formObject.setVisible("CardDetails_add", false);
				formObject.setVisible("CardDetails_modify", false);
				formObject.setVisible("CardDetails_delete", false);
				formObject.setVisible("cmplx_CardDetails_cmplx_CardCRNDetails", false);
				formObject.setTop("CardDetails_Save", 300);
				formObject.setHeight("CardDetails_Frame1", formObject.getTop("CardDetails_Save")+formObject.getHeight("CardDetails_Save")+20);
				formObject.setHeight("Card_Details", formObject.getHeight("CardDetails_Frame1")+20);
				adjustFrameTops("Card_Details,Supplementary_Cont,FATCA,KYC,OECD,Reference_Details");
				//Code added by aman to not show the fields that aRE not visible in CSO
			
				if ("LI".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2))){
				formObject.setLocked("CardDetails_Frame1",true);
				}
				//below code added by nikhil
				formObject.setVisible("cmplx_CardDetails_Statement_cycle", false);
				formObject.setVisible("CardDetails_Label7", false);
				//++below code added by nikhil for Self-Supp CR
				Load_Self_Supp_Data();
				//--above code added by nikhil for Self-Supp CR
			}
			//++Below code added by nikhil 8/11/17 as per CC issues sheet
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				CreditCard.mLogger.info( "Activity name is:" + formObject.getNGValue("cmplx_CC_Loan_DDSMode"));
			      
				//formObject.setLocked("chequeNo",true);
				//formObject.setLocked("chequeStatus",true);
				loadPicklist_ServiceRequest();
				CreditCard.mLogger.info( "Activity name is:" + formObject.getNGValue("cmplx_CC_Loan_DDSMode"));
				//below code added by nikhil 19/1/18
				String dds_mode=formObject.getNGValue("cmplx_CC_Loan_DDSMode");
				//below code also fix point "30-Service Details#Validations not as per FSD."
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
				
				else if("F".equalsIgnoreCase(dds_mode) || "Flat".equalsIgnoreCase(dds_mode))
				//above code also fix point "30-Service Details#Validations not as per FSD."
				
				{
					
					formObject.setLocked("cmplx_CC_Loan_Percentage",true);
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);
				}
				//below code also fix point "30-Service Details#Validations not as per FSD."
				else if("P".equalsIgnoreCase(dds_mode) || "Per".equalsIgnoreCase(dds_mode))
				//above code also fix point "30-Service Details#Validations not as per FSD."
				
				{
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
					formObject.setLocked("cmplx_CC_Loan_Percentage",false);
				}
				else
				{
					formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);
					formObject.setLocked("cmplx_CC_Loan_Percentage",false);
				}
				
				if(formObject.getNGValue("cmplx_CC_Loan_HoldType").equalsIgnoreCase("T")){
					formObject.setLocked("cmplx_CC_Loan_HoldFrom_Date",false);
					formObject.setLocked("cmplx_CC_Loan_HOldTo_Date",false);
				}
				else{
					formObject.setLocked("cmplx_CC_Loan_HoldFrom_Date",true);
					formObject.setLocked("cmplx_CC_Loan_HOldTo_Date",true);
				}
				if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_F").equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_ModeOfSI"))){
					formObject.setLocked("cmplx_CC_Loan_FlatAMount",false);
					formObject.setLocked("cmplx_CC_Loan_SI_Percentage",true);

				}
				else if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_P").equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_ModeOfSI"))){
					formObject.setLocked("cmplx_CC_Loan_FlatAMount",true);
					formObject.setLocked("cmplx_CC_Loan_SI_Percentage",false);

				}
				else{
					formObject.setLocked("cmplx_CC_Loan_FlatAMount",true);
					formObject.setLocked("cmplx_CC_Loan_SI_Percentage",true);
				}
				 //++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
				//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
				//formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
				//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

				   
			}
			//--Above code added by nikhil 8/11/17 as per CC issues sheet
			//added by yash on 22/8/2017
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//12th sept
				 notepad_load();
				 notepad_withoutTelLog();
				
			}
			//ended by yash
			
			
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

               formObject.setLocked("DecisionHistory_dec_reason_code",false); // As the fields is locked not disabled
               formObject.setLocked("DecisionHistory_ReferTo",true);
               //changed by nikhil 4/12/18 to save
 		 		fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
 		 		formObject.setWidth("DecisionHistory_dec_reason_code", 300);
 		 		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				//below code added by nikhil 20/12/17
				formObject.setEnabled("cmplx_DEC_VerificationRequired", false);
				//loadPicklist3();
 				//CreditCard.mLogger.info("Top of DecisionHistory_save "+ formObject.getTop("DecisionHistory_save"));
 				//CreditCard.mLogger.info("height of DecisionHistory_save "+ formObject.getHeight("DecisionHistory_save"));
 				//for decision fragment made changes 8th dec 2017
 		 		//formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+20);
 		 		//changed by akshay on 6/12/17 for decision alignment
			
				//Added by shivang for SC Application form
				if(formObject.getWFActivityName().equalsIgnoreCase("SalesCoordinator")){
					formObject.setVisible("GenerateAppForm", true);  
					formObject.setTop("GenerateAppForm", 100);
					formObject.setLeft("GenerateAppForm", 800);
					formObject.setHeight("GenerateAppForm", 30);
				}
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
