package com.newgen.omniforms.user;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.skutil.SKLogger;

import javax.faces.application.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.faces.validator.ValidatorException;


public class RLOS_Reject extends RLOSCommon implements FormListener
{

	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside RLOS_Reject RLOS");
		SKLogger.writeLog("RLOS RLOS_Reject", "Inside formLoaded()" + pEvent.getSource().getName());

	}
	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
			new RLOSCommonCode().CustomerFragment_Load();

			SKLogger.writeLog("RLOS Initiation", "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			SKLogger.writeLog("RLOS Initiation", "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));//added By akshay on 25/9/17 as per point 14
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Created_By",formObject.getUserName());
			formObject.setNGValue("Cust_Name",formObject.getNGValue("Customer_Name"));

			formObject.setNGValue("lbl_init_channel_val",formObject.getNGValue("initiationChannel"));

			formObject.setNGValue("lbl_curr_date_val",currDate);
			formObject.setNGValue("ApplicationRefNo", formObject.getWFFolderId());
			formObject.setNGValue("lbl_user_name_val",formObject.getUserName());
			formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
			formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Subproduct_productGrid"));
			formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));
			
			//added by Akshay on 25/9/17 as exception ws thrown when loading for first time/product grid empty
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n>0){
				formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
			}
			//ended by Akshay on 25/9/17 as exception ws thrown when loading for first time/product grid empty

			formObject.setNGValue("RM_Name",getRMName());
			formObject.setNGValue("lbl_TL_Name_val",formObject.getNGValue("RM_Name"));

			
			if(formObject.getNGValue("empType").contains("Salaried")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				formObject.setSheetVisible("ParentTab",1, false);
			}

			else if(formObject.getNGValue("empType").contains("Self Employed")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
				formObject.setSheetVisible("ParentTab",3, false);
			}


			if(formObject.getNGValue("Product_Type").contains("Personal Loan") && Integer.parseInt(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",1200);
			}
			if(formObject.getNGValue("PrimaryProduct").equals("Credit Card"))
				formObject.setSheetVisible("ParentTab",7, true);

		}catch(Exception e)
		{
			SKLogger.writeLog("RLOS RLOS_Reject", "Exception:"+e.getMessage());
		}
	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		try
		{
			switch (pEvent.getType()) 
			{
			case FRAME_EXPANDED:
						SKLogger.writeLog("RLOS FRAG LOADED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());					
						new RLOSCommonCode().FrameExpandEvent(pEvent);
						break;

			case FRAGMENT_LOADED:
				new RLOSCommonCode().DisableFragmentsOnLoad(pEvent);
				break;
				
			}

		}
		catch(Exception ex)
		{
			if(ex instanceof ValidatorException)
			{   
				if(popupFlag.equalsIgnoreCase("Y"))
				{

					if(popUpControl.equals(""))
					{
						throw new ValidatorException(new FacesMessage(popUpMsg));
					}else
					{
						throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

					}

				}
				else{
					HashMap<String,String> hm=new HashMap<String,String>();
					hm.put("Error","Checked");
					if(!popUpMsg.equals("")) {
						try{ throw new ValidatorException(new CustomExceptionHandler("Details Fetched", popUpMsg,"EventType", hm));}finally{hm.clear();}

					} else {
						try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));}finally{hm.clear();}

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

	public void saveFormCompleted(FormEvent pEvent) {
		SKLogger.writeLog("RLOS RLOS_Reject", "Inside saveFormCompleted()" + pEvent);
		FormContext.getCurrentInstance().getFormReference();


	}


	public void submitFormCompleted(FormEvent pEvent)
			throws ValidatorException {
		SKLogger.writeLog("RLOS RLOS_Reject", "Inside submitFormCompleted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		SKLogger.writeLog("RLOS Initiation", "Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		saveIndecisionGrid();
	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
	}


	public void initialize() {
		SKLogger.writeLog("RLOS RLOS_Reject", "Inside initialize()");
	}

	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

	}
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

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
