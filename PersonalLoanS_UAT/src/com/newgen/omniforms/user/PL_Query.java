package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;

public class PL_Query extends PLCommon implements FormListener{

	boolean IsFragLoaded=false;
	String queryData_load="";
	  FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
	//	System.out.println("Inside initiation PL");
		//PL_SKLogger.writeLog("PL Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        
        /*try{
            //loadPicklistCustomer();
            //formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
            //formObject.setNGFrameState("Part_Match",0);
            //loadPicklist_PartMatch();
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
            PL_SKLogger.writeLog("PL Part_Match","Inside Part_Match");            
            //LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			//LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where Code='PL' and ActivityName='Branch_Init'");
			
        }catch(Exception e)
        {
            PL_SKLogger.writeLog("PL Initiation", "Exception:"+e.getMessage());
        }*/
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	  formObject =FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
        	  PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
        	  
        	  if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
      			formObject.fetchFragment("ProductContainer", "Product", "q_Product");
      			//loadProductCombo();
      	
      		}
        	  else{
        		  new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);  
        	  }
				
					
					break;
                
          case FRAGMENT_LOADED:
        	  PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						formObject.setLocked("Customer_Frame1", true);
						
					}	
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						formObject.setLocked("Product_Frame1", true);
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						loadPicklistProduct("Personal Loan");
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
						formObject.setLocked("IncomeDetails_Frame1", true);							
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						formObject.setLocked("ExtLiability_Frame1", true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						formObject.setLocked("EMploymentDetails_Frame1",true);
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
						formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					//	formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
						formObject.setLocked("AddressDetails_Frame1", true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
						
						formObject.setLocked("AltContactDetails_Frame1",true);
					//	formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
					} 
					
					if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
						
						formObject.setLocked("FATCA_Frame6", true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){
						
						formObject.setLocked("KYC_Frame1",true);
					//	formObject.setEnabled("KYC_Save",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
						
						formObject.setLocked("OECD_Frame8",true);
					//	formObject.setEnabled("OECD_Save",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc")){
						formObject.setLocked("IncomingDoc_Frame1", true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						formObject.setLocked("DecisionHistory_Frame1", true);
					    formObject.setVisible("DecisionHistory_chqbook",false);
	                //	loadPicklist1();
	                	//Common function for decision fragment textboxes and combo visibility
	                	//decisionLabelsVisibility();
				 } 	
					//code by saurabh gupta on 27th June 17.
					/*String hiddenFieldString = formObject.getNGValue("fields_string");
					if(hiddenFieldString!=null && !hiddenFieldString.equalsIgnoreCase("") && !hiddenFieldString.equalsIgnoreCase(" ")){
						String[] fieldNames = hiddenFieldString.split(",");
						setChangedFieldsColor(fieldNames);
					}*/
			
			  break;
			  
			default: break;
	     
	     }

	}	
	
	
	public void initialize() {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	*/}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	*/}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {/*
		  formObject =FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		PL_SKLogger.writeLog("PersonnalLoanS","Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	*/}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	*/}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		 
		 if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Parallel"))
		 {
			 formObject.setNGValue("parallel_sequential","P");
		 }
		 else
		 {
			 formObject.setNGValue("parallel_sequential","S");
		 }
		saveIndecisionGrid();
	*/}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}



}
