var dirtyFlag = {};
var onLoadValues = {};
var stateValues = {};
/*
function getServerValues(fieldId){
	return getFromPreLoadedValues(fieldId,0);	
}

function getServerState(fieldId){
	return getFromPreLoadedValues(fieldId,1);	
}*/
function fieldState(fieldId){
	
	if(isLocked(fieldId)){
		return "Locked";
	}
	else if(!isLocked(fieldId)){
		return "UnLocked";
	}
	else if(isEnabled(fieldId)){
		return "Enabled";
	}
	else if(!isEnabled(fieldId)){
		return "Disabled";
	}
	
}

function states(fieldId){
	this.enabled = getFieldState(fieldId,'enable');
	this.locked = getFieldState(fieldId,'lock');
}
function getFieldState(fieldId,mode){
	if(mode=='enable'){
		if(com.newgen.omniforms.formviewer.isEnabled(fieldId)!=undefined && com.newgen.omniforms.formviewer.isEnabled(fieldId)!='undefined'){
			return com.newgen.omniforms.formviewer.isEnabled(fieldId).toString();
		}
		else{
			return '';
		}
		
	}
	else if(mode=='lock'){
		if(com.newgen.omniforms.formviewer.isLocked(fieldId)!=undefined && com.newgen.omniforms.formviewer.isLocked(fieldId)!='undefined'){
			return com.newgen.omniforms.formviewer.isLocked(fieldId).toString();
		}
		else{
			return '';
		}
		
	}
}
/*
function stateComparison(formField){
	
	if(fieldState(formField)!=getServerState(formField)){
		return false;
	}
	return true;
}*/

function valuesComparison(formField){
	if(formField){
		/*if(dirtyFlag[formField]){
			if(dirtyFlag[formField]!=getNGValue(formField).toString()){
				alert('Devtools activity encountered for '+getLabel(formField));
				return '';
			}
		}
		else if(getNGValue(formField).toString()!=getServerValues(formField)){
				alert('Devtools activity encountered for '+getLabel(formField));
				return '';
		}*/
		//Deepak comented as this can't be used as server side validation - 23-06-2019
		/*
		if(getNGValue(formField).toString()!=onLoadValues[formField]){
				alert('Suspicious activity encountered for '+formField);
				setNGValue(formField,onLoadValues[formField]);
				setLocked(formField,stateValues[formField].locked);
				setEnabled(formField,stateValues[formField].enabled);
		}*/
	}
}
/*
function getLabel(formField){
	return getFromPreLoadedValues(formField,2);
}*/
/*
function getFromPreLoadedValues(formField,index){
	for(var key in onLoadValues){
			if(onLoadValues[formField]){
				return onLoadValues[formField].split('=')[index];
			}
		}
	return '';	
}*/

function start(formField){
	/*if(!stateComparison(formField)){
			if(stateValues.hasOwnProperty(formField)){
				valuesComparison(formField);
			}
			else{
				alert('Devtools activity encountered for '+getLabel(formField));
				return '';
			}
		}*/
		//else{
			valuesComparison(formField);
		//}
}

function getFetchedFragmentFields(){
	var fieldsMap = {};
	var hashMap = {};
	if(window.parent.strprocessname=='CreditCard'){
	hashMap = CCFRAGMENTLOADOPT;
	}
	else if(window.parent.strprocessname=='RLOS'){
	hashMap = RLOSFRAGMENTLOADOPT;
	}
	for(var key in hashMap){
		//if(RLOSFRAGMENTLOADOPT[key]=='N'){
			var el = document.getElementById(key);
			if(el!=null || el!=undefined)
			$.extend(fieldsMap,searchfieldSet(el));
		//}
		//else{continue;}
	}
	setstates(fieldsMap)
	return fieldsMap;
}

function setstates(valuesObj){
	for(var key in valuesObj){
	stateValues[key]= new states(key);	
	}
}

function searchfieldSet(el){
var fieldMap={};
var fragChildren = Array.prototype.slice.call(el.childNodes);
	for(var index in fragChildren){
		if(fragChildren[index].tagName=='FIELDSET'){
			var frameName = fragChildren[index].id;
			var fieldset = document.getElementById(frameName);
			var isGridFragment = false;
			var frameFields = Array.prototype.slice.call(fieldset.childNodes);
			for(var index in frameFields){
				if(frameFields[index].outerHTML.indexOf('type="ListView"')>-1){
					isGridFragment = true;
					continue;
				}
				else if(frameFields[index].tagName=='FIELDSET'){
					$.extend(fieldMap,searchfieldSet(document.getElementById(frameName)));
				}
				else if(frameFields[index].tagName=='DIV' && (frameFields[index].id.indexOf('_Cont')==-1 && frameFields[index].id.indexOf('_cont')==-1)){
					$.extend(fieldMap,getFrameFields(frameFields[index].id));	
				}
			}
			if(!isGridFragment){
			$.extend(fieldMap,getFrameFields(frameName));
			}
			else{break;}
		}
		else{ continue;}
	}
return fieldMap;
}

function getFrameFields(frameName){
	var fieldsinFrame = document.getElementById(frameName).childNodes;
	var temp = {};
	try{
	for(var i=0;i<fieldsinFrame.length;i++){ 
		if((fieldsinFrame[i].type=='text' || fieldsinFrame[i].type=='checkbox' || fieldsinFrame[i].tagName=='SELECT') && isVisible(fieldsinFrame[i].id)){ 
			if(getNGValue(fieldsinFrame[i].id)!=null && getNGValue(fieldsinFrame[i].id)!=undefined && getNGValue(fieldsinFrame[i].id)!='undefined' && getNGValue(fieldsinFrame[i].id)!='null')
			temp[fieldsinFrame[i].id] = getNGValue(fieldsinFrame[i].id).toString();
		}
		else if(fieldsinFrame[i].tagName=='DIV' && fieldsinFrame[i].type!='ListView'){
			var temp1=getFrameFields(fieldsinFrame[i].id);
			//for(var key in temp1){
			$.extend(temp,temp1);
			//}
		}	
	}
	}catch(ex){
			console.log('exception in getframefields: '+ex);
	}
	//return "frame="+frameName+"&fields="+temp;
	return temp;
}
/*
function setFrameFields(frameName){
		var params = getFrameFields(frameName);
		CallAjax_Security('Security_Check.jsp',params);
}*/

function checkDevtools(){
	/*var fields = argsValue.split(" ");
	for(var i=0;i<fields.length;i++){
		var formFields = '';
		if(fields[i].indexOf('Frame')>-1){
			var frameFields = getFrameFields(fields[i]);
			for(var key in frameFields){
				if(isVisible(key)){
				start(key);
				}
			}
		}
		else{
			if(isVisible(key)){
			start(fields[i]);
			}
		}
	}
	return argsValue;*/
	for(var key in onLoadValues){
		start(key);
	}
}
function setEnabledCustom(fieldId,value){
	 setEnabled(fieldId,value);
	 stateValues[fieldId]= new states(fieldId);
}

 function setLockedCustom(fieldId,value){
	 setLocked(fieldId,value);
	 stateValues[fieldId]= new states(fieldId);
 }
 function setNGValueCustom(fieldId,value){
	 setNGValue(fieldId,value);
	 //dirtyFlag[fieldId]=value.toString();
	 if(isVisible(fieldId)){
	 //var mapId = window.parent.pid.split("-")[1]+"_"+window.parent.userName;
	 //CallAjax_Security('Security_Check.jsp',"control="+fieldId+"&data="+value+"&state="+fieldState(fieldId)+"&identifier="+mapId);
	 //dirtyFlag[fieldId]=value.toString();
	 if(stateValues[fieldId] && stateComp(fieldId))
	 onLoadValues[fieldId] = value.toString();
	 }
 }
 function stateComp(fieldId){
	if(stateValues[fieldId].enabled == com.newgen.omniforms.formviewer.isEnabled(fieldId).toString() && stateValues[fieldId].locked == com.newgen.omniforms.formviewer.isLocked(fieldId).toString())
	return true;
	return false;
 }
 
/* function CallAjax_Security(jspName,params)
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
						alert("URL doesn't exist!");
					else 
						alert("Status is "+xmlReq.status);	
					break;
				default:
					//alert(xmlReq.status);
					break;
				}
			}
			
			xmlReq.open('POST','/formviewer/resources/scripts/'+jspName,true);				
			xmlReq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
			xmlReq.send(params);
			//return response;
	   }
	
    catch(e)
    {
		alert("Some error occured while fetching Customer Exposure. Please try ofter sometime or contact administrator");
		//return false;
    }
}*/


