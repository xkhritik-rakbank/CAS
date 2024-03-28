//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOS_ReInitiate.java
//Author					: Deepak Kumar
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------

package com.newgen.omniforms.user;

//import com.newgen.omni.wf.util.excp.NGException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;
@SuppressWarnings("serial")


public class RLOS_ReInitiate extends RLOSCommon implements FormListener
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	String ReqProd=null;
	//RLOS.mLogger.info("Inside initiation RLOS");
	public void formLoaded(FormEvent pEvent)
	{
		/*RLOS.mLogger.info("Inside initiation RLOS");
		RLOS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());*/
	}

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);

		//	RLOS.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			//RLOS.mLogger.info( "currTime:" + currDate);
			//formObject.setNGValue("Intro_Date",formObject.getNGValue("CreatedDate"));//Tarang to be removed on friday(1/19/2018)
			formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Re Initiation");
			formObject.setNGValue("Created_By",formObject.getUserName());
			
			formObject.setNGValue("lbl_init_channel_val","Re_Initiate");
			
			formObject.setNGValue("lbl_curr_date_val",currDate);
			formObject.setNGValue("ApplicationRefNo", formObject.getWFFolderId());
			formObject.setNGValue("lbl_user_name_val",formObject.getUserName());
			formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
			formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Subproduct_productGrid"));
			formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));
			formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
						
			
			String init_Channel=formObject.getNGValue("initiationChannel");
			if("".equals(init_Channel))
				formObject.setNGValue("initiationChannel","Branch_Init");
			//if(formObject.getNGValue("empType").contains("Salaried")){
			if(formObject.getNGValue("EmploymentType").contains("Salaried") || formObject.getNGValue("EmploymentType").contains("Pensioner")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				formObject.setSheetVisible("ParentTab",1, false);
			}
			
			//else if(formObject.getNGValue("empType").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed"))){
			else if(formObject.getNGValue("EmploymentType").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed"))){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
				formObject.setSheetVisible("ParentTab",3, false);
			}
			//added By Akshay-23/7/2017
			new RLOSCommonCode().CustomerFragment_Load();
			
			//ended By Akshay-23/7/2017

			if(formObject.getNGValue("Product_Type").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan")) && Integer.parseInt(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",110);
			}
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equals(formObject.getNGValue("PrimaryProduct")))
				formObject.setSheetVisible("ParentTab",7, true);

			//RLOS.mLogger.info("Value Of Init Channel:"+init_Channel);
		
			if("".equals(formObject.getNGValue("cmplx_Customer_MiddleName")))
			{
				formObject.setNGValue("Cust_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}

			else{
				formObject.setNGValue("Cust_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
		}
		catch(Exception e)
		{
			//RLOS.mLogger.info( "Exception:"+e.getMessage());
			RLOS.logException(e);
		}
	}
	
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();		

		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		try
		{
			switch (pEvent.getType()) 
			{		
			case FRAME_EXPANDED:				
			new RLOSCommonCode().call_Frame_Expanded(pEvent);
				break;

			case MOUSE_CLICKED:
				new RLOSCommonCode().call_Mouse_Clicked(pEvent);
				break;	
				
			case TAB_CLICKED:

				RLOS.mLogger.info(formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName())));
				
				//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded

				break;


			case FRAGMENT_LOADED:
				new RLOS_Initiation().call_Fragement_Loaded(pEvent);
				 break;

			case VALUE_CHANGED:
				new RLOSCommonCode().call_Value_Changed(pEvent);
				 break;
				 
			default:
				break;

			}
		}

		finally
		{
			RLOS.mLogger.info( "Inside finally after try catch");
		}
	
	}	
	public void visibilityFrameIncomeDetails(FormReference formObject) {

		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct")) /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
			//SKLogger.writeLog("RLOS @AKSHAY", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
			formObject.setVisible("IncomeDetails_Label12", true);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);	
			formObject.setVisible("IncomeDetails_Label14", true);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", true);	
		}	
		/*else{
			formObject.setVisible("IncomeDetails_Label12", false);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);	
			formObject.setVisible("IncomeDetails_Label14", false);
			formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", false);			
		}*/
		//}

		//if(n>0)
		//{					
		//String EmpType=formObject.getNGValue("empType");
		String EmpType=formObject.getNGValue("EmploymentType");
		//RLOS.mLogger.info( "Emp Type Value is:"+EmpType);

		if("Salaried".equalsIgnoreCase(EmpType)|| "Salaried Pensioner".equalsIgnoreCase(EmpType)|| "Pensioner".equalsIgnoreCase(EmpType))
		{
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 730);
			formObject.setHeight("IncomeDetails_Frame1", 680);	
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomeDetails_Label11", false);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", false);
				formObject.setVisible("IncomeDetails_Label13", false);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", false);
				formObject.setVisible("IncomeDetails_Label3", false);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", false);
			}	
			else{
				formObject.setVisible("IncomeDetails_Label11", true);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", true);
				formObject.setVisible("IncomeDetails_Label13", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", true);
				formObject.setVisible("IncomeDetails_Label3", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", true);
			}	
		}

		else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(EmpType))
		{							
			formObject.setVisible("IncomeDetails_Frame2", false);
			formObject.setTop("IncomeDetails_Frame3",40);
			formObject.setHeight("Incomedetails", 300);
			formObject.setHeight("IncomeDetails_Frame1", 280);
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomeDetails_Label20", false);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", false);
				formObject.setVisible("IncomeDetails_Label22", false);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", false);
				formObject.setVisible("IncomeDetails_Label35", false);
				formObject.setVisible("IncomeDetails_Label5", false);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", false);
				formObject.setVisible("IncomeDetails_Label36", true);
			}	
			else{
				formObject.setVisible("IncomeDetails_Label20", true);
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", true);
				formObject.setVisible("IncomeDetails_Label22", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", true);
				formObject.setVisible("IncomeDetails_Label35", true);
				formObject.setVisible("IncomeDetails_Label5", true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", true);
				formObject.setVisible("IncomeDetails_Label36", true);
			}	
		}

	}

	public String makeInputForGrid(String customerName) {
		String temp = "<ListItem><SubItem>"+customerName+"</SubItem>"
				+"<SubItem>Authorised signatory</SubItem>"
				+"<SubItem>Primary</SubItem>";

		for(int i=0;i<21;i++){
			temp+= "<SubItem></SubItem>";
		}

		temp+="</ListItem>";

		return temp;
	}

	/*public String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		RLOS.mLogger.info( "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date.equals(null) || date.equals(""))
		{
			return new_date;
		}

		try{
		SimpleDateFormat sdf_old=new SimpleDateFormat(Old_Format);
		SimpleDateFormat sdf_new=new SimpleDateFormat(new_Format);
		new_date=sdf_new.format(sdf_old.parse(date));
		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in parsing date:"+printException(e));
		}
		return new_date;
	}*/

	public void saveFormStarted(FormEvent pEvent) 
	{
		//RLOS.mLogger.info( "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		//RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");

		//formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
		formObject.setNGValue("EmploymentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
		formObject.setNGValue("priority_ProdGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,9));
		formObject.setNGValue("Subproduct_productGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2));
		formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By Akshay on 16/9/17 to set header
		formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header


		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		
		formObject.setNGValue("NewApplicationNo", formObject.getWFFolderId());
	}

	public void saveFormCompleted(FormEvent pEvent) {
	//	RLOS.mLogger.info( "Inside saveFormCompleted()" + pEvent);


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		//RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");


		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex = null;
		outputindex = formObject.getNGDataFromDataCache(squery);
		//RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);
	}

	public void submitFormStarted(FormEvent pEvent)
			throws ValidatorException {
		//RLOS.mLogger.info( "Inside submitFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		formObject.setNGValue("loan_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
		formObject.setNGValue("email_id", formObject.getUserName()+"@rlos.com");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		/*	String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex = null;
		List<List<String>> secondquery=null;
		outputindex = formObject.getNGDataFromDataCache(squery);
		RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);*/
		//added for document function
		int itemIndex=formObject.getWFFolderId();
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);
		IncomingDoc();
	//	RLOS.mLogger.info( "after calling incomingdoc function");
		//added for document function

		//RLOS.mLogger.info( "Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		saveIndecisionGrid();
		TypeOfProduct();//Loan type
		//CIFIDCheck();  
		//tanshu started
		/*		HashMap<String,String> hm1= new HashMap<String,String>();
		String requested_product="";
		String requested_subproduct="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		RLOS.mLogger.info("valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				RLOS.mLogger.info(requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				RLOS.mLogger.info(requested_subproduct);

			}
		}
		String sQuery="SELECT Name FROM PDBDocument with(nolock) WHERE DocumentIndex IN (SELECT DocumentIndex FROM PDBDocumentContent with(nolock) WHERE ParentFolderIndex= '"+itemIndex+"')";
		outputindex = null;
		RLOS.mLogger.info( "sQuery for document name is:" +  sQuery);
		outputindex = formObject.getNGDataFromDataCache(sQuery);
		RLOS.mLogger.info( "outputItemindex is:" +  outputindex);






		if(outputindex==null || outputindex.size()==0) {
			RLOS.mLogger.info("output index is blank");
			String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
			RLOS.mLogger.info( "sQuery for document name is:" +  query);
			secondquery = formObject.getNGDataFromDataCache(query);
			for(int j = 0; j < secondquery.size(); j++) {
				if("Y".equalsIgnoreCase(secondquery.get(j).get(1))) {
					//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents"));
				}
			}
		}



		String Document_Name =outputindex.get(0).get(0);
		RLOS.mLogger.info( "Document_Index Document_Name is:" + Document_Name);
		String[] arrval=new String[outputindex.size()];
		if(outputindex != null && outputindex.size() != 0)
		{
			RLOS.mLogger.info("Staff List "+outputindex);
			for(int i = 0; i < outputindex.size(); i++)
			{
				arrval[i]=outputindex.get(i).get(0);
				//str.append(outputindex.get(i).get(0));
				//str.append(",");
			}
		}
		//RLOS.mLogger.info( " sMap is:" +  str.toString());
		//String arr=str.substring(0, str.length()-1);
		//RLOS.mLogger.info( " arr is:" +  arr);

		//String[] arrval = arr.split(",");
		for(int k=0;k<arrval.length;k++)
		{
			RLOS.mLogger.info( " arrval is:" +  arrval[k]);
		}


		RLOS.mLogger.info(,requested_product);
		String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
		outputindex = null;
		RLOS.mLogger.info( "sQuery for document name is:" +  query);
		outputindex = formObject.getNGDataFromDataCache(query);
		RLOS.mLogger.info( "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());
		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		RLOS.mLogger.info("repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
		String [] misseddoc=new String[outputindex.size()];
		for(int j = 0; j < outputindex.size(); j++)
		{
			String DocName =outputindex.get(j).get(0);
			String Mandatory =outputindex.get(j).get(1);
			RLOS.mLogger.info( "Document_Index Document_Name is:" + DocName+","+Mandatory);

			if (repObj.getRepeaterRowCount() != 0) {
				if(Mandatory.equalsIgnoreCase("Y")){

					int l=0;
					while(l<arrval.length)
					{
						RLOS.mLogger.info("DocName::"+DocName+":str:"+arrval[l]);

						if(arrval[l].equalsIgnoreCase(DocName))
						{
							RLOS.mLogger.info("document is present in the list");
							String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
							RLOS.mLogger.info("StatusValue::"+StatusValue);
							if(!StatusValue.equalsIgnoreCase("Recieved")){
								repObj.setValue(j, "cmplx_DocName_Status", "Recieved");
								repObj.setEditable(j, "cmplx_DocName_Status", false);
								RLOS.mLogger.info("StatusValue::123final"+StatusValue);

							}

							break;
						}
						else{
							RLOS.mLogger.info("Document is not present in the list");
							misseddoc[j]=DocName;
							l++;
							RLOS.mLogger.info( " misseddoc is in j is:" +  misseddoc[j]);

							String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
							RLOS.mLogger.info("StatusValue::"+StatusValue);
							String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
							RLOS.mLogger.info("Remarks::"+Remarks);
							if(!(StatusValue.equalsIgnoreCase("Recieved")||StatusValue.equalsIgnoreCase("Deferred"))){
								if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
									RLOS.mLogger.info("As you have not attached the Mandatory Document fill the Remarks");
									throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));

								}
								else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
									RLOS.mLogger.info("Proceed further");


								}
							}
						}
					}
				}
			}
		}
		StringBuilder mandatoryDocName = new StringBuilder("");
		for(int k=0;k<misseddoc.length;k++)
		{
			if(null != misseddoc[k]) {
				mandatoryDocName.append(misseddoc[k]).append(",");
			}
			RLOS.mLogger.info( "misseddoc is:" +misseddoc[k]);
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
		RLOS.mLogger.info( "misseddoc is:" +mandatoryDocName.toString());
		//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));

		//tanshu ended
		 */
	}

	public void submitFormCompleted(FormEvent pEvent)
			throws ValidatorException {
		//RLOS.mLogger.info( "Inside submitFormCompleted()" + pEvent);

	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		//RLOS.mLogger.info( "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		//RLOS.mLogger.info( "Inside initialize()");
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

