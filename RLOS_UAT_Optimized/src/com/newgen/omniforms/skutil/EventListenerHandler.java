//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: EventListenerHandler.java
//Author					: Akshay Gupta
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: to define common utility methods used throughout the file
//----------------------------------------------------------------------------------------------------

package com.newgen.omniforms.skutil;


import java.util.List;

import javax.faces.event.ActionEvent;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.RLOS;
import com.newgen.omniforms.user.RLOSCommon;
import com.newgen.omniforms.util.OFUtility;
import com.newgen.omniforms.util.Constant.EVENT;

/**
 *
 * @author mohit.sharma
 */
public class EventListenerHandler extends EventListenerImplementor {

	
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
        
    }

    @Override
    public void btnSearch_Clicked(ActionEvent ae) {
        RLOS.mLogger.info("Inside method btnSearch_Clicked");
    	FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        //PickList m_objPickList = formObject.getNGPickList(sProcessName+"SENDER_LOGsICAL_TERMINAL" , "BIC_CODE,RELATIONSHIP" , true , 10);
        String filter_value;

        if ("EMploymentDetails_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);        
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'%" 
                    + filter_value + "%' or EMPLOYER_CODE Like'%" + filter_value + "%'";
            List<List<String>> result=formObject.getDataFromDataSource(query);
           
            m_objPickList.populateData(result);
        }
        
        m_objPickList.setVisible(true);
    }

    @Override
    
    public void btnOk_Clicked(ActionEvent ae) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        RLOS.mLogger.info("Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
        if ("EMploymentDetails_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
            RLOS.mLogger.info("Inside OK button");
            try{
            	OFUtility.updateState("cmplx_EmploymentDetails_IncInPL", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(2)) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_IncInCC", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(7)) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
                formObject.setNGValue("cmplx_EmploymentDetails_IncInPL",m_objPickList.getSelectedValue().get(2) , false);
            
            	RLOS.mLogger.info("after loading picklist:"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));

              OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue"); 
              //formObject.setNGValue("cmplx_EmploymentDetails_Indus_Macro", m_objPickList.getSelectedValue().get(5));
             // formObject.setNGValue("cmplx_EmploymentDetails_Indus_Micro", m_objPickList.getSelectedValue().get(6));

              OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue"); 
            	RLOS.mLogger.info("before setting value in indus micro: "+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
                OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(6) , "setNGValue"); 
            	RLOS.mLogger.info("after setting value in indus micro"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Micro"));

                OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(8) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_CurrEmployer", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusCC", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusPL", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue");
                 for(int i =0;i<11;i++){
                	 RLOS.mLogger.info( "Val of controls received at "+i+" is: "+m_objPickList.getSelectedValue().get(i));
                 }       
              formObject.setLocked("cmplx_EmploymentDetails_EmpName", true);
              formObject.setLocked("cmplx_EmploymentDetails_EMpCode", true);
             
              formObject.setEnabled("cmplx_EmploymentDetails_IncInPL", false);
              formObject.setEnabled("cmplx_EmploymentDetails_IncInCC", false);
              formObject.setEnabled("cmplx_EmploymentDetails_Freezone", false);

              
              formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", true);
              formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", true);
              formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", true);
              formObject.setLocked("cmplx_EmploymentDetails_FreezoneName", true);
              formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer", true);
              formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", true);             
              formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
                      
            }
            catch(Exception e){
				RLOS.mLogger.info( "Exception occured in btnOk_Clicked: " + e.getMessage()+ RLOSCommon.printException(e));
           }
       }
 
} 
    
    
    @Override
    public void btnPrev_Clicked(ActionEvent ae) {
       
        RLOS.mLogger.info("###########" + ae.getComponent().getClientId());
    }
    
    public String CalcCheckboxValue(String input)
    {
    	 if("Y".equals(input) || "Freezone".equalsIgnoreCase(input))
         	return "true";
         else
        	return "false";
    }
}
