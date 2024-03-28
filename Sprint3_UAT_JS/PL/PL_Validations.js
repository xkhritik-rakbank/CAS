var country_GCC= ["AE","SA","KW","QA","BH","OM"];
var checkTO="false";
var Fpu_Grid_save_Button=false;
function validate()
{
	//return true;
	if(getNGValue('cmplx_Decision_Decision')=='Reject'){
		if(Decision_Save_Check())
			return true;
		else 
			return false;
	}

	
	if(!checkMandatory(PL_CUSTOMER))
		return false;
	if(!checkMandatory(PL_PRODUCT))
		return false;
	if(!checkMandatory(PL_GUARANTOR))
		return false;
	if(!checkMandatory(PL_INCOME_SALARIED))
		return false;	
	if(!checkMandatory(PL_INCOME_SELFEMPLOYED))
		return false;
	if(!checkMandatory(PL_EMPLOYMENT))
		return false;
	if(!checkMandatory(PL_NEP_EMPLOYMENT))
		return false;
	if(!checkMandatory(PL_Freezone_EMPLOYMENT))
		return false;
	if(!checkMandatory(PL_Tenancy_EMPLOYMENT))
		return false;
	if(!checkMandatory(PL_EMpId_EMPLOYMENT))
		return false;
	if(!checkMandatory(PL_Misc))
		return false;
	if(!checkMandatory(PL_LIABILITY))
		return false;
	if(!checkMandatory(PL_ELIGIBILITY))
		return false;
	if(!checkMandatory(PL_LoanDetails))
		return false;
	if(!checkMandatory(PL_ADDRESS))
		return false;
	if(!checkMandatory(PL_CONTACTDETAILS))
		return false;
	if(!checkMandatory(PL_CARDDETAILS))
		return false;
	if(!checkMandatory(PL_SUPPLEMENTARYCARDDETAILS))
		return false;
	if(!checkMandatory(PL_FATCA))
		return false;
	if(!checkMandatory(PL_KYC))
		return false;
	if(!checkMandatory(PL_OECD))
		return false;
 
	// ++ below code already present - 09-10-2017	
	if(!checkMandatory(PL_SERVICEREQUEST_BTC))
		return false;
	if(!checkMandatory(PL_SERVICEREQUEST_DDS))
		return false;
	if(!checkMandatory(PL_SERVICEREQUEST_SI))
		return false;
	if(!checkMandatory(PL_SERVICEREQUEST_RVC))
		return false;
	// ++ above code already present - 09-10-2017
	
	if(!checkMandatory(PL_DECISION))
		return false;
	return true;	
}	

function ReferenceDetails_Save_Check()
	{
		if(!checkMandatory(PL_REFERENCE))
			return false;
		return true;	
	}	

function setALOCListed(){
	var NewEmployer=getNGValue('cmplx_EmploymentDetails_Others');
	var IncInCC=getNGValue('cmplx_EmploymentDetails_IncInCC');
	var INcInPL=getNGValue('cmplx_EmploymentDetails_IncInPL');
	var reqProd=getNGValue('PrimaryProduct');
	
	if(reqProd=='Personal Loan' && NewEmployer==true && (IncInCC==false || INcInPL==false))
	{
		setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
		setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
		setLocked("cmplx_EmploymentDetails_EmpStatusCC", true);
		setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);

	}
	
	else if(reqProd=='Credit Card' && NewEmployer==true && (IncInCC==false || INcInPL==false))
	{
		setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
		setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
		setNGValue("cmplx_EmploymentDetails_CurrEmployer", "CN");
		setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
		setLocked("cmplx_EmploymentDetails_CurrEmployer", true);
		setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);
		//document.getElementById('cmplx_EmploymentDetails_EmpStatusCC').remove(7);
		return true;
	}
	
	else{
		if(NewEmployer==true){
			setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "");
			setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "");
			setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
			setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);
			setLocked("cmplx_EmploymentDetails_CurrEmployer", false);
		}
		return true;
	}
}
 
function Customer_Save_Check1(){//by shweta
     //updated by Tarang on 01/19/2018 against drop 4 point 25   
	var activityname=window.parent.stractivityName;
	var Id=getNGValue('cmplx_Customer_EmiratesID');
	var Dob=getNGValue('cmplx_Customer_DOb');
	var nep = getNGValue('cmplx_Customer_NEP');

	if(expiraygreatissue('cmplx_Customer_VisaIssueDate','cmplx_Customer_VIsaExpiry')==false)
	{
	return false;
	}
	// hritik PCASI 3548 - If expiray date of visa/Emiratesid is greater than issued date by 10 years.
		var from = getNGValue('cmplx_Customer_IdIssueDate')
		var parts = from.split('/');
		var fromDate=new Date(parts[2],parts[1]-1,parts[0]);
		var fromDate_year=fromDate.getFullYear();
		var fromDate_month=fromDate.getMonth();
		var fromDate_day=fromDate.getDate();
		var fromDate = new Date(fromDate_year+10,fromDate_month,fromDate_day);
		
		var to = getNGValue('cmplx_Customer_EmirateIDExpiry')
		var parts = to.split('/');
		var toDate=new Date(parts[2],parts[1]-1,parts[0]);
		var toDate_year=toDate.getFullYear();
		var toDate_month=toDate.getMonth();
		var toDate_day=toDate.getDate();
		
		if(fromDate<toDate)
		{
			showAlert('cmplx_Customer_EmirateIDExpiry','Expiry date cannot be greater than 10 years from issued date!!');
			return false;
		}
		//below code added for CAS Simplification
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(((Id=="")||(Id.length != 15) ||(Id.substr(0,3)!='784'))&&(nep=='') && isFieldFilled('cmplx_Customer_marsoomID')==false && activityName=='DDVT_maker' ){
		if(Id.substr(0,3)!='784'){
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		else{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
		}
		return false;
	}
	if((Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9)) && Id!='' && activityName=='DDVT_maker')
	{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL074']);
		return false;
	} 
    if((getNGValue('cmplx_Customer_EmiratesID')=='' || (getNGValue('cmplx_Customer_EmiratesID').length!=15) || (getNGValue('cmplx_Customer_EmiratesID').substr(0,3)!='784'))&&(getNGValue('cmplx_Customer_NEP')== "")){
			if(getNGValue('cmplx_Customer_CardNotAvailable') != false && getNGValue('cmplx_Customer_marsoomID')=='' && getNGSelectedItemText('cmplx_Customer_ResidentNonResident')=='RESIDENT')
			{
				showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL211']);  
					return false;
			}

	}
    
   
    if(isFieldFilled('cmplx_Customer_NEP')==false )
	{
	    setLockedCustom('cmplx_Customer_EmiratesID',false);
		//alert ('abcd');
		
	}
     
				
	/*if ((getNGValue('cmplx_Customer_NEP')!="")&& ((getNGValue('cmplx_Customer_EIDARegNo')=='')||(getNGValue('cmplx_Customer_EIDARegNo')==null))){
		showAlert('cmplx_Customer_EIDARegNo',alerts_String_Map['VAL206']);
	}*/	//Arun (06/12/17) commented as this field is not mandatory
     if(getNGValue('cmplx_Customer_FIrstNAme')=='' || getNGValue('cmplx_Customer_FIrstNAme') == null){
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL224']);  return false;      }
				
     if(getNGValue('cmplx_Customer_LAstNAme')=='' || getNGValue('cmplx_Customer_LAstNAme') == null){
                showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL239']); return false;      }
				
     if(getNGValue('cmplx_Customer_DOb')=='' || getNGValue('cmplx_Customer_DOb') == null){
                showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL045']); return false;      }
				
     if(isFieldFilled('cmplx_Customer_Nationality')==false){
                showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL090']); return false;      }
				
     if(getNGValue('cmplx_Customer_MobNo')=='' || getNGValue('cmplx_Customer_MobNo') == null){
                showAlert('cmplx_Customer_MobNo',alerts_String_Map['VAL243']);    return false;      }
     if(getNGValue('cmplx_Customer_PAssportNo')=='' && getNGValue('cmplx_Customer_Nationality')!='AE'){
                showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL249']); return false;      }
	
				
     if(((getNGValue('cmplx_Customer_CardNotAvailable')==false) &&(getNGValue('cmplx_Customer_NTB')== false))&&((getNGValue('cmplx_Customer_CIFNO')==''))){             
				showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL033']);
				return false;
	}
				
     if(isFieldFilled('cmplx_Customer_Title')==false){
                showAlert('cmplx_Customer_Title',alerts_String_Map['VAL284']); return false;      }
				
     if(isFieldFilled('cmplx_Customer_gender')==false){
                showAlert('cmplx_Customer_gender','gender cannot be blank'); return false;      }
				
	if(getNGValue('cmplx_Customer_Constitution')==''){
	showAlert('cmplx_Customer_Constitution','Constitution cannot be blank'); return false;  }    
				//for constitutiom		
     if(getNGValue('cmplx_Customer_age')==''){
                showAlert('cmplx_Customer_age',alerts_String_Map['VAL003']); return false;      }
				
     if((getNGValue('cmplx_Customer_IdIssueDate')=='')&&(isFieldFilled('cmplx_Customer_NEP') == false) &&  isFieldFilled('cmplx_Customer_marsoomID')==false){
            showAlert('cmplx_Customer_IdIssueDate',alerts_String_Map['VAL235']); 
			return false;      			
	}
     if((getNGValue('cmplx_Customer_EmirateIDExpiry')=='')&&(isFieldFilled('cmplx_Customer_NEP') == false ) &&  isFieldFilled('cmplx_Customer_marsoomID')==false){
            showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['VAL208']);    return false;      }
	
	if(getNGValue('cmplx_Customer_NEP')!='EXEID'){
    if(validatePastDate('cmplx_Customer_EmirateIDExpiry',"Emirate Card has expired!!")==false)
			return false;
	}
	// commented  by akshay on 20/11/2017 as below condition handles both these cases 
    /*else if((getNGValue('cmplx_Customer_VisaNo')=='') && getNGValue('cmplx_Customer_GCCNational')=='N')
                showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);
			
	else if(getNGValue('cmplx_Customer_VisaNo')=='' && getNGValue('cmplx_Customer_Nationality')=='AE')
		showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);*/
	if(getNGValue('cmplx_Customer_VisaNo')=='' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
		if(getNGValue('cmplx_Customer_GCCNational')=='Y'|| getNGValue('cmplx_Customer_GCCNational')=='N'  ){
			var flag=0;
			for(var i=0; i<country_GCC.length; i++)
			{
				if(country_GCC[i]==getNGValue('cmplx_Customer_Nationality'))
				{
					flag=1;
					break;
				}
			}
			if(flag==0)
			{
				showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);
				return false;
			}
			
		}
	}
	else if(getNGValue('cmplx_Customer_VisaNo')=='') {	
		showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);
		return false;
	}	
	
	
    if((getNGValue('cmplx_Customer_VisaIssuedate')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_VisaIssuedate',alerts_String_Map['VAL355']);return false;      }
			
	if((getNGValue('cmplx_Customer_VIsaExpiry')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_VIsaExpiry',alerts_String_Map['VAL292']);return false;      }
			
// added by yash on 19/7/2017 for removing the validation of emirates of visa proc 812
    if (isFieldFilled('cmplx_Customer_EMirateOfVisa') == false && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N')
    {
        showAlert('cmplx_Customer_EMirateOfVisa', alerts_String_Map['VAL210']);
        return false;
    }
// ended 
	 if(isFieldFilled('cmplx_Customer_ResidentNonResident')==false){
                showAlert('cmplx_Customer_ResidentNonResident',alerts_String_Map['VAL264']);  return false;      }      
	 if(getNGValue('cmplx_Customer_PassportIssueDate')==''){
                showAlert('cmplx_Customer_PassportIssueDate',alerts_String_Map['VAL354']); return false;   
		}
				
    if(getNGValue('cmplx_Customer_PassPortExpiry')==''){
                showAlert('cmplx_Customer_PassPortExpiry',alerts_String_Map['VAL250']); return false;      }
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
	
     if((com.newgen.omniforms.util.getComponentValue(document.getElementById('cmplx_Customer_SecNAtionApplicable'))=='Yes')&&(isFieldFilled('cmplx_Customer_SecNationality')==false)){
                showAlert('cmplx_Customer_SecNationality',alerts_String_Map['VAL267']);return false;      }
	 if((com.newgen.omniforms.util.getComponentValue(document.getElementById('cmplx_Customer_Third_Nationaity_Applicable'))=='Yes')&&(isFieldFilled('cmplx_Customer_Third_Nationaity')==false)){
                showAlert('cmplx_Customer_Third_Nationaity','Third Nationality cannot be blank');return false;      }//By Alok for Third Nationality Field
     if(isFieldFilled('cmplx_Customer_GCCNational')==false){
                showAlert('cmplx_Customer_GCCNational',alerts_String_Map['VAL229']);  return false;      }
				
     if(isFieldFilled('cmplx_Customer_CustomerCategory')==false){
                showAlert('cmplx_Customer_CustomerCategory',alerts_String_Map['VAL182']);return false;      }
     if(isFieldFilled('cmplx_Customer_yearsInUAE')==false && getNGValue('cmplx_Customer_GCCNational')!='Y' &&(getNGValue('cmplx_Customer_Nationality')!='AE') && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_yearsInUAE',alerts_String_Map['VAL254']);return false;      }
     if(isFieldFilled('cmplx_Customer_MAritalStatus')==false){
                showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['VAL081']);return false;      }
	 if(isFieldFilled('cmplx_Customer_MotherName')==false){
                showAlert('cmplx_Customer_MotherName',alerts_String_Map['VAL247']); return false;      }       
				
     if(isFieldFilled('cmplx_Customer_EmirateOfResidence')==false){
                showAlert('cmplx_Customer_EmirateOfResidence',alerts_String_Map['VAL209']);return false;      }
     if(isFieldFilled('cmplx_Customer_COuntryOFResidence')==false){
                showAlert('cmplx_Customer_COuntryOFResidence',alerts_String_Map['VAL177']);return false;      }
     if(isVisible('cmplx_Customer_bankwithus')==true && isLocked('cmplx_Customer_bankwithus')==false && isFieldFilled('cmplx_Customer_bankwithus')==false){
         showAlert('cmplx_Customer_bankwithus','Please Select Banking with Us');return false;      }
	if(isCustomerMinor()){
		 if(getNGValue('cmplx_Customer_guarname')==''){	
			showAlert('cmplx_Customer_guarname',alerts_String_Map['PL382']);
			return false;
		}	
		 if(getNGValue('cmplx_Customer_guarcif')==''){	
				showAlert('cmplx_Customer_guarcif',alerts_String_Map['PL390']);
				return false;
		}	
	}
	//PCASI - 2694
	if((getNGValue('cmplx_Customer_RM_TL_NAME') == '') && (activityName == 'DDVT_maker')){
				showAlert('cmplx_Customer_RM_TL_NAME',"RM TL Name cannot be blank.");
				return false;
	}
		return true;
}		

//added by prabhakar


function KYC_add_Check(){
	
/*			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			//var custTypePickList = document.getElementById("KYC_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			var CustType=getNGValue("KYC_CustomerType");
			var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,3));
			}
*/			
	 if(getNGValue('KYC_CustomerType')==""||getNGValue('KYC_CustomerType')=="--Select--")
		{
			showAlert('KYC_CustomerType','Applicant type can not be blank');
			return false;
		}
	else if(getNGValue('KYC_Combo1')==""||getNGValue('KYC_Combo1')=="--Select--"){
		console.log(getNGValue('cmplx_KYC_KYC_Held')+"INside KYC HELD");
		showAlert('cmplx_KYC_KYC_Held',alerts_String_Map['CC110']);
		return false;
		}
	else if(getNGValue('KYC_Combo1')=='Y' && getNGValue('KYC_DatePicker1')=='')
		{
			showAlert('','KYC Review Date cannot be blank for any row where kYC held is Yes');
			return false;
		}
	else if((getNGValue('KYC_Combo2')==""||getNGValue('KYC_Combo2')=="--Select--") && getNGValue('KYC_Combo1')=='Y'){
		showAlert('cmplx_KYC_PEP',alerts_String_Map['CC111']);
		return false;
		}
			return true;
}
function product_Save_Check()
{
	if(com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")==0)
			showAlert('Product_type',alerts_String_Map['PL029']);
	else
			return true;
	return false;
}
	
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
	var EmpType=getNGValue('EmploymentType');
		if(EmpType=='Salaried'){
			if(!checkMandatory(PL_INCOME_SALARIED)){
				return false;
			}
				
			if(getNGValue('cmplx_IncomeDetails_grossSal')=="")
				{
					showAlert("cmplx_IncomeDetails_grossSal","gross Salary is mandatory");
					return false;
				}
			if(getNGValue('cmplx_IncomeDetails_totSal')=="")
			{
				showAlert("cmplx_IncomeDetails_totSal","Total Salary is mandatory");
				return false;
			}
			if(getNGValue('cmplx_IncomeDetails_Basic')=="")
			{
				showAlert("cmplx_IncomeDetails_totSal","Basic Salary is mandatory");
				return false;
			}
			////Done by aman for JIRA 020818
			if(getNGValue('cmplx_IncomeDetails_AvgNetSal')=="")
			{
				showAlert("cmplx_IncomeDetails_AvgNetSal","Net Salary is mandatory");
				return false;
			}
			//Done by aman for JIRA 020818
			//below code added by nikhil PCSP-89
			if(getNGValue('cmplx_IncomeDetails_netSal1')=="")
			{
				showAlert('cmplx_IncomeDetails_netSal1',alerts_String_Map['VAL373']);
				return false;
			}
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
			//done by sagarika for PCSP-209
			/*if(getNGValue('cmplx_IncomeDetails_SalaryXferToBank')=="" || getNGValue('cmplx_IncomeDetails_SalaryXferToBank')=="--Select--")
			{
				showAlert('cmplx_IncomeDetails_SalaryXferToBank','Salary Transfer To Bank Is Mandatory!!');
				return false;
			}*/
			
			else if(getNGValue('cmplx_IncomeDetails_Is_Tenancy_contract')=='--Select--' && getNGValue('cmplx_EmploymentDetails_CurrEmployer')!='CA' && getNGValue('cmplx_IncomeDetails_Accomodation')=='Yes'  && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan'){
				showAlert('cmplx_IncomeDetails_Is_Tenancy_contract',alerts_String_Map['VAL335']);
				return false;
			}	
				
		}	
		return true;			
}

function IMD_Save_Check()
	{
		// ++ below code already present - 09-10-2017
		
		
			if(activityName=='Disbursal_Maker')
			{
			if(getNGValue('cmplx_LoanDetails_name')=='' || getNGValue('cmplx_LoanDetails_name')=='--Select--')
			{
				showAlert('cmplx_LoanDetails_name',alerts_String_Map['PL395']);
				return false;
			}		
			if(getNGValue('cmplx_LoanDetails_paidon')=='' || getNGValue('cmplx_LoanDetails_paidon')=='--Select--')
			{
				showAlert('cmplx_LoanDetails_paidon',alerts_String_Map['PL396']);
				return false;
			}
			if(getNGValue('cmplx_LoanDetails_trandate')=='' || getNGValue('cmplx_LoanDetails_trandate')=='--Select--')
			{
				showAlert('cmplx_LoanDetails_trandate',alerts_String_Map['PL397']);
				return false;
			}
			if((getNGValue('cmplx_LoanDetails_paymode')=='' || getNGValue('cmplx_LoanDetails_paymode')=='--Select--'))
			{
				showAlert('cmplx_LoanDetails_paymode',alerts_String_Map['PL398']);
				return false;
			}
			if(getNGValue('cmplx_LoanDetails_status')=='' || getNGValue('cmplx_LoanDetails_status')=='--Select--')
			{
				showAlert('cmplx_LoanDetails_status',alerts_String_Map['PL399']);
				return false;
			}		
			if(getNGValue('cmplx_LoanDetails_amt')=='' || getNGValue('cmplx_LoanDetails_amt')=='--Select--')
			{
				showAlert('cmplx_LoanDetails_amt',alerts_String_Map['PL400']);
				return false;
			}
			
			if(getNGValue('cmplx_LoanDetails_paymode')=='C')
			{
			return checkMandatory('cmplx_LoanDetails_chqid#Cheque ID,cmplx_LoanDetails_chqno#CR/Cheque/DD no,cmplx_LoanDetails_chqdat#Cheque Date,cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_reason#Reason,cmplx_LoanDetails_micr#MICR');
			}
			if(getNGValue('cmplx_LoanDetails_paymode')=='T')
			{
			return checkMandatory('cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_bankdeal#Dealing Bank');
			}			
			if(getNGValue('cmplx_LoanDetails_currency')=='' || getNGValue('cmplx_LoanDetails_currency')=='--Select--')
			{
				showAlert('cmplx_LoanDetails_currency',alerts_String_Map['PL401']);
				return false;
			}
			if((getNGValue('cmplx_LoanDetails_favourof')=='' || getNGValue('cmplx_LoanDetails_favourof')=='--Select--') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4).indexOf('RESC')>-1)
			{
				showAlert('cmplx_LoanDetails_favourof',alerts_String_Map['PL402']);
				return false;
			}	
			if((getNGValue('cmplx_LoanDetails_drawnon')=='' || getNGValue('cmplx_LoanDetails_drawnon')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_drawnon',alerts_String_Map['PL403']);
				return false;
			}		
			if((getNGValue('cmplx_LoanDetails_reason')=='' || getNGValue('cmplx_LoanDetails_reason')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_reason',alerts_String_Map['PL404']);
				return false;
			}
			if((getNGValue('cmplx_LoanDetails_micr')=='' || getNGValue('cmplx_LoanDetails_micr')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_micr',alerts_String_Map['PL405']);
				return false;
			}
			//for proc-10047
			if((getNGValue('cmplx_LoanDetails_chqno')=='' || getNGValue('cmplx_LoanDetails_chqno')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_chqno','CR/Cheque/DD No is mandatory');
				return false;
			}
			if((getNGValue('cmplx_LoanDetails_chqdat')=='' || getNGValue('cmplx_LoanDetails_chqdat')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_chqdat','Cheque Date is Mandatory');
				return false;
			}
			/*if((getNGValue('cmplx_LoanDetails_city')=='' || getNGValue('cmplx_LoanDetails_city')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_city','City is Mandatory');
				return false;
			}*/
			if((getNGValue('cmplx_LoanDetails_bankdeal')=='' || getNGValue('cmplx_LoanDetails_bankdeal')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_bankdeal','Dealing Bank is Mandatory');
				return false;
			}
			}
			return true;
						
	}
	function Employment_Save_Check()
	{
		// ++ below code already present - 09-10-2017
		//code added by isha for mandatory fields in employment tab
		var activityname=window.parent.stractivityName;
		if(getNGValue('cmplx_Customer_VIPFlag')==false)
		{
			if (activityName == 'CAD_Analyst1')
			{
				if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='' || getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_ApplicationCateg',alerts_String_Map['PL039']);
					return false;
				}		
				if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='' || getNGValue('cmplx_EmploymentDetails_targetSegCode')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_targetSegCode',alerts_String_Map['PL040']);
					return false;
				}
				
				if(getNGValue('cmplx_EmploymentDetails_ClassificationCode')=='' || getNGValue('cmplx_EmploymentDetails_ClassificationCode')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_ClassificationCode',alerts_String_Map['PL041']);
					return false;
				}
				/*if(getNGValue('cmplx_EmploymentDetails_marketcode')=='' || getNGValue('cmplx_EmploymentDetails_marketcode')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_marketcode',alerts_String_Map['PL129']);
					return false;
				}*/
				if((getNGValue('cmplx_EmploymentDetails_channelcode')=='' || getNGValue('cmplx_EmploymentDetails_channelcode')=='--Select--'))
				{
					showAlert('cmplx_EmploymentDetails_channelcode',alerts_String_Map['PL042']);
					return false;
				}
				if(getNGValue('cmplx_Customer_NEP')!='')
				{
					if(getNGValue('cmplx_EmploymentDetails_NepType')=='' || getNGValue('cmplx_EmploymentDetails_NepType')=='--Select--')
					{
						showAlert('cmplx_EmploymentDetails_NepType',alerts_String_Map['PL047']);
						return false;
					}
				}
				if((getNGValue('cmplx_EmploymentDetails_collectioncode')=='' || getNGValue('cmplx_EmploymentDetails_collectioncode')=='--Select--') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4).indexOf('RES')>-1)
				{
					showAlert('cmplx_EmploymentDetails_collectioncode',alerts_String_Map['PL043']);
					return false;
				}
				if(getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='' || (getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='--Select--' || getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='Select')){
					showAlert('cmplx_EmploymentDetails_HeadOfficeEmirate',alerts_String_Map['PL062']);
				    return false;
				}
				/*if(getNGValue('cmplx_EmploymentDetails_PromotionCode')=='' || getNGValue('cmplx_EmploymentDetails_PromotionCode')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_PromotionCode',alerts_String_Map['PL044']);
					return false;
				}*/
				if(getNGValue('cmplx_EmploymentDetails_EMpCode')=='' || getNGValue('cmplx_EmploymentDetails_EMpCode')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_EMpCode',alerts_String_Map['VAL332']);
					return false;
				}
				if(getNGValue('cmplx_Customer_Nationality')!='AE'  && (getNGValue('cmplx_EmploymentDetails_VisaSponser')=='' || getNGValue('cmplx_EmploymentDetails_VisaSponser')==' '))
				{
					showAlert('cmplx_EmploymentDetails_VisaSponser',alerts_String_Map['VAL291']);
					return false;
				}
				 if(getNGValue('cmplx_EmploymentDetails_DOJ')=='' && getNGValue('EmploymentType').indexOf("Pensioner")==-1)
				{
					showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['PL411']);
					return false;	
				}	
				
					 if(getNGValue('cmplx_EmploymentDetails_Field_visit_done')=='' || getNGValue('cmplx_EmploymentDetails_Field_visit_done')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_Field_visit_done',alerts_String_Map['PL412']);
					return false;	
				}
				
				if(getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='' || getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='Select' || getNGValue('cmplx_EmploymentDetails_JobConfirmed')=='--Select--')
				{
					showAlert('cmplx_EmploymentDetails_JobConfirmed','Fill confirmed in Job!!');
					return false;	
				} // pcasi 3609 hritik 7.7.21
				
				if(getNGValue('cmplx_EmploymentDetails_DOJPrev')!=null && getNGValue('cmplx_EmploymentDetails_DOJPrev')!=''){
					if(getNGValue('cmplx_EmploymentDetails_LOSPrevious')=='' && getNGValue('cmplx_EmploymentDetails_LOSPrevious')=='00.00') {
						showAlert('cmplx_EmploymentDetails_LOSPrevious',alerts_String_Map['PL045']);
						return false;
					}
				}
				if(getNGValue('cmplx_EmploymentDetails_LOSPrevious')!=null && getNGValue('cmplx_EmploymentDetails_LOSPrevious')!='00.00'){
					if(getNGValue('cmplx_EmploymentDetails_DOJPrev')=='') {
						showAlert('cmplx_EmploymentDetails_DOJPrev','Please choose a value from date of Living drop down!');
						return false;
					}
				}
	
				if(getNGValue('cmplx_EmploymentDetails_IncInCC')==true || getNGValue('cmplx_EmploymentDetails_IncInPL')==true)
				{
					if(checkMandatory(PL_EMPLOYMENT_CA_PLCC))
					{	
						if(getNGValue('cmplx_EmploymentDetails_NepType'=='NTJ'))
						{
							if(getNGValue('cmplx_EmploymentDetails_LOSPrevious')=='')
							{
								showAlert('cmplx_EmploymentDetails_LOSPrevious',alerts_String_Map['PL045']);
								return false;
	
							}
						}
						
					}
					else{
						return false;
					}
				}
				if(getNGValue('cmplx_EmploymentDetails_IncInCC')==false || getNGValue('cmplx_EmploymentDetails_IncInPL')==false)
				{
					if(checkMandatory(PL_EMPLOYMENT_CA_NONPLCC))
					{
						if(getNGValue('cmplx_EmploymentDetails_NepType'=='NTJ'))
						{
							if(getNGValue('cmplx_EmploymentDetails_LOSPrevious')=='')
							{
								showAlert('cmplx_EmploymentDetails_LOSPrevious',alerts_String_Map['PL045']);
								return false;
	
							}
						}
						if(getNGValue('cmplx_EmploymentDetails_Freezone'==true))
						{
							if(getNGValue('cmplx_EmploymentDetails_FreezoneName')=='' || getNGValue('cmplx_EmploymentDetails_FreezoneName')=='--Select--')
							{
								showAlert('cmplx_EmploymentDetails_FreezoneName',alerts_String_Map['PL046']);
								return false;
	
							}
						}
							//commented by saurabh on 7th Dec
						//return false;
					}
					
				}
			
	
			
			}/* else if(activityName=='CSM' ){
				if(getNGValue('cmplx_Customer_Nationality')!='AE'  && (getNGValue('cmplx_EmploymentDetails_VisaSponser')=='' || getNGValue('cmplx_EmploymentDetails_VisaSponser')==' '))
				{
					showAlert('cmplx_EmploymentDetails_VisaSponser',alerts_String_Map['VAL291']);
					return false;
				}
			}*/
			/*if((getNGValue('cmplx_EmploymentDetails_Designation')=='OTHER' || getNGValue('cmplx_EmploymentDetails_Designation')=='41')
					&& (getNGValue('cmplx_EmploymentDetails_OtherDesign')==null || getNGValue('cmplx_EmploymentDetails_OtherDesign')=='') && (getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SAL')){			
							showAlert('cmplx_EmploymentDetails_OtherDesign','Please Enter Other Designation (Income Document)');
							return false;
			}//pcasp-2348
*/			
		if(getNGValue('cmplx_Customer_NEP')!='')
		{
			if(getNGValue('cmplx_EmploymentDetails_NepType')=='' || getNGValue('cmplx_EmploymentDetails_NepType')=='--Select--')
			{
				showAlert('cmplx_EmploymentDetails_NepType',alerts_String_Map['PL047']);
				return false;
			}
		}
		// ++ above code already present - 09-10-2017
		 if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='NEP'){
			if(!checkMandatory(PL_NEP_EMPLOYMENT))
				return false;
		}		
				
		else if(getNGValue('cmplx_EmploymentDetails_targetSegCode').indexOf('Freezone')>-1){
			if(!checkMandatory(PL_Freezone_EMPLOYMENT))
				return false;
		}		
				
		else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='Tenancy contract'){
			if(!checkMandatory(PL_Tenancy_EMPLOYMENT))
				return false;
		}		
		
		/*else if(getNGValue('cmplx_EmploymentDetails_targetSegCode').indexof('Empid')>-1){
			if(checkMandatory(PL_EMpId_EMPLOYMENT))
				return false;
		}*/
		
			
		else if(!checkMandatory(PL_EMPLOYMENT))
			return false;
	}  	
	return true;	
}	
	/*
	 if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='--Select--')
				showAlert('cmplx_EmploymentDetails_ApplicationCateg',alerts_String_Map['PL039']);	
				
	else if(getNGValue('cmplx_EmploymentDetails_EmpName')=='')
				showAlert('cmplx_EmploymentDetails_EmpName',alerts_String_Map['PL048']);
				
	else if(getNGValue('cmplx_EmploymentDetails_EMpCode')=='')
					showAlert('cmplx_EmploymentDetails_EmpName',alerts_String_Map['PL049']);	
					
	else if(getNGValue('cmplx_EmploymentDetails_Emp_Type')=='--Select--')
				showAlert('cmplx_EmploymentDetails_Emp_Type',alerts_String_Map['PL050']);
				
	else if(getNGValue('cmplx_EmploymentDetails_CurrEmployer')=='--Select--')
				showAlert('cmplx_EmploymentDetails_CurrEmployer',alerts_String_Map['PL051']);
	
	else if(getNGValue('cmplx_EmploymentDetails_EmpContractType')=='--Select--')
				showAlert('cmplx_EmploymentDetails_EmpContractType',alerts_String_Map['PL052']);
	
	else if(getNGValue('cmplx_EmploymentDetails_EmpIndusSector')=='--select--')
				showAlert('cmplx_EmploymentDetails_EmpIndusSector',alerts_String_Map['PL053']);
	
	else if(getNGValue('cmplx_EmploymentDetails_Indus_Macro')=='--select--')
				showAlert('cmplx_EmploymentDetails_Indus_Macro',alerts_String_Map['PL054']);

	else if(getNGValue('cmplx_EmploymentDetails_Indus_Micro')=='--select--')
				showAlert('cmplx_EmploymentDetails_Indus_Micro',alerts_String_Map['PL055']);

				
	else if(getNGValue('cmplx_EmploymentDetails_DOJ')=='')
				showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['PL056']);						
	
	else if(getNGValue('cmplx_EmploymentDetails_Designation')=='--Select--')
				showAlert('cmplx_EmploymentDetails_Designation',alerts_String_Map['PL057']);
				
	else if(getNGValue('cmplx_EmploymentDetails_DesigVisa')=='--Select--')
				showAlert('cmplx_EmploymentDetails_DesigVisa',alerts_String_Map['PL058']);			
	
	else if(getNGValue('name="cmplx_EmploymentDetails_JobConfirmed"')=='--Select--')
				showAlert('name="cmplx_EmploymentDetails_JobConfirmed"',alerts_String_Map['PL059']);		
				
	else if(getNGValue('cmplx_EmploymentDetails_LOS')=='')
				showAlert('cmplx_EmploymentDetails_LOS',alerts_String_Map['PL060']);
	
	else if(getNGValue('cmplx_EmploymentDetails_FreezoneName')=='')
				showAlert('cmplx_EmploymentDetails_FreezoneName',alerts_String_Map['PL061']);
	
	else if(getNGValue('cmplx_EmploymentDetails_HeadOfficeEmirate')=='')
				showAlert('cmplx_EmploymentDetails_HeadOfficeEmirate',alerts_String_Map['PL062']);
				
	else if(getNGValue('cmplx_EmploymentDetails_IncInPL')==false)
				showAlert('cmplx_EmploymentDetails_IncInPL',alerts_String_Map['PL063']);
				
	else if(getNGValue('cmplx_EmploymentDetails_IncInCC')==false)
				showAlert('cmplx_EmploymentDetails_IncInCC',alerts_String_Map['PL064']);								
	
	else
		return true;
		
return false;			
	}
	*/
	function Misc_Save_Check()
	{
		if(getNGValue('cmplx_MiscFields_School')=='')
					showAlert('cmplx_MiscFields_School',alerts_String_Map['PL065']);
					
		else if(getNGValue('cmplx_MiscFields_PropertyType')=='')
					showAlert('cmplx_MiscFields_PropertyType',alerts_String_Map['PL066']);
					
		else if(getNGValue('cmplx_MiscFields_RealEstate')=='')
					showAlert('cmplx_MiscFields_RealEstate',alerts_String_Map['PL067']);
					
		else if(getNGValue('cmplx_MiscFields_FarmEmirate')=='')
					showAlert('cmplx_MiscFields_FarmEmirate',alerts_String_Map['PL068']);
		
		else			
			return true;
	
	return false;
	}		
	
	function Liability_Save_Check()
	{
		/*if(getNGValue("cmplx_Liability_New_AECBconsentAvail")==false){
				setEnabled("Liability_New_Button1",false);
				showAlert('cmplx_Liability_New_AECBconsentAvail',alerts_String_Map['PL069']);
				}
				
		else if(getNGValue("cmplx_Liability_New_AECBconsentAvail")==true)
			setEnabled("Liability_New_Button1",true);	
		 			
		else if(com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Liability_New_cmplx_GR_IntLiability")==0)
			showAlert('prd_typ',alerts_String_Map['PL070']);
		
		else if(getNGValue("cmplx_Liability_New_TAI")=='')
			showAlert('cmplx_Liability_New_TAI',alerts_String_Map['PL071']);*/
			
		if(getNGValue("cmplx_Liability_New_DBRNet")=='')
			showAlert('cmplx_Liability_New_DBRNet',alerts_String_Map['PL072']);
			
		/*else if(getNGValue("cmplx_Liability_New_AggrExposure")=='')
			showAlert('cmplx_Liability_New_AggrExposure',alerts_String_Map['PL073']);	
			
		else
			return true;
	return false;*/
	}
	
	function Eligibility_Save_Check()
	{
		if(getNGValue('cmplx_EligibilityAndProductInfo_FinalDBR')=='')
				showAlert('cmplx_EligibilityAndProductInfo_FinalDBR',alerts_String_Map['PL074']);
				
		else if(getNGValue('cmplx_EligibilityAndProductInfo_Tenor')=='')
					showAlert('cmplx_EligibilityAndProductInfo_Tenor',alerts_String_Map['PL075']);
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_NetPayout')=='')
					showAlert('cmplx_EligibilityAndProductInfo_NetPayout',alerts_String_Map['PL076']);		
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_RepayFreq')=='')
					showAlert('cmplx_EligibilityAndProductInfo_RepayFreq',alerts_String_Map['PL077']);
					
		/*else if(getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate')=='')
					showAlert('cmplx_EligibilityAndProductInfo_FirstRepayDate',alerts_String_Map['PL078']);
			*/		
		else if(getNGValue('cmplx_EligibilityAndProductInfo_InterestType')=='')
					showAlert('cmplx_EligibilityAndProductInfo_InterestType',alerts_String_Map['PL079']);
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_BaseRateType')=='')
					showAlert('cmplx_EligibilityAndProductInfo_BaseRateType',alerts_String_Map['PL080']);
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_MArginRate')=='')
					showAlert('cmplx_EligibilityAndProductInfo_MArginRate',alerts_String_Map['PL081']);			
		
		else if(getNGValue('cmplx_EligibilityAndProductInfo_BAseRate')=='')
					showAlert('cmplx_EligibilityAndProductInfo_BAseRate',alerts_String_Map['PL082']);
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_ProdPrefRate')=='')
					showAlert('cmplx_EligibilityAndProductInfo_ProdPrefRate',alerts_String_Map['PL083']);
		
		else if(getNGValue('cmplx_EligibilityAndProductInfo_FinalInterestRate')=='')
					showAlert('cmplx_EligibilityAndProductInfo_FinalInterestRate',alerts_String_Map['PL084']);
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_MaturityDate')=='')
					showAlert('cmplx_EligibilityAndProductInfo_MaturityDate',alerts_String_Map['PL085']);			
		
		else if(getNGValue('cmplx_EligibilityAndProductInfo_LPF')=='')
					showAlert('cmplx_EligibilityAndProductInfo_LPF',alerts_String_Map['PL086']);
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_LPFAmount')=='')
					showAlert('cmplx_EligibilityAndProductInfo_LPFAmount',alerts_String_Map['PL087']);		
		
		else if(getNGValue('cmplx_EligibilityAndProductInfo_Insurance')=='')
					showAlert('cmplx_EligibilityAndProductInfo_Insurance',alerts_String_Map['PL088']);
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_InsuranceAmount')=='')
					showAlert('cmplx_EligibilityAndProductInfo_InsuranceAmount',alerts_String_Map['PL089']);	
		else
			return true;
	return false;
	}
function checkCust()
{//cmplx_fieldvisit_sp2_field_v_time
//cmplx_fieldvisit_sp2_drop1
	if(getNGValue('cmplx_cust_ver_sp2_desig_drop')=="--Select--" || getNGValue('cmplx_cust_ver_sp2_desig_drop')=="")
	{
		showAlert('cmplx_cust_ver_sp2_desig_drop',alerts_String_Map['CC291']);	
		return false;
	}
	if(getNGValue('cmplx_cust_ver_sp2_doj_drop')=="--Select--" || getNGValue('cmplx_cust_ver_sp2_doj_drop')=="")
	{
		showAlert('cmplx_cust_ver_sp2_doj_drop',alerts_String_Map['CC292']);	
		return false;
	}
	if(getNGValue('cmplx_cust_ver_sp2_saalry_drop')=="--Select--" || getNGValue('cmplx_cust_ver_sp2_saalry_drop')=="")
	{
		showAlert('cmplx_cust_ver_sp2_saalry_drop',alerts_String_Map['CC293']);	
		return false;
	}
	//PCASI-3660
	/*if(getNGValue('cmplx_cust_ver_sp2_desig_drop')=='Mismatch'&& getNGValue('cmplx_cust_ver_sp2_doj_remarks')=='')	//cmplx_cust_ver_sp2_desig_remarks
	{
		showAlert('cmplx_cust_ver_sp2_doj_remarks',alerts_String_Map['PL437']);
		return false;
	}*/
	
	if(getNGValue('cmplx_cust_ver_sp2_desig_drop')=='Mismatch'&& getNGValue('cmplx_cust_ver_sp2_desig_remarks')=='')
	{
		showAlert('cmplx_cust_ver_sp2_desig_remarks',alerts_String_Map['PL437']);
		return false;
	}
	
	if(getNGValue('cmplx_cust_ver_sp2_ContactedOn')=='Mismatch'&& getNGValue('cmplx_cust_ver_sp2_Mobile')=='')
	{
		showAlert('cmplx_cust_ver_sp2_Mobile',alerts_String_Map['PL437']);
		return false;
	}//by Alok for JIRA 3660
	
	/*if(getNGValue('cmplx_cust_ver_sp2_emp_status_drop')=='Mismatch'&& (getNGValue('cmplx_cust_ver_sp2_emp_status__remarks')=='' ||getNGValue('cmplx_cust_ver_sp2_emp_status__remarks')=='--Select--' ||getNGValue('cmplx_emp_ver_sp2_empstatus_remarks')=='0'))
	{
		showAlert('cmplx_cust_ver_sp2_emp_status__remarks',alerts_String_Map['PL437']);
		return false;
	}*/
	
	if(getNGValue('cmplx_cust_ver_sp2_emp_status_drop')=='Mismatch'&& (getNGValue('cmplx_cust_ver_sp2_emp_status__remarks')=='' ||getNGValue('cmplx_cust_ver_sp2_emp_status__remarks')=='0'))//cmplx_cust_ver_sp2_fpu_remarks
	{
		showAlert('cmplx_cust_ver_sp2_emp_status__remarks',alerts_String_Map['PL437']);
		return false;
	}
	
	if(getNGValue('cmplx_cust_ver_sp2_saalry_drop')=='Mismatch'&& getNGValue('cmplx_cust_ver_sp2_salary_remarks')=='')
	{
		showAlert('cmplx_cust_ver_sp2_salary_remarks',alerts_String_Map['PL437']);
		return false;
	}
	if(getNGValue('cmplx_cust_ver_sp2_years_drop')=='Mismatch'&& getNGValue('cmplx_cust_ver_sp2_years_remarks')=='')
	{
		showAlert('cmplx_cust_ver_sp2_years_remarks',alerts_String_Map['PL437']);
		return false;
	}
	if(getNGValue('cmplx_cust_ver_sp2_salary_payment_drop')=='Mismatch'&& getNGValue('cmplx_cust_ver_sp2_salary_payment_remarks')=='')
	{
		showAlert('cmplx_cust_ver_sp2_salary_payment_remarks',alerts_String_Map['PL437']);
		return false;
	}
	if(getNGValue('cmplx_cust_ver_sp2_doj_drop')=='Mismatch'&& getNGValue('cmplx_cust_ver_sp2_doj_remarks')=='')
	{
		showAlert('cmplx_cust_ver_sp2_doj_remarks',alerts_String_Map['PL437']);
		return false;
	}
	
	
	if(getNGValue('cmplx_cust_ver_sp2_fpu_remarks')=="")
	{
	showAlert('cmplx_cust_ver_sp2_fpu_remarks',alerts_String_Map['CC290']);	
	return false;
	}
	return true;
}
function checkEmp()
{
	
	if(getNGValue('cmplx_emp_ver_sp2_desig_drop')=="--Select--" ||getNGValue('cmplx_emp_ver_sp2_desig_drop')=="")
	{
		showAlert('cmplx_emp_ver_sp2_desig_drop',alerts_String_Map['CC291']);	
		return false;
	}
	
	if(getNGValue('cmplx_emp_ver_sp2_off_drop')=="--Select--" ||getNGValue('cmplx_emp_ver_sp2_off_drop')=="")
	{
		showAlert('cmplx_emp_ver_sp2_off_drop',alerts_String_Map['PL437']);	
		return false;
	}//by Alok for JIRA 3660
	
	if(getNGValue('cmplx_emp_ver_sp2_emirate_drop')=="--Select--" || getNGValue('cmplx_emp_ver_sp2_emirate_drop')=="")
	{
		showAlert('cmplx_emp_ver_sp2_emirate_drop',alerts_String_Map['CC294']);	
		return false;
	}
	if(getNGValue('cmplx_emp_ver_sp2_doj_drop')=="--Select--" || getNGValue('cmplx_emp_ver_sp2_doj_drop')=="")
	{
		showAlert('cmplx_emp_ver_sp2_doj_drop',alerts_String_Map['CC292']);	
		return false;
	}
	
	if(getNGValue('cmplx_emp_ver_sp2_salary_drop')=="--Select--" || getNGValue('cmplx_emp_ver_sp2_salary_drop')=="")
	{
		showAlert('cmplx_emp_ver_sp2_salary_drop',alerts_String_Map['CC293']);	
		return false;
	}
	if(getNGValue('cmplx_emp_ver_sp2_fpu_rem')=="")
	{
	showAlert('cmplx_emp_ver_sp2_fpu_rem',alerts_String_Map['CC290']);	
	return false;
	}
	if(getNGValue('cmplx_emp_ver_sp2_desig_drop')=='Mismatch'&& getNGValue('cmplx_emp_ver_sp2_desig_remarks')=='')
	{
		showAlert('cmplx_emp_ver_sp2_desig_remarks',alerts_String_Map['PL437']);
		return false;
	}
	
	if(getNGValue('cmplx_emp_ver_sp2_off_drop')=='Mismatch'&& getNGValue('cmplx_emp_ver_sp2_landline')=='')
	{
		showAlert('cmplx_emp_ver_sp2_landline',alerts_String_Map['PL437']);
		return false;
	}//by Alok for JIRA 3660
	if(getNGValue('cmplx_emp_ver_sp2_doj_drop')=='Mismatch'&& getNGValue('cmplx_emp_ver_sp2_doj_remarks')=='')
	{
		showAlert('cmplx_emp_ver_sp2_doj_remarks',alerts_String_Map['PL437']);
		return false;
	}
	if(getNGValue('cmplx_emp_ver_sp2_salary_drop')=='Mismatch'&& getNGValue('cmplx_emp_ver_sp2_salary_remarks')=='')
	{
		showAlert('cmplx_emp_ver_sp2_salary_remarks',alerts_String_Map['PL437']);
		return false;
	}
	if(getNGValue('cmplx_emp_ver_sp2_sal_pay_drop')=='Mismatch'&& getNGValue('cmplx_emp_ver_sp2_sal_pay_remarks')=='')
	{
		showAlert('cmplx_emp_ver_sp2_sal_pay_remarks',alerts_String_Map['PL437']);
		return false;
	}
	if(getNGValue('cmplx_emp_ver_sp2_salary_break_drop')=='Mismatch'&& getNGValue('cmplx_emp_ver_sp2_salary_break_remarks')=='')
	{
		showAlert('cmplx_emp_ver_sp2_salary_break_remarks',alerts_String_Map['PL437']);
		return false;
	}
	if(getNGValue('cmplx_emp_ver_sp2_empstatus_drop')=='Mismatch'&& (getNGValue('cmplx_emp_ver_sp2_empstatus_remarks')=='' ||getNGValue('cmplx_emp_ver_sp2_empstatus_remarks')=='--Select--' ||getNGValue('cmplx_emp_ver_sp2_empstatus_remarks')=='0'))
	{
		showAlert('cmplx_emp_ver_sp2_empstatus_remarks',alerts_String_Map['PL437']);
		return false;
	}
	return true;
}


	
	function ContactDetails_Save_Check()
	{
		if (validateMail1('AlternateContactDetails_EMAIL1_PRI') || getNGValue('AlternateContactDetails_EMAIL2_SEC')=='')
		{
			setNGValueCustom('AlternateContactDetails_EMAIL2_SEC',getNGValue('AlternateContactDetails_EMAIL1_PRI'));
	
		}
		if(getNGValue('AlternateContactDetails_MobileNo1')==''){
				showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['PL090']);
				return false;
		}		
		
		else if(getNGValue('AlternateContactDetails_MobileNo2')==''){
				showAlert('AlternateContactDetails_MobileNo2',alerts_String_Map['PL091']);	
				return false;
		}		
		
		else if(getNGValue('AlternateContactDetails_HOMECOUNTRYNO')==''){
					showAlert('AlternateContactDetails_HOMECOUNTRYNO',alerts_String_Map['PL092']);
					return false;
		}			
		else if(getNGValue('AlternateContactDetails_OFFICENO')=='')
					showAlert('AlternateContactDetails_OFFICENO',alerts_String_Map['PL093']);
		
		
		else if(getNGValue('AlternateContactDetails_EMAIL1_PRI')==''){
					showAlert('AlternateContactDetails_EMAIL1_PRI',alerts_String_Map['PL094']);
					return false;
		}
		//below code added nikhil for CIF Creation failing Creation
					
		else if(getNGValue('AlternateContactDetails_ESTATEMENTFLAG')=='' ||getNGValue('AlternateContactDetails_ESTATEMENTFLAG')=="--Select--"){
			showAlert('AlternateContactDetails_ESTATEMENTFLAG',alerts_String_Map['PL095']);	
			return false;
		}
		
		else if(getNGValue('AlternateContactDetails_carddispatch')=='--Select--' && getNGValue('is_cc_waiver_require')!='Y'){
					showAlert('AlternateContactDetails_carddispatch',alerts_String_Map['PL096']);	
					return false;
		}
					
		else if(getNGValue('AlternateContactDetails_carddispatch')=='Branch')
		{//by shweta
			if(getNGValue('AlternateContactDetails_CustomerDomicileBranch')=='--Select--'){
				showAlert('AlternateContactDetails_CustomerDomicileBranch',alerts_String_Map['PL097']);
			   return false;
			}
		}
			
	return true;
	}
	
	function CardDetails_Save()
	{
		if(getNGValue('cmplx_CardDetails_CardEmbossingName')=='')
		{
			showAlert('cmplx_CardDetails_CardEmbossingName',alerts_String_Map['PL098']);
			return false;
		}
		
		else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='Business titanium Card' || getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='Self Employed Credit Card'){
			 if(getNGValue('cmplx_CardDetails_CompanyEmbossingName')=='')
			{
				showAlert('cmplx_CardDetails_CompanyEmbossingName',alerts_String_Map['PL099']);
				return false;
			}
		}
		
		 if(getNGValue('Product_type')=='Islamic')
		{		
			if(getNGValue('cmplx_CardDetails_CharityOrg')=='--Select--')
			{
					showAlert('cmplx_CardDetails_CharityOrg',alerts_String_Map['PL100']);
					return false;
			}
					
		    else if(getNGValue('cmplx_CardDetails_CharityAmount')=='')
			{
					showAlert('cmplx_CardDetails_CharityAmount',alerts_String_Map['PL101']);
					return false;
			}
		}
					
		 if(getNGValue('cmplx_CardDetails_SendStatTo')=='')
		{
		  showAlert('cmplx_CardDetails_SendStatTo',alerts_String_Map['PL102']);
		  return false;
		}
		
	return true;
	}
	
	//added by akshay on 20/5/18 for proc 9461
	function checkMandatory_CardDetails_Save(){
		var activity = window.parent.stractivityName;
		/*if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,0)=='Islamic'){
			if(getNGValue('cmplx_CardDetails_charity_amount')==""){
				showAlert('cmplx_CardDetails_charity_amount',alerts_String_Map['VAL171']);
				return false;
			}
			else if(getNGValue('cmplx_CardDetails_Charity_org')==""){
				showAlert('cmplx_CardDetails_Charity_org',alerts_String_Map['CC036']);
				return false;
				}
			}---commented by akshay  on 1/5/18*/
			if(getNGValue('is_cc_waiver_require')!='Y'){
			if(getNGValue("IS_Approve_Cif")!='Y' && activityName=='DDVT_maker' )
			{
			if(getNGValue('cmplx_CardDetails_cardemboss')==""){
				showAlert('cmplx_CardDetails_cardemboss',alerts_String_Map['VAL029']);
				return false;
			}
			}
			}
			//change by saurabh on 7th Jan
			/*else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5).indexOf('KALYAN')>-1 && com.newgen.omniforms.formviewer.isEnabled('cmplx_CardDetails_CompanyEmbossing_name')){
				 if(isFieldFilled('cmplx_CardDetails_CompanyEmbossing_name')==false)
				{
					showAlert('cmplx_CardDetails_CompanyEmbossing_name',alerts_String_Map['VAL036']);
					return false;
				}
			}*/
			
			if(getNGValue('cmplx_CardDetails_sendstatto')==""){
				showAlert('cmplx_CardDetails_sendstatto',alerts_String_Map['CC200']);
				return false;
			}
			if(getNGValue('cmplx_CardDetails_statcycle')==""){
				showAlert('cmplx_CardDetails_statcycle',alerts_String_Map['VAL278']);
				return false;
			}
			if(getNGValue('is_cc_waiver_require')!='Y' && (getNGValue('cmplx_CardDetails_suppcardreq')=='' || getNGValue('cmplx_CardDetails_suppcardreq')=='--Select--'))
			{
				showAlert('cmplx_CardDetails_suppcardreq',alerts_String_Map['PL102']);
				  return false;
			}
			if(getNGValue("cmplx_CardDetails_securitycheck")==true && getLVWRowCount('cmplx_CardDetails_cmpmx_gr_cardDetails')==0){
				if(getNGValue('CardDetails_BankName')==""){
					showAlert('CardDetails_BankName',alerts_String_Map['CC018']);	
					return false;
				}
				else if(getNGValue('CardDetails_ChequeNumber')==""){
					showAlert('CardDetails_ChequeNumber',alerts_String_Map['CC037']);	
					return false;
				}
				else if(getNGValue('CardDetails_Amount')==""){
					showAlert('CardDetails_Amount',alerts_String_Map['CC007']);	
					return false;
				}
				else if(getNGValue('CardDetails_Date')==""){
					showAlert('CardDetails_Date',alerts_String_Map['CC058']);	
					return false;
				}
			}
			
			//12th september
			//change by saurabh on 7th Jan
			if(activity!='CSM' && getNGValue('is_cc_waiver_require')!='Y'){
				if(getNGValue('cmplx_CardDetails_SelfSupp_required')=='Yes' && (getNGValue('cmplx_CardDetails_Selected_Card_Product')=='' || getNGValue('cmplx_CardDetails_Selected_Card_Product')=='--Select--') )
				{
					showAlert('cmplx_CardDetails_SelfSupp_required',alerts_String_Map['VAL386']);
					  return false;
				}
				if(getNGValue('cmplx_CardDetails_SelfSupp_required')=='Yes' && getNGValue('cmplx_CardDetails_SelfSupp_Limit')==''  )
				{
					showAlert('cmplx_CardDetails_SelfSupp_Limit','Self Supplement Card Limit is Mandatory!');
					  return false;
				}
				if(getNGValue('cmplx_CardDetails_SelfSupp_required')=='Yes' && getNGValue('cmplx_CardDetails_Self_card_embossing')==''  )
				{
					showAlert('cmplx_CardDetails_Self_card_embossing','Self Supplement Card Embossing is Mandatory!');
					  return false;
				}
				if(getNGValue('cmplx_CardDetails_SelfSupp_required')=='' || getNGValue('cmplx_CardDetails_SelfSupp_required')=='--Select--')
				{
					showAlert('cmplx_CardDetails_SelfSupp_required',alerts_String_Map['VAL387']);
					  return false;
				}
				
				
				var n=getLVWRowCount("cmplx_CardDetails_cmpmx_gr_cardDetails");
				if(n==0 && getNGValue("cmplx_CardDetails_securitycheck")==true){
					showAlert('cmplx_CardDetails_cmpmx_gr_cardDetails','Security Cheque Details '+alerts_String_Map['CC097']);
					return false;
				}
				
				var crngridcount = getLVWRowCount('cmplx_CardDetails_cmplx_CardCRNDetails');
				if(crngridcount>0){
					for(var i=0;i<crngridcount;i++){
						//condition added by akshay on 31/3/18
						if(getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,1)=='' || getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,2)==''){
						showAlert('cmplx_CardDetails_cmplx_CardCRNDetails', alerts_String_Map['CC263']+ getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,0));
						return false;
						}
						if(getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,4)==''){
						showAlert('cmplx_CardDetails_cmplx_CardCRNDetails', ' Please modify Interest Profile for Eligible cards');
						return false;
						}
						if(getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,0).indexOf('KALYAN')>-1 && getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,8)==''){
						showAlert('cmplx_CardDetails_cmplx_CardCRNDetails', ' Please generate KRN number for '+getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,0));
						return false;
						}
					}
				}
			}				
			//12th september
			return true;
		}
		
	//now this fn is working  for fatca add validation due to addition of grid save button functionality replace by add button
	function FATCA_Save_Check(optype)
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_FATCA_cmplx_FATCAGrid');
		var custTypePickList = document.getElementById("cmplx_FATCA_CustomerType");
		//var picklistValues=getPickListValues(custTypePickList);
		var picklistValues=getPickListValues(custTypePickList);
		var CustType=getNGValue("cmplx_FATCA_CustomerType");
		var gridValue=[];
		for(var i=0;i<n;i++)
		{
			gridValue.push(getLVWAT("cmplx_FATCA_cmplx_FATCAGrid",i,13));
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
				showAlert('cmplx_FATCA_IdDoc',alerts_String_Map['VAL268']);
				return false;
			}
			else if(getNGValue('FATCA_USRelaton')=='R' && getNGValue('cmplx_FATCA_W9Form')==false){
				showAlert('cmplx_FATCA_W9Form','Please select W9 Form.');
				return false;
			} // pcasi3725
			else if(getNGValue('cmplx_FATCA_W8Form')==true)
			{
				if(getNGValue('cmplx_FATCA_SignedDate')=='' && getNGValue('FATCA_USRelaton')=='R') //added only for R signed date needed
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
			if(getNGValue('cmplx_FATCA_SignedDate')=='' && getNGValue('FATCA_USRelaton')=='R') //added only for R signed date needed
			{
						showAlert('cmplx_FATCA_SignedDate',alerts_String_Map['VAL276']);
						return false;
			}
			else if(getNGValue('cmplx_FATCA_ExpiryDate')=='' && getNGValue('FATCA_USRelaton')=='R') //added only for R signed date needed
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
			else if(getNGValue('cmplx_FATCA_ConPerHasUS')=='')
			{
						showAlert('cmplx_FATCA_ConPerHasUS',alerts_String_Map['VAL176']);
						return false;
			}
			var rowCount = com.newgen.omniforms.formviewer.getItemCount('cmplx_FATCA_SelectedReason');			
			if(rowCount == 0){
						showAlert('cmplx_FATCA_SelectedReason',alerts_String_Map['VAL269']);
						return false;
			}
			else if(rowCount == 1 && com.newgen.omniforms.formviewer.getNGItemText('cmplx_FATCA_SelectedReason',0)==""){
						showAlert('cmplx_FATCA_SelectedReason',alerts_String_Map['VAL269']);
						return false;
			}			
			/*else if(getNGValue('cmplx_FATCA_SelectedReason')=='')
			{
						showAlert('cmplx_FATCA_SelectedReason',alerts_String_Map['VAL269']);
						return false;
			}*/
	}	
		for(var i=0;i<gridValue.length;i++)
		{
			 if(gridValue.indexOf(CustType)>-1 && optype=="add")
			{
						showAlert('cmplx_FATCA_CustomerType','FATCA Already Added for Customer'+CustType);
						return false;
						break;
			}
		}
	return true;
	}
	/* function FATCA_Save_Check()
	{
		// ++ below code already present - 09-10-2017		
		if(getNGValue('cmplx_FATCA_USRelation')=='--Select--' || getNGValue('cmplx_FATCA_USRelation')=='' )
		{
				showAlert('cmplx_FATCA_USRelation',alerts_String_Map['PL103']);
		        return false;
		}
		
		else if(getNGValue('cmplx_FATCA_iddoc')==false && getNGValue('cmplx_FATCA_decforIndv')==false && getNGValue('cmplx_FATCA_w8form')==false && getNGValue('cmplx_FATCA_w9form')==false && getNGValue('cmplx_FATCA_lossofnationality')==false && getNGValue('cmplx_FATCA_USRelation')!='C')
		{
			showAlert('cmplx_FATCA_iddoc',alerts_String_Map['PL104']);
			return false;
		}
		
		// ++ below code already present - 09-10-2017 value of US relation is N or R not Yes
		//else if(getNGValue('cmplx_FATCA_USRelation')=='Yes')
		else if(getNGValue('cmplx_FATCA_USRelation')=='N' || getNGValue('cmplx_FATCA_USRelation')=='R')
		{
		    if(getNGValue('cmplx_FATCA_TINNo')=='')
			{
					showAlert('cmplx_FATCA_TINNo',alerts_String_Map['PL105']);	
					return false;
			}
		}
		
		if(getNGValue('cmplx_FATCA_SignedDate')=='' && getNGValue('cmplx_FATCA_USRelation')!='C')
		{
					showAlert('cmplx_FATCA_SignedDate',alerts_String_Map['PL106']);
					return false;
		}
					
		else if(getNGValue('cmplx_FATCA_ExpiryDate')=='')
		{
					showAlert('cmplx_FATCA_ExpiryDate',alerts_String_Map['PL107']);
                    return false;
		}					
					
		else if(getNGValue('cmplx_FATCA_Category')=='--Select--')
		{
					showAlert('cmplx_FATCA_Category',alerts_String_Map['PL108']);
					return false;
		}
					
		else if(getNGValue('cmplx_FATCA_ControllingPersonUSRel')=='')
		{
					showAlert('cmplx_FATCA_ControllingPersonUSRel',alerts_String_Map['PL109']);
					return false;
		}
		// ++ below code already present - 09-10-2017 value of US relation is N or R not Yes			
		//else if(getNGValue('cmplx_FATCA_USRelation')=='Yes')
		else if(getNGValue('cmplx_FATCA_USRelation')=='N' || getNGValue('cmplx_FATCA_USRelation')=='R')
		{			
			//alert(getNGValue('cmplx_FATCA_selectedreason'));
			/*if(getNGValue('cmplx_FATCA_selectedreason')==''){
					showAlert('cmplx_FATCA_selectedreason',alerts_String_Map['PL110']);
					return false;
			}
			var listbox = document.getElementById('cmplx_FATCA_selectedreason');
			var selectedReasonrowcount = listbox.options.length;
			var alert = false;
			if(selectedReasonrowcount>0){
				if(selectedReasonrowcount==1 && listbox.options[0].value==''){
					alert = true;
				}
				else if(selectedReasonrowcount>1){
					var tempflag = true;
					for(var i=0;i<selectedReasonrowcount;i++){
						if(listbox.options[i].value!=''){
							tempflag = false;
							break;
						}
					}
					alert = tempflag;
				}
			}
			else{
			alert = true;
			}
			if(alert){
			showAlert('cmplx_FATCA_selectedreason',alerts_String_Map['PL110']);
					return false;
			}
		}
											
		
	return true;
	}
	*/
	 function KYC_save_Check()
	{
		// ++ below code already present - 09-10-2017
		/*if(getNGValue('cmplx_KYC_KYCHeld')=='' || getNGValue('cmplx_KYC_KYCHeld')=='--Select--')
				showAlert('cmplx_KYC_KYCHeld',alerts_String_Map['PL111']);
			// ++ below code already present - 09-10-2017	
		else if(getNGValue('cmplx_KYC_PEP')=='' || getNGValue('cmplx_KYC_PEP')=='--Select--')
					showAlert('cmplx_KYC_PEP',alerts_String_Map['PL112']);
		else
		return true;
	return false;*/
	var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			//var custTypePickList = document.getElementById("KYC_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			
			
			for(var i=0;i<n;i++)
			{
				if(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0)=='Y' && getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,4)=='')
				{
					showAlert('',"KYC review date cannot be blank where kYC held is Yes");
					return false;
				}
			}
	} 
	
	function OECD_Save_Check()
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			var custTypePickList = document.getElementById("OECD_CustomerType");
			var picklistValues=getPickListValues(custTypePickList);
			if((picklistValues.length)>n)
			{
				showAlert('OECD_CustomerType','Please add OECD for all Applicant type');	
					return false;
			}
			
				
					
				
					
		return true;
	}
	// ++ below code already present - 09-10-2017
	function checkMandatory_OecdGrid(operation)
	{
	 
			//Added By prabhakar
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			//var custTypePickList = document.getElementById("OECD_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			var CustType=getNGValue("OECD_CustomerType");
			var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_OECD_cmplx_GR_OecdDetails",i,8));
			}
			 if(getNGValue('OECD_CustomerType')=='--Select--' || getNGValue('OECD_CustomerType')=='')
		{
					showAlert('OECD_CustomerType','Applicant type can not be blank');
					return false;
		}
		else if(getNGValue('OECD_CRSFlag')=='--Select--' || getNGValue('OECD_CRSFlag')==''){
					showAlert('OECD_CRSFlag',alerts_String_Map['PL117']);
					return false;
		}
		else if(getNGValue('OECD_CRSFlag')=='Y'){
					if(getNGValue('OECD_CRSFlagReason')=='--Select--' || getNGValue('OECD_CRSFlagReason')=='' || getNGValue('OECD_CRSFlagReason')=='NA' ){
						showAlert('OECD_CRSFlagReason',alerts_String_Map['PL118']);
						return false;
					}
					if( getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')=='' ){
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['PL119']);
						return false;
					}
					if( getNGValue('OECD_tinNo')==''  /*&& getNGValue('OECD_noTinReason')=='' */){
						showAlert('OECD_tinNo',alerts_String_Map['PL121']);
						return false;
					}
					// if(getNGValue('OECD_CountryBirth')=='--Select--' || getNGValue('OECD_CountryBirth')==''){
					//showAlert('OECD_CountryBirth',alerts_String_Map['PL114']);
					//return false;
					//}
					//if(getNGValue('OECD_townBirth')=='--Select--' || getNGValue('OECD_townBirth')==''){
					//			showAlert('OECD_townBirth',alerts_String_Map['PL120']);
					//			return false;
					//}
					if(gridValue.indexOf(CustType)>-1 && operation=='Add')
					{
						showAlert('OECD_CustomerType','OECD Already Added for Applicant '+CustType);
						return false;
					}
		}
		else if(getNGValue('OECD_CRSFlag')=='N')
		{
					if(getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')=='' ){
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['PL119']);
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['PL119']);
						return false;
					}
					if(getNGValue('OECD_tinNo')=='' /* && getNGValue('OECD_noTinReason')==''*/ ){  //jahnavi
						showAlert('OECD_tinNo',alerts_String_Map['PL121']);
						return false;
					}		
					if(gridValue.indexOf(CustType)>-1 && operation=='Add')
					{
						showAlert('OECD_CustomerType','OECD Already Added for Applicant '+CustType);
						return false;
					}					
		}
		else if(gridValue.indexOf(CustType)>-1 && operation=='Add')
			{
				
						showAlert('OECD_CustomerType','OECD Already Added for Applicant '+CustType);
						return false;
		}
	return true;
	}
	
	// ++ above code already present - 09-10-2017
	 
		/*
		if(getNGValue('cmplx_CC_Loan_transType')=='' || getNGValue('cmplx_CC_Loan_transType')=='--Select--')
					showAlert('cmplx_CC_Loan_transType',alerts_String_Map['PL123']);
				
		else if(getNGValue('cmplx_CC_Loan_CcNo')=='')
					showAlert('cmplx_CC_Loan_CcNo',alerts_String_Map['PL124']);
					
		else if(getNGValue('cmplx_CC_Loan_TransMode')=='')
					showAlert('cmplx_CC_Loan_TransMode',alerts_String_Map['PL125']);
					
		else if(getNGValue('cmplx_CC_Loan_BankName')=='')
					showAlert('cmplx_CC_Loan_BankName',alerts_String_Map['PL126']);
					
		else if(getNGValue('cmplx_CC_Loan_Tenure')=='')
					showAlert('cmplx_CC_Loan_Tenure',alerts_String_Map['PL127']);
					
		else if(getNGValue('cmplx_CC_Loan_DispatchChannel')=='')
					showAlert('cmplx_CC_Loan_DispatchChannel',alerts_String_Map['PL128']);
					
		else if(getNGValue('cmplx_CC_Loan_MarketingCode')=='')
					showAlert('cmplx_CC_Loan_MarketingCode',alerts_String_Map['PL129']);
					
		else if(getNGValue('cmplx_CC_Loan_Date')=='')
					showAlert('cmplx_CC_Loan_Date',alerts_String_Map['PL130']);
					
		else if(getNGValue('cmplx_CC_Loan_SourceCode')=='')
					showAlert('cmplx_CC_Loan_SourceCode',alerts_String_Map['PL131']);			
		
		else if(getNGValue('cmplx_CC_Loan_TotalBalXfer')=='')
					showAlert('cmplx_CC_Loan_TotalBalXfer',alerts_String_Map['PL132']);
					
		else if(getNGValue('cmplx_CC_Loan_DDSFlag')==false)
					showAlert('cmplx_CC_Loan_DDSFlag',alerts_String_Map['PL133']);
					
		else if(getNGValue('cmplx_CC_Loan_Percentage')=='')
					showAlert('cmplx_CC_Loan_Percentage','alerts_String_Map['PL134']);
					
		else if(getNGValue('cmplx_CC_Loan_DDSAmount')=='')
					showAlert('cmplx_CC_Loan_DDSAmount',alerts_String_Map['PL135']);
					
		else if(getNGValue('cmplx_CC_Loan_DDSMode')=='')
					showAlert('cmplx_CC_Loan_DDSMode',alerts_String_Map['PL136']);
					
		else if(getNGValue('cmplx_CC_Loan_AccType')=='' || getNGValue('cmplx_CC_Loan_AccType')=='--Select--')
					showAlert('cmplx_CC_Loan_AccType',alerts_String_Map['PL137']);			
		
		else if(getNGValue('cmplx_CC_Loan_DDSIBanNo')=='')
					showAlert('cmplx_CC_Loan_DDSIBanNo',alerts_String_Map['PL138']);
					
		else if(getNGValue('cmplx_CC_Loan_DDSStartdate')=='')
					showAlert('cmplx_CC_Loan_DDSStartdate',alerts_String_Map['PL139']);
					
		else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='')
					showAlert('cmplx_CC_Loan_ModeOfSI',alerts_String_Map['PL140']);
					
		else if(getNGValue('cmplx_CC_Loan_DueDate')=='')
					showAlert('cmplx_CC_Loan_DueDate',alerts_String_Map['PL141']);	
		
		else if(getNGValue('cmplx_CC_Loan_SI_Percentage')=='')
					showAlert('cmplx_CC_Loan_SI_Percentage',alerts_String_Map['PL142']);
		
		else if(getNGValue('cmplx_CC_Loan_FlatAMount')=='')
					showAlert('cmplx_CC_Loan_FlatAMount',alerts_String_Map['PL143']);
					
		else if(getNGValue('cmplx_CC_Loan_SIDate')=='')
					showAlert('cmplx_CC_Loan_SIDate',alerts_String_Map['PL144']);
					
		else if(getNGValue('cmplx_CC_Loan_VPSStatus')=='')
					showAlert('cmplx_CC_Loan_VPSStatus',alerts_String_Map['PL145']);
		
		else if(getNGValue('cmplx_CC_Loan_VPSPAckage')=='' || getNGValue('cmplx_CC_Loan_VPSPAckage')==null)
				showAlert('cmplx_CC_Loan_VPSPAckage',alerts_String_Map['PL146']);					
		
		else if(getNGValue('cmplx_CC_Loan_VPSSaleMode')=='')
					showAlert('cmplx_CC_Loan_VPSSaleMode',alerts_String_Map['PL147']);	
		
		else if(getNGValue('cmplx_CC_Loan_VPSSourceCode')=='' || getNGValue('cmplx_CC_Loan_VPSSourceCode')==null)
				showAlert('cmplx_CC_Loan_VPSSourceCode',alerts_String_Map['PL148']);					
		
		else if(getNGValue('cmplx_CC_Loan_VPSRegDate')=='')
					showAlert('cmplx_CC_Loan_VPSRegDate',alerts_String_Map['PL149']);	
			
		else if(getNGValue('cmplx_CC_Loan_VPSActDate')=='')
					showAlert('cmplx_CC_Loan_VPSActDate',alerts_String_Map['PL150']);			
		
		else
			return true;
	
	return false;
}	
	*/
	//Changes Done by aman for preferred choice
	function checkPrefferedChoice(){
		var gridRowCount = getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		var prefferedFlag = false;
		var IS_HOME=false,IS_RESIDENCE=false,IS_OFFICE=false;
		var custTypePickList = document.getElementById("AddressDetails_CustomerType");
		var picklistValues=getPickListValues(custTypePickList);
		var grid_CustomerType=[];
		var preferedCustomers = [];
		var allPreffered=0;//added by prabhakar
		for(var i=0; i<gridRowCount;i++)
		{
			 if(grid_CustomerType.indexOf(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13))==-1)
				{
					grid_CustomerType.push(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13));
				}
		}
			for(var i=0; i<gridRowCount;i++){
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'Home'  && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_HOME=true;
				}
				
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'OFFICE' && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_OFFICE=true;
				}
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'RESIDENCE' &&getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_RESIDENCE=true;
				}
				
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,10) == 'true'){
						allPreffered++;
						if( preferedCustomers.indexOf(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13))==-1)
						{
							preferedCustomers.push(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13));
						}					
				}
					
			}
			if(allPreffered==picklistValues.length){
				prefferedFlag = true;						
			}
			else if(allPreffered > (custTypePickList.length-1))
			{
				showAlert('AddressDetails_addtype','Multiple Preferred for Single Applicant. Please Resolve!');
				return false;
			}
			if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false && IS_HOME==false){
				showAlert('AddressDetails_addtype',alerts_String_Map['PL363']);
				return false;
			}
			if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false){
				showAlert('AddressDetails_addtype',alerts_String_Map['PL365']);
				return false;
			}
			else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_HOME==false && IS_RESIDENCE==false){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL364']);
					return false;
				}
			else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_HOME==false){
				showAlert('AddressDetails_addtype',alerts_String_Map['PL366']);
				return false;
			}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false ){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL369']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_RESIDENCE==false){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL368']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_HOME==false){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL367']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && IS_OFFICE==false && IS_RESIDENCE==false ){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL375']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && IS_OFFICE==false){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL374']);
					return false;
				}	
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && ( IS_RESIDENCE==false)){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL373']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_HOME==false) && ( IS_RESIDENCE==false) ){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL372']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_RESIDENCE==false)){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL371']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_HOME==false)){
					showAlert('AddressDetails_addtype',alerts_String_Map['PL370']);
					return false;
				}
				else if(!prefferedFlag){
					showAlert('AddressDetails_PreferredAddress','Please Add prefer Address for app Applicant');
					return false;
				}
				else {
					return true;
				}
			
			
	}
	//Changes Done by aman for preferred choice
	function MOL_Save_Check()
	{
	
		if(getNGValue('cmplx_MOL_compname')=='')
					showAlert('cmplx_MOL_compname',alerts_String_Map['PL151']);
		
		else if(getNGValue('cmplx_MOL_desig')=='')
					showAlert('cmplx_MOL_desig',alerts_String_Map['PL057']);
					
		else if(getNGValue('cmplx_MOL_nationality')=='')
					showAlert('cmplx_MOL_nationality',alerts_String_Map['PL013']);
					
		else if(getNGValue('cmplx_MOL_passno')=='')
					showAlert('cmplx_MOL_passno',alerts_String_Map['PL015']);
		else if(getNGValue('cmplx_MOL_gross')=='')
			showAlert('cmplx_MOL_gross',alerts_String_Map['PL034']);
		else if(getNGValue('cmplx_MOL_molexp')=='' || getNGValue('cmplx_MOL_molexp')==null)
				showAlert('cmplx_MOL_molexp',alerts_String_Map['PL152']);					
		
		else if(getNGValue('cmplx_MOL_molissue')=='')
					showAlert('cmplx_MOL_molissue',alerts_String_Map['PL153']);	
		
		else if(getNGValue('cmplx_MOL_ctrcttype')=='' || getNGValue('cmplx_MOL_ctrcttype')==null)
				showAlert('cmplx_MOL_ctrcttype',alerts_String_Map['PL154']);					
		
		else if(getNGValue('cmplx_MOL_molsalvar')=='')
				//change by saurabh on 15th Jan
					showAlert('cmplx_MOL_molsalvar',alerts_String_Map['PL155']);
		
		else if(getNGValue('cmplx_MOL_molsalvar')>100)
			showAlert('cmplx_MOL_molsalvar','The MOL Salary Variance value cannot be greater than 100');
			
		else if(getNGValue('cmplx_MOL_docatt')=='')
					showAlert('cmplx_MOL_docatt',alerts_String_Map['PL156']);
		
		else if(getNGValue('cmplx_MOL_basic')=='')
			showAlert('cmplx_MOL_basic','MOL Basic Salary cannot be blank');	
		else
			return true;
	}
	
	function partMatch_Save_Check()
	{
		if(getLVWRowCount('cmplx_PartMatch_cmplx_Partmatch_grid')==0)
			showAlert('cmplx_PartMatch_cmplx_Partmatch_grid',alerts_String_Map['PL157']);
		else
			return true;
	}
	
	function WorldCheck_Save_Check()
	{
		if(getLVWRowCount('cmplx_WorldCheck_WorldCheck_Grid')==0)
			showAlert('cmplx_WorldCheck_WorldCheck_Grid',alerts_String_Map['PL158']);
		else
			return true;
	}
	
	function RejectEnq_Save_Check()
	{
		if(getLVWRowCount('cmplx_RejectEnq_RejectEnqGrid')==0)
			showAlert('cmplx_RejectEnq_RejectEnqGrid',alerts_String_Map['PL159']);
		else
			return true;
	}
	
	function SalaryEnq_Save_Check()
	{
		if(getLVWRowCount('cmplx_SalaryEnq_SalGrid')==0)
			showAlert('cmplx_SalaryEnq_SalGrid',alerts_String_Map['PL160']);
		else
			return true;
	}
	
	function Decision_Save_Check()
	{
		if(getNGValue('cmplx_Decision_Decision')=='--Select--')
					showAlert('cmplx_Decision_Decision',alerts_String_Map['PL161']);		
					
		else if(getNGValue('cmplx_Decision_Decision')=='Reject'){
				if(getNGValue('RejectRemarks')=='')
					showAlert('RejectRemarks',alerts_String_Map['PL162']);
				else return true;
		}
		else	
			return true;
	
	return false;
	}
	
		
 function DMandatory()
{
    var activityname = window.parent.stractivityName;
	var activityNameforNotepad = '';
	
	//below lines changed by akshay on 23/11/18
	var newAddedrow=0;
	var ReferTo="";
	
	for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
	{
		if(getLVWAT('Decision_ListView1',i,12)=='')
		{
			
			//below code added by nikhil on 28/11/18 as referto wrong value set on first time
			if(newAddedrow>0)
			{
			ReferTo+=','+getLVWAT('Decision_ListView1',i,6);
			}
			else
			{
			ReferTo+=getLVWAT('Decision_ListView1',i,6);
			}
			newAddedrow++;
		}			
	}
	if(newAddedrow==0)
	{	
		showAlert('cmplx_Decision_Decision', alerts_String_Map['PL165']);
		return false;
	} else {

		setNGValueCustom('q_MailSubject',ReferTo);
		setNGValueCustom('ReferTo',ReferTo); //changed by aman for name change
	
	}//added by shweta for refer cases
	if(getNGValue('cmplx_Decision_Decision')=='Reject' || getNGValue('cmplx_Decision_Decision')=='Refer')  
	{
		if((activityName!='FCU'  || activityName!='FPU'  || activityName!='Compliance'))
		 {
			var desc = getNGValue('cmplx_Decision_Decision');
			if(activityName=='CAD_Analyst2')
			{
				if(flag_add_new==true)
				{
					showAlert('DecisionHistory_Button4',alerts_String_Map['VAL391']);
					return false;
				}
			}
		
			if(activityName=='CAD_Analyst1' && (desc.toUpperCase()=='REFER' ||  desc.toUpperCase()=='REJECT')) //refr commented by jahnavi for 3603 // again uncommented by hritik 
			{
				if(flag_add_new==true)
				{
					showAlert('DecisionHistory_Button4',alerts_String_Map['VAL391']);
					return false;
				}
				
			//changes doen for mail approval PCSP-690
/*			if((getNGValue('cmplx_Decision_ReferTo')!='Smart CPV' && getNGValue('cmplx_Decision_ReferTo')!='Mail Approval') 
			&& (getNGValue('cmplx_Decision_CADDecisiontray')=='' || getNGValue('cmplx_Decision_CADDecisiontray')=='--Select--') && getNGValue('DectechCategory')!='A')
				{
					showAlert('cmplx_Decision_CADDecisiontray',alerts_String_Map['CC271']);
						return false;
				} hritik 27.6.21 PCASI 3526	 */
			}
		 }
		else if((activityName=='FCU'  || activityName=='FPU'  || activityName=='Compliance') && getNGValue('cmplx_Decision_Decision')=='Refer' && validateReferGridonRefer()){		
			return true;
		}	
		
	}
	
	/*if (getNGValue('cmplx_Decision_Decision') == '' || getNGValue('cmplx_Decision_Decision') == '--Select--' || getNGValue('cmplx_Decision_Decision') == null)
	{
		showAlert('cmplx_Decision_Decision', alerts_String_Map['PL165']);
		return false;
	}
	
	else if( (getNGValue('cmplx_Decision_Decision').toUpperCase()=='REFER' || getNGValue('cmplx_Decision_Decision').toUpperCase()=='REJECT'))
	{
		if(getNGValue('cmplx_Decision_Decision').toUpperCase()=='REFER' && (getNGValue('cmplx_Decision_ReferTo')=='' || getNGValue('cmplx_Decision_ReferTo')=='--Select--'))
		{
			showAlert('cmplx_Decision_ReferTo',alerts_String_Map['CC255']);
			return false;
		}
		else if(getNGValue('DecisionHistory_DecisionReasonCode')=='' || getNGValue('DecisionHistory_DecisionReasonCode')=='--Select--')
		{
			showAlert('DecisionHistory_DecisionReasonCode',alerts_String_Map['VAL198']);
			return false;
		}
		else{
			return true;//added by akshay on 17/10/18
		}
	}*/	
  //  alert('decision is:' + getNGValue('cmplx_Decision_Decision'));
  // alert('$' + PLFRAGMENTLOADOPT['ReferHistory'] + '$');
	if(getNGValue('cmplx_Decision_Decision').toUpperCase()=='REFER' && (getNGValue('cmplx_Decision_ReferTo').toUpperCase()=='DDVT_CHECKER'||getNGValue('cmplx_Decision_ReferTo').toUpperCase()=='DISBURSAL_MAKER'||getNGValue('cmplx_Decision_ReferTo').toUpperCase()=='DISBURSAL_CHECKER'||getNGValue('cmplx_Decision_ReferTo').toUpperCase()=='POSTDISBURSAL_MAKER'||getNGValue('cmplx_Decision_ReferTo').toUpperCase()=='POSTDISBURSAL_CHECKER') || getNGValue('cmplx_Decision_ReferTo').toUpperCase()=='DDVT_MAKER' )
	{
		setNGValue('prevws_refer_color','Y');  //by jahnavi for color code orange if wi referred
	}
    if (activityName == 'DDVT_maker')
    {
        if (getNGValue('Is_Customer_Create') == 'Y')
        {
            if (!getNGValue('aecb_call_status') == 'Success')
            {
                showAlert('ExtLiability_Button1', alerts_String_Map['PL163']);
                return false;
            }
        }
		
		
				if(isVisible('RiskRating_Frame1')==false)
			{
			showAlert('RiskRating_Frame1','Please Visit Risk rating section First.');
			return false;			
			}
		if(getNGValue('cmplx_RiskRating_Total_riskScore')=='')
		{
			showAlert('cmplx_RiskRating_Total_riskScore','Risk score cannot be empty');
			return false;
		}
			/*if(getNGValue('cmplx_Customer_NTB')==true && upload_sig==false)
		{
		 showAlert('IncomingDocNew_UploadSig','Please upload signature');
		 return false;
		}*/	
		if(!Customer_Save_Check1()){
				return false;
		}	
		
		else if(!Employment_Save_Check()){
			return false;
			}
		else if(getNGValue('Is_Financial_Summary')!='Y'){
			showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
			return false;
		}
		/*else if(!Income_Save_Check()){
			return false;
		}*/
		else if(getNGValue("cmplx_Customer_minor")=="true"){
			 if (!Minor_PL_Demo_CheckOnDone())
			        return false;
		}
		
		if (isVisible('ELigibiltyAndProductInfo_IFrame4')){
			if(getFromJSPTable('ELigibiltyAndProductInfo_IFrame4')==null){
				com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame4','/webdesktop/custom/CustomJSP/Funding_Account_No.jsp');
			if(getFromJSPTable('ELigibiltyAndProductInfo_IFrame4')!=null){
				var table = getFromJSPTable('ELigibiltyAndProductInfo_IFrame4').rows.length;
				if(table>1){
					var anyFundingAccountSelected = false;
					for(var i=1;i<table;i++){
						if(getFromJSPTable('ELigibiltyAndProductInfo_IFrame4').rows[i].cells[0].childNodes[0].checked){
							anyFundingAccountSelected = true;
							break;
						}
					}
					 if(anyFundingAccountSelected==false)	
						{
							showAlert('ELigibiltyAndProductInfo_IFrame4','Please select a funding account from Funding Account Number grid!!!');
							return false;
						}
					
				}
				}
			}
		}
		
		//below code added by jahnavi for risk rating alert
       // Added by Pooja for PCASI-3888 signature mandatory at ddvt maker
	 if(getNGValue('cmplx_Customer_NTB')==true){
        var rows = getLVWRowCount('cmplx_IncomingDocNew_IncomingDocGrid');
           for(var i=0;i<rows;i++){
             if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,4)!='Received'  &&(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',i,0)=='Signature'))
                 {
                 showAlert("","Please upload Signature")
     	          return false;

                  }
                    }
	}
	   
	   
		
    }
	else if(activityname=='CAD_Analyst1'){
	var desc = getNGValue('cmplx_Decision_Decision');
	
		if(desc=='Approve')
		{
			if( !validatePastDate('cmplx_LoanDetails_frepdate',"Repayment") )
			{
				showAlert("","First repayment date cannot be past date , kindly refer back to DDVT maker" );
				return false;
			}	
			if(getNGValue('ELigibiltyAndProductInfo_EFMS_Status')!='Y')
			{
				showAlert("","Please Refresh EFMS Status");
				return false;	
			}
		}
		/*if(getNGValue('ELigibiltyAndProductInfo_EFMS_Status')!='Y' && activityname=='CAD_Analyst1' && desc=='Approve') /by jahnavi to remove alerts for refer decison
				{
				showAlert(,alerts_String_Map['CC281']);return false;	
				}*/
		//below lines commented by akshay on 23/11/18	
	/*if(desc.toUpperCase()=='REFER' && (getNGValue('cmplx_Decision_ReferTo')=='' || getNGValue('cmplx_Decision_ReferTo')=='--Select--'))
		{
		showAlert('cmplx_Decision_ReferTo',alerts_String_Map['CC255']);
				return false;
		}
		else if(getNGValue('DecisionHistory_DecisionReasonCode')=='' || getNGValue('DecisionHistory_DecisionReasonCode')=='--Select--')
		{
		showAlert('DecisionHistory_DecisionReasonCode',alerts_String_Map['VAL198']);
				return false;
		}*/
		/*if ((getNGValue('cmplx_Decision_CADDecisiontray')=='' || getNGValue('cmplx_Decision_CADDecisiontray')=='Select' ) && getNGValue('DectechCategory')!='A')
		{
		showAlert('cmplx_Decision_CADDecisiontray',alerts_String_Map['CC271']);
				return false;
		}*/
		
		if(!Income_Save_Check()){
			return false;
		}
		/* commented by saurabh to overcome showstopper
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		if(getNGValue('EFMS_IS_Alerted') == '' || getNGValue('EFMS_IS_Alerted').toLowerCase() == 'null' ){
			alert("Cannot submit Workitem as EFMS status not fetched");
			return false;
		}*/
		//-- Above code added  by abhishek on 04/01/2018 for EFMS refresh functionality
	}
	/*else if(activityname=='PostDisbursal_Checker'){
		if(!checkMandatory(PL_POST_DISBURSAL)){
			return false;
		}
		
	}*/
	else if(activityName=="PostDisbursal_Checker") {
		if(!checkMandatory(PL_POST_DISBURSAL)){
			return false;
		}
		if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1 ) {
			if(getNGValue("cmplx_Decision_DecDisbChecker")=='PD' && checkTO =="false")
			{
				checkTO="true";
				showAlert(pId,'Check If TO  step is required');
				return false;
			}	
		}	
	}
	
	
	else if(activityName=='FCU' || activityName=='FPU'){
		if(!checkMandatory(PL_Decision_FCU))
			return false;
	   	 if (getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU')>0){
	   	 
	              for(var i=0;i<getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU');i++)
	   			{
	   				if(getLVWAT('cmplx_SmartCheck1_SmartChkGrid_FCU',i,2)==''){
	   					showAlert('cmplx_SmartCheck1_SmartChkGrid_FCU','Please fill smart check');
	   					return false;
	   			}
	   	  }
	   	}
	   	if(getNGValue('Is_FPU_Generated')!='Y' && fpu_generated==false){
	   		showAlert('Generate_FPU_Report','Please Generate the FPU Report');
	   		return false;
	   	}
		
   	}

	 if(!DMandatoryPL()){
		return false;
	}
	
	if(activityname == 'DDVT_maker'){
		activityNameforNotepad = 'DDVT_Maker';
	}
	else{
		activityNameforNotepad = activityname;
	}
	
	if(!Is_NotepadDetails_Modified(activityNameforNotepad)){
			return false;
	}
	
	//below code commented by akshay on 23/11/18
	/*if(getNGValue('cmplx_Decision_Decision')=='Refer' && com.newgen.omniforms.formviewer.getNgSelectedValues('cmplx_Decision_ReferTo')==''){
        showAlert('cmplx_Decision_ReferTo', alerts_String_Map['CC255']);
        return false;
    }*/
	
	//added by shweta on oct 17-10 
	//3224
	var Decision=getNGValue("cmplx_Decision_Decision")
	if((activityName=='FCU'  || activityName=='FPU'  || activityName=='Compliance' || activityName=='DSA_CSO_Review' || activityName=='CPV_Analyst'  || activityName=='RM_Review') && Decision=='Information Updated')
	{
		if(!validateReferGrid())
		{
			return false;
		}
	}
	
	//ended by akshay on 12/9/17 added fpu/fcu for pcasi3877
		if((getNGValue('cmplx_fieldvisit_sp2_drop1')=="Yes") && (activityName=='FPU' || activityName=='FCU'))
		{
		if(getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate')=="" || getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate')=="--Select--")
		{
			showAlert('cmplx_fieldvisit_sp2_field_rep_receivedDate','Field visit report Recieved date is mandatory');
			return false;
		}
		 if(getNGValue('cmplx_fieldvisit_sp2_field_rep_receivedDate')!=""){//
			if(getNGValue('cmplx_fieldvisit_sp2_field_visit_rec_time')=="")	{
				showAlert("cmplx_fieldvisit_sp2_field_visit_rec_time","Please fill Field visit received time");
				return false;
			}
		 } //JIRA 3036 
		 if(fieldvisit_flag_reci==true )
		 {	
			
			 showAlert('cmplx_fieldvisit_sp2_field_visit_rec_time',"Please update field visit Recieved time");
			 return false;
		 }
		}

    return true;
}
 
//added by nikhil on 12/9/17 added in pl by shweta
	function validateReferGridonRefer()
	{
		var is_grid_modified=true;
		var n=getLVWRowCount('cmplx_ReferHistory_cmplx_GR_ReferHistory');
		var activity = activityName;
		if(n>0){
			for(var i=0;i<n;i++){
				var referFrom = getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,2);
				if(activity=='CPV_Analyst'){activity='CPV Checker';}
				else if(activity=='DSA_CSO_Review' && referFrom=='CSM'){activity='CSO (for documents)';}
				else if(activity=='DSA_CSO_Review'){activity='Source';}
				if(getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,5)==activity && getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)=='Complete' && getNGValue('cmplx_Decision_Decision')=='Refer'){
					is_grid_modified=false;
					
				}
				
			}
		}
		else{
				is_grid_modified=false;
		}
		
		if(is_grid_modified==false){
					showAlert('ReferHistory_status',alerts_String_Map['CC265']);
					return false;
				}
	  return true;	
	
	}
	


function checkMandatory_ReferenceGrid(opType)
{
	var gridrowCount = getLVWRowCount('cmplx_ReferenceDetails_cmplx_ReferenceGrid');
		if(opType == 'add' && gridrowCount==2){
		showAlert('',alerts_String_Map['PL229']);
		setNGValue('ReferenceDetails_ref_NAme',"");
		setNGValue('ReferenceDetails_ref_mobile',"");
		setNGValue('ReferenceDetails_reference_phone',"");
		return false;
		}
	if(getNGValue('ReferenceDetails_ref_NAme')==""){
		showAlert('ReferenceDetails_ref_NAme',alerts_String_Map['PL230']);
		return false;
		}
	else if(getNGValue('ReferenceDetails_ref_mobile')==""){
		showAlert('ReferenceDetails_ref_mobile',alerts_String_Map['PL231']);
		return false;
		}
	/*else if(getNGValue('ReferenceDetails_reference_phone')==""){
		showAlert('ReferenceDetails_reference_phone',alerts_String_Map['PL232']);
		return false;
		}*///Arun (09/10)
	else if(getNGValue('ReferenceDetails_ref_Relationship')=="--Select--" || getNGValue('ReferenceDetails_ref_Relationship')==""){
		showAlert('ReferenceDetails_ref_Relationship',alerts_String_Map['PL233']);
		return false;
		}	
	return true;
} //Arun (09/10)

function AccountSummary_checkMandatory()
	{
		if(getNGValue('cmplx_Customer_CIFNO')=='' || getNGValue('cmplx_Customer_CIFNO')==null){
			showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL234']);	
			return false;
		}
	return true;	
	}	


function DMandatoryPL(){
	
	var activityName = window.parent.stractivityName;
	
	// changes done to make decision mandatory - bug correct --Select-- condition also added
    if (getNGValue('cmplx_Decision_Decision') == '' || getNGValue('cmplx_Decision_Decision') == '--Select--')
    {
        showAlert('cmplx_Decision_Decision', alerts_String_Map['PL251']);
        return false;
    }
	
 // hritik 26.6.21 PCASI - 3377 PCASI - 3561
	var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid");
					var gridValue=[];
					var set=0;
			for(var i=0;i<n;i++)
			{
				if(getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,12)==activityName || (getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,12)=='FCU' && activityName == 'FPU')){
					gridValue.push(getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",i,12));
					var set=i;
				}

			}
			for(var i=0;i<gridValue.length;i++)
				{
					if(gridValue.indexOf(activityName)>-1 || gridValue.indexOf('FCU')>-1 )
					{
						var Actionremark=getLVWAT("cmplx_NotepadDetails_cmplx_notegrid",set,10);
	                		if(Actionremark=='')
	                		{
	                			showAlert('NotepadDetails_Frame2','Please complete the Notepad Instruction detials!!');
								return false;
	                		}
	                	}
                }
	// end
	
	if(activityName=='Compliance')
	{
		var worldAdd = getNGValue('Is_World_Check_Add'); //Corrected ID  flag check if row is added in world check grid
		if(worldAdd=='Y')
		{			  
			var n=getLVWRowCount("cmplx_Compliance_cmplx_gr_compliance");			
				if(n==0){
						showAlert('cmplx_Compliance_cmplx_gr_compliance',alerts_String_Map['PL235']);
						return false;			
				}
				if(n>=1)
				{
					if(getLVWAT("cmplx_Compliance_cmplx_gr_compliance",0,5)=='')
					{
						showAlert('cmplx_Compliance_cmplx_gr_compliance',alerts_String_Map['PL235']);
						return false;
					}
				}
			
		}
		//PCASI - 3721
		if(!checkMandatory_Frames(getNGValue('Mandatory_Frames'))){
			return false;
		}
	/*	if(!checkMandatory_KYC()){
			return false;
		} */
	}
	else if(activityName=='CAD_Analyst1' || activityName=='CAD_Analyst2')
	{
		
		//++below code added by siva on 16102019 for PCAS-2696 CR
		if(activityName=='CAD_Analyst1' && getNGValue('Eligibility_Trigger')=='Y')
		{
			showAlert('ELigibiltyAndProductInfo_Button1',alerts_String_Map['VAL388']);
			return false;
		}
		//--above code added by siva on 16102019 for PCAS-2696 CR
		var Decision=getNGValue("cmplx_Decision_Decision"); // hritik 28.7.21 - PCASI 3735
		if(flag_add_new==true && Decision=='Approve' )//saurabh1 for CAM //and decision added by jahnavi for 3603
		{
			showAlert('DecisionHistory_Button4',alerts_String_Map['VAL391']);
			return false;
		}
		var Decision=getNGValue("cmplx_Decision_Decision"); //by jahnavi for JIRA 3065
		if(activityName=='CAD_Analyst1' && Decision=='Approve')
		{
			if( !validatePastDate('cmplx_LoanDetails_frepdate',"Repayment"))
			{
				showAlert("","First repayment date cannot be past date , kindly refer back to DDVT maker" );
				return false;
			}	
			
		}
		if(activityName=='CAD_Analyst2' && Decision=='Sendback' && flag_add_new==true)
		{
			showAlert('DecisionHistory_Button4',alerts_String_Map['VAL391']);
			return false;
		} // hritik 3735
		
		//added by aman for PCSP-94
		if((getNGValue('cmplx_Customer_VIPFlag')==false || getNGValue('EmploymentType')=='Pensioner') ){
		if(!Income_Save_Check())
				{
				return false;
				}
		}
		
		// disha FSD
		var desc = getNGValue('cmplx_Decision_Decision');
		var highDelegAuth  = getNGValue('cmplx_Decision_Highest_delegauth');
		//alert(highDelegAuth);
	if(getNGValue('cmplx_Decision_Manual_Deviation')==true && activityName=='CAD_Analyst1')
	{
		if(getNGValue('cmplx_Decision_Manual_deviation_reason')=='' )
		{
			showAlert('DecisionHistory_Button6',alerts_String_Map['PL236']);
			return false;
		}
       }
        if(desc=='Refer to Smart CPV')
		{
			var n=getLVWRowCount("cmplx_SmartCheck1_SmartChkGrid_FCU");
			if(n==0)
			{
				showAlert('cmplx_SmartCheck1_SmartChkGrid_FCU',alerts_String_Map['PL237']);
			}			
	}
      //below code added by nikhil for PCAS-2258
		if(desc=='Approve' && getNGValue('cmplx_CustDetailVerification_Decision')=='Approve Sub to CIF')
		{
			var note_grid='cmplx_NotepadDetails_cmplx_notegrid';
			var row_count=getLVWRowCount(note_grid);
			var note_flag=true;
			for(var i=0;i<row_count;i++)
			{
				if(getLVWAT(note_grid,i,1)=='NDDVT' && getLVWAT(note_grid,i,6)=='CAD_Analyst1')
				{
					note_flag=false;
				}
			}
			if(note_flag)
			{
			showAlert(note_grid,alerts_String_Map['PL423']);
				return false;
			}
		}
		 //validation for customer detail verification CPV changes 16-04 - Changed on 26/5/21 - CPV Changes Req 1 (hidden)
/*		 
		if ((getNGValue('cmplx_CustDetailVerification_Decision') == '' || getNGValue('cmplx_CustDetailVerification_Decision') == '--Select--')  && activityName=='CAD_Analyst1' )
		{
			showAlert('cmplx_CustDetailVerification_Decision', alerts_String_Map['CC165']);
			return false;
		}
*/	
	if(desc=='Approve')
	{
		//alert ("Decision approved");
		setLocked('cmplx_Decision_CADDecisiontray',true);		
	}
	else
	{
		setLocked('cmplx_Decision_CADDecisiontray',true); // hritik 29.6.21 PCASI 3526
		
	} //Arun (08/10) for PROC 2773
	
	if(highDelegAuth=='' && getNGValue('DectechCategory')!='A'  && desc=='Approve'){
		showAlert('cmplx_Decision_Highest_delegauth',alerts_String_Map['PL238']);
		return false;
	}
	if(getNGValue('all_call_status')!='' && getNGValue('cmplx_Customer_EmiratesID')!='')
	{
		var fail_status= getNGValue('all_call_status');
		fail_status = fail_status.replace("#",",");
		showAlert('cmplx_Decision_Highest_delegauth','Call failed for : '+fail_status +'  Kindly Re-Run');
			return false;
	}
	if(desc=='' || desc=='--Select--')
	{	showAlert('cmplx_Decision_Decision',alerts_String_Map['PL239']);
		return false;
	}
	if(desc=='Reject')
	{
		if(getNGValue('DecisionHistory_DecisionReasonCode')=='' || getNGValue('DecisionHistory_DecisionReasonCode')=='--Select--')
		{
			showAlert('DecisionHistory_DecisionReasonCode',alerts_String_Map['PL239']);
						return false;
		}
	}
	if(desc=='Refer to Credit')
	{
		if(getNGValue('cmplx_Decision_CADDecisiontray')=='' || getNGValue('cmplx_Decision_CADDecisiontray')=='--Select--')
		{
			showAlert('cmplx_Decision_CADDecisiontray',alerts_String_Map['PL240']);
						return false;
		}
	}
	
	}
	
	else if(activityName=='Smart_CPV')
	{
		if(isVisible('SmartCheck_Frame1')==false)
		{
		showAlert('','Please Visit Smart Section for CPV Remarks');
		return false;
		}
		if(getNGValue('cmplx_Decision_Decision')!='Smart CPV Hold')
		{
			var identifier=false;
			var smart_grid=getLVWRowCount('cmplx_SmartCheck_SmartCheckGrid');
			for(var i=0;i<smart_grid;i++)
			{
				if(getLVWAT('cmplx_SmartCheck_SmartCheckGrid',i ,1)=='')
				{
					identifier=true;
				}
			}
			if(identifier)
			{
				showAlert('','Please complete Remarks for Smart Check Section');
				return false;
			}
		}
		 // added by abhishek as per CC FSD
			/*if(!checkSmartCPVDec()){
				alert('Cannot Submit WorkItem as the decision is Hold');
				return false;
			}*/
	}
	else if(activityName=='FCU'  || activityName=='FPU' )
	{
		//alert("inside FCU");
		var desc = getNGValue('cmplx_Decision_Decision');//cmplx id correcte by shweta
        if(desc=='Refer to Smart CPV')
		{
			var n=getLVWRowCount("cmplx_SmartCheck1_SmartChkGrid_FCU");
			if(n==0)
			{
				showAlert('cmplx_SmartCheck1_SmartChkGrid_FCU',alerts_String_Map['PL242']);
			}
		}
		//added 6th sept for proc 2021
		
			if (getNGValue('cmplx_Decision_Feedbackstatus') == '' || getNGValue('cmplx_Decision_Feedbackstatus') == '--Select--'
			||getNGValue('cmplx_Decision_Feedbackstatus') == null)
			{
				showAlert('cmplx_Decision_Feedbackstatus',alerts_String_Map['PL243']);
				return false;
			}
		if(Fpu_Grid_save_Button==false)	{
			showAlert('Fpu_Grid_Button1','Please click on save button in FPU Grid before processing the wi');
			return false;
		}
		//ended 6th sept for proc 2021
			
	}
	// changes done to make CPV frgament tab decisions mandatory
	else if(activityName=='CPV')
	{
		/* if (getNGValue('cmplx_HCountryVerification_Decision') == '' || getNGValue('cmplx_HCountryVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_HCountryVerification_Decision', alerts_String_Map['PL244']);
			return false;
		}*/   //06-06-2021 PCASI-3141 to remoce the alert
		 if (getNGValue('cmplx_ResiVerification_Decision') == '' || getNGValue('cmplx_ResiVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_ResiVerification_Decision', alerts_String_Map['PL245']);
			return false;
		}
		//below code commented by bandana for 'CM mandatory in CPV Screen' CR
		 /* if (getNGValue('cmplx_CustDetailVerification_Decision') == '' || getNGValue('cmplx_CustDetailVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_CustDetailVerification_Decision', alerts_String_Map['PL246']);
			return false;
		} */
		 if (getNGValue('cmplx_BussVerification_Decision') == '' || getNGValue('cmplx_BussVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_BussVerification_Decision', alerts_String_Map['PL247']);
			return false;
		}
		 if (getNGValue('cmplx_RefDetVerification_Decision') == '' || getNGValue('cmplx_RefDetVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_RefDetVerification_Decision', alerts_String_Map['PL248']);
			return false;
		}
		 //changes done by shweta for jira no PCAS-2290 FLV in cpv tab start
		 if (getNGValue('cmplx_OffVerification_Decision') == '' || getNGValue('cmplx_OffVerification_Decision') == '--Select--'  && !(isLocked('cmplx_OffVerification_Decision') && !isEnabled('OfficeandMobileVerification_Enable')))
		{
			showAlert('cmplx_OffVerification_Decision', alerts_String_Map['PL249']);
			return false;
		}
		 //changes done by shweta for jira no PCAS-2290 FLV in cpv tab end

		 if (getNGValue('cmplx_LoanandCard_Decision') == '' || getNGValue('cmplx_LoanandCard_Decision') == '--Select--')
		{
			showAlert('cmplx_LoanandCard_Decision', alerts_String_Map['PL250']);
			return false;
		}
// changes done to restrict user if any of the decision is pending in CPV tab fragmments than in main decision tab user cannot take Decision as Approve		
		if (getNGValue('cmplx_HCountryVerification_Decision') == 'Pending' || getNGValue('cmplx_ResiVerification_Decision') == 'Pending' || getNGValue('cmplx_BussVerification_Decision') == 'Pending' || getNGValue('cmplx_RefDetVerification_Decision') == 'Pending' || getNGValue('cmplx_OffVerification_Decision') == 'Pending' || getNGValue('cmplx_LoanandCard_Decision') == 'Pending')
		{
			if (getNGValue('cmplx_Decision_Decision') == 'Approve')
			{
				showAlert('cmplx_Decision_Decision', alerts_String_Map['PL252']);
				return false;
			}
		}		
	}
	else if(activityName=='Disbursal_Maker'){
		
		/*var lpf=getNGValue("cmplx_LoanDetails_lpfamt").replace(",", "");
		if(parseFloat(lpf)<500 || parseFloat(lpf)>2500)
		{
			showAlert('cmplx_LoanDetails_lpfamt','LPF Amount should be within 500-2500');
			console.log("Hi");
			return false; only for ddvt maker
		}*/
		 if(getNGValue('cmplx_LoanDetails_is_repaymentSchedule_generated')!='Y'){
			showAlert('LoanDetails_Button1','Please generate repayment Schedule in Loan Details!!');
				return false;
		}
		 if(getLVWAT('cmplx_LoanDetails_cmplx_LoanGrid',0,1)!=getNGValue("cmplx_LoanDetails_frepdate")){
				showAlert('cmplx_LoanDetails_cmplx_LoanGrid','Disbursal Details Due Date should be same as First Repayment Date!!');
					return false;
			}
		if(getLVWAT('cmplx_LoanDetails_cmplx_LoanGrid',0,2)!=getLVWAT("cmplx_LoanDetails_cmplx_LoanGrid",0,3)){
				showAlert('','Payment release Date should be same as disbursal Date!!');
				
					return false;
		}
		/*var lpf=getNGValue('cmplx_LoanDetails_lpfamt');
		var lpf_amt=parseFloat(lpf);
		if(lpf_amt< 500 && lpf_amt>2500)
		{
			showAlert('',"LPF Amount should not be less than 500 and greater than 2500");
			return false;
		}*/
		var Decision=getNGValue("cmplx_Decision_Decision");
		if(Decision=='Approve')
		{
			if( !validatePastDate('cmplx_LoanDetails_frepdate',"Repayment") || getLVWAT('cmplx_LoanDetails_cmplx_LoanGrid',0,1)!=getNGValue("cmplx_LoanDetails_frepdate"))
			{
				showAlert("","First repayment date and due date cannot be past date , kindly refer back to DDVT maker" );
				return false;
			}	
			
		}
		/*if(!LoanDetails_Save_Check())
		{
			return false;
		}*/
	}
	//PCASI 2694
/*	else if(activityName == 'DDVT_maker'){
		if(getNGValue('cmplx_Customer_RM_TL_NAME') == ''){
			showAlert('cmplx_Customer_RM_TL_NAME',"Please fill RM TL Name");
			return false;
		}
	} */
	
	//PCASI 3152
	/*else if(activityName == 'DDVT_maker'  || activityName == 'DDVT_checker'){
		if(getNGValue("is_cc_waiver_require")=="N" && isVisible('ELigibiltyAndProductInfo_Frame6')==false)
		{
			showAlert('ELigibiltyAndProductInfo_Frame6',"Please visit the card details tab");
			return false;   //by ALOK
		}
	}*/
	else if(activityName=='DDVT_maker' && getNGValue("IS_Approve_Cif")!="Y")
	{
		//added by akshay on 21/10/18
		
		//var lpf=getNGValue("cmplx_LoanDetails_lpfamt");
		if(getNGValue("IS_Approve_Cif")!="Y")
		{
		/*if(parseInt(lpf)<500 || parseInt(lpf)>2500)
		{
			showAlert('cmplx_LoanDetails_lpfamt','LPF Amount should be within 500-2500');
			console.log("Hi");
			return false;       //by jahnavi for 2677
		}*/
		}
		
		if(getNGValue('cmplx_Customer_NTB')==true && !checkMandatory(PL_RISKRATING))
		{
			return false;
		}
		if(getNGValue('cmplx_Customer_NTB')==true && flag_firco==false && getNGValue('cmplx_Decision_Decision')!='Refer')
		{
			showAlert("Customer_FetchFirco","Please click on fetch firco button");
			return false;
		}
		if(!checkMandatory_AddressDetails_Save()){
			return false;
		}
		
		else if(!checkForNA_AddressGrid()){
			return false;
		}	
		else if(!ContactDetails_Save_Check()){
			return false;
		}
		else if(!checkMandatory_CardDetails_Save() && !getLVWRowCount('cmplx_CardDetails_cmplx_CardCRNDetails')==0){
				return false;
		}	
		else if(!checkFor_SupplemntGrid()){
			return false;
		}		
		else if(!checkMandatory_FATCA()){
			return false;
		}
		else if(!checkMandatory_KYC()){
			return false;
		}
		else if(!checkMandatory_OECD()){
			return false;
		}
		else if(!checkMandatory_FinacleCRM()){
			return false;
		}
		
		else if(!LoanDetails_Save_Check())
		{
			return false;
		}
		else if(!checkMandatory(PL_LoanDetail_Save)){
			return false;
		}	
		

		else if(!checkMandatory_IMD())
		{
			return false;
		}
		
		else if(!IMD_Save_Check())
		{
				return false;
		}
		var row_count=getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');
		var cif_found=false;
		var ntb_case=false;
		if(row_count>0)
		{
			for(var i=0;i<=row_count;i++)
			{
				if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)!=getNGValue('cmplx_Customer_CIFNO') && getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=="true" )
				{
					cif_found=true;
				}
				if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=="true" )
				{
					ntb_case=true;
				}
			}
						//change by saurabh on 20th Dec
				if(ntb_case &&  getNGValue('cmplx_Customer_NTB')==true && getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)!=''){
					showAlert('cmplx_Customer_NTB', ' Customer cannot be NTB when cif has been identified in Part Match');
					setNGValueCustom('cmplx_Customer_NTB',false);
					return false;
			}
			/*if(cif_found)
			{
			showAlert('cmplx_Customer_CIFNO', ' CIF which is considered for obligations is not preset in Personal details');
			return false;
			}*/
		}
		if(!validateApprovedLimitSupplementary()){
			return false;
		}
		
		
		if(getNGValue('is_cc_waiver_require')=='N'){//changed from LI to PU by akshay on 3/5/18 for proc 8978
			var crngridcount = getLVWRowCount('cmplx_CardDetails_cmplx_CardCRNDetails');
			if(crngridcount>0){
				for(var i=0;i<crngridcount;i++){
					//condition added by akshay on 31/3/18
					if(getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,1)=='' || getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,2)==''){
					showAlert('cmplx_CardDetails_cmplx_CardCRNDetails', alerts_String_Map['CC263']+ getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,0));
					return false;
					}
					if(getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,4)==''){
					showAlert('cmplx_CardDetails_cmplx_CardCRNDetails', ' Please modify Interest Profile for Eligible cards');
					return false;
					}
					if(getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,0).indexOf('KALYAN')>-1 && getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,8)==''){
					showAlert('cmplx_CardDetails_cmplx_CardCRNDetails', ' Please generate KRN number for '+getLVWAT('cmplx_CardDetails_cmplx_CardCRNDetails',i,0));
					return false;
					}
				}
			}
			var SecurityGridCount=getLVWRowCount('cmplx_CardDetails_cmpmx_gr_cardDetails');
			if(SecurityGridCount==0  && !Check_Elite_Customer() && getNGValue('cmplx_Customer_VIPFlag')==false){//by shweta 
				showAlert('CardDetails_BankName', alerts_String_Map['CC264']);
				return false;
			}
		
		}
		if(getLVWAT('cmplx_LoanDetails_cmplx_LoanGrid',0,1)!=getNGValue("cmplx_LoanDetails_frepdate")){
			showAlert('cmplx_LoanDetails_cmplx_LoanGrid','Disbursal Details Due Date should be same as First Repayment Date!!');
				return false;
		}
		
		// added by hritik 10/6/21
		
		if(activityName=='DDVT_maker' && (getNGValue('Is_CustLock')=='N' || getNGValue('Is_CustLock')=='') && (getNGValue('cmplx_Customer_NTB')!=true))
		{
					showAlert('PartMatch_CifLock',"CIF lock is Mandatory");
					com.newgen.omniforms.formviewer.setNGFocus('PartMatch_CifLock');
					//setNGFrameState('ProductContainer',1);
					return false;
	    }
		// Hritik 24/6/21 PCASI-3516
		else if (activityName=='DDVT_maker')
	    {
		    
			var count=getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');
			for(var i=0;i<count;i++)
			{
				if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)!='' && getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)!='true' && getNGValue('cmplx_Customer_NTB')!=true)
				{
					showAlert('cmplx_FinacleCRMCustInfo_FincustGrid','Consider for Obligations is mandatory for existing cif customers');
					return false;
				}
			}
			if((getNGValue('cmplx_EmploymentDetails_channelcode')=='' || getNGValue('cmplx_EmploymentDetails_channelcode')=='--Select--'))
				{
					showAlert('cmplx_EmploymentDetails_channelcode',alerts_String_Map['PL042']);
					return false;
				}
			if(getNGValue("is_cc_waiver_require")=="N" && isVisible('ELigibiltyAndProductInfo_Frame6')==false)
				{
				showAlert('ELigibiltyAndProductInfo_Frame6',"Please visit the card details tab");
				return false;   //by ALOK
				}
				//PCASI 2694
			if(getNGValue('cmplx_Customer_RM_TL_NAME') == '')
				{
					showAlert('cmplx_Customer_RM_TL_NAME',"Please fill RM TL Name");
					return false;
				}
	    }
		
		/*if(getNGValue('all_call_status')!='' && getNGValue('cmplx_Customer_EmiratesID')!='')
		{
			var fail_status= getNGValue('all_call_status');
			fail_status = fail_status.replace("#",",");
			showAlert('cmplx_Decision_Highest_delegauth','Call failed for : '+fail_status +'  Kindly Re-Run');
				return false;
		}*/
	}
	
	//added by akshay on 21/5/18 for mandating cif to be created
	else if(activityName=='DDVT_Checker')
	{
		var primary_index=0;
		for(var i=0;i<getLVWRowCount('cmplx_Decision_MultipleApplicantsGrid');i++)
		{
		
			if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',i,0)=='Primary')
			{
			primary_index=i;
			}
			if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',i,3)==''){
					showAlert('','CIF for applicant: '+getLVWAT('cmplx_Decision_MultipleApplicantsGrid',i,1).bold()+' not created!!');
					return false;
			}
		}	
		if(getNGValue("is_cc_waiver_require")=="N" && isVisible('ELigibiltyAndProductInfo_Frame6')==false)
		{
			showAlert('ELigibiltyAndProductInfo_Frame6',"Please visit the card details tab");
			return false;   //by ALOK
		}
		if(!checkMandatory_Frames("LoanDetails:Loan Details:LoanDetails_Frame1")){
			return false;
		}
		//added by akshay on 2/7/18 for proc 11500
		
		//Placed outside below if for PCASI - 3615
		if(getNGValue("cmplx_Decision_Decision") == 'Approve' )	//PCASI - 3632
		{
			if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',primary_index,7)!='Y' || cif_update_flag!='Y')
			{
				showAlert('DecisionHistory_Button3', 'Customer Update not done!!!');
				return false;
			}
			else if(getNGValue('Is_ACCOUNT_MAINTENANCE_REQ')!='Y')
			{
				showAlert('DecisionHistory_updcust', 'Account Update not done!!!');
				return false;
			}
			if((getNGValue('Is_CHEQUE_BOOK_ELIGIBILITY')!='Y' || getNGValue('Is_NEW_CARD_REQ')!='Y') 
				&&(getNGValue('Account_Number')=='') 
				&&(getNGValue('cmplx_Customer_NTB') == true) 
				&&(getNGValue('AlternateContactDetails_RetainAccIfLoanReq')==true))
			{
				showAlert('DecisionHistory_chqbook', 'Cheque book/Debit Card Request button not clicked!!!');
				return false;
			}
		}
		if(getNGValue("cmplx_Decision_Decision") == 'Refer'){
				if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',primary_index,7)=='Y' || cif_update_flag=='Y')
			{
				showAlert('cmplx_Decision_Decision', 'CIF update is already done, Please take decision as Approve');
				return false;
			}
		}// if cif update is done & user try to refer this alert come pcasi 3680
		
		if(getNGValue('cmplx_PartMatch_CIF_Lock_Status')=='Y' && (getNGValue('cmplx_Customer_NTB')!=true)) //pcasi3559
			{
				showAlert('PartMatch_CifUnlock',"CIF Unlock is Mandatory");
				com.newgen.omniforms.formviewer.setNGFocus('DecisionHistory_CifUnlock');
				//setNGFrameState('ProductContainer',1);
				return false;
		    }
		//below condition added by bandana for PCASI-2671
		if(getNGValue('Is_CustLock')=='Y' && (getNGValue('cmplx_Customer_NTB')!=true)){
			showAlert('PartMatch_CifUnlock','Please unlock the CIF');
				return false;		
		}	
		
		
	}
	// ++ below code already present - 09-10-2017 - code has been commented cauz PL_DECISION constant defined and decision is mandatory on done
	if(activityName!='Initiation')
	{
		var mField =  PL_DECISION.split(",");//CC_DECISION_MANDATORY_FIELDS.split(",");--> commented by saurabh on 12th sept as constant not defined.
		for(var i = 0; i < mField.length; i++) {
			var j = mField[i].toString().split("#");
				if(!isFieldFilled(j[0],j[1])){
				showAlert(j[0],j[1]+alerts_String_Map['PL253']);
				return false;				
				}
		}
		//added by saurabh on 1st Feb
		if(activityName=='CSM'){
			if(!Customer_Save_Check1()){
				return false;
			}
			if(!validateApprovedLimitSupplementary()){
				return false;
			}
			if(getNGValue("cmplx_Customer_minor")=="true"){
				 if (!Minor_PL_Demo_CheckOnDone())
				        return false;
			}
			//change by shweta for incoming doc defer waiv functionality. 5 Feb 19.
			Check_Documents_Submit();
			if (isVisible('ELigibiltyAndProductInfo_IFrame2')){

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
							break;
						}
					}
					//below condition added by akshay on 20/9/18
					 if(anyCardSelected==false && cc_waiv==false)	
					{
						showAlert('ELigibiltyAndProductInfo_IFrame2','Please select a card from eligibile cards grid!!!');
						return false;
					}
					 else if ((cc_waiv==false)&&  anyCardSelected){
							if (!checkMandatory_CardDetails_Save())
								return false;
					}
				}
			}
		}

				//added by aman for Drop4 on 4th April	
		if (getNGValue('cmplx_CardDetails_suppcardreq')=='Yes'){
			var gridrowcount = getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
			var SupplementRow=getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid');
			var addressFound = '';
			var addressapplicantsList = [];
			var appType = document.getElementById('AddressDetails_CustomerType');
			
			for(var i=0;i<gridrowcount;i++){
			addressapplicantsList.push(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13));
			}
			
			for(var j=0;j<appType.options.length;j++){
				if(appType.options[j].value!='--Select--' && addressapplicantsList.indexOf(appType.options[j].value)==-1){
				showAlert('DecisionHistory_DecisionReasonCode','Please add Address Details for '+appType.options[j].value);
				return false;	
				}
			}
			/*for(var i=0;i<gridrowcount;i++){
				if ((getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('S-')>-1)){
				//applicantsList.push(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13));
				if(applicantsList.indexOf(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13))>-1){
					addressFound='Y';
				}
				
				if (SupplementRow>0 && addressFound!='Y'){
				showAlert('DecisionHistory_DecisionReasonCode','Please add Address Details for Supplementary customer');
				return false;
				}
				
					//break;
				}
			}*/
		}
		//added by aman for Drop4 on 4th April
		}
	}
	//below code addedby bandana for bundled products mention in email
	//hritik changes as per jira 3799 
	if (getNGValue('is_cc_waiver_require')=='Y')
	{
	if (getNGValue('loan_type')=='Islamic'){
		setNGValue('StatusType', 'Personal Finance');
	}
	else{
		setNGValue('StatusType', 'Personal Loan');
	}
	}
	if (getNGValue('is_cc_waiver_require')!='Y'){
		if (getNGValue('loan_type')=='Islamic'){
			setNGValue('StatusType', 'Personal Finance, Credit Card');
		}
	else{
		setNGValue('StatusType', 'Personal Loan, Credit Card');
		}
	}
	if (getNGValue('loan_type')=='Islamic'){
		setNGValue('Workitem_ID', 'islamic');
	}else{
		setNGValue('Workitem_ID', 'BANK');
	}//code for bundled products mention in email ends
		return true;
} //Arun (07/09/17)

function checkMandatory_Partmatch()
	{
			if(getNGValue('PartMatch_fname')=="")
			{
				showAlert('PartMatch_fname',alerts_String_Map['PL010']);
				return false;
			}
				
			else if(getNGValue('PartMatch_lname')==""){
				showAlert('PartMatch_lname',alerts_String_Map['PL011']);
				return false;
				}
				
		/*	else if(getNGValue('PartMatch_funame')==""){
				showAlert('PartMatch_funame',alerts_String_Map['PL254']);
				return false;
				}
			*/	
			else if( getNGValue('PartMatch_newpass')==''){
				showAlert('PartMatch_newpass',alerts_String_Map['PL255']);	
				return false;
			}
			
		/*	else if(getNGValue('PartMatch_oldpass')==''){
				showAlert('PartMatch_visafno',alerts_String_Map['PL256']);
				return false;
			}*/	
			else if(getNGValue('PartMatch_mno1')==''){
				showAlert('PartMatch_mno1',alerts_String_Map['PL090']);
				return false;
			}
			
			else if(getNGValue('PartMatch_Dob')==''){
				showAlert('PartMatch_Dob',alerts_String_Map['PL012']);
				return false;
			}
		/*	if(getNGValue('cmplx_Customer_CardNotAvailable')==true )
			{
				if(getNGValue('PartMatch_EID')==''){
					showAlert('PartMatch_EID',alerts_String_Map['PL007']);
					return false;
				}	
			}
			else if(getNGValue('PartMatch_drno')==''){
				showAlert('PartMatch_drno',alerts_String_Map['PL257']);
				return false;
			}	
			else if(getNGValue('PartMatch_nationality')==''){
				showAlert('PartMatch_nationality',alerts_String_Map['PL013']);
				return false;
			}	
			else if(getNGValue('PartMatch_CIFID')==''){
				showAlert('PartMatch_CIFID',alerts_String_Map['PL234']);
				return false;
			}
		*/	
return true;			
	}      				
	
	
 /* function Address_Validate()
	{
		var flag_address=false;
		var AddType=getNGValue("addtype");
		var preffAddr=getNGValue('PreferredAddress');
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		var CustomeType=getNGValue('AddressDetails_CustomerType');
		var n = getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		if(n>0)
		{
			for(var i=0;i<n;i++){
				var grid_AddType=com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				var grid_preffAddr=com.newgen.omniforms.formviewer.getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,10);
				var grid_CustomeType=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,13);
				if(AddType==grid_AddType && CustomeType==grid_CustomeType){
					showAlert('addtype',"Cannot add two "+ AddType+" Addresses");	
				}
				else if(preffAddr==true && grid_preffAddr==preffAddr.toString()  && CustomeType==grid_CustomeType)
					showAlert('',alerts_String_Map['PL353']);
					
				else
					flag_address = true;
			}		
		}			

		else
			flag_address = true;		
			
		return flag_address;
	} */
	function Address_Validate(opType)
	{
		var flag_address=false;
		var AddType=getNGValue("AddressDetails_addtype");
		var CustomeType=getNGValue('AddressDetails_CustomerType');//added by prabhakar
		var preffAddr=getNGValue('AddressDetails_PreferredAddress');
		var n = getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		if(n>0)
		{
			for(var i=0;i<n;i++){
				var grid_AddType=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				var grid_CustomeType=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,13);//added by prabhakar
				var grid_preffAddr=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,10);
				if(AddType==grid_AddType && opType == 'add' && CustomeType==grid_CustomeType){
					showAlert('AddressDetails_addtype',"Cannot add two "+ AddType+" Addresses for Same Applicant ");	
					return false;
	}
				else if(preffAddr==true && AddType=='Home'){
					showAlert('AddressDetails_addtype',alerts_String_Map['VAL066']);
					return false;
				}
				else if(preffAddr==true && grid_preffAddr==preffAddr.toString() && CustomeType==grid_CustomeType && opType == 'add'){
					showAlert('AddressDetails_addtype',alerts_String_Map['VAL141']);
					return false;
				}	
				else
					flag_address = true;
			}		
		}						
		else
		{
			if(preffAddr==true && AddType=='Home'){
				showAlert('AddressDetails_addtype',alerts_String_Map['VAL066']);
				return false;
			}
			else{
			flag_address = true;		
			}
		}
	
		return flag_address;
	}
	function Product_modify_validate()
{
		var ReqProd=getNGValue("ReqProd");
		var EmpType=getNGValue("EmpType");
		var priority=getNGValue("Priority");
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		
		if(n==1)
		{
			if(priority!='Primary'){
				showAlert('Priority',alerts_String_Map['PL354']);
				return false;
			}	
			else 
				return true;
		}	
		
		else if(n>1)
			{
				for(var i=0;i<n;i++)
				{
					var grid_EmpType=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,5);
					var grid_Priority=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9);
					if(grid_EmpType!=EmpType && grid_EmpType!="--Select--" && EmpType!="--Select--"){
						showAlert('EmpType',alerts_String_Map['PL355']);
						return false;
						}
					var grid_Prod=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,1);
					
					if(grid_Prod=="Personal Loan" && grid_Prod==ReqProd && com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),1)!=ReqProd){
						showAlert('ReqProd',alerts_String_Map['PL356']);
						return false;
					}	
					
					if(grid_Priority=="Primary" && grid_Priority==priority){
								showAlert('Priority',alerts_String_Map['PL357']);
								return false;
					}			
				}
			}
			
		if(ReqProd=='Personal Loan')
			setNGValue('LimitAcc','NA');
		
		else if(ReqProd=='Credit Card')	{
			setNGValue('ReqTenor','NA');
		}

		return true;
}

function Product_add_validate()
{
		var ReqProd=getNGValue("ReqProd");
		var EmpType=getNGValue("EmpType");
		var priority=getNGValue("Priority");
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		
		if(n==0)
		{
			if(priority!='Primary'){
				showAlert('Priority',alerts_String_Map['PL354']);
				return false;
			}	
			else
				return true;
		}		

		else if(n>0)
			{
				for(var i=0;i<n;i++)
				{
					var grid_EmpType=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,5);
					var grid_Priority=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9);
					
					if(grid_EmpType!=EmpType && grid_EmpType!="--Select--" && EmpType!="--Select--"){
						showAlert('EmpType',alerts_String_Map['PL355']);
						
						return false;
						}
					var grid_Prod=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,1);
					
					if(grid_Prod=="Personal Loan" && grid_Prod==ReqProd){
						showAlert('ReqProd',alerts_String_Map['PL356']);
						return false;
					}	
					
					if(grid_Priority=="Primary" && grid_Priority==priority){
								showAlert('Priority',alerts_String_Map['PL357']);
								return false;
					}			
				}
			}
			
		if(ReqProd=='Personal Loan')
			setNGValue('LimitAcc','NA');
		
		else if(ReqProd=='Credit Card')	{
			//setNGValue('Scheme','NA');
			setNGValue('ReqTenor','NA');
		}

	return true;
}

function NEW_ACCOUNT_REQ_checkMandatory()
	{
		if(getNGValue('cmplx_Customer_CIFNO')=='' || getNGValue('cmplx_Customer_CIFNO')==null){
			showAlert('cmplx_Customer_CIFNO','');
			return false;
		}
		
		else if(getNGValue('cmplx_Decision_IBAN')==""){
			showAlert('cmplx_Decision_IBAN',alerts_String_Map['PL258']);	
			return false;
		}
	return true;
	}
	
	function checkMandatory_OecdGrid()
	{
		 //Added By prabhakar
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			//var custTypePickList = document.getElementById("OECD_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			var CustType=getNGValue("OECD_CustomerType");
			var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_OECD_cmplx_GR_OecdDetails",i,8));
			}if(getNGValue('OECD_CustomerType')=='--Select--' || getNGValue('OECD_CustomerType')=='')
		{
					showAlert('OECD_CustomerType','Applicant type can not be blank');
					return false;
		}
		else if(getNGValue('OECD_CRSFlag')=='--Select--' || getNGValue('OECD_CRSFlag')==''){
					showAlert('OECD_CRSFlag',alerts_String_Map['PL259']);
					return false;
		}
		else if(getNGValue('OECD_CRSFlag')=='Y' || getNGValue('OECD_CRSFlag')=='Y'){
					if(getNGValue('OECD_CRSFlagReason')=='--Select--' || getNGValue('OECD_CRSFlagReason')=='' || getNGValue('OECD_CRSFlagReason')=='NA' ){
						showAlert('OECD_CRSFlagReason',alerts_String_Map['PL117']);
						return false;
					}
					if( getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')=='' ){
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['PL119']);
					return false;
					}
					if(getNGValue('OECD_CountryBirth')=='--Select--' || getNGValue('OECD_CountryBirth')==''){
				     showAlert('OECD_CountryBirth',alerts_String_Map['PL114']);
					return false;
					}
					if(getNGValue('OECD_townBirth')=='--Select--' || getNGValue('OECD_townBirth')==''){
								showAlert('OECD_townBirth',alerts_String_Map['PL113']);
					return false;
					}
					 if(getNGValue('OECD_noTinReason')=='' && getNGValue('OECD_tinNo')==''){
						showAlert('OECD_noTinReason',alerts_String_Map['VAL092']);
						return false;
					}
		}
		else if(getNGValue('OECD_CRSFlag')=='N' || getNGValue('OECD_CRSFlag')=='No')
		{
					if(getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')=='' ){
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['PL119']);
					return false;
		}
					if(getNGValue('OECD_tinNo')==''  && getNGValue('OECD_noTinReason')=='' ){
						showAlert('OECD_tinNo',alerts_String_Map['PL121']);
					return false;
					}
		}
		
		if(gridValue.length>=3)
			{
				showAlert('OECD_CustomerType','OECD Already Added for Customer'+CustType);
				return false;
			}
		
		/*for(var i=0;i<gridValue.length;i++)
		{
			if(gridValue.indexOf(CustType)>-1)
			{
				showAlert('OECD_CustomerType','OECD Already Added for Customer'+CustType);
				return false;
				break;
			}
		}*///commented by Alok

		return true;
	}
//code by bandana starts
		// added by abhishek as per CC FSD		
function validate_BTGrid()
{
	var n=getLVWRowCount("cmplx_CC_Loan_cmplx_btc");
	/*if(n==4){
		showAlert('',alerts_String_Map['VAL023']);
		return false;
	}*/
	 if(n>0){
		for(var i=0;i<n;i++)
		{
			
			
			if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype') && 
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,2)==getNGValue('transferMode') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)==getNGValue('creditcardNo') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,19)==getNGValue('cmplx_CC_Loan_BTC_CardProduct') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,22)==getNGValue('Account_No_for_Swift') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,23)==getNGValue('Account_No_for_AT') )
					{//Added by shweta for pcasp-1401
					  showAlert('transtype',alerts_String_Map['VAL142']);	
					  return false;
				    }
			
			/*if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype') && getLVWAT('cmplx_CC_Loan_cmplx_btc',i,2)==getNGValue('transferMode') && getNGValue('transtype')=='BT' && getNGValue('transferMode')!='A' &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)==getNGValue('creditcardNo') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,19)==getNGValue('cmplx_CC_Loan_BTC_CardProduct') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,22)==getNGValue('Account_No_for_Swift') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,23)==getNGValue('Account_No_for_AT')){//Added by shweta for pcasp-1401
				  showAlert('transtype',alerts_String_Map['VAL142']);	
				return false;
			  }
			else if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype') && getLVWAT('cmplx_CC_Loan_cmplx_btc',i,19)==getNGValue('cmplx_CC_Loan_BTC_CardProduct')){//Added by shweta for pcasp-1401
				showAlert('transtype',alerts_String_Map['VAL142']);	
				*/
		}
	}	
	
	return true;
}

function validate_BTGrid_Modify()
{
	var n=getLVWRowCount("cmplx_CC_Loan_cmplx_btc");
		/*if(n==4){
			showAlert('',alerts_String_Map['VAL023']);
			return false;
		}*/
		 if(n>0){
			for(var i=0;i<n;i++)
			{
			   /*if(!getLVWAT('cmplx_CC_Loan_cmplx_btc',parseInt(com.newgen.omniforms.formviewer.getNgSelectedIndices('cmplx_CC_Loan_cmplx_btc')),0)==getNGValue('transtype')){
				   
				   if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype') && getLVWAT('cmplx_CC_Loan_cmplx_btc',i,2)==getNGValue('transferMode') 
					&& getNGValue('transtype')=='BT' && getNGValue('transferMode')!='A'){
					  showAlert('transtype',alerts_String_Map['VAL142']);	
					return false;
				  }
					if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype')){
						showAlert('transtype',alerts_String_Map['VAL142']);	
						return false;
					}
			    }*/
				// below changes done by shivang for PCASP-3191
				if((i!=parseInt(com.newgen.omniforms.formviewer.getNgSelectedIndices('cmplx_CC_Loan_cmplx_btc'))) && 
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype') && 
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,2)==getNGValue('transferMode') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,1)==getNGValue('creditcardNo') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,19)==getNGValue('cmplx_CC_Loan_BTC_CardProduct') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,22)==getNGValue('Account_No_for_Swift') &&
					getLVWAT('cmplx_CC_Loan_cmplx_btc',i,23)==getNGValue('Account_No_for_AT') )
					{//Added by shweta for pcasp-1401
					  showAlert('transtype',alerts_String_Map['VAL142']);	
					  return false;
				    }
			}
			}
			
			
		
		return true;
}

	
	function checkMandatory_BTGrid()
{	
		
		if(check_bt_modify==false)
		{
		if(getNGValue('cmplx_Customer_NTB')=='false' || !getNGValue('cmplx_Customer_NTB'))//PCASP-3191
		{
			showAlert('','Service requests currently are available only for NTB customers in CAS');
			return false;
		}
		}
		// Below code added by shweta for pcasp-1377
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
			
	}
	 //Below added by Shweta for PCASP-1377
		else if(getNGValue('transtype')=='CCC' ){
		 /*if(!checkMandatory(CC_Loan_CCC)){
		  return false;
		 }*/
			if(getNGValue('transferMode')==null ||  getNGValue('transferMode')=='--Select--' || getNGValue('transferMode')=='' ){
				showAlert('transferMode','Please Select Transfer Mode');
				return false;	
			} else if(getNGValue('transferMode')=='S'){
				 if(!checkMandatory(CC_Loan_CCC_S)){
				  return false;
				 } 	
			}else if(getNGValue('transferMode')=='C'){
		 		 if(!checkMandatory(CC_Loan_CCC_C)){
		 			 return false;
		 		 }
			}else if(getNGValue('transferMode')=='A'){
		 		 if(!checkMandatory(CC_Loan_CCC_A)){
		 			 return false;
		 		 }
			}	
		}
	// Above code added by Shweta for PCASP-1377
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
		  if(getNGValue('Account_No_for_Swift').length <13|| getNGValue('Account_No_for_Swift').length >13){//pcasp-1377
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
	  if(getNGValue('transferMode')=='S'){
		 		 if(getNGValue('CC_Loan_Bank_code')==''){
				 showAlert('CC_Loan_Bank_code', 'Bank Code is Mandatory for Swift!');
					setEnabled('CC_Loan_Bank_code',true);
				 
		 			  return false;
		 		 }
			}
	 if(getNGValue('transtype')=='SC' || getNGValue('transtype')=='CCC' && getNGValue('transferMode')=='C' || getNGValue('transferMode')=='A'){
		 if(getNGValue('creditcardNo').length <13 || getNGValue('creditcardNo').length >13){//pcasp-1377
				showAlert('creditcardNo', 'Please Enter a valid Account Number.');  
				return false;
			}
	 }
	/*if(getNGValue('transferMode')=='C')
	 {
	 	 if(!checkMandatory(CC_Loan_Cheque)){
		  return false;
	 }
	 }*/
	return true;
}

//code by bandana ends	
	/*function checkMandatory_SupplementGrid()
	{
		if(getNGValue('FirstName')==''){
				showAlert('FirstName',alerts_String_Map['PL010']);
				return false;
		}		
				
		else if(getNGValue('lastname')==''){
					showAlert('lastname',alerts_String_Map['PL011']);
					return false;
		}			
					
		else if(getNGValue('passportNo')==''){
					showAlert('passportNo',alerts_String_Map['PL015']);
					return false;
		}			
					
		else if(getNGValue('DOB')==''){
					showAlert('DOB',alerts_String_Map['PL012']);//[val012]
					return false;
		}			
					
		else if(getNGValue('nationality')=='--Select--'){
					showAlert('nationality',alerts_String_Map['PL013']);
					return false;
		}			
					
		else if(getNGValue('MobNo')==''){
					showAlert('MobNo',alerts_String_Map['PL014']);	
					return false;
		}			
		
		else if(getNGValue('ResdCountry')==''){
			showAlert('ResdCountry',alerts_String_Map['PL260']);
			return false;
		}	
		
		else if(getNGValue('gender')=='--Select--'){
					showAlert('gender',alerts_String_Map['PL019']);
					return false;
			}		
					
		else if(getNGValue('relationship')=='--Select--'){
			showAlert('relationship',alerts_String_Map['PL261']);
			return false;
		}		
				
		else if(getNGValue('cardEmbName')==''){
					showAlert('cardEmbName',alerts_String_Map['PL098']);
					return false;
		}		
					
		else if(getNGValue('CompEmbName')==''){
				showAlert('CompEmbName',alerts_String_Map['PL099']);
				return false;
			}	
	return true;				
	}*/
	
	function checkMandatory_Address()
{
		 var AddType=getNGValue("AddressDetails_addtype");
		 var POBox=getNGValue("AddressDetails_pobox");
		 var houseNo=getNGValue("AddressDetails_house");
		 var BuildName=getNGValue("AddressDetails_buildname");
		 var Streetname=getNGValue("AddressDetails_street");
		 var landmark=getNGValue("AddressDetails_landmark");
		 var city=getNGValue("AddressDetails_city");
		 var state=getNGValue("AddressDetails_state");
		 var country=getNGValue("AddressDetails_country");
		 var YearsATcurrent=getNGValue("AddressDetails_years");
		 var prefAdd=getNGValue("AddressDetails_PreferredAddress");
		 var CustomerType=getNGValue("AddressDetails_CustomerType");
		if(CustomerType==""||CustomerType=="--Select--")
			{
			showAlert('AddressDetails_CustomerType','Applicant Type can not be blank');	
			return false;
			}
		else if((AddType=='--Select--')||(AddType=='')){
			showAlert('AddressDetails_addtype',alerts_String_Map['VAL002']);
			return false;
		}	
		else if(AddType.toUpperCase()=='OFFICE')
		{
			if(BuildName == ""){
			showAlert('AddressDetails_buildname',alerts_String_Map['VAL019']);	
				return false;
			}
			 if(country=="" || country=="--Select--"){
				showAlert('AddressDetails_country',alerts_String_Map['VAL040']);	
				return false;
				}
			else if(houseNo ==""){
			showAlert('AddressDetails_house',alerts_String_Map['VAL063']);	
				return false;
			}		
			else if(state=="--Select--" || state==""){
				showAlert('AddressDetails_state',alerts_String_Map['VAL137']);	
				return false;
				}
				//changed done for CR PCSP-651
			/*else if(landmark=="" ){
				showAlert('AddressDetails_landmark',alerts_String_Map['VAL075']);	
				return false;
				}*/	
			else if(POBox==""){
				showAlert('AddressDetails_pobox',alerts_String_Map['VAL122']);	
				return false;
				}
			/*	else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
				return false;
				} */
			else if(!prefcheck()){
			showAlert('AddressDetails_PreferredAddress',alerts_String_Map['VAL141']);
				return false;
			}
		}
		else if(AddType.toUpperCase()=='RESIDENCE' || AddType.toUpperCase()=='MAILING')
		{
			 if(BuildName == ""){
			showAlert('AddressDetails_buildname',alerts_String_Map['VAL019']);	
				return false;
			}
			else if(houseNo ==""){
			showAlert('AddressDetails_house',alerts_String_Map['VAL063']);	
				return false;
			}		
			else if(Streetname==""){
				showAlert('AddressDetails_street','StreetName cannot be blank');	
				return false;
				}	
				//changed done for CR PCSP-651
			/*else if(landmark=="" ){
				showAlert('AddressDetails_landmark',alerts_String_Map['VAL075']);	
				return false;
				}	*/
			//else if(city=="" || city=="--Select--"){
				//showAlert('AddressDetails_city',alerts_String_Map['VAL008']);	
				//return false;
				//}	
			else if( state=="--Select--" || state==""){
				showAlert('AddressDetails_state',alerts_String_Map['VAL137']);	
				return false;
				}
			else if(country=="" || country=="--Select--"){
				showAlert('AddressDetails_country',alerts_String_Map['VAL040']);	
				return false;
				}
			/*	else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
				return false;
				} */
			if(POBox==""){
			showAlert('AddressDetails_pobox',alerts_String_Map['VAL122']);	
			return false;
			}
			else if(prefAdd==true)
		{
			if(POBox==""){
			showAlert('AddressDetails_pobox',alerts_String_Map['VAL122']);	
			return false;
			}
			if(!prefcheck()){
			showAlert('AddressDetails_PreferredAddress',alerts_String_Map['VAL141']);
				return false;
			}
		}
		}
		else if(AddType.toUpperCase()=='HOME')
		{
			if(BuildName == ""){
			showAlert('AddressDetails_buildname',alerts_String_Map['VAL019']);	
				return false;
			}
			else if(houseNo ==""){
			showAlert('AddressDetails_house',alerts_String_Map['VAL063']);	
				return false;
			}
			//changed done for CR PCSP-651			
			/*else if(landmark=="" ){
				showAlert('AddressDetails_landmark',alerts_String_Map['VAL075']);	
				return false;
				}*/
			else if(Streetname==""){
				showAlert('AddressDetails_street',alerts_String_Map['VAL138']);	
				return false;
				}	
			else if(city=="" || city=="--Select--"){
				showAlert('AddressDetails_city',alerts_String_Map['VAL008']);	
				return false;
				}	
			else if(state=="--Select--" || state==""){
				showAlert('AddressDetails_state',alerts_String_Map['VAL137']);	
				return false;
				}
			else if(country=="" || country=="--Select--"){
				showAlert('AddressDetails_country',alerts_String_Map['VAL040']);	
				return false;
				}
			/*	else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
				return false;
				} */
		}
	return true;
	}
	/* function checkMandatory_Address()
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
		 var CustomerType=getNGValue("AddressDetails_CustomerType");
		
		 if(CustomerType==""||CustomerType=="--Select--")
			{
			showAlert('AddressDetails_CustomerType',alerts_String_Map['VAL349']);	
			return false;
			}	
		else if(AddType=='--Select--' || AddType==''){
			showAlert('addtype',alerts_String_Map['PL262']);
			return false;
		}	
			
		else if(AddType=='OFFICE')
		{	
			if(POBox==""){
				showAlert('pobox',alerts_String_Map['PL263']);	
				return false;
				}
				
			else if(state=="--Select--" || state=''){
				showAlert('state',alerts_String_Map['PL264']);	
				return false;
				}
				
			else if(country=="" || country=="--Select--"){
				showAlert('country',alerts_String_Map['PL265']);	
				return false;
				}
		}

		else if(AddType=='RESIDENCE')
		{
			if(prefAdd==true)
			{
				if(POBox==""){
				showAlert('pobox',alerts_String_Map['PL263']);
				return false;
				}
			}
			
			else if(BuildName==""){
				showAlert('buildname',alerts_String_Map['PL266']);	
				return false;
				}
				
			else if(Streetname==""){
				showAlert('street',alerts_String_Map['PL267']);	
				return false;
				}	
				
			else if(landmark=="" ){
				showAlert('landmark',alerts_String_Map['PL268']);	
				return false;
				}	
				
			else if(city=="" || city=="--Select--"){
				showAlert('city',alerts_String_Map['PL269']);	
				return false;
				}	
				
			else if( state=="--Select--"){
				showAlert('state',alerts_String_Map['PL264']);
				return false;
				}
				
			else if(country=="" || country=="--Select--"){
				showAlert('country',alerts_String_Map['PL265']);
				return false;
				}
		}
			
		else if(AddType=='HOME')
		{
			if(BuildName==""){
				showAlert('buildname',alerts_String_Map['PL270']);	
				return false;
				}
				
			else if(Streetname==""){
				showAlert('street',alerts_String_Map['PL267']);	
				return false;
				}	
				
			else if(city=="" || city=="--Select--"){
				showAlert('city',alerts_String_Map['PL269']);
				return false;
				}	
				
			else if(state=="--Select--"){
				showAlert('state',alerts_String_Map['PL264']);
				return false;
				}
				
			else if(country=="" || country=="--Select--"){
				showAlert('country',alerts_String_Map['PL265']);
				return false;
				}
		}
	return true;
	} */
	
	function AccountSummary_checkMandatory()
	{
		if(getNGValue('cmplx_Customer_NTB')==true){
			return true;
		}
		if(getNGValue('cmplx_Customer_CIFNO')=='' || getNGValue('cmplx_Customer_CIFNO')==null){
			showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL234']);
			return false;
		}
		return true;	
	}
	
	function checkMandatory_LiabilityGrid()
	{
		var activityName=window.parent.stractivityName;
		var contractType=getNGValue('ExtLiability_contractType');
		var CAC_ind = getNGValue('ExtLiability_CACIndicator');//name change by Tarang on 28/02/2018
		var LIMIT=getNGValue('ExtLiability_Limit');
		var EMI=getNGValue('Liability_New_EMI');
		var remarks=getNGValue('ExtLiability_remarks');
		var outstanding=getNGValue('Liability_New_Outstanding');
		if(contractType=="--Select--" || contractType=="")
		{
			showAlert('contractType',alerts_String_Map['PL271']);	
			return false;
		}
		
		//Changes Done by aman for PCSP-67
		else if (getNGValue('Contract_App_Type')=="L" && getNGValue('ExtLiability_Limit')==''){
			showAlert('ExtLiability_Limit',alerts_String_Map['VAL382']);	
			return false;
		
		}
			
		else if(EMI=="0" && getNGValue('ExtLiability_Limit')!='' && getNGValue('Contract_App_Type')=="L"){
			showAlert('EMI',alerts_String_Map['VAL080']);	
			return false;
			}
		//Changes Done by aman for PCSP-67	
		//Added by aman for PCSP-174
		else if(CAC_ind==true && getNGValue('Liability_New_MOB')==''){
			showAlert('Liability_New_MOB',alerts_String_Map['VAL380']);	
			return false;
			}
		else if(CAC_ind==true && getNGValue('Avg_Utilization')==''){
			showAlert('Avg_Utilization',alerts_String_Map['VAL381']);	
			return false;
			}		
		//Added by aman for PCSP-174	
		
			
		//Changes Done by aman for PCSP-67
		else if (getNGValue('Contract_App_Type')=="L" && getNGValue('ExtLiability_Limit')==''){
			showAlert('ExtLiability_Limit',alerts_String_Map['VAL382']);	
			return false;
		
		}
		else if(getNGValue('ExtLiability_Takeoverindicator')==true && getNGValue('ExtLiability_takeoverAMount')==''){
		showAlert('ExtLiability_takeoverAMount',alerts_String_Map['PL258']);	
		return false;
		}
			// ++ below code already present - 09-10-2017
		
			
		else if(outstanding=='' && activityName!='CAD_Analyst1'){

			showAlert('outstanding','Outstanding cannot be blank');	
			return false;
			}	
			// ++ above code already present - 09-10-2017
		else if(getNGValue('ExtLiability_Takeoverindicator')==true && getNGValue('ExtLiability_takeoverAMount')==''){
			showAlert('ExtLiability_takeoverAMount',alerts_String_Map['PL258']);	
			return false;
			}
		return true;
	}
	
	function checkMandatory_PartnerGrid()
	{
			if(getNGValue('PartnerDetails_PartnerName')=="")
			{
				showAlert('PartnerDetails_PartnerName',alerts_String_Map['PL275']);	
				return false;
			}
				
			else if(getNGValue('PartnerDetails_Dob')==""){
				showAlert('PartnerDetails_Dob',alerts_String_Map['PL012']);
				return false;
				}
				
			else if(getNGValue('PartnerDetails_passno')==""){
				showAlert('PartnerDetails_passno',alerts_String_Map['PL015']);
				return false;
				}
				
			else if( getNGValue('PartnerDetails_authsigname')==''){
				showAlert('PartnerDetails_authsigname',alerts_String_Map['PL276']);	
				return false;
			}
			
			else if( getNGValue('PartnerDetails_nationality')==''){
				showAlert('PartnerDetails_nationality',alerts_String_Map['PL013']);
				return false;
			}	
			else if(getNGValue('PartnerDetails_shareholding')==''){
				showAlert('PartnerDetails_shareholding',alerts_String_Map['PL277']);
				return false;
			}	
	}
	
	function AuthSignatory_checkMandatory()
	{
		if(getNGValue('AuthorisedSignDetails_CheckBox1')==true)
		{
			if(getNGValue('CIFNo')=="")
			{
			showAlert('CIFNo',alerts_String_Map['PL016']);
			return false;
			}
			
			else if(flag_Authorised_FetchDetails==false)
			{	
				showAlert('AuthorisedSignDetails_Button3',alerts_String_Map['PL278']);
				return false;
			}
		}
			
		else
		{	
			if(getNGValue('authName')=="")
			{
				showAlert('authName',alerts_String_Map['PL276']);
				return false;
			}
				
			else if(getNGValue('DOB')==""){
				showAlert('DOB',alerts_String_Map['PL279']);	
				return false;
				}
				
			else if(getNGValue('VisaNumber')==""){
				showAlert('VisaNumber',alerts_String_Map['PL280']);	
				return false;
				}
				
			else if( getNGValue('shareholding')==''){
				showAlert('shareholding',alerts_String_Map['PL281']);	
				return false;
			}
			
			else if( getNGValue('SoleEmployee')==''){
				showAlert('SoleEmployee',alerts_String_Map['PL282']);	
				return false;
			}
		}	
	return true;
	}
	
	function checkMandatory_CompanyGrid()
{
	if(getNGValue('CompanyDetails_CheckBox4')==true){
		if(getNGValue('cif')=='')
		{
			showAlert('cif',alerts_String_Map['PL016']);
			return false;
		}
		else if(flag_Company_FetchDetails==false){
			showAlert('CompanyDetails_Button3',alerts_String_Map['PL278']);
			return false;
		}	
	}
	else
	{
		if(getNGValue('appType')=="" || getNGValue('appType')=="--Select--")
		{
			showAlert('appType',alerts_String_Map['PL283']);	
			return false;
		}
			
		else if(getNGValue('compName')==""){
			showAlert('compName',alerts_String_Map['PL151']);
			return false;
			}
			
		else if(getNGValue('compIndus')=="--Select--"){
			showAlert('compIndus',alerts_String_Map['PL284']);	
			return false;
			}
			
		else if( getNGValue('tlNo')==''){
			showAlert('tlNo',alerts_String_Map['PL285']);	
			return false;
		}
		
		else if( getNGValue('TLExpiry')==''){
			showAlert('TLExpiry',alerts_String_Map['PL286']);	
			return false;
		}
			
		else if(getNGValue('indusSector')=="--Select--" || getNGValue('indusSector')==""){
			showAlert('indusSector',alerts_String_Map['PL287']);	
			return false;
			}
			
		else if(getNGValue('indusMAcro')=="" || getNGValue('indusMAcro')=="--Select--"){
			showAlert('indusMAcro',alerts_String_Map['PL288']);	
			return false;
			}
			
		else if(getNGValue('indusMicro')==''){
			showAlert('indusMicro',alerts_String_Map['PL289']);	
			return false;
		}
		
		else if(getNGValue('desig')=='--Select--' || getNGValue('desig')==''){
			showAlert('desig',alerts_String_Map['PL057']);//val057	
			return false;
		}
		
		else if(getNGValue('desigVisa')==""){
			showAlert('desigVisa',alerts_String_Map['PL290']);	
			return false;
			}
			
		else if(getNGValue('legalEntity')==""){
			showAlert('legalEntity',alerts_String_Map['PL291']);	
			return false;
			}
			
		else if( getNGValue('estbDate')==''){
			showAlert('estbDate',alerts_String_Map['PL292']);	
			return false;
		}
		
		else if( getNGValue('lob')==''){
			showAlert('lob',alerts_String_Map['PL293']);	
			return false;
		}
		else if(getNGValue('grt40')=='--Select--'){
			showAlert('grt40',alerts_String_Map['PL294']);	
			return false;
		}
	}
return true;	
}

function validate_GuarantorGrid()
{
	var cif=getNGValue('GuarantorDetails_cif');
	var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Guarantror_GuarantorDet");
	for(var i=0;i<n;i++)
		if(getLVWAT('cmplx_Guarantror_GuarantorDet',i,0)==cif){
			showAlert('GuarantorDetails_cif',alerts_String_Map['PL295']);
			return false;
		}	
return true;
}

function checkMandatory_GuarantorGrid()
{
	if(getNGValue('GuarantorDetails_guardianCif')=='--Select--'){
		showAlert('GuarantorDetails_guardianCif', 'Please Select Guarantor CIF Available.');
		return false;
	}
	if(getNGValue('GuarantorDetails_cif')==''){
		showAlert('GuarantorDetails_cif', 'Please Enter Guarantor CIF');
		return false;
	}
	else if(flag_GuarantorDetails==false) {
		showAlert('GuarantorDetails_Button2',alerts_String_Map['VAL124']);
		return false;
	}		
	else if (getNGValue('GuarantorDetails_Fname') == '' || getNGValue('GuarantorDetails_Fname')==null || getNGValue('GuarantorDetails_Fname')==undefined)
    {
        showAlert('GuarantorDetails_Fname', alerts_String_Map['VAL061']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_lname') == '' || getNGValue('GuarantorDetails_lname')==null || getNGValue('GuarantorDetails_lname')==undefined)
    {
        showAlert('GuarantorDetails_lname', alerts_String_Map['VAL076']);
        return false;
    }
	else if(!validateCustomerName(getNGValue('GuarantorDetails_Fname'),getNGValue('GuarantorDetails_Mname'),getNGValue('GuarantorDetails_lname'))){
		showAlert('GuarantorDetails_Fname','Customer Full Name cannot exceed 80 characters');
		return false;
	}
	else if (getNGValue('GuarantorDetails_passpNo') == '')
    {
        showAlert('GuarantorDetails_passpNo', alerts_String_Map['VAL097']);
        return false;
    }
	else if ((getNGValue('GuarantorDetails_title') == '')||(getNGValue('GuarantorDetails_title') == '--Select--'))
    {
        showAlert('GuarantorDetails_title', alerts_String_Map['VAL144']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_eid') == '')
    {
        showAlert('GuarantorDetails_eid', alerts_String_Map['VAL055']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_dob') == '')
    {
        showAlert('GuarantorDetails_dob', alerts_String_Map['VAL045']);
        return false;
    }
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
	else if (getNGValue('GuarantorDetails_idIssueDate') == '')
    {
        showAlert('GuarantorDetails_idIssueDate', alerts_String_Map['VAL068']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_eidExpiry') == '')
    {
        showAlert('GuarantorDetails_eidExpiry', alerts_String_Map['VAL050']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_mobNo') == '')
    {
        showAlert('GuarantorDetails_mobNo', alerts_String_Map['VAL086']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_passExpiry') == '')
    {
        showAlert('GuarantorDetails_passExpiry', alerts_String_Map['VAL096']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_visaNo') == '' && getNGValue('GuarantorDetails_nationality')!='AE')
    {
        showAlert('GuarantorDetails_visaNo', alerts_String_Map['VAL154']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_age') == '')
    {
        showAlert('GuarantorDetails_age', alerts_String_Map['VAL003']);
        return false;
    }
	else if (getNGValue('GuarantorDetails_visaExpiry') == '' && getNGValue('GuarantorDetails_nationality')!='AE')
    {
        showAlert('GuarantorDetails_visaExpiry', alerts_String_Map['VAL153']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_MAritalStatus')==false)
    {
        showAlert('GuarantorDetails_MAritalStatus', alerts_String_Map['VAL364']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_empType')==false)
    {
        showAlert('GuarantorDetails_empType', alerts_String_Map['VAL365']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_employmentStatus')==false)
    {
        showAlert('GuarantorDetails_employmentStatus', alerts_String_Map['VAL366']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_MothersName')==false)
    {
        showAlert('GuarantorDetails_MothersName', alerts_String_Map['VAL367']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_VisaIssueDate')==false && getNGValue('GuarantorDetails_nationality')!='AE')
    {
        showAlert('GuarantorDetails_VisaIssueDate', alerts_String_Map['VAL368']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_passportIssueDate')==false)
    {
        showAlert('GuarantorDetails_passportIssueDate', alerts_String_Map['VAL369']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_designation')==false)
    {
        showAlert('GuarantorDetails_designation', alerts_String_Map['VAL370']);
        return false;
    }
	else if (isFieldFilled('GuarantorDetails_email')==false)
    {
        showAlert('GuarantorDetails_email', alerts_String_Map['PL005']);
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
		showAlert('Product_type',alerts_String_Map['PL296']);
		return false;
		}
	else if(ReqProd==""|| ReqProd=="--Select--"){
		showAlert('ReqProd',alerts_String_Map['PL297']);
		return false;
		}
	else if(AppType=="--Select--"){
		showAlert('AppType',alerts_String_Map['PL298']);
		return false;
		}
	else if(ReqLimit==""){
		showAlert('ReqLimit',alerts_String_Map['PL299']);
		return false;
		}
	else if(EmpType=="" || EmpType=='--Select--'){
		showAlert('EmpType',alerts_String_Map['PL300']);
		return false;
		}
	else if(SubProd=="" || SubProd=='--Select--'){
		showAlert('subProd',alerts_String_Map['PL301']);	
		return false;
		}
		
	else if(Priority=="" || Priority=="--Select--"){
		showAlert('Priority',alerts_String_Map['PL270']);
		return false;
		}
		
	else if(ReqProd=='Personal Loan'){
		var tenor=getNGValue('ReqTenor');
		var scheme=getNGValue('Scheme');
		if(tenor==""){
			showAlert('ReqTenor',alerts_String_Map['PL302']);
			return false;
			}
			
		else if(scheme=='' || scheme=='--Select--'){
			showAlert("Scheme",alerts_String_Map['PL360']);
			return false;
			}
	}
		
	else if(ReqProd=='Credit Card' && SubProd!='IM'){
		var CardProd=getNGValue('CardProd');
		 if(CardProd=='' || CardProd=='--Select--'){
			showAlert("CardProd",alerts_String_Map['PL303']);
			return false;
		}
		
		else if(SubProd=='Limit'){
			//var typeReq=getNGValue('typeReq');
			var LimitExpiryDate=getNGValue('LimitExpiryDate');
			var LimitAcc=getNGValue('LimitAcc');
			/*if(typeReq=='--Select--'){
				showAlert("typeReq",alerts_String_Map['PL304']);
				return false;
			}*/
			
		 if(LimitExpiryDate==''){
				showAlert("LimitExpiryDate",alerts_String_Map['PL305']);
				return false;
			}
		
			else if(LimitAcc==''){
				showAlert("LimitAcc",alerts_String_Map['PL306']);
				return false;
			}
		}
	}	
return true;
}

function Validate_OTP_checkMandatory()
{
	if(getNGValue('OTP_No')==''){
		showAlert('OTP_No',alerts_String_Map['PL307']);	
		return false;
		}
	else if(getNGValue('cmplx_Customer_MobNo')==""){
		showAlert('cmplx_Customer_MobNo',alerts_String_Map['PL308']);
		return false;
		}	
	else if(getNGValue('cmplx_Customer_CIFNO')==""){
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL309']);
		return false;
		}
	else if(getNGValue('cmplx_Customer_FIrstNAme')==""){
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['PL310']);	
		return false;
		}
	return true;
}

function Send_OTP_checkMandatory()
{
	if(getNGValue('OTP_Mobile_NO')==''){
		showAlert('OTP_Mobile_NO',alerts_String_Map['PL308']);
		return false;
		}
	else if(getNGValue('cmplx_Customer_CIFNO')==""){
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL309']);
		return false;
		}
	else if(getNGValue('cmplx_Customer_FIrstNAme')==""){
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['PL310']);
		return false;
		}
	return true;
}

function checkMandatory_ReferenceGrid()
{
	if(getNGValue('ref_NAme')==""){
		showAlert('ref_NAme',alerts_String_Map['PL230']);
		return false;
		}
	else if(getNGValue('ref_mobile')==""){
		showAlert('ref_mobile',alerts_String_Map['PL231']);
		return false;
		}
	else if(getNGValue('ref_Relationship')=="--Select--"){
		showAlert('ref_Relationship',alerts_String_Map['PL233']);
		return false;
		}
	return true;
}
function checkMandatory_ContactDetails_save(){
					// added by abhishek for CC FSD
					var n=getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
				for(var i=0;i<n;i++){
					 if(getNGValue('AlternateContactDetails_HOMECOUNTRYNO')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='HOME'){
					showAlert('AlternateContactDetails_HOMECOUNTRYNO',alerts_String_Map['PL311']);	
					return false;
					}
					if(getNGValue('AlternateContactDetails_OFFICENO')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='OFFICE'){
					showAlert('AlternateContactDetails_OFFICENO',alerts_String_Map['PL312']);	
					return false;
					}
					if(getNGValue('AlternateContactDetails_RESIDENCENO')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='RESIDENCE'){
					showAlert('AlternateContactDetails_RESIDENCENO',alerts_String_Map['PL313']);	
					return false;
					}
			}
			if(getNGValue('AlternateContactDetails_MobileNo1')=="")
			{
				showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['PL314']);	
				return false;
			}//by shweta
			else if(getNGValue('AlternateContactDetails_MobileNo2')==""){
				showAlert('AlternateContactDetails_MobileNo2',alerts_String_Map['PL315']);	
				return false;
				}
			else if(getNGValue('AlternateContactDetails_EMAIL1_PRI')==""){
				showAlert('AlternateContactDetails_EMAIL1_PRI',alerts_String_Map['PL316']);	
				return false;
				}
			else if(getNGValue('AlternateContactDetails_ESTATEMENTFLAG')==""||getNGValue('AlternateContactDetails_ESTATEMENTFLAG')=="--Select--"){
			showAlert('AlternateContactDetails_ESTATEMENTFLAG',alerts_String_Map['PL317']);	
			return false;
			}
			else if(getNGValue('AlternateContactDetails_carddispatch')==""||getNGValue('AlternateContactDetails_carddispatch')=="--Select--"){
			showAlert('AlternateContactDetails_carddispatch',alerts_String_Map['PL096']);
			return false;
			}
		return true;
	} //Arun from CC
	//++Below code added by nikhil 13/11/2017 for Code merge

	function chechmandatory_SaveFCU()
	{
		
	//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7
	if(!checkMandatory(PL_Custdetail_FCU))
	{
		return false;
	}
	
	var ver="cmplx_CustDetailverification1_MobNo1_veri:cmplx_CustDetailverification1_MobNo2_veri:cmplx_CustDetailverification1_DOB_ver:cmplx_CustDetailverification1_PO_Box_Veri:cmplx_CustDetailverification1_Emirates_veri:cmplx_CustDetailverification1_Off_Telephone_veri";
	var update="cmplx_CustDetailverification1_MobNo1_updates:cmplx_CustDetailverification1_MobNo2_Updates:cmplx_CustDetailverification1_Dob_update:cmplx_CustDetailverification1_PO_Box_Uodates:cmplx_CustDetailverification1_EmiratesId_updates:cmplx_CustDetailverification1_OfficeTelephone_Updates";
	var name="Mobile No. 1:Mobile No. 2:Date of Birth:P.O Box No.:Emirates:Office Telephone No.";
	if(!CheckMandatory_Verification(ver,update,name))
	{
		return false;
	}
	//--above code added by nikhil 31/10/17 as per CC FSD 2.7
	//++Below code commented by  nikhil 31/10/17 as per CC FSD 2.7
		/*
	if(getNGValue('cmplx_CustDetailverification1_MobNo1_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates','Updates column for Mobile No 1 cannot be blank');
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_MobNo2_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo2_Updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo2_Updates','Updates column for Mobile No 2 cannot be blank');
			return false;
		}
	}
	if(getNGValue('CustDetailVerification1_Combo3')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_Dob_update')==""){
			showAlert('cmplx_CustDetailverification1_Dob_update','Updates column for Mobile No ! cannot be blank');
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_PO_Box_Veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates','Updates column for Mobile No ! cannot be blank');
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_Emirates_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates','Updates column for Mobile No ! cannot be blank');
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_Off_Telephone_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates','Updates column for Mobile No ! cannot be blank');
			return false;
		}
	}*/
	return true;
}
//--Above code added by nikhil 13/11/2017 for Code merge
function checkMandatory_saveBussinessVeri(){
	if(getNGValue('cmplx_BussVerification1_ContactPerson')==""){
		showAlert('cmplx_BussVerification1_ContactPerson',alerts_String_Map['CC048']);
			return false;
	}
	if(getNGValue('cmplx_BussVerification1_ContactTelephoneCheck')==""||getNGValue('cmplx_BussVerification1_ContactTelephoneCheck')=="--Select--"){
		showAlert('cmplx_BussVerification1_ContactTelephoneCheck',alerts_String_Map['CC049']);
			return false;
	}
	return true;
}
//++Below code added by nikhil 13/11/2017 for Code merge
function checkMandatory_EmpVerification(){
	//change by saurabh on 20 Dec for PCSP-198
	if(getNGValue('cmplx_EmploymentVerification_offTelNoCntctd')=='' || getNGValue('cmplx_EmploymentVerification_offTelNoCntctd')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_offTelNoCntctd',alerts_String_Map['PL388']);
			return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_offTelNoCntctd')=="Yes"){
		if(getNGValue('cmplx_EmploymentVerification_OffTelNo')==""){
			showAlert('cmplx_EmploymentVerification_OffTelNo','Office Telephone Number cannot be blank');
			return false;
		}
		if(getNGValue('cmplx_EmploymentVerification_OffTelnoValidatedfrom')==""||getNGValue('cmplx_EmploymentVerification_OffTelnoValidatedfrom')=="--Select--"){
			showAlert('cmplx_EmploymentVerification_OffTelnoValidatedfrom','Office Telephone Number Validated From cannot be blank');
			return false;
		}
	}
	if(getNGValue('cmplx_EmploymentVerification_HRDcntctNo')!=""){
		if(getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')==""||getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')=="--Select--"){
			//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7

				showAlert('cmplx_EmploymentVerification_HRDNoCntctd','HRD No. Contacted is Mandatory');
				//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
			return false;
		}
	}
	if(getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')=="Yes"){
		if(getNGValue('cmplx_EmploymentVerification_HRDcntctName')==""){
			showAlert('cmplx_EmploymentVerification_HRDcntctName','HRD Contact Name cannot be blank');
			return false;
		}
	}
	//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7
	if(getNGValue('cmplx_EmploymentVerification_Salary_variance')=="yes"){
		//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
		if(getNGValue('cmplx_EmploymentVerification_ReasonForVariance')==""){
			showAlert('cmplx_EmploymentVerification_ReasonForVariance','Reason for variance is mandatory');
			return false;
		}
	}

	//++Below code commented by nikhil 31/10/17
	/*
	if(getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_ver')=='Yes'||getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_ver')=='No'){
		if(getNGValue('cmplx_EmploymentVerification_fixedsalupd')==''||getNGValue('cmplx_EmploymentVerification_fixedsalupd')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_fixedsalupd','Fixed Salary Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_AccomProvided_upd')==''||getNGValue('cmplx_EmploymentVerification_AccomProvided_upd')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_AccomProvided_upd','Accomodation Provided Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_Desig_upd')==''||getNGValue('cmplx_EmploymentVerification_Desig_upd')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_Desig_upd','Designation Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_doj_upd')==''||getNGValue('cmplx_EmploymentVerification_doj_upd')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_doj_upd','Date of Joining Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_confirmedinJob_upd')==''||getNGValue('cmplx_EmploymentVerification_confirmedinJob_upd')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_confirmedinJob_upd','Confirmed in Job Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_LoanFromCompany_updates')==''||getNGValue('cmplx_EmploymentVerification_LoanFromCompany_updates')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_LoanFromCompany_updates','Loan/Advance From Compan Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_PermanentDeductSal_updates')==''||getNGValue('cmplx_EmploymentVerification_PermanentDeductSal_updates')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_PermanentDeductSal_updates','Permanent Deduction From Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_SmartCheck_updates')==''||getNGValue('cmplx_EmploymentVerification_SmartCheck_updates')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_SmartCheck_updates','Smart Check Updates is mandatory');
		return false;	
		}
		if(getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_updates')==''||getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_updates')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_FiledVisitedInitiated_updates','Field Visit Initiated Updates is mandatory');
		return false;	
		}
	}
	if(getNGValue('cmplx_EmploymentVerification_Remarks')==''){
		showAlert('cmplx_EmploymentVerification_Remarks','Remarks are mandatory to fill');
		return false;
	}*/
//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7
	if(!checkMandatory(PL_Employveri_FCU))
	{
		return false;
	}
	
	var ver="cmplx_EmploymentVerification_fixedsal_ver:cmplx_EmploymentVerification_AccomProvided_ver:cmplx_EmploymentVerification_Desig_ver:cmplx_EmploymentVerification_doj_ver:cmplx_EmploymentVerification_confirmedinJob_ver:cmplx_EmploymentVerification_LoanFromCompany_ver:cmplx_EmploymentVerification_PermanentDeductSal_ver:cmplx_EmploymentVerification_SmartCheck_ver:cmplx_EmploymentVerification_FiledVisitedInitiated_ver";
	var update="cmplx_EmploymentVerification_fixedsalupd:cmplx_EmploymentVerification_AccomProvided_upd:cmplx_EmploymentVerification_Desig_upd:cmplx_EmploymentVerification_doj_upd:cmplx_EmploymentVerification_confirmedinJob_upd:cmplx_EmploymentVerification_LoanFromCompany_updates:cmplx_EmploymentVerification_PermanentDeductSal_updates:cmplx_EmploymentVerification_SmartCheck_updates:cmplx_EmploymentVerification_FiledVisitedInitiated_updates";
	var name="Fixed Salary:Accomodation Provided:Designation:Date of Joining:Confirmed in Job:Loan/Advance From Company:Permanent Deduction From Salary:Smart Check:Field Visit Initiated";
	if(!CheckMandatory_Verification(ver,update,name))
	{
		return false;
	}
	//--above code added by nikhil 31/10/17 as per CC FSD 2.7
	return true;
}//Arun from CC
	//--Above code added by nikhil 13/11/2017 for Code merge
function validate_ReferenceGrid()
{
	var n=getLVWRowCount("cmplx_Customer_cmplx_GR_ReferenceDetailsGrid");
	if(n==2){
		showAlert('cmplx_Customer_cmplx_GR_ReferenceDetailsGrid',alerts_String_Map['PL337']);
		return false;
	}
	
	if(getLVWAT('cmplx_Customer_cmplx_GR_ReferenceDetailsGrid',0,0)==getNGValue('ref_NAme') && getLVWAT('cmplx_Customer_cmplx_GR_ReferenceDetailsGrid',0,1)==getNGValue('ref_mobile')){
		showAlert('cmplx_Customer_cmplx_GR_ReferenceDetailsGrid',alerts_String_Map['PL338']);
		return false;
	}
return true;	
}
function MaturityDate_1()
{
	var FirstRepaymentDate = getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate');
	if(isFieldFilled('cmplx_EligibilityAndProductInfo_FirstRepayDate')==false){
		return "";
	}
	
	var Tenor = getNGValue('cmplx_EligibilityAndProductInfo_Tenor');
	if(isFieldFilled('cmplx_EligibilityAndProductInfo_Tenor')==false){
		Tenor=0;
	}
	
	var parts = FirstRepaymentDate.split('/');
	var oldDate=new Date(parts[2],parts[1]-1,parts[0]);
	oldDate.setMonth(oldDate.getMonth()+(parseInt(Tenor)-1));
	var matday = oldDate.getDate();
	var matMonth = oldDate.getMonth();
	matMonth++;
	var matYear = oldDate.getFullYear();
	if(matday<10){matday='0'+matday;}
	if(matMonth<10){matMonth='0'+matMonth;}
	var maturityDate=matday+'/'+matMonth+'/'+matYear;
	return maturityDate;

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
		
			
	if(Id==""){
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['PL007']);//val007
		return false;
		}
	/*else if(Id.charAt(0)!='7' ||  Id.charAt(1)!='8' || Id.charAt(2)!='4'){
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['PL339']);
			return false;
		}	*/
	else if(CIF==""){
		showAlert('cmplx_Customer_CIFNO',alerts_String_Map['PL234']);
		return false;
		}	
	else if(Lname==""){
		showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['PL011']);
		return false;
		}
	else if(MobNo==""){
		showAlert('cmplx_Customer_MobNo','Please Fill Mobile Number');
		com.newgen.omniforms.formviewer.setNGFocus("cmplx_Customer_MobNo");
		return false;
		}
	else if(PassNo==""){
		showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['PL015']);
		return false;
		}
	else if(Nat==""){
		showAlert('cmplx_Customer_Nationality',alerts_String_Map['PL013']);
		return false;
		}
	else if(Dob==""){
		showAlert('cmplx_Customer_DOb',alerts_String_Map['PL012']);
		return false;
		}
	/*else {
		var year_dob=Dob.substring(6,10);
		var year_id=Id.charAt(3)+Id.charAt(4)+Id.charAt(5)+Id.charAt(6);
		if(year_dob!=year_id){
		showAlert('',alerts_String_Map['PL340']);
		return false;
		}
	}	*/
return true;
}

function LoanDetails_Save_Check()
	{
		//alert("I am an alert box!");
	var activityName = window.parent.stractivityName;
		if(!checkMandatory(PL_LoanDetails)){
				return false;
		}
		/*else if(parseInt(getNGValue('cmplx_LoanDetails_lpfamt'))<parseInt(getNGValue('LoanDetails_MinLPF')) || parseInt(getNGValue('cmplx_LoanDetails_lpfamt'))>parseInt(getNGValue('LoanDetails_MaxLPF')))
		{
			
			setLocked('cmplx_LoanDetails_lpfamt',false);
			setEnabled('cmplx_LoanDetails_lpfamt',true);
			showAlert('cmplx_LoanDetails_lpfamt','LPF Amount must be between '+getNGValue('LoanDetails_MinLPF')+' and '+getNGValue('LoanDetails_MaxLPF'));
			return false;
		}*/
		else if(getNGValue('cmplx_LoanDetails_is_repaymentSchedule_generated')!='Y' && getNGValue('IS_Approve_Cif')!='Y'){ //FOR JIRA 2951
			showAlert('LoanDetails_Button1','Please generate repayment Schedule in Loan Details!!');
				return false;
		}
		var moratorium = getNGValue('cmplx_LoanDetails_moratorium');//PCASI-1050 
		if((moratorium == '' || moratorium == null) && activityName=='DDVT_maker' && getNGValue('IS_Approve_Cif')!='Y') //2951 JIRA
		{
			showAlert('cmplx_LoanDetails_moratorium', 'Please Fill Moratorium');
			return false;
		}
		if(getNGValue("IS_Approve_Cif")!="Y") //2951 JIRA
		{
		if(!validatePastDate('cmplx_LoanDetails_frepdate','Repayment')){
			return false;
		}
		}
		 
	return true;	
	}

function validateDisbursalGrid(){
	var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_LoanDetails_cmplx_LoanGrid");
	if(n>0){
		showAlert('','Only one Disbursal Details Can be added.');
		return false;
	}
	if(!validatePastDate('LoanDetails_payreldate','Payment Release')){
			return false;
		}
	return true;
}
	function LoanDetDisburse_Check()
	{
		var Mode_of_Disbursal=getNGValue("LoanDetails_modeofdisb");
		var Hold_Code=getNGValue("LoanDetails_holdcode");
		//showAlert('LoanDetails_holdcode','Hold Code is mandatory');
		if(Mode_of_Disbursal=='Transfer')
		{
			if(checkMandatory(PL_LoanDisb))
			return false;
		}
		else if(Hold_Code == 'AS2' || Hold_Code == 'CAD' || Hold_Code == 'COM')
		{
			if(checkMandatory(PL_LoanDisb1))
			return false;
		}
		return true;
	}//Arun (21/09/17)
	//++Below code added by nikhil 13/11/2017 for Code merge

	//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7
 function CheckMandatory_Verification(s1,s2,s3)
 {
	 var ver = s1.split(":");
	 var update=s2.split(":");
	 var name=s3.split(":");
	 for(var i = 0 ; i<ver.length;i++)
	 {
		 if(getNGValue(ver[i])=='Mismatch' && (getNGValue(update[i])=='' || getNGValue(update[i])=='--Select--'))
		 {
			 showAlert(update[i],name[i]+" update is mandatory");
			 return false;
		 }
	 }
	 return true;
	 
 }
 
 /*else if(getNGValue(ver[i])=='Mismatch' && getNGValue(name[i]) != '' && (getNGValue(update[i])==getNGValue(name[i])))
		 {
			 showAlert(update[i]+"Please select a different value");
			 setNGValue(update[i],'');
			 return false;
		 }*/
//--above code added by  nikhil 31/10/17 as per CC FSD 2.7

//--Above code added by nikhil 13/11/2017 for Code merge


function CheckMandatory_equaldata(d1,d2,d3)
 {
	 var ver = d1.split(":");
	 var update=d2.split(":");
	 var name=d3.split(":");
	 for(var i = 0 ; i<ver.length;i++)
	 {
		 if(getNGValue(ver[i])=='Mismatch' && (getNGValue(name[i]) == getNGValue(update[i])))
		 {
			 showAlert(update[i]+"Enter different value");
			 return false;
		 }
	 }
	 return true;
	 
 }//arun 13/12/17
 
 
 //hritik - PCASI 3548
 
 function expiraygreatissue(pId1,pId2)
{
	// hritik PCASI 3548 - If expiray date of visa/Emiratesid is greater than issued date by 10 years.
	var issue=pId1;
	var from = getNGValue(issue);
    var parts = from.split('/');
	var fromDate=new Date(parts[2],parts[1]-1,parts[0]);
    var fromDate_year=fromDate.getFullYear();
	var fromDate_month=fromDate.getMonth();
	var fromDate_day=fromDate.getDate();
    var fromDate = new Date(fromDate_year+10,fromDate_month,fromDate_day);
    
    var to = getNGValue(pId2)
    var parts = to.split('/');
	var toDate=new Date(parts[2],parts[1]-1,parts[0]);
    var toDate_year=toDate.getFullYear();
	var toDate_month=toDate.getMonth();
	var toDate_day=toDate.getDate();
    
    if(fromDate<toDate)
	{
    	showAlert(pId2,'Expiry date cannot be greater than 10 years from issued date!!');
    	return false;
    }
}

//++below code added by nikhil for toteam
function CheckConditionsLC()
{
	var row_count=getLVWRowCount('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate');
	var activeflag=false;
	for(var i=0;i<=row_count;i++)
	{
		if(getLVWAT('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate',i,7)=='Received' && getNGValue('PostDisbursal_Status')=='Received')
		{
			activeflag=true;
		}
	}
	if(activeflag && getNGValue('PostDisbursal_Bank_Type')=='Primary Bank') //multiple secondary LC CR 3207
	{
		showAlert('PostDisbursal_Status',alerts_String_Map['PL378']);
		return false;
	}
	if(!checkMandatory(PL_Liability_certi))
	{
		return false;
	}
	return true;
}
function CheckConditionsMCQ()
{
	if(!checkMandatory(PL_Manager_Cheque))
	{
		return false;
	}
	if(mcq_deposit_flag)
	{
		showAlert('PostDisbursal_NLCFollowUp','MCQ deposit date changed ,Kindly update the NLC follow up date');
		return false;
	}
	return true;
}
function CheckConditionsBG()
{
	if(!checkMandatory(PL_Bank_Guarantee))
	{
		return false;
	}
	return true;
}
function CheckConditionsNLC()
{
	if(!checkMandatory(PL_No_Liability_certi))
	{
		return false;
	}
	return true;
}
//Tanshu Aggarwal Card Details Mandatory check(16/08/2017)
//12th september
function checkMandatory_2ndgridcarddetails(){
	if(isFieldFilled('CardDetails_CardProduct')==false){
		showAlert('CardDetails_CardProduct',alerts_String_Map['CC033']);
		return false;
	}
	else if(getNGValue('CardDetails_ECRN')==""){
		showAlert('CardDetails_ECRN',alerts_String_Map['CC075']);
		return false;
	}
	else if(getNGValue('CardDetails_CRN')==""){
		showAlert('CardDetails_CRN',alerts_String_Map['CC055']);
		return false;
	}
	else if(isFieldFilled('CardDetails_TransactionFP')==false){
		showAlert('CardDetails_TransactionFP',alerts_String_Map['CC218']);
		return false;
	}
	else if(isFieldFilled('CardDetails_InterestFP')==false){
		showAlert('CardDetails_InterestFP',alerts_String_Map['CC109']);
		return false;
	}
	else if(isFieldFilled('CardDetails_FeeProfile')==false){
		showAlert('CardDetails_FeeProfile',alerts_String_Map['CC085']);
		return false;
	}
	return true;
}
function CheckConditionsSTL()
{
	var row_count=getLVWRowCount('cmplx_PostDisbursal_cpmlx_gr_NLC');
	var primarybank=false;
	for(var i=0;i<=row_count;i++)
	{
		if(getLVWAT('cmplx_PostDisbursal_cpmlx_gr_NLC',i,6)=='Primary Bank' )
		{
			primarybank=true;
		}
	}
	if(primarybank && getNGValue('cmplx_PostDisbursal_STLReceivedDate')=='' )
	{
		showAlert('cmplx_PostDisbursal_STLReceivedDate',alerts_String_Map['PL379']);
		return false;
	}
	if(primarybank && getNGValue('cmplx_PostDisbursal_Remarks')=='')
	{
		showAlert('cmplx_PostDisbursal_Remarks',alerts_String_Map['PL380']);
		return false;
	}
	if(!primarybank && (getNGValue('cmplx_PostDisbursal_STLReceivedDate')=='' || getNGValue('cmplx_PostDisbursal_STLReceivedDate')!=''))
	{
		showAlert('cmplx_PostDisbursal_Remarks',alerts_String_Map['PL381']);
		return false;
	}
	return true;
}

//added by akshay on 12/9/17
	function validateReferGrid()
	{
		var is_grid_modified=true;
		var is_complete=true;//by shweta
		var n=getLVWRowCount('cmplx_ReferHistory_cmplx_GR_ReferHistory');
		var activity = activityName;
		if(n>0){
			for(var i=0;i<n;i++){
				var referFrom = getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,2);
				var gridReferVal = getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,5);
				if(activity=='CPV_Analyst'){activity='CPV Checker';}
				else if(activity=='DSA_CSO_Review' && gridReferVal !='DSA_CSO_Review'){activity='Source';}
				else if(activity=='DSA_CSO_Review'){activity='Source';}
				
				if(getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,2)!='Disbursal_Checker' && gridReferVal==activity && (getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)=='' || getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)=='--Select--')){
					is_grid_modified=false;
				}
				//below code added by nikhil for PCSP-487
				//added by shweta for code sync
				if( getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,5)==activity && getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)!='Complete'){
					is_complete=false;
				}
			}
		}
		else{
				is_grid_modified=false;
		}
		
		if(is_grid_modified==false && n>0){
					showAlert('ReferHistory_status',alerts_String_Map['CC254']);
					return false;
				}
		//added by shweta for code sync
		//below code added by nikhil for PCSP-487
		//changes for PCAS-2431
		if(is_complete==false && getNGValue('cmplx_Decision_Decision').indexOf('Hold')==-1){
			showAlert('ReferHistory_status',alerts_String_Map['PL415']);
			return false;
		}
	  return true;	
	}	
	
	
//below finction added by nikhil 
function checkforfinaclecustinfo()
{
var row_count=getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');
var cif_changed=false;
if(row_count>0)
{
for(var i=0;i<=row_count;i++)
	{
		if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)==getNGValue('cmplx_Customer_CIFNO') )
		{
			cif_changed=true;
		}
	}
}
else{
	cif_changed=true;
}
	return cif_changed;
}

//below code added by nikhil
function CheckMandatory_VerificationCPV(s1,s2,s3,s4)
 {
	 var ver = s1.split(":");
	 var update=s2.split(":");
	 var name=s3.split(":");
	 var val=s4.split(":");
	 for(var i = 0 ; i<ver.length;i++)
	 {
		
		 if(getNGValue(ver[i])=='Mismatch' && (getNGValue(update[i])=='' || getNGValue(update[i])=='--Select--'))
		 {
			 showAlert(update[i],name[i]+" update is mandatory");
			 return false;
		 }
		 else if(getNGValue(ver[i])=='Mismatch' && (getNGValue(update[i])==getNGValue(val[i])))
		 {
			 showAlert(update[i],name[i]+" update cannot be same as value");
			 return false;
		 }
	 }
	 return true;
	 
 }
 //below code added by nikhil 29/12/17
function checkMandatory_SupplementGrid()
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
					showAlert('SupplementCardDetails_DOB',alerts_String_Map['VAL045']);
					return false;
		}			
		else if(getNGValue('SupplementCardDetails_nationality')=='--Select--'){
					showAlert('SupplementCardDetails_nationality',alerts_String_Map['VAL090']);
					return false;
		}			
		else if(getNGValue('SupplementCardDetails_MobNo')==''){
					showAlert('SupplementCardDetails_MobNo',alerts_String_Map['VAL084']);			
					return false;
		}			
		else if(com.newgen.omniforms.formviewer.isLocked('SupplementCardDetails_FetchDetails')==false){
			showAlert('SupplementCardDetails_FetchDetails',alerts_String_Map['PL278']);			
			return false;
		}				
		else if(getNGValue('SupplementCardDetails_ResdCountry')==''){
			showAlert('SupplementCardDetails_ResdCountry',alerts_String_Map['VAL133']);
			return false;
		}	
		else if(getNGValue('SupplementCardDetails_gender')=='--Select--'){
					showAlert('SupplementCardDetails_gender',alerts_String_Map['VAL064']);
					return false;
			}		
		else if(getNGValue('SupplementCardDetails_Relationship')=='--Select--' || getNGValue('SupplementCardDetails_Relationship')==''){
			showAlert('SupplementCardDetails_Relationship',alerts_String_Map['VAL128']);
			return false;
		}
		else if(getNGValue('SupplementCardDetails_MotherNAme')==''){
			showAlert('SupplementCardDetails_MotherNAme',alerts_String_Map['VAL247']);
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
		else if(getNGValue('SupplementCardDetails_cardEmbName')==''){
					showAlert('SupplementCardDetails_cardEmbName',alerts_String_Map['VAL029']);
					return false;
			}		
		
			else if(getNGValue('SupplementCardDetails_EmailID')=='' && isVisible('SupplementCardDetails_EmailID')){
				showAlert('SupplementCardDetails_EmailID','Email ID cannot be blank');
				return false;
			}
		// hritik 23.6.21 PCASI 3420
			else if(getNGValue('SupplementCardDetails_Text1')=='')
			{
				showAlert('SupplementCardDetails_Text1','Please fill approved limit');
				return false;
			}
			
		var Id = getNGValue('SupplementCardDetails_Text6');
			var Dob=getNGValue('SupplementCardDetails_DOB');
			if((Id!="")&&((Id.length != 15) ||(Id.substr(0,3)!='784'))){
				if(Id.substr(0,3)!='784'){
					showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL108']);
				}
				else{
					showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL108']);
				}
				return false;
			}
			
			else if(Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9))
			{
				showAlert('SupplementCardDetails_Text6',alerts_String_Map['VAL074']);
				return false;
			} 		
			return true;
								
	}

//--abobe code added by nikhil for toteam
	
//added by akshay on 12/1/18	
function checkForNA_AddressGrid()
	{
		for(var i=0;i<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');i++){
			if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,5)=='NA' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,5)=='')//city
			{
				showAlert('city',alerts_String_Map['VAL346']);
				return false;
			}
			if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,6)=='NA' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,6)=='')//state
			{
				showAlert('state',alerts_String_Map['VAL345']);
				return false;
			}
			if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,7)=='NA' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,7)=='')//country
			{
				showAlert('country',alerts_String_Map['VAL347']);
				return false;
			}
		/*	if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,9)=='' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,9)=='NA')//years in current address
			{
				showAlert('AddressDetails_years','Please enter Years in Current Address');
				return false;
			} */
		}
		return true;
	}

	function checkFor_SupplemntGrid()
	{
		if (getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid')>0){
		for(var i=0;i<getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid');i++){
			if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,32)=='' )//Employment Status
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL056']);
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,21)=='' )//Email Id
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL207']);
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,31)=='' )//occupation
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL048']);
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,37)=='' )//Marital Status
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL081']);
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,16)=='' )//nON Resident
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['PL018']);
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,8)=='' )//Resident COuntry
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL133']);
				return false;
			}
			
			
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,14)=='' )//Title
			{
				showAlert('SupplementCardDetails_Frame1','Please add Title');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,34)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add ID Issue Date');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,36)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Passport Issue Date');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,35)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Visa Issue Date');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,15)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please Add Passport Expiry Date');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,18)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Emirates Expiry Date');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,23)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Visa Expiry Date');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,22)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please Enter Visa Number');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,40)=='' )//Employent Type
			{
				showAlert('SupplementCardDetails_Frame1','Please Enter Employment Type');
				return false;
			}
			
			}
		}
		return true;
	}
function checkMandatory_Add_CardDetails(){
	if(getNGValue("cmplx_CardDetails_securitycheck")==true){
		if(getNGValue('CardDetails_BankName')==""){
		showAlert('CardDetails_BankName',alerts_String_Map['CC018']);	
			return false;
		}
		else if(getNGValue('CardDetails_ChequeNumber')==""){
		showAlert('CardDetails_ChequeNumber',alerts_String_Map['CC037']);	
			return false;
		}
		else if(getNGValue('CardDetails_Amount')==""){
		showAlert('CardDetails_Amount',alerts_String_Map['CC007']);	
			return false;
		}
		else if(getNGValue('CardDetails_Date')==""){
		showAlert('CardDetails_Date',alerts_String_Map['CC058']);	
			return false;
		}
		
	}
	if(getNGValue('cmplx_CardDetails_MarketCode')=="" || getNGValue('cmplx_CardDetails_MarketCode')=="--Select--"){
		showAlert('cmplx_CardDetails_MarketCode',alerts_String_Map['PL129']);	
		return false;
	}
	
	return true;
}	
//Added by prabhakar
	function checkMandatory_AddressDetails_Save(){
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			var custTypePickList = document.getElementById("AddressDetails_CustomerType");
			var picklistValues=getPickListValues(custTypePickList);
			if(n==0){
				showAlert('cmplx_AddressDetails_cmplx_AddressGrid',alerts_String_Map['CC097']);	
				return false;
			}
			 else if(picklistValues.length>n)
			{
				showAlert('AddressDetails_CustomerType',alerts_String_Map['CC259']);	
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
					//alert(gridValue.indexOf(picklistValues[i]));
					if((gridValue.indexOf(picklistValues[i])==-1))
					{
						showAlert('AddressDetails_CustomerType',alerts_String_Map['CC267']+picklistValues[i]);
						return false;
						break;
					}
				}
			} 
			return true;
		}
		//added by prabhakar
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
		appType_selectedRow='G-'+getLVWAT('cmplx_Guarantror_GuarantorDet',getNGListIndex('cmplx_GuarantorDetails_cmplx_GuarantorGrid'),7)+' '+getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',getNGListIndex('cmplx_GuarantorDetails_cmplx_GuarantorGrid'),9);
	}
	
	if(getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid')>0)
	{
		for(var i=0;i<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');i++)
		{
			if(Trim(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13))==appType_selectedRow)
			{
				showAlert('AddressDetails_CustomerType',alerts_String_Map['VAL359']);
				return false;
			}
		}
	}
	if(getLVWRowCount('cmplx_FATCA_cmplx_FATCAGrid')>0)
	{
		for(var i=0;i<getLVWRowCount('cmplx_FATCA_cmplx_FATCAGrid');i++)
		{
			if(Trim(getLVWAT('cmplx_FATCA_cmplx_FATCAGrid',i,13))==appType_selectedRow)
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
				showAlert('OECD_CustomerType',alerts_String_Map['VAL362']);
				return false;
			}
		}
	}
return true;	

}		

function checkMandatory_FATCA()
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_FATCA_cmplx_FATCAGrid");
				var custTypePickList = document.getElementById('cmplx_FATCA_CustomerType');
				var picklistValues=getPickListValues(custTypePickList);
				
				if((picklistValues.length)>n || n==0)
				{
					showAlert('cmplx_FATCA_CustomerType','Please add FATCA for all Applicant type');	
						return false;
				}
		return true;
	}	
	
	function checkMandatory_KYC()
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
		var custTypePickList = document.getElementById("KYC_CustomerType");
		var picklistValues=getPickListValues(custTypePickList);
		
		
			if((picklistValues.length)>n ||n==0)
			{
				showAlert('KYC_CustomerType','Please add kyc for all Applicant type');	
				return false;
			}
		/*	else if(activityName == 'Compliance' && getNGValue('cmplx_RiskRating_Total_riskScore') >= '4')
			{	//pcasi-3721
				if(getNGValue('KYC_Combo1')=='')
				{
					showAlert('KYC_Combo1','KYC mandatory as this is a High Risk Case');
					return false;
				}
			} */
			else if(getNGValue('KYC_Combo1')=='Y' && getNGValue('KYC_DatePicker1')=='')
			{
				showAlert('','KYC Review Date cannot be blank for any row where KYC held is Yes');
				return false;
			}
			
		
		return true;
	}
	
	
	function checkMandatory_OECD()
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			var custTypePickList = document.getElementById("OECD_CustomerType");//Added by prabhakar drop-4Point-3
			var picklistValues=getPickListValues(custTypePickList);//Added by prabhakar drop-4Point-3
			if((picklistValues.length)>n || n==0)
			{
				showAlert('OECD_CustomerType','Please add OECD for all Applicant  type');	
					return false;
			}
				return true;
	}	
	
	function checkMandatory_FinacleCRM()
	{
		if(getNGValue('cmplx_Customer_NTB')==false){
			var  consider_for_obligations=false;
			if(getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid')>0)
			{
				for(var i=0;i<	getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');i++){
					if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=='true')
					{
						consider_for_obligations=true;
					}
				}
				
				if(consider_for_obligations==false){
					showAlert('FinacleCRMCustInfo_Text1','Consider for Obligations not checked in Finacle CRM grid!!');
					return false;
				}
			}
			
		}	
		return true;	
	}

		function checkMandatory_IMD()
		{	
			if(activityName =='Disbursal_Maker')
			{
			if(getNGValue('cmplx_LoanDetails_paymode')=='C')
			{
				return checkMandatory('cmplx_LoanDetails_chqid#Cheque ID,cmplx_LoanDetails_chqno#CR/Cheque/DD no,cmplx_LoanDetails_chqdat#Cheque Date,cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_reason#Reason,cmplx_LoanDetails_micr#MICR');
			}
			if(getNGValue('cmplx_LoanDetails_paymode')=='T')
			{
				return checkMandatory('cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_bankdeal#Dealing Bank');
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
			setNGFrameState('DecisionHistory',1);
			return false;			
			}
	}
	}
	return true;
}	




function checkMandatory_Add()
{
      var Decision =getNGValue("cmplx_Decision_Decision");
	  var ReferTo=getNGValue("cmplx_Decision_ReferTo");
	  var DecisionReasonCode=com.newgen.omniforms.formviewer.getNgSelectedValues('DecisionHistory_DecisionReasonCode');
	  var DecisionSubReason=com.newgen.omniforms.formviewer.getNgSelectedValues('DecisionHistory_DecisionSubReason');

	  var remarks=getNGValue('cmplx_Decision_REMARKS');
	  
        if(Decision==""||Decision=="--Select--" )
		{
			showAlert('cmplx_Decision_Decision',"Please select decision value");
			return false;
		}
		else if(Decision=='Refer' && (ReferTo=='' || ReferTo=='--Select--') && getNGValue('FircoStatusLabel')!='Hit')
		{
			showAlert('cmplx_Decision_ReferTo',"Please select a value in Refer To");
			return false;
		}
		else if((Decision=='Refer' || Decision=='Reject') && (DecisionReasonCode=='' || DecisionReasonCode=='--Select--')&& getNGValue('FircoStatusLabel')!='Hit')
		{
			showAlert('DecisionHistory_DecisionReasonCode',"Please select decision reason value");
			return false;
		}
		else if (isVisible('DecisionHistory_DecisionSubReason')==true && isEnabled('DecisionHistory_DecisionSubReason')==true ){
			 if((Decision=='Refer' || Decision=='Reject') && (DecisionSubReason=='' || DecisionSubReason=='--Select--')&& getNGValue('FircoStatusLabel')!='Hit'){
				 showAlert('DecisionHistory_DecisionSubReason',alerts_String_Map['PL434']);
				 return false;
			 }

		}
		else if(isVisible('cmplx_Decision_REMARKS')==true && remarks=='')
		{
			showAlert('cmplx_Decision_REMARKS',alerts_String_Map['CC190']);
			return false;
		}//by shweta
        
		else if(Decision=='Refer' && (ReferTo=='FPU'|| ReferTo=='FCU') /* && (activityName=='CAD_Analyst1' || activityName=='CPV')*/) //to trigger the alert for all the worksteps , changes by jahnavi to give alert for report generation at multiple refers
		{
			setNGValue('Is_FPU_Generated','N');
			fpu_generated==false;
			if(isVisible('SmartCheck1_Frame1')==false)
			{
				showAlert('','Please Visit Smart Section for FPU Remarks');
				return false;
			}
			var smart_grid=getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU');
			var identifier =true;
			if(smart_grid==0)
			{
			showAlert('','Please Add Smart Check for FPU');
			return false;
			}
			for(var i=0;i<smart_grid;i++)
			{
				if(getLVWAT('cmplx_SmartCheck1_SmartChkGrid_FCU',i ,2)=='')
				{
				identifier=false;
				}
			}
			if(identifier)
			{
			showAlert('','Please Add Smart Check for FPU');
			return false;
			}
			
		
		}
		else if(activityName=='FCU' ||  activityName=='FPU')
		{
			if(isVisible('SmartCheck1_Frame1')==false)
			{
			showAlert('','Please Visit Smart Section for FPU Remarks');
			return false;
			}
			var identifier=false;
			var smart_grid=getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU');
			for(var i=0;i<smart_grid;i++)
			{
			if(getLVWAT('cmplx_SmartCheck1_SmartChkGrid_FCU',i ,2)=='')
			{
			identifier=true;
			}
			}
			if(identifier)
			{
			showAlert('','Please complete Remarks for Smart Check Section');
			return false;
			}
		}
		else if(Decision=='Refer' && ReferTo=='Smart CPV'  && (activityName=='CAD_Analyst1')){
			if(isVisible('SmartCheck_Frame1')==false)
			{
				showAlert('','Please Visit Smart Section for CPV Remarks');
				return false;
			}
			var smart_grid=getLVWRowCount('cmplx_SmartCheck_SmartCheckGrid');
			var identifier_CPV =true;
			if(smart_grid==0)
			{
			showAlert('','Please Add Smart Check for CPV');
			return false;
			}
			for(var i=0;i<smart_grid;i++)
			{
				if(getLVWAT('cmplx_SmartCheck_SmartCheckGrid',i ,1)=='')
				{
				identifier_CPV=false;
				}
			}
			if(identifier_CPV)
			{
			showAlert('','Please Add Smart Check for CPV');
			return false;
			}
		}
		else if(activityName=='Smart_CPV'){
			if(isVisible('SmartCheck_Frame1')==false)
			{
			showAlert('','Please Visit Smart Section for CPV Remarks');
			return false;
			}
			//decision id corrected PCASI-3462
			if(getNGValue('cmplx_Decision_Decision')!='Smart CPV Hold')
			{
			var identifier=false;
			var smart_grid=getLVWRowCount('cmplx_SmartCheck_SmartCheckGrid');
			for(var i=0;i<smart_grid;i++)
			{
			if(getLVWAT('cmplx_SmartCheck_SmartCheckGrid',i ,1)=='')
			{
				identifier=true;
			}
			//else added to check cpv remarks PCASI-3462
			else{
				identifier=false;
			}
			}
			if(identifier)
			{
			showAlert('','Please complete Remarks for Smart Check Section');
			return false;
			}
			}
		}
		else if(activityName=='CAD_Analyst1')
		{
			var category=getNGValue('DectechCategory');
			if(category=='A' && Decision!='Reject')
			{
				showAlert('','Dectech Category A can only Declined');
				return false;

			}
		}
		else if(activityName=='Disbursal_Maker')
		{	
			if(Decision=='Approve')
			{
			if(!validatePastDate("cmplx_LoanDetails_frepdate","Repayment"))
			{
				showAlert('',"You cannot approve with past dates");
				return false;
			}
			}
				
		}
		
     return true;
}


function Add_Validate()
{
	var flag_add = false;
    var n = getLVWRowCount("Decision_ListView1");
    var activityname=window.parent.stractivityName;
	var count=0;
	var same=0;//by 
	var non_multiple_refer=0;
	if(n>0)
		{
							
			for(var i=0;i<n;i++)
			{
				if(getLVWAT('Decision_ListView1',i,2)==activityname && getLVWAT('Decision_ListView1',i,12)!='Y')
				{
					count=count+1;
				}
				//below code by nikhil 29/11 to avoid add same WS refer
				if(getLVWAT('Decision_ListView1',i,6)==getNGValue('cmplx_Decision_ReferTo') && getLVWAT('Decision_ListView1',i,12)!='Y')
				{
					same=1;
				}
				if((getLVWAT('Decision_ListView1',i,6)=='Credit' || getLVWAT('Decision_ListView1',i,6)=='Smart CPV' || getLVWAT('Decision_ListView1',i,6)=='Mail Approval' || getLVWAT('Decision_ListView1',i,6)=='CPV Checker' || getLVWAT('Decision_ListView1',i,6)=='DDVT_maker') && getLVWAT('Decision_ListView1',i,12)!='Y')
				{
					non_multiple_refer=1;
				}
				
			}
			
			if(getNGValue("cmplx_Decision_Decision")=='Refer')
			{	
				if(getLVWAT('Decision_ListView1',n-1,3)=='Refer' && getLVWAT('Decision_ListView1',i,12)!='Y' && getLVWAT('Decision_ListView1',n-1,2)==activityname && (getNGValue('cmplx_Decision_ReferTo')=='Smart CPV' ||getNGValue('DecisionHistory_ReferTo')=='Mail Approval' || getNGValue('cmplx_Decision_ReferTo')=='Credit' || getNGValue('cmplx_Decision_ReferTo')=='DDVT_maker'|| getNGValue('cmplx_Decision_ReferTo')=='CPV Checker'))
				{
					showAlert("","Multiple Refer cannot be added with this Workstep.");
					return false;
				}
				if(same==1)
				{
					showAlert('',alerts_String_Map['CC275']);
					return false;
				}
				//changes for PCSP-925
				if(activityname!='CAD_Analyst1' && activityname!='CPV' && activityname!='CAD_Analyst2'){//on these user can select multiple refer  
					if(count==1){
						showAlert('',alerts_String_Map['CC253']);
						return false;
					}
				}
				//below code added by nikhil for non-multiple refer WS
				else if(count==1)
				{
					if(getNGValue('cmplx_Decision_ReferTo')=='Credit' || getNGValue('cmplx_Decision_ReferTo')=='Smart CPV' || getNGValue('cmplx_Decision_ReferTo')=='Mail Approval' || getNGValue('cmplx_Decision_ReferTo')=='CPV Checker')
					{
						showAlert('','Multiple Refer Cannot be Used with these Workstep!');
						return false;
					}
					if(non_multiple_refer==1)
					{
						showAlert('','Multiple Refer Cannot be Used with these Workstep!');
						return false;
					}
				}
				else if(count==2){
						showAlert('',"Only a maximum 2 worksteps can be referred to!!!");
						return false;
					}
			}
			else
			{
				if(count>=1){
						showAlert('',"Cannot add two decision values!!!");
						return false;
				}
			}
		}
   
	var flag_ccdd = 1;
	return true;
}

function modify_row()
{//by shweta 
	var activityname=window.parent.stractivityName;
	var selectedRow=com.newgen.omniforms.formviewer.getNGListIndex('Decision_ListView1');

	if(getLVWAT('Decision_ListView1',selectedRow,12)=='Y') 
	{
		showAlert('','Only newly added rows can be modified!!!');
		return false;	}
	//below code added by nikhil by nikhil for PCSP-320
	var flag_add = false;
    var n = getLVWRowCount("Decision_ListView1");
    var activityname=window.parent.stractivityName;
	var count=0;
	var same=0;
	var non_multiple_refer=0;
	if(n>0)
		{
           for(var i=0;i<n;i++)
			{
				if(getLVWAT('Decision_ListView1',i,2)==activityname && getLVWAT('Decision_ListView1',i,12)!='Y')
				{
					count=count+1;
				}
				//below code by nikhil 29/11 to avoid add same WS refer
				if(getLVWAT('Decision_ListView1',i,6)==getNGValue('cmplx_Decision_ReferTo') && getLVWAT('Decision_ListView1',i,12)!='Y')
				{
					same=1;
				}
				if((getLVWAT('Decision_ListView1',i,6)=='Credit' || getLVWAT('Decision_ListView1',i,6)=='Smart CPV' || getLVWAT('Decision_ListView1',i,6)=='Mail Approval' || getLVWAT('Decision_ListView1',i,6)=='CPV Checker'||getLVWAT('Decision_ListView1',i,6)=='DDVT_maker') && getLVWAT('Decision_ListView1',i,12)!='Y')
				{
					non_multiple_refer=1;
				}
				
			}
			
			if(getNGValue("cmplx_Decision_Decision")=='Refer')
			{
				//commented by nikhil as not required in modify PCSP-613
				//below code by nikhil 29/11 to avoid add same WS refer
				/*if(same==1)
				{
					showAlert('',alerts_String_Map['CC275']);
					return false;
				}*/
				//change for PCSP-925
				// hritik 28.6.21 PCASI - 3453 ( removed - getNGValue('cmplx_Decision_ReferTo')=='Smart CPV' ||)
				if(getLVWAT('Decision_ListView1',n-1,3)=='Refer' && getLVWAT('Decision_ListView1',i,12)!='Y' && getLVWAT('Decision_ListView1',n-1,2)==activityname && (getNGValue('DecisionHistory_ReferTo')=='Mail Approval' || getNGValue('cmplx_Decision_ReferTo')=='Credit' || getNGValue('cmplx_Decision_ReferTo')=='DDVT_maker'|| getNGValue('cmplx_Decision_ReferTo')=='CPV Checker'))
				{
					showAlert("","Multiple Refer cannot be added with this Workstep.");
					return false;
				}
			
				if(activityname!='CAD_Analyst1' && activityname!='CPV' && activityname!='CAD_Analyst2'){//on these user can select multiple refer  
					if(count==1){
						showAlert('',alerts_String_Map['CC253']);
						return false;
					}
				}
				//below code added by nikhil for non-multiple refer WS
				else if(count==2)
				{
				if(getNGValue('cmplx_Decision_ReferTo')=='Credit' || getNGValue('cmplx_Decision_ReferTo')=='Smart CPV' || getNGValue('cmplx_Decision_ReferTo')=='Mail Approval' || getNGValue('cmplx_Decision_ReferTo')=='CPV Checker')
				{
					showAlert('','Multiple Refer Cannot be Used with these Workstep!');
					return false;
				}
				if(non_multiple_refer==1)
				{
					showAlert('','Multiple Refer Cannot be Used with these Workstep!');
					return false;
				}
				}
				
			}
			
		}

		return true;
}
 function fpureport_condition()
 {
 var activityname = window.parent.stractivityName;
	var activityNameforNotepad = '';
	
	//below lines changed by akshay on 23/11/18
	var newAddedrow=0;
	var ReferTo="";
	
	for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
	{
		if(getLVWAT('Decision_ListView1',i,12)=='')
		{
			
			//below code added by nikhil on 28/11/18 as referto wrong value set on first time
			if(newAddedrow>0)
			{
			ReferTo+=','+getLVWAT('Decision_ListView1',i,6);
			}
			else
			{
			ReferTo+=getLVWAT('Decision_ListView1',i,6);
			}
			newAddedrow++;
		}			
	}
	if(newAddedrow==0)
	{	
		showAlert('cmplx_Decision_Decision', alerts_String_Map['PL165']);
		return false;
	} else {

		setNGValueCustom('q_MailSubject',ReferTo);
		setNGValueCustom('ReferTo',ReferTo); //changed by aman for name change
	
	}//added by shweta for refer cases
	if(getNGValue('cmplx_Decision_Decision')=='Reject' || getNGValue('cmplx_Decision_Decision')=='Refer')  
	{
		if((activityName=='FCU'  || activityName=='FPU'  || activityName=='Compliance') && getNGValue('cmplx_Decision_Decision')=='Refer' && validateReferGridonRefer()){		
			return true;
		}	
	}
		
	 if(activityName=='FCU' || activityName=='FPU'){
		if(!checkMandatory(PL_Decision_FCU))
			return false;
	   	 if (getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU')>0){
	   	 
	              for(var i=0;i<getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU');i++)
	   			{
	   				if(getLVWAT('cmplx_SmartCheck1_SmartChkGrid_FCU',i,2)==''){
	   					showAlert('cmplx_SmartCheck1_SmartChkGrid_FCU','Please fill smart check');
	   					return false;
	   			}
	   	  }
	   	}
		}
		var Decision=getNGValue("cmplx_Decision_Decision")
	if((activityName=='FCU'  || activityName=='FPU'  || activityName=='Compliance' || activityName=='DSA_CSO_Review' || activityName=='CPV_Analyst'  || activityName=='RM_Review') && Decision=='Information Updated')
	{
		if(!validateReferGrid())
		{
			return false;
		}
	}
	 if(!DMandatoryPL()){
		return false;
	}
	return true;
	}
function delete_dec_row()
{//by shweta on 05-05-2020
	var activityname=window.parent.stractivityName;
	var selectedRow=com.newgen.omniforms.formviewer.getNGListIndex('Decision_ListView1');
	if(selectedRow==-1)
	{
		showAlert('','Please select a row to delete');
		return false;
	}
	else if(getLVWAT('Decision_ListView1',selectedRow,12)=='Y')
	{
		showAlert('','Only newly added rows can be deleted!!!');
		return false;
	}
	return true;
}

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
	else if((docStatus!='' && docStatus!='--Select--')  && docStatus=='Deferred' && (defuntildate=='' || defuntildate==null)){
		showAlert('IncomingDocNew_DeferredUntilDate','Please input Deferred until Date');
		return false;
	}
	else if(docIndex=='' && docStatus=='Received'){
		showAlert('IncomingDocNew_Status','Kindly attach Document first');
		return false;
	}
	return true;
}
//added by nikhil for Duplicate row entry check
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
//below function by saurabh for incoming doc new	
//changes by saurabh for Defer Waiver functionality on 5th Feb 19.
function Check_Documents_Submit()
{
	for(var docGrid_count=0;docGrid_count<getLVWRowCount('cmplx_IncomingDocNew_IncomingDocGrid');docGrid_count++)
	{
		if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',docGrid_count,4)=='Deferred'){
			setNGValueCustom("is_deferral_approval_require","Y");
			setNGValue("Deferral_req","Y");
		}
		if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',docGrid_count,4)=='Waived'){
			setNGValueCustom("is_waiver_approval_require","Y");
			setNGValue("Waiver_req","Y");
		}
		
		
	}
	return true;
}
//below code added by nikhil 21-04-2019 for CPV changes
function CheckCADecision(CAD_Dec)
{
var CA_Dec=getNGValue('CAD_ANALYST1_DECISION');
if(CA_Dec=='Reject' && (CAD_Dec!='Reject' && CAD_Dec!='Sendback'))
{
	showAlert('','Decision cannot be '+CAD_Dec+' as CA Decision is '+CA_Dec);
	setNGValue('cmplx_Decision_Decision','--Select--');//cmplx id corrected by shweta
	return false;
}
else if(CA_Dec=='Refer' && (CAD_Dec!='Refer' && CAD_Dec!='Sendback'))
{
	showAlert('','Decision cannot be '+CAD_Dec+' as CA Decision is '+CA_Dec);
	setNGValue('cmplx_Decision_Decision','--Select--');//cmplx id corrected by shweta
	return false;
}
return true;
}	

 // added by abhishek for smart CPV dec check as per CC FSD
 function checkSmartCPVDec(){
	 var dec = getNGValue('cmplx_Decision_Decision');
	 if(dec == 'Smart CPV Hold'){
		 return false;
	 }
	 else{
		 return true;
	 }
 }
 //by shweta
 function Check_Elite_Customer()
 {
 		var Nationality=getNGValue("cmplx_Customer_Nationality");
 		var CustomerSubSeg=getNGValue("cmplx_Customer_CustomerSubSeg");
 		var Salary=getNGValue("cmplx_IncomeDetails_totSal").replace(",", "");
 		if(Nationality == 'AE')
 		{
 			return true;
 		}
 		else if(CustomerSubSeg=='PAM' && parseFloat(Salary)>=50000)
 		{
 			return true;
 		}
 		else
 		{
 			return false;
 		}
 }
 
 function DDS_Save_CC()
	{	// Below code added by shweta
	
	if(getNGValue('cmplx_Customer_NTB')=='false' || !getNGValue('cmplx_Customer_NTB'))//PCASP-3191
		{
			showAlert('','Service requests currently are available only for NTB customers in CAS');
			return false;
		}////by jahnavi for JIRA 3158
		if(isFieldFilled('cmplx_CC_Loan_DDS_CardProduct')==false || getNGValue('cmplx_CC_Loan_DDS_CardProduct')=='--Select--' 
			||  getNGValue('cmplx_CC_Loan_DDS_CardProduct')=='')
		{
			showAlert('cmplx_CC_Loan_DDS_CardProduct','Please add Card Product');
			return false;	
		}
			
		if(getNGValue('cmplx_CC_Loan_DDSFlag')==true){
			
			if(isFieldFilled('cmplx_CC_Loan_DDSMode')==false){
				showAlert('cmplx_CC_Loan_DDSMode',alerts_String_Map['VAL194']);
				return false;
			}
			else if(getNGValue('cmplx_CC_Loan_DDSMode')=='F' && getNGValue('cmplx_CC_Loan_DDSAmount')==''){
				showAlert('cmplx_CC_Loan_DDSAmount',alerts_String_Map['VAL191']);
				return false;
			}
			else if(getNGValue('cmplx_CC_Loan_DDSMode')=='P' && getNGValue('cmplx_CC_Loan_Percentage')==''){
				showAlert('cmplx_CC_Loan_Percentage',alerts_String_Map['VAL195']);
				return false;
			}
			//Added by Shivang for PCASP-1546
			else if(getNGValue('cmplx_CC_Loan_DDSMode')=='P' && (parseFloat(getNGValue('cmplx_CC_Loan_Percentage'))>100)){
				showAlert('cmplx_CC_Loan_Percentage',alerts_String_Map['VAL396']);
				return false;
			}
			else if(!checkMandatory(CC_DDS))
				return false;
		}
		return true;
	}
	
	function SI_Save_CC()
	{	// Below code added by shweta
		if(isFieldFilled('cmplx_CC_Loan_SI_CardProduct')==false || getNGValue('cmplx_CC_Loan_SI_CardProduct')=='--Select--'
			|| getNGValue('cmplx_CC_Loan_SI_CardProduct')=='')//added by shweta for pcasp-1377
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
		
		
//below code also fix point "30-Service Details#Validations not as per FSD."

		else if(getNGValue('cmplx_CC_Loan_StartMonth')==''){
			showAlert('cmplx_CC_Loan_StartMonth',alerts_String_Map['CC205']);
			return false;
		} 
		//below code added by nikhil for proc-3976
		else if(getNGValue('cmplx_CC_Loan_HoldType')=='')
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
//above code also fix point "30-Service Details#Validations not as per FSD."

//below code also fix point "30-Service Details#Validations not as per FSD."
		/*else if(getNGValue('cmplx_CC_Loan_SIOnDueDate')=='No')
//above code also fix point "30-Service Details#Validations not as per FSD."
		{
			if(getNGValue('cmplx_CC_Loan_SI_day')==''){
			showAlert('cmplx_CC_Loan_SI_day',alerts_String_Map['VAL273']);
			return false;
			}
		}*/
		
		else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='F')
		{
			if(getNGValue('cmplx_CC_Loan_FlatAMount')==''){
			showAlert('cmplx_CC_Loan_FlatAMount',alerts_String_Map['VAL274']);
			return false;
			}
		}
		if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='P'){
			if(getNGValue('cmplx_CC_Loan_SI_Percentage')==''){
			showAlert('cmplx_CC_Loan_SI_Percentage',alerts_String_Map['PL142']);
			return false;
			}
			else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='P' && (parseFloat(getNGValue('cmplx_CC_Loan_SI_Percentage'))>100)){
				showAlert('cmplx_CC_Loan_SI_Percentage',alerts_String_Map['VAL396']);
				return false;
			}
		}
		
		
		return true;
	}
	// Below code added by shweta
	function RVC_Save_Check()
	{
		
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
			setVisible("GuarantorDet", true);
			setNGValue("cmplx_Customer_minor", true);
			setLocked("cmplx_Customer_minor", true);
				setVisible("Customer_Label37",true);
				setVisible("Customer_Label35",true);
				setVisible("cmplx_Customer_guarname",true);
				setVisible("cmplx_Customer_guarcif",true);
				setTop("GuarantorDet",parseInt(getTop('ProductContainer'))+parseInt(getHeight("ProductContainer"))+15+"px");
				setTop("Incomedetails",parseInt(getTop('GuarantorDet'))+parseInt(getHeight("GuarantorDet"))+15+"px");
			
		}else {
			setVisible("GuarantorDet", false);
			setNGValue("cmplx_Customer_minor", false);
				setVisible("Customer_Label37",false);
				setVisible("Customer_Label35",false);
				setVisible("cmplx_Customer_guarname",false);
				setVisible("cmplx_Customer_guarcif",false);
		}
		
	}
	
	
	function Minor_PL_Demo_CheckOnDone(){
		var grid_name='cmplx_Guarantror_GuarantorDet';
		var i=0;
		if(getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid')>0){
			var flag_DataFound=false;
			for(var j=0;j<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');j++){
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',j,13)=='G-'+getLVWAT(grid_name,i,7)+' '+getLVWAT(grid_name,i,9)){
					flag_DataFound=true;
					break;
				}	
			}
			if(flag_DataFound==false){
				showAlert('cmplx_AddressDetails_cmplx_AddressGrid',alerts_String_Map['PL393']);	
				return false;	
			}								
		}	
		
		if(getLVWRowCount('cmplx_FATCA_cmplx_FATCAGrid')>0){
			var flag_DataFound=false;
			for(var j=0;j<getLVWRowCount('cmplx_FATCA_cmplx_FATCAGrid');j++){
				if(getLVWAT('cmplx_FATCA_cmplx_FATCAGrid',j,13)=='G-'+getLVWAT(grid_name,i,7)+' '+getLVWAT(grid_name,i,9)){
					flag_DataFound=true;
					break;
				}	
			}
			if(flag_DataFound==false){
				showAlert('cmplx_FATCA_cmplx_FATCAGrid',alerts_String_Map['PL429']);	
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
				showAlert('cmplx_KYC_cmplx_KYCGrid',alerts_String_Map['PL429']);	
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
	
	function check_Primary_Disbursal()
	{
		var lvname = 'cmplx_CCCreation_cmplx_CCCreationGrid';
			var rows = getLVWRowCount(lvname);
			var pass = true;
			if(rows>0){
				for(var i=0;i<rows;i++){
					if(getLVWAT(lvname,i,12)=='Primary'  && (getLVWAT(lvname,i,8)!='Y' || getLVWAT(lvname,i,9)!='Y' || getLVWAT(lvname,i,10)!='Y')){
					pass = false;
					break;
					}
				}
			}
			if(!pass && getLVWAT(lvname,getNGListIndex(lvname),12)=='Supplement' && getLVWAT(lvname,getNGListIndex(lvname),13)==getNGValue('cmplx_Customer_PAssportNo') ){
				showAlert(lvname, 'Kindly complete disbursal of all Primary cards to Proceed!');
				return false;
			}
			return true;
	}
	
	//below code added by jahnavi for risk score pdf generation
function generate_riskscore_pdf()

{	
if(riskScoreCalFlag == '1')
		{
			
			var riskRate_generated=true;
			var wi_name = window.parent.pid;
			var docName = "RISK_RATING_REPORT";
			var attrbList = "";		
			attrbList += RiskRatingTemplate(); 
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
					RemoveIndicator('GenTemp');
				}
				
			}
}

	//--above code added by nikhil for Self-Supp CR	
	//below code added by nikhil for PCAS-2408 CR


	


