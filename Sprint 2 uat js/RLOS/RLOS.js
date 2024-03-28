/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : RLOS
File Name                                                                     : RLOS.js
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : All js custom functions 
------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------
Date					  Change By						Change Description 
31.01.2017                Disha                         for optimization, such that on second time fragment click comboload function is not called again and again from java code
fdg
------------------------------------------------------------------------------------------------------------------------------------------------------
 */
 
//++ below code already present on 10/10/17
 var activityName = window.parent.stractivityName;
var pname=window.parent.strprocessname;
 
var row;
//to have docindex
var flag_ReadFromCIF=false;
var popup_win = null;
var flag_Company_FetchDetails=false;
var flag_Authorised_FetchDetails=false;
var aecbCallStatus="";
var GCC="BH,IQ,KW,OM,QA,SA,AE";
var rlosValuesData = {};
var hiddenString = '';
var RLOSFRAGMENTLOADOPT={};
RLOSFRAGMENTLOADOPT['CustomerDetails'] = 'N';//changed by akshay on 22/1/18
RLOSFRAGMENTLOADOPT['GuarantorDetails'] = '';
RLOSFRAGMENTLOADOPT['ProductDetailsLoader'] = 'N';//changed by akshay on 22/1/18
RLOSFRAGMENTLOADOPT['Incomedetails'] = '';
RLOSFRAGMENTLOADOPT['CompanyDetails'] = '';
RLOSFRAGMENTLOADOPT['PartnerDetails'] = '';
RLOSFRAGMENTLOADOPT['Liability_container'] = '';
RLOSFRAGMENTLOADOPT['EmploymentDetails'] = '';
RLOSFRAGMENTLOADOPT['MiscFields'] = '';	
RLOSFRAGMENTLOADOPT['EligibilityAndProductInformation'] = '';
RLOSFRAGMENTLOADOPT['Address_Details_container'] = '';
RLOSFRAGMENTLOADOPT['Alt_Contact_container'] = '';
RLOSFRAGMENTLOADOPT['Supplementary_Container'] = '';
RLOSFRAGMENTLOADOPT['CardDetails_container'] = '';
RLOSFRAGMENTLOADOPT['FATCA_container'] = '';
RLOSFRAGMENTLOADOPT['KYC_container'] = '';
RLOSFRAGMENTLOADOPT['OECD_container'] = '';
RLOSFRAGMENTLOADOPT['CC_Loan_container'] = '';
RLOSFRAGMENTLOADOPT['CustomerDocumentsContainer'] = '';
RLOSFRAGMENTLOADOPT['OutgoingDocumentsContainer'] = '';
RLOSFRAGMENTLOADOPT['DeferralContainer'] = '';
RLOSFRAGMENTLOADOPT['DecisioningFields'] = '';
RLOSFRAGMENTLOADOPT['DeviationHistoryContainer'] = '';
RLOSFRAGMENTLOADOPT['IncomingDocuments'] = '';
RLOSFRAGMENTLOADOPT['DecisionHistoryContainer'] = '';
RLOSFRAGMENTLOADOPT['AuthorisedSignatoryDetails'] = '';
RLOSFRAGMENTLOADOPT['ReferenceDetails_container'] = '';
RLOSFRAGMENTLOADOPT['Notepad_Values'] = '';

function focus_rlos(pId){
	
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
	
	//console.log(JSON.stringify(rlosValuesData));
}


function click_RLOS(pId,frameState){

		/*if(pId == 'Button2'){
		/*setNGValueCustom('cmplx_Liability_New_DBR','123');
		setNGValueCustom('cmplx_Liability_New_TAI','123');
		setNGValueCustom('cmplx_Liability_New_DBRNet','12');
		setNGValueCustom('cmplx_Liability_New_AggrExposure','1234');
		}----commented by akshay on 11/1/18 as it was empty event*/
		if(getNGValue(pId)!=null && getNGValue(pId)!=undefined && document.getElementById(pId).innerHTML.indexOf('<table') == -1){
			setNGValueCustom(pId,getNGValue(pId));
		}
		if(pId=='existingOldCustomer'){
			if(getNGValue('existingOldCustomer')==true){
				setLockedCustom('OldApplicationNo',false);
				setLockedCustom('Fetch_existing_cas',false);
			}
			else{
				setLockedCustom('OldApplicationNo',true);
				setLockedCustom('Fetch_existing_cas',true);
			}	
		//	return true;//added By AKshay on 10/10/17-----not required as this code is commented in java
		}
		
		if(pId=='PartnerDetails'){
			//var authRowCount = getLVWRowCount('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails');
			if(!isVisible('ShareHolding')){
			showAlert('AuthorisedSignatoryDetails',alerts_String_Map['VAL116']);
			setNGFrameState('PartnerDetails',1);
			return false;
			}
		}
		
		if(pId=='CustomerDetails'|| pId=='GuarantorDetails'|| pId=='ProductDetailsLoader'|| pId=='DecisionHistoryContainer'|| pId=='MiscFields'|| pId=='Address_Details_container' || pId=='Supplementary_Container'|| pId=='FATCA_container' || pId=='KYC_container' || pId=='OECD_container' || pId=='CustomerDocumentsContainer'|| pId=='OutgoingDocumentsContainer'|| pId=='DeferralContainer'|| pId=='DecisioningFields'|| pId=='DeviationHistoryContainer' || pId=='PartnerDetails' ||  pId=='DeferralDocuments'|| pId=='IncomingDocuments' || pId=='AuthorisedSignatoryDetails' || pId=='ReferenceDetails_container' || pId=='Notepad_Values'){
		
		if(pId=='DecisionHistoryContainer')
		{
			if(!checkMandatory_Frames(getNGValue('Mandatory_Frames')))
			return false;
			
		}
		
		if(RLOSFRAGMENTLOADOPT && frameState==0){
							var key = pId;
							var value = RLOSFRAGMENTLOADOPT[key];
								if(value=='' || value=='Y'){
									doNotLoadFragmentSecTime(pId);			
										return true;
								}		
							else 
								return false;
						
					} 
				}
				
		
		
		
		else if(pId=='SupplementCardDetails_FetchDetails')
		{
			if(getNGValue('SupplementCardDetails_Text1')==''){
			showAlert('SupplementCardDetails_Text1',alerts_String_Map['CC082']);
				return false;
			}
			else if(getNGValue('FirstName')==''){
			showAlert('FirstName',alerts_String_Map['VAL061']);
				return false;
			}
			/*else if(getNGValue('MiddleName')==''){
			showAlert('MiddleName',alerts_String_Map['VAL083']);
				return false;
			}--Commented By Akshay on 8/10/17 as it is not mandatory*/
			else if(getNGValue('lastname')==''){
			showAlert('lastname',alerts_String_Map['VAL076']);
				return false;
			}
			else if(getNGValue('passportNo')==''){
			showAlert('passportNo',alerts_String_Map['VAL098']);
				return false;
			}
			else if(getNGValue('DOB')==''){
			showAlert('DOB',alerts_String_Map['VAL049']);
				return false;
			}
			else if(getNGValue('nationality')=='' || getNGValue('nationality')=='--Select--'){
			showAlert('nationality',alerts_String_Map['VAL121']);
				return false;
			}
			else if(getNGValue('MobNo')==''){
			showAlert('MobNo',alerts_String_Map['VAL086']);
				return false;
			}
			/*else if(getNGValue('MotherNAme')==''){
			showAlert('MotherNAme',alerts_String_Map['VAL247']);
				return false;
			}*/
			
			/*else if(getNGValue('SupplementCardDetails_Text1')==''){
			showAlert('SupplementCardDetails_Text1',alerts_String_Map['VAL053']);
				return false;
			}--Commented By Akshay on 8/10/17 as it is not mandatory*/
			
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
			} //Arun 25/12/17
			
			else if(!checkForApplicantTypeInGrids("Supplement")){
				return false;
			}
			return true;
		}
		
		else if(pId=='cmplx_OECD_cmplx_GR_OecdDetails'){
			var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_OECD_cmplx_GR_OecdDetails');
			if(selectedRow != -1){
				if(getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,5)!=null && getLVWAT('cmplx_OECD_cmplx_GR_OecdDetails',selectedRow,5)!=""){
					setEnabledCustom('OECD_noTinReason',false);
				}
			}
		}
		/*else if(pId=='DecisionHistory_Button1')
		{		
			var url="/webdesktop/resources/scripts/Reject_Reasons.jsp";
			window.open(url, "", "width=500,height=500");
		}--Commented by AKshay on 16/9/17 as it is no longer required!!*/
		
		// disha changes for aecb - frame to load only if fetch liability call has success status start
		/*
		if(pId=='ELigibiltyAndProductInfo_Frame4')
		{			
			aecbCallStatus = getNGValue('aecb_call_status');
			if(aecbCallStatus=='Success')
				return true;
			
			else
				return false;	
		}
		if(pId=='ELigibiltyAndProductInfo_Frame5')
		{
			aecbCallStatus = getNGValue('aecb_call_status');
			if(aecbCallStatus=='Success')
				return true;
			
			else
				return false;
		}
		if(pId=='ELigibiltyAndProductInfo_Frame6')
		{
			aecbCallStatus = getNGValue('aecb_call_status');
			if(aecbCallStatus=='Success')
				return true;
			
			else
				return false;
		}	*/
		// disha changes for aecb - frame to load only if fetch liability call has success status end		
		
		//for optimization, such that on second time fragment click comboload function is not called again and again from java code
		
		
				
		else if(pId=='Incomedetails'){
			//var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			//var prodval=getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
			var Product_type=getNGValue('Product_Type');
			if(Product_type==''){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Incomedetails',1);
				return false;
			}	
			else if(RLOSFRAGMENTLOADOPT['Incomedetails']==''){
				RLOSFRAGMENTLOADOPT['Incomedetails']='N';
					return true;
			}
			
			else if(RLOSFRAGMENTLOADOPT['Incomedetails']=='N' ){
					if(getNGValue("PrimaryProduct")=='Credit Card'){
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
				//var EmpType=getNGValue("empType");//Tarang to be removed on friday(1/19/2018)
				var EmpType=getNGValue("EmploymentType");
				if(EmpType=="Salaried" || EmpType=="Salaried Pensioner"){
					setVisible("IncomeDetails_Frame3", false);
					setVisible("IncomeDetails_Frame2", true);
					com.newgen.omniforms.formviewer.setHeight("Incomedetails", "730px");
					com.newgen.omniforms.formviewer.setHeight("IncomeDetails_Frame1", "680px");
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
					setVisible("IncomeDetails_Frame1", true);
					setVisible("IncomeDetails_Frame2", false);
					setVisible("IncomeDetails_Frame3", true);
					com.newgen.omniforms.formviewer.setTop("IncomeDetails_Frame3","40px");
					com.newgen.omniforms.formviewer.setHeight("Incomedetails", "430px");
					com.newgen.omniforms.formviewer.setHeight("IncomeDetails_Frame1", "400px");
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
					//	alert("cmplx_IncomeDetails_AvgBalFreq"+cmplx_IncomeDetails_AvgBalFreq);
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
					//	alert("cmplx_IncomeDetails_AvgBalFreq"+cmplx_IncomeDetails_AvgBalFreq);
					}
				
				}
			
		}
		
		else if(pId=='Liability_container')
		{
			//var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		    //var prodval=getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
			var Product_type=getNGValue('Product_Type');
		
			if(Product_type==''){
			
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('Liability_container',1);
				return false;
			}
			
			else if(RLOSFRAGMENTLOADOPT['Liability_container']=='')
			{
				RLOSFRAGMENTLOADOPT['Liability_container']='N';				
				return true;
			}					
			else if(RLOSFRAGMENTLOADOPT['Liability_container']=='N')
			{	
				Fields_Liability();
			}
		}
		
		else if(pId=='Button_City')
		{ 
			return true;
		}
		else if(pId=='Designation_button' || pId=='DesignationAsPerVisa_button' || pId=='CardDispatchToButton' || pId=='SecNationality_Button' || pId=='BirthCountry_Button' || pId=='ResidentCountry_Button' || pId=='AddressDetails_Button1' || pId=='Nationality_ButtonPartMatch' || pId=='MOL_Nationality_Button' || pId=='CardDetails_bankName_Button' || pId=='EmploymentDetails_Bank_Button' || pId=='FreeZone_Button' )
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
		else if(pId=='CompanyDetails_SearchAloc')
		{ 
			if(getNGValue('compName')!=''){
			return true;
			}
			showAlert('CompanyDetails_SearchAloc','Kindly enter company name');
		}
		//added by nikhil to set default expiry date 30/1
		else if(pId=='cmplx_IncomingDocNew_IncomingDocGrid')
		{
		return true;
		}
		else if(pId=='CompanyDetails')
		{   
			 if (getNGValue('Product_Type') == ''){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('CompanyDetails',1);
				return false;
			}	
			else if(RLOSFRAGMENTLOADOPT['CompanyDetails']=='')
			{   
				RLOSFRAGMENTLOADOPT['CompanyDetails']='N';				
				return true;
			}					
			
			else if(RLOSFRAGMENTLOADOPT['CompanyDetails']=='N' )
			{			
				//var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				//for(var i=0;i<n;i++){
					if(getNGValue("Subproduct_productGrid")=="BTC"){
						setVisible("CompanyDetails_Label8", true);
						setVisible("effecLOB", true);//Effective length of buss
						setVisible("CompanyDetails_Label27", false);
						setVisible("EOW", false);//Emirate of work
						setVisible("CompanyDetails_Label29", false);
						setVisible("headOffice", false);//head office emirate
						setVisible("CompanyDetails_Label28", false);
						setVisible("visaSponsor", false);//visa sponsor
						//break;
					}
						
					else if(getNGValue("Subproduct_productGrid")=="SE" ){
						setVisible("CompanyDetails_Label27", true);
						setVisible("EOW", true);//Emirate of work
						setVisible("CompanyDetails_Label29", true);
						setVisible("headOffice", true);
						setVisible("CompanyDetails_Label28", true);
						setVisible("visaSponsor", true);
						setVisible("CompanyDetails_Label8", false);
						setVisible("effecLOB", false);//Effective length of buss
						//break;
					}
					
					else{
						setVisible("CompanyDetails_Label27", false);
						setVisible("EOW", false);//Emirate of work
						setVisible("CompanyDetails_Label29", false);
						setVisible("headOffice", false);
						setVisible("CompanyDetails_Label28", false);
						setVisible("visaSponsor", false);
						setVisible("CompanyDetails_Label8", false);
						setVisible("effecLOB", false);
					}				
			}	
		}
		
		else if(pId=='EmploymentDetails')
		{
			 if (getNGValue('Product_Type') == ''){		
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('EmploymentDetails',1);
				return false;
			}	
			else if(RLOSFRAGMENTLOADOPT['EmploymentDetails']=='')
			{
				RLOSFRAGMENTLOADOPT['EmploymentDetails']='N';
				//calcLOSInCurrCompany('cmplx_EmploymentDetails_DOJ');	
				return true;
			}
			
		//++ below code already present on 10/10/17 started
			else if(RLOSFRAGMENTLOADOPT['EmploymentDetails']=='N')
			{	
				if(getNGValue('PrimaryProduct')=='Credit Card')
				{
					setVisible('cmplx_EmploymentDetails_EmpContractType',false);
					setVisible('EMploymentDetails_Label71',false);
				}
				else{
					setVisible('cmplx_EmploymentDetails_EmpContractType',true);
					setVisible('EMploymentDetails_Label71',true);
				}
			}
		//++ below code already present on 10/10/17 ended

		}
		
		else if(pId=='EligibilityAndProductInformation' && frameState==0){
			 if (getNGValue('Product_Type') == ''){
			
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('EligibilityAndProductInformation',1);
				return false;
				}
			else if(RLOSFRAGMENTLOADOPT['EligibilityAndProductInformation']==''){
				RLOSFRAGMENTLOADOPT['EligibilityAndProductInformation']='N';
				return true;
			}
			
			else if(RLOSFRAGMENTLOADOPT['EligibilityAndProductInformation']=='N'){
				Fields_Eligibility();
				Fields_ApplicationType_Eligibility();
						//modified by akshay on 9/1/17 for proc 3518
						if(getNGValue("PrimaryProduct")=='Personal Loan'){
							setVisible('ELigibiltyAndProductInfo_Frame2',true);
							setVisible('ELigibiltyAndProductInfo_Frame4',true);
							setVisible('ELigibiltyAndProductInfo_Frame3',false);
							setVisible("ELigibiltyAndProductInfo_Frame5",false);//CC

							setTop('ELigibiltyAndProductInfo_Frame2',parseInt(getTop('ELigibiltyAndProductInfo_Frame6'))+parseInt(getHeight("ELigibiltyAndProductInfo_Frame6"))+10+'px');

							/*if(isVisible('ELigibiltyAndProductInfo_Frame5')){
								setTop('ELigibiltyAndProductInfo_Frame5',parseInt(getTop('ELigibiltyAndProductInfo_Frame4'))+parseInt(getHeight("ELigibiltyAndProductInfo_Frame4"))+5+'px');
							}*/

						}
						else if(getNGValue("PrimaryProduct")=='Credit Card'){
							setVisible("ELigibiltyAndProductInfo_Frame5",true);//CC
							setVisible('ELigibiltyAndProductInfo_Frame2',false);
							setVisible('ELigibiltyAndProductInfo_Frame4',false);
							//if(isVisible('ELigibiltyAndProductInfo_Frame5')){ //this line added by Arun(06/12/17) to set visible CC frame visible in ELigibiltyAndProductInfo
							setTop('ELigibiltyAndProductInfo_Frame5',parseInt(getTop('ELigibiltyAndProductInfo_Frame1'))+10+'px');
							setTop('ELigibiltyAndProductInfo_Frame6',parseInt(getTop('ELigibiltyAndProductInfo_Frame5'))+parseInt(getHeight("ELigibiltyAndProductInfo_Frame5"))+10+'px');

							if(getNGValue("Subproduct_productGrid").toUpperCase().indexOf("-SEC")>-1){
								setVisible('ELigibiltyAndProductInfo_Frame3',true);
								setTop("ELigibiltyAndProductInfo_Frame3",getTop("ELigibiltyAndProductInfo_Frame6")+parseInt(getHeight("ELigibiltyAndProductInfo_Frame4"))+10+'px');

						}		
						else
							setVisible('ELigibiltyAndProductInfo_Frame3',false);	
						
						}
						else{
							setVisible('ELigibiltyAndProductInfo_Frame2',false);
							setVisible('ELigibiltyAndProductInfo_Frame4',false);
							setVisible('ELigibiltyAndProductInfo_Frame5',false);
						}
				//added by saurabh.
					//return true;
				
				//COmmented By AKshay started on 10/10/17-----as this code is not doing anything
				/*}
				if(getNGValue('')=='SEC'){
					setVisible('',true);
				}
				else{
				setVisible('',false);*/
			 //COmmented By AKshay ended on 10/10/17-----as this code is not doing anything
				}
			}		
			
		else if(pId=='Alt_Contact_container' ){
			if(RLOSFRAGMENTLOADOPT['Alt_Contact_container']==''){
				RLOSFRAGMENTLOADOPT['Alt_Contact_container']='N';
				return true;
			}
			
		}
		                //added by Akshay for World Check
                                else if(pId=='WorldCheck_fetch'){
									//changes done by Deepak to save world check details
									var value = 'WorldCheck';
									var field_string_value = getNGValue('FrameName');
									if(field_string_value.indexOf(value)==-1){
									setNGValue('FrameName',field_string_value+'WorldCheck,');
									}
                                    return true;

                                }
                                //ended by Akshay for World Check

		
		else if(pId=='CardDetails_container')
		{
			var rowCount = getLVWRowCount('cmplx_Product_cmplx_ProductGrid');
			 if (rowCount==0){
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('CardDetails_container',1);
				return false;
			}	
			if(RLOSFRAGMENTLOADOPT['CardDetails_container']==''){
				RLOSFRAGMENTLOADOPT['CardDetails_container']='N';
				return true;
			}
			
			else if(RLOSFRAGMENTLOADOPT['CardDetails_container']=='N' ){
				
			 if (getNGValue("PrimaryProduct") == 'Personal Loan'){
					setVisible("CardDetails_Label7", true);
					setVisible("cmplx_CardDetails_statCycle", true);
									
				}	
				
				if(getNGValue("Subproduct_productGrid")=='BTC' || getNGValue("Subproduct_productGrid")=='SE')
				{
					setVisible("CardDetails_Label3", true);
					setVisible("cmplx_CardDetails_CompanyEmbossingName", true);
					setLeft("CardDetails_Label5", "552px");
					setLeft("cmplx_CardDetails_SuppCardReq", "552px");
					
				}
				if(getNGValue("LoanType_Primary")=='Islamic'){
					setVisible("CardDetails_Label2", true);
					setVisible("cmplx_CardDetails_CharityOrg", true);
					setVisible("CardDetails_Label4", true);
					setVisible("cmplx_CardDetails_CharityAmount", true);
					setLeft("CardDetails_Label5", "1059px");
					setLeft("cmplx_CardDetails_SuppCardReq", "1059px");				
				}	

			}	
		}
		
		else if(pId=='FetchWorldCheck_SE'){
		return true;
		}
		/*else if(pId=='CheckEligibility_SE'){
		return true;
		}*/
		
		// below code added by Arun (05/12/17) for the CC-SE case
		else if(pId=='CheckEligibility_SE'){
		//condition added by saurabh on 17th Nov 17.
		//change by saurabh on 13th dec
		var flag = false;
			if(getNGValue('Is_Financial_Summary')!='Y'){
				if(getNGValue('cmplx_Customer_NTB')==false){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
				}
				else if(getNGValue('aecb_call_status')=='' && getNGValue('cmplx_Customer_NEP')!='NEWC'){
				showAlert('Liability_container',alerts_String_Map['VAL106']);
				return false;
				}
				else{
				flag = true;
				}
			}
			else{flag = true;}
			/*else if(getNGValue('Is_employment_save')!='Y' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='BTC' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='SE'){
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}*/
			if(flag){
			//change by saurabh on 30th Nov for FSD 2.7
			var counter = getNGValue('reEligbility_init_counter').split(';')[0];
			var eligFlag = getNGValue('reEligbility_init_counter').split(';')[1];
			if(pId=='ELigibiltyAndProductInfo_Button1'){
				if(counter==(parseInt(alerts_String_Map['VAL323'])-1).toString()){
				setNGValueCustom('reEligbility_init_counter',alerts_String_Map['VAL323']+';'+eligFlag);
				setEnabledCustom('ELigibiltyAndProductInfo_Button1',false);
				}
				else if(parseInt(counter)<(parseInt(alerts_String_Map['VAL323'])-1)){
				setNGValueCustom('reEligbility_init_counter',(parseInt(counter)+1).toString()+';'+eligFlag);
				}
			}
			else if(pId=='CheckEligibility_SE'){
				setNGValueCustom('reEligbility_init_counter',counter+';Y');
			}
			
			}
			return true;
		}
		// above code added by Arun (05/12/17) for the CC-SE case
		
		else if(pId=='CC_Loan_container' ){
			 if (getNGValue('Product_Type') == ''){
			
				showAlert('cmplx_Product_cmplx_ProductGrid',alerts_String_Map['VAL100']);
				setNGFrameState('CC_Loan_container',1);
				return false;
			}	
			if(RLOSFRAGMENTLOADOPT['CC_Loan_container']==''){
				RLOSFRAGMENTLOADOPT['CC_Loan_container']='N';
				return true;
			}	
		}
		
		else if(pId=='ReadFromCard')
		{
			//setNGValueCustom("cmplx_Customer_IsGenuine", true);
			var readstatus = SetValueCustomer_ReadCard();	
			EIDA_Visa_date();//Deepak Need to check it's  use
		
		}
			
		else if(pId=='cmplx_Customer_CardNotAvailable'){	
				if(getNGValue("cmplx_Customer_CardNotAvailable")==true)
				{
					setBlank_Customer();
					setEnabledCustom("ReadFromCard",false);
					//commented by nikhi for Sprint-1
					//setVisible("Customer_Frame2", true);
					com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "620px");
					com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "650px");
					setLockedCustom("Validate_OTP_Btn", false);
					setLockedCustom("OTP_No", false);
					setLockedCustom("FetchDetails", false);
					setNGValueCustom("cmplx_Customer_IsGenuine", false);	
					setLockedCustom("cmplx_Customer_EmiratesID",false);
				//	setLockedCustom("cmplx_Customer_CIFNO",false);
					setLockedCustom("cmplx_Customer_FIrstNAme",false);
					setLockedCustom("cmplx_Customer_MiddleName",false);
					setLockedCustom("cmplx_Customer_LAstNAme",false);
					setLockedCustom("cmplx_Customer_Nationality",false);
					setLockedCustom("cmplx_Customer_MobNo",false);
					setLockedCustom("cmplx_Customer_DOb",false);
					//added by nikhil 29/11
					setEnabledCustom("cmplx_Customer_DOb",true);
					//setEnabledCustom("cmplx_Customer_DOb",true);
					setEnabledCustom("cmplx_Customer_card_id_available",false);
					//added by Akshay on 11/9/2017 for disabling CIFID when card not avail is selected
					setNGValueCustom("cmplx_Customer_card_id_available",false);
					setLockedCustom("cmplx_Customer_CIFNO",true);
					//ended by Akshay on 11/9/2017 for disabling CIFID when card not avail is selected
					setLockedCustom("cmplx_Customer_PAssportNo",false);
					setVisible('Customer_Label55',true);
					setVisible('cmplx_Customer_marsoomID',true);
					setLockedCustom('cmplx_Customer_marsoomID',false);
					setEnabledCustom('cmplx_Customer_NEP',false);
					setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
					setLockedCustom("cmplx_Customer_IdIssueDate",false);
					//setEnabledCustom("cmplx_Customer_SecNAtionApplicable",true);
					setLockedCustom("cmplx_Customer_VIsaExpiry",false);
			setEnabledCustom("cmplx_Customer_VIsaExpiry",true);
				}
				else 
				{
					setEnabledCustom("ReadFromCard",true);
					setBlank_Customer();
					SetDisableCustomer();
					setEnabledCustom("FetchDetails", false);
					//setNGValueCustom("cmplx_Customer_IsGenuine", false);
						setLockedCustom("cmplx_Customer_EmiratesID",true);
				//	setLockedCustom("cmplx_Customer_CIFNO",false);
					setLockedCustom("cmplx_Customer_FIrstNAme",true);
					setLockedCustom("cmplx_Customer_MiddleName",true);
					setLockedCustom("cmplx_Customer_LAstNAme",true);
					setLockedCustom("cmplx_Customer_Nationality",true);
					setLockedCustom("cmplx_Customer_MobNo",true);
					setLockedCustom("cmplx_Customer_DOb",true);
					//setEnabledCustom("cmplx_Customer_DOb",false);
					setNGValueCustom("cmplx_Customer_ResidentNonResident", "--Select--");
					//commented by nikhi for Sprint-1
					//setVisible("Customer_Frame2", false);
					com.newgen.omniforms.formviewer.setHeight("Customer_Frame1", "505px");
					com.newgen.omniforms.formviewer.setHeight("CustomerDetails", "530px");
					setLockedCustom("cmplx_Customer_EmiratesID",true);
					setLockedCustom("cmplx_Customer_CIFNO",true);
					setEnabledCustom("cmplx_Customer_NEP",true);
					setVisible('cmplx_Customer_marsoomID',false);
					setVisible('Customer_Label55',false);
					setEnabledCustom("cmplx_Customer_SecNAtionApplicable",false);
					setEnabledCustom("cmplx_Customer_card_id_available",true);
				}
			}
		
			
		
		else if(pId=='cmplx_Customer_card_id_available')
        {    
			setBlank_Customer();
			if(getNGValue('cmplx_Customer_card_id_available')==true){
					setLockedCustom("cmplx_Customer_CIFNO",false);
					setLockedCustom("cmplx_Customer_IdIssueDate",false);
					setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
					setLockedCustom("cmplx_Customer_EmiratesID",true);
					setLockedCustom("cmplx_Customer_FIrstNAme",true);
					setLockedCustom("cmplx_Customer_MiddleName",true);
					setLockedCustom("cmplx_Customer_LAstNAme",true);
					setLockedCustom("cmplx_Customer_Nationality",true);
					setLockedCustom("cmplx_Customer_MobNo",true);
					setLockedCustom("cmplx_Customer_PAssportNo",true);
					setLockedCustom("cmplx_Customer_DOb",true);
					setEnabledCustom("cmplx_Customer_CIFNO",true);
					setEnabledCustom("ReadFromCard",false);
					setEnabledCustom('cmplx_Customer_NEP',false);
					setEnabledCustom("cmplx_Customer_CardNotAvailable",false);
				}
						
					
			else{
					setLockedCustom("cmplx_Customer_CIFNO",true);
					setNGValueCustom("cmplx_Customer_CIFNO","");
					setLockedCustom("cmplx_Customer_EmiratesID",false);
					setLockedCustom("cmplx_Customer_FIrstNAme",false);
					setLockedCustom("cmplx_Customer_MiddleName",false);
					setLockedCustom("cmplx_Customer_LAstNAme",false);
					setLockedCustom("cmplx_Customer_Nationality",false);
					setLockedCustom("cmplx_Customer_MobNo",false);
					setLockedCustom("cmplx_Customer_PAssportNo",false);
					setLockedCustom("cmplx_Customer_DOb",false);
					setEnabledCustom("ReadFromCard",true);
					setEnabledCustom('cmplx_Customer_NEP',true);
					setEnabledCustom("cmplx_Customer_CardNotAvailable",true);
				}
				setNGValueCustom(pId,getNGValue(pId));
        }
		
		
		else if(pId=='FetchDetails')
		{
			//alert('new alert---');
			setLockedCustom("cmplx_Customer_VIsaExpiry",false);
			setEnabledCustom("cmplx_Customer_VIsaExpiry",true);
			if(checkMandatory_FetchDetails())
			{
				
				setLockedCustom("ReadFromCard", true);
				setLockedCustom("FetchDetails", true);
				setLockedCustom("cmplx_Customer_PassPortExpiry",false);
				//setLockedCustom("cmplx_Customer_VIsaExpiry",false);
				return true;
			}
			
		}
		
		else if(pId=='IncomeDetails_FinacialSummarySelf' || pId=='IncomeDetails_Button1')
		{
					//24/07/2017 Liabilities should be Fetched First till it gets the status as Success
			if((getNGValue('aecb_call_status')=='Fail' || getNGValue('aecb_call_status') == '' || getNGValue('aecb_call_status') == null) && getNGValue('cmplx_Customer_NEP')!='NEWC')
				{
					var height=parseInt(getHeight('Liability_container'));
						if(height > 30){
							showAlert('Liability_New_fetchLiabilities',alerts_String_Map['VAL106']);
							return false;
						}
						else{
						 setNGFrameState('Liability_container',0);
						 showAlert('Liability_New_fetchLiabilities',alerts_String_Map['VAL106']);
						return false;
						}
					return true;
				}
					//24/07/2017 Liabilities should be Fetched First till it gets the status as Success
				
			var request_name = "";
			// below code done to find opertaion names of financial summary on 29th Dec by disha
			/*
			if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SAL' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'SALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SE' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='BTC' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='IM' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,SALDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='LI' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,SALDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='LI' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PA' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'SALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PA' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PU' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'SALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PU' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'TRANSUM,AVGBALDET,RETURNDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='EXP' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,SALDET';//TRANSUM,AVGBALDET,RETURNDET,LIENDET,SIDET
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SEC' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,LIENDET,SALDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='SEC' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'RETURNDET,LIENDET,AVGBALDET,TRANSUM';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PULI' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,SALDET';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='PULI' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Self Employed'){
				request_name = 'RETURNDET,LIENDET,AVGBALDET,TRANSUM';
			}
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)=='NAT' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)=='Salaried'){
				request_name = 'RETURNDET,SALDET';//'AVGBALDET,TRANSUM,RETURNDET';
			}
			else{
				request_name = 'RETURNDET';
			}
			*/
					
				//added by akshay on 1/2/18
				var company_cif=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",1,3);//changed by akshay for proc 12543
				var param_json=('{"cmplx_Customer_CIFNo":"'+getNGValue('cmplx_Customer_CIFNO')+'","sub_product":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)+'","emp_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)+'","company_cif":"'+company_cif+'","application_type":"'+getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4)+'"}');
				// above code done to find opertaion names of financial summary on 29th Dec by disha
				var wi_name = window.parent.pid;
				var activityName = window.parent.stractivityName;
				//alert ('wi_name '+ wi_name);
				//alert ('activityName '+ activityName);
				setNGValueCustom('cmplx_IncomeDetails_AvgCredTurnover','');
				setNGValueCustom('cmplx_IncomeDetails_CredTurnover','');
				
				setNGValueCustom('cmplx_IncomeDetails_AvgBal','');
				var url='/webdesktop/custom/CustomJSP/integration.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName+'&sessionId='+window.parent.sessionId;
				//window.open(url, "", "width=500,height=300");
				//Deepak new code added to open single window start
				if(popup_win && !popup_win.closed){
					popup_win.focus();
				}
				else{
					popup_win = window.open_(url, "", "scrollbars=yes,resizable=yes,width=620px,height=700px"); //changed by aman to send the code to java
				}
				//Deepak new code added to open single window END
				
				
				setNGValueCustom('Is_Financial_Summary','Y');
			return true;
		
		}
		
		
		else if(pId=='cmplx_Customer_SecNAtionApplicable_1')
		{
			setLockedCustom('SecNationality_Button',true);
			setNGValueCustom('cmplx_Customer_SecNationality',"");

		}
		
		else if(pId=='cmplx_Customer_SecNAtionApplicable_0'){
			setLockedCustom('SecNationality_Button',false);
			//setEnabledCustom('cmplx_Customer_SecNationality',true);
        }
		
		else if(pId=='cmplx_IncomeDetails_Accomodation_1')
		{
			setLockedCustom('cmplx_IncomeDetails_AccomodationValue',true);
			setNGValueCustom('cmplx_IncomeDetails_AccomodationValue',"");

		}
		else if(pId=='cmplx_IncomeDetails_Accomodation_0'){
			setLockedCustom('cmplx_IncomeDetails_AccomodationValue',false);
        }
		
		else if(pId=='IncomeDetails_Add'){
			if(checkMandatory_IncomeGrid())
				return true;
		}		
			
		else if(pId=='IncomeDetails_Modify'){
			if(checkMandatory_IncomeGrid())
				return true;
		}	
		else if(pId=='IncomeDetails_Delete'){
			return true;
		}
		
		else if(pId=='cmplx_Customer_NTB'){
			if(getNGValue("cmplx_Customer_NTB")==true)
						SetEnableCustomer();
			else if(getNGValue("cmplx_Customer_NTB")==false)
						SetDisableCustomer();
		}
			
		else if(pId=='ReferenceDetails_Reference_Add'){
			if(checkMandatory_ReferenceGrid('add')){
				if(validate_ReferenceGrid()){
					return true;
				}
					
			}
				
		}			
				
		else if(pId=='ReferenceDetails_Reference__modify'){
			if(checkMandatory_ReferenceGrid('modify'))
				return true;
		}		
			
		else if(pId=='ReferenceDetails_Reference_delete'){
			return true;
		}	
		else if(pId=='grid_details_add'){
			return true;
		}
		
		else if(pId=='Send_OTP_Btn' ){
			if(Send_OTP_checkMandatory())				
			{
				setNGValueCustom("Req_Type_Value","GenerateOTP");
				return true;
			}
		}
		
		else if(pId=='DecisionHistory_SendSMS'){
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			var product='';
				for(var i=0;i<n;i++){
					product = getLVWAT("cmplx_Product_cmplx_ProductGrid",i,1) + ", "+product;
				}
			var sms_msg_text = "Dear Customer, your request no "+window.parent.pid+" for a RAKBANK "+product+" is received and under process. regards, RAKBANK";
			setNGValueCustom("SMS_text_msg",sms_msg_text);
			return true;
		}
		
		else if( pId=='Validate_OTP_Btn' ){
			if(Validate_OTP_checkMandatory())
			{
				setNGValueCustom("Req_Type_Value","ValidateOTP");
				return true;
			}
		}
		
		//changed by akshay on 27/2/18 for DROP 4
		else if(pId=='DecisionHistory_Button3')
		
		{
			if(getLVWAT('cmplx_DecisionHistory_MultipleApplicantsGrid',getNGListIndex('cmplx_DecisionHistory_MultipleApplicantsGrid'),0)=='Primary'){
				if(checkMandatory_CreateCIF('PRIMARY',pId)){
					return true;
				}
				}
			else if(getLVWAT('cmplx_DecisionHistory_MultipleApplicantsGrid',getNGListIndex('cmplx_DecisionHistory_MultipleApplicantsGrid'),0)=='Guarantor'){
				if(checkMandatory_CreateCIF('GUARANTOR',pId)){
	
		
		
		
					return true;
		}
		}
			return false;
		}
		
		//below code added by akshay on 15/2/18
		else if(pId=='cmplx_DecisionHistory_MultipleApplicantsGrid')
		{	
			/*if(getLVWAT(pId,getNGListIndex(pId),3)==''){
		
				setVisible('DecisionHistory_Button3',true);
		
		}
			else{
				setVisible('DecisionHistory_Button3',false);
		}*/
		
			return true;
		}
		
			//added by akshay on 23/4/18
		else if(pId=='cmplx_GuarantorDetails_cmplx_GuarantorGrid')
		{
			/*if(getLVWAT(pId,getNGListIndex(pId),1)!=''){
				setLockedCustom('ReadFromCIF',true);
			}
			else{*/
				setLockedCustom('ReadFromCIF',true);
			//}
		}	

//Added by aman 08062018
	else if( pId=='SecNationality_Button'|| pId=='CardDispatchToButton'|| pId=='Designation_button'|| pId=='AddressDetails_Button1'|| pId=='EmploymentDetails_Bank_Button'|| pId=='DesignationAsPerVisa_button'|| pId=='FreeZone_Button')
	{
		return true;
	}

	//Added by aman 08062018
	
		else if(pId=='Liability_New_fetchLiabilities'){
			if(AccountSummary_checkMandatory())
			{
				var request_name = "";
				//changes doen by nikhil to remove double spacing
				var middle_name="";
				if(getNGValue('cmplx_Customer_MiddleName')!='')
				{
					middle_name=' '+getNGValue('cmplx_Customer_MiddleName');
				}
				var full_name = getNGValue('cmplx_Customer_FIrstNAme')+middle_name+' '+getNGValue('cmplx_Customer_LAstNAme');
				//var full_name = getNGValue('cmplx_Customer_FIrstNAme')+' '+getNGValue('cmplx_Customer_MiddleName')+' '+getNGValue('cmplx_Customer_LAstNAme');
				var TxnAmount=replaceAll(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,3),',','');//.replace(',','');
				var NoOfInstallments=replaceAll(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,7),',','');
				var prod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1);
				var subprod = getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
				var gender = getNGValue('cmplx_Customer_gender');
				// changes done by disha for drop-4 on 26-02-2018 Initiation || Pipeline and Rejected external applications are not getting reflected in the Pipeline grid OverridePeriod values changed to 0 from 1
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
					for(var i=1; i<comp_row;i++){
						if(com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,2)!='Primary'){
							if (i==1){
								company_cif=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,3);
								trade_comp_name=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,4);
								trade_lic_no=com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,6);
							}
							else{
								company_cif=company_cif+","+com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,3);
								trade_comp_name=trade_comp_name+","+com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,4);
								trade_lic_no=trade_lic_no=trade_lic_no+","+com.newgen.omniforms.formviewer.getLVWAT("cmplx_CompanyDetails_cmplx_CompanyGrid",i,6);
							}
						}
					}
					
					if(isVisible('GuarantorDetails_Frame1')==true){	
						var guarantor_CIF=	getLVWAT('cmplx_GuarantorDetails_cmplx_GuarantorGrid',0,1);
					}
					
					if((getNGValue('cmplx_Customer_NTB')==false) || (getNGValue('cmplx_Customer_NTB')==true && ((company_cif!='' && company_cif!=null) || guarantor_CIF!='')))
					{
						request_name = 'InternalExposure,ExternalExposure';
					}
					else{
						request_name = 'ExternalExposure';
					}
					//Changes done for company AECB end
					//Changes done by aman for AECB Start
					// acc_id replaced with Account_Number Deepak 12Nov2017
					var param_json=('{"cmplx_customer_FirstNAme":"'+getNGValue('cmplx_Customer_FIrstNAme')+'","cmplx_Customer_LAstNAme":"'+getNGValue('cmplx_Customer_LAstNAme')+'","cmplx_Customer_DOb":"'+getNGValue('cmplx_Customer_DOb')+'","cmplx_Customer_MobNo":"'+getNGValue('cmplx_Customer_MobNo')+'","cmplx_Customer_gender":"'+gender+'","OverridePeriod":"'+OverridePeriod+'","wi_name":"'+wi_name_aecb+'","cmplx_Customer_Nationality":"'+getNGValue('cmplx_Customer_Nationality')+'","cmplx_Customer_PAssportNo":"'+getNGValue('cmplx_Customer_PAssportNo')+'","TxnAmount":"'+TxnAmount+'","NoOfInstallments":"'+NoOfInstallments+'","cmplx_Customer_Passport2":"'+getNGValue('cmplx_Customer_Passport2')+'","cmplx_Customer_Passport3":"'+getNGValue('cmplx_Customer_Passport3')+'","cmplx_Customer_PAssport4":"'+getNGValue('cmplx_Customer_PAssport4')+'","cmplx_Username":"deepak","cmplx_Customer_CIFNO":"'+getNGValue('cmplx_Customer_CIFNO')+'","cif":"'+company_cif+'","trade_comp_name":"'+trade_comp_name+'","trade_lic_no":"'+trade_lic_no+'","account_no":"'+getNGValue('Account_Number')+'","cmplx_Customer_EmiratesID":"'+getNGValue('cmplx_Customer_EmiratesID')+'","cmplx_EligibilityAndProductInfo_NumberOfInstallment":"5","cmplx_Customer_short_name":"'+getNGValue('cmplx_Customer_short_name')+'","full_name":"'+full_name+'","cmplx_Customer_NTB":"'+getNGValue('cmplx_Customer_NTB')+'"}');
				//Changes done by aman for AECB end
				//alert(param_json);
				param_json = encodeURIComponent(param_json);
				var url='/webdesktop/custom/CustomJSP/integration.jsp?request_name=' + request_name + '&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod+'&sessionId='+window.parent.sessionId;
				
				//Deepak new code added to open single window start
				if(popup_win && !popup_win.closed){
					popup_win.focus();
				}
				else{
					popup_win = window.open_(url,'', "width=650px,height=650px");	
				}
				//Deepak new code added to open single window END
				if(typeof popup_win==='object'){
					var ReportURL=popup_win['ReportUrl'];
					setNGValueCustom('cmplx_Liability_New_Aecb_Report_Url',ReportURL);
					 status=popup_win['aecb_call_status'];
				}
				else{
					status=popup_win;
				}
				//alert('status'+status);
				setNGValueCustom('aecb_call_status',status);
				//document.getElementById('Liability_New_IFrame_internal').src='/webdesktop/resources/scripts/internal_liability.jsp';
				/*if(getNGValue('cmplx_Customer_NTB')==false)
				{
					var url='/webdesktop/resources/scripts/integration.jsp?request_name=CollectionsSummary&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod;
					window.showModalDialog(url, "", "width=500,height=300");
					//window.open(url, "", "width=500,height=300");
					document.getElementById('Liability_New_IFrame_internal').src='/webdesktop/resources/scripts/internal_liability.jsp';
				}
				//cardnumber 12/07/2017
				if(getNGValue('cmplx_Customer_NTB')==false)
				{
					var url='/webdesktop/resources/scripts/integration.jsp?request_name=CARD_INSTALLMENT_DETAILS&param_json=' + param_json + '&wi_name=' + wi_name + '&activityName=' +activityName + '&prod=' + prod + '&subprod=' + subprod;
					window.showModalDialog(url, "", "width=500,height=300");
					//window.open(url, "", "width=500,height=300");
					document.getElementById('Liability_New_IFrame_internal').src='/webdesktop/resources/scripts/internal_liability.jsp';
					document.getElementById('Liability_New_IFrame_external').src='/webdesktop/resources/scripts/External_Liability.jsp';
				}
				
				//cardnumber 12/07/2017
				document.getElementById('Liability_New_IFrame_internal').src='/webdesktop/resources/scripts/internal_liability.jsp';
				document.getElementById('Liability_New_IFrame_external').src='/webdesktop/resources/scripts/External_Liability.jsp';
				//document.getElementById("Liability_New_IFrame_internal").style.top="100px";
				return true;
				*/
				return true;//changes by nikhil for product dedupe functionality wotking
			}
			
}
	
		//Added Code for Customer Save Check
		
		else if(pId=='ReadFromCIF'){
			if(getNGValue('cmplx_Customer_IscustomerSave')!='Y'){
				showAlert('cmplx_Customer_IscustomerSave',alerts_String_Map['VAL044']);
				return false;
			}	
			else if(getNGValue('CompanyDetails_CIF')==''){//name changed by akshay on 12/3/18 as decision was not loading
				showAlert('CompanyDetails_CIF',alerts_String_Map['VAL033']);
				return false;
			}	
			flag_ReadFromCIF=true;
			return true;
		}
		
		else if(pId=='Customer_Button1'){
			setEnabledCustom('Customer_Button1',false);
			return true;
		}
		
		else if(pId=='cmplx_FATCA_W8Form')
		{
			if(getNGValue('cmplx_FATCA_W9Form')==true)
			setNGValueCustom('cmplx_FATCA_W8Form','false');
			return false;
		}
		
		else if(pId=='cmplx_FATCA_W9Form')
		{
			if(getNGValue('cmplx_FATCA_W8Form')==true)
			setNGValueCustom('cmplx_FATCA_W9Form','false');
			return false;
		}
		
		
		
		//Added code for Customer Save Check		
		else if(pId=='GuarantorDetails_add'){
			if(getNGValue('cmplx_Customer_IscustomerSave')!='Y'){
				showAlert('cmplx_Customer_IscustomerSave',alerts_String_Map['VAL044']);
				return false;
			}	
		if(getNGValue('age_gua')<=21)
		{
			showAlert('age_gua',alerts_String_Map['PL409'])
			return false;
		}
    		else if(checkMandatory_GuarantorGrid()){
    			if(validate_GuarantorGrid()){
    				flag_ReadFromCIF=true;
					return true;
				}
			 }
    	}
		
		else if(pId=='GuarantorDetails_modify'){
		if(getNGValue('age_gua')<=21)
		{
			showAlert('age_gua',alerts_String_Map['PL409'])
			return false;
		}
			if(checkMandatory_GuarantorGrid()){
				return true;
			}
		}		

		else if( pId=='GuarantorDetails_delete' ){
			if(checkForApplicantTypeInGrids('Guarantor'))
				return true;			
		}
		
		 else if(pId=='Add'){
			if(checkMandatory_ProductGrid()){
					if(Product_add_validate()){
						if(getNGValue('ReqProd')=='Personal Loan')
						{
							if(getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')<21){
								setVisible("GuarantorDetails", true);
								setTop("GuarantorDetails",parseInt(getTop('ProductDetailsLoader'))+parseInt(getHeight("ProductDetailsLoader"))+5+"px");
								setTop("Incomedetails",parseInt(getTop('GuarantorDetails'))+parseInt(getHeight("GuarantorDetails"))+5+"px");
							}
							else
							{
								setVisible("GuarantorDetails", false);
							}
							//setSheetVisible("ParentTab", 1, false);
							setVisible('CardDetails_Label3',false);
							setVisible('cmplx_CardDetails_CompanyEmbossingName',false);
							setVisible("DecisionHistory_Button3", true);
						}
						if(getNGValue('ReqProd')=='Credit Card')
						{
							setSheetVisible("ParentTab", 7, true);
							//setSheetVisible("ParentTab", 1, true);
							setVisible('CardDetails_Label3',true);
							setVisible('cmplx_CardDetails_CompanyEmbossingName',true);
							//below code added by nikhil drop-4
							if(getNGValue('subProd')=='LI' && getNGValue('cmplx_Customer_NTB')==true)
							{
								showAlert('subProd',alerts_String_Map['VAL348']);
								return false;
							}
						}
						
						if(getNGValue('EmpType')=='Salaried' || getNGValue('EmpType')=='Salaried Pensioner' || getNGValue('EmpType')=='Pensioner'){								
							setSheetVisible("ParentTab", 1, false);
							setSheetVisible("ParentTab", 3, true);
						}	
						else if(getNGValue('EmpType')=='Self Employed'){
							setSheetVisible("ParentTab", 1, true);
							setSheetVisible("ParentTab", 3, false);
						}
						
						if(getNGValue('AppType')!='RESC' && getNGValue('AppType')!='RESN' && getNGValue('AppType')!='RESR'){
						setVisible("Liability_New_Label25", false);
						setVisible("cmplx_Liability_New_paid_installments", false);
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
			if(getLVWRowCount("cmplx_Product_cmplx_ProductGrid")==0){
					showAlert('','No row present in table');	
					return false;
			}
			if(checkMandatory_ProductGrid()){
				if(Product_modify_validate()){
					
					if(getNGValue('ReqProd')=='Personal Loan')
						{
							if(getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')<21){
								setVisible("GuarantorDetails", true);
								setTop("Incomedetails",parseInt(getTop('GuarantorDetails'))+parseInt(getHeight("GuarantorDetails"))+5+"px");
							}
							setSheetVisible("ParentTab", 1, false);
							setSheetVisible("ParentTab", 7, false);
							setVisible('CardDetails_Label3',false);
							setVisible('cmplx_CardDetails_CompanyEmbossingName',false);
							setVisible("DecisionHistory_Button3", true);
						}
					if(getNGValue('ReqProd')=='Credit Card')
					{
						setSheetVisible("ParentTab", 7, true);
						//setSheetVisible("ParentTab", 1, true);
						setVisible('CardDetails_Label3',true);
						setVisible('cmplx_CardDetails_CompanyEmbossingName',true);
						//below code added by nikhil drop-4
						if(getNGValue('subProd')=='LI' && getNGValue('cmplx_Customer_NTB')==true)
							{
								showAlert('subProd',alerts_String_Map['VAL348']);
								return false;
							}
					}
					if(getNGValue('EmpType')=='Salaried'  || getNGValue('EmpType')=='Salaried Pensioner'  || getNGValue('EmpType')=='Pensioner'){								
						setSheetVisible("ParentTab", 1, false);
						setSheetVisible("ParentTab", 3, true);
					}	
					else if(getNGValue('EmpType')=='Self Employed'){
						setSheetVisible("ParentTab", 1, true);
						setSheetVisible("ParentTab", 3, false);
					}
						if(getNGValue('AppType')!='RESC' && getNGValue('AppType')!='RESN' && getNGValue('AppType')!='RESR'){
						setVisible("Liability_New_Label25", false);
						setVisible("cmplx_Liability_New_paid_installments", false);
					}
					else{
					setVisible("Liability_New_Label25", true);
					setVisible("cmplx_Liability_New_paid_installments", true);
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
			if(getLVWRowCount("cmplx_Product_cmplx_ProductGrid")==0){
					showAlert('','No row present in table');	
					return false;
			}
			return true;
		}
		
		else if(pId=='cmplx_Product_cmplx_ProductGrid')
		{
			/*if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),1)=='Personal Loan'){
				setVisible('Product_Label3',true);
				setVisible('Product_Label5',true);
				setVisible('ReqTenor',true);				
				setVisible('Scheme',true);
				setVisible('CardProd',false);
				setVisible('Product_Label6',false);

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),1)=='Credit Card'){
				setVisible('CardProd',true);
				setVisible('Product_Label6',true);

			}
			//below code added by Arun (06/12/17) for user cannot modify the added data in the product grid
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='IM'){
				setVisible('Product_Label5',true);
				setVisible('ReqTenor',true);
				setVisible('Product_Label6',true);
				setVisible("CardProd",true);

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='SAL'){
				setVisible('Product_Label6',true);
				setVisible("CardProd",true);

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='BPA'){
				setVisible('Product_Label6',true);
				setVisible("CardProd",true);

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='PULI'){
				setVisible("Product_Label6",true);//Arun (06/12/17) to show this field
				setVisible("CardProd",true);//Arun (06/12/17) to show this field
				setVisible("Product_Label16",false);//Arun (06/12/17) to hide this field
				setVisible("LimitAcc",false);//Arun (06/12/17) to hide this field

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='SE'){
			
				setVisible('Product_Label6',true);//Arun 18/12/17

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='SEC'){
				setVisible('Product_Label8',true);
				setVisible('FDAmount',true);
				setVisible('Product_Label6',true);//Arun 18/12/17
				setVisible('CardProd',true);//Arun 18/12/17

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='LI'){
				setVisible('Product_Label8',false);
				setVisible('FDAmount',false);
				setVisible('Product_Label6',false);//Arun 18/12/17
				setVisible('CardProd',false);//Arun 18/12/17

			}
			
			else if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2)=='PU'){
				setVisible("Product_Label6",false);//Arun (06/12/17) to hide this field
				setVisible("CardProd",false);//Arun (06/12/17) to hide this field
				setVisible("Product_Label7",true);
				setVisible("LastPermanentLimit",true);
				setVisible("Product_Label9",true);
				setVisible("LastTemporaryLimit",true);
				setVisible("Product_Label16",false);//Arun (06/12/17) to hide this field
				setVisible("LimitAcc",false);//Arun (06/12/17) to hide this field
				setVisible("CardDetails_container",false);//Arun (06/12/17) to hide this fragment
			}
			//above code added by Arun (06/12/17) for user cannot modify the added data in the product grid
			else{
				setVisible('Product_Label8',true);
				setVisible('FDAmount',true);
					setVisible('Product_Label6',true);
					setVisible('Product_Label3',false);
					setVisible('Scheme',false);
					setVisible('Product_Label5',false);
					setVisible('ReqTenor',false);
					//if(getLVWAT('cmplx_Product_cmplx_ProductGrid',
				//added by akshay on 2/1/18	
				if(getLVWAT(pId,0,1)=='Credit Card'){		
					setVisible('Product_Label6',true);	
				}
				else{
					setVisible('Product_Label6',false);	
				}	
					setVisible("CardProd", true);
					if(getLVWAT('cmplx_Product_cmplx_ProductGrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),2).indexOf('Limit')>-1){
						setVisible("LimitAcc", true);
						setVisible("LimitExpiryDate", true);
						//setVisible("typeReq", true);
						setVisible("Product_Label15", true);
						setVisible("Product_Label16", true);
						setVisible("Product_Label18", true);
					}
				}--Commented by akshay on 3/1/18 as code shifted to java*/
				return true;
		}			
			
		else if(pId=='Button9'){
			return true;
			}
		
		/*else if(pId=='subProd'){
			return true;
		}*/
		
		else if(pId=='CompanyDetails_CheckBox4'){
			if(getNGValue(pId)==true){
				setLockedCustom('CompanyDetails_CIF',false);//name changed by akshay on 12/3/18 as decision tab was not loading(ame component twice error)
				setLockedCustom('CompanyDetails_Button3',false);
				setLockedCustom('CompanyDetails_SearchAloc',true);
				setLockedCustom('appType',true);
				setLockedCustom('compIndus',true);
				setLockedCustom('tlNo',true);
				setLockedCustom('TLExpiry',true);
				setLockedCustom('indusSector',true);
				setLockedCustom('indusMAcro',true);
				setLockedCustom('indusMicro',true);
				setLockedCustom('desig',true);
				setLockedCustom('desigVisa',true);
				setLockedCustom('legalEntity',true);
				setLockedCustom('estbDate',true);
				setLockedCustom('lob',true);
				setLockedCustom('bvr',true);
				setLockedCustom('POA',true);
				setLockedCustom('exportToIran',true);
				setLockedCustom('grt40',true);
				setLockedCustom('compName',true);
				setNGValueCustom('indusSector','');
				setNGValueCustom('indusMAcro','');
				setNGValueCustom('indusMicro','');
				setNGValueCustom('compName','');
			}
			else{
			setLockedCustom('compName',false);
				setLockedCustom('CompanyDetails_CIF',true);//name changed by akshay on 12/3/18 as decision tab was not loading(ame component twice error)
				//setEnabledCustom('cif',true);//commented by akshay----no need
				setLockedCustom('CompanyDetails_Button3',true);
				setLockedCustom('CompanyDetails_SearchAloc',false);
				setLockedCustom('appType',false);
				setLockedCustom('compIndus',false);
				setLockedCustom('tlNo',false);
				setLockedCustom('TLExpiry',false);
				setLockedCustom('indusSector',false);
				setLockedCustom('indusMAcro',false);
				setLockedCustom('indusMicro',false);
				setLockedCustom('desig',false);
				setLockedCustom('desigVisa',false);
				setLockedCustom('legalEntity',false);
				setLockedCustom('estbDate',false);
				setLockedCustom('lob',false);
				setLockedCustom('bvr',false);
				setLockedCustom('POA',false);
				setLockedCustom('exportToIran',false);
				setLockedCustom('grt40',false);
				setNGValueCustom('CompanyDetails_CIF', '');//name changed by akshay on 12/3/18 as decision tab was not loading(ame component twice error)
				
			}
		}
		
		else if(pId=='CompanyDetails_Button3')
		{
			if(getNGValue('CompanyDetails_CIF')==''){//name changed by akshay on 12/3/18 as decision tab was not loading(ame component twice error)
				showAlert('CompanyDetails_CIF',alerts_String_Map['VAL032']);
				return false;
			}	
			flag_Company_FetchDetails=true;
			return true;
		}
		
		//added by yash for RLOS FSD
		else if(pId=='NotepadDetails_Add'){
		if(checkMandatory(RLOS_Notepad)){
			
			
			return true;
		}
                 } 
			//added by abhishek as per RLOS FSD
		else if(pId=='NotepadDetails_Modify'){
			
			var notepadDesc = getLVWAT('cmplx_NotepadDetails_cmplx_notegrid', com.newgen.omniforms.formviewer.getNGListIndex("cmplx_NotepadDetails_cmplx_notegrid"),12);
				if(notepadDesc!='NA' && notepadDesc!=activityName){
					
					 showAlert('NotepadDetails_notedesc',alerts_String_Map['VAL152']);
						 
						   document.getElementById("NotepadDetails_notedesc").value = '';
						    document.getElementById("NotepadDetails_notecode").value = '';
							 document.getElementById("NotepadDetails_Actdate").value = '';
					  return false;
						
					
				}
				else{
					if(checkMandatory(CC_Notepad_modify))
				    return true;
				}
			
			
            }
			
		else if(pId=='NotepadDetails_Delete')
			return true;
		// added by abhishek as per RLOS FSD
		
		else if(pId=='CompanyDetails_Add'){
			if(checkMandatory_CompanyGrid()){
				if(validate_Company()){
					setNGValueCustom('appNAme',getNGValue('compName'));
					setNGValueCustom('appCateg','Business');
					setLockedCustom('CompanyDetails_Frame3',false);
					return true;
				}
			}	
			flag_Company_FetchDetails=false;
		}	
		
		else if(pId=='CompanyDetails_Modify'){
			if(checkMandatory_CompanyGrid()){
				if(validate_Company()){
					if(getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',com.newgen.omniforms.formviewer.getNGListIndex(		'cmplx_CompanyDetails_cmplx_CompanyGrid'),1)=='Business'){
						setNGValueCustom('appNAme',getNGValue('compName'));
					}
				return true;	//modified by akshay on 15/12/17
				}
			}		
        }
		else if(pId=='CompanyDetails_delete'){
			return true;	
		}
		
		//changed by akshay on 18/1/18
		else if(pId=='cmplx_CompanyDetails_cmplx_CompanyGrid'){
			return true;
			/*if(getNGListIndex(pId)==0){
				setLockedCustom('CompanyDetails_Frame1',true);
				setLockedCustom('CompanyDetails_Modify',false);
				setLockedCustom('CompanyDetails_Add',false);
			}
			else{
				setLockedCustom('CompanyDetails_Frame1',false);
				setLockedCustom('CompanyDetails_Modify',false);
				setLockedCustom('CompanyDetails_Add',false);
			}*/	
		}

		
		else if(pId=='PartnerDetails_add'){
			if(checkMandatory_PartnerGrid())
				return true;
		}		
		
		else if(pId=='PartnerDetails_modify'){
			return true;
         }
		else if(pId=='PartnerDetails_delete'){
			return true;		
		}
		else if(pId=='AuthorisedSignDetails_CheckBox1'){
			if(getNGValue(pId)==true){
 //added By AKshay on 10/10/17 start-----For Point 15 of BTC_NTB_Defects
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
				setLockedCustom('AuthorisedSignDetails_shareholding',true);
				setLockedCustom('AuthorisedSignDetails_SoleEmployee',true);
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
				setLockedCustom('AuthorisedSignDetails_shareholding',false);
				setLockedCustom('AuthorisedSignDetails_SoleEmployee',false);
 //ended By AKshay on 10/10/17 start-----For Point 15 of BTC_NTB_Defects
			}
		}
		
		if(pId=='AuthorisedSignDetails_FetchDetails')
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
		else if(pId=='cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails'){
			//modified by akshay on 18/1/18 for disabling primary row pre fetched fields
			if(getNGListIndex('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails')==0 && getNGListIndex('cmplx_CompanyDetails_cmplx_CompanyGrid')==0){		//For primary row
				setLockedCustom('AuthorisedSignDetails_CheckBox1',true);
				//setLockedCustom('AuthorisedSignDetails_CIFNo',true);
				setLockedCustom('AuthorisedSignDetails_FetchDetails',true);
				setLockedCustom('AuthorisedSignDetails_Name',true);
				setLockedCustom('AuthorisedSignDetails_DOB',true);
				setLockedCustom('AuthorisedSignDetails_VisaNumber',true);
				setLockedCustom('AuthorisedSignDetails_PassportNo',true);
				setLockedCustom('AuthorisedSignDetails_nationality',true);
				setLockedCustom('AuthorisedSignDetails_VisaExpiryDate',true);
				setLockedCustom('AuthorisedSignDetails_PassportExpiryDate',true);
				setLockedCustom('AuthorisedSignDetails_Status',true);
				setLockedCustom('AuthorisedSignDetails_delete',true);
			}
			else{
				setLockedCustom('AuthorisedSignDetails_CheckBox1',false);
				//setLockedCustom('AuthorisedSignDetails_CIFNo',false);
				setLockedCustom('AuthorisedSignDetails_FetchDetails',false);
				setLockedCustom('AuthorisedSignDetails_Name',false);
				setLockedCustom('AuthorisedSignDetails_DOB',false);
				setLockedCustom('AuthorisedSignDetails_VisaNumber',false);
				setLockedCustom('AuthorisedSignDetails_PassportNo',false);
				setLockedCustom('AuthorisedSignDetails_nationality',false);
				setLockedCustom('AuthorisedSignDetails_VisaExpiryDate',false);
				setLockedCustom('AuthorisedSignDetails_PassportExpiryDate',false);
				setLockedCustom('AuthorisedSignDetails_Status',false);
				setLockedCustom('AuthorisedSignDetails_delete',false);
			}	
		}		
		
		else if(pId=='cmplx_EmploymentDetails_Others')
		{
			if(getNGValue('cmplx_EmploymentDetails_Others')==true){
				setVisible('EMploymentDetails_Label72',false);//Arun (07/12/17) to hide this field if others is checked
				setVisible('cmplx_EmploymentDetails_EMpCode',false);//Arun (07/12/17) to hide this field if others is checked
				setLockedCustom("cmplx_EmploymentDetails_EmpName",false);
				setLockedCustom("cmplx_EmploymentDetails_EMpCode",true);
				//added by abhishek
				setLockedCustom("EMploymentDetails_Text21",true);
				setLockedCustom("EMploymentDetails_Text22",true);
				setNGValueCustom("cmplx_EmploymentDetails_EmpName","");
				setNGValueCustom("cmplx_EmploymentDetails_EMpCode","");
				setEnabledCustom("EMploymentDetails_Button2",false);
				//added by akshay on 14/12/17 for Initiation point
				setNGValueCustom("cmplx_EmploymentDetails_IncInPL",false);
				setNGValueCustom("cmplx_EmploymentDetails_IncInCC",false);
				//setEnabledCustom('cmplx_EmploymentDetails_IncInPL',true);
				//setEnabledCustom('cmplx_EmploymentDetails_IncInCC',true);

				//added By AKshay started on 10/10/17-----FOr point 69 in BTC_NTB_Defects xls---After aloc  fetched,when others checked,alloc fields are still disabled

				setLockedCustom('cmplx_EmploymentDetails_EmpStatusPL',false);
				setLockedCustom('cmplx_EmploymentDetails_EmpStatusCC',false);
				setLockedCustom('cmplx_EmploymentDetails_CurrEmployer',false);
				setLockedCustom('cmplx_EmploymentDetails_EmpIndusSector',false);
				setLockedCustom('cmplx_EmploymentDetails_Indus_Macro',false);
				setLockedCustom('cmplx_EmploymentDetails_Indus_Micro',false);
				//setLockedCustom('cmplx_EmploymentDetails_FreezoneName',false);
				setNGValueCustom('cmplx_EmploymentDetails_EmpStatusPL','');
				setNGValueCustom('cmplx_EmploymentDetails_EmpStatusCC','');
				setNGValueCustom('cmplx_EmploymentDetails_CurrEmployer','');
				setNGValueCustom('cmplx_EmploymentDetails_EmpIndusSector','');
				setNGValueCustom('cmplx_EmploymentDetails_Indus_Macro','');
				setNGValueCustom('cmplx_EmploymentDetails_Indus_Micro','');
				setNGValueCustom('cmplx_EmploymentDetails_FreezoneName','');
				setNGValueCustom('cmplx_EmploymentDetails_CurrEmployer','CN');
				setLockedCustom('cmplx_EmploymentDetails_CurrEmployer',true);
				setEnabledCustom('cmplx_EmploymentDetails_ApplicationCateg',true);
				

			}
			else{
				setVisible('EMploymentDetails_Label72',true);//Arun (07/12/17) to hide this field if others is checked
				setVisible('cmplx_EmploymentDetails_EMpCode',true);//Arun (07/12/17) to hide this field if others is checked
				setLockedCustom("cmplx_EmploymentDetails_EmpName",true);
				setLockedCustom("cmplx_EmploymentDetails_EMpCode",true);
				//added by abhishek
				setNGValueCustom("cmplx_EmploymentDetails_EmpName","");
				setNGValueCustom("cmplx_EmploymentDetails_EMpCode","");
				setLockedCustom("EMploymentDetails_Text21",false);
				setLockedCustom("EMploymentDetails_Text22",false);
				//setEnabledCustom("EMploymentDetails_Button2",true);--commented by akshay on 29/4/18 for proc 8441
				setNGValueCustom("cmplx_EmploymentDetails_IncInPL",false);
				setNGValueCustom("cmplx_EmploymentDetails_IncInCC",false);
				setLockedCustom('cmplx_EmploymentDetails_EmpStatusPL',true);
				setLockedCustom('cmplx_EmploymentDetails_EmpStatusCC',true);
				setLockedCustom('cmplx_EmploymentDetails_CurrEmployer',true);
				setLockedCustom('cmplx_EmploymentDetails_EmpIndusSector',true);
				setLockedCustom('cmplx_EmploymentDetails_Indus_Macro',true);
				setLockedCustom('cmplx_EmploymentDetails_Indus_Micro',true);
				setLockedCustom('cmplx_EmploymentDetails_FreezoneName',true);
				setNGValueCustom('cmplx_EmploymentDetails_Freezone',false);
				setNGValueCustom('cmplx_EmploymentDetails_EmpStatusPL','');
				setNGValueCustom('cmplx_EmploymentDetails_EmpStatusCC','');
				setNGValueCustom('cmplx_EmploymentDetails_CurrEmployer','');
				setNGValueCustom('cmplx_EmploymentDetails_EmpIndusSector','');
				setNGValueCustom('cmplx_EmploymentDetails_Indus_Macro','');
				setNGValueCustom('cmplx_EmploymentDetails_Indus_Micro','');
				setNGValueCustom('cmplx_EmploymentDetails_FreezoneName','');
				setEnabledCustom('cmplx_EmploymentDetails_IncInPL',false);
				setEnabledCustom('cmplx_EmploymentDetails_Freezone',true);
				setEnabledCustom('cmplx_EmploymentDetails_IncInCC',false);
				//added By AKshay ended on 10/10/17-----FOr point 69 in BTC_NTB_Defects xls---After aloc  fetched,when others checked,alloc fields are still disabled	
				setEnabledCustom('cmplx_EmploymentDetails_ApplicationCateg',true);				
			}
			if(setALOCListed())
				return true;

		}
		
		else if(pId=='cmplx_EmploymentDetails_IncInCC'){
			if(setALOCListed())
				return true;
		}

		else if(pId=='cmplx_EmploymentDetails_IncInPL'){
			if(setALOCListed())
				return true;
		}
		//added by akshay for proc 7664 on 7 april 2018
		else if(pId=='cmplx_EmploymentDetails_Freezone'){
			if(getNGValue(pId)==true)
			{
			setLockedCustom('cmplx_EmploymentDetails_FreezoneName',false);
			}
			else{
			setLockedCustom('cmplx_EmploymentDetails_FreezoneName',true);
			}
		}	
		else if(pId=='EMploymentDetails_Button2'){
			if(getNGValue('EMploymentDetails_Text21')!='' || getNGValue('EMploymentDetails_Text22')!=''){
				return true;	
			}
			showAlert('EMploymentDetails_Button2','Kindly provide Employer name or code');
		}	
		
		else if(pId=='ParentTab'){
			return true;
		}
		
		else if(pId=='cmplx_Liability_New_AECBconsentAvail'){
			if(getNGValue('cmplx_Liability_New_AECBconsentAvail')==true)
				setLockedCustom('Liability_New_fetchLiabilities',false);
			else
				setLockedCustom('Liability_New_fetchLiabilities',true);
		}
		
		
		//added by Akshay on 9/9/17 as per FSD
		 if(pId=='ExtLiability_CACIndicator')//name change by Tarang on 28/02/2018
		 {
			if(getNGValue('ExtLiability_CACIndicator')==true)//name change by Tarang on 28/02/2018
			{
				setLockedCustom('QCAmount',false);
				setLockedCustom('QCEMi',false);
				setLockedCustom('takeOverIndicator',true);
				setNGValueCustom('ConsiderObligations',false);//added By Tarang started on 12/02/2018 as per drop 4 point 18
				setLockedCustom('ConsiderObligations',true); //added By Tarang started on 12/02/2018 as per drop 4 point 18
			}	
			else{
				setLockedCustom('QCAmount',true);
				setLockedCustom('QCEMi',true);
				setLockedCustom('takeOverIndicator',false);
				setLockedCustom('ConsiderObligations',false);//added By Tarang started on 12/02/2018 as per drop 4 point 18
			}
		}
		
		 if(pId=='takeOverIndicator'){
			if(getNGValue('takeOverIndicator')==true){
				setLockedCustom('takeoverAMount',false);
				setNGValueCustom('ConsiderObligations',false);
				setLockedCustom('ExtLiability_CACIndicator',true);//name change by Tarang on 28/02/2018
			}	
			else{
				setLockedCustom('takeoverAMount',true);
				setNGValueCustom('ConsiderObligations',true);
				setLockedCustom('ExtLiability_CACIndicator',false);//name change by Tarang on 28/02/2018
			}
		}
		//ended by Akshay on 9/9/17 as per FSD
		//change by saurabh on 4th Dec for CAC indicator.
		 if(pId=='Liability_New_add'){
			if(checkMandatory_LiabilityGrid()){
				if(CAC_check(pId)){
				return true;	
				}
		}
		}
		else if(pId=='Liability_New_modify'){
			if(checkMandatory_LiabilityGrid()){
			if(CAC_check(pId)){
			return true;
			}
		}
		}
		else if(pId=='Liability_New_delete'){
			if(CAC_check(pId)){
			return true;
			}
		}	
		else if(pId=='addr_Add'){
			if(checkMandatory_Address('add'))
				if(Address_Validate('add'))
				{
				
					return true;
				}					
		}
		
		else if(pId=='addr_Modify'){
			if (checkMandatory_Address('modify'))
	            if (Address_Validate('modify'))
					return true;
		}
			else if(pId=='addr_Delete'){
			return true;
		}
		else if(pId=='Reject'){
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
			////select Kyc for all customer type
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");
			var custTypePickList = document.getElementById("KYC_CustomerType");
			var picklistValues=getPickListValues(custTypePickList);
			if((picklistValues.length)>n)
			{
				showAlert('AddressDetails_CustomerType','Please add KYC for all Application Type');	
					return false;
			}
			 removeFrame(pId);
				return true;
		}
		else if(pId=='Account'){
			return true;
		}
		
		
		else if(pId=='Eligibility_Emp' || pId=='ELigibiltyAndProductInfo_Button1'){
		// Done by aman for CR 1312	
		//below code commented by nikhil 05/01/2018 as same moved to java
		/*	if (pId=='ELigibiltyAndProductInfo_Button1' && (  parseInt(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit'))!=parseInt(getNGValue('cmplx_EligibilityAndProductInfo_EFCHidden')) ))
		{
			showAlert('',alerts_String_Map['VAL377']);
			return false;
		}*/
// Done by aman for CR 1312
		
		//condition added by saurabh on 17th Nov 17.
		var Atype = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4);
			if (getNGValue('dedupecheck')=='Y' && (Atype!='TOPN' && Atype!='TOPE')){
				showAlert('',alerts_String_Map['VAL357']);
				return false;
			}
			if(getNGValue('Is_Financial_Summary')!='Y'){
				if(getNGValue('cmplx_Customer_NTB')==false){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
				}
				else if(getNGValue('aecb_call_status')=='' && getNGValue('cmplx_Customer_NEP')!='NEWC'){
				showAlert('Liability_container',alerts_String_Map['VAL106']);
				return false;
				}
			}
			 if((getNGValue('Is_employment_save')!='Y' && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Pensioner'))&& getNGValue('cmplx_Customer_VIPFlag')!=true)  {
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}
			else if(getNGValue('IS_WORLD_CHECK')==null || getNGValue('IS_WORLD_CHECK')==''){
				showAlert('WorldCheck_fetch','Please perform fetch world check');
				return false;
			}
			else{
			//change by saurabh on 30th Nov for FSD 2.7
			var counter = getNGValue('reEligbility_init_counter').split(';')[0];
			var eligFlag = getNGValue('reEligbility_init_counter').split(';')[1];
			if(pId=='ELigibiltyAndProductInfo_Button1'){
				if(counter==(parseInt(alerts_String_Map['VAL323'])-1).toString()){
				setNGValueCustom('reEligbility_init_counter',alerts_String_Map['VAL323']+';'+eligFlag);
				setEnabledCustom('ELigibiltyAndProductInfo_Button1',false);
				}
				else if(parseInt(counter)<(parseInt(alerts_String_Map['VAL323'])-1)){
				setNGValueCustom('reEligbility_init_counter',(parseInt(counter)+1).toString()+';'+eligFlag);
				}
			}
			else if(pId=='Eligibility_Emp'){
				setNGValueCustom('reEligbility_init_counter',counter+';Y');
			}
			
			}
			return true;
		}
		/*else if(pId=='ELigibiltyAndProductInfo_Button1'){
		//condition added by saurabh on 17th Nov 17.
			if(getNGValue('Is_Financial_Summary')!='Y'){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
			}
			else if(getNGValue('Is_employment_save')!='Y'){
				showAlert('EMploymentDetails_Save',alerts_String_Map['VAL319']);
				return false;
			}
			else{
			return true;
			}
		}*/
		else if(pId=='Button2'){
			var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			var salType=getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6);
			var Atype = getLVWAT("cmplx_Product_cmplx_ProductGrid",0,4);
			
			if (getNGValue('dedupecheck')=='Y' && (Atype!='TOPN' && Atype!='TOPE')){
				showAlert('',alerts_String_Map['VAL357']);
				return false;
			}
			
			if (!Customer_Save_Check()){
				showAlert('',alerts_String_Map['VAL118']);
			}
				else if(getNGValue('cmplx_Customer_Is_Employer_Search')!='Y' &&  ( getNGValue('EmploymentType').indexOf('Salaried')>-1)){
					showAlert('customer_search','Employer Search is mandatory!!');
				return false;
				}
			else if (n==0){
				showAlert('',alerts_String_Map['VAL100']);
			}
			else if((getNGValue('Is_Financial_Summary')!='Y')&& !getNGValue('cmplx_Customer_NTB')){
				showAlert('IncomeDetails_FinacialSummarySelf',alerts_String_Map['VAL223']);
				return false;
			}
			else{
				return true;
				}
		}
		
		 if(pId=='Eligibilitycheck'){
			return true;
		}
		
		
		else if(pId=='SupplementCardDetails_Button2')
		{	
			return true;
		}
		// Changed by aman for CR 1112
		else if(pId=='SupplementCardDetails_add'){
			if(checkMandatory_SupplementGrid()){
				if(validateSuppEntry('add')){
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
		else if(pId=='SupplementCardDetails_modify'){
			if(checkMandatory_SupplementGrid()){
				if(validateSuppEntry('modify'))
				return true;
		}		
		}		
					
		else if(pId=='SupplementCardDetails_delete'){
			if(checkForApplicantTypeInGrids('Supplement'))
				return true;
		}
		
		else if(pId=='SupplementCardDetails_cmplx_SupplementGrid')
		{
			setLockedCustom('SupplementCardDetails_FetchDetails',true);
		}
		
		if(pId=='cmplx_FATCA_w8form'){
			if(getNGValue('cmplx_FATCA_W8Form')==true && (getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')){
			setLockedCustom('cmplx_FATCA_w9form',true);
			}
			else{
			setLockedCustom('cmplx_FATCA_w9form',false);
			}	
	}
	
	else if(pId=='cmplx_FATCA_w9form'){
		 if(getNGValue('cmplx_FATCA_w9form')==true && (getNGValue('FATCA_USRelaton')=='R' || getNGValue('FATCA_USRelaton')=='N')){
			setLockedCustom('cmplx_FATCA_w8form',true);
		}
		else{
			setLockedCustom('cmplx_FATCA_w8form',false);
			}	
	}
	
	
		else if(pId=='FATCA_Button1')
		{
			return true;
		}
			
		else if(pId=='FATCA_Button2'){
			return true;	
		}

		// added by akshay on 15/9/17 to add bt details in grid as per  FSD
		else if(pId=='BT_Add'){
			if(checkMandatory_BTGrid()){
				if(validate_BTGrid(pId))
					return true;
			}
			
		}
		else if(pId=='BT_Modify'){
			if(checkMandatory_BTGrid()){
				if(validate_BTGrid(pId))
					return true;
			}	
		}
		
		else if(pId=='BT_Delete'){
			return true;
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
				setLockedCustom("DDS_save",true);//added by akshay on 24/1/18

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
		// ended by akshay to add bt details in grid as per  FSD

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
		else if(pId=='IncomingDocNew_Modifybtn'){
			if(!checkMandatoryIncomingDoc()){
				return false;
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
		//above code by saurabh for incoming doc new
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
		
			//added here for documents
		else if(pId=='IncomingDoc_AddFromDMSButton'){
			addDocFromOD();
		}
		//code added to see the documents name
		else if(pId=='IncomingDoc_AddFromPCButton'){
			var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		//	alert("row clicked:"+row);
		setNGValueCustom('RowNum',row);
			var doc_clicked_id ="IncomingDoc_Frame_Reprow"+row+"_Repcolumn0";
			//alert("value: "+ document.getElementById(doc_clicked_id).value);
			var docname1=document.getElementById(doc_clicked_id).value;
		//	alert("value of docname "+ docname1);
			addDocFromPC();
			window.parent.DocName(docname1);
			 //to have docIndex
			 //commented by deepak as this is no longer in use
			//window.parent.rowClicked(row);//added By Tanshu on 10/10/17-----to set Docindex corresponding to each doc added
		 //to have docIndex
			return true;
		}
		
		else if(pId=='IncomingDoc_ScanButton'){
				openScannerWindow();
				//ScanDocClick();//added By Akshay on 2/8/17 for scanning docs issue

		}
		
		 else if (pId == 'IncomingDoc_ViewButton')
    {
		//commented by deepak as this is no longer in use
        //var obj = getInterfaceData("DLIST", "");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        var row_print = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn11";
        var docIndex = document.getElementById(row_print).value;
        parent.reloadapplet(docIndex, '');
    }
		
		 else if (pId == 'IncomingDoc_PrintButton')
    {
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
		setNGValueCustom('RowNum',row);
        var row_print = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn11";
        var docIndex = document.getElementById(row_print).value;
        var doc_clicked_id = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn0";
        var docName = document.getElementById(doc_clicked_id).value;
        printImageDocument(docIndex);//added By Tanshu on 10/10/17-----to print png fies(for now...coordinating with product)
    }

		//modified to download the document 
		else if(pId=='IncomingDoc_DownloadButton'){
		//commented by deepak as this is no longer in use
		//var obj = getInterfaceData("DLIST", "");
        var row = com.newgen.omniforms.formviewer.getRepRowClickIndex();
        var row_print = "IncomingDoc_Frame_Reprow" + row + "_Repcolumn11";
        var docIndex = document.getElementById(row_print).value;
        parent.reloadapplet(docIndex, '');
		customDownloadDocument();
		}
		//modified to download the document 
			//ended here
		
		else if(pId=='Customer_save'){
			if(Customer_Save_Check()){
				setNGValueCustom('cmplx_Customer_IscustomerSave','Y');
				//below code commented by nikhil 28/11/17 as same moved to product add/modify
							/*if(getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')<21){
								setVisible("GuarantorDetails", true);
								setTop("GuarantorDetails",parseInt(getTop('ProductDetailsLoader'))+parseInt(getHeight("ProductDetailsLoader"))+5+"px");
								setTop("Incomedetails",parseInt(getTop('GuarantorDetails'))+parseInt(getHeight("GuarantorDetails"))+5+"px");
							}
							else
							{
								setVisible("GuarantorDetails", false);
							}*/
								 removeFrame(pId);
				return true; 
			}
		}
		else if(pId=='Customer_search'){
		if(getNGValue('cmplx_Customer_Employer_name')!='' || getNGValue('cmplx_Customer_Employer_code')!='' )
		{
			setNGValueCustom("cmplx_Customer_Is_Employer_Search", 'Y');
			//added by nikhil 28/11 to save emploument details
			var value = 'EmploymentDetails';
			var field_string_value = getNGValue('FrameName');
			if(field_string_value.indexOf(value)==-1){
			var hiddenString = value+',';
			var previousValue = field_string_value;
			hiddenString = previousValue + hiddenString;
			setNGValueCustom('FrameName',hiddenString);
		}
				return true; 		
				
		}
		else
		{
			showAlert('cmplx_Customer_Employer_name',alerts_String_Map['VAL372']);
			return false;			
		}
		}
		
		else if(pId=='Product_Save'){
			if(checkMandatory(RLOS_PRODUCT)){
				removeFrame(pId);
				return true;}
		}
		
		else if(pId=='GuarantorDetails_Save'){
			if(Guarantor_Save_Check()){
				removeFrame(pId);
				return true;  }
		}
		//added by yash on 27/7/2017 for the proc 801
		else if(pId=='CompanyDetails_save')
		{
			if(company_save())
				return true;  
		}
		// ended by yash 
		else if(pId=='IncomeDetails_Salaried_Save' || pId=='IncomeDetails_SelfEmployed_Save'){
			if(Income_Save_Check()){
				removeFrame(pId);
				return true;	}
		}
		
		else if(pId=='Liability_New_Save'){
			
			if(checkMandatory(RLOS_LIABILITY)){
				removeFrame(pId);
				return true;}
		/*var empType=getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2);
		if (empType=='Salaried'){
			if(checkMandatory(RLOS_LIABILITY))
				return true;	
		}
		else{
		if(checkMandatory(RLOS_LIABILITY_SELF_EMPLOYED))
				return true;
		}*/ //Arun commented (30-11-17) as this validation is not needed
		}
		
		else if(pId=='EMploymentDetails_Save'){
			if(Employment_Save_Check()){
				setNGValueCustom('Is_employment_save','Y');
				removeFrame(pId);
				return true;
			}
			
		}
		
		else if(pId=='ELigibiltyAndProductInfo_Save'){
			var EFCHidden=getNGValue('cmplx_EligibilityAndProductInfo_EFCHidden');
			var FinalLimit=getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit');
			if (FinalLimit==''){
				FinalLimit=0;
			}
			if (EFCHidden==''){
				EFCHidden=0;
			}
			
			
			  if (parseInt(EFCHidden)+parseInt(FinalLimit) <3000){
					showAlert('cmplx_EligibilityAndProductInfo_FinalLimit',alerts_String_Map['VAL371']);
					return false;
		}
			
			  if(Eligibility_Save_Check()){
					removeFrame(pId);
					return true;}
		}
						
		else if(pId=='AddressDetails_Save'){
			if(checkMandatory_AddressDetails_Save())
			{
				if(checkPrefferedChoice()){
				removeFrame(pId);
					return true;
				}
			}
		}
		
		else if(pId=='ContactDetails_Save'){
			if(ContactDetails_Save_Check()){
				removeFrame(pId);
				return true;}
		}
		else if(pId=='ReferenceDetails_save'){
			if(ReferenceDetails_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}		
				
		 else if(pId=='CardDetails_save'){
			if(CardDetails_Save())
				{
				removeFrame(pId);
				return true;}
		}		
					
		else if(pId=='SupplementCardDetails_Save'){
			{
				removeFrame(pId);
				return true;}
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
			setNGValueCustom('cmplx_FATCA_selectedreasonhidden',selectedReason);
			return true;
			}
		}
		//Added By Prabhakar for drop-4Point-3
		else if(pId=='FATCA_Modify')
		{
			if(FATCA_Save_Check('modify'))
			{
			var selectedReason=getNGValue('cmplx_FATCA_selectedreasonhidden');
			if(selectedReason.charAt(selectedReason.length-1)==',')
			{
				selectedReason=selectedReason.substring(0,selectedReason.length-1)
			}
			setNGValueCustom('cmplx_FATCA_selectedreasonhidden',selectedReason);
			return true;
			}
		}
		//Added By Prabhakar for drop-4Point-3
		else if(pId=='FATCA_Delete')
		{
			setNGValueCustom('cmplx_FATCA_selectedreasonhidden','');
			return true;
		}
		//Added By Prabhakar for drop-4Point-3
		else if(pId=='cmplx_FATCA_cmplx_GR_FatcaDetails')
		{
			/*if(com.newgen.omniforms.formviewer.getNGListIndex("cmplx_FATCA_cmplx_GR_FatcaDetails")==-1){
				com.newgen.omniforms.formviewer.NGClear("cmplx_FATCA_SelectedReason");
			}*/
			return true;
		}
		//changed by prabhakar drop -4 point-3
		else if(pId=='FATCA_Save'){
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_FATCA_cmplx_GR_FatcaDetails");
			var custTypePickList = document.getElementById('cmplx_FATCA_CustomerType');
			var picklistValues=getPickListValues(custTypePickList);
			if((picklistValues.length)>n || n==0)
			{
				showAlert('cmplx_FATCA_CustomerType','Please add FATCA for all Applicant type');	
					return false;
			}
				
				removeFrame(pId);
				return true;
		}
		
		else if(pId=='KYC_Save'){
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
		
		else if(pId=='OECD_Save'){
			var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			var custTypePickList = document.getElementById("OECD_CustomerType");//Added by prabhakar drop-4Point-3
			var picklistValues=getPickListValues(custTypePickList);//Added by prabhakar drop-4Point-3
			if((picklistValues.length)>n || n==0)
			{
				showAlert('OECD_CustomerType','Please add OECD for all Applicant  type');	
					return false;
			}
			removeFrame(pId);
				return true;
		}
		
		else if(pId=='BTC_save'){
			if(checkMandatory(RLOS_BT))
			{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='CC_Loan_DDS_save'){
			if(DDS_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='CC_Loan_SI_save'){
			if(SI_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='CC_Loan_RVC_Save'){
			if(RVC_Save_Check())
				{
				removeFrame(pId);
				return true;}
		}
		else if(pId=='IncomingDoc_Save'){
			
			return true;
		}
		else if(pId=='NotepadDetails_save'){
			if(checkMandatory(Notepad_Details))
			{
				removeFrame(pId);
				return true;}
		}
		
		else if(pId=='DecisionHistory_Save'){
			if(!Decision_Save_Check()){
			{
				removeFrame(pId);
				return true;}
	}
	else{return true;}
		}
		
		// added by yash on 31 march
		//alert('pId is :' + pId);
	else if(pId=='DecisionHistory_Button2')
		{
		//below code by nikhil for PCSP-19
		var value = 'IncomingDocuments';
		var field_string_value = getNGValue('FrameName');
		if(field_string_value.indexOf(value)==-1){
			var hiddenString = value+',';
			var previousValue = field_string_value;
			hiddenString = previousValue + hiddenString;
			setNGValue('FrameName',hiddenString);
			}
			//window.parent.executeAction('2', 'Y');
			//changes done by disha for new generate template socket on 11-Sep-2018
			var wi_name = window.parent.pid;
			var docName = "Application_Form";
			var attrbList = "";		
			attrbList += RLOSTemplateData(); 	
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
			/*var Product_type_value= getNGValue('LoanType_Primary');
			var ReqProd_value= getNGValue('PrimaryProduct');
			var subProd_value= getNGValue('Subproduct_productGrid');
			var AppType_value= getNGValue('Application_Type');
			//alert('Product_type is :' + Product_type_value + 'ReqProd is : ' + ReqProd_value + 'subProd is :' + subProd_value);

			if((Product_type_value=='Islamic') && (ReqProd_value=='Credit Card') && (subProd_value=='IM'))
			 {
				window.parent.executeAction('4', 'Y');
				return true;
			 }
			if((Product_type_value=='Conventional') && (ReqProd_value=='Personal Loan') && (subProd_value=='EXP') && (AppType_value=='TOPE'))
			 {
				window.parent.executeAction('5', 'Y');
				return true;
			 }
			if((Product_type_value=='Islamic') && (ReqProd_value=='Personal Loan'))
			 {
				window.parent.executeAction('6', 'Y');
				return true;
			 }
			if((Product_type_value=='Conventional') && (ReqProd_value=='Personal Loan'))
			 {
				window.parent.executeAction('7', 'Y');
				return true;
			 }
		}*/
		//code ended by yash
		else if(pId=='OECD_add')
		{	
			if(checkMandatory_OecdGrid())
			return true;
		}
		else if(pId=='OECD_modify')
		{
			if(checkMandatory_OecdGridModify())
			return true;
		}
		else if(pId=='OECD_delete')
		{
			return true;
		}
		
		
				
		else if(pId=='Fetch_existing_cas')
		{ 	
			if(getNGValue('OldApplicationNo')=='' || getNGValue('OldApplicationNo')==null)
			{
				showAlert('OldApplicationNo',alerts_String_Map['VAL110']);
				return false;
			}
			
				var old_app_no=getNGValue('OldApplicationNo');
				var wi_name= window.parent.pid;
				var new_app_no=getNGValue('ApplicationRefNo');//added by akshay on 22/1/18
				
				//added by akshay on 29/1/18
				if(getNGValue('Is_Customer_Details')=='Y' || getNGValue('cmplx_Customer_NTB')==true){
					showAlert('OldApplicationNo',alerts_String_Map['VAL342']);
					return false;
				}
				
				var ajaxReq;
				var dataFromAjax;
				var output;
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}
				//alert("Here--------------------------");
				
				var url = "/webdesktop/custom/CustomJSP/exec_datacopy_procdure.jsp";//?ProcName=NG_RLOS_COPY_DATA_PROC&old_app_no="+old_app_no+"&wi_name="+wi_name+"&new_app_no="+new_app_no
				//alert("After----------"+url);
				//window.open(url);
				var params="old_app_no="+old_app_no+"&wi_name="+wi_name+"&new_app_no="+new_app_no;//"table="+stable+"&columnsNames="+data_save_var+"&columnsValues="+value+"&swhere="+swhere;
				
				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send(params);
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					output=ajaxReq.responseText;
					if(output.trim()=='success'){
					var columnNames,columnName_arr;
					ajaxReq.open("POST", "/formviewer/resources/scripts/RLOS/System_parameters.properties", false);  
					ajaxReq.send();
					if (ajaxReq.readyState == 4 && ajaxReq.status == 200){
						dataFromAjax = ajaxReq.responseText;
						var parts=dataFromAjax.split('\n');	
						//console.log(parts[0]);		
						for(var i=0;i<parts.length;i++){
							if((parts[i].split('='))[0]=='EXTERNALTABLE_COLUMNS')		
							columnNames=(parts[i].split('='))[1];
							columnName_arr=columnNames.split(',');
						}
						//Change done by Surabh for SQL Injection
						//var query="SELECT "+columnNames+" FROM NG_RLOS_EXTTABLE with (nolock) where itemindex ='"+old_app_no+"' ";
						var result=getDataFromDB('FetchData',"item=="+old_app_no,'PLjs');
						//console.log(result);
						var columnValue_arr=(result.split('~')[0]).split('#');
						//console.log(columnValue_arr);
						for(var counter=0;counter<columnName_arr.length;counter++){
							console.log(columnName_arr[counter]+':'+columnValue_arr[counter]);
							setNGValueCustom(columnName_arr[counter],columnValue_arr[counter]);
						}			
					}
					else
					{
						showAlert('',alerts_String_Map['VAL343']);
						return false;
					}
				  
					//added by akshay on 29/1/18
					if(getNGValue('EmploymentType')=='Salaried' || getNGValue('EmploymentType')=='Salaried Pensioner' || getNGValue('EmpType')=='Pensioner'){								
						setSheetVisible("ParentTab", 1, false);
						setSheetVisible("ParentTab", 3, true);
						if(getNGValue("PrimaryProduct")=='Credit Card'){
						setSheetVisible("ParentTab", 7, true);
						}
						else{
						setSheetVisible("ParentTab", 7, false);
						}
					}	
					else if(getNGValue('EmploymentType')=='Self Employed'){
						setSheetVisible("ParentTab", 1, true);
						setSheetVisible("ParentTab", 3, false);
						setSheetVisible("ParentTab", 7, true);
					}
					//added by akshay on 29/1/18
				setLockedCustom('existingOldCustomer',true);
				setLockedCustom('OldApplicationNo',true);
				setLockedCustom('Fetch_existing_cas',true);
				if(getNGValue('IS_SupplementCard_Required')=='Y'){
					setVisible('Supplementary_Container',true);
					setTop('Supplementary_Container',(parseInt(getTop('CardDetails_container'))+parseInt(getHeight('CardDetails_container'))+10)+'px');
				}
				
				for (var prop in RLOSFRAGMENTLOADOPT){
				  if (RLOSFRAGMENTLOADOPT[prop]=='N') { 
					//alert("prop: " + prop + " value: " + RLOSFRAGMENTLOADOPT[prop]);
					setNGFrameState(prop,1);
					RLOSFRAGMENTLOADOPT[prop]='';
					setNGFrameState(prop,0);
				  }
				}
				
				showAlert('',alerts_String_Map['VAL344']);
				
			}
			else{
				showAlert('',alerts_String_Map['VAL343']);
				return false;
				}
				
				
					//return true;
				}
				else
				{
				showAlert('',alerts_String_Map['VAL343']);
					return false;
				}
			
		}
		
		else if(pId=='Liability_New_Frame2' && frameState==0)
		{
			if(getNGValue('cmplx_Customer_NTB')==false || getNGValue('cmplx_Customer_NTB')==null)
			{
				var aecb_call_status=getNGValue('aecb_call_status');
				//alert("aecb_call_status 1"+ aecb_call_status);
				if(aecb_call_status=='Fail' || aecb_call_status=='' || aecb_call_status==null){
					showAlert('Liability_New_fetchLiabilities',alerts_String_Map['VAL106']);
					setNGFrameState('Liability_New_Frame2',1);
					return false;
				}
				else{
					com.newgen.omniforms.formviewer.setIFrameSrc('Liability_New_IFrame_internal','/webdesktop/custom/CustomJSP/internal_liability.jsp');
					//document.getElementById("Liability_New_IFrame_internal").style.top="100px";
				}
					
			}
			else{
					com.newgen.omniforms.formviewer.setIFrameSrc('Liability_New_IFrame_internal','/webdesktop/custom/CustomJSP/internal_liability.jsp');
					//document.getElementById("Liability_New_IFrame_internal").style.top="100px";
			}
				
		}
		else if(pId=='Liability_New_Frame3' && frameState==0)
		{
			var aecb_call_status=getNGValue('aecb_call_status');
			//alert("aecb_call_status 2"+ aecb_call_status);
			if(aecb_call_status=='Fail' || aecb_call_status=='' || aecb_call_status==null){
				showAlert('Liability_New_fetchLiabilities',alerts_String_Map['VAL106']);
				setNGFrameState('Liability_New_Frame3',1);
				return false;
			}
			else
				com.newgen.omniforms.formviewer.setIFrameSrc('Liability_New_IFrame_external','/webdesktop/custom/CustomJSP/External_Liability.jsp');//dynamic load of jsp from code

		}
		else if(pId=='Liability_New_Frame5' && frameState==0)
		{
			if(getNGValue('cmplx_Customer_NTB')==false || getNGValue('cmplx_Customer_NTB')==null)
			{
				var aecb_call_status=getNGValue('aecb_call_status');
				//alert("aecb_call_status 3"+ aecb_call_status);
				if(aecb_call_status=='Fail' || aecb_call_status=='' || aecb_call_status==null){
					showAlert('Liability_New_fetchLiabilities',alerts_String_Map['VAL106']);
					setNGFrameState('Liability_New_Frame5',1);
					return false;
				}
				else
					com.newgen.omniforms.formviewer.setIFrameSrc('Liability_New_IFrame_pipeline','/webdesktop/custom/CustomJSP/Pipeline.jsp');//dynamic load of jsp
			}
			else
					com.newgen.omniforms.formviewer.setIFrameSrc('Liability_New_IFrame_pipeline','/webdesktop/custom/CustomJSP/Pipeline.jsp');//dynamic load of jsp
		}		
		
		else if(pId=='Liability_New_Frame4' && frameState==0)
		{
			if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4).indexOf('TKO')>-1)
			{
				setEnabledCustom('takeOverIndicator',true);
			}
			else{
				setEnabledCustom('takeOverIndicator',false);
			}
			/*	if(getNGValue('Application_Type').indexOf('TKO')>-1)
			{
				setEnabledCustom('takeOverIndicator',true);
				setNGValueCustom('takeoverAMount','');
			}
			else{
				setEnabledCustom('takeOverIndicator',false);
			}*/
			setLockedCustom("takeoverAMount",true);
			setLockedCustom("QCAmount", true);//added by Akshay on 29/9/17 as per FSD
			setLockedCustom("QCEMi", true);//added by Akshay on 29/9/17 as per FSD
		}
		//ended By Akshay on 10/10/17 for point 31 in BTC_NTB sheet---Takeover indicator to be enabled only if the application type is takeover
			
		else if(pId=='Liability_New_RefreshExternal'){
			//alert('inside');
			document.getElementById('Liability_New_IFrame_external').src='/webdesktop/custom/CustomJSP/External_Liability.jsp';
			
			return false;		
		}
		
		//added by akshay on 12/1/18 for dynamic load of iframes
		else if(pId=='ELigibiltyAndProductInfo_Frame4' && frameState==0)//PL
		{
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame3','/webdesktop/custom/CustomJSP/Product_Eligibility.jsp');
		}
		
		else if(pId=='ELigibiltyAndProductInfo_Frame5' && frameState==0)//CC
		{
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame1','/webdesktop/custom/CustomJSP/Eligibility_Card_Product.jsp');
		}
		
		else if(pId=='ELigibiltyAndProductInfo_Frame6' && frameState==0)//Eligible
		{
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame2','/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp');
		}
		
		else if(pId=='ELigibiltyAndProductInfo_Frame2' && frameState==0)//Funding
		{
			com.newgen.omniforms.formviewer.setIFrameSrc('ELigibiltyAndProductInfo_IFrame_Funding','/webdesktop/custom/CustomJSP/Funding_Account_No.jsp');
		}
		
		
		//Prateek change 26-11-2017 :To adjust IFrame top under the fragment in order to maintain the collapsible feature 
		/*
		if (pId =='Liability_New_Frame2')
		{			
			document.getElementById("Liability_New_IFrame_internal").style.top="69px";			
			return true;
		}
		
		if (pId =='Liability_New_Frame3')
		{
			alert(document.getElementById("Liability_New_IFrame_internal").style.top);
			document.getElementById("Liability_New_IFrame_internal").style.top="69px";
			document.getElementById("Liability_New_IFrame_external").style.top="30px";
			return true;
		}
		
		if (pId =='Liability_New_Frame5')
		{
			document.getElementById("Liability_New_IFrame_pipeline").style.top="30px";
			return true;
		}
		*/
		
		//Prateek change end
}	
	
//for document related tab
function addDocFromOD(){
window.parent.openAddDocWin(); //workdesk.js
}
function addDocFromPC(){
window.parent.openImportDocWin(); //workdesk.js
}
function openScannerWindow(){
window.parent.openOmniScanDocUrl();
//window.parent.scanDoc(); //workdesk.js
}
function printImageDocument(docIndex){
 var docWindowObj="";
	   if(window.parent.strprocessname==''){
		//docWindowObj=window.parent;
		showAlert('','Please open document interface!!');
		return false;
		}
		else{
			docWindowObj=docWindowObj=window.parent;
		
		var rowNumber=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('RowNum').value;
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
window.parent.downloadDoc("http://192.168.54.80/webdesktop/components/workitem/view/workdesk_default.jsf#","/webdesktop"); //doctypes.js
}
//ended here
			 
function clickCase(controlName){
	return true;
	
}
//for optimization, such that on second time fragment click comboload function is not called again and again from java code
function doNotLoadFragmentSecTime(pId){
	RLOSFRAGMENTLOADOPT[pId]='N';
}	


// new function added to refresh Dectech jsp
function RLOS_postHookDRefresh(controlName){
	if(controlName=='ELigibiltyAndProductInfo_Button1'){
	document.getElementById('ELigibiltyAndProductInfo_IFrame1').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Product.jsp';
	document.getElementById('ELigibiltyAndProductInfo_IFrame2').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp';
	document.getElementById('ELigibiltyAndProductInfo_IFrame3').src='/webdesktop/custom/CustomJSP/Product_Eligibility.jsp';
	}
	//Added by aman for Drop5
	//Deepak else added in below condition
	else if(controlName=='cmplx_EligibilityAndProductInfo_InterestRate'){
	document.getElementById('ELigibiltyAndProductInfo_IFrame1').src='/webdesktop/custom/CustomJSP/Eligibility_Card_Product.jsp';
	
	document.getElementById('ELigibiltyAndProductInfo_IFrame3').src='/webdesktop/custom/CustomJSP/Product_Eligibility.jsp';
	}
	//checkDevtools();
	onLoadValues = getFetchedFragmentFields();
}