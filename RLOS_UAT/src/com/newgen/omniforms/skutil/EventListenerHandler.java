package com.newgen.omniforms.skutil;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.CheckBox;
import com.newgen.omniforms.component.Form;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.TextBox;
//import com.newgen.omniforms.component.ListBox;
import com.newgen.omniforms.component.ComboBox;


import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.RLOSCommon;
import com.newgen.omniforms.util.Constant.EVENT;
import com.newgen.omniforms.util.OFUtility;

import javax.faces.event.ActionEvent;

/**
 *
 * @author mohit.sharma
 */
public class EventListenerHandler extends EventListenerImplementor {

	public EventListenerHandler(String picklistid) {
        super(picklistid);
    }

    public EventListenerHandler(String picklistid, EVENT compId) {
        super(picklistid, compId);
    }

    @Override
    public void btnNext_Clicked(ActionEvent ae) {
        //PickList objPckList = FormContext.getCurrentInstance().getFormReference().getNGPickList(true);
        //System.out.println(" Fetched Records = " + objPckList.getM_iTotalRecordsFetched());
    }

    @Override
    public void btnSearch_Clicked(ActionEvent ae) {
        SKLogger.writeLog("RLOS","Inside method btnSearch_Clicked");
    	FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        //PickList m_objPickList = formObject.getNGPickList(sProcessName+"SENDER_LOGsICAL_TERMINAL" , "BIC_CODE,RELATIONSHIP" , true , 10);
        String filter_value = "";

        if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);        
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'%" 
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
            SKLogger.writeLog("EventListenerHandler", "Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
            if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
                SKLogger.writeLog("EventListenerHandler", "Inside OK button");
                try{
            	OFUtility.updateState("cmplx_EmploymentDetails_IncInPL", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(2)) , "setNGValue"); 
                OFUtility.updateState("cmplx_EmploymentDetails_IncInCC", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)) , "setNGValue");
                OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(7)) , "setNGValue"); 
                    OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
                    OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
                    //OFUtility.updateState("cmplx_EmploymentDetails_IncInPL", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(2)) , "setNGValue"); 
                    //OFUtility.updateState("cmplx_EmploymentDetails_IncInCC", "value", CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)) , "setNGValue");
                    formObject.setNGValue("cmplx_EmploymentDetails_IncInPL",m_objPickList.getSelectedValue().get(2) , false);
               /* if(!m_objPickList.getSelectedValue().get(4).equalsIgnoreCase("")){
                    //formObject.setNGValue("cmplx_EmploymentDetails_IncInCC", m_objPickList.getSelectedValue().get(3), false);
                    if(m_objPickList.getSelectedValue().get(3).equalsIgnoreCase("Y")){
                    	SKLogger.writeLog("EventListnerHandle ","btnOk_Clicked() inside for cmplx_EmploymentDetails_IncInCC");
                    	formObject.setNGValue("cmplx_EmploymentDetails_IncInCC", true);
                    }
              */ 
                    // uncommented by aman on 28th Oct as Macro masters not loading
                    //uncommented by saurabh on 18th Oct as Macro masters not loading.
                    //new custom query to keep --select-- at top even though only description taken out.
                   
                    new Common().LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select distinct(macro) from NG_MASTER_EmpIndusMacroAndMicro where IndustrySector='"+m_objPickList.getSelectedValue().get(4)+"'");
                    SKLogger.writeLog("EventListnerHandle ","select distinct(macro) from NG_MASTER_EmpIndusMacroAndMicro where IndustrySector='"+m_objPickList.getSelectedValue().get(4)+"'");
                    
                    new Common().LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select micro from NG_MASTER_EmpIndusMacroAndMicro where IndustrySector='"+m_objPickList.getSelectedValue().get(4)+"'");
                    SKLogger.writeLog("EventListnerHandle ","select micro from NG_MASTER_EmpIndusMacroAndMicro where IndustrySector='"+m_objPickList.getSelectedValue().get(4)+"'");
                     
                    
                  /*  if(!m_objPickList.getSelectedValue().get(5).equalsIgnoreCase("")){
                	new Common().LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select distinct(micro) from NG_MASTER_EmpIndusMacroAndMicro where IndustrySector='"+m_objPickList.getSelectedValue().get(4)+"' and macro='"+m_objPickList.getSelectedValue().get(5)+"'");
                }*/
            	SKLogger.writeLog("EventListnerHandle ","after loading picklist:"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
                    OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue"); 
              formObject.setNGValue("cmplx_EmploymentDetails_Indus_Macro", m_objPickList.getSelectedValue().get(5));
              formObject.setNGValue("cmplx_EmploymentDetails_Indus_Micro", m_objPickList.getSelectedValue().get(6));
              //OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue"); 
            	SKLogger.writeLog("EventListnerHandle ","before setting value in indus micro: "+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
               // OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(6) , "setNGValue"); 
            	SKLogger.writeLog("EventListnerHandle ","after setting value in indus micro"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Micro"));
                    OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(8) , "setNGValue"); 
                    OFUtility.updateState("cmplx_EmploymentDetails_CurrEmployer", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue");
                    OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusCC", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue");
                    OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusPL", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue");
                
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
                //  formObject.setLocked("cmplx_EmploymentDetails_EmpName", true);
                }
                catch(Exception e){
                SKLogger.writeLog("EventListenerHandler", "Exception occured in btnOk_Clicked: " + e.getMessage()+ new RLOSCommon().printException(e));
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
}
