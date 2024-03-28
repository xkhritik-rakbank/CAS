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
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        
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
        CreditCard.mLogger.info("@nikhil"+m_objPickList.getAssociatedTxtCtrl());
        
        if ("EMploymentDetails_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	  filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25); 
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017	Disha		
            //String query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPR_NAME like '"
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS_CC,ALOC_REMARKS_PL,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME Like '"
                    + filter_value + "%' or EMPLOYER_CODE Like '" + filter_value + "%'";
            List<List<String>> result=formObject.getDataFromDataSource(query);
           
            m_objPickList.populateData(result);
        }
        
        else  if ("CompanyDetails_Aloc_search".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {
        	filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25); 
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017	Disha		
            String query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPR_NAME like'%" 
                    + filter_value + "%' or EMPLOYER_CODE Like'%" + filter_value + "%'";
            List<List<String>> result=formObject.getDataFromDataSource(query);
           
            m_objPickList.populateData(result);
        }
        
        
        else  if ("CustDetailVerification1_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
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
        
        else  if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
        	CreditCard.mLogger.info("Inside method Button_State inside state"+m_objPickList.getAssociatedTxtCtrl());
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);        
            String query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
               
            List<List<String>> result=formObject.getNGDataFromDataCache(query);
            CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
            m_objPickList.populateData(result);
        
        }
        else if("Button_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	CreditCard.mLogger.info("Inside method Button_City inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
              
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
       
       }
        else if("Designation_button1".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	CreditCard.mLogger.info("Inside method Designation_button1 inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select description,code from NG_MASTER_Designation with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           CreditCard.mLogger.info("sagarika vishnoi");   
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
         
       }
        else if("cmplx_Customer_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	CreditCard.mLogger.info("Inside method Designation_button1 inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select code,description from NG_MASTER_Designation with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           CreditCard.mLogger.info("sagarika vishnoi");   
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
         
       }
        else if("Designation_button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	CreditCard.mLogger.info("Inside method Designation_button2 inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select description,code from NG_MASTER_Designation with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           CreditCard.mLogger.info("sagarika vishnoi d2");   
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
       
       }
       //Designation_button
        //added by nikhil for check for PCSP-534
        else  if ("cmplx_EmploymentDetails_OtherBankCAC".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {	
        CreditCard.mLogger.info("Inside method Button_City inside state"+m_objPickList.getAssociatedTxtCtrl());
        filter_value = m_objPickList.getSearchFilterValue();
        m_objPickList.setBatchRequired(true);
        m_objPickList.setBatchSize(25);
        //query changed by nikhil for PCSP-635
        String query="select code,description from NG_MASTER_othBankCAC with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           
        List<List<String>> result=formObject.getNGDataFromDataCache(query);
        CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
        m_objPickList.populateData(result);
    
    }
        else  if ("AlternateContactDetails_CardDisp".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {	
            CreditCard.mLogger.info("Inside method Card Dispatch inside state"+m_objPickList.getAssociatedTxtCtrl());
            filter_value = m_objPickList.getSearchFilterValue();
            m_objPickList.setBatchRequired(true);
            m_objPickList.setBatchSize(25);
            //query changed by nikhil for PCSP-635
            String query="select code,description from NG_MASTER_Dispatch with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
               
            List<List<String>> result=formObject.getNGDataFromDataCache(query);
            CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
            m_objPickList.populateData(result);
        
        }
        else{
			filter_value = m_objPickList.getSearchFilterValue();
			String Cntrl_name = m_objPickList.getAssociatedTxtCtrl();
			CreditCard.mLogger.info("PL"+"##AKshay: "+Cntrl_name+" aman: "+filter_value);
			
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(25);
			String val=NGFUserResourceMgr_CreditCard.getMasterManagerSecond(Cntrl_name);
			CreditCard.mLogger.info("PL"+"##AKshay: "+Cntrl_name+" aman: "+val);
			String[] value= val.split(":");
			String sColumnName=value[1];
			String[] sCol=sColumnName.split(",");
			CreditCard.mLogger.info("PL"+"##AKshay: "+filter_value);
			String query="select "+value[1]+" from "+value[0]+" with (nolock)  where isActive='Y' and "+sCol[0]+" like '"  + filter_value + "%' or "+sCol[1]+" Like '" + filter_value + "%'";
			CreditCard.mLogger.info("PL"+"##AKshay"+query);
			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			CreditCard.mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);
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
       if ("EMploymentDetails_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        if( m_objPickList.getSelectedValue().get(1).equalsIgnoreCase( m_objPickList.getSelectedValue().get(23)))
		{
	        OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_NOB", "value", m_objPickList.getSelectedValue().get(2) , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_Status_PLExpact", "value", m_objPickList.getSelectedValue().get(3) , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_Status_PLNational", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue");
			//OFUtility.updateState("cmplx_EmploymentDetails_categcards", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue");
			formObject.setNGValue("cmplx_EmploymentDetails_IncInPL", CalcCheckboxValue( m_objPickList.getSelectedValue().get(5))); 
           	OFUtility.updateState("cmplx_EmploymentDetails_dateinPL", "value", common.Convert_dateFormat(m_objPickList.getSelectedValue().get(6),"dd-MMM-yy","dd/MM/yyyy") , "setNGValue");				   
           	formObject.setNGValue("cmplx_EmploymentDetails_IncInCC", CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)));
           	OFUtility.updateState("cmplx_EmploymentDetails_dateinCC", "value",common.Convert_dateFormat(m_objPickList.getSelectedValue().get(8),"dd-MMM-yy","dd/MM/yyyy") , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_authsigname", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_accpvded", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue");
			//Added by shivang for PCASP-1533
			CreditCard.mLogger.info( "Sub product ::"+formObject.getNGValue("Sub_Product"));
			CreditCard.mLogger.info( "Val of PL is:"+m_objPickList.getSelectedValue().get(5));
			if(formObject.getNGValue("Sub_Product").equalsIgnoreCase("IM")){
				CreditCard.mLogger.info( "Val of PL is:"+m_objPickList.getSelectedValue().get(5));
				CreditCard.mLogger.info( "Val of CC is:"+m_objPickList.getSelectedValue().get(7));
				if((CalcCheckboxValue( m_objPickList.getSelectedValue().get(5)).equalsIgnoreCase("false") &&CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)).equalsIgnoreCase("false"))
					||(CalcCheckboxValue( m_objPickList.getSelectedValue().get(5)).equalsIgnoreCase("N") &&CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)).equalsIgnoreCase("N"))){
				
					CreditCard.mLogger.info( "INside CNOAL condition on IM ::");
					formObject.setVisible("EMploymentDetails_Label41", true);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
				}else{
					formObject.setVisible("EMploymentDetails_Label41", false);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", false);
				}
			}
			CreditCard.mLogger.info( "before Micro");
			//Condition Modified for PCASP-1673
			if((!("false".equalsIgnoreCase( CalcCheckboxValue( m_objPickList.getSelectedValue().get(5))) && "false".equalsIgnoreCase(CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)))))
					&& (!("NM".equalsIgnoreCase(m_objPickList.getSelectedValue().get(21))|| "NM".equalsIgnoreCase(m_objPickList.getSelectedValue().get(22)))))
			{
				CreditCard.mLogger.info( "Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
	        OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue"); 
	        //OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
	       // OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue");
	       String override=isNMFCNOAL(m_objPickList.getSelectedValue().get(7), m_objPickList.getSelectedValue().get(5), m_objPickList.getSelectedValue().get(20),
	    		   m_objPickList.getSelectedValue().get(22), m_objPickList.getSelectedValue().get(21));
	       /*if(override.equalsIgnoreCase("false")){
	        if("".equalsIgnoreCase(m_objPickList.getSelectedValue().get(12)))
            {
                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", "--Select--" , "setNGValue"); 
            }
            else
            {
                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
            }      
            CreditCard.mLogger.info("before setting value in indus micro: "+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
            if("".equalsIgnoreCase(m_objPickList.getSelectedValue().get(13)))
            {
                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", "--Select--" , "setNGValue"); 
            }
            else
            {
                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue"); 
            }
	       }*/
			}
			 CreditCard.mLogger.info("deepak before setting value in indus macro"+m_objPickList.getSelectedValue().get(12));
			 CreditCard.mLogger.info("deepak before setting value in indus micro"+m_objPickList.getSelectedValue().get(13));
		        if(!"".equalsIgnoreCase(m_objPickList.getSelectedValue().get(12)))
	            {
	               OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
	            }      
	            CreditCard.mLogger.info("before setting value in indus micro: "+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
	            if(!"".equalsIgnoreCase(m_objPickList.getSelectedValue().get(13)))
	            {
	               OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue"); 
	            }
	            CreditCard.mLogger.info("deepak after setting value in indus macro"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Micro"));
				 CreditCard.mLogger.info("deepak after setting value in indus micro"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Micro"));
            CreditCard.mLogger.info("after setting value in indus micro"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Micro"));
			
			//Added by nikhil for production case //Commented by Deepak post implementation of Micro & Macro change. 
//			if(("".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL")) || "CN".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL")) || "NM".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL"))) && ("".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC")) || "CN".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC")) || "NM".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC"))))
//			{	
//				OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue");
//				OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue");
//			}
			// CreditCard.mLogger.info("sagarika CCStatus"+ m_objPickList.getSelectedValue().get(21));
			// CreditCard.mLogger.info("sagarika CCStatus"+ m_objPickList.getSelectedValue().get(21));
			// CreditCard.mLogger.info("sagarika");
			/*else
			{
				if(!("NM".equalsIgnoreCase(m_objPickList.getSelectedValue().get(21))|| "CN".equalsIgnoreCase(m_objPickList.getSelectedValue().get(21))))
				{
					 OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
					 OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue"); 
				}
			}*/
			CreditCard.mLogger.info( "After micro code");
	        OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(14)) , "setNGValue"); 
	        OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(15) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_ownername", "value", m_objPickList.getSelectedValue().get(16) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_remarks", "value", m_objPickList.getSelectedValue().get(17) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_Aloc_RemarksPL", "value", m_objPickList.getSelectedValue().get(18) , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_highdelinq", "value", m_objPickList.getSelectedValue().get(19) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_CurrEmployer", "value", m_objPickList.getSelectedValue().get(20) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusCC", "value", m_objPickList.getSelectedValue().get(21) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusPL", "value", m_objPickList.getSelectedValue().get(22) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_Main_Emp_Code", "value", m_objPickList.getSelectedValue().get(23) , "setNGValue");
			OFUtility.updateState("cmplx_EmploymentDetails_payroll_flag", "value", m_objPickList.getSelectedValue().get(24), "setNGValue");
			
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2).equalsIgnoreCase("IM")){
				CreditCard.mLogger.info("Inside IM check "+m_objPickList.getSelectedValue().get(25));
			if(m_objPickList.getSelectedValue().get(25).equalsIgnoreCase("P")){
				OFUtility.updateState("cmplx_EmploymentDetails_EmployerType", "value", "Private" , "setNGValue");
				formObject.setNGValue("cmplx_EmploymentDetails_EmployerType", "Private");
			}
			else if(m_objPickList.getSelectedValue().get(25).equalsIgnoreCase("G")){
				OFUtility.updateState("cmplx_EmploymentDetails_EmployerType", "value", "Government" , "setNGValue");
				formObject.setNGValue("cmplx_EmploymentDetails_EmployerType", "Government");
			}
			else{
				OFUtility.updateState("cmplx_EmploymentDetails_EmployerType", "value", "" , "setNGValue");
			}
				formObject.setLocked("cmplx_EmploymentDetails_EmployerType", true);
			}
			
			//Added by shivang for PCASP-1523
			CreditCard.mLogger.info( "Employer Category PL ::"+m_objPickList.getSelectedValue().get(20));
			CreditCard.mLogger.info( "Employer Status - PL ::"+m_objPickList.getSelectedValue().get(22));
			CreditCard.mLogger.info( "Included in CC Aloc ::"+m_objPickList.getSelectedValue().get(7));
			if(("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))||formObject.getNGValue("Sub_Product").equalsIgnoreCase("SAL")) && (!formObject.getNGValue("Sub_Product").equalsIgnoreCase("IM"))){
				if((CalcCheckboxValue( m_objPickList.getSelectedValue().get(5)).equalsIgnoreCase("false") && CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)).equalsIgnoreCase("false"))
					|| (CalcCheckboxValue( m_objPickList.getSelectedValue().get(5)).equalsIgnoreCase("N") && CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)).equalsIgnoreCase("N"))
					|| (m_objPickList.getSelectedValue().get(20).equalsIgnoreCase("CN")
						&& (m_objPickList.getSelectedValue().get(22).equalsIgnoreCase("OP")||m_objPickList.getSelectedValue().get(22).equalsIgnoreCase("RS"))
						&& (m_objPickList.getSelectedValue().get(21).equalsIgnoreCase("CN")||m_objPickList.getSelectedValue().get(21).equalsIgnoreCase("")))
					|| (formObject.getNGValue("Sub_Product").equalsIgnoreCase("SAL") && m_objPickList.getSelectedValue().get(20).equalsIgnoreCase("CN")&&
						((m_objPickList.getSelectedValue().get(21).equalsIgnoreCase("NM") || m_objPickList.getSelectedValue().get(21).equalsIgnoreCase("CN")||m_objPickList.getSelectedValue().get(21).equalsIgnoreCase(""))
						 && (m_objPickList.getSelectedValue().get(22).equalsIgnoreCase("NM") || m_objPickList.getSelectedValue().get(22).equalsIgnoreCase("CN")||m_objPickList.getSelectedValue().get(22).equalsIgnoreCase("")) )))
				{
					CreditCard.mLogger.info( " Inside if condition of CNOAL ::");
					formObject.setVisible("EMploymentDetails_Label41", true);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setEnabled("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", false);
				}else{
					formObject.setVisible("EMploymentDetails_Label41", true);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setNGValue("cmplx_EmploymentDetails_LengthOfBusiness", "");
					formObject.setEnabled("cmplx_EmploymentDetails_LengthOfBusiness", false);
					formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
				}
			}
			// Production changes for CES flag
			formObject.setNGValue("cmplx_EmploymentDetails_CESflag",getCESflag(m_objPickList.getSelectedValue().get(1),m_objPickList.getSelectedValue().get(0),formObject));
			formObject.setLocked("cmplx_EmploymentDetails_CESflag",true);
		}
        //added by nikhil for Duplicate alert 16/12/2018
        else
        {
        	OFUtility.updateState("", "error", "Duplicate employer code is selected and the original record to be selected","eventDispatched");
        }
		
       }
       
       if("CompanyDetails_Aloc_search".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
       {
    	   CreditCard.mLogger.info( "INside Company Aloc search ");
           if( m_objPickList.getSelectedValue().get(1).equalsIgnoreCase( m_objPickList.getSelectedValue().get(22)))
   		{
   	        OFUtility.updateState("compName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
   	        OFUtility.updateState("CompanyDetails_comp_code", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
   			//OFUtility.updateState("cmplx_EmploymentDetails_NOB", "value", m_objPickList.getSelectedValue().get(2) , "setNGValue");
   			OFUtility.updateState("EmployerStatusPLExpact", "value", m_objPickList.getSelectedValue().get(3) , "setNGValue");
   			OFUtility.updateState("EmployerStatusPLNational", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue");
   			//OFUtility.updateState("cmplx_EmploymentDetails_categcards", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue");
   			//formObject.setNGValue("cmplx_EmploymentDetails_IncInPL", CalcCheckboxValue( m_objPickList.getSelectedValue().get(5))); 
             // 	OFUtility.updateState("cmplx_EmploymentDetails_dateinPL", "value", common.Convert_dateFormat(m_objPickList.getSelectedValue().get(6),"MM/dd/yyyy","dd/MM/yyyy") , "setNGValue");				   
             // 	formObject.setNGValue("cmplx_EmploymentDetails_IncInCC", CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)));
             // 	OFUtility.updateState("cmplx_EmploymentDetails_dateinCC", "value",common.Convert_dateFormat(m_objPickList.getSelectedValue().get(8),"MM/dd/yyyy","dd/MM/yyyy") , "setNGValue");
   			//OFUtility.updateState("cmplx_EmploymentDetails_authsigname", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue"); 
   			//OFUtility.updateState("cmplx_EmploymentDetails_accpvded", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue"); 
   			CreditCard.mLogger.info( "Industry Macro : Micro "+m_objPickList.getSelectedValue().get(12)+" : "+m_objPickList.getSelectedValue().get(13));
   			
   			String override=isNMFCNOAL(m_objPickList.getSelectedValue().get(7), m_objPickList.getSelectedValue().get(5), m_objPickList.getSelectedValue().get(19),
 	    		   m_objPickList.getSelectedValue().get(21), m_objPickList.getSelectedValue().get(20));
   			if(override.equalsIgnoreCase("false")){
   				
   				OFUtility.updateState("indusSector", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue");
   	        //OFUtility.updateState("indusMAcro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
   	        //OFUtility.updateState("indusMicro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue"); 
   	        formObject.setNGValue("indusMAcro", m_objPickList.getSelectedValue().get(12));//PCASP-2944
   	        formObject.setNGValue("indusMicro", m_objPickList.getSelectedValue().get(13));
   			} 
   	       // OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(14)) , "setNGValue"); 
   	       // OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(15) , "setNGValue"); 
   			//OFUtility.updateState("cmplx_EmploymentDetails_ownername", "value", m_objPickList.getSelectedValue().get(16) , "setNGValue"); 
   			//OFUtility.updateState("cmplx_EmploymentDetails_remarks", "value", m_objPickList.getSelectedValue().get(17) , "setNGValue"); 
   			OFUtility.updateState("HighDelinquencyEmployer", "value", m_objPickList.getSelectedValue().get(18) , "setNGValue"); 
   			OFUtility.updateState("EmployerCategoryPL", "value", m_objPickList.getSelectedValue().get(19) , "setNGValue"); 
   			OFUtility.updateState("EmployerStatusCC", "value", m_objPickList.getSelectedValue().get(20) , "setNGValue"); 
   			//pcasp-2848
   			OFUtility.updateState("EmployerStatusPLExpact", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue"); 
   			OFUtility.updateState("EmployerStatusPLNational", "value", m_objPickList.getSelectedValue().get(3) , "setNGValue"); 
   			OFUtility.updateState("EmployerCategoryPL", "value", m_objPickList.getSelectedValue().get(18) , "setNGValue"); 
   			OFUtility.updateState("EmployerStatusCC", "value", m_objPickList.getSelectedValue().get(19) , "setNGValue"); 
   			CreditCard.mLogger.info("check cnoal"+formObject.getNGValue("EmployerStatusCC"));
   			//OFUtility.updateState("cmplx_EmploymentDetails_Main_Emp_Code", "value", m_objPickList.getSelectedValue().get(22) , "setNGValue");
   		//formObject.setLocked("compName",true);
   			if(m_objPickList.getSelectedValue().get(19).equalsIgnoreCase("CN")||m_objPickList.getSelectedValue().get(19).equalsIgnoreCase("NM")||formObject.getNGValue("EmployerStatusCC").equalsIgnoreCase("NM")||formObject.getNGValue("EmployerStatusCC").equalsIgnoreCase("NM"))
   			{
   				formObject.setLocked("EmployerStatusPLExpact",false);
   				formObject.setLocked("EmployerStatusPLNational",false);
   				formObject.setLocked("indusSector",false);
   				formObject.setLocked("indusMicro",false);
   				formObject.setLocked("indusMAcro",false);
   				formObject.setLocked("HighDelinquencyEmployer",false);
   				formObject.setLocked("EmployerCategoryPL",false);
   				formObject.setLocked("EmployerStatusCC",false);
   				formObject.setLocked("legalEntity", false);
   				formObject.setLocked("MarketingCode", false);
   			}
   			else
   			{
   			formObject.setLocked("EmployerStatusPLExpact",true);
			formObject.setLocked("EmployerStatusPLNational",true);
			formObject.setLocked("indusSector",true);
			formObject.setLocked("indusMicro",true);
			formObject.setLocked("indusMAcro",true);
			formObject.setLocked("HighDelinquencyEmployer",true);
			formObject.setLocked("EmployerCategoryPL",true);
			formObject.setLocked("EmployerStatusCC",true);
   			}
   		}
           else
           {
        	   	
					//RLOS.mLogger.info("Employer code does not match main employer code");
					//alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL049");
					//RLOS.mLogger.info(alert_msg);
					OFUtility.updateState("", "error", "Duplicate employer code is selected and the original record to be selected","eventDispatched");
					//throw new ValidatorException(new FacesMessage(alert_msg));
				
           }
   		
          
       }
        
        if ("CustDetailVerification1_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
            CreditCard.mLogger.info( "Inside OK button");
            
                CreditCard.mLogger.info(m_objPickList.getSelectedValue().get(7));
                List<String> mylist = new ArrayList<String>();
                mylist.add("cmplx_CustDetailverification1_EmiratesId");
          
       }
        else if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	CreditCard.mLogger.info("Inside Button_State catch: ");
        	CreditCard.mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(1));
    		
        	OFUtility.updateState("AddressDetails_state", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
        	 
             
         } 
        else if ("Button_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	CreditCard.mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));
        	
        	OFUtility.updateState("AddressDetails_city", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
         }
        else if ("Designation_button1".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"cmplx_cust_ver_sp2_desig_remarks".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	CreditCard.mLogger.info("Inside Designation_button1 catch: "+m_objPickList.getSelectedValue().get(0));
        	
        	OFUtility.updateState("cmplx_cust_ver_sp2_desig_remarks", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
         }//sagarika on 17-06-19
        else if ("Designation_button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"cmplx_emp_ver_sp2_desig_remarks".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	CreditCard.mLogger.info("Inside Designation_button2 catch: "+m_objPickList.getSelectedValue().get(0));
        	
        	OFUtility.updateState("cmplx_emp_ver_sp2_desig_remarks", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
         }
        else if ("ButtonOECD_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"OECD_townBirth".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	CreditCard.mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));
        	
        	OFUtility.updateState("OECD_townBirth", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
         }
        else if ("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_Customer_SecNationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"BirthCountry".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"ResidentCountry".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"AddressDetails_country".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"PartMatch_nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_MOL_nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"CardDetails_BankName".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_EmploymentDetails_OtherBankCAC".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_EmploymentDetails_DesigVisa".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_EmploymentDetails_FreezoneName".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_EmploymentDetails_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"AlternateContactDetails_CardDisp".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
        ){
			
			String gcc = "BH,IQ,KW,OM,QA,SA,AE";
			
			OFUtility.updateState(m_objPickList.getAssociatedTxtCtrl(), "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			//Added By Shivang
			CreditCard.mLogger.info("@Shivang :: Inside Nationality Chnage Event:: NAtionality:"+m_objPickList.getSelectedValue().get(0));
			if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  m_objPickList.getSelectedValue().get(0).equalsIgnoreCase("AE")){
				CreditCard.mLogger.info("@Shivang :: Inside Nationality if block");
				formObject.setLocked("cmplx_Customer_VisaNo",true);
				formObject.setLocked("cmplx_Customer_VisaIssueDate",true);
				formObject.setLocked("cmplx_Customer_VisaExpiry",true);
			}
			else if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  (!m_objPickList.getSelectedValue().get(0).equalsIgnoreCase("AE"))){
				
				formObject.setLocked("cmplx_Customer_VisaNo",false);
				formObject.setLocked("cmplx_Customer_VisaIssueDate",false);
				formObject.setLocked("cmplx_Customer_VisaExpiry",false);
			}
			if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  gcc.contains(m_objPickList.getSelectedValue().get(0))){
				CreditCard.mLogger.info("@Shivang :: Inside Nationality if block");
				formObject.setNGValue("cmplx_Customer_GCCNational","Y");
			}
			else if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  !(gcc.contains(m_objPickList.getSelectedValue().get(0)))){
				CreditCard.mLogger.info("@Shivang :: Inside Nationality if block");
				formObject.setNGValue("cmplx_Customer_GCCNational","N");
			}
		}
        else if ("cmplx_Customer_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
        	CreditCard.mLogger.info("sagarika emp:"+m_objPickList.getSelectedValue().get(1));
        	
        	OFUtility.updateState("cmplx_Customer_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
        	//formObject.setNGValue("cmplx_EmploymentDetails_Designation",formObject.getNGValue("cmplx_Customer_Designation"));
        	OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
        	int framestate2=formObject.getNGFrameState("EmploymentDetails");
    		if(framestate2 == 0){
    			
    			formObject.setNGFrameState("EmploymentDetails",1);	
    			CreditCard.mLogger.info("fetched employment details sagarika 1");
    			OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue");
    		}
    		else {//end of fetchfrag of employment09
            	OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 

    			CreditCard.mLogger.info("fetched employment details sagarika");
    			//below code added by nikhil for PCSP-22
    			
    			
    		}
        	CreditCard.mLogger.info("sagarika emp value change:"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
        }
      //++below code added by nikhil for PCAS-1212 CR   
        new CC_Common().Update_Office_Address();
      //--above code added by nikhil for PCAS-1212 CR
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
   
    public String isNMFCNOAL(String InclCCAloc,String InclPLAloc,String EmpCatPL,String EmpStatusPL,String EmpStatusCC){
    	
    	if((InclCCAloc.equalsIgnoreCase("N") && InclPLAloc.equalsIgnoreCase("N"))
    	   ||(InclCCAloc.equalsIgnoreCase("false")&& InclPLAloc.equalsIgnoreCase("false"))
    	   ||(EmpCatPL.equalsIgnoreCase("CN")&& (EmpStatusPL.equalsIgnoreCase("OP")||EmpStatusPL.equalsIgnoreCase("RS"))
					&& (InclCCAloc.equalsIgnoreCase("false") || InclCCAloc.equalsIgnoreCase("N")))
		   ||(EmpCatPL.equalsIgnoreCase("CN")&&
					   (EmpStatusCC.equalsIgnoreCase("NM") || EmpStatusCC.equalsIgnoreCase("CN")||EmpStatusCC.equalsIgnoreCase(""))
				    && (EmpStatusPL.equalsIgnoreCase("NM") || EmpStatusPL.equalsIgnoreCase("CN")||EmpStatusPL.equalsIgnoreCase("")))
    	){
    		return "true";
    	}
    	else if((EmpStatusCC.equalsIgnoreCase("NM")&& (EmpStatusPL!="NM" && EmpStatusPL!="CN")
    			&&(InclCCAloc.equalsIgnoreCase("Y")||InclCCAloc.equalsIgnoreCase("true"))&&(InclPLAloc.equalsIgnoreCase("Y")||InclPLAloc.equalsIgnoreCase("true")))
    			||(EmpStatusPL.equalsIgnoreCase("NM")&& (EmpStatusCC!="NM" && EmpStatusCC!="CN")
    	    			&&(InclCCAloc.equalsIgnoreCase("Y")||InclCCAloc.equalsIgnoreCase("true"))&&(InclPLAloc.equalsIgnoreCase("Y")||InclPLAloc.equalsIgnoreCase("true"))))
    	{
    		return "true";
    	}
		return "false";
    	
    }
    
    private String getCESflag(String empId, String empName, FormReference formObject) 
	{
		String cesFlag = "";
		String query= "select top 1 CES_Plus from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPLOYER_CODE='"+empId+"' or EMPR_NAME='"+empName+"' order by CES_Plus desc";
		CreditCard.mLogger.info("########### query for CES: " + query);
		List<List<String>> list = formObject.getDataFromDataSource(query);
		if(!list.isEmpty() && list!= null){
			cesFlag = list.get(0).get(0);
		}
		CreditCard.mLogger.info("########### cesFlag value: " + cesFlag);
		return cesFlag;
	}
}
