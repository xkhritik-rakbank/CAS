/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var subFormURL;
var activityName;
var processName;
var subFormWindowName;
var username;
var DOCRLOS;
try{
	var pname=window.parent.strprocessname;
	var activityName=window.parent.stractivityName;

	if(pname == 'CreditCard')
	{
		document.write("<script src='/formviewer/resources/scripts/CC/CCGlobal.js'></script>");
	}		
	if(pname == 'PersonalLoanS')
	{
		document.write("<script src='/formviewer/resources/scripts/PL/PLGlobal.js'></script>");
	}
	if(pname == 'RLOS')
	{
		document.write("<script src='/formviewer/resources/scripts/RLOS/RLOSGlobal.js'></script>");
	}
	//Changes by Sajan for FALCON CDOB
	if(pname == 'DigitalOnBoarding')
	{
	//	alert('This is CDOB');
		document.write("<script src='/formviewer/resources/scripts/CDOB/CDOBGlobal.js'></script>");
	}
	document.write("<script src='/formviewer/resources/scripts/Doc_template_generation.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/SecurityCheck.js'></script>");
	/*else{			
		document.write("<script src='/webdesktop/resources/scripts/HPE.js'></script>");
		document.write("<script src='/webdesktop/resources/scripts/SRM.js'></script>");
	}*/


	}catch(e){alert("Exception"+e);}
	
	function DocName(docname1){
DOCRLOS=docname1;
//alert("DOCRLOS:"+DOCRLOS);
}
/*
function getdocTypeListExt(docListObj)
{
var docTypeList = new Array();
var count = -1;
// How to Capture Existing Doc List
		docTypeList[0]=window.opener.DOCRLOS;
  var tmpDocTypeList = docListObj;
return docTypeList;
}
*/
	
function eventDispatched(pId,pEvent){
    window.status=pId +"_"+pEvent.type ;
    var pName=window.parent.strprocessname;
	var activityname=window.parent.stractivityName;
	var frameState=0;
	if(pName=='RLOS')
	{
	switch(pEvent.type)
    {           
		case 'change':
						if(change_RLOS(pId)){
						checkDevtools();
							return true;
							}
						break;
        case 'click':
						if(click_RLOS(pId,frameState)){
						checkDevtools();
							return true;
							}
						break;
		case 'blur':
						if(blur_RLOS(pId)){
							return true;
							}
						break;
		case 'focus': 
							if(focus_rlos(pId)){
								return true;
			}
								break;					
		}
	}	
	else if(pName=='CreditCard')
        {
		switch(pEvent.type)
            {
			case 'change':
							if(change_CreditCard(pId)){
							//checkDevtools();
								return true;
								}
							break;
			case 'click': 		
							if(click_CreditCard(pId,frameState)){
							//checkDevtools();
								return true;
								}
							break;
			case 'blur':
							if(blur_CC(pId)){
								return true;
							}								
				break;
			case 'focus': 
							if(focus_CreditCard(pId)){
								return true;
			}
								break;	
		}	
	}
	else if(pName=='PersonalLoanS')
	{
		switch(pEvent.type)
		{	
			case 'change':	
							if(change_PersonalLoanS(pId)){
								return true;
								}
							break;
			case 'click': 		
							if(click_PersonalLoanS(pId,frameState)){
								return true;
								}
							break;
			case 'blur':
							if(blur_PL(pId)){
								return true;
							}								
							break;	
			case 'focus': 
							if(focus_PersonalLoanS(pId)){
								return true;	
							}
							break;
		}
	}
	//Added by Sajan for FALCON CDOB
	else if(pName=='DigitalOnBoarding')
	{
		switch(pEvent.type)
		{	
			case 'change':	
							if(change_DigitalOnBoarding(pId)){
								return true;
								}
							break;
			case 'click': 		
							if(click_DigitalOnBoarding(pId,frameState)){
								return true;
								}
							break;
			case 'blur':
							if(blur_DigitalOnBoarding(pId)){
								return true;
							}								
							break;	
			case 'focus': 
							if(focus_DigitalOnBoarding(pId)){
								return true;	
							}
							break;
		}
	}
	return false;                                         
}


function formPopulated()
{
	var processName = window.parent.strprocessname;
	var activityName = window.parent.stractivityName;
	//onLoadValues = "cmplx_Customer_Passport2=Passport2=UnLocked=Passport 2(##)cmplx_Customer_Passport3=Passport3=UnLocked=Passport 3(##)cmplx_Customer_FIrstNAme=Saurabh=Locked=First Name";
	//getFrameFields('Customer_Frame1');
	if(processName=='RLOS'){
		//onLoadValues = getFetchedFragmentFields();
		load_PropertyFile();//added by akshay on 27/10/17 for loading property file
			setSheetVisible("ParentTab", 8, false);
			setSheetVisible("ParentTab", 10, false);
			if(getNGValue('PrimaryProduct')!='Credit Card'){
			setSheetVisible("ParentTab", 7, false);
			}
			if(getNGValue('Product_Type')=='' && getLVWRowCount('cmplx_Product_cmplx_ProductGrid')>0){
				setNGValueCustom('Product_Type',getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1));
			}
			setNGValueCustom("WI_name", window.parent.pid);
			setLockedCustom('OldApplicationNo',true);
			setLockedCustom('Fetch_existing_cas',true);
			//setNGValue("Channel_Name", "Branch Initiation");
			//change by saurabh on 1st Dec for Tanshu points.
		//	console.log('age: '+getNGValue('cmplx_Customer_age'));
			if(getNGValue('cmplx_Customer_age')==null || getNGValue('cmplx_Customer_age')=='' || parseFloat(getNGValue('cmplx_Customer_age'))>21){
			/*setTop("Incomedetails",parseInt(getTop('ProductDetailsLoader'))+parseInt(getHeight("ProductDetailsLoader"))+5+"px");*/
								setVisible("GuarantorDetails", false);
							}
			setTop("Incomedetails","1290px");
			if(getNGValue('IS_SupplementCard_Required')!='Y'){//added by akshay on 3/1/18
				setVisible("Supplementary_Container",false);
			}
			else{setVisible("Supplementary_Container",true);}
			setVisible("Product_Label3",false);
			setVisible("Product_Label12",false);
			setVisible("Scheme",false);
			setVisible("Product_Label5",false);
			setVisible("Product_Label21",false);
			setVisible("ReqTenor",false);
			//alert('Product_Type '+getNGValue('Product_Type'));
			if(getNGValue('cmplx_CardDetails_SuppCardReq')=='No')
			//alert('cmplx_CardDetails_SuppCardReq' +cmplx_CardDetails_SuppCardReq);
		{
		//alert(yash);
			setVisible("SupplementCardDetails_Frame1",false);
		}
		//change by saurabh on 30th Nov for dectech counter fsd 2.7
		if(getNGValue('reEligbility_init_counter')==''){
			setNGValueCustom('reEligbility_init_counter','0;N');
		}
	}
	else if (processName == 'PersonalLoanS')
	 {
		 load_PropertyFile();//added by akshay on 27/10/17 for loading property file
		 var activityName = window.parent.stractivityName;
		 var short_name = getNGValue('cmplx_Customer_FIrstNAme').charAt(0)+" "+getNGValue('cmplx_Customer_MiddleName').charAt(0)+" "+getNGValue('cmplx_Customer_LAstNAme').charAt(0);
		setNGValue('cmplx_Customer_short_name',short_name);
		//PL_FORM_POPULATED(activityName);
		setNGValue("workitem_name", window.parent.pid);
		setNGValue("cmplx_Customer_apptype", "I");
		setNGValue("ReqProd", "Personal Loan");
		setLocked('ReqProd',true);
		if(getNGValue('reEligibility_PL_counter')==''){
			setNGValue('reEligibility_PL_counter','0;0');
		}
		//alert('PL activityName' + activityName);
		tabSheetHandling(activityName);	
		//setTop("ProductContainer",parseInt(getTop('CustomerDetails'))+parseInt(getHeight("CustomerDetails"))+5+"px");
	 	//setTop("IncomeDEtails",parseInt(getTop('ProductContainer'))+parseInt(getHeight("ProductContainer"))+5+"px");
		if(getNGValue('cmplx_Customer_age')!=null && getNGValue('cmplx_Customer_age')!='' && parseInt(getNGValue('cmplx_Customer_age'))<21){
			setVisible("GuarantorDet",true);
			setTop("IncomeDEtails",parseInt(getTop('GuarantorDet'))+parseInt(getHeight("GuarantorDet"))+5+"px");
			}
		if(getNGValue('IS_SupplementCard_Required')!='Y'){//added by akshay on 8/4/18
				setVisible("Supplementary_Card_Details",false);
		}	
		if(activityName=='FCU' || activityName=='CPV'){
			setVisible('Business_Ver',false);
		}
		if(activityName=='Post_Disbursal'){
			setVisible('Post_Disbursal',true);
		}
		if(activityName=='DDVT_Checker')
		{
			setVisible("DecisionHistory_chqbook",true);
			if(getNGValue('AlternateContactDetails_RetainAccIfLoanReq')==true)
			{
				setVisible("CUSTOMER_UPDATE_REQ",true);
			}
			else
				setVisible("CUSTOMER_UPDATE_REQ",false);
		}		
		else{
			setVisible("DecisionHistory_chqbook",false);
			}
			colorChangedfields();	
	 }
	else if (processName == 'CreditCard')
	 {
		//onLoadValues = getFetchedFragmentFields();
		load_PropertyFile();//added by akshay on 27/10/17 for loading property file
	 //change by saurabh on 30th Nov for dectech counter fsd 2.7
		if(getNGValue('reEligibility_CC_counter')==''){
			setNGValueCustom('reEligibility_CC_counter','0;0');
		}
		if(getNGValue('IS_SupplementCard_Required')!='Y'){//added by akshay on 9/1/18
				setVisible("Supplementary_Cont",false);
		}	
	 //change by saurabh on 5th Jan
	 var short_name = getNGValue('cmplx_Customer_FirstNAme').charAt(0)+" "+getNGValue('cmplx_Customer_MiddleNAme').charAt(0)+" "+getNGValue('cmplx_Customer_LastNAme').charAt(0);
		setNGValueCustom('cmplx_Customer_Shortname',short_name);
		//Below code reset Visa issue date based on visa expiry date on form load comneted for jira PCAS-1823
		/*
	 var VisaExpiry=getNGValue('cmplx_Customer_VisaExpiry');
		if(VisaExpiry!=null){
			var years_Visaexpiry=parseInt(VisaExpiry.substr(6,10));
			var years_VisaIssue=years_Visaexpiry-3;
			var VisaIssue=VisaExpiry.replace(years_Visaexpiry,years_VisaIssue);
			setNGValueCustom('cmplx_Customer_VisaIssueDate',VisaIssue);
		}
*/		
	 if(activityName=='FCU' || activityName=='CPV'){
			setVisible('Business_Verification',false);
		}
	 //added by yash for hiding company details when salaried is selected for proc 776
	 var n=getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
	 if (activityName=='CSM')
	 {
		 if(n>=0)
		 {
			for(var i=0;i<n;i++){
				if(getLVWAT("cmplx_Product_cmplx_ProductGrid",i,1)=="Credit Card" && getLVWAT("cmplx_Product_cmplx_ProductGrid",i,6)=="Salaried")
				{
				setSheetVisible("Tab1",1, false);
				}
		}
		}
		//ended by yash
	}
		//alert('CC activityName' + activityName);
		//tabSheetHandling_CC(activityName); 
		//alert("Hi",activityName);
		//setNGValue("WI_name", window.parent.pid);
		setNGValueCustom("CC_Wi_Name", window.parent.pid);
		colorChangedfields();
	 }
return true;
}
function colorChangedfields(){
var activityName = window.parent.stractivityName;	
if(activityName=="CSM"|| activityName=="DDVT_Maker" || activityName=="DDVT_Checker" || activityName=="DDVT_maker"){
			var hiddenStringvalue = getNGValue('fields_string');
			if(hiddenStringvalue != null && hiddenStringvalue != "" && hiddenStringvalue != " "){
				var fieldNames = hiddenStringvalue.split(",");
				for(var i=0;i<fieldNames.length-1;i++){
					console.log(fieldNames[i]);
					if(document.getElementById(fieldNames[i])){
					document.getElementById(fieldNames[i]).style.setProperty ("background", "#ffe0bd", "important");
					}
				}
			}
		}
}
function eventTabClick(tabid,sheetid)
{
    var pname = window.parent.strprocessname;
    var activityname=window.parent.stractivityName;
	if (pname == 'PersonalLoanS' || pname == 'CreditCard'){
		VisitSystemChecks_CAD(sheetid);
	}	   /*if ((pname == 'PersonalLoanS' || pname == 'PersonalLoanS') && activityname=='CAD_Analyst1')
    {
        if (sheetid == '7')//System Checks
        {
            if (isVisible('CardDetails_Frame1')==false || isVisible('AltContactDetails_Frame1')==false)
            {
               return true;
            }
        }
    }*/
}
 

 
function validateForm(pEvent){   
	var pname=window.parent.strprocessname;
	var activityname=window.parent.stractivityName;
	if(pEvent == 'S'){
	//below code for PCSP-683
	if(!CustomSaveJsp())
	{
		return false;
	}
	//added by nikhil to save jsp on save
		  if (pname == 'RLOS')
        {
		//CustomSaveJsp();
			return true;
		}
		if(pname=='PersonalLoanS'){
		//CustomSaveJsp();
		return true;
			/*if(!DMandatoryPL()){
				return false;
				}*/
			}	
		if(pname=='CreditCard'){
		//CustomSaveJsp();
		return true;
			/*if(!DMandatory()){
				return false;
			}*/
		}
		if(pname=='DigitalOnBoarding'){
		//CustomSaveJsp();
		return true;
			/*if(!DMandatory()){
				return false;
			}*/
		}
	}
	//changed by nikhil 24/11/18 for mandatory validation at Done on Initiation
	if(pEvent == 'D' || pEvent == 'I'){
		if(pname=='RLOS')
		{
			// ++ below code already present -09-10-2017 Changes done to allow decision validation for Reject also
			//if(activityname != 'Reject')
				if(!validate())
					return false;
		}
		if(pname=='PersonalLoanS'){
			if(!DMandatory()){//DmandatoryPL called inside Dmandatory.change by saurabh on 5th oct.
				return false;
				}		
			}
		if(pname=='CreditCard'){
				if(!DMandatoryCC()){
				return false;
			}
			//below code for PCSP-683
			if(!CustomSaveJsp())
			{
			return false;
			}
			
		}
		if(pname=='DigitalOnBoarding'){
				if(!DMandatoryCDOB()){
				return false;
			}
			//below code for PCSP-683
			if(!CustomSaveJsp())
			{
			return false;
			}
			
		}
			
	}
	
                    
                
}

//added by prabhakar started for custom jsp save On Done Click
function CustomSaveJsp()
{              try{
                //added by prabhakar started for custom jsp save
                //Deepak 05-Dec-2018 below change done to get iframe id becase of omniforms & webdesktop change.
                if(window.frames.length>3){
                                if(pname=='RLOS'){
                                                var internalLiabilitySave=window.frames["Liability_New_IFrame_internal"]
                                                var externalLiabilitySave=window.frames["Liability_New_IFrame_external"]
                                                var pipeLiabilitySave=window.frames["Liability_New_IFrame_pipeline"]
                                }
                else{      
                                var internalLiabilitySave=window.frames["ExtLiability_IFrame_internal"]
                                var externalLiabilitySave=window.frames["ExtLiability_IFrame_external"]
                                var pipeLiabilitySave=window.frames["ExtLiability_IFrame_pipeline"]
                }
                var peronalLoanSave=window.frames["ELigibiltyAndProductInfo_IFrame3"]
                var eligibleForCardSave=window.frames["ELigibiltyAndProductInfo_IFrame2"]
                var fundingAccSave=window.frames["ELigibiltyAndProductInfo_IFrame4"]
                var CardSave=window.frames["ELigibiltyAndProductInfo_IFrame1"]
                if(internalLiabilitySave!=null && typeof internalLiabilitySave != 'undefined')
                {
                  if(typeof internalLiabilitySave.document.getElementById('savedata')!='undefined' &&  internalLiabilitySave.document.getElementById('savedata')!=null)
                {
                                internalLiabilitySave.document.getElementById('hiddenFlag').value='true';
                                internalLiabilitySave.document.getElementById('savedata').click();
                }
                }
                if(externalLiabilitySave!=null && typeof externalLiabilitySave != 'undefined')
                {
                if(typeof externalLiabilitySave.document.getElementById('savedata')!='undefined' && externalLiabilitySave.document.getElementById('savedata')!=null)
                {
                externalLiabilitySave.document.getElementById('hiddenFlag').value='true';
                externalLiabilitySave.document.getElementById('savedata').click();
				if(getNGValue('stopexecution')=='stop')
				{
					setNGValue('stopexecution','');
					return false;
				}
                }
                }
                if(pipeLiabilitySave!=null && typeof pipeLiabilitySave != 'undefined')
                {
                if(typeof pipeLiabilitySave.document.getElementById('savedata')!='undefined' && pipeLiabilitySave.document.getElementById('savedata')!=null)
                {
                pipeLiabilitySave.document.getElementById('hiddenFlag').value='true';
                pipeLiabilitySave.document.getElementById('savedata').click();
                }
                }
                if(peronalLoanSave!=null && typeof peronalLoanSave != 'undefined')
                {
                if(typeof peronalLoanSave.document.getElementById('savedata')!='undefined' && peronalLoanSave.document.getElementById('savedata')!=null)
                {
                peronalLoanSave.document.getElementById('hiddenFlag').value='true';
                peronalLoanSave.document.getElementById('savedata').click();
                }
                }
                if(eligibleForCardSave!=null && typeof eligibleForCardSave != 'undefined')
                {
                if(typeof eligibleForCardSave.document.getElementById('savedata')!='undefined' && eligibleForCardSave.document.getElementById('savedata')!=null)
                {
                eligibleForCardSave.document.getElementById('hiddenFlag').value='true';
                eligibleForCardSave.document.getElementById('savedata').click();
                }
                }
                if(fundingAccSave!=null && typeof fundingAccSave != 'undefined')
                {
                if(typeof fundingAccSave.document.getElementById('savedata')!='undefined' && fundingAccSave.document.getElementById('savedata')!=null)
                {
                fundingAccSave.document.getElementById('hiddenFlag').value='true';
                fundingAccSave.document.getElementById('savedata').click();
                }
                }
                if(CardSave!=null && typeof CardSave != 'undefined')
                {
                if(typeof CardSave.document.getElementById('savedata')!='undefined' && CardSave.document.getElementById('savedata')!=null)
                {
                CardSave.document.getElementById('hiddenFlag').value='true';
                CardSave.document.getElementById('savedata').click();
                }
                }
                }
}
catch(ex){
                                alert(ex);
}
return true;
}


function validateEmail(id)
{	
    var x=com.newgen.omniforms.formviewer.getNGValue(id);	
    var atpos=x.indexOf("@");
    var dotpos=x.lastIndexOf(".");
    if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length)
    {
        alert("Please enter valid e-mail address");
        setNGValueCustom(id,'');
        com.newgen.omniforms.formviewer.setNGFocus(id);
        return false;
    }
}

function validateAlpha(id){       
    var TCode = com.newgen.omniforms.formviewer.getNGValue(id);     	
    if( /[^a-zA-Z0-9]/.test( TCode ) ) {  
        alert('Enter Correct Value, Only Alph-numeric Values are Allowed!'); 
        setNGValueCustom(id,'');
        com.newgen.omniforms.formviewer.setNGFocus(id);        
        return false;    
    }        
} 

function OpenSubForm()
{
    /*if(pAjax.status == "success")
    {        */
    var winRef = window[subFormWindowName];     
    var features='height=600,width=800,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,modal=yes';
    if( typeof winRef =='undefined' || winRef.closed)    
    {             
        // winRef = window.open("form.xhtml?FormName="+subFormURL+"&cacheMapKey="+pid+"_"+subFormURL,subFormWindowName,features);   
        
        winRef = window.open("form.xhtml?FormName="+subFormURL+"&username="+username,subFormWindowName,features); 
        winRef.focus();               
    } 
// }    
    
}

function CloseSubForm(pAjax)
{
    if(pAjax.status == "begin")
    { 
        document.getElementById("processImg").style.display="block";
    }
    if(pAjax.status == "success")// actual data act
    {      
        
        var validated=document.getElementById("bValidated").value;  
        var pSubmitValueJSON=document.getElementById("nghiddenValueText").value;  
        //alert("here value=="+pSubmitValueJSON);
        if(validated=="true"){
            document.getElementById("processImg").style.display="none";
            self.close();
        //    window.opener.setClosedSubFormRef(window,pSubmitValueJSON);
        // window.opener.document.getElementById("btnRefresh").click();              
        // window.close();  
        }else{
            document.getElementById("processImg").style.display="none";
            var errMsg=document.getElementById("txtErrorMessage").value;
            com.newgen.omniforms.util.showError(document.getElementById("ngform"),errMsg); 
        }
        
    }
}


// Processing image on any event of java (user not able to click on form) start
function postHookFormEvent (controlName )
{
	try {
		if (window.parent.strprocessname == 'RLOS' || window.parent.strprocessname == 'PersonalLoanS' ||  window.parent.strprocessname == 'CreditCard'||  window.parent.strprocessname == 'DigitalOnBoarding') {
			var pControl = document.getElementById(controlName);
            var id=jQuery(pControl).attr("repeaterChild");  // returns control's id
			if(window.parent.strprocessname == 'RLOS'){
				if(controlName =="Address_Details_container")
				{
				adjustScroll("cmplx_AddressDetails_cmplx_AddressGrid");//05-07-19
				}
				RLOS_postHookDRefresh(controlName);
			}
			else if(window.parent.strprocessname == 'CreditCard'){
				CC_postHookDRefresh(controlName);
			}
			else if(window.parent.strprocessname == 'PersonalLoanS'){
					PL_postHookDRefresh(controlName);
				}
				else if(window.parent.strprocessname == 'DigitalOnBoarding'){
					CDOB_postHookDRefresh(controlName);
				}
				
			
			if(id == undefined){        // non-repeater
				RemoveIndicator("application");
                window.parent.hidePopupMask();
			}
		}
		
	} catch (e) {
		console.log(e);

	}

}


function preHookFormEvent  (controlName )
{
	try {
			if (window.parent.strprocessname == 'RLOS'|| window.parent.strprocessname == 'PersonalLoanS' ||  window.parent.strprocessname == 'CreditCard'||  window.parent.strprocessname == 'DigitalOnBoarding') {
				var pControl = document.getElementById(controlName);
                var id=jQuery(pControl).attr("repeaterChild");  // returns control's id
                if(id == undefined){        // non-repeater
                    window.parent.setPopupMask();
                    CreateIndicator("application");
                }
			}
	} catch (e) {
					console.log(e);

	}

}



function templateData()
{
 
    var attrbList = "";
    var Pname = window.parent.strprocessname;
 
    if (Pname == 'CreditCard')
    {
        attrbList += CLTemplateData();
    }
    else if (Pname == 'PersonalLoanS')
    {
    attrbList += PLTemplateData();    
    }
	else if (Pname == 'RLOS')
    {
    attrbList += RLOSTemplateData();    
    }
    return attrbList;
}


function CreateIndicator(indicatorFrameId){
    var ParentDocWidth = document.body.clientWidth;
    var ParentDocHeight = document.body.clientHeight;  
    if( ParentDocHeight == 0 ){
        if(document.getElementById("ngform")!= null){
             ParentDocHeight = document.getElementById("ngform").clientHeight
        }
    }
    var top = 0;
    var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1;
    if(typeof window.chrome != 'undefined') {
        top = 0;
    } else if(isSafari){
        top = window.document.body.scrollTop;
    }
    
    var ImgTop=ParentDocHeight/2-10 + window.document.documentElement.scrollTop + top;
    var ImgLeft=ParentDocWidth/2-25;
 
    try {
        
        img = document.createElement("IMG");
        img.setAttribute("src", "/webdesktop/resources/images/progressimg.gif");//Bug 66459
        img.setAttribute("name", indicatorFrameId);
        img.setAttribute("id", indicatorFrameId);
        img.style.left = ImgLeft+"px";
        img.style.top = ImgTop+"px";
        img.style.width = "54px";//Bug 66459
        img.style.height = "55px";//Bug 66459
        img.style.position="absolute";
        img.style.zIndex = "9999";
        img.style.visibility="visible";
        //initPopUp();setPopupMask();
        document.body.appendChild(img);
    }
    catch(ex) {}
    document.body.style.cursor='wait';
}
 
function RemoveIndicator(indicatorFrameId){
    try {
        var img = document.getElementById(indicatorFrameId);
        document.body.removeChild(img);
       // hidePopupMask();
    }
    catch(ex) {
        //hidePopupMask();
    }
    document.body.style.cursor='auto';
}


function repeaterEvents(controlId,pEvent){
	switch(controlId){
		case 'cmplx_OrigVal_ovdec':
		{
			var rowIndex;
			if(window.parent.strprocessname=='PersonalLoanS' || window.parent.strprocessname=='CreditCard'){
			rowIndex = com.newgen.omniforms.formviewer.getSelectedRowIndex('OriginalValidation_Frame');
			}
			else{
			rowIndex = com.newgen.omniforms.formviewer.getSelectedRowIndex('IncomingDocument_Frame');	
			}
			var user = window.parent.userName;
			com.newgen.omniforms.formviewer.setRepeaterValue("cmplx_OrigVal_approvedby",rowIndex,user);
			break;
		}
		case 'cmplx_DocName_OVDec':
		{
			var rowIndex;
			if(window.parent.strprocessname=='PersonalLoanS'){
			rowIndex = com.newgen.omniforms.formviewer.getSelectedRowIndex('IncomingDoc_Frame2');
			}
			else{
			rowIndex = com.newgen.omniforms.formviewer.getSelectedRowIndex('IncomingDocument_Frame');	
			}
			var user = window.parent.userName;
			com.newgen.omniforms.formviewer.setRepeaterValue("cmplx_DocName_ApprovedBy",rowIndex,user);
			break;
		}
		case 'cmplx_DocName_DocStatus':
			var rowIndex = com.newgen.omniforms.formviewer.getSelectedRowIndex('IncomingDoc_Frame');
			var docmandatory = com.newgen.omniforms.formviewer.getRepeaterValue('cmplx_DocName_Mandatory',rowIndex);
			var docsta = com.newgen.omniforms.formviewer.getRepeaterValue('cmplx_DocName_DocStatus',rowIndex);
			if(docmandatory=='N' && (docsta=='Deferred' || docsta=='Waived')){
				com.newgen.omniforms.formviewer.setRepeaterValue("cmplx_DocName_DocStatus",rowIndex,'--Select--');
			}	
	}
		/*var rowIndex = com.newgen.omniforms.formviewer.getSelectedRowIndex('IncomingDoc_Frame');
		var value = com.newgen.omniforms.formviewer.getRepeaterValue("cmplx_DocName_DocStatus",rowIndex);
		var user = window.parent.userName;
		com.newgen.omniforms.formviewer.setRepeaterValue("cmplx_DocName_Remarks",rowIndex,user);
		showAlert('','controlId: '+controlId+' pEvent: '+pEvent+' value: '+value);*/
		return true;//changed by akshay for incoming dox save issue on 11/1/18
}

//new method added by akshay to stop unnecessary server requests for these frames on fragment load
function executeFrameEvent(pId){
	var pName=window.parent.strprocessname;
	if(pName=='RLOS'){
		if(pId == "Liability_New_Frame2" || pId == "Liability_New_Frame3" || pId == "Liability_New_Frame4" ||  pId == "Liability_New_Frame5" || pId=='ELigibiltyAndProductInfo_Frame4' ||  pId == "ELigibiltyAndProductInfo_Frame5" ||  pId == "ELigibiltyAndProductInfo_Frame6" ||  pId == "ELigibiltyAndProductInfo_Frame2" ||  pId == "ELigibiltyAndProductInfo_Frame3" ){
			return false;
		}
	}
	else if(pName=='CreditCard'){
		if(pId == "ExtLiability_Frame2" || pId == "ExtLiability_Frame3" || pId == "ExtLiability_Frame4" ||  pId == "ExtLiability_Frame5"  ||  pId == "ELigibiltyAndProductInfo_Frame5" ||  pId == "ELigibiltyAndProductInfo_Frame6" ||  pId == "ELigibiltyAndProductInfo_Frame3" || pId=='FinacleCore_Frame6' || pId=='FinacleCore_Frame2' ||pId=='FinacleCore_Frame3' ||pId=='FinacleCore_avgbal' ||pId=='FinacleCore_Frame8' || pId=='FinacleCore_Frame5'){
			return false;
		}
	}	
	else if(pName=='PersonalLoanS'){
		if(pId == "ExtLiability_Frame2" || pId == "ExtLiability_Frame3" || pId == "ExtLiability_Frame4" ||  pId == "ExtLiability_Frame5"  ||  pId == "ELigibiltyAndProductInfo_Frame4" ||  pId == "ELigibiltyAndProductInfo_Frame6" ||  pId == "ELigibiltyAndProductInfo_Frame2" || pId=='FinacleCore_Frame6' || pId=='FinacleCore_Frame2' ||pId=='FinacleCore_Frame3' ||pId=='FinacleCore_avgbal' ||pId=='FinacleCore_Frame8' || pId=='FinacleCore_Frame5' || pId=='PostDisbursal_Frame2' || pId=='PostDisbursal_Frame3' || pId=='PostDisbursal_Frame6' || pId=='PostDisbursal_Frame4' || pId=='PostDisbursal_Frame5'){
			return false;
		}
	}	
    return true;
}
//New method added to handle keypress event Deepak 18 Dec 2017
function handleShortCutKeys(pEvent){
	var pName=window.parent.strprocessname;
	var activityname=window.parent.stractivityName;
    var keyCode_str = event.keyCode;
    var pID_name='';
    if((pEvent != undefined && pEvent != null)&&((keyCode_str>47&&keyCode_str<58)||(keyCode_str>95&&keyCode_str<106)||keyCode_str==110||keyCode_str==190)){
        if (pEvent.srcElement){
			pID_name = pEvent.srcElement.name;
		}
        else if (pEvent.target.name){
			pID_name = pEvent.target.name;
		}
		if(pID_name!='' && pName=='RLOS')
{
			if(!keydown_RLOS(pID_name,keyCode_str)){
					cancelBubble(pEvent);
			}	
		}
		else if(pID_name!='' && pName=='CreditCard'){
			if(!keydown_CC(pID_name,keyCode_str)){
					cancelBubble(pEvent);
			}
		}
		else if(pID_name!='' && pName=='PersonalLoanS'){
			if(!keydown_PL(pID_name,keyCode_str)){
					cancelBubble(pEvent);
			}
		}
		else if(pID_name!='' && pName=='DigitalOnBoarding'){
			if(!keydown_CDOB(pID_name,keyCode_str)){
					cancelBubble(pEvent);
			}
		}
    }	
}





				
					
					
						
				
    
		
						
						




function CancelSubForm(pAjax)
{
    if(pAjax.status == "success")
    {      
        window.close();         
    }
}

function setFieldValues(pAjax){
    if(pAjax.status == "success")
    {        
        if((typeof window.parent.parent.frames["workitem"] != 'undefined') && (window.parent.parent.frames["workitem"] != null) ){
            window.parent.parent.frames["workitem"].done();
        }
    }
}

function saveSubForm(){    
    com.newgen.omniforms.formviewer.fireFormValidation("D");
    document.getElementById("btnSave").click();  
}

function wait(){
   
}

function WaitBeforeClick(){
    var start = new Date().getTime();
    while (new Date().getTime() < start + 1000);
}

function getCalculatedDueDate(){
    var result=com.newgen.omniforms.formviewer.getNGValue("TF_DUE_DATE");        
    var billType = com.newgen.omniforms.formviewer.getNGValue("TF_BILL_TYPE");
    var noOfdays = com.newgen.omniforms.formviewer.getNGValue("TF_NO_OF_DAYS");
    var productName = com.newgen.omniforms.formviewer.getNGValue("TF_PROD_TYPE");
    if(productName=='OBD')
        noOfdays = com.newgen.omniforms.formviewer.getNGValue("TF_OTHER_DAYS");
    else
        noOfdays = com.newgen.omniforms.formviewer.getNGValue("TF_NO_OF_DAYS");
    var inputDate = "";
    if (billType=="Sight") {
        inputDate = com.newgen.omniforms.formviewer.getNGValue("TF_LODGEMENT_DATE");
    } else if (billType=="Usance") {
        inputDate = com.newgen.omniforms.formviewer.getNGValue("TF_BILL_DATE");
    }
    inputDate=  getDateFromFormat(inputDate,"dd/MM/yyyy") ;
    if (inputDate!="" && noOfdays!="") {
        var date=new Date();
        date.setTime(inputDate);		
        date.setDate(date.getDate()+parseInt(noOfdays));  
        result = formatDate(date,"dd/MM/yyyy");        
    }       
    return result;
}

function getParameterList(pId,pEvent)
{
var args="";
var pName=window.parent.strprocessname;

if (pName=='RLOS'){
	switch(pEvent.type)
	{
	case 'change': if(pId=='ReqProd'){
						args='Product_type';
					}
					else if(pId=='subProd'){
						args='Product_type ReqProd';
					}
					else if(pId=='AppType'){
						args='Product_type ReqProd subProd';
					}
					else if(pId=='Scheme'){
						args='Product_type ReqProd subProd AppType';
					}
					else if(pId=='CardProd'){
						args='Product_type ReqProd subProd AppType';
					}
					else if(pId=='EmpType'){
						args='Product_type ReqProd subProd AppType';
					}
					else if(pId=='cmplx_EmploymentDetails_targetSegCode'){
						args='cmplx_EmploymentDetails_ApplicationCateg';
					}
				    else if (pId=='cmplx_IncomeDetails_StatementCycle2'){
						args='@this';
					}
					else if (pId=='IncomeDetails_BankStatFrom'){
						args='@this';
					} 
					else if (pId=='cmplx_IncomeDetails_AnnualRentFreq'){
						args='@this';
					} 
					else if (pId=='cmplx_IncomeDetails_AvgCredTurnoverFreq'){
						args='@this';
					}
					 else if (pId=='cmplx_IncomeDetails_CreditTurnoverFreq'){
						args='@this';
					} 
					else if (pId=='cmplx_IncomeDetails_AvgBalFreq' ||pId=='chequeStatus' ||pId=='transtype' ||pId=='transferMode' ||pId=='bankName' ||pId=='dispatchChannel' ||pId=='sourceCode' ||pId=='AppStatus' ||pId=='approvalCode' ||pId=='cmplx_CC_Loan_DDSMode' ||pId=='cmplx_CC_Loan_AccType' ||pId=='cmplx_CC_Loan_DDSBankAName' ||pId=='cmplx_CC_Loan_ModeOfSI' ||pId=='cmplx_CC_Loan_SIOnDueDate' ||pId=='cmplx_CC_Loan_StartMonth' ||pId=='cmplx_CC_Loan_HoldType' ||pId=='cmplx_CC_Loan_VPSPAckage' ||pId=='cmplx_CC_Loan_VPSSourceCode' || pId=='cmplx_EmploymentDetails_targetSegCode' || pId=='cmplx_Customer_Nationality'){
						args='@this';
					}
					else if(pId=='cmplx_EmploymentDetails_EmpIndusSector'){
						args='EMploymentDetails_Frame1';
					}
					else if(pId=='cmplx_EmploymentDetails_Indus_Macro'){
						args='EMploymentDetails_Frame1';
					}
					
					
     break;		
	case 'click': 
				if(pId=='Customer_search' )
							args='cmplx_Customer_Employer_name cmplx_Customer_Employer_code';
				else if(pId=='Button2' )
							args='Liability_New_Frame1 Customer_Frame1 EMploymentDetails_Frame1 Product_Frame1 IncomeDetails_Frame1 FrameName';//added by nikhil for PCSP-513
				else if (pId=='Designation_button'){
					args='cmplx_EmploymentDetails_Designation';
				}
				else if (pId=='DesignationAsPerVisa_button'){
					args='cmplx_EmploymentDetails_DesigVisa';
				}
				else if (pId=='FreeZone_Button'){
					args='cmplx_EmploymentDetails_FreezoneName';
				}
				else if (pId=='ELigibiltyAndProductInfo_Button1'){
					args='Liability_New_Frame1 Customer_Frame1 Product_Frame1 IncomeDetails_Frame1 EMploymentDetails_Frame1 ELigibiltyAndProductInfo_Frame1';
				}
				else if (pId=='AddressDetails_Button1'){
					args='Country';
				}
				else if (pId=='Button_State'){
					args='state';
				}
				else if (pId=='Button_City'){
					args='city';
				}
				else if (pId=='AddressDetails_Button1'){
					args='Country';
				}
				else if (pId=='Button_State'){
					args='state';
				}
				else if (pId=='Button_City'){
					args='city';
				}
				else if (pId=='addr_Add'){
					args='AddressDetails_Frame1';
				}
				else if (pId=='addr_Modify'){
					args='AddressDetails_Frame1';
				}
				else if (pId=='addr_Delete'){
					args='@this';
				}				
				else if (pId=='CardDispatchToButton'){
					args='AlternateContactDetails_carddispatch';
				}
				else if (pId=='ButtonOECD_State'){
					args='AddressDetails_Frame1';
				}
				else if (pId=='FATCA_Add'){
					args='FATCA_Fatca_Frame1';
				}
				else if (pId=='FATCA_Modify'){
					args='FATCA_Fatca_Frame1';
				}
				else if (pId=='FATCA_Delete'){
					args='@this';
				}
				else if (pId=='OECD_add'){
					args='OECD_Frame8';
				}
				else if (pId=='OECD_modify'){
					args='OECD_Frame8';
				}
				else if (pId=='OECD_delete'){
					args='@this';
				}
				else if(pId=='FetchDetails')
				{
					args='Customer_Frame1'; //Nikhil 17Oct 2018 changes done to send complete form on fetch details for integration.
				}
				else if(pId=='Customer_Button1')
				{
					args='Customer_Frame1';
				}
				else if(pId=='Customer_save')
				{
					args='Customer_Frame1';
				}
				else if(pId=='Add' || pId=='Modify' || pId=='Delete')
				{
					args='Product_Frame1';
				}
				
				else if(pId=='IncomingDocNew_Save')
				{
					args='IncomingDocNew_Frame1';
				}
				else if(pId=='Product_Save')
				{
					args='Product_Frame1';
				}
				else if(pId=='CompanyDetails' || pId=='Liability_container' || pId=='Incomedetails' || pId=='FetchWorldCheck_SE' || pId=='WorldCheck_fetch' || pId=='IncomingDocuments' || pId=='CC_Loan' || pId=='DecisionHistoryContainer')
				{
					args='@this';
				}
				else if(pId=='CompanyDetails_Button3')
				{
					args='CompanyDetails_CIF';
				}
				else if(pId=='CompanyDetails_Add')
				{
					args='CompanyDetails_Frame1';
				}
				else if(pId=='CheckEligibility_SE' )
				{
					args='Liability_New_Frame1 Customer_Frame1 CompanyDetails_Frame1  Product_Frame1 IncomeDetails_Frame1';
				}
				else if(pId=='Eligibility_Emp' )
				{
					args='Liability_New_Frame1 Customer_Frame1  EMploymentDetails_Frame1 Product_Frame1 IncomeDetails_Frame1';
				}
				else if(pId=='ELigibiltyAndProductInfo_Button1')
				{
					args='Liability_New_Frame1 Customer_Frame1 CompanyDetails_Frame1 EMploymentDetails_Frame1 Product_Frame1 IncomeDetails_Frame1 ELigibiltyAndProductInfo_Frame1';
				}
				else if(pId=='IncomeDetails_Salaried_Save')
				{
					args='IncomeDetails_Frame2';
				}
				else if(pId=='IncomeDetails_SelfEmployed_Save' || pId=='IncomeDetails_Add' || pId=='IncomeDetails_Modify' ||pId=='IncomeDetails_Delete')
				{
					args='IncomeDetails_Frame3';
				}
				else if(pId=='CompanyDetails_SearchAloc')
				{
					args='compName';
				}
				else if(pId=='Liability_New_Save')
				{
					args='Liability_New_Frame1';
				}
				else if(pId=='ELigibiltyAndProductInfo_Save')
				{
					args='ELigibiltyAndProductInfo_Frame1';
				}
				else if(pId=='ContactDetails_Save')
				{
					args='AltContactDetails_Frame1';
				}
				else if(pId=='CardDetails_save')
				{
					args='CardDetails_Frame1';
				}
				else if(pId=='ReferenceDetails_Reference_Add' || pId=='ReferenceDetails_Reference__modify' || pId=='ReferenceDetails_Reference_delete' || pId=='ReferenceDetails_save')
				{
					args='ReferenceDetails_Frame1';
				}
				else if(pId=='IncomingDoc_Save')
				{
					args='IncomingDoc_Frame1';
				}
				else if(pId=='DecisionHistory_Save')
				{
					args='DecisionHistory_Frame1';
				}
				else if(pId=='BT_Add' || pId=='BT_Modify' || pId=='BT_Delete' || pId=='BTC_save' || pId=='CC_Loan_DDS_save' || pId=='CC_Loan_SI_save' || pId=='CC_Loan_RVC_Save' )
				{
					args='CC_Loan_Frame1';
				}
				else if(pId=='EMploymentDetails_Save')
				{
					args='EMploymentDetails_Frame1';
				}
				else if(pId=='Liability_New_add' || pId=='Liability_New_modify' || pId=='Liability_New_delete')
				{
					args='Liability_New_Frame4';
				}
				else if(pId=='KYC_Add' || pId=='KYC_Modify' || pId=='KYC_Delete')
				{
					args='KYC_Frame7';
				}
				else if(pId=='IncomingDocNew_Addbtn' || pId=='IncomingDocNew_Modifybtn')
				{
					args='IncomingDocNew_Frame1';
				}
				//++below code added by nikhil for Self-Supp CR
				else if(pId=='cmplx_CardDetails_SelfSupp_required' || pId=='CardDetails_Self_add' || pId=='CardDetails_Self_remove')
				{
					args='CardDetails_Frame1';
				}
				//--above code added by nikhil for Self-Supp CR
				else if(pId=='Customer_Check' || pId=='Eligibility_Check')
				{
					args='Customer_Frame1';
				}
				
			break;	
		default: break;			
	}
}
if(pName=='PersonalLoanS')
{
	switch(pEvent.type)
	{
		case "click" :args=PL_click_getParameter(pId);
					   break;
		case 'change':
						if(pId=='ReqProd'){
							args='Product_type';
						}	
						else if(pId=='subProd'){
							args='Product_type ReqProd';
						}
						else if(pId=='AppType'){
							args='Product_type ReqProd subProd';
						}
						else if(pId=='Scheme'){
							args='Product_type ReqProd subProd AppType';
						}
						else if(pId=='EmpType'){
							args='Product_type ReqProd subProd AppType';
						}
						else if(pId=='cmplx_EmploymentDetails_targetSegCode'){
							args='cmplx_EmploymentDetails_ApplicationCateg';
						}
						else if (pId=='NotepadDetails_notedesc'){
							args=pId;
						}
						else if (pId=='cmplx_Decision_Decision'){
							args=pId;
						} 
						else
							args='@this';
						break;
		default: break;
	}
}
else if(pName=='CreditCard'){
	switch(pEvent.type)
	{
		case "click" :
		               if(pId=='Designation_button5_View'){
							args='cmplx_cust_ver_sp2_desig_remarks';
						} 
						else if(pId=='Designation_button6_View'){
							args='cmplx_emp_ver_sp2_desig_remarks';
						} 
						/*else if(pId=='Nationality_Button_View'){
							args='cmplx_Customer_Nationality';
						} */
						else if(pId=='SecNationality_Button_View'){
							args='cmplx_Customer_SecNationality';
						} 
						else if(pId=='Button_City_View'){
							args='AddressDetails_city';
						} 
						else if(pId=='Button_State_View'){
							args='AddressDetails_state';
						} 
						else if(pId=='AddressDetails_Button1_View'){
							args='AddressDetails_country';
						} 
						else if(pId=='CardDispatchToButton_View'){
							args='AlternateContactDetails_CardDisp';
						} 
						else if(pId=='ButtonOECD_State_View'){
							args='OECD_townBirth';
						}
                        else if(pId=='ButtonOECD_State_View'){
							args='OECD_townBirth';
						} 
                        else if(pId=='Designation_button_View'){
							args='cmplx_EmploymentDetails_Designation';
						}            						
						else if (pId=='DesignationAsPerVisa_button_View')
						{
							args='cmplx_EmploymentDetails_DesigVisa';
						}
						else if (pId=='Designation_button8_View')
						{
							args='cmplx_Customer_Designation';
						}
			else{
			args=cc_click_parameterlist(pId);
	}
			break;
		case 'change':
						if(pId=='ReqProd'){
							args='Product_type';
						}	
						else if(pId=='subProd'){
							args='Product_type ReqProd';
						}
						else if(pId=='AppType'){
							args='Product_type ReqProd subProd';
						}
						else if(pId=='Scheme'){
							args='Product_type ReqProd subProd AppType';
						}
						else if(pId=='EmpType'){
							args='Product_type ReqProd subProd AppType';
						}
						else if(pId=='cmplx_EmploymentDetails_targetSegCode'){
							args='cmplx_EmploymentDetails_ApplicationCateg cmplx_EmploymentDetails_targetSegCode';
						}
						else if (pId=='NotepadDetails_notedesc'){
							args=pId;
						}
						else if (pId=='cmplx_DEC_Decision'){
							args=pId;
						} 
				else if(pId=='cmplx_Customer_Designation')
						{
							args=pId;
						}
						else
							args='@this';
						break;	
		default: break;
	}
}else if(pName=='DigitalOnBoarding'){
	switch(pEvent.type)
	{
		case "click" :
		               if(pId=='Designation_button5_View'){
							args='cmplx_cust_ver_sp2_desig_remarks';
						} 
						else if(pId=='Designation_button6_View'){
							args='cmplx_emp_ver_sp2_desig_remarks';
						} 
						/*else if(pId=='Nationality_Button_View'){
							args='cmplx_Customer_Nationality';
						} */
						else if(pId=='SecNationality_Button_View'){
							args='cmplx_Customer_SecNationality';
						} 
						else if(pId=='Button_City_View'){
							args='AddressDetails_city';
						} 
						else if(pId=='Button_State_View'){
							args='AddressDetails_state';
						} 
						else if(pId=='AddressDetails_Button1_View'){
							args='AddressDetails_country';
						} 
						else if(pId=='CardDispatchToButton_View'){
							args='AlternateContactDetails_CardDisp';
						} 
						else if(pId=='ButtonOECD_State_View'){
							args='OECD_townBirth';
						}
                        else if(pId=='ButtonOECD_State_View'){
							args='OECD_townBirth';
						} 
                        else if(pId=='Designation_button_View'){
							args='cmplx_EmploymentDetails_Designation';
						}            						
						else if (pId=='DesignationAsPerVisa_button_View')
						{
							args='cmplx_EmploymentDetails_DesigVisa';
						}
						else if (pId=='Designation_button8_View')
						{
							args='cmplx_Customer_Designation';
						}
			else{
			args=cdob_click_parameterlist(pId);
	}
			break;
		case 'change':
						if(pId=='ReqProd'){
							args='Product_type';
						}	
						else if(pId=='subProd'){
							args='Product_type ReqProd';
						}
						else if(pId=='AppType'){
							args='Product_type ReqProd subProd';
						}
						else if(pId=='Scheme'){
							args='Product_type ReqProd subProd AppType';
						}
						else if(pId=='EmpType'){
							args='Product_type ReqProd subProd AppType';
						}
						else if(pId=='cmplx_EmploymentDetails_targetSegCode'){
							args='cmplx_EmploymentDetails_ApplicationCateg cmplx_EmploymentDetails_targetSegCode';
						}
						else if (pId=='NotepadDetails_notedesc'){
							args=pId;
						}
						else if (pId=='cmplx_DEC_Decision'){
							args=pId;
						} 
				else if(pId=='cmplx_Customer_Designation')
						{
							args=pId;
						}
						else
							args='@this';
						break;	
		default: break;
	}
}

return args;
}	
function setSubFormDimensions()
{
    var dim=new Object();
//    dim["left"]=0;
//        dim["top"]=0;
//        dim["width"]=1000;
//        dim["height"]=1000;
    /*Write your code above this line.
     *Below one is the Product code.
     *Do not change it.
     */
    var count=0;
    for(var key in dim)
        count++;
    if(count==0)
    {
        dim["left"]=30;
        dim["top"]=30;
        dim["width"]=600;
        dim["height"]=600;
    }
    return dim;
}
   //Deepak below method added for SP2 and BPM process.
   function customValidation(opt){
		return true;
   }