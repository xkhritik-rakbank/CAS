
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects
Project/Product                                                   : Rakbank  
Application                                                       : Credit Card
Module                                                            : Hold CPV
File Name                                                         : CC_Hold_CPV
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

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_CA_HOLD extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              

	 ***********************************************************************************  */
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
		PersonalLoanS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		
		makeSheetsInvisible(tabName, "10,12,13,14,15,16,17");
		
	}
	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header             

	 ***********************************************************************************  */
	public void formPopulated(FormEvent pEvent) 
	{
     try{
    	 PersonalLoanS.mLogger.info("Inside CC_Hold_CPV CC");
    	 new PersonalLoanSCommonCode().setFormHeader(pEvent);
    	 enable_CPV();
        }catch(Exception e)
        {
            PersonalLoanS.mLogger.info( "Exception:"+e.getMessage());
        }
        
    }
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
				//	PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						 new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);						

					 		break;
					
						case FRAGMENT_LOADED:
							//	PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
								fragment_loaded(pEvent,formObject);
								
								
						
					  break;
					  
					case MOUSE_CLICKED:
						//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
						break;
				
					
					 case VALUE_CHANGED:
						//	PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new PersonalLoanSCommonCode().value_Change(pEvent);
							 
						break;	 
					
					 default: break;
					
				}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}


	public void initialize() {
		// TODO Auto-generated method stub
		
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//below code added by nikhil 7/12/17
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision")); 
		//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//formObject.setLocked("Customer_Frame1",true);
			LoadView(pEvent.getSource().getName());
			//setDisabled();
			loadPicklistCustomer();
		}	
		
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			formObject.setLocked("Product_Frame1",true);
		}
		
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("AddressDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			loadPicklist_Address();
		}
		
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("AltContactDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			
		}
		
		
		
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("OECD_Frame8",true);
			LoadView(pEvent.getSource().getName());
			
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++below code added by nikhil for Self-Supp CR
			Load_Self_Supp_Data();
			//--above code added by nikhil for Self-Supp CR
			formObject.setLocked("CardDetails_Frame1",true);
			
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_Account_num_sal", "select Acctid as Account_ID from  ng_RLOS_CUSTEXPOSE_AcctDetails where child_wi ='"+formObject.getWFWorkitemName()+"' and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' order by case when accttype='CURRENT ACCOUNT' then '1'  when accttype='FAST SAVER' then '2' else accttype end asc");
			LoadPickList("cmplx_IncomeDetails_Account_self_num", "select Acctid as Account_ID from  ng_RLOS_CUSTEXPOSE_AcctDetails where  child_wi ='"+formObject.getWFWorkitemName()+"' and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' order by case when accttype='CURRENT ACCOUNT' then '1'  when accttype='FAST SAVER' then '2' else accttype end asc");
		}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
               formObject.setLocked("CompanyDetails_Frame1", true);
            }
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
        	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
            
        	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
        }
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
            LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
        }
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		//	formObject.setLocked("EMploymentDetails_Frame1", true);
			LoadView(pEvent.getSource().getName());
			
			loadPicklist4();
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
           
         if(	NGFUserResourceMgr_PL.getGlobalVar("PL_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
					if(	NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
						formObject.setVisible("FinacleCore_avgbal", true);
						PersonalLoanS.mLogger.info( "set visible FinacleCore_Frame8 if condition ");
						formObject.setVisible("FinacleCore_Frame8", true);
						PersonalLoanS.mLogger.info( "after set visible FinacleCore_Frame8 if condition ");
						formObject.setVisible("FinacleCore_Frame2", false);
						formObject.setVisible("FinacleCore_Frame3", false);
						PersonalLoanS.mLogger.info( "Inside fianacle core fragment if condition ");
					}
					else{
						formObject.setVisible("FinacleCore_Frame2", true);
						formObject.setVisible("FinacleCore_Frame3", true);
						formObject.setVisible("FinacleCore_avgbal", false);
						PersonalLoanS.mLogger.info( "ELSE set visible FinacleCore_Frame8 ELSE condition ");
						formObject.setVisible("FinacleCore_Frame8", false);
						PersonalLoanS.mLogger.info( "AFTER  Inside fianacle core fragment else condition ");
					}
				}
         		formObject.setLocked("FinacleCore_Frame1", true);
         		LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
    			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

        }
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
			//LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			
			formObject.setLocked("PartMatch_Frame1", true);
        								
        }
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetails_Frame1",true);
			
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMIncident_Frame1",true);
			
		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("MOL1_Frame1",true);
		}
		
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification_Frame1",true);
			formObject.setLocked("cmplx_CustDetailVerification_Mob_No1_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Mob_No2_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_dob_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_POBoxNo_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_emirates_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_persorcompPOBox_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Resno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Offtelno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountryaddr_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_email1_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_email2_val",true);
		
			
		}
		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);

		}
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck_Frame1",true);
			
		}
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
		}
		//added by yash on 24/8/2017
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++Below code added by nikhil 13/11/2017 for Code merge
			
			//++Below code added by  nikhil 10/10/17 as per FSD 2.7.3	
			notepad_load();
		    formObject.setVisible("NotepadDetails_Frame3",true);
		  //--Above code added by  nikhil 10/10/17 as per FSD 2.7.3
		  //--Above code added by nikhil 13/11/2017 for Code merge
			formObject.setVisible("NotepadDetails_save",true);
			formObject.setTop("NotepadDetails_save",400);
		}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017	
			 PersonalLoanS.mLogger.info("***********Inside decision history");
	         fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label38,cmplx_Decision_NoofAttempts_CA#\n#DecisionHistory_Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
	       
	         formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				
	         PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
	       //for decision fragment made changes 8th dec 2017		
			formObject.setNGValue("cmplx_Decision_Decision", "Sendback");
			formObject.setNGValue("decision", "Sendback");
	                        
            loadPicklist3();
            
		 } 	
			
	
	}


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

