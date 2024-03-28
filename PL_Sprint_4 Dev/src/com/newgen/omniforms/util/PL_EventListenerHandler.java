package com.newgen.omniforms.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 svn checking
 */


import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		String filter_value="";
		mLogger.info("RLOS"+"Inside method m_objPickList.getAssociatedTxtCtrl()"+m_objPickList.getAssociatedTxtCtrl());
		
		if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(25);
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017	Disha
			  String query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS_CC,ALOC_REMARKS_PL,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME Like '"
	                    + filter_value + "%' or EMPLOYER_CODE Like '" + filter_value + "%'";
			List<List<String>> result=formObject.getNGDataFromDataCache(query);//to populate data in employment tab
			mLogger.info("query to load data is:"+query);
			//for(int i=0;i<20;i++)
				//mLogger.info("PL"+"Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
			m_objPickList.populateData(result);

		}
		else  if ("AddressDetails_state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
			mLogger.info("Inside method Button_State inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);        
			String query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'"  + filter_value + "%' or Description Like'" + filter_value + "%'";

			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		}
	/*	else  if ("Customer_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
			mLogger.info("Inside method Button_dsa_code inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);        
			String query="select UserId , UserId from NG_MASTER_SourceCode with (nolock)";
			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		} */
		
		else  if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) || "OECD_townBirth".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"ButtonOECD_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
			PersonalLoanS.mLogger.info("Inside method Button_State inside state"+m_objPickList.getAssociatedTxtCtrl());
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(20);        
			String query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'"  + filter_value + "%' or Description Like'" + filter_value + "%'";

			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);

		}

		//code by bandana for fcu
		else if("Designation_button1".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	PersonalLoanS.mLogger.info("Inside method Designation_button1 inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select description,code from NG_MASTER_Designation with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
         
       }
		 
	        else if("Designation_button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
	        {	PersonalLoanS.mLogger.info("Inside method Designation_button2 inside state"+m_objPickList.getAssociatedTxtCtrl());
	           filter_value = m_objPickList.getSearchFilterValue();
	           m_objPickList.setBatchRequired(true);
	           m_objPickList.setBatchSize(25);        
	           String query="select description,code from NG_MASTER_Designation with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
	           List<List<String>> result=formObject.getNGDataFromDataCache(query);
	           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
	           m_objPickList.populateData(result);
	       
	       }
		//code by bandana for fcu ends
		//code by saurabh1 for freezone start
		else if("EMploymentDetails_FreeZone_Button".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	PersonalLoanS.mLogger.info("Inside method EMploymentDetails_FreeZone_Button inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select description,code from NG_MASTER_freezoneName with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           PersonalLoanS.mLogger.info("saurabh1");   
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
         
       }
		//code by saurabh1 for freezone end
		//code by saurabh1 for desig button emp details start
		else if("EMploymentDetails_Designation_button".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	PersonalLoanS.mLogger.info("Inside method EMploymentDetails_Designation_button inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select description,code from NG_MASTER_freezoneName with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           PersonalLoanS.mLogger.info("saurabh1");   
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
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
	            PersonalLoanS.mLogger.info("##Arun");
	            for(int i=0;i<20;i++)
	            	PersonalLoanS.mLogger.info("Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
	        }
	        
	        else  if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){ 
	        	PersonalLoanS.mLogger.info("Inside method Button_State inside state"+m_objPickList.getAssociatedTxtCtrl());
	            filter_value = m_objPickList.getSearchFilterValue();
	            m_objPickList.setBatchRequired(true);
	            m_objPickList.setBatchSize(25);        
	            String query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
	               
	            List<List<String>> result=formObject.getNGDataFromDataCache(query);
	            PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
	            m_objPickList.populateData(result);
	        
	        }
	        else if("Button_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
	        {	PersonalLoanS.mLogger.info("Inside method Button_City inside state"+m_objPickList.getAssociatedTxtCtrl());
	           filter_value = m_objPickList.getSearchFilterValue();
	           m_objPickList.setBatchRequired(true);
	           m_objPickList.setBatchSize(25);        
	           String query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
	              
	           List<List<String>> result=formObject.getNGDataFromDataCache(query);
	           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
	           m_objPickList.populateData(result);
	       
	       }
			//PCASI - 2694
	        else if("cmplx_Customer_RM_TL_NAME".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
	        {	PersonalLoanS.mLogger.info("Inside method TLRM_Button inside state"+m_objPickList.getAssociatedTxtCtrl());
	           filter_value = m_objPickList.getSearchFilterValue();
	           m_objPickList.setBatchRequired(true);
	           m_objPickList.setBatchSize(25);        
	           String query="select code,description from NG_MASTER_RM_TL with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
	              
	           List<List<String>> result=formObject.getNGDataFromDataCache(query);
	           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
	           m_objPickList.populateData(result);
	       
	       }
		//PCASI-3589 by Alok Tiwari
	        else if("cmplx_Customer_Sourcing_Branch_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
	        {	PersonalLoanS.mLogger.info("Inside method Sourcing_Branch_Code_Button inside state"+m_objPickList.getAssociatedTxtCtrl());
	           filter_value = m_objPickList.getSearchFilterValue();
	           m_objPickList.setBatchRequired(true);
	           m_objPickList.setBatchSize(25);        
	           String query="select code,description from ng_master_source_branch_code with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
	              
	           List<List<String>> result=formObject.getNGDataFromDataCache(query);
	           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
	           m_objPickList.populateData(result);
	       
	       }
		
		

		//PCASI - 3880 by Alok Tiwari
	       else if("cmplx_Customer_DSA_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
	        {	PersonalLoanS.mLogger.info("Inside method DSA code inside state"+m_objPickList.getAssociatedTxtCtrl());
	           filter_value = m_objPickList.getSearchFilterValue();
	           m_objPickList.setBatchRequired(true);
	           m_objPickList.setBatchSize(25);        
	           String query="select UserId,UserId from NG_MASTER_SourceCode with (nolock)  where UserId like'%"  + filter_value + "%'";
	              
	           List<List<String>> result=formObject.getNGDataFromDataCache(query);
	           PersonalLoanS.mLogger.info("Inside method DSAcode_Clicked"+query);
	           m_objPickList.populateData(result);
	       
	       }
		//code by saurabh1 for desig button emp details end
		//code by saurabh1 for desigvisa button emp details start
		else if("EMploymentDetails_DesignationAsPerVisa_button".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))
        {	PersonalLoanS.mLogger.info("Inside method EMploymentDetails_DesignationAsPerVisa_button inside state"+m_objPickList.getAssociatedTxtCtrl());
           filter_value = m_objPickList.getSearchFilterValue();
           m_objPickList.setBatchRequired(true);
           m_objPickList.setBatchSize(25);        
           String query="select description,code from NG_MASTER_freezoneName with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
           PersonalLoanS.mLogger.info("saurabh1");   
           List<List<String>> result=formObject.getNGDataFromDataCache(query);
           PersonalLoanS.mLogger.info("Inside method btnSearch_Clicked"+query);
           m_objPickList.populateData(result);
         
       }
		  else  if ("cmplx_EmploymentDetails_OtherBankCAC".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {	
		        PersonalLoanS.mLogger.info("Inside method Button_City inside state"+m_objPickList.getAssociatedTxtCtrl());
		        filter_value = m_objPickList.getSearchFilterValue();
		        m_objPickList.setBatchRequired(true);
		        m_objPickList.setBatchSize(25);
		        String query="select code,description from NG_MASTER_othBankCAC with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
		           
		        List<List<String>> result=formObject.getNGDataFromDataCache(query);
		        m_objPickList.populateData(result);
		    
		    }
		        else  if ("AlternateContactDetails_carddispatch".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {	
		        	PersonalLoanS.mLogger.info("Inside method Card Dispatch inside state"+m_objPickList.getAssociatedTxtCtrl());
		            filter_value = m_objPickList.getSearchFilterValue();
		            m_objPickList.setBatchRequired(true);
		            m_objPickList.setBatchSize(25);
		            String query="select code,description from NG_MASTER_Dispatch with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";
		               
		            List<List<String>> result=formObject.getNGDataFromDataCache(query);
		            m_objPickList.populateData(result);
		        
		        }
		//code by saurabh1 for desigvisa button emp details end
		
		else{
			filter_value = m_objPickList.getSearchFilterValue();
			mLogger.info("PL"+"##AKshay"+m_objPickList.getAssociatedTxtCtrl()+"aman"+filter_value);			
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(25);
			String val=NGFUserResourceMgr_PL.getMasterManagerSecond(m_objPickList.getAssociatedTxtCtrl());
			mLogger.info("PL"+"##AKshay"+m_objPickList.getAssociatedTxtCtrl()+"aman"+val);
			String[] value= val.split(":");
			String sColumnName=value[1];
			String[] sCol=sColumnName.split(",");
			mLogger.info("PL"+"##AKshay"+filter_value);
			String query="select "+value[1]+" from "+value[0]+" with (nolock)  where isActive='Y' and "+sCol[0]+" like'%"  + filter_value + "%' or "+sCol[1]+" Like'%" + filter_value + "%'";
			mLogger.info("PL"+"##AKshay"+query);
			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			mLogger.info("Inside method btnSearch_Clicked"+query);
			m_objPickList.populateData(result);
		}

		
		m_objPickList.setVisible(true);
	}
	
/*
	public void btnSearch_Clicked(ActionEvent ae) {
		mLogger.info("RLOS"+"Inside method btnSearch_Clicked");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
		String filter_value="";

		if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
			filter_value = m_objPickList.getSearchFilterValue();
			m_objPickList.setBatchRequired(true);
			m_objPickList.setBatchSize(25);
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017	Disha
			String query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,EMPLOYER_CATEGORY_PL_EXPAT,EMPLOYER_CATEGORY_PL_NATIONAL,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,NATURE_OF_BUSINESS,ACCOMMODATION_PROVIDED,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,HIGH_DELINQUENCY_EMPLOYER from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME like '%" 
				+ filter_value + "%' or EMPLOYER_CODE Like '%" + filter_value + "%'";
			//List<List<String>> result=formObject.getNGDataFromDataCache(query);
			mLogger.info("PL"+"##AKshay");
			//for(int i=0;i<20;i++)
				//mLogger.info("PL"+"Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
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
*/

	@Override    
	public void btnOk_Clicked(ActionEvent ae) throws ValidatorException {
		PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		mLogger.info("EventListenerHandler"+ "Val of m_objPickList.getAssociatedTxtCtrl() is:"+m_objPickList.getAssociatedTxtCtrl());
		try{
		if (m_objPickList.getAssociatedTxtCtrl().equalsIgnoreCase("EMploymentDetails_Button2")) {
			mLogger.info("EventListenerHandler"+ "Inside OK button");
			
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
					
					if(!("false".equalsIgnoreCase( CalcCheckboxValue( m_objPickList.getSelectedValue().get(5))) && "false".equalsIgnoreCase(CalcCheckboxValue( m_objPickList.getSelectedValue().get(7)))))
					{
			        OFUtility.updateState("cmplx_EmploymentDetails_EmpIndusSector", "value", m_objPickList.getSelectedValue().get(11) , "setNGValue"); 
			        //OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
			       // OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue");
			        if("".equalsIgnoreCase(m_objPickList.getSelectedValue().get(12)))
		            {
		                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", "--Select--" , "setNGValue"); 
		            }
		            else
		            {
		                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Macro", "value", m_objPickList.getSelectedValue().get(12) , "setNGValue"); 
		            }      
		            if("".equalsIgnoreCase(m_objPickList.getSelectedValue().get(13)))
		            {
		                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", "--Select--" , "setNGValue"); 
		            }
		            else
		            {
		                   OFUtility.updateState("cmplx_EmploymentDetails_Indus_Micro", "value", m_objPickList.getSelectedValue().get(13) , "setNGValue"); 
		            }
					}
				    OFUtility.updateState("cmplx_EmploymentDetails_Freezone", "value",  CalcCheckboxValue(m_objPickList.getSelectedValue().get(14)) , "setNGValue"); 
			        OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(15) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_ownername", "value", m_objPickList.getSelectedValue().get(16) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_remarks", "value", m_objPickList.getSelectedValue().get(17) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Remarks_PL", "value", m_objPickList.getSelectedValue().get(18) , "setNGValue");
					OFUtility.updateState("cmplx_EmploymentDetails_highdelinq", "value", m_objPickList.getSelectedValue().get(19) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_CurrEmployer", "value", m_objPickList.getSelectedValue().get(20) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusCC", "value", m_objPickList.getSelectedValue().get(21) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_EmpStatusPL", "value", m_objPickList.getSelectedValue().get(22) , "setNGValue"); 
					OFUtility.updateState("cmplx_EmploymentDetails_Main_Emp_Code", "value", m_objPickList.getSelectedValue().get(23) , "setNGValue");
					OFUtility.updateState("cmplx_EmploymentDetails_payroll_flag", "value", m_objPickList.getSelectedValue().get(24) , "setNGValue");

				}
				else {
		        	OFUtility.updateState("", "error", "Duplicate employer code is selected and the original record to be selected","eventDispatched");

				}

				if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")))
				{
					formObject.setVisible("cmplx_EmploymentDetails_TL_Number", true);
					formObject.setVisible("EMploymentDetails_Label63", true);
					formObject.setVisible("EMploymentDetails_Label65", true);
					formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", true);
				}
				// Production changes for CES flag
				formObject.setNGValue("cmplx_EmploymentDetails_CESflag",getCESflag(m_objPickList.getSelectedValue().get(1),m_objPickList.getSelectedValue().get(0),formObject));
				formObject.setLocked("cmplx_EmploymentDetails_CESflag",true);	
		}
		else if ("cmplx_Customer_RM_TL_NAME".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside TLRM_Button catch: "+m_objPickList.getSelectedValue().get(1));
			OFUtility.updateState("cmplx_Customer_RM_TL_NAME", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		
			//PCASI - 2694
			PersonalLoanS.mLogger.info( m_objPickList.getSelectedValue().get(1));
			formObject.setNGValue("cmplx_Customer_RM_TL_NAME",m_objPickList.getSelectedValue().get(1));
			String sRMTLName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			//formObject.setNGValue("cmplx_Customer_RM_TL_NAME",  m_objPickList.getSelectedValue().get(1));
			PersonalLoanS.mLogger.info("sRMTLName :: "+sRMTLName);
			if(!(sRMTLName.equalsIgnoreCase(null) || sRMTLName.isEmpty())){
				formObject.setNGValue("RM_Name", sRMTLName);
				formObject.setNGValue("RmTlNameLabel", sRMTLName);
				PersonalLoanS.mLogger.info("RMTLName :: "+formObject.getNGValue("RM_Name")+ " RmTlNameLabel :: "+formObject.getNGValue("RmTlNameLabel"));
			}
		}
		else if ("Button_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_state".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("AddressDetails_state", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 


		} 
		/*else if ("Customer_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("Customer_Button2", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 


		} //by jahnavi for DSA CODE picklist*/
		
		else if ("cmplx_Customer_DSA_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(0));

			OFUtility.updateState("cmplx_Customer_DSA_Code", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			formObject.setNGValue("cmplx_Customer_DSA_Code",m_objPickList.getSelectedValue().get(0));

		}//by Alok for DSA Code //edit hritik 
		else if ("cmplx_Customer_Sourcing_Branch_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(0));

			OFUtility.updateState("cmplx_Customer_Sourcing_Branch_Code", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			formObject.setNGValue("cmplx_Customer_Sourcing_Branch_Code",m_objPickList.getSelectedValue().get(0));

		}//by Alok for Sourcing_Branch_Code
		else if ("Button_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"AddressDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("AddressDetails_city", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}//added by shweta
		else if ("ButtonLoan_City".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) ||"cmplx_LoanDetails_city".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_LoanDetails_city", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("ButtonOECD_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"OECD_townBirth".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside Button_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("OECD_townBirth", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		 else if ("cmplx_Customer_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
	        	
	        	OFUtility.updateState("cmplx_Customer_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
	        	//formObject.setNGValue("cmplx_EmploymentDetails_Designation",formObject.getNGValue("cmplx_Customer_Designation"));
	        	OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
	        	int framestate2=formObject.getNGFrameState("EmploymentDetails");
	    		if(framestate2 == 0){
	    			
	    			formObject.setNGFrameState("EmploymentDetails",1);	
	    			OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue");
	    		}
	    		else {//end of fetchfrag of employment09
	            	OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue");
	    		}
	        }
	/*	else if ("cmplx_EmploymentDetails_Designation".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_EmploymentDetails_Designation", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("cmplx_EmploymentDetails_FreezoneName".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_EmploymentDetails_FreezoneName", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("cmplx_EmploymentDetails_DesigVisa".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_EmploymentDetails_DesigVisa", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("cmplx_EmploymentDetails_OtherBankCAC".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_EmploymentDetails_OtherBankCAC", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("CardDetails_BankName".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("CardDetails_BankName", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("cmplx_MOL_nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_MOL_nationality", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("PartMatch_nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("PartMatch_nationality", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("AddressDetails_country".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("AddressDetails_country", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("ResidentCountry".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("ResidentCountry", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
		}
		else if ("BirthCountry".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("BirthCountry", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
		}
		else if ("cmplx_Customer_SecNationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(0));

			OFUtility.updateState("cmplx_Customer_SecNationality", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
		}
		else if ("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			mLogger.info("Inside ButtonLoan_City catch: "+m_objPickList.getSelectedValue().get(1));

			OFUtility.updateState("cmplx_Customer_Nationality", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
		}*/
		
		
		else if ("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_Customer_SecNationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
				||"cmplx_Customer_Third_Nationaity".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())
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
				||"AlternateContactDetails_carddispatch".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())){
				
			String gcc = "BH,IQ,KW,OM,QA,SA,AE";
			PersonalLoanS.mLogger.info("@Shivang :: Inside Nationality Chnage Event:: NAtionality:"+m_objPickList.getSelectedValue().get(0));

			OFUtility.updateState(m_objPickList.getAssociatedTxtCtrl(), "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
			PersonalLoanS.mLogger.info("@Shivang :: Inside Nationality Chnage Event:: NAtionality:"+formObject.getNGValue("AddressDetails_country"));

			if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  m_objPickList.getSelectedValue().get(0).equalsIgnoreCase("AE")){
			/*	formObject.setLocked("cmplx_Customer_VisaNo",true);
				formObject.setLocked("cmplx_Customer_VisaIssueDate",true);
				formObject.setLocked("cmplx_Customer_VIsaExpiry",true); */
			}
			else if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  (!m_objPickList.getSelectedValue().get(0).equalsIgnoreCase("AE"))){
				
				formObject.setLocked("cmplx_Customer_VisaNo",false);
				formObject.setLocked("cmplx_Customer_VisaIssueDate",false);
				formObject.setLocked("cmplx_Customer_VIsaExpiry",false);
			}
			if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  gcc.contains(m_objPickList.getSelectedValue().get(0))){
				formObject.setNGValue("cmplx_Customer_GCCNational","Y");
			}
			else if("cmplx_Customer_Nationality".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()) &&  !(gcc.contains(m_objPickList.getSelectedValue().get(0)))){
				formObject.setNGValue("cmplx_Customer_GCCNational","N");
			}
						
		}
		else  if ("CustDetailVerification1_Button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			  PersonalLoanS.mLogger.info( "Inside OK button");
	            
	            PersonalLoanS.mLogger.info(m_objPickList.getSelectedValue().get(7));
	                List<String> mylist = new ArrayList<String>();
	                mylist.add("cmplx_CustDetailverification1_EmiratesId");
	          
	       }
		//code by bandana for fcu
		 else if ("Designation_button1".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"cmplx_cust_ver_sp2_desig_remarks".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
	        	PersonalLoanS.mLogger.info("Inside Designation_button1 catch: "+m_objPickList.getSelectedValue().get(1));
	        	
	        	OFUtility.updateState("cmplx_cust_ver_sp2_desig_remarks", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
	         }//code by bandana end for fcu
		 else if ("Designation_button2".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			 PersonalLoanS.mLogger.info("Inside Designation_button2 catch: "+m_objPickList.getSelectedValue().get(1));
	        	
	        	OFUtility.updateState("cmplx_emp_ver_sp2_desig_remarks", "value", m_objPickList.getSelectedValue().get(1) , "setNGValue"); 
	         }
		 else if ("cmplx_Customer_DSA_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
				mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(0));

				OFUtility.updateState("cmplx_Customer_DSA_Code", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
				formObject.setNGValue("cmplx_Customer_DSA_Code",m_objPickList.getSelectedValue().get(0));

			}//by Alok for DSA Code //edit hritik 
		 else if ("cmplx_Customer_Sourcing_Branch_Code".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
				mLogger.info("Inside Button_State catch: "+m_objPickList.getSelectedValue().get(0));

				OFUtility.updateState("cmplx_Customer_Sourcing_Branch_Code", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
				formObject.setNGValue("cmplx_Customer_Sourcing_Branch_Code",m_objPickList.getSelectedValue().get(0));

			}//by Alok for Sourcing_Branch_Code
		 else if ("cmplx_emp_ver_sp2_desig_remarks".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			 PersonalLoanS.mLogger.info("Inside Designation_button2 catch: "+m_objPickList.getSelectedValue().get(1));
	        	
	        	OFUtility.updateState("cmplx_emp_ver_sp2_desig_remarks", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
	         }
		 else if ("cmplx_emp_ver_sp2_desig_as_visa_update".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())) {
			 PersonalLoanS.mLogger.info("Inside Designation_button2 catch: "+m_objPickList.getSelectedValue().get(1));
	        	
	        	OFUtility.updateState("cmplx_emp_ver_sp2_desig_as_visa_update", "value", m_objPickList.getSelectedValue().get(0) , "setNGValue"); 
	         }
		else
		{     
			mLogger.info("EventListenerHandler"+ "Employer code does not match main employer code");
			String alert_msg=NGFUserResourceMgr_PL.getAlert("val048");
			mLogger.info(alert_msg);
			OFUtility.updateState("", "error",alert_msg,"eventDispatched");			
		}
		if (!("ButtonOECD_State".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl())||"OECD_townBirth".equalsIgnoreCase(m_objPickList.getAssociatedTxtCtrl()))) {
			 new PLCommon().Update_Office_Address();//by shweta to update address
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
		if(input.equals("Y") || input.equalsIgnoreCase("Freezone") || input.equalsIgnoreCase("FZ"))
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
	private boolean getCESflag(String empId, String empName, FormReference formObject) 
	{
		boolean cesFlag = false;
		String query= "select top 1 CES_flag from ng_master_aloc_ces with (nolock) where Employer_Code='"+empId+"' or Employer_Name='"+empName+"'";
		List<List<String>> list = formObject.getDataFromDataSource(query);
		if(!list.isEmpty() && list!= null){
			if("true".equalsIgnoreCase(list.get(0).get(0)) || "Yes".equalsIgnoreCase(list.get(0).get(0)))
				return true;
			else
				return false;
		}
		return cesFlag;
	}
}
