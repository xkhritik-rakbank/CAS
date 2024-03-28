package com.newgen.omniforms.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_Query extends PLCommon implements FormListener{

	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("Inside PL_Query-->FormLOaded()");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
        makeSheetsInvisible("Tab1", "12,13,14,15,16"); //Hide CPV, Dispatch Tab
        if("CustomerHold".equalsIgnoreCase(sActivityName)){
			makeSheetsInvisible(tabName, "6,7,11,12,13,14,15,16");//Hide Services request, system checkDisbursal,Compliance,FCU,CAD,OV,Dispatch	
		}
		if("SalesCoordinator".equalsIgnoreCase(sActivityName)){
			makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if(NGFUserResourceMgr_PersonalLoanS.getGlobalVar("CC_CSM").equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if("DDVT_Maker".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,12,13,14,15,16");

       	}
        else if("DDVT_Checker".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,12,13,14,15,16");
       	}
        else if("CPV".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,12,13,14,15,16");
       	}
        else if("Cad_Analyst2".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,12,13,14,15,16");
    		formObject.setVisible("SmartCheck_Label2",true);
    		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);
    		formObject.setVisible("CC_Loan_Frame1", false);
       	}
        else if("CAD_Analyst1".equalsIgnoreCase(sActivityName)){
        	
        	PersonalLoanS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

    		makeSheetsInvisible(tabName, "11,12,13,14,15,16");
    		formObject.setVisible("SmartCheck_Label2",true);
    		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);
    		formObject.setVisible("CC_Loan_Frame1", false);
    		formObject.setLocked("ELigibiltyAndProductInfo_Frame4", true);
    	
       	}
        else if("Disbursal".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,13,14,15,16");
        	
       	}
        else if("FCU".equalsIgnoreCase(sActivityName)||"FPU".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible("Tab1", "7,11,12,13,14,15,16");
        	formObject.setVisible("Decision_ListView1", true);
        	formObject.setLocked("Fpu_Grid_Frame1",true);
        	formObject.setLocked("cmplx_FPU_Grid_Analyst_Name", true);
        	formObject.setNGFrameState("FPU_GRID", 0);
        	formObject.setLocked("FPU_GRID", true);
       	}
        else if("Original_Validation".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible("Tab1", "7,12,13,14,15,16");
       	}
        else if("Compliance".equalsIgnoreCase(sActivityName)){
        	PersonalLoanS.mLogger.info("Inside Compliance call from Creditcard");
        	makeSheetsInvisible(tabName, "11,12,14,15,16");
    		formObject.setVisible("Compliance_Frame2",false);
       	}
        else if("Rejected_queue".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "6,7,11,12,13,14,15,16");
       	}
        else if("Rejected_Application".equalsIgnoreCase(sActivityName)){	       	
        	makeSheetsInvisible(tabName, "6,7,11,12,13,14,15,16");
       	}
        else if("Fulfillment_RM".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if("Telesales_RM".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if("Telesales_Agent".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "6,7,11,12,13,14,15,16");
       	}
        else if("Post_Disbursal".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if("CSM_Review".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,,11,12,13,14,15,16");
       	}
        else if("HR".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if("RM".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "6,7,11,12,13,14,15,16");
       	}
        else if("DSA_CSO_Review".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if("CardCollection".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15");
       	}
        else if("Sales_Approver".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "6,7,11,12,13,14,15,16");
       	}
        else if("Dispatch".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "7,11,12,13,14,15,16");
       	}
        else if("Hold_CPV".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,12,13,14,15,16");
       	}
        else if("Smart_CPV".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,12,13,14,15,16");
       	}
    	//new java file for new worksteo CPV_Analyst has been added 
        else if("CPV_Analyst".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "11,12,13,14,15,16");
       	}   
    	//Added by Rajan for PCASP-1879
        else if("Smart_CPV_Hold".equalsIgnoreCase(sActivityName)){
        	makeSheetsInvisible(tabName, "9,12,13,14,15,16");
        }
		
        else{
        	mLogger.info("Inside else");
        	makeSheetsInvisible(tabName, "13,14,15,16");//For PCSP-337
            
        }
		
	
	}

	public String getTypeProd() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		//Deepak 06june2018 below code changed because of null pointer exception in sys logs
		int prd_count=formObject.getLVWRowCount(prodGridId);
		String typeProd = "";
		if(prd_count>0){
			typeProd = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),0);
		}
		//String typeProd = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),0);

		return typeProd;
	}
	public void formPopulated(FormEvent pEvent) 
	{
		new PersonalLoanSCommonCode().setFormHeader(pEvent);
		//empty method
		
	}
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */
	public String getSubproduct() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		//Deepak 06june2018 below code changed because of null pointer exception in sys logs
		int prd_count=formObject.getLVWRowCount(prodGridId);
		String subprod = "";
		if(prd_count>0){
			subprod = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),2);
		}

		//String subprod = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),2);

		return subprod;
	}

	public void fragment_loaded(ComponentEvent pEvent)
	{
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPicklistCustomer();

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1", true);
			String ReqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			formObject.setLocked("Product_Frame1",true);
			formObject.setEnabled("Scheme",false);
			formObject.setLocked("Scheme",true);
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with(nolock) where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			LoadPickList("subProd", "select '--Select--' as description,'' as code union select description,code from ng_MASTER_SubProduct_PL with (nolock) order by code");
			//formObject.setEnabled("Scheme",false);
			//formObject.setLocked("Scheme",true);

			//LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			//String sub_prod="EXP".equalsIgnoreCase(getSubproduct())?"Expat":"National";
			/*formObject.clear("Scheme");
			//formObject.setNGValue("Scheme", "--Select--");
			String TypeofProduct=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),0);
			String subprod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2);
			formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),4);
			formObject.clear("Scheme");
			//formObject.setNGValue("Scheme", "--Select--");
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
			LoadPickList("SubProd", "select '--select--',''as 'code' union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL  with (nolock) order by code");
			//LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			//added by abhishek
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with (nolock)  where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"' order by code");
			LoadPickList("EmpType", " select '--Select--' union select convert(varchar, description) from ng_MASTER_PRD_EMPTYPE with (nolock)  where SubProduct = '"+ReqProd+"'");
			//LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='"+sub_prod+"'   and TypeofProduct='"+getTypeProd()+"' order by SCHEMEID");
			//String q="select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='"+sub_prod+"'   and TypeofProduct='"+getTypeProd()+"' order by SCHEMEID";
			//PersonalLoanS.mLogger.info("queryyy for scheme to load"+q);
			int n = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			PersonalLoanS.mLogger.info("PL query: "+ "Row count: "+n);
			for(int i=0;i<n;i++)
			{
				PersonalLoanS.mLogger.info("value of requested type is"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+ 0+ 1) +"");
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				PersonalLoanS.mLogger.info("schemeeeee"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7));
				//if(NGFUserResourceMgr_PL.getGlobalVar("PL_PL").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1)) || NGFUserResourceMgr_PL.getGlobalVar("PL_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1))){
					String sscheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);
					
					String sub_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2 );
					String sub_prod="EXP".equalsIgnoreCase(sub_product)?"Expat":"National";
					String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0);
					LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='"+sub_prod+"'   and TypeofProduct='"+prod_type+"' order by SCHEMEID");
					String q="select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='"+sub_prod+"'   and TypeofProduct='"+prod_type+"' order by SCHEMEID";
					PersonalLoanS.mLogger.info("queryyy for scheme to load"+q);
					String map_scheme="select top 1 SCHEMEDESC from NG_master_Scheme where SCHEMeID = '"+sscheme+"'";
					List<List<String>> result = formObject.getDataFromDataSource(map_scheme);
					
					if(result!=null && !result.isEmpty()){
						if(null!=result.get(0) && !result.get(0).isEmpty()){
							String s=result.get(0).get(0);
							formObject.setNGValue("SCHEM", s);
							formObject.setNGValue("Scheme",s);
							PersonalLoanS.mLogger.info("scheme"+s);
						}
					}
					
					break;                                                          
				//}
			}*/
			PersonalLoanS.mLogger.info("***********Inside scheme load read only");
			loadPicklistProduct("Personal Loan");
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1", true);				
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1", true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()) || "Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setVisible("Liability_New_Label1",false);
				formObject.setVisible("Liability_New_MOB",false);
				formObject.setVisible("Liability_New_Label2",true);
				formObject.setVisible("Liability_New_Utilization",true);
				formObject.setVisible("Liability_New_Label3",false);
				formObject.setVisible("Liability_New_Outstanding",false);
				formObject.setVisible("Liability_New_DPD30inlast6",false);
				formObject.setVisible("Liability_New_DPD30inlast18",false);//by shweta for pcasp-1520
				formObject.setVisible("Liability_New_DPD30inlast6",false);
				formObject.setVisible("Liability_New_Label4",false);
				formObject.setVisible("Liability_New_writeoff",false);//by shweta for pcasp-1521
				formObject.setVisible("Liability_New_Label5",false);
				formObject.setVisible("Liability_New_worststatuslast24",false);//by shweta for pcasp-1522
				formObject.setVisible("Liability_New_Label2",false);
				formObject.setVisible("Liability_New_Utilization",false);
				formObject.setVisible("Liability_New_Label10",false);
				formObject.setVisible("Liability_New_rejAppsinlast3months",false);//by shweta for pcasp-1523
				//by shweta for pcasi-994,995,996
				formObject.setVisible("ExtLiability_QCAmt", false);
				formObject.setVisible("ExtLiability_QCEMI",false);
				formObject.setVisible("ExtLiability_CACIndicator",false);
				formObject.setVisible("ExtLiability_Label20",false);
				formObject.setVisible("ExtLiability_Label23",false);
			}
			formObject.setEnabled("ExtLiability_AECBReport", true);
			formObject.setLocked("ExtLiability_AECBReport", false);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
		
			LoadView(pEvent.getSource().getName());
			loadPicklist4();
			if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()) || "Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setVisible("EMploymentDetails_Label34",true);
				formObject.setVisible("cmplx_EmploymentDetails_HeadOfficeEmirate",true);
			}
			
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			formObject.setLocked("ELigibiltyAndProductInfo_Frame4", true);
			formObject.setLocked("ELigibiltyAndProductInfo_Frame6", true); // pcasi 3525
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			LoadView(pEvent.getSource().getName());
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			//Code ended by siva on 01112019 for PCAS-1401
			
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
			Loadpicklistfatca();
			formObject.setLocked("FATCA_Frame6", true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
			loadPicklist_KYC();
			formObject.setLocked("KYC_Frame7",true);
			
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadView(pEvent.getSource().getName());
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
			
		}

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("IncomingDoc_Frame1", true);
		}
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//done by nikhil for PCAS-2358
			formObject.setLocked("CustDetailVerification_Frame1",true);
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");//pcasi-1003
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			enable_custVerification();
		}
		
		//code changes by bandana starts
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();

		}
		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanDetails_Frame1",true);

		}
		else if ("FPU_GRID".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Fpu_Grid_Frame1",true);
			formObject.setEnabled("Fpu_Grid_Frame1",false);
			formObject.setNGFrameState("FPU_GRID", 0);
		}
		//code changes by bandana ends

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
		/*	formObject.setLocked("DecisionHistory_Frame1", true);
			formObject.setVisible("DecisionHistory_chqbook",false);*/
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			formObject.setVisible("Decision_ListView1", true);
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017
			
		} 
		//below code added by shweta to sync PL with CC

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();
			
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			//enable_homeVerification(); pcasi-1027
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			enable_ResVerification();
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setEnabled("OfficeandMobileVerification_Frame1",false);
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			PersonalLoanS.mLogger.info( "set visible OfficeandMobileVerification inside condition ");

			//++Below code added by nikhil 13/11/2017 for Code merge
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
			
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);
			enable_loanCard();
			
		}
		
	/*	else if("Disbursal".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("Hritik to visible the disbursal");
        	formObject.setVisible("Loan_Disbursal_Frame2",true);
        	formObject.setLocked("Loan_Disbursal_Frame2",true);
        	
       	} // Hritik */
		
		else if("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("@Rajan1 Inside smart check");
			formObject.setLocked("SmartCheck_Frame1",true);//
			formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid",false);
		
		}
		//Below changed by Rajan on 24/06/2021
		else if ("EmploymentVerification_s2".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("@Rajan1");
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
			LoadPickList("cmplx_FPU_Grid_Officer_Name", "select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
		}
		else if ("Employment_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("@Rajan1");
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
			LoadPickList("cmplx_FPU_Grid_Officer_Name", "select ' --Select-- 'as UserName union select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
		}
		else if ("Cust_ver_sp2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			PersonalLoanS.mLogger.info("@cust_ver_sp2");
			LoadView(pEvent.getSource().getName());
		}
		//above code added by shweta to sync PL with CC

	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {

		switch(pEvent.getType()) {

		case FRAME_EXPANDED:        	  
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);          	
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
			break;
			
		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;

		default: break;

		}

	}	


	public void initialize() {	//empty method
	}


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		//empty method

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
