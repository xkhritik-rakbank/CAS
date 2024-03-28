	/*-----------------------------------------------------------------------------------------------------
File Name			: RLOSGlobal.js
Author              : sanjeev-kumar@newgen.co.in
Date written (DD/MM/YYYY)    : 22-Aug-2016 
Description			: This file contains the generic functions for RLOS
					 Please always maintain change history.
----------------------------------------------------------------------------------------------------
CHANGE HISTORY
----------------------------------------------------------------------------------------------------
	Date     		Edited By	   		Comments
-----------       ----------------     ----------                
22-Aug-2016       	sanjeev-kumar	   variable pName,actName added for process/activity name.
22-Aug-2016       	sanjeev-kumar	   Function getNGValue(skControl) added for fetching form field value.
22-Aug-2016       	sanjeev-kumar	   Function setNGValue(skControl,skValue) added for setting form field value.
22-Aug-2016       	sanjeev-kumar	   Function setNGFocus(skControl) added for setting focus on field.
22-Aug-2016       	sanjeev-kumar	   Function showAlert(skControl,msg) added for showing msg to user.
22-Aug-2016       	sanjeev-kumar	   Function Trim(value) triming the value.
22-Aug-2016       	sanjeev-kumar	   Function isFieldFilled(fieldName) added for validating blank field.

----------------------------------------------------------------------------------------------------*/
var pName=""; //process name

var actName=""; //activity name
try{
	pName=window.parent.strprocessname;		
	actName=window.parent.stractivityName;
}catch(e){}
try{
	
	document.write("<script src='/formviewer/resources/scripts/CDOB/CDOB_Constants.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/CDOB/DigitalOnBoarding.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/RLOS/Common_util.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/RLOS/RLOSCommon.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/CDOB/CDOB_Validations.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/Doc_template_generation.js'></script>");


}catch(e){alert("Exception rlosGlobal"+e);}

//----------------------------------------------------------------------------------------------------
//Function Name                    : getNGValue(string)
//Date Written (DD/MM/YYYY)        : 22-Aug-2016
//Author                           : sanjeev-kumar@newgen.co.in
//Input Parameters                 : String
//Return Values                    : string
//Description                      : returns the value of control.
//----------------------------------------------------------------------------------------------------
function getNGValue(skControl){return com.newgen.omniforms.formviewer.getNGValue(skControl);}
//----------------------------------------------------------------------------------------------------
//Function Name                    : setNGValue(string,string)
//Date Written (DD/MM/YYYY)        : 22-Aug-2016
//Author                           : sanjeev-kumar@newgen.co.in
//Input Parameters                 : String
//Description                      : set the value of control.
//----------------------------------------------------------------------------------------------------
function setNGValue(skControl,skValue){com.newgen.omniforms.formviewer.setNGValue(skControl,skValue);}
//----------------------------------------------------------------------------------------------------
//Function Name                    : setNGFocus(string)
//Date Written (DD/MM/YYYY)        : 22-Aug-2016
//Author                           : sanjeev-kumar@newgen.co.in
//Input Parameters                 : String
//Description                      : set the focus on control.
//----------------------------------------------------------------------------------------------------
function setNGFocus(skControl){com.newgen.omniforms.formviewer.setNGFocus(skControl);}
//----------------------------------------------------------------------------------------------------
//Function Name                    : showAlert(string,string)
//Date Written (DD/MM/YYYY)        : 22-Aug-2016
//Author                           : sanjeev-kumar@newgen.co.in
//Input Parameters                 : String
//Description                      : Show the alert as required and set the focus on control.
//----------------------------------------------------------------------------------------------------
function showAlert(skControl,skMessage){com.newgen.omniforms.util.showError(document.getElementById(skControl),skMessage);}
//----------------------------------------------------------------------------------------------------
//Function Name                    : setVisible(string,boolean)
//Date Written (DD/MM/YYYY)        : 25-Jan-2017
//Author                           : akshay.gupta@newgen.co.in
//Input Parameters                 : String,Boolean
//Description                      : set visibility of a control
//----------------------------------------------------------------------------------------------------
function  isVisible(skControl){return com.newgen.omniforms.formviewer.isVisible(skControl);}
//added by yash on 22nd nov 17 for cam report.
function getFromJSPTable(iframeId){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab');}
function getFromJSPSpecificRow(iframeId,rownum){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab').rows[rownum];}
function getFromJSPSpecificRowCell(iframeId,rownum,cellnum){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab').rows[rownum].cells[cellnum];}
function getFromJSPSpecificRowCellContent(iframeId,rownum,cellnum){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab').rows[rownum].cells[cellnum].childNodes[0];}
function getNGSelectedItemText(skControl){return com.newgen.omniforms.formviewer.getNGSelectedItemText(skControl);}//added by tarang on 22/2/18
function  setVisible(skControl,skValue){com.newgen.omniforms.formviewer.setVisible(skControl,skValue);}
//----------------------------------------------------------------------------------------------------
//Function Name                    : setEnabled(string,string)
//Date Written (DD/MM/YYYY)        : 25-Jan-2017
//Author                           : akshay.gupta@newgen.co.in
//Input Parameters                 : String,Boolean
//Description                      : set state of a control
//----------------------------------------------------------------------------------------------------
function  setEnabled(skControl,skValue){com.newgen.omniforms.formviewer.setEnabled(skControl,skValue);}

function setLocked(skControl,skValue){com.newgen.omniforms.formviewer.setLocked(skControl, skValue);}
//----------------------------------------------------------------------------------------------------
//Function Name                    : Trim(string)
//Date Written (DD/MM/YYYY)        : 22-Aug-2016
//Author                           : sanjeev-kumar@newgen.co.in
//Input Parameters                 : String
//Return Values                    : string
//Description                      : returns string after triming it.
//----------------------------------------------------------------------------------------------------
function getHeight(skControl){return com.newgen.omniforms.formviewer.getHeight(skControl);}

//added by nikhil for Finacle core alignment
function setHeight(skControl){return com.newgen.omniforms.formviewer.setHeight(skControl,skValue);}
function getTop(skControl){return com.newgen.omniforms.formviewer.getTop(skControl);}

function setTop(skControl,skValue){com.newgen.omniforms.formviewer.setTop(skControl,skValue);}
function  isEnabled(skControl){return com.newgen.omniforms.formviewer.isEnabled(skControl);}
function  isLocked(skControl){return com.newgen.omniforms.formviewer.isLocked(skControl);}
function setLeft(skControl,skValue){com.newgen.omniforms.formviewer.setLeft(skControl, skValue);}

function getLVWRowCount(skControl){return com.newgen.omniforms.formviewer.getLVWRowCount(skControl);}

function getLVWAT(skControl,rowIndex,columnIndex){return com.newgen.omniforms.formviewer.getLVWAT(skControl,rowIndex,columnIndex);}

function setSheetVisible(skControl,Index,skValue){com.newgen.omniforms.formviewer.setSheetVisible(skControl,Index,skValue);}

function setNGFrameState(skControl,skValue){com.newgen.omniforms.formviewer.setNGFrameState(skControl,skValue);}
//added by akshay on 9/12/17
function getNGListIndex(skControl){return com.newgen.omniforms.formviewer.getNGListIndex(skControl);}

function Trim(str) 
{ 
	if(str==null)return str;
	var i=0;var j=0;
	for (i=0;i < str.length;i++) {
		if (str.charAt(i) != ' ') 
			break; 
	} 
	for (j=str.length - 1;j>= 0; j--) { 
		if (str.charAt(j) != ' ') 
		break; 
	}
	//if (j < i) j = i-1; str = str.substring(i, j+1); 
		return str; 
}
//----------------------------------------------------------------------------------------------------
//Function Name                    : isFieldFilled(fieldName)
//Date Written (DD/MM/YYYY)        : 22-Aug-2016
//Author                           : sanjeev-kumar@newgen.co.in
//Input Parameters                 : fieldName,displayName
//Return Values                    : boolean
//Description                      : Check whether the value has entered in the each control or not.
//---------------------------------------------------------------------------------------------------
function isFieldFilled(fieldName)
{	
	 var fieldValue = Trim(getNGValue(fieldName));
		if((fieldValue=='') ||(fieldValue==null) || (fieldValue=='--SELECT--')||(fieldValue=='--Select--'))
		{
			return false;
		}
	return true;
}

function checkMandatory(constantName){
	var mField = constantName.split(",");
		for(var i = 0; i < mField.length; i++) {
		var flag = true;
		var j = mField[i].toString().split("#");
			if(j[0]=="PartMatch_CIFID" && getNGValue('cmplx_Customer_NTB')){flag = false;}
			if((j[0]=='cmplx_HCountryVerification_personcontctd' || j[0]=='cmplx_HCountryVerification_Relwithpersoncntcted') && getNGValue('cmplx_HCountryVerification_Hcountrytelverified')!='Yes'){flag = false;}
			if(j[0]=='cmplx_EligibilityAndProductInfo_InterestType' && getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2)!='IM'){flag = false;}
			// disha 06-10-2017 change accroding to offshore change in notepad grid name
			//if((j[0].indexOf('_GRID')!=-1)||(j[0].indexOf('_GR')!=-1) ||(j[0].indexOf('inwardtt')!=-1) ||(j[0].indexOf('_CompanyGrid')!=-1) ||(j[0].indexOf('_partnerGrid')!=-1)||(j[0].indexOf('_Telloggrid')!=-1)|| (j[0].indexOf('_cmplx_notegrid')!=-1)||(j[0].indexOf('RejectEnqGrid')!=-1))
			if((j[0].indexOf('_GRID')!=-1)||(j[0].indexOf('_GR')!=-1) ||(j[0].indexOf('inwardtt')!=-1) ||(j[0].indexOf('_CompanyGrid')!=-1) ||(j[0].indexOf('_partnerGrid')!=-1)||(j[0].indexOf('_Telloggrid')!=-1)|| (j[0].indexOf('_notegrid')!=-1)||(j[0].indexOf('RejectEnqGrid')!=-1)||(j[0].indexOf('_Grid')!=-1)||(j[0].indexOf('_supplementGrid')!=-1))			
			{
				//dont uncomment this - disha 06-10-2017 this is uncommented at Onsite but we have done accroding to offshore now
				if(j[0].indexOf('_GRID')>-1){//added by akshay on 11/1/18
					j[0]=j[0].substring(0,j[0].length-5);
				}
				if(getLVWRowCount(j[0])==0){
					showAlert(j[0],j[1]+" cannot be blank");
					return false;
				}	
			}
			
			//else if(!isFieldFilled(j[0]) && flag){
			// disha 06-10-2017 below change according to onsite
			else if(!isFieldFilled(j[0]) && flag && isVisible(j[0]) && com.newgen.omniforms.formviewer.isEnabled(j[0]) && !isLocked(j[0])){
				if(j[0]=="cmplx_MOL_docatt")
					showAlert(j[0],j[1]+" field is mandatory");
				else
					showAlert(j[0],j[1]+" is mandatory");
					return false;				
			}
		}
		
	return true;
}


//