package com.newgen.omniforms.util;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.CC_Common;
import com.newgen.omniforms.user.CreditCard;
import com.newgen.omniforms.user.NGFUserResourceMgr_CreditCard;

import com.newgen.omniforms.util.Constant.EVENT;

/**
 *
 * @author mohit.sharma
 */
public class EventListenerHandler extends EventListenerImplementor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public EventListenerHandler(String picklistid) {
        super(picklistid);
    }

    public EventListenerHandler(String picklistid, EVENT compId) {
        super(picklistid, compId);
    }

    @Override
    public void btnNext_Clicked(ActionEvent ae) {
        //PickList objPckList = FormContext.getCurrentInstance().getFormReference().getNGPickList(true);
        //CreditCard.mLogger.info(" Fetched Records = " + objPckList.getM_iTotalRecordsFetched());
    }

    @Override
    public void btnSearch_Clicked(ActionEvent ae) {
        CreditCard.mLogger.info("Inside method btnSearch_Clicked");
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        //PickList m_objPickList = formObject.getNGPickList(sProcessName+"SENDER_LOGsICAL_TERMINAL" , "BIC_CODE,RELATIONSHIP" , true , 10);
        String filter_value = "";
  
        /*if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'%" 
            + filter_value + "%' or EMPLOYER_CODE Like'%" + filter_value + "%'";
            //List<List<String>> result=formObject.getNGDataFromDataCache(query);
            m_objPickList.populateData(query);
            CreditCard.mLogger.info("##AKshay");
            for(int i=0;i<20;i++)
                CreditCard.mLogger.info("Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
        }*/
        
        if ("EMploymentDetails_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25); 
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017	Disha		
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPR_NAME like'%" 
                    + filter_value + "%' or EMPLOYER_CODE Like'%" + filter_value + "%'";
            List<List<String>> result=formObject.getNGDataFromDataCache(query);
           
            m_objPickList.populateData(result);
        }
        
        if ("CustDetailVerification1_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);
            String query="select distinct(Emirates_ID),Customer_Name,Cif_ID,FCU_Remarks,FCU_Analyst_Name,FCU_Decision from NG_RLOS_FCU_Decision with (nolock) where Emirates_ID like'%" 
            + filter_value + "%' or Cif_ID Like'%" + filter_value + "%'";
            //List<List<String>> result=formObject.getNGDataFromDataCache(query);
            m_objPickList.populateData(query);
            CreditCard.mLogger.info("##Arun");
            for(int i=0;i<20;i++)
                CreditCard.mLogger.info("Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
        }
  
        //m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        m_objPickList.setVisible(true);
    }
	
 
    @Override    
    public void btnOk_Clicked(ActionEvent ae) throws ValidatorException {
    	try{
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
       Common_Utils common=new Common_Utils(CreditCard.mLogger);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
       CreditCard.mLogger.info( "Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
        if( m_objPickList.getSelectedValue().get(1).equalsIgnoreCase( m_objPickList.getSelectedValue().get(22)))
		{
	        OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_NOB", "value", m_objPickList.getSelectedValue().get(2) , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_Status_PLExpact", "value", m_objPickList.getSelectedValue().get(3) , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_Status_PLNational", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue");
			//OFUtility.updateState("cmplx_EmploymentDetails_categcards", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue");
			formObject.setNGValue("cmplx_EmploymentDetails_IncInPL", CalcCheckboxValue( m_objPickList.getSelectedValue().get(5))); 
           	OFUtility.updateState("cmplx_EmploymentDetails_dateinPL", "value", common.Convert_dateFormat(m_objPickList.getSelectedValue().get(6),"MM/dd/yyyy","dd/MM/yyyy") , "setNGValue");				   
           	formObject.setNGValue("cmplx_EmploymentDetails_IncInCC", CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)));
           	OFUtility.updateState("cmplx_EmploymentDetails_dateinCC", "value",common.Convert_dateFormat(m_objPickList.getSelectedValue().get(8),"MM/dd/yyyy","dd/MM/yyyy") , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_authsigname", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_accpvded", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(14)) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(15) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_ownername", "value", m_objPickList.getSelectedValue().get(16) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_remarks", "value", m_objPickList.getSelectedValue().get(17) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_highdelinq", "value", m_objPickList.getSelectedValue().get(18) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_CurrEmployer", "value", m_objPickList.getSelectedValue().get(19) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusCC", "value", m_objPickList.getSelectedValue().get(20) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusPL", "value", m_objPickList.getSelectedValue().get(21) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_Main_Emp_Code", "value", m_objPickList.getSelectedValue().get(22) , "setNGValue");

		}
		else
		{     
			CreditCard.mLogger.info("EventListenerHandler"+ "Employer code does not match main employer code");
        	String alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL073");
        	CreditCard.mLogger.info(alert_msg);
        	OFUtility.updateState("", "error",alert_msg,"eventDispatched");			
		}
    	
        
        if ("CustDetailVerification1_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
            CreditCard.mLogger.info( "Inside OK button");
            
                CreditCard.mLogger.info(m_objPickList.getSelectedValue().get(7));
                List<String> mylist = new ArrayList<String>();
                mylist.add("cmplx_CustDetailverification1_EmiratesId");
          
       }
    	}
    	catch(Exception ex){
    		CreditCard.mLogger.info( "Exception occured in btnOk_Clicked: " + ex.getMessage());
    		CC_Common.printException(ex);
    	}
}  
    
    public String CalcCheckboxValue(String input)
    {
         if("Y".equals(input) || "Freezone".equalsIgnoreCase(input))
             return "true";
         else
            return "false";
    }

    @Override
    public void btnPrev_Clicked(ActionEvent ae) {
        CreditCard.mLogger.info("###########" + ae.getComponent().getClientId());
    }
}
