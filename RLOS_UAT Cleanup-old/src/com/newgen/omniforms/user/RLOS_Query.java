package com.newgen.omniforms.user;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import javax.faces.application.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;

@SuppressWarnings("serial")

public class RLOS_Query extends RLOSCommon implements FormListener
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	String ReqProd=null;
	//RLOS.mLogger.info("Inside initiation RLOS");
	public void formLoaded(FormEvent pEvent)
	{
		RLOS.mLogger.info("Inside Query RLOS");
		RLOS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
	}

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			//formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");	
			//formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			RLOS.mLogger.info("Inside Query RLOS");
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails", 0);
			RLOS.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			RLOS.mLogger.info( "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",currDate);
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Branch Initiation");
			formObject.setNGValue("Created_By",formObject.getUserName());

			// formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
			formObject.setNGValue("initiationChannel","Branch_Init");
			String init_Channel=formObject.getNGValue("initiationChannel");
			if("".equals(init_Channel))
				formObject.setNGValue("initiationChannel","Branch_Init");
			if(formObject.getNGValue("empType").contains("Salaried"))
				formObject.setSheetVisible("ParentTab",1, false);
			else if(formObject.getNGValue("empType").contains("Self Employed"))
				formObject.setSheetVisible("ParentTab",3, false);
			//added By Akshay-23/7/2017
			//new RLOSCommonCode().CustomerFragment_Load();
			/*formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
			*///ended By Akshay-23/7/2017

			if(formObject.getNGValue("Product_Type").contains("Personal Loan") && Integer.parseInt(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",110);
			}
			if("Credit Card".equals(formObject.getNGValue("PrimaryProduct")))
				formObject.setSheetVisible("ParentTab",7, true);

			RLOS.mLogger.info("Value Of Init Channel:"+init_Channel);
			formObject.setNGValue("Channel_Name","Branch Initiation");
			if(formObject.getNGValue("cmplx_Customer_FIrstNAme")==null)
			{
				formObject.setNGValue("cmplx_Customer_FIrstNAme","");
			}
			else if(formObject.getNGValue("cmplx_Customer_MiddleName")==null)
			{
				formObject.setNGValue("cmplx_Customer_MiddleName","");
			}

			else if( formObject.getNGValue("cmplx_Customer_LAstNAme")==null)
			{
				formObject.setNGValue("cmplx_Customer_LAstNAme","");
			}
			else
				formObject.setNGValue("Cust_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+formObject.getNGValue("cmplx_Customer_MiddleName")+formObject.getNGValue("cmplx_Customer_LAstNAme"));

		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception:"+e.getMessage());
			RLOS.logException(e);
		}
	}
	
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {		
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			switch (pEvent.getType()) 
			{		
			case FRAME_EXPANDED:
				new RLOSCommonCode().call_Frame_Expanded(pEvent);
				break;

			
			case TAB_CLICKED:
				//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded

				break;

			case FRAGMENT_LOADED:
				new RLOS_Initiation().call_Fragement_Loaded(pEvent);
				break;

			case VALUE_CHANGED:
				new RLOSCommonCode(). call_Value_Changed(pEvent);
				

				break;
			default:
				break;

			}
		}
		
	

	public void saveFormStarted(FormEvent pEvent) 
	{
		RLOS.mLogger.info( "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");
		
		formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
		formObject.setNGValue("priority_ProdGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,9));
		formObject.setNGValue("Subproduct_productGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2));


		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex ;
		outputindex = formObject.getNGDataFromDataCache(squery);
		RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);
	}

	public void saveFormCompleted(FormEvent pEvent) {
		RLOS.mLogger.info( "Inside saveFormCompleted()" + pEvent);


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");


		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex ;
		outputindex = formObject.getNGDataFromDataCache(squery);
		RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);


	}



	public void submitFormStarted(FormEvent pEvent)
	throws ValidatorException {
		RLOS.mLogger.info( "Inside submitFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		formObject.setNGValue("loan_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
		formObject.setNGValue("email_id", formObject.getUserName()+"@rlos.com");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex;
		List<List<String>> secondquery;
		outputindex = formObject.getNGDataFromDataCache(squery);
		RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);




		RLOS.mLogger.info( "Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		
		saveIndecisionGrid();
		TypeOfProduct();//Loan type
		//CIFIDCheck();  
		//tanshu started
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






		if(outputindex==null || !outputindex.isEmpty()) 
		{
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



		String Document_Name;
		if(outputindex!=null && !outputindex.isEmpty())
				Document_Name = outputindex.get(0).get(0);
		else
			Document_Name="";
		
		RLOS.mLogger.info( "Document_Index Document_Name is:" + Document_Name);
		String[] arrval=new String[outputindex.size()];
		if(outputindex != null && !outputindex.isEmpty())
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


		RLOS.mLogger.info(requested_product);
		String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
		outputindex = null;
		RLOS.mLogger.info( "sQuery for document name is:" +  query);
		outputindex = formObject.getNGDataFromDataCache(query);
		RLOS.mLogger.info( "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());
		IRepeater repObj;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		RLOS.mLogger.info("repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
		String [] misseddoc=new String[outputindex.size()];
		for(int j = 0; j < outputindex.size(); j++)
		{
			String DocName =outputindex.get(j).get(0);
			String Mandatory =outputindex.get(j).get(1);
			RLOS.mLogger.info( "Document_Index Document_Name is:" + DocName+","+Mandatory);

			if (repObj.getRepeaterRowCount() != 0 && "Y".equalsIgnoreCase(Mandatory)) 
			{
				int l=0;
				while(l<arrval.length)
				{
					RLOS.mLogger.info("DocName::"+DocName+":str:"+arrval[l]);

					if(arrval[l].equalsIgnoreCase(DocName))
					{
						RLOS.mLogger.info("document is present in the list");
						String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
						RLOS.mLogger.info("StatusValue::"+StatusValue);
						if(!"Recieved".equalsIgnoreCase(StatusValue))
						{
							repObj.setValue(j, "cmplx_DocName_Status", "Recieved");
							repObj.setEditable(j, "cmplx_DocName_Status", false);
							RLOS.mLogger.info("StatusValue::123final"+StatusValue);
						}

						break;
					}
					else
					{
						RLOS.mLogger.info("Document is not present in the list");
						misseddoc[j]=DocName;
						l++;
						RLOS.mLogger.info( " misseddoc is in j is:" +  misseddoc[j]);

						String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
						RLOS.mLogger.info("StatusValue::"+StatusValue);
						String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
						RLOS.mLogger.info("Remarks::"+Remarks);
						if(!("Recieved".equalsIgnoreCase(StatusValue)||"Deferred".equalsIgnoreCase(StatusValue))){
							if("".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){
								RLOS.mLogger.info("As you have not attached the Mandatory Document fill the Remarks");
								throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));

							}
							else if(!"".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){
								RLOS.mLogger.info("Proceed further");


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



	}


	public void submitFormCompleted(FormEvent pEvent)
	throws ValidatorException {
		RLOS.mLogger.info( "Inside submitFormCompleted()" + pEvent);

	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		RLOS.mLogger.info( "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		RLOS.mLogger.info( "Inside initialize()");
	}

	public String decrypt(String arg0) {
		
		return null;
	}

	public String encrypt(String arg0) {
		
		return null;
	}

}

