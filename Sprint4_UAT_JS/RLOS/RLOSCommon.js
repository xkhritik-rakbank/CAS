var activityName = window.parent.stractivityName;
var pname=window.parent.strprocessname;
 var alerts_String;
var alerts_String_Map={};
var eidPluginBaseURL = "https://localhost:4043/";
var lastAccessToken;
function setFieldsVisible(ReqProd){
	if(ReqProd=='Credit Card'){
				setVisible("CardProd", true);
				setVisible("Product_Label6",true);
				setVisible("Product_Label3",false); 
				setVisible("Product_Label12",false); 
				setVisible("Product_Label21",false);
				setVisible("Scheme",false); 
				setVisible("Product_Label5",false);
				setVisible("Product_Label10",false);
				setVisible("ReqTenor",false);
				setVisible("MiscFields",false);
				
			}
			
	else if(ReqProd=='Personal Loan'){
				setVisible("Product_Label3",true); 
				setVisible("Product_Label12",true); 
				setVisible("Product_Label21",true); 
				setVisible("Scheme",true); 
				setVisible("Product_Label5",true);
				setVisible("CardProd", false);
				setVisible("ReqTenor",true);
				setLeft("Product_Label3", "555px");
				setLeft("Scheme", "555px");
				setLeft("Product_Label5", "822px");
				setLeft("ReqTenor", "822px");
				setVisible("Product_Label10",false);
				setVisible("Product_Label7",false); 
				setVisible("Product_Label6",false); 
				setVisible("Product_Label8",false);
				//setVisible("typeReq",false);
				setVisible("LimitChange",false);
				setVisible("LimitAcc",false); 
				setVisible("LimitExpiryDate",false);
				
				setVisible("MiscFields",true);
				setVisible("LimitAcc", false);
				setVisible("LimitExpiryDate", false);
				//setVisible("typeReq", false);
				setVisible("Product_Label15", false);
				setVisible("Product_Label16", false);
				setVisible("Product_Label17", false);
				setVisible("Product_Label18", false);
				setVisible("Product_Label22", false);
				setVisible("Product_Label23", false);
				setVisible("Product_Label24", false);
				//setNGValueCustom("EmpType","Salaried");//Tarang to be removed on friday(1/19/2018)
				setNGValueCustom("EmploymentType","Salaried");
				//added by akshay on 28/12/17
				setVisible("NoOfMonths", false);
				setVisible("LastPermanentLimit", false);
				setVisible("LastTemporaryLimit", false);
				setVisible("Product_Label9", false);
				setVisible("FDAmount", false);
			}
			
	else{
				setVisible("CardProd",false); 
				setVisible("Product_Label6",false);
				setVisible("Product_Label3",false); 
				setVisible("Product_Label12",false); 
				setVisible("Scheme",false); 
				setVisible("Product_Label5",false);
				setVisible("Product_Label10",false);
				setVisible("ReqTenor",false);
				setVisible("Product_Label5",false); 
				setVisible("Product_Label7",false); 
				setVisible("Product_Label6",false); 
				setVisible("Product_Label8",false);
				//setVisible("typeReq",false);
				setVisible("LimitChange",false);
				setVisible("LimitAcc",false); 
				setVisible("LimitExpiryDate",false);
				setVisible("LimitAcc", false);
				setVisible("LimitExpiryDate", false);
				//setVisible("typeReq", false);
				setVisible("Product_Label15", false);
				setVisible("Product_Label16", false);
				setVisible("Product_Label17", false);
				setVisible("Product_Label18", false);
				setVisible("Product_Label21", false);
				setVisible("Product_Label22", false);
				setVisible("Product_Label23", false);
				setVisible("Product_Label24", false);
				//setNGValueCustom("EmpType","--Select--");//Tarang to be removed on friday(1/19/2018)
				setNGValueCustom("EmploymentType","--Select--");
				//added by akshay on 28/12/17
				setVisible("NoOfMonths", false);
				setVisible("LastPermanentLimit", false);
				setVisible("LastTemporaryLimit", false);
				setVisible("Product_Label9", false);
				setVisible("FDAmount", false);
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

function setALOCListed(){
	var NewEmployer=getNGValue('cmplx_EmploymentDetails_Others');
	var IncInCC=getNGValue('cmplx_EmploymentDetails_IncInCC');
	var INcInPL=getNGValue('cmplx_EmploymentDetails_IncInPL');
	var reqProd=getNGValue('PrimaryProduct');
	
	if(reqProd=='Personal Loan' && NewEmployer==true && (IncInCC==false || INcInPL==false))
	{
		setNGValueCustom("cmplx_EmploymentDetails_EmpStatusPL", "CN");
		setNGValueCustom("cmplx_EmploymentDetails_EmpStatusCC", "CN");
		setLockedCustom("cmplx_EmploymentDetails_EmpStatusCC", true);
		setLockedCustom("cmplx_EmploymentDetails_EmpStatusPL", false);

	}
	
	else if(reqProd=='Credit Card' && NewEmployer==true && (IncInCC==false || INcInPL==false))
	{
		setNGValueCustom("cmplx_EmploymentDetails_EmpStatusPL", "CN");
		setNGValueCustom("cmplx_EmploymentDetails_EmpStatusCC", "CN");
		setNGValueCustom("cmplx_EmploymentDetails_CurrEmployer", "CN");
		setLockedCustom("cmplx_EmploymentDetails_EmpStatusPL", true);
		setLockedCustom("cmplx_EmploymentDetails_CurrEmployer", true);
		setLockedCustom("cmplx_EmploymentDetails_EmpStatusCC", false);
		//document.getElementById('cmplx_EmploymentDetails_EmpStatusCC').remove(7);
		return true;
	}
	
	else{
		if(NewEmployer==true){
			setNGValueCustom("cmplx_EmploymentDetails_EmpStatusPL", "");
			setNGValueCustom("cmplx_EmploymentDetails_EmpStatusCC", "");
			setLockedCustom("cmplx_EmploymentDetails_EmpStatusPL", false);
			setLockedCustom("cmplx_EmploymentDetails_EmpStatusCC", false);
			setLockedCustom("cmplx_EmploymentDetails_CurrEmployer", false);
		}
		return true;
	}
}	

function prefcheck(opType){
var pref = getNGValue("PreferredAddress");
var customerType=getNGValue("Customer_Type");
	if(pref){
		var gridrowcount = getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		if(gridrowcount > 0){
			for(var i=0;i<gridrowcount;i++){
				var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_AddressDetails_cmplx_AddressGrid');
				if(i == selectedRow && opType!='add'){
				continue;
				}
		var gridpref = getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,10);
		var gridCustomerType=getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13);
				if((gridpref == "true" || gridpref == true) && gridCustomerType==customerType){//added by prabhakar
					return false;
				}
			}
		}
	}
	return true;
}
/*
function FirstRepaymentDate()
{
    var moratorium = getNGValue('cmplx_EligibilityAndProductInfo_Moratorium');
	

   if(moratorium =='' || moratorium == null || moratorium == '0' )
   {  
	   return true;
   }   
 
   else
   {
   var dateObj = new Date(); 
   var val = dateObj.getTime();         
   moratorium = parseInt(moratorium)*86400000;
   val = val + moratorium;
   var val1=val;
   dateObj = new Date(val); 
   var month = dateObj.getMonth();
   month = parseInt(month)+1;
   val =  dateObj.getDate()+ "/"+ month + "/" + dateObj.getFullYear(); 
   setNGValueCustom('cmplx_EligibilityAndProductInfo_FirstRepayDate', val);
   
   }
}
*/ //commented by aman because old logic used here

function Moratorium(){
	var FirstRepay = getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate');
	var parts1=FirstRepay.split('/');
	var FirstRepayDate=new Date(parts1[2],parts1[1]-1,parts1[0]);
	var one_day=1000*3600*24;
	var currentdate= new Date();
	var date1_ms=FirstRepayDate.getTime();
	var date2_ms=currentdate.getTime();
	var day=date1_ms-date2_ms;
	day=day/one_day;
	day=parseInt(day);
	return day;
}
function FirstRepaymentDate() {
  var moratorium = getNGValue('cmplx_EligibilityAndProductInfo_Moratorium'); 
  var d = new Date();
  //change by saurabh on 26th Dec
  var FirstRepay = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear();//getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate'); 
  moratorium=parseInt(moratorium);
  var parts1=FirstRepay.split('/');
  var newDate=new Date(parts1[2],parts1[1]-1,parts1[0]);
		
   //var result = new Date(newDate);
   newDate.setDate(newDate.getDate() + moratorium);
   var day = newDate.getDate();	
   var month = newDate.getMonth()+1;	
   var newVal= ("0"+day).slice(-2)+'/'+("0"+month).slice(-2)+'/'+ newDate.getFullYear();


  setNGValueCustom('cmplx_EligibilityAndProductInfo_FirstRepayDate', newVal);
}

//added by yash  25/7/2017 for calcualting maturity date proc 475
function MaturityDate()
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
	/*var day_maturity=oldDate.getDate();
	var month_maturity = oldDate.getMonth()+1;
	var total_months=month_maturity+parseInt(Tenor);
	var year_maturity=oldDate.getFullYear();
	if(total_months>12){
		var years_tobeAdded=parseInt(total_months/12);
		var rem_months=parseInt(total_months%12);
		var year_maturity=year_maturity+parseInt(years_tobeAdded);
		month_maturity=rem_months;
		if(month_maturity==0){
			month_maturity=oldDate.getMonth()+1;
			year_maturity=year_maturity-1;
		}
	}
	if(day_maturity<10){
		day_maturity='0'+day_maturity;
	}
	if(month_maturity<10){
		month_maturity='0'+month_maturity;
	}*/
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
// ended

// added  by yash on 21/7/2017 for calculating netrate 
function calcnetrate()
{
var netrate1 = 0.0 ;
var baserate = getNGValue('cmplx_EligibilityAndProductInfo_BAseRate');
var productrate = getNGValue('cmplx_EligibilityAndProductInfo_ProdPrefRate');
var marginrate = getNGValue('cmplx_EligibilityAndProductInfo_MArginRate');
var netrate = parseFloat(baserate) + parseFloat(productrate) + parseFloat(marginrate)
var netrate1 = netrate1+netrate;
setNGValueCustom ("cmplx_EligibilityAndProductInfo_FinalInterestRate",netrate1);
}
// ended by yash 

function ageatmaturity(){
	var maturityDate=getNGValue('cmplx_EligibilityAndProductInfo_MaturityDate');
	if(maturityDate==null || maturityDate==""){
		return "";
	}
	var maturityAge=calcAge(getNGValue('cmplx_Customer_DOb'),maturityDate);
	var parts = maturityAge.split('.');
	var ageAtMaturity=0;
	if(parseInt(parts[1])==0)
		ageAtMaturity =parseInt(parts[0])+'.00';
	else if(parseInt(parts[1])<10){
		 ageAtMaturity =parseInt(parts[0]) +'.0'+parseInt(parts[1]);
	}
	else
	    ageAtMaturity =parseInt(parts[0]) +'.'+parseInt(parts[1]) ;
	setNGValueCustom("cmplx_EligibilityAndProductInfo_ageAtMaturity",ageAtMaturity);

}

	function Fatca_disableCondition(){
			setLockedCustom("cmplx_FATCA_IdDoc", true);
			setNGValueCustom("cmplx_FATCA_IdDoc", false);
			setLockedCustom("cmplx_FATCA_DecForInd", true);
			setNGValueCustom("cmplx_FATCA_DecForInd", false);
			setLockedCustom("cmplx_FATCA_w8form",true);
			setNGValueCustom("cmplx_FATCA_w8form", false);
			setLockedCustom("cmplx_FATCA_w9form",true);
			setNGValueCustom("cmplx_FATCA_w9form", false);
			setLockedCustom("cmplx_FATCA_lossofnationality",true);
			setNGValueCustom("cmplx_FATCA_lossofnationality", false);
			setLockedCustom("cmplx_FATCA_TINNo",true);
			setNGValueCustom("cmplx_FATCA_TINNo","");
			setLockedCustom("cmplx_FATCA_SignedDate",true);
			setNGValueCustom("cmplx_FATCA_SignedDate","");
			setLockedCustom("cmplx_FATCA_ExpiryDate",true);
			setNGValueCustom("cmplx_FATCA_ExpiryDate","");
			setLockedCustom("cmplx_FATCA_Category",true);
			setNGValueCustom("cmplx_FATCA_Category","");
			setLockedCustom("cmplx_FATCA_ControllingPersonUSRel",true);
			setNGValueCustom("cmplx_FATCA_ControllingPersonUSRel","");
			setLockedCustom("cmplx_FATCA_ListedReason",true);
			setNGValueCustom("cmplx_FATCA_ListedReason","");
			setLockedCustom("cmplx_FATCA_SelectedReason",true);
			setNGValueCustom("cmplx_FATCA_SelectedReason","");			
			setLockedCustom("FATCA_Button1",true);
			setLockedCustom("FATCA_Button2",true);

	}
	//Changed by aman on 22/12
	function Fatca_Enable(){
			setLockedCustom("cmplx_FATCA_IdDoc", false);
			setEnabledCustom("cmplx_FATCA_IdDoc",true);
			setNGValueCustom("cmplx_FATCA_IdDoc", false);
			setLockedCustom("cmplx_FATCA_DecForInd", false);
			setEnabledCustom("cmplx_FATCA_DecForInd",true);
			setNGValueCustom("cmplx_FATCA_DecForInd", false);
			setLockedCustom("cmplx_FATCA_w8form",false);
			setEnabledCustom("cmplx_FATCA_w8form",true);
			setNGValueCustom("cmplx_FATCA_w8form", false);
			setLockedCustom("cmplx_FATCA_w9form",false);
			setEnabledCustom("cmplx_FATCA_w9form",true);
			setNGValueCustom("cmplx_FATCA_w9form", false);
			setLockedCustom("cmplx_FATCA_lossofnationality",false);
			setEnabledCustom("cmplx_FATCA_lossofnationality",true);
			setNGValueCustom("cmplx_FATCA_lossofnationality", false);
			setEnabledCustom("cmplx_FATCA_TINNo",true);
			setLockedCustom("cmplx_FATCA_TINNo",false);
			setNGValueCustom("cmplx_FATCA_TINNo","");
			setEnabledCustom("cmplx_FATCA_SignedDate",true);
			setLockedCustom("cmplx_FATCA_SignedDate",false);
			setNGValueCustom("cmplx_FATCA_SignedDate","");
			setEnabledCustom("cmplx_FATCA_ExpiryDate",true);
			setLockedCustom("cmplx_FATCA_ExpiryDate",false);
			setNGValueCustom("cmplx_FATCA_ExpiryDate","");
			setEnabledCustom("cmplx_FATCA_Category",true);
			setLockedCustom("cmplx_FATCA_Category",false);
			setNGValueCustom("cmplx_FATCA_Category","");
			//setEnabledCustom("cmplx_FATCA_ControllingPersonUSRel",true);
			setNGValueCustom("cmplx_FATCA_ControllingPersonUSRel","");
			setLockedCustom("cmplx_FATCA_ListedReason",false);
			setEnabledCustom("cmplx_FATCA_ListedReason",true);
			setNGValueCustom("cmplx_FATCA_ListedReason","");
			setLockedCustom("cmplx_FATCA_SelectedReason",false);
			setEnabledCustom("cmplx_FATCA_SelectedReason",true);
			setNGValueCustom("cmplx_FATCA_SelectedReason","");
			setLockedCustom("FATCA_Button1",false);
			setLockedCustom("FATCA_Button2",false);
			setLockedCustom("cmplx_FATCA_ControllingPersonUSRel",false);
			setEnabledCustom("cmplx_FATCA_ControllingPersonUSRel",true);
			setNGValueCustom("cmplx_FATCA_ControllingPersonUSRel","");
	}
	//Changed by aman on 22/12
	
	
	
	function Fields_Liability()
	{
		var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(var i=0;i<n;i++){
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="BTC" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
					setVisible("Button2",false);
					setVisible("Label9",false);
					setVisible("Liability_New_Label21",false); 
					setVisible("Liability_New_Label14",false);
					setVisible("cmplx_Liability_New_DBR",false);
					setVisible("cmplx_Liability_New_DBRNet",false); 
					setVisible("Label15",false);
					setVisible("cmplx_Liability_New_TAI",false); 
					setLeft('cmplx_Liability_New_AggrExposure',"24px");
					setLeft('Liability_New_Label15',"24px");
					break;
				}
				else{
					setVisible("Button2",true);
					setVisible("Label9",true);
					setVisible("Liability_New_Label21",true); 
					setVisible("Liability_New_Label14",true);
					setVisible("cmplx_Liability_New_DBR",true);
					setVisible("cmplx_Liability_New_DBRNet",true); 
					setVisible("Label15",true);
					setVisible("cmplx_Liability_New_TAI",true); 
					setLeft('cmplx_Liability_New_AggrExposure',"1078px");
					setLeft('Liability_New_Label15',"1078px");
				}	
			}
		}
		else{
			setVisible("Button2",true);
			setVisible("Label9",true);
			setVisible("Liability_New_Label21",true); 
			setVisible("Liability_New_Label14",true);
			setVisible("cmplx_Liability_New_DBR",true);
			setVisible("cmplx_Liability_New_DBRNet",true); 
			setVisible("Label15",true);
			setVisible("cmplx_Liability_New_TAI",true); 
			setLeft('cmplx_Liability_New_AggrExposure','1078px');
			setLeft('Liability_New_Label15','1078px');
		}	
		
	}
	
		function Fields_ApplicationType_Eligibility()
	{

				if(getNGValue("Application_Type")=="Top up"){
					setLockedCustom("cmplx_EligibilityAndProductInfo_NetPayout",false); 
					setVisible("ELigibiltyAndProductInfo_Label1",false);
					setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
					setVisible("ELigibiltyAndProductInfo_Label2",false);
					setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
				}
				
				else if(getNGValue("Application_Type")=="TKOE" || getNGValue('Application_Type')=="TKON")
				{
					setLockedCustom("cmplx_EligibilityAndProductInfo_NetPayout",true); 
					setVisible("ELigibiltyAndProductInfo_Label1",true);
					setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
					setVisible("ELigibiltyAndProductInfo_Label2",true);
					setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);

				}
				else{
					setLockedCustom("cmplx_EligibilityAndProductInfo_NetPayout",true); 
					setVisible("ELigibiltyAndProductInfo_Label1",false);
					setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
					setVisible("ELigibiltyAndProductInfo_Label2",false);
					setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
				}
	}
	
	
	function  Fields_Eligibility()
	{
						
				if(getNGValue('PrimaryProduct')=="Personal Loan" ){
					setVisible("ELigibiltyAndProductInfo_Label39",true);
					setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",true);
				
					if(getNGValue('Application_Type')=="TKOE" || getNGValue('Application_Type')=="TKON")
						{
							setVisible("ELigibiltyAndProductInfo_Label1",true);
							setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
							setVisible("ELigibiltyAndProductInfo_Label2",true);
							setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
						}
				}
				
				else{
					setVisible("ELigibiltyAndProductInfo_Label39",false);
					setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
					
					if(getNGValue('Subproduct_productGrid')=="IM" ){
						setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",true); //fianl tai
						setVisible("ELigibiltyAndProductInfo_Label41",true);
						setVisible("ELigibiltyAndProductInfo_Label8",false);
						setVisible("ELigibiltyAndProductInfo_Label2",false);
						setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
						setVisible("cmplx_EligibilityAndProductInfo_Moratorium",false);
						setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",false); 
						setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
						setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
						setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
						setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
						setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
						setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
						setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
						setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
						setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
						setVisible("ELigibiltyAndProductInfo_Label31",false);
						setVisible("ELigibiltyAndProductInfo_Label15",false);
						setVisible("ELigibiltyAndProductInfo_Label28",false);
						setVisible("ELigibiltyAndProductInfo_Label17",false);
						//setVisible("ELigibiltyAndProductInfo_Label10",false);
						setVisible("ELigibiltyAndProductInfo_Label16",false); 
						setVisible("ELigibiltyAndProductInfo_Label29",false);
						setVisible("ELigibiltyAndProductInfo_Label18",false);
						setVisible("ELigibiltyAndProductInfo_Label33",false);
						setVisible("ELigibiltyAndProductInfo_Label30",false);
						setVisible("ELigibiltyAndProductInfo_Label32",false);
						setVisible("ELigibiltyAndProductInfo_Label19",false);	
						setVisible("ELigibiltyAndProductInfo_Label35",false);
						setVisible("ELigibiltyAndProductInfo_Label20",false);
						setVisible("ELigibiltyAndProductInfo_Label36",false);
						setVisible("ELigibiltyAndProductInfo_Label21",false);
						//setVisible("ELigibiltyAndProductInfo_Label37",false);
						setVisible("ELigibiltyAndProductInfo_Label22",false);
						//setVisible("ELigibiltyAndProductInfo_Label38",false);
						
					}			
											
					else if(getNGValue('Subproduct_productGrid')=="SAL"){
						//Eligibility_Hide();
						setVisible("ELigibiltyAndProductInfo_Label41",true); 
						setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",true); //Final TAI
						//Deepak Below 2 lines are commented by deepak on 21 Oct 2018 for Total Final Limit/IM Amount
						//setLeft('cmplx_EligibilityAndProductInfo_FinalLimit','553px');
						//setTop('cmplx_EligibilityAndProductInfo_FinalLimit','831px');	
					}
					
					else if(getNGValue('Subproduct_productGrid')=="BTC" ){
						
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",false);
						setVisible("ELigibiltyAndProductInfo_Label3",false);
						//Eligibility_Hide();			
						setLeft('cmplx_EligibilityAndProductInfo_FinalLimit','24px');
						setLeft('ELigibiltyAndProductInfo_Label4','24px');
						
					}
					else if(getNGValue('Subproduct_productGrid')=="LI" || getNGValue('Subproduct_productGrid')=="SAL"){
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
						setVisible("ELigibiltyAndProductInfo_Label3",true);
						//Eligibility_Hide();
						
					}
					else if(getNGValue('Subproduct_productGrid')=="PU" ){
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
						setVisible("ELigibiltyAndProductInfo_Label3",true);
						//Eligibility_Hide();
					}	
					else{
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
						setVisible("ELigibiltyAndProductInfo_Label3",true);
						setVisible("ELigibiltyAndProductInfo_Label39",true);
						setVisible("ELigibiltyAndProductInfo_Label40",true);
						setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",true);
						//Eligibility_UnHide();
					}
					//below code commented by nikhil for proc-3249
					//Eligibility_UnHide();
					elig_fields();
			}
	}	
			

	function  Fields_ServiceRequest()
	{
		
			if(getLVWAT("Subproduct_productGrid")=="IM" ){
				setVisible("CC_Loan_Frame3",false); 
				setVisible("CC_Loan_Frame5",true); 
			}
			else if(getLVWAT("Subproduct_productGrid").indexOf("BTC")>-1 ){
				setVisible("CC_Loan_Frame3",false);
				setVisible("CC_Loan_Frame5",true); 					
				
			}
			else if(getLVWAT("Subproduct_productGrid")=="LI" ){
				setVisible("CC_Loan_Frame5",false); 
				setVisible("CC_Loan_Frame3",false); 
			
			}
			else if(getLVWAT("Subproduct_productGrid")=="PU" ){
				setVisible("CC_Loan_Frame5",false); 
				setVisible("CC_Loan_Frame3",false); 
			}
			
			else{
				setVisible("CC_Loan_Frame5",true); 
				setVisible("CC_Loan_Frame3",true); 
			}
	}	
	
	function SetValueCustomer_ReadCard(){
		window.parent.setPopupMask();
		CreateIndicator("application");	
		var userData = { 
			appCode: 'CAS',
			recordReference: (window.parent.pid).substring(5,14)+Math.floor((Math.random()*90)+10),
			useCaseCode: 'CAS001',
			userId: 'deepak'
		}
		$.ajax({
			url : eidPluginBaseURL+"card-data",
			type : 'POST',
			data: JSON.stringify(userData),
			async: true,
			dataType: 'json',
			success : function(data, textStatus, jQxhr) {
					RemoveIndicator("application");
					window.parent.hidePopupMask();
					var dataobj = data.cardData;		
					setNGValueCustom('cmplx_Customer_EmiratesID',dataobj.idNumber);
					setNGValueCustom('cmplx_Customer_IdIssueDate',dataobj.emiratesIdIssueDate);
					setNGValueCustom('cmplx_Customer_EmirateIDExpiry',dataobj.emiratesIdExpiryDate);
					setNGValueCustom('cmplx_Customer_Title',dataobj.title);
					lastAccessToken = data.config.accessToken;
					var full_name  = dataobj.fullNameEnglish;
						if(full_name.indexOf(',')>-1){
                                var full_name_arr = full_name.split(',');
                                if(full_name_arr.length ==5){
                                                var first_name = full_name_arr[0];
                                                var middle_name = full_name_arr[1];
                                                var last_name = full_name_arr[4];
                                                setNGValueCustom("cmplx_Customer_FIrstNAme",first_name.toUpperCase());//Deepak Changes done to set value from card into upper case.
                                                setNGValueCustom("cmplx_Customer_MiddleName",middle_name.toUpperCase());
                                                setNGValueCustom("cmplx_Customer_LAstNAme",last_name.toUpperCase());
                                                }
                                
                                }
								 else{
									setNGValueCustom("cmplx_Customer_FIrstNAme",full_name.toUpperCase());
								}
								
					setNGValueCustom('cmplx_Customer_gender',dataobj.gender);
					setNGValueCustom('cmplx_Customer_DOb',dataobj.dateOfBirth);
					setNGValueCustom('cmplx_Customer_PAssportNo',dataobj.passportNumber);
					setNGValueCustom('cmplx_Customer_VisaNo',dataobj.visaNumber);
					setNGValueCustom('cmplx_Customer_VIsaExpiry',dataobj.visaExpiryDate);
					setNGValueCustom('cmplx_Customer_Nationality',get_EID_nationality(dataobj.nationalityCode));
					setNGValueCustom('cmplx_Customer_MobNo',dataobj.mobileNumber);
					setNGValueCustom('cmplx_Customer_MotherName',dataobj.motherFullName);
					if(validateFutureDate('cmplx_Customer_DOb')){
						var age=calcAge(getNGValue('cmplx_Customer_DOb'),'');
						YYMM('','cmplx_Customer_age',age);
					}
					
					checkCardGenuineStatus();
					EidaS_lockfields();
					
			},
			error : function(jqXhr, textStatus, errorThrown) {
				RemoveIndicator("application");
				window.parent.hidePopupMask();
				if(jqXhr.readyState==0){
					alert('Please check local EIDA server is down! Kindly start the same and then try again.');
				}
				else{
					var Erroobj = JSON.parse(jqXhr.responseText);	
					alert('Error whhile reading info from card: '+ Erroobj.errorDesc);
				}
			}
		});	
	}
	
	function get_EID_nationality(code_3){
		var national_code = '{"AFG":"AF","ALB":"AL","DZA":"DZ","ASM":"AS","AND":"AD","AGO":"AO","AIA":"AI","ATA":"AQ","ATG":"AG","ARG":"AR","ARM":"AM","ABW":"AW","AUS":"AU","AUT":"AT","AZE":"AZ","BHS":"BS","BHR":"BH","BGD":"BD","BRB":"BB","BLR":"BY","BEL":"BE","BLZ":"BZ","BEN":"BJ","BMU":"BM","BTN":"BT","BOL":"BO","BES":"BQ","BIH":"BA","BWA":"BW","BVT":"BV","BRA":"BR","IOT":"IO","BRN":"BN","BGR":"BG","BFA":"BF","BDI":"BI","KHM":"KH","CMR":"CM","CAN":"CA","CPV":"CV","CYM":"KY","CAF":"CF","TCD":"TD","CHL":"CL","CHN":"CN","CXR":"CX","CCK":"CC","COL":"CO","COM":"KM","COG":"CG","COD":"CD","COK":"CK","CRI":"CR","HRV":"HR","CUB":"CU","CUW":"CW","CYP":"CY","CZE":"CZ","CIV":"CI","DNK":"DK","DJI":"DJ","DMA":"DM","DOM":"DO","ECU":"EC","EGY":"EG","SLV":"SV","GNQ":"GQ","ERI":"ER","EST":"EE","ETH":"ET","FLK":"FK","FRO":"FO","FJI":"FJ","FIN":"FI","FRA":"FR","GUF":"GF","PYF":"PF","ATF":"TF","GAB":"GA","GMB":"GM","GEO":"GE","DEU":"DE","GHA":"GH","GIB":"GI","GRC":"GR","GRL":"GL","GRD":"GD","GLP":"GP","GUM":"GU","GTM":"GT","GGY":"GG","GIN":"GN","GNB":"GW","GUY":"GY","HTI":"HT","HMD":"HM","VAT":"VA","HND":"HN","HKG":"HK","HUN":"HU","ISL":"IS","IND":"IN","IDN":"ID","IRN":"IR","IRQ":"IQ","IRL":"IE","IMN":"IM","ISR":"IL","ITA":"IT","JAM":"JM","JPN":"JP","JEY":"JE","JOR":"JO","KAZ":"KZ","KEN":"KE","KIR":"KI","PRK":"KP","KOR":"KR","KWT":"KW","KGZ":"KG","LAO":"LA","LVA":"LV","LBN":"LB","LSO":"LS","LBR":"LR","LBY":"LY","LIE":"LI","LTU":"LT","LUX":"LU","MAC":"MO","MKD":"MK","MDG":"MG","MWI":"MW","MYS":"MY","MDV":"MV","MLI":"ML","MLT":"MT","MHL":"MH","MTQ":"MQ","MRT":"MR","MUS":"MU","MYT":"YT","MEX":"MX","FSM":"FM","MDA":"MD","MCO":"MC","MNG":"MN","MNE":"ME","MSR":"MS","MAR":"MA","MOZ":"MZ","MMR":"MM","NAM":"NA","NRU":"NR","NPL":"NP","NLD":"NL","NCL":"NC","NZL":"NZ","NIC":"NI","NER":"NE","NGA":"NG","NIU":"NU","NFK":"NF","MNP":"MP","NOR":"NO","OMN":"OM","PAK":"PK","PLW":"PW","PSE":"PS","PAN":"PA","PNG":"PG","PRY":"PY","PER":"PE","PHL":"PH","PCN":"PN","POL":"PL","PRT":"PT","PRI":"PR","QAT":"QA","ROU":"RO","RUS":"RU","RWA":"RW","REU":"RE","BLM":"BL","SHN":"SH","KNA":"KN","LCA":"LC","MAF":"MF","SPM":"PM","VCT":"VC","WSM":"WS","SMR":"SM","STP":"ST","SAU":"SA","SEN":"SN","SRB":"RS","SYC":"SC","SLE":"SL","SGP":"SG","SXM":"SX","SVK":"SK","SVN":"SI","SLB":"SB","SOM":"SO","ZAF":"ZA","SGS":"GS","SSD":"SS","ESP":"ES","LKA":"LK","SDN":"SD","SUR":"SR","SJM":"SJ","SWZ":"SZ","SWE":"SE","CHE":"CH","SYR":"SY","TWN":"TW","TJK":"TJ","TZA":"TZ","THA":"TH","TLS":"TL","TGO":"TG","TKL":"TK","TON":"TO","TTO":"TT","TUN":"TN","TUR":"TR","TKM":"TM","TCA":"TC","TUV":"TV","UGA":"UG","UKR":"UA","ARE":"AE","GBR":"GB","USA":"US","UMI":"UM","URY":"UY","UZB":"UZ","VUT":"VU","VEN":"VE","VNM":"VN","VGB":"VG","VIR":"VI","WLF":"WF","ESH":"EH","YEM":"YE","ZMB":"ZM","ZWE":"ZW","ALA":"AX"}';

	var natio_code = JSON.parse(national_code);
	return natio_code[code_3];
}

  
  function checkCardGenuineStatus() {
	var CardGenuine = {
		accessToken: lastAccessToken
	};

	window.parent.setPopupMask();
    CreateIndicator("application");	
	
	var ajaxReq = $.ajax({
		url : eidPluginBaseURL+"card-status?t="+new Date(),		
		dataType : 'json',
		timeout: 60*1000,
		type : 'post',
		contentType : 'application/json',
		async: true,
		data : JSON.stringify(CardGenuine),
		success : function(data, textStatus, jQxhr) {
			RemoveIndicator("application");
            window.parent.hidePopupMask();
			setNGValueCustom("Is_EID_Genuine", "Y");
			setNGValueCustom("cmplx_Customer_IsGenuine", true);
			alert('The card scanned is a genuine card!');
		},
		error : function(error) {
			RemoveIndicator("application");
            window.parent.hidePopupMask();
			if(error.responseText!=undefined){
				var responseobj = JSON.parse(error.responseText)
				alert('Card Genuine check failed: '+responseobj.errorDesc+' !');
			}else{
				alert('check Card status error : Please check local EIDA server is down! Kindly start the same and then try again.');
			}
			setNGValueCustom("Is_EID_Genuine", "N");
			
			
		}
	});
}
  function EidaS_lockfields(){
		setLockedCustom("FetchDetails", false);	
		setLockedCustom("ReadFromCard", true);
		setLockedCustom("cmplx_Customer_FIrstNAme",false);
		setLockedCustom("cmplx_Customer_MiddleName",false);
		setLockedCustom("cmplx_Customer_LAstNAme",false);
		setLockedCustom("cmplx_Customer_DOb",false);
		setLockedCustom("cmplx_Customer_PAssportNo",false);
		setLockedCustom("cmplx_Customer_Nationality",false);
		setLockedCustom("cmplx_Customer_MobNo",false);
  }
  
  function SetValueCustomer_CheckEligibility(){
		setNGValueCustom("cmplx_EligibilityAndProductInfo_FinalDBR","30");
	//	setNGValueCustom("cmplx_EligibilityAndProductInfo_FinalTAI","4500");
	//	setNGValueCustom("cmplx_EligibilityAndProductInfo_InterestRate","12");
	//	setNGValueCustom("cmplx_EligibilityAndProductInfo_EMI","3000");
		//setNGValueCustom("cmplx_EligibilityAndProductInfo_Tenor","4");
	//	setNGValueCustom("cmplx_EligibilityAndProductInfo_FinalLimit","4");
		setNGValueCustom("cmplx_EligibilityAndProductInfo_NetPayout","4");
		var Tenor= getNGValue('cmplx_EligibilityAndProductInfo_Tenor');
		var tenorinyears=Tenor/12;
		var age= getNGValue('cmplx_Customer_age');
		var totalage= parseInt(age) + parseInt(tenorinyears);
		ageatmaturity();

		
  }
  
 function SetValueCustomer_FetchDetails(){
		
		setNGValueCustom("cmplx_Customer_Title","Mr");
		setNGValueCustom("cmplx_Customer_LastNAme2","AL Habib");
		setNGValueCustom("cmplx_Customer_ResidentNonResident","Resident");
		setNGValueCustom("cmplx_Customer_gender","Male");
		setNGValueCustom("cmplx_Customer_MotherName","Rubeena");
		setNGValueCustom("cmplx_Customer_VisaNo","VISA007");
		setNGValueCustom("cmplx_Customer_MAritalStatus","Married");
		setNGValueCustom("cmplx_Customer_COuntryOFResidence","UAE");
		//setNGValueCustom("cmplx_Customer_SecNAtionApplicable","Yes");
		setNGValueCustom("cmplx_Customer_SecNationality","Indian");
		setNGValueCustom("cmplx_Customer_Third_Nationaity","Austria");
		setNGValueCustom("cmplx_Customer_EmirateIDExpiry","12/01/1990");
		setNGValueCustom("cmplx_Customer_IdIssueDate","12/01/1986");
		setNGValueCustom("cmplx_Customer_PassPortExpiry","12/01/1990");
		setNGValueCustom("cmplx_Customer_VIsaExpiry","12/01/1986");
		setNGValueCustom("cmplx_Customer_GCCNational","Yes");
		setNGValueCustom("cmplx_Customer_VIPFlag",true);
		setNGValueCustom("cmplx_Customer_EMirateOfVisa","Dubai");
		setNGValueCustom("cmplx_Customer_EmirateOfResidence","Dubai");
		setNGValueCustom("cmplx_Customer_yearsInUAE","1");
		setNGValueCustom("cmplx_Customer_CustomerCategory","Gold");
		
		setLockedCustom("cmplx_Customer_MotherName",false);
		setLockedCustom("cmplx_Customer_VisaNo",false);
		setLockedCustom("cmplx_Customer_MAritalStatus",false);
		setLockedCustom("cmplx_Customer_COuntryOFResidence",false);
		setLockedCustom("cmplx_Customer_SecNAtionApplicable",false);
		setLockedCustom("cmplx_Customer_SecNationality",false);
		setLockedCustom("cmplx_Customer_Third_Nationaity_Applicable",false);
		setLockedCustom("cmplx_Customer_Third_Nationaity",false);
		setLockedCustom("cmplx_Customer_EmirateIDExpiry",false);
		setLockedCustom("cmplx_Customer_IdIssueDate",false);
		setLockedCustom("cmplx_Customer_PassPortExpiry",false);
		setLockedCustom("cmplx_Customer_VIsaExpiry",false);
		setLockedCustom("cmplx_Customer_GCCNational",false);
		setEnabledCustom("cmplx_Customer_VIPFlag",true);
		setLockedCustom("cmplx_Customer_EMirateOfVisa",false);
		setLockedCustom("cmplx_Customer_EmirateOfResidence",false);
		setLockedCustom("cmplx_Customer_yearsInUAE",false);
		setLockedCustom("cmplx_Customer_CustomerCategory",false);
	}
	
	function setBlank_Customer()
	{
		var fields="cmplx_Customer_EmiratesID,cmplx_Customer_LAstNAme,cmplx_Customer_DOb,cmplx_Customer_MobNo,cmplx_Customer_PAssportNo,cmplx_Customer_Nationality,cmplx_Customer_ResidentNonResident,cmplx_Customer_EIDARegNo,cmplx_Customer_CIFNO,cmplx_Customer_marsoomID,cmplx_Customer_Title,cmplx_Customer_FIrstNAme,cmplx_Customer_MiddleName,cmplx_Customer_gender,cmplx_Customer_MotherName,cmplx_Customer_VisaNo,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_age,cmplx_Customer_SecNationality,cmplx_Customer_Third_Nationaity,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_EMirateOfVisa,cmplx_Customer_VIPFlag,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_PassportIssueDate,cmplx_Customer_VisaIssuedate"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
		//change by saurabh for security condition
			if(getNGValue(field_array[i])!='--Select--' && getNGValue(field_array[i])!=false)
				setNGValueCustom(field_array[i],"");
		//setNGValueCustom('cmplx_Customer_card_id_available',false);
	}	
	
	function SetEnableCustomer()
	{
		var fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_MotherName,cmplx_Customer_VisaNo,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_VIPFlag,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIsaExpiry,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_PassportIssueDate,cmplx_Customer_PassPortExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_VisaIssuedate"; 

		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setLockedCustom(field_array[i], false);

		setEnabledCustom('cmplx_Customer_VIPFlag',true);
		setEnabledCustom('cmplx_Customer_SecNAtionApplicable',true);
	}
		
	function SetDisableCustomer()
	{
		var fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_age,cmplx_Customer_MotherName,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_VisaNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_SecNationality,cmplx_Customer_Third_Nationaity,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_PassportIssueDate,cmplx_Customer_VisaIssuedate"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setLockedCustom(field_array[i], true);
		setEnabledCustom('cmplx_Customer_VIPFlag',false);
		setEnabledCustom('cmplx_Customer_SecNAtionApplicable',false);	
	}
	
	function validateApprovedLimitSupplementary(){
		if(isVisible('SupplementCardDetails_Frame1')){
			var lsitViewname = 'SupplementCardDetails_cmplx_SupplementGrid';
			var suppRowCount = getLVWRowCount(lsitViewname);
			if(suppRowCount>0){
				var cardProdLimit = {};
				for(var i=0;i<suppRowCount;i++){
					cardProdLimit[getLVWAT(lsitViewname,i,22)] = getLVWAT(lsitViewname,i,26);
				}
				for(var key in cardProdLimit){
					var gridValue = cardProdLimit[key];
					var currentFinalLimit = '';
					setNGValueCustom('SupplementCardDetails_CardProduct',key);
					var combotext = com.newgen.omniforms.formviewer.getNGSelectedItemText('SupplementCardDetails_CardProduct');
					if(combotext.indexOf('(')>-1){
						var temp = combotext.split('(');
						currentFinalLimit = temp[temp.length-1].substr(0,(temp[temp.length-1]).length - 1);
						var acceptableValue = parseFloat(currentFinalLimit)*(1);//Deepak changes done to remove validation
						if(parseFloat(gridValue)>acceptableValue){
							showAlert(lsitViewname,'The approved limit: '+gridValue+' for the Card Product: '+key+' cannot be greater than 80% of the Final Limit');	
							return false;
						}
					}
				}
				return true;
			}
		}
		return true;
	}
	
	function validateBlanksuppCardProds(){
		if(isVisible('SupplementCardDetails_Frame1')){
			var lsitViewname = 'SupplementCardDetails_cmplx_SupplementGrid';
			var suppRowCount = getLVWRowCount(lsitViewname);
			if(suppRowCount>0){
				for(var i=0;i<suppRowCount;i++){
					//cardProdLimit[] = getLVWAT(lsitViewname,i,26);
					if(getLVWAT(lsitViewname,i,22)==null || getLVWAT(lsitViewname,i,22)=='' || getLVWAT(lsitViewname,i,22)==' '){
						showAlert(lsitViewname,'Please select a Card Product for customer : '+getLVWAT(lsitViewname,i,0)+' '+getLVWAT(lsitViewname,i,2));	
						return false;
					}
				}
				return true;
			}
		}
		return true;
	}
	
function Eligibility_Hide()
{	//alert('Inside hide function');
	setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",false);
	setVisible("ELigibiltyAndProductInfo_Label41",false);
	setVisible("cmplx_EligibilityAndProductInfo_InterestRate",false); 
	setVisible("cmplx_EligibilityAndProductInfo_EMI",false);
	setVisible("cmplx_EligibilityAndProductInfo_Tenor",false);
	setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
	setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",false);
	setVisible("cmplx_EligibilityAndProductInfo_Moratorium",false);
	setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",false);	
	setVisible("cmplx_EligibilityAndProductInfo_InterestType",false);
	setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",false); 
	setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
	setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
	setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
	setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",false);
	setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",false);
	setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
	setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
	setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
	setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
	setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
	setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
	setVisible("ELigibiltyAndProductInfo_Label5",false);
	setVisible("ELigibiltyAndProductInfo_Label6",false); 
	setVisible("ELigibiltyAndProductInfo_Label7",false);
	setVisible("ELigibiltyAndProductInfo_Label8",false);
	setVisible("ELigibiltyAndProductInfo_Label14",false);
	setVisible("ELigibiltyAndProductInfo_Label12",false);
	setVisible("ELigibiltyAndProductInfo_Label13",false);
	setVisible("ELigibiltyAndProductInfo_Label15",false);	
	setVisible("ELigibiltyAndProductInfo_Label17",false);
	setVisible("ELigibiltyAndProductInfo_Label16",false);
	setVisible("ELigibiltyAndProductInfo_Label18",false);
	setVisible("ELigibiltyAndProductInfo_Label23",false);
	setVisible("ELigibiltyAndProductInfo_Label13",false);
	setVisible("ELigibiltyAndProductInfo_Label19",false);	
	setVisible("ELigibiltyAndProductInfo_Label11",false);
	setVisible("ELigibiltyAndProductInfo_Label20",false);
	setVisible("ELigibiltyAndProductInfo_Label21",false);
	setVisible("ELigibiltyAndProductInfo_Label22",false);	
	setVisible("ELigibiltyAndProductInfo_Label24",false);
	setVisible("ELigibiltyAndProductInfo_Label25",false);
	setVisible("ELigibiltyAndProductInfo_Label9",false);
	setVisible("ELigibiltyAndProductInfo_Label27",false);
	setVisible("ELigibiltyAndProductInfo_Label28",false);	
	setVisible("ELigibiltyAndProductInfo_Label10",false);
	setVisible("ELigibiltyAndProductInfo_Label29",false);	
	setVisible("ELigibiltyAndProductInfo_Label33",false);
	setVisible("ELigibiltyAndProductInfo_Label34",false);
	setVisible("ELigibiltyAndProductInfo_Label26",false);
	setVisible("ELigibiltyAndProductInfo_Label31",false);
	setVisible("ELigibiltyAndProductInfo_Label30",false);
	setVisible("ELigibiltyAndProductInfo_Label32",false);
	setVisible("ELigibiltyAndProductInfo_Label35",false);	
	setVisible("ELigibiltyAndProductInfo_Label36",false);
	setVisible("ELigibiltyAndProductInfo_Label37",false);
	setVisible("ELigibiltyAndProductInfo_Label38",false);	
}
	
	function Eligibility_UnHide()
{
	setVisible("ELigibiltyAndProductInfo_Text7",true); //fianl tai
	setVisible("ELigibiltyAndProductInfo_Label41",true);
	setVisible("cmplx_EligibilityAndProductInfo_InterestRate",true); 
	setVisible("cmplx_EligibilityAndProductInfo_EMI",true);
	setVisible("cmplx_EligibilityAndProductInfo_Tenor",true);
	setVisible("cmplx_EligibilityAndProductInfo_NetPayout",true);
	//setVisible("ELigibiltyAndProductInfo_Button1",true);
	setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",true);
	setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",true);	
	setVisible("cmplx_EligibilityAndProductInfo_Moratorium",true);
	setVisible("cmplx_EligibilityAndProductInfo_InterestType",true);
	setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",true); 
	setVisible("cmplx_EligibilityAndProductInfo_MArginRate",true);
	setVisible("cmplx_EligibilityAndProductInfo_BAseRate",true);
	setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
	setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",true);
	setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",true);
	setVisible("cmplx_EligibilityAndProductInfo_LPF",true);	
	setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",true);
	setVisible("cmplx_EligibilityAndProductInfo_Insurance",true);
	setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
	setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",true);
	setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
	setVisible("ELigibiltyAndProductInfo_Label5",true);
	setVisible("ELigibiltyAndProductInfo_Label6",true); 
	setVisible("ELigibiltyAndProductInfo_Label7",true);
	setVisible("ELigibiltyAndProductInfo_Label8",true);
	setVisible("ELigibiltyAndProductInfo_Label14",true);
	setVisible("ELigibiltyAndProductInfo_Label12",true);
	setVisible("ELigibiltyAndProductInfo_Label13",true);
	setVisible("ELigibiltyAndProductInfo_Label15",true);	
	setVisible("ELigibiltyAndProductInfo_Label17",true);
	setVisible("ELigibiltyAndProductInfo_Label16",true);
	setVisible("ELigibiltyAndProductInfo_Label18",true);
	setVisible("ELigibiltyAndProductInfo_Label23",true);
	setVisible("ELigibiltyAndProductInfo_Label13",true);
	setVisible("ELigibiltyAndProductInfo_Label19",true);	
	setVisible("ELigibiltyAndProductInfo_Label11",true);
	setVisible("ELigibiltyAndProductInfo_Label20",true);
	setVisible("ELigibiltyAndProductInfo_Label21",true);
	setVisible("ELigibiltyAndProductInfo_Label22",true);	
	setVisible("ELigibiltyAndProductInfo_Label24",true);
	setVisible("ELigibiltyAndProductInfo_Label25",true);
	setVisible("ELigibiltyAndProductInfo_Label9",true);
	setVisible("ELigibiltyAndProductInfo_Label27",true);
	setVisible("ELigibiltyAndProductInfo_Label28",true);	
	setVisible("ELigibiltyAndProductInfo_Label10",true);
	setVisible("ELigibiltyAndProductInfo_Label29",true);
	setVisible("ELigibiltyAndProductInfo_Label31",true);
	setVisible("ELigibiltyAndProductInfo_Label30",true);
	setVisible("ELigibiltyAndProductInfo_Label32",true);	
	setVisible("ELigibiltyAndProductInfo_Label33",true);
	setVisible("ELigibiltyAndProductInfo_Label34",true);
	setVisible("ELigibiltyAndProductInfo_Label26",true);
	setVisible("ELigibiltyAndProductInfo_Label35",true);	
	setVisible("ELigibiltyAndProductInfo_Label36",true);
	setVisible("ELigibiltyAndProductInfo_Label37",true);
	setVisible("ELigibiltyAndProductInfo_Label38",true);	
}

function elig_fields(){
if("SE"==(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2)) || ("BTC"==(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2))) || ("Self Employed"==(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)))){
			setVisible("cmplx_EligibilityAndProductInfo_FinalDBR", false);//cmplx_EligibilityAndProductInfo_FinalDBR
			setVisible("ELigibiltyAndProductInfo_Label3", false);
			setVisible("ELigibiltyAndProductInfo_Label41", false);
			setVisible("cmplx_EligibilityAndProductInfo_FinalTAI", false);

			setVisible("ELigibiltyAndProductInfo_Label5", false);
			setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
			setVisible("ELigibiltyAndProductInfo_Label6", false);
			setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
			setVisible("ELigibiltyAndProductInfo_Label7", false);
			setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
			setVisible("ELigibiltyAndProductInfo_Label8", false);
			setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
			setVisible("ELigibiltyAndProductInfo_Label2", false);
			setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
			setVisible("ELigibiltyAndProductInfo_Label1", false);
			setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
			setVisible("ELigibiltyAndProductInfo_Label39", false);
			setVisible("cmplx_EligibilityAndProductInfo_instrumenttype", false);
			setVisible("ELigibiltyAndProductInfo_Label11", false);

			setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
			setVisible("ELigibiltyAndProductInfo_Label39", false);
			setVisible("ELigibiltyAndProductInfo_Label39", false);
			setVisible("ELigibiltyAndProductInfo_Label31", false);
			setVisible("cmplx_EligibilityAndProductInfo_Moratorium", false);
			setVisible("ELigibiltyAndProductInfo_Label12", false);
			setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
			setVisible("ELigibiltyAndProductInfo_Label14", false);
			setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
			setVisible("ELigibiltyAndProductInfo_Label15", false);
			setVisible("cmplx_EligibilityAndProductInfo_BaseRateType", false);
			setVisible("ELigibiltyAndProductInfo_Label17", false);
			setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
			setVisible("ELigibiltyAndProductInfo_Label16",false); 
			setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
			setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
			setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
			setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
			setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
			setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
			setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
			setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
			setVisible("ELigibiltyAndProductInfo_Label18",false);
			setVisible("ELigibiltyAndProductInfo_Label23", false);
			setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			setVisible("ELigibiltyAndProductInfo_Label13", false);
			setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
			setVisible("ELigibiltyAndProductInfo_Label30",false);
			setVisible("ELigibiltyAndProductInfo_Label32",false);
			setVisible("ELigibiltyAndProductInfo_Label19",false);
			setVisible("ELigibiltyAndProductInfo_Label20",false);
			setVisible("ELigibiltyAndProductInfo_Label21",false);
			setVisible("ELigibiltyAndProductInfo_Label22",false);

			setTop("ELigibiltyAndProductInfo_Save", 290);
			setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
			setLeft("ELigibiltyAndProductInfo_Label4", 16);

			

		}
		else if(("Salaried"==(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6)) || "Salaried Pensioner"==(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6))) && !"IM"==(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,2))){
			if(!("Personal Loan"==(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,1)))){
				setVisible("ELigibiltyAndProductInfo_Label5", false);
				setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
				setVisible("ELigibiltyAndProductInfo_Label6", false);
				setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
				setVisible("ELigibiltyAndProductInfo_Label7", false);
				setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);


				setVisible("ELigibiltyAndProductInfo_Label8", false);
				setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
				setVisible("ELigibiltyAndProductInfo_Label2", false);
				setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
				setVisible("ELigibiltyAndProductInfo_Label1", false);
				setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
				setVisible("ELigibiltyAndProductInfo_Label39", false);
				setVisible("cmplx_EligibilityAndProductInfo_instrumenttype", false);
				setVisible("ELigibiltyAndProductInfo_Label11", false);

				setVisible("ELigibiltyAndProductInfo_Label11", false);
				setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
				setVisible("ELigibiltyAndProductInfo_Label39", false);
				setVisible("ELigibiltyAndProductInfo_Label39", false);
				setVisible("ELigibiltyAndProductInfo_Label31", false);
				setVisible("cmplx_EligibilityAndProductInfo_Moratorium", false);
				setVisible("ELigibiltyAndProductInfo_Label12", false);
				setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
				setVisible("ELigibiltyAndProductInfo_Label14", false);
				setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
				setVisible("ELigibiltyAndProductInfo_Label15", false);
				setVisible("cmplx_EligibilityAndProductInfo_BaseRateType", false);
				setVisible("ELigibiltyAndProductInfo_Label17", false);
				setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
				setVisible("ELigibiltyAndProductInfo_Label16",false); 
				setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
				setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
				setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
				setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
				setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
				setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
				setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
				setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
				setVisible("ELigibiltyAndProductInfo_Label18",false);
				setVisible("ELigibiltyAndProductInfo_Label23", false);
				setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
				setVisible("ELigibiltyAndProductInfo_Label13", false);
				setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
				setVisible("ELigibiltyAndProductInfo_Label30",false);
				setVisible("ELigibiltyAndProductInfo_Label32",false);
				setVisible("ELigibiltyAndProductInfo_Label19",false);
				setVisible("ELigibiltyAndProductInfo_Label20",false);
				setVisible("ELigibiltyAndProductInfo_Label21",false);
				setVisible("ELigibiltyAndProductInfo_Label22",false);

				setVisible("cmplx_EligibilityAndProductInfo_NetRate", false);
				setTop("ELigibiltyAndProductInfo_Save", 240);
			}
			else{
				//Shweta-vat changes
				setVisible("ELigibiltyAndProductInfo_Label24",true);
				setVisible("cmplx_EligibilityAndProductInfo_LoanProcessingVATPercent",true);
				setVisible("ELigibiltyAndProductInfo_Label25",true);
				setVisible("cmplx_EligibilityAndProductInfo_InsuranceVATPercent",true);
				setVisible("ELigibiltyAndProductInfo_Label26",true);
				setVisible("cmplx_EligibilityAndProductInfo_InsuranceVat",true);
				setVisible("ELigibiltyAndProductInfo_Label27",true);
				setVisible("cmplx_EligibilityAndProductInfo_LoanProcessingVat",true);

				if(getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).contains("TKO")){
					setVisible("ELigibiltyAndProductInfo_Label8",false);
					setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
					setVisible("ELigibiltyAndProductInfo_Label2",true);
					setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
					setVisible("ELigibiltyAndProductInfo_Label1",true);
					setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
				}
				else if(getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).contains("TOP")){
					setVisible("ELigibiltyAndProductInfo_Label2",false);
					setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
					setVisible("ELigibiltyAndProductInfo_Label1",false);
					setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
					setVisible("ELigibiltyAndProductInfo_Label8",true);
					setVisible("cmplx_EligibilityAndProductInfo_NetPayout",true);
				}
				else{
					setVisible("ELigibiltyAndProductInfo_Label8",false);
					setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
					setVisible("ELigibiltyAndProductInfo_Label2",false);
					setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
					setVisible("ELigibiltyAndProductInfo_Label1",false);
					setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
				}
			}
			//setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
			//setLeft("ELigibiltyAndProductInfo_Label5", 16);




		}
		else{
			setVisible("ELigibiltyAndProductInfo_Label8", false);
			setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
			setVisible("ELigibiltyAndProductInfo_Label39", false);
			setVisible("cmplx_EligibilityAndProductInfo_instrumenttype", false);
			setVisible("cmplx_EligibilityAndProductInfo_Moratorium", false);
			setVisible("ELigibiltyAndProductInfo_Label31", false);
			setVisible("ELigibiltyAndProductInfo_Label15", false);
			setVisible("cmplx_EligibilityAndProductInfo_BaseRateType", false);
			setVisible("ELigibiltyAndProductInfo_Label17", false);
			setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
			setVisible("ELigibiltyAndProductInfo_Label16",false); 
			setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
			setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
			setVisible("ELigibiltyAndProductInfo_Label18",false);
			setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
			setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
			setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
			setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
			setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
			setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
			setVisible("ELigibiltyAndProductInfo_Label30",false);
			setVisible("ELigibiltyAndProductInfo_Label32",false);
			setVisible("ELigibiltyAndProductInfo_Label19",false);
			setVisible("ELigibiltyAndProductInfo_Label20",false);
			setVisible("ELigibiltyAndProductInfo_Label21",false);
			setVisible("ELigibiltyAndProductInfo_Label22",false);

		}
}
//added for slowness issue
function rlos_click_parameterlist(pId)
	{
		var args='';
		switch (pId){
		case 'Address_Details_container': args='@this';break;	
		case 'Customer_save':		args = 'Customer_Frame1';	break;
		case 'Add':		args = 'Product_Frame1';	break;
		case 'Modify':		args = 'Product_Frame1';	break;
		case 'Delete':		args = 'Product_Frame1';	break;
		case 'Product_Save':		args = 'Product_Frame1';	break;
		case 'IncomeDetails_Salaried_Save':		args = 'IncomeDetails_Frame2';	break;
		case 'IncomeDetails_SelfEmployed_Save':		args = 'IncomeDetails_Frame3';	break;
		case 'IncomeDetails_Add':		args = 'IncomeDetails_Frame3';	break;
		case 'IncomeDetails_Modify':		args = 'IncomeDetails_Frame3';	break;
		case 'IncomeDetails_Delete':		args = 'IncomeDetails_Frame3';	break;
		case 'CompanyDetails_Add':		args = 'CompanyDetails';	break;
		case 'CompanyDetails_Modify':		args = 'CompanyDetails';	break;
		case 'CompanyDetails_delete':		args = 'CompanyDetails';	break;
		case 'CompanyDetails_save1':		args = 'CompanyDetails';	break;
		case 'AuthorisedSignDetails_add':		args = 'CompanyDetails';	break;
		case 'AuthorisedSignDetails_modify':		args = 'CompanyDetails';	break;
		case 'AuthorisedSignDetails_delete':		args = 'CompanyDetails';	break;
		case 'PartnerDetails_add':		args = 'CompanyDetails';	break;
		case 'PartnerDetails_modify':		args = 'CompanyDetails';	break;
		case 'PartnerDetails_delete':		args = 'CompanyDetails';	break;
		case 'CompanyDetails_save':		args = 'CompanyDetails';	break;
		case 'EMploymentDetails_Button2':		args = 'EMploymentDetails_Text21 EMploymentDetails_Text22';	break;
		case 'EMploymentDetails_Save':		args = 'EMploymentDetails_Frame1';	break;
		case 'Liability_New_Save':		args = 'Liability_New';	break;
		case 'Liability_New_add':		args = 'Liability_New_Frame4';	break;
		case 'Liability_New_modify':		args = 'Liability_New_Frame4';	break;
		case 'Liability_New_delete':		args = 'Liability_New_Frame4';	break;
		case 'ELigibiltyAndProductInfo_Save':		args = 'ELigibiltyAndProductInfo';	break;
		case 'cmplx_EligibilityAndProductInfo_Tenor':		args = 'ELigibiltyAndProductInfo';	break;
		case 'cmplx_EligibilityAndProductInfo_InterestRate':		args = 'ELigibiltyAndProductInfo';	break;
		case 'cmplx_EligibilityAndProductInfo_Moratorium':		args = 'ELigibiltyAndProductInfo';	break;
		case 'AddressDetails_addr_Add':		args = 'AddressDetails_Frame1';	break;
		case 'AddressDetails_addr_Modify':		args = 'AddressDetails_Frame1';	break;
		case 'AddressDetails_addr_Delete':		args = 'AddressDetails_Frame1';	break;
		case 'AddressDetails_Save':		args = 'AddressDetails_Frame1';	break;
		case 'ContactDetails_Save':		args = 'AltContactDetails_Frame1';	break;
		case 'CardDetails_save':		args = 'CardDetails_Frame1';	break;
		case 'SupplementCardDetails_add':		args = 'SupplementCardDetails_Frame1';	break;
		case 'SupplementCardDetails_modify':		args = 'SupplementCardDetails_Frame1';	break;
		case 'SupplementCardDetails_delete':		args = 'SupplementCardDetails_Frame1';	break;
		case 'SupplementCardDetails_Save':		args = 'SupplementCardDetails_Frame1';	break;	
		case 'ReferenceDetails_Reference_Add':		args = 'ReferenceDetails_Frame1';	break;	
		case 'ReferenceDetails_Reference__modify':		args = 'ReferenceDetails_Frame1';	break;	
		case 'ReferenceDetails_Reference_delete':		args = 'ReferenceDetails_Frame1';	break;	
		case 'ReferenceDetails_save':		args = 'ReferenceDetails_Frame1';	break;	
		case 'SupplementCardDetails_Save':		args = 'SupplementCardDetails_Frame1';	break;	
		case 'SupplementCardDetails_Save':		args = 'SupplementCardDetails_Frame1';	break;
		case 'SupplementCardDetails_Save':		args = 'SupplementCardDetails_Frame1';	break;
		case 'SupplementCardDetails_Save':		args = 'SupplementCardDetails_Frame1';	break;
		case 'SupplementCardDetails_Save':		args = 'SupplementCardDetails_Frame1';	break;		
		case 'ReadFromCIF':		args = 'GuarantorDetails';	break;		

		default: args='';  break;
		}
	return args;		
	}
