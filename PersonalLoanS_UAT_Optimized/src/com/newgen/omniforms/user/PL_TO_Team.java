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


import java.util.HashMap;
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
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			formObject.setLocked("Product_Frame1",true);
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame2",true);
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Liability_New_Frame1",true);
		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
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
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist1();
			//++Below code added by nikhil 4/12/17 for toteam decision
			formObject.setVisible("Decision_Label1", false);
			formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED", false);
			formObject.setVisible("DecisionHistory_Label6", false);
			formObject.setVisible("cmplx_Decision_IBAN", false);
			formObject.setVisible("DecisionHistory_Label6", false);
			formObject.setVisible("cmplx_Decision_desc", false);
			formObject.setVisible("DecisionHistory_Label3", false);
			formObject.setVisible("cmplx_Decision_strength", false);
			formObject.setVisible("DecisionHistory_Label4", false);
			formObject.setVisible("cmplx_Decision_weakness", false);
			formObject.setVisible("DecisionHistory_Label9", false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
			formObject.setVisible("DecisionHistory_Label8", false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
			formObject.setVisible("DecisionHistory_Label7", false);
			formObject.setVisible("cmplx_Decision_AccountNo", false);
			formObject.setVisible("DecisionHistory_chqbook", false);
			
		} 	
		//++below code added by nikhil for toteam
				else if("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					LoadPickList("PostDisbursal_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
					LoadPickList("PostDisbursal_BG_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
					LoadPickList("PostDisbursal_Emirate", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_emirate with (nolock)  where isActive='Y' order by code");
					int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
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

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}
