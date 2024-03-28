function FPUTemplate_PL(){
	var attrbList="";
	attrbList += "&<header>&" +"FPU REPORT@10";
	attrbList += "&<Current_date_time>&" + getNGValue('CurrentDateLabel')+ ' '+new Date().getHours()+':'+new Date().getMinutes() +'@10';
	attrbList += "&<cust_info_winame>&" + get_ngformValue_template('CustDetailVerification1_ApplicationNo');
	attrbList += "&<cust_info_cifno>&" + get_ngformValue_template('CustDetailVerification1_CifId');
	attrbList += "&<customer_name>&" + get_ngformValue_template('CustDetailVerification1_CustName');
	attrbList += "&<customer_dob>&" + get_ngformValue_template('cmplx_CustDetailverification1_DOB_value');
	attrbList += "&<customer_mob>&" + get_ngformValue_template('cmplx_CustDetailverification1_MobNo1_value');
	attrbList += "&<customer_nationality>&" + get_ngformValue_template('CustDetailVerification1_nationality');
	attrbList += "&<cust_visno>&" + get_ngformValue_template('CustDetailVerification1_VisaNo');
	attrbList += "&<cust_visissue>&" + get_ngformValue_template('CustDetailVerification1_VisaIssueDt');
	attrbList += "&<cust_visexp>&" + get_ngformValue_template('CustDetailVerification1_VisaExpDt');
	attrbList += "&<cust_emiratesID>&" + get_ngformValue_template('CustDetailVerification1_EmiratesID');
	attrbList += "&<expDatePrimary>&" + get_ngformValue_template('CustDetailVerification1_EmirIDExpDt');
	attrbList += "&<cust_passport>&" + get_ngformValue_template('CustDetailVerification1_PassNo');
	attrbList += "&<cust_info_remarks>&" + get_ngformValue_template('cmplx_CustDetailverification1_fpuremarks').replace("$","");
	//----**-----
	attrbList += "&<Mobile1>&" + get_ngformValue_template('Cust_ver_sp2_Text6');
	attrbList += "&<Mobile2>&" + get_ngformValue_template('Cust_ver_sp2_Text8');
	attrbList += "&<Contacted_on>&" + get_ngformValue_template('cmplx_cust_ver_sp2_ContactedOn');
	attrbList += "&<Contacted_date>&" + get_ngformValue_template('cmplx_cust_ver_sp2_ContactedDate');
	attrbList += "&<Mobile>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Mobile');
	//attrbList += "&<cust_verifiers_name>&" + get_ngformValue_template('cmplx_cust_ver_sp2_verifier_name');
	//attrbList += "&<cust_verifiers>&" + get_ngformValue_template('cmplx_cust_ver_sp2_verifier');
	if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed' )
	{
		//Customer section
		attrbList += "&<SE_NumEmp>&" + get_ngformValue_template('cmplx_cust_ver_sp2_No_of_employee');
		attrbList += "&<SE_SolOwner>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Applicant_sole_own');
		attrbList += "&<SE_NumPartner>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Nos_part_buss');
		attrbList += "&<SE_Details>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Loc_comp_timings');
		attrbList += "&<SE_NatBuis>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Nature_of_bus');
		attrbList += "&<SE_AppDet>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Since_inv_bus');
		attrbList += "&<SE_CopBanDet>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Coorporate_Bank_Detls');
		attrbList += "&<cust_fpu_remarks>&" + get_ngformValue_template('cmplx_cust_ver_sp2_FPU_Remarks_Self');
		attrbList += "&<cust_fpu_remarks>&" + get_ngformValue_template('cmplx_cust_ver_sp2_FPU_Remarks_Self');

		//Employment section
		attrbList += "&<company_name>&" + get_ngformValue_template('EmploymentVerification_s2_Text11');
		attrbList += "&<company_industry>&" + get_ngformValue_template('EmploymentVerification_s2_Text38');
		attrbList += "&<industry_macro>&" + get_ngformValue_template('EmploymentVerification_s2_Text36');
		attrbList += "&<company_doe>&" + get_ngformValue_template('EmploymentVerification_s2_Text39');
		attrbList += "&<industry_micro>&" + get_ngformValue_template('EmploymentVerification_s2_Text37');
		attrbList += "&<SE_NatBuisEmp>&" + get_ngformValue_template('EmploymentVerification_s2_Text40');
		
		attrbList += "&<SE_Existing1>&" + get_ngformValue_template('EmploymentVerification_s2_Text13');
		attrbList += "&<SE_Existing2>&" + get_ngformValue_template('EmploymentVerification_s2_Text14');
		attrbList += "&<SE_Existing3>&" + get_ngformValue_template('EmploymentVerification_s2_Text15');
			
		attrbList += "&<SE_NumEmpUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_numberOfEmpl');
		attrbList += "&<SE_LobUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_lob');
		attrbList += "&<SE_EloUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_effective_own');
		
		attrbList += "&<SE_AnnIncUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_annual_inc');
		attrbList += "&<SE_PriBanUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_primary_bank');
		attrbList += "&<SE_SecBanUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_sec_bank');
		
		attrbList += "&<emp_cat_pl>&" + get_ngformValue_template('EmploymentVerification_s2_Text5');
		attrbList += "&<emp_cat_cc>&" + get_ngformValue_template('EmploymentVerification_s2_Text8');
		
		attrbList += "&<SE_nameontl>&" + get_ngformValue_template('EmploymentVerification_s2_Text16');
		attrbList += "&<SE_tlnum>&" + get_ngformValue_template('EmploymentVerification_s2_Text18');
		attrbList += "&<SE_emirates>&" + get_ngformValue_template('EmploymentVerification_s2_Text41');
		attrbList += "&<SE_NatBuisTl>&" + get_ngformValue_template('EmploymentVerification_s2_Text17');
		attrbList += "&<freezone>&" + get_ngformValue_template('EmploymentVerification_s2_freezone');


		attrbList += "&<comp_prev_employer>&" + get_ngformValue_template('cmplx_emp_ver_sp2_PreviousEmployer');
		attrbList += "&<positive_financial_strength>&" + get_ngformValue_template('cmplx_emp_ver_sp2_strength_drop');
		attrbList += "&<fin_Mismatch>&" + get_ngformValue_template('cmplx_emp_ver_sp2_strength_drop');	

	}else {
		attrbList = getVerificationData(attrbList,'cust_Designation','Cust_ver_sp2_Text13','cmplx_cust_ver_sp2_desig_drop','cmplx_cust_ver_sp2_desig_remarks');
		
		attrbList = getVerificationData(attrbList,'cust_DOJ','Cust_ver_sp2_Text14','cmplx_cust_ver_sp2_doj_drop','cmplx_cust_ver_sp2_doj_remarks');
		attrbList = getVerificationData(attrbList,'cust_employment_status','Cust_ver_sp2_Combo9','cmplx_cust_ver_sp2_emp_status_drop','cmplx_cust_ver_sp2_emp_status__remarks');
		attrbList = getVerificationData(attrbList,'cust_salary','Cust_ver_sp2_Text16','cmplx_cust_ver_sp2_saalry_drop','cmplx_cust_ver_sp2_salary_remarks');
		attrbList = getVerificationData(attrbList,'salary_payment_mode','Cust_ver_sp2_Text20','cmplx_cust_ver_sp2_salary_payment_drop','cmplx_cust_ver_sp2_salary_payment_remarks');
		attrbList = getVerificationData(attrbList,'yrs_uae','Cust_ver_sp2_Text18','cmplx_cust_ver_sp2_years_drop','cmplx_cust_ver_sp2_years_remarks');
		attrbList = getVerificationData(attrbList,'los_at_prev_emp','Cust_ver_sp2_Text4','cmplx_cust_ver_sp2_prevemp_los_drop','cmplx_cust_ver_sp2_prevemp_los_remark');
		attrbList = getVerificationData(attrbList,'emp_Designation','EmploymentVerification_s2_Text1','cmplx_emp_ver_sp2_desig_drop','cmplx_emp_ver_sp2_desig_remarks');
		//attrbList = getVerificationData(attrbList,'emirate','EmploymentVerification_s2_Combo15','cmplx_emp_ver_sp2_emirate_drop','cmplx_emp_ver_sp2_emirate_remarks');
		attrbList = getVerificationData(attrbList,'emp_DOJ','EmploymentVerification_s2_Text4','cmplx_emp_ver_sp2_doj_drop','cmplx_emp_ver_sp2_doj_remarks');
		attrbList = getVerificationData(attrbList,'emp_employement_status','EmploymentVerification_s2_Combo14','cmplx_emp_ver_sp2_empstatus_drop','cmplx_emp_ver_sp2_empstatus_remarks');
		attrbList = getVerificationData(attrbList,'emp_salary','EmploymentVerification_s2_Text7','cmplx_emp_ver_sp2_salary_drop','cmplx_emp_ver_sp2_salary_remarks');
		
		attrbList = getVerificationData(attrbList,'emp_salary_break','','cmplx_emp_ver_sp2_salary_break_drop','cmplx_emp_ver_sp2_salary_break_remarks');
		
		attrbList = getVerificationData(attrbList,'emp_salary_paymode','EmploymentVerification_s2_Text29','cmplx_emp_ver_sp2_sal_pay_drop','cmplx_emp_ver_sp2_sal_pay_remarks');
		attrbList = getVerificationData(attrbList,'loan_deduction','','cmplx_emp_ver_sp2_loan_drop','cmplx_emp_ver_sp2_loan_remarks');
		attrbList = getVerificationData(attrbList,'los_at_prev_emp','Cust_ver_sp2_Text4','cmplx_cust_ver_sp2_prevemp_los_drop','cmplx_cust_ver_sp2_prevemp_los_remark');
		//attrbList = getVerificationData(attrbList,'Authorized_person_name','','cmplx_emp_ver_sp2_loan_drop','cmplx_emp_ver_sp2_loan_remarks');	
		//sagarika add office_address  Authorized_person_name 
		//attrbList += "&<designation_as_per_visa>&" + get_ngformValue_template('cmplx_emp_ver_sp2_strength_drop');
		attrbList += "&<Authorized_person_name>&" + get_ngformValue_template('EmploymentVerification_s2_auth_name');
		attrbList += "&<designation_as_per_visa>&" + get_ngformValue_template('EmploymentVerification_s2_desig_visa');
			attrbList += "&<office_address>&" + get_ngformValue_template('EmploymentVerification_s2_off_add');
		attrbList += "&<flat_house>&" + get_ngformValue_template('EmploymentVerification_s2_flat_vila');
		attrbList += "&<building_name>&" + get_ngformValue_template('EmploymentVerification_s2_Build_name');
		attrbList += "&<street_name>&" + get_ngformValue_template('EmploymentVerification_s2_street_name');
		attrbList += "&<landmark_name>&" + get_ngformValue_template('EmploymentVerification_s2_landmark');
		attrbList += "&<area_city>&" + get_ngformValue_template('EmploymentVerification_s2_Area_city');
		attrbList += "&<labour_card>&" + get_ngformValue_template('EmploymentVerification_s2_labor_card');
		attrbList += "&<mol_salary>&" + get_ngformValue_template('EmploymentVerification_s2_MOL_salary');
		attrbList += "&<is_freezone>&" + get_ngformValue_template('EmploymentVerification_s2_freezone');
		attrbList += "&<visa_company>&" + get_ngformValue_template('EmploymentVerification_s2_visa_company_name');
		attrbList += "&<company_name_loc>&" + get_ngformValue_template('EmploymentVerification_s2_aloc_name');
		attrbList += "&<tl_number>&" + get_ngformValue_template('EmploymentVerification_s2_TL_Number');
		attrbList += "&<tl_emirate>&" + get_ngformValue_template('EmploymentVerification_s2_TL_emirate');
		attrbList += "&<confirmed_in_job>&" + get_ngformValue_template('EmploymentVerification_s2_COB');
		attrbList += "&<loan_advanceform>&" + get_ngformValue_template('cmplx_emp_ver_sp2_loan_adv_from');
		attrbList += "&<permanent_deduction>&" + get_ngformValue_template('cmplx_emp_ver_sp2_Permanent_deduct');
		attrbList += "&<prev_employer>&" + get_ngformValue_template('cmplx_emp_ver_sp2_Prev_employer');
		//sagarika
		//attrbList += "&<positive_financial_strength>&" + get_ngformValue_template('cmplx_emp_ver_sp2_strength_rem');
		attrbList = getVerificationData(attrbList,'positive_financial','','cmplx_cust_ver_sp2_emp_status_drop','cmplx_emp_ver_sp2_strength_rem');
		
		attrbList += "&<cust_fpu_remarks>&" + get_ngformValue_template('cmplx_cust_ver_sp2_fpu_remarks').replace("$","");

	}

	attrbList += "&<previous_employer>&" + get_ngformValue_template('cmplx_cust_ver_sp2_prev_emp_remarks');
	//attrbList += "&<previous_employer>&" + get_ngformValue_template('cmplx_cust_ver_sp2_prev_emp_remarks');
	
	attrbList += "&<ofc_tel_number>&" + get_ngformValue_template('EmploymentVerification_s2_Text23');
	attrbList += "&<Verification_emp>&" + get_ngformValue_template('cmplx_emp_ver_sp2_off_drop');
	attrbList += "&<landline_number>&" + get_ngformValue_template('cmplx_emp_ver_sp2_landline');
	attrbList += "&<emp_verifiers_name>&" + get_ngformValue_template('cmplx_emp_ver_sp2_verifier_name');
	attrbList += "&<emp_verifiers>&" + get_ngformValue_template('cmplx_emp_ver_sp2_verifier');
	attrbList += "&<application_type>&" + get_ngformValue_template('cmplx_emp_ver_sp2_application_type');
	
	
	attrbList += "&<emp_fpu_remarks>&" + get_ngformValue_template('cmplx_emp_ver_sp2_fpu_rem').replace("$","");
	attrbList += "&<Date>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_drop1');
	attrbList += "&<field_visit_initiated>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_drop3');
	attrbList += "&<field_visit_initiated_date>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_field_visit_date');
	attrbList += "&<field_visit_initiated_time>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_field_v_time');
	attrbList += "&<field_visit_report>&" + (get_ngformValue_template('cmplx_fieldvisit_sp2_field_rep_receivedDate')+' '+get_ngformValue_template('cmplx_fieldvisit_sp2_field_visit_rec_time'));
	attrbList += "&<acc_stat_verifi>&" + get_ngformValue_template('cmplx_BankingCheck_AccStatementUpdate');
	attrbList += "&<nlc_verifi>&" + get_ngformValue_template('cmplx_BankingCheck_NLCVerificatonUpdate');
	attrbList += "&<company_list_check>&" + get_ngformValue_template('cmplx_BankingCheck_CompanyListingUpdate');
	
	attrbList = getBankingCheckUpdates(attrbList,'BC_acc_details','cmplx_BankingCheck_BankAccDetailUpdate','cmplx_BankingCheck_BankAccDetailRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_salary_credits','cmplx_BankingCheck_SalaryCreditUpdate','cmplx_BankingCheck_SalaryCreditRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_acc_conduct','cmplx_BankingCheck_AccConductUpdate','cmplx_BankingCheck_AccConductRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_liability_check','cmplx_BankingCheck_LiabilityCheckUpdate','cmplx_BankingCheck_LiabilityCheckRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_repayment_conduct','cmplx_BankingCheck_RepaymentConductUpdate','cmplx_BankingCheck_RepaymentConductRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_creditcard_stat_verifi','cmplx_BankingCheck_CCVerificationUpdate','cmplx_BankingCheck_CCVerificationRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_acc_stat_verifi','cmplx_BankingCheck_AccStatementUpdate','cmplx_BankingCheck_AccStatementRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_nlc_verifi','cmplx_BankingCheck_NLCVerificatonUpdate','cmplx_BankingCheck_NLCVericationRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_company_list_check','cmplx_BankingCheck_CompanyListingUpdate','cmplx_BankingCheck_CompanyListingRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_Aecb','cmplx_BankingCheck_AECBMismatchUpdate','cmplx_BankingCheck_AECBMismatchRemarks');

	attrbList += "&<CL_EIDVal>&" +get_ngformValue_template('cmplx_checklist_sp2_eid');
	attrbList += "&<CL_VisaVal>&" +get_ngformValue_template('cmplx_checklist_sp2_visa');
	attrbList += "&<CL_AECBCheck>&"  +get_ngformValue_template('cmplx_checklist_sp2_aecb');
	attrbList += "&<CL_DirectExcp>&" +get_ngformValue_template('cmplx_checklist_sp2_direct_exp');
	attrbList += "&<CL_MOLCheck>&"  +get_ngformValue_template('cmplx_checklist_sp2_mol');
	attrbList += "&<CL_AlocCheck>&" +get_ngformValue_template('cmplx_checklist_sp2_aloc');
	attrbList += "&<CL_CompDirectExp>&"+get_ngformValue_template('cmplx_checklist_sp2_company_direct');
	attrbList += "&<CL_CardandLoanP>&"  +get_ngformValue_template('cmplx_checklist_sp2_card');
	attrbList += "&<CL_DedupeCheck>&" +get_ngformValue_template('cmplx_checklist_sp2_dedupe');
	attrbList += "&<CL_TLCheck>&" +get_ngformValue_template('cmplx_checklist_sp2_tl');
	attrbList += "&<CL_AddCheck1>&" +get_ngformValue_template('cmplx_checklist_sp2_add_1');
	attrbList += "&<CL_AddCheck2>&" +get_ngformValue_template('cmplx_checklist_sp2_add_2');
	attrbList += "&<CL_AddCheck3>&"  +get_ngformValue_template('cmplx_checklist_sp2_add_3');
	
	var username = window.parent.userName;
	attrbList += "&<FPU_Grid1>&" + username +'@10';
	attrbList += "&<FPU_Grid2>&" + getNGValue('cmplx_FPU_Grid_case_recived_date')+'@10';
	attrbList += "&<FPU_Grid3>&" + getNGValue('cmplx_FPU_Grid_Realloc_Analyst')+'@10';
	attrbList += "&<FPU_Grid4>&" + getNGValue('cmplx_FPU_Grid_Realloc_recived_date')+'@10';
	attrbList += "&<FPU_Grid5>&" + getNGValue('cmplx_FPU_Grid_CLosure_date')+'@10';
	attrbList += "&<FPU_Grid10>&" + getNGValue('cmplx_FPU_Grid_case_status')+'@10';
	attrbList += "&<FPU_Grid7>&" + getNGValue('cmplx_FPU_Grid_Officer_Name')+'@10';
	attrbList += "&<FPU_Grid8>&" + getNGValue('Fpu_Grid_Combo1')+'@10';
	attrbList += "&<FPU_Grid9>&" + getNGValue('cmplx_FPU_Grid_Actionable')+'@10';
	//attrbList += "&<FPU_Grid10>&" + getNGValue('cmplx_FPU_Grid_case_status')+'@10';

	return attrbList;
}


function calcLoanMultiple()
{
//PCASI-1726
var finalLoanAmount = com.newgen.omniforms.formviewer.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden");
var FinalTAI= com.newgen.omniforms.formviewer.getNGValue("cmplx_EligibilityAndProductInfo_FinalTAI");
if (window.parent.strprocessname=='PersonalLoanS' &&( finalLoanAmount=='0' || finalLoanAmount==''))
{
	if(getFromJSPTable('ELigibiltyAndProductInfo_IFrame3')!=null)
		finalLoanAmount=getFromJSPTable('ELigibiltyAndProductInfo_IFrame3').rows[1].cells[4].childNodes[0].value;
}
var loanMultiple=parseFloat(finalLoanAmount)/ parseFloat(FinalTAI);

// changes done by disha for PCSP-62 - 13/12/2108 NaN value appears when finalLoanAmount is blank due to parse float calculation
if (window.parent.strprocessname=='CreditCard' || loanMultiple=='NaN' || loanMultiple==NaN || loanMultiple=='')
{
	return '@10';
}
else{
return loanMultiple.toFixed(2) + "@10";
}
}

function get_ngformValue_template(complx_name){
	
		var field_value = com.newgen.omniforms.formviewer.getNGValue(complx_name)+'';
		if(field_value=='false'){
			field_value='NO';
		} 
		else if(field_value=='true'){
			field_value='YES';
		}
		else if(field_value=='--Select--' || field_value==null+'' || field_value==''){
			field_value='';
		}
		else if(field_value==false){
			field_value='';
		}
		return field_value + "@10";
	
}

function get_ngformText_template(complx_name){
	var field_value =  com.newgen.omniforms.formviewer.getNGSelectedItemText(complx_name);
	if(field_value==true || field_value=='true'){
		field_value='YES';
	}
	//change by saurabh on 24/12/18.
	else if(field_value=='--Select--' || field_value==null || field_value==''){
		field_value='';
	}
	else if(field_value==false || field_value=='false'){
		field_value='NO';
	} 
	return field_value + "@10";
}

function get_ngformText_template_dev(complx_name){
	var field_value =  trimStr(com.newgen.omniforms.formviewer.getNGValue(complx_name));
	//var field_arr = field_value.split('\n');
	//chnage by saurabh for PCSP-318
	var temp = field_value;
	/*if(field_value!=''){
	var field_arr = field_value.split('\n');
		for(var i=0;i<field_arr.length;i++){
			temp= temp+ field_arr[i].substring(field_arr[i].lastIndexOf('-')+1,	field_arr[i].length)+',';
		}
		if(temp.charAt(temp.length-1)==','){
			temp = temp.substring(0,temp.length-1);
		}
	}*/
	 	
	return temp + "@10";
}


function get_ngformGridColumn_template(complx_name,rowIndex,columnIndex) {
	var field_value=com.newgen.omniforms.formviewer.getLVWAT(complx_name,rowIndex,columnIndex);
	
	 if(field_value=='--Select--' || field_value==null || field_value==''){
		field_value='';
	} 	
	else if(field_value=='1.00'|| field_value=='1.0'){
		field_value='1.0';
	}
	else if(field_value==true || field_value=='true' || field_value=='T'){
		field_value='YES';
	}
	else if(field_value==false || field_value=='false' || field_value=='F'){
		field_value='NO';
	} 
	
	return field_value + "@10";
}

function get_ngform_controlObject(control_name){//return object of a form dropdown control
	try{
	return document.getElementById(control_name);
}
catch(err){console.log(err);}
}
function get_ngformGridCode_Description(CodeWithSpace,control_name){
	try{
	var code=CodeWithSpace.split('@')[0];
	for(var i=0;i<control_name.length;i++)
	{
		if(control_name[i].value==code){
			return control_name.options[i].text + "@10";
		}	
	}
	return " "+"@10";
	}
catch(err){console.log(err);}

}
//function added by Saurabh for FPU Report CR.
function FPUTemplate(){
	var attrbList="";
	attrbList += "&<header>&" +"FPU REPORT@10";
	attrbList += "&<Current_date_time>&" + getNGValue('CurrentDateLabel')+ ' '+new Date().getHours()+':'+new Date().getMinutes() +'@10';
	//Customer information
	attrbList = getVerificationData(attrbList,'cust_info_winame','CustDetailVerification1_ApplicationNo','cmplx_CustDetailverification1_Application_No_drop','cmplx_CustDetailverification1_Application_No_remarks');
	
	attrbList = getVerificationData(attrbList,'cust_info_cifno','CustDetailVerification1_CifId','cmplx_CustDetailverification1_CIF_drop','cmplx_CustDetailverification1_CIF_remark');
	attrbList = getVerificationData(attrbList,'customer_name','CustDetailVerification1_CustName','cmplx_CustDetailverification1_cust_name_drop','cmplx_CustDetailverification1_cust_name_remark');
	attrbList = getVerificationData(attrbList,'customer_dob','cmplx_CustDetailverification1_DOB_value','cmplx_CustDetailverification1_DOB_ver','cmplx_CustDetailverification1_Dob_update');
	attrbList = getVerificationData(attrbList,'customer_mob','cmplx_CustDetailverification1_MobNo1_value','cmplx_CustDetailverification1_MobNo1_veri','cmplx_CustDetailverification1_MobNo1_updates');
	attrbList = getVerificationData(attrbList,'customer_nationality','CustDetailVerification1_nationality','cmplx_CustDetailverification1_Nationality_drop','cmplx_CustDetailverification1_Nationality_remark');
	
	attrbList = getVerificationData(attrbList,'cust_visno','CustDetailVerification1_VisaNo','cmplx_CustDetailverification1_Visa_No_drop','cmplx_CustDetailverification1_Visa_No_remark');
	
	attrbList = getVerificationData(attrbList,'cust_visissue','CustDetailVerification1_VisaIssueDt','cmplx_CustDetailverification1_VisaIssue_Date_drop','cmplx_CustDetailverification1_VisaIssue_date_remark');
	attrbList = getVerificationData(attrbList,'cust_visexp','CustDetailVerification1_VisaExpDt','cmplx_CustDetailverification1_VisaExpiry_date_drop','cmplx_CustDetailverification1_VisaExpiry_date_remark');
	attrbList = getVerificationData(attrbList,'cust_emiratesID','CustDetailVerification1_EmiratesID','cmplx_CustDetailverification1_Emirate_Id_drop','cmplx_CustDetailverification1_Emirate_Id_remark');
	attrbList = getVerificationData(attrbList,'expDatePrimary','CustDetailVerification1_EmirIDExpDt','cmplx_CustDetailverification1_EmirateIDExp_Date_drop','cmplx_CustDetailverification1_EmirateIDExp_Date_remark');
	attrbList = getVerificationData(attrbList,'cust_passport','CustDetailVerification1_PassNo','cmplx_CustDetailverification1_Passport_NO_drop','cmplx_CustDetailverification1_Passport_NO_remark');
	attrbList += "&<cust_info_remarks>&" + get_ngformValue_template('cmplx_CustDetailverification1_fpuremarks').replace("$","");
	//----**-----
	attrbList += "&<Mobile1>&" + get_ngformValue_template('Cust_ver_sp2_Text6');
	attrbList += "&<Mobile2>&" + get_ngformValue_template('Cust_ver_sp2_Text8');
	attrbList += "&<Contacted_on>&" + get_ngformValue_template('cmplx_cust_ver_sp2_ContactedOn');
	attrbList += "&<Contacted_Date>&" + get_ngformValue_template('cmplx_cust_ver_sp2_ContactedDate');//anuj140721
	attrbList += "&<Mobile>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Mobile');
	attrbList += "&<cust_verifiers_name>&" + get_ngformValue_template('cmplx_cust_ver_sp2_verifier_name');
	attrbList += "&<cust_verifiers>&" + get_ngformValue_template('cmplx_cust_ver_sp2_verifier');
	attrbList += "&<los_at_prev_emp>&" + get_ngformValue_template('Cust_ver_sp2_Text4');//anuj140721
	attrbList += "&<los_at_prev_emp_ver>&" + get_ngformValue_template('cmplx_cust_ver_sp2_prevemp_los_drop');//anuj140721
	attrbList += "&<designation_as_per_visa>&" + get_ngformValue_template('EmploymentVerification_s2_desig_visa');//anuj140721
	attrbList += "&<designation_as_per_visa_upd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_desig_as_visa_update');//anuj140721
	attrbList += "&<address_type>&" + get_ngformValue_template('EmploymentVerification_s2_off_add');//anuj140721
	attrbList += "&<address_type_upd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_Auth_person_update');//anuj140721
	attrbList += "&<flat_villa_house>&" + get_ngformValue_template('EmploymentVerification_s2_flat_vila');//anuj140721
	attrbList += "&<flat_villa_house_upd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_flat_villa_update');//anuj140721
	attrbList += "&<building_Name>&" + get_ngformValue_template('EmploymentVerification_s2_Build_name');//anuj140721
	attrbList += "&<building_upd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_building_NAme_update');//anuj140721
	attrbList += "&<landmark_type>&" + get_ngformValue_template('EmploymentVerification_s2_landmark');//anuj140721
	attrbList += "&<landmark_type_upd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_landmark_update');//anuj140721
	attrbList += "&<street_name_type>&" + get_ngformValue_template('EmploymentVerification_s2_street_name');//anuj140721
	attrbList += "&<street_name_type_upd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_street_name_update');//anuj140721
	attrbList += "&<area_city>&" + get_ngformValue_template('EmploymentVerification_s2_Area_city');//anuj140721
	attrbList += "&<area_city_type>&" + get_ngformValue_template('cmplx_emp_ver_sp2_Area_city_update');//anuj140721 
	attrbList += "&<labour_card_number>&" + get_ngformValue_template('EmploymentVerification_s2_labor_card');//anuj140721
	attrbList += "&<mol_salary>&" + get_ngformValue_template('EmploymentVerification_s2_MOL_salary');//anuj140721
	attrbList += "&<is_a_freezone>&" + get_ngformValue_template('EmploymentVerification_s2_freezone');//anuj140721
	attrbList += "&<freezone_name>&" + get_ngformValue_template('EmploymentVerification_s2_Freezone_name');//anuj140721 
	attrbList += "&<visa_company_name>&" + get_ngformValue_template('EmploymentVerification_s2_visa_company_name');//anuj140721
	attrbList += "&<company_name_as_aloc>&" + get_ngformValue_template('EmploymentVerification_s2_aloc_name');//anuj140721
	attrbList += "&<tl_number>&" + get_ngformValue_template('EmploymentVerification_s2_TL_Number');//anuj140721
	attrbList += "&<company_name_as_doc>&" + get_ngformValue_template('cmplx_emp_ver_sp2_company_name');//anuj140721
	attrbList += "&<tl_emirate>&" + get_ngformValue_template('EmploymentVerification_s2_TL_emirate');//anuj140721
	attrbList += "&<confirmed_in_job>&" + get_ngformValue_template('EmploymentVerification_s2_COB');//anuj140721
	attrbList += "&<load_advance_form>&" + get_ngformValue_template('cmplx_emp_ver_sp2_loan_adv_from');//anuj140721
	attrbList += "&<permanent_deduction_From>&" + get_ngformValue_template('cmplx_emp_ver_sp2_Permanent_deduct');//anuj140721
	attrbList += "&<previous_employer_emp_ver>&" + get_ngformValue_template('cmplx_emp_ver_sp2_Prev_employer');//anuj140721
	attrbList += "&<employer_name>&" + get_ngformValue_template('EmploymentVerification_s2_loan_emp_name');//anuj140721
	attrbList += "&<total_no_active_loan>&" + get_ngformValue_template('cmplx_emp_ver_sp2_num_active_loan');//anuj140721
	attrbList += "&<total_no_active_cc>&" + get_ngformValue_template('cmplx_emp_ver_sp2_num_active_CC');//anuj140721
	attrbList += "&<field_visit_recived>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_field_visit_rec_time');//anuj140721
	attrbList += "&<efms_check>&" + get_ngformValue_template('cmplx_checklist_sp2_add_1');//anuj140721
	attrbList += "&<rb5_check>&" + get_ngformValue_template('cmplx_checklist_sp2_add_2');//anuj140721
	attrbList += "&<mohre_status>&" + get_ngformValue_template('cmplx_checklist_sp2_add_3');//anuj140721
	attrbList += "&<field_visit_rep_check>&" + get_ngformValue_template('cmplx_checklist_sp2_fvr');//anuj140721
	attrbList += "&<card_portfolio_check>&" + get_ngformValue_template('cmplx_checklist_sp2_card');//anuj140721
	attrbList += "&<comapny_direct_exposure>&" + get_ngformValue_template('cmplx_checklist_sp2_company_direct');//anuj140721
	attrbList += "&<FPU_Grid11>&" + get_ngformValue_template('cmplx_FPU_Grid_ordb_app');//anuj140721
	attrbList += "&<employer_name_mismatch>&" + get_ngformValue_template('EmploymentVerification_s2_comp_emp_name');//anuj140721
	attrbList += "&<tl_number_mismatch>&" + get_ngformValue_template('EmploymentVerification_s2_comp_tl_no');//anuj140721
	attrbList += "&<tl_emirate_mismatch>&" + get_ngformValue_template('EmploymetlntVerification_s2_comp_emirate');//anuj140721
	if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed' )
	{
		//Customer section
		attrbList += "&<SE_NumEmp>&" + get_ngformValue_template('cmplx_cust_ver_sp2_No_of_employee');
		attrbList += "&<SE_SolOwner>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Applicant_sole_own');
		attrbList += "&<SE_NumPartner>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Nos_part_buss');
		attrbList += "&<SE_Details>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Loc_comp_timings');
		attrbList += "&<SE_NatBuis>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Nature_of_bus');
		attrbList += "&<SE_AppDet>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Since_inv_bus');
		attrbList += "&<SE_CopBanDet>&" + get_ngformValue_template('cmplx_cust_ver_sp2_Coorporate_Bank_Detls');
		attrbList += "&<cust_fpu_remarks>&" + get_ngformValue_template('cmplx_cust_ver_sp2_FPU_Remarks_Self');
		attrbList += "&<cust_fpu_remarks>&" + get_ngformValue_template('cmplx_cust_ver_sp2_FPU_Remarks_Self');

		//Employment section
		attrbList += "&<company_name>&" + get_ngformValue_template('EmploymentVerification_s2_Text11');
		attrbList += "&<company_industry>&" + get_ngformValue_template('EmploymentVerification_s2_Text38');
		attrbList += "&<industry_macro>&" + get_ngformValue_template('EmploymentVerification_s2_Text36');
		attrbList += "&<company_doe>&" + get_ngformValue_template('EmploymentVerification_s2_Text39');
		attrbList += "&<industry_micro>&" + get_ngformValue_template('EmploymentVerification_s2_Text37');
		attrbList += "&<SE_NatBuisEmp>&" + get_ngformValue_template('EmploymentVerification_s2_Text40');
		
		attrbList += "&<SE_Existing1>&" + get_ngformValue_template('EmploymentVerification_s2_Text13');
		attrbList += "&<SE_Existing2>&" + get_ngformValue_template('EmploymentVerification_s2_Text14');
		attrbList += "&<SE_Existing3>&" + get_ngformValue_template('EmploymentVerification_s2_Text15');
			
		attrbList += "&<SE_NumEmpUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_numberOfEmpl');
		attrbList += "&<SE_LobUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_lob');
		attrbList += "&<SE_EloUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_effective_own');
		
		attrbList += "&<SE_AnnIncUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_annual_inc');
		attrbList += "&<SE_PriBanUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_primary_bank');
		attrbList += "&<SE_SecBanUpd>&" + get_ngformValue_template('cmplx_emp_ver_sp2_sec_bank');
		
		attrbList += "&<emp_cat_pl>&" + get_ngformValue_template('EmploymentVerification_s2_Text5');
		attrbList += "&<emp_cat_cc>&" + get_ngformValue_template('EmploymentVerification_s2_Text8');
		
		attrbList += "&<SE_nameontl>&" + get_ngformValue_template('EmploymentVerification_s2_Text16');
		attrbList += "&<SE_tlnum>&" + get_ngformValue_template('EmploymentVerification_s2_Text18');
		attrbList += "&<SE_emirates>&" + get_ngformValue_template('EmploymentVerification_s2_Text41');
		attrbList += "&<SE_NatBuisTl>&" + get_ngformValue_template('EmploymentVerification_s2_Text17');
		attrbList += "&<freezone>&" + get_ngformValue_template('EmploymentVerification_s2_CheckBox2');


		attrbList += "&<comp_prev_employer>&" + get_ngformValue_template('cmplx_emp_ver_sp2_PreviousEmployer');
		attrbList += "&<positive_financial_strength>&" + get_ngformValue_template('cmplx_emp_ver_sp2_strength_rem');
		attrbList += "&<fin_Mismatch>&" + get_ngformValue_template('cmplx_emp_ver_sp2_strength_drop');	

	}else {
		attrbList = getVerificationData(attrbList,'cust_Designation','Cust_ver_sp2_Text13','cmplx_cust_ver_sp2_desig_drop','cmplx_cust_ver_sp2_desig_remarks');
		
		attrbList = getVerificationData(attrbList,'cust_DOJ','Cust_ver_sp2_Text14','cmplx_cust_ver_sp2_doj_drop','cmplx_cust_ver_sp2_doj_remarks');
		attrbList = getVerificationData(attrbList,'cust_employment_status','Cust_ver_sp2_Combo9','cmplx_cust_ver_sp2_emp_status_drop','cmplx_cust_ver_sp2_emp_status__remarks');
		attrbList = getVerificationData(attrbList,'cust_salary','Cust_ver_sp2_Text16','cmplx_cust_ver_sp2_saalry_drop','cmplx_cust_ver_sp2_salary_remarks');
		attrbList = getVerificationData(attrbList,'salary_payment_mode','Cust_ver_sp2_Text20','cmplx_cust_ver_sp2_salary_payment_drop','cmplx_cust_ver_sp2_salary_payment_remarks');
		attrbList = getVerificationData(attrbList,'yrs_uae','Cust_ver_sp2_Text18','cmplx_cust_ver_sp2_years_drop','cmplx_cust_ver_sp2_years_remarks');
		attrbList = getVerificationData(attrbList,'emp_Designation','EmploymentVerification_s2_Text1','cmplx_emp_ver_sp2_desig_drop','cmplx_emp_ver_sp2_desig_remarks');
		attrbList = getVerificationData(attrbList,'emirate','EmploymentVerification_s2_Combo15','cmplx_emp_ver_sp2_emirate_drop','cmplx_emp_ver_sp2_emirate_remarks');
		attrbList = getVerificationData(attrbList,'emp_DOJ','EmploymentVerification_s2_Text4','cmplx_emp_ver_sp2_doj_drop','cmplx_emp_ver_sp2_doj_remarks');
		attrbList = getVerificationData(attrbList,'emp_employement_status','EmploymentVerification_s2_Combo14','cmplx_emp_ver_sp2_empstatus_drop','cmplx_emp_ver_sp2_empstatus_remarks');
		attrbList = getVerificationData(attrbList,'emp_salary','EmploymentVerification_s2_Text7','cmplx_emp_ver_sp2_salary_drop','cmplx_emp_ver_sp2_salary_remarks');
		
		attrbList = getVerificationData(attrbList,'emp_salary_break','','cmplx_emp_ver_sp2_salary_break_drop','cmplx_emp_ver_sp2_salary_break_remarks');
		
		attrbList = getVerificationData(attrbList,'emp_salary_paymode','EmploymentVerification_s2_Text29','cmplx_emp_ver_sp2_sal_pay_drop','cmplx_emp_ver_sp2_sal_pay_remarks');
		
		attrbList = getVerificationData(attrbList,'loan_deduction','','cmplx_emp_ver_sp2_loan_drop','cmplx_emp_ver_sp2_loan_remarks');	
		attrbList += "&<positive_financial_strength>&" + get_ngformValue_template('cmplx_emp_ver_sp2_strength_drop');
		attrbList += "&<cust_fpu_remarks>&" + get_ngformValue_template('cmplx_cust_ver_sp2_fpu_remarks').replace("$","");

	}

	attrbList += "&<previous_employer>&" + get_ngformValue_template('cmplx_cust_ver_sp2_prev_emp_remarks');
	attrbList += "&<ofc_tel_number>&" + get_ngformValue_template('EmploymentVerification_s2_Text23');
	attrbList += "&<landline_number>&" + get_ngformValue_template('cmplx_emp_ver_sp2_landline');
	attrbList += "&<emp_verifiers_name>&" + get_ngformValue_template('cmplx_emp_ver_sp2_verifier_name');
	attrbList += "&<emp_verifiers>&" + get_ngformValue_template('cmplx_emp_ver_sp2_verifier');
	attrbList += "&<application_type>&" + get_ngformValue_template('cmplx_emp_ver_sp2_application_type');
	
	
	attrbList += "&<emp_fpu_remarks>&" + get_ngformValue_template('cmplx_emp_ver_sp2_fpu_rem').replace("$","");
	attrbList += "&<Date>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_drop1');
	attrbList += "&<field_visit_initiated>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_drop3');
	attrbList += "&<field_visit_initiated_date>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_field_visit_date');
	attrbList += "&<field_visit_initiated_time>&" + get_ngformValue_template('cmplx_fieldvisit_sp2_field_v_time');
	attrbList += "&<field_visit_report>&" + (get_ngformValue_template('cmplx_fieldvisit_sp2_field_rep_receivedDate')+' '+get_ngformValue_template('cmplx_fieldvisit_sp2_field_visit_rec_time'));
	attrbList += "&<acc_stat_verifi>&" + get_ngformValue_template('cmplx_BankingCheck_AccStatementUpdate');
	attrbList += "&<nlc_verifi>&" + get_ngformValue_template('cmplx_BankingCheck_NLCVerificatonUpdate');
	attrbList += "&<company_list_check>&" + get_ngformValue_template('cmplx_BankingCheck_CompanyListingUpdate');
	
	attrbList = getBankingCheckUpdates(attrbList,'BC_acc_details','cmplx_BankingCheck_BankAccDetailUpdate','cmplx_BankingCheck_BankAccDetailRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_salary_credits','cmplx_BankingCheck_SalaryCreditUpdate','cmplx_BankingCheck_SalaryCreditRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_acc_conduct','cmplx_BankingCheck_AccConductUpdate','cmplx_BankingCheck_AccConductRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_liability_check','cmplx_BankingCheck_LiabilityCheckUpdate','cmplx_BankingCheck_LiabilityCheckRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_repayment_conduct','cmplx_BankingCheck_RepaymentConductUpdate','cmplx_BankingCheck_RepaymentConductRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_creditcard_stat_verifi','cmplx_BankingCheck_CCVerificationUpdate','cmplx_BankingCheck_CCVerificationRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_acc_stat_verifi','cmplx_BankingCheck_AccStatementUpdate','cmplx_BankingCheck_AccStatementRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_nlc_verifi','cmplx_BankingCheck_NLCVerificatonUpdate','cmplx_BankingCheck_NLCVericationRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_company_list_check','cmplx_BankingCheck_CompanyListingUpdate','cmplx_BankingCheck_CompanyListingRemarks');
	attrbList = getBankingCheckUpdates(attrbList,'BC_Aecb','cmplx_BankingCheck_AECBMismatchUpdate','cmplx_BankingCheck_AECBMismatchRemarks');

	attrbList += "&<CL_EIDVal>&" +get_ngformValue_template('cmplx_checklist_sp2_eid');
	attrbList += "&<CL_VisaVal>&" +get_ngformValue_template('cmplx_checklist_sp2_visa');
	attrbList += "&<CL_AECBCheck>&"  +get_ngformValue_template('cmplx_checklist_sp2_aecb');
	attrbList += "&<CL_DirectExcp>&" +get_ngformValue_template('cmplx_checklist_sp2_direct_exp');
	attrbList += "&<CL_MOLCheck>&"  +get_ngformValue_template('cmplx_checklist_sp2_mol');
	attrbList += "&<CL_AlocCheck>&" +get_ngformValue_template('cmplx_checklist_sp2_aloc');
	attrbList += "&<CL_CompDirectExp>&"+get_ngformValue_template('cmplx_checklist_sp2_company_direct');
	attrbList += "&<CL_CardandLoanP>&"  +get_ngformValue_template('cmplx_checklist_sp2_card');
	attrbList += "&<CL_DedupeCheck>&" +get_ngformValue_template('cmplx_checklist_sp2_dedupe');
	attrbList += "&<CL_TLCheck>&" +get_ngformValue_template('cmplx_checklist_sp2_tl');
	attrbList += "&<CL_AddCheck1>&" +get_ngformValue_template('cmplx_checklist_sp2_add_1');
	attrbList += "&<CL_AddCheck2>&" +get_ngformValue_template('cmplx_checklist_sp2_add_2');
	attrbList += "&<CL_AddCheck3>&"  +get_ngformValue_template('cmplx_checklist_sp2_add_3');
	
	var username = window.parent.userName;
	attrbList += "&<FPU_Grid1>&" + username +'@10';
	attrbList += "&<FPU_Grid2>&" + getNGValue('cmplx_FPU_Grid_case_recived_date')+'@10';
	attrbList += "&<FPU_Grid3>&" + getNGValue('cmplx_FPU_Grid_Realloc_Analyst')+'@10';
	attrbList += "&<FPU_Grid4>&" + getNGValue('cmplx_FPU_Grid_Realloc_recived_date')+'@10';
	attrbList += "&<FPU_Grid5>&" + getNGValue('cmplx_FPU_Grid_CLosure_date')+'@10';
	attrbList += "&<FPU_Grid6>&" + getNGValue('cmplx_FPU_Grid_Status_Application')+'@10';
	attrbList += "&<FPU_Grid7>&" + getNGValue('cmplx_FPU_Grid_Officer_Name')+'@10';
	attrbList += "&<FPU_Grid8>&" + getNGValue('cmplx_FPU_Grid_Officer_closure_date')+'@10';
	attrbList += "&<FPU_Grid9>&" + getNGValue('cmplx_FPU_Grid_Actionable')+'@10';
	attrbList += "&<FPU_Grid10>&" + getNGValue('cmplx_FPU_Grid_case_status')+'@10';

	return attrbList;
}
//function added by Saurabh for FPU Report CR.
function getBankingCheckUpdates(attrList,tag,verifValId,updValId){
	if(getNGValue(verifValId)!='--Select--' && getNGValue(verifValId)!='null' && getNGValue(verifValId)!='undefined'){

			attrList+= "&<"+tag+"_Mismatch>&"   +get_ngformValue_template(verifValId);
			attrList+= "&<"+tag+"_Remark>&" +get_ngformValue_template(updValId);

	}
	else{
		attrList+="&<"+tag+"_Mismatch>&"+' @10';
		attrList+="&<"+tag+"_Remark>&"+' @10';		
	}
	return attrList;
	
}
function getVerificationData(attrList,tag,defValId,verifValId,updValId){
	if(getNGValue(verifValId)!='--Select--' && getNGValue(verifValId)!='null' && getNGValue(verifValId)!='undefined'){
	//------Print values first column----
		if(defValId!='' && getNGValue(defValId)!='' && getNGValue(defValId)!='null' && getNGValue(defValId)!='undefined'){
			if(defValId=='Cust_ver_sp2_Text13' || defValId=='EmploymentVerification_s2_Text1'){
				var wparams="Code=="+getNGValue(defValId);
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+'Designation_CC'+"&wparams="+wparams+"&pname="+'CCTemplateData';
				var desig_Query = CallAjax(jspName,params);
				var fields = desig_Query.split('#');
				attrList+= "&<"+tag+">&"+fields[0]+'@10';
			}
			else{
				attrList+= "&<"+tag+">&"+getNGValue(defValId)+'@10';
			}
		}else {
			attrList+= "&<"+tag+">&"+' @10';

		}
	//---*****-------
	//------Print verification values second column----
	attrList+= "&<"+tag+"_ver>&"+getNGValue(verifValId)+'@10';
	//------Print update values third column----
	if(getNGValue(verifValId)=='Mismatch' || tag=='loan_deduction' || tag=='emp_salary_break'){
		if(getNGValue(updValId)!='' && getNGValue(updValId)!='null' && getNGValue(updValId)!='undefined'){
			if(defValId=='Cust_ver_sp2_Text13' || defValId=='EmploymentVerification_s2_Text1'){
				var wparams="Code=="+getNGValue(updValId);
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+'Designation_CC'+"&wparams="+wparams+"&pname="+'CCTemplateData';
				var desig_Query = CallAjax(jspName,params);
				var fields = desig_Query.split('#');
				attrList+= "&<"+tag+"_upd>&"+fields[0]+'@10';
			}
			else if(defValId=='Cust_ver_sp2_Combo9' ||defValId=='EmploymentVerification_s2_Combo14'){
				var wparams="Code=="+getNGValue(updValId);
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+'EmpStatus'+"&wparams="+wparams+"&pname="+'CCTemplateData';
				var desig_Query = CallAjax(jspName,params);
				var fields = desig_Query.split('#');
				attrList+= "&<"+tag+"_upd>&"+fields[0]+'@10';
			}
			else{
				attrList+= "&<"+tag+"_upd>&"+getNGValue(updValId)+'@10';
			}
		}
		else{
			attrList+="&<"+tag+"_upd>&"+' @10';
		}
	}else{
		attrList+="&<"+tag+"_upd>&"+' @10';
	}
	}
	else {
		attrList+= "&<"+tag+">&"+' @10';
		attrList+="&<"+tag+"_ver>&"+' @10';
		attrList+="&<"+tag+"_upd>&"+' @10';
	 }
	return attrList;
}

function CLTemplateData()
{
	 var attrbList="";
	 var wparams="";
	 var pname="CCTemplateData";
   // var attrbList_arr = "company,compName;company_cifno,cif";
	//var age = "";
	attrbList+=creditcard_check(attrbList,'DecisionHistory_Decision_ListView1');
	attrbList+=creditcard_template(attrbList);
	attrbList+=creditcard_notepad(attrbList);
	attrbList+=creditcard_selectPL(attrbList);
	attrbList+=creditcard_selectCC(attrbList);
	attrbList+=fetch_remarks(attrbList,'DecisionHistory_Decision_ListView1');
	attrbList+=fetch_referTo(attrbList,'DecisionHistory_Decision_ListView1');
	attrbList+=fetch_referTo_se(attrbList,'DecisionHistory_Decision_ListView1');
	//Added by shivang for SE CAM
		attrbList+= getAECBSummary(attrbList);
	if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed' || com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='BTC')//added by prateeka
	{
		attrbList+=getCompanyDetails(attrbList,'cmplx_CompanyDetails_cmplx_CompanyGrid');
	}
	attrbList += "&<CC_Wi_name>&" + get_ngformValue_template('WiLabel');
	attrbList += "&<company>&" + get_ngformValue_template('compName');
    attrbList += "&<company_cifno>&" + get_ngformValue_template('cif');
    attrbList += "&<trade>&" + get_ngformValue_template('TLExpiry');
    attrbList += "&<comapny_lob>&" + get_ngformValue_template('lob');
    attrbList += "&<passport_valid>&" + get_ngformValue_template('AuthorisedSignDetails_PassportExpiryDate');
	attrbList += "&<RM_Name>&" + get_ngformValue_template('RM_Name');
	attrbList += "&<AuthorisedSignDetails_Name>&" + get_ngformValue_template('AuthorisedSignDetails_Name');// added by Prateeka on 20/nov for SE
	attrbList += "&<AuthorisedSignDetails_Name>&" + get_ngformValue_template('AuthorisedSignDetails_Name');// added by Prateeka on 20/nov for SE cmplx_IncomeDetails_netSal1
//attrbList += "&<AppType>&" + get_ngformValue_template('AppType');
	//added by yash on 21 nov 
	 attrbList += "&<BankingDuration>&" + get_ngformValue_template('cmplx_IncomeDetails_DurationOfBanking2');
		attrbList += "&<gender>&" + get_ngformText_template('cmplx_Customer_gender');
	attrbList += "&<MAritalStatus>&" + get_ngformText_template('cmplx_Customer_MAritalStatus');
	//fetching details fro CAM for spring 2.0 by shivang starts 
	//pcasp-2381
	var Query = "FinalFDAmount"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var FinalFDAmount = CallAjax(jspName,params);
	var fields = FinalFDAmount.split('#');
	var fdAmt=fields[0];
	if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){
		fields[0]='';
	}
	attrbList +="&<FDAmount>&" + fields[0];
	
	var Query = "Designation_CC"
	wparams="Code=="+getNGValue('cmplx_EmploymentDetails_DesigVisa');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var designationaspervisa = CallAjax(jspName,params);
	var fields = designationaspervisa.split('#');
	if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){
		fields[0]='';
	}
		attrbList += "&<Designation_As_Per_Visa>&" + fields[0]+'@10';
	var grossSalwithAccomodation = '';	
	grossSalwithAccomodation = getNGValue('cmplx_IncomeDetails_grossSal');
	attrbList += "&<GrossSal>&" + grossSalwithAccomodation + '@10';
	attrbList += "&<AccomodationValue>&" + getNGValue('cmplx_IncomeDetails_Accomodation_Value');
	attrbList += "&<LengthOfBusiness>&" + getNGValue('cmplx_EmploymentDetails_LengthOfBusiness') + '@10';
	attrbList += "&<DectechDecision>&" + getNGValue('cmplx_DEC_DectechDecision') + '@10';
	var mol_resi_sal='';
	var mol_gross_sal='';
	if(getNGValue('cmplx_MOL_resall')!='' && getNGValue('cmplx_MOL_resall')!=null)
	{
		mol_resi_sal = parseFloat(getNGValue('cmplx_MOL_resall').replace(",","")) ;
	}
	else{
		mol_resi_sal = getNGValue('cmplx_MOL_resall');
	}
	if(getNGValue('cmplx_MOL_gross')!='' && getNGValue('cmplx_MOL_gross')!=null)
	{
		mol_gross_sal = parseFloat(getNGValue('cmplx_MOL_gross').replace(",","")) + mol_resi_sal ;
	}
	else{
		mol_gross_sal = getNGValue('cmplx_MOL_gross') + mol_resi_sal;
	}
	attrbList += "&<mol_gross_sal_with_residence>&" + " " +mol_gross_sal + '@10';
	
	var Query = "DectechDecision"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var dectechDecision = CallAjax(jspName,params);
	var fields = dectechDecision.split('#');
	//attrbList += "&<DectechDecision>&" + fields[0]+'@10';
		
	var Query = "DectechDelegation"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var dectechDelegation = CallAjax(jspName,params);
	var fields = dectechDelegation.split('#');
	attrbList += "&<DectechDelegation>&" + fields[0]+'@10';
		
	var Query = "MOB"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var externalMOB = CallAjax(jspName,params);
	var fields = externalMOB.split('#');
	var mob =  fields[0];
	if(mob.indexOf("NODATA")>-1)
	{
		mob='';
	}
	attrbList += "&<ExternalMOB>&" + mob+'@10';
	var Query = "DectechEligibility"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var dectecheligibility = CallAjax(jspName,params);
	var fields = dectecheligibility.split('#');
	dectecheligibility=fields[0];
	if(dectecheligibility.indexOf("NODATA")>-1 || dectecheligibility.indexOf("ERROR")>-1)
	{
		dectecheligibility='';
	}
	attrbList += "&<finalLimit>&" + dectecheligibility+'@10';
	//attrbList += "&<finalLimit>&" + com.newgen.omniforms.formviewer.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit");	
	attrbList += "&<TotalFinalLimit>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalLimit');
	var Query = "TotalObligation"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var totalObligation = CallAjax(jspName,params);
	var fields = totalObligation.split('#');
	var isLiabilityPresent=fields[0];
	var final_Limit='';
	if(getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")!='' || getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")!=0)
	{
		final_Limit = parseFloat(getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")*3/100)<100?100:parseFloat(getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")*3/100);
	}
	if(isLiabilityPresent==0 || isLiabilityPresent=='' || isLiabilityPresent=='0')
	{
		if((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,9))=="Primary@10"){
			
			if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,0)=="Conventional@10")
			{
				var Query = "NumOfCardRequested"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var NumOfCardRequested = CallAjax(jspName,params);
				var fields = NumOfCardRequested.split('#');
				var numOfCardReq=fields[0];
				
				var Query = "IsConventionalSelected"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var NumOfCardRequested = CallAjax(jspName,params);
				var fields = NumOfCardRequested.split('#');
				var isConventionalSelected=fields[0];
				if(numOfCardReq==1 && isConventionalSelected > 0)
				{   
					final_Limit=final_Limit;
				}
				else{
					var Query = "IslamicAmtFetch"
					wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
					var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
					var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
					var IslamicAmtFetch = CallAjax(jspName,params);
					var fields = IslamicAmtFetch.split('#');
					var islamicAmt=fields[0];
					final_Limit= parseFloat(final_Limit) + parseFloat(islamicAmt);
				}
			}
			else
			{							
					var Query = "IsConventionalSelected"
					wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
					var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
					var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
					var NumOfCardRequested = CallAjax(jspName,params);
					var fields = NumOfCardRequested.split('#');
					var isConventionalSelected=fields[0];
				  if(isConventionalSelected>0){
					var Query = "FinalLimitForConventional"
					wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
					var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
					var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
					var FinalConventionalLimit = CallAjax(jspName,params);
					var fields = FinalConventionalLimit.split('#');
					var finalConventionalLimit=fields[0];
					
					var Query = "IslamicAmtFetch"
					wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
					var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
					var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
					var IslamicAmtFetch = CallAjax(jspName,params);
					var fields = IslamicAmtFetch.split('#');
					var islamicAmt=fields[0];
					final_Limit= parseFloat(final_Limit) + parseFloat(finalConventionalLimit) + parseFloat(islamicAmt);
				 	
				 }
				else{
				    var Query = "IslamicAmtFetch"
					wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
					var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
					var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
					var IslamicAmtFetch = CallAjax(jspName,params);
					var fields = IslamicAmtFetch.split('#');
					var islamicAmt=fields[0];
					if(islamicAmt==""){
						islamicAmt=0;
					}
					final_Limit= parseFloat(final_Limit) + parseFloat(islamicAmt);
				}
			}
		}
	}
	else{
				var Query = "OD_LoanEmi"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var OD_LoanEmi = CallAjax(jspName,params);
				var fields = OD_LoanEmi.split('#');
				var odLoanEmi = fields[0];
				if(odLoanEmi>0){}
				else{odLoanEmi=0;}
				var Query = "OD_LoanEmi_IM"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var OD_LoanEmi_im = CallAjax(jspName,params);
				var fields = OD_LoanEmi_im.split('#');
				var OD_LoanEmi_im = fields[0];
				if(OD_LoanEmi_im>0){}
				else{OD_LoanEmi_im=0;}
				var Query = "IslamicAmtFetch"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var IslamicAmtFetch = CallAjax(jspName,params);
				var fields = IslamicAmtFetch.split('#');
				var islamicAmt=fields[0];
				if(islamicAmt>0){}
				else{islamicAmt=0;}
				var Query = "cardLimitCalc"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var cardLimit = CallAjax(jspName,params);
				var fields = cardLimit.split('#');
				var cardLimitCalc=fields[0];
				if(parseFloat(cardLimitCalc)>0){}
				else{cardLimitCalc=0;}
				var Query = "FinalLimitForConventional"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var FinalConventionalLimit = CallAjax(jspName,params);
				var fields = FinalConventionalLimit.split('#');
				var finalConventionalLimit=fields[0];
				if(finalConventionalLimit>0){}
				else{finalConventionalLimit=0;}
				
				var Query = "ExternalLiabilityLimit"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var ExternalLiabilityLimit = CallAjax(jspName,params);
				var fields = ExternalLiabilityLimit.split('#');
				var extLiabilityLimit=fields[0];
				
				var Query = "InternalIslamicLimit"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var InternalIslamicLimit = CallAjax(jspName,params);
				var fields = InternalIslamicLimit.split('#');
				var intIslLimit=0;
				
				if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){
				intIslLimit=0;
				}
				else{ 
				intIslLimit=fields[0];
				}
				
				var Query = "InternalConvLimit"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var InternalConvLimit = CallAjax(jspName,params);
				var fields = InternalConvLimit.split('#');
				var intConvLimit=0;
				if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){ 
				intConvLimit=0;
				}
				else{ 
				intConvLimit=fields[0];
				}
				
		if((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,9))=="Primary@10"){
			
			if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,0)=="Conventional@10")
			{
				var Query = "NumOfCardRequested"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var NumOfCardRequested = CallAjax(jspName,params);
				var fields = NumOfCardRequested.split('#');
				var numOfCardReq=fields[0];
				
				var Query = "IsConventionalSelected"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var NumOfCardRequested = CallAjax(jspName,params);
				var fields = NumOfCardRequested.split('#');
				var isConventionalSelected=fields[0];
								
				/*if(numOfCardReq>0 && isConventionalSelected > 0)
				{   
					final_Limit= parseFloat(final_Limit) + parseFloat(odLoanEmi) + parseFloat(cardLimitCalc);
				}
				else{
					 
					final_Limit= parseFloat(final_Limit) + parseFloat(cardLimitCalc) + parseFloat(odLoanEmi);
				}*/
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='IM'){
					final_Limit= parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_EMI')) + parseFloat(intConvLimit) + parseFloat(extLiabilityLimit)+ parseFloat(OD_LoanEmi_im);
				}else{
				final_Limit= parseFloat(final_Limit) + parseFloat(odLoanEmi) + parseFloat(extLiabilityLimit)+parseFloat(intIslLimit);
				}
			}
			else
			{		
					/*if(finalConventionalLimit!=0){
					finalConventionalLimit = finalConventionalLimit *3/100;	
					}
					if(finalConventionalLimit<100)
					{
						finalConventionalLimit=100;
					}*/
					
				   final_Limit=parseFloat(final_Limit)+ parseFloat(odLoanEmi)+ parseFloat(extLiabilityLimit)+parseFloat(intIslLimit)+ parseFloat(intConvLimit);
			}
		 }
	}
	
	attrbList += "&<TotalObligation>&" + final_Limit+'@10';
		
	var Query = "AECBHistory"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var aecbHistmonthCount = CallAjax(jspName,params);
	var fields = aecbHistmonthCount.split('#');
	attrbList += "&<AECBHistoryCount>&" + fields[0]+'@10';
	
	//changes done by shivang for 2.1
	attrbList += "&<AECBScore>&" + com.newgen.omniforms.formviewer.getNGValue('cmplx_Liability_New_AECBScore');
	attrbList += "&<Range>&" + com.newgen.omniforms.formviewer.getNGValue('cmplx_Liability_New_Range');
	attrbList += "&<ReferenceNo>&" + com.newgen.omniforms.formviewer.getNGValue('cmplx_Liability_New_ReferenceNo');
	
	var Query = "EligibleCardLimit"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var eligibleLimit = CallAjax(jspName,params);
	var fields = eligibleLimit.split('#');
	attrbList += "&<eligibleLimit>&" + fields[0]+'@10';
	
	var username = window.parent.userName;
	attrbList += "&<Reviewed_By>&" + username +'@10';
	//fetching designation as per visa by shivang end 
	//Adding for Pre-Approved CAM
    var Query = "WorstD24"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var worstD24 = CallAjax(jspName,params);
	var fields = worstD24.split('#');
	var worstPaymentDelay24= fields[0];
	if(worstPaymentDelay24.indexOf("NODATA")>-1 || worstPaymentDelay24=='' || worstPaymentDelay24=="ERROR"){
		worstPaymentDelay24='0';
	}
	attrbList += "&<worstPaymentDelay24>&" + worstPaymentDelay24+'@10';
	var propertyValue='';
	  var applType=get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4);
	   if(applType=="MOR" || applType=="MOR@10"){
		 attrbList += "&<mortgageLoan>&" + "Yes"+"@10";
		 //attrbList += "&<mortgageValue>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4) +"@10";
		 attrbList += "&<rakSelect>&" + " "+"@10";
		 attrbList += "&<pbd>&" + " "+"@10";
		 attrbList += "&<rakElite>&" + " "+"@10";
		 var Query = "MortgageValue";
	     wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		 var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		 var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		 var mortgageValue = CallAjax(jspName,params);
		 var fields = mortgageValue.split('#');
		 propertyValue = fields[0];
		 if(propertyValue.indexOf("NODATA")>-1 || propertyValue.indexOf("ERROR")>-1){
			propertyValue='0'; 
		 }
		 attrbList += "&<propertyValue>&" + propertyValue+'@10';
	   }
	   else if(applType=="RSEL" || applType=="RSEL@10"){
		 attrbList += "&<mortgageLoan>&" + " "+"@10";
		 attrbList += "&<mortgageValue>&" + "0" +"@10";
		 attrbList += "&<rakSelect>&" + "Yes"+"@10";
		 attrbList += "&<pbd>&" + " "+"@10";
		 attrbList += "&<rakElite>&" + " "+"@10";
		 attrbList += "&<propertyValue>&" + propertyValue+'@10';
	   }
	   else if(applType=="RELT" || applType=="RELT@10" || applType=="RELTN" || applType=="RELTN@10"){
		 attrbList += "&<mortgageLoan>&" + " "+"@10";
		 attrbList += "&<rakSelect>&" + " "+"@10";
		 attrbList += "&<pbd>&" + " "+"@10";
		 attrbList += "&<rakElite>&" + "Yes"+"@10";
		 var Query = "RakEliteValue";
	     wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		 var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		 var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		 var mortgageValue = CallAjax(jspName,params);
		 var fields = mortgageValue.split('#');
		 propertyValue = fields[0]
		 if(propertyValue.indexOf("NODATA")>-1 || propertyValue.indexOf("ERROR")>-1){
			propertyValue='0'; 
		 }
		 attrbList += "&<propertyValue>&" + propertyValue+'@10';
	   }
	   else if(applType=="PBD" || applType=="PBD@10"){
		 attrbList += "&<mortgageLoan>&" + " "+"@10";
		 attrbList += "&<mortgageValue>&" + "0" +"@10";
		 attrbList += "&<rakSelect>&" + " "+"@10";
		 attrbList += "&<pbd>&" + "Yes"+"@10";
		 attrbList += "&<rakElite>&" + " "+"@10";
		 attrbList += "&<propertyValue>&" + propertyValue+'@10';
	   }
	   else{
		 attrbList += "&<mortgageLoan>&" + " "+"@10";
		 attrbList += "&<mortgageValue>&" + "0" +"@10";
		 attrbList += "&<rakSelect>&" + " "+"@10";
		 attrbList += "&<pbd>&" + " "+"@10";
		 attrbList += "&<rakElite>&" + " "+"@10";
		 attrbList += "&<propertyValue>&" + propertyValue+'@10';
	   }
	   attrbList += "&<CRG_Code>&" + get_ngformGridColumn_template("cmplx_FinacleCore_FinaclecoreGrid",0,10);//PCASP-2834
	   attrbList += "&<Segment>&" + "NA"+"@10";//get_ngformGridColumn_template("cmplx_FinacleCore_FinaclecoreGrid",0,14);//PCASP-2834
	   
	    var propValueSE='';
	    var Query = "PropValueSE";
		wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var val = CallAjax(jspName,params);
		var fields = val.split('#');
		 propValueSE = fields[0];
		if(propValueSE.indexOf("NODATA")>-1 || propValueSE.indexOf("ERROR")>-1)
		{
			propValueSE='';
		}
	   attrbList += "&<propValueSE>&" + propValueSE +'@10';//PCASP-2834
		//Average Balance(3 month) calculation
        var avgBalance3='';
	    var Query = "AvgBal3Month";
		wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var avg = CallAjax(jspName,params);
		var fields = avg.split('#');
		 avgBalance3 = fields[0];
		if(avgBalance3.indexOf("NODATA")>-1 || avgBalance3.indexOf("ERROR")>-1)
		{
			avgBalance3='';
		}
       /*if(getNGValue("cmplx_IncomeDetails_AvgBalFreq")=="Quarterly")
	   {
		 avgBalance3 = getNGValue("cmplx_IncomeDetails_AvgBal");
	   }
	   else if(getNGValue("cmplx_IncomeDetails_AvgBalFreq")=="Annually" && (getNGValue("cmplx_IncomeDetails_AvgBal")!="" && getNGValue("cmplx_IncomeDetails_AvgBal")!="@10" && getNGValue("cmplx_IncomeDetails_AvgBal")!=null))
       {
		 avgBalance3 = (parseFloat(getNGValue("cmplx_IncomeDetails_AvgBal")))/4;
       }
	   else if(getNGValue("cmplx_IncomeDetails_AvgBalFreq")=="Half Yearly" && (getNGValue("cmplx_IncomeDetails_AvgBal")!="" && getNGValue("cmplx_IncomeDetails_AvgBal")!="@10" && getNGValue("cmplx_IncomeDetails_AvgBal")!=null))
       {
		 avgBalance3 = (parseFloat(getNGValue("cmplx_IncomeDetails_AvgBal")))/2;
       }
	   else if(getNGValue("cmplx_IncomeDetails_AvgBalFreq")=="Monthly" && (getNGValue("cmplx_IncomeDetails_AvgBal")!="" && getNGValue("cmplx_IncomeDetails_AvgBal")!="@10" && getNGValue("cmplx_IncomeDetails_AvgBal")!=null))
       {
		 avgBalance3 = (parseFloat(getNGValue("cmplx_IncomeDetails_AvgBal")))*3;
       }
	   else if(getNGValue("cmplx_IncomeDetails_AvgBalFreq")=="Weekly" && (getNGValue("cmplx_IncomeDetails_AvgBal")!="" && getNGValue("cmplx_IncomeDetails_AvgBal")!="@10" && getNGValue("cmplx_IncomeDetails_AvgBal")!=null))
       {
		 avgBalance3 = (parseFloat(getNGValue("cmplx_IncomeDetails_AvgBal")))*12;
       }
	   else{
		   avgBalance3='0';
	   }*/
	   
        attrbList += "&<avgBalance3>&" + avgBalance3 + "@10";

        //Total Credit Turnover (last 3 months) calculation
	    var totCrTurnover3='';
	    var Query = "TotalCr3Month";
		wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var avg = CallAjax(jspName,params);
		var fields = avg.split('#');
		 totCrTurnover3 = fields[0];
		 if(totCrTurnover3.indexOf("NODATA")>-1 || totCrTurnover3.indexOf("ERROR")>-1)
		{
			totCrTurnover3='';
		}
	   /*if(getNGValue("cmplx_IncomeDetails_CreditTurnoverFreq")=="Quarterly")
	   {
		totCrTurnover3 = getNGValue("cmplx_IncomeDetails_CredTurnover");
	   }
	   else if(getNGValue("cmplx_IncomeDetails_CreditTurnoverFreq")=="Annually" && (getNGValue("cmplx_IncomeDetails_CredTurnover")!="" && getNGValue("cmplx_IncomeDetails_CredTurnover")!="@10" && getNGValue("cmplx_IncomeDetails_CredTurnover")!=null))
	   {
		totCrTurnover3 = (parseFloat(getNGValue("cmplx_IncomeDetails_CredTurnover")))/4;
	   }
	   else if(getNGValue("cmplx_IncomeDetails_CreditTurnoverFreq")=="Half Yearly" && (getNGValue("cmplx_IncomeDetails_CredTurnover")!="" && getNGValue("cmplx_IncomeDetails_CredTurnover")!="@10" && getNGValue("cmplx_IncomeDetails_CredTurnover")!=null))
	   {
		 totCrTurnover3 = (parseFloat(getNGValue("cmplx_IncomeDetails_CredTurnover")))/2;
	   }
	   else if(getNGValue("cmplx_IncomeDetails_CreditTurnoverFreq")=="Monthly" && (getNGValue("cmplx_IncomeDetails_CredTurnover")!="" && getNGValue("cmplx_IncomeDetails_CredTurnover")!="@10" && getNGValue("cmplx_IncomeDetails_CredTurnover")!=null))
	   {
	     totCrTurnover3 = (parseFloat(getNGValue("cmplx_IncomeDetails_CredTurnover")))*3;
       }
	   else if(getNGValue("cmplx_IncomeDetails_CreditTurnoverFreq")=="Weekly" && (getNGValue("cmplx_IncomeDetails_CredTurnover")!="" && getNGValue("cmplx_IncomeDetails_CredTurnover")!="@10" && getNGValue("cmplx_IncomeDetails_CredTurnover")!=null))
	   {
	     totCrTurnover3 = (parseFloat(getNGValue("cmplx_IncomeDetails_CredTurnover")))*12;
       }
	    else{
				totCrTurnover3='0';
		}*/
	   attrbList += "&<TotalCrTO3>&" + totCrTurnover3 + "@10";
	  
	var Query = "TotalAUM";
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var aum = CallAjax(jspName,params);
	var fields = aum.split('#');
	var totalAum = fields[0];
	if(totalAum.indexOf("NODATA")>-1 || totalAum.indexOf("ERROR")>-1)
		{
			totalAum='';
		}
		attrbList += "&<TotalAum>&" + totalAum + "@10";
		
		var Query = "AuthorizedSign";
		wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var AuthorizedSign = CallAjax(jspName,params);
		var fields = AuthorizedSign.split('#');
		var IsAuthorizedSign = fields[0];
		if(IsAuthorizedSign.indexOf("NODATA")>-1 || IsAuthorizedSign.indexOf("ERROR")>-1)
		{
			totalAum='';
		}
		attrbList += "&<AuthorizedSign>&" + IsAuthorizedSign + "@10";
		
	var Query = "Nationality_CC";
	wparams="Code=="+getNGValue('cmplx_Customer_Nationality');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var nat_Query = CallAjax(jspName,params);
	var fields = nat_Query.split('#');
	attrbList += "&<Nationality>&" + fields[0]+'@10';
		attrbList += "&<National>&" + get_ngformText_template('cmplx_Customer_GCCNational');
		attrbList += "&<SalaryDay>&" + get_ngformValue_template('cmplx_IncomeDetails_SalaryDay');
		attrbList += "&<Salary_transfer>&" + get_ngformText_template('cmplx_IncomeDetails_SalaryXferToBank');
		attrbList += "&<EmpStatus>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatus');
		//changes for PCSP 23 by saurbh on 8th Jan
		attrbList += "&<Accprovided>&" + get_ngformValue_template('cmplx_IncomeDetails_Accomodation');
		attrbList += "&<Confirmed>&" + get_ngformValue_template('cmplx_EmploymentDetails_JobConfirmed');
		attrbList += "&<Movement>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_empmovemnt'):" "+"@10");
	Query = "Designation_CC";
	wparams="Code=="+getNGValue('cmplx_EmploymentDetails_Designation');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var desig_Query = CallAjax(jspName,params);
	var fields = desig_Query.split('#');
		attrbList += "&<Cuurent_Designation>&" + fields[0]+'@10';
		//attrbList += "&<EmpType>&" + get_ngformText_template('cmplx_EmploymentDetails_Emp_Type');

		// changes done by disha for Generate Template on 17-10-18 start
		attrbList += "&<Customer_cifno>&" + get_ngformValue_template('cmplx_Customer_CIFNo');
		attrbList += "&<EmpName>&" + get_ngformValue_template('cmplx_EmploymentDetails_EmpName');
		attrbList += "&<EMpCode>&" + get_ngformValue_template('cmplx_EmploymentDetails_EMpCode');
		attrbList += "&<VisaSponser>&" + get_ngformValue_template('cmplx_EmploymentDetails_VisaSponser');
		attrbList += "&<LOS>&" + get_ngformValue_template('cmplx_EmploymentDetails_LOS');
		attrbList += "&<FinalDBR>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalDBR');
		attrbList += "&<FinalTAI>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalTai');
		//changes by saurabh for PCSP-23 on 7 Feb 19.
		attrbList += "&<Entry_Date>&" + get_ngformValue_template('Created_Date');
		//PCASP-2833
		if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')!='Self Employed'){
			attrbList += "&<Account_Number>&" + get_ngformValue_template('Account_Number');
		}else{
		var Query = "AccountNoforCustomer";
			var wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var AcctNo = CallAjax(jspName,params);
			var fields = AcctNo.split('#');
			var acctnumber = fields[0];
			if(acctnumber.indexOf("NODATA")>-1 || acctnumber.indexOf("ERROR")>-1){
				acctnumber='0';
			}
			attrbList += "&<Account_Number>&" + acctnumber + '@10';
		}

		//attrbList += "&<Account_Number>&" + get_ngformValue_template('Account_Number');
	// changes done by disha for Generate Techanges done by disha for Generate Template on 17-10-18 end
		attrbList += "&<Emp_Type>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6),get_ngform_controlObject('EmpType'));
		if(com.newgen.omniforms.formviewer.isVisible('cmplx_EmploymentDetails_tierno'))//sagarika on 24-02-20
		{
		attrbList += "&<targetSegCode>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1||get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Pensioner')>-1)?get_ngformText_template('cmplx_EmploymentDetails_targetSegCode'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,22),get_ngform_controlObject('TargetSegmentCode')))+" -"+get_ngformText_template('cmplx_EmploymentDetails_tierno');
		}
		else{
		attrbList += "&<targetSegCode>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1||get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Pensioner')>-1)?get_ngformText_template('cmplx_EmploymentDetails_targetSegCode'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,22),get_ngform_controlObject('TargetSegmentCode')));
		}
		
		/*attrbList += "&<marketcode>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_marketcode'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,28),get_ngform_controlObject('MarketingCode')));*/
		attrbList += "&<marketcode>&" + get_ngformText_template('cmplx_EmploymentDetails_marketcode');
		attrbList += "&<Top_UP>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_NetPayout');
		attrbList += "&<Micro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Micro');
		attrbList += "&<Macro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Macro');
		attrbList += "&<Strength>&" + get_ngformText_template_dev('cmplx_DEC_Strength');
		attrbList += "&<Weakness>&" + get_ngformText_template_dev('cmplx_DEC_Weakness');
		attrbList += "&<Maturity_Age>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_AgeAtMaturity');
		attrbList += "&<Total_Exposure>&" + get_ngformValue_template('cmplx_Liability_New_AggrExposure');

		attrbList += "&<PLST>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_EmpStatusPL'):" "+"@10");
		attrbList += "&<PLE>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_Status_PLExpact'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,35),get_ngform_controlObject('EmployerStatusPLExpact')));
		attrbList += "&<PLN>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_Status_PLNational'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,36),get_ngform_controlObject('EmployerStatusPLNational')));
		attrbList += "&<EmpStatus_PL>&" + get_ngformText_template("cmplx_EmploymentDetails_EmpStatusPL");
		/*attrbList += "&<PLCAT>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,33),get_ngform_controlObject('EmployerCategoryPL')));*/
		attrbList += "&<PLCAT>&" + get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer');
		/*attrbList += "&<PLCATC>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_EmpStatusCC'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,34),get_ngform_controlObject('EmployerStatusCC')));*/
		attrbList += "&<PLCATC>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatusCC');
		/*attrbList += "&<ALOC_CC>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_remarks'):" "+"@10");*/
		var aloc_remark_CC='';
		aloc_remark_CC=get_ngformValue_template('cmplx_EmploymentDetails_remarks');
		if(aloc_remark_CC=="@10" || aloc_remark_CC == "" || aloc_remark_CC==null){
			attrbList += "&<ALOC_CC>&" + "No@10";
		}else{
			attrbList += "&<ALOC_CC>&" + "Yes@10";
		}
	
		//change by saurabh for PCSP-315
		attrbList += "&<ALOC_PL>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_Aloc_RemarksPL'):" "+"@10");
		var aloc_remark=''
		aloc_remark= ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_Aloc_RemarksPL'):" "+"@10");
		if(aloc_remark=="@10" || aloc_remark == "" || aloc_remark==null){
			attrbList += "&<ALOC_PL>&" + "No@10";
		}else{
			attrbList += "&<ALOC_PL>&" + "Yes@10";
		}
		//Added by shivang for PCASP-2954
		if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed')
		{
			attrbList += "&<ALOC_CC>&" + "NA@10";
			attrbList += "&<ALOC_PL>&" + "NA@10";
		}
		attrbList += "&<High>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_highdelinq'):get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,32));
		
		attrbList += "&<NepType>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_NepType'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,23),get_ngform_controlObject('NepType')));
		attrbList += "&<Resident>&" + get_ngformText_template('cmplx_Customer_RESIDENTNONRESIDENT');
		//PCASP-2995
		attrbList += "&<Age>&" + calcAge(getNGValue("cmplx_Customer_DOb"),'');
		//attrbList += "&<Age>&" + get_ngformValue_template('cmplx_Customer_Age');
		attrbList += "&<CROPS>&"+getCropsNotepad('NDISB') +"@10";
		attrbList += "&<Bank_Name>&" +" "+"@10";
		attrbList += "&<Acc_No>&" +" "+"@10";
		attrbList += "&<AvgCR33>&" + get_ngformText_template('cmplx_FinacleCore_avg_credit_3month');
		attrbList += "&<avgbal3>&" + get_ngformText_template('cmplx_FinacleCore_total_avg_last13');
		attrbList += "&<AvgCR6>&" + get_ngformText_template('cmplx_FinacleCore_avg_credit_6month');
		attrbList += "&<avgbal6>&" + get_ngformText_template('cmplx_FinacleCore_total_avg_last_16');
		attrbList += "&<Collection>&" + get_ngformText_template('cmplx_EmploymentDetails_Collectioncode');
		attrbList += "&<Classification>&" + get_ngformText_template('cmplx_EmploymentDetails_ClassificationCode');
		attrbList += "&<FieldVisitedDone>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_FieldVisitDone'):" "+"@10");
		attrbList += "&<LoanMultiple>&" + calcLoanMultiple();
		//Added by shivang for PA CAM
		var bank_Name='';
		var bank_From='';
		var bank_To='';
		for(var i=0;i< getLVWRowCount('cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails');i++){
			bank_Name = get_ngformGridColumn_template("cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails",i,0);	
			bank_From = get_ngformGridColumn_template("cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails",i,1);
			bank_To = get_ngformGridColumn_template("cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails",i,2);	
		}
		if(getNGValue("cmplx_IncomeDetails_SalaryXferToBank")=="Y" || getNGValue("cmplx_IncomeDetails_SalaryXferToBank")=="Y@10"){
			bank_Name='';
		}
		attrbList += "&<bank_Name>&" + bank_Name;		
		attrbList += "&<Bank_From>&" + bank_From;
		attrbList += "&<Bank_To>&" + bank_To;		
		//Added by tarang on 03/04/2018
		attrbList += "&<MIS>&" + get_ngformText_template('cmplx_EmploymentDetails_MIS');
attrbList += "&<Channel>&" + get_ngformText_template('cmplx_EmploymentDetails_channelcode');
attrbList += "&<Promo>&" + get_ngformText_template('cmplx_EmploymentDetails_PromotionCode');
attrbList += "&<High>&" + get_ngformText_template('cmplx_EmploymentDetails_highdelinq');
attrbList += "&<source_code>&" + get_ngformValue_template('InitChannelLabel');

attrbList += "&<Net_Salary>&"+get_ngformValue_template('cmplx_IncomeDetails_AvgNetSal');
attrbList += "&<applicant_cat>&" + get_ngformText_template('cmplx_Customer_CustomerCategory');
//changed by nikhil for PCSP-912 updated by nikhil
attrbList += "&<application_cat>&" + get_ngformText_template('cmplx_EmploymentDetails_ApplicationCateg');
attrbList += "&<ECRN>&" + get_ngformValue_template('ECRN');
var crnNumber='';
for(var i=0; i< getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
{
	if(getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails")==1){
		crnNumber = get_ngformGridColumn_template("cmplx_CardDetails_cmplx_CardCRNDetails",0,2);
	}
	else{
		crnNumber = crnNumber + get_ngformGridColumn_template("cmplx_CardDetails_cmplx_CardCRNDetails",i,2) + ",";
	}
	
}
//Changed by Rajan for PCASP-2315
if(crnNumber.charAt(crnNumber.length-1)==',')
{
	crnNumber=crnNumber.substring(0,crnNumber.length-1);
}
attrbList += "&<CRN>&" + crnNumber;
attrbList += "&<customer_name>&" + get_ngformValue_template('CustomerLabel');
/*attrbList += "&<Current_date_time>&" + get_ngformValue_template('CurrentDateLabel')+ ' '+new Date().getHours()+':'+new Date().getMinutes();*/
attrbList += "&<Current_date_time>&" + get_ngformValue_template('CurrentDateLabel')+ ' '+ addZero(new Date().getHours())+ ':'+ addZero(new Date().getMinutes());
if(get_ngformValue_template('cmplx_Liability_New_DBRNet')=="@10")
{
	attrbList += "&<DBRNET>&" + "0.00@10";
}
else{
attrbList += "&<DBRNET>&" + get_ngformValue_template('cmplx_Liability_New_DBRNet');
}
//below line commented by saurabh for PCSP-248 on 24th Dec
//attrbList += "&<FinalDBR>&" + get_ngformValue_template('cmplx_Liability_New_DBR');

attrbList += "&<TAI>&" + get_ngformValue_template('cmplx_Liability_New_TAI');

attrbList += "&<InsuranceAmount>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_InsuranceAmount');

attrbList += "&<EMI>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_EMI');
//attrbList += "&<Dec_Remarks>&" + get_ngformValue_template('cmplx_DEC_Remarks');
attrbList += "&<ReferTo>&" + get_ngformValue_template('ReferTo');
//attrbList += "&<ReferReason>&" + get_ngformText_template('DecisionHistory_dec_reason_code');
//attrbList += "&<Decision>&" + get_ngformValue_template('cmplx_DEC_Decision');

attrbList += "&<existing>&" + (get_ngformValue_template('cmplx_Customer_NTB').split('@')[0]=='NO'?'YES@10':'NO@10');
//add today
attrbList += "&<Tenor>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_Tenor');
attrbList += "&<InterestRate>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_InterestRate');
attrbList += "&<MOL_Var>&" + get_ngformValue_template('cmplx_MOL_molsalvar');
attrbList += "&<IMAMOUNT>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalLimit');
if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,2)=='IM'){
attrbList += "&<FirstRepayDate>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FirstRepayDate');
attrbList += "&<Last_Repay>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_MaturityDate');
}
else{
attrbList += "&<FirstRepayDate>&@10";
attrbList += "&<Last_Repay>&@10";
}

//change by saurabh on 4 Feb 19 for PCSP-898
Query = "CardType";
	wparams="WiName=="+window.parent.pid;
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var desig_Query = CallAjax(jspName,params);
	var fields = desig_Query.split('#');
		attrbList += "&<Card_Product>&" + fields[0]+'@10';
		attrbList += "&<Card_prod>&" + fields[0]+'@10';
//attrbList += "&<Card_prod>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,5),get_ngform_controlObject('CardProd'));
attrbList += "&<score_grade>&" + get_ngformValue_template('cmplx_DEC_ScoreGrade');
attrbList += "&<Doc_name>&" + get_ngformValue_template('DocName');
attrbList += "&<Doc_Sta>&" + get_ngformValue_template('DocSta');
if(get_ngformValue_template('cmplx_EmploymentDetails_IncInPL')=="YES@10" || get_ngformValue_template('cmplx_EmploymentDetails_IncInPL')=="Yes@10")
{
	attrbList += "&<IncInPL>&" + "Yes@10";
}else{
	attrbList += "&<IncInPL>&" + "No@10";
}
if(get_ngformValue_template('cmplx_EmploymentDetails_IncInCC')=="YES@10" || get_ngformValue_template('cmplx_EmploymentDetails_IncInCC')=="Yes@10")
{
	attrbList += "&<IncInCC>&" + "Yes@10";
}else{
	attrbList += "&<IncInCC>&" + "No@10";
}

attrbList += "&<Deferred>&" + get_ngformValue_template('DeferredUntilDate');
attrbList += "&<AECB_SCORE>&" + get_ngformValue_template('cmplx_Liability_New_AECBScore');// ADDED BY Prateeka
attrbList += "&<Processing>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_LPF');
    //attrbList += "&<Scheme>&" + get_ngformValue_template('Scheme');
 attrbList += "&<company_name>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4);
 attrbList += "&<Applicant_Category>&" + get_ngformText_template('cmplx_Customer_CustomerCategory');
 attrbList += "&<Employer_Category>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,33),get_ngform_controlObject('EmployerCategoryPL')));
 attrbList += "&<comp_CIF>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,3);
 attrbList += "&<TL_Expiry>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,7);
  attrbList += "&<Indus_sector>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,8);
   //attrbList += "&<application>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",0,4);
    attrbList += "&<LOB>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,15);
	//attrbList += "&<DectechDecision>&" + get_ngformValue_template('cmplx_DEC_DectechDecision');
	attrbList += "&<Product_type>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,0);
	attrbList += "&<requested_limit>&" + get_ngformValue_template("cmplx_EligibilityAndProductInfo_FinalLimit");

	attrbList += "&<subproduct>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,2),get_ngform_controlObject('subProd'));
	attrbList += "&<requested_product>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,1);
	attrbList += "&<applicant_type>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4),get_ngform_controlObject('AppType'));	

	//attrbList += "&<applicant_type>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4);
	//attrbList += "&<Card_Product>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,5),get_ngform_controlObject('CardProd'));
	attrbList += "&<Scheme>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,8);
	//attrbList += "&<WORSTL24>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,17),get_ngform_controlObject('Liability_New_worstStatusInLast24'));
	
	attrbList += "&<WORSTL24>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?" "+"@10":get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,17));
	

	attrbList += "&<TOTAL>&" + get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,12);
	attrbList += "&<AECB>&" + get_ngformValue_template('AECBHistMonthCnt');
	attrbList += "&<WORSTD24>&" + get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,13);
	attrbList += "&<authorize_name>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,2);
	attrbList += "&<auth_passex>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,9);
	attrbList += "&<auth_cif>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,1);
	attrbList += "&<auth_dob>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,4);
	attrbList += "&<auth_nat>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,3);
	attrbList += "&<auth_visaex>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,6);
	attrbList += "&<auth_visadob>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,4);
	attrbList += "&<loan_limit>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,3);
	//attrbList += "&<loan_limit>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,3);
	//var age = getAge($('#auth_visadob').val());
		var Query = "PLE";
		//"SELECT top 1 isnull(csmNext,'') as csmNext,isnull(userName,'') as userName FROM NG_RLOS_GR_DECISION with (nolock) WHERE dec_wi_Name ='"+ com.newgen.omniforms.formviewer.getNGValue('WiLabel')+"' order by csmNext";
		wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var csmNext_Result = CallAjax(jspName,params);
		var act_name=window.parent.stractivityName;
		var username=window.parent.userName;
		if(act_name=='Cad_Analyst2')
		{
		var level=com.newgen.omniforms.formviewer.getNGValue('cadNExt');
		csmNext_Result=csmNext_Result+"~Cad_Analyst2#"+username+"#"+level+"#~";
		}
		
		//alert(csmNext_Result);
		var rem1='',rem2='',rem3='',rem4='',rem5='',rem6='',rem7='',rem8='',rem9='',rem10='';
		var rows = csmNext_Result.trim().split('~');
		for(var i=0;i<rows.length-1;i++){
			var workstep=rows[i].split('#')[0].trim();
			var username=rows[i].split('#')[1];
			var cadlevel=rows[i].split('#')[2];
			if(workstep=='CAD_Analyst1'){
				rem1=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L1'){
				rem6=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L2'){
				rem6=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L3'){
				rem7=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L4'){
				rem3=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L5'){
				rem8=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L6'){
				rem4=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L7'){
				rem9=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='Ln'){
				rem5=username;
				rem10=username;
			}
		}
		/*var csmNext = fields[0]+"@10";
		var userName = fields[1]+"@10";*/
		if(act_name=='CAD_Analyst1')
		{
		username=window.parent.userName;
		}
		if(act_name=='CAD_Analyst1')
		{
		rem1=username;
		}
		attrbList += "&<Remarks_1>&"+rem1+"@10";
		attrbList += "&<Remarks_2>&"+rem2+"@10";
		attrbList += "&<Remarks_3>&"+rem3+"@10";
		attrbList += "&<Remarks_4>&"+rem4+"@10";
		attrbList += "&<Remarks_5>&"+rem5+"@10";
		attrbList += "&<Remarks_6>&"+rem6+"@10";
		attrbList += "&<Remarks_7>&"+rem7+"@10";
		attrbList += "&<Remarks_8>&"+rem8+"@10";
		attrbList += "&<Remarks_9>&"+rem9+"@10";
		attrbList += "&<Remarks_10>&"+rem10+"@10";
		// Added by shivang for getting Bundled product
		attrbList += "&<Bundled_Product>&"+ get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4);
		var Query = "DisbursalDate"
		wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var disbursalDate = CallAjax(jspName,params);
		var fields = disbursalDate.split('#');
		var disbDate =  fields[0];
		
		if(disbDate.indexOf("NODATA")>-1 || disbDate.indexOf("ERROR")>-1)
		{
			disbDate='';
		}
		var loanAmt='';
		
		if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='MOR'))
		 {
			var Query = "LoanAmount_MOR";
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var loanamount = CallAjax(jspName,params);
			var fields = loanamount.split('#');
			loanAmt = fields[0];
			attrbList += "&<noRepayments>&" + "NA"+'@10';
			attrbList += "&<balTenor>&" + "NA"+'@10';	
		}
		 else  if(((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='Pre-Approved') || (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RFL' || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RFLP' ))
		 {
			 var Query = "LoanAmount_RFL";
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var loanamount = CallAjax(jspName,params);
				var fields = loanamount.split('#');		
				loanAmt = fields[0];		
				var remaiInstalments = '';
				var totinstalments = '';
				if(fields.length>1 && !(loanAmt.indexOf("NODATA")>-1)){
				remaiInstalments = fields[1];
				totinstalments = fields[2];
				if(remaiInstalments.indexOf("NODATA")>-1 || remaiInstalments.indexOf("ERROR")>-1)
				{
					remaiInstalments=0;
				}
				if(totinstalments.indexOf("NODATA")>-1 || totinstalments.indexOf("ERROR")>-1)
				{
					totinstalments=0;
				} else {
					totinstalments= totinstalments-remaiInstalments;
				}
				}
				attrbList += "&<noRepayments>&" + totinstalments+'@10';
				attrbList += "&<balTenor>&" + remaiInstalments+'@10';
				
		} else {
			var Query = "LoanAmount";
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var loanamount = CallAjax(jspName,params);
			var fields = loanamount.split('#');
			loanAmt = fields[0];

		}
		if(loanAmt.indexOf("NODATA")>-1 || loanAmt.indexOf("ERROR")>-1)
		{
			loanAmt='';
		}
		 
		attrbList += "&<Loan_Amount>&" + loanAmt+'@10';
		attrbList += "&<Disbursal_Date>&" + disbDate+'@10';
		
		/*if (csmNext=='L1@10')
		{
			attrbList += "&<Remarks_1>&" + userName;
		}
		else if (csmNext=='L2@10')
		{
			attrbList += "&<Remarks_2>&" + userName;
		}
		else if (csmNext=='L3@10')
		{
			attrbList += "&<Remarks_3>&" + userName;
		}
		else if (csmNext=='L4@10')
		{
			attrbList += "&<Remarks_4>&" + userName;
		}
		else if (csmNext=='L5@10')
		{
			attrbList += "&<Remarks_5>&" + userName;
		}
		else if (csmNext=='L6@10')
		{
			attrbList += "&<Remarks_6>&" + userName;
		}
		else if (csmNext=='L7@10')
		{
			attrbList += "&<Remarks_7>&" + userName;
		}
			else if (csmNext=='L8@10')
		{
			attrbList += "&<Remarks_8>&" + userName;
		}
			else if (csmNext=='L9@10')
		{
			attrbList += "&<Remarks_9>&" + userName;
		}
			else if (csmNext=='L10@10')
		{
			attrbList += "&<Remarks_10>&" + userName;
		}
		else{
		}*/

    return attrbList;
}
//Added by Shivang for FALCON CAM

function FalconTemplateData(){
	var attrbList="";
	 attrbList+=CLTemplateData();
	 attrbList += "&<PortalRefNo>&" +getNGValue('PORTAL_REF_NUMBER');
	 attrbList += "&<NetSal1>&" +getNGValue('cmplx_IncomeDetails_netSal1') + '@10';
	 attrbList += "&<NetSal2>&" +getNGValue('cmplx_IncomeDetails_netSal2') + '@10';
	 attrbList += "&<NetSal3>&" +getNGValue('cmplx_IncomeDetails_netSal3') + '@10';
	 attrbList += "&<PortalApprovedLimit>&" +getNGValue('cmplx_EligibilityAndProductInfo_PortalApprovedLimit') + '@10';
	 if(getNGValue('cpv')==''){
		 attrbList += "&<Is_CPV_Done>&" +'No' + '@10';
	 }else{
		 attrbList += "&<Is_CPV_Done>&" + getNGValue('cpv') + '@10';
	 }
	 attrbList+="&<falcon_header>&SALARIED CREDIT CARD- Falcon Employment@10";
	 attrbList += "&<FALCON_Wi_name>&" +(get_ngformValue_template('WiLabel').replace("CDOB-","")).replace("-process","");
	 return attrbList;
}
function addZero(i) {
  if (i < 10) {
    i = "0" + i;
  }
  return i;
}

function getCompanyDetails(attrbList,GridName)//added by prateeka   
{
	if (getLVWRowCount(GridName) >0)
					  {
						for(var i=0; i <getLVWRowCount(GridName); i++)
						{ 
							if (getLVWAT(GridName,i,1)=='Business')
							{
								attrbList += "&<SE_Legalenity>&" + get_ngformGridCode_Description(get_ngformGridColumn_template(GridName,i,13),get_ngform_controlObject('legalEntity'));

								//attrbList += "&<SE_Legalenity>&" + get_ngformGridColumn_template(GridName,i,13);
								attrbList += "&<targetSegCode>&" + get_ngformGridColumn_template(GridName,i,22);
								attrbList += "&<SE_POA>&" + get_ngformGridColumn_template(GridName,i,20);
								attrbList += "&<SE_LOB>&" + get_ngformGridColumn_template(GridName,i,15);
								attrbList += "&<SE_ELOB>&" + getLVWAT(GridName,i,37);//PCASP-2948
								attrbList += "&<SE_Emp_indus_macro>&" + get_ngformGridColumn_template(GridName,i,9);
								attrbList += "&<SE_Emp_indus_micro>&" + get_ngformGridColumn_template(GridName,i,10);
								attrbList += "&<SE_Corp_cif>&" + get_ngformGridColumn_template(GridName,i,3);
								attrbList += "&<SE_Bvr_Fvr>&" + get_ngformGridColumn_template(GridName,i,18);
								attrbList += "&<SE_Comp_name>&" + get_ngformGridColumn_template(GridName,i,4);
								attrbList += "&<SE_Head_office>&" + get_ngformGridColumn_template(GridName,i,17);
								attrbList += "&<SE_plcat>&" + get_ngformGridColumn_template(GridName,i,33);
								attrbList += "&<SE_EmpStatus_PL>&" + get_ngformGridColumn_template(GridName,i,36); 
								attrbList += "&<SE_PLCATC>&" + get_ngformGridColumn_template(GridName,i,34);
							}
						}
					  }
	
	//Added by Shivang for Fetching Additional company Details from DB which is not available in UI
	
	var pname="CCTemplateData";	
	var Query = "Facility_BookedLast_6Month"
	var wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var facilityBooked = CallAjax(jspName,params);
	var fields = facilityBooked.split('#');
	var facilityBooked = fields[0];
	if(facilityBooked.indexOf("NODATA")>-1 || facilityBooked.indexOf("ERROR")>-1){
		facilityBooked='0';
	}
	attrbList += "&<FacilityBooked>&" + facilityBooked + '@10';
	//CR received on 19-Nov-2020
	if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='BTC'){
		attrbList += "&<ExposureWithRAK>&" + getNGValue("cmplx_Liability_New_BTCTotExpsure") + '@10';
	}
	else{
		var Query = "ExposureWithRAK"
		var wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var expWithRAK = CallAjax(jspName,params);
		var fields = expWithRAK.split('#');
		var expWithRAK = fields[0];
		if(expWithRAK.indexOf("NODATA")>-1 || expWithRAK.indexOf("ERROR")>-1){
			expWithRAK='0';
		}
		attrbList += "&<ExposureWithRAK>&" + expWithRAK + '@10';
	}
	var Query = "AccountNoforCompany"
	var wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var AcctNo = CallAjax(jspName,params);
	var fields = AcctNo.split('#');
	var acctnumber = fields[0];
	if(acctnumber.indexOf("NODATA")>-1 || acctnumber.indexOf("ERROR")>-1){
		acctnumber='0';
	}
	attrbList += "&<AccountNo_forCompany>&" + acctnumber + '@10';

	return attrbList;
}
//Added by Shivang for SE CAM
function getAECBSummary(attrbList)
{
	var pname="CCTemplateData";
	
	var Query = "AECBHistSignatory"
	var wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var aecbHist = CallAjax(jspName,params);
	var fields = aecbHist.split('#');
	var aecbHistSign = fields[0];
	if(aecbHistSign.indexOf("NODATA")>-1 || aecbHistSign.indexOf("ERROR")>-1){
		aecbHistSign='0';
	}
		
	Query = "AECBHistCompany"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var aecbHist = CallAjax(jspName,params);
	var fields = aecbHist.split('#');
	var aecbHistCompany = fields[0];
	if(aecbHistCompany.indexOf("NODATA")>-1 || aecbHistCompany.indexOf("ERROR")>-1){
		aecbHistCompany='0';
	}
	
	Query = "MOB_Signatory"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var mob = CallAjax(jspName,params);
	var fields = mob.split('#');
	var mobSignatory = fields[0];
	if(mobSignatory.indexOf("NODATA")>-1 || mobSignatory.indexOf("ERROR")>-1){
		mobSignatory='0';
	}
	Query = "MOB_Company"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var mob = CallAjax(jspName,params);
	var fields = mob.split('#');
	var mobCompany = fields[0];
	if(mobCompany.indexOf("NODATA")>-1 || mobCompany.indexOf("ERROR")>-1){
		mobCompany='0';
	}	
	
	Query = "WSL24MonthSign"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var worstStatus = CallAjax(jspName,params);
	var fields = worstStatus.split('#');
	var worstStatus_Sign = fields[0];
	if(worstStatus_Sign.indexOf("NODATA")>-1 || worstStatus_Sign.indexOf("ERROR")>-1){
		worstStatus_Sign='';
	}
	Query = "WSL24MonthCompany"
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var worstStatus = CallAjax(jspName,params);
	var fields = worstStatus.split('#');
	var worstStatus_Comp = fields[0];
	if(worstStatus_Comp.indexOf("NODATA")>-1 || worstStatus_Comp.indexOf("ERROR")>-1){
		worstStatus_Comp='';
	}
	Query = "AECBCheck"
	wparams="Code=="+com.newgen.omniforms.formviewer.getNGValue('Sub_Product');
	jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var corporate = CallAjax(jspName,params);
	var fields = corporate.split('#');
	var corp = fields[0];
	if(corp.indexOf("NODATA")>-1 || corp=='N'){
		attrbList += "&<SE_aecbHistCompany>&" + "NA" +'@10';
		attrbList += "&<SE_MOB_Company>&" + "NA" +'@10';
		attrbList += "&<SE_WSL24MonthCompany>&" + "NA" +'@10';
	}
	else{
		attrbList += "&<SE_aecbHistCompany>&" + aecbHistCompany +'@10';
		attrbList += "&<SE_MOB_Company>&" + mobCompany +'@10';
		attrbList += "&<SE_WSL24MonthCompany>&" + worstStatus_Comp +'@10';
	}
	attrbList += "&<SE_aecbHistSignatory>&" + aecbHistSign +'@10';	
	attrbList += "&<SE_MOB_Signatory>&" + mobSignatory +'@10';	
	attrbList += "&<SE_WSL24MonthSign>&" + worstStatus_Sign +'@10';
	
	
	return attrbList;
}
function creditcard_selectPL(attrbList)
{
	var attrbList = "";
	if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)
	{
		if($('#cmplx_EmploymentDetails_IncInPL').is(":checked")){
			attrbList+="&<IncInPL>&"+"Yes@10";
		}
		else{
			attrbList+="&<IncInPL>&"+"No@10";
		}
	}
	else 
	{
		attrbList += "&<IncInPL>&" + " " + "@10";
	}
	return attrbList;
}
function creditcard_selectCC(attrbList)
{
	var attrbList = "";
	if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1){
		if($('#cmplx_EmploymentDetails_IncInCC').is(":checked")){
			attrbList+="&<IncInCC>&"+"Yes@10";
		}
		else{
			attrbList+="&<IncInCC>&"+"No@10";
		}
	}
	else 
	{
		attrbList += "&<IncInCC>&" + " " + "@10";
	}

	return attrbList;
}
//changed by nikhil for PCSP-23
function creditcard_check(attrbList,GridName){
	var attrbList = "";
	var smart_cpv="";
	var fcu="";
	var Compliance="";
	if (getLVWRowCount(GridName) >0)
	{
		for(var i=0; i <getLVWRowCount(GridName); i++)
		{
			var refer_to= get_ngformGridColumn_template(GridName,i,6).split('@')[0];
			if(refer_to == 'Smart CPV')
			{
			smart_cpv="Y";
			}
			if(refer_to == 'FCU')
			{
			fcu="Y";
			}
			if(refer_to == 'Compliance')
			{
			Compliance="Y";
			}
		}
	}
	//var other_check= get_ngformValue_template("cmplx_DEC_Decision");
//alert("other_check" + other_check);
	//var value = getFromJSPSpecificRowCellContent('')
	if(smart_cpv=="Y"){
		attrbList+="&<check1>&Yes@10";
	}
	else
	{
		attrbList+="&<check1>&No@10";
	}
	if(fcu=="Y"){
		attrbList+="&<check>&Yes@10";
	}
	else
	{
		attrbList+="&<check>&No@10";
	}
	if(Compliance=="Y"){
		attrbList+="&<check_compl>&Yes@10";
	}
	else
	{
		attrbList+="&<check_compl>&No@10";
	}
	
	return attrbList;
	 
}
	 function creditcard_notepad(attrbList)
	 {
		 var attrbList = "";
		 var note_desc1 = "";
		 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid") > 0){
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid"); i++){
                    var note_desc1= com.newgen.omniforms.formviewer.getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,1);
					if(note_desc1 =='NCARD'){
						                      attrbList = "&<desc1>&"+get_ngformGridColumn_template("cmplx_NotepadDetails_cmplx_notegrid",i,2)+"@10";
											  break;
                    }
					else{
						attrbList = "&<desc1>&"+" "+"@10";
					}
					
	 }
		 }
		 else{
				attrbList = "&<desc1>&"+" "+"@10";
			}
		 return attrbList;
	 }

function creditcard_template(attrbList){
	 var attrbList = "";
	 var value_type = "";
	 var note_desc = "";
			if (getLVWRowCount("DecisionHistory_Decision_ListView1") >= 0){
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("DecisionHistory_Decision_ListView1"); i++){
                    var value_type= get_ngformGridColumn_template("DecisionHistory_Decision_ListView1",i,2);
                    if(value_type!=undefined && value_type.split('@')[0]=='CAD_Analyst1'){
						                      attrbList+= "&<Remarks>&"+get_ngformGridColumn_template("DecisionHistory_Decision_ListView1",i,4)+"@10";
                   
					}
					if(value_type!=undefined && value_type.split('@')[0]=='CAD_Analyst2'){
						                      attrbList+= "&<TYPE1>&"+get_ngformGridColumn_template("DecisionHistory_Decision_ListView1",i,4)+"@10";
                    }
					else
					{
					attrbList+="&<Remarks>&"+(com.newgen.omniforms.formviewer.getLVWAT("DecisionHistory_Decision_ListView1",i,4) =='') ? "" :
					com.newgen.omniforms.formviewer.getLVWAT("DecisionHistory_Decision_ListView1",i,4)+"@10";
					}				
                }
			
			}
			/* if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid") >0){*/
			var temp='';
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid"); i++){
                    var note_desc= com.newgen.omniforms.formviewer.getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,1);
					if(note_desc =='NCPV' || note_desc =='NSMART'){
						temp+=com.newgen.omniforms.formviewer.getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,2)+',';
					}
				}
				if(temp.charAt(temp.length-1)==','){
					temp = temp.substring(0,temp.length-1);
				}
				if(temp!=''){
						attrbList += "&<desc>&"+temp+"@10"+",";
				}
					else{
						attrbList = "&<desc>&"+" "+"@10";
				}
			
		//	}
			
			/*else{
						attrbList = "&<desc>&"+" "+"@10";
				}*/
	

if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid") >= 0){
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid"); i++){
				   
                 var product_type= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,0);
			     var product1_type= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2);
				  //added by shivang for sprint3
                 var Emptype= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,6);
				 var applicationType= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,4);				  
				//Changed by Shivang
                 if(product_type=='Islamic' && (product1_type=='Instant Money' || product1_type=='IM')){
				attrbList+="&<header>&INSTANT FINANCE@10";
				attrbList+="&<loan>&FINANCE@10";

				}
				else if(product_type=='Conventional' && (product1_type=='Instant Money' || product1_type=='IM')){
				attrbList+="&<header>&INSTANT MONEY@10";
                attrbList+="&<loan>&Loan@10";
				}
				else if(product_type=='Conventional' && product1_type=='Limit Increase'){
				attrbList+="&<header>&CREDIT CARD LIMIT INCREASE@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				//Changed by Shivang
				else if(product_type=='Islamic' && product1_type=='Limit Increase'){
				attrbList+="&<header>&CREDIT CARD LIMIT INCREASE@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Islamic' && product1_type=='PU'){
				attrbList+="&<header>&CREDIT CARD PRODUCT UPGRADE@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='PU'){
				attrbList+="&<header>&CREDIT CARD PRODUCT UPGRADE@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				
				else if(product_type=='Islamic' && product1_type=='PULI'){
				attrbList+="&<header>&CREDIT CARD LIMIT INCREASE AND PRODUCT UPGRADE@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='PULI'){
				attrbList+="&<header>&CREDIT CARD LIMIT INCREASE AND PRODUCT UPGRADE@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				
				//Added by Shivang for Bundled Card header
				else if(Emptype=='Self Employed' && product1_type=='BPA' ){
				attrbList+="&<header>&BUNDLED CARD(SELF EMPLOYED)@10";
				attrbList+="&<loan>&Loan@10";
				}
				//Added by Shivang for Bundled Card header
				else if((!(Emptype=='Self Employed')) && product1_type=='BPA' ){
				attrbList+="&<header>&BUNDLED CARD(SALARIED)@10";
				attrbList+="&<loan>&Loan@10";
				}				
				//if(product_type=='Islamic' && product1_type=='BPA' && product2_type=='PLBUN')
				else if(product_type=='Islamic' && product1_type=='BPA' ){
				attrbList+="&<header>&CREDIT CARD PL BUNDLE@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				//if(product_type=='Conventional' && product1_type=='BPA' && product2_type=='PLBUN')
				else if(product_type=='Conventional' && product1_type=='BPA'){
				attrbList+="&<header>&CREDIT CARD PL BUNDLE@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				
				//if(product_type=='Islamic' && product1_type=='BPA' && product2_type=='ALBUN')
				else if(product_type=='Islamic' && product1_type=='BPA'){
				attrbList+="&<header>&CREDIT CARD AL BUNDLE@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				//if(product_type=='Conventional' && product1_type=='BPA' && product2_type=='ALBUN')
				else if(product_type=='Conventional' && product1_type=='BPA'){
				attrbList+="&<header>&CREDIT CARD AL BUNDLE@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				//commented by Shivang for spring3 as New condition Introduced
				/*else if(product_type=='Islamic' && product1_type=='SEC'){
				attrbList+="&<header>&SECURED CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='SEC'){
				attrbList+="&<header>&SECURED CREDIT CARD@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				*/
				else if(product_type=='Islamic' && product1_type=='SE'){ //added by Prateeka
					if (getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid') >0)
					  {
						for(var i=0; i <getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid'); i++)
						{ 
							if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,1)=='Business')
							{
								if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU@10' || getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU')
								{
									attrbList+="&<header>&SELF EMPLOYED - BAU@10";
									attrbList+="&<loan>&Finance@10";
								}	
								
								else 
								{
									attrbList+="&<header>&SELF EMPLOYED - DOCTOR SURROGATE@10";
									attrbList+="&<loan>&Finance@10";
								}
									
							}
						}
					  }
		
				}
				else if(product_type=='Conventional' && product1_type=='SE'){ //added by Prateeka
				
				if (getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid') >0)
					  {
						for(var i=0; i <getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid'); i++)
						{ 
							if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,1)=='Business')
							{
								if (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU@10' || getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,21)=='BAU')
								{
									attrbList+="&<header>&SELF EMPLOYED - BAU@10";
									attrbList+="&<loan>&Loan@10";
								}	
								
								else 
								{
									attrbList+="&<header>&SELF EMPLOYED - DOCTOR SURROGATE@10";
									attrbList+="&<loan>&Loan@10";
								}
									
							}
						}
					  }
				}
				//Added By Shivang for BTC CAM header
				else if(product_type=='Islamic' && product1_type=='BTC'){					
					if(applicationType=='SMES'){
						attrbList+="&<header>&BUSINESS TITANIUM CARD(SECURED)@10";
					}else{
						attrbList+="&<header>&BUSINESS TITANIUM CARD@10";
					}
					attrbList+="&<loan>&Finance@10";
				}
				//Added By Shivang for BTC CAM header
				else if(product_type=='Conventional' && product1_type=='BTC'){					
					if(applicationType=='SMES'){
						attrbList+="&<header>&BUSINESS TITANIUM CARD(SECURED)@10";
					}else{
						attrbList+="&<header>&BUSINESS TITANIUM CARD@10";
					}
					attrbList+="&<loan>&Loan@10";
				}
				//Added by shivang for sprint3 for Secured CAM header
				else if((!(Emptype=='Self Employed')) && product1_type=='SEC'){
				attrbList+="&<header>&SECURED CARD(SALARIED)@10";
				attrbList+="&<loan>&Loan@10";
				}
				//Added by shivang for sprint3 for Secured CAM header
				else if(Emptype=='Self Employed' && product1_type=='SEC'){
				attrbList+="&<header>&SECURED CARD(SELF EMPLOYED)@10";
				attrbList+="&<loan>&Loan@10";
				}
				else if(product_type=='Islamic' && product1_type=='SAL'){
				attrbList+="&<header>&SALARIED CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='SAL'){
				attrbList+="&<header>&SALARIED CREDIT CARD@10";
				attrbList+="&<loan>&Loan@10";
				}
				
				/*else if(product_type=='Islamic' && product1_type=='SE'){
				attrbList+="&<header>&Self Employed - CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='SE'){
				attrbList+="&<header>&Self Employed- CREDIT CARD@10";
				attrbList+="&<loan>&Loan@10";
				}*/
				
				else if(product1_type=='PA' && !(Emptype=='Self Employed')){
				//attrbList+="&<header>&PRE - APPROVED CREDIT CARD@10";
				attrbList+="&<header>&PRE – APPROVED CREDIT CARD SAL@10";
				}
				else if(product1_type=='PA' && Emptype=='Self Employed' && applicationType=='MOR'){
					//attrbList+="&<header>&PRE - APPROVED CREDIT CARD@10";
					attrbList+="&<header>&MORTGAGE LOAN@10";
				}
				else if(product1_type=='PA' && Emptype=='Self Employed' && (applicationType=='RFL' ||applicationType =='RFLP')){
					//attrbList+="&<header>&PRE - APPROVED CREDIT CARD@10";
					attrbList+="&<header>&RFL@10";
				}
				else if(product1_type=='PA' && Emptype=='Self Employed'){
					//attrbList+="&<header>&PRE - APPROVED CREDIT CARD@10";
					attrbList+="&<header>&PRE APPROVED (SELF EMPLOYED) @10";
				}
				//Added below by Tarang on 26/03/2018
 				else if(product_type=='Islamic' && product1_type=='EXP'){
				attrbList+="&<header>&EXPAT FINANCE@10";
				attrbList+="&<loan>&Finance@10";
				}
				else if(product_type=='Conventional' && product1_type=='EXP'){
				attrbList+="&<header>&EXPAT LOANS@10";
				attrbList+="&<loan>&Loan@10";
				}
				else if(product_type=='Islamic' && product1_type=='NAT'){
				attrbList+="&<header>&UAE NATIONAL FINANCE@10";
				attrbList+="&<loan>&Loan@10";
				}
				else if(product_type=='Conventional' && product1_type=='NAT'){
				attrbList+="&<header>&UAE NATIONAL LOANS@10";
				attrbList+="&<loan>&Loan@10";
				}
				//Added above by Tarang on 26/03/2018
				else{
				attrbList+="&<header>&SALARIED CREDIT CARD@10";
				
				attrbList+="&<loan>&Loan@10";
				}
                 if(product_type=='Conventional' && product1_type=='PA'){
                	 attrbList+="&<loan>&Loan@10";
                 } else  if(product_type=='Islamic' && product1_type=='PA'){
                	 attrbList+="&<loan>&Finance@10";
                 }
			   }
				}

			
return attrbList;
}

function fetch_referTo_se(attrbList,GridName) // prateeka change doe SE CAM starts
{

	var cad2_decision='';
	var cad1_decision='';
	var cad1_referto = '';
	var cad2_referto = '';
	var cad1_refer_remarks='';
	var cad2_refer_remarks='';
	var resetflag=0;
	var resetflag_cad2=0;
	//var cad_decision_reason_code='';
	var activityName=window.parent.stractivityName;
	if (getLVWRowCount(GridName) >0){
		for(var i=0; i <getLVWRowCount(GridName); i++){
			var workstepName= get_ngformGridColumn_template(GridName,i,2).split('@')[0];
				if(activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2' || activityName=='CAD_Analyst2')
				{
				 if(workstepName =='CAD_Analyst1'  ){
					//attrbList += "&<Remarks_CAD>&"+get_ngformGridColumn_template(GridName,i,4);					
					cad1_decision=get_ngformGridColumn_template(GridName,i,3);					
						if((cad1_decision=='Refer@10' || cad1_decision =="REFER@10")  )
						{
							if(resetflag==1){
								cad1_referto="";
								cad1_refer_remarks="";
								resetflag=0;
							}
							if(cad1_referto=='')
							{
							cad1_referto= get_ngformGridColumn_template(GridName,i,6);	
							cad1_refer_remarks= get_ngformGridColumn_template(GridName,i,4);
							//cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
							}
							else
							{
							cad1_referto= cad1_referto + ", " +get_ngformGridColumn_template(GridName,i,6);
							cad1_refer_remarks= cad1_refer_remarks + ", " +get_ngformGridColumn_template(GridName,i,4);
							//cad_decision_reason_code= cad_decision_reason_code + ", " +get_ngformGridColumn_template(GridName,i,8);
							}
						}
						else
						{
						cad1_refer_remarks=get_ngformGridColumn_template(GridName,i,4);
						cad1_referto="";
						//cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
						}
					}											
				 else if((workstepName =='CAD_Analyst2' || workstepName=='Cad_Analyst2') ){ 
					
					 if(get_ngformGridColumn_template(GridName,i,3)=="Refer@10" || get_ngformGridColumn_template(GridName,i,3)=="REFER@10"){
						if(cad2_referto=='')
							{
							cad2_referto= get_ngformGridColumn_template(GridName,i,6);	
							cad2_refer_remarks= get_ngformGridColumn_template(GridName,i,4);
							//cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
							}
							else
							{
							cad2_referto= cad2_referto + ", " +get_ngformGridColumn_template(GridName,i,6);
							cad2_refer_remarks= cad2_refer_remarks + ", " +get_ngformGridColumn_template(GridName,i,4);
							//cad_decision_reason_code= cad_decision_reason_code + ", " +get_ngformGridColumn_template(GridName,i,8);
							}
					 }
					 else
						{
						cad2_referto="";
						cad2_refer_remarks=get_ngformGridColumn_template(GridName,i,4);
						if(get_ngformGridColumn_template(GridName,i,3)=="Sendback@10" || get_ngformGridColumn_template(GridName,i,3)=="SENDBACK@10"){
							resetflag=1;
						}
						//cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
						}
						
					}
					else
					{
					resetflag_cad2=1;
					cad2_referto="";
					cad1_referto="";
					cad1_refer_remarks="";
					cad2_refer_remarks="";
					//cad_decision_reason_code= "";
					}
				}				
		}
			/*var n= getLVWRowCount(GridName);
		for(int j=n-1;i>n-3;j--){
			var workstepName= get_ngformGridColumn_template(GridName,j,2).split('@')[0];
			cad1_decision=get_ngformGridColumn_template(GridName,i,3).split('@')[0];
			if(activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2')
				{
				 if(workstepName =='CAD_Analyst1' &&((cad1_decision=='Refer@10' || cad1_decision =="REFER@10"))){
					 
					 cad1_referto= cad1_referto + ", " +get_ngformGridColumn_template(GridName,i,6);
					 cad1_refer_remarks= cad1_refer_remarks + ", " +get_ngformGridColumn_template(GridName,i,4);
				 }
				}
		}*/
	}
		attrbList += "&<se_CAD1_ReferTo>&" + cad1_referto + '@10';
		attrbList += "&<se_CAD2_ReferTo>&" + cad2_referto + '@10';
		attrbList += "&<se_cad1_refer_remarks>&" + cad1_refer_remarks + '@10';
		attrbList += "&<se_cad2_refer_remarks>&" + cad2_refer_remarks + '@10';
		
		return attrbList;
	
} // prateeka change doe SE CAM ends








function fetch_referTo(attrbList,GridName){

	var cad2_decision='';
	var cad1_decision='';
	var cad1_referto = '';
	var cad_refer_remarks='';
	var cad_decision_reason_code='';
	var activityName=window.parent.stractivityName;
	if (getLVWRowCount(GridName) >0){
		for(var i=0; i <getLVWRowCount(GridName); i++){
			var workstepName= get_ngformGridColumn_template(GridName,i,2).split('@')[0];
				if(activityName=='CAD_Analyst1')
				{
				 if(workstepName =='CAD_Analyst1' ){
					//attrbList += "&<Remarks_CAD>&"+get_ngformGridColumn_template(GridName,i,4);
					
					cad1_decision=get_ngformGridColumn_template(GridName,i,3);
					
					
						if((cad1_decision=='Refer@10' || cad1_decision =="REFER@10")  )
						{
							if(cad1_referto=='')
							{
							cad1_referto= get_ngformGridColumn_template(GridName,i,6);	
							cad_refer_remarks= get_ngformGridColumn_template(GridName,i,4);
							cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
							}
							else
							{
							cad1_referto= cad1_referto + ", " +get_ngformGridColumn_template(GridName,i,6);
							cad_refer_remarks= cad_refer_remarks + ", " +get_ngformGridColumn_template(GridName,i,4);
							cad_decision_reason_code= cad_decision_reason_code + ", " +get_ngformGridColumn_template(GridName,i,8);
							}
						}
						else
						{
						cad_refer_remarks=get_ngformGridColumn_template(GridName,i,4);
						cad1_referto="";
						cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
						}
						}
						else
						{
						cad1_referto="";
						cad_decision_reason_code="";
						}
					}							
					
					if(activityName=='Cad_Analyst2' || activityName=='CAD_Analyst2'){//PCASI-1104 by shweta
				 if((workstepName =='CAD_Analyst2' || workstepName=='Cad_Analyst2') ){ 
					 if(get_ngformGridColumn_template(GridName,i,3)=="Refer@10" || get_ngformGridColumn_template(GridName,i,3)=="REFER@10"){
						if(cad1_referto=='')
							{
							cad1_referto= get_ngformGridColumn_template(GridName,i,6);	
							cad_refer_remarks= get_ngformGridColumn_template(GridName,i,4);
							cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
							}
							else
							{
							cad1_referto= cad1_referto + ", " +get_ngformGridColumn_template(GridName,i,6);
							cad_refer_remarks= cad_refer_remarks + ", " +get_ngformGridColumn_template(GridName,i,4);
							cad_decision_reason_code= cad_decision_reason_code + ", " +get_ngformGridColumn_template(GridName,i,8);
							}
					 }
					 else
						{
						cad1_referto="";
						cad_refer_remarks=get_ngformGridColumn_template(GridName,i,4);
						cad_decision_reason_code= get_ngformGridColumn_template(GridName,i,8);
						}
						
					}
					else
					{
					cad1_referto="";
					cad_decision_reason_code= "";
					}
				}					
		}	
	}
	
	
	//added by shivang for PCAS 2744	
	attrbList += "&<CAD1_ReferTo>&" + cad1_referto + '@10';
attrbList += "&<Dec_Remarks>&" + cad_refer_remarks + '@10';
attrbList += "&<ReferReason>&" + cad_decision_reason_code + '@10';
	
	return attrbList;
}
function fetch_remarks(attrbList,GridName){

	var remarks_Map={'Remarks_Init':'','Remarks_Ops':'','Remarks_Feedback':'','Remarks_CPV':'','Remarks_Sales':'','Remarks_CPV_Feed':'','Remarks_CAD':'','Remarks_CAD1':'','Remarks_CAD2':'','Remarks_Compliance':''};
	//changes done by saurabh for PCSP-23 on 7th Feb 19.
	var workstepPlaceholderMap = {'Branch_Init':'Remarks_Init','Original_Validation':'Remarks_Ops','FCU':'Remarks_Feedback','CPV':'Remarks_CPV',
	'Collection_Agent_Review':'Remarks_Sales','Smart_CPV':'Remarks_CPV_Feed','CAD_Analyst1':'Remarks_CAD1','CAD_Analyst2':'Remarks_CAD2','Cad_Analyst2':'Remarks_CAD2','Compliance':'Remarks_Compliance'};
	//added by shivang for PCAS 2744
	var cad2_decision='';
	var cad1_decision='';
	var cad1_referto = '';
	var cad_refer_remarks= '';
	if (getLVWRowCount(GridName) >0){
		for(var i=0; i <getLVWRowCount(GridName); i++){
			var workstepName= get_ngformGridColumn_template(GridName,i,2).split('@')[0];
			//change by saurabh on 7th Feb 19.
			if(!remarks_Map[workstepPlaceholderMap[workstepName]]){
				if(workstepName =='Branch_Init'){
					//attrbList += "&<Remarks_Init>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_Init']= get_ngformGridColumn_template(GridName,i,4);

				}
				else if(workstepName =='DDVT_Checker'){
					//attrbList += "&<Remarks_Ops>&"+get_ngformGridColumn_template(GridName,i,4);
									remarks_Map['Remarks_Ops']= get_ngformGridColumn_template(GridName,i,4);

				}
				else if(workstepName =='FCU'){
					//attrbList += "&<Remarks_Feedback>&"+get_ngformGridColumn_template(GridName,i,4);
									remarks_Map['Remarks_Feedback']= get_ngformGridColumn_template(GridName,i,4);

				}
				else if(workstepName =='CPV'){
					//attrbList += "&<Remarks_CPV>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_CPV']= get_ngformGridColumn_template(GridName,i,4);

					}
				else if(workstepName =='Collection_Agent_Review'){
					//attrbList += "&<Remarks_Sales>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_Sales']= get_ngformGridColumn_template(GridName,i,4);

					}
				else if(workstepName =='Smart_CPV'){
					//attrbList += "&<Remarks_CPV_Feed>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_CPV_Feed']= get_ngformGridColumn_template(GridName,i,4);

					}
				else if(workstepName =='CAD_Analyst1'){
					//attrbList += "&<Remarks_CAD>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_CAD1']= get_ngformGridColumn_template(GridName,i,4);
					
						
							cad1_decision=get_ngformGridColumn_template(GridName,i,3);			
																	
					}
				else if(workstepName =='CAD_Analyst2' || workstepName=='Cad_Analyst2'){
					//attrbList += "&<Remarks_CAD>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_CAD2']= get_ngformGridColumn_template(GridName,i,4);
					//added by shivang for PCAS 2744
					 
						cad2_decision=get_ngformGridColumn_template(GridName,i,3);
					 

					}
				else if(workstepName =='Compliance'){
					//attrbList += "&<Remarks_Compliance>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_Compliance']= get_ngformGridColumn_template(GridName,i,4);

					}
			}
			if(workstepName =='CAD_Analyst1')
			{
					//attrbList += "&<Remarks_CAD>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_CAD1']= get_ngformGridColumn_template(GridName,i,4);
					
						
							cad1_decision=get_ngformGridColumn_template(GridName,i,3);			
																	
			}
			if(workstepName =='CAD_Analyst2' || workstepName=='Cad_Analyst2')
			{
					//attrbList += "&<Remarks_CAD>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_CAD2']= get_ngformGridColumn_template(GridName,i,4);
					//added by shivang for PCAS 2744
					 
						cad2_decision=get_ngformGridColumn_template(GridName,i,3);					 

			}			
		}
	}
	
	var key_array=Object.keys(remarks_Map);
	for(var i=0;i<key_array.length;i++)
	{
		attrbList+='&<'+key_array[i]+'>&'+(remarks_Map[key_array[i]]==''?'@10':remarks_Map[key_array[i]]);
	}
	/*else{
		attrbList += "&<Remarks_Init>&"+" "+"@10";
		attrbList += "&<Remarks_Ops>&"+" "+"@10";
		attrbList += "&<Remarks_Feedback>&"+" "+"@10";
		attrbList += "&<Remarks_CPV>&"+" "+"@10";
		attrbList += "&<Remarks_Sales>&"+" "+"@10";
		attrbList += "&<Remarks_CPV_Feed>&"+" "+"@10";
		attrbList += "&<Remarks_CAD>&"+" "+"@10";
		attrbList += "&<Remarks_Compliance>&"+" "+"@10";
	}*/
	//added by shivang for PCAS 2744
	attrbList += "&<CAD1_Decision>&" + cad1_decision + '@10';
	attrbList += "&<Decision>&" + cad1_decision + '@10';
	//attrbList += "&<CAD1_ReferTo>&" + cad1_referto + '@10';	
	attrbList += "&<CAD2_Decision>&" + cad2_decision + '@10';
	return attrbList;
}
function rlosloan_template(attrbList,Mode){
     //var attrbList = "";
	 var attrbList = "";
	var ofc_value;
	var residence_value;
	var home_value;
	var pname = "CCTemplateData";
	if(Mode=='P'){
		//saurabh. 4/3/19 point 12.
          ofc_value  ="&<building_ofc>&"+" "+"@10" + "&<landmark_ofc>&"+" "+"@10" + "&<city_ofc>&"+" "+"@10" + "&<street_ofc>&"+" "+"@10" + "&<pobox_ofc>&"+" "+"@10" + "&<country_ofc>&"+" "+"@10" + "&<location_ofc>&"+" "+"@10" + "&<flat_ofc>&"+" "+"@10";     
		  //saurabh . 4/3/19 point 13.
            residence_value="&<building_resi>&"+" "+"@10" + "&<landmark_resi>&"+" "+"@10" + "&<city_resi>&"+" "+"@10" + "&<street_resi>&"+" "+"@10" + "&<pobox_resi>&"+" "+"@10" + "&<country_resi>&"+" "+"@10" + "&<flat_resi>&"+" "+"@10" + "&<resid_type>&"+" "+"@10";    
            home_value="&<building_home>&"+" "+"@10" + "&<landmark_home>&"+" "+"@10" + "&<city_home>&"+" "+"@10" + "&<street_home>&"+" "+"@10" + "&<pobox_home>&"+" "+"@10" + "&<country_home>&"+" "+"@10" + "&<flat_home>&"+" "+"@10";
     
	 }
	 else if(Mode=='G'){
		 ofc_value  ="&<building_ofc_guar>&"+" "+"@10" + "&<landmark_ofc_guar>&"+" "+"@10" + "&<city_ofc_guar>&"+" "+"@10" + "&<street_ofc_guar>&"+" "+"@10" + "&<pobox_ofc_guar>&"+" "+"@10" + "&<country_ofc_guar>&"+" "+"@10" + "&<location_ofc_guar>&"+" "+"@10" + "&<flat_ofc_guar>&"+" "+"@10";     
            residence_value="&<building_resi_guar>&"+" "+"@10" + "&<landmark_resi_guar>&"+" "+"@10" + "&<city_resi_guar>&"+" "+"@10" + "&<street_resi_guar>&"+" "+"@10" + "&<pobox_resi_guar>&"+" "+"@10" + "&<country_resi_guar>&"+" "+"@10" + "&<flat_resi_guar>&"+" "+"@10" + "&<resid_type_guar>&"+" "+"@10";    
            home_value="&<building_home_guar>&"+" "+"@10" + "&<landmark_home_guar>&"+" "+"@10" + "&<city_home_guar>&"+" "+"@10" + "&<street_home_guar>&"+" "+"@10" + "&<pobox_home_guar>&"+" "+"@10" + "&<country_home_guar>&"+" "+"@10" + "&<flat_home_guar>&"+" "+"@10";
	 }
     
            if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid") >= 0){
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid"); i++)
               
               {
                    var value_type= com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
					var appType = com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,13);
                if(window.pname=='CreditCard'|| window.pname=='CC'){
					
                    if(value_type =='OFFICE' && appType.indexOf(Mode)>-1)
						
                     {
							var Query = "OfficeCity";						 	
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5);
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var city = fields[0]+"@10";
							if(city.indexOf("ERROR")>-1){
								city='';
							}
							var Query1 = "OfficeCountry";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,7);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var country = fields1[0]+"@10";
							if(country.indexOf("ERROR")>-1){
								country='';
							}
                                         if(Mode=='P'){
                                           ofc_value  = "&<building_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" + "&<landmark_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<city_ofc>&"+city+"@10" +"@10" + "&<pobox_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"@10" + "&<country_ofc>&"+country+"@10" + "&<location_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10"+"&<flat_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10";
										}
										else if(Mode=='G'){
											ofc_value  = "&<building_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" + "&<landmark_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<city_ofc_guar>&"+city+"@10" +"@10" + "&<pobox_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"@10" + "&<country_ofc_guar>&"+country+"@10" + "&<location_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10"+"&<flat_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10";
										}
					}
                     
                     if(value_type =='RESIDENCE' && appType.indexOf(Mode)>-1)
                    
                     {
							var Query="ResidenceCity";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5);
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var city = fields[0]+"@10";
							
							var Query1 = "ResidenceCountry";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,7);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var country = fields1[0]+"@10";
											if(Mode=='P'){
                                              residence_value = "&<building_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" + "&<landmark_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<city_resi>&"+city+"@10" + "&<street_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<pobox_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"@10" + "&<country_resi>&"+country+"@10" + "&<flat_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10"+"&<resid_type>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,11)+"@10";
                                             }
										else if(Mode=='G'){
										residence_value = "&<building_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" + "&<landmark_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<city_resi_guar>&"+city+"@10" + "&<street_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<pobox_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"@10" + "&<country_resi_guar>&"+country+"@10" + "&<flat_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10"+"&<resid_type_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,11)+"@10";
										}											 
                    
                
                    }
                    if(value_type =='Home' && appType.indexOf(Mode)>-1)
                     
                     {
							var Query="HomeCity";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5);
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var city = fields[0]+"@10";
							
							var Query1="HomeCountry";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,7);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var country = fields1[0]+"@10";
											if(Mode=='P'){
                                               home_value = "&<building_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" + "&<landmark_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<city_home>&"+city+"@10" + "&<street_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<pobox_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"@10" + "&<country_home>&"+country+"@10" + "&<flat_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" ;
											   }
											   else if(Mode=='G'){
											    home_value = "&<building_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" + "&<landmark_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<city_home_guar>&"+city+"@10" + "&<street_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<pobox_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"@10" + "&<country_home_guar>&"+country+"@10" + "&<flat_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" ;
											   }
                    }           
				}
				else{
                    if(value_type =='OFFICE' && appType.indexOf(Mode)>-1)
						
                     {
							var Query = "OfficeCity";
						 	//var Query = "SELECT isnull(Description,'') as city FROM NG_MASTER_City with (nolock) WHERE code =";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var city = fields[0]+"@10";
							if(city.indexOf("ERROR")>-1){
								city='';
							}
							var Query1 = "OfficeCountry";
							//var Query1 = "SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code ='"+ com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"'";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var country = fields1[0]+"@10";
							if(country.indexOf("ERROR")>-1){
								country='';
							}
                                         if(Mode=='P'){
                                           ofc_value  = "&<building_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<landmark_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5)+"@10" + "&<city_ofc>&"+city+"@10" +"@10" + "&<pobox_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" + "&<country_ofc>&"+country+"@10" + "&<location_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10"+"&<flat_ofc>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10";
										}
										else if(Mode=='G'){
											ofc_value  = "&<building_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<landmark_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5)+"@10" + "&<city_ofc_guar>&"+city+"@10" +"@10" + "&<pobox_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" + "&<country_ofc_guar>&"+country+"@10" + "&<location_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10"+"&<flat_ofc_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10";
										}
					}
                     
                     if(value_type =='RESIDENCE' && appType.indexOf(Mode)>-1)
                    
                     {
							var Query="ResidenceCity";
						 	//var Query = "SELECT isnull(Description,'') as city FROM NG_MASTER_City with (nolock) WHERE code ='"+ com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,6)+"'";
							//changes by saurabh for application form. Changes on 14th Jan 19.
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var city = fields[0]+"@10";
							
							var Query1 = "ResidenceCountry";
							//var Query1 = "SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code ='"+ com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"'";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var country = fields1[0]+"@10";
											if(Mode=='P'){
                                              residence_value = "&<building_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<landmark_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5)+"@10" + "&<city_resi>&"+city+"@10" + "&<street_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<pobox_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" + "&<country_resi>&"+country+"@10" + "&<flat_resi>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10"+"&<resid_type>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,11)+"@10";
                                             }
										else if(Mode=='G'){
										residence_value = "&<building_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<landmark_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5)+"@10" + "&<city_resi_guar>&"+city+"@10" + "&<street_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<pobox_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" + "&<country_resi_guar>&"+country+"@10" + "&<flat_resi_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10"+"&<resid_type_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,11)+"@10";
										}											 
                    
                
                    }
                    if(value_type =='Home' && appType.indexOf(Mode)>-1)
                     
                     {
							var Query="HomeCity";
						 	//var Query = "SELECT isnull(Description,'') as city FROM NG_MASTER_City with (nolock) WHERE code ='"+ com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,6)+"'";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var city = fields[0]+"@10";
							
							var Query1="HomeCountry";
							//var Query1 = "SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code ='"+ com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"'";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var country = fields1[0]+"@10";
											if(Mode=='P'){
                                               home_value = "&<building_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<landmark_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5)+"@10" + "&<city_home>&"+city+"@10" + "&<street_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<pobox_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" + "&<country_home>&"+country+"@10" + "&<flat_home>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" ;
											   }
											   else if(Mode=='G'){
											    home_value = "&<building_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,3)+"@10" + "&<landmark_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5)+"@10" + "&<city_home_guar>&"+city+"@10" + "&<street_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,4)+"@10" + "&<pobox_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,1)+"@10" + "&<country_home_guar>&"+country+"@10" + "&<flat_home_guar>&"+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,2)+"@10" ;
											   }
                    }
            
				}
		}
    }
     attrbList+=  ofc_value + residence_value + home_value ;
    return attrbList;
            
}


function RLOSTemplateData(docName)
{ 
	//changes done by shivang for Islamic App form changes starts
	if(window.pname=='CreditCard'|| window.pname=='CC'){
        var currentDate = get_ngformValue_template('CurrentDateLabel');
	}
	else{
		var currentDate = get_ngformValue_template('lbl_curr_date_val');
	}
	var doj = get_ngformValue_template('cmplx_EmploymentDetails_DOJ');
	var dateOfLeaving = get_ngformValue_template('cmplx_EmploymentDetails_DateOfLeaving');
	var expDateSup1 = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,15);
	var expDateSup2 = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,15);
	var emirateIDExpiry = "";
	var passPortExpiry = "";
	var mobileNo1 = "";
	if(!getNGValue('AlternateContactDetails_MobileNo1').indexOf('00')==0){
		mobileNo1 = "00" + get_ngformValue_template('AlternateContactDetails_MobileNo1');
	}
	else{
		mobileNo1 = get_ngformValue_template('AlternateContactDetails_MobileNo1');
	}
	var mobNo2 = get_ngformValue_template('AlternateContactDetails_MobNo2');
	var residenceNo = get_ngformValue_template('AlternateContactDetails_ResidenceNo');
	var homeCOuntryNo = get_ngformValue_template('AlternateContactDetails_HomeCOuntryNo');
	var OfficeNo = get_ngformValue_template('AlternateContactDetails_OfficeNo');
	var mobileNo1_part1="";
	var mobNo2_part1="";
	var residenceNo_part1 = "";
	var homeCOuntryNo_part1 = "";
	var OfficeNo_part1 = "";
	var mobileNo1_part2="";
	var mobNo2_part2="";
	var residenceNo_part2 = "";
	var homeCOuntryNo_part2 = "";
	var OfficeNo_part2 = "";
	if(getNGValue('cmplx_Customer_EmiratesID')!=''){
	      emirateIDExpiry = get_ngformValue_template('cmplx_Customer_EmirateIDExpiry');	
	
	}
	else if(getNGValue('cmplx_Customer_PAssportNo')!=''){
	  passPortExpiry = get_ngformValue_template('cmplx_Customer_PassPortExpiry');
	}
	if(docName=="Application_Form_Islamic" || docName=="Application_Form"){
		 currentDate = currentDate.replace("/","");
		 currentDate = currentDate.replace("/","");
		 doj = doj.replace("/","");
		 doj = doj.replace("/","");
		 dateOfLeaving = dateOfLeaving.replace("/","");
		 dateOfLeaving = dateOfLeaving.replace("/","");
		 emirateIDExpiry = emirateIDExpiry.replace("/","");
		 emirateIDExpiry = emirateIDExpiry.replace("/","");
		 passPortExpiry = passPortExpiry.replace("/","");
		 passPortExpiry = passPortExpiry.replace("/","");
		 expDateSup1 = expDateSup1.replace("/","");
		 expDateSup1 = expDateSup1.replace("/","");
		 expDateSup2 = expDateSup2.replace("/","");
		 expDateSup2 = expDateSup2.replace("/","");
	}	
	//changes done by shivang for Islamic App form changes ends
	var attrbList = "";
	var pname = 'DocTemplateRLOS';
	var wparams='';
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	//attrbList+=rlos_gurantor(attrbList);
	attrbList+=rlosloan_template(attrbList,"P");
	attrbList+=BT_rlos(attrbList);
	attrbList+=IM_rlos(attrbList);
	attrbList+=get_Reference_Detail(attrbList);
	//change by saurabh for CR on 30/5/19.
	attrbList+=get_NoTin_Detail(attrbList);
	//saurabh. 4/3/19 point 2.
	//changes done by shivang for Islamic App form changes starts
	attrbList += "&<customer_title>&" + get_ngformText_template('cmplx_Customer_Title');
	attrbList += "&<Current_date_time>&" + currentDate;
	if(window.pname=='CreditCard'|| window.pname=='CC'){
		attrbList += "&<Current_date_time_dec>&" + get_ngformValue_template('CurrentDateLabel');
        attrbList += "&<WIname>&" + get_ngformValue_template('parent_WIName');
		attrbList += "&<customer_name>&" + getNGValue('cmplx_Customer_FirstNAme')+' '+getNGValue('cmplx_Customer_MiddleNAme')+' '+		 getNGValue('cmplx_Customer_LastNAme');
	}
	else{
		attrbList += "&<Current_date_time_dec>&" + get_ngformValue_template('lbl_curr_date_val');
		attrbList += "&<WIname>&" + get_ngformValue_template('WI_name');
		if(docName=="Application_Form_Islamic" || docName=="Application_Form"){
			attrbList += "&<customer_name>&" + get_ngformValue_template('CUSTOMERNAME');
		}
		else{
				attrbList += "&<customer_name>&" + get_ngformText_template('cmplx_Customer_Title')+ ' ' + get_ngformValue_template('CUSTOMERNAME');
			}
	}
	
	//changes done by shivang for Islamic App form changes ends
	attrbList += "&<PAssportNo>&" + get_ngformValue_template('cmplx_Customer_PAssportNo');
	// changes done by disha for Generate Template on 17-10-18 start
	attrbList += "&<Customer_cifno>&" + get_ngformValue_template('cmplx_Customer_CIFNo');
	// changes done by disha for Generate Template on 17-10-18 end
	//changes done by shivang for Islamic App form changes starts,SC App form Generation
	//attrbList += "&<Current_date_time>&" + get_ngformValue_template('lbl_curr_date_val');
	
attrbList += "&<gender>&" + get_ngformText_template('cmplx_Customer_gender');  
attrbList += "&<MArtialStatus>&" + get_ngformText_template('cmplx_Customer_MAritalStatus');	
//saurabh. 4/3/19 point 5.
if(get_ngformGridColumn_template('cmplx_Product_cmplx_ProductGrid',0,6)!='Salaried@10' && get_ngformGridColumn_template('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed@10'){
    attrbList += "&<EmploymentType>&" + "Other@10";
	attrbList += "&<EmploymentType1>&" + get_ngformGridColumn_template('cmplx_Product_cmplx_ProductGrid',0,6);
}
else{
	attrbList += "&<EmploymentType>&" + get_ngformGridColumn_template('cmplx_Product_cmplx_ProductGrid',0,6);
}
//change by saurabh for CR on 30/5/19.
attrbList += "&<empStatusPrimary>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatus');	

if(getNGValue('cmplx_IncomeDetails_Basic').indexOf(".00")>-1)
{
	attrbList += "&<Basic>&" + get_ngformValue_template('cmplx_IncomeDetails_Basic');
}
else 
{
     attrbList += "&<Basic>&" + getNGValue('cmplx_IncomeDetails_Basic') + ".00" + "@10";
}	
	

attrbList += "&<takeamount>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_TakeoverAMount');	

attrbList += "&<takebank>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_takeoverBank');	

attrbList += "&<housing>&" + get_ngformValue_template('cmplx_IncomeDetails_housing');	
attrbList += "&<transport>&" + get_ngformValue_template('cmplx_IncomeDetails_transport');	
attrbList += "&<CostOfLiving>&" + get_ngformValue_template('cmplx_IncomeDetails_CostOfLiving');	
attrbList += "&<FixedOT>&" + get_ngformValue_template('cmplx_IncomeDetails_FixedOT');	
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')!='Self Employed'){
attrbList += "&<grossSal>&" + get_ngformValue_template('cmplx_IncomeDetails_grossSal');
}else{attrbList += "&<grossSal>&@10";}
attrbList += "&<other>&" + get_ngformValue_template('cmplx_IncomeDetails_Other');

attrbList += "&<MotherName>&" + get_ngformValue_template('cmplx_Customer_MotherName');	
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')!='Self Employed'){
	if(get_ngformValue_template('cmplx_IncomeDetails_SalaryXferToBank')=='Y@10')
	{
		attrbList += "&<Salary_transfer>&" + "YES@10";
	}
	else if(get_ngformValue_template('cmplx_IncomeDetails_SalaryXferToBank')=='N@10')
	{
		attrbList += "&<Salary_transfer>&" + "NO@10";
	}
	else{
    attrbList += "&<Salary_transfer>&" + get_ngformValue_template('cmplx_IncomeDetails_SalaryXferToBank');
	}
}else{ attrbList += "&<Salary_transfer>&" +"@10";}
//attrbList += "&<outstanding>&" + get_ngformValue_template('Outsatanding_Amount');
//attrbList += "&<obligations>&" + get_ngformValue_template('Consider_For_Obligations');	

attrbList += "&<CountryOFResidence>&" + get_ngformText_template('cmplx_Customer_COuntryOFResidence');
attrbList += "&<CountryOfResidence>&" + get_ngformText_template('cmplx_Customer_COuntryOFResidence');
attrbList += "&<TotalFinalLimit>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalLimit');

//saurabh. 4/3/19 point 4.
attrbList += "&<YearsInUAE>&" + get_ngformValue_template('cmplx_Customer_yearsInUAE');
//changes by saurabh for Id doc for new application form template
//changes done by shivang for Islamic App form changes starts
	if(getNGValue('cmplx_Customer_EmiratesID')!=''){
	attrbList += "&<DocType>&" +"Emirates ID@10";	
	attrbList += "&<id_doc>&" + get_ngformValue_template('cmplx_Customer_EmiratesID');	
	//changes by saurabh for application form CR. on 30/5/19.
	attrbList += "&<expDatePrimary>&" + emirateIDExpiry;	
	
	}
	else if(getNGValue('cmplx_Customer_PAssportNo')!=''){
	attrbList += "&<DocType>&" +"Passport@10";
	attrbList += "&<id_doc>&" + get_ngformValue_template('cmplx_Customer_PAssportNo');	
	attrbList += "&<expDatePrimary>&" + passPortExpiry;
	}
	//changes done by shivang for Islamic App form changes ends
	else{
	attrbList += "&<DocType>&@10";
	attrbList += "&<id_doc>&@10";	
	attrbList += "&<expDatePrimary>&@10";
	}
var Query="DesignationCheck";
wparams="Code=="+com.newgen.omniforms.formviewer.getNGValue('cmplx_EmploymentDetails_Designation');
var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var DB_Query = CallAjax(jspName,params);
DB_Query = trimStr(DB_Query);
var fields = DB_Query.split('#');
if(fields[0].indexOf("ERROR")>-1 || fields[0].indexOf("NODATAFOUND")>-1)
{
	fields[0]='';
}
var retVal = fields[0];
attrbList += "&<Designation>&" + retVal +"@10";

var Query = "AccountNoforCustomer";
var wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');

var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var AcctNo = CallAjax(jspName,params);
var fields = AcctNo.split('#');
var acctnumber = fields[0];
if(acctnumber.indexOf("NODATA")>-1 || acctnumber.indexOf("ERROR")>-1){
	acctnumber='0';
}
attrbList += "&<Account_Number>&" + acctnumber + '@10';

//saurabh . 4/3/19 point 17.
var CTQuery="CardTypesListForAppForm";
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
var	params="Query="+CTQuery+"&wparams="+wparams+"&pname="+pname;
var CTDB_Query = CallAjax(jspName,params);
CTDB_Query = trimStr(CTDB_Query);
var fields = CTDB_Query.split('#');
var retVal = fields[0];
attrbList += "&<CardTypes>&" + retVal +"@10";

var CTQuery="AccountNoForExisting";
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
var	params="Query="+CTQuery+"&wparams="+wparams+"&pname="+pname;
var CTDB_Query = CallAjax(jspName,params);
CTDB_Query = trimStr(CTDB_Query);
var fields = CTDB_Query.split('#');
var retVal = fields[0];
if(retVal.indexOf("ERROR")>-1 || retVal.indexOf("NODATA")>-1)
{
 retVal='';
}
attrbList += "&<AcctNumber>&" + retVal +"@10";



//saurabh. 4/3/19 point 10.
if(com.newgen.omniforms.formviewer.getNGValue('cmplx_EmploymentDetails_targetSegCode')=='CAC'){
	attrbList += "&<CardIssueBank>&" + get_ngformValue_template('cmplx_EmploymentDetails_OtherBankCAC') +"@10";

	var LimitQuery="CardLimitValue";
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
	var	params="Query="+LimitQuery+"&wparams="+wparams+"&pname="+pname;
	var CTDB_Query = CallAjax(jspName,params);
	CTDB_Query = trimStr(CTDB_Query);
	var fields = CTDB_Query.split('#');
	var retVal = fields[0];
	attrbList += "&<CardLimit>&" + retVal +"@10";
}
else{
	attrbList += "&<CardIssueBank>&" +"@10";
	attrbList += "&<CardLimit>&" +"@10";
}

if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')!='Self Employed'){
attrbList += "&<EmirateOfWork>&" + get_ngformText_template('cmplx_EmploymentDetails_EmirateOfWork');
attrbList += "&<EmployerName>&" + get_ngformValue_template('cmplx_EmploymentDetails_EmpName'); 
attrbList += "&<EmployerCode>&" + get_ngformValue_template('cmplx_EmploymentDetails_EMpCode'); 
//saurabh. 4/3/19 point 11.
var flag = false;
for(var i=0;i<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');i++){
	if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,10)=='true' && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf("P-")>-1){
		attrbList += "&<PrefAddType>&" + getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) + "@10";
		flag = true;		
		break;
	}
}
if(!flag){
attrbList += "&<PrefAddType>&@10"; 
}
attrbList += "&<TotYrsEmp>&" + get_ngformValue_template('cmplx_EmploymentDetails_LOS');  
}else{attrbList += "&<EmirateOfWork>&@10"; attrbList += "&<EmployerName>&@10"; attrbList += "&<EmployerCode>&@10"; attrbList += "&<TotYrsEmp>&@10";}
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
attrbList += "&<HeadOfficeEmirate>&" + get_ngformText_template('cmplx_EmploymentDetails_HeadOfficeEmirate');
}else{attrbList += "&<HeadOfficeEmirate>&@10";}
//changes done by shivang for Islamic App form changes
attrbList += "&<DOJ>&" + doj;
//saurabh. 4/3/19. point 8.
//changes done by shivang for Islamic App form changes
attrbList += "&<DOL>&" + dateOfLeaving;

attrbList += "&<JobConfirmed>&" + get_ngformValue_template('cmplx_EmploymentDetails_JobConfirmed');

//changes by saurabh on 14th Nov.
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
attrbList += "&<CompanyName>&" + getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,4);
attrbList += "&<BusinessStartDate>&" + getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,14).replaceAll("/","").substring(0,4)+getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,14).replaceAll("/","").substring(6,8);
attrbList += "&<EmirateOfWorkself>&" + getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,17);
attrbList += "&<HeadOfficeEmirateself>&" + getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,18);
attrbList += "&<BusinessAccNo>&" + "";
}
else{
attrbList += "&<EmirateOfWorkself>&" + '@10';
attrbList += "&<HeadOfficeEmirateself>&" + "@10";
}

//change end.

//saurabh. 4/3/19 point 9.
attrbList += "&<Accomodation>&" + get_ngformValue_template('cmplx_IncomeDetails_Accomodation');
	if(window.pname=='CreditCard'|| window.pname=='CC'){
		attrbList += "&<SMSOptOut>&" + get_ngformValue_template('cmplx_CardDetails_SMS_Output');
		attrbList += "&<cardEmbossName>&" + get_ngformValue_template('cmplx_CardDetails_cardEmbossing_name');
	}
	else{
		attrbList += "&<SMSOptOut>&" + get_ngformValue_template('cmplx_CardDetails_SMSOptOut');
		attrbList += "&<cardEmbossName>&" + get_ngformValue_template('cmplx_CardDetails_CardEmbossingName');
	}

	
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')!='Self Employed'){
	attrbList += "&<SalaryDay>&" + addZero(parseInt(get_ngformValue_template('cmplx_IncomeDetails_SalaryDay').replace("@10","")));
}else{attrbList += "&<SalaryDay>&@10";}
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')!='Self Employed'){
	attrbList += "&<DurationOfBanking>&" + get_ngformValue_template('cmplx_IncomeDetails_DurationOfBanking');
}else{attrbList += "&<DurationOfBanking>&@10";}
	attrbList += "&<AvgBalFreq>&" + get_ngformValue_template('cmplx_IncomeDetails_AvgBalFreq');
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
	attrbList += "&<AvgBal>&" + get_ngformValue_template('cmplx_IncomeDetails_AvgBal');
	}else{attrbList += "&<AvgBal>&@10";}
	
	attrbList += "&<CreditTurnoverFreq>&" + get_ngformValue_template('cmplx_IncomeDetails_CreditTurnoverFreq');
	if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
	attrbList += "&<CredTurnover>&" + get_ngformValue_template('cmplx_IncomeDetails_CredTurnover');
	}else{attrbList += "&<CredTurnover>&@10";}
	attrbList += "&<AvgCredTurnoverFreq>&" + get_ngformValue_template('cmplx_IncomeDetails_AvgCredTurnoverFreq');
	if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
	attrbList += "&<AvgCredTurnover>&" + get_ngformValue_template('cmplx_IncomeDetails_AvgCredTurnover');
	}else{attrbList += "&<AvgCredTurnover>&@10";}
	if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
	attrbList += "&<AnnualRentFreq>&" + get_ngformValue_template('cmplx_IncomeDetails_AnnualRentFreq');
	}else{attrbList += "&<AnnualRentFreq>&@10";}
attrbList += "&<AnnualRent>&" + get_ngformValue_template('cmplx_IncomeDetails_AnnualRent');

attrbList += "&<Overtime_Month1>&" + get_ngformValue_template('cmplx_IncomeDetails_Overtime_Month1');
attrbList += "&<Overtime_Month2>&" + get_ngformValue_template('cmplx_IncomeDetails_Overtime_Month2');
attrbList += "&<Overtime_Month3>&" + get_ngformValue_template('cmplx_IncomeDetails_Overtime_Month3');
attrbList += "&<Commission_Month1>&" + get_ngformValue_template('cmplx_IncomeDetails_Commission_Month1');
attrbList += "&<Commission_Month2>&" + get_ngformValue_template('cmplx_IncomeDetails_Commission_Month2');
attrbList += "&<Commission_Month3>&" + get_ngformValue_template('cmplx_IncomeDetails_Commission_Month3');
attrbList += "&<PhoneAllow_Month1>&" + get_ngformValue_template('cmplx_IncomeDetails_PhoneAllow_Month1');
attrbList += "&<PhoneAllow_Month2>&" + get_ngformValue_template('cmplx_IncomeDetails_PhoneAllow_Month2');
attrbList += "&<PhoneAllow_Month3>&" + get_ngformValue_template('cmplx_IncomeDetails_PhoneAllow_Month3');
attrbList += "&<serviceAllow_Month1>&" + get_ngformValue_template('cmplx_IncomeDetails_serviceAllow_Month1');
attrbList += "&<serviceAllow_Month2>&" + get_ngformValue_template('cmplx_IncomeDetails_serviceAllow_Month2');
attrbList += "&<serviceAllow_Month3>&" + get_ngformValue_template('cmplx_IncomeDetails_serviceAllow_Month3');
attrbList += "&<Bonus_Month1>&" + get_ngformValue_template('cmplx_IncomeDetails_Bonus_Month1');
attrbList += "&<Bonus_Month2>&" + get_ngformValue_template('cmplx_IncomeDetails_Bonus_Month2');
attrbList += "&<Bonus_Month3>&" + get_ngformValue_template('cmplx_IncomeDetails_Bonus_Month3');
attrbList += "&<Flying_Month1>&" + get_ngformValue_template('cmplx_IncomeDetails_Flying_Month1');
attrbList += "&<Flying_Month2>&" + get_ngformValue_template('cmplx_IncomeDetails_Flying_Month2');
attrbList += "&<Flying_Month3>&" + get_ngformValue_template('cmplx_IncomeDetails_Flying_Month3');
attrbList += "&<Other_Month1>&" + get_ngformValue_template('cmplx_IncomeDetails_Other_Month1');
attrbList += "&<Other_Month2>&" + get_ngformValue_template('cmplx_IncomeDetails_Other_Month2');
attrbList += "&<Other_Month3>&" + get_ngformValue_template('cmplx_IncomeDetails_Other_Month3');
//changes done by shivang for Islamic App form changes starts
if(docName=="Application_Form_Islamic" || docName=="Application_Form"){
	if(mobileNo1!=''){
	mobileNo1_part1 = mobileNo1.substring(0,5);
	mobileNo1_part2 = mobileNo1.substring(5,15);
	var temp = mobileNo1_part2.split("@");
		mobileNo1_part2 = temp[0];
	}
	if(mobNo2!=''){
	mobNo2_part1 = mobNo2.substring(0,5);
	mobNo2_part2 = mobNo2.substring(5,15);
	var temp = mobNo2_part2.split("@");
		mobNo2_part2 = temp[0];
	}
	if(residenceNo!=''){
	residenceNo_part1 = residenceNo.substring(0,5);
	residenceNo_part2 = residenceNo.substring(5,15);
	var temp = residenceNo_part2.split("@");
		residenceNo_part2 = temp[0];
	}
	if(homeCOuntryNo!=''){
	homeCOuntryNo_part1 = homeCOuntryNo.substring(0,5);
	homeCOuntryNo_part2 = homeCOuntryNo.substring(5,15);
	var temp = homeCOuntryNo_part2.split("@");
		homeCOuntryNo_part2 = temp[0];
	}
	if(OfficeNo!=''){
	OfficeNo_part1 = OfficeNo.substring(0,5);
	OfficeNo_part2 = OfficeNo.substring(5,15);
	var temp = OfficeNo_part2.split("@");
		OfficeNo_part2 = temp[0];
	}
	
	attrbList += "&<MobileNo1_part1>&" + mobileNo1_part1;
	attrbList += "&<MobileNo1_part2>&" + mobileNo1_part2;
	attrbList += "&<MobileNo2_part1>&" + mobNo2_part1;
	attrbList += "&<MobileNo2_part2>&" + mobNo2_part2;
	attrbList += "&<ResidenceNo_part1>&" + residenceNo_part1;
	attrbList += "&<ResidenceNo_part2>&" + residenceNo_part2;
	attrbList += "&<HomeCOuntryNo_part1>&" + homeCOuntryNo_part1;
	attrbList += "&<HomeCOuntryNo_part2>&" + homeCOuntryNo_part2;
	attrbList += "&<OfficeNo_part1>&" + OfficeNo_part1;
	attrbList += "&<OfficeNo_part2>&" + OfficeNo_part2;
	
}else{
attrbList += "&<MobileNo1>&" + get_ngformValue_template('AlternateContactDetails_MobileNo1');
attrbList += "&<MobileNo2>&" + get_ngformValue_template('AlternateContactDetails_MobNo2');
attrbList += "&<ResidenceNo>&" + get_ngformValue_template('AlternateContactDetails_ResidenceNo');
attrbList += "&<HomeCOuntryNo>&" + get_ngformValue_template('AlternateContactDetails_HomeCOuntryNo');
attrbList += "&<OfficeNo>&" + get_ngformValue_template('AlternateContactDetails_OfficeNo');
}
//changes done by shivang for Islamic App form changes ends
attrbList += "&<Email2>&" + get_ngformValue_template('AlternateContactDetails_Email2');
attrbList += "&<Email1>&" + get_ngformValue_template('AlternateContactDetails_Email1');

//changes done For AirArabiaCardEmail 
attrbList += "&<airArabiacardEmail>&" + getNGValue('AltContactDetails_Text7');

attrbList += "&<yearsinUAE>&" + get_ngformValue_template('cmplx_Customer_yearsInUAE');


if(com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_GuarantorDetails_cmplx_GuarantorGrid')>0){
	attrbList += "&<guarantor_Name>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,7) + ' '+get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,8) + ' '+ get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,9);
	attrbList += "&<Guarantor_passport>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,5);
}
else{attrbList += "&<guarantor_Name>&@10"; attrbList += "&<Guarantor_passport>&@10"; }
attrbList+=rlosloan_template(attrbList,"G");

	if(window.pname=='CreditCard' ||window.pname=='CC'){
		attrbList+= getSupplementaryCardDetail(attrbList,"SupplementCardDetails_cmplx_supplementGrid","CreditCard");
		attrbList+= getReferenceNfatcaDetail(attrbList,"cmplx_RefDetails_cmplx_Gr_ReferenceDetails","CreditCard",docName);
	}
	else{
	attrbList += "&<custName_Supp>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,0) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,1) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,2);
//attrbList += "&<middle_sup>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,1);
//attrbList += "&<lasu>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,2);
attrbList += "&<card_embossing>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,11);

attrbList += "&<passport_sup>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,3);
//changes done by shivang fro fromatting date in islamic form
attrbList += "&<supp1_mothername>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,9);
attrbList += "&<supp2_mothername>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,9);
attrbList += "&<supp1_approvedlimit>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,26);
attrbList += "&<supp2_approvedlimit>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,26);
attrbList += "&<supp1_EmiratedID>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,21);
attrbList += "&<supp2_EmiratedID>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,21);
//country of residence
attrbList += "&<countryofresidence1>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,7);
attrbList += "&<countryofresidence2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,7);


if(get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,21)=="@10" || get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,21) == ""){
	attrbList += "&<supp1_EmiratedID>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,3);
}

if(get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,21)=="@10" || get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,21) == ""){
	attrbList += "&<supp2_EmiratedID>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,3);
}
var supAEResiudent = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,7);
if(supAEResiudent=="AE@10")
{
	attrbList += "&<supp1_uaeResident>&" + "AE";
}
else
{
	if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No@10"){
	attrbList += "&<supp1_uaeResident>&" + "NO";
	}
}
var supAEResiudent1 = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,7);
if(supAEResiudent1=="AE@10")
{
	attrbList += "&<supp2_uaeResident>&" + "AE";
}
else
{	
	if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No@10"){
	attrbList += "&<supp2_uaeResident>&" + "NO";
	}
}

if(get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,3)=="@10" || get_ngformGridColumn_template(          "SupplementCardDetails_cmplx_SupplementGrid",0,3)==""){
	attrbList += "&<supp1_uaeResident>&" +"" +"@10";
}
if(get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,3)=="@10" || get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,3)==""){
	attrbList += "&<supp2_uaeResident>&" +"" +"@10";
}
//attrbList += "&<supp2_approvedlimit>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,7);
attrbList += "&<expDateSup1>&" + expDateSup1;
var relationship1= '';
relationship1 = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,10);
if(relationship1 == 'SON@10' || relationship1 == 'DAU@10')
{
	relationship1="Child";
}
else if(relationship1 == 'WIF@10' || relationship1 == 'HUS@10')
{
	relationship1="Spouse";
}
else if(relationship1 == 'SEL@10')
{
	relationship1="Self";
}
else if(relationship1 == 'MOT@10' || relationship1 == 'FAT@10')
{
	relationship1="Parent";
}
else if(relationship1 == 'SIS@10' || relationship1 == 'BRO@10')
{
	relationship1="Sibling";
}
else
{
	if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No@10"){
	relationship1="Others";
	}
}		
attrbList += "&<Relationship1>&" + relationship1;
var relationship2= '';
relationship2 = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,10);
if(relationship2 == 'SON@10' || relationship2 == 'DAU@10')
{
	relationship2="Child";
}
else if(relationship2 == 'WIF@10' || relationship2 == 'HUS@10')
{
	relationship2="Spouse";
}
else if(relationship2 == 'SEL@10')
{
	relationship2="Self";
}
else if(relationship2 == 'MOT@10' || relationship2 == 'FAT@10')
{
	relationship2="Parent";
}
else if(relationship2 == 'SIS@10' || relationship2 == 'BRO@10')
{
	relationship2="Sibling";
}
else
{
	if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")!="No@10"){
	relationship2="Others";
	}
}		
attrbList += "&<Relationship2>&" + relationship2;
// fetching SupplementCardDetails designation
var Query="DesignationCheck";
if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="Yes" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="YES"
 || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")=="Yes"){
if(get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,23).indexOf("@")>-1){
wparams="Code=="+get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,23).split("@")[0];
}
else{
	wparams="Code=="+get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,23);
}
}
var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var DB_Query = CallAjax(jspName,params);
DB_Query = trimStr(DB_Query);
var fields = DB_Query.split('#');
var supp_Design1 = fields[0];
if(supp_Design1.indexOf("ERROR")>-1 || supp_Design1.indexOf("NODATA")>-1)
{
	supp_Design1='';
}
attrbList += "&<supp_Designation1>&" + supp_Design1 +"@10";

var Query="DesignationCheck";
if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="Yes" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="YES" 
 || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")=="Yes"){
if(get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,23).indexOf("@")>-1){	
wparams="Code=="+get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,23).split("@")[0];
}
else{
	wparams="Code=="+get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,23);
}
}
var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var DB_Query = CallAjax(jspName,params);
DB_Query = trimStr(DB_Query);
var fields = DB_Query.split('#');
var supp_Design2 = fields[0];
if(supp_Design2.indexOf("ERROR")>-1 || supp_Design2.indexOf("NODATA")>-1)
{
	supp_Design2='';
}
attrbList += "&<supp_Designation2>&" + supp_Design2 +"@10";


//changes done by shivang for Islamic App form changes starts
 var mobileSup = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,6);
 var mbl = get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,6);
 var mbl_part1= "";
 var mbl_part2 = "";
 var mobileSup_part1 = "";
 var mobileSup_part2 = "";
 var friend1_Mob = "";
 var Friend1_Mobile_part1="";
 var Friend1_Mobile_part2="";
 var friend2_Mob = "";
 var Friend2_Mobile_part1= "";
 var Friend2_Mobile_part2= "";
 
 if(docName=="Application_Form_Islamic" || docName=="Application_Form"){
	//changes done by shivang 
	 if(mobileSup!=''){
		 mobileSup_part1 = mobileSup.substring(0,5);
	    mobileSup_part2 = mobileSup.substring(5,15); 
		var temp = mobileSup_part2.split("@");
		mobileSup_part2 = temp[0];
	 }
	 attrbList += "&<mobile_sup_part1>&" + mobileSup_part1;
	 attrbList += "&<mobile_sup_part2>&" + mobileSup_part2;	 
	 if(mbl!=''){
		 mbl_part1 = mbl.substring(0,5);
	    mbl_part2 = mbl.substring(5,15); 
		var temp = mbl_part2.split("@");
		mbl_part2 = temp[0];
	 }
	 attrbList += "&<mbl_part1>&" + mbl_part1;
	 attrbList += "&<mbl_part2>&" + mbl_part2;	 
	 //fetching details for Reference @ code changes done by shivang starts
	 attrbList += "&<Friend1_Name>&" + get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",0,0);	 
	 friend1_Mob = get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",0,1);
	 if(friend1_Mob!=''){
		 Friend1_Mobile_part1 = friend1_Mob.substring(0,5);
	    Friend1_Mobile_part2 = friend1_Mob.substring(5,15); 
		var temp = Friend1_Mobile_part2.split("@");
		Friend1_Mobile_part2 = temp[0];
	 }
	 attrbList += "&<Friend1_Mobile_part1>&" + Friend1_Mobile_part1;
	 attrbList += "&<Friend1_Mobile_part2>&" + Friend1_Mobile_part2;
	 attrbList += "&<Friend1_Relation>&" + get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",0,3);
	 
	 
	 attrbList += "&<Friend2_Name>&" + get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",1,0);	 
	 friend2_Mob = get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",1,1);
	 if(friend2_Mob!=''){
		 Friend2_Mobile_part1 = friend2_Mob.substring(0,5);
	    Friend2_Mobile_part2 = friend2_Mob.substring(5,15); 
		var temp = Friend2_Mobile_part2.split("@");
		Friend2_Mobile_part2 = temp[0];
	 }
	 attrbList += "&<Friend2_Mobile_part1>&" + Friend2_Mobile_part1;
	 attrbList += "&<Friend2_Mobile_part2>&" + Friend2_Mobile_part2;
	 attrbList += "&<Friend2_Relation>&" + get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",1,3);
	 //fetching details for Reference @ code changes done by shivang end
	 
	 //fetching Mailing details by shivang starts
	 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid") >= 0){
		 var residence_house = "";
		 var residence_city = "";
		 var residence_country = "";
		 var residence_zipcode = "";
		 var mailingAdd_house = "";
		 var mailingAdd_city = "";
		 var mailingAdd_country = "";
		 var mailingAdd_zipcode = "";
		 var pname = "CCTemplateData";
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid"); i++)              
               {
                    var value_type= get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
					var mailingAdd= get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,10);
					                   
                    if(mailingAdd =='YES@10' || mailingAdd =='Yes@10' || mailingAdd =='YES')					
                     {							
                            mailingAdd_house = get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,3); 
			                mailingAdd_city = com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
			                var Query="ResidenceCity";
							wparams="Code=="+mailingAdd_city;
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var mailingAdd_city = fields[0];	
							if(mailingAdd_city.indexOf('NODATA')>-1 ||mailingAdd_city.indexOf('ERROR')>-1){
								mailingAdd_city='';
							}				
							mailingAdd_country =com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
							var Query1 = "ResidenceCountry";
							wparams="Code=="+mailingAdd_country;
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var mailingAdd_country = fields1[0];
							if(mailingAdd_country.indexOf('NODATA')>-1 ||mailingAdd_country.indexOf('ERROR')>-1){
								mailingAdd_country='';
							}
					        mailingAdd_zipcode = get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,1);							 
					}
					if(value_type =='RESIDENCE')						
                     {							
							//attrbList += "&<MailingAdd_building_name>&" + get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
                            residence_house = get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,3); 
			                 residence_city = com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
			                 var Query="ResidenceCity";
							wparams="Code=="+residence_city;
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var residence_city = fields[0];	
							if(residence_city.indexOf('NODATA')>-1 ||residence_city.indexOf('ERROR')>-1){
								residence_city='';
							}								
							residence_country =com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
							var Query1 = "ResidenceCountry";
							wparams="Code=="+mailingAdd_country;
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var residence_country = fields1[0];
							if(residence_country.indexOf('NODATA')>-1 ||residence_country.indexOf('ERROR')>-1){
								residence_country='';
							}
					        residence_zipcode=  get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,1);							 
					}
                      
        }
			if( (residence_house == mailingAdd_house) && (residence_city == mailingAdd_city) && (residence_country == mailingAdd_country) 
					&& (residence_zipcode == mailingAdd_zipcode)){
						attrbList += "&<MailingAdd_building_name>&" + mailingAdd_house; 
			                attrbList += "&<MailingAdd_city>&" + mailingAdd_city; 							
							 attrbList += "&<MailingAdd_country>&" + mailingAdd_country;
							attrbList += "&<MailingAdd_zip_code>&" + mailingAdd_zipcode;
						
					}
						else{	attrbList += "&<MailingAdd_building_name>&" + mailingAdd_house; 
			                attrbList += "&<MailingAdd_city>&" + mailingAdd_city; 							
							 attrbList += "&<MailingAdd_country>&" + mailingAdd_country;
							attrbList += "&<MailingAdd_zip_code>&" + mailingAdd_zipcode;
						}
    }
	 //fetching Mailing details by shivang end
	 //FATCA changes done by shivang
	 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_FATCA_cmplx_GR_FatcaDetails") >= 0){
			var fatcaCheck="";
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_FATCA_cmplx_GR_FatcaDetails"); i++)              
               {
                    var value_type= get_ngformGridColumn_template("cmplx_FATCA_cmplx_GR_FatcaDetails",i,13);
					        value_type = value_type.substring(0,1);
                    if(value_type =='P')					
                     {							
                            fatcaCheck = get_ngformGridColumn_template("cmplx_FATCA_cmplx_GR_FatcaDetails",i,0);
							if(fatcaCheck == "O@10")
							{
								fatcaCheck= "YES";
								attrbList += "&<fatcaCheck>&" + fatcaCheck;
							break;
							}
							else{
								fatcaCheck = "NO";
								attrbList += "&<fatcaCheck>&" + fatcaCheck;
							}
					}
					
                      
        }
	 //FATCA changes done by shivang

    }
	 
	 
  }else{
	attrbList += "&<mobile_sup>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,6);
 }
 //changes done by shivang for Islamic App form changes end
 
 //changes Done by Shivang starts for murhaba_file_Date starts
  /*var Query2="MurahbaFileDate";
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
	var	params="Query="+Query2+"&wparams="+wparams+"&pname="+pname;
	var Murahba_Query = CallAjax(jspName,params);
	Murahba_Query = trimStr(Murahba_Query);
	var fields = Murahba_Query.split('#');
	 var Murahba_Date = fields[0];
	if(Murahba_Date!=''){
	var Murahba_year = Murahba_Date.substring(0,4);
	var Murahba_month = Murahba_Date.substring(4,6);
	var Murahba_day = Murahba_Date.substring(6,8);
	Murahba_Date = Murahba_day + '/' + Murahba_month + '/' + Murahba_month;
	}
	else{
		Murahba_Date = '';
	}
	attrbList += "&<murhaba_file_Date>&" + Murahba_Date +"@10";
	*/
	//changes Done by Shivang murhaba_file_Date ends
	
	attrbList += "&<custName_Supp2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,0) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,1) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,2);
	
	attrbList += "&<card_embossing2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,11);
	attrbList += "&<passport_sup2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,3);
	attrbList += "&<expDateSup2>&" + expDateSup2;

	}

//changes done by shivang for self-supplemantry card details on 19/11

if(getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid') < 2 && (get_ngformValue_template('cmplx_CardDetails_SelfSupp_required')=='Yes@10' || 
	get_ngformValue_template('cmplx_CardDetails_SelfSupp_required')=='Yes')){
		
		attrbList += "&<Relationship2>&" + 'Self';
		attrbList += "&<supp2_approvedlimit>&" + get_ngformValue_template('cmplx_CardDetails_SelfSupp_Limit');
		attrbList += "&<custName_Supp2>&" + get_ngformValue_template('CUSTOMERNAME');
		attrbList += "&<card_embossing2>&" + get_ngformValue_template('cmplx_CardDetails_Self_card_embossing');
	}
	
attrbList += "&<first_gr>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,6);
attrbList += "&<middle_gr>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,7);
attrbList += "&<last_gr>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,8);
attrbList += "&<passport_gr>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,4);
attrbList += "&<mobile_gr>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,4);

attrbList += "&<mbl>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,6);
attrbList += "&<product_type_app>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,1);
	attrbList += "&<sms>&" + get_ngformText_template('cmplx_CardDetails_SMSOptOut_cont');
	attrbList += "&<Loan_amt>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalLimit');
	
	attrbList += "&<monthly>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_EMI');
	
	attrbList += "&<InterestRate>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_InterestRate');
	attrbList += "&<loan_fees>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_LPFAmount');
	attrbList += "&<Tenor_l>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_Tenor');
	attrbList += "&<first>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FirstRepayDate');
	
	if(window.pname=='CreditCard' ||window.pname=='CC'){
		attrbList += "&<PILname>&" + get_ngformValue_template('cmplx_Customer_LastNAme');
		attrbList += "&<PIFname>&" + get_ngformValue_template('cmplx_Customer_FirstNAme');
		attrbList += "&<PIMname>&" + get_ngformValue_template('cmplx_Customer_MiddleNAme');
	}
	else{
		attrbList += "&<PILname>&" + get_ngformValue_template('cmplx_Customer_LAstNAme');
		attrbList += "&<PIFname>&" + get_ngformValue_template('cmplx_Customer_FIrstNAme');
		attrbList += "&<PIMname>&" + get_ngformValue_template('cmplx_Customer_MiddleName');
	}
	attrbList += "&<PIDOB>&" + get_ngformValue_template('cmplx_Customer_DOb');
	
	var flag = false;
	//changes by shivang for finding description by code starts 
	var townOfBirth = "";
	var pname = "CCTemplateData";
	for(var i=0;i<getLVWRowCount('cmplx_OECD_cmplx_GR_OecdDetails');i++){
		if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,8).indexOf("P-")>-1){
		                    var Query="ToB";
							wparams="Code=="+getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,3);
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var townofbirth = fields[0];	
							if(townofbirth.indexOf('NODATA')>-1 ||townofbirth.indexOf('ERROR')>-1){
								townofbirth='';
							}				
			                attrbList += "&<PITOB>&"+ townofbirth + "@10";
			//attrbList += "&<PITOB>&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,3) + "@10";
			//townOfBirth = getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,3) + "@10";
			                var Query1 = "ResidenceCountry";
							wparams="Code=="+getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,2);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var cob = fields1[0];
							if(cob.indexOf('NODATA')>-1 ||cob.indexOf('ERROR')>-1){
								cob='';
							}
			                attrbList += "&<PICOB>&" + cob + "@10";
			//attrbList += "&<CTR>&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,4) + "@10";
			//attrbList += "&<TINNo>&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,5) + "@10";
			flag = true;		
			break;
		}
	}
	if(!flag){
	attrbList += "&<PITOB>&"  + "@10";
		attrbList += "&<PICOB>&" + "@10";
		//attrbList += "&<CTR>&" +  "@10";
		//attrbList += "&<TINNo>&"  + "@10";
	}
	/*if(docName=="Application_Form_Islamic"){
		var Query2="TownOfBirth";
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	wparams="WiName=="+townOfBirth;
	var	params="Query="+Query2+"&wparams="+wparams+"&pname="+pname;
	var town = CallAjax(jspName,params);
	town = trimStr(town);
	var fields = town.split('#');
	var TOB = fields[0];
	attrbList += "&<PITOB>&" + TOB +"@10";
	}
	*/
	//RakProtectEnrollment Changed
	                        var Query="RakProtectEnroll";
							wparams="Wi_Name=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var Enrollment = CallAjax(jspName,params);
							var fields = Enrollment.split('#');
							var Enrollment = fields[0];	
							if(Enrollment.indexOf('NODATA')>-1 ||Enrollment.indexOf('ERROR')>-1){
								Enrollment='';
							}				
			                attrbList += "&<RakProtectEnrollment>&"+ Enrollment + "@10";
	
	//changes by shivang for finding description by code end
	//var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var Query="Tenor";
	//var Query = "SELECT top 1 isnull(OutstandingAmt,'') as OutstandingAmt,isnull(TotalNoOfInstalments,'') as TotalNoOfInstalments,isnull(Consider_For_Obligations,'') as Consider_For_Obligations,isnull(PaymentsAmt,'') as PaymentsAmt FROM ng_rlos_cust_extexpo_LoanDetails with (nolock) WHERE Wi_Name ='"+ com.newgen.omniforms.formviewer.getNGValue('WI_name')+"' AND LoanType = 'Personal Loan' order by loanapproveddate desc";
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
	var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	
	var Tenor=CallAjax(jspName,params);
	Tenor = trimStr(Tenor);
	
	var Query1="input";
	//var Query1 = "SELECT top 1 isnull(OutstandingAmt,'') as OutstandingAmt,isnull(TotalNoOfInstalments,'') as TotalNoOfInstalments,isnull(Consider_For_Obligations,'') as Consider_For_Obligations,isnull(PaymentsAmt,'') as PaymentsAmt FROM ng_rlos_cust_extexpo_LoanDetails with (nolock) WHERE Wi_Name ='"+ com.newgen.omniforms.formviewer.getNGValue('WI_name')+"' AND LoanType = 'Car Loan' order by loanapproveddate desc";
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
	var	params1="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
	//var params1="Query1="+Query1;
	var input=CallAjax(jspName,params1);
	input = trimStr(input);
			
	if (input!="ERROR")

	{
				var fields1 = input.split('#');
				var amt_auto = fields1[0]+"@10";
				var installments_auto = fields1[1]+"@10";

				var obligtions_auto = fields1[2]+"@10";
				var emi_auto = fields1[3]+"@10";
				attrbList += "&<otustanding_auto>&" + amt_auto;
				attrbList += "&<obli_auto>&" + obligtions_auto;	
				attrbList += "&<tenor_auto>&" + installments_auto;	
				attrbList += "&<emi_auto>&" + emi_auto;	
	}
 if (Tenor!="ERROR")
  {
	var fields = Tenor.split('#');
	var amt = fields[0]+"@10";
	var installments = fields[1]+"@10";

	var obligtions = fields[2]+"@10";
	var emi = fields[3]+"@10";	

	//attrbList += "&<tenor>&" + installments;
	attrbList += "&<outstanding>&" + amt;
	attrbList += "&<obligations>&" + obligtions;	
	attrbList += "&<tenor>&" + installments;	
	attrbList += "&<emi>&" + emi;	
	}

 if (Tenor=="ERROR" || input=="ERROR")
	{
		        attrbList += "&<otustanding_auto>&"+ "@10";
				attrbList += "&<obli_auto>&"+ "@10" ;	
				attrbList += "&<tenor_auto>&"+ "@10" ;	
				attrbList += "&<emi_auto>&"+ "@10";
				 attrbList += "&<emi_car>&"+ "@10";
				attrbList += "&<tenor_car>&"+ "@10" ;	
				attrbList += "&<otustanding_car>&"+ "@10" ;	
				attrbList += "&<obli_car>&"+ "@10";
				attrbList += "&<emi>&"+ "@10";
				attrbList += "&<tenor>&"+ "@10" ;	
				attrbList += "&<outstanding>&"+ "@10" ;	
				attrbList += "&<obligations>&"+ "@10";
	}
	
	else 
	{
		        attrbList += "&<otustanding_auto>&"+ "@10";
				attrbList += "&<obli_auto>&"+ "@10" ;	
				attrbList += "&<tenor_auto>&"+ "@10" ;	
				attrbList += "&<emi_auto>&"+ "@10";
				 attrbList += "&<emi_car>&"+ "@10";
				attrbList += "&<tenor_car>&"+ "@10" ;	
				attrbList += "&<otustanding_car>&"+ "@10" ;	
				attrbList += "&<obli_car>&"+ "@10";
				attrbList += "&<emi>&"+ "@10";
				attrbList += "&<tenor>&"+ "@10" ;	
				attrbList += "&<outstanding>&"+ "@10" ;	
				attrbList += "&<obligations>&"+ "@10";
	}
	var Query2="NTBCheck";
	//var Query2 = "SELECT isnull(NTB,'') as NTB FROM NG_RLOS_EXTTABLE with (nolock) WHERE WIname ='"+ com.newgen.omniforms.formviewer.getNGValue('WI_name')+"'";
	
	/*var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
	var	params="Query="+Query2+"&wparams="+wparams+"&pname="+pname;
	var NTB_Query = CallAjax(jspName,params);
	NTB_Query = trimStr(NTB_Query);
	var fields = NTB_Query.split('#');
	var NTB = fields[0]+"@10";
	//saurabh. 4/3/19 point 1.
	if(NTB=='false@10')
		attrbList += "&<NTB>&" +"NO@10";
	else if(NTB=='true@10')
		attrbList += "&<NTB>&" + "YES@10";
	else
		attrbList += "&<NTB>&"+"@10";*/
	
	if((get_ngformValue_template('cmplx_Customer_NTB').split('@')[0]=='NO'?'YES@10':'NO@10') == 'YES@10')
	{
		attrbList += "&<NTB>&" +"NO@10";
	}
	else{
		attrbList += "&<NTB>&" + "YES@10";
	}

	//Added by Shivang for Skyward No.
	attrbList += "&<SkywardPrimary>&" + getNGValue('AlternateContactDetails_EnrollRewardsIdentifier').replace("EK","") +"@10";
	if(getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid')>0){
	attrbList += "&<SkywardSupp1>&" + getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',0,33).replace("EK","") +"@10";	
	}
	var Query="VerifiedBy";
	wparams="userid=="+com.newgen.omniforms.formviewer.getNGValue('lbl_user_name_val');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var verified = CallAjax(jspName,params);
	var fields = verified.split('#');
	var verified = fields[0];	
	if(verified.indexOf('NODATA')>-1 ||verified.indexOf('ERROR')>-1){
		verified='';
	}
	if(verified==''||verified==null){
		attrbList += "&<Verifiedby>&"+ com.newgen.omniforms.formviewer.getNGValue('lbl_user_name_val')+ "@10";
	}
	else{
		attrbList += "&<Verifiedby>&"+ com.newgen.omniforms.formviewer.getNGValue('lbl_user_name_val')+"(" + verified+ ")@10";
	}
	return attrbList;	
	
		
}

//function Added by shivang for SC App form
function getSupplementaryCardDetail(attrbList,GridName,ProcessName)
{
	var pname = 'DocTemplateRLOS';
	var wparams='';
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	if(ProcessName=='CreditCard' ||ProcessName=='CC'){
		
	attrbList += "&<custName_Supp>&" + get_ngformGridColumn_template(GridName,0,0) + ' ' + get_ngformGridColumn_template(GridName,0,1) + ' ' + get_ngformGridColumn_template(GridName,0,2);
	attrbList += "&<card_embossing>&" + get_ngformGridColumn_template(GridName,0,9);
	attrbList += "&<passport_sup>&" + get_ngformGridColumn_template(GridName,0,3);
	attrbList += "&<supp1_mothername>&" + get_ngformGridColumn_template(GridName,0,12);
	attrbList += "&<supp2_mothername>&" + get_ngformGridColumn_template(GridName,1,12);
	attrbList += "&<supp1_approvedlimit>&" + get_ngformGridColumn_template(GridName,0,28);
	attrbList += "&<supp2_approvedlimit>&" + get_ngformGridColumn_template(GridName,1,28);
	attrbList += "&<supp1_EmiratedID>&" + get_ngformGridColumn_template(GridName,0,7);
	attrbList += "&<supp2_EmiratedID>&" + get_ngformGridColumn_template(GridName,1,7);

	if(get_ngformGridColumn_template(GridName,0,7)=="@10" || get_ngformGridColumn_template(GridName,0,7) == ""){
		attrbList += "&<supp1_EmiratedID>&" + get_ngformGridColumn_template(GridName,0,3);
	}
	if(get_ngformGridColumn_template(GridName,1,7)=="@10" || get_ngformGridColumn_template(GridName,1,7) == ""){
		attrbList += "&<supp2_EmiratedID>&" + get_ngformGridColumn_template(GridName,1,3);
	}
	var supAEResiudent = get_ngformGridColumn_template(GridName,0,8);
	if(supAEResiudent=="AE@10")
	{
		attrbList += "&<supp1_uaeResident>&" + "AE";
	}
	else
	{
		if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")!="No" || com.newgen.omniforms.formviewer.	 getNGValue("cmplx_CardDetails_Supplementary_Card")!="No@10"){
		attrbList += "&<supp1_uaeResident>&" + "NO";
		}
	}
	var supAEResiudent1 = get_ngformGridColumn_template(GridName,1,8);
	if(supAEResiudent1=="AE@10")
	{
		attrbList += "&<supp2_uaeResident>&" + "AE";
	}
	else
	{	
		if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")!="No" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")!="No@10"){
		attrbList += "&<supp2_uaeResident>&" + "NO";
		}
	}

	if(get_ngformGridColumn_template(GridName,0,3)=="@10" || get_ngformGridColumn_template(GridName,0,3)==""){
		attrbList += "&<supp1_uaeResident>&" +"" +"@10";
	}
	if(get_ngformGridColumn_template(GridName,1,3)=="@10" || get_ngformGridColumn_template(GridName,1,3)==""){
		attrbList += "&<supp2_uaeResident>&" +"" +"@10";
	}

//attrbList += "&<expDateSup1>&" + expDateSup1;
	var relationship1= '';
	relationship1 = get_ngformGridColumn_template(GridName,0,13);
	if(relationship1 == 'SON@10' || relationship1 == 'DAU@10')
	{
		relationship1="Child";
	}
	else if(relationship1 == 'WIF@10' || relationship1 == 'HUS@10')
	{
		relationship1="Spouse";
	}
else if(relationship1 == 'SEL@10')
{
	relationship1="Self";
}
else if(relationship1 == 'MOT@10' || relationship1 == 'FAT@10')
{
	relationship1="Parent";
}
else if(relationship1 == 'SIS@10' || relationship1 == 'BRO@10')
{
	relationship1="Sibling";
}
else
{
	if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")!="No" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")!="No@10"){
	relationship1="Others";
	}
}		
attrbList += "&<Relationship1>&" + relationship1;
var relationship2= '';
relationship2 = get_ngformGridColumn_template(GridName,1,13);
if(relationship2 == 'SON@10' || relationship2 == 'DAU@10')
{
	relationship2="Child";
}
else if(relationship2 == 'WIF@10' || relationship2 == 'HUS@10')
{
	relationship2="Spouse";
}
else if(relationship2 == 'SEL@10')
{
	relationship2="Self";
}
else if(relationship2 == 'MOT@10' || relationship2 == 'FAT@10')
{
	relationship2="Parent";
}
else if(relationship2 == 'SIS@10' || relationship2 == 'BRO@10')
{
	relationship2="Sibling";
}
else
{
	if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")!="No" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")!="No@10"){
	relationship2="Others";
	}
}		
attrbList += "&<Relationship2>&" + relationship2;
var Query="DesignationCheck";
if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="Yes" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="YES" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")=="Yes"){
if(get_ngformGridColumn_template(GridName,0,23).indexOf("@")>-1){
wparams="Code=="+get_ngformGridColumn_template(GridName,0,31).split("@")[0];
}
else{
	wparams="Code=="+get_ngformGridColumn_template(GridName,0,31);
}
}
var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var DB_Query = CallAjax(jspName,params);
DB_Query = trimStr(DB_Query);
var fields = DB_Query.split('#');
var supp_Design1 = fields[0];
if(supp_Design1.indexOf("ERROR")>-1 || supp_Design1.indexOf("NODATA")>-1)
{
	supp_Design1='';
}
attrbList += "&<supp_Designation1>&" + supp_Design1 +"@10";

var Query="DesignationCheck";
if(com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="Yes" || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_Supplementary_Card")=="YES" 
 || com.newgen.omniforms.formviewer.getNGValue("cmplx_CardDetails_SuppCardReq")=="Yes"){
if(get_ngformGridColumn_template(GridName,1,31).indexOf("@")>-1){	
wparams="Code=="+get_ngformGridColumn_template(GridName,1,31).split("@")[0];
}
else{
	wparams="Code=="+get_ngformGridColumn_template(GridName,1,31);
}
}
var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var DB_Query = CallAjax(jspName,params);
DB_Query = trimStr(DB_Query);
var fields = DB_Query.split('#');
var supp_Design2 = fields[0];
if(supp_Design2.indexOf("ERROR")>-1 || supp_Design2.indexOf("NODATA")>-1)
{
	supp_Design2='';
}
	attrbList += "&<supp_Designation2>&" + supp_Design2 +"@10";
	attrbList += "&<custName_Supp2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,0) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,1) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,2);
	
	attrbList += "&<card_embossing2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,11);
	attrbList += "&<passport_sup2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,3);
	//attrbList += "&<expDateSup2>&" + expDateSup2;
 var mobileSup = get_ngformGridColumn_template(GridName,0,6);
 var mbl = get_ngformGridColumn_template(GridName,1,6);
 var mbl_part1= "";
 var mbl_part2 = "";
 var mobileSup_part1 = "";
 var mobileSup_part2 = "";
 var friend1_Mob = "";
 var Friend1_Mobile_part1="";
 var Friend1_Mobile_part2="";
 var friend2_Mob = "";
 var Friend2_Mobile_part1= "";
 var Friend2_Mobile_part2= "";
 
	 if(mobileSup!=''){
		 mobileSup_part1 = mobileSup.substring(0,5);
	    mobileSup_part2 = mobileSup.substring(5,15); 
		var temp = mobileSup_part2.split("@");
		mobileSup_part2 = temp[0];
	 }
	 attrbList += "&<mobile_sup_part1>&" + mobileSup_part1;
	 attrbList += "&<mobile_sup_part2>&" + mobileSup_part2;	 
	 if(mbl!=''){
		 mbl_part1 = mbl.substring(0,5);
	    mbl_part2 = mbl.substring(5,15); 
		var temp = mbl_part2.split("@");
		mbl_part2 = temp[0];
	 }
	 attrbList += "&<mbl_part1>&" + mbl_part1;
	 attrbList += "&<mbl_part2>&" + mbl_part2;		
	}
	return attrbList;
}

//function Added by shivang for SC App form
function getReferenceNfatcaDetail(attrbList,GridName,ProcessName,docName)
{
	if(ProcessName=='CreditCard' ||ProcessName=='CC'){
		var friend1_Mob = "";
		var Friend1_Mobile_part1="";
		var Friend1_Mobile_part2="";
		var friend2_Mob = "";
		var Friend2_Mobile_part1= "";
		var Friend2_Mobile_part2= "";
		if(docName=="Application_Form_Islamic" || docName=="Application_Form")
		{
			attrbList += "&<Friend1_Name>&" + get_ngformGridColumn_template(GridName,0,0);	 
		friend1_Mob = get_ngformGridColumn_template(GridName,0,1);
		if(friend1_Mob!=''){
			Friend1_Mobile_part1 = friend1_Mob.substring(0,5);
			Friend1_Mobile_part2 = friend1_Mob.substring(5,15); 
			var temp = Friend1_Mobile_part2.split("@");
			Friend1_Mobile_part2 = temp[0];
		}
		attrbList += "&<Friend1_Mobile_part1>&" + Friend1_Mobile_part1;
		attrbList += "&<Friend1_Mobile_part2>&" + Friend1_Mobile_part2;
		attrbList += "&<Friend1_Relation>&" + get_ngformGridColumn_template(GridName,0,2);	 
		attrbList += "&<Friend2_Name>&" + get_ngformGridColumn_template(GridName,1,0);	 
	 friend2_Mob = get_ngformGridColumn_template(GridName,1,1);
	 if(friend2_Mob!=''){
		 Friend2_Mobile_part1 = friend2_Mob.substring(0,5);
	    Friend2_Mobile_part2 = friend2_Mob.substring(5,15); 
		var temp = Friend2_Mobile_part2.split("@");
		Friend2_Mobile_part2 = temp[0];
	 }
	 attrbList += "&<Friend2_Mobile_part1>&" + Friend2_Mobile_part1;
	 attrbList += "&<Friend2_Mobile_part2>&" + Friend2_Mobile_part2;
	 attrbList += "&<Friend2_Relation>&" + get_ngformGridColumn_template(GridName,1,2);
	 
	 //fetching Mailing details by shivang starts
	 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid") >= 0){
		 var residence_house = "";
		 var residence_city = "";
		 var residence_country = "";
		 var residence_zipcode = "";
		 var mailingAdd_house = "";
		 var mailingAdd_city = "";
		 var mailingAdd_country = "";
		 var mailingAdd_zipcode = "";
		 var pname = "CCTemplateData";
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid"); i++)              
               {
                    var value_type= get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
					var mailingAdd= get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,10);
					                   
                     if(mailingAdd =='YES@10' || mailingAdd =='Yes@10' || mailingAdd =='YES')					
                     {							
                            mailingAdd_house = get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,3); 
			                mailingAdd_city = com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5);
			                var Query="ResidenceCity";
							wparams="Code=="+mailingAdd_city;
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var mailingAdd_city = fields[0];	
							if(mailingAdd_city.indexOf('NODATA')>-1 ||mailingAdd_city.indexOf('ERROR')>-1){
								mailingAdd_city='';
							}				
							mailingAdd_country =com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,7);
							var Query1 = "ResidenceCountry";
							wparams="Code=="+mailingAdd_country;
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var mailingAdd_country = fields1[0];
							if(mailingAdd_country.indexOf('NODATA')>-1 ||mailingAdd_country.indexOf('ERROR')>-1){
								mailingAdd_country='';
							}
					        mailingAdd_zipcode = get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,1);							 
					}
					if(value_type =='RESIDENCE')						
                     {							
							//attrbList += "&<MailingAdd_building_name>&" + get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
                            residence_house = get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,3); 
			                 residence_city = com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,5);
			                 var Query="ResidenceCity";
							wparams="Code=="+residence_city;
							var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
							var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
							var city_Query = CallAjax(jspName,params);
							var fields = city_Query.split('#');
							var residence_city = fields[0];	
							if(residence_city.indexOf('NODATA')>-1 ||residence_city.indexOf('ERROR')>-1){
								residence_city='';
							}								
							residence_country =com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,7);
							var Query1 = "ResidenceCountry";
							wparams="Code=="+mailingAdd_country;
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var residence_country = fields1[0];
							if(residence_country.indexOf('NODATA')>-1 ||residence_country.indexOf('ERROR')>-1){
								residence_country='';
							}
					        residence_zipcode=  get_ngformGridColumn_template("cmplx_AddressDetails_cmplx_AddressGrid",i,1);							 
					}
                      
        }
			if( (residence_house == mailingAdd_house) && (residence_city == mailingAdd_city) && (residence_country == mailingAdd_country) 
					&& (residence_zipcode == mailingAdd_zipcode)){
						attrbList += "&<MailingAdd_building_name>&" + mailingAdd_house; 
			                attrbList += "&<MailingAdd_city>&" + mailingAdd_city; 							
							 attrbList += "&<MailingAdd_country>&" + mailingAdd_country;
							attrbList += "&<MailingAdd_zip_code>&" + mailingAdd_zipcode;
						
					}
						else{	attrbList += "&<MailingAdd_building_name>&" + mailingAdd_house; 
			                attrbList += "&<MailingAdd_city>&" + mailingAdd_city; 							
							 attrbList += "&<MailingAdd_country>&" + mailingAdd_country;
							attrbList += "&<MailingAdd_zip_code>&" + mailingAdd_zipcode;
						}
    }

	 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_FATCA_cmplx_GR_FatcaDetails") >= 0){
			var fatcaCheck="";
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_FATCA_cmplx_GR_FatcaDetails"); i++)              
               {
                    var value_type= get_ngformGridColumn_template("cmplx_FATCA_cmplx_GR_FatcaDetails",i,14);
					        value_type = value_type.substring(0,1);
                    if(value_type =='P')					
                     {							
                            fatcaCheck = get_ngformGridColumn_template("cmplx_FATCA_cmplx_GR_FatcaDetails",i,0);
							if(fatcaCheck == "O@10")
							{
								fatcaCheck= "YES";
								attrbList += "&<fatcaCheck>&" + fatcaCheck;
							break;
							}
							else{
								fatcaCheck = "NO";
								attrbList += "&<fatcaCheck>&" + fatcaCheck;
							}
					}
					
                      
        }

    }
	 
	 
	}

	}
	return attrbList;
}
//function added by Saurabh for Application form CR
function get_NoTin_Detail(attrbList){
	
	var pname = 'DocTemplateRLOS';
	var wparams='';
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var Query2="FetchArabic";
	var counter=1;
	for(var i=0;i<getLVWRowCount('cmplx_OECD_cmplx_GR_OecdDetails');i++){
		if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,8).indexOf("P-")>-1){
			var country="";
			if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,4)){
				wparams="Code=="+getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,4);
				var	params="Query=Country"+"&wparams="+wparams+"&pname="+pname;
				var NTB_Query = CallAjax(jspName,params);
				NTB_Query = trimStr(NTB_Query);
				var fields = NTB_Query.split('#');
				country = fields[0];
			}
			attrbList += "&<CTR"+counter+">&" + country + "@10";
			attrbList += "&<TINNo"+counter+">&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,5) + "@10";
			if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,6)){
				wparams="Reason=="+getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,6);
				var	params="Query="+Query2+"&wparams="+wparams+"&pname="+pname;
				var NTB_Query = CallAjax(jspName,params);
				NTB_Query = trimStr(NTB_Query);
				var fields = NTB_Query.split('#');
				var NTB = fields[0]+"@10";
				var NTB2 = fields[1]+"@10";
				
				attrbList += "&<NOTINEng"+counter+">&" + NTB2 + "@10";
				attrbList += "&<NOTINArabic"+counter+">&" + NTB + "@10";
			}
			counter++;		
			
		}
	}
	if(counter<3){
		for(var i=counter;i<=3;i++){
		attrbList += "&<CTR"+i+">&" +  "@10";
		attrbList += "&<TINNo"+i+">&"  + "@10";
		attrbList += "&<NOTINEng"+i+">&"  + "@10";
		attrbList += "&<NOTINArabic"+i+">&"  + "@10";
		}
	}
	
	
	
	return attrbList;
}


	function CallAjax(jspName,params)
{
	var response="";
	try{			
			var xmlReq = null;
			if(window.XMLHttpRequest) xmlReq = new XMLHttpRequest();
			else if(window.ActiveXObject) xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
			if(xmlReq==null) return; // Failed to create the request
				xmlReq.onreadystatechange = function()
			{
				switch(xmlReq.readyState)
				{
				case 0: 
					break;
				case 1: 
					break;
				case 2: 
					break;
				case 3: 
					break;
				case 4: 
					if (xmlReq.status==200) 
					{
					response=xmlReq.responseText;
					}
					else if (xmlReq.status==404)
						alert("Doc_template : URL doesn't exist!");
					else 
						alert("Doc_template : Status is "+xmlReq.status);	
					break;
				default:
					//alert(xmlReq.status);
					break;
				}
			}
			
			xmlReq.open('POST',jspName,false);				
			xmlReq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
			xmlReq.send(params);
			return response;
	   }
	
    catch(e)
    {
		alert("Doc_template : "+e.message);
		//return false;
    }
}
function trimStr(str) {
                       return str.replace(/^\s+|\s+$/g, '');
            }
//Below changes done by shweta for PL CAM
function PLTemplateData()
{
	 var attrbList="";
     var wparams="";
// var attrbList_arr = "company,compName;company_cifno,cif";
//var age = "";
var pname = "DocTemplatePL";//by shweta
attrbList+=creditcard_check(attrbList,'Decision_ListView1');
attrbList+=personalLoan_template(attrbList);
attrbList+=creditcard_notepad(attrbList);
attrbList+=fetch_remarks(attrbList,'Decision_ListView1');
attrbList+=personalLoan_selectPL(attrbList);
attrbList+=fetch_referTo(attrbList,'Decision_ListView1');
attrbList+=fetch_referTo_se(attrbList,'Decision_ListView1');
attrbList+= getAECBSummary(attrbList);
//attrbList+=creditcard_selectCC(attrbList,'Decision_ListView1');
//CHANGE BY SAURABH ON 11-12 FOR JIRA - PCSP-31
//attrbList += "&<Current_date_time>&" + get_ngformValue_template('CurrentDateLabel')+ ' '+new Date().getHours()+':'+new Date().getMinutes();
attrbList += "&<Current_date_time>&" + get_ngformValue_template('CurrentDateLabel')+ ' '+addZero(new Date().getHours())+ ':'+ addZero(new Date().getMinutes());
var Query = "Designation_CC"
wparams="Code=="+getNGValue('cmplx_EmploymentDetails_DesigVisa');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var designationaspervisa = CallAjax(jspName,params);
var fields = designationaspervisa.split('#');
if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){
	fields[0]='';
}
	attrbList += "&<Designation_As_Per_Visa>&" + fields[0]+'@10';
var Query = "FinalFDAmount"
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var FinalFDAmount = CallAjax(jspName,params);
var fields = FinalFDAmount.split('#');
var fdAmt=fields[0];
if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){
	fields[0]=0;
}
attrbList +="&<FDAmount>&" + fields[0];

attrbList += "&<PL_wi_name>&" + get_ngformValue_template('WiLabel');	
attrbList += "&<company>&" + get_ngformValue_template('compName');
attrbList += "&<company_cifno>&" + get_ngformValue_template('cif');
attrbList += "&<trade>&" + get_ngformValue_template('TLExpiry');
attrbList += "&<comapny_lob>&" + get_ngformValue_template('lob');
attrbList += "&<passport_valid>&" + get_ngformValue_template('AuthorisedSignDetails_PassportExpiryDate');
attrbList += "&<RM_Name>&" + get_ngformValue_template('RM_Name');
//attrbList += "&<AppType>&" + get_ngformValue_template('AppType');
//added by yash on 21 nov

// changes done by disha for Generate Template on 17-10-18 start
	attrbList += "&<Customer_cifno>&" + get_ngformValue_template('cmplx_Customer_CIFNO');
	attrbList += "&<FinalDBR>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalDBR');
	attrbList += "&<FinalTAI>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalTAI');
	attrbList += "&<Entry_Date>&" + get_ngformValue_template('Introduction_Date');
	attrbList += "&<Account_Number>&" + get_ngformValue_template('Account_Number');
	attrbList += "&<EmpName>&" + get_ngformValue_template('cmplx_EmploymentDetails_EmpName');
	attrbList += "&<EMpCode>&" + get_ngformValue_template('cmplx_EmploymentDetails_EMpCode');
	attrbList += "&<VisaSponser>&" + get_ngformValue_template('cmplx_EmploymentDetails_VisaSponser');
	attrbList += "&<LOS>&" + get_ngformValue_template('cmplx_EmploymentDetails_LOS');
	attrbList += "&<ActWorkEmployer>&" + get_ngformValue_template('cmplx_EmploymentDetails_actualworkemp');//PCASI-4
// changes done by disha for Generate Template on 17-10-18 end
	
	attrbList += "&<Emp_Type>&" + get_ngformText_template('cmplx_EmploymentDetails_Emp_Type');	
	attrbList += "&<targetSegCode>&" + get_ngformText_template('cmplx_EmploymentDetails_targetSegCode');
var Query = "DectechDelegation"
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var dectechDelegation = CallAjax(jspName,params);
var fields = dectechDelegation.split('#');
attrbList += "&<DectechDelegation>&" + fields[0]+'@10';
var Query = "MOB"
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var externalMOB = CallAjax(jspName,params);
var fields = externalMOB.split('#');
var mob =  fields[0];
if(mob.indexOf("NODATA")>-1)
{
	mob='';
}
attrbList += "&<ExternalMOB>&" + mob+'@10';
var Query = "DectechEligibility"
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var dectecheligibility = CallAjax(jspName,params);
var fields = dectecheligibility.split('#');
dectecheligibility=fields[0];
if(dectecheligibility.indexOf("NODATA")>-1 || dectecheligibility.indexOf("ERROR")>-1)
{
	dectecheligibility='';
}
attrbList += "&<finalLimit>&" + dectecheligibility+'@10';
attrbList += "&<TotalFinalLimit>&" +get_ngformValue_template("cmplx_EligibilityAndProductInfo_FinalLimit");	
var Query = "TotalObligation"
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var totalObligation = CallAjax(jspName,params);
var fields = totalObligation.split('#');
var isLiabilityPresent=fields[0];
var final_Limit='';//PCASP-1014 by shweta
if(getNGValue("cmplx_EligibilityAndProductInfo_EMI")!='' || getNGValue("cmplx_EligibilityAndProductInfo_EMI")!=0)
{
	final_Limit = getNGValue("cmplx_EligibilityAndProductInfo_EMI");
	if(final_Limit==""){
		final_Limit=0;
	}
	final_Limit= parseFloat(final_Limit)
}
if(isLiabilityPresent==0 || isLiabilityPresent=='' || isLiabilityPresent=='0')
{
	if((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,8))=="Primary@10"){
		
		if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,0)=="Conventional@10")
		{
			var Query = "NumOfCardRequested"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var NumOfCardRequested = CallAjax(jspName,params);
			var fields = NumOfCardRequested.split('#');
			var numOfCardReq=fields[0];
			
			var Query = "IsConventionalSelected"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var NumOfCardRequested = CallAjax(jspName,params);
			var fields = NumOfCardRequested.split('#');
			var isConventionalSelected=fields[0];
			if(numOfCardReq==1 && isConventionalSelected > 0)
			{   
				final_Limit=final_Limit;
			}
			else{
				var Query = "IslamicAmtFetch"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var IslamicAmtFetch = CallAjax(jspName,params);
				var fields = IslamicAmtFetch.split('#');
				var islamicAmt=fields[0];
				if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1 || islamicAmt==""){
					islamicAmt=0;
				}
				final_Limit= parseFloat(final_Limit) + parseFloat(islamicAmt);
			}
		}
		else
		{							
				var Query = "IsConventionalSelected"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var NumOfCardRequested = CallAjax(jspName,params);
				var fields = NumOfCardRequested.split('#');
				var isConventionalSelected=fields[0];
			  if(isConventionalSelected>0){
				var Query = "FinalLimitForConventional"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var FinalConventionalLimit = CallAjax(jspName,params);
				var fields = FinalConventionalLimit.split('#');
				var finalConventionalLimit=fields[0];
				if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1 || finalConventionalLimit==""){
					finalConventionalLimit=0;
				}
				var Query = "IslamicAmtFetch"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var IslamicAmtFetch = CallAjax(jspName,params);
				var fields = IslamicAmtFetch.split('#');
				var islamicAmt=fields[0];
				if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1 || islamicAmt==""){
					islamicAmt=0;
				}
				final_Limit= parseFloat(final_Limit) + parseFloat(finalConventionalLimit) + parseFloat(islamicAmt);
			 	
			 }
			else{
			    var Query = "IslamicAmtFetch"
				wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
				var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
				var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
				var IslamicAmtFetch = CallAjax(jspName,params);
				var fields = IslamicAmtFetch.split('#');
				var islamicAmt=fields[0];
				if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1 || islamicAmt==""){
					islamicAmt=0;
				}
				final_Limit= parseFloat(final_Limit) + parseFloat(islamicAmt);
			}
		}
	}
}
else{
			var Query = "OD_LoanEmi"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var OD_LoanEmi = CallAjax(jspName,params);
			var fields = OD_LoanEmi.split('#');
			var odLoanEmi = fields[0];
			if(odLoanEmi>0){}
			else{odLoanEmi=0;}
			var Query = "IslamicAmtFetch"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var IslamicAmtFetch = CallAjax(jspName,params);
			var fields = IslamicAmtFetch.split('#');
			var islamicAmt=fields[0];
			if(islamicAmt>0){}
			else{islamicAmt=0;}
			var Query = "cardLimitCalc";
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var cardLimit = CallAjax(jspName,params);
			var fields = cardLimit.split('#');
			var cardLimitCalc=fields[0];
			if(parseFloat(cardLimitCalc)>0){}
			else{cardLimitCalc=0;}
			var Query = "FinalLimitForConventional"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var FinalConventionalLimit = CallAjax(jspName,params);
			var fields = FinalConventionalLimit.split('#');
			var finalConventionalLimit=fields[0];
			if(finalConventionalLimit>0){}
			else{finalConventionalLimit=0;}
			
			var Query = "ExternalLiabilityLimit"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var ExternalLiabilityLimit = CallAjax(jspName,params);
			var fields = ExternalLiabilityLimit.split('#');
			var extLiabilityLimit=fields[0];
			
			var Query = "InternalIslamicLimit"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var InternalIslamicLimit = CallAjax(jspName,params);
			var fields = InternalIslamicLimit.split('#');
			var intIslLimit=0;
			if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){
				intIslLimit=0;
			}
			else{ 
				intIslLimit=fields[0];
			}
			
			var Query = "InternalConvLimit"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var InternalConvLimit = CallAjax(jspName,params);
			var fields = InternalConvLimit.split('#');
			var intConvLimit=0;
			if(fields[0].indexOf("NODATA")>-1 || fields[0].indexOf("ERROR")>-1){ 
				intConvLimit=0;
			}
			else{ 
				intConvLimit=fields[0];
			}
			
			
			
	if((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,8))=="Primary@10"){
		
		if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,0)=="Conventional@10")
		{
			var Query = "NumOfCardRequested"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var NumOfCardRequested = CallAjax(jspName,params);
			var fields = NumOfCardRequested.split('#');
			var numOfCardReq=fields[0];
			
			var Query = "IsConventionalSelected"
			wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
			var NumOfCardRequested = CallAjax(jspName,params);
			var fields = NumOfCardRequested.split('#');
			var isConventionalSelected=fields[0];
			//PCASI-1700				
			final_Limit= parseFloat(final_Limit)  + parseFloat(intConvLimit) +  parseFloat(extLiabilityLimit) + parseFloat(odLoanEmi);
		}
		else
		{		
			   final_Limit=parseFloat(final_Limit)+ parseFloat(odLoanEmi)+ parseFloat(extLiabilityLimit)+parseFloat(intIslLimit)+ parseFloat(intConvLimit);
		}
	 }
}

//PCASI-3098 and PCASI-3110 bandana
if (com.newgen.omniforms.formviewer.getNGValue("ReqProd")=='Personal Loan'){
var threePerc = com.newgen.omniforms.formviewer.getNGValue("cmplx_EligibilityAndProductInfo_EFCHidden");
threePerc  = parseFloat(3/100)*parseFloat(threePerc);
final_Limit = parseFloat(final_Limit) + parseFloat(threePerc);

for(var i=0;i<getLVWRowCount('cmplx_Liability_New_cmplx_LiabilityAdditionGrid');i++){
		if(getLVWAT('cmplx_Liability_New_cmplx_LiabilityAdditionGrid',i,8)){
			
			var ManLiab = parseFloat(getLVWAT('cmplx_Liability_New_cmplx_LiabilityAdditionGrid',i,2));
			final_Limit = final_Limit+ parseFloat(ManLiab);
		}
	}
}

attrbList += "&<TotalObligation>&" + final_Limit+'@10';

	
	attrbList += "&<PLST>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatusPL');
	attrbList += "&<PLE>&" + get_ngformText_template('cmplx_EmploymentDetails_categexpat');
	attrbList += "&<PLN>&" + get_ngformText_template('cmplx_EmploymentDetails_categnational');
	attrbList += "&<PLCAT>&" + get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer');
	attrbList += "&<PLCATC>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatusCC');
//	attrbList += "&<ALOC_CC>&" + get_ngformValue_template('cmplx_EmploymentDetails_remarks');
//	attrbList += "&<ALOC_PL>&" + get_ngformValue_template('cmplx_EmploymentDetails_Remarks_PL');
	attrbList += "&<High>&" + get_ngformValue_template('cmplx_EmploymentDetails_highdelinq');
	
	attrbList += "&<NepType>&" + get_ngformText_template('cmplx_EmploymentDetails_NepType');
	attrbList += "&<DectechDecision>&" + get_ngformValue_template('cmplx_Decision_Dectech_decsion');
    var Query = "AECBHistory"
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var aecbHistmonthCount = CallAjax(jspName,params);
var fields = aecbHistmonthCount.split('#');
attrbList += "&<AECBHistoryCount>&" + fields[0]+'@10';

//changes done by shivang for 2.1
attrbList += "&<AECBScore>&" + com.newgen.omniforms.formviewer.getNGValue('cmplx_Liability_New_AECBScore');
attrbList += "&<Range>&" + com.newgen.omniforms.formviewer.getNGValue('cmplx_Liability_New_Range');
attrbList += "&<ReferenceNo>&" + com.newgen.omniforms.formviewer.getNGValue('cmplx_Liability_New_ReferenceNo');

var Query = "EligibleCardLimit"
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var eligibleLimit = CallAjax(jspName,params);
var fields = eligibleLimit.split('#');
attrbList += "&<eligibleLimit>&" + fields[0]+'@10';

var username = window.parent.userName;
attrbList += "&<Reviewed_By>&" + username +'@10';
	attrbList += "&<gender>&" + get_ngformText_template('cmplx_Customer_gender');
attrbList += "&<MAritalStatus>&" + get_ngformText_template('cmplx_Customer_MAritalStatus');
var Query = "Nationality_CC";
wparams="Code=="+getNGValue('cmplx_Customer_Nationality');//by shweta
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var nat_Query = CallAjax(jspName,params);
var fields = nat_Query.split('#');
attrbList += "&<Nationality>&" + fields[0]+'@10';
	attrbList += "&<National>&" + get_ngformText_template('cmplx_Customer_GCCNational');
            attrbList += "&<SalaryDay>&" + get_ngformValue_template('cmplx_IncomeDetails_SalaryDay');
	attrbList += "&<Salary_transfer>&" + get_ngformText_template('cmplx_IncomeDetails_SalaryXferToBank');
	attrbList += "&<EmpStatus>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatus');
	//changes for PCSP 23 by saurbh on 8th Jan
	attrbList += "&<AccomodationValue>&" + get_ngformValue_template('cmplx_IncomeDetails_AccomodationValue');
	attrbList += "&<Accprovided>&" + get_ngformValue_template('cmplx_IncomeDetails_Accomodation');
	attrbList += "&<Confirmed>&" + get_ngformValue_template('cmplx_EmploymentDetails_JobConfirmed');
	attrbList += "&<Movement>&" + get_ngformText_template('cmplx_EmploymentDetails_empmovemnt');
	Query = "Designation_CC";
wparams="Code=="+getNGValue('cmplx_EmploymentDetails_Designation');//by shweta
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var desig_Query = CallAjax(jspName,params);
var fields = desig_Query.split('#');
if(fields[0].indexOf("ERROR")>-1 || fields[0].indexOf("NODATAFOUND")>-1)
{
	fields[0]=getNGValue('cmplx_EmploymentDetails_Designation');
}
	attrbList += "&<Cuurent_Designation>&" + fields[0]+'@10';
	attrbList += "&<Maturity_Age>&" + get_ngformValue_template("cmplx_LoanDetails_ageatmaturity");
            //attrbList += "&<Top_UP>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_NetPayout');
	attrbList += "&<Micro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Micro');
	attrbList += "&<Macro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Macro');
	attrbList += "&<Strength>&" + get_ngformText_template_dev('cmplx_Decision_strength');
	attrbList += "&<Weakness>&" + get_ngformText_template_dev('cmplx_Decision_weakness');
            attrbList += "&<Total_Exposure>&" + get_ngformValue_template('cmplx_Liability_New_AggrExposure');

	var aloc_remark_CC='';
	aloc_remark_CC=get_ngformValue_template('cmplx_EmploymentDetails_remarks');
	if(aloc_remark_CC=="@10" || aloc_remark_CC == "" || aloc_remark_CC==null){
		attrbList += "&<ALOC_CC>&" + "No@10";
	}else{
		attrbList += "&<ALOC_CC>&" + "Yes@10";
	}

	//change by saurabh for PCSP-315
	attrbList += "&<ALOC_PL>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,5).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_Remarks_PL'):" "+"@10");
	var aloc_remark=''
	aloc_remark= ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,5).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_Remarks_PL'):" "+"@10");
	if(aloc_remark=="@10" || aloc_remark == "" || aloc_remark==null){
		attrbList += "&<ALOC_PL>&" + "No@10";
	}else{
		attrbList += "&<ALOC_PL>&" + "Yes@10";
	}
	attrbList += "&<Resident>&" + get_ngformText_template('cmplx_Customer_ResidentNonResident');
	attrbList += "&<Age>&" + get_ngformValue_template('cmplx_Customer_age');
	attrbList += "&<RM_Name>&" + get_ngformValue_template('RM_Name');
	attrbList += "&<AvgCR33>&" + get_ngformText_template('cmplx_FinacleCore_avg_credit_3month');
	attrbList += "&<avgbal3>&" + get_ngformText_template('cmplx_FinacleCore_total_avg_last13');
	attrbList += "&<AvgCR6>&" + get_ngformText_template('cmplx_FinacleCore_avg_credit_6month');
	attrbList += "&<avgbal6>&" + get_ngformText_template('cmplx_FinacleCore_total_avg_last_16');
	attrbList += "&<Collection>&" + get_ngformText_template('cmplx_EmploymentDetails_Collectioncode');
	attrbList += "&<Classification>&" + get_ngformText_template('cmplx_EmploymentDetails_ClassificationCode');
	attrbList += "&<MIS>&" + get_ngformText_template('cmplx_EmploymentDetails_MIS');
attrbList += "&<Channel>&" + get_ngformText_template('cmplx_EmploymentDetails_channelcode');
attrbList += "&<Promo>&" + get_ngformText_template('cmplx_EmploymentDetails_PromotionCode');
attrbList += "&<High>&" + get_ngformValue_template('cmplx_EmploymentDetails_highdelinq');
attrbList += "&<source_code>&" + get_ngformValue_template('InitChannelLabel');;

attrbList += "&<Net_Salary>&"+get_ngformValue_template('cmplx_IncomeDetails_AvgNetSal');
attrbList += "&<applicant_cat>&" + get_ngformText_template('cmplx_Customer_CustomerCategory');
//changed by nikhil for PCSP-912 updated by nikhil
attrbList += "&<application_cat>&" + get_ngformText_template('cmplx_EmploymentDetails_ApplicationCateg');
attrbList += "&<ECRN>&" + get_ngformValue_template('ECRN');
attrbList += "&<CRN>&" + get_ngformValue_template('CRN');
attrbList += "&<customer_name>&" + get_ngformValue_template('CustomerLabel');
//attrbList += "&<Curent_date_time>&" + get_ngformValue_template('CurrentDateLabel');
if(get_ngformValue_template('cmplx_Liability_New_DBRNet')=="@10")
{
attrbList += "&<DBRNET>&" + "0.00@10";
}
else{
attrbList += "&<DBRNET>&" + get_ngformValue_template('cmplx_Liability_New_DBRNet');
}
attrbList += "&<TAI>&" + get_ngformValue_template('cmplx_Liability_New_TAI');
attrbList += "&<marketcode>&" + get_ngformText_template('cmplx_EmploymentDetails_marketcode');//added by shweta
attrbList += "&<InsuranceAmount>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_InsuranceAmount');
attrbList += "&<TakeOver_Product>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,7),get_ngform_controlObject('Scheme'));
attrbList += "&<TakeOver_Code>&"+get_ngformText_template('cmplx_EligibilityAndProductInfo_takeoverBank');
//Bandana: PCASI-3611 and 3613
if (get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4).indexOf("TOP")>-1)
	attrbList += "&<Top_UP>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_NetPayout');
else
    attrbList += "&<Top_UP>&" + "@10";
//Bandana: PCASI-3611 and 3613 ends
attrbList += "&<EMI>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_EMI');
//attrbList += "&<Dec_Remarks>&" + get_ngformValue_template('cmplx_Decision_REMARKS');
attrbList += "&<ReferTo>&" + get_ngformValue_template('ReferTo');
//Refer reason commented for PCASI-3107
//attrbList += "&<ReferReason>&" + get_ngformText_template('DecisionHistory_DecisionReasonCode');
//attrbList += "&<Decision>&" + get_ngformText_template('cmplx_Decision_Decision');
attrbList += "&<LengthOfBusiness>&" + get_ngformValue_template('cmplx_EmploymentDetails_LengthOfBusiness');
//attrbList += "&<Designation_As_Per_Visa>&" + get_ngformValue_template('cmplx_EmploymentDetails_DesigVisa');
attrbList += "&<DateOfJoining>&" + get_ngformValue_template('cmplx_EmploymentDetails_DOJ');
attrbList += "&<Gross_Salary>&" + get_ngformValue_template('cmplx_IncomeDetails_grossSal');

	//attrbList += "&<CROPS>&"+getCropsNotepad('NDISB') +"@10";
	attrbList += "&<Bank_Name>&" +" "+"@10";
	attrbList += "&<Acc_No>&" +" "+"@10";
	//Added by tarang on 03/04/2018
	attrbList += "&<Bank_From>&" + get_ngformGridColumn_template("cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails",0,1);
	attrbList += "&<Bank_To>&" + get_ngformGridColumn_template("cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails",0,2);
	//Added by tarang on 03/04/2018
attrbList += "&<existing>&" + (get_ngformValue_template('cmplx_Customer_NTB').split('@')[0]=='NO'?'YES@10':'NO@10');
//add today
attrbList += "&<Tenor>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_Tenor');
attrbList += "&<InterestRate>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_InterestRate');
attrbList += "&<MOL_Var>&" + get_ngformValue_template('cmplx_MOL_molsalvar');
attrbList += "&<IMAMOUNT>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalLimit');//by shweta
/*if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,1)=='IM'){
attrbList += "&<FirstRepayDate>&" + get_ngformValue_template('cmplx_LoanDetails_frepdate');
attrbList += "&<Last_Repay>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_MaturityDate');
}
else{
attrbList += "&<FirstRepayDate>&@10";
attrbList += "&<Last_Repay>&@10";
}*/
attrbList += "&<FirstRepayDate>&" + get_ngformValue_template('cmplx_LoanDetails_frepdate');
//change by saurabh on 4 Feb 19 for PCSP-898
Query = "CardType";
wparams="WiName=="+window.parent.pid;
var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var desig_Query = CallAjax(jspName,params);
var fields = desig_Query.split('#');
	attrbList += "&<Card_Product>&" + fields[0]+'@10';
	attrbList += "&<Card_prod>&" + fields[0]+'@10';
attrbList += "&<score_grade>&" + get_ngformValue_template('cmplx_Decision_score_grade');
if(get_ngformValue_template('cmplx_EmploymentDetails_IncInPL')=="YES@10" || get_ngformValue_template('cmplx_EmploymentDetails_IncInPL')=="Yes@10")
{
attrbList += "&<IncInPL>&" + "Yes@10";
}else{
attrbList += "&<IncInPL>&" + "No@10";
}
if(get_ngformValue_template('cmplx_EmploymentDetails_IncInCC')=="YES@10" || get_ngformValue_template('cmplx_EmploymentDetails_IncInCC')=="Yes@10")
{
attrbList += "&<IncInCC>&" + "Yes@10";
}else{
attrbList += "&<IncInCC>&" + "No@10";
}
attrbList += "&<Processing>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_LPF');
//attrbList += "&<Scheme>&" + get_ngformValue_template('Scheme');
attrbList += "&<company_name>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4);
attrbList += "&<FieldVisitedDone>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,5).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_FieldVisitDone'):" "+"@10");
attrbList += "&<LoanMultiple>&" + calcLoanMultiple();
attrbList += "&<Applicant_Category>&" + get_ngformText_template('cmplx_Customer_CustomerCategory');
attrbList += "&<Employer_Category>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,5).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,33),get_ngform_controlObject('EmployerCategoryPL')));

attrbList += "&<Employer_Category>&" + get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer');
//attrbList += "&<application>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",0,4);
attrbList += "&<Product_type>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,0);
attrbList += "&<requested_limit>&" + get_ngformValue_template("cmplx_EligibilityAndProductInfo_FinalLimit");
//attrbList += "&<Referral_Name>&" + get_ngformGridColumn_template("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,0);
attrbList += "&<Referral_Name>&" + get_ngformValue_template("cmplx_EmploymentDetails_PromotionCode");
attrbList += "&<MOL_Contract_Date>&" + get_ngformValue_template('cmplx_MOL_ctrctstart');//PCASI-1064 by shweta


/*var Query2 = "SELECT isnull(Description,'') as city FROM ng_MASTER_SubProduct_PL with (nolock) WHERE code ='"+ get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,2)+"'";
var jspName="/webdesktop/resources/scripts/CustomSelect.jsp";
var params="Query="+Query2;
var subProduct_Query = CallAjax(jspName,params);
var fields2 = subProduct_Query.split('#');
var subProduct = fields2[0]+"@10";*/
//attrbList += "&<subproduct>&" + subProduct;
	attrbList += "&<subproduct>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,2),get_ngform_controlObject('subProd'));

attrbList += "&<requested_product>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,1);
/*var Query1 = "SELECT isnull(Description,'') as city FROM ng_master_ApplicationType with (nolock) WHERE code ='"+ get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4)+"'";
params="Query="+Query1;
var appType_Query = CallAjax(jspName,params);
var field1 = appType_Query.split('#');
var appType = fields1[0]+"@10";*/
attrbList += "&<applicant_type>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,4),get_ngform_controlObject('AppType'));	
//attrbList += "&<applicant_type>&" + appType;
attrbList += "&<Scheme>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,7),get_ngform_controlObject('Scheme'));
attrbList += "&<WORSTL24>&" + get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,17);
attrbList += "&<SalaryDay>&" + get_ngformValue_template('cmplx_IncomeDetails_SalaryDay');
attrbList += "&<Salary_transfer>&" + get_ngformText_template('cmplx_IncomeDetails_SalaryXferToBank');
attrbList += "&<TakeOver_BankName>&" + get_ngformText_template('cmplx_EligibilityAndProductInfo_takeoverBank');
attrbList += "&<TakeOver_Amount>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_TakeoverAMount');
attrbList += "&<Doc_name>&" + get_ngformValue_template('DocName');
attrbList += "&<Doc_Sta>&" + get_ngformValue_template('DocSta');
//attrbList += "&<IncInPL>&" + get_ngformValue_template('cmplx_EmploymentDetails_IncInPL');
//attrbList += "&<IncInCC>&" + get_ngformValue_template('cmplx_EmploymentDetails_IncInCC');
attrbList += "&<Deferred>&" + get_ngformValue_template('DeferredUntilDate');
attrbList += "&<TOTAL>&" + get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,12);
attrbList += "&<AECB>&" + get_ngformValue_template('AECBHistMonthCnt');
attrbList += "&<WORSTD24>&" + get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,13);
attrbList += "&<authorize_name>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,2);
attrbList += "&<auth_passex>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,9);
attrbList += "&<auth_cif>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,1);
attrbList += "&<auth_dob>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,4);
attrbList += "&<auth_nat>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,3);
attrbList += "&<auth_visaex>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,6);
attrbList += "&<auth_visadob>&" + get_ngformGridColumn_template("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,4);
attrbList += "&<loan_limit>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,3);
var Query = "PLE";// PCASI-1104,1103 by shweta 
//"SELECT top 1 isnull(csmNext,'') as csmNext,isnull(userName,'') as userName FROM NG_RLOS_GR_DECISION with (nolock) WHERE dec_wi_Name ='"+ com.newgen.omniforms.formviewer.getNGValue('WiLabel')+"' order by csmNext";
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var csmNext_Result = CallAjax(jspName,params);
	var act_name=window.parent.stractivityName;
	var username=window.parent.userName;
	if(act_name=='CAD_Analyst2')
	{
	var level=com.newgen.omniforms.formviewer.getNGValue('CADNext');
	csmNext_Result=csmNext_Result+"~CAD_Analyst2#"+username+"#"+level+"#~";
	}
	//alert(csmNext_Result);
	var rem1='',rem2='',rem3='',rem4='',rem5='',rem6='',rem7='',rem8='',rem9='',rem10='';
	var rows = csmNext_Result.trim().split('~');
            for(var i=0;i<rows.length-1;i++){
		var workstep=rows[i].split('#')[0].trim();
		var username=rows[i].split('#')[1];
		var cadlevel=rows[i].split('#')[2];
		if(workstep=='CAD_Analyst1'){
			rem1=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='L1'){
			rem6=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='L2'){
			rem6=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='L3'){
			rem7=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='L4'){
			rem3=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='L5'){
			rem8=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='L6'){
			rem4=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='L7'){
			rem9=username;
		}
		else if(workstep=='CAD_Analyst2' && cadlevel=='Ln'){
			rem5=username;
			rem10=username;
		}
	}
      /*	var csmNext = fields[0]+"@10";
	var userName = fields[1]+"@10";  */
                  

	if(act_name=='CAD_Analyst1')//PCASI-1103 By shweta
	{
		var	username=window.parent.userName;
	rem1=username;
	}
	attrbList += "&<Remarks1>&"+rem1+"@10";
	attrbList += "&<Remarks2>&"+rem2+"@10";
	attrbList += "&<Remarks3>&"+rem3+"@10";
	attrbList += "&<Remarks4>&"+rem4+"@10";
	attrbList += "&<Remarks5>&"+rem5+"@10";
	attrbList += "&<Remarks6>&"+rem6+"@10";
	attrbList += "&<Remarks7>&"+rem7+"@10";
	attrbList += "&<Remarks8>&"+rem8+"@10";
	attrbList += "&<Remarks9>&"+rem9+"@10";
	attrbList += "&<Remarks10>&"+rem10+"@10";
        /* if (csmNext=='L1@10')
	{
		attrbList += "&<Remarks1>&" + userName;
	}
	else if (csmNext=='L2@10')
	{
		attrbList += "&<Remarks2>&" + userName;
	}
	else if (csmNext=='L3@10')
	{
		attrbList += "&<Remarks3>&" + userName;
	}
	else if (csmNext=='L4@10')
	{
		attrbList += "&<Remarks4>&" + userName;
	}
	else if (csmNext=='L5@10')
	{
		attrbList += "&<Remarks5>&" + userName;
	}
	else if (csmNext=='L6@10')
	{
		attrbList += "&<Remarks6>&" + userName;
	}
	else if (csmNext=='L7@10')
	{
		attrbList += "&<Remarks7>&" + userName;
	}
		else if (csmNext=='L8@10')
	{
		attrbList += "&<Remarks8>&" + userName;
	}
		else if (csmNext=='L9@10')
	{
		attrbList += "&<Remarks9>&" + userName;
	}
		else if (csmNext=='L10@10')
	{
		attrbList += "&<Remarks10>&" + userName;
	}
	else{
	} */

return attrbList;
}

function getCropsNotepad(noteCode){
	var retVal = '';
	if(com.newgen.omniforms.formviewer.isVisible('NotepadDetails_Frame2')){
		var rowCount = com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_NotepadDetails_cmplx_notegrid');
		if(rowCount>0){
			for(var i=0;i<rowCount;i++){
				if(com.newgen.omniforms.formviewer.getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',i,1)==noteCode){
					retVal+=com.newgen.omniforms.formviewer.getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',i,2)+',';
				}
			}
		}
		if(retVal.charAt(retVal.length-1)==','){
			retVal = retVal.substring(0,retVal.length-1);
		}
	}
	return retVal;
}
//function changed by saurabh for Application Form CR.
function BT_rlos(attrbList)
	 {
		 var attrbList = "";
		 var bt_rlos1 = "";
		 var bt1found=false;
		 var bt2found=false;
		 var Total_avg = 0;
		 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CC_Loan_cmplx_btc") > 0){
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CC_Loan_cmplx_btc"); i++){
                    var bt_rlos1= com.newgen.omniforms.formviewer.getLVWAT("cmplx_CC_Loan_cmplx_btc",i,0);
					if(bt_rlos1 =='BT'){
					var Total_avg = Total_avg + parseFloat(get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,11));
                            attrbList += "&<AED>&" + Total_avg + "@10";
						if(!bt1found){
							attrbList += "&<BT_CARD>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,1)+"@10";
							attrbList += "&<BT1_BankName>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,3)+"@10";
							attrbList += "&<BT1_NameOnCard>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,4)+"@10";
							bt1found=true;
						}
						else if(!bt2found){
							attrbList += "&<BT_CARD2>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,1)+"@10";
							attrbList += "&<BT2_BankName>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,3)+"@10";
							attrbList += "&<BT2_NameOnCard>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,4)+"@10";
							bt2found=true;
						}
						                      
                    }
					
	 }
	 
		 }
		 if(getLVWRowCount('cmplx_CC_Loan_cmplx_dds')>0){
            attrbList += "&<SI_DDS>&"+ "DDS" +"@10";
            attrbList += "&<SI_DDS_AcctNo>&"+getLVWAT('cmplx_CC_Loan_cmplx_dds',0,6)+"@10";
			 if(getLVWAT('cmplx_CC_Loan_cmplx_dds',0,1)=='M')
            attrbList += "&<SI_DDS_Mode>&"+ "M" +"@10";
            else
			{ attrbList += "&<SI_DDS_Mode>&"+ "Other" +"@10";}
			
		 }
		 else if(getLVWRowCount('cmplx_CC_Loan_cmplx_si')>0){
		 	attrbList += "&<SI_DDS>&"+ "SI" +"@10";
		 	attrbList += "&<SI_DDS_AcctNo>&"+getLVWAT('cmplx_CC_Loan_cmplx_si',0,0)+"@10";
			if(getLVWAT('cmplx_CC_Loan_cmplx_si',0,1)=='M')
            attrbList += "&<SI_DDS_Mode>&"+ "M" +"@10";
            else
			{  attrbList += "&<SI_DDS_Mode>&"+ "Other" +"@10";}
			attrbList += "&<DueDate>&"+ getLVWAT('cmplx_CC_Loan_cmplx_si',0,2); +"@10";
		 }
		 return attrbList;
	 }
	 function IM_rlos(attrbList)
	 {
		 var attrbList = "";
		 var im_rlos1 = "";
		 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CC_Loan_cmplx_btc") >= 0){
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CC_Loan_cmplx_btc"); i++){
                    var im_rlos1= com.newgen.omniforms.formviewer.getLVWAT("cmplx_CC_Loan_cmplx_btc",i,0);
					if(im_rlos1 =='LOC'){
						                      attrbList = "&<IM_CARD>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,1)+"@10";
                    }
					
	 }
		 }
		 return attrbList;
	 }
	 
	 function get_Reference_Detail(attrbList)
	 {
		 var im_rlos1 = "";
		 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails") > 0){
		   for(var i=0; i < com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails"); i++){
			   if(i>=2){break;}
				attrbList += "&<Friend"+(i+1)+"_Name>&"+get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",i,0)+"@10";
				attrbList += "&<Friend"+(i+1)+"_Relation>&"+get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",i,3)+"@10";
				attrbList += "&<Friend"+(i+1)+"_Mobile>&"+get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",i,1)+"@10";
				attrbList += "&<Friend"+(i+1)+"_Address>&"+get_ngformGridColumn_template("cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails",i,4)+"@10";
				//Friend1_
			}
		 }
		 return attrbList;
		 
	 }
//added by shweta for PL CAM
	 function personalLoan_selectPL(attrbList)
	 {
	 	var attrbList = "";
	 	if(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,5).split('@')[0].indexOf('Salaried')>-1){
	 		if($('#cmplx_EmploymentDetails_IncInCC').is(":checked")){
	 			attrbList+="&<IncInCC>&"+"Yes@10";
	 		}
	 		else{
	 			attrbList+="&<IncInCC>&"+"No@10";
	 		}
	 	}
	 	else 
	 	{
	 		attrbList += "&<IncInCC>&" + " " + "@10";
	 	}

	 	return attrbList;
	 }
	 
	 function personalLoan_template(attrbList){
		 var attrbList = "";
		 var value_type = "";
		 var note_desc = "";
				if (getLVWRowCount("Decision_ListView1") >= 0){
	               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("Decision_ListView1"); i++){
	                    var value_type= get_ngformGridColumn_template("Decision_ListView1",i,2);
	                    if(value_type!=undefined && value_type.split('@')[0]=='CAD_Analyst1'){
							                      attrbList+= "&<Remarks>&"+get_ngformGridColumn_template("Decision_ListView1",i,4)+"@10";
	                   
						}
						if(value_type!=undefined && value_type.split('@')[0]=='CAD_Analyst2'){
							                      attrbList+= "&<TYPE1>&"+get_ngformGridColumn_template("Decision_ListView1",i,4)+"@10";
	                    }
						else
						{
						attrbList+="&<Remarks>&"+(com.newgen.omniforms.formviewer.getLVWAT("Decision_ListView1",i,4) =='') ? "" :
						com.newgen.omniforms.formviewer.getLVWAT("Decision_ListView1",i,4)+"@10";
						}				
	                }
				
				}
				/* if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid") >0){*/
				var temp='';
	               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid"); i++){
	                    var note_desc= com.newgen.omniforms.formviewer.getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,1);
						if(note_desc =='NCPV' || note_desc =='NSMART'){
							temp+=com.newgen.omniforms.formviewer.getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,2)+',';
						}
					}
					if(temp.charAt(temp.length-1)==','){
						temp = temp.substring(0,temp.length-1);
					}
					if(temp!=''){
							attrbList += "&<desc>&"+temp+"@10"+",";
					}
						else{
							attrbList = "&<desc>&"+" "+"@10";
					}
				
			//	}
				
				/*else{
							attrbList = "&<desc>&"+" "+"@10";
					}*/
		

	if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid") >= 0){
	               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid"); i++){
					   
	                 var product_type= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,0);
				     var product1_type= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2);
					  //added by shivang for sprint3
	                 var Emptype= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,5);
					 var applicationType= com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,4);				  
					//Changed by Shivang
	                 if(product_type=='Islamic' && (product1_type=='Instant Money' || product1_type=='IM')){
					attrbList+="&<header>&INSTANT FINANCE@10";
					attrbList+="&<loan>&FINANCE@10";

					}
					else if(product_type=='Conventional' && (product1_type=='Instant Money' || product1_type=='IM')){
					attrbList+="&<header>&INSTANT MONEY@10";
	                attrbList+="&<loan>&Loan@10";
					}
					
	 				else if(product_type=='Islamic' && product1_type=='EXP'){
					attrbList+="&<header>&EXPAT FINANCE@10";
					attrbList+="&<loan>&Finance@10";
					}
					else if(product_type=='Conventional' && product1_type=='EXP'){
					attrbList+="&<header>&EXPAT LOANS@10";
					attrbList+="&<loan>&Loan@10";
					}
					else if(product_type=='Islamic' && product1_type=='NAT'){
					attrbList+="&<header>&UAE NATIONAL FINANCE@10";
					attrbList+="&<loan>&Loan@10";
					}
					else if(product_type=='Conventional' && product1_type=='NAT'){
					attrbList+="&<header>&UAE NATIONAL LOANS@10";
					attrbList+="&<loan>&Loan@10";
					}
					//Added above by Tarang on 26/03/2018
					else{
					attrbList+="&<header>&SALARIED PERSONAL LOAN@10";
					
					attrbList+="&<loan>&Loan@10";
					}
				   }
					}

				
	return attrbList;
	}


