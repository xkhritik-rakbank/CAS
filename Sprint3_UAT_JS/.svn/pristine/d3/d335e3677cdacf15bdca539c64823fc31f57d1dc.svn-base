/*
                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PersoanlLoanS.js
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : All js custom functions 

------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------
Date					  Change By						Change Description 
01.03.2017                Disha                         for optimization, such that on second time fragment click comboload function is not called again and again from java code
09.06.2017				  Disha							Changes done to hide OV tab at all worksteps and multiple if conditions merged
20.06.2017				  Disha							changes done to make decision mandatory - bug correct --Select-- condition also added

------------------------------------------------------------------------------------------------------------------------------------------------------
*/

var alerts_String;
var alerts_String_Map={};


// ++ below code already present - 09-10-2017 to have docindex
var row;
// ++ above code already present - 09-10-2017 to have docindex
var PLFRAGMENTLOADOPT = {};
var rlosValuesData = {};
var PLValuesData = {};
var hiddenStringPL = '';
var hiddenString = '';
var popup_win= null;
PLFRAGMENTLOADOPT['CustomerDetails'] = '';
PLFRAGMENTLOADOPT['ProductContainer'] = '';

PLFRAGMENTLOADOPT['CompanyDetails'] = '';
PLFRAGMENTLOADOPT['PartnerDetails'] = '';
PLFRAGMENTLOADOPT['EmploymentDetails'] = '';
PLFRAGMENTLOADOPT['MiscFields'] = '';
PLFRAGMENTLOADOPT['EligibilityAndProductInformation'] = '';
PLFRAGMENTLOADOPT['Address_Details_container'] = '';
PLFRAGMENTLOADOPT['Alt_Contact_container'] = '';
PLFRAGMENTLOADOPT['Supplementary_Container'] = '';
PLFRAGMENTLOADOPT['Card_Details'] = '';
PLFRAGMENTLOADOPT['FATCA'] = '';
PLFRAGMENTLOADOPT['KYC'] = '';
PLFRAGMENTLOADOPT['OECD'] = '';
PLFRAGMENTLOADOPT['CC_Loan_container'] = '';
PLFRAGMENTLOADOPT['CustomerDocumentsContainer'] = '';
PLFRAGMENTLOADOPT['OutgoingDocumentsContainer'] = '';
PLFRAGMENTLOADOPT['DeferralContainer'] = '';
PLFRAGMENTLOADOPT['DecisioningFields'] = '';
PLFRAGMENTLOADOPT['DeviationHistoryContainer'] = '';
PLFRAGMENTLOADOPT['IncomingDocuments'] = '';
PLFRAGMENTLOADOPT['DecisionHistoryContainer'] = '';
PLFRAGMENTLOADOPT['AuthorisedSignatoryDetails'] = '';
PLFRAGMENTLOADOPT['RepaymentSchedule'] = '';
PLFRAGMENTLOADOPT['GuarantorDet'] = '';
PLFRAGMENTLOADOPT['IncomeDEtails'] = '';
PLFRAGMENTLOADOPT['Self_Employed'] = '';
PLFRAGMENTLOADOPT['InternalExternalLiability'] = '';
PLFRAGMENTLOADOPT['LoanDetails'] = '';
PLFRAGMENTLOADOPT['DecisionHistory'] = '';
PLFRAGMENTLOADOPT['Inc_Doc'] = '';
PLFRAGMENTLOADOPT['Limit_Inc'] = '';
PLFRAGMENTLOADOPT['Loan_Disbursal'] = '';
PLFRAGMENTLOADOPT['CC_Creation'] = '';
PLFRAGMENTLOADOPT['FCUDecision'] = '';
PLFRAGMENTLOADOPT['FinacleCRM_Incidents'] = '';
PLFRAGMENTLOADOPT['FinacleCRM_CustInfo'] = '';
PLFRAGMENTLOADOPT['Finacle_Core'] = '';
PLFRAGMENTLOADOPT['MOL'] = '';
PLFRAGMENTLOADOPT['World_Check'] = '';
PLFRAGMENTLOADOPT['Sal_Enq'] = '';
PLFRAGMENTLOADOPT['Reject_Enq'] = '';
PLFRAGMENTLOADOPT['Cust_Detail_verification'] = '';
PLFRAGMENTLOADOPT['Reference_Details'] = '';
PLFRAGMENTLOADOPT['Business_verification'] = '';
PLFRAGMENTLOADOPT['Home_country_verification'] = '';
PLFRAGMENTLOADOPT['Residence_verification'] = '';
PLFRAGMENTLOADOPT['Guarantor_verification'] = '';
PLFRAGMENTLOADOPT['Reference_detail_verification'] = '';
PLFRAGMENTLOADOPT['Office_verification'] = '';
PLFRAGMENTLOADOPT['Loan_card_details'] = '';
PLFRAGMENTLOADOPT['NotepadDetails_FCU'] = '';
PLFRAGMENTLOADOPT['NotepadDetails_CAD'] = '';
PLFRAGMENTLOADOPT['Smart_check'] = '';
PLFRAGMENTLOADOPT['Cust_Det_Ver'] = '';
PLFRAGMENTLOADOPT['Business_Ver'] = '';
PLFRAGMENTLOADOPT['Emp_Verification'] = '';
PLFRAGMENTLOADOPT['Bank_Check'] = '';
PLFRAGMENTLOADOPT['NotepadDetails_CPV'] = '';
PLFRAGMENTLOADOPT['Supervisor_section'] = '';
PLFRAGMENTLOADOPT['Smart_chk'] = '';
PLFRAGMENTLOADOPT['Orig_validation'] = '';
PLFRAGMENTLOADOPT['Compliance'] = '';
PLFRAGMENTLOADOPT['Dispatch_Details'] = '';
PLFRAGMENTLOADOPT['Post_Disbursal'] = '';
PLFRAGMENTLOADOPT['Dec'] = '';
PLFRAGMENTLOADOPT['ReferHistory'] = '';
PLFRAGMENTLOADOPT['ReferenceDetails'] = ''; 
PLFRAGMENTLOADOPT['Credit_card_Enq'] = '';
PLFRAGMENTLOADOPT['Case_History'] = '';
PLFRAGMENTLOADOPT['LOS'] = '';
PLFRAGMENTLOADOPT['Part_Match'] = '';
PLFRAGMENTLOADOPT['Risk_Rating'] = '';
PLFRAGMENTLOADOPT['Notepad_Values'] = '';
PLFRAGMENTLOADOPT['Note_Details'] = '';
PLFRAGMENTLOADOPT['Postdisbursal_Checklist'] = '';
PLFRAGMENTLOADOPT['External_Blacklist'] = '';
PLFRAGMENTLOADOPT['Credit_card_Enq1'] = '';
PLFRAGMENTLOADOPT['LOS1'] = '';
PLFRAGMENTLOADOPT['Case_History1'] = '';
PLFRAGMENTLOADOPT['Supplementary_Card_Details'] = '';

function focus_PersonalLoanS(pId){
	var activityName=window.parent.stractivityName;
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
	
	//alert("focus on control: "+pId);
	if(activityName == 'CSM' || activityName== 'DDVT_Maker' || activityName== 'DDVT_maker'){
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


function click_PersonalLoanS(pId, frameState)
{
    var activityName = window.parent.stractivityName;
   
//below code added by akshay on 26/11/18 for refer functionality       
if(pId=='DecisionHistory_ADD')
	{
		if(checkMandatory_Add())
		{
				if(Add_Validate())
				{
					setLocked('cmplx_Decision_Decision',true);
					return true;
				}					
		}
	}
	if(pId=='DecisionHistory_Delete')
	{
		if(delete_dec_row()){
			return true
		}
	}
	
if(pId=='DecisionHistory_Modify')
{
	if(com.newgen.omniforms.formviewer.getNGListIndex('Decision_ListView1')==-1)
	{
		showAlert('','Please select a row to modify');
		return false;
	}
	else if(modify_row())
	{
		if(checkMandatory_Add())
		{
			setLocked('cmplx_Decision_Decision',true);
			return true;
		}
	}
	else
	{
		return false;
	}

}

else if(pId=='Decision_ListView1')
{
	setLocked('cmplx_Decision_Decision',false);
	var selectedRow_Decision=com.newgen.omniforms.formviewer.getNGListIndex('Decision_ListView1');
if(selectedRow_Decision!=-1)
{
var decision=getLVWAT('Decision_ListView1', selectedRow_Decision,3);
var Remarks=getLVWAT('Decision_ListView1', selectedRow_Decision,4);
var Decision_Reason=getLVWAT('Decision_ListView1', selectedRow_Decision,7);
setNGValue("cmplx_Decision_Decision",decision);
setNGValue("cmplx_Decision_REMARKS",Remarks);
setNGValue("DecisionHistory_DecisionReasonCode",Decision_Reason);	
}
}

   
   
   if (activityName == 'DDVT_maker')
    {
	// disha FSD
        if (pId == 'CustomerDetails' || pId == 'ProductContainer' || pId == 'IncomeDEtails' || pId == 'DecisionHistoryContainer' || pId == 'MiscFields' || pId ==
            'Address_Details_container' || pId == 'Supplementary_Container' || pId == 'FATCA' || pId == 'KYC' || pId == 'OECD' || pId ==
            'CustomerDocumentsContainer' || pId == 'OutgoingDocumentsContainer' || pId == 'DeferralContainer' || pId == 'DecisioningFields' || pId ==
            'DeviationHistoryContainer' || pId == 'CompanyDetails' || pId == 'PartnerDetails' || pId == 'DeferralDocuments' || pId == 'IncomingDocuments' ||
            pId == 'RepaymentSchedule' || pId == 'GuarantorDet' || pId == 'Self_Employed' || pId == 'InternalExternalLiability' || pId == 'LoanDetails' || pId ==
            'DecisionHistory' || pId == 'EligibilityAndProductInformation' || pId == 'Inc_Doc' || pId == 'EmploymentDetails' || pId == 'Alt_Contact_container' ||
            pId == 'FinacleCRM_Incidents' || pId == 'FinacleCRM_CustInfo' || pId == 'Finacle_Core' || pId == 'MOL' || pId == 'World_Check' || pId == 'Sal_Enq' ||
            pId == 'Reject_Enq' || pId == 'PartMatch_Search' || pId == 'ReferenceDetails' || pId == 'Credit_card_Enq' || pId == 'Case_History' || pId == 'LOS' || pId == 'Risk_Rating' || pId == 'Notepad_Values' || pId == 'Card_Details' || pId == 'External_Blacklist' || pId == 'Business_Verif' || pId == 'Supervisor_section' || pId == 'Supplementary_Card_Details' || pId == 'Credit_card_Enq1' || pId == 'Case_History1' || pId=='LOS1')
        {
			//change by saurabh on 8th Jan
            var patmatch_flag = getNGValue('Is_PartMatchSearch');
            /*if (pId == 'PartMatch_Button1')
            {
				var partCIF = getNGValue('PartMatch_CIFID');
							if(partCIF !='' && partCIF.length!=7)
							{
								showAlert('PartMatch_CIFID',alerts_String_Map['PL168']);
								return false;
							}
				
                setNGValue('cmplx_PartMatch_partmatch_flag', 'N');
                return true;
            }
            if (patmatch_flag == '' || patmatch_flag == null || patmatch_flag == 'N')
            {
                showAlert('Part_Match', alerts_String_Map['PL169']);
                return false;
            }*/
			if(pId == 'PartMatch_Search'){
					if(checkMandatory(PL_PartMatch))
					{							
						var partCIF = getNGValue('PartMatch_CIFID');
						if(partCIF !='' && partCIF.length!=7)
						{
							showAlert('PartMatch_CIFID',alerts_String_Map['PL168']);
							return false;
						}
						
						setNGValue('cmplx_PartMatch_partmatch_flag','false');
						//setLocked('PartMatch_Search',true);
						return true;
					}
				}
				else if(patmatch_flag == '' || patmatch_flag == null || patmatch_flag == 'N')
            {
                showAlert('Part_Match', alerts_String_Map['PL169']);
				setNGFrameState(pId,1);
                return false;
            }
           /* commented due to proc 12198
		   if(pId=='DecisionHistory')
					 {
						if(isVisible('IncomeDetails_Frame1')==false)
						{
							showAlert('IncomeDEtails','Please Visit Income Details First.');
							setNGFrameState('DecisionHistory',1);
							return false;
						}
						if(isVisible('ExtLiability_Frame1')==false)
						{
							showAlert('InternalExternalLiability','Please Visit Liability Section First.');
							setNGFrameState('DecisionHistory',1);
							return false;
						}
						if(isVisible('ELigibiltyAndProductInfo_Frame1')==false)
						{
							showAlert('EligibilityAndProductInformation','Please Visit Eligibility section First.');
							setNGFrameState('DecisionHistory',1);
							return false;
						}
					 }*/
					 //Nikhil Code added to check mandatory frames opened at every workstage or not. 16 Oct 2018
					 if(pId=='DecisionHistory' )
					 {
						if(!checkMandatory_Frames(getNGValue('Mandatory_Frames')))
						return false;
					 }
                return true;
        }
        if (pId == 'Part_Match')
            return true;

    }
    else
    {
        //for optimization, such that on second time fragment click comboload function is not called again and again from java code
	// disha FSD
        if (pId == 'CustomerDetails' || pId == 'ProductContainer' || pId == 'IncomeDEtails' || pId == 'EmploymentDetails' || pId == 'DecisionHistoryContainer' ||
            pId == 'MiscFields' || pId == 'Address_Details_container' || pId == 'Alt_Contact_container' || pId == 'Supplementary_Container' || pId == 'FATCA' ||
            pId == 'KYC' || pId == 'OECD' || pId == 'CustomerDocumentsContainer' || pId == 'OutgoingDocumentsContainer' || pId == 'DeferralContainer' || pId ==
            'DecisioningFields' || pId == 'DeviationHistoryContainer' || pId == 'CompanyDetails' || pId == 'PartnerDetails' || pId == 'DeferralDocuments' ||
            pId == 'IncomingDocuments' || pId == 'RepaymentSchedule' || pId == 'GuarantorDet' || pId == 'Self_Employed' || pId == 'InternalExternalLiability' ||
            pId == 'LoanDetails' || pId == 'DecisionHistory' || pId == 'EligibilityAndProductInformation' || pId == 'CC_Creation' || pId == 'Limit_Inc' || pId ==
            'Loan_Disbursal' || pId == 'Inc_Doc' || pId == 'FCUDecision' || pId == 'FinacleCRM_Incidents' || pId == 'FinacleCRM_CustInfo' || pId ==
            'Finacle_Core' || pId == 'MOL' || pId == 'World_Check' || pId == 'Sal_Enq' || pId == 'Cust_Detail_verification' || pId == 'Business_verification' ||
            pId == 'Home_country_verification' || pId == 'Residence_verification' || pId == 'Guarantor_verification' || pId == 'Reference_detail_verification' ||
            pId == 'Office_verification' || pId == 'Loan_card_details' || pId == 'Notepad_Details' || pId == 'Notepad_details' || pId == 'Smart_check' || pId ==
            'Cust_Det_Ver' || pId == 'Business_Verif' || pId == 'Emp_Verification' || pId == 'Bank_Check' || pId == 'Note_Details' || pId == 'Supervisor_section' ||
            pId == 'Smart_chk' || pId == 'Orig_validation' || pId == 'Compliance' || pId == 'Dispatch_Details' || pId == 'Post_Disbursal' || pId == 'Dec' ||
            pId == 'Part_Match' || pId == 'Reject_Enq' || pId == 'ReferenceDetails' || pId == 'Credit_card_Enq' || pId == 'Case_History' || pId == 'LOS' || pId == 'Risk_Rating' || pId == 'Notepad_Values' || pId == 'Card_Details' || pId == 'External_Blacklist' || pId =='Postdisbursal_Checklist' || pId == 'Supplementary_Card_Details' || pId == 'Credit_card_Enq1' || pId == 'Case_History1' || pId=='LOS1')
        {
				if(activityName=='CSM' && pId=='Alt_Contact_container')
			{
				setVisible("AltContactDetails_Label6", false);
				setVisible("AlternateContactDetails_OfficeExt", false);
			}
			 if(pId=='DecisionHistory' )
					 {
						if(!checkMandatory_Frames(getNGValue('Mandatory_Frames')))
						return false;
					 }
            if (PLFRAGMENTLOADOPT)
            {
                var key = pId;
                //alert(key);
                //alert("fd1"+PLFRAGMENTLOADOPT);
                var value = PLFRAGMENTLOADOPT[key];

                if (value == '' || value == 'Y')
                {
                    doNotLoadFragmentSecTime(pId);
                    return true;
                }
                else
                    return false;

            }
        }
    }

	//Below Code added by prabhakar for crn generation and kalayan
			 if(pId=='cmplx_CardDetails_cmplx_CardCRNDetails')
                {
                     var row=getNGListIndex('cmplx_CardDetails_cmplx_CardCRNDetails');
                    //alert(getLVWAT(pId,row,2).length);
                    if(getLVWAT(pId,row,2).length==0)
						{
							setLocked("CardDetails_Button1",false);
                        }
                    else
                    {
                     setLocked("CardDetails_Button1",true);
                    }
                    if((getLVWAT(pId,row,0).indexOf("KALYAN")>-1 && getLVWAT(pId,row,2).length>0))//|| getLVWAT(pId,row,2).length<0
                    {
                     setLocked("CardDetails_Button5",false);
                    }
                    else
                    {
                     setLocked("CardDetails_Button5",true);
                    }
					if(row>-1){
		return true;//required for loading picklist of fee profile etc. specific to card product
		}
				//return true;
                }
                //Above Code added by prabhakar for crn generation and kalyan    
    if (pId == 'ReferHistory' && frameState == 0)
    {
        if (getNGValue("cmplx_Decision_Decision") == null)
        {
            showAlert('', alerts_String_Map['PL171']);
            setNGFrameState('ReferHistory', 1);
        }
        else if (PLFRAGMENTLOADOPT['ReferHistory'] == '')
        {
            PLFRAGMENTLOADOPT['ReferHistory'] = 'N';
            return true;
        }

    }
	//changes done by disha for new generate template socket on 11-Sep-2018 start
	else if(pId=='Gen_welc_letter'){
		var wi_name = window.parent.pid;
			var prod_type = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,0);
			var app_type = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
			var docName = "Welcome_Letter_Conventional_Topup";
			if(prod_type=='Conventional'){
				if(app_type.indexOf('NEW')>-1 || app_type.indexOf('TKO')>-1){
					docName = 'Welcome_Letter_Conventional_New';
				}
				else if(app_type.indexOf('TOP')>-1){
					docName = 'Welcome_Letter_Conventional_Topup';
				}
			}
			else if(prod_type=='Islamic'){}
			
			var attrbList = "";		
			//attrbList += "";//PLTemplateData(); 
			attrbList += "&<Current_date_time>&" + getNGValue('CurrentDateLabel')+"@10";
			attrbList += "&<AccountNo>&"+getNGValue('Account_Number')+"@10";
			attrbList += "&<Loanamount>&"+getNGValue('cmplx_LoanDetails_lonamt')+" AED@10";
			attrbList += "&<FirstRepaymentdate>&"+getNGValue('cmplx_LoanDetails_frepdate')+"@10";
			attrbList += "&<EMI>&"+getNGValue('cmplx_LoanDetails_loanemi')+" AED@10";
			
			var email = getNGValue('AlternateContactDetails_EMAIL1_PRI');
			attrbList+="@#"+email;
			
			attrbList=encodeURIComponent(attrbList);
			
			var pass = "";
			if(getNGValue('cmplx_Customer_FIrstNAme')!='' && getNGValue('cmplx_Customer_DOb')!=''){
				var name="";
				var dob="";
				if(getNGValue('cmplx_Customer_FIrstNAme').length>4){
					name = getNGValue('cmplx_Customer_FIrstNAme').toLowerCase().substring(0,4);
				}
				dob = getNGValue('cmplx_Customer_DOb').split('/');
				pass = name+dob[0]+dob[1];
				
				CallAjaxTemplates(attrbList,wi_name,docName,'Y',pass);
			}
	}
	//changes done by disha for new generate template socket on 11-Sep-2018 end
	
	if(pId=="FinacleCRMCustInfo_Modify"){
		return true;
	
	}
	//aded by saurabh on 23rd July.
	else if(pId=='FinacleCRMCustInfo_Delete'){
		return true;
	}
	else if(pId=='IncomeDEtails'){
		document.getElementById("Button2").click();
				return false;
	}
	else if(pId=='IncomingDocuments'){
		document.getElementById("Button1").click();
				return false;
	}
	else if(pId=='Finacle_Core'){
		document.getElementById("Button4").click();
				return false;
	}
	else if(pId=='Cust_Detail_verification'){
		document.getElementById("Button5").click();
				return false;
	}
	else if(pId=='Loan_card_details'){
		document.getElementById("Button6").click();
				return false;
	}
	else if(pId=='FinacleCore_Button2'){	
				if(getNGValue('FinacleCore_accno')=='')
			{
				showAlert('FinacleCore_accno','Account NO Should Not Be blank');
						return false;
			}
			if(getNGValue('FinacleCore_Text1')=='')
			{
				showAlert('FinacleCore_accno',alerts_String_Map['PL224']);
						return false;
			}
			if(getNGValue('FinacleCore_cheqtype')=='' || getNGValue('FinacleCore_cheqtype')=='--Select--')
			{
				showAlert('FinacleCore_cheqtype',alerts_String_Map['PL225']);
						return false;
			}
			if(getNGValue('FinacleCore_cheqamt')=='')
			{
				showAlert('FinacleCore_cheqamt',alerts_String_Map['PL226']);
						return false;
			}
			return true;
		}
		
	else if(pId == 'cmplx_CCCreation_cmplx_CCCreationGrid'){
		var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CCCreation_cmplx_CCCreationGrid');
		if(selectedRow!=-1){
			//var updCustFlag = getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,10);
			var cardCreationFlag = getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,8);
			var capsFlag = getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow,9);
			/*if(updCustFlag!='Y'){
				setEnabled('CC_Creation_Update_Customer',true);
				setEnabled('CC_Creation_Button2',false);
				setEnabled('CC_Creation_Caps',false);
			}*/
			if(cardCreationFlag!='Y'){
				setEnabled('CC_Creation_Button2',true);
				setEnabled('CC_Creation_CAPS',false);
			}
			if(cardCreationFlag=='Y' && capsFlag!='Y'){
				
				setEnabled('CC_Creation_Button2',false);
				setEnabled('CC_Creation_CAPS',true);
			}
			if(cardCreationFlag=='Y'){
				setNGValue('CC_Creation_CardCreated','Y');
			}
			if(capsFlag=='Y'){
				setNGValue('CC_Creation_TransferToCAPS','Y');
			}
		}
		else{
			//setEnabled('CC_Creation_Update_Customer',false);
			setEnabled('CC_Creation_Button2',false);
			setEnabled('CC_Creation_CAPS',false);
		}
	}	
	//prabhakar
	else if(pId=='cmplx_Customer_SecNAtionApplicable_1')
		{
			setNGValue('cmplx_Customer_SecNationality',"");
			setLocked('cmplx_Customer_SecNationality',true);
			

		}
		
		else if(pId=='cmplx_Customer_SecNAtionApplicable_0'){
			setLocked('cmplx_Customer_SecNationality',false);
			setNGValue('cmplx_Customer_SecNationality',"");
			//setEnabled('cmplx_Customer_SecNationality',true);
        }
    if (pId == 'cmplx_Customer_CardNotAvailable')
    {
        if (getNGValue("cmplx_Customer_CardNotAvailable") == true)
        {
            setBlank_Customer();
            setEnabled("ReadFromCard", false);
            setVisible("Customer_Frame2", true);
            com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "850px");
            com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "870px");
            setLocked("Validate_OTP_Btn", false);
            setLocked("OTP_No", false);
           // setLocked("FetchDetails", false);
            setNGValue("cmplx_Customer_IsGenuine", false);
            setLocked("cmplx_Customer_EmiratesID", false);
            setLocked("cmplx_Customer_CIFNO", false);
            setLocked("cmplx_Customer_FIrstNAme", false);
            setLocked("cmplx_Customer_MiddleName", false);
            setLocked("cmplx_Customer_LAstNAme", false);
            setLocked("cmplx_Customer_Nationality", false);
            setLocked("cmplx_Customer_MobNo", false);
            setLocked("cmplx_Customer_DOb", false);
            setLocked("cmplx_Customer_PAssportNo", false);
            setVisible('Customer_Label55', true);
            setVisible('cmplx_Customer_marsoomID', true);
            setLocked('cmplx_Customer_marsoomID', false);
            setLocked('cmplx_Customer_NEP', true);


        }
        else
        {
            setEnabled("ReadFromCard", true);
            setBlank_Customer();
            SetDisableCustomer();
            setEnabled("FetchDetails", false);
            setNGValue("cmplx_Customer_ResidentNonResident", "--Select--");
            setVisible("Customer_Frame2", false);
            com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "780px");
            com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "790px");
            setLocked("cmplx_Customer_EmiratesID", true);
            setLocked("cmplx_Customer_CIFNO", true);
            setLocked("cmplx_Customer_FIrstNAme", true);
            setLocked("cmplx_Customer_MiddleName", true);
            setLocked("cmplx_Customer_LAstNAme", true);
            setLocked("cmplx_Customer_Nationality", true);
            setLocked("cmplx_Customer_MobNo", true);
            setLocked("cmplx_Customer_DOb", true);
            setLocked("cmplx_Customer_PAssportNo", true);
            setLocked("cmplx_Customer_NEP", false);
            setVisible('cmplx_Customer_marsoomID', false);
            setVisible('Customer_Label55', false);

        }
        //return true;
    }
	//Below Code added by prabhakar for crn generation and kalayan
	else if(pId=='CardDetails_Button1')
	{
		
			setLocked("CardDetails_Button1",true);
			return true;
		
	}
	else if(pId=='OriginalValidation_Save')
	{
	removeFrame(pId);
	return true;
	}
	
	//Added by aman 08062018
	else if(pId=='Nationality_Button' || pId=='SecNationality_Button'|| pId=='BirthCountry_Button'|| pId=='ResidentCountry_Button'|| pId=='Designation_button'|| pId=='AddressDetails_Button1'|| pId=='Nationality_ButtonPartMatch'|| pId=='MOL_Nationality_Button'|| pId=='CardDetails_bankName_Button'|| pId=='EmploymentDetails_Bank_Button'|| pId=='DesignationAsPerVisa_button'|| pId=='FreeZone_Button'|| pId=='CardDispatchToButton')
	{
		return true;
	}
	//Added by aman 08062018
	else if(pId=='cmplx_Liability_New_cmplx_LiabilityAdditionGrid')
	{
			if(getNGListIndex(pId)>-1){
				if(getLVWAT(pId,getNGListIndex(pId),5)=='true' && window.parent.stractivityName=='CSM' ){
					setLocked('ExtLiability_QCAmt',false);
					setLocked('ExtLiability_QCEMI',false);
				}
				else{
					setLocked('ExtLiability_QCAmt',true);
					setLocked('ExtLiability_QCEMI',true);
				}
				if(getLVWAT(pId,getNGListIndex(pId),3)=='true' && window.parent.stractivityName=='CSM' ){
					setLocked('ExtLiability_takeoverAMount',false);
					
				}
				else{
					setLocked('ExtLiability_takeoverAMount',true);
					
				}
			}
			
			//return true;
	}
	else if(pId=='CardDetails_Button5')
	{
	
			setLocked("CardDetails_Button5",true);
			return true;
		
	}
	else if(pId=='cmplx_CardDetails_cmplx_CardCRNDetails')
	{
		var activityName = window.parent.stractivityName;
		if(activityName=='DDVT_maker')
		{
		var row=getNGListIndex('cmplx_CardDetails_cmplx_CardCRNDetails');
		if (row==-1){
			setLocked("CardDetails_Button1",true);
			setLocked("CardDetails_Button5",true);
		}
		else{
		if(getLVWAT(pId,row,2).length==0)
		{
			setLocked("CardDetails_Button1",false);
		}
		else
		{
			setLocked("CardDetails_Button1",true);
		}
		console.log(getLVWAT(pId,row,8));
		if((getLVWAT(pId,row,0).indexOf("KALYAN")>-1) && getLVWAT(pId,row,2).length>0 && getLVWAT(pId,row,8).length==0)//|| getLVWAT(pId,row,2).length<0
		{
			setLocked("CardDetails_Button5",false);
		}
		else
		{
			setLocked("CardDetails_Button5",true);
			
		}
		}
		//return true;
	}
	}
	//Above Code added by prabhakar for crn generation and kalyan	
	else if(pId=='ExtLiability_Button1'){
			
			if(AccountSummary_checkMandatory())
			{
				setNGValue('IS_AECB','Y');
				var request_name = "";
				var full_name = getNGValue('cmplx_Customer_FIrstNAme')+' '+getNGValue('cmplx_Customer_MiddleName')+' '+getNGValue('cmplx_Customer_LAstNAme');
				var TxnAmount=replaceAll(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,3),',','');//.replace(',','');
				var NoOfInstallments=replaceAll(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6),',','');
				var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
				var subprod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
				var gender = getNGValue('cmplx_Customer_gender');
				var parentWiName = getNGValue('Parent_WIName');
				var OverridePeriod="0";
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
				
				if(getNGValue('cmplx_Liability_New_overrideAECB')==true){
					OverridePeriod='1';
				}
				//changes done by Deepak to make provider no unique
				var curr_date = new Date();
				var wi_name = window.parent.pid;
				var wi_name_aecb = wi_name.substring(5,14)+''+curr_date.getFullYear()+''+curr_date.getMonth()+''+curr_date.getDate()+''+curr_date.getHours()+''+curr_date.getMinutes()+''+curr_date.getMilliseconds();
				var activityName = window.parent.stractivityName;
				//Changes done for company AECB start
				var comp_row=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
				var company_cif="";
				var trade_comp_name="";
				var trade_lic_no="";
				var trade_lic_place="";
					for(var i=0; i<comp_row;i++){
						if (i==0){
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
					if((getNGValue('cmplx_Customer_NTB')==false) || (getNGValue('cmplx_Customer_NTB')==true && company_cif!='' && company_cif!=null))
					{
						request_name = 'InternalExposure,ExternalExposure';
					}
					else{
						request_name = 'ExternalExposure';
					}
					
					var param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_Customer_FIrstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LAstNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_MobNo":"'+getNGValue('cmplx_Customer_MobNo')+'","cmplx_Customer_gender":"'+gender+'","OverridePeriod":"'+OverridePeriod+'","wi_name":"'+wi_name_aecb+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","TxnAmount":"'+TxnAmount+'","NoOfInstallments":"'+NoOfInstallments+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_Passport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PAssport4')+'","cmplx_Username":"deepak","cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNO')+'","cif":"'+company_cif+'","trade_comp_name":"'+trade_comp_name+'","trade_lic_no":"'+trade_lic_no+'","trade_lic_place":"'+trade_lic_place+'","account_no":"'+getNGValue('Account_Number')+'","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_EmiratesID')+'","cmplx_EligibilityAndProductInfo_NumberOfInstallment":"5","cmplx_Customer_short_name":"'+getNGValue('cmplx_Customer_short_name')+'","full_name":"'+full_name+'","cmplx_Customer_NTB":"'+getNGValue('cmplx_Customer_NTB')+'"}');
				
				
				
				var url='/webdesktop/custom/CustomJSP/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod  +'&parentWiName=' + parentWiName+'&userName='+window.parent.userName+'&sessionId='+window.parent.sessionId;
				
				//window.showModalDialog(url,"","dialogHeight:300px;dialogWidth:500PX;");
				//window.open(url,"","dialogHeight:300px;dialogWidth:500PX;");
				//code modified by nikhil save latest aecb report
				var status;
				//Deepak new code added to open single window start
				if(popup_win && !popup_win.closed){
					popup_win.focus();
				}
				else{
					popup_win = window.open_(url, "", "width=500,height=500"); //changed by aman to send the code to java
				}
				//Deepak new code added to open single window END
				
				if(typeof popup_win==='object'){
					var ReportURL=popup_win['ReportUrl'];
					setNGValue('cmplx_Liability_New_Aecb_Report_Url',ReportURL);
					 status=popup_win['aecb_call_status'];
				}
				else{
					status=popup_win;
				}
				setNGValue('aecb_call_status',status);
				
				//Deepak 21-July-2018 new changes done in integration interface.
				/*
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					var url='/formviewer/resources/scripts/integration_PL.jsp?request_name=CollectionsSummary&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod +'&parentWiName=' + parentWiName +'&userName='+window.parent.userName;
					window.showModalDialog(url, "", "width=500,height=300");
				}
				
				//changed from integration.jsp to integration_PL.jsp by akshay on 3/4/18
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					var url='/formviewer/resources/scripts/integration_PL.jsp?request_name=CARD_INSTALLMENT_DETAILS&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod +'&userName='+window.parent.userName +'&parentWiName=' + parentWiName;
					window.showModalDialog(url, "", "width=500,height=300");
				}
				
				document.getElementById('ExtLiability_IFrame_internal').src='/formviewer/resources/scripts/internal_liability.jsp';
				document.getElementById('ExtLiability_IFrame_external').src='/formviewer/resources/scripts/External_Liability.jsp';
				document.getElementById('ExtLiability_IFrame_pipeline').src='/formviewer/resources/scripts/Pipeline.jsp';
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
		 
		 else if(pId=='ExtLiability_Frame4' && frameState==0)
		{
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('NEW')>-1)
			{
				setEnabled('ExtLiability_Takeoverindicator',false);
			}
			else{
				setEnabled('ExtLiability_Takeoverindicator',true);
			}
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1)
			{
				setEnabled('ExtLiability_Takeoverindicator',true);
				setNGValue('ExtLiability_takeoverAMount','');
			}
			else{
				setEnabled('ExtLiability_Takeoverindicator',false);
			}
			setLocked("ExtLiability_QCAmt", true);
			setLocked("ExtLiability_QCEMI", true);
			setLocked("ExtLiability_takeoverAMount", true);
		}
		
		 if (pId =='ExtLiability_Frame5' && frameState==0)
		 {
			//document.getElementById("ExtLiability_IFrame_pipeline").style.top="30px";
			com.newgen.omniforms.formviewer.setIFrameSrc('ExtLiability_IFrame_pipeline','/webdesktop/custom/CustomJSP/Pipeline.jsp');//dynamic load of jsp

		 }
		 
		 if (pId =='ELigibiltyAndProductInfo_Frame4' && frameState==0)//PL
		 {
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame3','/webdesktop/custom/CustomJSP/Product_Eligibility.jsp');
		 }
		 
		 if (pId =='ELigibiltyAndProductInfo_Frame6' && frameState==0)//Eligible for Card
		 {
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame2','/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp');//dynamic load of jsp
		 }
		 
		else if(pId=='ELigibiltyAndProductInfo_Frame2' && frameState==0)//Funding
		{
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame4','/webdesktop/custom/CustomJSP/Funding_Account_No.jsp');
		}
		
		else if(pId=='cmplx_CardDetails_Security_Check_Held'){
		var val=getNGValue("cmplx_CardDetails_Security_Check_Held");
		if(val==true){
			setLocked('CardDetails_BankName',false);
			setEnabled('CardDetails_ChequeNumber',true);
			setEnabled('CardDetails_Amount',true);
			setEnabled('CardDetails_Date',true);
			setEnabled('cmplx_CardDetails_CustClassification',true);
			setLocked('CardDetails_Date',false);
			setLocked('CardDetails_Amount',false);
			setLocked('CardDetails_ChequeNumber',false);
			
		}
		else{
			setLocked('CardDetails_BankName',true);
			setNGValue('CardDetails_ChequeNumber','');
			setNGValue('CardDetails_Amount','');
			setNGValue('CardDetails_Date','');
			//setEnabled('CardDetails_Combo5',false);
			setEnabled('CardDetails_ChequeNumber',false);
			setEnabled('CardDetails_Amount',false);
			setEnabled('CardDetails_Date',false);
			setLocked('CardDetails_Date',true);
			setEnabled('cmplx_CardDetails_CustClassification',false);
		}
	}
		
		
		
	//added by saurabh for point 39 in UAt observations.
	 if (pId =='cmplx_Liability_New_overrideIntLiab'){ 
    
        var liabChk = getNGValue('cmplx_Liability_New_overrideIntLiab');
        if(liabChk == true)
            {
                setVisible("Liability_New_Overwrite", true);
            }
            else{
                setVisible("Liability_New_Overwrite", false);
            }
                return false;            
        }
		
	
	
	//++above code added by abhishek for CSM point 8 on 7/11/2017
		
   else if (pId == 'cmplx_Customer_NEP')
    {
        var NEP = getNGValue("cmplx_Customer_NEP");
        if (NEP != '')
        {

            setEnabled("ReadFromCard", false);
            setVisible("Customer_Frame2", true);
            //com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "850px");
            //com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "870px");
            setEnabled("Validate_OTP_Btn", false);
            setLocked("OTP_No", true);
            setLocked("cmplx_Customer_CIFNO", false);
            setLocked("cmplx_Customer_FIrstNAme", false);
            setLocked("cmplx_Customer_MiddleName", false);
            setLocked("cmplx_Customer_LAstNAme", false);
            setLocked("cmplx_Customer_Nationality", false);
            setLocked("cmplx_Customer_MobNo", false);
            setLocked("cmplx_Customer_DOb", false);
            setLocked("cmplx_Customer_PAssportNo", false);
            setVisible("Customer_Label56", true);
            setVisible("cmplx_Customer_EIDARegNo", true);
            setLocked("cmplx_Customer_CardNotAvailable", true);
            setLocked("cmplx_Customer_EIDARegNo", false);
            setNGValue("cmplx_Customer_EmiratesID", "");
            SetEnableCustomer();
            setBlank_Customer();
            setLocked("cmplx_Customer_EmirateIDExpiry", true);
            setLocked("cmplx_Customer_IdIssueDate", true);
            setLocked("Customer_refname", false);
            setLocked("Customer_refmob", false);
            setLocked("Customer_refrel", false);
            setLocked("Customer_refaddr", false);
            setLocked("Customer_refph", false);
            setEnabled("Customer_ref_add", true);
            setEnabled("Customer_ref_mod", true);
            setEnabled("Customer_ref_del", true);
            setLocked("cmplx_Customer_EmiratesID", true);
            setLocked("cmplx_Customer_CardNotAvailable", true);
            setLocked("FetchDetails", true);
            setLocked("Customer_Button1", true);

        }
        else
        {
            setEnabled("ReadFromCard", true);
            setVisible("Customer_Frame2", false);
            //com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "850px");
            //com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "870px");
            setLocked("cmplx_Customer_CIFNO", true);
            setLocked("cmplx_Customer_FIrstNAme", true);
            setLocked("cmplx_Customer_MiddleName", true);
            setLocked("cmplx_Customer_LAstNAme", true);
            setLocked("cmplx_Customer_Nationality", true);
            setLocked("cmplx_Customer_MobNo", true);
            setLocked("cmplx_Customer_DOb", true);
            setLocked("cmplx_Customer_PAssportNo", true);
            setVisible("Customer_Label56", false);
            setVisible("cmplx_Customer_EIDARegNo", false);
            setLocked("cmplx_Customer_CardNotAvailable", false);
            setLocked("cmplx_Customer_EIDARegNo", true);
            SetDisableCustomer();
            setBlank_Customer();
        }
    }

    //for islamic added here


   else if (pId == 'CC_Creation')
    {
        var activityName = window.parent.stractivityName;
        if (activityName == 'Disbursal')
        {
            //alert("activityName"+activityName);
            if (n > 0)
            {
                for (var i = 0; i < n; i++)
                {
                    if (getLVWAT("cmplx_Product_cmplx_ProductGrid", i, 0) == 'Islamic')
                    {
                        setVisible('CC_Creation_Button1', true);
                    }
                    else
                    {
                        setVisible('CC_Creation_Button1', false);
                    }
                }
            }
        }
        return true;
    }
    //ended here
	//added by saurabh on 4th Jan
	else if(pId=='CardDetails_modify')
		{
		  if(checkMandatory_2ndgridcarddetails())
				return true;	
		}
		else if(pId=='ReferHistory_save')
		{removeFrame(pId);
		return true;
		}

	else if(pId=='IncomeDetails_FinacialSummarySelf')
		{	 var activityName = window.parent.stractivityName;
			
			//alert("inside financial summary");
			//24/07/2017 Liabilities should be Fetched First till it gets the status as Success
			if((getNGValue('IS_AECB')==null || getNGValue('IS_AECB') == '') && activityName != 'DDVT_maker' )
				{
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
			}
			//change for overwrite details
			//alert('financial summary: '+getNGValue('Is_Overwrite_Details').toUpperCase());
			/*if(getNGValue('Is_Overwrite_Details').toUpperCase()!='SUCCESS' && activityName != 'DDVT_maker'){
				showAlert('InternalExternalLiability','Please Overwrite Details first');
				return false;
			}----commented temprary by akshay as value not getting set in IE*/
			var request_name = "";
			var parentWiName = getNGValue('Parent_WIName');
			//alert('parent WI:'+parentWiName);
			var subproduct = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
			var empType = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5);
			
			// below code done to find opertaion names of financial summary on 29th Dec by disha
			
			/*
			if((subproduct=='Salaried Credit Card' || subproduct=='SAL') && empType=='Salaried'){
				request_name = 'SALDET,RETURNDET';//,Primary_CIF';
			}
			if((subproduct=='Self Employed Credit Card' || subproduct=='SE') && empType=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';//,Primary_CIF,Corporation_CIF';
			}
			if((subproduct=='Business titanium Card' || subproduct=='BTC') && empType=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';//,Primary_CIF,Corporation_CIF';
			}
			if((subproduct=='Instant Money' || subproduct=='IM') && empType=='Salaried'){
				request_name = 'RETURNDET,SALDET';//,Primary_CIF';
			}
			if((subproduct=='Limit Increase' || subproduct=='LI') && empType=='Salaried'){
				request_name = 'RETURNDET,SALDET';//,Primary_CIF';
			}
			if((subproduct=='Limit Increase' || subproduct=='LI') && empType=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';//,Primary_CIF,Corporation_CIF';
			}
			if((subproduct=='Pre-Approved' || subproduct=='PA') && empType=='Self Employed'){
				request_name = 'SALDET,RETURNDET';//,Primary_CIF,Corporation_CIF';
			}
			if((subproduct=='Pre-Approved' || subproduct=='PA') && empType=='Salaried'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';//,Primary_CIF';
			}
			if((subproduct=='Product Upgrade' || subproduct=='PU') && empType=='Salaried'){
				request_name = 'SALDET,RETURNDET';//,Primary_CIF';
			}
			if((subproduct=='Product Upgrade' || subproduct=='PU') && empType=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';//,Primary_CIF,Corporation_CIF';
			}
			if((getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='Expat Personal Loans' || getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='EXP')&& empType=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SIDET,SALDET';//'TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET';//,Primary_CIF';
			}
			if((subproduct=='Secured Card' || subproduct=='SEC') && empType=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SALDET';//,Primary_CIF';
			}
			if((subproduct=='Secured Card' || subproduct=='SEC') && empType=='Self Employed'){
				request_name = 'RETURNDET,LIENDET,AVGBALDET,TRANSUM';//,Primary_CIF,Corporation_CIF';
			}
			if((subproduct=='National Personal Loans' || subproduct=='NAT') && empType=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SIDET,SALDET';//,LIENDET,SALDET,Primary_CIF';
			}
			*/
				//var param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNO')+'"}');
				
				var param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNO')+'","sub_product":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)+'","emp_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)+'","application_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4)+'"}');
				
				// above code done to find opertaion names of financial summary on 29th Dec by disha
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				//alert ('wi_name '+ wi_name);
				//alert ('activityName '+ activityName);
				
				var url='/webdesktop/custom/CustomJSP/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name +'&activityName=' + activityName + '&parentWiName=' + parentWiName+'&sessionId='+window.parent.sessionId;;
				
				//Deepak new code added to open single window start
				if(popup_win && !popup_win.closed){
					popup_win.focus();
				}
				else{
					popup_win = window.open_(url, "", "scrollbars=yes,resizable=yes,width=620px,height=700px"); //changed by aman to send the code to java
				}
				//Deepak new code added to open single window END
				
				//window.open_(url, "", "scrollbars=yes,resizable=yes,width=620px,height=700px");
				setNGValue('Is_Financial_Summary','Y');
					return true;
			}
	

    //tanshu ddvt checker
    else if (pId == 'DecisionHistory_updcust')
    {
       // alert("inside update account request");
        return true;
    }
    //ended tanshu ddvt checker

    //for Incomindoc		
    else if (pId == 'IncomingDoc_AddFromDMSButton')
    {
		//below code added by nikhil
		//alert("row clicked inside add from pc button");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        //alert("row clicked:" + row);
		setNGValue('Rownum',row);
        var doc_clicked_id = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn4";
        //alert("value: " + document.getElementById(doc_clicked_id).value);
        var remarks_state = document.getElementById(doc_clicked_id).disabled;
        //alert("value of docname " + docname1);
		if(remarks_state == true)
		{
			return false;
		}
        addDocFromOD();
    }

    else if (pId == 'IncomingDoc_AddFromPCButton')
    {
		//below code added by nikhil
		//alert("row clicked inside add from pc button");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        //alert("row clicked:" + row);
		setNGValue('Rownum',row);
        var doc_clicked_id_1 = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn4";
        //alert("value: " + document.getElementById(doc_clicked_id).value);
        var remarks_state = document.getElementById(doc_clicked_id_1).disabled;
        //alert("value of docname " + docname1);
		if(remarks_state == true)
		{
			return false;
		}
        //alert("row clicked inside add from pc button");
       // var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        //alert("row clicked:" + row);
        var doc_clicked_id = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn0";
        //alert("value: " + document.getElementById(doc_clicked_id).value);
        var docname1 = document.getElementById(doc_clicked_id).value;
        //alert("value of docname " + docname1);
        addDocFromPC();
        window.parent.DocName(docname1);
        return true;
    }

    else if (pId == 'IncomingDoc_ScanButton')
    {
		//below code added by nikhil
		//alert("row clicked inside add from pc button");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        //alert("row clicked:" + row);
        var doc_clicked_id = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn4";
        //alert("value: " + document.getElementById(doc_clicked_id).value);
        var remarks_state = document.getElementById(doc_clicked_id).disabled;
        //alert("value of docname " + docname1);
		if(remarks_state == true)
		{
			return false;
		}
        openScannerWindow();
    }

    else if (pId == 'IncomingDoc_ViewButton')
    {
		var obj = getInterfaceData("DLIST", "");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		setNGValue('Rownum',row);
        var row_print = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn11";
	  //var row_print = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn11";
						
	   var docIndex = document.getElementById(row_print).value;
		//alert("value of docIndex is:"+docIndex);
        window.parent.reloadapplet(docIndex, '');
	}
	

    else if (pId == 'IncomingDoc_PrintButton')
    {
		var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		setNGValue('Rownum',row);
        var row_print = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn11";
        var docIndex = document.getElementById(row_print).value;
        var doc_clicked_id = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn0";
        var docName = document.getElementById(doc_clicked_id).value;
        printImageDocument(docIndex);
		
    }

    else if (pId == 'IncomingDoc_DownloadButton')
    {	
		// ++ below code already present - 09-10-2017 to view befor download
		var obj = getInterfaceData("DLIST", "");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		setNGValue('Rownum',row);
        var row_print = "IncomingDoc_Frame2_Reprow" + row + "_Repcolumn11";//IncomingDoc_Frame2_Reprow0_Repcolumn11
        var docIndex = document.getElementById(row_print).value;
        //parent.reloadapplet(docIndex, '');
		// ++ above code already present - 09-10-2017 to view befor download
        customDownloadDocument();
    }
    //ended here
    /* if(pId=='LoanDetails'){
    	var activityName = window.parent.stractivityName;
    	var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
    	alert(flag_value);
    	if(flag_value!='Y' && activityName=='DDVT_maker'){
    		showAlert('Part_Match',alerts_String_Map['PL169']);
    		setNGFrameState('LoanDetails',1);
    	}	
    	else if(PLFRAGMENTLOADOPT['LoanDetails']==''){
    		PLFRAGMENTLOADOPT['LoanDetails']='N';
    		CreateIndicator("temp");
    		document.getElementById("fade").style.display="block";
    		return true;
    	}
    }*/
    //tanshu ddvt checker
    else if (pId == 'DecisionHistory_Button3')
    {
        if(getNGListIndex('cmplx_Decision_MultipleApplicantsGrid')==-1)
			{
				showAlert('','Please select Customer Type from Applicants Grid in Decision tab!!!');
				return false;
			}
        return true;
    }
    else if (pId == 'DecisionHistory_chqbook')
    {
        //alert("inside cheque book button");
        return true;
    }
    //ended DecisionHistory_Button3

	
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
			else if(getNGValue('SupplementCardDetails_Text6')==''){
			showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL053']);
				return false;
			}
		return true;	
	}		
    else if (pId == 'CC_Creation_Update_Customer')
    {
        //alert("inside Customer_Update")
        return true;
    }
	else if(pId=='CC_Creation_CAPS'){
		return true;
	}
	else if(pId=='ExternalBlackList_Button1')
			{
				return true;
			}
	
	else if(pId=='MOL1_AttachButton'){
	var docname1='MOL_Certificate';
				window.parent.DocName(docname1);
				addDocFromPC();
				return true;
	}

    //Arun
		else if(pId=='cmplx_EmploymentDetails_IncInPL')
		{
			if(getNGValue('cmplx_EmploymentDetails_IncInPL')==true)
				setEnabled('cmplx_EmploymentDetails_dateinPL',true);
			else
				setEnabled('cmplx_EmploymentDetails_dateinPL',false);
		}
	//ended by akshay for JIRA:582
	
	
		else if(pId=='Customer_save'){
			//if(checkMandatory(PL_CUSTOMER)){
			if(getNGValue('cmplx_Customer_SecNAtionApplicable_0')==true && getNGValue('cmplx_Customer_SecNationality')=='')
			{
					showAlert('cmplx_Customer_SecNationality','Please Select Secondary Nationality');
					return false;
			}
				if(getNGValue('cmplx_Customer_minor') == true && getNGValue('cmplx_Customer_guarname')=='')
				{
					showAlert('cmplx_Customer_guarname',alerts_String_Map['PL382']); //Arun (22/11/17) Guardian name is mandatory for minor case
					return false;
				}
				else if(getNGValue('cmplx_Customer_minor') == true && getNGValue('cmplx_Customer_guarcif')=='')
				{
					showAlert('cmplx_Customer_guarcif',alerts_String_Map['PL390']); //Arun (22/11/17) Guardian CIF is mandatory for minor case
					return false;
				}
				//below code added by niukhil for ddvt issues
				//activity name check added by saurabh on 10th jan
				//var query = "select New_Cif_no from ng_rlos_decisionHistory with (nolock) where wi_name = '"+window.parent.pid+"'";
				//Change done by Saurabh for for SQL Injection.
				//var query = "select isnull((select New_Cif_no from ng_rlos_decisionHistory with (nolock) where wi_name = '"+window.parent.pid+"'),(select cif_id from NG_PL_EXTTABLE where PL_wi_name = '"+window.parent.pid+"'))";
				var query = 'PL_NewCif';
				var wparams="Wi_Name=="+window.parent.pid+"~~Wi_Name=="+window.parent.pid;
				var pname = 'PLjs';
				var newCifId = getDataFromDB(query,wparams,pname).split('#')[0];
				if(getNGValue('cmplx_Customer_NTB')==true && getNGValue('cmplx_Customer_CIFNO')!='' && newCifId!=getNGValue('cmplx_Customer_CIFNO'))
				{
					showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL384']);
					return false;
				}
				else if( getNGValue('cmplx_Customer_CIFNO')=='' && getNGValue('cmplx_Customer_NTB')==false )
				{
					showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL385']);
					return false;
				}
				//added by Tarang on 01/19/2018 against drop 4 point 25
				if(getNGSelectedItemText('cmplx_Customer_RESIDENTNONRESIDENT')=='RESIDENT' && getNGValue('cmplx_Customer_EmiratesID')=='')
				{
					showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL211']);
					return false;
				}
				if(getNGValue('cmplx_Customer_VisaIssueDate')=='' && getNGValue('cmplx_Customer_ResidentNonResident')=='Y' && getNGValue('cmplx_Customer_Nationality')!='AE')
				{
					showAlert('cmplx_Customer_VisaIssueDate',alerts_String_Map['VAL355']);
					return false;
				}
				if(getNGValue('cmplx_Customer_PassportIssueDate')=='')
				{
				showAlert('cmplx_Customer_PassportIssueDate',alerts_String_Map['VAL369']);
					return false;
				}
				removeFrame(pId);
				return true;
			//}				 
		}
		
		else if(pId=='Product_Save')
		{
			if(product_Save_Check()){
				removeFrame(pId);
				return true;}
		}
		
		else if(pId=='IMD_Save')
		{
			if(IMD_Save_Check())
			{
				removeFrame(pId);
				return true;}
		}//Arun 27/12/17
		else if(pId=='SupplementCardDetails_Save'){
			if(checkMandatory(PL_SUPPLEMENTARYCARDDETAILS))
				{
				removeFrame(pId);
				return true;}
		}
		if(pId=='CardDetails_Button2'){
		if(checkMandatory_Add_CardDetails())
	{
				removeFrame(pId);
				return true;}
		}
		//Modify button of Card Details
		if(pId=='CardDetails_Button3'){
			if(checkMandatory_Add_CardDetails())
				{
				removeFrame(pId);
				return true;}
		}
		if(pId=='CardDetails_Button4'){
			{
				removeFrame(pId);
				return true;}
		}
		//below code added by nikhil 29/12/17
		else if(pId=='SupplementCardDetails_Add')
		{
			if(checkMandatory_SupplementGrid()){
				if(validateSuppEntry('add')){
					removeFrame(pId);
					if (getLVWRowCount('cmplx_CC_Loan_cmplx_btc')>30 ){
						showAlert('cmplx_CC_Loan_cmplx_btc',alerts_String_Map['VAL376']);
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
					removeFrame(pId);
					if (getLVWRowCount('cmplx_CC_Loan_cmplx_btc')>30 ){
						showAlert('cmplx_CC_Loan_cmplx_btc',alerts_String_Map['VAL376']);
					}
					else{
					return true;
					}
				return true;
		}
		}}
			
				// Changed by aman for CR 1112
		else if(pId=='SupplementCardDetails_Delete')
		{	
			if(checkForApplicantTypeInGrids('Supplement')){
				{
				removeFrame(pId);
				return true;}	
			}
		}
		
		//Added By Prabhakar for drop-4Point-3
		else if(pId=='FATCA_Add' || pId=='FATCA_Modify')
		{
			if(FATCA_Save_Check())
			{
				var selectedReason=getNGValue('cmplx_FATCA_selectedreasonhidden');
				if(selectedReason.charAt(selectedReason.length-1)==',')
				{
					selectedReason=selectedReason.substring(0,selectedReason.length-1)
				}
				setNGValue('cmplx_FATCA_selectedreasonhidden',selectedReason);
				return true;
			}
			//return true;
		}
		//Added By Prabhakar for drop-4Point-3
					
		/*
		else if(pId=='FATCA_Modify')
		{
			if(FATCA_Save_Check())
			{
			var selectedReason=getNGValue('cmplx_FATCA_selectedreasonhidden');
			if(selectedReason.endsWith(","))
			{
				selectedReason=selectedReason.slice(0, -1);
			}
			setNGValue('cmplx_FATCA_selectedreasonhidden',selectedReason);
			return true;
			}
		}
		*/
		//Added By Prabhakar for drop-4Point-3
		
		else if(pId=='FATCA_Delete')
		{
			setNGValue('cmplx_FATCA_selectedreasonhidden','');
			return true;
		}
		else if(pId=='GuarantorDetails_Save')
		{
			if(checkMandatory(PL_GUARANTOR))
				{
				removeFrame(pId);
				return true;}  
		}
		
		else if(pId=='IncomeDetails_Salaried_Save')
		{
			if(checkMandatory(PL_INCOME_SALARIED))
			{
				if(getNGValue('cmplx_IncomeDetails_SalaryXferToBank')=='' || getNGValue('cmplx_IncomeDetails_SalaryXferToBank')=='--Select--')
				{
					showAlert('cmplx_IncomeDetails_SalaryXferToBank',alerts_String_Map['PL172']);
					return false;
				}
				else if(isFieldFilled('cmplx_IncomeDetails_AvgNetSal')==false)
				{
					showAlert('cmplx_IncomeDetails_AvgNetSal','Average Net Salary cannot be blank!');
					return false;
				}	
				//code commented by aman to not show mandatory alert 
				//code change by saurabh on 3rd Oct for JIRA-2537
			/*	if(getNGValue('cmplx_IncomeDetails_Accomodation')=='Yes')
				{
					if(getNGValue('cmplx_IncomeDetails_AccomodationValue')=='')
					{	
						showAlert('cmplx_IncomeDetails_AccomodationValue',alerts_String_Map['PL036']);
						return false;
					}	
				}
			*/	
			//code commented by aman to not show mandatory alert 
				
				removeFrame(pId);
				return true;	
				//code change by saurabh on 3rd Oct for JIRA-2537 end.
			}

		}
		//commented by saurabh on 10th nov 17.
		/*else if(pId=='cmplx_IncomeDetails_Accomodation_1')
		{
			setLocked('cmplx_IncomeDetails_AccomodationValue',true);
		}
		else if(pId=='cmplx_IncomeDetails_Accomodation_0'){
			setLocked('cmplx_IncomeDetails_AccomodationValue',false);
			//setEnabled('cmplx_IncomeDetails_AccomodationValue',true);
        }*/
		
		else if(pId=='IncomeDetails_SelfEmployed_Save')
		{
			if(checkMandatory(PL_INCOME_SELFEMPLOYED))
				{
				removeFrame(pId);
				return true;}	
		}		
	// ++ below code already present - 09-10-2017
		//added button code for card_services and card_notification call
	else if(pId=='CC_Creation_Button2'){
	
	
				removeFrame(pId);
				return true;
					
		}
	//added for loan Request at Disbursal Workstep
		else if(pId=='Loan_Disbursal_Button2'){
			//alert("inside loan request");
			
				removeFrame(pId);
				return true;
		}
		//ended for loan Request at Disbursal Workstep
		
		//2/07/2017 card_services call
		if(pId=='CC_Creation_Card_Services'){
			var height=parseInt(getHeight('CustomerDetails'));
					if(height > 30){
			}
			else{
				setNGFrameState('CustomerDetails',0);
			}
			
			var height=parseInt(getHeight('EligibilityAndProductInformation'));
					if(height > 30){
			}
			else{
				setNGFrameState('EligibilityAndProductInformation',0);
			}
			
			var height=parseInt(getHeight('IncomeDEtails'));
					if(height > 30){
			}
			else{
				setNGFrameState('IncomeDEtails',0);
			}
			var height=parseInt(getHeight('Limit_Inc'));
					if(height > 30){
			}
			else{
				setNGFrameState('Limit_Inc',0);
			}
			var height=parseInt(getHeight('Card_Details'));
					if(height > 30){
			}
			else{
				setNGFrameState('Card_Details',0);
			}
			/*var height=parseInt(getHeight('Finacle_Core'));
					if(height > 30){
			}
			else{
				setNGFrameState('Finacle_Core',0);
			}*/
			
			var Overtime= getNGValue('cmplx_IncomeDetails_Overtime_Avg');
			var CIf= getNGValue('cmplx_Customer_CIFNO');
			var DBR= getNGValue('cmplx_EligibilityAndProductInfo_FinalDBR');
			var CRN= getNGValue('CC_Creation_CRN');
			var ECRN= getNGValue('CC_Creation_ECRN');
			var GrossSal= getNGValue('cmplx_IncomeDetails_grossSal');
			var ComAvg= getNGValue('cmplx_IncomeDetails_Commission_Avg');
			var SalDay= getNGValue('cmplx_IncomeDetails_SalaryDay');
			var SalTransfer= getNGValue('cmplx_IncomeDetails_SalaryXferToBank');
			var AvgBal= getNGValue('cmplx_IncomeDetails_AvgBal');
			var AvgCredit= getNGValue('cmplx_IncomeDetails_AvgCredTurnover');
			var OtherAvg= getNGValue('cmplx_IncomeDetails_Other_Avg');
			var CardLim= getNGValue('CC_Creation_CardLimit');
			var NewLim= getNGValue('cmplx_LimitInc_New_Limit');	
			var ExpiryDate= getNGValue('cmplx_LimitInc_LimitExpiryDate');
			//var BankName= getNGValue('cmplx_CardDetails_Bank_Name');
			var Text6= getNGValue('FinacleCore_Text6');
			var Text2= getNGValue('FinacleCore_Text2');
			var Date1= getNGValue('FinacleCore_DatePicker1');
			
			if(Overtime==''||Overtime==null||Overtime=='null'){
						showAlert('cmplx_IncomeDetails_Overtime_Avg',alerts_String_Map['PL173'])
						return false;
			}
			if(CIf==''||CIf==null||CIf=='null'){
						showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL174']);
						return false;
			}
			else if(DBR==''||DBR==null||DBR=='null'){
						showAlert('cmplx_EligibilityAndProductInfo_FinalDBR',alerts_String_Map['PL175']);
						return false;
			}
			else if(CRN==''||CRN==null||CRN=='null'){
						showAlert('cmplx_CCCreation_CRN',alerts_String_Map['PL176']);
						return false;
			}
			else if(ECRN==''||ECRN==null||ECRN=='null'){
						showAlert('cmplx_CCCreation_ECRN',alerts_String_Map['PL177']);
						return false;
			}
			else if(GrossSal==''||GrossSal==null||GrossSal=='null'){
						showAlert('cmplx_IncomeDetails_grossSal',alerts_String_Map['PL178']);
						return false;
			}
			else if(ComAvg==''||ComAvg==null||ComAvg=='null'){
						showAlert('cmplx_IncomeDetails_Commission_Avg',alerts_String_Map['PL179']);
						return false;
			}
			else if(SalDay==''||SalDay==null||SalDay=='null'){
						showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['PL180']);
						return false;
			}
			else if(SalTransfer==''||SalTransfer==null||SalTransfer=='null'){
						showAlert('cmplx_IncomeDetails_SalaryXferToBank',alerts_String_Map['PL172']);
						return false;
			}
			/*else if((AvgBal==''||AvgBal==null||AvgBal=='null') && !isVisible('IncomeDetails_Frame2')){
						showAlert('cmplx_IncomeDetails_AvgBal',alerts_String_Map['PL181']);
						return false;
			}
			else if(AvgCredit==''||AvgCredit==null||AvgCredit=='null' && !isVisible('IncomeDetails_Frame2')){
						showAlert('cmplx_IncomeDetails_AvgCredTurnover',alerts_String_Map['PL182']);
						return false;
			}*/
			else if(OtherAvg==''||OtherAvg==null||OtherAvg=='null' && !isVisible('IncomeDetails_Frame2')){
						showAlert('cmplx_IncomeDetails_Other_Avg',alerts_String_Map['PL183']);
						return false;
			}
			else if(CardLim==''||CardLim==null||CardLim=='null'){
						showAlert('cmplx_CCCreation_cardlim',alerts_String_Map['PL184']);
						return false;
			}
			else if(NewLim==''||NewLim==null||NewLim=='null'){
						showAlert('cmplx_LimitInc_New_Limit',alerts_String_Map['PL185']);
						return false;
			}
			else if(ExpiryDate==''||ExpiryDate==null||ExpiryDate=='null'){
						showAlert('cmplx_LimitInc_LimitExpiryDate',alerts_String_Map['PL186']);
						return false;
			}
			/*else if(BankName==''||BankName==null||BankName=='null'){
						showAlert('cmplx_CardDetails_Bank_Name',alerts_String_Map['PL187']);
						return false;
			}*/
			/*else if(Text6==''||Text6==null||Text6=='null'){
						showAlert('FinacleCore_Text6',alerts_String_Map['PL188']);
						return false;
			}
			else if(Text2==''||Text2==null||Text2=='null'){
						showAlert('FinacleCore_Text2',alerts_String_Map['PL189']);
						return false;
			}
			else if(Date1==''||Date1==null||Date1=='null'){
						showAlert('FinacleCore_DatePicker1',alerts_String_Map['PL190']);
						return false;
			}*/
			else{
			return true;
			}
		
		}
		//2/07/2017 ended card_services call
		//added for contract_Update
		else if(pId=='CC_Creation_Button1'){
			//alert("inside contract update");
			
			 var height=parseInt(getHeight('Alt_Contact_container'));
					if(height > 30){
			}
			else{
				setNGFrameState('Alt_Contact_container',0);
			}
			// disha FSD
			var mobileno= getNGValue('AlternateContactDetails_MobileNo1');
			var emailadd= getNGValue('AlternateContactDetails_EMAIL1_PRI');
			var emailadd1= getNGValue('AlternateContactDetails_EMAIL2_SEC');
			if(mobileno==''){
						showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['PL090']);
						return false;
			}
			else if(emailadd==''){
						showAlert('AlternateContactDetails_EMAIL1_PRI',alerts_String_Map['PL094']);
						return false;
			}
			else if(emailadd1==''){
						showAlert('AlternateContactDetails_EMAIL2_SEC',alerts_String_Map['PL191']);
						return false;
			}
			else{
				return true;
			}
		}
		//ended
		// ++ above code already present - 09-10-2017
		
		else if(pId=='CompanyDetails_Save')
		{
			return true;		    
		}
		
		else if(pId=='ExtLiability_Save')
		{
			//Deepak Changes done for proc - 358
			CustomSaveJsp();
			if(checkMandatory(PL_LIABILITY))
				return true;	
		}
		//code added by yash for Postdisbursal_Checklist on 12/10/2017
		else if(pId=='PostDisbursal_Checklist_Button1')
		{
			if(checkMandatory(PL_POST_DISBURSAL))
				return true;	
		}
		//ended by yash 
 else if (pId == 'EMploymentDetails_Button2')
    {
       if(getNGValue('EMploymentDetails_Text21')!='' || getNGValue('EMploymentDetails_Text22')!=''){
				return true;	
		}
		showAlert('EMploymentDetails_Button2','Kindly provide Employer name or code');
    }
	else if (pId == 'ELigibiltyAndProductInfo_Button1')
    {
    if(activityName=='CAD_Analyst1' && visitSystemChecks_Flag==false){
				showAlert('',alerts_String_Map['CC257']);
				return false;
			}
		//change by saurabh on 13th Dec
        if(getNGValue('Is_Financial_Summary')!='Y'){
				if(getNGValue('cmplx_Customer_NTB')==false){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
				}
				else if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
			}
		 if(getNGValue('IS_Employment_Saved')!='Y' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='SE'){
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}
			else{
				if(getNGValue('reEligibility_PL_counter')!=''){
					var counter_elig = getNGValue('reEligibility_PL_counter').split(';')[0];
					var counter_decision = getNGValue('reEligibility_PL_counter').split(';')[1];
					if(pId=='ELigibiltyAndProductInfo_Button1'){
						if(counter_elig==(parseInt(alerts_String_Map['VAL323'])-1).toString()){
						setNGValue('reEligibility_PL_counter',alerts_String_Map['VAL323']+';'+counter_decision);
						setEnabled('ELigibiltyAndProductInfo_Button1',false);
						}
						else if(parseInt(counter_elig)<(parseInt(alerts_String_Map['VAL323'])-1)){
						setNGValue('reEligibility_PL_counter',(parseInt(counter_elig)+1).toString()+';'+counter_decision);
						}
					}
					//Deepak Changes done for proc - 358
					CustomSaveJsp();
				}
				return true;
			}
    }
  else  if (pId == 'CustDetailVerification1_Button2')
	//nikhil -- FCU changes - 21/10/18
    {
		var EmiratesID=getNGValue("cmplx_CustDetailverification1_EmiratesId");
		var CIFID=getNGValue("cmplx_CustDetailverification1_CIF_ID");
		var EmpName=getNGValue("CustDetailVerification1_EmpName");
		var EmpCode=getNGValue("CustDetailVerification1_EmpCode");
		if(EmpName!='' || EmpCode!=''  || EmiratesID!='' || CIFID!='')
			{
				return true;
			}
			else
			{	
				showAlert('CustDetailVerification1_Button2',alerts_String_Map['PL413']);
				return false;	
			}
        
    }

 else   if (pId == 'CustDetailVerification1_save')
    {
		if(chechmandatory_SaveFCU())//Arun (22/09/17)
        return true;
    }

    else if (pId == 'BussinessVerification1_save')
    {
		if(checkMandatory_saveBussinessVeri())//Arun (22/09/17)
        return true;
    }

  else  if (pId == 'EmploymentVerification_save')
    {
		if(checkMandatory_EmpVerification())//Arun (22/09/17)
        return true;
    }

   else if (pId == 'BankingCheck_save')
    {
        return true;
    }

  else  if (pId == 'NotepadDetailsFCU_save')
    {
        return true;
    }

  else  if (pId == 'SmartCheck1_save')
    {
        return true;
    }

   else if (pId == 'supervisorsection_save')
    {
        return true;
    }
		else if(pId=='EMploymentDetails_Save')
		{
			if(Employment_Save_Check()){
			//change by saurabh on 3rd Dec 17
			setNGValue('IS_Employment_Saved','Y');	
				return true;
			}
		}
		
		else if(pId=='ELigibiltyAndProductInfo_Save')
		{
			
			//alert("inside elegible")
			  if(checkMandatory(PL_ELIGIBILITY))
					{
				removeFrame(pId);
				return true;}
		}
		
		/*else if(pId=='LoanDetails_Save')
		{
			if(checkMandatory(PL_LoanDetails))
				return true;
		}*/
		
		else if(pId=='MiscellaneousFields_Save')
		{
			if(checkMandatory(PL_Misc))
				{
				removeFrame(pId);
				return true;}
		}
				
		else if(pId=='AddressDetails_Save')
		{
			if(checkMandatory_AddressDetails_Save())
				{
				if(checkPrefferedChoice()){
					
				removeFrame(pId);
				return true;
				}
		}
		}
		
		else if(pId=='AltContactDetails_ContactDetails_Save')
		{
		//change function by nikhil
			if(ContactDetails_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='ReferenceDetails_save')
		{
			if(ReferenceDetails_Save_Check())
				return true;
				}
		else if(pId=='CardDetails_Save'){
			if(checkMandatory_CardDetails_Save())
				{
				removeFrame(pId);
				return true;}		
		}
		else if(pId=='SupplementCardDetails_Save'){
			if(checkMandatory(PL_SUPPLEMENTARYCARDDETAILS))
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='FATCA_Save'){
			if(checkMandatory_FATCA())
					{
				removeFrame(pId);
				return true;}
		}
		
		else if(pId=='cmplx_FATCA_cmplx_FATCAGrid')
			{
			var selected_row=com.newgen.omniforms.formviewer.getNGListIndex("cmplx_FATCA_cmplx_FATCAGrid");
			var usrelation=getLVWAT('cmplx_FATCA_cmplx_FATCAGrid',selected_row,1);
								

			
				if (usrelation=='O')
				{
					Fatca_disableCondition();
					setLocked('cmplx_FATCA_ListedReason',true);
					setLocked('cmplx_FATCA_SelectedReason',true);
			
							setNGValue('cmplx_FATCA_iddoc',true);
							setNGValue('cmplx_FATCA_decforIndv',true);
						}
					
						else if(usrelation=='R' || usrelation=='N')
					{
						Fatca_Enable();
						setNGValue('cmplx_FATCA_iddoc',true);
						setNGValue('cmplx_FATCA_decforIndv',false);
					}
					else if(usrelation=='C'){
							Fatca_disableCondition();
							setLocked("cmplx_FATCA_SignedDate",false);
						}
					else{
						Fatca_Enable();
						}
				return true;
			}
		else if(pId=='KYC_Add')
		{	
			if(KYC_add_Check())
				return true;
		}
		else if(pId=='KYC_Modify')
		{
			if(KYC_add_Check())
				return true;
		}
		else if(pId=='KYC_Delete')
		{
			return true;
		}
		else if(pId=='KYC_Save'){
			if(checkMandatory_KYC())
				return true;
		}
		
		
		else if(pId=='cmplx_OECD_cmplx_GR_OecdDetails'){
			var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_OECD_cmplx_GR_OecdDetails');
			if(selectedRow != -1){
				if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,0)!='N' ){
					setEnabled('OECD_CRSFlagReason',false);
				}
				if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,0)!=null && getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,5)!=""){
					setEnabled('OECD_noTinReason',false);
				}
			}
		}
		
		else if(pId=='OECD_add'){
			// disha FSD
			if(OECD_Add_Check())
				return true;
		}
		else if(pId=='OECD_Save'){
			if(checkMandatory_OECD())
				{
				removeFrame(pId);
				return true;}
		}
		
		else if(pId=='RiskRating_Button1'){
		//added by akshay on 21/10/18
			if(getNGValue('cmplx_Customer_NTB')==true && !checkMandatory(PL_RISKRATING)){
				return false;
			}
			return true;
		}
		
		else if(pId=='BTC_save'){
			if(checkMandatory(PL_SERVICEREQUEST_BTC))
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='DDS_save'){
			if(checkMandatory(PL_SERVICEREQUEST_DDS))
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='SI_save'){
			if(checkMandatory(PL_SERVICEREQUEST_SI))
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='RVC_Save'){
			if(checkMandatory(PL_SERVICEREQUEST_RVC))
				{
				removeFrame(pId);
				return true;}
		}
		//below code added by nikhil
		else if(pId=='OfficeandMobileVerification_Enable'){
			return true;
		}
		else if(pId=='IncomingDoc_Save'){
			return true;
		}
		else if(pId=='ButtonLoan_City'){
			return true;
		}
		else if(pId=='DecisionHistory_save'){
			if(checkMandatory(PL_DECISION))
			{
				// disha FSD
				if (activityName == 'CAD_Analyst1')
				{
					if(getNGValue('cmplx_Decision_Manual_Deviation')==true)
					{
						if(getNGValue('cmplx_Decision_Manual_deviation_reason')=='' )
						{
							showAlert('DecisionHistory_Button6',alerts_String_Map['PL192']);
							return false;
						}
						 }
					if(getNGValue('cmplx_Decision_strength')=='')
					{
						showAlert('cmplx_Decision_strength',alerts_String_Map['PL193']);
						return false;
					}
					if(getNGValue('cmplx_Decision_weakness')=='')
					{
						showAlert('cmplx_Decision_weakness',alerts_String_Map['PL194']);
						return false;
					}
					if(getNGValue('cmplx_Decision_REMARKS')=='')
					{
						showAlert('cmplx_Decision_REMARKS',alerts_String_Map['PL195']);
						return false;
					}
				}
			
				removeFrame(pId);
				return true;}
			
		}
	// disha FSD
	else if(pId=='cmplx_Decision_Manual_Deviation')
	{
		if(getNGValue('cmplx_Decision_Manual_Deviation')==true)
		{
			setLocked('DecisionHistory_Button6',false);
			//change by saurabh on 2nd Dec.
			setEnabled('DecisionHistory_Button5', true);
			setLocked('cmplx_Decision_CADDecisiontray', false);
				
			
		}
		else
		{
			setLocked('DecisionHistory_Button6',true);
			//change by saurabh on 2nd Dec.
			setEnabled('DecisionHistory_Button5', false);
			setLocked('cmplx_Decision_CADDecisiontray', true);
		}
	}
  else if (pId == 'Loan_Disbursal_save')
    {
        return true;
    }
    if (pId == 'CC_Creation_save')
    {
        return true;
    }
    if (pId == 'Limit_Inc_save')
    {
        return true;
    }
	// ++ below code already present - 09-10-2017
	else if(pId=='IncomeDetail_Salaried_Save'){
			if(checkMandatory(PL_SALARIED_MANDATORY_FIELDS))
				return true;
		}
	// ++ above code already present - 09-10-2017	
		//added by Arun		
		
		
		else if(pId=='DecisionHistory_Button1')
		{		
			var url="/webdesktop/custom/CustomJSP/Reject_Reasons.jsp?ProcessName=PersonalLoanS";
			window.open_(url, "", "width=500,height=500");
		}
	// disha FSD
		else if(pId=='DecisionHistory_Button6')
		{
			var deviationReason = getNGValue('cmplx_Decision_Manual_deviation_reason');
			//changed by nikhil 31/10/18 for manual deviation opening
			var url='/webdesktop/custom/CustomJSP/Manual_Deviation_Reasons.jsp?ProcessName=PersonalLoanS&sessionId='+window.parent.sessionId+'&deviationReason=' +deviationReason;

			//var url="/formviewer/resources/scripts/Manual_Deviation_Reasons.jsp?ProcessName=PersonalLoanS";
			window.open_(url, "", "width=500,height=500");
		}
				
		// Replace with below code Repayment Schedule Generation - 22-08-17
	else if (pId == 'LoanDetails_Button1')
    {
		var jspName="CustomSelect.jsp";
		var params="";
		
        var loanAmount = getNGValue('cmplx_LoanDetails_lonamt');
		if(loanAmount == '' ||  loanAmount == null)
		{
			showAlert('cmplx_LoanDetails_lonamt', alerts_String_Map['PL196']);
			return false;
		}

	/*	var interest_Rate = getNGValue('cmplx_LoanDetails_inttype');
		if(interest_Rate == '' || interest_Rate == null)
		{
			showAlert('cmplx_LoanDetails_inttype', alerts_String_Map['PL197']);
			return false;
	}*/ //Arun (28/10/17) as mapping of rate% is wrong
		
		var interest_Rate = getNGValue('cmplx_LoanDetails_netrate');
		if(interest_Rate == '' || interest_Rate == null)
		{
			showAlert('cmplx_LoanDetails_netrate','Net Rate cannot be blank');
			return false;
		} //Arun (28/10/17) rate% is mapped to Base Rate
		
	
        var tenure = getNGValue('cmplx_LoanDetails_tenor');
		if(tenure == '' || tenure == null)
		{
			showAlert('cmplx_LoanDetails_tenor', alerts_String_Map['PL198']);
			return false;
		}	
		
        var repayment_Frequency = getNGValue('cmplx_LoanDetails_repfreq');
		if(repayment_Frequency == '' || repayment_Frequency == null || repayment_Frequency == '--Select--')
		{
			showAlert('cmplx_LoanDetails_repfreq', alerts_String_Map['PL199']);
			return false;
		}
		
		var installment_plan = getNGValue('cmplx_LoanDetails_insplan');
		if(installment_plan == '' || installment_plan == null || installment_plan  == '--Select--' )
		{
			showAlert('cmplx_LoanDetails_insplan', alerts_String_Map['PL200']);
			return false;
		}
		
		var disbursementDate = getNGValue('cmplx_LoanDetails_fdisbdate');
		if(disbursementDate == '' || disbursementDate == null)
		{
			showAlert('cmplx_LoanDetails_fdisbdate', alerts_String_Map['PL201']);
			return false;
		}
		
		var firstEMIdate = getNGValue('cmplx_LoanDetails_frepdate');
		if(firstEMIdate == '' || firstEMIdate == null)
		{
			showAlert('cmplx_LoanDetails_frepdate', alerts_String_Map['PL202']);
			return false;
		}
		
       
		var dda_Refer_no = getNGValue('cmplx_LoanDetails_ddaref');
		//if(dda_Refer_no == '' || dda_Refer_no == null)
		//{
		//	showAlert('cmplx_LoanDetails_ddaref', alerts_String_Map['PL203']);
		//	return false;
		//}
        var dda_Status = getNGValue('cmplx_LoanDetails_ddastatus');
		//if(dda_Status == '' || dda_Status == null || dda_Status  == '--Select--')
		//{
		//	showAlert('cmplx_LoanDetails_ddastatus', alerts_String_Map['PL204']);
		//	return false;
		//}
		
        var repayment_Mode = com.newgen.omniforms.formviewer.getNGSelectedItemText('cmplx_EligibilityAndProductInfo_InstrumentType');
		if(repayment_Mode == '' || repayment_Mode == null)
		{
			showAlert('cmplx_EligibilityAndProductInfo_InstrumentType', alerts_String_Map['PL205']);
			return false;
		}
             
        //var out_Flow = getNGValue('LoanDetails_Text32');     
      
		var wi_name = window.parent.pid;
		var ws_name = window.parent.stractivityName;
		var sQry="SELECT Account_Number FROM ng_pl_exttable  with (nolock) where pl_Wi_Name = '"+wi_name+"'";
			params="Query="+sQry;
		//var	funding_acc = selectFundingAccNo(jspName,params);
						//Change done by Saurabh for for SQL Injection.
		//var query = "select top 1 AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where (Wi_Name = '"+getNGValue('Parent_WIName')+"' or Child_Wi = '"+getNGValue('PL_wi_name')+"') and FundingAccount = 'true'";
		var funding_acc= getDataFromDB('PL_AcctId',"Parent_Wi_Name=="+getNGValue('Parent_WIName')+"~~Wi_Name=="+getNGValue('PL_wi_name'),'PLjs').split('#')[0] ;
			if(funding_acc.indexOf('NODATAFOUND')>-1){
			//var query = "select distinct New_Account_Number from ng_rlos_decisionHistory where wi_name ='"+getNGValue('Parent_WIName')+"' or wi_name ='"+getNGValue('PL_wi_name')+"'";
				funding_acc = getDataFromDB('PL_AcctNo',"Parent_Wi_Name=="+getNGValue('Parent_WIName')+"~~Wi_Name=="+getNGValue('PL_wi_name'),'PLjs').split('#')[0] ;
			}
        //var url="/formviewer/custom/CustomJSP/Generate_Schedule.jsp";
        //var url='/formviewer/custom/CustomJSP/Generate_Schedule.jsp?repayment_Frequency=' + repayment_Frequency + '&repayment_Mode='+ repayment_Mode+ '&dda_Refer_no=' + dda_Refer_no+ '&dda_Status='+ dda_Status+ '&interest_Rate=' +interest_Rate+ '&tenure=' +tenure + '&loanAmount=' +loanAmount;
        var url = '/webdesktop/custom/CustomJSP/Generate_Schedule.jsp?repayment_Frequency=' + repayment_Frequency + '&interest_Rate=' + interest_Rate +
            '&tenure=' + tenure + '&loanAmount=' + loanAmount + '&disbursementDate=' + disbursementDate + '&firstEMIdate=' + firstEMIdate + '&funding_acc=' + funding_acc + '&installment_plan=' + installment_plan + '&wi_name=' + wi_name + '&ws_name=' + ws_name+ '&dda_Refer_no=' + dda_Refer_no + '&dda_Status=' + dda_Status + '&repayment_Mode=' + repayment_Mode;
        //var url="/formviewer/resources/scripts/repayment.jsp";
       
	    window.open_(url, "", "width=1500,height=800");
		setNGValue('cmplx_LoanDetails_is_repaymentSchedule_generated','Y');
    }
		
		else if(pId=='LoanDetails_Button3'){
			if(checkMandatory(PL_LoanAdd)){
				// ++ below code already present - 09-10-2017
				//below code added by nikhil
				if(getNGValue('LoanDetails_holdcode')!='')
				return checkMandatory('LoanDetails_holdamt#Hold Amount,LoanDetails_holdrem#Hold Remarks');
				if(LoanDetDisburse_Check())
				return true;
			}
		}
		else if(pId=='LoanDetails_IMD_Save')
		{
			if(checkMandatory_IMD())
				return true;
		}
		else if(pId=='LoanDetails_Button4'){
			if(checkMandatory(PL_LoanAdd)){
				return true;
			}
		}
		else if(pId=='LoanDetails_Button5'){
			return true;
		}
		
		else if(pId=='Customer_ref_add'){
		//cmplx_Product.cmplx_ProductGrid
			return true;
		}
		
		else if(pId=='Customer_ref_mod'){
		//cmplx_Product.cmplx_ProductGrid
			return true;
		}
		
		else if(pId=='Customer_ref_del'){
		//cmplx_Product.cmplx_ProductGrid
			return true;
		}
		
		else if(pId=='Product_Add')
		{
			if(getNGValue('ReqProd')=='Personal Loan' &&  getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')<18)
			{
				setVisible("GuarantorDet", true);
				setTop("IncomeDEtails",parseInt(getTop('GuarantorDet'))+parseInt(getHeight("GuarantorDet"))+5+"px");
				//setSheetVisible("Tab1", 1, false);
			}
			if(getNGValue('EmpType')=='Salaried'){										
				setSheetVisible("Tab1", 2, true);
			}
			else if(getNGValue('EmpType')=='Self Employed'){	
				setSheetVisible("Tab1", 2, false);
			}
			return true;
		}
		
		else if(pId=='Product_Modify')
		{
			
			if(getNGValue('Scheme') == '0' || getNGValue('Scheme') == '--Select--')
			{
			showAlert('Scheme', alerts_String_Map['PL360']);
			return false;
			}
			if(parseInt(getNGValue('ReqTenor'))<=0 || parseInt(getNGValue('ReqTenor'))>301){
				showAlert('ReqTenor',"Tenor should be between 0 and 300");
				return false;
		}
			if(getNGValue('ReqProd')=='Personal Loan' &&  getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')<18)
			{
				setVisible("GuarantorDet", true);
				if(isVisible('Customer_Frame1'))
							setTop("IncomeDEtails","1310px");
						else
							setTop("IncomeDEtails","520px");								
						
			}
			
			if(getNGValue('EmpType')=='Salaried'){										
				setSheetVisible("Tab1", 2, true);
			}
			else if(getNGValue('EmpType')=='Self Employed'){	
				setSheetVisible("Tab1", 2, false);
			}
			return true;
		}
	
			else  if (pId == 'Product_Delete')
			    {
			        //cmplx_Product.cmplx_ProductGrid
			        return true;
			    }
//repititive Product_Modify event
	//below code added by nikhil
	else if (pId=='DecisionHistory_nonContactable' || pId=='DecisionHistory_cntctEstablished')
	{
	return true;
	}
		
		else if(pId=='GuarantorDetails_add'){
		//cmplx_Guarantror.GuarantorDet
		if(!checkMandatory(PL_GUARANTOR_ADD))
		return false;
		if(getNGValue('GuarantorDetails_age')<=21)
		{
			showAlert('GuarantorDetails_age',alerts_String_Map['PL409'])
			return false;
		}
		
			return true;
		}
		else if(pId=='GuarantorDetails_modify'){
		if(!checkMandatory(PL_GUARANTOR_ADD))
		return false;
		if(getNGValue('GuarantorDetails_age')<=21)
		{
			showAlert('GuarantorDetails_age',alerts_String_Map['PL409'])
			return false;
		}
			return true;
		}
		else if(pId=='GuarantorDetails_delete'){
			return true;
		}
		else if(pId=='GuarantorDetails_Save'){
				removeFrame(pId);
				return true;}
		else if(pId=='Product_Save'){
				removeFrame(pId);
				return true;}
		//Arun from CC as per FSD
		else if(pId=='cmplx_FATCA_w8form'){
			if(getNGValue('cmplx_FATCA_w8form')==true && (getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')){
			setLocked('cmplx_FATCA_w9form',true);
			}
			else{
			setLocked('cmplx_FATCA_w9form',false);
			}	
		}
		else if(pId=='cmplx_FATCA_w9form'){
			if(getNGValue('cmplx_FATCA_w9form')==true && (getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')){
			setLocked('cmplx_FATCA_w8form',true);
			}
			else{
			setLocked('cmplx_FATCA_w8form',false);
			}	
		}
		else if (pId == 'FATCA_Button1')
    {
        return true;
    }

    else if (pId == 'FATCA_Button2')
	{
        return true;
	}
	
	else if (pId == 'ExtLiability_Add')
    {	
		if(checkMandatory_LiabilityGrid())
        return true;
    }
	else if (pId == 'ExtLiability_Modify')
    {	
		if(checkMandatory_LiabilityGrid())
        return true;
    }
	else if (pId == 'ExtLiability_Delete')
    {
        return true;
    }
//reptitive code
		
 else  if (pId == 'CAD_Decision_Button2')
    {
        var subProd_value = getNGValue('subProd');
		var apptype_value = getNGValue('AppType');
		//var emptype_value = getNGValue('EmpType');//Tarang to be removed on friday(1/19/2018)
		var emptype_value = getNGValue('EmploymentType');
        if (subProd_value == 'BTC')
        {
            window.parent.executeAction('3', 'Y');
            return true;
        }
        if (subProd_value == 'IM')
        {
            window.parent.executeAction('6', 'Y');
            return true;
        }
		if (emptype_value == 'Salaried')
        {
            window.parent.executeAction('2', 'Y');
            return true;
        }
		if (emptype_value == 'Self Employed')
        {
            window.parent.executeAction('5', 'Y');
            return true;
        }
		if ((apptype_value == 'Expat') || (apptype_value == 'National'))
        {
            window.parent.executeAction('3', 'Y');
            return true;
        }
    }
		else if(pId=='LoanDetails_Save'){ 
			if(LoanDetails_Save_Check())
				return true;
		}
		else if(pId=='LoanDetaisDisburs_Save'){ 
			if(checkMandatory(PL_LoanDetail_Save))
			return true;
		}
		/*else if(pId=='LoanDetails_IMD_save'){ 
			if(checkMandatory(PL_LoanIMD_Save))
			return true;
		}*/ //Arun (23/11/17) for save button in IMD loan details
		else if(pId=='AddressDetails_addr_Add'){
		if(checkMandatory_Address()){
				if(Address_Validate('add'))
			return true;
				}	
		}//Arun
		else if(pId=='AddressDetails_addr_Modify'){
		//CustomerDetails
		if(checkMandatory_Address()){
				if(Address_Validate('modify'))
			return true;
				}
		}//Arun
		else if(pId=='AddressDetails_addr_Delete'){
		//CustomerDetails
			return true;
		}//Arun
		
		else if(pId=='OECD_add'){
		//CustomerDetails
			// ++ below code already present - 09-10-2017
			if(checkMandatory_OecdGrid()){
				return true;
			}

		}//Arun
		else if(pId=='OECD_modify'){
		//CustomerDetails
			// ++ below code already present - 09-10-2017
			if(checkMandatory_OecdGrid()){
				return true;
			}
			
		}//Arun
		else if(pId=='OECD_delete'){
		//CustomerDetails
			return true;
		}//Arun
		
		 else if (pId == 'ReferHistory_Modify')
		    {
		        return true;
		    } //Akshay
			
		 else if(pId=='HomeCountryVerification_Button1'){
			return true;
		}
		 else if(pId=='ResidenceVerification_Button1'){
			return true;
		}
		 else if(pId=='BussinessVerification_Button1'){
			return true;
		}
		else if(pId=='OfficeandMobileVerification_Button1'){
			
			return true;
		}
		else if(pId=='GuarantorVerification_Button1'){
			return true;
		}
		else if(pId=='ReferenceDetailVerification_Button1'){
			return true;
		}
		else if(pId=='CustDetailVerification_Button1'){
			return true;
		}
		else if(pId=='LoanandCard_Button1'){
			return true;
		}
		else if(pId=='NotepadDetails_Button3'){
			return true;
		}
		else if(pId=='NotepadDetails_Button4'){
			return true;
		}
		else if(pId=='NotepadDetails_Button5'){
			return true;
		}
		else if(pId=='NotepadDetails_Button6'){
			return true;
		}
		//++Below code added by nikhil 13/11/2017 for Code merge
		else if(pId=='Compliance_Modify'){
			return true;
		}
		else if(pId=='Compliance_Save'){
					
			return true;
			
		}
//--Above code added by nikhil 13/11/2017 for Code merge
	//added by akshay on 20/9/18
		else if(pId=='Loan_Disbursal_Button1'){//Initiate Contract creation
			if(validatePastDate('cmplx_EligibilityAndProductInfo_FirstRepayDate','Repayment')){
			return true;
			}
		}
		else if(pId=='Loan_Disbursal_Button2'){//New Loan Req
			return true;
		}
    else if(pId == 'CAD_Add')
    {
        return true;
    }
    else if(pId == 'CAD_Modify')
    {
        return true;
    }
    else if(pId == 'CAD_Delete')
    {
        return true;
    }
    else if(pId == 'CAD_save')
    {
        return true;
    }
    else if(pId == 'CAD_Decision_save')
    {
        return true;
    }

     else if(pId == 'CustDetailVerification_save')
    {
       		//++Below code added by nikhil 13/11/2017 for Code merge
		if(getNGValue('cmplx_CustDetailVerification_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
			if(!checkMandatory(PL_Custdetail_CPV))
			{
				return false;
				}
				//--Above code added by nikhil 13/11/2017 for Code merge
			//++ below code added by abhishek as per CC FSD 2.7.3
			var val="cmplx_CustDetailVerification_Mob_No1_val:cmplx_CustDetailVerification_Mob_No2_val:cmplx_CustDetailVerification_dob_val:cmplx_CustDetailVerification_POBoxNo_val:cmplx_CustDetailVerification_emirates_val:cmplx_CustDetailVerification_persorcompPOBox_val:cmplx_CustDetailVerification_Resno_val:cmplx_CustDetailVerification_Offtelno_val:cmplx_CustDetailVerification_hcountrytelno_val:cmplx_CustDetailVerification_hcountryaddr_val:cmplx_CustDetailVerification_email1_val:cmplx_CustDetailVerification_email2_val";
			var ver="cmplx_CustDetailVerification_mobno1_ver:cmplx_CustDetailVerification_mobno2_ver:cmplx_CustDetailVerification_dob_verification:cmplx_CustDetailVerification_POBoxno_ver:cmplx_CustDetailVerification_emirates_ver:cmplx_CustDetailVerification_persorcompPOBox_ver:cmplx_CustDetailVerification_offtelno_ver:cmplx_CustDetailVerification_email1_ver:cmplx_CustDetailVerification_email2_ver:cmplx_CustDetailVerification_hcountrytelno_ver:cmplx_CustDetailVerification_hcontryaddr_ver";
			var update="cmplx_CustDetailVerification_mobno1_upd:cmplx_CustDetailVerification_mobno2_upd:cmplx_CustDetailVerification_dob_upd:cmplx_CustDetailVerification_POBoxno_upd:cmplx_CustDetailVerification_emirates_upd:cmplx_CustDetailVerification_persorcompPOBox_upd:cmplx_CustDetailVerification_offtelno_upd:cmplx_CustDetailVerification_email1_upd:cmplx_CustDetailVerification_email2_upd:cmplx_CustDetailVerification_hcountrytelno_upd:cmplx_CustDetailVerification_hcountryaddr_upd";
			var name="Mobile No. 1:Mobile No. 2:Date of Birth:P.O Box No.:Emirates:Personal / Company P.O Box No.:Office Telephone No.:Email Address 1:Email Address 2:Home Country Telephone No.:Home Country Address";
			if(CheckMandatory_VerificationCPV(ver,update,name,val)){
				return true;
				}
//-- Above code added by abhishek as per CC FSD 2.7.3 

		}
		else{
			return true;
		}
	}	
			
    /*else if(pId == 'BussinessVerification_save')
    {
        return true;
    }*/
	
    else if(pId == 'HomeCountryVerification_save')
    { 
		if(getNGValue('cmplx_HCountryVerification_Decision')=='Positive'){
        if(getNGValue('cmplx_HCountryVerification_Hcountrytelverified')=='Yes'){
				if(checkMandatory(PL_HOMEVERIFICATION)){
				removeFrame(pId);
				return true;}
				}
			
				else{
				  showAlert('cmplx_HCountryVerification_Hcountrytelverified',alerts_String_Map['CC154']);
				}
		}
			else{return true;}
    }
    else if(pId == 'ResidenceVerification_save')
    {
		//below code added by nikhil
		if(getNGValue('cmplx_ResiVerification_Decision')=='Positive')
		{
        if(checkMandatory(PL_RESIDENCEVERIFICATION)){
				removeFrame(pId);
				return true;}
		}
		else
		{
				removeFrame(pId);
				return true;}
				
    }
    else if(pId == 'GuarantorVerification_save')
    {
      		if(getNGValue('cmplx_GuarantorVerification_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
			{
				if(isFieldFilled('cmplx_GuarantorVerification_verdoneonmob')==false)
				{
					 showAlert('cmplx_GuarantorVerification_verdoneonmob','Verification done on Mobile not filled!!!');		
					 return false;
				}	
				else{
					if(getNGValue('cmplx_GuarantorVerification_verdoneonmob')=='Mismatch' && getNGValue('cmplx_GuarantorVerification_Guar_mobno_upd')=='')
					{
						showAlert('cmplx_GuarantorVerification_Guar_mobno_upd','Guarantor Contact Mobile no Update not filled!!!');		
					 return false;	
					}
					else{
						return true;
					}	
				}
			}
			else{	
				return true;
			}
	}
    else if(pId == 'ReferenceDetailVerification_save')
    {
        if(getNGValue('cmplx_RefDetVerification_ref1cntctd')!='--Select--' && getNGValue('cmplx_RefDetVerification_ref1cntctd')!='' ){
					if(checkMandatory(PL_REFERENCEVERIFICATION)){
				removeFrame(pId);
				return true;}
				}
				else{
				   showAlert('cmplx_RefDetVerification_ref1cntctd',alerts_String_Map['CC152']);
				}
    }
    else if(pId == 'OfficeandMobileVerification_save')
    {
       
		if(getNGValue('cmplx_OffVerification_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
	   if(getNGValue('cmplx_OffVerification_offtelnocntctd') =='Yes' && (getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=="" || getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=="--Select--"))
			{
				showAlert('cmplx_OffVerification_offtelnovalidtdfrom',alerts_String_Map['CC241']);
				return false;
			}
			if(getNGValue('cmplx_OffVerification_hrdnocntctd') =='Yes')
			{
				if(getNGValue('cmplx_OffVerification_hrdcntctno')=="")
				{
					showAlert('cmplx_OffVerification_hrdcntctno',alerts_String_Map['CC242']);
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
			
			if((isFieldFilled('cmplx_OffVerification_offtelnocntctd')==false || getNGValue('cmplx_OffVerification_offtelnocntctd')=='NA') && (isFieldFilled('cmplx_OffVerification_hrdnocntctd') ==false || getNGValue('cmplx_OffVerification_hrdnocntctd')=='NA') && (isFieldFilled('cmplx_OffVerification_colleaguenoverified')==false || getNGValue('cmplx_OffVerification_colleaguenoverified')=='NA'))
			{
				showAlert('cmplx_OffVerification_hrdnocntctd',alerts_String_Map['PL407']);
				return false;
			}
			/*if(getNGValue('cmplx_OffVerification_offtelnocntctd') =='--Select--' || getNGValue('cmplx_OffVerification_offtelnocntctd') =='')
			{
				showAlert('cmplx_OffVerification_offtelnocntctd',alerts_String_Map['PL388']);
				return false;
			}*/
			/*if(getNGValue('cmplx_OffVerification_hrdnocntctd') =='--Select--' || getNGValue('cmplx_OffVerification_hrdnocntctd') =='')
			{
				showAlert('cmplx_OffVerification_hrdnocntctd',alerts_String_Map['CC248']);
				return false;
			}*/
			//below code added by nikhil 
			if(getNGValue('cmplx_OffVerification_hrdemailverified')=='Yes' && getNGValue('cmplx_OffVerification_hrdemailid')=='')
			{
				showAlert('cmplx_OffVerification_hrdemailid',alerts_String_Map['PL386']);
				return false;
			}
			/*
			if(getNGValue('cmplx_OffVerification_colleaguenoverified') == '--Select--' || getNGValue('cmplx_OffVerification_colleaguenoverified') == '')
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
			
			if(!checkMandatory(PL_OffVerification_CPV))
			{
				return false;
				}
			
			//++ below code added by abhishek as per CC FSD 2.7.3
			var ver="cmplx_OffVerification_hrdemailverified:cmplx_OffVerification_fxdsal_ver:cmplx_OffVerification_accpvded_ver:cmplx_OffVerification_desig_ver:cmplx_OffVerification_doj_ver:cmplx_OffVerification_cnfminjob_ver";
			var update="cmplx_OffVerification_hrdemailid:cmplx_OffVerification_fxdsal_upd:cmplx_OffVerification_accpvded_upd:cmplx_OffVerification_desig_upd:cmplx_OffVerification_doj_upd:cmplx_OffVerification_cnfrminjob_upd";
			var name="HRD / Customer Email Id:Fixed Salary:Accomodation Provided:Designation:Date of Joining:Confirmed in Job";
			var val="cmplx_OffVerification_fxdsal_val:cmplx_OffVerification_accprovd_val:cmplx_OffVerification_desig_val:cmplx_OffVerification_doj_val:cmplx_OffVerification_cnfrminjob_val";
			if(CheckMandatory_VerificationCPV(ver,update,name,val)){
				
				removeFrame(pId);
				return true;
				}
		}
		else{
				removeFrame(pId);
				return true;}
    }
    else if(pId == 'LoanandCard_save')
    {
        
		if(getNGValue('cmplx_LoanandCard_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
			var ver="cmplx_LoanandCard_loanamt_ver:cmplx_LoanandCard_tenor_ver:cmplx_LoanandCard_emi_ver:cmplx_LoanandCard_islorconv_ver:cmplx_LoanandCard_firstrepaydate_ver:cmplx_LoanandCard_cardtype_ver:cmplx_LoanandCard_cardlimit_ver";
			var update="cmplx_LoanandCard_loanamt_upd:cmplx_LoanandCard_tenor_upd:cmplx_LoanandCard_emi_upd:cmplx_LoanandCard_islorconv_upd:cmplx_LoanandCard_firstrepaydate_upd:cmplx_LoanandCard_cardtype_upd:cmplx_LoanandCard_cardlimit_upd";
			var name="Loan Amount:Tenor:EMI:Islamic / Conventional:First Repayment Date:Card Type:Card Limit";
			var val="cmplx_LoanandCard_loanamt_val:cmplx_LoanandCard_tenor_val:cmplx_LoanandCard_emi_val:cmplx_LoanandCard_islorconv_val:cmplx_LoanandCard_firstrepaydate_val:cmplx_LoanandCard_cardtype_val:cmplx_LoanandCard_cardlimit_val";
					if(CheckMandatory_VerificationCPV(ver,update,name,val)){
				removeFrame(pId);
				return true;}
			//-- Above code added by abhishek as per CC FSD 2.7.3 
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		else{
				removeFrame(pId);
				return true;}
	}	
	else if(pId == 'DecisionHistory_Button5')
    {
			//change by saurabh on 14th Dec
			if(getNGValue('Is_Financial_Summary')!='Y'){
				if(getNGValue('cmplx_Customer_NTB')==false){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
				}
				else if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
			}
			else if(getNGValue('IS_Employment_Saved')!='Y' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='SE'){
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}
			if(getNGValue('reEligibility_PL_counter')!=''){
				//change by saurabh on 2nd Dec 17.
				var counter_dec = getNGValue('reEligibility_PL_counter').split(';')[1];
				var counter_elig = getNGValue('reEligibility_PL_counter').split(';')[0];
				if(parseInt(counter_dec)<parseInt(alerts_String_Map['VAL323'])){
				setNGValue('reEligibility_PL_counter',counter_elig+';'+(parseInt(counter_dec)+1));
				return true;		    
				}
				else{
				showAlert('DecisionHistory_Button5','You have exhausted your attempts to Calculate Re-eligibility');
				return false;
				}
			}
			return true;
	}
    else if(pId == 'NotepadDetails_save')
    {
       // if(checkMandatory(PL_Notepad_grid))
			return true;	
    }
	else if(pId=='SmartCheck_Add'){
	var activityName = window.parent.stractivityName;
	if(activityName=='CAD_Analyst1'){
			if(getNGValue('SmartCheck_creditrem')==''){
			showAlert('SmartCheck_creditrem','Credit Remarks cannot be blank');
			return false;
			}
		}
		else if(activityName=='CPV'){
			if(getNGValue('SmartCheck_CPVrem')==''){
			showAlert('SmartCheck_CPVrem','CPV Remarks cannot be blank');
			return false;
			}
		}
		if(getNGValue('SmartCheck_creditrem')!='')
			{
				var comments=getNGValue('SmartCheck_creditrem'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValue('SmartCheck_creditrem',datetime+'-'+userid+'-'+comments);
			}
			if(getNGValue('SmartCheck_CPVrem')!='')
			{
				var comments=getNGValue('SmartCheck_CPVrem'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValue('SmartCheck_CPVrem',datetime+'-'+userid+'-'+comments);
			}
		return true;
	}
	else if(pId=='SmartCheck_Modify'){
	var activityName = window.parent.stractivityName;
	if(activityName=='CAD_Analyst1'){
			if(getNGValue('SmartCheck_creditrem')==''){
			showAlert('SmartCheck_creditrem','Credit Remarks cannot be blank');
			return false;
			}
		}
		else if(activityName=='CPV'){
			if(getNGValue('SmartCheck_CPVrem')==''){
			showAlert('SmartCheck_CPVrem','CPV Remarks cannot be blank');
			return false;
			}
		}
		
		return true;
	
	}
    else if(pId == 'SmartCheck_save')
    {
        return true;
    }
	else if(pId == 'cmplx_SmartCheck_SmartCheckGrid')
    {
		if(com.newgen.omniforms.formviewer.getNGListIndex('cmplx_SmartCheck_SmartCheckGrid')>-1){
			setEnabled('SmartCheck_Add',false);
			setEnabled('SmartCheck_Modify',true);
		}
		else{
			setEnabled('SmartCheck_Add',true);
			setEnabled('SmartCheck_Modify',false);
		}
	
        return true;
    }
	
	
// disha FSD
    else if(pId == 'NotepadDetails_Add')
    {
	if(checkMandatory(PL_Notepad)){
			
			
			return true;
		}
	/*if(getNGValue('NotepadDetails_notedesc')=='' || getNGValue('NotepadDetails_notedesc')=='--Select--')
	{
		showAlert('NotepadDetails_notedesc',alerts_String_Map['PL2006']);
		return false;
	}
	if(getNGValue('NotepadDetails_notecode')=='' )
	{
		showAlert('NotepadDetails_notecode',alerts_String_Map['PL207']);
		return false;
	}
	if(getNGValue('NotepadDetails_noteDate')=='' )
	{
		showAlert('NotepadDetails_noteDate',alerts_String_Map['PL208']);
		return false;
	}
	if(getNGValue('NotepadDetails_user')=='' )
	{
		showAlert('NotepadDetails_user',alerts_String_Map['PL209']);
		return false;
	}
        return true;*/
    }
    	//added by disha as per FSD
		else if(pId=='NotepadDetails_Modify'){
			
			var notepadDesc = getLVWAT('cmplx_NotepadDetails_cmplx_notegrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_NotepadDetails_cmplx_notegrid"),11);
			//++below code added by abhishek on 11/11/2017 for new CR
			var GridInstructionQueue = getLVWAT('cmplx_NotepadDetails_cmplx_notegrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_NotepadDetails_cmplx_notegrid"),5);
			var FieldInstructionQueue = getNGValue('NotepadDetails_insqueue');
			if(GridInstructionQueue!=FieldInstructionQueue){
						if((notepadDesc!='NA' && notepadDesc.toUpperCase()!=activityName.toUpperCase()) ){
							
							//++Above code added by abhishek on 11/11/2017 for new CR
							 showAlert('NotepadDetails_notedesc',alerts_String_Map['VAL152']);
								 
								   document.getElementById("NotepadDetails_notedesc").value = '--Select--';
									document.getElementById("NotepadDetails_notecode").value = '';
									 document.getElementById("NotepadDetails_Actdate").value = '';
									 
									  document.getElementById("NotepadDetails_notedetails").value = '';
									document.getElementById("NotepadDetails_inscompletion").checked = false;
									 document.getElementById("NotepadDetails_ActuserRemarks").value = '';
									  document.getElementById("NotepadDetails_Actusername").value = '';
									  
									  setEnabled('NotepadDetails_inscompletion',false);
									  setEnabled('NotepadDetails_ActuserRemarks',false);
									  //++ below code added by abhishek on 10/11/2017
									   setEnabled('NotepadDetails_notedetails',true);
									  setEnabled('NotepadDetails_notedesc',true);
									  setEnabled('NotepadDetails_Add',true);
									  setEnabled('NotepadDetails_Delete',true);
									  setLocked('NotepadDetails_notedetails',false);
									  //++ above code added by abhishek on 10/11/2017
									  
								   
							  return false;
								
							
						}
						else{
							if(checkMandatory(PL_Notepad_modify))
							return true;
					}
				}
				else{
					//if(checkMandatory(PL_Notepad_modify))
					if(notepadDesc.toUpperCase()==activityName.toUpperCase())
					{
					if(getNGValue('NotepadDetails_ActuserRemarks')=='')
					{
					showAlert('NotepadDetails_ActuserRemarks','Actioned User Remarks is Mandatory');
					return false;
					}
					if(getNGValue('NotepadDetails_inscompletion')==false)
					{
					showAlert('NotepadDetails_inscompletion','Instruction Completion is Mandatory');
					return false;
					}
					}
				    return true;
				}
			
            }    
	else if(pId == 'NotepadDetails_Delete')
    {
        return true;
    }
    else if(pId == 'NotepadDetails_Delete' || pId == 'cmplx_NotepadDetails_cmplx_notegrid')
    {
        return true;
    }
    else if(pId == 'NotepadDetails_Add1')
    {
        return true;
    }
    else if(pId == 'NotepadDetails_Clear')
    {
        return true;
    }
	
	else if(pId == 'NotepadDetailsFCU_Add')
    {
        return true;
    }	
	else if(pId == 'NotepadDetailsFCU_Modify')
    {
        return true;
    }
	else if(pId == 'NotepadDetailsFCU_Delete')
    {
        return true;
    }
	else if(pId == 'NotepadDetailsFCU_add')
    {
        return true;
    }
	else if(pId == 'NotepadDetailsFCU_Clear')
    {
        return true;
    }
	
	else if(pId == 'SmartCheck1_Add')
    {
        return true;
    }	
	else if(pId == 'SmartCheck1_Modify')
    {
        return true;
    }
	else if(pId == 'SmartCheck1_Delete')
    {
        return true;
    }
	
    else if(pId == 'WorldCheck1_Add')
    {
         if(checkMandatory(PL_WorldCheck))
		{ 
         	return true;
		}
        else
		{
            return false;
		}
    }
    else if(pId == 'WorldCheck1_Modify')
    {
        return true;
    }
    else if(pId == 'WorldCheck1_Delete')
    {
        return true;
    }
		
		else if(pId=='PartMatch_Save'){
			if(partMatch_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}		
		else if(pId=='MOL1_Save'){
			if(MOL_Save_Check())
				{
				removeFrame(pId);
				return true;}}
				
		else if(pId=='WorldCheck1_Save'){
			if(WorldCheck_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='RejectEnq_Save'){
			if(RejectEnq_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='SalaryEnq_Save'){
			if(SalaryEnq_Save_Check())
			{
				removeFrame(pId);
				return true;}
		}
		
		else if (pId == 'LoanDetails_Add')
		{
			if (checkMandatory(PL_LoanAdd()))
            return true;
		}//Arun (17/8/17)
	
		else if (pId == 'LoanDetails_Modify')
		{
            return true;
		}//Arun (17/8/17)
	
		else if (pId == 'LoanDetails_Delete')
		{
            return true;
		}//Arun (17/8/17)
	
		else if (pId == 'LoanDetails_Clear')
		{
            return true;
		}//Arun (17/8/17)
				
		/*else if(pId =='Loan_Disbursal' ){
			var activityName = window.parent.stractivityName;
			if(activityName=='Disbursal'){
			alert("activityName"+activityName);
			if(n>0){
					for(var i=0;i<n;i++){
						if(getLVWAT("cmplx_Product_cmplx_ProductGrid", i, 0)=='Islamic'){
							setVisible('Loan_Disbursal_Button2',true);
						}
						else{
							setVisible('Loan_Disbursal_Button2',false);
						}
					}
				}
			}	
			return true;
		}
      */
	  //added by akshay on 31/10/18
		else if(pId=='FinacleCore_save')
		{
			return true;
		}
		
		//code added here
		else if(pId=='Customer_ReadFromCard'){
		//alert("inside Customer_ReadFromCard button");
		//setEnabled("FetchDetails",true);
		return true;
		}
		
		else if(pId=='FetchDetails'){
		//alert("inside Fetch Details button");
		return true;
		}
		else if(pId=='Customer_Button1'){
			return true;
		}
		else if(pId=='cmplx_Customer_card_id_available')
        {    
			if(getNGValue('cmplx_Customer_card_id_available')==true){
					setEnabled("cmplx_Customer_CIFNO",true);
					}	
					
			else{
					    setEnabled("cmplx_Customer_CIFNO",false);
				}
           
        }
		
		
		else if(pId=='Liability_New_Overwrite')
		{
			var request_name = "";
				var param_json="";
				var url="";
				var prod = "";
				var subprod="";
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				var parentWiName = getNGValue('Parent_WIName');
				
			if(getNGValue('IS_AECB')=='Y')		
			{
			    
				request_name = 'ACCOUNT_SUMMARY';
				param_json=('{"cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNO')+'"}');
				url='/webdesktop/custom/CustomJSP/integration_AccountPL.jsp?request_name=' +  request_name + '&parentWiName=' + parentWiName + '&wi_name=' + wi_name + '&activityName=' +activityName +'&userName='+window.parent.userName+ '&param_json=' + param_json+'&sessionId='+window.parent.sessionId;
			
				var status=window.open_(url, "", "width=500,height=300");
				//setNGValue('Is_Overwrite_Details',status);
				//alert('After account summary: '+status);
				//alert('flag value'+getNGValue('Is_Overwrite_Details'));
				request_name = 'CARD_SUMMARY';			
				param_json=('{"cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNO')+'"}');
				url='/webdesktop/custom/CustomJSP/integration_CardPL.jsp?request_name=' + request_name + '&parentWiName=' + parentWiName + '&wi_name=' + wi_name + '&activityName=' +activityName +'&userName='+window.parent.userName+ '&param_json=' + param_json+'&sessionId='+window.parent.sessionId;
				status=window.open_(url, "", "width=500,height=300");
				
			//return true;
			}
			else				
			{
				showAlert('ExtLiability_Button1',alerts_String_Map['PL211']);
				return false;
			}
			
		}
		
		else if(pId=='IncomeDetails_Button1'){
			//alert("Inside Financial Summary");
			var request_name = "";
			var parentWiName = getNGValue('Parent_WIName');
			
			// below code done to find opertaion names of financial summary on 29th Dec by disha
			/*
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SAL' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'SALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SE' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='BTC' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='IM' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'RETURNDET,SALDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='LI' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'RETURNDET,SALDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='LI' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PA' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Self Employed'){
				request_name = 'SALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PA' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PU' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'SALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PU' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='EXP' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'RETURNDET,SALDET,LIENDET,SIDET';//'TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='Secured Card' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SALDET';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='Secured Card' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Self Employed'){
				request_name = 'RETURNDET,LIENDET,AVGBALDET,TRANSUM';
			}
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='NAT' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Salaried'){
				request_name = 'RETURNDET,SALDET';
			}
			*/

			//var param_json = ('{"cmplx_Customer_CIFNO":"' + getNGValue('cmplx_Customer_CIFNO') + '"}');
			
			var param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNO')+'","sub_product":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)+'","emp_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)+'"}');
				// above code done to find opertaion names of financial summary on 29th Dec by disha
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				//alert ('request_name '+ request_name);
				//alert ('activityName '+ activityName);

				var url ='/webdesktop/custom/CustomJSP/integration_PL.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name +'&activityName=' + activityName + '&parentWiName=' + parentWiName+'&userName='+window.parent.userName+'&sessionId='+window.parent.sessionId;
				//Deepak new code added to open single window start
				if(popup_win && !popup_win.closed){
					popup_win.focus();
				}
				else{
					popup_win = window.open_(url, "", "scrollbars=yes,resizable=yes,width=500px,height=700px"); //changed by aman to send the code to java
				}
				//Deepak new code added to open single window END
				//window.open_(url, "", "scrollbars=yes,resizable=yes,width=500px,height=700px");
				
				//change by saurabh on 3rd Dec for FSD 2.7
				setNGValue('Is_Financial_Summary','Y');
				return true;
		}
		//to check eligibility on the click of Check Eligibility button
		else if(pId=='Button30'){
			setValueCustomer_CheckEligibility();
			return true;
		}
		
		//code ended here
		else if(pId=='IncomingDoc_ViewSIgnature'){
			var wi_name = window.parent.pid;
			var activityName = window.parent.stractivityName;
			var url="/webdesktop/custom/CustomJSP/OpenImage.jsp?workstepName="+window.parent.stractivityName+"&signMatchNeededAtChecker=A"+"&debt_acc_num="+getNGValue('Account_Number')+"&wi_name="+wi_name+"&workstepName="+activityName+"&CifId="+getNGValue('cmplx_Customer_CIFNO');
			window.open_(url, "", "width=700,height=600");
		}	
		
		else if(pId=='IncomingDoc_UploadSig'){
			/*----Commented by akshay on 16/1/18
			var activityName = window.parent.stractivityName;
			var wi_name = window.parent.pid;
			var firstName = getNGValue('cmplx_Customer_FIrstNAme');
			var middleName = getNGValue('cmplx_Customer_MiddleName');
			var lastName = getNGValue('cmplx_Customer_LAstNAme');
			var fullName = firstName+" "+middleName+" "+lastName;
			
			//alert("fullName "+ fullName);
			
	 		var url = "/formviewer/CustomForms/RLOS/VerifySignature.jsp?CifId=" + getNGValue('cmplx_Customer_CIFNO') + "&AccountNo=" + getNGValue('Account_Number') +
	            		"&CustSeqNo=" + "001" + "&ItemIndex=" + getNGValue('AppRefNo') + "&CustomerName=" + fullName + "&docIndex=" + getNGValue('sig_docindex') +
	           		 "&WIName=" + wi_name + "&workstepName=" + activityName;*/
			uploadDocument();
			//window.open(url);
		}	
		
		/*else if(pId=='PartMatch_Search')
		{   
			/*var value=PLFRAGMENTLOADOPT['CustomerDetails']; 
			if (value=='' || value=='Y')
			{
				showAlert('CustomerDetails', alerts_String_Map['PL212']);
				return false;
			}
			if(checkMandatory_Partmatch())
			{
				setNGValue('cmplx_PartMatch_partmatch_flag','Y');
				return true;
			}
		}*/
	
		
		
		//for Blacklist Call
		else if(pId=='PartMatch_Blacklist')
		{
			/*Deepak comment for Testing need to be removed while giveing for SIT
			var partSearchPerform = getNGValue('Is_PartMatchSearch');
			if(partSearchPerform=="" || partSearchPerform =="N" || partSearchPerform == null)
			{
				showAlert('PartMatch_Search',alerts_String_Map['PL213']);
				return false;
			}*/
			return true;
		}
		//for Blacklist Call ended
		
		else if(pId=='DecisionHistory_Modify')
			return true;
			
		else if(pId=='PartMatch_Button1')	
		{
			var partSearchPerform = getNGValue('Is_PartMatchSearch');
			var activityName = window.parent.stractivityName;
			//alert("partSearchPerform "+ partSearchPerform);
			if(partSearchPerform=="" || partSearchPerform =="N")
			{
				showAlert('PartMatch_Search',alerts_String_Map['PL213']);
				return false;
			}
			else
			{
				//alert("cmplx_PartMatch_Is_NTB "+ getNGValue('cmplx_PartMatch_Is_NTB'));
				var SelectedRow = com.newgen.omniforms.formviewer.getNGListIndex("cmplx_PartMatch_cmplx_Partmatch_grid");
				
				var n=getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
				if(getNGValue('cmplx_PartMatch_Is_NTB')==false)
				{
					//alert("inside false");
					
					//alert('noRows -- '+ noRows);
					if(n==0)
					{
						showAlert('PartMatch_Button1',alerts_String_Map['PL213']);
						return false;
					}
					if(SelectedRow=='-1')
					{
						showAlert('PartMatch_Button1',alerts_String_Map['PL216']);
						return false;
					}
					//added by akshay on 31/3/18 for proc 7309
					else if(getLVWAT('cmplx_PartMatch_cmplx_Partmatch_grid',SelectedRow,0)==getNGValue('cmplx_Customer_CIFNo'))
					{
						showAlert(pId,alerts_String_Map['CC262']);
						return false;
					}
					
				}
				else
				{
					//alert('noRows12234 -- '+ noRows);
					if(n==0)
					{
						showAlert('PartMatch_Button1',alerts_String_Map['PL213']);
						return false;
					}
					if(activityName=='DDVT_maker')
					{
						//alert("inside true");
						setVisible("DecisionHistory_Button2", true);
						setNGValue("cmplx_Customer_CIFNO","");		
						setNGValue("cmplx_Customer_FIrstNAme","");		
						setNGValue("cmplx_Customer_LAstNAme","");		
						setNGValue("cmplx_Customer_Passport2","");		
						setNGValue("cmplx_Customer_VisaNo","");		
						setNGValue("cmplx_Customer_DLNo","");		
						setNGValue("cmplx_Customer_Nationality","");		
						setNGValue("cmplx_Customer_EmiratesID","");		
						setNGValue("IBAN_Number","");		
						setNGValue("Account_Number","");		
						showAlert('PartMatch_Button1',alerts_String_Map['PL218']);	
					}					
					
				}
				
			}
		var selected_row=com.newgen.omniforms.formviewer.getNGListIndex("cmplx_PartMatch_cmplx_Partmatch_grid");
			 var row_count=getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');
				if(row_count>0)
				{
				for(var i=0;i<row_count;i++)
				{
				if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)== getLVWAT('cmplx_PartMatch_cmplx_Partmatch_grid',selected_row,0))
				{
				
					showAlert('PartMatch_Button1',alerts_String_Map['PL389']);
					return false;
				}
				}					
					
				}
			return true;			
		}
		
		
		else if(pId=='cmplx_Decision_waiveoffver')
	{
		if(getNGValue('cmplx_Decision_waiveoffver')=='' || getNGValue('cmplx_Decision_waiveoffver')==null || getNGValue('cmplx_Decision_waiveoffver')==false )
		{				
			setNGValue('cmplx_Decision_VERIFICATIONREQUIRED','Yes');
			setNGValue('cpv_required','Y');
		}
		else
		{
			setNGValue('cmplx_Decision_VERIFICATIONREQUIRED','No');
			setNGValue('cpv_required','N');
		}
	}
		
		//commented by nikhil wrong validation
		/*else if(pId=='AddressDetails_addr_Add' || pId=='AddressDetails_addr_Modify' || pId=='AddressDetails_addr_Delete'){
			return true;
		}*/
		
		//added by akshay on 16/2/18
		else if(pId=='DecisionHistory_CifLock'){
			if(getNGValue('Is_CustLock')=='Y'){
				showAlert('',alerts_String_Map['PL391']);
				return false;
			}
			return true;	
		}
		
		else if(pId=='DecisionHistory_CifUnlock'){
			if(getNGValue('Is_CustLock')=='N' || getNGValue('Is_CustLock')==''){
				showAlert('',alerts_String_Map['PL392']);
				return false;
			}
			return true;
		}
		
		else if(pId=='DecisionHistory_Button2')
		{
			if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',getNGListIndex('cmplx_Decision_MultipleApplicantsGrid'),0)=='Primary'){
				if(checkMandatory_CreateCIF('PRIMARY',pId)){
					return true;
			}
			}
			else if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',getNGListIndex('cmplx_Decision_MultipleApplicantsGrid'),0)=='Guarantor'){
			
				if(checkMandatory_CreateCIF('GUARANTOR',pId)){
					return true;
			}
			}
			else if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',getNGListIndex('cmplx_Decision_MultipleApplicantsGrid'),0)=='Supplement'){
				if(checkMandatory_CreateCIF('SUPPLEMENT',pId)){
					return true;
			}
			}
				return false;
			}
			
		else if(pId=='cmplx_Liability_New_overrideAECB')
		{
			var overrideAECB= getNGValue('cmplx_Liability_New_overrideAECB');
			if(overrideAECB==true)
			{
				alert('Do you want to Re-run the Liabilities');
				return true;
			}
		}
		else if(pId=='cmplx_EmploymentDetails_Others'){
		var activityName = window.parent.stractivityName;
		if(activityName=='CSM'){
			var OthersCSM=getNGValue("cmplx_EmploymentDetails_Others");
			if(OthersCSM==true){
				setLocked("cmplx_EmploymentDetails_EmpName", false);
				setLocked("cmplx_EmploymentDetails_EMpCode", false);
				setLocked("cmplx_EmploymentDetails_FreezoneName", false);
				setLocked("cmplx_EmploymentDetails_Freezone", false);
				setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
				setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);
				setLocked("cmplx_EmploymentDetails_Emp_Categ_PL", false);
				setLocked("cmplx_EmploymentDetails_EmpIndusSector", false);
				setLocked("cmplx_EmploymentDetails_Indus_Macro", false);
				setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
				setLocked("cmplx_EmploymentDetails_IncInCC", false);
				setLocked("cmplx_EmploymentDetails_IncInPL", false);
				setLocked("cmplx_EmploymentDetails_Status_PLNational", false);
				setLocked("cmplx_EmploymentDetails_Status_PLExpact", false);
				setLocked("cmplx_EmploymentDetails_ownername", false);
				setLocked("cmplx_EmploymentDetails_NOB", false);
				setLocked("cmplx_EmploymentDetails_LOS", false);
				setLocked("EMploymentDetails_Combo34", false);
				setLocked("cmplx_EmploymentDetails_authsigname", false);
				setLocked("cmplx_EmploymentDetails_highdelinq", false);
				setLocked("cmplx_EmploymentDetails_dateinPL", false);
				setLocked("cmplx_EmploymentDetails_dateinCC", false);
				setLocked("cmplx_EmploymentDetails_remarks", false);
				setLocked("cmplx_EmploymentDetails_Aloc_RemarksPL", false);
			}
			else{
				setLocked("cmplx_EmploymentDetails_EmpName", true);
				setLocked("cmplx_EmploymentDetails_EMpCode", true);
				setLocked("cmplx_EmploymentDetails_FreezoneName", true);
				setLocked("cmplx_EmploymentDetails_Freezone", true);
				setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
				setLocked("cmplx_EmploymentDetails_EmpStatusCC", true);
				setLocked("cmplx_EmploymentDetails_Emp_Categ_PL", true);
				setLocked("cmplx_EmploymentDetails_EmpIndusSector", true);
				setLocked("cmplx_EmploymentDetails_Indus_Macro", true);
				setLocked("cmplx_EmploymentDetails_Indus_Micro", true);
				setLocked("cmplx_EmploymentDetails_IncInCC", true);
				setLocked("cmplx_EmploymentDetails_IncInPL", true);
				setLocked("cmplx_EmploymentDetails_Status_PLNational", true);
				setLocked("cmplx_EmploymentDetails_Status_PLExpact", true);
				setLocked("cmplx_EmploymentDetails_ownername", true);
				setLocked("cmplx_EmploymentDetails_NOB", true);
				setLocked("cmplx_EmploymentDetails_LOS", true);
				setLocked("EMploymentDetails_Combo34", true);
				setLocked("cmplx_EmploymentDetails_authsigname", true);
				setLocked("cmplx_EmploymentDetails_highdelinq", true);
				setLocked("cmplx_EmploymentDetails_dateinPL", true);
				setLocked("cmplx_EmploymentDetails_dateinCC", true);
				setLocked("cmplx_EmploymentDetails_remarks", true);
				setLocked("cmplx_EmploymentDetails_Aloc_RemarksPL", true);
			}
			if(setALOCListed())
				return true;
		}
	}
	
		else if(pId=='cmplx_EmploymentDetails_Freezone')
	{
		var freezone= getNGValue('cmplx_EmploymentDetails_Freezone');
		if(freezone==true)
		{
			setLocked('cmplx_EmploymentDetails_FreezoneName',false);
		}
		else
		{
			setLocked('cmplx_EmploymentDetails_FreezoneName',true);
		}
	}
		//++below code added by abhishek for CSM point 8 on 7/11/2017
		else if(pId=='ExtLiability_CACIndicator')
		{
			var CACIndicator= getNGValue('ExtLiability_CACIndicator');
			if(CACIndicator==true)
			{
				setEnabled("ExtLiability_QCAmt", true);
				setLocked("ExtLiability_QCAmt", false);
				setEnabled("ExtLiability_QCEMI", true);
				setLocked("ExtLiability_QCEMI", false);
			}
			else
			{
				setEnabled("ExtLiability_QCAmt", false);
				setLocked("ExtLiability_QCAmt", true);
				setEnabled("ExtLiability_QCEMI", false);
				setLocked("ExtLiability_QCEMI", true);
				document.getElementById("ExtLiability_QCAmt").value = "";
			document.getElementById("ExtLiability_QCEMI").value = "";
			}
			//return true;---commented by akshay on 24/1/18
		}
		//++above code added by abhishek for CSM point 8 on 7/11/2017
			//++below code added by abhishek for CSM point 9 on 7/11/2017
		else if(pId=='ExtLiability_Takeoverindicator')
		{
			var TakeOverIndicator= getNGValue('ExtLiability_Takeoverindicator');
			if(TakeOverIndicator==true)
			{
				setEnabled("ExtLiability_takeoverAMount", true);
				setNGValue("ExtLiability_Consdierforobligations", false);
				
			}
			else
			{
				
				setNGValue("ExtLiability_Consdierforobligations", true);
				setEnabled("ExtLiability_takeoverAMount", false);
			document.getElementById("ExtLiability_takeoverAMount").value = "";
			}
		
		}
		//++above code added by abhishek for CSM point 9 on 7/11/2017
		else if (pId=='FinacleCore_Button1')
		{
			if(getNGValue('FinacleCore_accno')=='')
			{
				showAlert('FinacleCore_accno','Account NO Should Not Be blank');
						return false;
			}
			if(getNGValue('FinacleCore_Text1')=='')
			{
				showAlert('FinacleCore_accno',alerts_String_Map['PL224']);
						return false;
			}
			if(getNGValue('FinacleCore_cheqtype')=='' || getNGValue('FinacleCore_cheqtype')=='--Select--')
			{
				showAlert('FinacleCore_cheqtype',alerts_String_Map['PL225']);
						return false;
			}
			if(getNGValue('FinacleCore_cheqamt')=='')
			{
				showAlert('FinacleCore_cheqamt',alerts_String_Map['PL226']);
						return false;
			}
			return true;
		}
		/*else if (pId=='FinacleCore_Button1')
		{
			return true;
		}*/
		else if (pId=='FinacleCore_Button3')
		{
			return true;
		}
		else if (pId=='FinacleCore_Button4')
		{
			if(getNGValue('FinacleCore_Text10')=='')
			{
				showAlert('FinacleCore_Text10',alerts_String_Map['PL224']);
						return false;
			}
			if(getNGValue('FinacleCore_Combo1')=='' || getNGValue('FinacleCore_Combo1')=='--Select--')
			{
				showAlert('FinacleCore_Combo1',alerts_String_Map['PL227']);
						return false;
			}
			if(getNGValue('FinacleCore_Text11')=='')
			{
				showAlert('FinacleCore_Text11',alerts_String_Map['PL228']);
						return false;
			}
			return true;
		}
		else if (pId=='FinacleCore_Button5')
		{
			return true;
		}
		else if (pId=='FinacleCore_Button6')
		{
			return true;
		}
		else if (pId=='FinacleCore_Button7')
		{
			return true;
		}
		else if (pId=='ReferenceDetails_Reference_Add')
		{
			if(checkMandatory_ReferenceGrid())//Arun (09/10)
			return true;
		}
		else if (pId=='ReferenceDetails_Reference__modify')
		{
			if(checkMandatory_ReferenceGrid())//Arun (09/10)
			return true;
		}
		else if (pId=='ReferenceDetails_Reference_delete')
		{
			return true;
		}
		
				//ADDED BY YASH FOR TEMPLATE GENERATION	
		else if (pId=='DecisionHistory_Button4')
			{
			//change by saurabh on 7th Dec
				setNGValue('Is_CAM_generated','Y');
				// code commented by disha on 11-Sep-2018
				//changes done by disha for new generate template socket on 11-Sep-2018
				/*showAlert('DecisionHistory_Button2','Module disabled');
				return false;
				window.parent.executeAction('1', 'Y');
				return true;
				*/
				var wi_name = window.parent.pid;
				var docName = "CAM_REPORT_SALARIED_PL";
				var attrbList = "";		
				attrbList += PLTemplateData(); 	
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
		//added by Tarang For welcome letter on 12/04/2018 
		else if (pId=='DecisionHistory_Button1')
		{
			setNGValue('IS_Welcome_Letter_Generated','Y');
			window.parent.executeAction('1', 'Y');
			return true;
		 }
		 //added below by Tarang For welcome letter on 12/04/2018 
		 else if (pId=='PostDisbursal_Gen_Covering_Letter')
			{
				window.parent.executeAction('2', 'Y');
				return true;
			 }
			 else if (pId=='PostDisbursal_Gen_Bank_Guarantee')
			{
				window.parent.executeAction('1', 'Y');
				return true;
			 }
			 //ended by yash for template generation pl_Wi_Name
			 
			 //Code added for AECB REPORT link
			//changed by akshay on 11/9/18 
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
		else if(pId=='EMploymentDetails_Button1')
		{
			//change by saurabh on 14th Dec
			if(getNGValue('Is_Financial_Summary')!='Y'){
				if(getNGValue('cmplx_Customer_NTB')==false){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
				}
				else if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
			}
			else if(getNGValue('IS_Employment_Saved')!='Y' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='SE'){
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}
			return true;
		}
		if(pId=='DecisionHistory_newStrengthAdd'){
            if(getNGValue('DecisionHistory_NewStrength')!=null && getNGValue('DecisionHistory_NewStrength')!='' && getNGValue('DecisionHistory_NewStrength')!= ' '){
                var strengths = getNGValue('cmplx_Decision_strength');
                var newStrength = getNGValue('DecisionHistory_NewStrength');
                var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
                setNGValue('cmplx_Decision_strength',strengths+'\n'+(userid+'-'+datetime+'-'+newStrength));
                setNGValue('DecisionHistory_NewStrength','');
            }
        }
        if(pId=='DecisionHistory_newWeaknessAdd'){
            if(getNGValue('DecisionHistory_NewWeakness')!=null && getNGValue('DecisionHistory_NewWeakness')!='' && getNGValue('DecisionHistory_NewWeakness')!= ' '){
                var strengths = getNGValue('cmplx_Decision_weakness');
                var newStrength = getNGValue('DecisionHistory_NewWeakness');
                var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
                setNGValue('cmplx_Decision_weakness',strengths+'\n'+(userid+'-'+datetime+'-'+newStrength));
                setNGValue('DecisionHistory_NewWeakness','');
            }
        }
		//Added below code by Tarang as per drop 4 Takeover on 06/04/2018
		else if(pId=='PostDisbursal_LMSDocumentUpdate')
		{
			return true;
		}
		//Added above code by Tarang as per drop 4 Takeover on 06/04/2018
		//++Below code added by nikhil for ToTeam
		
		else if(pId=='FinacleCRMCustInfo_save')
		{
			return true;
		}
		
		else if(pId=='PostDisbursal_LC_Add')
		{
			if(CheckConditionsLC())
			{
			return true;
			}
		}
		else if(pId=='PostDisbursal_MCQ_Add')
		{
			if(CheckConditionsMCQ())
			{
			return true;
			}
		}
		else if(pId=='PostDisbursal_BG_Add')
		{
			if(CheckConditionsBG())
			{
			return true;
			}
		}
		else if(pId=='PostDisbursal_NLC_Add')
		{
			if(CheckConditionsNLC())
			{
			return true;
			}
		}
		else if(pId=='PostDisbursal_Save')
		{
			if(CheckConditionsSTL())
			{
			return true;
			}
		}
		else if(pId=='PostDisbursal_LC_Modify' || pId=='PostDisbursal_LC_Delete' || pId=='PostDisbursal_MCQ_Modify' || pId=='PostDisbursal_MCQ_Delete' || pId=='PostDisbursal_BG_Modify' || pId=='PostDisbursal_BG_Delete' || pId=='PostDisbursal_NLC_Modify' || pId=='PostDisbursal_NLC_Delete')
		{
			return true;
		}
		
		//--Above code added by nikhil for ToTeam
		
		//++Below code added by abhishek for disbursal
		//modified by akshay on 8/3/18 for drop 4
		else if(pId=='DecisionHistory_updcust_loan')
		{
			if(getNGListIndex('cmplx_Decision_MultipleApplicantsGrid')==-1)
			{
				showAlert('','Please select Customer Type from Applicants Grid in Decision tab!!!');
				return false;
			}
			return true;
		}
		else if(pId=='DecisionHistory_updacct_loan')
		{
			return true;
		}
		else if(pId=='DecisionHistory_chqbook_loan')
		{
			return true;
		}
		
		//modified by akshay on 23-2-18
		else if(pId=='cmplx_ReferHistory_cmplx_GR_ReferHistory'){
		
			if( (getLVWAT(pId,getNGListIndex(pId),5)==activityName || (getLVWAT(pId,getNGListIndex(pId),5)=='Source' && activityName=='DSA_CSO_Review') || (getLVWAT(pId,getNGListIndex(pId),5)=='CSO (for documents)' && activityName=='DSA_CSO_Review') || (getLVWAT(pId,getNGListIndex(pId),5)=='Smart CPV' && activityName=='Smart_CPV') || (getLVWAT(pId,getNGListIndex(pId),5)=='CPV Checker' && activityName=='CPV_Analyst')))
			{
				setLocked('ReferHistory_status',false);
				setLocked('ReferHistory_Modify',false);
				return true;
			}
			else{	
				setLocked('ReferHistory_status',true);
				setLocked('ReferHistory_Modify',true);
				return false;
			}
		}
		//ended by akshay on 9/12/17

		//--Above code added by abhishek for disbursal
		
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		else if(pId=='ELigibiltyAndProductInfo_Refresh')
		{
			return true;
		}
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

		if(pId=='cmplx_CardDetails_securitycheck'){
			if(getNGValue("cmplx_CardDetails_securitycheck")==true){
				setEnabled('CardDetails_bankName_Button',true);//changes by nikhil 24/10
				setLocked('CardDetails_ChequeNumber',false);
				setLocked('CardDetails_Amount',false);
				setLocked('CardDetails_Date',false);
				
			}
			else{
				setNGValue('CardDetails_BankName','');
				setNGValue('CardDetails_ChequeNumber','');
				setNGValue('CardDetails_Amount','');
				setNGValue('CardDetails_Date','');
				setEnabled('CardDetails_bankName_Button',false);//changes by nikhil 24/10
				setLocked('CardDetails_ChequeNumber',true);
				setLocked('CardDetails_Amount',true);
				setLocked('CardDetails_Date',true);
			}
	}
	
		//below code added by akshay on 15/2/18
		else if(pId=='cmplx_Decision_MultipleApplicantsGrid')
		{
			var selectedrow=getNGListIndex(pId);
			if(selectedrow!=-1){
				if(activityName=='DDVT_Checker' ){
					if(getLVWAT(pId,selectedrow,0)=='Primary'){
					
											
						if(getNGValue("AlternateContactDetails_RetainAccIfLoanReq")==true){
						setVisible("DecisionHistory_Button3",true);	
							setVisible("DecisionHistory_updcust",true);
							if(getNGValue('Is_ACCOUNT_MAINTENANCE_REQ')=='Y'){
								setVisible("DecisionHistory_chqbook",true);
							}
							else{
							setVisible("DecisionHistory_chqbook",false);
							}
								
						}
						else
						{
						setVisible("DecisionHistory_Button3",false);	
							setVisible("DecisionHistory_updcust",false);
							setVisible("DecisionHistory_chqbook",false);
						}
					}
					
					else if(getLVWAT(pId,selectedrow,0)=='Supplement'){
						setVisible('DecisionHistory_Button3',false);
						setVisible("DecisionHistory_updcust",false);
						setVisible("DecisionHistory_chqbook",false);
						if(getLVWAT(pId,selectedrow,3)==''){
							setVisible('DecisionHistory_Button2',true);
						}
						else{
							setVisible('DecisionHistory_Button2',false);
						}
					}
					else if(getLVWAT(pId,selectedrow,0)=='Guarantor'){
						setVisible('DecisionHistory_Button3',true);
					}
				}
				else if(activityName=='Reject_Queue' ){
					setVisible('DecisionHistory_Button3',true);
				}			

			}
			else{
					setVisible('DecisionHistory_Button3',false);
					setVisible("DecisionHistory_updcust",false);
					setVisible("DecisionHistory_chqbook",false);
				}
		}	
	return false;
}


function changeCase(controlName)
{
 	switch(controlName)
	{	
		case 'cmplx_Decision1_Decision':
		
		//var dec=com.newgen.omniforms.formviewer.getNGValue(controlName);
		
		//com.newgen.omniforms.formviewer.setNGValue('Decision',dec);
			return true;
		
	}
//	alert("dec : "+dec);
	return true;
}
function setData(textarea)
{
	setNGValue('Text12',textarea);
	//alert(textarea);
	return true;
}
function clickCase(controlName)
{
	return true;
	
	
}	
function GetErroDesc(session_id)
{

	var xhr;
	var ajaxResult;
	ajaxResult="";
	var url ='/formviewer/resources/scripts/CC/akshay.jsp?sessionId='+session_id+"";
							window.open_(url,"_blank","width=400,height=300");
							
							

}

function doNotLoadFragmentSecTime(pId){
	PLFRAGMENTLOADOPT[pId]='N';
	}
	


//for document related tab
function addDocFromOD(){
window.parent.openAddDocWin(); //workdesk.js
}
function addDocFromPC(){
window.parent.openImportDocWin(); //workdesk.js
}
function openScannerWindow(){
window.parent.scanDoc(); //workdesk.js
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
		
		//var rowNumber=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('RowNum').value;
        //var docIndex_Id="IncomingDoc_Frame_Reprow"+rowNumber+"_Repcolumn11";
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
//ended here

function change_PersonalLoanS(pId)
{
	// disha FSD
	if(getNGValue(pId) != PLValuesData[pId]){
			var value = document.getElementById(document.getElementById(pId).parentNode.id).parentNode.id;
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(value)==-1){
			hiddenStringPL = value+',';
			//document.getElementById(pId).style.setProperty ("background", "#ffe0bd", "important");
			var previousValue = getNGValue('FrameName');
			hiddenStringPL = previousValue + hiddenStringPL;
			setNGValue('FrameName',hiddenStringPL);
			}
		}
	
	if(pId=="NotepadDetails_notedesc")
	{
		return true;
	}
	//changed by saurabh on 23rd Oct 17.
	var activityName = window.parent.stractivityName;
	if(activityName == 'CSM' || activityName== 'DDVT_Maker' || activityName== 'DDVT_maker'){
	if(rlosValuesData[pId]){
		if(getNGValue(pId) != rlosValuesData[pId]){
			var field_string_value = getNGValue('fields_string');
			if(field_string_value.indexOf(pId)==-1){
			hiddenString = pId+',';
			//document.getElementById(pId).style.setProperty ("background", "#ffe0bd", "important");
			var previousValue = getNGValue('fields_string');
			hiddenString = previousValue + hiddenString;
			setNGValue('fields_string',hiddenString);
			}
		}
		//console.log(hiddenString);
	}
	}
	// disha FSD winame added
	var activityName = window.parent.stractivityName;


// disha FSD - to disable fetch detail button integration point at CSM workstep
	if(activityName!='CSM' && activityName!='SalesCoordinator')
	{
		if (pId =='cmplx_Customer_EmiratesID' || pId =='cmplx_Customer_FIrstNAme' || pId =='cmplx_Customer_MiddleName' || pId =='cmplx_Customer_LAstNAme' || pId =='cmplx_Customer_DOb' || pId =='cmplx_Customer_Nationality' || pId =='cmplx_Customer_MobNo' || pId =='cmplx_Customer_PAssportNo'||pId =='cmplx_Customer_CIFNO' )	
		{		
			setEnabled('FetchDetails',true);
		}
	}
    if (pId == "cmplx_Customer_NEP" || pId == "cmplx_Customer_NTB")
    {
        if (getNGValue("cmplx_Customer_NTB") == false && getNGValue("cmplx_Customer_NEP") == '')
        {
            setVisible("IncomingDoc_ViewSIgnature", true);
            setVisible("IncomingDoc_UploadSig", false);
        }

        else if (getNGValue("cmplx_Customer_NEP") != '' || getNGValue("cmplx_Customer_NTB") == true)
            setVisible("IncomingDoc_ViewSIgnature", false);
        setVisible("IncomingDoc_UploadSig", true);
    }
	//below code added bysaurabh
	
	
	if(pId=='cmplx_Customer_Nationality')
	{
		var GCC="BH,IQ,KW,OM,QA,SA,AE";
		if(GCC.indexOf(getNGValue("cmplx_Customer_Nationality"))!=-1)
		{
			setNGValue('cmplx_Customer_GCCNational','Y');
		}
		else
		{
			setNGValue('cmplx_Customer_GCCNational','N');
		}
	}
	
	else if (pId =='cmplx_Customer_EmiratesID' || pId =='cmplx_Customer_FIrstNAme' || pId =='cmplx_Customer_MiddleName' || pId =='cmplx_Customer_LAstNAme' || pId =='cmplx_Customer_DOb' || pId =='cmplx_Customer_Nationality' || pId =='cmplx_Customer_MobNo' || pId =='cmplx_Customer_PAssportNo'||pId =='cmplx_Customer_CIFNO')	
	{
		if(activityName!='CSM' && activityName!='SalesCoordinator')
	{
		
		setEnabled('FetchDetails',true);
	}
	}
	else if (pId =='cmplx_Customer_CIFNo'){ 
	
	    var partCIF = getNGValue('cmplx_Customer_CIFNO');
		if(partCIF !="" && partCIF.length!=7)
		{
									showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL168']);
									return false;
		}
							
		}
	
	if(pId=='SupplementCardDetails_PassportIssueDate')
		validateFutureDate(pId);
	
	if(pId=='SupplementCardDetails_VisaIssueDate')
		validateFutureDate(pId);
	
	if(pId=='SupplementCardDetails_idIssueDate' || pId=='FinacleCore_doc'){
		validateFutureDate(pId);
	}
	if(pId=='SupplementCardDetails_VisaExpiry')
	{
		validatePastDate(pId,'Visa Expiry');
	}
	if(pId=='SupplementCardDetails_PassportExpiry')
	{
		validatePastDate(pId,'Passport Expiry');
	}
	if(pId=='SupplementCardDetails_EmiratesIDExpiry')
	{
		validatePastDate(pId,'Emirates Expiry');
	}
	//Changes by aman for todo list
	if(pId=='cmplx_EmploymentVerification_FiledVisitedInitiated_value')
	{
		return true;
	}
	//Changes by aman for todo list
	else if( pId=='cmplx_EmploymentDetails_LOSPrevious' ){
		//YearsinUAE(pId);
		if(getNGValue(pId)==''){
			setNGValue(pId,'00.00');
		}
		YYMM(pId,'','');
		//below validation added by aman for drop 5
		var YY=getNGValue(pId).split('.')[0];
		var MM=getNGValue(pId).split('.')[1];
		
		if(YY.length>2 || parseInt(MM)>11)
		{
			showAlert(pId,alerts_String_Map['VAL256']);
			setNGValue(pId,'00.00');
		}
	}
	
	if(pId=='cmplx_Customer_DOb')
	{
		if(validateFutureDate(pId))
		{
			var age=calcAge(getNGValue(pId),'');
			YYMM('','cmplx_Customer_age',age);
			
		}
		//below added by nikhil as per ddvt issues 6/12/17
		if(parseFloat(getNGValue('cmplx_Customer_age'))>18.0)
		{
			setVisible('cmplx_Customer_minor',false);
		}
		else{
			setVisible('cmplx_Customer_minor',true);
		}
	}
	
	if(pId=='GuarantorDetails_dob')
	{
		if(validateFutureDate(pId))
		{
			var age=calcAge(getNGValue(pId),'');
			YYMM('','GuarantorDetails_age',age);
			
		}
	}
	
	if(pId=='cmplx_CustDetailVerification_dob_upd')
		validateFutureDate(pId);
	
	if(pId=='cmplx_OffVerification_doj_upd')
		validateFutureDate(pId);
	//above arun 12/12/17 for CPv validation

	//below code added by nikhil 11/12/17
	else if(pId=='cmplx_LoanDetails_moratorium')
	{
	if (getNGValue('cmplx_LoanDetails_moratorium') !=''){
	
		var firstRepayDate=Calc_FirstRepaymentDate(getNGValue(pId));
		setNGValue('cmplx_LoanDetails_frepdate',firstRepayDate);
		setNGValue('LoanDetails_duedate',firstRepayDate);
		var tenor=getNGValue('cmplx_LoanDetails_tenor');
		var maturityDate=MaturityDate(firstRepayDate,tenor);
		setNGValue('cmplx_LoanDetails_maturitydate',maturityDate);
		var ageAtMaturity=calcAgeAtMaturity(maturityDate);
		setNGValue('cmplx_LoanDetails_ageatmaturity',ageAtMaturity);
	}
	}
	
		else if(pId=='cmplx_LoanDetails_fdisbdate')
		{
		setNGValue('cmplx_LoanDetails_paidon',getNGValue('cmplx_LoanDetails_fdisbdate'));
		}
	else if(pId=='cmplx_LoanDetails_paymode')
	{
		if(getNGValue('cmplx_LoanDetails_paymode')=='Q')
		{
			setLocked('cmplx_LoanDetails_chqdat',false);
			setLocked('cmplx_LoanDetails_chqno',false);
			//showAlert('cmplx_LoanDetails_chqno',alerts_String_Map['PL344']);
			//showAlert('cmplx_LoanDetails_chqdat',alerts_String_Map['PL345']);			
			//showAlert('cmplx_LoanDetails_drawnon',alerts_String_Map['PL346']);
			//showAlert('cmplx_LoanDetails_city',alerts_String_Map['PL347']);
			//showAlert('cmplx_LoanDetails_reason',alerts_String_Map['PL348']);
			//showAlert('cmplx_LoanDetails_micr',alerts_String_Map['PL349']);
			//return true;
		}
		else{
			setLocked('cmplx_LoanDetails_chqno',true);
			setLocked('cmplx_LoanDetails_chqdat',true);
			setNGValue('cmplx_LoanDetails_chqdat','');
			//return true;
		}
	}//Arun (22/09/17)
	else if(pId=='cmplx_LoanDetails_insur'){
		var insperc = getNGValue(pId);
		var loanAmt = getNGValue('cmplx_LoanDetails_lonamt');
		var insurVatperc = getNGValue('cmplx_LoanDetails_InsuranceVATPercent');
		if(loanAmt!='' && insperc!=''){
			if(com.newgen.omniforms.formviewer.isLocked('cmplx_LoanDetails_insuramt')){
			setLocked('cmplx_LoanDetails_insuramt',false);
			setNGValue('cmplx_LoanDetails_insuramt',((parseFloat(loanAmt)*parseFloat(insperc))/100));
			setLocked('cmplx_LoanDetails_insuramt',true);
			}
			else{
			setNGValue('cmplx_LoanDetails_insuramt',((parseFloat(loanAmt)*parseFloat(insperc))/100));
			}
		}
		if(getNGValue('cmplx_LoanDetails_insuramt')!='' && insurVatperc!=''){
			if(com.newgen.omniforms.formviewer.isLocked('cmplx_LoanDetails_InsuranceVat')){
			setLocked('cmplx_LoanDetails_InsuranceVat',false);
			setNGValue('cmplx_LoanDetails_InsuranceVat',((parseFloat(getNGValue('cmplx_LoanDetails_insuramt'))*parseFloat(insurVatperc))/100));
			setLocked('cmplx_LoanDetails_InsuranceVat',true);
			}
			else{
			setNGValue('cmplx_LoanDetails_InsuranceVat',((parseFloat(loanAmt)*parseFloat(insperc))/100));
			}
		}
		
	}
	 if(pId=='cmplx_Customer_VIsaExpiry')
	{
		validatePastDate(pId,'Visa Expiry');
		if(isFieldFilled('cmplx_Customer_VisaNo')==false){
			showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL351']);
			setNGValue(pId,'');
			return false;
		}
	}

	else if(pId=='cmplx_Customer_VisaIssuedate'){
		validateFutureDate(pId,"");	
		if(isFieldFilled('cmplx_Customer_VisaNo')==false){
			showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL351']);
			setNGValue(pId,'');
			return false;
		}
	}	
	else if(pId=='GuarantorDetails_eid'){
		var Id = getNGValue('GuarantorDetails_eid');
			var Dob=getNGValue('GuarantorDetails_dob');
			if((Id!="")&&((Id.length != 15) ||(Id.substr(0,3)!='784'))){
				if(Id.substr(0,3)!='784'){
					showAlert('GuarantorDetails_eid',alerts_String_Map['VAL108']);
					setNGValue(pId,'');
				}
				else{
					showAlert('GuarantorDetails_eid',alerts_String_Map['VAL108']);
					setNGValue(pId,'');
				}
				return false;
			}
			
			else if(Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9))
			{
				showAlert('GuarantorDetails_eid',alerts_String_Map['VAL074']);
				setNGValue(pId,'');
				return false;
			} //Arun 25/12/17
		
	}
	else if(pId=='cmplx_Customer_EmirateIDExpiry')
	{
		validatePastDate(pId,'Emirate ID Expiry');
		if(isFieldFilled('cmplx_Customer_EmiratesID')==false){
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL353']);
			setNGValue(pId,'');
			return false;
		}
	}
	//case added by saurabh on 20th Dec
	else if(pId=='cmplx_IncomeDetails_DurationOfBanking'){
		var txt=getNGValue(pId);
			  var re1='^\d{1,3}\.(0?[1-9]|1[012])$';    
			  var p = new RegExp(re1,["i"]);
			  var m = p.exec(txt);
			if(m != null) {
				showAlert(pId,'Please enter correct format(YY.MM)');
				setNGValue(pId,'');
				return false;
			}
	
	}
	// ++ below code already present - 09-10-2017
	 else if(pId=='cmplx_LoanDetails_frepdate')
	{
		  if(validatePastDate(pId,'First Repayment')){
		  var d= new Date();
				var today = d.getDate()+'/'+d.getMonth()+'/'+d.getFullYear();
				var moratorium = Moratorium(pId);
				//var months = (parseInt(moratorium.split('.')[0])*12)+parseInt(moratorium.split('.')[1])-1;
				setNGValue('cmplx_LoanDetails_moratorium',moratorium);
		var firstRepayDate=getNGValue(pId);
		//setNGValue('cmplx_LoanDetails_frepdate',firstRepayDate);
	var tenor=getNGValue('cmplx_LoanDetails_tenor');
		 setNGValue('cmplx_LoanDetails_maturitydate',MaturityDate(firstRepayDate,tenor));
		var ageAtMaturity=calcAge(getNGValue('cmplx_Customer_DOb'),MaturityDate(firstRepayDate,tenor));
		setNGValue('cmplx_LoanDetails_ageatmaturity',ageAtMaturity);
		  }
	}
	// ++ above code already present - 09-10-2017
	else if(pId=='cmplx_Customer_PassPortExpiry')
	{
		validatePastDate(pId,'Passport Expiry');
		if(isFieldFilled('cmplx_Customer_PAssportNo')==false){
			showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL352']);
			setNGValue(pId,'');
			return false;
		}
	}
else if(pId=='LoanDetails_loanamt')
{
setNGValue('LoanDetails_amt',getNGValue('LoanDetails_loanamt'));
}	
	else if(pId=='cmplx_OffVerification_Decision')
	{
	if(getNGValue('cmplx_OffVerification_Decision')=='Negative')
		setNGValue('employ_detail_match','No');
		else
		setNGValue('employ_detail_match','Yes');
	}
	
	//++below code added by nikhil for toteam
	else if(pId=='PostDisbursal_Expiry_Date')
	{
		validatePastDate(pId,'Expiry Date');
	}
	//--above code added by nikhil for toteam
	else if(pId=='cmplx_Customer_PassportIssueDate'){
		validateFutureDate(pId);	
		if(isFieldFilled('cmplx_Customer_PAssportNo')==false){
			showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL352']);
			setNGValue(pId,'');
			return false;
		}
	}
	else if(pId=='cmplx_CustDetailverification1_Dob_update')
	{
	validateFutureDate(pId);
	}
	
	else if(pId=='cmplx_Customer_yearsInUAE')
	{
		YearsinUAE(pId);
	}
	else if(pId=='Product_type')
	{
		var prodType=getNGValue(pId);
		if(prodType=='Islamic')
			setVisible("Product_DatePicker2", true);
		return true;
	}
	
	else if(pId=='ref_mobile')
	{
		var Mobile_no=getNGValue('ref_mobile');
		var mob_No="00971"+Mobile_no;
		setNGValue('ref_mobile',mob_No);
		//setNGValue('OTP_Mobile_NO',mob_No);
		return true;
	}

	else if(pId=='ReqProd')
	{
		var ReqProd=getNGValue('ReqProd');
		setFieldsVisible(ReqProd);
		return true;
	}
	
	else if(pId=='LimitExpiryDate')
	{
		validatePastDate(pId,'Limit Expiry');
	}
	else if(pId=='cmplx_Customer_IdIssueDate')
	{
		validateFutureDate('cmplx_Customer_IdIssueDate');
		if(isFieldFilled('cmplx_Customer_EmiratesID')==false){
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL353']);
			setNGValue(pId,'');
			return false;
		}
	}
	//below code added by nikhil
	else if(pId=='WorldCheck1_Dob')
	{
		//++ Below Code added By Abhishek on Oct 6, 2017  to fix : "17-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017++
		setNGValue("WorldCheck1_age", calcAge(getNGValue(pId),''));//modified by akshay on 14/10/17
		//-- Above Code added By Abhishek on Oct 6, 2017  to fix : "17-Initiation-Customer details-Age is not auto calculating in world check" : Reported By Shashank on Oct 05, 2017--
		validateFutureDate(pId);
	}	
		
    else if(pId=='ref_Relationship')
	{
		setNGValue('ref_Relationship','FRIENDS');
	}
	/*else if(pId=='cmplx_Customer_COuntryOFResidence')
	{
		setNGValue('cmplx_Customer_COuntryOFResidence','United Arab Emirates-AE');
	}		*/
		
	else if(pId=='subProd')
	{
		return true;
	}
	
	else if(pId=='AppType')
	{
		var appType=getNGValue("AppType");
		if(appType=='Temporary')
		{
			setVisible("LimitExpiryDate", true);
			setVisible("Product_Label18", true);
		}
		else
		{
			setVisible("LimitExpiryDate", false);
			setVisible("Product_Label18", false);
		}
		
	}
	
	else if(pId=='ReqLimit')
	{
		putComma(pId);
	}	
	else if(pId=='CardProd')
	{
		return true;
	}
	else if(pId=='ReqTenor')
	{
		if(parseInt(getNGValue(pId))<=0 || parseInt(getNGValue(pId))>301){
				showAlert(pId,"Tenor should be between 0 and 300");
				setNGValue(pId,'');
				return false;
		}	
		putComma(pId);
	}
	//added by akshay on 9/5/18 for proc 9240
	else if(pId=='cmplx_LoanDetails_lonamt')
	{
		var maxLoanAmount=getNGValue('LoanDetails_MaxLoanAmount');
		if(parseInt(getNGValue(pId))<=0 || parseInt(getNGValue(pId))>maxLoanAmount){
			showAlert(pId,"Loan Amount should be between 0 and "+maxLoanAmount);
			setNGValue(pId,'');
			return false;
		}	
	}
	else if(pId=='passExpiry' || pId=='GuarantorDetails_passExpiry')
	{
		validatePastDate(pId,'Passport Expiry');
	}
	// ++ below code already present - 09-10-2017 code optimised
	else if(pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other'){
		setNGValue(pId,setDecimalto2digits(pId));
		putComma(pId);		
		var grossSal=calcGrossSal();
		setNGValue("cmplx_IncomeDetails_grossSal",(grossSal>0)?grossSal:"");
		putComma('cmplx_IncomeDetails_grossSal');
		setNGValue("cmplx_IncomeDetails_totSal",calcTotalSal());
		putComma('cmplx_IncomeDetails_totSal');
		
	}
	else if(pId=='cmplx_IncomeDetails_UnderwritingOther')
	{
		setNGValue(pId,setDecimalto2digits(pId));
		putComma(pId);
	}
	
	// disha FSD
	else if(pId=='cmplx_IncomeDetails_SalaryDay')
	{
		if(getNGValue(pId)<1 || getNGValue(pId)>31 && getNGValue(pId)!=='')
		{
			showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['PL350']);
			// ++ below code already present - 09-10-2017
			setNGValue(pId,'');
		}
	}
	else if(pId=='cmplx_IncomeDetails_StatementCycle')
	{
		if(getNGValue(pId)<1 || getNGValue(pId)>31 && getNGValue(pId)!=='')
		{
			showAlert('cmplx_IncomeDetails_StatementCycle',alerts_String_Map['PL350']);
		}
	}		
	
	// ++ below code already present - 09-10-2017 code optimised
	else if(pId=='cmplx_IncomeDetails_Overtime_Month1' || pId=='cmplx_IncomeDetails_Overtime_Month2' || pId=='cmplx_IncomeDetails_Overtime_Month3'|| pId=='cmplx_IncomeDetails_Commission_Month1' || pId=='cmplx_IncomeDetails_Commission_Month2' || pId=='cmplx_IncomeDetails_Commission_Month3' || pId=='cmplx_IncomeDetails_FoodAllow_Month1' || pId=='cmplx_IncomeDetails_FoodAllow_Month2' || pId=='cmplx_IncomeDetails_FoodAllow_Month3' || pId=='cmplx_IncomeDetails_PhoneAllow_Month1' || pId=='cmplx_IncomeDetails_PhoneAllow_Month2' || pId=='cmplx_IncomeDetails_PhoneAllow_Month3' || pId=='cmplx_IncomeDetails_serviceAllow_Month1' || pId=='cmplx_IncomeDetails_serviceAllow_Month2' || pId=='cmplx_IncomeDetails_serviceAllow_Month3' || pId=='cmplx_IncomeDetails_Bonus_Month1'|| pId=='cmplx_IncomeDetails_Bonus_Month2' || pId=='cmplx_IncomeDetails_Bonus_Month3' || pId=='cmplx_IncomeDetails_Other_Month1' || pId=='cmplx_IncomeDetails_Other_Month2' || pId=='cmplx_IncomeDetails_Other_Month3' || pId=='cmplx_IncomeDetails_Flying_Month1' || pId=='cmplx_IncomeDetails_Flying_Month2' || pId=='cmplx_IncomeDetails_Flying_Month3'){
		
		setNGValue(pId,setDecimalto2digits(pId));
		putComma(pId);
		if(pId.indexOf('Overtime')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Overtime_Month1","cmplx_IncomeDetails_Overtime_Month2","cmplx_IncomeDetails_Overtime_Month3");
			setNGValue("cmplx_IncomeDetails_Overtime_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Commission')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Commission_Month1","cmplx_IncomeDetails_Commission_Month2","cmplx_IncomeDetails_Commission_Month3");
			setNGValue("cmplx_IncomeDetails_Commission_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('FoodAllow')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_FoodAllow_Month1","cmplx_IncomeDetails_FoodAllow_Month2","cmplx_IncomeDetails_FoodAllow_Month3");
			setNGValue("cmplx_IncomeDetails_FoodAllow_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('PhoneAllow')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_PhoneAllow_Month1","cmplx_IncomeDetails_PhoneAllow_Month2","cmplx_IncomeDetails_PhoneAllow_Month3");
			setNGValue("cmplx_IncomeDetails_PhoneAllow_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('serviceAllow')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_serviceAllow_Month1","cmplx_IncomeDetails_serviceAllow_Month2","cmplx_IncomeDetails_serviceAllow_Month3");
			setNGValue("cmplx_IncomeDetails_serviceAllow_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Bonus')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Bonus_Month1","cmplx_IncomeDetails_Bonus_Month2","cmplx_IncomeDetails_Bonus_Month3");
			setNGValue("cmplx_IncomeDetails_Bonus_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Other')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Other_Month1","cmplx_IncomeDetails_Other_Month2","cmplx_IncomeDetails_Other_Month3");
			setNGValue("cmplx_IncomeDetails_Other_Avg",(avg>0)?avg:"");
		}
		else if(pId.indexOf('Flying')>-1){
			var avg=calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Flying_Month1","cmplx_IncomeDetails_Flying_Month2","cmplx_IncomeDetails_Flying_Month3");
			setNGValue("cmplx_IncomeDetails_Flying_Avg",(avg>0)?avg:"");
		}
		
		setNGValue("cmplx_IncomeDetails_Totavgother",calcTotalAvgOther());
		putComma('cmplx_IncomeDetails_Totavgother');
		setNGValue("cmplx_IncomeDetails_totSal",calcTotalSal());
		putComma('cmplx_IncomeDetails_totSal');
	}
	
	
	// ++ below code already present - 09-10-2017
	else if (pId=='cmplx_EligibilityAndProductInfo_InterestRate')
	{
		var Intrate= getNGValue('cmplx_EligibilityAndProductInfo_InterestRate');
		var BaseRate= getNGValue('cmplx_EligibilityAndProductInfo_BAseRate'); 
		var ProdPrefRate= getNGValue('cmplx_EligibilityAndProductInfo_ProdPrefRate');
		setNGValue('cmplx_EligibilityAndProductInfo_MArginRate',parseFloat(Intrate)-parseFloat(BaseRate)-parseFloat(ProdPrefRate));
		setNGValue('cmplx_EligibilityAndProductInfo_FinalInterestRate',parseFloat(Intrate));
		setNGValue('cmplx_EligibilityAndProductInfo_InterestRate',parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_InterestRate')).toFixed(2));
		//change by saurabh on 7th Dec
		
		if(activityName=='CSM')
		{
			setNGValue("Interest_Rate_App_req","Y");  //added by disha for CSM Interest_Rate_Approval Flag on 6Th April 2018
		}
		return true;
	}
	//change by saurabh on 7th Dec
	else if(pId=='cmplx_EligibilityAndProductInfo_Tenor' || pId=='cmplx_LoanDetails_lpf'){
		return true;
	}
	// ++ above code already present - 09-10-2017
	
	else if(pId=='cmplx_LoanDetails_tenor')
	{
		var firstRepayDate=getNGValue('cmplx_LoanDetails_frepdate');
		var tenor=getNGValue(pId);
		var maturityDate=MaturityDate(firstRepayDate,tenor);
		setNGValue('cmplx_LoanDetails_maturitydate',maturityDate);
		return true;
	}
	// ++ below code already present - 09-10-2017 code optimised
	else if(pId=='cmplx_IncomeDetails_netSal1' || pId=='cmplx_IncomeDetails_netSal2' || pId=='cmplx_IncomeDetails_netSal3'){
		setNGValue(pId,setDecimalto2digits(pId));
		putComma(pId);	
		setNGValue("cmplx_IncomeDetails_AvgNetSal",calcAvgNet());
		putComma('cmplx_IncomeDetails_AvgNetSal');	
	}
	
	else if(pId=='cmplx_IncomeDetails_BankStatFromDate')
	{
		validateFutureDate(pId);
	}
			
	else if(pId=='cmplx_IncomeDetails_BankStatToDate')
	{
		validateFutureDate(pId);
	}
		
	else if(pId=='cmplx_CompanyDetails_tradelicenseExpiryDate')
	{
		validatePastDate(pId,'Trade License Expiry');
	}
	
	else if(pId=='DOB' || pId=='GuarantorDetails_idIssueDate' || pId=='GuarantorDetails_passportIssueDate' || pId=='GuarantorDetails_VisaIssueDate')
	{
		validateFutureDate(pId);
	}
	else if(pId=='cmplx_EmploymentDetails_ApplicationCateg')
	{
	if(getNGValue(pId)=='S')
	{
	setVisible('EMploymentDetails_Label59',true);
	setVisible('cmplx_EmploymentDetails_IndusSeg',true);
	}
	else
	{
	setVisible('EMploymentDetails_Label59',false);
	setVisible('cmplx_EmploymentDetails_IndusSeg',false);
	}
	}
		
	else if(pId=='PassportExpiryDate')
	{
		validatePastDate(pId,'Passport Expiry');
	}
		
	else if(pId=='eidExpiry' || pId=='GuarantorDetails_eidExpiry')
	{
		validatePastDate(pId,'Emirate ID Expiry');
	}
		
	else if(pId=='visaExpiry' || pId=='GuarantorDetails_visaExpiry')
	{
		validatePastDate(pId,'Visa Expiry');
	}
		
	else if(pId=='TLExpiry')
	{
		validatePastDate(pId,'Trade License Expiry');
	}
		
	else if(pId=='PartnerDetails_Dob'  || pId=='InwardTT_date')
	{
		validateFutureDate(pId);
	}
	else if(pId=='DecisionHistory_ReferTo')
	{
		return true;
	}
		
	else if(pId=='VisaExpiryDate')
	{
		validatePastDate(pId,'Visa Expiry');	
	}
	
	else if(pId=='cmplx_EmploymentDetails_DOJ'){
		validateFutureDate(pId);
		//calcLOSInCurrCompany(pId);
		YYMM(pId,'cmplx_EmploymentDetails_LOS','');
		//return true;
	}	
	else if(pId=='AddressDetails_years'){
		YYMM(pId,'','');
	}	
		
	else if(pId=='cmplx_EligibilityAndProductInfo_FirstRepayDate')
{	
		 if(validatePastDate(pId,'First Repayment')){
			var FirstRepaymentDate = getNGValue(pId);	
			var tenor=getNGValue('cmplx_EligibilityAndProductInfo_Tenor');
			var maturityDate=MaturityDate(FirstRepaymentDate,tenor);
			setNGValue("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
			
				var maturityAge=calcAge(getNGValue('cmplx_Customer_DOb'),maturityDate);
				setNGValue("cmplx_EligibilityAndProductInfo_AgeAtMaturity",maturityAge);
	/*			var d= new Date();
				var today = d.getDate()+'/'+d.getMonth()+'/'+d.getFullYear();
				var moratorium = calcAge(today,getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate'));
				var months = (parseInt(moratorium.split('.')[0])*12)+parseInt(moratorium.split('.')[1])-1;
				setNGValue('cmplx_EligibilityAndProductInfo_Moratorium',months);
		*/
		    var Days= Moratorium(pId);
			setNGValue('cmplx_EligibilityAndProductInfo_Moratorium',Days);
		
		 }
		 }
	
	else if(pId=='cmplx_EligibilityAndProductInfo_MaturityDate')	
		validatePastDate(pId,'Maturity');
	
	else if(pId=='cmplx_EligibilityAndProductInfo_Moratorium')
	{
		var firstRepayDate=Calc_FirstRepaymentDate(getNGValue(pId));
		setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',firstRepayDate);
	}
	
	else if(pId=='AlternateContactDetails_MobileNo1')
	{
	    var Mobile_no=getNGValue('AlternateContactDetails_MobileNo1');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=''){
			Mobile_no="00971"+Mobile_no;
			setNGValue('AlternateContactDetails_MobileNo1',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL087']);
			setNGValue('AlternateContactDetails_MobileNo1','');
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL088']);
			setNGValue('AlternateContactDetails_MobileNo1','');
			return false;
		}
		if(getNGValue('AlternateContactDetails_MobileNo2')=='')
		{
			setNGValue('AlternateContactDetails_MobileNo2',Mobile_no); //added By Tarang started on 13/02/2018 as per drop 4 point 16
		}
	}
	
	else if(pId=='AlternateContactDetails_MobileNo2')
	{
	    var Mobile_no=getNGValue('AlternateContactDetails_MobileNo2');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=''){
			Mobile_no="00971"+Mobile_no;
			setNGValue('AlternateContactDetails_MobileNo2',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo2',alerts_String_Map['VAL087']);
			setNGValue('AlternateContactDetails_MobileNo2','');
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo2',alerts_String_Map['VAL088']);
			setNGValue('AlternateContactDetails_MobileNo2','');
			return false;
		}
	}
	else if (pId=='AlternateContactDetails_Email1')
		validateMail1(pId);
	
	else if (pId=='AlternateContactDetails_Email2')
		validateMail1(pId);
	//below code changed by nikhil
	else if (pId=='cmplx_CustDetailVerification_email1_upd' || pId=='cmplx_CustDetailVerification_email2_upd')
		validateMail1(pId);//Arun 12/12/17 to CPV
	
	else if (pId=='cmplx_CustDetailVerification_email2_upd')//Arun 12/12/17 to CPV
		validateMail1(pId);//Arun 12/12/17 to CPV
		
	//added By Tarang for drop 4 point 1 started on 02/22/2018
	else if (pId=='AlternateContactDetails_carddispatch'){
		var cardDisp=getNGSelectedItemText('AlternateContactDetails_carddispatch');
		if(cardDisp=='International dispatch-INTL' || cardDisp=='Holding WI- HOLD' || cardDisp=='Card centre collection' || cardDisp=='COURIER'){
			setNGValue('Card_Dispatch_Option',cardDisp);
		}
		else{
			setNGValue('Card_Dispatch_Option','Branch');
		}
		//alert(getNGValue('Card_Dispatch_Option'));
	}
	//added By Tarang for drop 4 point 1 ended on 02/22/2018
	
	else if(pId=='MobNo')
	{
		var Mobile_no=getNGValue('MobNo');
		var mob_No=Mobile_no;
		if(Mobile_no.substring(0,5)!='00971'){
			mob_No="00971"+mob_No;
			setNGValue('MobNo',mob_No);
		}	
		if(mob_No.length!=15){
			showAlert('MobNo',alerts_String_Map['PL351']);
		}	

	}	
	else if(pId=='cmplx_FATCA_ExpiryDate')
	{
		validatePastDate(pId,'FATCA Expiry');	
	}	
	else if (pId=='FATCA_USRelaton'){
		
		if (getNGValue('FATCA_USRelaton')=='O')
		{
			if(getNGValue('cmplx_Customer_Nationality')=='US'){
				showAlert('FATCA_USRelaton',alerts_String_Map['PL352']);
				setNGValue('FATCA_USRelaton',"");
			}
			else{
				Fatca_disableCondition();
				setLocked('cmplx_FATCA_ListedReason',true);
				setLocked('cmplx_FATCA_SelectedReason',true);
			}

			setNGValue('cmplx_FATCA_iddoc',true);
			setNGValue('cmplx_FATCA_decforIndv',true);
		}
		
		else if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')
		{
			Fatca_Enable();
			setNGValue('cmplx_FATCA_iddoc',true);
			setNGValue('cmplx_FATCA_decforIndv',false);
		}
		else if(getNGValue('FATCA_USRelaton')=='C'){
				Fatca_disableCondition();
				setLocked("cmplx_FATCA_SignedDate",false);
			}
		else{
			Fatca_Enable();
			}
	} //Arun from CC as per FSD
	else if(pId=='cmplx_FATCA_SignedDate'){
		if(validateFutureDate(pId))
		{
		  
			var signedDate=getNGValue(pId);
			var years_signedDate=signedDate.substring(6,10);
			var years_expiryDate=parseInt(years_signedDate)+3;
			var ExpiryDate=signedDate.replace(years_signedDate,years_expiryDate);
			setNGValue('cmplx_FATCA_ExpiryDate',ExpiryDate);	
		  
		} 
	}	
	else if(pId=='cmplx_CC_Loan_TransMode')
	{
		if(getNGValue(pId)=='Cheque'){
			setLocked('cmplx_CC_Loan_mchequeno',false);
			setLocked('cmplx_CC_Loan_mchequeDate',false);
			setLocked('cmplx_CC_Loan_mchequestatus',false);
		}
		else{
			setLocked('cmplx_CC_Loan_mchequeno',true);
			setLocked('cmplx_CC_Loan_mchequeDate',true);
			setLocked('cmplx_CC_Loan_mchequestatus',true);
		}	
	}		
		
		
	else if(pId=='ReqProd')
	{
		if(getNGValue(pId)=='Personal Loan'){
			setVisible('IncomeDetails_Label12',false);
			setVisible('cmplx_IncomeDetails_StatementCycle',false);
			}
		return true;
	}
	else if(pId=='cmplx_Decision_Feedbackstatus')
	{
		return true;
	}
	
	
	else if(pId=='cmplx_Decision_Decision'){
		var desc = getNGValue('cmplx_Decision_Decision');
		if(activityName=="CAD_Analyst1")	
			{
				
				setNGValue("CAD_dec",desc);
				//change by saurabh on 3rd Dec for FSD 2.7
			if(getNGValue(pId)=='Approve'){
			//disable cad- dec tray
			setLocked('cmplx_Decision_CADDecisiontray',true);
				if(getNGValue('Is_Financial_Summary')!='Y'){
				showAlert('IncomeDEtails','Please fetch Financial Summary first');
				setNGValue(pId,'--Select--');
				return false;
				}
				else if(getNGValue('cmplx_Decision_Manual_Deviation')==true && parseInt(getNGValue('reEligibility_PL_counter').split(';')[1])==0){
				showAlert(pId,'Please calculate Re-eligibility first');
				setNGValue(pId,'--Select--');
				return false;
				}
				else if(getNGValue('cmplx_EmploymentDetails_FieldVisitDone')=='A'){
				showAlert(pId,'Cannot take Decision Approved as Field Visit Done is Awaited');
				setNGValue(pId,'--Select--');	
				return false;
				}
				else if(getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='FVR'){
				showAlert(pId,'Cannot take Decision Approved as Employer Status CC is Awaiting FVR');
				setNGValue(pId,'--Select--');	
				return false;
				}
				//change by saurabh on 7th DEC
				if(getNGValue('Is_CAM_generated')!='Y'){
					showAlert(pId,'Cannot take Decision Approved as CAM report not generated');
					setNGValue(pId,'--Select--');
					return false;
				}
				else if(getNGValue('cmplx_Customer_NTB')==false && getFromJSPRowCount('ELigibiltyAndProductInfo_IFrame4')==1){
					showAlert(pId,'No Account found in Funding Account Grid...Refer the workitem back to the RM CSO!!');
					setNGValue(pId,'--Select--');
					return false;
				}//added by akshay for proc 7731
			}
			else if(getNGValue(pId).toUpperCase()=='REJECT' || getNGValue(pId).toUpperCase()=='REFER')
			{
			//enable cad- dec tray
			setLocked('cmplx_Decision_CADDecisiontray',false);
			
			}
		}
		
		else if(activityName=='CPV')
		{
			
		//below code aded for proc-10022
		 if(getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF' )
		{
				if (getNGValue('cmplx_HCountryVerification_Decision') == 'Negative' || getNGValue('cmplx_HCountryVerification_Decision')=='Pending')
			{
				showAlert(pId, 'Decision cannot be Approve as Home Country Verification decision is '+getNGValue('cmplx_HCountryVerification_Decision'));
				setNGValue(pId,'--Select--');
				return false;
			}
			 if (getNGValue('cmplx_ResiVerification_Decision') == 'Negative' || getNGValue('cmplx_HCountryVerification_Decision')=='Pending')
			{
				showAlert(pId, 'Decision cannot be Approve as Residence Verification decision is '+getNGValue('cmplx_ResiVerification_Decision'));
				setNGValue(pId,'--Select--');
				return false;
			}
			 if (getNGValue('cmplx_CustDetailVerification_Decision') == 'Negative' || getNGValue('cmplx_HCountryVerification_Decision')=='Pending')
			{
				showAlert(pId, 'Decision cannot be Approve as Customer Detail Verification decision is '+getNGValue('cmplx_CustDetailVerification_Decision'));
				setNGValue(pId,'--Select--');
				return false;
			}
			
			 if (getNGValue('cmplx_RefDetVerification_Decision') == 'Negative' || getNGValue('cmplx_RefDetVerification_Decision')=='Pending')
			{
				showAlert(pId, 'Decision cannot be Approve as reference Verification decision is '+getNGValue('cmplx_RefDetVerification_Decision'));
				setNGValue(pId,'--Select--');
				return false;
			}
			 if ((getNGValue('cmplx_OffVerification_Decision') == 'Negative' || getNGValue('cmplx_OffVerification_Decision')=='Pending') && !(isLocked('cmplx_OffVerification_Decision') && !isEnabled('OfficeandMobileVerification_Enable')))
			{
				showAlert(pId, 'Decision cannot be Approve as Office Verification decision is '+getNGValue('cmplx_OffVerification_Decision'));
				setNGValue(pId,'--Select--');
				return false;
			}
			 if (getNGValue('cmplx_LoanandCard_Decision') == 'Negative'  || getNGValue('cmplx_LoanandCard_Decision')=='Pending')
			{
				showAlert(pId, 'Decision cannot be Approve as Loan and Card Verification decision is '+getNGValue('cmplx_LoanandCard_Decision'));
				setNGValue(pId,'--Select--');
				return false;
			}
			 if (getNGValue(pId)=='Approve Sub to CIF' && (getNGValue('cmplx_CustDetailVerification_mobno1_ver') != 'Mismatch' && getNGValue('cmplx_CustDetailVerification_POBoxno_ver') != 'Mismatch' && getNGValue('cmplx_CustDetailVerification_email1_ver') != 'Mismatch') )
		{
			showAlert(pId, 'Decision cannot be Approve Sub to CIF as no Mismatch in Customer Details.');
			setNGValue(pId,'--Select--');
			return false;
		}
			if (getNGValue(pId)=='Approve' && (getNGValue('cmplx_CustDetailVerification_mobno1_ver') == 'Mismatch' || getNGValue('cmplx_CustDetailVerification_POBoxno_ver') == 'Mismatch' || getNGValue('cmplx_CustDetailVerification_email1_ver') == 'Mismatch') )
			{
				showAlert(pId, 'Decision cannot be Approve as Mismatch in Customer Details. Please Select Approve Sub to CIF!');
				setNGValue(pId,'--Select--');
				return false;
			}
		}
	}
			//below code added by yash on 15/12/2017 fro toteam
		else if(activityName=="ToTeam")
			{
				if(getNGValue(pId)=='Pending for documentation')
				{
					setEnabled("DecisionHistory_DecisionReasonCode",true);
				}
				
			}
			//above code added by yash on 15/12/2017
		else if(activityName=='Disbursal'){
			if( desc=='Approved with deferred doc'){
				if(getNGValue('DeferredDocsList')=='' || getNGValue('DeferredDocsList')==null){
					showAlert(pId,'Cannot take Decision as Approved with Deferred doc as no documents are deferred');
					setNGValue(pId,'--Select--');	
					return false;
				}
				if(getNGValue('Application_Type')!='' && getNGValue('Application_Type').indexOf('TKO')==-1){
					showAlert(pId,'Cannot take Decision as Approved with Deferred doc as this is not a Takeover case');
					setNGValue(pId,'--Select--');	
					return false;
				}
			}
			else{
				var updCustflag = getNGValue('cmplx_LoanDisb_updateCustomerFlag');
				var accUpdflag = getNGValue('cmplx_LoanDisb_updateAccountFlag');
				var alertStat=false;
				var msg = "Loan Disbursal not completed";
				if(updCustflag!='Y' && isVisible('DecisionHistory_updcust_loan') && !isLocked('DecisionHistory_updcust_loan') && isEnabled('DecisionHistory_updcust_loan') ){
					alertStat = true;
				}
				if(accUpdflag!='Y' && isVisible('DecisionHistory_updacct_loan') && !isLocked('DecisionHistory_updacct_loan') && isEnabled('DecisionHistory_updacct_loan')){
					alertStat = true;
				}
				if(getNGValue('cmplx_LoanDisb_ChequeBookFlag')!='Y' && isVisible('DecisionHistory_chqbook_loan') && !isLocked('DecisionHistory_chqbook_loan') && isEnabled('DecisionHistory_chqbook_loan')){
					alertStat = true;
				}
				if(getNGValue('cmplx_LoanDisb_contractCreationFlag')!='Y' && isVisible('Loan_Disbursal_Button1') && !isLocked('Loan_Disbursal_Button1') && isEnabled('cmplx_LoanDisb_contractCreationFlag')){
					alertStat = true;
				}
				if(alertStat){
					showAlert(pId,msg);
					setNGValue(pId,'--Select--');	
					return false;
				}
			}
		}
		else if(activityName=='CC_Disbursal'){
			if(isVisible('CC_Creation_Frame3')){
					var lvname = 'cmplx_CCCreation_cmplx_CCCreationGrid';
					var rows = getLVWRowCount(lvname);
					var pass = true;
					if(rows>0){
						for(var i=0;i<rows;i++){
							if(getLVWAT(lvname,i,8)!='Y' || getLVWAT(lvname,i,9)!='Y'){
							pass = false;
							break;
							}
						}
					}
					if(!pass){
						showAlert(lvname, 'Cards Disbursal not completed');
						setNGValue(pId,'--Select--');
						return false;
					}
				}
		}
		else if(activityName=='DDVT_maker')
		{	
				setLocked('DecisionHistory_DecisionReasonCode', false);
				setNGValue("decision", desc);
		}	
			
		else{	
			setNGValue("decision", desc);
		}
		
		return true;//uncommented by akshay on 4/12/17

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
		com.newgen.omniforms.formviewer.setNGValue('ReferTo',com.newgen.omniforms.formviewer.getNgSelectedValues(pId));
	}	
	//ended by akshay on 9/12/17 for multiple refer
	
	else if(pId=='cmplx_CustDetailVerification_Decision' || pId=='cmplx_OffVerification_Decision' || pId=='cmplx_LoanandCard_Decision' || pId=='cmplx_HCountryVerification_Decision' || pId=='cmplx_ResiVerification_Decision' || pId=='cmplx_GuarantorVerification_Decision')
	{
		if(getNGValue(pId)=='Positive'){
			var ver;
			if(pId=='cmplx_CustDetailVerification_Decision'){
			  ver="cmplx_CustDetailVerification_mobno1_ver:cmplx_CustDetailVerification_mobno2_ver:cmplx_CustDetailVerification_dob_verification:cmplx_CustDetailVerification_POBoxno_ver:cmplx_CustDetailVerification_emirates_ver:cmplx_CustDetailVerification_persorcompPOBox_ver:cmplx_CustDetailVerification_offtelno_ver:cmplx_CustDetailVerification_email1_ver:cmplx_CustDetailVerification_email2_ver:cmplx_CustDetailVerification_hcountrytelno_ver:cmplx_CustDetailVerification_hcontryaddr_ver";
			 if(!checkMandatory(PL_Custdetail_CPV))
				{
					setNGValue(pId,'');
					return false;
				}
			}
			else if(pId=='cmplx_OffVerification_Decision'){
			 ver="cmplx_OffVerification_hrdemailverified:cmplx_OffVerification_fxdsal_ver:cmplx_OffVerification_accpvded_ver:cmplx_OffVerification_desig_ver:cmplx_OffVerification_doj_ver:cmplx_OffVerification_cnfminjob_ver";
						
					if(!checkMandatory(PL_OffVerification_CPV))
					{
						setNGValue(pId,'');
						return false;
					}
			}	
			
			else if(pId=='cmplx_LoanandCard_Decision'){
				ver="cmplx_LoanandCard_loanamt_ver:cmplx_LoanandCard_tenor_ver:cmplx_LoanandCard_emi_ver:cmplx_LoanandCard_islorconv_ver:cmplx_LoanandCard_firstrepaydate_ver:cmplx_LoanandCard_cardtype_ver:cmplx_LoanandCard_cardlimit_ver";		
			}
			
			else if(pId=='cmplx_HCountryVerification_Decision'){
				if(!checkMandatory(PL_HOMEVERIFICATION))
					{
						setNGValue(pId,'');
						return false;
					}
			}

			else if(pId=='cmplx_HCountryVerification_Decision'){
				if(!checkMandatory(PL_RESIDENCEVERIFICATION))
					{
						setNGValue(pId,'');
						return false;
					}
			}
			
			var verificationFields_array=ver.split(':');
			for(var i=0;i<verificationFields_array.length;i++){
				if(getNGValue(verificationFields_array[i])=='No'){
						showAlert(verificationFields_array[i],'Decision cannot be taken positive since Verification is selected as No!!');
						setNGValue(pId,'');
				}
			}
		}	
	}
	
	
	else if(pId=='Product_subProd')
	{
		var subProd=getNGValue("Product_subProd");
		if(subProd=='IM'){
			setVisible("Product_Label5", true);
			setVisible("Product_ReqTenor", true);
		}	
		
		else {
			setVisible("Product_Label5", false);
			setVisible("Product_ReqTenor", false);
		}		
	return true;
	}
	
	else if (pId =='PartMatch_mno1')	
	{
		var Mobile_no=getNGValue('PartMatch_mno1');
		var mob_No="00971"+Mobile_no;
		setNGValue('PartMatch_mno1',mob_No);
	}
	else if (pId =='PartMatch_mno2')	
	{
		var Mobile_no=getNGValue('PartMatch_mno2');
		var mob_No="00971"+Mobile_no;
		setNGValue('PartMatch_mno2',mob_No);
	}
	
	else if (pId =='cmplx_OffVerification_hrdemailverified')	
	{
		if(getNGValue(pId)=='Yes')
		{
			showAlert('cmplx_OffVerification_hrdemailid',alerts_String_Map['PL386']);
		}
	}//Arun 12/12/17
	
	else if (pId =='cmplx_OffVerification_hrdnocntctd')	
	{
		if(getNGValue(pId)=='Yes' && getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=='')
		{
			showAlert('cmplx_OffVerification_offtelnovalidtdfrom',alerts_String_Map['PL387']);
			setNGValue(pId,'');
			
		}
	}//Arun 12/12/17
	
	//added by akshay on 28/12/17	
		//else if(pId=='ExtLiability_Limit')
	//{
	//	var limit=parseFloat(getNGValue(pId));
	//	var emi=(2/100*limit).toFixed(2);
	//	setNGValue('Liability_New_EMI',emi);
	//}	
	
	else if(pId=='OECD_tinNo'){
		var tin = getNGValue('OECD_tinNo');
		if(tin){
		setEnabled('OECD_noTinReason',false);
		}
		else{
		setEnabled('OECD_noTinReason',true);
		}
	}
	else if(pId=='cmplx_CardDetails_suppcardreq')
	{
		if(getNGValue('cmplx_CardDetails_suppcardreq')=='Yes')
			{
				setVisible("Supplementary_Card_Details", true);
				setTop('Supplementary_Card_Details',parseInt(getTop('Card_Details'))+parseInt(getHeight('Card_Details'))+30+'px');
				setNGValue('IS_SupplementCard_Required','Y');
				
			}

			else
			{
				if(getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid')>0)
				{
					showAlert(pId,alerts_String_Map['VAL358']);
					setNGValue(pId,'Yes');
					return false;
				}
				else{
					setNGValue('IS_SupplementCard_Required','N');
				}
				setVisible("Supplementary_Card_Details", false);
			}
	}
	
	else if(pId=='cmplx_LoanDetails_lpfamt')
	{
		setNGValue('cmplx_LoanDetails_amt',parseFloat(getNGValue(pId)) + parseFloat(getNGValue('cmplx_LoanDetails_insuramt'))+parseFloat(getNGValue('cmplx_LoanDetails_LoanProcessingVat'))+parseFloat(getNGValue('cmplx_LoanDetails_InsuranceVat')));
	}
	
	else if(pId=='cmplx_CustDetailVerification_mobno1_ver')
	{
		if(getNGValue(pId)=='Yes'){
			setLocked('cmplx_CustDetailVerification_mobno2_ver',true);
			setLocked('cmplx_CustDetailVerification_mobno2_upd',true);

		}
		else{
			setLocked('cmplx_CustDetailVerification_mobno2_ver',false);
			setLocked('cmplx_CustDetailVerification_mobno2_upd',false);
		}
	}

	else if(pId=='cmplx_CustDetailVerification_mobno2_ver')
	{
		if(getNGValue(pId)=='Yes'){
			setLocked('cmplx_CustDetailVerification_mobno1_ver',true);
			setLocked('cmplx_CustDetailVerification_mobno1_upd',true);

		}
		else{
			setLocked('cmplx_CustDetailVerification_mobno1_ver',false);
			setLocked('cmplx_CustDetailVerification_mobno1_upd',false);
		}
	}
	
	else if(pId=='LoanDetails_accno')
	{
		if(getNGValue(pId)!=''){
			setNGValue('LoanDetails_valstatus','Active');
		}
		else{
		setNGValue('LoanDetails_valstatus','Inactive');
		}
	}
	
	else if(pId=='cmplx_LoanDetails_drawnon')
	{
		if(getNGValue(pId)!=''){
			setNGValue('cmplx_LoanDetails_valstatus','Active');
		}
		else{
		setNGValue('cmplx_LoanDetails_valstatus','Inactive');
		}
	}
	
	if(pId=='SupplementCardDetails_FirstName' || pId=='SupplementCardDetails_lastname' || pId=='SupplementCardDetails_passportNo' || pId=='SupplementCardDetails_DOB' || pId=='SupplementCardDetails_Nationality' || pId=='SupplementCardDetails_MobNo' || pId=='SupplementCardDetails_Text6'){
		setLocked('SupplementCardDetails_FetchDetails',false);
	}
 return false;
 }
 

  
  function blur_PL(pId)
 {
	if(pId=='cmplx_Customer_MobNo')
	{
		var Mobile_no=getNGValue('cmplx_Customer_MobNo');
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValue('cmplx_Customer_MobNo',mob_No);
		}	
		if(mob_No.length!=14){
			showAlert('cmplx_Customer_MobNo',alerts_String_Map['PL341']);
			return false;
		}	
		setNGValue('OTP_Mobile_NO',mob_No);
		setLocked('FetchDetails',false);
	}
	
	else if(pId=='cmplx_CustDetailVerification_mobno1_upd' || pId=='GuarantorDetails_mobNo')
	{
		var Mobile_no1=getNGValue(pId);
		var mob_No1=Mobile_no1;
		if(mob_No1.substring(0,5)!='00971' && mob_No1.length<10 && mob_No1!=""){
			mob_No1="00971"+mob_No1;
			setNGValue(pId,mob_No1);
		}	
		if(mob_No1.length!=14 && mob_No1!=""){
			showAlert(pId,alerts_String_Map['VAL087']);
			return false;
		}	
		
	}//Arun 12/12/17 added for CPV validation
	
	else if(pId=='cmplx_CustDetailVerification_mobno2_upd')
	{
		var Mobile_no2=getNGValue('cmplx_CustDetailVerification_mobno2_upd');
		var mob_No2=Mobile_no2;
		if(mob_No2.substring(0,5)!='00971' && mob_No2.length<10 && mob_No2!=""){
			mob_No2="00971"+mob_No2;
			setNGValue('cmplx_CustDetailVerification_mobno2_upd',mob_No2);
		}	
		if(mob_No2.length!=14 && mob_No2!=""){
			showAlert('cmplx_CustDetailVerification_mobno2_upd',alerts_String_Map['VAL087']);
			return false;
		}	
		
	}//Arun 12/12/17 added for CPV validation
	
	else if(pId=='cmplx_Customer_CIFNO'){
		var cif=getNGValue('cmplx_Customer_CIFNO');
		if(cif.length!=7 && cif.length!=0){
			showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL342']);
			return false;
		}
		//belowcode added by nikhil as per ddvt issues
		else if(!checkforfinaclecustinfo())
		{
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL383']);
		return false;
		}
		else{	
			//hash='###'+cif.substring(3,7)+'###';
			//setNGValue('cmplx_Customer_CIFNO',hash);
			return true;
		}
	}
  return false;	
 }
 
	
	
function PL_postHookDRefresh(controlName){
	if(controlName=='ELigibiltyAndProductInfo_Button1'){
	//document.getElementById('ELigibiltyAndProductInfo_IFrame1').src='/formviewer/resources/scripts/Eligibility_Card_Product.jsp';
	document.getElementById('ELigibiltyAndProductInfo_IFrame2').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp';
	document.getElementById('ELigibiltyAndProductInfo_IFrame3').src='/webdesktop/custom/CustomJSP/Product_Eligibility.jsp';
	}
}
	
//New method added to handle keypress event Deepak 18 Dec 2017
 function keydown_PL(pId,keyCode_str)
 {
	//pID name need to added which are mentioned as 17,2 in field list document.
	if(pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other' || pId=='cmplx_IncomeDetails_Commission_Month1' || pId=='cmplx_IncomeDetails_Commission_Month2' || pId=='cmplx_IncomeDetails_Commission_Month3' || pId=='cmplx_IncomeDetails_Flying_Month1' || pId=='cmplx_IncomeDetails_Flying_Month2' || pId=='cmplx_IncomeDetails_Flying_Month3' || pId=='cmplx_IncomeDetails_Bonus_Month1' || pId=='cmplx_IncomeDetails_Bonus_Month2' || pId=='cmplx_IncomeDetails_Bonus_Month3' || pId=='cmplx_IncomeDetails_Overtime_Month1' || pId=='cmplx_IncomeDetails_Overtime_Month2' || pId=='cmplx_IncomeDetails_Overtime_Month3' || pId=='cmplx_IncomeDetails_FoodAllow_Month1'|| pId=='cmplx_IncomeDetails_FoodAllow_Month2' || pId=='cmplx_IncomeDetails_FoodAllow_Month3' || pId=='cmplx_IncomeDetails_PhoneAllow_Month1'|| pId=='cmplx_IncomeDetails_PhoneAllow_Month2' || pId=='cmplx_IncomeDetails_PhoneAllow_Month3' || pId=='cmplx_IncomeDetails_serviceAllow_Month1' || pId=='cmplx_IncomeDetails_serviceAllow_Month2' || pId=='cmplx_IncomeDetails_serviceAllow_Month3' || pId=='cmplx_IncomeDetails_Totavgother' || pId=='cmplx_RiskRating_Total_riskScore' || pId=='FinacleCore_Text11'|| pId=='cmplx_MOL_basic' || pId=='cmplx_MOL_resall' || pId=='cmplx_MOL_transall' || pId=='cmplx_MOL_gross' || pId=='cmplx_MOL_molsalvar' || pId=='cmplx_Decision_TotalOutstanding' || pId=='cmplx_Decision_TotalEMI' || pId=='cmplx_LoanandCard_cardlimit_val' || pId=='cmplx_LoanandCard_cardlimit_ver' || pId=='cmplx_LoanandCard_cardlimit_upd'|| pId=='cmplx_EmploymentVerification_Salary_variance' || pId=='EmploymentVerification_Text5' || pId=='cmplx_EmploymentVerification_fixedsal_val' || pId=='cmplx_EmploymentVerification_fixedsal_ver' || pId=='cmplx_EmploymentVerification_fixedsalupd' || pId=='cmplx_BankingCheck_SalaryCreditUpdate' || pId=='cmplx_MOL_transall' || pId=='cmplx_MOL_resall'){
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
				setNGValue(pId,getNGValue(pId));
				return false;
			}	
		}else{
			if(value.length==17 && !(keyCode_str=='110' || keyCode_str=='190')){
				setNGValue(pId,getNGValue(pId));
				return false;
			}	
		}
		return true;
	 }
	 //for 18,2 fields
	 else if(pId=='cmplx_EligibilityAndProductInfo_TakeoverAMount' || pId=='FinacleCore_cheqamt' || pId=='cmplx_IncomeDetails_UnderwritingOther')
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
				setNGValue(pId,getNGValue(pId));
				return false;
			}	
		}else{
			if(value.length==18 && !(keyCode_str=='110' || keyCode_str=='190')){
				setNGValue(pId,getNGValue(pId));
				return false;
			}	
		}
		return true;
	 }
	 else if(pId=='cmplx_Customer_FIrstNAme' || pId=='cmplx_Customer_MiddleName' || pId=='cmplx_Customer_LAstNAme')
	 {
		var value = getNGValue('cmplx_Customer_FIrstNAme')+getNGValue('cmplx_Customer_MiddleName')+getNGValue('cmplx_Customer_LAstNAme');
		if(value.length>80)
		{
			showAlert('cmplx_Customer_FIrstNAme','Customer Name Can be Max 80 Char!');
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