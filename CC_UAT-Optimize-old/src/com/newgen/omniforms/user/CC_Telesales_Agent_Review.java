
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Telesales Agent Review

File Name                                                         : CC_Telesales_Agent_Review

Author                                                            : Disha

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.HashMap;


import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;

import com.newgen.omniforms.listener.FormListener;


public class CC_Telesales_Agent_Review extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	public void formLoaded(FormEvent pEvent)
	{
		
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "6,7,8,9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
        
           new CC_CommonCode().setFormHeader(pEvent);
        }catch(Exception e)
        {
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().FrameExpandEvent(pEvent);
				 break;
                
          case FRAGMENT_LOADED:
        	  CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
        	  fragment_loaded(pEvent,formObject);
        	   break;
			  
			case MOUSE_CLICKED:
				CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().FrameExpandEvent(pEvent);
				break;
			
			 case VALUE_CHANGED:
					CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().value_changed(pEvent);
					break;
         
                  default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		CreditCard.mLogger.info( "Inside PL PROCESS initialize()" );
		 

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		 

	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
	}


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		 if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("Customer");
				loadPicklistCustomer();
				
			}	
			
			if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				disableButtonsCC("Product");
			}
			
			if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("IncomeDetails");
				loadpicklist_Income();
			}
			
			if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("Liability_New");
			}
			
			if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("EMploymentDetails");
				loadPicklistEmployment();
			}
			
			if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("ELigibiltyAndProductInfo");
			}
			
			if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Address();
				disableButtonsCC("AddressDetails");
			}
			
			if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("AltContactDetails");
			} 
			
			if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("FATCA");
			}
			
			
			if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("OECD");
			}
			if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				disableButtonsCC("IncomingDocument");
			}
			if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("Reference_Details");
				
			}
			if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("SupplementCardDetails");
				
			}
			if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				disableButtonsCC("CardDetails");
				
			}
			if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
	               disableButtonsCC("CompanyDetails");
	               
					/*LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");*/
	               loadPicklist_CompanyDet();
	            }
			if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
      	 disableButtonsCC("AuthorisedSignDetails");
      	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
          LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
      }
			if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				 disableButtonsCC("PartnerDetails");
          LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
      }
			
			if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 CreditCard.mLogger.info( "Activity name is:" + sActivityName);
		        formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		    	
				formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setVisible("NotepadDetails_Frame3",true);
				formObject.setVisible("NotepadDetails_save",false);
				formObject.setHeight("NotepadDetails_Frame3",260);
				formObject.setVisible("NotepadDetails_SaveButton",true);
				formObject.setTop("NotepadDetails_SaveButton",400);
				//formObject.setLocked("NotepadDetails_Frame1",true);
				
			
			
		}
			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setVisible("DecisionHistory_CheckBox1",false);
				formObject.setVisible("DecisionHistory_Label1",false);
				formObject.setVisible("cmplx_DEC_VerificationRequired",false);
				formObject.setVisible("DecisionHistory_Label3",false);
				formObject.setVisible("DecisionHistory_Combo3",false);
				formObject.setVisible("DecisionHistory_Label6",false);
				formObject.setVisible("DecisionHistory_Combo6",false);
				formObject.setVisible("Decision_Label4",false);
				formObject.setVisible("cmplx_DEC_Remarks",false);
				formObject.setVisible("DecisionHistory_Label8",false);
				formObject.setVisible("DecisionHistory_Text4",false);
				formObject.setVisible("DecisionHistory_Label7",false);
				formObject.setVisible("DecisionHistory_Text3",false);
				formObject.setVisible("DecisionHistory_Label2",false);
				formObject.setVisible("DecisionHistory_Text2",false);
				formObject.setVisible("cmplx_DEC_ReferReason",false);
				formObject.setVisible("cmplx_DEC_Description",false);
				formObject.setVisible("cmplx_DEC_Strength",false);
				formObject.setVisible("cmplx_DEC_Weakness",false);
				formObject.setVisible("cmplx_DEC_ContactPointVeri",false);   
				
				disableButtonsCC("DecisionHistory");
				
    	loadPicklist3();
		 } 	
	
		}
	
	
	

}

