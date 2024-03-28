// Mohit Sharma           30/12/2014           Bug 52409 -SQL Cabinet: While click on introduce workitem, nothing is responding in HTMLViewer while working proper in Appletviewer
// 16/03/2015             Mohit Sharma        Bugzilla – Bug 54594 Accepance of special characters in ValidatorException
// 27/02/2015             Shaveta Rani        Bugzilla – Bug 54181 On raising event WFDone from SaveFormCompleted, the calls SubmitFormStarted and SubmitFormCompleted are not invoked
// 21/05/2015             Mohit Sharma        Bug 55271- Picture to be shown while submitting the form instead of masking in html form
// 21/07/2015             Shaveta Rani        Bugzilla – Bug 55971 Highlighter in case of datepicker is very dark as compare to others.
// 26/08/2015             Shaveta Rani        Bugzilla – Bug 56343 Entries are not saved in form when click on save button only processing bar shown.
// 01/09/2015             Minakshi Sharma     Bugzilla - Bug 56502 Subform Integration optimization(Removal of static map) 
// 21/10/2015             Mohit Sharma        Bug 57445 - Open WI entries are not saved in formview.   
// 11/12/2015             Mohit Sharma        Bug 57949 - Entries are not saved in LDPI form
// 23/02/2017	          Mohit Sharma        Bug 68092 - Shared release of formviewer.war is not working with AddIns 	


var wioperation;
var param;
var width;
var currentWin;
var activityName;
var processName;
var workitemName;
var pid;
var username;
var DATEPICKERBACKGROUNDCOLOR = '#f2f5f7';
var DATEPICKERBORDERCOLOR = '#f2f5f7';
var DATEPICKERCOLOR = 'black';


function ZoneLostFocus()
{
  //Bugzilla – Bug 55971
  //Bug 57445 - Open WI entries are not saved in formview. 
  //Bug 57949 - Entries are not saved in LDPI form
  try{
  var mobiledeviceFlag=document.getElementById("txtMobileDeviceFlag").value;      
  if(mobiledeviceFlag == -1)
    window.parent.ZoneLostFocus();
  }
  catch(ex){
	  
  }
}

function ZoneGotFocus(x1,y1,x2,y2,name)
{
//window.parent.ZoneGotFocus(x1,y1,x2,y2,name);
}

function saveForm(type,param1)
{ 
    wioperation= type;
    param= param1;
    document.getElementById("txtEventType").value=type;
    document.getElementById("btnSave").click();   
}

function saveFormStarted(type,pDummySave)
{
    if(typeof pDummySave != 'undefined'){
         if(document.getElementById("dummySave")!=null){
             if(pDummySave != 'Y'){
                pDummySave = "N";
            }
            document.getElementById("dummySave").value=pDummySave;            
         }
     }
     
     document.getElementById("txtEventType").value=type;
     document.getElementById("btnSaveStarted").click();  
}

function formValidated(pAjax)
{
    com.newgen.omniforms.util.changeCursor(pAjax);
    if(pAjax.status == "success")
    {
        var isIBPSIntegrated = false;
        var bDummySave = "N";
        if(document.getElementById("isIBPSIntegrated") != null){
            isIBPSIntegrated = (document.getElementById("isIBPSIntegrated").value == "Y");
            bDummySave = document.getElementById("dummySave").value;
            if(bDummySave != 'Y'){
                bDummySave = "N";
            }
        }
        
        var validated=document.getElementById("bValidated").value;           
        if(validated=="false")
        { 
            var evntHdlr="";
            var excpHandlerJson;
            if(document.getElementById("excpJson").value!=null && document.getElementById("excpJson").value!="")
            {
                excpHandlerJson = JSON.parse(document.getElementById("excpJson").value);
                evntHdlr = excpHandlerJson.EventHandler; 
            }
            if( evntHdlr != "" )
            {
                 handleExcp(document.getElementById("excpJson").value);
            }
            else
            {
                    var errMsg=document.getElementById("txtErrorMessage").value;
                    //Bugzilla – Bug 54594
                    var res = errMsg.split("~");
                        bFormValidated=false;
                    if(res.length==2)
                        com.newgen.omniforms.util.showError(res[1],res[0]); 
                    else
                        com.newgen.omniforms.util.showError(document.getElementById("ngform"),errMsg);      
            }
            //return false;
            //com.newgen.omniforms.util.showError(document.getElementById("ngform"),errMsg); 
            //if(width==undefined)
            //    width=parseInt(window.parent.parent.frames["workitem"].document.body.clientWidth-10)+"px";
            // resizeForm(width);	 
        }
        else
        {
            var qString = "ngParam=" + document.getElementById("formXml").value + "&EventType=" + document.getElementById("txtEventType").value + "&isDummySave="+bDummySave;
            var response = oforms.ajax.processRequest(qString,decode_utf8(document.getElementById("formSavingUrl").value));
            if(isIBPSIntegrated){
                var mainCode = response.substring(response.indexOf("<MainCode>") + 10 , response.indexOf("</MainCode>"));
                if( mainCode != "0"){
                     bFormValidated = false;
                     com.newgen.omniforms.util.showError("Unable to save workitem.There is some error at server end!");
                }
                else{
                    document.getElementById("responseXml").value = response;
                    document.getElementById("btnPostSave").click();
                }
            }
            else{
                if (response == "701") 
                {
                    bFormValidated = false;
                    com.newgen.omniforms.util.showError("Unable to save workitem.There is some error at server end!");
                }
                else if (response != "") 
                {
                    bFormValidated = false;
                    com.newgen.omniforms.util.showError("Unable to save workitem.There is some error at server end!!");
                }
            }
        }
        var eventType=document.getElementById("txtEventType").value;
        window.parent.jsfformvalidation(eventType,validated,bringNextWorkItem,bDummySave);
        bringNextWorkItem=true;
        document.getElementById("dummySave").value='';            
    }
}


function handleExcp(excpHandlerJson)
{
    excpHandlerJson = customEventHandler(excpHandlerJson);
    document.getElementById("excpJson").value = JSON.stringify(excpHandlerJson);
    document.getElementById("btnEventHandler").click(); 
}

function decode_utf8(utftextBytes)
{
    var utftext = unescape(utftextBytes);
    var plaintext = "",temp;

    var i=c1=c2=c3=c4=0;

    while(i<utftext.length)
    {
        c1 = utftext.charCodeAt(i);
        temp = '?';

        if (c1<0x80)
        {
            temp = String.fromCharCode(c1);
            i++;
        }
        else if( (c1>>5) ==    6) //2 bytes
        {
            c2 = utftext.charCodeAt(i+1);

            if( !((c2^0x80)&0xC0))
                temp = String.fromCharCode(((c1&0x1F)<<6) | (c2&0x3F));
            i+=2;
        }
        else if( (c1>>4) == 0xE)  //3 bytes
        {
            c2 = utftext.charCodeAt(i+1);
            c3 = utftext.charCodeAt(i+2);

            if( !(((c2^0x80)|(c3^0x80))&0xC0) )
                temp = String.fromCharCode(((c1&0xF)<<12) | ((c2&0x3F)<<6) | (c3&0x3F));
            i+=3;
        }
        else
            i++;
        plaintext += temp;
    }
    return plaintext;
}

function customEventHandler(excpHandlerJson)
{
    excpHandlerJson=JSON.parse(excpHandlerJson);
    var eventHandlerName = decode_utf8(excpHandlerJson.EventHandler);
    var params = JSON.parse(excpHandlerJson.Parameters);
    var jsonParam = {};
    var firstParam = decode_utf8(excpHandlerJson.Summary);
    var secondParam = decode_utf8(excpHandlerJson.Detail);    
    for( var key in params )
    {
        jsonParam[decode_utf8(key)] = decode_utf8(params[key]);
    }
    params = jsonParam;
    /*
     *do not change here anything
     */
    if( eventHandlerName == "productAPI")
    {
        if( secondParam == "printTemplate")
        {
            if( window.printTemplate )
            {   
                printTemplate( firstParam , jsonParam );
            }
        }
        return;
    }
    /*
     **/
    excpHandlerJson.EventHandler = eventHandlerName;
    excpHandlerJson.Summary = firstParam;
    excpHandlerJson.Detail = secondParam;
    excpHandlerJson.Parameters = params; 
    return excpHandlerJson;
}

function formSaved(pAjax)
{
    if(pAjax.status == "begin")
    {
        CreateIndicator("temp");
        document.getElementById("fade").style.display="block";
    }
    if(pAjax.status == "success")
    {
        //Bugzilla – Bug 54181
        com.newgen.omniforms.util.changeCursor(pAjax);
        var validated=document.getElementById("bValidated").value; 
       var mobiledeviceFlag=document.getElementById("txtMobileDeviceFlag").value; 
       //Bugzilla – Bug 56343
        removeMasking();
        if(validated=="true"){
            //window.parent.formSaved(wioperation, param);
            if(mobiledeviceFlag!=-1)
                {
                    parent.postMessage(wioperation,"*"); 
                }
            else                    
                window.parent.formSaved(wioperation, param);
        } 
        else
        {
            var evntHdlr="";
            var excpHandlerJson;
            if(document.getElementById("excpJson").value!=null && document.getElementById("excpJson").value!="")
            {
                excpHandlerJson = JSON.parse(document.getElementById("excpJson").value);
                evntHdlr = excpHandlerJson.EventHandler; 
            }
            if( evntHdlr != "" )
            {
                 handleExcp(document.getElementById("excpJson").value);
            }
            else
            {
                var errMsg=document.getElementById("txtErrorMessage").value;
                var res = errMsg.split("~");
                if(res.length==2)
                    com.newgen.omniforms.util.showError(res[1],res[0]); 
                else
                    com.newgen.omniforms.util.showError(document.getElementById("ngform"),errMsg);           
            }
            
            
            //com.newgen.omniforms.util.showError(document.getElementById("ngform"),errMsg); 
            //if(width==undefined)
            //    width=parseInt(window.parent.parent.frames["workitem"].document.body.clientWidth-10)+"px";
            // resizeForm(width);	 
        }
        
    }
}

function handleShortCutKeys(event){
    var keyCode = event.keyCode;    // returns keyCode of various key present in keyboard...for ex : Tab has keycode = 9
    if(event.shiftKey)              // checks whether shiftkey is pressed or not
    {
        if( keyCode == 9 )
            {
                // do something
            }
    }
    if( event.ctrlKey )             // checks whether control key is pressed or not
    {
         if( keyCode == 9 )
            {
                // do something
            }
    }
}
function setWIAttributes(strprocessname,stractivityName,strUsername,strWorkitemName)
{
    processName = strprocessname;
    activityName = stractivityName;
    username=strUsername;
    workitemName=strWorkitemName;
	pid=workitemName;
}

function NGF_NotifyDataLoaded()
{
    //Bug 52409 -SQL Cabinet: While click on introduce workitem, nothing is responding in HTMLViewer while working proper in Appletviewer
    try
    {
        window.parent.NGF_NotifyDataLoaded('');
    }
    catch(ex){
        
    }
} 

function setClosedSubFormRef(winref,pData){	
	currentWin=winref;
        com.newgen.omniforms.util.iterateSubValue(pData);
	document.getElementById("btnRefresh").click();                
        /// js method on form along with new data test(data);
}

function getIVAppletObject(){
    return window.parent.parent.document.IVApplet;
}

function renderForm(pAjax){		
    if(pAjax.status=="success"){        
        //com.newgen.omniforms.util.iterateSubValue(); 
      /*  if(width==undefined)
            width=parseInt(window.parent.parent.frames["workitem"].document.body.clientWidth-10)+"px";
        resizeForm(width);	     */	                    
	currentWin.close();		
    }
}

function removeMasking(){
    setTimeout( function(){
        RemoveIndicator("temp");
        document.getElementById("fade").style.display="none";
    }
    , 1000);    
}


function resizeForm(pWidth){
    width=pWidth;
    var height=parseInt(window.parent.parent.frames["workitem"].document.body.clientHeight-50)+"px"; 
    com.newgen.omniforms.formviewer.resizeForm(pWidth);   
    document.getElementById('ngform').style.width = width;    
    document.getElementById('ngform').style.height=height;   
}

//Bugzilla – Bug 52190 Not able to get the Document Applet Object in my ngfUser for HTML
 
function getIVAppletObject()
{
    return window.parent.parent.document.IVApplet;
}
 

function CreateIndicator(indicatorFrameId){
    var ParentDocWidth = document.body.clientWidth;
    var ParentDocHeight = document.body.clientHeight;
    if( ParentDocHeight == 0 )
        ParentDocHeight = document.getElementById("ngform").clientHeight
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
        img.setAttribute("src", "../../resources/images/progress.gif");
        img.setAttribute("name", indicatorFrameId);
        img.setAttribute("id", indicatorFrameId);
        img.style.left = ImgLeft+"px";
        img.style.top = ImgTop+"px";
        img.style.width = "100px";
        img.style.height = "18px";
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
 
 function attachEvents(){      
   
   $('input[type="text"]').on({
		"change": function(e){                
		var jsan = {'javax.faces.behavior.event':'valueChange',execute:'@form',render:0};
                dispatchCtrlEvent($(this),e,jsan)
            }
        });
        
   $('input[type="text"]').on({
		"focus": function(e){                
		var jsan = {'javax.faces.behavior.event':'focus',execute:'@form',render:0};
                focushandler($(this));
                dispatchCtrlEvent($(this),e,jsan);
            }
        });     
   $('input[type="text"]').on({
		"keydown": function(e){                
		var jsan = {'javax.faces.behavior.event':'keydown',execute:'@form',render:0};
                dispatchCtrlEvent($(this),e,jsan)
            }
        });    
   $('input[type="text"]').on({
		"blur": function(e){                
		var jsan = {'javax.faces.behavior.event':'blur',execute:'@form',render:0};
                dispatchCtrlEvent($(this),e,jsan)
            }
        });    
}
function formPopulated(){
    // do not change here anything start
   // attachEvents();
    // do not change here anything end
    
}

function FetchDataForSubForm()
{
    document.getElementById("btnOpenSubForm").click();  
}


window.open_=window.open;

window.open=function(m_url,m_name,m_properties)
{
    var actionURL=getActionUrlFromURL(m_url);
    var listParam=getInputParamListFromURL(m_url);
    var win = openNewWindow(actionURL, m_name, m_properties, true,"Ext1","Ext2","Ext3","Ext4",listParam);
    return win;
}

function openNewWindow(sURL, sName, sFeatures, bReplace, Ext1, Ext2, Ext3, Ext4, listParameters)
{
//    if(sURL.indexOf("?")>0)
//    {
//        if(OAP_Id!=undefined || OAP_Id!=null)
//            sURL=sURL + "&OAP_Id="+ OAP_Id;
//    }
//    else
//    {
//        if(OAP_Id!=undefined || OAP_Id!=null)
//            sURL=sURL + "?OAP_Id="+ OAP_Id;
//    }
    var popup = window.open_('/formviewer/pages/view/blank.xhtml',sName,sFeatures,bReplace);
    popup.document.write("<HTML><HEAD><TITLE></TITLE></HEAD><BODY>");
   
    popup.document.write("<form id='postSubmit' method='post' action='"+sURL+"' enctype='application/x-www-form-urlencoded'>");
    for(var iCount=0;iCount<listParameters.length;iCount++)
    {
        var param=listParameters[iCount];
        popup.document.write("<input type='hidden' id='"+param[0]+"' name='"+param[0]+"'/>");
        popup.document.getElementById(param[0]).value=param[1];//handle single quotes etc
    }
    popup.document.write("</FORM></BODY></HTML>");	
    popup.document.close();
   // popup.document.forms[0].submit();
    
    try{
        popup.focus();
    }catch(e){}
    
    return popup;
} 

function getActionUrlFromURL(sURL)
{
    var ibeginingIndex=sURL.indexOf("?");
    if (ibeginingIndex == -1)
        return sURL;
    else
        return sURL.substring(0,ibeginingIndex);
}

function getInputParamListFromURL(sURL)
{
    var ibeginingIndex=sURL.indexOf("?");
    var listParam=new Array();
    if (ibeginingIndex == -1){
        return listParam;
    }
    var tempList = sURL.substring(ibeginingIndex+1,sURL.length);

    if(tempList.length>0)
    {
        var arrValue =tempList.split("&");
        for(var iCount=0;iCount<arrValue.length;iCount++)
        {
            var arrTempParam=arrValue[iCount].split("=");
            try
            {
                listParam.push(new Array(decode_ParamValue(arrTempParam[0]),decode_ParamValue(arrTempParam[1])));
            }catch(ex)
            {
            }
        }
    }
    return listParam;
}

function decode_ParamValue(param)
{
    var tempParam =param.replace(/\+/g,' ');
    tempParam = decodeURIComponent(tempParam);

    return tempParam;
}