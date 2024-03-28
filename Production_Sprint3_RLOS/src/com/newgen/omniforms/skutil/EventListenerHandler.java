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

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.NGFUserResourceMgr_RLOS;
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
		try
		{
		RLOS.mLogger.info("Inside method btnSearch_Clicked");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
		//PickList m_objPickList = formObject.getNGPickList(sProcessName+"SENDER_LOGsICAL_TERMINAL" , "BIC_CODE,RELATIONSHIP" , true , 10);
		String filter_value;
		RLOS.mLogger.info("Inside method btnSearch_Clicked inside print button"+m_objPickList.getAssociatedTxtCtrl());

		if ("EMploymentDetails_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) || "Customer_Search".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);        
			String query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'" 
							+ filter_value + "%' or EMPLOYER_CODE Like'" + filter_value + "%'";
			List<List<String>> result=formObject.getDataFromDataSource(query);

			m_objPickList.populateData(result);
		}
		else if ("CompanyDetails_SearchAloc".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) ) {
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);        
			String query="select distinct(EMPR_NAME),EMPLOYR_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like'" 
				+ filter_value + "%' or EMPLOYER_CODE Like'" + filter_value + "%'";
			List<List<String>> result=formObject.getDataFromDataSource(query);

			m_objPickList.populateData(result);
		}
		else if("cmplx_Customer_DSA_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) || "Customer_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	RLOS.mLogger.info("Inside method DSA code inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select UserId,UserId from NG_MASTER_SourceCode with (nolock)  where UserId like'%"  + filter_value + "%'";
              
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           RLOS.mLogger.info("Inside method DSAcode_Clicked"+query);
           m_objPickList.populateData(result);
       
       }
		//pcasi2694
		else if("cmplx_Customer_RM_TL_NAME".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	RLOS.mLogger.info("Inside method TLRM_Button inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select code,description from NG_MASTER_RM_TL with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
              
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
       
       }
		//PCASI-3589 by Alok Tiwari
        else if("cmplx_Customer_Sourcing_Branch_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())  || "Sourcing_Branch_Code_button".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	RLOS.mLogger.info("Inside method Sourcing_Branch_Code_Button inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select code,description from ng_master_source_branch_code with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
              
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
       
       }
	
		
		else  if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) || "OECD_townBirth".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"ButtonOECD_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
			RLOS.mLogger.info("Inside method Button_State inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);        
			String query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'"  + filter_value + "%' or Description Like'" + filter_value + "%'";

			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		}
		else  if ("Country".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
			RLOS.mLogger.info("Inside method Country inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);    
			//changed by deepak for Wrong code and description order 26/11/18
			String query="select Code,description from NG_Master_Country with (nolock)  where isActive='Y' and code like'"  + filter_value + "%' or Description Like'" + filter_value + "%'";

			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		}
		else  if ("city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
			RLOS.mLogger.info("Inside method Country inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);        
			String query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y' and code like'"  + filter_value + "%' or Description Like'" + filter_value + "%'";

			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		}
		else if( "cmplx_Customer_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())|| "Designation_button1".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	RLOS.mLogger.info("Inside method Designation_button1 inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(20);        
           String query="select code,description from NG_MASTER_Designation with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
        
       } //by jahnavi for JIRS-2894
		 
		//added by nikhil for check for PCSP-534
        else  if ("cmplx_EmploymentDetails_OtherBankCAC".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {	
        RLOS.mLogger.info("Inside method Button_City inside state"+m_objPickList.getAssociatedTxtCtrl());
        filter_value = m_objPickList.getSearchFilterValue();
        m_objPickList.setBatchRequired(true);
        m_objPickList.setBatchSize(25);
        //query changed by nikhil for PCSP-635
        String query="select code,description from NG_MASTER_othBankCAC with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           
        List<List<String>> result=formObject.getNGDataFromDataCache(query);
        RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
        m_objPickList.populateData(result);
    
        }
		//added by nikhil for check for PCSP-534
        else  if ("AlternateContactDetails_carddispatch".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {	
        RLOS.mLogger.info("Inside method Card Dispatch "+m_objPickList.getAssociatedTxtCtrl());
        filter_value = m_objPickList.getSearchFilterValue();
        m_objPickList.setBatchRequired(true);
        m_objPickList.setBatchSize(25);
        //query changed by nikhil for PCSP-635
        String query="select code,description from NG_MASTER_Dispatch with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           
        List<List<String>> result=formObject.getNGDataFromDataCache(query);
        RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
        m_objPickList.populateData(result);
    
        }
		
		else{
			filter_value = m_objPickList.getSearchFilterValue();
			RLOS.mLogger.info("PL"+"##AKshay"+m_objPickList.getAssociatedTxtCtrl()+"aman"+filter_value);
			
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);
			String val=NGFUserResourceMgr_RLOS.getMasterManagerSecond(m_objPickList.getAssociatedTxtCtrl());
			RLOS.mLogger.info("PL"+"##AKshay"+m_objPickList.getAssociatedTxtCtrl()+"aman"+val);
			String[] value= val.split(":");
			String sColumnName=value[1];
			String[] sCol=sColumnName.split(",");
			RLOS.mLogger.info("PL"+"##AKshay"+filter_value);
			String query="select "+value[1]+" from "+value[0]+" with (nolock)  where isActive='Y' and "+sCol[0]+" like'"  + filter_value + "%' or "+sCol[1]+" Like'" + filter_value + "%'";
			RLOS.mLogger.info("PL"+"##AKshay"+query);
			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			RLOS.mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);
		}

		m_objPickList.setVisible(true);
		}
		catch(Exception ex)
		{
			RLOS.mLogger.info("Inside Validator catch: "+ex.getMessage());
		}
	}

	@Override

	public void btnOk_Clicked(ActionEvent ae) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
		RLOS.mLogger.info("Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
		String alert_msg="";
		if ("EMploymentDetails_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) || "customer_search".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {//added by prabhakar
            RLOS.mLogger.info("Inside OK button");
            formObject.setNGValue("Is_employer_search", true);
            if( "customer_search".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) && formObject.isVisible("EMploymentDetails_Frame1")==false)
            {
            	formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
            	formObject.setTop("WorldCheck_fetch",470);

            }	
			try{
				RLOS.mLogger.info( "value of Emp Code: " +  m_objPickList.getSelectedValue().get(1));
				RLOS.mLogger.info( "value of main Emp Code: " +  m_objPickList.getSelectedValue().get(12));
				if( !m_objPickList.getSelectedValue().get(1).equalsIgnoreCase( m_objPickList.getSelectedValue().get(12)))
				{	

					RLOS.mLogger.info("Employer code does not match main employer code");
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL049");
					RLOS.mLogger.info(alert_msg);
					OFUtility.updateState("", "error", alert_msg,"eventDispatched");
					//throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					RLOS.mLogger.info("Employer code matched main employer code");
					formObject.setNGValue("cmplx_EmploymentDetails_IncInPL", CalcCheckboxValue( m_objPickList.getSelectedValue().get(2))); 
					formObject.setNGValue("cmplx_EmploymentDetails_IncInCC", CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)));
					OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(7)) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EmpName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EMpCode", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
					//formObject.setNGValue("cmplx_EmploymentDetails_IncInPL",CalcCheckboxValue( m_objPickList.getSelectedValue().get(2)));
					//formObject.setNGValue("cmplx_EmploymentDetails_IncInCC",CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)),false);
					
					RLOS.mLogger.info("after loading picklist:"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
					if(!("false".equalsIgnoreCase(CalcCheckboxValue( m_objPickList.getSelectedValue().get(2))) && "false".equalsIgnoreCase(CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)))))
					{
					OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue"); 
					//formObject.setNGValue("cmplx_EmploymentDetails_Indus_Macro", m_objPickList.getSelectedValue().get(5));
					// formObject.setNGValue("cmplx_EmploymentDetails_Indus_Micro", m_objPickList.getSelectedValue().get(6));
					//added by nikhil 03 march
					
					if("".equalsIgnoreCase(m_objPickList.getSelectedValue().get(5)))
                    {
                           OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", "--Select--" , "setNGValue"); 
                    }
                    else
                    {
                           OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue"); 
                    }      
                    RLOS.mLogger.info("before setting value in indus micro: "+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
                    if("".equalsIgnoreCase(m_objPickList.getSelectedValue().get(6)))
                    {
                           OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", "--Select--" , "setNGValue"); 
                    }
                    else
                    {
                           //OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(6) , "setNGValue"); 
                           formObject.setNGValue("cmplx_EmploymentDetails_Indus_Micro", m_objPickList.getSelectedValue().get(6));// Above is not working for Employer code 817833
                    }
                    RLOS.mLogger.info("after setting value in indus micro"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Micro"));
                    formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", true);
					formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", true);
					formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", true);
					}
					else
					{
						formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", false);
						formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);
						formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
					}
					
					//Added by nikhil for production case
					if(("".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL")) || "CN".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL")) || "NM".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL"))) && ("".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC")) || "CN".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC")) || "NM".equals(formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC"))))
					{
						if("".equals(m_objPickList.getSelectedValue().get(4)) || "".equals(m_objPickList.getSelectedValue().get(5)) || "".equals(m_objPickList.getSelectedValue().get(6)) )
						{
							formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", false);
							formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);
							formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
						}
						
						 formObject.setNGValue("cmplx_EmploymentDetails_Indus_Micro", m_objPickList.getSelectedValue().get(6));
						 OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue");
					}


					OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(8) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_CurrEmployer", "value", m_objPickList.getSelectedValue().get(9) , "setNGValue");
					OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusCC", "value", m_objPickList.getSelectedValue().get(10) , "setNGValue");
					OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusPL", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue");
					OFUtility.updateState("cmplx_EmploymentDetails_payroll_flag", "value", m_objPickList.getSelectedValue().get(13), "setNGValue");
					
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2).equalsIgnoreCase("IM")){
						RLOS.mLogger.info("Inside IM check:"+m_objPickList.getSelectedValue().get(14));
					if(m_objPickList.getSelectedValue().get(14).equalsIgnoreCase("P")){
						OFUtility.updateState("cmplx_EmploymentDetails_EmployerType", "value", "Private" , "setNGValue");
						formObject.setNGValue("cmplx_EmploymentDetails_EmployerType", "Private");
					}
					else if(m_objPickList.getSelectedValue().get(14).equalsIgnoreCase("G")){
						OFUtility.updateState("cmplx_EmploymentDetails_EmployerType", "value", "Government" , "setNGValue");
						formObject.setNGValue("cmplx_EmploymentDetails_EmployerType", "Government");
					}
					else{
						OFUtility.updateState("cmplx_EmploymentDetails_EmployerType", "value", "" , "setNGValue");
					}
						formObject.setLocked("cmplx_EmploymentDetails_EmployerType", true);
					}
					for(int i =0;i<11;i++){
						RLOS.mLogger.info( "Val of controls received at "+i+" is: "+m_objPickList.getSelectedValue().get(i));
					}       
					formObject.setLocked("cmplx_EmploymentDetails_EmpName", true);
					formObject.setLocked("cmplx_EmploymentDetails_EMpCode", true);

					formObject.setEnabled("cmplx_EmploymentDetails_IncInPL", false);
					formObject.setEnabled("cmplx_EmploymentDetails_IncInCC", false);
					formObject.setEnabled("cmplx_EmploymentDetails_Freezone", false);         
					 
					formObject.setLocked("cmplx_EmploymentDetails_FreezoneName", true);
					formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer", true);
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", true);             
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
					if(!"".equalsIgnoreCase( m_objPickList.getSelectedValue().get(8)))
					{
						formObject.setEnabled("cmplx_EmploymentDetails_Freezone", true);
						formObject.setNGValue("cmplx_EmploymentDetails_Freezone", "true");
						formObject.setLocked("FreeZone_Button", false);		
						
						
					}
					else
					{
						formObject.setEnabled("cmplx_EmploymentDetails_Freezone", false);
						formObject.setNGValue("cmplx_EmploymentDetails_Freezone", "false");
						formObject.setLocked("FreeZone_Button", true);		
					}
					RLOS.mLogger.info("Employer Category PL::"+m_objPickList.getSelectedValue().get(9));
					RLOS.mLogger.info("Employer Status PL::"+m_objPickList.getSelectedValue().get(11));
					RLOS.mLogger.info("Employer Status CC::"+m_objPickList.getSelectedValue().get(10));
					RLOS.mLogger.info("Included in PL Aloc"+m_objPickList.getSelectedValue().get(2));
					RLOS.mLogger.info("Included in CC Aloc"+m_objPickList.getSelectedValue().get(3));
					String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
					if(("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))||subProd.equalsIgnoreCase("SAL")) && (!subProd.equalsIgnoreCase("IM"))){
						if((CalcCheckboxValue( m_objPickList.getSelectedValue().get(2)).equalsIgnoreCase("false") && CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)).equalsIgnoreCase("false"))
							|| (CalcCheckboxValue( m_objPickList.getSelectedValue().get(2)).equalsIgnoreCase("N") && CalcCheckboxValue( m_objPickList.getSelectedValue().get(3)).equalsIgnoreCase("N"))
							|| ("CN".equalsIgnoreCase(m_objPickList.getSelectedValue().get(9))
								&& ("OP".equalsIgnoreCase(m_objPickList.getSelectedValue().get(11))||"RS".equalsIgnoreCase(m_objPickList.getSelectedValue().get(11)))
								&& ("CN".equalsIgnoreCase(m_objPickList.getSelectedValue().get(10))||m_objPickList.getSelectedValue().get(10).equalsIgnoreCase("")))
							|| (subProd.equalsIgnoreCase("SAL") && "CN".equalsIgnoreCase(m_objPickList.getSelectedValue().get(9))&&
								(("NM".equalsIgnoreCase(m_objPickList.getSelectedValue().get(10)) || "CN".equalsIgnoreCase(m_objPickList.getSelectedValue().get(10))||m_objPickList.getSelectedValue().get(10).equalsIgnoreCase(""))
								 && ("NM".equalsIgnoreCase(m_objPickList.getSelectedValue().get(11)) || "CN".equalsIgnoreCase(m_objPickList.getSelectedValue().get(11))||m_objPickList.getSelectedValue().get(11).equalsIgnoreCase("")) )))
						{
							RLOS.mLogger.info( " Inside if condition of CNOAL ::");
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
					String reqProd = formObject.getNGValue("PrimaryProduct");


					OFUtility.updateState("cmplx_EmploymentDetails_Auth_Desig", "value", m_objPickList.getSelectedValue().get(15), "setNGValue");
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProd)){
						if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")))
						{				

							formObject.setVisible("cmplx_EmploymentDetails_TL_Number", true);
							formObject.setVisible("EMploymentDetails_Label20", true);
							formObject.setVisible("EMploymentDetails_Label21", true);
							formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", true);
						}
					}
					
					// Production changes for CES flag
					formObject.setNGValue("cmplx_EmploymentDetails_CESflag",getCESflag(m_objPickList.getSelectedValue().get(1),m_objPickList.getSelectedValue().get(0),formObject));
					formObject.setLocked("cmplx_EmploymentDetails_CESflag",true);
				}
				
				
				//++below code added by nikhil for PCAS-1212 CR
				new RLOSCommon().Update_Office_Address();
				//--above code added by nikhil for PCAS-1212 CR
			}

			catch(ValidatorException vex){
				RLOS.mLogger.info("Inside Validator catch: "+alert_msg);
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			
			catch(Exception e){
				RLOS.mLogger.info( "Exception occured in btnOk_Clicked: " + e.getMessage()+ RLOSCommon.printException(e));
			}
		}
		else if ("CompanyDetails_SearchAloc".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
		{
			try{
				RLOS.mLogger.info( "value of Emp Code: " +  m_objPickList.getSelectedValue().get(1));
				RLOS.mLogger.info( "value of main Emp Code: " +  m_objPickList.getSelectedValue().get(12));
				if( !m_objPickList.getSelectedValue().get(1).equalsIgnoreCase( m_objPickList.getSelectedValue().get(12)))
				{	
					RLOS.mLogger.info("Employer code does not match main employer code");
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL049");
					RLOS.mLogger.info(alert_msg);
					OFUtility.updateState("", "error", alert_msg,"eventDispatched");
					//throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else
				{
					 
					OFUtility.updateState("compName", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
					
					OFUtility.updateState("indusSector", "value", m_objPickList.getSelectedValue().get(4) , "setNGValue"); 
					
					OFUtility.updateState("indusMAcro", "value", m_objPickList.getSelectedValue().get(5) , "setNGValue"); 
					
					OFUtility.updateState("indusMicro", "value", m_objPickList.getSelectedValue().get(6) , "setNGValue"); 
					
					
					
					

					

				}
			}

			catch(ValidatorException vex){
				RLOS.mLogger.info("Inside Validator catch: "+alert_msg);
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			catch(Exception e){
				RLOS.mLogger.info( "Exception occured in btnOk_Clicked: " + e.getMessage()+ RLOSCommon.printException(e));
			}
		}
		else if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			RLOS.mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("state", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 


		}
		else if ("cmplx_Customer_RM_TL_NAME".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			RLOS.mLogger.info("Inside TLRM_Button catch: "+m_objPickList.getSelectedValue().get(0));
			OFUtility.updateState("cmplx_Customer_RM_TL_NAME", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 

			//PCASI - 2694
			RLOS.mLogger.info( m_objPickList.getSelectedValue().get(0));
			formObject.setNGValue("cmplx_Customer_RM_TL_NAME",m_objPickList.getSelectedValue().get(0));
			String sRMTLName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			//formObject.setNGValue("cmplx_Customer_RM_TL_NAME",  m_objPickList.getSelectedValue().get(1));
			RLOS.mLogger.info("sRMTLName :: "+sRMTLName);
			if(!(sRMTLName.equalsIgnoreCase(null) || sRMTLName.isEmpty())){
				formObject.setNGValue("RM_Name", sRMTLName);
				formObject.setNGValue("RmTlNameLabel", sRMTLName);
				RLOS.mLogger.info("RMTLName :: "+formObject.getNGValue("RM_Name")+ " RmTlNameLabel :: "+formObject.getNGValue("RmTlNameLabel"));
			}
		}
		else if ("cmplx_Customer_DSA_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())  || "Customer_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			RLOS.mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(0));

			OFUtility.updateState("cmplx_Customer_DSA_Code", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			formObject.setNGValue("cmplx_Customer_DSA_Code",m_objPickList.getSelectedValue().get(0));

		}//by Alok for DSA Code //edit hritik  //by Alok for DSA CODE picklist
		
		else if ("cmplx_Customer_Sourcing_Branch_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())  || "Sourcing_Branch_Code_button".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			RLOS.mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(0));

			OFUtility.updateState("cmplx_Customer_Sourcing_Branch_Code", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			formObject.setNGValue("cmplx_Customer_Sourcing_Branch_Code",m_objPickList.getSelectedValue().get(0));

		}//by Alok for Sourcing_Branch_Code
		else if ("Button_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			RLOS.mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("city", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("ButtonOECD_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"OECD_townBirth".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			RLOS.mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("OECD_townBirth", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("cmplx_Customer_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			RLOS.mLogger.info("sagarika"+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_Customer_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			//++below code added by nikhil for PCAS-1212 CR
			new RLOSCommon().Update_Office_Address();
			//--above code added by nikhil for PCAS-1212 CR
		}
		
		else if ("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_Customer_SecNationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_Customer_Third_Nationaity".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"BirthCountry".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"ResidentCountry".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"Country".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"PartMatch_nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_MOL_nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"CardDetails_BankName".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_EmploymentDetails_OtherBankCAC".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_EmploymentDetails_DesigVisa".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_EmploymentDetails_FreezoneName".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
			//	||"cmplx_EmploymentDetails_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
			//	||"cmplx_Customer_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"AlternateContactDetails_carddispatch".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){
			
			
			
			OFUtility.updateState(m_objPickList.getAssociatedTxtCtrl(), "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
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
	private String getCESflag(String empId, String empName, FormReference formObject) 
	{
		String cesFlag = "";
		String query= "select top 1 CES_Plus from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPLOYER_CODE='"+empId+"' or EMPR_NAME='"+empName+"' order by CES_Plus desc";
		RLOS.mLogger.info("Query for CES flag "+query);
		List<List<String>> list = formObject.getDataFromDataSource(query);
		if(!list.isEmpty() && list!= null){
			cesFlag = list.get(0).get(0);
		}
		return cesFlag;
	}
}
