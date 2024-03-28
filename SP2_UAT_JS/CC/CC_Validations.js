var NoOfAttemptsValue=0;//Aman for Sprint 2
//changed for multiple refer functionality on 26-11-18
function checkMandatory_Add()
{
      var Decision =getNGValue("cmplx_DEC_Decision");
	  var ReferTo=getNGValue("DecisionHistory_ReferTo");
	  var DecisionReasonCode=com.newgen.omniforms.formviewer.getNgSelectedValues('DecisionHistory_dec_reason_code');
	  var remarks=getNGValue('cmplx_DEC_Remarks');
	  var activityName=window.parent.stractivityName;
	  
        if(Decision==""||Decision=="--Select--" )
		{
			showAlert('cmplx_DEC_Decision',"Please select decision value");
			return false;
		}
		else if(Decision=='Refer' && ReferTo=='')
		{
			showAlert('DecisionHistory_ReferTo',"Please select a value in Refer To");
			return false;
		}
		else if((Decision=='Refer' || Decision=='Reject') && (DecisionReasonCode=='' || DecisionReasonCode=='--Select--'))
		{
			showAlert('DecisionHistory_dec_reason_code',"Please select decision reason value");
			return false;
		}
		else if(isVisible('cmplx_DEC_Remarks')==true && remarks=='')
		{
			showAlert('cmplx_DEC_Remarks',alerts_String_Map['CC190']);
			return false;
		}
		else if(Decision=='Refer' && ReferTo=='FPU'  && (activityName=='CAD_Analyst1' || activityName=='CPV'))
		{
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
		else if(activityName=='FCU')
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
		else if(activityName=='CAD_Analyst1')
		{
			var category=getNGValue('DectechCategory');
			if(category=='A' && Decision!='Reject')
			{
				showAlert('','Dectech Category A can only Declined');
				return false;

			}
		}
     return true;
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
	return true;
}



//sagarika on 04-04-19
function validate_time(id)
{
  var str=getNGValue(id);
  var re=/^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
   if(re.test(str))
    {
       return true;
    }
    else
    {
       showAlert(pId,alerts_String_Map['CC283']);
       return false;
    }
}

//add a row in decision grid for multiple refer functionality on 26-11-18
function Add_Validate()
{
	var flag_add = false;
    var n = getLVWRowCount("DecisionHistory_Decision_ListView1");
    var activityname=window.parent.stractivityName;
	var count=0;
	var same=0;
	var non_multiple_refer=0;
	if(n>0)
		{
           for(var i=0;i<n;i++)
			{
				if(getLVWAT('DecisionHistory_Decision_ListView1',i,2)==activityname && getLVWAT('DecisionHistory_Decision_ListView1',i,12)!='Y')
				{
					count=count+1;
				}
				//below code by nikhil 29/11 to avoid add same WS refer
				if(getLVWAT('DecisionHistory_Decision_ListView1',i,6)==getNGValue('DecisionHistory_ReferTo') && getLVWAT('DecisionHistory_Decision_ListView1',i,12)!='Y')
				{
					same=1;
				}
				if((getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='Credit' || getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='Smart CPV' || getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='Mail Approval' || getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='CPV Checker' || getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='DDVT_Maker') && getLVWAT('DecisionHistory_Decision_ListView1',i,12)!='Y')
				{
					non_multiple_refer=1;
				}
				
			}
			
			if(getNGValue("cmplx_DEC_Decision")=='Refer')
			{
				//below code by nikhil 29/11 to avoid add same WS refer
				if(same==1)
				{
					showAlert('',alerts_String_Map['CC275']);
					return false;
				}
				//changes for PCSP-925
				if(activityname!='CAD_Analyst1' && activityname!='CPV' && activityname!='Cad_Analyst2'){//on these user can select multiple refer  
					if(count==1){
						showAlert('',alerts_String_Map['CC253']);
						return false;
					}
				}
				//below code added by nikhil for non-multiple refer WS
				else if(count==1)
				{
				if(getNGValue('DecisionHistory_ReferTo')=='Credit' || getNGValue('DecisionHistory_ReferTo')=='Smart CPV' || getNGValue('DecisionHistory_ReferTo')=='Mail Approval' || getNGValue('DecisionHistory_ReferTo')=='CPV Checker')
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
				if(count==1){
						showAlert('',"Cannot add two decision values!!!");
						return false;
				}
			}
		}
   var flag_ccdd = 1;
  return true;
}
//modify a row in decision grid for multiple refer functionality on 26-11-18
function modify_row()
{
	var activityname=window.parent.stractivityName;
	var selectedRow=com.newgen.omniforms.formviewer.getNGListIndex('DecisionHistory_Decision_ListView1');

	if(getLVWAT('DecisionHistory_Decision_ListView1',selectedRow,12)=='Y') 
	{
		showAlert('','Only newly added rows can be modified!!!');
		return false;	}
	//below code added by nikhil by nikhil for PCSP-320
	var flag_add = false;
    var n = getLVWRowCount("DecisionHistory_Decision_ListView1");
    var activityname=window.parent.stractivityName;
	var count=0;
	var same=0;
	var non_multiple_refer=0;
	if(n>0)
		{
           for(var i=0;i<n;i++)
			{
				if(getLVWAT('DecisionHistory_Decision_ListView1',i,2)==activityname && getLVWAT('DecisionHistory_Decision_ListView1',i,12)!='Y')
				{
					count=count+1;
				}
				//below code by nikhil 29/11 to avoid add same WS refer
				if(getLVWAT('DecisionHistory_Decision_ListView1',i,6)==getNGValue('DecisionHistory_ReferTo') && getLVWAT('DecisionHistory_Decision_ListView1',i,12)!='Y')
				{
					same=1;
				}
				if((getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='Credit' || getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='Smart CPV' || getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='Mail Approval' || getLVWAT('DecisionHistory_Decision_ListView1',i,6)=='CPV Checker') && getLVWAT('DecisionHistory_Decision_ListView1',i,12)!='Y')
				{
					non_multiple_refer=1;
				}
				
			}
			
			if(getNGValue("cmplx_DEC_Decision")=='Refer')
			{
				//commented by nikhil as not required in modify PCSP-613
				//below code by nikhil 29/11 to avoid add same WS refer
				/*if(same==1)
				{
					showAlert('',alerts_String_Map['CC275']);
					return false;
				}*/
				//change for PCSP-925
				if(activityname!='CAD_Analyst1' && activityname!='CPV' && activityname!='Cad_Analyst2'){//on these user can select multiple refer  
					if(count==1){
						showAlert('',alerts_String_Map['CC253']);
						return false;
					}
				}
				//below code added by nikhil for non-multiple refer WS
				else if(count==2)
				{
				if(getNGValue('DecisionHistory_ReferTo')=='Credit' || getNGValue('DecisionHistory_ReferTo')=='Smart CPV' || getNGValue('DecisionHistory_ReferTo')=='Mail Approval' || getNGValue('DecisionHistory_ReferTo')=='CPV Checker')
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
//delete a row in decision grid for multiple refer functionality on 26-11-18
function delete_dec_row()
{
	var activityname=window.parent.stractivityName;
	var selectedRow=com.newgen.omniforms.formviewer.getNGListIndex('DecisionHistory_Decision_ListView1');
	if(selectedRow==-1)
	{
		showAlert('','Please select a row to delete');
		return false;
	}
	else if(getLVWAT('DecisionHistory_Decision_ListView1',selectedRow,12)=='Y')
	{
		showAlert('','Only newly added rows can be deleted!!!');
		return false;
	}
	return true;
}
function Address_Validate(opType)
	{
		var flag_address=false;
		var AddType=getNGValue("AddressDetails_addtype");
		var CustomeType=getNGValue('AddressDetails_CustomerType');//added by prabhakar drop-4 poin-3
		var preffAddr=getNGValue('AddressDetails_PreferredAddress');
		var n = getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		var prefAddAlready = false;
		if(n>0)
		{
			for(var i=0;i<n;i++){
				var grid_AddType=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				var grid_CustomeType=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,13);//added by prabhakar drop-4 poin-3
				var grid_preffAddr=getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,11);
				if(AddType==grid_AddType && opType == 'add' && CustomeType==grid_CustomeType){
				showAlert('addtype',"Cannot add two "+ AddType+" Addresses for " +grid_CustomeType);//chnages by prabhakar drop-4 point-3	
					return false;
				}
				else if(preffAddr==true && AddType=='Home'){
					showAlert('AddressDetails_addtype',alerts_String_Map['VAL066']);
					return false;
				}
				else if(preffAddr==true && grid_preffAddr==preffAddr.toString() && opType!='modify' && CustomeType==grid_CustomeType){
					showAlert('addtype',alerts_String_Map['VAL093']+" for "+CustomeType);//modify by prabhakar
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
function checkMandatory_LiabilityGrid()
	{
		var contractType=getNGValue('ExtLiability_contractType');
		var EMI=getNGValue('Liability_New_EMI');
		var remarks=getNGValue('ExtLiability_remarks');
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
			showAlert('ExtLiability_contractType',alerts_String_Map['VAL149']);	
			return false;
		}
		
		//Changes Done by aman for PCSP-67
		//Changes Done by aman for PCSP-67
		else if (getNGValue('Contract_App_Type')=="L" && (getNGValue('ExtLiability_Limit')=='' && EMI=='')){
			showAlert('ExtLiability_Limit','Please fill EMI or Limit');	
			return false;
		
		}
			
		else if((EMI=="0" || getNGValue('ExtLiability_Limit')=='0') && getNGValue('Contract_App_Type')=="L"){
			showAlert('EMI','EMI or Limit cannot be zero');	
			return false;
			}
		//Changes Done by aman for PCSP-67	
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
	/*	else if(remarks==""){
			showAlert('remarks',alerts_String_Map['VAL129']);	
			return false;
			}
		*/// Commented for proc 8366	
		else if(getNGValue('takeOverIndicator')==true && getNGValue('takeoverAMount')==''){
			showAlert('takeoverAMount',alerts_String_Map['VAL140']);	
			return false;
			}
			//below code commented by nikhil for PCSP-626
		/*if(CAC_flag && CAC_ind==true){
			setNGValueCustom('ExtLiability_CACIndicator',false);//name change by Tarang on 28/02/2018
			return false;
			}*/
		return true;
	}	
function checkMandatory_Address(opType)
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
		 var CustomerType=getNGValue("AddressDetails_CustomerType");//ADDED by prabhakar drop-4 point-10
		 //added by prabhakar drop-4 point-10
		 if(CustomerType==""||CustomerType=="--Select--")
			{
			showAlert('AddressDetails_CustomerType','Applicant Type can not be blank');	
			return false;
			}
			//added by prabhakar drop-4 point-10 end
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
				//uncommented by nikhil for CR-CIF Creation Failaing
				else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
				return false;
				}
			else if(!prefcheck(opType)){
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
				
				//cr-CIF creation
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
			if(!prefcheck(opType)){
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
				//cr-CIF creation
				else if(YearsATcurrent==""){
				showAlert('AddressDetails_years','Please enter Years in current Address');	
				return false;
				}
		}
	return true;
	}
	
	function AccountSummary_checkMandatory()
	{
		if(getNGValue('cmplx_Customer_CIFNo')=='' || getNGValue('cmplx_Customer_CIFNo')==null){
			showAlert('cmplx_Customer_CIFNo',alerts_String_Map['VAL033']);	
			return false;
		}
	return true;	
	}	

	
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
function LengthVal(pId){
var value = getNGValue(pId);
		if(value.indexOf(',')>-1){
			value = replaceAll(value,',','');		
		}
//Deepak Code commented for proc - PCSP-310		
		/*if(value.indexOf('.')>-1){
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
		}*/
		
		return true;
}
		
function DMandatoryCC(){
	//alert("Inside CC DMandatory");
	//below condition added by akshay on 4/4/18 for proc 7485
	var activityName = window.parent.stractivityName;
			//below lines changed by akshay on 23/11/18

	var newAddedrow=0;
	var ReferTo="";
	for(var i=0;i<getLVWRowCount('DecisionHistory_Decision_ListView1');i++)
	{
		if(getLVWAT('DecisionHistory_Decision_ListView1',i,12)=='')
		{
			
			//below code added by nikhil on 28/11/18 as referto wrong value set on first time
			if(newAddedrow>0)
			{
			ReferTo+=','+getLVWAT('DecisionHistory_Decision_ListView1',i,6);
			}
			else
			{
			ReferTo+=getLVWAT('DecisionHistory_Decision_ListView1',i,6);
			}
			newAddedrow++;
		}		
	}
	if(newAddedrow==0)
	{
		showAlert('cmplx_DEC_Decision', 'Please add a decision in decision grid!!!');
		return false;
	}
	else
	{
		setNGValueCustom('q_MailSubject',ReferTo);
		//setNGValueCustom('ReferTo',ReferTo); changed by aman for name change
	}
	
	//added by nikhil 29/11 for refer/reject exemption
	if(getNGValue('cmplx_DEC_Decision')=='Reject' || getNGValue('cmplx_DEC_Decision')=='Refer')  
	{
	//Changes done for PCSP-319 by Aman (return false not done because asked in JIRA)
	//commented by nikhilas same move to add button
	/*	 if(activityName=='CAD_Analyst1' )
	 {
		var DectechCall = getNGValue('reEligibility_CC_counter').split(';')[1];
		if (parseInt(DectechCall)<1){
			showAlert('cmplx_DEC_ReferTo','Please run Dectech Call');
				
		}
	 }*/
	 //Changes done for PCSP-319 by Aman (return false not done because asked in JIRA)
	
	if((activityName!='FCU' || activityName!='Compliance'))
	 {
		var desc = getNGValue('cmplx_DEC_Decision');
			if(activityName=='Cad_Analyst2')
			{
		if(flag_add_new==true)
		{
			showAlert('DecisionHistory_Button4',alerts_String_Map['VAL391']);
			return false;
		}
			}
		
		if(activityName=='CAD_Analyst1' && (desc.toUpperCase()=='REFER' || desc.toUpperCase()=='REJECT'))
			{
		if(flag_add_new==true)
		{
			showAlert('DecisionHistory_Button4',alerts_String_Map['VAL391']);
			return false;
		}
				
			//changes doen for mail approval PCSP-690
			if((getNGValue('DecisionHistory_ReferTo')!='Smart CPV' && getNGValue('DecisionHistory_ReferTo')!='Mail Approval') 
			&& (getNGValue('cmplx_DEC_ReferTo')=='' || getNGValue('cmplx_DEC_ReferTo')=='--Select--') && getNGValue('cmplx_DEC_ReferTo')!='A')
			{
				showAlert('cmplx_DEC_ReferTo',alerts_String_Map['CC271']);
					return false;
			}	 
			}
			if(activityName=='CPV' || activityName=='CPV_Analyst')
			{
			if(getNGValue('cmplx_DEC_Decision')=='Approve' || getNGValue('cmplx_DEC_Decision')=='Refer' || getNGValue('cmplx_DEC_Decision')=='Reject')
			{
			NoOfAttemptsValue=NoOfAttemptsValue+1;
			if (getNGValue('cmplx_DEC_NoofAttempts')==''){
			setNGValueCustom('cmplx_DEC_NoofAttempts','1');
			}
			else if (getNGValue('cmplx_DEC_NoofAttempts')!=''&& NoOfAttemptsValue==1){
			var toSet;
			toSet=parseInt(getNGValue('cmplx_DEC_NoofAttempts'))+1;
			setNGValueCustom('cmplx_DEC_NoofAttempts',toSet);
			}
			}
			}
			return true;
	 }
	 
	else if((activityName=='FCU' || activityName=='Compliance') && getNGValue('cmplx_DEC_Decision')=='Refer' && validateReferGridonRefer())
	{
	
	return true;
	}	
			
	}
	
	//Added by aman for Understanding gap
	if(activityName=='FCU' ){
	 var EmpType=getNGValue('EmploymentType');
			if(EmpType=='Self Employed' ){
			
			
	 if(checkMandatory_EmpVerification()){
					return true;}
					
			}
			
		else{
	  if(!checkMandatory_EmpVerification()){
	 return false;}
	 if(!checkMandatory_saveBussinessVeri()){
	 return false;}
	 if (getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU')>0){
	 
           for(var i=0;i<getLVWRowCount('cmplx_SmartCheck1_SmartChkGrid_FCU');i++)
			{
				if(getLVWAT('cmplx_SmartCheck1_SmartChkGrid_FCU',i,2)==''){
				showAlert('cmplx_SmartCheck1_SmartChkGrid_FCU','Please fill smart check');
					return false;
				}
			
		
	 }}
	}}//DOne by aman for Sprint 2
	//Added by aman for Understanding gap
	/*
	if((getNGValue('cmplx_DEC_Decision')=='Reject' ) && (getNGValue('DecisionHistory_dec_reason_code')=='--Select--' || getNGValue('DecisionHistory_dec_reason_code')=='' )){
		showAlert('DecisionHistory_dec_reason_code',alerts_String_Map['CC167']);
		return false;
	}
	
	if((getNGValue('cmplx_DEC_Decision')=='Reject' || getNGValue('cmplx_DEC_Decision')=='Refer')  )
	{
	if((activityName=='FCU' || activityName=='Compliance') && getNGValue('cmplx_DEC_Decision')=='Refer' && validateReferGridonRefer())
	{
	return true;
	}
	else if((activityName!='FCU' || activityName!='Compliance'))
	{
		var desc = getNGValue('cmplx_DEC_Decision');
		if(activityName=='CAD_Analyst1' && (desc.toUpperCase()=='REFER' || desc.toUpperCase()=='REJECT'))
		{
			if(desc.toUpperCase()=='REFER' && (com.newgen.omniforms.formviewer.getNGSelectedItemText('DecisionHistory_ReferTo')=='' || com.newgen.omniforms.formviewer.getNGSelectedItemText('DecisionHistory_ReferTo')=='--Select--'))
			{
			showAlert('DecisionHistory_ReferTo',alerts_String_Map['CC255']);
					return false;
			}
			else if(getNGValue('DecisionHistory_dec_reason_code')=='' || getNGValue('DecisionHistory_dec_reason_code')=='--Select--')
			{
			showAlert('DecisionHistory_dec_reason_code',alerts_String_Map['VAL198']);
					return false;
			}
			else if(com.newgen.omniforms.formviewer.getNGSelectedItemText('DecisionHistory_ReferTo')!='Smart CPV' && (getNGValue('cmplx_DEC_ReferTo')=='' || getNGValue('cmplx_DEC_ReferTo')=='--Select--') && getNGValue('cmplx_DEC_ReferTo')!='A'){
				showAlert('cmplx_DEC_ReferTo',alerts_String_Map['CC271']);
					return false;
			}
			else{
				return true;
			}
		}
		else
		{
	return true;
	}
	}
	else{
		return false;
	}
		
	}*/
	
	//var activityName = window.parent.stractivityName;
	//Added by aman 
	var addressrowcount=getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
	// changes done to make decision mandatory - bug correct --Select-- condition also added
	//below code changed by nikhil 
  
  		//below lines commented by akshay on 23/11/18

  /*  if (getNGValue('cmplx_DEC_Decision') == null || getNGValue('cmplx_DEC_Decision') == '' || getNGValue('cmplx_DEC_Decision') == '--Select--' )
    {
        showAlert('cmplx_DEC_Decision', alerts_String_Map['CC166']);
        return false;
    }
	 else if(getNGValue('cmplx_DEC_Remarks')=='')
    {
    	showAlert('cmplx_DEC_Decision', alerts_String_Map['CC190']);
        return false;
    }*/
	
	 if(activityName=='Compliance')
	{
		var worldAdd = getNGValue('Is_WorldCheckAdd'); // flag check if row is added in world check grid
		if(worldAdd=='Y')
		{			  
			var n=getLVWRowCount("cmplx_Compliance_cmplx_gr_compliance");			
				if(n==0){
					//change by saurabh on 3rd July for Manish showstopper point 18.	
					var flag=false;
						for(var i=0;i<getLVWRowCount('cmplx_WorldCheck_WorldCheck_Grid');i++){
							if(getLVWAT('cmplx_WorldCheck_WorldCheck_Grid',i,17).indexOf('true')>-1){
								flag=true;
							}
						}
						if(flag){
						showAlert('cmplx_Compliance_cmplx_gr_compliance',alerts_String_Map['CC143']);
						return false;			
						}
				}
				if(n>=1)
				{
					if(getLVWAT("cmplx_Compliance_cmplx_gr_compliance",0,5)=='')
					{
						showAlert('cmplx_Compliance_cmplx_gr_compliance',alerts_String_Map['CC143']);
						return false;
					}
				}			
		}
		
			//below lines commented by akshay on 23/11/18
		/*//changed by akshay on 9/12/17 as this condition was wrong
		if (getNGValue('cmplx_DEC_Decision') == 'REJECT' && isFieldFilled('DecisionHistory_dec_reason_code')==false)
		{
			showAlert('DecisionHistory_dec_reason_code', alerts_String_Map['CC167']);
			return false;
		}
		//12th september*/
	}
	//Code corrected by deepak(= is corrected to ==) 30Dec 2018
	//done by sagarika for pcsp-360
		else if (activityName=='Original_Validation')
	{
	if(getNGValue('cmplx_DEC_Decision') == 'Approve')
	{
	setNGValueCustom("ORIGINAL_VALIDATION", "Yes");
	}
	else if(getNGValue('cmplx_DEC_Decision') == 'Reject')
	{
	setNGValueCustom("ORIGINAL_VALIDATION", "No");
	}
	}
	
	else if(activityName=='Disbursal')
	{
		var lvname = 'cmplx_CCCreation_cmplx_CCCreationGrid';
		var rows = getLVWRowCount(lvname);
		var pass = true;
		if(rows>0){
			for(var i=0;i<rows;i++){
				if(getLVWAT(lvname,i,8)!='Y' || getLVWAT(lvname,i,9)!='Y'){
				pass = false;
				break;
				}
			}
		}
		if(!pass){
			showAlert(lvname, 'Kindly complete disbursal of all the cards');
			return false;
		}
	}
	//below code added by nikhil
	else if(activityName=='DDVT_Maker')
	{
		//added by nikhil for CAS Simplication
		if(!Customer_Save_Check1()){
				return false;
			}
		if (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed' && getNGValue('cmplx_Customer_VIPFlag')==false){
			if(!checkMandatory(CC_EMPLOYMENTDETAILS)){
			
				return false;
			}
			else if(getNGValue('cmplx_EmploymentDetails_EMpCode')== '' )
				{
					showAlert('cmplx_EmploymentDetails_EMpCode',alerts_String_Map['VAL332']);
					return false;
				}//Arun (11/12/17)
		}
		if(getNGValue('cmplx_Customer_VIPFlag')==true)
		{
			if(getNGValue('cmplx_EmploymentDetails_EMpCode')== '' )
				{
					showAlert('cmplx_EmploymentDetails_EMpCode',alerts_String_Map['VAL372']);
					return false;
				}
				if(getNGValue('cmplx_EmploymentDetails_EmpName')== '' )
				{
					showAlert('cmplx_EmploymentDetails_EmpName',alerts_String_Map['VAL372']);
					return false;
				}
				if(getNGValue('cmplx_EmploymentDetails_ClassificationCode')== '' ||  getNGValue('cmplx_EmploymentDetails_ClassificationCode')== '')
				{
					showAlert('cmplx_EmploymentDetails_ClassificationCode',alerts_String_Map['VAL313']);
					return false;
				}
				if(getNGValue('cmplx_EmploymentDetails_marketcode')== '' ||  getNGValue('cmplx_EmploymentDetails_marketcode')== '')
				{
					showAlert('cmplx_EmploymentDetails_marketcode',alerts_String_Map['VAL242']);
					return false;
				}
		}
		//added by aman for PCSP-94
		//income save not required at DDVT
		/*if((getNGValue('cmplx_Customer_VIPFlag')==false || getNGValue('EmploymentType')=='Pensioner') ){
		if(!Income_Save_Check())
				{
				return false;
				}}*/
		if(getNGValue('cmplx_EligibilityAndProductInfo_FinalLimit')=='')
		{
		showAlert('cmplx_EligibilityAndProductInfo_FinalLimit','Final Limit Cannot be Blank!');
		return false;
		}
	//added by akshay on 12/1/18
		if(!checkForNA_Grids('cmplx_AddressDetails_cmplx_AddressGrid')){
			return false;
		}
		if(!checkMandatory_AddressDetails_Save()){
			return false;
		}
		if(!checkMandatory_ContactDetails_save())
		{
			return false;
			}
		//added by nikhil for PCAS-2235
		if(!checkMandatory_CardDetails_Save())
		{
		return false;
		}

if(!checkFor_SupplemntGrid()){
			return false;
		}		
	var row_count=getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');
	var cif_found=false;
	var ntb_case=false;
		if(row_count>0)
		{
		for(var i=0;i<row_count;i++)
			{
				if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)!=getNGValue('cmplx_Customer_CIFNo') && getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=="true" )
				{
					cif_found=true;
				}
				if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=="true" )
				{
					ntb_case=true;
				}
			}
			//change by saurabh on 20th Dec	
			if(ntb_case && getNGValue('cmplx_Customer_NTB')==true && getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)!=''){
				showAlert('cmplx_Customer_NTB', ' Customer cannot be NTB when cif has been identified in Part Match');
				setNGValueCustom('cmplx_Customer_NTB',false);
				return false;
			}
			/*if(!cif_found)//cif_found changed to !cif_found by akshay on 3/4/18 for proc 7499
			{
			showAlert('cmplx_Customer_CIFNO', ' CIF which is considered for obligations is not preset in Personal details');
			return false;
			}----commented temporary */
		}
		
		if(getNGValue('cmplx_Customer_NTB')==false){
			//Added By Shivang for JIRA PCAS1403
			var obligConsFlag = false;
			if(row_count>0)
				{
				  for(var i=0;i<row_count;i++){
					  
					  if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=="true"){
						  obligConsFlag=true;
						  break;
					  }
				  }
				  if(!obligConsFlag){
					  showAlert('',alerts_String_Map['CC295']);
					  return false;
				  }
				}
			else if(row_count == 0){
				showAlert('',alerts_String_Map['CC296']);
				return false;
			}
		}
		
		//added by saurabh for JIRA
		if (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed' && getNGValue('cmplx_Customer_VIPFlag')==false){
			if(!checkMandatory(CC_EMPLOYMENTDETAILS)){
				return false;
			}	
		}
		//change by saurabh
		if(getNGValue('is_cc_waiver_require')!='Y' && getNGValue('Sub_Product')!='PU'){//changed from LI to PU by akshay on 3/5/18 for proc 8978
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
			if(SecurityGridCount==0 && getNGValue('Sub_Product')!='LI' && !Check_Elite_Customer()){
				showAlert('CardDetails_BankName', alerts_String_Map['CC264']);
				return false;
			}
		}
	if(!checkForNA_Grids('cmplx_FATCA_cmplx_GR_FatcaDetails')){
			return false;
		}
	else if(!checkForNA_Grids('cmplx_KYC_cmplx_KYCGrid')){
			return false;
		}
	else if(!checkForNA_Grids('cmplx_OECD_cmplx_GR_OecdDetails')){
			return false;
		}	
	//change by saurabh for incoming doc defer waiv functionality. 5 Feb 19.
	Check_Documents_Submit();
	}
	//added by akshay on 21/5/18 for mandating cif to be created
	else if(activityName=='DDVT_Checker')
	{
		for(var i=0;i<getLVWRowCount('cmplx_DEC_MultipleApplicantsGrid');i++)
		{
			if(getLVWAT('cmplx_DEC_MultipleApplicantsGrid',i,3)==''){
					showAlert('','CIF for applicant: '+getLVWAT('cmplx_DEC_MultipleApplicantsGrid',i,1).bold()+' not created!!');
					return false;
			}
		}
	}
	//added by y
	//commented by akshay on 23/11/18
	/*
	else if (activityName=='Original_Validation'){
		 if (getNGValue('cmplx_DEC_Decision') == ''||getNGValue('cmplx_DEC_Decision') == '--Select--')
		{
			showAlert('cmplx_DEC_Decision', alerts_String_Map['CC166']);
			return false;
		}
		 if (getNGValue('cmplx_DEC_Decision') == 'REJECTED')
		{
			if(getNGValue('DecisionHistory_dec_reason_code') ==''||getNGValue('DecisionHistory_dec_reason_code')=='--Select--')
			showAlert('DecisionHistory_dec_reason_code', alerts_String_Map['CC167']);
			return false;
		}
		
	}*/
	// ++ above code already present - 06-10-2017 dmandatorycc validation
	else if(activityName=='CAD_Analyst1' || activityName=='Cad_Analyst2')
	{
		//++below code added by nikhil for PCAS-364 CR
		if(activityName=='CAD_Analyst1' && getNGValue('Eligibility_Trigger')=='Y')
		{
			showAlert('ELigibiltyAndProductInfo_Button1',alerts_String_Map['VAL388']);
			return false;
		}
		if(flag_add_new==true)//sagarika for CAM
		{
			showAlert('DecisionHistory_Button4',alerts_String_Map['VAL391']);
			return false;
		}
		
		//--above code added by nikhil for PCAS-364 CR
	//below code addded for JIRa Income/employment check
		if (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)!='Self Employed' && getNGValue('cmplx_Customer_VIPFlag')==false){
			if(!checkMandatory(CC_EMPLOYMENTDETAILS)){
				return false;
			}	
		}
		//added by aman for PCSP-94
		if((getNGValue('cmplx_Customer_VIPFlag')==false || getNGValue('EmploymentType')=='Pensioner') ){
		if(!Income_Save_Check())
				{
				return false;
				}}
		var desc = getNGValue('cmplx_DEC_Decision');
		
		if(isVisible('cmplx_CompanyDetails_cmplx_CompanyGrid')){
			for(var i=0;i<getLVWRowCount('cmplx_CompanyDetails_cmplx_CompanyGrid');i++){
				if(getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,1)=='Business'){
					if(getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',i,28)==''){
						showAlert('cmplx_CompanyDetails_cmplx_CompanyGrid', 'Marketing code of the company cannot be blank');
						return false;
					}
				}
			}
		}
		
		
		if(getNGValue('cmplx_DEC_HighDeligatinAuth')=='' && getNGValue('DectechCategory')!='A' && desc=='Approve')
		{
			
				showAlert('cmplx_DEC_HighDeligatinAuth',alerts_String_Map['CC256']);
				return false;
			
		}
		
		if(getNGValue('cmplx_DEC_Manual_Deviation')==true)
		{
			if(getNGValue('cmplx_DEC_Manual_Dev_Reason')=='' )
			{
				showAlert('DecisionHistory_ManualDevReason',alerts_String_Map['CC146']);
				return false;
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
		
		else if(desc=='Refer to Smart CPV')
		{
			var n=getLVWRowCount("cmplx_SmartCheck_SmartCheckGrid");
			if(n==0)
			{
				showAlert('cmplx_SmartCheck_SmartCheckGrid',alerts_String_Map['CC054']);
			}	

				//++ below code added by abhishek as per CC FSD 2.7.3
					var parts = getNGValue("DecisionHistory_CadTempField").split('#');
					var workstepValues = parts[0].split('|');
					var flag = true;
					for(var i=0; i<workstepValues.length; i++){
						if(workstepValues[i]=='CPV'){
							flag = false;
						}
					}
					if(flag==true){
						var retVal = confirm("CPV Decision is "+parts[1]+" do you still want to continue?");
					   if( retVal == true ){
						  
						return true;
					   }
					    else{
						
						  return false;
					   } 
					}
				//-- Above code added by abhishek as per CC FSD 2.7.3
		}
		 //validation for customer detail verification CPV changes 16-04
		if ((getNGValue('cmplx_CustDetailVerification_Decision') == '' || getNGValue('cmplx_CustDetailVerification_Decision') == '--Select--')  && activityName=='CAD_Analyst1' )
		{
			showAlert('cmplx_CustDetailVerification_Decision', alerts_String_Map['CC165']);
			return false;
		}
		//below lines commented by akshay on 23/11/18
		/*
		else if(desc=='Refer to Credit')
		{
			if(getNGValue('cmplx_DEC_ReferTo')=='' || getNGValue('cmplx_DEC_ReferTo')=='--Select--')
			{
				showAlert('cmplx_DEC_ReferTo',alerts_String_Map['CC142']);
							return false;
			}
		}*/
		/*
		else if(getNGValue('cmplx_IncomeDetails_Accomodation')=='Yes' && (getNGValue('cmplx_IncomeDetails_Accomodation_Value')=='' || getNGValue('cmplx_IncomeDetails_Accomodation_Value')==' ')){
				showAlert('cmplx_IncomeDetails_Accomodation_Value',alerts_String_Map['CC002']);
				return false;
			}*/
		else if(desc=='APPROVE' && activityName=='CAD_Analyst1' && getNGValue('cmplx_DEC_DeviationCode')==''){
			showAlert('cmplx_DEC_DeviationCode',alerts_String_Map['CC071']);
				return false;
		}
	}
	//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
	else if(activityName=='CAD_Analyst1')
	{		
	/* commented by saurabh to overcome showstopper
		if(getNGValue('EFMS_IS_Alerted') == '' || getNGValue('EFMS_IS_Alerted').toLowerCase() == 'null' ){
			alert("Cannot submit Workitem as EFMS status not fetched");
			return false;
		}
		*/
		//added by saurabh for JIRA
		
		
	}
	//-- Above code added  by abhishek on 04/01/2018 for EFMS refresh functionality
	else if(activityName=='Smart_CPV')
	{
		var n=getLVWRowCount("cmplx_SmartCheck_SmartCheckGrid");
		for (var i=0;i<n;i++)
		{
		//changed by nikhil for change in column no as wrong written 
		if(getLVWAT("cmplx_SmartCheck_SmartCheckGrid",i,1)=='')
			{
			showAlert('cmplx_SmartCheck_SmartCheckGrid',alerts_String_Map['CC144']);
			return false;
			}
		}
			
		 // added by abhishek as per CC FSD
			if(!checkSmartCPVDec()){
				alert('Cannot Submit WorkItem as the decision is Hold');
				return false;
			}
			//++ Below code added by abhishek as per CC FSD 2.7.3
			//++ Below code added by nikhil on 10/10/17
			//added by nikhil as per CC FSD
			//below lines commented by akshay on 23/11/18
			/*
			if(getNGValue('cmplx_DEC_Decision').toUpperCase()=='REJECT' && getNGValue('DecisionHistory_dec_reason_code').toUpperCase()=='--SELECT--' && activityName=='Smart_CPV')
			{
				alert(alerts_String_Map['VAL198']);
				return false;
			}*/
			//-- Above code added by nikhil on 10/10/17
			//-- Above code added by abhishek as per CC FSD 2.7.3
	}
	else if(activityName=='FCU')
	{
		var desc = getNGValue('cmplx_DEC_Decision');
	
		
        if(desc=='Refer to Smart CPV')
		{
			var n=getLVWRowCount("cmplx_SmartCheck_SmartCheckGrid");
			if(n==0)
			{
				showAlert('cmplx_SmartCheck_SmartCheckGrid',alerts_String_Map['CC145']);
			}
		}
//added 6th sept for proc 2021
		
		if (isFieldFilled('cmplx_DEC_FeedbackStatus')==false)
			{
				showAlert('cmplx_DEC_FeedbackStatus',alerts_String_Map['CC168']);
				return false;
			}
		
		//ended 6th sept for proc 2021	
	}
	//12th september
	else if(activityName=='Rejected_queue'){
		 if (getNGValue('cmplx_DEC_Decision') == '' || getNGValue('cmplx_DEC_Decision') == '--Select--')
		{
			showAlert('cmplx_DEC_Decision', alerts_String_Map['CC166']);
			return false;
		}
		 if (getNGValue('cmplx_DEC_RejectReason') == '' )
		{
			showAlert('cmplx_DEC_RejectReason', alerts_String_Map['CC153']);
			return false;
		}
		
	}
	//12th september
	
	// changes done to make CPV fragment tab decisions mandatory
	else if(activityName=='CPV')
	{
		 if (getNGValue('cmplx_HCountryVerification_Decision') == '' || getNGValue('cmplx_HCountryVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_HCountryVerification_Decision', alerts_String_Map['CC169']);
			return false;
		}
		 if (getNGValue('cmplx_ResiVerification_Decision') == '' || getNGValue('cmplx_ResiVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_ResiVerification_Decision', alerts_String_Map['CC174']);
			return false;
		}
		//commented by nikhi for CPV changes 16-04
		/* if (getNGValue('cmplx_CustDetailVerification_Decision') == '' || getNGValue('cmplx_CustDetailVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_CustDetailVerification_Decision', alerts_String_Map['CC165']);
			return false;
		}*/
		 if (getNGValue('cmplx_BussVerification_Decision') == '' || getNGValue('cmplx_BussVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_BussVerification_Decision', alerts_String_Map['CC162']);
			return false;
		}
		 if (getNGValue('cmplx_RefDetVerification_Decision') == '' || getNGValue('cmplx_RefDetVerification_Decision') == '--Select--')
		{
			showAlert('cmplx_RefDetVerification_Decision', alerts_String_Map['CC173']);
			return false;
		}
		 if ((getNGValue('cmplx_OffVerification_Decision') == '' || getNGValue('cmplx_OffVerification_Decision') == '--Select--') && !(isLocked('cmplx_OffVerification_Decision') && !isEnabled('OfficeandMobileVerification_Enable')))
		{
			showAlert('cmplx_OffVerification_Decision', alerts_String_Map['CC172']);
			return false;
		}
		 if (getNGValue('cmplx_LoanandCard_Decision') == '' || getNGValue('cmplx_LoanandCard_Decision') == '--Select--')
		{
			showAlert('cmplx_LoanandCard_Decision', alerts_String_Map['CC170']);
			return false;
		}		
	}
	
	// changes done to make decision mandatory - bug correct --Select-- condition also added
   else  if (getNGValue('cmplx_DEC_Decision') == '' || getNGValue('cmplx_DEC_Decision') == '--Select--')
    {
        showAlert('cmplx_DEC_Decision', alerts_String_Map['CC166']);
        return false;
    }
	
	// changes done to restrict user if any of the decision is pending in CPV tab fragments than in main decision tab user cannot take Decision as Approve
	else if(activityName=='CPV')
	{
	//changed by nikhil for CPV changes 16-04
		if (getNGValue('cmplx_HCountryVerification_Decision') == 'Pending' || getNGValue('cmplx_ResiVerification_Decision') == 'Pending'  || getNGValue('cmplx_BussVerification_Decision') == 'Pending' || getNGValue('cmplx_RefDetVerification_Decision') == 'Pending' || getNGValue('cmplx_OffVerification_Decision') == 'Pending' || getNGValue('cmplx_LoanandCard_Decision') == 'Pending')
		{
			if (getNGValue('cmplx_DEC_Decision') == 'Approve')
			{
				showAlert('cmplx_DEC_Decision', alerts_String_Map['CC029']);
				return false;
			}
		}
		// condition added by saurabh on 28th nov for drop 2
		if(getNGValue('cmplx_OffVerification_hrdnocntctd')=='Yes' && (getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=='' || getNGValue('cmplx_OffVerification_offtelnovalidtdfrom')=='--Select--')){
			showAlert('cmplx_OffVerification_offtelnovalidtdfrom', alerts_String_Map['CC241']);
			return false;
		}
	}
	
	// changes done to make decision mandatory - bug correct --Select-- condition also added
   else  if (getNGValue('cmplx_DEC_Decision') == '' || getNGValue('cmplx_DEC_Decision') == '--Select--')
    {
        showAlert('cmplx_DEC_Decision', alerts_String_Map['CC166']);
        return false;
    }
	
	
	else if(activityName!='Initiation')
	{
		var mField =  CC_DECISION_MANDATORY_FIELDS.split(",");
		for(var i = 0; i < mField.length; i++) {
			var j = mField[i].toString().split("#");
				if(!isFieldFilled(j[0],j[1])){
				showAlert(j[0],j[1]+" is mandatory.");
				return false;				
				}
		}
		
		if(activityName=='DDVT_Checker'){
			if(getNGValue('cmplx_DEC_New_CIFID')=='' && getNGValue('cmplx_Customer_NTB') && getNGValue('cmplx_Customer_CIFNo')==''){
				showAlert('cmplx_DEC_New_CIFID', 'Please create CIF first');
				return false;
			}
		
		}
		//added by saurabh on 1st Feb
		if(activityName=='CSM'){
			if(!Customer_Save_Check1()){
				return false;
			}
			if(!checkMandatory_AddressDetails_Save()){
			return false;
			}
			//change by saurabh for incoming doc defer waiv functionality. 5 Feb 19.
			Check_Documents_Submit();
			if(!checkMandatory(CC_ELIGIBILITY))
					return false;
		}
		
		
	}
	
	
	if(!Is_NotepadDetails_Modified(activityName)){
		return false;
	}
	//code added by aman to check address validation
	
			else if(addressrowcount!=0)
			{
				if(!checkPrefferedChoice_1()){
				return false;
				}
			}
//below code commented by akshay on 23/11/18
	/*
	if(getNGValue('cmplx_DEC_Decision')=='Refer' && com.newgen.omniforms.formviewer.getNgSelectedValues('DecisionHistory_ReferTo')==''){
        showAlert('DecisionHistory_ReferTo', alerts_String_Map['CC255']);
        return false;
    }
	*/
	
	if(activityName=='FCU' || activityName=='Compliance' || activityName=='DSA_CSO_Review' || activityName=='CPV_Analyst' || activityName=='RM_Review'){
		if(!validateReferGrid())
		{
			return false;
		}
	}
	//prabhakar jira no 1231
	if(isVisible('Liability_New_Overwrite')==true && getNGValue('Liability_New_overwrite_flag')=='N')
					{
						showAlert('InternalExternalLiability',alerts_String_Map['VAL390']);
						return false;
					}
	//ended by akshay on 12/9/17
	if(activityName=='CPV' || activityName=='CPV_Analyst')
	{
		if(getNGValue('cmplx_DEC_Decision')=='Approve' || getNGValue('cmplx_DEC_Decision')=='Refer' || getNGValue('cmplx_DEC_Decision')=='Reject')
		{
		NoOfAttemptsValue=NoOfAttemptsValue+1;
		if (getNGValue('cmplx_DEC_NoofAttempts')==''){
		setNGValueCustom('cmplx_DEC_NoofAttempts','1');
		}
		else if (getNGValue('cmplx_DEC_NoofAttempts')!=''&& NoOfAttemptsValue==1){
		var toSet;
		toSet=parseInt(getNGValue('cmplx_DEC_NoofAttempts'))+1;
		setNGValueCustom('cmplx_DEC_NoofAttempts',toSet);
		}
		}
	}
	return true;
}


// added by abhishek
function checkMandatory_ReferenceGrid()
{
	if(getNGValue('Reference_Details_ReferenceName')==""){
		showAlert('Reference_Details_ReferenceName',alerts_String_Map['VAL126']);
		return false;
		}
	else if(getNGValue('Reference_Details_ReferenceMobile')==""){
		showAlert('Reference_Details_ReferenceMobile',alerts_String_Map['VAL125']);
		return false;
		}

	else if(getNGValue('Reference_Details_ReferenceRelatiomship')=="--Select--" || getNGValue('Reference_Details_ReferenceRelatiomship')=="")
	{
		showAlert('Reference_Details_ReferenceRelatiomship',alerts_String_Map['VAL127']);
		return false;
	}
	return true;
}
function validate_ReferenceGrid()
{
	var n=getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
	if(n==2){
		showAlert('cmplx_RefDetails_cmplx_Gr_ReferenceDetails',alerts_String_Map['CC119']);
		return false;
	}
	
	if(getLVWAT('cmplx_RefDetails_cmplx_Gr_ReferenceDetails',0,0)==getNGValue('Reference_Details_ReferenceName') && getLVWAT('cmplx_RefDetails_cmplx_Gr_ReferenceDetails',0,1)==getNGValue('Reference_Details_ReferenceMobile')){
		showAlert('cmplx_RefDetails_cmplx_Gr_ReferenceDetails',alerts_String_Map['VAL005']);
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
	else if(ReqLimit=="" && SubProd!='PU'){
		showAlert('ReqLimit',alerts_String_Map['VAL130']);
		return false;
		}
	
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
		
	else if(ReqProd=='Credit Card' &&	(SubProd!='IM' && SubProd!='LI')){
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
		if(LastPermanentLimit=='' && getNGValue('AppType')=='P'){
			showAlert("LastPermanentLimit",alerts_String_Map['VAL079']);
				return false;
		}
			
		else if(LastTemporaryLimit=='' && getNGValue('AppType')=='P'){
			showAlert("LastTemporaryLimit",alerts_String_Map['VAL091']);
				return false;
			}
		else if((getNGValue('AppType')=='T' || getNGValue('AppType')=='NPULI') && NoOfMonths==''){
			showAlert("NoOfMonths",alerts_String_Map['VAL334']);
				return false;
			}
		}
		
		else if(SubProd=='SEC')
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

function Product_add_validate()
{
		var reqProd=getNGValue("CardProduct");
		var EmpType=getNGValue("EmpType");
		//var priority=getNGValue("cmplx_Product_cmplx_ProductGrid_Priority");
		var priority=getNGValue("Priority");
		
		//alert('priority'+priority);
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		
		if(n==0)
		{
			if(priority=='Primary'){
				return true;
			}	
			else{
				showAlert('Priority',alerts_String_Map['CC089']);
				return false;
				}
				
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
					
					if(grid_Prod=="Personal Loan" && grid_Prod==reqProd){
						showAlert('reqProd',alerts_String_Map['CC027']);
						return false;
					}	
					
					if(grid_Priority=="Primary" && grid_Priority==priority){
								showAlert('Priority',alerts_String_Map['CC028']);
								return false;
					}			
				}
			}
			
		if(reqProd=='Personal Loan')
			setNGValueCustom('LimitAcc','NA');
		
		else if(reqProd=='Credit Card')	{
			//setNGValueCustom('Scheme','NA');
			setNGValueCustom('ReqTenor','NA');
		}

	return true;
}	

function Product_modify_validate()
{
		var ReqProd=getNGValue("ReqProd");
		var EmpType=getNGValue("EmpType");
		var priority=getNGValue("Priority");
		var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		
		if(n==1)
		{
			if(priority=='Primary'){
				
				return true;
			}	
			else {
					showAlert('Priority',alerts_String_Map['CC089']);
					return false;
				}
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
					
					if(grid_Prod=="Personal Loan" && grid_Prod==ReqProd && com.newgen.omniforms.formviewer.getLVWAT("cmplx_Product_cmplx_ProductGrid",com.newgen.omniforms.formviewer.getNGListIndex("cmplx_Product_cmplx_ProductGrid"),1)!=ReqProd){
						showAlert('ReqProd',alerts_String_Map['CC027']);
						return false;
					}	
					
					if(grid_Priority=="Primary" && grid_Priority==priority){
								showAlert('Priority',alerts_String_Map['CC028']);
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
//Tanshu Aggarwal Reference Details Mandatory Check (17/08/2017)
function checkMandatory_RefDetailsGrid()
{
	var RefName=getNGValue('Reference_Details_ReferenceName');
	var RefMobile=getNGValue('Reference_Details_ReferenceMobile');
	var RefRelationship=getNGValue('Reference_Details_ReferenceRelatiomship');
	var RefAddress=getNGValue('Reference_Details_ReferenceAddress');
	var RefPhone=getNGValue('Reference_Details_ReferencePhone');
	
	if(getNGValue('Reference_Details_ReferenceName')==""){
		showAlert('Reference_Details_ReferenceName',alerts_String_Map['VAL126']);
		return false;
		}

	else if(getNGValue('Reference_Details_ReferenceMobile')==""){
		showAlert('Reference_Details_ReferenceMobile',alerts_String_Map['VAL125']);
		return false;
		}
	else if(getNGValue('Reference_Details_ReferenceRelatiomship')==""|| getNGValue('Reference_Details_ReferenceRelatiomship')=="--Select--"||getNGValue('Reference_Details_ReferenceRelatiomship')=='NA'){
		showAlert('Reference_Details_ReferenceRelatiomship',alerts_String_Map['CC188']);
		return false;
		}
	/*else if(RefAddress==""){
		showAlert('RefAddress',alerts_String_Map['CC183']);
		return false;
		}*/
	//else if(getNGValue('Reference_Details_ReferencePhone')==""){
		//showAlert('Reference_Details_ReferencePhone',alerts_String_Map['CC186']);
		//return false;
		//}
	return true;
	}
	
function checkMandatory_Reference_Details_Save(){
	var n=getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
	if(n==0){
		showAlert('cmplx_RefDetails_cmplx_Gr_ReferenceDetails',alerts_String_Map['CC131']);
		return false;
	}
	return true;
}

	function checkMandatory_CardDetails_Save(){
		var activity = window.parent.stractivityName;
		var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(Principal_Approval=='Y' && activityName=='CSM')
	{
		if(isFieldFilled('cmplx_CardDetails_cardEmbossing_name')==false)
		{
			showAlert('cmplx_CardDetails_cardEmbossing_name',alerts_String_Map['VAL029']);
			return false;
		}
		else  if(getNGValue('cmplx_CardDetails_SendStatementTO')=='')
		{
		  showAlert('cmplx_CardDetails_SendStatementTO',alerts_String_Map['VAL271']);
		  return false;
		}
		else
		{
			return true;
		}
	}
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
			
			if(getNGValue('cmplx_CardDetails_cardEmbossing_name')=="" && getNGValue('Sub_Product')!='LI'){
				showAlert('cmplx_CardDetails_cardEmbossing_name',alerts_String_Map['VAL029']);
				return false;
			}
			//change by saurabh on 7th Jan
			else if(getLVWAT("cmplx_Product_cmplx_ProductGrid",0,5).indexOf('KALYAN')>-1 && com.newgen.omniforms.formviewer.isEnabled('cmplx_CardDetails_CompanyEmbossing_name')){
				 if(isFieldFilled('cmplx_CardDetails_CompanyEmbossing_name')==false)
				{
					showAlert('cmplx_CardDetails_CompanyEmbossing_name',alerts_String_Map['VAL036']);
					return false;
				}
			}
			
			if(getNGValue('cmplx_CardDetails_SendStatementTO')==""){
				showAlert('cmplx_CardDetails_SendStatementTO',alerts_String_Map['CC200']);
				return false;
			}
			if(getNGValue('cmplx_CardDetails_Statement_cycle')==""){
				showAlert('cmplx_CardDetails_Statement_cycle',alerts_String_Map['VAL278']);
				return false;
			}
			if(getNGValue("cmplx_CardDetails_Security_Check_Held")==true && getLVWRowCount('cmplx_CardDetails_cmpmx_gr_cardDetails')==0){
				if(getNGValue('cmplx_CardDetails_Bank_Name')==""){
					showAlert('cmplx_CardDetails_Bank_Name',alerts_String_Map['CC018']);	
					return false;
				}
				else if(getNGValue('cmplx_CardDetails_Cheque_Number')==""){
					showAlert('cmplx_CardDetails_Cheque_Number',alerts_String_Map['CC037']);	
					return false;
				}
				else if(getNGValue('cmplx_CardDetails_Amount')==""){
					showAlert('cmplx_CardDetails_Amount',alerts_String_Map['CC007']);	
					return false;
				}
				else if(getNGValue('cmplx_CardDetails_Date_Cont')==""){
					showAlert('cmplx_CardDetails_Date_Cont',alerts_String_Map['CC058']);	
					return false;
				}
			}
			//++below code added by nikhil for Self-Supp CR
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
		//--above code added by nikhil for Self-Supp CR
			//12th september
			//change by saurabh on 7th Jan
			if(activity!='CSM' && getNGValue('is_cc_waiver_require')!='Y' && getNGValue('Sub_Product')!='LI'){
			var n=getLVWRowCount("cmplx_CardDetails_cmpmx_gr_cardDetails");
			if(n==0 && getNGValue("cmplx_CardDetails_Security_Check_Held")==true){
				showAlert('cmplx_CardDetails_cmpmx_gr_cardDetails','Security Cheque Details '+alerts_String_Map['CC097']);
				return false;
				}
			}				
			//12th september
			return true;
		}
//Tanshu Aggarwal Refernece Details Mandatory check(17/08/2017)

 /*function SetValueCustomer_CheckEligibility(){
		setNGValueCustom("cmplx_EligibilityAndProductInfo_FinalDBR","30");
		setNGValueCustom("cmplx_EligibilityAndProductInfo_FinalTAI","4500");
		setNGValueCustom("cmplx_EligibilityAndProductInfo_InterestRate","8%");
		setNGValueCustom("cmplx_EligibilityAndProductInfo_EMI","3000");
		setNGValueCustom("cmplx_EligibilityAndProductInfo_Tenor","4");
		
  }*/
  //added by yash on 11/8/2017
  function checkMandatory_CompanyGrid()
{	//code added by saurabh on 7th Nov 2017.
	var activityName = window.parent.stractivityName;
	//if(activityName!='CSM'){---------------commented on 11/1/18 by akshay for proc 3639
		if(getNGValue('ApplicationCategory')=='' || getNGValue('ApplicationCategory')=='--Select--'){
			showAlert('ApplicationCategory',alerts_String_Map['VAL161']);	
			return false;
		}
		else if(getNGValue('TargetSegmentCode')=='' || getNGValue('TargetSegmentCode')=='--Select--'){
			showAlert('TargetSegmentCode',alerts_String_Map['VAL312']);	
			return false;
		}
		else if((getNGValue('MarketingCode')=='' || getNGValue('MarketingCode')=='--Select--') && isVisible('MarketingCode')){
			showAlert('MarketingCode',alerts_String_Map['VAL242']);	
			return false;
		}
		else if((getNGValue('ClassificationCode')=='' || getNGValue('ClassificationCode')=='--Select--') && isVisible('ClassificationCode')){
			showAlert('ClassificationCode',alerts_String_Map['VAL313']);	
			return false;
		}
		/*else if(getNGValue('PromotionCode')=='' || getNGValue('PromotionCode')=='--Select--'){
			showAlert('PromotionCode',alerts_String_Map['VAL314']);	
			return false;
		}--commented by Akshay on 11/9/17 as it is non mandatory now*/
		//}
	if(getNGValue('CompanyDetails_CheckBox4')==true){
			// ++ below code already present - 06-10-2017 value of fields change
		if(getNGValue('cif')==''||getNGValue('cmplx_CompanyDetails_cmplx_CompanyGrid_CompanyCIF')=='')
		{
			showAlert('cmplx_CompanyDetails_cmplx_CompanyGrid_CompanyCIF',alerts_String_Map['VAL035']);
			return false;
		}
		else if(flag_Company_FetchDetails==false){
			showAlert('CompanyDetails_Button3',alerts_String_Map['VAL059']);
			return false;
		}	
	}
	else
	{
		if(isFieldFilled('appType')==false)
		{
			showAlert('appType',alerts_String_Map['VAL006']);	
			return false;
		}
			
		else if(getNGValue('compName')==""){
			showAlert('compName',alerts_String_Map['VAL039']);	
			return false;
			}
		//modified by akshay on 27/1/18	
		else if(isFieldFilled('compIndus')==false){
			showAlert('compIndus',alerts_String_Map['VAL038']);	
			return false;
			}
			
		else if( getNGValue('tlNo')==''){
			showAlert('tlNo',alerts_String_Map['VAL147']);	
			return false;
		}
		else if( getNGValue('TradeLicencePlace')==''){
			showAlert('CompanyDetails_TradeLicencePlace',alerts_String_Map['CC217']);	
			return false;
		}
		else if( getNGValue('TLIssueDate')==''){
			showAlert('CompanyDetails_DatePicker1',alerts_String_Map['CC215']);	
			return false;
		}
		
		else if( getNGValue('TLExpiry')==''){
			showAlert('TLExpiry',alerts_String_Map['VAL146']);	
			return false;
		}
			
		else if(isFieldFilled('indusSector')==false){
			showAlert('indusSector',alerts_String_Map['VAL072']);	
			return false;
			}
			
		else if(isFieldFilled('indusMAcro')==false){
			showAlert('indusMAcro',alerts_String_Map['VAL070']);	
			return false;
			}
			
		else if(isFieldFilled('indusMicro')==false){
			showAlert('indusMicro',alerts_String_Map['VAL071']);	
			return false;
		}
		
		////else if(isFieldFilled('desig')==false){
		//	showAlert('desig',alerts_String_Map['VAL048']);	
		//	return false;
		//}
		
		//else if(isFieldFilled('desigVisa')==false){
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
		/*else if(getNGValue('cmplx_CompanyDetails_cmplx_CompanyGrid_Grt40BussMob')=='--Select--'){
			showAlert('cmplx_CompanyDetails_cmplx_CompanyGrid_Grt40BussMob',alerts_String_Map['VAL065']);	
			return false;
		}*/
		//below code added by nikhil 17/1/18
		//else if( getNGValue('EmployerCategoryPL')=='' && (activityName!='CSM' && activityName!='DDVT_Maker')){
		//	showAlert('EmployerCategoryPL',alerts_String_Map['VAL338']);	
		//	return false;
		//}
		//else if( getNGValue('EmployerStatusCC')=='' && (activityName!='CSM' && activityName!='DDVT_Maker')){
		//	showAlert('EmployerStatusCC',alerts_String_Map['VAL339']);	
		//	return false;
		//}
		//else if( getNGValue('EmployerStatusPLExpact')=='' && (activityName!='CSM' && activityName!='DDVT_Maker')){
		//	//changed by nikhil. pls merge 22/1/18
		//	showAlert('EmployerStatusPLExpact',alerts_String_Map['VAL340']);	
		//	return false;
		//}
		//else if( getNGValue('EmployerStatusPLNational')==''&& (activityName!='CSM' && activityName!='DDVT_Maker')){
		//	showAlert('EmployerStatusPLNational',alerts_String_Map['VAL341']);	
		//	return false;
		//}
		
	}
return true;	
}

//Tanshu Aggarwal(16/08/2017) to check Address fields
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
				showAlert('AddressDetails_CustomerType',alerts_String_Map['CC266']);	
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
		
		function checkMandatory_AddressDetails_Add(){
		
			if(getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_AddrType')==""||getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_AddrType')=="--Select--")
			{
				showAlert('cmplx_AddressDetails_cmplx_AddressGrid_AddrType',alerts_String_Map['CC004']);	
				return false;
			}
			else if(getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_HouseNo')==""){
				showAlert('cmplx_AddressDetails_cmplx_AddressGrid_HouseNo',alerts_String_Map['VAL063']);	
				return false;
				}
			else if(getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_BuildingName')==""){
			showAlert('cmplx_AddressDetails_cmplx_AddressGrid_BuildingName',alerts_String_Map['CC022']);	
			return false;
			}
			else if(getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_State')==""||getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_State')=="--Select--"){
			showAlert('cmplx_AddressDetails_cmplx_AddressGrid_State',alerts_String_Map['VAL137']);	
			return false;
			}
			else if(getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_Country')==""||getNGValue('cmplx_AddressDetails_cmplx_AddressGrid_Country')=="--Select--"){
			showAlert('cmplx_AddressDetails_cmplx_AddressGrid_Country',alerts_String_Map['VAL040']);	
			return false;
			}
			//added by prabhakar
			else if(getNGValue('AddressDetails_CustomerType')==""||getNGValue('AddressDetails_CustomerType')=="--Select--")
			{
			showAlert('AddressDetails_CustomerType',alerts_String_Map['VAL349']);	
			return false;
			}
			return true;
		}

//Tanshu Aggarwal(16/08/2017) to check Address fields

//Tanshu Aggarwal Contact Details Mandatory check(16/08/2017)
function checkMandatory_ContactDetails_save(){
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(Principal_Approval=='Y' && activityName=='CSM')
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
	//CR61 Prabhakar
	//Deepak code corrected as extra PID is added with defining the same. 
	if (validateMail1('AlternateContactDetails_Email2') || getNGValue('AlternateContactDetails_Email2')=='')
	{
		
		setNGValueCustom('AlternateContactDetails_Email2',getNGValue('AlternateContactDetails_Email1'));
	}

// ++ below code already present - 06-10-2017 row count address of grid
	var n=getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
				for(var i=0;i<n;i++){
					 if(getNGValue('AlternateContactDetails_HomeCOuntryNo')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='HOME'){
					showAlert('AlternateContactDetails_HomeCOuntryNo',alerts_String_Map['CC101']);	
					return false;
					}
					if(getNGValue('AlternateContactDetails_OfficeNo')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='OFFICE'){
					showAlert('AlternateContactDetails_OfficeNo',alerts_String_Map['CC132']);	
					return false;
					}
					if(getNGValue('AlternateContactDetails_ResidenceNo')=="" && getLVWAT("cmplx_AddressDetails_cmplx_AddressGrid",i,0)=='RESIDENCE'){
					showAlert('AlternateContactDetails_ResidenceNo',alerts_String_Map['CC194']);	
					return false;
					}
					
					
			}
// ++ above code already present - 06-10-2017 row count of grid
	if(getNGValue('AlternateContactDetails_MobileNo1')=="")
			{
				showAlert('AlternateContactDetails_MobileNo1',alerts_String_Map['CC125']);	
				return false;
			}
			else if(getNGValue('AlternateContactDetails_MobNo2')==""){
				showAlert('AlternateContactDetails_MobNo2',alerts_String_Map['CC126']);	
				return false;
				}
			else if(getNGValue('AlternateContactDetails_HomeCOuntryNo')==""){
			showAlert('AlternateContactDetails_HomeCOuntryNo',alerts_String_Map['CC101']);	
			return false;
			}
			else if(getNGValue('AlternateContactDetails_OfficeNo')==""){
			showAlert('AlternateContactDetails_OfficeNo',alerts_String_Map['CC132']);	
			return false;
			}
			else if(getNGValue('AlternateContactDetails_Email1')==""){
			showAlert('AlternateContactDetails_Email1',alerts_String_Map['CC076']);	
			return false;
			}
			//added by nikhil for CR-CIF Creation FAIL
			//commented by sagarika for sp2 changes 
			/*else if(getNGValue('AlternateContactDetails_Email2')==""){
			showAlert('AlternateContactDetails_Email2',alerts_String_Map['VAL375']);	
			return false;
			}*/
			//else if(getNGValue('AlternateContactDetails_eStatementFlag')==""||getNGValue('AlternateContactDetails_eStatementFlag')=="--Select--"){
			//showAlert('AlternateContactDetails_eStatementFlag',alerts_String_Map['CC074']);	
			//return false;
			//}
			//added by nikhil drop-4
			if(getLVWRowCount('cmplx_Product_cmplx_ProductGrid')>0)
			{
	
			if((getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='LI' && (getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='TOPIM')) && (getNGValue('AlternateContactDetails_CardDisp')==""||getNGValue('AlternateContactDetails_CardDisp')=="--Select--")){
			showAlert('AlternateContactDetails_CardDisp',alerts_String_Map['VAL169']);	
			return false;
			}
			}
		return true;
	}
//Tanshu Aggarwal Contact Details Mandatory check(16/08/2017)

//Tanshu Aggarwal Card Details Mandatory check(16/08/2017)
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
	/*if(getNGValue('cmplx_CardDetails_MarketingCode')=="" || getNGValue('cmplx_CardDetails_MarketingCode')=="--Select--"){
		showAlert('cmplx_CardDetails_MarketingCode',alerts_String_Map['PL129']);	
		return false;
	}---commented by akshay on 10/4/18 as this ifield s dropped*/
	else if(getNGValue('cmplx_CardDetails_CustClassification')=="" || getNGValue('cmplx_CardDetails_CustClassification')=="--Select--"){
		showAlert('cmplx_CardDetails_CustClassification',alerts_String_Map['CC258']);	
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
	}/*
	else if(getNGValue('CardDetails_ECRN')==""){
		showAlert('CardDetails_ECRN',alerts_String_Map['CC075']);
		return false;
	}
	else if(getNGValue('CardDetails_CRN')==""){
		showAlert('CardDetails_CRN',alerts_String_Map['CC055']);
		return false;
	}*/
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
function KYC_add_Check(optype){
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
			showAlert('KYC_CustomerType',alerts_String_Map['VAL006']);
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
					if(gridValue.indexOf(CustType)>-1 && optype=='add')
					{
						showAlert('KYC_CustomerType','KYC Already Added for Customer '+CustType);
						return false;
						break;
					}
				}
			}
		return true;
}
function checkMandatory_CC_OECD(){
	var n=com.newgen.omniforms.formviewer.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			var custTypePickList = document.getElementById("OECD_CustomerType");
			var picklistValues=getPickListValues(custTypePickList);
			if((picklistValues.length)>n || n==0)
			{
				showAlert('OECD_CustomerType','Please add OECD for all Applicant type');	
					return false;
			}
			
				return true;
		}
	// var n=getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
	// if(n==0){
		// showAlert('cmplx_OECD_cmplx_GR_OecdDetails',alerts_String_Map['CC097']);
		// return false;
		// }
		// esle
		// {
//++Below code added by nikhil 13/11/2017 for Code merge

	function FATCA_Save_Check(optype)
	{	
		var n=com.newgen.omniforms.formviewer.getLVWRowCount('cmplx_FATCA_cmplx_GR_FatcaDetails');
			var custTypePickList = document.getElementById("FATCA_CustomerType");
			//var picklistValues=getPickListValues(custTypePickList);
			CustType=getNGValue("FATCA_CustomerType");
			var gridValue=[];
			for(var i=0;i<n;i++)
			{
				gridValue.push(getLVWAT("cmplx_FATCA_cmplx_GR_FatcaDetails",i,0));
			}
		if(isFieldFilled('FATCA_CustomerType')==false)
		{
			showAlert('FATCA_CustomerType','Applicant Type cannot be blank');
		        return false;
		}			
		else if(isFieldFilled('FATCA_USRelaton')==false)
		{
				showAlert('FATCA_USRelaton',alerts_String_Map['VAL288']);
		        return false;
		}
		else if(getNGValue('FATCA_USRelaton')=='N' || getNGValue('FATCA_USRelaton')=='R')
		{
			 if(getNGValue('FATCA_IdDoc')==false && getNGValue('FATCA_DecForInd')==false && getNGValue('FATCA_W8Form')==false && getNGValue('FATCA_W9Form')==false && getNGValue('FATCA_LossOFNationalCertificate')==false)
			{
				showAlert('FATCA_IdDoc',alerts_String_Map['VAL268']);
				return false;
			}
			else if(getNGValue('FATCA_W8Form')==true)
			{
				if(getNGValue('FATCA_SignedDate')=='')
					{
						showAlert('FATCA_SignedDate',alerts_String_Map['VAL276']);
						return false;
					}
			}
			else if(getNGValue('FATCA_W9Form')==true)
			{
				if(getNGValue('FATCA_TINNo')=='')
					{
						showAlert('FATCA_TINNo','Tin No cannot be blank');
						return false;
					}
			}
			if(getNGValue('FATCA_SignedDate')=='')
			{
						showAlert('FATCA_SignedDate',alerts_String_Map['VAL276']);
						return false;
			}
			else if(getNGValue('FATCA_ExpiryDate')=='')
			{
						showAlert('FATCA_ExpiryDate',alerts_String_Map['VAL220']);
						return false;
			}					
			/* Commented by prateek on 19-11-2017 as this alert was coming on Introduce for Empty Company Details which is wrong			
			else if(isFieldFilled('FATCA_Category')==false)
			{
						showAlert('FATCA_Category',alerts_String_Map['VAL174']);
						return false;
			}
			*/
			/*else if(getNGValue('cmplx_FATCA_ConPerHasUS')=='')
			{
						showAlert('cmplx_FATCA_ConPerHasUS',alerts_String_Map['VAL176']);
						return false;
			}*/
			var rowCount = com.newgen.omniforms.formviewer.getItemCount('FATCA_SelectedReason');			
			if(rowCount == 0){
						showAlert('FATCA_SelectedReason',alerts_String_Map['VAL269']);
						return false;
			}
			else if(rowCount == 1 && com.newgen.omniforms.formviewer.getNGItemText('FATCA_SelectedReason',0)==""){
						showAlert('FATCA_SelectedReason',alerts_String_Map['VAL269']);
						return false;
			}			
			/*else if(getNGValue('FATCA_SelectedReason')=='')
			{
						showAlert('FATCA_SelectedReason',alerts_String_Map['VAL269']);
						return false;
			}*/
	}	
	else if(gridValue.indexOf(CustType)>-1 && optype!='Modify')
			{
						showAlert('FATCA_CustomerType','FATCA Already Added for Customer'+CustType);
						return false;
			}
	return true;
	}
	/* if(getNGValue('FATCA_W8Form')== true && getNGValue('FATCA_SignedDate') == '')
	{
		
		showAlert('FATCA_SignedDate',alerts_String_Map['CC250']);
		return false;
	}
	else if(getNGValue('FATCA_W9Form')== true && getNGValue('FATCA_TINNo') == '')
	{
		showAlert('FATCA_TINNo',alerts_String_Map['CC251']);
		return false;
	}
	else
	{
		return true;
	} */
	



function chechmandatory_SaveFCU(){
	//++Below code added by  nikhil 16/10/17 as per CC FSD 2.2
	//++Below code commented by  nikhil 23/10/17 as per CC FSD 2.2

	if(!checkMandatory(CC_Custdetail_FCU))
	{
		return false;
	}
	//--above code commented by  nikhil 23/10/17 as per CC FSD 2.2
	var ver="cmplx_CustDetailverification1_MobNo1_veri:cmplx_CustDetailverification1_MobNo2_veri:cmplx_CustDetailverification1_DOB_ver:cmplx_CustDetailverification1_PO_Box_Veri:cmplx_CustDetailverification1_Emirates_veri:cmplx_CustDetailverification1_Off_Telephone_veri";
	var update="cmplx_CustDetailverification1_MobNo1_updates:cmplx_CustDetailverification1_MobNo2_Updates:cmplx_CustDetailverification1_Dob_update:cmplx_CustDetailverification1_PO_Box_Uodates:cmplx_CustDetailverification1_EmiratesId_updates:cmplx_CustDetailverification1_OfficeTelephone_Updates";
	var name="Mobile No. 1:Mobile No. 2:Date of Birth:P.O Box No. :Emirates :Office Telephone No. ";
	if(!CheckMandatory_Verification(ver,update,name))
	{
		return false;
	}
	//--above code added by nikhil 16/10/17 as per CC FSD 2.2
	//++Below code commented by  nikhil 16/10/17 as per CC FSD 2.2
	/*if(getNGValue('cmplx_CustDetailverification1_MobNo1_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates',alerts_String_Map['CC223']);
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_MobNo2_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo2_Updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo2_Updates',alerts_String_Map['CC224']);
			return false;
		}
	}
	if(getNGValue('CustDetailVerification1_Combo3')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_Dob_update')==""){
			showAlert('cmplx_CustDetailverification1_Dob_update',alerts_String_Map['CC222']);
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_PO_Box_Veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates',alerts_String_Map['CC222']);
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_Emirates_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates',alerts_String_Map['CC222']);
			return false;
		}
	}
	if(getNGValue('cmplx_CustDetailverification1_Off_Telephone_veri')=="No"){
		if(getNGValue('cmplx_CustDetailverification1_MobNo1_updates')==""){
			showAlert('cmplx_CustDetailverification1_MobNo1_updates',alerts_String_Map['CC222']);
			return false;
		}
	}*/
	//--above code commented by  nikhil 16/10/17 as per CC FSD 2.2
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
function checkMandatory_EmpVerification(){
	//change by saurabh on 20 Dec for PCSP-198
	if(getNGValue('cmplx_EmploymentVerification_offTelNoCntctd')=='' || getNGValue('cmplx_EmploymentVerification_offTelNoCntctd')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_offTelNoCntctd',alerts_String_Map['PL388']);
			return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_offTelNoCntctd')=="Yes"){
		if(getNGValue('cmplx_EmploymentVerification_OffTelNo')==""){
			showAlert('cmplx_EmploymentVerification_OffTelNo',alerts_String_Map['CC133']);
			return false;
		}
		if(getNGValue('cmplx_EmploymentVerification_OffTelnoValidatedfrom')==""||getNGValue('cmplx_EmploymentVerification_OffTelnoValidatedfrom')=="--Select--"){
			showAlert('cmplx_EmploymentVerification_OffTelnoValidatedfrom',alerts_String_Map['CC134']);
			return false;
		}
	}
	//below code added by nikhil for PCSP-294
	if(getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')=='' || getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')=='--Select--')
	{
		showAlert('cmplx_EmploymentVerification_HRDNoCntctd',alerts_String_Map['CC103']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_HRDcntctNo')!=""){
		if(getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')==""||getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')=="--Select--"){
				//++Below code added by  nikhil 13/10/17 as per CC FSD 2.2
				showAlert('cmplx_EmploymentVerification_HRDNoCntctd',alerts_String_Map['CC103']);
				//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
			return false;
		}
	}
	if(getNGValue('cmplx_EmploymentVerification_HRDNoCntctd')=="Yes"){
		if(getNGValue('cmplx_EmploymentVerification_HRDcntctName')==""){
			showAlert('cmplx_EmploymentVerification_HRDcntctName',alerts_String_Map['CC102']);
			return false;
		}
	}
	//below code added by nikhil for PCSP-214
	if(getNGValue('cmplx_EmploymentVerification_Salary_variance')=="" || getNGValue('cmplx_EmploymentVerification_Salary_variance')=="--Select--"){
	showAlert('cmplx_EmploymentVerification_Salary_variance',alerts_String_Map['CC279']);
			return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_Salary_variance')=="Yes"){
		if(getNGValue('cmplx_EmploymentVerification_ReasonForVariance')==""){
			showAlert('cmplx_EmploymentVerification_ReasonForVariance',alerts_String_Map['CC182']);
			return false;
		}
	}
	//added
	if(getNGValue('cmplx_EmploymentVerification_fixedsal_ver')==''||getNGValue('cmplx_EmploymentVerification_fixedsal_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_fixedsal_ver',alerts_String_Map['CC232']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_AccomProvided_ver')==''||getNGValue('cmplx_EmploymentVerification_AccomProvided_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_AccomProvided_ver',alerts_String_Map['CC227']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_Desig_ver')==''||getNGValue('cmplx_EmploymentVerification_Desig_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_Desig_ver',alerts_String_Map['CC230']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_doj_ver')==''||getNGValue('cmplx_EmploymentVerification_doj_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_doj_ver',alerts_String_Map['CC229']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_confirmedinJob_ver')==''||getNGValue('cmplx_EmploymentVerification_confirmedinJob_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_confirmedinJob_ver',alerts_String_Map['CC228']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_LoanFromCompany_ver')==''||getNGValue('cmplx_EmploymentVerification_LoanFromCompany_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_LoanFromCompany_ver',alerts_String_Map['CC233']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_PermanentDeductSal_ver')==''||getNGValue('cmplx_EmploymentVerification_PermanentDeductSal_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_PermanentDeductSal_ver',alerts_String_Map['CC234']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_SmartCheck_ver')==''||getNGValue('cmplx_EmploymentVerification_SmartCheck_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_SmartCheck_ver',alerts_String_Map['CC235']);
		return false;
	}
	if(getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_ver')==''||getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_ver')=='--Select--'){
		showAlert('cmplx_EmploymentVerification_FiledVisitedInitiated_ver',alerts_String_Map['CC231']);
		return false;
	}
	//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
	var ver="cmplx_EmploymentVerification_fixedsal_ver:cmplx_EmploymentVerification_AccomProvided_ver:cmplx_EmploymentVerification_Desig_ver:cmplx_EmploymentVerification_doj_ver:cmplx_EmploymentVerification_confirmedinJob_ver:cmplx_EmploymentVerification_LoanFromCompany_ver:cmplx_EmploymentVerification_PermanentDeductSal_ver:cmplx_EmploymentVerification_SmartCheck_ver:cmplx_EmploymentVerification_FiledVisitedInitiated_ver";
	var update="cmplx_EmploymentVerification_fixedsalupd:cmplx_EmploymentVerification_AccomProvided_upd:cmplx_EmploymentVerification_Desig_upd:cmplx_EmploymentVerification_doj_upd:cmplx_EmploymentVerification_confirmedinJob_upd:cmplx_EmploymentVerification_LoanFromCompany_updates:cmplx_EmploymentVerification_PermanentDeductSal_updates:cmplx_EmploymentVerification_SmartCheck_updates:cmplx_EmploymentVerification_FiledVisitedInitiated_updates";
	var name="Fixed Salary:Accomodation Provided:Designation:Date of Joining:Confirmed in Job:Loan/Advance From Company:Permanent Deduction From Salary:Smart Check:Field Visit Initiated";
	if(!CheckMandatory_Verification(ver,update,name))
	{
		return false;
	}
	//changesdoen for PCSP-621
	if(getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_updates')=='' || getNGValue('cmplx_EmploymentVerification_FiledVisitedInitiated_updates')=='--Select--')
	{
		showAlert('cmplx_EmploymentVerification_FiledVisitedInitiated_updates',alerts_String_Map['PL420']);
		return false;
	}
	
	if(getNGValue('cmplx_EmploymentVerification_Remarks')==''){
		showAlert('cmplx_EmploymentVerification_Remarks',alerts_String_Map['CC190']);
		return false;
	}
	
	//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
	//++Below code commented by  nikhil 12/10/17 as per CC FSD 2.2
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
	}*/
	//--above code commented by  nikhil 12/10/17 as per CC FSD 2.2
	
	return true;
}
//12th september

function checkMandatory_PartnerGrid()
	{
			if(getNGValue('PartnerDetails_PartnerName')=="")
			{
				showAlert('PartnerDetails_PartnerName',alerts_String_Map['VAL094']);	
				return false;
			}
				
			else if(getNGValue('PartnerDetails_Dob')==""){
				showAlert('PartnerDetails_Dob',alerts_String_Map['VAL045']);	
				return false;
				}
				
			else if(getNGValue('PartnerDetails_passno')==""){
				showAlert('PartnerDetails_passno',alerts_String_Map['VAL098']);	
				return false;
				}
			// Removed by aman on 2510
			else if( getNGValue('PartnerDetails_nationality')=="" || getNGValue('PartnerDetails_nationality')=="--Select--"){
				showAlert('PartnerDetails_nationality',alerts_String_Map['VAL090']);
				return false;
			}	
			else if(getNGValue('PartnerDetails_shareholding')==''){
				showAlert('PartnerDetails_shareholding',alerts_String_Map['VAL136']);
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
				showAlert('AuthorisedSignDetails_Button4',alerts_String_Map['VAL059']);
				return false;
			}
		}
			
		else
		{	
			if(getNGValue('AuthorisedSignDetails_Name')=="")
			{
				showAlert('AuthorisedSignDetails_Name',alerts_String_Map['VAL011']);	
				return false;
			}
				
			else if(getNGValue('AuthorisedSignDetails_DOB')==""){
				showAlert('AuthorisedSignDetails_DOB',alerts_String_Map['VAL010']);	
				return false;
				}
				
			else if(getNGValue('AuthorisedSignDetails_VisaNumber')==""){
				showAlert('AuthorisedSignDetails_VisaNumber',alerts_String_Map['VAL015']);	
				return false;
				}
				else if(getNGValue('Designation')=="" || getNGValue('Designation')=="--Select--"){
				showAlert('Designation',alerts_String_Map['VAL048']);	
				return false;
				}
				else if(getNGValue('DesignationAsPerVisa')=="" || getNGValue('DesignationAsPerVisa')=="--Select--"){
				showAlert('DesignationAsPerVisa',alerts_String_Map['VAL047']);	
				return false;
				}
				
			//else if( getNGValue('AuthorisedSignDetails_shareholding')==''){
			//	showAlert('AuthorisedSignDetails_shareholding',alerts_String_Map['VAL013']);	
			//	return false;
			//}
			
			else if( getNGValue('AuthorisedSignDetails_SoleEmployee')=='' || getNGValue('AuthorisedSignDetails_SoleEmployee')=='--Select--'){
				showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['CC016']);	
				return false;
			}
		}	
	return true;
	}	
//Changes Done by aman for preferred choice
	function checkPrefferedChoice_1(){
		
	
		var gridRowCount = getLVWRowCount('cmplx_AddressDetails_cmplx_AddressGrid');
		var prefferedFlag = false;
		var IS_HOME=false,IS_RESIDENCE=false,IS_OFFICE=false;
		var grid_CustomerType=[];//added by prabhakar
		var custTypePickList = document.getElementById("AddressDetails_CustomerType");
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
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'Home' && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){//added by prabhakar && getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1)
					IS_HOME=true;
				}
				
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'OFFICE'&& getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_OFFICE=true;
				}
				else if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,0) == 'RESIDENCE'&& getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13).indexOf('P-')!=-1){
					IS_RESIDENCE=true;
				}
				//++below code modified bu nikhil for address details 
				if(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,10) == 'true' ){
						//prefferedFlag = true;
						//added by prabhakar
						allPreffered++;
						if( preferedCustomers.indexOf(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13))==-1)
						{
						preferedCustomers.push(getLVWAT('cmplx_AddressDetails_cmplx_AddressGrid',i,13));
						}
				}
				
			}
			//added by prabhakar
			if(allPreffered==custTypePickList.length-1)
			{
				prefferedFlag = true;
			}
			else if(allPreffered > (custTypePickList.length-1))
			{
				showAlert('addtype','Multiple Preferred for Single Applicant. Please Resolve!');
				return false;
			}
			
			
			//changed the ID of resident text box  12/11/2017
				if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false && IS_HOME==false){
					showAlert('addtype',alerts_String_Map['VAL299']);
					return false;
				}
				if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y"&& IS_OFFICE==false && IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['VAL301']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y"&& IS_HOME==false && IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['VAL300']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y"&& IS_OFFICE==false && IS_HOME==false){
					showAlert('addtype',alerts_String_Map['VAL302']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y"&& IS_OFFICE==false ){
					showAlert('addtype',alerts_String_Map['VAL305']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y"&& IS_RESIDENCE==false){
					showAlert('addtype',alerts_String_Map['VAL304']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')!="AE" && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y"&& IS_HOME==false){
					showAlert('addtype',alerts_String_Map['VAL303']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y" && IS_OFFICE==false && IS_RESIDENCE==false ){
					showAlert('addtype',alerts_String_Map['VAL311']);
					return false;
				}
				
				else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y" && IS_OFFICE==false){
					showAlert('addtype',alerts_String_Map['VAL310']);
					return false;
				}	
				else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="Y" && ( IS_RESIDENCE==false)){
					showAlert('addtype',alerts_String_Map['VAL309']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="N" && (IS_HOME==false) && ( IS_RESIDENCE==false) ){
					showAlert('addtype',alerts_String_Map['VAL308']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="N" && (IS_RESIDENCE==false)){
					showAlert('addtype',alerts_String_Map['VAL307'])
					return false;
				}
				else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="N" && (IS_HOME==false)){
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
				//changed the ID of resident text box  12/11/2017 by aman
			
	}
	//Changes Done by aman for preferred choice
	
	function Customer_Save_Check1(){
		var activityname=window.parent.stractivityName;
		var Id=getNGValue('cmplx_Customer_EmiratesID');
		var Dob=getNGValue('cmplx_Customer_DOb');
		var nep = getNGValue('cmplx_Customer_NEP');	
			//below code added for CAS Simplification
			var Principal_Approval=getNGValue('Is_Principal_Approval');
		if(((Id=="")||(Id.length != 15) ||(Id.substr(0,3)!='784'))&&(nep=='') && isFieldFilled('cmplx_Customer_marsoomID')==false && activityName=='DDVT_Maker' ){
				if(Id.substr(0,3)!='784'){
				showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
				}
				else{
				showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL108']);
				}
				return false;
			}
		if((Id.charAt(3)!==Dob.charAt(6) || Id.charAt(4)!==Dob.charAt(7) || Id.charAt(5)!==Dob.charAt(8) ||Id.charAt(6)!==Dob.charAt(9)) && Id!='' && activityName=='DDVT_Maker')
				{
				showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL074']);
				return false;
				} 
	//ID's Changed by aman 
	if(getNGValue('cmplx_Customer_Designation')==''){
				showAlert('cmplx_Customer_Designation',alerts_String_Map['VAL048']);

				return false;
				}
	if(getNGValue('cmplx_Customer_CIFID_Available')==true){	//CIF ID check
		if(getNGValue('cmplx_Customer_CIFNo')=='' && getNGValue('cmplx_Customer_NTB')==false){
				showAlert('cmplx_Customer_CIFNo',alerts_String_Map['VAL035']);
				com.newgen.omniforms.formviewer.setNGFocus(cmplx_Customer_CIFNo);
				return false;
				}
		/*else{
			return true;
			}*/
	}		
				
	/*else if(getNGValue('cmplx_Customer_EmiratesID')=='')
		if(getNGValue('cmplx_Customer_NEP') == false || getNGValue('cmplx_Customer_NEP') == null )
		{
		    
			showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['CC082']);
		}*/
		
		
		 if(getNGValue('cmplx_Customer_NEP') == '' || getNGValue('cmplx_Customer_NEP') == null )
		{
		    setLockedCustom('cmplx_Customer_EmiratesID',false);
			//alert ('abcd');
			
		}
         
				
	  if(getNGValue('cmplx_Customer_FirstNAme')==''){
				showAlert('cmplx_Customer_FirstNAme',alerts_String_Map['VAL061']);	
				com.newgen.omniforms.formviewer.setNGFocus(cmplx_Customer_FirstNAme);
				return false;
				//showAlert('cmplx_Customer_FIrstNAme',getNGValue('cmplx_Customer_NEP')) ;
	}				
				
	 if(getNGValue('cmplx_Customer_LastNAme')==''){
				showAlert('cmplx_Customer_LastNAme',alerts_String_Map['VAL076']);
				com.newgen.omniforms.formviewer.setNGFocus(cmplx_Customer_LastNAme);
				return false;
	}
				
	if(getNGValue('cmplx_Customer_DOb')==''){
				showAlert('cmplx_Customer_DOb',alerts_String_Map['VAL045']);
				return false;
				}
	if(isFieldFilled('cmplx_Customer_Nationality')==false){
				showAlert('cmplx_Customer_Nationality',alerts_String_Map['VAL090']);
				return false;
				}
	if(getNGValue('cmplx_Customer_MobileNo')==''){
				showAlert('cmplx_Customer_MobileNo',alerts_String_Map['VAL084']);	
				return false;
}
	if(getNGValue('cmplx_Customer_PAssportNo')==''){
				showAlert('cmplx_Customer_PAssportNo',alerts_String_Map['VAL098']);		
				return false;
	}
	/*if(getNGValue('cmplx_Customer_CIFNo')==''){
				showAlert('cmplx_Customer_CIFNo',alerts_String_Map['VAL035']);
				return false;
				}*/
	if(isFieldFilled('cmplx_Customer_Title')==false){
				showAlert('cmplx_Customer_Title',alerts_String_Map['VAL284']);
				return false;
			}
	if(isFieldFilled('cmplx_Customer_gender')==false){
				showAlert('cmplx_Customer_gender',alerts_String_Map['VAL064']);
				return false;
				}
	if(getNGValue('cmplx_Customer_age')==''){
				showAlert('cmplx_Customer_age',alerts_String_Map['VAL003']);
				return false;
	}
	if(isFieldFilled('cmplx_Customer_MAritalStatus')==false){
				showAlert('cmplx_Customer_MAritalStatus',alerts_String_Map['VAL081']);
				return false;
	}
	if(getNGValue('cmplx_Customer_MotherName')==''){
				showAlert('cmplx_Customer_MotherName',alerts_String_Map['VAL089']);
				return false;
				}
	if(Principal_Approval=='Y' && activityName=='CSM')
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
			else
			{
				return true;
			}	
	}
	if(getNGValue('cmplx_Customer_NEP') == '' || getNGValue('cmplx_Customer_NEP') == null )
	{
	    setLockedCustom('cmplx_Customer_EmiratesID',false);
		//alert ('abcd');
		
	}
	 if(getNGValue('cmplx_Customer_IdIssueDate')==''){
		if(getNGValue('cmplx_Customer_NEP') == '' || getNGValue('cmplx_Customer_NEP') == '--Select--' ){
			showAlert('cmplx_Customer_IdIssueDate',alerts_String_Map['CC104']);	
			return false;
			}
		}
	if(getNGValue('cmplx_Customer_EmirateIDExpiry')==''){
		if(getNGValue('cmplx_Customer_NEP') == '' || getNGValue('cmplx_Customer_NEP') == '--Select--' || getNGValue('cmplx_Customer_NEP')=='NEWC'){
			showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['VAL052']);		
			return false;
				}}
	//changed by nikhil 25/11/2018 to avoid visa validation for UAE NAtionals
	if(getNGValue('cmplx_Customer_VisaNo')=='' && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')!='N'){
				showAlert('cmplx_Customer_VisaNo',alerts_String_Map['VAL154']);
				return false;
				}
	//changed by nikhil 25/11/2018 to avoid visa validation for UAE NAtionals
	if(getNGValue('cmplx_Customer_VisaExpiry')=='' && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')!='N'){
				showAlert('cmplx_Customer_VisaExpiry',alerts_String_Map['CC238']);
				return false;
				}
						
	//changed by nikhil 25/11/2018 to avoid visa validation for UAE NAtionals			
	if(isFieldFilled('cmplx_Customer_EMirateOfVisa')==false && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')!='N'){
				showAlert('cmplx_Customer_EMirateOfVisa',alerts_String_Map['VAL210']);
				return false;
				}
	
	if(getNGValue('cmplx_Customer_PassPortExpiry')==''){
				showAlert('cmplx_Customer_PassPortExpiry',alerts_String_Map['VAL250']);
				return false;
	}
	if(isFieldFilled('cmplx_Customer_RESIDENTNONRESIDENT')==false){
				showAlert('cmplx_Customer_RESIDENTNONRESIDENT',alerts_String_Map['VAL134']);		
				return false;
				}
	if(com.newgen.omniforms.util.getComponentValue(document.getElementById('cmplx_Customer_SecNAtionApplicable'))=='Yes'){
		if(getNGValue('cmplx_Customer_SecNationality')=='--Select--' ||getNGValue('cmplx_Customer_SecNationality')==''){
				showAlert('cmplx_Customer_SecNationality',alerts_String_Map['VAL267']);
				return false;
				}
	}
	
	if(isFieldFilled('cmplx_Customer_GCCNational')==false){
				showAlert('cmplx_Customer_GCCNational',alerts_String_Map['CC094']);	
				return false;
				}
	
	if(isFieldFilled('cmplx_Customer_CustomerCategory')==false){
				showAlert('cmplx_Customer_CustomerCategory',alerts_String_Map['VAL182']);
				return false;
}
//change done by nikhil for PCAS-2246
	if(isFieldFilled('cmplx_Customer_yearsInUAE')==false && getNGValue('cmplx_Customer_GCCNational')!='Y' && getNGValue('cmplx_Customer_Nationality')!='AE' && getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')!='N'){
				showAlert('cmplx_Customer_yearsInUAE',alerts_String_Map['CC239']);
				return false;
				}
	
	if(isFieldFilled('cmplx_Customer_EmirateOfResidence')==false){
				showAlert('cmplx_Customer_EmirateOfResidence',alerts_String_Map['VAL209']);
				return false;
				}
	if(isFieldFilled('cmplx_Customer_COUNTRYOFRESIDENCE')==false){
				showAlert('cmplx_Customer_COUNTRYOFRESIDENCE',alerts_String_Map['VAL177']);
				return false;
				}
/*	//added by Tarang on 01/19/2018 against drop 4 point 25
	if((getNGSelectedItemText('cmplx_Customer_RESIDENTNONRESIDENT')=='RESIDENT' && getNGValue('cmplx_Customer_EmiratesID')=='')
	{
		showAlert('cmplx_Customer_EmiratesID',alerts_String_Map['VAL211']);
		return false;
	}
		//ID's Changed by aman 		
		*/
return true;
				
}

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
					showAlert('OECD_CRSFlag',alerts_String_Map['VAL043']);
					return false;
		}
		else if(getNGValue('OECD_CRSFlag')=='Y'){
					if(getNGValue('OECD_CRSFlagReason')=='--Select--' || getNGValue('OECD_CRSFlagReason')=='' || getNGValue('OECD_CRSFlagReason')=='NA' ){
						showAlert('OECD_CRSFlagReason',alerts_String_Map['VAL120']);
						return false;
					}
					if( getNGValue('OECD_CountryTaxResidence')=='--Select--' || getNGValue('OECD_CountryTaxResidence')=='' ){
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['CC163']);
						return false;
					}
					if( getNGValue('OECD_tinNo')==''  && getNGValue('OECD_noTinReason')=='' ){
						showAlert('OECD_tinNo',alerts_String_Map['CC179']);
						return false;
					}
					// if(getNGValue('OECD_CountryBirth')=='--Select--' || getNGValue('OECD_CountryBirth')==''){
					//showAlert('OECD_CountryBirth',alerts_String_Map['VAL041']);
					//return false;
					//}
					//if(getNGValue('OECD_townBirth')=='--Select--' || getNGValue('OECD_townBirth')==''){
					//			showAlert('OECD_townBirth',alerts_String_Map['VAL145']);
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
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['CC163']);
						showAlert('OECD_CountryTaxResidence',alerts_String_Map['CC163']);
						return false;
					}
					if(getNGValue('OECD_tinNo')==''  && getNGValue('OECD_noTinReason')=='' ){
						showAlert('OECD_tinNo',alerts_String_Map['CC179']);
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
	
	// added by abhishek as per CC FSD		
function validate_BTGrid()
{
	var n=getLVWRowCount("cmplx_CC_Loan_cmplx_btc");
		if(n==4){
			showAlert('',alerts_String_Map['VAL023']);
			return false;
		}
		else if(n>0){
			for(var i=0;i<n;i++)
			{
				if(getLVWAT('cmplx_CC_Loan_cmplx_btc',i,0)==getNGValue('transtype')){
					showAlert('transtype',alerts_String_Map['VAL142']);	
					return false;
				}
			}
		}	
		
		return true;
}
	
	function checkMandatory_BTGrid()
{	
	 if(isFieldFilled('transtype')==false){
		showAlert('transtype',alerts_String_Map['VAL148']);
		return false;
		}	
	
	else if(getNGValue('transtype')=='BT'){
		 if(!checkMandatory(CC_Loan_BT)){
		  return false;
		 }
			
	}
		else if(getNGValue('transtype')=='CCC'){
		 if(!checkMandatory(CC_Loan_CCC)){
		  return false;
		 }
			
	}
		else if(getNGValue('transtype')=='LOC'){
		 if(!checkMandatory(CC_Loan_LOC)){
		  return false;
		 }
			
	}
		else if(getNGValue('transtype')=='SC'){
		 if(!checkMandatory(CC_Loan_SmartCash)){
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

function DDS_Save_CC()
	{
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
			
			else if(!checkMandatory(CC_DDS))
				return false;
		}
		return true;
	}
	
	function SI_Save_CC()
	{
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
		}
		
		
		return true;
	}

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

		else if(isLocked('SupplementCardDetails_FetchDetails')==false){
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
        else if(getNGValue('SupplementCardDetails_ResidentCountry')=='--Select--'){
					showAlert('SupplementCardDetails_ResidentCountry','Resident Country cannot be blank');
					return false;
			}		
		else if(getNGValue('SupplementCardDetails_relationship')=='--Select--'){
			showAlert('SupplementCardDetails_relationship',alerts_String_Map['VAL128']);
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
		else if(Trim(getNGValue('SupplementCardDetails_cardEmbName'))==''){
					showAlert('SupplementCardDetails_cardEmbName',alerts_String_Map['VAL029']);
					return false;
			}
else if(getNGValue('SupplementCardDetails_Title')=='' || getNGValue('SupplementCardDetails_Title')=='--Select--'){
			showAlert('SupplementCardDetails_Title',alerts_String_Map['VAL284']);
				return false;
		}
else if(getNGValue('SupplementCardDetails_EmploymentStatus')=='' || getNGValue('SupplementCardDetails_EmploymentStatus')=='--Select--'){
			showAlert('SupplementCardDetails_EmploymentStatus',alerts_String_Map['VAL366']);
				return false;
		}		
			/*FOR PCAS-2633 by sagarika	
		else if(getNGValue('SupplementCardDetails_CompEmbName')=='' && isVisible('SupplementCardDetails_CompEmbName')){
				showAlert('SupplementCardDetails_CompEmbName',alerts_String_Map['VAL036']);
				return false;
			}*/
			else if(getNGValue('SupplementCardDetails_EmailID')=='' && isVisible('SupplementCardDetails_EmailID')){
				showAlert('SupplementCardDetails_EmailID','Email ID cannot be blank');
				return false;
			}
		if(getNGValue('SupplementCardDetails_ResidentCountry')=='AE')	
		{
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
		}
		if(getNGValue('SupplementCardDetails_EmiratesIDExpiry')=='' && getNGValue('SupplementCardDetails_ResidentCountry')=='AE')	
		{
			showAlert('SupplementCardDetails_Text6','Emirate ID Expiry date cannot be blank');
				
				return false;
		}
			if(getNGValue('SupplementCardDetails_VisaNo')=='' && getNGValue('SupplementCardDetails_ResidentCountry')=='AE' && getNGValue('SupplementCardDetails_Nationality')!='AE')//PCAS-3337 sagarika	
		{
			showAlert('SupplementCardDetails_Text6','Visa number cannot be blank');
				
				return false;
		}
			if(getNGValue('SupplementCardDetails_VisaExpiry')=='' && getNGValue('SupplementCardDetails_ResidentCountry')=='AE' && getNGValue('SupplementCardDetails_Nationality')!='AE')	
		{//PCAS-3337 sagarika
			showAlert('SupplementCardDetails_VisaExpiry','Visa Expiry date cannot be blank');
				
				return false;
		}
			if(getNGValue('SupplementCardDetails_VisaIssueDate')=='' && getNGValue('SupplementCardDetails_ResidentCountry')=='AE' && getNGValue('SupplementCardDetails_Nationality')!='AE')	
		{//PCAS-3337 sagarika
			showAlert('SupplementCardDetails_VisaIssueDate','Visa issue date cannot be blank');
				
				return false;
		}
		if(getNGValue('SupplementCardDetails_Nationality')=='AE')
		{
			if(getNGValue('SupplementCardDetails_VisaNo')!='' || getNGValue('SupplementCardDetails_VisaExpiry')!='' || getNGValue('SupplementCardDetails_VisaIssueDate')!='')
			{
				showAlert('SupplementCardDetails_VisaNo','Please Remove Visa Details for AE National Customer!');
			}
		}
			return true;
					
	}
	//Done for JIRA 020818
	function ValidateRepeaterTrnOverNBC(){
	 //return true;
	 var pattern = new RegExp('((\d+)((\.\d{1,2})?))$');
	 var cellName = '';
	 var cellValue = '';
	 
	 var Map_month={1:'Jan',2:'Feb',3:"March",4:'April',5:'May',6:'June',7:'July',8:'Aug',9:'Sep',10:'Oct',11:'Nov',12:'Dec'};
	 var Currentyear=(new Date()).getFullYear();
	
		 for(var j = 1 ; j<13; j++){
			  for(var i = 0 ; i<11;i++){
			 cellName = 'FinacleCore_Frame11_Reprow'+i+'_Repcolumn'+j+'';
			 cellValue = document.getElementById(cellName).value;
		var  cellDisabled=	document.getElementById(cellName).disabled;
		var currentDay=	document.getElementById('FinacleCore_Frame11_Reprow'+i+'_Repcolumn0').value;
			
			 /*if(!cellDisabled && (j==2 && i>=28)|| ((j==4 || j==6 || j==9 || j==11)&& i==30) )
				 {
					if(j==2 && (((Currentyear % 4 == 0) && (Currentyear % 100 != 0)) || (Currentyear % 400 == 0))){
						showAlert(cellName,'Value cannot be blank for day '+currentDay+' of month '+Map_month[j]);	
						return false;
					}
					else{
						document.getElementById(cellName).value='';	
					}		
				 }*/
			 
			 if(cellValue!='' && !cellDisabled){
				 console.log(pattern.test(cellValue));
				 if(pattern.test(cellValue)==null){
					 showAlert(cellName,'Please Enter a Valid Value for day '+currentDay+' of month '+Map_month[j]);
					 //com.newgen.omniforms.formviewer.setNGFocus(cellName);
						return false;
					 
				 }
				/* else  if(i==10 && (parseFloat(cellValue)<1 || parseFloat(cellValue)>30 || cellValue.indexOf('.')>-1 || cellValue=='undefined')){
					showAlert('','Please enter a value between 1 and 30 for No of Days for the month '+Map_month[j]);
					//document.getElementById(pId).value='';
					return false;
				}*/
			 }
			 /*else if(!cellDisabled){
				 
					showAlert(cellName,'Value cannot be blank for day '+currentDay+' of month '+Map_month[j]);	
					return false;			
			}*/
		}
	 }
	 return true;
 }
 //Done for JIRA 020818
 function ValidateRepeaterAvgNBC(){
	 //return true;
	 var pattern = new RegExp('((\d+)((\.\d{1,2})?))$');
	 //var pattern = new RegExp(RegEx,'i');
	 var cellName = '';
	 var cellValue = '';
	 
	 var Map_month={1:'Jan',2:'Feb',3:"March",4:'April',5:'May',6:'June',7:'July',8:'Aug',9:'Sep',10:'Oct',11:'Nov',12:'Dec'};
	 var Currentyear=(new Date()).getFullYear();
	 
	
		 for(var j = 1 ; j<13; j++){
			  for(var i = 0 ; i<11;i++){
			 cellName = 'FinacleCore_Frame10_Reprow'+i+'_Repcolumn'+j+'';
			 cellValue = document.getElementById(cellName).value;
		var  cellDisabled=	document.getElementById(cellName).disabled;
		var currentDay=	document.getElementById('FinacleCore_Frame10_Reprow'+i+'_Repcolumn0').value;
			
			/* if(!cellDisabled && (j==2 && i>=28)|| ((j==4 || j==6 || j==9 || j==11)&& i==30) )
				 {
					if(j==2 && (((Currentyear % 4 == 0) && (Currentyear % 100 != 0)) || (Currentyear % 400 == 0))){
						showAlert(cellName,'Value cannot be blank for day '+currentDay+' of month '+Map_month[j]);	
						return false;
					}
					else{
						document.getElementById(cellName).value='';	
					}		
				 }*/
			 
			 if(cellValue!='' && !cellDisabled){
				 console.log(pattern.test(cellValue));
				 if(pattern.test(cellValue)==null){
					 showAlert(cellName,'Please Enter a Valid Value for day '+currentDay+' of month '+Map_month[j]);
					 //com.newgen.omniforms.formviewer.setNGFocus(cellName);
						return false;
					 
				 }
				 else  if(i==10 && (parseFloat(cellValue)<1 || parseFloat(cellValue)>30 || cellValue.indexOf('.')>-1 || cellValue=='undefined')){
					showAlert('','Please enter a value between 1 and 30 for No of Days for the month '+Map_month[j]);
					//document.getElementById(pId).value='';
					return false;
				}
			 }
			 else if(!cellDisabled){
				 
					showAlert(cellName,'Value cannot be blank for day '+currentDay+' of month '+Map_month[j]);	
					return false;			
			}
		}
	 }
	 return true;
 }
 //++Below code added by nikhil 13/11/2017 for Code merge
 //++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
 function CheckMandatory_Verification(s1,s2,s3)
 {
	 var ver = s1.split(":");
	 var update=s2.split(":");
	 var name=s3.split(":");
	 for(var i = 0 ; i<ver.length;i++)
	 {
		 if(getNGValue(ver[i])=='Mismatch' && (getNGValue(update[i])=='' || getNGValue(update[i])=='--Select--'))//&& getNGValue((ver[i].split('_')[0]+'_val')!=''))
		 {
			 showAlert(update[i],name[i]+" update is mandatory");
			 return false;
		 }
	 }
	 return true;
	 
 }
 //--above code added by  nikhil 11/10/17 as per CC FSD 2.2
 //--Above code added by nikhil 13/11/2017 for Code merge
 
 // added by abhishek for smart CPV dec check as per CC FSD
 function checkSmartCPVDec(){
	 var dec = getNGValue('cmplx_DEC_Decision');
	 if(dec == 'Smart CPV Hold'){
		 return false;
	 }
	 else{
		 return true;
	 }
 }

 function AuthSignatory_validate(operationName)
	{
		var n=getLVWRowCount('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails');
		if(operationName=='Add'){
			if(n>0){
				if(getLVWAT('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails',0,10)=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}
				
				else if(getNGValue('AuthorisedSignDetails_SoleEmployee')=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}
			}
		}
		else if(operationName=='Modify'){
			if(n>1){
				if(getLVWAT('cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails',0,10)=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}
				
				else if(getNGValue('AuthorisedSignDetails_SoleEmployee')=='Yes'){
					showAlert('AuthorisedSignDetails_SoleEmployee',alerts_String_Map['VAL320']);
					return false;
				}
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
	
	//added by akshay on 12/9/17
	function validateReferGrid()
	{
		var is_grid_modified=true;
		var is_complete=true;
		var n=getLVWRowCount('cmplx_ReferHistory_cmplx_GR_ReferHistory');
		var activity = activityName;
		if(n>0){
			for(var i=0;i<n;i++){
				var referFrom = getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,2);
				if(activity=='CPV_Analyst'){activity='CPV Checker';}
				//else if(activity=='DSA_CSO_Review' && referFrom=='CSM'){activity='CSO (for documents)';}//Changed by nikhil for Update on refer details on Source
				else if(activity=='DSA_CSO_Review'){activity='Source';}
				
				//Added by aman for sprint 2
				//below is changed by nikhil for PROC-11799
				if( getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,5)==activity && getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)==''){
					is_grid_modified=false;
				}
				//below code added by nikhil for PCSP-487
				if( getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,5)==activity && getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)!='Complete'){
					is_complete=false;
				}
			}
		}
		else{
				is_grid_modified=false;
		}
		
		if(is_grid_modified==false){
					showAlert('ReferHistory_status',alerts_String_Map['CC254']);
					return false;
				}
		//below code added by nikhil for PCSP-487
		//changes for PCAS-2431
		if(is_complete==false && getNGValue('cmplx_DEC_Decision').indexOf('Hold')==-1){
			showAlert('ReferHistory_status',alerts_String_Map['PL415']);
			return false;
		}
	  return true;	
	
	}

		//added by nikhil on 12/9/17
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
				if(getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,5)==activity && getLVWAT('cmplx_ReferHistory_cmplx_GR_ReferHistory',i,6)=='Complete' && getNGValue('cmplx_DEC_Decision')=='Refer'){
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
	
		//below finction added by nikhil 
function checkforfinaclecustinfo()
{
var row_count=getLVWRowCount('cmplx_FinacleCRMCustInfo_FincustGrid');
var cif_changed=false;
if(row_count>0)
{
for(var i=0;i<row_count;i++)
	{
		if(getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,0)!=getNGValue('cmplx_Customer_CIFNo') &&  getLVWAT('cmplx_FinacleCRMCustInfo_FincustGrid',i,13)=='true')
		{
			cif_changed=true;
		}
	}
}
else{
	cif_changed=false;
}
	return cif_changed;
}

//added by akshay on 12/1/18
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
			//changed by nikhil 26/11 non-mandatory
			/*if(getLVWAT(gridName,i,9)=='' || getLVWAT(gridName,i,9)=='NA')//years in current address
			{
				showAlert('AddressDetails_years','Please enter Years in Current Address');
				return false;
			}*/
		}
		else  if(gridName=='cmplx_FATCA_cmplx_GR_FatcaDetails'){
			if(getLVWAT(gridName,i,0)=='NA')//us relation
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
			else if(getLVWAT(gridName,i,6)=='NA' && (getLVWAT(gridName,i,5)=='' ||getLVWAT(gridName,i,5)=='NA'))//no tin reason
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
///////////////////////////////////////////

function checkFor_SupplemntGrid()
	{
	var listViewName = 'SupplementCardDetails_cmplx_supplementGrid';
		if (getLVWRowCount(listViewName)>0){
		for(var i=0;i<getLVWRowCount(listViewName);i++){
			if(getLVWAT(listViewName,i,32)=='' )//Employment Status
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL056']);
				return false;
			}
			else if(getLVWAT(listViewName,i,21)=='' )//Email Id
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL207']);
				return false;
			}
			else if(getLVWAT(listViewName,i,31)=='' )//occupation
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL048']);
				return false;
			}
			else if(getLVWAT(listViewName,i,40)=='' )//Marital Status
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL081']);
				return false;
			}
			//Resident non-resident issue resolved by nikhil
			/*else if(getLVWAT(listViewName,i,16)=='' )//nON Resident
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['PL018']);
				return false;
			}*/
			else if(getLVWAT(listViewName,i,8)=='' )//Resident COuntry
			{
				showAlert('SupplementCardDetails_Frame1',alerts_String_Map['VAL133']);
				return false;
			}
			
			else if(getLVWAT(listViewName,i,14)=='' )//title
			{
				showAlert('SupplementCardDetails_Frame1','Please add Title');
				return false;
			}
			else if(getLVWAT(listViewName,i,7)=='' )//Emirates ID
			{
				showAlert('SupplementCardDetails_Frame1','Please add Emirates ID');
				return false;
			}
			
			else if(getLVWAT(listViewName,i,38)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add ID Issue Date');
				return false;
			}
			else if(getLVWAT(listViewName,i,36)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Passport Issue Date');
				return false;
			}
			else if(getLVWAT(listViewName,i,37)=='' &&  getLVWAT(listViewName,i,5)!='AE')//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Visa Issue Date');
				return false;
			}
			else if(getLVWAT(listViewName,i,15)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please Add Passport Expiry Date');
				return false;
			}
			else if(getLVWAT(listViewName,i,18)=='' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Emirates Expiry Date');
				return false;
			}
			else if(getLVWAT(listViewName,i,23)=='' &&  getLVWAT(listViewName,i,5)!='AE')//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please add Visa Expiry Date');
				return false;
			}
			else if(getLVWAT(listViewName,i,22)=='' &&  getLVWAT(listViewName,i,5)!='AE' )//ID Issue date
			{
				showAlert('SupplementCardDetails_Frame1','Please Enter Visa Number');
				return false;
			}
			else if(getLVWAT('SupplementCardDetails_cmplx_supplementGrid',i,39)=='' )//Employent Type
			{
				showAlert('SupplementCardDetails_Frame1','Please Enter Employment Type');
				return false;
			}
			}
		}
		return true;
	}
	
			function getPickListValues(pickListID)
				{	
					var picklistValues= [];
					for (var i = 0; i < pickListID.length; i++) 
					{
						if(pickListID.options[i].value!='--Select--'){
							picklistValues.push(pickListID.options[i].value);
						}
					}
						picklistValues.sort();
						return picklistValues;
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
				
				function validateSuppEntry(opType){
		var suppRows = getLVWRowCount('SupplementCardDetails_cmplx_supplementGrid');
		if(suppRows>0){
			var gridValues = [];
			var newValue = getNGValue('SupplementCardDetails_passportNo')+':'+getNGValue('SupplementCardDetails_CardProduct');
			for(var i=0;i<suppRows;i++){
				if(getNGListIndex('SupplementCardDetails_cmplx_supplementGrid')>-1 && i==getNGListIndex('SupplementCardDetails_cmplx_supplementGrid') && opType=='modify'){
					continue;
				}
				gridValues.push(getLVWAT('SupplementCardDetails_cmplx_supplementGrid',i,3)+':'+getLVWAT('SupplementCardDetails_cmplx_supplementGrid',i,30));
			}
			if(gridValues.indexOf(newValue)>-1){
				showAlert('','Only 1 supplementary card for a particular primary card is allowed for a customer');
				return false;
			}
		}
		return true;
	}
	
	function checkMandatory_CreateCIF(ApplicantType,pId){
switch(ApplicantType){
case 'PRIMARY': 
				if(getNGValue('cmplx_Customer_FirstNAme')=='' || getNGValue('cmplx_Customer_FirstNAme')==null){
				showAlert('cmplx_Customer_FirstNAme',alerts_String_Map['PL010']);	
				return false;
				}
				else if(getNGValue('cmplx_Customer_Title')=='' || getNGValue('cmplx_Customer_Title')==null){
					showAlert('cmplx_Customer_Title',alerts_String_Map['PL219']);	
					return false;
				}
				else if(getNGValue('cmplx_Customer_LastNAme')=='' || getNGValue('cmplx_Customer_LastNAme')==null){
					showAlert('cmplx_Customer_LastNAme',alerts_String_Map['PL011']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_gender')=="--Select--" || getNGValue('cmplx_Customer_gender')==null || getNGValue('cmplx_Customer_gender')==''){
					showAlert('cmplx_Customer_gender',alerts_String_Map['PL019']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')=="--Select--" || getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')==null  || getNGValue('cmplx_Customer_RESIDENTNONRESIDENT')==''){
					showAlert('cmplx_Customer_RESIDENTNONRESIDENT',alerts_String_Map['PL018']);
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
				else if(isFieldFilled('cmplx_Customer_EmirateIDExpiry')==false && isFieldFilled('cmplx_Customer_EmiratesID')==true){
					showAlert('cmplx_Customer_EmirateIDExpiry',alerts_String_Map['PL009']);
					return false;
				}
				else if(isFieldFilled('cmplx_Customer_VisaExpiry')==false && isFieldFilled('cmplx_Customer_VisaNo')==true){
					showAlert('cmplx_Customer_VisaExpiry',alerts_String_Map['PL022']);
					return false;
				}
				else if(isFieldFilled('cmplx_Customer_PassPortExpiry')==false && isFieldFilled('cmplx_Customer_PAssportNo')==true){
					showAlert('cmplx_Customer_PassPortExpiry',alerts_String_Map['PL023']);
					return false;
				}
				else if(getNGValue('cmplx_Customer_Nationality')=="--Select--" || getNGValue('cmplx_Customer_Nationality')==null || getNGValue('cmplx_Customer_Nationality')==''){
					showAlert('cmplx_Customer_Nationality',alerts_String_Map['PL013']);
					return false;
				}
				//below code changed by nikhil 18/10/18 for wrong alert at create CIF
				else if((getNGValue('cmplx_EmploymentDetails_EmpStatus')=="--Select--" || getNGValue('cmplx_EmploymentDetails_EmpStatus')==null ) && getNGValue('EmploymentType')!='Self Employed'){
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
				else if(getNGValue('AlternateContactDetails_Email1')==''){
							// disha FSD
							showAlert('AlternateContactDetails_Email1',alerts_String_Map['PL094']);
							return false;
				}
				else{
					return true;
				}
	break;

case 'SUPPLEMENT':var grid_name='SupplementCardDetails_cmplx_supplementGrid';
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
								else if(getLVWAT(grid_name,i,18)=='' && getLVWAT(grid_name,i,7)!=''){
									showAlert('SupplementCardDetails_EmiratesIDExpiry',alerts_String_Map['PL009']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,23)=='' && getLVWAT(grid_name,i,22)!=''){
									showAlert('SupplementCardDetails_VisaExpiry',alerts_String_Map['PL022']);	
									return false;
								}
								else if(getLVWAT(grid_name,i,15)=='' && getLVWAT(grid_name,i,3)!=''){
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
									showAlert('SupplementCardDetails_MobNo',alerts_String_Map['PL090']);	
									return false;
								}
							}
						}
					}
break;
}
return true;
}	
	
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
				showAlert('FATCA_CustomerType',alerts_String_Map['VAL360']);
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

function Income_Save_Check()
	{
	var Principal_Approval=getNGValue('Is_Principal_Approval');
	if(Principal_Approval=='Y' && activityName=='CSM')
	{
		if(getNGValue('cmplx_IncomeDetails_totSal')=="")
		{
			showAlert("cmplx_IncomeDetails_totSal","Total Salary is mandatory");
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
		
		else if(getNGValue('cmplx_IncomeDetails_Accomodation')=='Yes' && getNGValue(cmplx_IncomeDetails_AccomodationValue)=='' )
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
		var EmpType=getNGValue('EmploymentType');
		var AppType=getNGValue('Application_Type');
			if(EmpType=='Salaried' || EmpType=='Salaried Pensioner'){
				if(!checkMandatory(CC_INCOME_SALARIED)){
					return false;}
					
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
					//done by sagarika for PCSP-209
					if(getNGValue('cmplx_IncomeDetails_SalaryXferToBank')=="" || getNGValue('cmplx_IncomeDetails_SalaryXferToBank')=="--Select--")
					{
						showAlert('cmplx_IncomeDetails_SalaryXferToBank','Salary Transfer To Bank Is Mandatory!!');
						return false;
					}
					
			}	
			
	 if((EmpType=='Self Employed'  || AppType=='RSEL' || AppType=='RELT' || AppType=='RELTN') && (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,24)!='Surrogate' && getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,25)!='DOC')){
				
				if(activityName == 'CAD_Analyst1')
				 {
					//added by akshay on 29/12/17
					if(getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)=='PA' || (getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,21)=='S' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6)=='Self Employed')){
						if(!checkMandatory(CC_INCOME_SELFEMPLOYED_CAD1)){
							return false;
						}
					}	
					else if(!checkMandatory(CC_INCOME_SELFEMPLOYED_CAD1))
						return false;
				}
				else
				{
					if(!checkMandatory(CC_INCOME_SELFEMPLOYED))
						return false;
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
	else if(expDate=='' || expDate=='--Select--'){
		showAlert('IncomingDocNew_ExpiryDate','Please select Expiry Date');
		return false;
	}
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
		if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',docGrid_count,0)=='Deferred'){
		setNGValueCustom("is_deferral_approval_require","Y");
		}
		if(getLVWAT('cmplx_IncomingDocNew_IncomingDocGrid',docGrid_count,0)=='Waived'){
		setNGValueCustom("is_waiver_approval_require","Y");
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
	setNGValue('cmplx_DEC_Decision','--Select--');
	return false;
}
else if(CA_Dec=='Refer' && (CAD_Dec!='Refer' && CAD_Dec!='Sendback'))
{
	showAlert('','Decision cannot be '+CAD_Dec+' as CA Decision is '+CA_Dec);
	setNGValue('cmplx_DEC_Decision','--Select--');
	return false;
}
return true;
}
//++below code added by nikhil for Self-Supp CR
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
//--above code added by nikhil for Self-Supp CR	
//below code added by nikhil for PCAS-2408 CR
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