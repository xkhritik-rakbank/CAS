
function calcLoanMultiple()
{
var finalLoanAmount = com.newgen.omniforms.formviewer.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit");
var FinalTAI= com.newgen.omniforms.formviewer.getNGValue("cmplx_EligibilityAndProductInfo_FinalTAI");

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
	attrbList += "&<CC_Wi_name>&" + get_ngformValue_template('WiLabel');
	attrbList += "&<company>&" + get_ngformValue_template('compName');
    attrbList += "&<company_cifno>&" + get_ngformValue_template('cif');
    attrbList += "&<trade>&" + get_ngformValue_template('TLExpiry');
    attrbList += "&<comapny_lob>&" + get_ngformValue_template('lob');
    attrbList += "&<passport_valid>&" + get_ngformValue_template('AuthorisedSignDetails_PassportExpiryDate');
	attrbList += "&<RM_Name>&" + get_ngformValue_template('RM_Name');
    //attrbList += "&<AppType>&" + get_ngformValue_template('AppType');
	//added by yash on 21 nov

		attrbList += "&<gender>&" + get_ngformText_template('cmplx_Customer_gender');
	attrbList += "&<MAritalStatus>&" + get_ngformText_template('cmplx_Customer_MAritalStatus');
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
		attrbList += "&<Account_Number>&" + get_ngformValue_template('Account_Number');
	// changes done by disha for Generate Techanges done by disha for Generate Template on 17-10-18 end
		attrbList += "&<Emp_Type>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6),get_ngform_controlObject('EmpType'));
		attrbList += "&<targetSegCode>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_targetSegCode'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,22),get_ngform_controlObject('TargetSegmentCode')));
		
		attrbList += "&<marketcode>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_marketcode'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,28),get_ngform_controlObject('MarketingCode')));
		attrbList += "&<Top_UP>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_NetPayout');
		attrbList += "&<Micro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Micro');
		attrbList += "&<Macro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Macro');
		attrbList += "&<Strength>&" + get_ngformText_template_dev('cmplx_DEC_Strength');
		attrbList += "&<Weakness>&" + get_ngformText_template_dev('cmplx_DEC_Weakness');
		attrbList += "&<Maturity_Age>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_AgeAtMaturity');
		attrbList += "&<Total_Exposure>&" + get_ngformValue_template('cmplx_Liability_New_AggrExposure');

		attrbList += "&<PLST>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_EmpStatusPL'):" "+"@10");
		attrbList += "&<PLE>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_Status_PLExpact'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,35),get_ngform_controlObject('EmployerStatusPLExpactt')));
		attrbList += "&<PLN>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_Status_PLNational'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,36),get_ngform_controlObject('EmployerStatusPLNational')));
		attrbList += "&<PLCAT>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,33),get_ngform_controlObject('EmployerCategoryPL')));
		attrbList += "&<PLCATC>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_EmpStatusCC'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,34),get_ngform_controlObject('EmployerStatusCC')));
		attrbList += "&<ALOC_CC>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_remarks'):" "+"@10");
		//change by saurabh for PCSP-315
		attrbList += "&<ALOC_PL>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_Aloc_RemarksPL'):" "+"@10");
		attrbList += "&<High>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformValue_template('cmplx_EmploymentDetails_highdelinq'):get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,32));
		
		attrbList += "&<NepType>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_NepType'):get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,23),get_ngform_controlObject('NepType')));
		attrbList += "&<Resident>&" + get_ngformText_template('cmplx_Customer_RESIDENTNONRESIDENT');
		attrbList += "&<Age>&" + get_ngformValue_template('cmplx_Customer_Age');
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
		//Added by tarang on 03/04/2018
		attrbList += "&<Bank_From>&" + get_ngformGridColumn_template("cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails",0,1);
		attrbList += "&<Bank_To>&" + get_ngformGridColumn_template("cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails",0,2);
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
attrbList += "&<CRN>&" + get_ngformValue_template('CRN');
attrbList += "&<customer_name>&" + get_ngformValue_template('CustomerLabel');
attrbList += "&<Current_date_time>&" + get_ngformValue_template('CurrentDateLabel')+ ' '+new Date().getHours()+':'+new Date().getMinutes();

attrbList += "&<DBRNET>&" + get_ngformValue_template('cmplx_Liability_New_DBRNet');
//below line commented by saurabh for PCSP-248 on 24th Dec
//attrbList += "&<FinalDBR>&" + get_ngformValue_template('cmplx_Liability_New_DBR');

attrbList += "&<TAI>&" + get_ngformValue_template('cmplx_Liability_New_TAI');

attrbList += "&<InsuranceAmount>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_InsuranceAmount');

attrbList += "&<EMI>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_EMI');
attrbList += "&<Dec_Remarks>&" + get_ngformValue_template('cmplx_DEC_Remarks');
attrbList += "&<ReferTo>&" + get_ngformValue_template('ReferTo');
attrbList += "&<ReferReason>&" + get_ngformText_template('DecisionHistory_dec_reason_code');
attrbList += "&<Decision>&" + get_ngformValue_template('cmplx_DEC_Decision');

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
//attrbList += "&<IncInPL>&" + get_ngformValue_template('cmplx_EmploymentDetails_IncInPL');
//attrbList += "&<IncInCC>&" + get_ngformValue_template('cmplx_EmploymentDetails_IncInCC');
attrbList += "&<Deferred>&" + get_ngformValue_template('DeferredUntilDate');
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
	attrbList += "&<DectechDecision>&" + get_ngformValue_template('cmplx_DEC_DectechDecision');
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
		//alert(csmNext_Result);
		var rem1='',rem2='',rem3='',rem4='',rem5='',rem6='',rem7='',rem8='',rem9='',rem10='';
		var rows = csmNext_Result.trim().split('~');
		for(var i=0;i<rows.length-1;i++){
			var workstep=rows[i].split('#')[0];
			var username=rows[i].split('#')[1];
			var cadlevel=rows[i].split('#')[2];
			if(workstep=='CAD_Analyst1'){
				rem1=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L1'){
				rem6=username;
			}
			else if(workstep=='Cad_Analyst2' && cadlevel=='L2'){
				rem2=username;
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
                      
                 if(product_type=='Islamic' && product1_type=='Instant Money'){
				attrbList+="&<header>&INSTANT FINANCE@10";
				attrbList+="&<loan>&FINANCE@10";

				}
				else if(product_type=='Conventional' && product1_type=='Instant Money'){
				attrbList+="&<header>&INSTANT MONEY@10";
                attrbList+="&<loan>&Loan@10";
				}
				else if(product_type=='Conventional' && product1_type=='Limit Increase'){
				attrbList+="&<header>&CREDIT CARD LIMIT INCREASE@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				if(product_type=='Islamic' && product1_type=='Limit Increase'){
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
				else if(product_type=='Islamic' && product1_type=='SEC'){
				attrbList+="&<header>&SECURED CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='SEC'){
				attrbList+="&<header>&SECURED CREDIT CARD@10";
				attrbList+="&<loan>&Loan@10";
				

				}
				else if(product_type=='Islamic' && product1_type=='SE'){
				attrbList+="&<header>&Self Employed - CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='SE'){
				attrbList+="&<header>&Self Employed - CREDIT CARD@10";
				attrbList+="&<loan>&Loan@10";
				}
				
				else if(product_type=='Islamic' && product1_type=='BTC'){
				attrbList+="&<header>&BTC - CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='BTC'){
				attrbList+="&<header>&BTC- CREDIT CARD@10";
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
				
				else if(product_type=='Islamic' && product1_type=='SE'){
				attrbList+="&<header>&Self Employed - CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='SE'){
				attrbList+="&<header>&Self Employed- CREDIT CARD@10";
				attrbList+="&<loan>&Loan@10";
				}
				
				else if(product_type=='Islamic' && product1_type=='PA'){
				attrbList+="&<header>&PRE - APPROVED CREDIT CARD@10";
				attrbList+="&<loan>&Finance@10";
				

				}
				else if(product_type=='Conventional' && product1_type=='PA'){
				attrbList+="&<header>&PRE - APPROVED CREDIT CARD@10";
				attrbList+="&<loan>&Loan@10";
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
			   }
				}

			
return attrbList;
}
function fetch_remarks(attrbList,GridName){

	var remarks_Map={'Remarks_Init':'','Remarks_Ops':'','Remarks_Feedback':'','Remarks_CPV':'','Remarks_Sales':'','Remarks_CPV_Feed':'','Remarks_CAD':'','Remarks_CAD1':'','Remarks_CAD2':'','Remarks_Compliance':''};
	//changes done by saurabh for PCSP-23 on 7th Feb 19.
	var workstepPlaceholderMap = {'Branch_Init':'Remarks_Init','Original_Validation':'Remarks_Ops','FCU':'Remarks_Feedback','CPV':'Remarks_CPV',
	'Collection_Agent_Review':'Remarks_Sales','Smart_CPV':'Remarks_CPV_Feed','CAD_Analyst1':'Remarks_CAD1','CAD_Analyst2':'Remarks_CAD2','Cad_Analyst2':'Remarks_CAD2','Compliance':'Remarks_Compliance'};
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

					}
				else if(workstepName =='CAD_Analyst2' || workstepName=='Cad_Analyst2'){
					//attrbList += "&<Remarks_CAD>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_CAD2']= get_ngformGridColumn_template(GridName,i,4);

					}
				else if(workstepName =='Compliance'){
					//attrbList += "&<Remarks_Compliance>&"+get_ngformGridColumn_template(GridName,i,4);
					remarks_Map['Remarks_Compliance']= get_ngformGridColumn_template(GridName,i,4);

					}
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
							
							var Query1 = "OfficeCountry";
							//var Query1 = "SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code ='"+ com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8)+"'";
							wparams="Code=="+com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
							params="Query="+Query1+"&wparams="+wparams+"&pname="+pname;
							var country_Query = CallAjax(jspName,params);
							var fields1 = country_Query.split('#');
							var country = fields1[0]+"@10";
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
     attrbList+=  ofc_value + residence_value + home_value ;
    return attrbList;
            
}


function RLOSTemplateData()
{
	var attrbList = "";
	var pname = 'DocTemplateRLOS';
	var wparams='';
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	//attrbList+=rlos_gurantor(attrbList);
	attrbList+=rlosloan_template(attrbList,"P");
	attrbList+=BT_rlos(attrbList);
	attrbList+=IM_rlos(attrbList);
	attrbList+=get_Reference_Detail(attrbList);
	//saurabh. 4/3/19 point 2.
	attrbList += "&<customer_name>&" + get_ngformValue_template('CUSTOMERNAME');
	attrbList += "&<PAssportNo>&" + get_ngformValue_template('cmplx_Customer_PAssportNo');
	// changes done by disha for Generate Template on 17-10-18 start
	attrbList += "&<Customer_cifno>&" + get_ngformValue_template('cmplx_Customer_CIFNo');
	// changes done by disha for Generate Template on 17-10-18 end
	
	attrbList += "&<Current_date_time>&" + get_ngformValue_template('lbl_curr_date_val');
		attrbList += "&<WIname>&" + get_ngformValue_template('WI_name');
attrbList += "&<gender>&" + get_ngformText_template('cmplx_Customer_gender');  
attrbList += "&<MArtialStatus>&" + get_ngformText_template('cmplx_Customer_MAritalStatus');	
//saurabh. 4/3/19 point 5.
attrbList += "&<EmploymentType>&" + get_ngformGridColumn_template('cmplx_Product_cmplx_ProductGrid',0,6);	
attrbList += "&<Basic>&" + get_ngformValue_template('cmplx_IncomeDetails_Basic');	

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
attrbList += "&<Salary_transfer>&" + get_ngformValue_template('cmplx_IncomeDetails_SalaryXferToBank');
}else{ attrbList += "&<Salary_transfer>&@10";}
//attrbList += "&<outstanding>&" + get_ngformValue_template('Outsatanding_Amount');
//attrbList += "&<obligations>&" + get_ngformValue_template('Consider_For_Obligations');	

attrbList += "&<CountryOFResidence>&" + get_ngformText_template('cmplx_Customer_COuntryOFResidence');
attrbList += "&<CountryOfResidence>&" + get_ngformText_template('cmplx_Customer_COuntryOFResidence');
attrbList += "&<TotalFinalLimit>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalLimit');

//saurabh. 4/3/19 point 4.
attrbList += "&<YearsInUAE>&" + get_ngformValue_template('cmplx_Customer_yearsInUAE');
//changes by saurabh for Id doc for new application form template
	if(getNGValue('cmplx_Customer_EmiratesID')!=''){
	attrbList += "&<DocType>&Emirates ID@10";	
	attrbList += "&<id_doc>&" + get_ngformValue_template('cmplx_Customer_EmiratesID');	
	}
	else if(getNGValue('cmplx_Customer_PAssportNo')!=''){
	attrbList += "&<DocType>&Passport@10";
	attrbList += "&<id_doc>&" + get_ngformValue_template('cmplx_Customer_PAssportNo');	
	}
	else{
	attrbList += "&<DocType>&@10";
	attrbList += "&<id_doc>&@10";	
	}
var Query="DesignationCheck";
wparams="Code=="+com.newgen.omniforms.formviewer.getNGValue('cmplx_EmploymentDetails_Designation');
var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
var DB_Query = CallAjax(jspName,params);
DB_Query = trimStr(DB_Query);
var fields = DB_Query.split('#');
var retVal = fields[0];
attrbList += "&<Designation>&" + retVal +"@10";

//saurabh . 4/3/19 point 17.
var CTQuery="CardTypesList";
wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
var	params="Query="+CTQuery+"&wparams="+wparams+"&pname="+pname;
var CTDB_Query = CallAjax(jspName,params);
CTDB_Query = trimStr(CTDB_Query);
var fields = CTDB_Query.split('#');
var retVal = fields[0];
attrbList += "&<CardTypes>&" + retVal +"@10";

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
attrbList += "&<DOJ>&" + get_ngformValue_template('cmplx_EmploymentDetails_DOJ');
//saurabh. 4/3/19. point 8.
attrbList += "&<DOL>&" + get_ngformValue_template('cmplx_EmploymentDetails_DateOfLeaving');

attrbList += "&<JobConfirmed>&" + get_ngformValue_template('cmplx_EmploymentDetails_JobConfirmed');

//changes by saurabh on 14th Nov.
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
attrbList += "&<EmirateOfWorkself>&" + get_ngformText_template('cmplx_EmploymentDetails_EmirateOfWork');
}
else{
attrbList += "&<EmirateOfWorkself>&" + '@10';
}
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')=='Self Employed'){
attrbList += "&<HeadOfficeEmirateself>&" + get_ngformValue_template('cmplx_EmploymentDetails_HeadOfficeEmirate');
}
else{
attrbList += "&<HeadOfficeEmirateself>&" + "@10";
}
//change end.

//saurabh. 4/3/19 point 9.
attrbList += "&<Accomodation>&" + get_ngformValue_template('cmplx_IncomeDetails_Accomodation');
//saurabh. 4/3/19 point 15.
attrbList += "&<SMSOptOut>&" + get_ngformValue_template('cmplx_CardDetails_SMSOptOut');
//saurabh. 4/3/19 point 18.
attrbList += "&<cardEmbossName>&" + get_ngformValue_template('cmplx_CardDetails_CardEmbossingName');
	
if(com.newgen.omniforms.formviewer.getNGValue('EmploymentType')!='Self Employed'){
	attrbList += "&<SalaryDay>&" + get_ngformValue_template('cmplx_IncomeDetails_SalaryDay');
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
attrbList += "&<MobileNo1>&" + get_ngformValue_template('AlternateContactDetails_MobileNo1');
attrbList += "&<MobileNo2>&" + get_ngformValue_template('AlternateContactDetails_MobNo2');
attrbList += "&<ResidenceNo>&" + get_ngformValue_template('AlternateContactDetails_ResidenceNo');
attrbList += "&<HomeCOuntryNo>&" + get_ngformValue_template('AlternateContactDetails_HomeCOuntryNo');
attrbList += "&<OfficeNo>&" + get_ngformValue_template('AlternateContactDetails_OfficeNo');
attrbList += "&<Email2>&" + get_ngformValue_template('AlternateContactDetails_Email2');
attrbList += "&<Email1>&" + get_ngformValue_template('AlternateContactDetails_Email1');

attrbList += "&<yearsinUAE>&" + get_ngformValue_template('cmplx_Customer_yearsInUAE');


if(com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_GuarantorDetails_cmplx_GuarantorGrid')>0){
	attrbList += "&<guarantor_Name>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,7) + ' '+get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,8) + ' '+ get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,9);
	attrbList += "&<Guarantor_passport>&" + get_ngformGridColumn_template("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,5);
}
else{attrbList += "&<guarantor_Name>&@10"; attrbList += "&<Guarantor_passport>&@10"; }
attrbList+=rlosloan_template(attrbList,"G");
	attrbList += "&<custName_Supp>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,0) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,1) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,2);
//attrbList += "&<middle_sup>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,1);
//attrbList += "&<lasu>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,2);
attrbList += "&<card_embossing>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,11);

attrbList += "&<passport_sup>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,3);
attrbList += "&<Relationship>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,10);

attrbList += "&<mobile_sup>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",0,6);
	
	attrbList += "&<custName_Supp2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,0) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,1) + ' ' + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,2);
//attrbList += "&<middle_sup2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,1);
//attrbList += "&<last_sup2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,2);
attrbList += "&<card_embossing2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,11);

attrbList += "&<passport_sup2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,3);
attrbList += "&<Relationship2>&" + get_ngformGridColumn_template("SupplementCardDetails_cmplx_SupplementGrid",1,10);
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
	
	
	attrbList += "&<PILname>&" + get_ngformValue_template('cmplx_Customer_LAstNAme');
	attrbList += "&<PIFname>&" + get_ngformValue_template('cmplx_Customer_FIrstNAme');
	attrbList += "&<PIMname>&" + get_ngformValue_template('cmplx_Customer_MiddleName');
	attrbList += "&<PIDOB>&" + get_ngformValue_template('cmplx_Customer_DOb');
	
	var flag = false;
	for(var i=0;i<getLVWRowCount('cmplx_OECD_cmplx_GR_OecdDetails');i++){
	if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,8).indexOf("P-")>-1){
		attrbList += "&<PITOB>&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,3) + "@10";
		attrbList += "&<PICOB>&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,2) + "@10";
		attrbList += "&<CTR>&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,4) + "@10";
		attrbList += "&<TINNo>&" + getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,5) + "@10";
		flag = true;		
		break;
	}
	}
	if(!flag){
	attrbList += "&<PITOB>&"  + "@10";
		attrbList += "&<PICOB>&" + "@10";
		attrbList += "&<CTR>&" +  "@10";
		attrbList += "&<TINNo>&"  + "@10";
	}
	
	
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
	
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WI_name');
	var	params="Query="+Query2+"&wparams="+wparams+"&pname="+pname;
	var NTB_Query = CallAjax(jspName,params);
	NTB_Query = trimStr(NTB_Query);
	var fields = NTB_Query.split('#');
	var NTB = fields[0]+"@10";
	//saurabh. 4/3/19 point 1.
	if(NTB=='false@10')
		attrbList += "&<NTB>&YES@10";
	else if(NTB=='true@10')
		attrbList += "&<NTB>&NO@10";
	else
		attrbList += "&<NTB>&"+"@10";
		
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

function PLTemplateData()
{
	 var attrbList="";
    var attrbList_arr = "company,compName;company_cifno,cif";
	//var age = "";
	var pname = "DocTemplatePL";
	attrbList+=creditcard_check(attrbList);
	attrbList+=creditcard_template(attrbList);
	attrbList+=creditcard_notepad(attrbList);
	attrbList+=fetch_remarks(attrbList,'Decision_ListView1');
	attrbList+=creditcard_selectPL(attrbList);
	attrbList+=creditcard_selectCC(attrbList,'Decision_ListView1');
	//CHANGE BY SAURABH ON 11-12 FOR JIRA - PCSP-31
	attrbList += "&<Current_date_time>&" + get_ngformValue_template('CurrentDateLabel')+ ' '+new Date().getHours()+':'+new Date().getMinutes();
	attrbList += "&<PL_wi_name>&" + get_ngformValue_template('WiLabel');	
	attrbList += "&<company>&" + get_ngformValue_template('compName');
    attrbList += "&<company_cifno>&" + get_ngformValue_template('cif');
    attrbList += "&<trade>&" + get_ngformValue_template('TLExpiry');
    attrbList += "&<comapny_lob>&" + get_ngformValue_template('lob');
    attrbList += "&<passport_valid>&" + get_ngformValue_template('AuthorisedSignDetails_PassportExpiryDate');
    //attrbList += "&<AppType>&" + get_ngformValue_template('AppType');
	//added by yash on 21 nov
	
	// changes done by disha for Generate Template on 17-10-18 start
		attrbList += "&<Customer_cifno>&" + get_ngformValue_template('cmplx_Customer_CIFNo');
		attrbList += "&<FinalDBR>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalDBR');
		attrbList += "&<FinalTAI>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalTAI');
		attrbList += "&<Entry_Date>&" + get_ngformValue_template('Introduction_Date');
		attrbList += "&<Account_Number>&" + get_ngformValue_template('Account_Number');
		attrbList += "&<EmpName>&" + get_ngformValue_template('cmplx_EmploymentDetails_EmpName');
		attrbList += "&<EMpCode>&" + get_ngformValue_template('cmplx_EmploymentDetails_EMpCode');
		attrbList += "&<VisaSponser>&" + get_ngformValue_template('cmplx_EmploymentDetails_VisaSponser');
		attrbList += "&<LOS>&" + get_ngformValue_template('cmplx_EmploymentDetails_LOS');
	// changes done by disha for Generate Template on 17-10-18 end
		
		attrbList += "&<Emp_Type>&" + get_ngformText_template('cmplx_EmploymentDetails_Emp_Type');	
		attrbList += "&<targetSegCode>&" + get_ngformText_template('cmplx_EmploymentDetails_targetSegCode');
	
		attrbList += "&<PLST>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatusPL');
		attrbList += "&<PLE>&" + get_ngformText_template('cmplx_EmploymentDetails_categexpat');
		attrbList += "&<PLN>&" + get_ngformText_template('cmplx_EmploymentDetails_categnational');
		attrbList += "&<PLCAT>&" + get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer');
		attrbList += "&<PLCATC>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatusCC');
		attrbList += "&<ALOC_CC>&" + get_ngformValue_template('cmplx_EmploymentDetails_remarks');
		attrbList += "&<ALOC_PL>&" + get_ngformValue_template('cmplx_EmploymentDetails_Remarks_PL');
		attrbList += "&<High>&" + get_ngformValue_template('cmplx_EmploymentDetails_highdelinq');
		
		attrbList += "&<NepType>&" + get_ngformText_template('cmplx_EmploymentDetails_NepType');
		attrbList += "&<DectechDecision>&" + get_ngformValue_template('cmplx_Decision_Dectech_decsion');
	
		attrbList += "&<gender>&" + get_ngformText_template('cmplx_Customer_gender');
	attrbList += "&<MAritalStatus>&" + get_ngformText_template('cmplx_Customer_MAritalStatus');
	var Query = "Nationality_CC";
	wparams="Code=="+get_ngformValue_template('cmplx_Customer_Nationality');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var nat_Query = CallAjax(jspName,params);
	var fields = nat_Query.split('#');
	attrbList += "&<Nationality>&" + fields[0]+'@10';
		attrbList += "&<National>&" + get_ngformText_template('cmplx_Customer_GCCNational');

		attrbList += "&<EmpStatus>&" + get_ngformText_template('cmplx_EmploymentDetails_EmpStatus');
		//changes for PCSP 23 by saurbh on 8th Jan
		attrbList += "&<Accprovided>&" + get_ngformValue_template('cmplx_IncomeDetails_Accomodation');
		attrbList += "&<Confirmed>&" + get_ngformValue_template('cmplx_EmploymentDetails_JobConfirmed');
		attrbList += "&<Movement>&" + get_ngformText_template('cmplx_EmploymentDetails_empmovemnt');
		Query = "Designation_CC";
	wparams="Code=="+get_ngformValue_template('cmplx_EmploymentDetails_Designation');
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var desig_Query = CallAjax(jspName,params);
	var fields = desig_Query.split('#');
		attrbList += "&<Cuurent_Designation>&" + fields[0]+'@10';
		attrbList += "&<Maturity_Age>&" + get_ngformValue_template("cmplx_LoanDetails_ageatmaturity");

		attrbList += "&<Micro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Micro');
		attrbList += "&<Macro>&" + get_ngformText_template('cmplx_EmploymentDetails_Indus_Macro');
		attrbList += "&<Strength>&" + get_ngformText_template_dev('cmplx_Decision_strength');
		attrbList += "&<Weakness>&" + get_ngformText_template_dev('cmplx_Decision_weakness');

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
attrbList += "&<ECRN>&" + get_ngformText_template('ECRN');
attrbList += "&<CRN>&" + get_ngformText_template('CRN');
attrbList += "&<customer_name>&" + get_ngformValue_template('CustomerLabel');
//attrbList += "&<Curent_date_time>&" + get_ngformValue_template('CurrentDateLabel');
attrbList += "&<DBRNET>&" + get_ngformValue_template('cmplx_Liability_New_DBRNet');
attrbList += "&<FinalDBR>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_FinalTai');
attrbList += "&<TAI>&" + get_ngformValue_template('cmplx_Liability_New_TAI');
attrbList += "&<Total_Exposure>&" + get_ngformValue_template('cmplx_Liability_New_AggrExposure');
attrbList += "&<marketcode>&"+" "+"@10";
attrbList += "&<InsuranceAmount>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_InsuranceAmount');
attrbList += "&<TakeOver_Product>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,7),get_ngform_controlObject('Scheme'));
attrbList += "&<TakeOver_Code>&"+" "+"@10";
attrbList += "&<Top_UP>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_NetPayout');
attrbList += "&<EMI>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_EMI');
attrbList += "&<Dec_Remarks>&" + get_ngformValue_template('cmplx_Decision_REMARKS');
attrbList += "&<ReferTo>&" + get_ngformValue_template('ReferTo');
attrbList += "&<ReferReason>&" + get_ngformText_template('DecisionHistory_DecisionReasonCode');
attrbList += "&<Decision>&" + get_ngformText_template('cmplx_Decision_Decision');
		attrbList += "&<CROPS>&"+getCropsNotepad('NDISB') +"@10";
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
attrbList += "&<IMAMOUNT>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_EMI');
attrbList += "&<FirstRepayDate>&" + get_ngformValue_template('cmplx_LoanDetails_frepdate');
attrbList += "&<Last_Repay>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_MaturityDate');
//change by saurabh on 4 Feb 19 for PCSP-898
Query = "CardType";
	wparams="WiName=="+window.parent.pid;
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
	var desig_Query = CallAjax(jspName,params);
	var fields = desig_Query.split('#');
		attrbList += "&<Card_prod>&" + fields[0]+'@10';
attrbList += "&<score_grade>&" + get_ngformValue_template('cmplx_Decision_score_grade');
attrbList += "&<Processing>&" + get_ngformValue_template('cmplx_EligibilityAndProductInfo_LPF');
    //attrbList += "&<Scheme>&" + get_ngformValue_template('Scheme');
 attrbList += "&<company_name>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4);
 attrbList += "&<FieldVisitedDone>&" + ((get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,6).split('@')[0].indexOf('Salaried')>-1)?get_ngformText_template('cmplx_EmploymentDetails_FieldVisitDone'):" "+"@10");
 attrbList += "&<LoanMultiple>&" + calcLoanMultiple();
 attrbList += "&<Applicant_Category>&" + get_ngformText_template('cmplx_Customer_CustomerCategory');
  attrbList += "&<Employer_Category>&" + get_ngformText_template('cmplx_EmploymentDetails_CurrEmployer');
 attrbList += "&<comp_CIF>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,3);
 attrbList += "&<TL_Expiry>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,7);
  attrbList += "&<Indus_sector>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,8);
   //attrbList += "&<application>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",0,4);
    attrbList += "&<LOB>&" + get_ngformGridColumn_template("cmplx_CompanyDetails_cmplx_CompanyGrid",1,15);
	attrbList += "&<Product_type>&" + get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,0);
	attrbList += "&<requested_limit>&" + get_ngformValue_template("cmplx_EligibilityAndProductInfo_FinalLimit");
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
	attrbList += "&<Card_Product>&" + " "+"@10";
	attrbList += "&<Scheme>&" + get_ngformGridCode_Description(get_ngformGridColumn_template("cmplx_Product_cmplx_ProductGrid",0,7),get_ngform_controlObject('Scheme'));
	attrbList += "&<WORSTL24>&" + get_ngformGridColumn_template("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",0,17);
	attrbList += "&<SalaryDay>&" + get_ngformValue_template('cmplx_IncomeDetails_SalaryDay');
	attrbList += "&<Salary_transfer>&" + get_ngformText_template('cmplx_IncomeDetails_SalaryXferToBank');
	attrbList += "&<MOL_Var>&" + get_ngformValue_template('cmplx_MOL_molsalvar');
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
	var Query="CSMNext";
	//var Query = "SELECT top 1 isnull(csmNext,'') as csmNext,isnull(userName,'') as userName FROM NG_RLOS_GR_DECISION with (nolock) WHERE dec_wi_Name ='"+ com.newgen.omniforms.formviewer.getNGValue('WiLabel')+"' order by csmNext";
		var wparams="WiName=="+com.newgen.omniforms.formviewer.getNGValue('WiLabel');
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		var	params="Query="+Query+"&wparams="+wparams+"&pname="+pname;
		var csmNext_Result = CallAjax(jspName,params);
		//alert(csmNext_Result);
		var fields = csmNext_Result.split('#');
		var csmNext = fields[0]+"@10";
		var userName = fields[1]+"@10";
		attrbList += "&<Remarks1>&"+" "+"@10";
		attrbList += "&<Remarks2>&"+" "+"@10";
		attrbList += "&<Remarks3>&"+" "+"@10";
		attrbList += "&<Remarks4>&"+" "+"@10";
		attrbList += "&<Remarks5>&"+" "+"@10";
		attrbList += "&<Remarks6>&"+" "+"@10";
		attrbList += "&<Remarks7>&"+" "+"@10";
		attrbList += "&<Remarks8>&"+" "+"@10";
		attrbList += "&<Remarks9>&"+" "+"@10";
		attrbList += "&<Remarks10>&"+" "+"@10";
		if (csmNext=='L1@10')
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
		}
	
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

function BT_rlos(attrbList)
	 {
		 var attrbList = "";
		 var bt_rlos1 = "";
		 if (com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CC_Loan_cmplx_btc") >= 0){
               for(var i=0; i <com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_CC_Loan_cmplx_btc"); i++){
                    var bt_rlos1= com.newgen.omniforms.formviewer.getLVWAT("cmplx_CC_Loan_cmplx_btc",i,0);
					if(bt_rlos1 =='BT'){
						                      attrbList = "&<BT_CARD>&"+get_ngformGridColumn_template("cmplx_CC_Loan_cmplx_btc",i,1)+"@10";
                    }
					
	 }
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
