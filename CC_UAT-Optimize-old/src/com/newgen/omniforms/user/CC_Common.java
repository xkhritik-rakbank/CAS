/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CSM

File Name                                                         : CC_CSM

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Reference No/CR No   Change Date   Changed By    Change Description
1005				17-9-2017		Saurabh			changing chil node len to 1 from 2 to get missing tags values.	
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import com.newgen.custom.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.EventListenerHandler;






import com.newgen.omniforms.util.Common;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;


import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CC_Common extends Common{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String tabName = "Tab1";
	public void loadPicklistCustomer()  
	{
		CreditCard.mLogger.info( "Inside loadPicklistCustomer: ");
		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_Country order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		LoadPickList("cmplx_Customer_gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_gender with (nolock) order by code");
		LoadPickList("cmplx_Customer_CustomerCategory", "select '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CustomerCategory with (nolock) order by code");
	}
	public void loadPicklistEmployment(){  
		LoadPickList("cmplx_EmploymentDetails_NOB", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_NATUREOFBUSINESS with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_empmovemnt", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_EMPLOYERMOVEMENT with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_marketcode", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_MarketingCode  with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_categexpat", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_CATEGORYEXPAT with (nolock)");	
		LoadPickList("cmplx_EmploymentDetails_categnational", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_CATEGORYNATIONAL with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_categcards", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_CATEGORYCARDS with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_hotelrating", "select '--Select--' union select convert(varchar, Description) from NG_MASTER_HOTELRATING with (nolock)");

	}
	//added by yash 
	// ++ below code already present - 06-10-2017
	public void LoadPickList(String controlname, String query) {
		 CreditCard.mLogger.info( "Inside loadPicklist "); 
	      FormReference formObject = FormContext.getCurrentInstance()
	              .getFormReference();
	      CreditCard.mLogger.info( ":"+ query);
	   
	    List<String> control_Name=new ArrayList<String>();
	    control_Name.add(controlname);
	      formObject.getDataFromDataSource(query, control_Name);
	      CreditCard.mLogger.info( "Inside loadPicklist " + control_Name + ":"
	              + query);
	  }
	public void hide_sheet_employee()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist_Address: "); 
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Instant Money") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Salaried Credit Card")){
				CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
				makeSheetsInvisible(tabName, "1");

			}

			else 
			{
				//makeSheetsVisible(tabName, "3");
			}
		}
	}
	//12th sept
	public void LoadpicklistFCU()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist");
		String feedback=formObject.getNGValue("cmplx_DEC_FeedbackStatus");
		CreditCard.mLogger.info( "Inside loadPicklist::"+feedback);
		if(feedback.equalsIgnoreCase("Positive"))
		{
			formObject.setEnabled("cmplx_DEC_SubFeedbackStatus", true);
			formObject.setNGValue("cmplx_DEC_SubFeedbackStatus", "");
			LoadPickList("cmplx_DEC_SubFeedbackStatus", "select '--Select--' union select convert(varchar, Description) from NG_Master_Possubfeedbackstatus with (nolock)");
		}
		else if(feedback.equalsIgnoreCase("Negative"))
		{
			formObject.setEnabled("cmplx_DEC_SubFeedbackStatus", true);
			formObject.setNGValue("cmplx_DEC_SubFeedbackStatus", "");
			LoadPickList("cmplx_DEC_SubFeedbackStatus", "select '--Select--' union select convert(varchar, Description) from NG_Master_subfeedbackstatus with (nolock)");
		}
		else{
			formObject.setEnabled("cmplx_DEC_SubFeedbackStatus", false);
			formObject.setNGValue("cmplx_DEC_SubFeedbackStatus", " ");
		}
	}
	public void hide_sheet_company()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist_Address: "); 
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("BTC") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card")){
				CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
				makeSheetsInvisible(tabName, "3");

			}

			else 
			{
				//makeSheetsVisible(tabName, "3");
			}
		}
	}
	//ended by yash
// ++ below code already present - 06-10-2017
	public void employment_fields_hide()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist_Address: "); 
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6).equalsIgnoreCase("Salaried") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6).equalsIgnoreCase("Salaried Pensioner")){
				CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
				formObject.setVisible("EMploymentDetails_Label15", true);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
				formObject.setVisible("EMploymentDetails_Label32", true);
				formObject.setVisible("EMploymentDetails_Combo35", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("EMploymentDetails_Label36", true);
				formObject.setVisible("EMploymentDetails_Combo5", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("EMploymentDetails_Label33", false);
				//formObject.setVisible("cmplx_EmploymentDetails_channelcode", false); //Arun (12/10)
				formObject.setVisible("EMploymentDetails_Label37", false);
				formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", false);
				formObject.setVisible("EMploymentDetails_Label10", true);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", true);
				formObject.setVisible("cmplx_EmploymentDetails_MIS", false);
				formObject.setVisible("EMploymentDetails_Label38", false);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("cmplx_EmploymentDetails_StaffID", true);
				formObject.setVisible("EMploymentDetails_Label28", true);
				formObject.setEnabled("cmplx_EmploymentDetails_StaffID", false);
				formObject.setVisible("cmplx_EmploymentDetails_NepType", true);
				formObject.setVisible("EMploymentDetails_Label25", true);
				formObject.setEnabled("cmplx_EmploymentDetails_NepType", false);
				formObject.setVisible("cmplx_EmploymentDetails_Dept", true);
				formObject.setVisible("EMploymentDetails_Label5", true);
				formObject.setEnabled("cmplx_EmploymentDetails_Dept", false);
				formObject.setEnabled("cmplx_EmploymentDetails_tenancycntrctemirate", false);
				formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
				
				//formObject.setTop("EMploymentDetails_Label10",10);
				//formObject.setTop("cmplx_EmploymentDetails_marketcode",26);
				
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++

				formObject.setLeft("EMploymentDetails_Label10",794);
				formObject.setLeft("cmplx_EmploymentDetails_marketcode",794);
				formObject.setLeft("EMploymentDetails_Label4",24);
				formObject.setLeft("cmplx_EmploymentDetails_PromotionCode",24);
			}
			else 
				{
					//makeSheetsVisible(tabName, "3");
				}
			}
			}

	//added by yash on 21/8/2017
	
	
	public void employment_fields_IM()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist_Address: "); 
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("IM")){
				CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
				formObject.setVisible("EMploymentDetails_Label15", true);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
				formObject.setVisible("EMploymentDetails_Label32", true);
				formObject.setVisible("EMploymentDetails_Combo35", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("EMploymentDetails_Label36", true);
				formObject.setVisible("EMploymentDetails_Combo5", true);
				//++ Above Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("EMploymentDetails_Label33", false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode", false);
				formObject.setVisible("EMploymentDetails_Label37", false);
				formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", false);
				formObject.setVisible("EMploymentDetails_Label10", true);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", true);
				formObject.setVisible("cmplx_EmploymentDetails_MIS", false);
				formObject.setVisible("EMploymentDetails_Label38", false);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++
                formObject.setVisible("cmplx_EmploymentDetails_StaffID", true);
				formObject.setVisible("EMploymentDetails_Label28", true);
				formObject.setEnabled("cmplx_EmploymentDetails_StaffID", false);
				formObject.setVisible("cmplx_EmploymentDetails_NepType", true);
				formObject.setVisible("EMploymentDetails_Label25", true);
				formObject.setEnabled("cmplx_EmploymentDetails_NepType", true);
				formObject.setVisible("cmplx_EmploymentDetails_Dept", true);
				formObject.setVisible("EMploymentDetails_Label5", true);
				formObject.setEnabled("cmplx_EmploymentDetails_Dept", false);
				formObject.setEnabled("cmplx_EmploymentDetails_tenancycntrctemirate", true);
				formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
				    
				
				//formObject.setTop("EMploymentDetails_Label10",10);
				//formObject.setTop("cmplx_EmploymentDetails_marketcode",26);
				
				//++ Above Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++

				formObject.setLeft("EMploymentDetails_Label10",794);
				formObject.setLeft("cmplx_EmploymentDetails_marketcode",794);
				formObject.setLeft("EMploymentDetails_Label4",24);
				formObject.setLeft("cmplx_EmploymentDetails_PromotionCode",24);
			}
			else 
				{
					//makeSheetsVisible(tabName, "3");
				}
			}
			}
			
			// ++ above code already present - 06-10-2017
	//added by yash on 21/8/2017
	public void Eligibilityfields()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist_Address: "); 


		CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("SE") || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC")) || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed"))){
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label3", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label4", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTai", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label6", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label11", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label13", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label12", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate", false);
			formObject.setTop("ELigibiltyAndProductInfo_Save", 650);
			formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
			formObject.setLeft("ELigibiltyAndProductInfo_Label5", 16);

			CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));


		}
		else if((formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried Pensioner") ) && !formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM")){
			//++ Below Code added By Yash on Oct 14, 2017  to fix : 15-"Following fields will be enabled for the Credit analyst user " : Reported By Shashank on Oct 09, 2017++

			formObject.setVisible("ELigibiltyAndProductInfo_Label8", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label6", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label7", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestRate", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_EMI", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_Tenor", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_RepayFreq", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FirstRepayDate", true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label11", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", true);
			formObject.setEnabled("ELigibiltyAndProductInfo_Save", true);
			//++ Above Code added By Yash on Oct 14, 2017  to fix : 15-"Following fields will be enabled for the Credit analyst user " : Reported By Shashank on Oct 09, 2017++

			formObject.setVisible("ELigibiltyAndProductInfo_Label13", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label12", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate", false);
			formObject.setTop("ELigibiltyAndProductInfo_Save", 650);
			
			// ++ below code already present - 06-10-2017
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
				formObject.setVisible("EMploymentDetails_Label15", false);
				formObject.setVisible("EMploymentDetails_Label32", false);
				formObject.setVisible("EMploymentDetails_Combo35", false);
				formObject.setVisible("EMploymentDetails_Label15", true);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
				formObject.setVisible("EMploymentDetails_Label32", true);
				formObject.setVisible("EMploymentDetails_Combo35", true);
				//commmented by saurabh on 12th Oct.
				//formObject.setVisible("EMploymentDetails_Label36", false);
				formObject.setVisible("EMploymentDetails_Combo5", false);
				formObject.setVisible("EMploymentDetails_Label33", false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode", false);
				formObject.setVisible("EMploymentDetails_Label37", false);
				formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", false);
				formObject.setVisible("EMploymentDetails_Label10", true);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", true);
				formObject.setVisible("cmplx_EmploymentDetails_MIS", false);
				formObject.setVisible("EMploymentDetails_Label38", false);
				//commmented by saurabh on 12th Oct.
				//formObject.setTop("EMploymentDetails_Label10",10);
			//	formObject.setTop("cmplx_EmploymentDetails_marketcode",26);
				//formObject.setLeft("EMploymentDetails_Label10",794);
				//formObject.setLeft("cmplx_EmploymentDetails_marketcode",794);
				formObject.setLeft("EMploymentDetails_Label4",24);
				formObject.setLeft("cmplx_EmploymentDetails_PromotionCode",24);
				
				// ++ above code already present - 06-10-2017
			//formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
			//formObject.setLeft("ELigibiltyAndProductInfo_Label5", 16);
		}

	}
	// ended by yash on 23/8/2017

	//added by yash on 23/8/2017
	public void fieldsVisible()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist_Address: "); 
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("BTC"))){
				formObject.setVisible("cmplx_DEC_ContactPointVeri_cont", false);
				formObject.setVisible("cmplx_DEC_ContactPointVeri", false);
				formObject.setVisible("DecisionHistory_Label10", false);
				formObject.setVisible("cmplx_DEC_New_CIFID", false);
				formObject.setVisible("DecisionHistory_Label6", false);
				formObject.setVisible("cmplx_DEC_IBAN_No", false);
				formObject.setVisible("DecisionHistory_Label13", true);
				formObject.setVisible("cmplx_DEC_DeviationCode", true);
				formObject.setVisible("DecisionHistory_Label15", true);
				formObject.setVisible("cmplx_DEC_ScoreGrade", true);
				formObject.setVisible("DecisionHistory_Label14", true);
				formObject.setVisible("cmplx_DEC_DectechDecision", true);
				formObject.setVisible("DecisionHistory_Label5", false);
				formObject.setVisible("DecisionHistory_Label7", false);
				formObject.setVisible("cmplx_DEC_NewAccNo", false);
				formObject.setVisible("DecisionHistory_Label8", false);
				formObject.setVisible("cmplx_DEC_ChequebookRef", false);
				formObject.setVisible("DecisionHistory_Label9", false);
				formObject.setVisible("cmplx_DEC_DCR_Refno", false);
				//formObject.setTop("cmplx_DEC_DCR_Refno", 400);
				formObject.setTop("DecisionHistory_Label16", 288);
				formObject.setTop("cmplx_DEC_HighDeligatinAuth", 288);
				formObject.setLeft("DecisionHistory_Label13", 24);
				formObject.setLeft("cmplx_DEC_DeviationCode", 24);
				formObject.setLeft("DecisionHistory_Label14", 272);
				formObject.setLeft("cmplx_DEC_DectechDecision", 272);
				formObject.setLeft("DecisionHistory_Label15", 480);
				formObject.setLeft("cmplx_DEC_ScoreGrade", 480);
				formObject.setLeft("DecisionHistory_Label16", 700);
				formObject.setTop("cmplx_DEC_Gr_DecisonHistory", 450);
				formObject.setTop("DecisionHistory_save", 350);
				CreditCard.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6));


			}

			else 
			{
				//makeSheetsVisible(tabName, "3");
			}
		}
	}
	// ended by yash on 21/8/2017

	public void loadPicklistProduct(String ReqProd)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklistProduct$"+ReqProd+"$");
		String ProdType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);

		LoadPickList("Product_type", "select '--Select--' as type union select convert(varchar, Type) as type from NG_MASTER_TypeOfProduct with (nolock) order by type desc");
		LoadPickList("AppType", "select '--Select--','' as code union select convert(varchar, Description),Code as Description from ng_master_ApplicationType with (nolock) order by code");
		LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) as description from NG_MASTER_RequestedProduct with (nolock) where activityName='All'");
		
		if(ReqProd.equalsIgnoreCase("Personal Loan")){
			CreditCard.mLogger.info( "Inside equalsIgnoreCase()"); 
			formObject.setVisible("Schem", true);
			formObject.setLeft("Schem", 555);
			formObject.clear("subProd");
			formObject.setNGValue("subProd", "--Select--");
			formObject.clear("Schem");
			formObject.setNGValue("Schem", "--Select--");
			LoadPickList("subProd", "select '--Select--' union select convert(varchar(50),description) from ng_MASTER_SubProduct_PL with (nolock) where workstepName='Branch_Init'");
			LoadPickList("Schem", "select '--Select--' union select convert(varchar,SchemeDesc) from ng_MASTER_Scheme with (nolock) where SchemeDesc like 'P_%'");
			LoadPickList("EmpType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE order by code");
		}
		else if(ReqProd.equalsIgnoreCase("Credit Card")){
			formObject.setVisible("CardProd", true);
			formObject.clear("CardProd");
			formObject.clear("subProd");
			LoadPickList("subProd", "select  '--Select--' as description,'' as code union select convert(varchar,description),code from ng_master_Subproduct_CC with (nolock) order by code");
			// ++ below code already present - 06-10-2017
			if(ProdType.equalsIgnoreCase("Islamic"))
			LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");
			else
			LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code  NOT like '%AMAL%' order by code");
			// ++ above code already present - 06-10-2017
			LoadPickList("EmpType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE order by code");

			formObject.setVisible("Scheme", false);
		}
		else{
			formObject.clear("subProd");
			formObject.setNGValue("subProd", "--Select--");
		}
	}
	//++ Below Code added By Yash on Oct 14, 2017  to fix : "for loading the Mol nationality " : Reported By Shashank on Oct 09, 2017++
	public void loadPicklist_Mol()
	{
		 LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
	}
	//++ Above Code added By Yash on Oct 14, 2017  to fix : "for loading the Mol nationality " : Reported By Shashank on Oct 09, 2017++
	
	public void loadPicklist_Employment()
	{
		CreditCard.mLogger.info( "Inside loadpicklist4:"); 
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by Code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_Designation");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' union select convert(varchar, description) from ng_MASTER_EmploymentType");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmploymentStatus");
		//id changed by saurabh for emp categ PL on 10th oct.Mapping of field changed.
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_EmployerCategory_PL");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusPL", "select '--Select--' union select convert(varchar, description) from ng_master_EmployerStatusPL");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusCC", "select '--Select--' union select convert(varchar, description) from ng_master_EmployerStatusCC");
		//LoadPickList("cmplx_EmploymentDetails_FreezoneName", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmployerCategory_PL");
	}

	public void loadPicklist_Address()
	{
		//changed by akshay on 13/10/17
		CreditCard.mLogger.info( "Inside loadPicklist_Address: "); 
		LoadPickList("AddressDetails_addtype", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock) order by code");
		LoadPickList("AddressDetails_city", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
		LoadPickList("AddressDetails_state", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("AddressDetails_country", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by code");
	}
	public void loadPicklist_suppCard(){
		LoadPickList("SupplementCardDetails_cmplx_supplementGrid_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SupplementCardDetails_cmplx_supplementGrid_Gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
		LoadPickList("SupplementCardDetails_cmplx_supplementGrid_ResidentCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SupplementCardDetails_cmplx_supplementGrid_Relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");
	}

	public void loadPicklist_Fatca(){
		LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
	}

	public void loadPicklist_oecd(){
		LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("OECD_townBirth", "select '--Select--'as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock)");
		LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("OECD_CRSFlagReason", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_CRSReason with (nolock) order by code");
//added by akshay on 13/10/17
		
	}

	//added by yash for CC FSD
	public void loadPicklist3()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		String activityName = formObject.getWFActivityName();
		CreditCard.mLogger.info( "Inside loadpicklist3:");
		String Query = "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision where ProcessName='CreditCard' and workstepname='"+formObject.getWFActivityName()+"'";
		CreditCard.mLogger.info(Query );
		String query1="SELECT '--Select--' union select description FROM  ng_MASTER_RejectReasons";
		CreditCard.mLogger.info(query1);
		LoadPickList("cmplx_DEC_Decision_Reasoncode", query1);
		LoadPickList("cmplx_Decision_Decreasoncode", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_Master_ReferCredit with (nolock) order by code");			
		LoadPickList("cmplx_DEC_ReferReason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock) order by code");
		LoadPickList("cmplx_Decision_CADDecisiontray", "select '--Select--'  union select convert(varchar, Refer_Credit) from NG_Master_ReferCredit with (nolock)");
		if(activityName.equalsIgnoreCase("CAD_Analyst1")){
			LoadPickList("cmplx_DEC_Decision", Query);
		}
		else{
			LoadPickList("cmplx_DEC_Decision", Query);	
		}

	}

	public void loadPicklist_PartMatch()
	{
		CreditCard.mLogger.info( "Inside loadPicklist_PartMatch: ");
		LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}

	public void loadPicklistChecker()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 

		CreditCard.mLogger.info( "Inside loadpicklist3:");
		String Query = "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision where ProcessName='CreditCard' and workstepname='"+formObject.getWFActivityName()+"' and Initiation_Type NOT LIKE  '%Telesales_Init%'";
		CreditCard.mLogger.info(Query );
		LoadPickList("cmplx_DEC_Decision", Query);
	}
	//added by yash for CC FSD
	/*public void loadpicklist_company()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info( "Inside loadpicklist3:");
		LoadPickList("indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_IndustrySector with (nolock) order by code");
		LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
		LoadPickList("indusMAcro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMacro with (nolock) order by code");
		LoadPickList("indusMicro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMicro with (nolock) order by code");
		LoadPickList("legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_LegalEntity with (nolock) order by code");
		LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	}*/
	//ended by yash for CC FSD

	/*boolean setVisible_new(String visible_var)
	{
		SKLogger.writeLog("CC_Common"," inside setVisible()");
		try{

			String[] visible_arr = visible_var.split(",");
			for (int i = 0 ; i<visible_arr.length; i++){
				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			        formObject.setEnabled(visible_arr[i], false);
				}

				catch(Exception e){
					 SKLogger.writeLog("CC_Common", "Exception Occurred in setVisible field: "+ visible_arr[i]+" Exception" + e.getMessage());
					 return false;
				}

			}			
		}
		catch(Exception e){
			SKLogger.writeLog("CC_Common", "Exception Occurred in setVisible: " + e.getMessage());
			 return false;
		}
		 return true;
	}

	public void loadPicklist()
	{
		try{
		   LoadPickList("cmplx_CustomerDetails_MaritalStatus", "select '--Select--' union select convert(varchar, value) from samplePicklist");
		   }
		catch(Exception e)
		{
			SKLogger.writeLog("CC_Common", "Exception Occurred in loadPicklist: " + e.getMessage());
		}

    }


  public void loadInGrid(){
	try{
		SKLogger.writeLog("CC_Common", "Inside loadGrid: "); 
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	String Executexml="<ListItem><SubItem>sample</SubItem>"
						+"<SubItem>Akshay</SubItem>"
						+"<SubItem>Initiation</SubItem>"		
						+"<SubItem>Accept</SubItem>"
						+"<SubItem>ok good</SubItem></ListItem>";
	SKLogger.writeLog("CC_Common_new","Grid Data is"+Executexml);
	formObject.NGAddListItem("cmplx_Decision_RemarksHistory",Executexml);

	}


	catch(Exception e)
	{
		SKLogger.writeLog("CC_Common", "Exception Occurred in loadGrid: " + e.getMessage());
	}
 }

String fetchName(){
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	String s=formObject.getNGValue("myString");
	//SKLogger.writeLog("CC_Common", "Inside fetchName(): before loop s is " +s); 
	for (int i = 0; i < s.length(); i++) {

	    if (s.charAt(i) == '#') {
	    	SKLogger.writeLog("CC_Common", "Inside fetchName(): String s is "+s.substring(0,i)); 
	    	return s.substring(0,i);
	    }
	}
	return "ERROR";
  }

public boolean changeColor(){
	SKLogger.writeLog("CC_Common", "Inside changeColor() "); 
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	String s=formObject.getNGValue("myString");
	String[] s_arr1 = s.split("#");

	String[] s_arr2 = s_arr1[1].split(",");

	for (int i = 0 ; i<s_arr2.length; i++){
		try{
			SKLogger.writeLog("CC_Common", "Inside changeColor():s_arr2[] is "+s_arr2[i] ); 

	       formObject.setBackcolor(s_arr2[i], Color.green);

			}

		catch(Exception e){
			 SKLogger.writeLog("CC_Common", "Exception Occurred in changeColor field: "+ s_arr2[i]+" Exception" + e.getMessage());
			return false;
			}
		}
		return true;

  	}*/

	public void loadCustomerCombo()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadCustomerCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadCustomerCombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT CardNotAvail,NEP,IsGenuine,NTB, EmiratesID,LAstName,MobileNo,PassportNo,FirstName,MiddleName,mothersName,VisaNo,age,EmirateIdExpiry,IdIssueDate,PassportExpiry,VIsaExpiry,yearsINUAE,CustCateg,GCC_National,dob,CIFNO FROM ng_RLOS_Customer"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
		"cmplx_Customer_CARDNOTAVAIL;cmplx_Customer_NEP;cmplx_Customer_IsGenuine;cmplx_Customer_NTB;cmplx_Customer_EmiratesID;cmplx_Customer_LastNAme;cmplx_Customer_MobileNo;cmplx_Customer_PAssportNo;cmplx_Customer_FirstNAme;cmplx_Customer_MiddleNAme;cmplx_Customer_MotherName;cmplx_Customer_VisaNo;cmplx_Customer_Age;cmplx_Customer_EmirateIDExpiry;cmplx_Customer_IdIssueDate;cmplx_Customer_PassPortExpiry;cmplx_Customer_VisaExpiry;cmplx_Customer_yearsInUAE;cmplx_Customer_CustomerCategory;cmplx_Customer_GCCNational,cmplx_Customer_DOb,cmplx_Customer_CIFNo");

		formObject.getNGDataFromDataCache("Select Title FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_Title");
		// Aman Change to get the code
		// formObject.getNGDataFromDataCache("Select gender FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_gender");
		formObject.getNGDataFromDataCache("Select EmirateVisa FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_EMirateOfVisa");
		formObject.getNGDataFromDataCache("Select ResidentNonResident FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_RESIDENTNONRESIDENT");       
		formObject.getNGDataFromDataCache("Select SecNationalityApplicable FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_SecNAtionApplicable");
		formObject.getNGDataFromDataCache("Select SecNationality FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_SecNationality");
		formObject.getNGDataFromDataCache("Select CountryOfResidence FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_COUNTRYOFRESIDENCE");
		formObject.getNGDataFromDataCache("Select emirateOfResidence FROM ng_RLOS_Customer "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_Customer_EmirateOfResidence");


	}

	public void loadProductCombo()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadProductCombo()");
		try 
		{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			CreditCard.mLogger.info("Inside PLCommon ->loadFragment2Combo() ParentWI_NAme is "+ParentWI_Name);
			String query="Select 	prodType,ReqProduct,SubProduct,ReqLimit,ApplicantType, CardProduct,EmpType,ReqTenor,Schem,Priority,RequestType,NewLimitValue,LimitExpDate  from ng_RLOS_GR_Product where 	prod_WIname ='"+ ParentWI_Name+"'" ;    
			CreditCard.mLogger.info(query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			CreditCard.mLogger.info("Inside PLCommon ->loadProductCombo()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4)+list.get(0).get(5));
			for (List<String> a : list) 
			{
				CreditCard.mLogger.info("For LOOP:  "+a.get(0)+a.get(1)+a.get(2)+a.get(3)+a.get(4)+a.get(5));
				formObject.addItem("cmplx_Product_cmplx_ProductGrid", a);
			}
		}catch(Exception e){  CreditCard.mLogger.info("Exception Occurred:"+e.getMessage());}	
		//CreditCard.mLogger.info("Inside PLCommon ->loadProductCombo(): New WI_name :"+list.get(0).get(6));

	}


	public void loadEmpDetailsCombo()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadEmpDetailsCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadEmpDetailsCombo() ParentWI_NAme is "+ParentWI_Name);
		String QUERY="SELECT EmpName,EmpCode,freezoneName,designation,ConfirmedInJob,DOJ,DOLPrev,DOJPrev,LOS,LOSPrevious,Freezone,Kompass,VisaSponser,IncludedPL,IncludedCC FROM ng_RLOS_EmpDetails"
			+ " WHERE  wi_name='"+ ParentWI_Name+"'";
		CreditCard.mLogger.info("Inside PLCommon ->loadEmpDetailsCombo() Query is "+QUERY);

		formObject.getNGDataFromDataCache(QUERY,
		"cmplx_EmploymentDetails_EmpName;cmplx_EmploymentDetails_EMpCode;cmplx_EmploymentDetails_FreezoneName;cmplx_EmploymentDetails_Designation;cmplx_EmploymentDetails_JobConfirmed;cmplx_EmploymentDetails_DOJ;cmplx_EmploymentDetails_DOLPrev;cmplx_EmploymentDetails_DOJPrev;cmplx_EmploymentDetails_LOS;cmplx_EmploymentDetails_LOSPrevious;cmplx_EmploymentDetails_Freezone;cmplx_EmploymentDetails_Kompass;cmplx_EmploymentDetails_VisaSponser;cmplx_EmploymentDetails_IncInPL;cmplx_EmploymentDetails_IncInCC");
		formObject.getNGDataFromDataCache("Select NepType FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_NepType");
		formObject.getNGDataFromDataCache("Select AppCateg FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_ApplicationCateg");
		formObject.getNGDataFromDataCache("Select TargetSegCode FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_targetSegCode");
		formObject.getNGDataFromDataCache("Select empContractType FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmpContractType");       
		formObject.getNGDataFromDataCache("Select EMpstatus FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmpStatus");
		formObject.getNGDataFromDataCache("Select EmpType FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_Emp_Type");
		formObject.getNGDataFromDataCache("Select CurrEmployer FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_CurrEmployer");
		formObject.getNGDataFromDataCache("Select IndusSeg FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_IndusSeg");
		formObject.getNGDataFromDataCache("Select emirateOfWork FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmirateOfWork");
		formObject.getNGDataFromDataCache("Select headOfficeEmirate FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_HeadOfficeEmirate");
		formObject.getNGDataFromDataCache("Select EmpIndustry FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_EmpIndusSector");
		formObject.getNGDataFromDataCache("Select Emply_Industry_Macro FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_Emply_Industry_Macro");
		formObject.getNGDataFromDataCache("Select Emply_Industry_Micro FROM ng_RLOS_EmpDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EmploymentDetails_Emply_Industry_Micro");

	}

// ++ below code not commented at offshore - 06-10-2017
	public void loadIncomeDetailsCombo()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadIncomeDetailsCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadIncomeDetailsCombo() ParentWI_NAme is "+ParentWI_Name);
		String query=" SELECT basic,housing,transport,costOfliving,fixedOT,other,grossSal,overtime_Month1,overtime_Month2,overtime_Month3,overtime_avg,commission_Month1,commission_Month2,commission_Month3,commission_avg,foodAllow_Month1,foodAllow_Month2,foodAllow_Month3,foodAllow_avg,phoneAllow_Month1,phoneAllow_Month2,phoneAllow_Month3,phoneAllow_avg,serviceAllow_Month1,serviceAllow_Month2,serviceAllow_Month3,serviceAllow_avg,bonus_month1,bonus_month2,bonus_month3,bonus_avg,other_Month1,other_Month2,other_Month3,Other_avg,flying_Month1,flying_Month2,flying_Month3,flying_avg,totalSal,netSal1,netSal2,netSal3,avgNetSal,SalaryDay,SalaryXferToBank,DurationOfBanking,NoOfMonthsRakbankStat,AvgBal,CredTurnover,AvgCredTurnover,AnnualRent,bankStatFrom,bankStatFromDate,bankStatToDate,DurationOfBanking2,NoOfMonthsRakbankStat2,NoOfMonthsOtherkbankStat2,Statement_Cycle FROM ng_RLOS_IncomeDetails"
			+ " WHERE  wi_name='"+ ParentWI_Name+"' ";
		formObject.getNGDataFromDataCache(query,
		"cmplx_IncomeDetails_Basic;cmplx_IncomeDetails_housing;cmplx_IncomeDetails_transport;cmplx_IncomeDetails_CostOfLiving;cmplx_IncomeDetails_FixedOT;cmplx_IncomeDetails_Other;cmplx_IncomeDetails_grossSal;cmplx_IncomeDetails_Overtime_Month1;cmplx_IncomeDetails_Overtime_Month2;cmplx_IncomeDetails_Overtime_Month3;cmplx_IncomeDetails_Overtime_Avg;cmplx_IncomeDetails_Commission_Month1;cmplx_IncomeDetails_Commission_Month2;cmplx_IncomeDetails_Commission_Month3;cmplx_IncomeDetails_Commission_Avg;cmplx_IncomeDetails_FoodAllow_Month1;cmplx_IncomeDetails_FoodAllow_Month2;cmplx_IncomeDetails_FoodAllow_Month3;cmplx_IncomeDetails_FoodAllow_Avg;cmplx_IncomeDetails_PhoneAllow_Month1;cmplx_IncomeDetails_PhoneAllow_Month2;cmplx_IncomeDetails_PhoneAllow_Month3;cmplx_IncomeDetails_PhoneAllow_Avg;cmplx_IncomeDetails_serviceAllow_Month1;cmplx_IncomeDetails_serviceAllow_Month2;cmplx_IncomeDetails_serviceAllow_Month3;cmplx_IncomeDetails_serviceAllow_Avg;cmplx_IncomeDetails_Bonus_Month1;cmplx_IncomeDetails_Bonus_Month2;cmplx_IncomeDetails_Bonus_Month3;cmplx_IncomeDetails_Bonus_Avg;cmplx_IncomeDetails_Other_Month1;cmplx_IncomeDetails_Other_Month2;cmplx_IncomeDetails_Other_Month3;cmplx_IncomeDetails_Other_Avg;cmplx_IncomeDetails_Flying_Month1;cmplx_IncomeDetails_Flying_Month2;cmplx_IncomeDetails_Flying_Month3;cmplx_IncomeDetails_Flying_Avg;cmplx_IncomeDetails_TotSal;cmplx_IncomeDetails_netSal1;cmplx_IncomeDetails_netSal2;cmplx_IncomeDetails_netSal3;cmplx_IncomeDetails_AvgNetSal;cmplx_IncomeDetails_SalaryDay;cmplx_IncomeDetails_SalaryXferToBank;cmplx_IncomeDetails_DurationOfBanking;cmplx_IncomeDetails_NoOfMonthsRakbankStat;cmplx_IncomeDetails_AvgBal;cmplx_IncomeDetails_CredTurnover;cmplx_IncomeDetails_AvgCredTurnover;cmplx_IncomeDetails_AnnualRent;cmplx_IncomeDetails_BankStatFrom;cmplx_IncomeDetails_BankStatFromDate;cmplx_IncomeDetails_BankStatToDate;cmplx_IncomeDetails_DurationOfBanking2;cmplx_IncomeDetails_NoOfMonthsRakbankStat2;cmplx_IncomeDetails_NoOfMonthsOtherbankStat2;cmplx_IncomeDetails_statement_cycle");
		formObject.getNGDataFromDataCache("Select Accomodation FROM ng_RLOS_IncomeDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_Accomodation");
		formObject.getNGDataFromDataCache("Select AvgBalFreq FROM ng_RLOS_IncomeDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_AvgBalFreq");
		formObject.getNGDataFromDataCache("Select CreditTurnoverFreq FROM ng_RLOS_IncomeDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_CreditTurnoverFreq");
		formObject.getNGDataFromDataCache("Select AnnualRentFreq FROM ng_RLOS_IncomeDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_IncomeDetails_AnnualRentFreq");       
	}

	public void loadMiscFieldsCombo() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadMiscFieldsCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadMiscFieldsCombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT School,RealEstate  FROM ng_rlos_MiscFields"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
				"cmplx_MiscFields_School;cmplx_MiscFields_RealEstate"
				+ ";");
		formObject.getNGDataFromDataCache("Select propertyType FROM ng_rlos_MiscFields "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_MiscFields_PropertyType");
	}

	public void loadLiabilityNewCombo() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadLiabilityNewCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadLiabilityNewCombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT DBR,TAI,DBRNet,aecbconsent  FROM ng_RLOS_Liability_New"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
				"cmplx_ExternalLiabilities_DBR;cmplx_ExternalLiabilities_TAI;cmplx_ExternalLiabilities_DBRNetSalary;cmplx_ExternalLiabilities_AECBConsent"
				+ ";");

	}

	public void loadEligAndProductInfoCombo() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadEligAndProductInfoCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadEligAndProductInfoCombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT FinalDBR,FinalLimit,InterestRate,EMI,Tenor,Netpayout,ISCalculatereeligibility,Moratorium,FirstRepaymentDate,MarginRate,BaseRate,ProdprefRate,NetRate,MaturityDate,AgeAtMaturity,NumberOfInstallment,LPF,LPFAmt,Insurance,InsuranceAmt FROM ng_rlos_EligAndProdInfo"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
				"cmplx_EligibilityAndProductInfo_FinalDBR;cmplx_EligibilityAndProductInfo_FinalLimit;cmplx_EligibilityAndProductInfo_InterestRate;cmplx_EligibilityAndProductInfo_EMI;cmplx_EligibilityAndProductInfo_Tenor;cmplx_EligibilityAndProductInfo_NetPayout;cmplx_EligibilityAndProductInfo_ISReeligibility;cmplx_EligibilityAndProductInfo_Moratorium;cmplx_EligibilityAndProductInfo_FirstRepayDate;cmplx_EligibilityAndProductInfo_MArginRate;cmplx_EligibilityAndProductInfo_BAseRate;cmplx_EligibilityAndProductInfo_ProdPrefRate;cmplx_EligibilityAndProductInfo_NetRate;cmplx_EligibilityAndProductInfo_MaturityDate;cmplx_EligibilityAndProductInfo_AgeAtMaturity;cmplx_EligibilityAndProductInfo_NoOfInstallments;cmplx_EligibilityAndProductInfo_LPF;cmplx_EligibilityAndProductInfo_LPFAmount;cmplx_EligibilityAndProductInfo_Insurance;cmplx_EligibilityAndProductInfo_InsuranceAmount"
				+ ";");
		formObject.getNGDataFromDataCache("Select InstrumentType FROM ng_rlos_EligAndProdInfo "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_InstrumentType");
		formObject.getNGDataFromDataCache("Select RepaymentFreq FROM ng_rlos_EligAndProdInfo "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_RepayFreq");
		formObject.getNGDataFromDataCache("Select BaseRateType FROM ng_rlos_EligAndProdInfo "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_BaseRateType");
		formObject.getNGDataFromDataCache("Select InterestType FROM ng_rlos_EligAndProdInfo "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_EligibilityAndProductInfo_InterestType");       
	}

	public void loadAddressDetails()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadAddressDetails()");
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			CreditCard.mLogger.info("Inside PLCommon ->loadAddressDetails() ParentWI_NAme is "+ParentWI_Name);
			String query="Select 	addressType,houseNo,buildingName,StreetNAme,landmark, city,state,country,po_box,yearsinCurrAddr,addr_wi_name  from ng_RLOS_gr_AddressDetails where 	addr_wi_name ='"+ ParentWI_Name+"' or addr_wi_name='"+formObject.getWFWorkitemName()+"'" ;    
			CreditCard.mLogger.info(query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			CreditCard.mLogger.info("Inside PLCommon ->loadAddressDetails()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4)+list.get(0).get(5));
			for (List<String> a : list) 
			{
				CreditCard.mLogger.info("For LOOP:  "+a.get(0)+a.get(1)+a.get(2)+a.get(3)+a.get(4)+a.get(5));
				formObject.addItem("cmplx_AddressDetails_cmplx_AddressGrid", a);
			}

		}catch(Exception e){  CreditCard.mLogger.info("Exception Occurred:"+e.getMessage());}	
		//CreditCard.mLogger.info("Inside PLCommon ->loadProductCombo(): New WI_name :"+list.get(0).get(6));

	}

	public void loadAltContactDetailsCombo() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadAltContactDetailsCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadAltContactDetailsCombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT MobileNo_pri,MobileNo_sec,HomeCountryNo,OfficeNo,ResidenceNo,Email1_pri,Email2_sec,RetainAccIfLoanReq FROM ng_RLOS_AltContactDetails"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
				"AlternateContactDetails_MobileNo1;AlternateContactDetails_MobileNo2;AlternateContactDetails_HOMECOUNTRYNO;AlternateContactDetails_OFFICENO;AlternateContactDetails_RESIDENCENO;AlternateContactDetails_EMAIL1_PRI;AlternateContactDetails_EMAIL2_SEC;AlternateContactDetails_RetainAccIfLoanReq"
				+ ";");
		formObject.getNGDataFromDataCache("Select estatementflag FROM ng_RLOS_AltContactDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","AlternateContactDetails_ESTATEMENTFLAG");
		formObject.getNGDataFromDataCache("Select CustomerDomicileBranch FROM ng_RLOS_AltContactDetails "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","AlternateContactDetails_CustomerDomicileBranch");

	}

	public void loadFATCACombo() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadFATCACombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadFATCACombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT USRelation,TINNo,SignedDate,ExpiryDate FROM ng_rlos_FATCA"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
				"cmplx_FATCA_USRelation;cmplx_FATCA_TINNo;cmplx_FATCA_SignedDate;cmplx_FATCA_ExpiryDate"
				+ ";");
		formObject.getNGDataFromDataCache("Select TypeOFRelation FROM ng_rlos_FATCA "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_TypeOFRelation");
		formObject.getNGDataFromDataCache("Select DocsAvail FROM ng_rlos_FATCA "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_DocsAvail");
		formObject.getNGDataFromDataCache("Select DocsCollected FROM ng_rlos_FATCA "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_DocsCollec");
		formObject.getNGDataFromDataCache("Select COntrollingPersonHasUS FROM ng_rlos_FATCA "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_ControllingPersonUSRel");
		formObject.getNGDataFromDataCache("Select Category FROM ng_rlos_FATCA "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_FATCA_Category");
	}

	public void loadKYCCombo() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadKYCCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadKYCCombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT PEP  FROM ng_rlos_KYC"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
				"cmplx_KYC_PEP"
				+ ";");
		formObject.getNGDataFromDataCache("Select KYCHeld FROM ng_rlos_KYC "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_KYC_KYCHeld");


	}
	public void loadOECDCombo() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->loadOECDCombo()");
		String ParentWI_Name = formObject.getNGValue("Parent_WIName");
		CreditCard.mLogger.info("Inside PLCommon ->loadOECDCombo() ParentWI_NAme is "+ParentWI_Name);
		formObject.getNGDataFromDataCache(
				"SELECT CRSundocumentedFlag,CRSundocumentedFlagReason  FROM ng_rlos_OECD"
				+ " WHERE  wi_name='"+ ParentWI_Name+"' ",
				"cmplx_OECD_CRS_Flag;cmplx_OECD_CRSFlagReason"
				+ ";");
		formObject.getNGDataFromDataCache("Select city FROM ng_rlos_OECD "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_Town");
		formObject.getNGDataFromDataCache("Select CountryTaxResidence FROM ng_rlos_OECD "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_CountryOfTaxResd");
		formObject.getNGDataFromDataCache("Select country FROM ng_rlos_OECD "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_Country");
		formObject.getNGDataFromDataCache("Select TaxpayerID FROM ng_rlos_OECD "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_taxpayerID");
		formObject.getNGDataFromDataCache("Select NoTINReason FROM ng_rlos_OECD "+ " WHERE  wi_name='"+ ParentWI_Name+"' ","cmplx_OECD_NoTINReason");
	}


	/* public void loadInDecGrid()
    {

        FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
        SKLogger.writeLog("CreditCard> ","Inside CCCommon ->loadInDecGrid()");
         String ParentWI_Name = formObject.getNGValue("Parent_WIName");
        String query="select dateLastChanged,userName,workstepName,Decisiom,remarks from NG_RLOS_GR_DECISION where wi_name='"+ParentWI_Name+"' or wi_nAme='"+formObject.getWFWorkitemName()+"'";
         List<List<String>> list=formObject.getDataFromDataSource(query);
        SKLogger.writeLog("CreditCard> ","Inside CCCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
        for (List<String> a : list) 
        {

            formObject.addItemFromList("Decision_ListView1", a);
        }

    }
	 */
	public void Decision_cadanalyst1(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		List<String> controlList_visible= Arrays.asList("DecisionHistory_Label5","DecisionHistory_Text5","DecisionHistory_Label9",
				"DecisionHistory_Combo4","DecisionHistory_Text6","DecisionHistory_Label4","DecisionHistory_Combo5","DecisionHistory_Label16",
				"cmplx_DEC_HighDeligatinAuth","DecisionHistory_Button4","DecisionHistory_Label18","cmplx_DEC_ReferTo","DecisionHistory_Label13",
				"cmplx_DEC_DeviationCode","DecisionHistory_Label14","cmplx_DEC_DectechDecision","DecisionHistory_Label15","cmplx_DEC_ScoreGrade",
				"DecisionHistory_calReElig","DecisionHistory_ManualDevReason","cmplx_DEC_Manual_Deviation","DecisionHistory_ManualDevReason");

		List<String> controlList_hidden= Arrays.asList("cmplx_DEC_ContactPointVeri","DecisionHistory_chqbook","DecisionHistory_Label6",
				"cmplx_DEC_IBAN_No","DecisionHistory_Label7","cmplx_DEC_NewAccNo","DecisionHistory_Label8","cmplx_DEC_ChequebookRef",
				"DecisionHistory_Label9","cmplx_DEC_DCR_Refno","DecisionHistory_Label5","cmplx_DEC_Description","DecisionHistory_Label27","cmplx_DEC_Cust_Contacted","DecisionHistory_Label10","cmplx_DEC_New_CIFID","DecisionHistory_Combo5");

		List<String> controlList_setLeft= Arrays.asList("DecisionHistory_Button4","DecisionHistory_Decision_Label1","cmplx_DEC_VerificationRequired",
				"DecisionHistory_Label3","cmplx_DEC_Strength","DecisionHistory_Label4",
				"cmplx_DEC_Weakness","DecisionHistory_Decision_Label4","cmplx_DEC_Remarks","DecisionHistory_Label13","cmplx_DEC_DeviationCode",
				"DecisionHistory_Label14","cmplx_DEC_DectechDecision","DecisionHistory_Label15","cmplx_DEC_ScoreGrade","DecisionHistory_Decision_Label3",
				"cmplx_DEC_Decision","DecisionHistory_Label1","cmplx_DEC_ReferReason","DecisionHistory_Label16","cmplx_DEC_HighDeligatinAuth",
				"DecisionHistory_calReElig","cmplx_DEC_Manual_Deviation","DecisionHistory_ManualDevReason","DecisionHistory_Label18","cmplx_DEC_ReferTo");

		List<String> controlList_setTop= Arrays.asList("DecisionHistory_Button4","DecisionHistory_Decision_Label1","cmplx_DEC_VerificationRequired",
				"DecisionHistory_Label3","cmplx_DEC_Strength","DecisionHistory_Label4","cmplx_DEC_Weakness","DecisionHistory_Decision_Label4",
				"cmplx_DEC_Remarks","DecisionHistory_Label13","cmplx_DEC_DeviationCode","DecisionHistory_Label14","cmplx_DEC_DectechDecision",
				"DecisionHistory_Label15","cmplx_DEC_ScoreGrade","DecisionHistory_Decision_Label3","cmplx_DEC_Decision",
				"DecisionHistory_Label1","cmplx_DEC_ReferReason","DecisionHistory_Label16","cmplx_DEC_HighDeligatinAuth","DecisionHistory_calReElig",
				"cmplx_DEC_Manual_Deviation","DecisionHistory_ManualDevReason","DecisionHistory_Label18","cmplx_DEC_ReferTo","DecisionHistory_Decision_ListView1","DecisionHistory_save");

		List<String> setLeft_Values= Arrays.asList("590","305","305",
				"24","24","305","305","590",
				"590","24","24","305","305",
				"590","590","24","24","305",
				"305","590","590","784",
				"24","304","590","590");

		List<String> setTop_Values= Arrays.asList("8","8","24",
				"56","72","56","72","56","72","128",
				"145","128","145","128","145",
				"168","184","168","184","168",
				"184","170","216","216",
				"210","226","260","430");

		setVisibleTrue(controlList_visible);
		setVisibleFalse(controlList_hidden);
		setTop(controlList_setTop,setTop_Values);
		setLeft(controlList_setLeft,setLeft_Values);
		formObject.setHeight("DecisionHistory_Frame1", 460);	
		formObject.setHeight("DecisionHistory", 480);	   
		formObject.setLocked("cmplx_DEC_DectechDecision",true);
		formObject.setLocked("cmplx_DEC_HighDeligatinAuth",true);
		formObject.setLocked("cmplx_DEC_ReferTo",true);
		formObject.setLocked("cmplx_DEC_DeviationCode",true);
		formObject.setLocked("DecisionHistory_ManualDevReason",true);
		formObject.setLocked("cmplx_DEC_VerificationRequired",true);
	
	/*	//Code Change By aman to save Highest Delegation Auth
		String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
																													  
																							  
		if(!OutputXML.isEmpty()){
			String HighDel=OutputXML.get(0).get(0);
			if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !OutputXML.get(0).get(0).equalsIgnoreCase("null") ){
				{
					
					formObject.setNGValue("cmplx_DEC_HighDeligatinAuth", HighDel);
				}
			}
		}	
		//Code Change By aman to save Highest Delegation Auth
*/
		//ended by Akshay on 5/9/17
		loadPicklist3();

		LoadPickList("cmplx_DEC_ReferTo", "select '--Select--' union select convert(varchar, Refer_Credit) from ng_master_ReferCredit");
		
				
		
		
		if(formObject.getNGValue("cmplx_DEC_Manual_Deviation").equalsIgnoreCase("true")){
			formObject.setLocked("DecisionHistory_ManualDevReason",false);
			formObject.setEnabled("cmplx_DEC_ReferTo", false);	
		}
		else{
			formObject.setLocked("DecisionHistory_ManualDevReason",true);
			formObject.setEnabled("cmplx_DEC_ReferTo", true);	
		}


	}


	public void setDisabled()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		CreditCard.mLogger.info("Inside PLCommon ->setDisabled()");
		String fields="cmplx_Customer_FirstNAme,cmplx_Customer_MiddleNAme,cmplx_Customer_LastNAme,cmplx_Customer_Age,cmplx_Customer_DOb,cmplx_Customer_gender,cmplx_Customer_EmiratesID,cmplx_Customer_IdIssueDate,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_MotherName,cmplx_Customer_PAssportNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VisaNo,cmplx_Customer_VisaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_MobileNo,cmplx_Customer_CIFNo";
		String array[]=fields.split(",");
		for(int i=0;array[i]!=null;i++)
		{
			CreditCard.mLogger.info("Inside PLCommon ->setDisabled()"+array[i]);

			formObject.setEnabled(array[i], false);

		}
	}

	/* public void saveIndecisionGrid(){

        SKLogger.writeLog("CC_Common", "Inside saveIndecisionGrid: "); 
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
           String query="insert into NG_RLOS_GR_DECISION values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DEC_Decision")+"','"+formObject.getNGValue("DecisionHistory_Text1")+"','"+formObject.getWFWorkitemName()+"')";

        SKLogger.writeLog("CC_Common","Query is"+query);
        formObject.saveDataIntoDataSource(query);

        }  */

	public void setVisibleTrue(List<String> mylist){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside setVisibleTrue(): "+mylist);

		for(String control_name:mylist)
		{
			formObject.setVisible(control_name, true);
		}
	}

	public void setVisibleFalse(List<String> mylist){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside setVisibleFalse(): "+mylist);

		for(String control_name:mylist)
		{
			formObject.setVisible(control_name, false);
		}
	}

	public void setTop(List<String> mylist,List<String> values){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside setTop(): "+mylist);

		for(int i=0;i<mylist.size();i++)
		{
			formObject.setTop(mylist.get(i), Integer.parseInt(values.get(i)));
		}
	}

	public void setLeft(List<String> mylist,List<String> values){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside setLeft(): "+mylist);

		for(int i=0;i<mylist.size();i++)
		{
			formObject.setLeft(mylist.get(i), Integer.parseInt(values.get(i)));
		}
	}

	public void fetchfinacleTORepeater(){
		//SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame11");
		List<String> repeaterHeaders = new ArrayList<String>();
		repeaterHeaders.add("Day");
		repeaterHeaders.add("January");
		repeaterHeaders.add("February");
		repeaterHeaders.add("March");
		repeaterHeaders.add("April");
		repeaterHeaders.add("May");
		repeaterHeaders.add("June");
		repeaterHeaders.add("July");
		repeaterHeaders.add("August");
		repeaterHeaders.add("September");
		repeaterHeaders.add("October");
		repeaterHeaders.add("November");
		repeaterHeaders.add("December");

		// 23 sept change
		repeaterHeaders.add("Workitem");
		repObj.setRepeaterHeaders(repeaterHeaders);
		if(!(repObj.getRepeaterRowCount() > 0)){
			
			
			CreditCard.mLogger.info("after making headers");
			FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");
			List<List<String>> Day = null;
			String documentName = null;

			int repRowCount = 0;
			CreditCard.mLogger.info(""+repObj.toString());
			CreditCard.mLogger.info(""+ Day);
			try{
				for(int i=0;i<31;i++ ){
					repObj.addRow();
					//documentName = Day.get(i).get(0);
					CreditCard.mLogger.info(" "+ documentName);
					//repObj.setValue(i, 0, documentName);
					repRowCount = repObj.getRepeaterRowCount();
					repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_DAY1", ""+(i+1));
					// change 23 sept
					repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_wi_name_turn", formObject.getWFWorkitemName());
					CreditCard.mLogger.info( " " + repRowCount);

				}
				repObj.addRow();
				repObj.setValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_DAY1", "Total");
				// added by abhishek after adding hiddden workitem in repeater
				repObj.setValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_wi_name_turn", formObject.getWFWorkitemName());
				repObj.setRowDisabled(31, true);
				repObj.setColumnDisabled(0, true);
				// enable for last 6 months
				ChangeRepeaterTrnover(repObj);
			}
			catch (Exception e) {
				CreditCard.mLogger.info( " " + e.toString());
			} finally {
				repObj = null;
				repeaterHeaders = null;         
			}
		}
		else{
			repObj.setRowDisabled(31, true);
			repObj.setColumnDisabled(0, true);
			// enable for last 6 months
			ChangeRepeaterTrnover(repObj);
		}
	}

	public void fetchfinacleAvgRepeater()
	{
																													  
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame9");
		List<String> repeaterHeaders = new ArrayList<String>();
		repeaterHeaders.add("Account");
		repeaterHeaders.add("January");
		repeaterHeaders.add("February");
		repeaterHeaders.add("March");
		repeaterHeaders.add("April");
		repeaterHeaders.add("May");
		repeaterHeaders.add("June");
		repeaterHeaders.add("July");
		repeaterHeaders.add("August");
		repeaterHeaders.add("September");
		repeaterHeaders.add("October");
		repeaterHeaders.add("November");
		repeaterHeaders.add("December");
		repeaterHeaders.add("Consider for Eligibility");
		// added by abhishek after adding hiddden workitem in repeater
		repeaterHeaders.add("workitem");
		repObj.setRepeaterHeaders(repeaterHeaders);
		if(!(repObj.getRepeaterRowCount() > 0)){
			
											   
			CreditCard.mLogger.info("after making headers");
			FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");
			int repRowCount = 0;
			CreditCard.mLogger.info(""+repObj.toString());
			try{
				for(int i=0;i<8;i++ ){
					repObj.addRow();
					repRowCount = repObj.getRepeaterRowCount();
					CreditCard.mLogger.info( " " + repRowCount);
				}
				/* repObj.setValue(0, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", ""); 
				 repObj.setValue(1, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", "");
				 repObj.setValue(2, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", "");
				 repObj.setValue(3, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", "");
				 repObj.setValue(4, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", "");
				 repObj.setValue(5, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", "");*/
				repObj.setValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", "Monthly Total");
				repObj.setValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", "Avg.Monthly Bal");

				/*repObj.setValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_month1", "July");
				 repObj.setValue(8, "cmplx_FinacleCore_cmplx_avgbalance_new_month1", "August");
				 repObj.setValue(9, "cmplx_FinacleCore_cmplx_avgbalance_new_month1", "September");
				 repObj.setValue(10, "cmplx_FinacleCore_cmplx_avgbalance_new_month1", "October");
				 repObj.setValue(11, "cmplx_FinacleCore_cmplx_avgbalance_new_month1", "November");
				 repObj.setValue(12, "cmplx_FinacleCore_cmplx_avgbalance_new_month1", "December");*/
// changed from 13 to 14 by abhishek for proc-2566
				for(int i=0; i<14 ; i++){
					repObj.setColumnDisabled(i, true);
				}
			}

			catch (Exception e) {
				CreditCard.mLogger.info( " " + e.toString());
			} 
			finally {
				repObj = null;
				repeaterHeaders = null;         
			}
		}
		else{
			//changed from 13 to 14 by abhishek for proc-2566
			for(int i=0; i<14 ; i++){
				repObj.setColumnDisabled(i, true);
			}
			for(int j=0; j<6;j++){
				if(repObj.getValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_Account").equals("")){
					repObj.setEditable(j, "cmplx_FinacleCore_cmplx_avgbalance_new_Consider_for_obligation",false);
				}
			}
			repObj.setEditable(6, "cmplx_FinacleCore_cmplx_avgbalance_new_Consider_for_obligation",false);
			repObj.setEditable(7, "cmplx_FinacleCore_cmplx_avgbalance_new_Consider_for_obligation",false);
		}
	}


	public void fetchoriginalDocRepeater(){

		//SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		

		List<String> repeaterHeaders = new ArrayList<String>();

		repeaterHeaders.add("Document List");

		repeaterHeaders.add("Status");
		repeaterHeaders.add("Received Date");
		repeaterHeaders.add("Expected Date");
		repeaterHeaders.add("Remarks");
		repeaterHeaders.add("OV Decision");
		repeaterHeaders.add("Approved By");



		/*repeaterHeaders.add("Jun-16");

    repeaterHeaders.add("May-16");
    repeaterHeaders.add("Apr-16");

    repeaterHeaders.add("Mar-16");

    repeaterHeaders.add("Feb-16");
    repeaterHeaders.add("Jan-16");*/

		CreditCard.mLogger.info("after making headers");



		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> Day = null;

		String documentName = null;

		IRepeater repObj=null;

		int repRowCount = 0;



		repObj = formObject.getRepeaterControl("OriginalValidation_Frame1");

		CreditCard.mLogger.info(""+repObj.toString());



		//query = "SELECT distinct DAY FROM NG_RLOS_FinacleCore WHERE ProcessName='CreditCard'";

		/*ProcessDefId IN" + "(SELECT ProcessDefId FROM PROCESSDEFTABLE WHERE ProcessName ='"+processName+"')";*/



		//Day = //formObject.getNGDataFromDataCache(query);

		CreditCard.mLogger.info(""+ Day);





		try{

			if (repObj.getRepeaterRowCount() != 0) {

				CreditCard.mLogger.info( "CLeared repeater object");

				repObj.clear();

			}
			repObj.setRepeaterHeaders(repeaterHeaders);

			for(int i=0;i<4;i++ ){
				repObj.addRow();

				//documentName = Day.get(i).get(0);

				CreditCard.mLogger.info(" "+ documentName);

				//repObj.setValue(i, 0, documentName);

				repRowCount = repObj.getRepeaterRowCount();

				CreditCard.mLogger.info( " " + repRowCount);

			}

		}

		catch (Exception e) {

			CreditCard.mLogger.info( " " + e.toString());

		} finally {

			repObj = null;

			repeaterHeaders = null;         
		}

	}



	public void  loadPicklist1()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 

		//  SKLogger.writeLog("CC","Inside CCCommon ->loadPicklist1()"+ActivityName);

		LoadPickList("cmplx_DEC_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision where ProcessName='CreditCard' and WorkstepName='"+formObject.getWFActivityName()+"'");

	}

	/*public void updateGrid()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		SKLogger.writeLog("RLOS","Inside CCCommon ->UpdateGrid(): Current wi name:"+formObject.getWFWorkitemName());
		String query=null;
		try{	
			query="UPDATE ng_le_ProductGrid SET Wi_name='"+formObject.getWFWorkitemName()+"' where ChildMapping=(select ParentMapping from ng_le_Product WHERE Wi_name='"+formObject.getWFWorkitemName()+"')";
			}catch(Exception e)
				{
					SKLogger.writeLog("  CC Iniation updateGrid()", "Exception Occurrrd: "+e.getMessage());
				}
		formObject.saveDataIntoDataSource(query);
		formObject.getDataFromDataSource(query);

	}*/

	public void makeSheetsInvisible(String tabName, String sheetNo) {

		try {
			CreditCard.mLogger.info( "Inside makeSheetsInvisible()" );
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sheetArr[] = sheetNo.split(",");

			for (int i = 0; i < sheetArr.length; i++) {
				formObject.setSheetVisible(tabName, Integer.parseInt(sheetArr[i]), false);
			}
		} catch (Exception e) {
			new CC_CommonCode();
			CreditCard.mLogger.info( "Exception: "+CC_Common.printException(e));
		}

	}

	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize)
	{
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);
			CreditCard.mLogger.info( ": 1");
			if (sControlName.equalsIgnoreCase("EMploymentDetails_Button2")) 
				objPickList.setWindowTitle("Search Employer");

			CreditCard.mLogger.info( ": 2");
			objPickList.setHeight(400);
			objPickList.setWidth(500);
			objPickList.setVisible(true);
			objPickList.setSearchEnabled(true);
			CreditCard.mLogger.info( ": 3");
			objPickList.addPickListListener(new EventListenerHandler(objPickList.getClientId()));
			CreditCard.mLogger.info( ": 4");
			objPickList.populateData(sQuery);
			CreditCard.mLogger.info( ": 5");
			formObject = null;
		}catch(Exception ex){
			new CC_CommonCode();
			CreditCard.mLogger.info( "Exception: "+CC_Common.printException(ex));
		}
	}

	public void setMailId(String userName)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		try
		{			
			String squery= "select mailid from pdbuser with (nolock) WHERE UPPER(USERNAME)=UPPER('"+userName+"')";
			List<List<String>> outputindex = null;
			outputindex = formObject.getNGDataFromDataCache(squery);
			CreditCard.mLogger.info( "mailID outputItemindex is: " +  outputindex);
			String mailID =outputindex.get(0).get(0);
			CreditCard.mLogger.info( "mailID is:" +  mailID);
			formObject.setNGValue("processby_email",  mailID);

		}
		catch(Exception e)
		{
			CreditCard.logException(e);
		}
	}

	public static void getAge(String dateBirth,String controlName){
		
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			CreditCard.mLogger.info( "Inside getAge(): "); 

			if (dateBirth.contains("/")){
				String parts[] = dateBirth.split("/");
				Calendar dob = Calendar.getInstance();
				Calendar today = Calendar.getInstance();
				
				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				
				int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
				
				if (month<0){
					age--; 
					month=today.get(Calendar.MONTH);
					month= 12-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);
					if(today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
						month=month-1;
					}
				}
				else if(month == 0 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)) {
					age--;
					month= 11-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);//If month is same as current no need to count it
				}
				else if(month == 1 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 2 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 3 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 4 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 5 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 6 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 7 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 8 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 9 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 10 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 11 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				//SKLogger.writeLog("RLOS_Common", "Values are with /: "+parts[2]+parts[1]+parts[0]+" age: "+age); 
				CreditCard.mLogger.info("Age is====== "+age+"."+month);

				formObject.setNGValue(controlName,(age+"."+month).toString(),false); 
			}
			else if(dateBirth.contains("-")) {
				String parts[] = dateBirth.split("-");
				Calendar dob = Calendar.getInstance();
				Calendar today = Calendar.getInstance();

				dob.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])); 

				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

				if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
					age--; 
				}
				CreditCard.mLogger.info(parts[2]+parts[1]+parts[0]); 


				formObject.setNGValue("cmplx_Customer_age",age.toString(),false); 
			}
		}

		catch(Exception e){
			//try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));}finally{hm.clear();}
		}
}
	public void getAgeWorldCheck(String dateBirth){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside getAge(): "); 
		String parts[] = dateBirth.split("/");
		Calendar dob = Calendar.getInstance();
		Calendar today = Calendar.getInstance();

		dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

		Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
			age--; 
		}
		CreditCard.mLogger.info( "Values are: "+parts[2]+parts[1]+parts[0]+age); 


		formObject.setNGValue("cmplx_WorldCheck_WorldCheck_Grid_age",age.toString(),false); 
	}




	public void Fields_ApplicationType_Employment()
	{
		CreditCard.mLogger.info( "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,4).equalsIgnoreCase("Reschedulment") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					//label ID change by Saurabh on 12th Oct.
					formObject.setVisible("EMploymentDetails_Label33",true); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",true); 
					break;
				}
				else{
					//label ID change by Saurabh on 12th Oct.
					formObject.setVisible("EMploymentDetails_Label33",false); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",false); 
				}
			}
		}
	}

	public void loadPicklist4()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadpicklist4:");
		String reqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		String target = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
		LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");
		/*
	if(reqProd.equalsIgnoreCase("Personal Loan")){
		LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and (product = 'PL' or product='B') order by code");
	}
	else if(reqProd.equalsIgnoreCase("Credit Card")){
		LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and (product = 'CC' or product='B') order by code");	
	}*/
		if(reqProd.equalsIgnoreCase("Personal Loan")){
			if(appCategory.equalsIgnoreCase("BAU")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
			}
			else if(appCategory.equalsIgnoreCase("Surrogate")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'PL' or product='B') order by code");
			}
		}
		else if(reqProd.equalsIgnoreCase("Credit Card")){
			if(appCategory.equalsIgnoreCase("BAU")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory.equalsIgnoreCase("Surrogate")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'CC' or product='B') order by code");	
			}	
		}
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock) where isActive='Y' order by Code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock) where isActive='Y' order by Code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) where isActive='Y' order by Code");
		//LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by Code");
		//LoadPickList("cmplx_EmploymentDetails_tenancntrct", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by Code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancntrct", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpIndusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmpContractType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_IndusSeg", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_channelcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ChannelCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusCC where isActive='Y'  order by code");
		//id changed by saurabh for emp categ PL on 10th oct.Mapping of field changed.
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_EmployerCategory_PL order by code");
		LoadPickList("cmplx_EmploymentDetails_FreezoneName","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_freezoneName where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_Status_PLExpact","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Status_PLNational","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancycntrctemirate","select '--Select--' union select convert(varchar, Description) from NG_MASTER_othBankCAC where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType","select '--Select--' union select convert(varchar, Description) from NG_MASTER_EmpContractType where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_PromotionCode","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_NepType","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_NEPType where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
		//LoadPickList("TradeLicencePlace", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from ng_master_TradeLicensePlace with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_ClassificationCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_ClassificationCode with (nolock)  where Product='CC'  order by code");
		//++ Below Code added By Yash on Oct 11, 2017  to fix :11-"Other Bank CAC field sjould be disabled. It will be enabled only when the target segment code is CAC" : Reported By Shashank on Oct 09, 2017++
		
		if(target.equalsIgnoreCase("CAC")){
			formObject.setEnabled("cmplx_EmploymentDetails_tenancycntrctemirate", true);
			}

		//++ Above Code added By Yash on Oct 11, 2017  to fix :11-"Other Bank CAC field sjould be disabled. It will be enabled only when the target segment code is CAC" : Reported By Shashank on Oct 09, 2017++
		if(subprod!=null && subprod.equalsIgnoreCase("IM")){
			LoadPickList("cmplx_EmploymentDetails_marketcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = 'IM' order by code");	
		}
		else{
			LoadPickList("cmplx_EmploymentDetails_marketcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = '!IM' order by code");
		}

	}
	public void Loadpicklist_comdDetails(){
	    LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
		LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
		LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
		LoadPickList("headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
    }

	/*public void loadPicklist_CompanyDet(){
    	LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
		//LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
        LoadPickList("indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock) order by code");
        LoadPickList("indusMAcro", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMacro with (nolock) order by code");
        LoadPickList("indusMicro", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMicro with (nolock) order by code");
        LoadPickList("legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_LegalEntity with (nolock) order by code");
        LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
        LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
        LoadPickList("EOW", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_State with (nolock) order by code");
        LoadPickList("headOffice", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_State with (nolock) order by code");
        LoadPickList("CompanyDetails_Combo1", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_POAHolder with (nolock) order by code");
        LoadPickList("CompanyDetails_TradeLicencePlace", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_TradeLicensePlace with (nolock) order by code");

        //formObject.setNGValue("compIndus", "--Select--");
    }*/

	public void fetchfinacleDocRepeater(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame10");
		List<String> repeaterHeaders = new ArrayList<String>();
		repeaterHeaders.add("Day");
		repeaterHeaders.add("January");
		repeaterHeaders.add("February");
		repeaterHeaders.add("March");
		repeaterHeaders.add("April");
		repeaterHeaders.add("May");
		repeaterHeaders.add("June");
		repeaterHeaders.add("July");
		repeaterHeaders.add("August");
		repeaterHeaders.add("September");
		repeaterHeaders.add("October");
		repeaterHeaders.add("November");
		repeaterHeaders.add("December");

		// 23 sept change
		repeaterHeaders.add("Workitem");
		repObj.setRepeaterHeaders(repeaterHeaders);
		if(!(repObj.getRepeaterRowCount() > 0)){
																													   
											   
			CreditCard.mLogger.info("after making headers");
			FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");
			List<List<String>> Day = null;
			String documentName = null;
			int repRowCount = 0;
			CreditCard.mLogger.info(""+repObj.toString());
			CreditCard.mLogger.info(""+ Day);
			try{
				for(int i=0;i<31;i++ ){
					repObj.addRow();
					//documentName = Day.get(i).get(0);
					CreditCard.mLogger.info(" "+ documentName);
					//repObj.setValue(i, 0, documentName);
					repRowCount = repObj.getRepeaterRowCount();
					repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DAY", ""+(i+1));
					// change 23 sept
					repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc", formObject.getWFWorkitemName());
					CreditCard.mLogger.info( " " + repRowCount);

				}
				repObj.addRow();
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DAY", "No. Of Days");
				// to set the values of no of days for particular month
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN", "31");
				repObj.setValue(31, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc", formObject.getWFWorkitemName());
				String[] date1 = Currentdate().split("/");
				int year = Integer.parseInt(date1[2]);
				
				if(year % 4 == 0){
					repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB", "29");
				}
				else{
					repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB", "28");
				}

				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR", "31");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR", "30");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY", "31");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN", "30");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL", "31");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG", "31");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP", "30");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT", "31");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV", "30");
				repObj.setValue(31 , "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC", "31");

				repObj.setRowDisabled(31, true);
				repObj.addRow();
				repObj.setValue(32, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DAY", "Total");
				repObj.setValue(32, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc", formObject.getWFWorkitemName());
				repObj.setRowDisabled(32, true);
				repObj.addRow();
				repObj.setValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DAY", "Avg. balance");
				repObj.setValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc", formObject.getWFWorkitemName());
				repObj.setRowDisabled(33, true);
				repObj.setColumnDisabled(0, true);
				 // for enabling last 6 months
				 ChangeRepeater(repObj);
			}

			catch (Exception e) {
				CreditCard.mLogger.info( " " + e.toString());
			} finally {
				repObj = null;
				repeaterHeaders = null;         
			}
		}

		else{
			repObj.setRowDisabled(31, true);
			repObj.setRowDisabled(32, true);
			repObj.setRowDisabled(33, true);
			repObj.setColumnDisabled(0, true);
			 // for enabling last 6 months
			 ChangeRepeater(repObj);
		}
	}
	// added by abhishek for enabling last 6 months
	
	 public void ChangeRepeater(IRepeater repObj){
		 HashMap<Integer,String> Monthhm =new HashMap<Integer,String>();  
		Monthhm.put(1,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN");
		Monthhm.put(2,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB");
		Monthhm.put(3,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR");

		Monthhm.put(4,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR");
		Monthhm.put(5,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY");

		Monthhm.put(6,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN");
		Monthhm.put(7,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL");
		Monthhm.put(8,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG");

		Monthhm.put(9,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP");
		Monthhm.put(10,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT");

		Monthhm.put(11,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV");
		Monthhm.put(12,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC");

		ArrayList<String> MonthArray = new ArrayList<String>();
		MonthArray.add("JAN");
		MonthArray.add("FEB");
		MonthArray.add("MAR");
		MonthArray.add("APR");
		MonthArray.add("MAY");
		MonthArray.add("JUN");
		MonthArray.add("JUL");
		MonthArray.add("AUG");
		MonthArray.add("SEP");
		MonthArray.add("OCT");
		MonthArray.add("NOV");
		MonthArray.add("DEC");




		String[] date1 = Currentdate().split("/");
		int Monthval = Integer.parseInt(date1[0]);
		Monthval = Monthval - 2 ;
		    
		for(int i= 6 ;i>=1; i--){
			if(Monthval == -1){
		    		 
				MonthArray.remove(MonthArray.size() - 1);
			}
			else{
				MonthArray.remove(Monthval);
				Monthval--;
		    		 CreditCard.mLogger.info("inside Monthval else"+Monthval);
			}


		}

		   
		    
		    for(int i = MonthArray.size(); i >= 1 ; i--){
		    	 
																															 
		    	repObj.setColumnEditable(getKey(Monthhm,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_"+MonthArray.get(i-1)), false);
		    	
		    	
		    	
		    }
		    
		    
		   
		    
		    
	 }
	 
	 
		 public void ChangeRepeaterTrnover(IRepeater repObj){
			 HashMap<Integer,String> Monthhm =new HashMap<Integer,String>();  
			 Monthhm.put(1,"cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1");
			 Monthhm.put(2,"cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1");
			 Monthhm.put(3,"cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1");
			 
			 Monthhm.put(4,"cmplx_FinacleCore_cmplx_TurnoverNBC_APR1");
			 Monthhm.put(5,"cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1");
			 
			 Monthhm.put(6,"cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1");
			 Monthhm.put(7,"cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1");
			 Monthhm.put(8,"cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1");
			 
			 Monthhm.put(9,"cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1");
			 Monthhm.put(10,"cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1");
			 
			 Monthhm.put(11,"cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1");
			 Monthhm.put(12,"cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1");
			 
			 	ArrayList<String> MonthArray = new ArrayList<String>();
			 	MonthArray.add("JAN1");
			 	MonthArray.add("FEB1");
			 	MonthArray.add("MAR1");
			 	MonthArray.add("APR1");
			 	MonthArray.add("MAY1");
			 	MonthArray.add("JUN1");
			 	MonthArray.add("JUL1");
			 	MonthArray.add("AUG1");
			 	MonthArray.add("SEP1");
			 	MonthArray.add("OCT1");
			 	MonthArray.add("NOV1");
			 	MonthArray.add("DEC1");
			   
			 
			 	
			    
			    String[] date1 = Currentdate().split("/");
			    int Monthval = Integer.parseInt(date1[0]);
			    Monthval = Monthval - 2 ;
			    CreditCard.mLogger.info("Monthval value is"+Monthval);
			    for(int i=6 ;i>=1; i--){
			    	if(Monthval == -1){
			    		 CreditCard.mLogger.info("Inside monthval if"+Monthval);
			    		MonthArray.remove(MonthArray.size() - 1);
			    	}
			    	else{
			    		MonthArray.remove(Monthval);
			    		Monthval--;
			    		 CreditCard.mLogger.info("inside Monthval else"+Monthval);
			    	}
			    	
			    	
			    }
			    
			   
			    
			    for(int i = MonthArray.size(); i >= 1 ; i--){
			    	 
																															
			    	repObj.setColumnEditable(getKey(Monthhm,"cmplx_FinacleCore_cmplx_TurnoverNBC_"+MonthArray.get(i-1)), false);
			    	
			    	
			    	
			    }
			    
			 
	 }
	


		 public void CalculateAvgTotal(String Control) throws ValidatorException{
	            FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	            IRepeater repObj1 = formObject.getRepeaterControl("FinacleCore_Frame9");
	            int counter = 0;
	            float sum = 0 ;
	            float average = 0 ;
	            for(int i = 0 ; i<6 ; i++){
	                  // for onsite
	                  if(repObj1.getValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_Consider_for_obligation").equalsIgnoreCase("true")){
	                        sum += Float.parseFloat(repObj1.getValue(i, Control));      
	                        counter++;
	                  }
	                        /*sum += Float.parseFloat(repObj1.getValue(i, Control));    
	                        counter++;*/
	                  }
	            
	            try{
	                  if(counter>0){
	                        average = sum/counter;
	                        repObj1.setValue(6,Control ,convertFloatToString(sum));
	                        repObj1.setValue(7,Control , convertFloatToString(average));
	                        formObject.setNGValue("cmplx_FinacleCore_toal_accounts_last1", counter);
	                  }
	                  else{
	                        String alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL049");

	                        throw new ValidatorException(new FacesMessage(alert_msg));
	                  }

	            }catch(Exception ex)
	            {
	                  CreditCard.mLogger.info("Exception occured while setting values in repeater"+ex);
	            }
	      }


	public void CalculateAvgTotalthree(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj1 = formObject.getRepeaterControl("FinacleCore_Frame9");
		int counter = 0;
		float sum = 0;
		float average1 = 0;
		float average2 = 0;

		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JAN2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JAN2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_FEB2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_FEB2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_MAR2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_MAR2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_APR2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_APR2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_MAY2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_MAY2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JUN2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JUN2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JUL2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JUL2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_AUG2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_AUG2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_SEP2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_SEP2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_OCT2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_OCT2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_NOV2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_NOV2"));
			counter++;
		}
		if(!repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_DEC2").equals("0.0")){
			sum += Float.parseFloat(repObj1.getValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_DEC2"));
			counter++;
		}
		try{
			//average = sum/counter;
			average1 = sum/3;
			average2 = sum/6;
			//repObj1.setValue(6,Control , Float.toString(sum));
			//formObject.setNGValue("cmplx_FinacleCore_cmplx_avgbalance_new_Avg_Bal_3_Month", "--Select--");
			formObject.setNGValue("cmplx_FinacleCore_total_avg_last13", convertFloatToString(average1));
			formObject.setNGValue("cmplx_FinacleCore_total_avg_last_16",convertFloatToString(average2));
		}catch(Exception ex)
		{
			CreditCard.mLogger.info("Exception occured while setting values in repeater"+ex);
		}
	}
	public void CalculateAvgTotalsix(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int counter = 0;
		float sum = 0;
		float average = 0;
		float  average1 = 0;
		/*for(int i = 0 ; i<6 ; i++){
				if(repObj1.getValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_Consider_for_obligation").equalsIgnoreCase("true")){
				     sum += Float.parseFloat(repObj1.getValue(i, Control));	
					counter++;
				}

			}*/
		try{
			average = sum/counter;
			average1 = average/6;
			//repObj1.setValue(6,Control , Float.toString(sum));		
			formObject.setNGValue("cmplx_FinacleCore_cmplx_avgbalance_new_Avg_Bal_6_Month", average1);

		}catch(Exception ex)
		{
			CreditCard.mLogger.info("Exception occured while setting values in repeater"+ex+ "");
		}



	}


	//added 30/08/2017
	public String getCustOECD_details(String call_name){
		CreditCard.mLogger.info( "inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			CreditCard.mLogger.info( "inside getCustAddress_details add_row_count+ : "+add_row_count);
			for (int i = 0; i<add_row_count;i++){
				String City_of_Birth = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 3); //0
				String Country_of_Birth=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 2);//1
				String Undocumented_Flag=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 0);//2
				String UndocumentedFlag_Reason=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 1);//3

				if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
					add_xml_str = add_xml_str + "<OECDDet><CityOfBirth>"+City_of_Birth+"</CityOfBirth> ";
					add_xml_str = add_xml_str + "<CountryOfBirth>"+Country_of_Birth+"</CountryOfBirth>";
					add_xml_str = add_xml_str + "<CRSUnDocFlg>"+Undocumented_Flag+"</CRSUnDocFlg>";
					add_xml_str = add_xml_str + "<CRSUndocFlgReason>"+UndocumentedFlag_Reason+"</CRSUndocFlgReason></OECDDet>";

				}

			}
			CreditCard.mLogger.info( "OECD tag creation "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			CreditCard.mLogger.info( "Exception Occure in generate Address XMl"+e.getMessage());
			return add_xml_str;
		}

	}

	//ended 30/08/2017


	public String getCustAddress_details(String call_name){
		CreditCard.mLogger.info( "inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			CreditCard.mLogger.info( "inside getCustAddress_details add_row_count+ : "+add_row_count);

			for (int i = 0; i<add_row_count;i++){
				String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
				String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//1
				String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//2
				String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//3
				String Landmard=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//4
				String city = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//5
				String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//6
				String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//7
				String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//8
				String years_in_current_add=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 9);//9

				//added here
				CreditCard.mLogger.info( "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
				int years=Integer.parseInt(years_in_current_add);
				//ended here

				String preferrd="";
				//Code change to added Effective from and to start
				String EffectiveFrom="";
				String EffectiveTo="";
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				EffectiveTo=sdf1.format(cal.getTime());
				cal.add(Calendar.YEAR, -years);
				EffectiveFrom=sdf1.format(cal.getTime());
				CreditCard.mLogger.info(""+EffectiveTo);
				CreditCard.mLogger.info(""+EffectiveFrom);
				//Code change to added Effective from and to End
				CreditCard.mLogger.info(" Address pref flag: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11));
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11).equalsIgnoreCase("true"))//10
					preferrd = "Y";
				else
					preferrd = "N";


				if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
					add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
					//Code change to added Effective from and to start
					add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
					add_xml_str = add_xml_str + "<EffectiveTo>"+EffectiveTo+"</EffectiveTo>";
					add_xml_str = add_xml_str + "<HoldMailFlag>N</HoldMailFlag>";
					add_xml_str = add_xml_str + "<ReturnFlag>N</ReturnFlag>";
					add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>";
					//Code change to added Effective from and to End

					add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
					add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
					add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
					add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode></AddrDet>";
					//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
				}
				else if(call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
					add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<AddressLine4>"+Landmard+"</AddressLine4>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
					add_xml_str = add_xml_str + "<EffectiveFromDate>"+EffectiveFrom+"</EffectiveFromDate>";
					add_xml_str = add_xml_str + "<EffectiveToDate>"+EffectiveTo+"</EffectiveToDate>";
					add_xml_str = add_xml_str + "<NumberOfYears>"+years+"</NumberOfYears>";
					add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>"
					+ "</AddrDet>";
					//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
				}
				else if (call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
					add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType><AddrPrefFlag>"+preferrd+"</AddrPrefFlag><PrefFormat>STRUCTURED_FORMAT</PrefFormat>";
					add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
					add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
					add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
					add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
				}
				else{
					add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
				}
			}
			CreditCard.mLogger.info( "Address tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			CreditCard.mLogger.info( "Exception Occure in generate Address XMl"+e.getMessage());
			return add_xml_str;
		}


	}
	/*public void loadInCardGrid()
{

	FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
	SKLogger.writeLog("RLOS ","Inside  ->loadInCardGrid()");
	 String WI_Name = formObject.getNGValue("WIname");
	String query="select CardType,CardNo,CardStatus,CardLimit,Outstanding,ConsiderforObligations,QCAvailed,QCAmtorEMI,LastPaidDate,Worststatusdate,AECBExcepRemarks,Liabililtytype,Status,RequestedDate,RequestedAmount,MOBforIM,NoofRepayments,SettledAmount,TypeofSettlement,encryp_CardNo,card_wi_name,ConsiderForApplication from ng_RLOS_GR_CardExternalLiability with (nolock) where card_wi_name='"+WI_Name+"'";
	List<List<String>> list=formObject.getDataFromDataSource(query);
	 SKLogger.writeLog("RLOS query is:",query);
	 AesUtil myutil=new AesUtil();
	 for(List<String> mylist : list)
	 {
		 String CardType=mylist.get(0);
		 //String CardNo=mylist.get(1);
		 String CardStatus=mylist.get(2);
		 String CardLimit=mylist.get(3);
		 String Outstanding=mylist.get(4);
		 String ConsiderforObligations=mylist.get(5);
		 String QCAvailed=mylist.get(6);
		 String QCAmtorEMI=mylist.get(7);
		 //String LastPaidDate=mylist.get(8);
		 String LastPaidDate="01/11/17";

		 //String Worststatusdate=mylist.get(9);
		 String Worststatusdate="01/11/17";
		 String AECBExcepRemarks=mylist.get(10);
		 String Liabililtytype=mylist.get(11);
		 String Status=mylist.get(12);
		// String RequestedDate=mylist.get(13);
		 String RequestedDate="01/11/17";
		 String RequestedAmount=mylist.get(14);
		 String MOBforIM=mylist.get(15);
		 String NoofRepayments=mylist.get(16);
		 String SettledAmount=mylist.get(17);
		 String TypeofSettlement=mylist.get(18);
		 String encryp_CardNo=mylist.get(19);
		 String card_wi_name=mylist.get(20);
		 String ConsiderForApplication=mylist.get(21);

		String decrypt_CardNo=myutil.Decrypt(encryp_CardNo);
		SKLogger.writeLog("RLOS decrypt_CardNo is:",decrypt_CardNo);
		 List<String> tempList=new ArrayList<String>();
		 tempList.add(CardType);
		 tempList.add(decrypt_CardNo);
		 tempList.add(CardStatus);
		 tempList.add(CardLimit);
		 tempList.add(Outstanding);
		 tempList.add(ConsiderforObligations);
		 tempList.add(QCAvailed);
		 tempList.add(QCAmtorEMI);
		 tempList.add(LastPaidDate);
		 tempList.add(Worststatusdate);
		 tempList.add(AECBExcepRemarks);
		 tempList.add(Liabililtytype);
		 tempList.add(Status);
		 tempList.add(RequestedDate);
		 tempList.add(RequestedAmount);
		 tempList.add(MOBforIM);
		 tempList.add(NoofRepayments);
		 tempList.add(SettledAmount);
		 tempList.add(TypeofSettlement);
		 tempList.add(encryp_CardNo);
		 tempList.add(card_wi_name);
		 tempList.add(ConsiderForApplication);

		 SKLogger.writeLog("RLOS TempList is:",tempList.toString());
	formObject.addItemFromList("cmplx_ExternalLiabilities_cmplx_CardGrid", tempList);

	 }
	// String query2="Select encryp_CardNo from ng_RLOS_GR_CardExternalLiability where card_wi_name='"+WI_Name+"'";
	// List<List<String>> list2=formObject.getDataFromDataSource(query2);


	// String CardNo=list2.get(0).get(0);
	 //SKLogger.writeLog("RLOS CardNo is:",query);

	 //SKLogger.writeLog("RLOS decrypt_CardNo is:",decrypt_CardNo);

	 List<String> tempList=new ArrayList();
	 tempList.add(decrypt_CardNo);
	 SKLogger.writeLog("RLOS tempList is:",tempList.get(0));

	 list.add(1, tempList);
	 for(List<String> mylist : list)
		for(String mystring:mylist)
		 SKLogger.writeLog("RLOS mystring is:",mystring);


	//SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
	for (List<String> a : list) 
	{

		formObject.addItemFromList("Decision_ListView1", a);
	}

}
	 */
	// tanshu started
	public String GenerateXML(String callName,String Operation_name)
	{
		CreditCard.mLogger.info("RLOSCommon"+ "Inside GenerateXML():");

		StringBuffer final_xml= new StringBuffer("");
		String header ="";
		String footer = "";
		String parentTagName="";
		Socket socket = null;
		OutputStream out = null;
		InputStream socketInputStream = null;
		DataOutputStream dout = null;
		DataInputStream din = null;
		String mqOutputResponse=null;
		String mqInputRequest=null;
		String cabinetName=null;
		String wi_name=null;
		String ws_name=null;
		String sessionID=null;
		String userName=null;
		String socketServerIP;
		String sQuery=null;

		Integer socketServerPort;
		String fin_call_name="Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY";
		CreditCard.mLogger.info("$$outputgGridtXML "+"before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String emp_type ="";
			int prod_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if (prod_count!=0){
				 emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
			}
			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			CreditCard.mLogger.info("RLOSCommon"+ "sQuery"+sQuery_header);
			List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);
			if(!OutputXML_header.isEmpty()){
				CreditCard.mLogger.info("RLOSCommon header: "+OutputXML_header.get(0).get(0)+" footer: "+OutputXML_header.get(0).get(1)+" parenttagname: "+OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);
				String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
				// String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val";
				// String sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive FROM NG_Integration_PL_field_Mapping where Call_name='"+callName+"'ORDER BY tag_seq ASC" ;
				if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
					CreditCard.mLogger.info("inside if of operation"+"operation111"+Operation_name);
					CreditCard.mLogger.info("inside if of operation"+"callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					CreditCard.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
				}
				else{
					CreditCard.mLogger.info("inside else of operation"+"operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					CreditCard.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
				}

				List<List<String>> OutputXML=formObject.getDataFromDataSource(sQuery);
				CreditCard.mLogger.info("OutputXML"+"OutputXML"+OutputXML);
				if(!OutputXML.isEmpty()){
					//CreditCard.mLogger.info("$$AKSHAY",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					CreditCard.mLogger.info("GenerateXML Integration field mapping table"+OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3)+OutputXML.get(0).get(4));
					CreditCard.mLogger.info("GenerateXML Integration field mapping table"+OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					int n=OutputXML.size();
					CreditCard.mLogger.info("CC_Common"+"row count:"+n);


					if( n> 0)
					{

						CreditCard.mLogger.info(""+"column length"+col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();
						Map<String, String> recordFileMap = new HashMap<String, String>();

						for(List<String> mylist:OutputXML)
						{
							// for(int i=0;i<col_n.length();i++)
							for(int i=0;i<8;i++)
							{
								//CreditCard.mLogger.info("rec: "+records.item(rec));
								CreditCard.mLogger.info(""+"column length values"+col_n);
								String[] col_name = col_n.split(",");
								recordFileMap.put(col_name[i],mylist.get(i));
							}
							recordFileMap.get("call_type");
							String Call_name = (String) recordFileMap.get("Call_name");
							String form_control = (String) recordFileMap.get("form_control");
							String parent_tag = (String) recordFileMap.get("parent_tag_name");
							String tag_name = (String) recordFileMap.get("xmltag_name");
							String is_repetitive = (String) recordFileMap.get("is_repetitive");
							String default_val = (String) recordFileMap.get("default_val");
							String data_format12 = (String) recordFileMap.get("data_format");
							CreditCard.mLogger.info("#RLOS COmmonm inside generate XML: "+"tag_name : "+tag_name +" valuie of default_val: "+default_val+" Call_name: "+Call_name+" parent_tag"+ parent_tag);
							String form_control_val="";
							java.util.Date startDate;

							if(tag_name.equalsIgnoreCase("AddressDetails") &&( Call_name.equalsIgnoreCase("DEDUP_SUMMARY")||Call_name.equalsIgnoreCase("BLACKLIST_DETAILS") ||Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ"))){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
									xml_str = xml_str + getCustAddress_details();
									CreditCard.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}

							//added 30/08/2017
							else if(tag_name.equalsIgnoreCase("OECDDet") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
								CreditCard.mLogger.info("inside 1st if"+"inside customer update req1234");
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info("inside 1st if"+"inside customer update req2123");
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info("RLOS COMMON"+" before adding OECD+ "+xml_str);
									xml_str = xml_str + getCustOECD_details(Call_name);
									CreditCard.mLogger.info("RLOS COMMON"+" after adding OeCD+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							//ended 30/08/2017
							else if(tag_name.equalsIgnoreCase("PhnLocalCode") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
								CreditCard.mLogger.info("inside PL Common generate xml"+"PhnLocalCode to substring");
								String xml_str = int_xml.get(parent_tag);
								String phn_no = formObject.getNGValue(form_control);
								if((!phn_no.equalsIgnoreCase("")) && phn_no.indexOf("00971")>-1){
									phn_no = phn_no.substring(5);
								}

								xml_str = xml_str+"<"+tag_name+">"+phn_no+"</"+ tag_name+">";

								CreditCard.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							//code change for Customer update to add address tag.
							else if(tag_name.equalsIgnoreCase("AddrDet") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
								CreditCard.mLogger.info("inside 1st if"+"inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
									xml_str = xml_str + getCustAddress_details(Call_name);
									CreditCard.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	                            	
							}
							//code change for Customer update to add address tag.
							else if(tag_name.equalsIgnoreCase("MaritalStatus") && (Call_name.equalsIgnoreCase("DEDUP_SUMMARY")||Call_name.equalsIgnoreCase("BLACKLIST_DETAILS"))){
								String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+marrital_code
								+"</"+ tag_name+">";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("VIPFlg") && Call_name.equalsIgnoreCase("NEW_CARD_REQ")){
								String vip_flag="N";
								if(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")){
									vip_flag="Y";
								}
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+vip_flag
								+"</"+ tag_name+">";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding VIP flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("ProcessingUserId") && Call_name.equalsIgnoreCase("NEW_CARD_REQ")){
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
								+"</"+ tag_name+">";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(tag_name.equalsIgnoreCase("ProcessingDate") && Call_name.equalsIgnoreCase("NEW_CARD_REQ")){
								String xml_str = int_xml.get(parent_tag);
								SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
								xml_str = xml_str + "<"+tag_name+">"+sdf1.format(new Date())
								+"</"+ tag_name+">";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							//FOR dectech

							else if(tag_name.equalsIgnoreCase("Channel") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

								String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

								String xml_str = int_xml.get(parent_tag);
								xml_str =  "<"+tag_name+">"+(ReqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")
								+"</"+ tag_name+">";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if(tag_name.equalsIgnoreCase("emp_type") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

								String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
								if(empttype!=null){	
									if (empttype.equalsIgnoreCase("Salaried")){
										empttype="S";
									}
									else if (empttype.equalsIgnoreCase("Salaried Pensioner")){
										empttype="SP";
									}
									else {
										empttype="SE";
									}
								}
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}
							else if(tag_name.equalsIgnoreCase("world_check") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ ");

								String world_check=formObject.getNGValue("IS_WORLD_CHECK");
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
								if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid")==0){
									world_check="Negative";
								}
								else {
									world_check="Positive";
								}


								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding world_check+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if(tag_name.equalsIgnoreCase("prev_loan_dbr") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ ");
								String PreviousLoanDBR="";
								String PreviousLoanEmp="";
								String PreviousLoanMultiple="";
								String PreviousLoanTAI="";

								String squeryloan="select PreviousLoanDBR,PreviousLoanEmp,PreviousLoanMultiple,PreviousLoanTAI from ng_RLOS_CUSTEXPOSE_LoanDetails where Request_Type='CollectionsSummary' and Limit_Increase='true' and Child_Wi= '"+formObject.getWFWorkitemName()+"'";
								List<List<String>> prevLoan=formObject.getDataFromDataSource(squeryloan);
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ "+squeryloan);

								if (prevLoan!=null && prevLoan.size()>0){
									PreviousLoanDBR=prevLoan.get(0).get(0);
									PreviousLoanEmp=prevLoan.get(0).get(1);
									PreviousLoanMultiple=prevLoan.get(0).get(2);
									PreviousLoanTAI=prevLoan.get(0).get(3);
								}


								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanDBR+"</"+ tag_name+"><prev_loan_tai>"+PreviousLoanTAI+"</prev_loan_tai><prev_loan_multiple>"+PreviousLoanMultiple+"</prev_loan_multiple><prev_loan_amount></prev_loan_amount><prev_loan_employer>"+PreviousLoanEmp+"</prev_loan_employer>";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding world_check+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if(tag_name.equalsIgnoreCase("no_of_cheque_bounce_int_3mon_Ind") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (NOC!=null && NOC.size()>0){
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							else if(tag_name.equalsIgnoreCase("no_of_DDS_return_int_3mon_Ind") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (NOC!=null && NOC.size()>0){
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							else if(tag_name.equalsIgnoreCase("blacklist_cust_type") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
								String ParentWI_Name = formObject.getNGValue("Parent_WIName");
								String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail where cif_wi_name='"+ParentWI_Name+"' and cif_searchType = 'Internal'";
								List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
								String internal_blacklist =  "";
								String internal_blacklist_date =  "";
								String internal_blacklist_code =  "";
								String internal_negative_flag =  "";
								String internal_negative_date =  "";
								String internal_negative_code =  "";

								if (Blacklist!=null && Blacklist.size()>0){		
									internal_blacklist =  Blacklist.get(0).get(0);
									internal_blacklist_date =  Blacklist.get(0).get(1);
									internal_blacklist_code =  Blacklist.get(0).get(2);
									internal_negative_flag =  Blacklist.get(0).get(3);
									internal_negative_date =  Blacklist.get(0).get(4);
									internal_negative_code =  Blacklist.get(0).get(5);
								}
								String xml_str =  int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+"><internal_blacklist>"+internal_blacklist+"</internal_blacklist><internal_blacklist_date>"+internal_blacklist_date+"</internal_blacklist_date><internal_blacklist_code>"+internal_blacklist_code+"</internal_blacklist_code><negative_cust_type>I</negative_cust_type><internal_negative_flag>"+internal_negative_flag+"</internal_negative_flag><internal_negative_date>"+internal_negative_date+"</internal_negative_date><internal_negative_code>"+internal_negative_code+"</internal_negative_code>";

								CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}

							/*
							 * else if(tag_name.equalsIgnoreCase("LienDetails") && (Call_name.equalsIgnoreCase("CARD_NOTIFICATION")||Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST"))){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									PL_SKLogger.writeLog("inside 1st if","inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getLien_details(Call_name);
									PL_SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							 * */
							else if(((tag_name.equalsIgnoreCase("lob"))||(tag_name.equalsIgnoreCase("target_segment_code"))||(tag_name.equalsIgnoreCase("designation"))||(tag_name.equalsIgnoreCase("emp_name"))||(tag_name.equalsIgnoreCase("industry_sector"))||(tag_name.equalsIgnoreCase("industry_marco"))||(tag_name.equalsIgnoreCase("industry_micro"))||(tag_name.equalsIgnoreCase("bvr"))||(tag_name.equalsIgnoreCase("eff_date_estba"))||(tag_name.equalsIgnoreCase("poa"))||(tag_name.equalsIgnoreCase("tlc_issue_date"))||(tag_name.equalsIgnoreCase("cc_employer_status"))||(tag_name.equalsIgnoreCase("pl_employer_status"))||(tag_name.equalsIgnoreCase("marketing_code"))) && Call_name.equalsIgnoreCase("DECTECH") && emp_type.equalsIgnoreCase("Self Employed")){
								CreditCard.mLogger.info("RLOSCommon java file"
										+"inside getProduct_details : ");
								String xml_str =  int_xml.get(parent_tag);
								int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
								String lob="";
								String target_segment_code="";
								String designation="";
								String emp_name="";
								String industry_sector="";
								String eff_date_estba="";
								String industry_marco="";
								String industry_micro="";
								String bvr="";
								String poa="";
								String marketing_code="";
								
								for (int i = 0; i<Comp_row_count;i++){
									lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); //0
									target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 25); //0
									designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); //0
									emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
								//	industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); //0
									eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); //0
									industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); //0
									industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); //0
									industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); //0
									bvr = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 19); //0
									poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 20); //0
									marketing_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 27); //0
									
								
								}
								if(tag_name.equalsIgnoreCase("lob")){
									xml_str = xml_str+ "<"+tag_name+">"+lob+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("target_segment_code")){
									xml_str = xml_str+ "<"+tag_name+">"+target_segment_code+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("designation")){
									xml_str = xml_str+ "<"+tag_name+">"+designation+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("emp_name")){
									xml_str = xml_str+ "<"+tag_name+">"+emp_name+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("industry_sector")){
									xml_str = xml_str+ "<"+tag_name+">"+industry_sector+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("industry_marco")){
									xml_str = xml_str+ "<"+tag_name+">"+industry_marco+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("industry_micro")){
									xml_str = xml_str+ "<"+tag_name+">"+industry_micro+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("bvr")){
									xml_str = xml_str+ "<"+tag_name+">"+bvr+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("eff_date_estba")){
									xml_str = xml_str+ "<"+tag_name+">"+eff_date_estba+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("poa")){
									xml_str = xml_str+ "<"+tag_name+">"+poa+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("cc_employer_status")){
									xml_str = xml_str+ "<"+tag_name+">"+bvr+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("tlc_issue_date")){
									xml_str = xml_str+ "<"+tag_name+">"+eff_date_estba+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("pl_employer_status")){
									xml_str = xml_str+ "<"+tag_name+">"+poa+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("marketing_code")){
									xml_str = xml_str+ "<"+tag_name+">"+marketing_code+"</"+ tag_name+">";
								}
								CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if((tag_name.equalsIgnoreCase("auth_sig_sole_emp")||tag_name.equalsIgnoreCase("shareholding_perc")) && Call_name.equalsIgnoreCase("DECTECH") && emp_type.equalsIgnoreCase("Self Employed")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
								String auth_sig_sole_emp =  "";
								String shareholding_perc =  "";
								int Authsign_row_count = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
								if (Authsign_row_count !=0){
									auth_sig_sole_emp =(formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 10).equalsIgnoreCase("Yes")?"Y":"N"); //0
									shareholding_perc = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 9); //0
									
									
								}
								String xml_str =  int_xml.get(parent_tag);
								if(tag_name.equalsIgnoreCase("auth_sig_sole_emp")){
									xml_str = xml_str+ "<"+tag_name+">"+auth_sig_sole_emp+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("shareholding_perc")){
									xml_str = xml_str+ "<"+tag_name+">"+shareholding_perc+"</"+ tag_name+">";
								}
								

								CreditCard.mLogger.info("RLOS COMMON"+" after adding shareholding_perc+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}
							else if((tag_name.equalsIgnoreCase("external_blacklist_flag")||tag_name.equalsIgnoreCase("external_blacklist_date")||tag_name.equalsIgnoreCase("external_blacklist_code")) && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
							String ParentWI_Name = formObject.getNGValue("Parent_WIName");
							String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where (cif_wi_name='"+ParentWI_Name+"' or cif_wi_name='"+formObject.getWFWorkitemName()+"') and cif_searchType = 'External'";
							CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ "+squeryBlacklist);
							List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
							String External_blacklist_date =  "";
							String External_blacklist_code =  "";
								
							if (Blacklist!=null && Blacklist.size()>0){		
								External_blacklist_date =  Blacklist.get(0).get(1);
								External_blacklist_code =  Blacklist.get(0).get(2);
							}
							String xml_str =  int_xml.get(parent_tag);
							if(tag_name.equalsIgnoreCase("external_blacklist_flag")){
								xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("external_blacklist_date")){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_date+"</"+ tag_name+">";
									}
								else if(tag_name.equalsIgnoreCase("external_blacklist_code")){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_code+"</"+ tag_name+">";
									}
							
							CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						
									                            	
						}
							else if(tag_name.equalsIgnoreCase("ApplicationDetails") && (Call_name.equalsIgnoreCase("DECTECH"))){
								CreditCard.mLogger.info("inside 1st if"+"inside DECTECH req1");

								CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding product+ "+xml_str);
								xml_str = xml_str + getProduct_details();
								CreditCard.mLogger.info("RLOS COMMON"+" after adding product+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}
							//code commented for age calculation as the generic method is used - 28-sept-2017 start
							/*
						      else if(tag_name.equalsIgnoreCase("age") && Call_name.equalsIgnoreCase("DECTECH")){
						    	  CreditCard.mLogger.info("RLOS COMMON"+" Inside age + ");
									if(int_xml.containsKey(parent_tag))
									{
										//by aman for getting difference between 2 dates in YY.MM format.
										String age = getYearsDifference(formObject,"cmplx_Customer_DOb");		
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+age
										+"</"+ tag_name+">";

										CreditCard.mLogger.info("RLOS COMMON"+" after adding age+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}		                            	
								}

							else if(tag_name.equalsIgnoreCase("LOS") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
									//CreditCard.mLogger.info("RLOS COMMON"+" LOS length+ "+LOS.length());
									CreditCard.mLogger.info("RLOS COMMON"+" value of LOS "+LOS);
								try{	
								if (LOS!=null){
										CreditCard.mLogger.info("RLOS COMMON"+" after adding los+ "+LOS);
									if (LOS.length()!=0){
										if (LOS.length()==1){
											LOS="0"+LOS+".00";
										}
										else{
											LOS=LOS+".00";
										}
									}

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+LOS
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding los+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}
								}	
								catch (Exception e){
									CreditCard.mLogger.info("RLOS COMMON"+" after adding los+ "+printException(e));


								}
							}
							}*/
							//code commented for age calculation as the generic method is used - 28-sept-2017 End
							/*else if(tag_name.equalsIgnoreCase("resident_flag") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String resident_flag=formObject.getNGValue("cmplx_Customer_RESIDENTNONRESIDENT");

									String xml_str = int_xml.get(parent_tag);
									if (resident_flag!=null){
										if (resident_flag.equalsIgnoreCase("Resident")){
											resident_flag="Y";
										}
										else{
											resident_flag="N";
										}
									}
									xml_str = xml_str + "<"+tag_name+">"+resident_flag
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_RESIDENTNONRESIDENT+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}*/
							else if(tag_name.equalsIgnoreCase("cust_name") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
									String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
									String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");

									String full_name=first_name+" "+middle_name+""+last_name;

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+full_name
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}

							else if(tag_name.equalsIgnoreCase("ref_phone_no") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ ");
									int count=formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
									String ref_phone_no="";
									String ref_relationship="";
									CreditCard.mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ "+count);
									if (count != 0){
										ref_phone_no=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,4);
										ref_relationship=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,2);
										CreditCard.mLogger.info("RLOS COMMON"+" after adding ref_phone_no+ "+ref_phone_no);
										CreditCard.mLogger.info("RLOS COMMON"+" after adding ref_relationship+ "+ref_relationship);
									}


									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+ref_phone_no
									+"</"+ tag_name+"><ref_relationship>"+ref_relationship+"</ref_relationship>";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}

							else if(tag_name.equalsIgnoreCase("confirmed_in_job") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

									if (confirmedinjob!=null){
										if (confirmedinjob.equalsIgnoreCase("true")){
											confirmedinjob="Y";
										}
										else{
											confirmedinjob="N";
										}

										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+confirmedinjob
										+"</"+ tag_name+">";

										CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("included_pl_aloc") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String included_pl_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");

									if (included_pl_aloc!=null){
										if (included_pl_aloc.equalsIgnoreCase("true")){
											included_pl_aloc="Y";
										}
										else{
											included_pl_aloc="N";
										}

										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+included_pl_aloc
										+"</"+ tag_name+">";

										CreditCard.mLogger.info("RLOS COMMON"+" after adding included_pl_aloc+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
								}
							}
							else if(tag_name.equalsIgnoreCase("included_cc_aloc") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String included_cc_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");

									if (included_cc_aloc!=null){
										if (included_cc_aloc.equalsIgnoreCase("true")){
											included_cc_aloc="Y";
										}
										else{
											included_cc_aloc="N";
										}

										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+included_cc_aloc
										+"</"+ tag_name+">";

										CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
								}
							}
							else if(tag_name.equalsIgnoreCase("vip_flag") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String vip_flag=formObject.getNGValue("cmplx_Customer_VIPFlag");


									if (vip_flag.equalsIgnoreCase("true")){
										vip_flag="Y";
									}
									else{
										vip_flag="N";
									}

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+vip_flag
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("standing_instruction") && Call_name.equalsIgnoreCase("DECTECH")){
								CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE standing_instruction+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE Child_wi='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+squerynoc);
								String standing_instruction="";
								standing_instruction=NOC.get(0).get(0);
								if (NOC!=null && NOC.size()>0){
									String xml_str =  int_xml.get(parent_tag);
									if (standing_instruction.equalsIgnoreCase("0")){
										standing_instruction="N";
									}
									else{
										standing_instruction="Y";
									}

									xml_str = xml_str+ "<"+tag_name+">"+standing_instruction
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding standing_instruction+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							/*	else if(tag_name.equalsIgnoreCase("avg_credit_turnover_6") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{	
									int avg_credit_turnover6th=0;
									String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
									String avg_credit_turnover6=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
									if ((!avg_credit_turnover6.equalsIgnoreCase(null))&&(!avg_credit_turnover6.equalsIgnoreCase(""))){
									 avg_credit_turnover6th=Integer.parseInt(avg_credit_turnover6);
									}
									//String avg_credit_turnover3="";
									String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
									String avg_bal6=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
									int avg_bal6th=Integer.parseInt(avg_bal6);
									if (avg_credit_turnover_freq.equalsIgnoreCase("Annually")){
										avg_credit_turnover6th=avg_credit_turnover6th/2;
									}
									else if (avg_credit_turnover_freq.equalsIgnoreCase("Monthly")){
										avg_credit_turnover6th=6*avg_credit_turnover6th;
									}
									else if (avg_credit_turnover_freq.equalsIgnoreCase("Quarterly")){
										avg_credit_turnover6th=2*avg_credit_turnover6th;
									}
									if (avg_bal_freq.equalsIgnoreCase("Annually")){
										avg_bal6th=avg_bal6th/2;
									}
									else if (avg_bal_freq.equalsIgnoreCase("Monthly")){
										avg_bal6th=6*avg_bal6th;
									}
									else if (avg_bal_freq.equalsIgnoreCase("Quarterly")){
										avg_bal6th=2*avg_bal6th;
									}	

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover6th
									+"</"+ tag_name+"><avg_credit_turnover_3>"+avg_credit_turnover6th/2+"</avg_credit_turnover_3><avg_bal_3>"+avg_bal6th+"</avg_bal_3><avg_bal_6>"+avg_bal6th/2+"</avg_bal_6>";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							 */		/*	else if(tag_name.equalsIgnoreCase("aggregate_exposed") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String aeQuery = "SELECT A.CreditLimit, B.TotalOutstandingAmt,C.TotalAmount,D.TotalAmt FROM ng_RLOS_CUSTEXPOSE_CardDetails a FULL OUTER JOIN ng_RLOS_CUSTEXPOSE_LoanDetails B ON A.Wi_Name=B.Wi_Name FULL OUTER JOIN ng_rlos_cust_extexpo_CardDetails C ON C.Wi_Name=A.Wi_Name FULL OUTER JOIN ng_rlos_cust_extexpo_LoanDetails D ON D.Wi_Name=A.Wi_Name WHERE A.Wi_Name = '"+formObject.getWFWorkitemName()+"'";
									CreditCard.mLogger.info("aggregate_exposed sQuery"+aeQuery, "");
									List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);
									CreditCard.mLogger.info("aggregate_exposed list size"+aggregate_exposed.size(), "");
									float CreditLimit;
									float TotalOutstandingAmt;
									float TotalAmount;
									float TotalAmt;
									CreditCard.mLogger.info("outsidefor list ", "values");
									CreditLimit=0.0f;
									TotalOutstandingAmt=0.0f;
									TotalAmount=0.0f;
									TotalAmt=0.0f;
									float Total;
									CreditCard.mLogger.info("outsidefor list ", "values");
									Total=0.0f;

									for (int i = 0; i<aggregate_exposed.size();i++){


										if(aggregate_exposed.get(i).get(0)!=null && !aggregate_exposed.get(i).get(0).isEmpty() &&  !aggregate_exposed.get(i).get(0).equals("") && !aggregate_exposed.get(i).get(0).equalsIgnoreCase("null") ){
												CreditLimit = CreditLimit+  Float.parseFloat(aggregate_exposed.get(i).get(0));
										}
										if(aggregate_exposed.get(i).get(1)!=null && !aggregate_exposed.get(i).get(1).isEmpty() &&  !aggregate_exposed.get(i).get(1).equals("") && !aggregate_exposed.get(i).get(1).equalsIgnoreCase("null") ){
												TotalOutstandingAmt =TotalOutstandingAmt + Float.parseFloat(aggregate_exposed.get(i).get(1));
										}
										if(aggregate_exposed.get(i).get(2)!=null && !aggregate_exposed.get(i).get(2).isEmpty() &&  !aggregate_exposed.get(i).get(2).equals("") && !aggregate_exposed.get(i).get(2).equalsIgnoreCase("null") ){
												TotalAmount =TotalAmount+  Float.parseFloat(aggregate_exposed.get(i).get(2));
										}
										if(aggregate_exposed.get(i).get(3)!=null && !aggregate_exposed.get(i).get(3).isEmpty() &&  !aggregate_exposed.get(i).get(3).equals("") && !aggregate_exposed.get(i).get(3).equalsIgnoreCase("null") ){
												TotalAmt = TotalAmt+ Float.parseFloat(aggregate_exposed.get(i).get(3));
										}

									}
									Total=	CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt;

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+Total
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding aggregate_exposed+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							  */			
							else if(tag_name.equalsIgnoreCase("accomodation_provided") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String accomodation_provided=formObject.getNGValue("cmplx_IncomeDetails_Accomodation");

									if (accomodation_provided!=null){
										if (accomodation_provided.equalsIgnoreCase("Yes")){
											accomodation_provided="Y";
										}
										else{
											accomodation_provided="N";
										}

										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+accomodation_provided
										+"</"+ tag_name+">";

										CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
								}
							}
							else if(tag_name.equalsIgnoreCase("AccountDetails") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info("RLOS COMMON"+" before adding internal liability+ "+xml_str);
									xml_str = xml_str + getInternalLiabDetails();
									CreditCard.mLogger.info("RLOS COMMON"+" after internal liability+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("InternalBureau") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauData+ "+xml_str);
								String temp = InternalBureauData();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauData+ "+xml_str);
									int_xml.get(parent_tag);
									int_xml.put(parent_tag, xml_str);
								}


							}
							else if(tag_name.equalsIgnoreCase("InternalBouncedCheques") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBouncedCheques+ "+xml_str);
								String temp = InternalBouncedCheques();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info("RLOS COMMON"+" after InternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("InternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauIndividualProducts+ "+xml_str);
								String temp = InternalBureauIndividualProducts();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("InternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauPipelineProducts+ "+xml_str);
								String temp = InternalBureauPipelineProducts();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("ExternalBureau") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding ExternalBureau+ "+xml_str);
								String temp = ExternalBureauData();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureau+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("ExternalBouncedCheques") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding ExternalBouncedCheques+ "+xml_str);
								String temp = ExternalBouncedCheques();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                    	
							}
							else if(tag_name.equalsIgnoreCase("ExternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								String temp =  ExternalBureauIndividualProducts();
								CreditCard.mLogger.info("RLOS COMMON"+" value of temp to be adding temp+ "+temp);
								String Manual_add_Liab =  ExternalBureauManualAddIndividualProducts();

								if((!temp.equalsIgnoreCase("")) || (!Manual_add_Liab.equalsIgnoreCase(""))){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp + Manual_add_Liab;
									CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	                            	
							}
							else if(tag_name.equalsIgnoreCase("ExternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+xml_str);
								String temp =  ExternalBureauPipelineProducts();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										CreditCard.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                        	
							}
							//for Dectech


							else if(tag_name.equalsIgnoreCase("MinorFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
								if(int_xml.containsKey(parent_tag))
								{
									float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
									String age_flag = "N";
									if(Age<18)
										age_flag="Y";
									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+age_flag
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("NonResidentFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									String res_flag ="N";

									if(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")){
										res_flag="Y";
									}

									xml_str = xml_str + "<"+tag_name+">"+res_flag
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("RecipientAddress") && Call_name.equalsIgnoreCase("CHEQUE_BOOK_ELIGIBILITY") ){
								int add_len=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
								String add_res_val ="";
								String xml_str = int_xml.get(parent_tag);
								if(add_len>0){
									for(int i=0;i<add_len;i++){
										CreditCard.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
										if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("Home")){
											formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
											add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+" "+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);
										}
									}
									xml_str = xml_str + "<"+tag_name+">"+add_res_val
									+"</"+ tag_name+">";

									CreditCard.mLogger.info("RLOS COMMON"+" after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}
							}

							else if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
							{
								CreditCard.mLogger.info("inside 1st if"+"inside 1st if");
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info("inside 1st if"+"inside 2nd if");
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info("inside 1st if"+"inside 2nd if xml string"+xml_str);
									if(is_repetitive.equalsIgnoreCase("Y") && int_xml.containsKey(tag_name)){
										CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string");
										xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
										CreditCard.mLogger.info("CC_Common"+"value after adding "+ Call_name+": "+xml_str);
										CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string"+xml_str);
										int_xml.remove(tag_name);
										int_xml.put(tag_name, xml_str);
										CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string int_xml");
									}
									else{
										CreditCard.mLogger.info("inside else of parent tag"+"value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
										CreditCard.mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
										if(form_control.trim().equalsIgnoreCase("") && default_val.trim().equalsIgnoreCase("")){
											CreditCard.mLogger.info("inside if added by me"+"inside");
											xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											CreditCard.mLogger.info("added by xml"+"xml_str"+xml_str);
										}
										else if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).trim().equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null")))
										{
											CreditCard.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
											if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
												form_control_val = formObject.getNGValue(form_control).toUpperCase();
											}
											else
												form_control_val = formObject.getNGValue(form_control);

											if(!data_format12.equalsIgnoreCase("text")){
												String[] format_arr = data_format12.split(":");
												String format_name = format_arr[0];
												String format_type = format_arr[1];
												CreditCard.mLogger.info(""+"format_name"+format_name);
												CreditCard.mLogger.info(""+"format_type"+format_type);

												if(format_name.equalsIgnoreCase("date")){
													DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
													DateFormat df_new = new SimpleDateFormat(format_type);

													try {
														startDate = df.parse(form_control_val);
														form_control_val = df_new.format(startDate);
														CreditCard.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

													} catch (ParseException e) {
														CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
														e.printStackTrace();
													}
													catch (Exception e) {
														CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
														e.printStackTrace();
													}
												}
												else if(format_name.equalsIgnoreCase("number")){
													if(form_control_val.contains(",")){
														form_control_val = form_control_val.replace(",", "");
													}

												}
												//change here for other input format

											}
											CreditCard.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											CreditCard.mLogger.info("inside else of parent tag xml_str"+"xml_str"+ xml_str);
										}

										else if(default_val==null || default_val.trim().equalsIgnoreCase("")){
											CreditCard.mLogger.info("#RLOS Common GenerateXML IF part"+"no value found for tag name: "+ tag_name);
										}
										else{
											CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
											form_control_val = default_val;

											CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ xml_str);

										}
										//code change for to remove docdect incase ref no is not present start	                                       
										if(tag_name.equalsIgnoreCase("DocumentRefNumber") && parent_tag.equalsIgnoreCase("Document") && form_control_val.trim().equalsIgnoreCase("")){
											if(xml_str.contains("</Document>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</Document>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if(tag_name.equalsIgnoreCase("DocRefNum") && parent_tag.equalsIgnoreCase("DocDetails") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</DocDetails>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDetails>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if(tag_name.equalsIgnoreCase("PhnLocalCode") && parent_tag.equalsIgnoreCase("PhnDetails") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</PhnDetails>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if(tag_name.equalsIgnoreCase("Email") && parent_tag.equalsIgnoreCase("EmlDet") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</EmlDet>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</EmlDet>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if(tag_name.equalsIgnoreCase("DocNo") && parent_tag.equalsIgnoreCase("DocDet") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</DocDet>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDet>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else{
											int_xml.put(parent_tag, xml_str);
										}
										//code change for to remove docdect incase ref no is not present end
										//int_xml.put(parent_tag, xml_str);
										CreditCard.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+xml_str);

									}

								}
								else{
									String new_xml_str ="";
									CreditCard.mLogger.info("inside else of parent tag main 2"+"value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
									CreditCard.mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
									if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).trim().equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null"))){
										CreditCard.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
										if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
											form_control_val = formObject.getNGValue(form_control).toUpperCase();
										}
										else
											form_control_val = formObject.getNGValue(form_control);
										if(!data_format12.equalsIgnoreCase("text")){
											String[] format_arr = data_format12.split(":");
											String format_name = format_arr[0];
											String format_type = format_arr[1];
											if(format_name.equalsIgnoreCase("date")){
												DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
												DateFormat df_new = new SimpleDateFormat(format_type);
												// java.util.Date startDate;
												try {
													startDate = df.parse(form_control_val);
													form_control_val = df_new.format(startDate);
													CreditCard.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

												} catch (ParseException e) {
													CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
													e.printStackTrace();
												}
												catch (Exception e) {
													CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
													e.printStackTrace();
												}

											}
											//Changes Done to accept data format as number start
											else if(format_name.equalsIgnoreCase("number")){
												if(form_control_val.contains(",")){
													form_control_val = form_control_val.replace(",", "");
												}

											}
											//Changes Done to accept data format as number END	
											//change here for other input format

										}
										CreditCard.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										CreditCard.mLogger.info("inside else of parent tag xml_str"+"new_xml_str"+ new_xml_str);
									}

									else if(default_val==null || default_val.trim().equalsIgnoreCase("")){
										if(int_xml.containsKey(parent_tag)|| is_repetitive.equalsIgnoreCase("Y")){
											new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
										}
										CreditCard.mLogger.info("#RLOS Common GenerateXML Inside Else Part"+"no value found for tag name: "+ tag_name);
									}
									else{
										CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
										form_control_val = default_val;
										CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ new_xml_str);

									}
									int_xml.put(parent_tag, new_xml_str);
									CreditCard.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+new_xml_str);

								}
							}

						}


						final_xml=final_xml.append("<").append(parentTagName).append(">");
						CreditCard.mLogger.info("RLOS"+"Final XMLold--"+final_xml);

						Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
						CreditCard.mLogger.info("itr of hashmap"+"itr"+itr);
						while (itr.hasNext())
						{
							Map.Entry<String, String> entry =  itr.next();
							CreditCard.mLogger.info("entry of hashmap"+"entry"+entry);
							if(final_xml.indexOf((entry.getKey()))>-1){
								CreditCard.mLogger.info("RLOS"+"itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
								CreditCard.mLogger.info("value of final xml"+"final_xml"+final_xml);
								itr.remove();
							}

						}    
						final_xml=final_xml.append("</").append(parentTagName).append(">");
						CreditCard.mLogger.info("CC_Common"+"final_xml: "+final_xml);
						CreditCard.mLogger.info("FInal XMLnew is: "+ final_xml.toString());
						final_xml.insert(0, header);
						final_xml.append(footer);
						CreditCard.mLogger.info("FInal XMLnew with header: "+final_xml.toString());
						formObject.setNGValue("Is_"+callName,"Y");
						CreditCard.mLogger.info("value of "+callName+" Flag: "+formObject.getNGValue("Is_"+callName));

						//added above code
						try{


						}
						catch(Exception e){

						}
						cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
						CreditCard.mLogger.info("$$outputgGridtXML "+"cabinetName "+cabinetName);
						wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
						ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
						CreditCard.mLogger.info("$$outputgGridtXML "+"ActivityName "+ws_name);
						sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
						userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
						CreditCard.mLogger.info("$$outputgGridtXML "+"userName "+userName);
						CreditCard.mLogger.info("$$outputgGridtXML "+"sessionID "+sessionID);



						String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
						List<List<String>> outputMQXML=formObject.getDataFromDataSource(sMQuery);
						CreditCard.mLogger.info("$$outputgGridtXML "+"sMQuery "+sMQuery);
						if(!outputMQXML.isEmpty()){
							CreditCard.mLogger.info("$$outputgGridtXML "+outputMQXML.get(0).get(0)+","+outputMQXML.get(0).get(1));
							socketServerIP =  outputMQXML.get(0).get(0);
							CreditCard.mLogger.info("$$outputgGridtXML "+"socketServerIP "+socketServerIP);
							socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
							CreditCard.mLogger.info("$$outputgGridtXML "+"socketServerPort "+socketServerPort);
							if(!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0)))
							{
								socket = new Socket(socketServerIP, socketServerPort);
								out = socket.getOutputStream();
								socketInputStream = socket.getInputStream();
								dout = new DataOutputStream(out);
								din = new DataInputStream(socketInputStream);
								mqOutputResponse="";	     
								mqInputRequest= getMQInputXML(sessionID,cabinetName,wi_name,ws_name,userName,final_xml);
								CreditCard.mLogger.info("$$outputgGridtXML "+"mqInputRequest "+mqInputRequest);

								if (mqInputRequest != null && mqInputRequest.length() > 0) 
								{
									int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
									CreditCard.mLogger.info("Final XML output len: "+outPut_len+"");
									mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
									CreditCard.mLogger.info("MqInputRequest"+"Input Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
									dout.write(mqInputRequest.getBytes("UTF-16LE"));
									dout.flush();                
								}
								byte[] readBuffer = new byte[50000];
								int num = din.read(readBuffer);
								boolean wait_flag = true;
								int out_len=0;

								if (num > 0) 
								{
									while(wait_flag){
										CreditCard.mLogger.info("MqOutputRequest"+"num :"+num);
										byte[] arrayBytes = new byte[num];
										System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
										mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
										CreditCard.mLogger.info("MqOutputRequest"+"inside loop output Response :\n"+mqOutputResponse);
										if(mqOutputResponse.contains("##8##;")){
											String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
											mqOutputResponse = mqOutputResponse_arr[1];
											out_len = Integer.parseInt(mqOutputResponse_arr[0]);
											CreditCard.mLogger.info("MqOutputRequest"+"First Output Response :\n"+mqOutputResponse);
											CreditCard.mLogger.info("MqOutputRequest"+"Output length :\n"+out_len);
										}
										if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
											wait_flag=false;
										}
										Thread.sleep(100);
										num = din.read(readBuffer);

									}
									// Aman Code added for dectech to replace the &lt and &gt start 13 sept 2017
									if(mqOutputResponse.contains("&lt;")){
										CreditCard.mLogger.info("MqOutputRequest"+"inside for Dectech :\n"+mqOutputResponse);
										mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
										CreditCard.mLogger.info("MqOutputRequest"+"after replacing lt :\n"+mqOutputResponse);
										mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
										CreditCard.mLogger.info("MqOutputRequest"+"after replacing gt :\n"+mqOutputResponse);
									}
									// Aman Code added for dectech to replace the &lt and &gt end 13 sept 2017

									CreditCard.mLogger.info("MqOutputRequest"+"Final Output Response :\n"+mqOutputResponse);
									socket.close();
									return mqOutputResponse;
								}


							}
							else{
								CreditCard.mLogger.info("SocketServerIp and SocketServerPort is not maintained "+"");
								CreditCard.mLogger.info("SocketServerIp is not maintained "+socketServerIP);
								CreditCard.mLogger.info(" SocketServerPort is not maintained "+socketServerPort.toString());
								return "MQ details not maintained";
							}
						}
						else{
							CreditCard.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
							return "MQ details not maintained";
						}
					}

				}
				else {
					CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}


			}
			else{
				CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in Master table for : "+callName);
				return "Call not maintained";
			}
		}
		catch (UnknownHostException e) 
		{        
			e.printStackTrace();
			return "0";
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception ocurred: "+e.getLocalizedMessage());
			CreditCard.mLogger.info("CC_Common"+"$$final_xml: "+final_xml);
			CreditCard.mLogger.info("CC_Common"+"Exception occured in main thread: "+ e.getMessage());
			return "0";
		}    
		return "";
	} 

	public String getYearsDifference(FormReference formObject,String controlName) {
		int MON;
		int Year;
		String age="";
		String DOB=formObject.getNGValue(controlName);
		CreditCard.mLogger.info(" Inside age + "+DOB);
		String CurrDate= new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		if (DOB!=null){	
			String[] Dob=DOB.split("/");
			String[] CurreDate=CurrDate.split("/");
			int monthbirthDate=Integer.parseInt(Dob[1]);
			int monthcurrDate=Integer.parseInt(CurreDate[1]);
			int YearbirthDate=Integer.parseInt(Dob[2]);
			int yearcurrDate=Integer.parseInt(CurreDate[2]);
			if (monthcurrDate<monthbirthDate){
				yearcurrDate=yearcurrDate-1;
				Year=yearcurrDate-YearbirthDate;
				MON=monthcurrDate-monthbirthDate;
				MON=12+MON;
				if ((MON==10)||(MON==11)){
					age=Year+"."+MON;}
				else{
					age=Year+".0"+MON;
				}

			}
			else if (monthcurrDate>monthbirthDate){
				Year=yearcurrDate-YearbirthDate;
				MON=monthcurrDate-monthbirthDate;
				if ((MON==10)||(MON==11)){
					age=Year+"."+MON;}
				else{
					age=Year+".0"+MON;
				}
			}
			else {
				Year=yearcurrDate-YearbirthDate;
				if(Year<10){
					age="0"+Year+".00";
				}
				else{
					age=Year+".00";
				}

			}
		}
		return age;
	}
	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuffer final_xml) 
	{
		FormContext.getCurrentInstance().getFormConfig( );

		StringBuffer strBuff=new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>"+sessionID+"</SessionId>");
		strBuff.append("<EngineName>"+cabinetName+"</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_CC_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>"+wi_name+"</WI_NAME>");
		strBuff.append("<WS_NAME>"+ws_name+"</WS_NAME>");
		strBuff.append("<USER_NAME>"+userName+"</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);		
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");	
		CreditCard.mLogger.info("inside getOutputXMLValues"+"getMQInputXML"+strBuff.toString());
		return strBuff.toString();
	}


	public void valueSetCustomer(String outputResponse ,String operationName)
	{
		CreditCard.mLogger.info("RLOSCommon valueSetCustomer"+ "Inside valueSetCustomer():");
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		//String Operation_name="";
		String Call_type="";
		XMLParser objXMLParser = new XMLParser();
		try
		{
			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
				CreditCard.mLogger.info("RLOSCommon valueSetCustomer"+ "outputXMLHead");
			}
			objXMLParser.setInputXML(outputXMLHead);
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				CreditCard.mLogger.info("$$response "+response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				CreditCard.mLogger.info("$$returnDesc "+returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				CreditCard.mLogger.info("$$returnCode "+returnCode);
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
				//CreditCard.mLogger.info("$$outputXMLMsg "+outputXMLMsg);
				CreditCard.mLogger.info("$$outputXMLMsg getOutputXMLValues"+"check inside getOutputXMLValues");
				//getOutputXMLValues(outputXMLMsg,response);
				getOutputXMLValues(outputXMLMsg,response,operationName);
				CreditCard.mLogger.info("$$outputXMLMsg "+"outputXMLMsg");
			}
			//ended by me
			Call_type= outputResponse.substring(outputResponse.indexOf("<CallType>")+10,outputResponse.indexOf("</CallType>"));
			if	(Call_type!= null){
				if (Call_type.equals("CA")){
					dectechfromeligbility(outputResponse);
				}
				if (Call_type.equals("PM")){
					dectechfromeligbility(outputResponse);
				}
			}
		}
		catch(Exception e)
		{            
			CreditCard.mLogger.info("Exception occured in valueSetCustomer method:  "+e.getMessage());
			
		}
	}
	public static void dectechfromeligbility(String outputResponse){
		try{
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				String squery="";
				String Output_TAI="";
				String Output_Decision="";
				String Output_Final_DBR="";
				String Output_Existing_DBR  ="";
				String Output_Eligible_Amount="";
				String Output_Delegation_Authority="";
				String output_accomodation="";
				String Grade="";
				String Output_Interest_Rate="";
				String Output_Net_Salary_DBR="";
				String ReasonCode="";
				String DeviationCode="";
				String Output_Accommodation_Allowance="";
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml = "";
				String sGeneralData = formObject.getWFGeneralData();
				CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+sGeneralData);
				double cac_calc_limit=0.0;
				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>")); ;
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>")); ;
				String Output_Affordable_EMI="";
				//Setting the value in ELIGANDPROD info
				if (outputResponse.contains("Output_TAI")){
					Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
					if (Output_TAI!=null){
						try{
							formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI);

						}
						catch (Exception e){
							CreditCard.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
						}


					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+Output_TAI);
				}
				
				boolean DecFragVis=formObject.isVisible("DecisionHistory_Frame1");
				CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+DecFragVis);
				if (DecFragVis==false){
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
					
					new CC_Common().Decision_cadanalyst1();
					
				}
				
				if (outputResponse.contains("Output_Delegation_Authority")){
					Output_Delegation_Authority = outputResponse.substring(outputResponse.indexOf("<Output_Delegation_Authority>")+29,outputResponse.indexOf("</Output_Delegation_Authority>")); ;
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
					formObject.setNGValue("cmplx_DEC_HighDeligatinAuth", Output_Delegation_Authority);
				}
				//Added by aman for PROC-2535
				if (outputResponse.contains("Output_Accommodation_Allowance")){
					Output_Accommodation_Allowance = outputResponse.substring(outputResponse.indexOf("<Output_Accommodation_Allowance>")+32,outputResponse.indexOf("</Output_Accommodation_Allowance>")); ;
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Accommodation_Allowance"+Output_Accommodation_Allowance);
					formObject.setNGValue("cmplx_IncomeDetails_CompanyAcc", Output_Accommodation_Allowance);
				}
				//Added by aman for PROC-2535
				if (outputResponse.contains("Output_Decision")){
					Output_Decision = outputResponse.substring(outputResponse.indexOf("<Output_Decision>")+17,outputResponse.indexOf("</Output_Decision>"));
					if (Output_Decision!=null){
						try{
							if (Output_Decision.equalsIgnoreCase("D")){
								Output_Decision="Declined";
							}	
							else if (Output_Decision.equalsIgnoreCase("A")){
								Output_Decision="Approve";
							}	
							else if (Output_Decision.equalsIgnoreCase("R")){
								Output_Decision="Refer";
							}	
							formObject.setNGValue("cmplx_DEC_DectechDecision", Output_Decision);


						}
						catch (Exception e){
							CreditCard.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
						}


					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+Output_TAI);
				}
				
				if (outputResponse.contains("Output_Eligible_Cards")){
					try{
						String Output_Eligible_Cards = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Cards>")+23,outputResponse.indexOf("</Output_Eligible_Cards>"));
						Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
						String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
						strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);

						CreditCard.mLogger.info("Output_Eligible_Cards"+ "after removing the starting and Trailing:"+Output_Eligible_Cards);
						String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
						for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
							String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array);
							String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
							String[] Limit=Output_Eligible_Cards_Array[1].split(":");
							String[] flag=Output_Eligible_Cards_Array[2].split(":");
							String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
							String LIMIT= Limit[1];
							String FLAG= flag[1].substring(1,flag[1].length()-1);

							CreditCard.mLogger.info("Output_Eligible_Cards"+ "Card_Prod:"+Card_Prod[1]);
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "Limit:"+Limit[1]);
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "flag:"+flag[1]);
							String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit VALUES ('"+Card_Product+"','"+LIMIT+"','','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"')";
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "QUERY:"+query);
							formObject.saveDataIntoDataSource(query);

							/* for (int j=0; j<Output_Eligible_Cards_Array.length;j++){
	    					 String[] values=Output_Eligible_Cards_Array[j].split(":");
	    					 CreditCard.mLogger.info("Output_Eligible_Cards"+ "values:"+values);
	    					  if(values[0].contains("\"")){
	    						  values[0]=values[0].substring(1, values[0].length()-1);
	    				    	}
	    					  if(values[1].contains("\"")){
	    						  values[1]=values[1].substring(1, values[1].length()-1);
	    				    	}
	    					  String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
	    					  strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
	    					  String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit("+values[0]+") VALUES ('"+values[1]+"')";
	    					  CreditCard.mLogger.info("Output_Eligible_Cards"+ "QUERY:"+query);
	    			    	  formObject.saveDataIntoDataSource(query);
	    				  }*/
						}
					}
					catch(Exception e){
						CreditCard.mLogger.info("RLOSCommon"+ "Exception occurred in elig dectech"+printException(e));

					}
				}	
				if (outputResponse.contains("Output_Final_DBR")){

					Output_Final_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Final_DBR>")+18,outputResponse.indexOf("</Output_Final_DBR>"));
					if (Output_Final_DBR!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				if (outputResponse.contains("Output_Interest_Rate")){
					Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("<Output_Interest_Rate>")+22,outputResponse.indexOf("</Output_Interest_Rate>")); ;
					if (Output_Interest_Rate!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Interest_Rate"+Output_Interest_Rate);

				}
				//Setting the value in ELIGANDPROD info
				//Setting the value in lIABILITY info
				if (outputResponse.contains("Output_Existing_DBR")){
					Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>")); ;
					if (Output_Existing_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

				}
				if (outputResponse.contains("Output_Affordable_EMI")){
					Output_Affordable_EMI = outputResponse.substring(outputResponse.indexOf("<Output_Affordable_EMI>")+23,outputResponse.indexOf("</Output_Affordable_EMI>")); ;
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

				}
				try{
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
					cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+tenor+" "+RateofInt+" "+out_aff_emi+" "+cac_calc_limit);
					}
					catch(Exception e){}

				if (outputResponse.contains("Output_Net_Salary_DBR")){
					Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>")); ;
					if (Output_Net_Salary_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);

				}
				//Setting the value in lIABILITY info
				//Setting the value in creditCard iFrame
				
				
				if (outputResponse.contains("output_accomodation")){
					output_accomodation = outputResponse.substring(outputResponse.indexOf("<output_accomodation>")+20,outputResponse.indexOf("</output_accomodation>")); ;
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+output_accomodation);
					if (output_accomodation!=null){
						formObject.setNGValue("cmplx_IncomeDetails_CompanyAcc", output_accomodation);
					}
				}
				if (outputResponse.contains("Grade")){
					Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>")); ;
					if (Grade!=null){
			    		formObject.setNGValue("cmplx_DEC_ScoreGrade", Grade);
			    	}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Grade"+Grade);

				}
				if (outputResponse.contains("Output_Eligible_Amount")){
					Output_Eligible_Amount = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Amount>")+24,outputResponse.indexOf("</Output_Eligible_Amount>")); ;
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);

				}
				//Setting the value in creditCard iFrame


				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//CreditCard.mLogger.info("$$Value set for DECTECH->>"+"UpdateinputXML is:"+UpdateinputXML);
				String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","Child_Wi ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

				strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
				CreditCard.mLogger.info("$$Value set for DECTECH->>"+"strOutputXml is:"+strOutputXml);

				String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
				String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
				CreditCard.mLogger.info("$$Value set for DECTECH->>"+"mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);

				outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				CreditCard.mLogger.info("$$outputResponse "+"inside outpute get outputResponse"+outputResponse);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(outputResponse));

				Document doc = builder.parse(is);
				doc.getDocumentElement().normalize();

				CreditCard.mLogger.info("Root element :" +doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("PM_Reason_Codes");
				

				for (int temp = 0; temp < nList.getLength(); temp++) {
					String Reason_Decision="";
					Node nNode = nList.item(temp);
					

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//CreditCard.mLogger.info("Student roll no : " + eElement.getAttribute("rollno"));
						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());

						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());
						String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);//2


						ReasonCode= eElement.getElementsByTagName("Reason_Code").item(0).getTextContent();
						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						try{
							if(temp==0){
								
								DeviationCode=ReasonCode;}
							else{
								DeviationCode=DeviationCode+","+ReasonCode;}
							}
						catch(Exception e){
							
						}CreditCard.mLogger.info("$$outputResponse "+"Value of Reason_Decision"+Reason_Decision);
						if (temp==0){
							if (Reason_Decision.equalsIgnoreCase("D")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
							if (Reason_Decision.equalsIgnoreCase("R")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
							if (Reason_Decision.equalsIgnoreCase("A")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
						}
						else{
							if (Reason_Decision.equalsIgnoreCase("D")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
							if (Reason_Decision.equalsIgnoreCase("R")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
							if (Reason_Decision.equalsIgnoreCase("A")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
						}
						CreditCard.mLogger.info("$$outputResponse "+"Squery is"+squery);
						formObject.saveDataIntoDataSource(squery);

					}
				} formObject.setNGValue("cmplx_DEC_DeviationCode", DeviationCode); 
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());

		}

	}


	public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		try
		{   
			CreditCard.mLogger.info("inside getOutputXMLValues"+"inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";

			//  sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"'";
			//code added here
			if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
				CreditCard.mLogger.info("inside if of operation"+"operation111"+Operation_name);
				CreditCard.mLogger.info("inside if of operation"+"callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_CC_Integration_Field_Response where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"'" ;
				CreditCard.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
			}
			else{
				sQuery = "SELECT "+col_name+" FROM NG_CC_Integration_Field_Response where Call_Name ='"+response+"'";
			}

			//ended here
			List<List<String>> outputTableXML=formObject.getDataFromDataSource(sQuery);


			String[] col_name_arr = col_name.split(",");

			CreditCard.mLogger.info("$$outputTableXML"+outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
			int n=outputTableXML.size();
			CreditCard.mLogger.info("outputTableXML size: " + n+"");

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
			CreditCard.mLogger.info("name is doc : "+ doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			if( n> 0)
			{
				new LinkedHashMap<String, String>();
				Map<String, String> responseFileMap = new HashMap<String, String>();
				for(List<String> mylist:outputTableXML)
				{
					for(int i=0;i<col_name_arr.length;i++)
					{
						responseFileMap.put(col_name_arr[i],mylist.get(i));
					}
					String form_control = (String) responseFileMap.get("Form_Control");
					String parent_tag = (String) responseFileMap.get("Parent_Tag_Name");
					String fielddbxml_tag = (String) responseFileMap.get("XmlTag_Name");
					String Grid_col_tag = (String) responseFileMap.get("grid_table_xml_tags");
					CreditCard.mLogger.info(" Grid_col_tag"+"Grid_col_tag"+Grid_col_tag);
					if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
					{                    
						for (int i = 0; i < nl.getLength(); i++)
						{
							nl.item(i);
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
								CreditCard.mLogger.info("RLOS Common# getOutputXMLValues()"+" fielddbxml_tag: "+fielddbxml_tag);
								String indirectMapping = (String) responseFileMap.get("InDirect_Mapping");
								String gridMapping = (String) responseFileMap.get("Grid_Mapping");

								if(gridMapping.equalsIgnoreCase("Y"))
								{
									CreditCard.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
									if(Grid_col_tag.contains(",")){
										String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
										//String grid_detail_str = nl.item(i).getNodeValue();
										NodeList childnode  = nl.item(i).getChildNodes();
										CreditCard.mLogger.info("Grid_col_tag_arr"+"Grid_col_tag_arr: "+Grid_col_tag);   
										CreditCard.mLogger.info("childnode"+"childnode"+childnode); 
										List<String> Grid_row = new ArrayList<String>(); 
										Grid_row.clear();

										String flaga="N";
										for(int k = 0;k<Grid_col_tag_arr.length;k++){

											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
													CreditCard.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getNodeName());
													CreditCard.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getTextContent());
													if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("AddrPrefFlag"))
													{
														if(childnode.item(child_node_len).getTextContent().equalsIgnoreCase("Y"))
															Grid_row.add("true");

														else if(childnode.item(child_node_len).getTextContent().equalsIgnoreCase("N"))
															Grid_row.add("false");

													}
													else
														Grid_row.add(childnode.item(child_node_len).getTextContent());
													flaga="Y";
													break;
												}                                            
											}

											if(flaga.equalsIgnoreCase("N") ){
												CreditCard.mLogger.info("child_node_len "+"Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												// CreditCard.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getTextContent());
												Grid_row.add("NA");

											}
											flaga="N";

										}
										//Grid_row.add("true");
										Grid_row.add(formObject.getWFWorkitemName());
										//Grid_col_tag_arr[k]
										//code to add row in grid. and pass Grid_row in that.
										CreditCard.mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added in address grid: "+ Grid_row.toString());

										if(fielddbxml_tag.equalsIgnoreCase("AddrDet")){ 
											formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);


										}
									}
									else{
										CreditCard.mLogger.info("RLOS Common# getOutputXMLValues()"+ "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
									}

								}
								else if(indirectMapping.equalsIgnoreCase("Y")){
									CreditCard.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
									String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
									//code added
									if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
										CreditCard.mLogger.info("inside if of operation"+"operation111"+Operation_name);
										CreditCard.mLogger.info("inside if of operation"+"callName111"+col_name);
										sQuery = "SELECT "+col_list+" FROM NG_CC_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
										CreditCard.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
									}

									else{
										sQuery = "SELECT "+col_list+" FROM NG_CC_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "query: "+sQuery);
									}
									List<List<String>> outputindirect=formObject.getDataFromDataSource(sQuery);
									CreditCard.mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "1");
									String col_list_arr[] = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "inside list loop");

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "inside put map"+x);
										}
										String xmltag_name = gridResponseMap.get("xmltag_name").toString();
										String tag_value = gridResponseMap.get("tag_value").toString();
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
										String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list").toString();
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
										String indirect_form_control = gridResponseMap.get("form_control").toString();
										CreditCard.mLogger.info("indirect_form_control in string"+indirect_form_control );
										String indirect_val = gridResponseMap.get("indirect_val").toString();
										String IS_Master = gridResponseMap.get("IS_Master").toString();
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "IS_Master"+IS_Master);
										String Master_Name = gridResponseMap.get("Master_Name").toString();
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "Master_Name"+Master_Name);
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "all details fetched");
										if(IS_Master.equalsIgnoreCase("Y")){
											String code = nl.item(i).getTextContent();
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping code:"+code);
											sQuery= "Select description from "+Master_Name+" where Code='"+code+"'";
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getDataFromDataSource(sQuery);
											String value=query.get(0).get(0);
											CreditCard.mLogger.info("#query.get(0).get(0)"+value );
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value);
											CreditCard.mLogger.info("indirect_form_control value"+formObject.getNGValue(indirect_form_control));
										}

										else if(indirect_form_control!=null && !indirect_form_control.equalsIgnoreCase("")){
											if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
												CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
												formObject.setNGValue(indirect_form_control,indirect_val,false);
											}
											// CreditCard.mLogger.info("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);

										}

										else{
											CreditCard.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping part2 else");
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
												CreditCard.mLogger.info("childnode"+"childnode"+childnode);
												if(indirect_tag_list.contains(",")){
													CreditCard.mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list with ,");
													String indirect_tag_list_arr[] = indirect_tag_list.split(",");
													String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
															CreditCard.mLogger.info("#RLOS Common inside child node 1 "+ "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(1).getTextContent());
															if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
																//Ref. 1006
																for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){
																	//Ref. 1006 end.
																	CreditCard.mLogger.info("#RLOS common: "+ "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

																		CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																		CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																		CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"");
																		CreditCard.mLogger.info(""+indirect_formfield_list_arr[k]+" :"+childnode.item(child_node_len).getTextContent());
																		formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																	}
																}

															}
														}
													}
													else{
														CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
													} 
												}
												else{
													CreditCard.mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list without ,");
													for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
														if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
																CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"");
																CreditCard.mLogger.info(""+indirect_formfield_list+" :"+childnode.item(child_node_len).getTextContent());
																formObject.setNGValue(indirect_formfield_list,childnode.item(child_node_len).getTextContent(),false);
															}
														}

													}
												}


											}
										}     
									}
									//List<List<String>> outputIndirectXML=formObject.getNGDataFromDataCache(sQuery);
									//CreditCard.mLogger.info("$$outputIndirectXML "+outputIndirectXML.get(0).get(0)+outputIndirectXML.get(0).get(1)+outputIndirectXML.get(0).get(2));

								}
								if(indirectMapping.equalsIgnoreCase("N") && gridMapping.equalsIgnoreCase("N"))
								{    
									CreditCard.mLogger.info("check14 " +"check");
									tagValue = getTagValue(outputXMLMsg,nl.item(i).getNodeName());
									CreditCard.mLogger.info("Node value "+"tagValue:"+tagValue);
									CreditCard.mLogger.info("Node form_control "+"form_control:"+ form_control);

									CreditCard.mLogger.info("$$tagValue NN  "+tagValue);
									CreditCard.mLogger.info("$$form_control  NN "+form_control);
									formObject.setNGValue(form_control,tagValue,false);
								}
							}
						}
					}
					//till for loop

				}
			}
		}
		catch(Exception e)
		{
			CreditCard.mLogger.info("Exception occured in getOutputXMLValues:  "+e.getMessage());

		}
	}

	public static void parseDedupe_summary(String outxml){
		try{
		outxml=outxml.substring(outxml.indexOf("<MQ_RESPONSE_XML>")+17,outxml.indexOf("</MQ_RESPONSE_XML>"));
		

		String tagName= "Customer";		
		String subTagName= "Document,EmailAddress,PhoneFax,StatusInfo";
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int dedupe_row_count = formObject.getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
		if(dedupe_row_count>0){
			for(int i=0;i<dedupe_row_count;i++)
			{
				try {
					formObject.removeItem("cmplx_PartMatch_cmplx_Partmatch_grid", dedupe_row_count);
				} catch (Exception e) {
					CreditCard.mLogger.info( "Exception occured while removing data from Grid"+e.getMessage());
				}
			}

		}

		String [] valueArr=null;
		Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
		tagValuesMap=getTagData_dedupeSummary(outxml,tagName,subTagName);
		Map<String, String> map = tagValuesMap;
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			valueArr=entry.getValue().split("~");
			String[] col_name = valueArr[0].split(":,#:");
			String[] col_val = valueArr[1].split(":,#:");
			String cif_id = "NA";
			String CIF_Status= "NA";
			String First_name= "NA";
			String Last_name= "NA";
			String Full_name= "NA";
			String Passport_no= "NA";
			String old_passport_no= "NA";
			String visa_no= "NA";
			String mobile_no1= "NA";
			String mobile_no2= "NA";
			String dOB= "NA";
			String Eid= "NA";
			String DL= "NA";
			String nationality= "NA";
			String company_name= "NA";
			String TL_no= "NA";
			List<String> Grid_row = new ArrayList<String>();
			for(int i =0; i<col_name.length;i++){
				if(col_name[i].equalsIgnoreCase("CIFID"))
					cif_id = col_val[i];
				else if(col_name[i].equalsIgnoreCase("Suspended")){
					CIF_Status = col_val[i];

					if(CIF_Status.equalsIgnoreCase("Y"))
						CIF_Status="N";
					else
						CIF_Status="Y";
				}
				else if(col_name[i].equalsIgnoreCase("FirstName"))
					First_name = col_val[i];
				else if(col_name[i].equalsIgnoreCase("LastName"))
					Last_name = col_val[i];
				else if(col_name[i].equalsIgnoreCase("FullName"))
					Full_name = col_val[i];
				else if(col_name[i].equalsIgnoreCase("PPT"))
					Passport_no = col_val[i];
				else if(col_name[i].equalsIgnoreCase("OPPT"))
					old_passport_no = col_val[i];
				else if(col_name[i].equalsIgnoreCase("VISA"))
					visa_no = col_val[i];
				else if(col_name[i].equalsIgnoreCase("CELLPH1"))
					mobile_no1 = col_val[i];
				else if(col_name[i].equalsIgnoreCase("HOMEPH1"))
					mobile_no2 = col_val[i];
				else if(col_name[i].equalsIgnoreCase("DateOfBirth")){
					dOB = "";
					if (col_val[i]!=null && !col_val[i].equalsIgnoreCase("")){
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
						try{
							dOB = sdf2.format(sdf1.parse(col_val[i]));
							CreditCard.mLogger.info( "Dedupe Summary Date chabge: "+dOB);
						}
						catch(Exception e){
							CreditCard.mLogger.info( "Exception while parsing DOB in Dedupe summary"+e.getMessage());
							dOB="";
						}
					}
					else{
						dOB="";
					}
				}
				else if(col_name[i].equalsIgnoreCase("EMID"))
					Eid = col_val[i];
				else if(col_name[i].equalsIgnoreCase("DRILV"))
					DL = col_val[i];
				else if(col_name[i].equalsIgnoreCase("Nationality"))
					nationality = col_val[i];
				/* else if(col_name[i].equalsIgnoreCase("Suspended"))
					  company_name = col_val[i];*/
				else if(col_name[i].equalsIgnoreCase("TDLIC"))
					TL_no = col_val[i];
			}
			Grid_row.add(cif_id);
			Grid_row.add(CIF_Status);
			Grid_row.add(First_name);
			Grid_row.add(Last_name);
			Grid_row.add(Full_name);
			Grid_row.add(Passport_no);
			Grid_row.add(old_passport_no);
			Grid_row.add(visa_no);
			Grid_row.add(mobile_no1);
			Grid_row.add(mobile_no2);
			Grid_row.add("");
			//Grid_row.add(dOB);
			Grid_row.add(Eid);
			Grid_row.add(DL);
			Grid_row.add(nationality);
			Grid_row.add(company_name);
			Grid_row.add(TL_no);
			Grid_row.add(formObject.getWFWorkitemName());
			CreditCard.mLogger.info( Grid_row.toString());
			formObject.addItemFromList("cmplx_PartMatch_cmplx_Partmatch_grid", Grid_row);
		}
		}catch(Exception ex){
			CreditCard.mLogger.info( printException(ex));
		}
	}

	public static Map<String, String> getTagData_dedupeSummary(String parseXml,String tagName,String sub_tag){

		Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
		try {
			CreditCard.mLogger.info(parseXml);
			CreditCard.mLogger.info(tagName);
			CreditCard.mLogger.info(sub_tag);
			InputStream is = new ByteArrayInputStream(parseXml.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList_loan = doc.getElementsByTagName(tagName);
			for(int i = 0 ; i<nList_loan.getLength();i++){
				String col_name = "";
				String col_val ="";
				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				String id = ch_nodeList.item(0).getTextContent();
				for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					if(sub_tag.contains(ch_nodeList.item(ch_len).getNodeName())){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						if(col_name.equalsIgnoreCase("")){
							col_name = sub_ch_nodeList.item(0).getTextContent();
							col_val = sub_ch_nodeList.item(1).getTextContent();
						}
						else{
							col_name = col_name+":,#:"+sub_ch_nodeList.item(0).getTextContent();
							col_val = col_val+":,#:"+sub_ch_nodeList.item(1).getTextContent();
						}

					}
					else if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("PersonDetails")){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						for (int k =0;k<sub_ch_nodeList.getLength();k++){
							col_name = col_name+":,#:"+sub_ch_nodeList.item(k).getNodeName();
							col_val = col_val+":,#:"+sub_ch_nodeList.item(k).getTextContent()+"";
						}
					}
					else if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("ContactDetails")){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						for (int k =0;k<sub_ch_nodeList.getLength();k++){
							NodeList contact_sub_ch_nodeList =  sub_ch_nodeList.item(k).getChildNodes();
							for (int con_ch_len=0; con_ch_len<contact_sub_ch_nodeList.getLength();con_ch_len++){
								col_name = col_name+":,#:"+contact_sub_ch_nodeList.item(0).getTextContent();
								col_val = col_val+":,#:"+contact_sub_ch_nodeList.item(1).getTextContent();
							}
						}
					} 
					else{
						if(col_name.equalsIgnoreCase("")){
							col_name = ch_nodeList.item(ch_len).getNodeName();
							col_val = ch_nodeList.item(ch_len).getTextContent();
						}
						else{
							col_name = col_name+":,#:"+ch_nodeList.item(ch_len).getNodeName();
							col_val = col_val+":,#:"+ch_nodeList.item(ch_len).getTextContent();
						}

					}

				}
				CreditCard.mLogger.info(id);
				CreditCard.mLogger.info(col_name);
				CreditCard.mLogger.info(col_val);
				if(!col_name.equalsIgnoreCase(""))
					tagValuesMap.put(id, col_name+"~"+col_val);	
			}

		} catch (Exception e) {

			CreditCard.logException(e);
			CreditCard.mLogger.info(e.getMessage());
		}
		return tagValuesMap;
	}



	public static String getTagValue(String xml, String tag) throws ParserConfigurationException, SAXException, IOException 
	{   
		//CreditCard.mLogger.info("Tag:"+tag+" XML:"+xml);
		Document doc = getDocument(xml);
		NodeList nodeList = doc.getElementsByTagName(tag);

		int length = nodeList.getLength();
		//CreditCard.mLogger.info("NodeList Length: " + length);

		if (length > 0) {
			Node node =  nodeList.item(0);
			//CreditCard.mLogger.info("Node : " + node);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = node.getChildNodes();
				String value = "";
				int count = childNodes.getLength();
				for (int i = 0; i < count; i++) {
					Node item = childNodes.item(i);
					if (item.getNodeType() == Node.TEXT_NODE) {
						value += item.getNodeValue();
					}
				}
				return value;
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				return node.getNodeValue();
			}

		}
		return "";
	}

	public static Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException
	{
		Document doc=null;
		try
		{
		// Step 1: create a DocumentBuilderFactory
		DocumentBuilderFactory dbf =
			DocumentBuilderFactory.newInstance();

		// Step 2: create a DocumentBuilder
		DocumentBuilder db = dbf.newDocumentBuilder();

		// Step 3: parse the input file to get a Document object
		 doc = db.parse(new InputSource(new StringReader(xml)));
		}
		catch(Exception ex)
		{
			CreditCard.logException(ex);
		}
		finally
		{
			CreditCard.mLogger.info("Inside finally block of getDocument method");
		
		}
		return doc;
	}  



	//tanshu ended here

	public void fetchIncomingDocRepeater(){/*

		    CreditCard.mLogger.info( "inside fetchIncomingDocRepeater");

		    FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		    FormConfig formobject = FormContext.getCurrentInstance().getFormConfig();
		    String sActivityName = formobject.getConfigElement("ActivityName");
		   
		     String requested_product="";
		     String requested_subproduct="";
		    int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		      
		    if(n>0)
		    {
		        for(int i=0;i<n;i++)
		        {
		             requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
		             
		             requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);


		       }    
		   }
		      


		    List<String> repeaterHeaders = new ArrayList<String>();
	        if (sActivityName.equalsIgnoreCase("Original_Validation") || sActivityName.equalsIgnoreCase("Dispatch") || sActivityName.equalsIgnoreCase("Post_Disbursal") || sActivityName.equalsIgnoreCase("Disbursal")  || sActivityName.equalsIgnoreCase("CardCollection") || sActivityName.equalsIgnoreCase("CustomerHold"))
	        {
																									  
	        	repeaterHeaders.add("Document Name");

		        repeaterHeaders.add("Expire Date");
		        repeaterHeaders.add("Mandatory");
		        repeaterHeaders.add("Status");
		        repeaterHeaders.add("Remarks");

		        repeaterHeaders.add("Add from DMS");

		        repeaterHeaders.add("Add from PC");

		        repeaterHeaders.add("Scan");
		        repeaterHeaders.add("View");

		        repeaterHeaders.add("Print");

		        repeaterHeaders.add("Download");
		        repeaterHeaders.add("DocIndex");

		        repeaterHeaders.add("OV Remarks");
		        repeaterHeaders.add("OV Decision");
		        repeaterHeaders.add("Approved By");
	        }
	        else
	        {
		    repeaterHeaders.add("Document Name");

		    repeaterHeaders.add("Expire Date");
		    repeaterHeaders.add("Mandatory");
		    //repeaterHeaders.add("Deferred/Waived");
		    repeaterHeaders.add("Status");
		    repeaterHeaders.add("Remarks");

		    repeaterHeaders.add("Add from DMS");

		    repeaterHeaders.add("Add from PC");

		    repeaterHeaders.add("Scan");
		    repeaterHeaders.add("View");

		    repeaterHeaders.add("Print");

		    repeaterHeaders.add("Download");
		    repeaterHeaders.add("DocIndex");

	        }
		    CreditCard.mLogger.info("after making headers");



		    FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");


		    List<List<String>> docName = null;
		    String documentName = null;
		    String documentNameMandatory=null;
	        String ovRemarks=null;
	        String ovdecision = null;
	        String approvedBy=null;
	        String statusValue = null;

		    String query = "";

		    IRepeater repObj=null;
		    CreditCard.mLogger.info("after creating the object for repeater");

		    int repRowCount = 0;



		    repObj = formObject.getRepeaterControl("IncomingDocument_Frame");

		    CreditCard.mLogger.info(""+repObj.toString());



		    //query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";

		    // ProcessDefId IN" + "(SELECT ProcessDefId FROM PROCESSDEFTABLE WHERE ProcessName ='"+processName+"')";




		    //docName = formObject.getNGDataFromDataCache(query);
//		    CreditCard.mLogger.info(""+ docName);

		    try{
	        repObj.setRepeaterHeaders(repeaterHeaders);

	        if (sActivityName.equalsIgnoreCase("Original_Validation") )
	        {
	        	CreditCard.mLogger.info( "add row ov ");
	        	for(int i=0;i<docName.size();i++ )
	        	{
		            repObj.addRow();                    

		            documentName = docName.get(i).get(0);
		            documentNameMandatory = docName.get(i).get(1);
		            statusValue = docName.get(i).get(3);

		            CreditCard.mLogger.info(" "+ documentName);
		            CreditCard.mLogger.info(" "+ documentNameMandatory);

		            repObj.setValue(i, 0, documentName);
		            repObj.setValue(i, 2, documentNameMandatory);
		            if(statusValue.equalsIgnoreCase("Recieved"))
		            {
		            	repObj.setEditable(i,"cmplx_DocName_OVRemarks" , true);
		            	repObj.setEditable(i,"cmplx_DocName_OVDec" , true);
		            	repObj.setEditable(i,"cmplx_DocName_ApprovedBy" , true);
		            }
		            else
		            {
		            	repObj.setEditable(i,"cmplx_DocName_OVRemarks" , false);
		            	repObj.setEditable(i,"cmplx_DocName_OVDec" , false);
		            	repObj.setEditable(i,"cmplx_DocName_ApprovedBy" , false);
		            }
		            repRowCount = repObj.getRepeaterRowCount();

		            CreditCard.mLogger.info( " " + repRowCount);
	        	}
	         }
	        if ( sActivityName.equalsIgnoreCase("Dispatch") || sActivityName.equalsIgnoreCase("Post_Disbursal") || sActivityName.equalsIgnoreCase("Disbursal")  || sActivityName.equalsIgnoreCase("CardCollection") || sActivityName.equalsIgnoreCase("CustomerHold"))
	        {
	        	CreditCard.mLogger.info( "add row ov ");
	        	for(int i=0;i<docName.size();i++ )
	        	{
		            repObj.addRow();                    

		            documentName = docName.get(i).get(0);
		            documentNameMandatory = docName.get(i).get(1);	          

		            CreditCard.mLogger.info(" "+ documentName);
		            CreditCard.mLogger.info(" "+ documentNameMandatory);

		            repObj.setValue(i, 0, documentName);
		            repObj.setValue(i, 2, documentNameMandatory);

		            repRowCount = repObj.getRepeaterRowCount();

		            CreditCard.mLogger.info( " " + repRowCount);
	        	}
	         }
	        else
	        {
	        	CreditCard.mLogger.info( "add row CSm maker ");


	        	for(int i=0;i<docName.size();i++ )
	        	{
		            repObj.addRow();

		            repObj.setColumnVisible(12, false);
		            repObj.setColumnVisible(13, false);
		            repObj.setColumnVisible(14, false);

		            documentName = docName.get(i).get(0);
		            documentNameMandatory = docName.get(i).get(1);

		            CreditCard.mLogger.info(" "+ documentName);
		            CreditCard.mLogger.info(" "+ documentNameMandatory);

		            repObj.setValue(i, 0, documentName);
		            repObj.setValue(i, 2, documentNameMandatory); 	     

		            repRowCount = repObj.getRepeaterRowCount();

		            CreditCard.mLogger.info( " " + repRowCount);

	        	} 
	        }
	       }

		   catch (Exception e) {

		       CreditCard.mLogger.info( " " + e.toString());

		        } finally {

		               repObj = null;

		               repeaterHeaders = null;         
		  }
	 */

		//above code is commented as a backup and below code is copied from PLCommon. Done by saurabh on 9th August.

		CreditCard.mLogger.info( "inside fetchIncomingDocRepeater");
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName = formobject.getConfigElement("ActivityName");
		CreditCard.mLogger.info("");
		//12th sept
		String requested_product="";
		String requested_subproduct="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		CreditCard.mLogger.info("valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				CreditCard.mLogger.info(requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				CreditCard.mLogger.info(requested_subproduct);

			}    
		}
		CreditCard.mLogger.info(requested_product);

		//Disha started (10/6/17)

		List<String> repeaterHeaders = new ArrayList<String>();
		CreditCard.mLogger.info(sActivityName);
		if (sActivityName.equalsIgnoreCase("Original_Validation") || sActivityName.equalsIgnoreCase("Dispatch") || sActivityName.equalsIgnoreCase("Post_Disbursal") || sActivityName.equalsIgnoreCase("Disbursal") || sActivityName.equalsIgnoreCase("CC_Disbursal") || sActivityName.equalsIgnoreCase("Collection_User"))
		{
			CreditCard.mLogger.info("OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expiry Date");
			repeaterHeaders.add("Mandatory");
			//repeaterHeaders.add("Deferred/Waived");
			repeaterHeaders.add("Status");
			//repeaterHeaders.add("Deferred Until Date");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");

			repeaterHeaders.add("OV Remarks");
			repeaterHeaders.add("OV Decision");
			repeaterHeaders.add("Approved By");
		}
		else
		{
			CreditCard.mLogger.info(" non OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expire Date");
			repeaterHeaders.add("Mandatory");
			//repeaterHeaders.add("Deferred/Waived");
			repeaterHeaders.add("Status");
			//repeaterHeaders.add("Deferred Until Date");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");

		}
		CreditCard.mLogger.info("after making headers");

		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> docName = null;
		String documentName = null;
		String documentNameMandatory=null;
		String statusValue = null;
		String remarks = null;

		String query = "";

		IRepeater repObj=null;
		CreditCard.mLogger.info("after creating the object for repeater");

		int repRowCount = 0;
		try{
			repObj = formObject.getRepeaterControl("IncomingDocument_Frame");

			CreditCard.mLogger.info(""+repObj.toString());


			// query = "SELECT distinct DocName,Mandatory,Status,Remarks, FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
			//query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='LE-0000000007308-RLOS'";
			query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getNGValue("Parent_WIName")+"'";
			CreditCard.mLogger.info(""+query);
			docName = formObject.getNGDataFromDataCache(query);

		}
		catch(Exception ex){
			CreditCard.mLogger.info(""+printException(ex));

		}

		//docName = formObject.getNGDataFromDataCache(query);
		//        CreditCard.mLogger.info(""+ docName);

		try{

			/*if (repObj.getRepeaterRowCount() == 0) {

	            CreditCard.mLogger.info( "when row count is zero");
	            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
	            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"'";
	            //query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='Personal Loan' and SubProductName='Expat Personal Loans'";
	            docName = formObject.getNGDataFromDataCache(query);
	            PL_CreditCard.mLogger.info(""+ docName);
	        //    repObj.clear();

	        }
	        if (repObj.getRepeaterRowCount() != 0) {

	            CreditCard.mLogger.info( "when row count is not zero");
	            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"' and ProcessName='RLOS'";
	            query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"'";
	            //query = "SELECT distinct DocName,Mandatory FROM ng_rlos_incomingDoc WHERE ProductName='Personal Loan' and SubProductName='Expat Personal Loans' and  wi_name='"+formObject.getWFWorkitemName()+"'";
	            docName = formObject.getNGDataFromDataCache(query);
	            PL_CreditCard.mLogger.info(""+ docName);


	        }*/
			repObj.setRepeaterHeaders(repeaterHeaders);

			if (sActivityName.equalsIgnoreCase("Original_Validation") )
			{
				CreditCard.mLogger.info( "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);
					statusValue = docName.get(i).get(3);

					CreditCard.mLogger.info(" "+ documentName);
					CreditCard.mLogger.info(" "+ documentNameMandatory);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					if(statusValue.equalsIgnoreCase("Recieved"))
					{
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , true);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , true);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , true);
					}
					else
					{
						/*repObj.setEditable(i,"IncomingDoc_Frame2_Reprow"+i+"_Repcolumn12" , false);
		            	repObj.setEditable(i,"IncomingDoc_Frame2_Reprow"+i+"_Repcolumn13" , false);
		            	repObj.setEditable(i,"IncomingDoc_Frame2_Reprow"+i+"_Repcolumn14" , false);*/
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , false);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , false);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , false);
					}
					repRowCount = repObj.getRepeaterRowCount();

					CreditCard.mLogger.info( " " + repRowCount);
				}
			}
			if ( sActivityName.equalsIgnoreCase("Dispatch") || sActivityName.equalsIgnoreCase("Post_Disbursal") || sActivityName.equalsIgnoreCase("Disbursal") || sActivityName.equalsIgnoreCase("CC_Disbursal") || sActivityName.equalsIgnoreCase("Collection_User"))
			{
				CreditCard.mLogger.info( "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);	          

					CreditCard.mLogger.info(" "+ documentName);
					CreditCard.mLogger.info(" "+ documentNameMandatory);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);

					repRowCount = repObj.getRepeaterRowCount();

					CreditCard.mLogger.info( " " + repRowCount);
				}
			}
			else
			{
				CreditCard.mLogger.info( "add row CSm maker ");


				for(int i=0;i<docName.size();i++ )
				{
					repObj.addRow();

					repObj.setColumnVisible(12, false);
					repObj.setColumnVisible(13, false);
					repObj.setColumnVisible(14, false);

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					remarks = docName.get(i).get(4);

					CreditCard.mLogger.info(" "+ documentName);
					CreditCard.mLogger.info(" "+ documentNameMandatory);
					CreditCard.mLogger.info(" "+ remarks);
					CreditCard.mLogger.info(" "+ statusValue);

					repObj.setValue(i, 0, documentName);
					repObj.setDisabled(i, "cmplx_DocName_DocName", true);
					repObj.setValue(i, 2, documentNameMandatory);
					if(statusValue == null || statusValue.equalsIgnoreCase("null") || statusValue.equalsIgnoreCase(null)){
						statusValue = "--Select--"; 
					}
					repObj.setValue(i, 3, statusValue);
					repObj.setValue(i, 4, remarks);

					repRowCount = repObj.getRepeaterRowCount();

					CreditCard.mLogger.info( " " + repRowCount);

				} //Disha ended (10/6/17)
			}
		}
		catch (Exception e) {

			CreditCard.mLogger.info( " " + printException(e));

		} finally {

			repObj = null;

			repeaterHeaders = null;         
		}


	}



	public String DOBinDDMMYYYY(String DateofBirth) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

		String date = DateofBirth;

		String date1 = "";

		try {

			if(date!=null){

				date1 = sdf1.format(sdf.parse(date));

			}

		} catch (ParseException ex) {

			CreditCard.mLogger.info("Eror in parsing date");

		}

		CreditCard.mLogger.info("Original date from record :" + date);

		CreditCard.mLogger.info("Date post conversion : " + date1);

		return date1;

	}

	public void loadPicklist_ServiceRequest()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info( "Inside loadPicklist_ServiceRequest: "); 
		LoadPickList("transType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransactionType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("transferMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransferMode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("DispatchChannel", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DispatchChannel with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TargetSegmentCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("MarketingCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("sourceCode", "select Branch , SOL_ID,0 as sno from NG_MASTER_SourceCode where userid = '"+formObject.getNGValue("lbl_user_name_val")+"'  union select distinct Branch,SOL_ID,1 as sno from NG_MASTER_SourceCode where Branch !='' and SOL_ID !='' order by sno");
		LoadPickList("appstatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_ApplicationStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("approvalcode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApprovalCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("chequeStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MChequeStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DDSMode with (nolock) where IsActive = 'Y' order by code ");
		LoadPickList("cmplx_CC_Loan_AccType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSBankAName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_ModeOfSI", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ModeOfSI with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSPAckage", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_VPSPackage with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSSourceCode", "select '--Select--' as Branch,'' as SOL_ID union  select distinct Branch,SOL_ID from NG_MASTER_SourceCode with (nolock) where Branch !='' and SOL_ID !=''");
		LoadPickList("cmplx_CC_Loan_StartMonth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Month with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_HoldType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_HoldType with (nolock) where IsActive = 'Y' order by code");
		//cmplx_CC_Loan_HoldType
	}

	public void ChangeRepeater(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame10");
		// String ComboValue = formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED");
		// CreditCard.mLogger.info("value of combo is"+ComboValue); 

		HashMap<Integer,String> Monthhm =new HashMap<Integer,String>();  
		Monthhm.put(1,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN");
		Monthhm.put(2,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB");
		Monthhm.put(3,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR");

		Monthhm.put(4,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR");
		Monthhm.put(5,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY");

		Monthhm.put(6,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN");
		Monthhm.put(7,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL");
		Monthhm.put(8,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG");

		Monthhm.put(9,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP");
		Monthhm.put(10,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT");

		Monthhm.put(11,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV");
		Monthhm.put(12,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC");

		ArrayList<String> MonthArray = new ArrayList<String>();
		MonthArray.add("JAN");
		MonthArray.add("FEB");
		MonthArray.add("MAR");
		MonthArray.add("APR");
		MonthArray.add("MAY");
		MonthArray.add("JUN");
		MonthArray.add("JUL");
		MonthArray.add("AUG");
		MonthArray.add("SEP");
		MonthArray.add("OCT");
		MonthArray.add("NOV");
		MonthArray.add("DEC");




		String[] date1 = Currentdate().split("/");
		int Monthval = Integer.parseInt(date1[0]);
		Monthval = Monthval - 2 ;
		CreditCard.mLogger.info("Monthval value is"+Monthval);
		for(int i= 6 ;i>=1; i--){
			if(Monthval == -1){
				CreditCard.mLogger.info("Inside monthval if"+Monthval);
				MonthArray.remove(MonthArray.size() - 1);
			}
			else{
				MonthArray.remove(Monthval);
				Monthval--;
				CreditCard.mLogger.info("inside Monthval else"+Monthval);
			}


		}

		CreditCard.mLogger.info("Month arrray  size is"+MonthArray.size());

		for(int i = MonthArray.size(); i >= 1 ; i--){
			CreditCard.mLogger.info("array values at last"+"cmplx_FinacleCore_cmplx_AvgBalanceNBC_"+MonthArray.get(i-1));
			CreditCard.mLogger.info("key values"+ getKey(Monthhm,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_"+MonthArray.get(i-1)));
			repObj.setColumnEditable(getKey(Monthhm,"cmplx_FinacleCore_cmplx_AvgBalanceNBC_"+MonthArray.get(i-1)), false);



		}





	}


	public void ChangeRepeaterTrnover(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame11");
		// String ComboValue = formObject.getNGValue("cmplx_FinacleCore_cmplx_TurnoverNBC_BS_ANALYSED");
		// CreditCard.mLogger.info("value of combo is"+ComboValue, ""); 

		HashMap<Integer,String> Monthhm =new HashMap<Integer,String>();  
		Monthhm.put(1,"cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1");
		Monthhm.put(2,"cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1");
		Monthhm.put(3,"cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1");

		Monthhm.put(4,"cmplx_FinacleCore_cmplx_TurnoverNBC_APR1");
		Monthhm.put(5,"cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1");

		Monthhm.put(6,"cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1");
		Monthhm.put(7,"cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1");
		Monthhm.put(8,"cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1");

		Monthhm.put(9,"cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1");
		Monthhm.put(10,"cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1");

		Monthhm.put(11,"cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1");
		Monthhm.put(12,"cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1");

		ArrayList<String> MonthArray = new ArrayList<String>();
		MonthArray.add("JAN1");
		MonthArray.add("FEB1");
		MonthArray.add("MAR1");
		MonthArray.add("APR1");
		MonthArray.add("MAY1");
		MonthArray.add("JUN1");
		MonthArray.add("JUL1");
		MonthArray.add("AUG1");
		MonthArray.add("SEP1");
		MonthArray.add("OCT1");
		MonthArray.add("NOV1");
		MonthArray.add("DEC1");




		String[] date1 = Currentdate().split("/");
		int Monthval = Integer.parseInt(date1[0]);
		Monthval = Monthval - 2 ;
		CreditCard.mLogger.info("Monthval value is"+Monthval);
		for(int i=6 ;i>=1; i--){
			if(Monthval == -1){
				CreditCard.mLogger.info("Inside monthval if"+Monthval);
				MonthArray.remove(MonthArray.size() - 1);
			}
			else{
				MonthArray.remove(Monthval);
				Monthval--;
				CreditCard.mLogger.info("inside Monthval else"+Monthval);
			}


		}

		CreditCard.mLogger.info("Month arrray  size is"+MonthArray.size());

		for(int i = MonthArray.size(); i >= 1 ; i--){
			CreditCard.mLogger.info("array values at last"+"cmplx_FinacleCore_cmplx_TurnoverNBC_"+MonthArray.get(i-1));
			CreditCard.mLogger.info("key values"+ getKey(Monthhm,"cmplx_FinacleCore_cmplx_TurnoverNBC_"+MonthArray.get(i-1)));
			repObj.setColumnEditable(getKey(Monthhm,"cmplx_FinacleCore_cmplx_TurnoverNBC_"+MonthArray.get(i-1)), false);



		}


	}
	static Integer getKey(HashMap<Integer, String> map, String value) {
		Integer key = null;
		for(Map.Entry<Integer, String> entry : map.entrySet()) {
			if((value == null && entry.getValue() == null) || (value != null && value.equals(entry.getValue()))) {
				key = entry.getKey();
				break;
			}
		}
		return key;
	}

	public String Currentdate(){
		Date date = new Date();
		String DATE_FORMAT = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String Todaydate = sdf.format(date);
		CreditCard.mLogger.info("Todays date is"+Todaydate);
		return Todaydate;
	}
	//functions added with respect to change in Customer_Details call(Tanshu Aggarwal 29/05/2017)


	public void CalculateRepeater(String Controlname ){
		CreditCard.mLogger.info("value of control is"+Controlname);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame10");
		//int Finalmonthval = Integer.parseInt(monthval);
		int noOfDays = Integer.parseInt(repObj.getValue(31, Controlname ));
		float sum = 0;
		float Average = 0;
		float value = 0 ;
		//float Rowvalue = 0 ;

		for(int i=0; i<31 ; i++){
			if(!(repObj.getValue(i,Controlname).equals("") || repObj.getValue(i,Controlname) == null)){
				value = Float.parseFloat(repObj.getValue(i,Controlname));
				sum += value;
			}
			CreditCard.mLogger.info("value of intermediate sum is"+sum);
		}
		Average = sum/noOfDays;
		String Finalsum =convertFloatToString(sum) ;
		String FinalAverage =  convertFloatToString(Average);

		CreditCard.mLogger.info("value of finalsum is"+Finalsum);
		CreditCard.mLogger.info("value of finalsaverage is"+FinalAverage);
		repObj.setValue(32, Controlname, Finalsum);
		repObj.setValue(33, Controlname, FinalAverage);
	}


	public void CalculateRepeaterTrnover(String Controlname){
		CreditCard.mLogger.info("value of control is"+Controlname);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame11");
		//int Finalmonthval = Integer.parseInt(monthval);

		float sum = 0;
		float value = 0 ;
		//float Rowvalue = 0 ;

		for(int i=0; i<31 ; i++){
			if(!(repObj.getValue(i,Controlname).equals("") || repObj.getValue(i,Controlname) == null)){
				value = Float.parseFloat(repObj.getValue(i,Controlname));
				sum += value;
			}
			CreditCard.mLogger.info("value of intermediate sum is"+sum);
		}
		String Finalsum = convertFloatToString(sum);
		CreditCard.mLogger.info("value of finalsum is"+Finalsum);
		repObj.setValue(31, Controlname, Finalsum);
	}

	public void addToAvgRepeater(IRepeater repObj){
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        //IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame10");
        IRepeater repObj1 = formObject.getRepeaterControl("FinacleCore_Frame9");
        String Accno  = formObject.getNGValue("cmplx_FinacleCore_account_number_avg_nbc");
        CreditCard.mLogger.info("Accno value is"+Accno);
        //ArrayList<ArrayList<String>> AvgBalList = new ArrayList<ArrayList<String>>();
        ArrayList<String> SimpleAvgBalList = new ArrayList<String>();

        SimpleAvgBalList.add(Accno);
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV"));
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC"));
        // added by abhishek after adding hiddden workitem in repeater
        SimpleAvgBalList.add(repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc"));

        try{
              int j = 0;
              for(j=0;j<6;j++){
                    if(repObj1.getValue(j,"cmplx_FinacleCore_cmplx_avgbalance_new_Account").equals("")  ){
                          break;
                    }
              }
              if(j<6){
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_Account", SimpleAvgBalList.get(0));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_JAN2", SimpleAvgBalList.get(1));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_FEB2", SimpleAvgBalList.get(2));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_MAR2", SimpleAvgBalList.get(3));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_APR2", SimpleAvgBalList.get(4));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_MAY2", SimpleAvgBalList.get(5));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_JUN2", SimpleAvgBalList.get(6));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_JUL2", SimpleAvgBalList.get(7));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_AUG2", SimpleAvgBalList.get(8));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_SEP2", SimpleAvgBalList.get(9));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_OCT2", SimpleAvgBalList.get(10));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_NOV2", SimpleAvgBalList.get(11));
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_DEC2", SimpleAvgBalList.get(12));
              // added by abhishek after adding hiddden workitem in repeater
              repObj1.setValue(j, "cmplx_FinacleCore_cmplx_avgbalance_new_wi_name_avgnew", SimpleAvgBalList.get(13));

              repObj1.setEditable(j, "cmplx_FinacleCore_cmplx_avgbalance_new_Consider_for_obligation", true);
              }
              // added by abhishek to insert repeater values in 3rd table
              //create Arraylist of repeater values
              List<List<String>> FinalRepeaterList = new ArrayList<List<String>>();
              for(int i=0; i<34 ; i++)
              {
                    List<String> RepeaterList = new ArrayList<String>();
                    CreditCard.mLogger.info("NEW Accno value is"+Accno);

                    RepeaterList.add(Accno);
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV"));
                    RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC"));
                    CreditCard.mLogger.info("RepeaterList for "+i+" is: :"+RepeaterList);
                    FinalRepeaterList.add(i, RepeaterList);
                    CreditCard.mLogger.info("FinalRepeaterList for  "+i+" is ::"+FinalRepeaterList);

              }
              CreditCard.mLogger.info("FinalRepeaterList is:"+FinalRepeaterList);

              // delete if there is existing data for that account number
              String delQuery = "delete from NG_RLOS_RP_avgNBC_temp where Acc_no = '"+Accno+"'";
              CreditCard.mLogger.info("query for deletion of repeater value in 3rd table is:"+delQuery);
              formObject.saveDataIntoDataSource(delQuery);
              // create a loop to make insert queries and save in database
              for(int i=0; i<34 ; i++){
                    String query =  "insert into NG_RLOS_RP_avgNBC_temp values('"+FinalRepeaterList.get(i).get(0)+"','"+FinalRepeaterList.get(i).get(1)+"','"+FinalRepeaterList.get(i).get(2)+"','"+FinalRepeaterList.get(i).get(3)+"','"+FinalRepeaterList.get(i).get(4)+"','"+FinalRepeaterList.get(i).get(5)+"','"+FinalRepeaterList.get(i).get(6)+"','"+FinalRepeaterList.get(i).get(7)+"','"+FinalRepeaterList.get(i).get(8)+"','"+FinalRepeaterList.get(i).get(9)+"','"+FinalRepeaterList.get(i).get(10)+"','"+FinalRepeaterList.get(i).get(11)+"','"+FinalRepeaterList.get(i).get(12)+"','"+FinalRepeaterList.get(i).get(13)+"')";
                    CreditCard.mLogger.info("query for insertion of repeater value in 3rd table is:"+query);
                    formObject.saveDataIntoDataSource(query);
              }
              // after success clear values from repeater 
              for(int i=0 ; i<34 ; i++){
                    if(i!=31){
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV");
                          repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC");
                          // added by abhishek after adding hiddden workitem in repeater
                         // repObj.clearValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc");
                    }
              }

              formObject.clear("cmplx_FinacleCore_account_number_avg_nbc");
        }catch(Exception ex){
              CreditCard.mLogger.info("Exception occured while setting values in repeater"+ex);
        }
  }

	public void ModifyAvgNBCData(String Accno,IRepeater repObj1){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame10");
		//IRepeater repObj1 = formObject.getRepeaterControl("FinacleCore_Frame9");
		List<List<String>> Repeaterlist = new ArrayList<List<String>>();
		String query="select Acc_no,avgNBC_winame,Mon_JAN,Mon_FEB,Mon_MAR,Mon_APR,Mon_MAY,Mon_JUN,Mon_JUL,Mon_AUG,Mon_SEP,Mon_OCT,Mon_NOV,Mon_DEC from NG_RLOS_RP_avgNBC_temp where Acc_no = '"+Accno+"' ";
		CreditCard.mLogger.info("select query for avg nbc repeater"+query);
		Repeaterlist = formObject.getDataFromDataSource(query);
		CreditCard.mLogger.info("Repeaterlist after select query for avg bal nbc"+Repeaterlist);
		if(Repeaterlist.size()>0){
			// Loop to populate arraylist data on repeater
			for(int i =0 ; i<34 ; i++){
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_wi_name_avg_nbc", Repeaterlist.get(i).get(1));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN", Repeaterlist.get(i).get(2));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB", Repeaterlist.get(i).get(3));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR", Repeaterlist.get(i).get(4));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR", Repeaterlist.get(i).get(5));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY", Repeaterlist.get(i).get(6));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN", Repeaterlist.get(i).get(7));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL", Repeaterlist.get(i).get(8));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG", Repeaterlist.get(i).get(9));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP", Repeaterlist.get(i).get(10));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT", Repeaterlist.get(i).get(11));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV", Repeaterlist.get(i).get(12));
				repObj.setValue(i, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC", Repeaterlist.get(i).get(13));
			}
	
			formObject.setNGValue("cmplx_FinacleCore_account_number_avg_nbc", Repeaterlist.get(0).get(0));
			// set height of repeater frame
			formObject.setTop("FinacleCore_Frame10", 80);
			// clear row of avg bal nbc for last 1 year row
			for( int i=0 ; i<6 ; i++){
				CreditCard.mLogger.info("on modify button click account value for row "+i+""+repObj1.getValue(i,"cmplx_FinacleCore_cmplx_avgbalance_new_Account"));
				if(repObj1.getValue(i,"cmplx_FinacleCore_cmplx_avgbalance_new_Account").equalsIgnoreCase(Accno)){
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_Account");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_JAN2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_FEB2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_MAR2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_APR2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_MAY2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_JUN2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_JUL2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_AUG2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_SEP2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_OCT2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_NOV2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_DEC2");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_consider_avg_new");
					repObj1.clearValue(i, "cmplx_FinacleCore_cmplx_avgbalance_new_wi_name_avgnew");
					break;
				}
			}
		}
	
	// added by abhishek for modifying the data of turnover repeater
	// ++ below code already present - 06-10-2017
	//added by nikhil for repeater for clearing total and avg.
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_JAN2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_FEB2");
		repObj1.clearValue( 6, "cmplx_FinacleCore_cmplx_avgbalance_new_MAR2");
		repObj1.clearValue( 6, "cmplx_FinacleCore_cmplx_avgbalance_new_APR2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_MAY2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_JUN2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_JUL2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_AUG2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_SEP2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_OCT2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_NOV2");
		repObj1.clearValue(6, "cmplx_FinacleCore_cmplx_avgbalance_new_DEC2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JAN2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_FEB2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_MAR2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_APR2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_MAY2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JUN2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_JUL2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_AUG2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_SEP2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_OCT2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_NOV2");
		repObj1.clearValue(7, "cmplx_FinacleCore_cmplx_avgbalance_new_DEC2");
		//ended by nikhil
	}
	// added by abhishek for modifying the data of turnover repeater

	public void ModifyTrnOverData(String Accno){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame11");
		List<List<String>> Repeaterlist = new ArrayList<List<String>>();
		String query="select Acc_no,TurnoverNBC_winame,Mon_JAN,Mon_FEB,Mon_MAR,Mon_APR,Mon_MAY,Mon_JUN,Mon_JUL,Mon_AUG,Mon_SEP,Mon_OCT,Mon_NOV,Mon_DEC from NG_RLOS_RP_TRNOVERNBC_temp where Acc_no = '"+Accno+"' ";
		CreditCard.mLogger.info("select query for turnover repeater"+query);
		Repeaterlist = formObject.getDataFromDataSource(query);
		CreditCard.mLogger.info("Repeaterlist after select query for turnover::"+Repeaterlist);
		// Loop to populate arraylist data on repeater
		for(int i =0 ; i<32 ; i++){
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_wi_name_turn", Repeaterlist.get(i).get(1));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1", Repeaterlist.get(i).get(2));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1", Repeaterlist.get(i).get(3));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1", Repeaterlist.get(i).get(4));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1", Repeaterlist.get(i).get(5));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1", Repeaterlist.get(i).get(6));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1", Repeaterlist.get(i).get(7));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1", Repeaterlist.get(i).get(8));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1", Repeaterlist.get(i).get(9));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1", Repeaterlist.get(i).get(10));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1", Repeaterlist.get(i).get(11));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1", Repeaterlist.get(i).get(12));
			repObj.setValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1", Repeaterlist.get(i).get(13));
		}

		formObject.setNGValue("cmplx_FinacleCore_account_turnstatistics", Repeaterlist.get(0).get(0));

		// set height of repeater frame

		formObject.setTop("FinacleCore_Frame11", 80);


	}

	public void addToTrnoverGrid(IRepeater repObj){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame11");
		String Accno  = formObject.getNGValue("cmplx_FinacleCore_account_turnstatistics");
		CreditCard.mLogger.info("Accno value is"+Accno);
		String Count ="";
		ArrayList<String> SimpleTrnoverList = new ArrayList<String>();
		//SimpleTrnoverList.clear();
		//ArrayList<ArrayList<String>> FinalList = new ArrayList<ArrayList<String>>();
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1").equals("0.0") ||repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1").equals("0")|| repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1",repObj);
			//CreditCard.mLogger.info("Count value in grid"+Count, "");
			SimpleTrnoverList.add(Accno);
			CreditCard.mLogger.info("Accno value in grid"+Accno);
			SimpleTrnoverList.add("January");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1"));
			SimpleTrnoverList.add("false");
			// added for winame
			SimpleTrnoverList.add(formObject.getWFWorkitemName());
			CreditCard.mLogger.info("list values for jan"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			CreditCard.mLogger.info("list values for january are "+SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1").equals("0.0")|| repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1",repObj);
			SimpleTrnoverList.add(Accno);
			CreditCard.mLogger.info("Accno value in grid"+Accno);
			SimpleTrnoverList.add("February");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());
			CreditCard.mLogger.info("list values for feb"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			CreditCard.mLogger.info("list values for january are "+SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1").equals("0.0") ||repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1").equals("0")|| repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("March");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for march"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1").equals("0.0")|| repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_APR1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("April");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for april"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("MAY");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for may"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("June");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for june"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1").equals("0")|| repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("July");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for july"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("August");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for aug"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("September");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for sep"+SimpleTrnoverList);
			formObject.addItem("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("October");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for oct"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("November");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());

			CreditCard.mLogger.info("list values for nov"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		if(!(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1").equals("0.0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1").equals("0") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1").equals("") || repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1") == null)){
			CreditCard.mLogger.info("trnover repeater total value is"+repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1"));
			Count = countNoOfCredit("cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1",repObj);
			SimpleTrnoverList.add(Accno);
			SimpleTrnoverList.add("December");
			SimpleTrnoverList.add(Count);
			SimpleTrnoverList.add(repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1"));
			SimpleTrnoverList.add("false");
			SimpleTrnoverList.add(formObject.getWFWorkitemName());
			CreditCard.mLogger.info("list values for decs"+SimpleTrnoverList);
			formObject.addItemFromList("cmplx_FinacleCore_credturn_grid", SimpleTrnoverList);
			SimpleTrnoverList.clear();
		}
		// added by abhishek to insert repeater values in 3rd table
		//create Arraylist of repeater values
		List<List<String>> FinalRepeaterList = new ArrayList<List<String>>();
		for(int i=0; i<32 ; i++)
		{
			List<String> RepeaterList = new ArrayList<String>();
			CreditCard.mLogger.info("NEW Accno value is"+Accno);

			RepeaterList.add(Accno);
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_wi_name_turn"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1"));
			RepeaterList.add(repObj.getValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1"));
			CreditCard.mLogger.info("RepeaterList for "+i+" is: :"+RepeaterList);
			FinalRepeaterList.add(i, RepeaterList);
			CreditCard.mLogger.info("FinalRepeaterList for  "+i+" is ::"+FinalRepeaterList);

		}
		CreditCard.mLogger.info("FinalRepeaterList is:"+FinalRepeaterList);

		// delete if there is existing data for that account number
		String delQuery = "delete from NG_RLOS_RP_TRNOVERNBC_temp where Acc_no = '"+Accno+"'";
		CreditCard.mLogger.info("query for deletion of repeater value in 3rd table is:"+delQuery);
		formObject.saveDataIntoDataSource(delQuery);
		// create a loop to make insert queries and save in database
		for(int i=0; i<32 ; i++){
			String query =  "insert into NG_RLOS_RP_TRNOVERNBC_temp values('"+FinalRepeaterList.get(i).get(0)+"','"+FinalRepeaterList.get(i).get(1)+"','"+FinalRepeaterList.get(i).get(2)+"','"+FinalRepeaterList.get(i).get(3)+"','"+FinalRepeaterList.get(i).get(4)+"','"+FinalRepeaterList.get(i).get(5)+"','"+FinalRepeaterList.get(i).get(6)+"','"+FinalRepeaterList.get(i).get(7)+"','"+FinalRepeaterList.get(i).get(8)+"','"+FinalRepeaterList.get(i).get(9)+"','"+FinalRepeaterList.get(i).get(10)+"','"+FinalRepeaterList.get(i).get(11)+"','"+FinalRepeaterList.get(i).get(12)+"','"+FinalRepeaterList.get(i).get(13)+"')";
			CreditCard.mLogger.info("query for insertion of repeater value in 3rd table is:"+query);
			formObject.saveDataIntoDataSource(query);
		}
		// after success clear values from repeater 
		// added by abhishek to clear repeater after add
		for(int i=0 ; i<32 ; i++){
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_APR1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1");
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1");

			// added by abhishek after adding hiddden workitem in repeater
			repObj.clearValue(i, "cmplx_FinacleCore_cmplx_TurnoverNBC_wi_name_turn");
		}
		formObject.clear("cmplx_FinacleCore_account_turnstatistics");
	}
	// added by abhishek as per CC FSD
	public String countNoOfCredit(String controlName,IRepeater repObj){
		Integer count=0;
		for(int i = 0 ; i<31 ; i++){
			if(!repObj.getValue(i,controlName).equals("") ){
				count++;
			}
		}
		CreditCard.mLogger.info( "control name"+controlName);
		return count.toString();

	}
	// added by abhishek as per CC FSD to convert Float to String
	public String convertFloatToString(float val){
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(2);
		String finalVal = df.format(val);
		return finalVal;
	}

	//incoming doc function
	public void IncomingDoc(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
	
		String [] finalmisseddoc=new String[70];
		int rowRowcount=repObj.getRepeaterRowCount();
		CreditCard.mLogger.info( "sQuery for document name is: rowRowcount" +  rowRowcount);
			if (repObj.getRepeaterRowCount() != 0) {
			
				for(int j = 0; j < rowRowcount; j++)
				{
					String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
					CreditCard.mLogger.info( "sQuery for document name is: DocName" +  DocName);
				
					String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
					CreditCard.mLogger.info( "sQuery for document name is: Mandatory" +  Mandatory);
						
					if("Y".equalsIgnoreCase(Mandatory))
						{
							 String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex")==null?"":repObj.getValue(j,"cmplx_DocName_DocIndex");
							 CreditCard.mLogger.info("DocIndex"+DocIndex);
							 String StatusValue=repObj.getValue(j,"cmplx_DocName_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Status");
							 CreditCard.mLogger.info("StatusValue"+StatusValue);
							 String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks")==null?"":repObj.getValue(j, "cmplx_DocName_Remarks");
							 CreditCard.mLogger.info("Remarks"+Remarks);
							
								 if(DocIndex==null||DocIndex.equalsIgnoreCase("")||DocIndex.equalsIgnoreCase("null")){
									 CreditCard.mLogger.info("StatusValue inside DocIndex"+DocIndex);
									 if(StatusValue.equalsIgnoreCase("Received")){
										 CreditCard.mLogger.info("StatusValue inside DocIndex recieved");
										 //below line commented as this mandatory document is already received. 
										// finalmisseddoc[j]=DocName;
									 }
								
					                  else if(StatusValue.equalsIgnoreCase("Deferred")){
					                      formObject.setNGValue("is_deferral_approval_require","Y");
					                      formObject.RaiseEvent("WFSave");
					                      CreditCard.mLogger.info(formObject.getNGValue("is_deferral_approval_require"));
										  if(Remarks.equalsIgnoreCase("")){
											  CreditCard.mLogger.info("As you have not attached the Mandatory Document and the status is Deferred");
												throw new ValidatorException(new FacesMessage("As you have Deferred "+DocName+" Document,So kindly fill the Remarks"));
											}
											else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
												CreditCard.mLogger.info("Proceed further");
											}
					                 }
					                  else if(StatusValue.equalsIgnoreCase("Waived")){
					                      formObject.setNGValue("is_waiver_approval_require","Y");
					                      formObject.RaiseEvent("WFSave");
					                      CreditCard.mLogger.info(formObject.getNGValue("is_waiver_approval_require"));
											if(Remarks.equalsIgnoreCase("")){
												CreditCard.mLogger.info("As you have not attached the Mandatory Document and the status is Waived");
												throw new ValidatorException(new FacesMessage("As you have Waived "+DocName+" Document,So kindly fill the Remarks"));
											}
											else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
												CreditCard.mLogger.info("Proceed further");
											}
									  }
					                  else if((StatusValue.equalsIgnoreCase("--Select--"))||(StatusValue.equalsIgnoreCase(""))){
					                	  CreditCard.mLogger.info("StatusValue inside DocIndex is blank");
					                	  finalmisseddoc[j]=DocName;
					                  }
					                  else if((StatusValue.equalsIgnoreCase("Pending"))){
					                	  CreditCard.mLogger.info("StatusValue of doc is Pending");
					                	  
					                  }
									}
									else{
										if(!(DocIndex.equalsIgnoreCase(""))){
											if(!StatusValue.equalsIgnoreCase("Received")){
												repObj.setValue(j,"cmplx_DocName_Status","Received");
												repObj.setEditable(j, "cmplx_DocName_Status", false);
												CreditCard.mLogger.info("StatusValue::123final"+StatusValue);
											}
											else {
												
												CreditCard.mLogger.info("StatusValue::123final status is already received");
											}
										}
									}
									
								}
							}
						}
						StringBuilder mandatoryDocName = new StringBuilder("");
						
						CreditCard.mLogger.info("length of missed document"+finalmisseddoc.length);
						 CreditCard.mLogger.info("length of missed document mandatoryDocName.length"+mandatoryDocName.length());
					
						for(int k=0;k<finalmisseddoc.length;k++)
						{
							if(null != finalmisseddoc[k]) {
								mandatoryDocName.append(finalmisseddoc[k]).append(",");
							}
							CreditCard.mLogger.info( "finalmisseddoc is:" +finalmisseddoc[k]);
							CreditCard.mLogger.info("length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
						}
						mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
						CreditCard.mLogger.info( "misseddoc is:" +mandatoryDocName.toString());
					
						if(mandatoryDocName.length()<=0){
						
							CreditCard.mLogger.info( "misseddoc is: inside if condition");
						
						}
						else{
							CreditCard.mLogger.info( "misseddoc is: inside if condition");
							 CreditCard.mLogger.info("length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
							throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
						}
					}
	//incomingdoc function
	public void calculateTrnovrGrid(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		float total = 0 ;
		int n = formObject.getLVWRowCount("cmplx_FinacleCore_credturn_grid");
		for(int i=0 ; i<n ; i++){
			if(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", i, 4).equalsIgnoreCase("true")){
				total += Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", i, 3));
				CreditCard.mLogger.info( "->>>>>>>"+total);
			}
		}
		CreditCard.mLogger.info("->>>>>>>"+total);
		formObject.setNGValue("cmplx_FinacleCore_total_credit_3month",convertFloatToString(total) );
		formObject.setNGValue("cmplx_FinacleCore_total_credit_6month",convertFloatToString(total*2));
	}


	public void loadpicklist_Income(){
		LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
	}
	public void LoadPicklistVerification(List<String> mylist){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//PL_SKLogger.writeLog("RLOS_Common", "Inside LoadPicklistVerification(): "+mylist);

		for(String control_name:mylist)
		{
			formObject.setVisible(control_name, true);
			LoadPickList(control_name, "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri order by code");
		}
	}
	public void loadPicklist_custverification(){
		LoadPickList("cmplx_CustDetailVerification_mobno1_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_CustDetailVerification_mobno2_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification'");
		LoadPickList("cmplx_CustDetailVerification_dob_verification", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_POBoxno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_emirates_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_persorcompPOBox_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_resno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_offtelno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_hcountrytelno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_hcontryaddr_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_email1_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
		LoadPickList("cmplx_CustDetailVerification_email2_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");

	}

	public void loadPicklist_officeverification(){
		LoadPickList("cmplx_OffVerification_fxdsal_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_OffVerification_accpvded_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_OffVerification_desig_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_OffVerification_doj_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_OffVerification_cnfminjob_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");

		LoadPickList("cmplx_OffVerification_offtelnocntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Other'");
		LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Other'");
		LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Other'");
		LoadPickList("cmplx_OffVerification_hrdemailverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Other'");

		LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Tel_ValidatedFrom'");
	}

	public void loadPicklist_loancardverification(){
		LoadPickList("cmplx_LoanandCard_loanamt_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_tenor_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_emi_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_islorconv_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_firstrepaydate_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_cardtype_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_cardlimit_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
	}

	public void loadPicklist_WorldCheck()
	{
		LoadPickList("cmplx_WorldCheck_WorldCheck_Grid_BirthCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
		LoadPickList("cmplx_WorldCheck_WorldCheck_Grid_ResidentCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}

	public static String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;

	}

	public void partMatchValues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        int n=formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
        for(int i=0;i<n;i++)
        {
		try{
			CreditCard.mLogger.info( "Inside partMatchValues()-->"+formObject.getNGValue("cmplx_Customer_CIFNo"));

			formObject.setNGValue("PartMatch_CIFID",formObject.getNGValue("cmplx_Customer_CIFNo"));
			formObject.setNGValue("PartMatch_fname",formObject.getNGValue("cmplx_Customer_FirstNAme"));
			formObject.setNGValue("PartMatch_lname",formObject.getNGValue("cmplx_Customer_LastNAme"));
			formObject.setNGValue("PartMatch_newpass",formObject.getNGValue("cmplx_Customer_PAssportNo"));
			formObject.setNGValue("PartMatch_visafno",formObject.getNGValue("cmplx_Customer_VisaNo"));
			formObject.setNGValue("PartMatch_mno1",formObject.getNGValue("cmplx_Customer_MobileNo"));
			formObject.setNGValue("PartMatch_Dob",formObject.getNGValue("cmplx_Customer_DOb"));
			formObject.setNGValue("PartMatch_EID",formObject.getNGValue("cmplx_Customer_EmiratesID"));
			formObject.setNGValue("PartMatch_nationality",formObject.getNGValue("cmplx_Customer_Nationality"));
			formObject.setNGValue("PartMatch_drno",formObject.getNGValue("cmplx_Customer_DLNo"));
			formObject.setNGValue("PartMatch_mno2",formObject.getNGValue("AlternateContactDetails_MobNo2"));
            formObject.setNGValue("PartMatch_compname", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,4));
            formObject.setNGValue("PartMatch_tlNo", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,6));
            
			formObject.setNGValue("PartMatch_funame",formObject.getNGValue("PartMatch_fname")+formObject.getNGValue("PartMatch_lname"));

			int partgridCount = formObject.getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
			CreditCard.mLogger.info( Integer.toString(partgridCount));

			/*if(formObject.getNGValue("Is_PartMatchSearch").equalsIgnoreCase("Y"))
   		{
   			CreditCard.mLogger.info("Data in Grid");
   			formObject.setVisible("PartMatch_Search",false);
   		}
   		else
   			formObject.setVisible("PartMatch_Search",true);*/
		}
		catch(Exception e)
		{
			CreditCard.mLogger.info(printException(e));

		}
        }
        //formObject.setNGFrameState("PartMatch_Frame1", 1);
    

	}

	public void loadInDecGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			/*if(activityName.equalsIgnoreCase("CAD_Analyst1")){
				listviewName = "cmplx_CADDecision_cmplx_gr_Decisonhistory";	
			}
			else{
				listviewName = "DecisionHistory_Decision_ListView1";
			}*/
			String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm'),userName,workstepName,Decisiom,remarks,dec_wi_name,'','','','','','' from ng_rlos_gr_Decision with (nolock) where dec_wi_name='"+ParentWI_Name+"' or dec_wi_name='"+formObject.getWFWorkitemName()+"' order by datelastchanged desc";
			CreditCard.mLogger.info("Inside CCCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			CreditCard.mLogger.info("Inside CCCommon ->loadInDecGrid()"+list);
			/* try{
	             String date=list.get(0).get(0);
	             Date d=new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
	             CreditCard.mLogger.info("PersonnalLoanS>","value of date is:"+d.toString());*/
			for (List<String> a : list) 
			{
				/*List<String> mylist=new ArrayList<String>();
	                 mylist.add(d.toString());
	                 mylist.add(list.get(0).get(1));
	                 mylist.add(list.get(0).get(2));
	                 mylist.add(list.get(0).get(3));
	                 mylist.add(list.get(0).get(4));
	                 mylist.add(list.get(0).get(5));*/
				formObject.addItemFromList("DecisionHistory_Decision_ListView1", a);
			}
		}catch(Exception e){  CreditCard.mLogger.info("Exception Occurred loadInDecGrid :"+e.getMessage());}    
	}

	public void loadInWorldGrid()
	{

		CreditCard.mLogger.info("Query is loadInWorldGrid ");
	}

	public void saveIndecisionGridCSM(){
		CreditCard.mLogger.info( "Inside saveIndecisionGrid: "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		String Cifid=formObject.getNGValue("DecisionHistory_Cif_ID");
		String emiratesid=formObject.getNGValue("DecisionHistory_Emirates_Id");
		String custName=formObject.getNGValue("DecisionHistory_Customer_Name");
		String entrytime= new SimpleDateFormat("MM/dd/yyyy HH:mm").format(formObject.getNGValue("Entry_date_time"));
		CreditCard.mLogger.info("Query1 is"+entrytime);
		entrytime="";
		//12th sept
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks,status, dec_wi_name,Entry_Date,CIF_ID,Emirates_ID,CustName) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DEC_Decision")+"','"+formObject.getNGValue("cmplx_DEC_Remarks")+"','"+formObject.getNGValue("cmplx_Decision_Status")+"','"+formObject.getWFWorkitemName()+"','"+entrytime+"','"+Cifid+"','"+emiratesid+"','"+custName+"')";
		//String query1="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks,status, dec_wi_name) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DEC_Decision")+"','"+formObject.getNGValue("cmplx_DEC_Remarks")+"','"+formObject.getNGValue("cmplx_Decision_Status")+"','"+formObject.getWFWorkitemName()+"')";
		//12th sept
		CreditCard.mLogger.info("Query is"+query);
		formObject.saveDataIntoDataSource(query);
	}
	public void saveIndecisionGrid(){

		CreditCard.mLogger.info( "Inside saveIndecisionGrid: "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks,status, dec_wi_name,Entry_Date) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DEC_Decision")+"','"+formObject.getNGValue("cmplx_DEC_Remarks")+"','"+formObject.getNGValue("cmplx_Decision_Status")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getNGValue("Entry_date_time")+"')";
		CreditCard.mLogger.info("Query is"+query);
		formObject.saveDataIntoDataSource(query);

	}
	public String getCustAddress_details(){
		CreditCard.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		CreditCard.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);
		String  add_xml_str ="";
		for (int i = 0; i<add_row_count;i++){
			String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
			String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//1
			String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//2
			String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//3
			String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//4
			String Landmard = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//5
			String city=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//6
			String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//7
			String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//8
			String preferrd="";
			if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10).equalsIgnoreCase("true"))//10
				preferrd = "Y";
			else
				preferrd = "N";

			add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType><AddrPrefFlag>"+preferrd+"</AddrPrefFlag><PrefFormat>STRUCTURED_FORMAT</PrefFormat>";
			add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
			add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
			add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
			add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
			add_xml_str = add_xml_str + "<City>"+city+"</City>";
			add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode>";
			add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
			add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
		}
		CreditCard.mLogger.info("RLOSCommon"+ "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public void setcustomer_enable(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setLocked("cmplx_Customer_Title", false);
		formObject.setLocked("cmplx_Customer_ResidentNonResident", false);
		formObject.setLocked("cmplx_Customer_gender", false);
		//formObject.setLocked("cmplx_Customer_MotherName", false);
		formObject.setLocked("cmplx_Customer_VisaNo", false);
		formObject.setLocked("cmplx_Customer_MAritalStatus", false);
		formObject.setLocked("cmplx_Customer_COuntryOFResidence", false);
		formObject.setLocked("cmplx_Customer_SecNAtionApplicable", false);
		formObject.setLocked("cmplx_Customer_SecNationality", false);
		formObject.setLocked("cmplx_Customer_EMirateOfVisa", false);
		formObject.setLocked("cmplx_Customer_EmirateOfResidence", false);
		formObject.setLocked("cmplx_Customer_yearsInUAE", false);
		formObject.setLocked("cmplx_Customer_CustomerCategory", false);
		formObject.setLocked("cmplx_Customer_GCCNational", false);
		formObject.setLocked("cmplx_Customer_VIPFlag", false);
		//formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
		//formObject.setLocked("cmplx_Customer_VIsaExpiry", false);
	}

	public void parse_cif_eligibility(String output,String operation_name){

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			CreditCard.mLogger.info("inside parse_cif_eligibility");
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
			String passport_list = "";
			//Map<String, String> cif_details = new HashMap<String, String>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
			CreditCard.mLogger.info( doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			for (int nodelen=0; nodelen<nl.getLength();nodelen++){
				Map<String, String> cif_details = new HashMap<String, String>();
				nl.item(nodelen);
				if(nl.item(nodelen).getNodeName().equalsIgnoreCase("CustomerDetails"))
				{
					int no_of_product = 0;
					NodeList childnode  = nl.item(nodelen).getChildNodes();
					for (int childnodelen= 0;childnodelen<childnode.getLength();childnodelen++){
						String tag_name = childnode.item(childnodelen).getNodeName();
						String tag_value=childnode.item(childnodelen).getTextContent();
						if(tag_name!=null && tag_value!=null){
							if(tag_name.equalsIgnoreCase("Products")){
								++no_of_product;
								cif_details.put(tag_name, no_of_product+"");
							}else{
								if(tag_name.equalsIgnoreCase("PassportNum"))
									passport_list = tag_value+ ","+passport_list;
								cif_details.put(tag_name, tag_value);
							}

						}
						else{
							CreditCard.mLogger.info(tag_name+ " tag value: " +tag_value);
						}

					}
					if(cif_details.containsKey("CustId")){
						Cus_details.put(Integer.parseInt(cif_details.get("CustId")), (HashMap<String, String>) cif_details) ;
					}
				}
			}
			int Prim_cif = primary_cif_identify(Cus_details);
			if(operation_name.equalsIgnoreCase("Supplementary_Card_Details")){
				Map<String, String> Supplementary = new HashMap<String, String>();
				Supplementary = Cus_details.get(Prim_cif);
				formObject.setNGValue("Supplementary_CIFNO",Supplementary.get("CustId")==null?"":Supplementary.get("CustId"));
			}
			else if(operation_name.equalsIgnoreCase("Primary_CIF")){
				formObject.clear("q_CIFDetail");
				save_cif_data(Cus_details,Prim_cif);
				if(Prim_cif!=0){
					CreditCard.mLogger.info(Prim_cif+"");
					Map<String, String> prim_entry = new HashMap<String, String>();
					prim_entry = Cus_details.get(Prim_cif);
					String primary_pass = prim_entry.get("PassportNum");
					passport_list = passport_list.replace(primary_pass, "");
					CreditCard.mLogger.info(prim_entry.get("CustId"));
					set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
				}
			}
			CreditCard.mLogger.info( Prim_cif+"");

		}
		catch(Exception e){
			CreditCard.mLogger.info( e.getMessage());
		}

	}

	// Aman Changes done for internal and external liability for the mapping on 13th sept 2017 starts
	public String  getInternalLiabDetails(){
		CreditCard.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		CreditCard.mLogger.info("getInternalLiabDetails sQuery"+sQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("getInternalLiabDetails list size"+OutputXML.size());

		for (int i = 0; i<OutputXML.size();i++){

			String accountType = "";
			String role = "";
			String accNumber = "";
			String acctOpenDate = ""; 
			String acctStatus = "";
			String acctSegment = "";
			String acctSubSegment = "";
			String acctCreditGrade = "";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				accountType = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				role = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				accNumber = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				acctOpenDate = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				acctStatus = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				acctSegment = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				acctSubSegment = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				acctCreditGrade = OutputXML.get(i).get(7).toString();
			}
			if(accNumber!=null && !accNumber.equalsIgnoreCase("") && !accNumber.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<AccountDetails><type_of_account>"+accountType+"</type_of_account>";
				add_xml_str = add_xml_str + "<role>"+role+"</role>";
				add_xml_str = add_xml_str + "<account_number>"+accNumber+"</account_number>";
				add_xml_str = add_xml_str + "<acct_open_date>"+acctOpenDate+"</acct_open_date>";
				add_xml_str = add_xml_str + "<acct_status>"+acctStatus+"</acct_status>";
				add_xml_str = add_xml_str + "<account_segment>"+acctSegment+"</account_segment>";
				add_xml_str = add_xml_str + "<account_sub_segment>"+acctSubSegment+"</account_sub_segment>";
				add_xml_str = add_xml_str + "<credit_grate_code>"+acctCreditGrade+"</credit_grate_code>";
				add_xml_str = add_xml_str + "<cust_type>"+role+"</cust_type></AccountDetails>";
			}
		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String InternalBureauData(){
		CreditCard.mLogger.info( "inside InternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");

		String NoOfContracts = "";
		String Total_Exposure = "";
		String WorstCurrentPaymentDelay = ""; 
		String Worst_PaymentDelay_Last24Months = "";
		String Nof_Records = "";

		String  add_xml_str ="";
		try{
			add_xml_str = add_xml_str + "<InternalBureau><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<full_name>"+formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+""+formObject.getNGValue("cmplx_Customer_LAstNAme")+"</full_name>";// fullname fieldname to be confirmed from onsite
			String sQuery = "SELECT OutstandingAmt,OverdueAmt,CreditLimit FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Child_Wi = '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   TotalOutstandingAmt ,OverdueAmt,TotalLoanAmount FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			CreditCard.mLogger.info("InternalBureauData list size"+OutputXML.size());
			//SKLogger.writeLog("InternalBureauData list "+OutputXML, "");
			CreditCard.mLogger.info( "values");
			float TotOutstandingAmt;
			float TotOverdueAmt;

			
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;

			
			for(int i = 0; i<OutputXML.size();i++){
				CreditCard.mLogger.info( "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					CreditCard.mLogger.info("Totaloutstanding"+i+ "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(0));
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					CreditCard.mLogger.info("TotOverdueAmt"+i+ "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}

			}
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLDerived = formObject.getDataFromDataSource(sQueryDerived);
			 if(OutputXMLDerived!=null && OutputXMLDerived.size()>0 && OutputXMLDerived.get(0)!=null){
			if(!(OutputXMLDerived.get(0).get(0)==null || OutputXMLDerived.get(0).get(0).equals("")) ){
				NoOfContracts= OutputXMLDerived.get(0).get(0).toString();
			}
			if(!(OutputXMLDerived.get(0).get(1)==null || OutputXMLDerived.get(0).get(1).equals("")) ){
				Total_Exposure= OutputXMLDerived.get(0).get(1).toString();
			}
			if(!(OutputXMLDerived.get(0).get(2)==null || OutputXMLDerived.get(0).get(2).equals("")) ){
				WorstCurrentPaymentDelay= OutputXMLDerived.get(0).get(2).toString();
			}
			if(!(OutputXMLDerived.get(0).get(3)==null || OutputXMLDerived.get(0).get(3).equals("")) ){
				Worst_PaymentDelay_Last24Months= OutputXMLDerived.get(0).get(3).toString();
			}
			if(!(OutputXMLDerived.get(0).get(4)==null || OutputXMLDerived.get(0).get(4).equals("")) ){
				Nof_Records= OutputXMLDerived.get(0).get(4).toString();
			}
			 }
			add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";

			add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
			add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>"; // to be populated later
			add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; // to be populated later
			add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
			String sQuerycheque = "SELECT 'DDS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'DDS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLcheque = formObject.getDataFromDataSource(sQuerycheque);


			add_xml_str = add_xml_str + "<cheque_return_3mon>"+OutputXMLcheque.get(2).get(1)+"</cheque_return_3mon>";
			add_xml_str = add_xml_str + "<dds_return_3mon>"+OutputXMLcheque.get(0).get(1)+"</dds_return_3mon>";
			add_xml_str = add_xml_str + "<cheque_return_6mon>"+OutputXMLcheque.get(3).get(1)+"</cheque_return_6mon>";
			add_xml_str = add_xml_str + "<dds_return_6mon>"+OutputXMLcheque.get(1).get(1)+"</dds_return_6mon>";
			add_xml_str = add_xml_str + "<internal_charge_off>"+"N"+"</internal_charge_off><company_flag>N</company_flag></InternalBureau>";// to be populated later



			CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			CreditCard.mLogger.info( "Exception occurred in InternalBureauData()"+e.getMessage()+printException(e));
			return "";
		}

	}

	public String InternalBouncedCheques(){
		CreditCard.mLogger.info("RLOSCommon java file"+ "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"'";
		CreditCard.mLogger.info("InternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("InternalBouncedCheques list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq="";
			String amount = "";
			String reason = ""; 
			String returnDate = "";


			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				internal_bounced_cheques_id = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				bouncedCheq = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				chequeNo = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				amount = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				reason = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				returnDate = OutputXML.get(i).get(6).toString();
			}


			if(applicantID!=null && !applicantID.equalsIgnoreCase("") && !applicantID.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>"+applicantID+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>"+internal_bounced_cheques_id+"</internal_bounced_cheques_id>";
				if (bouncedCheq.equalsIgnoreCase("ICCS")){
					add_xml_str = add_xml_str + "<bounced_cheque>"+"1"+"</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>"+chequeNo+"</cheque_no>";
				add_xml_str = add_xml_str + "<amount>"+amount+"</amount>";
				add_xml_str = add_xml_str + "<reason>"+reason+"</reason>";
				add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; 
				add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
				if (bouncedCheq.equalsIgnoreCase("DDS")){
					add_xml_str = add_xml_str + "<bounced_cheque_dds>"+"1"+"</bounced_cheque_dds>"; 
				}
				add_xml_str=  add_xml_str+"<company_flag>N</company_flag></InternalBouncedCheques>";
			}

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String InternalBureauIndividualProducts(){
		CreditCard.mLogger.info( "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,Consider_For_Obligations,LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,PaymentsAmount,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,Consider_For_Obligations,CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_CardDetails where 	Child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type In ('InternalExposure','CollectionsSummary')";
		CreditCard.mLogger.info("InternalBureauIndividualProducts sQuery"+sQuery);
		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Child_wi = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("InternalBureauIndividualProducts list size"+OutputXML.size());
		CreditCard.mLogger.info("InternalBureauIndividualProducts list "+OutputXML);
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String mol_sal_var = formObject.getNGValue("cmplx_MOL_molsalvar");

		try{
			for (int i = 0; i<OutputXML.size();i++){

				String cifId = "";
				String agreementId = "";
				String product_type = "";
				String loan_start_date = "";
					  
				String loanmaturitydate = "";
				String lastupdatedate = "";

				String outstandingamt = "";
				String totalloanamount = "";
				String Emi = "";
				String paymentmode = ""; 
				String totalnoofinstalments = "";
				String remaininginstalments = "";
				String overdueamt = "";

				String nofdayspmtdelay = "";
				String monthsonbook = "";
				String currentlycurrent = "";
				String currmaxutil = ""; 
				String DPD_30_in_last_6_months = "";
				String DPD_60_in_last_18_months = "";
				String propertyvalue = "";

				String loan_disbursal_date = "";
				String marketingcode = "";
				String DPD_30_in_last_3_months = "";
				String DPD_30_in_last_9_months = ""; 
				String DPD_30_in_last_12_months = "";
				String DPD_30_in_last_18_months = "";
				String DPD_30_in_last_24_months = "";

				String DPD_60_in_last_3_months = "";
				String DPD_60_in_last_6_months = ""; 
				String DPD_60_in_last_9_months = "";
				String DPD_60_in_last_12_months = "";
				String DPD_60_in_last_24_months = "";
				String DPD_90_in_last_3_months = "";
				String DPD_90_in_last_6_months = ""; 
				String DPD_90_in_last_9_months = "";
				String DPD_90_in_last_12_months = "";
				String DPD_90_in_last_18_months = "";
				String DPD_90_in_last_24_months = "";
				String DPD_120_in_last_3_months = ""; 
				String DPD_120_in_last_6_months = "";
				String DPD_120_in_last_9_months = "";
				String DPD_120_in_last_12_months = "";
				String DPD_120_in_last_18_months = "";
				String DPD_120_in_last_24_months = ""; 
				String DPD_150_in_last_3_months = "";
				String DPD_150_in_last_6_months = "";
				String DPD_150_in_last_9_months = "";
				String DPD_150_in_last_12_months = ""; 
				String DPD_150_in_last_18_months = "";
				String DPD_150_in_last_24_months = "";
				String DPD_180_in_last_3_months = "";
				String DPD_180_in_last_6_months = "";
				String DPD_180_in_last_9_months = ""; 
				String DPD_180_in_last_12_months = "";
				String DPD_180_in_last_24_months = "";
				String CardExpiryDate = "";
				String Consider_For_Obligations = "";
				String phase = "";
				String writeoffStat = "";
				String writeoffstatdt = "";
				String lastrepmtdt = "";
				String Limit_increase = "";
				String part_settlement_date = "";
				String part_settlement_amount = "";
				String SchemeCardProduct = "";
				String General_Status = "";
				String Combined_Limit = "";
				String SecuredCard = "";
				String Internal_WriteOff_Check="";
				String EmployerType=formObject.getNGValue("EMploymentDetails_Combo5");
				String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				CreditCard.mLogger.info("Inside for"+ "asdasdasd");
				String paid_installment="";
				if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) )
				{
					cifId = OutputXML.get(i).get(0).toString();
				}
				if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) )
				{
					agreementId = OutputXML.get(i).get(1).toString();
				}				
				if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) )
				{
					product_type = OutputXML.get(i).get(2).toString();
					if (product_type.equalsIgnoreCase("Home In One")){
						product_type="HIO";
					}
					else{
						product_type = OutputXML.get(i).get(63).toString();
					}
				}
								
				if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) )
				{
					loan_start_date = OutputXML.get(i).get(5).toString();
				}
				if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") )
				{
					loanmaturitydate = OutputXML.get(i).get(6).toString();
				}
				if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") )
				{
					lastupdatedate = OutputXML.get(i).get(7).toString();
				}				
				if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") )
				{
					outstandingamt = OutputXML.get(i).get(8).toString();
				}
				if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) )
				{
					totalloanamount = OutputXML.get(i).get(9).toString();
				}
				if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) )
				{
					Emi = OutputXML.get(i).get(10).toString();
				}				
				if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) )
				{
					paymentmode = OutputXML.get(i).get(11).toString();
				}
				if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") )
				{
					//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12).toString();
				}
				if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) )
				{
					remaininginstalments = OutputXML.get(i).get(13).toString();
				}				
				
				if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) )
				{
					overdueamt = OutputXML.get(i).get(15).toString();
				}
				if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) )
				{
					nofdayspmtdelay = OutputXML.get(i).get(16).toString();
				}				
				if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) )
				{
					monthsonbook = OutputXML.get(i).get(17).toString();
				}
				if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) )
				{
					currentlycurrent = OutputXML.get(i).get(18).toString();
				}
				if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) )
				{
					currmaxutil = OutputXML.get(i).get(19).toString();
				}				
				if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) )
				{
					DPD_30_in_last_6_months = OutputXML.get(i).get(20).toString();
				}
				if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) )
				{
					DPD_60_in_last_18_months = OutputXML.get(i).get(21).toString();
				}
				if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) )
				{
					propertyvalue = OutputXML.get(i).get(22).toString();
				}				
				if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) )
				{
					loan_disbursal_date = OutputXML.get(i).get(23).toString();
				}
				if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) )
				{
					marketingcode = OutputXML.get(i).get(24).toString();
				}
				if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) )
				{
					DPD_30_in_last_3_months = OutputXML.get(i).get(25).toString();
				}				
				if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) )
				{
					DPD_30_in_last_9_months	 = OutputXML.get(i).get(26).toString();
				}
				if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) )
				{
					DPD_30_in_last_12_months = OutputXML.get(i).get(27).toString();
				}
				if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) )
				{
					DPD_30_in_last_18_months = OutputXML.get(i).get(28).toString();
				}				
				if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) )
				{
					DPD_30_in_last_24_months = OutputXML.get(i).get(29).toString();
				}
				if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) )
				{
					DPD_60_in_last_3_months = OutputXML.get(i).get(30).toString();
				}
				if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) )
				{
					DPD_60_in_last_6_months = OutputXML.get(i).get(31).toString();
				}				
				if(!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals("")) )
				{
					DPD_60_in_last_9_months = OutputXML.get(i).get(32).toString();
				}
				if(!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals("")) )
				{
					DPD_60_in_last_12_months = OutputXML.get(i).get(33).toString();
				}
				if(!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals("")) )
				{
					DPD_60_in_last_24_months = OutputXML.get(i).get(34).toString();
				}				
				if(!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals("")) )
				{
					DPD_90_in_last_3_months = OutputXML.get(i).get(35).toString();
				}
				if(!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals("")) )
				{
					DPD_90_in_last_6_months = OutputXML.get(i).get(36).toString();
				}
				if(!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals("")) )
				{
					DPD_90_in_last_9_months = OutputXML.get(i).get(37).toString();
				}				
				if(!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals("")) )
				{
					DPD_90_in_last_12_months = OutputXML.get(i).get(38).toString();
				}
				if(!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals("")) )
				{
					DPD_90_in_last_18_months = OutputXML.get(i).get(39).toString();
				}
				if(!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals("")) )
				{
					DPD_90_in_last_24_months = OutputXML.get(i).get(40).toString();
				}				
				if(!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals("")) )
				{
					DPD_120_in_last_3_months = OutputXML.get(i).get(41).toString();
				}
				if(!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals("")) )
				{
					DPD_120_in_last_6_months = OutputXML.get(i).get(42).toString();
				}
				if(!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals("")) )
				{
					DPD_120_in_last_9_months = OutputXML.get(i).get(43).toString();
				}				
				if(!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals("")) )
				{
					DPD_120_in_last_12_months = OutputXML.get(i).get(44).toString();
				}
				if(!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals("")) )
				{
					DPD_120_in_last_18_months = OutputXML.get(i).get(45).toString();
				}
				if(!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals("")) )
				{
					DPD_120_in_last_24_months = OutputXML.get(i).get(46).toString();
				}				
				if(!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals("")) )
				{
					DPD_150_in_last_3_months = OutputXML.get(i).get(47).toString();
					
				}
				if(!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals("")) )
				{
					DPD_150_in_last_6_months = OutputXML.get(i).get(48).toString();
				}
				if(!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals("")) )
				{
					DPD_150_in_last_9_months = OutputXML.get(i).get(49).toString();
				}				
				if(!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals("")) )
				{
					DPD_150_in_last_12_months = OutputXML.get(i).get(50).toString();
				}
				if(!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals("")) )
				{
					DPD_150_in_last_18_months = OutputXML.get(i).get(51).toString();
				}
				if(!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals("")) )
				{
					DPD_150_in_last_24_months = OutputXML.get(i).get(52).toString();
				}				
				if(!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals("")) )
				{
					DPD_180_in_last_3_months = OutputXML.get(i).get(53).toString();
				}
				if(!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals("")) )
				{
					DPD_180_in_last_6_months = OutputXML.get(i).get(54).toString();
				}
				if(!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals("")) )
				{
					DPD_180_in_last_9_months = OutputXML.get(i).get(55).toString();
				}				
				if(!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals("")) )
				{
					DPD_180_in_last_12_months = OutputXML.get(i).get(56).toString();
				}
				if(!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals("")) )
				{
					DPD_180_in_last_24_months = OutputXML.get(i).get(57).toString();
				}
				if(!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals("")) )
				{
					CardExpiryDate = OutputXML.get(i).get(60).toString();
				}

				if(!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals("")) )
				{
					Consider_For_Obligations = OutputXML.get(i).get(61).toString();
					if (Consider_For_Obligations.equalsIgnoreCase("false")){
						Consider_For_Obligations="Y";
					}
					else {
						Consider_For_Obligations="N";
					}
				}

				if(!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals("")) )
				{
					phase = OutputXML.get(i).get(62).toString();
					if (phase.startsWith("C")){
						phase="C";
					}
					else {
						phase="A";
					}
				}
				if(!(OutputXML.get(i).get(64) == null || OutputXML.get(i).get(64).equals("")) )
				{
					writeoffStat = OutputXML.get(i).get(64).toString();
				}
				if(!(OutputXML.get(i).get(65) == null || OutputXML.get(i).get(65).equals("")) )
				{
					writeoffstatdt = OutputXML.get(i).get(65).toString();
				}
				if(!(OutputXML.get(i).get(66) == null || OutputXML.get(i).get(66).equals("")) ){
					lastrepmtdt = OutputXML.get(i).get(66).toString();
				}
				if(!(OutputXML.get(i).get(67) == null || OutputXML.get(i).get(67).equals("")) ){
					Limit_increase = OutputXML.get(i).get(67).toString();
					if (Limit_increase.equalsIgnoreCase("false")){
						Limit_increase="N";
					}
					else{
						Limit_increase="Y";
					}
				}
				if(!(OutputXML.get(i).get(68) == null || OutputXML.get(i).get(68).equals("")) ){
					//commented by aman 22-10-2017 to handdle part settelment data
					//part_settlement_date = OutputXML.get(i).get(67).toString();
					String abc=OutputXML.get(i).get(68).toString();
					abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
					String abcsa[]=abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if(!(OutputXML.get(i).get(69) == null || OutputXML.get(i).get(69).equals("")) ){
					SchemeCardProduct = OutputXML.get(i).get(69).toString();
				}
				if(!(OutputXML.get(i).get(70) == null || OutputXML.get(i).get(70).equals("")) ){
					General_Status = OutputXML.get(i).get(70).toString();
				}
				if(!(OutputXML.get(i).get(71) == null || OutputXML.get(i).get(71).equals("")) ){
					Internal_WriteOff_Check = OutputXML.get(i).get(71).toString();
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='"+SchemeCardProduct+"'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getDataFromDataSource(sQueryCombinedLimit);
				if (sQueryCombinedLimitXML!= null){
					Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='"+SchemeCardProduct+"'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getDataFromDataSource(sQuerySecuredCard);
				if (sQuerySecuredCardXML!= null){
					SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
				}
				
				if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
					CreditCard.mLogger.info("Inside if"+ "asdasdasd");
					add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){					
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
					}
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
						add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
					}	
					add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
					add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
					add_xml_str = add_xml_str + "<role_of_customer>Primary</role_of_customer>"; 

					add_xml_str = add_xml_str + "<start_date>"+loan_start_date+"</start_date>"; 
					add_xml_str = add_xml_str + "<close_date>"+loanmaturitydate+"</close_date>"; 
					add_xml_str = add_xml_str + "<date_last_updated>"+lastupdatedate+"</date_last_updated>"; 
					add_xml_str = add_xml_str + "<outstanding_balance>"+outstandingamt+"</outstanding_balance>"; 
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
						add_xml_str = add_xml_str + "<total_amount>"+totalloanamount+"</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>"+Emi+"</payments_amount>"; 
					add_xml_str = add_xml_str + "<method_of_payment>"+paymentmode+"</method_of_payment>"; 
					add_xml_str = add_xml_str + "<total_no_of_instalments>"+totalnoofinstalments+"</total_no_of_instalments>"; 
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>"; 
					add_xml_str = add_xml_str + "<worst_status>"+writeoffStat+"</worst_status>"; 
					add_xml_str = add_xml_str + "<worst_status_date>"+writeoffstatdt+"</worst_status_date>"; 
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){

						add_xml_str = add_xml_str + "<credit_limit>"+totalloanamount+"</credit_limit>"; 
					}
					add_xml_str = add_xml_str + "<overdue_amount>"+overdueamt+"</overdue_amount>"; 
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+nofdayspmtdelay+"</no_of_days_payment_delay>"; 
					add_xml_str = add_xml_str + "<mob>"+monthsonbook+"</mob>"; 
					add_xml_str = add_xml_str + "<last_repayment_date>"+lastrepmtdt+"</last_repayment_date>"; 
					add_xml_str = add_xml_str + "<currently_current>"+currentlycurrent+"</currently_current>"; 
					add_xml_str = add_xml_str + "<current_utilization>"+currmaxutil+"</current_utilization>"; 
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD_30_in_last_6_months+"</dpd_30_last_6_mon>"; 
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD_60_in_last_18_months+"</dpd_60p_in_last_18_mon>"; 


					add_xml_str = add_xml_str + "<card_product>"+SchemeCardProduct+"</card_product>"; 
					add_xml_str = add_xml_str + "<property_value>"+propertyvalue+"</property_value>"; 
					add_xml_str = add_xml_str + "<disbursal_date>"+loan_disbursal_date+"</disbursal_date>"; 
					add_xml_str = add_xml_str + "<marketing_code>"+marketingcode+"</marketing_code>"; 
					add_xml_str = add_xml_str + "<card_expiry_date>"+CardExpiryDate+"</card_expiry_date>"; 
					add_xml_str = add_xml_str + "<card_upgrade_indicator>"+Limit_increase+"</card_upgrade_indicator>"; 
					add_xml_str = add_xml_str + "<part_settlement_date>"+part_settlement_date+"</part_settlement_date>"; 
					add_xml_str = add_xml_str + "<part_settlement_amount>"+part_settlement_amount+"</part_settlement_amount>"; 
					add_xml_str = add_xml_str + "<part_settlement_reason>"+""+"</part_settlement_reason>"; 
					add_xml_str = add_xml_str + "<limit_expiry_date>"+""+"</limit_expiry_date>"; 
					add_xml_str = add_xml_str + "<no_of_primary_cards>"+CountXML.get(0).get(0)+"</no_of_primary_cards>"; 
					add_xml_str = add_xml_str + "<no_of_repayments_done>"+remaininginstalments+"</no_of_repayments_done>"; 
					add_xml_str = add_xml_str + "<card_segment>"+SchemeCardProduct+"</card_segment>"; 
					add_xml_str = add_xml_str + "<product_type>"+OutputXML.get(i).get(63)+"</product_type>"; 
					add_xml_str = add_xml_str + "<product_category>"+SchemeCardProduct+"</product_category>"; 
					add_xml_str = add_xml_str + "<combined_limit_flag>"+Combined_Limit+"</combined_limit_flag>"; 
					add_xml_str = add_xml_str + "<secured_card_flag>"+SecuredCard+"</secured_card_flag>"; 
					add_xml_str = add_xml_str + "<resch_tko_flag>"+Limit_increase+"</resch_tko_flag>"; 

					add_xml_str = add_xml_str + "<general_status>"+General_Status+"</general_status>"; 
					add_xml_str = add_xml_str + "<consider_for_obligation>"+Consider_For_Obligations+"</consider_for_obligation>"; 
					add_xml_str = add_xml_str + "<limit_increase>"+Limit_increase+"</limit_increase>"; 

					add_xml_str = add_xml_str + "<role>Primary</role>"; 
					add_xml_str = add_xml_str + "<limit>"+""+"</limit>"; 
					add_xml_str = add_xml_str + "<status>"+phase+"</status>";
					add_xml_str = add_xml_str + "<emi>"+Emi+"</emi>"; 
					add_xml_str = add_xml_str + "<os_amt>"+outstandingamt+"</os_amt>"; 

					add_xml_str = add_xml_str + "<dpd_30_in_last_3mon>"+DPD_30_in_last_3_months+"</dpd_30_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_6mon>"+DPD_30_in_last_6_months+"</dpd_30_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_9mon>"+DPD_30_in_last_9_months+"</dpd_30_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_12mon>"+DPD_30_in_last_12_months+"</dpd_30_in_last_12mon>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_18mon>"+DPD_30_in_last_18_months+"</dpd_30_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_24mon>"+DPD_30_in_last_24_months+"</dpd_30_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_3mon>"+DPD_60_in_last_3_months+"</dpd_60_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_6mon>"+DPD_60_in_last_6_months+"</dpd_60_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_9mon>"+DPD_60_in_last_9_months+"</dpd_60_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_12mon>"+DPD_60_in_last_12_months+"</dpd_60_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>"+DPD_60_in_last_18_months+"</dpd_60_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_24mon>"+DPD_60_in_last_24_months+"</dpd_60_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_3mon>"+DPD_90_in_last_3_months+"</dpd_90_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_6mon>"+DPD_90_in_last_6_months+"</dpd_90_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_9mon>"+DPD_90_in_last_9_months+"</dpd_90_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_12mon>"+DPD_90_in_last_12_months+"</dpd_90_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_18mon>"+DPD_90_in_last_18_months+"</dpd_90_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_24mon>"+DPD_90_in_last_24_months+"</dpd_90_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_3mon>"+DPD_120_in_last_3_months+"</dpd_120_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_6mon>"+DPD_120_in_last_6_months+"</dpd_120_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_9mon>"+DPD_120_in_last_9_months+"</dpd_120_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_12mon>"+DPD_120_in_last_12_months+"</dpd_120_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_18mon>"+DPD_120_in_last_18_months+"</dpd_120_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_24mon>"+DPD_120_in_last_24_months+"</dpd_120_in_last_24mon>";

					add_xml_str = add_xml_str + "<dpd_150_in_last_3mon>"+DPD_150_in_last_3_months+"</dpd_150_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_6mon>"+DPD_150_in_last_6_months+"</dpd_150_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_9mon>"+DPD_150_in_last_9_months+"</dpd_150_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_12mon>"+DPD_150_in_last_12_months+"</dpd_150_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_18mon>"+DPD_150_in_last_18_months+"</dpd_150_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_24mon>"+DPD_150_in_last_24_months+"</dpd_150_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_3mon>"+DPD_180_in_last_3_months+"</dpd_180_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_6mon>"+DPD_180_in_last_6_months+"</dpd_180_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_9mon>"+DPD_180_in_last_9_months+"</dpd_180_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_12mon>"+DPD_180_in_last_12_months+"</dpd_180_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_18mon>"+""+"</dpd_180_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_24mon>"+DPD_180_in_last_24_months+"</dpd_180_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<last_temp_limit_exp>"+""+"</last_temp_limit_exp>"; 
					add_xml_str = add_xml_str + "<last_per_limit_exp>"+""+"</last_per_limit_exp>"; 
					add_xml_str = add_xml_str + "<security_cheque_amt>"+""+"</security_cheque_amt>"; 
					add_xml_str = add_xml_str + "<mol_salary_variance>"+mol_sal_var+"</mol_salary_variance>";
					if(Kompass!=null){
						if(Kompass.equalsIgnoreCase("true")){
							add_xml_str = add_xml_str + "<kompass>"+"Y"+"</kompass>";
						}
						else{
							add_xml_str = add_xml_str + "<kompass>"+"N"+"</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+EmployerType+"</employer_type>"; 


					if (totalnoofinstalments!=null && remaininginstalments!=null && !totalnoofinstalments.equals("") &&  !remaininginstalments.equals("")) {
						paid_installment= Integer.toString(Integer.parseInt(totalnoofinstalments) -Integer.parseInt(remaininginstalments));
						CreditCard.mLogger.info( "paid_installment"+paid_installment);

					}
					if (ReqProd.equalsIgnoreCase("Credit Card")){

						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					}
					else{
						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><company_flag>N</company_flag><type_of_od>"+""+"</type_of_od><amt_paid_last6mnths>"+""+"</amt_paid_last6mnths></InternalBureauIndividualProducts>";

					}
				}	

			}
		}
		catch(Exception e){
			CreditCard.mLogger.info( "Internal liab tag Cration: "+ printException(e));
		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(){
		CreditCard.mLogger.info("RLOSCommon java file"+ "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
		CreditCard.mLogger.info("InternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("InternalBureauPipelineProducts list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String Product = "";
			String lastUpdateDate = ""; 
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";
			String agreementId = "";

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				Product = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				lastUpdateDate = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				TotAmount = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				TotNoOfInstlmnt = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				TotLoanAmt = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				agreementId = OutputXML.get(i).get(7).toString();
			}

			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
				add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+agreementId+"</internal_bureau_pipeline_products_id>";// to be populated later
				add_xml_str = add_xml_str + "<ppl_provider_no>"+""+"</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_product>"+Product+"</ppl_product>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>"+""+"</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_phase>"+""+"</ppl_phase>"; // to be populated later

				add_xml_str = add_xml_str + "<ppl_role>"+Product+"</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmount+"</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+TotNoOfInstlmnt+"</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>"+TotLoanAmt+"</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+""+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></InternalBureauPipelineProducts>"; // to be populated later
			}

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauData(){
		CreditCard.mLogger.info("RLOSCommon java file"+ "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		CreditCard.mLogger.info("ExternalBureauData sQuery"+sQuery+ "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  Child_wi  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			CreditCard.mLogger.info("ExternalBureauData list size"+OutputXML.size()+"");
			List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
			/*
		float TotOutstandingAmt = 0.0f;
		float TotOverdueAmt = 0.0f;
		float TotalExposure = 0.0f;
		for(int i = 0; i<OutputXML.size();i++){
			if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(1));
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(2));
			}
			if(OutputXML.get(i).get(3)!=null && !OutputXML.get(i).get(3).isEmpty() &&  !OutputXML.get(i).get(3).equals("") && !OutputXML.get(i).get(3).equalsIgnoreCase("null") ){
					TotalExposure = TotalExposure + Float.parseFloat(OutputXML.get(i).get(3));
			}
		}*/
			if (AecbHistQueryData.get(0).get(0).equalsIgnoreCase("0")){


				String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
				String fullnm=formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
				String TotalOutstanding="";
				String TotalOverdue="";
				String NoOfContracts="";
				String Total_Exposure="";
				String WorstCurrentPaymentDelay="";
				String Worst_PaymentDelay_Last24Months="";
				String Worst_Status_Last24Months="";
				String Nof_Records="";
				String NoOf_Cheque_Return_Last3="";
				String Nof_DDES_Return_Last3Months="";
				String Nof_Cheque_Return_Last6="";
				String DPD30_Last6Months="";
				add_xml_str = add_xml_str + "<ExternalBureau>"; 
				add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";

				add_xml_str = add_xml_str + "<full_name>"+fullnm+"</full_name>"; 
				add_xml_str = add_xml_str + "<total_out_bal>"+TotalOutstanding+"</total_out_bal>";

				add_xml_str = add_xml_str + "<total_overdue>"+TotalOverdue+"</total_overdue>";
				add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";
				add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
				add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>";
				add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; 
				add_xml_str = add_xml_str + "<worst_status_24>"+Worst_Status_Last24Months+"</worst_status_24>"; 


				add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
				add_xml_str = add_xml_str + "<cheque_return_3mon>"+NoOf_Cheque_Return_Last3+"</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>"+Nof_DDES_Return_Last3Months+"</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>"+Nof_Cheque_Return_Last6+"</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>"+DPD30_Last6Months+"</dds_return_6mon>";
				add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+""+"</prod_external_writeoff_amount>";

				add_xml_str = add_xml_str + "<no_months_aecb_history >"+AecbHistQueryData.get(0).get(0)+"</no_months_aecb_history >";

				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";



				CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}
			else{
				for (int i = 0; i<OutputXML.size();i++){

					String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
					String fullnm="";
					String TotalOutstanding="";
					String TotalOverdue="";
					String NoOfContracts="";
					String Total_Exposure="";
					String WorstCurrentPaymentDelay="";
					String Worst_PaymentDelay_Last24Months="";
					String Worst_Status_Last24Months="";
					String Nof_Records="";
					String NoOf_Cheque_Return_Last3="";
					String Nof_DDES_Return_Last3Months="";
					String Nof_Cheque_Return_Last6="";
					String DPD30_Last6Months="";
					if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
						fullnm = OutputXML.get(i).get(1).toString();
					}				
					if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
						TotalOutstanding = OutputXML.get(i).get(2).toString();

					}
					if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
						TotalOverdue = OutputXML.get(i).get(3).toString();
					}
					if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
						NoOfContracts = OutputXML.get(i).get(4).toString();
					}				
					if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
						Total_Exposure = OutputXML.get(i).get(5).toString();
					}
					if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
						WorstCurrentPaymentDelay = OutputXML.get(i).get(6).toString();
					}
					if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") ){
						Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7).toString();
					}				
					if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") ){
						Worst_Status_Last24Months = OutputXML.get(i).get(8).toString();
					}
					if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
						Nof_Records = OutputXML.get(i).get(9).toString();
					}
					if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
						NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10).toString();
					}				
					if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
						Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11).toString();
					}
					if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") ){
						//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
						Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12).toString();
					}
					if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
						DPD30_Last6Months = OutputXML.get(i).get(13).toString();
					}

					add_xml_str = add_xml_str + "<ExternalBureau>"; 
					add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";

					add_xml_str = add_xml_str + "<full_name>"+fullnm+"</full_name>"; 
					add_xml_str = add_xml_str + "<total_out_bal>"+TotalOutstanding+"</total_out_bal>";

					add_xml_str = add_xml_str + "<total_overdue>"+TotalOverdue+"</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>";
					add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; 
					add_xml_str = add_xml_str + "<worst_status_24>"+Worst_Status_Last24Months+"</worst_status_24>"; 


					add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
					add_xml_str = add_xml_str + "<cheque_return_3mon>"+NoOf_Cheque_Return_Last3+"</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>"+Nof_DDES_Return_Last3Months+"</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>"+Nof_Cheque_Return_Last6+"</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>"+DPD30_Last6Months+"</dds_return_6mon>";
					add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+""+"</prod_external_writeoff_amount>";

					add_xml_str = add_xml_str + "<no_months_aecb_history >"+AecbHistQueryData.get(0).get(0)+"</no_months_aecb_history >";

					add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";


				}
				CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}

		}

		catch(Exception e)
		{
			CreditCard.mLogger.info("RLOSCommon"+ "Exception occurred in externalBureauData()"+e.getMessage()+printException(e));
			return null;
		}
	}

	public String ExternalBouncedCheques(){
		CreditCard.mLogger.info("RLOSCommon java file"+ "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		CreditCard.mLogger.info("ExternalBouncedCheques sQuery"+sQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("ExternalBouncedCheques list size"+OutputXML.size());

		for (int i = 0; i<OutputXML.size();i++){

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = ""; 
			String returnDate = "";
			String providerNo = "";


			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				chqNo = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				Amount = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				Reason = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				providerNo = OutputXML.get(i).get(5).toString();
			}


			add_xml_str = add_xml_str + "<ExternalBouncedCheques><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<external_bounced_cheques_id>"+""+"</external_bounced_cheques_id>";
			add_xml_str = add_xml_str + "<bounced_cheque>"+""+"</bounced_cheque>";
			add_xml_str = add_xml_str + "<cheque_no>"+chqNo+"</cheque_no>";
			add_xml_str = add_xml_str + "<amount>"+Amount+"</amount>";
			add_xml_str = add_xml_str + "<reason>"+Reason+"</reason>";
			add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; // to be populated later
			add_xml_str = add_xml_str + "<provider_no>"+providerNo+"</provider_no><company_flag>N</company_flag></ExternalBouncedCheques>"; // to be populated later


		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauIndividualProducts(){
		CreditCard.mLogger.info( "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails where Child_wi= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails where Child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' ";
		CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+OutputXML.size());
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		for (int i = 0; i<OutputXML.size();i++){

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String AgreementId = "";
			String ContractType = "";
			String provider_no = ""; 
			String phase = "";
			String CustRoleType = ""; 
			String start_date = "";	
			String close_date = "";
			String OutStanding_Balance = "";
			String TotalAmt = "";
			String PaymentsAmt = "";
			String TotalNoOfInstalments = "";
			String RemainingInstalments = "";
			String WorstStatus = ""; 
			String WorstStatusDate = "";
			String CreditLimit = ""; 
			String OverdueAmt = "";
			String NofDaysPmtDelay = "";
			String MonthsOnBook = "";
			String last_repayment_date = "";
			String DPD60Last18Months = "";
			String AECBHistMonthCnt = "";
			String DPD30Last6Months = "";
			String currently_current = "";
			String current_utilization = "";
			String delinquent_in_last_3months = "";
			String QC_Amt = "";
			String QC_emi = "";
			String CAC_Indicator = "";
			String consider_for_obligation="";

			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				AgreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				ContractType = OutputXML.get(i).get(2).toString();
				try{
					String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
					CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+cardquery);
					List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
					ContractType=cardqueryXML.get(0).get(0);
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType"+ContractType+ "ContractType");
				}
				catch(Exception e){
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+e+ "Exception");

					ContractType= OutputXML.get(i).get(2).toString();
				}
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				provider_no = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				phase = OutputXML.get(i).get(4).toString();
				if (phase.startsWith("A")){
					phase="A";
				}
				else {
					phase="C";
				}
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				CustRoleType = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				start_date = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				close_date = OutputXML.get(i).get(7).toString();
			}				
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				OutStanding_Balance = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				TotalAmt = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				PaymentsAmt = OutputXML.get(i).get(10).toString();
			}
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				TotalNoOfInstalments = OutputXML.get(i).get(11).toString();
			}
			if(!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12).equals("")) ){
				RemainingInstalments = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				WorstStatus = OutputXML.get(i).get(13).toString();
			}
			if(!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals("")) ){
				WorstStatusDate = OutputXML.get(i).get(14).toString();
			}				
			if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
				CreditLimit = OutputXML.get(i).get(15).toString();
			}
			if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
				OverdueAmt = OutputXML.get(i).get(16).toString();
			}
			if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
				NofDaysPmtDelay = OutputXML.get(i).get(17).toString();
			}				
			if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) ){
				MonthsOnBook = OutputXML.get(i).get(18).toString();
			}
			if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) ){
				last_repayment_date = OutputXML.get(i).get(19).toString();
			}
			if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) ){
				currently_current = OutputXML.get(i).get(20).toString();
			}
			if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) ){
				current_utilization = OutputXML.get(i).get(21).toString();
			}
			if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) ){
				DPD30Last6Months = OutputXML.get(i).get(22).toString();
			}
			if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) ){
				DPD60Last18Months = OutputXML.get(i).get(23).toString();
			}
			if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) ){
				AECBHistMonthCnt = OutputXML.get(i).get(24).toString();
			}				


			if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) ){
				delinquent_in_last_3months = OutputXML.get(i).get(25).toString();
			}
			if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) ){
				QC_Amt = OutputXML.get(i).get(26).toString();
			}
			if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) ){
				QC_emi = OutputXML.get(i).get(27).toString();
			}
			if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) ){
				CAC_Indicator = OutputXML.get(i).get(28).toString();
				if (CAC_Indicator != null && !(CAC_Indicator.equalsIgnoreCase(""))){
					if (CAC_Indicator.equalsIgnoreCase("true")){
						CAC_Indicator="Y";
					}
					else {
						CAC_Indicator="N";
					}
				}
			}
			String TakeOverIndicator="";
			if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
				TakeOverIndicator = OutputXML.get(i).get(29).toString();
				if (TakeOverIndicator != null && !(TakeOverIndicator.equalsIgnoreCase(""))){
					if (TakeOverIndicator.equalsIgnoreCase("true")){
						TakeOverIndicator="Y";
					}
					else {
						TakeOverIndicator="N";
					}
				}
			}
			if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(30).toString();
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}

			add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<external_bureau_individual_products_id>"+AgreementId+"</external_bureau_individual_products_id>";
			add_xml_str = add_xml_str + "<contract_type>"+ContractType+"</contract_type>";
			add_xml_str = add_xml_str + "<provider_no>"+provider_no+"</provider_no>";
			add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
			add_xml_str = add_xml_str + "<role_of_customer>"+CustRoleType+"</role_of_customer>";
			add_xml_str = add_xml_str + "<start_date>"+start_date+"</start_date>"; 

			add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
			add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding_Balance+"</outstanding_balance>";
			add_xml_str = add_xml_str + "<total_amount>"+TotalAmt+"</total_amount>";
			add_xml_str = add_xml_str + "<payments_amount>"+PaymentsAmt+"</payments_amount>";
			add_xml_str = add_xml_str + "<total_no_of_instalments>"+TotalNoOfInstalments+"</total_no_of_instalments>";
			add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+RemainingInstalments+"</no_of_remaining_instalments>";
			add_xml_str = add_xml_str + "<worst_status>"+WorstStatus+"</worst_status>";
			add_xml_str = add_xml_str + "<worst_status_date>"+WorstStatusDate+"</worst_status_date>";

			add_xml_str = add_xml_str + "<credit_limit>"+CreditLimit+"</credit_limit>";
			add_xml_str = add_xml_str + "<overdue_amount>"+OverdueAmt+"</overdue_amount>";
			add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+NofDaysPmtDelay+"</no_of_days_payment_delay>";
			add_xml_str = add_xml_str + "<mob>"+MonthsOnBook+"</mob>";
			add_xml_str = add_xml_str + "<last_repayment_date>"+last_repayment_date+"</last_repayment_date>";
			if (currently_current != null && currently_current.equalsIgnoreCase("1"))
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			else
			{
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
			add_xml_str = add_xml_str + "<current_utilization>"+current_utilization+"</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD30Last6Months+"</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD60Last18Months+"</dpd_60p_in_last_18_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>"+AECBHistMonthCnt+"</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>"+CAC_Indicator+"</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>"+QC_emi+"</qc_emi>";
			if (ReqProd.equalsIgnoreCase("Credit Card")){
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate")+"</cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate")+"</cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator></ExternalBureauIndividualProducts>";
			}

		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String ExternalBureauManualAddIndividualProducts(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info("ExternalBureauManualAddIndividualProducts sQuery");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+Man_liab_row_count);
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String Take_over_Indicator = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 3).equalsIgnoreCase("true")?"Y":"N"); //0
				String cac_Indicator = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5).equalsIgnoreCase("true")?"Y":"N"); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				if (cac_Indicator.equalsIgnoreCase("true")){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				String consider_for_obligation = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8).equalsIgnoreCase("true")?"Y":"N"); //0
				//String MOB = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String delinquent_in_last_3months = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13).equalsIgnoreCase("true")?"1":"0");
				String dpd_30_last_6_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14).equalsIgnoreCase("true")?"1":"0");
				String dpd_60p_in_last_18_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15).equalsIgnoreCase("true")?"1":"0");
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+applicant_id+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>"+Type_of_Contract+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>Main</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>"+modifiedDate+"</start_date>"; 

				add_xml_str = add_xml_str + "<close_date></close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding+"</outstanding_balance>";
				add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
				add_xml_str = add_xml_str + "<payments_amount>"+Limit+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status></worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>"+EMI+"</credit_limit>";
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>"+mob+"</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>"+Utilization+"</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+dpd_30_last_6_mon+"</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+dpd_60p_in_last_18_mon+"</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>"+cac_Indicator+"</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>"+Qc_Emi+"</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>"+Qc_amt+"</qc_amount><cac_bank_name>"+formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate")+"</cac_bank_name><take_over_indicator>"+Take_over_Indicator+"</take_over_indicator><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";

			}

		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauPipelineProducts(){
		CreditCard.mLogger.info( "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,Total_Amount,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline from ng_rlos_cust_extexpo_LoanDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine  from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
		CreditCard.mLogger.info("ExternalBureauPipelineProducts sQuery"+sQuery);
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("ExternalBureauPipelineProducts list size"+OutputXML.size());
		String cifId=formObject.getNGValue("cmplx_Customer_CIFNO");

		for (int i = 0; i<OutputXML.size();i++){

			String agreementID = "";
			String ProviderNo="";
			String contractType = "";
			String productType = "";
			String role = ""; 
			String lastUpdateDate = "";
			String TotAmt = "";
			String noOfInstalmnt = "";
			String creditLimit = "";
			String noOfDayinPpl = "";
			String consider_for_obligation="";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
				contractType = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				productType = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				role = OutputXML.get(i).get(4).toString();
			}
			if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !OutputXML.get(i).get(5).equals("") && !OutputXML.get(i).get(5).equalsIgnoreCase("null") ){
				lastUpdateDate = OutputXML.get(i).get(5).toString();
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
				TotAmt = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				noOfInstalmnt = OutputXML.get(i).get(7).toString();
			}
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				creditLimit = OutputXML.get(i).get(8).toString();
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !OutputXML.get(i).get(9).equals("") && !OutputXML.get(i).get(9).equalsIgnoreCase("null") ){
				noOfDayinPpl = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(10).toString();
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}

			add_xml_str = add_xml_str + "<ExternalBureauPipelineProducts><applicant_ID>"+cifId+"</applicant_ID>";
			add_xml_str = add_xml_str + "<external_bureau_pipeline_products_id>"+agreementID+"</external_bureau_pipeline_products_id>";
			add_xml_str = add_xml_str + "<ppl_provider_no>"+ProviderNo+"</ppl_provider_no>";
			add_xml_str = add_xml_str + "<ppl_type_of_contract>"+contractType+"</ppl_type_of_contract>";
			add_xml_str = add_xml_str + "<ppl_type_of_product>"+productType+"</ppl_type_of_product>";
			add_xml_str = add_xml_str + "<ppl_phase>"+"PIPELINE"+"</ppl_phase>";
			add_xml_str = add_xml_str + "<ppl_role>"+role+"</ppl_role>"; 

			add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
			add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmt+"</ppl_total_amount>";
			add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+noOfInstalmnt+"</ppl_no_of_instalments>";
			add_xml_str = add_xml_str + "<ppl_credit_limit>"+creditLimit+"</ppl_credit_limit>";

			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauPipelineProducts>"; // to be populated later




		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	// Aman Changes done for internal and external liability for the mapping on 13th sept 2017 ends
	public String getProduct_details(){
		CreditCard.mLogger.info( "inside getProduct_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		CreditCard.mLogger.info( "inside getCustAddress_details add_row_count+ : "+prod_row_count);
		String  prod_xml_str ="";
		String Manual_Dev=formObject.getNGValue("cmplx_DEC_Manual_Deviation");
		String FinalLimitQuery="select isnull(Final_limit,0) from ng_rlos_IGR_Eligibility_CardProduct where child_wi='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitXML = formObject.getDataFromDataSource(FinalLimitQuery);
		String finalLimit="";
		if (FinalLimitXML.size()!= 0){
			finalLimit=FinalLimitXML.get(0).get(0);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit", finalLimit);
		}

		for (int i = 0; i<prod_row_count;i++){
			String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
			String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
			String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3);//3
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4
			String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
			String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);//6
			String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//7
			String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 12);//8
			limitExpiry=Convert_dateFormat(limitExpiry, "dd/MM/yyyy", "yyyy-MM-dd");

			prod_xml_str = prod_xml_str + "<ApplicationDetails><product_type>"+(prod_type.equalsIgnoreCase("Conventional")?"CON":"ISL")+"</product_type>";
			prod_xml_str = prod_xml_str + "<app_category>"+formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")+"</app_category>";
			prod_xml_str = prod_xml_str + "<requested_product>"+(reqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")+"</requested_product>";
			prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
			prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
			prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
			prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
			prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
			prod_xml_str = prod_xml_str + "<tenure>"+tenure+"</tenure>";
			prod_xml_str = prod_xml_str + "<customer_type>"+(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"NTB":"Existing")+"</customer_type>";
			if(Manual_Dev!=null){ 
				if(Manual_Dev.equalsIgnoreCase("true")){
					prod_xml_str = prod_xml_str + "<limit_expiry_date>"+limitExpiry+"</limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation>Y</manual_deviation></ApplicationDetails>";
				}
				else{
					prod_xml_str = prod_xml_str + "<limit_expiry_date>"+limitExpiry+"</limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation>N</manual_deviation></ApplicationDetails>";
				}   
			}
			else {
				prod_xml_str = prod_xml_str + "<limit_expiry_date>"+limitExpiry+"</limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation></manual_deviation></ApplicationDetails>";

			}


		}
		CreditCard.mLogger.info( "Address tag Cration: "+ prod_xml_str);
		return prod_xml_str;
	}
	public String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		CreditCard.mLogger.info( "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date==null || date.equals(""))
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
			CreditCard.mLogger.info( "Exception occurred in parsing date:"+e.getMessage());
		}
		return new_date;
	}


	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			CreditCard.mLogger.info( "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 


			String WI_Name = formObject.getWFWorkitemName();
			CreditCard.mLogger.info( "inside save_cif_data methos wi_name: "+WI_Name );
			Map<String, String> curr_entry = new HashMap<String, String>();
			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();

			while(itr.hasNext()){
				List<String> Cif_data=new ArrayList<String>();
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();
				curr_entry = entry.getValue();

				Cif_data.add(curr_entry.get("SearchType")!=null?curr_entry.get("SearchType"):"");
				Cif_data.add(curr_entry.get("CustId")!=null?curr_entry.get("CustId"):"");
				Cif_data.add(curr_entry.get("PassportNum")!=null?curr_entry.get("PassportNum"):"");
				Cif_data.add(curr_entry.get("BlacklistFlag")!=null?curr_entry.get("BlacklistFlag"):"");
				Cif_data.add(curr_entry.get("DuplicationFlag")!=null?curr_entry.get("DuplicationFlag"):"");
				Cif_data.add(curr_entry.get("NegatedFlag")!=null?curr_entry.get("NegatedFlag"):"");
				Cif_data.add(curr_entry.get("Products")!=null?curr_entry.get("Products"):"");
				Cif_data.add(curr_entry.get("BlacklistReasonCode")!=null?curr_entry.get("BlacklistReasonCode"):"");
				Cif_data.add(curr_entry.get("BlacklistDate")!=null?curr_entry.get("BlacklistDate"):"");
				Cif_data.add(curr_entry.get("NegatedReasonCode")!=null?curr_entry.get("NegatedReasonCode"):"");
				Cif_data.add(curr_entry.get("NegatedDate")!=null?curr_entry.get("NegatedDate"):"");
				if(curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")){
					Cif_data.add("Y");
				}
				else{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItemFromList("q_cif_detail", Cif_data);

				CreditCard.mLogger.info( "data to dave in cif details grid: "+ Cif_data);
				curr_entry=null;
				Cif_data=null;
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured while saving data for Customer Eligibility : "+ e.getMessage());
		}


	}
	public void set_nonprimaryPassport(String cif_id, String pass_list){//changes done to save CIF without 0.
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.setNGValue("cmplx_Customer_CIFNO",cif_id);
			if(pass_list.contains(",")){
				String[] pass_list_arr = pass_list.split(",");
				for(int i= 0; i<pass_list_arr.length && i<4 ; i++){
					formObject.setNGValue("cmplx_Customer_Passport"+(i+2),pass_list_arr[i]);
				}
			}

		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured while seting non primary CIF: "+ e.getMessage());
		}

	}

	public int primary_cif_identify(Map<Integer, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry = new HashMap<String, String>();
			Map<String, String> curr_entry = new HashMap<String, String>();


			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();

				curr_entry = entry.getValue();
				if(curr_entry.get("SearchType").equalsIgnoreCase("Internal"))
				{
					if(primary_cif==0 && curr_entry.containsKey("Products")){
						primary_cif = entry.getKey();
					}
					else if(curr_entry.containsKey("Products"))
					{
						prim_entry = cusDetails.get(primary_cif+"");
						int prim_entry_prod_no = Integer.parseInt(prim_entry.get("Products"));
						int curr_entry_prod_no = Integer.parseInt(curr_entry.get("Products"));

						if(curr_entry_prod_no>prim_entry_prod_no){
							primary_cif = entry.getKey();
						}
						else if(curr_entry_prod_no==prim_entry_prod_no)
						{
							int prim_cif_no = Integer.parseInt(prim_entry.get("CustId"));
							int curr_cif_no = Integer.parseInt(curr_entry.get("CustId"));
							if(curr_cif_no>prim_cif_no)
								primary_cif = curr_cif_no;

						}

					}
				}

			}

		}
		catch(Exception e){
			CreditCard.mLogger.info( e.getMessage());
		}

		return primary_cif;
	}

	//Code for Customer Updated.
	public String CustomerUpdate(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg = "";
		String outputResponse = "";
		String   ReturnCode="";
		String cif_verf_status = formObject.getNGValue("is_cust_verified");
		String Existingcust = formObject.getNGValue("cmplx_Customer_NTB");
		if(Existingcust.equalsIgnoreCase("false")){
			cif_verf_status = "Y";
		}
		String Cif_lock_status = formObject.getNGValue("Is_CustLock");
		//String Cif_unlock_status = formObject.getNGValue("is_cust_verified");
		CreditCard.mLogger.info( "cif_verf_status : "+ cif_verf_status);
		CreditCard.mLogger.info( "cif_Lock_status : "+ Cif_lock_status);
		if (cif_verf_status.equalsIgnoreCase("")||cif_verf_status.equalsIgnoreCase("N")){
			outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","CIF_verify");

			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(ReturnCode.equalsIgnoreCase("0000")){
				formObject.setNGValue("is_cust_verified", "Y");
				cif_verf_status="Y";
				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL019");
			}
			else{
				formObject.setNGValue("is_cust_verified", "N");
				CreditCard.mLogger.info( "Error in Cif Enquiry operation Return code is: "+ReturnCode);
				alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL015");
			}
		}

		if (cif_verf_status.equalsIgnoreCase("Y")&&(Cif_lock_status.equalsIgnoreCase("")||Cif_lock_status.equalsIgnoreCase("N")))
		{
			CreditCard.mLogger.info( "Inside Lock and Update Customer");
			outputResponse = GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(ReturnCode.equalsIgnoreCase("0000"))
			{
				Cif_lock_status="Y";
				CreditCard.mLogger.info( "Locked Successfully and now Unlock and update customer");
				formObject.setNGValue("Is_CustLock", "Y");
				outputResponse = GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

				if(ReturnCode.equalsIgnoreCase("0000"))
				{
					formObject.setNGValue("Is_CustLock", "N");
					Cif_lock_status="N";
					CreditCard.mLogger.info( "Cif UnLock sucessfull");

					outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);

					if(ReturnCode.equalsIgnoreCase("0000")){
						formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
						CreditCard.mLogger.info("value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						valueSetCustomer(outputResponse,"");    
						formObject.setEnabled("DecisionHistory_Button3", false);
						CreditCard.mLogger.info("CUSTOMER_UPDATE_REQ is generated");
						CreditCard.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL018");
					}
					else{
						alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL017");
						formObject.setEnabled("DecisionHistory_Button3", true);
						CreditCard.mLogger.info("Customer Update operation Failed");
						formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
					}
					CreditCard.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					formObject.RaiseEvent("WFSave");
					CreditCard.mLogger.info("after saving the flag");
				}
				else{
					CreditCard.mLogger.info( "Cif UnLock failed return code: "+ReturnCode);
					alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL016");
				}

			}
			else{
				formObject.setNGValue("Is_CustLock", "N");
				CreditCard.mLogger.info( "Error in Cif Lock operation Return code is: "+ReturnCode);
				alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL015");
			}

		}
		else if(cif_verf_status.equalsIgnoreCase("Y")&& Cif_lock_status.equalsIgnoreCase("Y"))
		{
			outputResponse = GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(ReturnCode.equalsIgnoreCase("0000"))
			{
				formObject.setNGValue("Is_CustLock", "N");
				Cif_lock_status="N";
				CreditCard.mLogger.info( "Cif UnLock sucessfull");

				outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);

				if(ReturnCode.equalsIgnoreCase("0000")){
					formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
					CreditCard.mLogger.info("value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					valueSetCustomer(outputResponse,"");    
					CreditCard.mLogger.info("CUSTOMER_UPDATE_REQ is generated");
					CreditCard.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				}
				else{
					CreditCard.mLogger.info("ACCOUNT_MAINTENANCE_REQ is not generated");
					formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
				}
				CreditCard.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				formObject.RaiseEvent("WFSave");
				CreditCard.mLogger.info("after saving the flag");
				if(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ").equalsIgnoreCase("Y"))
				{ 
					CreditCard.mLogger.info("inside if condition");
					formObject.setEnabled("DecisionHistory_Button3", false); 
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","FetchDetails#Customer Updated Successful!!","",hm));
				}
				else{
					formObject.setEnabled("DecisionHistory_Button3", true);
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","FetchDetails#Customer Updated Fail!!","",hm));
				}

			}
			else{
				CreditCard.mLogger.info( "Cif UnLock failed return code: "+ReturnCode);
				alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL016");
			}
		}
		return alert_msg;
	}

	public void loadInSmartGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{

			String query="select creditremarks,cpvremarks,smart_wi_name from NG_RLOS_GR_SmartCheck with (nolock) where smart_wi_name='"+formObject.getWFWorkitemName()+"'";
			CreditCard.mLogger.info("Inside CCCommon ->loadInSmartGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			CreditCard.mLogger.info("Inside CCCommon ->loadInSmartGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2));
			/* try{
	             String date=list.get(0).get(0);
	             Date d=new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
	             CreditCard.mLogger.info("PersonnalLoanS>","value of date is:"+d.toString());*/
			for (List<String> a : list) 
			{
				/*List<String> mylist=new ArrayList<String>();
	                 mylist.add(d.toString());
	                 mylist.add(list.get(0).get(1));
	                 mylist.add(list.get(0).get(2));
	                 mylist.add(list.get(0).get(3));
	                 mylist.add(list.get(0).get(4));
	                 mylist.add(list.get(0).get(5));*/
				formObject.addItemFromList("cmplx_SmartCheck_SmartCheckGrid", a);
			}
		}catch(Exception e){  CreditCard.mLogger.info("Exception Occurred loadInDecGrid :"+e.getMessage());}    
	}
	public static String ExecuteQuery_APdelete(String tableName,String sWhere, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>"+
		"<APDelete_Input><Option>APDelete</Option>"+
		"<TableName>"+tableName+"</TableName>"+
		"<WhereClause>"+sWhere+"</WhereClause>"+
		"<EngineName>"+cabinetName+"</EngineName>"+
		"<SessionId>"+sessionId+"</SessionId>"+
		"</APDelete_Input>";
		return sInputXML;	
	}
	public static String ExecuteQuery_APInsert(String tableName,String columnName,String strValues, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>" +"\n"+
		"<APInsert_Input>" +"\n"+
		"<Option>APInsert</Option>" +"\n"+
		"<TableName>" + tableName + "</TableName>" +"\n"+
		"<ColName>" + columnName + "</ColName>" +"\n"+
		"<Values>" + strValues + "</Values>" +"\n"+
		"<EngineName>" + cabinetName + "</EngineName>" +"\n"+
		"<SessionId>" + sessionId + "</SessionId>" +"\n"+
		"</APInsert_Input>";
		return sInputXML;	
	}





	public void saveSmartCheckGrid(){

		CreditCard.mLogger.info( "Inside saveSmartCheckGrid: "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query="insert into NG_RLOS_GR_SmartCheck(smart_wi_name, creditremarks, decision) values('"+formObject.getWFWorkitemName()+"','"+formObject.getNGValue("SmartCheck_CR_Remarks")+"','"+formObject.getNGValue("cmplx_SmartCheck_Decision")+"')";
		CreditCard.mLogger.info("Query is"+query);
		formObject.saveDataIntoDataSource(query);            
	}

	// added by abhishek on 13 july to disable only buttons and not whole fragment
	public void disableButtonsCC(String fragName){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String activityName = formObject.getWFActivityName(); 
		if(fragName.equalsIgnoreCase("customer")){

			formObject.setLocked("Customer_ReadFromCard",true);
			formObject.setLocked("cmplx_Customer_CARDNOTAVAIL",true);
			formObject.setLocked("cmplx_Customer_NEP",true);
			formObject.setLocked("Customer_FetchDetails",true);
			formObject.setLocked("Customer_Button1",true);
			formObject.setLocked("cmplx_Customer_IsGenuine",true);
			formObject.setLocked("cmplx_Customer_NTB",true);
			formObject.setLocked("cmplx_Customer_CIFID_Available",true);
			formObject.setLocked("cmplx_Customer_VIPFlag",true);
			 //++ Below Code already exists for : "1-CSM-Customer details-" Age should be non editable for the user" : Reported By Shashank on Oct 05, 2017++
			if(activityName.equalsIgnoreCase("CSM")){
			formObject.setLocked("cmplx_Customer_Age",true);
			formObject.setLocked("cmplx_Customer_GCCNational",true);
			}
			 //++ Above Code already exists for : "1-CSM-Customer details-" Age should be non editable for the user" : Reported By Shashank on Oct 05, 2017++
			

		}
		else if(fragName.equalsIgnoreCase("Product")){

			formObject.setLocked("Add",true);
			if(activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("Add",false);
				CreditCard.mLogger.info("Query is inside product");
				formObject.setLocked("subProd", false);
				formObject.setLocked("AppType", false);
				formObject.setLocked("EmpType", false);
				CreditCard.mLogger.info("Query is after product");
			}
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("Modify",true);
			}
		}
		else if(fragName.equalsIgnoreCase("IncomeDetails")){

			formObject.setLocked("IncomeDetails_Button4",true);
			formObject.setLocked("IncomeDetails_FinacialSummarySelf",true);
		}
		else if(fragName.equalsIgnoreCase("AuthorisedSignDetails")){

			formObject.setLocked("CompanyDetails_Button3", true);
			formObject.setLocked("AuthorisedSignDetails_Button4", true);
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("AuthorisedSignDetails_add", true);
				formObject.setLocked("AuthorisedSignDetails_modify", true);
				formObject.setLocked("AuthorisedSignDetails_delete", true);
			}
		}
		else if(fragName.equalsIgnoreCase("PartnerDetails")){
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("PartnerDetails_add", true);
				formObject.setLocked("PartnerDetails_modify", true);
				formObject.setLocked("PartnerDetails_delete", true);
			}
			//12th september
			//++ Below Code already exists for : "14-CSM-Partner Details-" first make partner details fragment enable" : Reported By Shashank on Oct 05, 2017++
			else{
				formObject.setLocked("PartnerDetails_Frame1",false);
			}
			//++ Above Code already exists for : "14-CSM-Partner Details-" first make partner details fragment enable" : Reported By Shashank on Oct 05, 2017++
			
			//12th september
		}
		else if(fragName.equalsIgnoreCase("CompanyDetails")){
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("CompanyDetails_Add", true);
				formObject.setLocked("CompanyDetails_Modify", true);
				formObject.setLocked("CompanyDetails_delete", true);
			}
			//++ Below Code already exists for : "5-CSM-Company Details-" NEP should not be enabled" : Reported By Shashank on Oct 05, 2017++
			if(activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("NepType", true);
			}
			//++ Above Code already exists for : "5-CSM-Company Details-" NEP should not be enabled" : Reported By Shashank on Oct 05, 2017++
			formObject.setLocked("CompanyDetails_CheckBox4", true);
			formObject.setLocked("CompanyDetails_Button3", true);
		}
		else if(fragName.equalsIgnoreCase("Liability_New")){

			formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",true);
			formObject.setLocked("ExtLiability_AECBReport",true);
			formObject.setLocked("cmplx_Liability_New_overrideAECB",true);
			formObject.setLocked("cmplx_Liability_New_overrideIntLiab",true);
			formObject.setLocked("ExtLiability_Button1",true);
			formObject.setLocked("Liability_New_Overwrite",true);
			formObject.setLocked("ExtLiability_Button2",true);
			formObject.setLocked("ExtLiability_Button3",true);
			formObject.setLocked("ExtLiability_Button4",true);
			//12th sept
			formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",false);
			formObject.setLocked("ExtLiability_AECBReport",false);
			formObject.setLocked("cmplx_Liability_New_overrideAECB",false);
			formObject.setLocked("ExtLiability_Button2",false);
			formObject.setLocked("ExtLiability_Button3",false);
			formObject.setLocked("ExtLiability_Button4",false);
			//12th sept
					//P2-47 Override AECB and override internal liabilities tickbox not to be enabled for CSM (Cosmetics)
					 if(activityName.equalsIgnoreCase("CSM")){
						 CreditCard.mLogger.info("inside liability Details of CSM");
						 formObject.setEnabled("cmplx_Liability_New_overrideAECB",false);
						 CreditCard.mLogger.info("after liability Details of CSM");
							
				  }
						//P2-47 Override AECB and override internal liabilities tickbox not to be enabled for CSM (Cosmetics)
		}
		else if(fragName.equalsIgnoreCase("EMploymentDetails")){

			formObject.setLocked("EMploymentDetails_Button2",true);
		}
		else if(fragName.equalsIgnoreCase("ELigibiltyAndProductInfo")){
			if(!activityName.equalsIgnoreCase("CAD_Analyst1")){
				formObject.setLocked("ELigibiltyAndProductInfo_Button1",true);
			}
		}
		else if(fragName.equalsIgnoreCase("AddressDetails")){
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("AddressDetails_addr_Add",true);
				formObject.setLocked("AddressDetails_addr_Modify",true);
				formObject.setLocked("AddressDetails_addr_Delete",true);
			}
		}
		else if(fragName.equalsIgnoreCase("AltContactDetails")){

			formObject.setLocked("AlternateContactDetails_RetainaccifLoanreq",true);
		}
		else if(fragName.equalsIgnoreCase("FATCA")){
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("FATCA_Button1",true);
				formObject.setLocked("FATCA_Button2",true);
			}
		}
		else if(fragName.equalsIgnoreCase("OECD")){
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("OECD_add",true);
				formObject.setLocked("OECD_modify",true);
				formObject.setLocked("OECD_delete",true);
			}
		}
		else if(fragName.equalsIgnoreCase("IncomingDocument")){

			formObject.setLocked("IncomingDocument_Frame",false);
		}
		else if(fragName.equalsIgnoreCase("Reference_Details")){
			CreditCard.mLogger.info("before CSM");
			if(!activityName.equalsIgnoreCase("CSM")){
				CreditCard.mLogger.info("inside CSM");
				formObject.setLocked("Reference_Details_Reference_Add",true);
				formObject.setLocked("Reference_Details_Reference__modify",true);
				formObject.setLocked("Reference_Details_Reference_delete",true);
			}
			CreditCard.mLogger.info("after CSM");
			//formObject.setLocked("Reference_Details_Save",true);
		}
		else if(fragName.equalsIgnoreCase("SupplementCardDetails")){

			formObject.setLocked("SupplementCardDetails_Button1",true);
			formObject.setLocked("SupplementCardDetails_Button2",true);
			formObject.setLocked("SupplementCardDetails_Button3",true);
			formObject.setLocked("SupplementCardDetails_Button4",true);
		}
		else if(fragName.equalsIgnoreCase("CardDetails")){
			if(!activityName.equalsIgnoreCase("CSM")){
				formObject.setLocked("CardDetails_Button2",true);
				formObject.setLocked("CardDetails_Button3",true);
				formObject.setLocked("CardDetails_Button4",true);
			}
		}
		else if(fragName.equalsIgnoreCase("DecisionHistory")){

			formObject.setLocked("DecisionHistory_Button3",true);
			formObject.setLocked("DecisionHistory_Button4",true);
			formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
		}
	}
	
	public void hideAtCSMCompany(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//++ Below Code already exists for : "6-CSM-Company Details-" Marketing code, classification code and promotion code should not be visible at cSM" : Reported By Shashank on Oct 05, 2017++
		formObject.setVisible("MarketingCode", false);
		formObject.setVisible("ClassificationCode", false);
		formObject.setVisible("PromotionCode", false);
		//++ Above Code already exists for : "6-CSM-Company Details-" Marketing code, classification code and promotion code should not be visible at cSM" : Reported By Shashank on Oct 05, 2017++
		formObject.setVisible("HighDelinquencyEmployer", false);
		formObject.setVisible("EmployerCategoryPL", false);
		formObject.setVisible("EmployerStatusCC", false);
		formObject.setVisible("EmployerStatusPLExpat", false);
		formObject.setVisible("EmployerStatusPLNational", false);
	
		formObject.setVisible("CompanyDetails_Label31", true);
		formObject.setVisible("CompanyDetails_Label32", true);
		formObject.setVisible("CompanyDetails_Label17", true);
		formObject.setVisible("CompanyDetails_Label23", false);//marketing
		formObject.setVisible("CompanyDetails_Label30", false);//classification
		formObject.setVisible("CompanyDetails_Label31", false);//promotion
		formObject.setVisible("CompanyDetails_Combo4", false);
		formObject.setVisible("CompanyDetails_Label14", false);
		formObject.setVisible("CompanyDetails_Label16", false);
		formObject.setVisible("CompanyDetails_Label12", false);
		formObject.setVisible("CompanyDetails_Label15", false);
	
	

		
	}
	
	public void enable_CPV(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();


		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Instant Money")){

			formObject.setVisible("Customer_Details_Verification", true);
			formObject.setVisible("Office_Mob_Verification", true);
			formObject.setVisible("LoanCard_Details_Check", true);
			formObject.setVisible("Notepad_Details", false);
			formObject.setVisible("Business_Verification", false);
			formObject.setVisible("Smart_Check", false);
			formObject.setVisible("HomeCountry_Verification", false);
			formObject.setVisible("ResidenceVerification", false);
			formObject.setVisible("Reference_Detail_Verification", false);

			// set alignment
			formObject.setTop("Office_Mob_Verification", formObject.getTop("Customer_Details_Verification")+formObject.getHeight("Customer_Details_Verification")+5);
			formObject.setTop("LoanCard_Details_Check", formObject.getTop("Office_Mob_Verification")+formObject.getHeight("Office_Mob_Verification")+5);


		}

		else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business Titanium Card")){


			formObject.setVisible("Customer_Details_Verification", true);
			formObject.setVisible("Office_Mob_Verification", false);
			formObject.setVisible("LoanCard_Details_Check", true);
			formObject.setVisible("Notepad_Details", false);
			formObject.setVisible("Business_Verification", true);
			formObject.setVisible("Smart_Check", false);
			formObject.setVisible("HomeCountry_Verification", false);
			formObject.setVisible("ResidenceVerification", false);
			formObject.setVisible("Reference_Detail_Verification", false);

			//set alignment
			formObject.setTop("LoanCard_Details_Check", formObject.getTop("Business_Verification")+formObject.getHeight("Business_Verification")+5);
		}

		else  if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){

			formObject.setVisible("Customer_Details_Verification", true);
			formObject.setVisible("Office_Mob_Verification", true);
			formObject.setVisible("Reference_Detail_Verification", true);
			formObject.setVisible("ResidenceVerification", true);
			formObject.setVisible("LoanCard_Details_Check", true);

			formObject.setVisible("Notepad_Details", false);

			formObject.setVisible("HomeCountry_Verification", true);
			formObject.setVisible("Business_Verification", false);
			formObject.setVisible("Smart_Check", false);

			// set alignment
			formObject.setTop("HomeCountry_Verification", formObject.getTop("Customer_Details_Verification")+formObject.getHeight("Customer_Details_Verification")+5);
			formObject.setTop("ResidenceVerification", formObject.getTop("HomeCountry_Verification")+formObject.getHeight("HomeCountry_Verification")+5);
			formObject.setTop("Reference_Detail_Verification", formObject.getTop("ResidenceVerification")+formObject.getHeight("ResidenceVerification")+5);
			formObject.setTop("Office_Mob_Verification", formObject.getTop("Reference_Detail_Verification")+formObject.getHeight("Reference_Detail_Verification")+5);
			formObject.setTop("LoanCard_Details_Check", formObject.getTop("Office_Mob_Verification")+formObject.getHeight("Office_Mob_Verification")+5);
		}
	}
	//++ Below Code added By Yash on Oct 12, 2017  to fix : 34-"Office verification tab will not be visible as the case is for self employed customer " : Reported By Shashank on Oct 09, 2017++

	public void enable_cad1()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business Titanium Card")){
			formObject.setVisible("Office_Mob_Verification", false);
		}
		else if (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){
			formObject.setVisible("Office_Mob_Verification", false);
			
		}
		else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Instant Money")){
			formObject.setVisible("Business_Verification", false);

	}
		else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card")){
			formObject.setVisible("Business_Verification", false);

	}
	}
	//++ Above Code added By Yash on Oct 12, 2017  to fix : 34-"Office verification tab will not be visible as the case is for self employed customer " : Reported By Shashank on Oct 09, 2017++

	//++ below code added by abhishek as per CC FSD 2.7.3

		
	public void enable_custVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		// auto populaate fields from prev tabs
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
		
		formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
		formObject.setNGValue("cmplx_CustDetailVerification_Mob_No1_val", formObject.getNGValue("AlternateContactDetails_MobileNo1"));
		formObject.setNGValue("cmplx_CustDetailVerification_Mob_No2_val", formObject.getNGValue("AlternateContactDetails_MobNo2"));
		formObject.setNGValue("cmplx_CustDetailVerification_email1_val", formObject.getNGValue("AlternateContactDetails_Email1"));
		formObject.setNGValue("cmplx_CustDetailVerification_email2_val", formObject.getNGValue("AlternateContactDetails_Email2"));
		formObject.setNGValue("cmplx_CustDetailVerification_dob_val", formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("cmplx_CustDetailVerification_Resno_val", formObject.getNGValue("AlternateContactDetails_ResidenceNo"));
		formObject.setNGValue("cmplx_CustDetailVerification_Offtelno_val", formObject.getNGValue("AlternateContactDetails_OfficeNo"));
		formObject.setNGValue("cmplx_CustDetailVerification_hcountrytelno_val", formObject.getNGValue("AlternateContactDetails_HomeCOuntryNo"));
		// formObject.setNGValue("", formObject.getNGValue(""));

		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Instant Money") ||
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business Titanium Card")){

			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_dob_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_POBoxNo_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_emirates_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_persorcompPOBox_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Offtelno_val",false);

			// hide
			formObject.setVisible("CustDetailVerification_Label17", false);
			formObject.setVisible("cmplx_CustDetailVerification_Resno_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_resno_ver", false);
			formObject.setVisible("cmplx_CustDetailVerification_resno_upd", false);
			
			formObject.setVisible("CustDetailVerification_Label9", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_ver", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_upd", false);
			
			formObject.setVisible("CustDetailVerification_Label10", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);
		}
		
		else  if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){

			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_dob_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_POBoxNo_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_emirates_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_persorcompPOBox_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Offtelno_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountrytelno_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountryaddr_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Resno_val",false);

		}

	}
	//-- Above code added by abhishek as per CC FSD 2.7.3
	
	//++ below code added by abhishek as per CC FSD 2.7.3

	public void enable_loanCard(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Instant Money")){

			formObject.setEnabled("cmplx_LoanandCard_loanamt_val",false);
			formObject.setEnabled("cmplx_LoanandCard_tenor_val",false);
			formObject.setEnabled("cmplx_LoanandCard_emi_val",false);
			formObject.setEnabled("cmplx_LoanandCard_islorconv_val",false);
			formObject.setEnabled("cmplx_LoanandCard_firstrepaydate_val",false);
			// hide
			formObject.setVisible("LoanandCard_Label6", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_val", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_ver", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_upd", false);
			
			formObject.setVisible("LoanandCard_Label18", false);
			
			formObject.setVisible("LoanandCard_Label10", false);
			formObject.setVisible("LoanandCard_Label13", false);
			formObject.setVisible("LoanandCard_Label14", false);
			formObject.setVisible("LoanandCard_Label15", false);
			
			formObject.setVisible("LoanandCard_Label11", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_val", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_ver", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_upd", false);
			
			formObject.setVisible("LoanandCard_Label12", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_val", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_ver", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_upd", false);
		}
		else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business Titanium Card") ||
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){
			// disabled
			
			formObject.setEnabled("cmplx_LoanandCard_cardtype_val",false);
			formObject.setEnabled("cmplx_LoanandCard_cardlimit_val",false);
			formObject.setEnabled("cmplx_LoanandCard_islorconv_val",false);
			
			//hide
			
			formObject.setVisible("LoanandCard_Label17", false);
			
			formObject.setVisible("LoanandCard_Label1", false);
			formObject.setVisible("LoanandCard_Label7", false);
			formObject.setVisible("LoanandCard_Label8", false);
			formObject.setVisible("LoanandCard_Label9", false);
			
			formObject.setVisible("LoanandCard_Label2", false);
			formObject.setVisible("cmplx_LoanandCard_loanamt_val", false);
			formObject.setVisible("cmplx_LoanandCard_loanamt_ver", false);
			formObject.setVisible("cmplx_LoanandCard_loanamt_upd", false);
			
			formObject.setVisible("LoanandCard_Label3", false);
			formObject.setVisible("cmplx_LoanandCard_tenor_val", false);
			formObject.setVisible("cmplx_LoanandCard_tenor_ver", false);
			formObject.setVisible("cmplx_LoanandCard_tenor_upd", false);
			
			formObject.setVisible("LoanandCard_Label4", false);
			formObject.setVisible("cmplx_LoanandCard_emi_val", false);
			formObject.setVisible("cmplx_LoanandCard_emi_ver", false);
			formObject.setVisible("cmplx_LoanandCard_emi_upd", false);
			
			formObject.setVisible("LoanandCard_Label6", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_val", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_ver", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_upd", false);
		}

		
	}
	//-- Above code added by abhishek as per CC FSD 2.7.3
	public void enable_homeVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card")){
			formObject.setNGValue("cmplx_HCountryVerification_hcountrytel",formObject.getNGValue("AlternateContactDetails_HomeCOuntryNo1"));

			formObject.setEnabled("cmplx_HCountryVerification_Hcountrytelverified",true);
			formObject.setEnabled("cmplx_HCountryVerification_hcountrytel",false);
			formObject.setEnabled("cmplx_HCountryVerification_personcontctd",true);
			formObject.setEnabled("cmplx_HCountryVerification_Relwithpersoncntcted",true);
			formObject.setEnabled("cmplx_HCountryVerification_Remarks",true);
			formObject.setEnabled("cmplx_HCountryVerification_Decision",true);
		}
	}

	public void enable_ResVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card")){
			formObject.setNGValue("cmplx_ResiVerification_cntcttelno",formObject.getNGValue("AlternateContactDetails_ResidenceNo"));

			formObject.setEnabled("cmplx_ResiVerification_Telnoverified",true);
			formObject.setEnabled("cmplx_ResiVerification_cntcttelno",false);
			formObject.setEnabled("cmplx_ResiVerification_Remarks",true);
			formObject.setEnabled("cmplx_ResiVerification_Decision",true);

		}
	}

	public void enable_ReferenceVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card")){
			formObject.setNGValue("cmplx_RefDetVerification_ref1cntctno",formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,1));
			formObject.setNGValue("cmplx_RefDetVerification_ref1name",formObject.getNGValue("AlternateContactDetails_ResidenceNo",0,0));
			formObject.setNGValue("cmplx_RefDetVerification_ref1phone",formObject.getNGValue("AlternateContactDetails_ResidenceNo",0,4));



		}
	}
	//++ below code added by abhishek as per CC FSD 2.7.3

	public void enable_officeVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card")||
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM") || 
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Instant Money")){
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");

			formObject.setNGValue("cmplx_OffVerification_offtelno",formObject.getNGValue("AlternateContactDetails_OfficeNo"));
			formObject.setNGValue("cmplx_OffVerification_fxdsal_val",formObject.getNGValue("cmplx_IncomeDetails_netSal1"));
			formObject.setNGValue("cmplx_OffVerification_accprovd_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
			formObject.setNGValue("cmplx_OffVerification_desig_val",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			formObject.setNGValue("cmplx_OffVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			formObject.setNGValue("cmplx_OffVerification_cnfrminjob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));

			// disable fields

			formObject.setEnabled("cmplx_OffVerification_offtelno",false);
			formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
			formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
			formObject.setEnabled("cmplx_OffVerification_desig_val",false);
			formObject.setEnabled("cmplx_OffVerification_doj_val",false);
			formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);



		}
	}
	//-- Above code added by abhishek as per CC FSD 2.7.3
	//++ Below Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment " : Reported By Shashank on Oct 09, 2017++

	public void enable_busiVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")|| formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Salaried Credit Card") || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Business titanium Card"))||(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC"))){
	
			
	
				formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
	
				formObject.setNGValue("cmplx_BussVerification_TradeLicName",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,6));
				formObject.setNGValue("cmplx_BussVerification_signame",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0));
	
	
				// disable fields
	
				formObject.setEnabled("cmplx_BussVerification_TradeLicName",false);
				formObject.setEnabled("cmplx_BussVerification_signame",false);
	
	
	
			}
		}
	
	//++ Above Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment " : Reported By Shashank on Oct 09, 2017++

	
	/*public void populate_businessverification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Self Employed Credit Card")){
			formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");

			formObject.setNGValue("cmplx_BussVerification_TradeLicName",formObject.getNGValue("tlNo"));
			formObject.setNGValue("cmplx_BussVerification_signame",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0));


			// disable fields

			formObject.setEnabled("cmplx_BussVerification_TradeLicName",false);
			formObject.setEnabled("cmplx_BussVerification_signame",false);




		}
	}*/


	//added by abhishek as per CC FSD for notepad details load add modify delete button

	public void notepad_load(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		CreditCard.mLogger.info( "Activity name is:" + sActivityName);
		//formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName());
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate);
		//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);
		formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);
		formObject.setTop("NotepadDetails_SaveButton",200);
	}
	public void Notepad_add(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");

		CreditCard.mLogger.info(""+formObject.getUserId()+"-"+formObject.getUserName());
		//formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);

		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		// added by abhishek as per CC FSD
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);
		//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);

		// to make frame in Add state
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
	}

	public void Notepad_modify(){
		// added by abhishek as per CC FSD
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		CreditCard.mLogger.info( "Activity name is:" + sActivityName);
		//formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);
		//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);
		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);

		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
	}
	public void income(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String subprod =formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
		String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
		CreditCard.mLogger.info( "subprod"+subprod+",emptype:"+empType);
		if(subprod.equalsIgnoreCase("Business titanium Card")&& empType.equalsIgnoreCase("Self Employed")){
			CreditCard.mLogger.info( "inside if condition");
			formObject.setEnabled("IncomeDEtails", false);
		}
		
		//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
		
		//}
		//added 9/08/2017
		if(formObject.getNGValue("cmplx_IncomeDetails_Accomodation").equalsIgnoreCase("No")){
			formObject.setLocked("cmplx_IncomeDetails_CompanyAcc",true);
		}
		
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			CreditCard.mLogger.info( "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
				CreditCard.mLogger.info( "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
				formObject.setVisible("IncomeDetails_Label12", true);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);	
				formObject.setVisible("IncomeDetails_Label14", true);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", true);	
			}	
			else{
				formObject.setVisible("IncomeDetails_Label12", false);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);	
				formObject.setVisible("IncomeDetails_Label14", false);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", false);			
			}
		}
		
		if(n>0)
		{					
			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			CreditCard.mLogger.info( "Emp Type Value is:"+EmpType);

			if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 640);
				formObject.setHeight("IncomeDetails_Frame1", 615);	
				if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
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
				
			else if(EmpType.equalsIgnoreCase("Self Employed"))
			{							
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 490);
				formObject.setHeight("IncomeDetails_Frame1", 490);
				if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
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
				LoadPickList("BankStatFrom","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_othBankCAC where isActive='Y' order by code");
				//LoadPickList("BankStatFrom","select '--Select--' union select convert(varchar, Description) from NG_MASTER_bankname where isActive='Y'"); //Arun (12/10)
				//LoadPickList("ncomeDetails_bankStatfrom","select '--Select--' union select convert(varchar, Description) from NG_MASTER_bankname where isActive='Y'"); //Arun (12/10)
			}
		}
	}
	public void emp_details(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		loadPicklist4();
		if(!formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("SAL")){
			formObject.setVisible("EMploymentDetails_Label59", false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
		}
		else{
			formObject.setVisible("EMploymentDetails_Label59", true);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
		}
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
			formObject.setVisible("EMploymentDetails_Label71", false);
			formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
		}
		else{
			formObject.setVisible("EMploymentDetails_Label71", true);
			formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
		}
		new CC_CommonCode().lockALOCfields();
		
	}
	public void Notepad_delete(){
		// added by abhishek as per CC FSD
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		CreditCard.mLogger.info( "Activity name is:" + sActivityName);
		//formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);


		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);
		//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);

		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
	}

	public void Notepad_grid(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName(),false);
		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		formObject.setNGValue("NotepadDetails_Actdate",modifiedDate,false);

		formObject.setLocked("NotepadDetails_notedesc",true);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",true);
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);

		formObject.setEnabled("NotepadDetails_inscompletion",true);
		formObject.setLocked("NotepadDetails_ActuserRemarks",false);
	}
	// added by abhishek to align the frames as per CC FSD
	public void notepad_withoutTelLog(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setTop("NotepadDetails_savebutton", formObject.getHeight("NotepadDetails_Frame2")+10);
		formObject.setHeight("NotepadDetails_Frame1", formObject.getTop("NotepadDetails_savebutton")+40);
		formObject.setHeight("Notepad_Values", formObject.getHeight("NotepadDetails_Frame1")+5);
		formObject.setTop("DecisionHistory", formObject.getHeight("Notepad_Values")+20);

	}

	public void maker_Decision(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setTop("DecisionHistory_Decision_Label1", 10);
		formObject.setLeft("DecisionHistory_Decision_Label1", 25);
		formObject.setTop("cmplx_DEC_VerificationRequired", formObject.getTop("DecisionHistory_Decision_Label1")+formObject.getHeight("DecisionHistory_Decision_Label1"));
		formObject.setLeft("cmplx_DEC_VerificationRequired", 25);

		formObject.setTop("DecisionHistory_Decision_Label3", 10);
		formObject.setLeft("DecisionHistory_Decision_Label3", formObject.getLeft("DecisionHistory_Decision_Label1")+275);
		formObject.setTop("cmplx_DEC_Decision", formObject.getTop("DecisionHistory_Decision_Label3")+formObject.getHeight("DecisionHistory_Decision_Label3"));
		formObject.setLeft("cmplx_DEC_Decision", formObject.getLeft("DecisionHistory_Decision_Label1")+275);


		formObject.setTop("DecisionHistory_Label1", 10);
		formObject.setLeft("DecisionHistory_Label1", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
		formObject.setTop("cmplx_DEC_ReferReason", formObject.getTop("DecisionHistory_Label1")+formObject.getHeight("DecisionHistory_Label1"));
		formObject.setLeft("cmplx_DEC_ReferReason", formObject.getLeft("DecisionHistory_Decision_Label3")+275);


		formObject.setTop("DecisionHistory_Rejreason", 10);
		formObject.setLeft("DecisionHistory_Rejreason", formObject.getLeft("DecisionHistory_Label1")+275);
		formObject.setTop("cmplx_DEC_RejectReason", formObject.getTop("DecisionHistory_Rejreason")+formObject.getHeight("DecisionHistory_Rejreason"));
		formObject.setLeft("cmplx_DEC_RejectReason", formObject.getLeft("DecisionHistory_Label1")+275);

		formObject.setTop("DecisionHistory_Label5", formObject.getTop("DecisionHistory_Decision_Label1") + 60);
		formObject.setLeft("DecisionHistory_Label5", 25);
		formObject.setTop("cmplx_DEC_Description", formObject.getTop("DecisionHistory_Label5")+formObject.getHeight("DecisionHistory_Label5"));
		formObject.setLeft("cmplx_DEC_Description", 25);

		formObject.setTop("DecisionHistory_Label3", formObject.getTop("DecisionHistory_Label5"));
		formObject.setLeft("DecisionHistory_Label3", formObject.getLeft("DecisionHistory_Label5")+275);
		formObject.setTop("cmplx_DEC_Strength", formObject.getTop("DecisionHistory_Label3")+formObject.getHeight("DecisionHistory_Label3"));
		formObject.setLeft("cmplx_DEC_Strength", formObject.getLeft("DecisionHistory_Label3"));

		formObject.setTop("DecisionHistory_Label4", formObject.getTop("DecisionHistory_Label5"));
		formObject.setLeft("DecisionHistory_Label4",  formObject.getLeft("DecisionHistory_Label3")+275);
		formObject.setTop("cmplx_DEC_Weakness", formObject.getTop("DecisionHistory_Label4")+formObject.getHeight("DecisionHistory_Label4"));
		formObject.setLeft("cmplx_DEC_Weakness", formObject.getLeft("DecisionHistory_Label4"));

		formObject.setTop("DecisionHistory_Decision_Label4", formObject.getTop("DecisionHistory_Label5")) ;
		formObject.setLeft("DecisionHistory_Decision_Label4",  formObject.getLeft("DecisionHistory_Label4")+275);
		formObject.setTop("cmplx_DEC_Remarks", formObject.getTop("DecisionHistory_Decision_Label4")+formObject.getHeight("DecisionHistory_Decision_Label4"));
		formObject.setLeft("cmplx_DEC_Remarks",  formObject.getLeft("DecisionHistory_Decision_Label4"));



		formObject.setTop("DecisionHistory_Decision_ListView1", formObject.getTop("DecisionHistory_Label5")+100);
		formObject.setLeft("DecisionHistory_Decision_ListView1", 25);

		formObject.setVisible("DecisionHistory_save", true);
		formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+10);
		formObject.setLeft("DecisionHistory_save", 25);

		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save") +100);
		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1") +5);

	}

	public void cpv_Decision(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		formObject.setTop("DecisionHistory_Decision_Label3", 10);
		formObject.setLeft("DecisionHistory_Decision_Label3", 25);
		formObject.setTop("cmplx_DEC_Decision", formObject.getTop("DecisionHistory_Decision_Label3")+formObject.getHeight("DecisionHistory_Decision_Label3"));
		formObject.setLeft("cmplx_DEC_Decision", 25);

		formObject.setTop("DecisionHistory_Label11", 10);
		formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
		formObject.setTop("cmplx_DEC_Decision_Reasoncode",formObject.getTop("DecisionHistory_Label11")+formObject.getHeight("DecisionHistory_Label11"));
		formObject.setLeft("cmplx_DEC_Decision_Reasoncode",formObject.getLeft("DecisionHistory_Decision_Label3")+275);

		formObject.setTop("DecisionHistory_Label12",10);
		formObject.setLeft("DecisionHistory_Label12",formObject.getLeft("DecisionHistory_Label11")+275);
		formObject.setTop("cmplx_DEC_NoofAttempts", formObject.getTop("DecisionHistory_Label12")+formObject.getHeight("DecisionHistory_Label12"));
		formObject.setLeft("cmplx_DEC_NoofAttempts", formObject.getLeft("DecisionHistory_Label11")+275);

		formObject.setTop("DecisionHistory_Decision_Label4",10);
		formObject.setLeft("DecisionHistory_Decision_Label4",formObject.getLeft("DecisionHistory_Label12")+275);
		formObject.setTop("cmplx_DEC_RejectReason", formObject.getTop("DecisionHistory_Decision_Label4")+formObject.getHeight("DecisionHistory_Decision_Label4"));
		formObject.setLeft("cmplx_DEC_RejectReason",formObject.getLeft("DecisionHistory_Label12")+275);

		formObject.setTop("DecisionHistory_Decision_ListView1", formObject.getTop("DecisionHistory_Decision_Label4") +60 );
		formObject.setLeft("DecisionHistory_Decision_ListView1", 25);

		formObject.setVisible("DecisionHistory_save", true);
		formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+20);
		formObject.setLeft("DecisionHistory_save", 25);

		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save") +70);
		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1") +5);
	}
	public void loadPicklist_CompanyDet(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
		LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
		//LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
		if(subprod!=null && subprod.equalsIgnoreCase("IM")){
			LoadPickList("MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = 'IM' order by code");	
		}
		else{
			LoadPickList("MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = '!IM' order by code");
		}
		LoadPickList("EmployerStatusPLExpact","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("EmployerStatusPLNational","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("EmployerStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusCC where isActive='Y'  order by code");
		LoadPickList("PromotionCode","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y' order by code");
		LoadPickList("ClassificationCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' order by code");
		LoadPickList("TargetSegmentCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_TargetSegmentCode where isActive='Y' order by code");
		LoadPickList("EmployerCategoryPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL where isActive='Y'  order by code");
		LoadPickList("NepType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_NEPType with (nolock) order by code");
		LoadPickList("ApplicationCategory", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");
		LoadPickList("indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock) order by code");
		LoadPickList("indusMAcro", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMacro with (nolock) order by code");
		LoadPickList("indusMicro", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMicro with (nolock) order by code");
		LoadPickList("legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_LegalEntity with (nolock) order by code");
		LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("EOW", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_State with (nolock) order by code");
		LoadPickList("headOffice", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_State with (nolock) order by code");
		LoadPickList("CompanyDetails_Combo1", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_POAHolder with (nolock) order by code");
		LoadPickList("TradeLicencePlace", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_TradeLicensePlace with (nolock) order by code");

		//formObject.setNGValue("compIndus", "--Select--");
	}
	public static BigDecimal calcEMI(BigDecimal P, BigDecimal N, BigDecimal ROI) {
		BigDecimal emi = new BigDecimal(0) ;
		try{
			MathContext mc = MathContext.DECIMAL128;     
			BigDecimal R = ROI.divide(new BigDecimal(1200),mc);
			BigDecimal nemi1 = P.multiply(R,mc);
			BigDecimal npower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal dpower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal denominator = dpower1.subtract(BigDecimal.ONE);
			BigDecimal numerator = nemi1.multiply(npower1);
			emi = numerator.divide(denominator,0,RoundingMode.UP);

		}
		catch(Exception e){
			CreditCard.mLogger.info( "Exception occured in calcEMI() : ");
		}
		return emi;
	}
	public static String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);

			double seedvalue=Math.round(PMTEMI.doubleValue());
			double loamamount=B_loamamount.doubleValue();;
			int tenure=B_tenure.intValue();;
			double intrate=(B_intrate.intValue())/100.0;	
			CreditCard.mLogger.info("seedvalue  **************"+seedvalue);
			CreditCard.mLogger.info("loamamount  **************"+loamamount);
			CreditCard.mLogger.info("tenure=  **************"+tenure);
			CreditCard.mLogger.info("intrate  **************"+intrate);

			int iterations=2*(int)Math.round(PMTEMI.intValue()*.10);
			CreditCard.mLogger.info("PMTEMI   **"+PMTEMI+"  for intrate @"+intrate+ " iterations"+iterations);
			loanAmt_DaysDiff=seedvalue+"";
		}
		catch(Exception e){
			CreditCard.mLogger.info( "Exception occured in calcgoalseekEMI() : ");
			loanAmt_DaysDiff="0";
		}

		return loanAmt_DaysDiff;
	}
	public static double Cas_Limit(double aff_emi,double rate,double tenureMonths)
	{
		double pmt;
		try{
			double new_rate = (rate/100)/12;
			 pmt = (aff_emi)*(1-Math.pow(1+new_rate,-tenureMonths))/new_rate;
			CreditCard.mLogger.info("CC_Common"+"final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			pmt=0;
		}
		return pmt;
		
	}
	public static String getEMI(double loanAmount,double rate,double tenureMonths)
	{       
		String loanAmt_DaysDiff="";
		try{
												
			   
	
			BigDecimal B_intrate= BigDecimal.valueOf(rate);

            BigDecimal B_tenure= BigDecimal.valueOf(tenureMonths);

            BigDecimal B_loamamount= BigDecimal.valueOf(loanAmount);
            if(B_intrate.equals(BigDecimal.ZERO) ||B_tenure.equals(BigDecimal.ZERO) || B_loamamount.equals(BigDecimal.ZERO))
            {

             return "0";
             
            }


			
			
			
            loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);



		}
		catch(Exception e){
			CreditCard.mLogger.info( "Exception occured in getEMI() : ");
			loanAmt_DaysDiff = "0";
		}
		return loanAmt_DaysDiff;
	}	
	public String fetch_cust_details_supplementary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = GenerateXML("CUSTOMER_DETAILS","Supplementary_Card_Details");
			CreditCard.mLogger.info("Inside Customer");
			/*String Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
			SKLogger.writeLog("RLOS value of Gender",Gender);
			String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
			SKLogger.writeLog("RLOS value of Marital_Status",Marital_Status);*/
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info(ReturnCode);

			if(ReturnCode.equalsIgnoreCase("0000") ){
				valueSetCustomer(outputResponse,"Supplementary_Card_Details");
				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL033");

				String Date1=formObject.getNGValue("DOB");
				CreditCard.mLogger.info(Date1);
				SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
				SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
				String Datechanged=sdf2.format(sdf1.parse(Date1));
				CreditCard.mLogger.info(Datechanged);
				formObject.setNGValue("DOB",Datechanged,false);
			}
			else{
				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL030");
			}

		}catch(Exception ex){
			CreditCard.mLogger.info( "Exception occured in fetch_cust_details_primary method"+printException(ex));
			if(alert_msg.equalsIgnoreCase("")){
				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL029");
			}
		}
		return alert_msg;
	}
	
	//++ below code added by abhishek as per CC FSD 2.7.3
	public String getAPProcedureInputXml(String engineName, String param, String procName,String sessionId ) {
        return "<?xml version=\"1.0\"?>\n" + 
               "<APProcedure_Input>\n" + 
                   "<Option>APProcedure_WithDBO</Option>\n" + 
                   "<ProcName>"+ procName + "</ProcName>\n" + 
                   "<Params>" + param + "</Params>\n" + 
                   "<EngineName>" + engineName+ "</EngineName>\n" +
                   "<SessionId>"+sessionId+"</SessionId>" 
               + "<APProcedure_Input>";
    }
	//-- Above code added by abhishek as per CC FSD 2.7.3
	
	public String formatDateFromOnetoAnother(String date,String givenformat,String resultformat) {
	    String result = "";
	    SimpleDateFormat givenDateformat;
	    SimpleDateFormat resultDateformat;
	    try {
	    	givenDateformat = new SimpleDateFormat(givenformat);
	    	resultDateformat = new SimpleDateFormat(resultformat);
	        result = resultDateformat.format(givenDateformat.parse(date));
	        CreditCard.mLogger.info("Date converted: old Date: "+date+" \n new date: "+result);
	    }
	    catch(Exception e) {
	    	CreditCard.mLogger.info("Exception occured while converting Date: "+e.getMessage());
	    	CreditCard.mLogger.info("Date: "+date+"\n givenformat: "+givenformat+"\n resultformat: " +resultformat);
	        return date;
	    }
	    finally {
	    	givenDateformat=null;
	    	resultDateformat=null;
	    }
	    return result;
	}

}