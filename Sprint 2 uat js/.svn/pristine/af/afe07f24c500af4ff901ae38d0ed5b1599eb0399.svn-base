
function validate()
{
	//return true;
	if(getNGValue('cmplx_DecisionHistory_Decision')=='Reject'){
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
 
function Customer_Save_Check(){
     //updated by Tarang on 01/19/2018 against drop 4 point 25               
    if((getNGValue('cmplx_Customer_EmiratesID')=='' || (getNGValue('cmplx_Customer_EmiratesID').length!=15) || (getNGValue('cmplx_Customer_EmiratesID').substr(0,3)!='784'))&&(getNGValue('cmplx_Customer_NEP')== "")){
			if(getNGValue('cmplx_Customer_CardNotAvailable') != false && getNGValue('cmplx_Customer_marsoomID')=='' && getNGSelectedItemText('cmplx_Customer_ResidentNonResident')=='RESIDENT')
			{
				showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL211']);  
					return false;
			}

	}
				
	/*if ((getNGValue('cmplx_Customer_NEP')!="")&& ((getNGValue('cmplx_Customer_EIDARegNo')=='')||(getNGValue('cmplx_Customer_EIDARegNo')==null))){
		showAlert('cmplx_Customer_EIDARegNo',alerts_String_Map['VAL206']);
	}*/	//Arun (06/12/17) commented as this field is not mandatory
     if(getNGValue('cmplx_Customer_FIrstNAme')=='' || getNGValue('cmplx_Customer_FIrstNAme') == null){
		showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['VAL224']);  return false;      }
				
    else if(getNGValue('cmplx_Customer_LAstNAme')=='' || getNGValue('cmplx_Customer_LAstNAme') == null){
                showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['VAL239']); return false;      }
				
    else if(getNGValue('cmplx_Customer_DOb')=='' || getNGValue('cmplx_Customer_DOb') == null){
                showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL045']); return false;      }
				
    else if(isFieldFilled('cmplx_Customer_Nationality')==false){
                showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL090']); return false;      }
				
    else if(getNGValue('cmplx_Customer_MobNo')=='' || getNGValue('cmplx_Customer_MobNo') == null){
                showAlert('cmplx_Customer_MobNo',alerts_String_Map['VAL243']);    return false;      }
    else if(getNGValue('cmplx_Customer_PAssportNo')=='' && getNGValue('cmplx_Customer_Nationality')!='AE'){
                showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL249']); return false;      }
	
				
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
				
    else if((getNGValue('cmplx_Customer_IdIssueDate')=='')&&(getNGValue('cmplx_Customer_NEP') == "" || getNGValue('cmplx_Customer_NEP') == null) &&  isFieldFilled('cmplx_Customer_marsoomID')==false){
            showAlert('cmplx_Customer_IdIssueDate',alerts_String_Map['VAL235']); 
			return false;      			
	}
    else if((getNGValue('cmplx_Customer_EmirateIDExpiry')=='')&&(getNGValue('cmplx_Customer_NEP') == "" || getNGValue('cmplx_Customer_NEP') == null ) &&  isFieldFilled('cmplx_Customer_marsoomID')==false){
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
	
     if((getNGValue('cmplx_Customer_VisaNo')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);return false;      }
   else if((getNGValue('cmplx_Customer_VisaIssuedate')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_VisaIssuedate',alerts_String_Map['VAL355']);return false;      }
			
	else if((getNGValue('cmplx_Customer_VIsaExpiry')=='') && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N'){
                showAlert('cmplx_Customer_VIsaExpiry',alerts_String_Map['VAL292']);return false;      }
			
// added by yash on 19/7/2017 for removing the validation of emirates of visa proc 812
    else if (isFieldFilled('cmplx_Customer_EMirateOfVisa') == false && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_ResidentNonResident')!='N')
    {
        showAlert('cmplx_Customer_EMirateOfVisa', alerts_String_Map['VAL210']);
        return false;
    }
// ended 
	  else if(isFieldFilled('cmplx_Customer_ResidentNonResident')==false){
                showAlert('cmplx_Customer_ResidentNonResident',alerts_String_Map['VAL264']);  return false;      }      
	else if(getNGValue('cmplx_Customer_PassportIssueDate')==''){
                showAlert('cmplx_Customer_PassportIssueDate',alerts_String_Map['VAL354']); return false;   
		}
				
    else if(getNGValue('cmplx_Customer_PassPortExpiry')==''){
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
	
    else if((com.newgen.omniforms.util.getComponentValue(document.getElementById('cmplx_Customer_SecNAtionApplicable'))=='Yes')&&(isFieldFilled('cmplx_Customer_SecNationality')==false)){
                showAlert('cmplx_Customer_SecNationality',alerts_String_Map['VAL267']);return false;      }
    else if(isFieldFilled('cmplx_Customer_GCCNational')==false){
                showAlert('cmplx_Customer_GCCNational',alerts_String_Map['VAL229']);  return false;      }
				
    else if(isFieldFilled('cmplx_Customer_CustomerCategory')==false){
                showAlert('cmplx_Customer_CustomerCategory',alerts_String_Map['VAL182']);return false;      }
    else if(isFieldFilled('cmplx_Customer_yearsInUAE')==false &&(getNGValue('cmplx_Customer_Nationality')!='AE')){
                showAlert('cmplx_Customer_yearsInUAE',alerts_String_Map['VAL254']);return false;      }
    else if(isFieldFilled('cmplx_Customer_MAritalStatus')==false){
                showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['VAL081']);return false;      }
	else if(isFieldFilled('cmplx_Customer_MotherName')==false){
                showAlert('cmplx_Customer_MotherName',alerts_String_Map['VAL247']); return false;      }       
				
    else if(isFieldFilled('cmplx_Customer_EmirateOfResidence')==false){
                showAlert('cmplx_Customer_EmirateOfResidence',alerts_String_Map['VAL209']);return false;      }
    else if(isFieldFilled('cmplx_Customer_COuntryOFResidence')==false){
                showAlert('cmplx_Customer_COuntryOFResidence',alerts_String_Map['VAL177']);return false;      }
	
	else
		return true;
}		

//added by prabhakar
function KYC_add_Check(){
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			//var custTypePickList = document.getElementById("KYC_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			CustType=getNGValue("KYC_CustomerType");
			var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_KYC_cmplx_KYCGrid",i,0));
			}
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
	else if(getNGValue('KYC_Combo2')==""||getNGValue('KYC_Combo2')=="--Select--"){
		showAlert('cmplx_KYC_PEP',alerts_String_Map['CC111']);
		return false;
		}
			else
			{
				for(var i=0;i<gridValue.length;i++)
				{
					if(gridValue.indexOf(CustType)>-1)
					{
						showAlert('KYC_CustomerType','KYC Already Added for Customer '+CustType);
						return false;
						break;
					}
				}
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
				
		if(isFieldFilled('cmplx_IncomeDetails_grossSal')==false)
				showAlert('cmplx_IncomeDetails_grossSal',alerts_String_Map['VAL230']);
				
		else if(isFieldFilled('cmplx_IncomeDetails_totSal')==false)
				showAlert('cmplx_IncomeDetails_totSal',alerts_String_Map['VAL286']);
		
		else if(isFieldFilled('cmplx_IncomeDetails_AvgNetSal')==false)
					showAlert('cmplx_IncomeDetails_AvgNetSal',alerts_String_Map['VAL164']);
		
		
		else if(isFieldFilled('cmplx_IncomeDetails_SalaryDay')==false)
				showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['VAL265']);
		
		else if(isFieldFilled('cmplx_IncomeDetails_SalaryXferToBank')==false)
				showAlert('cmplx_IncomeDetails_SalaryXferToBank',alerts_String_Map['VAL324']);
		
		else if(getNGValue('cmplx_IncomeDetails_Is_Tenancy_contract')=='--Select--' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Personal Loan'){
			showAlert('cmplx_IncomeDetails_Is_Tenancy_contract',alerts_String_Map['VAL335']);
			return false;
		}	
		
		
			/*else if(getNGValue('cmplx_IncomeDetails_Accomodation')==null)
			{
			showAlert('cmplx_IncomeDetails_Accomodation','Accomodation Provided is Mandatory!');
			return false;
			}*/
		else
			return true;			
				
}

function IMD_Save_Check()
	{
		// ++ below code already present - 09-10-2017
		
		
		
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
			if(getNGValue('cmplx_LoanDetails_paymode')=='C')
			{
			return checkMandatory('cmplx_LoanDetails_chqid#Cheque ID,cmplx_LoanDetails_chqno#CR/Cheque/DD no,cmplx_LoanDetails_chqdat#Cheque Date,cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_reason#Reason,cmplx_LoanDetails_micr#MICR');
			}
			if(getNGValue('cmplx_LoanDetails_paymode')=='T')
			{
			return checkMandatory('cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_bankdeal#Dealing Bank');
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
			if((getNGValue('cmplx_LoanDetails_city')=='' || getNGValue('cmplx_LoanDetails_city')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_city','City is Mandatory');
				return false;
			}
			if((getNGValue('cmplx_LoanDetails_bankdeal')=='' || getNGValue('cmplx_LoanDetails_bankdeal')=='--Select--') && getNGValue('cmplx_LoanDetails_paymode')=='Q')
			{
				showAlert('cmplx_LoanDetails_bankdeal','Dealing Bank is Mandatory');
				return false;
			}
			return true;	
	}
	function Employment_Save_Check()
	{
		// ++ below code already present - 09-10-2017
		var activityname=window.parent.stractivityName;
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
			if(getNGValue('cmplx_EmploymentDetails_marketcode')=='' || getNGValue('cmplx_EmploymentDetails_marketcode')=='--Select--')
			{
				showAlert('cmplx_EmploymentDetails_marketcode',alerts_String_Map['PL041']);
				return false;
			}
			if((getNGValue('cmplx_EmploymentDetails_channelcode')=='' || getNGValue('cmplx_EmploymentDetails_channelcode')=='--Select--') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4).indexOf('RESC')>-1)
			{
				showAlert('cmplx_EmploymentDetails_channelcode',alerts_String_Map['PL042']);
				return false;
			}
			if((getNGValue('cmplx_EmploymentDetails_collectioncode')=='' || getNGValue('cmplx_EmploymentDetails_collectioncode')=='--Select--') && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4).indexOf('RES')>-1)
			{
				showAlert('cmplx_EmploymentDetails_collectioncode',alerts_String_Map['PL043']);
				return false;
			}
			if(getNGValue('cmplx_EmploymentDetails_marketcode')=='' || getNGValue('cmplx_EmploymentDetails_marketcode')=='--Select--')
			{
				showAlert('cmplx_EmploymentDetails_marketcode',alerts_String_Map['PL044']);
				return false;
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
		

		
		}
		
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
	else if(getNGValue('cmplx_EmploymentDetails_DOJ')=='' && getNGValue('EmploymentType').indexOf("Pensioner")==-1)
	{
		showAlert('cmplx_EmploymentDetails_DOJ',alerts_String_Map['PL411']);
		return false;	
	}	
	
		/*else if(getNGValue('cmplx_EmploymentDetails_Field_visit_done')=='')
	{
		showAlert('cmplx_EmploymentDetails_Field_visit_done',alerts_String_Map['PL412']);
		return false;	
	}*/
		
	else if(!checkMandatory(PL_EMPLOYMENT))
		return false;
		
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
					
		else if(getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate')=='')
					showAlert('cmplx_EligibilityAndProductInfo_FirstRepayDate',alerts_String_Map['PL078']);
					
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
	
	function ContactDetails_Save_Check()
	{
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
			else if(getNGValue('AlternateContactDetails_EMAIL2_SEC')==''){
					showAlert('AlternateContactDetails_EMAIL2_SEC',alerts_String_Map['VAL375']);
					return false;
		}
					
		//else if(getNGValue('AlternateContactDetails_ESTATEMENTFLAG')==''){
			//		showAlert('AlternateContactDetails_ESTATEMENTFLAG',alerts_String_Map['PL095']);	
		  //return false;
		//}
		
		else if(getNGValue('AlternateContactDetails_carddispatch')=='--Select--' && getNGValue('is_cc_waiver_require')!='Y'){
					showAlert('AlternateContactDetails_carddispatch',alerts_String_Map['PL096']);	
					return false;
		}
					
		else if(getNGValue('AlternateContactDetails_carddispatch')=='Branch')
		{
			if(getNGValue('AlternateContactDetails_custdomicile')=='--Select--'){
				showAlert('AlternateContactDetails_custdomicile',alerts_String_Map['PL097']);
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
			
			if(getNGValue('cmplx_CardDetails_cardemboss')==""){
				showAlert('cmplx_CardDetails_cardemboss',alerts_String_Map['VAL029']);
				return false;
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
	function FATCA_Save_Check()
	{
		var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails');
			var custTypePickList = document.getElementById("cmplx_FATCA_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			CustType=getNGValue("cmplx_FATCA_CustomerType");
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
				showAlert('cmplx_FATCA_IdDoc',alerts_String_Map['VAL268']);
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
			/*else if(getNGValue('cmplx_FATCA_ConPerHasUS')=='')
			{
						showAlert('cmplx_FATCA_ConPerHasUS',alerts_String_Map['VAL176']);
						return false;
			}*/
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
			if(gridValue.indexOf(CustType)>-1)
			{
						showAlert('cmplx_FATCA_CustomerType','FATCA Already Added for Customer'+CustType);
						return false;
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
	/* function KYC_save_Check()
	{
		// ++ below code already present - 09-10-2017
		if(getNGValue('cmplx_KYC_KYCHeld')=='' || getNGValue('cmplx_KYC_KYCHeld')=='--Select--')
				showAlert('cmplx_KYC_KYCHeld',alerts_String_Map['PL111']);
			// ++ below code already present - 09-10-2017	
		else if(getNGValue('cmplx_KYC_PEP')=='' || getNGValue('cmplx_KYC_PEP')=='--Select--')
					showAlert('cmplx_KYC_PEP',alerts_String_Map['PL112']);
		else
		return true;
	return false;
	} */
	
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
	function OECD_Add_Check()
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
		if(getNGValue('OECD_CRSFlag')=='' || getNGValue('OECD_CRSFlag')=='--Select--' )
			showAlert('OECD_CRSFlag',alerts_String_Map['PL117']);

		else if(getNGValue('OECD_CRSFlag')=='Y' )
		{
			if(getNGValue('OECD_CRSFlagReason')=='' || getNGValue('OECD_CRSFlagReason')=='--Select--' )
			showAlert('OECD_CRSFlagReason',alerts_String_Map['PL118']);
		}
		else if(getNGValue('OECD_CountryTaxResidence')=='' || getNGValue('OECD_CountryTaxResidence')=='--Select--' )
			showAlert('OECD_CountryTaxResidence',alerts_String_Map['PL119']);

		else if(getNGValue('OECD_CountryBirth')=='' || getNGValue('OECD_CountryBirth')=='--Select--' )
			showAlert('OECD_CountryBirth',alerts_String_Map['PL114']);

		else if(getNGValue('OECD_townBirth')=='' || getNGValue('OECD_townBirth')=='--Select--' )
			showAlert('OECD_townBirth',alerts_String_Map['PL120']);

		else if(getNGValue('OECD_tinNo')=='' || getNGValue('OECD_townBirth')=='--Select--' )
			showAlert('OECD_tinNo',alerts_String_Map['PL121']);

		else if(getNGValue('OECD_noTinReason')=='' || getNGValue('OECD_townBirth')=='--Select--' )
		{
			showAlert('OECD_noTinReason',alerts_String_Map['PL122']);
		}
		else if(gridValue.indexOf(CustType)>-1)
			{
						showAlert('OECD_CustomerType','OECD Already Added for Applcant '+CustType);
						return false;
			}

		else 
		return true;

	return false;
	}
	// ++ above code already present - 09-10-2017
	 function ServiceRequest_Save_Check()
	 {
		if(getNGValue('cmplx_CC_Loan_TransMode')=='Cheque'){
			if(!checkMandatory(RLOS_TRANSFERMODE_SERVICEREQUEST))
				return false;
		}
		else if(!checkMandatory(RLOS_SERVICEREQUEST))
				return false;
				
		else return true;		
	}	
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
		var allPreffered=0;//added by prabhakar
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
				
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,11) == 'true'){
						allPreffered++;
						//prefferedFlag = true;						
				}
				if(allPreffered==picklistValues.length)
			{
						prefferedFlag = true;						
				}
				
			}
			
			
			
				if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false && IS_HOME==false){
					showAlert('addtype',alerts_String_Map['PL363']);
					return false;
				}
				if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['PL365']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_HOME==false && IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['PL364']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false && IS_HOME==false){
					showAlert('addtype',alerts_String_Map['PL366']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_OFFICE==false ){
					showAlert('addtype',alerts_String_Map['PL369']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['PL368']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_ResidentNonResident')=="Y"&& IS_HOME==false){
					showAlert('addtype',alerts_String_Map['PL367']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && IS_OFFICE==false && IS_RESIDENCE==false ){
					showAlert('addtype',alerts_String_Map['PL375']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && IS_OFFICE==false){
					showAlert('addtype',alerts_String_Map['PL374']);
					return false;
				}	
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="Y" && ( IS_RESIDENCE==false)){
					showAlert('addtype',alerts_String_Map['PL373']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_HOME==false) && ( IS_RESIDENCE==false) ){
					showAlert('addtype',alerts_String_Map['PL372']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_RESIDENCE==false)){
					showAlert('addtype',alerts_String_Map['PL371'])
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="N" && (IS_HOME==false)){
					showAlert('addtype',alerts_String_Map['PL370'])
					return false;
				}
				else if(!prefferedFlag){
					showAlert('PreferredAddress','Please Add prefer Address for app Applicant');
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
		
		else if(getNGValue('cmplx_MOL_molexp')=='' || getNGValue('cmplx_MOL_molexp')==null)
				showAlert('cmplx_MOL_molexp',alerts_String_Map['PL152']);					
		
		else if(getNGValue('cmplx_MOL_molissue')=='')
					showAlert('cmplx_MOL_molissue',alerts_String_Map['PL153']);	
		
		else if(getNGValue('cmplx_MOL_ctrcttype')=='' || getNGValue('cmplx_MOL_ctrcttype')==null)
				showAlert('cmplx_MOL_ctrcttype',alerts_String_Map['PL154']);					
		
		else if(getNGValue('cmplx_MOL_molsalvar')=='')
				//change by saurabh on 15th Jan
					showAlert('cmplx_MOL_molsalvar',alerts_String_Map['PL155']);	
			
		else if(getNGValue('cmplx_MOL_docatt')=='')
					showAlert('cmplx_MOL_docatt',alerts_String_Map['PL156']);			
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
		if(getNGValue('cmplx_DecisionHistory_Decision')=='--Select--')
					showAlert('cmplx_DecisionHistory_Decision',alerts_String_Map['PL161']);		
					
		else if(getNGValue('cmplx_DecisionHistory_Decision')=='Reject'){
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
	
	
	for(var i=0;i<getLVWRowCount('Decision_ListView1');i++)
	{
		if(getLVWAT('Decision_ListView1',i,9)=='')
		{
			newAddedrow++;
		}		
	}
	if(newAddedrow==0)
	{
		showAlert('cmplx_Decision_Decision', alerts_String_Map['PL165']);
		return false;
	}
	
	/*if (getNGValue('cmplx_Decision_Decision') == '' || getNGValue('cmplx_Decision_Decision') == '--Select--' || getNGValue('cmplx_Decision_Decision') == null)
	{
		showAlert('cmplx_Decision_Decision', alerts_String_Map['PL165']);
		return false;
	}
	
	else if( (getNGValue('cmplx_Decision_Decision').toUpperCase()=='REFER' || getNGValue('cmplx_Decision_Decision').toUpperCase()=='REJECT'))
	{
		if(getNGValue('cmplx_Decision_Decision').toUpperCase()=='REFER' && (getNGValue('DecisionHistory_ReferTo')=='' || getNGValue('DecisionHistory_ReferTo')=='--Select--'))
		{
			showAlert('DecisionHistory_ReferTo',alerts_String_Map['CC255']);
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

    if (activityname == 'DDVT_maker')
    {
        if (getNGValue('Is_Customer_Create') == 'Y')
        {
            if (!getNGValue('aecb_call_status') == 'Success')
            {
                showAlert('ExtLiability_Button1', alerts_String_Map['PL163']);
                return false;
            }
        }
		
		if(!Customer_Save_Check()){
				return false;
		}	
		
		else if(!Employment_Save_Check()){
			return false;
			}
		else if(getNGValue('Is_Financial_Summary')!='Y'){
			showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
			return false;
		}
		else if(!Income_Save_Check()){
			return false;
		}
		
    }
	else if(activityname=='CAD_Analyst1'){
	var desc = getNGValue('cmplx_Decision_Decision');
		//below lines commented by akshay on 23/11/18	
	/*if(desc.toUpperCase()=='REFER' && (getNGValue('DecisionHistory_ReferTo')=='' || getNGValue('DecisionHistory_ReferTo')=='--Select--'))
		{
		showAlert('DecisionHistory_ReferTo',alerts_String_Map['CC255']);
				return false;
		}
		else if(getNGValue('DecisionHistory_DecisionReasonCode')=='' || getNGValue('DecisionHistory_DecisionReasonCode')=='--Select--')
		{
		showAlert('DecisionHistory_DecisionReasonCode',alerts_String_Map['VAL198']);
				return false;
		}*/
		if ((getNGValue('cmplx_Decision_CADDecisiontray')=='' || getNGValue('cmplx_Decision_CADDecisiontray')=='Select' ) && getNGValue('DectechCategory')!='A')
		{
		showAlert('cmplx_Decision_CADDecisiontray',alerts_String_Map['CC271']);
				return false;
		}
		
		else if(!Income_Save_Check()){
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
	else if(activityname=='Post_Disbursal'){
		if(!checkMandatory(PL_POST_DISBURSAL)){
			return false;
		}
	}
	else if(activityname=='FCU')
	{
		if(!checkMandatory(PL_Decision_FCU))
			return false;
	}//Arun (07/09/17)

  //below code commented by akshay on 23/11/18
  /* else if (getNGValue('cmplx_Decision_Decision') == '' || getNGValue('cmplx_Decision_Decision') == '--Select--' || getNGValue('cmplx_Decision_Decision') == null)
    {

        showAlert('cmplx_Decision_Decision', alerts_String_Map['PL165']);
        return false;
    }
	 else if(getNGValue('cmplx_Decision_REMARKS')=='')
    {
    	showAlert('cmplx_Decision_REMARKS', alerts_String_Map['CC190']);
        return false;
    }*/
    /*else if (PLFRAGMENTLOADOPT['ReferHistory'] != 'N' && getNGValue('cmplx_Decision_Decision') != null &&getNGValue('cmplx_Decision_Decision').indexOf('Compliance')>-1)
    {
        showAlert('ReferHistory', alerts_String_Map['PL166']);
        return false;
    }*/
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
	/*if(getNGValue('cmplx_Decision_Decision')=='Refer' && com.newgen.omniforms.formviewer.getNgSelectedValues('DecisionHistory_ReferTo')==''){
        showAlert('DecisionHistory_ReferTo', alerts_String_Map['CC255']);
        return false;
    }*/
	
	
	if(activityName=='FCU' || activityName=='Compliance' || activityName=='DSA_CSO_Review'){
		if(!validateReferGrid())
		{
			return false;
		}
	}	
	//ended by akshay on 12/9/17		

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
	else if(isFieldFilled('ReferenceDetails_ref_Relationship')==false){
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
	
	if(activityName=='Compliance')
	{
		var worldAdd = getNGValue('Is_WorldCheckAdd'); // flag check if row is added in world check grid
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
	}
	else if(activityName=='CAD_Analyst1' || activityName=='CAD_Analyst2')
	{
		// disha FSD
		var desc = getNGValue('cmplx_Decision_Decision');
		var highDelegAuth  = getNGValue('cmplx_Decision_Highest_delegauth');
		//alert(highDelegAuth);
		//change by saurabh as per FSD manual deviation not available in CAD2 screenshots.
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
	
	if(desc=='Approve')
	{
		//alert ("Decision approved");
		setLocked('cmplx_Decision_CADDecisiontray',true);		
	}
	else
	{
		setLocked('cmplx_Decision_CADDecisiontray',false);	
		
	} //Arun (08/10) for PROC 2773
	
	if(highDelegAuth=='' && getNGValue('DectechCategory')!='A'){
		showAlert('cmplx_Decision_Highest_delegauth',alerts_String_Map['PL238']);
		return false;
	}
	if(desc=='' || desc=='--Select--')
	{	showAlert('cmplx_Decision_Decision',alerts_String_Map['PL239']);
		return false;}
	if(desc=='Reject')
	{
		if(getNGValue('cmplx_Decision_Decreasoncode')=='' || getNGValue('cmplx_Decision_Decreasoncode')=='--Select--')
		{
			showAlert('cmplx_Decision_Decreasoncode',alerts_String_Map['PL239']);
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
	if(!Income_Save_Check()){
			return false;
		}
	
	}
	else if(activityName=='Smart_CPV')
	{
		var n=getLVWRowCount("cmplx_SmartCheck1_SmartChkGrid_FCU");
		if(n==1)
		{
			if(getLVWAT("cmplx_SmartCheck1_SmartChkGrid_FCU",0,2)=='')
			{
				showAlert('cmplx_Compliance_cmplx_gr_compliance',alerts_String_Map['PL241']);
				return false;
			}				
		}	
	}
	else if(activityName=='FCU')
	{
		//alert("inside FCU");
		var desc = getNGValue('cmplx_DEC_Decision');
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
		
		//ended 6th sept for proc 2021
			
	}
	// changes done to make CPV frgament tab decisions mandatory
	else if(activityName=='CPV')
	{
		 if (getNGValue('cmplx_HCountryVerification_Decision') == '' || getNGValue('cmplx_HCountryVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_HCountryVerification_Decision', alerts_String_Map['PL244']);
			return false;
		}
		 if (getNGValue('cmplx_ResiVerification_Decision') == '' || getNGValue('cmplx_ResiVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_ResiVerification_Decision', alerts_String_Map['PL245']);
			return false;
		}
		 if (getNGValue('cmplx_CustDetailVerification_Decision') == '' || getNGValue('cmplx_CustDetailVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_CustDetailVerification_Decision', alerts_String_Map['PL246']);
			return false;
		}
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
		 if (getNGValue('cmplx_OffVerification_Decision') == '' || getNGValue('cmplx_OffVerification_Decision') == '--Select--' || getNGValue('cmplx_OffVerification_Decision') == null)
		{
			showAlert('cmplx_OffVerification_Decision', alerts_String_Map['PL249']);
			return false;
		}
		 if (getNGValue('cmplx_LoanandCard_Decision') == '' || getNGValue('cmplx_LoanandCard_Decision') == '--Select--')
		{
			showAlert('cmplx_LoanandCard_Decision', alerts_String_Map['PL250']);
			return false;
		}		
	}
	
	
	// changes done to restrict user if any of the decision is pending in CPV tab fragmments than in main decision tab user cannot take Decision as Approve
	else if(activityName=='CPV')
	{
		if (getNGValue('cmplx_HCountryVerification_Decision') == 'Pending' || getNGValue('cmplx_ResiVerification_Decision') == 'Pending' || getNGValue('cmplx_CustDetailVerification_Decision') == 'Pending' || getNGValue('cmplx_BussVerification_Decision') == 'Pending' || getNGValue('cmplx_RefDetVerification_Decision') == 'Pending' || getNGValue('cmplx_OffVerification_Decision') == 'Pending' || getNGValue('cmplx_LoanandCard_Decision') == 'Pending')
		{
			if (getNGValue('cmplx_Decision_Decision') == 'Approve')
			{
				showAlert('cmplx_Decision_Decision', alerts_String_Map['PL252']);
				return false;
			}
		}
	}
	
	else if(activityName=='DDVT_maker')
	{
		//added by akshay on 21/10/18
		if(getNGValue('cmplx_Customer_NTB')==true && !checkMandatory(PL_RISKRATING))
		{
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
		else if(!checkMandatory_CardDetails_Save()){
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
		var row_count=getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');
		var cif_found=true;
		if(row_count>0)
		{
			for(var i=0;i<=row_count;i++)
			{
				if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)==getNGValue('cmplx_Customer_CIFNO') && getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=="true" )
				{
					cif_found=false;
				}
			}
						//change by saurabh on 20th Dec
				if(cif_found && getNGValue('cmplx_Customer_NTB')==true){
				showAlert('cmplx_Customer_CIFNO', ' Customer cannot be NTB when cif has been identified in Part Match');
				setNGValue('cmplx_Customer_NTB',false);
					return false;
			}
			if(cif_found)
			{
			showAlert('cmplx_Customer_CIFNO', ' CIF which is considered for obligations is not preset in Personal details');
			return false;
			}
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
			if(SecurityGridCount==0){
				showAlert('CardDetails_BankName', alerts_String_Map['CC264']);
				return false;
			}
		}
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
		
		//added by akshay on 2/7/18 for proc 11500
			if(getNGValue('AlternateContactDetails_RetainAccIfLoanReq')==true){
				
				 if(getLVWAT('cmplx_Decision_MultipleApplicantsGrid',primary_index,7)!='Y'){
					showAlert('DecisionHistory_Button3', 'Customer Update not done!!!');
					return false;
					}	
				else if(getNGValue('Is_ACCOUNT_MAINTENANCE_REQ')!='Y'){
					showAlert('DecisionHistory_updcust', 'Account Update not done!!!');
					return false;
					}
				else if((getNGValue('Is_CHEQUE_BOOK_ELIGIBILITY')!='Y' || getNGValue('Is_NEW_CARD_REQ')!='Y') && getNGValue('Account_Number')==''){
					showAlert('DecisionHistory_chqbook', 'Cheque book/Debit Card Request button not clicked!!!');
					return false;
					}
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
			if(!validateApprovedLimitSupplementary()){
				return false;
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
	// ++ above code already present - 09-10-2017 - code has been commented cauz PL_DECISION constant defined and decision is mandatory on done
	
	
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
				var grid_preffAddr=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,11);
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
					var grid_EmpType=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,6);
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
					var grid_EmpType=com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",i,6);
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
		
		else if(getNGValue('cmplx_DecisionHistory_IBAN')==""){
			showAlert('cmplx_DecisionHistory_IBAN',alerts_String_Map['PL258']);	
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
		else if(getNGValue('OECD_CRSFlag')=='Y'){
					if(getNGValue('OECD_CRSFlagReason')=='--Select--' || getNGValue('OECD_CRSFlagReason')=='' || getNGValue('OECD_CRSFlagReason')=='NA' ){
						showAlert('OECD_CRSFlagReason',alerts_String_Map['PL117']);
						return false;
					}
					if( getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')=='' ){
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['PL119']);
					return false;
		}
					if( getNGValue('OECD_tinNo')==''  && getNGValue('OECD_noTinReason')=='' ){
						showAlert('OECD_tinNo',alerts_String_Map['PL121']);
						return false;
					}
					// if(getNGValue('OECD_CountryBirth')=='--Select--' || getNGValue('OECD_CountryBirth')==''){
				//	showAlert('OECD_CountryBirth',alerts_String_Map['PL114']);
					//return false;
		//}
					//if(getNGValue('OECD_townBirth')=='--Select--' || getNGValue('OECD_townBirth')==''){
					//			showAlert('OECD_townBirth',alerts_String_Map['PL113']);
					//return false;
					//}
		}
		else if(getNGValue('OECD_CRSFlag')=='N')
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
		else
			{
				for(var i=0;i<gridValue.length;i++)
				{
					if(gridValue.indexOf(CustType)>-1)
					{
						showAlert('OECD_CustomerType','OECD Already Added for Customer'+CustType);
						return false;
						break;
					}
					}
		}
		return true;
	}
	
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
				else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
				return false;
				}
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
			else if(city=="" || city=="--Select--"){
				showAlert('AddressDetails_city',alerts_String_Map['VAL008']);	
				return false;
				}	
			else if( state=="--Select--" || state==""){
				showAlert('AddressDetails_state',alerts_String_Map['VAL137']);	
				return false;
				}
			else if(country=="" || country=="--Select--"){
				showAlert('AddressDetails_country',alerts_String_Map['VAL040']);	
				return false;
				}
				else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
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
				else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
				return false;
				}
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
	var cif=getNGValue('cif');
	var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_GuarantorDetails_cmplx_GuarantorGrid");
	for(var i=0;i<n;i++)
		if(getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',i,0)==cif){
			showAlert('cif',alerts_String_Map['PL295']);
			return false;
		}	
return true;
}

function checkMandatory_GuarantorGrid()
{
	if(getNGValue('cif')==''){
				showAlert('cif',alerts_String_Map['PL016']);
				return false;
	}			
	
	else if(flag_ReadFromCIF==false){
		showAlert('ReadFromCIF',alerts_String_Map['PL359']);
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
					 if(getNGValue('AlternateContactDetails_HomeCOuntryNo')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='HOME'){
					showAlert('AlternateContactDetails_HomeCOuntryNo',alerts_String_Map['PL311']);	
					return false;
					}
					if(getNGValue('AlternateContactDetails_OfficeNo')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='OFFICE'){
					showAlert('AlternateContactDetails_OfficeNo',alerts_String_Map['PL312']);	
					return false;
					}
					if(getNGValue('AlternateContactDetails_ResidenceNo')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='RESIDENCE'){
					showAlert('AlternateContactDetails_ResidenceNo',alerts_String_Map['PL313']);	
					return false;
					}
			}
			if(getNGValue('AlternateContactDetails_MobileNo1')=="")
			{
				showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['PL314']);	
				return false;
			}
			else if(getNGValue('AlternateContactDetails_MobNo2')==""){
				showAlert('AlternateContactDetails_MobNo2',alerts_String_Map['PL315']);	
				return false;
				}
			else if(getNGValue('AlternateContactDetails_Email1')==""){
				showAlert('AlternateContactDetails_Email1',alerts_String_Map['PL316']);	
				return false;
				}
			else if(getNGValue('AlternateContactDetails_eStatementFlag')==""||getNGValue('AlternateContactDetails_eStatementFlag')=="--Select--"){
			showAlert('AlternateContactDetails_eStatementFlag',alerts_String_Map['PL317']);	
			return false;
			}
			else if(getNGValue('AlternateContactDetails_CardDisp')==""||getNGValue('AlternateContactDetails_CardDisp')=="--Select--"){
			showAlert('AlternateContactDetails_CardDisp',alerts_String_Map['PL096']);
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
		showAlert('cmplx_BussVerification1_ContactPerson',alerts_String_Map['PL321']);
			return false;
	}
	if(getNGValue('cmplx_BussVerification1_ContactTelephoneCheck')==""||getNGValue('cmplx_BussVerification1_ContactTelephoneCheck')=="--Select--"){
		showAlert('cmplx_BussVerification1_ContactTelephoneCheck',alerts_String_Map['PL322']);
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
} //Arun from CC
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
		if(!checkMandatory(PL_LoanDetails)){
				return false;
		}
		else if(parseInt(getNGValue('cmplx_LoanDetails_lpfamt'))<parseInt(getNGValue('LoanDetails_MinLPF')) || parseInt(getNGValue('cmplx_LoanDetails_lpfamt'))>parseInt(getNGValue('LoanDetails_MaxLPF')))
		{
			showAlert('cmplx_LoanDetails_lpfamt','LPF Amount must be between '+getNGValue('LoanDetails_MinLPF')+' and '+getNGValue('LoanDetails_MaxLPF'));
			return false;
		}	
		else if(getNGValue('cmplx_LoanDetails_is_repaymentSchedule_generated')!='Y'){
			showAlert('LoanDetails_Button1','Please generate repayment Schedule in Loan Details!!');
				return false;
		}
			if(!validatePastDate('cmplx_EligibilityAndProductInfo_FirstRepayDate','Repayment')){
			return false;
		}
	return true;	
	}
	function LoanDetDisburse_Check()
	{
		var Mode_of_Disbursal=getNGValue("LoanDetails_modeofdisb");
		var Hold_Code=getNGValue("LoanDetails_holdcode");
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

//++below code added by nikhil for toteam
function CheckConditionsLC()
{
	var row_count=getLVWRowCount('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate');
	var activeflag=false;
	for(var i=0;i<=row_count;i++)
	{
		if(getLVWAT('cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate',i,7)=='Active' && getNGValue('PostDisbursal_Status')=='Active')
		{
			activeflag=true;
		}
	}
	if(activeflag)
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
		if(getLVWAT('cmplx_PostDisbursal_cpmlx_gr_NLC',i,2)=='Yes' )
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
	if(!primarybank && (getNGValue('cmplx_PostDisbursal_STLReceivedDate')!='' || getNGValue('cmplx_PostDisbursal_STLReceivedDate')!=''))
	{
		showAlert('cmplx_PostDisbursal_Remarks',alerts_String_Map['PL381']);
	}
	
}

//added by akshay on 12/9/17
	function validateReferGrid()
	{
		var is_grid_modified=true;
		var n=getLVWRowCount('cmplx_ReferHistory_cmplx_GR_ReferHistory');
		var activity = activityName;
		if(n>0){
			for(var i=0;i<n;i++){
				var referFrom = getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,2);
				var gridReferVal = getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,5);
				if(activity=='CPV_Analyst'){activity='CPV Checker';}
				else if(activity=='DSA_CSO_Review' && referFrom=='CSM'){activity='CSO (for documents)';}
				else if(activity=='DSA_CSO_Review' && gridReferVal !='DSA_CSO_Review'){activity='Source';}
				
				if(getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,2)!='Disbursal' && gridReferVal==activity && (getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)=='' || getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)=='--Select--')){
					is_grid_modified=false;
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
			if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,9)=='' || getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,9)=='NA')//years in current address
			{
				showAlert('AddressDetails_years','Please enter Years in Current Address');
				return false;
			}
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
	if(getNGValue("cmplx_CardDetails_Security_Check_Held")==true){
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
	if(getNGValue('cmplx_CardDetails_MarketingCode')=="" || getNGValue('cmplx_CardDetails_MarketingCode')=="--Select--"){
		showAlert('cmplx_CardDetails_MarketingCode',alerts_String_Map['PL129']);	
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
		appType_selectedRow='G-'+getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',getNGListIndex('cmplx_GuarantorDetails_cmplx_GuarantorGrid'),7)+' '+getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',getNGListIndex('cmplx_GuarantorDetails_cmplx_GuarantorGrid'),9);
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
			if(getNGValue('cmplx_LoanDetails_paymode')=='C')
			{
				return checkMandatory('cmplx_LoanDetails_chqid#Cheque ID,cmplx_LoanDetails_chqno#CR/Cheque/DD no,cmplx_LoanDetails_chqdat#Cheque Date,cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_reason#Reason,cmplx_LoanDetails_micr#MICR');
			}
			if(getNGValue('cmplx_LoanDetails_paymode')=='T')
			{
				return checkMandatory('cmplx_LoanDetails_drawnon#Drawn On Account,cmplx_LoanDetails_bankdeal#Dealing Bank');
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
	  var ReferTo=getNGValue("DecisionHistory_ReferTo");
	  var DecisionReasonCode=com.newgen.omniforms.formviewer.getNgSelectedValues('DecisionHistory_DecisionReasonCode');
	  var remarks=getNGValue('cmplx_Decision_REMARKS');
	  
        if(Decision==""||Decision=="--Select--" )
		{
			showAlert('cmplx_Decision_Decision',"Please select decision value");
			return false;
		}
		else if(Decision=='Refer' && ReferTo=='')
		{
			showAlert('DecisionHistory_ReferTo',"Please select a value in Refer To");
			return false;
		}
		else if((Decision=='Refer' || Decision=='Reject') && (DecisionReasonCode=='' || DecisionReasonCode=='--Select--'))
		{
			showAlert('DecisionHistory_DecisionReasonCode',"Please select decision reason value");
			return false;
		}
		else if(isVisible('cmplx_Decision_REMARKS')==true && remarks=='')
		{
			showAlert('cmplx_Decision_REMARKS',alerts_String_Map['CC190']);
			return false;
		}
     return true;
}


function Add_Validate()
{
	var flag_add = false;
    var n = getLVWRowCount("Decision_ListView1");
    var activityname=window.parent.stractivityName;
	var count=0;
	if(n>0)
		{
							
			for(var i=0;i<n;i++)
			{
				if(getLVWAT('Decision_ListView1',i,2)==activityname && getLVWAT('Decision_ListView1',i,9)!='Y')
				{
					count=count+1;
				}
				
			}
			
			if(getNGValue("cmplx_Decision_Decision")=='Refer')
			{
				if(activityname!='CAD_Analyst1' && activityname!='CPV'){//on these user can select multiple refer  
					if(count==1){
						showAlert('',alerts_String_Map['CC253']);
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
				if(count==1){
						showAlert('',"Cannot add two decision values!!!");
						return false;
				}
			}
		}
   
  return true;
}

function modify_row()
{
	var activityname=window.parent.stractivityName;
	var selectedRow=com.newgen.omniforms.formviewer.getNGListIndex('Decision_ListView1');

	if(getLVWAT('Decision_ListView1',selectedRow,9)=='Y') 
	{
		showAlert('','Only newly added rows can be modified!!!');
		return false;	}

		return true;
}

function delete_dec_row()
{
	var activityname=window.parent.stractivityName;
	var selectedRow=com.newgen.omniforms.formviewer.getNGListIndex('Decision_ListView1');
	if(selectedRow==-1)
	{
		showAlert('','Please select a row to delete');
		return false;
	}
	else if(getLVWAT('Decision_ListView1',selectedRow,9)=='Y')
	{
		showAlert('','Only newly added rows can be deleted!!!');
		return false;
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