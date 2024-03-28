/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_TO_Team.java
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_TO_Team extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("PL_TO_Team Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("PL TO Team"+ "Exception:"+e.getMessage());
			printException(e);
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1",true);
		}	
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			formObject.setLocked("Product_Frame1",true);
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame2",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Liability_New_Frame1",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");

		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
			loadEligibilityData();
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("AltContactDetails_Frame1",true);
		} 
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("FATCA_Frame1",true);
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("KYC_Frame1",true);
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("OECD_Frame1",true);
		}
		//added  by yash on 26/12/2017
		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS,cmplx_Decision_AppID#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
			loadPicklist1();
			//++Below code added by yash 15/12/17 for toteam decision
			formObject.setEnabled("cmplx_Decision_New_CIFNo",false);
			formObject.setEnabled("cmplx_Decision_IBAN",false);
			formObject.setVisible("DecisionHistory_Label26",false);
			//++above code added by yash on 15/12/2017 for toteam decision
			
			
			//for decision fragment made changes 8th dec 2017
			
		} 	
		//++below code changed added by yash for toteam
				else if("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					//String sQuery = "Select distinct DocName FROM ng_rlos_incomingDoc WHERE  docname like 'liability_certificate%' and DocSta = 'Received' and wi_name='"+formObject.getWFWorkitemName()+"'";
					String sQuery = "Select distinct DocName FROM ng_rlos_incomingDoc WHERE  docname like 'liability_certificate%' and wi_name='"+formObject.getWFWorkitemName()+"'";
					LoadPickList("PostDisbursal_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
					LoadPickList("PostDisbursal_BG_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
					LoadPickList("PostDisbursal_Emirate", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_emirate with (nolock)  where isActive='Y' order by code");
					mLogger.info("LC query before load is ::"+sQuery);
					LoadPickList("PostDisbursal_LC_LC_Doc_Name", sQuery);

					LoadPickList("PostDisbursal_MCQ_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_BG_LC_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_NLC_LC_Doc_name", sQuery);
					formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);

					formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
					formObject.setLocked("PostDisbursal_NLC_LC_Doc_name",true);
					mLogger.info("LC query after load is ::"+sQuery);
					
					int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");

					mLogger.info("LC query before load is ::"+sQuery);
					LoadPickList("PostDisbursal_LC_LC_Doc_Name", sQuery);

					LoadPickList("PostDisbursal_MCQ_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_BG_LC_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_NLC_LC_Doc_name", sQuery);
					formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);

					formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
					formObject.setLocked("PostDisbursal_NLC_LC_Doc_name",true);
					mLogger.info("LC query after load is ::"+sQuery);
					//int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");

					boolean active_flag=false;
					for(int i=0;i<row_count;i++)
					{
						if(formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate", row_count-1, 7).equalsIgnoreCase("Active"))
						{
							active_flag=true;
						}
					}
					if(!active_flag)
					{
					formObject.setLocked("PostDisbursal_Frame3", true);
					formObject.setLocked("PostDisbursal_Frame6", true);
					formObject.setLocked("PostDisbursal_Frame4", true);
					formObject.setLocked("PostDisbursal_Frame5",true);
					}
				}
				//--above code added by nikhil for toteam
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			mLogger.info(" In PL_Iniation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);

			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
			break;

		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;

		case VALUE_CHANGED:
			new PersonalLoanSCommonCode().value_Change(pEvent);

			break;

		default: break;

		}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		//empty method

	}


	public void initialize() {
			//empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
			//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method

		//++ below code added by Deepak on 19/03/2018 for EFMS refresh functionality
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();

		String decision_str = formObject.getNGValue("cmplx_Decision_Decision");
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_Submit_Desc").equalsIgnoreCase(decision_str)){
			objInput.add("Text:"+formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0));
			formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput);
		}

		//++ above code added by Deepak on 19/03/2018 for EFMS refresh functionality	
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
		loadInDecGrid();
	}

}
