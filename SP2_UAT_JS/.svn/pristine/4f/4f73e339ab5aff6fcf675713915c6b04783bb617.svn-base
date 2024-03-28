	/*-----------------------------------------------------------------------------------------------------
File Name			: PLGlobal.js
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
	
	document.write("<script src='/formviewer/resources/scripts/PL/PL_Constants.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/PL/PersonalLoanS.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/PL/PLCommon.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/PL/PL_Validations.js'></script>");
	document.write("<script src='/formviewer/resources/scripts/RLOS/Common_util.js'></script>");

}catch(e){alert("Exception PL Global"+e);}

//----------------------------------------------------------------------------------------------------
//Function Name                    : getNGValue(string)
//Date Written (DD/MM/YYYY)        : 22-Aug-2016
//Author                           : sanjeev-kumar@newgen.co.in
//Input Parameters                 : String
//Return Values                    : string
//Description                      : returns the value of control.
//----------------------------------------------------------------------------------------------------
function getNGValue(skControl){return com.newgen.omniforms.formviewer.getNGValue(skControl);}
function getFromJSPTable(iframeId){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab');}
function getFromJSPSpecificRow(iframeId,rownum){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab').rows[rownum];}
function getFromJSPSpecificRowCell(iframeId,rownum,cellnum){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab').rows[rownum].cells[cellnum];}
function getFromJSPSpecificRowCellContent(iframeId,rownum,cellnum){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab').rows[rownum].cells[cellnum].childNodes[0];}
function getFromJSPRowCount(iframeId){return document.getElementById(iframeId).contentWindow.document.getElementById('mytab').rows.length;}

function getNGSelectedItemText(skControl){return com.newgen.omniforms.formviewer.getNGSelectedItemText(skControl);}//added by tarang on 22/2/18
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
function  isEnabled(skControl){return com.newgen.omniforms.formviewer.isEnabled(skControl);}
function  isLocked(skControl){return com.newgen.omniforms.formviewer.isLocked(skControl);}
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

function setSheetVisible(skControl,Index,skValue){com.newgen.omniforms.formviewer.setSheetVisible(skControl,Index,skValue);}

function getHeight(skControl){return com.newgen.omniforms.formviewer.getHeight(skControl);}

function getTop(skControl){return com.newgen.omniforms.formviewer.getTop(skControl);}

function setTop(skControl,skValue){com.newgen.omniforms.formviewer.setTop(skControl,skValue);}

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
	if (j < i) j = i-1; str = str.substring(i, j+1); 
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
		// ++ below code already present - 09-10-2017 --Select-- added
		if((fieldValue=='') ||(fieldValue==null) || (fieldValue.toUpperCase()=='--SELECT--')|| (fieldValue.toUpperCase()=='--Select--'))
		{
			return false;
		}
	return true;
}

function checkMandatory(constantName){
	var mField = constantName.split(",");
		for(var i = 0; i < mField.length; i++) {
			//alert( mField[i]);
		var j = mField[i].toString().split("#");
		//alert( j[0]);
			if(j[0].indexOf('_GRID')>-1)
			{
				j[0]=j[0].substring(0,j[0].length-5);
				if(getLVWRowCount(j[0])==0){
					showAlert(j[0],j[1]+" cannot be blank");
					return false;
				}	
			}
			
			else if(!isFieldFilled(j[0]) && isVisible(j[0]) && com.newgen.omniforms.formviewer.isEnabled(j[0]) && !com.newgen.omniforms.formviewer.isLocked(j[0])){
					showAlert(j[0],j[1]+" is mandatory");
					return false;				
			}
		}
	return true;
}


//