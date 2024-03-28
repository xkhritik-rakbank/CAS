
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Compliance

File Name                                                         : CC_Compliance

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_Compliance extends CC_Common implements FormListener
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "8,9,11,12,14,15,16,17");
		formObject.setVisible("Compliance_Frame2",false);
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
	    	 CreditCard.mLogger.info("Inside CC_Hold_CPV CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            CreditCard.mLogger.info( "Exception:"+e.getMessage());
	        }
	        
	    }
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		new HashMap<String,String>();
		
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
				new CC_CommonCode().mouse_clicked(pEvent);
				break;
				//ended by yash on 24/8/2017
				
			
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

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		  formObject =FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		CreditCard.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		//added by akshay on 9/12/17 for multile Refer
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				List<String> objInput=new ArrayList<String> ();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:Compliance");
				CreditCard.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
				formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput);
			}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		LoadReferGrid();
		saveIndecisionGrid();
		//formObject.setNGValue("cmplx_DEC_Remarks","");
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
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
			
		}*/
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				//formObject.setLocked("Product_Frame1",true);
				/*formObject.setEnabled("Customer_save",true);
				formObject.setLocked("cmplx_Customer_ReferrorCode",false);
				formObject.setLocked("cmplx_Customer_ReferrorName",false);
				formObject.setLocked("cmplx_Customer_AppType",false);
				formObject.setLocked("cmplx_Customer_corporateCode",false);
				formObject.setLocked("cmplx_Customer_Bankingwithus",false);
				formObject.setLocked("cmplx_Customer_noofDependent",false);
				formObject.setLocked("cmplx_Customer_guardian",false);
				formObject.setLocked("cmplx_Customer_minor",false);
				formObject.setEnabled("Customer_Reference_Add",true);
				formObject.setEnabled("Customer_Reference__modify",true);
				formObject.setEnabled("Customer_Reference_delete",true);*/
				loadPicklistCustomer();
				
			}	
			
			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
				/*
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				*/
				formObject.setLocked("Product_Frame1",true);
				/*formObject.setEnabled("Product_Save",true);
				formObject.setEnabled("Product_Add",true);
				formObject.setEnabled("Product_Modify",true);
				formObject.setEnabled("Product_Delete",true);*/
			}
			
			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("IncomeDetails_Frame1",true);
				//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
				loadpicklist_Income();
			}
			
			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ExtLiability_Frame1",true);
				/*formObject.setEnabled("Liability_New_AECBReport",true);
				formObject.setEnabled("Liability_New_Save",true);*/
				LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");
}
			
			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("EMploymentDetails_Frame1",true);
				loadPicklistEmployment();
				loadPicklist4();
			}
			
			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				/*formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);*/
			}
			
			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Address();
				formObject.setLocked("AddressDetails_Frame1",true);
				//loadPicklist_Address();
				/*formObject.setEnabled("AddressDetails_Save",true);
				formObject.setEnabled("AddressDetails_addr_Add",true);
				formObject.setEnabled("AddressDetails_addr_Modify",true);
				formObject.setEnabled("AddressDetails_addr_Delete",true);*/
			}
			
			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("AltContactDetails_Frame1",true);
				/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
			} 
			
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("FATCA_Frame1",true);
				//12thsept
				formObject.setLocked("FATCA_Frame6",true);
				//12thsept
				
			}
			
			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("KYC_Frame1",true);
				/*formObject.setEnabled("KYC_Save",true);*/
			}
			
			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
				//12th sept
				//formObject.setLocked("OECD_Frame1",true);
				formObject.setLocked("OECD_Frame8",true);
				//12th sept
			}
			else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
				
				formObject.setLocked("IncomingDocument_Frame",true);
				/*formObject.setEnabled("OECD_Save",true);*/
			}
			else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				//12thsept
				//formObject.setLocked("Reference_Details_Frame1",true);
				formObject.setLocked("Reference_Details_Frame1",true);
				//12th sept
				
			}
			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SupplementCardDetails_Frame1",true);
				
			}
			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CardDetails_Frame1",true);
				
			}
			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
	               formObject.setLocked("CompanyDetails_Frame1", true);
	            /*   
					LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
	                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
	                
	            */	loadPicklist_CompanyDet();
	               }
			else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
            	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
	            // formObject.setLocked("AuthorisedSignDetails_Button4", true);
            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				}
			else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("PartnerDetails_Frame1", true);
                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            }
if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						formObject.setLocked("ExtLiability_Frame1",true);
						
					}

			//added by yash on 22/8/2017
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//12th sept
				 notepad_load();
				 notepad_withoutTelLog();
				 //12th sept
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
				formObject.setVisible("NotepadDetails_SaveButton",true);
				formObject.setTop("NotepadDetails_SaveButton",400);
			}
			//ended by yash
			//else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName()) && "DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				
			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())){
				
				 	CreditCard.mLogger.info("***********Inside checker decision history");
					fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
             		CreditCard.mLogger.info("***********Inside checker after fragment alignment decision history");
             		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
    				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
    				//for decision fragment made changes 8th dec 2017
                    loadPicklist3();
                   
                  //++Below code added by nikhil 13/11/2017 for Code merge
                   /* formObject.setVisible("DecisionHistory_Label11", false);
                    formObject.setVisible("DecisionHistory_dec_reason_code", false);
                    formObject.setLeft("DecisionHistory_Decision_Label4", formObject.getLeft("cmplx_DEC_Remarks"));*/
                  //--Above code added by nikhil 13/11/2017 for Code merge
			 
		 } 	
			else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("WorldCheck1_Frame1",true);
				loadInWorldGrid();
			}
			//12th sept
			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("PartMatch_Frame1",true);
//++ Below Code added By Yash on Oct 14, 2017  to fix : "for loading the Part Match nationality " : Reported By Shashank on Oct 09, 2017++
						
						loadPicklist_PartMatch();
						//++ Above Code added By Yash on Oct 14, 2017  to fix : "for loading the Part match nationality " : Reported By Shashank on Oct 09, 2017++
						
				
				
			}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMIncident_Frame1",true);
				
			}
			else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
				
			}
			else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("ExternalBlackList_Frame1",true);
				
			}
			else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("FinacleCore_Frame1",true);
				
			}
			else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				loadPicklist_Mol();
				formObject.setLocked("MOL1_Frame1",true);
				
			}
			else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				//loadPicklist_Address();
				formObject.setLocked("RejectEnq_Frame1",true);
				
			}
			else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("SalaryEnq_Frame1",true);
			}
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CC_Loan_Frame1",true);
				formObject.setLocked("CC_Loan_Frame2",true);
				formObject.setLocked("CC_Loan_Frame3",true);
				formObject.setLocked("totBalTransfer",true);
				loadPicklist_ServiceRequest();
			}
			
			//12th sept
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
            
            formObject.setLocked("Compliance_Frame1",true);
//++ Below Code added By Yash on Oct 24, 2017  to fix : "To populate the description of country in residence country and birth country" : Reported By Shashank on Oct 09, 2017++
            
          //++Below code added by nikhil 13/11/2017 for Code merge       
            formObject.setVisible("Compliance_Frame2", false);
          //--Above code added by nikhil 13/11/2017 for Code merge
            loadInComplianceGrid();
                    
             //++ Above Code added By Yash on Oct 24, 2017  to fix : "To populate the description of country in residence country and birth country" : Reported By Shashank on Oct 09, 2017++
            formObject.setLocked("Compliance_ComplianceRemarks",false);
            //12th september
            formObject.setLocked("Compliance_Remarks",true);
            formObject.setLocked("Compliance_Decision",true);
            //12th september
           // formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);
            formObject.setLocked("Compliance_Save",false); 
            formObject.setLocked("Compliance_Button2",false); 
            
            formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
			
			 formObject.setNGFrameState("World_Check",0);
			 int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
			 int Compliance_row_count=formObject.getLVWRowCount("cmplx_Compliance_cmplx_gr_compliance");
			 CreditCard.mLogger.info("value of world check "+n);
			 CreditCard.mLogger.info("value of compliance row count "+Compliance_row_count);
			 //below code modified by nikhil 02/12/17 to add all rows of world check to compliance grid
			 if(n>Compliance_row_count)
			 {
			 for(int i=0;i<n;i++)
   			 { 
   				 //SKLogger_CC.writeLog("CC value of world check row grid to get values","inside world check grid--- ");
   				String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,12);
   				//SKLogger_CC.writeLog("CC value of world check UID","UID "+UID);
   				formObject.setNGValue("Compliance_UID",UID);
   				
   				String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,15);
   				 //SKLogger_CC.writeLog("CC value of world check EI ","EI "+EI);
   				formObject.setNGValue("Compliance_EI",EI);
   				String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,0);
   				 //SKLogger_CC.writeLog("CC value of world check Name","Name "+Name);	
   				formObject.setNGValue("Compliance_Name",Name);
   				CreditCard.mLogger.info("Compliance Dob"+formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,2));
   				String Dob =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,2).equalsIgnoreCase("NA")?"":formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,2);
   				//SKLogger_CC.writeLog("CC value of world check Dob","Dob "+Dob);
   				formObject.setNGValue("Compliance_DOB",Dob);
   				CreditCard.mLogger.info("setting compliance"+Dob);
   				String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,7);
   				 //SKLogger_CC.writeLog("CC value of world check Citizenship","Citizenship"+Citizenship);	
   				formObject.setNGValue("Compliance_Citizenship",Citizenship);
   				String Remarks = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,16);
   				formObject.setNGValue("Compliance_Remarks",Remarks);
   				String Id_No = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,1);
   				formObject.setNGValue("Compliance_IdentificationNumber",Id_No);
   				String Age = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,3);
   				formObject.setNGValue("Compliance_Age",Age);
   				
   				String Position = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,4);
   				formObject.setNGValue("Compliance_Positon",Position);
   				String Deceased = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,5); 
   				formObject.setNGValue("Compliance_Deceased",Deceased);
   				String Category = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,6);
   				formObject.setNGValue("Compliance_Category",Category);
   				String Location = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,8);
   				formObject.setNGValue("Compliance_Location",Location);
   				String Identification = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,9); 
   				formObject.setNGValue("Compliance_Identification",Identification);
   				String Biography = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,10); 
   				formObject.setNGValue("Compliance_Biography",Biography);
   				String Reports = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,11); 
   				formObject.setNGValue("Compliance_Reports",Reports);
   				String Entered_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,13).equalsIgnoreCase("NA")?"":formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,13);
   				formObject.setNGValue("Compliance_EntertedDate",Entered_Date);
   				CreditCard.mLogger.info("setting compliance"+Entered_Date);
   				String Updated_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,14).equalsIgnoreCase("NA")?"":formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,14);
   				formObject.setNGValue("Compliance_UpdatedDate",Updated_Date);
   				CreditCard.mLogger.info("setting compliance"+Updated_Date);
   				String Document = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,18);  
   				formObject.setNGValue("Compliance_Document",Document);
   				String Decision = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,19);
   				formObject.setNGValue("Compliance_Decision",Decision);
   				String Match_Rank = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,20).equalsIgnoreCase("NA")?"":formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,20);
   				formObject.setNGValue("Compliance_MatchRank",Match_Rank);
   				CreditCard.mLogger.info("setting compliance"+Match_Rank);
   				String Alias = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,21);
   				formObject.setNGValue("Compliance_Alias",Alias);
   				String birth_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,22).equalsIgnoreCase("NA")?"":formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,22);
   				formObject.setNGValue("Compliance_BirthCountry",birth_Country);
   				String resident_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,23).equalsIgnoreCase("NA")?"":formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,23);
   				formObject.setNGValue("Compliance_ResidenceCountry",resident_Country);
   				String Address = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,24);
   				formObject.setNGValue("Compliance_Address",Address);
   				String Gender = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,25).equalsIgnoreCase("NA")?"":formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,25);
   				formObject.setNGValue("Compliance_Gender",Gender);
   				CreditCard.mLogger.info("setting compliance"+Gender);
   				String Listed_On = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,26);
   				formObject.setNGValue("Compliance_ListedOn",Listed_On);
   				String Program = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,27);
   				formObject.setNGValue("Compliance_Program",Program);
   				String external_ID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,28);
   				formObject.setNGValue("Compliance_ExternalID",external_ID);
   				String data_Source = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,29);
   				formObject.setNGValue("Compliance_DataSource",data_Source);
   				
   				formObject.setNGValue("Compliance_Winame",formObject.getWFWorkitemName());
   				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");
				 	 }
			 }
			//formObject.RaiseEvent("WFSave");
   			formObject.setNGFrameState("World_Check",1);
			//12th september
			CreditCard.mLogger.info( "befor locking frame2");
			formObject.setLocked("Compliance_Frame2",true);
			formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);
			CreditCard.mLogger.info( "after locking frame 2");
			
			//12th september
            
        }
	
	
	}
	
	

}

