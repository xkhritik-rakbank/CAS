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
var flag_firco=false;
var alerts_String;
var alerts_String_Map={};


// ++ below code already present - 09-10-2017 to have docindex
var row;
// ++ above code already present - 09-10-2017 to have docindex
var country_GCC= ["AE","SA","KW","QA","BH","OM"];
var initiation_uw=["CAD_Analyst1","DDVT_maker","CSM","SalesCoordinatorCSM"];
var PLFRAGMENTLOADOPT = {};
var rlosValuesData = {};
var PLValuesData = {};
var hiddenStringPL = '';
var hiddenString = '';
var popup_win= null;
var flag_GuarantorDetails=false;
var fieldvisit_flag_ini=false;
var fieldvisit_flag_reci=false;
var mcq_deposit_flag=false;
var cif_update_flag='N';
//var upload_sig=false;
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
PLFRAGMENTLOADOPT['cust_ver_sp2'] = ''; //fragment name changed Cust_Det_Ver
//PLFRAGMENTLOADOPT['Business_Ver'] = '';
PLFRAGMENTLOADOPT['fieldvisit_sp2'] = '';// fragment added
PLFRAGMENTLOADOPT['checklist_ver_sp2'] = '';// fragment added
PLFRAGMENTLOADOPT['exceptionalCase_sp2'] = '';// fragment added
PLFRAGMENTLOADOPT['Banking_Check'] = '';
PLFRAGMENTLOADOPT['Employment_Verification'] = '';
PLFRAGMENTLOADOPT['Smart_Check1'] = '';
PLFRAGMENTLOADOPT['CheckList'] = '';
PLFRAGMENTLOADOPT['Emp_Verification'] = '';
PLFRAGMENTLOADOPT['Exceptional_Case_Alert'] = '';
PLFRAGMENTLOADOPT['Customer_Details_Verification1'] = '';//fragment aded for fcu
PLFRAGMENTLOADOPT['Field_Visit_Initiated'] = '';//fragment aded for fcu
//PLFRAGMENTLOADOPT['Bank_Check'] = '';	//// fragment deleted
PLFRAGMENTLOADOPT['NotepadDetails_CPV'] = '';
PLFRAGMENTLOADOPT['Supervisor_section'] = '';
//PLFRAGMENTLOADOPT['Smart_chk'] = '';
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
PLFRAGMENTLOADOPT['Details'] = '';
PLFRAGMENTLOADOPT['Frame4'] = '';
PLFRAGMENTLOADOPT['Customer_Info_FPU'] = '';
PLFRAGMENTLOADOPT['FPU_GRID'] = '';


var NoOfAttemptsValue=0;//Added by Siva for Sprint 3
var NoOfAttemptsValue_CA=0;//Added by Shweta
var NoOfAttemptsValue_Smart=0;//Added by Shweta
var flag_add_new=false;
var riskScoreCalFlag=0;//by shweta
var ext_liab_click=false;
var fpu_generated=false;
var dectech_click_reminder=false; //by jahnavi
var overwriteReminder=false; //by Bandana PCASI-3459
function focus_PersonalLoanS(pId){
	var activityName=window.parent.stractivityName;
	if(pId.indexOf('cmplx_')>-1 || pId.indexOf('AlternateContactDetails_')>-1 ||   pId.indexOf('LoanDetails_')>-1 ||   pId.indexOf('GuarantorDetails_')>-1){
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
	if(activityName == 'CSM' || activityName== 'DDVT_Maker' || activityName== 'DDVT_Checker' || activityName== 'DDVT_maker'){
	//changes made by saurabh on 23rd Oct.
	if(pId.indexOf('cmplx_')>-1 || pId.indexOf('AlternateContactDetails_')>-1 ||   pId.indexOf('LoanDetails_')>-1 ||   pId.indexOf('GuarantorDetails_')>-1){
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
	 //added by saurabh1 for CAM S4 i4
    var activityName = window.parent.stractivityName;
	if(activityName == 'CAD_Analyst1' || activityName == 'CAD_Analyst2'){
	flag_add_new=true;
	}//saurabh1 end code
	if(pId== 'EmploymentVerification_approved_report'){
		var NoOfRows1=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_emp_ver_sp2_cmplx_loan_disb");
		   if(NoOfRows1 > 0) return true;
		   else showAlert(pId,'No Data To generate Report');      

	}

	if(pId== 'EmploymentVerification_s2_approved_report' ){
		var NoOfRows2=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_emp_ver_sp2_cmplx_loan_rej");
		   if(NoOfRows2 > 0) return true;
		   else showAlert(pId,'No Data To generate Report');          

	}
		
	// pCASI 3606
	if(pId=='Employment_Verification' || pId=='EmploymentVerification_s2_Frame2' && activityName=='CAD_Analyst1'){
	 	setLocked('EmploymentVerification_s2_search_TL_number',true)
	 }
if((pId=='EligibilityAndProductInformation'||pId=='ELigibiltyAndProductInfo_Frame1')&& activityName=='CAD_Analyst1')
{
	if(isVisible('ExtLiability_Frame1')==false)
	{
		showAlert('','Please visit liability deatils first');
		return false;
	}
	else{
		return true;
	}
	
	
}
	if(pId== 'EmploymentVerification_s2_download_comp_report'){
		var NoOfRows3=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_emp_ver_sp2_cmplx_comp_mismatch");
		   if(NoOfRows3 > 0) return true;
		   else showAlert(pId,'No Data To generate Report');          

	}
	// PCASI-2864 - Landline number - incorrect format "Alert not generating". (Will generate on save btn) - Hritik 9/6/21
	if(pId=='EmploymentVerification_s2_Button1')
		 { 
		 if(checkEmp())
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
			setNGValueCustom('cmplx_emp_ver_sp2_landline','');
		}		
		if(mob_No.length<13 || mob_No.length>15){
			showAlert(pId,'Landline number should be between 13-15 digits');
			setNGValueCustom('cmplx_emp_ver_sp2_landline','');
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
	//to team changes by jahnavi
	if(pId=='PostDisbursal_Frame3')
	
		{
			var count =getLVWRowCount('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate')
			if(count>0)
			{
				
				setLocked('PostDisbursal_Frame3',false);
				
			}
		}
		if(pId=='PostDisbursal_Frame6')
	
		{
			var count =getLVWRowCount('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate')
			if(count>0)
			{
				
				setLocked('PostDisbursal_Frame6',false);
			}
				
		}
		if(pId=='PostDisbursal_Frame5')
	
		{
			var count =getLVWRowCount('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate')
			if(count>0)
			{
				setLocked('PostDisbursal_Frame5',false);
				
				setLocked('cmplx_PostDisbursal_Full_Funds_Released',false);
				setLocked('cmplx_PostDisbursal_Government_Company',false);
			}
		}
		if(pId=='PostDisbursal_Frame4')
	
		{
			var count =getLVWRowCount('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate')
			if(count>0)
			{
				
				setLocked('PostDisbursal_Frame4',false);
				setEnabled('PostDisbursal_NLC_Bank_Type',true);
				setEnabled('PostDisbursal_NLC_Bank_Name',true);
				setEnabled('PostDisbursal_NLC_Status',true);
				setEnabled('PostDisbursal_NLC_LC_Doc_name',true);
				setEnabled('PostDisbursal_25Released',true);
				setEnabled('PostDisbursal_FullFundReleased',true);
				setEnabled('PostDisbursal_NLC_released_TO',true);
				
				
			}
		}
	if(pId=='Generate_FPU_Report'){
		
		if(isVisible('checklist_ver_sp2_Frame1')==false || isVisible('SmartCheck1_Frame1')==false || isVisible('Banking_Check')==false || isVisible('fieldvisit_sp2_Frame1')==false || isVisible('EmploymentVerification_s2_Frame2')==false || isVisible('Cust_ver_sp2_Frame1')==false || isVisible('CustDetailVerification1_Frame1')==false)
		{
			showAlert('','Please Visit all FPU fragments first');
			return false;	
		}	
		if(! fpureport_condition()) //by jahnavi for 3125
		{
			return false;
		}  
		if(!validateReferGrid()) //by jahnavi for 3084
		{
			return false;
		}
		
		setNGValueCustom('Is_FPU_Generated','Y');
		fpu_generated=true;
		var wi_name = window.parent.pid;
			var docName = "FPU_REPORT_SALARIED";
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
						RemoveIndicator('GenTemp');
						document.getElementById('EmploymentVerification_approved_report').click();
						document.getElementById('EmploymentVerification_s2_approved_report').click();
						document.getElementById('EmploymentVerification_s2_download_comp_report').click();
						alert("Template generated successfully");
						RemoveIndicator('GenTemp');
						return true;
					}
					//changes by saurabh for PCSP-204
					RemoveIndicator('GenTemp');
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					RemoveIndicator('GenTemp');
				}
			
	}
	 if(pId=='DecisionHistory_Button7'){
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
	if(pId=='fieldvisit_sp2_Button1')
	{  
		if(getNGValue('cmplx_fieldvisit_sp2_drop1')=='Yes')
		{
		if(getNGValue('cmplx_fieldvisit_sp2_drop3')==""||getNGValue('cmplx_fieldvisit_sp2_drop3')=='--Select--')
		{
			showAlert('cmplx_fieldvisit_sp2_drop3','Agency Name is Mandatory');
			return false;
		}
		if(getNGValue('cmplx_fieldvisit_sp2_field_visit_date')==""){
			showAlert('cmplx_fieldvisit_sp2_field_visit_date','Please enter the field visit date');
			return false;
		}
		if(getNGValue('cmplx_fieldvisit_sp2_field_visit_date')!=""){//
			if(getNGValue('cmplx_fieldvisit_sp2_field_v_time')=="")	{
				showAlert("cmplx_fieldvisit_sp2_field_v_time","Please fill Field visit initiated time");
				return false;
			}
		 }
		 if(fieldvisit_flag_ini==true )
		 {
			 showAlert('cmplx_fieldvisit_sp2_field_v_time',"Please update field visit initiation time");
			 return false;  //FCU 3036
		 }
		 
		 
		 if(getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate')!=""){//
			if(getNGValue('cmplx_fieldvisit_sp2_field_visit_rec_time')=="")	{
				showAlert("cmplx_fieldvisit_sp2_field_visit_rec_time","Please fill Field visit received time");
				return false;
			}
		 }
		 if(getNGValue('cmplx_fieldvisit_sp2_field_v_time')!=""){
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
		 if(getNGValue('cmplx_fieldvisit_sp2_field_visit_rec_time')!="")	 {
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
		var d2 = getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate');
		var parts=d2.split('/');
		var date2=new Date(parts[2],parts[1]-1,parts[0]);
		var d =  getNGValue('cmplx_fieldvisit_sp2_field_visit_date');
		var parts = d.split('/');
		var date1=new Date(parts[2],parts[1]-1,parts[0]);
		if(date1>date2){
			 showAlert('cmplx_fieldvisit_sp2_field_rep_receivedDate','Field visit received date should be greater than field initiated date');
			 return false;
		}
		}
		else if(getNGValue('cmplx_fieldvisit_sp2_drop1')==''|| getNGValue('cmplx_fieldvisit_sp2_drop1')=='--Select--')
		{
			showAlert('cmplx_fieldvisit_sp2_drop1','Field Visit Requirement not Specified');
			return false;
		}
		/*else 
			// hritik 22.6.21 PCASI 3189
			setEnabled("fieldvisit_sp2_Frame1",true);*/
		return true;
	}
	if(pId=='SmartCheck1_Save'){
			return true;		    
		}
		
	if(pId=='Fpu_Grid_Button1'){
		Fpu_Grid_save_Button=true;
		return true;
	}
	if(pId=='Customer_FetchFirco'){
		flag_firco=true;
		var checkVal = confirm("Are you sure you have verified the personal details to initiate FIRCO?");
		if(checkVal==true){
			return true;
		}else {
			return false;
		}
	}
	if(pId=='Cust_ver_sp2_Button1'){
			if(checkCust())
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
	//PCASI-1086
	
	if( pId=='CustDetailVerification1_nationality_View' || pId=='ButtonOECD_State_View' || pId=='CardDispatchToButton_View'|| pId=='SecNationality_Button_View'|| pId=='Third_Nationality_Button_View'|| pId=='Nationality_Button_View'|| pId=='Designation_button3_View' || pId=='Designation_button4_View'|| pId=='Designation_button1'|| pId=='Designation_button2' || pId=='Designation_button5_View' || pId=='Designation_button6_View' ||pId=='Designation_button8_View'  || pId=='EMploymentDetails_Designation_button_View' || pId=='EMploymentDetails_DesignationAsPerVisa_button_View')
	{
	return true;
	}
	if(pId=='cmplx_cust_ver_sp2_ContactedOn')
	{
		if(getNGValue('cmplx_cust_ver_sp2_ContactedOn')=='Mismatch')
		{
			setEnabled('cmplx_cust_ver_sp2_Mobile',true);
			setLocked('cmplx_cust_ver_sp2_Mobile',false); // PCASI 3434
		}
		else{
			setEnabled('cmplx_cust_ver_sp2_Mobile',false);
		}
	}
	
	if(pId=='cmplx_Customer_Designation')
	{
	setNGValue('cmplx_EmploymentDetails_Designation',getNGValue('cmplx_cust_ver_sp2_ContactedOn'));
	return true;
	}
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

    if(pId=='checklist_ver_sp2_Button1'){  
					return true;
		}
    if(pId=='CustDetailVerification1_Button1')
	{
		return true;
	}
	if(pId=='exceptionalCase_sp2_Button2'){  
					return true;
		}
	if(pId=='GuarantorDetails_Button2'){

		if(getNGValue('GuarantorDetails_guardianCif')=='--Select--'){//name changed by akshay on 12/3/18 as decision was not loading
			showAlert('GuarantorDetails_guardianCif','Please Select Guarantor CIF Available');
			return false;
		}	
		else if(getNGValue('GuarantorDetails_guardianCif')=='Yes' || (getNGValue('GuarantorDetails_guardianCif')=='Same' && getNGValue('GuarantorDetails_cif')!='7777777')){//name changed by akshay on 12/3/18 as decision was not loading
			if(getNGValue('GuarantorDetails_cif')==''){
				showAlert('GuarantorDetails_cif','Please Enter Guarantor CIF');
				return false;
			}
		}
		else if(!checkMandatory(PL_GUARANTOR_FetchDetails)){
			return false;
		}
		flag_GuarantorDetails=true;
	
		return true;
	}
	else if(pId=='cmplx_Guarantror_GuarantorDet')
	{	
		setLockedCustom('GuarantorDetails_guardianCif',true);
		setLockedCustom('GuarantorDetails_cif',true);
	}
	if(pId=='CC_Creation_Validate_Skyward')
	{
		return true;
	}
//below code added by akshay on 26/11/18 for refer functionality       
if(pId=='DecisionHistory_ADD')
	{
		//++below code added by siva on 16102019 for PCAS-2696 CR
		if(activityName=='CAD_Analyst1' && (getNGValue('Eligibility_Trigger')=='Y' || getNGValue('Eligibility_Trigger')==''))
		{
			showAlert('ELigibiltyAndProductInfo_Button1',alerts_String_Map['VAL388']);
			return false;
		}
		//--above code added by siva on 16102019 for PCAS-2696 CR
		
		//below code added by siva on 04112019 for PCAS-1268
		if(activityName=='CPV' && getNGValue('cmplx_Decision_Decision')=='CPV Hold')//PCAS-1868 sagarika
		{
			if(getNGValue('cmplx_Decision_SetReminder')=="")
			{
			showAlert('cmplx_Decision_SetReminder',alerts_String_Map['VAL394']);
			return false;
			}
		}
		if((activityName=='CAD_Analyst1' || activityName=='CAD_Analyst2') && getNGValue('cmplx_Decision_Decision')=='CA_Hold')//PCAS-1868 sagarika
		{
			if(getNGValue('cmplx_Decision_SetReminder_CA')=="")
			{
				showAlert('cmplx_Decision_SetReminder_CA',alerts_String_Map['VAL394']);
				return false;
			}
		}
		if(activityName=='ToTeam')
		{
			var count =getLVWRowCount('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate')
			if(count>0)
			{
				setLocked('PostDisbursal_Frame5',false);
				setLocked('PostDisbursal_Frame4',false);
				setLocked('PostDisbursal_Frame3',false);
				setLocked('PostDisbursal_Frame6',false);
				setLocked('cmplx_PostDisbursal_Full_Funds_Released',false);
				setLocked('cmplx_PostDisbursal_Government_Company',false);
			}
		}
		if(activityName=='FCU'|| activityName=='FPU')
		{
			if(!checkMandatory(PL_Decision_FCU))
				return false;
		}
		if(activityName=='Disbursal_Checker'){
			if( getNGValue('cmplx_Decision_Decision')=='Approve' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1 ) {
				if(getNGValue("cmplx_Decision_DecDisbChecker")=="" && getNGValue("cmplx_Decision_DecDisbChecker")=="--Select--"){
					showAlert('cmplx_Decision_DecDisbChecker','Please Select Next Workstep');
					setNGValueCustom(pId,'');	
					return false;
				}
			}
		}
		else if(activityName=='Smart_CPV'){
			if(getNGValue('cmplx_Decision_SetReminder_Smart')=="" && getNGValue('cmplx_Decision_Decision')=='Smart CPV Hold')
			{
			showAlert('cmplx_Decision_SetReminder_Smart',alerts_String_Map['VAL394']);
			return false;
			}
			
			if(getNGValue('CPV_REQUIRED')=='Y')
			{
			showAlert('DecisionHistory_ADD','Please check main WI!');
			}
			//showAlert('DecisionHistory_ADD','Please check main WI!');

		}
		
		//code ended by siva on 04112019 for PCAS-1268
		
		if(checkMandatory_Add())
		{
				if(Add_Validate())
				{
					setLocked('cmplx_Decision_Decision',true);
					//flag_add_new=true; commented by saurabh1
					//console.log(flag_add_new);commented by saurabh1
					if(window.parent.stractivityName=='Original_Validation'){
						if(getNGValue('cmplx_Decision_Decision')=='Approve')
						{
							setNGValueCustom('ORIGINAL_VALIDATION','Yes');
						}
						else if(getNGValue('cmplx_Decision_Decision')=='Reject')
						{
							setNGValueCustom('ORIGINAL_VALIDATION','No');
						}
					}
					var userid = window.parent.userName;	
					//changed by nikhil for CAD1 only					
					if((getNGValue('cmplx_Decision_strength').indexOf(userid)==-1 || getNGValue('cmplx_Decision_strength')=='') && activityName=='CAD_Analyst1')
					{
						showAlert('cmplx_Decision_strength',alerts_String_Map['CC148']);
						return false;
					}
					//changed by nikhil for CAD1 only
					if((getNGValue('cmplx_Decision_weakness').indexOf(userid)==-1 || getNGValue('cmplx_Decision_weakness')=='') && activityName=='CAD_Analyst1' && getNGValue('EmploymentType')!='Self Employed')
					{
						showAlert('cmplx_Decision_weakness',alerts_String_Map['CC149']);
						return false;
					}
					return true;
				}					
		}
	
	}
	if(pId=='DecisionHistory_Delete')
	{	//by shweta
		if(delete_dec_row()){
			setLockedCustom('cmplx_Decision_Decision',false);
			if(window.parent.stractivityName=='Original_Validation'){
					setNGValueCustom('ORIGINAL_VALIDATION','');
				}
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
	if(activityName=='FCU'|| activityName=='FPU')
		{
			if(!checkMandatory(PL_Decision_FCU))
				return false;
		}
	else if(modify_row())
	{
		if(activityName=='CAD_Analyst1')
		{
			if(getNGValue('cmplx_Decision_Manual_Deviation')==true)
				{
					if(getNGValue('cmplx_Decision_Manual_deviation_reason')=='' )
						{
							showAlert('DecisionHistory_Button6',alerts_String_Map['CC146']);
							return false;
						}
				}
		}
	
		if(checkMandatory_Add())
		{
			
			setLocked('cmplx_Decision_Decision',true);
			
	
			if(window.parent.stractivityName=='Original_Validation'){
				if(getNGValue('cmplx_Decision_Decision')=='Approve')
				{
					setNGValueCustom('ORIGINAL_VALIDATION','Yes');
				}
				else if(getNGValue('cmplx_Decision_Decision')=='Reject')
				{
					setNGValueCustom('ORIGINAL_VALIDATION','No');
				}
			}
//by shweta
			//document.getElementById('DecisionHistory_Button4').click();//added by saurabh1
			flag_add_new=true;

	//console.log(flag_add_new);

			return true;
		}
	}
	
	else
	{	//console.log('test 4 for mod in else');
		return false;
	}

}
//by shweta
else if(pId=='RiskRating_CalButton'){
		if(isVisible('EMploymentDetails_Frame1')==false)
		{
			showAlert('EMploymentDetails_Frame1','Please Visit employment details First.');
			setLocked('RiskRating_CalButton',true);
			return false;			
		}
	
			
		if(checkMandatory(PL_RISK_RATING)) {
			riskScoreCalFlag=1;// value 1 means risk calculated
			return true;
		} 
		
		//setNGValueCustom('Is_RiskRate_Generated','Y');
		/*if(riskScoreCalFlag == '1')
		{
			
			generate_riskscore_pdf();
			return true;
		}*/
			
	}
	/*var ed=com.newgen.omniforms.formviewer.getNGFrameState("EmploymentDetails");
	if(ed!='0')
	{
		showAlert('EmploymentDetails','Please expand employment deatils');
		return false;*/
	


else if(pId=='RiskRating_Button1'){
	if(checkMandatory(PL_RISK_RATING)) {
		var totRiskRating = getNGValue('cmplx_RiskRating_Total_riskScore');
		if(totRiskRating =='' && riskScoreCalFlag==0)
		{
			showAlert('cmplx_RiskRating_Total_riskScore',alerts_String_Map['PL425']);
			return false;
		}
		return true;
	} 
}
else if (pId=='DecisionHistory_EFMS_Status')
{
return true;
}
else if(pId=='Decision_ListView1')
{
	setLocked('cmplx_Decision_Decision',false);
	var selectedRow_Decision=com.newgen.omniforms.formviewer.getNGListIndex('Decision_ListView1');
if(selectedRow_Decision!=-1)
{
var decision=getLVWAT('Decision_ListView1', selectedRow_Decision,3);
var Remarks=getLVWAT('Decision_ListView1', selectedRow_Decision,4);
var Decision_Reason=getLVWAT('Decision_ListView1', selectedRow_Decision,8);//added by shweta decision reason is at 8th index
var Decision_SubReason=getLVWAT('Decision_ListView1', selectedRow_Decision,13);//added by shweta decision reason is at 8th index

setNGValue("cmplx_Decision_Decision",decision);
setNGValue("cmplx_Decision_REMARKS",Remarks);
setNGValue("DecisionHistory_DecisionReasonCode",Decision_Reason);	
setNGValue("DecisionHistory_DecisionSubReason",Decision_SubReason);	
}
}

   
   
   if (activityName == 'DDVT_maker')
    {
	// disha FSD
        if ( pId=='Frame4' || pId == 'CustomerDetails' || pId == 'ProductContainer' || pId == 'IncomeDEtails' || pId == 'DecisionHistoryContainer' || pId == 'MiscFields' || pId ==
            'Address_Details_container' || pId == 'Supplementary_Container' || pId == 'FATCA' || pId == 'KYC' || pId == 'OECD' || pId ==
            'CustomerDocumentsContainer' || pId == 'OutgoingDocumentsContainer' || pId == 'DeferralContainer' || pId == 'DecisioningFields' || pId ==
            'DeviationHistoryContainer' || pId == 'CompanyDetails' || pId == 'PartnerDetails' || pId == 'DeferralDocuments' || pId == 'IncomingDocuments' ||
            pId == 'RepaymentSchedule' || pId == 'GuarantorDet' || pId == 'Self_Employed' || pId == 'InternalExternalLiability' || pId == 'LoanDetails' || pId ==
            'DecisionHistory' || pId == 'EligibilityAndProductInformation' || pId == 'Inc_Doc' || pId == 'EmploymentDetails' || pId == 'Alt_Contact_container' ||
            pId == 'FinacleCRM_Incidents' || pId == 'FinacleCRM_CustInfo' || pId == 'Finacle_Core' || pId == 'MOL' || pId == 'World_Check' || pId == 'Sal_Enq' ||
            pId == 'Reject_Enq' || pId == 'PartMatch_Search' || pId=='Details' || pId == 'ReferenceDetails' || pId == 'Credit_card_Enq' || pId == 'Case_History' || pId == 'LOS' || pId == 'Risk_Rating' || pId == 'Notepad_Values' || pId == 'Card_Details' || pId == 'External_Blacklist' || pId == 'Business_Verif' || pId == 'Supervisor_section' || pId == 'Supplementary_Card_Details' || pId == 'Credit_card_Enq1' || pId == 'Case_History1' || pId=='LOS1')
        {
			//change by saurabh on 8th Jan
        	var patmatch_flag = getNGValue('cmplx_PartMatch_partmatch_flag');
           if(pId == 'PartMatch_Search'){
					if(checkMandatory(PL_PartMatch))
					{	
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
						if(partCIF !='' && partCIF.length!=7)
						{
							showAlert('PartMatch_CIFID',alerts_String_Map['PL168']);
							return false;
						}
						
						setNGValue('cmplx_PartMatch_partmatch_flag','false');
						//setLocked('PartMatch_Search',true);
						return true;
					} else 
						{
						return false;//done by sagarika for PCSP 468
						}
				}
				else if((patmatch_flag == '' || patmatch_flag == null) && isLocked('Customer_save')==false)
            {
                showAlert('Part_Match', alerts_String_Map['PL169']);
				setNGFrameState(pId,1);
                return false;
            }else if((patmatch_flag=='false') && (isLocked('Customer_save')==false))
			{
				showAlert('PartMatch_Blacklist',"Please click Blacklist Check Button");
				com.newgen.omniforms.formviewer.setNGFocus('PartMatch_Blacklist');
				//setNGFrameState('ProductContainer',1);
				return false;
			}
			//  added on 20/5/21 PCASI-2671
			
			else if(pId == 'IncomeDEtails' && (getNGValue('Is_CustLock')=='N' ||getNGValue('Is_CustLock')=='' ) && (getNGValue('cmplx_Customer_NTB')!=true)) //pcasi3559
			{
				showAlert('PartMatch_CifLock',"CIF Lock is Mandatory");
				com.newgen.omniforms.formviewer.setNGFocus('PartMatch_CifLock');
				//setNGFrameState('ProductContainer',1);
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
       /*if (pId == 'Part_Match')
            return true;*/
		
	}
    else
    {
        //for optimization, such that on second time fragment click comboload function is not called again and again from java code
	// disha FSD
        if ( pId=='FPU_GRID' || pId=='Customer_Info_FPU' || pId == 'CustomerDetails' || pId=='Frame4' || pId == 'ProductContainer' || pId == 'IncomeDEtails' || pId == 'EmploymentDetails' || pId == 'DecisionHistoryContainer' ||
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
            pId == 'Part_Match' || pId == 'Reject_Enq' || pId == 'ReferenceDetails' || pId == 'Credit_card_Enq' || pId == 'Case_History' || pId == 'LOS' || pId == 'Risk_Rating' || pId == 'Notepad_Values' || pId == 'Card_Details' || pId == 'External_Blacklist' || pId =='Postdisbursal_Checklist' || pId == 'Supplementary_Card_Details' || pId == 'Credit_card_Enq1' || pId == 'Case_History1' || pId=='LOS1' || pId=='Details' || pId=='Customer_Details_Verification1' || pId=='Field_Visit_Initiated' || pId=='CheckList' || pId=='Exceptional_Case_Alert'|| pId=='Employment_Verification' || pId=='Smart_Check1' || pId=='Banking_Check')
        {
				if(activityName=='CSM' && pId=='Alt_Contact_container')
			{
				setVisible("AltContactDetails_Label6", false);
				setVisible("AlternateContactDetails_OfficeExt", false);
			}
			 if(pId=='DecisionHistory' )
			 {
					if(!checkMandatory_Frames(getNGValue('Mandatory_Frames'))){
					return false;
					}
					//below code added by siva on 20102019 for not triggering Integration calls each time on WI for already triggered Workitems
					/*if (activityName == 'CAD_Analyst1' || activityName == 'CAD_Analyst2')
					{
						if(getNGValue('Is_Financial_Summary') != 'Y')
						{
							showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
							return false;
						}
						if(getNGValue('IS_AECB') != 'Y')
						{
							showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
							return false;
						}
						 if((getNGValue('reEligibility_PL_counter') == '') || (getNGValue('reEligibility_PL_counter') == null) || (parseInt(getNGValue('reEligibility_PL_counter').split(';')[0])==0))
						 {
							showAlert(pId,'Please calculate Re-eligibility from EligibilityAndProductInformation');
							return false;				
						 }
					}*/
					//above code added by siva on 20102019 for not triggering Integration calls each time on WI for already triggered Workitems 
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
	var activityName = window.parent.stractivityName;
				
				if(activityName=='DDVT_maker')			
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
		//console.log(getLVWAT(pId,row,8));
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
			else if(prod_type=='Islamic'){
				docName = 'Welcome_Letter_Islamic_New';
			}
			
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
	
    
    
   //changes done by shweta jira# PCAS-1403  to save finacle customer information fragment on 01-10-2019 start
	else if(pId=='FinacleCRMCustInfo_Add'){
		//code added by bandana for sync start
		//Deepak Code added for PCAS-3564 
		for(var i=0;i<getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');i++)
					{
						if(getNGValue('FinacleCRMCustInfo_Text1')==getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0))
						{
						showAlert(pId,alerts_String_Map['CC262']);
						return false;
						}
					}
		//code added by bandana for sync end
		return true;
	}
     //changes done by shweta jira# PCAS-1403  to save finacle customer information fragment on 01-10-2019 end

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
	else if(pId=='Liability_New_SettlementFlag'){
		var setFlag=getNGValue("Liability_New_SettlementFlag");
		if(setFlag==true)
		{
			setNGValue("ExtLiability_Consdierforobligations", false);
		}
		else
		{
			setNGValue("ExtLiability_Consdierforobligations", true);				
		}
	}
	
	else if(pId=='ExtLiability_Consdierforobligations'){
		var coObli=getNGValue("ExtLiability_Consdierforobligations");
		if(coObli==true)
		{
			setNGValue("Liability_New_SettlementFlag", false);
		}
		else
		{
			setNGValue("Liability_New_SettlementFlag", true);				
		}
	}
	//Code start by bandana
	else if(pId=='cmplx_IncomingDocNew_IncomingDocGrid')
	{
		//Added by Imran for Documents Modifications
		//added by pooja for 3530
		if(window.parent.stractivityName!='Branch_Init' && window.parent.stractivityName!='DSA_CSO_Review' 
		&& window.parent.stractivityName!='DDVT_maker' &&  window.parent.stractivityName!='Original_Validation'
		&& window.parent.stractivityName!='Disbursal_Checker' &&  window.parent.stractivityName!='Document_Checker' 
		&&  window.parent.stractivityName!='Disbursal_Maker')
		{
			var rowSel = getNGListIndex(pId);
			if(rowSel>-1)
			{
				if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',rowSel,4)=='Received' || getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',rowSel,4)=='Waived')
				{
					setLockedCustom("IncomingDocNew_Status",true);
					setLockedCustom('IncomingDocNew_DocType',true);
					setLockedCustom('IncomingDocNew_DocName',true);
					//showAlert('','Cannot modify received document');
					return false
				}
			}
			else
			{
				setLockedCustom('IncomingDocNew_DocType',false);
				setLockedCustom('IncomingDocNew_DocName',false);
				setLockedCustom("IncomingDocNew_Status",false);
			}
		}
		else if(window.parent.stractivityName=='DDVT_maker')
		{
			var rowSel = getNGListIndex(pId);
			if(rowSel>-1)
			{
				if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',rowSel,4)=='Received')
				{
					setLockedCustom("IncomingDocNew_Status",true);
					setLockedCustom('IncomingDocNew_DocType',true);
					setLockedCustom('IncomingDocNew_DocName',true);
				}
				else
			{
				
				setLockedCustom("IncomingDocNew_Status",false);
			}
			}
				else
			{
				
				setLockedCustom("IncomingDocNew_Status",false);
			}
		
		}
		//Added by Pooja for 3530
		else if(window.parent.stractivityName =='Original_Validation' || window.parent.stractivityName=='Disbursal_Checker' || window.parent.stractivityName=='Document_Checker' || window.parent.stractivityName=='Disbursal_Maker')
		{
			var rowSel = getNGListIndex(pId);
			if(rowSel>-1){
			if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',rowSel,4)=='Received' || getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',rowSel,4)=='Waived'){
			setLockedCustom('IncomingDocNew_DocType',false);
			setLockedCustom('IncomingDocNew_DocName',false);
			setLockedCustom('IncomingDocNew_ExpiryDate',false);
			setLockedCustom('IncomingDocNew_Docindex',false);
			setLockedCustom('IncomingDocNew_DeferredUntilDate',false);
			setLockedCustom('IncomingDocNew_mandatory',false);
			setLockedCustom('IncomingDocNew_Status',false);

		}}}
		//end here
		else
		{
			setLockedCustom('IncomingDocNew_DocType',false);
			setLockedCustom('IncomingDocNew_DocName',false);
			setLockedCustom("IncomingDocNew_Status",false);
		}
		//Documents validation block finished.
		return true;
	}
	else if(pId=='IncomingDocNew_AddtoDMSbtn'){
			if(getNGValue('IncomingDocNew_DocName')!=''){
			
				addDocFromOD();
			}
			else{
			showAlert('IncomingDocNew_AddtoDMSbtn','Kindly choose Document Type and Name');
			return false;
			}
			
		}
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
				document.getElementById("ExtLiability_takeoverAMount").value = "";

			}
		}
		if(TakeOver==true)
		{
			setEnabled("ExtLiability_takeoverAMount", true);
			setLockedCustom("ExtLiability_takeoverAMount", false);
			setNGValue("ExtLiability_Consdierforobligations", false);
			
		}
		else
		{
			setNGValue("ExtLiability_Consdierforobligations", true);
			setNGValue("Liability_New_SettlementFlag", false);
			setEnabled("ExtLiability_takeoverAMount", false);
			setLockedCustom("ExtLiability_takeoverAMount", true);
			document.getElementById("ExtLiability_takeoverAMount").value = "";
		}
	}
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
		else if(pId=='IncomingDocNew_Modifybtn'){
			//One should not be able to modify already received/waived-off documents.
			//Modified if condition by Vaishvi for PCASI- 3501 for work steps ddvt-maker and disbursement-maker
			//Added by pooja for 3530
			if(window.parent.stractivityName!='Branch_Init' && window.parent.stractivityName!='DSA_CSO_Review' 
			&& window.parent.stractivityName!='DDVT_maker' && window.parent.stractivityName!='Disbursal_Maker'
			&& window.parent.stractivityName!='RM_Review' && window.parent.stractivityName!='Original_Validation' 
			&& window.parent.stractivityName!='Disbursal_Checker'&& window.parent.stractivityName!='Document_Checker' ){
				var rows = getLVWRowCount('cmplx_IncomingDocNew_IncomingDocGrid');
				var incomingdoc= getNGValue('IncomingDocNew_DocType');
				for(var i=0;i<rows;i++){
					if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,0)==incomingdoc)
					{
						if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,4)=='Received')
						{
							showAlert('','Cannot modify received document');
							return false
						}
						else if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,4)=='Waived')
						{
							showAlert('','Cannot modify waived-off document');
							return false
						}
						else if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,4)=='Pending' || getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,4)=='Deferred')
						{
							
						}
					}
				}
			}
			//adding finished for docs already received
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
	
	//Code end by bandana
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
	else if(pId=='FinacleCore_Button2'){//added by shweta for seq 105
		
		if(checkMandatory(PL_FINACLECORE_DDS))
		{	var new_value = 'Finacle_Core';
		var field_string_value = getNGValue('FrameName');
		if(field_string_value.indexOf(new_value)==-1){
			var newString = field_string_value + new_value+',';
			setNGValueCustom('FrameName',newString);
		}
			
		removeFrame(pId);
		return true;}
	}
		
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
			if(updCustFlag!='Y'){
				setEnabledCustom('CC_Creation_Update_Customer',true);
				//setEnabledCustom('CC_Creation_Button2',false);
				//setEnabledCustom('CC_Creation_CAPS',false);
			}
			if(/*updCustFlag=='Y' &&*/ cardCreationFlag!='Y'){
				setEnabledCustom('CC_Creation_Update_Customer',true);
				setEnabledCustom('CC_Creation_Button2',true);
				setEnabledCustom('CC_Creation_CAPS',false);
			}
			if(/*updCustFlag=='Y' &&*/ cardCreationFlag=='Y' && capsFlag!='Y'){
				setEnabledCustom('CC_Creation_Update_Customer',true);
				setEnabledCustom('CC_Creation_Button2',false);
				setEnabledCustom('CC_Creation_CAPS',true);
			}
			if(cardCreationFlag=='Y'){
				setNGValueCustom('CC_Creation_CardCreated','Y');
			}
			if(capsFlag=='Y'){
				setNGValueCustom('CC_Creation_TransToCAPS','Y');
			}
			if("Supplement"==Cust_type && PassNo==getNGValue('cmplx_Customer_PAssportNo'))
			{
				setEnabledCustom('CC_Creation_Update_Customer',false);
			}
			
		}
		else{
			//setEnabled('CC_Creation_Update_Customer',false);
			setEnabledCustom('CC_Creation_Update_Customer',false);
			setEnabled('CC_Creation_Button2',false);
			setEnabled('CC_Creation_CAPS',false);
		}
	}	
	//Shivang falcon bau-to enable skyward number on ddvt
	else if(pId == 'SupplementCardDetails_cmplx_SupplementGrid'){
			var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid');
			if(selectedRow==-1)
			{
				return true;
			}
			var cardProduct = getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',selectedRow,30);
			var Passport= getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',selectedRow,3);
			if(cardProduct.indexOf('EKTMC')>-1 || cardProduct.indexOf('EKWEC')>-1 || cardProduct.indexOf('EKWEI')>-1){
				setVisible("SupplementCardDetails_Label40",true);
				setVisible("SupplementCardDetails_Text2",true);
				setEnabled("SupplementCardDetails_Text2",true);
				if(activityName == 'DDVT_Maker'){
						setLockedCustom("SupplementCardDetails_Text2",false);
				}
				else{
					setLockedCustom("SupplementCardDetails_Text2",true);
				}
			}	
			//setNGValue('SupplementCardDetails_Old_passport',Passport);
			
	}
	//prabhakar
	else if(pId=='cmplx_Customer_SecNAtionApplicable_1')
		{
			setNGValue('cmplx_Customer_SecNationality',"");
			setLocked('cmplx_Customer_SecNationality',true);
			setLocked('SecNationality_Button',true); //hritik

		}
		
		else if(pId=='cmplx_Customer_SecNAtionApplicable_0'){
			setLocked('cmplx_Customer_SecNationality',false);
			setNGValue('cmplx_Customer_SecNationality',"");
			setLocked('SecNationality_Button',false); //hritik
			//setEnabled('cmplx_Customer_SecNationality',true);
        }
		else if(pId=='cmplx_Customer_Third_Nationaity_Applicable_1')
		{
			setNGValue('cmplx_Customer_Third_Nationaity',"");
			setLocked('cmplx_Customer_Third_Nationaity',true);
			

		}
		
		else if(pId=='cmplx_Customer_Third_Nationaity_Applicable_0'){
			setLocked('cmplx_Customer_Third_Nationaity',false);
			setNGValue('cmplx_Customer_Third_Nationaity',"");
			//setEnabled('cmplx_Customer_Third_Nationaity',true);
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
	else if(pId=='OriginalValidation_Save')
	{
	return true;
	}
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
	//Button Id was wrong  
	else if (pId=='EMploymentDetails_Designation_button'|| pId=='EMploymentDetails_DesignationAsPerVisa_button'|| pId=='EMploymentDetails_FreeZone_Button')
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
	//Added by aman 08062018
	else if(pId=='cmplx_EmploymentDetails_JobConfirmed' || pId=='cmplx_EmploymentDetails_Freezone' ||   pId=='FinacleCore_CheckBox1' || pId=='cmplx_EmploymentDetails_RegPayment' || pId=='cmplx_EmploymentDetails_MinimumWait' || pId=='Nationality_Button' || pId=='SecNationality_Button'|| pId=='Third_Nationality_Button' || pId=='Customer_Button2'|| pId=='BirthCountry_Button'|| pId=='ResidentCountry_Button' || pId=='AddressDetails_Button1'|| pId=='Nationality_ButtonPartMatch'|| pId=='MOL_Nationality_Button'|| pId=='CardDetails_bankName_Button'|| pId=='EmploymentDetails_Bank_Button')
	{
		//++below code added by siva on 16102019 for PCAS-2696 CR
		setNGValueCustom('Eligibility_Trigger','Y');
		//--above code added by siva on 16102019 for PCAS-2696 CR
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
			
	else if(pId=='ExtLiability_Button1'){
			//Code changes done to save AECB data
			var new_value = 'ExtLiability_Frame1';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			if(AccountSummary_checkMandatory())
			{
				var activityName = window.parent.stractivityName;
				
				if(activityName=='DDVT_maker')
					setNGValue('IS_AECB','D');
				else
					setNGValue('IS_AECB','Y');
				setNGValueCustom('Liability_New_overwrite_flag','N');//by jahnavi for 3626
				//++below code added by siva on 16102019 for PCAS-2696 CR
				setNGValueCustom('Eligibility_Trigger','Y');
				//--above code added by siva on 16102019 for PCAS-2696 CR
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
					var guarantor_CIF = ""; // by bandana : guarantor_CIF having no value since defined within if block.
					if(isVisible('GuarantorDetails_Frame1')==true){	
						guarantor_CIF=	getLVWAT('cmplx_Guarantror_GuarantorDet',0,1);
					}
					
					if((getNGValue('cmplx_Customer_NTB')==false) || (getNGValue('cmplx_Customer_NTB')==true && ((company_cif!='' && company_cif!=null) || guarantor_CIF!='')))
					{
						request_name = 'InternalExposure,ExternalExposure';
					}
					else{
						request_name = 'ExternalExposure';
					}
				
					var cifToBePassed="";
					if(getNGValue('cmplx_Customer_NTB')==true){
						cifToBePassed = "";	
					}
					else{
						cifToBePassed = getNGValue('cmplx_Customer_CIFNO');	
					}
					var param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_Customer_FIrstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LAstNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_MobNo":"'+getNGValue('cmplx_Customer_MobNo')+'","cmplx_Customer_gender":"'+gender+'","OverridePeriod":"'+OverridePeriod+'","wi_name":"'+wi_name_aecb+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","TxnAmount":"'+TxnAmount+'","NoOfInstallments":"'+NoOfInstallments+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_Passport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PAssport4')+'","cmplx_Username":"deepak","cmplx_Customer_CIFNO":"'+cifToBePassed+'","cif":"'+company_cif+'","trade_comp_name":"'+trade_comp_name+'","trade_lic_no":"'+trade_lic_no+'","trade_lic_place":"'+trade_lic_place+'","account_no":"'+getNGValue('Account_Number')+'","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_EmiratesID')+'","cmplx_EligibilityAndProductInfo_NumberOfInstallment":"5","cmplx_Customer_short_name":"'+getNGValue('cmplx_Customer_short_name')+'","full_name":"'+full_name+'","cmplx_Customer_NTB":"'+getNGValue('cmplx_Customer_NTB')+'","Individual_Aecb":"Y","Corporate_Aecb":"N"}');

				
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
			var activityName = window.parent.stractivityName;

			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('NEW')>-1)
			{
				setEnabled('ExtLiability_Takeoverindicator',false);
			}
			else{
				setEnabled('ExtLiability_Takeoverindicator',true);
			}
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1 && (initiation_uw.indexOf(activityName)>-1))
			{
				setEnabled('ExtLiability_Takeoverindicator',true);
				setNGValue('ExtLiability_takeoverAMount','');
			}
			else{
				setEnabled('ExtLiability_Takeoverindicator',true);
			}
			setLocked("ExtLiability_QCAmt", true);
			setLocked("ExtLiability_QCEMI", true);
			setLocked("ExtLiability_takeoverAMount", true);
			if(activityName=='CPV'){
				setEnabled('ExtLiability_Takeoverindicator',false); //PCASO 3564
			}
			if(activityName=='CAD_Analyst2'){
				setEnabled('ExtLiability_Takeoverindicator',false); //PCASI 3718
			}
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
		
		else if(pId=='cmplx_CardDetails_securitycheck'){
		var val=getNGValue("cmplx_CardDetails_securitycheck");
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
		  if(checkMandatory_2ndgridcarddetails()){
			  var new_value = 'Card_Details';
				var field_string_value = getNGValue('FrameName');
				if(field_string_value.indexOf(new_value)==-1){
					var newString = field_string_value + new_value+',';
					setNGValueCustom('FrameName',newString);
				}
				return true;	
			}
		}
		else if(pId=='ReferHistory_save')
		{
		return true;
		}
	else if(pId=='CardDetails_delete')
	{
	  return true;	
	}

	else if(pId=='IncomeDetails_FinacialSummarySelf')
		{	 var activityName = window.parent.stractivityName;
			
			//alert("inside financial summary");
			//24/07/2017 Liabilities should be Fetched First till it gets the status as Success
			if((getNGValue('IS_AECB')==null || getNGValue('IS_AECB') == '') )
				{
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
			}
			if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true)
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}//jahnavi for 3626
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
				
				var param_json=('{"cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNO')+'","sub_product":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)+'","emp_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)+'","application_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4)+'"}');
				
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
				//++below code added by siva on 16102019 for PCAS-2696 CR
				setNGValueCustom('Eligibility_Trigger','Y');
				//--above code added by siva on 16102019 for PCAS-2696 CR
					return true;
			}
	

    //tanshu ddvt checker
    else if (pId == 'DecisionHistory_updcust')
    {
       // alert("inside update account request");
	   if(isVisible('LoanDetails_Frame3')==false)
			{
				showAlert('LoanDetails_Frame3','Please Visit Disbursal details section under loan details');
				return false;
			}
	   if(getNGValue('Is_ACCOUNT_MAINTENANCE_REQ')=='Y')
			{
				showAlert('DecisionHistory_updcust', 'Account Update is already done!!!');
				return false;
			}//hritik 3865
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
		if(getNGValue('cmplx_Decision_Decision')=='' || getNGValue('cmplx_Decision_Decision')=='--Select--'){
    		showAlert('cmplx_Decision_Decision','Please take decision as Approve!')
    		return false;
    	} //hritik 3680 - dec empty need to take dec as app
		
		/*var primary_index=0;
		var cifupdate='N';
		for(var i=0;i<getLVWRowCount('cmplx_Decision_MultipleApplicantsGrid');i++)
		{
		
			if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',i,0)=='Primary')
			{
			primary_index=i;
			if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',primary_index,7)=='Y' || cif_update_flag=='Y')
				{
					cifupdate='Y';
				}
			}
		} */
		/*for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
		{
			if(getLVWAT('Decision_ListView1',i,3)=='Refer' && getLVWAT('Decision_ListView1',i,2)=='DDVT_Checker' && cifupdate=='Y')
			{
				showAlert('cmplx_Decision_Decision', 'CIf update is already done, Please change the decision to Approve');
				return false;
			}
		} hritik pcasi3680 */
		
		cif_update_flag='Y';
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
		if((getNGValue('Is_CHEQUE_BOOK_ELIGIBILITY')=='Y' || getNGValue('Is_NEW_CARD_REQ')=='Y')
				&&(getNGValue('cmplx_Customer_NTB') == true))
			{
				showAlert('DecisionHistory_chqbook', 'Cheque book/Debit Card Request is already done!');
				return false;
			}//hritik 3865
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
		if(getNGListIndex('cmplx_Decision_MultipleApplicantsGrid')==-1)
			{
				showAlert('','Please select the same Customer Type from Applicants Grid in Decision tab!!!');
				return false;
			}
		var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_Decision_MultipleApplicantsGrid');
		if(selectedRow != -1)
		{
				val1=getLVWAT('cmplx_Decision_MultipleApplicantsGrid',selectedRow,1)
		}

		var selectedRow_1 = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_CCCreation_cmplx_CCCreationGrid');
		if(selectedRow_1 != -1)
		{
				val2=getLVWAT('cmplx_CCCreation_cmplx_CCCreationGrid',selectedRow_1,3)
		}
        if( val1.toLowerCase != val2.toLowerCase)
        {
				showAlert('','Please select the same Customer Type from Applicants Grid in Decision tab!!!');
				return false;
		} //hritik;
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
			if(Customer_Save_Check1())
			{
			removeFrame(pId);
			return true;}
			else
			return false;
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
				var new_value = 'LoanDetails';
				var field_string_value = getNGValue('FrameName');
				if(field_string_value.indexOf(new_value)==-1){
					var newString = field_string_value + new_value+',';
					setNGValueCustom('FrameName',newString);
				}
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
			{
				removeFrame(pId);
				return true;}
		}
		//below code added by nikhil 29/12/17
		else if(pId=='SupplementCardDetails_Add')
		{
			if(checkMandatory_SupplementGrid()){
				if(validateSuppEntry('add')){
					
					var age=calcAge(getNGValue('SupplementCardDetails_DOB'),'');
					// Changed by Rajan for PCASP-1427
					if(getNGValue("SupplementCardDetails_Relationship")=='MOT' ||getNGValue("SupplementCardDetails_Relationship")=='FAT'||getNGValue("SupplementCardDetails_Relationship")=='SON'||
							getNGValue("SupplementCardDetails_Relationship")=='BRO' || getNGValue("SupplementCardDetails_Relationship")=='DAU' ||getNGValue("SupplementCardDetails_Relationship")=='Relative'||
							getNGValue("SupplementCardDetails_Relationship")=='HUS'||getNGValue("SupplementCardDetails_Relationship")=='WIF' ||getNGValue("SupplementCardDetails_Relationship")=='SIS' ||getNGValue("SupplementCardDetails_Relationship")=='PAR')
					{
						if(age<15){
									showAlert(pId,'Minor Customer not Applicable for Credit Card');
									//setNGValueCustom(pId,'');
									return false;
								  }
					}
					else if(getNGValue("SupplementCardDetails_Relationship")!='MOT' ||getNGValue("SupplementCardDetails_Relationship")!='FAT'||getNGValue("SupplementCardDetails_Relationship")!='SON'||
							getNGValue("SupplementCardDetails_Relationship")!='BRO' || getNGValue("SupplementCardDetails_Relationship")!='DAU' ||getNGValue("SupplementCardDetails_Relationship")!='Relative'||
							getNGValue("SupplementCardDetails_Relationship")!='HUS'|| getNGValue("SupplementCardDetails_Relationship")!='WIF'  ||getNGValue("SupplementCardDetails_Relationship")!='SIS' || getNGValue("SupplementCardDetails_Relationship")!='PAR')
					{
						if(age<21){
									showAlert(pId,'Minor Customer not Applicable for Credit Card');
									//setNGValueCustom(pId,'');
									return false;
								  }
					}
					if (getLVWRowCount('cmplx_CC_Loan_cmplx_btc')>30 ){
						showAlert('cmplx_CC_Loan_cmplx_btc',alerts_String_Map['VAL376']);
					}
					else{
					return true;
					}
				}
		}		
		}	
		else if(pId=='IncomeDetails_RefreshFinacle')
		{
			//To refresh the finacle core data
			return true;
		}
				// Changed by aman for CR 1112
		else if(pId=='SupplementCardDetails_Modify')
		{
		 if(checkMandatory_SupplementGrid()){
				if(validateSuppEntry('modify')){
					var age=calcAge(getNGValue('SupplementCardDetails_DOB'),'');
					// Changed by Rajan for PCASP-1427
					if(getNGValue("SupplementCardDetails_Relationship")=='MOT' ||getNGValue("SupplementCardDetails_Relationship")=='FAT'||getNGValue("SupplementCardDetails_Relationship")=='SON'||
							getNGValue("SupplementCardDetails_Relationship")=='BRO' || getNGValue("SupplementCardDetails_Relationship")=='DAU' ||getNGValue("SupplementCardDetails_Relationship")=='Relative'||
							getNGValue("SupplementCardDetails_Relationship")=='HUS'||getNGValue("SupplementCardDetails_Relationship")=='WIF' ||getNGValue("SupplementCardDetails_Relationship")=='SIS' ||getNGValue("SupplementCardDetails_Relationship")=='PAR')
					{
						if(age<15){
									showAlert(pId,'Minor Customer not Applicable for Credit Card');
									//setNGValueCustom(pId,'');
									return false;
								  }
					}
					else if(getNGValue("SupplementCardDetails_Relationship")!='MOT' ||getNGValue("SupplementCardDetails_Relationship")!='FAT'||getNGValue("SupplementCardDetails_Relationship")!='SON'||
							getNGValue("SupplementCardDetails_Relationship")!='BRO' || getNGValue("SupplementCardDetails_Relationship")!='DAU' ||getNGValue("SupplementCardDetails_Relationship")!='Relative'||
							getNGValue("SupplementCardDetails_Relationship")!='HUS'|| getNGValue("SupplementCardDetails_Relationship")!='WIF'  ||getNGValue("SupplementCardDetails_Relationship")!='SIS' || getNGValue("SupplementCardDetails_Relationship")!='PAR')
					{
						if(age<21){
									showAlert(pId,'Minor Customer not Applicable for Credit Card');
									//setNGValueCustom(pId,'');
									return false;
								  }
					}
				
					if (getLVWRowCount('cmplx_CC_Loan_cmplx_btc')>30 ){
						showAlert('cmplx_CC_Loan_cmplx_btc',alerts_String_Map['VAL376']);
					}
					else{
					return true;
					}
				return true;
				
				}
		 	}
		 }
			
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
		else if(pId=='FATCA_Add')
		{
			if(FATCA_Save_Check('add'))
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
		else if( pId=='FATCA_Modify')
		{
			if(FATCA_Save_Check('Modify'))
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
			if(getNGValue('cmplx_Customer_VIPFlag')==true)
			{
				removeFrame(pId);
					return true;
			}
			if(Income_Save_Check() )
			{
				removeFrame(pId);
			return true;
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
		//++below code added by shweta for Self-Supp CR
		 if(!check_Primary_Disbursal())
		 {
			 return false;
		 }
		 //--above code added by shweta for Self-Supp CR
		//alert("inside New Card Request");
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
       if(getNGValue('EMploymentDetails_Text21')!='' || getNGValue('cmplx_EmploymentDetails_EMpCode')!=''){	// Imran Changed from EMploymentDetails_Text22 to cmplx_EmploymentDetails_EMpCode
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
		if(activityName=='CAD_Analyst1')
		{
			 dectech_click_reminder=false;
			 setNGValue('LoanLabel',getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit'));
			 if(getNGValue('cmplx_Decision_Decision')=='Reject')
				 setLocked('cmplx_Decision_ReferTo',true);
		}
	    if (getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')=='')
		{
			showAlert('','Final Limit Cannot be Blank!');
			return false;
		}
	    var mandatory_frame = getNGValue('Mandatory_Frames');
	    if(!checkMandatory_Frames(mandatory_frame))
		{
	    	return false;
		}
	    var newAddedrow=0;
		for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
		{
		if(getLVWAT('Decision_ListView1',i,12)=='')
		{
			newAddedrow++;
		}		
		}
		if(newAddedrow>0)
		{
			showAlert('cmplx_Decision_Decision',alerts_String_Map['PL421']);
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
				if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true) //jahnavi for 3626
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}
			}
			//below code added by bandana for PCASI-3459
			if(getNGValue('IS_AECB')!='Y'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
			if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N' && getNGValue('cmplx_Customer_NTB')!=true)
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false; //jahnavi for 3626
					}
			//below code added by bandana for PCASI-3459 ends
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
					//CustomSaveJsp();
				}
				var new_value = 'DecisionHistory,IncomeDetails';
				var field_string_value = getNGValue('FrameName');
				if(field_string_value.indexOf(new_value)==-1){
					var newString = field_string_value + new_value+',';
					setNGValueCustom('FrameName',newString);
				}
				//++below code added by siva on 16102019 for PCAS-2696 CR
				setNGValueCustom('Eligibility_Trigger','N');
				//--above code added by siva on 16102019 for PCAS-2696 CR
				return true;
			}
    }
  else  if (pId == 'CustDetailVerification1_Button2')
    {
		var CompName=getNGValue("CustDetailVerification1_Compname");
		var CompCif=getNGValue("CustDetailVerification1_Compcode");
		var EmiratesID=getNGValue("cmplx_CustDetailverification1_EmiratesId");
		var CIFID=getNGValue("CustDetailVerification1_Text8");
		if(CompName!='' || CompCif!=''  || EmiratesID!='' || CIFID!='')
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
				return true;
			}
		else if(pId=='KYC_Add')
		{	//hritik 23.6.21 PCASI-3502 (prev added this to function kyc-add-check causing issue for modify)
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			var CustType=getNGValue("KYC_CustomerType");
		    var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,3));
			}
				for(var i=0;i<gridValue.length;i++)
				{
					if(gridValue.indexOf(CustType)>-1)
					{
						showAlert('KYC_CustomerType','KYC Already Added for Customer '+CustType);
						return false;
						break;
					}
				}
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
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			//var custTypePickList = document.getElementById("KYC_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			
			
			for(var i=0;i<n;i++)
			{
				if(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0)=='Y' && getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,4)=='')
				{
					showAlert('',"KYC review date cannot be blank for any row where kYC held is Yes");
					return false;
				}
			}
			if(checkMandatory_KYC())
			{	
				for(var i=0;i<n;i++)
			{
				if(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0)=='Y' && getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,4)=='')
				{
					showAlert('',"KYC review date cannot be blank for any row");
					return false;
				}  //by jahnavi for 2698
			}
				return true;
			}
			
			
		
		}
		else if(pId=="FATCA_modify")
		{
			return true;
		}
		
		
		else if(pId=='cmplx_OECD_cmplx_GR_OecdDetails'){
			var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_OECD_cmplx_GR_OecdDetails');
			if(selectedRow != -1){
				if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,5)!=null && getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,5)!=""){
					setEnabled('OECD_CRSFlagReason',true);
				}
			}
		}
		
		else if(pId=='OECD_add')
		{	
			if(checkMandatory_OecdGrid('Add'))
			return true;
		}
		else if(pId=='OECD_modify')
		{
			//if(checkMandatory_OecdGrid('Modify'))
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
			check_bt_modify=false;
			if(checkMandatory_BTGrid()){
				if(validate_BTGrid())
					return true;
			}
			
		}
		else if(pId=='BT_Modify'){
			check_bt_modify=true;
			if(checkMandatory_BTGrid()){
				//Changed by Shivang for PCASP-1445
				if(validate_BTGrid_Modify())
					return true;
			}	
		}
		
		else if(pId=='BT_Delete'){
			return true;
		}
		
		else if(pId=='BTC_save'){
			check_bt_modify=false;
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
		
		//code by bandana starts
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
				setLockedCustom("cmplx_CC_Loan_DDSEntityNo",false);
				setLockedCustom("DDS_save",false);

			}
		}	

		//code added by bandana ends
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
			var activityName = window.parent.stractivityName;
			//if(checkMandatory(PL_DECISION))
			//{
				// disha FSD
			if(activityName=='Reject_Queue'){
				var Decision=getNGValue("cmplx_Decision_Decision");
				var Remarks=getNGValue("cmplx_Decision_RejectReason");
				if(Decision==""||Decision=="--Select--"){
					showAlert('cmplx_Decision_Decision',alerts_String_Map['CC176']);
					return false;
				}
				if(Remarks==""){
					showAlert('cmplx_Decision_RejectReason',alerts_String_Map['CC155']);
					return false;
				}
				else{
					return true;
				}
			}
			if(activityName=='Original_Validation'){
				
					
				var Decision=getNGValue("cmplx_Decision_Decision");
				
				if(Decision==""||Decision=="--Select--"){
					showAlert('cmplx_Decision_Decision',alerts_String_Map['CC176']);
					return false;
				}
				/*if(Decision=='Approve') //jahnavi
				{
					if(getLVWAT("cmplx_LoanDetails_cmplx_LoanGrid",0,2)!=getLVWAT("cmplx_LoanDetails_cmplx_LoanGrid",0,3))
					{
						showAlert('',"Payment release date should be current date/disbursal date");
						console.log("Test");
						//setNGValue("LoanDetails_payreldate",getNGValue("LoanDetails_disbdate"));
						return false;
					}
				}*/
				if(Decision=="REJECT"){
					var DecisionResnCode=getNGValue("DecisionHistory_DecisionReasonCode");
					if(DecisionResnCode==""||DecisionResnCode=="--Select--"){
		
		showAlert('DecisionHistory_DecisionReasonCode',alerts_String_Map['CC177']);
						return false;
					}
					return true;
				}
				else{
					return true;
				}
				
			}
				if (activityName == 'CAD_Analyst1')
				{
					 //validation for customer detail verification CPV changes 16-04 //Changed on 26/5/21 - CPV Changes Req 1 
					 /* 
					if ((getNGValue('cmplx_CustDetailVerification_Decision') == '' || getNGValue('cmplx_CustDetailVerification_Decision') == '--Select--') && getNGValue("cpv_required")!='N' )//by shweta
						{
							showAlert('cmplx_CustDetailVerification_Decision', alerts_String_Map['CC165']);
							return false;
						}
					*/	
					var decision=getNGValue("cmplx_Decision_Decision");
					if(decision=="REJECT")
					{
						var Reason_code=getNGValue("DecisionHistory_DecisionReasonCode");
						if(Reason_code==""||Reason_code=="--Select--"){
							showAlert('DecisionHistory_DecisionReasonCode',alerts_String_Map['CC178']);
							return false;
						}
						
					}
					if(decision=="Refer to Credit")
					{
						var cad_decision=getNGValue("cmplx_Decision_CADDecisiontray");

						if(cad_decision==""||cad_decision=="--Select--"){
							showAlert('cad_decision',alerts_String_Map['CC175']);
							return false;
						}
					}
					// disha Manual deviaton reason mandatory if tick box checked
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
			// ++ above code already present - 06-10-2017 validation for OV and CAD1
					else{
						if(!checkMandatory(PL_DECISION))
						return false;
					}
				}
			
				removeFrame(pId);
				return true;
				//}
			
		}
	// disha FSD
	else if(pId=='cmplx_Decision_Manual_Deviation')
	{
		var new_value = 'DecisionHistory';
		var field_string_value = getNGValue('FrameName');
		if(field_string_value.indexOf(new_value)==-1){
			var newString = field_string_value + new_value+',';
			setNGValueCustom('FrameName',newString);
		}

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
	//to update manual deviation if selected after taking decision
		var newAddedrow=0;
		for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
		{
			if(getLVWAT('Decision_ListView1',i,12)=='')
			{
				newAddedrow++;
			}		
		}
		if(newAddedrow>0)
		{
			showAlert(pId,alerts_String_Map['CAS025']);
			if(pId=='cmplx_Decision_Manual_Deviation' ){
				if(getNGValue('cmplx_Decision_Manual_Deviation')==true){
					setNGValueCustom('cmplx_Decision_Manual_Deviation','false');
				}
				else{
					setNGValueCustom('cmplx_Decision_Manual_Deviation','true');
				}
			}
			return false;
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
    //changed by shweta
		else if(pId=='DecisionHistory_Button6')
		{ //Manual deviation button
			var new_value = 'DecisionHistory';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
			}
			var deviationReason = getNGValue('cmplx_Decision_Manual_deviation_reason');
			//changed by nikhil 31/10/18 for manual deviation opening
			var url='/webdesktop/custom/CustomJSP/Manual_Deviation_Reasons.jsp?ProcessName=PersonalLoanS&sessionId='+window.parent.sessionId+'&deviationReason=' +deviationReason;
			var returnValue='';
			window.open_(url, "", "scrollbars=yes,resizable=yes,width=500px,height=700px");

			//Deepak Changes done for PCAS-3464 on 16 Oct
			if(getNGValue('cmplx_Decision_Manual_deviation_reason')!=returnValue){
				setNGValueCustom('Eligibility_Trigger','Y');
			}
			//setNGValueCustom('cmplx_Decision_Manual_deviation_reason',returnValue)
			return true;
			//var url="/formviewer/resources/scripts/Manual_Deviation_Reasons.jsp?ProcessName=PersonalLoanS";
			//window.open_(url, "", "width=500,height=500");
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

		var interest_type = getNGValue('cmplx_LoanDetails_inttype');
		if(interest_type == '' || interest_type == null)
		{
			showAlert('cmplx_LoanDetails_inttype', alerts_String_Map['PL197']);
			return false;
	}
		
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
		var moratorium = getNGValue('cmplx_LoanDetails_moratorium');//PCASI-1050
		if((moratorium == '' || moratorium == null) && activityName=='DDVT_maker')
		{
			showAlert('cmplx_LoanDetails_moratorium', 'Please Fill Moratorium');
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
		var scheme=getLVWAT("cmplx_Product_cmplx_ProductGrid",0,7);
		var Query = "schemecode";
	var wparams="";
wparams="schemeid=="+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,7);//by shweta
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params1="Query="+Query+"&wparams="+wparams+"&pname="+"PLjs";
var schemedesc = CallAjax(jspName,params1);
var fields1 = schemedesc.split('#');
if(fields1[0].indexOf("ERROR")>-1 || fields1[0].indexOf("NODATAFOUND")>-1)
{
	fields1[0]=getLVWAT("cmplx_Product_cmplx_ProductGrid",0,7);
}
		var funding_acc="";
		var funding_status="";
		var schemecode="";
		schemecode=fields1[0];
		var Emi_val = getNGValue('cmplx_LoanDetails_loanemi');
		var result= getDataFromDB('PL_AcctId',"Parent_Wi_Name=="+getNGValue('Parent_WIName')+"~~Wi_Name=="+getNGValue('PL_wi_name'),'PLjs').split('#~')[0] ;
		if(result.trim()!='NODATAFOUND' && result.indexOf("FAIL")==-1){
			var url_n = result.split('#');	
			funding_status=url_n[1];
			funding_acc=url_n[0];	
		}	
		else{
			//var query = "select distinct New_Account_Number from ng_rlos_decisionHistory where wi_name ='"+getNGValue('Parent_WIName')+"' or wi_name ='"+getNGValue('PL_wi_name')+"'";
				funding_acc = getDataFromDB('PL_AcctNo',"Parent_Wi_Name=="+getNGValue('Parent_WIName')+"~~Wi_Name=="+getNGValue('PL_wi_name'),'PLjs').split('#')[0] ;
				if(funding_acc.trim()!='' ){
					funding_status='Active';
				}else {
					funding_status="";
					funding_acc="";

				}
		}
		
        //var url="/formviewer/custom/CustomJSP/Generate_Schedule.jsp";
        //var url='/formviewer/custom/CustomJSP/Generate_Schedule.jsp?repayment_Frequency=' + repayment_Frequency + '&repayment_Mode='+ repayment_Mode+ '&dda_Refer_no=' + dda_Refer_no+ '&dda_Status='+ dda_Status+ '&interest_Rate=' +interest_Rate+ '&tenure=' +tenure + '&loanAmount=' +loanAmount;
        var url = '/webdesktop/custom/CustomJSP/Generate_Schedule.jsp?repayment_Frequency=' + repayment_Frequency + '&interest_Rate=' + interest_Rate +
            '&tenure=' + tenure + '&loanAmount=' + loanAmount + '&disbursementDate=' + disbursementDate + '&firstEMIdate=' + firstEMIdate + '&funding_acc=' + funding_acc +  '&funding_status=' + funding_status + '&installment_plan=' + installment_plan + '&wi_name=' + wi_name + '&ws_name=' + ws_name+ '&dda_Refer_no=' + dda_Refer_no + '&dda_Status=' + dda_Status + '&repayment_Mode=' + repayment_Mode +'&Emi_val='+Emi_val +'&schemecode='+schemecode;
        //var url="/formviewer/resources/scripts/repayment.jsp";
       
	    window.open_(url, "", "width=1500,height=800");
		setNGValue('cmplx_LoanDetails_is_repaymentSchedule_generated','Y');
    }
		
		else if(pId=='LoanDetails_Button3'){
			if(validateDisbursalGrid()){					
				if(checkMandatory(PL_LoanAdd)){
					// ++ below code already present - 09-10-2017
					//below code added by nikhil
					if(getNGValue('LoanDetails_holdcode')!='')
					return checkMandatory('LoanDetails_holdamt#Hold Amount,LoanDetails_holdrem#Hold Remarks');
					if(LoanDetDisburse_Check())
					return true;
					if(getNGValue('LoanDetails_payreldate')!=getNGValue("LoanDetails_disbdate")){
					showAlert('LoanDetails_Button3','Payment release Date should be same as disbursal Date!!');
					console.log("check datess");
					return false;
			}
					if(!validatePastDate('LoanDetails_payreldate','Payment Release')){
						return false;
					}
					if(getNGValue('LoanDetails_payreldate')!=getNGValue("LoanDetails_disbdate")){
					showAlert('cmplx_LoanDetails_cmplx_LoanGrid','Payment release Date should be same as disbursal Date!!');
					console.log("check datess");
					return false;
			}
			
				}
			}
		}
		else if(pId=='LoanDetails_IMD_Save')
		{
			if(checkMandatory_IMD())
				return true;
		}
		else if(pId=='LoanDetails_Button4'){
			if(checkMandatory(PL_LoanAdd)){
				if(getNGValue('LoanDetails_holdcode')!='')
					return checkMandatory('LoanDetails_holdamt#Hold Amount,LoanDetails_holdrem#Hold Remarks');
					if(LoanDetDisburse_Check())
					return true;
				if(!validatePastDate('LoanDetails_payreldate','Payment Release')){
			return false;
		}//by jahnavi
		if(getNGValue('LoanDetails_payreldate')!=getNGValue("LoanDetails_disbdate")){
					showAlert('','Payment release Date should be same as disbursal Date!!');
					console.log("check datess");
					return false;
			}
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
			if(getNGValue('ReqProd')=='Personal Loan' &&   isCustomerMinor())
			{			
				minorCustomerFieldVisibility("true");
				//setSheetVisible("Tab1", 1, false);
			}else {
				minorCustomerFieldVisibility("false");
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
		/*	if(parseInt(getNGValue('ReqTenor'))<=0 || parseInt(getNGValue('ReqTenor'))>301){
				showAlert('ReqTenor',"Tenor should be between 0 and 300");
				return false;
		} */
			if(getNGValue('ReqProd')=='Personal Loan' &&  isCustomerMinor())
			{
				minorCustomerFieldVisibility("true");
			} else {
				minorCustomerFieldVisibility("false");
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
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Guarantror_GuarantorDet");
		if(n>0){
			showAlert('','Only one Guarantor Details Can be added.');
			return false;
		}
		if(getNGValue('GuarantorDetails_age')<=21)
		{
			showAlert('GuarantorDetails_age',alerts_String_Map['PL409']);
			return false;
		}
		else if(checkMandatory_GuarantorGrid()){
			if(validate_GuarantorGrid()){
				flag_GuarantorDetails=true;
				return true;
			}
		 }
		
			return true;
		}
		else if(pId=='GuarantorDetails_modify'){
		
		if(getNGValue('GuarantorDetails_age')<=21)
		{
			showAlert('GuarantorDetails_age',alerts_String_Map['PL409']);
			return false;
		}
		else if(checkMandatory_GuarantorGrid()){
			if(validate_GuarantorGrid()){
				flag_GuarantorDetails=true;
				return true;
			}
		 }
			return true;
		}
		else if(pId=='GuarantorDetails_delete'){
			if(checkForApplicantTypeInGrids('Guarantor')) {
				setLocked('GuarantorDetails_Button2',false);
				setLocked('GuarantorDetails_cif',false);
				return true;	
			}
		}
		else if(pId=='GuarantorDetails_Save'){
				removeFrame(pId);
				return true;}
		else if(pId=='Product_Save'){
				removeFrame(pId);
				return true;}
		//Arun from CC as per FSD
		else if(pId=='cmplx_FATCA_W8Form'){
			if(getNGValue('cmplx_FATCA_W8Form')==true && (getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')){
			setLocked('cmplx_FATCA_W9Form',true);
			}
			else{
			setLocked('cmplx_FATCA_W9Form',false);
			}	
		}
		else if(pId=='cmplx_FATCA_W9Form'){
			if(getNGValue('cmplx_FATCA_W9Form')==true && (getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')){
			setLocked('cmplx_FATCA_W8Form',true);
			}
			else{
			setLocked('cmplx_FATCA_W8Form',false);
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
			if(getLVWAT("cmplx_LoanDetails_cmplx_LoanGrid",0,2)!=getLVWAT("cmplx_LoanDetails_cmplx_LoanGrid",0,3))
					{
						showAlert("cmplx_LoanDetails_cmplx_LoanGrid","Payment release date should be current date/disbursal date");
						console.log("Test");
						//setNGValue("LoanDetails_payreldate",getNGValue("LoanDetails_disbdate"));
						return false;
					}
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
			//if(checkMandatory_OecdGrid()){
				console.log("modify");
				return true;
			//}
			
		}//Arun
		else if(pId=='OECD_delete'){
		//CustomerDetails
			return true;
		}//Arun
		
		 else if (pId == 'ReferHistory_Modify')
		    {
			 if((activityName=='FCU' || activityName=='FPU') && getNGValue('ReferHistory_status')=='Complete'){
					setLocked('Generate_FPU_Report',false);
				}
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
    	//commented by shweta
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
    
    	//Shweta for PCSP-470 
    	//shweta for seq no-105
		else if(pId=='NotepadDetails_Dateofcalling' || pId=='FinacleCore_cheqretdate')
		{
			validateFutureDateexcCurrent(pId);
		}
//--Above code added by nikhil 13/11/2017 for Code merge
	//added by akshay on 20/9/18
		else if(pId=='Loan_Disbursal_Button1'){//Initiate Contract creation
			if(validatePastDate('cmplx_LoanDetails_frepdate','Repayment')){
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
    {//added by shweta 
    	 if(getNGValue('cmplx_CustDetailVerification_Decision') =='' || getNGValue('cmplx_CustDetailVerification_Decision') =='--Select--')
 		{
 			showAlert('cmplx_CustDetailVerification_Decision','Customer Verification Decision is blank!');
 			return false;
 		}
       		//++Below code added by nikhil 13/11/2017 for Code merge
		if(getNGValue('cmplx_CustDetailVerification_Decision')=='Positive' || getNGValue('cmplx_CustDetailVerification_Decision')=='Approve Sub to CIF')//changed by akshay on 25/6/18 for proc 9815
		{
			if(!checkMandatory(PL_Custdetail_CPV))
			{
				return false;
				}
			//--Above code added by nikhil 13/11/2017 for Code merge
			var ver="cmplx_CustDetailVerification_mobno1_ver:cmplx_CustDetailVerification_mobno2_ver:cmplx_CustDetailVerification_dob_ver:cmplx_CustDetailVerification_POBoxno_ver:cmplx_CustDetailVerification_emirates_ver:cmplx_CustDetailVerification_persorcompPOBox_ver:cmplx_CustDetailVerification_hcountrytelno_val:cmplx_CustDetailVerification_hcountryaddr_val:cmplx_CustDetailVerification_email1_ver:cmplx_CustDetailVerification_email2_ver:cmplx_CustDetailVerification_Mother_name_ver";
			var update="cmplx_CustDetailVerification_mobno1_upd:cmplx_CustDetailVerification_mobno2_upd:cmplx_CustDetailVerification_dob_upd:cmplx_CustDetailVerification_POBoxno_upd:cmplx_CustDetailVerification_emirates_upd:cmplx_CustDetailVerification_persorcompPOBox_upd:cmplx_CustDetailVerification_hcountrytelno_upd:cmplx_CustDetailVerification_hcountryaddr_upd:cmplx_CustDetailVerification_email1_upd:cmplx_CustDetailVerification_email2_upd:cmplx_CustDetailVerification_Mother_name_upd";
			var name="Mobile No. 1:Mobile No. 2:Date of Birth:P.O Box No.:Emirates:Personal / Company P.O Box No.:Home Country Telephone No.:Home Country Address:Email Address 1:Email Address 2";			
			if(CheckMandatory_Verification(ver,update,name)){
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
		//added by aastha for code syn 'office verification' fragment under cpv tab under cad_Analyst workStep1
			if((getNGValue('cmplx_OffVerification_hrdnocntctd') =='No' || getNGValue('cmplx_OffVerification_hrdnocntctd') =='--Select--')&&(getNGValue('cmplx_OffVerification_colleaguenoverified') =='No' || getNGValue('cmplx_OffVerification_colleaguenoverified') =='--Select--') &&(getNGValue('cmplx_OffVerification_hrdemailverified') =='No' || getNGValue('cmplx_OffVerification_hrdemailverified') =='--Select--') )
			{
				showAlert('cmplx_OffVerification_Decision','Decison cannot be positive');
				return false;
			}
			if(getNGValue('cmplx_OffVerification_hrdnocntctd') =='Yes' && (getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=="" || getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=="--Select--") )
			{
				showAlert('cmplx_OffVerification_offtelnovalidtdfrom',alerts_String_Map['PL427']);
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
		//added by aastha for code syn 'office verification' fragment under cpv tab under cad_Analyst workStep1	added
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
				//end changes
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
			/*if(getNGValue('cmplx_OffVerification_hrdnocntctd') =='--Select--' || getNGValue('cmplx_OffVerification_hrdnocntctd') =='')
			{
				showAlert('cmplx_OffVerification_hrdnocntctd',alerts_String_Map['CC248']);
				return false;
			}*/
			//below code added by nikhil 
			/*if(getNGValue('cmplx_OffVerification_hrdemailverified')=='Yes' && getNGValue('cmplx_OffVerification_hrdemailid')=='')
			{
				showAlert('cmplx_OffVerification_hrdemailid',alerts_String_Map['PL386']);
				return false;
			}*/
			
		/*	if(getNGValue('cmplx_OffVerification_colleaguenoverified') == '--Select--' || getNGValue('cmplx_OffVerification_colleaguenoverified') == '')
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
			if (getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)=='Self Employed'){
				if(!checkMandatory(PL_SELF_OFFICEVERIFICATION))
				{
					return false;
				}
			}
			else{
				if(!checkMandatory(PL_OFFICEVERIFICATION))
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
				if(CheckMandatory_Verification(ver,update,name) || getNGValue('EmploymentType')=='Self Employed'){//changed by akshay on 28/6/18 for proc 10507
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
    else if(pId == 'LoanandCard_save')
    {//changed by shweta on 04-12-2019
    	if(getNGValue('cmplx_LoanandCard_Decision')=='Positive')//changed by akshay on 25/6/18 for proc 9815
		{
    		if(!checkMandatory(PL_LoanCard_CPV))
			{
				return false;
			}
    		var ver="cmplx_LoanandCard_loanamt_ver:cmplx_LoanandCard_tenor_ver:cmplx_LoanandCard_emi_ver:cmplx_LoanandCard_islorconv_ver:cmplx_LoanandCard_firstrepaydate_ver";//:cmplx_LoanandCard_cardtype_ver:cmplx_LoanandCard_cardlimit_ver";
			var update="cmplx_LoanandCard_loanamt_upd:cmplx_LoanandCard_tenor_upd:cmplx_LoanandCard_emi_upd:cmplx_LoanandCard_islorconv_upd:cmplx_LoanandCard_firstrepaydate_upd";//cmplx_LoanandCard_cardtype_upd:cmplx_LoanandCard_cardlimit_upd";
			var name="Loan Amount:Tenor:EMI:Islamic / Conventional:First Repayment Date";//:Card Type:Card Limit";
			//var val="cmplx_LoanandCard_loanamt_val:cmplx_LoanandCard_tenor_val:cmplx_LoanandCard_emi_val:cmplx_LoanandCard_islorconv_val:cmplx_LoanandCard_firstrepaydate_val:cmplx_LoanandCard_cardtype_val:cmplx_LoanandCard_cardlimit_val";
			if(CheckMandatory_Verification(ver,update,name)){
				return true;
			}
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
	else if(pId == 'DecisionHistory_Button5') //by shweta calculate re-eligibility button
    {	//Mandatory frames should be fetched before running re-eligibility call
		//below code added by nikhil for PCSP-674
		var mandatory_frame = getNGValue('Mandatory_Frames');
		if(!checkMandatory_Frames(mandatory_frame))
		{
			return false;
		}
		var newAddedrow=0;
		for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
		{
			if(getLVWAT('Decision_ListView1',i,12)=='')
			{
				newAddedrow++;
			}		
		}
		if(newAddedrow>0)
		{
			showAlert('cmplx_Decision_Decision',alerts_String_Map['PL421']);
			return false;
		}
		//change by saurabh on 30th nov 17.
		if(activityName=='CAD_Analyst1' && visitSystemChecks_Flag==false){//ADDED BY AKSHAY ON 20/12/17
			showAlert('',alerts_String_Map['CC257']);
			return false;
		}	
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
			//Below Frame data should be saved after running eligibility call
			var new_value = 'DecisionHistory,IncomeDetails';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(new_value)==-1){
				var newString = field_string_value + new_value+',';
				setNGValueCustom('FrameName',newString);
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
    
    //added by shweta for PCAS-1270
	else if(pId=='cmplx_NotepadDetails_cmplx_Telloggrid')
	{
		return true;
	}
    else if(pId == 'NotepadDetails_save')
    { 
		//below code added by Shweta for PCSP-521 and updated for the same on 18th october
		if(activityName=='CPV' || activityName=='FCU' ||activityName=='FPU'  || activityName=='CPV_Analyst') {
			var tellelog=getLVWRowCount('cmplx_NotepadDetails_cmplx_notegrid');
			var notepad=getLVWRowCount('cmplx_NotepadDetails_cmplx_Telloggrid'); 
			if(tellelog==0 && notepad==0){
				showAlert('NotepadDetails_save',alerts_String_Map['PL419']);
				return false;
			}			
			
		}
		else {
			if(!checkMandatory(PL_Notepad_grid))
				return false;
		}
		return true;
    }
	else if(pId=='SmartCheck_Add'){//added by shweta for PCSP-25
		var activityName = window.parent.stractivityName;
		if(checkMandatory_Smartcheck()){
			if(getNGValue('SmartCheck_creditrem')!='' && activityName=='CAD_Analyst1')
			{
				var comments=getNGValue('SmartCheck_creditrem'); 
				var userid = window.parent.userName;
                var d = new Date();
                var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck_creditrem',datetime+'-'+userid+'-'+comments);
			}
			
			if(getNGValue('SmartCheck_CPVrem')!='' && activityName!='CAD_Analyst1')
			{
				var comments=getNGValue('SmartCheck_CPVrem'); 
				var userid = window.parent.userName;
	            var d = new Date();
	            var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
				setNGValueCustom('SmartCheck_CPVrem',datetime+'-'+userid+'-'+comments);
			}
		return true;
		}
	}
	else if (pId == 'EmploymentVerification_s2_Designation_button2' || pId == 'EmploymentVerification_s2_desig_visa_Select' || pId == 'EmploymentVerification_s2_Designation_button4_View' || pId == 'EmploymentVerification_s2_desig_visa_view' || pId == 'EmploymentVerification_s2_Designation_button6_View' || pId == 'EmploymentVerification_s2_desig_visa_update_view')
	{
	return true;
	}
	else if(pId=='SmartCheck_Modify'){
	var activityName = window.parent.stractivityName;
	if(checkMandatory_Smartcheck()){
		//Added by aman for PCSP-380
		//Done by aman for production issue
		if(getNGValue('SmartCheck_creditrem')!='' && activityName=='CAD_Analyst1')
		{
			var comments=getNGValue('SmartCheck_creditrem'); 
			var userid = window.parent.userName;
            var d = new Date();
            var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
			setNGValueCustom('SmartCheck_creditrem',datetime+'-'+userid+'-'+comments);
		}
		if(getNGValue('SmartCheck_CPVrem')!=''&& activityName!='CAD_Analyst1')
		{
			var comments=getNGValue('SmartCheck_CPVrem'); 
			var userid = window.parent.userName;
            var d = new Date();
            var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
			setNGValueCustom('SmartCheck_CPVrem',datetime+'-'+userid+'-'+comments);
		}
		return true;
		}
	}
    else if(pId == 'SmartCheck_save')// changed by shweta for PCSP-25 button name is different in cc and pl
    {
        return true;
    }
	else if(pId == 'cmplx_SmartCheck_SmartCheckGrid')
    {
			//change by shweta for PCSP-25 d
			//changed by shweta for PCSP-462
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
		return true;

    }
	else if(pId=='SmartCheck_Delete'){
		return true;
	}
	
	
// disha FSD
    else if(pId == 'NotepadDetails_Add')
    {
	if(checkMandatory(PL_Notepad)){
		//below code added by shweta for PCSP-416
		if(getNGValue('NotepadDetails_workstep')==activityName) {
			showAlert('NotepadDetails_Add',alerts_String_Map['PL417']);
			return false;
		}		
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
			//added by shweta for pcsp-959
			var notepadDesc = getLVWAT('cmplx_NotepadDetails_cmplx_notegrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_NotepadDetails_cmplx_notegrid"),11);
			//++below code added by abhishek on 11/11/2017 for new CR
			var GridInstructionQueue = getLVWAT('cmplx_NotepadDetails_cmplx_notegrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_NotepadDetails_cmplx_notegrid"),5);
			var FieldInstructionQueue = getNGValue('NotepadDetails_insqueue');
			if(GridInstructionQueue==FieldInstructionQueue){
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
					if(activityName!=GridInstructionQueue) {
						if(checkMandatory(PL_Notepad_modify)){
							//below code added by Shweta for PCSP-501
							if(getNGValue('NotepadDetails_inscompletion')==false)
							{
								showAlert('NotepadDetails_inscompletion',alerts_String_Map['PL414']);
								return false;
							}									
						}
						if(getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',getNGListIndex('cmplx_NotepadDetails_cmplx_notegrid'),7)=='true') {//PCAS-2101 by sagarika
							showAlert('cmplx_NotepadDetails_cmplx_notegrid',alerts_String_Map['PL422']);
							return false;
						}
							return true;
					}
				}
			}
			else{
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
    else if(pId == 'NotepadDetails_Add1' || pId== 'NotepadDetails_Clear')
    {
    	var new_value = 'Notepad_Values';
		var field_string_value = getNGValue('FrameName');
		if(field_string_value.indexOf(new_value)==-1){
			var newString = field_string_value + new_value+',';
			setNGValueCustom('FrameName',newString);
		}
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
    {//by shweta
		if(getNGValue('SmartCheck1_CreditRemarks')!='' && (activityName=='CAD_Analyst1' || activityName=='CAD_Analyst2'))
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
		if(getNGValue('SmartCheck1_FCURemarks')!=''&& (activityName=='FCU' || activityName=='FPU' ))
		{
			var comments=getNGValue('SmartCheck1_FCURemarks'); 
			var userid = window.parent.userName;
            var d = new Date();
            var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
			setNGValueCustom('SmartCheck1_FCURemarks',datetime+'-'+userid+'-'+comments);
		}
		return true;
    }	
	else if(pId == 'SmartCheck1_Modify')
    {	//by shweta
		if(getNGValue('SmartCheck1_CreditRemarks')!='' && (activityName=='CAD_Analyst1' || activityName=='CAD_Analyst2'))
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
		if(getNGValue('SmartCheck1_FCURemarks')!=''&& (activityName=='FCU' || activityName=='FPU' ))
		{
			var comments=getNGValue('SmartCheck1_FCURemarks'); 
			var userid = window.parent.userName;
            var d = new Date();
            var datetime = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear()+' '+d.getHours()+':'+d.getMinutes();
			setNGValueCustom('SmartCheck1_FCURemarks',datetime+'-'+userid+'-'+comments);
		}
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
			setNGValueCustom('FinacleCoreSave','Y');//added by shweta for seq #105
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
				overwriteReminder = true; //by Bandana PCASI-3459
				setNGValueCustom('Liability_New_overwrite_flag','Y');//jahnavi for 3646
			if(getNGValue('IS_AECB')=='Y' || getNGValue('IS_AECB')=='D' )		
			{
				//++below code added by siva on 16102019 for PCAS-2696 CR
				setNGValueCustom('Eligibility_Trigger','Y');
				//--above code added by siva on 16102019 for PCAS-2696 CR
			    setNGValueCustom('Liability_New_overwrite_flag','Y');//jahnavi for 3646
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
			
			var param_json=('{"cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNO')+'","sub_product":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)+'","emp_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5)+'"}');
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
		else if(pId=='IncomingDocNew_ViewSIgnature'){
			var wi_name = window.parent.pid;
			var activityName = window.parent.stractivityName;
			if(""==getNGValue('Funding_Account_no')){
				showAlert('IncomingDocNew_ViewSIgnature',alerts_String_Map['PL436']);
				return false;
			}
			var url="/webdesktop/custom/CustomJSP/OpenImage.jsp?workstepName="+window.parent.stractivityName+"&signMatchNeededAtChecker=A"+"&debt_acc_num="+getNGValue('Funding_Account_no')+"&wi_name="+wi_name+"&workstepName="+activityName+"&CifId="+getNGValue('cmplx_Customer_CIFNO');
			window.open_(url, "", "width=700,height=600");
		}	
		
		else if(pId=='IncomingDocNew_UploadSig'){
					//upload_sig=true;
			//----Commented by akshay on 16/1/18
			var activityName = window.parent.stractivityName;
			var wi_name = window.parent.pid;
			var firstName = getNGValue('cmplx_Customer_FIrstNAme');
			var middleName = getNGValue('cmplx_Customer_MiddleName');
			var lastName = getNGValue('cmplx_Customer_LAstNAme');
			var fullName = firstName+" "+middleName+" "+lastName;
			
			//alert("fullName "+ fullName);
			
	 		var url = "/formviewer/CustomForms/RLOS/VerifySignature.jsp?CifId=" + getNGValue('cmplx_Customer_CIFNO') + "&AccountNo=" + getNGValue('Account_Number') +
	            		"&CustSeqNo=" + "001" + "&ItemIndex=" + getNGValue('AppRefNo') + "&CustomerName=" + fullName + "&docIndex=" + getNGValue('sig_docindex') +
	           		 "&WIName=" + wi_name + "&workstepName=" + activityName;
			var result = null;
				var finalxmlResponse = null;
				var activityName = window.parent.stractivityName;
				var wi_name = window.parent.pid;
				var firstName = getNGValue('cmplx_Customer_FirstNAme');
				var lastName = getNGValue('cmplx_Customer_LastNAme');
				var fullName = getNGValue('CUSTOMERNAME');
				var strGenralData = window.parent.strGeneralData;
				var FolderId = strGenralData.substring( strGenralData.indexOf("<FolderId>") + 10 , strGenralData.indexOf("</FolderId>") );
		
			//	alert("fullName uploadDocument"+ fullName);
				$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/VerifySignature.jsp",
					data: { CifId:getNGValue('cmplx_Customer_CIFNO'),AccountNo:getNGValue('Account_Number'),CustSeqNo:'001',ItemIndex:FolderId,CustomerName:fullName,docIndex:getNGValue('sig_docindex'),WIName:wi_name ,workstepName:activityName} ,
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
           	 
			//uploadDocument();
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
					else if(getLVWAT('cmplx_PartMatch_cmplx_Partmatch_grid',SelectedRow,0)==getNGValue('cmplx_Customer_CIFNO'))
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
		
		//added by akshay on 16/2/18 and added part match on 18/5/21 PCASI 2671
		else if((getNGValue('cmplx_Customer_NTB')==true) && (pId=='DecisionHistory_CifLock' || pId=='PartMatch_CifLock')){
			showAlert('','This is NTB case');
		} //pcasi3559
		else if((getNGValue('cmplx_Customer_NTB')==true) && (pId=='DecisionHistory_CifUnlock' || pId=='PartMatch_CifUnlock')){
			showAlert('','This is NTB case');
		}
		else if(pId=='DecisionHistory_CifLock' || pId=='PartMatch_CifLock'){
			if(getNGValue('Is_CustLock')=='Y'){
				showAlert('',alerts_String_Map['PL391']);
				return false;
			}
			return true;	
		}
		
		else if(pId=='DecisionHistory_CifUnlock' || pId=='PartMatch_CifUnlock'){
			if(getNGValue('Is_CustLock')=='N' || getNGValue('Is_CustLock')==''){
				showAlert('',alerts_String_Map['PL392']);
				return false;
			}
			return true;
		}
		// added on 18/5/21 PCASI 2671  - PCASI-3206 10/6/21 
//		else if(pId=='PartMatch_CifUnlock'){
//			if( getNGValue('cmplx_PartMatch_CIF_Lock_Status')=='N' || getNGValue('cmplx_PartMatch_CIF_Lock_Status')=='' ){
//				showAlert('',alerts_String_Map['PL392']);
//				return false;
//			}
//			return true;
//		}
		
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
			else if(activityName=='DDVT_maker'||activityName=='CAD_Analyst1')
			{
				setLocked("cmplx_EmploymentDetails_EmpName", true);
				setLocked("cmplx_EmploymentDetails_EMpCode", true);
				setLocked("cmplx_EmploymentDetails_FreezoneName", false);
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
				setLocked("cmplx_EmploymentDetails_authsigname", false);
				setLocked("cmplx_EmploymentDetails_highdelinq", true);
				setLocked("cmplx_EmploymentDetails_dateinPL", true);
				setLocked("cmplx_EmploymentDetails_dateinCC", true);
				setLocked("cmplx_EmploymentDetails_remarks", true);
				setLocked("cmplx_EmploymentDetails_Aloc_RemarksPL", true);
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
		//++below code added by siva on 16102019 for PCAS-2696 CR
		setNGValueCustom('Eligibility_Trigger','Y');
		//--above code added by siva on 16102019 for PCAS-2696 CR
	}
		//++below code added by abhishek for CSM point 8 on 7/11/2017
		if(pId=='ExtLiability_CACIndicator')
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
		if(pId=='ExtLiability_Takeoverindicator')
		{
			var TakeOverIndicator= getNGValue('ExtLiability_Takeoverindicator');
			if(TakeOverIndicator==true )
			{
				setEnabled("ExtLiability_takeoverAMount", true);
				setLockedCustom("ExtLiability_takeoverAMount", false);//PCASI-1097
				setNGValue("ExtLiability_Consdierforobligations", false);
				
			}
			else
			{
				
				setNGValue("ExtLiability_Consdierforobligations", true);
				setNGValue("Liability_New_SettlementFlag", false);
				setEnabled("ExtLiability_takeoverAMount", false);
				setLockedCustom("ExtLiability_takeoverAMount", true);//PCASI-1097
			document.getElementById("ExtLiability_takeoverAMount").value = "";
			}
		
		}
		//++above code added by abhishek for CSM point 9 on 7/11/2017
		else if (pId=='FinacleCore_Button1')
		{
			if(checkMandatory(PL_FINACLECORE_DDS)) {//added by shweta for seq#105
				var new_value = 'Finacle_Core';
				var field_string_value = getNGValue('FrameName');
				if(field_string_value.indexOf(new_value)==-1){
					var newString = field_string_value + new_value+',';
					setNGValueCustom('FrameName',newString);
				}
				removeFrame(pId);
				return true;
			}
		}
		else if (pId=='EmploymentVerification_s2_search_TL_number')
		{ 
		    
			return true;
		}
			
		else if (pId=='EmploymentVerification_s2_comp_save')
		{
			return true;
		}
		else if (pId=='FinacleCore_Button3')
		{
			if(checkMandatory(PL_FINACLECORE_DDS))//added by shweta for seq no 105
			{
				removeFrame(pId);
				return true;
			}
		}
		else if (pId=='FinacleCore_Button4')
		{
			if(checkMandatory(PL_FINACLECORE_INWARDTT)){//added by shweta for seq#105
				removeFrame(pId);
				return true;
			}
		}
		else if (pId=='FinacleCore_Button5')
		{
			if(checkMandatory(PL_FINACLECORE_INWARDTT)){//added by shweta for seq#105
				removeFrame(pId);
				return true;
			}
		}
		else if (pId=='FinacleCore_Button6')
		{
			removeFrame(pId);//added by shweta for seq 105
			return true;
		}
		else if (pId=='FinacleCore_Button7')
		{
			removeFrame(pId);//added by shweta for seq 105
			return true;
		}
	/*	else if(pId=='DecisionHistory_ADD') //by jahnavi for ddvt decision
		{
			if(activityName=='DDVT_Hold')
			{
			
/*	var newAddedrow=0;
		for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
		{
			if(getLVWAT('Decision_ListView1',i,12)=='')
			{
				newAddedrow++;
			}		
		}
			var value= getNGValue('FrameName');
		var to_remove=document.getElementById(document.getElementById(pId).parentNode.id).parentNode.id;
		var array=value.split(',');
		var index = array.indexOf(to_remove);
		 array.splice(index,0,to_remove);
		setNGValue('FrameName',array);
		}
		}*/
		if(pId=='cmplx_CC_Loan_cmplx_btc')   //3-05-2021 by jahnavi for BT/CCC/SC grid CR
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
			 setVisible("cmplx_CC_Loan_cmplx_btc_payment_purpose",true);
		 }
		 else{
			 setNGValue("cmplx_CC_Loan_cmplx_btc_payment_purpose",'');//PCASP-3186
			 setVisible("CC_Loan_Label25",false);
			 setVisible("cmplx_CC_Loan_cmplx_btc_payment_purpose",false);
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
	else if(pId=='BT_Add'){
			if(checkMandatory_BTGrid()){
				if(validate_BTGrid())
					return true;
			}
			
		}
		else if(pId=='BT_Modify'){
			if(checkMandatory_BTGrid()){
				
				if(validate_BTGrid_Modify())
					return true;
			}	
		}
		
		else if(pId=='BT_Delete'){
			return true;
		}
		//03-05-2021 /BT/CCC/ jahnavi
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
				// added by Imran for EFMS
				//if(getNGValue('ELigibiltyAndProductInfo_EFMS_Status')!='Y' && activityName=='CAD_Analyst1')
				{
				//showAlert(pId,alerts_String_Map['CC281']);
				
				//return false;	commented by jahnavi for 3067
				}
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
				attrbList += PLTemplateData(); 	//Added by shweta to remove tah names
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
				
				//CreateIndicator('GenTemp');//by shweta PCASI-1010
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
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
				}
				flag_add_new=false;//sagarika for CAM saurabh1
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
				var result=getDataFromDB('PLE_ReportUrl',"Wi_Name=="+window.parent.pid,'PLjs').trim();
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
				else if(getNGValue('IS_AECB')!='Y' || getNGValue('IS_AECB')!='D'){
				showAlert('InternalExternalLiability',alerts_String_Map['VAL106']);
				return false;
				}
			}
			else if(getNGValue('IS_Employment_Saved')!='Y' ){
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
		else if(pId=='PostDisbursal_MCQ_Modify')
		{
			if(mcq_deposit_flag && getNGValue('PostDisbursal_MCQ_Deposit_Date')!='')
			{
		showAlert('PostDisbursal_NLCFollowUp','MCQ deposit date changed ,Kindly update the NLC follow up date');
		return false;
			}
			return true;
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
			if( (getLVWAT(pId,getNGListIndex(pId),5)==activityName || (getLVWAT(pId,getNGListIndex(pId),5)=='Source' && activityName=='DSA_CSO_Review') || (getLVWAT(pId,getNGListIndex(pId),5)=='CSO (for documents)' && activityName=='DSA_CSO_Review') || ((getLVWAT(pId,getNGListIndex(pId),5)=='CPV Checker' || getLVWAT(pId,getNGListIndex(pId),5)=='Smart_CPV') && activityName=='CPV_Analyst')|| (getLVWAT(pId,getNGListIndex(pId),5)=='FPU' && (activityName=='FCU'||activityName=='FPU'))))
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
		//below code added by nikhil for PCSP-504 Sprint-2
			setNGValueCustom('ELigibiltyAndProductInfo_EFMS_Status','Y');
			return true;
		}
		//-- Above code added  by abhishek on 04/01/2018 for EFMS refresh functionality
		else if(pId=='Button_City')
		{ 
			return true;
		}
		else if(pId=='TLRM_Button')
		{ 
			return true;
		}
		else if(pId=='Sourcing_Branch_Code_button')
		{ 
			return true;
		}//By Alok on 31/10/21
		else if(pId=='Customer_Button2')
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
				setEnabledCustom('CardDetails_BankName',true);
				setEnabledCustom('CardDetails_ChequeNumber',true);
				setEnabledCustom('CardDetails_Amount',true);
				setLocked('CardDetails_Date',false);
				
			}
			else{
				setNGValue('CardDetails_BankName','');
				setNGValue('CardDetails_ChequeNumber','');
				setNGValue('CardDetails_Amount','');
				setNGValue('CardDetails_Date','');
				setEnabledCustom('CardDetails_BankName',false);
				setEnabledCustom('CardDetails_ChequeNumber',false);
				setEnabledCustom('CardDetails_Amount',false);
				setLocked('CardDetails_Date',true);
			}
	}
	
		//below code added by akshay on 15/2/18
		else if(pId=='cmplx_Decision_MultipleApplicantsGrid')
		{
			var selectedrow=getNGListIndex(pId);
			if(selectedrow!=-1){
				if(activityName=='DDVT_Checker'){
				if(getLVWAT(pId,selectedrow,0)=='Primary'){
					//if(getNGValue('cmplx_Decision_Decision')=='Approve'){
					
											
						//if(getNGValue("AlternateContactDetails_RetainAccIfLoanReq")==true){
							setVisible("DecisionHistory_Button3",true);	
							setVisible("DecisionHistory_updcust",true);
							//if(getNGValue('Is_ACCOUNT_MAINTENANCE_REQ')=='Y'){
							if(getNGValue('cmplx_Customer_NTB') == true && getNGValue("AlternateContactDetails_RetainAccIfLoanReq")==true){
								setVisible("DecisionHistory_chqbook",true);
							}
							else{
							setVisible("DecisionHistory_chqbook",false);
							}
								
						//}
						//else
						//{
						//setVisible("DecisionHistory_Button3",false);	
							//setVisible("DecisionHistory_updcust",false);
							//setVisible("DecisionHistory_chqbook",false);
						//}
					}
					//PCASI-2925
					else if(getLVWAT(pId,selectedrow,0)=='Supplement'){
                             //setVisible('DecisionHistory_Button3',false);
                             setVisible("DecisionHistory_updcust",false);
                             setVisible("DecisionHistory_chqbook",false);
                             if(getLVWAT(pId,selectedrow,3)==''){
                                setVisible('DecisionHistory_Button2',true);
								setEnabledCustom('DecisionHistory_Button2',true);
                            }
                            else{
                            setVisible('DecisionHistory_Button2',false);
                            setEnabledCustom('DecisionHistory_Button2',false);
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
		else if(pId=='WorldCheck_status' || pId=='WorldCheck_fetch'){
			return true;
		}

		//Added by Imran
		else if(pId == 'cmplx_FinacleCore_DDSgrid' && activityName=='CAD_Analyst1'){
			return true;
		}
		//bandana: for sending sms and email on WS TO teams
		else if(pId=='PostDisbursal_MCQ_Sms' || pId=='PostDisbursal_LC_Sms' || pId=='PostDisbursal_STL_SMS' ){
			
			return true;
		}
		
		else if (pId=='cmplx_PostDisbursal_cpmlx_gr_NLC') {
			setEnabled('PostDisbursal_MCQ_Sms',true);
		}
		
		else if (pId=='cmplx_PostDisbursal_cmplx_gr_ManagersCheque') {
			setEnabled('PostDisbursal_MCQ_Sms',true);
		}
		
		else if (pId=='PostDisbursal_MCQ_Sms') {
			setEnabled('PostDisbursal_MCQ_Sms',true);
		}
		else if (pId=='LoanDeatils_calculateemi') {
			return true;
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
{	 //added by saurabh1 for CAM S4 i4
	console.log("change_PersonalLoanS :: "+pId);
    var activityName = window.parent.stractivityName;
	if (activityName == 'DDVT_maker')
    {
		if(pId== 'cmplx_Customer_Nationality' || pId=='SupplementCardDetails_Nationality'  ){
		if(getNGValue(pId)=='AE'){
			setLocked('cmplx_Customer_VisaNo',false);
			setLocked('cmplx_Customer_VisaIssueDate',false);
			setLocked('cmplx_Customer_VIsaExpiry',false);
		}//By Alok on 28/11/2021

    }
	}
	if(activityName == 'CAD_Analyst1' || activityName == 'CAD_Analyst2'){
	flag_add_new=true;
	}//saurabh1 end code
	if(activityName=='ToTeam' && pId=='DecisionHistory_DecisionReasonCode'){
		setNGValueCustom(pId,getNGSelectedItemText(pId));
	} else{
		setNGValueCustom(pId,getNGValue(pId));
	}
	var activityName = window.parent.stractivityName;
	//changed by saurabh on 23rd Oct 17.
	if(getNGValue(pId) != PLValuesData[pId]){
		if(pId=='cmplx_LoanDetails_inttype' || pId=='cmplx_LoanDetails_marginrate' || pId=='cmplx_LoanDetails_frepdate' || pId=='cmplx_LoanDetails_lonamt' || pId=='cmplx_LoanDetails_tenor' || pId==''){
			setNGValue('cmplx_LoanDetails_is_repaymentSchedule_generated','N');
		}
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
		if(value.indexOf('IncomeDetails')>-1 || value.indexOf('ExtLiability')>-1 || value.indexOf('EMploymentDetails')>-1   || value.indexOf('FinacleCore')>-1 || value.indexOf('EmploymentDetails')>-1)
		{
			setNGValueCustom('Eligibility_Trigger','Y');
		}
		//--above Code added by nikhil for PCAS-364 CR
	}
	// hritk 5.7.21 PCASI 3245 3358 - 3810
	if(pId=='AlternateContactDetails_MobileNo1'){
		var Mobile_no=getNGValue('AlternateContactDetails_MobileNo1');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=''){
			Mobile_no="00971"+Mobile_no;
			setNGValue('AlternateContactDetails_MobileNo1',Mobile_no);
		}
    	setNGValue('cmplx_Customer_MobNo',getNGValue('AlternateContactDetails_MobileNo1'));
		setLocked('cmplx_Customer_MobNo',true);
		change_PersonalLoanS('cmplx_Customer_MobNo');
    }
	if(pId=='FircoStatusLabel')
	{
		if(getNGValue(pId=='Hit'))
		{			setNGValue('cmplx_Decision_Decision','Refer');
		setEnabled('cmplx_Decision_Decision','false');
		}
	}
	if(pId=='cmplx_LoanDetails_lpfamt'){
			var lpf = getNGValue('cmplx_LoanDetails_lpfamt');
			if(lpf>2500){
			    showAlert('cmplx_LoanDetails_lpfamt','Loan Processing Fee cannot be greater than 2500')
			}
		}
    if(pId=='cmplx_Customer_MobNo'){
    	setNGValue('AlternateContactDetails_MobileNo1',getNGValue('cmplx_Customer_MobNo'));
    }
	if(pId== 'cmplx_Customer_Nationality' || pId=='SupplementCardDetails_Nationality'  ){
		if(getNGValue(pId)=='AE'){
			setLockedCustom('cmplx_Customer_VisaNo',true);
			setLockedCustom('cmplx_Customer_VisaIssueDate',true);
			setLockedCustom('cmplx_Customer_VIsaExpiry',true);
		}else if(!(getNGValue(pId)=='AE')){
			setLockedCustom('cmplx_Customer_VisaNo',false);
			setLockedCustom('cmplx_Customer_VisaIssueDate',false);
			setLockedCustom('cmplx_Customer_VIsaExpiry',false);
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
	
	
		
	if(pId== 'GuarantorDetails_nationality' ){
		if(getNGValue(pId)=='AE'){
			setLockedCustom('GuarantorDetails_visaNo',true);
			setLockedCustom('GuarantorDetails_VisaIssueDate',true);
			setLockedCustom('GuarantorDetails_visaExpiry',true);
			setNGValue('GuarantorDetails_visaNo','');
			setNGValue('GuarantorDetails_VisaIssueDate','');
			setNGValue('GuarantorDetails_visaExpiry','');
		}else if(!(getNGValue(pId)=='AE')){
			setLockedCustom('GuarantorDetails_visaNo',false);
			setLockedCustom('GuarantorDetails_VisaIssueDate',false);
			setLockedCustom('GuarantorDetails_visaExpiry',false);
		}
	}
	if(pId=='PostDisbursal_MCQ_Deposit_Date')
	{
		mcq_deposit_flag=true;
	}
	if(pId=='PostDisbursal_NLCFollowUp')
	{
		mcq_deposit_flag=false;
	}
	if(activityName == 'CSM' || activityName== 'DDVT_maker' || activityName== 'DDVT_Checker'){
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
	if(pId=='cmplx_CustDetailverification1_EmiratesId'){
		var c1 = getNGValue('cmplx_CustDetailverification1_EmiratesId');
		if((c1.length != 15) ||(c1.substr(0,3)!='784')){
			if(c1.substr(0,3)!='784'){
				showAlert(pId,alerts_String_Map['VAL108']);
				setNGValue(pId,'');
			}
			else{
				showAlert(pId,alerts_String_Map['VAL108']);
				setNGValue(pId,'');
			}
		}
	}
	else if(pId=='cmplx_Customer_VisaNo'){
		if(getNGValue('cmplx_Customer_Nationality')=='AE' && getNGValue('cmplx_Customer_VisaNo').length>0){
			setNGValue('cmplx_Customer_VisaNo','');
			alert('cmplx_Customer_VisaNo','Visa No NA for UAE Citizen');
			return false;
		}
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
	if(pId=='cmplx_emp_ver_sp2_off_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setEnabled("cmplx_emp_ver_sp2_landline",true);
			setLocked('cmplx_emp_ver_sp2_landline',false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_landline",false);
			setLocked('cmplx_emp_ver_sp2_landline',true);
		}
	}
	
	if(pId=='KYC_Combo1'){
	    if(getNGValue('KYC_Combo1')=='Y')
	    {
			setLocked('KYC_CustomerType',false);
			//setLocked('KYC_Combo2',false);
			setLocked('KYC_DatePicker1',false);
	    }
	    else{
            //setLocked('KYC_CustomerType',true);
			//setLocked('KYC_Combo2',true);
			setLocked('KYC_DatePicker1',true);
	    }
	}  //hritik 3721 
	
	if(pId=='cmplx_emp_ver_sp2_desig_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			//cmplx_emp_ver_sp2_landline
			setLocked('EmploymentVerification_s2_Designation_button2',false);
			setLocked('cmplx_cust_ver_sp2_desig_remarks',true);
			setEnabled('EmploymentVerification_s2_Designation_button6_View',true);
			setLocked('EmploymentVerification_s2_Designation_button6_View',false);
	
			
		}
		else			
		{
			setLocked('EmploymentVerification_s2_Designation_button2',true);
			setLocked('cmplx_cust_ver_sp2_desig_remarks',true);
			setNGValue('cmplx_cust_ver_sp2_desig_remarks','');
			setLocked('EmploymentVerification_s2_Designation_button6_View',true);
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
	else  if(pId=='cmplx_emp_ver_sp2_salary_break_drop')
	{
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_emp_ver_sp2_salary_break_remarks",true);
			setLocked("cmplx_emp_ver_sp2_salary_break_remarks",false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_salary_break_remarks",false);
			setLocked("cmplx_emp_ver_sp2_salary_break_remarks",true);
		}
	}
	else  if(pId=='cmplx_emp_ver_sp2_sal_pay_drop')
	{
		if(getNGValue(pId)=='Other')
		{
			setEnabled("cmplx_emp_ver_sp2_sal_pay_remarks",true);
			setLocked("cmplx_emp_ver_sp2_sal_pay_remarks",false);
		}
		else
		{
			setEnabled("cmplx_emp_ver_sp2_sal_pay_remarks",false);
			setLocked("cmplx_emp_ver_sp2_sal_pay_remarks",true);
		}
	}
	else if(pId=='cmplx_FPU_Grid_case_status'){
		if(getNGValue('cmplx_FPU_Grid_case_status')=='Actionable'){
			setNGValue('cmplx_FPU_Grid_Actionable','Yes');
		}else if(getNGValue('cmplx_FPU_Grid_case_status')=='Non-Actionable'){
			setNGValue('cmplx_FPU_Grid_Actionable','No');
		} else{
			setNGValue('cmplx_FPU_Grid_Actionable','--Select--');
		}
	}
	
	
	if(pId=='cmplx_cust_ver_sp2_desig_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_desig_drop')=='Mismatch'){
			setLocked('Designation_button1',false);
			setLocked('cmplx_cust_ver_sp2_desig_remarks',true);
			setLocked('Designation_button5_View',false);

		}
		else{
			setLocked('Designation_button1',true);
			setLocked('Designation_button5_View',true);
			setLocked('cmplx_cust_ver_sp2_desig_remarks',true);
			setNGValue('cmplx_cust_ver_sp2_desig_remarks','');



		}
	}
	if(pId=='cmplx_cust_ver_sp2_ContactedOn')
	{
		if(getNGValue('cmplx_cust_ver_sp2_ContactedOn')=='Mismatch')
		{
			setEnabled('cmplx_cust_ver_sp2_Mobile',true);
			setLocked('cmplx_cust_ver_sp2_Mobile',false); // PCASI 3434
		}
		else{
			setEnabled('cmplx_cust_ver_sp2_Mobile',false);
			setLocked('cmplx_cust_ver_sp2_Mobile',true);

		}
	}
	
	else if(pId=='cmplx_cust_ver_sp2_doj_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_doj_drop')=='Mismatch'){
			setLocked('cmplx_cust_ver_sp2_doj_remarks',false);
		}
		else{
			setLocked('cmplx_cust_ver_sp2_doj_remarks',true);
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
	else if (pId=='cmplx_cust_ver_sp2_ContactedDate'){
		validateFutureDateexcCurrent('cmplx_cust_ver_sp2_ContactedDate')
	} // hritik
	
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
	else if(pId=='cmplx_cust_ver_sp2_prevemp_los_drop'){
		if(getNGValue('cmplx_cust_ver_sp2_prevemp_los_drop')=='Mismatch'){
			setLocked('cmplx_cust_ver_sp2_prevemp_los_remark',false);

		}
		else{
			setLocked('cmplx_cust_ver_sp2_prevemp_los_remark',true);

		}
	}

	
	//below code added by isha
	if(pId=='SupplementCardDetails_Text6'){
	    var Id = getNGValue('SupplementCardDetails_Text6');
		var Dob=getNGValue('SupplementCardDetails_DOB');//added by saurabh1 PCAS-2959
		if(((Id=="")||(Id.length != 15) ||(Id.substr(0,3)!='784')) && isFieldFilled('cmplx_Customer_marsoomID')==false && activityName=='DDVT_maker' ){
			if(Id.substr(0,3)!='784'){
				showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL108']);
			}
			else{
				showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL108']);
			}
			return false;
		}
		
		if((Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9)) && Id!='' && activityName=='DDVT_maker')
		{
			showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL074']);
			return false;
		} 		
		return true;
	}
	//below code added by isha
	if(pId=='SupplementCardDetails_MobNo')
	{
		var Mobile_no=getNGValue('SupplementCardDetails_MobNo');
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValue('SupplementCardDetails_MobNo',mob_No);
		}	
		if(mob_No.length!=14){
			showAlert('SupplementCardDetails_MobNo',alerts_String_Map['PL341']);
			return false;
		}
                if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=''){
			showAlert('SupplementCardDetails_MobNo',alerts_String_Map['VAL088']);
			setNGValue('SupplementCardDetails_MobNo','');
			return false;
		}	
	}
	if(pId=='cmplx_fieldvisit_sp2_field_visit_date')
	{
		fieldvisit_flag_ini=true;  //jahnavi fcu 
	}
	if(pId=='cmplx_fieldvisit_sp2_field_rep_receivedDate')
	{
		fieldvisit_flag_reci=true;  //jahnavi fcu
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
	 if(pId=='cmplx_fieldvisit_sp2_field_v_time')
	 {
		 fieldvisit_flag_ini=false;
	 }
	 if(pId=='cmplx_fieldvisit_sp2_field_visit_rec_time')
	 {
		 fieldvisit_flag_reci=false;
	 }
	 
	 if(pId=='cmplx_EmploymentDetails_RegPayment')
	{
		console.log(getNGValue('cmplx_EmploymentDetails_RegPayment'))
		return true;
	}

	//code changes by bandana for fcu starts
	if(pId=='cmplx_fieldvisit_sp2_drop1')
	{//alert("value");
	if(getNGValue(pId)=="Yes")
	{//alert("yes");
		setLocked("cmplx_fieldvisit_sp2_drop2",false);
		setLocked("cmplx_fieldvisit_sp2_drop3",false);
		setLocked("cmplx_fieldvisit_sp2_field_visit_date",false);
		setLocked("cmplx_fieldvisit_sp2_field_v_time",false);
		setLocked("cmplx_fieldvisit_sp2_field_rep_receivedDate",false);
		setLocked("cmplx_fieldvisit_sp2_field_visit_rec_time",false);
		setEnabled("cmplx_fieldvisit_sp2_field_visit_date",true);
		setEnabled("cmplx_fieldvisit_sp2_field_v_time",true);
		setEnabled("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
		setEnabled("cmplx_fieldvisit_sp2_field_visit_rec_time",true);
}
	else if(getNGValue(pId)=="No")
	{//alert("no");
	  setNGValue("cmplx_fieldvisit_sp2_field_v_time","");
				setNGValue("cmplx_fieldvisit_sp2_drop3","");
				setNGValue("cmplx_fieldvisit_sp2_field_rep_receivedDate","");
				setNGValue("cmplx_fieldvisit_sp2_field_visit_rec_time","");
				setNGValue("cmplx_fieldvisit_sp2_field_visit_date","");
				setNGValue("cmplx_fieldvisit_sp2_drop2","");
		setLocked("cmplx_fieldvisit_sp2_drop2",false);
		setLocked("cmplx_fieldvisit_sp2_drop3",true);
		setLocked("cmplx_fieldvisit_sp2_field_visit_date",true);
		setLocked("cmplx_fieldvisit_sp2_field_v_time",true);
		setLocked("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
		setLocked("cmplx_fieldvisit_sp2_field_visit_rec_time",true);
		 setEnabled("cmplx_fieldvisit_sp2_field_visit_date",false);
		setEnabled("cmplx_fieldvisit_sp2_field_v_time",false);
		setEnabled("cmplx_fieldvisit_sp2_field_rep_receivedDate",false);
		setEnabled("cmplx_fieldvisit_sp2_field_visit_rec_time",false);
		
		 
	}
	return true;
	}
//code change by bandana fcu ends

	 
	//Done by Isha for PCAS-1410
	if(pId=='cmplx_EmploymentDetails_CurrEmployer')
	{
		var activityName=window.parent.stractivityName;
		//Commented by shweta for PCAS-2484
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
	// disha FSD
	 if(pId=='CompanyDetails_EffectiveLOB')
	 {
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
	//changed by saurabh on 23rd Oct 17.
	//commented by shweta
	/*
	 	var activityName = window.parent.stractivityName;
	if(activityName == 'CSM' || activityName== 'DDVT_Maker' || activityName== 'DDVT_checker'){
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
	}*/
	// disha FSD winame added


// disha FSD - to disable fetch detail button integration point at CSM workstep
	
   
	//below code added bysaurabh
	
	//change by bandana for  PCAS-2637
	//++below code added by nikhil for Self-Supp CR
	 else if(pId=='cmplx_Decision_subfeedback')
		{
			return true;
		}
	 else if(pId=='GuarantorDetails_guardianCif'){
		 if(getNGValue('GuarantorDetails_guardianCif')=='Same'){
			if(getNGValue('cmplx_Customer_guarcif')==''){	
					showAlert('cmplx_Customer_guarcif',alerts_String_Map['PL390']);
					return false;
			}
			setLocked('GuarantorDetails_cif',true);
			setNGValue('GuarantorDetails_cif',getNGValue('cmplx_Customer_guarcif'));
		}
		 else if(getNGValue('GuarantorDetails_guardianCif')=='Yes'){
				setLocked('GuarantorDetails_cif',false);
		} else if(getNGValue('GuarantorDetails_guardianCif')=='No'){
				setNGValue('GuarantorDetails_cif','');
				setLocked('GuarantorDetails_cif',false);
		} else {
				setLocked('GuarantorDetails_cif',true);
		}
		 
	 }
		
		else if(pId=='cmplx_CardDetails_SelfSupp_required')
		{
			if(getNGValue(pId)=='Yes')
			{
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
		else if(pId=='cmplx_CardDetails_SelfSupp_Limit')
		{
			if(parseFloat(getNGValue(pId))>parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')))
			{
				showAlert('cmplx_CardDetails_SelfSupp_Limit',alerts_String_Map['VAL389']);
				return false;
			}
		return  true;			
		}
		else if(pId=='SupplementCardDetails_Text1')
		{
			if(parseFloat(getNGValue(pId))>parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')))
			{
				showAlert('SupplementCardDetails_Text1','Supplementary Limit cannot be greater than' +getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit'));
				return false;
			}
		return  true;			
		}//By Alok for Supplement Card 
		else if(pId=='cmplx_CardDetails_Selected_Card_Product')
		{
			return true;			
		}
	//change by bandana ends for  PCAS-2637

	else if(pId=='cmplx_Customer_FIrstNAme' || pId=='cmplx_Customer_MiddleName' || pId=='cmplx_Customer_LAstNAme'){
		var short_name = getNGValue('cmplx_Customer_FIrstNAme').charAt(0)+" "+getNGValue('cmplx_Customer_MiddleName').charAt(0)+" "+getNGValue('cmplx_Customer_LAstNAme').charAt(0);
		setNGValueCustom('cmplx_Customer_Shortname',short_name);
		setNGValueCustom('PartMatch_fname',getNGValue('cmplx_Customer_FIrstNAme'));
		setNGValueCustom('PartMatch_lname',getNGValue('cmplx_Customer_LAstNAme'));
		setNGValueCustom('PartMatch_funame',getNGValue('cmplx_Customer_FIrstNAme') + ' ' + getNGValue('cmplx_Customer_LAstNAme'));
	}
	else if(pId=='OECD_CRSFlag'){
		//below code modified by nikhil 01/02/18
		if(getNGValue('OECD_CRSFlag')=='Y'){
			setLockedCustom('OECD_CRSFlagReason',false);
		}
		else{
			setLockedCustom('OECD_CRSFlagReason',true);
			setNGValueCustom('OECD_CRSFlagReason','');
		}	
	}
	
	else if (pId=='FATCA_USRelaton'){
		//alert(getNGValue('FATCA_USRelaton'));
		
		if (getNGValue('FATCA_USRelaton')=='O')
		{
			if(getNGValue('cmplx_Customer_Nationality')=='US'){
				showAlert('FATCA_USRelaton',alerts_String_Map['VAL151']);
				setNGValueCustom('FATCA_USRelaton',"");
			}
			else{
			Fatca_disableCondition();
			}
			setNGValueCustom('cmplx_FATCA_IdDoc',true);
			setNGValueCustom('cmplx_FATCA_DecForInd',true);
		}
		
		else if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')
		{
			Fatca_Enable();
			setNGValueCustom('cmplx_FATCA_IdDoc',true);
			setNGValueCustom('cmplx_FATCA_DecForInd',false);
			
		}
		
		else if(getNGValue('FATCA_USRelaton')=='C'){
				Fatca_disableCondition();
				setLockedCustom("cmplx_FATCA_SignedDate",false);
			}
		
		else{
			Fatca_Enable();
			}
		
	}
	
	/*else if (pId =='cmplx_Customer_EmiratesID' || pId =='cmplx_Customer_FIrstNAme' || pId =='cmplx_Customer_MiddleName' || pId =='cmplx_Customer_LAstNAme' || pId =='cmplx_Customer_DOb' || pId =='cmplx_Customer_Nationality' || pId =='cmplx_Customer_MobNo' || pId =='cmplx_Customer_PAssportNo'||pId =='cmplx_Customer_CIFNO')	
	{
		if(activityName!='CSM' && activityName!='SalesCoordinator')
	{
		
		setEnabled('FetchDetails',true);
	}
	}*/
//by shweta
	else if (pId=='cmplx_RiskRating_BusinessSeg' || pId=='cmplx_RiskRating_SubSeg' 
												 || pId=='cmplx_RiskRating_Demographics'
												 || pId=='cmplx_RiskRating_Industry'){
		setNGValueCustom("cmplx_RiskRating_Total_riskScore","");
	}
	else if(pId=='cmplx_CustDetailverification1_Dob_update' || pId=='FinacleCore_ReturnDate' || pId=='InwardTT_date'){
		
		validateFutureDateexcCurrent(pId);
	}
	else if(pId=='cmplx_CustDetailVerification_resno_ver')
	{ 
		if(getNGValue(pId)=='Mismatch'){
			setLocked("cmplx_CustDetailVerification_resno_upd",false);
		}
		else{
			setNGValueCustom("cmplx_CustDetailVerification_resno_upd","");
			setLocked("cmplx_CustDetailVerification_resno_upd",true);		
		}
	}
	else if(pId=='cmplx_CustDetailVerification_offtelno_ver')
	{ 
	  if(getNGValue(pId)=='Mismatch'){
		setLocked("cmplx_CustDetailVerification_offtelno_upd",false);
	  }
	 else{
		setNGValueCustom("cmplx_CustDetailVerification_offtelno_upd","");
		setLocked("cmplx_CustDetailVerification_offtelno_upd",true);
		}
	}
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
//by shweta to correct loan and card fragment
	else if(pId=='cmplx_LoanandCard_islorconv_ver'){
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled('cmplx_LoanandCard_islorconv_upd',true);
			setLocked('cmplx_LoanandCard_islorconv_upd',false);
		}
		else{
			setNGValueCustom('cmplx_LoanandCard_islorconv_upd',"--Select--");	
			setLocked('cmplx_LoanandCard_islorconv_upd',true);
		}
	}
	else if(pId=='cmplx_LoanandCard_loanamt_ver'){
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_LoanandCard_loanamt_upd",true);
			setLocked("cmplx_LoanandCard_loanamt_upd",false);
		}
		else{
			setNGValueCustom("cmplx_LoanandCard_loanamt_upd","");	
			setLocked("cmplx_LoanandCard_loanamt_upd",true);
		}
	}
	else if(pId=='cmplx_LoanandCard_tenor_ver'){
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_LoanandCard_tenor_upd",true);
			setLocked("cmplx_LoanandCard_tenor_upd",false);
		}
		else{
			setNGValueCustom("cmplx_LoanandCard_tenor_upd","");	
			setLocked("cmplx_LoanandCard_tenor_upd",true);
		}
	}
	else if(pId=='cmplx_LoanandCard_emi_ver'){
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_LoanandCard_emi_upd",true);
			setLocked("cmplx_LoanandCard_emi_upd",false);
		}
		else{
			setNGValueCustom("cmplx_LoanandCard_emi_upd","");	
			setLocked("cmplx_LoanandCard_emi_upd",true);
		}
	}
	else if(pId=='cmplx_LoanandCard_firstrepaydate_ver'){
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_LoanandCard_firstrepaydate_upd",true);
			setLocked("cmplx_LoanandCard_firstrepaydate_upd",false);
		}
		else{
			setNGValueCustom("cmplx_LoanandCard_firstrepaydate_upd","");	
			setLocked("cmplx_LoanandCard_firstrepaydate_upd",true);
		}
	}
	else if(pId=='cmplx_OffVerification_hrdnocntctd'){
		if(getNGValue(pId)=='Yes')
		{
			
       setLocked("cmplx_OffVerification_offtelnovalidtdfrom",false);//PCAS-2514 sagarika
	    setEnabled('cmplx_OffVerification_hrdcntctname',true);
		setLocked('cmplx_OffVerification_hrdcntctname',false);
		setLocked("cmplx_OffVerification_hrdcntctdesig",false);
        setLocked("cmplx_OffVerification_colleagueno",true);
	    setLocked("cmplx_OffVerification_colleaguename",true);
		setLocked("cmplx_OffVerification_colleaguedesig",true);
		setLocked("cmplx_OffVerification_fxdsal_ver",false);
        setLocked("cmplx_OffVerification_accpvded_ver",false);
	    setLocked("cmplx_OffVerification_doj_ver",false);
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
		setNGValueCustom("cmplx_OffVerification_hrdemailverified","--Select--");
		}
		else
		{ 
		setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);
		setEnabled('cmplx_OffVerification_hrdcntctname',false);
		setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		//Changed by shivang for PCASP-2035
		//setNGValueCustom("cmplx_OffVerification_hrdcntctname","");
		//setNGValueCustom("cmplx_OffVerification_hrdcntctdesig","");
	//	setLocked("cmplx_OffVerification_colleaguenoverified",false); hritik 24.6.21 pcasi 3442
		setLocked("cmplx_OffVerification_hrdemailverified",false);
		setLocked("cmplx_OffVerification_colleagueno",true);
		setLocked("cmplx_OffVerification_colleaguename",true);
		setLocked("cmplx_OffVerification_colleaguedesig",true);
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
	//	setLocked("cmplx_OffVerification_colleaguenoverified",false); hritik 24.6.21 pcasi 3442
	
     }
	}
	else if(pId=='cmplx_OffVerification_colleaguenoverified'){
		if(getNGValue(pId)=='Yes')
		{//sagarika PCAS-3225
		setNGValueCustom("cmplx_OffVerification_fxdsal_ver","");
		setNGValueCustom("cmplx_OffVerification_accpvded_ver","");
		setNGValueCustom("cmplx_OffVerification_desig_ver","");
		setNGValueCustom("cmplx_OffVerification_doj_ver","");	
		setNGValue("cmplx_OffVerification_hrdcntctname","");
		setNGValue("cmplx_OffVerification_hrdcntctdesig","");
		setNGValue("cmplx_OffVerification_hrdemailid","");
		
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
		{
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
	if(pId=='cmplx_OffVerification_hrdemailverified'){
		if(getNGValue(pId)=='Yes')
		{
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
		//setNGValue("cmplx_OffVerification_hrdnocntctd","--Select--");
		//setLocked("cmplx_OffVerification_hrdnocntctd",true);
		//setLocked("cmplx_OffVerification_hrdcntctdesig",true);
		
		}
		else{
				setNGValueCustom("cmplx_OffVerification_fxdsal_ver","");//sagarika PCAS-3225
		setNGValueCustom("cmplx_OffVerification_accpvded_ver","");
		setNGValueCustom("cmplx_OffVerification_desig_ver","");
		setNGValueCustom("cmplx_OffVerification_doj_ver","");
		//Changed by shivang for PCASP-2035
		//setNGValue("cmplx_OffVerification_hrdcntctname","");
		//setNGValue("cmplx_OffVerification_hrdcntctdesig","");
		//setNGValue("cmplx_OffVerification_hrdemailid","");
		//	setLocked("cmplx_OffVerification_colleaguenoverified",false); hritik 24.6.21 pcasi 3442
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
	if(pId=='cmplx_OffVerification_doj_ver'){
		if(getNGValue(pId)=='Mismatch')
		{
	    setEnabled("cmplx_OffVerification_doj_upd",true);
		setLocked("cmplx_OffVerification_doj_upd",false);
		}
		else{
			setEnabled("cmplx_OffVerification_doj_upd",true);
			setNGValueCustom("cmplx_OffVerification_doj_upd","");	
			setLocked("cmplx_OffVerification_doj_upd",true);
		}
		
	}
	if(pId=='cmplx_OffVerification_fxdsal_ver') {
		if(getNGValue(pId)=='Mismatch')
		{
			setEnabled("cmplx_OffVerification_fxdsal_upd",true);
			setLocked("cmplx_OffVerification_fxdsal_upd",false);
		}
		else{
			setEnabled("cmplx_OffVerification_fxdsal_upd",true); 
			setNGValueCustom("cmplx_OffVerification_fxdsal_upd","");
			setLocked("cmplx_OffVerification_fxdsal_upd",true);
		
		}
	}
	if(pId=='cmplx_OffVerification_cnfminjob_ver'){
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
	
	if(pId=='cmplx_OffVerification_accpvded_ver'){
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
	if(pId=='cmplx_OffVerification_desig_ver'){
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
	if(pId=='cmplx_CustDetailVerification_persorcompPOBox_ver'){

		if(getNGValue(pId)=='Mismatch')
		{
		setLocked("cmplx_CustDetailVerification_persorcompPOBox_upd",false);
		}
		else{
			setNGValueCustom("cmplx_CustDetailVerification_persorcompPOBox_upd","");
			setLocked("cmplx_CustDetailVerification_persorcompPOBox_upd",true);
		}
		
	}
	if(pId=='cmplx_CustDetailVerification_emirates_ver'){
		if(getNGValue(pId)=='Mismatch')
		{
		setLocked("cmplx_CustDetailVerification_emirates_upd",false);
		}
		else{
			setNGValueCustom("cmplx_CustDetailVerification_emirates_upd","--Select--");
		setLocked("cmplx_CustDetailVerification_emirates_upd",true);
		}
		
	}
	if(pId=='cmplx_CustDetailVerification_POBoxno_ver'){
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
	if(pId=='cmplx_cust_ver_sp2_ContactedOn')
	{
		if(getNGValue('cmplx_cust_ver_sp2_ContactedOn')=='Mismatch')
		{
			setEnabled('cmplx_cust_ver_sp2_Mobile',true);
			setLocked('cmplx_cust_ver_sp2_Mobile',false); // PCASI 3434
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
	if(pId=='cmplx_CustDetailVerification_dob_ver')
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
	
	if(pId=='cmplx_emp_ver_sp2_application_type')
	{
	if(getNGValue(pId)=="Salaried")
	{
	//alert("salaried");
        com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame2",true);
		com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame3",false);
	}
	else if(getNGValue(pId)=="Self employed")
	{
	//alert("self");
	  com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame2",true);
		com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame3",false);
	}
	else{
	com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame2",false);
	 com.newgen.omniforms.formviewer.setVisible("EmploymentVerification_s2_Frame3",false);
	}
	}
	else if(pId=='cmplx_OffVerification_fxdsal_upd'){
		setNGValueCustom(pId,setDecimalto2digits(pId));
		putComma(pId);	
	}
	//added by shweta
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
	if(pId=='cmplx_Decision_FeedbackStatus'){
		return true;

	}

	// ++ above code already present - 06-10-2017 notedesc change for note code and Feedback
	//change by saurabh for PCSP-332.
	if(pId=='AlternateContactDetails_AirArabiaIdentifier'){
		validateMail1(pId);
	}
		if(pId=='cmplx_Customer_Nationality')
	{
		//Deepak Changes done for PCAS-2799
		if(getNGValue(pId)!='--Select--')
		{
			var age=getNGValue("cmplx_Customer_age");
			//by shweta
			var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
			if(age !="" && age<21    && getNGValue("cmplx_Customer_Nationality")!='AE')
			{
				showAlert(pId,'Expat Minor Customer not Applicable for ' + prod);
				setNGValueCustom(pId,'--Select--');
				return false;
			}
			else if(age!="" && age<18.06 &&  getNGValue("cmplx_Customer_Nationality")=='AE'){
				showAlert(pId,'Customer not Applicable for ' + prod);
				setNGValueCustom(pId,'');
				return false;
			}
			else {
			if(isCustomerMinor()){
				minorCustomerFieldVisibility("true");
			} else {
				minorCustomerFieldVisibility("false");

			}
			}
		}

		var GCC="BH,IQ,KW,OM,QA,SA,AE";
		if(GCC.indexOf(getNGValue("cmplx_Customer_Nationality"))!=-1)
		{
			setNGValueCustom('cmplx_Customer_GCCNational','Y'); // pcasi 3518
			setLocked("cmplx_Customer_VisaNo",true);
			setLocked("cmplx_Customer_VisaIssuedate",true);
			setLocked("cmplx_Customer_VIsaExpiry",true);
		}
		else
		{
			setNGValueCustom('cmplx_Customer_GCCNational','N');
		}
		setNGValueCustom('PartMatch_nationality',getNGValue(pId));
	
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
	if (pId =='cmplx_Customer_CIFNO'){ 
		   var partCIF = getNGValue('cmplx_Customer_CIFNO');
		   if(partCIF !="" && partCIF.length!=7)
			{
					showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL168']);
					return false;
			}	
			setNGValueCustom('PartMatch_CIFID',getNGValue(pId));
		}
	// added by abhishek as per CC FSD
	 if (pId == "cmplx_Customer_NEP" || pId == "cmplx_Customer_NTB")
    	 {
        	if (getNGValue("cmplx_Customer_NTB") == false && getNGValue("cmplx_Customer_NEP") == '')
        	{
           	 	setVisible("IncomingDocNew_ViewSIgnature", true);
            		setVisible("IncomingDocNew_UploadSig", false);
        	}

       		 else if (getNGValue("cmplx_Customer_NEP") != '' || getNGValue("cmplx_Customer_NTB") == true && activityname!='DDVT_Checker'){
           	 	setVisible("IncomingDocNew_ViewSIgnature", false);
       		 	setVisible("IncomingDocNew_UploadSig", true);
			 }
				else if (getNGValue("cmplx_Customer_NEP") != '' || getNGValue("cmplx_Customer_NTB") == true && activityname!='DDVT_Checker'){
           	 	setVisible("IncomingDocNew_ViewSIgnature", false);
       		 	setVisible("IncomingDocNew_UploadSig", true);
				setEnabled("IncomingDocNew_UploadSig",false);
		 }
    }
	else if(pId=='transferMode'){
		if(getNGValue('transtype')=='' || getNGValue('transtype')==null){
			showAlert('transtype',alerts_String_Map['VAL397']);
			setNGValue('transferMode','');
				return false;
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
		}
		else if(getNGValue('transtype')=='BT' && (getNGValue('transferMode')=='C'|| getNGValue('transferMode')=='CHEQUE')){
			setNGValue('dispatchChannel','998');
			setLockedCustom("chequeNo",false);
			setLockedCustom("chequeDate",false);
			setLockedCustom("chequeStatus",false);
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
		}
		else if(getNGValue('transtype')=='SC' && (getNGValue('transferMode')=='C'|| getNGValue('transferMode')=='CHEQUE')){
			setLockedCustom("dispatchChannel",false);
			setNGValue('dispatchChannel','998');
			setLockedCustom("chequeNo",false);
			setLockedCustom("chequeDate",false);
			setLockedCustom("chequeStatus",false);
			setNGValue('creditcardNo','');//pcasp-1377
			setNGValue('tenor','');//pcasp-1377
			setLockedCustom("tenor",true);
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
		}
		//for PCASP-1377
		else if(getNGValue('transtype')=='CCC' && (getNGValue('transferMode')=='S'|| getNGValue('transferMode')=='SWIFT')){
			setNGValue('chequeNo','');//pcasp-1377
			setNGValue('chequeDate','');//pcasp-1377
			setNGValue('chequeStatus','');//pcasp-1377
			setNGValue('creditcardNo','');//pcasp-1377
			setNGValue('tenor','');//pcasp-1377
			setLockedCustom("chequeNo",true);
			setLockedCustom("chequeDate",true);
			setLockedCustom("chequeStatus",true);
			setNGValue('dispatchChannel','');
			setLockedCustom("dispatchChannel",true);
			setLockedCustom("tenor",true);
			setLockedCustom("creditcardNo",false);
			setNGValueCustom('bankName','');
			setLockedCustom("bankName",false);
			setLockedCustom("bankName",false);
		}
		else if(getNGValue('transtype')=='CCC' && (getNGValue('transferMode')=='C'|| getNGValue('transferMode')=='CHEQUE')){
			setLockedCustom("dispatchChannel",false);
			setNGValue('dispatchChannel','998');
			setLockedCustom("chequeNo",false);
			setLockedCustom("chequeDate",false);
			setLockedCustom("chequeStatus",false);
			setNGValue('tenor','');//pcasp-1377
			setLockedCustom("tenor",true);	
			setNGValue('creditcardNo','');//pcasp-1377
			setLockedCustom("creditcardNo",false);				
		}
		else if(getNGValue('transtype')=='CCC' && getNGValue('transferMode')=='A'){
			setNGValue('chequeDate','');//pcasp-1377
			setNGValue('chequeNo','');//pcasp-1377
			setNGValue('chequeStatus','');//pcasp-1377
			setNGValue('tenor','');//pcasp-1377	
			setNGValue('creditcardNo','');//pcasp-1377
			setNGValue('dispatchChannel','');
			setLockedCustom("dispatchChannel",true);
			setLockedCustom("chequeNo",true);
			setLockedCustom("chequeDate",true);
			setLockedCustom("chequeStatus",true);
			setLockedCustom("tenor",true);
			setLockedCustom("creditcardNo",false);
				
		}
	// ++ below code already present - 06-10-2017 transferMode	
//	return true;
		
	}
	else if (pId=='transtype') {
		if(getNGValue('transtype')=='BT'){
			//code added by deepak on 071020170- to blank the fileds which are disabled on changing the transaction type. Ref07102017-trsn001
			//setNGValueCustom('tenor','');//Tarang to be removed on friday(1/19/2018)
			setNGValueCustom('tenure','');
			setNGValueCustom('creditcardNo','');
			setLockedCustom("creditcardNo",false);
			setLockedCustom("tenor",true);
			setNGValue("cmplx_CC_Loan_cmplx_btc_payment_purpose",'');//PCASP-3186
			setVisible("CC_Loan_Label25",false);
			setVisible("cmplx_CC_Loan_cmplx_btc_payment_purpose",false);
			
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
		//setVisible("CC_Loan_Label25",true);
		setVisible("cmplx_CC_Loan_cmplx_btc_payment_purpose",true);
	}
	else if(getNGValue('transtype')=='LOC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM'){
	showAlert('transtype','Loan On card is only applicable for IM');
	setNGValueCustom('transtype','');
	setNGValue('creditcardNo','');
	setLockedCustom("creditcardNo",true);
	setNGValue("cmplx_CC_Loan_cmplx_btc_payment_purpose",'');//PCASP-3186
	setVisible("CC_Loan_Label25",false);
	setVisible("cmplx_CC_Loan_cmplx_btc_payment_purpose",false);
	return false;
	}
	else {
		setLockedCustom("tenor",false);
		setNGValue("cmplx_CC_Loan_cmplx_btc_payment_purpose",'');//PCASP-3186
		setVisible("CC_Loan_Label25",false);
		setVisible("cmplx_CC_Loan_cmplx_btc_payment_purpose",false);
		var rows = getLVWRowCount('cmplx_CC_Loan_cmplx_btc');
		var flag = true;
		for(var i=0;i<rows;i++){
			if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)==getNGValue('Account_Number')){
				flag = false;
				break;
			}
		}
		if(flag){
		setNGValueCustom('creditcardNo',getNGValue('Account_Number'));
		setNGValueCustom('benificiaryName',getNGValue("cmplx_Customer_FirstNAme")+' '+getNGValue('cmplx_Customer_MiddleNAme')+' '+getNGValue("cmplx_Customer_LastNAme"));
		}
		setLockedCustom("creditcardNo",false);
		setLockedCustom("bankName",false);
		
	}
		
// ++ Above code already present - 06-10-2017 transtype
//return true;
}
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
		else
		{
			setLockedCustom("cmplx_CC_Loan_DDSAmount",true);//Added by shweta for pcasp-1504
			setLockedCustom("cmplx_CC_Loan_Percentage",true);//Added by shweta for pcasp-1504
			setNGValueCustom("cmplx_CC_Loan_Percentage",'');//Added by shivang for pcasp-1504
			setNGValueCustom("cmplx_CC_Loan_DDSAmount",'');//Added by shivang for pcasp-1504
		}
	}
	  //-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

	
	// added by abhishek as per CC FSD
	else if(pId=='cmplx_CC_Loan_DDSExecDay'){
		var execDay = getNGValue('cmplx_CC_Loan_DDSExecDay');
		if(execDay <1 || execDay >31){
			showAlert('cmplx_CC_Loan_DDSExecDay',alerts_String_Map['CC158']);
			return false;
		}
		return true;
	}
	
	else if(pId=='cmplx_CC_Loan_SI_day'){
		var execDay = getNGValue('cmplx_CC_Loan_SI_day');
		if(execDay <1 || execDay >28){
			showAlert('cmplx_CC_Loan_SI_day',alerts_String_Map['CC157']);
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
	 //by shweta percentage validation
		if(pId=='cmplx_LoanDetails_lpf' || pId=='cmplx_LoanDetails_insur'){
			if(getNGValue(pId)!=''){
				if(parseFloat(getNGValue(pId))>100 || parseFloat(getNGValue(pId))<0){
					showAlert(pId,"Please Enter a valid percentage!"); 
					setNGValue(pId,'');
					return false;
				}
			}
		}
		else if(pId=='cmplx_CC_Loan_ModeOfSI'){
			if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='F'){
					setLockedCustom('cmplx_CC_Loan_FlatAMount',false);
					setLockedCustom('cmplx_CC_Loan_SI_Percentage',true);
					setNGValue('cmplx_CC_Loan_SI_Percentage','');
				}
				else if(getNGValue('cmplx_CC_Loan_ModeOfSI')==''){
					setLockedCustom('cmplx_CC_Loan_FlatAMount',false);
					setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
					setNGValue('cmplx_CC_Loan_FlatAMount','');
					setNGValue('cmplx_CC_Loan_SI_Percentage','');

				}
				else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='P'){
					setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
					setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
					setNGValue('cmplx_CC_Loan_FlatAMount','');
					setNGValue('cmplx_CC_Loan_SI_Percentage','100');
				}
			else{
					setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
					setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
				}
			}
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
	
	else if(pId=='cmplx_Customer_DOb')
	{
		if(getNGValue(pId)!='')//sagarika CR
		{
		if(validateFutureDate(pId)){
		var age=calcAge(getNGValue(pId),'');//modified by akshay on 14/10/17
		
			var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
			if(age<21 && getNGValue("cmplx_Customer_Nationality")!="" && getNGValue("cmplx_Customer_Nationality")!='AE')
			{
				showAlert(pId,'Expat Minor Customer not Applicable for ' + prod);
				setNGValueCustom(pId,'');
				return false;
			}//end by sagarika
			else if(age<18.06 &&  getNGValue("cmplx_Customer_Nationality")=='AE'){
				showAlert(pId,'Customer not Applicable for ' + prod);
				setNGValueCustom(pId,'');
				return false;
			}
			else {
				YYMM('','cmplx_Customer_age',age);
				if( isCustomerMinor()){
					minorCustomerFieldVisibility("true");
				} else {
					minorCustomerFieldVisibility("false");
				}
				
			}
		 }
		}
		setNGValueCustom('PartMatch_Dob',getNGValue(pId));
	}
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
	//case added by saurabh for task list points on 11/4/19.
	else if(pId=='cmplx_BussVerification1_ActualDOB' || pId=='cmplx_CustDetailverification1_Dob_update' || pId=='SupplementCardDetails_ReceivedDate')
	{
		validateFutureDate(pId);
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
	else if(pId=='SupplementCardDetails_IDIssueDate')
	{
		validateFutureDate(pId);
	}
	else if(pId=='SupplementCardDetails_PassportIssueDate')
	{
		validateFutureDate(pId);
	}
	//case added by saurabh for task list points on 11/4/19.
	else if(pId=='cmplx_BussVerification1_ActualDOB' || pId=='cmplx_CustDetailverification1_Dob_update' || pId=='SupplementCardDetails_ReceivedDate')
	{
		validateFutureDate(pId);
	}
	else if(pId=='IncomingDocNew_DeferredUntilDate'){
		validatePastDate(pId,'Deferred Until');
	}
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
	//Code by bandana added
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
		//setNGValueCustom('IncomingDocNew_Status',''); //commented by Vaishvi for PCASI - 3501
		setNGValueCustom('IncomingDocNew_Remarks','');
		setNGValueCustom('IncomingDocNew_Docindex','');
		return false;
	}
	else if(pId=='IncomingDocNew_Status'){
		if(getNGValue(pId)=='Deferred'){
			setLockedCustom('IncomingDocNew_DeferredUntilDate',false);
			setNGValue('IncomingDocNew_ExpiryDate','');
		}
		else if(getNGValue(pId)=='Received'){
			var today = new Date();
			var date = today.getDate();
			var month = today.getMonth()+1;
			var year = today.getFullYear();
			if(date<10){
				var date = "0"+date;
			}
			else{
				var date=date;
			}
			if(month<10){
				var format = date+"/0"+month+"/"+year;
			}
			else{
				var format = date+"/"+month+"/"+year;
			}
			setNGValue('IncomingDocNew_ExpiryDate',format);
		}
		else{
			setLockedCustom('IncomingDocNew_DeferredUntilDate',true);
			setNGValue('IncomingDocNew_ExpiryDate','');
		}
	}
	else if(pId=='IncomingDocNew_DeferredUntilDate'){
		validatePastDate(pId,'Deferred Until');
	}
	//below function by saurabh for incoming doc new end
	//Code by bandana end
	else if(pId=='GuarantorDetails_dob')
	{
		if(validateFutureDate(pId))
		{
			var age=calcAge(getNGValue(pId),'');
			YYMM('','GuarantorDetails_age',age);
			
		}
	}
	
	else if(pId=='cmplx_CustDetailVerification_dob_upd')
		validateFutureDate(pId);
	
	else if(pId=='cmplx_OffVerification_doj_upd') {
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
		
	//above arun 12/12/17 for CPv validation
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

	//below code added by nikhil 11/12/17
	else if(pId=='cmplx_LoanDetails_moratorium')
	{
		if (getNGValue('cmplx_LoanDetails_moratorium') !=''){
			var firstRepayDate=Calc_FirstRepaymentDate(getNGValue(pId));
			setNGValue('cmplx_LoanDetails_frepdate',firstRepayDate);

			setNGValue('LoanDetails_duedate',firstRepayDate);

			var tenor=getNGValue('cmplx_LoanDetails_tenor');
			var maturityDate=MaturityDate_2(firstRepayDate,tenor);
			setNGValue('cmplx_LoanDetails_maturitydate',maturityDate);
			calcAgeAtMaturityLoanDetails();
		}
	}
	
		else if(pId=='cmplx_LoanDetails_fdisbdate')
		{
			var moratorium = Moratorium('cmplx_LoanDetails_frepdate');
	    setNGValue('cmplx_LoanDetails_moratorium',moratorium); //hritik 3801
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
	else if(pId=='cmplx_Customer_VIsaExpiry')
	{
		var activityName = window.parent.stractivityName;
		validatePastDate(pId,'Visa Expiry');
		if(isFieldFilled('cmplx_Customer_VisaNo')==false){
			if(activityName!='DDVT_maker'){
			showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL351']);
			setNGValueCustom(pId,'');
			return false;
		}
	}}
	
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
			  	//pcasi-2659
				 setNGValue("LoanDetails_duedate",getNGValue("cmplx_LoanDetails_frepdate"));

				var today = d.getDate()+'/'+d.getMonth()+'/'+d.getFullYear();
				var moratorium = Moratorium(pId);
				//var months = (parseInt(moratorium.split('.')[0])*12)+parseInt(moratorium.split('.')[1])-1;
				setNGValue('cmplx_LoanDetails_moratorium',moratorium);
				var firstRepayDate=getNGValue(pId);
		//setNGValue('cmplx_LoanDetails_frepdate',firstRepayDate);
				var tenor=getNGValue('cmplx_LoanDetails_tenor');
				setNGValue('cmplx_LoanDetails_maturitydate',MaturityDate_2(firstRepayDate,tenor));
				calcAgeAtMaturityLoanDetails();
				setNGValue('cmplx_LoanDetails_is_repaymentSchedule_generated','N');
		  }
	}
	//else if(pId=='LoanDetails_payreldate')
//jahnavi	{}
	// ++ above code already present - 09-10-2017
	else if(pId=='cmplx_Customer_PassPortExpiry')
	{
		validatePastDate(pId,'Passport Expiry');
		if(isFieldFilled('cmplx_Customer_PAssportNo')==false){
			showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL352']);
			setNGValueCustom(pId,'');
			return false;
		}
	}
else if(pId=='LoanDetails_loanamt')
{
setNGValue('LoanDetails_amt',getNGValue('LoanDetails_loanamt'));
}	
	
	//++below code added by nikhil for toteam
	else if(pId=='PostDisbursal_Expiry_Date')
	{
		//validatePastDate(pId,'Expiry Date');
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
		setNGValueCustom('PartMatch_EID',getNGValue(pId));
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
	 /*	if(parseInt(getNGValue(pId))<=0 || parseInt(getNGValue(pId))>301){
				showAlert(pId,"Tenor should be between 0 and 300");
				setNGValue(pId,'');
				return false;
		}	*/
		putComma(pId);
	}
	//added by akshay on 9/5/18 for proc 9240
	else if(pId=='cmplx_LoanDetails_lonamt')
	{
		/*var maxLoanAmount=getNGValue('LoanDetails_MaxLoanAmount');
		if(parseInt(getNGValue(pId))<=0 || parseInt(getNGValue(pId))>maxLoanAmount){
			showAlert(pId,"Loan Amount should be between 0 and "+maxLoanAmount);
			setNGValue(pId,'');
			return false;
		}*/	
		if(parseInt(getNGValue(pId))<=0){
			showAlert(pId,"Loan Amount should be greater than 0.");
			setNGValue(pId,'');
			return false;
		}
		return true;
	}
	else if(pId=='passExpiry' || pId=='GuarantorDetails_passExpiry')
	{
		validatePastDate(pId,'Passport Expiry');
	}
	// ++ below code already present - 09-10-2017 code optimised
	else if(pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other' ||pId=='cmplx_IncomeDetails_Pension'||pId=='cmplx_IncomeDetails_RentalIncome'||pId=='cmplx_IncomeDetails_EducationalAllowance'){
		//setNGValue(pId,setDecimalto2digits(pId));
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
	
	else if(pId=='cmplx_IncomeDetails_UnderwritingOther')
	{
		setNGValue(pId,setDecimalto2digits(pId));
		putComma(pId);
	}
	
	// disha FSD
	else if(pId=='cmplx_IncomeDetails_SalaryDay')
	{
		var date = parseInt(getNGValue(pId));
		if(date<1 || date>31 && getNGValue(pId)!=""){
			showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['VAL102']);
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
	else if (pId=='cmplx_LoanDetails_marginrate'){
		var marginRate= getNGValue('cmplx_LoanDetails_marginrate');
		var BaseRate= getNGValue('cmplx_LoanDetails_baserate'); 
		var ProdPrefRate= getNGValue('cmplx_LoanDetails_pdtpref');
		setNGValue('cmplx_LoanDetails_netrate',(parseFloat(marginRate)+parseFloat(BaseRate)+parseFloat(ProdPrefRate)).toFixed(2));
		setNGValue('cmplx_EligibilityAndProductInfo_InterestRate',(parseFloat(marginRate)+parseFloat(BaseRate)+parseFloat(ProdPrefRate)).toFixed(2));	//PCASI 3605
		
	}
	else if (pId=='cmplx_EligibilityAndProductInfo_InterestRate')
	{
		var Intrate= getNGValue('cmplx_EligibilityAndProductInfo_InterestRate');
		var BaseRate= getNGValue('cmplx_EligibilityAndProductInfo_BAseRate'); 
		var ProdPrefRate= getNGValue('cmplx_EligibilityAndProductInfo_ProdPrefRate');
		setNGValue('cmplx_EligibilityAndProductInfo_MArginRate',parseFloat(Intrate)-parseFloat(BaseRate)-parseFloat(ProdPrefRate));
		setNGValue('cmplx_EligibilityAndProductInfo_FinalInterestRate',parseFloat(Intrate));
		setNGValue('cmplx_EligibilityAndProductInfo_InterestRate',parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_InterestRate')).toFixed(2));
		//change by saurabh on 7th Dec
		
		//added by disha for CSM Interest_Rate_Approval Flag on 6Th April 2018
		//Removed IM condition; In PL
		if(activityName=='CSM' || activityName=='Telesales_RM')
		{
			setNGValueCustom("Interest_Rate_App_req","Y");  //added by disha for CSM Interest_Rate_Approval Flag on 6Th April 2018
		}
		
		return true;
	}
	//change by saurabh on 7th Dec
	else if(pId=='cmplx_EligibilityAndProductInfo_Tenor' || pId=='cmplx_LoanDetails_lpf'){
		return true;
	}
	// ++ above code already present - 09-10-2017
	if(pId=='cmplx_Decision_FeedbackStatus')
	{
		return true;
	}
	
	else if(pId=='cmplx_LoanDetails_tenor')
	{
		var firstRepayDate=getNGValue('cmplx_LoanDetails_frepdate');
		var tenor=getNGValue(pId);
		var maturityDate=MaturityDate_2(firstRepayDate,tenor);
		setNGValue('cmplx_LoanDetails_maturitydate',maturityDate);
		calcAgeAtMaturityLoanDetails();
		return true;
	}
	// ++ below code already present - 09-10-2017 code optimised
	else if(pId=='cmplx_IncomeDetails_netSal1' || pId=='cmplx_IncomeDetails_netSal2' || pId=='cmplx_IncomeDetails_netSal3'){
		setNGValueCustom(pId,setDecimalto2digits(pId));
		putComma(pId);	
		setNGValueCustom("cmplx_IncomeDetails_AvgNetSal",calcAvgNet());
		putComma('cmplx_IncomeDetails_AvgNetSal');	
	}
	
	else if(pId=='cmplx_IncomeDetails_BankStatFromDate')
	{
		validateFutureDate(pId);
		if(CompareFrom_ToDate(pId,'IncomeDetails_BankStatToDate')==false){
			document.getElementById(pId).value=null;
		}
	}
			
	else if(pId=='cmplx_IncomeDetails_BankStatToDate')
	{
		validateFutureDate(pId);
		if(CompareFrom_ToDate('IncomeDetails_BankStatFromDate',pId)==false){
			document.getElementById(pId).value=null;	
		}	
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
		if(getNGValue(pId)=='S' || getNGValue(pId)=='BAU') // hritik PCASI 3751
			{
				setVisible('EMploymentDetails_Label59',true);
				setVisible('cmplx_EmploymentDetails_IndusSeg',true);
			}
		else
			{
				setVisible('EMploymentDetails_Label59',false);
				setVisible('cmplx_EmploymentDetails_IndusSeg',false);
			}
		return true;
	}
	else if(pId=='cmplx_Customer_PAssportNo')
	{
		setNGValueCustom('PartMatch_newpass',getNGValue(pId));
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
	else if(pId=='cmplx_EligibilityAndProductInfo_Tenor'){
		calAgeAtMaturity();
		return true;
	}
		
	else if(pId=='VisaExpiryDate')
	{
		validatePastDate(pId,'Visa Expiry');	
	}
	
	//below code added by siva on 04112019 for PCAS-1268
	else if(pId=='cmplx_Decision_SetReminder'){
	//changes done by nikhil for PCAS-2511
      validatePastDate(pId,'');
		NoOfAttemptsValue=NoOfAttemptsValue+1;
		if (getNGValue('cmplx_Decision_NoofAttempts')==''){
		setNGValueCustom('cmplx_Decision_NoofAttempts','1');
		}
		else if (getNGValue('cmplx_Decision_NoofAttempts')!=''&& NoOfAttemptsValue==1){
		var toSet;
		toSet=parseInt(getNGValue('cmplx_Decision_NoofAttempts'))+1;
		setNGValueCustom('cmplx_Decision_NoofAttempts',toSet);
		}
		
	}
	//code ended by siva on 04112019 for PCAS-1268
	else if(pId=='cmplx_Decision_SetReminder_CA'){
		//changes done by nikhil for PCAS-2511
	      validatePastDate(pId,'');
			NoOfAttemptsValue_CA=NoOfAttemptsValue_CA+1;
			if (getNGValue('cmplx_Decision_NoOfAttempts_CA')==''){
			setNGValueCustom('cmplx_Decision_NoOfAttempts_CA','1');
			}
			else if (getNGValue('cmplx_Decision_NoOfAttempts_CA')!=''&& NoOfAttemptsValue_CA==1){
			var toSet;
			toSet=parseInt(getNGValue('cmplx_Decision_NoOfAttempts_CA'))+1;
			setNGValueCustom('cmplx_Decision_NoOfAttempts_CA',toSet);
			}		
		}	
	else if(pId=='cmplx_Decision_SetReminder_Smart'){
		//changes done by nikhil for PCAS-2511
	      validatePastDate(pId,'');
			NoOfAttemptsValue_Smart=NoOfAttemptsValue_Smart+1;
			if (getNGValue('cmplx_Decision_NoOfAttempts_Smart')==''){
			setNGValueCustom('cmplx_Decision_NoOfAttempts_Smart','1');
			}
			else if (getNGValue('cmplx_Decision_NoOfAttempts_Smart')!=''&& NoOfAttemptsValue_Smart==1){
			var toSet;
			toSet=parseInt(getNGValue('cmplx_Decision_NoOfAttempts_Smart'))+1;
			setNGValueCustom('cmplx_Decision_NoOfAttempts_Smart',toSet);
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
	//added By Akshay
	else if(pId=='cmplx_EmploymentDetails_EmpIndusSector')
		return true;
		
	else if(pId=='cmplx_EmploymentDetails_Indus_Macro')
		return true;
	//ended by Akshay
	else if(pId=='AddressDetails_years'){
		YYMM(pId,'','');
	}	
	else if(pId=='NotepadDetails_Dateofcalling')
	{
		validateFutureDateexcCurrent(pId);
		//setNGValueCustom(pId,'NotepadDetails_Dateofcalling');
		//return false;
	}
		
	else if(pId=='cmplx_EligibilityAndProductInfo_FirstRepayDate')
{	
		 if(validatePastDate(pId,'First Repayment')){
			var FirstRepaymentDate = getNGValue(pId);	
			var tenor=getNGValue('cmplx_EligibilityAndProductInfo_Tenor');
			var maturityDate=MaturityDate_2(FirstRepaymentDate,tenor);
			setNGValueCustom("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
		
			calcAgeAtMaturity();
		    var Days= Moratorium(pId);
		    setNGValueCustom('cmplx_EligibilityAndProductInfo_Moratorium',Days);
		
		 }
		 }
	
	else if(pId=='cmplx_EligibilityAndProductInfo_MaturityDate')	
		validatePastDate(pId,'Maturity');
	
	else if(pId=='cmplx_EligibilityAndProductInfo_Moratorium')
	{
		if (getNGValue('cmplx_EligibilityAndProductInfo_Moratorium') !=''){
			var firstRepayDate=Calc_FirstRepaymentDate(getNGValue(pId));
			setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',firstRepayDate);
			var maturityDate=MaturityDate_1();
			setNGValueCustom("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
			calcAgeAtMaturity();
		}
	}
	else if(pId=='cmplx_Customer_Dsa_NAme')
		{
			setNGValueCustom('cmplx_Customer_DSA_Code',getNGValue('cmplx_Customer_Dsa_NAme'));
		}	
	else if(pId=='bankName')
		{
			setNGValueCustom('CC_Loan_Bank_code',getNGValue(pId));
	//Added by shivang for PCASP-2156
			setNGValueCustom('bankCode',getNGValue(pId));
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
	}//by shweta
	
	else if (pId=='AlternateContactDetails_EMAIL1_PRI' ||pId=='AlternateContactDetails_EMAIL2_SEC')
		validateMail1(pId);
	
	else if (pId=='AlternateContactDetails_EMAIL1_PRI' && (validateMail1('AlternateContactDetails_EMAIL2_SEC') || getNGValue('AlternateContactDetails_EMAIL2_SEC')==''))
		setNGValueCustom('AlternateContactDetails_EMAIL2_SEC',getNGValue('AlternateContactDetails_EMAIL1_PRI'));

	
	//Skyward Number Validations
	else if(pId=='AlternateContactDetails_EnrollRewardsIdentifier')
	{  
	  var enroll=getNGValue(pId);
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
	//Supplementary Skyward Validation
	else if(pId=='SupplementCardDetails_Text2')
	{
		var enroll=getNGValue(pId);
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
				showAlert('SupplementCardDetails_Text2','Incorrect Skyward Number. Please enter a valid Skyward Number');
	
			}
		 }
		else 
		{
			showAlert('SupplementCardDetails_Text2','Incorrect Skyward Number. Please enter a valid Skyward Number'); 
		}
		}
		else 
		{
			showAlert('SupplementCardDetails_Text2','Incorrect Skyward Number. Please enter a valid Skyward Number'); 
		}
	}
	//below code changed by nikhil
	else if (pId=='cmplx_CustDetailVerification_email1_upd' || pId=='cmplx_CustDetailVerification_email2_upd')
		validateMail1(pId);//Arun 12/12/17 to CPV
	
	else if (pId=='cmplx_CustDetailVerification_email2_upd' || pId=='SupplementCardDetails_EmailID' || pId=='cmplx_OffVerification_hrdemailid')//Arun 12/12/17 to CPV
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
	
	//Deepak 23 Dec changes done as per Mansih Point
	else if(pId=='cmplx_CustDetailVerification_mobno1_ver' || pId=='cmplx_CustDetailVerification_mobno2_ver' || pId=='cmplx_CustDetailVerification_dob_ver' || pId=='cmplx_CustDetailVerification_POBoxno_ver' || pId=='cmplx_CustDetailVerification_emirates_ver' || pId=='cmplx_CustDetailVerification_persorcompPOBox_ver' || pId=='cmplx_CustDetailVerification_offtelno_ver' || pId=='cmplx_CustDetailVerification_email1_ver' || pId=='cmplx_CustDetailVerification_email2_ver' || pId=='cmplx_CustDetailVerification_Mother_name_ver'){//pcasi-1003
		setNGValueCustom('cmplx_CustDetailVerification_Decision','');
	}
	else if(pId=='cmplx_OffVerification_hrdemailverified' || pId=='cmplx_OffVerification_fxdsal_ver' || pId=='cmplx_OffVerification_accpvded_ver' || pId=='cmplx_OffVerification_desig_ver' || pId=='cmplx_OffVerification_doj_ver' || pId=='cmplx_OffVerification_cnfminjob_ver'){
		setNGValueCustom('cmplx_OffVerification_Decision','');
	}
	else if(pId=='cmplx_LoanandCard_loanamt_ver' || pId=='cmplx_LoanandCard_tenor_ver'|| pId=='cmplx_LoanandCard_emi_ver' || pId=='cmplx_LoanandCard_islorconv_ver' || pId =='cmplx_LoanandCard_firstrepaydate_ver'){
		setNGValueCustom('cmplx_LoanandCard_Decision','');
	}
	
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
		
		/*if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N' || getNGValue('FATCA_USRelaton')=='O' || getNGValue('FATCA_USRelaton')=='C')
		{
			var today = new Date();
			var date = today.getDate();
			var month = today.getMonth()+1;
			var year = today.getFullYear();
			var format = date+"/"+month+"/"+year;
			setNGValue('cmplx_FATCA_SignedDate',format);
		}//Done by Alok Tiwari for Signed date*/
		
		if (getNGValue('FATCA_USRelaton')=='O')
		{
			if(getNGValue('cmplx_Customer_Nationality')=='US'){
				showAlert('FATCA_USRelaton',alerts_String_Map['PL352']);
				setNGValue('FATCA_USRelaton',"");
			}
			/*if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N' || getNGValue('FATCA_USRelaton')=='O' || getNGValue('FATCA_USRelaton')=='C')
		{
			var today = new Date();
			var date = today.getDate();
			var month = today.getMonth()+1;
			var year = today.getFullYear();
			var format = date+"/"+month+"/"+year;
			setNGValue('cmplx_FATCA_SignedDate',format);
		}*/
			else{
				Fatca_disableCondition();
				setLocked('cmplx_FATCA_ListedReason',true);
				setLocked('cmplx_FATCA_SelectedReason',true);
			}

			setNGValue('cmplx_FATCA_IdDoc',true);
			setNGValue('cmplx_FATCA_DecForInd',true);
		}
		
		
		else if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')
		{
			Fatca_Enable();
			setNGValue('cmplx_FATCA_IdDoc',true);
			setNGValue('cmplx_FATCA_DecForInd',false);
		}
		if(getNGValue('FATCA_USRelaton')=='R'){
			setNGValue('cmplx_FATCA_W9Form',true);
		} 
		//3725
		if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')
		{
			var today = new Date();
			var date = today.getDate();
			var month = today.getMonth()+1;
			var year = today.getFullYear();
			if(date<10){
				var date = "0"+date;
			}
			else{
				var date=date;
			}
			if(month<10){
				var format = date+"/0"+month+"/"+year;
			}
			else{
				var format = date+"/"+month+"/"+year;
			}
			setNGValue('cmplx_FATCA_SignedDate',format);
		}
		
		if(getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')
		 {
			var today = new Date();
			var date_1 = today.getDate();
			var month_1 = today.getMonth()+1;
			var year_1 = today.getFullYear()+3;
			if(date_1<10){
				var date_1 = "0"+date_1;
			}
			else{
				var date_1=date_1;
			}
			if(month_1<10)
			{
				var format = date_1+"/0"+month_1+"/"+year_1;
			}
			else
			{
				var format = date_1+"/"+month_1+"/"+year_1;
			}
				
			setNGValue('cmplx_FATCA_ExpiryDate',format);
		}	
		
		else if(getNGValue('FATCA_USRelaton')=='C'){
				Fatca_disableCondition();
				setLocked("cmplx_FATCA_SignedDate",false);
			}
			else if(getNGValue('FATCA_USRelaton')=='O')
			{
				Fatca_disableCondition();
				setEnabledCustom("FATCA_TINNo",false);
			setEnabledCustom("FATCA_SignedDate",false);
			setLockedCustom("FATCA_SignedDate",true);
			setEnabledCustom("FATCA_ExpiryDate",false);
			setLockedCustom("FATCA_ExpiryDate",true);
			setEnabledCustom("cmplx_FATCA_Category",true);
			setEnabledCustom("FATCA_ControllingPersonUSRel",false);
			setEnabledCustom("cmplx_FATCA_ListedReason",false);
			setEnabledCustom("cmplx_FATCA_SelectedReason",false);
			setEnabledCustom("FATCA_Button1",false);
			setEnabledCustom("FATCA_Button2",false);
			}
		else
		{
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
			setEnabledCustom("cmplx_FATCA_Category",true);
			setEnabledCustom("FATCA_ControllingPersonUSRel",true);
			setEnabledCustom("cmplx_FATCA_ListedReason",true);
			setEnabledCustom("cmplx_FATCA_SelectedReason",true);
			setEnabledCustom("FATCA_Button1",true);
			setEnabledCustom("FATCA_Button2",true);
			}
	} //Arun from CC as per FSD
	else if(pId=='cmplx_FATCA_SignedDate' || pId=='FATCA_USRelaton' || pId=='cmplx_FATCA_CustomerType'){ //hritik
		if(validateFutureDate(pId,'Signed Date'))
		{
		  
			var signedDate=getNGValue('cmplx_FATCA_SignedDate');
			var years_signedDate=signedDate.substring(6,10);
			var years_expiryDate=parseInt(years_signedDate)+3;
			var ExpiryDate=signedDate.replace(years_signedDate,years_expiryDate);
			setNGValueCustom('cmplx_FATCA_ExpiryDate',ExpiryDate);	
		  
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
	else if(pId=='cmplx_EligibilityAndProductInfo_EFCHidden')
	{	
		if(activityName=='CAD_Analyst1')
		{
		dectech_click_reminder=true;
		showAlert('','Additional limit changed,Trigger the dectech call again');
		return false;
		}
	}
	else if(pId=='cmplx_Decision_Feedbackstatus')
	{
		return true;
	}
	
	
		else if(pId=='cmplx_CustDetailVerification_Decision' || pId=='cmplx_OffVerification_Decision' || pId=='cmplx_LoanandCard_Decision' || pId=='cmplx_HCountryVerification_Decision' || pId=='cmplx_ResiVerification_Decision' || pId=='cmplx_GuarantorVerification_Decision')
	{
		//Deepak 23 Dec condiotn added for CPV PCSP-184
		if(getNGValue('cmplx_Decision_Decision')!='--Select--' && getNGValue('cmplx_Decision_Decision')!=null){
			showAlert(pId,'Value can not be changed as final decision is already selected');
			//change done by nikhil cpv 16-04
			setNGValueCustom(pId,'--Select--');
			return false;
			
		}
		if(getNGValue(pId)=='Positive'  || getNGValue(pId)=='Approve Sub to CIF'){
			var ver;

			if(pId=='cmplx_CustDetailVerification_Decision'){
			  ver="cmplx_CustDetailVerification_mobno1_ver:cmplx_CustDetailVerification_mobno2_ver:cmplx_CustDetailVerification_dob_ver:cmplx_CustDetailVerification_POBoxno_ver:cmplx_CustDetailVerification_emirates_ver:cmplx_CustDetailVerification_persorcompPOBox_ver:cmplx_CustDetailVerification_offtelno_ver:cmplx_CustDetailVerification_email1_ver:cmplx_CustDetailVerification_email2_ver:cmplx_CustDetailVerification_hcountrytelno_ver:cmplx_CustDetailVerification_hcontryaddr_ver:cmplx_CustDetailVerification_Mother_name_ver";
			
			 if(!checkMandatory(PL_Custdetail_CPV))
				{
				 	setNGValueCustom(pId,'');
					return false;
				}
			}

			else if(pId=='cmplx_OffVerification_Decision'){
				ver="cmplx_OffVerification_hrdemailverified:cmplx_OffVerification_fxdsal_ver:cmplx_OffVerification_accpvded_ver:cmplx_OffVerification_desig_ver:cmplx_OffVerification_doj_ver:cmplx_OffVerification_cnfminjob_ver";				
				if(!checkMandatory(PL_OffVerification_CPV))
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
			
			else if(pId=='cmplx_LoanandCard_Decision'){
				ver="cmplx_LoanandCard_loanamt_ver:cmplx_LoanandCard_tenor_ver:cmplx_LoanandCard_emi_ver:cmplx_LoanandCard_islorconv_ver:cmplx_LoanandCard_firstrepaydate_ver;//:cmplx_LoanandCard_cardtype_ver:cmplx_LoanandCard_cardlimit_ver";		
				 if(!checkMandatory(PL_LoanCard_CPV))
					{
						setNGValueCustom(pId,'');
						return false;
					}
			}
			
			
			else if(pId=='cmplx_HCountryVerification_Decision'){
				if(!checkMandatory(PL_HOMEVERIFICATION))
					{
						setNGValueCustom(pId,'');
						return false;
					}
			}

			else if(pId=='cmplx_ResiVerification_Decision'){//by shweta
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
						setNGValueCustom(pId,'');
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
				setNGValue('IS_Approve_Cif','N');
			}
		}	
	}
	
	else if(pId=='cmplx_Decision_Decision'){
		var activityName = window.parent.stractivityName;
		var desc = getNGValue('cmplx_Decision_Decision');
		if (getNGValue(pId).toUpperCase()!='REFER'){
			com.newgen.omniforms.formviewer.NGClear('cmplx_Decision_ReferTo');
			setLockedCustom('cmplx_Decision_ReferTo',true);
		}
		if(getNGValue(pId).toUpperCase()!='REJECT'){
			com.newgen.omniforms.formviewer.NGClear('DecisionHistory_DecisionReasonCode');
			com.newgen.omniforms.formviewer.NGClear('DecisionHistory_DecisionSubReason');

		}
		if(getNGValue(pId)=='Refer')
			{
				setNGValue('prevws_refer_color','Y');  //by jahnavi for color code orange if wi referred
			}		
			if(activityName=='Disbursal_Checker')
		{
			if(getNGValue('cmplx_LoanDisb_AgreementID')=='')
			{	
				setLocked('PostDisbursal_Checklist_Frame2',true);
				setEnabled('PostDisbursal_Checklist_Frame2',false);
				setLocked('PostDisbursal_Checklist_Frame1',true);
				setEnabled('PostDisbursal_Checklist_Frame1',false);
				setLocked('PostDisbursal_Checklist_Frame3',true);
				setEnabled('PostDisbursal_Checklist_Frame3',false);
				setLocked('PostDisbursal_Checklist_Frame4',true);
				setEnabled('PostDisbursal_Checklist_Frame4',false);
				if((getLVWAT("cmplx_Product_cmplx_ProductGrid",0,0)=='Islamic') && (desc =='Approve and Exit' || desc=='Approve and Post Disbursal' || desc=='Approve and Takeover' || desc=='Reject'||desc=='Approve to LMS Push'))
				{	
					setNGValue('cmplx_Decision_Decision','');
					showAlert('cmplx_Decision_Decision','Cannot select this decision as agreement ID is not generated yet! Please approve the workitem to generate the agreement id ');
					return false;
				}
				else if((getLVWAT("cmplx_Product_cmplx_ProductGrid",0,0)=='Conventional') && (desc =='Approve and Exit' || desc=='Approve and Post Disbursal' || desc=='Approve and Takeover'||desc=='Reject'))
				{	
					setNGValue('cmplx_Decision_Decision','');
					showAlert('cmplx_Decision_Decision','Cannot select this decision as agreement ID is not generated yet! Please approve the workitem to LMS Push generate the agreement id ');
					return false;
				}
				
			}
			else if(getNGValue('cmplx_LoanDisb_AgreementID')!='')
			{	
				setLocked('PostDisbursal_Checklist_Frame2',false);
				setEnabled('PostDisbursal_Checklist_Frame2',true);
				setLocked('PostDisbursal_Checklist_Frame1',false);
				setEnabled('PostDisbursal_Checklist_Frame1',true);
				setLocked('PostDisbursal_Checklist_Frame3',false);
				setEnabled('PostDisbursal_Checklist_Frame3',true);
				setLocked('PostDisbursal_Checklist_Frame4',true);
				setEnabled('PostDisbursal_Checklist_Frame4',false);
				if(desc !='Approve and Takeover' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1)
				{	
					setNGValue('cmplx_Decision_Decision','');
					showAlert('cmplx_Decision_Decision','Please select approve to takeover decision as application type is Take Over');
					
					return false;
				}
				else if((desc =='Approve to LMS Push' || desc=='SendBack' || desc=='Refer' ||desc=='Approve and Takeover')&& (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')==-1) && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,0)=='Conventional')
				{	
					setNGValue('cmplx_Decision_Decision','');
					showAlert('cmplx_Decision_Decision','Please select approve to Post disbursal or approve to exit decision');
					return false;
			}
			else if(( desc=='SendBack' || desc=='Refer' ||desc=='Approve and Takeover')&& getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')==-1 && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,0)=='Islamic')
				{	
					setNGValue('cmplx_Decision_Decision','');
					showAlert('cmplx_Decision_Decision','Please select approve to Post disbursal or approve to exit decision');
					return false;
			}
			}
		}//above code by jahnavi for post disbursal CR
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
		else if (getNGValue('cmplx_BussVerification_Decision') == 'Negative' && (getNGValue(pId)=='Approve' || getNGValue(pId)=='Approve Sub to CIF'))
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
		else if(getNGValue(pId)=='Reject' && (getNGValue('cmplx_HCountryVerification_Decision')=='Positive' || getNGValue('cmplx_HCountryVerification_Decision')==null) && (getNGValue('cmplx_ResiVerification_Decision')=='Positive' || getNGValue('cmplx_ResiVerification_Decision')==null) &&  (getNGValue('cmplx_CustDetailVerification_Decision')=='Positive' || getNGValue('cmplx_CustDetailVerification_Decision')==null) && (getNGValue('cmplx_BussVerification_Decision')=='Positive' || getNGValue('cmplx_BussVerification_Decision')==null)
		&& (getNGValue('cmplx_RefDetVerification_Decision')=='Positive' || getNGValue('cmplx_RefDetVerification_Decision')==null) && (getNGValue('cmplx_OffVerification_Decision')=='Positive' || getNGValue('cmplx_OffVerification_Decision')==null) && (getNGValue('cmplx_LoanandCard_Decision')=='Positive' || getNGValue('cmplx_LoanandCard_Decision')==null))
		{
			showAlert(pId, 'Decision cannot be '+getNGValue(pId)+' as all verification is Positive');
			setNGValueCustom(pId,'--Select--');
			return false;
		}
		//change by saurabh for task list points on 11/4/19.
		if(!isLocked('cmplx_Decision_SetReminder')){
			setLockedCustom('cmplx_Decision_SetReminder',true);
		}
		setNGValueCustom('cmplx_Decision_SetReminder',''); 
		}
		//change by saurabh for task list points on 11/4/19.
		else if(getNGValue(pId)=='CPV Hold'){
			setLockedCustom('cmplx_Decision_SetReminder',false);
			setNGValueCustom('cmplx_Decision_SetReminder',"");
		}
		
	}
	//added by nikhil as per CC FSD
	else if (activityName=='CPV_Analyst'){
				//--commented by akshay on 4/1/17	
			setVisible("DecisionHistory_Button2",false);
			setVisible("DecisionHistory_Label6",false);
			//setVisible("DecisionHistory_dec_reason_code",true);
			//setVisible('DecisionHistory_Label11',true);	
		
	}
	else if (activityName=='Smart_CPV'){
		setVisible("DecisionHistory_Button2",false);
		//setVisible("DecisionHistory_dec_reason_code",true);
		//setVisible('DecisionHistory_Label11',true);
		if(getNGValue(pId)=='Smart CPV Hold') {
			setLocked('cmplx_Decision_SetReminder_Smart',false);
			setLocked('cmplx_Decision_NoOfAttempts_Smart',true);
		}
		else{
			if(getNGValue(pId)=='Smart CPV Hold') {
				setLocked('cmplx_Decision_SetReminder_Smart',true);
				setLocked('cmplx_Decision_NoOfAttempts_Smart',true);
			}
		}
	}
		//added by shweta for cpv analyst1 for PCSP-319
	else if (activityName=='CAD_Analyst1'){
			setNGValue("CAD_dec",desc);
			setEnabledCustom("cmplx_Decision_Manual_Deviation", true);
			//below code added by shweta for PCV changes 17-04
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
				setLocked('cmplx_Decision_SetReminder_CA',true);
				setLocked('cmplx_Decision_NoOfAttempts_CA',true);
			}
			if(getNGValue(pId).toUpperCase()=='REJECT')
			{	//below code added by Shweta for PCSP-319
				 if(parseInt(getNGValue('reEligibility_PL_counter').split(';')[0])==0)
				 {
					showAlert(pId,'Please calculate Re-eligibility from EligibilityAndProductInformation');
					setNGValueCustom(pId,'--Select--');
					return false;				
				 }
				 //below code added by Shweta for PCSP-671
				 else if(getNGValue('cmplx_Decision_Manual_Deviation')==true && parseInt(getNGValue('reEligibility_PL_counter').split(';')[1])==0){
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
				 setLocked('cmplx_Decision_SetReminder_CA',true);
					setLocked('cmplx_Decision_NoOfAttempts_CA',true);
	        
				//setEnabledCustom("cmplx_DEC_Manual_Deviation", false);
				//setNGValueCustom('cmplx_DEC_Manual_Deviation',false);//commmented by aman for PCSP-391
				setLockedCustom('cmplx_Decision_Manual_deviation_reason',false);// Changed by aman for PCSP-331
				setLockedCustom('cmplx_Decision_CADDecisiontray',false);

				//enable cad- dec tray
		
							
			}
			else if(getNGValue(pId).toUpperCase()=='REFER')
			{
				setLocked('cmplx_Decision_CADDecisiontray',true); // hritik 29.6.21 PCASI 3526
			//below code added by nikhil for PCSP-319
				 if(parseInt(getNGValue('reEligibility_PL_counter').split(';')[0])==0)
				 {
					showAlert(pId,'Please calculate Re-eligibility from EligibilityAndProductInformation');
					setNGValueCustom(pId,'--Select--');
					return false;				
				 }
				 //below code added by nikhil for PCSP-671
				 else if(getNGValue('cmplx_Decision_Manual_Deviation')==true && parseInt(getNGValue('reEligibility_PL_counter').split(';')[1])==0){
					showAlert(pId,'Please calculate Re-eligibility first');
					setNGValueCustom(pId,'--Select--');
					return false;		
					}

			//enable cad- dec tray
				//changes doen for PCSP-690 30/1
				if(getNGValue('cmplx_Decision_ReferTo')=='Smart CPV' || getNGValue('cmplx_Decision_ReferTo')=='Mail Approval')
				{
				setLockedCustom('cmplx_Decision_CADDecisiontray',true);
				}
				else
				{
				setLockedCustom('cmplx_Decision_CADDecisiontray',true); // hritik 29.6.21 PCASI 3526
				}
				setNGValueCustom('cmplx_Decision_CADDecisiontray','');
				setNGValueCustom('cmplx_Decision_ReferTo','');
				setLocked('cmplx_Decision_SetReminder_CA',true);
				setLocked('cmplx_Decision_NoOfAttempts_CA',true);
				
			}
			 else if(getNGValue(pId)=='CA_HOLD')
				{
					setLocked('cmplx_Decision_SetReminder_CA',false);
					setLocked('cmplx_Decision_NoOfAttempts_CA',true);
					
				}
			 else if(getNGValue(pId)=='Refer to Credit') {
					setEnabledCustom("cmplx_Decision_Manual_Deviation", true);
					//setEnabledCustom('cmplx_DEC_ReferTo',true);			
				}
			 else{
					//disable cad- dec tray
						setLockedCustom('cmplx_Decision_CADDecisiontray',true);
				//change by saurabh on 30th Nov for FSD 2.7
				//change made by saurabh on 14th Dec
						if(getNGValue(pId)=='APPROVE' || getNGValue(pId)=='Approve'){
							if(getNGValue('Is_Financial_Summary')!='Y' && getNGValue('cmplx_Customer_NTB')==false){
							showAlert('IncomeDetails','Please fetch Financial Summary first');
							setNGValueCustom(pId,'--Select--');
							return false;
							}
							//below code added by nikhil for PCSP-504 Sprint-2
							//deepak activityName condition added by for PCAS-2125
							else if(getNGValue('ELigibiltyAndProductInfo_EFMS_Status')!='Y' && activityName=='CAD_Analyst1')
							{
								showAlert(pId,alerts_String_Map['CC281']);
								return false;	
							}
							else if(parseInt(getNGValue('reEligibility_PL_counter').split(';')[0])==0){
								showAlert(pId,'Please calculate Re-eligibility from EligibilityAndProductInformation');
								setNGValueCustom(pId,'--Select--');
								return false;				
							}
							else if(getNGValue('cmplx_Decision_Manual_Deviation')==true && parseInt(getNGValue('reEligibility_PL_counter').split(';')[1])==0){
								showAlert(pId,'Please calculate Re-eligibility first');
								setNGValueCustom(pId,'--Select--');
								return false;		
							}
							else if(getNGValue('cmplx_EmploymentDetails_Field_visit_done')=='A'){
								showAlert(pId,'Cannot take Decision Approved as Field Visit Done is Awaited');
								setNGValueCustom(pId,'--Select--');	
								return false;
							}
							else if(getNGValue('cmplx_EmploymentDetails_EmpStatusCC')=='FVR'){
								showAlert(pId,'Cannot take Decision Approved as Employer Status CC is Awaiting FVR');
								setNGValueCustom(pId,'--Select--');	
								return false;
							}
							//change by saurabh on 7th DEC -  PCASI-3202 Hritik 10/6/21 
//							if(getNGValue('Is_CAM_generated')!='Y'){
//								showAlert(pId,'Cannot take Decision Approved as CAM report not generated');
//								setNGValueCustom(pId,'--Select--');
//								return false;
//							}
							setLocked('cmplx_Decision_SetReminder_CA',true);
							setLocked('cmplx_Decision_NoOfAttempts_CA',true);
							
							setNGValueCustom('cmplx_Decision_CADDecisiontray','');
						}
								//--commented by akshay on 4/1/17
						//setNGValueCustom('DecisionHistory_dec_reason_code',"--Select--");
							//setEnabledCustom('DecisionHistory_dec_reason_code',false);
							//setVisible('DecisionHistory_dec_reason_code',false);
							  //setVisible('DecisionHistory_Label11',false);
							//setEnabledCustom('cmplx_DEC_ReferTo',false);
					//	setEnabledCustom("cmplx_DEC_Manual_Deviation", false);
					//	setNGValueCustom('cmplx_DEC_Manual_Deviation',false);
				setLockedCustom('cmplx_Decision_Manual_deviation_reason',true);
							
			}
		}//changed done by shweta for PCSP-319 end
			//below code added by nikhil for PCSP-504 Sprint-2
	else if (activityName=='CAD_Analyst2')
	{
	//below code added by nikhil for CPV changes 21-04-2019
	if(!CheckCADecision(getNGValue(pId)))
	{
		return false;
	}
	if(getNGValue(pId)=='CA_HOLD')
	{
	setLocked('cmplx_Decision_SetReminder_CA',false);
			setLocked('cmplx_Decision_NoOfAttempts_CA',true);
	}	
	/*if(getNGValue(pId)=='APPROVE' || getNGValue(pId)=='Approve')
	{
	if(getNGValue('Is_CAM_generated')!='Y')
	{
		showAlert(pId,alerts_String_Map['CC282']);
		setNGValueCustom(pId,'--Select--');
		return false;
	}*/
	}
	//below code added by yash on 15/12/2017 fro toteam
	else if(activityName=="ToTeam") {
		if(getNGValue(pId)=='Await pending documents')
		{
			setEnabled("DecisionHistory_DecisionReasonCode",true);
			setEnabled("DecisionHistory_DecisionSubReason",true);

		}		
	}
	
	//above code added by yash on 15/12/2017
	else if(activityName=='Disbursal_Checker'){
		
		if( desc=='Approved with deferred doc'){
			if(getNGValue('DeferredDocsList')=='' || getNGValue('DeferredDocsList')==null){
				showAlert(pId,'Cannot take Decision as Approved with Deferred doc as no documents are deferred');
				setNGValueCustom(pId,'--Select--');	
				return false;
			}
			if(getNGValue('Application_Type')!='' && getNGValue('Application_Type').indexOf('TKO')==-1){
				showAlert(pId,'Cannot take Decision as Approved with Deferred doc as this is not a Takeover case');
				setNGValueCustom(pId,'--Select--');	
				return false;
			}
		}
		else{
			if(((getNGValue(pId)=='Approve and Post Disbursal'||getNGValue(pId)=='Approve and Exit')&& getLVWAT("cmplx_Product_cmplx_ProductGrid",0,0)=='Islamic')||(getNGValue(pId)=='Approve and Takeover' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1)) { 
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
					setNGValueCustom(pId,'--Select--');	
					return false;
				}
				if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1 ) {
					setLocked("cmplx_Decision_DecDisbChecker",false);
				}
				
			} else {
				setLocked("cmplx_Decision_DecDisbChecker",true);
				setNGValueCustom('cmplx_Decision_DecDisbChecker','');
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
				if(!pass && getNGValue('cmplx_Decision_Decision')!='Reject'){
					showAlert(lvname, 'Cards Disbursal not completed');
					setNGValueCustom(pId,'--Select--');
					return false;
				}
			}
	}
	else if (activityName=='Original_Validation')
	{
		if(getNGValue('cmplx_Decision_Decision')=='Approve')
		{
		if(getLVWAT("cmplx_LoanDetails_cmplx_LoanGrid",0,2)!=getLVWAT("cmplx_LoanDetails_cmplx_LoanGrid",0,3))
					{
						showAlert("cmplx_LoanDetails_cmplx_LoanGrid","Payment release date should be current date/disbursal date");
						console.log("Test");
						//setNGValue("LoanDetails_payreldate",getNGValue("LoanDetails_disbdate"));
						return false;
					}
		}
	}
	else if( activityName=='DDVT_Checker' && getNGValue('cmplx_Decision_Decision')=='Refer')	// By Alok for Decision
		{
			setNGValue("ProcessBy", "ProcessedBy");
		}	
	else if(activityName=='DDVT_maker')
	{	 //by shweta
		if(getNGValue('cmplx_Customer_NTB')==true && !checkMandatory(PL_RISKRATING)){
			if(riskScoreCalFlag!=1 && getNGValue("cmplx_RiskRating_Total_riskScore")!=''){
				showAlert('cmplx_RiskRating_Total_riskScore',alerts_String_Map['PL425']);
				return false;
			}
		}
			//setLocked('DecisionHistory_DecisionReasonCode', false);
			setNGValueCustom("decision", desc);
		
	}	
	else if( activityName=='DDVT_checker' && pId == 'Approve' && getNGValue('RefFrmDDVT')=='Y')	//PCASI - 3422 bu jahnavi
		{
			setNGValue("cmplx_Decision_Decision",'');
			showAlert('cmplx_Decision_Decision',"WI was refered from ddvt to source , so only refer decision is allowed to checker");
			return false;
		}
	
	else{	
		setNGValueCustom("decision", desc);
		setLocked('cmplx_Decision_SetReminder_CA',true);
		setLocked('cmplx_Decision_NoOfAttempts_CA',true);
	}
	
	return true;//uncommented by akshay on 4/12/17

	}
	else if (pId=='DecisionHistory_DecisionReasonCode'){
		if(activityName=='ToTeam'){
			return true;
		}
	}
	else if(pId=='cmplx_Decision_ReferTo')
	{
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
		if(getNGValue('cmplx_Decision_Decision')=='Refer' && (getNGValue('cmplx_Decision_ReferTo')=='Smart CPV' || getNGValue('cmplx_Decision_ReferTo')=='Mail Approval'))
		{
		//Below code added by nikhil for PCSP-754
		setNGValueCustom('cmplx_Decision_CADDecisiontray','--Select--');
		setLockedCustom('cmplx_Decision_CADDecisiontray',true);		
		}
		else
		{
		setLockedCustom('cmplx_Decision_CADDecisiontray',true); // hritik 29.6.21 PCASI 3526
		}
		return true;
	}
	
	//ended by akshay on 9/12/17 for multiple refer
		//below code added by nikhil for employment_detail match
	else if(pId=='cmplx_Customer_MobNo'){
		setNGValueCustom('OTP_Mobile_NO',mob_No);
		}//PCAS-2539 by sagarika
	else if(pId=='cmplx_OffVerification_Decision')
	{
		if(getNGValue('cmplx_OffVerification_Decision')=='Negative')
		setNGValueCustom('employ_detail_match','No');
		else
		setNGValueCustom('employ_detail_match','Yes');
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
	}
	
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
				setTop('Supplementary_Card_Details',parseInt(getTop('Card_Details'))+parseInt(getHeight('Card_Details'))+10+'px');
				setTop('FATCA',parseInt(getTop('Supplementary_Card_Details'))+parseInt(getHeight('Supplementary_Card_Details'))+10+'px');
				setTop('KYC',parseInt(getTop('FATCA'))+parseInt(getHeight('FATCA'))+10+'px');
				setTop('OECD',parseInt(getTop('KYC'))+parseInt(getHeight('KYC'))+10+'px');
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
				setTop('FATCA',parseInt(getTop('Card_Details'))+parseInt(getHeight('Card_Details'))+10+'px');
				setTop('KYC',parseInt(getTop('FATCA'))+parseInt(getHeight('FATCA'))+10+'px')
				setTop('OECD',parseInt(getTop('KYC'))+parseInt(getHeight('KYC'))+10+'px')
			}
	}
	
	else if(pId=='cmplx_LoanDetails_lpfamt')
	{
		setNGValue('cmplx_LoanDetails_amt',parseFloat(getNGValue(pId)) + parseFloat(getNGValue('cmplx_LoanDetails_insuramt'))+parseFloat(getNGValue('cmplx_LoanDetails_LoanProcessingVat'))+parseFloat(getNGValue('cmplx_LoanDetails_InsuranceVat')));
	}
	
	 //added by shweta PCSP-101
	//Unreachable code 
	if(pId=='cmplx_CustDetailVerification_mobno1_ver')
	{
		var mob1 = getNGValue(pId);
		if(mob1=='Mismatch')	{
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

	else if(pId=='cmplx_CustDetailVerification_mobno2_ver')
	{
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
	//pcasi-1003
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
				setNGValueCustom("cmplx_CustDetailVerification_email2_ver","NA");
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
	
	else if(pId=='LoanDetails_accno')
	{
		if(getNGValue(pId)!='' && getNGValue('cmplx_Customer_NTB')==false){
			setNGValue('LoanDetails_valstatus','Active');
		}
		else{
		setNGValue('LoanDetails_valstatus','Inactive');
		}
	} //changed bu jahnavi to change to default active value of account
	
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
	
	//below code added by siva on 01112019 for PCAS-1401
	if(pId=='AlternateContactDetails_AirArabiaIdentifier')
	{
		validateMail1(pId);
		var arabia=getNGValue('AlternateContactDetails_AirArabiaIdentifier');
		 if(!(arabia.length>15 && arabia.length<30))
		 {
			showAlert(pId,alerts_String_Map['PL435']); 
			setNGValue(pId,'');
			return false;
		 }			
	}
	//code ended by siva on 01112019 for PCAS-1401
	
	//if(pId == 'cmplx_Customer_RM_TL_NAME' && (activityName == 'DDVT_maker' || activityName == 'Disbursal_Maker')){
	//var rmtlname = getNGValue('cmplx_Customer_RM_TL_NAME');
	//	if(rmtlname != ''){
	//	setNGValue('RM_Name',rmtlname);
	//	setNGValue('RmTlNameLabel',rmtlname);
	//	}
	//}
	
	
 return false;
 
 }

//smartchck function by shweta  for smartcheck mandatory fields.
function checkMandatory_Smartcheck(){
var activityName=window.parent.stractivityName;
	if(!getNGValue('SmartCheck_creditrem') && activityName.indexOf('CAD')>-1){
		showAlert('SmartCheck_creditrem',alerts_String_Map['CC054']);
			return false;
	}
	else if(!getNGValue('SmartCheck_CPVrem') && activityName=='CPV'){
		showAlert('SmartCheck_CPVrem',alerts_String_Map['CC053']);
			return false;
	}
	return true;
}
 

  
  function blur_PL(pId)
 {
	if(pId=='cmplx_Customer_MobNo' ||   pId=='PartMatch_mno1' || pId=='PartMatch_mno2')
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
	// added by hritik PCASI-2864 
	else if(pId=='cmplx_emp_ver_sp2_landline')
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
	}}//by jahnavi to set margin rate decimal to 2
	if(pId=='cmplx_LoanDetails_marginrate')
	{
	//PCASI - 3515
		var marginRate = getNGValue('cmplx_LoanDetails_marginrate');
		
		var substr = marginRate.substr(marginRate.indexOf(".")+1,marginRate.length);
		//console.log(substr);
		if(substr.length > 3){
			//console.log("Not valid");
			showAlert(pId,'Please enter Margin Rate upto 3 decimal places');
		}
		setNGValue('cmplx_LoanDetails_marginrate',marginRate.substr(0,marginRate.indexOf(".")+4));
	//setNGValue('cmplx_LoanDetails_marginrate',(parseFloat(getNGValue('cmplx_LoanDetails_marginrate'))).toFixed(3));
	}
	//by jahnavi for 3197
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
		}
	}
	else if(pId=='cmplx_Customer_guarcif'){
		var cif=getNGValue('cmplx_Customer_guarcif');
		var Primcif=getNGValue('cmplx_Customer_CIFNO');

		if(cif !='' && cif.length!=7)
		{
		setNGValue('cmplx_Customer_guarcif','');
			showAlert('cmplx_Customer_guarcif','Please Enter valid guardian CIF ID');
			return false;
		}
		if(cif!='' && cif==Primcif)
		{
		setNGValue('cmplx_Customer_guarcif','');
			showAlert('cmplx_Customer_guarcif','Guardian and Primary CIF can not be same');
			return false;
		}

	}
	else if(pId=='cmplx_OffVerification_hrdcntctno')
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
		if(pId=='cmplx_Customer_MobNo'){
		setNGValueCustom('OTP_Mobile_NO',mob_No);
		}
		//setLockedCustom('Customer_FetchDetails',false);
	}
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
/*	else if(pId=='cmplx_emp_ver_sp2_landline')
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
*/	
	
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
	else if(pId=='cmplx_cust_ver_sp2_Mobile')
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
	
	else if(pId=='AlternateContactDetails_MobileNo1')
	{
	    var Mobile_no=getNGValue('AlternateContactDetails_MobileNo1');
		//added for special character check - Vaishvi - PCASI - 3593, 3594
		if(Mobile_no != null || Mobile_no != ''){
		specialCharCheck(Mobile_no,"AlternateContactDetails_MobileNo1","Special Characters not allowed in Mobile No- Primary field");
		return false;
		}
		
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=''){
			Mobile_no="00971"+Mobile_no;
			setNGValueCustom('AlternateContactDetails_MobileNo1',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL087']);
			setNGValueCustom('AlternateContactDetails_MobileNo1','');
			setNGValueCustom('AlternateContactDetails_MobileNo2',''); //added By Tarang started on 13/02/2018 as per drop 4 point 16
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL088']);
			setNGValueCustom('AlternateContactDetails_MobileNo1','');
			setNGValueCustom('AlternateContactDetails_MobileNo2',''); //added By Tarang started on 13/02/2018 as per drop 4 point 16
			return false;
		}
	}
	
	else if(pId=='AlternateContactDetails_MobileNo2')
	{
	    var Mobile_no=getNGValue('AlternateContactDetails_MobileNo2');
		//added for special character check - Vaishvi - PCASI - 3593, 3594
		if(Mobile_no != null || Mobile_no != ''){
		specialCharCheck(Mobile_no,"AlternateContactDetails_MobileNo2","Special Characters not allowed in Mobile No- Secondary field");
		return false;
		}
		
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=''){
			Mobile_no="00971"+Mobile_no;
			setNGValueCustom('AlternateContactDetails_MobileNo2',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo2',alerts_String_Map['VAL087']);
			setNGValueCustom('AlternateContactDetails_MobileNo2','');
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=''){
			showAlert('AlternateContactDetails_MobileNo2',alerts_String_Map['VAL088']);
			setNGValueCustom('AlternateContactDetails_MobileNo2','');
			return false;
		}
	}
	
	//added for special character check - Vaishvi - PCASI - 3593, 3594 - start
	
	else if(pId == 'AlternateContactDetails_HOMECOUNTRYNO'){
	var homeCountryNo = getNGValue('AlternateContactDetails_HOMECOUNTRYNO');
	if(homeCountryNo != null || homeCountryNo != ''){
		specialCharCheck(homeCountryNo,"AlternateContactDetails_HOMECOUNTRYNO","Special Characters not allowed in Home Country Number field");
		return false;
		}
	}
	else if(pId == 'AlternateContactDetails_OFFICENO'){
	var officeNo = getNGValue('AlternateContactDetails_OFFICENO');
	if(officeNo != null || officeNo != ''){
		specialCharCheck(officeNo,"AlternateContactDetails_OFFICENO","Special Characters not allowed in Office Number field");
		return false;
		}
	}
	else if(pId == 'AlternateContactDetails_OfficeExt'){
	var officeExt = getNGValue('AlternateContactDetails_OfficeExt');
	if(officeExt != null || officeExt != ''){
		specialCharCheck(officeExt,"AlternateContactDetails_OfficeExt","Special Characters not allowed in Office Extension field");
	return false;
	}
	}
	else if(pId == 'AlternateContactDetails_RESIDENCENO'){
	var residencyNo = getNGValue('AlternateContactDetails_RESIDENCENO');
	if(residencyNo != null || residencyNo != ''){
		specialCharCheck(residencyNo,"AlternateContactDetails_RESIDENCENO","Special Characters not allowed in Residency Number field");
	return false;
	}
	}
	else if(pId == 'AddressDetails_house'){
	var houseDetails = getNGValue('AddressDetails_house');
	if(houseDetails != '' || houseDetails != null){
	specialCharCheckForAddress(houseDetails,'AddressDetails_house','Special Characters not allowed in Flat/Villa/House No field');
	return false;
	}
	}
	else if(pId == 'AddressDetails_buildname'){
	var buildingName = getNGValue('AddressDetails_buildname');
	if(buildingName != '' || buildingName != null){
	specialCharCheckForAddress(buildingName,'AddressDetails_buildname','Special Characters not allowed in Building Name field');
	return false;
	}
	}
	else if(pId == 'AddressDetails_street'){
	var street = getNGValue('AddressDetails_street');
	if(street != '' || street != null){
	specialCharCheckForAddress(street,'AddressDetails_street','Special Characters not allowed in Street field');
	return false;
	}
	}
	else if(pId == 'AddressDetails_landmark'){
	var landmark = getNGValue('AddressDetails_landmark');
	if(landmark != '' || landmark != null){
	specialCharCheckForAddress(landmark,'AddressDetails_landmark','Special Characters not allowed in Landmark field');
	return false;
	}
	}
	
	//added for special character check - Vaishvi - PCASI - 3593, 3594 - end

	
  return false;	
 }
 
	
	
function PL_postHookDRefresh(controlName){//Calculate re-eligibility button name is different
	if(controlName=='ELigibiltyAndProductInfo_Button1' || controlName=='DecisionHistory_Button5'){ // modified by siva on 16102019 for PCAS-396 CR
		//document.getElementById('ELigibiltyAndProductInfo_IFrame1').src='/formviewer/resources/scripts/Eligibility_Card_Product.jsp';
		document.getElementById('ELigibiltyAndProductInfo_IFrame2').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp';
		document.getElementById('ELigibiltyAndProductInfo_IFrame3').src='/webdesktop/custom/CustomJSP/Product_Eligibility.jsp';
	}
	else if(controlName=='ExtLiability_Button1'){ //added by siva on 01112019 for PCSP-672
		document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';
		document.getElementById('ExtLiability_IFrame_external').src='/webdesktop/custom/CustomJSP/External_Liability.jsp';
		document.getElementById('ExtLiability_IFrame_pipeline').src='/webdesktop/custom/CustomJSP/Pipeline.jsp';
		
		//ended by siva on 01112019 for PCSP-672
	}else if(controlName=='EligibilityAndProductInformation' && activityName=='CSM'){
		calcAgeAtMaturity();
	}
	else if(controlName=='LoanDetails_Button5'){
		if(getNGValue("cmplx_LoanDetails_lonamt") != "")
		 {
			 setNGValue("LoanDetails_loanamt",getNGValue("cmplx_LoanDetails_lonamt"));
			 setNGValue("LoanDetails_amt", getNGValue("cmplx_LoanDetails_lonamt"));
		 }
		setNGValue("LoanDetails_duedate", getNGValue("cmplx_LoanDetails_frepdate"));
		 var fName = getNGValue("cmplx_Customer_FIrstNAme");//Arun (21/09/17)
		 var mName = getNGValue("cmplx_Customer_MiddleName");//Arun (21/09/17)
   		var lName =   getNGValue("cmplx_Customer_LAstNAme");//Arun (21/09/17)
		var fullName = fName+" "+mName+" "+lName; //Arun (21/09/17)
		setNGValue("LoanDetails_benefname", fullName);
		setNGValue("LoanDetails_modeofdisb","T");
		setNGValue("LoanDetails_disbdate",getNGValue("cmplx_LoanDetails_fdisbdate"));
		setNGValue("LoanDetails_payreldate",getNGValue("cmplx_LoanDetails_fdisbdate"));


	}
	else if(controlName=='EmploymentVerification_approved_report' || controlName=='EmploymentVerification_s2_approved_report' || controlName=='EmploymentVerification_s2_download_comp_report'){
		if(getNGValue('cmplx_emp_ver_sp2_inputxml')!="" && getNGValue('cmplx_emp_ver_sp2_docsize')!="" ){
			window.parent.customAddDoc(getNGValue('cmplx_emp_ver_sp2_inputxml'),  getNGValue('cmplx_emp_ver_sp2_docsize'), 'new');
				
			alert("File generated successfully");
		}
	
		//Commented by Vaishvi for PCASI-3120
		//formObject.setNGValue("cmplx_emp_ver_sp2_inputxml","");
		//formObject.setNGValue("cmplx_emp_ver_sp2_docsize","");
		
		//Added by Vaishvi for PCASI-3120
		setNGValue("cmplx_emp_ver_sp2_inputxml","");
		setNGValue("cmplx_emp_ver_sp2_docsize","");
	}
	else if(controlName=='Customer_FetchFirco'){
		if(getNGValue('Doc_waiver')!="" && getNGValue('Doc_deferral')!="" ){
			window.parent.customAddDoc(getNGValue('Doc_waiver'),  getNGValue('Doc_deferral'), 'new');
				
			alert("File generated successfully");
		}
		setNGValue("Doc_waiver","");
		setNGValue("Doc_deferral","");
	}
	else if(controlName=='RiskRating_CalButton')
	{
		generate_riskscore_pdf();
	}
	
	/*else if(controlName=='Generate_FPU_Report' ){
		
	
		var docindex= (getNGValue("cmplx_emp_ver_sp2_docsize")).split(',');
		var inputxml=getNGValue('cmplx_emp_ver_sp2_inputxml').split('$');
		if(getNGValue('cmplx_emp_ver_sp2_docsize')!="" ){
			window.parent.customAddDoc(inputxml[0], docindex[0] , 'new');
			window.parent.customAddDoc(inputxml[1],  docindex[1], 'new');
			
			window.parent.customAddDoc(inputxml[2],  docindex[2], 'new');
			

		}
		/*if((getNGValue('cmplx_emp_ver_sp2_inputxml_reject')).includes("REJECTED_LOAN_PL_") && getNGValue('cmplx_emp_ver_sp2_docsize')!="" ){
			window.parent.customAddDoc(getNGValue('cmplx_emp_ver_sp2_inputxml_reject'),  getNGValue('cmplx_emp_ver_sp2_docsize'), 'new');
				
			
		}
		if((getNGValue('cmplx_emp_ver_sp2_inputxml')).includes("LOAN_APPROVED_PL") && getNGValue('cmplx_emp_ver_sp2_docsize')!="" ){
			window.parent.customAddDoc(getNGValue('cmplx_emp_ver_sp2_inputxml'),  getNGValue('cmplx_emp_ver_sp2_docsize'), 'new');
		}	
		if((getNGValue('cmplx_emp_ver_sp2_inputxml_comp')).includes("COMPANY_MISMATCH_PL") && getNGValue('cmplx_emp_ver_sp2_docsize')!="" ){
			window.parent.customAddDoc(getNGValue('cmplx_emp_ver_sp2_inputxml_comp'),  getNGValue('cmplx_emp_ver_sp2_docsize'), 'new');
		}
		
		alert("File generated successfully");
		
		
		//Commented by Vaishvi for PCASI-3120
		//formObject.setNGValue("cmplx_emp_ver_sp2_inputxml","");
		//formObject.setNGValue("cmplx_emp_ver_sp2_docsize","");
		
		//Added by Vaishvi for PCASI-3120
		setNGValue("cmplx_emp_ver_sp2_inputxml","");
		setNGValue("cmplx_emp_ver_sp2_docsize","");
	}*/
	
	
	else if (controlName=='SupplementCardDetails_Add' || controlName=='SupplementCardDetails_Modify')
	{
		setNGValue("SupplementCardDetails_ResidentCountry","AE");
	}
	else if(controlName=='cmplx_LoanDetails_cmplx_LoanGrid'){
		setNGValue("LoanDetails_duedate", getNGValue("cmplx_LoanDetails_frepdate"));	
	}
	else if(controlName=='OfficeandMobileVerification_save')
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
}
	
//New method added to handle keypress event Deepak 18 Dec 2017
 function keydown_PL(pId,keyCode_str)
 {
	//pID name need to added which are mentioned as 17,2 in field list document.

	if(pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other' || pId=='cmplx_IncomeDetails_Commission_Month1' || pId=='cmplx_IncomeDetails_Commission_Month2' || pId=='cmplx_IncomeDetails_Commission_Month3' || pId=='cmplx_IncomeDetails_Flying_Month1' || pId=='cmplx_IncomeDetails_Flying_Month2' || pId=='cmplx_IncomeDetails_Flying_Month3' || pId=='cmplx_IncomeDetails_Bonus_Month1' || pId=='cmplx_IncomeDetails_Bonus_Month2' || pId=='cmplx_IncomeDetails_Bonus_Month3' || pId=='cmplx_IncomeDetails_Overtime_Month1' || pId=='cmplx_IncomeDetails_Overtime_Month2' || pId=='cmplx_IncomeDetails_Overtime_Month3' || pId=='cmplx_IncomeDetails_FoodAllow_Month1'|| pId=='cmplx_IncomeDetails_FoodAllow_Month2' || pId=='cmplx_IncomeDetails_FoodAllow_Month3' || pId=='cmplx_IncomeDetails_PhoneAllow_Month1'|| pId=='cmplx_IncomeDetails_PhoneAllow_Month2' || pId=='cmplx_IncomeDetails_PhoneAllow_Month3' || pId=='cmplx_IncomeDetails_serviceAllow_Month1' || pId=='cmplx_IncomeDetails_serviceAllow_Month2' || pId=='cmplx_IncomeDetails_serviceAllow_Month3' || pId=='cmplx_IncomeDetails_Totavgother' || pId=='cmplx_RiskRating_Total_riskScore' || pId=='FinacleCore_Text11'|| pId=='cmplx_MOL_basic' || pId=='cmplx_MOL_resall' || pId=='cmplx_MOL_transall' || pId=='cmplx_MOL_gross' || pId=='cmplx_MOL_molsalvar' || pId=='cmplx_Decision_TotalOutstanding' || pId=='cmplx_Decision_TotalEMI' || pId=='cmplx_LoanandCard_cardlimit_val' || pId=='cmplx_LoanandCard_cardlimit_ver' || pId=='cmplx_LoanandCard_cardlimit_upd'|| pId=='cmplx_EmploymentVerification_Salary_variance' || pId=='EmploymentVerification_Text5' || pId=='cmplx_EmploymentVerification_fixedsal_val' || pId=='cmplx_EmploymentVerification_fixedsal_ver' || pId=='cmplx_EmploymentVerification_fixedsalupd' || pId=='cmplx_BankingCheck_SalaryCreditUpdate' || pId=='cmplx_MOL_transall' || pId=='cmplx_MOL_resall' || pId=='cmplx_IncomeDetails_Pension'){
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
 
  function PL_click_getParameter(pId)
 {
	var args = '';
	switch(pId)
	{
		case 'Product_Modify':args='ProductContainer';break;
		case 'Modify':	args = 'Product_Frame1';	break;
		case 'Delete':	args = 'Product_Frame1';	break;
		case 'IncomeDetails_Add':	args = 'IncomeDetails_Frame1';	break;
		case 'IncomeDetails_Modify':	args = 'IncomeDetails_Frame1';	break;
		case 'IncomeDetails_Delete':	args = 'IncomeDetails_Frame1';	break;
		case 'IncomeDetails_Salaried_Save':	args = 'IncomeDetails';	break;
		case 'IncomeDetails_SelfEmployed_Save':	args = 'IncomeDetails';	break;
		case 'CompanyDetails_Add':	args = 'CompanyDetails_Frame1';	break;
		case 'CompanyDetails_Modify':	args = 'CompanyDetails_Frame1';	break;
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
		case 'ExtLiability_Add':	args = 'ExtLiability_Frame4';	break;
		case 'ExtLiability_Modify':	args = 'ExtLiability_Frame4';	break;
		case 'ExtLiability_Delete':	args = 'ExtLiability_Frame4';	break;//PCASI-994
		case 'ExtLiability_Save':	args = 'ExtLiability_Frame1';	break;
		case 'ELigibiltyAndProductInfo_Button1':	args = 'ELigibiltyAndProductInfo_Frame1 DecisionHistory IncomeDetails_Frame1 FrameName';	break;//changed by nikhil for PCSP-328 and also for PCSP-489 updated 12th jan
		case 'DecisionHistory_Button5': args= 'DecisionHistory_Frame1 cmplx_Decision_Manual_Deviation NotepadDetails_Frame1'; break;//changed by nikhil for PCSP-483
		case 'ELigibiltyAndProductInfo_Save':	args = 'ELigibiltyAndProductInfo_Frame1';	break;
		case 'ELigibiltyAndProductInfo_Refresh':	args = 'ELigibiltyAndProductInfo_Frame1';	break;
		case 'Nationality_Button':	args = 'cmplx_Customer_Nationality';	break;
		case 'SecNationality_Button':	args = 'cmplx_Customer_SecNationality';	break;
		case 'Third_Nationality_Button':	args = 'cmplx_Customer_Third_Nationaity';	break;
		case 'Customer_FetchDetails':	args = 'CustomerDetails';	break;
		case 'Customer_Button1':	args = 'CustomerDetails';	break;
		case 'Customer_Button2':	args = 'cmplx_Customer_DSA_Code';	break;//By Alok
		case 'Sourcing_Branch_Code_button':	args = 'cmplx_Customer_Sourcing_Branch_Code';	break;//By Alok on 31/10/21
		
		case 'Customer_save':	args = 'CustomerDetails';	break;
		case 'Product_Save':	args = 'ProductContainer';	break;
		//below code added by nikhil to correct add to average functionality 18/10
		case 'FinacleCore_AddToAverage': args = 'FinacleCore_avgbal'; break;
		case 'FinacleCore_AddToTurnover': args = 'FinacleCore_Frame8'; break;
		case 'FinacleCore_Button9': args = 'FinacleCore_Frame3'; break;
		case 'Designation_button':	args = 'cmplx_EmploymentDetails_Designation';	break;
		case 'Designation_button7': args='cmplx_Customer_Designation';break;
		case 'Designation_button8_view':args='cmplx_Customer_Designation';break;
		case 'EMploymentDetails_DesignationAsPerVisa_button':	args = 'cmplx_EmploymentDetails_DesigVisa';	break;
		case 'EMploymentDetails_FreeZone_Button':	args = 'cmplx_EmploymentDetails_FreezoneName';	break;
		case 'EMploymentDetails_Save':	args = 'EmploymentDetails';	break;
		case 'Button_City':	args = 'AddressDetails_city';	break;
		case 'Button_State':	args = 'AddressDetails_state';	break;
		case 'AddressDetails_Button1':	args = 'AddressDetails_country';	break;
		case 'AddressDetails_addr_Add': args='Address_Details_container'; break;	
		case 'AddressDetails_addr_Modify': args='Address_Details_container'; break;
		case 'AddressDetails_addr_Delete': args='Address_Details_container'; break;//cmplx_EmploymentDetails_remarks
		case 'AddressDetails_Save': args='Address_Details_container'; break;
		case 'AltContactDetails_ContactDetails_Save': args='Alt_Contact_container'; break;
		case 'AltContactDetails_ContactDetails_Save': args = 'CustomerDetails'; break; //hritik 22.7.21 
		case 'CardDetails_Button2': args='CardDetails_Frame1'; break;
		case 'CardDetails_Button3': args='CardDetails_Frame1'; break;
		case 'CardDetails_Button4': args='CardDetails_Frame1'; break;
		case 'CardDetails_Button1': args='CardDetails_Frame1 AlternateContactDetails_AirArabiaIdentifier'; break;
		case 'CardDetails_Button5': args='CardDetails_Frame1'; break;
		case 'CardDetails_modify': args='CardDetails_Frame1 AltContactDetails_Frame1'; break;
		case 'CardDetails_delete': args='CardDetails_Frame1 AltContactDetails_Frame1'; break;
		case 'CardDetails_Save': args='CardDetails_Frame1 AltContactDetails_Frame1'; break;
		case 'SupplementCardDetails_Button1': args='Supplementary_Card_Details'; break;
		case 'SupplementCardDetails_Button2': args='Supplementary_Card_Details'; break;
		case 'SupplementCardDetails_Button3': args='Supplementary_Card_Details'; break;
		case 'SupplementCardDetails_Button4': args='Supplementary_Card_Details'; break;
		case 'SupplementCardDetails_Save': args='Supplementary_Card_Details'; break;
		case 'FATCA_Button1': args='cmplx_FATCA_ListedReason cmplx_FATCA_SelectedReason'; break;//IDs corrected by shweta
		case 'FATCA_Button2': args='cmplx_FATCA_ListedReason cmplx_FATCA_SelectedReason'; break;
		case 'FATCA_Save': args='FATCA'; break;
		case 'KYC_Save': args='KYC'; break;
		case 'OECD_add': args='OECD'; break;
		case 'OECD_modify': args='OECD'; break;
		case 'OECD_delete': args='OECD'; break;
		case 'OECD_Save': args='OECD'; break;	
		case 'MOL1_Save': args = 'MOL1_Frame1'; break; //pcasi3685
		case 'ButtonOECD_State':	args = 'OECD_townBirth';	break;	
		case 'ReferenceDetails_Reference_delete': args='ReferenceDetails_Frame1'; break;//by shweta 05-052020 Wrong ids
		case 'ReferenceDetails_save': args='ReferenceDetails_Frame1'; break;
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
		case 'PartMatch_Button1': args='Part_Match'; break;
		case 'PartMatch_Save': args='Part_Match'; break;
		case 'DecisionHistory_CifLock': args='@this'; break;
		case 'DecisionHistory_CifUnlock': args='@this'; break;
		case 'NotepadDetails_Add': args='Notepad_Values'; break;
		case 'NotepadDetails_Modify': args='Notepad_Values'; break;
		case 'NotepadDetails_save': args='Notepad_Values'; break;//changed by shweta bbutton name is different in CC and PL
		case 'CustDetailVerification1_Button2' : args='CustDetailVerification1_Frame1'; break;//for fcu changes 21/10/18-Nikhil
		case 'CC_Creation_Button2' : args='CC_Creation'; break;//changes by nikhil 30/10/18 for Card Creation 
		case 'RejectEnq_Save': args='RejectEnq_Frame1';break;
		case 'WorldCheck1_Add':args='WorldCheck1_Frame1';break;
		case 'WorldCheck1_Modify':args='WorldCheck1_Frame1';break;
		case 'WorldCheck1_Delete':args='WorldCheck1_Frame1';break;
		case 'WorldCheck1_Save':args='WorldCheck1_Frame1';break;
		case 'FinacleCore_Button1': args='FinacleCore_Frame6';break;
		case 'FinacleCore_Button2': args='FinacleCore_Frame6';break;
		case 'FinacleCore_Button3': args='FinacleCore_Frame6';break;
		case 'FinacleCore_Button1': args='FinacleCore_Frame6';break;
		case 'ExternalBlackList_Button1': args='External_Blacklist';break;
		case 'FinacleCRMCustInfo_Add': args='FinacleCRM_CustInfo';break;   //changes done by shweta for jira# PCAS-1403 on 01-10-2019 to save finacle customer information fragment; fragment id is different in cc and pl start
		case 'FinacleCRMCustInfo_Modify': args='FinacleCRM_CustInfo';break;
		case 'FinacleCRMCustInfo_Delete': args='FinacleCRM_CustInfo';break;
		case 'FinacleCRMCustInfo_Button1': args='FinacleCRM_CustInfo';break; //changes done by shweta for jira# PCAS-1403 on 01-10-2019 to save finacle customer information fragment; fragment id is different in cc and pl end
		case 'FinacleCRMIncident_Button1': args='Finacle_CRM_Incidents';break;	
		case 'FinacleCore_Button4': args='FinacleCore_Frame5';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'FinacleCore_Button5': args='FinacleCore_Frame5';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'SmartCheck_Add': args='SmartCheck_Frame1';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'SmartCheck_Modify': args='SmartCheck_Frame1';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'CardCollection_Button1': args='CardCollection_Frame1';break;//changed by nikhil 01/11/2018 for Grid issues
		case 'ReferHistory_Modify': args='ReferHistory_Frame1';break;
		case 'Compliance_Button2': args='Compliance_Frame1';break;
		case 'DecisionHistory_ADD': args='DecisionHistory'; break;
		case 'DecisionHistory_Button6': args='DecisionHistory'; break;
		case 'DecisionHistory_Delete': args='DecisionHistory'; break;
		case 'DecisionHistory_Modify': args='DecisionHistory'; break;
		case 'EMploymentDetails_Button2': args='EMploymentDetails_Frame2'; break;//changed by nikhil for employer Search PCSP-70
		case 'SupplementCardDetails_Modify': args='SupplementCardDetails_Frame1'; break;//changed by nikhil 15/12/2018
		case 'SupplementCardDetails_Add': args='SupplementCardDetails_Frame1'; break;//changed by nikhil 15/12/2018
		case 'SupplementCardDetails_FetchDetails': args='SupplementCardDetails_Frame1'; break;//changed by Deepak Supplymentry fetch details button
		case 'EmploymentVerification_Button1': args='EmploymentVerification_Frame1';break;//added by nikhil for PCSP-158
		case 'BankingCheck_save': args='BankingCheck_Frame1';break;//added by nikhil for PCSP-155
		case 'OfficeandMobileVerification_save' : args='OfficeandMobileVerification_Frame1';break;//added by nikhil for PCSP-224 changes done by shweta button name is different in Pl
		case 'CustDetailVerification1_Button1' : args='CustDetailVerification1_Frame1';break;//added by nikhil for PCSP-235
		case 'LoanandCard_save' : args='LoanandCard_Frame1';break;//added by shweta 
		case 'WorldCheck_fetch': args='WorldCheck';break;//added by nikhil for PCSP-387
		case 'IncomingDocNew_Addbtn': args='IncomingDocNew_Frame1';break; // change by disha for new incoming document CR 04/01/2019
		case 'IncomingDocNew_Modifybtn': args='IncomingDocNew_Frame1';break; // change by disha for new incoming document CR 04/01/2019
		case 'IncomingDocNew_Save': args='IncomingDocNew_Frame1';break; // change by disha for new incoming document CR 04/01/2019
		case 'EMploymentDetails_Designation_button_View': args='cmplx_EmploymentDetails_Designation';break;//code by nikhil for PCSP-503 //saurabh1 for desig view button
		case 'EMploymentDetails_DesignationAsPerVisa_button_View': args='cmplx_EmploymentDetails_DesigVisa';break;//code by nikhil for PCSP-503 //saurabh1 for desigvisa view button
		case 'KYC_Add': args='KYC_Frame7';break;//by shweta on 05-05-2020
		case 'KYC_Modify': args='KYC_Frame7'; break;
		case 'CustDetailVerification1_nationality_View': args='CustDetailVerification1_nationality';break;//code by nikhil for PCSP-503 //saurabh1 for desig view button
		case 'ReferenceDetails_Reference_Add': args='ReferenceDetails_Frame1';break;
		case 'ReferenceDetails_Reference__modify':args='ReferenceDetails_Frame1';break;
		case 'FATCA_Add': args='FATCA_Frame6';break;//by shweta on 05-05-2020
		case 'FATCA_Modify': args='FATCA_Frame6';break;
		case 'PartMatch_Search': args='PartMatch_Frame1';break;
		case 'PartMatch_Blacklist': args='PartMatch_Frame1';break;
		case 'NotepadDetails_Add1':  args='NotepadDetails_Frame3';break;//code changed by nikhil for PCSP-555
		case 'AltContactDetails_Saved':  args='AltContactDetails_Frame1';break;//code changed by nikhil for Card dispatch save
		case 'OriginalValidation_Save': args='Original_validation';break;//code for OV save
		case 'EmploymentVerification_s2_Button1': args='EmploymentVerification_s2_Frame2'; break;
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
	   case 'Third_Nationality_Button_View':args='cmplx_Customer_Third_Nationaity';break;
	   case 'Button_City_View':args='AddressDetails_city';break;
	   case 'Button_State_View':args='AddressDetails_state';break;
	   case 'AddressDetails_Button1_View':args='AddressDetails_country';break;
	   case 'CardDispatchToButton_View':args='AlternateContactDetails_carddispatch';break;
	   case 'ButtonOECD_State_View':args='OECD_townBirth';break;
	   case 'EMploymentDetails_Designation_button_View':args='cmplx_EmploymentDetails_Designation';break;//saurabh1 for desig view button
	   case 'EMploymentDetails_DesignationAsPerVisa_button_View':args='cmplx_EmploymentDetails_DesigVisa';break;//saurabh1 for desigvisa view button
	   case 'Designation_button8_View':args='cmplx_Customer_Designation';break;
	   case 'Nationality_Button1_View':args='PartMatch_nationality';break;
	   case 'LoanandCard_Button1':args='LoanandCard_Frame1';break;
	 //--above code added by nikhil for Self-Supp CR
		case 'SmartCheck1_Add' : args='SmartCheck1_Frame1';break;
		case 'SmartCheck1_Modify' : args='SmartCheck1_Frame1';break;
		case 'LoanDetaisDisburs_Save': args='LoanDetails_Frame3'; break;
		case 'LoanDetails_Button3': args='LoanDetails_Frame3'; break;
		case 'LoanDetails_Button4': args='LoanDetails_Frame3'; break;
		case 'LoanDetails_Button5': args='LoanDetails_Frame3 cmplx_LoanDetails_frepdate CustomerDetails'; break;
		case 'LoanDetails_Button6': args='LoanDetails_Frame3'; break;
		case 'IMD_Save': args='LoanDetails_Frame4'; break;
		case 'LoanDetails_Save': args='LoanDetails_Frame1'; break;

		case 'GuarantorDetails_add': args='GuarantorDetails_Frame1'; break;
		case 'GuarantorDetails_modify': args='GuarantorDetails_Frame1'; break;
		case 'GuarantorDetails_delete': args='GuarantorDetails_Frame1'; break;
		case 'GuarantorDetails_Save': args='GuarantorDetails_Frame1'; break;	
		case 'GuarantorDetails_Button2': args='GuarantorDetails_Frame1'; break;
		case 'EmploymentVerification_s2_Designation_button4_View' : args='EmploymentVerification_s2_Text1';break;
		case 'EmploymentVerification_s2_desig_visa_view' : args='EmploymentVerification_s2_desig_visa';break;
		case 'EmploymentVerification_s2_Designation_button6_View' : args='cmplx_emp_ver_sp2_desig_remarks';break;
		case 'EmploymentVerification_s2_desig_visa_update_view' : args='cmplx_emp_ver_sp2_desig_as_visa_update';break;
		case 'PostDisbursal_LC_Add': args='PostDisbursal_Frame1';break;
		case 'PostDisbursal_LC_Modify': args='PostDisbursal_Frame1';break;
		case 'PostDisbursal_LC_Delete': args='PostDisbursal_Frame1';break;
		case 'PostDisbursal_MCQ_Add': args='PostDisbursal_Frame3';break;
		case 'PostDisbursal_MCQ_Modify': args='PostDisbursal_Frame3';break;
		case 'PostDisbursal_MCQ_Delete': args='PostDisbursal_Frame3';break;
		case 'PostDisbursal_BG_Modify': args='PostDisbursal_Frame6';break;
		case 'PostDisbursal_BG_Add': args='PostDisbursal_Frame6';break;
		case 'PostDisbursal_BG_Delete': args='PostDisbursal_Frame6';break
		case 'PostDisbursal_NLC_Modify': args='PostDisbursal_Frame4';break;
		case 'PostDisbursal_NLC_Add': args='PostDisbursal_Frame4';break;
		case 'PostDisbursal_NLC_Delete': args='PostDisbursal_Frame4';break;
		case 'PostDisbursal_Save':args='PostDisbursal_Frame1';break;
		case 'Customer_FetchFirco':	args = 'CustomerDetails';	break;
		case 'RiskRating_CalButton':	args = 'RiskRating_Frame1';	break;
		case 'LoanDeatils_calculateemi': args = 'LoanDetails_Frame1'; break;
		
		

		default: args='@this';
		break;
	}
	return args;
}

//function added by Vaishvi for Special character check -  PCASI - 3594, 3593
function specialCharCheck (input,fieldName,message)
{
	for(i=0; i<input.length; i++)
	{
		a = input.charCodeAt(i);
			if((a === 43) || (a === 45))	//ASCII value for + is 43 and - is 45
			{
				showAlert(fieldName,message);
				break;
			}
	}
}

function specialCharCheckForAddress (input,fieldName,message)
{
	for(i=0; i<input.length; i++)
	{
		a = input.charCodeAt(i);
			if((a === 42) || (a === 43) || (a == 44) || (a == 47))	//ASCII value for * + , / respectively
			{
				showAlert(fieldName,message);
				break;
			}
	}
}

