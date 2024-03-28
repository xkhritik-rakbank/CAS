/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Credit Card
File Name                                                                     : CreditCard.js
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : All js custom functions 

------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------
Date					  Change By						Change Description 
19.07.2017                Disha                         changes done to make CPV fragment tab decisions mandatory
19.07.2017                Disha                         changes done to restrict user if any of the decision is pending in CPV tab fragments than in main decision tab user cannot take Decision as Approve

------------------------------------------------------------------------------------------------------------------------------------------------------
 */
 //below code added by tanshu - 06-10-2017 to have docindex of income doc fragment
 var row;
//above code added by tanshu - 06-10-2017 to have docindex of income doc fragment
var country_GCC= ["AE","SA","KW","QA","BH","OM"];
var CCFRAGMENTLOADOPT={};
var rlosValuesData = {};
var hiddenString = '';
var PLValuesData = {};
var hiddenStringPL = '';
var popup_win = null;
CCFRAGMENTLOADOPT['CustomerDetails'] = '';
CCFRAGMENTLOADOPT['ProductContainer'] = '';
CCFRAGMENTLOADOPT['GuarantorDet'] = '';
CCFRAGMENTLOADOPT['Self_Employed'] = '';
CCFRAGMENTLOADOPT['EmploymentDetails'] = '';
CCFRAGMENTLOADOPT['IncomeDetails'] = '';
CCFRAGMENTLOADOPT['Internal_External_Liability'] = '';
CCFRAGMENTLOADOPT['MiscFields'] = '';
CCFRAGMENTLOADOPT['EligibilityAndProductInformation'] = '';
CCFRAGMENTLOADOPT['LoanDetails'] = '';
CCFRAGMENTLOADOPT['Address_Details_container'] = '';
CCFRAGMENTLOADOPT['Alt_Contact_container'] = '';
CCFRAGMENTLOADOPT['FATCA'] = '';
CCFRAGMENTLOADOPT['KYC'] = '';
CCFRAGMENTLOADOPT['OECD'] = '';
CCFRAGMENTLOADOPT['Customer_Info_FPU'] = '';
CCFRAGMENTLOADOPT['RepaymentSchedule'] = '';
CCFRAGMENTLOADOPT['DecisionHistory'] = '';
CCFRAGMENTLOADOPT['Part_Match'] = '';
CCFRAGMENTLOADOPT['Card_Details'] = '';
CCFRAGMENTLOADOPT['Supplementary_Cont'] = '';
CCFRAGMENTLOADOPT['Partner_Details'] = '';
CCFRAGMENTLOADOPT['Finacle_Core'] = '';
CCFRAGMENTLOADOPT['Reject_Enquiry'] = '';
CCFRAGMENTLOADOPT['Finacle_CRM_Incidents'] = '';
CCFRAGMENTLOADOPT['Finacle_CRM_CustomerInformation'] = '';
CCFRAGMENTLOADOPT['World_Check'] = '';
CCFRAGMENTLOADOPT['MOL'] = '';
CCFRAGMENTLOADOPT['Details'] = '';
CCFRAGMENTLOADOPT['Salary_Enquiry'] = '';
CCFRAGMENTLOADOPT['Customer_Details_Verification'] = '';
CCFRAGMENTLOADOPT['Business_Verification'] = '';
CCFRAGMENTLOADOPT['HomeCountry_Verification'] = '';
CCFRAGMENTLOADOPT['ResidenceVerification'] = '';
CCFRAGMENTLOADOPT['Reference_Detail_Verification'] = '';
CCFRAGMENTLOADOPT['Office_Mob_Verification'] = '';
CCFRAGMENTLOADOPT['Guarantor_Verification'] = '';
CCFRAGMENTLOADOPT['LoanCard_Details_Check'] = '';
CCFRAGMENTLOADOPT['Notepad_Details'] = '';
CCFRAGMENTLOADOPT['Smart_Check'] = '';
CCFRAGMENTLOADOPT['Original_validation'] = '';
CCFRAGMENTLOADOPT['Compliance'] = '';
CCFRAGMENTLOADOPT['CompDetails'] = '';
CCFRAGMENTLOADOPT['Auth_Sign_Details'] = '';
CCFRAGMENTLOADOPT['FcuDecision'] = '';
CCFRAGMENTLOADOPT['FPU_GRID'] = '';
CCFRAGMENTLOADOPT['Reference_Details'] = '';
CCFRAGMENTLOADOPT['PostDisbursal'] = '';
CCFRAGMENTLOADOPT['Card_Collection_Decision'] = '';
CCFRAGMENTLOADOPT['CC_Creation'] = '';
CCFRAGMENTLOADOPT['Limit_Increase'] = '';
CCFRAGMENTLOADOPT['Loan_Disbursal'] = '';
CCFRAGMENTLOADOPT['CheckList'] = '';
CCFRAGMENTLOADOPT['Field_Visit_Initiated'] = '';
CCFRAGMENTLOADOPT['Exceptional_Case_Alert'] = '';

CCFRAGMENTLOADOPT['Customer_Details_Verification1'] = '';
CCFRAGMENTLOADOPT['Business_Verification1'] = '';
CCFRAGMENTLOADOPT['Employment_Verification'] = '';
CCFRAGMENTLOADOPT['Banking_Check'] = '';
CCFRAGMENTLOADOPT['Notepad_Details1'] = '';
CCFRAGMENTLOADOPT['supervisor_section'] = '';
CCFRAGMENTLOADOPT['Smart_Check1'] = '';
CCFRAGMENTLOADOPT['Frame4'] = '';
CCFRAGMENTLOADOPT['CAD'] = '';
CCFRAGMENTLOADOPT['CAD_Decision'] = '';
CCFRAGMENTLOADOPT['Dispatch_Details'] = '';
CCFRAGMENTLOADOPT['External_Blacklist'] = '';
CCFRAGMENTLOADOPT['CreditCard_Enq'] = '';
CCFRAGMENTLOADOPT['Case_hist'] = '';
CCFRAGMENTLOADOPT['LOS'] = '';
CCFRAGMENTLOADOPT['Notepad_Values'] = '';
CCFRAGMENTLOADOPT['ReferHistory'] = '';//added by akshay on 8/12/17
var flag_Company_FetchDetails=false;
var flag_Authorised_FetchDetails=false;
var visitSystemChecks_Flag=false;//added by akshay on 20/12/17
var NoOfAttemptsValue=0;//Aman for Sprint 2
var NoOfAttemptsValue_CA=0;
var NoOfAttemptsValue_Smart=0;
var flag_add_new=false;
var flag_firco=false;
var flag_supp_firco=false;
var Company_save_clicked=false;
var ext_liab_click=false;
var fpu_generated=false;
var update_customer=false;
function focus_CreditCard(pId){
	var activityName=window.parent.stractivityName;
	//alert("focus on control: "+pId);
	if(pId.indexOf('cmplx_')>-1 || pId.indexOf('AlternateContactDetails_')>-1){
		if(!PLValuesData[pId]){ // if data structure does not contains this ID.
			//if(getNGValue(pId)){ //if control value is not blank. For first time focus.
				PLValuesData[pId] = getNGValue(pId);
			//}
		}
		else{
			if(PLValuesData[pId] != getNGValue(pId)){
				PLValuesData[pId] = getNGValue(pId);
			}
		}
		}
	
	
	
	
	if(activityName == 'CSM' || activityName== 'DDVT_Maker'){
	//changes made by saurabh on 23rd Oct.
	if(pId.indexOf('cmplx_')>-1 || pId.indexOf('AlternateContactDetails_')>-1){
		if(!rlosValuesData[pId]){ // if data structure does not contains this ID.
			//if(getNGValue(pId)){ //if control value is not blank. For first time focus.
				rlosValuesData[pId] = getNGValue(pId);
			//}
		}
		else{
			if(rlosValuesData[pId] != getNGValue(pId)){
				rlosValuesData[pId] = getNGValue(pId);
			}
		}
		}
	}
	//console.log(JSON.stringify(rlosValuesData));
}

//added by gaurav 161122
var dialogToOpenType = null;
var popupWindow=null;
function setValue(val1) 
{
   //you can use the value here which has been returned by your child window
   popupWindow = val1;
   if(dialogToOpenType == 'DecisionHistory_ManualDevReason'){
	   if(popupWindow != 'undefined' && popupWindow!=null && popupWindow!="NO_CHANGE" && popupWindow!='[object Window]') {
			if(getNGValue('cmplx_DEC_Manual_Dev_Reason')!=popupWindow){
				setNGValueCustom('Eligibility_Trigger','Y');
			}
			if(popupWindow=='NO_CHANGE'){
				setNGValueCustom('cmplx_DEC_Manual_Dev_Reason',"");
			}
			else{
				setNGValueCustom('cmplx_DEC_Manual_Dev_Reason',popupWindow);
				setNGValueCustom('Eligibility_Trigger','Y');
			}
			//alert("the values are " +document.getElementById('H_CHECKLIST').value);
		}
   }		
}

function openCustomDialog (dialogToOpen,workstepName)
		{
			dialogToOpenType = dialogToOpen;
			if (workstepName!=null &&  workstepName!='')
			{
				//var popupWindow=null;
				var url='/webdesktop/custom/CustomJSP/Manual_Deviation_Reasons.jsp?ProcessName=CreditCard&sessionId='+window.parent.sessionId+'&deviationReason=' +deviationReason;
				sOptions = 'dialogWidth:850px; dialogHeight:500px; dialogLeft:250px; dialogTop:80px; center:yes;edge:raised; help:no; resizable:no; scroll:yes;scrollbar:yes; status:no; statusbar:no; toolbar:no; menubar:no; addressbar:no; titlebar:no;';
				//added below to handle window.open/window.showModalDialog according to type of browser starts here.
				/***********************************************************/
				var windowParams="height=600,width=650,toolbar=no,directories=no,status=no,center=yes,scrollbars=no,resizable=no,modal=yes,addressbar=no,menubar=no";
				if(dialogToOpen=='DecisionHistory_ManualDevReason')
				{
					var WINAME = '<%=WINAME%>';
					
					//window.showModalDialog("../RBL_Specific/history.jsp?WINAME="+WINAME,"", "dialogWidth:60; dialogHeight:400px; center:yes;edge:raised; help:no; resizable:no; scroll:yes;scrollbar:yes; status:no; statusbar:no; toolbar:no; menubar:no; addressbar:no; titlebar:no;");
					
					//added below to handle window.open/window.showModalDialog according to type of browser starts here.
					/***********************************************************/
					var windowParams="height=600,width=650,toolbar=no,directories=no,status=no,center=yes,scrollbars=no,resizable=no,modal=yes,addressbar=no,menubar=no";
					if (window.showModalDialog) {
						window.showModalDialog(url,null,sOptions);
					} else {
						window.open_(url,this, windowParams);
					}
					/************************************************************/
					//added below to handle window.open/window.showModalDialog according to type of browser  ends here.
					
					//Check if the call is for Ops_Maker and the call is for first time or not
					//if (workstepName=='Ops_Maker')
						//document.getElementById('flagForDecHisButton').value = 'Yes';
				}
				
								
			}
		}
//ends here.


function click_CreditCard(pId,frameState)
{

	if(getNGValue(pId)!=null && getNGValue(pId)!=undefined && document.getElementById(pId).innerHTML.indexOf('<table') == -1){
			setNGValueCustom(pId,getNGValue(pId));
	}
	var activityName=window.parent.stractivityName;
//done by sagarika for view button
//done by nikhil for part match view button
	if(pId=='SecNationality_Button_View'|| pId=='Nationality_Button_View'||pId=='DesignationAsPerVisa_button_View'|| pId=='Designation_button_View'|| pId=='Cust_ver_sp2_Designation_button3_View'
	|| pId=='AddressDetails_Button1_View' || pId=='Designation_button1'|| pId=='Designation_button2'|| pId=='Button_City_View'|| pId=='Button_State_View'|| pId=='ButtonOECD_State_View' || pId=='CardDispatchToButton_View'|| pId=='ButtonOECD_State_View' || pId=='Nationality_Button1_View'|| pId=='Designation_button7')
	{
		return true;
	}
if(pId=='Designation_button3_View' || pId=='Designation_button4_View'|| pId=='Designation_button1'|| pId=='Designation_button2' || pId=='Designation_button5_View' || pId=='Designation_button6_View' ||pId=='Designation_button8_View' || pId=='Cust_ver_sp2_Designation_button3_View' || pId=='Cust_ver_sp2_Designation_button5_View' || pId=='EmploymentVerification_s2_Designation_button4_View' || pId=='EmploymentVerification_s2_Designation_button6_View')
{
return true;
}
if((pId=='cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails') && (activityName=='CAD_Analyst1'))// Added by Rajan for PCASP-2232
{
	setEnabledCustom("POAHolders", true)
	setEnabledCustom("AuthorisedSignDetails_modify", true);
	setLockedCustom("POAHolders", false);
	setLockedCustom("AuthorisedSignDetails_modify", false);
}
	//sagarika for PCAS-2238
	if(pId=='NotepadDetails_Add1' || pId=='NotepadDetails_Clear')
	{
		var new_value = 'Notepad_Values';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
		
	//chnaged by nikhil for Wrong Code
		return true;
		
	}
		if(pId=='cmplx_EmploymentDetails_RegPayment' || pId=='cmplx_EmploymentDetails_MinimumWait')
	{
		setNGValueCustom('Eligibility_Trigger','Y');
		return true;
	}
	//sagarika on 17/06
	if(pId=='cmplx_fieldvisit_sp2_drop1')
	{//alert("value");
		if(getNGValue(pId)=="Yes")
		{//alert("yes");
		 com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_drop2",true);
		  com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_drop3",true);
		   com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_visit_date",true);
			com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_v_time",true);
			 com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
			 com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_visit_rec_time",true);

		}

		else if(getNGValue(pId)=="No")
		{//alert("no");
		 com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_drop2",false);
		  com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_drop3",false);
		   com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_visit_date",false);
			com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_v_time",false);
			 com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_rep_receivedDate",false);
			 com.newgen.omniforms.formviewer.setEnabled("cmplx_fieldvisit_sp2_field_visit_rec_time",false);
			 
			com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_visit_date",true);
			com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_v_time",true);
			 com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
			 com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_visit_rec_time",true);
			 
			
			 
		}
	}
	if(pId=='CC_Creation_Validate_Skyward')
	{
		return true;//sagarika
	}
	
	
	//added for multiple refer functionality on 26-11-18	
	if(pId=='DecisionHistory_ADD')
	{
		
		//++below code added by nikhil for PCAS-364 CR
		if(activityName=='CAD_Analyst1' && (getNGValue('Eligibility_Trigger')=='Y' || getNGValue('Eligibility_Trigger')==''))
		{
			showAlert('ELigibiltyAndProductInfo_Button1',alerts_String_Map['VAL388']);
			return false;
		}
				if(activityName=='CPV' && getNGValue('cmplx_DEC_Decision')=='CPV Hold')//PCAS-1868 sagarika
		{
			if(getNGValue('cmplx_DEC_SetReminder')=="")
			{
			showAlert('cmplx_DEC_SetReminder',alerts_String_Map['VAL394']);
			return false;
			}
		}
		if((activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2') && getNGValue('cmplx_DEC_Decision')=='CA_Hold')//PCAS-1868 sagarika
		{
			if(getNGValue('cmplx_DEC_SetReminder_CA')=="")
			{
			showAlert('cmplx_DEC_SetReminder_CA',alerts_String_Map['VAL394']);
			return false;
			}
		}
	
		//--above code added by nikhil for PCAS-364 CR
		//Deepak 10 Sept 2019 for PCAS-2404
		else if(activityName=='Smart_CPV'){
			if(getNGValue('cmplx_DEC_SetReminder_Smart')=="" && getNGValue('cmplx_DEC_Decision')=='Smart CPV Hold')
			{
			showAlert('cmplx_DEC_SetReminder_Smart',alerts_String_Map['VAL394']);
			return false;
			}
			if(getNGValue('CPV_REQUIRED')=='Y')
			{
			showAlert('DecisionHistory_ADD','Please check main WI!');
			}

		}
		if(activityName=='CAD_Analyst1')
		{
			if(getNGValue('cmplx_DEC_Manual_Deviation')==true)
				{
					if(getNGValue('cmplx_DEC_Manual_Dev_Reason')=='' )
						{
							showAlert('DecisionHistory_ManualDevReason',alerts_String_Map['CC146']);
							return false;
						}
				}
		}
		var n=getLVWRowCount('cmplx_ReferHistory_cmplx_GR_ReferHistory');
		 if(activityName=='DSA_CSO_Review' && getNGValue("cmplx_DEC_Decision")=="Customer Hold-Document requested" && getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',n-1,6)=='Complete')
		{
			showAlert('cmplx_DEC_Decision',"If refer history is complete then Customer Hold-Document requested is not allowed");
		}
		if(checkMandatory_Add())
		{
				if(Add_Validate())
				{
					setLockedCustom('cmplx_DEC_Decision',true);
					//change by saurabh for PCSP-360
					if(window.parent.stractivityName=='Original_Validation'){
						if(getNGValue('cmplx_DEC_Decision')=='Approve')
						{
							setNGValueCustom('ORIGINAL_VALIDATION','Yes');
						}
						else if(getNGValue('cmplx_DEC_Decision')=='Reject')
						{
							setNGValueCustom('ORIGINAL_VALIDATION','No');
						}
					}
					//below code added by nikhil for PCSP-486
					var userid = window.parent.userName;	
					//changed by nikhil for CAD1 only					
					if((getNGValue('cmplx_DEC_Strength').indexOf(userid)==-1 || getNGValue('cmplx_DEC_Strength')=='') && activityName=='CAD_Analyst1')
					{
						showAlert('cmplx_DEC_Strength',alerts_String_Map['CC148']);
						return false;
					}
					//changed by nikhil for CAD1 only
					if((getNGValue('cmplx_DEC_Weakness').indexOf(userid)==-1 || getNGValue('cmplx_DEC_Weakness')=='') && activityName=='CAD_Analyst1' && getNGValue('EmploymentType')!='Self Employed')
					{
						showAlert('cmplx_DEC_Weakness',alerts_String_Map['CC149']);
						return false;
					}
					
					flag_add_new=true;
					console.log(flag_add_new);
					return true;
				}					
		}
	}
	//Code aded by bandana for PCASP-6
		if(pId=='FinacleCore_CalculateAUM')
		{
			return true;
		}	
		if(pId=='FinacleCore_InvestmentModify')
		{
			return true;
		}
		if(pId=='cmplx_FinacleCore_cmplx_gr_investment')
		{
		setEnabledCustom('FinacleCore_InvestmentModify',true);
		}
	//Code for PACSP-6 ends
	if(pId=='DecisionHistory_Delete')
	{
		if(delete_dec_row()){
			//change by saurabh for PCSP-360
			setLockedCustom('cmplx_DEC_Decision',false);
			if(window.parent.stractivityName=='Original_Validation'){
					setNGValueCustom('ORIGINAL_VALIDATION','');
				}
			return true
		}
	}
	
	if(pId=='cmplx_CC_Loan_cmplx_btc')
	{
		var selected=com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CC_Loan_cmplx_btc');
		var transfer=getLVWAT("cmplx_CC_Loan_cmplx_btc",selected,0);
		var transferMode=getLVWAT("cmplx_CC_Loan_cmplx_btc",selected,2);
		if(transfer!="" && transferMode!=""){
			setLocked("transtype", true);
			setLocked("transferMode", true);
		  }
		 else{
		    setLocked("transtype", false);
			setLocked("transferMode", false);
		  }
		 if(transfer=="CCC"){
			 setVisible("CC_Loan_Label25",true);//PCASP-3186
			 setVisible("PaymentPurpose",true);
		 }
		 else{
			 setNGValue("PaymentPurpose",'');//PCASP-3186
			 setVisible("CC_Loan_Label25",false);
			 setVisible("PaymentPurpose",false);
		 }
		if(transfer=='BT' &&(transferMode=='S' || transferMode=='C'))
		{
			setLockedCustom("creditcardNo",false);
			setLockedCustom("bankName",false);
			setLockedCustom("nameOnCard",false);
			setLockedCustom("amount",false);
			setLockedCustom("marketingCode",false);
			setLockedCustom("approvalCode",false);
			setLockedCustom("AppStatus",false);
			setLockedCustom("date",false);
			setLockedCustom("tenor",true);
			setVisible("CC_Loan_Label24", false);
			setNGValue("Account_No_for_AT", "");
			setLockedCustom("Account_No_for_AT",true);
			setVisible("Account_No_for_AT", false);
			setVisible("CC_Loan_Label23", false);
			setNGValue("Account_No_for_Swift", "");
			setLockedCustom("Account_No_for_Swift",true);
			setVisible("Account_No_for_Swift", false);
			if(transferMode=='S'){
				setLockedCustom("chequeNo",true);
				setLockedCustom("chequeDate",true);
				setLockedCustom("chequeStatus",true);
				setNGValue('dispatchChannel','');
				setLockedCustom("dispatchChannel",true);
			}
			else if(transferMode=='C'){
				setLockedCustom("chequeNo",false);
				setLockedCustom("chequeDate",false);
				setLockedCustom("chequeStatus",false);
				setNGValue('dispatchChannel','998');
				setLockedCustom("dispatchChannel",true);
			}
		}
		else if((transfer=='CCC' && transferMode=='S')||(transfer=='SC' && transferMode=='S'))
		{
			setNGValue('Account_No_for_AT','');
			setLockedCustom("Account_No_for_AT",true);
			setVisible("CC_Loan_Label24", false);
			setVisible("Account_No_for_AT", false);
			setVisible("CC_Loan_Label23", true);
			setVisible("Account_No_for_Swift", true);
			setLockedCustom("Account_No_for_Swift",false);
			setLockedCustom("bankName",false);
			setLockedCustom("benificiaryName",false);
			setLockedCustom("nameOnCard",false);
			setLockedCustom("amount",false);
			setLockedCustom("iban",false);
			setLockedCustom("marketingCode",false);
			setLockedCustom("approvalCode",false);
			setLockedCustom("AppStatus",false);
			setLockedCustom("date",false);
			setLockedCustom("tenor",true);
			setLockedCustom("chequeNo",true);
			setLockedCustom("chequeDate",true);
			setLockedCustom("chequeStatus",true);
			setLockedCustom("dispatchChannel",true);
			setLockedCustom("creditcardNo",true);
		}
		else if((transfer=='CCC' && transferMode=='A')||(transfer=='SC' && transferMode=='A'))
		{
			setNGValue('Account_No_for_Swift','');
			setLockedCustom("Account_No_for_Swift",true);
			setVisible("CC_Loan_Label23", false);
			setVisible("Account_No_for_Swift", false);
			setVisible("CC_Loan_Label24", true);
			setVisible("Account_No_for_AT", true);
			setLockedCustom("Account_No_for_AT",false);
			setLockedCustom("amount",false);
			setLockedCustom("benificiaryName",false);
			setLockedCustom("nameOnCard",false);
			setLockedCustom("marketingCode",false);
			setLockedCustom("approvalCode",false);
			setLockedCustom("AppStatus",false);
			setLockedCustom("date",false);
			setLockedCustom("tenor",true);
			setLockedCustom("bankName",true);
			setLockedCustom("chequeNo",true);
			setLockedCustom("chequeDate",true);
			setLockedCustom("chequeStatus",true);
			setLockedCustom("dispatchChannel",true);
			setLockedCustom("creditcardNo",true);
		}
		else if((transfer=='CCC' && transferMode=='C')||(transfer=='SC' && transferMode=='C'))
		{
			setLockedCustom("benificiaryName",false);
			setLockedCustom("nameOnCard",false);
			setLockedCustom("amount",false);
			setLockedCustom("marketingCode",false);
			setLockedCustom("approvalCode",false);
			setLockedCustom("AppStatus",false);
			setLockedCustom("date",false);
			setNGValue('dispatchChannel','998');
			setLockedCustom("dispatchChannel",true);
			setLockedCustom("chequeNo",false);
			setLockedCustom("chequeDate",false);
			setLockedCustom("chequeStatus",false);
			setLockedCustom("tenor",true);
			setLockedCustom("creditcardNo",true);
			setLockedCustom("bankName",true);
		}
		return true;
	}
	//Added by shivang for PCASP-3177
	else if(pId=='cmplx_CC_Loan_cmplx_dds')
	{
		var selected=com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CC_Loan_cmplx_dds');
		var DDSMode=getLVWAT("cmplx_CC_Loan_cmplx_dds",selected,1);
		if(DDSMode=='F')
		{
			setLockedCustom("cmplx_CC_Loan_Percentage",true);
			setLockedCustom("cmplx_CC_Loan_DDSAmount",false);
			setNGValue("cmplx_CC_Loan_Percentage",'');
		}
		else if(DDSMode=='P')
		{
			setLockedCustom("cmplx_CC_Loan_Percentage",false);
			setLockedCustom("cmplx_CC_Loan_DDSAmount",true);
			setNGValue("cmplx_CC_Loan_DDSAmount",'');
		}
		else if(DDSMode=='M')
		{
			setLockedCustom("cmplx_CC_Loan_Percentage",true);
			setLockedCustom("cmplx_CC_Loan_DDSAmount",true);
			setNGValue("cmplx_CC_Loan_DDSAmount",'');
		}
	}
	//Added by shivang for PCASP-3177
	else if(pId=='cmplx_CC_Loan_cmplx_si')
	{
		var selected=com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CC_Loan_cmplx_si');
		var SIMode=getLVWAT("cmplx_CC_Loan_cmplx_si",selected,1);
		if(SIMode=='F')
		{
			setLockedCustom("cmplx_CC_Loan_SI_Percentage",true);
			setLockedCustom("cmplx_CC_Loan_FlatAMount",false);
			setNGValue("cmplx_CC_Loan_SI_Percentage",'');
		}
		else if(SIMode=='P')
		{
			setLockedCustom("cmplx_CC_Loan_SI_Percentage",false);
			setLockedCustom("cmplx_CC_Loan_FlatAMount",true);
			setNGValue("cmplx_CC_Loan_FlatAMount",'');
		}
		else if(SIMode=='M')
		{
			setLockedCustom("cmplx_CC_Loan_SI_Percentage",true);
			setLockedCustom("cmplx_CC_Loan_FlatAMount",true);
			setNGValue("cmplx_CC_Loan_FlatAMount",'');
		}
	}
	else if(pId=='GenerateAppForm'){
			var value = 'IncomingDocuments';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(value)==-1){
			var hiddenString = value+',';
			var previousValue = field_string_value;
			hiddenString = previousValue + hiddenString;
			setNGValue('FrameName',hiddenString);
			}
			var wi_name = window.parent.pid;
			//changes done by nikhil to correct doc name
			if(getNGValue('loan_type')=='Islamic')
			{			
				docName="Application_Form_Islamic";
			}
			else 
			{
				docName="Application_Form";
			}
			var attrbList = "";		
			attrbList += RLOSTemplateData(docName);
			attrbList=encodeURIComponent(attrbList);

			var ajaxReq; 
			var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
				
				
				var url = "/webdesktop/custom/CustomJSP/Generate_Template.jsp?attrbList="+attrbList+"&wi_name="+wi_name+"&docName="+docName+"&sessionId="+window.parent.sessionId;

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send(null);
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);
					var columnValue_arr=dataFromAjax.split('~');
					var statusDoc = columnValue_arr[0];
					var inputXML = columnValue_arr[1];
					var docIndex = columnValue_arr[2];
					statusDoc= statusDoc.trim();
					inputXML= inputXML.trim();
					docIndex= docIndex.trim();
					//alert("inputXML "+ inputXML);
					
					window.parent.customAddDoc(inputXML, docIndex, 'new');
					if(statusDoc == 'Success')
					{
						alert("Template generated successfully");
					}
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
				}
		}
if(pId=='DecisionHistory_Modify')
{
	if(com.newgen.omniforms.formviewer.getNGListIndex('DecisionHistory_Decision_ListView1')==-1)
	{
		showAlert('','Please select a row to modify');
		return false;
	}
	else if(modify_row())
	{
		if(activityName=='CAD_Analyst1')
		{
			if(getNGValue('cmplx_DEC_Manual_Deviation')==true)
				{
					if(getNGValue('cmplx_DEC_Manual_Dev_Reason')=='' )
						{
							showAlert('DecisionHistory_ManualDevReason',alerts_String_Map['CC146']);
							return false;
						}
				}
		}
		if(checkMandatory_Add())
		{
			setLockedCustom('cmplx_DEC_Decision',true);
			//change by saurabh for PCSP-360
			if(window.parent.stractivityName=='Original_Validation'){
						if(getNGValue('cmplx_DEC_Decision')=='Approve')
						{
							setNGValueCustom('ORIGINAL_VALIDATION','Yes');
						}
						else if(getNGValue('cmplx_DEC_Decision')=='Reject')
						{
							setNGValueCustom('ORIGINAL_VALIDATION','No');
						}
					}
				//	document.getElementById('DecisionHistory_Button4').click();//sagarika for CR CAM report
				flag_add_new=true;
			return true;
		}
	
		
	}
	else
	{
		return false;
	}

}
	//Deepak Added for PCAS-3464
	if(pId=='cmplx_DEC_Manual_Deviation'||pId=='DecisionHistory_ManualDevReason'){
	var new_value = 'DecisionHistory';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
				}
		var newAddedrow=0;
			for(var i=0;i<getLVWRowCount('DecisionHistory_Decision_ListView1');i++)
			{
				if(getLVWAT('DecisionHistory_Decision_ListView1',i,12)=='')
				{
					newAddedrow++;
				}		
			}
			if(newAddedrow>0)
			{
				showAlert(pId,alerts_String_Map['CAS025']);
				if(pId=='cmplx_DEC_Manual_Deviation' ){
					if(getNGValue('cmplx_DEC_Manual_Deviation')==true){
						setNGValueCustom('cmplx_DEC_Manual_Deviation','false');
					}
					else{
						setNGValueCustom('cmplx_DEC_Manual_Deviation','true');
					}
				}
				return false;
			}
	}
if(pId=='DecisionHistory_Decision_ListView1')
{
	setLockedCustom('cmplx_DEC_Decision',false);
	//Added by Pooja For 2 decision
	setLockedCustom('DecisionHistory_ADD',true);
	var selectedRow_Decision=com.newgen.omniforms.formviewer.getNGListIndex('DecisionHistory_Decision_ListView1');
	if(selectedRow_Decision!=-1)
	{
		var decision=getLVWAT('DecisionHistory_Decision_ListView1', selectedRow_Decision,3);
		var Remarks=getLVWAT('DecisionHistory_Decision_ListView1', selectedRow_Decision,4);
		var Decision_Reason=getLVWAT('DecisionHistory_Decision_ListView1', selectedRow_Decision,8);
		// Changes for Deepak PCAS-3430
		setNGValueCustom("cmplx_DEC_Remarks",Remarks);
		setNGValueCustom("DecisionHistory_dec_reason_code",Decision_Reason);
		if(getLVWRowCount('DecisionHistory_Decision_ListView1')-1==getNGListIndex('DecisionHistory_Decision_ListView1')){
			setNGValueCustom("cmplx_DEC_Decision",decision);
		}else{
			setLockedCustom('cmplx_DEC_Decision',true);
		}
		
			
	}
}
//ended for multiple refer 26/11/18	

		if(activityName == 'DDVT_Maker'){
			if(pId=='ProductContainer' || pId=='IncomeDetails' || pId=='EligibilityAndProductInformation' || pId=='Address_Details_container' || pId=='Alt_Contact_container' ||  pId=='Card_Details' ||  pId=='Supplementary_Cont' ||  pId=='FATCA' ||  pId=='KYC' ||  pId=='OECD' || pId=='Details' || pId=='Finacle_CRM_Incidents' || pId=='Finacle_CRM_CustomerInformation'  || pId=='MOL' || pId=='World_Check' || pId=='Reject_Enquiry' || pId=='Salary_Enquiry' || pId=='Frame4' || pId=='DecisionHistory' || pId == 'PartMatch_Search' || pId == 'Reference_Details'||pId=='External_Blacklist' || pId=='PostDisbursal'){
				//var flag ='true';
				//setNGFrameState('Part_Match',0);
				var patmatch_flag = getNGValue('cmplx_PartMatch_partmatch_flag');
					if(pId == 'PartMatch_Search'){
						if(checkMandatory(CC_PARTMATCH))
						{	
							//below added by nikhil FOR PCSP-468
							/*if(getNGValue('PartMatch_visafno')=='' && getNGValue('PartMatch_nationality')!='AE' )	
								{
									showAlert('PartMatch_visafno',alerts_String_Map['VAL154']);
									return false;
								}
								*/
								//Changed by Rajan to avoid visa validation for GCC Countries for PCASP-1383
								if(getNGValue('PartMatch_visafno')=='')	
								{
								var flag=0;
								for(var i=0; i<country_GCC.length; i++)
								{
									if(country_GCC[i]==getNGValue('PartMatch_nationality'))
									{
										flag=1;
										break;
									}
								}
								if(flag==0)
								{showAlert('PartMatch_visafno',alerts_String_Map['VAL154']);
							
									return false;
								}
								}
							var partCIF = getNGValue('PartMatch_CIFID');
							if(partCIF !="" && partCIF.length!=7)
							{
								showAlert('PartMatch_CIFID',alerts_String_Map['VAL105']);
								return false;
							}
							
							setNGValueCustom('cmplx_PartMatch_partmatch_flag','false');
							//setLockedCustom('PartMatch_Search',true);
							return true;
						}
						else 
						{
						return false;//done by sagarika for PCSP 468
						}
					}
					else if((patmatch_flag == '' || patmatch_flag == null) && isLocked('Customer_save')==false){
					//change by saurabh on 11/4/19.
								showAlert('Part_Match',alerts_String_Map['CC072']);
								com.newgen.omniforms.formviewer.setNGFocus('PartMatch_Search');
								//setNGFrameState('ProductContainer',1);
								return false;
					 }
					else if((patmatch_flag=='false') && (isLocked('Customer_save')==false))
					{
								showAlert('PartMatch_Save',"Please click Blacklist Check Button");
								com.newgen.omniforms.formviewer.setNGFocus('PartMatch_Blacklist');
								//setNGFrameState('ProductContainer',1);
								return false;
					 }
					  else if((patmatch_flag=='true') && (isLocked('Customer_save')==false) && getNGValue('EmploymentType')=='Self Employed' && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,3)!='')
					{
								showAlert('PartMatch_Company_blacklist',"Please Perform Company Blacklist Check");
								com.newgen.omniforms.formviewer.setNGFocus('PartMatch_Company_blacklist');
								//setNGFrameState('ProductContainer',1);
								return false;
					 }

					 if(pId=='DecisionHistory' )
					 {
					 if(!checkMandatory_Frames(getNGValue('Mandatory_Frames')))
						return false;
						}
					 /*if(pId=='DecisionHistory')
					 {
						if(isVisible('IncomeDetails_Frame1')==false)
						{
							showAlert('IncomeDetails','Please Visit Income Details First.');
							
							return false;
						}
						if(isVisible('ExtLiability_Frame1')==false)
						{
							showAlert('Internal_External_Liability','Please Visit Liability Section First.');
							
							return false;
						}
						if(isVisible('ELigibiltyAndProductInfo_Frame1')==false)
						{
							showAlert('EligibilityAndProductInformation','Please Visit Eligibility section First.');
							
							return false;
						}
					 }*/
					 
					 return true;
					
			}
		}
		
	
			 if(pId=='CustomerDetails' || pId=='ProductContainer'|| pId=='GuarantorDet'|| pId=='Self_Employed'|| pId=='EmploymentDetails' || pId=='IncomeDetails'|| pId=='Internal_External_Liability' || pId=='MiscFields' || pId=='EligibilityAndProductInformation' || pId=='LoanDetails'|| pId=='Address_Details_container'|| pId=='Alt_Contact_container'|| pId=='FATCA'|| pId=='KYC' || pId=='OECD' || pId=='RepaymentSchedule' ||  pId=='DecisionHistory'|| pId=='Part_Match' || pId=='Card_Details' || pId=='Supplementary_Cont' || pId=='Partner_Details'  || pId=='Reject_Enquiry'|| pId=='Finacle_CRM_Incidents' || pId== 'Finacle_CRM_CustomerInformation' || pId== 'World_Check'  || pId =='MOL' || pId=='Details' || pId=='Salary_Enquiry' || pId=='Customer_Details_Verification'  || pId=='Business_Verification' || pId=='HomeCountry_Verification' || pId=='ResidenceVerification' || pId=='Reference_Detail_Verification' || pId=='Office_Mob_Verification' || pId=='LoanCard_Details_Check' || pId=='Notepad_Details' || pId=='Smart_Check' || pId=='Original_validation' || pId=='Compliance' || pId=='CompDetails' || pId=='Auth_Sign_Details' || pId=='FcuDecision' || pId == 'Reference_Details' || pId=='Guarantor_Verification' || pId=='Card_Collection_Decision' || pId=='CC_Creation' || pId=='Limit_Increase' || pId=='Loan_Disbursal' || pId=='Customer_Details_Verification1' || pId=='Business_Verification1' || pId=='Employment_Verification' || pId=='Banking_Check'|| pId=='Exceptional_Case_Alert'||pId=='Field_Visit_Initiated'|| pId=='CheckList' || pId=='Notepad_Details1' || pId=='supervisor_section' || pId=='Smart_Check1' || pId=='Frame4' || pId=='CAD' || pId=='CAD_Decision' || pId=='Dispatch_Details' || pId=='LOS' || pId=='Case_hist' || pId=='CreditCard_Enq' || pId=='External_Blacklist' || pId=='Notepad_Values' || pId=='Customer_Info_FPU' || pId=='FPU_GRID'){
				if(activityName=='CSM' && pId=='Alt_Contact_container')
				{
				setVisible("AltContactDetails_Label7", false);
				setVisible("AlternateContactDetails_OfficeExt", false);
				}
				if(pId=='DecisionHistory')
					 { 
				 /*if(activityName=='FCU')//sagarika for PCAS-3015
						 {
							if(isVisible('Cust_ver_sp2_Frame1')==false)
						{
							showAlert('Cust_ver_sp2_Frame1','Please visit Customer Detail Verification');
							setNGFrameState('DecisionHistory',1);
							return false; 
						 }
						 if(isVisible('EmploymentVerification_s2_Frame1')==false)
						{
							showAlert('EmploymentVerification_s2_Frame1','Please visit Employment Verification');
							setNGFrameState('DecisionHistory',1);
							return false;
						 }
						 }*/
						
						/*if(isVisible('IncomeDetails_Frame1')==false)
						{
							showAlert('IncomeDetails','Please Visit Income Details First.');
							setNGFrameState('DecisionHistory',1);
							return false;
						}
						if(isVisible('ExtLiability_Frame1')==false)
						{
							showAlert('Internal_External_Liability','Please Visit Liability Section First.');
							setNGFrameState('DecisionHistory',1);
							return false;
						}
						if(isVisible('ELigibiltyAndProductInfo_Frame1')==false)
						{
							showAlert('EligibilityAndProductInformation','Please Visit Eligibility section First.');
							setNGFrameState('DecisionHistory',1);
							return false;
						}*/
						if(!checkMandatory_Frames(getNGValue('Mandatory_Frames')))
						return false;
						
					 }
				if(CCFRAGMENTLOADOPT){
							var key = pId;
							var value = CCFRAGMENTLOADOPT[key];
								if(value=='' || value=='Y'){
									doNotLoadFragmentSecTime(pId);			
										return true;
								}		
							else 
								return false;
						
					} 
				}
	//added by akshay on 8/12/17	
	//Below Code added by prabhakar for crn generation and kalayan
	else if(pId=='CardDetails_Button1')
	{
		if(!(getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid")>0))
		{
		showAlert("PartMatch_Button1","Please add in Finacle CRM Customer Info");	
		return false;
		}
		var Grid='cmplx_CardDetails_cmplx_CardCRNDetails';
		var Selected = getLVWAT(Grid,getNGListIndex(Grid),9);
		
		for (var i=0;i<getLVWRowCount(Grid);i++)
		{
			if(getLVWAT(Grid,i,1)=='' && getLVWAT(Grid,i,9)=='Primary' && Selected=='Supplement')
			{
				showAlert('CardDetails_Button1','Please Generate CRN for all Primary Card First');
				return false;
			}
		}
			setLockedCustom("CardDetails_Button1",true);
			return true;
	}
	else if(pId=='ReferHistory_save')
		{
				removeFrame(pId);
				return true;}
	else if(pId=='CC_Creation_Caps'){
		return true;
	}
	else if(pId=='FinacleCRMCustInfo_Add'){
		//Deepak Code added for PCAS-3564
		for(var i=0;i<getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');i++)
					{
						if(getNGValue('FinacleCRMCustInfo_Text1')==getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0))
						{
						showAlert(pId,alerts_String_Map['CC262']);
						return false;
						}
					}
		return true;
	}
	else if(pId=='Button1'){
		
	return true;	
	}
	else if(pId=='Button2'){
		
	return true;	
	}
	else if(pId=='Button3'){
		
	return true;	
	}
	else if (pId=='DecisionHistory_EFMS_Status')
	{
	return true;
	}
	else if(pId=='Button11'){
		
	return true;	
	}
	else if(pId=='Button12'){
		
	return true;	
	}
	else if(pId=='cmplx_FinacleCore_DDSgrid')
	{
		return true;
	}
	else if(pId=='CustDetailVerification1_Button1')
	{
		return true;
	}
	else if(pId=='Button13'){
		
	return true;	
	}
	else if(pId=='FinacleCore_Lien_modify'){
		
	return true;	
	}
	else if(pId=='Fpu_Grid_Button1'){
		
	return true;	
	}
	
	//added by nikhil to set default expiry date 30/1
	else if(pId=='cmplx_IncomingDocNew_IncomingDocGrid')
	{
		return true;
	}
	else if(pId=='FinacleCRMCustInfo_Modify' ){
		return true;
	}
	else if(pId=='FinacleCRMCustInfo_Delete' || pId=='CardCollection_Button1' || pId=='CardCollection_Button4'){
		return true;
	}
	
	//below code by saurabh for incoming doc new start
			//added here for documents
		else if(pId=='IncomingDocNew_AddtoDMSbtn'){
			if(getNGValue('IncomingDocNew_DocName')!=''){
			
				addDocFromOD();
			}
			else{
			showAlert('IncomingDocNew_AddtoDMSbtn','Kindly choose Document Type and Name');
			return false;
			}
			
		}
		else if(pId=='IncomingDocNew_Save'){
			return true;
		}
		//code added to see the documents name
		else if(pId=='IncomingDocNew_AddFromPCbtn'){
			/*var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		//	alert("row clicked:"+row);
		setNGValueCustom('RowNum',row);
			var doc_clicked_id ="IncomingDoc_Frame_Reprow"+row+"_Repcolumn0";
			//alert("value: "+ document.getElementById(doc_clicked_id).value);*/
			if(getNGValue('IncomingDocNew_DocName')!=''){
				
				var docname1=getNGValue('IncomingDocNew_DocName'); //document.getElementById(doc_clicked_id).value;
		//	alert("value of docname "+ docname1);
			window.parent.DocName(docname1);
			addDocFromPC();
			
			}
			else{
			showAlert('IncomingDocNew_AddFromPCbtn','Kindly choose Document Type and Name');
			return false;
			}
			 //to have docIndex
			 //commented by deepak as this is no longer in use
			//window.parent.rowClicked(row);//added By Tanshu on 10/10/17-----to set Docindex corresponding to each doc added
		 //to have docIndex
			return true;
		}
		
		//below code by saurabh for incoming doc new
		else if(pId=='IncomingDocNew_Addbtn'){
			if(!checkMandatoryIncomingDoc()){
				return false;
			}
			if(checkDuplicateRow()){
				showAlert('IncomingDocNew_Addbtn','Document already added');
				return false;
			}
			return true;
		}
		//changes by saurabh on 9th Jan
		//Code aded by bandana for PCASP-6
		if(pId=='FinacleCore_CalculateAUM')
		{
			return true;
		}	
		if(pId=='FinacleCore_InvestmentModify')
		{
			return true;
		}
		if(pId=='cmplx_FinacleCore_cmplx_gr_investment')
		{
		setEnabledCustom('FinacleCore_InvestmentModify',true);
		}
		//Code for PACSP-6 ends
		else if(pId=='IncomingDocNew_Modifybtn'){
			if(!checkMandatoryIncomingDoc()){
				return false;
			}
			if(isLocked("IncomingDocNew_DocType")) {
				setLockedCustom("IncomingDocNew_DocType", false);
			}
			if(isLocked("IncomingDocNew_DocName")) {
				setLockedCustom("IncomingDocNew_DocName", false);
			}
			return true;
		}
		else if(pId=='cmplx_IncomingDocNew_IncomingDocGrid'){
			var rowSel = getNGListIndex(pId);
			if(rowSel>-1){
				setLockedCustom('IncomingDocNew_DocType',true);
				setLockedCustom('IncomingDocNew_DocName',true);
			}
			else{
				setLockedCustom('IncomingDocNew_DocType',false);
				setLockedCustom('IncomingDocNew_DocName',false);
			}
		}
		//Changes done in below code by Deepak 12 feb for add from scan start
		else if(pId=='IncomingDocNew_Scanbtn'){
			var docDiv = window.parent.document.getElementById("docDiv");	
        	if(docDiv != null){
				if(getNGValue('IncomingDocNew_DocName')!=''){
					//changes by saurabh for scan button doc list. 5th Feb 19.
					var docname1=getNGValue('IncomingDocNew_DocName'); //document.getElementById(doc_clicked_id).value;
					window.parent.DocName(docname1);
					openScannerWindow();
				}
				else{
					showAlert('IncomingDocNew_AddtoDMSbtn','Kindly choose Document Type and Name');
					return false;
				}
				//ScanDocClick();//added By Akshay on 2/8/17 for scanning docs issue
			}
			else{	
				showAlert('IncomingDocNew_AddtoDMSbtn','Kindly open document view interface to use this functionality');
			}
		}
		//Changes done in below code by Deepak 12 feb for add from scan end
		 else if (pId == 'IncomingDocNew_Viewbtn')
		{
			//commented by deepak as this is no longer in use
			//var obj = getInterfaceData("DLIST", "");
			//var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
			//var row_print = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn11";
			if(getNGListIndex("cmplx_IncomingDocNew_IncomingDocGrid")>-1){
			var docIndex = getNGValue('IncomingDocNew_Docindex');
			parent.reloadapplet(docIndex, '');
			}
			else{
				showAlert('IncomingDocNew_Viewbtn', 'Kindly select a document from the grid');
			}
		}
		
		 else if (pId == 'IncomingDocNew_Printbtn')
    {
        //var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		//setNGValueCustom('RowNum',row);
        //var row_print = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn11";
		if(getNGListIndex("cmplx_IncomingDocNew_IncomingDocGrid")>-1){
        var docIndex = getNGValue('IncomingDocNew_Docindex');//document.getElementById(row_print).value;
        //var doc_clicked_id = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn0";
        var docName = getNGValue('IncomingDocNew_DocName');//document.getElementById(doc_clicked_id).value;
        printImageDocument(docIndex);//added By Tanshu on 10/10/17-----to print png fies(for now...coordinating with product)
		}
		else{
			showAlert('IncomingDocNew_Printbtn', 'Kindly select a document from the grid');
		}
    }

		//modified to download the document 
		else if(pId=='IncomingDocNew_Downloadbtn'){
		//commented by deepak as this is no longer in use
		//var obj = getInterfaceData("DLIST", "");
        //var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        //var row_print = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn11";
		if(getNGListIndex("cmplx_IncomingDocNew_IncomingDocGrid")>-1){
        var docIndex = getNGValue('IncomingDocNew_Docindex');//document.getElementById(row_print).value;
        parent.reloadapplet(docIndex, '');
		customDownloadDocument();
		}
		else{
			showAlert('IncomingDocNew_Downloadbtn', 'Kindly select a document from the grid');
		}
		}
		//modified to download the document 
			//ended here
		//below code by saurabh for incoming doc new end
	
	else if(pId=='CardDetails_Button5')
	{
	
			setLockedCustom("CardDetails_Button5",true);
			return true;
		
	}
	else if(pId=='cmplx_CardDetails_cmplx_CardCRNDetails')
	{
var activityName = window.parent.stractivityName;
				
				if(activityName=='DDVT_Maker')			
			{
		var row=getNGListIndex('cmplx_CardDetails_cmplx_CardCRNDetails');
		if(getLVWAT(pId,row,2).length==0)
		{
			setLockedCustom("CardDetails_Button1",false);
		}
		else
		{
			setLockedCustom("CardDetails_Button1",true);
		}
		console.log(getLVWAT(pId,row,8));
		if((getLVWAT(pId,row,0).indexOf("KALYAN")>-1) && getLVWAT(pId,row,2).length>0 && getLVWAT(pId,row,8).length==0)//|| getLVWAT(pId,row,2).length<0
		{
			setLockedCustom("CardDetails_Button5",false);
		}
		else
		{
			setLockedCustom("CardDetails_Button5",true);
			
		}
		if(row>-1){
		return true;//required for loading picklist of fee profile etc. specific to card product
	}
	}
	
	}
	//Above Code added by prabhakar for crn generation and kalyan		
	else if (pId == 'ReferHistory' && frameState == 0)
    {
        if (getNGValue("cmplx_DEC_Decision") == null)
        {
            showAlert('', alerts_String_Map['PL171']);
            setNGFrameState('ReferHistory', 1);
        }
        else if (CCFRAGMENTLOADOPT['ReferHistory'] == '')
        {
            CCFRAGMENTLOADOPT['ReferHistory'] = 'N';
            return true;
        }

    }
	else if(pId=='ExtLiability_Save')
		{
			ext_liab_click=true;
			if(checkMandatory(CC_LIABILITY))
				{
					
				removeFrame(pId);
				return true;}
	}
	//change by saurabh on 31st Dec
	else if(pId=='Limit_Inc_Button1'){
	
		return true;
	}
	else if(pId=='Limit_Inc_UpdCust'){
		return true;
	}
	//Added by aman 08062018
	else if(pId=='Nationality_Button' || pId=='SecNationality_Button'|| pId=='BirthCountry_Button'|| pId=='ResidentCountry_Button'|| pId=='AddressDetails_Button1'|| pId=='Nationality_ButtonPartMatch'|| pId=='MOL_Nationality_Button'|| pId=='CardDetails_bankName_Button'|| pId=='EmploymentDetails_Bank_Button')
	{
		return true;
	}
	//added by nikhil to save card dispatch 20/01/2018
	else if(pId=='CardDispatchToButton')
	{
		var new_value = 'AltContactDetails_Frame1';
		var field_string_value = getNGValue('FrameName');
		if(field_string_value.indexOf(new_value)==-1)
		{
			var newString = field_string_value + new_value+',';
			setNGValueCustom('FrameName',newString);
		}
		return true;
	}
	//Added by aman 08062018
	//below code added by nikhil for PCSP-85
	else if (pId=='Designation_button'|| pId=='DesignationAsPerVisa_button'|| pId=='FreeZone_Button')
	{
		
		var value = 'EmploymentDetails';
		var field_string_value = getNGValue('FrameName');
		if(field_string_value.indexOf(value)==-1){
			var hiddenString = value+',';
			var previousValue = field_string_value;
			hiddenString = previousValue + hiddenString;
			setNGValueCustom('FrameName',hiddenString);
			}
			//++below code added by nikhil for PCAS-364 CR
				setNGValueCustom('Eligibility_Trigger','Y');
				//--above code added by nikhil for PCAS-364 CR
		return true;
	}
	//Added by shivang for PCASP-2707
	else if(pId =='Cust_ver_sp2_Designation_button1' || pId == 'EmploymentVerification_s2_Designation_button2'){
		return true;
		
	}
	 else if (pId == 'ReferHistory_Modify')
	{
	var n=getLVWRowCount("DecisionHistory_Decision_ListView1");
		if((activityName=='FCU' || activityName=='FPU') && getNGValue('ReferHistory_status')=='Complete'){
			setLocked('Generate_FPU_Report',false);
		}
		if(activityName=='RM_Review')
		{
			 if(getLVWAT("DecisionHistory_Decision_ListView1",n-1,2)!="RM_Review")
		{
			showAlert("","Please take the decision first");
			return false;
		}
			 if(getLVWAT("DecisionHistory_Decision_ListView1",n-1,2)=="RM_Review" && getLVWAT("DecisionHistory_Decision_ListView1",n-1,3)=="Reject")
		{
			showAlert("","Refer History cannot be chosen");
			return false;
		}

		}

		return true;
	} 
	//code by saurabh on 18th dec
	else if(pId == 'cmplx_CCCreation_cmplx_CCCreationGrid'){
		var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CCCreation_cmplx_CCCreationGrid');
		if(selectedRow!=-1){
			var updCustFlag = getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,10);
			var cardCreationFlag = getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,8);
			var capsFlag = getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,9);
			var Cust_type= getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,12);
			var PassNo= getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,13);
			var card=getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,5);
			var skywardFlag=getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid', selectedRow, 17);
			var interestFeeProfile= getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,11).toUpperCase();
			if(skywardFlag!='Y' && interestFeeProfile.indexOf('SKYWARDS')>-1){
				setEnabledCustom('CC_Creation_Validate_Skyward', true);//added by saurabh1
			}
			else{
				setEnabledCustom('CC_Creation_Validate_Skyward', false);
			}
			if("Supplement"==Cust_type && PassNo==getNGValue('cmplx_Customer_PAssportNo'))
			{
				updCustFlag='Y';
			}
			if(updCustFlag!='Y'){
				setEnabledCustom('CC_Creation_Update_Customer',true);
				setEnabledCustom('CC_Creation_Button2',false);
				setEnabledCustom('CC_Creation_Caps',false);
			}
			if(updCustFlag=='Y' && cardCreationFlag!='Y'){
			setEnabledCustom('CC_Creation_Update_Customer',true);
				setEnabledCustom('CC_Creation_Button2',true);
				setEnabledCustom('CC_Creation_Caps',false);
			}
			if(updCustFlag=='Y' && cardCreationFlag=='Y' && capsFlag!='Y'){
				setEnabledCustom('CC_Creation_Update_Customer',true);
				setEnabledCustom('CC_Creation_Button2',false);
				setEnabledCustom('CC_Creation_Caps',true);
			}
			if(cardCreationFlag=='Y'){
				setNGValueCustom('CC_Creation_CardCreated','Y');
				setEnabledCustom('CC_Creation_Button2',false);
			}
			if(capsFlag=='Y'){
				setNGValueCustom('CC_Creation_TransferToCAPS','Y');
				setEnabledCustom('CC_Creation_Caps',false);
			}
			if("Supplement"==Cust_type && PassNo==getNGValue('cmplx_Customer_PAssportNo'))
			{
				setEnabledCustom('CC_Creation_Update_Customer',false);
			}
			
		}
		
		else{
			setEnabledCustom('CC_Creation_Update_Customer',false);
			setEnabledCustom('CC_Creation_Button2',false);
			setEnabledCustom('CC_Creation_Caps',false);
		}
	}
	
	//sagarika falcon bau-to enable skyward number on ddvt
	else if(pId == 'SupplementCardDetails_cmplx_supplementGrid'){
			var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('SupplementCardDetails_cmplx_supplementGrid');
			if(selectedRow==-1)
			{
				return true;
			}
			var cardProduct = getLVWAT('SupplementCardDetails_cmplx_supplementGrid',selectedRow,30);
			var Passport= getLVWAT('SupplementCardDetails_cmplx_supplementGrid',selectedRow,3);
			if(cardProduct.indexOf('EKTMC')>-1 || cardProduct.indexOf('EKWEC')>-1 || cardProduct.indexOf('EKWEI')>-1){
				setVisible("SupplementCardDetails_Label38",true);
				setVisible("SupplementCardDetails_Text1",true);
				setEnabled("SupplementCardDetails_Text1",true);
				if(activityName == 'DDVT_Maker'){
						setLockedCustom("SupplementCardDetails_Text1",false);
				}
				else{
					setLockedCustom("SupplementCardDetails_Text1",true);
				}
			}	
			setNGValue('SupplementCardDetails_Old_passport',Passport);
			
	}	
	//ended by akshay on 8/12/17	
			
			
	//var flag1 = 'false';
		else if(pId=='Customer_save'){
				//alert("inside customer save");
			
			if(Customer_Save_Check1())
				{
				removeFrame(pId);
				return true;}
				else
				return false;
		}
		else if(pId=='FinacleCRMCustInfo_Button2'){
			return true;
		}
		//smartcheck buttons code added by saurabh on 3rd Oct for smart check grid buttons.
		else if(pId=='SmartCheck_Add'){
		var activityName = window.parent.stractivityName;
			if(checkMandatory_Smartcheck()){
			//Done by aman for production issue
			if(getNGValue('SmartCheck_CR_Remarks')!='' && activityName=='CAD_Analyst1')
			{
				var comments=getNGValue('SmartCheck_CR_Remarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck_CR_Remarks',datetime+'-'+userid+'-'+comments);
			}
			if(getNGValue('SmartCheck_CPVRemarks')!='' && activityName!='CAD_Analyst1')
			{
				var comments=getNGValue('SmartCheck_CPVRemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck_CPVRemarks',datetime+'-'+userid+'-'+comments);
			}
			//Done by aman for production issue
				return true;
			}
		}
		//smartcheck buttons code added by saurabh on 3rd Oct for smart check grid buttons.
		else if(pId=='SmartCheck_Modify'){
		var activityName = window.parent.stractivityName;
			if(checkMandatory_Smartcheck()){
			//Added by aman for PCSP-380
			//Done by aman for production issue
			if(getNGValue('SmartCheck_CR_Remarks')!='' && activityName=='CAD_Analyst1')
			{
				var comments=getNGValue('SmartCheck_CR_Remarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck_CR_Remarks',datetime+'-'+userid+'-'+comments);
			}
			if(getNGValue('SmartCheck_CPVRemarks')!=''&& activityName!='CAD_Analyst1')
			{
				var comments=getNGValue('SmartCheck_CPVRemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck_CPVRemarks',datetime+'-'+userid+'-'+comments);
			}
			//Done by aman for production issue
			//Added by aman for PCSP-380
				return true;
			}
		}
		//smartcheck buttons code added by saurabh on 3rd Oct for smart check grid buttons.
		else if(pId=='SmartCheck_Delete'){
				return true;
		}
		// commented by abhishek as per CC FSD
		/* 
		if(pId == 'Reference_Details_save'){
			return true;
		} */
		//++below code added by nikhil 26/10/17 as per CC FSD 2.2
		else if(pId=='BankingCheck_save'){
				{
				removeFrame(pId);
				return true;}
		}
		//--above code added by nikhil 26/10/17 as per CC FSD 2.2
		else if(pId=='SupplementCardDetails_FetchDetails')
		{
			if(getNGValue('SupplementCardDetails_FirstName')==''){
			showAlert('SupplementCardDetails_FirstName',alerts_String_Map['VAL061']);
				return false;
			}
			
			else if(getNGValue('SupplementCardDetails_lastname')==''){
			showAlert('SupplementCardDetails_lastname',alerts_String_Map['VAL076']);
				return false;
			}
			else if(getNGValue('SupplementCardDetails_passportNo')==''){
			showAlert('SupplementCardDetails_passportNo',alerts_String_Map['VAL098']);
				return false;
			}
			else if(getNGValue('SupplementCardDetails_DOB')==''){
			showAlert('SupplementCardDetails_DOB',alerts_String_Map['VAL049']);
				return false;
			}
			else if(getNGValue('SupplementCardDetails_Nationality')=='' || getNGValue('SupplementCardDetails_Nationality')=='--Select--'){
			showAlert('SupplementCardDetails_Nationality',alerts_String_Map['VAL121']);
				return false;
			}
			else if(getNGValue('SupplementCardDetails_MobNo')==''){
			showAlert('SupplementCardDetails_MobNo',alerts_String_Map['VAL084']);
				return false;
			}
			else if(getNGValue('SupplementCardDetails_Text6')=='' && getNGValue('SupplementCardDetails_ResidentCountry')=='AE'){
			showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL053']);
				return false;
			}
			return true;
		}
		//Above code added by shweta
		else if(pId=='DDS_Add'){
			if(DDS_Save_CC()){
				return true;
			}
			
		}
		else if(pId=='DDS_Modify'){
			if(DDS_Save_CC()){				
				return true;
			}	
		}
		else if(pId=='DDS_Delete'){
			return true;
		}
		else if(pId=='SI_Add'){
			if(SI_Save_CC()){				
				return true;
			}
			
		}
		else if(pId=='SI_Modify'){
			if(SI_Save_CC()){
				return true;
			}	
		}
		else if(pId=='SI_Delete'){
			return true;
		}
		else if(pId=='RVC_Add'){
			if(RVC_Save_Check()){
					return true;
			}
			
		}
		else if(pId=='RVC_Modify'){
			if(RVC_Save_Check()){
				return true;
			}	
		}
		else if(pId=='RVC_Delete'){
			return true;
		}
		//Above code added by shweta
		// added by akshay on 15/9/17 to add bt details in grid as per  FSD
		else if(pId=='BT_Add'){
			if(checkMandatory_BTGrid()){
				if(validate_BTGrid())
					return true;
			}
			
		}
		else if(pId=='BT_Modify'){
			if(checkMandatory_BTGrid()){
				//Changed by Shivang for PCASP-1445
				if(validate_BTGrid_Modify())
					return true;
			}	
		}
		
		else if(pId=='BT_Delete'){
			return true;
		}
		else if (pId=='Customer_FircoStatus'||pId=='DecisionHistory_FircoStatus' )//sagarika for firco refresh
		{
			return true;
		}
		else if(pId=='cmplx_CC_Loan_cmplx_btc'){
			return true;
		}
		else if(pId=='cmplx_EmploymentDetails_Freezone'){
			if(getNGValue('cmplx_EmploymentDetails_Freezone')==true)
			{
				setLockedCustom("cmplx_EmploymentDetails_FreezoneName",false);
			}
			else
			{
				setLockedCustom("cmplx_EmploymentDetails_FreezoneName",true);
			}
		}
		//added by saurabh for point 39 in UAt observations.
		else if (pId =='cmplx_Liability_New_overrideIntLiab'){ 
    
        var liabChk = getNGValue('cmplx_Liability_New_overrideIntLiab');
        if(liabChk == true)
            {
                setVisible("Liability_New_Overwrite", true);
                
                
            }
            else{
                setVisible("Liability_New_Overwrite", false);
            }
                return true;            
        }
//++ Below Code already exists for : "22-CSM-Liability addition-" Takeover amount should be enabled only when takeover indicator is selected" : Reported By Shashank on Oct 05, 2017++
		else if(pId=='ExtLiability_Takeoverindicator'){
			var TakeOver=getNGValue("ExtLiability_Takeoverindicator");
			var activityName = window.parent.stractivityName;
			if(activityName=='CSM'){
				if(TakeOver==true){
				setLockedCustom("ExtLiability_takeoverAMount",false);
				setLockedCustom("ExtLiability_CACIndicator",true);
				}
				else{
					setLockedCustom("ExtLiability_takeoverAMount",true);
					setLockedCustom("ExtLiability_takeoverAMount",true);
					setLockedCustom("ExtLiability_CACIndicator",false);
				}
			}
		}
		//++ Below Code already exists for : "22-CSM-Liability addition-" Takeover amount should be enabled only when takeover indicator is selected" : Reported By Shashank on Oct 05, 2017++
			
		//++ Below Code already exists for : "21-CSM-Liability addition-" QC Amount and QC EMI should be enabled only if CAC indicator is checked" : Reported By Shashank on Oct 05, 2017++
		else if(pId=='ExtLiability_CACIndicator'){
			var activityName = window.parent.stractivityName;
			var CACInd=getNGValue("ExtLiability_CACIndicator");
			if(activityName=='CSM'){
				if(CACInd==true){
					setLockedCustom("ExtLiability_QCAmt", false);
					setLockedCustom("ExtLiability_QCEMI", false);
					setLockedCustom("ExtLiability_Takeoverindicator", true);
				}
				else{
					setLockedCustom("ExtLiability_QCAmt", true);
					setLockedCustom("ExtLiability_QCEMI", true);
					setLockedCustom("ExtLiability_Takeoverindicator", false);
					}
				}
			}
			//below code addeed by saurabh
			else if(pId=='DecisionHistory_AddStrength'){
            if(getNGValue('DecisionHistory_NewStrength')!=null && getNGValue('DecisionHistory_NewStrength')!='' && getNGValue('DecisionHistory_NewStrength')!= ' '){
                var strengths = getNGValue('cmplx_DEC_Strength');
                var newStrength = getNGValue('DecisionHistory_NewStrength');
                var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
                setNGValueCustom('cmplx_DEC_Strength',strengths+'\n'+(userid+'-'+datetime+'-'+newStrength));
                setNGValueCustom('DecisionHistory_NewStrength','');
            }
        }
        else if(pId=='DecisionHistory_AddWeakness'){
            if(getNGValue('DecisionHistory_NewWeakness')!=null && getNGValue('DecisionHistory_NewWeakness')!='' && getNGValue('DecisionHistory_NewWeakness')!= ' '){
                var strengths = getNGValue('cmplx_DEC_Weakness');
                var newStrength = getNGValue('DecisionHistory_NewWeakness');
                var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
                setNGValueCustom('cmplx_DEC_Weakness',strengths+'\n'+(userid+'-'+datetime+'-'+newStrength));
                setNGValueCustom('DecisionHistory_NewWeakness','');
            }
        }

			
		//++ Above Code already exists for : "21-CSM-Liability addition-" QC Amount and QC EMI should be enabled only if CAC indicator is checked" : Reported By Shashank on Oct 05, 2017++
		else if(pId=='ExtLiability_Frame4' && frameState==0)
		{
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('NEW')>-1)
			{
				setEnabledCustom('ExtLiability_Takeoverindicator',false);
			}
			else{
				setEnabledCustom('ExtLiability_Takeoverindicator',true);
			}
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1)
			{
				setEnabledCustom('ExtLiability_Takeoverindicator',true);
				setNGValueCustom('ExtLiability_takeoverAMount','');
			}
			else{
				setEnabledCustom('ExtLiability_Takeoverindicator',false);
			}
			setLockedCustom("ExtLiability_QCAmt", true);
			setLockedCustom("ExtLiability_QCEMI", true);
			setLockedCustom("ExtLiability_takeoverAMount", true);
		}
		// ++ below code already exist 06-10-2017 ALOC fields are also editable
	//P1-30,ALOC fields are also editable
		else if(pId=='cmplx_EmploymentDetails_Others'){
		var activityName = window.parent.stractivityName;
		if(activityName=='CSM'){
			var OthersCSM=getNGValue("cmplx_EmploymentDetails_Others");
			if(OthersCSM==true){
				setLockedCustom("cmplx_EmploymentDetails_EmpName", false);
				setLockedCustom("cmplx_EmploymentDetails_EMpCode", false);
				setLockedCustom("cmplx_EmploymentDetails_FreezoneName", false);
				setLockedCustom("cmplx_EmploymentDetails_Freezone", false);
				setLockedCustom("cmplx_EmploymentDetails_EmpStatusPL", false);
				setLockedCustom("cmplx_EmploymentDetails_EmpStatusCC", false);
				setLockedCustom("cmplx_EmploymentDetails_Emp_Categ_PL", false);
				setLockedCustom("cmplx_EmploymentDetails_EmpIndusSector", false);
				setLockedCustom("cmplx_EmploymentDetails_Indus_Macro", false);
				setLockedCustom("cmplx_EmploymentDetails_Indus_Micro", false);
				setLockedCustom("cmplx_EmploymentDetails_IncInCC", false);
				setLockedCustom("cmplx_EmploymentDetails_IncInPL", false);
				setLockedCustom("cmplx_EmploymentDetails_Status_PLNational", false);
				setLockedCustom("cmplx_EmploymentDetails_Status_PLExpact", false);
				setLockedCustom("cmplx_EmploymentDetails_ownername", false);
				setLockedCustom("cmplx_EmploymentDetails_NOB", false);
				setLockedCustom("cmplx_EmploymentDetails_LOS", false);
				setLockedCustom("EMploymentDetails_Combo34", false);
				setLockedCustom("cmplx_EmploymentDetails_authsigname", false);
				setLockedCustom("cmplx_EmploymentDetails_highdelinq", false);
				setLockedCustom("cmplx_EmploymentDetails_dateinPL", false);
				setLockedCustom("cmplx_EmploymentDetails_dateinCC", false);
				setLockedCustom("cmplx_EmploymentDetails_remarks", false);
				setLockedCustom("cmplx_EmploymentDetails_Aloc_RemarksPL", false);
			}
			else{
				setLockedCustom("cmplx_EmploymentDetails_EmpName", true);
				setLockedCustom("cmplx_EmploymentDetails_EMpCode", true);
				setLockedCustom("cmplx_EmploymentDetails_FreezoneName", true);
				setLockedCustom("cmplx_EmploymentDetails_Freezone", true);
				setLockedCustom("cmplx_EmploymentDetails_EmpStatusPL", true);
				setLockedCustom("cmplx_EmploymentDetails_EmpStatusCC", true);
				setLockedCustom("cmplx_EmploymentDetails_Emp_Categ_PL", true);
				setLockedCustom("cmplx_EmploymentDetails_EmpIndusSector", true);
				setLockedCustom("cmplx_EmploymentDetails_Indus_Macro", true);
				setLockedCustom("cmplx_EmploymentDetails_Indus_Micro", true);
				setLockedCustom("cmplx_EmploymentDetails_IncInCC", true);
				setLockedCustom("cmplx_EmploymentDetails_IncInPL", true);
				setLockedCustom("cmplx_EmploymentDetails_Status_PLNational", true);
				setLockedCustom("cmplx_EmploymentDetails_Status_PLExpact", true);
				setLockedCustom("cmplx_EmploymentDetails_ownername", true);
				setLockedCustom("cmplx_EmploymentDetails_NOB", true);
				setLockedCustom("cmplx_EmploymentDetails_LOS", true);
				setLockedCustom("EMploymentDetails_Combo34", true);
				setLockedCustom("cmplx_EmploymentDetails_authsigname", true);
				setLockedCustom("cmplx_EmploymentDetails_highdelinq", true);
				setLockedCustom("cmplx_EmploymentDetails_dateinPL", true);
				setLockedCustom("cmplx_EmploymentDetails_dateinCC", true);
				setLockedCustom("cmplx_EmploymentDetails_remarks", true);
				setLockedCustom("cmplx_EmploymentDetails_Aloc_RemarksPL", true);
			}
			if(setALOCListed())
				return true;
		}
	}
		
		//P1-30,ALOC fields are also editable
// ++ above code already exist 06-10-2017 ALOC fields are also editable
		else if(pId=='IncomeDetails_SelfEmployed_Save')
		 {
			if( getNGValue('cmplx_Customer_VIPFlag')==false && Income_Save_Check())
				{
				removeFrame(pId);
				return true;
				}
			else if(getNGValue('cmplx_Customer_VIPFlag')==true){	
				return true;
			}
		}
		
		else if(pId=='cmplx_Customer_CIFID_Available'){
			setLockedCustom('cmplx_Customer_CIFNo',false);
		}
		/*else if(pId=='FATCA_Save'){
			if(FATCA_Save_Check())
				return true;
		}*/
		else if(pId=='OECD_add')
		{	
			if(checkMandatory_OecdGrid('Add'))
			return true;
		}
		else if(pId=='OECD_modify')
		{
			if(checkMandatory_OecdGrid('Modify'))
			return true;
		}
		else if(pId=='OECD_delete')
		{
			return true;
		}
		//below id changed by nikhil 29/12/17
		// Changed by aman for CR 1112
		else if(pId=='SupplementCardDetails_Add')
		{
			if(checkMandatory_SupplementGrid()){
				if(validateSuppEntry('add')){
				var age=calcAge(getNGValue('SupplementCardDetails_DOB'),'');
				// Changed by Rajan for PCASP-1427
				if(getNGValue("SupplementCardDetails_Relationship")=='MOT' ||getNGValue("SupplementCardDetails_Relationship")=='FAT'||getNGValue("SupplementCardDetails_Relationship")=='SON'|| getNGValue("SupplementCardDetails_Relationship")=='BRO' || getNGValue("SupplementCardDetails_Relationship")=='DAU' ||getNGValue("SupplementCardDetails_Relationship")=='HUS'||getNGValue("SupplementCardDetails_Relationship")=='WIF' ||getNGValue("SupplementCardDetails_Relationship")=='SIS' ||getNGValue("SupplementCardDetails_Relationship")=='PAR')
				{
					if(age<15){
								showAlert(pId,'Minor Customer not Applicable for Credit Card');
								//setNGValueCustom(pId,'');
								return false;
							  }
				}
				else if(getNGValue("SupplementCardDetails_Relationship")!='MOT' ||getNGValue("SupplementCardDetails_Relationship")!='FAT'||getNGValue("SupplementCardDetails_Relationship")!='SON'||getNGValue("SupplementCardDetails_Relationship")!='BRO' || getNGValue("SupplementCardDetails_Relationship")!='DAU'|| getNGValue("SupplementCardDetails_Relationship")!='HUS'|| getNGValue("SupplementCardDetails_Relationship")!='WIF'  ||getNGValue("SupplementCardDetails_Relationship")!='SIS' || getNGValue("SupplementCardDetails_Relationship")!='PAR')
				{  // changed by shivang for PCASP-3203
					if(age<21 && getNGValue('SupplementCardDetails_Non_Resident')==false){
						showAlert(pId,'Minor Customer not Applicable for Credit Card');
								//setNGValueCustom(pId,'');
						return false;
					}
					else if(getNGValue('SupplementCardDetails_Non_Resident')==true){
						showAlert(pId,'Only immediate family members are allowed for non resident supplementary card for ' + prod);
						return false;
					}
				}
					var cardProduct=getNGValue("SupplementCardDetails_CardProduct");
					if(cardProduct.indexOf('EKTMC')>-1 || cardProduct.indexOf('EKWEC')>-1 || cardProduct.indexOf('EKWEI')>-1){
						showAlert('',"Kindly Delete the supplementary card request.");
						return false;
					}
					if (getLVWRowCount('SupplementCardDetails_cmplx_supplementGrid')>30 ){
						showAlert('SupplementCardDetails_cmplx_supplementGrid',alerts_String_Map['VAL376']);
					}
					else{
					return true;
					}
				}
		}		
		}			
				// Changed by aman for CR 1112
		else if(pId=='SupplementCardDetails_Modify')
		{
		 if(checkMandatory_SupplementGrid()){
				if(validateSuppEntry('modify')){
					var age=calcAge(getNGValue('SupplementCardDetails_DOB'),'');
				// Changed by Rajan for PCASP-1427
				if(getNGValue("SupplementCardDetails_Relationship")=='MOT' ||getNGValue("SupplementCardDetails_Relationship")=='FAT'||getNGValue("SupplementCardDetails_Relationship")=='SON'||getNGValue("SupplementCardDetails_Relationship")=='BRO' || getNGValue("SupplementCardDetails_Relationship")=='DAU' ||getNGValue("SupplementCardDetails_Relationship")=='HUS'||getNGValue("SupplementCardDetails_Relationship")=='WIF' ||getNGValue("SupplementCardDetails_Relationship")=='SIS' ||getNGValue("SupplementCardDetails_Relationship")=='PAR')
				{
					if(age<15){
								showAlert(pId,'Minor Customer not Applicable for Credit Card');
								//setNGValueCustom(pId,'');
								return false;
							  }
				}
				else if(getNGValue("SupplementCardDetails_Relationship")!='MOT' ||getNGValue("SupplementCardDetails_Relationship")!='FAT'||getNGValue("SupplementCardDetails_Relationship")!='SON'||getNGValue("SupplementCardDetails_Relationship")!='BRO' || getNGValue("SupplementCardDetails_Relationship")!='DAU' ||getNGValue("SupplementCardDetails_Relationship")!='HUS'|| getNGValue("SupplementCardDetails_Relationship")!='WIF'  ||getNGValue("SupplementCardDetails_Relationship")!='SIS' || getNGValue("SupplementCardDetails_Relationship")!='PAR')
				{// changed by shivang for PCASP-3203
					if(age<21 && getNGValue('SupplementCardDetails_Non_Resident')==false){
					    showAlert(pId,'Minor Customer not Applicable for Credit Card');
						//setNGValueCustom(pId,'');
						return false;
					}
					else if(getNGValue('SupplementCardDetails_Non_Resident')==true){
						showAlert(pId,'Only immediate family members are allowed for non resident supplementary card for ' + prod);
						return false;
					}
				}
					var cardProduct=getNGValue("SupplementCardDetails_CardProduct");
					if(cardProduct.indexOf('EKTMC')>-1 || cardProduct.indexOf('EKWEC')>-1 || cardProduct.indexOf('EKWEI')>-1){
						showAlert('',"Kindly Delete the supplementary card request.");
						return false;
					}
					if (getLVWRowCount('SupplementCardDetails_cmplx_supplementGrid')>30 ){
						showAlert('SupplementCardDetails_cmplx_supplementGrid',alerts_String_Map['VAL376']);
					}
					else{
					return true;
					}
				return true;
		}
		}}
		//++below code added by nikhil for Self-Supp CR
		else if(pId=='CardDetails_Self_add')
		{
			var new_value = 'CardDetails_Frame1';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1)
			{
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			return true;
		}
		else if(pId=='CardDetails_Self_remove')
		{
			var new_value = 'CardDetails_Frame1';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1)
			{
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			return true;
		}	
		//--above code added by nikhil for Self-Supp CR
				// Changed by aman for CR 1112
		else if(pId=='SupplementCardDetails_Delete')
		{	
			if(checkForApplicantTypeInGrids('Supplement')){
				return true;	
			}
		}
		if(pId=='SupplementCardDetails_Save')
		{
		  if(checkMandatory(CC_SUPPLEMENTARY_GRID))
			{
				removeFrame(pId);
				return true;}
		}
		//12th september
		else if (pId=='ExtLiability_Button2'){
		if(checkMandatory_LiabilityGrid()){
				
				return true;	
				
		}
			
		}
		else if(pId=='ExtLiability_Button3'){
		//added by aman for PCSP-67
			if(checkMandatory_LiabilityGrid()){
				
				return true;	
				
		}
		}
		else if(pId=='ExtLiability_Button4'){
		setNGValueCustom('Eligibility_Trigger','Y');
			return true;
		}
		// ++ below code already exist 06-10-2017 - Incoming document save button
		else if (pId=='IncomingDocument_Save'){
			return true;
		}
		//12th september
		else if(pId=='ExternalBlackList_Button1')		
		{
			return true;
		}
		
		else if(pId=='cmplx_Customer_SecNAtionApplicable_1')
		{
			setLockedCustom('cmplx_Customer_SecNationality',true);
			setLockedCustom('SecNationality_Button',true);
		}
		// added by abhishek as per CC FSD
		if(pId=='FATCA_w8form'){
			if(getNGValue('FATCA_w8form')==true && (getNGValue('FATCA_USRelation')=='R' || getNGValue('FATCA_USRelation')=='N')){
			setLockedCustom('FATCA_w9form',true);
			}
			else{
			setLockedCustom('FATCA_w9form',false);
			}	
		}
	
		else if(pId=='FATCA_w9form'){
			if(getNGValue('FATCA_w9form')==true && (getNGValue('FATCA_USRelation')=='R' || getNGValue('FATCA_USRelation')=='N')){
			setLockedCustom('FATCA_w8form',true);
			}
			else{
			setLockedCustom('FATCA_w8form',false);
			}	
		}
		//added by yash for CC FSD
	    if (pId=='NotepadDetails_savebutton')
	{
			//below code added by nikhil for PCSP-521 and updated for the same on 13th january
			if(activityName=='CPV' || (activityName=='FCU'||activityName=='FPU') || activityName=='CPV_Analyst' || activityName=='Smart_CPV')
			{
				var tellelog=getLVWRowCount('cmplx_NotepadDetails_cmplx_notegrid');
				var notepad=getLVWRowCount('cmplx_NotepadDetails_cmplx_Telloggrid'); 
				if(tellelog==0 && notepad==0)
				{
					showAlert('NotepadDetails_savebutton',alerts_String_Map['PL419']);
					return false;
				}
				
				
			}
			else
			{
			if(!checkMandatory(CC_Notepad_grid))
			return false;
			}
			return true;
		}
		else if(pId=='FATCA_Button1')
		{
			return true;
		}
			
		else if(pId=='FATCA_Button2'){
			return true;	
		}
		
		else if(pId=='cmplx_Customer_SecNAtionApplicable_0'){
			setLockedCustom('SecNationality_Button',false);
			//setEnabledCustom('cmplx_Customer_SecNationality',true);
        }
		
		if(pId=='DecisionHistory_Button3')
		{
			update_customer=true;
			//alert("inside createcif");
			return true;
		}
		
		if(pId=='cmplx_OECD_cmplx_GR_OecdDetails'){
			var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_OECD_cmplx_GR_OecdDetails');
			if(selectedRow != -1){
				if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,5)!=null && getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,5)!=""){
					setEnabledCustom('OECD_noTinReason',false);
				}
			}
			return true;
		}
		
		if(pId=='CC_Creation_Card_Services')
		{
			//alert("inside createcif");
			return true;
		}
		else if(pId=='Incomedetails'){
			
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Incomedetails',1);
			}	
			if(CCFRAGMENTLOADOPT['Incomedetails']==''){
				CCFRAGMENTLOADOPT['Incomedetails']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				document.getElementById("Button3").click();
				return false;
				//return true;
			}
			
			else if(CCFRAGMENTLOADOPT['Incomedetails']=='N' ){
					if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,1)=='Credit Card' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,9)=='Primary'){
						setVisible("IncomeDetails_Label12", true);
						setVisible("cmplx_IncomeDetails_StatementCycle", true);
						setVisible("IncomeDetails_Label14", true);
						setVisible("cmplx_IncomeDetails_StatementCycle2", true);
					}	
					else{
						setVisible("IncomeDetails_Label12", false);
						setVisible("cmplx_IncomeDetails_StatementCycle", false);
						setVisible("IncomeDetails_Label14", false);
						setVisible("cmplx_IncomeDetails_StatementCycle2", false);
					}
				}	
				var EmpType=getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6);
				if(EmpType=="Salaried" || EmpType=="Salaried Pensioner"){
					if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=="PA")
					{
					// console.log("In"); sagarika for PA subproduct
;					//setVisible("IncomeDetails_Frame1", true);
						setVisible("IncomeDetails_Frame2", true);
						setVisible("IncomeDetails_Frame3",true);//sagarika
					}
					else{
					setVisible("IncomeDetails_Frame3", false);
					}
					setVisible("IncomeDetails_Frame2", true);
					com.newgen.omniforms.formviewer.setHeight("Incomedetails", "630px");
					com.newgen.omniforms.formviewer.setHeight("IncomeDetails_Frame1", "605px");
					if(getNGValue('cmplx_Customer_NTB')==true){
						setVisible("IncomeDetails_Label11", false);
						setVisible("cmplx_IncomeDetails_DurationOfBanking", false);
						setVisible("IncomeDetails_Label13", false);
						setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", false);
						setVisible("IncomeDetails_Label3", false);
						setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", false);
					}	
					else{
						setVisible("IncomeDetails_Label11", true);
						setVisible("cmplx_IncomeDetails_DurationOfBanking", true);
						setVisible("IncomeDetails_Label13", true);
						setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", true);
						setVisible("IncomeDetails_Label3", true);
						setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", true);
					}	
				}
				
				else if(EmpType=="Self Employed"){
					setVisible("IncomeDetails_Frame2", false);
					setVisible("IncomeDetails_Frame3", true);
					com.newgen.omniforms.formviewer.setTop("IncomeDetails_Frame3","40px");
					com.newgen.omniforms.formviewer.setHeight("Incomedetails", "300px");
					com.newgen.omniforms.formviewer.setHeight("IncomeDetails_Frame1", "280px");
					if(getNGValue('cmplx_Customer_NTB')==true){
						setVisible("IncomeDetails_Label20", false);
						setVisible("cmplx_IncomeDetails_DurationOfBanking2", false);
						setVisible("IncomeDetails_Label22", false);
						setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", false);
						setVisible("IncomeDetails_Label35", false);
						setVisible("IncomeDetails_Label5", false);
						setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", false);
						setVisible("IncomeDetails_Label36", false);
						setNGValueCustom("cmplx_IncomeDetails_AvgBalFreq","Half Yearly");
						//alert("cmplx_IncomeDetails_AvgBalFreq"+cmplx_IncomeDetails_AvgBalFreq);
					}	
					else{
						setVisible("IncomeDetails_Label20", true);
						setVisible("cmplx_IncomeDetails_DurationOfBanking2", true);
						setVisible("IncomeDetails_Label22", true);
						setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", true);
						setVisible("IncomeDetails_Label35", true);
						setVisible("IncomeDetails_Label5", true);
						setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", true);
						setVisible("IncomeDetails_Label36", true);
						setNGValueCustom("cmplx_IncomeDetails_AvgBalFreq","Half Yearly");
						//alert("cmplx_IncomeDetails_AvgBalFreq"+cmplx_IncomeDetails_AvgBalFreq);
					}
				
				}
			
			LimitIncreaseFields_Income();
			ProductUpgrade_Income();
		}
		
		
		else if (pId == 'cmplx_Liability_New_AECBconsentAvail')
		{
			if (getNGValue('cmplx_Liability_New_AECBconsentAvail') == true)
				setLockedCustom('Liability_New_fetchLiabilities', false);
			else
				setLockedCustom('Liability_New_fetchLiabilities', true);
		}
		else if (pId == 'IncomeDetails_Add'){
			if(getNGValue('BankStatFrom')=='--Select--' || getNGValue('BankStatFrom')==''){
				showAlert('BankStatFrom',alerts_String_Map['CC019']);
				return false;
			}
			/*else if(getNGValue('IncomeDetails_BankStatFromDate')==''){
				showAlert('IncomeDetails_BankStatFromDate',alerts_String_Map['CC020']);
				return false;
			}
			else if(getNGValue('IncomeDetails_BankStatToDate')==''){
				showAlert('IncomeDetails_BankStatToDate',alerts_String_Map['CC021']);
				return false;
			} sagarika CR bank statement*/
			else{
			return true;
			}
		}
		else if (pId == 'IncomeDetails_Modify'){
		if(getNGValue('ncomeDetails_bankStatfrom')=='--Select--' || getNGValue('ncomeDetails_bankStatfrom')==''){
				showAlert('ncomeDetails_bankStatfrom',alerts_String_Map['CC019']);
				return false;
			}
		/*sagarika for CR change
			else if(getNGValue('IncomeDetails_BankStatFromDate')==''){
				showAlert('IncomeDetails_BankStatFromDate',alerts_String_Map['CC020']);
				return false;
			}
			else if(getNGValue('IncomeDetails_BankStatToDate')==''){
				showAlert('IncomeDetails_BankStatToDate',alerts_String_Map['CC021']);
				return false;
			}*/
			else{
			return true;
			}
		}
		else if (pId == 'IncomeDetails_Delete'){
			return true;
		}
		else if(pId=='AddressDetails_Save'){
			if(checkMandatory_AddressDetails_Save())
			{
				if(checkPrefferedChoice_1()){
					{
				removeFrame(pId);
				return true;}
				}
				else{return false;}
			}
		}
		//++below code added by nikhil for PCAS-1212 CR
		else if(pId=='cmplx_AddressDetails_cmplx_AddressGrid')
		{
			if(getNGListIndex(pId)!=-1 && (getLVWAT(pId,getNGListIndex(pId),0)=='OFFICE' && getLVWAT(pId,getNGListIndex(pId),13).indexOf('P-')>-1))
			{
				setLockedCustom('buildname',true);
				setLockedCustom('house',true);
				setLockedCustom('street',true);
			}
			else
			{
				setLockedCustom('buildname',false);
				setLockedCustom('house',false);
				setLockedCustom('street',true);
			}
		}
		//--above code added by nikhil for PCAS-1212 CR
		
		 else  if(pId=='Internal_External_Liability')
		{
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Internal_External_Liability',1);
			}
			
			else if(CCFRAGMENTLOADOPT['Internal_External_Liability']=='')
			{
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				CCFRAGMENTLOADOPT['Internal_External_Liability']='N';				
				return true;
			}					
			else if(CCFRAGMENTLOADOPT['Internal_External_Liability']=='N')
			{	
				CC_Fields_Liability();
			}
		}
		else  if(pId=='CompDetails')
		{
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('CompDetails',1);
			}	
			else if(CCFRAGMENTLOADOPT['CompDetails']=='')
			{
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				CCFRAGMENTLOADOPT['CompDetails']='N';				
				return true;
			}					
			
			else if(CCFRAGMENTLOADOPT['CompDetails']=='N' )
			{			
				var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				for(var i=0;i<n;i++){
					if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="BTC" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
						setVisible("CompanyDetails_Label8", true);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", true);//Effective length of buss
						setVisible("CompanyDetails_Label27", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", false);//Emirate of work
						setVisible("CompanyDetails_Label29", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", false);//head office emirate
						setVisible("CompanyDetails_Label28", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_VisaSponsor", false);//visa sponsor
						break;
					}
						
					else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Self Employed Credit Card" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
						setVisible("CompanyDetails_Label27", true);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", true);//Emirate of work
						setVisible("CompanyDetails_Label29", true);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", true);
						setVisible("CompanyDetails_Label28", true);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_VisaSponsor", true);
						setVisible("CompanyDetails_Label8", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", false);//Effective length of buss
						break;
					}
					
					else{
						setVisible("CompanyDetails_Label27", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", false);//Emirate of work
						setVisible("CompanyDetails_Label29", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", false);
						setVisible("CompanyDetails_Label28", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_VisaSponsor", false);
						setVisible("CompanyDetails_Label8", false);
						setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", false);
					}		
				}	
			}
		}
		else if(pId=='EmploymentDetails')
		{
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('EmploymentDetails',1);
			}	
			else if(CCFRAGMENTLOADOPT['EmploymentDetails']=='')
			{
		//		CreateIndicator("temp");
			//	document.getElementById("fade").style.display="block";
				CCFRAGMENTLOADOPT['EmploymentDetails']='N';				
				return true;
			}					
			else if(CCFRAGMENTLOADOPT['EmploymentDetails']=='N' )
			{	
						IMFields_Employment();
						BTCFields_Employment();
						LimitIncreaseFields_Employment();
						ProductUpgrade_Employment();
			}
		}
		else  if(pId=='EligibilityAndProductInformation'){
			
			
			
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('EligibilityAndProductInformation',1);
				return false;
				}
			if(CCFRAGMENTLOADOPT['EligibilityAndProductInformation']==''){
				CCFRAGMENTLOADOPT['EligibilityAndProductInformation']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}
			
			else if(CCFRAGMENTLOADOPT['EligibilityAndProductInformation']=='N'){
				Fields_Eligibility();
				var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if(n>0){
					for(var i=0;i<n;i++){
						if(getLVWAT("cmplx_Product_cmplx_ProductGrid", i, 1)=='Personal Loan' && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=='Primary'){
							setVisible('ELigibiltyAndProductInfo_Frame2',true);
							setVisible('ELigibiltyAndProductInfo_Frame4',true);
							setVisible('ELigibiltyAndProductInfo_Frame5',false);
							break;
						}
						else if(getLVWAT("cmplx_Product_cmplx_ProductGrid", i, 1)=='Credit Card' && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=='Primary'){
							setVisible('ELigibiltyAndProductInfo_Frame2',false);
							setVisible('ELigibiltyAndProductInfo_Frame4',false);
							setVisible('ELigibiltyAndProductInfo_Frame5',true);
							
						if(getLVWAT("cmplx_Product_cmplx_ProductGrid", i, 5).toUpperCase().indexOf("SECURED")>-1 && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=='Primary'){
							setVisible('ELigibiltyAndProductInfo_Frame3',true);
						}	
						else
								setVisible('ELigibiltyAndProductInfo_Frame3',false);	
						break;
						}
						else{
							setVisible('ELigibiltyAndProductInfo_Frame2',false);
							setVisible('ELigibiltyAndProductInfo_Frame4',false);
							setVisible('ELigibiltyAndProductInfo_Frame5',false);
						}
					}
				}
				else{
					setVisible('ELigibiltyAndProductInfo_Frame2',false);
					setVisible('ELigibiltyAndProductInfo_Frame3',false);
				}	
			
			}	
		}
		
		else  if(pId=='Alt_Contact_container' ){
			if(CCFRAGMENTLOADOPT['Alt_Contact_container']==''){
				CCFRAGMENTLOADOPT['Alt_Contact_container']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}
			
		}
	
	//for Customer Eligibility call(Tanshu Aggarwal-29/05/2017)
		else if(pId=='Customer_Button1'){
		//alert("inside button dedupe");
			return true;
		}
		else  if(pId=='CC_Loan_container' ){
			
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('CC_Loan_container',1);
				return false;
			}	
			if(CCFRAGMENTLOADOPT['CC_Loan_container']==''){
				CCFRAGMENTLOADOPT['CC_Loan_container']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}	
		}
		else if(pId=='Finacle_CRM_Incidents' ){
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Finacle_CRM_Incidents',1);
				return false;
			}	
			if(CCFRAGMENTLOADOPT['Finacle_CRM_Incidents']==''){
				CCFRAGMENTLOADOPT['Finacle_CRM_Incidents']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}	
		}

		else if(pId=='Finacle_CRM_CustomerInformation' ){
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Finacle_CRM_CustomerInformation',1);
				return false;
			}	
			if(CCFRAGMENTLOADOPT['Finacle_CRM_CustomerInformation']==''){
				CCFRAGMENTLOADOPT['Finacle_CRM_CustomerInformation']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}	
		}
//change by saurabh on 3 aug.
		else if(pId=='Finacle_Core' ){
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Finacle_Core',1);
				return false;
			}
			setLocked('Button1',false);
				setEnabled('Button1',true);
			//Added by aman for PCSP-200
			if (!isVisible('cmplx_FinacleCore_FinaclecoreGrid'))
			document.getElementById('Button1').click();
				
			
			
			//Added by aman for PCSP-200
			/*if(CCFRAGMENTLOADOPT['Finacle_Core']==''){
				CCFRAGMENTLOADOPT['Finacle_Core']='N';
				CreateIndicator("temp");
				document.getElementById("fade").style.display="block";
				return true;
			}	*/
		return false;
		}

		else if(pId=='World_Check' ){
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('World_Check',1);
				return false;
			}	
			if(CCFRAGMENTLOADOPT['World_Check']==''){
				CCFRAGMENTLOADOPT['World_Check']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}	
		}

		else if(pId=='MOL' ){
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('MOL',1);
				return false;
			}	
			if(CCFRAGMENTLOADOPT['MOL']==''){
				CCFRAGMENTLOADOPT['MOL']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}	
		}
		else if(pId=='Customer_Details_Verification'){
			document.getElementById('Button12').click();
			return false;
		}
		else if(pId=='LoanCard_Details_Check'){
			document.getElementById('Button13').click();
			return false;
		}
     // added for CARD_SERVICES_REQUEST 
	 else if(pId=='CC_Creation_Button2' ){
		 //++below code added by nikhil for Self-Supp CR
		 if(!check_Primary_Disbursal())
		 {
			 return false;
		 }
		 //--above code added by nikhil for Self-Supp CR
		//alert("inside New Card Request");
		return true;
	 }
 //added by yash
	else  if(pId=='FinacleCore_CalculateTotal'){
		// added by abhishek to validate repeater values
		if(ValidateRepeaterAvgNBC('FinacleCore_Frame10')){
			return true;
		}
		}
	//below code commented by nikhil for wrong validation at add to average 18/10.	
	/*else if(pId=='FinacleCore_CalculateTurnover'){
		if(ValidateRepeaterAvgNBC('FinacleCore_Frame11')){
			return true;
		}
		}*/
		//ended by yash
	 // ended for CARD_SERVICES_REQUEST 
	 //ended
		else if(pId=='Salary_Enquiry' ){
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Salary_Enquiry',1);
				return false;
			}	
			if(CCFRAGMENTLOADOPT['Salary_Enquiry']==''){
				CCFRAGMENTLOADOPT['Salary_Enquiry']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}	
		}

		else if(pId=='Reject_Enquiry' ){
		
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Reject_Enquiry',1);
				return false;
			}	
			if(CCFRAGMENTLOADOPT['Reject_Enquiry']==''){
				CCFRAGMENTLOADOPT['Reject_Enquiry']='N';
				//CreateIndicator("temp");
				//document.getElementById("fade").style.display="block";
				return true;
			}	
		}
		/*if(pId=='Part_Match' ){
			//var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='BTC' || getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='Self Employed Credit Card'){
						setVisible("PartMatch_Label16", true);
						setVisible("PartMatch_Text14", true);
						setVisible("PartMatch_Label17", true);
						setVisible("PartMatch_Text15", true);
				}
			else{
			
						setVisible("PartMatch_Label16", false);
						setVisible("PartMatch_Text14", false);
						setVisible("PartMatch_Label17", false);
						setVisible("PartMatch_Text15", false);
			}
			if(CCFRAGMENTLOADOPT['Part_Match']==''){
				CCFRAGMENTLOADOPT['Part_Match']='N';
				CreateIndicator("temp");
				document.getElementById("fade").style.display="block";
				return true;
			}	
		}*/
		
		// added for CARD_SERVICES_REQUEST 
		else if(pId=='CC_Creation_Update_Customer'){
			//alert("inside Customer_Update")
			return true;
		}
		 // ended for CARD_SERVICES_REQUEST 
		
		//tanshu added
		else if(pId=='IncomeDetails_FinacialSummarySelf')
		{
			//alert("inside financial summary");
			//call changes by saurabh on 6th Dec
			var activityANme = window.parent.stractivityName;
			if((getNGValue('IS_AECB')==null || getNGValue('IS_AECB') == '') && activityANme!='DDVT_Maker')
			{
				showAlert('Internal_External_Liability',alerts_String_Map['VAL106']);
				return false;
			}
			if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true)
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}
			//change for overwrite details
			/*if(getNGValue('Is_Overwrite_Details').toUpperCase()!='SUCCESS' && activityANme!='DDVT_Maker'){
				showAlert('Internal_External_Liability','Please Overwrite Details first');
				return false;
			}---commented temprary by akshay*/
			var request_name = "";
			var parentWiName = getNGValue('parent_WIName');
			var subproduct = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
			
			// below code done to find opertaion names of financial summary on 29th Dec by disha
			/*
			if((subproduct=='Salaried Credit Card' || subproduct=='SAL') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SIDET,SALDET';
			}
			if((subproduct=='Self Employed Credit Card' || subproduct=='SE') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET';
			}
			if((subproduct=='Business titanium Card' || subproduct=='BTC') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET';
			}
			if((subproduct=='Instant Money' || subproduct=='IM') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,SALDET,LIENDET,SIDET';
			}
			if((subproduct=='Limit Increase' || subproduct=='LI') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,SALDET,LIENDET,SIDET';
			}
			if((subproduct=='Limit Increase' || subproduct=='LI') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET';
			}
			if((subproduct=='Pre-Approved' || subproduct=='PA') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET';
			}
			if((subproduct=='Pre-Approved' || subproduct=='PA') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SIDET,SALDET';
			}
			if((subproduct=='Product Upgrade' || subproduct=='PU') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SIDET,SALDET';
			}
			if((subproduct=='Product Upgrade' || subproduct=='PU') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET';
			}
			if((subproduct=='Expat Personal Loans' || subproduct=='EXP') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SIDET,SALDET';
			}
			if((subproduct=='Secured Card' || subproduct=='SEC') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SALDET,SIDET';
			}
			if((subproduct=='Secured Card' || subproduct=='SEC') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'RETURNDET,LIENDET,AVGBALDET,TRANSUM,SIDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='National Personal Loans' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SALDET,SIDET';
			} */
				//var param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNo')+'"}');
				var company_cif=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",1,3);
				var param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNo')+'","sub_product":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)+'","emp_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)+'","company_cif":"'+company_cif+'","application_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4)+'"}');
				
				// above code done to find opertaion names of financial summary on 29th Dec by disha
				
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				//alert ('wi_name '+ wi_name);
				//alert ('activityName '+ activityName);
				setNGValueCustom('cmplx_IncomeDetails_AvgCredTurnover','');
				setNGValueCustom('cmplx_IncomeDetails_CredTurnover','');
				//setNGValueCustom('cmplx_IncomeDetails_SalaryDay','');
				setNGValueCustom('cmplx_IncomeDetails_AvgBal','');
				
				var url = '/webdesktop/custom/CustomJSP/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name +'&activityName=' + activityName + '&parentWiName=' + parentWiName+'&sessionId='+window.parent.sessionId;;
				//window.showModalDialog(url, "", "scrollbars=yes,resizable=yes,width=620px,height=700px");
				
				var returnValue=window.open_(url, "", "scrollbars=yes,resizable=yes,width=500px,height=700px");
				//alert('return '+returnValue);
				//below code commented as same logic moved in db procedure 18/10/18
				/*if(typeof returnValue==='object'){
					var AvgBalance=returnValue['AvgBalanceDtls'];
					var AvgCrTurnOver=returnValue['AvgCrTurnOver'];
					var TotalCrAmt=returnValue['TotalCrAmt'];
					if(AvgBalance!='' && AvgBalance!='undefined'){
						setNGValueCustom('cmplx_IncomeDetails_AvgBal',AvgBalance);
					}
					if(AvgCrTurnOver!='' && AvgCrTurnOver!='undefined'){
						setNGValueCustom('cmplx_IncomeDetails_AvgCredTurnover',AvgCrTurnOver);
					}
					if(AvgCrTurnOver!='' && AvgCrTurnOver!='undefined'){
						setNGValueCustom('cmplx_IncomeDetails_CredTurnover',AvgCrTurnOver);
					}
					 status=returnValue['aecb_call_status'];
				}*/
				
				//change by saurabh on 30th Nov for FSD 2.7
				setNGValueCustom('Is_Financial_Summary','Y');
				//++below code added by nikhil for PCAS-364 CR
				setNGValueCustom('Eligibility_Trigger','Y');
				//--above code added by nikhil for PCAS-364 CR
				
					return true;
			}
			else if(pId=='IncomeDetails_RefreshFinacle')
			{
				//To refresh the finacle core data
				return true;
			}
			/*if(pId=='ExtLiability_Button1'){
			//if(AccountSummary_checkMandatory())
			//{				
				var request_name = "";
				if(getNGValue('existingOldCustomer')==true)
				{
					request_name = 'InternalExposure,ExternalExposure,CollectionsSummary';
				}
				else
					request_name = 'ExternalExposure';
				
				var prod = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,1);
				var subprod = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
				
				var param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_customer_FirstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LAstNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_gender":"'+getNGValue('cmplx_Customer_gender')+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_Passport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PAssport4')+'","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_PAssport4')+'"}');
				
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				
				var url='/formviewer/resources/scripts/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod;
				window.open(url, "", "width=500,height=300");
				return true;
			//}
		}	*/
		else if(pId=='Customer_FetchDetails'){
			setEnabled("PartMatch_Search",true);
			setLocked("PartMatch_Search",false);
			
		if((getNGValue('is_fetch')=='')||(getNGValue('is_fetch')=='Y' && getNGValue('Is_PartMatchSearch')!='Y'))
		{
			return true;
		}
		if(getNGValue('is_fetch')=='Y' && getNGValue('Is_PartMatchSearch')=='Y')
		{
			var n=getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");
					if(n>0)
					{
						for(var i=0;i<n;i++)
						{
							if(getLVWAT("cmplx_FinacleCRMCustInfo_FincustGrid",i,0)!="" || getLVWAT("cmplx_FinacleCRMCustInfo_FincustGrid",i,0)==getNGValue("cmplx_Customer_CIFNo"))
							{
							showAlert(" ","Delete CIFs added from part match and re-add after fetch details & part match is completed");
		                    return false;	
							}
						}
					}
		setNGValue('cmplx_PartMatch_partmatch_flag'," ");	
        setLocked("PartMatch_Search", false);
		setEnabled("PartMatch_Search", true);
		return true;
		}
		else{
			return true;
		}
		}
		//tanshu ended
		
		else if(pId=='CardDetails_Reset'){
			return true;
		}
		
		
		//for Incomindoc		
		else if(pId=='IncomingDocument_AddFromDMSButton'){
			//below code added bu nikhil
			//alert("row clicked inside add from pc button");
			var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
			//alert("row clicked:"+row);
			var doc_clicked_id ="IncomingDocument_Frame_Reprow"+row+"_Repcolumn4";
			//alert("value: "+ document.getElementById(doc_clicked_id).value);
			var docname1=document.getElementById(doc_clicked_id).disabled;
			//alert("value of docname "+ docname1);
			if(docname1==true)
			{
				return false;
			}
			addDocFromOD();
		}
		
		else if(pId=='IncomingDocument_AddFromPCButton'){
		/*	//alert("row clicked inside add from pc button");
			var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
			//alert("row clicked:"+row);
			var doc_clicked_id ="IncomingDocument_Frame_Reprow"+row+"_Repcolumn0";
			var doc_clicked_id_1 ="IncomingDocument_Frame_Reprow"+row+"_Repcolumn4";
			//alert("value: "+ document.getElementById(doc_clicked_id).value);
			var docname1=document.getElementById(doc_clicked_id).value;
			var docname2=document.getElementById(doc_clicked_id_1).readOnly;
			if(docname2==true)
			{
				return false;
			}
			//alert("value of docname "+ docname1);
			addDocFromPC();
			window.parent.DocName(docname1);
			 //below code added by tanshu - 06-10-2017 to have docindex of income doc fragment
			window.parent.rowClicked(row);
			//below code added by tanshu - 06-10-2017 to have docindex of income doc fragment
			return true;
		*/	
			
			
			
			  var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
			  setNGValueCustom('Rownum',row);
              var doc_clicked_id_1 = "IncomingDocument_Frame_Reprow" + row + "_Repcolumn4";
              var remarks_state = document.getElementById(doc_clicked_id_1).disabled;
			  if(remarks_state == true)
				{
					return false;
				}
			  var doc_clicked_id = "IncomingDocument_Frame_Reprow" + row + "_Repcolumn0";
              var docname1 = document.getElementById(doc_clicked_id).value;
        
				addDocFromPC();
			  window.parent.DocName(docname1);
              return true;
			
		}
		
		else if(pId=='IncomingDocument_ScanButton'){
			//below code added by nikhil
			//alert("row clicked inside add from pc button");
			var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
			//alert("row clicked:"+row);
			var doc_clicked_id ="IncomingDocument_Frame_Reprow"+row+"_Repcolumn4";
			//alert("value: "+ document.getElementById(doc_clicked_id).value);
			var docname1=document.getElementById(doc_clicked_id).disabled;
			//alert("value of docname "+ docname1);
			if(docname1==true)
			{
				return false;
			}
				openScannerWindow();
				
				
		}
		
		else if(pId=='IncomingDocument_ViewButton'){
				/*	var obj = getInterfaceData("DLIST","");
					alert(obj);
					return false;
				*/	
		var obj = getInterfaceData("DLIST", "");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        var row_print = "IncomingDocument_Frame_Reprow" + row + "_Repcolumn11";
	  //var row_print = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn11";
						
	   var docIndex = document.getElementById(row_print).value;
		//alert("value of docIndex is:"+docIndex);
        window.parent.reloadapplet(docIndex, '');

		}
		
		else if(pId=='IncomingDocument_PrintButton'){
				//printImageDocument();
		var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        var row_print = "IncomingDocument_Frame_Reprow" + row + "_Repcolumn11";
        var docIndex = document.getElementById(row_print).value;
        var doc_clicked_id = "IncomingDocument_Frame_Reprow" + row + "_Repcolumn0";
        var docName = document.getElementById(doc_clicked_id).value;
        printImageDocument(docIndex);
	}
		
		else if(pId=='IncomingDocument_DownloadButton'){
		//below code added by tanshu - 06-10-2017 to view befor download
			var obj = getInterfaceData("DLIST", "");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        var row_print = "IncomingDocument_Frame_Reprow" + row + "_Repcolumn11";
        var docIndex = document.getElementById(row_print).value;
        parent.reloadapplet(docIndex, '');
		//above code added by tanshu - 06-10-2017 to view befor download
				customDownloadDocument();
		}
			//ended here
		//below code added by nikhil 16/1/17
			else if(pId=='MOL1_AttachButton')
			{
				var docname1='MOL_printout';
				window.parent.DocName(docname1);
				addDocFromPC();
				return true;
			}
		
		else if(pId == 'Reference_Details_Reference_Add'){
			if(checkMandatory_ReferenceGrid()){
					if(validate_ReferenceGrid())
						return true;
				}
		}
		else if(pId == 'Reference_Details_Reference__modify'){
			if(checkMandatory_ReferenceGrid())
							return true;
		}
		else if(pId == 'Reference_Details_Reference_delete'){
			return true;
		}
		
	else if(pId=='Customer_Reference_Add'){
				if(checkMandatory_ReferenceGrid()){
					if(validate_ReferenceGrid())
						return true;
						}
				}
					
					else if(pId=='Customer_Reference__modify'){
						if(checkMandatory_ReferenceGrid())
							return true;
				}
				else if(pId=='Customer_Reference_delete'){
					return true;
			  }
			else if(pId=='Add'){
			 if(checkMandatory_ProductGrid()){
					if(Product_add_validate()){
						if(getNGValue('ReqProd')=='Personal Loan' &&  getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')<18)
						{
							setVisible("GuarantorDetails", true);
							//if(isVisible('Customer_Frame1'))
								setTop("Incomedetails",parseInt(getTop('GuarantorDetails'))+parseInt(getHeight("GuarantorDetails"))+5+"px");
							//else
								//setTop("Incomedetails","520px");
								
							setSheetVisible("ParentTab", 1, false);
						}
						if(getNGValue('ReqProd')=='Credit Card')
						{
							setSheetVisible("ParentTab", 7, true);
							setSheetVisible("ParentTab", 1, true);
						}
						
						if(getNGValue('EmpType')=='Salaried'){								
							setSheetVisible("ParentTab", 1, false);
							setSheetVisible("ParentTab", 3, true);
						}	
						else if(getNGValue('EmpType')=='Self Employed'){
							setSheetVisible("ParentTab", 1, true);
							setSheetVisible("ParentTab", 3, false);
						}
						return true;
					}
			else 
				return false;
		}
		else
			return false;
	}
		
		 else if(pId=='Modify'){
			if(checkMandatory_ProductGrid()){
				if(Product_modify_validate()){
					if(getNGValue('ReqProd')=='Personal Loan'  &&  getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')<18)
					{
						setVisible("GuarantorDetails", true);
						if(isVisible('Customer_Frame1'))
							setTop("Incomedetails","1310px");
						else
							setTop("Incomedetails","520px");								
						setSheetVisible("ParentTab", 1, false);
					}
					if(getNGValue('ReqProd')=='Credit Card')
					{
						setSheetVisible("ParentTab", 7, true);
						setSheetVisible("ParentTab", 1, true);
					}
					if(getNGValue('EmpType')=='Salaried'){								
						setSheetVisible("ParentTab", 1, false);
						setSheetVisible("ParentTab", 3, true);
					}	
					else if(getNGValue('EmpType')=='Self Employed'){
						setSheetVisible("ParentTab", 1, true);
						setSheetVisible("ParentTab", 3, false);
					}
					
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}	
		
		else if(pId=='Delete'){
			return true;
		}
		else if (pId=='cmplx_FinacleCore_FinaclecoreGrid')
		{
			return true;
		}
		
		 else if(pId=='cmplx_Product_cmplx_ProductGrid')
		{
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),1)=='Personal Loan'){
				setVisible('Product_Label3',true);
				setVisible('Product_Label5',true);
				setVisible('ReqTenor',true);
				setVisible('Scheme',true);
				setVisible('CardProd',false);
				setVisible('Product_Label6',false);

			}
			else{
					setVisible('Product_Label6',true);
					setVisible('Product_Label3',false);
					setVisible('Scheme',false);
					if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)!='IM')
						setVisible("CardProd", true);
					if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2).indexOf('Limit')>-1){
						setVisible("LimitAcc", true);
						setVisible("LimitExpiryDate", true);
						//setVisible("typeReq", true);
						setVisible("Product_Label15", true);
						setVisible("Product_Label16", true);
						setVisible("Product_Label18", true);
					}
				}
				return true;
		}

		else if(pId=='Product_Save'){
			if(checkMandatory(CC_PRODUCT))
				{
				removeFrame(pId);
				return true;}
		}
		
		else  if(pId=='IncomeDetails_Salaried_Save'){
		if(getNGValue('cmplx_Customer_VIPFlag')==true)
		{
			removeFrame(pId);
				return true;
		}
			if(Income_Save_Check() )
				{
				removeFrame(pId);
				return true;}
		}
		
		/* else if(pId=='IncomeDetails_SelfEmployed_Save'){
			if(checkMandatory(CC_INCOME_SELFEMPLOYED))
				return true;	
		}*/
		else if(pId=='EMploymentDetails_Button2'){
			if(getNGValue('EMploymentDetails_Text21')!='' || getNGValue('EMploymentDetails_Text22')!=''){
				return true;	
		}
		showAlert('EMploymentDetails_Button2','Kindly provide Employer name or code');
		}

		else if(pId=='FinacleCRMCustInfo_save'){ 
			if(Employment_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='Button6')
			return true;
		else if(pId=='ELigibiltyAndProductInfo_Save'){
			  if(checkMandatory(CC_ELIGIBILITY))
			{
				
			
			// Done by aman for CR 1312	
			//below code commented by nikhil for PCSP-
		/*	
		if (parseInt(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit'))!=parseInt(getNGValue('cmplx_EligibilityAndProductInfo_EFCHidden')))
		{
			showAlert('',alerts_String_Map['VAL377']);
			return false;
		}	
		*/
// Done by aman for CR 1312 
				//below code changes by nikhil 22-10-18 
				if(getNGValue('Sub_Product')=='IM' && (getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4)!='NEWIM') && (getNGValue('cmplx_EligibilityAndProductInfo_FullEligibility')=='' || getNGValue('cmplx_EligibilityAndProductInfo_FullEligibility')=='--Select--'))
				{
					showAlert('cmplx_EligibilityAndProductInfo_FullEligibility',alerts_String_Map['CAS026']);
					return false;
				}
				if(getNGValue('Sub_Product')=='IM' && (getNGValue('cmplx_EligibilityAndProductInfo_Max_threshold_exposure')=='' || getNGValue('cmplx_EligibilityAndProductInfo_Max_threshold_exposure')=='--Select--') && activityName=='CAD_Analyst1'
				 && !(getNGValue('cmplx_EmploymentDetails_IncInCC')) && !(getNGValue('cmplx_EmploymentDetails_IncInPL')))
				{
					showAlert('cmplx_EligibilityAndProductInfo_Max_threshold_exposure',alerts_String_Map['CAS027']);
					return false;
				}
				
					removeFrame(pId);
					return true;				
					
				
			}
		}
		else if(pId=='CustDetailVerification1_Button2'){
		//below code added by nikhil for FCU Changes 21/10/18
			var CompName=getNGValue("CustDetailVerification1_Compname");
			var CompCif=getNGValue("CustDetailVerification1_Compcode");
			var EmiratesID=getNGValue("cmplx_CustDetailverification1_EmiratesId");
			var CIFID=getNGValue("cmplx_CustDetailverification1_CID_ID");
			if(CompName!='' || CompCif!=''  || EmiratesID!='' || CIFID!='')
			{
				return true;
			}
			else
			{	
				showAlert('CustDetailVerification1_Button2',alerts_String_Map['PL413']);
				return false;			
			  //if(checkMandatory(CC_ELIGIBILITY))
					
					}
		}	
		// ++ below code already present - 06-10-2017 for world check
		else if(pId=='WorldCheck1_fetch')
		{
			 // alert('inside world check');
					return true;
		}
		// sagarika
		else if(pId=='Cust_ver_sp2_Button1'){
			if(getNGValue('EmploymentType')!='Self Employed')
			{
			if(checkCust() )
			{
				if(getNGValue('cmplx_cust_ver_sp2_Mobile')!="")
				{
						var Mobile_no=getNGValue('cmplx_cust_ver_sp2_Mobile');
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValueCustom(pId,mob_No);
		}
		if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
			showAlert(pId,'Incorrect mobile number format');
			setNGValueCustom(pId,'');
		}		
		if(mob_No.length<13 || mob_No.length>15){
			showAlert(pId,alerts_String_Map['CC277']);
			return false;
			}
				}
				else 
				{
					return true;
				}
				return true;
			
			}
			
			else
						//if(chechmandatory_SaveFCU())
						return false;
		}
		else
		{//pcasp-2875		
			if(getNGValue('cmplx_cust_ver_sp2_Mobile')!="") {
					var Mobile_no=getNGValue('cmplx_cust_ver_sp2_Mobile');
					var mob_No=Mobile_no;
					if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
						mob_No="00971"+mob_No;
						setNGValueCustom(pId,mob_No);
					}
					if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
						showAlert(pId,'Incorrect mobile number format');
						setNGValueCustom(pId,'');
					}		
					if(mob_No.length<13 || mob_No.length>15){
						showAlert(pId,alerts_String_Map['CC277']);
						return false;
					}
			} 
			//PCASP-2874 by shweta
			if(getNGValue('cmplx_cust_ver_sp2_FPU_Remarks_Self')==null || getNGValue('cmplx_cust_ver_sp2_FPU_Remarks_Self')=="")
			{
				showAlert(pId,alerts_String_Map['CC290']);
				return false;
			}
			return true;
		
			}
		}
		else if(pId=='BussinessVerification1_Button1'){  
			// ++ below code already present - 06-10-2017 to check mandatory on BussinessVerification
					if(checkMandatory_saveBussinessVeri())
					return true;
		}
		 else if(pId=='EmploymentVerification_s2_Button1')
		 { 
		 if (getNGValue('EmploymentType')!='Self Employed')
		 {
		 if(checkEmp() )
		 {
			 if(getNGValue('cmplx_emp_ver_sp2_landline')!="")
				{
		var Mobile_no=getNGValue('cmplx_emp_ver_sp2_landline');
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValueCustom(pId,mob_No);
		}
		if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
			showAlert(pId,'Incorrect land line number format');
			setNGValueCustom(pId,'');
		}		
		if(mob_No.length<13 || mob_No.length>15){
			showAlert(pId,'Landline number should be between 13-15 digits');
			return false;
			}
		
				}
					return true;
		 }
				else 
				{
					return false;
				}		
		 }
		 else{//pcasp-2875
			 if(getNGValue('cmplx_emp_ver_sp2_landline')!="") {
				var Mobile_no=getNGValue('cmplx_emp_ver_sp2_landline');
				var mob_No=Mobile_no;
				if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
					mob_No="00971"+mob_No;
					setNGValueCustom(pId,mob_No);
				}
				if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
					showAlert(pId,'Incorrect land line number format');
					setNGValueCustom(pId,'');
				}		
				if(mob_No.length<13 || mob_No.length>15){
					showAlert(pId,'Landline number should be between 13-15 digits');
					return false;
					}
				}
		 	if(getNGValue('cmplx_emp_ver_sp2_fpu_rem')==""){
		 		showAlert('cmplx_emp_ver_sp2_fpu_rem',alerts_String_Map['CC290']);	
		 		return false;
			}
				return true;
		 }
		 }
		//sagarika
		 else if(pId=='fieldvisit_sp2_Button1')
		 {  
	 if(getNGValue('cmplx_fieldvisit_sp2_field_visit_date')!="")
	 {//
	if(getNGValue('cmplx_fieldvisit_sp2_field_v_time')=="")		
{
	showAlert("cmplx_fieldvisit_sp2_field_v_time","Please fill Field visit initiated time");
	return false;
}
	 }
	 	 if(getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate')!="")
	 {//
	if(getNGValue('cmplx_fieldvisit_sp2_field_visit_rec_time')=="")		
{
	showAlert("cmplx_fieldvisit_sp2_field_visit_rec_time","Please fill Field visit received time");
	return false;
}
	 }
		 //alert("clicked");
		// validate_time('cmplx_fieldvisit_sp2_field_v_time');
	if(getNGValue('cmplx_fieldvisit_sp2_field_v_time')!="")	 
	{
  var str=getNGValue('cmplx_fieldvisit_sp2_field_v_time');
  var re=/^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
   if(re.test(str))
    {
     
    }
    else
    {
       showAlert(pId,alerts_String_Map['CC283']);
       return false;
    }
	}
/*	if(getNGValue('cmplx_fieldvisit_sp2_field_visit_date')!="")	 
	{
      if(getNGValue('cmplx_fieldvisit_sp2_field_v_time')!="")
	  {
		  
	  }
    }PartMatch_Button1*/
	if(getNGValue('cmplx_fieldvisit_sp2_field_visit_rec_time')!="")	 
	{
	 var str=getNGValue('cmplx_fieldvisit_sp2_field_visit_rec_time');
  var re=/^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
   if(re.test(str))
    {
       
    }
    else
    {
       showAlert(pId,alerts_String_Map['CC283']);
       return false;
    }
	}
		// validate_time('cmplx_fieldvisit_sp2_field_visit_rec_time');
	var d2 = getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate');
   var parts=d2.split('/');
    var date2=new Date(parts[2],parts[1]-1,parts[0]);
    var d =  getNGValue('cmplx_fieldvisit_sp2_field_visit_date');
   var parts = d.split('/');
   var date1=new Date(parts[2],parts[1]-1,parts[0]);
   if(date1>date2)
   {
	   showAlert('cmplx_fieldvisit_sp2_field_rep_receivedDate','Field visit received date should be greater than field initiated date');
	   //setNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate','');
	   return false;
   }

          return true;
		}
		 else if(pId=='checklist_ver_sp2_Button1'){  
				//	if(checkMandatory_EmpVerification())
					return true;
		}
		else if(pId=='FinacleCore_Data_Refresh'){  
				//	if(checkMandatory_EmpVerification())
					return true;
		}
		 else if(pId=='exceptionalCase_sp2_Button2'){  
				//	if(checkMandatory_EmpVerification())
					return true;
		}
		else if(pId=='CAD_Decision_Button2')
		{
			var subProd_value= getNGValue('subProd');
			var apptype_value = getNGValue('AppType');
			if(subProd_value=='BTC')
			 {
				window.parent.executeAction('2', 'Y');
				//return true;
			 }
			else if(subProd_value=='IM') 
			 {
				window.parent.executeAction('3', 'Y');
				//return true;
			 }
			else if ((apptype_value == 'Expat') || (apptype_value == 'National'))
			{
				window.parent.executeAction('1', 'Y');
				//return true;
			}
		 }
		 
		else if(pId=='ELigibiltyAndProductInfo_Button1'){
		var vvip_flag= getNGValue('cmplx_Customer_VIPFlag');
		if (getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')=='')
		{
			showAlert('','Final Limit Cannot be Blank!');
			return false;
		}
		var mandatory_frame = getNGValue('Mandatory_Frames');
		if(getNGValue('EmploymentType')=='Self Employed' && !vvip_flag)
		{
		mandatory_frame+='#Finacle_Core:Finacle Core:FinacleCore_Frame1';
		}
		if(!checkMandatory_Frames(mandatory_frame))
		{
		return false;
		}
		/*if(isVisible("DecisionHistory_Frame1")==false)
		{
			showAlert('DecisionHistory_Frame1','Please Visit Decision History First.');
			setNGFrameState('DecisionHistory',1);
			return false;			
		}*/
			//SetValueCustomer_CheckEligibility();
				// Done by aman for CR 1312	
		// Done by aman for CR 1312	
		//below code commented by nikhil as same moved to java.	
	/*	if (parseInt(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit'))!=parseInt(getNGValue('cmplx_EligibilityAndProductInfo_EFCHidden')))
		{
			showAlert('',alerts_String_Map['VAL377']);
			return false;
		}	*/
		
// Done by aman for CR 1312 
// Done by aman for CR 1312
		
			//change by saurabh on 30th Nov for FSD 2.7
			//change by saurabh on 13th Dec
			//below code added by nikhil for PCSP-674
			var newAddedrow=0;
			for(var i=0;i<getLVWRowCount('DecisionHistory_Decision_ListView1');i++)
			{
			if(getLVWAT('DecisionHistory_Decision_ListView1',i,12)=='')
			{
				newAddedrow++;
			}		
			}
			if(newAddedrow>0)
			{
			showAlert('cmplx_DEC_Decision',alerts_String_Map['PL421']);
			return false;
			}
			if(activityName=='CAD_Analyst1' && visitSystemChecks_Flag==false){
				showAlert('',alerts_String_Map['CC257']);
				return false;
			}
			
			 if(getNGValue('FinacleCoreSave')!='Y' && (getNGValue('Sub_Product')=='BTC' || getNGValue('Sub_Product')=='SE'  || getNGValue('Sub_Product')=='PA') && !vvip_flag){//changed by akshay for proc 12490
				 showAlert('',alerts_String_Map['CC274']);
				 	return false;
			 }
			  if(Company_save_clicked==false && (getNGValue('Sub_Product')=='BTC' || getNGValue('Sub_Product')=='SE') && !vvip_flag){//changed by akshay for proc 12490
				 showAlert('CompanyDetails_Save','Company Save is Mandatory!');
				 	return false;
			 }
			 //below code added by nikhil for Data Refresh 
			 if(getNGValue('Data_refresh_trigger')=='Y')
			 {
			 showAlert('FinacleCore_Data_Refresh','Please Perform Finacle Data Refersh!');
				return false;
			 }
			   if(ext_liab_click==false && (getNGValue('Sub_Product')=='BTC' || getNGValue('Sub_Product')=='SE' || getNGValue('Sub_Product')=='PA') && !vvip_flag){//changed by akshay for proc 12490
				 showAlert('ExtLiability_Save','Liability Details Save is Mandatory!');
				 	return false;
			 }
			
			 if(getNGValue('Is_Financial_Summary')!='Y' && getNGValue('Account_Number')!=''){
				if(getNGValue('cmplx_Customer_NTB')==false){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
				}
				if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
				// Prabhakar Jira 1231
				if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true)
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}
			}
			//Prabhakar jira no-1231 6-08-2019
				if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true)
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}
			//below code added by nikhil 31/12/18 for PCSP-389
			if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
			 if(getNGValue('IS_Employment_Saved')!='Y' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed'){
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}
			if(getNGValue('Sub_Product')=='SEC' || (getNGValue('Sub_Product')=='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='SMES'))
			{
				if(getNGValue('cmplx_FinacleCore_Final_FD_Amount')=='' || getNGValue('cmplx_FinacleCore_Final_FD_Amount')==null)
				{
					showAlert('ELigibiltyAndProductInfo_Button1','Final FD Amount is Mandatory!');
				return false;
				}
			}
			var apptype = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
			//deepak commented for CRTO change as perafrin email.
			 //if ((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SE' || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA'&& !(apptype=='MOR' || apptype=='RFL' || apptype=='SME'))) && (getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=0 || getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=-1) && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='CAC'  && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='DOC'){
			 if ((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SE' || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA'&& !(apptype=='MOR'))) && (getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=0 || getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=-1) && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='CAC'  && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='DOC'){
			 	 if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='BPA' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='SAL' && !vvip_flag){
					if(getNGValue('cmplx_FinacleCore_total_avg_last13')=='' || getNGValue('cmplx_FinacleCore_total_avg_last13')==''){
					showAlert('',alerts_String_Map['CC272']);
					return false;
					}
					//Deepak changes for CRTO validation on 17 OCT 21 
					if(getNGValue('cmplx_FinacleCore_avg_credit_3month')=='' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='PA'){
					showAlert('',alerts_String_Map['CC273']);
					return false;
					}
				}
			}
			
			
			var counter_elig = getNGValue('reEligibility_CC_counter').split(';')[0];
			var counter_decision = getNGValue('reEligibility_CC_counter').split(';')[1];
			if(pId=='ELigibiltyAndProductInfo_Button1'){
				if(counter_elig==(parseInt(alerts_String_Map['VAL323'])-1).toString()){
				setNGValueCustom('reEligibility_CC_counter',alerts_String_Map['VAL323']+';'+counter_decision);
				setEnabledCustom('ELigibiltyAndProductInfo_Button1',false);
				}
				else if(parseInt(counter_elig)<(parseInt(alerts_String_Map['VAL323'])-1)){
				setNGValueCustom('reEligibility_CC_counter',(parseInt(counter_elig)+1).toString()+';'+counter_decision);
				}
			}
			//Deepak Code 24 Dec to save highest deligation on ELigibiltyAndProductInfo start.
			//changed by nikhil for PCSP-489
			var new_value = 'DecisionHistory,IncomeDetails';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			//Deepak Code 24 Dec to save highest deligation on ELigibiltyAndProductInfo end.
			//++below code added by nikhil for PCAS-364 CR
				setNGValueCustom('Eligibility_Trigger','N');
			//--above code added by nikhil for PCAS-364 CR
			return true;
			
	}
	
		else if(pId=='CompanyDetails_Add'){
			if(checkMandatory_CompanyGrid()){
				if(validate_Company()){
					//Added by shivang fro PCASP-2519
					/*var tlexpDate=getLVWAT('TLExpiry_Cont');
					if(checkPastDate(tlexpDate)){
					 showAlert('TLExpiry_Cont',alerts_String_Map['VAL399']);
					 return false;
					}*/
					var pId="TLExpiry";
					validatePastDate(pId,"TLExpiry");
					setNGValueCustom('appNAme',getNGValue('compName'));
					setNGValueCustom('appCateg','Business');
					setLockedCustom('CompanyDetails_Frame3',false);
					return true;
				}
			}	
			flag_Company_FetchDetails=false;
		}	
		
		else if(pId=='CompanyDetails_Modify'){
		var new_value = 'CompDetails';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			if(checkMandatory_CompanyGrid()){
				if(validate_Company()){
					//Added by shivang fro PCASP-2519
					//var tlexpDate=getNGValue('TLExpiry_Cont');
					/*if(checkPastDate(tlexpDate)){
					 showAlert('TLExpiry_Cont',alerts_String_Map['VAL399']);
					 return false;
					}*/
					var pId="TLExpiry";
					if(!validatePastDate(pId,"TLExpiry")){
						setLocked('TLExpiry',false);
						return false;
					}
					if(getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',com.newgen.omniforms.formviewer.getNGListIndex(		'cmplx_CompanyDetails_cmplx_CompanyGrid'),1)=='Business'){
						setNGValueCustom('appNAme',getNGValue('compName'));
					}
					return true;
				}
				
			}		
			
        }
		else if(pId=='CompanyDetails_delete'){
			return true;	
		}	
		//added by akshay on 15/12/17
		else if(pId=='cmplx_CompanyDetails_cmplx_CompanyGrid'){
			if(getNGListIndex(pId)==0 ){
				setLockedCustom('CompanyDetails_delete',true);
			}
			else{
				setLockedCustom('CompanyDetails_delete',false);
			}
			if(activityName=='Cad_Analyst2')
			{
			setLockedCustom('CompanyDetails_delete',true);
			}
			return true;
		}
		else if(pId=='cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails' ){
			if(getNGListIndex(pId)==0){
				setLockedCustom('AuthorisedSignDetails_delete',true);
			}
			else{
				setLockedCustom('AuthorisedSignDetails_delete',false);
			}
			if(activityName=='Cad_Analyst2' || activityName=='CAD_Analyst1')
			{
			setLockedCustom('AuthorisedSignDetails_delete',true);
			}	
			if(activityName=='CAD_Analyst1'){
				setLocked('AuthorisedSignDetails_FetchDetails',false);
			}			
		}
		//ended by akshay on 15/12/17
	
		else if(pId=='CompanyDetails_Button3')
		{
		var new_value = 'CompDetails';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			if(getNGValue('cif')==''){
				showAlert('cif',alerts_String_Map['CC039']);
				return false;
			}	
			flag_Company_FetchDetails=true;
			return true;
		}	
		else if(pId=='CompanyDetails_Modify'){
		if(getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CompanyDetails_cmplx_CompanyGrid'),1)=='Business'){
				setNGValueCustom('appNAme',getNGValue('compName'));
			}
			return true;
		}
		else if(pId=='CompanyDetails_delete')
			return true;	
	// ++ below code already present - 06-10-2017 for income details button
		else if(pId=='IncomeDetails_Button1'){
			return true;
			}
		else if(pId=='IncomeDetails_Button2'){
			return true;
			}
		else if(pId=='IncomeDetails_Button3'){
			return true;
			}
	// ++ above code already present - 06-10-2017 for income details button
		else if(pId=='PartnerDetails_add'){
			if(checkMandatory_PartnerGrid())
				return true;
		}
		else if(pId=='PartnerDetails_modify'){
			if(checkMandatory_PartnerGrid())
			return true;
         }
		else if(pId=='PartnerDetails_delete'){
			return true;		
		}
		else if(pId=='FinacleCore_Cal_FD_Amount')
		{
			if(getNGValue('cmplx_FinacleCore_Account_FD')=='')
			{
				showAlert('FinacleCore_Cal_FD_Amount','Please Select FD Account');
				return false;
			}
			return true;
		}
				//added by yash for CC FSD
		else if(pId=='CompanyDetails_Button1')
		{
			if(checkMandatory(CC_CompanyDetails))
			{
				if(getNGValue('CompanyDetails_CheckBox4')==true)
			{
				if(getNGValue('cif')=='')
				{
				showAlert('cif',alerts_String_Map['CC039']);
				return false;
				}
			}
			
		}
				return true;	
		}
	// ++ below code already present - 06-10-2017 card details button
		else if(pId=='CardDetails_add')
		{
		  if(checkMandatory_2ndgridcarddetails())
				return true;	
		}
		else if(pId=='CardDetails_modify')
		{
			//Added by Nikita on 28 July 2021 for CRN generated on the basis of applicant Type Start
		  
			 var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CardDetails_cmplx_CardCRNDetails');
			if(selectedRow!=-1){
				var Applicanttype = getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',selectedRow,9);
				var ECRN=getNGValue("CardDetails_ECRN");
				var CRN =	getNGValue("CardDetails_CRN");
				 if(Applicanttype=='Primary')
				 {
					if(CRN.substring(CRN.length-2,CRN.length)!='00' || ECRN.substring(ECRN.length-2,ECRN.length)!='00')
					{
						showAlert('','ECRN/CRN generated is invalid. Please generate new ECRN/CRN for Primary.');
						return false;
					}
					else
						return true;
				 }
				 else if(Applicanttype=='Supplement')
				 {
					if(CRN.substring(CRN.length-2,CRN.length)=='00') 
					{
						showAlert('','ECRN/CRN generated is invalid. Please generate new ECRN/CRN for Supplementary.');
						return false;
						
					}
					else
						return true;
				 }
			}
			//Added by Nikita on 28 July 2021 for CRN generated on the basis of applicant Type End
				 CRN=CRN+","+getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
				  ECRN=ECRN+","+getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 1);
				setNGValue("CRN", CRN);	
				setNGValue("ECRN", ECRN);	
		  if(checkMandatory_2ndgridcarddetails())
				return true;	
		}
		else if(pId=='CardDetails_delete')
		{
		  return true;	
		}
	// ++ above code already present - 06-10-2017 card details button
	
	// ++ below code already present - 06-10-2017 supplementary details button
	if(pId=='SupplementCardDetails_Button1')
		{
		if(getNGListIndex('SupplementCardDetails_cmplx_supplementGrid')==-1){
			 showAlert('','Please Select a Row to trigger Firco call!');
			 return false;
		}
		var new_value = 'SupplementCardDetails_Frame1';
		var field_string_value = getNGValue('FrameName');
		if(field_string_value.indexOf(new_value)==-1)
		{
			var newString = field_string_value + new_value+',';
			setNGValueCustom('FrameName',newString);
		}
		 return true;
		}
	if(pId=='Customer_FetchFirco'){
		var checkVal = confirm("Are you sure you have verified the personal details to initiate FIRCO?");
		if(checkVal==true){
			return true;
		}else {
			return false;
		}
	}
		if(pId=='SupplementCardDetails_Button2')
		{
		  if(checkMandatory_SupplementGrid())
				return true;
		}
		if(pId=='SupplementCardDetails_Button3')
		{
		 if(checkMandatory_SupplementGrid())
				return true;
		}
		if(pId=='SupplementCardDetails_Button4')
		{	
				return true;	
		}
		
		if(pId=='SupplementCardDetails_Save')
		{
		  if(checkMandatory(CC_SUPPLEMENTARY_GRID))
				return true;
		}
		// ++ above code already present - 06-10-2017 supplementary details button
			if(pId=='ExternalBlackList_Save'){
				return true;
			}
		  /*else if(checkMandatory(CC_ExternalBlackList)){
				return true;
				}*/
		else if(pId=='FinacleCRMCustInfo_Button1'){
		  if(checkMandatory(CC_FinacleCRM_Customer))
				return true;
				}
	   else	if(pId=='AuthorisedSignDetails_Button1')
		{
		  if(checkMandatory(CC_AuthorizeDetails))
				return true;
		}				
		  else	if(pId=='PartnerDetails_Button1'){
		  if(checkMandatory(CC_PartnerGrid))
				return true;	
		}								
								
		//added by yash for cc FSD
		else if(pId=='NotepadDetails_Add'){
		if(checkMandatory(CC_Notepad)){
			//below code added by sagarika for PCSP-416
		if(getNGValue('NotepadDetails_workstep')==activityName)
		{
		showAlert('NotepadDetails_Add',alerts_String_Map['PL417']);
		return false;
		}
			return true;
			}
		}
		
		
                  
			//added by abhishek as per CC FSD
		else if(pId=='NotepadDetails_Modify'){
		
			if(getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',getNGListIndex('cmplx_NotepadDetails_cmplx_notegrid'),7)=='true')//PCAS-2101 by sagarika
							{
								showAlert('cmplx_NotepadDetails_cmplx_notegrid',alerts_String_Map['PL422']);
								return false;
							}
			
			var notepadDesc = getLVWAT('cmplx_NotepadDetails_cmplx_notegrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_NotepadDetails_cmplx_notegrid"),11);
			//++below code added by abhishek on 11/11/2017 for new CR
			var GridInstructionQueue = getLVWAT('cmplx_NotepadDetails_cmplx_notegrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_NotepadDetails_cmplx_notegrid"),6);
			var FieldInstructionQueue = getNGValue('NotepadDetails_insqueue');
			//changed by nikhil for PCSP-739
			if(GridInstructionQueue==FieldInstructionQueue){
						if((notepadDesc!='NA' && n&& notepadDesc.toUpperCase()!=activityName.toUpperCase()) ){
							
							//++Above code added by abhishek on 11/11/2017 for new CR
							 showAlert('NotepadDetails_notedesc',alerts_String_Map['VAL152']);
								 
								   document.getElementById("NotepadDetails_notedesc").value = '--Select--';
									document.getElementById("NotepadDetails_notecode").value = '';
									 document.getElementById("NotepadDetails_Actdate").value = '';
									 
									  document.getElementById("NotepadDetails_notedetails").value = '';
									document.getElementById("NotepadDetails_inscompletion").checked = false;
									 document.getElementById("NotepadDetails_ActuserRemarks").value = '';
									  document.getElementById("NotepadDetails_Actusername").value = '';
									  
									  setEnabledCustom('NotepadDetails_inscompletion',false);
									  setEnabledCustom('NotepadDetails_ActuserRemarks',false);
									  //++ below code added by abhishek on 10/11/2017
									   setEnabledCustom('NotepadDetails_notedetails',true);
									  setEnabledCustom('NotepadDetails_notedesc',true);
									  setEnabledCustom('NotepadDetails_Add',true);
									  setEnabledCustom('NotepadDetails_Delete',true);
									  setLockedCustom('NotepadDetails_notedetails',false);
									  //++ above code added by abhishek on 10/11/2017
									  
								   
							  return false;
								
							
						}
						else{
						if(activityName!=GridInstructionQueue)
							{
								if(checkMandatory(CC_Notepad_modify))
								{
									//below code added by nikhil for PCSP-501
									if(getNGValue('NotepadDetails_inscompletion')==false)
									{
										showAlert('NotepadDetails_inscompletion',alerts_String_Map['PL414']);
										return false;
									}
								}
								else{
									return false;
								}
							}
							if(getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',getNGListIndex('cmplx_NotepadDetails_cmplx_notegrid'),7)=='true')//PCAS-2101 by sagarika
							{
								showAlert('cmplx_NotepadDetails_cmplx_notegrid',alerts_String_Map['PL422']);
								return false;
							}
							return true;
							}
					
				}
				else{
					//if(checkMandatory(CC_Notepad_modify))
				    return true;
				}
			
			
            }
			
		else if(pId=='NotepadDetails_Delete')
			return true;
		// added by abhishek as per CC FSD
		
		else if(pId=='cmplx_NotepadDetails_cmplx_notegrid')
			return true;
			//ended by yash
			
			


//ended by yash for CC FSD
//changed by yash on 12/8/2017
		if(pId=='AuthorisedSignDetails_CheckBox1'){
		//id changes by saurabh on 6th Dec
			if(getNGValue(pId)==true){
				setLockedCustom('AuthorisedSignDetails_CIFNo',false);
				setLockedCustom('AuthorisedSignDetails_FetchDetails',false);
				setLockedCustom('AuthorisedSignDetails_Name',true);
				setLockedCustom('AuthorisedSignDetails_nationality',true);
				setLockedCustom('AuthorisedSignDetails_DOB',true);
				setLockedCustom('AuthorisedSignDetails_VisaNumber',true);
				setLockedCustom('AuthorisedSignDetails_VisaExpiryDate',true);
				setLockedCustom('AuthorisedSignDetails_Status',true);
				setLockedCustom('AuthorisedSignDetails_PassportNo',true);
				setLockedCustom('AuthorisedSignDetails_PassportExpiryDate',true);
				setLockedCustom('AuthorisedSignDetails_Combo1',true);
				//setLockedCustom('AuthorisedSignDetails_shareholding',false);
				//setLockedCustom('AuthorisedSignDetails_SoleEmployee',false);
			}
			else{
				setLockedCustom('AuthorisedSignDetails_CIFNo',true);
				setLockedCustom('AuthorisedSignDetails_FetchDetails',true);
				setLockedCustom('AuthorisedSignDetails_Name',false);
				setLockedCustom('AuthorisedSignDetails_nationality',false);
				setLockedCustom('AuthorisedSignDetails_DOB',false);
				setLockedCustom('AuthorisedSignDetails_VisaNumber',false);
				setLockedCustom('AuthorisedSignDetails_VisaExpiryDate',false);
				setLockedCustom('AuthorisedSignDetails_Status',false);
				setLockedCustom('AuthorisedSignDetails_PassportNo',false);
				setLockedCustom('AuthorisedSignDetails_PassportExpiryDate',false);
				setLockedCustom('AuthorisedSignDetails_Combo1',false);
			}
		}
		 if(pId=='ExtLiability_CACIndicator'){	
				if(getNGValue("ExtLiability_CACIndicator")==true)
				{
					// added by yash					
					setLockedCustom("ExtLiability_QCAmt",false);
				    setLockedCustom("ExtLiability_QCEMI",false);
					setLockedCustom("Liability_New_MOB",false);//changed by aman for PCSP-174
					
					
				}
				else 
				{
				setLockedCustom("ExtLiability_QCAmt",true);
				    setLockedCustom("ExtLiability_QCEMI",true);
					setLockedCustom("Liability_New_MOB",true);//changed by aman for PCSP-174
				}
			}
if(pId=='ExtLiability_Takeoverindicator'){	
				if(getNGValue("ExtLiability_Takeoverindicator")==true)
				{
					// added by yash					
					setLockedCustom("ExtLiability_takeoverAMount",false);
					//change by saurabh on 7th Dec
					setNGValueCustom("ExtLiability_Consdierforobligations", false);
				    //setLockedCustom("ExtLiability_QCEMI",false);
					
				}
				else 
				{
				setLockedCustom("ExtLiability_takeoverAMount",true);
				//change by saurabh on 7th Dec
				setNGValueCustom("ExtLiability_Consdierforobligations", true);
				    //setLockedCustom("ExtLiability_QCEMI",true);
				}
			}
		//change by saurabh on 6th Dec
		else if(pId=='AuthorisedSignDetails_FetchDetails')
		{
		
			if(getNGValue('AuthorisedSignDetails_CIFNo')==''){
				showAlert('AuthorisedSignDetails_CIFNo',alerts_String_Map['VAL032']);
				return false;
			}	
			flag_Authorised_FetchDetails=true;
			return true;
		}	
		
		else if(pId=='AuthorisedSignDetails_add'){
			if(AuthSignatory_checkMandatory()){
				if(AuthSignatory_validate('Add')){//added by akshay on 23/11/17 for validating sole employee
					if(getNGValue('AuthorisedSignDetails_SoleEmployee') == "Yes"){
						setLockedCustom('CompanyDetails_Frame3',true);
					}
					else{
						setLockedCustom('CompanyDetails_Frame3',false);
					}	
					flag_Authorised_FetchDetails=false;
					return true;//added By AKshay on 10/10/17-----FOr point 16 in BTC_NTB_Defects xls
				}
			}					
					
		}
		
		else if(pId=='AuthorisedSignDetails_modify'){
			if(AuthSignatory_checkMandatory()){
				if(AuthSignatory_validate('Modify')){//added by akshay on 23/11/17 for validating sole employee
					if(getNGValue('AuthorisedSignDetails_SoleEmployee') == "Yes"){
						setLockedCustom('CompanyDetails_Frame3',true);
					}
					else{
						setLockedCustom('CompanyDetails_Frame3',false);
					}	
					flag_Authorised_FetchDetails=false;
					return true;//added By AKshay on 10/10/17-----FOr point 16 in BTC_NTB_Defects xls
				}
			}					
					
		}
		
		else if(pId=='AuthorisedSignDetails_delete'){
			return true;
		}
			
		else if(pId=='BTC_save'){
			if(checkMandatory(CC_BTC))
				{
				removeFrame(pId);
				return true;}
		}
		//Below code added by shweta
		else if(pId=='DDS_save'){
			// ++ below code already present - 06-10-2017 below line of onsite commented
			if(checkMandatory(CC_DDS_GRID)){//Added by shweta for pcasp-1401
			removeFrame(pId);
			return true;}
		}
		else if(pId=='SI_save'){
		// ++ below code already present - 06-10-2017 below line of onsite commented
			if(checkMandatory(CC_SI_GRID)){//Added by shweta for pcasp-1401
			removeFrame(pId);
			return true;}
		}
		else if(pId=='RVC_Save'){
			if(checkMandatory(CC_RVC_GRID)){//Added by shweta for pcasp-1401
				removeFrame(pId);
				return true;}
		//Above code added by shweta
		// added by abhishek as per CC FSd
		}
		else if(pId=='cmplx_CC_Loan_DDSFlag')
		{
			if(getNGValue("cmplx_CC_Loan_DDSFlag")==false)
			{
				setLockedCustom("cmplx_CC_Loan_DDSMode",true);
				setLockedCustom("cmplx_CC_Loan_DDSAmount",true);
				setLockedCustom("cmplx_CC_Loan_Percentage",true);
				setLockedCustom("cmplx_CC_Loan_DDSExecDay",true);
				setLockedCustom("cmplx_CC_Loan_AccType",true);
				setLockedCustom("cmplx_CC_Loan_DDSIBanNo",true);
				setLockedCustom("cmplx_CC_Loan_DDSStartdate",true);
				setLockedCustom("cmplx_CC_Loan_DDSBankAName",true);
				setLockedCustom("cmplx_CC_Loan_DDSEntityNo",true);
				setLockedCustom("DDS_save",true);

			}
			else{
				setLockedCustom("cmplx_CC_Loan_DDSMode",false);
				setLockedCustom("cmplx_CC_Loan_DDSAmount",false);
				setLockedCustom("cmplx_CC_Loan_Percentage",false);
				setLockedCustom("cmplx_CC_Loan_DDSExecDay",false);
				setLockedCustom("cmplx_CC_Loan_AccType",false);
				setLockedCustom("cmplx_CC_Loan_DDSIBanNo",false);
				setLockedCustom("cmplx_CC_Loan_DDSStartdate",false);
				setLockedCustom("cmplx_CC_Loan_DDSBankAName",false);
				setLockedCustom("cmplx_CC_Loan_DDSEntityNo",true);//PCASP-2142
				setLockedCustom("DDS_save",false);

			}
		}	
		else if(pId=='IncomingDoc_ViewSIgnature'){
			var wi_name = window.parent.pid;
			var activityName = window.parent.stractivityName;
			var url="/webdesktop/custom/CustomJSP/OpenImage.jsp?workstepName="+window.parent.stractivityName+"&signMatchNeededAtChecker=A"+"&debt_acc_num="+""+"&wi_name="+wi_name+"&workstepName="+activityName+"&CifId="+getNGValue('cmplx_Customer_CIFNo');
			window.open_(url, "", "width=700,height=600,scrollbars=yes, scrollbars=1, toolbar=no, resizable=1, menubar=no");
		}	
		
		else if(pId=='IncomingDoc_UploadSig'){
			/*var activityName = window.parent.stractivityName;
			var wi_name = window.parent.pid;
			var firstName = getNGValue('cmplx_Customer_FirstNAme');
			var middleName = getNGValue('cmplx_Customer_MiddleNAme');
			var lastName = getNGValue('cmplx_Customer_LastNAme');
			var fullName = firstName+" "+middleName+" "+lastName;
			
			//alert("fullName "+ fullName);
			*/
			
			uploadDocument();
			//
		}
		/*else if(pId=='PartMatch_Search'){
			//if(checkMandatory(CC_PARTMATCH))
			return true;
		}*/	
		
		else if(pId=='PartMatch_Button1')	
		{
			var partSearchPerform = getNGValue('cmplx_PartMatch_partmatch_flag');
			var activityName = window.parent.stractivityName;
			//alert("partSearchPerform "+ partSearchPerform);
			if(partSearchPerform=="" || partSearchPerform =="N")
			{
				showAlert('PartMatch_Search',alerts_String_Map['CC160']);
				return false;
			}
			if(getNGValue('cmplx_PartMatch_partmatch_flag')=="")
			{
				showAlert('PartMatch_Search',"Please do part match first!");
				return false;
			}
			else
			{
				//alert("cmplx_PartMatch_Is_NTB "+ getNGValue('cmplx_PartMatch_Is_NTB'));
				var SelectedRow = com.newgen.omniforms.formviewer.getNGListIndex("cmplx_PartMatch_cmplx_Partmatch_grid");
				
				var n=getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
				//alert("inside false");
				
				//alert('noRows -- '+ noRows);
				if(n==0)
				{
					showAlert('PartMatch_Button1',alerts_String_Map['CC160']);
					return false;
				}
				if(SelectedRow=='-1')
				{
					showAlert('PartMatch_Button1',alerts_String_Map['CC161']);
					return false;
				}
				//added by akshay on 31/3/18 for proc 7309
				//else if(getLVWAT('cmplx_PartMatch_cmplx_Partmatch_grid',SelectedRow,0)==getNGValue('cmplx_Customer_CIFNo'))
					//{
						//showAlert(pId,alerts_String_Map['CC262']);
						//return false;
					//}
					for(var i=0;i<getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');i++)
					{
						if(getLVWAT('cmplx_PartMatch_cmplx_Partmatch_grid',SelectedRow,0)==getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0))
						{
						showAlert(pId,alerts_String_Map['CC262']);
						return false;
						}
					}
					
			}	
			return true;			
		}
		
		else if(pId=='PartMatch_Save'){
				removeFrame(pId);
				return true;}
		
		else if(pId=='CompanyDetails_Save'){
		Company_save_clicked=true;
				//removeFrame(pId);
				
				if(CheckCompanyMandatory())
			{
				//var pId="TLExpiry";
				//validatePastDate(pId,"TLExpiry");
				return true; 
			}}//Arun (11/10)
		
		
		else if(pId=='Compliance_Button2'){			
				return true;
		}	
		
		else if(pId=='FinacleCore_Button1'){
			if(checkMandatory(CC_FINACLECORE_DDS))
				{
				var new_value = 'Finacle_Core';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
				removeFrame(pId);
				return true;}
		}	
		else if(pId=='FinacleCore_Button2'){	
				{
				var new_value = 'Finacle_Core';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
				removeFrame(pId);
				return true;}
		}
		else if(pId=='FinacleCore_Button3'){
			
				{
				removeFrame(pId);
				return true;}
		}	
		else if(pId=='FinacleCore_Button4'){
			if(checkMandatory(CC_FINACLECORE_INWARDTT))
				{
				removeFrame(pId);
				return true;}
		}	
		else if(pId=='FinacleCore_Button5'){
				removeFrame(pId);
				return true;}
		// ++ below code already present - 06-10-2017 Finacle core button
		else if(pId=='FinacleCore_Button6'){
				removeFrame(pId);
				return true;}
		else if(pId=='FinacleCore_Button7'){
				removeFrame(pId);
				return true;}
		// ++ above code already present - 06-10-2017 Finacle core button
	//added by yash for CC FSD
		else if(pId=='FinacleCore_Save'){
			//if(checkMandatory(CC_InwardTT))
				setNGValueCustom('FinacleCoreSave','Y');
				return true;
		}
		else if(pId=='WorldCheck1_Button1'){
			if(checkMandatory(CC_WORLDCHECK))
				{
				removeFrame(pId);
				return true;}
			else
				return false;
		}	
		else if(pId=='WorldCheck1_Button2'){			
				return true;
		}	
		else if(pId=='WorldCheck1_Button3'){
			
				return true;
		}
         /*(else if(pId=='PartnerDetails_Button1'){
			if(Partner_Save_Check())
				return true;
		 }*/
		 else if(pId=='FinacleCRMIncident_Button1'){
			return true;		    
		}
		else if(pId=='FinacleCRMCustInfo_Button1'){
			return true;		    
		}
		else if(pId=='FinacleCore_Button8'){
			return true;		    
		}
		else if(pId=='MOL1_Button1'){
		if(checkMandatory(CC_MOL))
			return true;
			else
				return false;		    
		}
		else if(pId=='PartMatch_Button3'){
			return true;		    
		}
		//added by yash on 23/8/2017
		 if(pId=='DecisionHistory_save'){
                //  if(checkMandatory(CC_Decision))
				//12th september as decision rsn code is mandatory on dec when selected as rejected
			var activityName = window.parent.stractivityName;
					if(activityName=='Rejected_queue'){
						var Decision=getNGValue("cmplx_DEC_Decision");
						var Remarks=getNGValue("cmplx_DEC_RejectReason");
						if(Decision==""||Decision=="--Select--"){
							showAlert('cmplx_DEC_Decision',alerts_String_Map['CC176']);
							return false;
						}
						if(Remarks==""){
							showAlert('cmplx_DEC_RejectReason',alerts_String_Map['CC155']);
							return false;
						}
						else{
							return true;
						}
					}
					// ++ below code already present - 06-10-2017 validation for OV and CAD1 on decision save
					if(activityName=='Original_Validation'){
						var Decision=getNGValue("cmplx_DEC_Decision");
						var DecisionResnCode=getNGValue("DecisionHistory_dec_reason_code");
						if(Decision==""||Decision=="--Select--"){
							showAlert('cmplx_DEC_Decision',alerts_String_Map['CC176']);
							return false;
						}
						if(Decision=="REJECTED"){
							if(DecisionResnCode==""||DecisionResnCode=="--Select--"){
							showAlert('DecisionHistory_dec_reason_code',alerts_String_Map['CC177']);
							return false;
						}
						return true;
						}
						else{
							return true;
						}
					}
					if(activityName=='CAD_Analyst1'){
						
						var decision=getNGValue("cmplx_DEC_Decision")
						var Reason_code=getNGValue("DecisionHistory_dec_reason_code");
						var cad_decision=getNGValue("cmplx_DEC_ReferTo");
						if(decision=="REJECT")
						{
							if(Reason_code==""||Reason_code=="--Select--"){
								showAlert('DecisionHistory_dec_reason_code',alerts_String_Map['CC178']);
								return false;
							}
						}
						if(decision=="Refer to Credit")
						{
							if(cad_decision==""||cad_decision=="--Select--"){
								showAlert('cad_decision',alerts_String_Map['CC175']);
								return false;
							}
						}
						// disha Manual deviaton reason mandatory if tick box checked
						if(getNGValue('cmplx_DEC_Manual_Deviation')==true)
						{
							if(getNGValue('cmplx_DEC_Manual_Dev_Reason')=='' )
							{
								showAlert('DecisionHistory_ManualDevReason',alerts_String_Map['CC146']);
								return false;
							}
						}
						if(getNGValue('cmplx_DEC_Strength')=='')
						{
							showAlert('cmplx_DEC_Strength',alerts_String_Map['CC148']);
							return false;

						}
						if(getNGValue('cmplx_DEC_Weakness')=='')
						{
							showAlert('cmplx_DEC_Weakness',alerts_String_Map['CC149']);
							return false;

						}
						if(getNGValue('cmplx_DEC_Remarks')=='')
						{
							showAlert('cmplx_DEC_Remarks',alerts_String_Map['CC147']);
							return false;

						}
				// ++ above code already present - 06-10-2017 validation for OV and CAD1
						else{
							if(!checkMandatory(CC_DECISION))
							return false;
						}
					}
					//change by saurabh for PCSP-143
					/*//change by saurabh on christmas.
					if(getNGValue('cmplx_DEC_Decision').toUpperCase()=="REJECT"){
							if(isFieldFilled('DecisionHistory_dec_reason_code')==false){
								showAlert('DecisionHistory_dec_reason_code',alerts_String_Map['CC177']);
								return false;
							}	
					}*/
					
					
			return true;					
		}
		//added by yash on 11/8/2017
		else if(pId=='WorldCheck1_Button4'){
		if(checkMandatory(CC_WorldCheck))
			return true;		    
		}
		else if(pId=='SalaryEnq_Button1'){
			if(checkMandatory(CC_SalaryEnquiry))
			return true;		    
		}
		else if(pId=='RejectEnq_Save'){
		if(checkMandatory(CC_RejectEnq))
			return true;		    
		}
	
//added by yash for CC FSD
		/*else if(pId=='NotepadDetails_save_button'){
                 if(checkMandatory(CC_Notepad_grid))
			return true;		    
		}*/
               /* combining it with NotepadDetails_Add1
			   else if(pId=='NotepadDetails_Clear'){
					 

			return true;		    
		}*/
		else if(pId=='NotepadDetails_save'){
		if(checkMandatory(CC_Notepad_Details))
			{
				removeFrame(pId);
				return true;}

//ended by yash			
		}
		else if(pId=='SmartCheck1_Modify'){
		if(getNGValue('SmartCheck1_CreditRemarks')!='' && (activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2'))
			{
				var comments=getNGValue('SmartCheck1_CreditRemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck1_CreditRemarks',datetime+'-'+userid+'-'+comments);
			}
			if(getNGValue('SmartCheck1_CPVRemarks')!=''&& (activityName=='CPV' || activityName=='CPV_Analyst'))
			{
				var comments=getNGValue('SmartCheck1_CPVRemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck1_CPVRemarks',datetime+'-'+userid+'-'+comments);
			}
			if(getNGValue('SmartCheck1_FCURemarks')!=''&& (activityName=='FCU'||activityName=='FPU'))
			{
				var comments=getNGValue('SmartCheck1_FCURemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck1_FCURemarks',datetime+'-'+userid+'-'+comments);
			}
			return true;		    
		}
		else if(pId=='SmartCheck1_Add'){
			if(getNGValue('SmartCheck1_CreditRemarks')==""  && (activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2'))
				{
					showAlert("SmartCheck1_CreditRemarks","Credit remarks is blank");
					return false;
				}
			
		if(getNGValue('SmartCheck1_CreditRemarks')!='' && (activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2'))
			{
				var comments=getNGValue('SmartCheck1_CreditRemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck1_CreditRemarks',datetime+'-'+userid+'-'+comments);
			}
			if(getNGValue('SmartCheck1_CPVRemarks')!=''&& (activityName=='CPV' || activityName=='CPV_Analyst'))
			{
				var comments=getNGValue('SmartCheck1_CPVRemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck1_CPVRemarks',datetime+'-'+userid+'-'+comments);
			}
			if(getNGValue('SmartCheck1_FCURemarks')!=''&& (activityName=='FCU'||activityName=='FPU'))
			{
				var comments=getNGValue('SmartCheck1_FCURemarks'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck1_FCURemarks',datetime+'-'+userid+'-'+comments);
			}
			return true;		    
		}
		else if(pId=='SmartCheck1_Delete'){
			return true;		    
		}
		else if(pId=='SmartCheck1_Save'){
			return true;		    
		}
		// Added for dectech call
		else if(pId=='DecisionHistory_calReElig'){
		var vvip_flag= getNGValue('cmplx_Customer_VIPFlag');
		var mandatory_frame = getNGValue('Mandatory_Frames');
		if(getNGValue('EmploymentType')=='Self Employed' && !vvip_flag)
		{
		mandatory_frame+='#Finacle_Core:Finacle Core:FinacleCore_Frame1';
		}
		if(!checkMandatory_Frames(mandatory_frame))
		{
		return false;
		}
		
			//below code added by nikhil for PCSP-674
			var newAddedrow=0;
			for(var i=0;i<getLVWRowCount('DecisionHistory_Decision_ListView1');i++)
			{
			if(getLVWAT('DecisionHistory_Decision_ListView1',i,12)=='')
			{
				newAddedrow++;
			}		
			}
			if(newAddedrow>0)
			{
			showAlert('cmplx_DEC_Decision',alerts_String_Map['PL421']);
			return false;
			}
			//change by saurabh on 30th nov 17.
			if(activityName=='CAD_Analyst1' && visitSystemChecks_Flag==false){//ADDED BY AKSHAY ON 20/12/17
				showAlert('',alerts_String_Map['CC257']);
				return false;
			}	
			 if(getNGValue('FinacleCoreSave')!='Y' && (getNGValue('Sub_Product')=='BTC' || getNGValue('Sub_Product')=='SE'  || getNGValue('Sub_Product')=='PA') && !vvip_flag){//changed by akshay for proc 12490
				 showAlert('',alerts_String_Map['CC274']);
				 	return false;
			 }
			  if(Company_save_clicked==false && (getNGValue('Sub_Product')=='BTC' || getNGValue('Sub_Product')=='SE') && !vvip_flag){//changed by akshay for proc 12490
				 showAlert('CompanyDetails_Save','Company Save is Mandatory!');
				 	return false;
			 }
			
			 if(getNGValue('Is_Financial_Summary')!='Y' && getNGValue('Account_Number')!=''){
				if(getNGValue('cmplx_Customer_NTB')==false){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
				}
				if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
				// Prabhakar Jira 1231
				if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true)
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}
			}
			//Prabhakar jira no-1231 6-08-2019
				if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true)
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}
			//below code added by nikhil 31/12/18 for PCSP-389
			if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
			 if(getNGValue('IS_Employment_Saved')!='Y' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed'){
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}
			if(getNGValue('Sub_Product')=='SEC' || (getNGValue('Sub_Product')=='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='SMES'))
			{
				if(getNGValue('cmplx_FinacleCore_Final_FD_Amount')=='' || getNGValue('cmplx_FinacleCore_Final_FD_Amount')==null)
				{
					showAlert('ELigibiltyAndProductInfo_Button1','Final FD Amount is Mandatory!');
				return false;
				}
			}
			var apptype = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
			//deepak commented for CRTO change as perafrin email.
			//if ((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SE' || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA'&& !(apptype=='MOR' || apptype=='RFL' || apptype=='SME'))) && (getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=0 || getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=-1) && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='CAC'  && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='DOC')
			if ((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SE' || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA'&& !(apptype=='MOR' ))) && (getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=0 || getLVWRowCount('cmplx_FinacleCore_FinaclecoreGrid')!=-1) && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='CAC'  && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,22)!='DOC')
			{
				 if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='BPA' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='SAL' && !vvip_flag){
					 
					if(getNGValue('cmplx_FinacleCore_total_avg_last13')=='' || getNGValue('cmplx_FinacleCore_total_avg_last13')==''){
					showAlert('',alerts_String_Map['CC272']);
					return false;
					}
					//Deepak changes for CRTO validation on 17 OCT 21 
					if(getNGValue('cmplx_FinacleCore_avg_credit_3month')=='' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='PA'){
					showAlert('',alerts_String_Map['CC273']);
					return false;
					}
				}
			}
				//changed by nikhil for PCSP-489
			var new_value = 'DecisionHistory,IncomeDetails';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			//Deepak Code 24 Dec to save highest deligation on ELigibiltyAndProductInfo end.
			//++below code added by nikhil for PCAS-364 CR
				setNGValueCustom('Eligibility_Trigger','N');
			var counter_dec = getNGValue('reEligibility_CC_counter').split(';')[1];
			var counter_elig = getNGValue('reEligibility_CC_counter').split(';')[0];
			if(parseInt(counter_dec)<parseInt(alerts_String_Map['VAL323'])){
			setNGValueCustom('reEligibility_CC_counter',counter_elig+';'+(parseInt(counter_dec)+1));
			return true;		    
			}
			else{
			showAlert('DecisionHistory_calReElig','You have exhausted your attempts to Calculate Re-eligibility');
			return false;
			}
			
		}
		// Added for dectech call
		//Tanshu Aggarwal AddressDetailsSave 16/08/2017
		//commented by abhishek as per CC FSD
		/* if(pId=='AddressDetails_Save'){
			if(checkMandatory_AddressDetails_Save()){
				return true;
				}
		} */
		//Tanshu Aggarwal AddressDetailsSave 16/08/2017
		
		//Tanshu Aggarwal Address Add Mandatory Check
		/* if(pId=='AddressDetails_addr_Add')
        {   
			if(checkMandatory_AddressDetails_Add()){
				return true;
				}
			}
		if(pId=='AddressDetails_addr_Modify')
        {   
			if(checkMandatory_AddressDetails_Add()){
				return true;
				}
			}
		if(pId=='AddressDetails_addr_Delete')
        {   
			return true;
		}*/
		//Tanshu Aggarwal Address Add Mandatory Check
		
		
		
		//Tanshu Aggarwal Contact Details Check
		if(pId=='AltContactDetails_Saved'){
			if(checkMandatory_ContactDetails_save())
			return true;
		}
		//Tanshu Aggarwal Contact Details Check
		
		//Tanshu Aggarwal Card Details field visibility on security check
	if(pId=='cmplx_CardDetails_Security_Check_Held'){
		if(getNGValue("cmplx_CardDetails_Security_Check_Held")==true){
			setEnabledCustom('CardDetails_BankName',true);
			setEnabledCustom('CardDetails_ChequeNumber',true);
			setEnabledCustom('CardDetails_Amount',true);
			setLockedCustom('CardDetails_Date',false);
			
		}
		else{
			setNGValueCustom('CardDetails_BankName','');
			setNGValueCustom('CardDetails_ChequeNumber','');
			setNGValueCustom('CardDetails_Amount','');
			setNGValueCustom('CardDetails_Date','');
			setEnabledCustom('CardDetails_BankName',false);
			setEnabledCustom('CardDetails_ChequeNumber',false);
			setEnabledCustom('CardDetails_Amount',false);
			setLockedCustom('CardDetails_Date',true);
		}
	}
	//Tanshu Aggarwal Card Details field visibility on security check
		
	//Tanshu Aggarwal Card Details Mandatory Check(16/08/2017)
	//Add button of Card Details
	if(pId=='CardDetails_Button2'){
		if(checkMandatory_Add_CardDetails())
		{
		//for PCAS-3046
		var new_value = 'Card_Details';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
		return true;
		}
	}
	//Modify button of Card Details
	if(pId=='CardDetails_Button3'){
		if(checkMandatory_Add_CardDetails())
		{
		//for PCAS-3046
		var new_value = 'Card_Details';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			
		}
		return true;
		}
	}
	if(pId=='CardDetails_Button4'){
		return true;
	}
	//Tanshu Aggarwal Card Details mandatory Check(16/08/2017)	
		
		else if(pId=='DecisionHistory_Button1'){
			if(checkMandatory(CC_DECISION)){
				return true;
				}
         	}
	//Arun added (14-jun)
			else if (pId=='cmplx_Customer_CARDNOTAVAIL')
			{
				var CardNotavail=getNGValue("cmplx_Customer_CARDNOTAVAIL");
				var activityName = window.parent.stractivityName;
				if (activityName=='Fulfillment_RM' || activityName=='Telesales_RM')
				{
					if (CardNotavail==true)
					{
						setLockedCustom("cmplx_Customer_Age",true);							
						setLockedCustom("cmplx_Customer_IdIssueDate",true);
						setLockedCustom("cmplx_Customer_EmirateIDExpiry",true);
						setLockedCustom("cmplx_Customer_VisaNo",true);
						setLockedCustom("cmplx_Customer_VisaExpiry",true);
						setLockedCustom("cmplx_Customer_PassPortExpiry",true);
						setLockedCustom("cmplx_Customer_yearsInUAE",true);
						setLockedCustom("cmplx_Customer_MotherName",true);
						setLockedCustom("Customer_FetchDetails",true);
						setLockedCustom("Customer_ReadFromCard",true);
					}
					else
					{
						setLockedCustom("cmplx_Customer_Age",false);							
						setLockedCustom("cmplx_Customer_IdIssueDate",false);
						setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
						setLockedCustom("cmplx_Customer_VisaNo",false);
						setLockedCustom("cmplx_Customer_VisaExpiry",false);
						setLockedCustom("cmplx_Customer_PassPortExpiry",false);
						setLockedCustom("cmplx_Customer_yearsInUAE",false);
						setLockedCustom("cmplx_Customer_MotherName",false);
						setLockedCustom("FetchDetails",false);
					}
				}
			}  //Arun ended (14-jun)

		else if(pId=='cmplx_Customer_NEP'){
			var NEP=getNGValue("cmplx_Customer_NEP");
			if(NEP!=''){
					
					setEnabledCustom("ReadFromCard",false);
					//setEnabledCustom("Customer_FetchDetails",true);
					//setVisible("Customer_Frame2", true);
					//com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "850px");
					//com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "870px");
					//setEnabledCustom("Validate_OTP_Btn", false);
					//setLockedCustom("OTP_No", true);
					setLockedCustom("cmplx_Customer_CIFNo",false);
					setLockedCustom("cmplx_Customer_FirstNAme",false);
					setLockedCustom("cmplx_Customer_MiddleNAme",false);
					setLockedCustom("cmplx_Customer_LastNAme",false);
					setLockedCustom("cmplx_Customer_Nationality",false);
					setLockedCustom("cmplx_Customer_MobileNo",false);
					setLockedCustom("cmplx_Customer_DOb",false);
					setLockedCustom("cmplx_Customer_PAssportNo",false);
					setVisible("Customer_Label56",true);
					setVisible("cmplx_Customer_EIDARegNo",true);
					setLockedCustom("cmplx_Customer_CARDNOTAVAIL",true);
					setLockedCustom("cmplx_Customer_EIDARegNo",false);
					setNGValueCustom("cmplx_Customer_EmiratesID","");
					SetEnableCustomer();
					setBlank_Customer();
					setLockedCustom("cmplx_Customer_EmirateIDExpiry",true);
					setLockedCustom("cmplx_Customer_IdIssueDate",true);
					setNGValueCustom("cmplx_Customer_NTB",true);
					setLockedCustom("Customer_Button1",true);
				}
				else{
					
					//setEnabledCustom("ReadFromCard",true);
					setEnabledCustom("ReadFromCard",false);
					//setEnabledCustom("Customer_FetchDetails",true);
					setVisible("Customer_Frame2", false);
					//com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "850px");
					//com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "870px");
					setLockedCustom("cmplx_Customer_CIFNo",true);
					setLockedCustom("cmplx_Customer_FIrstNAme",true);
					setLockedCustom("cmplx_Customer_MiddleName",true);
					setLockedCustom("cmplx_Customer_LAstNAme",true);
					setLockedCustom("cmplx_Customer_Nationality",true);
					setLockedCustom("cmplx_Customer_MobileNo",true);
					setLockedCustom("cmplx_Customer_DOb",true);
					setLockedCustom("cmplx_Customer_PAssportNo",true);
					setVisible("Customer_Label56",false);
					setVisible("cmplx_Customer_EIDARegNo",false);
					setLockedCustom("cmplx_Customer_CARDNOTAVAIL",true);
					setLockedCustom("cmplx_Customer_EIDARegNo",true);
					setNGValueCustom("cmplx_Customer_NTB",false);
					SetDisableCustomer1();
					setBlank_Customer();
				}
		}
		else if(pId=='CC_Creation_CheckBox1')
	{
		return true;
	}
		else if(pId=='CompanyDetails_CheckBox4'){
			if(getNGValue(pId)==true){
				setLockedCustom('cif',false);
				if(activityName!='CSM'){
					setLockedCustom('CompanyDetails_Button3',false);
				}
				setLockedCustom('CompanyDetails_Aloc_search',true);
				setLockedCustom('appType',true);
				setLockedCustom('compIndus',true);
				setLockedCustom('tlNo',true);
				setLockedCustom('TLExpiry',true);
				setLockedCustom('CompanyDetails_TradeLicencePlace',true);
				setLockedCustom('CompanyDetails_DatePicker1',true);
				setLockedCustom('indusSector',true);
				setLockedCustom('indusMAcro',true);
				setLockedCustom('indusMicro',true);
				setLockedCustom('desig',true);
				setLockedCustom('desigVisa',true);
				setLockedCustom('lob',true);
				setLockedCustom('legalEntity',true);
				//setLockedCustom('estbDate',true);
				setLockedCustom('CompanyDetails_highdelin',true);
				setLockedCustom('CompanyDetails_currempcateg',true);
				setLockedCustom('CompanyDetails_categcards',true);
				setLockedCustom('CompanyDetails_categexpat',true);
				setLockedCustom('CompanyDetails_categnational',true);
				//setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_BVR',true);
				setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_POA',true);
				setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_ExportToIran',true);
				setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_Grt40BussMob',true);
				setNGValueCustom('indusSector','');
				setNGValueCustom('indusMAcro','');
				setNGValueCustom('indusMicro','');
				
				setLockedCustom('compName',true);
				
				
				
			}
			else{
				setLockedCustom('cif',true);
				setLockedCustom('CompanyDetails_Button3',true);
				setLockedCustom('CompanyDetails_Aloc_search',false);
				setLockedCustom('compName',false);
				setLockedCustom('appType',false);
				setLockedCustom('compIndus',false);
				setLockedCustom('tlNo',false);
				setLockedCustom('TLExpiry',false);
				setLockedCustom('CompanyDetails_TradeLicencePlace',false);
				setLockedCustom('CompanyDetails_DatePicker1',false);
				setLockedCustom('indusSector',false);
				setLockedCustom('indusMAcro',false);
				setLockedCustom('indusMicro',false);
				setLockedCustom('desig',false);
				setLockedCustom('desigVisa',false);
				setLockedCustom('lob',false);
				setEnabledCustom('lob',true);//sagarika for proc 1082
				setLockedCustom('legalEntity',false);
				setLockedCustom('estbDate',false);
				setLockedCustom('CompanyDetails_highdelin',false);
				setLockedCustom('CompanyDetails_currempcateg',false);
				setLockedCustom('CompanyDetails_categcards',false);
				setLockedCustom('CompanyDetails_categexpat',false);
				setLockedCustom('CompanyDetails_categnational',false);
				//setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_BVR',false);
				setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_POA',false);
				setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_ExportToIran',false);
				setLockedCustom('cmplx_CompanyDetails_cmplx_CompanyGrid_Grt40BussMob',false);
				// ++ above code already present - 06-10-2017 company detail value change 
				
				
			}
		}
		
		else if(pId=='CompanyDetails_Button3')
		{
			if(getNGValue('cmplx_CompanyDetails_cmplx_CompanyGrid_CompanyCIF')==''){
				showAlert('cmplx_CompanyDetails_cmplx_CompanyGrid_CompanyCIF',alerts_String_Map['CC039']);
				return false;
			}	
			flag_Company_FetchDetails = true;
			return true;
		}
		
		else if(pId=='EMploymentDetails_Save'){
		if(getNGValue('cmplx_Customer_VIPFlag')==false)
		{
		var Principal_Approval=getNGValue('Is_Principal_Approval');
	var Principal_Approval=getNGValue('Is_Principal_Approval');
		if(Principal_Approval=='Y' && activityName=='CSM')
		{
			if(getNGValue('cmplx_EmploymentDetails_EmpIndusSector')=='' || getNGValue('cmplx_EmploymentDetails_EmpIndusSector')=='--Select--')
		{
			showAlert('cmplx_EmploymentDetails_EmpIndusSector',alerts_String_Map['CAS004']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='' || getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='--Select--')
		{
			showAlert('cmplx_EmploymentDetails_ApplicationCateg',alerts_String_Map['CAS024']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_Indus_Micro')=='')
		{
			showAlert('cmplx_EmploymentDetails_Indus_Micro',alerts_String_Map['CAS023']);
			return false;
		}
		//Added by shivang for PCASP-2440
		else if(getNGValue('cmplx_EmploymentDetails_DOJ')=='' && getNGValue('cmplx_Customer_VIPFlag')==false && getNGValue("Sub_Product")!='SEC')
		{
			showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['CAS022']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_LengthOfBusiness')=='' && getNGValue('cmplx_Customer_VIPFlag')==false)
		{
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SAL' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM'){
				if((!getNGValue('cmplx_EmploymentDetails_IncInCC') && !getNGValue('cmplx_EmploymentDetails_IncInPL'))
					||(getNGValue('cmplx_EmploymentDetails_CurrEmployer')=='CN' && (getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='OP'||getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='RS') 
					&& (getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='CN'||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')==''||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='--Select--'))
					||(getNGValue('cmplx_EmploymentDetails_CurrEmployer')=='CN' &&((getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='NM'   ||getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='CN'||getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='')&& 
					(getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='NM' || getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='CN'||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')==''||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='--Select--')))){
						
						showAlert('cmplx_EmploymentDetails_LengthOfBusiness',alerts_String_Map['CAS021']);
						return false;
					}
				}
			
		}
		else if(getNGValue('cmplx_EmploymentDetails_Indus_Macro')=='')
		{
			showAlert('cmplx_EmploymentDetails_Indus_Macro',alerts_String_Map['CAS020']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='EMPID' && getNGValue('cmplx_EmploymentDetails_IndusSeg')=='')
		{
			showAlert('cmplx_EmploymentDetails_IndusSeg',alerts_String_Map['CAS019']);
			return false;
			
			} 
			//Deepak 06July2020 removing below condition as the check need to be applied only at CA stage.
			/*else if((getNGValue('cmplx_EmploymentDetails_Designation')=='OTHER' || getNGValue('cmplx_EmploymentDetails_Designation')=='41')
			&& (getNGValue('cmplx_EmploymentDetails_OtherDesign')==null || getNGValue('cmplx_EmploymentDetails_OtherDesign')=='') && (getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SAL')){			
					showAlert('cmplx_EmploymentDetails_OtherDesign','Please Enter Other Designation (Income Document)');
					return false;
			}//pcasp-2348
			*/
		else
		{
			return true;
		}
		}
		var frameNameSec=CC_EMPLOYMENTDETAILS;
		var EmpType=getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6);
		if(getNGValue("Sub_Product")=="SEC"){
			frameNameSec=CC_EMPLOYMENTDETAILS_SEC;
		}
			if(checkMandatory(frameNameSec)){
				if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='' || getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='--Select--'){
						showAlert('cmplx_EmploymentDetails_ApplicationCateg',alerts_String_Map['CAS024']);
						return false;
		            }
				if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BPA' &&  getNGValue('cmplx_EmploymentDetails_targetSegCode')=='')
				{
					showAlert('cmplx_EmploymentDetails_targetSegCode',"Target Segment cannot be blank");
					return false;
				}
				if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BPA' &&  getNGValue('cmplx_EmploymentDetails_ClassificationCode')=='')
				{
					showAlert('cmplx_EmploymentDetails_ClassificationCode',"Classification cannot be blank");
					return false;
				}//PCASP-2479
				if((getNGValue('cmplx_EmploymentDetails_marketcode')=='' || getNGValue('cmplx_EmploymentDetails_marketcode')=='--Select--') && ((getNGValue("Sub_Product")=="BPA" || getNGValue("Sub_Product")=="SEC") && EmpType=="Salaried")){
						showAlert('cmplx_EmploymentDetails_marketcode',"Marketing Code cannot be blank");
						return false;
		            }
					if((getNGValue('cmplx_EmploymentDetails_EmpStatus')=='' || getNGValue('cmplx_EmploymentDetails_EmpStatus')=='--Select--') && ((getNGValue("Sub_Product")=="BPA" || getNGValue("Sub_Product")=="SEC") && EmpType=="Salaried")){
						showAlert('cmplx_EmploymentDetails_EmpStatus',"Employment Status cannot be blank");
						return false;
		            }
					if((getNGValue('cmplx_EmploymentDetails_Emp_Type')=='' || getNGValue('cmplx_EmploymentDetails_Emp_Type')=='--Select--') && ((getNGValue("Sub_Product")=="BPA" || getNGValue("Sub_Product")=="SEC") && EmpType=="Salaried")){
						showAlert('cmplx_EmploymentDetails_Emp_Type',"Employment Type cannot be blank");
						return false;
		            }
					if((getNGValue('cmplx_EmploymentDetails_FieldVisitDone')=='' || getNGValue('cmplx_EmploymentDetails_FieldVisitDone')=='--Select--') && (getNGValue("Sub_Product")=="IM"))
					{
		            	showAlert('cmplx_EmploymentDetails_FieldVisitDone',"Please fill fields visit done.");
		            	return false;
		            } //hritik ppct38
					if((getNGValue('cmplx_EmploymentDetails_Designation')=='' || getNGValue('cmplx_EmploymentDetails_Designation')=='--Select--') && ((getNGValue("Sub_Product")=="BPA" || getNGValue("Sub_Product")=="SEC") && EmpType=="Salaried")){
						showAlert('cmplx_EmploymentDetails_Designation',"Designation cannot be blank");
						return false;
		            }
				if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,5)=="EKTMC-EXPAT" ||getLVWAT('cmplx_Product_cmplx_ProductGrid',0,5)=="EKWEI-EXPAT"||getLVWAT('cmplx_Product_cmplx_ProductGrid',0,5)=="EKTMC-UAE"||getLVWAT('cmplx_Product_cmplx_ProductGrid',0,5)=="EKWEC-EXPAT" ||getLVWAT('cmplx_Product_cmplx_ProductGrid',0,5)=="EKWEC-UAE")&& getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='S' && getNGValue("cmplx_EmploymentDetails_targetSegCode")=='AMS')
				{
					if(getNGValue('cmplx_EmploymentDetails_tierno')=="--Select--" ||getNGValue('cmplx_EmploymentDetails_tierno')=="")
					{
					showAlert('cmplx_EmploymentDetails_tierno',"Tier Number cannot be blank");
                  return false;					
					}
				}
					//Added by Rajan for IM FLV   and also for PCASP-1378
					if(activityName!='DDVT_Maker')
				{
				if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM' && getNGValue("Sub_Product")!='SEC' && (getNGValue('cmplx_EmploymentDetails_EmployerType')=='' ||getNGValue('cmplx_EmploymentDetails_EmployerType')=='--Select--'  ) )
				{	
					showAlert('cmplx_EmploymentDetails_EmployerType','Employer Type is mandatory');
					return false;
				}
				if((getNGValue('cmplx_EmploymentDetails_EmirateOfWork')=='' ||getNGValue('cmplx_EmploymentDetails_EmirateOfWork')=='--Select--') &&  getNGValue("Sub_Product")!='SEC') 
				{	
					showAlert('cmplx_EmploymentDetails_EmirateOfWork','Emirate of work is mandatory');
					return false;
				}
				if ((getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='' ||getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='--Select--' ) && isVisible('cmplx_EmploymentDetails_HeadOfficeEmirate') ) 
				{	
					showAlert('cmplx_EmploymentDetails_HeadOfficeEmirate','Head Office emirate is mandatory');
					return false;
				}
				
			}
					// Below added by Rajan for PCASP-589
					if(activityName=='DDVT_Maker' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SAL')
					{
					if(getNGValue('cmplx_EmploymentDetails_VisaSponser')=='')
					{	
					showAlert('cmplx_EmploymentDetails_VisaSponser','Visa Sponser is mandatory');
					return false;
					}
					if((getNGValue('cmplx_EmploymentDetails_EmirateOfWork')=='' ||getNGValue('cmplx_EmploymentDetails_EmirateOfWork')=='--Select--') &&  getNGValue("Sub_Product")!='SEC')  
					{	
					showAlert('cmplx_EmploymentDetails_EmirateOfWork','Emirate of work is mandatory');
					return false;
					}
					/*if ((getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='' ||getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='--Select--' ) ) 
				{	
			showAlert('cmplx_EmploymentDetails_HeadOfficeEmirate','Head Office emirate is mandatory');
			return false;
					}*/
					
				}
				
				if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='LIFSUR')
			{
			if(getNGValue('cmplx_EmploymentDetails_InsuranceValue')=='')
			{
			showAlert('cmplx_EmploymentDetails_InsuranceValue',alerts_String_Map['CC284']);
			return false;
			}
			if(getNGValue('cmplx_EmploymentDetails_PremAmt')=='')
			{
			showAlert('cmplx_EmploymentDetails_PremAmt',alerts_String_Map['CC287']);
			return false;
			}
			
			if(getNGValue('cmplx_EmploymentDetails_PremPaid')=='')
			{
			showAlert('cmplx_EmploymentDetails_PremPaid',alerts_String_Map['CC285']);
			return false;
			}
			if(getNGValue('cmplx_EmploymentDetails_PremType')=='--Select--' || getNGValue('cmplx_EmploymentDetails_PremType')=='')//PCAS-1865
			{
			showAlert('cmplx_EmploymentDetails_PremType',alerts_String_Map['CC286']);
			return false;
			}
		/*	if(getNGVlaue('cmplx_EmploymentDetails_RegPay')=='false')
			{showAlert('cmplx_EmploymentDetails_RegPay',alerts_String_Map['CC287']);
		return false;
		}
			if(getNGValue('cmplx_EmploymentDetails_Withinminwait')=='')
			{showAlert('cmplx_EmploymentDetails_Withinminwait',alerts_String_Map['CC288']);
		return false;
		}*/
			}
			
			if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='MOTSUR')
			{
			if(getNGValue('cmplx_EmploymentDetails_MotorInsurance')=='')
			{showAlert('cmplx_EmploymentDetails_MotorInsurance',alerts_String_Map['CC289']);
		return false;}
	
			}
				if(getNGValue('cmplx_EmploymentDetails_EMpCode')== '' && activityName!='SalesCoordinator')
				{
					showAlert('cmplx_EmploymentDetails_EMpCode',alerts_String_Map['VAL332']);
					return false;
				}//Arun (11/12/17)
				//Added by Shivang for PCASP-2040
				else if(getNGValue('cmplx_EmploymentDetails_DOJ')=='' && getNGValue('EmploymentType').indexOf("Pensioner")==-1 && getNGValue('cmplx_Customer_VIPFlag')==false && getNGValue("Sub_Product")!='SEC')
				{
					showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['PL411']);
					return false;	
				}
				//below code added by Deepak as the same removed without comment.
				else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='CAC' && getNGValue('cmplx_EmploymentDetails_OtherBankCAC')=='')
				{	//below changed for Wrong alert
					showAlert('cmplx_EmploymentDetails_OtherBankCAC',alerts_String_Map['VAL374']);
					return false;	
				}
				else if(getNGValue('cmplx_EmploymentDetails_LOSPrevious')=='' && getNGValue('cmplx_EmploymentDetails_NepType')=='NEWJ'){
					showAlert('cmplx_EmploymentDetails_LOSPrevious',alerts_String_Map['VAL337']);
					return false;
				}	
				else if(getNGValue('Application_Type')=='RESC' || getNGValue('Application_Type')=='RESN' || getNGValue('Application_Type')=='RESR'){
					if(getNGValue('cmplx_EmploymentDetails_channelcode')=='--Select--'){
						showAlert('cmplx_EmploymentDetails_channelcode',alerts_String_Map['VAL253']);
						return false;
					}
				}
				//changes by nikhil for PCAS-2416
				//change by saurabh for PPG 10June 19.getNGValue('cmplx_EmploymentDetails_IncInCC')!=true && getNGValue('cmplx_EmploymentDetails_IncInPL')!=true && (getNGValue('cmplx_EmploymentDetails_IndusSeg')=='D' || getNGValue('cmplx_EmploymentDetails_IndusSeg')=='J') && 
				//Changes done by prabhakar 24-08-2019 jira no 2483
				else if(getNGValue('cmplx_EmploymentDetails_LengthOfBusiness')=='' && getNGValue('cmplx_Customer_VIPFlag')==false){
				if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SAL' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM'){
				if((!getNGValue('cmplx_EmploymentDetails_IncInCC') && !getNGValue('cmplx_EmploymentDetails_IncInPL'))
					||(getNGValue('cmplx_EmploymentDetails_CurrEmployer')=='CN' && (getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='OP'||getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='RS') 
					&& (getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='CN'||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')==''||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='--Select--'))
					||(getNGValue('cmplx_EmploymentDetails_CurrEmployer')=='CN' &&((getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='NM'   ||getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='CN'||getNGValue('cmplx_EmploymentDetails_EmpStatusPL')=='')&& 
					(getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='NM' || getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='CN'||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')==''||getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='--Select--')))){
						
						showAlert('cmplx_EmploymentDetails_LengthOfBusiness',alerts_String_Map['PL293']);
						return false;
					}
				}
				
				}
				//added by nikhil PCSP-124
				//added by nikhil for PCSP-172 changed 
				if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='EMPID' && (getNGValue('cmplx_EmploymentDetails_IndusSeg')=='' || getNGValue('cmplx_EmploymentDetails_IndusSeg')=='--Select--') )
				{
					showAlert('cmplx_EmploymentDetails_IndusSeg',alerts_String_Map['CC276']);
					return false;
				}
			/*	//Changed by shivang as per Nep change requested by simi
				//Deepak further changes for remove this validation from all the stages.
				else if((getNGValue('cmplx_EmploymentDetails_targetSegCode')=='NEPNAL' || getNGValue('cmplx_EmploymentDetails_targetSegCode')=='NEPALO') && (getNGValue('cmplx_EmploymentDetails_NepType')=='--Select--' || getNGValue('cmplx_EmploymentDetails_NepType')==''))//Jira-2606
				{
					showAlert('cmplx_EmploymentDetails_NepType',alerts_String_Map['VAL392']);
					return false;
				}sagarika for NEP change production*/
				else if((getNGValue('cmplx_EmploymentDetails_Designation')=='OTHER' || getNGValue('cmplx_EmploymentDetails_Designation')=='41') &&
				(getNGValue('cmplx_EmploymentDetails_OtherDesign')==null || getNGValue('cmplx_EmploymentDetails_OtherDesign')=='') && (getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SAL') && activityName=='CAD_Analyst1'){//PCASP-2348 sagarika //Deepak 06July2020 additional condition of CA stage has been applied for production issue
					showAlert('cmplx_EmploymentDetails_OtherDesign','Please Enter Other Designation (Income Document)');
					return false;
				}//pcasp-2348
			//commented by shivang for falcon BAU testing
			/*if(getNGValue('cmplx_Customer_Nationality')!='AE'  && (getNGValue('cmplx_EmploymentDetails_VisaSponser')=='' || getNGValue('cmplx_EmploymentDetails_VisaSponser')==' '))
			{
				showAlert('cmplx_EmploymentDetails_VisaSponser',alerts_String_Map['VAL291']);
					return false;
			}*/
			//change by saurabh on 30th Nov 17
				setNGValueCustom('IS_Employment_Saved','Y');
					return true;
			}
         }
		 else
		 {
		 setNGValueCustom('IS_Employment_Saved','Y');
		 return true;
		 }
		 }
			
		/*else if(pId=='ExtLiability_Button1'){
		
			if(AccountSummary_checkMandatory())
			{				
				var request_name = "";
				var param_json="";
				var url="";
				var prod = "";
				var subprod="";
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				var parentWiName = getNGValue('Parent_WIName');
				
				var ajaxReq;
				var dataFromAjax;
				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}
				
				prod = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,1);
				subprod = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
				
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					request_name = 'InternalExposure,ExternalExposure';
				}
				else
					request_name = 'ExternalExposure';
				
				param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_customer_FirstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LAstNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_gender":"'+getNGValue('cmplx_Customer_gender')+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_Passport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PAssport4')+'","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_PAssport4')+'"}');
								
				url='/formviewer/resources/scripts/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod + '&parentWiName=' +parentWiName;
				//alert('url 1 ' + url);
				window.open(url, "", "width=500,height=300");
				
				
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					request_name = 'CollectionsSummary';
				}
				param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_customer_FirstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LAstNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_gender":"'+getNGValue('cmplx_Customer_gender')+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_Passport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PAssport4')+'","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_PAssport4')+'"}');
								
				url='/formviewer/resources/scripts/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod + '&parentWiName=' +parentWiName;
				
				//alert('url 2 ' + url);
				window.open(url, "", "width=500,height=300");
				
				
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,0)=='Islamic')
				{
					request_name = 'ACCOUNT_SUMMARY';
					param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNo')+'"}');
					url='/formviewer/resources/scripts/integration_AccountPL.jsp?request_name=' + request_name + '&parentWiName=' + parentWiName + '&wi_name=' + wi_name + '&activityName=' +activityName + '&param_json=' + param_json;
					//alert('url 3 ' + url);
					window.open(url, "", "width=500,height=300");
				}				
				
				request_name = 'CARD_SUMMARY';
				
				param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNo')+'"}');
					url='/formviewer/resources/scripts/integration_CardPL.jsp?request_name=' + request_name + '&parentWiName=' + parentWiName + '&wi_name=' + wi_name + '&activityName=' +activityName + '&param_json=' + param_json;
					
					//alert('url 4 ' + url);
				window.open(url, "", "width=500,height=300");
				
			return true;
			}
		}*/
	   /*else if(pId=='AuthorisedSignDetails_Button1'){
				return true;
			}*/
			
			//tanshu Aggarwal(17/08/2017) for reference details
				/*if(pId=='Reference_Details_Reference_Add'){
					if(checkMandatory_RefDetailsGrid()){
						var n=getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
						if(n>1){
							showAlert('cmplx_RefDetails_cmplx_Gr_ReferenceDetails',alerts_String_Map['CC025']);
							return false;
						}
					return true;
				}
			}*/
					
	if(pId=='Reference_Details_Reference__modify'){
		if(checkMandatory_RefDetailsGrid())
			return true;
	}
				
	if(pId=='Reference_Details_Reference_delete')
			return true;
		
	if(pId=='Reference_Details_save'){
		if(checkMandatory_Reference_Details_Save())
		{
				removeFrame(pId);
				return true;}
		}
	if(pId=='CardDetails_Save'){
		if(checkMandatory_CardDetails_Save())
		{
				removeFrame(pId);
				return true;}
	}	
			//Tanshu Aggarwal(17/08/2017) for Refernece Details
		if(pId=='ExtLiability_Button1'){
			//Deepak Code change to save AECB provider no start.
			var new_value = 'ExtLiability_Frame1';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			//Deepak Code change to save AECB provider no END.
			if(AccountSummary_checkMandatory())
			{
				if(activityName== 'DDVT_Maker'){
					setNGFrameState('IncomeDetails',2);
				}
				setNGValueCustom('IS_AECB','Y');
				//Prabhakar jira no-1231 26-08-2019
				setNGValueCustom('Liability_New_overwrite_flag','N');
				//++below code added by nikhil for PCAS-364 CR
				setNGValueCustom('Eligibility_Trigger','Y');
				//--above code added by nikhil for PCAS-364 CR
				var request_name = "";
				//changes done for double spacing nikhil 28th JAN
				var middle_name="";
				if(getNGValue('cmplx_Customer_MiddleNAme')!='')
				{
					middle_name=' '+getNGValue('cmplx_Customer_MiddleNAme');
				}
				var full_name = getNGValue('cmplx_Customer_FirstNAme')+middle_name+' '+getNGValue('cmplx_Customer_LastNAme');
				var TxnAmount=replaceAll(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,3),',','');//.replace(',','');
				var NoOfInstallments=replaceAll(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,7),',','');
				var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
				var subprod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
				var gender = getNGValue('cmplx_Customer_gender');
				var parentWiName = getNGValue('parent_WIName');
				var OverridePeriod="0";
				if(gender=='F'){
					gender='1';
				}
				else if(gender=='M'){
					gender='2';
				}
				else{
					gender='3';
				}
				
				
				if(getNGValue('cmplx_Liability_New_overrideAECB')==true){
					OverridePeriod='1';
				}
				//changes done by Deepak to make provider no unique
				var curr_date = new Date();
				var wi_name = window.parent.pid;
				var wi_name_aecb = wi_name.substring(3,13)+''+curr_date.getFullYear()+''+("0" + (curr_date.getMonth() + 1)).slice(-2)+''+("0" + curr_date.getDate()).slice(-2)+''+("0" + curr_date.getHours()).slice(-2)+''+("0" + curr_date.getMinutes()).slice(-2)+''+("0" + curr_date.getMilliseconds()).slice(-3);
				var activityName = window.parent.stractivityName;
				//Changes done for company AECB start
				var comp_row=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
				var company_cif="";
				var trade_comp_name="";
				var trade_lic_no="";
				var trade_lic_place="";
				var cifToBePassed = "";
					for(var i=0; i<comp_row;i++){
						if(com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,2)!="Primary"){
						if (company_cif==""){
							company_cif=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,3);
							trade_comp_name=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,4);
							trade_lic_no=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,6);
							trade_lic_place=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,7);
						}
						else{
							company_cif=company_cif+","+com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,3);
							trade_comp_name=trade_comp_name+","+com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,4);
							trade_lic_no=trade_lic_no=trade_lic_no+","+com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,6);
							trade_lic_place=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,7);
						}
						}
					}
					if(getNGValue('cmplx_Customer_VIPFlag')==true){
						if(getNGValue('cmplx_Customer_NTB')==true){
							setNGValueCustom('aecb_call_status','true');
							showAlert('ExtLiability_Button1',alerts_String_Map['CAS028']);
							return false;
						}
						else{
							request_name = 'InternalExposure';
						}
						
					}
					else if((getNGValue('cmplx_Customer_NTB')==false) || (getNGValue('cmplx_Customer_NTB')==true && company_cif!='' && company_cif!=null))
					{
						request_name = 'InternalExposure,ExternalExposure';
					}
					else{
						request_name = 'ExternalExposure';
					}
					if(getNGValue('cmplx_Customer_NTB')==true){
						cifToBePassed = "";	
					}
					else{
						cifToBePassed = getNGValue('cmplx_Customer_CIFNo');	
					}
					var param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_Customer_FirstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LastNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_MobNo":"'+getNGValue('cmplx_Customer_MobileNo')+'","cmplx_Customer_gender":"'+gender+'","OverridePeriod":"'+OverridePeriod+'","wi_name":"'+wi_name_aecb+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","TxnAmount":"'+TxnAmount+'","NoOfInstallments":"'+NoOfInstallments+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_PAssport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PASSPORT4')+'","cmplx_Username":"deepak","cmplx_Customer_CIFNO":"'+cifToBePassed+'","cif":"'+company_cif+'","trade_comp_name":"'+trade_comp_name+'","trade_lic_no":"'+trade_lic_no+'","trade_lic_place":"'+trade_lic_place+'","account_no":"B01","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_EmiratesID')+'","cmplx_EligibilityAndProductInfo_NumberOfInstallment":"5","cmplx_Customer_short_name":"'+getNGValue('cmplx_Customer_short_name')+'","full_name":"'+full_name+'","cmplx_Customer_NTB":"'+getNGValue('cmplx_Customer_NTB')+'","Individual_Aecb":"'+getNGValue('Individual_Aecb')+'","Corporate_Aecb":"'+getNGValue('Corporate_Aecb')+'"}');
				
				
				param_json = encodeURIComponent(param_json);
				var url='/webdesktop/custom/CustomJSP/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod  +'&parentWiName=' + parentWiName+'&sessionId='+window.parent.sessionId;
				
				//Deepak new code added to open single window start
				if(popup_win && !popup_win.closed){
					popup_win.focus();
				}
				else{
					popup_win = window.open_(url, "", "width=500,height=500"); //changed by aman to send the code to java
				}
				//Deepak new code added to open single window END
				
				//window.showModalDialog(url,"","dialogHeight:300px;dialogWidth:500PX;");
				//window.open(url,"","dialogHeight:300px;dialogWidth:500PX;");
				//code modified by nikhil to save latest aecb url
				//var status;
				//var returnValue=window.open_(url,'', "width=650px");
				//alert(returnValue);
				//uncommented by saurabh on 21/12
				if(typeof returnValue==='object'){
					var ReportURL=returnValue['ReportUrl'];
					setNGValueCustom('cmplx_Liability_New_Aecb_Report_Url',ReportURL);
					 status=returnValue['aecb_call_status'];
				}
				else{
					status=returnValue;
				}
				setNGValueCustom('aecb_call_status',status);
				//Deepak 21-July-2018 new changes done in integration interface.
				/*
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					var url='/formviewer/resources/scripts/integration_PL.jsp?request_name=CollectionsSummary&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod +'&parentWiName=' + parentWiName  ;
					window.showModalDialog(url, "", "width=500,height=300");
					//document.getElementById('Liability_New_IFrame_internal').src='/formviewer/resources/scripts/internal_liability.jsp';
				}
				
				//cardnumber 12/07/2017
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					var url='/formviewer/resources/scripts/integration_PL.jsp?request_name=CARD_INSTALLMENT_DETAILS&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod +'&parentWiName=' + parentWiName  ;
					window.showModalDialog(url, "", "width=500,height=300");
					//document.getElementById('Liability_New_IFrame_internal').src='/formviewer/resources/scripts/internal_liability.jsp';
				}
				
				document.getElementById('ExtLiability_IFrame_internal').src='/formviewer/resources/scripts/internal_liability.jsp';
				document.getElementById('ExtLiability_IFrame_external').src='/formviewer/resources/scripts/External_Liability.jsp';
				//setVisible("Liability_New_Overwrite",true);
			    return true;
				*/
			}
		}
		
		
		//modified by akshay on 12/1/18
		 if (pId =='ExtLiability_Frame2' && frameState==0)
		 {
			//document.getElementById("ExtLiability_IFrame_internal").style.top="30px";
			com.newgen.omniforms.formviewer.setIFrameSrc('ExtLiability_IFrame_internal','/webdesktop/custom/CustomJSP/internal_liability.jsp');
		 } 
		 
		 if (pId =='ExtLiability_Frame3' && frameState==0)
		 {
			//document.getElementById("ExtLiability_IFrame_external").style.top="30px";
			com.newgen.omniforms.formviewer.setIFrameSrc('ExtLiability_IFrame_external','/webdesktop/custom/CustomJSP/External_Liability.jsp');//dynamic load of jsp from code
		 } 
		 
		 if (pId =='ExtLiability_Frame5' && frameState==0)
		 {
			//document.getElementById("ExtLiability_IFrame_pipeline").style.top="30px";
			com.newgen.omniforms.formviewer.setIFrameSrc('ExtLiability_IFrame_pipeline','/webdesktop/custom/CustomJSP/Pipeline.jsp');//dynamic load of jsp

		 }
		 
		 if (pId =='ELigibiltyAndProductInfo_Frame5' && frameState==0)//CC
		 {
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame1','/webdesktop/custom/CustomJSP/Eligibility_Card_Product.jsp');
		 }
		 
		 if (pId =='ELigibiltyAndProductInfo_Frame6' && frameState==0)//Eligible for Card
		 {
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame2','/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp');
		 }
		 
		
		
		//Below code commented by Deepak to run Exposure at CAD
		/*	
		else if(pId=='ExtLiability_Button1'){
		

			if(AccountSummary_checkMandatory())
			{
				setNGValueCustom('IS_AECB','Y');
				var request_name = "";
				var param_json="";
				var url="";
				var prod = "";
				var subprod="";
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				var parentWiName = getNGValue('parent_WIName');
				
				var ajaxReq;
				var dataFromAjax;
				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}
				
				prod = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,1);
				subprod = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
				var gender = getNGValue('cmplx_Customer_gender');
				var request_name = "";
				
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					request_name = 'InternalExposure,ExternalExposure';
				}
				else{
					request_name = 'ExternalExposure';
				}
				if(gender=='F'){
					gender='1';
				}
				else if(gender=='M'){
					gender='2';
				}
				else{
					gender='3';
				}
				
				var param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_Customer_FIrstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LAstNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_gender":"'+gender+'","wi_name":"'+wi_name.substring(5,14)+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_Passport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PAssport4')+'","cmplx_Username":"deepak","cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNo')+'","acc_id":"'+getNGValue('acc_id')+'","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_EmiratesID')+'","cmplx_EligibilityAndProductInfo_NumberOfInstallment":"5","cmplx_Customer_short_name":"'+getNGValue('cmplx_Customer_short_name')+'"}');
				
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				
				var url='/formviewer/resources/scripts/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod +'&parentWiName=' + parentWiName;
				
				//window.open(url, "", "width=500,height=300");
				window.showModalDialog(url,"","dialogHeight:300px;dialogWidth:500PX;");
				//window.open(url,"","dialogHeight:300px;dialogWidth:500PX;");
				document.getElementById('ExtLiability_IFrame_internal').src='/formviewer/resources/scripts/internal_liability.jsp';
				//window.open(url,"","dialogHeight:300px;dialogWidth:500PX;");

				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					var url='/formviewer/resources/scripts/integration_PL.jsp?request_name=CollectionsSummary&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod +'&parentWiName=' + parentWiName;
					window.open(url, "", "width=500,height=300");
				}
				setVisible("Liability_New_Overwrite",true);
			    return true;

			}
		}
		*/
		else if(pId=='Liability_New_Overwrite')
		{
				var request_name = "";
				var param_json="";
				var url="";
				var prod = "";
				var subprod="";
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				var parentWiName = getNGValue('parent_WIName');
				
			if(getNGValue('IS_AECB')=='Y')		
			{
				
				//++below code added by nikhil for PCAS-364 CR
				//Prabhakar jira no-1231 16-08-2019
				setNGValueCustom('Liability_New_overwrite_flag','Y');
				setNGValueCustom('Eligibility_Trigger','Y');
				//--above code added by nikhil for PCAS-364 CR
				
					request_name = 'ACCOUNT_SUMMARY';
					param_json=('{"cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNo')+'"}');
					url='/webdesktop/custom/CustomJSP/integration_AccountPL.jsp?request_name=' + request_name + '&parentWiName=' + parentWiName + '&wi_name=' + wi_name + '&activityName=' +activityName + '&param_json=' + param_json+'&sessionId='+window.parent.sessionId;
					//alert('url 3 ' + url);
					window.open_(url, "ACCOUNT_SUMMARY", "width=650px,height=650px");
				
				request_name = 'CARD_SUMMARY';			
				param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNo')+'"}');
				url='/webdesktop/custom/CustomJSP/integration_CardPL.jsp?request_name=' + request_name + '&parentWiName=' + parentWiName + '&wi_name=' + wi_name + '&activityName=' +activityName + '&param_json=' + param_json+'&sessionId='+window.parent.sessionId;


				window.open_(url, "CARD_SUMMARY", "width=650px,height=650px");
				document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';
				//change by saurabh on 22nd Dec
				//setNGValueCustom('Is_Overwrite_Details','Y');
				//return true;
			}
			else				
			{
				showAlert('ExtLiability_Button1',alerts_String_Map['CC159']);
				return false;
			}
			
		}
		
		else if(pId=='CustDetailVerification_Button1'){
		if(getNGValue('cmplx_CustDetailVerification_Decision') =='' || getNGValue('cmplx_CustDetailVerification_Decision') =='--Select--')
		{
			showAlert('cmplx_CustDetailVerification_Decision','Customer Verification Decision is blank!');
			return false;
		}
			//++Below code added by nikhil 13/11/2017 for Code merge
		if(getNGValue('cmplx_CustDetailVerification_Decision')=='Positive' || getNGValue('cmplx_CustDetailVerification_Decision')=='Approve Sub to CIF' )//changed by akshay on 25/6/18 for proc 9815
		{
			if(!checkMandatory(CC_Custdetail_CPV))
			{
				return false;
				}
				//--Above code added by nikhil 13/11/2017 for Code merge
			//++ below code added by abhishek as per CC FSD 2.7.3
			var ver="cmplx_CustDetailVerification_mobno1_ver:cmplx_CustDetailVerification_mobno2_ver:cmplx_CustDetailVerification_dob_verification:cmplx_CustDetailVerification_POBoxno_ver:cmplx_CustDetailVerification_emirates_ver:cmplx_CustDetailVerification_persorcompPOBox_ver:cmplx_CustDetailVerification_offtelno_ver:cmplx_CustDetailVerification_email1_ver:cmplx_CustDetailVerification_email2_ver:cmplx_CustDetailVerification_Mother_name_ver";
			var update="cmplx_CustDetailVerification_mobno1_upd:cmplx_CustDetailVerification_mobno2_upd:cmplx_CustDetailVerification_dob_upd:cmplx_CustDetailVerification_POBoxno_upd:cmplx_CustDetailVerification_emirates_upd:cmplx_CustDetailVerification_persorcompPOBox_upd:cmplx_CustDetailVerification_offtelno_upd:cmplx_CustDetailVerification_email1_upd:cmplx_CustDetailVerification_email2_upd:cmplx_CustDetailVerification_Mother_name_upd";
			var name="Mobile No. 1:Mobile No. 2:Date of Birth:P.O Box No.:Emirates:Personal / Company P.O Box No.:Office Telephone No.:Email Address 1:Email Address 2: Mother's Name";
			if(CheckMandatory_Verification(ver,update,name)){
				return true;
				}
//-- Above code added by abhishek as per CC FSD 2.7.3 
		}
		else{
		return true;

		}
		}
		//++below code added by nikhil for PCAS-364 CR
		else if(pId=='cmplx_EmploymentDetails_JobConfirmed' || pId=='cmplx_EmploymentDetails_Freezone' || pId=='FinacleCore_CheckBox1' || pId=='cmplx_EmploymentDetails_RegPayment' || pId=='cmplx_EmploymentDetails_MinimumWait')
		{
			setNGValueCustom('Eligibility_Trigger','Y');
		}
		//--above code added by nikhil for PCAS-364 CR
		// added by abhishek as per CC FSD
		else if(pId=='BussinessVerification_Button1'){
		
		//added by akshay on 25/4/18 for proc 8735
		 if(getNGValue('cmplx_BussVerification_contctTelChk')=='Yes')
		{
			if(isFieldFilled('cmplx_BussVerification_desigofverifier')==false)
			{
				showAlert('cmplx_BussVerification_desigofverifier',alerts_String_Map['CC269']);
				return false;
			}
			else if(isFieldFilled('cmplx_BussVerification_cntctpersonname')==false)
			{
				showAlert('cmplx_BussVerification_desigofverifier',alerts_String_Map['CC270']);
				return false;
			}
		}
		
		if(getNGValue('cmplx_BussVerification_Decision')=='Pending' || getNGValue('cmplx_BussVerification_Decision')=='Approved' || getNGValue('cmplx_BussVerification_Decision')=='Reject')//changed by akshay on 25/6/18 for proc 9815
		{
				if(checkMandatory(CC_BUSINESSVERIFICATION)){
				return true;
				}
		}
		
		else{
				return true;
				
			}

		}
		// added by abhishek as per CC FSD
		else if(pId=='HomeCountryVerification_Button1'){
	
			if(getNGValue('cmplx_HCountryVerification_Hcountrytelverified')=='Yes'){
				if(checkMandatory(CC_HOMEVERIFICATION)){
					return true;
					}
				}
			
				else{
				  showAlert('cmplx_HCountryVerification_Hcountrytelverified',alerts_String_Map['CC154']);
				}
		
			}
		else if(pId=='ResidenceVerification_Button1'){
		if(getNGValue('cmplx_ResiVerification_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
			if(checkMandatory(CC_RESIDENCEVERIFICATION)){
				return true;
				}
				}
				else{
				return true;
				}
		}
		else if(pId=='GuarantorVerification_Button1'){

		return true;

		}
		//commented by abhishek as per CC FSD
		else if(pId=='ReferenceDetailVerification_Button1'){
		if(getNGValue('cmplx_ResiVerification_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
		
		if(getNGValue('cmplx_RefDetVerification_ref1cntctd')!='--Select--' && getNGValue('cmplx_RefDetVerification_ref1cntctd')!='' ){
					if(checkMandatory(CC_REFERENCEVERIFICATION)){
					return true;
					}
				}
				else{
				   showAlert('cmplx_RefDetVerification_ref1cntctd',alerts_String_Map['CC152']);
				}
		}
		else{
		return true;
		}

		}
		
		else if(pId=='OfficeandMobileVerification_Button1'){
		
		if(getNGValue('cmplx_OffVerification_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
			if((getNGValue('cmplx_OffVerification_hrdnocntctd') =='No' || getNGValue('cmplx_OffVerification_hrdnocntctd') =='--Select--')&&(getNGValue('cmplx_OffVerification_colleaguenoverified') =='No' || getNGValue('cmplx_OffVerification_colleaguenoverified') =='--Select--') &&(getNGValue('cmplx_OffVerification_hrdemailverified') =='No' || getNGValue('cmplx_OffVerification_hrdemailverified') =='--Select--') )
			{
				showAlert('cmplx_OffVerification_Decision','Decison cannot be positive');
				return false;
			}
			if(getNGValue('cmplx_OffVerification_hrdnocntctd') =='Yes' && (getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=="" || getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=="--Select--") )
			{
				showAlert('cmplx_OffVerification_offtelnovalidtdfrom',alerts_String_Map['CC241']);
				return false;
			}
			if(getNGValue('cmplx_OffVerification_hrdnocntctd') =='Yes')
			{
				if(getNGValue('cmplx_OffVerification_hrdcntctname')=="")
				{
					showAlert('cmplx_OffVerification_hrdcntctname',alerts_String_Map['CC243']);
				return false;
				}
				if(getNGValue('cmplx_OffVerification_hrdcntctdesig')=="")
				{
					showAlert('cmplx_OffVerification_hrdcntctdesig',alerts_String_Map['CC244']);
				return false;
				}
			
				if(getNGValue('cmplx_OffVerification_hrdcntctno')=="")
				{
					showAlert('cmplx_OffVerification_hrdcntctno',alerts_String_Map['CC242']);
				return false;
				}
				
			}
			if(getNGValue('cmplx_OffVerification_hrdemailverified') =='Yes')
			{
				if(getNGValue('cmplx_OffVerification_hrdemailid')=='')
				{
				showAlert('cmplx_OffVerification_hrdemailid','HRD/Customer Email is Mandatory!');
				return false;	
				}
				if(getNGValue('cmplx_OffVerification_hrdcntctname')=="")
				{
					showAlert('cmplx_OffVerification_hrdcntctname',alerts_String_Map['CC243']);
				return false;
				}
				if(getNGValue('cmplx_OffVerification_hrdcntctdesig')=="")
				{
					showAlert('cmplx_OffVerification_hrdcntctdesig',alerts_String_Map['CC244']);
				return false;
				}
				
				
			}
			if(getNGValue('cmplx_OffVerification_colleaguenoverified') =='Yes')
			{
				if(getNGValue('cmplx_OffVerification_colleagueno')=="")
				{
					showAlert('cmplx_OffVerification_colleagueno',alerts_String_Map['CC245']);
				return false;
				}
				if(getNGValue('cmplx_OffVerification_colleaguename')=="")
				{
					showAlert('cmplx_OffVerification_colleaguename',alerts_String_Map['CC246']);
				return false;
				}
				if(getNGValue('cmplx_OffVerification_colleaguedesig')=="")
				{
					showAlert('cmplx_OffVerification_colleaguedesig',alerts_String_Map['CC247']);
				return false;
				}
			}
			/*if((isFieldFilled('cmplx_OffVerification_offtelnocntctd')==false || getNGValue('cmplx_OffVerification_offtelnocntctd')=='NA') && (isFieldFilled('cmplx_OffVerification_hrdnocntctd') ==false || getNGValue('cmplx_OffVerification_hrdnocntctd')=='NA') && (isFieldFilled('cmplx_OffVerification_colleaguenoverified')==false || getNGValue('cmplx_OffVerification_colleaguenoverified')=='NA'))
			{
				showAlert('cmplx_OffVerification_hrdnocntctd',alerts_String_Map['PL407']);
				return false;
			}*/
			/*if(getNGValue('cmplx_OffVerification_colleaguenoverified') == '--Select--' || getNGValue('cmplx_OffVerification_colleaguenoverified') == '')
			{
				showAlert('cmplx_OffVerification_colleaguenoverified',alerts_String_Map['CC249']);
				return false;
			}*/
			if(getNGValue('cmplx_OffVerification_reamrks') == '')
			{
				showAlert('cmplx_OffVerification_reamrks',alerts_String_Map['VAL129']);
				return false;
			}
			if(getNGValue('cmplx_OffVerification_Decision') == '--Select--' || getNGValue('cmplx_OffVerification_Decision') == '')
			{
				showAlert('cmplx_OffVerification_Decision',alerts_String_Map['VAL197']);
				return false;
			}
			if (getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				if(!checkMandatory(CC_SELF_OFFICEVERIFICATION))
				{
					return false;
				}
			}
			else{
				if(!checkMandatory(CC_OFFICEVERIFICATION))
				{
					return false;
				}
				else{//sagarika for PCAS-2289
					if(getNGValue('cmplx_OffVerification_cnfminjob_ver')!='NA' && getNGValue('cmplx_OffVerification_hrdnocntctd')=='Yes' && getNGValue('cmplx_EmploymentDetails_LOS')>'.06')//done by sagarika 
					{
						setLockedCustom('cmplx_OffVerification_cnfminjob_ver',false);
						setEnabled('cmplx_OffVerification_cnfminjob_ver',true);
						if(getNGValue('cmplx_OffVerification_cnfminjob_ver')== '--Select--')
						{
							showAlert('cmplx_OffVerification_cnfminjob_ver',"Confirmed in Job Verification cannot be blank");
							return false;
						}
					}
				}
			}	//Arun 14/12/17		
			//++ below code added by abhishek as per CC FSD 2.7.3
			var ver="cmplx_OffVerification_hrdemailverified:cmplx_OffVerification_fxdsal_ver:cmplx_OffVerification_accpvded_ver:cmplx_OffVerification_desig_ver:cmplx_OffVerification_doj_ver:cmplx_OffVerification_cnfminjob_ver";
			var update="cmplx_OffVerification_hrdemailid:cmplx_OffVerification_fxdsal_upd:cmplx_OffVerification_accpvded_upd:cmplx_OffVerification_desig_upd:cmplx_OffVerification_doj_upd:cmplx_OffVerification_cnfrminjob_upd";
			var name="HRD / Customer Email Id:Fixed Salary:Accomodation Provided:Designation:Date of Joining:Confirmed in Job";
			if(CheckMandatory_Verification(ver,update,name)|| getNGValue('EmploymentType')=='Self Employed'){//changed by akshay on 28/6/18 for proc 10507
				//pcasp-2348
				if(getNGValue('cmplx_OffVerification_desig_upd')!=null && getNGValue('cmplx_OffVerification_desig_upd')=='OTHER' || getNGValue('cmplx_OffVerification_desig_upd')=='41') {
					if(getNGValue('cmplx_OffVerification_OthDesign_Upt') ==null || getNGValue('cmplx_OffVerification_OthDesign_Upt')=='')
					{
						showAlert('cmplx_OffVerification_OthDesign_Upt','Please Enter Other Designation');
						return false;
					} else{
						return true;
					}
				}else
					return true;
			}
			}
				else if(getNGValue('cmplx_OffVerification_Decision')=='--Select--' || getNGValue('cmplx_OffVerification_Decision')==''){
				showAlert('cmplx_OffVerification_Decision','Please select Decision');
				return false;
				}
				else{
				return true;
				}
		}
//-- Above code added by abhishek as per CC FSD 2.7.3 
		
		else if(pId=='LoanandCard_Button1'){
//++ below code added by abhishek as per CC FSD 2.7.3
//++Below code added by nikhil 13/11/2017 for Code merge
		if(getNGValue('cmplx_LoanandCard_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
			if(!checkMandatory(CC_LoanCard_CPV))
			{
				return false;
			}
			var ver="cmplx_LoanandCard_cardtype_ver:cmplx_LoanandCard_cardlimit_ver";//cmplx_LoanandCard_loanamt_ver:cmplx_LoanandCard_tenor_ver:cmplx_LoanandCard_emi_ver:cmplx_LoanandCard_islorconv_ver:cmplx_LoanandCard_firstrepaydate_ver:
			var update="cmplx_LoanandCard_cardtype_upd:cmplx_LoanandCard_cardlimit_upd";//cmplx_LoanandCard_loanamt_upd:cmplx_LoanandCard_tenor_upd:cmplx_LoanandCard_emi_upd:cmplx_LoanandCard_islorconv_upd:cmplx_LoanandCard_firstrepaydate_upd:
			var name="Card Type:Card Limit";//Loan Amount:Tenor:EMI:Islamic / Conventional:First Repayment Date:
			if(CheckMandatory_Verification(ver,update,name)){
				return true;
			}
			//-- Above code added by abhishek as per CC FSD 2.7.3 
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		else if(getNGValue('cmplx_LoanandCard_Decision')=='')
		{
			showAlert('cmplx_LoanandCard_Decision','Loan and Card Decision is Mandatory!!');
			return false;
		}
		else{
			return true;
		}
		}
		//added by yash for cc fsd
		else if(pId=='cmplx_DEC_Manual_Deviation')
		{	var man_dev = getNGValue('cmplx_DEC_Manual_Deviation')
			if(man_dev == true)
			{
				setLockedCustom('DecisionHistory_ManualDevReason',false);
				//commented by nikhil PCSP-887
				//setLockedCustom('cmplx_DEC_ReferTo', false);
				
				//change by saurabh on 2nd dec for FSD point.
				setEnabledCustom('DecisionHistory_calReElig', true);
			}
			else
			{
				setLockedCustom('DecisionHistory_ManualDevReason',true);
				//commented by nikhil PCSP-887
				//setLockedCustom('cmplx_DEC_ReferTo', false);	
				//change by saurabh on 2nd dec for FSD point.
				setEnabledCustom('DecisionHistory_calReElig', false);
				
			}
			
		}
	// ++below change already present - 06-10-2017 corrected process name should be : CreditCard
	else if(pId=='DecisionHistory_ManualDevReason')
		{
			dialogToOpenType = pId;
		var new_value = 'DecisionHistory';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
				}
			var deviationReason = getNGValue('cmplx_DEC_Manual_Dev_Reason');
			var url='/webdesktop/custom/CustomJSP/Manual_Deviation_Reasons.jsp?ProcessName=CreditCard&sessionId='+window.parent.sessionId+'&deviationReason=' +deviationReason;//changed by nikhil  
			//var url="/formviewer/resources/scripts/Manual_Deviation_Reasons.jsp?ProcessName=CreditCard";
			//window.open_(url, "", "width=500,height=500");
			//Changes done by deepak to save Deviation code before cam report.
			//var returnValue=window.showModalDialog(url, "", "scrollbars=yes,resizable=yes,width=500px,height=700px");
			//Deepak Changes done for PCAS-3464 on 16 Oct
			
			sOptions = 'dialogWidth:850px; dialogHeight:500px; dialogLeft:250px; dialogTop:80px; center:yes;edge:raised; help:no; resizable:no; scroll:yes;scrollbar:yes; status:no; statusbar:no; toolbar:no; menubar:no; addressbar:no; titlebar:no;';
			//added below to handle window.open/window.showModalDialog according to type of browser starts here.
			/***********************************************************/
			var windowParams="height=600,width=650,toolbar=no,directories=no,status=no,center=yes,scrollbars=no,resizable=no,modal=yes,addressbar=no,menubar=no";
			var returnValue = '';
			if (window.showModalDialog) {
				returnValue = window.showModalDialog(url,null,sOptions);
			} else {
				returnValue = window.open_(url, "", windowParams);
			}
			
			if(getNGValue('cmplx_DEC_Manual_Dev_Reason')!=returnValue){
				setNGValueCustom('Eligibility_Trigger','Y');
			}
			if(returnValue=='NO_CHANGE'){
				setNGValueCustom('cmplx_DEC_Manual_Dev_Reason',"");
			}
			else{
				setNGValueCustom('cmplx_DEC_Manual_Dev_Reason',returnValue);
				setNGValueCustom('Eligibility_Trigger','Y');
			}
			return true;

		}
		//ended by yash
		else if(pId=='NotepadDetails_Button3'){

		return true;

		}
		else if(pId=='SmartCheck_Button1'){

		return true;

		}
		
		else if(pId=='PartMatch_Blacklist')
		{
		//alert("inside blacklist ");
			var partSearchPerform = getNGValue('cmplx_PartMatch_partmatch_flag');
			//alert("inside blacklist partSearchPerform "+ partSearchPerform);
			if(partSearchPerform=="" || partSearchPerform =="N" || partSearchPerform == null)
			{
				showAlert('PartMatch_Search',alerts_String_Map['CC160']);
				return false;
			}
			setNGValueCustom('cmplx_PartMatch_partmatch_flag','true');
			return true;
		}
		else if (pId == 'PartMatch_Company_blacklist')
		{
		var partSearchPerform = getNGValue('cmplx_PartMatch_partmatch_flag');
			//alert("inside blacklist partSearchPerform "+ partSearchPerform);
			if(partSearchPerform=="" || partSearchPerform =="N" || partSearchPerform == null)
			{
				showAlert('PartMatch_Search',alerts_String_Map['CC160']);
				return false;
			}
			else if( partSearchPerform=="false"  )
			{
				showAlert('PartMatch_Blacklist',"Please Perform Individual Blacklist First !");
				return false;
			}
			setNGValueCustom('cmplx_PartMatch_partmatch_flag','NA');
			return true;
		}
		/*else if(pId=='DecisionHistory_Button2')
        {        
            //var url="/formviewer/resources/scripts/Reject_Reasons.jsp?ProcessName=CreditCard";
            //window.open(url, "", "width=500,height=500");
			return true;
        }*/
		else if(pId=='FinacleCore_AddToAverage')
        {        
            if(checkMandatory(CC_AVGACCNO)){
				return true;
				}
        }
		else if(pId=='FinacleCore_AddToTurnover')
        {      
			if(checkMandatory(CC_TRNOVERACCNO)){
				return true;
				}
        }
		else if(pId=='Loan_Disbursal_Save')
        {        
            return true;
        }
		else if(pId=='CC_Creation_Save')
        {        
            return true;
        }
		else if(pId=='Limit_Inc_Save')
        {        
            return true;
        }
		else if(pId=='OriginalValidation_Save')
        {        
            return true;
        }
		else if(pId=='Compliance_Save')
        {        
            return true;
        }
		else if(pId=='cmplx_Compliance_cmplx_gr_compliance')
		{
			return true;
		}
		else if(pId=='NotepadDetailsFCU_Button4')
		{
			return true;
		}
		else if(pId=='NotepadDetailsFCU_Button6')
		{
			return true;
		}
		else if(pId=='NotepadDetailsFCU_Button5')
		{
			return true;
		}
		else if(pId=='BankingCheck_Button1')
		{
			return true;
		}
		else if(pId=='NotepadDetailsFCU_Button1')
		{
			return true;
		}
		else if(pId=='NotepadDetailsFCU_Button2')
		{
			return true;
		}
		else if(pId=='NotepadDetailsFCU_Button3')
		{
			return true;
		}
		else if(pId=='supervisorsection_Button1')
		{
			return true;
		}
		else if(pId=='cmplx_DEC_ContactPointVeri')
		{
			if(getNGValue('cmplx_DEC_ContactPointVeri')=='' || getNGValue('cmplx_DEC_ContactPointVeri')==null || getNGValue('cmplx_DEC_ContactPointVeri')==false )
			{				
				setNGValueCustom('cmplx_DEC_VerificationRequired','Yes');
				setNGValueCustom('CPV_REQUIRED','Y');
			}
			else
			{
				setNGValueCustom('cmplx_DEC_VerificationRequired','No');
				setNGValueCustom('CPV_REQUIRED','N');
			}
		}
		
		//added by akshay on 22/2/18 for drop 4
		else if(pId=='cmplx_DEC_MultipleApplicantsGrid')
		{
			if(window.parent.stractivityName=='DDVT_Checker'){
				if(getLVWAT(pId,getNGListIndex(pId),0)=='Primary')
				{
					if(getNGValue("cmplx_Customer_NTB")==true && getNGValue("cmplx_DEC_New_CIFID")==""){
						setVisible('DecisionHistory_Button2',true);
						if(com.newgen.omniforms.formviewer.isEnabled('DecisionHistory_Button2')){
							setEnabledCustom('DecisionHistory_Button2',true);
						}
					}
					else{
						setVisible('DecisionHistory_Button2',false);
					}
				}
				else if(getNGListIndex(pId)>-1){//Supplement case
			//added by aman for Drop4 on 4th April
					if(getLVWAT(pId,getNGListIndex(pId),3)==''){
						setVisible('DecisionHistory_Button2',true);
						setLockedCustom('DecisionHistory_Button2',false);
						setTop("Incomedetails","150px");
						setLeft("Incomedetails","550px");
						/*if(com.newgen.omniforms.formviewer.isEnabled('DecisionHistory_Button2')){
							com.newgen.omniforms.formviewer.setEnabledCustom('DecisionHistory_Button2',true);
						}*/
					}
					else{
					setVisible('DecisionHistory_Button2',false);
					}
				}
				else{
					setVisible('DecisionHistory_Button2',false);
				}
			}		
		}
		else if(pId=='DecisionHistory_Button2')
		{
			if(getNGListIndex('cmplx_DEC_MultipleApplicantsGrid')==-1)
			{
				showAlert('DecisionHistory_Button2',alerts_String_Map['CC268']);
				return false;
			}
			
			else if(getLVWAT('cmplx_DEC_MultipleApplicantsGrid',getNGListIndex('cmplx_DEC_MultipleApplicantsGrid'),0)=='Primary'){
				if(checkMandatory_CreateCIF('PRIMARY',pId)){
					return true;
				}
			}
			
			else if(getLVWAT('cmplx_DEC_MultipleApplicantsGrid',getNGListIndex('cmplx_DEC_MultipleApplicantsGrid'),0)=='Supplement'){
				if(checkMandatory_CreateCIF('SUPPLEMENT',pId)){
					return true;
				}
			}
				return false;
		}
		//added by nikhil for PCAS-1270
		else if(pId=='cmplx_NotepadDetails_cmplx_Telloggrid')
		{
			return true;
		}
		/*changed by akshay on 19/2/18 for DROP 4
		else if(pId=='DecisionHistory_Button2'){
			var n=getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			var mobileno= getNGValue('AlternateContactDetails_MobileNo1');
			var emailadd= getNGValue('AlternateContactDetails_Email1');
			if(getNGListIndex('cmplx_DEC_MultipleApplicantsGrid')==-1)
			{
				showAlert('DecisionHistory_Button2',alerts_String_Map['CC268']);
				return false;
			}
			if(getNGValue('cmplx_Customer_FirstNAme')=='' || getNGValue('cmplx_Customer_FirstNAme')==null){
			showAlert('cmplx_Customer_FirstNAme',alerts_String_Map['VAL061']);	
			return false;
			}
			else if(getNGValue('cmplx_Customer_Title')=='' || getNGValue('cmplx_Customer_Title')==null){
				showAlert('cmplx_Customer_Title',alerts_String_Map['VAL031']);	
				return false;
			}
			else if(getNGValue('cmplx_Customer_LastNAme')=="" || getNGValue('cmplx_Customer_LastNAme')==null){
				showAlert('cmplx_Customer_LastNAme',alerts_String_Map['VAL076']);	
				return false;
			}
			
			else if(getNGValue('cmplx_Customer_gender')=="--Select--" || getNGValue('cmplx_Customer_gender')==null || getNGValue('cmplx_Customer_gender')==''){
				showAlert('cmplx_Customer_gender',alerts_String_Map['VAL064']);	
				return false;
			}
			else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="--Select--" || getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')==null  || getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')==''){
				showAlert('cmplx_Customer_RESIDENTNONRESIDENT',alerts_String_Map['VAL134']);	
				return false;
			}

		
			else if(getNGValue('cmplx_Customer_MAritalStatus')=="--Select--" || getNGValue('cmplx_Customer_MAritalStatus')==null|| getNGValue('cmplx_Customer_MAritalStatus')==''){
				showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['VAL081']);	
				return false;
			}
			else if(getNGValue('cmplx_Customer_DOb')=="" || getNGValue('cmplx_Customer_DOb')==null){
				showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL045']);	
				return false;
			}
			else if((getNGValue('cmplx_Customer_EmirateIDExpiry')=="" || getNGValue('cmplx_Customer_EmirateIDExpiry')==null) && getNGValue('cmplx_Customer_NTB')!= true){
				showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['VAL052']);	
				return false;
			}
			else if(getNGValue('cmplx_Customer_VisaExpiry')=="" || getNGValue('cmplx_Customer_VisaExpiry')==null){
				showAlert('cmplx_Customer_VisaExpiry',alerts_String_Map['VAL153']);	
				return false;
			}
			else if(getNGValue('cmplx_Customer_PassPortExpiry')=="" || getNGValue('cmplx_Customer_PassPortExpiry')==null){
				showAlert('cmplx_Customer_PassPortExpiry',alerts_String_Map['VAL095']);	
				return false;
			}
			else if(getNGValue('cmplx_Customer_Nationality')=="--Select--" || getNGValue('cmplx_Customer_Nationality')==null || getNGValue('cmplx_Customer_Nationality')==''){
				showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL090']);	
				return false;
			}
			else if(getNGValue('cmplx_EmploymentDetails_EmpStatus')=="--Select--" || getNGValue('cmplx_EmploymentDetails_EmpStatus')==null){
				showAlert('cmplx_EmploymentDetails_EmpStatus',alerts_String_Map['VAL056']);	
				return false;
			}
			
			
			else if (n==0){
					showAlert('cmplx_AddressDetails_cmplx_AddressGrid',alerts_String_Map['VAL009']);
					return false;
			}
				//if(NEW_ACCOUNT_REQ_checkMandatory())
			else if(mobileno==''){
						showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL085']);
						return false;
			}
			else if(emailadd==''){
						showAlert('AlternateContactDetails_Email1',alerts_String_Map['VAL051']);
						return false;
			}
			else{
				return true;
			}
		}
	*/
		else if(pId=='Generate_FPU_Report'){
				if(isVisible('checklist_ver_sp2_Frame1')==false || isVisible('SmartCheck1_Frame1')==false || isVisible('Banking_Check')==false || isVisible('fieldvisit_sp2_Frame1')==false || isVisible('EmploymentVerification_s2_Frame1')==false || isVisible('Cust_ver_sp2_Frame1')==false || isVisible('CustDetailVerification1_Frame1')==false)
			{
			showAlert('','Please Visit all FPU fragments first');
			return false;
			
			}	
			setNGValueCustom('Is_FPU_Generated','Y');
			fpu_generated=true;
			var wi_name = window.parent.pid;
			var docName = "FPU_REPORT_SALARIED";
			//PCASP-2786
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed'){
				 docName = "FPU_REPORT_SELF_EMPLOYED";
			}

			var attrbList = "";		
			attrbList += FPUTemplate(); 
			attrbList= attrbList.replace(/\+/g, "SynPlus");
			attrbList= attrbList.replace(/\%/g, "SynPerc");
            attrbList=encodeURIComponent(attrbList);
			
			var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
				//changes by saurabh for PCSP-204
				CreateIndicator('GenTemp');
				var url = "/webdesktop/custom/CustomJSP/Generate_Template.jsp?attrbList="+attrbList+"&wi_name="+wi_name+"&docName="+docName+"&sessionId="+window.parent.sessionId;

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send(null);
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);
					var columnValue_arr=dataFromAjax.split('~');
					var statusDoc = columnValue_arr[0];
					var inputXML = columnValue_arr[1];
					var docIndex = columnValue_arr[2];
					statusDoc= statusDoc.trim();
					if(inputXML!=undefined){
						inputXML= inputXML.trim();
						docIndex= docIndex.trim();
					}
					//alert("inputXML "+ inputXML);
					
					window.parent.customAddDoc(inputXML, docIndex, 'new');
					if(statusDoc == 'Success')
					{
						alert("Template generated successfully");
					}
					//changes by saurabh for PCSP-204
					RemoveIndicator('GenTemp');
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					//changes by saurabh for PCSP-204
					RemoveIndicator('GenTemp');
				}
	}
		//added by yash for CC FSD TEMPLATE
		else if(pId=='DecisionHistory_Button4')
		{
			//below code added by nikhil for PCAS-1369
			//deepak activityName condition added by for PCAS-2125
				if(getNGValue('ELigibiltyAndProductInfo_EFMS_Status')!='Y' && activityName=='CAD_Analyst1')
				{
				showAlert(pId,alerts_String_Map['CC281']);
				
				return false;	
				}
			setNGValueCustom('Is_CAM_generated','Y');
			
			//changes done by disha for new generate template socket on 11-Sep-2018
			var wi_name = window.parent.pid;
			var docName = "";
			var attrbList = "";		
			attrbList += CLTemplateData(); 
			attrbList= attrbList.replace(/\+/g, "SynPlus");
			attrbList= attrbList.replace(/\%/g, "SynPerc");
            attrbList=encodeURIComponent(attrbList);
			
			if(activityName=='CAD_Analyst1'){
				//change by saurabh on 7th Dec
				setNGValueCustom('Is_CAM_generated','Y');
				//PCASP-2834
				 if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed'))
				 {
					docName = "CAM_REPORT_PRE_APPROVED";
					//window.parent.executeAction('6', 'Y');
					//return true;
				 }
				 else  if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='MOR'))
				 {
						docName = "CAM_REPORT_PA_ML";
				}
				 else  if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RFL' || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RFLP' ))
				 {
						docName = "CAM_REPORT_PA_RFL";
				}
				 else  if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed'))
				 {
						docName = "CAM_REPORT_PA_SE";
				}
				 else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Product Upgrade') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PU'))
				 {
					 docName = "CAM_REPORT_PRODUCT_UPGRADE";
					//window.parent.executeAction('5', 'Y');
					//return true;
				 }
				else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Limit Increase') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='LI'))
				 {
					docName = "CAM_REPORT_LIMIT_INCREASE";
					//window.parent.executeAction('5', 'Y');
					//return true;
				 }
				 //commented by Shivang for spring3 as New condition Introduced
				  /*else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Secured Card') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SEC'))
				 {
					  docName = "CAM_REPORT_SELF_EMPLOYED";
					//window.parent.executeAction('4', 'Y');
					//return true;
				 }*/
				 else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PULI')
				 {
					  docName = "CAM_REPORT_SELF_EMPLOYED";
					//window.parent.executeAction('5', 'Y');
					//return true;
				 }
				  else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='IM') //added by Prateeka
				 {
					  docName = "CAM_REPORT_IM";
					//window.parent.executeAction('5', 'Y');
					//return true;
				 }

				  //added by Prateeka
				else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SE')
				 {
					  if (getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid') >0)
					  {
						for(var i=0; i <getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid'); i++)
						{ 
							if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,1)=='Business')
							{
								if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU@10' || getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU')
								{
									docName = "CAM_REPORT_SELF_EMPLOYED_BAU";
								}	
								else
								{									
									docName = "CAM_REPORT_SELF_EMPLOYED_SURROGATE";
								}
								
								
							}
						}
					  }
					//window.parent.executeAction('2', 'Y');
					//return true;
				 }
				 
				  //Added By shivang for BTC CAM
				 else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='SMES')
				 {
					 docName = "CAM_REPORT_BTC_SECURED";
				 }
				 else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' && (!(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='SMES')))
				 {
					 docName = "CAM_REPORT_BTC";
				 }
				 //Added By Shivang for Secured Card CAM
				else if((!(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SEC'))
				 {
					  docName = "CAM_REPORT_SEC_SALARIED";					
				 }
				else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed') && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SEC'))
				 {
					  docName = "CAM_REPORT_SEC_SELF_EMPLOYED";					
				 }
				 //Added By Shivang for Bundled Card CAM
				else if((!(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BPA'))
				 {
					  docName = "CAM_REPORT_BUNDLED_SAL";					
				 }
				else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed') && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BPA'))
				 {
					  docName = "CAM_REPORT_BUNDLED_SE";					
				 }
				else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6).indexOf('Salaried')>-1 || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6).indexOf('Pensioner')>-1)//done by sagarika for PCAS-2059
				 {
					 docName = "CAM_REPORT_SALARIED";
					//window.parent.executeAction('1', 'Y');
					//return true;
				 }
				
				
			}
			else if(activityName=='Cad_Analyst2'){
				
				//commented by Shivang for spring3 as New condition Introduced
				 /*if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Secured Card') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SEC'))
				 {
					docName = "CAM_REPORT_SELF_EMPLOYED";
					//window.parent.executeAction('6', 'Y');
					//return true;
				 }*/
				 if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed'))
				 {
					docName = "CAM_REPORT_PRE_APPROVED";
					//window.parent.executeAction('6', 'Y');
					//return true;
				 }
				 else  if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='MOR'))
				 {
						docName = "CAM_REPORT_PA_ML";
				}
				 else  if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RFL' || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RFLP' ))
				 {
						docName = "CAM_REPORT_PA_RFL";
				}
				 else  if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed'))
				 {
						docName = "CAM_REPORT_PA_SE";
				}
				else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Product Upgrade') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PU'))
				 {
					docName = "CAM_REPORT_PRODUCT_UPGRADE";
					//window.parent.executeAction('4', 'Y');
					//return true;
				 }
				 else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Limit Increase') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='LI'))
				 { 
					 docName = "CAM_REPORT_LIMIT_INCREASE";
					//window.parent.executeAction('1', 'Y');
					//return true;
				 } 
				 //Added by Shivang
				 else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='IM') 
				 {
					  docName = "CAM_REPORT_IM";
					//window.parent.executeAction('5', 'Y');
					//return true;
				 }
				else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SE')
				 {
					 if (getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid') >0)
					  {
						for(var i=0; i <getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid'); i++)
						{ 
							if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,1)=='Business')
							{
								if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU@10' || getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU')
									docName = "CAM_REPORT_SELF_EMPLOYED_BAU";
									
								else	
									docName = "CAM_REPORT_SELF_EMPLOYED_SURROGATE";
								
								
							}
						}
					  }
					//window.parent.executeAction('3', 'Y');
					//return true;
				 }
				 
				 //Added By shivang for BTC CAM
				 else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='SMES')
				 {
					 docName = "CAM_REPORT_BTC_SECURED";
				 }
				 else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' && (!(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='SMES')))
				 {
					 docName = "CAM_REPORT_BTC";
				 }
				 //Added By Shivang for Secured Card CAM
				else if((!(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SEC'))
				 {
					  docName = "CAM_REPORT_SEC_SALARIED";					
				 }
				else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed') && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='SEC'))
				 {
					  docName = "CAM_REPORT_SEC_SELF_EMPLOYED";					
				 }
				 //Added By Shivang for Bundled Card CAM
				else if((!(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BPA'))
				 {
					  docName = "CAM_REPORT_BUNDLED_SAL";					
				 }
				else if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed') && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BPA'))
				 {
					  docName = "CAM_REPORT_BUNDLED_SE";					
				 }
				 else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6).indexOf('Salaried')>-1 || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6).indexOf('Pensioner')>-1)//done by sagarika for PCAS-2059
				 {
					 docName = "CAM_REPORT_SALARIED";
					//window.parent.executeAction('2', 'Y');
					//return true;
				 }
				
			
			}
			// docName = "CAM_REPORT_SELF_EMPLOYED";
				var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
				//changes by saurabh for PCSP-204
				CreateIndicator('GenTemp');
				var url = "/webdesktop/custom/CustomJSP/Generate_Template.jsp?attrbList="+attrbList+"&wi_name="+wi_name+"&docName="+docName+"&sessionId="+window.parent.sessionId;

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send(null);
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);
					var columnValue_arr=dataFromAjax.split('~');
					var statusDoc = columnValue_arr[0];
					var inputXML = columnValue_arr[1];
					var docIndex = columnValue_arr[2];
					statusDoc= statusDoc.trim();
					if(inputXML!=undefined){
						inputXML= inputXML.trim();
						docIndex= docIndex.trim();
					}
					//alert("inputXML "+ inputXML);
					
					window.parent.customAddDoc(inputXML, docIndex, 'new');
					if(statusDoc == 'Success')
					{
						alert("Template generated successfully");
					}
					//changes by saurabh for PCSP-204
					RemoveIndicator('GenTemp');
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					//changes by saurabh for PCSP-204
					RemoveIndicator('GenTemp');
				}
				flag_add_new=false;//sagarika for CAM
			
		}
		//ended by yash for CC FSD Template
		
		else if(pId=='AddressDetails_addr_Add')
        {
		if(checkMandatory_Address('add')){
				if(Address_Validate('add'))
					return true;
}					
		}
		else if (pId=='cmplx_IncomeDetails_Accomodation_0' || pId=='cmplx_IncomeDetails_Accomodation_1')
		{
		setNGValueCustom('Eligibility_Trigger','Y');
		}
		else if(pId=='ExtLiability_Consdierforobligations')
		{
			setNGValueCustom('Eligibility_Trigger','Y');
		}
		else if(pId=='AddressDetails_addr_Modify')
        {        
        if (checkMandatory_Address('modify')){
	            if (Address_Validate('modify'))
					return true;
					}
		}
		else if(pId=='AddressDetails_addr_Delete')
        {        
            return true;
        }
		else if(pId=='cmplx_Customer_card_id_available')
        {    
			if(getNGValue('cmplx_Customer_card_id_available')==true){
					setLockedCustom("cmplx_Customer_CIFNo",false);
						
						 
					}	
					
					else{
					    setLockedCustom("cmplx_Customer_CIFNo",true);
					}
           
        }
		else if(pId=='Customer_Button1'){
		//alert("inside button dedupe");
			return true;
		}
 // ++ below code already present - 06-10-2017 finacle core, Liability addiion, OECD grid button
		
		
		else if(pId=='FinacleCore_CalculateTurnover'){
		
		return true;
		}

		else if (pId =='cmplx_Liability_New_overrideIntLiab'){ 
	
	    var liabChk = getNGValue('cmplx_Liability_New_overrideIntLiab');
		if(liabChk == true)
			{
				setVisible("Liability_New_Overwrite", true);
				


			}
			else{
				setVisible("Liability_New_Overwrite", false);

			}
				return true;			
		}
	else if(pId=='OECD_Save'){
			//if(checkMandatory(CC_OECD))
			//12th september
			if(checkMandatory_CC_OECD())
			//12th september
				{
				removeFrame(pId);
				return true;}
		}
		
		else if(pId=='OECD_modify')
		{
			if(checkMandatory_OecdGrid())
			return true;
		}	

		else if(pId=='OECD_delete')
		{
			return true;
		}	
		else if(pId=='cmplx_FATCA_cmplx_GR_FatcaDetails')
		{
				return true;
		}
		//Added By Prabhakar for drop-4Point-3
		// else if(pId=='FATCA_Add')
		// {
			// var selectedReason=getNGValue('FATCA_selectedreasonhidden');
			// if(selectedReason.endsWith(","))
			// {
				// selectedReason=selectedReason.slice(0, -1);
			// }
			// setNGValueCustom('FATCA_selectedreasonhidden',selectedReason);
			// return true;
		// }
		else if(pId=='FATCA_Add')
		{
			if(FATCA_Save_Check('add'))
			{
			var selectedReason=getNGValue('FATCA_selectedreasonhidden');
			if(selectedReason.charAt(selectedReason.length-1)==',')
			{
				selectedReason=selectedReason.substring(0,selectedReason.length-1)
			}
			setNGValueCustom('FATCA_selectedreasonhidden',selectedReason);
				return true;
			}
		}
		//Added By Prabhakar for drop-4Point-3
		else if(pId=='FATCA_Modify')
		{
			if(FATCA_Save_Check('Modify'))
			{
			var selectedReason=getNGValue('FATCA_selectedreasonhidden');
			if(selectedReason.charAt(selectedReason.length-1)==',')
			{
				selectedReason=selectedReason.substring(0,selectedReason.length-1)
			}
			setNGValueCustom('FATCA_selectedreasonhidden',selectedReason);
				return true;
		}
		}
		//Added By Prabhakar for drop-4Point-3
		else if(pId=='FATCA_Delete')
		{
			setNGValueCustom('FATCA_selectedreasonhidden','');
			return true;
		}
		else if(pId=='FATCA_Save'){
				var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_FATCA_cmplx_GR_FatcaDetails");
			var custTypePickList = document.getElementById('FATCA_CustomerType');
			var picklistValues=getPickListValues(custTypePickList);
			if((picklistValues.length)>n)
			{
				showAlert('FATCA_CustomerType','Please add FATCA for all Applicant type');	
					return false;
			}
			
				removeFrame(pId);
				return true;	
		}
		
		else if(pId=='KYC_Add')
		{	
			if(KYC_add_Check('add'))
				return true;
		}
		else if(pId=='KYC_Modify')
		{
			if(KYC_add_Check('modify'))
				return true;
		}
		else if(pId=='KYC_Delete')
		{
			return true;
		}
		else if(pId=='KYC_Save'){
			////select Kyc for all customer type
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			var custTypePickList = document.getElementById("KYC_CustomerType");
			var picklistValues=getPickListValues(custTypePickList);
			if((picklistValues.length)>n ||n==0)
			{
				showAlert('AddressDetails_CustomerType','Please add kyc for all customer type');	
				return false;
			}
			
				removeFrame(pId);
				return true;
		}
		else if(pId=='FinacleCRMCustInfo_Button2'){
			return true;	
		}
		else if(pId=='DecisionHistory_CifUnlock'){
			if(getNGValue('Is_CustLock_Disbursal')=='N' || getNGValue('Is_CustLock_Disbursal')==''){
				showAlert('',alerts_String_Map['PL392']);
				return false;
			}
			return true;
		}
		else if(pId=='DecisionHistory_CifLock'){
			if(getNGValue('Is_CustLock_Disbursal')=='Y' ){
				showAlert('',alerts_String_Map['PL391']);
				return false;
			}
			return true;
		}
		/*else if(pId=='BT_Add'){
			if(checkMandatory_BTGrid()){
				if(validate_BTGrid())
					return true;
			}
			
		}*/
		else if(pId=='BT_Modify'){
		if(checkMandatory_BTGrid()){
				if(validate_BTGrid())
					return true;
			}	
		}
		else if(pId=='BT_Delete'){
		 return true;
		}
		 //++ Below Code added By abhishek on Oct 6, 2017  to fix : "21,22-Add modify button not working for world check grid,Save button not working in World check" : Reported By Shashank on Oct 05, 2017++

		else if(pId=='WorldCheck1_Add'){
			if(checkMandatory(CC_WORLDCHECK)){
				return true;
			}
		}
		else if(pId=='WorldCheck1_Modify'){
		 return true;
		}
		else if(pId=='WorldCheck1_Delete'){
		return true;
		}
		else if(pId=='WorldCheck1_Save'){
			if(checkMandatory(CC_WorldCheck)){
				removeFrame(pId);
				return true;}
		
		
		}
		 //-- Above Code added By abhishek on Oct 6, 2017  to fix : "21,22-Add modify button not working for world check grid,Save button not working in World check" : Reported By Shashank on Oct 05, 2017--

		//added by abhishek as per CC FSD 
		else if(pId=='DecisionHistory_nonContactable'){
		return true;
		}
		//added by abhishek as per CC FSD 
		else if(pId=='DecisionHistory_cntctEstablished'){
		return true;
		}
		//added by abhishek as per CC FSD 
		else if(pId=='SmartCheck_Add'){
		if(checkMandatory(CC_SmartCheckAdd)){
				return true;
			}
		}
		else if(pId=='SmartCheck_Modify'){
		if(checkMandatory(CC_SmartCheckModify)){

				return true;
			}
		}
		else if(pId == 'cmplx_SmartCheck_SmartCheckGrid')
		{
				//change by aman for PCSP-25 d
				//changed by nikhil for PCSP-462
		if(com.newgen.omniforms.formviewer.getNGListIndex('cmplx_SmartCheck_SmartCheckGrid')>-1 && getLVWAT(pId,getNGListIndex(pId),1)!='' && activityName=='CAD_Analyst1'){
			setEnabledCustom('SmartCheck_Add',false);
			setEnabledCustom('SmartCheck_Modify',true);
		} //Added by Shivang for handling the condition for PCASP-2069 where user have to modify the row Against the CAD remarks row(Over the Bandana fix for PCASP-2069)
		else if(com.newgen.omniforms.formviewer.getNGListIndex('cmplx_SmartCheck_SmartCheckGrid')>-1 && getLVWAT(pId,getNGListIndex(pId),1)=='' && getLVWAT(pId,getNGListIndex(pId),0)!='' && activityName=='Smart_CPV'){
			setEnabledCustom('SmartCheck_Add',false);
			setEnabledCustom('SmartCheck_Modify',true);
		}
		//changed by nikhil for PCSP-462
		else if(com.newgen.omniforms.formviewer.getNGListIndex('cmplx_SmartCheck_SmartCheckGrid')>-1 && getLVWAT(pId,getNGListIndex(pId),0)!='' && getLVWAT(pId,getNGListIndex(pId),3)!='N'  && activityName=='Smart_CPV'){
			setEnabledCustom('SmartCheck_Add',false);
			setEnabledCustom('SmartCheck_Modify',true);
		}
		else if(com.newgen.omniforms.formviewer.getNGListIndex('cmplx_SmartCheck_SmartCheckGrid')>-1){
			setEnabledCustom('SmartCheck_Add',false);
			setEnabledCustom('SmartCheck_Modify',false);
		}	
		 
		else{
			setEnabledCustom('SmartCheck_Add',true);
			setEnabledCustom('SmartCheck_Modify',false);
		}
	//change by aman for PCSP-25
        return true;
		}
		else if(pId=='SmartCheck_Delete'){
		return true;

		}
		else if(pId=='CompanyDetails_Aloc_search'){
		if(getNGValue('compName')!=''){
			var new_value = 'CompDetails';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			
		}
		//Added By shivang for PCASP-2147
		else{
			showAlert('CompanyDetails_SearchAloc','Kindly enter company name');
			return false;
		}
		return true;
		}
		else if(pId=='OfficeandMobileVerification_Enable' || pId=='BussinessVerification_Enable'){
		setLockedCustom(pId,'true');
		return true;
		}
	// ++ above code already present - 06-10-2017 finacle core, Liability addiion, OECD grid button
		 //Code added for AECB REPORT link
		else if(pId=='ExtLiability_AECBReport')
		{
			//if(getFromJSPRowCount('ExtLiability_IFrame_external')>1){
							//Change done by Saurabh for for SQL Injection.
				//var query="select ReportURL from NG_RLOS_CUSTEXPOSE_Derived where child_wi='"+window.parent.pid+"' and Request_Type='ExternalExposure'";
				var result=getDataFromDB('PL_ReportUrl',"Wi_Name=="+window.parent.pid,'PLjs').trim();
				if(result!='NODATAFOUND' && result.indexOf("FAIL")==-1){
					var url_n = result.split('#~');	
					for(var i=0;i<url_n.length;i++)
					{
						if(url_n[i]!='' && url_n[i]!='undefined')
						{
						window.open_(url_n[i],"","ByPassURL");
						}
					}	
					
				}
				else{
				showAlert('ExtLiability_AECBReport','AECB report does not exist for this customer');
				return false;	
				}							
		}
		
		
		else if(pId=='Liability_New_Button1')
		{
			//if(getFromJSPRowCount('ExtLiability_IFrame_external')>1){
							//Change done by Saurabh for for SQL Injection.
				//var query="select ReportURL from NG_RLOS_CUSTEXPOSE_Derived where child_wi='"+window.parent.pid+"' and Request_Type='ExternalExposure'";
				var result=getDataFromDB('PL_ReportUrl_company',"Wi_Name=="+window.parent.pid,'PLjs').trim();
				if(result!='NODATAFOUND' && result.indexOf("FAIL")==-1){
					var url_n = result.split('#~');	
					for(var i=0;i<url_n.length;i++)
					{
						if(url_n[i]!='' && url_n[i]!='undefined')
						{
						window.open_(url_n[i],"","ByPassURL");
						}
					}	
					
				}
				else{
				showAlert('Liability_New_Button1','AECB report does not exist for this company');
				return false;	
				}							
		}
		//added by abhishek as per CC FSD
		else if(pId=='FinacleCore_Button9'){
			return true;
		}	
		//added by abhishek as per CC FSD to recalculate data
		else if(pId=='FinacleCore_ReCalculate'){
			return true;
		}
		//added by abhishek as per CC FSD to modify avg bal nbc repeater data
		else if(pId=='FinacleCore_Repeater_modify'){
		
			if(getNGValue('FinacleCore_accmodify')=='' || getNGValue('FinacleCore_acc_modify')==''){
				showAlert('FinacleCore_acc_modify',alerts_String_Map['CC151']);
				return false;
			}
			else{
				return true;
			}
			
		}
		else if(pId=='FinacleCore_Button10'){
		
			if(getNGValue('FinacleCore_accmodify')=='' || getNGValue('FinacleCore_acc_modify')==''){
				showAlert('FinacleCore_acc_modify','Please enter an account number than Review Balance');
				return false;
			}
			else{
				return true;
			}
			
		}
		else if(pId=='FinacleCore_Modifycfo')
		{
			return true;
		}
		//added by gaurav
		else if(pId=='Customer_Consents')
		{
			return true;
		}
		//added by abhishek as per CC FSD to modify turnover repeater data
		else if(pId=='FinacleCore_TrnRepeater_modify'){
			return true;
		}
		else if(pId=='FinacleCore_Button11'){
			return true;
		}
		
		//modified by akshay on 23-2-18
		else if(pId=='cmplx_ReferHistory_cmplx_GR_ReferHistory'){
			//DOne by aman for sprint 2
			if( (getLVWAT(pId,getNGListIndex(pId),5)==activityName || (getLVWAT(pId,getNGListIndex(pId),5)=='Source' && activityName=='DSA_CSO_Review') || (getLVWAT(pId,getNGListIndex(pId),5)=='CSO (for documents)' && activityName=='DSA_CSO_Review') || (getLVWAT(pId,getNGListIndex(pId),5)=='CPV Checker' && activityName=='CPV_Analyst')|| ((getLVWAT(pId,getNGListIndex(pId),5)=='FPU'||getLVWAT(pId,getNGListIndex(pId),5)=='FCU') && (activityName=='FCU'||activityName=='FPU')&&(activityName=='DDVT_maker')&&(activityName=='DDVT_Maker'))))
			{
				setLockedCustom('ReferHistory_status',false);
				setLockedCustom('ReferHistory_Modify',false);
				return true;
			}
			else{	
				setLockedCustom('ReferHistory_status',true);
				setLockedCustom('ReferHistory_Modify',true);
				return false;
			}
		}
		//ended by akshay on 9/12/17
		
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		else if(pId=='ELigibiltyAndProductInfo_Refresh')
		{
		//below code added by nikhil for PCSP-504 Sprint-2
			setNGValueCustom('ELigibiltyAndProductInfo_EFMS_Status','Y');
			return true;
		}
//-- Above code added  by abhishek on 04/01/2018 for EFMS refresh functionality
		else if(pId=='Button_City')
		{ 
			return true;
		}
		
		else if(pId=='Button_State')
		{ 
			return true;
		}
		else if(pId=='ButtonOECD_State')
		{ 
			return true;
		}
			//Changes done by aman on 18th July
		
		else if(pId=='cmplx_FinacleCore_credturn_grid'){
			var RowNum=getNGListIndex(pId);
			var RowToInsert='';
			var value=getLVWAT('cmplx_FinacleCore_credturn_grid',RowNum,0);
			var value2=getLVWAT('cmplx_FinacleCore_credturn_grid',RowNum,1);
			var toSet='';
			if(getNGValue('AccountNoMonth')==''){
				toSet=value+','+value2;
				
			}
			else{
				toSet=getNGValue('AccountNoMonth')+':'+value+','+value2;
			}
			if (getNGValue('AccountNoMonth').indexOf(value+','+value2)==-1)
			{
				if(getNGValue('RowNum')==''){
					RowToInsert=RowNum;
				}
				else{
					RowToInsert=getNGValue('RowNum')+':'+RowNum;
				}
			setNGValueCustom('RowNum',RowToInsert);	
			setNGValueCustom('AccountNoMonth',toSet);
				
			}
		}
		
		else if(pId=='Clear'){
			setNGValueCustom('AccountNoMonth','');
			setNGValueCustom('RowNum','');	
		}
	//Changes done by aman on 18th July
	return false;
}
//for document related tab
 function addDocFromOD(){
window.parent.openAddDocWin(); //workdesk.js
}
function addDocFromPC(){
window.parent.openImportDocWin(); //workdesk.js
}
//Changes done by deepak for scan
function openScannerWindow(){
window.parent.openOmniScanDocUrl();
//window.parent.scanDoc(); //workdesk.js
}
function printImageDocument(docIndex){
//window.parent.PrintDoc(); //doctypes.js
var docWindowObj="";
	   if(window.parent.strprocessname==''){
		//docWindowObj=window.parent;
		showAlert('','Please open document interface!!');
		return false;
		}
		else{
			docWindowObj=docWindowObj=window.parent;
		
		var rowNumber=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('Rownum').value;
        var docIndex_Id="IncomingDoc_Frame_Reprow"+rowNumber+"_Repcolumn11";
		if(docIndex=='')
		{
			showAlert('','Either User has not attached document or it is not mapped!!');
			return false;
		}	
window.parent.PrintDoc(); //doctypes.js
}
}
function customDownloadDocument(){
window.parent.downloadDoc("http://192.168.54.80/formviewer/components/workitem/view/workdesk_default.jsf#","/formviewer"); //doctypes.js
} 
  
  
  /*
function addDocFromOD(){
	window.parent.openAddDocWin(); //workdesk.js
}

function addDocFromPC(){
window.parent.openImportDocWin(); //workdesk.js
}
function openScannerWindow(){
window.parent.scanDoc(); //workdesk.js
}
function printImageDocument(){
window.parent.PrintDoc(); //doctypes.js
}
function customDownloadDocument(){
window.parent.downloadDoc("http://192.168.54.80/formviewer/components/workitem/view/workdesk_default.jsf#","/formviewer"); //doctypes.js
}*/
//ended here
function change_CreditCard(pId)
{
setNGValueCustom(pId,getNGValue(pId));
	//this code moved up as needs to be ran all time in case of change by nikhil 05/08/2019
	var activityName = window.parent.stractivityName;
	//changed by saurabh on 23rd Oct 17.
	if(getNGValue(pId) != PLValuesData[pId]){
			var value = document.getElementById(document.getElementById(pId).parentNode.id).parentNode.id;
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(value)==-1){
			hiddenStringPL = value+',';
			//document.getElementById(pId).style.setProperty ("background", "#ffe0bd", "important");
			var previousValue = getNGValue('FrameName');
			hiddenStringPL = previousValue + hiddenStringPL;
			setNGValueCustom('FrameName',hiddenStringPL);
			}
			//++below code added by nikhil for PCAS-364 CR
			//changed code for PCAS-2696
			if(value.indexOf('IncomeDetails')>-1 || value.indexOf('ExtLiability')>-1 || value.indexOf('EMploymentDetails')>-1   || value.indexOf('FinacleCore')>-1 || value.indexOf('EmploymentDetails')>-1 || value.indexOf('CompDetails')>-1 )
			{
				setNGValueCustom('Eligibility_Trigger','Y');
			}
			//--above Code added by nikhil for PCAS-364 CR
		}
	//Added by shivang for PCASP-1671
	if(activityName== 'DDVT_Maker'){
	if(getNGValue(pId)=='AE'){
		setLockedCustom('cmplx_Customer_VisaNo',true);
		setLockedCustom('cmplx_Customer_VisaIssueDate',true);
		setLockedCustom('cmplx_Customer_VisaExpiry',true);
	}else if(!(getNGValue(pId)=='AE')){
		setLockedCustom('cmplx_Customer_VisaNo',false);
		setLockedCustom('cmplx_Customer_VisaIssueDate',false);
		setLockedCustom('cmplx_Customer_VisaExpiry',false);
	}
	if(getNGValue(pId)=='AE'){
		setLockedCustom('SupplementCardDetails_VisaNo',true);
		setLockedCustom('SupplementCardDetails_VisaIssueDate',true);
		setLockedCustom('SupplementCardDetails_VisaExpiry',true);
		
	}else if(!(getNGValue(pId)=='AE')){
		setLockedCustom('SupplementCardDetails_VisaNo',false);
		setLockedCustom('SupplementCardDetails_VisaIssueDate',false);
		setLockedCustom('SupplementCardDetails_VisaExpiry',false);
	}
	}
	if(activityName == 'CSM' || activityName== 'DDVT_Maker'){
	if(rlosValuesData[pId]){
		if(getNGValue(pId) != rlosValuesData[pId]){
			var field_string_value = getNGValue('fields_string');
			if(field_string_value.indexOf(pId)==-1){
			hiddenString = pId+',';
			//document.getElementById(pId).style.setProperty ("background", "#ffe0bd", "important");
			var previousValue = getNGValue('fields_string');
			hiddenString = previousValue + hiddenString;
			setNGValueCustom('fields_string',hiddenString);
			}
		}
		//console.log(hiddenString);
	}
	}
	// Added by Pooja
	if(pId=='cmplx_CardDetails_SelfSupp_Limit'){
		if(getNGValue(pId)!=""){
			var str=getNGValue('cmplx_CardDetails_SelfSupp_Limit');
			if(str.indexOf('.')>-1){
				showAlert('cmplx_CardDetails_SelfSupp_Limit','Decimal Value is not allowed!');
				return false;

		}

	}
	}
	if(pId=='cmplx_EmploymentDetails_LengthOfBusiness'){
		//YearsinUAE(pId);
		YYMM(pId,'','');
		//below validation added by akshay for proc 6718
		var YY=getNGValue(pId).split('.')[0];
		var MM=getNGValue(pId).split('.')[1];
		if(YY.length>2 || parseInt(MM)>11)
		{
			showAlert(pId,alerts_String_Map['VAL256']);
			setNGValueCustom(pId,'');
		}
	}//hritik
	if(pId=='CC_Creation_CheckBox1')
	{
		return true;
	}
	if(pId=='CC_Creation_Validate_Skyward')
	{
		return true;//sagarika
	}
	if(pId=='cmplx_fieldvisit_sp2_field_rep_receivedDate')
	{
	var d2 = getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate');
   var parts=d2.split('/');
   var date2=new Date(parts[2],parts[1]-1,parts[0]);
    var d =  getNGValue('cmplx_fieldvisit_sp2_field_visit_date');
   var parts = d.split('/');
   var date1=new Date(parts[2],parts[1]-1,parts[0]);
   if(date1>date2)
   {
	   showAlert('cmplx_fieldvisit_sp2_field_rep_receivedDate','Field visit received date should be greater than field initiated date');
	  // setNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate','');
	   return false;
   }
	}
	if(pId=='CardDetails_ChequeNumber')
	{
		if(getNGValue('CardDetails_ChequeNumber')!='' && getNGValue("cmplx_CardDetails_Security_Check_Held")!=
true)
		{
			showAlert('cmplx_CardDetails_Security_Check_Held','Security check held tick box is not Ticked');
		}
	}
	if(pId=='CardDetails_Amount')
	{
		if(getNGValue('CardDetails_Amount')!='' && getNGValue("cmplx_CardDetails_Security_Check_Held")!=
true)
		{
			showAlert('cmplx_CardDetails_Security_Check_Held','Security check held tick box is not Ticked');
		}
	}
	if(pId=='AlternateContactDetails_EnrollRewardsIdentifier')
{  var enroll=getNGValue(pId);
	var length1=enroll.length;
	var start=length1-9;
	
	if((length1>=9 && length1<=11))
	{
	var n=enroll.substring(start,length1);
	var number=parseInt(n);
	if(!isNaN(number))
	{
	var res1 = enroll.substring(start,start+8);
	var res2=enroll.substring(start+8,length1);
    var f = parseInt(res1);
	var reminder = parseInt(res2);
	if(f%7!=reminder)
	 {
	 setNGValue('AlternateContactDetails_EnrollRewardsIdentifier','');
		showAlert('AlternateContactDetails_EnrollRewardsIdentifier','Incorrect Skyward Number. Please enter a valid Skyward Number');
	
	 }
	 }
	 else 
	 {
	 setNGValue('AlternateContactDetails_EnrollRewardsIdentifier','');
		showAlert('AlternateContactDetails_EnrollRewardsIdentifier','Incorrect Skyward Number. Please enter a valid Skyward Number'); 
	 }
	}
	 else 
	 {
	 setNGValue('AlternateContactDetails_EnrollRewardsIdentifier','');
		showAlert('AlternateContactDetails_EnrollRewardsIdentifier','Incorrect Skyward Number. Please enter a valid Skyward Number'); 
	 }
	}
	if(pId=='cmplx_emp_ver_sp2_off_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setEnabled("cmplx_emp_ver_sp2_landline",true);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_landline",false);
		}
	}
		if(pId=='cmplx_emp_ver_sp2_desig_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setEnabled("cmplx_emp_ver_sp2_desig_remarks",true);
			setLocked("cmplx_emp_ver_sp2_desig_remarks",false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_desig_remarks",false);
			setLocked("cmplx_emp_ver_sp2_desig_remarks",true);
		}
	}
			if(pId=='cmplx_emp_ver_sp2_empstatus_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setEnabled("cmplx_emp_ver_sp2_empstatus_remarks",true);
			setLocked("cmplx_emp_ver_sp2_empstatus_remarks",false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_empstatus_remarks",false);
			setLocked("cmplx_emp_ver_sp2_empstatus_remarks",true);
		}
	}
			if(pId=='cmplx_emp_ver_sp2_salary_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setEnabled("cmplx_emp_ver_sp2_salary_remarks",true);
			setLocked("cmplx_emp_ver_sp2_salary_remarks",false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_salary_remarks",false);
			setLocked("cmplx_emp_ver_sp2_salary_remarks",true);
		}
	}
				if(pId=='cmplx_emp_ver_sp2_salary_break_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setEnabled("cmplx_emp_ver_sp2_salary_break_remarks",true);
			setLocked("cmplx_emp_ver_sp2_salary_break_remarks",false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_salary_break_remarks",false);
			setLocked("cmplx_emp_ver_sp2_salary_break_remarks",true);
		}
	}
					if(pId=='cmplx_emp_ver_sp2_doj_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setEnabled("cmplx_emp_ver_sp2_doj_remarks",true);
			setLocked("cmplx_emp_ver_sp2_doj_remarks",false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_doj_remarks",false);
			setLocked("cmplx_emp_ver_sp2_doj_remarks",true);
		}
	}
	
	if(pId=='SupplementCardDetails_Text1')
{  var enroll=getNGValue(pId);
	var length1=enroll.length;
	var start=length1-9;
	if((length1>=9 && length1<=11))
	{
	var n=enroll.substring(start,length1);
	var number=parseInt(n);
	if(isNaN(number))
	{
	var res1 = enroll.substring(start,start+8);
	var res2=enroll.substring(start+8,length1);
    var f = parseInt(res1);
	var reminder = parseInt(res2);
	if(f%7!=reminder)
	 {
		showAlert('SupplementCardDetails_Text1','Incorrect Skyward Number. Please enter a valid Skyward Number');
	
	 }
	 }
	 else 
	 {
		showAlert('SupplementCardDetails_Text1','Incorrect Skyward Number. Please enter a valid Skyward Number'); 
	 }
	}
	 else 
	 {
		showAlert('SupplementCardDetails_Text1','Incorrect Skyward Number. Please enter a valid Skyward Number'); 
	 }
	}

	if(pId=='cmplx_EmploymentDetails_RegPayment')
	{
		console.log(getNGValue('cmplx_EmploymentDetails_RegPayment'))
		return true;
	}
	
	else if(pId=='cmplx_CustDetailVerification_resno_ver')
	{ 
  if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_resno_upd",false);
	}
	else{
	setNGValueCustom("cmplx_CustDetailVerification_resno_upd","");
	setLocked("cmplx_CustDetailVerification_resno_upd",true);
	
	}
	}
		else if(pId=='cmplx_CustDetailVerification_offtelno_ver')
	{ 
  if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_offtelno_upd",false);
	}
	else{
	setNGValueCustom("cmplx_CustDetailVerification_offtelno_upd","");
	setLocked("cmplx_CustDetailVerification_offtelno_upd",true);
	
	}
	}//cmplx_CustDetailVerification_hcountrytelno_upd
		else if(pId=='cmplx_CustDetailVerification_hcountrytelno_ver')
	{ 
  if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_hcountrytelno_upd",false);
	}
	else{
	setNGValueCustom("cmplx_CustDetailVerification_hcountrytelno_upd","");
	setLocked("cmplx_CustDetailVerification_hcountrytelno_upd",true);
	
	}
	}//
	
		else if(pId=='cmplx_CustDetailVerification_hcontryaddr_ver')
	{ 
  if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_hcountryaddr_upd",false);
	}
	else{
	setNGValueCustom("cmplx_CustDetailVerification_hcountryaddr_upd","");
	setLocked("cmplx_CustDetailVerification_hcountryaddr_upd",true);
	
	}
	}
	
	//sagarika CR change
	else if(pId=='cmplx_OffVerification_hrdnocntctd')
	{
		if(getNGValue(pId)=='Yes')
		{
setVisible("OfficeandMobileVerification_Label9",false);
setVisible("OfficeandMobileVerification_Label3",true);

		setVisible("OfficeandMobileVerification_Label0",false);
		setVisible("OfficeandMobileVerification_Label14",false);
       setLocked("cmplx_OffVerification_offtelnovalidtdfrom",false);//PCAS-2514 sagarika
	    setEnabled('cmplx_OffVerification_hrdcntctname',true);
		setVisible("OfficeandMobileVerification_Label7",true);
		setVisible("cmplx_OffVerification_hrdcntctno",true);
		setVisible("OfficeandMobileVerification_Label13",true);
		setVisible("cmplx_OffVerification_hrdcntctdesig",true);
		setVisible("cmplx_OffVerification_hrdcntctname",true);
		  setVisible("OfficeandMobileVerification_Frame2",true);
		setLocked('cmplx_OffVerification_hrdcntctname',false);
		setLocked("cmplx_OffVerification_hrdcntctdesig",false);
        setLocked("cmplx_OffVerification_colleagueno",true);
	    setLocked("cmplx_OffVerification_colleaguename",true);
		setLocked("cmplx_OffVerification_colleaguedesig",true);
		setLocked("cmplx_OffVerification_fxdsal_ver",false);
        setLocked("cmplx_OffVerification_accpvded_ver",false);
	    setLocked("cmplx_OffVerification_doj_ver",false);
		setVisible("cmplx_OffVerification_colleagueno",false);
		setVisible("cmplx_OffVerification_colleaguename",false);
		setVisible("cmplx_OffVerification_hrdemailid",false);
		setVisible("OfficeandMobileVerification_Label8",false);
        setVisible("cmplx_OffVerification_colleaguedesig",false);
		if(getNGValue('cmplx_EmploymentDetails_LOS')>=0.06)
		{
		setNGValueCustom("cmplx_OffVerification_cnfminjob_ver","NA");
		setLocked("cmplx_OffVerification_cnfminjob_ver",true);
	
		}
		else{
		setLocked("cmplx_OffVerification_cnfminjob_ver",false);	
		setEnabled("cmplx_OffVerification_cnfminjob_ver",true);	
		setNGValueCustom("cmplx_OffVerification_cnfminjob_ver","--Select--");
		}
		setLocked("cmplx_OffVerification_desig_ver",false);
		setEnabled("cmplx_OffVerification_fxdsal_ver",true);
        setEnabled("cmplx_OffVerification_accpvded_ver",true);
	    setEnabled("cmplx_OffVerification_doj_ver",true);
		//setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
		setEnabled("cmplx_OffVerification_desig_ver",true);
	//setNGValueCustom("cmplx_OffVerification_hrdnocntctd","--Select--");
		setNGValueCustom("cmplx_OffVerification_hrdemailverified","--Select--")
		setNGValueCustom("cmplx_OffVerification_colleaguenoverified","--Select--");
		setLocked("cmplx_OffVerification_colleaguenoverified",true);
		setNGValueCustom("cmplx_OffVerification_fxdsal_ver","");//sagarika PCAS-3225
		setNGValueCustom("cmplx_OffVerification_accpvded_ver","");
		setNGValueCustom("cmplx_OffVerification_desig_ver","");
		setNGValueCustom("cmplx_OffVerification_doj_ver","");
		////setNGValueCustom("cmplx_OffVerification_cnfminjob_ver","--Select--");
		setNGValueCustom("cmplx_OffVerification_fxdsal_upd","");
		setNGValueCustom("cmplx_OffVerification_accpvded_upd","--Select--");
		setNGValueCustom("cmplx_OffVerification_OthDesign_Upt","");//pcasp-2348
		setNGValueCustom("cmplx_OffVerification_desig_upd","--Select--");//pcasp-2348
		setNGValueCustom("cmplx_OffVerification_doj_upd","");
		setNGValueCustom("cmplx_OffVerification_cnfrminjob_upd","--Select--");//cmplx_OffVerification_hrdemailid
		setLocked("cmplx_OffVerification_hrdemailid",true);
		setLocked("cmplx_OffVerification_hrdcntctno",false);
		
		setLocked("cmplx_OffVerification_hrdemailverified",true);
		setVisible("cmplx_OffVerification_offtelnovalidtdfrom",true);
			 setVisible("OfficeandMobileVerification_Label12",true);
			// setVisible("OfficeandMobileVerification_Label7",false);
			// setVisible("OfficeandMobileVerification_Label12",false);
		setNGValueCustom("cmplx_OffVerification_hrdemailverified","--Select--");
		}
		else
		{ 
		setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);
		setEnabled('cmplx_OffVerification_hrdcntctname',false);
		setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		 setVisible("OfficeandMobileVerification_Frame2",false);
		  setVisible("OfficeandMobileVerification_Label3",false);
		   setVisible("cmplx_OffVerification_hrdcntctno",false);
		    setVisible("OfficeandMobileVerification_Label7",false);
			 setVisible("cmplx_OffVerification_offtelnovalidtdfrom",false);
			 setVisible("OfficeandMobileVerification_Label7",false);
			 setVisible("OfficeandMobileVerification_Label12",false);
		//Changed by shivang for PCASP-2035
		//setNGValueCustom("cmplx_OffVerification_hrdcntctname","");
		//setNGValueCustom("cmplx_OffVerification_hrdcntctdesig","");
		setLocked("cmplx_OffVerification_colleaguenoverified",false);
		setLocked("cmplx_OffVerification_hrdemailverified",false);
		setLocked("cmplx_OffVerification_colleagueno",true);
		setLocked("cmplx_OffVerification_colleaguename",true);
		setLocked("cmplx_OffVerification_colleaguedesig",true);  
		setVisible("cmplx_OffVerification_hrdcntctdesig",false);
		setVisible("OfficeandMobileVerification_Label13",false);
		setVisible("cmplx_OffVerification_hrdcntctname",false);
		//Changed by shivang for PCASP-2035
		//setNGValueCustom("cmplx_OffVerification_hrdcntctno","");
	if(getNGValue('cmplx_OffVerification_hrdemailverified')!='Yes')
	{
		//setNGValueCustom("cmplx_OffVerification_hrdemailid","");
		
		setLocked("cmplx_OffVerification_fxdsal_ver",true);
		setLocked("cmplx_OffVerification_accpvded_ver",true);
		setLocked("cmplx_OffVerification_desig_ver",true);
		setLocked("cmplx_OffVerification_doj_ver",true);
		setLocked("cmplx_OffVerification_cnfminjob_ver",true);
		setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		setLocked("cmplx_OffVerification_hrdcntctname",true);
	}
		//setLocked("cmplx_OffVerification_cnfminjob_ver",true);
		setLocked("cmplx_OffVerification_hrdcntctno",true);
		//setLocked("cmplx_OffVerification_hrdemailid",true);
	
	setLocked("cmplx_OffVerification_hrdemailverified",false);	
	//setLocked("cmplx_OffVerification_desig_ver",true);
		
	
setLocked("cmplx_OffVerification_colleaguenoverified",false);
	
     }
	}
	else if(pId=='cmplx_OffVerification_colleaguenoverified')
	{
		if(getNGValue(pId)=='Yes')
		{//sagarika PCAS-3225
		setNGValueCustom("cmplx_OffVerification_fxdsal_ver","");
		setNGValueCustom("cmplx_OffVerification_accpvded_ver","");
		setNGValueCustom("cmplx_OffVerification_desig_ver","");
		setNGValueCustom("cmplx_OffVerification_doj_ver","");
setVisible("cmplx_OffVerification_colleagueno",true);//
	    setVisible("cmplx_OffVerification_colleaguename",true);
		setVisible("OfficeandMobileVerification_Label3",false);
		setVisible("OfficeandMobileVerification_Label10",true);
		setVisible("cmplx_OffVerification_colleaguedesig",true);
		setVisible("cmplx_OffVerification_offtelnovalidtdfrom",false);
		setVisible("OfficeandMobileVerification_Label12",false);
		setVisible("OfficeandMobileVerification_Frame2",false);
        setVisible("cmplx_OffVerification_hrdcntctname",false);
		setVisible("cmplx_OffVerification_hrdcntctdesig",false);
		setVisible("cmplx_OffVerification_hrdemailid",false);
		setVisible("OfficeandMobileVerification_Label8",false);
		setVisible("OfficeandMobileVerification_Label13",false);
		setVisible("OfficeandMobileVerification_Label8",false);
	    setVisible("OfficeandMobileVerification_Label9",true);
		setVisible("OfficeandMobileVerification_Label0",true);
		setVisible("OfficeandMobileVerification_Label14",true);		
		//For PCASP-2035
		//setNGValue("cmplx_OffVerification_hrdcntctname","");
		//setNGValue("cmplx_OffVerification_hrdcntctdesig","");
		//setNGValue("cmplx_OffVerification_hrdemailid","");
		
		setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);//PCAS-2514 sagarika
	   setLocked("cmplx_OffVerification_hrdcntctname",true);
	    setLocked("cmplx_OffVerification_hrdcntctno",true);
		setLocked("cmplx_OffVerification_hrdcntctname",true);
		setLocked("cmplx_OffVerification_hrdemailid",true);
		setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		setLocked("cmplx_OffVerification_hrdemailverified",true);
		setLocked("cmplx_OffVerification_fxdsal_ver",true);
		setEnabled("cmplx_OffVerification_fxdsal_ver",false);;
        setLocked("cmplx_OffVerification_accpvded_ver",true);
	    setLocked("cmplx_OffVerification_doj_ver",true);
		setLocked("cmplx_OffVerification_cnfminjob_ver",true);
		setLocked("cmplx_OffVerification_desig_ver",true);
		setLocked("cmplx_OffVerification_colleagueno",false);
	    setLocked("cmplx_OffVerification_colleaguename",false);
		setLocked("cmplx_OffVerification_colleaguedesig",false);
		setLocked("cmplx_OffVerification_hrdnocntctd",true);
		setNGValueCustom("cmplx_OffVerification_hrdnocntctd","--Select--");
		//setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
	
		//setNGValueCustom("cmplx_OffVerification_cnfminjob_ver","--Select--");
		setNGValueCustom("cmplx_OffVerification_fxdsal_upd","");
		setNGValueCustom("cmplx_OffVerification_desig_upd","--Select--");//pcasp-2348
		setNGValueCustom("cmplx_OffVerification_accpvded_upd","--Select--");
		setNGValueCustom("cmplx_OffVerification_OthDesign_Upt","");//pcasp-2348
		setNGValueCustom("cmplx_OffVerification_doj_upd","");
		setNGValueCustom("cmplx_OffVerification_cnfrminjob_upd","--Select--");
		setNGValueCustom("cmplx_OffVerification_hrdnocntctd","--Select--");
		setNGValueCustom("cmplx_OffVerification_hrdemailverified","--Select--");
		
	    }
		else
		{setVisible("cmplx_OffVerification_colleagueno",false);
	    setVisible("cmplx_OffVerification_colleaguename",false);
		setVisible("cmplx_OffVerification_colleaguedesig",false);
setVisible("OfficeandMobileVerification_Label9",false);
		setVisible("OfficeandMobileVerification_Label0",false);
		setVisible("OfficeandMobileVerification_Label14",false);
setVisible("OfficeandMobileVerification_Label10",false);		
			setLocked("cmplx_OffVerification_hrdemailverified",false);
			setLocked("cmplx_OffVerification_hrdnocntctd",false);
		setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);//PCAS-2514 sagarika
		setLocked("cmplx_OffVerification_hrdcntctname",true);//sagarika for missed out field
	    setLocked("cmplx_OffVerification_hrdcntctno",true);
		setLocked("cmplx_OffVerification_hrdcntctname",true);
		setLocked("cmplx_OffVerification_hrdemailid",true);
		setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		setLocked("cmplx_OffVerification_hrdemailverified",false);
		
	
		setLocked("cmplx_OffVerification_colleagueno",true);
	    setLocked("cmplx_OffVerification_colleaguename",true);
		setLocked("cmplx_OffVerification_colleaguedesig",true);
		setLocked("cmplx_OffVerification_hrdnocntctd",false);
		
		//setLocked("cmplx_OffVerification_hrdemailverified",false);
		
     }
	}
	if(pId=='cmplx_OffVerification_hrdemailverified')
	{
		if(getNGValue(pId)=='Yes')
		{ setVisible("cmplx_OffVerification_offtelnovalidtdfrom",false);
			setVisible("OfficeandMobileVerification_Label12",false); 
			setVisible("OfficeandMobileVerification_Label12",false);
			setVisible("cmplx_OffVerification_hrdcntctno",false);
			setVisible("OfficeandMobileVerification_Label7",false); 
			setVisible("cmplx_OffVerification_hrdemailid",true);
			setVisible("OfficeandMobileVerification_Label8",true);
			setVisible("OfficeandMobileVerification_Frame2",true);
			setVisible("cmplx_OffVerification_hrdcntctname",true);
			setVisible("OfficeandMobileVerification_Label3",true);
			setVisible("cmplx_OffVerification_hrdcntctdesig",true);
			setVisible("OfficeandMobileVerification_Label13",true);
		setLocked("cmplx_OffVerification_colleagueno",true);
	    setLocked("cmplx_OffVerification_colleaguename",true);
		setLocked("cmplx_OffVerification_colleaguedesig",true);
		//Changed by shivang for PCASP-2035
	    //setNGValue("cmplx_OffVerification_colleaguename","");
		//setNGValue("cmplx_OffVerification_colleagueno","");
		//setNGValue("cmplx_OffVerification_colleaguedesig","");
		setLocked("cmplx_OffVerification_fxdsal_ver",false);
		setEnabled("cmplx_OffVerification_fxdsal_ver",true);
		setLocked("cmplx_OffVerification_accpvded_ver",false);
		setEnabled("cmplx_OffVerification_accpvded_ver",true);
		setLocked("cmplx_OffVerification_desig_ver",false);
		setEnabled("cmplx_OffVerification_desig_ver",true);
		setLocked("cmplx_OffVerification_doj_ver",false);
		setEnabled("cmplx_OffVerification_doj_ver",true);
		if(getNGValue('cmplx_EmploymentDetails_LOS')>= 0.06)
		{
		setNGValueCustom("cmplx_OffVerification_cnfminjob_ver","NA");
		setLocked("cmplx_OffVerification_cnfminjob_ver",true);
		
		
		//setNGValueCustom("cmplx_OffVerification_cnfminjob_ver","--Select--");
		}
		else{
		setLocked("cmplx_OffVerification_cnfminjob_ver",false);	
		setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
		setNGValueCustom("cmplx_OffVerification_cnfminjob_ver","--Select--");
		}
		setLocked("cmplx_OffVerification_hrdcntctno",true);
	    setLocked("cmplx_OffVerification_hrdemailid",false);
		 setLocked("cmplx_OffVerification_hrdcntctdesig",false);
		//setLocked("cmplx_OffVerification_hrdcntctname",false);
	    setNGValueCustom("cmplx_OffVerification_colleaguenoverified","--Select--");
		setLocked("cmplx_OffVerification_colleaguenoverified",true);
		setNGValueCustom("cmplx_OffVerification_hrdnocntctd","--Select--");
		setLocked("cmplx_OffVerification_hrdnocntctd",true);
		   setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);//PCAS-2514 sagarika
	  setLocked('cmplx_OffVerification_hrdcntctname',false);
	   setEnabled('cmplx_OffVerification_hrdcntctname',true);
		setLocked("cmplx_OffVerification_hrdcntctdesig",false);
		setVisible("OfficeandMobileVerification_Label14",false);
		setVisible("OfficeandMobileVerification_Frame2",true);
		//setNGValue("cmplx_OffVerification_hrdnocntctd","--Select--");
		//setLocked("cmplx_OffVerification_hrdnocntctd",true);
		//setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		
		}
		else{ setVisible("cmplx_OffVerification_hrdcntctname",false);
		setVisible("cmplx_OffVerification_hrdcntctdesig",false);
		setVisible("OfficeandMobileVerification_Label3",false);
		setVisible("cmplx_OffVerification_hrdemailid",false);
		setVisible("OfficeandMobileVerification_Label8",false);
		setVisible("OfficeandMobileVerification_Label13",false);
		setVisible("OfficeandMobileVerification_Label8",false);
			
			setVisible("OfficeandMobileVerification_Frame2",false);
				setNGValueCustom("cmplx_OffVerification_fxdsal_ver","");//sagarika PCAS-3225
		setNGValueCustom("cmplx_OffVerification_accpvded_ver","");
		setNGValueCustom("cmplx_OffVerification_desig_ver","");
		setNGValueCustom("cmplx_OffVerification_doj_ver","");
		//Changed by shivang for PCASP-2035
		//setNGValue("cmplx_OffVerification_hrdcntctname","");
		//setNGValue("cmplx_OffVerification_hrdcntctdesig","");
		//setNGValue("cmplx_OffVerification_hrdemailid","");
			setLocked("cmplx_OffVerification_colleaguenoverified",false);
			setLocked("cmplx_OffVerification_hrdnocntctd",false);
			setLocked("cmplx_OffVerification_colleagueno",true);
	    setLocked("cmplx_OffVerification_colleaguename",true);
		setLocked("cmplx_OffVerification_colleaguedesig",true);
		setLocked("cmplx_OffVerification_hrdcntctno",true);
	    setLocked("cmplx_OffVerification_hrdemailid",true);	
		setLocked("cmplx_OffVerification_fxdsal_ver",true);
		setEnabled("cmplx_OffVerification_fxdsal_ver",false);;
        setLocked("cmplx_OffVerification_accpvded_ver",true);
	    setLocked("cmplx_OffVerification_doj_ver",true);
		setLocked("cmplx_OffVerification_cnfminjob_ver",true);
		setLocked("cmplx_OffVerification_desig_ver",true);
		setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		setLocked("cmplx_OffVerification_hrdcntctname",true);	

		
		
	//	setLocked("cmplx_OffVerification_colleaguenoverified",false);
		//setLocked("cmplx_OffVerification_hrdnocntctd",false);
		//setLocked("cmplx_OffVerification_hrdcntctdesig",true);	
		//setLocked("cmplx_OffVerification_hrdcntctname",false);
		}
	}
	if(pId=='cmplx_EligibilityAndProductInfo_FinalLimit') // by jahnavi for loan label
	{
		setNGValue("LoanLabel",getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
	}
	if(pId=='cmplx_OffVerification_doj_ver')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_OffVerification_doj_upd",true);
			setLocked("cmplx_OffVerification_doj_upd",false);
		}
		else{
			setEnabled("cmplx_OffVerification_doj_upd",true); //Added by Nikhil to make field in enabled state
			setNGValueCustom("cmplx_OffVerification_doj_upd","");	
			setLocked("cmplx_OffVerification_doj_upd",true);			
		}
	}
	if(pId=='cmplx_OffVerification_fxdsal_ver')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_OffVerification_fxdsal_upd",true);
			setLocked("cmplx_OffVerification_fxdsal_upd",false);
		}
		else{
			setEnabled("cmplx_OffVerification_fxdsal_upd",true); //Added by Nikhil to make field in enabled state
			setNGValueCustom("cmplx_OffVerification_fxdsal_upd","");
			setLocked("cmplx_OffVerification_fxdsal_upd",true);
		
		}
	}
	if(pId=='cmplx_OffVerification_cnfminjob_ver')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_OffVerification_cnfrminjob_upd",true);
			setLocked("cmplx_OffVerification_cnfrminjob_upd",false);
		}
		else{
			setEnabled("cmplx_OffVerification_cnfrminjob_upd",true); //Added by Nikhil to make field in enabled state
			setNGValueCustom("cmplx_OffVerification_cnfrminjob_upd","--Select--");
			setLocked("cmplx_OffVerification_cnfrminjob_upd",true);
		}
	}
	if(pId=='cmplx_OffVerification_accpvded_ver')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_OffVerification_accpvded_upd",true);
			setLocked("cmplx_OffVerification_accpvded_upd",false);
		}
		else{
			setEnabled("cmplx_OffVerification_accpvded_upd",true); //Added by Nikhil to make field in enabled state
			setNGValueCustom("cmplx_OffVerification_accpvded_upd","--Select--");	
			setLocked("cmplx_OffVerification_accpvded_upd",true);
		}
	}
	if(pId=='cmplx_OffVerification_desig_ver')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_OffVerification_desig_upd",true);
			setEnabled("cmplx_OffVerification_OthDesign_Upt",true);
			setLocked("cmplx_OffVerification_desig_upd",false);
			setLocked("cmplx_OffVerification_OthDesign_Upt",false);
		}
		else{
			setEnabled("cmplx_OffVerification_desig_upd",true); //Added by Nikhil to make field in enabled state
			setNGValueCustom("cmplx_OffVerification_desig_upd"," ");
			setLocked("cmplx_OffVerification_desig_upd",true);
			setEnabled("cmplx_OffVerification_OthDesign_Upt",true); 
			setNGValueCustom("cmplx_OffVerification_OthDesign_Upt"," ");
			setLocked("cmplx_OffVerification_OthDesign_Upt",true);
		}
	}
		/*	if(pId=='cmplx_CustDetailVerification_email2_ver')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setEnabled("cmplx_CustDetailVerification_email2_upd",true);
	setLocked("cmplx_CustDetailVerification_email2_upd",false);
	}
	else{
	setLocked("cmplx_CustDetailVerification_email2_upd",true);
	}
	}
	if(pId=='cmplx_CustDetailVerification_email1_ver')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_email1_upd",false);
	}
	else{
	setLocked("cmplx_CustDetailVerification_email1_upd",true);
	}
	} for PCAS 2592 done by sagarika*/
	if(pId=='cmplx_CustDetailVerification_persorcompPOBox_ver')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_persorcompPOBox_upd",false);
	}
	else{
		setNGValueCustom("cmplx_CustDetailVerification_persorcompPOBox_upd","");
		setLocked("cmplx_CustDetailVerification_persorcompPOBox_upd",true);
	}
	}
	if(pId=='cmplx_CustDetailVerification_emirates_ver')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_emirates_upd",false);
	}
	else{
		setNGValueCustom("cmplx_CustDetailVerification_emirates_upd","--Select--");
	setLocked("cmplx_CustDetailVerification_emirates_upd",true);
	}
	}
		if(pId=='cmplx_CustDetailVerification_POBoxno_ver')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_POBoxno_upd",false);
	}
	else{
	setNGValueCustom("cmplx_CustDetailVerification_POBoxno_upd","");	
	setLocked("cmplx_CustDetailVerification_POBoxno_upd",true);
	}
	}
if(pId=='SupplementCardDetails_ReceivedDate')
{
	validateFutureDate(pId);
}
if(pId=='cmplx_BussVerification_contctTelChk')
{
if(getNGValue(pId)=='Yes')
{
setLocked('cmplx_BussVerification_desigofverifier',false);
setLocked('cmplx_BussVerification_cntctpersonname',false);
setLocked('cmplx_BussVerification_cntctphone',false);
}
else
{
setLocked('cmplx_BussVerification_desigofverifier',true);
setLocked('cmplx_BussVerification_cntctpersonname',true);
setLocked('cmplx_BussVerification_cntctphone',true);
}
}
//Done by sagarika for CR-
if(pId=='cmplx_EmploymentDetails_CurrEmployer')
{
	var activityName=window.parent.stractivityName;
	//Commented by nikhil for PCAS-2484
	/*if(activityName=='CAD_Analyst1' && getNGValue(pId)=='CN')
	{
		setEnabled("cmplx_EmploymentDetails_Indus_Micro",true);
	    setEnabled("cmplx_EmploymentDetails_Indus_Macro",true);
	}*/
}//end

if(pId=='cmplx_Customer_Designation')
{
	//alert("s change");
	setNGValue('cmplx_EmploymentDetails_Designation',getNGValue('cmplx_cust_ver_sp2_ContactedOn'));
	return true;
}
if(pId=='TargetSegmentCode')
{
	/*if("DOC"==getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",1,22) && "SE"==getNGValue("Sub_Product"))
		{
			setVisible("CompanyDetails_Label29", true);
			setVisible("headOffice", true);
		}
		else
		{
			setVisible("CompanyDetails_Label29", false);
			setVisible("headOffice", false);
		}*/
}

	//cmplx_EmploymentDetails_Designation
		//setEnabled('cmplx_EmploymentDetails_MotorInsurance',true);
		if(pId=='cmplx_cust_ver_sp2_ContactedOn')
		{
			if(getNGValue('cmplx_cust_ver_sp2_ContactedOn')=='Mismatch')
			{
				setEnabled('cmplx_cust_ver_sp2_Mobile',true);
			}
			else{
				setEnabled('cmplx_cust_ver_sp2_Mobile',false);
			}
		}

if(pId=='cmplx_EmploymentDetails_PremType')
{
if(getNGValue('cmplx_EmploymentDetails_PremType')=='Half yearly' || getNGValue('cmplx_EmploymentDetails_PremType')=='Annually')
{
setEnabled('cmplx_EmploymentDetails_MinimumWait',true);
}
else{
	setNGValue('cmplx_EmploymentDetails_MinimumWait',false);
setEnabled('cmplx_EmploymentDetails_MinimumWait',false);	
}
}
/*if(pId=='cmplx_emp_ver_sp2_salary_break_drop')
{
if(getNGValue(pId)=="Mismatch")
{
 com.newgen.omniforms.formviewer.setLockedCustom("cmplx_emp_ver_sp2_salary_break_remarks",false);
 }
 else{
 com.newgen.omniforms.formviewer.setLockedCustom("cmplx_emp_ver_sp2_salary_break_remarks",true);

}
}*/
	
	//sagarika
	if(pId=='cmplx_fieldvisit_sp2_drop1')
	{//alert("value");
	if(getNGValue(pId)=="Yes")
	{//alert("yes");
	 com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_drop2",true);
	  com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_drop3",true);
	   com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_visit_date",true);
	    com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_v_time",true);
		 com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
		 com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_visit_rec_time",true);
}
	else if(getNGValue(pId)=="No")
	{//alert("no");
	 com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_drop2",false);
	  com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_drop3",false);
	   com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_visit_date",false);
	    com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_v_time",false);
		 com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_rep_receivedDate",false);
		 com.newgen.omniforms.formviewer.setEnabledCustom("cmplx_fieldvisit_sp2_field_visit_rec_time",false);
		  com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_visit_date",true);
	     com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_v_time",true);
		 com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
		 com.newgen.omniforms.formviewer.NGClear("cmplx_fieldvisit_sp2_field_visit_rec_time",true);
	}
	}
// Changed by Rajan to implement FCU changes in Customer Detail Verification
	if(pId=='cmplx_cust_ver_sp2_application_type')
	{
	if(getNGValue(pId)=="Salaried")
	{
	//alert("salaried");
        com.newgen.omniforms.formviewer.setVisible("Cust_ver_sp2_Frame2",true);
		com.newgen.omniforms.formviewer.setVisible("Cust_ver_sp2_Frame3",false);
	}
	else if(getNGValue(pId)=="Self Employed")
	{
	//alert("self");
	    com.newgen.omniforms.formviewer.setVisible("Cust_ver_sp2_Frame2",false);
		com.newgen.omniforms.formviewer.setVisible("Cust_ver_sp2_Frame3",true);
	}
	else{
	   com.newgen.omniforms.formviewer.setVisible("Cust_ver_sp2_Frame2",true);
	   com.newgen.omniforms.formviewer.setVisible("Cust_ver_sp2_Frame3",false);
	}
	}
	// Below added by Rajan for Customer Info Validation in FPU 
	
	if(pId=='cmplx_CustDetailverification1_Application_No_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_Application_No_remarks",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_Application_No_remarks",true);
	}}
	
    if(pId=='cmplx_CustDetailverification1_CIF_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_CIF_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_CIF_remark",true);
	}}
	
    if(pId=='cmplx_CustDetailverification1_cust_name_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_cust_name_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_cust_name_remark",true);
	}}
	
    if(pId=='cmplx_CustDetailverification1_DOB_ver')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_Dob_update",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_Dob_update",true);
	}}

    if(pId=='cmplx_CustDetailverification1_MobNo1_veri')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_MobNo1_updates",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_MobNo1_updates",true);
	}}

    if(pId=='cmplx_CustDetailverification1_Nationality_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_Nationality_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_Nationality_remark",true);
	}}

    if(pId=='cmplx_CustDetailverification1_Visa_No_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_Visa_No_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_Visa_No_remark",true);
	}}

    if(pId=='cmplx_CustDetailverification1_Emirate_Id_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_Emirate_Id_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_Emirate_Id_remark",true);
	}}
	
	if(pId=='cmplx_CustDetailverification1_VisaIssue_Date_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_VisaIssue_Date_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_VisaIssue_Date_remark",true);
	}}

	if(pId=='cmplx_CustDetailverification1_VisaExpiry_date_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_VisaExpiry_date_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_VisaExpiry_date_remark",true);
	}}
		
	if(pId=='cmplx_CustDetailverification1_EmirateIDExp_Date_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_EmirateIDExp_Date_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_EmirateIDExp_Date_remark",true);
	}}
	
		
	if(pId=='cmplx_CustDetailverification1_Passport_NO_drop')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLockedCustom("cmplx_CustDetailverification1_Passport_NO_remark",false);
	}
	else{
		setLockedCustom("cmplx_CustDetailverification1_Passport_NO_remark",true);
	}}
	
	
		if(pId=='cmplx_CustDetailVerification_dob_verification')
	{
	if(getNGValue(pId)=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_dob_upd",false);
	}
	else{
		setNGValueCustom("cmplx_CustDetailVerification_dob_upd","");
	setLocked("cmplx_CustDetailVerification_dob_upd",true);
	
	}
	}
	 if(pId=="cmplx_fieldvisit_sp2_drop2")
		 {
		 //alert("change");
		 return true;
		 }
		//*var   re = (1[012]|[1-9]):[0-5][0-9 
		// Changed by Rajan to implement FCU changes in Employment Verification
	if(pId=='cmplx_emp_ver_sp2_application_type')
	{
	if(getNGValue('EmploymentType')=="Salaried")
	{
	//alert("salaried");
        com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame2",true);
		com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame3",false);
	}
	else if(getNGValue('EmploymentType')=="Self Employed")
	{
	//alert("self");
	    com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame2",false);
		com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame3",true);
	}
	else{
	   com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame2",true);
	   com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame3",false);
	}
	}

	// ++ below code already present - 06-10-2017 notedesc change for note code and Feedback
	if(pId=="NotepadDetails_notedesc")
	{
		return true;
	}
	
	//for PCSP-67
	if(pId=="ExtLiability_contractType")
	{
		return true;
	}
		//for 5,2 fields
	 if(pId=='CompanyDetails_EffectiveLOB')
	 {
	 /*var value = getNGValue(pId);
		if(value.indexOf(',')>-1){
			value = replaceAll(value,',','');		
		}		
		if(value.indexOf('.')>-1){
			var parts=value.split('.');
			if(keyCode_str=='110' || keyCode_str=='190'){
				return false;
			}
			else if(parts[1].length==2){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}else{
			if(value.length==5 && !(keyCode_str=='110' || keyCode_str=='190')){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}
		return true;*/
		YYMM(pId,'','');
		//below validation added by akshay for proc 6718
		var YY=getNGValue(pId).split('.')[0];
		var MM=getNGValue(pId).split('.')[1];
		if(YY.length>2 || MM.length>2)
		{
			showAlert(pId,alerts_String_Map['VAL256']);
			setNGValueCustom(pId,'');
		}
	 }
	//ended by yash 
	//12th september
	if(pId=='CC_Creation_CheckBox1')
	{
		return true;
	}
	if(pId=='cmplx_DEC_FeedbackStatus'){
		return true;

	}
	// ++ above code already present - 06-10-2017 notedesc change for note code and Feedback
	//change by saurabh for PCSP-332.
	if(pId=='AlternateContactDetails_AirArabiaIdentifier'){
		validateMail1(pId);
	}
	//added by saurabh1 for enroll
	if(pId=='AlternateContactDetails_EnrollRewardsIdentifier'){
		//validateMail1(pId);
		return true;
	}
	//saurabh1 end for enroll
	 if(pId=='cmplx_Customer_Nationality')
	{
		//Deepak Changes done for PCAS-2799
		if(getNGValue(pId)!='--Select--')
		{
			var age=getNGValue("cmplx_Customer_age");
			if(age<18.06 && getNGValue("cmplx_Customer_Nationality")=='AE')
			{
			   showAlert(pId,'Minor Customer not Applicable for Credit Card');
				setNGValueCustom(pId,'--Select--');
				return false;
			}
			else if(age<21 && getNGValue("cmplx_Customer_Nationality")!='AE')
			{
				showAlert(pId,'Minor Customer not Applicable for Credit Card');
				setNGValueCustom(pId,'--Select--');
				return false;
			}
		}
		
		var GCC="BH,IQ,KW,OM,QA,SA,AE";
		if(GCC.indexOf(getNGValue("cmplx_Customer_Nationality"))!=-1)
		{
			setNGValueCustom('cmplx_Customer_GCCNational','Y');
		}
		else
		{
			setNGValueCustom('cmplx_Customer_GCCNational','N');
		}
		setNGValueCustom('PartMatch_nationality',getNGValue(pId));
	}
	
	
	// added by abhishek
	if(pId=="cmplx_Customer_NEP" || pId=="cmplx_Customer_NTB")
	{
		if(getNGValue("cmplx_Customer_NTB")==false && getNGValue("cmplx_Customer_NEP")==''){
				setVisible("IncomingDoc_ViewSIgnature",true);
				setVisible("IncomingDoc_UploadSig",false);
		}
		
		else if(getNGValue("cmplx_Customer_NEP")!='' || getNGValue("cmplx_Customer_NTB")==true){
				setVisible("IncomingDoc_ViewSIgnature",false);
				setVisible("IncomingDoc_UploadSig",true);
	}
	}
	//validation for statement cycle of Card Details Tanshu Aggarwal(17/08/2017)
		if(pId=='cmplx_CardDetails_Statement_cycle'){
			if(getNGValue(pId)<1 || getNGValue(pId)>31 && getNGValue(pId)!="")
			showAlert('cmplx_CardDetails_Statement_cycle',alerts_String_Map['VAL102']);
		}
	//validation for statement cycle of Card Details Tanshu Aggarwal(17/08/2017)
		
	else if(pId=='cmplx_Customer_FirstNAme' || pId=='cmplx_Customer_MiddleNAme' || pId=='cmplx_Customer_LastNAme'){
		var short_name = getNGValue('cmplx_Customer_FirstNAme').charAt(0)+" "+getNGValue('cmplx_Customer_MiddleNAme').charAt(0)+" "+getNGValue('cmplx_Customer_LastNAme').charAt(0);
		setNGValueCustom('cmplx_Customer_Shortname',short_name);
		setNGValueCustom('PartMatch_fname',getNGValue('cmplx_Customer_FirstNAme'));
		setNGValueCustom('PartMatch_lname',getNGValue('cmplx_Customer_LastNAme'));
		setNGValueCustom('PartMatch_funame',getNGValue('cmplx_Customer_FirstNAme') + ' ' + getNGValue('cmplx_Customer_LastNAme'));
	}
	//change by saurabh on 24th Dec.
	//Change by aman for PCSP-101
	else if(pId=='cmplx_CustDetailverification1_Dob_update' || pId=='FinacleCore_ReturnDate' || pId=='InwardTT_date'){
	
		validateFutureDateexcCurrent(pId);
	}
		//Change by aman for PCSP-101
	//change by saurabh for understanding gap on 17 dec 18 start.
	else if(pId=='cmplx_CustDetailVerification_mobno1_ver'){
	var mob1 = getNGValue(pId);
		if(mob1=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_mobno1_upd",false);
	}
	if(mob1!='Mismatch'){
	setNGValueCustom("cmplx_CustDetailVerification_mobno1_upd","");
	setLocked("cmplx_CustDetailVerification_mobno1_upd",true);
	
	}
		if((mob1=='Yes' || mob1=='No' || mob1=='Mismatch') ){
			setNGValueCustom('cmplx_CustDetailVerification_mobno2_ver','NA');
			setLocked("cmplx_CustDetailVerification_mobno2_upd",true);
			
		}
		else if(!(mob1=='Yes' || mob1=='No' || mob1=='Mismatch')){
			var mob2=getNGValue('cmplx_CustDetailVerification_mobno2_ver');
			if(mob2=='NA'){
				setNGValueCustom('cmplx_CustDetailVerification_mobno2_ver','');
			
			}
		}
		
	}
	else if(pId=='cmplx_CustDetailVerification_mobno2_ver'){
	var mob2 = getNGValue(pId);
		if(mob2=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_mobno2_upd",false);
	}
	if(mob2!='Mismatch')
	{
	setNGValueCustom("cmplx_CustDetailVerification_mobno2_upd","");
	setLocked("cmplx_CustDetailVerification_mobno2_upd",true);
    }
		if((mob2=='Yes' || mob2=='No' || mob2=='Mismatch') && getNGValue('cmplx_CustDetailVerification_mobno1_ver')==''){
			setNGValueCustom('cmplx_CustDetailVerification_mobno1_ver','NA');
			setLocked("cmplx_CustDetailVerification_mobno1_upd",true);
		}
		else if(!(mob2=='Yes' || mob2=='No' || mob2=='Mismatch')){
			var mob1=getNGValue('cmplx_CustDetailVerification_mobno1_ver');
			if(mob1=='NA'){
				setNGValueCustom('cmplx_CustDetailVerification_mobno1_ver','');
			}
		}
	}
	else if(pId=='cmplx_CustDetailVerification_Mother_name_ver')
	{
	 if(getNGValue(pId)=='Mismatch')
	  {
	  setLocked("cmplx_CustDetailVerification_Mother_name_upd",false);
	  }
	 else
		 {
		 setNGValueCustom("cmplx_CustDetailVerification_Mother_name_upd","");
		 setLocked("cmplx_CustDetailVerification_Mother_name_upd",true);
		 }
	}
	else if(pId=='cmplx_CustDetailVerification_email1_ver'){
	var mob1 = getNGValue(pId);
	if(mob1=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_email1_upd",false);
	}
	if(mob1!='Mismatch'){
	setNGValueCustom("cmplx_CustDetailVerification_email1_upd","");
	setLocked("cmplx_CustDetailVerification_email1_upd",true);
	
	}
		if((mob1=='Yes' || mob1=='No' || mob1=='Mismatch')){//PCAS-2592 sagarika
			setNGValueCustom('cmplx_CustDetailVerification_email2_ver','NA');
			setLocked("cmplx_CustDetailVerification_email2_upd","true");
		}
		else if(!(mob1=='Yes' || mob1=='No' || mob1=='Mismatch')){
			var mob2=getNGValue('cmplx_CustDetailVerification_email2_ver');
			if(mob2=='NA'){
				setNGValueCustom('cmplx_CustDetailVerification_email2_ver','');
					setLocked("cmplx_CustDetailVerification_email2_upd","true");
			}
		}
	}
	else if(pId=='cmplx_CustDetailVerification_email2_ver'){
	var mob2 = getNGValue(pId);
	if(mob2=='Mismatch')
	{
	setLocked("cmplx_CustDetailVerification_email2_upd",false);
	}
	if(mob2!='Mismatch'){
	setNGValueCustom("cmplx_CustDetailVerification_email2_upd","");
	setLocked("cmplx_CustDetailVerification_email2_upd",true);
	
	}
		if((mob2=='Yes' || mob2=='No' || mob2=='Mismatch')){//PCAS-2592 sagarika
			setNGValueCustom('cmplx_CustDetailVerification_email1_ver','NA');
			setLocked("cmplx_CustDetailVerification_email1_upd","true");
		}
		else if(!(mob2=='Yes' || mob2=='No' || mob2=='Mismatch')){
			var mob1=getNGValue('cmplx_CustDetailVerification_email1_ver');
			if(mob1=='NA'){
				setNGValueCustom('cmplx_CustDetailVerification_email1_ver','');
			}
		}
	}
	//change by saurabh for understanding gap on 17 dec 18 end.
	//changed by shivang for PCASP-1547
	if(pId=='cmplx_CC_Loan_ModeOfSI'){
		if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='F'){
				setLockedCustom('cmplx_CC_Loan_FlatAMount',false);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',true);
				setNGValue('cmplx_CC_Loan_SI_Percentage','');
				setNGValueCustom('cmplx_CC_Loan_FlatAMount','100');
			}
			else if(getNGValue('cmplx_CC_Loan_ModeOfSI')==''){
				setLockedCustom('cmplx_CC_Loan_FlatAMount',false);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
				setNGValue('cmplx_CC_Loan_FlatAMount','');
				setNGValue('cmplx_CC_Loan_SI_Percentage','');
				setNGValueCustom('cmplx_CC_Loan_FlatAMount','');

			}
			else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='P'){
				setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
				setNGValue('cmplx_CC_Loan_FlatAMount','');
				setNGValue('cmplx_CC_Loan_SI_Percentage','100');
				setNGValueCustom('cmplx_CC_Loan_FlatAMount','');
			}
			//PCASP-3177
			else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='M'){
				setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',true);
				setNGValue('cmplx_CC_Loan_FlatAMount','');
				setNGValue('cmplx_CC_Loan_SI_Percentage','3');
				setNGValueCustom('cmplx_CC_Loan_FlatAMount','');
			}
		else{
				setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
				setNGValueCustom('cmplx_CC_Loan_FlatAMount','');
			}
		}
		//PCASP-3186
		else if(pId=='cmplx_CC_Loan_SIOnDueDate'){
			if(getNGValue(pId)=='Yes'||getNGValue(pId)=='YES'){
				setNGValue('cmplx_CC_Loan_SI_day','');
				setLockedCustom('cmplx_CC_Loan_SI_day',true);				
			}
			else{
				setNGValue('cmplx_CC_Loan_SI_day','0');
				setLockedCustom('cmplx_CC_Loan_SI_day',false);
			}			
		}
		else if( pId=='cmplx_EmploymentDetails_LOSPrevious' ){
		//YearsinUAE(pId);
		if(getNGValue(pId)==''){
			setNGValueCustom(pId,'00.00');
		}
		YYMM(pId,'','');
		//below validation added by aman for drop 5
		var YY=getNGValue(pId).split('.')[0];
		var MM=getNGValue(pId).split('.')[1];
		
		if(YY.length>2 || parseInt(MM)>12)
		{
			showAlert(pId,alerts_String_Map['VAL256']);
			setNGValueCustom(pId,'00.00');
		}
	}	
	
	/*else if(pId=='cmplx_Customer_FIrstNAme' || pId=='cmplx_Customer_MiddleNAme' || pId=='cmplx_Customer_LAstNAme'  || pId=='cmplx_Customer_Nationality' || pId=='cmplx_Customer_PAssportNo')
		setLockedCustom('Customer_FetchDetails',false);*/
	else if(pId=='SupplementCardDetails_ApprovedLimit')
	{
		var limit = com.newgen.omniforms.formviewer.getNGSelectedItemText('SupplementCardDetails_CardProduct');
		if(limit.indexOf('(')>-1){
		var value = limit.substr(limit.indexOf('(')+1,limit.length-2);
			if(value != '' && parseInt(value)!=null && parseInt(value)>0){
				var aprLim = getNGValue(pId);
				if(aprLim!='' && parseFloat(aprLim)>0){
				//Deepak 10 Aug 2019 PCAS-2369 change the final limit validation from 80% to 100% for supplymentry card
					if(parseFloat(aprLim) > (parseFloat(value)*1)){
						showAlert(pId,alerts_String_Map['VAL378']);
						setNGValueCustom(pId,'');
					}
				}
				else{
				showAlert(pId,alerts_String_Map['VAL997']);
				setNGValueCustom(pId,'');
				}
			}
			else{
			showAlert(pId,alerts_String_Map['VAL379']);
			setNGValueCustom(pId,'');
			}
		}
		else{
			showAlert(pId,alerts_String_Map['VAL998']);
			setNGValueCustom(pId,'');
		}
	}
	else if(pId=='cmplx_Customer_DOb')
	{
		if(getNGValue(pId)!='')//sagarika CR
		{
		if(validateFutureDate(pId)){
		var age=calcAge(getNGValue(pId),'');//modified by akshay on 14/10/17
		//setNGValueCustom('cmplx_Customer_Age',age);
			//change by saurabh for CR on 11th June 19.
			//change by sagarika for CR 15,(1284)
			if(age<18.6 && getNGValue("cmplx_Customer_Nationality")=='AE')
			{
		       showAlert(pId,'Minor Customer not Applicable for Credit Card');
				setNGValueCustom(pId,'');
				return false;
			}
			else if(age<21 && getNGValue("cmplx_Customer_Nationality")!='AE')
			{
				showAlert(pId,'Minor Customer not Applicable for Credit Card');
				setNGValueCustom(pId,'');
				return false;
			}//end by sagarika
			else
			{
		YYMM('','cmplx_Customer_Age',age);
		if(age>65)
		{
			showAlert('',"Credit Shield is not applicable!");
		}
		document.getElementById('ELigibiltyAndProductInfo_IFrame2').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp';
			}
		}}
		setNGValueCustom('PartMatch_Dob',getNGValue(pId));
	}
	else if(pId=='AddressDetails_years'){
		YYMM(pId,'','');
	}
	else if(pId=='cmplx_CardDetails_cardEmbossing_name' || pId=='SupplementCardDetails_cardEmbName' || pId=='cmplx_CardDetails_Self_card_embossing') 	
	{
		setNGValue(pId,getNGValue(pId).trim());
	}
	else if(pId=='cmplx_EligibilityAndProductInfo_EMI')
	{
		setNGValueCustom('cmplx_EligibilityAndProductInfo_EMI',Math.round(getNGValue('cmplx_EligibilityAndProductInfo_EMI')));

	}
	//case added by saurabh for task list points on 11/4/19.
	else if(pId=='cmplx_BussVerification1_ActualDOB' || pId=='cmplx_CustDetailverification1_Dob_update' || pId=='SupplementCardDetails_ReceivedDate')
	{
		validateFutureDate(pId);
	}
	
	else if(pId=='bankName')
	{
	setNGValueCustom('CC_Loan_Bank_code',getNGValue(pId));
	//Added by shivang for PCASP-2156
	setNGValueCustom('bankCode',getNGValue(pId));
	}
	
	else if(pId=='SupplementCardDetails_IDIssueDate')
	{
		validateFutureDate(pId);
	}
	else if(pId=='SupplementCardDetails_PassportIssueDate')
	{
		validateFutureDate(pId);
	}
	//below code by saurabh for incoming doc new start on 9th Jan 19
	else if(pId=='IncomingDocNew_DocType'){
		setNGValueCustom('IncomingDocNew_mandatory','');
		setNGValueCustom('IncomingDocNew_Status','');
		setNGValueCustom('IncomingDocNew_Remarks','');
		setNGValueCustom('IncomingDocNew_Docindex','');
		var val = getNGValue('IncomingDocNew_DocType');
			if(val && val!='' && val!='--Select--'){
				var mandDocs = getNGValue('cmplx_IncomingDocNew_MandatoryDocument');
				if(mandDocs.indexOf(val)>-1){
					setNGValueCustom('IncomingDocNew_mandatory','Y');
				}
				else{
					setNGValueCustom('IncomingDocNew_mandatory','N');
				}
			return true;
			}
			showAlert(pId,'Please select a Document Type');
			return false;
	}
	else if(pId=='IncomingDocNew_DocName'){
		setNGValueCustom('IncomingDocNew_Status','');
		setNGValueCustom('IncomingDocNew_Remarks','');
		setNGValueCustom('IncomingDocNew_Docindex','');
		return false;
	}
	else if(pId=='IncomingDocNew_Status'){
		if(getNGValue(pId)=='Deferred'){
			setLockedCustom('IncomingDocNew_DeferredUntilDate',false);
		}
		else{
			setLockedCustom('IncomingDocNew_DeferredUntilDate',true);
		}
	}
	else if(pId=='IncomingDocNew_DeferredUntilDate'){
		validatePastDate(pId,'Deferred Until');
	}
	//below function by saurabh for incoming doc new end
	
	else if(pId=='SupplementCardDetails_VisaIssueDate')
	{
		validateFutureDate(pId);
	}
	else if(pId=='SupplementCardDetails_PassportExpiry')	
		validatePastDate(pId,'Passport');
	
	else if(pId=='SupplementCardDetails_VisaExpiry')	
		validatePastDate(pId,'Visa');
	
	else if(pId=='SupplementCardDetails_EmiratesIDExpiry')	
		validatePastDate(pId,'Emirates ID');
	
	
	//case added by saurabh on 20th Dec
	else if(pId=='cmplx_IncomeDetails_DurationOfBanking'){
		var txt=getNGValue(pId);
			  var re1='^\d{1,3}\.(0?[1-9]|1[012])$';    
			  var p = new RegExp(re1,["i"]);
			  var m = p.exec(txt);
			if(m != null) {
				showAlert(pId,'Please enter correct format(YY.MM)');
				setNGValueCustom(pId,'');
				return false;
			}
	
	}
	else if(pId=='cmplx_CustDetailVerification_dob_upd')
	{
	validateFutureDate(pId);
	}
	//below code added by nikhil
	else if(pId=='AddressDetails_addtype')
	{
	if(getNGValue(pId)=='RESIDENCE')
	{
	setLockedCustom('AddressDetails_ResidenceAddrType',false);
	}
	else
	{
	setLockedCustom('AddressDetails_ResidenceAddrType',true);
	}
	}
	
	//DOne by aman for sprint2
	else if(pId=='cmplx_DEC_SetReminder'){
	//changes done by nikhil for PCAS-2511
      validatePastDate(pId,'');
	 
		NoOfAttemptsValue=NoOfAttemptsValue+1;
		if (getNGValue('cmplx_DEC_NoofAttempts')==''){
		setNGValueCustom('cmplx_DEC_NoofAttempts','1');
		}
		else if (getNGValue('cmplx_DEC_NoofAttempts')!=''&& NoOfAttemptsValue==1){
		var toSet;
		toSet=parseInt(getNGValue('cmplx_DEC_NoofAttempts'))+1;
		setNGValueCustom('cmplx_DEC_NoofAttempts',toSet);
		}
		
	}	
	else if(pId=='cmplx_DEC_SetReminder_CA'){
	//changes done by nikhil for PCAS-2511
      validatePastDate(pId,'');
		NoOfAttemptsValue_CA=NoOfAttemptsValue_CA+1;
		if (getNGValue('cmplx_DEC_NoOfAttempts_CA')==''){
		setNGValueCustom('cmplx_DEC_NoOfAttempts_CA','1');
		}
		else if (getNGValue('cmplx_DEC_NoOfAttempts_CA')!=''&& NoOfAttemptsValue_CA==1){
		var toSet;
		toSet=parseInt(getNGValue('cmplx_DEC_NoOfAttempts_CA'))+1;
		setNGValueCustom('cmplx_DEC_NoOfAttempts_CA',toSet);
		}
		
	}	
	else if(pId=='cmplx_DEC_SetReminder_Smart'){
	//changes done by nikhil for PCAS-2511
      validatePastDate(pId,'');
		NoOfAttemptsValue_Smart=NoOfAttemptsValue_Smart+1;
		if (getNGValue('cmplx_DEC_NoOfAttempts_Smart')==''){
		setNGValueCustom('cmplx_DEC_NoOfAttempts_Smart','1');
		}
		else if (getNGValue('cmplx_DEC_NoOfAttempts_Smart')!=''&& NoOfAttemptsValue_Smart==1){
		var toSet;
		toSet=parseInt(getNGValue('cmplx_DEC_NoOfAttempts_Smart'))+1;
		setNGValueCustom('cmplx_DEC_NoOfAttempts_Smart',toSet);
		}
		
	}	
	else if(pId=='cmplx_EmploymentDetails_DOJ'){
		if(validateFutureDate(pId)){
			var LOS=calcAge(getNGValue(pId),'');//modified by akshay on 14/10/17			
			setNGValueCustom('cmplx_EmploymentDetails_LOS',LOS);
			//below code added by nikhil PCSP-90
			field = 'cmplx_EmploymentDetails_LOS';
			var value=LOS;
		 if(value.indexOf('.')>-1){
			if(value.charAt(0)=='.'){
				value=value.replace('.','0.');
			}
			if(value.split(".")[1].length==1){
				value=value.replace('.','.0');	
			}
			else if(value.split(".")[1].length==0){
				value=value.replace('.','.00');	
			}	
		}
		else if(value!=''){
			value=value+".00";
		}
		setNGValueCustom(field,value);
		//return true;
		}
	}
//PCASP-877 nikhil
else if(pId=='cmplx_OffVerification_doj_upd'){
		if(validateFutureDate(pId)){
			var LOS=calcAge(getNGValue(pId),'');//modified by akshay on 14/10/17			
			setNGValueCustom('cmplx_OffVerification_Calculated_LOS',LOS);
			//below code added by nikhil PCSP-90
			field = 'cmplx_OffVerification_Calculated_LOS';
			var value=LOS;
		 if(value.indexOf('.')>-1){
			if(value.charAt(0)=='.'){
				value=value.replace('.','0.');
			}
			if(value.split(".")[1].length==1){
				value=value.replace('.','.0');	
			}
			else if(value.split(".")[1].length==0){
				value=value.replace('.','.00');	
			}	
		}
		else if(value!=''){
			value=value+".00";
		}
		setNGValueCustom(field,value);
		//return true;
		}
		if(getNGValue(pId)=='')
		{
		setNGValueCustom('cmplx_OffVerification_Calculated_LOS','');	
		}
	}	
	else if(pId=='cmplx_OffVerification_offtelnocntctd')
	{
		if(getNGValue("Sub_Product")=='SE')
		{
		if(getNGValue(pId)=='Yes')
		{
		setLocked('cmplx_OffVerification_colleaguename',false);
		setLocked('cmplx_OffVerification_colleaguedesig',false);
		}
		else
		{
		setLocked('cmplx_OffVerification_colleaguename',true);
		setLocked('cmplx_OffVerification_colleaguedesig',true);
		}
		}
	}
	// Added by Rajan for PCASP-2172
		if(getNGValue("Sub_Product")=='SE' && (activityName=='CPV' || activityName=='CPV_Analyst'))
		{
		if(getNGValue(pId)=='Yes')
		{
		setLocked('cmplx_OffVerification_offtelno', false);
		setEnabled('cmplx_OffVerification_offtelno', true);
		
		}
		else
		{
		setLocked('cmplx_OffVerification_offtelno', true);
		}
		}
	//added By Akshay
	else if(pId=='cmplx_EmploymentDetails_EmpIndusSector')
		return true;
		
	else if(pId=='cmplx_EmploymentDetails_Indus_Macro')
		return true;
	//ended by Akshay
	
	else if(pId=='cmplx_EmploymentDetails_ApplicationCateg'){
	//commented by nikhil for PCSP-172
		/*if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='S')
		{
			setVisible("EMploymentDetails_Label59",true);
			setVisible("cmplx_EmploymentDetails_IndusSeg",true);
		}
		else
		{
			setVisible("EMploymentDetails_Label59",false);
			setVisible("cmplx_EmploymentDetails_IndusSeg",false);
		}*/
		return true;

	}//Arun 24/12/17
	//change in self-supp 20-08-2019
		else if(pId=='cmplx_CardDetails_SelfSupp_Limit')
		{
			if(parseFloat(getNGValue(pId))>parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')))
			{
				showAlert('cmplx_CardDetails_SelfSupp_Limit',alerts_String_Map['VAL389']);
				return false;
			}
		return  true;			
		}
		else if(pId=='cmplx_CardDetails_Selected_Card_Product')
		{
			return true;			
		}
	
	
	else if (pId=='FATCA_USRelaton'){		
		
		if (getNGValue(FATCA_USRelaton)=='O')
		{
			if(getNGValue('cmplx_Customer_Nationality')=='US'){
				showAlert('FATCA_USRelaton',alerts_String_Map['VAL151']);
				setNGValueCustom('FATCA_USRelaton',"");
			}
			else{
			Fatca_disableCondition();
			}
			setNGValueCustom('FATCA_iddoc',true);
			setNGValueCustom('FATCA_decforIndv',true);
		}
		// ++ below code alraedy present 06-10-2017 conditions for FATCA US relation
		else if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')

		{
			Fatca_Enable();
			setNGValueCustom('FATCA_iddoc',true);
			setNGValueCustom('FATCA_decforIndv',false);
			
		}
		else if(getNGValue('FATCA_USRelaton')=='C'){
				Fatca_disableCondition();
				setLockedCustom("FATCA_SignedDate",false);
			}
	// ++ above code alraedy present 06-10-2017 conditions for FATCA US relation
			else{
			Fatca_Enable();
			setEnabledCustom("FATCA_iddoc", true);	
			setEnabledCustom("FATCA_decforIndv", true);
			setEnabledCustom("FATCA_w8form",true);
			setEnabledCustom("FATCA_w9form",true);
			setEnabledCustom("FATCA_lossofnationality",true);
			setEnabledCustom("FATCA_TINNo",true);
			setEnabledCustom("FATCA_SignedDate",true);
			setLockedCustom("FATCA_SignedDate",false);
			setEnabledCustom("FATCA_ExpiryDate",true);
			setLockedCustom("FATCA_ExpiryDate",false);
			setEnabledCustom("FATCA_Category",true);
			setEnabledCustom("FATCA_ControllingPersonUSRel",true);
			setEnabledCustom("FATCA_listedreason",true);
			setEnabledCustom("FATCA_selectedreason",true);
			setEnabledCustom("FATCA_Button1",true);
			setEnabledCustom("FATCA_Button2",true);
			}
		}
// ++ below code alraedy present 06-10-2017 conditions for loan si 
		//12th september
	else if(pId=='cmplx_CC_Loan_HOldTo_Date'){
		validatePastDate(pId,"HOld To");
	}
	else if(pId=='cmplx_CC_Loan_SIDate'){
		validatePastDate(pId,'SI Date');
	}
	else if(pId=='cmplx_CC_Loan_HoldFrom_Date'){
		validatePastDate(pId,'Hold From');
		if(CompareFrom_ToDate(pId,'cmplx_CC_Loan_HOldTo_Date')==false){
			document.getElementById(pId).value=null;
		}
	}
	else if(pId=='cmplx_CC_Loan_HOldTo_Date'){
		validatePastDate(pId,'Hold To');
		if(CompareFrom_ToDate('cmplx_CC_Loan_HoldFrom_Date',pId)==false){
			document.getElementById(pId).value=null;	
		}	
	}
	//12th september


	else if(pId=='FATCA_SignedDate'){
		if(validateFutureDate(pId,'Signed Date'))

		{
		  
			var signedDate=getNGValue(pId);
			var years_signedDate=signedDate.substring(6,10);
			var years_expiryDate=parseInt(years_signedDate)+3;
			var ExpiryDate=signedDate.replace(years_signedDate,years_expiryDate);
			setNGValueCustom('FATCA_ExpiryDate',ExpiryDate);	
		  
		} 
	}
		// ++ above code alraedy present 06-10-2017 conditions for loan si 
	else if(pId=='ApplicationCategory'){
		//Added by shivang for PCASP-1763
				var subProd="";
				var applicationType="";
				var applicationCategory="";
				subProd = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
				applicationType=  getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4);
				applicationCategory = getNGValue("ApplicationCategory");
				if((subProd=="SE" && applicationCategory=="BAU") 
						|| (subProd=="BTC" && applicationType!="SMES")){
					setVisible("Liability_New_Label6", true);
					setVisible("cmplx_Liability_New_NoofotherbankAuth", true);
					setVisible("Liability_New_Label8", true);
					setVisible("cmplx_Liability_New_NoofotherbankCompany", true);
					setVisible("Liability_New_Label7", true);
					setVisible("cmplx_Liability_New_TotcleanFundedAmt", true);
					//Added by shivang for PCASP-1714
					setEnabledCustom('bvr',true);
					setVisible("bvr", true);
					setLocked("bvr", false);
					//Added by shivang for PCASP-2124
					setVisible("CompanyDetails_Label29", false);
					setVisible("headOffice", false);
				}
				else if(subProd=="PA"){
					setEnabledCustom('bvr',true);
					setLocked("bvr", true);
					setNGValue("bvr", false);
					setVisible("bvr", false);
					if(applicationType=="RELT" || applicationType=="RELTN"){
						setVisible("Liability_New_Label6", false);
						setVisible("cmplx_Liability_New_NoofotherbankAuth", false);
						setVisible("Liability_New_Label8", false);
						setVisible("cmplx_Liability_New_NoofotherbankCompany", false);
						setVisible("Liability_New_Label7", false);
						setVisible("cmplx_Liability_New_TotcleanFundedAmt", false);

					}else{
						setVisible("Liability_New_Label6", true);
						setVisible("cmplx_Liability_New_NoofotherbankAuth", true);
						setVisible("Liability_New_Label8", false);
						setVisible("cmplx_Liability_New_NoofotherbankCompany", false);
						setVisible("Liability_New_Label7", false);
						setVisible("cmplx_Liability_New_TotcleanFundedAmt", false);
					}
				}
				else{
					setLocked("bvr", true);
					setNGValue("bvr", false);
					setVisible("bvr", false);
					setVisible("Liability_New_Label6", false);
					setVisible("cmplx_Liability_New_NoofotherbankAuth", false);
					setVisible("Liability_New_Label8", false);
					setVisible("cmplx_Liability_New_NoofotherbankCompany", false);
					setVisible("Liability_New_Label7", false);
					setVisible("cmplx_Liability_New_TotcleanFundedAmt", false);
				}
				if(subProd=="SE" && applicationCategory=="S"){
					setVisible("CompanyDetails_Label29", true);
					setVisible("headOffice", true);
				}
		return true;
	}
	else if(pId=='TLIssueDate'){
		validateFutureDate(pId);
	}
	//Added by Shivang
	else if(pId=='cmplx_cust_ver_sp2_desig_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_desig_drop')=='Mismatch'){
			setLocked('Cust_ver_sp2_Designation_button1',false);
		}
		else{
			setLocked('Cust_ver_sp2_Designation_button1',true);
		}
	}
	else if(pId=='cmplx_cust_ver_sp2_doj_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_doj_drop')=='Mismatch'){
			setLocked('cmplx_cust_ver_sp2_doj_remarks_Cont',false);
		}
		else{
			setLocked('cmplx_cust_ver_sp2_doj_remarks_Cont',true);
		}
	}
	else if(pId=='cmplx_cust_ver_sp2_emp_status_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_emp_status_drop')=='Mismatch'){
			setLocked('cmplx_cust_ver_sp2_emp_status__remarks',false);
		}
		else{
			setLocked('cmplx_cust_ver_sp2_emp_status__remarks',true);
		}
	}
	else if(pId=='cmplx_cust_ver_sp2_saalry_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_saalry_drop')=='Mismatch'){
			setLocked('cmplx_cust_ver_sp2_salary_remarks',false);
		}
		else{
			setLocked('cmplx_cust_ver_sp2_salary_remarks',true);
		}
	}
	else if(pId=='cmplx_cust_ver_sp2_salary_payment_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_salary_payment_drop')=='Mismatch'){
			setLocked('cmplx_cust_ver_sp2_salary_payment_remarks',false);
		}
		else{
			setLocked('cmplx_cust_ver_sp2_salary_payment_remarks',true);
		}
	}
	else if(pId=='cmplx_cust_ver_sp2_years_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_years_drop')=='Mismatch'){
			setLocked('cmplx_cust_ver_sp2_years_remarks',false);
		}
		else{
			setLocked('cmplx_cust_ver_sp2_years_remarks',true);
		}
	}
	//added by nikhil for PCSP-164
	else if(pId=='cmplx_DEC_SubFeedbackStatus')
	{
		return true;
	}
	
	else if(pId=='indusSector')
		return true;	
	
	else if(pId=='indusMAcro')
		return true;
		
	//change by saurabh on 29th nov 17 for Drop2 cr.
		else if(pId=='estbDate'){
			if(validateFutureDate(pId))
			{
			
				YYMM('','lob',calcAge(getNGValue(pId),''));
			
				return true;
			}
		}
		else if(pId=='eff_lob_date'){
			if(validateFutureDate(pId))
			{
			
				YYMM('','CompanyDetails_EffectiveLOB',calcAge(getNGValue(pId),''));
			
				return true;
			}
		}		
	//below code added by nikhil 11/12/17
	else if(pId=='cmplx_LoanDetails_moratorium')
	{
	var todayDate = new Date();
	var numberOfDaysToAdd = parseInt(getNGValue(pId));
	todayDate.setDate(todayDate.getDate() + numberOfDaysToAdd);
	var dd = todayDate.getDate();
	var mm = todayDate.getMonth() + 1;
	var y = todayDate.getFullYear();
	var todayFormattedDate = dd + '/'+ mm + '/'+ y;
	setNGValueCustom('cmplx_LoanDetails_frepdate',todayFormattedDate);
	}
	
	else if(pId=='cmplx_EmploymentDetails_Freezone'){
		if(getNGValue('cmplx_EmploymentDetails_Freezone')=="true")
		{
		setLockedCustom("cmplx_EmploymentDetails_FreezoneName",false);
		}
		else
		{
		setLockedCustom("cmplx_EmploymentDetails_FreezoneName",true);
		}
	}
	
	else if(pId=='cmplx_Customer_VisaExpiry'){
		validatePastDate(pId,'Visa Expiry');
		var flag=0;
				for(var i=0; i<country_GCC.length; i++)
				{
					if(country_GCC[i]==getNGValue('cmplx_Customer_Nationality'))
					{
						flag=1;
						break;
					}
				}
		if(isFieldFilled('cmplx_Customer_VisaNo')==false && flag==0){
			showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL351']);
			setNGValueCustom(pId,'');
			return false;
		}
		if(getNGValue('cmplx_Customer_NEP')!='')
		{
			var VisaExpiry=getNGValue('cmplx_Customer_VisaExpiry');
			var years_Visaexpiry=VisaExpiry.substring(6,10);
			var years_VisaIssue=years_Visaexpiry-3;
			var VisaIssue=VisaExpiry.replace(years_Visaexpiry,years_VisaIssue);
			setNGValueCustom('cmplx_Customer_VisaIssueDate',VisaIssue);
		}	
	}
	
	else if(pId=='cmplx_CardDetails_Supplementary_Card')
	{
		if(getNGValue('cmplx_CardDetails_Supplementary_Card')=='Yes')
			{
			if(getNGValue('Sub_Product')=='IM')
			{
			showAlert(pId,'Self and standalone supplementary card not allowed for IM/LOC product!');
			setNGValue('cmplx_CardDetails_Supplementary_Card','No');
			return false;
			}
				setVisible("Supplementary_Cont", true);
				setTop('Supplementary_Cont',parseInt(getTop('Card_Details'))+parseInt(getHeight('Card_Details'))+10+'px');
				setNGValueCustom('IS_SupplementCard_Required','Y');
				
			}

			else
			{
				if(getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid')>0)
				{
					showAlert(pId,alerts_String_Map['VAL358']);
					setNGValueCustom(pId,'Yes');
					return false;
				}
				else{
					setNGValueCustom('IS_SupplementCard_Required','N');
				}
				setVisible("Supplementary_Cont", false);
			}
	}

	
	
	else if(pId=='cmplx_Customer_EmirateIDExpiry'){
		validatePastDate(pId,'Emirate ID Expiry');
		if(isFieldFilled('cmplx_Customer_EmiratesID')==false && getNGValue('cmplx_Customer_NEP')!='NEWC' && getNGValue('cmplx_Customer_NEP')!='NEWJ'){
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL353']);
			setNGValueCustom(pId,'');
			return false;
		}
	}	
	else if(pId=='cmplx_Customer_EmiratesID')
	{
	var Id=getNGValue('cmplx_Customer_EmiratesID');
	var Dob=getNGValue('cmplx_Customer_DOb');
		 if(((Id=="")||(Id.length != 15) ||(Id.substr(0,3)!='784')) ){
		if(Id.substr(0,3)!='784'){
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		else{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		return false;
	}
	else if((Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9)) && Id!='')
		{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL074']);
		return false;
		}
		if(getNGValue('cmplx_Customer_EmiratesID')!=''){
			setNGValueCustom("cmplx_Customer_RESIDENTNONRESIDENT", 'Y');
		}
	setNGValueCustom('PartMatch_EID',getNGValue(pId));
	}

	else if(pId=='cmplx_Customer_PassPortExpiry'){
		validatePastDate(pId,'PassPort Expiry');
		if(isFieldFilled('cmplx_Customer_PAssportNo')==false){
			showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL352']);
			setNGValueCustom(pId,'');
			return false;
		}
	}	
	
	else if(pId=='cmplx_Customer_PassportIssueDate'){
		validateFutureDate(pId);	
		if(isFieldFilled('cmplx_Customer_PAssportNo')==false){
			showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL352']);
			setNGValueCustom(pId,'');
			return false;
		}
	}	
	
	else if(pId=='cmplx_Customer_VisaIssueDate'){
		validateFutureDate(pId);
		var flag=0;
				for(var i=0; i<country_GCC.length; i++)
				{
					if(country_GCC[i]==getNGValue('cmplx_Customer_Nationality'))
					{
						flag=1;
						break;
					}
				}
		if(isFieldFilled('cmplx_Customer_VisaNo')==false && flag==0){
			showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL351']);
			setNGValueCustom(pId,'');
			return false;
		}
	}
	else if(pId=='cmplx_Customer_VisaNo')
	{
		setNGValue('PartMatch_visafno',getNGValue(pId));
	}
	//changes done by nikhil
		//sagarika for PCSP-470
else if(pId=='NotepadDetails_Dateofcalling')
{
	validateFutureDateexcCurrent(pId);
		//setNGValueCustom(pId,'NotepadDetails_Dateofcalling');
			//return false;
		}
		
	
	else if(pId=='cmplx_Customer_yearsInUAE')
		YearsinUAE(pId);
	
	else if(pId=='OECD_tinNo'){
		var tin = getNGValue('OECD_tinNo');
		if(tin){
		setEnabledCustom('OECD_noTinReason',false);
		}
		else{
		setEnabledCustom('OECD_noTinReason',true);
		}
	}
	//changes done as per sanity issues
	else if(pId=='OECD_noTinReason'){
		var tin_reas = getNGValue('OECD_noTinReason');
		if(tin_reas=='' || tin_reas=='--Select--'){
		setEnabledCustom('OECD_tinNo',true);
		}
		else{
		setEnabledCustom('OECD_tinNo',false);
		}
	}
	
	else if(pId=='ReqProd')
	{
		var ReqProd=getNGValue('ReqProd');
		setFieldsVisible1(ReqProd);
		return true;
	}
	else if(pId=='FATCA_ExpiryDate'){
		validatePastDate(pId,"FATCA Expiry");	
	}
	
	else if(pId=='LimitExpiryDate')
	{
		validatePastDate(pId,'Limit Expiry');
	}
	//12th september
//++ Below Code already exists for : "1-CSM-Auth. Sign-" Expiry date should not be a past date" : Reported By Shashank on Oct 05, 2017++
	else if(pId=='AuthorisedSignDetails_VisaExpiryDate'){
		validatePastDate(pId,"Visa Expiry");
	}
	else if(pId=='AuthorisedSignDetails_PassportExpiryDate'){
		validatePastDate(pId,"Passport Expiry");
	}
	else if(pId=='AuthorisedSignDetails_DOB'){
		validateFutureDate(pId);
	}
	else if(pId=='PartnerDetails_Dob'){
		validateFutureDate(pId);
	}
	//12th september
	else if(pId=='cmplx_Customer_IdIssueDate')
	{
		validateFutureDate(pId);
		if(isFieldFilled('cmplx_Customer_EmiratesID')==false){
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL353']);
			setNGValueCustom(pId,'');
			return false;
		}
	}
	else if(pId=='cmplx_BussVerification1_ActualDOB')
	{
		validateFutureDate(pId);
	}
	else if(pId=='WorldCheck1_Dob')
	{
		//++ Below Code added By Abhishek on Oct 6, 2017  to fix : "17-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017++
		setNGValueCustom("WorldCheck1_age", calcAge(getNGValue(pId),''));//modified by akshay on 14/10/17
		//-- Above Code added By Abhishek on Oct 6, 2017  to fix : "17-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017--
		validateFutureDate(pId);
	}	
			
	else if(pId=='cmplx_IncomeDetails_Accomodation_1')
		{
			setLockedCustom('cmplx_IncomeDetails_CompanyAcc',true);
		}
		else if(pId=='cmplx_IncomeDetails_Accomodation_0'){
			setLockedCustom('cmplx_IncomeDetails_CompanyAcc',false);
			//setEnabledCustom('cmplx_IncomeDetails_AccomodationValue',true);
        }
	
	
	 else if (pId == 'subProd')
    {
        var subProd = getNGValue("subProd");
        if (subProd == 'BTC' || subProd == 'SE')
        {
            setNGValueCustom('EmpType','Self Employed');
            setLockedCustom('EmpType',true);
            setVisible("LimitAcc", false);
            setNGValueCustom('LimitAcc', 'NA');
            setVisible("LimitExpiryDate", false);
            setVisible("typeReq", false);
            setVisible("Product_Label15", false);
            setVisible("Product_Label16", false);
            setVisible("Product_Label17", false);
            setVisible("Product_Label18", false);
            setVisible("Product_Label22", false);
            setVisible("Product_Label23", false);
            setVisible("Product_Label24", false);
            setVisible('Product_Label6', true);
            setVisible('CardProd', true);
			setVisible("Product_Label8", false);
			setVisible("FDAmount", false);
			setVisible("Product_Label10", false);
			setVisible("Limit_Expdate", false);
			setVisible("Product_Label16", false);
			
			setVisible("Product_Label7", false);
			setVisible("LastPermanentLimit", false);
			setVisible("Product_Label9", false);
			setVisible("LastTemporaryLimit", false);
			setVisible("Product_Label11", false);
			setVisible("ExistingTempLimit", false);
        }

        else if (subProd == 'IM')
        {
            setNGValueCustom('EmpType','Salaried');
            setLockedCustom('EmpType',true);
            setVisible('Product_Label6', true);
            setVisible('CardProd',true);
            setVisible("LimitAcc", false);
            setNGValueCustom('LimitAcc', 'NA');
            setVisible("LimitExpiryDate", false);
            setVisible("typeReq", false);
            setVisible("Product_Label15", false);
            setVisible("Product_Label16", false);
            setVisible("Product_Label17", false);
            setVisible("Product_Label18", false);
            setVisible("Product_Label22", false);
            setVisible("Product_Label23", false);
            setVisible("Product_Label24", false);
			setVisible("Product_Label8", false);
			setVisible("FDAmount", false);
			
			setVisible("Product_Label10", false);
			setVisible("Limit_Expdate", false);
			setVisible("Product_Label16", false);
			setVisible("LimitAcc", false);
			setVisible("Product_Label7", false);
			setVisible("LastPermanentLimit", false);
			setVisible("Product_Label9", false);
			setVisible("LastTemporaryLimit", false);
			setVisible("Product_Label11", false);
			setVisible("ExistingTempLimit", false);
			
        }
        else if (subProd == 'Salaried Credit Card')
        {
            setNGValueCustom('EmpType','Salaried');
            setLockedCustom('EmpType',true);
            setVisible('Product_Label6', true);
			setVisible('CardProd', true);
            setVisible('cmplx_Product_cmplx_ProductGrid_cmplx_Product_cmplx_ProductGrid_cardProductuct', true);
            setVisible("LimitAcc", false);
            setNGValueCustom('LimitAcc', 'NA');
            setVisible("LimitExpiryDate", false);
            setVisible("typeReq", false);
            setVisible("Product_Label15", false);
            setVisible("Product_Label16", false);
            setVisible("Product_Label17", false);
            setVisible("Product_Label18", false);
            setVisible("Product_Label22", false);
            setVisible("Product_Label23", false);
            setVisible("Product_Label24", false);
			setVisible("Product_Label8", false);
			setVisible("FDAmount", false);
			
        }
		
		
        else if (subProd == 'Limit Increase' || subProd=='Product Upgrade with Limit inc')
        {
            setVisible("LimitAcc", true);
			setVisible('Product_Label6', true);
			setVisible('CardProd', true);
            //setVisible("Product_DatePicker4", true);
			//setVisible("Product_Label10", true);
			setVisible("typeReq", false);	
            setVisible("Product_Label15", true);
            setVisible("Product_Label16", true);
            setVisible("Product_Label17", true);
            //setVisible("Product_Label18", true);
            setVisible("Product_Label22", true);
            setVisible("Product_Label23", true);
            setVisible("Product_Label24", true);
			
			setVisible("Product_Label10", true);
            setVisible("Product_Label11", true);
            setVisible("Product_Label9", true);
			setVisible("Product_Label7", true);
			setVisible("LastPermanentLimit_Cont", true);
            setVisible("LastTemporaryLimit_Cont", true);
            setVisible("ExistingTempLimit_Cont", true);
			setVisible("Limit_Expdate_Cont", true);
			setVisible("Product_Label8", false);
			setVisible("FDAmount", false);
            //setLockedCustom('EmpType',false);
			setVisible("Product_Label15", false);
			setVisible("Limit_Expdate", true);
			setVisible("LimitAcc", true);
			setVisible("LastPermanentLimit", true);
			setVisible("LastTemporaryLimit", true);
			setVisible("ExistingTempLimit", true);
			//com.newgen.omniforms.formviewer.setTop("Product_Label11","122px");
			//com.newgen.omniforms.formviewer.setTop("ExistingTempLimit","138px");
			//com.newgen.omniforms.formviewer.setLeft("Product_Label16","555px");
			//com.newgen.omniforms.formviewer.setLeft("LimitAcc","555px");
			
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label10","822px");
			com.newgen.omniforms.formviewer.setLeft("Limit_Expdate","822px");
			com.newgen.omniforms.formviewer.setTop("Product_Label10","70px");
			com.newgen.omniforms.formviewer.setTop("Limit_Expdate","87px");
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label7","1080px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit","1080px");
			com.newgen.omniforms.formviewer.setTop("Product_Label7","70px");
			com.newgen.omniforms.formviewer.setTop("LastPermanentLimit","87px");
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label9","24px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit","24px");	
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label11","296px");
			com.newgen.omniforms.formviewer.setLeft("ExistingTempLimit","296px");
			com.newgen.omniforms.formviewer.setTop("Product_Label11","122px");
			com.newgen.omniforms.formviewer.setTop("ExistingTempLimit","138px");
			
        }
        else
        {
            setVisible("LimitAcc", false);
            setNGValueCustom('LimitAcc', 'NA');
            setVisible("LimitExpiryDate", false);
            setVisible("typeReq", false);
            setVisible("Product_Label15", false);
            setVisible("Product_Label16", false);
            setVisible("Product_Label17", false);
            setVisible("Product_Label18", false);
            setVisible("Product_Label22", false);
            setVisible("Product_Label23", false);
            setVisible("Product_Label24", false);
			setVisible("Product_Label10", false);
            setVisible("Product_Label11", false);
            setVisible("Product_Label9", false);
			setVisible("Product_Label7", false);
			setVisible("LastPermanentLimit_Cont", false);
            setVisible("LastTemporaryLimit_Cont", false);
            setVisible("ExistingTempLimit_Cont", false);
			setVisible("Limit_Expdate_Cont", false);
			setVisible("Product_Label8", false);
			setVisible("FDAmount", false);	
			
			setVisible("Limit_Expdate", false);
			setVisible("LimitAcc", false);
			setVisible("LastPermanentLimit", false);
			setVisible("LastTemporaryLimit", false);
			setVisible("ExistingTempLimit", false);
			
			if(subProd == 'Secured Card')
			{
				setVisible('Product_Label6', true);
				setVisible('CardProd', true);	
				setVisible("Product_Label8", true);
				com.newgen.omniforms.formviewer.setLeft("Product_Label8", "822px");
				com.newgen.omniforms.formviewer.setTop("Product_Label8","70px");
				setVisible("FDAmount", true);
				com.newgen.omniforms.formviewer.setLeft("FDAmount", "822px");
				com.newgen.omniforms.formviewer.setTop("FDAmount","87px");				
			}
			else
			{
				setVisible("Product_Label8", false);
				setVisible("FDAmount", false);	
			}
			//setLockedCustom('EmpType',false);
        }
		
		
        return true;
    }

    else if (pId == 'AppType')
    {
        var appType = getNGValue("AppType");
        if (appType == 'Temporary')
        {
            //setVisible("LimitExpiryDate", true);
            //setVisible("Product_Label18", true);
			setVisible("Limit_Expdate", true);
            setVisible("Product_Label10", true);
			com.newgen.omniforms.formviewer.setLeft("Product_Label7", "1080px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit", "1080px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label9", "24px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit", "24px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label11","296px");
			com.newgen.omniforms.formviewer.setLeft("ExistingTempLimit","296px");
			com.newgen.omniforms.formviewer.setTop("Product_Label9","122px");
			com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","138px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label16","555px");
			com.newgen.omniforms.formviewer.setLeft("LimitAcc","555px");
			//com.newgen.omniforms.formviewer.setTop("Product_Label7","70px");
			//com.newgen.omniforms.formviewer.setTop("LastPermanentLimit","85px");
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label10","822px");
			com.newgen.omniforms.formviewer.setLeft("Limit_Expdate","822px");
			com.newgen.omniforms.formviewer.setTop("Product_Label10","70px");
			com.newgen.omniforms.formviewer.setTop("Limit_Expdate","87px");
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label7","1080px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit","1080px");
			com.newgen.omniforms.formviewer.setTop("Product_Label7","70px");
			com.newgen.omniforms.formviewer.setTop("LastPermanentLimit","87px");
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label9","24px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit","24px");	
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label11","296px");
			com.newgen.omniforms.formviewer.setLeft("ExistingTempLimit","296px");
			com.newgen.omniforms.formviewer.setTop("Product_Label11","122px");
			com.newgen.omniforms.formviewer.setTop("ExistingTempLimit","138px");
		}
        else
        {
            //setVisible("LimitExpiryDate", false);
            //setVisible("Product_Label18", false);
			setVisible("Limit_Expdate", false);
            setVisible("Product_Label10", false);
			com.newgen.omniforms.formviewer.setLeft("Product_Label7", "824px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit", "824px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label9", "1080px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit", "1080px");
			com.newgen.omniforms.formviewer.setTop("Product_Label9","70px");
			com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","87px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label11","24px");
			com.newgen.omniforms.formviewer.setLeft("ExistingTempLimit","24px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label16","296px");
			com.newgen.omniforms.formviewer.setLeft("LimitAcc","296px");
			
			com.newgen.omniforms.formviewer.setTop("Product_Label7","70px");
			com.newgen.omniforms.formviewer.setTop("LastPermanentLimit","87px");
					
			com.newgen.omniforms.formviewer.setTop("Product_Label9","70px");
			com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","87px");
			
			com.newgen.omniforms.formviewer.setLeft("Product_Label16","296px");
			com.newgen.omniforms.formviewer.setLeft("LimitAcc","296px");	
			
			
			com.newgen.omniforms.formviewer.setTop("Product_Label11","122px");
			com.newgen.omniforms.formviewer.setTop("ExistingTempLimit","138px");
			
		}

    }
	// changes end for validation and visibility of new product fields - 16-8-2017

	
	else if(pId=='ReqLimit')
		putComma(pId);
		
	else if(pId=='CardProd')
		return true;
	
	else if(pId=='ReqTenor')
		putComma(pId);
	// ++ below code already present - 06-10-2017 calculate income details	
/*else if(pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other'){
			LengthVal(pId);
		setNGValue(pId,setDecimalto2digits(pId));

		putComma(pId);		
		var grossSal=calcGrossSal();
		setNGValue("cmplx_IncomeDetails_grossSal",(grossSal>0)?grossSal:"");
		putComma('cmplx_IncomeDetails_grossSal');
		setNGValue("cmplx_IncomeDetails_totSal",calcTotalSal());
		putComma('cmplx_IncomeDetails_totSal');
		
	}*/
	else if(pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other' || pId=='cmplx_IncomeDetails_RentalIncome' || pId=='cmplx_IncomeDetails_EducationalAllowance'){
		//setNGValueCustom(pId,setDecimalto2digits(pId));
		var flag=true;
		//change by Saurabh for PPG CR start
		if(pId=='cmplx_IncomeDetails_RentalIncome'){
			if(getNGValue(pId)){
				if(parseFloat(getNGValue(pId))){
					if(parseFloat(getNGValue(pId).replace(',',''))> parseFloat(getNGValue('cmplx_IncomeDetails_grossSal').replace(',',''))){
						showAlert(pId,"Rental Income cannot be greater than Gross Salary");
						setNGValue(pId,'');
						
					}
				}
				else{flag=false;}
			}
			else{flag=false;}
		}
		//change by Saurabh for PPG CR end
		if(flag){
		putComma(pId);		
		var grossSal=calcGrossSal();
		setNGValueCustom("cmplx_IncomeDetails_grossSal",(grossSal>0)?grossSal:"");
		putComma('cmplx_IncomeDetails_grossSal');
		if(getNGValue('cmplx_IncomeDetails_RentalIncome')!=""){//sagarika for PCAS2643
				//var rental=parseFloat(getNGValue('cmplx_IncomeDetails_RentalIncome'));
					if(parseFloat(getNGValue('cmplx_IncomeDetails_RentalIncome').replace(',',''))> parseFloat(getNGValue('cmplx_IncomeDetails_grossSal').replace(',',''))){
						showAlert('cmplx_IncomeDetails_RentalIncome',"Rental Income cannot be greater than Gross Salary");
						setNGValue('cmplx_IncomeDetails_RentalIncome','');
					}
				}
		
					
		setNGValueCustom("cmplx_IncomeDetails_totSal",calcTotalSal());
		putComma('cmplx_IncomeDetails_totSal');
		}
	}

// ++ above code already present - 06-10-2017 calculate income details

// ++ below change already present - 06-10-2017 valid salary day
else if(pId=='cmplx_IncomeDetails_SalaryDay'){
		var date = parseInt(getNGValue(pId));
		if(getNGValue("Sub_Product")!='BPA' && getNGValue("Sub_Product")!='SEC' && getNGValue('cmplx_Customer_VIPFlag')==false){
		if(date<1 || date>31 && getNGValue(pId)!=""){
			showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['VAL102']);
			setNGValueCustom(pId,'');
			}
		}
		
	}
	else if(pId=='cmplx_IncomeDetails_StatementCycle'){
		if((getNGValue(pId)<1 || getNGValue(pId)>31 )&& getNGValue(pId)!="")
			showAlert('cmplx_IncomeDetails_StatementCycle',alerts_String_Map['VAL102']);
	}
	
		//	CHANGE Started BY AKSHAY on 4/10/17 for changing total Salary calculation

	else if(pId=='cmplx_IncomeDetails_Overtime_Month1' || pId=='cmplx_IncomeDetails_Overtime_Month2' || pId=='cmplx_IncomeDetails_Overtime_Month3'|| pId=='cmplx_IncomeDetails_Commission_Month1' || pId=='cmplx_IncomeDetails_Commission_Month2' || pId=='cmplx_IncomeDetails_Commission_Month3' || pId=='cmplx_IncomeDetails_FoodAllow_Month1' || pId=='cmplx_IncomeDetails_FoodAllow_Month2' || pId=='cmplx_IncomeDetails_FoodAllow_Month3' || pId=='cmplx_IncomeDetails_PhoneAllow_Month1' || pId=='cmplx_IncomeDetails_PhoneAllow_Month2' || pId=='cmplx_IncomeDetails_PhoneAllow_Month3' || pId=='cmplx_IncomeDetails_serviceAllow_Month1' || pId=='cmplx_IncomeDetails_serviceAllow_Month2' || pId=='cmplx_IncomeDetails_serviceAllow_Month3' || pId=='cmplx_IncomeDetails_Bonus_Month1'|| pId=='cmplx_IncomeDetails_Bonus_Month2' || pId=='cmplx_IncomeDetails_Bonus_Month3' || pId=='cmplx_IncomeDetails_Other_Month1' || pId=='cmplx_IncomeDetails_Other_Month2' || pId=='cmplx_IncomeDetails_Other_Month3' || pId=='cmplx_IncomeDetails_Flying_Month1' || pId=='cmplx_IncomeDetails_Flying_Month2' || pId=='cmplx_IncomeDetails_Flying_Month3'){
		
		setNGValueCustom(pId,setDecimalto2digits(pId));
		putComma(pId);
		if(pId.indexOf('Overtime')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Overtime_Month1","cmplx_IncomeDetails_Overtime_Month2","cmplx_IncomeDetails_Overtime_Month3");
			setNGValueCustom("cmplx_IncomeDetails_Overtime_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Commission')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Commission_Month1","cmplx_IncomeDetails_Commission_Month2","cmplx_IncomeDetails_Commission_Month3");
			setNGValueCustom("cmplx_IncomeDetails_Commission_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('FoodAllow')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_FoodAllow_Month1","cmplx_IncomeDetails_FoodAllow_Month2","cmplx_IncomeDetails_FoodAllow_Month3");
			setNGValueCustom("cmplx_IncomeDetails_FoodAllow_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('PhoneAllow')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_PhoneAllow_Month1","cmplx_IncomeDetails_PhoneAllow_Month2","cmplx_IncomeDetails_PhoneAllow_Month3");
			setNGValueCustom("cmplx_IncomeDetails_PhoneAllow_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('serviceAllow')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_serviceAllow_Month1","cmplx_IncomeDetails_serviceAllow_Month2","cmplx_IncomeDetails_serviceAllow_Month3");
			setNGValueCustom("cmplx_IncomeDetails_serviceAllow_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Bonus')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Bonus_Month1","cmplx_IncomeDetails_Bonus_Month2","cmplx_IncomeDetails_Bonus_Month3");
			setNGValueCustom("cmplx_IncomeDetails_Bonus_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Other')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Other_Month1","cmplx_IncomeDetails_Other_Month2","cmplx_IncomeDetails_Other_Month3");
			setNGValueCustom("cmplx_IncomeDetails_Other_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Flying')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Flying_Month1","cmplx_IncomeDetails_Flying_Month2","cmplx_IncomeDetails_Flying_Month3");
			setNGValueCustom("cmplx_IncomeDetails_Flying_Avg",(avg>0)?avg:"");
		}
		
		setNGValueCustom("cmplx_IncomeDetails_totSal",calcTotalSal());
		putComma('cmplx_IncomeDetails_totSal');
		setNGValueCustom("cmplx_IncomeDetails_TotalAvgOther",calcTotalAvgOther());
		putComma('cmplx_IncomeDetails_TotalAvgOther');
	}
	
	

	else if (pId=='cmplx_EligibilityAndProductInfo_InterestRate')
	{
		var Intrate= getNGValue('cmplx_EligibilityAndProductInfo_InterestRate');
		var BaseRate= getNGValue('cmplx_EligibilityAndProductInfo_BAseRate'); 
		var ProdPrefRate= getNGValue('cmplx_EligibilityAndProductInfo_ProdPrefRate');
		//var Intrate= getNGValue('cmplx_EligibilityAndProductInfo_InterestRate');
		if(parseFloat(Intrate)>100){
			showAlert(pId,'Interest Rate should be less than 100');
			return false;
		}
		setNGValueCustom('cmplx_EligibilityAndProductInfo_MArginRate',parseFloat(Intrate)-parseFloat(BaseRate)-parseFloat(ProdPrefRate));
		setNGValueCustom('cmplx_EligibilityAndProductInfo_NetRate',parseInt(Intrate));
		setNGValueCustom('cmplx_EligibilityAndProductInfo_InterestRate',parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_InterestRate')).toFixed(2));
		
		//added by disha for CSM Interest_Rate_Approval Flag on 6Th April 2018
		if(getNGValue('Sub_Product')=='IM')
		{
			if(activityName=='CSM' || activityName=='Telesales_RM')
			{
				setNGValueCustom("Interest_Rate_App_req","Y");
			}
		
		}
		return true;
	}
	
	//added by akshay on 17 april 18
	else if(pId=='cmplx_EligibilityAndProductInfo_Tenor'){
		calAgeAtMaturity();
		return true;
	}
	
	//Changes by aman for todo list
	if(pId=='cmplx_EmploymentVerification_FiledVisitedInitiated_value')
	{
		return true;
	}
	//Changes done by aman on 1810 for FCU Screen
	if(pId=='cmplx_EmploymentVerification_FiledVisitedInitiated_ver')
	{
		return true;
	}
	if(pId=='cmplx_DEC_FeedbackStatus')
	{
		return true;
	}
	//Changes by aman for todo list
	
	else if(pId=='cmplx_IncomeDetails_netSal1' || pId=='cmplx_IncomeDetails_netSal2' || pId=='cmplx_IncomeDetails_netSal3'){
		setNGValueCustom(pId,setDecimalto2digits(pId));
		putComma(pId);	
		setNGValueCustom("cmplx_IncomeDetails_AvgNetSal",calcAvgNet());
		putComma('cmplx_IncomeDetails_AvgNetSal');	
	}
	//++below code added by nikhil for Self-Supp CR
		else if(pId=='cmplx_CardDetails_SelfSupp_required')
		{
			if(getNGValue(pId)=='Yes')
			{
			if(getNGValue('Sub_Product')=='IM')
			{
			showAlert(pId,'Self and standalone supplementary card not allowed for IM/LOC product!');
			setNGValue('cmplx_CardDetails_SelfSupp_required','No');
			return false;
			}
				setEnabled('CardDetails_Avl_Card_Product',true);
				setEnabled('CardDetails_Sel_Card_Product',true);
				setEnabled('CardDetails_Self_add',true);
				setEnabled('CardDetails_Self_remove',true);
			}
			else
			{
				setEnabled('CardDetails_Avl_Card_Product',false);
				setEnabled('CardDetails_Sel_Card_Product',false);
				setEnabled('CardDetails_Self_add',false);
				setEnabled('CardDetails_Self_remove',false);
			}
			return true;
		}
		//--above code added by nikhil for Self-Supp CR
	

  //below code commented by akshay on 17/11/17 as discussed with shashank
	else if(pId=='cmplx_EmploymentDetails_targetSegCode'){//Jira-2606
		  if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='NEPNAL' || getNGValue('cmplx_EmploymentDetails_targetSegCode')=='NEPALO')
		{
			setEnabled('cmplx_EmploymentDetails_NepType',true);

		}
		else
		{
			setEnabled('cmplx_EmploymentDetails_NepType',false);

		}
	//changed by nikhil for PCSP-527
		if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='EMPID')
		{console.log("empid");
			setVisible('cmplx_EmploymentDetails_IndusSeg',true);
			setVisible('EMploymentDetails_Label59',true);
			setVisible('EMploymentDetails_Label7',false);
			setVisible('cmplx_EmploymentDetails_OtherBankCAC',false);
			setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			setVisible('EMploymentDetails_Label41',true);
				setVisible('EMploymentDetails_Label42',false);
		setVisible('EMploymentDetails_Label43',false);
		setVisible('EMploymentDetails_Label44',false);
		setVisible('EMploymentDetails_Label46',false);
		setVisible('cmplx_EmploymentDetails_PremType',false);
		setVisible('EMploymentDetails_Label47',false);
	setVisible('EMploymentDetails_Label14',false);
		
		setVisible('cmplx_EmploymentDetails_RegPayment',false);
		setVisible('EMploymentDetails_Label52',false);
		setVisible('cmplx_EmploymentDetails_MinimumWait',false);
		setVisible('cmplx_EmploymentDetails_InsuranceValue',false);
		setVisible('cmplx_EmploymentDetails_PremAmt',false);
		setVisible('cmplx_EmploymentDetails_PremPaid',false);
			setVisible('EMploymentDetails_Label54',false);
		setVisible('cmplx_EmploymentDetails_MotorInsurance',false);
		//setEnabled('cmplx_EmploymentDetails_MotorInsurance',true);
			setEnabled('cmplx_EmploymentDetails_MinimumWait',false);
		setEnabled('cmplx_EmploymentDetails_PremPaid',false);
		setEnabled('cmplx_EmploymentDetails_RegPayment',false);
		setEnabled('cmplx_EmploymentDetails_PremType',false);
		setEnabled('cmplx_EmploymentDetails_InsuranceValue',false);
		setEnabled('cmplx_EmploymentDetails_PremAmt',false);
		//nikhil changes for PCAS-2483
		setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			setVisible('EMploymentDetails_Label41',true);
			
		}
		else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='CAC'){
		//commented by nikhil for PCSP-172
			/*if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='S')
			{
				setVisible('cmplx_EmploymentDetails_IndusSeg',true);
				setVisible('EMploymentDetails_Label59',true);
				
			}
			else{
				setVisible('cmplx_EmploymentDetails_IndusSeg',false);
				setVisible('EMploymentDetails_Label59',false);
			}*/
			setVisible('EMploymentDetails_Label7',true);
			setVisible('cmplx_EmploymentDetails_OtherBankCAC',true);
			//below added by nikhil to hide button also
			setVisible('EmploymentDetails_Bank_Button',true);
			//bewlo changes by nikhil for PCAS-2483
			setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			setVisible('EMploymentDetails_Label41',true);
				setVisible('EMploymentDetails_Label42',false);
		setVisible('EMploymentDetails_Label43',false);
		setVisible('EMploymentDetails_Label44',false);
		setVisible('EMploymentDetails_Label46',false);
		setVisible('cmplx_EmploymentDetails_PremType',false);
		setVisible('EMploymentDetails_Label47',false);
		//setVisible('EMploymentDetails_Label14',true);
		
		setVisible('cmplx_EmploymentDetails_RegPayment',false);
		setVisible('EMploymentDetails_Label52',false);
		setVisible('cmplx_EmploymentDetails_MinimumWait',false);
		setVisible('cmplx_EmploymentDetails_InsuranceValue',false);
		setVisible('cmplx_EmploymentDetails_PremAmt',false);
		setVisible('cmplx_EmploymentDetails_PremPaid',false);
			setVisible('EMploymentDetails_Label54',false);
		setVisible('cmplx_EmploymentDetails_MotorInsurance',false);
		//setEnabled('cmplx_EmploymentDetails_MotorInsurance',true);
		//	setEnabled('cmplx_EmploymentDetails_MinimumWait',false);
		setEnabled('cmplx_EmploymentDetails_PremPaid',false);
		setEnabled('cmplx_EmploymentDetails_RegPayment',false);
		setEnabled('cmplx_EmploymentDetails_PremType',false);
		setEnabled('cmplx_EmploymentDetails_InsuranceValue',false);
		setEnabled('cmplx_EmploymentDetails_PremAmt',false);
		//changes done by nikhil for PCAS-2483
		setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			setVisible('EMploymentDetails_Label41',true);
		}
	 else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='LIFSUR')
		{console.log("life");
			//alert("a");
			//console.log(getNGValue('cmplx_EmploymentDetails_targetSegCode'));
			if(activityName=='CSM')
			{
		//setEnabled('EMploymentDetails_Combo3',true);
		setTop("EMploymentDetails_Label42","282px");
               setTop("cmplx_EmploymentDetails_InsuranceValue", "297px");
                 setTop("EMploymentDetails_Label43","282px");
                setTop("cmplx_EmploymentDetails_PremAmt", "297px");
                 setTop("EMploymentDetails_Label44", "282px");
                 setTop("cmplx_EmploymentDetails_PremPaid","297px");
                 setTop("EMploymentDetails_Label46", "327px");//cmplx_emp_ver_sp2_empstatus_remarks
                 setTop("cmplx_EmploymentDetails_PremType", "342px");
                 setTop("EMploymentDetails_Label47", "327px");
                 setTop("cmplx_EmploymentDetails_RegPayment","327px");
                 setTop("EMploymentDetails_Label52", "327px");
     
			
		
		}
		
		setVisible('EMploymentDetails_Label42',true);
		setVisible('EMploymentDetails_Label43',true);
		setVisible('EMploymentDetails_Label44',true);
		setVisible('EMploymentDetails_Label46',true);
		setVisible('cmplx_EmploymentDetails_PremType',true);
		setVisible('EMploymentDetails_Label47',true);
		//setVisible('EMploymentDetails_Label14',true);
		
		setVisible('cmplx_EmploymentDetails_RegPayment',true);
		setVisible('EMploymentDetails_Label52',true);
		setVisible('cmplx_EmploymentDetails_MinimumWait',true);
		setVisible('cmplx_EmploymentDetails_InsuranceValue',true);
		setVisible('cmplx_EmploymentDetails_PremAmt',true);
		setVisible('cmplx_EmploymentDetails_PremPaid',true);
		//	setVisible('EMploymentDetails_Label54',false);
		//setVisible('cmplx_EmploymentDetails_MotorInsurance',false);
		setEnabled('cmplx_EmploymentDetails_MinimumWait',true);
		setEnabled('cmplx_EmploymentDetails_PremPaid',true);
		setEnabled('cmplx_EmploymentDetails_RegPayment',true);
		setEnabled('cmplx_EmploymentDetails_PremType',true);
		setEnabled('cmplx_EmploymentDetails_InsuranceValue',true);
		setEnabled('cmplx_EmploymentDetails_PremAmt',true);
		setVisible('EMploymentDetails_Label54',false);
		//changes done by nikhil for PCAS-2483
		setVisible('cmplx_EmploymentDetails_MotorInsurance',false);
		setEnabled('cmplx_EmploymentDetails_MotorInsurance',false);
		setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			setVisible('EMploymentDetails_Label41',true);
			setVisible('EMploymentDetails_Label41',true);
			//setNGValue('cmplx_EmploymentDetails_PremType',"--Select");//PCSP-2200 by sagarika
			//setNGVlaue('cmplx_EmploymentDetails_MinimumWait',false);
			//setNGValue('cmplx_EmploymentDetails_InsuranceValue',"");
			//setNGValue('cmplx_EmploymentDetails_PremAmt',"");
			//setNGValue('cmplx_EmploymentDetails_PremPaid',"");
			//setNGValue('cmplx_EmploymentDetails_MotorInsurance',"");
		//	setNGclear('cmplx_EmploymentDetails_RegPayment',true);
			//setNGValue('cmplx_EmploymentDetails_RegPayment',false);
		setVisible('cmplx_EmploymentDetails_Is_group_insur_policy',true);
 setVisible('cmplx_EmploymentDetails_Is_life_insur_lessthan_5',true);
	
		}
	else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='MOTSUR')
		{//console.log("target");
	if(activityName=='CSM')
	{
	 setTop("EMploymentDetails_Label54", "327px");
                 
                setTop("cmplx_EmploymentDetails_MotorInsurance",  "342px");
	}
	//console.log(getNGValue('cmplx_EmploymentDetails_targetSegCode'));
		setVisible('EMploymentDetails_Label42',false);
		setVisible('EMploymentDetails_Label43',false);
		setVisible('EMploymentDetails_Label44',false);
		setVisible('EMploymentDetails_Label46',false);
		setVisible('cmplx_EmploymentDetails_PremType',false);
		setVisible('EMploymentDetails_Label47',false);
		//setVisible('EMploymentDetails_Label14',true);
		
		setVisible('cmplx_EmploymentDetails_RegPayment',false);
		setVisible('EMploymentDetails_Label52',false);
		setVisible('cmplx_EmploymentDetails_MinimumWait',false);
		setVisible('cmplx_EmploymentDetails_InsuranceValue',false);
		setVisible('cmplx_EmploymentDetails_PremAmt',false);
		setVisible('cmplx_EmploymentDetails_PremPaid',false);
			setVisible('EMploymentDetails_Label54',true);
		setVisible('cmplx_EmploymentDetails_MotorInsurance',true);
		setEnabled('cmplx_EmploymentDetails_MotorInsurance',true);
			setEnabled('cmplx_EmploymentDetails_MinimumWait',false);
		setEnabled('cmplx_EmploymentDetails_PremPaid',false);
		setEnabled('cmplx_EmploymentDetails_RegPayment',false);
		setEnabled('cmplx_EmploymentDetails_PremType',false);
		setEnabled('cmplx_EmploymentDetails_InsuranceValue',false);
		setEnabled('cmplx_EmploymentDetails_PremAmt',false);
		//changes done by nikhil for PVAS-2483
		setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			setVisible('EMploymentDetails_Label41',true);
		//setVisible('EMploymentDetails_Label41',false);
			//setVisible('EMploymentDetails_Label41',false);
			//PCSP-2200 by sagarika
			//setNGValue('cmplx_EmploymentDetails_PremType',"--Select");
			//setNGVlaue('cmplx_EmploymentDetails_MinimumWait',false);
			//setNGValue('cmplx_EmploymentDetails_InsuranceValue',"");
			//setNGValue('cmplx_EmploymentDetails_PremAmt',"");
			//setNGValue('cmplx_EmploymentDetails_PremPaid',"");
		//	setNGValue('cmplx_EmploymentDetails_MotorInsurance',"");
		//	setNGclear('cmplx_EmploymentDetails_RegPayment',true);
			//setNGValue('cmplx_EmploymentDetails_RegPayment',false);
			//setNGclear('cmplx_EmploymentDetails_PremType',true);
		//setVisible('cmplx_EmploymentDetails_Withinminwait',false);
		//EMploymentDetails_Label54
		setVisible('cmplx_EmploymentDetails_Is_group_insur_policy',false);
 setVisible('cmplx_EmploymentDetails_Is_life_insur_lessthan_5',false);
	}
		
		else{
			setVisible('EMploymentDetails_Label7',false);
			setVisible('cmplx_EmploymentDetails_OtherBankCAC',false);
			setVisible('cmplx_EmploymentDetails_IndusSeg',true);//change by sagarika:suggested by simi -10/12/2020
			setVisible('EMploymentDetails_Label59',true);
			//below added by nikhil to hide button also
			setVisible('EmploymentDetails_Bank_Button',false);
			//changes done by nikhil for PCAS-2483
			setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			setVisible('EMploymentDetails_Label41',true);
				setVisible('EMploymentDetails_Label52',false);//jira 2533 sagarika
		setVisible('EMploymentDetails_Label47',false);
		setVisible('EMploymentDetails_Label46',false);
		setVisible('EMploymentDetails_Label44',false);
		setVisible('EMploymentDetails_Label43',false);
		setVisible('EMploymentDetails_Label42',false);
		//setVisible('EMploymentDetails_Label14',true);
		
		setVisible('cmplx_EmploymentDetails_PremAmt',false);
		setVisible('cmplx_EmploymentDetails_MinimumWait',false);
		setVisible('cmplx_EmploymentDetails_InsuranceValue',false);
		setVisible('cmplx_EmploymentDetails_PremPaid',false);
		setVisible('cmplx_EmploymentDetails_RegPayment',false);
		setVisible('cmplx_EmploymentDetails_PremType',false);
			setVisible('EMploymentDetails_Label54',false);
		setVisible('cmplx_EmploymentDetails_MotorInsurance',false);
		setVisible('cmplx_EmploymentDetails_Is_group_insur_policy',false);
 setVisible('cmplx_EmploymentDetails_Is_life_insur_lessthan_5',false);
		}
		if(getNGValue(pId)=='CAC')
		{
			//setLocked('ExtLiability_CACIndicator',false);
			//setEnabled('ExtLiability_CACIndicator',true);
			setVisible('ExtLiability_CACIndicator',true);
		}
		else
		{
			//setLocked('ExtLiability_CACIndicator',true);
			//setEnabled('ExtLiability_CACIndicator',false);
			setVisible('ExtLiability_CACIndicator',false);
			setNGValue('ExtLiability_CACIndicator',false);
			
			
		}
		return true; // for Tier No. change
	}	
	//Prateek change 5-12-2017 : In below _IncomeDetails_BankStat fields cmplx_ added after discussion with deepak
	else if(pId=='cmplx_IncomeDetails_BankStatFromDate'){
		validateFutureDate(pId);
		if(CompareFrom_ToDate(pId,'IncomeDetails_BankStatToDate')==false){
			document.getElementById(pId).value=null;
		}
	}
	else if(pId=='cmplx_IncomeDetails_BankStatToDate'){
		validateFutureDate(pId);
		if(CompareFrom_ToDate('IncomeDetails_BankStatFromDate',pId)==false){
			document.getElementById(pId).value=null;	
		}	
	}	
	
	else if(pId=='TLExpiry'||pId=='cmplx_CompanyDetails_cmplx_CompanyGrid_TLExpiry')
		validatePastDate(pId,'Trade License Expiry');
	
	 else if(pId=='estbDate')
		 validateFutureDate(pId);
	 
	else if(pId=='eff_lob_date')
		 validateFutureDate(pId);
	 
	else if(pId=='CompanyDetails_DatePicker1')
		 validateFutureDate(pId);
		 
	else if(pId=='cmplx_Customer_PassPortExpiry' || pId=='SupplementCardDetails_PassportExpiry')
		validatePastDate(pId,'PassPort Expiry');
		
	else if(pId=='cmplx_Customer_EmirateIDExpiry' || pId=='SupplementCardDetails_EmiratesIDExpiry')
		validatePastDate(pId,'Emirate ID Expiry');
		
	else if(pId=='cmplx_Customer_VisaExpiry' || pId=='SupplementCardDetails_VisaExpiry')
		validatePastDate(pId,'Visa Expiry');
		
	else if(pId=='cmplx_CompanyDetails_cmplx_CompanyGrid_TLExpiry')
		validatePastDate(pId,'TL Expiry');
		
	else if(pId=='cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails_AuthSignPassportExpiry')
		validatePastDate(pId,'Passport Expiry');
		
	else if(pId=='AuthorisedSignDetails_DOB')
		validateFutureDate(pId);
		
	else if(pId=='cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails_AuthSignVisaExpiry')
		validatePastDate(pId,'Visa Expiry');
		
	else if(pId=='PartnerDetails_Dob')
		validateFutureDate(pId);
		
		// below code already present - 06-10-2017 two times written
	/*else if(pId=='PartnerDetails_Dob'){
		validateFutureDate(pId);
		}*/
		// below code already present - 06-10-2017 EmploymentDetails_DOJ
		
	//below by nikhil for PCSP-90	
	/*else if(pId=='cmplx_EmploymentDetails_DOJ'){
		validateFutureDate(pId);
		calcLOSInCurrCompany(pId);
	}*/
		
	
	else if(pId=='cmplx_EmploymentDetails_DOLPrev'){
		validateFutureDate(pId);
		calcLOSInPreCompany(pId);
	}	
	else if(pId=='cmplx_Customer_PAssportNo')
	{
		setNGValueCustom('PartMatch_newpass',getNGValue(pId));
	}
	// below code already present - 06-10-2017 two times written
	/*	
	else if(pId=='cmplx_EmploymentDetails_DOJPrev'){
		validateFutureDate(pId);
		calcLOSInPreCompany(pId);
	}*/
	else if(pId=='cmplx_EligibilityAndProductInfo_FirstRepayDate'){	
		if(validatePastDate(pId,'First Repayment')){
		// FirstRepaymentDate();
			var maturityDate=MaturityDate();
			setNGValueCustom("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
			var maturityAge=calcAge(getNGValue('cmplx_Customer_DOb'),maturityDate);
				//setNGValueCustom("cmplx_EligibilityAndProductInfo_AgeAtMaturity",maturityAge);
			var Days= Moratorium();
			setNGValueCustom('cmplx_EligibilityAndProductInfo_Moratorium',Days);
		}
	}
	else if(pId=='cmplx_EligibilityAndProductInfo_MaturityDate')	
		validatePastDate(pId,'Maturity');
	
	else if(pId=='cmplx_EligibilityAndProductInfo_Moratorium')
		FirstRepaymentDate();
		
	/*else if(pId=='AlternateContactDetails_MobileNo1')
	{
		var Mobile_no=getNGValue('AlternateContactDetails_MobileNo1');
		var mob_No="00971"+Mobile_no;
		setNGValueCustom('AlternateContactDetails_MobileNo1',mob_No);
		return true;
	}*/
	
	/*else if(pId=='AlternateContactDetails_MobNo2')
	{
		var Mobile_no=getNGValue('AlternateContactDetails_MobNo2');
		var mob_No="00971"+Mobile_no;
		setNGValueCustom('AlternateContactDetails_MobNo2',mob_No);
		return true;
	}*/
//changed by nikhil
	if (pId=='AlternateContactDetails_Email1' || pId=='AlternateContactDetails_Email2' || pId=='cmplx_CustDetailVerification_email1_upd' || pId=='cmplx_CustDetailVerification_email2_upd' || pId=='SupplementCardDetails_EmailID' || pId=='cmplx_OffVerification_hrdemailid')
	{
		validateMail1(pId);
	}
	if (pId=='AlternateContactDetails_Email1' && (validateMail1('AlternateContactDetails_Email2') || getNGValue('AlternateContactDetails_Email2')==''))
	{
		
		setNGValueCustom('AlternateContactDetails_Email2',getNGValue('AlternateContactDetails_Email1'));
	}
	
	
		
	//added By Tarang for drop 4 point 1 started on 02/22/2018
	else if (pId=='AlternateContactDetails_CardDisp'){
		var cardDisp=getNGSelectedItemText('AlternateContactDetails_CardDisp');
		if(cardDisp=='International dispatch-INTL' || cardDisp=='Holding WI- HOLD' || cardDisp=='Card centre collection' || cardDisp=='COURIER'){
			setNGValueCustom('Card_Dispatch_Option',cardDisp);
		}
		else{
			setNGValueCustom('Card_Dispatch_Option','Branch');
		}
		//alert(getNGValue('Card_Dispatch_Option'));
	}
	//added By Tarang for drop 4 point 1 ended on 02/22/2018
	
	else if(pId=='cmplx_CC_Loan_TransMode')
	{
		if(getNGValue(pId)=='Cheque'){
			setLockedCustom('cmplx_CC_Loan_mchequeno',false);
			setLockedCustom('cmplx_CC_Loan_mchequeDate',false);
			setLockedCustom('cmplx_CC_Loan_mchequestatus',false);
		}
		else{
			setLockedCustom('cmplx_CC_Loan_mchequeno',true);
			setLockedCustom('cmplx_CC_Loan_mchequeDate',true);
			setLockedCustom('cmplx_CC_Loan_mchequestatus',true);
		}	
	}
	//Deepak 23 Dec changes done as per Mansih Point
	else if(pId=='cmplx_CustDetailVerification_mobno1_ver' || pId=='cmplx_CustDetailVerification_mobno2_ver' || pId=='cmplx_CustDetailVerification_dob_verification' || pId=='cmplx_CustDetailVerification_POBoxno_ver' || pId=='cmplx_CustDetailVerification_emirates_ver' || pId=='cmplx_CustDetailVerification_persorcompPOBox_ver' || pId=='cmplx_CustDetailVerification_offtelno_ver' || pId=='cmplx_CustDetailVerification_email1_ver' || pId=='cmplx_CustDetailVerification_email2_ver'||pId=='cmplx_CustDetailVerification_Mother_name_ver'){
		setNGValueCustom('cmplx_CustDetailVerification_Decision','');
	}
	else if(pId=='cmplx_OffVerification_hrdemailverified' || pId=='cmplx_OffVerification_fxdsal_ver' || pId=='cmplx_OffVerification_accpvded_ver' || pId=='cmplx_OffVerification_desig_ver' || pId=='cmplx_OffVerification_doj_ver' || pId=='cmplx_OffVerification_cnfminjob_ver'){
		setNGValueCustom('cmplx_OffVerification_Decision','');
	}
	else if(pId=='cmplx_LoanandCard_cardtype_ver' || pId=='cmplx_LoanandCard_cardlimit_ver'){
		setNGValueCustom('cmplx_LoanandCard_Decision','');
	}
	
	else if(pId=='cmplx_CustDetailVerification_Decision' || pId=='cmplx_OffVerification_Decision' || pId=='cmplx_LoanandCard_Decision')
	{
	//Deepak 23 Dec condiotn added for CPV PCSP-184
		if(getNGValue('cmplx_DEC_Decision')!='--Select--' && getNGValue('cmplx_DEC_Decision')!=null){
			showAlert(pId,'Value can not be changed as final decision is already selected');
			//change done by nikhil cpv 16-04
			setNGValue(pId,'--Select--');
			return false;
			
		}
		if(getNGValue(pId)=='Positive' || getNGValue(pId)=='Approve Sub to CIF'){
			var ver;
			if(pId=='cmplx_CustDetailVerification_Decision'){
			 ver="cmplx_CustDetailVerification_mobno1_ver:cmplx_CustDetailVerification_mobno2_ver:cmplx_CustDetailVerification_dob_verification:cmplx_CustDetailVerification_POBoxno_ver:cmplx_CustDetailVerification_emirates_ver:cmplx_CustDetailVerification_persorcompPOBox_ver:cmplx_CustDetailVerification_offtelno_ver:cmplx_CustDetailVerification_email1_ver:cmplx_CustDetailVerification_email2_ver:cmplx_CustDetailVerification_Mother_name_ver";
				if(!checkMandatory(CC_Custdetail_CPV))
				{
					setNGValueCustom(pId,'');
					return false;
				}
			}
			else if(pId=='cmplx_OffVerification_Decision'){
			ver="cmplx_OffVerification_hrdemailverified:cmplx_OffVerification_fxdsal_ver:cmplx_OffVerification_accpvded_ver:cmplx_OffVerification_desig_ver:cmplx_OffVerification_doj_ver:cmplx_OffVerification_cnfminjob_ver";	
			if (getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				if(!checkMandatory(CC_SELF_OFFICEVERIFICATION))
				{
					setNGValueCustom(pId,'');
					return false;
				}
			}
			else{
					if(!checkMandatory(CC_OFFICEVERIFICATION) )
					{
						
						
						setNGValueCustom(pId,'');
						return false;
					}
					else if(getNGValue('cmplx_OffVerification_cnfminjob_ver')!='NA' && (getNGValue('cmplx_OffVerification_cnfminjob_ver')=='--Select--' || getNGValue('cmplx_OffVerification_cnfminjob_ver')=='' || getNGValue('cmplx_OffVerification_cnfminjob_ver')==' ') && isLocked('cmplx_OffVerification_cnfminjob_ver')!=true && isEnabled('cmplx_OffVerification_cnfminjob_ver')!=false)
					
					{
					showAlert('cmplx_OffVerification_cnfminjob_ver' , 'Confirmed in job Verification is Mandatory!' )
					setNGValueCustom(pId,'');
						return false;
					}
					
				}	
			}
			else if(pId=='cmplx_LoanandCard_Decision'){
			 ver="cmplx_LoanandCard_cardtype_ver:cmplx_LoanandCard_cardlimit_ver";
			 if(!checkMandatory(CC_LoanCard_CPV))
					{
						setNGValueCustom(pId,'');
						return false;
					}
			}
			if(getNGValue('cmplx_OffVerification_hrdemailverified')=='Yes')
			{
			var verificationFields_array=ver.split(':');
			for(var i=0;i<verificationFields_array.length;i++){
				if(getNGValue(verificationFields_array[i])=='No'){
						showAlert(verificationFields_array[i],'Decision cannot be taken positive since Verification is selected as No!!');
						setNGValueCustom(pId,'');
				}
			}
			}
		}
		//below code added by nikhil for CPV changes 17-04
		if(pId=='cmplx_CustDetailVerification_Decision')
		{
			if (getNGValue(pId)=='Positive' && (getNGValue('cmplx_CustDetailVerification_mobno1_ver') == 'Mismatch' || getNGValue('cmplx_CustDetailVerification_POBoxno_ver') == 'Mismatch' || getNGValue('cmplx_CustDetailVerification_email1_ver') == 'Mismatch' || getNGValue('cmplx_CustDetailVerification_emirates_ver') == 'Mismatch'   || getNGValue('cmplx_CustDetailVerification_persorcompPOBox_ver') == 'Mismatch'))
			{
			showAlert(pId, 'Decision cannot be Approve as Mismatch in Customer Details. Please Select Approve Sub to CIF!');
			setNGValue(pId,'--Select--');
			return false;
			}
			if (getNGValue(pId)=='Approve Sub to CIF' && (getNGValue('cmplx_CustDetailVerification_mobno1_ver') != 'Mismatch' && getNGValue('cmplx_CustDetailVerification_POBoxno_ver') != 'Mismatch' && getNGValue('cmplx_CustDetailVerification_email1_ver') != 'Mismatch') && getNGValue('cmplx_CustDetailVerification_emirates_ver') != 'Mismatch' && getNGValue('cmplx_CustDetailVerification_persorcompPOBox_ver') != 'Mismatch'    )
			{
			showAlert(pId, 'Decision cannot be Approve Sub to CIF as no Mismatch in Customer Details.');
			setNGValue(pId,'--Select--');
			return false;
			}
			if(getNGValue(pId)=='Approve Sub to CIF' && getNGValue('IS_Approve_Cif')!='C')
			{
				setNGValue('IS_Approve_Cif','Y');
			}
			else
			{
				setNGValue('IS_Approve_Cif','NA');
			}
		}	
	}
	//below code added by nikhil PCSP-92
	else if(pId=='cmplx_EmploymentDetails_EmpStatus')
	{
		return true;
	}
	

	else if(pId=='cmplx_DEC_Decision')
	{
		var activityName = window.parent.stractivityName;
		//PCSP-365
		if (getNGValue(pId).toUpperCase()!='REFER'){
		com.newgen.omniforms.formviewer.NGClear('DecisionHistory_ReferTo');
		setLockedCustom('DecisionHistory_ReferTo',true);
		
		if(getNGValue(pId).toUpperCase()!='REJECT'){
			com.newgen.omniforms.formviewer.NGClear('DecisionHistory_dec_reason_code');
		}
		}
	
	if((activityName=='CSM') || (activityName=='SalesCoordinatorCSM'))
	{
			
	
		if(getNGValue(pId).toUpperCase()!='REJECT'){
			
			//12th september
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
			//setEnabledCustom('DecisionHistory_dec_reason_code',false);
			//setVisible('cmplx_DEC_RejectReason',true);--commented by akshay on 4/1/17
			 // setEnabledCustom('cmplx_DEC_RejectReason',true);
			//12th september
			setVisible('DecisionHistory_Button2',false);
			setVisible('DecisionHistory_Label6',false);
			//commented by nikhil for wrong code in 
			//setLockedCustom('DecisionHistory_Decision_ListView1',true);
			
			}
		}
			//added
			//++Below code added by nikhil 13/11/2017 for Code merge

		else if(activityName=='FCU'||activityName=='FPU')
		{
			//++Below code added by  nikhil 26/10/17 as per CC FSD 2.2 
			//setVisible("DecisionHistory_save",true);//save
			//setTop("DecisionHistory_save","250px");
			//--above code added by  nikhil 26/10/17 as per CC FSD 2.2 
			if(getNGValue(pId).toUpperCase()=='REJECT')
			{
				//--commented by akshay on 4/1/17
			//setVisible("DecisionHistory_dec_reason_code",true);//rsn code
			//setVisible("DecisionHistory_Label11",true);//rsn code label
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
			//setEnabledCustom('DecisionHistory_dec_reason_code',true);
			//setTop("DecisionHistory_Label11","110px");
			//setTop("DecisionHistory_dec_reason_code","126px");
			//setLeft("DecisionHistory_Label11","400px");
			//setLeft("DecisionHistory_dec_reason_code","400px");
			//setVisible("DecisionHistory_save",true);//save
			//setTop("DecisionHistory_save","250px");
			
			
		}
		else
		{//--commented by akshay on 4/1/17
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
			//setEnabledCustom('DecisionHistory_dec_reason_code',false);
			//setVisible("DecisionHistory_dec_reason_code",false);//rsn code
			//setVisible("DecisionHistory_Label11",false);
			//setVisible("DecisionHistory_save",true);//save
			//setTop("DecisionHistory_save","250px");
		}
	}
	else if(activityName=='CPV' || activityName=='CPV_Analyst')//sagarika for PCAS 2372
		{
			setVisible("DecisionHistory_Button2",false);
			setVisible("DecisionHistory_Label6",false);
			if(getNGValue(pId)=='Approve' && (getNGValue('cmplx_OffVerification_Decision')=='' || getNGValue('cmplx_OffVerification_Decision')=='--Select--'))//sagarikaPCA-2769
			{
				showAlert('cmplx_OffVerification_Decision','User cannot take the decision as Office Verification decision is blank');
				setNGValue(pId,'--Select--');
				return false;
			}
			
			
			
		//below all code chenged by nikhil fo PCSP-74/75/76
		if(getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF' || getNGValue(pId)=='Reject')
		{
			if ( getNGValue('cmplx_HCountryVerification_Decision')=='Pending' )
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Home Country Verification decision is '+getNGValue('cmplx_HCountryVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_HCountryVerification_Decision') == 'Negative' && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Home Country Verification decision is '+getNGValue('cmplx_HCountryVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_ResiVerification_Decision')=='Pending')
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Residence Verification decision is '+getNGValue('cmplx_ResiVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_ResiVerification_Decision') == 'Negative' && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Residence Verification decision is '+getNGValue('cmplx_ResiVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		//commented by nikhil for CPV changes 16-04
		/*else if (getNGValue('cmplx_CustDetailVerification_Decision')=='Pending')
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Customer Detail Verification decision is '+getNGValue('cmplx_CustDetailVerification_Decision'));
			setNGValue(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_CustDetailVerification_Decision') == 'Negative' && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Customer Detail Verification decision is '+getNGValue('cmplx_CustDetailVerification_Decision'));
			setNGValue(pId,'--Select--');
			return false;
		}*/
		else if (getNGValue('cmplx_BussVerification_Decision')=='Pending')
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Bussiness Verification decision is '+getNGValue('cmplx_BussVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_BussVerification_Decision') == 'Reject' && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Bussiness Verification decision is '+getNGValue('cmplx_BussVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_RefDetVerification_Decision')=='Pending')
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as reference Verification decision is '+getNGValue('cmplx_RefDetVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_RefDetVerification_Decision') == 'Negative' && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as reference Verification decision is '+getNGValue('cmplx_RefDetVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if ((getNGValue('cmplx_OffVerification_Decision')=='Pending') && !(isLocked('cmplx_OffVerification_Decision') && !isEnabled('OfficeandMobileVerification_Enable')))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Office Verification decision is '+getNGValue('cmplx_OffVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if ((getNGValue('cmplx_OffVerification_Decision') == 'Negative' ) && !(isLocked('cmplx_OffVerification_Decision') && !isEnabled('OfficeandMobileVerification_Enable')) && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Office Verification decision is '+getNGValue('cmplx_OffVerification_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_LoanandCard_Decision')=='Pending')
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Loan and Card Verification decision is '+getNGValue('cmplx_LoanandCard_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		 else if (getNGValue('cmplx_LoanandCard_Decision') == 'Negative'  && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Loan and Card Verification decision is '+getNGValue('cmplx_LoanandCard_Decision'));
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		//done by nikhil for cpv changes 16-04
		/*else if (getNGValue(pId)=='Approve' && (getNGValue('cmplx_CustDetailVerification_mobno1_ver') == 'Mismatch' || getNGValue('cmplx_CustDetailVerification_POBoxno_ver') == 'Mismatch' || getNGValue('cmplx_CustDetailVerification_email1_ver') == 'Mismatch') )
		{
			showAlert(pId, 'Decision cannot be Approve as Mismatch in Customer Details. Please Select Approve Sub to CIF!');
			setNGValue(pId,'--Select--');
			return false;
		}*/
		//below comdition added by nikhil for PCSP-184
		
		//below comdition added by Aman  for PCSP-40
		/*else if (getNGValue(pId)=='Approve Sub to CIF' && (getNGValue('cmplx_CustDetailVerification_mobno1_ver') != 'Mismatch' && getNGValue('cmplx_CustDetailVerification_POBoxno_ver') != 'Mismatch' && getNGValue('cmplx_CustDetailVerification_email1_ver') != 'Mismatch') )
		{
			showAlert(pId, 'Decision cannot be Approve Sub to CIF as no Mismatch in Customer Details.');
			setNGValue(pId,'--Select--');
			return false;
		}*/
		//below comdition added by Aman for PCSP-40	
		//changed by nikhil for cpv changes 16-04
		else if(getNGValue(pId)=='Reject' && (getNGValue('cmplx_HCountryVerification_Decision')=='Positive' || getNGValue('cmplx_HCountryVerification_Decision')==null) && (getNGValue('cmplx_ResiVerification_Decision')=='Positive' || getNGValue('cmplx_ResiVerification_Decision')==null) && (getNGValue('cmplx_BussVerification_Decision')=='Approved' || getNGValue('cmplx_BussVerification_Decision')==null)
		&& (getNGValue('cmplx_RefDetVerification_Decision')=='Positive' || getNGValue('cmplx_RefDetVerification_Decision')==null) && (getNGValue('cmplx_OffVerification_Decision')=='Positive' || getNGValue('cmplx_OffVerification_Decision')==null) && (getNGValue('cmplx_LoanandCard_Decision')=='Positive' || getNGValue('cmplx_LoanandCard_Decision')==null))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as all verification is Positive');
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		//change by saurabh for task list points on 11/4/19.
		if(!isLocked('cmplx_DEC_SetReminder')){
			setLockedCustom('cmplx_DEC_SetReminder',true);
		}
		setNGValueCustom('cmplx_DEC_SetReminder','');
		
	}
	//change by saurabh for task list points on 11/4/19.
	else if(getNGValue(pId)=='CPV Hold'){
		setLockedCustom('cmplx_DEC_SetReminder',false);
		setNGValueCustom('cmplx_DEC_SetReminder',"");
	}
	
	}
	else if(activityName=='CPV_Analyst')
		{
			setVisible("DecisionHistory_Button2",false);
			setVisible("DecisionHistory_Label6",false);
			//++Below code added by  nikhil 26/10/17 as per CC FSD 2.2 
			//setVisible("DecisionHistory_save",true);//save
			//setTop("DecisionHistory_save","250px");
			//--above code added by  nikhil 26/10/17 as per CC FSD 2.2 
			if(getNGValue(pId).toUpperCase()=='REJECT')
			{
			//--commented by akshay on 4/1/17
			//setVisible("DecisionHistory_dec_reason_code",true);//rsn code
			//setVisible("DecisionHistory_Label11",true);//rsn code label
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
			//setEnabledCustom('DecisionHistory_dec_reason_code',true);
			//setTop("DecisionHistory_Label11","110px");
			//setTop("DecisionHistory_dec_reason_code","126px");
			//setLeft("DecisionHistory_Label11","400px");
			//setLeft("DecisionHistory_dec_reason_code","400px");
			//setVisible("DecisionHistory_save",true);//save
			//setTop("DecisionHistory_save","250px");
			
			
		}
		else
		{
			//--commented by akshay on 4/1/17
			setVisible("DecisionHistory_Button2",false);
			setVisible("DecisionHistory_Label6",false);
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
			//setEnabledCustom('DecisionHistory_dec_reason_code',false);
			//setVisible("DecisionHistory_dec_reason_code",false);//rsn code
			//setVisible("DecisionHistory_Label11",false);
			//setVisible("DecisionHistory_save",true);//save
			//setTop("DecisionHistory_save","250px");
		}
	}
	//--Above code added by nikhil 13/11/2017 for Code merge
	else if (activityName=='Compliance'){
		if(getNGValue(pId).toUpperCase()=='REJECT'){
						//--commented by akshay on 4/1/17
			//setVisible("DecisionHistory_dec_reason_code",true);//rsn code
			//setVisible("DecisionHistory_Label11",true);//rsn code label
			//setEnabledCustom('DecisionHistory_dec_reason_code',true);
			//setTop("DecisionHistory_dec_reason_code","90px");//rsn code
			//setTop("DecisionHistory_Label11","74px");
			//setLeft("DecisionHistory_dec_reason_code","280px");//rsn code
    		//setLeft("DecisionHistory_Label11","280px");
			}
		else{
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
			//setEnabledCustom('DecisionHistory_dec_reason_code',false);
			//setVisible("DecisionHistory_dec_reason_code",false);//rsn code
			//setVisible("DecisionHistory_Label11",false);
		}
		
	}
	// ++ below code already present - 06-10-2017 decision validation
	
	else if (activityName=='Original_Validation'){
		if(getNGValue(pId).toUpperCase()=='REJECTED'){
						//--commented by akshay on 4/1/17
			//setVisible("DecisionHistory_dec_reason_code",true);//rsn code
			//setVisible("DecisionHistory_Label11",true);//rsn code label
			//setEnabledCustom('DecisionHistory_dec_reason_code',true);
			//setTop("DecisionHistory_dec_reason_code","90px");//rsn code
			//setTop("DecisionHistory_Label11","74px");
			//setLeft("DecisionHistory_dec_reason_code","580px");//rsn code
    		//setLeft("DecisionHistory_Label11","580px");
			}
		else{
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
			//setEnabledCustom('DecisionHistory_dec_reason_code',false);
			//setVisible("DecisionHistory_dec_reason_code",false);//rsn code
			//setVisible("DecisionHistory_Label11",false);
		}
		
	}
	//22nd september
	
	//added by nikhil as per CC FSD
	else if (activityName=='Smart_CPV'){
				//--commented by akshay on 4/1/17	
			setVisible("DecisionHistory_Button2",false);
			//setVisible("DecisionHistory_dec_reason_code",true);
			//setVisible('DecisionHistory_Label11',true);
			if(getNGValue(pId)=='Smart CPV Hold')
		{
			setLocked('cmplx_DEC_SetReminder_Smart',false);
			setLocked('cmplx_DEC_NoOfAttempts_Smart',true);
			
		}
		else{
			if(getNGValue(pId)=='Smart CPV Hold')
		{
			setLocked('cmplx_DEC_SetReminder_Smart',true);
			setLocked('cmplx_DEC_NoOfAttempts_Smart',true);
			
		}
		}
			
			
		
	}
	// ++ above code already present - 06-10-2017 decision validation
	else if (activityName=='CAD_Analyst1')
	{
		setEnabledCustom("cmplx_DEC_Manual_Deviation", true);
		//below code added by nikhil for PCV changes 17-04
		//chnages done by nikhil for PCAS-2282
		if(getNGValue(pId)=='Approve' )
		{
		if (getNGValue('cmplx_CustDetailVerification_Decision')=='Pending')
		 {
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Customer Detail Verification decision is '+getNGValue('cmplx_CustDetailVerification_Decision'));
			setNGValue(pId,'--Select--');
			return false;
		}
		else if (getNGValue('cmplx_CustDetailVerification_Decision') == 'Negative' && (getNGValue(pId)=='Approve' ))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as Customer Detail Verification decision is '+getNGValue('cmplx_CustDetailVerification_Decision'));
			setNGValue(pId,'--Select--');
			return false;
		}
		setLocked('cmplx_DEC_SetReminder_CA',true);
			setLocked('cmplx_DEC_NoOfAttempts_CA',true);
		}
		if(getNGValue(pId).toUpperCase()=='REJECT')
		{
			//below code added by nikhil for PCSP-319
			 if(parseInt(getNGValue('reEligibility_CC_counter').split(';')[0])==0)
			 {
				showAlert(pId,'Please calculate Re-eligibility from EligibilityAndProductInformation');
				setNGValueCustom(pId,'--Select--');
				return false;				
			 }
			 //below code added by nikhil for PCSP-671
			 else if(getNGValue('cmplx_DEC_Manual_Deviation')==true && parseInt(getNGValue('reEligibility_CC_counter').split(';')[1])==0){
				showAlert(pId,'Please calculate Re-eligibility first');
				setNGValueCustom(pId,'--Select--');
				return false;		
				}
		//--commented by akshay on 4/1/17
			setVisible('DecisionHistory_Button2',false);//cif button
			  //setEnabledCustom('cmplx_DEC_ReferReason',true);
			  //setEnabledCustom('DecisionHistory_dec_reason_code',true);
			  //setVisible('DecisionHistory_dec_reason_code',true);
			  //setVisible('DecisionHistory_Label11',true);			  
			   
            //setVisible('DecisionHistory_Label6',true);
           // setVisible('cmplx_DEC_RejectReason',true);
           // setVisible('DecisionHistory_Button2',true);
        setLocked('cmplx_DEC_SetReminder_CA',true);
			setLocked('cmplx_DEC_NoOfAttempts_CA',true);
			//setEnabledCustom("cmplx_DEC_Manual_Deviation", false);
			//setNGValueCustom('cmplx_DEC_Manual_Deviation',false);//commmented by aman for PCSP-391
			setLockedCustom('DecisionHistory_ManualDevReason',false);// Changed by aman for PCSP-331
			//enable cad- dec tray
			setLockedCustom('cmplx_DEC_ReferTo',true); // hritik cpv changes
			//if(getNGValue('DecisionHistory_dec_reason_code')==""||getNGValue('DecisionHistory_dec_reason_code')=="--Select--"){
			//	showAlert('DecisionHistory_dec_reason_code',alerts_String_Map['VAL198']);
			//	return false;	
			//}
						
		}
		else if(getNGValue(pId).toUpperCase()=='REFER')
		{
		//below code added by nikhil for PCSP-319
			 if(parseInt(getNGValue('reEligibility_CC_counter').split(';')[0])==0)
			 {
				showAlert(pId,'Please calculate Re-eligibility from EligibilityAndProductInformation');
				setNGValueCustom(pId,'--Select--');
				return false;				
			 }
			 //below code added by nikhil for PCSP-671
			 else if(getNGValue('cmplx_DEC_Manual_Deviation')==true && parseInt(getNGValue('reEligibility_CC_counter').split(';')[1])==0){
				showAlert(pId,'Please calculate Re-eligibility first');
				setNGValueCustom(pId,'--Select--');
				return false;		
				}

		//enable cad- dec tray
			//changes doen for PCSP-690 30/1
			if(getNGValue('DecisionHistory_ReferTo')=='Smart CPV' || getNGValue('DecisionHistory_ReferTo')=='Mail Approval')
			{
			setLockedCustom('cmplx_DEC_ReferTo',true);// hritik cpv changes
			}
			else
			{
			setLockedCustom('cmplx_DEC_ReferTo',true);
			}
			setNGValueCustom('DecisionHistory_ReferTo','');
			setLocked('cmplx_DEC_SetReminder_CA',true);
			setLocked('cmplx_DEC_NoOfAttempts_CA',true);
			
		}
		
	 else if(getNGValue(pId)=='CA_HOLD')
		{
			setLocked('cmplx_DEC_SetReminder_CA',false);
			setLocked('cmplx_DEC_NoOfAttempts_CA',true);
			
		}
		
		 else if(getNGValue(pId)=='Refer to Credit')
		{
			setEnabledCustom("cmplx_DEC_Manual_Deviation", true);
			//setEnabledCustom('cmplx_DEC_ReferTo',true);
			
		}
		else{
		//disable cad- dec tray
			setLockedCustom('cmplx_DEC_ReferTo',true);
	//change by saurabh on 30th Nov for FSD 2.7
	//change made by saurabh on 14th Dec
			if(getNGValue(pId)=='APPROVE' || getNGValue(pId)=='Approve'){
				if(getNGValue('Is_Financial_Summary')!='Y' && getNGValue('cmplx_Customer_NTB')==false && getNGValue('Account_Number')!='') {
				showAlert('IncomeDetails','Please fetch Financial Summary first');
				setNGValueCustom(pId,'--Select--');
				return false;
				}
				//below code added by nikhil for PCSP-504 Sprint-2
				//deepak activityName condition added by for PCAS-2125
				else if(getNGValue('ELigibiltyAndProductInfo_EFMS_Status')!='Y' && activityName=='CAD_Analyst1')
				{
				showAlert(pId,alerts_String_Map['CC281']);
				setNGValueCustom(pId,'--Select--');
				return false;	
				}
				else if(parseInt(getNGValue('reEligibility_CC_counter').split(';')[0])==0){
				showAlert(pId,'Please calculate Re-eligibility from EligibilityAndProductInformation');
				setNGValueCustom(pId,'--Select--');
				return false;				
				}
				else if(getNGValue('cmplx_DEC_Manual_Deviation')==true && parseInt(getNGValue('reEligibility_CC_counter').split(';')[1])==0){
				showAlert(pId,'Please calculate Re-eligibility first');
				setNGValueCustom(pId,'--Select--');
				return false;		
				}
				else if(getNGValue('cmplx_EmploymentDetails_FieldVisitDone')=='A'){
				showAlert(pId,'Cannot take Decision Approved as Field Visit Done is Awaited');
				setNGValueCustom(pId,'--Select--');	
				return false;
				}
				else if(getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='FVR'){
				showAlert(pId,'Cannot take Decision Approved as Employer Status CC is Awaiting FVR');
				setNGValueCustom(pId,'--Select--');	
				return false;
				}
				//change by saurabh on 7th DEC
				/*if(getNGValue('Is_CAM_generated')!='Y'){
				showAlert(pId,'Cannot take Decision Approved as CAM report not generated');
				setNGValueCustom(pId,'--Select--');
				return false;
				}*/
				setLocked('cmplx_DEC_SetReminder_CA',true);
			setLocked('cmplx_DEC_NoOfAttempts_CA',true);
				setNGValueCustom('cmplx_DEC_ReferTo','');
			}
					//--commented by akshay on 4/1/17
			//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
				//setEnabledCustom('DecisionHistory_dec_reason_code',false);
				//setVisible('DecisionHistory_dec_reason_code',false);
				  //setVisible('DecisionHistory_Label11',false);
				//setEnabledCustom('cmplx_DEC_ReferTo',false);
		//	setEnabledCustom("cmplx_DEC_Manual_Deviation", false);
		//	setNGValueCustom('cmplx_DEC_Manual_Deviation',false);
			setLockedCustom('DecisionHistory_ManualDevReason',true);
				
		}
		
	}
	//below code added by nikhil for PCSP-504 Sprint-2
	else if (activityName=='Cad_Analyst2')
	{
	//below code added by nikhil for CPV changes 21-04-2019
	if(!CheckCADecision(getNGValue(pId)))
	{
		return false;
	}
	if(getNGValue(pId)=='CA_HOLD')
	{
	setLocked('cmplx_DEC_SetReminder_CA',false);
			setLocked('cmplx_DEC_NoOfAttempts_CA',true);
	}
	else{
	setLocked('cmplx_DEC_SetReminder_CA',true);
			setLocked('cmplx_DEC_NoOfAttempts_CA',true);
	}
	/*if(getNGValue(pId)=='APPROVE' || getNGValue(pId)=='Approve')
	{
	if(getNGValue('Is_CAM_generated')!='Y')
	{
		showAlert(pId,alerts_String_Map['CC282']);
		setNGValueCustom(pId,'--Select--');
		return false;
	}
	}//sagarika for CAM changes.*/
	}
	
	return true;
	}
	
	//added by akshay on 9/12/17 for multiple refer
	else if(pId=='DecisionHistory_ReferTo'){
		if(activityName!='CAD_Analyst1' && activityName!='CPV'){//on these user can select multiple refer  
			var selectedRows_Index=com.newgen.omniforms.formviewer.getNgSelectedIndices(pId);//returns array of index of selected rows
			if(selectedRows_Index.length>1){
				showAlert(pId,alerts_String_Map['CC253']);
				//to deselect the selected rows
				for(var i=0;i<selectedRows_Index.length;i++){
					document.getElementById(pId)[selectedRows_Index[i]].selected=false;
				}
				return false;
			}
		}	
		//com.newgen.omniforms.formviewer.setNGValueCustom('ReferTo',com.newgen.omniforms.formviewer.getNgSelectedValues(pId));
		//var mulRefer= getNGValue('ReferTo');
		//changed by nikhil as refer to change not working
		setNGValueCustom('q_MailSubject',com.newgen.omniforms.formviewer.getNgSelectedValues(pId));
		var mulRefer= getNGValue('q_MailSubject');//Changed by aman for name change 100319
		var murReferSplit=mulRefer.split(',');
		if (murReferSplit.length>2)
		{
			showAlert(pId,alerts_String_Map['CC259']);
			return false;
		}
		//below code added by nikhil PCSP-317
		if(getNGValue('cmplx_DEC_Decision')=='Refer' && (getNGValue('DecisionHistory_ReferTo')=='Smart CPV' || getNGValue('DecisionHistory_ReferTo')=='Mail Approval'))
		{
		//Below code added by nikhil for PCSP-754
		setNGValueCustom('cmplx_DEC_ReferTo','--Select--');
		setLockedCustom('cmplx_DEC_ReferTo',true);		
		}
		else
		{
		setLockedCustom('cmplx_DEC_ReferTo',true); // hritik cpv changes
		}
		return true;
	}	
	//ended by akshay on 9/12/17 for multiple refer
	
	//below code added by nikhil for employment_detail match
	else if(pId=='cmplx_OffVerification_Decision')
	{
		if(getNGValue('cmplx_OffVerification_Decision')=='Negative')
		setNGValueCustom('EMPLOYMENT_DETAILS_MATCH','No');
		else
		setNGValueCustom('EMPLOYMENT_DETAILS_MATCH','Yes');
	}
	
	
	
	//ended by yash for cc FSD
	else if(pId=='cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED')
	{
		
		return true;
	}	
	else if(pId=='cmplx_FinacleCore_cmplx_TurnoverNBC_BS_ANALYSED')
	{
		
		return true;
	}	
	/*if(pId=='cmplx_DEC_Decision'){
        var desc = getNGValue('cmplx_DEC_Decision');
        setNGValueCustom("Decision", desc);
        if(desc=='REJECT')
        {
		//++ Below code added by abhishek as per CC FSD 2.7.3
		//++ below code added by nikhil on 10/10/17
			if(activityName != 'CPV' || activityName != 'Hold_CPV' )
			//-- Above code added by nikhil on 10/10/17	
		//-- Above code added by abhishek as per CC FSD 2.7.3
           // setVisible('DecisionHistory_Label6',true);
            //setVisible('cmplx_DEC_RejectReason',true);
            setVisible('DecisionHistory_Button2',true);
        }
    }--commented by akshay on 5/1/17 as same event already exists*/
	
	//Commented by prabhakar  for jira 2539 code moved with  if(pId=='cmplx_Customer_MobileNo' || pId=='PartMatch_mno1' || pId=='PartMatch_mno2' )
	/*else if (pId =='PartMatch_mno1')	
	{
		if(getNGValue(pId)!="")
		{
		var Mobile_no=getNGValue(pId);
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValueCustom(pId,mob_No);
		}
		if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
			showAlert(pId,'Incorrect mobile number format');
			setNGValueCustom(pId,'');
		}		
		if(mob_No.length!=14){
			showAlert(pId,alerts_String_Map['VAL087']);
			return false;
		}	
		if(pId=='cmplx_Customer_MobileNo'){
		setNGValueCustom('OTP_Mobile_NO',mob_No);
		}//PCAS-2539 by sagarika
		
	}
	else if (pId =='PartMatch_mno2')	
	{
		var Mobile_no=getNGValue('PartMatch_mno2');
		var mob_No="00971"+Mobile_no;
		setNGValueCustom('PartMatch_mno2',mob_No);
	}*/
	/*else if (pId =='cmplx_Customer_EmiratesID' || pId =='cmplx_Customer_FirstNAme' || pId =='cmplx_Customer_MiddleNAme' || pId =='cmplx_Customer_LastNAme' || pId =='cmplx_Customer_DOb' || pId =='cmplx_Customer_Nationality' || pId =='cmplx_Customer_MobileNo' || pId =='cmplx_Customer_PAssportNo' || pId =='cmplx_Customer_CIFNo' )	
	{
		
		
		
		setLockedCustom("Customer_FetchDetails",false);
	}*/
	else if (pId =='cmplx_Customer_CIFNo'){ 
	
	    var partCIF = getNGValue('cmplx_Customer_CIFNo');
		/*if(partCIF !="" && partCIF.length!=7)
		{
			showAlert('cmplx_Customer_CIFNo',alerts_String_Map['VAL105']);
			return false;
		}*/
			setNGValueCustom('PartMatch_CIFID',getNGValue(pId));
		}	
		else if(pId=='cmplx_Liability_New_overrideIntLiab'){
		if(getNGValue('cmplx_Liability_New_overrideIntLiab')=='true')
		setVisible("Liability_New_Overwrite",true);
		else
		setVisible("Liability_New_Overwrite",false);
		
		}
		// added by abhishek as per CC FSD
		else if(pId=='transferMode'){
			if(getNGValue('transtype')=='' || getNGValue('transtype')==null){
				showAlert('transtype',alerts_String_Map['VAL397']);
				setNGValue('transferMode','');
					return false;
			}
			if(getNGValue('transferMode')=='A' && getNGValue('transtype')!='BT'){
				setNGValue('Account_No_for_Swift','');
				setLockedCustom("Account_No_for_Swift",true);
				setVisible("CC_Loan_Label23", false);
				setVisible("Account_No_for_Swift", false);
				setVisible("CC_Loan_Label24", true);
				setVisible("Account_No_for_AT", true);
				setLockedCustom("Account_No_for_AT",false);
			}
			else if((getNGValue('transferMode')=='S'|| getNGValue('transferMode')=='SWIFT')&& getNGValue('transtype')!='BT'){
				setNGValue('Account_No_for_AT','');
				setLockedCustom("Account_No_for_AT",true);
				setVisible("CC_Loan_Label24", false);
				setVisible("Account_No_for_AT", false);
				setVisible("CC_Loan_Label23", true);
				setVisible("Account_No_for_Swift", true);
				setLockedCustom("Account_No_for_Swift",false);
			}
			else{
				setNGValue('Account_No_for_Swift','');
				setNGValue('Account_No_for_AT','');
				setLockedCustom("Account_No_for_Swift",true);
				setLockedCustom("Account_No_for_AT",true);
				setVisible("CC_Loan_Label23", false);
				setVisible("Account_No_for_Swift", false);
				setVisible("CC_Loan_Label24", false);
				setVisible("Account_No_for_AT", false);
			}
		  if(getNGValue('transferMode')=='CHEQUE' || getNGValue('transferMode')=='C'){
				setLockedCustom("chequeNo",false);
				setLockedCustom("chequeDate",false);
				setLockedCustom("chequeStatus",false);
				setLockedCustom("dispatchChannel",false);
			}
			else {
				setLockedCustom("chequeNo",true);
				setLockedCustom("chequeDate",true);
				setLockedCustom("chequeStatus",true);
				setLockedCustom("dispatchChannel",true);
			}
			if(getNGValue('transtype')=='BT' && (getNGValue('transferMode')=='S'|| getNGValue('transferMode')=='SWIFT')){
				
				setLockedCustom("chequeNo",true);
				setLockedCustom("chequeDate",true);
				setLockedCustom("chequeStatus",true);
				setNGValue('dispatchChannel','');
				setLockedCustom("dispatchChannel",true);
				setNGValueCustom('creditcardNo','');
				setLockedCustom("creditcardNo",false);
				setLockedCustom("tenor",true);
			}
			else if(getNGValue('transtype')=='BT' && (getNGValue('transferMode')=='C'|| getNGValue('transferMode')=='CHEQUE')){
				setNGValue('dispatchChannel','998');
				setLockedCustom("dispatchChannel",true);
				setLockedCustom("chequeNo",false);
				setLockedCustom("chequeDate",false);
				setLockedCustom("chequeStatus",false);
				setNGValueCustom('creditcardNo','');
				setLockedCustom("creditcardNo",false);
				setLockedCustom("tenor",true);
			}
			else if(getNGValue('transtype')=='BT' && getNGValue('transferMode')=='A'){
				showAlert('transferMode',alerts_String_Map['VAL398']);
				setNGValue('transferMode','');
				return false;
			}
			else if(getNGValue('transtype')=='SC' && (getNGValue('transferMode')=='S'|| getNGValue('transferMode')=='SWIFT')){
				
				setNGValue('chequeNo','');//pcasp-1633
				setNGValue('chequeDate','');
				setNGValue('chequeStatus','');
				setNGValue('tenor','');
				setLockedCustom("chequeNo",true);
				setLockedCustom("chequeDate",true);
				setLockedCustom("chequeStatus",true);
				setNGValue('dispatchChannel','');
				setLockedCustom("dispatchChannel",true);
				setLockedCustom("tenor",true);
				setNGValueCustom('creditcardNo','');
				setLockedCustom("creditcardNo",true);
			}
			else if(getNGValue('transtype')=='SC' && (getNGValue('transferMode')=='C'|| getNGValue('transferMode')=='CHEQUE')){
				setLockedCustom("dispatchChannel",true);
				setNGValue('dispatchChannel','998');
				setLockedCustom("chequeNo",false);
				setLockedCustom("chequeDate",false);
				setLockedCustom("chequeStatus",false);
				setNGValue('tenor','');//pcasp-1377
				setLockedCustom("tenor",true);
				setNGValueCustom('creditcardNo','');
				setLockedCustom("creditcardNo",true);
				
			}
			else if(getNGValue('transtype')=='SC' && getNGValue('transferMode')=='A'){
				setNGValue('chequeNo','');//pcasp-1633
				setNGValue('chequeDate','');
				setNGValue('chequeStatus','');
				setNGValue('tenor','');
				setNGValue('dispatchChannel','');
				setLockedCustom("dispatchChannel",true);
				setLockedCustom("chequeNo",true);
				setLockedCustom("chequeDate",true);
				setLockedCustom("chequeStatus",true);
				setLockedCustom("tenor",true);
				setNGValueCustom('creditcardNo','');
				setLockedCustom("creditcardNo",true);
			}
			//for PCASP-1377
			else if(getNGValue('transtype')=='CCC' && (getNGValue('transferMode')=='S'|| getNGValue('transferMode')=='SWIFT')){
				setNGValue('chequeNo','');//pcasp-1377
				setNGValue('chequeDate','');//pcasp-1377
				setNGValue('chequeStatus','');//pcasp-1377
				setNGValue('tenor','');//pcasp-1377
				setLockedCustom("chequeNo",true);
				setLockedCustom("chequeDate",true);
				setLockedCustom("chequeStatus",true);
				setNGValue('dispatchChannel','');
				setLockedCustom("dispatchChannel",true);
				setLockedCustom("tenor",true);
				setNGValueCustom('bankName','');
				setLockedCustom("bankName",false);
				setLockedCustom("bankName",false);
			}
			else if(getNGValue('transtype')=='CCC' && (getNGValue('transferMode')=='C'|| getNGValue('transferMode')=='CHEQUE')){
				setLockedCustom("dispatchChannel",true);
				setNGValue('dispatchChannel','998');
				setLockedCustom("chequeNo",false);
				setLockedCustom("chequeDate",false);
				setLockedCustom("chequeStatus",false);
				setNGValue('tenor','');//pcasp-1377
				setLockedCustom("tenor",true);	
				setNGValueCustom('bankName','--Select--');
				setLockedCustom("bankName",true);
				setNGValueCustom('bankCode','');
				setNGValueCustom('CC_Loan_Bank_code','');							
				setNGValue('creditcardNo','');//pcasp-1377
				setLockedCustom("creditcardNo",true);
				setNGValue('iban','');
			}
			else if(getNGValue('transtype')=='CCC' && getNGValue('transferMode')=='A'){
				setNGValue('chequeDate','');//pcasp-1377
				setNGValue('chequeNo','');//pcasp-1377
				setNGValue('chequeStatus','');//pcasp-1377
				setNGValue('tenor','');//pcasp-1377	
				setNGValue('dispatchChannel','');
				setLockedCustom("dispatchChannel",true);
				setLockedCustom("chequeNo",true);
				setLockedCustom("chequeDate",true);
				setLockedCustom("chequeStatus",true);
				setLockedCustom("tenor",true);
				setNGValueCustom('bankName','--Select--');
				setLockedCustom("bankName",true);	
			}
		// ++ below code already present - 06-10-2017 transferMode	
	//	return true;
			
		}
		
			else if(pId=='transtype'){
				setNGValue('transferMode','');//PCASP-3202
					if(getNGValue('transtype')=='BT'){
							//code added by deepak on 071020170- to blank the fileds which are disabled on changing the transaction type. Ref07102017-trsn001
							//setNGValueCustom('tenor','');//Tarang to be removed on friday(1/19/2018)
							setNGValueCustom('tenure','');
							setNGValueCustom('creditcardNo','');
							setLockedCustom("creditcardNo",false);
							setLockedCustom("tenor",true);
							setNGValue("PaymentPurpose",'');//PCASP-3186
							setVisible("CC_Loan_Label25",false);
							setVisible("PaymentPurpose",false);
						}
				
					else if(getNGValue('transtype')=='CCC'){
						//code added by deepak on 071020170- to blank the fileds which are disabled on changing the transaction type. Ref07102017-trsn001
						//setNGValueCustom('tenor','');//Tarang to be removed on friday(1/19/2018)
						setNGValueCustom('tenure','');
						setNGValueCustom('creditcardNo','');
						setNGValueCustom('bankName','');
						setLockedCustom("tenor",true);
						setLockedCustom("creditcardNo",true);
						setLockedCustom("bankName",true);	
						setVisible("CC_Loan_Label25",true);
						setVisible("PaymentPurpose",true);
					}
					else if(getNGValue('transtype')=='LOC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM'){
					showAlert('transtype','Loan On card is only applicable for IM');
					setNGValueCustom('transtype','');
					setNGValue('creditcardNo','');
					setLockedCustom("creditcardNo",true);
					setNGValue("PaymentPurpose",'');//PCASP-3186
					setVisible("CC_Loan_Label25",false);
					setVisible("PaymentPurpose",false);
					return false;
					}
					else {
						setLockedCustom("tenor",false);
						setNGValue("PaymentPurpose",'');//PCASP-3186
						setVisible("CC_Loan_Label25",false);
						setVisible("PaymentPurpose",false);
						var rows = getLVWRowCount('cmplx_CC_Loan_cmplx_btc');
						var flag = true;
						for(var i=0;i<rows;i++){
							if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)==getNGValue('Account_Number')){
								flag = false;
								break;
							}
						}
						if(flag){
						//setNGValueCustom('creditcardNo',getNGValue('Account_Number'));
						setNGValueCustom('benificiaryName',getNGValue("cmplx_Customer_FirstNAme")+' '+getNGValue('cmplx_Customer_MiddleNAme')+' '+getNGValue("cmplx_Customer_LastNAme"));
						}
						setLockedCustom("creditcardNo",false);
						setLockedCustom("bankName",false);
						
					}
	// ++ Above code already present - 06-10-2017 transtype
			return true;
		}

		//Added by shivang for PCASP-1632,1633
		else if(pId=='amount'){
			if(isVisible('ELigibiltyAndProductInfo_Frame1')==false)
			{
			 showAlert('EligibilityAndProductInformation','Please Visit Eligibility section First.');
			 setNGValue('amount','');
			 return false;
			}
			if(parseFloat(getNGValue('amount'))<1000){
				showAlert('amount','Amount cannot be less than 1000');
				setNGValue('amount','');
			    return false;
			}
			/*PCASP-1464
			if(parseFloat(getNGValue('amount'))>parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit'))*0.95)
			{
				showAlert('amount',"Amount can't be more than 95% percent of final limit");
				setNGValue('amount','');
				return false;
			}*/
		}
		//++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
		//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
		else if(pId=='cmplx_CC_Loan_DDSMode')
		{
			var dds_mode=getNGValue('cmplx_CC_Loan_DDSMode');
			//below code also fix point "30-Service Details#Validations not as per FSD."
			if(dds_mode=='F')
			//above code also fix point "30-Service Details#Validations not as per FSD."
			
			{
				
				setLockedCustom("cmplx_CC_Loan_Percentage",true);//Added by shweta for pcasp-1504
				setLockedCustom("cmplx_CC_Loan_DDSAmount",false);//Added by shweta for pcasp-1504
				setNGValue("cmplx_CC_Loan_Percentage",'');//Added by shivang for pcasp-1504
			}
			//below code also fix point "30-Service Details#Validations not as per FSD."
			else if(dds_mode=='P')
			//above code also fix point "30-Service Details#Validations not as per FSD."
			
			{
				setLockedCustom("cmplx_CC_Loan_DDSAmount",true);//Added by shweta for pcasp-1504
				setLockedCustom("cmplx_CC_Loan_Percentage",false);//Added by shweta for pcasp-1504
				setNGValueCustom("cmplx_CC_Loan_Percentage",'100');//Added by shivang for pcasp-1504
				setNGValueCustom("cmplx_CC_Loan_DDSAmount",'');//Added by shivang for pcasp-1504
			}
			//PCASP-3177
			else if(dds_mode=='M')
			{
				setLockedCustom("cmplx_CC_Loan_DDSAmount",true);
				setLockedCustom("cmplx_CC_Loan_Percentage",true);
				setNGValueCustom("cmplx_CC_Loan_Percentage",'5');
				setNGValueCustom("cmplx_CC_Loan_DDSAmount",'');
			}
			else
			{
				setLockedCustom("cmplx_CC_Loan_DDSAmount",true);//Added by shweta for pcasp-1504
				setLockedCustom("cmplx_CC_Loan_Percentage",true);//Added by shweta for pcasp-1504
				setNGValueCustom("cmplx_CC_Loan_Percentage",'');//Added by shivang for pcasp-1504
				setNGValueCustom("cmplx_CC_Loan_DDSAmount",'');//Added by shivang for pcasp-1504
			}
		}
		//Below change done by shivang for PCASP-2142
		else if(pId=='cmplx_CC_Loan_DDSAmount'){
			if(isVisible('ELigibiltyAndProductInfo_Frame1')==false)
			{
			 showAlert('EligibilityAndProductInformation','Please Visit Eligibility section First.');
			 setNGValue('cmplx_CC_Loan_DDSAmount','');
			 return false;
			}
			else if(parseFloat(getNGValue('cmplx_CC_Loan_DDSAmount'))>parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')))
			{
				showAlert('cmplx_CC_Loan_DDSAmount',"Flat Amount can't be more than 100 percent of final limit");
				setNGValue('cmplx_CC_Loan_DDSAmount','');
				return false;
			}
		}
		else if(pId=='cmplx_CC_Loan_DDSBankAName'){
			return true;
		}
		  //-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

		
		// added by abhishek as per CC FSD
		else if(pId=='cmplx_CC_Loan_DDSExecDay'){
			var execDay = getNGValue('cmplx_CC_Loan_DDSExecDay');
			if(execDay <1 || execDay >28){
				showAlert('cmplx_CC_Loan_DDSExecDay',alerts_String_Map['CC157']);
				return false;
			}
			return true;
		}
			else if(pId=='cmplx_CC_Loan_AccNo'){
			var execDay = getNGValue('cmplx_CC_Loan_AccNo');
			if(execDay.length!=13){
				showAlert('cmplx_CC_Loan_AccNo','Length of Acccount Number should be 13');
				return false;
			}
			return true;
		}
		
		else if(pId=='cmplx_CC_Loan_SI_day'){
			var execDay = getNGValue('cmplx_CC_Loan_SI_day');
			if(execDay <1 || execDay >31){
				showAlert('cmplx_CC_Loan_SI_day',alerts_String_Map['CC158']);
				setNGValueCustom('cmplx_CC_Loan_SI_day','');
				return false;
			}
			return true;
		}
		
		
		else if(pId=='cmplx_CC_Loan_HoldType'){
			if(getNGValue('cmplx_CC_Loan_HoldType')=='T'){
				setLockedCustom('cmplx_CC_Loan_HoldFrom_Date',false);
				setLockedCustom('cmplx_CC_Loan_HOldTo_Date',false);
			}
			else{
				setLockedCustom('cmplx_CC_Loan_HoldFrom_Date',true);
				setLockedCustom('cmplx_CC_Loan_HOldTo_Date',true);
			}
		}	
		//Below change done by shivang for PCASP-2142
		else if(pId=='cmplx_CC_Loan_FlatAMount'){
			if(isVisible('ELigibiltyAndProductInfo_Frame1')==false)
			{
			 showAlert('EligibilityAndProductInformation','Please Visit Eligibility section First.');
			 setNGValue('cmplx_CC_Loan_FlatAMount','');
			 return false;
			}
			else if(parseFloat(getNGValue('cmplx_CC_Loan_FlatAMount'))>parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')))
			{
				showAlert('cmplx_CC_Loan_FlatAMount',"Flat Amount can't be more than 100 percent of final limit");
				setNGValue('cmplx_CC_Loan_FlatAMount','');
				return false;
			}
		}
		
		// added by abhishek as per CC FSD
		else if(pId=='CompanyDetails_DatePicker1'){
			//validateFutureDate(pId));
			getLOB(getNGValue('CompanyDetails_DatePicker1'));
			return true;
		}
		//added by nikhil for PcSP-676
		else if(pId=='cmplx_OffVerification_fxdsal_upd'){
		setNGValueCustom(pId,setDecimalto2digits(pId));
		putComma(pId);	
		}
		// Added by yash for notecode auto populate on 24sept
		else if(pId=='NotepadDetails_notedesc'){
			
			return true;
		}
		else if(pId=='cmplx_FinacleCore_total_avg_last13'){
			setNGValueCustom('FinacleCoreSave','N');
		}
		else if(pId=='cmplx_FinacleCore_total_avg_last_16'){
			setNGValueCustom('FinacleCoreSave','N');
		}
		else if(pId=='cmplx_FinacleCore_total_deduction_3month'){
			setNGValueCustom('FinacleCoreSave','N');
			return true;
		}
		else if(pId=='cmplx_FinacleCore_total_deduction_6month'){
			setNGValueCustom('FinacleCoreSave','N');
			return true;
		}
		//below code added by nikhil
		else if(pId=='cmplx_CustDetailVerification_mobno1_upd' || pId=='cmplx_CustDetailVerification_mobno2_upd'  )
		{
		var Mobile_no=getNGValue(pId);
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValueCustom(pId,mob_No);
		}	
		if(mob_No.length!=14){
			showAlert(pId,alerts_String_Map['VAL087']);
			setNGValueCustom(pId,'');
			return false;
		}
		if (mob_No.indexOf('00971')>0  && (mob_No.length==14)){
			showAlert(pId,"Please enter mobile no starting with 00971");
			setNGValueCustom(pId,'');
			return false;
		}

		}
	//added by akshay on 28/12/17	
	/*else if(pId=='ExtLiability_Limit')
	{
		var limit=parseFloat(getNGValue(pId));
		var emi=(2/100*limit).toFixed(2);
		setNGValueCustom('Liability_New_EMI',emi);
	}*/
	else if(pId=='cmplx_DEC_Area'){
	return true;
	}
	
	if(pId=='SupplementCardDetails_FirstName' || pId=='SupplementCardDetails_lastname' || pId=='SupplementCardDetails_passportNo' || pId=='SupplementCardDetails_DOB' || pId=='SupplementCardDetails_Nationality' || pId=='SupplementCardDetails_MobNo' || pId=='SupplementCardDetails_Text6'){
		setLockedCustom('SupplementCardDetails_FetchDetails',false);
	}
	return false;
}


function blur_CC(pId){
//below code changes by nikhil for wrong alert on change 6/12 // changes done by prabhakar jira number 2539
	if(pId=='cmplx_Customer_MobileNo' || pId=='PartMatch_mno1' || pId=='PartMatch_mno2' )
	{if(getNGValue(pId)!="")
		{
		var Mobile_no=getNGValue(pId);
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValueCustom(pId,mob_No);
		}
		if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
			showAlert(pId,'Incorrect mobile number format');
			setNGValueCustom(pId,'');
		}		
		if(mob_No.length!=14){
			showAlert(pId,alerts_String_Map['VAL087']);
			return false;
		}	
		if(pId=='cmplx_Customer_MobileNo'){
		setNGValueCustom('OTP_Mobile_NO',mob_No);
		}
		}
		//setLocked('Customer_FetchDetails',false);
	}
	if(pId=='cmplx_emp_ver_sp2_landline')
	{
		if(getNGValue(pId)!="")
		{
		
	var Mobile_no=getNGValue('cmplx_emp_ver_sp2_landline');
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValue(pId,mob_No);
		}
		if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
			showAlert(pId,'Incorrect Landline number format');
			setNGValue(pId,'');
		}		
		if(mob_No.length<13 || mob_No.length>15){
			showAlert(pId,'Landline number should be between 13-15 digits');
			return false;
		}	
	}}
		if(pId=='cmplx_cust_ver_sp2_Mobile')
	{
		if(getNGValue(pId)!="")
		{
	var Mobile_no=getNGValue('cmplx_cust_ver_sp2_Mobile');
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValue(pId,mob_No);
		}
		if(mob_No.substring(0,5)!='00971' && mob_No.length>10){
			showAlert(pId,'Incorrect mobile number format');
			setNGValue(pId,'');
		}		
		if(mob_No.length<13 || mob_No.length>15){
			showAlert(pId,alerts_String_Map['CC277']);
			return false;
		}	
	}}
	// Changes done by aman 1012 for CR
	if(pId=='cmplx_OffVerification_hrdcntctno')
	{
		if(getNGValue(pId)!="" && getNGValue("cmplx_OffVerification_hrdnocntctd")=="Yes")//PCAS-2232 by sagarika
		{
		var Mobile_no=getNGValue(pId);
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<11){
			mob_No="00971"+mob_No;
			setNGValueCustom(pId,mob_No);
		}
		if(mob_No.substring(0,5)!='00971' && mob_No.length>9){
			showAlert(pId,'Incorrect office number format');
			setNGValueCustom(pId,'');
		}		
		if(mob_No.length<13 || mob_No.length>15){
			showAlert(pId,alerts_String_Map['VAL393']);
			return false;
		}	
		if(pId=='cmplx_Customer_MobileNo'){
		setNGValueCustom('OTP_Mobile_NO',mob_No);
		}
		//setLockedCustom('Customer_FetchDetails',false);
	}
	}
	// Changes done by aman 1012 for CR
	else if(pId=='cmplx_Customer_CIFNo'){
		var cif=getNGValue('cmplx_Customer_CIFNo');
		if(cif.length!=7 && cif.length!=0){
			showAlert('cmplx_Customer_CIFNo',alerts_String_Map['VAL034']);
			return false;
		}
		//belowcode added by nikhil as per ddvt issues
		else if(checkforfinaclecustinfo())
		{
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL383']);
		return false;
		}
		else{	
			//hash='###'+cif.substring(3,7)+'###';
			//setNGValueCustom('cmplx_Customer_CIFNo',hash);
			return true;
		}
	}

	// added by abhishek as per CC FSD
	/*else if(pId=='Reference_Details_ReferencePhone')
	{
	    var Mobile_no=getNGValue('Reference_Details_ReferencePhone');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10){
			Mobile_no="00971"+Mobile_no;
			setNGValueCustom('Reference_Details_ReferencePhone',Mobile_no);
		}	
		if(Mobile_no.length!=14){
			showAlert('Reference_Details_ReferencePhone',alerts_String_Map['VAL087']);
			setNGValueCustom('Reference_Details_ReferencePhone','');
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971'){
			showAlert('Reference_Details_ReferencePhone',alerts_String_Map['VAL088']);
			setNGValueCustom('Reference_Details_ReferencePhone','');
			return false;
		}
	}*/
	//below code added by nikhil for incoming DOC
	/*else if(pId=='IncomingDocNew_DocName')
	{
		return true;
	}*/
	else if(pId=='AlternateContactDetails_MobileNo1')
	{
	    var Mobile_no=getNGValue('AlternateContactDetails_MobileNo1');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=''){
			Mobile_no="00971"+Mobile_no;
			setNGValueCustom('AlternateContactDetails_MobileNo1',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL087']);
			setNGValueCustom('AlternateContactDetails_MobileNo1','');
			setNGValueCustom('AlternateContactDetails_MobNo2',''); //added By Tarang started on 13/02/2018 as per drop 4 point 16
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL088']);
			setNGValueCustom('AlternateContactDetails_MobileNo1','');
			setNGValueCustom('AlternateContactDetails_MobNo2',''); //added By Tarang started on 13/02/2018 as per drop 4 point 16
			return false;
		}
	}
	
	else if(pId=='AlternateContactDetails_MobNo2')
	{
	    var Mobile_no=getNGValue('AlternateContactDetails_MobNo2');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=''){
			Mobile_no="00971"+Mobile_no;
			setNGValueCustom('AlternateContactDetails_MobNo2',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobNo2',alerts_String_Map['VAL087']);
			setNGValueCustom('AlternateContactDetails_MobNo2','');
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobNo2',alerts_String_Map['VAL088']);
			setNGValueCustom('AlternateContactDetails_MobNo2','');
			return false;
		}
	}
   return false;
   //ended by yash for CC FSD
}


function changeCase(controlName){
	switch(controlName)
	{	
		case 'cmplx_Decision1_Decision':
		
		//var dec=com.newgen.omniforms.formviewer.getNGValue(controlName);
		
		//com.newgen.omniforms.formviewer.setNGValueCustom('Decision',dec);
			return true;
			//commented by nikhil for Wrong code
			//done by sagarika for 416
			//case 'NotepadDetails_notedesc':
			//return true;
		
	}
	//alert("dec : "+dec);
	return true;
}


	
function setData(textarea)
{
	setNGValueCustom('Text12',textarea);
	//alert(textarea);
	return true;
}
function clickCase(controlName){
	return true;

}	
function GetErroDesc(session_id){

	var xhr;
	var ajaxResult;
	ajaxResult="";
	
	var url ='/formviewer/resources/scripts/CC/akshay.jsp?sessionId='+session_id+"";
	window.open_(url,"_blank","width=400,height=300");
							
}

function CC_FORM_POPULATED(activityName){
	//alert('inside CC_FORM_POPULATED !!!!!' + activityName);
	
	//if(activityName=='SalesCoordinatorCSM')
	//{
			SetDisableCustomer();
	//}
}

function SetDisableCustomer()
	{
		//alert('inside SetDisableCustomer' );
		var fields="Customer_Frame1,Product_Frame1,IncDetails_Frame2,IncDetails_Frame3,ExternalLiabilities_CheckBox1,ExternalLiabilities_CheckBox5,ExternalLiabilities_CheckBox6,ExternalLiabilities_Button2,ExternalLiabilities_Button1,ExternalLiabilities_Text2,ExternalLiabilities_Text3,ExternalLiabilities_DatePicker1,ExternalLiabilities_DatePicker2,ExternalLiabilities_cbr,ExternalLiabilities_tai,ExternalLiabilities_dbrnetSal,ExternalLiabilities_aggExposure,ExternalLiabilities_Save"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
		{
			//alert('field_array[i]:'+field_array[i]);
			setLockedCustom(field_array[i], false);
		}
	}
	
	function SetDisableCustomer1()
	{
		var fields="cmplx_Customer_Title,cmplx_Customer_RESIDENTNONRESIDENT,cmplx_Customer_gender,cmplx_Customer_age,cmplx_Customer_MotherName,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_VisaNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_MAritalStatus,cmplx_Customer_COUNTRYOFRESIDENCE,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIPFlag,cmplx_Customer_PassportIssueDate"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setLockedCustom(field_array[i], true);
	}
	
	

	
	function setCCFieldsVisible(ReqProd){
	if(ReqProd=='Credit Card'){
				setVisible("CardProd", true);
				setVisible("Product_Label6",true);
				setVisible("Product_Label5",true);
				setVisible("ReqTenor",true);
				setVisible("Product_Label8",false); 
				//setVisible("Product_Label12",false); 
				//setVisible("Product_Label21",false);
				setVisible("Schem",false); 
				setVisible("Product_Label5",false);
				//setVisible("Product_Label10",true);
				setVisible("reqTenor",false);
				setVisible("MiscFields",false);
				setVisible("Supplementary_Container",true);
				setNGValueCustom("CardProd","--Select--");
			}
			
	else if(ReqProd=='Personal Loan'){
				setVisible("Product_Label8",true); 
				//setVisible("Product_Label12",true); 
				//setVisible("Product_Label21",true); 
				setVisible("Schem",true); 
				setVisible("Product_Label5",true);
				setVisible("reqTenor",true);
				com.newgen.omniforms.formviewer.setLeft("Product_Label8", "555px");
				com.newgen.omniforms.formviewer.setLeft("Schem", "555px");
				com.newgen.omniforms.formviewer.setLeft("Product_Label5", "822px");
				com.newgen.omniforms.formviewer.setLeft("reqTenor", "822px");
				setVisible("CardProd",false); 
				
				setVisible("Product_Label6",false); 
				
				setVisible("requestType",false);
				
				setVisible("limitValue",false); 
				setVisible("LimitExpiry",false);
				setVisible("Supplementary_Container",false);
				
				setVisible("Product_Label3", false);
				
				setVisible("Product_Label9", false);
				setVisible("Product_Label7", false);
				
				setNGValueCustom("empType","Salaried");
			}
			
	else{
				setVisible("CardProd",false); 
				setVisible("Product_Label6",false);
				setVisible("Product_Label8",false); 
				
				setVisible("Schem",false); 
				setVisible("Product_Label5",false);
				
				setVisible("reqTenor",false);
				setVisible("Product_Label5",false); 
				
				setVisible("Product_Label6",false); 
				
				setVisible("requestType",false);
				
				setVisible("limitValue",false); 
				setVisible("LimitExpiry",false);
				
				
				setVisible("Product_Label15", false);
				setVisible("Product_Label16", false);
				
				
				setNGValueCustom("empType","--Select--");
		}
	}
	/*
	function Employment_Save_Check()
	{
		alert("I am an alert box!");
     if(getNGValue('cmplx_EmploymentDetails_channelcode')=='--Select--')
	 {   
         alert("I am !");
		 showAlert('cmplx_EmploymentDetails_channelcode',alerts_String_Map['CC034']);
	 }
	 
		
	return true;	
	}*/
	
	function Partner_Save_Check()
	{
		//alert("I am an alert box!");
		//if(checkMandatory(CC_PartnerDetails))
				return true;
		
	}
	
	function SetEnableCustomer()
	{
		var fields="cmplx_Customer_Title,cmplx_Customer_RESIDENTNONRESIDENT,cmplx_Customer_gender,cmplx_Customer_MotherName,cmplx_Customer_VisaNo,cmplx_Customer_MAritalStatus,cmplx_Customer_COUNTRYOFRESIDENCE,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_PassPortExpiry,cmplx_Customer_VisaExpiry,cmplx_Customer_PassportIssueDate"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setLockedCustom(field_array[i], false);
		setEnabledCustom('cmplx_Customer_VIPFlag',true);
	}
	
	
	
	function setBlank_Customer()
	{
		var fields="cmplx_Customer_EmiratesID,cmplx_Customer_LastNAme,cmplx_Customer_DOb,cmplx_Customer_MobileNo,cmplx_Customer_PAssportNo,cmplx_Customer_Nationality,cmplx_Customer_RESIDENTNONRESIDENT,cmplx_Customer_EIDARegNo,cmplx_Customer_CIFNo,cmplx_Customer_masroomID,cmplx_Customer_Title,cmplx_Customer_FirstNAme,cmplx_Customer_MiddleNAme,cmplx_Customer_gender,cmplx_Customer_MotherName,cmplx_Customer_VisaNo,cmplx_Customer_MAritalStatus,cmplx_Customer_COUNTRYOFRESIDENCE,cmplx_Customer_Age,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_PassportIssueDate,cmplx_Customer_VisaIssuedate"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			if(getNGValue(field_array[i])!='--Select--')
				setNGValueCustom(field_array[i],"");	
	}
	
	
function doNotLoadFragmentSecTime(pId){
	CCFRAGMENTLOADOPT[pId]='N';
	}
	
	
		function LimitIncreaseFields_Income1()
	{
		var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(var i=0;i<n;i++){ 
				if(getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)=="Limit Increase"){
					setEnabledCustom("cmplx_IncomeDetails_MonthlyRent",false); 
					setEnabledCustom("cmplx_IncomeDetails_NoOfBankStat",false); 
					break;
				}
			}
		}
	}


	function ProductUpgrade_Income1()
	{
		var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(var i=0;i<n;i++){ 
				if(getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)== "Product Upgrade"){
					setEnabledCustom("cmplx_IncomeDetails_MonthlyRent",false); 
					setEnabledCustom("cmplx_IncomeDetails_NoOfBankStat",false); 		
				}
			}
		}
	}
	
function CC_Fields_Liability()
	{
		var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(var i=0;i<n;i++){
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="BTC" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
					
					setVisible("ExtLiability_Label9",false);
					setVisible("ExtLiability_Label25",false); 
					setVisible("ExtLiability_Label14",false);
					setVisible("cmplx_Liability_New_DBR",false);
					setVisible("cmplx_Liability_New_TAI",false); 
					setVisible("cmplx_Liability_New_DBRNet",false);
					
					
					break;
				}
				else{
					
					setVisible("ExtLiability_Label9",true);
					setVisible("ExtLiability_Label25",true); 
					setVisible("ExtLiability_Label14",true);
					setVisible("cmplx_Liability_New_DBR",true);
					setVisible("cmplx_Liability_New_TAI",true); 
					setVisible("cmplx_Liability_New_DBRNet",true);
					
				}	
			}
		}
		else{
					setVisible("ExtLiability_Label9",true);
					setVisible("ExtLiability_Label25",true); 
					setVisible("ExtLiability_Label14",true);
					setVisible("cmplx_Liability_New_DBR",true);
					setVisible("cmplx_Liability_New_TAI",true); 
					setVisible("cmplx_Liability_New_DBRNet",true);
					setVisible("ExtLiability_Label15",true); 
					setVisible("cmplx_Liability_New_AggrExposure",true);
			
		}	
		
	}	
	
	function setFieldsVisible1(ReqProd){
	if(ReqProd=='Credit Card'){
				setVisible("CardProd", true);
				setVisible("Product_Label6",true);// card prod
				setVisible("Product_Label3",false); // Scheme
				setVisible("Scheme",false); 
				setVisible("Product_Label5",false); // req tenor
				setVisible("ReqTenor",false);
				//setVisible("MiscFields",false);
				setVisible("Supplementary_Container",true);
				setNGValueCustom("CardProd","--Select--");
			}
			
			
	else{
				setVisible("CardProd",false); 
				setVisible("Product_Label6",false);
				setVisible("Product_Label3",false); 
				setVisible("Scheme",false); 
				setVisible("Product_Label5",false);
				setVisible("ReqTenor",false);
				setVisible("Product_Label6",false); 
				//setVisible("typeReq",false);
				

				setVisible("LimitExpiryDate",false);
				setVisible("LimitAcc", false);
				
				//setVisible("typeReq", false);
				setVisible("Product_Label15", false);
				setVisible("Product_Label16", false);
				setVisible("Product_Label18", false);
				setNGValueCustom("EmpType","--Select--");
		}
	}
	
	function tabSheetHandling_CC(activityName)
	{
		//alert(activityName);
		if (activityName=='SalesCoordinatorCSM' && ("W"==window.parent.parent.wiViewMode) )   
		{			
			setSheetVisible("Tab1",6, false);	//System Checks
			setSheetVisible("Tab1",7, false);
			// Contact Point Verification
			setSheetVisible("Tab1",8, false);	// FCU
			setSheetVisible("Tab1",9, false);	// Document
			setSheetVisible("Tab1",11, false);	// OV
			setSheetVisible("Tab1",12, false);  // Compliance
			setSheetVisible("Tab1",13, false); //FCU Decision
			setSheetVisible("Tab1",15, false); //FCU Decision
			setSheetVisible("Tab1",16, false); //FCU Decision
			setSheetVisible("Tab1",17, false); //FCU Decision
			setSheetVisible("Tab1",18, false); // Card Collection
			
		}
		else if (activityName=='DDVT_Maker' && ("W"==window.parent.parent.wiViewMode))
		{
			setSheetVisible("Tab1",8, false);	// Contact Point Verification
			setSheetVisible("Tab1",9, false);	// FCU
			//alert(activityName);
			setSheetVisible("Tab1",11, false);	// OV
			setSheetVisible("Tab1",12, false);	// Compliance
		}
		else if (activityName=='FCU'||activityName=='FPU')
		{
			setSheetVisible("Tab1",8, false);	// Contact Point Verification
			setSheetVisible("Tab1",11, false);	// OV
			setSheetVisible("Tab1",12, false);	// DISBURSAL
			setSheetVisible("Tab1",13, false);	// Compliance
			setSheetVisible("Tab1",15, false);	// CAD_Decision
			setSheetVisible("Tab1",16, false);	// FcuDecision
		    setSheetVisible("Tab1",17, false);	// CARD Collection Decision
			setSheetVisible("Tab1",18, false);	// DISPATCH
		}
		
		else if(activityName=='Fulfillment_RM' && ("W"==window.parent.parent.wiViewMode))
		{
			setSheetVisible("Tab1",6, true);    //Service Request
			setSheetVisible("Tab1",7, false);	//System Checks
			setSheetVisible("Tab1",8, false);	// Contact Point Verification
			setSheetVisible("Tab1",9, false);	// FCU
			setSheetVisible("Tab1",11, false);	// OV
			setSheetVisible("Tab1",13, false);  // Compliance
			setSheetVisible("Tab1",12, false);	// DISBURSAL
			setSheetVisible("Tab1",15, false);	// CAD_Decision
			setSheetVisible("Tab1",16, false);	// FcuDecision
		    setSheetVisible("Tab1",17, false);	// CARD Collection Decision
			setSheetVisible("Tab1",18, false);	// DISPATCH
		}
		else if(activityName=='Cad_Analyst1' || activityName=='Cad_Analyst2')
		{
			setSheetVisible("Tab1",13, false);  // Compliance
		}
	}
	
	
	function uploadDocument(){
				var result = null;
				var finalxmlResponse = null;
				var activityName = window.parent.stractivityName;
				var wi_name = window.parent.pid;
				var firstName = getNGValue('cmplx_Customer_FirstNAme');
				var lastName = getNGValue('cmplx_Customer_LastNAme');
				var fullName = firstName+" "+lastName;
				var strGenralData = window.parent.strGeneralData;
				var FolderId = strGenralData.substring( strGenralData.indexOf("<FolderId>") + 10 , strGenralData.indexOf("</FolderId>") );
				 if (getNGValue('sig_docindex')==''){
					showAlert('IncomingDoc_UploadSig',alerts_String_Map['PL406']);
					return false;
				 }
			//	alert("fullName uploadDocument"+ fullName);
				$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/VerifySignature.jsp",
					data: { CifId:getNGValue('cmplx_Customer_CIFNo'),AccountNo:'',CustSeqNo:'001',ItemIndex:FolderId,CustomerName:fullName,docIndex:getNGValue('sig_docindex'),WIName:wi_name ,workstepName:activityName} ,
					dataType: "text",
					async: false,
					success: function (response) {
					//	alert('result in integration_all: '+ response);	
							//cifIntegration(response);
							alert('Signature uploaded Successfully !');
						console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});     			
			return result;
           }
		/*   //added by yash for CC FSD
		   function calcAverage_Overtime()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month1")!='')
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_Overtime_Month1").replace(",", ""));
		
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month2")!='')
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_Overtime_Month2").replace(",", ""));
		
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month3")!='')	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_Overtime_Month3").replace(",", ""));
	
		
	avg =(month1 + month2 +month3)/3;
	setNGValueCustom("cmplx_IncomeDetails_Overtime_Avg",avg.toFixed(2));
}
*/
     // function changed by abhishek as per CC FSD

	function Fatca_disableCondition(){
			setLockedCustom("FATCA_iddoc", true);
			setNGValueCustom("FATCA_iddoc", false);
			setLockedCustom("FATCA_decforIndv", true);
			setNGValueCustom("FATCA_decforIndv", false);
			setLockedCustom("FATCA_w8form",true);
			setNGValueCustom("FATCA_w8form", false);
			setLockedCustom("FATCA_w9form",true);
			setNGValueCustom("FATCA_w9form", false);
			setLockedCustom("FATCA_lossofnationality",true);
			setNGValueCustom("FATCA_lossofnationality", false);
			setLockedCustom("FATCA_TINNo",true);
			setNGValueCustom("FATCA_TINNo","");
			setLockedCustom("FATCA_SignedDate",true);
			setNGValueCustom("FATCA_SignedDate","");
			setLockedCustom("FATCA_ExpiryDate",true);
			setNGValueCustom("FATCA_ExpiryDate","");
			setLockedCustom("FATCA_Category",true);
			setNGValueCustom("FATCA_Category","");
			setLockedCustom("FATCA_ControllingPersonUSRel",true);
			setNGValueCustom("FATCA_ControllingPersonUSRel","");
			setLockedCustom("FATCA_listedreason",true);
			setNGValueCustom("FATCA_listedreason","");
			setLockedCustom("FATCA_selectedreason",true);
			setNGValueCustom("FATCA_selectedreason","");			
			setLockedCustom("FATCA_Button1",true);
			setLockedCustom("FATCA_Button2",true);

	}
	
	function Fatca_Enable(){
			setLockedCustom("FATCA_iddoc", false);
			setNGValueCustom("FATCA_iddoc", false);
			setLockedCustom("FATCA_decforIndv", false);
			setNGValueCustom("FATCA_decforIndv", false);
			setLockedCustom("FATCA_w8form",false);
			setNGValueCustom("FATCA_w8form", false);
			setLockedCustom("FATCA_w9form",false);
			setNGValueCustom("FATCA_w9form", false);
			setLockedCustom("FATCA_lossofnationality",false);
			setNGValueCustom("FATCA_lossofnationality", false);
			setLockedCustom("FATCA_TINNo",false);
			setNGValueCustom("FATCA_TINNo","");
			setLockedCustom("FATCA_SignedDate",false);
			setNGValueCustom("FATCA_SignedDate","");
			setLockedCustom("FATCA_ExpiryDate",false);
			setNGValueCustom("FATCA_ExpiryDate","");
			setLockedCustom("FATCA_Category",false);
			setNGValueCustom("FATCA_Category","");
			setLockedCustom("FATCA_ControllingPersonUSRel",false);
			setNGValueCustom("FATCA_ControllingPersonUSRel","");
			setLockedCustom("FATCA_listedreason",false);
			setNGValueCustom("FATCA_listedreason","");
			setLockedCustom("FATCA_selectedreason",false);
			setNGValueCustom("FATCA_selectedreason","");
			setLockedCustom("FATCA_Button1",false);
			setLockedCustom("FATCA_Button2",false);
	}
	// added by abhishek as per CC FSD
	
	
	function getLOB(d1){
		//var d1 = "16/10/2012";
	var d2 =  new Date();
	var parts = d1.split('/');
	
	var years = d2.getFullYear() - parts[2];
	
	var month = (d2.getMonth()+1) - parts[1];
	if(month.toString().length == 1){
		month = '0'+month;
	}
	if(years.toString().length == 1){
		years = '0'+years;
	}
	var lobvalue = years+'.'+month;
		
	/* 
	var DOB=new Date(parts[2],parts[1]-1,parts[0]);
	var date1 = new Date(DOB);
    var diff = d2.getTime() - date1.getTime();
	var lob  = 	Math.floor(diff / (1000 * 60 * 60 * 24 * 365.25)); */
	setNGValueCustom("lob", lobvalue);
}
function CC_postHookDRefresh(controlName){
	if(controlName=='ELigibiltyAndProductInfo_Button1' || controlName=='DecisionHistory_calReElig' ){//PCAS -2812
	document.getElementById('ELigibiltyAndProductInfo_IFrame1').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Product.jsp';
	document.getElementById('ELigibiltyAndProductInfo_IFrame2').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp';
	//change done by shivang  for hiding Filed visit done
	if(activityName=='CAD_Analyst1' && visitSystemChecks_Flag==true){
	if(getNGValue("Sub_Product")!='IM')
	{
		setVisible('cmplx_EmploymentDetails_FieldVisitDone',false);
		setVisible('EMploymentDetails_Label32',false);
	}
	}
	}
	if(getNGValue("Sub_Product")=='IM'){
		setVisible('cmplx_Liability_New_AECBCompanyconsentAvail_cont',false);
		setVisible("Liability_New_Label11",false);
		if(controlName=='EligibilityAndProductInformation' && activityName=='CSM'){
			calAgeAtMaturity();
		}
	}
	else if(controlName=='ExtLiability_Button1'){
		document.getElementById('Liability_New_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';
		document.getElementById('ExtLiability_IFrame_external').src='/webdesktop/custom/CustomJSP/External_Liability.jsp';
		document.getElementById('ExtLiability_IFrame_pipeline').src='/webdesktop/custom/CustomJSP/Pipeline.jsp';
	}
	else if(controlName=='cmplx_EmploymentDetails_targetSegCode'){
		//document.getElementById('Liability_New_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';
		document.getElementById('ExtLiability_IFrame_external').src='/webdesktop/custom/CustomJSP/External_Liability.jsp';
		//document.getElementById('ExtLiability_IFrame_pipeline').src='/webdesktop/custom/CustomJSP/Pipeline.jsp';
	}
	//added by nikhil for scroll bar issue
	else if(controlName =='Part_Match')
	{
		adjustScroll('cmplx_PartMatch_Blacklist_Grid');
		adjustScroll('cmplx_PartMatch_cmplx_Partmatch_grid');
		
	}
	
	else if(controlName =='Finacle_CRM_CustomerInformation')
	{
		adjustScroll('cmplx_FinacleCRMCustInfo_FincustGrid');
				
	}
	//Added By shivang for PCASP-1700
	else if(controlName=='cmplx_CompanyDetails_cmplx_CompanyGrid' && (activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2' || activityName=='CPV' || activityName=='CPV_Analyst'
			|| activityName=='Hold_CPV'))
	{
		setLocked('CompanyDetails_Button3',true);
		setLocked('CompanyDetails_delete',true);
	}
	//PCASP-2971
	else if(controlName=='CompanyDetails_Button3'){
		if(validateFutureDate('estbDate'))
			{			
				YYMM('','lob',calcAge(getNGValue('estbDate'),''));

			}
	}
	else if(controlName=='SI_Delete'||controlName=='SI_Modify'||controlName=='SI_Add'){
		setNGValue("cmplx_CC_Loan_StartMonth", "1"); //PCASP-3186
	}
	//Code changes by shivang for hiding Aggregate Exposure Field starts
	else if (controlName=='Internal_External_Liability')
	{
		//setVisible("ExtLiability_Label15",false);
		//setVisible("cmplx_Liability_New_AggrExposure",false);
	}
		
	else if(controlName=='EligibilityAndProductInformation')
	{
		setLockedCustom('cmplx_EligibilityAndProductInfo_FinalDBR',true);
		setLockedCustom('cmplx_EligibilityAndProductInfo_FinalTai',true);
		//Added by Shivang for PCASP-1431
		if(activityName=='CAD_Analyst1'){
		setLocked("cmplx_EligibilityAndProductInfo_FirstRepayDate_Cont", true);
		setLocked("cmplx_EligibilityAndProductInfo_FirstRepayDate", true);
		}		
	}
	else if(controlName=='Office_Mob_Verification' && activityName=='CAD_Analyst1')
	{
		setLocked("cmplx_OffVerification_fxdsal_ver",true);//for PCASP-20
		setLocked("cmplx_OffVerification_accpvded_ver",true);//for PCASP-20
		setLocked("cmplx_OffVerification_desig_ver",true);//for PCASP-20
		setLocked("cmplx_OffVerification_doj_ver",true);//for PCASP-20
	}
	else if(controlName=='OfficeandMobileVerification_Button1')
	{
		if(getNGValue('cmplx_OffVerification_colleaguenoverified')=='Yes')
		{
			setNGValue('cmplx_OffVerification_fxdsal_ver',' ');
			setLocked('cmplx_OffVerification_fxdsal_ver',true);
			setEnabled('cmplx_OffVerification_fxdsal_ver',false);
			setLocked('cmplx_OffVerification_accpvded_ver',true);
			setEnabled('cmplx_OffVerification_accpvded_ver',false);
			setLocked('cmplx_OffVerification_desig_ver',true);
			setEnabled('cmplx_OffVerification_desig_ver',false);
			setLocked('cmplx_OffVerification_doj_ver',true);
			setEnabled('cmplx_OffVerification_doj_ver',false);
			
			
		}
		if(getNGValue('cmplx_OffVerification_offtelnocntctd')=='Yes' && (activityName=='CPV_Analyst'|| activityName=='CPV') && getNGValue("Sub_Product")=='SE')// Added By rajan
			{
				if(getNGValue('cmplx_OffVerification_colleaguename') == '')
				{
				showAlert('cmplx_OffVerification_colleaguename',"Colleague name can't be blank");
				return false;
				}
				
				if(getNGValue('cmplx_OffVerification_colleaguedesig') == '')
				{
				showAlert('cmplx_OffVerification_colleaguedesig',"Colleague Designation can't be blank");
				return false;
				}
			}
	}
	else if(controlName=='IncomingDocNew_Addbtn' || controlName=='IncomingDocNew_Modifybtn')
	{
		setNGValue('IncomingDocNew_Username',window.parent.userName);
		setNGValue('IncomingDocNew_workstep',window.parent.stractivityName);
	}
	else if(controlName=='cmplx_IncomingDocNew_IncomingDocGrid' )
	{
		if(getNGValue('IncomingDocNew_Username')=='')
		{
		setNGValue('IncomingDocNew_Username',window.parent.userName);
		setNGValue('IncomingDocNew_workstep',window.parent.stractivityName);
		}
	}
	else if (controlName=='SupplementCardDetails_Add' || controlName=='SupplementCardDetails_Modify')
	{
		setNGValue("SupplementCardDetails_ResidentCountry","AE");
	}
	else if(controlName=='Customer_FetchDetails' && getNGValue('CifLabel')=='' && getNGValue('cmplx_Customer_NTB')==true)
	{
	setNGValue('cmplx_Customer_CIFNo','');
	}
	//Code changes by shivang for hiding Aggregate Exposure Field ends
	//Deepak commented on 17/07/2019 for removing security point.
	//onLoadValues = getFetchedFragmentFields();
	}
	//smartchck function by saurabh on 3rd oct for smartcheck mandatory fields.
	function checkMandatory_Smartcheck(){
	var activityName=window.parent.stractivityName;
		if(!getNGValue('SmartCheck_CR_Remarks') && activityName.indexOf('CAD')>-1){
			showAlert('SmartCheck_CR_Remarks',alerts_String_Map['CC054']);
				return false;
		}
		else if(!getNGValue('SmartCheck_CPVRemarks') && (activityName=='CPV' || activityName=='Smart_CPV')){
			showAlert('SmartCheck_CPVRemarks',alerts_String_Map['CC053']);
				return false;
		}
		return true;
	}


	
	function getLOB(d1){
		//var d1 = "16/10/2012";
	var d2 =  new Date();
	var parts = d1.split('/');
	
	var years = d2.getFullYear() - parts[2];
	
	var month = (d2.getMonth()+1) - parts[1];
	if(month.toString().length == 1){
		month = '0'+month;
	}
	if(years.toString().length == 1){
		years = '0'+years;
	}
	var lobvalue = years+'.'+month;
		
	/* 
	var DOB=new Date(parts[2],parts[1]-1,parts[0]);
	var date1 = new Date(DOB);
    var diff = d2.getTime() - date1.getTime();
	var lob  = 	Math.floor(diff / (1000 * 60 * 60 * 24 * 365.25)); */
	setNGValueCustom("lob", lobvalue);
}

//New method added to handle keypress event Deepak 18 Dec 2017
 function keydown_CC(pId,keyCode_str)
 {	//added by nikhil for PCAS-1249
 //chnaged by nikhil for PcSP-676
	//pID name need to added which are mentioned as 17,2 in field list document.
	if(pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other' || pId=='cmplx_IncomeDetails_Commission_Month1' || pId=='cmplx_IncomeDetails_Commission_Month2' || pId=='cmplx_IncomeDetails_Commission_Month3' || pId=='cmplx_IncomeDetails_Flying_Month1' || pId=='cmplx_IncomeDetails_Flying_Month2' || pId=='cmplx_IncomeDetails_Flying_Month3' || pId=='cmplx_IncomeDetails_Bonus_Month1' || pId=='cmplx_IncomeDetails_Bonus_Month2' || pId=='cmplx_IncomeDetails_Bonus_Month3' || pId=='cmplx_IncomeDetails_Overtime_Month1' || pId=='cmplx_IncomeDetails_Overtime_Month2' || pId=='cmplx_IncomeDetails_Overtime_Month3' || pId=='cmplx_IncomeDetails_FoodAllow_Month1' || pId=='cmplx_IncomeDetails_FoodAllow_Month2' || pId=='cmplx_IncomeDetails_FoodAllow_Month3' || pId=='cmplx_IncomeDetails_PhoneAllow_Month1' || pId=='cmplx_IncomeDetails_PhoneAllow_Month2' || pId=='cmplx_IncomeDetails_PhoneAllow_Month3' || pId=='cmplx_IncomeDetails_serviceAllow_Month1' || pId=='cmplx_IncomeDetails_serviceAllow_Month2' || pId=='cmplx_IncomeDetails_serviceAllow_Month3' || pId=='cmplx_IncomeDetails_TotalAvgOther' || pId=='SupplementCardDetails_Text17' || pId=='cmplx_FinacleCore_total_avg_last13' || pId=='cmplx_FinacleCore_total_avg_last_16' || pId=='FinacleCore_Text11'  || pId=='cmplx_MOL_gross' || pId=='cmplx_MOL_permit' || pId=='cmplx_MOL_docatt' || pId=='cmplx_DEC_TotalOutstanding' || pId=='cmplx_DEC_TotalEMI' || pId=='cmplx_LoanandCard_cardlimit_val' || pId=='cmplx_LoanandCard_cardlimit_ver' || pId=='cmplx_LoanandCard_cardlimit_upd' || pId=='cmplx_EmploymentVerification_Salary_variance' || pId=='EmploymentVerification_Text5' || pId=='cmplx_EmploymentVerification_fixedsal_val' || pId=='cmplx_EmploymentVerification_fixedsal_ver' || pId=='cmplx_EmploymentVerification_fixedsalupd' || pId=='cmplx_BankingCheck_SalaryCreditUpdate' || pId=='CC_Creation_CardLimit' || pId=='CC_Creation_CombinedLimit' || pId=='cmplx_LimitInc_CurrentLimit' || pId=='cmplx_LimitInc_New_Limit' || pId=='cmplx_IncomeDetails_AnnualRent' || pId=='cmplx_IncomeDetails_AvgBal' || pId=='cmplx_IncomeDetails_CredTurnover' || pId=='cmplx_IncomeDetails_AvgCredTurnover' || pId=='cmplx_OffVerification_fxdsal_upd' || pId=='cmplx_IncomeDetails_netSal1' || pId=='cmplx_IncomeDetails_netSal2' || pId=='cmplx_IncomeDetails_netSal3'){
	
		//alert(pEvent.keyCode);
	var value = getNGValue(pId);
		if(value.indexOf(',')>-1){
			value = replaceAll(value,',','');		
		}		
		if(value.indexOf('.')>-1){
			var parts=value.split('.');
			if(keyCode_str=='110' || keyCode_str=='190'){
				return false;
			}
			else if(parts[1].length==2){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}else{
			if(value.length==17 && !(keyCode_str=='110' || keyCode_str=='190')){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}
		return true;
	 }
	 //for 18,2 fields
	//Added by shweta for pcasp-1504
	 else if(pId=='totalBalanceTransfer'  || pId=='cmplx_CC_Loan_DDSAmount'  ||  pId=='cmplx_CC_Loan_FlatAMount' || pId=='amount' || pId=='ExtLiability_Limit' || pId=='cmplx_IncomeDetails_UnderwritingOther' ||  pId=='totalBalanceTransfer' || pId=='InwardTT_amount' || pId=='cmplx_FinacleCore_total_avg_last13' || pId=='cmplx_FinacleCore_total_avg_last_16' || pId=='cmplx_MOL_basic' || pId=='cmplx_CardDetails_charity_amount' || pId=='AddressDetails_years' || pId=='cmplx_MOL_resall' || pId=='cmplx_MOL_transall')
	 {
	 var value = getNGValue(pId);
		if(value.indexOf(',')>-1){
			value = replaceAll(value,',','');		
		}		
		if(value.indexOf('.')>-1){
			var parts=value.split('.');
			if(keyCode_str=='110' || keyCode_str=='190'){
				return false;
			}
			else if(parts[1].length==2){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}else{
			if(value.length==18 && !(keyCode_str=='110' || keyCode_str=='190')){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}
		
		return true;
	 }
	 else if(pId=='cmplx_CC_Loan_SI_Percentage' || pId=='cmplx_CC_Loan_Percentage')//Added by shweta for pcasp-1504
		{
		
		 var value = getNGValue(pId);
		if(value.indexOf(',')>-1){
			value = replaceAll(value,',','');		
		}		
		if(value.indexOf('.')>-1){
			var parts=value.split('.');
			if(keyCode_str=='110' || keyCode_str=='190'){
				return false;
			}
			else if(parts[1].length==2){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}else{
			if(value.length==3 && !(keyCode_str=='110' || keyCode_str=='190')){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}
		
		return true;
		}
		else if(pId=='cmplx_CardDetails_cardEmbossing_name' || pId=='SupplementCardDetails_cardEmbName' || pId=='cmplx_CardDetails_Self_card_embossing') 	
	{
		var value=getNGValue(pId);
		var spaceCount= (value.split(' ').length - 1);

		if(spaceCount>2)
		{
			showAlert(pId,'Maximum 2 space is allowed!!');
			setNGValue(pId,value.trim());
			return false;
		}
		else if(spaceCount==2 && keyCode_str==32)
		{
		showAlert(pId,'Maximum 2 space is allowed!!');
			return false;
		}
		else
		{
		return true;
		}
	}	
		
	 
	 else{
		 return true;
	 }
 }
//ADDED BY AKSHAY ON 20/12/17
 function VisitSystemChecks_CAD(sheetid)
 {
	var activityName=window.parent.stractivityName;	
	if ((pname == 'PersonalLoanS' || pname == 'CreditCard') && activityName=='CAD_Analyst1')
    {
        if (sheetid == '7')//System Checks
        {
			 visitSystemChecks_Flag=true;
		}
	}
 }	

 //Done by aman on 0709
	function removeFrame(pId){
		var value= getNGValue('FrameName');
		var to_remove=document.getElementById(document.getElementById(pId).parentNode.id).parentNode.id;
		var array=value.split(',');
		var index = array.indexOf(to_remove);
		if (index > -1) {
			array.splice(index, 1);
		}
		setNGValueCustom('FrameName',array);
	}
//Done by aman on 0709	
//Added by shivang
 function checkPastDate(expDate)
{
	var today = new Date();
	 today= addZero(today.getDate()) +"/"+ addZero(today.getMonth()+1) +"/"+ today.getFullYear();
	 if(expDate<today){
	 	return true;	 	
	 }
	 else{
	 	return false;
	 }
	
}
function addZero(i) {
  if (i < 10) {
    i = "0" + i;
  }
  return i;
}
	
//changed by akshay on 31/10/18
 function cc_click_parameterlist(pId)
 {
	var args = '';
	switch(pId)
	{
		case 'Modify':	args = 'ReqLimit Priority CardProd';	break;
		case 'Delete':	args = 'Product_Frame1';	break;
		case 'IncomeDetails_Add':	args = 'IncomeDetails_Frame1';	break;
		case 'IncomeDetails_Modify':	args = 'IncomeDetails_Frame1';	break;
		case 'IncomeDetails_Delete':	args = 'IncomeDetails_Frame1';	break;
		case 'IncomeDetails_Salaried_Save':	args = 'IncomeDetails';	break;
		case 'IncomeDetails_SelfEmployed_Save':	args = 'IncomeDetails';	break;
		case 'CompanyDetails_Add':	args = 'CompanyDetails_Frame1';	break;
		case 'CompanyDetails_Modify':	args = 'CompanyDetails_Frame1 CompanyDetails_Frame2 CompanyDetails_Frame3';	break;
		case 'CompanyDetails_delete':	args = 'CompanyDetails_Frame1';	break;
		case 'CompanyDetails_Button3':	args = 'cif';	break;
		case 'CompanyDetails_Aloc_search':	args = 'compName';	break;
		case 'AuthorisedSignDetails_add':	args = 'CompanyDetails_Frame2';	break;
		case 'AuthorisedSignDetails_modify':	args = 'CompanyDetails_Frame2';	break;
		case 'AuthorisedSignDetails_delete':	args = 'CompanyDetails_Frame2';	break;
		case 'AuthorisedSignDetails_FetchDetails':	args = 'AuthorisedSignDetails_CIFNo';	break;
		case 'PartnerDetails_add':	args = 'CompanyDetails_Frame3';	break;
		case 'PartnerDetails_modify':	args = 'CompanyDetails_Frame3';	break;
		case 'PartnerDetails_delete':	args = 'CompanyDetails_Frame3';	break;
		case 'CompanyDetails_Save':	args = 'CompanyDetails_Frame3';	break;
		case 'ExtLiability_Button2':	args = 'ExtLiability_Frame4';	break;
		case 'ExtLiability_Button3':	args = 'ExtLiability_Frame4';	break;
		case 'ExtLiability_Button4':	args = 'ExtLiability_Frame4';	break;
		case 'ExtLiability_Save':	args = 'ExtLiability_Frame1';	break;
		case 'ELigibiltyAndProductInfo_Button1':	args = 'ELigibiltyAndProductInfo_Frame1 ExtLiability_Frame1 EMploymentDetails_Frame1 DecisionHistory IncomeDetails_Frame1 FrameName';	break;//changed by nikhil for PCSP-328 and also for PCSP-489 updated 12th jan
		case 'DecisionHistory_calReElig': args= 'DecisionHistory_Frame1 cmplx_DEC_Manual_Deviation NotepadDetails_Frame1'; break;//changed by nikhil for PCSP-483
		case 'ELigibiltyAndProductInfo_Save':	args = 'ELigibiltyAndProductInfo_Frame1';	break;
		case 'ELigibiltyAndProductInfo_Refresh':	args = 'ELigibiltyAndProductInfo_Frame1';	break;
		case 'Nationality_Button':	args = 'cmplx_Customer_Nationality';	break;
		case 'SecNationality_Button':	args = 'cmplx_Customer_SecNationality';	break;
		case 'Customer_FetchDetails':	args = 'CustomerDetails';	break;
		case 'Customer_FetchFirco':	args = 'CustomerDetails';	break;

		case 'Customer_Button1':	args = 'CustomerDetails';	break;
		case 'Customer_save':	args = 'CustomerDetails';	break;
		case 'Product_Save':	args = 'ProductContainer';	break;
		case 'FinacleCore_Cal_FD_Amount': args = 'FinacleCore_Frame1'; break;
		//below code added by nikhil to correct add to average functionality 18/10
		case 'FinacleCore_AddToAverage': args = 'FinacleCore_avgbal'; break;
		case 'FinacleCore_AddToTurnover': args = 'FinacleCore_Frame8'; break;
		case 'FinacleCore_Button9': args = 'FinacleCore_Frame3'; break;
		case 'Designation_button':	args = 'cmplx_EmploymentDetails_Designation';	break;
		case 'Designation_button7': args='cmplx_Customer_Designation';break;
		case 'Designation_button8_view':args='cmplx_Customer_Designation';break;
		case 'DesignationAsPerVisa_button':	args = 'cmplx_EmploymentDetails_DesigVisa';	break;
		case 'FreeZone_Button':	args = 'cmplx_EmploymentDetails_FreezoneName';	break;
		case 'EMploymentDetails_Save':	args = 'EmploymentDetails';	break;
		case 'Button_City':	args = 'AddressDetails_city';	break;
		case 'Button_State':	args = 'AddressDetails_state';	break;
		case 'AddressDetails_Button1':	args = 'AddressDetails_country';	break;
		case 'AddressDetails_addr_Add': args='Address_Details_container'; break;	
		case 'AddressDetails_addr_Modify': args='Address_Details_container'; break;
		case 'AddressDetails_addr_Delete': args='Address_Details_container'; break;//cmplx_EmploymentDetails_remarks
		case 'AddressDetails_Save': args='Address_Details_container'; break;
		case 'AltContactDetails_ContactDetails_Save': args='Alt_Contact_container'; break;
		case 'CardDetails_Button2': args='CardDetails_Frame1'; break;
		case 'CardDetails_Button3': args='CardDetails_Frame1'; break;
		case 'CardDetails_Button4': args='CardDetails_Frame1'; break;
		case 'CardDetails_Button1': args='CardDetails_Frame1 AlternateContactDetails_AirArabiaIdentifier'; break;
		case 'CardDetails_Button5': args='CardDetails_Frame1'; break;
		case 'CardDetails_modify': args='CardDetails_Frame1 AltContactDetails_Frame1'; break;
		case 'CardDetails_delete': args='CardDetails_Frame1 AltContactDetails_Frame1'; break;
		case 'CardDetails_Save': args='CardDetails_Frame1 AltContactDetails_Frame1'; break;
		case 'SupplementCardDetails_Button1': args='SupplementCardDetails_Frame1'; break;
		case 'SupplementCardDetails_Button2': args='Supplementary_Card_Details'; break;
		case 'SupplementCardDetails_Button3': args='Supplementary_Card_Details'; break;
		case 'SupplementCardDetails_Button4': args='Supplementary_Card_Details'; break;
		case 'SupplementCardDetails_Save': args='Supplementary_Card_Details'; break;
		case 'FATCA_Button1': args='FATCA_ListedReason FATCA_SelectedReason'; break;
		case 'FATCA_Button2': args='FATCA_ListedReason FATCA_SelectedReason'; break;
		case 'FATCA_Save': args='FATCA'; break;
		case 'KYC_Save': args='KYC'; break;
		case 'OECD_add': args='OECD'; break;
		case 'OECD_modify': args='OECD'; break;
		case 'OECD_delete': args='OECD'; break;
		case 'OECD_Save': args='OECD'; break;	
		case 'ButtonOECD_State':	args = 'OECD_townBirth';	break;
		case 'ReferenceDetails_Reference_Add': args='Reference_Details'; break;
		case 'ReferenceDetails_Reference__modify': args='Reference_Details'; break;
		case 'ReferenceDetails_Reference_delete': args='Reference_Details'; break;
		case 'ReferenceDetails_save': args='Reference_Details'; break;
		case 'BT_Add': args='totBalTransfer'; break;
		case 'BT_Modify': args='totBalTransfer'; break;
		case 'BT_Delete': args='totBalTransfer'; break;
		case 'BTC_save': args='totBalTransfer'; break;
		case 'DDS_Add': args='CC_Loan_Frame4'; break;//below code added by shweta 
		case 'DDS_Modify': args='CC_Loan_Frame4'; break;
		case 'DDS_Delete': args='CC_Loan_Frame4'; break;
		case 'SI_Add': args='CC_Loan_Frame2'; break;
		case 'SI_Modify': args='CC_Loan_Frame2'; break;
		case 'SI_Delete': args='CC_Loan_Frame2'; break;
		case 'RVC_Add': args='CC_Loan_Frame3'; break;
		case 'RVC_Modify': args='CC_Loan_Frame3'; break;
		case 'RVC_Delete': args='CC_Loan_Frame3'; break;//above code added by shweta
		case 'DDS_save': args='CC_Loan_Frame4'; break;
		case 'SI_save': args='CC_Loan_Frame2'; break;
		case 'RVC_Save': args='CC_Loan_Frame3'; break;
		case 'PartMatch_Search': args='Part_Match'; break;
		case 'PartMatch_Blacklist': args='Part_Match'; break;
		case 'PartMatch_Company_blacklist': args='Part_Match'; break;
		case 'PartMatch_Button1': args='Part_Match'; break;
		case 'PartMatch_Save': args='Part_Match'; break;
		case 'DecisionHistory_CifLock': args='@this'; break;
		case 'DecisionHistory_CifUnlock': args='@this'; break;
		case 'NotepadDetails_Add': args='Notepad_Values'; break;
		case 'NotepadDetails_Modify': args='Notepad_Values'; break;
		case 'NotepadDetails_savebutton': args='Notepad_Values'; break;
		case 'CustDetailVerification1_Button2' : args='CustDetailVerification1_Frame1'; break;//for fcu changes 21/10/18-Nikhil
		case 'CC_Creation_Button2' : args='CC_Creation'; break;//changes by nikhil 30/10/18 for Card Creation 
		case 'RejectEnq_Save': args='RejectEnq_Frame1';break;
		case 'WorldCheck1_Add':args='WorldCheck1_Frame1';break;
		case 'WorldCheck1_Modify':args='WorldCheck1_Frame1';break;
		case 'WorldCheck1_Delete':args='WorldCheck1_Frame1';break;
		case 'WorldCheck1_Save':args='WorldCheck1_Frame1';break;
		case 'FinacleCore_Button1': args='FinacleCore_Frame6';break;
		case 'FinacleCore_Lien_modify': args='FinacleCore_Frame1';break;
		case 'FinacleCore_Button2': args='FinacleCore_Frame6';break;
		case 'FinacleCore_Button3': args='FinacleCore_Frame6';break;
		case 'FinacleCore_Button1': args='FinacleCore_Frame6';break;
		case 'ExternalBlackList_Button1': args='External_Blacklist';break;
		case 'FinacleCRMCustInfo_Add': args='Finacle_CRM_CustomerInformation';break;
		case 'FinacleCRMCustInfo_Modify': args='Finacle_CRM_CustomerInformation';break;
		case 'FinacleCRMCustInfo_Delete': args='Finacle_CRM_CustomerInformation';break;
		case 'FinacleCRMCustInfo_Button1': args='Finacle_CRM_CustomerInformation';break;
		case 'FinacleCRMIncident_Button1': args='Finacle_CRM_Incidents';break;	
		
		case 'FinacleCore_Button4': args='FinacleCore_Frame5';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'FinacleCore_Button5': args='FinacleCore_Frame5';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'FinacleCore_Repeater_modify': args='FinacleCore_Frame2';break;//changed by shweta for PCASP-2342
		case 'FinacleCore_Button10': args='FinacleCore_Frame2';break; //by jahnavi for view avaerage balance
		case 'FinacleCore_Modifycfo':args='FinacleCore_Frame1';break;
		case 'FinacleCore_Button8': args='FinacleCore_Frame2';break;//changed by shweta for PCASP-2342
		case 'SmartCheck_Add': args='SmartCheck_Frame1';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'SmartCheck_Modify': args='SmartCheck_Frame1';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'CardCollection_Button1': args='CardCollection_Frame1';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'ReferHistory_Modify': args='ReferHistory_Frame1';break;
		case 'Compliance_Button2': args='Compliance_Frame1';break;
		case 'DecisionHistory_ADD': args='DecisionHistory'; break;
		case 'DecisionHistory_ManualDevReason': args='DecisionHistory'; break;
		case 'DecisionHistory_Delete': args='DecisionHistory'; break;
		case 'DecisionHistory_Modify': args='DecisionHistory'; break;
		case 'EMploymentDetails_Button2': args='EMploymentDetails_Frame2'; break;//changed by nikhil for employer Search PCSP-70
		case 'SupplementCardDetails_Modify': args='SupplementCardDetails_Frame1'; break;//changed by nikhil 15/12/2018
		case 'SupplementCardDetails_Add': args='SupplementCardDetails_Frame1'; break;//changed by nikhil 15/12/2018
		case 'SupplementCardDetails_FetchDetails': args='SupplementCardDetails_Frame1'; break;//changed by Deepak Supplymentry fetch details button
		case 'EmploymentVerification_Button1': args='EmploymentVerification_Frame1';break;//added by nikhil for PCSP-158
		case 'BankingCheck_save': args='BankingCheck_Frame1';break;//added by nikhil for PCSP-155
		case 'OfficeandMobileVerification_Button1' : args='OfficeandMobileVerification_Frame1';break;//added by nikhil for PCSP-224
		case 'CustDetailVerification1_Button1' : args='CustDetailVerification1_Frame1';break;//added by nikhil for PCSP-235
		case 'WorldCheck_fetch': args='WorldCheck';break;//added by nikhil for PCSP-387
		case 'IncomingDocNew_Addbtn': args='IncomingDocNew_Frame1';break; // change by disha for new incoming document CR 04/01/2019
		case 'IncomingDocNew_Modifybtn': args='IncomingDocNew_Frame1';break; // change by disha for new incoming document CR 04/01/2019
		case 'IncomingDocNew_Save': args='IncomingDocNew_Frame1';break; // change by disha for new incoming document CR 04/01/2019
		case 'Designation_button_View': args='cmplx_EmploymentDetails_Designation';break;//code by nikhil for PCSP-503
		case 'DesignationAsPerVisa_button_View': args='cmplx_EmploymentDetails_DesigVisa';break;//code by nikhil for PCSP-503
		case 'KYC_Add': args='KYC_Frame1';break;
		case 'KYC_Modify': args='KYC_Frame1'; break;
		case 'Reference_Details_Reference_Add': args='Reference_Details_Frame1';break;
		case 'Reference_Details_Reference__modify':args='Reference_Details_Frame1';break;
		case 'FATCA_Add': args='Fatca_Frame6';break;
		case 'FATCA_Modify': args='Fatca_Frame6';break;
		case 'PartMatch_Search': args='PartMatch_Frame1';break;
		case 'PartMatch_Blacklist': args='PartMatch_Frame1';break;
		case 'NotepadDetails_Add1':  args='NotepadDetails_Frame3';break;//code changed by nikhil for PCSP-555
		case 'AltContactDetails_Saved':  args='AltContactDetails_Frame1';break;//code changed by nikhil for Card dispatch save
		case 'OriginalValidation_Save': args='Original_validation';break;//code for OV save
		case 'EmploymentVerification_s2_Button1': args='EmploymentVerification_s2_Frame1'; break;
		case 'Cust_ver_sp2_Button1': args='Cust_ver_sp2_Frame1'; break;
		case 'fieldvisit_sp2_Button1': args='fieldvisit_sp2_Frame1'; break;
		case 'checklist_ver_sp2_Button1': args='checklist_ver_sp2_Frame1'; break;
		case 'exceptionalCase_sp2_Button2': args='exceptionalCase_sp2_Frame1'; break;
		case 'Nationality_Button_View':args='cmplx_Customer_Nationality';break;
	//	case 'Designation_button7':args='cmplx_Customer_Designation';break;
	//by sagarika for the CRS done on 01/07/2019
	   case 'EmploymentDetails':args='cmplx_Customer_Designation';break;
	   case 'Designation_button5_View':args='cmplx_cust_ver_sp2_desig_remarks';break;
	   case 'Designation_button6_View':args='cmplx_emp_ver_sp2_desig_remarks';break;
	   case 'SecNationality_Button_View':args='cmplx_Customer_SecNationality';break;
	   case 'Button_City_View':args='AddressDetails_city';break;
	   case 'Button_State_View':args='AddressDetails_state';break;
	   case 'AddressDetails_Button1_View':args='AddressDetails_country';break;
	   case 'CardDispatchToButton_View':args='AlternateContactDetails_CardDisp';break;
	   case 'ButtonOECD_State_View':args='OECD_townBirth';break;
	   case 'Designation_button_View':args='cmplx_EmploymentDetails_Designation';break;
	   case 'DesignationAsPerVisa_button_View':args='cmplx_EmploymentDetails_DesigVisa';break;
	   case 'Designation_button8_View':args='cmplx_Customer_Designation';break;
	   case 'Nationality_Button1_View':args='PartMatch_nationality';break;
	   //++below code added by nikhil for Self-Supp CR
	   case 'CardDetails_Self_add':args='CardDetails_Frame1';break;
	   case 'CardDetails_Self_remove':args='CardDetails_Frame1';break;	   
	//--above code added by nikhil for Self-Supp CR
	   case 'SmartCheck1_Add' : args='SmartCheck1_Frame1';break;
	   case 'SmartCheck1_Modify' : args='SmartCheck1_Frame1';break;
	   case 'BussinessVerification_Button1' : args='BussinessVerification_Frame1';break;
	   case 'EmploymentVerification_s2_Designation_button2':	args = 'cmplx_emp_ver_sp2_desig_remarks';break;
	   case 'Cust_ver_sp2_Designation_button1':	args = 'cmplx_cust_ver_sp2_desig_remarks';break;
	   case 'Cust_ver_sp2_Designation_button3_View':args='Cust_ver_sp2_Text13';break;
	   case 'Cust_ver_sp2_Designation_button5_View':args='cmplx_cust_ver_sp2_desig_remarks';break;
	   case 'EmploymentVerification_s2_Designation_button4_View':args='EmploymentVerification_s2_Text1';break;
	   case 'EmploymentVerification_s2_Designation_button6_View':args='cmplx_emp_ver_sp2_desig_remarks';break;
	   case 'FinacleCore_InvestmentModify': args = 'FinacleCore_Frame13';break;
	   case 'FinacleCore_CalculateAUM': args = 'FinacleCore_Frame13';break;
		default: args='@this';
		break;
	}
	return args;
}
