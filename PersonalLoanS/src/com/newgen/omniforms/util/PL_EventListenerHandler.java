package com.newgen.omniforms.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.SimpleDateFormat;
import java.util.List;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.CheckBox;
import com.newgen.omniforms.component.DatePicker;
import com.newgen.omniforms.component.Form;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.TextBox;
//import com.newgen.omniforms.component.ListBox;
import com.newgen.omniforms.component.ComboBox;


import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Constant.EVENT;
import com.newgen.omniforms.util.OFUtility;

import javax.faces.event.ActionEvent;

/**
 *
 * @author mohit.sharma
 */
public class PL_EventListenerHandler extends EventListenerImplementor {

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
        PL_SKLogger.writeLog("RLOS","Inside method btnSearch_Clicked");
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        String filter_value = "";

        if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'%" 
            + filter_value + "%' or EMPLOYER_CODE Like'%" + filter_value + "%'";
            //List<List<String>> result=formObject.getDataFromDataSource(query);
            PL_SKLogger.writeLog("PL","##AKshay");
            for(int i=0;i<20;i++)
            	PL_SKLogger.writeLog("PL","Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
            m_objPickList.populateData(query);
          
        }
        //m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        m_objPickList.setVisible(true);
    }

  
    @Override    
    public void btnOk_Clicked(ActionEvent ae) {
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        PL_SKLogger.writeLog("EventListenerHandler", "Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
        if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
            PL_SKLogger.writeLog("EventListenerHandler", "Inside OK button");
            try{
             
                OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
				OFUtility.updateState("cmplx_EmploymentDetails_NOB", "value", m_objPickList.getSelectedValue().get(2) , "setNGValue");
				OFUtility.updateState("cmplx_EmploymentDetails_categexpat", "value", m_objPickList.getSelectedValue().get(3) , "setNGValue");
				OFUtility.updateState("cmplx_EmploymentDetails_categnational", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue");
				OFUtility.updateState("cmplx_EmploymentDetails_categcards", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_IncInPL", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(6)) , "setNGValue");
				OFUtility.updateState("cmplx_EmploymentDetails_dateinPL", "value", CalcDateValue(m_objPickList.getSelectedValue().get(7)) , "setNGValue");				   
                OFUtility.updateState("cmplx_EmploymentDetails_IncInCC", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(8)) , "setNGValue"); 
				OFUtility.updateState("cmplx_EmploymentDetails_dateinCC", "value", CalcDateValue(m_objPickList.getSelectedValue().get(9)) , "setNGValue");
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
                PL_SKLogger.writeLog("EventListenerHandler", "Exception occured in btnOk_Clicked: " + e.getMessage());
           }
       }
 
}  
    @Override
    public void btnPrev_Clicked(ActionEvent ae) {
        System.out.println("###########" + ae.getComponent().getClientId());
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
    	PL_SKLogger.writeLog("Date before Conversion:",input);
		SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
		 Date_new=sdf2.format(sdf1.parse(input));
		PL_SKLogger.writeLog("converted date:",Date_new);
    	}
    	  catch(Exception e){
              PL_SKLogger.writeLog("EventListenerHandler", "Exception occured in CalcDateValue: " + e.getMessage());
         }
   return Date_new;
    }
}
