
function change_RLOS(pId)
{
	//added by akshay on 23/4/18
		setNGValueCustom(pId,getNGValue(pId));
		if(getNGValue(pId) != rlosValuesData[pId]){
			var value = document.getElementById(document.getElementById(pId).parentNode.id).parentNode.id;
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(value)==-1){
			hiddenString = value+',';
			//document.getElementById(pId).style.setProperty ("background", "#ffe0bd", "important");
			var previousValue = getNGValue('FrameName');
			hiddenString = previousValue + hiddenString;
			setNGValueCustom('FrameName',hiddenString);
			}
		}//sagarika
		/*
				if(pId=='cmplx_EmploymentDetails_EmpStatusPL')
{
	var activityName=window.parent.stractivityName;
	if( getNGValue(pId)=='CN')
	{
		setEnabled("cmplx_EmploymentDetails_Indus_Macro",true);
	    setEnabled("cmplx_EmploymentDetails_Indus_Micro",true);
	}
}
		//start by sagarika 
		if(pId=='cmplx_EmploymentDetails_EmpStatusPL')
{
	var activityName=window.parent.stractivityName;
	if( getNGValue(pId)=='CN')
	{
		setEnabled("cmplx_EmploymentDetails_Indus_Macro",true);
	    setEnabled("cmplx_EmploymentDetails_Indus_Micro",true);
	}
}//end by sagarika */
		//Added by shivang
		//Modified by shivang to add the PID check for PCASP-2052
		if(pId=='nationality'){
			if(getNGValue('nationality')=='AE'){
				setLockedCustom('SupplementCardDetails_VisaIssueDate',true);
			}else{
					setLockedCustom('SupplementCardDetails_VisaIssueDate',false);
			}
		}
		if(pId=='cmplx_EligibilityAndProductInfo_LPFAmount'){
			var lpf = getNGValue('cmplx_EligibilityAndProductInfo_LPFAmount');
			if(lpf>2500){
			    showAlert('cmplx_EligibilityAndProductInfo_LPFAmount','Loan Processing Fee cannot be greater than 2500')
			}
		}//hritik
		// Added by pooja for decimal 
		if(pId=='cmplx_CardDetails_SelfSupp_Limit'){
		if(getNGValue(pId)!=""){
			var str=getNGValue('cmplx_CardDetails_SelfSupp_Limit');
			if(str.indexOf('.')>-1){
				showAlert('cmplx_CardDetails_SelfSupp_Limit','Decimal Value is not allowed!');
				return false;

		}

	}
	}
		
		if(pId== 'GuarantorDetails_nationality' ){
			if(getNGValue(pId)=='AE'){
				setLockedCustom('visaNo',true);
				setLockedCustom('GuarantorDetails_VisaISsueDate',true);
				setLockedCustom('visaExpiry',true);
				setEnabled('GuarantorDetails_VisaISsueDate',false);
				setEnabled('visaExpiry',false);
			}else if(!(getNGValue(pId)=='AE')){
				setLockedCustom('visaNo',false);
				setLockedCustom('GuarantorDetails_VisaISsueDate',false);
				setLockedCustom('visaExpiry',false);
				setEnabled('GuarantorDetails_VisaISsueDate',true);
				setEnabled('visaExpiry',true);
			}
		}
		else if (pId=='GuarantorDetails_guardianCif'){
			if(getNGValue('GuarantorDetails_guardianCif')=='Yes'){
				setLocked('cif',false);
			} else if(getNGValue('GuarantorDetails_guardianCif')=='Same'){				
				if(getNGValue('cmplx_Customer_guarcif')==''){	
						showAlert('cmplx_Customer_guarcif',alerts_String_Map['PL390']);
						return false;
				}
				setNGValue('cif',getNGValue('cmplx_Customer_guarcif'));
				setLocked('cif',true);
			} else if(getNGValue('GuarantorDetails_guardianCif')=='No'){
				setNGValue('cif','');
				setLocked('cif',false);
			} else {
				setLocked('cif',true);
			}	
		}
		else if(pId=='cmplx_Customer_Nationality'){
			var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);//by shweta
			if(prod=='Personal Loan' && getNGValue("cmplx_Customer_Nationality")!=""){
			var age=getNGValue("cmplx_Customer_age");
			 if(age!="" && age<21 && getNGValue("cmplx_Customer_Nationality")!='AE')
				{
					showAlert('','Expat Minor Customer not Applicable for ' +  prod);
					setNGValueCustom(pId,'');
					return false;
				}
				 else if(age!="" && age<18.06){
						showAlert(pId,alerts_String_Map['PL428']);
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
			if(getNGValue(pId)=='AE')
			{
			setLockedCustom('cmplx_Customer_VisaNo',true);
			setLockedCustom('cmplx_Customer_VisaIssuedate',true);
			setLockedCustom('cmplx_Customer_VIsaExpiry',true);
			setNGValueCustom('cmplx_Customer_ResidentNonResident','Y');
			setLockedCustom('cmplx_Customer_ResidentNonResident',true);
		
			}else{
			setLockedCustom('cmplx_Customer_VisaNo',false);
			setLockedCustom('cmplx_Customer_VisaIssuedate',false);
			setLockedCustom('cmplx_Customer_VIsaExpiry',false);
			setNGValueCustom('cmplx_Customer_ResidentNonResident','Y');
			}
				
		}
		if(pId=='cmplx_Customer_EmiratesID'){
			if(getNGValue(cmplx_Customer_EmiratesID)!=''){
				setNGValueCustom('cmplx_Customer_ResidentNonResident','Y');
			}
		}
		//Modified by shivang to add the PID check end 
		if(pId=='IncomingDocNew_ExpiryDate')
		{
		validatePastDate(pId,"Expiry date");
		
		}
		//console.log(hiddenString);
	//Changes Done by aman for PCSP-67
	if(pId=='contractType')
	{
		return true;
	}
	//Changes Done by aman for PCSP-67
	
	if(pId=='cif')
	{
	
		if(getNGValue('cif') !="" && getNGValue('cif') .length!=7){
			showAlert('cif','Please Enter valid Guardian CIF');
			return false;
		}
		setLockedCustom('ReadFromCIF',false);
	}
	if(pId=='cmplx_EmploymentDetails_RegPay')
	{
	//	console.log("reg");
		return true;
	}
	

if(pId=='cmplx_EmploymentDetails_targetSegCode')
	{
	
	 if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='LIFSUR')
		{
			
		//setEnabled('EMploymentDetails_Combo3',true);
		setVisible('EMploymentDetails_Label10',true);
		setVisible('EMploymentDetails_Label11',true);
		setVisible('EMploymentDetails_Label12',true);
		setVisible('EMploymentDetails_Label13',true);
		setVisible('EMploymentDetails_Label14',true);
		setVisible('EMploymentDetails_Label15',true);
		//setVisible('EMploymentDetails_Label14',true);
		setVisible('cmplx_EmploymentDetails_PremAmt',true);
		setVisible('cmplx_EmploymentDetails_Withinminwait',true);
		setVisible('cmplx_EmploymentDetails_lifeInsurance',true);
		setVisible('cmplx_EmploymentDetails_NoPremPaid',true);
		setVisible('cmplx_EmploymentDetails_RegPay',true);
		setVisible('cmplx_EmploymentDetails_PremiumType',true);
		setEnabledCustom('cmplx_EmploymentDetails_Withinminwait',true);
		setEnabledCustom('cmplx_EmploymentDetails_NoPremPaid',true);
		setEnabledCustom('cmplx_EmploymentDetails_RegPay',true);
		setEnabledCustom('cmplx_EmploymentDetails_PremiumType',true);
		setEnabledCustom('cmplx_EmploymentDetails_lifeInsurance',true);
		setEnabledCustom('cmplx_EmploymentDetails_PremAmt',true);
		setVisible('EMploymentDetails_Label16',false);
		setVisible('cmplx_EmploymentDetails_MotorInsurance',false);
		setVisible('cmplx_EmploymentDetails_Is_group_insur_policy',true);
	setVisible('cmplx_EmploymentDetails_Is_life_insur_lessthan_5',true);
		
		}
	else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='MOTSUR')
		{
			setVisible('EMploymentDetails_Label10',false);
		setVisible('EMploymentDetails_Label11',false);
		setVisible('EMploymentDetails_Label12',false);
		setVisible('EMploymentDetails_Label13',false);
		setVisible('EMploymentDetails_Label14',false);
		setVisible('EMploymentDetails_Label15',false);
		//setVisible('EMploymentDetails_Label14',true);
		
		setVisible('cmplx_EmploymentDetails_PremAmt',false);
		setVisible('cmplx_EmploymentDetails_Withinminwait',false);
		setVisible('cmplx_EmploymentDetails_lifeInsurance',false);
		setVisible('cmplx_EmploymentDetails_NoPremPaid',false);
		setVisible('cmplx_EmploymentDetails_RegPay',false);
		setVisible('cmplx_EmploymentDetails_PremiumType',false);
			setVisible('EMploymentDetails_Label16',true);
		setVisible('cmplx_EmploymentDetails_MotorInsurance',true);
		setEnabledCustom('cmplx_EmploymentDetails_MotorInsurance',true);
		//setVisible('cmplx_EmploymentDetails_Withinminwait',false);
		setVisible('cmplx_EmploymentDetails_Is_group_insur_policy',false);
 setVisible('cmplx_EmploymentDetails_Is_life_insur_lessthan_5',false);
		
	}
	else{
		
			setVisible('EMploymentDetails_Label10',false);
		setVisible('EMploymentDetails_Label11',false);
		setVisible('EMploymentDetails_Label12',false);
		setVisible('EMploymentDetails_Label13',false);
		setVisible('EMploymentDetails_Label14',false);
		setVisible('EMploymentDetails_Label15',false);
		//setVisible('EMploymentDetails_Label14',true);
		
		setVisible('cmplx_EmploymentDetails_PremAmt',false);
		setVisible('cmplx_EmploymentDetails_Withinminwait',false);
		setVisible('cmplx_EmploymentDetails_lifeInsurance',false);
		setVisible('cmplx_EmploymentDetails_NoPremPaid',false);
		setVisible('cmplx_EmploymentDetails_RegPay',false);
		setVisible('cmplx_EmploymentDetails_PremiumType',false);
			setVisible('EMploymentDetails_Label16',false);
		setVisible('cmplx_EmploymentDetails_MotorInsurance',false);
		setVisible('cmplx_EmploymentDetails_Is_group_insur_policy',false);
 setVisible('cmplx_EmploymentDetails_Is_life_insur_lessthan_5',false);
		
	}}
	

		//setEnabled('cmplx_EmploymentDetails_MotorInsurance',true);
		

if(pId=='cmplx_EmploymentDetails_PremiumType')
{
if(getNGValue('cmplx_EmploymentDetails_PremiumType')=='Half yearly' || getNGValue('cmplx_EmploymentDetails_PremiumType')=='Annually')
{
setEnabledCustom('cmplx_EmploymentDetails_Withinminwait',true);
}
else{
	setNGValueCustom('cmplx_EmploymentDetails_Withinminwait',false);
setEnabledCustom('cmplx_EmploymentDetails_Withinminwait',false);	
}
}

	
	
	if(pId=='cmplx_Customer_FIrstNAme' || pId=='cmplx_Customer_MiddleName' || pId=='cmplx_Customer_LAstNAme')
	{
		var short_name = getNGValue('cmplx_Customer_FIrstNAme').substring(0,1)+" "+getNGValue('cmplx_Customer_MiddleName').substring(0,1)+" "+getNGValue('cmplx_Customer_LAstNAme').substring(0,1);
		setNGValueCustom('cmplx_Customer_short_name',short_name);
		//Prateek change 6-12-2017 : enable fetch detail button on value change of these fields				
		if(getNGValue('cmplx_Customer_CIFNO')!="")
			setLockedCustom('FetchDetails',false);
		
	}
	
	if (pId=='state'){
		return true;
	}
	else if(pId=='cmplx_CardDetails_CardEmbossingName' || pId=='cardEmbName' || pId=='cmplx_CardDetails_Self_card_embossing')
	{
		setNGValue(pId,getNGValue(pId).trim());
	}
	
	else if(pId=='cmplx_Customer_NEP'){
			var NEP=getNGValue("cmplx_Customer_NEP");
			if(NEP!=""){
					setEnabledCustom("FetchDetails",true); //Arun(11/12/17) to set enable after selecting data in Customer type
					setEnabledCustom("Customer_Check",true); 
					//setNGValueCustom('cmplx_Customer_NEP','true');
					//setNGValueCustom('NTB','true');
					setEnabledCustom("ReadFromCard",false);
					//setEnabledCustom("FetchDetails",false);
				//	setVisible("Customer_Frame2", true);
					setVisible("Customer_Frame2", false);
					document.getElementById('Customer_Label36').classList.remove('mandatoryLabel');//to remove mandatory star
					setEnabledCustom("Validate_OTP_Btn", false);
					setLockedCustom("OTP_No", true);
					//code change to disable CIF at NEP
					//setLockedCustom("cmplx_Customer_CIFNO",false);
					setLockedCustom("cmplx_Customer_FIrstNAme",false);
					setLockedCustom("cmplx_Customer_MiddleName",false);
					setLockedCustom("cmplx_Customer_LAstNAme",false);
					setLockedCustom("cmplx_Customer_Nationality",false);
					setLockedCustom("cmplx_Customer_MobNo",false);
					setLockedCustom("cmplx_Customer_DOb",false);
					setEnabledCustom("cmplx_Customer_DOb",true);
					setLockedCustom("cmplx_Customer_PAssportNo",false);
					setVisible("Customer_Label56",true);
					setVisible("cmplx_Customer_EIDARegNo",true);
					setLockedCustom("cmplx_Customer_CardNotAvailable",true);
					setLockedCustom("cmplx_Customer_EIDARegNo",false);
					//setNGValueCustom("cmplx_Customer_EmiratesID",""); // Prateek change 6-12-2017 : This line commented as per query raised by rachit
					setLockedCustom("cmplx_Customer_EmiratesID",false);
					setLockedCustom("Customer_Button1",true);
					setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
					setLockedCustom("cmplx_Customer_IdIssueDate",false);
					//setLockedCustom("cmplx_Customer_Designation",false);
					setLockedCustom("cmplx_Customer_Basic_Sal",false);
					setLockedCustom("cmplx_Customer_Email_ID",false);
					setLockedCustom("cmplx_Customer_Title",false);
					setLockedCustom("cmplx_Customer_gender",false);
					setLockedCustom("Designation_button1",false);
					//setNGValueCustom("cmplx_Customer_NTB",true);  
					//SetEnableCustomer();
					//setBlank_Customer();
					if(NEP=="NEWC"){
						setLockedCustom("cmplx_Customer_EmiratesID",true);
						setLockedCustom("cmplx_Customer_EmirateIDExpiry",true);
						setLockedCustom("cmplx_Customer_IdIssueDate",true);
					}
					/*else{
						setLockedCustom("cmplx_Customer_EmiratesID",false);
						setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
						setLockedCustom("cmplx_Customer_IdIssueDate",false);
					}*/	
					setEnabledCustom("cmplx_Customer_card_id_available", false);
					setLockedCustom("EMploymentDetails_Label25",false);//Added by Akshay on 9/9/17 for enabling NEP type as per FSD
					setLockedCustom("cmplx_EmploymentDetails_NepType",false);
					setLockedCustom("NepType",false);
					if(com.newgen.omniforms.formviewer.isVisible('CompanyDetails_Frame1')){
						com.newgen.omniforms.formviewer.setVisible('CompanyDetails_Label15',true);
						com.newgen.omniforms.formviewer.setVisible('NepType',true);
					}
					//nikhil 28/11 to d customer if nep = '...'
					setEnabledCustom("cmplx_Customer_CardNotAvailable", false);
	
				}
				else{
				
					setEnabledCustom("FetchDetails",false); //Arun(11/12/17) to set enable after selecting data in Customer type
					//setEnabledCustom("Customer_Check",false); 
					//setNGValueCustom('cmplx_Customer_NEP','false');
					//setNGValueCustom('NTB','false');
					setEnabledCustom("ReadFromCard",true);
					//setEnabledCustom("FetchDetails",true);
					//setVisible("Customer_Frame2", false);
					setVisible("Customer_Frame2", true);
					document.getElementById('Customer_Label36').classList.add('mandatoryLabel');//to remove mandatory star
					setLockedCustom("cmplx_Customer_CIFNO",true);
					setLockedCustom("cmplx_Customer_FIrstNAme",true);
					setLockedCustom("cmplx_Customer_MiddleName",true);
					setLockedCustom("cmplx_Customer_LAstNAme",true);
					setLockedCustom("cmplx_Customer_Nationality",true);
					setLockedCustom("cmplx_Customer_MobNo",true);
					setLockedCustom("cmplx_Customer_DOb",true);
					setEnabledCustom("cmplx_Customer_DOb",false);
					setLockedCustom("cmplx_Customer_PAssportNo",true);
					/*setLockedCustom("Designation_button1",true);
					setLockedCustom("cmplx_Customer_Basic_Sal",true);
					setLockedCustom("cmplx_Customer_Email_ID",true);*/
					setVisible("Customer_Label56",false);
					setVisible("cmplx_Customer_EIDARegNo",false);
					//setLockedCustom("cmplx_Customer_CardNotAvailable",false); 	temp commented by Aman for Sprint 1
					setLockedCustom("cmplx_Customer_EIDARegNo",true);
					SetDisableCustomer();
					setBlank_Customer();
					//setNGValueCustom("cmplx_Customer_NTB",false);
					setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
					setLockedCustom("cmplx_Customer_IdIssueDate",false);
					setEnabledCustom("cmplx_Customer_card_id_available", false);
					//setEnabledCustom("cmplx_Customer_CardNotAvailable",true);    temp commented by Aman for Sprint 1
					setLockedCustom("EMploymentDetails_Label25",true);//Added by Akshay on 9/9/17 for disabling NEP type as per FSD
					setLockedCustom("cmplx_EmploymentDetails_NepType",true);
					setLockedCustom("NepType",true);
					if(isLocked('cmplx_Customer_EmiratesID')){
					setLockedCustom("cmplx_Customer_EmiratesID",false);
					setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
					setLockedCustom("cmplx_Customer_IdIssueDate",false);
					}
					if(com.newgen.omniforms.formviewer.isVisible('CompanyDetails_Frame1')){
					
					com.newgen.omniforms.formviewer.setVisible('CompanyDetails_Label15',false);
						com.newgen.omniforms.formviewer.setVisible('NepType',false);
					}
					//nikhil 28/11 to enable customer if nep = ''
					setEnabledCustom("cmplx_Customer_CardNotAvailable", true);
					setLockedCustom("cmplx_Customer_CardNotAvailable",false);
					
					}
					
		}
		
	else if(pId=='limitEMI'){
		var value = getNGValue('limitEMI');
		if(value){
			if(value.indexOf('.')>-1){
			var valArr = value.split('.');
			var beforeDecimal = valArr[0];
			var afterDecimal = valArr[1];
				if(beforeDecimal.length >18 || afterDecimal.length>2){
					showAlert('limitEMI',alerts_String_Map['VAL069']);
					setNGValueCustom('limitEMI',"");
					return false;
				}
			}
			else{
				if(value.length > 18){
				showAlert('limitEMI',alerts_String_Map['VAL069']);
				setNGValueCustom('limitEMI',"");
					return false;
				}
			}
		}
	}
	//Prateek change 6-12-2017 : enable fetch detail button on value change of these fields
	/* else if(pId=='cmplx_Customer_FIrstNAme' || pId=='cmplx_Customer_MiddleName' || pId=='cmplx_Customer_LAstNAme'  || pId=='cmplx_Customer_Nationality' || pId=='cmplx_Customer_DOb' || pId=='cmplx_Customer_PAssportNo')*/
	 else if(pId=='cmplx_Customer_Nationality' || pId=='cmplx_Customer_DOb' || pId=='cmplx_Customer_PAssportNo')
	 {
		if(getNGValue('cmplx_Customer_CIFNO')!="")
			setLockedCustom('FetchDetails',false);
	}
	else if(pId=='OECD_tinNo'){
		var tin = getNGValue('OECD_tinNo');
		if(tin){
		setEnabledCustom('OECD_noTinReason',false);
		}
		else{
		setEnabledCustom('OECD_noTinReason',true);
		}
	}
	//added by nikhil 25/11/18 Manish Points
	else if(pId=='OECD_noTinReason'){
		var tin_reas = getNGValue('OECD_noTinReason');
		if(tin_reas=='' || tin_reas=='--Select--'){
		setEnabledCustom('OECD_tinNo',true);
		}
		else{
		setEnabledCustom('OECD_tinNo',false);
		}
	}
	
	 if(pId=='cmplx_Customer_Nationality')
	{
		//Deepak Changes done for PCAS-2799
		if(getNGValue(pId)!='--Select--')
		{
			/*var age=getNGValue("cmplx_Customer_age");
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
			}*/
		}
		
		var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);//by shweta
		if(prod=='Personal Loan' ){
			var age=getNGValue("cmplx_Customer_age");
		 if(age !="" && age<21 && getNGValue("cmplx_Customer_Nationality")!='AE')
			{
				showAlert('','Expat Minor Customer not Applicable for ' +  prod);
				setNGValueCustom(pId,'');
				return false;
			}
			 else if(age !="" && age<18.06){
					showAlert(pId,alerts_String_Map['PL428']);
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
		//done for refreshing incoming document. By Saurabh on 4th Jan
		return true;
	}
	
	else if (pId=='cmplx_EmploymentDetails_EmpStatus'){
		return true;
	}
			
	
	//modified by akshay on 14/10/17
	if(pId=='cmplx_Customer_DOb')
	{
	 if(getNGValue(pId)!='')
		{
		if(validateFutureDate(pId)){
			var age=calcAge(getNGValue(pId),'');
			if( document.getElementById('ELigibiltyAndProductInfo_IFrame2')!=null)
				document.getElementById('ELigibiltyAndProductInfo_IFrame2').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp';
			if(age>65)
			{
				showAlert('',"Credit shield is not applicable!");
			}
			var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);//by shweta
			if(prod=='Personal Loan' && getNGValue("cmplx_Customer_Nationality")!=""){
			 if(age<21 && getNGValue("cmplx_Customer_Nationality")!='AE')
				{
					showAlert('','Expat Minor Customer not Applicable for ' +  prod);
					setNGValueCustom(pId,'');
					return false;
				}
			 else if(age<18.6){
					showAlert(pId,alerts_String_Map['PL428']);
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
			 } else {
					YYMM('','cmplx_Customer_age',age);
			 }
			}
		}
	}	
	if(pId=='cmplx_EligibilityAndProductInfo_LPF'){
		if(getNGValue(pId)!=''){
			if(parseFloat(getNGValue(pId))>100 || parseFloat(getNGValue(pId))<0){
				showAlert(pId,"Please Enter a valid percentage!"); 
				setNGValue(pId,'');
				return false;
			}
		}
	}
//sagarika for air arabia
if(pId=='AlternateContactDetails_AirArabiaIdentifier'){
		validateMail1(pId);
		var arabia=getNGValue('AlternateContactDetails_AirArabiaIdentifier');
     if(!(arabia.length>15 && arabia.length<30))
	 {
		showAlert(pId,alerts_String_Map['VAL387']); 
		setNGValue(pId,'');
		return false;
	 }
		
	}
if(pId=='KYC_Combo1'){
	    if(getNGValue('KYC_Combo1')=='Y')
	    {
			setLocked('KYC_CustomerType',false);
			setLocked('KYC_Combo2',false);
			setLocked('KYC_DatePicker1',false);
	    }
	    else{
           // setLocked('KYC_CustomerType',true);
			//setLocked('KYC_Combo2',true);
			setLocked('KYC_DatePicker1',true);
	    }
	}  //hritik 3721 
			
//added by saurabh1 for enroll
if(pId=='AlternateContactDetails_EnrollRewardsIdentifier'){
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
//saurabh1 end for enroll	
	//Changed by Aman for issue 0504
	else if(pId=='dob')
	{
		if(validateFutureDate(pId)){
			var age=calcAge(getNGValue(pId),'');
			//setNGValueCustom('cmplx_Customer_age',age);
			YYMM('','age_gua',age);
		}
	}
	//Changed by Aman for issue 0504
	else if(pId=='cmplx_EligibilityAndProductInfo_EFCHidden')
	{
		if(getNGValue(pId)<3000)
		{
		showAlert(pId,'Additional Limit cannot be less than 3000');
		return false;
		}
	}
	else if(pId=='cmplx_Customer_VIsaExpiry'){
		validatePastDate(pId,"Visa Expiry Date");
		if(isFieldFilled('cmplx_Customer_VisaNo')==false){
			showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL351']);
			setNGValueCustom(pId,'');
			return false;
		}
		//commented by nikhil for PCSP-658
		/*
		if(getNGValue('cmplx_Customer_NTB')==true)

		{
			var VisaExpiry=getNGValue('cmplx_Customer_VIsaExpiry');
			var years_Visaexpiry=VisaExpiry.substring(6,10);
			var years_VisaIssue=years_Visaexpiry-3;
			var VisaIssue=VisaExpiry.replace(years_Visaexpiry,years_VisaIssue);
			setNGValueCustom('cmplx_Customer_VisaIssuedate',VisaIssue);
		}*/	

	}
	
	else if(pId=='cmplx_Customer_IdIssueDate'){
		validateFutureDateexcCurrent('cmplx_Customer_IdIssueDate');//sagarika
		if(isFieldFilled('cmplx_Customer_EmiratesID')==false){
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL353']);
			setNGValueCustom(pId,'');
			return false;
		}
		//added By Akshay
		if(getNGValue('cmplx_Customer_CardNotAvailable')==true && getNGValue('cmplx_Customer_Nationality')!='AE')
		{
			setNGValueCustom('cmplx_Customer_VisaIssuedate',getNGValue('cmplx_Customer_IdIssueDate'));
		}	
		//ended by Akshay		
	}
	
	else if(pId=='cmplx_Customer_EmirateIDExpiry')
	{
		//below condition added by nikhil for PCSP-657
		if(getNGValue('cmplx_Customer_NEP')!='EXEID' && getNGValue('cmplx_Customer_NEP')!='RWEID')
		{
		validatePastDate(pId,"Emirate ID Expiry");
		}
		//Prateek change 6-12-2017 : EMID expiry date can't be future date if EMID is expired
		validateFutureDate(pId);
		//changed by nikhil for PCSP-367
		if(isFieldFilled('cmplx_Customer_EmiratesID')==false && getNGValue('cmplx_Customer_NEP')==''){
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL353']);
			setNGValueCustom(pId,'');
			return false;
		}
	}
	else if(pId=='LastTemporaryLimit')
		validateFutureDate(pId,"LastTemporaryLimit");
	
	else if(pId=='cmplx_Customer_PassPortExpiry'){
		validatePastDate(pId,"Passport Expiry");
		if(isFieldFilled('cmplx_Customer_PAssportNo')==false){
			showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL352']);
			setNGValueCustom(pId,'');
			return false;
		}
	}	
	
	else if(pId=='cmplx_Customer_PassportIssueDate'){
		validateFutureDateexcCurrent(pId); //By Prabhakar  jira2620
		if(isFieldFilled('cmplx_Customer_PAssportNo')==false){
			showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL352']);
			setNGValueCustom(pId,'');
			return false;
		}
	}	
	
	else if(pId=='cmplx_Customer_VisaIssuedate'){
		validateFutureDateexcCurrent(pId);	
		if(isFieldFilled('cmplx_Customer_VisaNo')==false){
			showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL351']);
			setNGValueCustom(pId,'');
			return false;
		}
	}

	else if(pId=='GuarantorDetails_PassportIssueDate' || pId=='GuarantorDetails_VisaISsueDate'){
		validateFutureDate(pId,"");		
	}
	
		
//added by akshay on 27/12/17
	else if(pId=='cmplx_Customer_yearsInUAE'){
		YearsinUAE(pId);
	}
	
	else if(pId=='cmplx_Customer_yearsInUAE' || pId=='cmplx_IncomeDetails_DurationOfBanking' || pId=='Liability_New_mob' || pId=='EffectiveLOB' || pId=='years'|| pId=='cmplx_EmploymentDetails_LengthOfBusiness' ){
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
	}	
	//commented by saurabh on 1st Dec for Tanshu points.
	/*else if(pId=='cmplx_EmploymentDetails_LOSPrevious'){
		LOSPrevious(pId);
	}*/
	else if( pId=='cmplx_EmploymentDetails_LOSPrevious' ){
		//YearsinUAE(pId);
		if(getNGValue(pId)==''){
			setNGValueCustom(pId,'00.00');
		}
		YYMM(pId,'','');
		//below validation added by aman for drop 5
		var YY=getNGValue(pId).split('.')[0];
		var MM=getNGValue(pId).split('.')[1];
		
		if(YY.length>2 || parseInt(MM)>11)
		{
			showAlert(pId,alerts_String_Map['VAL256']);
			setNGValueCustom(pId,'00.00');
		}
	}	
	
	else if(pId=='ReferenceDetails_ref_mobile')
	{
		var mob_No = getNGValue('ReferenceDetails_ref_mobile');
		/*if(mob_No == ""){
		
		}*/
	   if (mob_No.substring(0, 5) != '00971' && mob_No.length < 10)
        {
            mob_No = "00971" + mob_No;
            setNGValueCustom('ReferenceDetails_ref_mobile', mob_No);
        }
        if (mob_No.length != 14)
        {
            showAlert('ReferenceDetails_ref_mobile', alerts_String_Map['VAL087']);
			setNGValueCustom('ReferenceDetails_ref_mobile',"");
            return false;
        }
		if (mob_No.substring(0, 5) != '00971')
        {
            showAlert('ReferenceDetails_ref_mobile', alerts_String_Map['VAL088']);
			setNGValueCustom('ReferenceDetails_ref_mobile',"");
            return false;
        }
	}
	else if(pId=='ReqProd')
	{
		var ReqProd=getNGValue('ReqProd');
		setFieldsVisible(ReqProd);
		return true;
	}
	
	else if(pId=='Product_type')
	{
		//var ReqProd=getNGValue('ReqProd');
		//setFieldsVisible(ReqProd);
		return true;
	}
		
	else if(pId=='LimitExpiryDate'){
		validatePastDate(pId,"Limit Expiry");
       }
	else if(pId=='idIssueDate'){
	validateFutureDate('idIssueDate');
	}
	
	else if(pId=='subProd')
	{
        var subProd = getNGValue("subProd");
		//code by saurabh on 16th Nov.
		if(getNGValue('cmplx_Customer_NTB')==true && (subProd=='PA' || subProd=='PULI' || subProd=='BPA' || subProd=='PU' || subProd=='LI')){
			showAlert('subProd', 'This is not a valid Sub Product as the customer is new to Bank');
			setNGValueCustom('subProd',"");
            return false;
		}
        if (subProd == 'BTC' || subProd == 'SE')
        {
            //com.newgen.omniforms.formviewer.addComboItem('EmpType','Self Employed','Self Employed');
			//setNGValueCustom('EmpType','Self Employed');
            //setLockedCustom('EmpType',true);
           // setVisible("LimitAcc", false);
           // setNGValueCustom('LimitAcc', 'NA');
           // setVisible("LimitExpiryDate", false);
          //  setVisible("typeReq", false);
            setVisible("Product_Label15", false);
            setVisible("Product_Label16", false);
            setVisible("Product_Label17", false);
            setVisible("Product_Label18", false);
            setVisible("Product_Label22", false);
            setVisible("Product_Label23", false);
            setVisible("Product_Label24", false);
            setVisible('Product_Label6', true);
            setVisible('CardProd', true);
			setVisible("Product_Label5", false);
			setVisible("ReqTenor", false);//added By Akshay on 9/9/2017 for removing tenor when product is not CC-IM
			setNGValueCustom('ReqTenor','');
			setVisible("Product_Label8", false);
			setVisible("FDAmount", false);
			setVisible("Product_Label10", false);
			setVisible("NoOfMonths", false);
			//setVisible("Limit_Expdate", false);
			//setVisible("Product_Label16", false);
			//setVisible("LimitAcc", false);
			setVisible("Product_Label7", false);
			setVisible("LastPermanentLimit", false);
			setVisible("Product_Label9", false);
			setVisible("LastTemporaryLimit", false);
				setVisible("Limit_Expiry_Date", false);
			setVisible("Limit_Expiry_Date_Lable", false);
		
			//setVisible("Product_Label11", false);
			//setVisible("ExistingTempLimit", false);
        }

//modified by akshay on 3/1/17      
	  else if (subProd == 'IM')
        {
           
			setVisible('Product_Label6', true);
            setVisible('CardProd',true);
			
			setVisible("Product_Label5", true);
			setVisible("ReqTenor", true);
            
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
			setVisible("NoOfMonths", false);
			setVisible("Product_Label16", false);
				setVisible("Limit_Expiry_Date", false);
			setVisible("Limit_Expiry_Date_Lable", false);
		
			setVisible("Product_Label7", false);
			setVisible("LastPermanentLimit", false);
			setVisible("Product_Label9", false);
			setVisible("LastTemporaryLimit", false);
			//setVisible("Product_Label11", false);
			setLeft('ReqTenor','822px');
			setLeft('Product_Label5','822px');	
        }
		
        else if (subProd == 'SAL')
        {
            //setNGValueCustom('EmpType','Salaried');
            //setLockedCustom('EmpType',true);
            setVisible('Product_Label6', true);
			setVisible('CardProd', true);
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
			setVisible("Limit_Expiry_Date", false);
			setVisible("Limit_Expiry_Date_Lable", false);
		
			
        }
		
		
        else if (subProd == 'LI')
        {
            setVisible("LimitAcc", true);
			//setVisible('Product_Label6', true);
			//setVisible('CardProd', true);
			//setLockedCustom('CardProd', true); //Locked by aman for LI case
            //setVisible("Product_DatePicker4", true);
			//setVisible("Product_Label10", true);
           // setVisible("typeReq", true);
		  	
			setVisible("Product_Label6", false); //Arun (06/12/17) to hide this field
			setVisible("CardProd", false); //Arun (06/12/17) to hide this field
			
            setVisible("Product_Label15", true);
			setVisible("Product_Label16", true);
			//change by saurabh on 3rd Dec
			
            setVisible("Product_Label17", true);
            //setVisible("Product_Label18", true);
            setVisible("Product_Label22", true);
            setVisible("Product_Label23", true);
            setVisible("Product_Label24", true);
			
			setVisible("Product_Label10", true);
			setVisible("NoOfMonths", true);
            //setVisible("Product_Label11", true); commented by saurabh on 29th Nov for DROP2 CR.
            setVisible("Product_Label9", true);
			setVisible("Product_Label7", true);
			setVisible("LastPermanentLimit", true);
            setVisible("LastTemporaryLimit", true);
            //setVisible("ExistingTempLimit", true); commented by saurabh on 29th Nov for DROP2 CR.
			setVisible("Limit_Expdate", true);
			setVisible("Product_Label5", false);
			setVisible("ReqTenor", false);
			setNGValueCustom('ReqTenor','');
			setVisible("Product_Label8", false);
			 setVisible("typeReq", false);
			setVisible("FDAmount", false);
            //setLockedCustom('EmpType',false);
			setVisible("Product_Label15", false);
			setVisible("Limit_Expiry_Date", true);
			setVisible("Limit_Expiry_Date_Lable", true);
			
			com.newgen.omniforms.formviewer.setTop("Product_Label7","70px");
			com.newgen.omniforms.formviewer.setTop("LastPermanentLimit","85px");
			com.newgen.omniforms.formviewer.setTop("Product_Label9","70px");
			com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","85px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label7", "824px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit", "824px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label9", "555px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit", "555px"); 
			com.newgen.omniforms.formviewer.setLeft("Product_Label10", "1078px");
			com.newgen.omniforms.formviewer.setLeft("NoOfMonths", "1078px");
			com.newgen.omniforms.formviewer.setTop("Product_Label10","70px");
			com.newgen.omniforms.formviewer.setTop("NoOfMonths","86px");

        }
		
		        else if (subProd == 'PULI')
        {
            setVisible("LimitAcc", true);
			//setVisible('Product_Label6', true);
			//setVisible('CardProd', true);
			//setLockedCustom('CardProd', true); //Locked by aman for LI case
            //setVisible("Product_DatePicker4", true);
			//setVisible("Product_Label10", true);
           // setVisible("typeReq", true);
            setVisible("Product_Label15", true);
			setVisible("Product_Label16", true);
			//change by saurabh on 3rd Dec
            setVisible("Product_Label17", true);
            //setVisible("Product_Label18", true);
            setVisible("Product_Label22", true);
            setVisible("Product_Label23", true);
            setVisible("Product_Label24", true);
			setVisible("Product_Label10", true);
			setVisible("NoOfMonths", true);
            //setVisible("Product_Label11", true); commented by saurabh on 29th Nov for DROP2 CR.
            setVisible("Product_Label9", true);
			setVisible("Product_Label7", true);
			setVisible("LastPermanentLimit", true);
            setVisible("LastTemporaryLimit", true);
            //setVisible("ExistingTempLimit", true); commented by saurabh on 29th Nov for DROP2 CR.
			//setVisible("Limit_Expdate", true);
			setVisible("Product_Label5", false);
			setVisible("ReqTenor", false);
			setNGValueCustom('ReqTenor','');
			setVisible("Product_Label8", false);
			 setVisible("typeReq", false);
			setVisible("FDAmount", false);
            //setLockedCustom('EmpType',false);
			setVisible("Product_Label15", false);
			 setVisible("LastTemporaryLimit", true);
             setVisible("Product_Label9", true);
			setVisible("Limit_Expiry_Date", true);
			setVisible("Limit_Expiry_Date_Lable", true);
		
			//com.newgen.omniforms.formviewer.setTop("Product_Label11","122px");
			//com.newgen.omniforms.formviewer.setTop("ExistingTempLimit","138px");
			//com.newgen.omniforms.formviewer.setLeft("Product_Label16","555px");
			//com.newgen.omniforms.formviewer.setLeft("LimitAcc","555px");
			
			
			com.newgen.omniforms.formviewer.setTop("Product_Label7","70px");
			com.newgen.omniforms.formviewer.setTop("LastPermanentLimit","85px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label7", "1078px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit", "1078px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label9", "824px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit", "824px"); 
			com.newgen.omniforms.formviewer.setLeft("Product_Label10", "24px");
			com.newgen.omniforms.formviewer.setLeft("NoOfMonths", "24px");
			com.newgen.omniforms.formviewer.setTop("Product_Label10","122px");
			com.newgen.omniforms.formviewer.setTop("NoOfMonths","138px");
			com.newgen.omniforms.formviewer.setTop("Product_Label9","70px");
			com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","85px");

        }
		
        else
        {
			if(subProd != 'EXP' && subProd != 'NAT'){
            setVisible("Product_Label5", false);
			setVisible("ReqTenor", false);
			setNGValueCustom('ReqTenor','');
			}
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
			setVisible("NoOfMonths", false);
			setVisible("Product_Label11", false);
            setVisible("Product_Label9", false);
			setVisible("Product_Label7", false);
			setVisible("LastPermanentLimit", false);
            setVisible("LastTemporaryLimit", false);
            setVisible("ExistingTempLimit", false);
			setVisible("Limit_Expdate", false);
			setVisible("Product_Label8", false);
			setVisible("FDAmount", false);	
				setVisible("Limit_Expiry_Date", false);
			setVisible("Limit_Expiry_Date_Lable", false);
		
			if(subProd == 'SEC')
			{
				setVisible('Product_Label6', true);
				setVisible('CardProd', true);	
				setVisible("Product_Label8", true);
				com.newgen.omniforms.formviewer.setLeft("Product_Label8", "555px");
				setVisible("FDAmount", true);
				com.newgen.omniforms.formviewer.setLeft("FDAmount", "555px");		
			setVisible("Limit_Expiry_Date", false);
			setVisible("Limit_Expiry_Date_Lable", false);
						
			}
			else
			{
				setVisible("Product_Label8", false);
				setVisible("FDAmount", false);	
				setVisible("Limit_Expiry_Date", false);
			setVisible("Limit_Expiry_Date_Lable", false);
			}
			//setLockedCustom('EmpType',false);
        }
		
		
        return true;
    }
	
	else if(pId=='AppType')
	{
        var appType = getNGValue("AppType");
        if ( getNGValue('subProd')=='LI' && appType == 'T')
        {
            //setVisible("LimitExpiryDate", true);
            //setVisible("Product_Label18", true);
			//setVisible("Limit_Expdate", true);
            setVisible("Product_Label10", true);
			setVisible("NoOfMonths", true);
				setVisible("Limit_Expiry_Date", true);
			setVisible("Limit_Expiry_Date_Lable", true);
		
			
			//com.newgen.omniforms.formviewer.setLeft("Product_Label11","296px");
			//com.newgen.omniforms.formviewer.setLeft("ExistingTempLimit","296px");
			//com.newgen.omniforms.formviewer.setTop("Product_Label9","122px");
			//com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","138px");
			//com.newgen.omniforms.formviewer.setLeft("Product_Label16","555px");
			com.newgen.omniforms.formviewer.setTop("Product_Label7","70px");
			com.newgen.omniforms.formviewer.setTop("LastPermanentLimit","85px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label7", "824px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit", "824px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label9", "555px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit", "555px"); 
			com.newgen.omniforms.formviewer.setLeft("Product_Label10", "1078px");
			com.newgen.omniforms.formviewer.setLeft("NoOfMonths", "1078px");
			com.newgen.omniforms.formviewer.setTop("Product_Label10","70px");
			com.newgen.omniforms.formviewer.setTop("NoOfMonths","86px");
			com.newgen.omniforms.formviewer.setTop("Product_Label9","70px");
			com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","85px");
		
		}
        else if(getNGValue('subProd')=='LI' && appType == 'P')
        {
            setVisible("Product_Label10", false);
			setVisible("NoOfMonths", false);
				setVisible("Limit_Expiry_Date", false);
			setVisible("Limit_Expiry_Date_Lable", false);
		
			com.newgen.omniforms.formviewer.setLeft("Product_Label7", "824px");
			com.newgen.omniforms.formviewer.setLeft("LastPermanentLimit", "824px");
			com.newgen.omniforms.formviewer.setLeft("Product_Label9", "555px");
			com.newgen.omniforms.formviewer.setLeft("LastTemporaryLimit", "555px");  //Arun (22/10/17)
			com.newgen.omniforms.formviewer.setTop("Product_Label9","70px");
			com.newgen.omniforms.formviewer.setTop("LastTemporaryLimit","86px"); //Arun (22/10/17)
		}
	if(getNGValue('subProd')=='BTC' && appType == 'SMES')
	{
		setVisible("Product_Label8", true);
			setVisible("FDAmount", true);
	}
	else if (getNGValue('subProd')=='BTC' && appType != 'SMES')
	{
		setVisible("Product_Label8", false);
			setVisible("FDAmount", false);
	}
    
		return true;	
	}
	
	else if(pId=='EmpType')
	{
		//Added by shivang 
		if(getNGValue("AppType")=='PLBUN' && getNGValue(pId)=='Self Employed'){
			showAlert(pId,"Self Employed can't be applicable for PL Bundle Application type!");
			setNGValue(pId,'--Select--');
			return false;
		}
		return true;
	}	
		//changed on 21st of dec 2017
	/*else if (pId=='NotepadDetails_notedesc'){
		setNGValueCustom('NotepadDetails_notedetails','');
	}--commented on 9/1/18 by akshay as it is duplicate event*/
	//changed on 21st of dec 2017
	
	else if(pId=='ReqLimit'){
		putComma(pId);
	}	
	else if(pId=='Scheme'){
		return true;
	}	
	else if(pId=='ReqTenor'){
	/*	if(parseInt(getNGValue(pId))<=parseInt(getNGValue('minTenor')) || parseInt(getNGValue(pId))>parseInt(getNGValue('maxTenor'))){
				showAlert(pId,"Tenor should be between "+getNGValue('minTenor')+" and "+getNGValue('maxTenor'));
				return false;
		}	*/ 
		putComma(pId);
	}
	else if(pId=='passExpiry'){
		validatePastDate(pId,"Passport Expiry");
	}
	else if(pId=='ApplicationCategory'){
		return true;
	}
	//	CHANGE Started BY AKSHAY on 4/10/17 for changing total Salary calculation

	else if(pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other' || pId=='cmplx_IncomeDetails_RentalIncome' || pId=='cmplx_IncomeDetails_EducationalAllowance'||pId=='cmplx_IncomeDetails_Pension'){
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
		setNGValueCustom("cmplx_IncomeDetails_totSal",calcTotalSal());
		putComma('cmplx_IncomeDetails_totSal');
		}
	}
	else if(pId=='cmplx_Customer_Basic_Sal')
	{
		putComma(pId);	
		setNGValueCustom('cmplx_IncomeDetails_Basic',getNGValue(pId));
		var grossSal=calcGrossSal();
		setNGValueCustom("cmplx_IncomeDetails_grossSal",(grossSal>0)?grossSal:"");
		putComma('cmplx_IncomeDetails_grossSal');
		setNGValueCustom("cmplx_IncomeDetails_totSal",calcTotalSal());
		putComma('cmplx_IncomeDetails_totSal');
	}
	else if(pId=='cmplx_Customer_Email_ID')
	{
		setNGValueCustom('AlternateContactDetails_Email1',getNGValue(pId));
		setNGValueCustom('AlternateContactDetails_Email2',getNGValue(pId));
	}

	//Tanshu Aggarwal(19/07/2017) for proc 865
	else if(pId=='cmplx_IncomeDetails_SalaryDay'){
		var date = parseInt(getNGValue(pId));
		if(date<1 || date>31 && getNGValue(pId)!=""){
			showAlert('cmplx_IncomeDetails_SalaryDay',alerts_String_Map['VAL102']);
			setNGValueCustom(pId,'');
			}
	}
	//(19/07/2017) for proc 865
	
	else if(pId=='cmplx_IncomeDetails_StatementCycle'){
		if((getNGValue(pId)<1 || getNGValue(pId)>31 )&& getNGValue(pId)!="")
			showAlert('cmplx_IncomeDetails_StatementCycle',alerts_String_Map['VAL102']);
	}
	
	else if(pId=='cmplx_IncomeDetails_Overtime_Month1' || pId=='cmplx_IncomeDetails_Overtime_Month2' || pId=='cmplx_IncomeDetails_Overtime_Month3'|| pId=='cmplx_IncomeDetails_Commission_Month1' || pId=='cmplx_IncomeDetails_Commission_Month2' || pId=='cmplx_IncomeDetails_Commission_Month3' || pId=='cmplx_IncomeDetails_FoodAllow_Month1' || pId=='cmplx_IncomeDetails_FoodAllow_Month2' || pId=='cmplx_IncomeDetails_FoodAllow_Month3' || pId=='cmplx_IncomeDetails_PhoneAllow_Month1' || pId=='cmplx_IncomeDetails_PhoneAllow_Month2' || pId=='cmplx_IncomeDetails_PhoneAllow_Month3' || pId=='cmplx_IncomeDetails_serviceAllow_Month1' || pId=='cmplx_IncomeDetails_serviceAllow_Month2' || pId=='cmplx_IncomeDetails_serviceAllow_Month3' || pId=='cmplx_IncomeDetails_Bonus_Month1'|| pId=='cmplx_IncomeDetails_Bonus_Month2' || pId=='cmplx_IncomeDetails_Bonus_Month3' || pId=='cmplx_IncomeDetails_Other_Month1' || pId=='cmplx_IncomeDetails_Other_Month2' || pId=='cmplx_IncomeDetails_Other_Month3' || pId=='cmplx_IncomeDetails_Flying_Month1' || pId=='cmplx_IncomeDetails_Flying_Month2' || pId=='cmplx_IncomeDetails_Flying_Month3'){
		
		//setNGValueCustom(pId,setDecimalto2digits(pId));
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
	}
	
	else if(pId=='cmplx_IncomeDetails_netSal1' || pId=='cmplx_IncomeDetails_netSal2' || pId=='cmplx_IncomeDetails_netSal3'){
		//setNGValueCustom(pId,setDecimalto2digits(pId));
		putComma(pId);	
		setNGValueCustom("cmplx_IncomeDetails_AvgNetSal",calcAvgNet());
		putComma('cmplx_IncomeDetails_AvgNetSal');	
	}
	//added By Tarang as per drop 4 point 1 started on 26/02/2018
	else if(pId=='AlternateContactDetails_CardDisp')
	{
		if(getNGValue('AlternateContactDetails_CardDisp')=='998')
		{
			 setNGValueCustom('Card_Dispatch_Option','Courier');
		}
		else if(getNGSelectedItemText('AlternateContactDetails_CardDisp')=='Holding WI- HOLD')
		{
			setNGValueCustom('Card_Dispatch_Option','Holding WI- HOLD');
		}
		else if(getNGSelectedItemText('AlternateContactDetails_CardDisp')=='Card centre collection')
		{
			setNGValueCustom('Card_Dispatch_Option','Card centre collection');
		}
		else if(getNGValue('AlternateContactDetails_CardDisp')=='009')
		{
			setNGValueCustom('Card_Dispatch_Option','International dispatch-INTL');
		}
		else
		{
			setNGValueCustom('Card_Dispatch_Option','Branch');
		}
	}
	//added By Tarang as per drop 4 point 1 ended on 26/02/2018
	 if (pId=='cmplx_EligibilityAndProductInfo_InterestRate')
	{
		var Intrate= getNGValue('cmplx_EligibilityAndProductInfo_InterestRate');
		if(parseFloat(Intrate)>100){
			showAlert(pId,'Interest Rate should be less than 100');
			return false;
		}
		var BaseRate= getNGValue('cmplx_EligibilityAndProductInfo_BAseRate'); 
		var ProdPrefRate= getNGValue('cmplx_EligibilityAndProductInfo_ProdPrefRate');
		setNGValueCustom('cmplx_EligibilityAndProductInfo_MArginRate',parseFloat(Intrate)-parseFloat(BaseRate)-parseFloat(ProdPrefRate));
		setNGValueCustom('cmplx_EligibilityAndProductInfo_MArginRate',setDecimalto3digits('cmplx_EligibilityAndProductInfo_MArginRate'));
		//changes by saurabh for saving data when set from JS in disabled field.
		setEnabledCustom('cmplx_EligibilityAndProductInfo_FinalInterestRate',true);
		setNGValueCustom('cmplx_EligibilityAndProductInfo_FinalInterestRate',parseFloat(Intrate).toFixed(2));
		setEnabledCustom('cmplx_EligibilityAndProductInfo_FinalInterestRate',false);
		setNGValueCustom('cmplx_EligibilityAndProductInfo_InterestRate',parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_InterestRate')).toFixed(2));
		//below change by saurabh on 28th nov 17 for recalculating emi on iterest rate change.
		if(getNGValue('PrimaryProduct')=='Personal Loan' || getNGValue('Subproduct_productGrid')=='IM')//added by akshay on 24/1/18
		{
			setNGValueCustom("Interest_Rate_App_req","Y");
		}
		return true;
	}
	
		/*else if(pId=='TLIssueDate'){
			if(validateFutureDate(pId))
				//commented by saurabh on 29th Nov for DROp2 CR.
				//return true;
		}*/
		//change by saurabh on 29th nov 17 for Drop2 cr.
		else if(pId=='estbDate'){
			if(validateFutureDate(pId)){
			var lobValue=calcAge(getNGValue(pId),'');
			YYMM('','lob',lobValue);
			}
				
		}
		else if(pId=='eff_lob_date'){
			if(validateFutureDate(pId)){
			var elobValue=calcAge(getNGValue(pId),'');
			YYMM('','EffectiveLOB',elobValue);
			}
				
		}
		//change in self-supp 20-08-2019
		else if(pId=='cmplx_CardDetails_SelfSupp_Limit')
		{
			if(parseFloat(getNGValue(pId))>parseFloat(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')))
			{
				showAlert('cmplx_CardDetails_SelfSupp_Limit',alerts_String_Map['VAL389']);
				return false;
			}				
		}
		
	//below code commented by akshay on 17/11/17 as discussed with shashank
	else if(pId=='cmplx_EmploymentDetails_targetSegCode'){
	
		if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='EMPID')
		{
			setVisible('cmplx_EmploymentDetails_IndusSeg',true);
			setVisible('EMploymentDetails_Label59',true);
			setVisible('EMploymentDetails_Label3',false);
			setVisible('cmplx_EmploymentDetails_OtherBankCAC',false);
		}
		else if(getNGValue('cmplx_EmploymentDetails_targetSegCode')=='CAC'){
			if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='S')
			{
				setVisible('cmplx_EmploymentDetails_IndusSeg',true);
				setVisible('EMploymentDetails_Label59',true);
				}
			else{
				setVisible('cmplx_EmploymentDetails_IndusSeg',false);
				setVisible('EMploymentDetails_Label59',false);
			}
			setVisible('EMploymentDetails_Label3',true);
			setVisible('cmplx_EmploymentDetails_OtherBankCAC',true);
			setVisible('Liability_New_Label5',true);//Manish points -- 21/10/18 -- Nikhil
			setVisible('cmplx_Liability_New_CACBankName',true);
		}
		else{
			setVisible('EMploymentDetails_Label3',false);
			setVisible('cmplx_EmploymentDetails_OtherBankCAC',false);
			setVisible('Liability_New_Label5',false);//Manish points -- 21/10/18 -- Nikhil
			setVisible('cmplx_Liability_New_CACBankName',false);
			setVisible('cmplx_EmploymentDetails_IndusSeg',false);
			setVisible('EMploymentDetails_Label59',false);
		}
		//change for incoming doc new by saurabh
		return true;
	}
	else if(pId=='TargetSegmentCode')//Manish points -- 21/10/18 -- Nikhil
	{
		if(getNGValue(pId)=='CAC')
		{
		setVisible('Liability_New_Label5',true);//Manish points -- 21/10/18 -- Nikhil
		setVisible('cmplx_Liability_New_CACBankName',true);
		}
		else
		{
		setVisible('Liability_New_Label5',false);//Manish points -- 21/10/18 -- Nikhil
		setVisible('cmplx_Liability_New_CACBankName',false);
		}
	}
	else if(pId=='cmplx_EmploymentDetails_ApplicationCateg')
	{
		if(getNGValue('cmplx_EmploymentDetails_ApplicationCateg')=='S')
			{
				setVisible('EMploymentDetails_Label9',true);
				setVisible('cmplx_EmploymentDetails_LengthOfBusiness',true);
			}
			//Deepak code change for PCAS-2483
		/*else
			{
				setVisible('EMploymentDetails_Label9',false);
				setVisible('cmplx_EmploymentDetails_LengthOfBusiness',false);
			}*/		
		return true;
	}
	
	 //Arun (06/12/17) to make industry segment visible
	//uncommened till here// ref-26092017-aloc Deepak
	//added By Akshay
	else if(pId=='indusMAcro')
		return true;
		
	else if(pId=='indusSector')
		return true;
		
	else if(pId=='cmplx_EmploymentDetails_EmpIndusSector')
		return true;
		
	else if(pId=='cmplx_EmploymentDetails_Indus_Macro')
		return true;
	//ended by Akshay
	
	else if(pId=='IncomeDetails_BankStatFromDate'){
		validateFutureDate(pId);
		if(CompareFrom_ToDate(pId,'IncomeDetails_BankStatToDate')==false){
			//commented by saurabh on 2nd dec as it is causing unknown behaviour
			//document.getElementById(pId).value=null;
			setNGValueCustom(pId,'');
		}
	}
	else if(pId=='IncomeDetails_BankStatToDate'){
		validateFutureDate(pId);
		if(CompareFrom_ToDate('IncomeDetails_BankStatFromDate',pId)==false){
		//commented by saurabh on 2nd dec as it is causing unknown behaviour
			//document.getElementById(pId).value=null;
		setNGValueCustom(pId,'');			
		}	
	}	
	 if(pId=='TLExpiry'){
		validatePastDate(pId,"Trade License Expiry");
	}
	else if(pId=='DOB'){
		validateFutureDate(pId);
	}
	else if(pId=='dob'){
	//change by saurabh on 1 Dec for Tanshu points.
	if(validateFutureDate('dob')){
			setNGValueCustom('age',calcAge(getNGValue('dob'),''));
			//return true;
		}
		}
		//case added by saurabh on 1st Dec 17 for Tanshu points.
	else if(pId=='addtype'){
		if(getNGValue(pId)!='RESIDENCE'){
			setLockedCustom('AddressDetails_ResidenceAddrType',true);
		}
		else{
			setLockedCustom('AddressDetails_ResidenceAddrType',false);
		}
	}
	 if(pId=='PassportExpiryDate'){
		validatePastDate(pId,"Passport Expiry");
		}
	else if(pId=='eidExpiry'){
		validatePastDate(pId,"EID Expiry");
	}
	else if(pId=='visaExpiry'){
		validatePastDate(pId,"Visa Expiry");
	}	
	else if(pId=='TLExpiry'){
		validatePastDate(pId, "TLE Expiry");
	}	
	else if(pId=='PartnerDetails_Dob'){
		validateFutureDate(pId);
	}	
	else if(pId=='VisaExpiryDate'){
		validatePastDate(pId,"Visa Expiry");	
	}
	
	//added By Akshay on 14/9/17 for validating dates in Authorised tab
	else if(pId=='AuthorisedSignDetails_DOB'){
		validateFutureDate(pId);	
	}
	
	else if(pId=='AuthorisedSignDetails_VisaExpiryDate'){
		validatePastDate(pId,'Visa Expiry');	
	}
	
	else if(pId=='AuthorisedSignDetails_PassportExpiryDate'){
		validatePastDate(pId,'Passport Expiry');	
	}
	//ended By Akshay on 14/9/17 for validating date in Authorised tab
	
	//modified by akshay on 14/10/17
	else if(pId=='cmplx_EmploymentDetails_DOJ'){
		if(validateFutureDate(pId)){
			var LOS=calcAge(getNGValue(pId),'');
			setNGValueCustom('cmplx_EmploymentDetails_LOS',LOS);
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
		}	
	}	
		
	//added by bhavnish and yash on 11/6/2017 for calling function
 /*   else if (pId == 'cmplx_EligibilityAndProductInfo_Tenor')
        {
		
		MaturityDate();
        }*/
		 
   /* else if (pId == 'cmplx_EligibilityAndProductInfo_Moratorium')
        {
		FirstRepaymentDate();
		MaturityDate();
        }
	*/
//modified by akshay on 14/10/17	
    if (pId == 'cmplx_EligibilityAndProductInfo_FirstRepayDate')
	    {
	     if(validatePastDate(pId,'First Repayment')){
		// FirstRepaymentDate();
			var maturityDate=MaturityDate();
			setNGValueCustom("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
			if(getNGValue('PrimaryProduct')=='Personal Loan'){
				 ageatmaturity();
	/*			var d= new Date();
				var today = d.getDate()+'/'+d.getMonth()+'/'+d.getFullYear();
				var moratorium = calcAge(today,getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate'));
				var months = (parseInt(moratorium.split('.')[0])*12)+parseInt(moratorium.split('.')[1])-1;
				setNGValueCustom('cmplx_EligibilityAndProductInfo_Moratorium',months);
		*/
		    var Days= Moratorium();
			setNGValueCustom('cmplx_EligibilityAndProductInfo_Moratorium',Days);
		}
		 }
		 //ageatmaturity();
		 //return true;
		 
		}
		
	
	else if (pId == 'cmplx_EligibilityAndProductInfo_TakeoverAMount')
        {
		var finalLoanAmount= getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit');
		var takeoverAmount=getNGValue('cmplx_EligibilityAndProductInfo_TakeoverAMount');
			if (parseInt(takeoverAmount)>parseInt(finalLoanAmount)){
					showAlert('cmplx_EligibilityAndProductInfo_TakeoverAMount',alerts_String_Map['VAL331']);
		}
		
		}
	
	
	else if (pId == 'cmplx_EligibilityAndProductInfo_Moratorium')
        {
		if (getNGValue('cmplx_EligibilityAndProductInfo_Moratorium') !=''){
		FirstRepaymentDate();
		var maturityDate=MaturityDate();
		setNGValueCustom("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
		if(getNGValue('PrimaryProduct')=='Personal Loan'){
			 ageatmaturity();
        }
		}
		}
		
    /*else if (pId == 'cmplx_EligibilityAndProductInfo_MaturityDate')
	{
	//alert(getNGValue('cmplx_EligibilityAndProductInfo_Tenor'));
		 validatePastDate(pId,"Maturity");
		 //ageatmaturity();
    }*/
	
		// ended by bhavnsih and yash on 11/6/2017s
	else if(pId=='AlternateContactDetails_MobileNo1')
	
	{
		var mob_No=getNGValue('AlternateContactDetails_MobileNo1');
		if(mob_No.length <10){
		var mob_No="00971"+mob_No;
		}
		setNGValueCustom('AlternateContactDetails_MobileNo1',mob_No);
		setNGValueCustom('AlternateContactDetails_MobNo2',mob_No); //added By Tarang started on 13/02/2018 as per drop 4 point 16
		
	}
	// added by yash on 21/7/2017 for netrate
	else if (pId == 'cmplx_EligibilityAndProductInfo_FinalInterestRate')
        {
		calcnetrate();
        }
		// ended 
	else if(pId=='AlternateContactDetails_MobNo2')
	
	{
		var mob_No=getNGValue('AlternateContactDetails_MobNo2');
		if(mob_No.length <10){
		var mob_No="00971"+mob_No;
		}
		setNGValueCustom('AlternateContactDetails_MobNo2',mob_No);
		
	}
	else if (pId=='cmplx_Customer_Title')
	{
	if(getNGValue(pId)=='FTHER' || getNGValue(pId)=='HE' || getNGValue(pId)=='HH' || getNGValue(pId)=='MAST.' || getNGValue(pId)=='MR.' || getNGValue(pId)=='SH' || getNGValue(pId)=='SIR')
	{
		setNGValue('cmplx_Customer_gender','M');
	}
	else if(getNGValue(pId)=='HREXC' || getNGValue(pId)=='HRH' || getNGValue(pId)=='HRM' || getNGValue(pId)=='MADAM' || getNGValue(pId)=='MISS' || getNGValue(pId)=='MISSUS' || getNGValue(pId)=='MRS.' || getNGValue(pId)=='SHA')
	{
		setNGValue('cmplx_Customer_gender','F');
	}
	}
	
   
	//deepak SupplementCardDetails_Text2 added in below condition to validate supplymentry Email ID. JIRA PCAS-2073 on 08 July 2019.
	else if (pId=='AlternateContactDetails_Email1' || pId=='AlternateContactDetails_Email2' || pId=='GuarantorDetails_email' || pId=='SupplementCardDetails_Text2' || pId=='cmplx_Customer_Email_ID'){
		validateMail1(pId);
	}
	//Cr61 Prabhakar 27-08-2019
	if (pId=='AlternateContactDetails_Email1' && (validateMail1('AlternateContactDetails_Email2') || getNGValue('AlternateContactDetails_Email2')==''))
	{
		
		setNGValueCustom('AlternateContactDetails_Email2',getNGValue('AlternateContactDetails_Email1'));
	}
	
	//commented by saurabh on 10th Nov2017
	/*else if(pId=='AlternateContactDetails_carddispatch'){
		if(getNGValue(pId)=='Branch'){
			setVisible('AlternateContactDetails_custdomicile',true);
			setVisible('AltContactDetails_Label14',true);
		}	
		else{
			setVisible('AlternateContactDetails_custdomicile',false);
			setVisible('AltContactDetails_Label14',false);
		}
		return true;
	}*/	
	else if(pId=='mobNo')
	{
		var Mobile_no=getNGValue('mobNo');
		var mob_No=Mobile_no;
		if(mob_No == ""){
		showAlert('mobNo',alerts_String_Map['VAL086']);
		return false;
		}
		else if(Mobile_no.substring(0,5)!='00971' && mob_No.length<10){
			mob_No="00971"+mob_No;
			setNGValueCustom('mobNo',mob_No);
		}	
		else if(mob_No.length!=14){
			showAlert('mobNo',alerts_String_Map['VAL087']);
			return false;
		}	

	}	
	
	else if(pId=='cmplx_CardDetails_SuppCardReq')
		{
			//changed by akshay on 3/1/17
			if(getNGValue('cmplx_CardDetails_SuppCardReq')=='Yes')
			{
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='IM')
			{
			showAlert(pId,'Self and standalone supplementary card not allowed for IM/LOC product!');
			setNGValue('cmplx_CardDetails_SuppCardReq','No');
			return false;
			}
				setVisible("Supplementary_Container", true);
				setNGValueCustom('IS_SupplementCard_Required','Y');//added by akshay on 3/1/17
				setTop('Supplementary_Container',parseInt(getTop('CardDetails_container'))+parseInt(getHeight('CardDetails_container'))+30+'px');
				setTop('FATCA_container',parseInt(getTop('Supplementary_Container'))+parseInt(getHeight('Supplementary_Container'))+30+'px');
				setTop('KYC_container',parseInt(getTop('FATCA_container'))+parseInt(getHeight('FATCA_container'))+30+'px');
				setTop('OECD_container',parseInt(getTop('KYC_container'))+parseInt(getHeight('KYC_container'))+30+'px');
				setTop('ReferenceDetails_container',parseInt(getTop('OECD_container'))+parseInt(getHeight('OECD_container'))+30+'px');
				
				
				//return true;
				
			}

			else
			{
				if(getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid')>0)
				{
					showAlert(pId,alerts_String_Map['VAL358']);
					setNGValueCustom(pId,'Yes');
					return false;
				}
				
				setVisible("Supplementary_Container", false);
				setNGValueCustom('IS_SupplementCard_Required','N');
			}
		}
		//++below code added by nikhil for Self-Supp CR
		else if(pId=='cmplx_CardDetails_SelfSupp_required')
		{
			if(getNGValue(pId)=='Yes')
			{
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='IM')
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
	
	
	
	else if(pId=='cmplx_FATCA_ExpiryDate'){
		validatePastDate(pId,"Expiry");	
	}
	else if(pId=='cmplx_Customer_Dsa_NAme')	
	{
		return true;
	}
	//added By akshay on 14/9/2017 for auto populating expiry date based on signed date
	else if(pId=='cmplx_FATCA_SignedDate'){
		if(validateFutureDate(pId))
		{
		  
			var signedDate=getNGValue(pId);
			var years_signedDate=signedDate.substring(6,10);
			var years_expiryDate=parseInt(years_signedDate)+3;
			var ExpiryDate=signedDate.replace(years_signedDate,years_expiryDate);
			setNGValueCustom('cmplx_FATCA_ExpiryDate',ExpiryDate);
			setLockedCustom("cmplx_FATCA_ExpiryDate",true);//Arun (11/12/17) to make non editable if signed date is selected
		  
		} 
	}	
		//ended By akshay on 14/9/2017 for auto populating expiry date based on signed date

	//added By Akshay on 28/06/2017 
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
	//ended By Akshay on 28/06/2017 
	
	//added By Tarang for drop 4 point 1 started on 02/22/2018
	else if (pId=='AlternateContactDetails_carddispatch'){
		var cardDisp=getNGSelectedItemText('AlternateContactDetails_carddispatch');
		if(cardDisp=='International dispatch-INTL' || cardDisp=='Holding WI- HOLD' || cardDisp=='Card centre collection' || cardDisp=='COURIER'){
			setNGValueCustom('Card_Dispatch_Option',cardDisp);
		}
		else{
			setNGValueCustom('Card_Dispatch_Option','Branch');
		}
		//alert(getNGValue('Card_Dispatch_Option'));
	}
	//added By Tarang for drop 4 point 1 ended on 02/22/2018
	//added By Akshay on 15/9/2017 for disabling below fields when transfermode is cheque
		if(pId=='transferMode'){
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
				setNGValue('creditcardNo','');
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
				setLockedCustom("Account_No_for_Swift",false);
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
				setNGValue('creditcardNo','');//pcasp-1377
				setLockedCustom("creditcardNo",true);
				setNGValue('iban','');
			}
			else if(getNGValue('transtype')=='CCC' && getNGValue('transferMode')=='A'){
				setNGValue('chequeNo','');//pcasp-1377
				setNGValue('chequeDate','');//pcasp-1377
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
		}
		
		else if(pId=='transtype'){
			setNGValue('transferMode','');//PCASP-3202
			//changes by saurabh on 14 nov 17.
			if(getNGValue('transtype')=='BalanceTransfer'||getNGValue('transtype')=='BT'){
					setNGValueCustom('tenor','');//pcasp-1377
					setLockedCustom("tenor",true);
					setNGValueCustom('creditcardNo','');
					setLockedCustom("creditcardNo",false);
					setLockedCustom("bankName",false);
					setNGValue("PaymentPurpose",'');//PCASP-3186
					setVisible("CC_Loan_Label25",false);
					setVisible("PaymentPurpose",false);
				}
		
			else if(getNGValue('transtype')=='CreditCardCheque'||getNGValue('transtype')=='CCC'){
				setNGValueCustom('tenor','');//pcasp-1377
				setLockedCustom("tenor",true);
				setNGValueCustom('creditcardNo','');
				setLockedCustom("creditcardNo",true);
				setLockedCustom("bankName",true);
				setVisible("CC_Loan_Label25",true);
				setVisible("PaymentPurpose",true);
			}
			else if(getNGValue('transtype')=='LOC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM'){
				showAlert('transtype','Loan On card is only applicable for IM');
				setNGValueCustom('transtype','');
				setNGValueCustom('creditcardNo','');
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
				setNGValueCustom('benificiaryName',getNGValue("cmplx_Customer_FIrstNAme")+' '+getNGValue('cmplx_Customer_MiddleName')+' '+getNGValue("cmplx_Customer_LAstNAme"));
				}
				setLockedCustom("creditcardNo",false);
				setLockedCustom("bankName",false);	
			}
			return true;
		}
		
		 if(pId=='bankName'){
			setNGValueCustom('bankCode',getNGValue(pId));
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
		else if(pId=='cmplx_CC_Loan_DDSMode'){
			if(getNGValue('cmplx_CC_Loan_DDSMode')=='flat'||getNGValue('cmplx_CC_Loan_DDSMode')=='F'){
				setLockedCustom('cmplx_CC_Loan_DDSAmount',false);
				setLockedCustom('cmplx_CC_Loan_Percentage',true);
				setNGValue("cmplx_CC_Loan_Percentage",'');//Added by shweta for pcasp-1504

			}
			else if(getNGValue('cmplx_CC_Loan_DDSMode')=='Per' ||getNGValue('cmplx_CC_Loan_DDSMode')=='P'){
				setLockedCustom('cmplx_CC_Loan_Percentage',false);
				setLockedCustom('cmplx_CC_Loan_DDSAmount',true);
				setNGValue("cmplx_CC_Loan_Percentage",'100');//Added by shweta for pcasp-1504
				setNGValue("cmplx_CC_Loan_DDSAmount",'');//Added by shweta for pcasp-1504

			}
			//PCASP-3177
			else if(getNGValue('cmplx_CC_Loan_DDSMode')=='M')
			{
				setLockedCustom("cmplx_CC_Loan_DDSAmount",true);
				setLockedCustom("cmplx_CC_Loan_Percentage",true);
				setNGValueCustom("cmplx_CC_Loan_Percentage",'5');
				setNGValueCustom("cmplx_CC_Loan_DDSAmount",'');
			}
			else{
				setLockedCustom('cmplx_CC_Loan_DDSAmount',true);
				setLockedCustom('cmplx_CC_Loan_Percentage',true);
				setNGValue("cmplx_CC_Loan_Percentage",'');//Added by shweta for pcasp-1504
				setNGValue("cmplx_CC_Loan_DDSAmount",'');//Added by shweta for pcasp-1504

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
		//ended by Akshay on 11/10/17 for point 63---On changing the DDS mode type from percentage to flat the percentage field is not getting disabled

		 if(pId=='cmplx_CC_Loan_ModeOfSI'){
		if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='F'){
				setLockedCustom('cmplx_CC_Loan_FlatAMount',false);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',true);
				setNGValue('cmplx_CC_Loan_SI_Percentage','');//added by shweta for pcasp-1504

			}
			else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='P'){
				setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
				setNGValue('cmplx_CC_Loan_FlatAMount','');//added by shweta for pcasp-1504
				setNGValue('cmplx_CC_Loan_SI_Percentage','100');//added by shweta for pcasp-1504
			}
			//PCASP-3177
			else if(getNGValue('cmplx_CC_Loan_ModeOfSI')=='M'){
				setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',true);
				setNGValue('cmplx_CC_Loan_FlatAMount','');
				setNGValue('cmplx_CC_Loan_SI_Percentage','3');
				setNGValueCustom('cmplx_CC_Loan_FlatAMount','');
			}
			else if(getNGValue('cmplx_CC_Loan_ModeOfSI')==''){//added by shweta for pcasp-1504
				setLockedCustom('cmplx_CC_Loan_FlatAMount',false);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
				setNGValue('cmplx_CC_Loan_FlatAMount','');
				setNGValue('cmplx_CC_Loan_SI_Percentage','');//added by shweta for pcasp-1504

			}
		else{
				setLockedCustom('cmplx_CC_Loan_FlatAMount',true);
				setLockedCustom('cmplx_CC_Loan_SI_Percentage',false);
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
		else if(pId=='cmplx_CC_Loan_SI_day'){
			if(getNGValue(pId)<1 || getNGValue(pId)>28 && getNGValue(pId)!=""){
				showAlert('cmplx_CC_Loan_SI_day',alerts_String_Map['VAL101']);
				return false;
			}	
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
	//ended By Akshay on 15/9/2017 for disabling below fields when transfermode is cheque

	//below function by saurabh for incoming doc new start
	else if(pId=='IncomingDocNew_DocType'){
		setNGValueCustom('IncomingDocNew_mandatory','');
		setNGValueCustom('IncomingDocNew_Status','');
		setNGValueCustom('IncomingDocNew_Remarks','');
		setNGValueCustom('IncomingDocNew_Docindex','');
		var val = getNGValue('IncomingDocNew_DocType');
			if(val && val!='' && val!='--Select--'){
				return true;
			}
			showAlert(pId,'Please select a Document Type');
			return false;
	}
	else if(pId=='IncomingDocNew_DocName'){
		var val = getNGValue('IncomingDocNew_DocName');
		setNGValueCustom('IncomingDocNew_Status','');
		setNGValueCustom('IncomingDocNew_Remarks','');
		setNGValueCustom('IncomingDocNew_Docindex','');
		if(val && val!='' && val!='--Select--'){
				var mandDocs = getNGValue('cmplx_IncomingDocNew_MandatoryDocument');
				if(mandDocs.indexOf(getNGValue('IncomingDocNew_DocType'))>-1){
					setNGValueCustom('IncomingDocNew_mandatory','Y');
				}
				else{
					setNGValueCustom('IncomingDocNew_mandatory','N');
				}
			}
			else{
			showAlert(pId,'Please select a Document Name');
			}
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
		}			//hritik 3667
		else{
			setLockedCustom('IncomingDocNew_DeferredUntilDate',true);
			setNGValue('IncomingDocNew_ExpiryDate','');
		}
	}
	else if(pId=='IncomingDocNew_DeferredUntilDate'){
		validatePastDate(pId,'Deferred Until');
	}
	//below function by saurabh for incoming doc new end
	
	
	//25/07/2017 expiry Date Tanshu
		else if(pId == 'cmplx_DocName_ExpiryDate'){
		var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		var row_clicked_Expiry = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn1";
		var  ExpiryDate = document.getElementById(row_clicked_Expiry).value;
		
			validateExpiryDate(ExpiryDate,row_clicked_Expiry);
		}
	//ended
	else if(pId=='cmplx_DecisionHistory_Decision')
	{
		setNGValueCustom('Decision',getNGValue(pId));
		//changesd by nikhil 26/11/18 to loadpicklist 'customer-hold' case
		if(getNGValue(pId).toUpperCase()=='REJECT' || getNGValue(pId).toUpperCase()=='RE-INITIATE' || getNGValue(pId)=='Customer Hold-Document requested'){//addedby akshay on 21/10/18
			//setLockedCustom('DecisionHistory_DecisionReasonCode',false);
			return true;//added by akshay on 28/4/18 for proc 8457
		}
		else{
			if(getNGValue(pId)=='Submit'){
			//change by saurabh on 20th dec
				if(getNGValue('Is_Financial_Summary')!='Y' && getNGValue('cmplx_Customer_NTB')!=true){
				showAlert(pId,'Please fetch Financial Summary first');
				setNGValueCustom(pId,'--Select--');
				}
				else if((getNGValue('reEligbility_init_counter').split(';')[1])!='Y'){
				showAlert(pId,'Please run Dectech first');
				setNGValueCustom(pId,'--Select--');	
				}
				else if(parseInt(getNGValue('reEligbility_init_counter').split(';')[0])==0){
				showAlert(pId,'Please run Calculate Re-Eligibility');
				setNGValueCustom(pId,'--Select--');	
				}
				else if(getNGValue('IS_WORLD_CHECK')==null || getNGValue('IS_WORLD_CHECK')==''){
				showAlert('WorldCheck_fetch','Please perform fetch world check');
				setNGValueCustom(pId,'--Select--');	
				return false;
				}
			}
			setLockedCustom('DecisionHistory_DecisionReasonCode',true);
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
	//added by abhishek point 64
	// else if(pId=='cmplx_CC_Loan_TransMode')	
	// {
		// return true;	
	// }
	//added by abhishek point 65
	else if(pId=='cmplx_CC_Loan_DDSStartdate'){
		validatePastDate(pId,"DDS Start");
	}
	
	else if(pId=='NotepadDetails_notedesc'){
		setNGValueCustom('NotepadDetails_notedetails','');//added on 9/1/18
		return true;
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
	// ADDED BY YASH ON 25/7/2017 for proc 475
	else if (pId == 'cmplx_EligibilityAndProductInfo_Tenor')
        {

		var maturityDate=MaturityDate();
		setNGValueCustom("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
		setNGValueCustom("cmplx_EligibilityAndProductInfo_NumberOfInstallment",getNGValue('cmplx_EligibilityAndProductInfo_Tenor'));
		if(getNGValue('PrimaryProduct')=='Personal Loan'){
			 ageatmaturity();
		}
		//below change by saurabh on 28th nov 17 for recalculating emi on tenor change.
		return true;
        }
	/*
	else if(pId=='cmplx_EligibilityAndProductInfo_FinalInterestRate')
	{
		//modified by akshay on 24/1/18
		if(getNGValue('PrimaryProduct')=='Personal Loan' || getNGValue('Subproduct_productGrid')=='IM')//changed by akshay on 11/1/18
		{
			setNGValueCustom("Interest_Rate_App_req","Y");
		}
	}---commented by akshay on 24/1/18*/	
	//added by akshay on 28/12/17
	/*else if(pId=='Limit')
	{
		var limit=parseFloat(getNGValue(pId));
		var emi=(2/100*limit).toFixed(2);
		setNGValueCustom('Emi',emi);
	}*/
	else if(pId=='SupplementCardDetails_Text1' || pId=='passportNo'){
		var flag = false;
		if(pId=='SupplementCardDetails_Text1' && getNGValue('cmplx_Customer_EmiratesID')==getNGValue(pId)){
			flag = true;
		}
		else if(pId=='passportNo' && getNGValue('cmplx_Customer_PAssportNo')==getNGValue(pId)){
			flag = true;
		}
		if(flag){
			showAlert(pId,alerts_String_Map['VAL999']);
			setNGValueCustom(pId,'');
			return false;
		}
	}
	if(pId=='cmplx_Customer_EmiratesID')
	{
	setNGValueCustom('cmplx_Customer_ResidentNonResident','Y');
	}
	
	if(pId=='FirstName' || pId=='lastname' || pId=='passportNo' || pId=='DOB' || pId=='nationality' || pId=='MobNo' || pId=='SupplementCardDetails_Text1'){
		setLockedCustom('SupplementCardDetails_FetchDetails',false);
	}
	//added by nikhil 17/12/2018 as in previous code
	//code by Saurabh on 1st Dec 18
	if(pId=='SupplementCardDetails_ApprovedLimit'){
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
	
 return false;
 }
 
 function blur_RLOS(pId)
 {
	if(pId=='cmplx_Customer_MobNo')
	{
		var Mobile_no=getNGValue('cmplx_Customer_MobNo');
		var mob_No=Mobile_no;
		if(mob_No.substring(0,5)!='00971' && mob_No.length<10 && mob_No!=""){
			mob_No="00971"+mob_No;
			setNGValueCustom('cmplx_Customer_MobNo',mob_No);
		}	
		if(mob_No.length!=14 && mob_No!=""){
			showAlert('cmplx_Customer_MobNo',alerts_String_Map['VAL087']);
			return false;
		}	
		setNGValueCustom('OTP_Mobile_NO',mob_No);
	}
	
	
	if(pId=='ReferenceDetails_reference_phone')
	{
	    var Mobile_no=getNGValue('ReferenceDetails_reference_phone');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=""){
			Mobile_no="00971"+Mobile_no;
			setNGValueCustom('ReferenceDetails_reference_phone',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=""){
			showAlert('ReferenceDetails_reference_phone',alerts_String_Map['VAL087']);
			setNGValueCustom('ReferenceDetails_reference_phone','');
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=""){
			showAlert('ReferenceDetails_reference_phone',alerts_String_Map['VAL088']);
			setNGValueCustom('ReferenceDetails_reference_phone','');
			return false;
		}
	}
	
	if(pId=='MobNo')
	{
	    var Mobile_no=getNGValue('MobNo');
		if(Mobile_no.substring(0,5)!='00971' && Mobile_no.length<10 && Mobile_no!=""){
			Mobile_no="00971"+Mobile_no;
			setNGValueCustom('MobNo',Mobile_no);
		}	
		if(Mobile_no.length!=14 && Mobile_no!=""){
			showAlert('MobNo',alerts_String_Map['VAL087']);
			setNGValueCustom('MobNo','');
			return false;
		}
		if(Mobile_no.length==14 && Mobile_no.substring(0,5)!='00971' && Mobile_no!=""){
			showAlert('MobNo',alerts_String_Map['VAL088']);
			setNGValueCustom('MobNo','');
			return false;
		}
	}
	
	if(pId=='AlternateContactDetails_MobileNo1')
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
		if(getNGValue('AlternateContactDetails_MobNo2')=='')
		{
			setNGValueCustom('AlternateContactDetails_MobNo2',Mobile_no); //added By Tarang started on 13/02/2018 as per drop 4 point 16
		}
	}
	
	if(pId=='AlternateContactDetails_MobNo2')
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
	
	else if(pId=='cmplx_Customer_CIFNO'){
		var cif=getNGValue('cmplx_Customer_CIFNO');
		if(cif.length!=7 && cif.length!=0){
			showAlert('cmplx_Customer_CIFNO',alerts_String_Map['VAL034']);
			return false;
		}
		else{
			//below code added by nikhilset
			//hash='###'+cif.substring(3,7)+'###';
			setNGValueCustom('Is_Entity_Details','N');
			setEnabledCustom("FetchDetails",true);
			return false;
		}
	}
	// ADDED BY YASH ON 25/7/2017 for proc 475
	else if (pId == 'cmplx_EligibilityAndProductInfo_Tenor')
        {

		var maturityDate=MaturityDate();
		setNGValueCustom("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
		if(getNGValue('PrimaryProduct')=='Personal Loan'){
			 ageatmaturity();
		}
		//below change by saurabh on 28th nov 17 for recalculating emi on tenor change.
		return true;
        }
	
		// ended by yash on 25/7/2017
  //return false;	
 }
 
 
 
 //New method added to handle keypress event Deepak 18 Dec 2017
 function keydown_RLOS(pId,keyCode_str)
 {
	//pID name need to added which are mentioned as 17,2 in field list document.
	if(pId=='cmplx_IncomeDetails_housing' || pId=='cmplx_IncomeDetails_Basic' || pId=='cmplx_IncomeDetails_transport' || pId=='cmplx_IncomeDetails_CostOfLiving' || pId=='cmplx_IncomeDetails_FixedOT' || pId=='cmplx_IncomeDetails_Other' || pId=='cmplx_IncomeDetails_Commission_Month1'|| pId=='cmplx_IncomeDetails_Commission_Month2' || pId=='cmplx_IncomeDetails_Commission_Month3' || pId=='cmplx_IncomeDetails_Flying_Month1' || pId=='cmplx_IncomeDetails_Flying_Month2' || pId=='cmplx_IncomeDetails_Flying_Month3' || pId=='cmplx_IncomeDetails_Bonus_Month1' || pId=='cmplx_IncomeDetails_Bonus_Month2' || pId=='cmplx_IncomeDetails_Bonus_Month3' || pId=='cmplx_IncomeDetails_Overtime_Month1' || pId=='cmplx_IncomeDetails_Overtime_Month2' || pId=='cmplx_IncomeDetails_Overtime_Month3' || pId=='cmplx_IncomeDetails_FoodAllow_Month1' || pId=='cmplx_IncomeDetails_FoodAllow_Month2' || pId=='cmplx_IncomeDetails_FoodAllow_Month3' || pId=='cmplx_IncomeDetails_PhoneAllow_Month1' || pId=='cmplx_IncomeDetails_PhoneAllow_Month2' || pId=='cmplx_IncomeDetails_PhoneAllow_Month3' || pId=='cmplx_IncomeDetails_serviceAllow_Month1' || pId=='cmplx_IncomeDetails_serviceAllow_Month2' || pId=='cmplx_IncomeDetails_serviceAllow_Month3'|| pId=='cmplx_IncomeDetails_netSal1' || pId=='cmplx_IncomeDetails_netSal2' || pId=='cmplx_IncomeDetails_netSal3' || pId=='cmplx_IncomeDetails_AvgBal' || pId=='cmplx_IncomeDetails_CredTurnover' || pId=='cmplx_IncomeDetails_AvgCredTurnover'||pId=='cmplx_IncomeDetails_Pension'){
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
	 //add pId for 18,2 fields
	 else if(pId=='cmplx_CC_Loan_FlatAMount' || pId=='cmplx_IncomeDetails_UnderwritingOther')
	 {
	 
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
			if(value.length==18 && !(keyCode_str=='110' || keyCode_str=='190')){
				setNGValueCustom(pId,getNGValue(pId));
				return false;
			}	
		}
		return true;
	 
	 }
	  else if(pId=='cmplx_Customer_yearsInUAE' || pId=='cmplx_IncomeDetails_DurationOfBanking' || pId=='Liability_New_mob' || pId=='years' ){
			var value=getNGValue(pId);
			var filter=/^(\d{1,3})?(\.)?((0?[0-9]|1[012]))?$/;
			//console.log(!/^(\d{1,3})(\.(0?[0-9]|1[012]))?$/.test(value));//^(\d{1,3})?(\.)?((0?[0-9]|1[012]))?$
		if(keyCode_str!='8' && keyCode_str!='46'){//backspace and delete key codes	
			if(!filter.test(value)){
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
	else if(pId=='cmplx_CardDetails_CardEmbossingName' || pId=='cardEmbName') 	
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
	 
