package com.newgen.omniforms.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.PickList;
//import com.newgen.omniforms.component.ListBox;


import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;


import com.newgen.omniforms.user.NGFUserResourceMgr_PL;
import com.newgen.omniforms.user.PLCommon;
import com.newgen.omniforms.user.PersonalLoanS;

import com.newgen.omniforms.util.Constant.EVENT;
import com.newgen.omniforms.util.OFUtility;

import javax.faces.event.ActionEvent;
//changes done to check duplicate selection compare emp code and main emp code and show alert added - 06-09-2017 Disha
import javax.faces.validator.ValidatorException;
/**
 *
 * @author mohit.sharma
 */
public class PL_EventListenerHandler extends EventListenerImplementor {
	private static final long serialVersionUID = 1L;
	Logger mLogger=PersonalLoanS.mLogger;

	public PL_EventListenerHandler(String picklistid) {
		super(picklistid);
	}

	public PL_EventListenerHandler(String picklistid, EVENT compId) {
		super(picklistid, compId);
	}

	@Override
	public void btnNext_Clicked(ActionEvent ae) {
		//PickList objPckList = FormContext.getCurrentInstance().getFormReference().getNGPickList(true);
		//System.out.println(" Fetched Records = " + objPckList.getM_iTotalRecordsFetched());
	}

	public void btnSearch_Clicked(ActionEvent ae) {
		mLogger.info("RLOS"+"Inside method btnSearch_Clicked");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
		String filter_value;

		if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(25);
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017	Disha
			String query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'%" 
				+ filter_value + "%' or EMPLOYER_CODE Like'%" + filter_value + "%'";
			//List<List<String>> result=formObject.getNGDataFromDataCache(query);
			mLogger.info("PL"+"##AKshay");
			for(int i=0;i<20;i++)
				mLogger.info("PL"+"Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
			m_objPickList.populateData(query);

		}

		else  if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
			mLogger.info("Inside method Button_State inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(25);        
			String query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";

			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		}
		else{
			mLogger.info("Inside method Button_City inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(25);        
			String query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";

			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		}

		//m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
		m_objPickList.setVisible(true);
	}


	@Override    
	public void btnOk_Clicked(ActionEvent ae) throws ValidatorException {
		PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		mLogger.info("EventListenerHandler"+ "Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
		try{
		if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
			mLogger.info("EventListenerHandler"+ "Inside OK button");
			
				if( m_objPickList.getSelectedValue().get(1).equalsIgnoreCase( m_objPickList.getSelectedValue().get(12)))
				{
					OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
					//OFUtility.updateState("cmplx_EmploymentDetails_NOB", "value", m_objPickList.getSelectedValue().get(2) , "setNGValue");
					//OFUtility.updateState("cmplx_EmploymentDetails_Status_PLExpact", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue");
					//OFUtility.updateState("cmplx_EmploymentDetails_Status_PLNational", "value", m_objPickList.getSelectedValue().get(3) , "setNGValue");
					//OFUtility.updateState("cmplx_EmploymentDetails_categcards", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue");
					formObject.setNGValue("cmplx_EmploymentDetails_IncInPL", CalcCheckboxValue( m_objPickList.getSelectedValue().get(2))); 
					//OFUtility.updateState("cmplx_EmploymentDetails_dateinPL", "value", common.Convert_dateFormat(m_objPickList.getSelectedValue().get(6),"MM/dd/yyyy","dd/MM/yyyy") , "setNGValue");				   
					formObject.setNGValue("cmplx_EmploymentDetails_IncInCC", CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)));
					//OFUtility.updateState("cmplx_EmploymentDetails_dateinCC", "value",common.Convert_dateFormat(m_objPickList.getSelectedValue().get(8),"MM/dd/yyyy","dd/MM/yyyy") , "setNGValue");
					//OFUtility.updateState("cmplx_EmploymentDetails_authsigname", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue"); 
					//OFUtility.updateState("cmplx_EmploymentDetails_accpvded", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(6) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(7)) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(8) , "setNGValue"); 
					//OFUtility.updateState("cmplx_EmploymentDetails_ownername", "value", m_objPickList.getSelectedValue().get(16) , "setNGValue"); 
					//OFUtility.updateState("cmplx_EmploymentDetails_remarks", "value", m_objPickList.getSelectedValue().get(17) , "setNGValue"); 
					//OFUtility.updateState("cmplx_EmploymentDetails_highdelinq", "value", m_objPickList.getSelectedValue().get(18) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_CurrEmployer", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusCC", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusPL", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Main_Emp_Code", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue");

				}
		}
		else if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("AddressDetails_state", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 


		} 
		else if ("Button_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("AddressDetails_city", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("ButtonLoan_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_LoanDetails_city", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("ButtonOECD_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"OECD_townBirth".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("OECD_townBirth", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else
		{     
			mLogger.info("EventListenerHandler"+ "Employer code does not match main employer code");
			String alert_msg=NGFUserResourceMgr_PL.getAlert("val048");
			mLogger.info(alert_msg);
			OFUtility.updateState("", "error",alert_msg,"eventDispatched");			
		}
	}
		catch(Exception e){
			mLogger.info("EventListenerHandler"+ "Exception occured in btnOk_Clicked: " + e.getMessage());
			PLCommon.printException(e);			
		}

	}  
	@Override
	public void btnPrev_Clicked(ActionEvent ae) {
		mLogger.info("Inside btnPrev_Clicked()");
	}

	public String CalcCheckboxValue(String input)
	{
		if(input.equals("Y") || input.equalsIgnoreCase("Freezone"))
			return "true";
		else
			return "false";
	}

	public String CalcDateValue(String input)
	{
		String Date_new="";
		try{
			mLogger.info("Date before Conversion:"+input);
			SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
			SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
			Date_new=sdf2.format(sdf1.parse(input));
			mLogger.info("converted date:"+Date_new);
		}
		catch(Exception e){
			mLogger.info("EventListenerHandler"+ "Exception occured in CalcDateValue: " + e.getMessage());
			PLCommon.printException(e);			
		}
		return Date_new;
	}
}
