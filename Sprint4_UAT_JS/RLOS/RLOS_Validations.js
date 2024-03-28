
function DMandatoryRLOS()
{
	if (getNGValue('IS_PRODUCT_DUPLICATE') == 'Yes')
	{
		showAlert('Add', alerts_String_Map['VAL027']);
		return false;
		
	}
	return true;
}
	
function DMandatory(){
	var mField = RLOS_MANDATORY_FIELDS.split(",");
		for(var i = 0; i < mField.length; i++) {
			var j = mField[i].toString().split("#");
				if(!isFieldFilled(j[0],j[1])){
				showAlert(j[0],j[1]+" is mandatory.");
				return false;				
				}
		}
	return true;
}

//below function by saurabh for incoming doc new
function checkMandatoryIncomingDoc(){
	var docType=getNGValue('IncomingDocNew_DocType');
	var docName=getNGValue('IncomingDocNew_DocName');
	var expDate=getNGValue('IncomingDocNew_ExpiryDate');
	var docStatus=getNGValue('IncomingDocNew_Status');
	var docIndex=getNGValue('IncomingDocNew_Docindex');
	var defuntildate = getNGValue('IncomingDocNew_DeferredUntilDate');
	
	if(docType=='' || docType=='--Select--'){
		showAlert('IncomingDocNew_DocType','Please select Document Type');
		return false;
	}
	else if(docName=='' || docName=='--Select--'){
		showAlert('IncomingDocNew_DocName','Please select Document Name');
		return false;
	}
	/*else if(expDate=='' || expDate=='--Select--'){
		showAlert('IncomingDocNew_ExpiryDate','Please select Expiry Date');
		return false;
	}*/
	else if(docStatus=='' || docStatus=='--Select--'){
		showAlert('IncomingDocNew_Status','Please select Document Status');
		return false;
	}
	else if((docStatus!='' && docStatus!='--Select--') && docStatus=='Deferred' && (defuntildate=='' || defuntildate==null)){
		showAlert('IncomingDocNew_DeferredUntilDate','Please input Deferred until Date');
		return false;
	}
	else if(docIndex=='' && docStatus=='Received'){
		showAlert('IncomingDocNew_Status','Kindly attach Document first');
		return false;
	}
	return true;
}
//below function by saurabh for incoming doc new
function checkDuplicateRow(){
	var docType=getNGValue('IncomingDocNew_DocType');
	var docName=getNGValue('IncomingDocNew_DocName');
	var rows = getLVWRowCount('cmplx_IncomingDocNew_IncomingDocGrid');
	for(var i=0;i<rows;i++){
		if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,0)==docType && getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,1)==docName){
			return true;
		}
	}
	return false;
}

function checkMandatory_FetchDetails()
{
	var Id=getNGValue('cmplx_Customer_EmiratesID');
	var Lname=getNGValue('cmplx_Customer_LAstNAme');
	var MobNo=getNGValue('cmplx_Customer_MobNo');
	var PassNo=getNGValue('cmplx_Customer_PAssportNo');
	var Nat=getNGValue('cmplx_Customer_Nationality');
	var Dob=getNGValue('cmplx_Customer_DOb');
	var CIF=getNGValue('cmplx_Customer_CIFNO');
	var Fname = getNGValue('cmplx_Customer_FIrstNAme');
	var cifcheckAvail= getNGValue('cmplx_Customer_card_id_available');
	var nep = getNGValue('cmplx_Customer_NEP');	
			
	if(((Id=="")||(Id.length != 15) ||(Id.substr(0,3)!='784'))&&(cifcheckAvail==false)&&(nep=='') && isFieldFilled('cmplx_Customer_marsoomID')==false){
		if(Id.substr(0,3)!='784'){
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		else{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		return false;
	} //Arun 25/12/17 for Emirates id should not be mandatory for NEP customer
	/*else if(Id.charAt(0)!='7' ||  Id.charAt(1)!='8' || Id.charAt(2)!='4'){
			showAlert('cmplx_Customer_EmiratesID','First three digits are fixed-784');
			return false;
		}	*/
	/* Deepak as CIF is non mandatory for 
	else if(CIF==""){
		showAlert('cmplx_Customer_CIFNO','Please fill CIF ID');
		return false;
		}	*/
	if(CIF !="" && CIF.length!=7){
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL105']);
			return false;
		}
	else if((Fname=="")&&(cifcheckAvail==false)){
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL111']);
		return false;
		}
	else if((Lname=="")&&(cifcheckAvail==false)){
		showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL112']);
		return false;
		}
	else if((MobNo.length != 14)&&(cifcheckAvail==false)){
		showAlert('cmplx_Customer_MobNo',alerts_String_Map['VAL087']);
		return false;
		}
	else if((PassNo=="")&&(cifcheckAvail==false)){
		showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL115']);
		return false;
		}
	else if((Nat=="" || Nat=="--Select--")&&(cifcheckAvail==false)){
		showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL114']);
		return false;
		}
	else if((Dob=="")&&(cifcheckAvail==false)){
		showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL109']);	
		return false;
		}
		// added by yash for checking emirates ID with Valid DOB for PROC 745\
		//below condition modified by nikhil for PCSP-657
		else if((Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9)) && Id!='')
		{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL074']);
		return false;
		} //Arun 25/12/17 for Emirates id should not be mandatory for NEP customer
		// ended by yash 
	/*else {
		var year_dob=Dob.substring(6,10);
		var year_id=Id.charAt(3)+Id.charAt(4)+Id.charAt(5)+Id.charAt(6);
		if(year_dob!=year_id){
		showAlert('','Digit 4-7 must be same as date Of Birth');
		return false;
		}
	}	*/
return true;
}		

function checkMandatory_IncomeGrid()
{
	if(getNGValue('IncomeDetails_BankStatFrom')==""){
		showAlert('IncomeDetails_BankStatFrom',alerts_String_Map['VAL016']);
		return false;
		}
	/*sagarika CR bank not mandatory
	else if(getNGValue('IncomeDetails_BankStatFromDate')==""){
		showAlert('IncomeDetails_BankStatFromDate',alerts_String_Map['VAL017']);
		return false;
		}
	else if(getNGValue('IncomeDetails_BankStatToDate')==""){
		showAlert('IncomeDetails_BankStatToDate',alerts_String_Map['VAL018']);
		return false;
		}*/
	return true;
	
}

function checkMandatory_ReferenceGrid(opType)
{
	var gridrowCount = getLVWRowCount('cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails');
		if(opType == 'add' && gridrowCount==2){
		showAlert('',alerts_String_Map['VAL082']);
		setNGValueCustom('ReferenceDetails_ref_NAme',"");
		setNGValueCustom('ReferenceDetails_ref_mobile',"");
		setNGValueCustom('ReferenceDetails_reference_phone',"");
		return false;
		}
	if(getNGValue('ReferenceDetails_ref_NAme')==""){
		showAlert('ReferenceDetails_ref_NAme',alerts_String_Map['VAL126']);
		return false;
		}
	else if(getNGValue('ReferenceDetails_ref_mobile')==""){
		showAlert('ReferenceDetails_ref_mobile',alerts_String_Map['VAL125']);
		return false;
		}
	/*else if(getNGValue('ReferenceDetails_reference_phone')==""){
		showAlert('ReferenceDetails_reference_phone','Reference Phone cannot be blank');
		return false;
		}*///Arun (09/10)
	else if(isFieldFilled('ReferenceDetails_ref_Relationship')==false){
		showAlert('ReferenceDetails_ref_Relationship',alerts_String_Map['VAL127']);
		return false;
		}	
	return true;
}
	
function validate_ReferenceGrid()
{
	var n=getLVWRowCount("cmplx_ReferenceDetails_cmplx_ReferenceGrid");
	if(n==2){
		showAlert('cmplx_ReferenceDetails_cmplx_ReferenceGrid',alerts_String_Map['VAL082']);
		return false;
	}
	
	if(getLVWAT('cmplx_ReferenceDetails_cmplx_ReferenceGrid',0,0)==getNGValue('ReferenceDetails_ref_NAme') && getLVWAT('cmplx_ReferenceDetails_cmplx_ReferenceGrid',0,1)==getNGValue('ReferenceDetails_ref_mobile')){
		showAlert('cmplx_ReferenceDetails_cmplx_ReferenceGrid',alerts_String_Map['VAL005']);
		return false;
	}
return true;	
}

function checkMandatory_BTGrid()
{	
		if(getNGValue('cmplx_Customer_NTB')=='false' || !getNGValue('cmplx_Customer_NTB') && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM'))//PCASP-3191                         //hritik 12/12/21  added   !IM case
		{
			showAlert('','Service requests currently are available only for NTB customers in CAS');
			return false;
		}
		// Below code added by shweta
		if(isFieldFilled('cmplx_CC_Loan_BTC_CardProduct')==false || getNGValue('cmplx_CC_Loan_BTC_CardProduct')=='--Select--'
			||  getNGValue('cmplx_CC_Loan_BTC_CardProduct')=='')//added by shweta for pcasp-1377
		{
			showAlert('cmplx_CC_Loan_BTC_CardProduct','Please add Card Product');
			return false;	
		}
	 if(isFieldFilled('transtype')==false){
		showAlert('transtype',alerts_String_Map['VAL148']);
		return false;
		}	
	
	else if(getNGValue('transtype')=='BT'){
		 if(!checkMandatory(CC_Loan_BT)){
		  return false;
		 }
		/*Handled in CC_Constant as it is Mandatory for Swift & Cheque		
		if((getNGValue('transferMode')!='' && getNGValue('transferMode')=='S')&& getNGValue('creditcardNo')==''){
			showAlert('creditcardNo','Please Enter Credit Card Number');
				return false;
		}	*/
	}
	else if(getNGValue('transtype')=='CCC'){//pcasp-1377
		if(getNGValue('transferMode')==null ||  getNGValue('transferMode')=='--Select--' || getNGValue('transferMode')=='' ){
			showAlert('transferMode','Please Select Transfer Mode');
			return false;	
		} else if(getNGValue('transferMode')=='S'){
			 if(!checkMandatory(CC_Loan_CCC_S)){
				 return false;
			 }
		} else if(getNGValue('transferMode')=='C'){
			if(!checkMandatory(CC_Loan_CCC_C)){
				return false;
			}
		} else if(getNGValue('transferMode')=='A'){
			if(!checkMandatory(CC_Loan_CCC_A)){
					return false;
				}  
			}				
		}
		else if(getNGValue('transtype')=='LOC'){
			 if(!checkMandatory(CC_Loan_LOC)){
				 return false;
			 }
		}
		else if(getNGValue('transtype')=='SC'){//pcasp-1633
			if(getNGValue('transferMode')==null ||  getNGValue('transferMode')=='--Select--' || getNGValue('transferMode')=='' ){
				showAlert('transferMode','Please Select Transfer Mode');
				return false;	
			}else if(getNGValue('transferMode')=='C'){
		 		 if(!checkMandatory(CC_Loan_SmartCash_C)){
		 			  return false;
		 		 }
			} else if(getNGValue('transferMode')=='A'){
		 		 if(!checkMandatory(CC_Loan_SmartCash_A)){
		 			  return false;
		 		 }
			}  else if(getNGValue('transferMode')=='S'){
		 		 if(!checkMandatory(CC_Loan_SmartCash_S)){
		 			  return false;
		 		 }
			} 			
		}
	/*commented by shivang	
	 if((getNGValue('transtype')=='SC' && (getNGValue('transferMode')=='S' || getNGValue('transferMode')=='A'))
		||(getNGValue('transtype')=='CCC' && (getNGValue('transferMode')=='S' || getNGValue('transferMode')=='A'))){ //condition handled by shivang for PCASP-1633
		 if(getNGValue('creditcardNo').length <13|| getNGValue('creditcardNo').length >13){//pcasp-1377
				showAlert('creditcardNo', 'Please Enter a valid Account Number.');  
				return false;
			}
	 }*/
	 //PCASP-2877
	 if(getNGValue('iban').length >0 && getNGValue('iban').length <23){
		 showAlert('iban', 'Length of IBAN should be 23');  
		 return false;
	 }
	 else if(getNGValue('creditcardNo').length >0 && getNGValue('creditcardNo').length <16){
		showAlert('creditcardNo', 'Lenght of Credit Card Number should be 16');  
		 return false; 
	 }
	 //PCASP-1377
	 if((getNGValue('transtype')=='CCC' && getNGValue('transferMode')=='S') && getNGValue('iban').length <23){
		 showAlert('iban', 'Length of IBAN should be 23');  
		 return false;
	 }
	 //condition handled by shivang for PCASP-1633,PCASP-1377
	 if((getNGValue('transtype')=='SC'||getNGValue('transtype')=='CCC'||getNGValue('transtype')=='LOC') && getNGValue('transferMode')=='S'){
		 
		 if(getNGValue('Account_No_for_Swift')==''){
			showAlert('Account_No_for_Swift', 'Account Number cannot be Blank!');  
			return false; 
		 }
		 else if(getNGValue('Account_No_for_Swift').length <13|| getNGValue('Account_No_for_Swift').length >13){//pcasp-1377
				showAlert('Account_No_for_Swift', 'Please Enter a valid Account Number.');  
				return false;
			}
	 }
	 if((getNGValue('transtype')=='SC'||getNGValue('transtype')=='CCC'||getNGValue('transtype')=='LOC') && getNGValue('transferMode')=='A'){
		 
		 if(getNGValue('Account_No_for_AT')==''||getNGValue('Account_No_for_AT')=='--Select--'){
			showAlert('Account_No_for_AT', 'Account Number cannot be Blank!');  
			return false; 
		 }
	 }
	 /*if(checkDuplicateAccount()){
		 showAlert('creditcardNo', 'Account Number Should be same for all the LOC/SC/CCC Transaction type');  
		 return false;
	 }*/
	/*if(getNGValue('transferMode')=='C')pcasp-1377
	{
	if(!checkMandatory(CC_Loan_cheque)){
		  return false;
		 }
	}*/
	return true;
}
function checkDuplicateAccount()
{
    var n = getLVWRowCount('cmplx_CC_Loan_cmplx_btc');
    if(n>0)
	{
	 for(var i=0;i<n;i++)
	  {
        if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)!=getNGValue('creditcardNo') && getNGValue('transtype')!='BT' && getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)!='BT')
        {
			return true;
        }
      }
    }
    return false;
}

/*function validateKYCGrid()
{
	if(getNGValue('existingOldCustomer')==true)
		{
			var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_KYC_cmplx_KYCGrid');
		 if(getNGValue('ReqProd')=='Personal Loan')
			{
				
			for(var i=0;i<n;i++)
			{
				if(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0)=='Y' && getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,4)=='')
				{
					showAlert('',"KYC review date cannot be blank");
					return false;
				}
			}
			    //for existing customers
			}
		}
		return true;
}*/
function validate_BTGrid(pId)
{
	var n=getLVWRowCount("cmplx_CC_Loan_cmplx_btc");
	if(pId.indexOf('BT_Add')>-1){
		 if(n>0){
			for(var i=0;i<n;i++)
			{	// below changes done by shivang for PCASP-3191
				if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype') && 
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,2)==getNGValue('transferMode') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)==getNGValue('creditcardNo') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,21)==getNGValue('cmplx_CC_Loan_BTC_CardProduct') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,22)==getNGValue('Account_No_for_Swift') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,23)==getNGValue('Account_No_for_AT') )
					{//Added by shweta for pcasp-1401
					  showAlert('transtype',alerts_String_Map['VAL142']);	
					  return false;
				    }				
			}
		}
	}
		
	else if(pId.indexOf('BT_Modify')>-1){
		 if(n>0){
			for(var i=0;i<n;i++)
			{
				if((i!=parseInt(com.newgen.omniforms.formviewer.getNgSelectedIndices('cmplx_CC_Loan_cmplx_btc'))) && 
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype') && 
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,2)==getNGValue('transferMode') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)==getNGValue('creditcardNo') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,21)==getNGValue('cmplx_CC_Loan_BTC_CardProduct') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,22)==getNGValue('Account_No_for_Swift') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,23)==getNGValue('Account_No_for_AT') )
					{//Added by shweta for pcasp-1401
					  showAlert('transtype',alerts_String_Map['VAL142']);	
					  return false;
				    }
			}
		}
	}	
	return true;	
}
		
function Send_OTP_checkMandatory()
{
	if(getNGValue('OTP_Mobile_NO')==''){
		showAlert('OTP_Mobile_NO',alerts_String_Map['VAL103']);	
		return false;
		}
	else if(getNGValue('cmplx_Customer_CIFNO')==""){
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL107']);
		return false;
		}
	else if(getNGValue('cmplx_Customer_FIrstNAme')==""){
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL111']);	
		return false;
		}
	return true;
}	

	
function Validate_OTP_checkMandatory()
{
	if(getNGValue('OTP_No')==''){
		showAlert('OTP_No',alerts_String_Map['VAL104']);	
		return false;
		}
	else if(getNGValue('cmplx_Customer_MobNo')==""){
		showAlert('cmplx_Customer_MobNo',alerts_String_Map['VAL113']);
		return false;
		}	
	else if(getNGValue('cmplx_Customer_CIFNO')==""){
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL107']);
		return false;
		}
	else if(getNGValue('cmplx_Customer_FIrstNAme')==""){
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL111']);	
		return false;
		}
	return true;
}

function checkMandatory_ProductGrid()
{
	var Product_type=getNGValue('Product_type');
	var ReqProd=getNGValue('ReqProd');
	var AppType=getNGValue('AppType');
	var ReqLimit=getNGValue('ReqLimit');
	var EmpType=getNGValue('EmpType');
	var Priority=getNGValue('Priority');
	var SubProd=getNGValue('subProd');
	
		
			
	 if(Product_type=="" || Product_type=="--Select--"){
		showAlert('Product_type',alerts_String_Map['VAL150']);
		return false;
		}
	else if(ReqProd==""|| ReqProd=="--Select--"){
		showAlert('ReqProd',alerts_String_Map['VAL131']);
		return false;
		}
	else if(SubProd=="" || SubProd=='--Select--'){
		showAlert('subProd',alerts_String_Map['VAL139']);	
		return false;
		}
	else if(AppType=="--Select--" || AppType==""){
		showAlert('AppType',alerts_String_Map['VAL007']);
		return false;
		}
	else if(EmpType=="" || EmpType=='--Select--'){
		showAlert('EmpType',alerts_String_Map['VAL057']);
		return false;
		}	
	/*else if(ReqLimit=="" && SubProd!='PU'){
		showAlert('ReqLimit',alerts_String_Map['VAL130']);
		return false;
		}*/
	
	else if(Priority=="" || Priority=="--Select--"){
		showAlert('Priority',alerts_String_Map['VAL123']);	
		return false;
		}
		
	else if(ReqProd=='Personal Loan'){
		var tenor=getNGValue('ReqTenor');
		var scheme=getNGValue('Scheme');
		if(scheme=='' || scheme=='--Select--' || scheme=='0'){
			showAlert("Scheme",alerts_String_Map['VAL135']);
			return false;
			}
			
		if(tenor==""){
			showAlert('ReqTenor',alerts_String_Map['VAL132']);
			return false;
			}	
	}
		
	else if(ReqProd=='Credit Card' &&	(SubProd!='IM' && SubProd!='LI' && SubProd!='SEC' && SubProd!='BTC' )){
		var CardProd=getNGValue('CardProd');
		 if(CardProd=='' || CardProd=='--Select--'){
			showAlert("CardProd",alerts_String_Map['VAL030']);
			return false;
		}
	}	
	else if(SubProd=='LI' || SubProd=='PULI'){
			//var typeReq=getNGValue('typeReq');
		var LastPermanentLimit=getNGValue('LastPermanentLimit');
		var LastTemporaryLimit=getNGValue('LastTemporaryLimit');
		var NoOfMonths=getNGValue('NoOfMonths');
		/*if(LastPermanentLimit=='' && getNGValue('AppType')=='P'){
			showAlert("LastPermanentLimit",alerts_String_Map['VAL079']);
				return false;
		}*/
			
		/*else if(LastTemporaryLimit=='' && getNGValue('AppType')=='P'){
			showAlert("LastTemporaryLimit",alerts_String_Map['VAL091']);
				return false;
			}*/
		if((getNGValue('AppType')=='T' || getNGValue('AppType')=='NPULI') && NoOfMonths==''){
			showAlert("NoOfMonths",alerts_String_Map['VAL334']);
				return false;
			}
		}
		
		else if(SubProd=='SEC' || (SubProd=='BTC' && getNGValue('AppType')=='SMES'))
		{
			var FDAmt=getNGValue('FDAmount');
			if(FDAmt==''){
			showAlert("FDAmount",alerts_String_Map['VAL328']);
			return false;
		 }
		} //Arun (07/12/17)	 to get mandatory alert for FD Amount in product

	
	else if(ReqProd=='Credit Card' && SubProd=='IM'){
			if(getNGValue('ReqTenor')=="" || getNGValue('ReqTenor')==" "){
			showAlert("ReqTenor",alerts_String_Map['VAL132']);
				return false;
			}
		}
		
		/*if(ReqProd=='Credit Card' && SubProd=='SEC'){
			if(getNGValue('FDAmount')==""){
				showAlert("FDAmount",alerts_String_Map['VAL328']);
				return false;
			}
		}*/
return true;
}		

function checkMandatory_GuarantorGrid()
{
	if(getNGValue('GuarantorDetails_guardianCif')=='--Select--'){
		showAlert('GuarantorDetails_guardianCif', 'Please Select Guarantor CIF Available.');
		return false;
	}
	if(getNGValue('cif')=='' && getNGValue('GuarantorDetails_guardianCif')!='No'){
		showAlert('cif', 'Please Enter Guarantor CIF');
		return false;
	}
	else if(flag_ReadFromCIF==false && isLocked('ReadFromCIF')==false) {
		showAlert('ReadFromCIF',alerts_String_Map['VAL124']);
		return false;
	}	
	else if(getNGValue('GuarantorDetails_apptype')=='--Select--'){
		showAlert('GuarantorDetails_apptype', 'Please Select Applicant Type.');
		return false;
	}
	
	else if (getNGValue('Fname') == '' || getNGValue('Fname')==null || getNGValue('Fname')==undefined)
    {
        showAlert('Fname', alerts_String_Map['VAL061']);
        return false;
    }
	else if (getNGValue('lname') == '' || getNGValue('lname')==null || getNGValue('lname')==undefined)
    {
        showAlert('lname', alerts_String_Map['VAL076']);
        return false;
    }
	else if(!validateCustomerName(getNGValue('Fname'),getNGValue('Mname'),getNGValue('lname'))){
		showAlert('Fname','Customer Full Name cannot exceed 80 characters');
		return false;
	}
	else if (getNGValue('passpNo') == '')
    {
        showAlert('passpNo', alerts_String_Map['VAL097']);
        return false;
    }
	else if ((getNGValue('title') == '')||(getNGValue('title') == '--Select--'))
    {
        showAlert('title', alerts_String_Map['VAL144']);
        return false;
    }
	else if (getNGValue('eid_gua') == '')
    {
        showAlert('eid_gua', alerts_String_Map['VAL055']);
        return false;
    }
	//Tanshu Aggarwal(19/07/2017) for proc 888
	else if (getNGValue('dob') == '')
    {
        showAlert('dob', alerts_String_Map['VAL045']);
        return false;
    }
	//(19/07/2017) for proc 888
	//(19/07/2017) for proc 888
	else if (isFieldFilled('GuarantorDetails_nationality')==false)
    {
        showAlert('GuarantorDetails_nationality', alerts_String_Map['VAL090']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_gender')==false)
    {
        showAlert('GuarantorDetails_gender', alerts_String_Map['VAL064']);
        return false;
    }
	else if (getNGValue('idIssueDate') == '')
    {
        showAlert('idIssueDate', alerts_String_Map['VAL068']);
        return false;
    }
	else if (getNGValue('eidExpiry') == '')
    {
        showAlert('eidExpiry', alerts_String_Map['VAL050']);
        return false;
    }
	else if (getNGValue('mobNo') == '')
    {
        showAlert('mobNo', alerts_String_Map['VAL086']);
        return false;
    }
	else if (getNGValue('passExpiry') == '')
    {
        showAlert('passExpiry', alerts_String_Map['VAL096']);
        return false;
    }
	else if (getNGValue('visaNo') == '' && getNGValue('GuarantorDetails_nationality')!='AE')
    {
        showAlert('visaNo', alerts_String_Map['VAL154']);
        return false;
    }
	else if (getNGValue('age_gua') == '')
    {
        showAlert('age_gua', alerts_String_Map['VAL003']);
        return false;
    }
	else if (getNGValue('visaExpiry') == '' && getNGValue('GuarantorDetails_nationality')!='AE')
    {
        showAlert('visaExpiry', alerts_String_Map['VAL153']);
        return false;
    }
	//for proc-10018
	else if (isFieldFilled('GuarantorDetails_MaritalStatus')==false)
    {
        showAlert('GuarantorDetails_MaritalStatus', alerts_String_Map['VAL364']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_empType')==false)
    {
        showAlert('GuarantorDetails_empType', alerts_String_Map['VAL365']);
        return false;
    }
	else if (isFieldFilled('EmploymentStatus')==false)
    {
        showAlert('EmploymentStatus', alerts_String_Map['VAL366']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_MothersName')==false)
    {
        showAlert('GuarantorDetails_MothersName', alerts_String_Map['VAL367']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_VisaISsueDate')==false && getNGValue('GuarantorDetails_nationality')!='AE')
    {
        showAlert('GuarantorDetails_VisaISsueDate', alerts_String_Map['VAL368']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_PassportIssueDate')==false)
    {
        showAlert('GuarantorDetails_PassportIssueDate', alerts_String_Map['VAL369']);
        return false;
    }
	else if (isFieldFilled('Guarantor_Designation')==false)
    {
        showAlert('Guarantor_Designation', alerts_String_Map['VAL370']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_email')==false)
    {
        showAlert('GuarantorDetails_email', alerts_String_Map['PL005']);
        return false;
    }
	return true;
}

function validate_GuarantorGrid()
{
	var cif=getNGValue('cif');
	var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_GuarantorDetails_cmplx_GuarantorGrid");
	for(var i=0;i<n;i++)
		if(getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',i,0)==cif){
			showAlert('cif',alerts_String_Map['VAL004']);
			return false;
		}	
return true;
}	

function checkMandatory_CompanyGrid()
{
	//added by akshay on 18/1/18 to bypass validations for primary row to modify authorised details
	if(getNGListIndex('cmplx_CompanyDetails_cmplx_CompanyGrid')==0)
	{
		return true;
	}
	if(getNGValue('CompanyDetails_CheckBox4')==true || getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC'){
		if(getNGValue('CompanyDetails_CIF')=='')
		{
			showAlert('CompanyDetails_CIF',alerts_String_Map['VAL035']);
			return false;
		}
		//commneted to remove validation Deepak 12 Nov 2017
		/*else if(flag_Company_FetchDetails==false){
			showAlert('CompanyDetails_Button3',alerts_String_Map['VAL059']);
			return false;
		}*/	
	}
	
		if(getNGValue('ApplicationCategory')=="" || getNGValue('ApplicationCategory')=="--Select--")
		{
			showAlert('ApplicationCategory',alerts_String_Map['VAL329']);	
			return false;
		}//Arun (07/12/17) for mandatory alert of this field in company details
		
		else if(getNGValue('TargetSegmentCode')=="" || getNGValue('TargetSegmentCode')=="--Select--")
		{
			showAlert('TargetSegmentCode',alerts_String_Map['VAL330']);	
			return false;
		}//Arun (07/12/17) for mandatory alert of this field in company details
		
		else if((getNGValue('NepType')=="" || getNGValue('NepType')=="--Select--") && (getNGValue('cmplx_Customer_NEP')!='' || getNGValue('cmplx_Customer_NEP')=="--Select--"))
		{
			showAlert('NepType',alerts_String_Map['PL047']);	
			return false;
		}
		
		else if(getNGValue('appType')=="" || getNGValue('appType')=="--Select--")
		{
			showAlert('appType',alerts_String_Map['VAL006']);	
			return false;
		}
			
		else if(getNGValue('compName')==""){
			showAlert('CompanyDetails_compName',alerts_String_Map['VAL039']);	
			return false;
			}
			
		else if(isFieldFilled('compIndus')==false){
			showAlert('CompanyDetails_compIndus',alerts_String_Map['VAL038']);	
			return false;
			}
			
		else if( getNGValue('tlNo')==''){
			showAlert('CompanyDetails_tlNo',alerts_String_Map['VAL147']);	
			return false;
		}
		else if( getNGValue('TradeLicencePlace')==''){
			showAlert('TradeLicencePlace',alerts_String_Map['CC217']);	
			return false;
		}
		else if(getNGValue('TLIssueDate')==''){
			showAlert('TLIssueDate',alerts_String_Map['CC215']);	
			return false;
		}
		
		else if( getNGValue('TLExpiry')==''){
			showAlert('TLExpiry',alerts_String_Map['VAL146']);	
			return false;
		}
			
		else if(getNGValue('indusSector')=="--Select--" || getNGValue('indusSector')==""){
			showAlert('indusSector',alerts_String_Map['VAL072']);	
			return false;
			}
			
		else if(getNGValue('indusMAcro')=="" || getNGValue('indusMAcro')=="--Select--"){
			showAlert('indusMAcro',alerts_String_Map['VAL070']);	
			return false;
			}
			//change by saurabh on 28th Dec
		else if(getNGValue('indusMicro')=='' || getNGValue('indusMicro')=="--Select--"){
			showAlert('indusMicro',alerts_String_Map['VAL071']);	
			return false;
		}
		
		//else if(getNGValue('desig')=='--Select--' || getNGValue('desig')==''){
		//	showAlert('desig',alerts_String_Map['VAL048']);	
		//	return false;
		//}
		
		//else if(getNGValue('desigVisa')==""){
		//	showAlert('desigVisa',alerts_String_Map['VAL047']);	
		//	return false;
		//	}
			
		else if(getNGValue('legalEntity')==""){
			showAlert('legalEntity',alerts_String_Map['VAL077']);	
			return false;
			}
			
		else if( getNGValue('estbDate')==''){
			showAlert('estbDate',alerts_String_Map['VAL046']);	
			return false;
		}
		
		else if( getNGValue('lob')==''){
			showAlert('lob',alerts_String_Map['VAL078']);	
			return false;
		}
		else if( getNGValue('eff_lob_date')==''){
			showAlert('eff_lob_date','Effective LOB Date cannot be blank');	
			return false;
		}
		else if( getNGValue('EffectiveLOB')==''){
			showAlert('EffectiveLOB',alerts_String_Map['PL410']);	
			return false;
		}
		else if(getNGValue('grt40')=='--Select--'){
			showAlert('grt40',alerts_String_Map['VAL065']);	
			return false;
		}
		
	
return true;	
}
	
	function AuthSignatory_checkMandatory()
	{
		if(getNGValue('AuthorisedSignDetails_CheckBox1')==true)
		{
			if(getNGValue('AuthorisedSignDetails_CIFNo')=="")
			{
			showAlert('AuthorisedSignDetails_CIFNo',alerts_String_Map['VAL035']);	
			return false;
			}
			
			else if(flag_Authorised_FetchDetails==false)
			{	
				showAlert('AuthorisedSignDetails_FetchDetails',alerts_String_Map['VAL059']);
				return false;
			}
		}
			
			
			if(getNGValue('AuthorisedSignDetails_Name')=="")
			{
				showAlert('AuthorisedSignDetails_Name',alerts_String_Map['VAL011']);	
				return false;
			}
				
			else if(getNGValue('AuthorisedSignDetails_nationality')=="")
			{
				showAlert('AuthorisedSignDetails_nationality',alerts_String_Map['VAL336']);	
				return false;
			}	
			else if(getNGValue('AuthorisedSignDetails_DOB')==""){
				showAlert('AuthorisedSignDetails_DOB',alerts_String_Map['VAL010']);	
				return false;
				}
				
			else if((getNGValue('AuthorisedSignDetails_VisaNumber')=="") && GCC.indexOf(getNGValue("AuthorisedSignDetails_nationality"))==-1)
			{
				//alert(getNGValue('AuthorisedSignDetails_nationality'));
				showAlert('AuthorisedSignDetails_VisaNumber',alerts_String_Map['VAL015']);	
				return false;
				}
			//added By AKshay startedon 10/10/17----for point 16 of BTC_NTB_Defects xls---The alues are being added to the grid and being saved even without entering the mandatory fields	
			else if((getNGValue('AuthorisedSignDetails_PassportNo')=="")){
				showAlert('AuthorisedSignDetails_PassportNo',alerts_String_Map['VAL012']);	
				return false;
				}
			//ended By AKshay ended on 10/10/17----for point 16 of BTC_NTB_Defects xls---The alues are being added to the grid and being saved even without entering the mandatory fields	
		//below code commented by nikhil proc-3742
			/*else if( getNGValue('AuthorisedSignDetails_shareholding')==''){
				showAlert('AuthorisedSignDetails_shareholding',alerts_String_Map['VAL013']);	
				return false;
			}*/
			//added By AKshay started on 10/10/17----for point 16 of BTC_NTB_Defects xls---The alues are being added to the grid and being saved even without entering the mandatory fields	
			/* Commented by Shivang for PCASP-2060
			else if( isFieldFilled('AuthorisedSignDetails_SoleEmployee')==false){
				showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL014']);	
				return false;
			}
			//added By AKshay ended on 10/10/17----for point 16 of BTC_NTB_Defects xls---The alues are being added to the grid and being saved even without entering the mandatory fields
			else if(isFieldFilled('Designation')==false){
				showAlert('Designation',alerts_String_Map['VAL048']);	
				return false;
			}

			else if(isFieldFilled('DesignationAsPerVisa')==false){
				showAlert('DesignationAsPerVisa',alerts_String_Map['VAL047']);	
				return false;
			}*/
			//below code added by nikhil 15/1
			else if(isFieldFilled('AuthorisedSignDetails_nationality')==false){
				showAlert('AuthorisedSignDetails_nationality',alerts_String_Map['VAL090']);	
				return false;
			}

	return true;
	}
	
	function checkMandatory_PartnerGrid()
	{
			if(getNGValue('PartnerDetails_PartnerName')=="")
			{
				showAlert('PartnerDetails_PartnerName',alerts_String_Map['VAL094']);	
				return false;
			}
				
			else if(getNGValue('PartnerDetails_Dob')==""){
				showAlert('PartnerDetails_Dob',' Date Of Birth cannot be blank');	
				return false;
				}
				
			else if(getNGValue('PartnerDetails_passno')==""){
				showAlert('PartnerDetails_passno',alerts_String_Map['VAL098']);	
				return false;
				}
				
			else if( getNGValue('PartnerDetails_authsigname')==''){
				showAlert('PartnerDetails_authsigname',alerts_String_Map['VAL011']);	
				return false;
			}
			
			else if( getNGValue('PartnerDetails_nationality')==''){
				showAlert('PartnerDetails_nationality',alerts_String_Map['VAL090']);
				return false;
			}	
			else if(getNGValue('PartnerDetails_shareholding')==''){
				showAlert('PartnerDetails_shareholding',alerts_String_Map['VAL136']);
				return false;
			}
	 return true;		
	}
	
	function checkMandatory_LiabilityGrid()
	{
		var contractType=getNGValue('contractType');
		var EMI=getNGValue('Emi');
		var remarks=getNGValue('remarks');
		//change by saurabh on 4th Dec
		var CAC_ind = getNGValue('ExtLiability_CACIndicator');//name change by Tarang on 28/02/2018
		var CAC_flag = false;
		var rowCount = getLVWRowCount('cmplx_Liability_New_cmplx_LiabilityAdditionGrid');
		if(rowCount>0){
			for(var i=0;i<rowCount;i++){
				if(getLVWAT('cmplx_Liability_New_cmplx_LiabilityAdditionGrid',i,5)=='true'){
					CAC_flag = true;
				}
			}
		}
		if((contractType=="--Select--")||contractType=="")
		{
			showAlert('contractType',alerts_String_Map['VAL149']);	
			return false;
		}
		//Changes Done by aman for PCSP-67
		else if (getNGValue('Contract_App_Type')=="L" && getNGValue('Limit')==''){
			showAlert('Limit',alerts_String_Map['VAL382']);	
			return false;
		
		}
			
		else if(EMI=="0" && getNGValue('Limit')!='' && getNGValue('Contract_App_Type')=="L"){
			showAlert('EMI',alerts_String_Map['VAL080']);	
			return false;
			}
		//Changes Done by aman for PCSP-67	
	/*	else if(remarks==""){
			showAlert('remarks',alerts_String_Map['VAL129']);	
			return false;
			}
		*/// Commented for proc 8366	
		else if(getNGValue('takeOverIndicator')==true && getNGValue('takeoverAMount')==''){
			showAlert('takeoverAMount',alerts_String_Map['VAL140']);	
			return false;
			}
		if(CAC_flag && CAC_ind==true){
			setNGValueCustom('ExtLiability_CACIndicator',false);//name change by Tarang on 28/02/2018
			return false;
			}
		return true;
	}	
		
function AccountSummary_checkMandatory()
	{
		if(getNGValue('cmplx_Customer_CIFNO')=='' || getNGValue('cmplx_Customer_CIFNO')==null){
			if(!getNGValue('cmplx_Customer_NTB')==true){
				showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL033']);	
				return false;
			}
		}

			if((getNGValue('cmplx_Customer_card_id_available') == true ||  getNGValue('existingOldCustomer')==true) && com.newgen.omniforms.formviewer.isEnabled('Customer_Button1') == true){
			showAlert('cmplx_Customer_card_id_available',alerts_String_Map['VAL117']);	
				return false;
			}
		//added by akshay on 19/3/18 
		else if(getNGValue('cmplx_Customer_IscustomerSave')!='Y'){
				showAlert('Customer_save',alerts_String_Map['VAL044']);	
				return false;
			}
		else if(getNGValue('cmplx_Customer_NEP')=='NEWC'){
				showAlert('',alerts_String_Map['VAL356']);	
				return false;
			}
			
	return true;	
	}	
	
function checkMandatory_Address(opType)
{
		 var AddType=getNGValue("addtype");
		 var POBox=getNGValue("pobox");
		 var houseNo=getNGValue("house");
		 var BuildName=getNGValue("buildname");
		 var Streetname=getNGValue("street");
		 var landmark=getNGValue("landmark");
		 var city=getNGValue("city");
		 var state=getNGValue("state");
		 var country=getNGValue("country");
		 var YearsATcurrent=getNGValue("years");
		 var prefAdd=getNGValue("PreferredAddress");
		 var CustomerType=getNGValue("Customer_Type");//added by prabhakar drop-4 point-3(Applicanttype)
		
		if(CustomerType==""||CustomerType=="--Select--")
			{
			showAlert('Customer_Type','Applicant type can not be blank');	
			return false;
			}
		else if((AddType=='--Select--')||(AddType=='')){
			showAlert('addtype',alerts_String_Map['VAL002']);
			return false;
		}	
		
		else if(AddType.toUpperCase()=='OFFICE')
		{
			if(BuildName == ""){
			showAlert('buildname',alerts_String_Map['VAL019']);	
				return false;
			}
			 if(country=="" || country=="--Select--"){
				showAlert('country',alerts_String_Map['VAL040']);	
				return false;
				}
			else if(houseNo ==""){
			showAlert('house',alerts_String_Map['VAL063']);	
				return false;
			}		
				
			else if(state=="--Select--" || state==""){
				showAlert('state',alerts_String_Map['VAL137']);	
				return false;
				}
				//changed done for CR PCSP-651
			/*else if(landmark=="" ){
				showAlert('landmark',alerts_String_Map['VAL075']);	
				return false;
				}*/	
					
			else if(POBox==""){
				showAlert('pobox',alerts_String_Map['VAL122']);	
				return false;
				}
			//uncommented by nikhil for CR-CIF Creation Failing
			/*else if(YearsATcurrent==""){
				showAlert('years','Please enter Years in current Address');	
				return false;
				}*/ //commented by Pooja for 3528
		 	else if(!prefcheck(opType)){
			showAlert('PreferredAddress',alerts_String_Map['VAL141']);
				return false;
			}	
			
		}

		else if(AddType.toUpperCase()=='RESIDENCE' || AddType.toUpperCase()=='MAILING')
		{
			
			 if(BuildName == ""){
			showAlert('buildname',alerts_String_Map['VAL019']);	
				return false;
			}
			else if(houseNo ==""){
			showAlert('house',alerts_String_Map['VAL063']);	
				return false;
			}		
			else if(Streetname==""){
				showAlert('street',alerts_String_Map['VAL138']);	
				return false;
				}	
			//changed done for CR PCSP-651	
			/*else if(landmark=="" ){
				showAlert('landmark',alerts_String_Map['VAL075']);	
				return false;
				}	*/
				
			else if(city=="" || city=="--Select--"){
				showAlert('city',alerts_String_Map['VAL008']);	
				return false;
				}	
				
			else if( state=="--Select--" || state==""){
				showAlert('state',alerts_String_Map['VAL137']);	
				return false;
				}
				
			else if(country=="" || country=="--Select--"){
				showAlert('country',alerts_String_Map['VAL040']);	
				return false;
				}
			//uncommented by nikhil for CR-CIF Creation Failing
		/*else if(YearsATcurrent==""){
				showAlert('years','Please enter Years in current Address');	
				return false;
				}*/ //commented by Pooja for 3528		
				
			else if(prefAdd==true)
		{
			if(POBox==""){
			showAlert('pobox',alerts_String_Map['VAL122']);	
			return false;
			}
			if(!prefcheck(opType)){
			showAlert('PreferredAddress',alerts_String_Map['VAL141']);
				return false;
			}
		}
		
		}
			
		else if(AddType.toUpperCase()=='HOME')
		{
			if(BuildName == ""){
			showAlert('buildname',alerts_String_Map['VAL019']);	
				return false;
			}
				
			else if(houseNo ==""){
			showAlert('house',alerts_String_Map['VAL063']);	
				return false;
			}	
			//changed done for CR PCSP-651
			/*else if(landmark=="" ){
				showAlert('landmark',alerts_String_Map['VAL075']);	
				return false;
				}*/
				
			else if(Streetname==""){
				showAlert('street',alerts_String_Map['VAL138']);	
				return false;
				}	
				
			else if(city=="" || city=="--Select--"){
				showAlert('city',alerts_String_Map['VAL008']);	
				return false;
				}	
				
			else if(state=="--Select--" || state==""){
				showAlert('state',alerts_String_Map['VAL137']);	
				return false;
				}
				
			else if(country=="" || country=="--Select--"){
				showAlert('country',alerts_String_Map['VAL040']);	
				return false;
				}
				//uncommented by nikhil for CR-CIF Creation Failing
				/*else if(YearsATcurrent==""){
				showAlert('years','Please enter Years in current Address');	
				return false;
				}*/	
		}
	return true;
	}	
	//added by prabhakar
	function KYC_add_Check(){
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			var custTypePickList = document.getElementById("KYC_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			CustType=getNGValue("KYC_CustomerType");
			var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0));
			}
	 if(getNGValue('KYC_CustomerType')==""||getNGValue('KYC_CustomerType')=="--Select--")
		{
			showAlert('KYC_CustomerType','Applicant Type can not be blank');
			return false;
		}
	else if(getNGValue('KYC_Combo1')==""||getNGValue('KYC_Combo1')=="--Select--"){
		console.log(getNGValue('cmplx_KYC_KYC_Held')+"INside KYC HELD");
		showAlert('cmplx_KYC_KYC_Held',alerts_String_Map['CC110']);
		return false;
		}
	else if(getNGValue('KYC_Combo2')==""||getNGValue('KYC_Combo2')=="--Select--"){
		showAlert('cmplx_KYC_PEP',alerts_String_Map['CC111']);
		return false;
		}
	else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan')
	{
		if(getNGValue('KYC_Combo1')=='Y' && getNGValue('KYC_DatePicker1')=='')
		{
			showAlert('','KYC Review Date cannot be blank for any row where KYC held is Yes');
			return false;
		}
	}
			else
			{
				for(var i=0;i<gridValue.length;i++)
				{
					if(gridValue.indexOf(CustType)>-1)
					{
						showAlert('KYC_CustomerType','KYC Already Added for Customer'+CustType);
						return false;
						break;
					}
				}
			}
			return true;
}
	function checkMandatory_SupplementGrid()
	{
		if(getNGValue('SupplementCardDetails_Text1')=='' && getNGValue('ResdCountry')=='AE'){
			showAlert('SupplementCardDetails_Text1',alerts_String_Map['CC082']);
				return false;
		}
		else if(getNGValue('FirstName')==''){
			showAlert('FirstName',alerts_String_Map['VAL061']);
				return false;
		}
				
		else if(getNGValue('lastname')==''){
					showAlert('lastname',alerts_String_Map['VAL076']);
					return false;
		}

		else if(!validateCustomerName(getNGValue('FirstName'),getNGValue('MiddleName'),getNGValue('lastname'))){
		showAlert('FirstName','Customer Full Name cannot exceed 80 characters');
		return false;
	}	
					
		else if(getNGValue('passportNo')==''){
					showAlert('passportNo',alerts_String_Map['VAL097']);
					return false;
		}			
					
		else if(getNGValue('DOB')==''){
					showAlert('DOB',alerts_String_Map['VAL045']);
					return false;
		}			
					
		else if(getNGValue('nationality')==''){
					showAlert('nationality',alerts_String_Map['VAL090']);
					return false;
		}			
					
		else if(getNGValue('MobNo')==''){
					showAlert('MobNo',alerts_String_Map['VAL084']);			
					return false;
		}			
		
		else if(isLocked('SupplementCardDetails_FetchDetails')==false){
			showAlert('SupplementCardDetails_FetchDetails',alerts_String_Map['PL278']);			
			return false;
		}			
		else if(getNGValue('ResdCountry')==''){
			showAlert('ResdCountry',alerts_String_Map['VAL133']);
			return false;
		}	
		
		else if(getNGValue('gender')==''){
					showAlert('gender',alerts_String_Map['VAL064']);
					return false;
			}		
					
		else if(getNGValue('relationship')==''){
			showAlert('relationship',alerts_String_Map['VAL128']);
			return false;
		}

		else if(getNGValue('MotherNAme')==''){
			showAlert('MotherNAme',alerts_String_Map['VAL247']);
				return false;
		}	
		else if(getNGValue('SupplementCardDetails_CardProduct')=='' || getNGValue('SupplementCardDetails_CardProduct')=='--Select--'){
			showAlert('SupplementCardDetails_CardProduct',alerts_String_Map['VAL998']);
				return false;
		}
		else if(getNGValue('SupplementCardDetails_ApprovedLimit')==''){
			showAlert('SupplementCardDetails_ApprovedLimit',alerts_String_Map['VAL997']);
				return false;
		}		
				/*var grid_pdt=com.newgen.omniforms.formviewer.getLVWATgetLVWAT("cmplx_Product_cmplx_ProductGrid",i,5);
		if(grid_pdt == "KALYAN-EXPAT")*/
		//change by saurabh on 3rd jan		
		else if(getNGValue('cardEmbName')=='' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)!='Personal Loan'){		
		if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,5)=="KALYAN-EXPAT")
		{			
					showAlert('cardEmbName',alerts_String_Map['VAL029']);
					return false;
					}//Arun (09/10)
			}		
					
	/*	else if(getNGValue('CompEmbName')=='' && isVisible('CompEmbName')){
				showAlert('CompEmbName',alerts_String_Map['VAL036']);
				return false;
			}PCAS-2633	*/
		if(getNGValue('ResdCountry')=='AE')
		{
			var Id = getNGValue('SupplementCardDetails_Text1');
			var Dob=getNGValue('DOB');
			if((Id!="")&&((Id.length != 15) ||(Id.substr(0,3)!='784'))){
				if(Id.substr(0,3)!='784'){
					showAlert('SupplementCardDetails_Text1',alerts_String_Map['VAL108']);
				}
				else{
					showAlert('SupplementCardDetails_Text1',alerts_String_Map['VAL108']);
				}
				return false;
			}
			
			else if(Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9))
			{
				showAlert('SupplementCardDetails_Text1',alerts_String_Map['VAL074']);
				return false;
			} 
		}
		if(getNGValue('SupplementCardDetails_VisaIssueDate')=="" && getNGValue('ResdCountry')=='AE' && getNGValue('nationality')!='AE')//PCAS-3337 sagarika
		{
			showAlert('SupplementCardDetails_VisaIssueDate','Visa Issue date cannot be blank');
				return false;
		}	
			if(getNGValue('SupplementCardDetails_IDIssueDate')=="" && getNGValue('ResdCountry')=='AE' && getNGValue('nationality')!='AE')//PCAS-3337 sagarika
		{
			showAlert('SupplementCardDetails_IDIssueDate','ID issue date cannot be blank');
				return false;
		}	
		
	return true;				
	}	
	
	function validateSuppEntry(opType){
		var suppRows = getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid');
		if(suppRows>0){
			var gridValues = [];
			var newValue = getNGValue('passportNo')+':'+getNGValue('SupplementCardDetails_CardProduct');
			for(var i=0;i<suppRows;i++){
				if(getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid')>-1 && i==getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid') && opType=='modify'){
					continue;
				}
				gridValues.push(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,3)+':'+getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,22));
			}
			if(gridValues.indexOf(newValue)>-1){
				showAlert('','Only 1 supplementary card for a particular primary card is allowed for a customer');
				return false;
			}
		}
		return true;
	}
	function checkMandatory_OecdGrid()
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			//var custTypePickList = document.getElementById("OECD_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			var applicantType=getNGValue("OECD_CustomerType")//Added by prabhakar drop-4 point 10
			var gridValue=[];//Added by prabhakar drop-4 point 10
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_OECD_cmplx_GR_OecdDetails",i,8));
			}
		if(getNGValue('OECD_CustomerType')=='--Select--' || getNGValue('OECD_CustomerType')=='')
			{
							showAlert('OECD_CustomerType','Applicant Type can not be blank');
							return false;
			}
		else if(getNGValue('OECD_CRSFlag')=='--Select--' || getNGValue('OECD_CRSFlag')==''){
					showAlert('OECD_CRSFlag',alerts_String_Map['VAL043']);
					return false;
		}
		else if(getNGValue('OECD_CRSFlag')=='Yes' || getNGValue('OECD_CRSFlag')=='Y'){
				if(getNGValue('OECD_CRSFlagReason')=='' || getNGValue('OECD_CRSFlagReason')=='--Select--')
				{
					showAlert('OECD_CRSFlagReason',alerts_String_Map['VAL120']);
					return false;
				}
				
				else if(getNGValue('OECD_CountryBirth')=='--Select--' || getNGValue('OECD_CountryBirth')==''){
							showAlert('OECD_CountryBirth',alerts_String_Map['VAL041']);
							return false;
				}
				else if(getNGValue('OECD_townBirth')=='--Select--' || getNGValue('OECD_townBirth')==''){
							showAlert('OECD_townBirth',alerts_String_Map['VAL145']);
							return false;
				}
				else if(getNGValue('OECD_noTinReason')=='' && getNGValue('OECD_tinNo')==''){
					showAlert('OECD_noTinReason',alerts_String_Map['VAL092']);
					return false;
				}
		}
		else if(getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')==''){
			showAlert('OECD_CountryTaxResidence',alerts_String_Map['VAL042']);
			return false;
		}
		else if(getNGValue('OECD_noTinReason')=='' && getNGValue('OECD_tinNo')==''){
			showAlert('OECD_noTinReason',alerts_String_Map['VAL092']);
			return false;
		}
		/*Changed by Shivanshi for PCASI-3726(For Personal Loan)*/
		if( getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan'){
			//Added by Alok
			if(gridValue.length>=3) 
			{
				showAlert('OECD_CustomerType','OECD Already Added for Customer'+applicantType);
				return false;
			}
		}
		else {
			for(var i=0;i<gridValue.length;i++)
			{
				if(gridValue.indexOf(applicantType)>-1)
				{
					showAlert("OECD_CustomerType","OECD already added for "+applicantType);
					return false;
					break;
				}
			}
		}
		
		/*for(var i=0;i<gridValue.length;i++)
		{
			if(gridValue.indexOf(applicantType)>-1)
			{
				showAlert("OECD_CustomerType","OECD already added for "+applicantType);
				return false;
				break;
			}
		}*///commented by Alok
		return true;
	}
	
	function checkMandatory_OecdGridModify()
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			//var custTypePickList = document.getElementById("OECD_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			var applicantType=getNGValue("OECD_CustomerType")//Added by prabhakar drop-4 point 10
			var gridValue=[];//Added by prabhakar drop-4 point 10
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_OECD_cmplx_GR_OecdDetails",i,8));
			}
		if(getNGValue('OECD_CustomerType')=='--Select--' || getNGValue('OECD_CustomerType')=='')
			{
							showAlert('OECD_CustomerType','Applicant Type can not be blank');
							return false;
			}
		else if(getNGValue('OECD_CRSFlag')=='--Select--' || getNGValue('OECD_CRSFlag')==''){
					showAlert('OECD_CRSFlag',alerts_String_Map['VAL043']);
					return false;
		}
		else if(getNGValue('OECD_CRSFlag')=='Yes' || getNGValue('OECD_CRSFlag')=='Y'){
				if(getNGValue('OECD_CRSFlagReason')=='' || getNGValue('OECD_CRSFlagReason')=='--Select--'){
					showAlert('OECD_CRSFlag',alerts_String_Map['VAL120']);
					return false;
		}
		
		else if(getNGValue('OECD_CountryBirth')=='--Select--' || getNGValue('OECD_CountryBirth')==''){
					showAlert('OECD_CountryBirth',alerts_String_Map['VAL041']);
					return false;
		}
		else if(getNGValue('OECD_townBirth')=='--Select--' || getNGValue('OECD_townBirth')==''){
					showAlert('OECD_townBirth',alerts_String_Map['VAL145']);
					return false;
				}
		}
		
		if(getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')==''){
				showAlert('OECD_CountryTaxResidence',alerts_String_Map['VAL042']);
				return false;
		}
		/*else if(getNGValue('OECD_tinNo')==''){
					showAlert('OECD_tinNo',alerts_String_Map['VAL143']);
					return false;
		}*/
		else if(getNGValue('OECD_noTinReason')=='' && getNGValue('OECD_tinNo')==''){
					showAlert('OECD_noTinReason',alerts_String_Map['VAL092']);
					return false;
		}
		else{
			return true;
		}
		return true;
	}
	
	/*
	function checkMandatory_CreateCIFAcc()
	{   
		if(getNGValue('cmplx_Customer_FIrstNAme')=='' || getNGValue('cmplx_Customer_FIrstNAme')==null){
			showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL061']);	
			return false;
		}
		else if(getNGValue('cmplx_Customer_Title')=='' || getNGValue('cmplx_Customer_Title')==null){
			showAlert('cmplx_Customer_Title',alerts_String_Map['VAL031']);	
			return false;
		}
		else if(getNGValue('cmplx_Customer_LAstNAme')=="" || getNGValue('cmplx_Customer_LAstNAme')==null){
			showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL076']);	
			return false;
		}
		
		else if(getNGValue('cmplx_Customer_gender')=="--Select--" || getNGValue('cmplx_Customer_gender')==null || getNGValue('cmplx_Customer_gender')==''){
			showAlert('cmplx_Customer_gender',alerts_String_Map['VAL064']);	
			return false;
		}
		else if(getNGValue('cmplx_Customer_ResidentNonResident')=="--Select--" || getNGValue('cmplx_Customer_ResidentNonResident')==null  || getNGValue('cmplx_Customer_ResidentNonResident')==''){
			showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL134']);	
			return false;
		}

	/*	else if(getNGValue('cmplx_Customer_MotherName')=="" || getNGValue('cmplx_Customer_MotherName')==null){
			showAlert('cmplx_Customer_MotherName',alerts_String_Map['VAL089']);	
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
		else if(getNGValue('cmplx_Customer_EmirateIDExpiry')=="" || getNGValue('cmplx_Customer_EmirateIDExpiry')==null){
			if(!(getNGValue('NEP')=='true' || getNGValue('cmplx_Customer_NTB')==true)){
				showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['VAL052']);	
				return false;
			}	
		}
		else if(getNGValue('cmplx_Customer_VIsaExpiry')=="" || getNGValue('cmplx_Customer_VIsaExpiry')==null){
			showAlert('cmplx_Customer_VIsaExpiry',alerts_String_Map['VAL153']);	
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
		else if(getNGValue('cmplx_EmploymentDetails_IndusSeg')=="--Select--" || getNGValue('cmplx_EmploymentDetails_IndusSeg')==null){
			showAlert('cmplx_EmploymentDetails_IndusSeg',alerts_String_Map['VAL073']);	
			return false;
		}
		
		else if(getNGValue('AlternateContactDetails_MobileNo1')=="" || getNGValue('AlternateContactDetails_MobileNo1')==null){
			showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL084']);	
			return false;
		}
		else if(getNGValue('AlternateContactDetails_Email1')=="" || getNGValue('AlternateContactDetails_Email1')==null){
			showAlert('AlternateContactDetails_Email1',alerts_String_Map['VAL056']);	
			return false;
		}
		else if(getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid')==0){
			showAlert('cmplx_AddressDetails_cmplx_AddressGrid',alerts_String_Map['VAL001']);	
			return false;
		}
		
	/*	else if(getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid')==0){
			showAlert('cmplx_CompanyDetails_cmplx_CompanyGrid',alerts_String_Map['VAL037']);	
			return false;
		}
		
		}
	return true;
}
*/

//Function added for drop4 2/4/18	
	function checkMandatory_CreateCIF(ApplicantType,pId){
switch(ApplicantType){
case 'PRIMARY': 

		var grid_count=getLVWRowCount('cmplx_DecisionHistory_MultipleApplicantsGrid');
		var gc='';
		for(var i=0;i<grid_count;i++){
			if (getLVWAT('cmplx_DecisionHistory_MultipleApplicantsGrid',i,0).indexOf('Guarantor')>-1 && (getLVWAT('cmplx_DecisionHistory_MultipleApplicantsGrid',i,3)=='' || getLVWAT('cmplx_DecisionHistory_MultipleApplicantsGrid',i,3)=='7777777')){
				gc='y';
				break;
			}
		}
		if (gc=='y'){
			showAlert('cmplx_DecisionHistory_MultipleApplicantsGrid',alerts_String_Map['PL432']);	
			return false;
	
		}
			
				if(getNGValue('cmplx_Customer_FIrstNAme')=='' || getNGValue('cmplx_Customer_FIrstNAme')==null){
			showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL061']);	
			return false;
		}
		
		else if(getNGValue('cmplx_Customer_LAstNAme')=="" || getNGValue('cmplx_Customer_LAstNAme')==null){
			showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL076']);	
			return false;
		}
				
		
		else if(!validateCustomerName(getNGValue('cmplx_Customer_FIrstNAme'),getNGValue('cmplx_Customer_MiddleName'),getNGValue('cmplx_Customer_LAstNAme'))){
		showAlert('cmplx_Customer_FIrstNAme','Customer Full Name cannot exceed 80 characters');
		return false;
	}
		
		else if(getNGValue('cmplx_Customer_Title')=='' || getNGValue('cmplx_Customer_Title')==null){
			showAlert('cmplx_Customer_Title',alerts_String_Map['VAL031']);	
			return false;
		}
		
		else if(getNGValue('cmplx_Customer_gender')=="--Select--" || getNGValue('cmplx_Customer_gender')==null || getNGValue('cmplx_Customer_gender')==''){
			showAlert('cmplx_Customer_gender',alerts_String_Map['VAL064']);	
			return false;
		}
		else if(getNGValue('cmplx_Customer_ResidentNonResident')=="--Select--" || getNGValue('cmplx_Customer_ResidentNonResident')==null  || getNGValue('cmplx_Customer_ResidentNonResident')==''){
			showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL134']);	
			return false;
		}
	/*	else if(getNGValue('cmplx_Customer_MotherName')=="" || getNGValue('cmplx_Customer_MotherName')==null){
			showAlert('cmplx_Customer_MotherName',alerts_String_Map['VAL089']);	
			return false;
		}*/
		else if(getNGValue('cmplx_Customer_MAritalStatus')=="--Select--" || getNGValue('cmplx_Customer_MAritalStatus')==null|| getNGValue('cmplx_Customer_MAritalStatus')==''){
			showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['VAL081']);	
			return false;
		}
		else if(getNGValue('cmplx_Customer_DOb')=="" || getNGValue('cmplx_Customer_DOb')==null){
			showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL045']);	
			return false;
		}
		else if(isFieldFilled('cmplx_Customer_EmirateIDExpiry')==false && isFieldFilled('cmplx_Customer_EmiratesID') ==true){
				showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['VAL052']);	
				return false;		
		}
		else if(validatePastDate('cmplx_Customer_EmirateIDExpiry',"Emirate Card has expired!!")==false){
					return false;
			}
		else if((getNGValue('cmplx_Customer_VisaNo')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
	                showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);
	                return false;      
	     }
	    
	   else if((getNGValue('cmplx_Customer_VisaIssuedate')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N' && isFieldFilled('cmplx_Customer_VisaNo')==true){
				 showAlert('cmplx_Customer_VisaIssuedate',alerts_String_Map['VAL355']);
				 return false; 
	   }
		
		else if((getNGValue('cmplx_Customer_VIsaExpiry')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N' && isFieldFilled('cmplx_Customer_VisaNo')==true){
            showAlert('cmplx_Customer_VIsaExpiry',alerts_String_Map['VAL292']);return false;      }
		else if(validatePastDate('cmplx_Customer_VIsaExpiry',"Visa has expired!!")==false){
			return false;
		}			
		else if(isFieldFilled('cmplx_Customer_PassPortExpiry')==false && isFieldFilled('cmplx_Customer_PAssportNo')==true){
			showAlert('cmplx_Customer_PassPortExpiry',alerts_String_Map['VAL095']);	
			return false;
		}
			else if(isFieldFilled('cmplx_Customer_Nationality')==false){
			showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL090']);	
			return false;
		}
		//Below code added by Arun (29/11/17) for this alert should not come for CC-SE
		else if(!(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'))
		{	
			if(isFieldFilled('cmplx_EmploymentDetails_EmpStatus')==false){
				showAlert('cmplx_EmploymentDetails_EmpStatus',alerts_String_Map['VAL056']);	
				return false;
			}
			 if(isFieldFilled('cmplx_EmploymentDetails_Emp_Type')==false){
				showAlert('cmplx_EmploymentDetails_Emp_Type',alerts_String_Map['VAL057']);	
				return false;
			}
			if(isFieldFilled('cmplx_EmploymentDetails_Designation')==false){
				showAlert('cmplx_EmploymentDetails_Designation',alerts_String_Map['VAL048']);	
				return false;
			}
		}		
		//Above code added by Arun (29/11/17) for this alert should not come for CC-SE
		/*else if(getNGValue('cmplx_EmploymentDetails_IndusSeg')=="--Select--" || getNGValue('cmplx_EmploymentDetails_IndusSeg')==null){
			showAlert('cmplx_EmploymentDetails_IndusSeg',alerts_String_Map['VAL073']);	
			return false;
		}*/
		//changed by saurabh on 1st Dec for Tanshu points.
		var n =getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		 if (n==0){
				showAlert('cmplx_AddressDetails_cmplx_AddressGrid',alerts_String_Map['VAL009']);
				return false;
		}
		else if(!checkForNA_AddressGrid()){
			return false;
			}
			//if(NEW_ACCOUNT_REQ_checkMandatory())
		else if(isFieldFilled('AlternateContactDetails_MobileNo1')==false){
					showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['VAL085']);
					return false;
		}
		else if(isFieldFilled('AlternateContactDetails_Email1')==false){
					showAlert('AlternateContactDetails_Email1',alerts_String_Map['VAL051']);
					return false;
		}
		/*else if(!isVisible('AlternateContactDetails_custdomicile')){
			showAlert('AlternateContactDetails_carddispatch',alerts_String_Map['VAL028']);
					return false;
		}*/ //Arun (19/11/17) as discussed with deepak sir the alert should not come as the masters are changed for Card dispatch to
		else if(isVisible('AlternateContactDetails_custdomicile') && getNGValue('AlternateContactDetails_custdomicile')==''){
			showAlert('AlternateContactDetails_custdomicile',alerts_String_Map['VAL119']);
					return false;
		}
		//Above code added by Arun (29/11/17) for this alert should not come for CC-SE
		else{
			return true;
		}
	break;
case 'GUARANTOR': 
					var grid_name='cmplx_GuarantorDetails_cmplx_GuarantorGrid';
					if(getLVWRowCount(grid_name)>0){
						for(var i=0;i<getLVWRowCount(grid_name);i++){
							if(getLVWAT(grid_name,i,5)==getLVWAT(pId,getNGListIndex(pId),2)){
								if(getLVWAT(grid_name,i,7)==''){
									showAlert('Fname',alerts_String_Map['PL010']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,9)==''){
									showAlert('lname',alerts_String_Map['PL011']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,6)==''){
									showAlert('title',alerts_String_Map['PL219']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,12)==''){
									showAlert('GuarantorDetails_gender',alerts_String_Map['PL019']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,3)==''){
									showAlert('dob',alerts_String_Map['PL012']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,14)==''){
									showAlert('eidExpiry',alerts_String_Map['PL009']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,18)==''  ){
									showAlert('visaExpiry',alerts_String_Map['PL022']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,16)==''){
									showAlert('passExpiry',alerts_String_Map['PL023']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,4)==''){
									showAlert('GuarantorDetails_nationality',alerts_String_Map['PL013']);	
									return false;
								}
								else if(getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid')>0){
									var flag_GuarantorFound=false;
									for(var j=0;j<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');j++){
										if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',j,13)=='G-'+getLVWAT(grid_name,i,7)+' '+getLVWAT(grid_name,i,9)){
												flag_GuarantorFound=true;
										}	
									}
									if(flag_GuarantorFound==false){
										showAlert('ReadFromCIF',alerts_String_Map['PL393']);	
										return false;	
									}
									
									else if(!checkForNA_AddressGrid()){
												return false;
									}									
								}		
								else if(getLVWAT(grid_name,i,10)==''){
									showAlert('mobNo',alerts_String_Map['PL090']);	
									return false;
								}
							}
						}
					}
	break;
}
return true;
	}
	//Function added for drop4 2/4/18
	/*
	function NEW_ACCOUNT_REQ_checkMandatory()
	{
		if(getNGValue('cmplx_Customer_CIFNO')=='' || getNGValue('cmplx_Customer_CIFNO')==null){
			showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL033']);	
			return false;
		}
		
		else if(getNGValue('cmplx_DecisionHistory_IBAN')==""){
			showAlert('cmplx_DecisionHistory_IBAN',alerts_String_Map['VAL067']);	
			return false;
		}
	return true;
	}
	*/
function Product_add_validate()
{
		var ReqProd=getNGValue("ReqProd");
		var EmpType=getNGValue("EmpType");
		var priority=getNGValue("Priority");
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		
		if(n==0)
		{
			if(priority!='Primary'){
				showAlert('Priority',alerts_String_Map['VAL062']);
				return false;
			}	
			else
				return true;
		}		
       
		else if(n>0)
			{
				
				for(var i=0;i<n;i++)
				{
					var grid_EmpType=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,6);
					var grid_Priority=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9);
					
					if(grid_EmpType!=EmpType && grid_EmpType!="--Select--" && EmpType!="--Select--"){
						showAlert('EmpType',alerts_String_Map['VAL024']);
						
						return false;
						}
					var grid_Prod=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,1);
					// added by saurabh on 11th July 17 for code sent by disha.
					if(grid_Prod == "Personal Loan" && ReqProd == "Credit Card"){
						showAlert('ReqProd',alerts_String_Map['VAL022']);
						return false;
					}
					if(grid_Prod=="Personal Loan" && grid_Prod==ReqProd){
						showAlert('ReqProd',alerts_String_Map['VAL025']);
						return false;
					}
					if(grid_Prod=="Credit Card" && grid_Prod!=ReqProd && n!=0){
						showAlert('ReqProd',alerts_String_Map['VAL099']);
						return false;
					}
					
					
					if(grid_Priority=="Primary" && grid_Priority==priority){
								showAlert('Priority',alerts_String_Map['VAL026']);
								return false;
					}			
				}
			}
			
		if(ReqProd=='Personal Loan')
			setNGValueCustom('LimitAcc','');
		
		else if(ReqProd=='Credit Card')	{
			//setNGValueCustom('Scheme','NA');
			setNGValueCustom('ReqTenor','');
		}

	return true;
}
	
	function Product_modify_validate()
{
		var ReqProd=getNGValue("ReqProd");
		var EmpType=getNGValue("EmpType");
		var priority=getNGValue("Priority");
		var n = getLVWRowCount("cmplx_Product_cmplx_ProductGrid");

		
		if(n==1)
		{
			if(priority!='Primary'){
				showAlert('Priority',alerts_String_Map['VAL062']);
				return false;
			}	
			else 
				return true;
		}	
		
		else if(n>1)
			{
				for(var i=0;i<n;i++)
				{
					var grid_EmpType=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,6);
					var grid_Priority=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9);
					if(grid_EmpType!=EmpType && grid_EmpType!="--Select--" && EmpType!="--Select--"){
						showAlert('EmpType',alerts_String_Map['VAL024']);
						return false;
						}
					var grid_Prod=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,1);
					// added by saurabh on 11th July 17 for code sent by disha. 
					if(grid_Prod == "Personal Loan" && ReqProd == "Credit Card"){
						showAlert('ReqProd',alerts_String_Map['VAL022']);
						return false;
					}
					if(grid_Prod=="Personal Loan" && grid_Prod==ReqProd && com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),1)!=ReqProd){
						showAlert('ReqProd',alerts_String_Map['VAL025']);
						return false;
					}
						var s=com.newgen.omniforms.formviewer.getNGLVWSelectedRows("cmplx_Product_cmplx_ProductGrid");
						
					if(grid_Prod=="Credit Card" && grid_Prod!=ReqProd && s!=0){
						showAlert('ReqProd',alerts_String_Map['VAL099']);
						return false;
					}
					if(grid_Priority=="Primary" && grid_Priority==priority && i != 0){
								showAlert('Priority',alerts_String_Map['VAL026']);
								return false;
					}			
				}
			}
			
		if(ReqProd=='Personal Loan')
			setNGValueCustom('LimitAcc','NA');
		
		else if(ReqProd=='Credit Card')	{
			setNGValueCustom('ReqTenor','NA');
		}

		return true;
}

	
		
	function Address_Validate(opType)
	{
		var flag_address=false;
		var AddType=getNGValue("addtype");
		var selectedRow = getNGListIndex('cmplx_AddressDetails_cmplx_AddressGrid');
		var preffAddr=getNGValue('PreferredAddress');
		var n = getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		var CustomeType=getNGValue('Customer_Type');//added by prabhakar drop-4 point-3

		if(n>0)
		{
			for(var i=0;i<n;i++){
				var grid_AddType=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				var grid_preffAddr=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,10);
				var grid_CustomeType=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,13)//added  by prabhakar drop-4 point-3
				if(AddType==grid_AddType && opType == 'add' && CustomeType==grid_CustomeType){
					showAlert('addtype',"Cannot add two "+ AddType+" Addresses for "+CustomeType);	//changed by prabhakar drop-4 point-3
					return false;
				}
				else if(AddType==grid_AddType && opType == 'modify' && i!=selectedRow && CustomeType==grid_CustomeType){//changed by prabhakar drop-4 point-3
					showAlert('addtype',"Cannot modify "+ AddType+" as this Address is already added for "+CustomeType);//changed by prabhakar drop-4 point-3	
					return false;
				}
				else if(preffAddr==true && AddType=='Home'){
					showAlert('addtype',alerts_String_Map['VAL066']);
					return false;
				}
				else if(preffAddr==true && grid_preffAddr==preffAddr && CustomeType==grid_CustomeType ){
					showAlert('addtype',alerts_String_Map['VAL093']+" for "+CustomeType);
					return false;
				}	
					
				else
					flag_address = true;
			}		
		}			

		else
		{
			if(preffAddr==true && AddType=='Home'){
				showAlert('addtype',alerts_String_Map['VAL066']);
				return false;
			}
			else if(preffAddr==false && AddType!='Home'){
				showAlert('addtype',alerts_String_Map['VAL060']);
				return false;
			}
			else{
			flag_address = true;		
			}
		}
			
			
		return flag_address;
	}
	

function validate()
{	
var Principal_Approval=getNGValue('Is_Principal_Approval');
//Positioning changed by aman to show the first alert as decision is mandatory
	if(!Decision_Save_Check()){
			return false;
	}
	
	if(getNGValue('all_call')!='')
	{
		var fail_status= getNGValue('all_call');
		fail_status = fail_status.replace("#",",");
		showAlert('','Call failed for : '+fail_status +'  Kindly Re-Run');
			return false;
	} //hritik 
	
	if((getNGValue('cmplx_EmploymentDetails_targetSegCode')=='CAC' || getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,25)=='CAC' ) && getNGValue('cmplx_Liability_New_CACBankName')=='' ){
			showAlert('cmplx_Liability_New_CACBankName','Please fill CAC Bank Name');
			return false;
	}
		var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_KYC_cmplx_KYCGrid');
		 if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan')
			{
				
			for(var i=0;i<n;i++)
			{
				if(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0)=='Y' && getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,4)=='')
				{
					showAlert('',"KYC review date cannot be blank for any row where KYC held is Yes");
					return false;
				}
			}
			    //for existing customers
			}
		
	/*if(!validateKYCGrid())
	{
		return false;  // kyc for existing customer
	}*/
	// disha Changes done to allow decision validation for Reject also
	if(activityName != 'Reject')	
	{
	 //added By akshay on 16/9/17 so that system dont ask for validations when decision id reject
	 if(getNGValue('cmplx_DecisionHistory_Decision')=='Reject'){
		if(isFieldFilled('DecisionHistory_DecisionReasonCode')==false){//changed by akshay on 3/1/18
			showAlert('DecisionHistory_DecisionReasonCode',alerts_String_Map['VAL198']);
			return false;
		}	
		else 
			return true;
	 }
	//ended By akshay on 16/9/17 so that system dont ask for validations when decision id reject
	if(getNGValue('cmplx_Customer_OTPValidation')!=true && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)!='IM' && getNGValue('cmplx_Customer_NTB')!=true){
		showAlert('cmplx_Customer_OTPValidation','Please Validate OTP');
			return false;
	}//by jahnavi
	if (!Customer_Save_Check())
	{
	/*if(getNGValue('cmplx_Customer_CIFNO')!='' && getNGValue('cmplx_Customer_OTPValidation')!=true)
			{
				showAlert('cmplx_Customer_OTPValidation','OTP Validation is Mandatory for Existing Customer!');
				return false;
			}*/
        return false;
	}
	
    else if (!checkMandatory(RLOS_PRODUCT))
        return false;

    else if (getNGValue('PrimaryProduct') == 'Personal Loan' && getNGValue('cmplx_Customer_age') < 18)
    {
        if (!checkMandatory(RLOS_Guarantor))
            return false;
    }
	
	//added on 9/9/2017 by Akshay as per FSD
	 if((getNGValue('cmplx_Customer_VIPFlag')==false || getNGValue('EmploymentType')=='Pensioner') ){
	// if(!((getNGValue('EmploymentType')=='Self Employed') && (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,25)=='CAC' || //getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,25)=='DOC')))
		if (!Income_Save_Check())
			return false;
	}	
		//ended on 9/9/2017 by Akshay as per FSD
		
	 if (getNGValue('EmploymentType').indexOf('Self')>-1 && getNGValue('PrimaryProduct')=='Credit Card'){
		if(!CheckCompanyMandatory())
			{
				return false; 
			}
	}		
   
     if (!Liability_Save_Check())
        return false;
	
		//change started on 9/9/2017 by Akshay to not ask for employment validation when VIP as per FSD
	else if (getNGValue('EmploymentType').indexOf('Salaried')>-1 && getNGValue('cmplx_Customer_VIPFlag')==false){	
		if (!Employment_Save_Check())
			return false;
		}
	
	
		//added by aman for PCSP-94
	else if (getNGValue('EmploymentType').indexOf('Salaried')>-1 && getNGValue('cmplx_Customer_VIPFlag')==true){
		if(Principal_Approval=='Y')
		{
		return true;
		}
 if(getNGValue('cmplx_EmploymentDetails_EmpStatus')=="--Select--" || getNGValue('cmplx_EmploymentDetails_EmpStatus')==null){
			showAlert('cmplx_EmploymentDetails_EmpStatus',alerts_String_Map['VAL056']);	
			return false;
		}
 /*if(getNGValue('cmplx_EmploymentDetails_EmployerType')=="--Select--" || getNGValue('cmplx_EmploymentDetails_EmployerType')==null){
			showAlert('cmplx_EmploymentDetails_EmpStatus','Please fill Employer Type');	
			return false;
		}*/
 if(getNGValue('cmplx_EmploymentDetails_Designation')=="" || getNGValue('cmplx_EmploymentDetails_Designation')==null){
			showAlert('cmplx_EmploymentDetails_Designation','Please fill designation');	
			return false;
		}

		}
	//added by aman for PCSP-94

	
		/*	//below logic added by saurabh on 18th Oct for setting company label at introducion if it is not set to forward it to the child process.
		if(getNGValue('lbl_Comp_Name_val')!=null && getNGValue('lbl_Comp_Name_val')==''){
			if(getNGValue('Employer_Name')!=null && getNGValue('Employer_Name')==''){
				setNGValueCustom('Employer_Name', getNGValue('cmplx_EmploymentDetails_EmpName'));
			}
			setNGValueCustom('lbl_Comp_Name_val',getNGValue('Employer_Name'));
		}--commented by akshay on 3/11/17 as it will not take value to child*/	
	
		//change ended on 9/9/2017 by Akshay to not ask for employment validation when VIP as per FSD

    /*if (!Eligibility_Save_Check())
        return false;
	*/

     if (!checkMandatory(RLOS_ADDRESS) || !checkPrefferedChoice() || !checkForNA_AddressGrid() || !checkMandatory_AddressDetails_Save()){//added by akshay on 16/10/17 for point 28 of PL_NTB sheet--Was able to introduce the WI without adding the mandatory addresses in the address grid
        return false;
		}
		//added by aman for Drop4 on 4th April	
	if (getNGValue('cmplx_CardDetails_SuppCardReq')=='Yes'){
		if(Principal_Approval=='Y')
	{
	return true;
	}
		var gridrowcount = getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		var SupplementRow=getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid');
		var addressFound = '';
		for(var i=0;i<gridrowcount;i++){
			if ((getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('S-')>-1)){
				addressFound='Y';
				break;
			}
		}
		if (SupplementRow>0 && addressFound!='Y'){
			showAlert('DecisionHistory_DecisionReasonCode','Please add Address Details for Supplementary customer');
			return false;
			}
	}
	//added by aman for Drop4 on 4th April
   if (!ContactDetails_Save_Check())
        return false;
   if (isVisible('ELigibiltyAndProductInfo_IFrame2')){
	if(Principal_Approval=='Y')
	{
	return true;
	}	   
		if(getFromJSPTable('ELigibiltyAndProductInfo_IFrame2')!=null){
		var table = getFromJSPTable('ELigibiltyAndProductInfo_IFrame2').rows.length;
		var subProd = com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
		var prod = com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",0,1);
		if(table>1){
			var cc_waiv=getFromJSPTable('ELigibiltyAndProductInfo_IFrame2').rows[1].cells[5].childNodes[0].checked;
			var anyCardSelected = false;
			for(var i=1;i<table;i++){
				if(getFromJSPTable('ELigibiltyAndProductInfo_IFrame2').rows[i].cells[0].childNodes[0].checked){
					anyCardSelected = true;
					//Added for PCASI-2859
					if(anyCardSelected==true && cc_waiv==false && prod=='Personal Loan' && getFromJSPTable('ELigibiltyAndProductInfo_IFrame2').rows[i].cells[3].childNodes[0].value==''){
						showAlert('ELigibiltyAndProductInfo_IFrame2','Please Enter the Eligible for card Final Limit!!!');
				        return false;
					}
					break;
				}
			}
			//below condition added by akshay on 20/9/18
			 if(anyCardSelected==false && cc_waiv==false)	
			{
				showAlert('ELigibiltyAndProductInfo_IFrame2','Please select a card from eligibile cards grid!!!');
				return false;
			} 
			else if ((cc_waiv==false)&& !(subProd=='PU') && !(prod=='Personal Loan' && !anyCardSelected)){
				if (!CardDetails_Save())
					return false;
				}
		}
	}		
	}	
	//change by saurabh on 28th Dec
	if (!Supplement_save_Check() && Principal_Approval!='Y')
        return false;	
    else if (!FATCA_Save_CheckOnDone())
        return false;
    else if (!checkMandatory(RLOS_KYC))
        return false;
    else if (!checkMandatory(RLOS_OECD))
        return false;
	 else if (!checkMandatory(RLOS_REFERENCE))
        return false;
	else if (!Check_Documents_Submit())
	{
		return false;
	}	
	/*
    else if (!ServiceRequest_Save_Check())
        return false;
	*/
//commented by aman because not in use now
	
//added by aman for the check of dds Si and RCV
	if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)!='Personal Loan'){
		if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='IM' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TOP')>-1){
			if(!checkMandatory(RLOS_BT)){
			return false;
			}
			/*if (!SI_Save_Check_ProdSave()){
			return false;
			}--commented by akshay--duplicate method*/
		
		 else if (!DDS_Save_Check()){
			return false;	}
		else if (!SI_Save_Check()){
			return false;}
		else if (!RVC_Save_Check()){
			return false;	}
		}
	} else{
		if(isCustomerMinor()){
			 if (!Minor_Demo_CheckOnDone())
			        return false;
		}
	}
 //added by aman for the check of dds Si and RCV
   
	
	//Deepak new condition to check cmplx_Customer_NEP value added on 07/07/2019 for PCAS-2062
	if(getNGValue('cmplx_Customer_CardNotAvailable')==false && getNGValue('cmplx_Customer_NTB')==false && getNGValue('cmplx_Customer_card_id_available')==false && (getNGValue('cmplx_Customer_NEP')=='--Select--' || getNGValue('cmplx_Customer_NEP')=='--Select--'))
		{
			if(getNGValue('Is_EID_Genuine')!='Y'){
				showAlert('ReadFromCard',alerts_String_Map['VAL204']);
				return false;
			}
			
			if(getNGValue('Is_Entity_Details')!='Y'){
				showAlert('ReadFromCard',alerts_String_Map['VAL219']);
				return false;
			}
		}
		
		
	/*if(getNGValue('aecb_call_status')!='Success'){
			showAlert('Liability_New_fetchLiabilities',alerts_String_Map['VAL186']);
			return false;
		}---Commented on 14/7/2017 By Akshay for testing!!---*/
				
	else if(getNGValue('cmplx_Customer_NTB')==true || (getNGValue('cmplx_Customer_NTB')==false && getNGValue('ExistingCustomerAccount')=='N'))
	{
		if(getNGValue('Is_Account_Create')!='Y' && getNGValue('PrimaryProduct')=='Personal Loan') 
		{
			if(getNGValue('cmplx_DecisionHistory_CifNo')=='' && getNGValue('cmplx_Customer_NTB')==true){
				showAlert('DecisionHistory_Button3',alerts_String_Map['VAL173']);
				return false;
			}
			
			else {
				showAlert('DecisionHistory_Button3','Account Not Created for the Customer');
				return false;	
			}
		}
	}
	else if	(getNGValue('ExistingCustomerAccount')=='Y' && getNGValue('PrimaryProduct')=='Personal Loan' && getNGValue('Account_Number')==''){
		showAlert('ELigibiltyAndProductInfo_Frame2',alerts_String_Map['VAL363']);
		return false;	
	}
	else{
			//disabled by saurabh on 15th May as per mail from Rachit.
			if(getNGValue('cmplx_Customer_OTPValidation')!=true && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)!='IM'  && getNGValue('cmplx_Customer_NTB')!=true){
				showAlert('cmplx_Customer_OTPValidation','Please Validate OTP');
					return false;
			}
			/*
				if(getNGValue('Is_Customer_Eligibility')!='Y'){
					showAlert('FetchDetails',alerts_String_Map['VAL185']);
					return false;
				}
				if(getNGValue('Is_Customer_Details')!='Y'){
					showAlert('FetchDetails',alerts_String_Map['VAL183']);
					return false;
				}
				/*if(getNGValue('Is_Financial_Summary')!='Y'){
					showAlert('IncomeDetails_FinnancialSummary',alerts_String_Map['VAL223']);
					return false;
				}---Commented on 14/7/2017 By Akshay for testing!!---*/
			}
		
		
		if(!validateBlanksuppCardProds()){
		return false;
		}
		
		if(!validateApprovedLimitSupplementary()){
		return false;
		}
		
			
	}
		
	if(getNGValue('cmplx_Customer_OTPValidation')!=true && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)!='IM' && getNGValue('cmplx_Customer_NTB')!=true){
		showAlert('cmplx_Customer_OTPValidation','Please Validate OTP');
			return false;
	}
		
	return true;		
		

}


 	
	function KYC_save_Check()
	{
		/*if(getNGValue('cmplx_KYC_KYCHeld')=='' || getNGValue('cmplx_KYC_KYCHeld')=='--Select--')
				showAlert('cmplx_KYC_KYCHeld',alerts_String_Map['VAL238']);
				
		else if(getNGValue('cmplx_KYC_PEP')=='' || getNGValue('cmplx_KYC_PEP')=='--Select--')
					showAlert('cmplx_KYC_PEP',alerts_String_Map['VAL251']);
		else
		return true;*/
		var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_KYC_cmplx_KYCGrid');
		if(n==0)
		{
			showAlert('cmplx_KYC_cmplx_KYCGrid','KYC Grid cannot be Empty');
	return false;
		}
		
		else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan')
			{
				
			for(var i=0;i<n;i++)
			{
				if(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0)=='Y' && getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,4)=='')
				{
					showAlert('',"KYC review date cannot be blank for any row");
					return false;
				}
			}
			
			}
	return true;
	}
	
	//change started by akshay for validating address added as per Resident/Non Resident Customer----FSD 20/9/17
	function checkPrefferedChoice(){
		var gridRowCount = getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		var prefferedFlag = false;
		var IS_HOME=false,IS_RESIDENCE=false,IS_OFFICE=false;
		var grid_CustomerType=[];
		var allPreffered=0;
		for(var i=0; i<gridRowCount;i++)
		{
			 if(grid_CustomerType.indexOf(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13))==-1)
				{
					grid_CustomerType.push(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13));
				}
		}
			for(var i=0; i<gridRowCount;i++){
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'Home' && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_HOME=true;
				}
				
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'OFFICE' && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_OFFICE=true;
				}
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'RESIDENCE' && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_RESIDENCE=true;
				}
				
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,10) == 'true' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,10) == 'Yes'){
						allPreffered++;//added by prabhakar
						//prefferedFlag = true;						
				}
				
			}
			//added by prabhakar
			if(allPreffered==grid_CustomerType.length)
			{
				prefferedFlag = true;
			}
			
			
				if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false && IS_HOME==false){
					showAlert('addtype',alerts_String_Map['VAL299']);
					return false;
				}
				if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['VAL301']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_HOME==false && IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['VAL300']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_HOME==false){
					showAlert('addtype',alerts_String_Map['VAL302']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false ){
					showAlert('addtype',alerts_String_Map['VAL305']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['VAL304']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_HOME==false){
					showAlert('addtype',alerts_String_Map['VAL303']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && IS_OFFICE==false && IS_RESIDENCE==false ){
					showAlert('addtype',alerts_String_Map['VAL311']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && IS_OFFICE==false){
					showAlert('addtype',alerts_String_Map['VAL310']);
					return false;
				}	
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && ( IS_RESIDENCE==false)){
					showAlert('addtype',alerts_String_Map['VAL309']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_HOME==false) && ( IS_RESIDENCE==false) ){
					showAlert('addtype',alerts_String_Map['VAL308']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_RESIDENCE==false)){
					showAlert('addtype',alerts_String_Map['VAL307'])
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_HOME==false)){
					showAlert('addtype',alerts_String_Map['VAL306'])
					return false;
				}

				else if(!prefferedFlag){
					showAlert('PreferredAddress','Please add Preferred address for all Applicant Type.');
					return false;
				}
				else {
					return true;
				}
			
			
	}
	//Changes Done by aman for preferred choice
	//Change ended by akshay for validating address added as per Resident/Non Resident Customer----FSD 20/9/17

function Customer_Save_Check(){
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(Principal_Approval=='Y')
	{
		   if(isFieldFilled('cmplx_Customer_MAritalStatus')==false)
		   {
                showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['VAL081']);
				return false;      
			}
    
			else if(isFieldFilled('cmplx_Customer_MotherName')==false)
			{
                showAlert('cmplx_Customer_MotherName',alerts_String_Map['VAL247']); 
				return false;      
			}
			else if(getNGValue('cmplx_Customer_NTB')==true && getNGValue('EmploymentType')!='Self Employed'){
				if(getNGValue('cmplx_Customer_Is_Employer_Search')!='Y' && getNGValue('Customer_CheckBox7')!=true)
				{
				  showAlert('Customer_search',alerts_String_Map['CAS018']); 
				return false; 	
				}
			}
			
			return true;
							
	}
     //updated by Tarang on 01/19/2018 against drop 4 point 25  
		var fname = getNGValue('cmplx_Customer_FIrstNAme');
		var mname = getNGValue('cmplx_Customer_MiddleName');
		var lname = getNGValue('cmplx_Customer_LAstNAme');
    if((getNGValue('cmplx_Customer_EmiratesID')=='' || (getNGValue('cmplx_Customer_EmiratesID').length!=15) || (getNGValue('cmplx_Customer_EmiratesID').substr(0,3)!='784'))&&(getNGValue('cmplx_Customer_NEP')== "" || getNGValue('cmplx_Customer_NEP')== "RWEID")){
			if(getNGValue('cmplx_Customer_CardNotAvailable') != false && getNGValue('cmplx_Customer_marsoomID')=='' && getNGSelectedItemText('cmplx_Customer_ResidentNonResident')=='RESIDENT')
			{
				showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL211']);  
					return false;
			}

	}
	if((getNGValue('cmplx_Customer_EmiratesID')=='' || (getNGValue('cmplx_Customer_EmiratesID').length!=15) || (getNGValue('cmplx_Customer_EmiratesID').substr(0,3)!='784'))&&( getNGValue('cmplx_Customer_NEP')== "RWEID"))
	{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL211']);  
					return false;
	}
	

	if ((getNGValue('cmplx_Customer_NEP')!="")&& ((getNGValue('cmplx_Customer_EIDARegNo')=='')||(getNGValue('cmplx_Customer_EIDARegNo')==null))){
		showAlert('cmplx_Customer_EIDARegNo',alerts_String_Map['VAL206']);
		return false;
	}	//Arun (06/12/17) commented as this field is not mandatory
     if(getNGValue('cmplx_Customer_FIrstNAme')=='' || getNGValue('cmplx_Customer_FIrstNAme') == null){
	 	
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL224']);
com.newgen.omniforms.formviewer.setNGFocus(cmplx_Customer_FirstNAme);		return false;      }
                
    else if(getNGValue('cmplx_Customer_LAstNAme')=='' || getNGValue('cmplx_Customer_LAstNAme') == null){
	
                showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL239']); 
				com.newgen.omniforms.formviewer.setNGFocus(cmplx_Customer_LastNAme);return false;      }
           
	else if(!validateCustomerName(fname,mname,lname)){
		showAlert('cmplx_Customer_FIrstNAme','Customer Full Name cannot exceed 80 characters');
		return false;
	}		
    else if(getNGValue('cmplx_Customer_DOb')=='' || getNGValue('cmplx_Customer_DOb') == null){
                showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL045']); return false;      }
                
    else if(isFieldFilled('cmplx_Customer_Nationality')==false){
                showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL090']); return false;      }
                
    else if(getNGValue('cmplx_Customer_MobNo')=='' || getNGValue('cmplx_Customer_MobNo') == null){
                showAlert('cmplx_Customer_MobNo',alerts_String_Map['VAL243']);    return false;      }
 
    else if(getNGValue('cmplx_Customer_PAssportNo')=='' && getNGValue('cmplx_Customer_Nationality')!='AE'){
                showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL249']); return false;      }
				else if(getNGValue('cmplx_Customer_Designation')=='')
				{
					 showAlert('cmplx_Customer_Designation',alerts_String_Map['VAL048']); return false; 
				}//sagarika
				
    else  if(((getNGValue('cmplx_Customer_CardNotAvailable')==false) &&(getNGValue('cmplx_Customer_NTB')== false))&&((getNGValue('cmplx_Customer_CIFNO')==''))){             
				showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL033']);
				return false;
	}
	
    else if(isFieldFilled('cmplx_Customer_Title')==false){
                showAlert('cmplx_Customer_Title',alerts_String_Map['VAL284']); return false;      }
            
    else if(isFieldFilled('cmplx_Customer_gender')==false){
                showAlert('cmplx_Customer_gender','gender cannot be blank'); return false;      }
                
    else if(getNGValue('cmplx_Customer_age')==''){
                showAlert('cmplx_Customer_age',alerts_String_Map['VAL003']); return false;      }
    
    else if((getNGValue('cmplx_Customer_IdIssueDate')=='')&&(getNGValue('cmplx_Customer_NEP') == "" || getNGValue('cmplx_Customer_NEP') == null) &&  isFieldFilled('cmplx_Customer_marsoomID')==false && isFieldFilled('cmplx_Customer_EmiratesID')==true){
            showAlert('cmplx_Customer_IdIssueDate',alerts_String_Map['VAL235']); 
			return false;      			
	}
	//below condition changed by nikhil for CR PCSP-367
    else if((getNGValue('cmplx_Customer_EmirateIDExpiry')=='') &&  isFieldFilled('cmplx_Customer_marsoomID')==false )
	{
            showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['VAL208']);    
			return false;     
	}
	
	//changed by nikhil for Wrong validation in Renewed EID case
	if(getNGValue('cmplx_Customer_NEP')!='EXEID' && getNGValue('cmplx_Customer_NEP')!='RWEID'){
    if(validatePastDate('cmplx_Customer_EmirateIDExpiry',"Emirate Card has expired!!")==false)
			return false;
	}
	// commented  by akshay on 20/11/2017 as below condition handles both these cases 
    /*else if((getNGValue('cmplx_Customer_VisaNo')=='') && getNGValue('cmplx_Customer_GCCNational')=='N')
                showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);
			
	else if(getNGValue('cmplx_Customer_VisaNo')=='' && getNGValue('cmplx_Customer_Nationality')=='AE')
		showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);*/
	
     if((getNGValue('cmplx_Customer_VisaNo')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);return false;      }
    
   else if((getNGValue('cmplx_Customer_VisaIssuedate')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N' && isFieldFilled('cmplx_Customer_VisaNo')==true){
					
                showAlert('cmplx_Customer_VisaIssuedate',alerts_String_Map['VAL355']);return false;      }
			 
	else if((getNGValue('cmplx_Customer_VIsaExpiry')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N' && isFieldFilled('cmplx_Customer_VisaNo')==true){
                showAlert('cmplx_Customer_VIsaExpiry',alerts_String_Map['VAL292']);return false;      }
	else if(validatePastDate('cmplx_Customer_VIsaExpiry',"Visa has expired!!")==false){
			return false;			}
			
// added by yash on 19/7/2017 for removing the validation of emirates of visa proc 812
    else if (isFieldFilled('cmplx_Customer_EMirateOfVisa') == false && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N')
    {
        showAlert('cmplx_Customer_EMirateOfVisa', alerts_String_Map['VAL210']);
        return false;
    }
	  
// ended 
	  else if(isFieldFilled('cmplx_Customer_ResidentNonResident')==false){
                showAlert('cmplx_Customer_ResidentNonResident',alerts_String_Map['VAL264']);  return false;      }      
                
	else if(getNGValue('cmplx_Customer_PassportIssueDate')=='' && isFieldFilled('cmplx_Customer_PAssportNo')==true){
                showAlert('cmplx_Customer_PassportIssueDate',alerts_String_Map['VAL354']); return false;   
		}
				
    else if(getNGValue('cmplx_Customer_PassPortExpiry')=='' && isFieldFilled('cmplx_Customer_PAssportNo')==true){
                showAlert('cmplx_Customer_PassPortExpiry',alerts_String_Map['VAL250']); return false;      }
	  
	
	else if(validatePastDate('cmplx_Customer_PassPortExpiry',"Passport has expired!!")==false){
			return false;			}
	/*else if (isFieldFilled('cmplx_Customer_EMirateOfVisa') == false)
    {
		if(getNGValue('cmplx_Customer_ResidentNonResident')=='N')
		 {
			 return true;
		 }
		else
		{
			showAlert('cmplx_Customer_EMirateOfVisa', alerts_String_Map['VAL210']);
			return false;
		}
	}--commented by akshay on 20/11/17 as its handled above*/
	
    else if((com.newgen.omniforms.util.getComponentValue(document.getElementById('cmplx_Customer_SecNAtionApplicable'))=='Yes')&&(isFieldFilled('cmplx_Customer_SecNationality')==false)){
                showAlert('cmplx_Customer_SecNationality',alerts_String_Map['VAL267']);return false;      }
    
    if((com.newgen.omniforms.util.getComponentValue(document.getElementById('cmplx_Customer_Third_Nationaity_Applicable'))=='Yes')&&(isFieldFilled('cmplx_Customer_Third_Nationaity')==false)){
                showAlert('cmplx_Customer_Third_Nationaity','Third Nationality cannot be blank');return false;      }//By Alok for Third Nationality Field
				
    else if(isFieldFilled('cmplx_Customer_GCCNational')==false){
                showAlert('cmplx_Customer_GCCNational',alerts_String_Map['VAL229']);  return false;      }
			
    else if(isFieldFilled('cmplx_Customer_CustomerCategory')==false){
                showAlert('cmplx_Customer_CustomerCategory',alerts_String_Map['VAL182']);return false;      }
 
    else if(isFieldFilled('cmplx_Customer_yearsInUAE')==false &&(getNGValue('cmplx_Customer_Nationality')!='AE') && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_yearsInUAE',alerts_String_Map['VAL254']);return false;      }
                
    else if(isFieldFilled('cmplx_Customer_MAritalStatus')==false){
                showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['VAL081']);return false;      }
    
	else if(isFieldFilled('cmplx_Customer_MotherName')==false){
                showAlert('cmplx_Customer_MotherName',alerts_String_Map['VAL247']); return false;      }       
                
				
    else if(isFieldFilled('cmplx_Customer_EmirateOfResidence')==false){
                showAlert('cmplx_Customer_EmirateOfResidence',alerts_String_Map['VAL209']);return false;      }
                
    else if(isFieldFilled('cmplx_Customer_COuntryOFResidence')==false){
                showAlert('cmplx_Customer_COuntryOFResidence',alerts_String_Map['VAL177']);return false;     
	}
	//below code added by nikhil for PCSP-536				
	else if(getNGValue('cmplx_Customer_VisaIssuedate')!='' && !validateFutureDateexcCurrent('cmplx_Customer_VisaIssuedate')){
				return false;
		}	
	else if(getNGValue('cmplx_Customer_NTB')==true){
				if(getNGValue('cmplx_Customer_Is_Employer_Search')!='Y' && getNGValue('Customer_CheckBox7')!=true && getNGValue('EmploymentType')!='Self Employed')
				{
				  showAlert('Customer_search',alerts_String_Map['CAS018']); 
				return false; 	
				}
			}		
	/*else if(getNGValue('cmplx_Customer_VisaIssuedate')!='Y'){
		showAlert('customer_search','Employer Search is mandatory!!');
		return false;
	}*/
     if(isCustomerMinor()){
		 if(getNGValue('cmplx_Customer_guarname')==''){	
			showAlert('cmplx_Customer_guarname',alerts_String_Map['PL382']);
			return false;
		}	
		 if(getNGValue('cmplx_Customer_guarcif')==''){	
				showAlert('cmplx_Customer_guarcif',alerts_String_Map['PL390']);
				return false;
		}
		 if(getNGValue('cmplx_Customer_guarcif') !="" && getNGValue('cmplx_Customer_guarcif') .length!=7){
				showAlert('cmplx_Customer_guarcif','Please Enter valid Guardian CIF');
				return false;
		 }
	}
	
        return true;
}        
		


function product_Save_Check()
{
	if(com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")==0)
			showAlert('Product_type',alerts_String_Map['VAL259']);
	else
			return true;
	return false;
}

function validateCustomerName(fname,mname,lname){
	if((fname.length+mname.length+lname.length)>80){
		return false;
	}
	return true;
}

// added by yash on 27/7/2017 for the proc 801
function company_save()
{  
     //Added by Rajan to disable save company details for PA self employed PCASP-597
	 if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed' && getNGValue('cmplx_Customer_VIPFlag')==true)
	 {
		 return true;
	 }
	
	if(getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid")==0){
			showAlert('compIndus',alerts_String_Map['VAL174']);
			return false;
	}		
	 if (getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails")!=0 || getLVWRowCount("cmplx_PartnerDetails_cmplx_partnerGrid")!=0)
	{
		showAlert('AuthorisedSignDetails_Name',alerts_String_Map['VAL322']);
		return false;
	}
	//Added by Rajan to disable save company details for PA self employed PCASP-597
	 if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed'&& getNGValue('cmplx_Customer_VIPFlag')==true)
	 {
		 return true;
	 }
	return true;	
}
// ended by yash 
	
function Guarantor_Save_Check()
{
		return true;				
}
	
function Income_Save_Check()
{
if(getNGValue('cmplx_Customer_VIPFlag')==true)
	{
	return true;
	}
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	var EmpType=getNGValue("EmploymentType");
	if(Principal_Approval=='Y' && (EmpType=='Salaried' || EmpType=='Salaried Pensioner' || (EmpType=='Self Employed' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA')))// Added by Rajan for PCASP-597
	{
		if(isFieldFilled('cmplx_IncomeDetails_totSal')==false)
		{
			showAlert('cmplx_IncomeDetails_totSal',alerts_String_Map['PL035']);
			return false;
		}
		else if(isFieldFilled('cmplx_IncomeDetails_grossSal')==false)
		{
			showAlert('cmplx_IncomeDetails_grossSal',alerts_String_Map['CAS005']);
			return false;
		}
		else if(isFieldFilled('cmplx_IncomeDetails_netSal1')==false)
		{
			showAlert('cmplx_IncomeDetails_netSal1',alerts_String_Map['CAS006']);
			return false;
		}
		else if(isFieldFilled('cmplx_IncomeDetails_Accomodation')==false)
		{
			showAlert('cmplx_IncomeDetails_Accomodation',alerts_String_Map['CAS007']);
			return false;
		}
		
		else if(getNGValue('cmplx_IncomeDetails_Accomodation')=='Yes' && getNGValue('cmplx_IncomeDetails_AccomodationValue')=='')
		{
			showAlert('cmplx_IncomeDetails_AccomodationValue',alerts_String_Map['CAS008']);
			return false;
		}//cmplx_IncomeDetails_SalaryDay
	    else if(isFieldFilled('cmplx_IncomeDetails_SalaryDay')==false)
		{
			showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['CAS009']);
			return false;
		}
		
		else 
		{
			return true;
		}
	}
	if(getNGValue('cmplx_Customer_VIPFlag')==true)
	{
	return true;
	}
	//var EmpType=getNGValue("empType");//Tarang to be removed on friday(1/19/2018)
	
	var AppType=getNGValue("Application_Type");
	if(EmpType=='Salaried' || EmpType=='Salaried Pensioner'){
		//Changed by Aman 22102018		
		if(isFieldFilled('cmplx_IncomeDetails_grossSal')==false){
				showAlert('cmplx_IncomeDetails_grossSal',alerts_String_Map['VAL230']);
				return false;
				}
		else if(isFieldFilled('cmplx_IncomeDetails_totSal')==false){
				showAlert('cmplx_IncomeDetails_totSal',alerts_String_Map['VAL286']);
				return false;
		}
		else if(isFieldFilled('cmplx_IncomeDetails_AvgNetSal')==false){
					showAlert('cmplx_IncomeDetails_AvgNetSal',alerts_String_Map['PL038']);
					return false;
		}
		// disha - 5-10-2017 code commented to make value of accomodation non-mandatory	
		 //else if((com.newgen.omniforms.util.getComponentValue(document.getElementById('cmplx_IncomeDetails_Accomodation'))=='Yes')&&(getNGValue('cmplx_IncomeDetails_AccomodationValue')==''))
                //showAlert('cmplx_IncomeDetails_AccomodationValue',alerts_String_Map['VAL289']);
    
		else if(isFieldFilled('cmplx_IncomeDetails_SalaryDay')==false){
				showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['VAL265']);
				return false;
		}
		else if(isFieldFilled('cmplx_IncomeDetails_SalaryXferToBank')==false && getNGValue("PrimaryProduct")=="Credit Card"){
				showAlert('cmplx_IncomeDetails_SalaryXferToBank',alerts_String_Map['VAL324']);
				return false;
		}
		
		/*else if(getNGValue('cmplx_IncomeDetails_Is_Tenancy_contract')=='--Select--' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan'){
			showAlert('cmplx_IncomeDetails_Is_Tenancy_contract',alerts_String_Map['VAL335']);
			return false;
		}	*/
		
		else if(isFieldFilled('cmplx_IncomeDetails_StatementCycle')==false && getNGValue("PrimaryProduct")=="Credit Card")
		{	
			showAlert('cmplx_IncomeDetails_StatementCycle',alerts_String_Map['VAL278']);
			return false;
		}
		//Changed by Aman 22102018
			/*else if(getNGValue('cmplx_IncomeDetails_Accomodation')==null)
			{
			showAlert('cmplx_IncomeDetails_Accomodation','Accomodation Provided is Mandatory!');
			return false;
			}*/
			if (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan'){
				if(getNGValue('cmplx_IncomeDetails_netSal2')=="")
				{
					showAlert('cmplx_IncomeDetails_netSal2','Net Salary 2 is Mandatory!');
					return false;
				}
				if(getNGValue('cmplx_IncomeDetails_netSal3')=="")
				{
					showAlert('cmplx_IncomeDetails_netSal3','Net Salary 3 is Mandatory!');
					return false;
				}
				if((getNGValue('cmplx_IncomeDetails_Is_Tenancy_contract')=='--Select--' || getNGValue('cmplx_IncomeDetails_Is_Tenancy_contract')=='Select') && getNGValue('cmplx_EmploymentDetails_CurrEmployer')!='CA' && getNGValue('cmplx_IncomeDetails_Accomodation')=='Yes'){
					showAlert('cmplx_IncomeDetails_Is_Tenancy_contract',alerts_String_Map['VAL335']);
					return false;
				}	
			}
		 if(AppType!='RSEL' && AppType!='RELT' && AppType!='RELTN'){
			return true;			
		}	
		
	}

	//changed by akshay for proc 12230
	 if((EmpType=='Self Employed'  || AppType=='RSEL' || AppType=='RELT' || AppType=='RELTN') && (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,24)!='Surrogate' && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,25)!='DOC')){
		var rowCount=getLVWRowCount('cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails');
		if(isFieldFilled('cmplx_IncomeDetails_AvgBalFreq')==false)
					showAlert('cmplx_IncomeDetails_AvgBalFreq',alerts_String_Map['VAL164']);
				
		else if(isFieldFilled('cmplx_IncomeDetails_AvgBal')==false)
				showAlert('cmplx_IncomeDetails_AvgBal',alerts_String_Map['VAL165']);
				
		else if(isFieldFilled('cmplx_IncomeDetails_CreditTurnoverFreq')==false)
				showAlert('cmplx_IncomeDetails_CreditTurnoverFreq',alerts_String_Map['VAL180']);
				
		else if(isFieldFilled('cmplx_IncomeDetails_CredTurnover')==false)
				showAlert('cmplx_IncomeDetails_CredTurnover',alerts_String_Map['VAL179']);
		
		else if(isFieldFilled('cmplx_IncomeDetails_AvgCredTurnoverFreq')==false)
				showAlert('cmplx_IncomeDetails_AvgCredTurnoverFreq',alerts_String_Map['VAL168']);
				
		else if(isFieldFilled('cmplx_IncomeDetails_AvgCredTurnover')==false)
					showAlert('cmplx_IncomeDetails_AvgCredTurnover',alerts_String_Map['VAL167']);
		
		else if(rowCount==0){
					showAlert('cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails',alerts_String_Map['VAL270']);
		}
		
		
		else if(getNGValue("PrimaryProduct")=="Credit Card")
		{
			if(isFieldFilled('cmplx_IncomeDetails_StatementCycle2')==false)
				showAlert('cmplx_IncomeDetails_StatementCycle2',alerts_String_Map['VAL278']);
			else
				return true;
		}
	else
		return true;
	}
	else
		return true;
return false;
}		
	
function Employment_Save_Check()
{
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(Principal_Approval=='Y')
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
		else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='' || getNGValue('cmplx_EmploymentDetails_targetSegCode')=='--Select--')
		{
			showAlert('cmplx_EmploymentDetails_targetSegCode',alerts_String_Map['VAL330']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_Indus_Micro')=='')
		{
			showAlert('cmplx_EmploymentDetails_Indus_Micro',alerts_String_Map['CAS023']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_DOJ')=='' && getNGValue('cmplx_Customer_VIPFlag')==false)
		{
			showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['CAS022']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='' || getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='Select' || getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='--Select--')
		{
			showAlert('cmplx_EmploymentDetails_JobConfirmed','Fill confirmed in Job!!');
			return false;	
		} // pcasi 3669 hritik 26.7.21
		else if((getNGValue('cmplx_EmploymentDetails_LengthOfBusiness')=='')  && getNGValue('cmplx_Customer_VIPFlag')==false)
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
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='IM'){
				if(!getNGValue('cmplx_EmploymentDetails_IncInCC')&& !getNGValue('cmplx_EmploymentDetails_IncInPL'))
					{
						
						showAlert('cmplx_EmploymentDetails_LengthOfBusiness',alerts_String_Map['PL293']);
						return false;
					}
				}
		}
		if(getNGValue('cmplx_EmploymentDetails_Indus_Macro')=='')
		{
			showAlert('cmplx_EmploymentDetails_Indus_Macro',alerts_String_Map['CAS020']);
			return false;
		}
		else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='EMPID' && getNGValue('cmplx_EmploymentDetails_IndusSeg')=='')
		{
			showAlert('cmplx_EmploymentDetails_IndusSeg',alerts_String_Map['CAS019']);
			return false;
			
		}
		
	 
		else
		{
			return true;
		}
	}
	if(getNGValue('cmplx_Customer_VIPFlag')==true)
		{
		return true;
		}
 	//alert(getNGValue('cmplx_EmploymentDetails_targetSegCode'));
	
	/*----commented by akshay on 31/1/18
	var targsegcode = getNGValue('cmplx_EmploymentDetails_targetSegCode');
		if (getNGValue('cmplx_EmploymentDetails_targetSegCode')=='NEP')
		{
			if (!checkMandatory(RLOS_NEP_EMPLOYMENT))
				return false;
		}

		else if ( getNGValue('cmplx_EmploymentDetails_targetSegCode')!=null && getNGValue('cmplx_EmploymentDetails_targetSegCode').indexOf('Freezone')>-1)
		{
			if (!checkMandatory(RLOS_Freezone_EMPLOYMENT))
				return false;
		}

		else if (getNGValue('cmplx_EmploymentDetails_targetSegCode')=='Tenancy contract')
		{
			if (!checkMandatory(RLOS_Tenancy_EMPLOYMENT))
				return false;
		}

		else if ( getNGValue('cmplx_EmploymentDetails_targetSegCode')!=null && (targsegcode == 'AVI' || targsegcode == 'EDU' || targsegcode == 'HOT' || targsegcode == 'MED' || targsegcode == 'PROM'))
		{
			if (!checkMandatory(RLOS_EMpId_EMPLOYMENT))
				return false;
		}
*/
		 if (!checkMandatory(RLOS_EMPLOYMENT))
			return false;
		
		if(getNGValue('cmplx_EmploymentDetails_EMpCode')== '' && getNGValue('cmplx_EmploymentDetails_Others')==false)
		{
			showAlert('cmplx_EmploymentDetails_EMpCode',alerts_String_Map['VAL332']);
			return false;
		}//Arun (11/12/17)
		
		else if(getNGValue('cmplx_EmploymentDetails_DOJ')=='' && getNGValue('EmploymentType').indexOf("Pensioner")==-1 && getNGValue('cmplx_Customer_VIPFlag')==false)
		{
			showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['PL411']);
			return false;	
		}
		//fOR pcsp-643
		else if(getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='' || getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='Select' || getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='--Select--')
		{
			showAlert('cmplx_EmploymentDetails_JobConfirmed','Fill confirmed in Job!!');
			return false;	
		} // pcasi 3669 hritik 26.7.21
		else if(getNGValue('cmplx_EmploymentDetails_IndusSeg')=='' && getNGValue('cmplx_EmploymentDetails_targetSegCode')=='EMPID'){
			showAlert('cmplx_EmploymentDetails_IndusSeg',alerts_String_Map['VAL384']);
			return false;
		}
			//fOR pcsp-643
		 else if(getNGValue('cmplx_EmploymentDetails_LOSPrevious')=='' && getNGValue('cmplx_EmploymentDetails_NepType')=='NEWJ'){
			showAlert('cmplx_EmploymentDetails_LOSPrevious',alerts_String_Map['VAL337']);
			return false;
		}	
		else  if(getNGValue('Application_Type')=='RESC' || getNGValue('Application_Type')=='RESN' || getNGValue('Application_Type')=='RESR'){
			if(getNGValue('cmplx_EmploymentDetails_channelcode')=='--Select--'){
				showAlert('cmplx_EmploymentDetails_channelcode',alerts_String_Map['VAL253']);
				return false;
			}
		}
		//change by saurabh for Tarng points on 13 nov.
		else if(getNGValue('cmplx_EmploymentDetails_Designation')=='')
		{
			showAlert('cmplx_EmploymentDetails_Designation',alerts_String_Map['PL057']);
			return false;	
		}
		else if(getNGValue('cmplx_EmploymentDetails_DesigVisa')=='' || getNGValue('cmplx_EmploymentDetails_DesigVisa')=='Select')
		{
			showAlert('cmplx_EmploymentDetails_DesigVisa',alerts_String_Map['VAL047']);
			return false;	
		}
		/*if(getNGValue('PrimaryProduct')=='Personal Loan' &&(getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='' || getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='Select')){
			showAlert('cmplx_EmploymentDetails_HeadOfficeEmirate',alerts_String_Map['PL062']);
		    return false;
		}*/
		if(getNGValue('PrimaryProduct')=='Personal Loan'){
			if(!getNGValue('cmplx_EmploymentDetails_IncInCC')&& !getNGValue('cmplx_EmploymentDetails_IncInPL')){
				if(getNGValue('cmplx_EmploymentDetails_TL_Number')==''){
					showAlert('cmplx_EmploymentDetails_TL_Number','Please Enter Tl Number.');
					return false;
				}
				if(getNGValue('cmplx_EmploymentDetails_TL_Emirate')==''){
					showAlert('cmplx_EmploymentDetails_TL_Emirate','Please Enter Tl Emirate.');
					return false;
				}
			}

		}

		/*if(getNGValue('cmplx_Customer_Nationality')!='AE' ){//&& (getNGValue('cmplx_EmploymentDetails_VisaSponser')=='' || getNGValue('cmplx_EmploymentDetails_VisaSponser')==' ')
			showAlert('cmplx_EmploymentDetails_VisaSponser',alerts_String_Map['VAL291']);
				return false;
		}*/
		
		
		
   return true;
}

	/*
	 if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='--Select--')
				showAlert('cmplx_EmploymentDetails_ApplicationCateg',alerts_String_Map['VAL162']);	
				
	else if(getNGValue('cmplx_EmploymentDetails_EmpName')=='')
				showAlert('cmplx_EmploymentDetails_EmpName',alerts_String_Map['VAL213']);
				
	else if(getNGValue('cmplx_EmploymentDetails_EMpCode')=='')
					showAlert('cmplx_EmploymentDetails_EmpName',alerts_String_Map['VAL212']);	
					
	else if(getNGValue('cmplx_EmploymentDetails_Emp_Type')=='--Select--')
				showAlert('cmplx_EmploymentDetails_Emp_Type',alerts_String_Map['VAL214']);
				
	else if(getNGValue('cmplx_EmploymentDetails_CurrEmployer')=='--Select--')
				showAlert('cmplx_EmploymentDetails_CurrEmployer',alerts_String_Map['VAL181']);
	
	else if(getNGValue('cmplx_EmploymentDetails_EmpContractType')=='--Select--')
				showAlert('cmplx_EmploymentDetails_EmpContractType',alerts_String_Map['VAL218']);
	
	else if(getNGValue('cmplx_EmploymentDetails_EmpIndusSector')=='--select--')
				showAlert('cmplx_EmploymentDetails_EmpIndusSector','alerts_String_Map['VAL217']);
	
	else if(getNGValue('cmplx_EmploymentDetails_Indus_Macro')=='--select--')
				showAlert('cmplx_EmploymentDetails_Indus_Macro',alerts_String_Map['VAL215']);

	else if(getNGValue('cmplx_EmploymentDetails_Indus_Micro')=='--select--')
				showAlert('cmplx_EmploymentDetails_Indus_Micro',alerts_String_Map['VAL216']);

				
	else if(getNGValue('cmplx_EmploymentDetails_DOJ')=='')
				showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['VAL201']);						
	
	else if(getNGValue('cmplx_EmploymentDetails_Designation')=='--Select--')
				showAlert('cmplx_EmploymentDetails_Designation',alerts_String_Map['VAL048']);
				
	else if(getNGValue('cmplx_EmploymentDetails_DesigVisa')=='--Select--')
				showAlert('cmplx_EmploymentDetails_DesigVisa',alerts_String_Map['VAL199']);			
	
	else if(getNGValue('name="cmplx_EmploymentDetails_JobConfirmed"')=='--Select--')
				showAlert('name="cmplx_EmploymentDetails_JobConfirmed"',alerts_String_Map['VAL175']);		
				
	else if(getNGValue('cmplx_EmploymentDetails_LOS')=='')
				showAlert('cmplx_EmploymentDetails_LOS',alerts_String_Map['VAL241']);
	
	else if(getNGValue('cmplx_EmploymentDetails_FreezoneName')=='')
				showAlert('cmplx_EmploymentDetails_FreezoneName',alerts_String_Map['VAL228']);
	
	else if(getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='')
				showAlert('cmplx_EmploymentDetails_HeadOfficeEmirate',alerts_String_Map['VAL231']);
				
	else if(getNGValue('cmplx_EmploymentDetails_IncInPL')==false)
				showAlert('cmplx_EmploymentDetails_IncInPL',alerts_String_Map['VAL237']);
				
	else if(getNGValue('cmplx_EmploymentDetails_IncInCC')==false)
				showAlert('cmplx_EmploymentDetails_IncInCC',alerts_String_Map['VAL236']);								
	
	else
		return true;
		
return false;			
	}
	*/
	
	
function DMandatoryRLOS()
{
	if (getNGValue('IS_PRODUCT_DUPLICATE') == 'Yes')
	{
		showAlert('Add', alerts_String_Map['VAL027']);
		return false;
		
	}
	return true;
}



	function Liability_Save_Check()
	{
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(Principal_Approval=='Y')
	{
	return true;
	}
		if(getNGValue("cmplx_Liability_New_AECBconsentAvail")==false){
				setEnabledCustom("Liability_New_Button1",false);
				showAlert('cmplx_Liability_New_AECBconsentAvail',alerts_String_Map['VAL157']);
				return false;
				}
				
		 if(getNGValue("cmplx_Liability_New_AECBconsentAvail")==true){
			setEnabledCustom("Liability_New_Button1",true);	
		}
		 			
		 if(getNGValue("cmplx_Liability_New_TAI")=='' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6) != "Self Employed"){
			showAlert('cmplx_Liability_New_TAI',alerts_String_Map['VAL281']);
			return false;
			}
		 if(getNGValue("cmplx_Liability_New_DBRNet")==''){
			showAlert('cmplx_Liability_New_DBRNet',alerts_String_Map['VAL190']);
			return false;
			}
			
		 if(getNGValue("cmplx_Liability_New_AggrExposure")==''){
			showAlert('cmplx_Liability_New_AggrExposure',alerts_String_Map['VAL159']);	
			return false;
			}
	return true;
	}
	
	function Eligibility_Save_Check()
	{
		if (getNGValue('Subproduct_productGrid')=='IM')
		{
			if (!checkMandatory(RLOS_ELIGIBILITY_IM))
				return false;
		}
		
		else if (getNGValue('Subproduct_productGrid')=='SAL')
		{
			if (!checkMandatory(RLOS_ELIGIBILITY_SAL))
				return false;
		}
		
		else if (getNGValue('Subproduct_productGrid')=='BTC' || getNGValue('Subproduct_productGrid')=='SE')
		{
			if (!checkMandatory(RLOS_ELIGIBILITY_BTC))
				return false;
		}
		
		else if (getNGValue('Subproduct_productGrid')=='IM')
		{
			if (!checkMandatory(RLOS_ELIGIBILITY_IM))
				return false;
		}
		
		else if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1){
			if (!checkMandatory(RLOS_ELIGIBILITY))
				return false;
			if(parseInt(getNGValue('cmplx_EligibilityAndProductInfo_TakeoverAMount'))>parseInt(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit'))){
				showAlert('cmplx_EligibilityAndProductInfo_TakeoverAMount',alerts_String_Map['VAL282']);
				return false;
			}	
		}
		else{
			if (!checkMandatory(RLOS_ELIGIBILITY))
				return false;
		}
		return true;
	}	
		
		
	
function ContactDetails_Save_Check()
{
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(Principal_Approval=='Y')
	{
		if (getNGValue('AlternateContactDetails_Email1') == '')
		{
        showAlert('AlternateContactDetails_Email1', alerts_String_Map['VAL207']);
        return false;
		}
		else if (getNGValue('AlternateContactDetails_MobileNo1') == '')
		{
        showAlert('AlternateContactDetails_MobileNo1', alerts_String_Map['VAL244']);
        return false;
		}
		else if (getNGValue('AlternateContactDetails_HomeCOuntryNo') == '')
		{
        showAlert('AlternateContactDetails_HomeCOuntryNo', alerts_String_Map['VAL234']);
        return false;
		}
		else if (((getNGValue('AlternateContactDetails_carddispatch') == '--Select--' || getNGValue('AlternateContactDetails_carddispatch') == '') && (getNGValue('is_cc_waiver_require') != 'Y')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='LI' && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='TOPIM')))
		{
        showAlert('AlternateContactDetails_carddispatch', alerts_String_Map['VAL169']);
        return false;
		}
		else
		{
			return true;
		}
	}
	//Cr61 Prabhakar 27-08-2019
	if (validateMail1('AlternateContactDetails_Email2') || getNGValue('AlternateContactDetails_Email2')=='')
	{
		setNGValueCustom('AlternateContactDetails_Email2',getNGValue('AlternateContactDetails_Email1'));
	}
    if (getNGValue('AlternateContactDetails_MobileNo1') == '')
    {
        showAlert('AlternateContactDetails_MobileNo1', alerts_String_Map['VAL244']);
        return false;
    }

    else if (getNGValue('AlternateContactDetails_MobNo2') == '')
    {
        showAlert('AlternateContactDetails_MobNo2', alerts_String_Map['VAL245']);
        return false;
    }
	
	else if (getNGValue('AlternateContactDetails_HomeCOuntryNo') == '')
    {
        showAlert('AlternateContactDetails_HomeCOuntryNo', alerts_String_Map['VAL234']);
        return false;
    }
	
	else if (getNGValue('AlternateContactDetails_OfficeNo') == '')
    {
        showAlert('AlternateContactDetails_OfficeNo', alerts_String_Map['VAL248']);
        return false;
    }
	
	else if (((getNGValue('AlternateContactDetails_carddispatch') == '--Select--' || getNGValue('AlternateContactDetails_carddispatch') == '') && (getNGValue('is_cc_waiver_require') != 'Y')) && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='LI' && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='TOPIM')))
    {
        showAlert('AlternateContactDetails_carddispatch', alerts_String_Map['VAL169']);
        return false;
    }

    else if (getNGValue('AlternateContactDetails_Email1') == '')
    {
        showAlert('AlternateContactDetails_Email1', alerts_String_Map['VAL207']);
        return false;
    }

	//added b7y nikhil for CR-CIF CReation Failaing
	 else if (getNGValue('AlternateContactDetails_Email2') == '')
    {
        showAlert('AlternateContactDetails_Email2', alerts_String_Map['VAL375']);
        return false;
    }
else if(isVisible('AlternateContactDetails_AirArabiaIdentifier'))//sagarika to make air arabia mandatory
{
	if(getNGValue('AlternateContactDetails_AirArabiaIdentifier')=='')
	{
		 showAlert('AlternateContactDetails_AirArabiaIdentifier', alerts_String_Map['VAL386']);
        return false;
	}
}
    //added by saurabh1 for enroll
/* PCASI-2856
else if(isVisible('AlternateContactDetails_EnrollRewardsIdentifier'))
{
	if(getNGValue('AlternateContactDetails_EnrollRewardsIdentifier')=='')
	{
		 showAlert('AlternateContactDetails_EnrollRewardsIdentifier', 'Enroll Rewards cannot be blank');
        return false;
	}
}*/
  //saurabh1 enroll end  
// added by yash on 19/7/2017 for Customer Domicile Branch cannot be blank proc 824
     else   if (getNGValue('AlternateContactDetails_custdomicile') == '--Select--')
        {
            showAlert('AlternateContactDetails_custdomicile', alerts_String_Map['VAL184']);
            return false;
        }
// ended by yash

//added By Akshay on 14/9/2017 to validate no's as per address selected
	else if(!checkMandatory_HomeNumber()){
		return false;
	}
   

    return true;
}

	function ReferenceDetails_Save_Check()
	{
		if(!checkMandatory(RLOS_REFERENCE))
			return false;
		return true;	
	}
	
		//added By AKshay on 10/10/17-----upon introduce click,if fragment not fetched,mandatory alert was not shown
	function CardDetails_Save()
	{
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	//Added by shivang
	if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC' && getNGValue('cmplx_CardDetails_CompanyEmbossingName')==''){
		showAlert('cmplx_CardDetails_CompanyEmbossingName',alerts_String_Map['CC297']);	
		return false;
		}
	if(Principal_Approval=='Y')
	{
		if(isFieldFilled('cmplx_CardDetails_CardEmbossingName')==false)
		{
			showAlert('cmplx_CardDetails_CardEmbossingName',alerts_String_Map['VAL029']);
			return false;
		}
		else  if(getNGValue('cmplx_CardDetails_SendStatTo')=='')
		{
		  showAlert('cmplx_CardDetails_SendStatTo',alerts_String_Map['VAL271']);
		  return false;
		}
		else
		{
			return true;
		}
	}
	if (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='LI'){
		if(isFieldFilled('cmplx_CardDetails_CardEmbossingName')==false)
		{
			showAlert('cmplx_CardDetails_CardEmbossingName',alerts_String_Map['VAL029']);
			return false;
		}
		
		else if(getNGValue('PrimaryProduct')=='Credit Card' && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='BTC')  && isVisible('cmplx_CardDetails_CompanyEmbossingName')){
			 if(isFieldFilled('cmplx_CardDetails_CompanyEmbossingName')==false && com.newgen.omniforms.formviewer.isEnabled('cmplx_CardDetails_CompanyEmbossingName')==true)
			{
				showAlert('cmplx_CardDetails_CompanyEmbossingName',alerts_String_Map['VAL036']);
				return false;
			}
		}
		
		 if(getNGValue('CardDetails_Islamic_mandatory')=='Y')
		{		
			if(isFieldFilled('cmplx_CardDetails_CharityOrg')==false && isVisible('cmplx_CardDetails_CharityOrg'))
			{
					showAlert('cmplx_CardDetails_CharityOrg',alerts_String_Map['VAL172']);
					return false;
			}
					
		    else if(getNGValue('cmplx_CardDetails_CharityAmount')=='' && getNGValue('cmplx_CardDetails_CharityOrg')!='No' && isVisible('cmplx_CardDetails_CharityAmount'))
			{
					showAlert('cmplx_CardDetails_CharityAmount',alerts_String_Map['VAL171']);
					return false;
			}
		}
		
		 if(getNGValue('cmplx_CardDetails_SendStatTo')=='')
		{
		  showAlert('cmplx_CardDetails_SendStatTo',alerts_String_Map['VAL271']);
		  return false;
		}
		
		if(isVisible('cmplx_CardDetails_statCycle'))
		{
			 if(getNGValue('cmplx_CardDetails_statCycle')=='')
			{
			  showAlert('cmplx_CardDetails_statCycle',alerts_String_Map['VAL278']);
			  return false;
			}
		}
		/*
		  if(getNGValue('cmplx_CardDetails_SuppCardReq')=='--Select--')
		{
		  showAlert('cmplx_CardDetails_SuppCardReq',alerts_String_Map['VAL280']);
		  return false;
		}*/
		//++below code added by nikhil for Self-Supp CR
		if(getNGValue('cmplx_CardDetails_SelfSupp_required')=='Yes' && getNGValue('cmplx_CardDetails_Selected_Card_Product')=='')
		{
			showAlert('cmplx_CardDetails_SelfSupp_required',alerts_String_Map['VAL386']);
			  return false;
		}
		if(getNGValue('cmplx_CardDetails_SelfSupp_required')=='' || getNGValue('cmplx_CardDetails_SelfSupp_required')=='--Select--')
		{
			showAlert('cmplx_CardDetails_SelfSupp_required',alerts_String_Map['VAL387']);
			  return false;
		}
// By simi
		if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan' && getNGValue('is_cc_waiver_require') != 'Y' && (getNGValue('cmplx_CardDetails_SuppCardReq')=='' || getNGValue('cmplx_CardDetails_SuppCardReq')=='--Select--'))
		{
			showAlert('cmplx_CardDetails_SuppCardReq',alerts_String_Map['PL102']);
			  return false;
		}
		//--above code added by nikhil for Self-Supp CR
	return true;
	}
	}
		//ended by Akshay on 25/9/17 for point 16(Changes done in existing function only)

		
	function Supplement_save_Check()
	{
		if(getNGValue('cmplx_CardDetails_SuppCardReq')=='Yes')
		{
			if(getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid')==0)
			{
			  showAlert('SupplementCardDetails_cmplx_SupplementGrid',alerts_String_Map['VAL279']);
			  return false;
			}
		}
	return true;	
	}
	
	function Minor_Demo_CheckOnDone(){
		var grid_name='cmplx_GuarantorDetails_cmplx_GuarantorGrid';
		var i=0;
		if(getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails')>0){
			var flag_DataFound=false;
			for(var j=0;j<getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails');j++){
				if(getLVWAT('cmplx_FATCA_cmplx_GR_FatcaDetails',j,13)=='G-'+getLVWAT(grid_name,i,7)+' '+getLVWAT(grid_name,i,9)){
					flag_DataFound=true;
					break;
				}	
			}
			if(flag_DataFound==false){
				showAlert('cmplx_FATCA_cmplx_GR_FatcaDetails',alerts_String_Map['PL429']);	
				return false;	
			}								
		}	
		if(getLVWRowCount('cmplx_KYC_cmplx_KYCGrid')>0){
			flag_DataFound=false;
			for(var j=0;j<getLVWRowCount('cmplx_KYC_cmplx_KYCGrid');j++){
				if(getLVWAT('cmplx_KYC_cmplx_KYCGrid',j,3)=='G-'+getLVWAT(grid_name,i,7)+' '+getLVWAT(grid_name,i,9)){
					flag_DataFound=true;
					break;
				}	
			}
			if(flag_DataFound==false){
				showAlert('cmplx_KYC_cmplx_KYCGrid',alerts_String_Map['PL430']);	
				return false;	
			}								
		}
		if(getLVWRowCount('cmplx_OECD_cmplx_GR_OecdDetails')>0){
			flag_DataFound=false;
			for(var j=0;j<getLVWRowCount('cmplx_OECD_cmplx_GR_OecdDetails');j++){
				if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',j,8)=='G-'+getLVWAT(grid_name,i,7)+' '+getLVWAT(grid_name,i,9)){
					flag_DataFound=true;
					break;
				}	
			}
			if(flag_DataFound==false){
				showAlert('cmplx_OECD_cmplx_GR_OecdDetails',alerts_String_Map['PL431']);	
				return false;	
			}								
		}
		return true;
	}

	function FATCA_Save_Check(buttonClick)
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails');
				
			var custTypePickList = document.getElementById("cmplx_FATCA_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			var CustType=getNGValue("cmplx_FATCA_CustomerType");
			var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_FATCA_cmplx_GR_FatcaDetails",i,0));
			}
		if(isFieldFilled('cmplx_FATCA_CustomerType')==false)
		{
			showAlert('cmplx_FATCA_CustomerType','Applicant Type cannot be blank');
		        return false;
		}			
		else if(isFieldFilled('FATCA_USRelaton')==false)
		{
				showAlert('FATCA_USRelaton',alerts_String_Map['VAL288']);
		        return false;
		}
		
		else if(getNGValue('FATCA_USRelaton')=='N' || getNGValue('FATCA_USRelaton')=='R')
		{
			 if(getNGValue('cmplx_FATCA_IdDoc')==false && getNGValue('cmplx_FATCA_DecForInd')==false && getNGValue('cmplx_FATCA_W8Form')==false && getNGValue('cmplx_FATCA_W9Form')==false && getNGValue('cmplx_FATCA_LossOFNationalCertificate')==false)
			{
				showAlert('cmplx_FATCA_iddoc',alerts_String_Map['VAL268']);
				return false;
			}
			
			else if(getNGValue('cmplx_FATCA_W8Form')==true)
			{
				if(getNGValue('cmplx_FATCA_SignedDate')=='')
					{
						showAlert('cmplx_FATCA_SignedDate',alerts_String_Map['VAL276']);
						return false;
					}
			}
			
			else if(getNGValue('cmplx_FATCA_W9Form')==true)
			{
				if(getNGValue('cmplx_FATCA_TINNo')=='')
					{
						showAlert('cmplx_FATCA_TINNo','Tin No cannot be blank');
						return false;
					}
			}
			
			if(getNGValue('cmplx_FATCA_SignedDate')=='')
			{
						showAlert('cmplx_FATCA_SignedDate',alerts_String_Map['VAL276']);
						return false;
			}
						
			else if(getNGValue('cmplx_FATCA_ExpiryDate')=='')
			{
						showAlert('cmplx_FATCA_ExpiryDate',alerts_String_Map['VAL220']);
						return false;
			}					
			/* Commented by prateek on 19-11-2017 as this alert was coming on Introduce for Empty Company Details which is wrong			
			else if(isFieldFilled('cmplx_FATCA_Category')==false)
			{
						showAlert('cmplx_FATCA_Category',alerts_String_Map['VAL174']);
						return false;
			}
			*/
						
		 else if(getNGValue('cmplx_FATCA_ConPerHasUS')=='' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan' )
			{
						showAlert('cmplx_FATCA_ConPerHasUS',alerts_String_Map['VAL176']);
						return false;
			}
			var rowCount = com.newgen.omniforms.formviewer.getItemCount('cmplx_FATCA_selectedReason');			
			if(rowCount == 0){
						showAlert('cmplx_FATCA_selectedReason',alerts_String_Map['VAL269']);
						return false;
			}
			else if(rowCount == 1 && com.newgen.omniforms.formviewer.getNGItemText('cmplx_FATCA_selectedReason',0)==""){
						showAlert('cmplx_FATCA_selectedReason',alerts_String_Map['VAL269']);
						return false;
			}			
			/*else if(getNGValue('cmplx_FATCA_selectedreason')=='')
			{
						showAlert('cmplx_FATCA_selectedreason',alerts_String_Map['VAL269']);
						return false;
			}*/
	
	}		
		for(var i=0;i<gridValue.length;i++)
		{
			if(gridValue.indexOf(CustType)>-1)
			{
						showAlert('cmplx_FATCA_CustomerType','FATCA Already Added for Customer'+CustType);
						return false;
						break;
			}
		}
		
	return true;
	}
	function FATCA_Save_CheckOnDone(){
	var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails');
		if(n==0){
			showAlert('cmplx_FATCA_cmplx_GR_FatcaDetails','FATCA Grid cannot be empty');
						return false;	
		}
		
		
	return true;
	}
		
	 function ServiceRequest_Save_Check()
	 {
		//changes by saurabh on 13th nov 2017 for FSD 2.7 Drop 2 points.
		var reqProd = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
		var subProd = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
		var appType = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
		if(subProd=='IM' && appType=='TOPIM'){
			if(!isVisible('CC_Loan_Frame1')){
			showAlert('CC_Loan_Frame1','Service Request Details are mandatory');
						return false;
			}
		}
		if(reqProd=='Credit Card'){
			//changes by saurabh on 13 nov 2017 for drop2.
			/*if(getNGValue('transferMode')=='Cheque' || getNGValue('transferMode')=='C'){
				if(!checkMandatory(RLOS_TRANSFERMODE_SERVICEREQUEST))
					return false;
			}
			else if(!checkMandatory(RLOS_SERVICEREQUEST))
					return false;*/
			/*if(!checkMandatory(RLOS_BT)){
				return false;
			}*/ //arun (19/11/17) as this alert should not come for CC SAL case
			if(!checkMandatory(CC_Loan_DDS)){
				return false;
			}
			else if(!checkMandatory(CC_Loan_SI)){
				return false;
			}
			else if(!checkMandatory(CC_Loan_RVC)){
				return false;
			}
			
		}		
	   return true;		
	}

	//added By akshay on 15/9/17 for validating DDs on save 
	function DDS_Save_Check()
	{
		// Below code added by shweta
		if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)!='Personal Loan')
		{
		if(getNGValue('cmplx_Customer_NTB')=='false' || !getNGValue('cmplx_Customer_NTB'))//PCASP-3191
		{
			showAlert('','Service requests currently are available only for NTB customers in CAS');
			return false;
		}
		}//by jahnavi for JIRA 3158
		if(isFieldFilled('cmplx_CC_Loan_DDS_CardProduct')==false || getNGValue('cmplx_CC_Loan_DDS_CardProduct')=='--Select--'
			||  getNGValue('cmplx_CC_Loan_DDS_CardProduct')=='')//added by shweta for pcasp-1377
		{
			showAlert('cmplx_CC_Loan_DDS_CardProduct','Please add Card Product');
			return false;	
		}
		if(getNGValue('cmplx_CC_Loan_DDSFlag')==true){
			
			if(isFieldFilled('cmplx_CC_Loan_DDSMode')==false){
				showAlert('cmplx_CC_Loan_DDSMode',alerts_String_Map['VAL194']);
				return false;
			}
			else if(getNGValue('cmplx_CC_Loan_DDSMode')=='F' && getNGValue('cmplx_CC_Loan_DDSAmount')==''){ //changed by aman because code is diff 08/12
				showAlert('cmplx_CC_Loan_DDSAmount',alerts_String_Map['VAL191']);
				return false;
			}
			else if(getNGValue('cmplx_CC_Loan_DDSMode')=='P' && getNGValue('cmplx_CC_Loan_Percentage')==''){ //changed by aman because code is diff 08/12
				showAlert('cmplx_CC_Loan_Percentage',alerts_String_Map['VAL195']);
				return false;
			}
			else if(getNGValue('cmplx_CC_Loan_DDSIBanNo').length >0 && getNGValue('cmplx_CC_Loan_DDSIBanNo').length <23){
		         showAlert('cmplx_CC_Loan_DDSIBanNo', 'Length of IBAN should be 23');  
		         return false;
	         }
			else if(!checkMandatory(RLOS_SERVICEREQUEST_DDS))
				return false;
		}
		return true;
	}

	function SI_Save_Check()
	{
		//PCASP-3186
		if(getNGValue('cmplx_Customer_NTB')=='true' || getNGValue('cmplx_Customer_NTB'))
		{
			showAlert('','SI Details is required only for Existing Customer');
			return false;
		}
		// Below code added by shweta
		if(isFieldFilled('cmplx_CC_Loan_SI_CardProduct')==false || getNGValue('cmplx_CC_Loan_SI_CardProduct')=='--Select--'
			||  getNGValue('cmplx_CC_Loan_SI_CardProduct')=='')//added by shweta for pcasp-1377
		{
			showAlert('cmplx_CC_Loan_SI_CardProduct','Please add Card Product');
			return false;	
		}
		
		if(getNGValue('cmplx_CC_Loan_AccNo')==''){
			showAlert('cmplx_CC_Loan_AccNo',alerts_String_Map['VAL155']);
			return false;
		}
		
		else if(isFieldFilled('cmplx_CC_Loan_ModeOfSI')==false){
			showAlert('cmplx_CC_Loan_ModeOfSI',alerts_String_Map['VAL246']);
			return false;
		}
		else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='F')
		{
			if(getNGValue('cmplx_CC_Loan_FlatAMount')==''){
			showAlert('cmplx_CC_Loan_FlatAMount',alerts_String_Map['VAL274']);
			return false;
			}
		}
		
		 if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='P')
		{
			if(getNGValue('cmplx_CC_Loan_SI_Percentage')==''){
			showAlert('cmplx_CC_Loan_SI_Percentage',alerts_String_Map['VAL275']); //Changed by Aman 08/12
			return false;
			}
		}
		 /*if(isFieldFilled('cmplx_CC_Loan_SIOnDueDate')==false)
		{
			showAlert('cmplx_CC_Loan_SIOnDueDate',alerts_String_Map['VAL316']);
			return false;	
		}*/
		
		else if(isFieldFilled('cmplx_CC_Loan_StartMonth')==false)
		{
			showAlert('cmplx_CC_Loan_StartMonth',alerts_String_Map['VAL317']);
			return false;	
		}
		/*commented as no such field on form
		else if(getNGValue('cmplx_CC_Loan_SIOnDueDate')=='No')
		{
			if(getNGValue('cmplx_CC_Loan_SI_day')==''){
			showAlert('cmplx_CC_Loan_SI_day',alerts_String_Map['VAL273']);
			return false;
			}
		}*/
		
		/*PCASP-3186
		 if(isFieldFilled('cmplx_CC_Loan_HoldType')==false)
		{
			showAlert('cmplx_CC_Loan_HoldType',alerts_String_Map['VAL315']);
			return false;	
		}
		*/
		else if(getNGValue('cmplx_CC_Loan_SI_day')=='' && getNGValue('cmplx_CC_Loan_SIOnDueDate')!='Yes'){//PCASP-3186
			showAlert('cmplx_CC_Loan_SI_day','SI day is Mandatory!');
			return false;
		}
		else if(getNGValue('cmplx_CC_Loan_HoldType')=='T')
		{
			if(getNGValue('cmplx_CC_Loan_HoldFrom_Date')==''){
			showAlert('cmplx_CC_Loan_HoldFrom_Date',alerts_String_Map['VAL232']);
			return false;
			}
			
			else if(getNGValue('cmplx_CC_Loan_HOldTo_Date')==''){
			showAlert('cmplx_CC_Loan_HOldTo_Date',alerts_String_Map['VAL233']);
			return false;
			}
		}
		
		return true;
	}
	
	function SI_Save_Check_ProdSave()
	{
		
		if(isFieldFilled('cmplx_CC_Loan_ModeOfSI')==false){
			showAlert('cmplx_CC_Loan_ModeOfSI',alerts_String_Map['VAL246']);
			return false;
		}
		else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='F')
		{
			if(getNGValue('cmplx_CC_Loan_FlatAMount')==''){
			showAlert('cmplx_CC_Loan_FlatAMount',alerts_String_Map['VAL274']);
			return false;
			}
		}
		
		 if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='P')
		{
			if(getNGValue('cmplx_CC_Loan_SI_Percentage')==''){
			showAlert('cmplx_CC_Loan_SI_Percentage',alerts_String_Map['VAL275']); //Changed by Aman 08/12
			return false;
			}
		}
		 if(isFieldFilled('cmplx_CC_Loan_SIOnDueDate')==false)
		{
			showAlert('cmplx_CC_Loan_SIOnDueDate',alerts_String_Map['VAL316']);
			return false;	
		}
		
		else if(isFieldFilled('cmplx_CC_Loan_StartMonth')==false)
		{
			showAlert('cmplx_CC_Loan_StartMonth',alerts_String_Map['VAL317']);
			return false;	
		}
		/*
		else if(getNGValue('cmplx_CC_Loan_SIOnDueDate')=='No')
		{
			if(getNGValue('cmplx_CC_Loan_SI_day')==''){
			showAlert('cmplx_CC_Loan_SI_day',alerts_String_Map['VAL273']);
			return false;
			}
		}*/
		
		 if(isFieldFilled('cmplx_CC_Loan_HoldType')==false)
		{
			showAlert('cmplx_CC_Loan_HoldType',alerts_String_Map['VAL315']);
			return false;	
		}
		
		else if(getNGValue('cmplx_CC_Loan_HoldType')=='T')
		{
			if(getNGValue('cmplx_CC_Loan_HoldFrom_Date')==''){
			showAlert('cmplx_CC_Loan_HoldFrom_Date',alerts_String_Map['VAL232']);
			return false;
			}
			
			else if(getNGValue('cmplx_CC_Loan_HOldTo_Date')==''){
			showAlert('cmplx_CC_Loan_HOldTo_Date',alerts_String_Map['VAL233']);
			return false;
			}
		}
		
		return true;
	}
	
	function RVC_Save_Check()
	{
		// Below code added by shweta
		if(isFieldFilled('cmplx_CC_Loan_RVC_CardProduct')==false || getNGValue('cmplx_CC_Loan_RVC_CardProduct')=='--Select--'
			||  getNGValue('cmplx_CC_Loan_RVC_CardProduct')=='')//added by shweta for pcasp-1377
		{
			showAlert('cmplx_CC_Loan_RVC_CardProduct','Please add Card Product');
			return false;	
		}
		if(getNGValue('cmplx_CC_Loan_VPSFlag')==true)
		{
		
			if(isFieldFilled('cmplx_CC_Loan_VPSPAckage')==false){
			showAlert('cmplx_CC_Loan_VPSPAckage',alerts_String_Map['VAL294']);
			return false;
			}
			
			else if(isFieldFilled('cmplx_CC_Loan_VPSSourceCode')==false){
			showAlert('cmplx_CC_Loan_VPSSourceCode',alerts_String_Map['VAL297']);
			return false;
			}
			
			else if(getNGValue('cmplx_CC_Loan_VPSSaleMode')==''){
			showAlert('cmplx_CC_Loan_VPSSaleMode',alerts_String_Map['VAL296']);
			return false;
			}
		}
		return true;
	}
	
	function Decision_Save_Check()
	{
		if(getNGValue('cmplx_DecisionHistory_Decision')=='--Select--' || getNGValue('cmplx_DecisionHistory_Decision')==null){
			showAlert('cmplx_DecisionHistory_Decision',alerts_String_Map['VAL197']);		
			return false;
		}			
		else if(getNGValue('cmplx_DecisionHistory_Decision')=='Reject'){
			if(getNGValue('RejectRemarks')==''){
				showAlert('RejectRemarks',alerts_String_Map['VAL262']);
				return false;
			}	
			else 
				return true;
		}
		return true;
	}
	
	function checkMandatory_HomeNumber()
	{
		var n=getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		if(n>0){
			for(var i=0;i<n;i++)	
			{
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0)=='Home')
				{
					if(isFieldFilled('AlternateContactDetails_HomeCOuntryNo')==false){
						showAlert('AlternateContactDetails_HomeCOuntryNo',alerts_String_Map['VAL234']);
						return false;
					}
						
				}
				
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0)=='OFFICE')
				{
					if(isFieldFilled('AlternateContactDetails_OfficeNo')==false){
						showAlert('AlternateContactDetails_OfficeNo',alerts_String_Map['VAL248']);
						return false;
					}
						
				}
				
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0)=='RESIDENCE')
				{
					if(isFieldFilled('AlternateContactDetails_ResidenceNo')==false){
						showAlert('AlternateContactDetails_ResidenceNo',alerts_String_Map['VAL263']);
						return false;
					}
						
				}
			}	
		}
		return true;
	}
	
	function AuthSignatory_validate(operationName)
	{
		var n=getLVWRowCount('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails');
		if(operationName=='Add'){
			if(n>0){
				/*if(getLVWAT('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails',0,10)=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}
				
				else if(getNGValue('AuthorisedSignDetails_SoleEmployee')=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}*/
			}
		}
		else if(operationName=='Modify'){
			if(n>1){
				/*if(getLVWAT('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails',0,10)=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}
				
				else if(getNGValue('AuthorisedSignDetails_SoleEmployee')=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}*/
			}
		}	
		return true;		
	}	
	
	
	function validate_Company()
	{
		var n=	getLVWRowCount('cmplx_PartnerDetails_cmplx_partnerGrid');
			if(getLVWAT('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails',0,10)=='Yes' && n>0)
			{
				showAlert('',alerts_String_Map['VAL321']);
				return false;
			}	
		return true;
	}	
	
	function checkForNA_AddressGrid()
	{
		for(var i=0;i<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');i++){
			if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,6)=='NA' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,6)=='')//city
			{
				showAlert('city',alerts_String_Map['VAL346']);
				return false;
			}
			if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,7)=='NA' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,7)=='')//state
			{
				showAlert('state',alerts_String_Map['VAL345']);
				return false;
			}
			if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,8)=='NA' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,8)=='')//country
			{
				showAlert('country',alerts_String_Map['VAL347']);
				return false;
			}
			/*if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,9)=='' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,9)=='NA')//years in current address
			{
				showAlert('years','Please enter Years in Current Address');
				return false;
			}*/
		}
		return true;
	}	
	//below code added by nikhil for drop-4 delivery
	function EIDA_Visa_date()
	{
		var visa_issue = getNGValue("cmplx_Customer_VisaIssuedate");
		var id_issue = getNGValue("cmplx_Customer_IdIssueDate");
		if(visa_issue != '' && id_issue != '')
		{
			setNGValueCustom("cmplx_Customer_VisaIssuedate", visa_issue);
			setNGValueCustom("cmplx_Customer_IdIssueDate", id_issue);
		}
		else if(visa_issue != '' && id_issue == '')
		{
			setNGValueCustom("cmplx_Customer_VisaIssuedate", visa_issue);
			setNGValueCustom("cmplx_Customer_IdIssueDate", id_issue);
		}
		else if(visa_issue == '' && id_issue != '')
		{
			//for expact customer
			if(getNGValue("cmplx_Customer_Nationality")!='AE' && getNGValue("cmplx_Customer_ResidentNonResident")=='Y')
			{
				setNGValueCustom("cmplx_Customer_VisaIssuedate", id_issue);
			}
			else
			{
				setNGValueCustom("cmplx_Customer_VisaIssuedate", "");	
			}
			
			setNGValueCustom("cmplx_Customer_IdIssueDate", id_issue);
		}
	}
	//Added by prabhakar	
	function checkMandatory_AddressDetails_Save(){
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			var custTypePickList = document.getElementById("Customer_Type");//added by prabhakar drop-4 point-3
			var picklistValues=getPickListValues(custTypePickList);//added by prabhakar drop-4 point-3
			if(n==0){
				showAlert('cmplx_AddressDetails_cmplx_AddressGrid',alerts_String_Map['CC097']);	
				return false;
			}
			 else if(picklistValues.length>n)
			{
				showAlert('Customer_Type',alerts_String_Map['CC259']);	
					return false;
			}
			else
			{
				var gridValue=[];
				for(var i=0;i<n;i++)
				{
					gridValue.push(getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,13));
				}
				gridValue.sort();
				for(var i=0;i<picklistValues.length;i++)
				{
				if(!checkforPreferredAddress(picklistValues[i]))
				{
				return false;
				}
					if((gridValue.indexOf(picklistValues[i])==-1))
					{
						showAlert('Customer_Type',alerts_String_Map['CC260']+picklistValues[i]);
						return false;
						break;
					}
				}
			} 
			return true;
		}
		//Added by prabhakar
			function getPickListValues(pickListID)
				{	
					var picklistValues= [];
					for (var i = 1; i < pickListID.length; i++) 
					{
						picklistValues.push(pickListID.options[i].value)
					}
						picklistValues.sort();
						return picklistValues;
				}
				
function checkForApplicantTypeInGrids(operation)
{
	var appType_selectedRow;
	
	if(operation=='Supplement'){
		appType_selectedRow='S-'+getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid'),0)+' '+getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid'),2)+'-'+getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid'),3);
	}
	else{
		appType_selectedRow='G-'+getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',getNGListIndex('cmplx_GuarantorDetails_cmplx_GuarantorGrid'),7)+' '+getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',getNGListIndex('cmplx_GuarantorDetails_cmplx_GuarantorGrid'),9);
	}
	
	if(getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid')>0)
	{
		for(var i=0;i<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');i++)
		{
			if(Trim(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13))==appType_selectedRow)
			{
				showAlert('Customer_Type',alerts_String_Map['VAL359']);
				return false;
			}
		}
	}
	if(getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails')>0)
	{
		for(var i=0;i<getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails');i++)
		{
			if(Trim(getLVWAT('cmplx_FATCA_cmplx_GR_FatcaDetails',i,13))==appType_selectedRow)
			{
				showAlert('cmplx_FATCA_CustomerType',alerts_String_Map['VAL360']);
				return false;
			}
		}
	}	
	
	if(getLVWRowCount('cmplx_KYC_cmplx_KYCGrid')>0)
	{
		for(var i=0;i<getLVWRowCount('cmplx_KYC_cmplx_KYCGrid');i++)
		{
			if(Trim(getLVWAT('cmplx_KYC_cmplx_KYCGrid',i,3))==appType_selectedRow)
			{
				showAlert('KYC_CustomerType',alerts_String_Map['VAL361']);
				return false;
			}
		}
	}	
	
	if(getLVWRowCount('cmplx_OECD_cmplx_GR_OecdDetails')>0)
	{
		for(var i=0;i<getLVWRowCount('cmplx_OECD_cmplx_GR_OecdDetails');i++)
		{
			if(Trim(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',i,8))==appType_selectedRow)
			{
				showAlert('cmplx_FATCA_CustomerType',alerts_String_Map['VAL362']);
				return false;
			}
		}
	}
return true;	

}		


function checkForNA_Grids(gridName)
{
		for(var i=0;i<getLVWRowCount(gridName);i++){
		
		if(gridName=='cmplx_AddressDetails_cmplx_AddressGrid'){	
			if(getLVWAT(gridName,i,5)=='NA' || getLVWAT(gridName,i,5)=='')//city
			{
				showAlert('city',alerts_String_Map['VAL346']);
				return false;
			}
			if(getLVWAT(gridName,i,6)=='NA' || getLVWAT(gridName,i,6)=='')//state
			{
				showAlert('state',alerts_String_Map['VAL345']);
				return false;
			}
			if(getLVWAT(gridName,i,7)=='NA' || getLVWAT(gridName,i,7)=='')//country
			{
				showAlert('country',alerts_String_Map['VAL347']);
				return false;
			}
		/*	if(getLVWAT(gridName,i,9)=='' || getLVWAT(gridName,i,9)=='NA')//years in current address
			{
				showAlert('AddressDetails_years','Please enter Years in Current Address');
				return false;
			}*/
		}
		else  if(gridName=='cmplx_FATCA_cmplx_GR_FatcaDetails'){
			if(getLVWAT(gridName,i,1)=='NA')//us relation
			{
				showAlert('FATCA_USRelaton',alerts_String_Map['PL103']);
				return false;
			}
		}
		else  if(gridName=='cmplx_OECD_cmplx_GR_OecdDetails'){
			if(getLVWAT(gridName,i,1)=='NA')//crs flag reason
			{
				showAlert('OECD_CRSFlagReason',alerts_String_Map['PL408']);
				return false;
			}
			else if(getLVWAT(gridName,i,6)=='NA')//no tin reason
			{
				showAlert('OECD_noTinReason',alerts_String_Map['VAL092']);
				return false;
			}
		}
		else  if(gridName=='cmplx_KYC_cmplx_KYCGrid'){
			if(getLVWAT(gridName,i,1)=='NA')//kyc held
			{
				showAlert('KYC_Combo1',alerts_String_Map['PL111']);
				return false;
			}
			
			if(getLVWAT(gridName,i,2)=='NA')//pep
			{
				showAlert('KYC_Combo2',alerts_String_Map['PL112']);
				return false;
			}
		}		

	}	
		return true;
}
function checkMandatory_Frames(Frames_Names)
{
	var Frames = Frames_Names.split("#");
	if(Frames_Names!='')
	{
	for(var i=0;i<Frames.length;i++)
	{
		var Frame=Frames[i].split(":");
		if(isVisible(Frame[2])==false)
			{
			showAlert(Frame[0],'Please Visit '+Frame[1]+' First.');
			setNGFrameState('DecisionHistoryContainer',1);
			return false;			
			}
	}
	}
	return true;
}
//below code added by nikhil for incoming doc mandatory validation
function Check_Documents_Submit()
{
	var mndrty_doc= getNGValue('cmplx_IncomingDocNew_MandatoryDocument').split(',');
	
	for(var mndrty_count=0;mndrty_count<mndrty_doc.length;mndrty_count++)
	{
		var match=0;
		for(var docGrid_count=0;docGrid_count<getLVWRowCount('cmplx_IncomingDocNew_IncomingDocGrid');docGrid_count++)
		{
			//changes by saurabh for Defer Waiver functionality on 5th Feb 19.
			if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',docGrid_count,0)=='Deferred'){
			setNGValueCustom("is_deferral_approval_require","Y");
			}
			if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',docGrid_count,0)=='Waived'){
			setNGValueCustom("is_waiver_approval_require","Y");
			}
			if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',docGrid_count,0)==mndrty_doc[mndrty_count])
			{
				match++;
				break;
			}
		}
		if(match==0)
		{
		showAlert('cmplx_IncomingDocNew_MandatoryDocument','Please Add Mandatory Document : '+mndrty_doc[mndrty_count]);
		return false;
		}
	}
	return true;
}
function Check_Mandate_CustomerCheck()
{
	var Product_Grid='cmplx_Product_cmplx_ProductGrid';
	var Product_Row_count = getLVWRowCount('cmplx_Product_cmplx_ProductGrid');
	var EmploymentType = getNGValue('EmploymentType');
	var Id=getNGValue('cmplx_Customer_EmiratesID');
	var Dob=getNGValue('cmplx_Customer_DOb');
	var nep = getNGValue('cmplx_Customer_NEP');
	var cifcheckAvail= getNGValue('cmplx_Customer_card_id_available');
	var CIF=getNGValue('cmplx_Customer_CIFNO');
	if(Product_Row_count==0)
	{
		showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['CAS001']);
		return false;
	}
	else if(((Id=="")||(Id.length != 15) ||(Id.substr(0,3)!='784')) && (cifcheckAvail==false)&& (nep=='') && isFieldFilled('cmplx_Customer_marsoomID')==false){
		if(Id.substr(0,3)!='784'){
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		else{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		return false;
	}
	if(CIF !="" && CIF.length!=7){
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL105']);
			return false;
		}
	/*else if(getNGValue('cmplx_Customer_EmiratesID')=='' && getNGValue('cmplx_Customer_NEP')!='NEWC')
	{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL055']);
		return false;
	}*/
	else if( (getNGValue('cmplx_Customer_FIrstNAme')=='') &&(cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL061']);
		return false;
	}
	else if((getNGValue('cmplx_Customer_LAstNAme')=='') &&(cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL076']);
		return false;
	}
	else if((getNGValue('cmplx_Customer_Title')=='' || getNGValue('cmplx_Customer_Title')=='--Select--') && (cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_Title',alerts_String_Map['VAL284']);
		return false;
	}
	else if((getNGValue('cmplx_Customer_DOb')=='') && (cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL045']);
		return false;
	}
	else if((Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9)) && Id!='')
		{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL074']);
		return false;
		}
	else if((getNGValue('cmplx_Customer_Nationality')=='' || getNGValue('cmplx_Customer_Nationality')=='--Select--') && (cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL090']);
		return false;
	}
	else if((getNGValue('cmplx_Customer_MobNo')=='') && (cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_MobNo',alerts_String_Map['VAL086']);
		return false;
	}
	else if((getNGValue('cmplx_Customer_PAssportNo')=='') && (cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL098']);
		return false;
	}
	else if((getNGValue('cmplx_Customer_gender')=='' || getNGValue('cmplx_Customer_gender')=='--Select--') && (cifcheckAvail==false))
	{
		showAlert('cmplx_Customer_gender',alerts_String_Map['VAL064']);
		return false;
	}
	else if(getNGValue('cmplx_Customer_Email_ID')=='')
	{
		showAlert('cmplx_Customer_Email_ID',alerts_String_Map['CAS002']);
		return false;
	}
	else if(getNGValue('cmplx_Customer_Basic_Sal')=='' && EmploymentType!='Self Employed')
	{
		showAlert('cmplx_Customer_Basic_Sal',alerts_String_Map['CAS003']);
		return false;
	}
	else if(getNGValue('cmplx_Customer_Designation')=='')
	{
		showAlert('cmplx_Customer_Designation',alerts_String_Map['VAL048']);
		return false;
	}
	if ((getNGValue('cmplx_Customer_NEP')!="")&& ((getNGValue('cmplx_Customer_EIDARegNo')=='')||(getNGValue('cmplx_Customer_EIDARegNo')==null))){
		showAlert('cmplx_Customer_EIDARegNo',alerts_String_Map['VAL206']);
		return false;
	}
	if(getNGValue('cmplx_Customer_Aecb_Consent_Recieved')==false)
	{
		showAlert('cmplx_Customer_Aecb_Consent_Recieved',alerts_String_Map['VAL157']);
		return false;
	}
	var age=calcAge(getNGValue('cmplx_Customer_DOb'),'');
			//setNGValueCustom('cmplx_Customer_age',age);
			//change by saurabh for CR on 11th June 19.
			//change by sagarika for CR 15,(1284)
	var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);//by shweta
	if(prod!='Personal Loan'){//PCASI-1099
			if(age<18.6 && getNGValue("cmplx_Customer_Nationality")=='AE' )
			{
		       showAlert('','Minor Customer not Applicable for ' +  prod);
				//setNGValueCustom(pId,'');
				return false;
			}
			else if(age<21 && getNGValue("cmplx_Customer_Nationality")!='AE')
			{
				showAlert('','Minor Customer not Applicable for ' +  prod);
				//setNGValueCustom(pId,'');
				return false;
			}//end by sagarika
	} else {//PCASI-1099
		 if(age<21 && getNGValue("cmplx_Customer_Nationality")!='AE')
			{
				showAlert('','Expat Minor Customer not Applicable for ' +  prod);
				return false;
			}
		 else if(age<18.06 && getNGValue("cmplx_Customer_Nationality")!='AE'){
				showAlert(pId,alerts_String_Map['PL428']);
				setNGValueCustom(pId,'');
				return false;
		} else {
			 if(isCustomerMinor()){
				 minorCustomerFieldVisibility("true");
			} else {
				 minorCustomerFieldVisibility("false");
			}
		}
		 
	}
	
		return true;
	
	
}


function checkforPreferredAddress(Applicant)
{
	var GridName='cmplx_AddressDetails_cmplx_AddressGrid';
	var Row_count=getLVWRowCount(GridName);
	var preferred=0;
	for(var i=0;i<Row_count;i++)
	{
		if(getLVWAT(GridName,i,13)==Applicant && getLVWAT(GridName,i,10)=='true')
		{
			preferred++;
		}

	}
	if(preferred==0)
	{
		showAlert('addtype','Preferred Address Not present for '+Applicant);
		return false;
	}
	if(preferred>1)
	{
	showAlert('addtype','Multiple Preferred Address present for '+Applicant);
		return false;
	}
	return true;
}
function CheckCompanyMandatory()
{ 
//Added by Rajan to disable save for PA Self Employed  & Salaried PCASP-597
if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA' && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed'||getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Salaried') && getNGValue('cmplx_Customer_VIPFlag')==true)
	 {
		 return true;
	 }
var Grid_name="cmplx_CompanyDetails_cmplx_CompanyGrid";
var Company_row_count = getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
var count=0;
for(var i=0;i<Company_row_count;i++)
{
	if(getLVWAT(Grid_name,i,1)=='Business')
	{
	count++;
	break;
	}
}
if(count==0)
{
showAlert('CompanyDetails_Add','Please Fill Company Details');
return false;
}
return true;
}

function isCustomerMinor() {
	if(com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")>=-1)
	{
		var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);//by shweta
		var age=getNGValue("cmplx_Customer_age");
		if(prod=='Personal Loan' && age!=null){
			if(age>=21){
				return false;
			}
			else if (age >=18.06 && age <=21.06 &&  getNGValue('cmplx_Customer_Nationality')!=null && getNGValue('cmplx_Customer_Nationality')=='AE') {
				return true;
			}
			else {
				return false;
			}
		}
	}
}

function minorCustomerFieldVisibility(isMinor){
	if(isMinor=="true"){
			setVisible("Customer_Label40",true);
			setVisible("Customer_Label42",true);
			setVisible("cmplx_Customer_guarname",true);
			setVisible("cmplx_Customer_guarcif",true);
			setLocked("cmplx_Customer_guarname",false);
			setLocked("cmplx_Customer_guarcif",false);
			setLocked("eidExpiry",false);
			setLocked("idIssueDate",false);
			setLocked("visaExpiry",false);
			setLocked("GuarantorDetails_VisaISsueDate",false);
			setLocked("dob",false);
			setLocked("passExpiry",false);
			setLocked("GuarantorDetails_PassportIssueDate",false);
			setVisible("GuarantorDetails", true);
			setTop("GuarantorDetails",parseInt(getTop('CustomerDetails'))+parseInt(getHeight("CustomerDetails"))+15+"px");
			setTop("Incomedetails",parseInt(getTop('GuarantorDetails'))+parseInt(getHeight("GuarantorDetails"))+15+"px");
		
	}else {
		setVisible("GuarantorDetails", false);
			setVisible("Customer_Label40",false);
			setVisible("Customer_Label42",false);
			setVisible("cmplx_Customer_guarname",false);
			setVisible("cmplx_Customer_guarcif",false);
	}
	
}
