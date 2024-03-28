/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PLCommon.js
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : All js common custom functions 
-------------------------------------------------------------------------------------------------------
										CHANGE HISTORY
-------------------------------------------------------------------------------------------------------
Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

var alerts_String;
var alerts_String_Map={};
var visitSystemChecks_Flag=false;//added by akshay on 20/12/17
var check_bt_modify=false;

function PL_FORM_POPULATED(activityName){
	//alert('inside PL_FORM_POPULATED' + activityName);
	
	if(activityName=='SalesCoordinatorCSM' || activityName=='Initiation')
	{
		//alert('inside SalesCoordinatorCSM');
			$('#PersonalLoan :input').each(function(index, item){
			var  parent = $(this).parent().attr('id');
			//alert('inside parent' + parent);
			if(parent=='CustomerDetails'){	
				//alert('inside CustomerDetails');
			
					$(this).css('background-color', '#FFFFFF');
					$(this).attr('disabled', false);
			} else if($(this).prop('type')=='text' || $(this).prop('type')=='select-one' || $(this).prop('type')=='button' || $(this).prop('type')=='checkbox' || $(this).prop('type')=='textarea'){
				//alert('outside CustomerDetails');
					//com.newgen.omniforms.formviewer.setNGBackColor(item,'#C0C0C0');
					//com.newgen.omniforms.formviewer.setEnabled(item, false);
					$(this).css('background-color', '#C0C0C0');
					$(this).attr('disabled', true);
			}
		});
		}
}

//changes done by disha for new generate template socket on 11-Sep-2018 start
function CallAjaxTemplates(attrbList,wi_name,docName,Preq,Pval){
		
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
			var params="preq="+Preq+"&pval="+Pval;
			ajaxReq.open("POST", url, false);
			ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			ajaxReq.send(params);
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
	
	//changes done by disha for new generate template socket on 11-Sep-2018 end

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
					setNGValue('SupplementCardDetails_CardProduct',key);
					var combotext = com.newgen.omniforms.formviewer.getNGSelectedItemText('SupplementCardDetails_CardProduct');
					if(combotext.indexOf('(')>-1){
						var temp = combotext.split('(');
						currentFinalLimit = temp[temp.length-1].substr(0,(temp[temp.length-1]).length - 1);
						var acceptableValue = parseFloat(currentFinalLimit)*(0.8);
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
	function validateSuppEntry(opType){//by shweta //saurabh1 for alert PCAS-2959
		var suppRows = getLVWRowCount('SupplementCardDetails_cmplx_SupplementGrid');
		if(suppRows>0){
			var gridValues = [];
			var newValue = getNGValue('SupplementCardDetails_passportNo')+':'+getNGValue('SupplementCardDetails_CardProduct');
			for(var i=0;i<suppRows;i++){
				if(getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid')>-1 && i==getNGListIndex('SupplementCardDetails_cmplx_SupplementGrid') && opType=='modify'){
					continue;
				}
				gridValues.push(getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,3)+':'+getLVWAT('SupplementCardDetails_cmplx_SupplementGrid',i,30));
			}
			if(gridValues.indexOf(newValue)>-1){
				showAlert('','Only 1 supplementary card for a particular primary card is allowed for a customer');
				return false;
			}
		}
		return true;
	}
	
function calcAgeAtMaturity(){
	var maturityDate = getNGValue('cmplx_EligibilityAndProductInfo_MaturityDate');
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

function calcAgeAtMaturityLoanDetails(){	
	var maturityDate=getNGValue('cmplx_LoanDetails_maturitydate');
	var maturityDate = getNGValue('cmplx_LoanDetails_maturitydate');
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
	setNGValueCustom("cmplx_LoanDetails_ageatmaturity",ageAtMaturity);
}


function Calc_FirstRepaymentDate(moratorium)
{
    //var moratorium = getNGValue('cmplx_LoanDetails_moratorium'); 
	var d = new Date();
  var FirstRepay = d.getDate()+'/'+(d.getMonth()+1)+'/'+d.getFullYear(); 
  moratorium=parseInt(moratorium);
  var parts1=FirstRepay.split('/');
  var newDate=new Date(parts1[2],parts1[1]-1,parts1[0]);
   //var result = new Date(newDate);
   newDate.setDate(newDate.getDate() + moratorium);
   var day = newDate.getDate();	
   var month = newDate.getMonth()+1;	
   var newVal= ("0"+day).slice(-2)+'/'+("0"+month).slice(-2)+'/'+ newDate.getFullYear();
	return newVal; 
   
   }


function MaturityDate_2(FirstRepaymentDate,Tenor)
{
	//var FirstRepaymentDate = getNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate');
	if(FirstRepaymentDate==""){
		return "";
	}
	
	//var Tenor = getNGValue('cmplx_EligibilityAndProductInfo_Tenor');
	if(Tenor==""){
		Tenor=0;
	}
	
	var parts = FirstRepaymentDate.split('/');
	var oldDate=new Date(parts[2],parts[1]-1,parts[0]);	// [0] - Date, [1] - month, [2] - years
	
	/*if(parts[1] == '03' && leapYearCheck(parts[2])){
		if(parts[0] == '31')
			oldDate.setDate(oldDate.getDate() - 2);
		if(parts[0] == '30')
			oldDate.setDate(oldDate.getDate() - 1);
	}
	
	else if(parts[1] == '03' && !leapYearCheck(parts[2])){
		if(parts[0] == '31')
			oldDate.setDate(oldDate.getDate() - 3);
		if(parts[0] == '30')
			oldDate.setDate(oldDate.getDate() - 2);
	}
	
	
	else
*/
	if(parts[0] == '31' && parts[1] != '03')
	oldDate = new Date(parts[2],parts[1]-1,parts[0]-1);
	
	
	oldDate.setMonth(oldDate.getMonth()+(parseInt(Tenor)-1));
	var matday = oldDate.getDate();
	var matMonth = oldDate.getMonth();
	matMonth++;
	var matYear = oldDate.getFullYear();
	if(matday<10){matday='0'+matday;}
	if(matMonth<10){matMonth='0'+matMonth;}
	var maturityDate=matday+'/'+matMonth+'/'+matYear;
	
	var maturityDateParts = maturityDate.split('/');	//dd/mm/yyyy
	var updatedMatDate = new Date(maturityDateParts[2],maturityDateParts[1]-1,maturityDateParts[0]);
	//console.log(leapYearCheck(maturityDateParts[2]));
	if(parts[1] == '03' && leapYearCheck(maturityDateParts[2])){
		if(parts[0] == '31')
			updatedMatDate.setDate(updatedMatDate.getDate() - 2);
		if(parts[0] == '30')
			updatedMatDate.setDate(updatedMatDate.getDate() - 1);
		if(parts[0] == '29')
			updatedMatDate.setDate(updatedMatDate.getDate());
	}
	
	else if(parts[1] == '03' && !leapYearCheck(maturityDateParts[2])){
		if(parts[0] == '31')
			updatedMatDate.setDate(updatedMatDate.getDate() - 3);
		if(parts[0] == '30')
			updatedMatDate.setDate(updatedMatDate.getDate() - 2);
		if(parts[0] == '29')
		updatedMatDate.setDate(updatedMatDate.getDate() - 1);
	}
	//console.log("updatedMatDate :: "+updatedMatDate);

	var date = updatedMatDate.getDate();
	var month = updatedMatDate.getMonth();
	var year = updatedMatDate.getFullYear();
	month++;
	if(date<10){date='0'+date;}
	if(month<10){month='0'+month;}

	
	maturityDate = date+'/'+month+'/'+year;
	
	return maturityDate;

}

function leapYearCheck(year){

if((0 == year % 4) && (0 != year % 100) || (0 == year % 400)){
// is leap year
return true;
}
else{
// not a leap year
return false;
}

}


function setFieldsVisible(ReqProd){
	if(ReqProd=='Credit Card'){
				setLocked("subProd", true);
				setLocked("EmpType",true);
				setLocked("AppType",true); 
				setLocked("ReqLimit",true); 
				setLocked("Priority",true);
				setLocked("CardProd",true); 
			}
			
	else if(ReqProd=='Personal Loan'){
				setLocked("subProd", true);
				setLocked("EmpType",true);
				setLocked("AppType",true); 
				setLocked("ReqLimit",true); 
				setLocked("Priority",true);
				setLocked("CardProd",true); 
				setVisible("Product_Label3",true); 
				setVisible("Product_Label12",true); 
				setVisible("Product_Label21",true); 
				setVisible("Scheme",true); 
				setVisible("Product_Label5",true);
				setVisible("ReqTenor",true);
				com.newgen.omniforms.formviewer.setLeft("Product_Label3", "555px");
				com.newgen.omniforms.formviewer.setLeft("Scheme", "555px");
				com.newgen.omniforms.formviewer.setLeft("Product_Label5", "822px");
				com.newgen.omniforms.formviewer.setLeft("ReqTenor", "822px");
				setVisible("CardProd",false); 
				setVisible("Product_Label10",false);
				setVisible("Product_Label7",false); 
				setVisible("Product_Label6",false); 
				setVisible("Product_Label8",false);
				//setVisible("typeReq",false);
				setVisible("LimitChange",false);
				setVisible("LimitAcc",false); 
				setVisible("LimitExpiryDate",false);
				setVisible("Supplementary_Container",false);
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
				//setNGValue("EmpType","Salaried");//Tarang to be removed on friday(1/19/2018)
				setNGValue("EmploymentType","Salaried");
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
				//setNGValue("EmpType","--Select--");//Tarang to be removed on friday(1/19/2018)
				setNGValue("EmploymentType","--Select--");
		}
	}
/*
	function calcAverage_Overtime()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var counter=0;
	var avg=0;
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month1")!='')
	{
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_Overtime_Month1").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month2")!='')
	{
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_Overtime_Month2").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month3")!=''){	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_Overtime_Month3").replace(",", ""));
	counter++;
	}
	//avg1 =(month1 + month2)/2;	
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	//setNGValue("cmplx_IncomeDetails_Overtime_Avg",avg1.toFixed(2));
	setNGValue("cmplx_IncomeDetails_Overtime_Avg",avg.toFixed(2));
}
	
	function calcAverage_Commission()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_Commission_Month1")!=''){
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_Commission_Month1").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_Commission_Month2")!=''){
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_Commission_Month2").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_Commission_Month3")!=''){	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_Commission_Month3").replace(",", ""));
	counter++;
	}
		
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	setNGValue("cmplx_IncomeDetails_Commission_Avg",avg.toFixed(2));
}

function calcAverage_foodAllowance()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Month1")!=''){
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_FoodAllow_Month1").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Month2")!=''){
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_FoodAllow_Month2").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Month3")!=''){	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_FoodAllow_Month3").replace(",", ""));
		counter++;
	}
		
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	setNGValue("cmplx_IncomeDetails_FoodAllow_Avg",avg.toFixed(2));
}

function calcAverage_PhoneAllowance()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month1")!=''){
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month1").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month2")!=''){
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month2").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month3")!=''){	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month3").replace(",", ""));
	counter++;
	}	
		
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	setNGValue("cmplx_IncomeDetails_PhoneAllow_Avg",avg.toFixed(2));
}

function calcAverage_ServiceAllowance()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Month1")!=''){
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_serviceAllow_Month1").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Month2")!=''){
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_serviceAllow_Month2").replace(",", ""));
		counter++;
	}	
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Month3")!=''){	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_serviceAllow_Month3").replace(",", ""));
	counter++;
	}	
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	setNGValue("cmplx_IncomeDetails_serviceAllow_Avg",avg.toFixed(2));
}

function calcAverage_bonus()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_Bonus_Month1")!=''){
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_Bonus_Month1").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Bonus_Month2")!=''){
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_Bonus_Month2").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Bonus_Month3")!=''){	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_Bonus_Month3").replace(",", ""));
	counter++;
	}
		
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	setNGValue("cmplx_IncomeDetails_Bonus_Avg",avg.toFixed(2));
}

function calcAverage_Other()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_Other_Month1")!=''){
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_Other_Month1").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Other_Month2")!=''){
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_Other_Month2").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Other_Month3")!=''){	
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_Other_Month3").replace(",", ""));
	counter++;
	}
		
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	setNGValue("cmplx_IncomeDetails_Other_Avg",avg.toFixed(2));
}

function calcAverage_Flying()
	{
	var month1=0;
	var month2=0;
	var month3 = 0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_Flying_Month1")!=''){
		month1 = parseFloat(getNGValue("cmplx_IncomeDetails_Flying_Month1").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Flying_Month2")!=''){
		month2 = parseFloat(getNGValue("cmplx_IncomeDetails_Flying_Month2").replace(",", ""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_Flying_Month3")!='')	{
		month3 = parseFloat(getNGValue("cmplx_IncomeDetails_Flying_Month3").replace(",", ""));
	counter++;
	}
		
	avg =(month1 + month2 +month3)/counter;
	avg= avg?avg:0;
	var avg_roundOff=avg.toFixed(2);
	setNGValue("cmplx_IncomeDetails_Flying_Avg",avg_roundOff);
}


function AvgNetCheck()
{
	var avg=0;
	var overtime=0;
	var commission=0;
	var foodallow=0;
	var phoneallow=0;
	var serviceallow=0;
	var bonus=0;
	var other=0;
	var flying=0;
		
	if(getNGValue("cmplx_IncomeDetails_Overtime_Avg")!='')	
		overtime = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Overtime_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Commission_Avg")!='')
		commission = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Commission_Avg"),",",""));
		
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Avg")!='')	
		foodallow = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_FoodAllow_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Avg")!='')	
		phoneallow = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_PhoneAllow_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Avg")!='')	
		serviceallow = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_serviceAllow_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Bonus_Avg")!='')	
		bonus = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Bonus_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Other_Avg")!='')	
		other = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Other_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Flying_Avg")!='')	
		flying = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Flying_Avg"),",",""));
		
	avg =overtime + commission +foodallow + phoneallow + serviceallow + bonus + other + flying;
	setNGValue("cmplx_IncomeDetails_AvgNetSal",avg.toFixed(2));
}

// ++ below code already present - 09-10-2017
function calcAverage_OtherAssessedIncome(pId1,pId2,pId3)
	{
		var month1=0;
		var month2=0;
		var month3 = 0;
		var counter=0;
		var avg=0;
		
		if(getNGValue(pId1)=='' && getNGValue(pId2)=='' && getNGValue(pId3)==''){
			return 0;
		}
	
			if(getNGValue(pId1)!='')
			{
				month1 = parseFloat(getNGValue(pId1).replace(",", ""));
				counter++;
			}
			if(getNGValue(pId2)!='')
			{
				month2 = parseFloat(getNGValue(pId2).replace(",", ""));
				counter++;
			}
			if(getNGValue(pId3)!=''){	
				month3 = parseFloat(getNGValue(pId3).replace(",", ""));
				counter++;
			}
		
		//avg1 =(month1 + month2)/2;	
		avg =(month1 + month2 +month3)/counter;
		//setNGValue("cmplx_IncomeDetails_Overtime_Avg",avg.toFixed(2));
		return avg;
	}
// ++ above code already present - 09-10-2017

	function calcGrossSal()
{
	var grossSal=0;
	var BasicSal=0;
	var RegHousing = 0;
	var RegTransport = 0;
	var RegCostOfLiv = 0;
	var FixOT = 0;
	var other=0;
	
	if(getNGValue("cmplx_IncomeDetails_Basic")!='')
		BasicSal = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Basic"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_housing")!='')	
		RegHousing = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_housing"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_transport")!='')	
		RegTransport = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_transport"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_CostOfLiving")!='')
		RegCostOfLiv = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_CostOfLiving"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_FixedOT")!='')
		FixOT = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_FixedOT"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Other")!='')	
		other = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Other"),',',''));
	
	grossSal = BasicSal + RegHousing +RegTransport+ RegCostOfLiv + FixOT+other;
	setNGValue("cmplx_IncomeDetails_grossSal",grossSal.toFixed(2));
}

// ++ below code already present - 09-10-2017
function setDecimalto2digits(fieldId){

var fieldValue = getNGValue(fieldId);
	if(fieldValue){
		var fieldValint = parseFloat(fieldValue);
		return fieldValint.toFixed(2);
	}
	else{
		return "";
	}
}
// ++ above code already present - 09-10-2017
function calcTotalSal()
{	
	var BasicSal=0;
	var RegHousing = 0;
	var RegTransport = 0;
	var RegCostOfLiv = 0;
	var FixOT = 0;
	var other=0;
	var overtime=0;
	var commission=0;
	var foodallow=0;
	var phoneallow=0;
	var serviceallow=0;
	var bonus=0;
	var otheravg=0;
	var flying=0;
	var tot=0;
	
	if(getNGValue("cmplx_IncomeDetails_Basic")!='')
		BasicSal = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Basic"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_housing")!='')	
		RegHousing = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_housing"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_transport")!='')	
		RegTransport = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_transport"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_CostOfLiving")!='')
		RegCostOfLiv = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_CostOfLiving"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_FixedOT")!='')
		FixOT = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_FixedOT"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Other")!='')	
		other = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Other"),',',''));	
		
	if(getNGValue("cmplx_IncomeDetails_Overtime_Avg")!='')	
		overtime = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Overtime_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Commission_Avg")!='')
		commission = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Commission_Avg"),",",""));
		
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Avg")!='')	
		foodallow = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_FoodAllow_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Avg")!='')	
		phoneallow = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_PhoneAllow_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Avg")!='')	
		serviceallow = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_serviceAllow_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Bonus_Avg")!='')	
		bonus = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Bonus_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Other_Avg")!='')	
		otheravg = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Other_Avg"),",",""));
	
	if(getNGValue("cmplx_IncomeDetails_Flying_Avg")!='')	
		flying = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Flying_Avg"),",",""));
		
	tot = BasicSal + RegHousing + RegTransport + RegCostOfLiv + FixOT + other + overtime + commission + foodallow + phoneallow + serviceallow + bonus + otheravg + flying;
	setNGValue("cmplx_IncomeDetails_totSal",tot.toFixed(2));
	
}
function calcAvgNet()
{
	var net1 = 0;
	var net2=0;
	var net3=0;
	var avg=0;
	var counter=0;
	if(getNGValue("cmplx_IncomeDetails_netSal1")!=''){	
		net1 = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_netSal1"),",",""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_netSal2")!=''){
		net2 = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_netSal2"),",",""));
		counter++;
	}
	if(getNGValue("cmplx_IncomeDetails_netSal3")!=''){	
		net3 = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_netSal3"),",",""));
		counter++;
	}
	avg =(net1 + net2 +net3)/counter;
	avg= avg?avg:0;
	setNGValue("cmplx_IncomeDetails_AvgNetSal",avg.toFixed(2));
}
function netsalcheck()
{
	var overtime1=0;
	var comission1=0;
	var foodAllow1 = 0;
	var phoneAllow1 = 0;
	var serviceAllow1 = 0;
	var bonus1 = 0;
	var other1=0;
	var flying1=0;
	
	var overtime2=0;
	var comission2=0;
	var foodAllow2 = 0;
	var phoneAllow2 = 0;
	var serviceAllow2 = 0;
	var bonus2 = 0;
	var other2=0;
	var flying2=0;
	
	var overtime3=0;
	var comission3=0;
	var foodAllow3 = 0;
	var phoneAllow3 = 0;
	var serviceAllow3 = 0;
	var bonus3 = 0;
	var other3=0;
	var flying3=0;
	
	var avgnet=0;
	var netsal1=0;
	var netsal2=0;
	var netsal3=0;
	
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month1")!='')
		overtime1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Overtime_Month1"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Commission_Month1")!='')	
		comission1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Commission_Month1"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Month1")!='')	
		foodAllow1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_FoodAllow_Month1"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month1")!='')
		phoneAllow1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month1"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Month1")!='')
		serviceAllow1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_serviceAllow_Month1"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Bonus_Month1")!='')	
		bonus1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Bonus_Month1"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_Other_Month1")!='')
		other1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Other_Month1"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Flying_Month1")!='')	
		flying1 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Flying_Month1"),',',''));
				
		total_month1 = overtime1 + comission1 +foodAllow1+ phoneAllow1 + serviceAllow1 + bonus1+other1+flying1;
		setNGValue("cmplx_IncomeDetails_netSal1",total_month1.toFixed(2));
		
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month2")!='')
		overtime2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Overtime_Month2"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Commission_Month2")!='')	
		comission2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Commission_Month2"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Month2")!='')	
		foodAllow2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_FoodAllow_Month2"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month2")!='')
		phoneAllow2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month2"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Month2")!='')
		serviceAllow2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_serviceAllow_Month2"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Bonus_Month2")!='')	
		bonus2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Bonus_Month2"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_Other_Month2")!='')
		other2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Other_Month2"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Flying_Month2")!='')	
		flying2 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Flying_Month2"),',',''));
		
	total_month2 = overtime2 + comission2 +foodAllow2+ phoneAllow2 + serviceAllow2+bonus2+other2+flying2;
	setNGValue("cmplx_IncomeDetails_netSal2",total_month2.toFixed(2));
		
	if(getNGValue("cmplx_IncomeDetails_Overtime_Month3")!='')
		overtime3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Overtime_Month3"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Commission_Month3")!='')	
		comission3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Commission_Month3"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_FoodAllow_Month3")!='')	
		foodAllow3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_FoodAllow_Month3"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month3")!='')
		phoneAllow3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_PhoneAllow_Month3"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_serviceAllow_Month3")!='')
		serviceAllow3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_serviceAllow_Month3"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Bonus_Month3")!='')	
		bonus3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Bonus_Month3"),',',''));
	
	if(getNGValue("cmplx_IncomeDetails_Other_Month3")!='')
		other3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Other_Month3"),',',''));
		
	if(getNGValue("cmplx_IncomeDetails_Flying_Month3")!='')	
		flying3 = parseInt(replaceAll(getNGValue("cmplx_IncomeDetails_Flying_Month3"),',',''));
		
	total_month3 = overtime3 + comission3 +foodAllow3+ phoneAllow3 + serviceAllow3+bonus3+other3+flying3;
	setNGValue("cmplx_IncomeDetails_netSal3",total_month3.toFixed(2));
	
	//avgnet = (netsal1 + netsal2 + netsal3);
	
	
}
*/

function FirstRepaymentDatea()
{
var moratorium=getNGValue('cmplx_EligibilityAndProductInfo_Moratorium');
var today = new Date();
var m =  today.getDay();
var n =  today.getMonth();
var x =  today.getFullYear();
n++;
var totalDays = moratorium + m;
if(n == 1 || n==3 || n==5 || n==7 || n==8 || n==10 || n==12)
{
	
   if(totalDays > 31)
	{
	   if(n==12)
    {
	today.setMonth(1);
	today.setFullYear(today.getFullYear()+1);
	today.setDay(totalDays-31);
	setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
	}
	 else
	 {
	  today.setMonth(today.getMonth()+2);
	//today.setFullYear(today.getFullYear()+1);
	  today.setDay(totalDays-31);
	  setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);	 
	 }
	}
	
	else
	{
	//today.setMonth(today.getMonth()+2);
	today.setDay(moratorium+today.getDay());
    setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
	}
}
else if (n == 2 )
{
   if((x % 4 == 0) && (x % 100 != 0) || (x % 400 == 0))
   {
		if(totalDays>29)
				{
				  today.setMonth(3);
	              today.setDay(totalDays-29);
				  setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
				}
				else
				{
				  //today.setMonth(3);
	              today.setDay(moratorium + today.getDay());
				  setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
			}
    }
	else
	{
	            if(totalDays>28)
				{
				  today.setMonth(3);
	              today.setDay(totalDays-28);
				  setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
				}
				else
				{
				  //today.setMonth(3);
	              today.setDay(moratorium + today.getDay());
				  setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
			}
	}
}
	else
	{
		if(totalDays>30)
		{
	today.setMonth(today.getMonth()+2);
	today.setDay(totalDays-30);
	setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
		}
		else
		{
	//today.setMonth(today.getMonth()+2);
	today.setDay(totalDays + today.getDay());
	setNGValue('cmplx_EligibilityAndProductInfo_FirstRepayDate',today);
		}
	}
}

	
	function Fields_Liability()
	{
		var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(var i=0;i<n;i++){
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Business titanium Card" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
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
	
	function  Fields_Eligibility()
	{
		var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(var i=0;i<n;i++){
				alert(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2) + "<--->"+getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9));
				
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,1)=="Personal Loan" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
					setVisible("ELigibiltyAndProductInfo_Label39",true);
					//setVisible("ELigibiltyAndProductInfo_Label40",true);
					setVisible("ELigibiltyAndProductInfo_Combo4",true);
				
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,4)=="Take Over")
					{
						setVisible("ELigibiltyAndProductInfo_Label1",true);
						setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
						setVisible("ELigibiltyAndProductInfo_Label2",true);
						setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
					}
				}
				
				else{
					setVisible("ELigibiltyAndProductInfo_Label39",false);
					//setVisible("ELigibiltyAndProductInfo_Label40",false);
					setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
					setVisible("ELigibiltyAndProductInfo_Label1",false);
					setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
					setVisible("ELigibiltyAndProductInfo_Label2",false);
					setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
					
					if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Instant Money" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
						setVisible("ELigibiltyAndProductInfo_Text7",true); //fianl tai
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
						setVisible("ELigibiltyAndProductInfo_Label10",false);
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
						setVisible("ELigibiltyAndProductInfo_Label37",false);
						setVisible("ELigibiltyAndProductInfo_Label22",false);
						setVisible("ELigibiltyAndProductInfo_Label38",false);
						
						break;
					}			
											
					else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Salaried Credit Card" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
						Eligibility_Hide();
						setVisible("ELigibiltyAndProductInfo_Label41",true); 
						setVisible("ELigibiltyAndProductInfo_Text7",true); //Final TAI
						setLeft('cmplx_EligibilityAndProductInfo_FinalLimit','553px');
						setLeft('ELigibiltyAndProductInfo_Label4','553px');
						setTop('cmplx_EligibilityAndProductInfo_FinalLimit','831px');
						setTop('ELigibiltyAndProductInfo_Label4','815px');
						break;
					}
					
					else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Business titanium Card" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
						
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",false);
						setVisible("ELigibiltyAndProductInfo_Label3",false);
						Eligibility_Hide();			
						setLeft('cmplx_EligibilityAndProductInfo_FinalLimit','24px');
						setLeft('ELigibiltyAndProductInfo_Label4','24px');
						break;
					}
					else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Limit Increase" || getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Salaried Credit Card" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
						setVisible("ELigibiltyAndProductInfo_Label3",true);
						Eligibility_Hide();
						break;
					}
					else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2) =="Product Upgrade" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
						setVisible("ELigibiltyAndProductInfo_Label3",true);
						Eligibility_Hide();
					}	
					else{
						setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
						setVisible("ELigibiltyAndProductInfo_Label3",true);
						setVisible("ELigibiltyAndProductInfo_Label39",true);
						setVisible("ELigibiltyAndProductInfo_Label40",true);
						setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",true);
						Eligibility_UnHide();
					}
				}
			}	
		}
		else{
			setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
			setVisible("ELigibiltyAndProductInfo_Label3",true);
			Eligibility_UnHide();
			setVisible("ELigibiltyAndProductInfo_Label39",false);
			setVisible("ELigibiltyAndProductInfo_Label40",false);
			setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);//iNSTRUMENT TYPE
			}
	}

	function  Fields_ServiceRequest()
	{
		var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(var i=0;i<n;i++){
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Instant Money" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
					setVisible("CC_Loan_Frame3",false); 
					setVisible("CC_Loan_Frame5",true); 
					break;
				
				}
				else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2).indexOf("Business titanium Card")>-1 && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
					setVisible("CC_Loan_Frame3",false);
					setVisible("CC_Loan_Frame5",true); 					
					break;
				}
				else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Limit Increase" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
					setVisible("CC_Loan_Frame5",false); 
					setVisible("CC_Loan_Frame3",false); 
					break;
				}
				else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,2)=="Product Upgrade" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,9)=="Primary"){
					setVisible("CC_Loan_Frame5",false); 
					setVisible("CC_Loan_Frame3",false); 
				}
				else{
					setVisible("CC_Loan_Frame5",true); 
					setVisible("CC_Loan_Frame3",true); 
				}
			}	
		}
		else{
			setVisible("CC_Loan_Frame5",true); 
			setVisible("CC_Loan_Frame3",true); 
			}
	}


function SetDisableCustomer()
	{
		var fields="cmplx_Customer_EmiratesID,Customer_ReadFromCard,cmplx_Customer_CARDNOTAVAIL,cmplx_Customer_NEP,cmplx_Customer_EIDARegNo,cmplx_Customer_FirstNAme,cmplx_Customer_MiddleNAme,cmplx_Customer_LastNAme,cmplx_Customer_DOb,cmplx_Customer_Nationality,cmplx_Customer_MobNo,cmplx_Customer_PAssportNo,cmplx_Customer_CIFNO,Customer_Text7,Customer_FetchDetails,cmplx_Customer_IsGenuine,cmplx_Customer_NTB,cmplx_Customer_Title,cmplx_Customer_gender,cmplx_Customer_age,cmplx_Customer_IdIssueDate,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_VisaNo,cmplx_Customer_VIsaExpiry,cmplx_Customer_EMirateOfVisa,cmplx_Customer_PassPortExpiry,cmplx_Customer_RESIDENTNONRESIDENT,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_Third_Nationaity_Applicable,cmplx_Customer_Third_Nationaity,cmplx_Customer_GCCNational,cmplx_Customer_CustomerCategory,cmplx_Customer_yearsInUAE,cmplx_Customer_MAritalStatus,cmplx_Customer_MotherName,cmplx_Customer_DLNo,cmplx_Customer_PAssport2,cmplx_Customer_Passport3,cmplx_Customer_PASSPORT4,cmplx_Customer_EmirateOfResidence,cmplx_Customer_COUNTRYOFRESIDENCE,cmplx_Customer_VIPFlag,cmplx_Customer_minor"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setLocked(field_array[i], false);
	}

	function setValueCustomer_CheckEligibility(){
		setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR","30");
		setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI","4500");
		setNGValue("cmplx_EligibilityAndProductInfo_InterestRate","8%");
		setNGValue("cmplx_EligibilityAndProductInfo_EMI","3000");
		setNGValue("cmplx_EligibilityAndProductInfo_Tenor","4");
	}
	
	
function SetEnableCustomer()
	{
		var fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_MotherName,cmplx_Customer_VisaNo,,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_Third_Nationaity_Applicable,cmplx_Customer_Third_Nationaity,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIPFlag"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setLocked(field_array[i], false);
		setLocked("cmplx_Customer_EmirateIDExpiry",false);
		setLocked("cmplx_Customer_IdIssueDate",false);
		setLocked("cmplx_Customer_PassPortExpiry",false);
		setLocked("cmplx_Customer_VIsaExpiry",false);
	}
		
	function SetDisableCustomer()
	{
		var fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_age,cmplx_Customer_MotherName,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_VisaNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_Third_Nationaity_Applicable,cmplx_Customer_Third_Nationaity,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIPFlag"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setLocked(field_array[i], true);
	}
	
	function SetValueCustomer_FetchDetails(){
		
		setNGValue("cmplx_Customer_Title","Mr");
		setNGValue("cmplx_Customer_LastNAme2","AL Habib");
		setNGValue("cmplx_Customer_ResidentNonResident","Resident");
		setNGValue("cmplx_Customer_gender","Male");
		setNGValue("cmplx_Customer_MotherName","Rubeena");
		setNGValue("cmplx_Customer_VisaNo","VISA007");
		setNGValue("cmplx_Customer_MAritalStatus","Married");
		setNGValue("cmplx_Customer_COuntryOFResidence","UAE");
		setNGValue("cmplx_Customer_SecNAtionApplicable","Yes");
		setNGValue("cmplx_Customer_SecNationality","Indian");
		setNGValue("cmplx_Customer_Third_Nationaity_Applicable","Yes");
		setNGValue("cmplx_Customer_Third_Nationaity","Austria");
		setNGValue("cmplx_Customer_EmirateIDExpiry","12/01/1990");
		setNGValue("cmplx_Customer_IdIssueDate","12/01/1986");
		setNGValue("cmplx_Customer_PassPortExpiry","12/01/1990");
		setNGValue("cmplx_Customer_VIsaExpiry","12/01/1986");
		setNGValue("cmplx_Customer_GCCNational","Yes");
		setNGValue("cmplx_Customer_VIPFlag",true);
		setNGValue("cmplx_Customer_EMirateOfVisa","Dubai");
		setNGValue("cmplx_Customer_EmirateOfResidence","Dubai");
		setNGValue("cmplx_Customer_yearsInUAE","1");
		setNGValue("cmplx_Customer_CustomerCategory","Gold");
		
		setLocked("cmplx_Customer_MotherName",false);
		setLocked("cmplx_Customer_VisaNo",false);
		setLocked("cmplx_Customer_MAritalStatus",false);
		setLocked("cmplx_Customer_COuntryOFResidence",false);
		setLocked("cmplx_Customer_SecNAtionApplicable",false);
		setLocked("cmplx_Customer_SecNationality",false);
		setLocked("cmplx_Customer_Third_Nationaity_Applicable",false);
		setLocked("cmplx_Customer_Third_Nationaity",false);
		setLocked("cmplx_Customer_EmirateIDExpiry",false);
		setLocked("cmplx_Customer_IdIssueDate",false);
		setLocked("cmplx_Customer_PassPortExpiry",false);
		setLocked("cmplx_Customer_VIsaExpiry",false);
		setEnabled("cmplx_Customer_VIPFlag",true);
		setLocked("cmplx_Customer_EMirateOfVisa",false);
		setLocked("cmplx_Customer_EmirateOfResidence",false);
		setLocked("cmplx_Customer_yearsInUAE",false);
		setLocked("cmplx_Customer_CustomerCategory",false);
	}
	
	function setBlank_Customer()
	{
		var fields="cmplx_Customer_EmiratesID,cmplx_Customer_LAstNAme,cmplx_Customer_DOb,cmplx_Customer_MobNo,cmplx_Customer_PAssportNo,cmplx_Customer_Nationality,cmplx_Customer_ResidentNonResident,cmplx_Customer_EIDARegNo,cmplx_Customer_CIFNO,cmplx_Customer_marsoomID,cmplx_Customer_Title,cmplx_Customer_FIrstNAme,cmplx_Customer_MiddleName,cmplx_Customer_gender,cmplx_Customer_MotherName,cmplx_Customer_VisaNo,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_age,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_Third_Nationaity_Applicable,cmplx_Customer_Third_Nationaity,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational"; 
		var field_array=fields.split(",");
		for(var i=0;i<field_array.length;i++)
			setNGValue(field_array[i],"");	
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
		setNGValue('FrameName',array);
	}
//Done by aman on 0709	
	
	function SetValueCustomer_ReadCard(){
		setNGValue("cmplx_Customer_EmiratesID","1234567890");
		setNGValue("cmplx_Customer_CIFNO","54321");
		setNGValue("cmplx_Customer_FIrstNAme","Shiekh");
		setNGValue("cmplx_Customer_MiddleName","Mohammad");
		setNGValue("cmplx_Customer_LAstNAme","AL Habib");
		setNGValue("cmplx_Customer_MobNo","9876543210");
		setNGValue("cmplx_Customer_PAssportNo","PASS123");
		setNGValue("cmplx_Customer_Nationality","Emirati");
		setNGValue("cmplx_Customer_DOb","12/01/1990");
	}
	
	
function fieldvisible(activityName)
{
	if (activityName=='DDVT_Checker')
	{
		setVisible("DecisionHistory_updcust", true);
		setVisible("DecisionHistory_chqbook", true);	

	}
	return true;
}

function tabSheetHandling(activityName)
{
    //alert('$'+activityName+'$');
	
	
    if ((activityName == 'CSM' || activityName == 'Interest_Rate_Approval' || activityName == 'Telesales_Agent_Review' || activityName == 'CSM_Review' ||activityName == 'DSA_CSO_Review' || activityName == 'RM_Review' || activityName == 'Collection_Agent_Review' || activityName == 'Relationship_Manager' || activityName == 'Rejected_Application' || activityName == 'Reject_Queue' || activityName == 'SalesCoordinator')&& ("W"==window.parent.parent.wiViewMode)
		)
    {
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 9, false); // FCU
        //setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
		setSheetVisible("Tab1", 15, false); //Dispatch
		setSheetVisible("Tab1", 16, false); //Dispatch

    }
    if ((activityName == 'DDVT_maker' || activityName == 'DDVT_Checker')&& ("W"==window.parent.parent.wiViewMode))
    {
        //setSheetVisible("Tab1", 6, false); //System Checks
	 //setSheetVisible("Tab1", 7, false); // Changes done by shweta  for jira id PCAS-1403 System Checks tab should be visible in  ddvt workstep 
        setSheetVisible("Tab1", 8, false); // Fraud Control Unit
        setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    	setSheetVisible("Tab1", 15, false); //Dispatch

	}
   
    if (activityName == 'ToTeam' )
    {
        //setSheetVisible("Tab1", 6, false); //System Checks
		setSheetVisible("Tab1", 7, false); // Contact Point Verification
        //setSheetVisible("Tab1", 8, false); // Fraud Control Unit
        //setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
       setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
		setVisible("CC_Creation",false);//by akshay
		setVisible("Limit_Inc",false);//By Akshay
		setSheetVisible("Tab1", 8, false);
		setSheetVisible("Tab1", 15, false);//dispatch
    }
	
    if (activityName == 'CPV' || activityName == 'Smart_CPV' || activityName=='CPV_Analyst' || activityName=='Hold_CPV')
    {
		//++Below code added by nikhil 31/10/17 as per CC FSD 2.7
		//setSheetVisible("Tab1", 8, false); // Contact Point Verification changed by shweta cpvtab should be visible in cpv workstep
		    	 //setSheetVisible("Tab1", 10, false); // Disbursal Details
        	setSheetVisible("Tab1", 11, false); // Compliance
        	setSheetVisible("Tab1", 12, false); // FCU Decision
       	 setSheetVisible("Tab1", 14, false); //Original Validation
		// ++ below code already present - 09-10-2017 - code is not commented at offshore
		//setSheetVisible("Tab1", 7, false); // Contact Point Verification
		setSheetVisible("Tab1", 13, false); //CAD
		setSheetVisible("Tab1", 15, false);
		//-- above code added by nikhil 31/10/17 as per CC FSD 2.7

    }
    if (activityName == 'Compliance' && ("W"==window.parent.parent.wiViewMode))
    {
		//if(getNGValue('Prev_ws')=='CPV'){
			//setSheetVisible("Tab1", 13, false); //CAD //Arun (27/09/17) P1 - Tabs appearing in the CAD section to be re-looked into (FCU, Compliance, CPV)
		//}       
	    setSheetVisible("Tab1", 9, false); // Fraud Control Unit
        setSheetVisible("Tab1", 11, false); // Disbursal Details
        setSheetVisible("Tab1", 13, false); // FCU Decision
       // if(getNGValue('Prev_ws')=='CAD_Analyst1' || getNGValue('Prev_ws')=='CAD_Analyst2'){  
			setSheetVisible("Tab1", 7, false); // Contact Point Verification
		//}
		setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
   		setSheetVisible("Tab1", 15, false); //Dispatch

   }
//change by saurabh on 19th Dec
   if ((activityName == 'Disbursal' || activityName == 'CC_Disbursal'  || activityName == 'Disbursal_Checker' || activityName == 'Disbursal_Maker')&& ("W"==window.parent.parent.wiViewMode))
    {// changed by shweta for jira PCSP-142
	   setSheetVisible("Tab1", 8, false); // Contact Point Verification
        setSheetVisible("Tab1", 9, false); // Fraud Control Unit
    	setSheetVisible("Tab1", 12, false); // Compliance
        setSheetVisible("Tab1", 13, false); // FCU Decision
        setSheetVisible("Tab1", 14, false); //CAD      	
        setSheetVisible("Tab1", 15, false); //Original Validation
   		setSheetVisible("Tab1", 16, false); //Dispatch

   }
   
   if (activityName == 'CAD_Analyst1' || activityName == 'CAD_Analyst2' )
    {	//changed by shweta
        setSheetVisible("Tab1", 11, false); // Disbursal Details
        setSheetVisible("Tab1", 12, false); // Compliance
        setSheetVisible("Tab1", 13, false); // FCU Decision
        setSheetVisible("Tab1", 15, false); //Original Validation
		if(getNGValue("cpv_required")=='N'){
			setSheetVisible("Tab1", 8, false); // Contact Point Verification
		}
		setSheetVisible("Tab1", 14, false); //CAD Decision
   		setSheetVisible("Tab1", 16, false); //Dispatch

   }


	//--Above code added by nikhil 13/11/2017 for Code merge
    if (activityName == 'Original_Validation' || activityName == 'Document_Checker')
    {
        //setSheetVisible("Tab1", 4, false); // loan
        //setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
//        setSheetVisible("Tab1", 10, false); // To Enable Documents Tab
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
		setSheetVisible("Tab1", 15, false); //Dispatch

    }
    if (activityName == 'Waiver_Authority' && activityName == 'Relationship_Manager' && activityName == 'RM_Review' && activityName == 'Interest_Rate_Approval')
    {
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
    if (activityName == 'Deferral_Authority')
    {
        setSheetVisible("Tab1", 4, false); // loan
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
    if (activityName == 'Relationship_Manager' && ("W"==window.parent.parent.wiViewMode))
    {
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 9, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
        
    }
    if (activityName == 'Rejected_Application')
    {
        setSheetVisible("Tab1", 4, false); // loan
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
    if (activityName == 'Reject_Queue')
    {
        setSheetVisible("Tab1", 4, false); // loan
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
    if (activityName == 'CSM_Review')
    {
        setSheetVisible("Tab1", 4, false); // loan
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
    
    if (activityName == 'SalesCoordinator' && ("W"==window.parent.parent.wiViewMode))
    {
        //setSheetVisible("Tab1", 4, false); // loan
        setSheetVisible("Tab1", 6, false); //System Checks
        setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 9, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
    
    if (activityName == 'Post_Disbursal' || activityName == 'PostDisbursal_Maker' || activityName == 'PostDisbursal_Checker')
    {
        setSheetVisible("Tab1", 6, false); //system checks
        setSheetVisible("Tab1", 7, false); // CPV
        setSheetVisible("Tab1", 8, false); // FCU
        setSheetVisible("Tab1", 10, true); // Disbursal Details
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
	if(activityName=='Collection_Agent_Review')
	{
		//alert(activityName);
        //setSheetVisible("Tab1", 6, false); //System Checks
		setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // Fraud Control Unit
        setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    }
	if(activityName=='CardCollection')
	{
		 //setSheetVisible("Tab1", 6, false); //System Checks
		setSheetVisible("Tab1", 7, false); // Contact Point Verification
        setSheetVisible("Tab1", 8, false); // Fraud Control Unit
        setSheetVisible("Tab1", 10, false); // Disbursal Details
        setSheetVisible("Tab1", 11, false); // Compliance
        setSheetVisible("Tab1", 12, false); // FCU Decision
        setSheetVisible("Tab1", 13, false); //CAD
        setSheetVisible("Tab1", 14, false); //Original Validation
    	//setSheetVisible("Tab1", 15, false); //Dispatch
	}
	return true;	
	}

	
function uploadDocument(){
				var result = null;
				var finalxmlResponse = null;
				var activityName = window.parent.stractivityName;
				var wi_name = window.parent.pid;
				var firstName = getNGValue('cmplx_Customer_FIrstNAme');
				var middleName = getNGValue('cmplx_Customer_MiddleName');
				var lastName = getNGValue('cmplx_Customer_LAstNAme');
				if(middleName==""){
					var fullName = firstName+" "+lastName;
				}
				else{
					var fullName = firstName+" "+middleName+" "+lastName;
				}
				//setNGValue('sig_docindex','194282');
				if (getNGValue('sig_docindex')==''){
					showAlert('IncomingDoc_UploadSig',alerts_String_Map['PL406']);
					return false;
				 }
			//	alert("fullName uploadDocument"+ fullName);
				$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/VerifySignature.jsp",
					data: { CifId:getNGValue('cmplx_Customer_CIFNO'),AccountNo:getNGValue('Account_Number'),CustSeqNo:'001',ItemIndex:getNGValue('AppRefNo'),CustomerName:fullName,docIndex:getNGValue('sig_docindex'),WIName:wi_name ,workstepName:activityName} ,
					dataType: "text",
					async: false,
					success: function (response) {
					//	alert('result in integration_all: '+ response);	
							//cifIntegration(response);
							alert('Signature uploaded Successfully !');
						//console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});     			
			return result;
           }
		   
	function FRAGLOADEDFIRSTTIME(pId){
		
	if(pId=='CustomerDetails'){	
		return true;
	}
	if(pId=='ProductContainer'){	
		return true;
	}
	
		if(pId=='IncomeDEtails'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				var product=getNGValue("Product");
				if (product!='Y' && activityName=='DDVT_maker'){
				showAlert('Product',alerts_String_Map['PL343']);
				setNGFrameState('IncomeDEtails',1);
				return false;
				}
				else if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('IncomeDEtails',1);
				return false;
				}
				
				 else if(PLFRAGMENTLOADOPT['IncomeDEtails']==''){
					PLFRAGMENTLOADOPT['IncomeDEtails']='N';
					return true;
				}
				
				else if(PLFRAGMENTLOADOPT['IncomeDEtails']=='N' ){
					
					for(var i=0;i<n;i++){
						if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,1)=='Credit Card' && getLVWAT("cmplx_Product_cmplx_ProductGrid",0,9)=='Primary'){
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
					var EmpType=getLVWAT("cmplx_Product_cmplx_ProductGrid",0,6);
					if(EmpType=="Salaried" || EmpType=="Salaried Pensioner" || EmpType=="Pensioner"){
						setVisible("IncomeDetails_Frame3", false);
						setVisible("IncomeDetails_Frame2", true);
						com.newgen.omniforms.formviewer.setHeight("Incomedetails", "630px");
						com.newgen.omniforms.formviewer.setHeight("IncomeDetails_Frame1", "605px");
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
						setVisible("IncomeDetails_Frame2", false);
						setVisible("IncomeDetails_Frame3", true);
						com.newgen.omniforms.formviewer.setTop("IncomeDetails_Frame3","40px");
						com.newgen.omniforms.formviewer.setHeight("Incomedetails", "300px");
						com.newgen.omniforms.formviewer.setHeight("IncomeDetails_Frame1", "280px");
						if(getNGValue('cmplx_Customer_NTB')==true){
							setVisible("IncomeDetails_Label20", false);
							setVisible("cmplx_IncomeDetails_DurationOfBanking2", false);
							setVisible("IncomeDetails_Label22", false);
							setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", false);
							setVisible("IncomeDetails_Label35", false);
							setVisible("IncomeDetails_Label5", false);
							setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", false);
							setVisible("IncomeDetails_Label36", false);
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
						}
					
					}
				
				//LimitIncreaseFields_Income();
				//ProductUpgrade_Income();--Commented by akshay on 10/11/17 as these are not used now
			}
		}	
		else if(pId=='InternalExternalLiability')
			{
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				var n=getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
				//alert(n);
				if((flag_value!='Y' || n==0) && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('InternalExternalLiability',1);
					return false;
				}
				
				 if(PLFRAGMENTLOADOPT['InternalExternalLiability']=='')
				{
					PLFRAGMENTLOADOPT['InternalExternalLiability']='N';				
					return true;
				}					
				else if(PLFRAGMENTLOADOPT['InternalExternalLiability']=='N')
				{	
					Fields_Liability();
				}
				
			}
			else if(pId=='EmploymentDetails')
			{
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('EmploymentDetails',1);
					return false;
				}
				 if(PLFRAGMENTLOADOPT['EmploymentDetails']=='' )
				{
					PLFRAGMENTLOADOPT['EmploymentDetails']='N';				
					return true;
				}					
				else if(PLFRAGMENTLOADOPT['EmploymentDetails']=='N' )
				{	
							/*IMFields_Employment();
							BTCFields_Employment();
							LimitIncreaseFields_Employment();
							ProductUpgrade_Employment();--Commented by akshay on 10/11/17 as these are not used now*/
				}
			}
			
			
			else if(pId=='DecisionHistory_updcust'){
			//alert("inside update account request");
				return true;
			}
			else if(pId=='EligibilityAndProductInformation'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('EligibilityAndProductInformation',1);
					return false;
				}	
				else if(PLFRAGMENTLOADOPT['EligibilityAndProductInformation']==''){
					PLFRAGMENTLOADOPT['EligibilityAndProductInformation']='N';
					return true;
				}
				
					
			}
			else if(pId=='Address_Details_container'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('Address_Details_container',1);
					return false;

				}	
				else if(PLFRAGMENTLOADOPT['Address_Details_container']==''){
					PLFRAGMENTLOADOPT['Address_Details_container']='N';
					return true;
				}
			}
			else if(pId=='Alt_Contact_container'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					return true;
				}	
				else if(PLFRAGMENTLOADOPT['Alt_Contact_container']==''){
					PLFRAGMENTLOADOPT['Alt_Contact_container']='N';
					// CreateIndicator("temp");
					// document.getElementById("fade").style.display="block";
					return true;
				}
			}
			
			else if(pId=='ReferenceDetails'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('ReferenceDetails',1);
					return false;
				}	
				else if(PLFRAGMENTLOADOPT['ReferenceDetails']==''){
					PLFRAGMENTLOADOPT['ReferenceDetails']='N';
					return true;
				}
			}
				
				
			else if(pId=='Card_Details'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('Card_Details',1);
				}	
				else if(PLFRAGMENTLOADOPT['Card_Details']==''){
					PLFRAGMENTLOADOPT['Card_Details']='N';
				return true;
				}
			}
			
			else if(pId=='Supplementary_Card_Details'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('Supplementary_Card_Details',1);
				}	
				else if(PLFRAGMENTLOADOPT['Supplementary_Card_Details']==''){
					PLFRAGMENTLOADOPT['Supplementary_Card_Details']='N';
					return true;
				}
			}
			
			else if(pId=='FATCA'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('FATCA',1);
				}	
				else if(PLFRAGMENTLOADOPT['FATCA']==''){
					PLFRAGMENTLOADOPT['FATCA']='N';
					return true;
				}
			}
			
			else if(pId=='KYC'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('KYC',1);
				}	
				else if(PLFRAGMENTLOADOPT['KYC']==''){
					PLFRAGMENTLOADOPT['KYC']='N';
				return true;
				}
			}
			
			else if(pId=='OECD'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('OECD',1);
				}	
				else if(PLFRAGMENTLOADOPT['OECD']==''){
					PLFRAGMENTLOADOPT['OECD']='N';
						return true;
				}
			}
		else if(pId=='Part_Match'){
			return true;
		
		}
			
			else if(pId=='FinacleCRM_Incidents'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('FinacleCRM_Incidents',1);
				}	
				else if(PLFRAGMENTLOADOPT['FinacleCRM_Incidents']==''){
					PLFRAGMENTLOADOPT['FinacleCRM_Incidents']='N';
					return true;
				}
			}
			
			else if(pId=='FinacleCRM_CustInfo'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('FinacleCRM_CustInfo',1);
				}	
				else if(PLFRAGMENTLOADOPT['FinacleCRM_CustInfo']==''){
					PLFRAGMENTLOADOPT['FinacleCRM_CustInfo']='N';
					return true;
				}
			}
			
			else if(pId=='Finacle_Core'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('Finacle_Core',1);
				}	
				 else
				 return true;
				//}
			}
			
			else if(pId=='MOL'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('MOL',1);
				}	
				else if(PLFRAGMENTLOADOPT['MOL']==''){
					PLFRAGMENTLOADOPT['MOL']='N';
					return true;
				}
			}
			
			else if(pId=='World_Check'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('World_Check',1);
				}	
				else if(PLFRAGMENTLOADOPT['World_Check']==''){
					PLFRAGMENTLOADOPT['World_Check']='N';
				return true;
				}
			}
			
			else if(pId=='Reject_Enq'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('Reject_Enq',1);
				}	
				else if(PLFRAGMENTLOADOPT['Reject_Enq']==''){
					PLFRAGMENTLOADOPT['Reject_Enq']='N';
					return true;
				}
			}
			
			else if(pId=='Sal_Enq'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('Sal_Enq',1);
				}	
				else if(PLFRAGMENTLOADOPT['Sal_Enq']==''){
					PLFRAGMENTLOADOPT['Sal_Enq']='N';
					return true;
				}
			}
			
			else if(pId=='Inc_Doc'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
				//alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('Inc_Doc',1);
				}	
				else if(PLFRAGMENTLOADOPT['Inc_Doc']==''){
					PLFRAGMENTLOADOPT['Inc_Doc']='N';
					// CreateIndicator("temp");
					// document.getElementById("fade").style.display="block";
					return true;
				}
			}
			
			else if(pId=='DecisionHistory'){
				//alert('a');
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
			//	alert(flag_value);
				
			    
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					setVisible('DecisionHistory_Button3',false);
					setVisible('DecisionHistory_updcust',false);
					setVisible('DecisionHistory_chqbook',false);
					//showAlert('Part_Match',alerts_String_Map['PL169']);
					//setNGFrameState('DecisionHistory',1);
					return true;
				}	
				else if(PLFRAGMENTLOADOPT['DecisionHistory']==''){
					PLFRAGMENTLOADOPT['DecisionHistory']='N';
					// CreateIndicator("temp");
					// document.getElementById("fade").style.display="block";
					return true;
				}
			}
			else if(pId=='LoanDetails'){
				var activityName = window.parent.stractivityName;
				var flag_value=getNGValue("cmplx_PartMatch_partmatch_flag");
			//	alert(flag_value);
				if(flag_value!='Y' && activityName=='DDVT_maker'){
					showAlert('Part_Match',alerts_String_Map['PL169']);
					setNGFrameState('LoanDetails',1);
				}	
				else if(PLFRAGMENTLOADOPT['LoanDetails']==''){
					PLFRAGMENTLOADOPT['LoanDetails']='N';
					// CreateIndicator("temp");
					// document.getElementById("fade").style.display="block";
					return true;
				}
			}
		else if(pId =='Loan_Disbursal' ){
			var activityName = window.parent.stractivityName;
			if(activityName=='Disbursal'){
			
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
		else if (pId =='CC_Creation'){
			return true;
		}
		else if (pId =='Cust_Detail_verification'){
			return true;
		}
		else if (pId =='Business_verification'){
			return true;
		}
		else if (pId =='Home_country_verification'){
			return true;
		}
		else if (pId =='Residence_verification'){
			return true;
		}
		else if (pId =='Guarantor_verification'){
			return true;
		}
		else if (pId =='Reference_detail_verification'){
			return true;
		}
		else if (pId =='Office_verification'){
			return true;
		}
		else if (pId =='Loan_card_details'){
			return true;
		}
		else if(pId=='Notepad_details'){
			return true;
		}	
		else if (pId =='Smart_check'){
			return true;
		}
		else if (pId =='Cust_Det_Ver'){
			return true;
		}
		else if (pId =='Business_Ver'){
			return true;
		}
		else if (pId =='Emp_Verification'){
			return true;
		}
		else if (pId =='Bank_Check'){
			return true;
		}
		else if (pId =='NotepadDetails_CPV'){
			return true;
		}
		else if (pId =='Supervisor_section'){
			return true;
		}
		else if (pId =='Smart_chk'){
			return true;
		}
		else if (pId =='Post_Disbursal'){
			return true;
		}
		else if (pId =='FCUDecision'){
			return true;
		}
		else if (pId =='NotepadDetails_FCU'){
			return true;
		}
		else if (pId =='NotepadDetails_CAD'){
			return true;
		}
		else if (pId =='Dec'){
			return true;
		}
		else if (pId =='ReferHistory'){
			return true;
		}
		else if (pId =='Orig_validation'){
			return true;
		}
		else if(pId=='Limit_Inc'){
			return true;
		}	
		else if(pId=='Compliance'){
			return true;
		}	
	}	
	function Fatca_disableCondition(){
			setLocked("cmplx_FATCA_IdDoc", true);
			setNGValue("cmplx_FATCA_IdDoc", false);
			setLocked("cmplx_FATCA_DecForInd", true);
			setNGValue("cmplx_FATCA_DecForInd", false);
			setLocked("cmplx_FATCA_W8Form",true);
			setNGValue("cmplx_FATCA_W8Form", false);
			setLocked("cmplx_FATCA_W9Form",true);
			setNGValue("cmplx_FATCA_W9Form", false);
			setLocked("cmplx_FATCA_LossOFNationalCertificate",true);
			setNGValue("cmplx_FATCA_LossOFNationalCertificate", false);
			setLocked("cmplx_FATCA_TINNo",true);
			setNGValue("cmplx_FATCA_TINNo","");
			setLocked("cmplx_FATCA_SignedDate",true);
			setNGValue("cmplx_FATCA_SignedDate","");
			setLocked("cmplx_FATCA_ExpiryDate",true);
			setNGValue("cmplx_FATCA_ExpiryDate","");
			setLocked("cmplx_FATCA_Category",true);
			setNGValue("cmplx_FATCA_Category","");
			setLocked("cmplx_FATCA_ControllingPersonUSRel",true);
			setNGValue("cmplx_FATCA_ControllingPersonUSRel","");
			setLocked("cmplx_FATCA_ListedReason",true);
			setNGValue("cmplx_FATCA_ListedReason","");
			setLocked("cmplx_FATCA_SelectedReason",true);
			setNGValue("cmplx_FATCA_SelectedReason","");			
			setLocked("FATCA_Button1",true);
			setLocked("FATCA_Button2",true);
	}
	function Fatca_Enable(){
			setLocked("cmplx_FATCA_IdDoc", false);
			setNGValue("cmplx_FATCA_IdDoc", false);
			setLocked("cmplx_FATCA_DecForInd", false);
			setNGValue("cmplx_FATCA_DecForInd", false);
			setLocked("cmplx_FATCA_W8Form",false);
			setNGValue("cmplx_FATCA_W8Form", false);
			setLocked("cmplx_FATCA_W9Form",false);
			setNGValue("cmplx_FATCA_W9Form", false);
			setLocked("cmplx_FATCA_LossOFNationalCertificate",false);
			setNGValue("cmplx_FATCA_LossOFNationalCertificate", false);
			setLocked("cmplx_FATCA_TINNo",false);
			setNGValue("cmplx_FATCA_TINNo","");
			setLocked("cmplx_FATCA_SignedDate",false);
			setNGValue("cmplx_FATCA_SignedDate","");
			setLocked("cmplx_FATCA_ExpiryDate",false);
			setNGValue("cmplx_FATCA_ExpiryDate","");
			setLocked("cmplx_FATCA_Category",false);
			setNGValue("cmplx_FATCA_Category","");
			setLocked("cmplx_FATCA_ControllingPersonUSRel",false);
			setNGValue("cmplx_FATCA_ControllingPersonUSRel","");
			setLocked("cmplx_FATCA_ListedReason",false);
			setNGValue("cmplx_FATCA_ListedReason","");
			setLocked("cmplx_FATCA_SelectedReason",false);
			setNGValue("cmplx_FATCA_SelectedReason","");
			setLocked("FATCA_Button1",false);
			setLocked("FATCA_Button2",false);
	}
	

 function VisitSystemChecks_CAD(sheetid)
 {
	var activityName=window.parent.stractivityName;	
	if ((pname == 'PersonalLoanS' || pname == 'CreditCard') && activityName=='CAD_Analyst1')
    {//change from 7 to 6 by saurabh on 24th Dec
        if (sheetid == '7')//System Checks
        {
			 visitSystemChecks_Flag=true;
		}
	}
 }		
function prefcheck(){
var pref = getNGValue("PreferredAddress");
var customerType=getNGValue("Customer_Type");
	if(pref){
		var gridrowcount = getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		if(gridrowcount > 0){
			for(var i=0;i<gridrowcount;i++){
				var selectedRow = com.newgen.omniforms.formviewer.getNGListIndex('cmplx_AddressDetails_cmplx_AddressGrid');
				if(i == selectedRow){
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
function checkMandatory_CreateCIF(ApplicantType,pId){
switch(ApplicantType){
case 'PRIMARY': 
				if(getNGValue('cmplx_Customer_FIrstNAme')=='' || getNGValue('cmplx_Customer_FIrstNAme')==null){
				showAlert('cmplx_Customer_FIrstNAme',alerts_String_Map['PL010']);	
				return false;
				}
				else if(getNGValue('cmplx_Customer_Title')=='' || getNGValue('cmplx_Customer_Title')==null){
					showAlert('cmplx_Customer_Title',alerts_String_Map['PL219']);	
					return false;
				}
				else if(getNGValue('cmplx_Customer_LAstNAme')=='' || getNGValue('cmplx_Customer_LAstNAme')==null){
					showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['PL011']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_gender')=="--Select--" || getNGValue('cmplx_Customer_gender')==null || getNGValue('cmplx_Customer_gender')==''){
					showAlert('cmplx_Customer_gender',alerts_String_Map['PL019']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_ResidentNonResident')=="--Select--" || getNGValue('cmplx_Customer_ResidentNonResident')==null  || getNGValue('cmplx_Customer_ResidentNonResident')==''){
					showAlert('cmplx_Customer_LAstNAme',alerts_String_Map['PL018']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_MAritalStatus')=="--Select--" || getNGValue('cmplx_Customer_MAritalStatus')==null|| getNGValue('cmplx_Customer_MAritalStatus')==''){
					showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['PL026']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_DOb')=='' || getNGValue('cmplx_Customer_DOb')==null){
					showAlert('cmplx_Customer_DOb',alerts_String_Map['PL012']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_EmirateIDExpiry')=='' || getNGValue('cmplx_Customer_EmirateIDExpiry')==null){
					showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['PL009']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_VIsaExpiry')=='' || getNGValue('cmplx_Customer_VIsaExpiry')==null){
					showAlert('cmplx_Customer_VIsaExpiry',alerts_String_Map['PL022']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_PassPortExpiry')=='' || getNGValue('cmplx_Customer_PassPortExpiry')==null){
					showAlert('cmplx_Customer_PassPortExpiry',alerts_String_Map['PL023']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')=="--Select--" || getNGValue('cmplx_Customer_Nationality')==null || getNGValue('cmplx_Customer_Nationality')==''){
					showAlert('cmplx_Customer_Nationality',alerts_String_Map['PL013']);
					return false;
				}
				else if(getNGValue('cmplx_EmploymentDetails_EmpStatus')=="--Select--" || getNGValue('cmplx_EmploymentDetails_EmpStatus')==null){
					showAlert('cmplx_EmploymentDetails_EmpStatus',alerts_String_Map['PL221']);	
					return false;
				}
				else if (getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid")==0){
						showAlert('cmplx_AddressDetails_cmplx_AddressGrid',alerts_String_Map['PL223']);
						return false;
				}
					//if(NEW_ACCOUNT_REQ_checkMandatory())
				else if(getNGValue('AlternateContactDetails_MobileNo1')==''){
							showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['PL090']);
							return false;
				}
				else if(getNGValue('AlternateContactDetails_EMAIL1_PRI')==''){
							// disha FSD
							showAlert('AlternateContactDetails_EMAIL1_PRI',alerts_String_Map['PL094']);
							return false;
				}
				else{
					return true;
				}
	break;
case 'GUARANTOR': 
					var grid_name='cmplx_Guarantror_GuarantorDet';
					if(getLVWRowCount(grid_name)>0){
						for(var i=0;i<getLVWRowCount(grid_name);i++){
							if(getLVWAT(grid_name,i,5)==getLVWAT(pId,getNGListIndex(pId),2)){
								if(getLVWAT(grid_name,i,7)==''){
									showAlert('GuarantorDetails_Fname',alerts_String_Map['PL010']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,9)==''){
									showAlert('GuarantorDetails_lname',alerts_String_Map['PL011']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,6)==''){
									showAlert('GuarantorDetails_title',alerts_String_Map['PL219']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,12)==''){
									showAlert('GuarantorDetails_gender',alerts_String_Map['PL019']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,3)==''){
									showAlert('GuarantorDetails_dob',alerts_String_Map['PL012']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,14)==''){
									showAlert('GuarantorDetails_eidExpiry',alerts_String_Map['PL009']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,18)==''){
									showAlert('GuarantorDetails_visaExpiry',alerts_String_Map['PL022']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,16)==''){
									showAlert('GuarantorDetails_passExpiry',alerts_String_Map['PL023']);	
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
										showAlert('',alerts_String_Map['PL391']);	
										return false;	
									}	
								}		
								else if(getLVWAT(grid_name,i,10)==''){
									showAlert('GuarantorDetails_mobNo',alerts_String_Map['PL090']);	
									return false;
								}
							}
						}
					}
	break;
case 'SUPPLEMENT':var grid_name='SupplementCardDetails_cmplx_SupplementGrid';
					if(getLVWRowCount(grid_name)>0){
						for(var i=0;i<getLVWRowCount(grid_name);i++){
							if(getLVWAT(grid_name,i,3)==getLVWAT(pId,getNGListIndex(pId),2)){
								if(getLVWAT(grid_name,i,0)==''){
									showAlert('SupplementCardDetails_FirstName',alerts_String_Map['PL010']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,2)==''){
									showAlert('SupplementCardDetails_lastname',alerts_String_Map['PL011']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,14)==''){
									showAlert('SupplementCardDetails_Title',alerts_String_Map['PL219']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,11)==''){
									showAlert('SupplementCardDetails_Gender',alerts_String_Map['PL019']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,4)==''){
									showAlert('SupplementCardDetails_DOB',alerts_String_Map['PL012']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,18)==''){
									showAlert('SupplementCardDetails_EmiratesIDExpiry',alerts_String_Map['PL009']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,23)==''){
									showAlert('SupplementCardDetails_VisaExpiry',alerts_String_Map['PL022']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,15)==''){
									showAlert('SupplementCardDetails_PassportExpiry',alerts_String_Map['PL023']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,5)==''){
									showAlert('SupplementCardDetails_Nationality',alerts_String_Map['PL013']);	
									return false;
								}
								else if(getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid')>0){
									var flag_SupplementFound=false;
									for(var j=0;j<getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');j++){
										if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',j,13)=='S-'+getLVWAT(grid_name,i,0)+' '+getLVWAT(grid_name,i,2)){
												flag_SupplementFound=true;
										}	
									}
									if(flag_SupplementFound==false){
										showAlert('',alerts_String_Map['PL392']);	
										return false;	
									}	
								}		
								else if(getLVWAT(grid_name,i,6)==''){
									showAlert('GuarantorDetails_mobNo',alerts_String_Map['PL090']);	
									return false;
								}
							}
						}
					}
break;
}
return true;
}

function Moratorium(pId){
	var DisbDate = getNGValue('cmplx_LoanDetails_fdisbdate');	//PCASI - 3590
	var FirstRepay = getNGValue(pId);
	var parts1=FirstRepay.split('/');
	var FirstRepayDate=new Date(parts1[2],parts1[1]-1,parts1[0]);
	var one_day=1000*3600*24;
	//var currentdate= new Date();	//PCASI - 3590
	var parts2=DisbDate.split('/'); //hritik 3801
	var disb_Date=new Date(parts2[2],parts2[1]-1,parts2[0]);
	var date1_ms=FirstRepayDate.getTime();
	//var date2_ms=currentdate.getTime();	//PCASI - 3590
	var date2_ms=disb_Date.getTime();
	var day=date1_ms-date2_ms;
	day=day/one_day;
	if(day>parseInt(day)){
	day++;
	}
	day=parseInt(day);
	return day;
}
	
//code below commented becuase of duplicacy in PersonalLoans.js	
/*//added for slowness issue
function PL_click_getParameter(pId)
{
	//chaNGES DONE IN BELOW FUNCTION BY AKSHAY ON 31/10/18
var args='';	
switch(pId)
{
	case 'FetchDetails':args='CustomerDetails';break;
	case 'Nationality_Button':args='cmplx_Customer_Nationality';break;
	case 'SecNationality_Button':args='cmplx_Customer_SecNationality';break;
	case 'Product_Modify':args='ProductContainer';break;
	case 'Customer_save': args='CustomerDetails'; break;	
	case 'Modify': args= 'ProductContainer'; break;
	case 'cmplx_Product_cmplx_ProductGrid': args='ProductContainer'; break;
	case 'Product_Save': args= 'ProductContainer'; break;
	case 'IncomeDetails_Salaried_Save': args= 'IncomeDEtails' ; break;
	case 'GuarantorDetails_add': args='GuarantorDet'; break;
	case 'GuarantorDetails_modify': args='GuarantorDet'; break;
	case 'GuarantorDetails_delete': args='GuarantorDet'; break;
	case 'GuarantorDetails_Save': args='GuarantorDet'; break;	
	case 'GuarantorDetails_Button2': args='GuarantorDet'; break;
	case 'ExtLiability_Add': args='InternalExternalLiability'; break;
	case 'ExtLiability_Modify': args='InternalExternalLiability'; break;
	case 'ExtLiability_Delete': args='InternalExternalLiability'; break;
	case 'ExtLiability_Save': args='InternalExternalLiability'; break;	
	case 'EMploymentDetails_Button2': args='EMploymentDetails_Text21 EMploymentDetails_Text22'; break;	
	case 'EMploymentDetails_Save': args='EmploymentDetails'; break;	
	case 'Designation_button': args='cmplx_EmploymentDetails_Designation';break;
	case 'DesignationAsPerVisa_button': args='cmplx_EmploymentDetails_DesigVisa';break;
	case 'FreeZone_Button': args='cmplx_EmploymentDetails_FreezoneName';break;
	case 'ELigibiltyAndProductInfo_Save': args='EligibilityAndProductInformation'; break;	
	case 'Button_City': args='AddressDetails_city'; break;	
	case 'Button_State': args='AddressDetails_state'; break;	
	case 'AddressDetails_Button1': args='AddressDetails_country'; break;	
	case 'AddressDetails_addr_Add': args='Address_Details_container'; break;	
	case 'AddressDetails_addr_Modify': args='Address_Details_container'; break;
	case 'AddressDetails_addr_Delete': args='Address_Details_container'; break;
	case 'AddressDetails_Save': args='Address_Details_container'; break;
	case 'AltContactDetails_ContactDetails_Save': args='Alt_Contact_container'; break;
	case 'CardDetails_Button2': args='CardDetails_Frame1'; break;
	case 'CardDetails_Button3': args='CardDetails_Frame1'; break;
	case 'CardDetails_Button4': args='CardDetails_Frame1'; break;
	case 'CardDetails_Button1': args='CardDetails_Frame1'; break;
	case 'CardDetails_Button5': args='CardDetails_Frame1'; break;
	case 'CardDetails_modify': args='CardDetails_Frame1'; break;
	case 'CardDetails_delete': args='CardDetails_Frame1'; break;
	case 'CardDetails_Save': args='CardDetails_Frame1'; break;
	case 'SupplementCardDetails_Button1': args='Supplementary_Card_Details'; break;
	case 'SupplementCardDetails_Button2': args='Supplementary_Card_Details'; break;
	case 'SupplementCardDetails_Button3': args='Supplementary_Card_Details'; break;
	case 'SupplementCardDetails_Button4': args='Supplementary_Card_Details'; break;
	case 'SupplementCardDetails_Save': args='Supplementary_Card_Details'; break;
	case 'FATCA_Button1': args='cmplx_FATCA_listedreason cmplx_FATCA_selectedreason'; break;
	case 'FATCA_Button2': args='cmplx_FATCA_listedreason cmplx_FATCA_selectedreason'; break;
	case 'FATCA_Save': args='FATCA'; break;
	case 'KYC_Save': args='KYC'; break;
	case 'OECD_add': args='OECD'; break;
	case 'OECD_modify': args='OECD'; break;
	case 'OECD_delete': args='OECD'; break;
	case 'OECD_Save': args='OECD'; break;
	case 'ButtonOECD_State': args='OECD_townBirth'; break;
	case 'ReferenceDetails_Reference_Add': args='Reference_Details'; break;
	case 'ReferenceDetails_Reference__modify': args='Reference_Details'; break;
	case 'ReferenceDetails_Reference_delete': args='Reference_Details'; break;
	case 'ReferenceDetails_save': args='Reference_Details'; break;
	case 'IncomingDoc_Save': args='Inc_Doc'; break;
	case 'HomeCountryVerification_save': args='Home_country_verification'; break;
	case 'ResidenceVerification_save': args='Residence_verification'; break;	
	case 'FinacleCore_Button1': args='FinacleCore_Frame6'; break;
	case 'FinacleCore_Button2': args='FinacleCore_Frame6'; break;
	case 'FinacleCore_Button3': args='FinacleCore_Frame6'; break;
	case 'FinacleCore_Button4': args='FinacleCore_Frame5'; break;
	case 'FinacleCore_Button5': args='FinacleCore_Frame5'; break;
	case 'FinacleCore_Button6': args='FinacleCore_Frame5'; break;
	case 'FinacleCore_Button7': args='FinacleCore_Frame5'; break;
	case 'FinacleCore_save': args='Finacle_Core'; break;
	case 'LoanDetails_Save': args='LoanDetails'; break;
	case 'LoanDetaisDisburs_Save': args='LoanDetails_Frame3'; break;
	case 'LoanDetails_Button3': args='LoanDetails_Frame3'; break;
	case 'LoanDetails_Button4': args='LoanDetails_Frame3'; break;
	case 'LoanDetails_Button5': args='LoanDetails_Frame3'; break;
	case 'LoanDetails_Button6': args='LoanDetails_Frame3'; break;
	case 'IMD_Save': args='LoanDetails_Frame4'; break;
	case 'LoanDetailsRepay_Save': args='LoanDetails_Frame2'; break;
	case 'CustDetailVerification_save': args='Cust_Detail_verification'; break;
	case 'BussinessVerification_save': args='Business_verification'; break;
	case 'GuarantorVerification_save': args='Guarantor_verification'; break;
	case 'ReferenceDetailVerification_save': args='Reference_detail_verification'; break;
	case 'OfficeandMobileVerification_save': args='Office_verification'; break;
	case 'SmartCheck_Add': args='Smart_check'; break;
	case 'SmartCheck_Modify': args='Smart_check'; break;
	case 'SmartCheck_save': args='Smart_check'; break;
	case 'FinacleCRMCustInfo_Add': args='FinacleCRM_CustInfo'; break;
	case 'FinacleCRMCustInfo_Modify': args='FinacleCRM_CustInfo'; break;
	case 'FinacleCRMCustInfo_Delete': args='FinacleCRM_CustInfo'; break;
	case 'FinacleCRMCustInfo_save': args='FinacleCRM_CustInfo'; break;
	case 'ExternalBlackList_Button1': args='External_Blacklist';break;
	case 'MOL1_Save': args='MOL1_Frame1'; break;
	case 'PartMatch_Search': args='Part_Match'; break;
	case 'PartMatch_Blacklist': args=''; break;
	case 'PartMatch_Button1': args='Part_Match'; break;
	case 'PartMatch_Save': args='Part_Match'; break;
	case 'NotepadDetails_Add': args='NotepadDetails_Frame1'; break;
	case 'NotepadDetails_Modify': args='NotepadDetails_Frame1'; break;
	case 'NotepadDetails_Delete': args='NotepadDetails_Frame1'; break;
	case 'NotepadDetails_Add1': args='NotepadDetails_Frame3'; break;
	case 'NotepadDetails_Clear': args='NotepadDetails_Frame3'; break;
	case 'NotepadDetails_save': args='NotepadDetails_Frame1'; break;
	case 'Loan_Disbursal_save': args='Loan_Disbursal'; break;
	case 'CC_Creation_save': args='CC_Creation'; break;
	case 'Limit_Inc_save': args='Limit_Inc'; break;
	case 'OriginalValidation_Save': args='Orig_validation'; break;
	case 'DecisionHistory_Modify': args='DecisionHistory'; break;
	case 'DecisionHistory_save': args='DecisionHistory'; break;
	case 'CustDetailVerification1_Button2': args='CustDetailVerification1_Frame1'; break; //added for fcu changes 21/10/18 - Nikhil
	case 'PostDisbursal_Checklist_Button1': args='PostDisbursal_Checklist_Frame1';	break;
	case 'RejectEnq_Save': args='Reject_Enq';break;
	case 'SalaryEnq_Save': args='Salary_Enq';break;
	case 'CC_Creation_Button2': args='CC_Creation_Frame3'; break;//chnaged by nikhil 04/11
	case 'CC_Creation_CAPS': args='CC_Creation_Frame3'; break;//chnaged by nikhil 04/11
	case 'WorldCheck1_Add': args='WorldCheck1_Frame1'; break;//chnaged by nikhil 05/11
	case 'WorldCheck1_Modify': args='WorldCheck1_Frame1'; break;//chnaged by nikhil 05/11
	case 'CustDetailVerification1_Button2': args='CustDetailVerification1_Frame1'; break;//chnaged by nikhil 05/11
	case 'SmartCheck1_Add': args='SmartCheck1_Frame1'; break;//chnaged by nikhil 05/11
	case 'SmartCheck1_Modify': args='SmartCheck1_Frame1'; break;//chnaged by nikhil 05/11
	case 'Compliance_Modify': args='Compliance_Frame1'; break;//chnaged by nikhil 05/11
	case 'ReferHistory_Modify': args='ReferHistory_Frame1'; break;//chnaged by nikhil 05/11
	case 'DecisionHistory_ADD': args='DecisionHistory'; break;
	case 'DecisionHistory_Modify': args='DecisionHistory'; break;
	case 'DecisionHistory_Delete': args='DecisionHistory'; break;
	
	default: args='@this'; break;
}	
return args;
}		*/									
							
