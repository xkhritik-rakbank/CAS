/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : DDVT Maker

File Name                                                         : CC_DDVT_Maker

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Reference No/CR No   Change Date   Changed By    Change Description
1. 				   9-6-2017      Disha         Changes done to load master values in world check birthcountry and residence country
2.					14-9-2017	Saurabh			commented else if condition which was firing customer details at part match.
1005				17-9-2017	Saurabh			added else condition for making TL no in partmatch invisible for Salaried.
1006				17-9-2017	Saurabh			change for overlapping fragments after add to application in Part match
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;


import java.util.List;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;

import com.newgen.omniforms.listener.FormListener;


public class CC_DDVT_maker extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	String ReqProd=null;
	
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
		makeSheetsInvisible(tabName, "8,9,11,12,13,14,15,16,17");		

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
			CreditCard.mLogger.info("Inside CC_DDVT_maker CC");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");//added By Akshay on 1/8/2017 to set Mob No 2 in part match
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_CreditCard.getGlobalVar("DDVT_Frame_Name"));
			

			//formObject.fetchFragment("ProductContafrainer", "Product", "q_product");
			//formObject.setNGFrameState("ProductContainer",0);
			//formObject.setNGValue("QueueLabel","CC_DDVT_maker");

			new CC_CommonCode().setFormHeader(pEvent);
			
			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			formObject.setNGFrameState("Part_Match",0);
			//String loanAmt = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3);
			//formObject.setNGValue("LoanLabel",loanAmt);

			//partMatchValues();


			/*if(formObject.getNGValue("cmplx_PartMatch_partmatch_flag").equalsIgnoreCase("false")){
				formObject.setLocked("PartMatch_Search", true);
			}*/

			//formObject.setNGValue("AppRefNo", formObject.getWFFolderId());
			//Added By Shivang for PCASP-2334
			formObject.setNGValue("Old_PassportNo", formObject.getNGValue("cmplx_Customer_PAssportNo"));
			formObject.setVisible("Customer_FetchFirco", true);
			//Deepak Code change done default the value of Phone no to 00971 in case it starts with 971 or without 971 -- 29 June 22
			set_ValidateMobileNo("AlternateContactDetails_MobileNo1");
			set_ValidateMobileNo("AlternateContactDetails_MobNo2");
			set_ValidateMobileNo("AlternateContactDetails_OfficeNo");

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
	
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().FrameExpandEvent(pEvent);						
			break;

		case FRAGMENT_LOADED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
			CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());			
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
		//below code added by akshay for proc 9646 on 27/5/18
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String CRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0, 2);
		for(int i=1;i<formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
		{
			CRN=CRN+","+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
		}
		formObject.setNGValue("CRN", CRN);
		formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));
		formObject.setNGValue("ECRNLabel",formObject.getNGValue("ECRN")); 

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
		UpdatePrimaryCif();
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
		CreditCard.mLogger.info("Submit form started method start:: Winame: "+ formObject.getWFWorkitemName()+" user name: "+ formObject.getUserName());
		//Added by shivang for saving sub product in ext table for PCASP-1626
		CreditCard.mLogger.info("@Shivang::Submit form started method start:: Sub_Product: "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2));	
		formObject.setNGValue("Sub_Product", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2));
		
		Check_selectedCard();
		Check_All_Limits();
		/*Check_EFC_Limit();*/
		SetAccount();
		FinacleCRMGridCIFblank();
		try{
			Custom_fragmentSave("IncomeDEtails"); //Deepak code change to save statement cycle.
			//Deepak new condition added for case which is changed from existing to NTB on 20 May 22
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNo"))){
				formObject.setNGValue("formObject.getNGValue","");
			}
			Data_reset("DecisionHistory");
			//deepak coded added to save processby
			String dec = formObject.getNGValue("cmplx_DEC_Decision");
			CreditCard.mLogger.info("cmplx_DEC_Decision value: "+ dec);
			if(dec!=null && !"".equals(dec) && "Refer".equalsIgnoreCase(dec)){
				CreditCard.mLogger.info("inside set value for ProcessBy: "+ formObject.getUserName());
				formObject.setNGValue("ProcessBy", formObject.getUserName());
				CreditCard.mLogger.info("after set value for ProcessBy: "+ formObject.getNGValue("ProcessBy"));
			}
			CustomSaveForm();
			String CRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0, 2);
			for(int i=1;i<formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
			{
				CRN=CRN+","+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
			}
			
			formObject.setNGValue("CRN", CRN);
			formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));
			//changed by nikhil 27/11 to set header
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			formObject.setNGValue("LoanLabel",formObject.getNGValue("Final_Limit"));
			//added by nikhil to set new Email ID
			formObject.setNGValue("email_id",formObject.getNGValue("AlternateContactDetails_Email1"));
			//Communications issue
			double age=Double.parseDouble(formObject.getNGValue("cmplx_Customer_Age"));
			if(age>65)
			{
				String query="update ng_rlos_IGR_Eligibility_CardLimit set  CardShield='false' where Child_Wi='"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'";
				formObject.saveDataIntoDataSource(query);
			}
			Save_Card_desc();
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in set ext table data: "+e.getMessage());
		}
		
		try{
			//++Below code added by nikhil 11/11/17
			saveIndecisionGrid();
		
			//saveIndecisionGridCSM(); //Added by arun (22/11/17) to include ddvt maker record in decision grid
			//--above code added by nikhil 11//11/17
			
			String dec = formObject.getNGValue("cmplx_DEC_Decision");
			if(dec!=null && !"".equals(dec) && "Submit".equalsIgnoreCase(dec)){
				formObject.setNGValue("Decision","Approve" );
			}
			else{
				formObject.setNGValue("Decision",dec);
			}
			
			//Deepak Changes done for PCAS-2577
			if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_VerificationRequired")))
			{
				CreditCard.mLogger.info("CC val change "+ "Inside Y of CPV required");
				formObject.setNGValue("CPV_REQUIRED","Y");
			}
			else
			{
				CreditCard.mLogger.info("CC val change "+ "Inside N of CPV required");
				formObject.setNGValue("CPV_REQUIRED","N");
			}
			if("PA".equals(formObject.getNGValue("Sub_Product")))
			{
				formObject.setNGValue("CPV_REQUIRED","N");
			}
			LoadReferGrid();
			formObject.setNGValue("IS_Stage_Reversal", "");
			formObject.setNGValue("CPV_Analyst_dec", "");
			formObject.setNGValue("CAD_ANALYST1_DECISION", "");
			formObject.setNGValue("CAD_Analyst2_Dec", "");
			formObject.setNGValue("CPV_DECISION", "");
			formObject.setNGValue("CPV_Analyst_dec", "");
			 String newCif = formObject.getNGValue("CardDetails_newPrimaryCif");
			 //CreditCard.mLogger.info("NewCif: Submit form "+ newCif);
			 //CreditCard.mLogger.info("old CIF: "+ formObject.getNGValue("cmplx_Customer_CIFNo"));
			 CreditCard.mLogger.info("NTB flag: "+ formObject.getNGValue("cmplx_Customer_NTB"));
			 if((!formObject.getNGValue("cmplx_Customer_CIFNo").equalsIgnoreCase(newCif)) && (!"".equalsIgnoreCase(newCif) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))) ){
				 //CreditCard.mLogger.info("Inside CIF update condition: "+ newCif);
				 formObject.setNGValue("cmplx_Customer_CIFNo",newCif);
				 formObject.setNGValue("CIF_ID",newCif);
				 formObject.setNGValue("CifLabel",newCif);
			 }
			 Custom_fragmentSave("CustomerDetails");
			 Custom_fragmentSave("AddressDetails");
			 Custom_fragmentSave("Address_Details_container");
		}//cmplx_Customer_FirstNAme
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in set decision+fragment data save table data: "+e.getMessage());
		}
		//CompanyDetails_Aloc_search
		try
		{
			CreditCard.mLogger.info("CC val change "+ "befor incomingdoc");
			//IncomingDoc();
			validateStatusForSupplementary();
			CreditCard.mLogger.info("CC val change "+ "after incomingdoc");
		}
		catch(Exception ex)
		{
			CreditCard.mLogger.info(ex);
			printException(ex);
		}
		
		/*//added by Nikita on 17 mar 2021 for the NTB check
				String alert_msg="";
				try
				{
					CreditCard.mLogger.info("NTB Check started ");
					
					if(validatefirstTimeDone())
					{
							if(validateNTB()==true && FinacleCRMGridCIFblank()==false)
								alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL124");
							else if(validateNTB()==false && FinacleCRMGridCIFblank()==true)
								alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL125");
					}
				}
				catch(Exception ex)
				{
					CreditCard.mLogger.info(ex);
					printException(ex);
				}	*/
				
		
		//--above code added by nikhil 11//11/17
		CreditCard.mLogger.info("Submit form started method end:: Winame: "+ formObject.getWFWorkitemName()+" user name: "+ formObject.getUserName());
	
	}
	
	private void FinacleCRMGridCIFblank(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg = "";
		CreditCard.mLogger.info("inside FinacleCRMGridCIFblank cmplx_Customer_CIFNo: " + formObject.getNGValue("cmplx_Customer_CIFNo"));
		CreditCard.mLogger.info("inside FinacleCRMGridCIFblank cmplx_DEC_New_CIFID: " + formObject.getNGValue("cmplx_DEC_New_CIFID"));
		try{
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				if(!formObject.getNGValue("cmplx_Customer_CIFNo").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_New_CIFID"))){
					if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNo"))){
						CreditCard.mLogger.info("inside FinacleCRMGridCIFblank: to set Dic CIF as blank as NTB is true and cif in primaryinfo is blank");
						//alert_msg="NTB is true and cif in primaryinfo is blank";
						formObject.setNGValue("cmplx_DEC_New_CIFID","");
					}
					else{
						alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL124");
					}
					
				}
			}
			else{
				String persoan_cif = formObject.getNGValue("cmplx_Customer_CIFNo");
				boolean cif_match = false;
				for(int i=0;i<formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");i++)
				{
					if(persoan_cif.equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid", i, 0)) && "true".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid", i, 13)))
					{
						cif_match= true;
						break;
					}

				}
				if((!cif_match) && formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid")>0){
					alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL125");
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in FinacleCRMGridCIFblank: "+ e.getMessage());

		}

		if(!alert_msg.equalsIgnoreCase("")){
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
	}
	
	/*private boolean validateNTB()
	{
		boolean NTBFlag=true;
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String NTB=formObject.getNGValue("cmplx_Customer_NTB");
		if(NTB.equalsIgnoreCase("true"))
			NTBFlag=true;
		else if(NTB.equalsIgnoreCase("false"))
			NTBFlag=false;
		
		return NTBFlag;
	}

	private boolean validatefirstTimeDone()
	{
		String workstep_name="";
		String decision="";
		boolean flag=true;
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			for(int i=0;i<formObject.getLVWRowCount("DecisionHistory_Decision_ListView1");i++){	
				workstep_name = formObject.getNGValue("DecisionHistory_Decision_ListView1",i,2);
				decision = formObject.getNGValue("DecisionHistory_Decision_ListView1",i,3);
				if(workstep_name.equals("DDVT_Maker") && decision.equals("Approve"))
				{
					flag= false;
					break;
					
				}
			}
		}
		
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in validatefirstTimeDone" +e.getMessage());
		}
		return flag;
	}

	private boolean FinacleCRMGridCIFblank()
	{
			String Cif="";
			boolean CIFBlank=false;
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int Row_count=formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");
			
			try{	
				for(int i=0;i<Row_count;i++)
				{
					Cif=formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid", i, 0);
					if(Cif=="")
					{
						CIFBlank= true;
						break;
					}
					
				}
			}
			catch(Exception e){
			CreditCard.mLogger.info("Exception occured in validatefirstTimeDone" +e.getMessage());
		}
			return CIFBlank;
	}	
*/	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		CreditCard.mLogger.info("@Shivang :: Inside Fragment Load");
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			CreditCard.mLogger.info("@Shivang :: Inside Customer Fragment Load");
			if("".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				//formObject.setLocked("Customer_Frame1",true);
				LoadView(pEvent.getSource().getName());
				formObject.setLocked("Customer_save",false);					
				formObject.setLocked("Customer_Frame3",false);
				formObject.setVisible("Customer_Frame2", false);
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
				// ++ below code not commented at offshore - 06-10-2017
				formObject.setLocked("cmplx_Customer_MobileNo",true);
			}

			//cmplx_DEC_MultipleApplicantsGrid
			//temp done by Aman for Sprint 1
			formObject.setLocked("cmplx_Customer_CIFNo",true);
			formObject.setLocked("Customer_Button1",true);
			formObject.setEnabled("cmplx_Customer_CIFNo",false);
			formObject.setEnabled("Customer_Button1",false);
			//temp done by Aman for Sprint 1
			formObject.setLocked("cmplx_Customer_CARDNOTAVAIL",true);
			formObject.setLocked("Customer_ReadFromCard",false);
			formObject.setLocked("cmplx_Customer_NEP",true);
			formObject.setLocked("cmplx_Customer_age", true);
			formObject.setLocked("cmplx_Customer_DLNo",false);
			formObject.setLocked("cmplx_Customer_PAssport2",false);
			formObject.setLocked("cmplx_Customer_Passport3",false);
			formObject.setLocked("cmplx_Customer_PASSPORT4",false);
			formObject.setLocked("Customer_Button1", false);
			//formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
			loadPicklistCustomer();

			//added by abhishek on 29may2017

			formObject.setLocked("cmplx_Customer_EmiratesID",false);
			formObject.setLocked("cmplx_Customer_FirstNAme",false);
			formObject.setLocked("cmplx_Customer_MiddleNAme",false);
			formObject.setLocked("cmplx_Customer_LastNAme",false);

			// ++ below code not commented at offshore - 06-10-2017
			//formObject.setLocked("cmplx_Customer_DOb",false);
			formObject.setEnabled("cmplx_Customer_DOb",true);
			formObject.setLocked("Nationality_Button",false);
			// ++ below code commented at offshore - 06-10-2017
			formObject.setLocked("cmplx_Customer_MobileNo",true);
			formObject.setLocked("cmplx_Customer_PAssportNo",false);
			formObject.setLocked("cmplx_Customer_card_id_available",false);
			formObject.setLocked("Customer_FetchDetails",false);
			formObject.setLocked("cmplx_Customer_CIFID_Available",false);
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFID_Available"))){
				formObject.setLocked("cmplx_Customer_CIFNo",false);
			}
			//++Below code added by nikhil 22/11/2017 

			formObject.setLocked("cmplx_Customer_IdIssueDate", false);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", false);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", false);
			formObject.setLocked("cmplx_Customer_VisaExpiry", false);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", false);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
			formObject.setLocked("cmplx_Customer_MotherName", false);
			formObject.setLocked("cmplx_Customer_IsGenuine",true);
			//--Above code added by nikhil 22/11/2017 
			// added for point 28
			formObject.setVisible("Customer_ReadFromCard",false);
			// added by abhishek acc. to new FSD for CC
			formObject.setLocked("Customer_FetchDetails",false);	
			setcustomer_enable();
			//below code added by nikhil 08/12/17
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_Customer_MobileNo",true);
			}
			else
			{
				formObject.setLocked("cmplx_Customer_MobileNo",true);
			}
			formObject.setLocked("cmplx_Customer_NTB",true);

			if(formObject.getNGValue("cmplx_Customer_age")!=null && !formObject.getNGValue("cmplx_Customer_age").equals("") && Double.parseDouble(formObject.getNGValue("cmplx_Customer_age"))>18){
				formObject.setNGValue("cmplx_Customer_minor", true);
			}
		}	
		else if ("Internal_External_Liability".equalsIgnoreCase(pEvent.getSource().getName())) {
			//changes for fragments disabled done by saurabh
			formObject.setLocked("ExtLiability_Frame1", true);
		}

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select '--Select--' union select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			//++Below code added by nikhil 7/11/17 as per CC issues sheet
			formObject.setLocked("ReqProd", true);
			formObject.setLocked("Product_type",true);
			formObject.setEnabled("subProd",true);
			formObject.setLocked("subProd",true);
			formObject.setEnabled("AppType",true);
			formObject.setLocked("AppType",true);
			formObject.setVisible("CardProd",true);
			formObject.setEnabled("CardProd",true);
			formObject.setVisible("EmpType",true);
			formObject.setEnabled("EmpType",true);
			//--Above code added by nikhil 7/11/17 as per CC issues sheet
			//changes done by prabhakar for jira 11046
			int prod_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prod_count>0){
				String sub_prd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2);
				if(("SEC").equalsIgnoreCase(sub_prd)){
					formObject.setVisible("Product_Label8",true);
					formObject.setVisible("FDAmount",true);
					//formObject.setVisible("Product_Label6",true);//Arun 18/12/17
					//formObject.setVisible("CardProd",true);//Arun 18/12/17

				}
			}
			formObject.setLocked("Product_Frame1",true);

		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//	formObject.setLocked("AddressDetails_Frame1",true);
			//formObject.setLocked("AddressDetails_Frame1",false);
			//below code commented by nikhil 07/01 for PCSP-519
			//LoadView(pEvent.getSource().getName());
			loadPicklist_Address();
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//below code commented by nikhil 07/01 for PCSP-519
			//LoadView(pEvent.getSource().getName());
			//formObject.setLocked("AltContactDetails_Frame1",false);
			//below code added by nikhil for loadpicklist of field
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh for air arabia functionality.
			AirArabiaValid();
			enrollrewardvalid();//added by saurabh1 for enroll
			//by saurabh1 for enroll
			if(formObject.isVisible("AlternateContactDetails_EnrollRewardsIdentifier")){
				formObject.setLocked("AlternateContactDetails_EnrollRewardsIdentifier",false);
				formObject.setEnabled("AlternateContactDetails_EnrollRewardsIdentifier",true);
			}//saurabh1 for enroll end
			//change by saurabh for Air Arabia functionality.
			if(formObject.isVisible("AlternateContactDetails_AirArabiaIdentifier")){
				formObject.setLocked("AlternateContactDetails_AirArabiaIdentifier",false);
				formObject.setEnabled("AlternateContactDetails_AirArabiaIdentifier",true);
			}
			//CreditCard.mLogger.info("value of  AlternateContactDetails_AirArabiaIdentifier: + "+formObject.getNGValue("AlternateContactDetails_AirArabiaIdentifier"));

			//Deepak Changes done not to run Air Arabia dedupe in case filed value is already available
			if("".equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_AirArabiaIdentifier"))){
				if(null!=formObject.getNGValue("cmplx_Customer_CIFNO") && !"".equals(formObject.getNGValue("cmplx_Customer_CIFNO"))){
					String squeryair="select AIR_ARABIA_IDENTIFIER from CAPS_MAIN_MIG_DATA where CIF='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"'";
					List<List<String>> airarabia=formObject.getNGDataFromDataCache(squeryair);
					//CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE squeryair+ "+squeryair);

					if (null!=airarabia && !airarabia.isEmpty()){
						formObject.setNGValue("AlternateContactDetails_AirArabiaIdentifier",airarabia.get(0).get(0));
					}
					else {//Commented for sonar
						String queryair="select con.AIR_ARABIA_IDENTIFIER from ng_RLOS_AltContactDetails con inner join ng_rlos_customer cus on cus.wi_name = con.wi_name where cus.PassportNo='"+formObject.getNGValue("cmplx_Customer_PAssportNo")+"' and con.AIR_ARABIA_IDENTIFIER is not null";
						List<List<String>> airarabia1=formObject.getNGDataFromDataCache(queryair);
						if (null!=airarabia1 && !airarabia1.isEmpty()){
							formObject.setNGValue("AlternateContactDetails_AirArabiaIdentifier",airarabia1.get(0).get(0));
						}
					}
				}
				else{
					CreditCard.mLogger.info("CIF is empty for Air Arabia logic to work");
				}

			}
			//	LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to



		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {


			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			formObject.setVisible("ELigibiltyAndProductInfo_Refresh", false);
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalInterestRate", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate", true);
			String Curr_wiName = formObject.getWFWorkitemName();
			String Parent_wiName = formObject.getNGValue("parent_WIName");
			String sQuery_CardProduct = "IF NOT EXISTS(select * from ng_rlos_IGR_Eligibility_CardLimit WITH(nolock) WHERE Child_Wi = '"+Curr_wiName+"') BEGIN update ng_rlos_IGR_Eligibility_CardLimit set Child_Wi = '"+Curr_wiName+"' where Wi_Name = '"+Parent_wiName+"' END  ";
			//CreditCard.mLogger.info( "Query to insert: card if not exist: "+sQuery_CardProduct);
			formObject.saveDataIntoDataSource(sQuery_CardProduct);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadInDecGrid();
			LoadReferGrid();


			formObject.setLocked("cmplx_DEC_New_CIFID",true);
			CreditCard.mLogger.info( "inside decision history fragment load");

			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Telesales_Init").equalsIgnoreCase(formObject.getNGValue("InitiationType")))
			{
				loadPicklist3();
			}
			else
			{
				loadPicklistChecker();
			}
			//changed by nikhil increase decision reaon width current limit
			fragment_ALign("DecisionHistory_Label10,cmplx_DEC_New_CIFID#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
			formObject.setWidth("DecisionHistory_dec_reason_code", 210);
			//formObject.setTop("DecisionHistory_Decision_ListView1",330);
			//formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+20);
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017
			//Ref. 1007 end.
			//below code added by nikhil
			formObject.setLocked("cmplx_DEC_VerificationRequired", true);
			//below code added by niukhil for PCAS-1346
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("DecisionHistory_CifLock", true);
				formObject.setLocked("DecisionHistory_CifUnlock", true);
			}
			else
			{
				formObject.setLocked("DecisionHistory_CifLock", false);
				formObject.setLocked("DecisionHistory_CifUnlock", false);
			}
		} 	

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			// commented by abhishek as per the CC FSD
			//formObject.setLocked("OECD_Frame8",true);
			//below code commented by nikhil 07/01 for PCSP-519
			//LoadView(pEvent.getSource().getName());
			loadPicklist_oecd();

		}//CompanyDetails_Button3
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//formObject.setLocked("KYC_Frame1",true);
			formObject.setLocked("KYC_Frame1",false);
			loadPicklist_KYC();

		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Tanshu Aggarwal changed as per the conversation with Shashank
			//formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("FATCA_Frame6",false);
			// ++ below code not commented at offshore - 06-10-2017
			formObject.setVisible("FATCA_DocsCollec", true);
			formObject.setVisible("FATCA_TypeOFRelation", true);
			loadPicklist_Fatca();

		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame",false);
			//shifted from CommonCode by akshay on 17/1/18
			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
				formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
			else if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
				//formObject.setVisible("IncomingDoc_UploadSig",true);
				//formObject.setEnabled("IncomingDoc_UploadSig",false);
			}
		}
		else if("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			// added by abhishek as per CC FSD
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)) && "yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Supplementary_Card"))){
				formObject.setLocked("SupplementCardDetails_Frame1",false);
			}
			openDemographicTabs();
			LoadPickList("SupplementCardDetails_MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode with (nolock) where isActive='Y' order by code");
			loadPicklist_suppCard();
			//CreditCard.mLogger.info( "@Shivang :: NAtionality"+formObject.getNGValue("SupplementCardDetails_Nationality"));
			if(formObject.getNGValue("SupplementCardDetails_Nationality").equalsIgnoreCase("AE")){
				formObject.setLocked("SupplementCardDetails_VisaNo",true);
				formObject.setLocked("SupplementCardDetails_VisaIssueDate",true);
				formObject.setLocked("SupplementCardDetails_VisaExpiry",true);
			}
			/*PCASP-592
			 * formObject.setNGValue("SupplementCardDetails_ResidentCountry","AE");
			formObject.setLocked("SupplementCardDetails_ResidentCountry",true);*/
			SupplementaryenrollrewardValid();

		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//++Below code added by nikhil 13/11/2017 for Code merge
			//LoadPickList("cmplx_CardDetails_Statement_cycle", "select '--Select--' as Description , '' as code union select convert(varchar, Description),code from NG_MASTER_StatementCycle with (nolock)");
			//	LoadPickList("CardDetails_BankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
			Loadpicklist_CardDetails_Frag();
			loadDataInCRNGrid();
			/*if(formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")>0){
				if ("LI".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
				{
					formObject.setLocked("CardDetails_Frame1",true);
				}
			}---commented by akshay on 3/5/18 for proc 8978*/

			//added by nikhil for PCAS-1329
			IslamicFieldsvisibility();
			//added by nikhil for disbursal showstopper
			Loadpicklist_CardDetails_Frag();
			//--Above code added by nikhil 13/11/2017 for Code merge
			//++below code added by nikhil for Self-Supp CR
			Load_Self_Supp_Data();
			//--above code added by nikhil for Self-Supp CR
			CreditCard.mLogger.info( "@Shivang :: subprod"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			//added by saurabh1 for founder member start
			String founderquery="select count(CreationGrid_winame) from NG_RLOS_gr_CCCreation where interestProfile like '%SKYWARDS%' and cardCreated='Y' and applicanttype='Primary'";
			List<List<String>> result=formObject.getDataFromDataSource(founderquery);
			int foundercount=Integer.parseInt(result.get(0).get(0));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5).contains("EKWE") && Check_Elite_Customer1(formObject)&& foundercount<=Integer.parseInt(getValueOfConstant("Founder_Count"))) {
				formObject.setVisible("CardDetails_Label2", true);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossing_name", true);
				formObject.setNGValue("cmplx_CardDetails_CompanyEmbossing_name","FOUNDER MEMBER");
				formObject.setEnabled("cmplx_CardDetails_CompanyEmbossing_name", false);
				formObject.setLocked("cmplx_CardDetails_CompanyEmbossing_name", true);
			}
			//added for saurabh1 for founder member end
			if(!(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC"))){
				formObject.setLocked("cmplx_CardDetails_CompanyEmbossing_name", true);
			}
			formObject.sortNGListview("cmplx_CardDetails_cmplx_CardCRNDetails", 9, false);
		}



		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//added 9/08/2017 Tanshu 

			String subprod =formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			CreditCard.mLogger.info( "subprod"+subprod+",emptype:"+empType);
			if("Business titanium Card".equalsIgnoreCase(subprod)&& "Self Employed".equalsIgnoreCase(empType)){
				CreditCard.mLogger.info( "inside if condition");
				formObject.setEnabled("IncomeDEtails", false);

			}
			//added 9/08/2017 Tanshu
			/*	formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
			//onsite the field name is cmplx_IncomeDetails_totSal and in offshore file cmplx_IncomeDetails_TotSal
			formObject.setLocked("cmplx_IncomeDetails_totSal", true);
			formObject.setLocked("cmplx_IncomeDetails_netSal1", true);
			formObject.setLocked("cmplx_IncomeDetails_netSal2", true);
			formObject.setLocked("cmplx_IncomeDetails_netSal3", true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal", true);
			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Commission_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg", true);
			formObject.setLocked("cmplx_IncomeDetails_Flying_Avg", true);*/
			/*LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			 */
			loadpicklist_Income();
			//changes for fragments by saurabh
			formObject.setLocked("IncomeDetails_Frame1", true);
			if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setEnabled("IncomeDetails_FinacialSummarySelf", true);
				formObject.setEnabled("IncomeDetails_RefreshFinacle", true);
				
				//Added by shivang for PCASP-1821
				if("Self Employed".equalsIgnoreCase(empType)){
					formObject.setLocked("cmplx_IncomeDetails_Account_self_num",false);
					formObject.setLocked("IncomeDetails_SelfEmployed_Save",false);
					formObject.setLocked("cmplx_IncomeDetails_StatementCycle2",false);
					
				}else{
					formObject.setLocked("cmplx_IncomeDetails_Account_num_sal",false);
					formObject.setLocked("IncomeDetails_Salaried_Save",false);
				}
			}

		}

		else if ("Finacle_CRM_Incidents".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Finacle_CRM_Incidents", true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadinFinacleCRNGrid(formObject);
			if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
			{
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
			}
			else
			{
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Corporate", "Corporate");
			}

		}

		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			CreditCard.mLogger.info( "Fter Load Pick Company: 1");
			//formObject.setLocked("cmplx_CompanyDetails_cmplx_CompanyGrid_CompanyCIF", true);
			formObject.setLocked("CompanyDetails_Button3", true);
			formObject.setLocked("CompanyDetails_Frame1", true);
			CreditCard.mLogger.info( "Fter Load Pick Company: 2");
			//below code added by nikhil 08/12/17
			formObject.setLocked("CompanyDetails_Frame2",true);
			formObject.setLocked("CompanyDetails_Frame3",true);
			//below code added by nikhil
			formObject.setLocked("CompanyDetails_EffectiveLOB", true);
			/*	LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
			LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
			 */	loadPicklist_CompanyDet();
			 CreditCard.mLogger.info( "Fter Load Pick Company: ");
			 //formObject.setLocked("MarketingCode",false);
			 //formObject.setLocked("ClassificationCode",false);
			 //formObject.setLocked("PromotionCode",false);
			 //formObject.setLocked("CompanyDetails_Frame1",true);--commented by akshay for proc 4195
			 formObject.setLocked("CompanyDetails_Frame2",true);
			 formObject.setLocked("CompanyDetails_Frame3",true);
			 formObject.setLocked("AuthorisedSignDetails_DOB",true);
			 formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",true);
			 formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",true);
			 formObject.setLocked("PartnerDetails_Dob",true);
			 formObject.setLocked("TLIssueDate",true);
			 formObject.setLocked("TLExpiry",true);
			 formObject.setLocked("estbDate",true);

			 formObject.setLocked("ApplicationCategory",false);
			 formObject.setLocked("TargetSegmentCode",false);
			 formObject.setLocked("PromotionCode",false);
			 formObject.setLocked("MarketingCode",false);
			 formObject.setLocked("ClassificationCode",false);
			 formObject.setLocked("CompanyDetails_Add",false);
			 formObject.setLocked("CompanyDetails_Modify",false);
			 formObject.setLocked("CompanyDetails_delete",false);

			 setComapnyCode();
			 AECBHistMonthCnt();
			 /*String customerName = formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
		new CC_CommonCode().AddInAuthorisedGrid(formObject,customerName);
		new CC_CommonCode().AddInCompanyGrid(formObject, customerName);*/
		}

		/*else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails_AuthSignCIFNo", true);
			formObject.setLocked("AuthorisedSignDetails_Button4", true);
			//LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
			//LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			// added by abhishek as per CC FSD
			formObject.setLocked("AuthorisedSignDetails_DOB",true);
			formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",true);
			formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",true);
		}

		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartnerDetails_Frame1",true);
			formObject.setLocked("PartnerDetails_Dob",true);
		}
		 */
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//addded for cas simplification

			//formObject.setLocked("EMploymentDetails_Frame1", true);
			//++Below code added by nikhil 6/11/17
			//loadPicklistEmployment();
			loadPicklist4();
			Fields_ApplicationType_Employment();
			//--Above code added by nikhil 6/11/17
			//loadPicklist4();
			//++below code added by abhishek for CAD point 3 on 7/11/2017
			//Deepak 10 Aug 2019 - PCAS-2484
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);

			//done by sagarika for CR
			/*String emp_CurrEmp =formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
			if("CN".equalsIgnoreCase(emp_CurrEmp))
			{

				formObject.setEnabled("cmplx_EmploymentDetails_Indus_Micro",true);
				formObject.setEnabled("cmplx_EmploymentDetails_Indus_Macro",true);
			}*/
			//Done by aman as per verification sheet 17/12
			//done by sagarika for PCSP-207
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")))
			{
				CreditCard.mLogger.info("inside NEP if condition");
				formObject.setLocked("EMploymentDetails_Label25", false);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", false);
			}
			else{
				CreditCard.mLogger.info("inside NEP else condition");
				formObject.setLocked("EMploymentDetails_Label25", true);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
			}
			//formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
			formObject.setLocked("cmplx_EmploymentDetails_FieldVisitDone",false);
			//Done by aman as per verification sheet 17/12

			//++above code added by abhishek for CAD point 3 on 7/11/2017
			//++Below code added by nikhil 8/11/17 as per CC issues sheet
			//change by saurabh on 30th Nov
			/*formObject.setVisible("EMploymentDetails_Label32", true);
			formObject.setVisible("cmplx_EmploymentDetails_FieldVisitDone",true);*/

			//--Above code added by nikhil 8/11/17 as per CC issues sheet
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
			{
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
				formObject.setVisible("EMploymentDetails_Label15", true);
			}
			else
			{
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
				formObject.setVisible("EMploymentDetails_Label15", false);
			}
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmirateOfWork",true);
			//formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",true);
			formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmployerType",true);             


			//closed for cas simplication

			/*formObject.setVisible("EMploymentDetails_Label28", true);
			formObject.setVisible("cmplx_EmploymentDetails_StaffID", true);//
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("EMploymentDetails_Combo5",false);
			formObject.setVisible("EMploymentDetails_Label33",false);
			formObject.setVisible("cmplx_EmploymentDetails_Collectioncode",false);
			formObject.setVisible("EMploymentDetails_Label37",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
			//formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
				formObject.setVisible("EMploymentDetails_Label62",false);
					formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			   //Code commented because freezone need to be present on DDVT Maker 31/10
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
			//formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			formObject.setVisible("EMploymentDetails_Label36",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
			formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
			loadPicklist4();
			formObject.setLocked("EMploymentDetails_Frame2", true);
			formObject.setLocked("EMploymentDetails_Text21", false);
			formObject.setLocked("EMploymentDetails_Text22", false);
			formObject.setLocked("EMploymentDetails_Button2", false);
			// aqdded by abhishek as per CC FSD
			formObject.setVisible("EMploymentDetails_Label25",true);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",true);s
			formObject.setVisible("EMploymentDetails_Label36",true);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",true);

			formObject.setLocked("EMploymentDetails_Save",false);
			//++Below code added by nikhil 7/11/17 as per CC issues sheet
			//below if condition added by 08/12/17
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)))
			{
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt",true);
			formObject.setVisible("EMploymentDetails_Label15",true);
			formObject.setVisible("EMploymentDetails_Label36",true);
			formObject.setVisible("cmplx_EmploymentDetails_FieldVisitDone",true);
			}
			//--Above code added by nikhil 7/11/17 as per CC issues sheet
			//++Below code added by nikhil 8/11/17 as per CC issues sheet
			//below code modified by nikhil 08/12/17
			formObject.setLocked("cmplx_EmploymentDetails_empmovemnt",true);
			formObject.setVisible("EMploymentDetails_Label32",true);
			formObject.setLocked("cmplx_EmploymentDetails_FieldVisitDone",true);
			//--Above code added by nikhil 8/11/17 as per CC issues sheet
			 */			
			//formObject.setVisible("", false);
			AECBHistMonthCnt();
			//formObject.setNGValue("cmplx_EmploymentDetails_marketcode","BAU");
			formObject.setLocked("cmplx_EmploymentDetails_EmployerType",false);  


		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			ChangeRepeater();
			ChangeRepeaterTrnover();
			formObject.setLocked("FinacleCore_Frame9", true);
			formObject.setLocked("FinacleCore_Frame10", true);
			formObject.setLocked("FinacleCore_Frame11", true);
			// added by abhishek as per the CC FSD
			formObject.setLocked("FinacleCore_Frame1", true);
			LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Cheque_Type with (nolock)");
			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' as description union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock) order by description desc");
			//++below code added by nikhil for ddvt CC issues
			formObject.setLocked("FinacleCore_Frame7", true);	
			if("SEC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) || ("BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) && "SMES".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4))))
			{
				formObject.setEnabled("FinacleCore_Lien_COB_1",true);
				formObject.setLocked("FinacleCore_Lien_COB_1",false);
				formObject.setEnabled("FinacleCore_Lien_modify",true);
			}
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			try{
				//partMatchValues();
				CreditCard.mLogger.info("CC"+ "Inside partMatch frag loaded!!");
				String Fullname="";
				//LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
				//uncommented by nikhil as Add to Application is application Sprint-2 hence forth
				//formObject.setLocked("PartMatch_Button1", true);
				int framestate_Alt_Contact = formObject.getNGFrameState("Alt_Contact_container");
				if(framestate_Alt_Contact == 0){
					CreditCard.mLogger.info("Alternate details alread fetched");
				}
				else {
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					CreditCard.mLogger.info("fetched Alternate Contact details");
				}

				int framestate_CompDetails = formObject.getNGFrameState("CompDetails");
				if(framestate_CompDetails == 0){
					CreditCard.mLogger.info("CompDetails already fetched");
				}
				else {
					formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
					CreditCard.mLogger.info("fetched CompDetails details");
				}

				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");

				formObject.setNGValue("PartMatch_fname", formObject.getNGValue("cmplx_Customer_FirstNAme"));
				//CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_FirstNAme')"+ formObject.getNGValue("cmplx_Customer_FirstNAme"));
				formObject.setNGValue("PartMatch_lname", formObject.getNGValue("cmplx_Customer_LastNAme"));
				CreditCard.mLogger.info("formObject.getNGValue('cmplx_Customer_LastNAme')"+ formObject.getNGValue("cmplx_Customer_LastNAme"));
				if(formObject.getNGValue("cmplx_Customer_MiddleNAme")!=null || formObject.getNGValue("cmplx_Customer_MiddleNAme").equals("")){
					Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
				}
				else{
					Fullname = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
				}

				formObject.setNGValue("PartMatch_funame", Fullname);
				formObject.setNGValue("PartMatch_newpass", formObject.getNGValue("cmplx_Customer_PAssportNo"));
				formObject.setNGValue("PartMatch_oldpass", formObject.getNGValue("cmplx_Customer_PAssport2")); // old passport no. field to be populated
				formObject.setNGValue("PartMatch_visafno", formObject.getNGValue("cmplx_Customer_VisaNo"));
				formObject.setNGValue("PartMatch_mno1", formObject.getNGValue("AlternateContactDetails_MobileNo1"));
				formObject.setNGValue("PartMatch_mno2", formObject.getNGValue("AlternateContactDetails_MobNo2"));
				formObject.setNGValue("PartMatch_Dob", formObject.getNGValue("cmplx_Customer_DOb"));
				formObject.setNGValue("PartMatch_EID", formObject.getNGValue("cmplx_Customer_EmiratesID"));
				formObject.setNGValue("PartMatch_drno", formObject.getNGValue("cmplx_Customer_DLNo"));
				formObject.setNGValue("PartMatch_nationality", formObject.getNGValue("cmplx_Customer_Nationality"));
				formObject.setNGValue("PartMatch_CIFID", formObject.getNGValue("cmplx_Customer_CIFNo"));
				//added by akshay on 27/12/17
				if(!"Y".equals(formObject.getNGValue("is_fetch")))
				{
					formObject.setLocked("PartMatch_Search", true);
				}
				else
				{
					formObject.setLocked("PartMatch_Search", false);


				}
				if(!"Y".equals(formObject.getNGValue("Is_PartMatchSearch"))){
					formObject.setLocked("PartMatch_Blacklist", true);
					formObject.setLocked("PartMatch_Company_blacklist", true);
				}
				if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
					formObject.setVisible("PartMatch_Label10", true);
					formObject.setVisible("PartMatch_compname", true);
					formObject.setVisible("PartMatch_Label9", true);
					formObject.setVisible("PartMatch_TLno", true);
					//Deepak 12Nov2017 changes done to Set comp details from Grid. Set value of First Row.
					int comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					if(comp_row_count>1){
						formObject.setNGValue("PartMatch_compname",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));
						formObject.setNGValue("PartMatch_TLno",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,6));
						/*formObject.setNGValue("PartMatch_compname", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid_companyName"));
						formObject.setNGValue("PartMatch_TLno", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid_TLNO"));*/
					}
				}
				//Ref. 1005
				else{

					formObject.setVisible("PartMatch_Label9", false);
					formObject.setVisible("PartMatch_TLno", false);
				}
				if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))
				{
					formObject.setVisible("PartMatch_Company_blacklist", true);
				}
				else
				{
					formObject.setVisible("PartMatch_Company_blacklist", false);
				}
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,3)))
				{
					formObject.setLocked("PartMatch_Company_blacklist", true);
				}
				//Ref. 1005 end.
			}catch(Exception e){
				CreditCard.mLogger.info("eXCEPTION"+ printException(e));	
			}

		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Reference_Details_Frame1",true);

		}
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){

			String cardProduct = "";
			cardProduct=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			//Added by Shivang for PCASP-1896
			if(cardProduct.equalsIgnoreCase("LOC STANDARD") || cardProduct.equalsIgnoreCase("LOC PREFERRED")){
				formObject.setNGValue("transtype","LOC");
			}
			//Added by shivang for PCASP-2891
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
				formObject.setNGValue("transtype","LOC");
				formObject.setLocked("transtype", true);
			}
			loadPicklist_ServiceRequest();
			//below code added by nikhil 19/1/18
			String dds_mode=formObject.getNGValue("cmplx_CC_Loan_DDSMode");
			//below code also fix point "30-Service Details#Validations not as per FSD."
			if("F".equalsIgnoreCase(dds_mode))
				//above code also fix point "30-Service Details#Validations not as per FSD."
				//Changed by Shivang for PCASP-1499
			{

				formObject.setLocked("cmplx_CC_Loan_Percentage",true);
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);
				formObject.setEnabled("cmplx_CC_Loan_DDSAmount", true);
			}
			//below code also fix point "30-Service Details#Validations not as per FSD."
			else if("P".equalsIgnoreCase(dds_mode))
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
			formObject.setLocked("cmplx_CC_Loan_DDSEntityNo", true);
			//++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
			//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
			//formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
			//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

		}

		else if ("Salary_Enquiry".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Salary_Enquiry", true);
		}

		//++ Below Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			//notepad_withoutTelLog();
			//change done by nikhil for PCAS-2356
			formObject.setVisible("NotepadDetails_Frame3",true);
			formObject.setLocked("NotepadDetails_Frame3",true);
		}
		//++ Above Code added By Yash on Oct 6, 2017  to fix : "28-User name is being popltaed as customer name" : Reported By Shashank on Oct 05, 2017++

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("WorldCheck1_age",true);
			//++ Below Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017++
			loadPicklist_WorldCheck();
			//-- Above Code added By Abhishek on Oct 6, 2017  to fix : "18,19-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017--
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())){
			//	LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");

		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())){
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab"))){

				formObject.setVisible("Liability_New_Overwrite", true);

			}
			else{
				formObject.setVisible("Liability_New_Overwrite", false);
			}
			//below code added by nikhil 09/12/17
			if("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))
			{
				formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);
			}
			else
			{
				formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", true);
				formObject.setVisible("Liability_New_Label14",true);
				formObject.setVisible("cmplx_Liability_New_Aecb_Score_Company",true);
				formObject.setVisible("Liability_New_Label15",true);
				formObject.setVisible("cmplx_Liability_New_Aecb_range_Company",true);
			}
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

		}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1", true);

		}

		else if("CreditCardEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CreditCard_Enq", true);

		}


		else if("LOS".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LOS", true);

		}

		else if("CaseHistoryReport".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Case_hist", true);
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
