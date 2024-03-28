
package com.newgen.omniforms.user;

import java.util.ArrayList;
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


public class CC_Disbursal extends CC_Common implements FormListener
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
	     makeSheetsInvisible(tabName, "8,9,11,13,14,15,16,17");
	     FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	     formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_CreditCard.getGlobalVar("Disbural_Frame_Name"));
		
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
	    	 CreditCard.mLogger.info("Inside CC_Disbursal CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	    	//added by saurabh on 31th Dec
	    	 String subProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
	    	 if(subProd.equalsIgnoreCase("LI") || subProd.equalsIgnoreCase("PU") || subProd.equalsIgnoreCase("PULI")){
	    		formObject.setVisible("Limit_Increase", true);
	    		formObject.setVisible("CC_Creation", false);
	    		formObject.setTop("Limit_Increase",formObject.getTop("CC_Creation"));
	    	 }
	    	 else{
	    		 formObject.setVisible("Limit_Increase", false);
	    		 formObject.setVisible("CC_Creation", true);
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
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	
        
       // CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().FrameExpandEvent(pEvent);						
						
						break;
					
					case FRAGMENT_LOADED:
						fragment_loaded(pEvent,formObject);
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
			saveIndecisionGrid();
			//++ below code added by Deepak on 15/07/2018 for EFMS refresh functionality
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Submit_Desc").equalsIgnoreCase(formObject.getNGValue("Decision"))){
				List<String> objInput=new ArrayList<String>();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:"+"Approve");
				CreditCard.mLogger.info("objInput args are: "+objInput);
				List<Object> objOutput=new ArrayList<Object>();
				objOutput.add("Text");
				objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}
		//++ above code added by Deepak on 15/07/2018 for EFMS refresh functionality
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
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			//Deepak 24 Dec Code moved by Deepak for PCSP 316
			if(formObject.isVisible("AltContactDetails_Frame1")==false){
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			}
			
			String cardDisp=formObject.getNGValue("AlternateContactDetails_CardDisp");
			if("009".equalsIgnoreCase(cardDisp) ){
				formObject.setNGValue("Card_Dispatch_Option","International dispatch-INTL");
			}
			/*else if("Holding WI- HOLD".equalsIgnoreCase(cardDisp))
    		{
    			formObject.setNGValue("Card_Dispatch_Option","Holding WI- HOLD");
    		}*/
			else if("151".equalsIgnoreCase(cardDisp))
			{
				formObject.setNGValue("Card_Dispatch_Option","Card centre collection");
			}
			else if("998".equalsIgnoreCase(cardDisp))
			{
				formObject.setNGValue("Card_Dispatch_Option","COURIER");
			}
			else
			{
				formObject.setNGValue("Card_Dispatch_Option","Branch");
			}

			CustomSaveForm();
			
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			//change by saurabh for adding submit condition
			//Deepak 30 Dec Change done for PCSP-386
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{	
			LoadReferGrid();
			}
			if("Submit".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) && !check_MurabhaFileConfirmed(formObject)){
				throw new ValidatorException(new FacesMessage("Please generate and confirm Murabha File!!!!"));
			}
			//added by akshay on 6/4/18 for proc 7641

		}
		catch(ValidatorException v){
			CreditCard.mLogger.info("Inside validator exception before throwing again.");
			throw new ValidatorException(new FacesMessage("Please generate and confirm Murabha File!!!!"));
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
		CreditCard.mLogger.info("fragemnt to be loaded is: "+pEvent.getSource().getName());
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistCustomer();
			LoadView(pEvent.getSource().getName());
			//formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_VisaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
			
			/*if (!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setNGValue("cmplx_Customer_EmirateIDExpiry", "");
			}*/
			//setDisabled();
		}	
		
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			
			formObject.setLocked("Product_Frame1",true);
		}
		
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("AddressDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			loadPicklist_Address();
		}
		
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//added By Tarang as per drop 4 point 1 started on 26/02/2018
			//formObject.setLocked("AltContactDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			CreditCard.mLogger.info("Inside alternate contact details fragment (Disbursal) :"+formObject.getNGValue("Card_Dispatch_Option"));
			String cardDisp=formObject.getNGSelectedItemText("AlternateContactDetails_CardDisp");
    		if("International dispatch-INTL".equalsIgnoreCase(cardDisp) || "Holding WI- HOLD".equalsIgnoreCase(cardDisp) || "Card centre collection".equalsIgnoreCase(cardDisp) || "COURIER".equalsIgnoreCase(cardDisp)){
    			formObject.setLocked("CardDispatchToButton",true);
    		}
    		else
    		{
    			formObject.setLocked("CardDispatchToButton",false);
    		}
    		
    		//change by saurabh for air arabia functionality.
			AirArabiaValid();
			//added By Tarang as per drop 4 point 1 ended on 26/02/2018
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			formObject.setLocked("AltContactDetails_Saved",false);
			formObject.setEnabled("AltContactDetails_Saved",true);
		}
		//added by yash for displaying cifid and customer name in Limit increase fragment on 19/6/2017
		//COMMENTED BY ABHISHEK AS PER cc fsd
		
		
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
            fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#cmplx_DEC_MultipleApplicantsGrid#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
            formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
            loadPicklist3();
            LoadReferGrid();
            //changed by nikhil
		} 	
		
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("OECD_Frame8",true);
			LoadView(pEvent.getSource().getName());
			
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("FATCA_SignedDate",true);
			formObject.setLocked("FATCA_ExpiryDate",true);
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			formObject.setEnabled("SupplementCardDetails_Frame1",false);
			
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName()) ) {
			Loadpicklist_CardDetails_Frag();
			//++below code added by nikhil for Self-Supp CR
			Load_Self_Supp_Data();
			//--above code added by nikhil for Self-Supp CR
			formObject.setLocked("CardDetails_Frame1",true);
			// Deepak 27 Dec PCSP-275
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Statement_cycle"))||"Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Statement_cycle"))){
				if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
					formObject.setNGValue("cmplx_CardDetails_Statement_cycle", formObject.getNGValue("cmplx_IncomeDetails_StatementCycle"));
				}
				else{
					formObject.setNGValue("cmplx_CardDetails_Statement_cycle", formObject.getNGValue("cmplx_IncomeDetails_StatementCycle2"));
				}
			}
			
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			loadpicklist_Income();
			}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
               formObject.setLocked("CompanyDetails_Frame1", true);
               
				/*LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");*/
               loadPicklist_CompanyDet();
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
		//	formObject.setLocked("EMploymentDetails_Frame1", true);
			LoadView(pEvent.getSource().getName());
			loadPicklist4();
			Fields_ApplicationType_Employment();
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
           
         if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
					if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
						formObject.setVisible("FinacleCore_avgbal", true);
						CreditCard.mLogger.info( "set visible FinacleCore_Frame8 if condition ");
						formObject.setVisible("FinacleCore_Frame8", true);
						CreditCard.mLogger.info( "after set visible FinacleCore_Frame8 if condition ");
						formObject.setVisible("FinacleCore_Frame2", false);
						formObject.setVisible("FinacleCore_Frame3", false);
						CreditCard.mLogger.info( "Inside fianacle core fragment if condition ");
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
         		LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
    			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

			
        }
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
		//	LoadPickList("PartMatch_nationality","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock) order by code"); //Arun (10/10)
			
			formObject.setLocked("PartMatch_Frame1", true);
        								
        }
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();
		}		
		else if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			//code by saurah on 17th dec
			formObject.setLocked("CC_Creation_Frame3",true);//added by akshay for proc 7746
			formObject.setLocked("CC_Creation_Save", false);
			LoadPickList("CC_Creation_Product","select '--Select--' as description,'' as code union select distinct convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
			String subProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			if(!"PU".equalsIgnoreCase(subProd) && !"LI".equalsIgnoreCase(subProd) && !"PULI".equalsIgnoreCase(subProd)){
				openDemographicTabs();
				loadInDecGrid();
				formObject.setTop("ReferHistory", 550);
				int framestate1=formObject.getNGFrameState("IncomeDEtails");
				if(framestate1 == 0){
					CreditCard.mLogger.info("Incomedetails");
				}
				else {
					formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
					CreditCard.mLogger.info("fetched income details");
					//formObject.setTop("IncomeDEtails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					expandIncome();
				}

				int framestate2=formObject.getNGFrameState("EmploymentDetails");
				CreditCard.mLogger.info("framestate for Employment is: "+framestate2);
				if(framestate2 == 0){
					CreditCard.mLogger.info("EmploymentDetails");
				}
				else {
					loadEmployment();
					CreditCard.mLogger.info("fetched employment details");
				}

				int framestate3=formObject.getNGFrameState("EligibilityAndProductInformation");
				if(framestate3 == 0){
					CreditCard.mLogger.info("EligibilityAndProductInformation");
				}
				else {
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligProd");
					expandEligibility();
					CreditCard.mLogger.info("fetched EligibilityAndProductInformation details");
				}
			loadInCCCreationGrid();
			}
			else{
				formObject.setLocked("CC_Creation_Frame3",true);
			}
		}
		else if ("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		else if ("Limit_Inc".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Reference_Details_Frame1",true);
			// added by abhishek as per CC FSD
			/*if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_LimitIncrease_Code").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))
					||	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_ProductUpgrade_Code").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))
					||NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PULI_Code").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))
					||	("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) && "TOPIM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4)))){
			*/	
				
				formObject.setLocked("Limit_Inc_Frame4",true);
				formObject.setLocked("cmplx_LimitInc_DetailsUploadedd",false);
				formObject.setLocked("Limit_Inc_Save",false);
				if(!"Y".equals(formObject.getNGValue("cmplx_LimitInc_UpdateCustFlag"))){
					formObject.setLocked("Limit_Inc_UpdCust",false);
				}
				else if("Y".equals(formObject.getNGValue("cmplx_LimitInc_UpdateCustFlag")) && !"Y".equals(formObject.getNGValue("Is_CardServices"))){
					formObject.setLocked("Limit_Inc_UpdCust",true);
					formObject.setLocked("Limit_Inc_Button1",false);
				}
					CreditCard.mLogger.info("before calling loadDataInLimitInc");
				loadDataInLimitInc();
				CreditCard.mLogger.info("after calling loadDataInLimitInc");
			//}
			/*else{
				formObject.setLocked("Limit_Inc_Frame4",true);
			}*/
		}
		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FCU_Decision_Frame1",true);
			
		}
		else if ("CAD".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CAD_Frame1",true);
			
		}
		else if ("CAD_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CAD_Decision_Frame1",true);
			
		}
		
		else if ("OriginalValidation".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OriginalValidation_Frame1",true);
			
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

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification_Frame1",true);
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");

			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");

		}
		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);
			
		}
		// added by abhishek as per CC FSd
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			//notepad_withoutTelLog();
			//change done by nikhil for PCAS-2356
			formObject.setVisible("NotepadDetails_Frame3",true);
			formObject.setLocked("NotepadDetails_Frame3",true);
			
			
		}
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck_Frame1",true);
			
		}
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Compliance_Frame1",true);
			
		}
		else if ("CardCollection".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CardCollection_Frame1",true);
			
		}
		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SalaryEnq_Frame1", true);
		}
		
		// fcu fragments start
		else if ("CustDetailVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification1_Frame1",true);
			
		}
		else if ("BussinessVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification1_Frame1",true);
			
		}
		else if ("EmploymentVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EmploymentVerification_Frame1",true);
			
		}
		else if ("BankingCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BankingCheck_IFrame1",true);
			
		}
		//commented by abhishek as per CC FSD
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
			formObject.setLocked("NotepadDetails_Frame1",true);
			
		}*/
		else if ("supervisorsection".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("supervisorsection_Frame1",true);
			
		}
		
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck1_Frame1",true);
			
		}
		
		// fcu fragments end
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
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

