function load_PropertyFile() {
	
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200){
    alerts_String = this.responseText;
	if(alerts_String.indexOf('\n')>-1);
	var parts=alerts_String.split('\n');
	for(var i=0;i<parts.length;i++)
	{
		var row=parts[i].split('=');
		row[0]=row[0].trim();
		alerts_String_Map[row[0]]=row[1];
	}
	}
  };
  
xhttp.open("POST", "/formviewer/resources/scripts/RLOS/Alerts.properties", true);  
 
 xhttp.send();
  
}


function calcAge(old_date,new_date){
	
		var newDate;
		if(new_date==''){
			newDate=new Date();
		}
		else{
		 var parts1=new_date.split('/');
		 newDate=new Date(parts1[2],parts1[1]-1,parts1[0]);
		}	
		var parts = old_date.split('/');
		var oldDate=new Date(parts[2],parts[1]-1,parts[0]);
	   var age = newDate.getFullYear() - oldDate.getFullYear();
	   //Deepak Changes done on 24 Jan 2019 for NaN.
	   if(oldDate=='Invalid Date' || newDate=='Invalid Date' ){
	   	return '0.00';
	   }
		var m = newDate.getMonth() - oldDate.getMonth();
		if (m < 0){
			age--;
			m= 12-oldDate.getMonth() + newDate.getMonth();
			//added by nikhil for PCAS-1320
			if(newDate.getDate() < oldDate.getDate())
			{
			m--;
			}
			
		}
		else if(m == 0 && newDate.getDate() < oldDate.getDate()) {
			age--;
			m= 11-oldDate.getMonth() + newDate.getMonth();//If month is same as current no need to count it
		}
		else if(m > 0 && newDate.getDate() < oldDate.getDate()){//case added by saurabh for PROC - 10159
			m--;
		}
		else if(newDate.getDate()-oldDate.getDate()!=0){
			//m++; Commented by aman for PROC-81
			if(m==12){
				age++;
				m=00;
			}	
		}	
		if ((age==0 || age==00) &&(m==00 || m==0)){
		m=01;
		}
		
		return age+'.'+m;
}
/*
function calcAge(old_date,new_date){
		//var dob=getNGValue(pId);
		//var today = new Date();
		
		var newDate;
		if(new_date==''){
			newDate=new Date();
		}
		else{
		 var parts1=new_date.split('/');
		 newDate=new Date(parts1[2],parts1[1]-1,parts1[0]);
		}	
		var parts = old_date.split('/');
		var oldDate=new Date(parts[2],parts[1]-1,parts[0]);
	   var age = newDate.getFullYear() - oldDate.getFullYear();
		var m = newDate.getMonth() - oldDate.getMonth();
		if (m < 0){
			age--;
			m= 12-oldDate.getMonth() + newDate.getMonth();
			if(newDate.getDate() < oldDate.getDate()){
				m=m-1;
			}	
		}
		else if(m == 0 && newDate.getDate() < oldDate.getDate()) {
			age--;
			m= 11-oldDate.getMonth() + newDate.getMonth();//If month is same as current no need to count it
		}
		else if(m > 0 && newDate.getDate() < oldDate.getDate()){//case added by saurabh for PROC - 10159
			m--;
		}
		if ((age==0 || age==00) &&(m==00 || m==0)){
		m=01;
		}
		
		return age+'.'+m;
}
*/

function CompareFrom_ToDate(pId1,pId2)
{
	var from_date=getNGValue(pId1);
	var to_date=getNGValue(pId2);
	 if(isFieldFilled(pId1)==false || isFieldFilled(pId2)==false){
		 return true;
	 }
	 var parts = from_date.split('/');
	 var from_Date=new Date(parts[2],parts[1]-1,parts[0]);
		parts = to_date.split('/');	
	var to_Date=new Date(parts[2],parts[1]-1,parts[0]);
	if(from_Date>to_Date){
		showAlert(pId1,alerts_String_Map['VAL325']);
		return false;
	}
	return true;	
}	

function validateFutureDate(pId){
	var d=getNGValue(pId);
    var today = new Date();
	var today_year=today.getFullYear();
	var today_month=today.getMonth();
	var today_day=today.getDate();
	if(d==""){
		return true;
	}
    var parts = d.split('/');
	var oldDate=new Date(parts[2],parts[1]-1,parts[0]);
	var oldDate_year=oldDate.getFullYear();
	var oldDate_month=oldDate.getMonth();
	var oldDate_day=oldDate.getDate();
			
	if(oldDate>today)
	{	
		//Prateek change 6-12-2017 : EMID expiry date can't be future date if EMID is expired
		if(pId=="cmplx_Customer_EmirateIDExpiry")
		{	
			//added by nikhil for PCSP-657
			if(document.getElementById("cmplx_Customer_NEP").value=="EXEID" || document.getElementById("cmplx_Customer_NEP").value=="RWEID")
			{
				showAlert(pId,alerts_String_Map['VAL327']);
				document.getElementById(pId).value=null;
				return false;
			}
		}
		else
		{
			//changed by nikhil for PCSP-536
			if(pId=='cmplx_Customer_VisaIssuedate')
			{
			showAlert(pId,alerts_String_Map['CC280']);
			return false;
			}
			else
			{
			showAlert(pId,alerts_String_Map['VAL187']);
			document.getElementById(pId).value=null;
			return false;
			}
		}		
	}
	else if(oldDate_year==today_year && oldDate_month==today_month && oldDate_day==today_day && pId!=('cmplx_FATCA_SignedDate'))
	{
	showAlert(pId,alerts_String_Map['VAL187']);
	document.getElementById(pId).value=null;
	return false;
	}	
	return true;
}
//sagarika
function validate_currentDate(pId)
{
 var d=getNGValue(pId);
    var today = new Date();
	var today_year=today.getFullYear();
	var today_month=today.getMonth();
	var today_day=today.getDate();
	if(d=="")
	{
		return true;
	}
    var parts = d.split('/');
	var oldDate=new Date(parts[2],parts[1]-1,parts[0]);
	var oldDate_year=oldDate.getFullYear();
	var oldDate_month=oldDate.getMonth();
	var oldDate_day=oldDate.getDate();
	if(oldDate_year==today_year && oldDate_month==today_month && oldDate_day==today_day )
	{
  return true; 
	}	
	else 
	{
	showAlert(pId,alerts_String_Map['PL418']);
	return false;
	}
}
//For PCSP-101
function validateFutureDateexcCurrent(pId){
	var d=getNGValue(pId);
    var today = new Date();
	var today_year=today.getFullYear();
	var today_month=today.getMonth();
	var today_day=today.getDate();
	if(d==""){
		return true;
	}
    var parts = d.split('/');
	var oldDate=new Date(parts[2],parts[1]-1,parts[0]);
	var oldDate_year=oldDate.getFullYear();
	var oldDate_month=oldDate.getMonth();
	var oldDate_day=oldDate.getDate();
			
	if(oldDate>today)
	{	
		//Prateek change 6-12-2017 : EMID expiry date can't be future date if EMID is expired
		if(pId=="cmplx_Customer_EmirateIDExpiry")
		{			
			if(document.getElementById("cmplx_Customer_NEP").value=="EXEID")
			{
				showAlert(pId,alerts_String_Map['VAL327']);
				document.getElementById(pId).value=null;
				return false;
			}
		}
		else
		{
			showAlert(pId,alerts_String_Map['VAL383']);
			document.getElementById(pId).value=null;
			return false;
		}		
	}
	/*else if(oldDate_year==today_year && oldDate_month==today_month && oldDate_day>today_day && pId!=('cmplx_FATCA_SignedDate'))
	{
	showAlert(pId,alerts_String_Map['VAL383']);
	document.getElementById(pId).value=null;
	return false;
	}sagarika	*/
	return true;
}


function validatePastDate(pId,msg){
	//added by nikhil or PCAS-2511
	var d='';
	if(pId=='cmplx_DEC_SetReminder' ||  pId=='cmplx_DEC_SetReminder_CA' || pId=='cmplx_DEC_SetReminder_Smart' 
		|| pId=='cmplx_Decision_SetReminder' ||  pId=='cmplx_Decision_SetReminder_CA' || pId=='cmplx_Decision_SetReminder_Smart')
	{
	d=getNGValue(pId).substring(0,10);
	}
	//else ifloan grid to d=getlv due date kechange pe bhi disable hogi
	else
	{
	d=getNGValue(pId);
	}
	//var d=getNGValue(pId);PCAS 2511
    var today = new Date();
    var today_year = today.getFullYear();
    var today_month = today.getMonth();
    var today_day = today.getDate();
	var todayDate =new Date(today_year,today_month,today_day);

    var parts = d.split('/');
    var oldDate = new Date(parts[2], parts[1] - 1, parts[0]);
    var oldDate_year = oldDate.getFullYear();
    var oldDate_month = oldDate.getMonth();
    var oldDate_day = oldDate.getDate();

    if (oldDate >= todayDate || d=="")
    {
       return true;
    }
    else 
    {		
	if(pId=="cmplx_Customer_EmirateIDExpiry" && getNGValue('cmplx_Customer_NEP')!='EXEID')
		{
			showAlert(pId, alerts_String_Map['VAL326']);
			document.getElementById(pId).value = null;
		}//Arun (06/12/17)	to modify the alert
        else if(msg==""){
			showAlert(pId, alerts_String_Map['VAL188']);
			document.getElementById(pId).value = null;
		}
		else
		{
			showAlert(pId, msg+' '+alerts_String_Map['VAL188']);
			document.getElementById(pId).value = null;		
			}	
		 return false;
    }

}


//Document Expiry Date 25/07/2017(Tanshu Aggarwal)
function validateExpiryDate(ExpiryDate,row_clicked_Expiry){
	//var d=getNGValue(pId);
    var today = new Date();
    var today_year = today.getFullYear();
    var today_month = today.getMonth();
    var today_day = today.getDate();
	if(today_month<10){
	today_month = '0'+today_month;
	}
	var datetosetinFailcase = today_day+'/'+today_month+'/'+today_year;
	
	var todayDate =new Date(today_year,today_month,today_day);

    var parts = ExpiryDate.split('/');
    var oldDate = new Date(parts[2], parts[1] - 1, parts[0]);
    var oldDate_year = oldDate.getFullYear();
    var oldDate_month = oldDate.getMonth();
    var oldDate_day = oldDate.getDate();

    if (oldDate >= todayDate)
    {
       return true;
    }
    else 
    {
        showAlert('', alerts_String_Map['VAL188']);
		document.getElementById(row_clicked_Expiry).value = datetosetinFailcase;
		return false;
    }

}
//Document Expiry Date 25/07/2017(Tanshu Aggarwal)



function YearsinUAE(pId)
{
    var yearsInUAE = getNGValue("cmplx_Customer_yearsInUAE");
    if(yearsInUAE.length==4 && yearsInUAE.charAt(1)=='.' ){
    	yearsInUAE= '0'+yearsInUAE;
		setNGValueCustom('cmplx_Customer_yearsInUAE',yearsInUAE);
    }
    var a = parseInt(yearsInUAE.charAt(3) + yearsInUAE.charAt(4));
	if(getNGValue("cmplx_Customer_Nationality")=='AE' && (getNGValue("cmplx_Customer_yearsInUAE")==''))
	{
		return true;
	}
	else
	{ 

		if (yearsInUAE.charAt(2) != '.')
		{
			showAlert('cmplx_Customer_yearsInUAE', alerts_String_Map['VAL257']);
			setNGValueCustom('cmplx_Customer_yearsInUAE',"");
			return false;
		}
	
		else if (yearsInUAE.charAt(0) == '0' && yearsInUAE.charAt(1) == '0' && yearsInUAE.charAt(3) == '0' && yearsInUAE.charAt(4) == '0')
		{
			showAlert('cmplx_Customer_yearsInUAE', alerts_String_Map['VAL257']);
			setNGValueCustom('cmplx_Customer_yearsInUAE',"");
			return false;
		}
		
	   else if(a>11)
	   {
		   showAlert('cmplx_Customer_yearsInUAE',alerts_String_Map['VAL290']);
		   setNGValueCustom('cmplx_Customer_yearsInUAE',"");
		   return false;
	   }
	   
	   else if(yearsInUAE.length!=5)
	   {  
		 showAlert('cmplx_Customer_yearsInUAE',alerts_String_Map['VAL256']);
		 setNGValueCustom('cmplx_Customer_yearsInUAE',"");	
			return false;
	   }
       
	}
}

function YYMM(changedField,fieldToset,directValue){
		var value;
		var decimalPart;
		var blankFlag = false;
		if(directValue==''){
		value=getNGValue(changedField);
		if(value.indexOf('/')>-1 || value.indexOf('-')>-1){
			value = calcAge(value,'');
		}
		}
		else{
		value = directValue;
		}
		 if(value.indexOf('.')>-1 && value.split('.').length<=2){
		 decimalPart = value.split(".")[1];
			if(value.charAt(0)=='.'){
				value=value.replace('.','0.');
			}
			if(decimalPart.length==1){
				value=value.replace('.','.0');	
			}
			else if(decimalPart.length==0){
				value=value.replace('.','.00');	
			}	
		}
		else if(value!='' && value.split('.').length<=2){
			value=value+".00";
		}
		else{
			value = '';
		}
		if(decimalPart){
			if(parseInt(decimalPart)>12){
				blankFlag = true;
			}
		}
		if( !blankFlag){
			if(fieldToset==''){
			setNGValueCustom(changedField,value);
			}
			else{
			setNGValueCustom(fieldToset,value);
			}
		}
		else{
			if(fieldToset==''){
			setNGValueCustom(changedField,'');
			}
			else{
			setNGValueCustom(fieldToset,'');
			}
		}
}
	//	CHANGE Started BY AKSHAY on 4/10/17 for changing total Salary calculation

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
	
		//changed by akshay on 12/12/17 as we can have multiple commas too
			if(getNGValue(pId1)!='')
			{
				month1 = parseFloat(replaceAll(getNGValue(pId1),",", ""));
				counter++;
			}
			if(getNGValue(pId2)!='')
			{
				month2 = parseFloat(replaceAll(getNGValue(pId2),",", ""));
				counter++;
			}
			if(getNGValue(pId3)!=''){	
				month3 = parseFloat(replaceAll(getNGValue(pId3),",", ""));
				counter++;
			}
		//changed by akshay on 12/12/17 as we can have multiple commas too

		//avg1 =(month1 + month2)/2;	
		avg =(month1 + month2 +month3)/counter;
		//setNGValueCustom("cmplx_IncomeDetails_Overtime_Avg",avg.toFixed(2));
		return avg.toFixed(2);
	}
	
	function calcTotalAvgOther()
	{
		var  avg_overtime=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Overtime_Month1","cmplx_IncomeDetails_Overtime_Month2","cmplx_IncomeDetails_Overtime_Month3"));
		var  avg_commission=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Commission_Month1","cmplx_IncomeDetails_Commission_Month2","cmplx_IncomeDetails_Commission_Month3"));
		var  avg_foodAllow=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_FoodAllow_Month1","cmplx_IncomeDetails_FoodAllow_Month2","cmplx_IncomeDetails_FoodAllow_Month3"));
		var avg_phoneAllow=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_PhoneAllow_Month1","cmplx_IncomeDetails_PhoneAllow_Month2","cmplx_IncomeDetails_PhoneAllow_Month3"));
		var avg_serviceAllow=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_serviceAllow_Month1","cmplx_IncomeDetails_serviceAllow_Month2","cmplx_IncomeDetails_serviceAllow_Month3"));
		var avg_bonus=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Bonus_Month1","cmplx_IncomeDetails_Bonus_Month2","cmplx_IncomeDetails_Bonus_Month3"));
		var avg_other=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Other_Month1","cmplx_IncomeDetails_Other_Month2","cmplx_IncomeDetails_Other_Month3"));
		var avg_flying=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Flying_Month1","cmplx_IncomeDetails_Flying_Month2","cmplx_IncomeDetails_Flying_Month3"));		
	
		var total=avg_overtime+avg_commission+avg_foodAllow+avg_phoneAllow+avg_serviceAllow+avg_bonus+avg_other+avg_flying;
		return total;
	}
	
function calcAvgNet()
{
	var net1 = 0;
	var net2=0;
	var net3=0;
	var avg=0;
	var counter=0;
	
	if(getNGValue("cmplx_IncomeDetails_netSal1")=='' && getNGValue("cmplx_IncomeDetails_netSal2")=='' && getNGValue("cmplx_IncomeDetails_netSal3")==''){
		return "";
	}
	
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
	//setNGValueCustom("cmplx_IncomeDetails_AvgNetSal",avg.toFixed(2));
	return avg.toFixed(2);
}

	function calcGrossSal()
{
	var grossSal=0;
	var BasicSal=0;
	var RegHousing = 0;
	var RegTransport = 0;
	var RegCostOfLiv = 0;
	var FixOT = 0;
	var other=0;
	var Pension=0;
	
	if(getNGValue("cmplx_IncomeDetails_Basic")=='' && getNGValue("cmplx_IncomeDetails_housing")=='' && getNGValue("cmplx_IncomeDetails_transport")=='' && getNGValue("cmplx_IncomeDetails_CostOfLiving")=='' && getNGValue("cmplx_IncomeDetails_FixedOT")=='' && getNGValue("cmplx_IncomeDetails_Other")=='')
	{
		return 0;
	}	
	
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
		
	// changed on 9/5/21  PCASI-2874
	if(getNGValue("cmplx_IncomeDetails_Pension")!='' && getNGValue("cmplx_IncomeDetails_Pension")!=null ) //hritik ppct 48
	    Pension = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Pension"),",", ""));
	
	grossSal = BasicSal + RegHousing +RegTransport+ RegCostOfLiv + FixOT + Pension + other;
	return grossSal.toFixed(2); //added By AKshay on 10/10/17-----to return gross Sal to be set from where iys called
	
}


function setDecimalto2digits(fieldId){

var fieldValue = getNGValue(fieldId).replace(new RegExp(',','g'),'');
	if(fieldValue){
		var fieldValint = parseFloat(fieldValue);
		return fieldValint.toFixed(2);
	}
	else{
		return "";
	}
}

function setDecimalto3digits(fieldId){

var fieldValue = getNGValue(fieldId);
	if(isFieldFilled(fieldId)==true){
		var fieldValint = parseFloat(fieldValue);
		return fieldValint.toFixed(3);
	}
	else{
		return "";
	}
}
//added By AKshay on 10/10/17-----to calculate Total Salary from existing functions and not from scratch
function calcTotalSal()
{
    var grossSal=0;
	var BasicSal=0;
	var RegHousing=0;
	var RegTransport=0;
	var RegCostOfLiv=0;
	var FixOT=0;
	var other=0;
	var rental=0;
	var Edu=0;
	var Pension=0;
	
	var avg=0;
	var totalSal=0;

	if(getNGValue("cmplx_IncomeDetails_Basic")!='')
		BasicSal = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Basic"),",", ""));

	if(getNGValue("cmplx_IncomeDetails_housing")!='')	
		RegHousing = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_housing"),",", ""));
		
	if(getNGValue("cmplx_IncomeDetails_transport")!='')	
	//chnaged by nikhil 26/11 total sal calcualtion not working
		RegTransport = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_transport"),",", ""));
	
	if(getNGValue("cmplx_IncomeDetails_CostOfLiving")!='')
		RegCostOfLiv = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_CostOfLiving"),",", ""));
		
	if(getNGValue("cmplx_IncomeDetails_FixedOT")!='')
		FixOT = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_FixedOT"),",", ""));
		
	if(getNGValue("cmplx_IncomeDetails_Other")!='')	
		other = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Other"),",", ""));
	
	if(getNGValue("cmplx_IncomeDetails_RentalIncome")!='')	
		rental = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_RentalIncome"),",", ""));
		
	if(getNGValue("cmplx_IncomeDetails_EducationalAllowance")!='')	
		Edu = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_EducationalAllowance"),",", ""));	
		
	// changed on 9/5/21  PCASI-2874
	if(getNGValue("cmplx_IncomeDetails_Pension")!='' && getNGValue("cmplx_IncomeDetails_Pension")!=null ) //hritik
	    Pension = parseFloat(replaceAll(getNGValue("cmplx_IncomeDetails_Pension"),",", ""));	
	
	grossSal=parseFloat(calcGrossSal());
	
	 avg_overtime=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Overtime_Month1","cmplx_IncomeDetails_Overtime_Month2","cmplx_IncomeDetails_Overtime_Month3"));
	 avg_commission=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Commission_Month1","cmplx_IncomeDetails_Commission_Month2","cmplx_IncomeDetails_Commission_Month3"));
	 avg_foodAllow=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_FoodAllow_Month1","cmplx_IncomeDetails_FoodAllow_Month2","cmplx_IncomeDetails_FoodAllow_Month3"));
	 avg_phoneAllow=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_PhoneAllow_Month1","cmplx_IncomeDetails_PhoneAllow_Month2","cmplx_IncomeDetails_PhoneAllow_Month3"));
	 avg_serviceAllow=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_serviceAllow_Month1","cmplx_IncomeDetails_serviceAllow_Month2","cmplx_IncomeDetails_serviceAllow_Month3"));
	 avg_bonus=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Bonus_Month1","cmplx_IncomeDetails_Bonus_Month2","cmplx_IncomeDetails_Bonus_Month3"));
	 avg_other=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Other_Month1","cmplx_IncomeDetails_Other_Month2","cmplx_IncomeDetails_Other_Month3"));
	 avg_flying=parseFloat(calcAverage_OtherAssessedIncome("cmplx_IncomeDetails_Flying_Month1","cmplx_IncomeDetails_Flying_Month2","cmplx_IncomeDetails_Flying_Month3"));		
	
	if(grossSal=='' && avg_overtime=='' && avg_commission=='' && avg_foodAllow=='' && avg_phoneAllow=='' && avg_serviceAllow=='' && avg_bonus=='' && avg_other=='' && avg_flying=='')
	{
		return '';
	}	
	//added by yash for calcualting total sal correctly on 20/7/2017 proc 840
		//avg = Math.round(total*100.0/3.0)/100;
// ended by yash on 20/7/2017

	avg=avg_overtime+avg_commission+avg_foodAllow+avg_phoneAllow+avg_serviceAllow+avg_bonus+avg_other+avg_flying;
	
	totalSal = avg + grossSal + rental + Edu - Pension;
	return totalSal.toFixed(2);
	//setNGValueCustom("cmplx_IncomeDetails_totSal",totalSal.toFixed(2));
}
//ended By AKshay on 10/10/17-----to calculate Total Salary from existing functions and not from scratch



function putComma(field){
	
	var limitComma=getNGValue(field);
	var limit=replaceAll(limitComma,',','');
	var parts = limit.split(".");
	parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	//parts.join(".");
	if(parts.length==2){
		var join=parts[0]+'.'+parts[1];
		setNGValueCustom(field,join);
	}
	else
		setNGValueCustom(field,parts[0]);
}

function replaceAll(str,replace,with_this)
{
    var str_hasil ="";
    var temp;

    for(var i=0;i<str.length;i++)
    {
        if (str[i] == replace)
        {
            temp = with_this;
        }
        else
        {
             temp = str[i];
        }

        str_hasil += temp;
    }

    return str_hasil;
}


function validateMail1(pId) {

    var email = getNGValue(pId);
    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

    if (filter.test(email)==false) {//braces added by akshay on 9/1/18 for proc 3521
		showAlert(pId,alerts_String_Map['VAL258']);
		setNGValueCustom(pId,'');// added by aman to clear if wrong EmAil
	}	
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
						showAlert('',alerts_String_Map['VAL021']);
					else 
						showAlert('',alerts_String_Map['VAL020']+xmlReq.status);	
					break;
				default:
					alert(xmlReq.status);
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
		alert("CallAjax : "+e.message);
		//return false;
    }
}

  
function Is_NotepadDetails_Modified(activityName)
{
	/*if(isVisible('NotepadDetails_Frame1')==false){
		showAlert('NotepadDetails_notedesc','');
		return false;
	}	*/
	var n=getLVWRowCount('cmplx_NotepadDetails_cmplx_notegrid');
	var note_modified=true;
	if(n>0){
		for(var i=0;i<n;i++)
		{
			if(getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',i,11)==activityName && getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',i,6)!=activityName) {
				if(getLVWAT('cmplx_NotepadDetails_cmplx_notegrid',i,8)==''){
					note_modified=false;
					break;
				}
			}	
		}
	}

	if(!note_modified){
		showAlert('NotepadDetails_Actusername',alerts_String_Map['PL376']);	
		return false;
	}			
	return true;
}
//function by saurabh on 4th Dec.
function CAC_check(pId){
	var rowCount = getLVWRowCount('cmplx_Liability_New_cmplx_LiabilityAdditionGrid');
	var CAC_inGrid = getLVWAT('cmplx_Liability_New_cmplx_LiabilityAdditionGrid',com.newgen.omniforms.formviewer.getNGListIndex('cmplx_Liability_New_cmplx_LiabilityAdditionGrid'),5);
	if(document.getElementById('Liability_New_IFrame_external').style.visibility=='hidden'){
	showAlert('','Please expand External Liabilities first');	
	return false;
	}
	else if(pId.indexOf('add')>-1){
		if(getNGValue('cacInd')==true){
			AllCACIndicators(false);
		}
	}
	else if(pId.indexOf('modify')>-1){
		if(CAC_inGrid=='true' && getNGValue('cacInd')==false){
			AllCACIndicators(true);
		}
		else if(CAC_inGrid=='false' && getNGValue('cacInd')==true){
			AllCACIndicators(false);
		}
	}
	else if(pId.indexOf('delete')>-1){
		if(CAC_inGrid=='true'){
			AllCACIndicators(true);
		}
	}
	return true;
}
//function by saurabh on 4th Dec.
function AllCACIndicators(flag){
	//setEnabledCustom('cacInd',flag);
	var table = getFromJSPTable('Liability_New_IFrame_external');
	for(var i=1;i<table.rows.length;i++){
		for(var j=1;j<table.rows[i].cells.length;j++){
			if(table.rows[i].cells[j].childNodes[0].id.indexOf('CAC_Indicator')>-1 && table.rows[i].cells[0].childNodes[0].value.indexOf('loan')==-1){
				if(flag){
				document.getElementById('Liability_New_IFrame_external').contentWindow.document.getElementById('CAC_Indicator_'+i).disabled = false;
				}
				else{
				document.getElementById('Liability_New_IFrame_external').contentWindow.document.getElementById('CAC_Indicator_'+i).disabled = true;
				}
			}
		}
	}
}

function getDataFromDB(query,wparams,pname){
	var result = '';
	var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
	var params="Query="+query+"&wparams="+wparams+"&pname="+pname;
	//var params="Query="+query;
	result=CallAjax1(jspName,params);
	//alert(result);
	if(result.indexOf("FAIL")!=-1 && result.indexOf("#FAIL#")==-1)
	{
		alert("Some error Encountered. Please try after some time");
		return;
	}
	else if(result.indexOf("FailedException")!=-1)
	{
		alert("Some Exception Occurred. Please try after some time");
		return;
	}
	else{
	
		return result;
	}

}

function CallAjax1(jspName,params)
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
						alert("CallAjax : URL doesn't exist!");
					else 
						alert(" CallAjax : Status is "+xmlReq.status);	
					break;
				default:
					alert(xmlReq.status);
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
		alert("CallAjax : "+e.message);
		//return false;
    }
}