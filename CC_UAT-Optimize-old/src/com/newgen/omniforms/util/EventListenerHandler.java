package com.newgen.omniforms.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.CreditCard;
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
            //List<List<String>> result=formObject.getDataFromDataSource(query);
            m_objPickList.populateData(query);
            CreditCard.mLogger.info("##AKshay");
            for(int i=0;i<20;i++)
                CreditCard.mLogger.info("Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
        }*/
        
        if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
        	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25); 
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017	Disha		
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'%" 
                    + filter_value + "%' or EMPLOYER_CODE Like'%" + filter_value + "%'";
            List<List<String>> result=formObject.getDataFromDataSource(query);
           
            m_objPickList.populateData(result);
        }
        
        if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("CustDetailVerification1_Button2")) {
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);
            String query="select distinct(Emirates_ID),Customer_Name,Cif_ID,FCU_Remarks,FCU_Analyst_Name,FCU_Decision from NG_RLOS_FCU_Decision where Emirates_ID like'%" 
            + filter_value + "%' or Cif_ID Like'%" + filter_value + "%'";
            //List<List<String>> result=formObject.getDataFromDataSource(query);
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
        CreditCard.mLogger.info( "Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
        /*if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
            CreditCard.mLogger.info( "Inside OK button");
            try{
            	
                CreditCard.mLogger.info(m_objPickList.getSelectedValue().get(7));
                SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
                SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
                String DateofInclusioninPLAloc_new=sdf2.format(sdf1.parse(m_objPickList.getSelectedValue().get(7)));
                CreditCard.mLogger.info(DateofInclusioninPLAloc_new);
                
                
                OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_NOB", "value", m_objPickList.getSelectedValue().get(2) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_categexpat", "value", m_objPickList.getSelectedValue().get(3) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_categnational", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_categcards", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_IncInPL", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(6)) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_dateinPL", "value", DateofInclusioninPLAloc_new , "setNGValue");                   
                OFUtility.updateState("cmplx_EmploymentDetails_IncInCC", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(8)) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_dateinCC", "value",  m_objPickList.getSelectedValue().get(9) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_authsigname", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_accpvded", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(14) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(15)) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(16) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_ownername", "value", m_objPickList.getSelectedValue().get(17) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_remarks", "value", m_objPickList.getSelectedValue().get(18) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_highdelinq", "value", m_objPickList.getSelectedValue().get(19) , "setNGValue"); 
                                                
            }
            catch(Exception e){
                CreditCard.mLogger.info( "Exception occured in btnOk_Clicked: " + e.getMessage());
           }
       }*/
        
        if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
            CreditCard.mLogger.info( "Inside OK button");
            try{
			
			     // changes done to check duplicate selection compare emp code and main emp code if/else condition added - 06-09-2017 Disha
                	 CreditCard.mLogger.info( "value of Emp Code" +  m_objPickList.getSelectedValue().get(1));
                	 CreditCard.mLogger.info( "value of main Emp Code" +  m_objPickList.getSelectedValue().get(9));
                if( m_objPickList.getSelectedValue().get(1).equalsIgnoreCase( m_objPickList.getSelectedValue().get(9)))
                {
					OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_IncInPL", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(2)) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_IncInCC", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(6) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(7)) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(8) , "setNGValue");
					OFUtility.updateState("cmplx_EmploymentDetails_Main_Emp_Code", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue");	
                }
				else
				{     
					CreditCard.mLogger.info( "Employer code does not match main employer code");
					throw new ValidatorException(new FacesMessage("Duplicate employer code is selected and the original record to be selected"));
					
				}
            }
            catch(Exception e){
                CreditCard.mLogger.info( "Exception occured in btnOk_Clicked: " + e.getMessage());
           }
       }
        
        if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("CustDetailVerification1_Button2")) {
            CreditCard.mLogger.info( "Inside OK button");
            try{

                CreditCard.mLogger.info(m_objPickList.getSelectedValue().get(7));
                List<String> mylist = new ArrayList<String>();
                mylist.add("cmplx_CustDetailverification1_EmiratesId");
                
                
                                                
            }
            catch(Exception e){
                CreditCard.mLogger.info( "Exception occured in btnOk_Clicked: " + e.getMessage());
           }
       }
    	}
    	catch(Exception ex){
    		CreditCard.mLogger.info( "Exception occured in btnOk_Clicked: " + ex.getMessage());
    	}
}  
    
    public String CalcCheckboxValue(String input)
    {
         if(input.equals("Y") || input.equalsIgnoreCase("Freezone"))
             return "true";
         else
            return "false";
    }

    @Override
    public void btnPrev_Clicked(ActionEvent ae) {
        CreditCard.mLogger.info("###########" + ae.getComponent().getClientId());
    }
}
