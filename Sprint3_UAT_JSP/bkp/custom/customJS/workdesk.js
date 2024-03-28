/*-----------------------------------------------------------------------------------------------------

File Name			: workdesk.js
Date written (DD/MM/YYYY)       :
Description			:
----------------------------------------------------------------------------------------------------
CHANGE HISTORY
----------------------------------------------------------------------------------------------------
Date(DD/MM/YYYY)    Change Description (Bug No. (If Any))
----------------------------------------------------------------------------------------------------
16/09/2011          28307
               WCL_9.0_038     28/01/2011       
WCL_8.0_237     25/06/2010     
WCL_9.0_025     11/01/2011       
03/11/2011              28850
03/11/2011          28745      
03/11/2011          28890       
04/11/2011          28769       
07/11/2011          28893       
23/11/2011          29308       
23/11/2011          29306      
25/11/2011          Bug 29170 
27/11/2011          Bug 29623  
28/12/2011           29628   
29/12/2011       29610      
29/12/2011       29695      
13/01/2012       29680      
30/01/2012       30308     
30/01/2012       30265     
30/01/2012       30266      
09/02/2012       30421     
30/03/2012       30922     
14/05/2012       31846 -    
09/07/2012       33089 -    
 02/08/2012       33710
 30543           08/06/2012          
Bug 30946      
 Bug 32608      
Bug 34156 
Bug 35735 -   11/20/2012     
Bug 36889      28/11/2012    
27/09/2013          Bug 41792 
03/06/2014      `Bug 42170    
06/06/2014       Bug 44632       
30/07/2014       Bug 42621       
04/08/2014       Bug 41862       
06/08/2014       Bug 47668                     
05/08/2014     PRDP bug 37598     
06/08/2014     PRDPBug 44696      
07/08/2014     PRDPBug 46495  
19/08/2014     PRDP Bug 38730    
15/06/2016     PRDP Bug 62299              
13/05/2016     Bug 60879 
29/04/2016     Bug 60314 
13/05/2016     Bug 60877 
13/05/2016     Bug 60878 
13/05/2016     Bug 60881 
13/05/2016     Bug 60875 
08/09/2016     Bug 64280 
07/11/2016     Bug 65276 
1/12/2016      Bug 66049 
12/12/2016     Bug 66206 
14/12/2016     Bug 55799
16/12/2016     Bug 55065
16/12/2016     Bug 64845       
22/12/2016     Bug 66390 
21/12/2016     Bug 57461 
 *                                                        
                                                           
                                                         
27/12/2016      Bug 66459 
04/12/2017      Bug 66587
04/01/2017      Bug 56513
4/01/2017       Bug 59916
05/01/2017      Bug 56011
12/01/2017      Bug 55372
111/01/2017     Bug 55055
17/01/2017      Bug 66744
27/03/2017      Bug 68117
29/03/2017      Bug 67762
29/03/2017      Bug 67103
12/04/2017      Bug 68500
27/12/2016     Bug 66459 
04/12/2016     Bug 66587 
04/01/2017     Bug 56513 
4/01/2017      Bug 59916 
05/01/2017     Bug 56011 
12/01/2017     Bug 55372
111/01/2017    Bug 55055 
17/01/2017     Bug 66744
27/03/2017     Bug 68117
29/03/2017     Bug 67762
29/03/2017     Bug 67103
04/12/2017     Bug 66587 
04/01/2017     Bug 56513 
4/01/2017      Bug 59916 
05/01/2017     Bug 56011 
12/01/2017     Bug 55372 
111/01/2017    Bug 55055 
17/01/2017     Bug 66744
27/03/2017     Bug 68117
29/03/2017    Bug 67762
29/03/2017    Bug 67103

04/12/2017     Bug 66587 
04/01/2017      Bug 56513 
04/12/2017      Bhanu Priya               Bug 66587 - Kindly provide Post Hook For Scan and Import Document.
04/01/2017      Nusbah Suroor           Bug 56513 - Support of refreshing document interface provided in case of overwriting and creating newversion via custom code.
04/12/2017       Bug 66587
04/01/2017      Bug 56513
4/01/2017       Bug 59916 
05/01/2017      Bug 56011 
12/01/2017      Bug 55372 
05/01/2017      Nusbah      Bug 56011 - Provide all the Hooks in client.js which are Present in OmniFlow 8.0, but missing in 9.0
12/01/2017      Nusbah      Bug 55372 - To append the Workitem no in Document Image or PDF while downloading the document
05/01/2017      Bug 56011
12/01/2017      Bug 55372
111/01/2017     Bug 55055 
17/01/2017      Bug 66744
27/03/2017      Bug 68117
29/03/2017      Bug 67762
29/03/2017      Bug 67103
12/04/2017      Bug 68500
12/04/2017      Bug 68530
12/04/2017      Bug 68535 
13/04/2017      Bug 68577
14/04/2017      Bug 68630      
26/04/2017      Bug 68517  
15/05/2017      Bug 69072
22/05/2017      Bug 69398 
21/07/2017      Bug 70568
21/07/2017      Bug 70567
07/08/2017      Bug 70894 
2/03/2017       Bug 67671
02/11/2017      Bug 73141
22/11/2017      Bug 73729
12/12/2017      Bug 74132
08/01/2018      Bug 74700      
18/01/2018      Bug 75231 
02/02/2018      Bug 75729
15/02/2018      Bug 76101   
26/04/2018          Bug 77382 
31/05/2018      Bug 78222
11/06/2018      Bug 78374 
28/06/2018      Bug 78644 
29/06/2018      Bug 78679
03/07/2018      Bug 78733       
24/07/2018      Bug 79177 - Arabic sp2: Annotation not getting saved (specific to arabic)
2/8/2018        Bug 79540 - Unable to save/introduce/done the WI from embedded view
23/08/2018      Bug 79847 - Need method to show informational message at done 
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
var saveCalled=false;
var ngfrmBuffer="";
var showHide='Y';
var customChild=new Array();
var customChildCount=-1;
var changeEvt="";
var WD_UID;
var docListwin;
var saveCheck=false;
var isOnchangeDoc="";
var bFetchModifiedFVB = false;
var saveReturnOption='';
var bUnlockWorkitem = true;

function fetchFieldValueBag(){

    //alert("fetchFieldValueBag");

    if(navigator.appName.indexOf("Netscape") != -1 && wiproperty.formType=="NGFORM" && ngformproperty.type=="applet" && ngfrmBuffer!=""){
        return ngfrmBuffer;
    }
    else{
        
        var window_workdesk;
        if(windowProperty.winloc=='N'){
            window_workdesk = window.opener;                          
        }
        else{
            window_workdesk = window;   
        }
        ngfrmBuffer="";        
        var requestString='pid='+encode_utf8(pid)+'&wid='+wid+'&taskid='+taskid+'&WD_UID='+WD_UID;
        if(window_workdesk.bFetchModifiedFVB)
        requestString+='&FetchModified=Y';    
    
        window_workdesk.bFetchModifiedFVB = false;
    
        var ajaxReq;
        if (window.XMLHttpRequest) {
            ajaxReq= new XMLHttpRequest();
        }else if (window.ActiveXObject) {
            ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
        }
        var url = "../ajaxgetAttribute.jsf";
        url = appendUrlSession(url);
        ajaxReq.open("POST", url, false);
        ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        ajaxReq.send(requestString);
       if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
            strAttribData=ajaxReq.responseText;
        }
        else
        {
            if(ajaxReq.status==598)
            {
                setmessageinDiv(ajaxReq.responseText,"false",3000);
                //alert(ajaxReq.responseText);
            }
            else if(ajaxReq.status == 599) {
                //window.open(sContextPath+"/login/logout.jsp?"+"error=4020",reqSession);
                url = sContextPath+"/error/errorpage.jsf?msgID=4020";
                url = appendUrlSession(url);
                var width = 320;
                var height = 160;
                var left = (window.screen.availWidth-width)/2;
                var top = (window.screen.availHeight-height)/2;

                //window.open(url,reqSession);
                if (window.showModalDialog){
                    window.showModalDialog(url,'',"dialogWidth:"+width +"px;dialogHeight:"+height+"px;center:yes;dialogleft: "+left+"px;dialogtop: "+top+"px");
                }
            }
            else if(ajaxReq.status == 400)
                alert(INVALID_REQUEST_ERROR);
            else if(ajaxReq.status==12029){
                alert(ERROR_SERVER); 
            }
            else
                alert(ERROR_DATA);
        }

        //alert(strAttribData);

        return strAttribData;

    }
}


function NGF_NotifyDataLoaded(formData){

    //alert("formData : "+formData);

    if(windowProperty.winloc == 'M')
        formBuffer=formData;
    else if(windowProperty.winloc == 'T')
        window.opener.opener.formBuffer = formData;
    else
        window.opener.formBuffer = formData;
    if(typeof isFormLoaded != 'undefined')
        isFormLoaded=true;
}

function expandTable(ref,tableId){
    if(document.getElementById(tableId).style.display=="none") {
        ref.src="../../../resources/images/minus.gif";
        document.getElementById(tableId).style.display="inline";
    }
    else{
        document.getElementById(tableId).style.display="none";
        ref.src="../../../resources/images/plus.gif";
    }
    
    initHWForm();
    if(typeof addJqueryScroll != 'undefined'){
        addJqueryScroll();
    }
}

function expandDETable(ref,tableId){
    if(document.getElementById("dataEntryForm:"+tableId).style.display=="none") {
          ref.src="../../../resources/images/minus.gif";
          document.getElementById("dataEntryForm:"+tableId).style.display="inline";
    }
    else{
           document.getElementById("dataEntryForm:"+tableId).style.display="none";
           ref.src="../../../resources/images/plus.gif";
    }
}
function formValueChange()
{
    if(windowProperty.winloc == 'M')
        saveCalled = true;
    else if(windowProperty.winloc == 'T')
        window.opener.opener.saveCalled = true;
    else
        window.opener.saveCalled = true;
}

function checkFormValueChange(ref)
{
    if(SharingMode){
        broadcastFormChangeEvent(ref);
    }
}

function toDoValueChange(toDoIndex,toDoName,broadCastFlag)
{
    var bBroadCastEvent = true;
    var window_workdesk = "";
    if(windowProperty.winloc == 'M')
                window_workdesk = window;
            else if(windowProperty.winloc == 'T')
                window_workdesk = window.opener.opener;
            else
                window_workdesk = window.opener;
    if(broadCastFlag != undefined && broadCastFlag == false)
        bBroadCastEvent = false;
    
    formValueChange();
        
    if(window_workdesk.SharingMode && bBroadCastEvent)
    {
        var objCombo = document.getElementById('wdesk:todo'+toDoIndex);
        var toDoValue = objCombo.value;
        
        window_workdesk.broadcastToDoChangeEvent(toDoIndex,toDoName,toDoValue); 
    }
}

function windowReflectTodoSaveFlag(toDoIndex,toDoValue){
    var spanToDo = document.getElementById('wdesk:value'+toDoIndex);
    spanToDo.innerHTML = toDoValue;
    spanToDo.style.visibility = "visible";
    
    formValueChange();
}

function addComplexWrapper(tableId){
    var complexParentId = tableId.split(':')[1];
    var url = "/webdesktop/ajaxworkdeskwindowsave.jsf?Action=CA&Opr=Add&ComplexParentId="+complexParentId+"&pid="+encodeURIComponent(pid)+"&wid="+wid+"&taskid="+taskid;    
    var ref = new net.ContentLoader(url, addComplexWrapperHandler, null, "POST", null, true);  
    ref.tableId = tableId;
}

function addComplexWrapperHandler(){  
    var jsonResp = eval(this.req.responseText);
    if(jsonResp[0] == 'success'){
        addComplex(this.tableId);
    }
}

function addComplex(tableId){
    var strHTML=document.getElementById(tableId+"-def").innerHTML;
    var tbody = document.getElementById(tableId);
    var tb1=document.createElement("div");
    try{
        var hideText=document.getElementById(tableId+"-value");
        var hideTextValue=hideText.value;
        while(strHTML.indexOf(tableId)!=-1)
            strHTML=strHTML.replace(tableId,SEPERATOR1);
        var strForId=tableId+"-"+hideTextValue;
        hideTextValue=parseInt(hideTextValue)+1;
        hideText.value=hideTextValue;
        while(strHTML.indexOf(SEPERATOR1)!=-1)
            strHTML=strHTML.replace(SEPERATOR1,strForId);
        tb1.innerHTML="<table>"+strHTML+"</table>";
        var row = document.createElement("TR");
        var td1 = document.createElement("TD");
        td1.className="wdaligntop";
        td1.appendChild(tb1);
        row.appendChild(td1);
        var inputId="abc";
        var td2 = document.createElement("TD");
        td2.className="wdaligntop";
        var inputEl2= document.createElement("INPUT") ;
        inputEl2.setAttribute("type","button");
        inputEl2.setAttribute("value",LABEL_DELETE_COMPLEXARRAY);
        inputEl2.className="EWButton";
        inputEl2.style.marginTop="3px"; 
        var funName="deleteComplexRowWrapper(this.parentNode.parentNode.rowIndex,'"+tableId+"')";
        inputEl2.onclick=function (){
            eval(funName);
            return false;
        }
        td2.appendChild(inputEl2);
        row.appendChild(td2);
        if(tbody.childNodes[0].nodeName=="TBODY"){
            tbody.childNodes[0].appendChild(row);
        }else{
            tbody.childNodes[1].appendChild(row);
        }
    }catch(ex){
        //alert(ex);
    }
    return false;
}

function addcSimpleChild(tableId,type,length){
    var hideText=document.getElementById(tableId+"-value");
    var hideTextValue=hideText.value;
    var inputId=tableId+"-"+hideTextValue;
    hideTextValue=parseInt(hideTextValue)+1;
    hideText.value=hideTextValue;
    try{
        var objTable = document.getElementById(tableId);
        var row = document.createElement("TR");
        var td1 = document.createElement("TD") ;
        var inputEl= document.createElement("INPUT") ;
        inputEl.setAttribute("name",inputId);
        inputEl.setAttribute("id",inputId);
        inputEl.className = "EWEnalbleTextField";        
        var validafun="textValidation(this,'"+type+"','"+length+"')";
        // inputEl.onmousedown=function (){eval(validafun);return false;}
        inputEl.onchange=function (){
            eval(validafun);
        }
        
        inputEl.onkeydown=function (){
            eval("formValueChange()");
        }
        
        td1.appendChild(inputEl);
        var td2 = document.createElement("TD");
        var inputEl2= document.createElement("INPUT") ;
        inputEl2.setAttribute("type","button");
        inputEl2.setAttribute("value",LABEL_DELETE_COMPLEXARRAY);
        inputEl2.className="EWButton";
        var funName="deleteRow(this.parentNode.parentNode.rowIndex,'"+tableId+"')";
        inputEl2.onclick=function (){
            eval(funName);
            return false;
        }
        td2.appendChild(inputEl2);
        row.appendChild(td1);
        row.appendChild(td2);
        if(objTable.childNodes[0].nodeName=="TBODY"){
            objTable.childNodes[0].appendChild(row);
        }else{
            objTable.childNodes[1].appendChild(row);
        }
    }catch(ex){
        //alert(ex);
    }
    return false;
}

function addBoolean(tableId){
    var combo_box = document.createElement('select');
    var choice = document.createElement('option');
    choice.value = ' ';
    choice.appendChild(document.createTextNode(' '));
    combo_box.appendChild(choice);
    choice = document.createElement('option');
    choice.value = 'true';
    choice.appendChild(document.createTextNode('true'));
    combo_box.appendChild(choice);
    choice = document.createElement('option');
    choice.value = 'false';
    choice.appendChild(document.createTextNode('false'));
    combo_box.appendChild(choice);
    var hideText=document.getElementById(tableId+"-value");
    var hideTextValue=hideText.value;
    var inputId=tableId+"-"+hideTextValue;
    hideTextValue=parseInt(hideTextValue)+1;
    hideText.value=hideTextValue;
    var objTable = document.getElementById(tableId);
    var row = document.createElement("TR");
    var td1 = document.createElement("TD") ;
    combo_box.className="EWEnalbleTextField";
    combo_box.setAttribute("name",inputId);
    combo_box.setAttribute("id",inputId);
        
    combo_box.onmousedown=function (){        
        eval("formValueChange()");
    } 

    combo_box.onkeydown=function (){        
        eval("formValueChange()");
    } 
    
    combo_box.onchange=function (){        
        eval("checkFormValueChange(this)");
    }  
    
    td1.appendChild(combo_box);
    var td2 = document.createElement("TD");
    var inputEl2= document.createElement("INPUT") ;
    inputEl2.setAttribute("type","button");
    inputEl2.setAttribute("value",LABEL_DELETE_COMPLEXARRAY);
    inputEl2.className="EWButton";
    var funName="deleteRow(this.parentNode.parentNode.rowIndex,'"+tableId+"')";
    inputEl2.onclick=function (){
        eval(funName);
        return false;
    }
    td2.appendChild(inputEl2);
    row.appendChild(td1);
    row.appendChild(td2);
    if(objTable.childNodes[0].nodeName=="TBODY"){
        objTable.childNodes[0].appendChild(row);
    }else{
        objTable.childNodes[1].appendChild(row);
    }
    return false;
}

function deleteRow(i,tableId){
    document.getElementById(tableId).deleteRow(i);
    var hideText=document.getElementById(tableId+"-value");
    var hideTextValue=hideText.value;
    hideTextValue=parseInt(hideTextValue)-1;
    hideText.value=hideTextValue;
    if(windowProperty.winloc == 'M')
        saveCalled = true;
    else if(windowProperty.winloc == 'T')
        window.opener.opener.saveCalled = true;
    else
        window.opener.saveCalled = true;
    
    orderElement(tableId)
    return false;
}

function deleteComplexRowWrapper(i,tableId){
    var complexParentId = tableId.split(':')[1];
    var url = "/webdesktop/ajaxworkdeskwindowsave.jsf?Action=CA&Opr=Delete&ComplexParentId="+complexParentId+"&Index="+i+"&pid="+encodeURIComponent(pid)+"&wid="+wid+"&taskid="+taskid;    
    var ref = new net.ContentLoader(url, deleteComplexRowWrapperHandler, null, "POST", null, true);  
    ref.tableId = tableId;
    ref.index = i;
}

function deleteComplexRowWrapperHandler(){  
    var jsonResp = eval(this.req.responseText);
    if(jsonResp[0] == 'success'){
        deleteComplexRow(this.index, this.tableId);
    }
}

function deleteComplexRow(i,tableId,insertionOrderId){
    var ref = document.getElementById(tableId);
    ref.rows[i].style.display = "none";
    
    var refIORef = document.getElementById(insertionOrderId);
    if(refIORef){
       var ioId = -(refIORef.value - 0);
       refIORef.value = ioId;
    }
    
    /*var hideText=document.getElementById(tableId+"-value");
    var hideTextValue=hideText.value;
    hideTextValue=parseInt(hideTextValue)-1;
    hideText.value=hideTextValue;*/
    if(windowProperty.winloc == 'M')
        saveCalled = true;
    else if(windowProperty.winloc == 'T')
        window.opener.opener.saveCalled = true;
    else
        window.opener.saveCalled = true;
    return false;
}

function textValidation(ref,type,length){
    var inputObjectValue=ref.value;
    var validationStatus='false';
    var sDateFormat=wiproperty.sDateFormat;
    var fieldLength=length;
    switch (parseInt(type)){
        case NG_VAR_INT:
            validationStatus = isInteger(inputObjectValue);
            break;
        case NG_VAR_LONG :
            validationStatus = isLong(inputObjectValue);
            break;
        case NG_VAR_FLOAT:
            validationStatus = isFloat(inputObjectValue);
            break;
        case NG_VAR_DATE:
            if (inputObjectValue != "")
            {
                validationStatus = ValidateDateFormat(inputObjectValue,sDateFormat);
                if (validationStatus == 1)
                {
                    validationStatus = "false";

                }
                else{
                    validationStatus=INVALID_FORMAT1;
                }
            }
            else
                validationStatus = "false";
            break;
        case NG_VAR_SHORTDATE:
            if (inputObjectValue != "")
            {
                validationStatus = ValidateShortDateFormat(inputObjectValue,sDateFormat);
                if (validationStatus == 1)
                    validationStatus = "false";
                else
                    validationStatus=INVALID_SHORT_DATE;
            }
            else
                validationStatus = "false";
            break;
        case NG_VAR_TIME:
            if (inputObjectValue != "")
            {
                validationStatus = ValidateTimeFormat(inputObjectValue);
                if (validationStatus == 1)
                    validationStatus = "false";
                else
                    validationStatus = INVALID_TIME_FORMAT;
            }
            else
                validationStatus = "false";
            break;
        case NG_VAR_DURATION:
            if (inputObjectValue != "")
            {
                validationStatus = validateDurationFormat(inputObjectValue);
                if (validationStatus == 1)
                    validationStatus = "false";
                else
                    validationStatus = INVALID_DURATION_FORMAT;
            }
            else
                validationStatus = "false";
            break;
        case NG_VAR_BOOLEAN:
            validationStatus = "false";
            break;
        case NG_VAR_STRING :
            validationStatus = isString(inputObjectValue,fieldLength);
            break;
    }
    if(validationStatus=="false"){

    // document.getElementById("wdesk:"+textId).focus();
    //return;
    }else{
        var formWindow=getWindowHandler(windowList,"formGrid");
        // setmessageinDiv(validationStatus,"true",1000);
        alert(validationStatus);
        ref.value="";
        formWindow.focus();
        ref.select();
    }
}


function getInStatus(winLoc){

    var extInt=new Array();
    extInt.append("wdesk:formGrid");
    extInt.append("wdesk:tableGrid");
    extInt.append("wdesk:todoGrid");
    extInt.append("wdesk:exceptionGrid");
    extInt.append("wdesk:taskGrid");//Added for Task
    var i;

    for(i=0;i<extInt.length;i++){
        var win1;
        var extInt1=window.document.getElementById(extInt[i]);
        if(extInt1){
            var ind=extInt[i].indexOf(':');
            addWindowToParent(windowList,extInt[i].substring(ind+1));
        }

    }

    if(interFace0 == 'form' ||  interFace1 == 'form')
    {
        if(wiproperty.formType=="NGFORM")
        {
            if(ngformproperty.type=="applet")
            {
                //detectJRE();
            }
        }
    }

    if(interFace2 == 'doc' ||  interFace3 == 'doc')
    {
        docLoaded = false;
    }

    if(interFace2 == 'form' ||  interFace3 == 'form')
    {
        formLoaded = false;
    }

    if(wiproperty.formType=="NGFORM" && ngformproperty.type=="applet" && (interFace0 =='form'|| interFace1 =='form')){

    // isFormLoaded=false;
    }else
    {
        isFormLoaded=true;
    }

}

function fetchFragmentData(fieldName){
        var requestString='pid='+encode_utf8(pid)+'&wid='+wid+'&taskid='+taskid+'&WD_UID='+WD_UID;
        var strFragmentData="";
        requestString=requestString+"&IsFragment=Y&FieldName="+fieldName;
        var ajaxReq;
        if (window.XMLHttpRequest) {
        ajaxReq= new XMLHttpRequest();
        } else if (window.ActiveXObject) {
        ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
        }
        var url = "../ajaxgetAttribute.jsf";
        url = appendUrlSession(url);
        ajaxReq.open("POST", url, false);
        ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        ajaxReq.send(requestString);
        if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
            strFragmentData=ajaxReq.responseText;
        }
        else
            {
                if(ajaxReq.status==598)
                    {  //setmessageinDiv(ajaxReq.responseText,"false",3000);
                        alert(ajaxReq.responseText);
                    }
                else if(ajaxReq.status == 599){
                      //window.open(sContextPath+"/login/logout.jsp?"+"error=4020",reqSession);
                    url = sContextPath+"/error/errorpage.jsf?msgID=4020";
                    url = appendUrlSession(url);
                    var width = 320;
                    var height = 160;
                    var left = (window.screen.availWidth-width)/2;
                    var top = (window.screen.availHeight-height)/2;

                    //window.open(url,reqSession);
                    if (window.showModalDialog){
                        window.showModalDialog(url,'',"dialogWidth:"+width +"px;dialogHeight:"+height+"px;center:yes;dialogleft: "+left+"px;dialogtop: "+top+"px");
                    }
                }
                else if(ajaxReq.status == 400)
                    alert(INVALID_REQUEST_ERROR);
                else if(ajaxReq.status==12029){
                    alert(ERROR_SERVER); 
                }
                else
                      alert(ERROR_DATA);
            }
        return strFragmentData;

}

function reloadapplet(docIndex,broadCastFlag,fromOp){
    var bBroadCastEvent = true;

    if(broadCastFlag != undefined && broadCastFlag == false)
        bBroadCastEvent = false;
    var isChrome = (typeof window.chrome != 'undefined')? true: false;
    var isIEBrowser = (navigator.appName=='Netscape') && (window.navigator.userAgent.indexOf('Trident/') < 0) && (!isChrome)? false: true;
    var isFirefox = navigator.userAgent.toLowerCase().indexOf("firefox") > 0;
    
    var cHeight = document.getElementById('docDiv').parentNode.style.height.split('p')[0];
    var cWidth = document.getElementById('docDiv').parentNode.style.width.split('p')[0];
    var isCase = false;
    if(cWidth == '' && cHeight == ''){// Handling for CaseView/TaskView
        isCase = true;
        cWidth = document.getElementById('docDiv').parentNode.parentNode.style.width.split('p')[0];
        cHeight = document.getElementById('docDiv').parentNode.parentNode.style.height.split('p')[0];
    }
    //document.getElementById('docviewer').style.height = cHeight - 50 + "px";

    //changes by Gaurav Asthana 19/10/2011 bug id 27260
    isSaveAnnotation();
    if(docIndex<0){
        document.getElementById('wdesk:ivapp').style.display='none';
        document.getElementById('wdesk:ifrm').style.display='none';
        if(document.getElementById('wdesk:docOperation'))
            document.getElementById('wdesk:docOperation').style.display='none';
        if(document.getElementById('wdesk:noDocSelected'))
            document.getElementById('wdesk:noDocSelected').style.display='inline';

        /*var noDocDiv=document.getElementById('wdesk:noDocSelected');
        if(noDocDiv!=null){
            noDocDiv.style.display = "none";
        }*/
        var isDefaultDoc = document.getElementById('IsDefaultDoc').value;
        if(document.getElementById('wdesk:docCombo')){        
            if(document.getElementById('wdesk:docCombo').value==-1){
                if(isDefaultDoc == "N"){
                    if(document.getElementById('docOptionsDiv') != null){
                        if(navigator.appName=='Netscape'){
                            document.getElementById('docOptionsDiv').style.display = "block";
                        } else {
                            document.getElementById('docOptionsDiv').style.display = "inline";
                        }
                    }
                }
            }
        }
        return false;
    } else{
        if(isIEBrowser){
            document.getElementById('wdesk:ivapp').style.display='inline';
        } else {
            document.getElementById('wdesk:ivapp').style.display='block';            
        }         
        document.getElementById('wdesk:ifrm').style.display='inline';
        if(document.getElementById('wdesk:docOperation'))
            document.getElementById('wdesk:docOperation').style.display='inline';
        if(document.getElementById('wdesk:noDocSelected'))
            document.getElementById('wdesk:noDocSelected').style.display='none';
    }
    //changes end here

    var tmpParsedDocJSON = eval("("+document.getElementById('wdesk:docInfoJSON').value+")");
    var bDocInfoJSONFound = false;
    var docInfoJSON = tmpParsedDocJSON[docIndex];
    var res;
    var appletHtm;
    if(typeof docInfoJSON != 'undefined'){
        bDocInfoJSONFound = true;
    }

    fromOp = (typeof fromOp == 'undefined'? '': fromOp);

    if(bDocInfoJSONFound && fromOp != 'checkin' && fromOp != 'getdocument' && !(fromOp == 'new' || fromOp == 'newversion'|| fromOp == 'openNewDoc' )){ //Bug 72131 
        res = docInfoJSON;
        appletHtm = res.htmlJSON;
    } else {
        var getApplet=document.IVApplet?"N":"Y";
        if(typeof strRemoteIp!='undefined' && strRemoteIp!="")
            getApplet="Y";
        var requestString='docindex='+docIndex+'&getApplet='+getApplet+'&pid='+encode_utf8(pid)+'&wid='+wid+'&taskid='+taskid+'&WD_UID='+WD_UID;
        if (window.XMLHttpRequest) {
            ajaxReq= new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
        }

        var url = "../ajaxgetdocument.jsf";
        url = appendUrlSession(url);
        ajaxReq.open("POST", url, false);
        ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        ajaxReq.send(requestString);
        if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
            docInfoJSON =  eval("("+ajaxReq.responseText+")");
            res = docInfoJSON;
            appletHtm = res.htmlJSON;
            bDocInfoJSONFound = true;
            tmpParsedDocJSON[docIndex] = docInfoJSON;
            document.getElementById('wdesk:docInfoJSON').value = JSON.stringify(tmpParsedDocJSON).toString();
        }
        else{
            if(ajaxReq.status == 400)
                alert(INVALID_REQUEST_ERROR);
            else if(ajaxReq.status==12029){
                alert(ERROR_SERVER);
            }
            else
                alert(ERROR_DATA);
        }
    }
    if(bDocInfoJSONFound){
        var appletCom=document.getElementById('wdesk:ivapp');
        var iframeCom=document.getElementById('wdesk:ifrm');
        var DocListDiv=document.getElementById('wdesk:docList');
        var DownloadDiv=document.getElementById('wdesk:downloadDiv');
        var VersionDiv=document.getElementById('wdesk:versionDiv');
        var CheckoutDiv=document.getElementById('wdesk:checkoutDiv');
        var docListDiv1=document.getElementById('wdesk:docList1');
        var CheckinDiv=document.getElementById('wdesk:checkinDiv');
        var saveTransDiv=document.getElementById('wdesk:saveTransformationDiv');
        var noDocDiv=document.getElementById('wdesk:noDocDiv');
        //var zoomCombo =document.getElementById('wdesk:zoomCom');
        var showImg =document.getElementById('wdesk:show');
        var showReadImg =  document.getElementById('wdesk:showRead');
        var showDefaultImg =document.getElementById('wdesk:defaultImg');
        var showNonDefaultImg =document.getElementById('wdesk:nonDefaultImg');
        DocListDiv.style.display="none";

        var printDiv;
        var browserName=navigator.appName;
        printDiv=document.getElementById('wdesk:PrintDiv');

        var editDiv = document.getElementById('wdesk:editDocDiv');

        var NoOfPages = res.document[0].NoOfPages - 0;
        var PageToDisplay =res.document[0].PageToDisplay-0;

        var docType=res.document[0].DocType;
        var docExt=res.document[0].DocExt;
        var documentName=res.document[0].DocName;
        //var imageIndex=res.document[0].ImgIndex;
        //var volumeId=res.document[0].VolIndex; 
        var imageAndVolIndex=res.document[0].ISIndex.split("%23");
        var imageIndex = imageAndVolIndex[0];
        var volumeId=imageAndVolIndex[1];
        var actionPanelProp=res.actionPanel;
        var styleApp=appletCom.style;
        var styleIfrm=iframeCom.style;
        //DocListDiv.style.display="inline";
        var objCombo = document.getElementById('wdesk:docCombo');
        document.getElementById('wdesk:noOfpages').value=NoOfPages;

        //DocNameDiv.innerHTML='<span>'+res.actionPanel.displayName+'</span>';
        if(document.getElementById("wdesk:deletedoc"))
            if(res.actionPanel.deletedoc=="1")
                document.getElementById("wdesk:deletedoc").style.display="inline";
            else
                document.getElementById("wdesk:deletedoc").style.display="none";
        if(res.actionPanel.defaultcheck=="1"){
            showDefaultImg.style.display="inline";
            objCombo.options[objCombo.options.selectedIndex].style.color = "green";
            showNonDefaultImg.style.display="none";
        }
        else{
            showDefaultImg.style.display="none";
            showNonDefaultImg.style.display="inline";
        }
        if(res.actionPanel.checkout=="1"){
            CheckoutDiv.style.display="inline";
        }
        else{
            CheckoutDiv.style.display="none";
        }
        if(res.actionPanel.checkin=="1"){
            CheckinDiv.style.display="inline";
        }
        else{
            CheckinDiv.style.display="none";
        }
        if(res.actionPanel.download=="1"){
            DownloadDiv.style.display="inline";
            if (browserName!="Netscape")
                if(printDiv !=null)
                    printDiv.style.display="inline";
            if(document.IVApplet){
                try{
                    document.IVApplet.enableOrDisablePrinting(true);
                } catch(e){}
            }
        }
        else{
            DownloadDiv.style.display="none";
            if(printDiv !=null)
                printDiv.style.display="none";                
            if(document.IVApplet){
                try{
                    document.IVApplet.enableOrDisablePrinting(false);
                } catch(e){}
            }
        }
        if(res.actionPanel.savetrans=="1" && saveTransFor==true && showStyleSaveTransformation=="Y" )
            saveTransDiv.style.display="inline";
        else
            saveTransDiv.style.display="none";

        if(res.actionPanel.tool=="1" && saveTransFor==true && showStyleToolBarOption=="Y")
            showImg.style.display="inline";
        else
            showImg.style.display="none";

        if(res.actionPanel.toolRead=="1" && showStyleToolBarOption=="Y")
            showReadImg.style.display="inline";
        else
            showReadImg.style.display="none";

        if(res.actionPanel.version=="1"){
            VersionDiv.style.display="inline";
        }
        else{
            VersionDiv.style.display="none";
        }
        //Bug 65154 starts
        if (typeof showVersionOption != "undefined"  && showVersionOption !="" && showVersionOption =="N")
        {
           VersionDiv.style.display="none"; 
        }
        if (typeof showStyleDownload != "undefined"  && showStyleDownload !="" && showStyleDownload =="N")
        {
           DownloadDiv.style.display="none"; 
        }
        if (typeof showSetAsDefault != "undefined"  && showSetAsDefault !="" && showSetAsDefault =="N")
        {
           showNonDefaultImg.style.display="none";
        }
        if (typeof showStyleDocumentList != "undefined"  && showStyleDocumentList !="" && showStyleDocumentList =="N")
        {
           docListDiv1.style.display="none";
        }
        if (typeof showPrintDoc != "undefined"  && showPrintDoc !="" && showPrintDoc =="Y")
        {
            printDiv.style.display="inline";
        }
        //Bug 65154 ends


        if(noDocDiv)
            noDocDiv.style.display="none";
        //changes by Gaurav Asthana 19/10/2011 bug id 27260
        if(document.getElementById('wdesk:noDocSelected'))
            document.getElementById('wdesk:noDocSelected').style.display='none';
        if(document.getElementById('wdesk:docOperation'))
            document.getElementById('wdesk:docOperation').style.display='inline';
        //changes end here        
        
         var supportedMimeType=true; 
         if(typeof res.document[0].SupportedMimeType != 'undefined'){
             var mimetype = res.document[0].SupportedMimeType;
             if(mimetype=="N"){
                 //Bug 78222
                 supportedMimeType = false;
             }
         }
        
        var bCustomDocumentExtFound = false;
        var customDocumentExt_array = customDocumentExt.split(',');
        for (var i = 0; i < customDocumentExt_array.length; i++) {
            customDocumentExt_array[i] = customDocumentExt_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
            if (docExt.toUpperCase() == customDocumentExt_array[i].toUpperCase()) {
                bCustomDocumentExtFound = true;
                break;
            }
        }
        
        if(docExt=="png"){
            docType="N";
        }
        if(((docType=="I" && pdfOpallViewer=="Y" && docExt=="pdf") || (docType=="I")) && supportedMimeType  && (!bCustomDocumentExtFound)){
            isOnchangeDoc="onchange";
            var pnrStatus = 'Y';
            if(typeof bSaveAnnotationDialog != 'undefined' && bSaveAnnotationDialog=='N'){
                pnrStatus = 'N';
            }
            var annotUrl = res.document[0].servletPath +'/viewimageannotation?DocId='+docIndex+'&PageNo=1';
            var writeUrl = res.document[0].servletPath +'/imageannotation/DocId='+docIndex+'&pid='+encode_utf8(pid)+'&wid='+wid+'&Option=SaveAnnot&PageNo=1'+'&PnrStatus='+pnrStatus+strSessionStr;
            if(taskid!=''){
                writeUrl += '&taskid='+taskid;
            }
            var stampUrl = res.document[0].servletPath +'/viewimagestamp'+strSessionStr+'?DocId='+docIndex+'&PageNo=1';
            //styleApp.display="inline";
            if(isOpAll=='N') {
                if(isIEBrowser){
                    styleApp.display="inline";
                } else {
                    styleApp.display="block";
                }
                styleIfrm.display="none";
                if((document.IVApplet && typeof strRemoteIp=='undefined') ||(document.IVApplet && typeof strRemoteIp!='undefined' && strRemoteIp=="")){
                    //var annotUrl = res.document[0].servletPath +'/viewimageannotation'+strSessionStr+'?DocId='+docIndex+'&PageNo=1';
                    //var writeUrl = res.document[0].servletPath +'/imageannotation/DocId='+docIndex+'&pid='+encode_utf8(pid)+'&wid='+wid+'&Option=SaveAnnot&PageNo=1'+strSessionStr;
                    //var stampUrl = res.document[0].servletPath +'/viewimagestamp'+strSessionStr+'?DocId='+docIndex+'&PageNo=1';

                    /*if(document.IVApplet.height != (formH-40)){
                        document.IVApplet.height=(formH-40)+'px';
                    }*/
                    //if(isAllPages=='Y')
                    // document.IVApplet.setDocumentIndex(decode_utf8(res.document[0].ISIndex));
                    if(viewAnnotation == "N"){
                        annotUrl = "";
                        writeUrl = "";
                        stampUrl = "";
                    }else{
                        try{
                            document.IVApplet.setNextWriteAnnotURL(writeUrl);
                            document.IVApplet.setNextImageStampsURL(stampUrl);
                        } catch(e){}
                    }

                    try{
                        document.IVApplet.openNewImage(res.document[0].docUrl,annotUrl,NoOfPages,PageToDisplay,true);
                    } catch(e){}

                }
                else{
                    appletCom.innerHTML=appletHtm;
                    if((isEmbd == 'Y') && (wdView == "em")){
                        document.IVApplet.height=(formH-25)+'px';
                    } else {
                        if(typeof wDeskLayout != 'undefined'){
                            if(wDeskLayout.WDeskType == 'E'){
                                if(isCase)// Handling for CaseView/TaskView
                                    document.IVApplet.height = (cHeight - 75)+"px";
                                else{
                                    if(isFirefox){
                                        document.IVApplet.height = (cHeight - 54)+"px";
                                    } else {
                                        document.IVApplet.height = (cHeight - 50)+"px";
                                    }
                                }
                            } else {
                                document.IVApplet.height = (cHeight - 28)+"px";
                            }
                        }
                    }
                }
                //document.IVApplet.showNHideToolBars(false,false);
                var obj = document.IVApplet;
                try{
                    resizePercentage = obj.getZoom();
                }catch(e){}
                //document.getElementById('wdesk:zoomCom').value=resizePercentage;
                //document.getElementById('wdesk:zoomCom').style.display="inline";
                document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/show_toolbar.png";
                document.getElementById('wdesk:hide').title=SHOW_TOOLBAR;
                document.getElementById('wdesk:hideRead').src=sContextPath+"/resources/images/show_toolbar.png";
                document.getElementById('wdesk:hideRead').title=SHOW_TOOLBAR;
                showHide='Y';
                
                
                DocListDiv.style.display="inline";
            }
            else
            {
                var documentHeight="";
                if(typeof docAndFormAppletHeight != 'undefined'){
                    documentHeight=getVal(docAndFormAppletHeight(strprocessname,stractivityName),"FormHeight");
                }
                if(documentHeight==""){
                    if((isEmbd == 'Y') && (wdView == "em")){
                        documentHeight=(formH-25);
                    } else {
                        if(typeof wDeskLayout != 'undefined'){
                            if(wDeskLayout.WDeskType == 'E'){
                                if(isCase)// Handling for CaseView/TaskView
                                    documentHeight=(cHeight - 75);
                                else{
                                    if(isFirefox){
                                        documentHeight=(cHeight - 54);
                                    } else {
                                        documentHeight=(cHeight - 50);
                                    }
                                }
                            } else {
                                documentHeight=(cHeight - 28);
                            }
                        } else {
                            documentHeight=(formH-30);
                        }
                    }
                }
                styleApp.display="none";
                if(isIEBrowser){
                    styleIfrm.display="inline";
                } else {
                    styleIfrm.display="block";
                }
                iframeCom.style.height = documentHeight+'px';
                iframeCom.innerHTML="<IFRAME id='docviewer' style='width:99%' frameborder='0' height='"+(documentHeight-4)+"px' />"
                var ifrm2=document.getElementById('docviewer');
                var stampINIPath=res.document[0].servletPath+"/readStampINI?WD_UID="+WD_UID;
                
            
                opall.PARAM.NumberOfPages = NoOfPages;
                opall.PARAM.StampServletURL = res.document[0].servletPath+"/stampServlet?stampName=";
                opall.PARAM.num_VisiblePage = 1;
                opall.PARAM.MenuBar = "true";
                opall.PARAM.printApproach = opallPrintApproach;
                opall.PARAM.ShowUsernamesWithAllAnnotation = true;
                opall.PARAM.ServerSupportMultiPage=false;//Bug 66397
                opall.PARAM.pageSetupIE = pageSetupIE;
                opall.PARAM.StampWithoutINI = stampWithoutINI;
                
                if ( typeof resizeOption != "undefined" ) {
                    if(resizeOption =='0') {        
                        opall.PARAM.ResizeOption = resizeOption-0;
                        if (typeof resizePercentage != "undefined" ){
                            match = document.cookie.match(new RegExp("ResizePercentage" + '=([^;]+)'));
							if(match){
							opall.PARAM.ResizePercentage = match[1] - 0;
							}
							else{
								opall.PARAM.ResizePercentage = resizePercentage - 0;
							}
					}
                    } else { 
                        opall.PARAM.ResizeOption = resizeOption-0;   
                    }
                }
            
                if (typeof userName != "undefined" )
                    opall.PARAM.CurrentUserName = userName;
            
                if(typeof window.strPrintOption != 'undefined')
                {
                    if (typeof printOption != "undefined" &&   printOption=="N" && typeof window.strPrintOption != "undefined"  &&    window.strPrintOption=="1"){
                        opall.PARAM.printOption="false";
                    } else {
                        opall.PARAM.printOption='true';
                    }
                } else if(typeof window.opener.strPrintOption != 'undefined')
{
                    if (typeof printOption != "undefined" &&   printOption=="N" && typeof window.opener.strPrintOption != "undefined"  &&    window.opener.strPrintOption=="1"){
                        opall.PARAM.printOption="false";
                    } else {
                        opall.PARAM.printOption='true';
                    }   
                }
                if (typeof  annotationColor != "undefined"  && annotationColor != "" && annotationColor.length > 0 )
                    opall.PARAM.AnnotationColor= annotationColor;
            
                if (typeof isZoomWindowChangeRequired != "undefined" && isZoomWindowChangeRequired != "" && isZoomWindowChangeRequired.length > 0 )
                    opall.PARAM.IsZoomWindowChangeRequired= isZoomWindowChangeRequired;
            
                if (typeof printParameter != "undefined" )
                    opall.PARAM.PrintParameter = printParameter;
            
                if (typeof watermarkPrinting != "undefined" )
                    opall.PARAM.watermarkPrinting = watermarkPrinting;
                
                if (typeof watermarkPosition != "undefined" )
                    opall.PARAM.TextWaterMarkPosition = watermarkPosition;
        
                if (typeof watermarkProp != "undefined" )
                    opall.PARAM.Watermark_Properties = watermarkProp;

                if(typeof getWaterMarkText != "undefined" )    
                    opall.PARAM.TextAsWaterMark = Trim(getWaterMarkText(strprocessname,stractivityName,pid));

             //   if ( typeof initialZoomLensPercentage != "undefined" && initialZoomLensPercentage.length > 0) {
             //       opall.PARAM.InitialZoomLensPercentage= initialZoomLensPercentage;
              //  } else {
              //      if (typeof zoomLens != "undefined" && zoomLens == "1") {
               // Bug 78052
                if (typeof initialZoomLensStatus != "undefined" && initialZoomLensStatus == "Y") {
                    opall.PARAM.InitialZoomLensStatus = 'true';
                    opall.PARAM.InitialZoomLensPercentage = initialZoomLensPercentage;
                } else {
                    opall.PARAM.InitialZoomLensStatus = 'false';
                    opall.PARAM.InitialZoomLensPercentage = initialZoomLensPercentage;
                }
             //       } else if (typeof zoomLens != "undefined"  && zoomLens == "0") {
              //          opall.PARAM.InitialZoomLensStatus='false';
              //      }
            //    }
                if (typeof bSaveAnnotationDialog != "undefined" && bSaveAnnotationDialog == "N")
                    opall.PARAM.bSaveAnnotationDialog = false;
                
                if (typeof strZoomLens != "undefined" && strZoomLens=="Y") {           
                    var zoomTokens = zoomLensAttributes.split(",");
                    var strLensWidth =  zoomTokens[0];
                    var strLensHeight =  zoomTokens[1];
                    var strLensLoc =  zoomTokens[2];
                    var isLensReqAtZoning =  zoomTokens[3];
                    opall.PARAM.ZoomLensForZoning = strLensWidth+","+strLensHeight+","+strLensLoc+","+isLensReqAtZoning;
                }
            
                if (typeof strAnnotationFont != "undefined"  && strAnnotationFont !="")
                    opall.PARAM.DefaultFontSettings = strAnnotationFont;
            
                if (typeof croppedImageMinQuality != "undefined"  && croppedImageMinQuality !="")
                    opall.PARAM.cropImageMinQuality = croppedImageMinQuality;
        
                if (typeof CroppedImageSize != "undefined"  && CroppedImageSize !="")
                    opall.PARAM.CroppedImageSize = CroppedImageSize-0;
        
                if (typeof transformOption != "undefined"  && transformOption !="")
                    opall.PARAM.TransformOption = transformOption;
        
                if (typeof annotationOption != "undefined"  && annotationOption !="")
                    opall.PARAM.AnnotationOption = annotationOption;
                if(typeof wiViewMode != "undefined" && wiViewMode=="R")
                    opall.PARAM.AnnotationOption = 1;
                opall.PARAM.url_ImageFileName = res.document[0].docUrl;
           
                if (typeof viewAnnotation != "undefined"  && viewAnnotation !="" && viewAnnotation=="Y") {
                    opall.PARAM.URL_Annotation = annotUrl;
                    opall.PARAM.url_WriteAnnotation = writeUrl;
                }
            
                if (typeof strStampServletCall != "undefined"  && strStampServletCall !="" && strStampServletCall=="Y") {
                    opall.PARAM.URL_ImageStampFile = stampUrl;
                    opall.PARAM.URL_StampINIPath = stampINIPath;
                }
                if(typeof isAllPages!='undefined' && isAllPages=='Y')
                opall.PARAM.ServerSupportMultiPage=false;
                else 
                opall.PARAM.ServerSupportMultiPage=true;
            
                opall.PARAM.AnnotationDisplay="true";
                opall.PARAM.ViewerWidth=cWidth;
                opall.PARAM.ViewerHeight=(documentHeight-80)+'px';
                opall.PARAM.localeDirection = pageDirection;
                isOnchangeDoc="onchange";
                //opall.PARAM.DynamicHideNShowToolBar=2;
                opall.PARAM.RetainViewSettings=true;
                
                if(typeof window.strPrintOption != 'undefined') {
                    if(window.strPrintOption == '1') {
                        opall.PARAM.printOption=false;
                    } else if(window.strPrintOption == '2') {
                        if(document.getElementById('wdesk:downloadDiv').style.display != 'undefined') {
                            if(document.getElementById('wdesk:downloadDiv').style.display == 'none') {
                                opall.PARAM.printOption=false;
                            }
                        }
                    }
                } else if(typeof window.opener.strPrintOption != 'undefined') {		
                    if(window.opener.strPrintOption == '1') {
                        opall.PARAM.printOption=false;
                    } else if(window.opener.strPrintOption == '2') {
                        if(document.getElementById('wdesk:downloadDiv').style.display != 'undefined') {
                            if(document.getElementById('wdesk:downloadDiv').style.display == 'none') {
                                opall.PARAM.printOption=false;
                            }
                        }
                    }
                }

                var num = 1;
                DocListDiv.style.display="inline";
              opall.showViewer(ifrm2,"/OpAll/OpAll/HTML/OpAll.jsp?var="+num+"&WD_UID="+WD_UID+'&rid='+MakeUniqueNumber());     
            }
        }
        else{
            var documentHeight="";
            if(typeof docAndFormAppletHeight != 'undefined'){
                documentHeight=getVal(docAndFormAppletHeight(strprocessname,stractivityName),"FormHeight");
            }
            if(documentHeight==""){
                if((isEmbd == 'Y') && (wdView == "em")){
                    documentHeight=(formH-25)+'px';
                } else {
                    if(typeof wDeskLayout != 'undefined'){
                        if(wDeskLayout.WDeskType == 'E'){
                            if(isCase)// Handling for CaseView/TaskView
                                documentHeight=(cHeight - 75)+'px';
                            else
                                documentHeight=(cHeight - 50)+'px';
                        } else {
                            documentHeight=(cHeight - 28)+'px';
                        }
                    } else {
                        documentHeight=(formH-30)+'px'
                    }
                }
            }
            styleApp.display="none";
            styleIfrm.display="inline";
            iframeCom.style.height=documentHeight;

            if (browserName=="Netscape")
            {
                if(printDiv != null){
                    printDiv.style.display="none";
                }
            }
            else
            {
                if(printDiv != null){
                    printDiv.style.display="inline";
                }
            }

            //document.getElementById('wdesk:zoomCom').style.display="none";
            var url=res.document[0].docUrl;

            // Bug Id: 28745

            if(cHeight==""){
                if((isEmbd == 'Y') && (wdView == "em")){
                    cHeight = formH - 0 + 27;
                }
            }
            
            
            if(docExt != "txt"){
                if((isEmbd == 'Y') && (wdView == "em")){
                //$("#contentDiv").mCustomScrollbar("destroy");
                }
                  
                if (bCustomDocumentExtFound) {
                    url = "/webdesktop/custom/viewer/viewer.jsp?docExt=" + docExt + "&documentName=" + documentName + "&imageId=" + imageIndex + "&volIndex=" + volumeId + "&siteId=" + siteId + "&CabinetName=" + cabName + "&userDBid=" + userIndx + "&JtsAddress=" + jtsIP + "&portId=" + jtsPort;
                }
                
                document.getElementById('wdesk:docFrameURL').value = url;    
                
                
                
                iframeCom.innerHTML="<IFRAME id='docviewer' frameborder='0' style='width:100%' height='"+documentHeight+"' SRC='about:blank'"+"/>";
                var docviewerObj = document.getElementById('docviewer'); 
                if( docviewerObj != null){
                    if(typeof isChromeOfficeViewer != 'undefined' && isChromeOfficeViewer()) {
                        docviewerObj.src = url;
                    } else {
                        var ifrm = docviewerObj;
                        IframeRequestWithPost(url,ifrm);
                    }
                    docviewerObj.style.display = "block";
                }
                
                DocListDiv.style.display="inline";                                
            }
            else {                
                var pageDir = (pageDirection == 'ltr')? 'left': 'right';   
                document.getElementById('wdesk:docFrameURL').value = url;
                iframeCom.innerHTML="<div id='docviewerDiv' style='overflow: auto; text-align:"+pageDir+"'></div>";

                var calledFromWin = (typeof calledFromWindow == 'undefined')? false: calledFromWindow;   
                if(calledFromWin){
                    var contentLoaderRef = new net.ContentLoader(url, conversationDocWinHandler, null, "POST",null, true);                        
                } else {
                    var contentLoaderRef = new net.ContentLoader(url, conversationDocHandler, null, "POST",null, true);                        
                }
            }
                
            if(typeof setFormFocus!='undefined')
                setFormFocus();
        }        
           
        if(typeof window.strPrintOption != 'undefined')
        {

            if(window.strPrintOption == '1')
            {
                if(document.getElementById('wdesk:downloadDiv'))
                    document.getElementById('wdesk:downloadDiv').style.display='none';
                if(document.getElementById('wdesk:PrintDiv'))
                    document.getElementById('wdesk:PrintDiv').style.display='none';
                if(document.IVApplet)
                    document.IVApplet.enableOrDisablePrinting(false);

            }
            else if(window.strPrintOption == '2')
            {
                if(typeof window.isEnableDownloadPrint != 'undefined' && window.isEnableDownloadPrint(strprocessname,stractivityName,userName))
                {
                    if(document.getElementById('wdesk:downloadDiv'))
                        document.getElementById('wdesk:downloadDiv').style.display='inline';
                    if(document.getElementById('wdesk:PrintDiv'))
                        document.getElementById('wdesk:PrintDiv').style.display='inline';
                    if(document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(true);
                }

            }

        }
    
        else if(typeof window.opener.strPrintOption != 'undefined')
        {

            if(window.opener.strPrintOption == '1')
            {
                if(document.getElementById('wdesk:downloadDiv'))
                    document.getElementById('wdesk:downloadDiv').style.display='none';
                if(document.getElementById('wdesk:PrintDiv'))
                    document.getElementById('wdesk:PrintDiv').style.display='none';
                if(isOpAll=='N' &&  document.IVApplet)
                    document.IVApplet.enableOrDisablePrinting(false);
                else 
                    opall_toolkit.enableOrDisablePrinting(false);

            }
            else if(window.opener.strPrintOption == '2')
            {
                if(typeof window.opener.isEnableDownloadPrint != 'undefined' && window.opener.isEnableDownloadPrint(window.opener.strprocessname,window.opener.stractivityName,window.opener.userName))
                {
                    if(document.getElementById('wdesk:downloadDiv'))
                        document.getElementById('wdesk:downloadDiv').style.display='inline';
                    if(document.getElementById('wdesk:PrintDiv'))
                        document.getElementById('wdesk:PrintDiv').style.display='inline';
                    if(isOpAll=='N' &&  document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(true);
                    else 
                        opall_toolkit.enableOrDisablePrinting(true);
                }

            }
        }
    }
    
    ChangeColorOnComboSelect(docIndex); //bug id 32178
    
    if(typeof isDocOrgName != 'undefined' && !isDocOrgName(strprocessname,stractivityName))
        displayDocTypeOnly(docIndex);
    else if(window.opener && typeof window.opener.isDocOrgName != 'undefined' && !window.opener.isDocOrgName(window.opener.strprocessname,window.opener.stractivityName))
        window.opener.displayDocTypeOnly(docIndex);

    if(broadCastFlag != undefined && broadCastFlag == false){
        var objCombo = document.getElementById('wdesk:docCombo');
        for(var i=0;i<objCombo.options.length;i++){
            if(objCombo.options[i].value == docIndex){
                objCombo.options.selectedIndex = i;
            }
        }
    }
    
    if (typeof documentPostHook != 'undefined') {
        var objComboBox = document.getElementById('wdesk:docCombo');
        var docName;
        for (var i = 0; i < objComboBox.options.length; i++) {
            if (objComboBox.options[i].value == docIndex) {
                docName = objComboBox.options[i].text;
                break;
            }
        }
        documentPostHook(docIndex, fromOp, docName, "");
    }
    
    var window_workdesk="";
    if(windowProperty.winloc == 'M')
        window_workdesk = window;
    else if(windowProperty.winloc == 'T')
        window_workdesk = window.opener.opener;
    else
        window_workdesk = window.opener;  
    if(window_workdesk.SharingMode && bBroadCastEvent)
    {
        var objComboBox = document.getElementById('wdesk:docCombo');
        var docName;
        for(var i=0;i<objComboBox.options.length;i++){
            if(objComboBox.options[i].value == docIndex){
                docName = objComboBox.options[i].text;
            }
        }
        window_workdesk.broadcastDocChangeEvent(docIndex,docName); 
    }
    if(document.getElementById('docOptionsDiv') != null){
        document.getElementById('docOptionsDiv').style.display="none";
    }
}

function IsImageDisplayed(PageNo){
        if(isOnchangeDoc=="onchange"){
        if(defalutShowToolbar=='Y'){
            var annotationRight = document.getElementById('wdesk:annotationRight').value;
            if(annotationRight=='0' || (checkAnnotRights=="Y" && document.getElementById('wdesk:checkoutDiv').style.display=="none")) {
                opall_toolkit.showNHideToolBars(true,false);
            }
            else{
                opall_toolkit.showNHideToolBars(true,true);
            }
            document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/hide_toolbar.png";
            document.getElementById('wdesk:hide').title=HIDE_TOOLBAR;
            showHide='N';
        }else{
            opall_toolkit.showNHideToolBars(false,false);
            document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/show_toolbar.png";
            document.getElementById('wdesk:hide').title=SHOW_TOOLBAR;
            showHide='Y';
        }
        isOnchangeDoc="";
        if(!isAnnotationToolbar(strprocessname,stractivityName) && showHide=='N'){
                    opall_toolkit.showNHideToolBars(true,false);
            }
    }else if(isOnchangeDoc=="onload"){
        if(defalutShowToolbar=='Y'){
            var annotationRight = document.getElementById('wdesk:annotationRight').value;
            if(annotationRight=='0' || (checkAnnotRights=="Y" && document.getElementById('wdesk:checkoutDiv').style.display=="none")) {
                opall_toolkit.showNHideToolBars(true,false);
            }
            else{
                opall_toolkit.showNHideToolBars(true,true);
            }

            document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/hide_toolbar.png";
            document.getElementById('wdesk:hide').title=HIDE_TOOLBAR;
            showHide='N';
        }else{
            opall_toolkit.showNHideToolBars(false,false);
            document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/show_toolbar.png";
            document.getElementById('wdesk:hide').title=SHOW_TOOLBAR;
            showHide='Y';
        }
        isOnchangeDoc="";
        if(!isAnnotationToolbar(strprocessname,stractivityName) && showHide=='N'){
                    opall_toolkit.showNHideToolBars(true,false);
            }
    }
    
    if (typeof wiViewMode != 'undefined' && wiViewMode == 'R') {
        opall_toolkit.showNHideToolBars(true, false);
    }
    if(typeof window.strPrintOption != 'undefined') {
        if(window.strPrintOption == '1') {
            if(isOpAll=='N' && document.IVApplet)
                document.IVApplet.enableOrDisablePrinting(false);
            else 
                opall_toolkit.enableOrDisablePrinting(false);
        } else if(window.strPrintOption == '2') {
            if(document.getElementById('wdesk:downloadDiv').style.display != 'undefined') {
                if(document.getElementById('wdesk:downloadDiv').style.display == 'none') {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(false);
                    else 
                        opall_toolkit.enableOrDisablePrinting(false);
                } else {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(true);
                    else 
                        opall_toolkit.enableOrDisablePrinting(true);
                }
            }
        }
    } else if(typeof window.opener.strPrintOption != 'undefined') {
        if(window.opener.strPrintOption == '1') {
            if(isOpAll=='N' && document.IVApplet)
                document.IVApplet.enableOrDisablePrinting(false);
            else
                opall_toolkit.enableOrDisablePrinting(false);
        } else if(window.opener.strPrintOption == '2') {
            if(document.getElementById('wdesk:downloadDiv').style.display != 'undefined')
            {
                if(document.getElementById('wdesk:downloadDiv').style.display == 'none') {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(false);
                    else 
                        opall_toolkit.enableOrDisablePrinting(false);
                }
                else {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(true);
                    else 
                        opall_toolkit.enableOrDisablePrinting(true);
                }
            }
        }
    }
    
    var documentHeight="";
    try{
        if(typeof docAndFormAppletHeight != 'undefined'){
            documentHeight=getVal(docAndFormAppletHeight(strprocessname,stractivityName),"DocHeight");
            if(documentHeight!=""){

                opall.PARAM.ViewerHeight=documentHeight+"px";

            }
        }
    }catch(ex){}

    if(typeof wiproperty!= 'undefined' && (wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"))
        setFormFocus();
    if(opall_toolkit && opall_toolkit.getCurrentPage() ){
    var strPageNumber=opall_toolkit.getCurrentPage();
    currentSelPageInImage(strPageNumber);
    }
     //document.getElementById('docOptionsDiv').style.display="none";
     if(typeof enableZonePartition != 'undefined'){
        enableZonePartition(strprocessname,stractivityName); 
    } 
}

function getPageWaterMark(pageNo){
    var obj = {};
	obj.textWaterMark =pageNo;
    obj.waterMarkPosition = 6;
    obj.textWaterMarkProperties = {
      'xCoordinate': 50,
      'yCoordinate': 90,
      'fontSize': 25,
      'fontStyle': 1,
      'transparency' : 0.9,
	  'angle' : 360,
    };    
    return JSON.stringify(obj);
   }
   
function isImageDisplayed(){    
    if(isOnchangeDoc=="onchange"){         
         if(defalutShowToolbar=='Y'){
                try{
                     var annotationRight = document.getElementById('wdesk:annotationRight').value;
                     if(annotationRight=='0' || (checkAnnotRights=="Y" && document.getElementById('wdesk:checkoutDiv').style.display=="none")) {
                        document.IVApplet.showNHideToolBars(true,false);
                    }
                    else{
                        document.IVApplet.showNHideToolBars(true,true);
                    }
                } catch(e){}
                document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/hide_toolbar.png";
                document.getElementById('wdesk:hide').title=HIDE_TOOLBAR;
                showHide='N';
           }else{
                try{
                    document.IVApplet.showNHideToolBars(false,false);
                } catch(e){}
                document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/show_toolbar.png";
                document.getElementById('wdesk:hide').title=SHOW_TOOLBAR;
                showHide='Y';
             }
        isOnchangeDoc="";
        
        if(!isAnnotationToolbar(strprocessname,stractivityName) && showHide=='N'){
            document.IVApplet.showNHideToolBars(true,false);
        }            
    }else if(isOnchangeDoc=="onload"){          
          if(defalutShowToolbar=='Y'){
              try{
                    var annotationRight = document.getElementById('wdesk:annotationRight').value;
                    if(annotationRight=='0' || (checkAnnotRights=="Y" && document.getElementById('wdesk:checkoutDiv').style.display=="none")) {
                        document.IVApplet.showNHideToolBars(true,false);
                    }
                    else{
                        document.IVApplet.showNHideToolBars(true,true);
                    }
                    
                    document.getElementById('wdesk:hideRead').src=sContextPath+"/resources/images/hide_toolbar.png";
                    document.getElementById('wdesk:hideRead').title=HIDE_TOOLBAR;
                } catch(e){}
           }
            isOnchangeDoc="";
            
            if(!isAnnotationToolbar(strprocessname,stractivityName) && showHide=='N'){
                document.IVApplet.showNHideToolBars(true,false);
            }
     }
     var documentHeight="";
           if(typeof docAndFormAppletHeight != 'undefined'){
                documentHeight=getVal(docAndFormAppletHeight(strprocessname,stractivityName),"DocHeight");
                if(documentHeight!=""){

                         document.IVApplet.height=documentHeight+"px";

                 }
            }

if(typeof wiproperty!= 'undefined' && (wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"))
         setFormFocus();
      //document.getElementById('docOptionsDiv').style.display="none";
}



function showNhide(view){
    if(typeof view == 'undefined')
        view = '';
    
    if(view=='R')
    {
        if(showHide=='Y')
            {
                showHide='N'
                if(checkAnnotRights=="Y" && document.getElementById('wdesk:checkoutDiv').style.display=="none"){
                if(isOpAll=='N') {
                document.IVApplet.showNHideToolBars(true,false);
                } else {
                opall_toolkit.showNHideToolBars(true,false);   
                }
                } else {
                if(isOpAll=='N') {
                    appletWin.document.IVApplet.showNHideToolBars(true,true);
            } else {
                opall_toolkit.showNHideToolBars(true,true);   
            }
            }
                document.getElementById('wdesk:hideRead').src=sContextPath+"/resources/images/hide_toolbar.png";
                document.getElementById('wdesk:hideRead').title=HIDE_TOOLBAR;
            }
        else
            {
                showHide='Y';
                if(isOpAll=='N') {
                document.IVApplet.showNHideToolBars(false,false);
                } else {
                opall_toolkit.showNHideToolBars(false,false);   
                }
                document.getElementById('wdesk:hideRead').src=sContextPath+"/resources/images/show_toolbar.png";
                document.getElementById('wdesk:hideRead').title=SHOW_TOOLBAR;
            }   
    
    } else{    
        if(showHide=='Y')
        {
            showHide='N';
            try{
                if(checkAnnotRights=="Y" && document.getElementById('wdesk:checkoutDiv').style.display=="none")
                    {
                    if(isOpAll=='N') {
                        document.IVApplet.showNHideToolBars(true,false);
                    } else {
                        opall_toolkit.showNHideToolBars(true,false);   
                    }
                }
                else
                    {
                    if(isOpAll=='N') {
                        document.IVApplet.showNHideToolBars(true,true);
                    } else {
                        opall_toolkit.showNHideToolBars(true,true);   
                    }
                }
            }catch(e){}
            document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/hide_toolbar.png";
            document.getElementById('wdesk:hide').title=HIDE_TOOLBAR;
        //document.getElementById('wdesk:zoomCom').style.display="none";

        }
        else
        {
            try{
            showHide='Y';
            //document.getElementById('wdesk:zoomCom').value=resizePercentage;
            //document.getElementById('wdesk:zoomCom').style.display="inline";
            if(isOpAll=='N') {
                var obj = document.IVApplet;
                resizePercentage = obj.getZoom();
                document.IVApplet.showNHideToolBars(false,false);
            } else {
                opall_toolkit.showNHideToolBars(false,false);   
            }}
        catch(e){}
            document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/show_toolbar.png";
            document.getElementById('wdesk:hide').title=SHOW_TOOLBAR;
        }
    }
    if(typeof window.strPrintOption != 'undefined')
    {

        if(window.strPrintOption == '1')
        {
            if(isOpAll=='N' && document.IVApplet)
                document.IVApplet.enableOrDisablePrinting(false);
            else 
                opall_toolkit.enableOrDisablePrinting(false);
        }
        else if(window.strPrintOption == '2')
        {
            if(document.getElementById('wdesk:downloadDiv').style.display != 'undefined')
            {
                if(document.getElementById('wdesk:downloadDiv').style.display == 'none')
                    {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(false);
                    else 
                        opall_toolkit.enableOrDisablePrinting(false);
                }
                else
                    {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(true);
                    else 
                        opall_toolkit.enableOrDisablePrinting(true);
                }
            }


            if(typeof window.isEnableDownloadPrint != 'undefined' && window.isEnableDownloadPrint(strprocessname,stractivityName,userName))
            {
                if(isOpAll=='N' && document.IVApplet)
                    document.IVApplet.enableOrDisablePrinting(true);
                else 
                    opall_toolkit.enableOrDisablePrinting(true);
            }

        }

    }
    else if(typeof window.opener.strPrintOption != 'undefined')
    {

        if(window.opener.strPrintOption == '1')
        {
            if(isOpAll=='N' && document.IVApplet)
                document.IVApplet.enableOrDisablePrinting(false);
            else
                 opall_toolkit.enableOrDisablePrinting(false);
        }
        else if(window.opener.strPrintOption == '2')
        {
            if(document.getElementById('wdesk:downloadDiv').style.display != 'undefined')
            {
                if(document.getElementById('wdesk:downloadDiv').style.display == 'none')
                    {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(false);
                    else 
                        opall_toolkit.enableOrDisablePrinting(false);
                }
                else
                    {
                    if(isOpAll=='N' && document.IVApplet)
                        document.IVApplet.enableOrDisablePrinting(true);
                    else 
                        opall_toolkit.enableOrDisablePrinting(true);
                }
            }

            if(typeof window.opener.isEnableDownloadPrint != 'undefined' && window.opener.isEnableDownloadPrint(window.opener.strprocessname,window.opener.stractivityName,window.opener.userName))
            {
                if(isOpAll=='N' && document.IVApplet)
                    document.IVApplet.enableOrDisablePrinting(true);
                else 
                    opall_toolkit.enableOrDisablePrinting(true);
            }

        }

    }
    return false;
}

function toggleIntFace(face1,face2){    
    if(navigator.appName.indexOf("Netscape") != -1 && wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"){
        if(face2=="form" && isFormLoaded==true){
            if((typeof bDefaultNGForm !='undefined') && !bDefaultNGForm){            
            } else {
                ngfrmBuffer=document.wdgc.getFieldValueBagEx();
            }
        }
    }

    document.getElementById(face1+'Div').style.display='';
    document.getElementById(face2+'Div').style.display='none';
    
    var scrollDir = (pageDirection == 'ltr')? 'left': 'right';    
    
    if(docLoaded == false && face1 == 'doc'){
        sendAsynAjaxRequest('doc', "doc_main_view.jsf");
        //addWindowToParent(windowList,'tableGrid');
    } else if(docLoaded == true && face1 == 'doc'){            
        var docType = "";
        var objCombo = document.getElementById('wdesk:docCombo');        
        if(objCombo){                    
            var docIndex = objCombo.value - 0;
            if(docIndex > 0){
                var tmpParsedDocJSON = eval("("+document.getElementById('wdesk:docInfoJSON').value+")");    
                var docInfoJSON = tmpParsedDocJSON[docIndex];
                if(typeof docInfoJSON != 'undefined'){
                    docType = docInfoJSON.document[0].DocType;
                }
            }
        }
        
        if(docType != 'I' && docType != ''){                   
            /*var docEle = $("#docviewerDiv").find(".mCSB_container")[0];
            if(docEle){                    
                var cWidth = document.getElementById('docDiv').parentNode.style.width.split('p')[0];
                docEle.style.width = (cWidth)+"px";                    
            }*/
        }
    }
    
    if(formLoaded == false && face1 == 'form')
    {
        if(wiproperty.formType=="NGFORM")
            if(ngformproperty.type=="applet")
            {
                //detectJRE();
            }                              
        sendAsynAjaxRequest('form', "form_main_view.jsf");
        formLoaded = true;
    } else if(formLoaded == true && face1 == 'form'){        
        if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm && !bAllDeviceForm) {
            var ngformIframe = window.parent.document.getElementById("ngformIframe");
            try{
                ngformIframe.contentWindow.updateNGHTMLFormLoad();
            } catch(e){}    
        } else if(wiproperty.formType == "HTMLFORM"){
            var cWidth = document.getElementById('formDiv').parentNode.style.width.split('p')[0];
            var cHeight = document.getElementById('formDiv').parentNode.style.height.split('p')[0];
            //document.getElementById('formDiv').style.width = (cWidth-3)+"px";
            document.getElementById('formDiv').style.height = (cHeight-3)+"px";
           // $("#formDiv").mCustomScrollbar("scrollTo",scrollDir,{scrollInertia:0});
        }
    }
    
    if(expLoaded == false && face1 == 'exp')
    {
        sendAsynAjaxRequest('exp', "exp_main_view.jsf");
    }else if(expLoaded == true && face1 == 'exp'){
       /*var cWidth = document.getElementById('expDiv').parentNode.style.width.split('p')[0];        
       var expEle = $("#expGrid").find(".mCSB_container")[0];
       if(expEle){
            expEle.style.width = (cWidth)+"px";     
       }*/
    }
    
    if(todoLoaded == false && face1 == 'todo')
    {
        sendAsynAjaxRequest('todo', "todo_main_view.jsf");
    } else if(todoLoaded == true && face1 == 'todo'){
        /*var cWidth = document.getElementById('todoDiv').parentNode.style.width.split('p')[0];
        var todoEle = $("#todoDiv").find(".mCSB_container")[0];
        if(todoEle){        
            todoEle.style.width = (cWidth)+"px";     
        }*/
        
        //$("#todoDiv").mCustomScrollbar("scrollTo",scrollDir,{scrollInertia:0});
    } 

    if(taskLoaded == false && face1 == 'task')
    {
        sendAsynAjaxRequest('task', "task_main_view.jsf");
    } else if(taskLoaded == true && face1 == 'task'){
        /*var cWidth = document.getElementById('taskDiv').parentNode.style.width.split('p')[0];
        var taskEle = $("#taskDiv").find(".mCSB_container")[0];
        if(taskEle){        
            taskEle.style.width = (cWidth)+"px";     
        }*/
        
//        $("#taskDiv").mCustomScrollbar("scrollTo",scrollDir,{
//            scrollInertia:0
//        });
    }
}

function clearExpBtn(){
    isCommentMandatory=window.document.getElementById('wdesk:IsCommentMandatory').value;
    var comnts = Trim(window.document.getElementById("wdesk:comnt").value);
    if(isCommentMandatory=="Y" && comnts==""){
        alert(ALERT_CLEAR_COMMENT);
        window.document.getElementById("wdesk:comnt").focus();
        return false;
    }
     clickButton(window.frames['expOpFrm'].document.getElementById('clear:clearExpBtn'));

}


function modifyExpBtn(){
    // alert(window.frames['expOpFrm'].document.getElementById('modify:modifyExpBtn'));
    clickButton(window.frames['expOpFrm'].document.getElementById('modify:modifyExpBtn'));
//window.frames['expOpFrm'].clickLink('modify:modifyExpBtn');
}

function raiseExpBtn(){
    
    var tmpExpList = document.getElementById('wdesk:expList');
    if(tmpExpList!= undefined && tmpExpList.value == ""){
        alert(SELECT_EXCEPTION);
        
        return false;
    }
    
    //alert(window.frames['expOpFrm'].document.getElementById('raise:raiseExpBtn'));
    clickButton(window.frames['expOpFrm'].document.getElementById('raise:raiseExpBtn'));
}

function responseExpBtn(){    
    var isCommentMandatory=window.document.getElementById('wdesk:IsCommentMandatory').value;
    var comnts = Trim(window.document.getElementById("wdesk:comnt").value);
    if(isCommentMandatory=="Y" && comnts==""){
        alert(ALERT_RESPONSE_COMMENT);
        window.document.getElementById("wdesk:comnt").focus();
        return false;
    }

    clickButton(window.frames['expOpFrm'].document.getElementById('clear:responseExpBtn'));
//window.frames['expOpFrm'].clickButton('clear:responseExpBtn');
}

function rejectExpBtn(){
    isCommentMandatory=window.document.getElementById('wdesk:IsCommentMandatory').value;
    var comnts = Trim(window.document.getElementById("wdesk:comnt").value);
    if(isCommentMandatory=="Y" && comnts==""){
        alert(ALERT_REJECT_COMMENT);
        window.document.getElementById("wdesk:comnt").focus();
        return false;
    }
    
    clickButton(window.frames['expOpFrm'].document.getElementById('clear:rejectExpBtn'));
}

function clickButton(obj)
{
    var fireOnThis =obj;
    if (document.createEvent)
    {
        var evObj = document.createEvent('MouseEvents');
        evObj.initMouseEvent("click", true, true, window,0, 0, 0, 0, 0, false, false, false, false, 0, null);
        fireOnThis.dispatchEvent(evObj);
    }
    else if (document.createEventObject)
    {
        fireOnThis.click();
    }
}

function for_save(value,from){

    // saveCalled=false;
    var formParam="";	//used to store form parameter
    var todoParam="";       //used to store todo param
    var taskParam="";      //used to store task param
    var param="";           //used to store combined param
    var status="";          //return status for save call
    var formWindow=getWindowHandler(windowList,"formGrid");
    var todoWindow=getWindowHandler(windowList,"todoGrid");
    var taskWindow=getWindowHandler(windowList,"taskGrid");
    if(formWindow){//added for Variant : 22 Oct 2013
        executeOnSaveVariant();
    }
    if(value =='CLOSE'){
        var excpWindow=getWindowHandler(windowList,"exceptionGrid");
        if(typeof excpWindow!='undefined'){
            excpWindow.excpOk("save");
        }
    }

    var bSaveTodo = true;
    if(SharingMode && from!=undefined && from == "broadCastForm"){
        bSaveTodo = false;
    }
    // alert("for_save"+formWindow);
    if(typeof formWindow!='undefined'){
        if(wiproperty.formType=="NGFORM"){
            status= validatData();
        }
        else if(wiproperty.formType=="HTMLFORM"){
            status=htmlFormOk();
        }
        else if(wiproperty.formType=="VARIANTFORM"){//added for Variant : 17 Oct 13
            status=htmlFormOk();
        }
        if(wiproperty.formType=="CUSTOMFORM"){
            var customformwindow=formWindow.frames['customform'];
            status=htmlFormOk(customformwindow);
        }
        if(status!='false' && status!=''){
            if(wiproperty.formType!="NGFORM")
            {
                setmessageinDiv(status,"true",1000);
                if(from == 'close')
                    alert(VALIDATION_FAILED);
                formWindow.focus();
            }
            return "failure";
        }
        
        if(status=='false'){
            wiform= formWindow.document.getElementById('wdesk');
            if(wiproperty.formType=="NGFORM"){
                if(ngformproperty.type=="applet"){
                    if(!bDefaultNGForm){                            
                    } else {
                        formParam="ngParam="+encodeURIComponent(formWindow.document.wdgc.getFieldValueBagEx())+"&";
                    }
                }
                else
                    formParam=formWindow.getNGFormValuesForAjax();
            }
            else if(wiproperty.formType=="HTMLFORM") {
                formParam=getFormValuesForAjax(wiform);
            }
            else if(wiproperty.formType=="VARIANTFORM") {//added for Variant : 17 Oct 13
                formParam=getFormValuesForAjax(wiform);
            }
            else if(wiproperty.formType=="CUSTOMFORM") {

                //alert(formWindow.frames['customform'].document.forms['wdesk']);
                var customform='';
                if(WIObjectSupport.toUpperCase()=='Y')
                    customform=formWindow.frames['customform'].document.forms['dataform'];
                else
                    customform=formWindow.frames['customform'].document.forms['wdesk'];

                formParam=getFormValuesForAjax(customform);

            }
        }
    }
    if(bSaveTodo){
        if((typeof todoWindow !='undefined' && typeof formWindow!='undefined')){
            if(todoWindow.location!=formWindow.location||wiproperty.formType=="CUSTOMFORM" || wiproperty.formType=="NGFORM"){
                //alert("customform");
                //alert(todoWindow.document.getElementById('wdesk'));
                var todoform= todoWindow.document.getElementById('wdesk');
                todoParam=todoWindow.getFormValuesForAjax(todoform);
            }
        }
        else if((typeof todoWindow!='undefined'&&typeof formWindow=='undefined')){
            //alert("wdesk");
            var todoform= todoWindow.document.getElementById('wdesk');
            todoParam=todoWindow.getFormValuesForAjax(todoform);
        }
    }
    if(typeof taskWindow!='undefined'){
        if(wiproperty.TaskFormType=="NGFORM"){
            status= validateTaskData();
        }
        else if(wiproperty.TaskFormType=="HTMLFORM"){
        // status=htmlFormOk();
        }
        if(status!='false' && status!=''){
            if(wiproperty.TaskFormType!="NGFORM")
            {
                setmessageinDiv(status,"true",1000);
                if(from == 'close')
                    alert(VALIDATION_FAILED);
                taskWindow.focus();
            }
            return "failure";
        }
        
        if(status=='false'){
            var taskform= taskWindow.document.getElementById('wdesk');
            if(wiproperty.TaskFormType=="NGFORM"){
                if(taskngformproperty.type=="applet"){
                    if(!bDefaultNGForm){                            
                    } else {
                        taskParam="taskngParam="+encodeURIComponent(taskWindow.document.wdgc.getFieldValueBagEx())+"&";
                    }
                }
                else
                    taskParam=taskWindow.getNGFormValuesForAjax();
            }
            else if(wiproperty.TaskFormType=="HTMLFORM") {
                taskParam=taskWindow.getFormValuesForAjax(taskform);
            }
        }
    }

    param=formParam+todoParam+taskParam;
    // alert("param"+param);
    //var pid=document.getElementById('wdesk:pid');
    //var wid=document.getElementById('wdesk:wid');
    // alert(pid);

    //param+='pid='+encode_utf8(pid.value)+'&wid='+wid.value;
    param+='pid='+encodeURIComponent(pid)+'&wid='+wid+'&taskid='+taskid+'&WD_UID='+WD_UID;
    if(from != undefined)
        param+='&fromOpt='+encode_utf8(from);
    if(value=="dummysave")
        param+="&dummysave=y"
    /* var strCustomParam=document.getElementById("customParam").value;
        param+="&customParam="+strCustomParam;*/
    var strExtParam=getExtParam(strprocessname,stractivityName);
    param+="&extParam="+strExtParam;
    var ajaxReq;
    var url='';
    for(var i=0;i<3;i++){
        try{
            ajaxReq=null;
            if (window.XMLHttpRequest) {
                ajaxReq= new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
            }
            url = "/webdesktop/ajaxworkdeskwindowsave.jsf";
            url = appendUrlSession(url);
            ajaxReq.open("POST", url, false);
            ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            //alert("---------------------"+param);
            ajaxReq.send(param);
            break;
        }catch(ex){

        }
    }
    if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
        document.body.style.cursor="default";
        removemessageFromDiv();
        if(value!='dummysave')
        {
            if(typeof formWindow!='undefined'){
                if(wiproperty.formType=="NGFORM")
                {
                    if(!(ngformproperty.type=="applet"))
                        formBuffer = new String(formWindow.document.wdgc.FieldValueBag);
                    else if(ngformproperty.type=="applet")
                    {
                        if(!bDefaultNGForm){                            
                            var ngformIframe = document.getElementById("ngformIframe");	
                            if(ngformIframe != null){
                                try{
                                    if(bAllDeviceForm){
                                        ngformIframe.contentWindow.eval("clearValueChanged()");	
                                    } else {
                                        ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.clearValueChanged()");	
                                    }
                                }catch(e){}
                            }
                        } else {
                            formBuffer=formWindow.document.wdgc.getFieldValueBagEx();
                            formBuffer=formBuffer+"";
                        }
                    }
                }
            }
            saveCalled=false;
            if(typeof confirmAnnotationSave!=undefined && confirmAnnotationSave=="Y"){
                if(document.getElementById('wdesk:ivapp')!= null  && (document.getElementById('wdesk:ivapp').style.display=="inline" || document.getElementById('wdesk:ivapp').style.display=="block")){
                    try{
                        if(document.IVApplet && document.IVApplet.annotationModified()){
                            document.IVApplet.saveAnnotations();
                        }
                    }catch(e){}
                }
                if(document.getElementById('wdesk:ifrm')!= null  && (document.getElementById('wdesk:ifrm').style.display=="inline"|| document.getElementById('wdesk:ifrm').style.display=="block")){
                    if(isOpAll=='Y' && opall_toolkit && opall_toolkit.annotationModified()){
                        opall_toolkit.saveAnnotation();
                    } 
                }
            }
            //setmessageinDiv('',"true",1000);
            setmessageinDivSuccess('',"true",1000,value);
        }
        postSaveFormHook('success',ajaxReq.status);
        return 'success';
    }
    else
    {
        if(ajaxReq.status==598)
        {
            if(ajaxReq.responseText == '975' ){ // Error handling for case refresh
                var errormsg = getRefreshMsg();
                if(document.getElementById('Save') != null){
                    document.getElementById('Save').style.display='none';
                }
                if(document.getElementById('Done') != null) {
                    document.getElementById('Done').style.display='none';
                }
                if(document.getElementById('Close') != null) {
                    document.getElementById('Close').style.display='none';
                }
                setmessageinDiv(errormsg, "false", 3000);
            }else    
                setmessageinDiv(ajaxReq.responseText,"true",3000);
            
            postSaveFormHook('failure',ajaxReq.status);
            if(from=='close'){
                return 'closeFail';
            }else
                return;
        //removemessageFromDiv();
        //alert(ajaxReq.responseText);

        }
        else if(ajaxReq.status==599)
        {
            removemessageFromDiv();
            //url = sContextPath+"/login/logout.jsp?"+"error=4020";
            url = sContextPath+"/error/errorpage.jsf?msgID=4020";
            url = appendUrlSession(url);
            //window.open(url,reqSession);

            var width = 320;
            var height = 160;
            var left = (window.screen.availWidth-width)/2;
            var top = (window.screen.availHeight-height)/2;            
            if (window.showModalDialog){
                window.showModalDialog(url,'',"dialogWidth:"+width +"px;dialogHeight:"+height+"px;center:yes;dialogleft: "+left+"px;dialogtop: "+top+"px");
            } else {
                setmessageinDiv(INVALID_SESSION, "true", 3000);
            }
        }
        else if(ajaxReq.status == 400)
            alert(INVALID_REQUEST_ERROR);
        else if(ajaxReq.status==12029){
            alert(ERROR_SERVER); 
        }
        else{
            removemessageFromDiv();
            alert(ALERT_OPERATION_UNSUCCESSFUL);
            postSaveFormHook('failure',ajaxReq.status);
            return false;
        }
        postSaveFormHook('failure',ajaxReq.status);
        return 'failure';
    }


    
}
function removemessageFromDiv(){
    if((isEmbd == 'Y') && (wdView == "em") && document.getElementById("wdesk:indicatorPG"))
        document.getElementById("wdesk:indicatorPG").style.display = "none";

    var messageImg=document.getElementById("wdesk:waitIndicator");
    if(messageImg) {
        messageImg.style.display='none';
    }
    var messageDiv=document.getElementById("wdesk:messagediv");
    if(messageDiv) {
        var messagePG = document.getElementById("wdesk:messagedivPG");
        if(messagePG){
            messagePG.style.display = "none";
            messageDiv.innerHTML="";
        }
    }
}

function InitHWDoc(op,documentIndex)
{ 
    op = (typeof op == 'undefined')? '': op;
    var cWidth = document.getElementById('docDiv').parentNode.style.width.split('p')[0];
    var cHeight = document.getElementById('docDiv').parentNode.style.height.split('p')[0];
    var isCase = false;
    if(cWidth == '' && cHeight == ''){// Handling for CaseView/TaskView
        isCase = true;
        cWidth = document.getElementById('docDiv').parentNode.parentNode.style.width.split('p')[0];
        cHeight = document.getElementById('docDiv').parentNode.parentNode.style.height.split('p')[0];
        document.getElementById('docDiv').style.height = (cHeight-30)+"px";
        document.getElementById('docDiv').style.width = (cWidth-2)+"px";
    }else{
        document.getElementById('docDiv').style.height = (cHeight)+"px";
        document.getElementById('docDiv').style.width = (cWidth)+"px";
    }

    isOnchangeDoc="onload";
    if(document.IVApplet && defalutShowToolbar=='Y'){
        document.getElementById('wdesk:hide').src=sContextPath+"/resources/images/hide_toolbar.png";
        document.getElementById('wdesk:hide').title=HIDE_TOOLBAR;
        showHide='N';
    }
    
    //change by Gaurav Bug id 27260 19/10/2011
    
    var objCombo = document.getElementById('wdesk:docCombo');
    var docIndex = -1;    
    if(objCombo){        
        docIndex = objCombo.value - 0;
        if(docIndex == -1){
            document.getElementById('wdesk:ifrm').style.display='none';
            document.getElementById('wdesk:noDocSelected').style.height=(cHeight-50)+"px";
            document.getElementById('wdesk:noDocSelected').style.display='inline';
            document.getElementById('wdesk:docOperation').style.display='inline';
        }
        if(isOpAll=='Y' && Trim(document.getElementById('wdesk:docCombo').value) != -1 && Trim(document.getElementById('wdesk:docCombo').value) != "" && op!="Resize"){
            if(op=='scan')
            {
              reloadapplet(documentIndex, "", op);
            }
            else
            {
              reloadapplet(document.getElementById('wdesk:docCombo').value, "", op);  
            }
        }
    }
    //changes end here


    if(document.IVApplet)
    {
        if((isEmbd == 'Y') && (wdView == "em")){
            document.IVApplet.height = (cHeight - 50)+"px";
        } else {
            if(wDeskLayout.WDeskType == 'E'){
                if (isCase)// Handling for CaseView/TaskView
                    document.IVApplet.height = (cHeight - 75)+"px";
                else
                    document.IVApplet.height = (cHeight - 50)+"px";
            }else {                
                if(typeof window.chrome != 'undefined'){
                    document.IVApplet.height = (cHeight - 33)+"px"; 
                } else {
                    document.IVApplet.height = (cHeight - 28)+"px"; 
                }
            }
        }
    }

    var docType = "";
    var docExt = "";
    if(docIndex > 0){
        var tmpParsedDocJSON = eval("("+document.getElementById('wdesk:docInfoJSON').value+")");    
        var docInfoJSON = tmpParsedDocJSON[docIndex];
        if(typeof docInfoJSON != 'undefined'){
            docType = docInfoJSON.document[0].DocType;
            docExt = docInfoJSON.document[0].DocExt;
        }    
    
        if(docType != 'I' && docType != ""){
            
            if(docExt != "txt"){
                var documentHeight="";
                if(typeof docAndFormAppletHeight != 'undefined'){
                    documentHeight=getVal(docAndFormAppletHeight(strprocessname,stractivityName),"FormHeight");
                    if(documentHeight!="")
                        document.getElementById('docviewer').style.height=documentHeight+"px";
                }

                if((isEmbd == 'Y') && (wdView == "em")){
                    document.getElementById('docviewer').style.height = cHeight - 50 + "px";
                } else {
                    if(wDeskLayout.WDeskType == 'E'){
                        if(isCase)// Handling for CaseView/TaskView
                            document.getElementById('docviewer').style.height = cHeight - 75 + "px";
                        else
                            document.getElementById('docviewer').style.height = cHeight - 50 + "px";
                    //document.getElementById('docviewer').style.width = cWidth - 50 + "px";
                    }else {
                        if(typeof window.chrome != 'undefined'){
                            document.getElementById('docviewer').style.height = cHeight - 33 + "px";
                        } else {
                            document.getElementById('docviewer').style.height = cHeight - 28 + "px";
                        }
                    }
                }
                
                if(op != "Resize"){
                    var docURL=document.getElementById('wdesk:docFrameURL').value;
                    if(docExt.toUpperCase()=='PDF'&&(typeof isPdftoolbarenable != 'undefined' && isPdftoolbarenable(strprocessname, stractivityName))){
                        var index=docURL.indexOf("#toolbar");
                        if(index!=-1){
                            docURL=docURL.subString(0,docURL.indexOf("#toolbar"));
                        }
                    }
                    document.getElementById('docviewer').src=docURL;
                }
                document.getElementById('docviewer').style.display = "block";
                
                var DocListDiv=document.getElementById('wdesk:docList');
                if(DocListDiv){
                    DocListDiv.style.display = "inline";
                }
            } else {   
                if(op == "Resize"){
                    updateConversationDoc();
                } else {
                    var DocListDiv=document.getElementById('wdesk:docList');
                    if(DocListDiv){
                        DocListDiv.style.display = "none";
                    }
                    
                    var contentLoaderRef = new net.ContentLoader(document.getElementById('wdesk:docFrameURL').value, conversationDocHandler, null, "POST",null, true);        
                }
            }
        } else {
            if(op == "Resize"){
                if((isEmbd == 'Y') && (wdView == "emwd")){                   
                    if(wDeskLayout.WDeskType != 'E'){
                        if(typeof window.chrome != 'undefined'){
                            document.getElementById('docviewer').style.height = cHeight - 33 + "px";
                        } else {
                            document.getElementById('docviewer').style.height = cHeight - 28 + "px";
                        }
                    }
                }
            }
        }
    }
}

function updateConversationDoc(){
    var docviewerDiv = document.getElementById("docviewerDiv"); 
    docviewerDiv.style.display = "block";
    
    var isEmbdFlag = (typeof isEmbd == 'undefined'? 'N': isEmbd);
    if((isEmbdFlag != 'Y') || (wdView != "em")){
        var cWidth = document.getElementById('docDiv').parentNode.style.width.split('p')[0];
        var cHeight = document.getElementById('docDiv').parentNode.style.height.split('p')[0];
        var isCase = false;
        if(cWidth == '' && cHeight == ''){// Handling for CaseView/TaskView
            isCase = true;
            cWidth = document.getElementById('docDiv').parentNode.parentNode.style.width.split('p')[0];
            cHeight = document.getElementById('docDiv').parentNode.parentNode.style.height.split('p')[0];
        }
        if(wDeskLayout.WDeskType == 'E'){
            docviewerDiv.style.height = (cHeight - 54) + "px";
        } else {
            if(typeof window.chrome != 'undefined'){
                docviewerDiv.style.height = cHeight - 33 + "px";
            } else {
                docviewerDiv.style.height = cHeight - 28 + "px";
            }
        }
        docviewerDiv.style.width = (cWidth - 4) + "px";
    } else {
        docviewerDiv.style.overflow = "hidden";
    }    
}

function conversationDocHandler(){
   // $("#docviewerDiv").mCustomScrollbar("destroy");
    
    var docviewerDiv = document.getElementById("docviewerDiv");    
    docviewerDiv.innerHTML = "<pre>"+this.req.responseText+"</pre>"; 
    docviewerDiv.style.display = "block";
    var elementHeightWidth = getElementHeightWidth(docviewerDiv);   

    var isEmbdFlag = (typeof isEmbd == 'undefined'? 'N': isEmbd);
    if((isEmbdFlag != 'Y') || (wdView != "em")){
        var cWidth = document.getElementById('docDiv').parentNode.style.width.split('p')[0];
        var cHeight = document.getElementById('docDiv').parentNode.style.height.split('p')[0];
        var isCase = false;
        if(cWidth == '' && cHeight == ''){// Handling for CaseView/TaskView
            isCase = true;
            cWidth = document.getElementById('docDiv').parentNode.parentNode.style.width.split('p')[0];
            cHeight = document.getElementById('docDiv').parentNode.parentNode.style.height.split('p')[0];
        }
        if(wDeskLayout.WDeskType == 'E'){
            docviewerDiv.style.height = (cHeight - 54) + "px";
        } else {
            if(typeof window.chrome != 'undefined'){
                docviewerDiv.style.height = cHeight - 33 + "px";
            } else {
                docviewerDiv.style.height = cHeight - 28 + "px";
            }
        }
        //docviewerDiv.style.marginLeft = "5px";
        docviewerDiv.style.width = (cWidth - 4) + "px";
        
        //getCustomScrollbar("#docviewerDiv", true, "yx", true, false, true, true);    

        /*var docviewerEle = $("#docviewerDiv").find(".mCSB_container")[0];
        if(docviewerEle){                    
            docviewerEle.style.height = (elementHeightWidth.Height) + "px"
            docviewerEle.style.width = (elementHeightWidth.Width) + "px"
        }*/
    } else {
        docviewerDiv.style.overflow = "hidden";
        //docviewerDiv.style.height = "100%";
        docviewerDiv.style.height = elementHeightWidth.Height+ "px"; 
        docviewerDiv.style.width = elementHeightWidth.Width + "px";
                
        /*getCustomScrollbar("#contentDiv", true, "yx", true, false, true, true);            
                
        var docviewerEle = $("#contentDiv").find(".mCSB_container")[0];
        if(docviewerEle){   
            docviewerEle.style.width = elementHeightWidth.Width + "px"; 
        }*/
    }    
    
    var DocListDiv=document.getElementById('wdesk:docList');
    if(DocListDiv){
        DocListDiv.style.display = "inline";
    }
}

function conversationDocWinHandler(){
   // $("#docviewerDiv").mCustomScrollbar("destroy");
    
    var docviewerDiv = document.getElementById("docviewerDiv");
    docviewerDiv.style.display = "block";
    docviewerDiv.innerHTML = "<pre>"+this.req.responseText+"</pre>";
    
    var elementHeightWidth = getElementHeightWidth(docviewerDiv);   
    var cHeight = document.getElementById('docDiv').style.height.split('p')[0];
    
    docviewerDiv.style.height = cHeight + "px";
    
    //getCustomScrollbar("#docviewerDiv", true, "yx", true, false, true, true);    

    /*var docviewerEle = $("#docviewerDiv").find(".mCSB_container")[0];
    if(docviewerEle){                    
        docviewerEle.style.height = (elementHeightWidth.Height) + "px";
        docviewerEle.style.width = (elementHeightWidth.Width) + "px";
    }  */ 
    
    var DocListDiv=document.getElementById('wdesk:docList');
    if(DocListDiv){
        DocListDiv.style.display = "inline";
    }
}

function initHWForm(heightH, compWidth) {
    if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm) {
        var ngformIframe = document.getElementById("ngformIframe");	
        if(ngformIframe != null){
            if(bAllDeviceForm){
                ngformIframe.style.width = compWidth-3+"px";
            }else {
                ngformIframe.style.width = "100%";
            }
            ngformIframe.style.height = heightH-11 + "px";
            
            if(!bAllDeviceForm){
                try{
                    ngformIframe.contentWindow.updateNGHTMLFormLoad();
                } catch(e){}    
            }
        }
    }else if(wiproperty.formType=="CUSTOMFORM"){
        var customformObj = document.getElementById("customform");
        if(customformObj != null){          
            customformObj.style.height = (heightH - 48) + "px"; 
            customformObj.style.width = (compWidth - 12) + "px";           
            
            //updateCustomFormScroller();
        }
    }else if(wiproperty.formType=="VARIANTFORM"){
        /*$("#contentDiv").mCustomScrollbar("destroy"); 
        if(typeof addJqueryScroll != 'undefined'){
            addJqueryScroll();
        }*/
        
        /*var FormEle = $("#containerDiv").find(".mCSB_container")[0];
        if(FormEle){ 
            var ref = document.getElementById("formDiv").firstElementChild;
            if(ref != null){
                FormEle.style.width = ref.clientWidth + "px";
            }
        } */          
    }else if(wiproperty.formType=="HTMLFORM"){
        /*$("#contentDiv").mCustomScrollbar("destroy");
        if(typeof addJqueryScroll != 'undefined'){
            addJqueryScroll();
        }*/
        
        /*var FormEle = $("#containerDiv").find(".mCSB_container")[0];
        if(FormEle){
            var ref = document.getElementById("formDiv").firstElementChild;
            if(ref != null){
                FormEle.style.width = ref.clientWidth + "px";
            }
        }  */         
    }else {
        var appletDiv = document.getElementById("appletDiv");    
        if(appletDiv != null && appletDiv != undefined) {        
            if(typeof compWidth != 'undefined')	{
                appletDiv.style.width = compWidth - 25 +"px";
            } else {
                appletDiv.style.width = "100%";
            }
            //appletDiv.style.width = "100%";
            appletDiv.style.height = heightH - 30 + "px";
        }
    }
}

function initHWFormNew()
{
    var cWidth = document.getElementById('formDiv').parentNode.style.width.split('p')[0];
    var cHeight = document.getElementById('formDiv').parentNode.style.height.split('p')[0];
    if(cWidth=='' && cHeight==''){ // Handling for CaseView/TaskView
        cWidth = document.getElementById('formDiv').parentNode.parentNode.style.width.split('p')[0]-2;
        cHeight = document.getElementById('formDiv').parentNode.parentNode.style.height.split('p')[0]-5;
    }
    if((isEmbd == 'Y') && (wdView == "em")){
        document.getElementById('formDiv').style.height = (cHeight-25)+"px";    
    } else{
        if(wDeskLayout.WDeskType == 'E'){
            if(typeof stractivityType != 'undefined'){
                if(stractivityType=='32'){
                      document.getElementById('formDiv').style.height = (cHeight-26)+5+"px";
                }
                else{
                    document.getElementById('formDiv').style.height = (cHeight-26)+"px";
                }
        	} 	
              else{
                    document.getElementById('formDiv').style.height = (cHeight-26)+"px";
                }
        } else {
            document.getElementById('formDiv').style.height = (cHeight-3)+"px";
        }
    }
    
    document.getElementById('formDiv').style.width = (cWidth)+"px";
    
    if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm) {
        var ngformIframe = document.getElementById("ngformIframe");	
        if(ngformIframe != null){        
            if((isEmbd == 'Y') && (wdView == "em")){
                ngformIframe.style.height = (cHeight - 28) + "px";
            } else {
                if(wDeskLayout.WDeskType == 'E'){
                    ngformIframe.style.height = (cHeight - 26) + "px";                    
                    ngformIframe.style.width = (cWidth) + "px";     
                    if(!bAllDeviceForm){
                        try{
                            ngformIframe.contentWindow.updateNGHTMLFormLoad();
                        } catch(e){}                 
                    }
                } else {
                    ngformIframe.style.height = (cHeight - 10) + "px"; 
                    ngformIframe.style.width = (cWidth - 10) + "px"; 
                    ngformIframe.style.marginTop = "5px";
                    if(!bAllDeviceForm){
                        try{
                            ngformIframe.contentWindow.updateNGHTMLFormLoad();
                        } catch(e){}    
                    }
                }
                
                document.getElementById('formDiv').style.overflow = "hidden";
            }
        }
    } else if(wiproperty.formType=="CUSTOMFORM"){
        var customformObj = document.getElementById("customform");
        if(customformObj != null){     
            customformObj.style.height = (cHeight - 48) + "px";                        
            customformObj.style.width = (cWidth - 22) + "px";
            //updateCustomFormScroller();            
        }
    } else if(wiproperty.formType=="HTMLFORM"){
        //getCustomScrollbar("#formDiv", true, "yx", true, false, true, true);
    } else if(wiproperty.formType=="VARIANTFORM"){
        //getCustomScrollbar("#formDiv", true, "yx", true, false, true, true);
    }
    
    // document.getElementById('wdesk:formGrid').style.height = (cHeight-25)+"px";
    //document.getElementById('wdesk:formGrid').style.width = (cWidth)+"px";
    if(document.getElementById('wdesk:formGrid') != null){
        document.getElementById('wdesk:formGrid').style.width = (cWidth)+"px";
    }

    var appletDiv = document.getElementById("appletDiv");
    if(appletDiv != null && appletDiv != undefined) {
        appletDiv.style.width = (cWidth-20)+"px";
        var isChrome = window.chrome;
        if(typeof isChrome != 'undefined') {
            if((isEmbd == 'Y') && (wdView == "em")){
                appletDiv.style.height = (cHeight - 40) + "px";
            } else {
                if(wDeskLayout.WDeskType == 'E'){
                    appletDiv.style.height = (cHeight - 43) + "px";
                } else {
                    appletDiv.style.height = (cHeight - 20) + "px";
                }
            }
        }else{
            if((isEmbd == 'Y') && (wdView == "em")){
                appletDiv.style.height = (cHeight - 45) + "px";
            }else {
                if(wDeskLayout.WDeskType == 'E'){
                    appletDiv.style.height = (cHeight - 48) + "px";
                } else {
                    appletDiv.style.height = (cHeight - 25) + "px";
                }
            }
        }
    }    
    
}

function updateCustomFormScroller(){
    var customFormIframe = document.getElementById("customform");	
    if(customFormIframe != null){               
        var cWidth = document.getElementById('formDiv').parentNode.style.width.split('p')[0];
        var cHeight = document.getElementById('formDiv').parentNode.style.height.split('p')[0];	
        if(cWidth=='' && cHeight==''){ // Handling for CaseView/TaskView
            cWidth = document.getElementById('formDiv').parentNode.parentNode.style.width.split('p')[0]-2;
            cHeight = document.getElementById('formDiv').parentNode.parentNode.style.height.split('p')[0]-5;
        }
        if(customFormIframe.contentWindow) {
            var iframeDocumentRef = customFormIframe.contentWindow.document;        
            if(iframeDocumentRef != null) {
                if(iframeDocumentRef.body != null){                
                    var isFirefox = (navigator.appName.indexOf('Netscape') > -1);
                    if(isFirefox || (iframeDocumentRef.readyState == "complete")){
                        var docHeightWidth = getDocHeightWidth(iframeDocumentRef);
						
                        var customformDiv = document.getElementById("customformDiv");	
                        var isEmbdFlag = (typeof isEmbd == 'undefined'? 'N': isEmbd);
                        if((isEmbdFlag != 'Y') || (wdView != "em")){
                            customFormIframe.style.height = "100%";
                            customFormIframe.setAttribute("width","100%");						
                            customFormIframe.contentWindow.document.body.scroll="yes";                            
                            
                            customformDiv.style.height = (cHeight - 45) + "px";
                            customformDiv.style.width = (cWidth - 22) + "px";

                            //getCustomScrollbar("#customformDiv", true, "yx", true);     
                        }else {                            
                            //customformDiv.style.overflow = "hidden";
                        }
                    }
                }
            }
        }
    }
}

function getProperty(){

    var ctst="";
    if(wiproperty.formType=="NGFORM" && (typeof document.wdgc != 'undefined') && document.wdgc&&ngformproperty.type=="activex")
    {
        //var ngdata= eval("("+ngformproperty+")");
        if(typeof document.wdgc.PassDataToNgfUsr !='undefined'){
            document.wdgc.UserId=ngformproperty.UserIndex;
            document.wdgc.UserName=ngformproperty.UserName;
            document.wdgc.WFProcessName=ngformproperty.WFProcessName;
            document.wdgc.WFActivityName=ngformproperty.WFActivityName;
            document.wdgc.WFFolderId=ngformproperty.WFFolderId;

            document.wdgc.PassDataToNgfUsr(ngformproperty.WFGeneralData);
            document.wdgc.WFGeneralData = ngformproperty.WFGeneralData;

            if (windowProperty.winloc == "M")
                document.wdgc.WindowTitle=window.top.document.title + ctst;
            else if (windowProperty.winloc =="S")
                document.wdgc.WindowTitle=window.top.document.title + ctst;
            else
                document.wdgc.WindowTitle=window.top.opener.top.document.title + ctst;

            document.wdgc.UseServerDateFormat = true;
            document.wdgc.WFCacheTime=ngformproperty.WFCacheTime;

            document.wdgc.WFCacheFile=ngformproperty.WFCacheFile;

            document.wdgc.FileCompressed = true;

            document.wdgc.FileUrl=ngformproperty.FileUrl;

            var fvb=getFieldValueBag(document.wdgc.NGClassString,false);

            if(typeof fvb !='undefined' && Trim(fvb)!='')
                document.wdgc.FieldValueBag=fvb;
        }
        else{
            alert("form view plugin not installed");
        }
    }
    else if(wiproperty.formType=="NGFORM" && (typeof document.wdgc != 'undefined') && document.wdgc&&ngformproperty.type=="applet")
    {
        if(!bDefaultNGForm){            
        } else {
            //var fvb=getFieldValueBag(document.wdgc.getNGClassString(),true);
            var fvb = strAttribData;
            //alert("Going to set data \n\n"+fvb);
            if(document.wdgc){
                if(typeof fvb !='undefined' && Trim(fvb)!='')
                    document.wdgc.setFieldValueBag(fvb);
            }
        }
    }
}

function detectJRE()
{
    if(!deployJava.versionCheck("1.4.1_02+"))
    {
        alert(ALERT_GET_JRE);
        if(getJRE == 'Y')
            window.open("http://www.java.com");
    }
}

function sendAjaxRequest(fromTab, toFile){
      

    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;

    var docWindow=getWindowHandler(winList,"tableGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var todoWindow = getWindowHandler(winList,"todoGrid");

    var vAction = "";
    if(fromTab == 'doc'){
        vAction = "doc";
    }
    if(fromTab == 'form'){
        vAction = "form";
    }
    if(fromTab == 'todo'){
        vAction = "todo";
    }
    if(fromTab == 'exp'){
        vAction = "exp";
    }

    var requestString=encode_utf8("pid")+"="+encode_utf8(pid)+"&"+encode_utf8("wid")+"="+encode_utf8(wid)+"&"+encode_utf8("taskid")+"="+encode_utf8(taskid)+"&height="+wiHeight+"&Action="+vAction+"&WD_UID="+WD_UID;
        
    if (window.XMLHttpRequest)
        ajaxReq= new XMLHttpRequest();
    else if (window.ActiveXObject) {
        ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
    }
    var url = toFile;
    url = appendUrlSession(url);
    ajaxReq.open("POST", url, false);
    ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    ajaxReq.send(requestString);
    
    if (ajaxReq.status == 200 && ajaxReq.readyState == 4) {
        var respText = ajaxReq.responseText;

        if(fromTab == 'doc'){
            respText = respText.substring(respText.indexOf('<doccontainer>')+14,respText.lastIndexOf('</doccontainer>'));
            
            document.getElementById('docDiv').innerHTML = respText;
            var ifrm=document.getElementById('docviewer');
            if(ifrm)
                ifrm.style.height=formH-40;
            //setDocumentWidth();  //puneet tbd
            var doccheckoutcount = document.getElementById('wdesk:doccheckoutcount').value;
            window_workdesk.docCheckOut = parseInt(doccheckoutcount);
        }
        else if (fromTab == 'form'){
            
            if(wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"){
            // isFormLoaded=false;
            }else
                isFormLoaded=true;
            ngattribute = respText.substring(respText.indexOf('<ngAttributeCont>')+17,respText.lastIndexOf('</ngAttributeCont>'));

            ngattribute=decode_utf8(ngattribute);
            respText = respText.substring(respText.indexOf('<formcontainer>')+15,respText.lastIndexOf('</formcontainer>'));

            /*var debugwin = window.open('','');
          debugwin.document.write(respText);*/

            document.getElementById('formDiv').innerHTML = respText;
        //setTimeout("settingsFn()",0);  //puneet tbd
        }
        else if (fromTab == 'exp'){
            respText = respText.substring(respText.indexOf('<expcontainer>')+14,respText.lastIndexOf('</expcontainer>'));
            document.getElementById('expDiv').innerHTML = respText;
        }
        else if (fromTab == 'todo'){
            respText = respText.substring(respText.indexOf('<todocontainer>')+15,respText.lastIndexOf('</todocontainer>')); 
            document.getElementById('todoDiv').innerHTML = respText;
        }
        else if (fromTab == 'task'){
            if(wiproperty.TaskFormType=="NGFORM" && taskngformproperty.type=="applet"){
            // isTaskFormLoaded=false;
            }else
                isTaskFormLoaded=true;
            taskngattribute = respText.substring(respText.indexOf('<taskngAttributeCont>')+17,respText.lastIndexOf('</taskngAttributeCont>'));

            taskngattribute=decode_utf8(taskngattribute);
            respText = respText.substring(respText.indexOf('<taskcontainer>')+15,respText.lastIndexOf('</taskcontainer>')); 
            document.getElementById('taskDiv').innerHTML = respText;
        }
    }
    else if(ajaxReq.status==12029){
        alert(ERROR_SERVER); 
    }
}

function ReloadHandler()
{

    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;

    var docWindow=getWindowHandler(winList,"tableGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var todoWindow = getWindowHandler(winList,"todoGrid");
    var taskWindow = getWindowHandler(winList,"taskGrid");

    if(todoWindow)
    {
        if(todoWindow.windowProperty.winloc=="M")
            sendAjaxRequest('todo', "todo_main_view.jsf");
        else
            todoWindow.sendAjaxRequest('todo', "todo_main_view.jsf");
    }
    if(taskWindow)
    {
        if(todoWindow.windowProperty.winloc=="M")
            sendAjaxRequest('task', "task_main_view.jsf");
        else
            todoWindow.sendAjaxRequest('task', "task_main_view.jsf");
    }
    if(excpWindow)
    {
        if(excpWindow.windowProperty.winloc=="M")
        {
            sendAjaxRequest('exp', "exp_main_view.jsf");
        // InitHWExp();  //puneet tbd
        }
        else
        {
            excpWindow.sendAjaxRequest('exp', "exp_main_view.jsf");
            excpWindow.InitHWExp();
        }
    }
    if(docWindow)
    {
        if(docWindow.windowProperty.winloc=="M")
        {
            sendAjaxRequest('doc', "doc_main_view.jsf");
            InitHWDoc();
        }
        else
        {
            docWindow.sendAjaxRequest('doc', "doc_main_view.jsf");
        //docWindow.InitHWDoc();  //puneet tbd
        }
    }
}

function TestReloadHandler()
{
    sendAjaxRequest('exp', "exp_main_view.jsf");
}

function rejectExcep_open(){
    var expWtype = "";
    /*var pid = document.getElementById('wdesk:pid').value;
    var wid = document.getElementById('wdesk:wid').value;*/
    var expId = document.getElementById('wdesk:selexpid').value;
    var expName = document.getElementById('wdesk:selexpname').value;
    if(document.getElementById('wdesk:expWtype'))
        expWtype = document.getElementById('wdesk:expWtype').value;
    //url = sContextPath+'/faces/workitem/operations/exception/clear_exception.jsp?Pid='+encode_utf8(pid)+'&Wid='+encode_utf8(wid)+'&expWtype='+expWtype+'&expId='+expId+'&expName='+encode_utf8(expName);
    url = '/webdesktop/components/workitem/operations/exception/clear_exception.jsf?Pid='+encode_utf8(pid)+'&Wid='+encode_utf8(wid)+'&Taskid='+encode_utf8(taskid)+'&expWtype='+expWtype+'&expId='+expId+'&expName='+encode_utf8(expName)+'&WD_UID='+WD_UID;

    var expHist = document.getElementById('expHistDiv');
    expHist.className = 'wdWDivBorderDis wdleftpadding10 wdrightpadding10 wdbottompadding10';
    var histTable = document.getElementById("wdesk:histGrid");
    histTable.style.display = 'none';
    var expCmnt = document.getElementById("wdesk:comnt");
    expCmnt.value = "";
    expCmnt.style.display = 'inline';
    var iframeExp=document.getElementById('expOpFrm');
    url = appendUrlSession(url);
    iframeExp.src = url;
    document.getElementById("wdesk:detailHdr").innerHTML = TITLE_REJECT_EXCEPTION;
    document.getElementById("wdesk:lblDetails").innerHTML = LABEL_EXCEPTION_COMMENTS;
    //document.getElementById("wdesk:rejectbtn").src = sContextPath+"/webtop/"+PATH+"images/ok.gif";
    document.getElementById("wdesk:rejectbtn").onclick = Function("rejectExpBtn();return false;");
    document.getElementById("wdesk:modifybtn").style.display = 'none';
    document.getElementById("wdesk:clearbtn").style.display = 'none';
    document.getElementById("wdesk:responsebtn").style.display = 'none';
}


function prepareWdeskDefaultMenu()
{
     menuobj=document.getElementById("wdsubmenu");//document.getElementById? document.getElementById("describe") : document.all? document.all.describe : document.layers? document.dep1.document.dep2 : ""
     if(typeof pageDirection != 'undefined'){
        if(pageDirection == 'rtl'){
            menuobj.style.left = "10px";
        } else {
            menuobj.style.right = "10px";         
        }
     } else {
         menuobj.style.right = "10px";         
     }
     convertIT('myMenuID');
}

function openToDOWin(winlocation,event){
    event = (typeof event == 'undefined')? null: event;
    var pid="";
    var wid="";
    var taskid="";
    var winloc="";
    if(document.getElementById('wdesk')){
     pid=document.getElementById('wdesk:pid');
     wid=document.getElementById('wdesk:wid');
     taskid=document.getElementById('wdesk:taskid');
     winloc='N';
    }
    else{
     pid=window.opener.document.getElementById('wdesk:pid');
     wid=window.opener.document.getElementById('wdesk:wid');
     taskid=window.opener.document.getElementById('wdesk:taskid');
     winloc='T';
    }

    var requestString=sContextPath+"/components/workitem/view/todowin.jsf?Action=todo&pid="+encode_utf8(pid.value)+"&wid="+encode_utf8(wid.value)+"&taskid="+encode_utf8(taskid.value)+"&winloc="+winloc+"&randNum="+Math.random();

    link_popup(requestString,"todoGrid",'scrollbars=no,resizable=yes,status=yes,width='+window1W+',height='+window1H+',left='+window1Y+',top='+window1X);
    cancelBubble(event);
    return false;
}

function openTaskWin(winlocation,event){
    event = (typeof event == 'undefined')? null: event;
    var pid="";
    var wid="";
    var taskid="";
    var winloc="";
    if(document.getElementById('wdesk')){
        pid=document.getElementById('wdesk:pid');
        wid=document.getElementById('wdesk:wid');
        taskid=document.getElementById('wdesk:taskid');
        winloc='N';
    }
    else{
        pid=window.opener.document.getElementById('wdesk:pid');
        wid=window.opener.document.getElementById('wdesk:wid');
        taskid=window.opener.document.getElementById('wdesk:taskid');
        winloc='T';
    }

    var requestString=sContextPath+"/components/workitem/view/taskwin.jsf?Action=task&pid="+encode_utf8(pid.value)+"&wid="+encode_utf8(wid.value)+"&taskid="+encode_utf8(taskid.value)+"&winloc="+winloc+"&randNum="+Math.random();

    link_popup(requestString,"taskGrid",'scrollbars=no,resizable=yes,status=yes,width='+window1W+',height='+window1H+',left='+window1Y+',top='+window1X);
    cancelBubble(event);
    return false;
}

function openForm(winlocation,event){
    event = (typeof event == 'undefined')? null: event;
    var pid="";
    var wid="";
    var taskid="";
    var winloc="";
    if(document.getElementById('wdesk')){
         pid=document.getElementById('wdesk:pid');
         wid=document.getElementById('wdesk:wid');
         taskid=document.getElementById('wdesk:taskid');
         winloc='N';
    }
    else{
         pid=window.opener.document.getElementById('wdesk:pid');
         wid=window.opener.document.getElementById('wdesk:wid');
         taskid=window.opener.document.getElementById('wdesk:taskid');
         winloc='T';
    }
    var requestString=sContextPath+"/components/workitem/view/wiform.jsf?Action=form&pid="+encode_utf8(pid.value)+"&wid="+encode_utf8(wid.value)+"&taskid="+encode_utf8(taskid.value)+"&winloc="+winloc+"&randNum="+Math.random()+"&width="+window1W;

    link_popup(requestString,"formGrid","scrollbars=no,status=yes,resizable=Yes,width="+window1W+",height="+window1H+",left="+window1Y+",top="+window1X);
    cancelBubble(event);
    return false;
}

function openExceptionWin(winlocation,event){
    event = (typeof event == 'undefined')? null: event;
    var pid="";
    var wid="";
    var taskid="";
    var winloc="";
    if(document.getElementById('wdesk')){
         pid=document.getElementById('wdesk:pid');
         wid=document.getElementById('wdesk:wid');
         taskid=document.getElementById('wdesk:taskid');
         winloc='N';
    }
    else{
         pid=window.opener.document.getElementById('wdesk:pid');
         wid=window.opener.document.getElementById('wdesk:wid');
         taskid=window.opener.document.getElementById('wdesk:taskid');
         winloc='T';
    }
    var wdViewMode = document.getElementById('wdView').value;
    var requestString="Action=exp&pid="+encode_utf8(pid.value)+"&wid="+wid.value+"&taskid="+taskid.value+"&expWtype=new"+"&winloc="+winloc+"&randNum="+Math.random()+"&height="+window1H+"&width="+window1W+"&wdView="+wdViewMode;
    url = sContextPath+"/components/workitem/view/exception.jsf?"+requestString;

    var win = link_popup(url,'exceptionGrid','resizable=yes,status=yes,scrollbars=yes,width='+window1W+',height='+window1H+',left='+window1Y+',top='+window1X+',visibility=show',windowList,false);
    cancelBubble(event);
}

function openDocumentWin(winlocation,event){
    event = (typeof event == 'undefined')? null: event;
    var pid="";
    var wid="";
    var taskid="";
    var winloc="";
    if(document.getElementById('wdesk')){
     pid=document.getElementById('wdesk:pid');
     wid=document.getElementById('wdesk:wid');
     taskid=document.getElementById('wdesk:taskid');
     winloc='N';
    }
    else{
     pid=window.opener.document.getElementById('wdesk:pid');
     wid=window.opener.document.getElementById('wdesk:wid');
     taskid=window.opener.document.getElementById('wdesk:taskid');
     winloc='T';
    }
    
    if(document.getElementById('IsDefaultDoc') != null){
        var isDefaultDoc=document.getElementById('IsDefaultDoc').value;
    }
    //Bug 59916
    if(document.getElementById('IsDefaultDocType') != null){
        var IsDefaultDocType=document.getElementById('IsDefaultDocType').value;
    }

    var requestString=sContextPath+"/components/workitem/view/document.jsf?Action=doc&pid="+encode_utf8(pid.value)+"&wid="+encode_utf8(wid.value)+"&taskid="+encode_utf8(taskid.value)+"&winloc="+winloc+"&randNum="+Math.random()+"&Width="+window1W+"&Height="+window1H+"&IsDefaultDoc="+isDefaultDoc+"&IsDefaultDocType="+IsDefaultDocType;//Bug 59916

    link_popup(requestString,"tableGrid","scrollbars=no,status=yes,resizable=no,width="+window1W+",height="+window1H+",left="+window1Y+",top="+window1X,windowList,false);
    cancelBubble(event);
    return false;
}

function deleteDoc(documentIndex, bShowAlert){
    bShowAlert = (typeof bShowAlert == 'undefined') ? true : bShowAlert;
	if(!deleteDocument(strprocessname, stractivityName))      //  Bug 67762
        return false;
    if(bShowAlert && !confirm(COMMENT_DELETE_DOCUMENT))
       return false;
    var objCombo = document.getElementById('wdesk:docCombo');
    var docIndex; 
    if(documentIndex )
    docIndex = documentIndex;     
    else 
    docIndex = objCombo.value;   
    var docName=objCombo.options[objCombo.options.selectedIndex].text;
    var pid=document.getElementById('wdesk:pid');
    var wid=document.getElementById('wdesk:wid');
    var taskid=document.getElementById('wdesk:taskid');
    var requestString='docindex='+docIndex+'&docname='+encode_utf8(docName)+'&pid='+encode_utf8(pid.value)+'&wid='+wid.value+'&taskid='+taskid.value;
    
    var ajaxReq;
    if (window.XMLHttpRequest) {
         ajaxReq= new XMLHttpRequest();
    } else if (window.ActiveXObject) {
          ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
        }
     var url = "../ajaxdeleteDoc.jsf";
    url = appendUrlSession(url);
    ajaxReq.open("POST", url, false);
    ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    ajaxReq.send(requestString);
    if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
        if(objCombo.options.length==1){
                objCombo.options.length = 0;
                document.getElementById("wdesk:docList").style.display="none";
                document.getElementById("wdesk:ifrm").style.display="none";
                document.getElementById("wdesk:ivapp").style.display="none";
                //document.getElementById("wdesk:noDocText2").style.display="inline";
                document.getElementById("wdesk:noDocDiv").style.display="inline";
                if(document.getElementById('docOptionsDiv') != null){
                    document.getElementById('docOptionsDiv').style.display="";
                }
          }
          else if(objCombo.options.length>1){
                for(var i=0;i<objCombo.options.length;i++)
                {
                        if(objCombo.options[i].value==docIndex)
                                objCombo.options[i]=null;
                }
                objCombo.selectedIndex=0;
                var docIndex = objCombo.value;
                reloadapplet(docIndex);
          }
         var window_workdesk='';
         if(windowProperty.winloc=="M")
             window_workdesk=window
         else if(windowProperty.winloc=="T")
            window_workdesk=window.opener.opener;
        else
            window_workdesk=window.opener;
        var isDocListWindow=getWindowHandler(window_workdesk.windowList,"DocumentList");
        if (isDocListWindow){
            isDocListWindow.refreshDocList("");
        }
        deleteDocPostHandler(docIndex);
     }
     else
        {
            if(ajaxReq.status==598)
                {setmessageinDiv(ajaxReq.responseText,"false",3000);
                    //alert(ajaxReq.responseText);
                }
            else if(ajaxReq.status == 599)                
                  window.open(sContextPath+"/error/errorpage.jsf?msgID=4020",reqSession);
            else if(ajaxReq.status == 400)
                alert(INVALID_REQUEST_ERROR);
             else if(ajaxReq.status==12029){
                //alert(ERROR_SERVER);
                window.open(sContextPath+"/error/errorpage.jsf?msgID=4020",reqSession);
            }
            else
                  alert(ERROR_DATA);
              deleteDocPostHandler(-1);
        }
}

var callfromVar="";
var bNgFormRefresh = false;
function mainSave(from,type, subType){       
    if(isFormLoaded==false)
        return;
    
    if(typeof isTaskFormLoaded != "undefined" && isTaskFormLoaded==false)//Form handling for Task Data
        return;
    
    var ngformIframe = document.getElementById("ngformIframe");                    
    if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm){
        try{
            var isValidated = false;
            if(bAllDeviceForm){
                isValidated = ngformIframe.contentWindow.eval("isComponentValidated('Y')");//Bug 82140
            } else {
                isValidated = ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.isComponentValidated()");                                
            }
            if(!isValidated){
                return;
            }
        }catch(e){}
    }

    //Form handling for Task Data
    var taskngformIframe = document.getElementById("taskngformIframe");                    
    if((wiproperty.TaskFormType == "NGFORM") && (taskngformproperty.type == "applet") && !bDefaultNGForm){
        try{
            var isValidated = taskngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.isComponentValidated()");                                
            if(!isValidated){
                return;
            }
        }catch(e){}
    }

    var formWindow=getWindowHandler(windowList,"formGrid");
    if(!form_cutomValidation("S"))
        return false;
    if(typeof from =='undefined')
        from = '';
   // var formWindow=getWindowHandler(windowList,"formGrid");
    if(!saveFormdata('formData','',from)){
        return false;
        }
    callfromVar=from; 
    if(SaveClick()){
        if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm && !bAllDeviceForm){
            try{
                type = (typeof type == 'undefined')? '': type; 
                subType = (typeof subType == 'undefined')? '': subType; 
                
                if(type == 'UnloadWIEvent'){
                    // when other workitem is loaded from workitemlist
                    if(formIntegrationApr == "4"){
                        window.WiDummySaveStruct.Type = type;
                        window.WiDummySaveStruct.SubType = subType; 
                        
                        ngformIframe.contentWindow.saveFormStarted("S","N");
                        //ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('S','N')");
                    } else {  
                        ngformIframe.contentWindow.clickLink('cmdNgFormRefresh');
                    }
                    
                    bNgFormRefresh = true;
                } else if(type == 'beforeunload'){
                    /*  Window is unloaded 
                        case1: on window close directly without saving form changes
                        case2: on parent window refresh
                    */                    
                } else {
                    // on workitem save from menu option or on window close from menu option or on prev-next
                    var ngformIframeWindow = ngformIframe.contentWindow;
                    
                    var wiDummySaveStructWinRef = ngformIframeWindow;                    
                    if(formIntegrationApr == "4"){
                        wiDummySaveStructWinRef = window;
                    }
                    
                    wiDummySaveStructWinRef.WiDummySaveStruct.Type = type;
                    wiDummySaveStructWinRef.WiDummySaveStruct.SubType = subType; 
                        
                    if(formIntegrationApr == "4"){
                        //ngformIframe.contentWindow.saveFormStarted("S","N");
                        ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('S','N')");
                    } else {                                   
                        ngformIframe.contentWindow.clickLink('cmdNgFormRefresh');
                    }
                }                
            }catch(e){
                
            }
        } else if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && (bAllDeviceForm || bAllDeviceTaskForm)){
            if(bCustomIForm){
                if(typeof ngformIframe.contentWindow.customIFormHandler != 'undefined'){
                    ngformIframe.contentWindow.customIFormHandler('S');
                }
            }
            
            var returnVal=for_save("mainSave",from);
            if(returnVal=='success'){
                RefreshClientComp();
                refreshTaskList();
            }
        } else {
            if(typeof formWindow!='undefined' && wiproperty.formType=="NGFORM"){                
                var retVal = formWindow.document.wdgc.SaveData('S');    //js error when form opens in new window
                if(retVal != 1)
                    return 'false';
            }
            showMasking();
            var returnVal=for_save("mainSave",from);
            if(returnVal=='success'){
                RefreshClientComp();
                refreshTaskList();
            }else if(returnVal == 'closeFail'){
                return returnVal;
            }
            hideMasking();
        }
    
    //Form handling for Task Data
    var taskWindow=getWindowHandler(windowList,"taskGrid");
    if((wiproperty.TaskFormType == "NGFORM") && (taskngformproperty.type == "applet") && !bDefaultNGForm){
            try{
                type = (typeof type == 'undefined')? '': type; 
                subType = (typeof subType == 'undefined') ? '' : subType;
                
                if(type == 'UnloadWIEvent'){
                    // when other workitem is loaded from workitemlist
                    taskngformIframe.contentWindow.clickLink('cmdTaskNgFormRefresh');
                    bNgFormRefresh = true;
                } else if(type == 'beforeunload'){
                /*  Window is unloaded 
                        case1: on window close directly without saving form changes
                        case2: on parent window refresh
                    */                    
                } else {
                    // on workitem save from menu option or on window close from menu option
                    var taskngformIframeWindow = taskngformIframe.contentWindow;
                    taskngformIframeWindow.WiDummySaveStruct.Type = type;     
                    taskngformIframeWindow.WiDummySaveStruct.SubType = subType;
                    taskngformIframe.contentWindow.clickLink('cmdTaskNgFormRefresh');
                }                
            }catch(e){
                
            }
        } else {
            //PRDP Bug 62299 starts
           /* if(typeof taskWindow!='undefined' && wiproperty.TaskFormType=="NGFORM"){                
                var retVal = taskWindow.document.wdgc.SaveData('S');    //js error when form opens in new window
                if(retVal != 1)
                    return 'false';
            }
            var returnVal=for_save("mainSave",from);
            if(returnVal=='success'){
                RefreshClientComp();
                refreshTaskList();
            }else if(returnVal == 'closeFail'){
                return returnVal;
            }*/
            //PRDP Bug 62299 ends
        }
    
   }
}

function saveNGFormHandler(){    
    var responseJson = eval("("+this.req.responseText+")");    
    this.IsValidateForm = responseJson.IsValidateForm;
}

function saveTaskNGFormHandler(){    
    var responseJson = eval("("+this.req.responseText+")");    
    this.IsValidateForm = responseJson.IsValidateForm;
}

function form_cutomValidation(opt){
    if(typeof customValidation!='undefined' &&!customValidation(opt)){
            return false;
    }
    var formWindow=getWindowHandler(windowList,"formGrid");
    var taskWindow=getWindowHandler(windowList,"taskGrid");
    
    if(typeof formWindow!='undefined' && wiproperty.formType=="NGFORM"){
        if(navigator.appName.indexOf("Netscape") != -1 && ngformproperty.type=="applet"){
            if(isEmbd=="N"){
                if(document.getElementById('formDiv') && document.getElementById('formDiv').style.display=='none'){
                    alert(LABEL_ALERT_MOZILA_FORM_HIDE_DONE);
                    return false;
                }
            }else if(isEmbd=="Y"){
                if(document.getElementById('formDiv').style.display=='none'){
                    alert(LABEL_ALERT_MOZILA_FORM_HIDE_DONE);
                    return false;
                }
            }
        }
        
        var validationStatus = false;
        try{
            if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm){
                var ngformIframe = document.getElementById("ngformIframe");	
                if(ngformIframe != null){
                    if(bAllDeviceForm){
                        validationStatus = ngformIframe.contentWindow.eval("fireFormValidation('"+opt+"')");	
                    } else {                    
                        if(formIntegrationApr == "4"){
                            validationStatus = ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('"+opt+"','Y',false)");	                            
                        } else {                            
                            validationStatus = ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('"+opt+"')");	
                        }
                    }
                }
            } else {            
                validationStatus = document.wdgc.ValidateControls(opt);
            }
        } catch(e){
            alert(UNSUCCESSFUL_ALERT);
        }
        
        if(validationStatus == false){
            if(typeof taskWindow != 'undefined'){
                try{
                    switchTab('tab-6');
                } catch(e){}                
            }            
            return false;
        }
    }

    //Handling for Task    
    if(typeof taskWindow!='undefined' && wiproperty.TaskFormType=="NGFORM"){
        if(navigator.appName.indexOf("Netscape") != -1 && taskngformproperty.type=="applet"){
            if(isEmbd=="N"){
                if(document.getElementById('taskDiv') && document.getElementById('taskDiv').style.display=='none'){
                    alert(LABEL_ALERT_MOZILA_FORM_HIDE_DONE);
                    return false;
                }
            }else if(isEmbd=="Y"){
                if(document.getElementById('taskDiv').style.display=='none'){
                    alert(LABEL_ALERT_MOZILA_FORM_HIDE_DONE);
                    return false;
                }
            }
        }
        
        var validationStatus = false;
        try{
            if((wiproperty.TaskFormType == "NGFORM") && (taskngformproperty.type == "applet") && !bDefaultNGForm){
                var taskngformIframe = document.getElementById("taskngformIframe");	
                if(taskngformIframe != null){
                    if(bAllDeviceTaskForm){//Bug 79176
                        validationStatus = taskngformIframe.contentWindow.eval("fireFormValidation('"+opt+"')");	
                    } else {
                        validationStatus = taskngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('"+opt+"')");	
                    }
                    
                    if(validationStatus == false){
                        try{
                            switchTab('tab-7');
                        } catch(e){}                
                    }
                }
            } else {            
                validationStatus = document.wdgc.ValidateControls(opt);
            }
        } catch(e){
            alert(UNSUCCESSFUL_ALERT);
        }
        
        if(validationStatus == false)
            return false;
    }

    return true;
}

function validatData(from){
    var status="false";
    var formWindow=getWindowHandler(windowList,"formGrid");
    if(typeof formWindow!='undefined'&&typeof formWindow!=null){
                if(wiproperty.formType=="NGFORM"){
                        //status=formWindow.saveNGForm();
                }
                else if(wiproperty.formType=="HTMLFORM"){
                    status=formWindow.htmlFormOk();
          }
          else if(wiproperty.formType=="VARIANTFORM"){//added for Variant : 17 Oct 13
            status=formWindow.htmlFormOk();
            if(status=="false" && from!="undefined" && (from=='introduce' || from=='done' )){
                status = formWindow.validateMandatoryFields();
            }
        }
               else if(wiproperty.formType=="CUSTOMFORM"){
                    status=formWindow.htmlFormOk(formWindow.frames['customform']);
          }
        }
	return status;//false for valid ,true for invalid
}

function validateTaskData(from){
    var status="false";
    var taskWindow=getWindowHandler(windowList,"taskGrid");
    if(typeof taskWindow!='undefined'&&typeof taskWindow!=null){
        if(wiproperty.TaskFormType=="NGFORM"){
        //status=formWindow.saveNGForm();
        }
        else if(wiproperty.TaskFormType=="HTMLFORM"){
            status=taskWindow.htmlFormOk();
        }
    }
    return status;//false for valid ,true for invalid
}

function getFormValuesForAjax(workdeskform)
{
    var str = "";
    var valueArr = null;
    var val = "";
    var cmd = "";
    var fobj = workdeskform;
    var stateName = "javax.faces.ViewState";
    var formid=fobj.id;
    //Omniflow 5.0 support using wi_object
    if(WIObjectSupport.toUpperCase()=='Y'){

        for(var i in wi_object.attribute_list)
        {

            if(wi_object.attribute_list[i].modified_flag ==  true)
            {
                str += encodeURIComponent("wdesk:"+wi_object.attribute_list[i].name) +  "=" + encodeURIComponent(Trim(wi_object.attribute_list[i].value)) + "&";
            }
            else
            {
                if(fobj.elements[wi_object.attribute_list[i].name])
                    str += encodeURIComponent("wdesk:"+wi_object.attribute_list[i].name) +  "=" + encodeURIComponent(Trim(fobj.elements[wi_object.attribute_list[i].name].value)) + "&";
            }
        }
    }
   // else{
   if(formid=='wdesk'){
        for(var i = 0;i < fobj.elements.length;i++)
        {
            switch(fobj.elements[i].type)
            {
                case "textarea":
                case "text":
                   var elementName = fobj.elements[i].name;
                    var elementValue = Trim(fobj.elements[i].value);
                    try {
                        elementValue = changeToServerTimeZone(elementName, elementValue, ServerTimeZone, ClientTimeZone);
                    }catch(exc){
                        // error in client method, use element's default value.
                    }
                    str += encodeURIComponent(elementName) +  "=" + encodeURIComponent(Trim(elementValue)) + "&";
                    
                    break;
                case "select-one":
                    if(fobj.elements[i].selectedIndex!=-1)
                        str += encodeURIComponent(fobj.elements[i].name) + "=" + encodeURIComponent(fobj.elements[i].options[fobj.elements[i].selectedIndex].value) + "&";
                    break;
                case "checkbox":
                    if(fobj.elements[i].checked)
                    {
                        str += encodeURIComponent(fobj.elements[i].name) + "=" +encodeURIComponent(fobj.elements[i].value) + "&";
                    }
                    break;
                case "hidden":
                    if(!(fobj.elements[i].name== stateName))
                    {
                        str +=encodeURIComponent(fobj.elements[i].name) +  "=" + encodeURIComponent(fobj.elements[i].value) + "&";
                    }
                    break;
            }
        }
    }
return str;
}

function setmessageinDiv(m,rem,dtime){
    if((isEmbd == 'Y') && (wdView == "em") && document.getElementById("wdesk:indicatorPG"))
        document.getElementById("wdesk:indicatorPG").style.display = "inline";

    if(m=='') 
    {
        var messageImg = document.getElementById("wdesk:waitIndicator");
        messageImg.style.left=document.body.clientWidth/2-40 + "px";
        messageImg.style.display='inline';
    } else {
        var messageDiv=document.getElementById("wdesk:messagediv");
        if(messageDiv){
            var messagePG = document.getElementById("wdesk:messagedivPG");
            if(messagePG){                
                var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1; 
                if(isSafari){
                    messagePG.style.width = "auto";
                    messagePG.style.display = "";
                } else {
                    messagePG.style.display = "";
                }
                messageDiv.innerHTML= m; //"<span style='background-color :#FF6347;font :bold 10pt arial;color :#fff ;' >"+m+"</span>";
            }
        }
    }

    if(!dtime) {
        dtime=3000;
    }
    if(rem) {
        if(rem=="true") {
            setTimeout("removemessageFromDiv()",dtime);
        }
    }
}

function setmessageinDivSuccess(m,rem,dtime,value){
    if((isEmbd == 'Y') && (wdView == "em") && document.getElementById("wdesk:indicatorPG"))
        document.getElementById("wdesk:indicatorPG").style.display = "inline";

    if(m=='') 
    {
        var messageImg = document.getElementById("wdesk:waitIndicator");
        messageImg.style.left=document.body.clientWidth/2-40 + "px";
        messageImg.style.display='inline';
    } else {
        var messageDiv=document.getElementById("wdesk:successMessagediv");
        if(messageDiv){
            var messagePG = document.getElementById("wdesk:messagedivSuccessPG");
            if(messagePG){                
                var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1; 
                var isIE = ((navigator.appName=='Netscape')?false:true);
                if(isSafari || !isIE){
                    messagePG.style.width = "auto";
                    messagePG.style.display = "";

                } else {
                    messagePG.style.display = "";
                }
                messageDiv.innerHTML= m; //"<span style='background-color :#FF6347;font :bold 10pt arial;color :#fff ;' >"+m+"</span>";
            }
        }
    }

    if(!dtime) {
        dtime=3000;
    }
    if(rem) {
        if(rem=="true") {
            value = typeof value == 'undefined'?'': value;
            setTimeout("removemessageFromDivSuccess('"+value+"')",dtime);
        }
    }
}

function removemessageFromDivSuccess(value){
    if((isEmbd == 'Y') && (wdView == "em") && document.getElementById("wdesk:indicatorPG"))
        document.getElementById("wdesk:indicatorPG").style.display = "none";

    var messageImg=document.getElementById("wdesk:waitIndicator");
    if(messageImg) {
        messageImg.style.display='none';
    }

    var messageDiv=document.getElementById("wdesk:successMessagediv");
    if(messageDiv) {
        var messagePG = document.getElementById("wdesk:messagedivSuccessPG");
        if(messagePG){
            messagePG.style.display = "none";
            messageDiv.innerHTML="";
        }
    }    
    value = typeof value == 'undefined'?'': value;
    if(value=='mainSave')
        setmessageinDivSuccess(WORKITEM_SAVED_SUCCESSFULLY,"true",3000);
}

function setFormValue(varName,varValue,varType){
    if(typeof varName == 'undefined' && typeof varValue == 'undefined' && typeof varType == 'undefined')
        return;
    var sDateFormat=wiproperty.sDateFormat;

    if(varType == 'undefined')
        varType = '';
    var formWindow=getWindowHandler(windowList,"formGrid");
    if(typeof formWindow!='undefined'){
       if(wiproperty.formType=="NGFORM"){
          var strvarName=varName;
                while(strvarName.indexOf('-')!=-1)
                    strvarName=strvarName.replace('-','.');
                if(varType  == NG_VAR_DURATION){
                    if(varValue == "")
                        varValue = "P0Y0M0DT0H0M0S";
                    else
                        varValue = durationLocaltoDb(varValue)
                }
                if(varType  == NG_VAR_DATE){
                    var validationStatus = ValidateDateFormat(varValue,sDateFormat,false);                    
                    if(validationStatus == 1)
                        {
                         varValue = LocalToDB(varValue,sDateFormat);
                        }
                }


                formWindow.setNGFormValue(strvarName,varValue);
        }
        else if(wiproperty.formType=="HTMLFORM"){
            if(formWindow.document.getElementById("wdesk:"+varName))
                formWindow.document.getElementById("wdesk:"+varName).value=varValue;
        }
         else if(wiproperty.formType=="VARIANTFORM"){//added for Variant : 17 Oct 13
            if(formWindow.document.getElementById("wdesk:"+varName))
                formWindow.document.getElementById("wdesk:"+varName).value=varValue;
        }
       else if(wiproperty.formType=="CUSTOMFORM"){
           if(formWindow.frames['customform'].document.getElementById("wdesk:"+varName))
                formWindow.frames['customform'].document.getElementById("wdesk:"+varName).value=varValue;
        }
    }
}
var showSuccessMsg=true;
function triggerDisplay(trigResponse,trigWin,todoId){
    var bBroadCastEvent = false;
        showSuccessMsg=true;
        var window_workdesk='';
        if(trigWin != 'todolist')
            todoId = '0';
        if(trigWin == 'action'){
            if(typeof document.forms['wdesk'] == 'undefined')
                window_workdesk = window.opener;
            else
                window_workdesk = window;
        }
       if(trigWin=='exception'){
            if(window.parent.windowProperty.winloc == 'M')
                    window_workdesk = window.parent;
            else if(window.parent.windowProperty == 'T')
                    window_workdesk = window.parent.opener.opener;
            else
                    window_workdesk = window.parent.opener;
        }
         else if(trigWin=='todolist'){
            if(windowProperty.winloc == 'M')
                window_workdesk = window;
            else if(windowProperty.winloc == 'T')
                window_workdesk = window.opener.opener;
            else
                window_workdesk = window.opener;
        }
        if(window_workdesk.SharingMode)
                bBroadCastEvent = true;
        var winList = window_workdesk.windowList;
        var docWindow = getWindowHandler(winList,"tableGrid");
        var formWindow = getWindowHandler(winList,"formGrid");
        var excpWindow = getWindowHandler(winList,"exceptionGrid");
       
        var pid = window_workdesk.document.getElementById('wdesk:pid').value;
        var wid = window_workdesk.document.getElementById('wdesk:wid').value;
        var taskid = window_workdesk.document.getElementById('wdesk:taskid').value;

        if((trigResponse[0].flag=='0') && (trigResponse[0].TriggerType=='S'))
        {
            if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm){
                var ngformIframe = window_workdesk.document.getElementById("ngformIframe");	
                if(ngformIframe != null){
                     if(typeof trigResponse[1] != 'undefined'){
                         if(window_workdesk.bAllDeviceForm){
                            ngformIframe.contentWindow.eval("com.newgen.iforms.setValue('"+trigResponse[1].attribXML+"')");	
                         } else {
                            ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.setValue('ngform','"+trigResponse[1].attribXML+"')");	
                         }
                     }
                }
            } else {            
                if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet")){
                   var formWin = getWindowHandler(window_workdesk.windowList,"formGrid");
                   
                   if(trigWin=='todolist'){
                        var assocField = document.getElementById("wdesk:todo"+todoId).alt;
                        document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
                        setFormData(assocField,trigResponse[0].status,'m');
                    }
                    
                   if(typeof formWin != 'undefined'){
                        if(typeof trigResponse[1] != 'undefined' && trigResponse[1] != null){
                            formWin.document.wdgc.setFieldValueBag(trigResponse[1].attribXML);
                        }
                   }
                } else {
                    if(trigWin=='todolist'){
                        var assocField = document.getElementById("wdesk:todo"+todoId).alt;
                        document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
                        setFormData(assocField,trigResponse[0].status,'m');
                    }
                    for(var i=1;i<trigResponse.length;i++){
                        var valSetter = trigResponse[i];
                        window_workdesk.setFormValue(valSetter.name,valSetter.value,valSetter.type);
                    }
                    if(bBroadCastEvent){
                        window_workdesk.broadcastSetDataEvent(JSON.stringify(trigResponse));
                    }
                }
            }
       }
       else if(trigResponse[0].flag=='0' && trigResponse[0].TriggerType == 'DE') {
            var thisWindow;
            var attribList = '', attributeData = '';
            for(var i=1;i<trigResponse.length;i++){
                var valSetter = trigResponse[i];
                attribList += valSetter.name + SEPERATOR1;
            }
            if(trigWin=='todolist'){
                thisWindow = 'Todo';
                url = '/webdesktop/components/workitem/view/htmlform.jsf?AttribList='+encode_utf8(attribList)+'&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid) + '&'+ encode_utf8("wdesk:pid")+"="+encode_utf8(pid)+"&"+encode_utf8("wdesk:wid")+"="+encode_utf8(wid)+"&"+encode_utf8("wdesk:taskid")+"="+encode_utf8(taskid)+'&PName=todo&Action=dataform';
            }
            else if(trigWin=='action'){
                thisWindow = 'act';
                var url='/webdesktop/components/workitem/view/htmlform.jsf?AttribList='+encode_utf8(attribList)+'&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid) + '&'+ encode_utf8("wdesk:pid")+"="+encode_utf8(pid)+"&"+encode_utf8("wdesk:wid")+"="+encode_utf8(wid)+"&"+encode_utf8("wdesk:taskid")+"="+encode_utf8(taskid)+'&PName=act&Action=dataform';
            }
            url = appendUrlSession(url);
            if(window.showModalDialog){
            attributeData = window.showModalDialog(url,'DEForm',"dialogWidth:"+600+"px;dialogHeight:"+500+"px;center:yes;");
            if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm){
                var DEObj = new net.ContentLoader('/webdesktop/ajaxDataEntryObj.jsf',dataEntryAjaxHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&attributeData='+encode_utf8(attributeData)+"&Op="+thisWindow,false);                
            } else {
                var DEObj = new net.ContentLoader('/webdesktop/ajaxDataEntryObj.jsf','',null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&attributeData='+encode_utf8(attributeData),false);
                if(windowProperty.winloc=='M'){
                    dataEntryHandler(attributeData,"Todo",bBroadCastEvent);
                }else
                    dataEntryHandler(attributeData,"TodoNet",bBroadCastEvent);
            }  
        }
            else{

                var url = '/webdesktop/components/workitem/view/htmlform.jsf';
                url = appendUrlSession(url);
                var wFeatures ='scrollbars=yes,status=yes,modal=yes,width='+window1W+',height='+window1H+',left='+window1Y+',top='+window1X;
                var listParam=new Array();
                listParam.push(new Array("AttribList",encode_ParamValue(attribList)));
                listParam.push(new Array("wid",encode_ParamValue(wid)));
                listParam.push(new Array("pid",encode_ParamValue(pid)));
                listParam.push(new Array("taskid",encode_ParamValue(taskid)));
                listParam.push(new Array("wdesk:pid",encode_ParamValue(pid)));
                listParam.push(new Array("wdesk:wid",encode_ParamValue(wid)));
                listParam.push(new Array("wdesk:taskid",encode_ParamValue(taskid)));
                listParam.push(new Array("Action",encode_ParamValue('dataform')));
                if(trigWin=='todolist')
                   listParam.push(new Array("PName",encode_ParamValue("todo")));
                else if(trigWin=='action')
                   listParam.push(new Array("PName",encode_ParamValue("act")));
                var win = openNewWindow(url,'FormView',wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);

                //var win = window.open(url,'FormView','scrollbars=yes,modal=yes,width='+window1W+',height='+window1H+',left='+window1Y+',top='+window1X);
            }
        }else if(trigResponse[0].flag=='0' && trigResponse[0].TriggerType == 'X') {
            if(trigResponse.length > 1){
                if(typeof excpWindow!='undefined'&&typeof excpWindow!=null){
                     updateExcpWin(excpWindow,trigResponse[1]);
                }
               
                if(trigResponse[1].isTrig == 'Y'){
                    var temp = trigResponse[1].trig;
                    //temp = eval("("+temp+")");
                    if(trigWin=='todolist')
                        triggerDisplay(temp,trigWin,todoId);
                    else
                        triggerDisplay(temp,trigWin);
                }
            }
        }else if((trigResponse[0].flag=='0') && (trigResponse[0].TriggerType=='G') && (trigResponse[0].description=='notExec'))
        {
           // var url = '/webdesktop/faces/workitem/view/doctype_gen_resp.jsp?wid='+wid+'&pid='+pid+'&fileName='+trigResponse[1].fileName+'&ArgList='+encode_utf8(trigResponse[1].genArgList)+'&trigWin='+trigWin+'&todoId='+todoId;
            //todo security
            var url = '/webdesktop/components/workitem/view/doctype_gen_resp.jsf';
            url = appendUrlSession(url);
            var wFeatures = 'resizable=no,status=yes,scrollbars=no,width=400,height=260,left='+(window.screen.availWidth - 400)/2+',top='+(window.screen.availHeight - 260)/2;
            var listParam=new Array();
            listParam.push(new Array("wid",encode_ParamValue(wid)));
            listParam.push(new Array("pid",encode_ParamValue(pid)));
            listParam.push(new Array("taskid",encode_ParamValue(taskid)));
            listParam.push(new Array("fileName",encode_ParamValue(trigResponse[1].fileName)));
            listParam.push(new Array("ArgList",encode_ParamValue(trigResponse[1].genArgList)));
            listParam.push(new Array("trigWin",encode_ParamValue(trigWin)));
            listParam.push(new Array("todoId",encode_ParamValue(todoId)));
            var win = openNewWindow(url,'doctype',wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);
            //window.open(url, 'doctype', 'resizable=no,scrollbars=no,width=400,height=260,left='+(window.screen.availWidth - 400)/2+',top='+(window.screen.availHeight - 260)/2);
        }
        
        else if((trigResponse[0].flag=='0') && (trigResponse[0].TriggerType=='G') && (trigResponse[0].description=='success')){
            if(docWindow){
                if((typeof trigResponse[1].GenerateResponseOnly != 'undefined') && (trigResponse[1].GenerateResponseOnly == "Y")){
                    var ref = document.getElementById("downloadiframe");
                    if(ref != null){
                        ref.src = "/webdesktop/servlet/getdocument?pid="+encode_utf8(trigResponse[1].pid)+"&wid="+trigResponse[1].wid+"&FileName="+encode_utf8(trigResponse[1].FileName)+"&GenerateResponseOnly=Y&CustomFileName="+encode_utf8(trigResponse[1].CustomFileName);
                    }
                } else {
                    addGenResDoc(trigResponse[1].docName,trigResponse[1].docIndex, trigResponse[1].diskName, docWindow, window_workdesk.DocOrgName);
                    if(bBroadCastEvent){
                        window_workdesk.broadcastAddGenResponseDocEvent(trigResponse[1].docName,trigResponse[1].docIndex, trigResponse[1].diskName, window_workdesk.DocOrgName);
                    }
                }
            }
       }
       else if((trigResponse[0].flag=='-1') && (trigResponse[0].TriggerType=='G') && (trigWin=='todolist')){
                var assocField = document.getElementById("wdesk:todo"+todoId).alt;
                document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
                setFormData(assocField,trigResponse[0].status,'m');
                window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),bBroadCastEvent,trigResponse[0].status,assocField);
            
            //alert(trigResponse[1].trigErr);
       }
       else if(trigResponse[0].flag=='0' && trigResponse[0].TriggerType == 'LA'){
         myBrowser = navigator.appName;
         //alert("launchApp 2");
         if(myBrowser != "Netscape"){
             window_workdesk.document.getElementById('BView').innerHTML = '<OBJECT ID="WDGC Control" WIDTH="1" name="wdgc1" HEIGHT="1" CODEBASE="'+window_workdesk.cabPath+'/webdesktop/webtop/webcontrol/wdgc.cab" CLASSID="CLSID:4088F53A-CAF6-11D6-B313-0000E8001307"></object>';
             if(!window_workdesk.installWebControl()){
             window_workdesk.installWdgc();
             window_workdesk.document.getElementById('BView').innerHTML = '<OBJECT ID="WDGC Control" WIDTH="1" name="wdgc1" HEIGHT="1" CODEBASE="'+window_workdesk.cabPath+'/webdesktop/webtop/webcontrol/wdgc.cab" CLASSID="CLSID:4088F53A-CAF6-11D6-B313-0000E8001307"></object>';
             }                          
             if(!window_workdesk.installWebControl()){
                 if(trigWin=='todolist'){
                    var assocField = document.getElementById("wdesk:todo"+todoId).alt;
                    document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
                    setFormData(assocField,trigResponse[0].status,'m');
                    window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),bBroadCastEvent,trigResponse[0].status,assocField);
                 }
                 return false;
             }
             window_workdesk.document.wdgc1.triggerLaunch(trigResponse[1].appname,decode_utf8(trigResponse[1].args));
             if(trigWin=='todolist'){
                 var assocField = document.getElementById("wdesk:todo"+todoId).alt;
                 document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
                 setFormData(assocField,trigResponse[0].status,'m');
                 window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),bBroadCastEvent,trigResponse[0].status,assocField);
             }
         }
         else if(trigWin=='todolist'){
                var assocField = document.getElementById("wdesk:todo"+todoId).alt;
                document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
                setFormData(assocField,trigResponse[0].status,'m');
                window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),bBroadCastEvent,trigResponse[0].status,assocField);
        }
    }else if(trigResponse[0].flag=='0' && trigResponse[0].TriggerType == 'U') {
        var url=trigResponse[1].url;
        var listParam=new Array();
        //alert("ActivityId="+stractivityId+",ProcessDefId="+strRouteId+",CabinetName="+cabName+",UserName="+userName+",SessionId="+sessionId+");
        listParam.push(new Array("WorkitemId",encode_ParamValue(wid)));
        listParam.push(new Array("ProcessInstanceId",encode_ParamValue(pid)));
        listParam.push(new Array("taskid",encode_ParamValue(taskid)));
        listParam.push(new Array("ActivityId",encode_ParamValue(stractivityId)));
        listParam.push(new Array("ProcessDefId",encode_ParamValue(strRouteId)));
        listParam.push(new Array("CabinetName",encode_ParamValue(cabName)));
        listParam.push(new Array("UserName",encode_ParamValue(userName)));
        listParam.push(new Array("SessionId",encode_ParamValue(sessionId)));
        
        var wFeatures = 'scrollbars=no,status=yes,width='+650+',height='+580+',left='+(window.screen.width - 650)/2+',top='+(window.screen.height - 650)/2+',resizable=yes';
        var url = appendUrlSession(url);
        var win = window.opener.openNewWindow(url,"",wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);
        win.focus();
    }
    else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'X')
    {
        showSuccessMsg=false; 
        alert(trigResponse[0].description);  
    }
    else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'S')
    {
        showSuccessMsg=false; 
        alert(trigResponse[0].description);  
    }
    else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'DE')
    {
        showSuccessMsg=false; 
        alert(trigResponse[0].description);  
    }
    else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'G')
    {
        showSuccessMsg=false; 
        if(trigResponse[1].trigErr!="" && trigResponse[1].trigErr !=null)
            alert(trigResponse[1].trigErr);
        else
            alert(trigResponse[0].description);  
    }
    else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'LA')
    {
        showSuccessMsg=false; 
        alert(trigResponse[0].description);  
    }
    else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'E')
    {
        showSuccessMsg=false; 
        alert(trigResponse[0].description);  
    }
    else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'M')
    {
        showSuccessMsg=false; 
        alert(trigResponse[0].description);  
    }else if(trigResponse[0].flag=='-1' && trigResponse[0].TriggerType == 'U') {
        showSuccessMsg=false; 
        alert(trigResponse[0].description); 
    } 
}

function broadcastWrapperToDoData(todoId,todoName,broadcastEvent,status,assocField){    
    if(broadcastEvent)
        broadcastToDoSetFormDataEvent(todoId,todoName,status,assocField,'m');
}

function getToDoName(todoId)
{
    var todoName;
    if(document.getElementById("wdesk:displayText"+todoId)){
        todoName = document.getElementById("wdesk:displayText"+todoId).value;
    }else{
        todoName = document.getElementById("wdesk:discriptionText"+todoId).title;
    }
    return todoName;
}

function reflectSetDataEvent(trigResponse){
    var window_workdesk = '';
    if(windowProperty.winloc == 'M')
        window_workdesk = window;
    else if(windowProperty.winloc == 'T')
        window_workdesk = window.opener.opener;
    else
        window_workdesk = window.opener;
    
    for(var i=1;i<trigResponse.length;i++){
        var valSetter = trigResponse[i];
        window_workdesk.setFormValue(valSetter.name,valSetter.value,valSetter.type);
    }
}

function dataEntryAjaxHandler(){
//    var broadcastEvent = false;
//    if(SharingMode){
//        broadcastEvent = true;
//    }
    var trigRes=eval("("+this.req.responseText+")");
    var attribXML = trigRes.attribXML;    
    dataEntryHandler(attribXML, trigRes.Op);
}

function dataEntryHandler(attribData,op,bBroadCastEvent)
{
    var window_workdesk="";
    var curWin="";
    var broadcastAttribData=attribData;
    
    if(op.indexOf('act')!= '-1'){
        if(op == 'actNet')
            curWin = window.opener;
        else
            curWin = window;
        if(typeof curWin.document.forms['wdesk'] == 'undefined')
            window_workdesk = curWin.opener;
        else
            window_workdesk = curWin;
    }
    else if(op.indexOf('Todo')!= '-1')
    {

        if(op == 'TodoNet'){
            curWin = window.opener;
        }
        else{

            curWin = window;
        }
        if(curWin.windowProperty.winloc == 'M')
            window_workdesk = curWin;
        else if(curWin.windowProperty.winloc == 'N')
            window_workdesk = curWin.opener;
        else
            window_workdesk = curWin.opener.opener;

    }
    else if(op == 'ENew'){
        if(windowProperty.winloc == 'T')
            window_workdesk = window.opener.opener;
        else if(windowProperty.winloc == 'N')
            window_workdesk = window.opener;
        else
            window_workdesk = window;
    }
    
    if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm){
        var formWindow = window_workdesk.getWindowHandler(window_workdesk.windowList,"formGrid");
        if(formWindow){
            var ngformIframe = formWindow.document.getElementById("ngformIframe");	
            if(ngformIframe != null){
                if(window_workdesk.bAllDeviceForm){
                    ngformIframe.contentWindow.eval("com.newgen.iforms.setValue('"+attribData+"')");	
                } else {
                    ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.setValue('ngform','"+attribData+"')");	
                }
            }
       }
    } else {  
        while(attribData != '')
        {
            var tmpData = attribData.substring(0,attribData.indexOf(SEPERATOR1));
            var name = tmpData.substring(0,tmpData.indexOf(SEPERATOR2));
            tmpData = tmpData.substring((tmpData.indexOf(SEPERATOR2) + 1));
            var value = tmpData.substring(0,tmpData.indexOf(SEPERATOR2));
            var type = tmpData.substring((tmpData.indexOf(SEPERATOR2) + 1));
            attribData = attribData.substring(attribData.indexOf(SEPERATOR1) + 1);
            if(typeof attribData == 'undefined')
                attribData = '';
            window_workdesk.setFormValue(name,value,type);
        }
    }
    if(bBroadCastEvent != undefined && bBroadCastEvent == true){
        window_workdesk.broadcastDataEntryEvent(broadcastAttribData,op);
    }
}

function installWdgc(){
    var myBrowser = navigator.appName;
    if(myBrowser != "Netscape"){
        
        var ret = confirm(CONFIRM_WEB_CONTROL_INSTALLATION);
        if(ret)
        {            
            url = '/webdesktop/components/workitem/view/waitwin_web_control.jsf?WD_UID='+WD_UID;
            url = appendUrlSession(url);
            window.showModalDialog(url,'', 'dialogWidth:380px;dialogHeight:150px;center:yes;');
            //window.location.reload();
        }
        
    }
}

function installWebControl()
{
	var isInstalled = false;
	var isNetscape = (navigator.appName.indexOf("Netscape") !=-1)?true:false;

	if(isNetscape)
	{
		return false;

	}
        else
	{
            if(document.all.wdgc1){
		isInstalled = (typeof document.all.wdgc1.isLoaded== 'undefined')?false:true;
            }
            else
            	isInstalled =false;
	}
	return isInstalled;
}

function setSt()// from waitwin.jsp
{
	window.status=LABEL_STATUS;
}

function setDataEntry()
{
    //alert("into setDataEntry");
    closeFrm = 'ok';
    if(launchFrm == 'null' || launchFrm == '')
        launchFrm = '';
    var inputObject,i;
    var inputObjectValue;
    var inputObjectType;
    var attributeData = '';
    var tmpObj = document.forms["dataEntryForm"];
    var status = htmlFormOk(window,"dataEntryForm");
    if(status !='false')
        return false;
    for ( i=0; i < document.forms["dataEntryForm"].elements.length ; i++)
    {
        if (document.forms["dataEntryForm"].elements[i].type == "text" || document.forms["dataEntryForm"].elements[i].type == "select-one")
        {
            inputObject = document.forms["dataEntryForm"].elements[i].name;
            inputObjectValue = Trim(document.forms["dataEntryForm"].elements[i].value);
            if(document.forms["dataEntryForm"].elements[i].type == "text")
                inputObjectType = document.forms["dataEntryForm"].elements[i].alt;
            else
                inputObjectType = "";
            attributeData +=inputObject.substring((inputObject.indexOf(SYMBOL_3) + 1))+SEPERATOR2+inputObjectValue+SEPERATOR2+inputObjectType+SEPERATOR1;
        }
    }    

     if(window.showModalDialog){
            window.returnValue=attributeData;
            window.close();
            return false;
    }
    else{
        //alert("attributeData"+attributeData);
        //alert(launchFrm);
        if(launchFrm == 'DEexpNew'){                                                            
            if(typeof window.opener.opener != 'undefined' && window.opener.opener != null){                
                window.opener.opener.tempExcpTrig[window.opener.opener.tempExcpTrig.length-1].value = attributeData;
                window.opener.closeExpOp();
                window.opener.opener.clickLinkSafari('wdesk:expRefresh');
            } else if(typeof window.opener.parent != 'undefined'){
                window.opener.parent.tempExcpTrig[window.opener.parent.tempExcpTrig.length-1].value = attributeData;
                window.opener.closeExpOp();
                window.opener.parent.clickLinkSafari('wdesk:expRefresh');
            }
            
            //window.opener.close();
            window.close();
        }
        else if(launchFrm == 'todo'){
            //dataEntryHandler(attributeData,'TodoNet');
            launchFrm = 'TodoNet';
            
            var window_workdesk='';
            if(typeof document.forms['wdesk'] == 'undefined'){
                window_workdesk = window.opener;
                if(window_workdesk.windowProperty.winloc == 'N'){
                    window_workdesk = window.opener.opener;            
                }
            } else{
                window_workdesk = window;
            }
            
            var pid = window_workdesk.document.getElementById('wdesk:pid').value;
            var wid = window_workdesk.document.getElementById('wdesk:wid').value;
            var taskid = window_workdesk.document.getElementById('wdesk:taskid').value;
            if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm){
                var DEObj = new net.ContentLoader('/webdesktop/ajaxDataEntryObj.jsf',dataEntryAjaxHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&attributeData='+encode_utf8(attributeData)+"&Op="+launchFrm,false);                
            } else {
                var DEObj = new net.ContentLoader('/webdesktop/ajaxDataEntryObj.jsf','',null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&attributeData='+encode_utf8(attributeData),false);
                dataEntryHandler(attributeData,'TodoNet');
            }
        } else if(launchFrm == 'act') {
            var window_workdesk='';            
            if(typeof document.forms['wdesk'] == 'undefined'){
                window_workdesk = window.opener;
                if(window_workdesk.windowProperty.winloc == 'N'){
                    window_workdesk = window.opener.opener;            
                }
            } else{
                window_workdesk = window;
            }
            
            var pid = window_workdesk.document.getElementById('wdesk:pid').value;
            var wid = window_workdesk.document.getElementById('wdesk:wid').value;
            var taskid = window_workdesk.document.getElementById('wdesk:taskid').value;
            if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm){
                var DEObj = new net.ContentLoader('/webdesktop/ajaxDataEntryObj.jsf',dataEntryAjaxHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&attributeData='+encode_utf8(attributeData)+"&Op="+launchFrm,false);                
            } else {
                var DEObj = new net.ContentLoader('/webdesktop/ajaxDataEntryObj.jsf','',null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&attributeData='+encode_utf8(attributeData),false);
                dataEntryHandler(attributeData,'actNet');
            }
        }
    //dataEntryHandler(attributeData,'actNet');
    }
}

function closeDataEntry()
{
    if(window.showModalDialog){
        window.returnValue="";
        window.close();
        return false;
    }
    else
    {
        window.close();
        return false;
    }
}

function closeDE(){
    if(closeFrm != 'ok'){
        if(launchFrm == 'null')
            launchFrm = '';
        if(!window.showModalDialog){
                if(launchFrm == 'DEexpNew'){
                window.opener.opener.tempExcpTrig[window.opener.opener.tempExcpTrig.length-1].value = "";
                window.opener.close();
            }
        }
    }
}


function openImportDocWin(event)
{
    event = (typeof event == 'undefined')? null: event;
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    var uploadLimit = getUploadMaxLength(strprocessname,stractivityName,'');
    if(typeof obj == 'undefined')
    {
        if(window.opener.isFormLoaded==false)
              return;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        if(typeof isFormLoaded == 'undefined'){
            if(typeof window.opener != 'undefined'){
                if(window.opener.isFormLoaded==false)
                return;
            }
        } else {
            if(isFormLoaded==false)
            return;
        }
        
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }
    var returnval = isDocTypeAddable(pid,wid,'',taskid) ;
    if(returnval == false || returnval == 'false')
        return false;
    var isNewVersion="Y";
    var isOverWrite="Y";
    
    if(!isNewVersionDoc(strprocessname,stractivityName))
        {
            isNewVersion="N";
        }
        
    
    if(!isOverWriteDoc(strprocessname,stractivityName))
        isOverWrite="N";
    if(typeof obj == 'undefined')
        var url = sContextPath+'/components/workitem/document/importdoc.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&winloc=T'+'&UploadLimit='+uploadLimit +'&rid='+MakeUniqueNumber()+"&isNewVersion="+isNewVersion+"&isOverWrite="+isOverWrite+"&processName="+strprocessname+'&activityName='+stractivityName;
    else
        var url = sContextPath+'/components/workitem/document/importdoc.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&UploadLimit='+uploadLimit +'&rid='+MakeUniqueNumber()+"&isNewVersion="+isNewVersion+"&isOverWrite="+isOverWrite+'&processName='+strprocessname+'&activityName='+stractivityName;
    url = appendUrlSession(url);

    var left = (window.screen.width - window8W)/2;
    var top = (document.documentElement.clientHeight - window8H)/2;
    
    var win = link_popup(url,'ImpDoc','resizable=yes,width='+window8W+',height='+window8H+',left='+left+',top='+top+',status=yes,resize=yes',windowList,'',false);    
    cancelBubble(event);
}
function onRadioChange(e){
        var multiLingualDocType;
     var tmpDocTypeList = document.getElementById('importForm:docListMenu');
    if(tmpDocTypeList!=null && tmpDocTypeList.options[tmpDocTypeList.options.selectedIndex].value == "-1")
       {
         return false;
      }
      
    var value1;
    if(!isIE())
        value1=e.target.value;
    else
        value1=e.srcElement.value;
    
    //for omniscan integration
        try{
            document.getElementById('importForm:mode').value = value1;
        }
        catch(e){}
        //end for omniscan integration

    if(value1=='new')
         document.getElementById('importForm:docPanel').style.display="none";
    else{
         var objCombo=document.getElementById('importForm:docListMenu');
         var docType=objCombo.options[objCombo.options.selectedIndex].value;    // Changed for multiLingual
         multiLingualDocType = objCombo.options[objCombo.options.selectedIndex].text;    // Changed for multiLingual
         if(docList==''){
             setParameter();
             var pid = document.getElementById('importForm:pid');
             var wid = document.getElementById('importForm:wid');
             var taskid = document.getElementById('importForm:taskid');
             var objCombo=document.getElementById('importForm:docListMenu');
             var docType=objCombo.options[objCombo.options.selectedIndex].value;  // Changed for multiLingual
             multiLingualDocType = objCombo.options[objCombo.options.selectedIndex].text;    // Changed for multiLingual
             var requestString='docType='+docType+'&pid='+encode_utf8(pid.value)+'&wid='+wid.value+'&taskid='+taskid.value+'&WD_UID='+WD_UID;
             var ajaxReq;
             if (window.XMLHttpRequest) {
                     ajaxReq= new XMLHttpRequest();
                } else if (window.ActiveXObject) {
                      ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
                    }
              var url = "ajaxdocList.jsf";
              url = appendUrlSession(url);
              ajaxReq.open("POST", url, false);
              ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
              ajaxReq.send(requestString);
              if (ajaxReq.status == 200 && ajaxReq.readyState == 4) {
                   docList=ajaxReq.responseText;
                   docList=eval("("+docList+")");
                }
              else
                  {
                      if(ajaxReq.status == 400)
                          alert(INVALID_REQUEST_ERROR);
                      else if(ajaxReq.status==12029){
                        alert(ERROR_SERVER); 
                      }
                      else
                          alert(ERROR_DATA);
              }
           }

              /*var objCombo1 = document.getElementById('docListName');
              if(objCombo1.options.length!=0){
                        var docname=objCombo1.options[objCombo1.options.selectedIndex].text;
                        if(docname.indexOf(docType)!=-1){
                                                document.getElementById('docPanel').style.display="inline";
                                                return;
                          }
               } */
               objCombo = document.getElementById('importForm:docListName');
               objCombo.options.length = 0;
               var dName;
               for(var count=0;count<docList.combo.length;count++){
                         dName=docList.combo[count].DocName;
                         var strDocType=docType+"(";
                         if(dName==docType || dName.indexOf(strDocType)==0){
                             //var displayName = docList.combo[count].DocName;
                             var index = dName.lastIndexOf("(");
                             var documentDisplayName = dName.substring(0,index);
                             if(index != '-1')
                                displayName = dName.replace(documentDisplayName,multiLingualDocType);
                             else
                                displayName = multiLingualDocType; 
                             
                            if(DocOrgName == "Y")
                                displayName = displayName + "("+ docList.combo[count].DiskName +")";

                            var option = new Option(displayName,docList.combo[count].DocIndex);

                            objCombo.options.add(option,objCombo.options.length);
                          }
                }
               document.getElementById('importForm:docPanel').style.display="";
        }
  }

function openNewDocWrapper(importDocJSON,broadCastFlag){
    
    var docIndex;
    var docName;
    var diskName;
    var selButton;
    var deldocId;
    var docType;
    if(broadCastFlag != undefined && broadCastFlag == false && importDocJSON!=undefined){
        docIndex = importDocJSON.DocIndex;
        docName = importDocJSON.DocName;
        diskName = importDocJSON.DiskName;
        selButton = importDocJSON.SelButton;
        deldocId = importDocJSON.DelDocId;
        docType = importDocJSON.DocType;
        var runscriptFlag = importDocJSON.runscript;
        openNewDocCollaboration(docIndex, docName, diskName, selButton, deldocId, docType, broadCastFlag,runscriptFlag);
    }
    else{
        docIndex = document.getElementById('importForm:docIndex').value;
        docName = document.getElementById('importForm:docName').value;
        diskName = document.getElementById('importForm:diskName').value;
        selButton = document.getElementById('importForm:selButton').value;
        deldocId = document.getElementById('importForm:delDocId').value;
        var docTypeObj = document.getElementById('importForm:docListMenu');
        if(!docTypeObj){
            docTypeObj = document.getElementById('importForm:labelDoct1');
            docType = docTypeObj.value;
        }else{
            if(docTypeObj.options.length > 0)
            	docType = docTypeObj.options[docTypeObj.options.selectedIndex].text;
            else
                return;
        }
        
        openNewDoc(docIndex,docName,diskName,selButton,deldocId,docType,broadCastFlag);
    }
    
}
function openNewDoc(docIndex,docName,diskName,selButton,deldocId,docType,broadCastFlag)
{
    document.forms['importForm'].importForm.value = windowProperty.winloc;
    var window_workdesk="";
    if(windowProperty.winloc=="T")
        window_workdesk=window.opener.opener;
    else
        window_workdesk=window.opener;
    if(typeof calledFromWindow !="undefined"&&calledFromWindow)//Bug 79470
        window_workdesk=window.opener.opener;//Bug 79470
    var docTimeStamp = "";
    var winList=window_workdesk.windowList;
    var docWindow=getWindowHandler(winList,"tableGrid");
    var formWindow=getWindowHandler(winList,"formGrid");
    var pid = document.getElementById('importForm:pid').value;
    var wid = document.getElementById('importForm:wid').value;
    var taskid = document.getElementById('importForm:taskid').value;
    
    if(isDocTimeStamp == "Y")
        docTimeStamp = docName.substring(docName.lastIndexOf("("));

    refreshDocs(window_workdesk); // Refreshing the document list on case file view
    
    if(DocOrgName == 'Y')
    {
        if(docType != '' && docType != ' ' && docType != 'null')
            docName = docType+docTimeStamp+"(" + diskName + ")";
        else
            docName += docTimeStamp+"(" + diskName + ")";
    }
    else
    {
        if(docType != '' && docType != ' ' && docType != 'null')
            docName = docType+docTimeStamp; 
    }

    if(docWindow){
        if(runscript=='true'){
            if(selButton == 'new')
            {                
                var objCombo = docWindow.document.getElementById('wdesk:docCombo'); 
                if(objCombo.length == 0){
                    if(!reloadNewAddedDoc(strprocessname,stractivityName)){
                        var opt = docWindow.document.createElement("OPTION");
                        opt.text = "-------"+SELECT_DOCUMENT+"-------";
                        opt.value = "-1";
                        objCombo.add(opt,0);
                        objCombo.selectedIndex = 0;
                        
                        var ref = docWindow.document.getElementById('wdesk:noDocDiv');
                        if(typeof ref != 'undefined'){                        
                            ref.style.display="none";
                        }

                        ref = docWindow.document.getElementById('wdesk:docOperation');
                        if(typeof ref != 'undefined'){                        
                            ref.style.display="none";
                        }
                    }   
                }
                
                if(isIE() || isInternetExplorer11() || isEdge()){
                    var opt = docWindow.document.createElement("OPTION");
                    opt.text = docName;
                    opt.value = docIndex;
                    objCombo.add(opt);
                } else{
                    var option=new Option(docName,docIndex);
                    objCombo.options.add(option,objCombo.options.length);
                }
                
                if(reloadNewAddedDoc(strprocessname,stractivityName)){
                    objCombo.selectedIndex = objCombo.options.length-1;
                }
                
                if(typeof docWindow.document.getElementById('wdesk:docList') != 'undefined'){
                    docWindow.document.getElementById('wdesk:docList').style.display="inline";
                }

                if(reloadNewAddedDoc(strprocessname,stractivityName)){
                    docWindow.reloadapplet(docIndex,false,'new');
                }
            /*if (window.opener.docListwin)
                     openDocList();*/
            }
            else if(selButton == 'newversion')
            {
                var objCombo = docWindow.document.getElementById('wdesk:docCombo');
                var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                    if(objCombo[count].value == docIndex)
                    {
                        selectedindex = count;
                        objCombo[count].text = docName;
                        break;
                    }
                }
                
                if(reloadNewAddedDoc(strprocessname,stractivityName)){
                    objCombo.selectedIndex = selectedindex;
                    docWindow.reloadapplet(docIndex,false,'getdocument');
                }
            }
            else if(selButton == 'deleteadd')
            {
                var objCombo = docWindow.document.getElementById('wdesk:docCombo');
                var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                    if(objCombo[count].value == deldocId)
                    {
                        selectedindex = count;
                        objCombo[count].value = docIndex;
                        objCombo[count].text = docName;
                        break;
                    }
                }
            
                if(reloadNewAddedDoc(strprocessname,stractivityName)){
                    objCombo.selectedIndex = selectedindex;
                    docWindow.reloadapplet(docIndex,false,'getdocument');
                }
            }
    
            var formWindow = window_workdesk.getWindowHandler(window_workdesk.windowList, "formGrid");
            var ngformIframe = formWindow.document.getElementById("ngformIframe");
            if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm && !window_workdesk.bAllDeviceForm){                
                IsScanAction = (typeof IsScanAction == 'undefined')? false: IsScanAction;
                
                if(formWindow && IsScanAction){                    
                    if(ngformIframe != null){
                        // parameter passing           
                        var ngformIframeWindow = ngformIframe.contentWindow;
                        
                        var wiDummySaveStructWinRef = ngformIframeWindow;
                        if(formIntegrationApr == "4"){
                            wiDummySaveStructWinRef = window;
                        }

                        wiDummySaveStructWinRef.WiDummySaveStruct.Type = 'OpenNewDoc';
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.FormType = 'NGHTML';
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.window_workdesk = window_workdesk; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.sourceWindow = window;
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.pid = pid; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.wid = wid; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.taskid = taskid; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.docType = docType; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.docIndex = docIndex; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.docName = docName; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.diskName = diskName; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.selButton = selButton; 
                        wiDummySaveStructWinRef.WiDummySaveStruct.Params.deldocId = deldocId;
                        
                        if(formIntegrationApr == "4"){
                            ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('S','Y')");	                                                                
                        } else {
                            ngformIframeWindow.clickLink('cmdNgFormDummySave');
                        }
                    }
                    return false;
                }
            } else {
                if(window_workdesk.bCustomIForm){
                    if(formWindow && ngformIframe != null){
                        if(typeof ngformIframe.contentWindow.customIFormHandler != 'undefined'){
                            ngformIframe.contentWindow.customIFormHandler('S','Y');
                        }
                    }
               }
               
                commonOpenNewDoc(window_workdesk,'',pid, wid, docType, docIndex, docName, diskName, selButton, deldocId,taskid);
            }
    
        /*window_workdesk.for_save("dummysave");
        executeScanAction(window_workdesk,pid, wid, docType);
        if(isCloseImportWindow=="Y")
               window.close();*/
       
        }
        else {
            if(typeof errorFlag != 'undefined' && errorFlag == "true" && typeof documentPostHook != 'undefined'){
                documentPostHook(docIndex, selButton, docName, errorMessage);
            }
        }
    
        if(docWindow.SharingMode && docIndex!=''){
            docWindow.broadcastImportDocEvent(docIndex, docName, diskName, selButton, deldocId, docType,runscript);
        }
    }
    else {
        if(typeof docIndex != 'undefined' && docIndex!="" && typeof documentPostHook != 'undefined'){
            documentPostHook(docIndex, selButton, docName, "");
        }
        else {
            if(typeof errorFlag != 'undefined' && errorFlag == "true" && typeof documentPostHook != 'undefined'){
                documentPostHook(docIndex, selButton, docName, errorMessage);
            }
        }
    }
    hidePopupMask();
}

function refreshDocs(window_workdesk) {
    if(window_workdesk.document.getElementById('wdesk:refreshDocs')!=null)
        window_workdesk.clickLink('wdesk:refreshDocs');
}

function commonOpenNewDoc(window_workdesk,formType, pid, wid, docType,docIndex, docName, diskName, selButton, deldocId,taskid){
    formType = typeof formType == 'undefined'? '': formType;
    
    window_workdesk.for_save("dummysave");
    executeScanAction(window_workdesk,pid, wid, docType,taskid,docIndex, docName);
    if(isCloseImportWindow=="Y")
           window.close();
    
    if(formType == 'NGHTML'){
        var docWindow=getWindowHandler(window_workdesk.windowList,"tableGrid");
    
        if(docWindow.SharingMode && docIndex!=''){
            docWindow.broadcastImportDocEvent(docIndex, docName, diskName, selButton, deldocId, docType);
        }  
    }
}

function openNewDocCollaboration(docIndex,docName,diskName,selButton,deldocId,docType,broadCastFlag,runscriptFlag)
{
    if(docType == null || typeof docType == 'undefined'){
        docType = '';
    }
    //document.forms['importForm'].importForm.value = windowProperty.winloc;
    var window_workdesk="";
    window_workdesk = window;
    var winList=window_workdesk.windowList;
    var docWindow=getWindowHandler(winList,"tableGrid");
    //var formWindow=getWindowHandler(winList,"formGrid");
    var pid = document.getElementById('wdesk:pid').value;
    var wid = document.getElementById('wdesk:wid').value;
    var taskid = document.getElementById('wdesk:taskid').value;
    if(DocOrgName == 'Y')
        docName += "(" + diskName + ")";
    if(docWindow){
        if(runscriptFlag=='true'){
            if(selButton == 'new')
            {
                var objCombo = docWindow.document.getElementById('wdesk:docCombo');
                if(isIE() || isInternetExplorer11()){
                    var opt = docWindow.document.createElement("OPTION");
                    opt.text = docName;
                    opt.value = docIndex;
                    objCombo.add(opt);
                }
                else{
                    var option=new Option(docName,docIndex);
                    objCombo.options.add(option,objCombo.options.length);
                }

                objCombo.selectedIndex = objCombo.options.length-1;
                docWindow.reloadapplet(docIndex,false);
            /*if (window.opener.docListwin)
                     openDocList();*/
            }
            else if(selButton == 'newversion')
            {
                var objCombo = docWindow.document.getElementById('wdesk:docCombo');
                var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                    if(objCombo[count].value == docIndex)
                    {
                        selectedindex = count;
                        objCombo[count].text = docName;
                        break;
                    }
                }
                objCombo.selectedIndex = selectedindex;
                docWindow.reloadapplet(docIndex,false);
            }
            else if(selButton == 'deleteadd')
            {
                var objCombo = docWindow.document.getElementById('wdesk:docCombo');
                var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                    if(objCombo[count].value == deldocId)
                    {
                        selectedindex = count;
                        objCombo[count].value = docIndex;
                        objCombo[count].text = docName;
                        break;
                    }
                }
                objCombo.selectedIndex = selectedindex;
                docWindow.reloadapplet(docIndex,false);
            }
            window_workdesk.for_save("dummysave");
            executeScanAction(window_workdesk,pid, wid, docType,taskid);
        }
    }
}

function setParameter()
{
    var pid = document.getElementById('importForm:pid');
    var wid = document.getElementById('importForm:wid');
    var taskid = document.getElementById('importForm:taskid');
    var obj = window.top.opener.document.forms['wdesk'];
    var strdatadefname;
    var docValue;
     var objCombo1 = document.getElementById('importForm:docListName');
    if(objCombo1 && objCombo1.options.length!=0){
        docValue=objCombo1.options[objCombo1.options.selectedIndex].value;
        document.getElementById('importForm:documentId').value=docValue;        
    }
    if(typeof obj == 'undefined')
    {
        wid.value = window.top.opener.top.opener.document.getElementById('wdesk:wid').value;
        pid.value = window.top.opener.top.opener.document.getElementById('wdesk:pid').value;
        taskid.value = window.top.opener.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        wid.value = window.top.opener.document.getElementById('wdesk:wid').value;
        pid.value = window.top.opener.document.getElementById('wdesk:pid').value;
        taskid.value = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    
    var processname = window.opener.strprocessname;
    var activityname = window.opener.stractivityName;
    var username = window.opener.userName;
    var docTypeObj = document.getElementById('importForm:docListMenu');
    if(!docTypeObj){
        docTypeObj = document.getElementById('importForm:labelDoct1');
    }
    var docTypeName = docTypeObj.value;
    var docComment="";
    var docDisplayName= docTypeObj.options[docTypeObj.selectedIndex].text;
    strdatadefname = customDataDefName(processname,activityname,username,docTypeName);
    docComment=modifyDocComment(processname,activityname,username,docTypeName);
    document.getElementById('importForm:hiddocDisplayName').value = docDisplayName;
    document.getElementById('importForm:DataDefName').value = strdatadefname;
    document.getElementById('importForm:UploadLimit').value = uploadLimit;
    document.getElementById('importForm:winloc').value = winLoc;
    document.getElementById('importForm:hidedocComment').value = docComment;
}

function checkFileSize(inputFile) {
    if (inputFile.files && inputFile.files[0].size == 0) {
        fieldValidator(null, ZERO_FILE_SIZE, "absolute", true);
        inputFile.value = null; // Clears the field.
    }
    //Bug 66390
    var flag=false;
    for(var i=0; i<latinChars.length;i++){
        if(inputFile.files[0].name.indexOf(latinChars[i]) != -1) {
            flag=true;
            break;
        }
    }
    if(flag==true){
        fieldValidator(null, "File Name is having Special Characters", "absolute", true);
        inputFile.value = null; // Clears the field.
    }
    
}

function beforeImport(id)
{
    var tmpDocTypeList = document.getElementById('importForm:docListMenu');
    if(tmpDocTypeList!= null && tmpDocTypeList.options[tmpDocTypeList.options.selectedIndex].value == "-1")
       {
         alert(SELECT_DOC_TYPE);
         return false;
      }
      
    var docTypeName = document.getElementById('importForm:docListMenu').value;
    if(tmpDocTypeList != null){  
     if(!importDocumentPrehook(docTypeName))
         return false;
    }
    var pid , wid, taskid;
    var obj = window.top.opener.document.forms['wdesk'];
    if(typeof obj == 'undefined')
    {
        wid = window.top.opener.top.opener.document.getElementById('wdesk:wid').value;
        pid = window.top.opener.top.opener.document.getElementById('wdesk:pid').value;
        taskid = window.top.opener.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    var fileobj = document.getElementById(id);
    
    var docModifyFlagInfo = document.getElementById("importForm:hidDocModifyFlagInfo").value;
        if(docModifyFlagInfo != undefined && docModifyFlagInfo != '' && docModifyFlagInfo != "") {
            var docModifyFlagInfoJson = JSON.parse(docModifyFlagInfo);
            if((docModifyFlagInfoJson[docTypeName] == 'M' || docModifyFlagInfoJson[docTypeName] == 'm' ) && ! isDocTypeAddable(pid,wid,docTypeName,taskid)){
                alert("Documnet Does not have Proper rights");
            fileobj.focus();
            return false;
                
            }
        }
        
    if(Trim(fileobj.value) == ""){
            alert(SELECT_DOC_TO_UPLOAD);
            fileobj.focus();
            return false;
    }
    else if(Trim(fileobj.value) != "")
    {
            if(document.getElementById("importForm:GDocMimeType")!=null&&(document.getElementById("importForm:GDocMimeType").value=="application/vnd.google-apps.document"
            ||document.getElementById("importForm:GDocMimeType").value=="application/vnd.google-apps.presentation"||document.getElementById("importForm:GDocMimeType").value=="application/vnd.google-apps.spreadsheet")){
            var FileExt;
            if(document.getElementById("importForm:GDocMimeType").value=="application/vnd.google-apps.document"){
                FileExt="docx";
            }
            else if(document.getElementById("importForm:GDocMimeType").value=="application/vnd.google-apps.spreadsheet"){
                FileExt="xlsx";
            }
            else{
                FileExt="pptx";
            }
            if (!validateUploadDocType(FileExt))
            {
                alert(INVALID_FILE_TYPE);
                fileobj.focus();
                return false;
            }
            else if (!valUploadDocRestriction(FileExt, UplCheck, fTypeJsonObj))
            {
                alert(INVALID_FILE_TYPE);
                fileobj.focus();
                return false;
            } else if (!validateUploadFormFieldRestriction(fileName)) {
                alert(DOCUMENT_NAME_DOES_NOT_MATCHES_FIELD_VALUE);
                fileobj.focus();
                return false;
            }

            if (!isUploadedDocType(fileobj.value, docTypeName))
            {
                fileobj.focus();
                return false;
            }
        }
        else{
            if (fileobj.value.indexOf('.') == -1)
            {
                alert(SPECIFY_EXT_FOR_DOC);
                fileobj.focus();
                return false;
            }

            dotPos = fileobj.value.lastIndexOf('.');
            var FileExt = fileobj.value.substring(dotPos + 1, fileobj.value.length);
            var filePos;
            if (fileobj.value.indexOf("/") == -1) {
                filePos = fileobj.value.lastIndexOf('\\');
            } else {
                filePos = fileobj.value.lastIndexOf('/');
            }
            var fileName = fileobj.value.substring(filePos + 1, fileobj.value.length);
            if (FileExt == "")
            {
                alert(SPECIFY_EXT_FOR_DOC);
                fileobj.focus();
                return false;
            } else if (!validateUploadDocType(FileExt))//WCL_8.0_081
            {
                alert(INVALID_FILE_TYPE);
                fileobj.focus();
                return false;
            } else if (!valUploadDocRestriction(FileExt, UplCheck, fTypeJsonObj))
            {
                alert(INVALID_FILE_TYPE);
                fileobj.focus();
                return false;
            } else if (!validateUploadFormFieldRestriction(fileName)) {
                alert(DOCUMENT_NAME_DOES_NOT_MATCHES_FIELD_VALUE);
                fileobj.focus();
                return false;
            }

            if (!isUploadedDocType(fileobj.value, docTypeName))
            {
                fileobj.focus();
                return false;
            }
        }
        
              var window_workdesk="";
            if (typeof window.opener.opener.document.forms['wdesk'] == 'undefined')
                window_workdesk = window.opener;
            else
                window_workdesk = window.opener.opener;
            if(window_workdesk.for_save('dummysave') == 'failure')
                return false;
            var docType = document.getElementById('importForm:docListMenu');
            if(docType != 'undefined' && docType!=null){
                var docTypeName = document.getElementById('importForm:docListMenu').value;
                if(!validateUploadDocTypeName(docTypeName))
                {
                        docType.focus();
                        return false;
                }
            }
    }
    if(NGDocApplet == 'Y'){
            document.getElementById("importForm:divApplet").innerHTML='<APPLET code="com.newgen.docApplet.NGDocApplet.class" Name="docApplet" width="0" height="0" archive="/webdesktop/webtop/applet/docApplet.jar" cabbase="/webdesktop/webtop/applet/docApplet.cab"><param name = "filePath" value="'+fileobj.value+'"></APPLET>';
            var FileSize = document.docApplet.returnSize();
            if(!checkFile(FileSize))
            {
                    var proceed = alert(CONFIRM_MAX_UPLOAD_LIMIT + uploadLimit + " MB" + CONFIRM_CURRENT_FILESIZE + (FileSize/1024/1024).toPrecision(4) + " MB");
                    return false;
            }
    }    
    setPopupMask();
    showPopupMask();
    CreateIndicator("application");
    /*
    var messageImg = document.getElementById("importForm:waitIndicator");    
    messageImg.style.left=''+(document.body.clientWidth-105)/2+'px';
    messageImg.style.top=''+document.body.clientHeight/2+'px';
    messageImg.style.display='inline';
    */
    return true;
}

function importClick(){
    /*
    if(validator('importForm:fileupload,importForm:docListMenu')){
        setParameter();
        return(beforeImport('importForm:fileupload'));
    } 
    */
    var documentName = document.getElementById('fileupload').value.replace(/^.*[\\\/]/, '');;
     var documentType = document.getElementById('importForm:docListMenu').value;
     var bValidateDocumentName = true;
     if(typeof ValidateDocumentName!= 'undefined') {
         bValidateDocumentName = ValidateDocumentName(documentName,documentType);
     }
    if(!bValidateDocumentName){
        return false;
    }
    if(googleIntegrated){
        if(document.getElementById("saveOptions2").checked){
            if(document.getElementById('importForm:fileuploadDrive').value==NO_FILE_CHOSEN||Trim(document.getElementById('importForm:fileuploadDrive').value)==""){
                addCSS(document.getElementById('importForm:fileuploadDrive'), "mandatoryControlCSS");
                notifierAbs('notifier', ENTER_MANDATORY_FIELD, "absolute", true);
                return false;
            }
            else if(Number(document.getElementById('importForm:GDocSize').value)>uploadLimit * 1024 * 1024 + 50){
                notifierAbs('notifier', CONFIRM_MAX_UPLOAD_LIMIT+uploadLimit+"MB", "absolute", true);
                return false;
            }
            if(validator('importForm:docListMenu,importForm:docComment')){
                setParameter();
                if(beforeImport('importForm:fileuploadDrive')){
                    return true;
                }
                else{
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else{
            if(validator('fileupload,importForm:docListMenu,importForm:docComment')){
                setParameter();
                if(beforeImport('fileupload')){
                    document.getElementById('importForm:GDocId').value="";
                    document.getElementById('importForm:GDocName').value="";
                    document.getElementById('importForm:GDocSize').value="";
                    document.getElementById('importForm:GDocUrl').value="";
                    document.getElementById('importForm:GDocMimeType').value="";
                    return true;
                }
                else{
                    return false;
                }
            }
            else {
                return false;
            }
        }
    }
    else{
        if(validator('fileupload,importForm:docListMenu,importForm:docComment')){
            setParameter();
            return(beforeImport('fileupload'));
        }
        else{
            return false;
        }
    }

}

function fireTrigger(type,index){

    var pid=document.getElementById('wdesk:pid');
    var wid=document.getElementById('wdesk:wid');
    var taskid=document.getElementById('wdesk:taskid');
    var window_workdesk = window;
    
    if(windowProperty.winloc == 'M'){
        if(isFormLoaded==false)
           return;
        /*for_save('dummysave');
        saveCalled=true;*/
    }
    else if(windowProperty.winloc == 'T'){
        if(window.opener.opener.isFormLoaded==false)
           return;
        /*window.opener.opener.for_save('dummysave');
        window.opener.opener.saveCalled=true;*/
        window_workdesk = window.opener.opener;
    }
    else{
        if(window.opener.isFormLoaded==false)
          return;      
        /*window.opener.for_save('dummysave');
        window.opener.saveCalled=true;*/
        window_workdesk = window.opener;
    }
    
    var formWindow = window_workdesk.getWindowHandler(window_workdesk.windowList, "formGrid");
    var ngformIframe = formWindow.document.getElementById("ngformIframe");
    if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm && !window_workdesk.bAllDeviceForm){       
       if(formWindow){
           if(ngformIframe != null){
               // parameter passing           
               var ngformIframeWindow = ngformIframe.contentWindow;
               
               var wiDummySaveStructWinRef = ngformIframeWindow;
               if(formIntegrationApr == "4"){
                    wiDummySaveStructWinRef = window;
               }
               wiDummySaveStructWinRef.WiDummySaveStruct.Type = 'Trigger';
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.window_workdesk = window_workdesk;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.sourceWindow = window;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.index = index;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.pid = pid;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.wid = wid;           
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.taskid = taskid;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.type = type;

                if(formIntegrationApr == "4"){
                    ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('S','Y')");	                                                                
                } else {
                    ngformIframeWindow.clickLink('cmdNgFormDummySave');
                }
           }
       }
   } else {
       if(formWindow && window_workdesk.bCustomIForm){
            if(ngformIframe != null){
                if(typeof ngformIframe.contentWindow.customIFormHandler != 'undefined'){
                    ngformIframe.contentWindow.customIFormHandler('S','Y');
                }
            }
        }
        
       commonFireTrigger(window_workdesk,type,index,pid,wid,taskid);
   }
   
    /*winRef.for_save('dummysave');
    winRef.saveCalled=true;
    
    var requestString='trigorigin='+type+'&index='+index+'&pid='+encode_utf8(pid.value)+'&wid='+wid.value+'&WD_UID='+WD_UID;
    var docChange=new net.ContentLoader('/webdesktop/ajaxfireTrigger.jsf?'+requestString,triggerHandler,null,"POST","",false);*/
}

function commonFireTrigger(window_workdesk,type,index,pid,wid,taskid){
    window_workdesk.for_save('dummysave');
    window_workdesk.saveCalled=true;
    
    var requestString='trigorigin='+type+'&index='+index+'&pid='+encode_utf8(pid.value)+'&wid='+wid.value+'&taskid='+taskid.value+'&WD_UID='+WD_UID;
    var docChange=new net.ContentLoader('/webdesktop/ajaxfireTrigger.jsf?'+requestString,triggerHandler,null,"POST","",false);
}

function triggerHandler(){
    var broadcastEvent = false;
    
    var window_workdesk = "";
    if(windowProperty.winloc == 'M')
                window_workdesk = window;
            else if(windowProperty.winloc == 'T')
                window_workdesk = window.opener.opener;
            else
                window_workdesk = window.opener;
    
    if(window_workdesk.SharingMode)
        broadcastEvent = true;
    var trigRes=eval("("+this.req.responseText+")");
    var trigResponse = trigRes.trigRes;
    var todoId = trigRes.todoId;
    var assocField = document.getElementById("wdesk:todo"+todoId).alt;
    if(trigResponse[0].flag=='0' && trigResponse[0].TriggerType != 'LA' && trigResponse[0].TriggerType != 'E' && trigResponse[0].TriggerType !='G'){
        document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
        setFormData(assocField,trigResponse[0].status,'m');
        window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),broadcastEvent,trigResponse[0].status,assocField);
    }
    else if(trigResponse[0].flag=='0' && trigResponse[0].TriggerType == 'E'){
        document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
        setFormData(assocField,trigResponse[0].status,'m');
        window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),broadcastEvent,trigResponse[0].status,assocField);
    }
    else if(trigResponse[0].TriggerType != 'LA' && trigResponse[0].TriggerType != 'G'){
        document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
        setFormData(assocField,trigResponse[0].status,'m');
        window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),broadcastEvent,trigResponse[0].status,assocField);
    }
    if((trigResponse[0].flag=='0') && (trigResponse[0].TriggerType=='G') && (trigResponse[0].description=='success')){
        document.getElementById("wdesk:todo"+todoId).value = trigResponse[0].status;
        setFormData(assocField,trigResponse[0].status,'m');
        window_workdesk.broadcastWrapperToDoData(todoId,getToDoName(todoId),broadcastEvent,trigResponse[0].status,assocField);
    }
    if((trigResponse[0].flag=='0' && trigResponse[0].TriggerType != 'E' && trigResponse[0].TriggerType != 'M') || (trigResponse[0].flag=='-1' &&  trigResponse[0].TriggerType == 'M') ||(trigResponse[0].TriggerType == 'G') || (trigResponse[0].TriggerType == 'X') || (trigResponse[0].flag=='-1' &&  trigResponse[0].TriggerType == 'E') || (trigResponse[0].flag=='-1' &&  trigResponse[0].TriggerType == 'U')){
        triggerDisplay(trigResponse,'todolist',todoId);
    }
}

function reflectToDoTriggerValue(assocField,todoId,todoValue,modelUpdate){
    document.getElementById("wdesk:todo"+todoId).value = todoValue;
    document.getElementById("wdesk:todo"+todoId).style.visibility = 'visible';
    setFormData(assocField,todoValue,modelUpdate);
}

function setFormData(varName,varValue,modelUpdate,fromtodo){
   if(typeof fromtodo != 'undefined' && fromtodo == 'todopick'){
             
        if(windowProperty.winloc == 'M')
            saveCalled = true;
        else if(windowProperty.winloc == 'T')
            window.opener.opener.saveCalled = true;
        else
            window.opener.saveCalled = true;
    }
    if(varName != null && varName!= ''){
        var winList="";
        var formWindow="";
        if(typeof modelUpdate == 'undefined')
            modelUpdate='';
        var wdeskWindow=getLocation();
        if(wdeskWindow)
            winList=wdeskWindow.windowList;
        if(winList)
            formWindow=getWindowHandler(winList,"formGrid");
        if(typeof formWindow == 'undefined'||modelUpdate=='m'){
            var pid=document.getElementById('wdesk:pid');
            var wid=document.getElementById('wdesk:wid');
            var taskid=document.getElementById('wdesk:taskid');
            var requestString='varName='+varName+'&varValue='+varValue+'&pid='+encode_utf8(pid.value)+'&wid='+wid.value+'&taskid='+taskid.value+'&Action=dataform&WD_UID='+WD_UID;
            new net.ContentLoader('/webdesktop/ajaxupdateclient.jsf',updateHandler,null,"POST",requestString);            
            
            if((wdeskWindow.wiproperty.formType == "NGFORM") && (wdeskWindow.ngformproperty.type == "applet") && !wdeskWindow.bDefaultNGForm){            
                
            } else {
                if(formWindow){
                    wdeskWindow.setFormValue(varName,varValue);
                }
            }
        }
        else{
            wdeskWindow.setFormValue(varName,varValue);
        }
    }
}

function getLocation(){
    var loc="";
    if(windowProperty.winloc)
        loc=windowProperty.winloc;
    if(loc=="N")//S from summary
        return window.opener;
    else if(loc=="T")//from Tool
        return window.opener.opener;
    else
        return window;


}
function updateHandler(){    
    var winList="";
    var formWindow="";
    var wdeskWindow=getLocation();
    if(wdeskWindow)
        winList=wdeskWindow.windowList;
    if(winList)
        formWindow=getWindowHandler(winList,"formGrid");
    
    if(formWindow){
        if((wdeskWindow.wiproperty.formType == "NGFORM") && (wdeskWindow.ngformproperty.type == "applet") && !wdeskWindow.bDefaultNGForm){            
            var ngformIframe = formWindow.document.getElementById("ngformIframe");	
            if(ngformIframe != null){
                var response = eval("("+this.req.responseText+")");
                if(typeof response.attribXML != 'undefined'){
                    if(wdeskWindow.bAllDeviceForm){
                        ngformIframe.contentWindow.eval("com.newgen.iforms.setValue('"+response.attribXML+"')");	
                    } else {
                        ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.setValue('ngform','"+response.attribXML+"')");	
                    }
                }
            }        
        }
    }
}

function deHandler(){

}

function updateExcpWin(excpWindow,trigResponse){
        var expTbl = excpWindow.document.getElementById("wdesk:expTable");
        //var excpHistory = excpWindow.document.getElementById('wdesk:histGrid');
        var excpId = trigResponse.excepIndex;
        var stat = trigResponse.status;
        if(stat == 'R')
            stat = RAISED;
        else if(stat == 'C')
            stat = CLEARED;
        else
            stat = RESPONDED;
        var expSeqList = trigResponse.expSeq;
        for(var k =0;k<expSeqList.length;k++)
        {
            var expSeqId = expSeqList[k].seqId;
            var tempExp = expSeqList[k].tempExp;
            if(tempExp == "N"){
                excpWindow.addException('',expTbl,excpId+'_'+expSeqId,trigResponse.name,stat,trigResponse.comments);
            }
            else{
                excpWindow.removeTempExp(excpId,expSeqId,trigResponse.comments);
            }
            if(k == expSeqList.length-1){
                excpWindow.clkMscExp(excpId+'_'+expSeqId,trigResponse.name);
            }
        }
}

function openConversationWin(event)
{
    event = (typeof event == 'undefined')? null: event;
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    if(typeof obj == 'undefined')
    {
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }
    if(typeof obj == 'undefined')
        var url = sContextPath+'/components/workitem/document/conversation.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&winloc=T'+'&rid='+MakeUniqueNumber();
    else
        var url = sContextPath+'/components/workitem/document/conversation.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&rid='+MakeUniqueNumber();
    url = appendUrlSession(url);

    var left = (window.screen.width - windowW)/2;
    var top = (document.documentElement.clientHeight - 360)/2;
    var win =  link_popup(url,'Conv','resizable=no,scrollbars=auto,width='+windowW+',height=360,left='+left+',top='+top+',status=yes,resize=yes',windowList,false);
    cancelBubble(event);
}

function OkConversation() {

    var pid = document.getElementById('textForm:pid');
    var wid = document.getElementById('textForm:wid');
    var taskid = document.getElementById('textForm:taskid');
    var txtArea = document.getElementById('textForm:TxtArea').value;
    var isValidatedXssTexts = validateXssTexts(txtArea);
    if(!isValidatedXssTexts)
      {
      alert(INVALID_FILE_WORD);
      return false;
      }
    if(Trim(txtArea)== "")
    {
        alert(ALERT_SOME_CONVERSATION);
        document.getElementById('textForm:TxtArea').focus();
        //  window.location.reload();
        return false;
    }

    var addAsNew = document.getElementById('textForm:ChkAddAsNew').checked;
      if(typeof window.top.opener.document.forms['wdesk'] == 'undefined')
    {
        wid.value = window.top.opener.top.opener.document.getElementById('wdesk:wid').value;
        pid.value = window.top.opener.top.opener.document.getElementById('wdesk:pid').value;
        taskid.value = window.top.opener.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        wid.value = window.top.opener.document.getElementById('wdesk:wid').value;
        pid.value = window.top.opener.document.getElementById('wdesk:pid').value;
        taskid.value = window.top.opener.document.getElementById('wdesk:taskid').value;
    }

    var param="";
    param+='addAsNew='+addAsNew+'&txtArea='+encode_utf8(txtArea)+'&wid='+wid.value+'&taskid='+taskid.value+'&pid='+encode_utf8(pid.value)+'&WD_UID='+WD_UID;

    var ajaxReq;
    if (window.XMLHttpRequest) {
    ajaxReq= new XMLHttpRequest();
    } else if (window.ActiveXObject) {
    ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
    }

    var url = sContextPath+"/ajaxConversationObj.jsf";
    url = appendUrlSession(url);
    ajaxReq.open("POST", url, false);
    ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    ajaxReq.send(param);
    if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
            var response=ajaxReq.responseText;
            myBrowsers = navigator.appName;
            if (myBrowsers=="Netscape")
            {
               document.getElementById('textForm:TxtArea').value="";
               document.getElementById('textForm:ChkAddAsNew').disabled =false;
               //document.getElementById('textForm:ChkAddAsNew').checked=false;
            }
            
            //convComboHandler(response);
            convComboHandlerWrapper(false, response);
            document.body.style.cursor="default";

          window.opener.OkConversationClick();
          if(window.opener.SharingMode){
              var data=eval("("+response+")");
              var docName = data.document[0].docName;
              var docIndex = data.document[0].docIndex;
              var diskName = data.document[0].diskName;
              window.opener.broadcastConversationEvent(response,addAsNew,docName,docIndex,diskName);
          }
          window.top.close();
            return 'success';
             
    }
    else
    {
        if(ajaxReq.status==598)
        {
            alert(ajaxReq.responseText);
        }
        else if(ajaxReq.status==599)
        {

            //url = "/webdesktop/faces/login/login.jsp?"+"error="+ajaxReq.getResponseHeader('ngerror');
            // url = sContextPath+"/login/logout.jsf?"+"error=4020";
            url = sContextPath+"/error/errorpage.jsf?msgID=4020";
            url = appendUrlSession(url);
            
            //window.open(url,reqSession);

            var width = 320;
            var height = 160;
            var left = (window.screen.availWidth-width)/2;
            var top = (window.screen.availHeight-height)/2;

            //window.open(url,reqSession);
            if (window.showModalDialog){
                window.showModalDialog(url,'',"dialogWidth:"+width +"px;dialogHeight:"+height+"px;center:yes;dialogleft: "+left+"px;dialogtop: "+top+"px");
            }
        }
        else if(ajaxReq.status == 400)
                  alert(INVALID_REQUEST_ERROR);
        else if(ajaxReq.status==12029){
            alert(ERROR_SERVER); 
        }
        else{
            alert(ERROR_DATA);
        }
        document.body.style.cursor="default";
        window.top.close();
        return 'failure';
         
    }
    //window.top.close();
}

function saveChat(ref) {
    var addConversation=confirm(SAVE_CONFORMATION);
    if(addConversation==true){
    var pid = document.getElementById('chatfram:bp:pid');
    var wid = document.getElementById('chatfram:bp:wid');
    var taskid = document.getElementById('chatfram:bp:taskid');
    var txtArea = document.getElementById('chatfram:bp:msgtab').parentNode.innerHTML
    var addAsNew = true;
    if(typeof window.top.opener.document.forms['wdesk'] == 'undefined')
        {
            wid.value = window.parent.document.getElementById('wdesk:wid').value;
            pid.value = window.parent.document.getElementById('wdesk:pid').value;
            taskid.value = window.parent.document.getElementById('wdesk:taskid').value;
        }
        else
        {
            wid.value = window.top.opener.document.getElementById('wdesk:wid').value;
            pid.value = window.top.opener.document.getElementById('wdesk:pid').value;
            taskid.value = window.top.opener.document.getElementById('wdesk:taskid').value;
        }

    var ajaxReq;
    if (window.XMLHttpRequest) {
    ajaxReq= new XMLHttpRequest();
    } else if (window.ActiveXObject) {
    ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
    }
    DocOrgName='Y';
    var param="";
    var url = "/webdesktop/generic"+"/saveConversation.jsf";
    url = appendUrlSession(url);
    ajaxReq.open("POST", url, false);
    ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    ajaxReq.send(param);
    if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
            var response=ajaxReq.responseText;
            txtArea=response.replace("$$$$$$", txtArea);
            txtArea = "</pre>" + txtArea + "<pre>";
    }
    var encryptedTA=EncryptPassword(txtArea,eToken);
    param+='addAsNew='+addAsNew+'&txtArea='+encode_utf8(encryptedTA)+ '&isTxtAreaEncy=Y'+'&Chat=Y'+'&wid='+wid.value+'&taskid='+taskid.value+'&pid='+encode_utf8(pid.value)+'&WD_UID='+WD_UID;
    
    var url = "/webdesktop/ajaxConversationObj.jsf";
    url = appendUrlSession(url);
    ajaxReq.open("POST", url, false);
    ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    ajaxReq.send(param);
    if (ajaxReq.status == 200&&ajaxReq.readyState == 4) {
            var response=ajaxReq.responseText;
            myBrowsers = navigator.appName;
            convComboHandlerWrapper(false, response);
            document.body.style.cursor="default";

          window.parent.OkConversationClick();
          if(window.parent.SharingMode){
              var data=eval("("+response+")");
              var docName = data.document[0].docName;
              var docIndex = data.document[0].docIndex;
              var diskName = data.document[0].diskName;
              window.parent.broadcastConversationEvent(response,addAsNew,docName,docIndex,diskName);
          }
//          window.top.close();
            return 'success';
             
    }
    else
    {
        if(ajaxReq.status==598)
        {
            alert(ajaxReq.responseText);
        }
        else if(ajaxReq.status==599)
        {
            url = sContextPath+"/error/errorpage.jsf?msgID=4020";
            url = appendUrlSession(url);
            
            //window.open(url,reqSession);

            var width = 320;
            var height = 160;
            var left = (window.screen.availWidth-width)/2;
            var top = (window.screen.availHeight-height)/2;

            //window.open(url,reqSession);
            if (window.showModalDialog){
                window.showModalDialog(url,'',"dialogWidth:"+width +"px;dialogHeight:"+height+"px;center:yes;dialogleft: "+left+"px;dialogtop: "+top+"px");
            }
        }
        else if(ajaxReq.status == 400)
                  alert(INVALID_REQUEST_ERROR);
        else if(ajaxReq.status==12029){
            alert(ERROR_SERVER); 
        }
        else{
            alert(ERROR_DATA);
        }
        document.body.style.cursor="default";
        window.top.close();
        return 'failure';
         
    }
    //window.top.close();
    }
}

function convComboHandlerWrapper(broadCastFlag,data){

    var addAsNew;
    var docName;
    var docIndex;
    var diskName;
    var window_workdesk="";
    
    if(broadCastFlag==true){
      
        addAsNew = data.AddAsNew;
        docName = data.docName;
        docIndex = data.docIndex;
        diskName = data.diskName;
        if(windowProperty.winloc=="T")
            window_workdesk=window.opener;
        else
            window_workdesk=window;
        convComboHandler(docName, docIndex, diskName, addAsNew, window_workdesk);
    }
    else{
        data=eval("("+data+")");
        docName = data.document[0].docName;
        docIndex = data.document[0].docIndex;
        diskName = data.document[0].diskName;
        if(document.getElementById('textForm:ChkAddAsNew'))
            addAsNew = document.getElementById('textForm:ChkAddAsNew').checked;
        else
            addAsNew=false;
        if(document.getElementById('textForm:ChkAddAsNew')){
        if(windowProperty.winloc=="T")
            window_workdesk=window.opener.opener;
        else
            window_workdesk=window.opener;
            }
            else
                window_workdesk=window.parent;
        convComboHandler(docName, docIndex, diskName, addAsNew, window_workdesk);
        myBrowsers = navigator.appName;
        if (myBrowsers!="Netscape")
         window.top.close();
    }
}
function convComboHandler(docName,docIndex,diskName,addAsNew,window_workdesk)
{
    var winList=window_workdesk.windowList;
    var docWindow=getWindowHandler(winList,"tableGrid");
    if(docWindow)
    {
        var objCombo = docWindow.document.getElementById('wdesk:docCombo'); 
        if(DocOrgName == 'Y')
            docName += "(" + diskName + ")";
        if(addAsNew == true  )
        {

             var  myBrowser = navigator.appName;
             if (myBrowser!="Netscape" || isInternetExplorer11())
             {
                     var opt = docWindow.document.createElement("OPTION");
                     opt.text = docName;
                     opt.value = docIndex;
                     objCombo.add(opt);
             }
             else
             {

                     var option = new Option(docName,docIndex);
                     objCombo.options.add(option,objCombo.options.length);
             }
             objCombo.selectedIndex = objCombo.options.length-1;
        }
        else
        {
            var selectedindex = -1;
            for(var count=0;count<objCombo.length;count++)
            {
                if(objCombo[count].value == docIndex)
                {
                    selectedindex = count;
                    objCombo[count].text = docName;
                    break;
                }
            }

            if(selectedindex == -1){
                 myBrowser = navigator.appName;
                 if (myBrowser!="Netscape" || isInternetExplorer11())
                 {
                         opt = docWindow.document.createElement("OPTION");
                         opt.text = docName;
                         opt.value = docIndex;
                         objCombo.add(opt);
                 }
                 else
                 {

                         option = new Option(docName,docIndex);
                         objCombo.options.add(option,objCombo.options.length);
                 }
                 selectedindex = objCombo.options.length-1;
            }

            objCombo.selectedIndex = selectedindex;
        }
        docWindow.reloadapplet(docIndex,false,'getdocument');
    }
    
}

function ChangeRdbSelImpDoc(){
    document.getElementById('importForm:docPanel').style.display="none";
    if(isIE() && !(getIEVersion()>=10)) 
        document.getElementsByName('importForm:addMode')[1].checked=true;
    else
        document.getElementsByName('importForm:addMode')[0].checked=true;
    var docListVal = document.getElementById('importForm:docListMenu').value;
    var window_workdesk="";
    if(windowProperty.winloc=="T")
        window_workdesk=window.opener.opener;
    else
        window_workdesk=window.opener;
    var winList=window_workdesk.windowList;
    var docWindow=getWindowHandler(winList,"tableGrid");
    var pid , wid, taskid;
    var obj = window.top.opener.document.forms['wdesk'];
    if(typeof obj == 'undefined')
    {
        wid = window.top.opener.top.opener.document.getElementById('wdesk:wid').value;
        pid = window.top.opener.top.opener.document.getElementById('wdesk:pid').value;
        taskid = window.top.opener.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    
    var ref = document.forms['importForm']['importForm:addMode'][0];
    if(ref == null){        
        ref = document.getElementsByName('importForm:addMode')[0];        
    }
    
    if(ref != null){
        ref.disabled=false;
    }
    
    var returnval = isDocTypeAddable(pid,wid,docListVal,taskid);
    enableDisableDocOptions(returnval,docListVal);
}

function enableDisableDocOptions(docTypeAddable, docListVal) {
    var ref = document.getElementsByName('importForm:addMode')[0];
    if(ref){
        ref.parentNode.style.display="none";
    }
        
    ref = document.getElementsByName('importForm:addMode')[1];
    if(ref){
        ref.parentNode.style.display="none";
    }
        
    ref = document.getElementsByName('importForm:addMode')[2];
    if(ref){
        ref.parentNode.style.display="none";
    }
        
    ref = null;
    if (docTypeAddable == true) {
        ref = document.getElementsByName('importForm:addMode')[1]
        if (ref != null) {
            ref.parentNode.style.display="";
            ref.disabled = false;
        }
        ref = document.getElementsByName('importForm:addMode')[2];
        if (ref != null) {
            ref.parentNode.style.display="";
            ref.disabled = false;
        }
    } else {
        //		ref = document.forms['importForm']['importForm:addMode'][1];
        //		if (ref != null) {
        //                        ref.parentNode.style.display="none";
        //			ref.disabled = true;
        //		}
        //
        //		ref = document.forms['importForm']['importForm:addMode'][2];
        //		if (ref != null) {
        //                        ref.parentNode.style.display="none";
        //			ref.disabled = true;
        //		}
        if(docListVal!="-1"){
            ref = document.getElementsByName('importForm:addMode')[0];
            if (ref != null) {
                ref.parentNode.style.display="";
                ref.disabled = false;
                ref.click();
            }
        }
    }
    uploadLimit = getUploadMaxLength(strprocessname, stractivityName, document.getElementById('importForm:docListMenu').value);
    document.getElementById("importForm:uploadLimitLabel").innerHTML = uploadLimit + "MB)";

    if (typeof document.getElementsByName('importForm:addMode')[3] != 'undefined' && checkModifyRight(docListVal) == true && docTypeAddable == true) {
        document.getElementsByName('importForm:addMode')[3].disabled = false;
    } else if (typeof document.getElementsByName('importForm:addMode')[3] != 'undefined' && checkModifyRight(docListVal) == false) {
        if (enableOverwrite == true && docTypeAddable == true) {
            document.getElementsByName('importForm:addMode')[3].disabled = false;
        } else {
            document.getElementsByName('importForm:addMode')[3].disabled = true;
        }
    }
    
    try {
        var docModifyFlagInfo = document.getElementById("importForm:hidDocModifyFlagInfo").value;
        if(docModifyFlagInfo != undefined && docModifyFlagInfo != '' && docModifyFlagInfo != "") {
            var docModifyFlagInfoJson = JSON.parse(docModifyFlagInfo);
            if (docModifyFlagInfoJson[docListVal] == 'B'|| docModifyFlagInfoJson[docListVal] == 'b'|| docModifyFlagInfoJson[docListVal] == 'A' || docModifyFlagInfoJson[docListVal] == 'a') {
                // Add Right: New Document will be disabled
                
                ref = document.getElementsByName('importForm:addMode')[0];
                if (ref != null) {
                    ref.parentNode.style.display="";
                    ref.disabled = false;
                    ref.click();
                }
                
                ref = document.getElementsByName('importForm:addMode')[1];
                if (ref != null) {
                    ref.parentNode.style.display="none";
                    ref.disabled = true;
                }

                ref = document.getElementsByName('importForm:addMode')[2];
                if (ref != null) {
                    ref.parentNode.style.display="none";
                    ref.disabled = true;
                }
            } else if((docModifyFlagInfoJson[docListVal] == 'M' || docModifyFlagInfoJson[docListVal] == 'm' )){
                // Modify right ->  New Document will be disabled
                
                ref = document.getElementsByName('importForm:addMode')[0];
                if (ref != null) {
                    ref.parentNode.style.display="none";
                    ref.disabled = true;
                }
                
                if(docTypeAddable == true){
                    ref = document.getElementsByName('importForm:addMode')[1]
                    if (ref != null) {
                        ref.parentNode.style.display="";
                        ref.disabled = false;
                    }
                
                    ref = document.getElementsByName('importForm:addMode')[2];
                    if (ref != null) {
                        ref.parentNode.style.display="";
                        ref.disabled = false;
                    }
                    var alreadySelected=false;
                    if(newVersion=='Y'){
                        document.getElementsByName('importForm:addMode')[1].click();
                        alreadySelected=true;
                    }
        
                    if(overWrite=='Y'){
                        if(!alreadySelected){
                            document.getElementsByName('importForm:addMode')[2].click();
                        }
                    } 
                }
            } else if (docModifyFlagInfoJson[docListVal] == 'T'|| docModifyFlagInfoJson[docListVal] == 't') {
                // All/Total rights: New Document, New Version and Overwrite will be enabled
                
                ref = document.getElementsByName('importForm:addMode')[0];
                if (ref != null) {
                    ref.parentNode.style.display="";
                    ref.disabled = false;
                    ref.click();
                }                   
                
                if(docTypeAddable == true){                    
                    ref = document.getElementsByName('importForm:addMode')[1];
                    if (ref != null) {
                        ref.parentNode.style.display="";
                        ref.disabled = false;
                    }

                    ref = document.getElementsByName('importForm:addMode')[2];
                    if (ref != null) {
                        ref.parentNode.style.display="";
                        ref.disabled = false;
                    }
                }
            }
        }
    } catch(exc) {
    }
    
    if (docTypeAddable == true) {
        if(typeof disableNewBtnIfDocPresent != 'undefined' && disableNewBtnIfDocPresent()){  
            if((Trim(docListVal) != "")){
                document.getElementsByName('importForm:addMode')[0].parentNode.style.display="none";
                document.getElementsByName('importForm:addMode')[0].disabled=true;
                var alreadySelected=false;
                if(newVersion=='Y'){
                    document.getElementsByName('importForm:addMode')[1].click();
                    alreadySelected=true;
                }
        
                if(overWrite=='Y'){
                    if(!alreadySelected){
                        document.getElementsByName('importForm:addMode')[2].click();
                    }
                }             
            }
        }
    }
}
        

function scanDoc(event)
{
    event = (typeof event == 'undefined')? null: event;
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    if(typeof obj == 'undefined')
    {
        if(window.opener.isFormLoaded==false)
               return;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
      if(isFormLoaded==false)
           return;
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }
    var returnval = isDocTypeAddable(pid,wid,'',taskid);
    if( returnval == false || returnval == 'false')
        return ;

    var url = sContextPath+'/components/workitem/document/scandoc.jsf';
    url = appendUrlSession(url);

    var left = (window.screen.width - windowW)/2;
    var top = (document.documentElement.clientHeight - (windowH-40))/2;
    var wFeatures ='scrollbars=no,width='+windowW+',height='+(windowH-40)+',left='+left+',top='+top+',status=yes,resize=yes';
    
    var listParam=new Array();
    listParam.push(new Array("rid",encode_ParamValue(MakeUniqueNumber())));
    listParam.push(new Array("wid",encode_ParamValue(wid)));
    listParam.push(new Array("pid",encode_ParamValue(pid)));
    listParam.push(new Array("taskid",encode_ParamValue(taskid)));
    listParam.push(new Array("Action",encode_ParamValue("scandoc")));

     var win = openNewWindow(url,'ScanDocument',wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);

    // var url = '/webdesktop/faces/workitem/document/scan/scandoc_frameset.jsp?rid='+MakeUniqueNumber()+'&wid='+wid+'&pid='+encode_utf8(pid);
    //var win = window.open(url,'ScanDocument','scrollbars=yes,width='+windowW+',height='+windowH+',left='+windowY+',top='+windowX+',resize=yes');
    win.focus();
    cancelBubble(event);
}

function setNGFormValue(varName,varValue){
    if(wiproperty.formType=="NGFORM"&&ngformproperty.type=="applet")
    {try{
         if(!bDefaultNGForm){             
         } else {
            //varValue = getfvbForset(varName,varValue,true);
            var classString = document.wdgc.getNGClassString();
            document.wdgc.setNGValue(classString+'_'+varName,varValue);
            
            //document.wdgc.setNGValueEx(classString+'_'+varName,varValue);            
         }
         }catch(e){
               }
    }
    else{
         try{
          var classString= document.wdgc.NGClassString;
          varName=classString+'_'+varName;
          document.wdgc.NGValue(varName)=varValue;
         }catch(e){
              }
	}

}

function executeAction(index,isGenRep){
    if (typeof isActionClick != 'undefined'){
    if(isActionClick==true){
        alert(ALERT_ACTION_IN_PROGRESS);
        return;
    }
    else
        isActionClick=true;
    }
    var requestString = index+'';
    genResActionIndex = index+'';
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    if(typeof obj == 'undefined')
    {
       if(window.opener.isFormLoaded==false)
          return;
        pid = window.opener.document.getElementById('wdesk:pid').value;
        wid = window.opener.document.getElementById('wdesk:wid').value;
        taskid = window.opener.document.getElementById('wdesk:taskid').value;
       // window.opener.saveCalled=true;
    }
    else
    {
        if(isFormLoaded==false)
          return;
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
       // saveCalled=true;
    }
    var window_workdesk="";
    if(typeof window.top.document.forms['wdesk'] == 'undefined')
        window_workdesk = window.top.opener;
    else
        window_workdesk = window.top;
    if(isEmbd == 'Y'){
        window_workdesk = window;
    }
    
    var formWindow = getWindowHandler(window_workdesk.windowList, "formGrid");    
    var ngformIframe = formWindow.document.getElementById("ngformIframe");   
   if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm && !window_workdesk.bAllDeviceForm){       
       if(formWindow){       
           if(ngformIframe != null){
               // parameter passing           
               var ngformIframeWindow = ngformIframe.contentWindow;
               var wiDummySaveStructWinRef = ngformIframeWindow;
                if(formIntegrationApr == "4"){
                    wiDummySaveStructWinRef = window;
                }

               wiDummySaveStructWinRef.WiDummySaveStruct.Type = 'Action';
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.window_workdesk = window_workdesk;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.requestString = requestString;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.pid = pid;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.wid = wid;           
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.taskid = taskid;
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.isGenRep = isGenRep;

                if(formIntegrationApr == "4"){
                    ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('S','Y')");	                                                                
                } else {
                    ngformIframeWindow.clickLink('cmdNgFormDummySave');
                }
           }
       }
   } else {
       if(formWindow && window_workdesk.bCustomIForm){
            if(ngformIframe != null){
                if(typeof ngformIframe.contentWindow.customIFormHandler != 'undefined'){
                    ngformIframe.contentWindow.customIFormHandler('S','Y');
                }
            }
        }
        
        commonExecuteAction(window_workdesk,isGenRep,requestString,pid,wid,taskid);        
   }
}

function commonExecuteAction(window_workdesk,isGenRep,requestString,pid,wid,taskid){
    if(window_workdesk.for_save('dummysave') == 'failure')
            return false;

    var isdebug ="N";
    if(isDebug(strprocessname,stractivityName))
        isdebug ="Y";
        
    if(isGenRep=="Y")
        var ActionDocObj = new net.ContentLoader('/webdesktop/ajaxActionDocObj.jsf?Option='+requestString+'&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&isdebug='+isdebug+'&WD_UID='+WD_UID,openActionWindow,null,"POST","",false);
    else{
        var strTemplateData="";
        if(typeof templateData!='undefined'){
            strTemplateData=templateData();
        }
        if(typeof strTemplateData!='undefined'){
            strTemplateData=encode_utf8(strTemplateData);
        }
        else
            strTemplateData="";
        var dataStrFinal = 'Data='+strTemplateData;
        
        //   var ActionObj = new net.ContentLoader('/webdesktop/ajaxActionObj.jsf?Option='+requestString+'&wid='+wid+'&pid='+encode_utf8(pid)+'&WD_UID='+WD_UID,actionHandler,null,"POST","");    
        var ActionObj = new net.ContentLoader('/webdesktop/ajaxActionObj.jsf?Option='+requestString+'&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&WD_UID='+WD_UID,actionHandler,null,"POST",dataStrFinal,false);
    }
}

function actionHandler()
{
    showSuccessMsg = true;
    if(this.req.responseText == 'ConditionFailed'){
        if (window.showModalDialog){
            var url = sContextPath+"/components/workitem/view/dialogalert.jsf?WD_UID="+WD_UID;
            window.showModalDialog(url,'',"dialogWidth:280px;dialogHeight:150px;center:yes;");
        }
       //alert(ALT_ACTION_CONDITION_FAILED);
       return;
     }
    else if(this.req.responseText == 'GlbFailed')
    {
        // Bug Id: 28769
        window.focus();
        //-------------
        //alert(DYNAMIC_CONST_COND_FAILED);
        setmessageinDiv(DYNAMIC_CONST_COND_FAILED,"true",3000);
        return;
    }
    var bBroadCast = false;
    if(SharingMode){
        bBroadCast = true;
    }
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    if(typeof obj == 'undefined')
    {
        pid = window.opener.document.getElementById('wdesk:pid').value;
        wid = window.opener.document.getElementById('wdesk:wid').value;
        taskid = window.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }
    var window_workdesk="";
    if(typeof window.top.document.forms['wdesk'] == 'undefined')
        window_workdesk = window.top.opener;
    else
        window_workdesk = window.top;
    if(isEmbd == 'Y'){
        window_workdesk = window;
    }
    var winList = window_workdesk.windowList;
    var docWindow = getWindowHandler(winList,"tableGrid");
    var formWindow = getWindowHandler(winList,"formGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var actionList = eval("("+this.req.responseText+")");
    var actionType = '';
    var actionStatus = true;
     if(actionList.length==1)
         {
           if(typeof obj == 'undefined')
              window.opener.saveCalled=true;
           else
             saveCalled=true;
        }
    for(var k =0;k<actionList.length;k++)
    {
        var actionResponse = actionList[k+1].Action;
         if(actionResponse[k].ActionType!='Release')
           {
             if(typeof obj == 'undefined')
                      window.opener.saveCalled=true;
                else
                      saveCalled=true;
          }
        if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Trig'))
        {
            var temp = actionResponse[k+1].actionResponse;
            var mybrowser = navigator.appName;
             //check added for Bug 41452 - Launch Application Trigger not working for mspaint.
             if(mybrowser== "Netscape" && temp[0].flag=='0' && temp[0].TriggerType == 'LA'){
                setmessageinDiv(ALERT_BROWSER_NOT_SUPPORTED,"true",3000);
                return;
             }else{
                 triggerDisplay(temp,'action');
                 setmessageinDivSuccess(ACTION_EXECUTED_SUCCESSFULLY,"true",3000);
                 if(showSuccessMsg==true){
                    if(typeof temp[k+1]!='undefined')        //Bug 69398 
                     postHookGenerateResponse(temp[k+1].docIndex, temp[k+1].docName);
                 }
             }
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Set'))
        {
            if(actionResponse[k].description == 'failure')
            {
                // Bug Id: 28769
                window.focus();
                //-------------
                //alert(DYNAMIC_CONST_COND_FAILED);
                setmessageinDiv(DYNAMIC_CONST_COND_FAILED,"true",3000);
                return;
            }
            var temp = actionResponse;
            if(formWindow)
            {
                if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm){
                    var ngformIframe = formWindow.document.getElementById("ngformIframe");	
                    if(ngformIframe != null){
                        if(typeof temp[k+1] != 'undefined'){
                            if(window_workdesk.bAllDeviceForm){
                                ngformIframe.contentWindow.eval("com.newgen.iforms.setValue('"+temp[k+1].attribXML+"')");	
                            } else {
                                ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.setValue('ngform','"+temp[k+1].attribXML+"')");	
                            }
                        }
                    }
                } else {  
                    for(var i=1;i<temp.length;i++){
                        var valSetter=temp[i];
                        window_workdesk.setFormValue(valSetter.name,valSetter.value,valSetter.type);
                    }
                }
            }
        }
         else if((actionResponse[k].flag=='1') && (actionResponse[k].ActionType=='Set'))
        {
            if(actionResponse[k].description == 'failure')
            {
                // Bug Id: 28769
                window.focus();
                //-------------
                //alert(ALT_COMPLEX_VARIABLE_NOT_COMPATIBLE);
                setmessageinDiv(ALT_COMPLEX_VARIABLE_NOT_COMPATIBLE,"true",3000);
                return;
            }
            var temp = actionResponse;
            if(formWindow)
            {                            
                for(var i=1;i<temp.length;i++){
                    var valSetter=temp[i];
                    window_workdesk.setFormValue(valSetter.name,valSetter.value,valSetter.type);
                }                
            }
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='SubmitI'))
        {
            actionType = 'SubmitI';
            actionStatus = window_workdesk.introduceWI();
            break;
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='SubmitD'))
        {
            window_workdesk.done();
            break;
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Release'))
        {
                            actionType = 'Release';
			 window_workdesk.unlockflag="Y";
			 //window_workdesk.close();
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Priority'))
        {
            // Bug Id: 28769
            window.focus();
            //-------------
			 //alert(actionResponse[k+1].errMsg);
                         setmessageinDivSuccess(actionResponse[k+1].errMsg,"true",3000);
                         showSuccessMsg = false;
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Excep'))
        {
            if(actionResponse.length > 1){
                if(excpWindow)
                     updateExcpWin(excpWindow,actionResponse[k+1]);

                if(actionResponse[k+1].isTrig == "Y"){
                    var temp = actionResponse[k+1].trig;
                    //temp = eval("("+temp+")");
                    triggerDisplay(temp,'action');
                }
            }
        }
       k++;
    }
    if(showSuccessMsg==true){
        if(this.req.getResponseHeader("ActionStatus") == "Success"){
            //alert(ACTION_EXECUTED_SUCCESSFULLY);
            //setmessageinDiv(ACTION_EXECUTED_SUCCESSFULLY,"true",3000);
            if(bBroadCast){
                broadcastActionEvent(actionList);
            }
            if((actionStatus==false) && (actionType == 'SubmitI')){                
            } else {
                setmessageinDivSuccess(ACTION_EXECUTED_SUCCESSFULLY,"true",3000);
            }
            
            if(actionType == 'Release'){
                saveCheck= true;
                freeWorkitem('', '');
                window_workdesk.close();
            }
        }
    } 
    else{
        if(bBroadCast){
           broadcastActionEvent(actionList);
        }
    }

}

function genResponse()
{
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    if(typeof obj == 'undefined')
    {
        pid = window.opener.document.getElementById('wdesk:pid').value;
        wid = window.opener.document.getElementById('wdesk:wid').value;
        taskid = window.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }

    var window_workdesk="";
    var strTemplateData="";
    if(typeof templateData!='undefined'){
        strTemplateData=templateData();
    }
    if(typeof strTemplateData!='undefined'){
        strTemplateData=encode_utf8(strTemplateData);
    }
    else
        strTemplateData="";
    dataStrFinal = 'Data='+strTemplateData;
    if(typeof window.top.document.forms['wdesk'] == 'undefined')
        window_workdesk = window.top.opener;
    else
        window_workdesk = window.top;
    if(changeEvt=='new'){
        var ActionObj = new net.ContentLoader('/webdesktop/ajaxActionObj.jsf?Option='+genResActionIndex+'&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid),actionHandler,null,"POST",dataStrFinal,false);
    }
    else{
        var ActionObj = new net.ContentLoader('/webdesktop/ajaxActionObj.jsf?Option='+genResActionIndex+'&genDocIndex='+genDocIndex+'&changeEvt='+changeEvt+'&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid),actionHandler,null,"POST",dataStrFinal,false);
    }

}

var genDocListRes="";
function openActionWindow()
{
    isActionClick=false;
    if(this.req.responseText == 'ConditionFailed'){
        alert(ALT_ACTION_CONDITION_FAILED);
        return;
    }
    else if(this.req.responseText == 'GlbFailed')
    {
        alert(DYNAMIC_CONST_COND_FAILED);
        return;
    }
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    if(typeof obj == 'undefined')
    {
        pid = window.opener.document.getElementById('wdesk:pid').value;
        wid = window.opener.document.getElementById('wdesk:wid').value;
        taskid = window.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }
    var window_workdesk="";
    if(typeof window.top.document.forms['wdesk'] == 'undefined')
        window_workdesk = window.top.opener;
    else
        window_workdesk = window.top;
    var triggerResponse=eval("("+this.req.responseText+")");
    genDocListRes = triggerResponse;
    var trigRes = eval("("+triggerResponse[0].actionResponse+")");
    if(trigRes[0].combo.length > 0)
    {
        openActionWin(trigRes[0].combo,trigRes[0].DocType);
    }
    else
    {
        var strTemplateData="";
        if(typeof templateData!='undefined'){
            strTemplateData=templateData();
        }
        if(typeof strTemplateData!='undefined'){
            strTemplateData=encode_utf8(strTemplateData);
        }
        else
            strTemplateData="";
        var dataStrFinal = 'Data='+strTemplateData;

        var ActionObj = new net.ContentLoader('/webdesktop/ajaxActionObj.jsf?Option='+genResActionIndex+'&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&WD_UID='+WD_UID,actionHandler,null,"POST",dataStrFinal,false);
       }
}

function openActionWin(docArray,genDocType){

    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    var uploadLimit = getUploadMaxLength(strprocessname, stractivityName,'');
    if(typeof obj == 'undefined')
    {
        if(window.opener.isFormLoaded==false)
            return;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        if(isFormLoaded==false)
            return;
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }
    var returnval = isDocTypeAddable(pid,wid,'',taskid) ;
    if(returnval == false || returnval == 'false')
        return false;
    if(typeof obj == 'undefined')
        var url = '/webdesktop/components/workitem/document/genresdocument/actionwin.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&winloc=T'+'&UploadLimit='+uploadLimit+'&docType='+encode_utf8(genDocType)+'&rid='+MakeUniqueNumber()+"&WD_UID="+WD_UID;
    else
        var url = '/webdesktop/components/workitem/document/genresdocument/actionwin.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&UploadLimit='+uploadLimit +'&docType='+encode_utf8(genDocType)+'&rid='+MakeUniqueNumber()+"&WD_UID="+WD_UID;
    url = appendUrlSession(url);
    //var win = link_popup(url,'GenResDoc','resizable=no,width='+(wratio*windowW)+',height='+(hratio*windowH)+',left='+(windowY/wratio)+',top='+(windowX/hratio)+',resize=yes',windowList,false);
    
    var width = 500;
    var height = 300;
    var left = (window.screen.width - width)/2 + window.document.documentElement.scrollLeft;
    var top = (document.documentElement.clientHeight - height)/2 + window.document.documentElement.scrollTop;
    var win = link_popup(url,'GenResDoc','resizable=no,width='+width+',height='+height+',left='+left+',top='+top+',resize=yes',windowList,false);
}

function actionCollaborationHandler(actionJSON){
    var obj = document.forms['wdesk'];
    var window_workdesk="";
    if(typeof window.top.document.forms['wdesk'] == 'undefined')
        window_workdesk = window.top.opener;
    else
        window_workdesk = window.top;
    if(isEmbd == 'Y'){
        window_workdesk = window;
    }
    var winList = window_workdesk.windowList;
    var formWindow = getWindowHandler(winList,"formGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var actionList = actionJSON.ActionRspnsTxt;
    var actionStatus = true;
    if(actionList.length==1)
    {
        if(typeof obj == 'undefined')
            window.opener.saveCalled=true;
        else
            saveCalled=true;
    }
    for(var k =0;k<actionList.length;k++)
    {
        var actionResponse = actionList[k+1].Action;
         if(actionResponse[k].ActionType!='Release')
           {
             if(typeof obj == 'undefined')
                      window.opener.saveCalled=true;
                else
                      saveCalled=true;
          }
        if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Trig'))
        {
            var temp = actionResponse[k+1].actionResponse;
             //check added for Bug 41452 - Launch Application Trigger not working for mspaint.
             //triggerDisplay(temp,'action');
             
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Set'))
        {
            var temp = actionResponse;
            if(formWindow)
            {
                for(var i=1;i<temp.length;i++){
                    var valSetter=temp[i];
                    window_workdesk.setFormValue(valSetter.name,valSetter.value,valSetter.type);
                }
            }
        }
         else if((actionResponse[k].flag=='1') && (actionResponse[k].ActionType=='Set'))
        {
            var temp = actionResponse;
            if(formWindow)
            {
                for(var i=1;i<temp.length;i++){
                    var valSetter=temp[i];
                    window_workdesk.setFormValue(valSetter.name,valSetter.value,valSetter.type);
                }
            }
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Priority'))
        {
            // Bug Id: 28769
            window.focus();
            //-------------
			 //alert(actionResponse[k+1].errMsg);
                         //setmessageinDivSuccess(actionResponse[k+1].errMsg,"true",3000);
                         showSuccessMsg = false;
        }
        else if((actionResponse[k].flag=='0') && (actionResponse[k].ActionType=='Excep'))
        {
            if(actionResponse.length > 1){
/*                if(excpWindow)
                     updateExcpWin(excpWindow,actionResponse[k+1]);

                if(actionResponse[k+1].isTrig == "Y"){
                    var temp = actionResponse[k+1].trig;
                    //temp = eval("("+temp+")");
                    //triggerDisplay(temp,'action');
                }*/
            }
        }
       k++;
    }
    notifier(actionJSON.UserName+ACTION_EXECUTED+actionList[0].Actions);
}

function addGenResDoc(docName, docIndex, diskName, docWindow, docOrgName){
    if(docIndex != ''){
        var objCombo = docWindow.document.getElementById('wdesk:docCombo');
        var opt = docWindow.document.createElement("OPTION");
        if(docOrgName == 'Y')
            opt.text = docName + "(" + diskName + ")";
        else
            opt.text = docName;
        opt.value = docIndex;
    if(changeEvt=='new' || changeEvt=='' ){
            objCombo.add(opt);
            objCombo.selectedIndex = objCombo.options.length-1;
        }
        else{
            for(var count=0;count<objCombo.length;count++)
                {
                    if(objCombo[count].value == genDocIndex)
                    {
                        selectedindex = count;
                        objCombo[count].value = docIndex;
                        objCombo[count].text = docName;
                        break;
                     }
                }
            objCombo.selectedIndex = selectedindex;
        }
        docWindow.reloadapplet(docIndex,false,changeEvt);
    }

     var genDocWindow=getWindowHandler(windowList,"GenResDoc");
     if(genDocWindow){
        genDocWindow.close();
	}

}

function openProperties(winloc,event){
    event = (typeof event == 'undefined')? null: event;
    if(winloc == 'S')
        wdesk_win = window;
    else if(winloc == 'T')
        wdesk_win = window.opener;
    var pid=wdesk_win.document.getElementById('wdesk:pid');
    var wid=wdesk_win.document.getElementById('wdesk:wid');
    var taskid=wdesk_win.document.getElementById('wdesk:taskid');
    var archivalMode = wdesk_win.ArchivalMode;
    url = sContextPath+'/components/workitem/operations/workitemproperties.jsf?Action=1&ProcessInstanceID='+encode_utf8(pid.value)+'&WorkitemID='+encode_utf8(wid.value)+'&taskid='+encode_utf8(taskid.value)+"&ArchivalMode="+encode_utf8(archivalMode);

    var windowHeight=420;
    var windowWidth=600;

    var left = (window.screen.width - windowWidth)/2;
    var top = (document.documentElement.clientHeight - windowHeight)/2;

 //   popupIFrameOpener(this, "WIProperties", url, 600, 410, left, top, false, false, false, true);

    var wFeatures = 'height='+windowHeight+',width='+windowWidth+',resizable=0,status=1,scrollbars=auto,top='+top+',left='+left;

    openNewWindow(url,'WIProperties',wFeatures, true,"Ext1","Ext2","Ext3","Ext4","");

    //url = appendUrlSession(url);
    //var win =  link_popup(url,'HistWI','scrollbars=yes,width=795,height='+(window.screen.availHeight-50)+',left='+(window.screen.availWidth/2-397)+',top=0',windowList,false);//WCL_8.0_087
    cancelBubble(event); 
}

function closewin(val,pName)
{
    if(window.showModalDialog)
        window.returnValue=val;
    else{
        if(pName=='introduce1'){

            window.opener.handleIntroduceWI(val);
        }
        else if(pName=='done1'){

            window.opener.handleDoneWI(val);
        }
        else if(pName=='freeWorkitem1'){
            window.opener.handleunlockreq(val);
        }
        else if(pName=='browseNextWorkitem'){
            window.opener.handleprevnext(val,"n");
        }
        else if(pName=='browsePrevWorkitem'){
            window.opener.handleprevnext(val,"p");
        }
        else if(pName=='refer1'){
            window.opener.handlereferwi(val);
        }
        else if(pName=='reassign1'){
            window.opener.handlereassignwi(val);
        }
        else if(pName=='DeleteQueryFire'){
            window.opener.DeleteQueryFire(val);
        }
        else if(pName=='accept'){
            window.opener.handleAudit(val,"A");
        }
        else if(pName=='reject'){            
            window.opener.handleAudit(val,"R");
        }
        else if(pName=='lockforme1'){
            window.opener.handleLockForMeWI(val);
        }
        else if(pName=='release1'){
            window.opener.handleReleaseWI(val);
        }
    }
    window.close();
}

function viewLinkedWi(event)
{
    event = (typeof event == 'undefined')? null: event;
    var pid='';
    var wid='';
    var taskid='';
    if(document.getElementById('wdesk')){
         pid=document.getElementById('wdesk:pid').value;
         wid=document.getElementById('wdesk:wid').value;
         taskid=document.getElementById('wdesk:taskid').value;
    }
    else{
         pid=window.opener.document.getElementById('wdesk:pid').value;
         wid=window.opener.document.getElementById('wdesk:wid').value;
         taskid=window.opener.document.getElementById('wdesk:taskid').value;
    }
    var url = sContextPath+'/components/workitem/operations/link/link_Wi.jsf';
    url = appendUrlSession(url);

    var left = (window.screen.width - 600)/2;
    var top = (document.documentElement.clientHeight - 380)/2;
    var wFeatures = 'scrollbars=no,status=yes,width=600,height=380,left='+left+',top='+top;

    var listParam=new Array();
    listParam.push(new Array("OpenFrom",encode_ParamValue('Link')));
    listParam.push(new Array("pid",encode_ParamValue(pid)));
    listParam.push(new Array("wid",encode_ParamValue(wid)));
    listParam.push(new Array("taskid",encode_ParamValue(taskid)));
    listParam.push(new Array("opt",encode_ParamValue('linkWi')));
    listParam.push(new Array("SortOrder",encode_ParamValue('A')));
    listParam.push(new Array("InstrumentListCallFlag",encode_ParamValue('L')));
    listParam.push(new Array("rid",encode_ParamValue(MakeUniqueNumber())));

    var win = openNewWindow(url,'linkWi',wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);
    cancelBubble(event); 
    // var url = '/webdesktop/faces/workitem/operations/link/link_Wi.jsp?OpenFrom=Link&Pid='+encode_utf8(pid)+'&Wid='+wid+'&opt=linkWi&rid='+MakeUniqueNumber();
    //var win = window.open(url,'linkWi','scrollbars=no,width='+window1W+',height='+window1H+',left='+window1Y+',top='+window1X);
}

function viewSearchedWi()
{

    var pid='';
    var wid='';
    var taskid='';
    if(document.getElementById('wdesk')){
         pid=document.getElementById('wdesk:pid').value;
         wid=document.getElementById('wdesk:wid').value;
         taskid=document.getElementById('wdesk:taskid').value;
    }
    else{
         pid=window.opener.document.getElementById('wdesk:pid').value;
         wid=window.opener.document.getElementById('wdesk:wid').value;
         taskid=window.opener.document.getElementById('wdesk:taskid').value;
    }
    var url = sContextPath+'/components/workitem/operations/link/link_Wi.jsf';
    url = appendUrlSession(url);
    var wFeatures = 'scrollbars=no,status=yes,width=600,height=380,left=100,top=100';

    var listParam=new Array();
    listParam.push(new Array("OpenFrom",encode_ParamValue('Link')));
    listParam.push(new Array("pid",encode_ParamValue(pid)));
    listParam.push(new Array("wid",encode_ParamValue(wid)));
    listParam.push(new Array("taskid",encode_ParamValue(taskid)));
    listParam.push(new Array("opt",encode_ParamValue('Search')));
    listParam.push(new Array("SortOrder",encode_ParamValue('A')));
    listParam.push(new Array("InstrumentListCallFlag",encode_ParamValue('L')));
    listParam.push(new Array("rid",encode_ParamValue(MakeUniqueNumber())));

    var win = openNewWindow(url,'linkWi',wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);

    // var url = '/webdesktop/faces/workitem/operations/link/link_Wi.jsp?OpenFrom=Link&Pid='+encode_utf8(pid)+'&Wid='+wid+'&opt=linkWi&rid='+MakeUniqueNumber();
    //var win = window.open(url,'linkWi','scrollbars=no,width='+window1W+',height='+window1H+',left='+window1Y+',top='+window1X);
}



function searchDoc(event)
{
    event = (typeof event == 'undefined')? null: event;
    var height=window3H;
    var left = (window.screen.width - window3W)/2;
    var top = (document.documentElement.clientHeight - height)/2;
    
    var pid = document.getElementById('wdesk:pid').value;
    var wid = document.getElementById('wdesk:wid').value;    
    var url = sContextPath+"/components/workitem/document/odsearch.jsf?FoldDocFlag=DS&rid="+MakeUniqueNumber()+"&Option=SearchExtendSession"+"&pid="+encode_utf8(pid)+"&wid="+wid;    
    var win = link_popup(url,"SrchDocFld",'scrollbars=yes,status=yes,width='+window3W+',height='+height+',left='+left+',top='+top,windowList,false);
    cancelBubble(event);
}

var addDocWin = null;
function openAddDocWin(event)
{
    event = (typeof event == 'undefined')? null: event;
    var height=window3H;
    var left = (window.screen.width - window3W)/2;
    var top = (document.documentElement.clientHeight - height)/2;

    var pid = document.getElementById('wdesk:pid').value;
    var wid = document.getElementById('wdesk:wid').value;
    var url = sContextPath+"/components/workitem/document/odadddoc.jsf?rid="+MakeUniqueNumber()+"&pid="+encode_utf8(pid)+"&wid="+wid;
        
    if((typeof addDocWin != 'undefined') && (addDocWin!=null)){
        if(addDocWin.closed){
            addDocWin=null;
        }
    }
   
    var win = null;
    if(addDocWin == null){
        win = link_popup(url,"ODAddDoc",'resizable=1,scrollbars=yes,status=yes,width='+window3W+',height='+height+',left='+left+',top='+top,windowList,false);
        addDocWin = win;
    } else {
        addDocWin.focus();
    }
    cancelBubble(event);
    
}

function searchFolder(event)
{
    event = (typeof event == 'undefined')? null: event;
    var height=window3H;
    var left = (window.screen.width - window3W)/2;
    var top = (document.documentElement.clientHeight - height)/2;

    var pid = document.getElementById('wdesk:pid').value;
    var wid = document.getElementById('wdesk:wid').value;
    var url = sContextPath+"/components/workitem/document/odsearch.jsf?FoldDocFlag=F&rid="+MakeUniqueNumber()+"&Option=SearchExtendSession"+"&pid="+encode_utf8(pid)+"&wid="+wid;
    var win = link_popup(url,"SrchDocFld",'scrollbars=yes,status=yes,width='+window3W+',height='+height+',left='+left+',top='+top,windowList,false);
    cancelBubble(event);
}

function closeWorkdesk(){
    if(SharingMode){
        if(collaborationMode == "CR"){
            leaveSharing("MenuClose");
            
            window.onbeforeunload = null;            
            window.top.close();                    
            return;
        }
    }
    
    /*** Do not close the workitem on clicking X in save workitem modal dialog ***/
    saveCheck= true;

    if(wiproperty.formType=="CUSTOMFORM") {
        saveCalled=true;
    }
    

    if(wiproperty.formType=="NGFORM" && saveCalled!=true)
        ngformSaveCalled();
    
    if(taskid!=null && taskid!="" && wiproperty.TaskFormType=="NGFORM"){
        saveCalled=false;
        if(wiproperty.TaskFormType=="NGFORM" && saveCalled!=true)
            taskngformSaveCalled();
    }
     if(isCloseConfirmationRequired=='N'){ //Bug 55065
       saveCalled=false;  
     }
     
    if(isOnlyCloseConfirRequired=='Y'){
        var retval = confirm(DO_YOU_WANT_TO_CLOSE_WORKITEM);
        if(!retval)
            return false; 
        else
        {
            saveCalled=false;
        }
    } 

    if(!freeWorkitem('','menuWinClose'))
    {
        return false;
    }
        
    //saveWorkDeskLayout('C');//For unlocking workitem on close of workitem
    
     /*if(wiproperty.locked=="Y" && strRemovefrommap=="Y"){          
          removefrommap();
     }*/
    
    /*** Do not close the workitem on clicking X in save workitem modal dialog ***/
    if((unlockflag=="Y" && wiproperty.locked!="Y") && (wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm && !bAllDeviceForm){
        
    } else {
        window.top.close();
    }
}

function doCleanUp(from, skipWiSave)
{   
    if(typeof from == 'undefined'){
        from = '';
    }
        
    skipWiSave = (typeof skipWiSave == 'undefined')? false: skipWiSave;
    
    try{
        if(SharingMode){
            if(collaborationMode == "CR"){
                leaveSharing("WindowClose");       
                window.top.close();
                afterDoCleanup(from);            
                return;
            }
        }

        if(typeof bEscapeSave != 'undefined'){        
            if(bEscapeSave){
                return;
            }
        }

        if(wiproperty.formType=="NGFORM" && saveCalled!=true && !skipWiSave)
            ngformSaveCalled();
        
        if (taskid != null && taskid != "" && wiproperty.TaskFormType == "NGFORM") { // Handling for task
            saveCalled = false;
            if (wiproperty.TaskFormType == "NGFORM" && saveCalled != true)
                taskngformSaveCalled();
        }
        
        isSaveAnnotation();

        if(wiproperty.formType=="CUSTOMFORM" && !skipWiSave) {
            saveCalled=true;
        }   
        
        var ngformIframe = document.getElementById("ngformIframe");	
        if(ngformIframe != null && (from != "UnloadWIEvent")){
                try{
                    ngformIframe.contentWindow.clearNGHTMLViewMap();
                }catch(e){}
        }
                

       /*** Do not close the workitem on clicking X in save workitem modal dialog ***/
        if(!saveCheck){
            freeWorkitem('',from);
        }

        if(SharingMode){
            if(collaborationMode == "CO"){
                unshareWi("MenuClose");
            }
        }    

       /*** Do not close the workitem on clicking X in save workitem modal dialog ***/
        saveCheck=false;
        closewindows(windowList);
        windowList.clear();
        if(customChildCount!=-1)    {
            for(i=0;i<=customChildCount;i++)
                if (customChild[i] && !customChild[i].closed)
                    customChild[i].close();
        }    
    }catch(e){}
    
    try{
        SetCookies();
        if(window.opener)
            removeFromArray(window.opener.workitemList,winname);
    }
    catch(e){
    ;
    }
        
    afterDoCleanup(from);
    
    stopPrintScreen();
}

function afterDoCleanup(from){
    if(from == "beforeunload"){        
        if(typeof window.opener != 'undefined'){
            if(typeof window.opener.afterWIDoCleanup != 'undefined'){
                window.opener.afterWIDoCleanup(window.name, false);
            }
        }
    }
}

function ngformSaveCalled()
{
   if(wiproperty.formType=="NGFORM")
   {
       var formBuffer1;
       
       if(!(ngformproperty.type=="applet")){
           if((typeof document.wdgc != 'undefined') && document.wdgc){
                var MAIN_SEPARATOR = "\u00B6";
                formBuffer1 = new String(document.wdgc.FieldValueBag);
                var tempArray = formBuffer.split(MAIN_SEPARATOR);
                var formArray = formBuffer1.split(MAIN_SEPARATOR);
                for ( var i=0;i<formArray.length;i++) {
                   if(formArray[i]!=tempArray[i])
                      saveCalled=true;
                }
           }
        }
       else
       {
           if(!bDefaultNGForm){   
                var ngformIframe = document.getElementById("ngformIframe");	
                if(ngformIframe != null){
                    var isValueChanged = false;
                    
                    try{
                        if(bAllDeviceForm){
                            isValueChanged = ngformIframe.contentWindow.eval("isValueChanged()");	
                        } else {
                            isValueChanged = ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.isValueChanged()");
                        }
                    } catch(e){                        
                    }
                    
                    if(isValueChanged){
                        saveCalled=true;
                    }
                }    
           }else {
               if((typeof document.wdgc != 'undefined') && document.wdgc){
                    if(typeof document.wdgc.getFieldValueBagEx != 'undefined'){
                        formBuffer1=document.wdgc.getFieldValueBagEx();
                        //formBuffer1=formBuffer1+"";
                        if(formBuffer1!=formBuffer)
                            saveCalled=true;
                    }
                }
            }
        }        
    }
    else if(wiproperty.formType=="CUSTOMFORM")
        saveCalled=true;
}

function taskngformSaveCalled()
{
    if(wiproperty.TaskFormType=="NGFORM")
    {
        var formBuffer1;
       
        if(!(taskngformproperty.type=="applet")){
            if((typeof document.wdgc != 'undefined') && document.wdgc){
                var MAIN_SEPARATOR = "\u00B6";
                formBuffer1 = new String(document.wdgc.FieldValueBag);
                var tempArray = formBuffer.split(MAIN_SEPARATOR);
                var formArray = formBuffer1.split(MAIN_SEPARATOR);
                for ( var i=0;i<formArray.length;i++) {
                    if(formArray[i]!=tempArray[i])
                        saveCalled=true;
                }
            }
        }
        else
        {
            if(!bDefaultNGForm){   
                var taskngformIframe = document.getElementById("taskngformIframe");	
                if(taskngformIframe != null){
                    var isValueChanged = false;
                    
                    try{
                        isValueChanged = taskngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.isValueChanged()");	
                    } catch(e){                        
                    }
                    
                    if(isValueChanged){
                        saveCalled=true;
                    }
                }    
            }else {
                if((typeof document.wdgc != 'undefined') && document.wdgc){
                    if(typeof document.wdgc.getFieldValueBagEx != 'undefined'){
                        formBuffer1=document.wdgc.getFieldValueBagEx();
                        //formBuffer1=formBuffer1+"";
                        if(formBuffer1!=formBuffer)
                            saveCalled=true;
                    }
                }
            }
        }        
    }
   else if(wiproperty.formType=="CUSTOMFORM")
       saveCalled=true;
}

function SetCookies()
{
	var obj = document.IVApplet;
        if(isOpAll=='Y'){
        obj = opall_toolkit;  
    }
	if(typeof obj != 'undefined')
	{

		try
		{
			var resizePercentage = 8;
			if(isOpAll=='N'){
                            resizePercentage = obj.getZoom();
                        } else {
                            resizePercentage = obj.getZoom().resizePercentage;
                        }
			var zoomLens = 0;
			zoomLens = obj.isZoomLensOpen();
			var zoomLensPercentage = 0;
                        var penThickness=3;
			if(zoomLens == 1)
			{
				zoomLensPercentage = obj.getZoomLensPercent();
				zoomLensPercentage += '%';
			}
			var today = new Date();
                        //if(obj.getPenThickness()>0)
                         //   penThickness=obj.getPenThickness();
			var expiry = new Date(today.getTime() + 28 * 24 * 60 * 60 * 1000); // 28 days
			document.cookie = "ResizePercentage=" + resizePercentage + ";  path=/; expires=" + expiry.toGMTString() ;
			document.cookie = "ZoomLens=" + zoomLens + "; path=/; expires=" + expiry.toGMTString() ;
			document.cookie = "ZoomLensPercentage=" + zoomLensPercentage + "; path=/; expires=" + expiry.toGMTString();
                        document.cookie = "PenThickness=" + penThickness + ";  path=/; expires=" + expiry.toGMTString() ;
                        if(isOpAll=='Y'){
                            var resizeOption = 0;
                            resizeOption = obj.getZoom().resizeOption;
                            document.cookie = "ResizeOption=" + resizeOption + ";  path=/; expires=" + expiry.toGMTString() ;
                         }
                }
		catch(ex)
		{
			;
		}

	}


}


function openSearchWin(event)
{
    event = (typeof event == 'undefined')? null: event;
    var browser;
    if(navigator.appName!="Netscape")
     browser="explorer";
    else
        browser="netscape"
    var url = sContextPath+ '/components/search/searchworklist.jsf?Action=1&OpenFrom=Link&browser='+browser+'&processRightsFlag='+'Y'+'&isSearch='+'Y';
    url = appendUrlSession(url);

    var WindowHeight=500;
    var WindowWidth=620;
    
    var isChrome = window.chrome;
    if(typeof isChrome != 'undefined') {
        WindowHeight=WindowHeight+15;
        WindowWidth=WindowWidth+15;
    }
    var left = (window.screen.width - 620)/2;
    var top = (document.documentElement.clientHeight - 420)/2;

   link_popup(url,'searchWin','width='+WindowWidth+',height='+WindowHeight+',toolbar=no,status=yes,resizable=no,menubar=no,scrollbars=no,left='+left+',top='+top,windowList,false);
   cancelBubble(event);
   //addWindowToParent(windowList, "searchWin");
}
function openPreferences()
{
    url = sContextPath+'/components/workitem/operations/preferences.jsf?Action=1';

    var left = (window.screen.width - 600)/2;
    var top = (document.documentElement.clientHeight - 410)/2;

    popupIFrameOpener(this, "Preferences", url, 600, 410, left, top, false, false, false, true);

}

function showProcessing(){
    var divx= document.createElement("div");
    var imgx=document.createElement("img");
    imgx.src=sContextPath+"/resources/images/progress.gif";
    divx.appendChild(imgx);
    divx.style.position="absolute";
    divx.style.left=document.body.clientWidth/2-20 + "px";
    divx.style.top=28 + "px";

//    if(isEmbd == 'Y') {
//        if(document.getElementById("wdesk:indicatorPG"))
//            document.getElementById("wdesk:indicatorPG").style.display = "inline";
//        divx.style.top=29;
//    }

    document.body.appendChild(divx);
    divx.id="msgdiv"
}
function hideProcessing(){
    var divy=document.getElementById("msgdiv");
    if(divy)
        document.body.removeChild(divy);
//    if(isEmbd == 'Y' && document.getElementById("wdesk:indicatorPG"))
//        document.getElementById("wdesk:indicatorPG").style.display = "none";
   }

function displayMenu(divID){
    var mainMenuString="";
    if(myMenu.length > 0){
        for(var i=0;i<myMenu.length-1;i++){           
            mainMenuString+="<a style='cursor:pointer;' title='"+myMenu[i][2]+"' onClick='"+myMenu[i][1]+"'>"+myMenu[i][0]+"</a><span class='separatorclass'>|</span>" ;
        }
        mainMenuString+="<a style='cursor:pointer;' title='"+myMenu[i][2]+"' onClick='"+myMenu[i][1]+"'>"+myMenu[i][0]+"</a>" ;
        var menudiv=document.getElementById(divID);
        menudiv.innerHTML=mainMenuString;
    } else {
        document.getElementById(divID).innerHTML="";
    }
}

function clickRefer(winloc){
    if(winloc == 'S')
        wdesk_win = window;
    else if(winloc == 'T')
        wdesk_win = window.opener;
    var pid=wdesk_win.document.getElementById('wdesk:pid');
    var wid=wdesk_win.document.getElementById('wdesk:wid');
    var taskid=wdesk_win.document.getElementById('wdesk:taskid');
	url = sContextPath+'/components/workitem/operations/referworkitem.jsf?Action=1&OpenFrom=WI&pid='+encode_utf8(pid.value)+'&wid='+encode_utf8(wid.value)+'&taskid='+encode_utf8(taskid.value)+'&viewflag=V&winloc='+winloc+'&WD_UID='+WD_UID;
	url = appendUrlSession(url);
        var left = (window.screen.width - 375)/2;
        var top = (document.documentElement.clientHeight - 290)/2;
        popupIFrameOpener(wdesk_win, "Refer", url, 375, 290, left, top, false, true, false, false, true);
    //var win = link_popup(url, 'refer', 'resizable=no,scrollbars=yes,width='+windowW+',height='+windowH+',left='+windowY+',top='+windowX,windowList,false);
}

function clickReassign(winloc){
    if(winloc == 'S')
        wdesk_win = window;
    else if(winloc == 'T')
        wdesk_win = window.opener;
    if(mandatoryCommentsBeforeReassign(strprocessname,stractivityName)){
        var pid=wdesk_win.document.getElementById('wdesk:pid');
        var wid=wdesk_win.document.getElementById('wdesk:wid');
            var taskid=wdesk_win.document.getElementById('wdesk:taskid');
            url = sContextPath+'/components/workitem/operations/reassignworkitem.jsf?Action=1&OpenFrom=WI&pid='+encode_utf8(pid.value)+'&wid='+encode_utf8(wid.value)+'&taskid='+encode_utf8(taskid.value)+'&viewflag=V&winloc='+winloc+'&WD_UID='+WD_UID+'&wLMode='+wLMode;
            url = appendUrlSession(url);
            var left = (window.screen.width - 350)/2;
            var top = (document.documentElement.clientHeight - 290)/2;
            popupIFrameOpener(wdesk_win, "Reassign", url, 350, 290, left, top, false, true, false, false, true);
        //var win = link_popup(url, 'refer', 'resizable=no,scrollbars=yes,width='+windowW+',height='+windowH+',left='+windowY+',top='+windowX,windowList,false);
    }
}

function clickRevoke(winloc){
        if(winloc == 'S')
            wdesk_win = window;
        else if(winloc == 'T')
            wdesk_win = window.opener;
        var dataFrm = wdesk_win.document.getElementById('wdesk');
        dataFrm.CloseWindow.value='false';
        if(window.CalledFrom == "M") {
            dataFrm.CloseWindow.value='true';
            dataFrm.CalledFrom.value='M';
        }
        
        wdesk_win.unlockflag="N";
        wdesk_win.clickWiLink("REVOKESAVE","wdesk:controller");
}

function viewCommentWi(){
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
        var url = 'viewcomments.jsf?Action=comments&wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&rid='+MakeUniqueNumber();
	url = appendUrlSession(url);
        var win =  link_popup(url,'comm','resizable=no,status=yes,scrollbars=auto,width='+(windowW+150)+',height='+(windowH+25)+',left='+windowY+',top='+windowX+',resize=yes',windowList,false);
}

var act_List="";
function showActions(list)
{
    var actList = '';
    for(var i=0;i<list.length;i++)
        actList += list[i].name + SEPERATOR1 + list[i].index + SEPERATOR2;
    act_List=list;
    url = sContextPath+'/components/workitem/operations/showactions.jsf?ActList='+encode_utf8(actList);
    moreactionString=actList;
    url = appendUrlSession(url);
    //var win = link_popup(url,'MoreAct','scrollbars=yes,status=yes,width='+100+',height='+150+',left='+windowY+',top='+windowX,windowList,false);
    
    var windowWidth=200;
    var windowHeight=220;    
    var left = (window.screen.width - windowWidth)/2;
    var top = (document.documentElement.clientHeight - windowHeight)/2;
    
    //popupIFrameOpener(this, "MoreAct", url, windowWidth, windowHeight, left, top, false, true, false, false, true, false);
    
    if(typeof isEmbd != 'undefined' && (isEmbd=='Y')){
        popupIFrameOpener(this, "MoreAct", url, windowWidth, windowHeight, left, top, false, true, false, true, true, false);
    }else {
        popupIFrameOpener(this, "MoreAct", url, windowWidth, windowHeight, left, top, false, true, false, false, true, false);
    }
    
    var iframeRef = window.document.getElementById("popupIFrame_MoreAct");
    if(iframeRef != null){
        if(typeof isEmbd != 'undefined' && (isEmbd=='Y')){
            
            if(typeof pageDirection != 'undefined'){         
                if(pageDirection == 'rtl'){
                    iframeRef.style.left = "10px";   
                } else {                      
                    iframeRef.style.left = compWidth -windowWidth -10+ "px";
                }
             }
            
            iframeRef.style.top = "25px";
            iframeRef.style.display = "inline";
        }else {
            var parentScreenWidth = window.screen.width - 10;               
            var maxRightPos = getMaxWidth();    
            if(maxRightPos > parentScreenWidth){
                parentScreenWidth = maxRightPos;
            }
            
            if(typeof pageDirection != 'undefined'){         
                if(pageDirection == 'rtl'){
                    iframeRef.style.left = "10px";   
                } else {                      
                    iframeRef.style.left = parentScreenWidth - windowWidth - 10 + "px";
                }
             }
            
            iframeRef.style.top = "25px";
            iframeRef.style.display = "inline";
        }
    }
}

function displayAction(index,name,isGenRep)
{
    var displayText = '<a href="javascript:void(0)" class=fieldlabel ';
    //displayText += 'onclick="window.blur();executeAction('+index+');window.focus();return false;">';
    displayText += 'onclick="window.parent.executeAction(\''+index+'\',\''+isGenRep+'\');closeBp();return false;">';
    displayText+=getShortNameTest(name,100) + '</a>';
    return displayText;
}

function getInterfaceData(opt,from){
    if(typeof from == 'undefined')
        from = 'C'
    if(document.getElementById('wdesk:pid')!=null){
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    } else if(window.parent.document.getElementById('wdesk:pid')!=null){
        pid = window.parent.document.getElementById('wdesk:pid').value;
        wid = window.parent.document.getElementById('wdesk:wid').value;
        taskid = window.parent.document.getElementById('wdesk:taskid').value;
    }
    
    var todoParam = '';
    var isTodo = 'N';
    var serverResponse;
    var xhReq;
    if (window.XMLHttpRequest)
        xhReq = new XMLHttpRequest();
    else if (window.ActiveXObject)
        xhReq = new ActiveXObject("Microsoft.XMLHTTP");
    var url = '../ajaxinterface.jsf';
    if(opt == 'E' && from == 'F'){
        var excpWindow=getWindowHandler(windowList,"exceptionGrid");
        if(typeof excpWindow !='undefined'){
            if(excpWindow.CheckExpTempStatus(excpWindow)){
                alert(COMMIT_EXCEPTION);
                return false;
            }
       }
    }
    if(opt == 'T' && from == 'F'){
        var todoWindow=getWindowHandler(windowList,"todoGrid");
        if(typeof todoWindow !='undefined'){
            var todoform= todoWindow.document.getElementById('wdesk');
            todoParam=todoWindow.getFormValuesForAjax(todoform);
            isTodo = 'Y';
        }
    }
    url = appendUrlSession(url);
    xhReq.open("POST", url, false);
    xhReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhReq.send(todoParam+'Opt='+opt+'&pid='+encode_utf8(pid)+'&wid='+wid+'&taskid='+taskid+'&isTodo='+isTodo+'&WD_UID='+WD_UID);
    if (xhReq.status == 200 && xhReq.readyState == 4) {
        var serverResponse = xhReq.responseText;
        if(opt=="DTYPE" || opt=="DLIST") {

        }
       else{
           serverResponse = eval("("+serverResponse+")");
       }
        return serverResponse;
   }
   else{
       if(xhReq.status == 599){
            //window.open(sContextPath+"/login/logout.jsp?"+"error=4020",reqSession);
            url = sContextPath+"/error/errorpage.jsf?msgID=4020";
            url = appendUrlSession(url);
            var width = 320;
            var height = 160;
            var left = (window.screen.availWidth-width)/2;
            var top = (window.screen.availHeight-height)/2;

            //window.open(url,reqSession);
            if (window.showModalDialog){
                window.showModalDialog(url,'',"dialogWidth:"+width +"px;dialogHeight:"+height+"px;center:yes;dialogleft: "+left+"px;dialogtop: "+top+"px");
            }
       }
       else if(xhReq.status == 400)
                  alert(INVALID_REQUEST_ERROR);
       else if(xhReq.status==12029){
            alert(ERROR_SERVER); 
        }
       else
            alert(ERROR_DATA);
   }
}


function SaveZone(currentPage, X1, Y1, X2, Y2) {
  var strlockStatus="N";
  if(typeof wiproperty == 'undefined') {
     strlockStatus=window.opener.wiproperty.locked;
  }else{
      strlockStatus=wiproperty.locked;
  }
    if(strlockStatus=="Y"){
         alert(LABEL_ALERT_CROP_READ_ONLY);
         return;
    }
    /*
     if(document.getElementById('wdesk:downloadDiv').style.display=="none"){
        alert(NO_AUTHORIZATION_TO_CROP);
        return;
    }
    */
    var strPageNo=0;
    if(typeof document.IVApplet != 'undefined')
    strPageNo=document.IVApplet.getCurrentPage();  
    else 
    strPageNo=opall_toolkit.getCurrentPage();   
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    var objCombo=document.getElementById('wdesk:docCombo');
    var docName=objCombo.options[objCombo.options.selectedIndex].text;
    if(typeof obj == 'undefined') {
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else {
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }
    if(typeof isCustomCrop!='undefined' && isCustomCrop=="Y" && typeof CustomCrop!='undefined' && CustomCrop(strprocessname,stractivityName)){
         var  fieldName=croppedByField(strprocessname,stractivityName);
         var url = '/webdesktop/components/workitem/document/customcropdoc.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&strDocName='+encode_utf8(docName)+'&croppedByField='+fieldName+'&winloc=T'+'&rid='+MakeUniqueNumber();
         //url = appendUrlSession(url);
         url = appendUrlSession(url)+"&WD_UID="+WD_UID+"&X1="+X1+"&Y1="+Y1+"&X2="+X2+"&Y2="+Y2+"&PageNo="+strPageNo;
         
         var formWindow = getWindowHandler(windowList, "formGrid");
            var ngformIframe = formWindow.document.getElementById("ngformIframe");
           if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm && !bAllDeviceForm){            
               if(formWindow){                   
                   if(ngformIframe != null){
                       // parameter passing           
                       var ngformIframeWindow = ngformIframe.contentWindow;
                       
                       var wiDummySaveStructWinRef = ngformIframeWindow;
                        if(formIntegrationApr == "4"){
                            wiDummySaveStructWinRef = window;
                        }
                        
                       wiDummySaveStructWinRef.WiDummySaveStruct.Type = 'SaveZone_CustomCrop';
                       wiDummySaveStructWinRef.WiDummySaveStruct.Params.window_workdesk = window; 
                       wiDummySaveStructWinRef.WiDummySaveStruct.Params.url = url; 
                       
                       if(formIntegrationApr == "4"){
                            ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('S','Y')");	                                                                
                       } else {						
                            ngformIframeWindow.clickLink('cmdNgFormDummySave');
                       }
                   }                   
               }
           } else {
               if(formWindow && bCustomIForm){
                if(ngformIframe != null){
                    if(typeof ngformIframe.contentWindow.customIFormHandler != 'undefined'){
                        ngformIframe.contentWindow.customIFormHandler('S','Y');
                    }
                }
            }
            
               commonSaveZone(url);
           }
   
         /*if(for_save('dummysave') == 'failure'){
             return false;
         }
         var url = '/webdesktop/components/workitem/document/customcropdoc.jsf?wid='+wid+'&pid='+encode_utf8(pid)+'&strDocName='+encode_utf8(docName)+'&croppedByField='+fieldName+'&winloc=T'+'&rid='+MakeUniqueNumber();

         url = appendUrlSession(url);
         var win = link_popup(url,'CropDoc','resizable=no,width='+(windowW-120)+',height='+(windowH-160)+',left='+windowY+',top='+windowX+',resize=yes',windowList,false);*/
    }else{
    var returnval = isDocTypeAddable(pid,wid,'',taskid) ;
    if(returnval == false || returnval == 'false')
        return false;
    var customCropVar="";
    var docTypeList=cropDocTypeList(strprocessname,stractivityName,strPageNo,docName);
    
    if(customCropVar!=""){
         if(typeof obj == 'undefined')
            var url = '/webdesktop/components/workitem/document/cropdocument.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&strDocName='+encode_utf8(docName)+'&cropVarName='+customCropVar+'&winloc=T'+'&rid='+MakeUniqueNumber()+'&customDocTypes='+docTypeList+"&X1="+X1+"&Y1="+Y1+"&X2="+X2+"&Y2="+Y2+"&PageNo="+strPageNo;
        else
             url = '/webdesktop/components/workitem/document/cropdocument.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&strDocName='+encode_utf8(docName)+'&cropVarName='+customCropVar+'&rid='+MakeUniqueNumber()+'&customDocTypes='+docTypeList+"&X1="+X1+"&Y1="+Y1+"&X2="+X2+"&Y2="+Y2+"&PageNo="+strPageNo;
    }else{
        if(typeof obj == 'undefined')
            url = '/webdesktop/components/workitem/document/cropdocument.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&strDocName='+encode_utf8(docName)+'&winloc=T'+'&rid='+MakeUniqueNumber()+'&customDocTypes='+docTypeList+"&X1="+X1+"&Y1="+Y1+"&X2="+X2+"&Y2="+Y2+"&PageNo="+strPageNo;
        else
            url = '/webdesktop/components/workitem/document/cropdocument.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&strDocName='+encode_utf8(docName)+'&rid='+MakeUniqueNumber()+'&customDocTypes='+docTypeList+"&X1="+X1+"&Y1="+Y1+"&X2="+X2+"&Y2="+Y2+"&PageNo="+strPageNo;
      }
    url = appendUrlSession(url);

    var win = link_popup(url,'CropDoc','resizable=no,status=yes,width='+(windowW-100)+',height='+(windowH-120)+',left='+windowY+',top='+windowX+',resize=yes',windowList,false);
}
}

function commonSaveZone(url){
    if(for_save('dummysave') == 'failure'){
         return false;
     }
    var win = link_popup(url,'CropDoc','resizable=no,width='+(windowW-120)+',height='+(windowH-160)+',left='+windowY+',top='+windowX+',resize=yes',windowList,false);
}

function cropImage() {
    var docWindow = window.top.opener;
    var ServletUrl = document.getElementById("cropForm:ServletUrl").value;
    if(typeof isCustomCrop!='undefined' && isCustomCrop=='Y'){
        var croppedImgName=document.getElementById("cropForm:CropImgName").value;
        croppedImgName=Trim(croppedImgName);
        if(croppedImgName==""){
            var cropImgLabel=document.getElementById("cropForm:CropImgLabel").innerHTML;
            fieldValidator("cropForm:CropImgName", ALERT_PLEASE_ENTER_THE_VALUE+cropImgLabel, "absolute", true);
            //alert(ALERT_PLEASE_ENTER_THE_VALUE+cropImgLabel);
            //document.getElementById("cropForm:CropImgName").focus();
            return false;
        }
        var queryString = "?croppedImgName="+encode_utf8(croppedImgName)+"&isCustomCrop=Y"+"&WD_UID="+WD_UID;
        //addDocXML = window.opener.document.IVApplet.getCroppedImage(ServletUrl+queryString);
        var addDocXML = "";
    
        if(window.opener.isOpAll=='N' && (typeof window.opener.document.IVApplet != undefined || typeof window.opener.document.IVApplet !='undefined'))
            addDocXML = window.opener.document.IVApplet.getCroppedImage(ServletUrl+queryString);
        else 
            addDocXML = window.opener.opall_toolkit.getCroppedImage(ServletUrl+queryString,X1,Y1,X2,Y2);

    }else{
        if(document.getElementById("cropForm:docListMenu")){
            documentName = document.getElementById("cropForm:docListMenu").options[document.getElementById("cropForm:docListMenu").selectedIndex].value;
        }
        else if(document.getElementById("cropForm:labelDoct")) {
            documentName = document.getElementById('cropForm:labelDoct').value;
        }
        var strConvTiff;
    if(window.opener.isOpAll=='Y' && isTiffConversionRequired(window.opener.strprocessname, window.opener.stractivityName, documentName)) {
        strConvTiff = "Y";
    } else {
        strConvTiff = "N";
    }
        if(Trim(documentName) == ""){
            fieldValidator("cropForm:labelDoct", ENTER_DOCTYPE, "absolute", true);
            //alert(ENTER_DOCTYPE);
            return;
        }
    
        if(!validateCropDocTypeName(documentName))
            return;
        var strCropDocumentName=documentName;
        var customMenu=document.getElementById("cropForm:CustomCombo");
        var selButton = document.getElementById('cropForm:selButton').value;

        if(customMenu){
            if(customMenu.options[customMenu.selectedIndex].value!="-1")
                strCropDocumentName=strCropDocumentName+"("+customMenu.options[customMenu.selectedIndex].value+")";
        }
        var docValue='';
        var objCombo1 = document.getElementById('cropForm:docListName');
        if(objCombo1.options.length!=0)
            docValue=objCombo1.options[objCombo1.options.selectedIndex].value;

        //Bug 55799 starts
        var strCropComments="";
        if(document.getElementById('cropForm:docCropComment'))
           strCropComments = document.getElementById('cropForm:docCropComment').value;
         else
        strCropComments = strCropDocumentName;  
        //Bug 55799 ends
   
        if(isIE())
        {
            if(document.getElementsByName('cropForm:addMode')[0].checked)
                cropAddMode = "new";
            else if(document.getElementsByName('cropForm:addMode')[1].checked)
                cropAddMode = document.getElementsByName('cropForm:addMode')[1].value;
            else
                cropAddMode = "deleteadd";
        }
        else
        {
            if(document.getElementsByName('cropForm:addMode')[0].checked)
                cropAddMode = "new";
            else if(document.getElementsByName('cropForm:addMode')[1].checked)
                cropAddMode = document.getElementsByName('cropForm:addMode')[1].value;
            else
                cropAddMode = "deleteadd";
        }

        strCropDocumentName = nameUploadDocument(strCropDocumentName,'cropDocument',window.opener.strprocessname,window.opener.stractivityName,window.opener.userName);

        var pid=document.getElementById("cropForm:pid").value;
        var wid=document.getElementById("cropForm:wid").value;
        var taskid=document.getElementById("cropForm:taskid").value;
        var docName=document.getElementById("cropForm:docName").value;
        var documentIndex = docWindow.document.getElementById('wdesk:docCombo').options[docWindow.document.getElementById('wdesk:docCombo').selectedIndex].value;
        var parentDocumentName = docWindow.document.getElementById('wdesk:docCombo').options[docWindow.document.getElementById('wdesk:docCombo').selectedIndex].text;
        var folderId = document.getElementById("cropForm:FolderId").value;
        var VolumeId = document.getElementById("cropForm:VolumeId").value;
        //var ServletUrl = document.getElementById("cropForm:ServletUrl").value;
        var queryString="";
        if(window.opener.isOpAll=='N' && (typeof window.opener.document.IVApplet != undefined || typeof window.opener.document.IVApplet !='undefined'))
        {
            if(cropAddMode != 'new')
                queryString = "?documentName="+strCropDocumentName+"&documentIndex="+documentIndex+"&parentDocumentName="+encode_utf8(parentDocumentName)+"&folderId="+folderId+"&VolumeId="+VolumeId+"&pid="+encode_utf8(pid)+"&wid="+wid+"&taskid="+taskid+"&croppedFromdocName="+encode_utf8(docName)+"&addMode="+cropAddMode+"&targetDocIndex="+encode_utf8(docValue)+"&CropComments="+strCropComments+"&WD_UID="+WD_UID;
            else
                queryString = "?documentName="+strCropDocumentName+"&documentIndex="+documentIndex+"&parentDocumentName="+encode_utf8(parentDocumentName)+"&folderId="+folderId+"&VolumeId="+VolumeId+"&pid="+encode_utf8(pid)+"&wid="+wid+"&taskid="+taskid+"&croppedFromdocName="+encode_utf8(docName)+"&addMode="+cropAddMode+"&CropComments="+strCropComments+"&WD_UID="+WD_UID;
        } else {
            if(cropAddMode != 'new')
                queryString = "?documentName="+strCropDocumentName+"&documentIndex="+documentIndex+"&parentDocumentName="+encode_utf8(parentDocumentName)+"&folderId="+folderId+"&VolumeId="+VolumeId+"&pid="+encode_utf8(pid)+"&wid="+wid+"&taskid="+taskid+"&croppedFromdocName="+encode_utf8(docName)+"&addMode="+cropAddMode+"&targetDocIndex="+encode_utf8(docValue)+"&Ext=jpg"+"&CropComments="+strCropComments+"&IsTiffConversionRequired="+strConvTiff+"&WD_UID="+WD_UID;
            else
                queryString = "?documentName="+strCropDocumentName+"&documentIndex="+documentIndex+"&parentDocumentName="+encode_utf8(parentDocumentName)+"&folderId="+folderId+"&VolumeId="+VolumeId+"&pid="+encode_utf8(pid)+"&wid="+wid+"&taskid="+taskid+"&croppedFromdocName="+encode_utf8(docName)+"&addMode="+cropAddMode+"&Ext=jpg"+"&CropComments="+strCropComments+"&IsTiffConversionRequired="+strConvTiff+"&WD_UID="+WD_UID;
        }
    
        var addDocXML = "";
    
        if(window.opener.isOpAll=='N' && (typeof window.opener.document.IVApplet != undefined || typeof window.opener.document.IVApplet !='undefined'))
            addDocXML = window.opener.document.IVApplet.getCroppedImage(ServletUrl+queryString);
        else 
            addDocXML = window.opener.opall_toolkit.getCroppedImage(ServletUrl+queryString,X1,Y1,X2,Y2);    
        if(addDocXML != -1){
            var servletResp = getVal(addDocXML,"Status");
    
            if(servletResp != "0") {
                alert(getVal(addDocXML,"Error"));
                return false;
            }
            
            window.opener.addCropDoc(addDocXML,cropAddMode,docValue);
        }
    }
    window.top.close();
}

function addCropDoc(addXML,cropAddMode,deletedDocIndex)
{
    var pid=document.getElementById('wdesk:pid').value;
    var wid=document.getElementById('wdesk:wid').value;
    var taskid=document.getElementById('wdesk:taskid').value;
    var url='../ajaxCustomDoc.jsf';
    url = appendUrlSession(url);
    var requestString='pid='+pid+'&wid='+wid+'&taskid='+taskid+'&docXML='+addXML;
    if(typeof cropAddMode!='undefined' && cropAddMode != '')
    {
        requestString = requestString +'&addMode=' +  cropAddMode +'&deletedDocIndex=' +  deletedDocIndex;
        var docLoader = new net.ContentLoader(url,docCropHandler,null,"POST",requestString);
    }
    else
        var docLoader = new net.ContentLoader(url,docHandler,null,"POST",requestString);
}
function docCropHandler()
{
    var response = this.req.responseText;
    response = eval("("+response+")");
    var docWindow = "";
    var docIndex = response.DocIndex;
    var docName = response.DocName;
    var diskName = response.DiskName;
    var selButton = response.selButton;
    var runScript = response.RunScript;
    var docType = response.docType;
    if(DocOrgName == 'Y')
        docName += "(" + diskName + ")";
    var  deletedDocIndex = response.deletedDocIndex;
    if(windowProperty.winloc == "M")
        docWindow = getWindowHandler(windowList,"tableGrid");
    else
        docWindow = getWindowHandler(window.opener.windowList,"tableGrid");
    if(docWindow){
        if(runScript=='true'){
            if(selButton == 'new')
            {
               var objCombo = docWindow.document.getElementById('wdesk:docCombo');

                if((navigator.appName=='Netscape')?false:true || isInternetExplorer11()){
                    var opt = docWindow.document.createElement("OPTION");
                    opt.text = docName;
                    opt.value = docIndex;
                    objCombo.add(opt);
                }
                else{
                    var option=new Option(docName,docIndex);
                    objCombo.options.add(option,objCombo.options.length);
                }
                objCombo.selectedIndex = objCombo.options.length-1;
                docWindow.reloadapplet(docIndex);
            }
            else if(selButton == 'newversion')
            {
                var objCombo = docWindow.document.getElementById('wdesk:docCombo');
                var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                     if(objCombo[count].value == docIndex)
                    {
                        selectedindex = count;
                        objCombo[count].text = docName;
                        break;
                    }
                }
                objCombo.selectedIndex = selectedindex;
                docWindow.reloadapplet(docIndex,false,'getdocument');
            }
            else if(selButton == 'deleteadd')
            {
                var objCombo = docWindow.document.getElementById('wdesk:docCombo');
                var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                    if(objCombo[count].value == deletedDocIndex)
                    {
                        selectedindex = count;
                        objCombo[count].value = docIndex;
                        objCombo[count].text = docName;
                        break;
                    }
                }
                objCombo.selectedIndex = selectedindex;
                docWindow.reloadapplet(docIndex,false,'getdocument');
            }

        }
    }
}

function customAddDoc(addXML, docIndex, addModeCustom)
{
    /*  
         addXML: Output XML of "NGOAddDocument" call in case of adding new document
                 Output XML of "NGOChangeDocumentProperty" call in case of overwrite or newversion
         docIndex :   Document Index of the document to be overwritten or newversion 
         addModeCustom:   'new' , 'deleteadd' ( for overwrite)  or  newversion (for making new version)
    
    */
    //Bug 67075
    if(typeof addModeCustom != 'undefined' && (addModeCustom == 'newversion' || addModeCustom == 'deleteadd'))
        if(typeof docIndex == 'undefined' ||  docIndex == "")
            return false; 
     
    if(typeof addModeCustom == 'undefined') 
        addModeCustom = 'new';
    
    var pid=document.getElementById('wdesk:pid').value;
    var wid=document.getElementById('wdesk:wid').value;
    var taskid=document.getElementById('wdesk:taskid').value;
    var url='../ajaxCustomDoc.jsf';
    url = appendUrlSession(url);
    var requestString='pid='+pid+'&wid='+wid+'&taskid='+taskid+'&docXML='+addXML+'&addMode='+addModeCustom+'&deletedDocIndex='+docIndex;
    var docLoader = new net.ContentLoader(url,docHandler,null,"POST",requestString);
}

function docHandler()
{
    var response = this.req.responseText;
    response = eval("("+response+")");
    customOpenNewDoc(response.DocIndex, response.DocName, response.DiskName, response.selButton,response.RunScript,response.docType,response.deletedDocIndex);
}

function customOpenNewDoc(docIndex, docName, diskName, selButton,runScript,docType,deletedDocIndex)
{
    if(typeof deletedDocIndex == 'undefined') 
        deletedDocIndex = "";
    
    var docWindow = "";

    if(windowProperty.winloc == "M")
        docWindow = getWindowHandler(windowList,"tableGrid");
    else
        docWindow = getWindowHandler(window.opener.windowList,"tableGrid");
    if(DocOrgName == 'Y')
            docName = docName + "(" + diskName + ")";
    if(docWindow){
        
        var objCombo = docWindow.document.getElementById('wdesk:docCombo');
        if(runScript=='true'){
        if(selButton == 'new')
        {
        if((navigator.appName=='Netscape')?false:true || isInternetExplorer11())
        {
            var opt = docWindow.document.createElement("OPTION");
            opt.text = docName;
            opt.value = docIndex;
            objCombo.add(opt);
        }
        else
        {
            var option=new Option(docName,docIndex);
            objCombo.options.add(option,objCombo.options.length);
        }
        objCombo.selectedIndex = objCombo.options.length-1;
            docWindow.reloadapplet(docIndex,'','openNewDoc');
        }//Bug 72131  starts
      else if(selButton == 'newversion')
         {
          var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                     if(objCombo[count].value == docIndex)
                    {
                        selectedindex = count;
                        objCombo[count].text = docName;
                        break;
                    }
                }
                objCombo.selectedIndex = selectedindex;
                docWindow.reloadapplet(docIndex,'','openNewDoc');
             
             
         }
        else if(selButton == 'deleteadd')
         {
                var selectedindex = '';
                for(var count=0;count<objCombo.length;count++)
                {
                    if(objCombo[count].value == deletedDocIndex)
                    {
                        selectedindex = count;
                        objCombo[count].value = docIndex;
                        objCombo[count].text = docName;
                        break;
                    }
                }
                objCombo.selectedIndex = selectedindex;
                docWindow.reloadapplet(docIndex,'','openNewDoc');
        }
       }
     }//Bug 72131 ends
}
function openUploadDocWin(event){
    event = (typeof event == 'undefined')? null: event;
    var url = "/webdesktop/components/workitem/document/uploaddocument.jsf?rid="+MakeUniqueNumber()+"&Option="+encode_utf8("doccab/adddoc_frameset");
    var height=window3H+100;
    var win = link_popup(url,"UploadDoc",'scrollbars=yes,status=yes,width='+(window3W)+',height='+(height)+',left='+window3Y+',top='+window3X,windowList,false);
    cancelBubble(event);
}
function isAnnotationsModified() {
    if(typeof confirmAnnotationSave!=undefined && confirmAnnotationSave=="Y"){
        if(document.getElementById('wdesk:ivapp')!= null  && (document.getElementById('wdesk:ivapp').style.display=="inline" || document.getElementById('wdesk:ivapp').style.display=="block")){
            if(document.IVApplet.annotationModified()){
                var ret = confirm(ALERT_UNSAVED_ANNOTATION);
                if(ret){
                    var argv = isAnnotationsModified.arguments;
                    var argc = argv.length;
                    for (var i = 0; i < argc; i++) {
                        var abc = document.IVApplet.saveAnnotations();
                    }
                }
            }
        }
        else if(document.getElementById('wdesk:ifrm')!= null  && (document.getElementById('wdesk:ifrm').style.display=="inline"|| document.getElementById('wdesk:ifrm').style.display=="block")){
            if(isOpAll=='Y' && opall_toolkit && opall_toolkit.annotationModified()){
                
                if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "1") {
                    var ret = confirm(ALERT_UNSAVED_ANNOTATION);
                    if (ret) {
                        opall_toolkit.saveAnnotation();
                    }
                } else {
                    if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "2") {
                        opall_toolkit.saveAnnotation();
                    }
                }
            } 
        }
        else if(typeof window.opener != "undefined" && window.opener.document.getElementById('wdesk:ifrm')){
            if(isOpAll=='Y' && opall_toolkit && opall_toolkit.annotationModified()){
                
                if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "1") {
                    var ret = confirm(ALERT_UNSAVED_ANNOTATION);
                    if (ret) {
                        opall_toolkit.saveAnnotation();
                    }
                } else {
                    if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "2") {
                        opall_toolkit.saveAnnotation();
                    }
                }
            } 
        }
    }
}

function isSaveAnnotation(){
     if(typeof confirmAnnotationSave!=undefined && confirmAnnotationSave=="Y"){
         if(document.getElementById('wdesk:ivapp')!= null  && (document.getElementById('wdesk:ivapp').style.display=="inline" || document.getElementById('wdesk:ivapp').style.display=="block")){
             try{
                 if(document.IVApplet && document.IVApplet.annotationModified()){
                     var ret = confirm(ALERT_UNSAVED_ANNOTATION);
                     if(ret){
                         document.IVApplet.saveAnnotations();
                     }
                 }
             }catch(e){}
         }
        else if(document.getElementById('wdesk:ifrm')!= null  && (document.getElementById('wdesk:ifrm').style.display=="inline"|| document.getElementById('wdesk:ifrm').style.display=="block")){
            if(isOpAll=='Y' && opall_toolkit && opall_toolkit.annotationModified()){
                if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "1") {
                    var ret = confirm(ALERT_UNSAVED_ANNOTATION);
                    if (ret) {
                        opall_toolkit.saveAnnotation();
                    }
                } else {
                    if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "2") {
                        opall_toolkit.saveAnnotation();
                    }
                }
            } 
        }
        else if(typeof window.opener != "undefined" && window.opener.document.getElementById('wdesk:ifrm')){
            if(isOpAll=='Y' && opall_toolkit && opall_toolkit.annotationModified()){
                
                if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "1") {
                    var ret = confirm(ALERT_UNSAVED_ANNOTATION);
                    if (ret) {
                        opall_toolkit.saveAnnotation();
                    }
                } else {
                    if (typeof annotationAutoSave != "undefined" && annotationAutoSave == "2") {
                        opall_toolkit.saveAnnotation();
                    }
                }
            } 
        }
     }
 }

 function dataClassSearch(winlocation){
    var pid="";
    var wid="";
    var taskid="";
    var winloc="";
    if(document.getElementById('wdesk')){
     pid=document.getElementById('wdesk:pid').value;
     wid=document.getElementById('wdesk:wid').value;
     taskid=document.getElementById('wdesk:taskid').value;
     winloc='N';
    }
    else{
     pid=window.opener.document.getElementById('wdesk:pid').value;
     wid=window.opener.document.getElementById('wdesk:wid').value;
     taskid=window.opener.document.getElementById('wdesk:taskid').value;
     winloc='T';
    }
    var url ="/webdesktop/components/searchdms/dataclasssearch.jsf?winloc="+winloc+"&randNum="+MakeUniqueNumber()+"&pid="+encode_utf8(pid)+"&wid="+wid+"&taskid="+taskid+"&NewWin=Y&Option=SearchExtendSession";
    var height=window3H+100;
    var win = link_popup(url,"SrchDClass",'scrollbars=yes,status=yes,width='+(window3W)+',height='+(height)+',left='+window3Y+',top='+window3X,windowList,false);
}

function globalIndexSearch(winlocation){
    var pid="";
    var wid="";
    var taskid="";
    var winloc="";
    if(document.getElementById('wdesk')){
     pid=document.getElementById('wdesk:pid').value;
     wid=document.getElementById('wdesk:wid').value;
     taskid=document.getElementById('wdesk:taskid').value;
     winloc='N';
    }
    else{
     pid=window.opener.document.getElementById('wdesk:pid').value;
     wid=window.opener.document.getElementById('wdesk:wid').value;
     taskid=window.opener.document.getElementById('wdesk:taskid').value;
     winloc='T';
    }
    var url ="/webdesktop/components/searchdms/glbindexsearch.jsf?winloc="+winloc+"&randNum="+MakeUniqueNumber()+"&pid="+pid+"&wid="+wid+"&taskid="+taskid+"&NewWin=Y&Option=SearchExtendSession";
    var height=window3H+100;
    var win = link_popup(url,"SrchGlbld",'scrollbars=yes,status=yes,width='+(window3W)+',height='+(height)+',left='+window3Y+',top='+window3X,windowList,false);
}

function getInterface(tabName,Srcurl,typeOf,parentContainerDiv){
     var url = sContextPath + "/components/workitem/view/";

    if(navigator.appName.indexOf("Netscape") != -1 && wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"){
        if(tabName=="form" && isFormLoaded==true){
            if(!bDefaultNGForm){
            } else {
                if(typeof document.wdgc !='undefined' && document.wdgc != null){
                    ngfrmBuffer = document.wdgc.getFieldValueBagEx();
                }
            }
        }
    }    
   
    if(docLoaded == false && tabName == 'doc') {
        url += "doc_main_view.jsf";
        sendAsynAjaxRequest('doc', url);
        //addWindowToParent(windowList,'tableGrid');
    }   

    if(formLoaded == false && tabName == 'form') {
        if(wiproperty.formType == "NGFORM") {
            if(ngformproperty.type == "applet") {
                //detectJRE();
            }
        }
        
        if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm) {
            if(bAllDeviceForm){         
                if(bCustomIForm){
                    url += "customiforms_main_view.jsf?fid=Form";
                } else {
                    url += "iforms_main_view.jsf";
                }                
            } else {
                url += "ngform_main_view.jsf";
            }
        } else {
            url += "form_main_view.jsf";
        }
         if(bFetchModifiedFVB && SharingMode){
                url += "?FetchModified=Y";
                bFetchModifiedFVB = false;
         }
        if(newWDJason!=null){
            if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm) {                
                /*if((oldWDJason.RouteId == newWDJason.RouteId) && (oldWDJason.ActivityId == newWDJason.ActivityId) && (newWDJason.Processname==oldWDJason.Processname) && (newWDJason.FormIndex==oldWDJason.FormIndex)) {                                               
                    var ngformIframe = document.getElementById("ngformIframe");	
                    if(ngformIframe != null){
                        try{
                            ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.refreshForm()");	
                        }catch(e){}
                    }                                 
                } else {
                    sendAsynAjaxRequest('form', url);
                }*/
                sendAsynAjaxRequest('form', url);
            } else {
                var newNGFormProperty = eval("("+newWDJason.NGFormproperty+")");            
                if((oldWDJason.RouteId == newWDJason.RouteId) && (oldWDJason.ActivityId == newWDJason.ActivityId) && (newWDJason.Processname==oldWDJason.Processname) && (newWDJason.FormIndex==oldWDJason.FormIndex) && (newNGFormProperty.type=="applet" && oldWDJason.NGFormproperty.type=="applet")) {                               
                   //document.wdgc.initializeForm();
                   //document.wdgc.setWFGeneralData(newWDJason.GeneralData);               
                   //bFetchModifiedFVB = true;//check
                   var wiData = fetchFieldValueBag();               
                   //document.wdgc.setFieldValueBag(wiData);
                   document.wdgc.refreshForm(newWDJason.GeneralData, wiData)
                   formLoaded = true;
                   addWindowToParent(windowList,'formGrid');
                   document.getElementById('formDiv').style.display = "inline";
                   if(isEmbd == 'Y') {
                       expLoaded = false;
                   }
                } else {
                    sendAsynAjaxRequest('form', url);
                }
            }
        }

        if(newWDJason==null) {
            sendAsynAjaxRequest('form', url);
        }
    }

    if(expLoaded == false && tabName == 'exp') {
        url += "exp_main_view.jsf";
        sendAsynAjaxRequest('exp', url);
    }

    if(todoLoaded == false && tabName == 'todo') {
        url += "todo_main_view.jsf";
        sendAsynAjaxRequest('todo', url);
    }

    if(taskLoaded == false && tabName == 'task') {
        if((wiproperty.TaskFormType == "NGFORM") && (taskngformproperty.type == "applet") && !bDefaultNGForm) {
            url += "taskngform_main_view.jsf"; // NG Form
        } else {
            url += "task_main_view.jsf"; // HTML Form
        }
        sendAsynAjaxRequest('task', url);
    }

    if(sap0Loaded == false && tabName == 'SAP0') {        
        url += "sap_main_view.jsf?"+encode_utf8('pid')+"="+pid+"&"+encode_utf8("wid")+"="+wid+"&"+encode_utf8("taskid")+"="+taskid+"&"+"sapDefId="+sapDefName0;
        sendAsynAjaxRequest('SAP0', url);
    }

    if(sap1Loaded == false && tabName == 'SAP1') {
        url += "sap_main_view.jsf?"+encode_utf8('pid')+"="+pid+"&"+encode_utf8("wid")+"="+wid+"&"+encode_utf8("taskid")+"="+taskid+"&"+"sapDefId="+sapDefName1;
        sendAsynAjaxRequest('SAP1', url);
    }

    if(sap2Loaded == false && tabName == 'SAP2') {
        url += "sap_main_view.jsf?"+encode_utf8('pid')+"="+pid+"&"+encode_utf8("wid")+"="+wid+"&"+encode_utf8("taskid")+"="+taskid+"&"+"sapDefId="+sapDefName2;
        sendAsynAjaxRequest('SAP2', url);
    }

    if(sap3Loaded == false && tabName == 'SAP3') {
        url += "sap_main_view.jsf?"+encode_utf8('pid')+"="+pid+"&"+encode_utf8("wid")+"="+wid+"&"+encode_utf8("taskid")+"="+taskid+"&"+"sapDefId="+sapDefName3;
        sendAsynAjaxRequest('SAP3', url);
    }
    //Bug 64845
    if(tabName == 'SAP') {
        if(typeOf!=undefined && typeOf!="" && typeOf!=null){
            url += "sap_main_view.jsf?"+encode_utf8('pid')+"="+pid+"&"+encode_utf8("wid")+"="+wid+"&"+encode_utf8("taskid")+"="+taskid+"&"+"sapDefId="+typeOf;
            sendAsynAjaxRequest('SAP', url);
        }
    }
    
    if((typeOf!=undefined || typeOf!="") && typeOf=='Ext'){
        var parentDiv=window.document.getElementById(tabName+'Div');
        var interfaceDivObj=window.document.getElementById(getInterfaceKeyByType(tabName));
        
        if(wDeskLayout.WDeskType == 'F'){
            if(typeof parentContainerDiv != 'undefined'){
                interfaceDivObj = parentContainerDiv;
            }
        }
        
        var qString = "";
        if(Srcurl.indexOf("?")<0){
            qString = "?";
        } else {
            qString = "&";
        }
        
        qString += "pid="+encodeURIComponent(pid)+"&wid="+wid+"&taskid="+taskid+"&UserIndex="+userIndex+"&UserId="+userName+"&WD_UID="+WD_UID+"&SessionId="+sessionId;
        Srcurl += qString;
        
        var frame=createIFrameExt(Srcurl, tabName, interfaceDivObj,'Ext');
        parentDiv.appendChild(frame);
        parentDiv.style.display='block';
    }


}

function initNLoadCustomInterfaces(){
    var intDiv = document.getElementById('formDiv');
    if(intDiv != null && intDiv != undefined) {
        getInterface('form');
    }

    intDiv = document.getElementById('docDiv');
    if(intDiv != null && intDiv != undefined) {
        getInterface('doc');
    }

    intDiv = document.getElementById('expDiv');
    if(intDiv != null && intDiv != undefined) {
        getInterface('exp');
    }

    intDiv = document.getElementById('todoDiv');
    if(intDiv != null && intDiv != undefined) {
        getInterface('todo');
    }

    intDiv = document.getElementById('taskDiv');
    if(intDiv != null && intDiv != undefined) {
        getInterface('task');
    }

    intDiv = document.getElementById('SAP0Div');
    if(intDiv != null && intDiv != undefined) {
        getInterface('SAP0');
    }

    intDiv = document.getElementById('SAP1Div');
    if(intDiv != null && intDiv != undefined) {
        getInterface('SAP1');
    }

    intDiv = document.getElementById('SAP2Div');
    if(intDiv != null && intDiv != undefined) {
        getInterface('SAP2');
    }

    intDiv = document.getElementById('SAP3Div');
    if(intDiv != null && intDiv != undefined) {
        getInterface('SAP3');
    }
    
    intDiv = document.getElementById('SAPDiv');
    if(intDiv != null && intDiv != undefined) {
        getInterface('SAP');
    }
}

function initNLoadInterfaces() {    
    var leftContainerRef = document.getElementById('wdesk:lefttddiv');
    var rightContainerRef = document.getElementById('wdesk:righttd');
    var intDiv;
    
    if(interFace0 != '') {
        intDiv = document.getElementById(interFace0+'Div');
        if(intDiv == null && intDiv == undefined) {
            intDiv = window.document.createElement('Div');
            intDiv.id = interFace0+'Div';
            leftContainerRef.appendChild(intDiv);
        } 
        intDiv.style.display = 'inline';
        getInterface(interFace0);
    }    

    if(interFace1 != '') {
        intDiv = document.getElementById(interFace1+'Div');
        if(intDiv == null && intDiv == undefined) {
            intDiv = window.document.createElement('Div');
            intDiv.id = interFace1+'Div';
            rightContainerRef.appendChild(intDiv);
        }  
        intDiv.style.display = 'inline';
        getInterface(interFace1);
    }

    if(interFace2 != '') {
        intDiv = document.getElementById(interFace2+'Div');
        if(intDiv == null && intDiv == undefined) {
            intDiv = window.document.createElement('Div');
            intDiv.id = interFace2+'Div';
            leftContainerRef.appendChild(intDiv);
        }
        intDiv.style.display = 'none';        
        //getInterface(interFace2);
    }

    if(interFace3 != '') {
        intDiv = document.getElementById(interFace3+'Div');
        if(intDiv == null && intDiv == undefined) {
            intDiv = window.document.createElement('Div');
            intDiv.id = interFace3+'Div';
            rightContainerRef.appendChild(intDiv);
        }  
        intDiv.style.display = 'none';        
        //getInterface(interFace3);
    }

    if(interFace0=='' && interFace1=='' && interFace2=='' && interFace3==''){
       // setmessageinDiv(NO_DATA_FOUND, "false", 3000);
    }
}

function sendAsynAjaxRequest(fromTab, toFile ,callBackFunction, addWin , changeVisibility, eventType,docIndex){
    
    var bAddWin = true;
    var bChangeVisibility = true;

    if(addWin != undefined && !addWin)
    bAddWin = false;

    if(changeVisibility != undefined && !changeVisibility)
    bChangeVisibility = false;

    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;
    var docWindow= getWindowHandler(winList,"tableGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var todoWindow = getWindowHandler(winList,"todoGrid");
    var taskWindow = getWindowHandler(winList,"taskGrid");

    var isDefaultDoc = "";
    //Bug 59916
    var isDefaultDocType = "";
    if(document.getElementById('IsDefaultDocType') != null){
    isDefaultDocType = document.getElementById('IsDefaultDocType').value;
    }
    //Bug 59916
    var vAction = "";
    if(fromTab == 'doc'){
        vAction = "doc";
        isDefaultDoc=document.getElementById('IsDefaultDoc').value;
    }
    if(fromTab == 'form'){
        vAction = "form";        
    }
    if(fromTab == 'todo'){
        vAction = "todo";        
    }
    if(fromTab == 'task'){
        vAction = "task";        
    }
    if(fromTab == 'exp'){
        vAction = "exp";  
      if(typeof calledFrom!='undefined' && document.getElementById('wdesk:calledFrom') != null){ //Bug 68117
            calledFrom=document.getElementById('wdesk:calledFrom').value;
      }
    }
    if(taskid == undefined){
        taskid=''
    }
     if(typeof calledFrom == 'undefined'){	//Bug 68117
        calledFrom=''
    }
    var wdViewMode = document.getElementById('wdView').value;
    var url = toFile;
    var requestString = encode_utf8("pid")+"="+encodeURIComponent(pid)+"&"+encode_utf8("wid")+"="+encode_utf8(wid)+"&"+encode_utf8("taskid")+"="+encode_utf8(taskid)+"&height="+wiHeight+"&Action="+vAction+"&IsDefaultDoc="+isDefaultDoc+"&wdViewmode="+wdViewMode+"&WD_UID="+WD_UID+"&IsDefaultDocType="+isDefaultDocType;//Bug 59916
    requestString=requestString+"&calledFrom="+calledFrom;	//Bug 68117
    var contentLoaderRef = new net.ContentLoader(url, interfaceLoadHandler, callBackOnError, "POST",requestString, true);
    
    if(callBackFunction != undefined && callBackFunction.length>0) {
        try {
               RemoveIndicator("handlerCall");
        }
        catch(e){
        }
    }
    
    function interfaceLoadHandler(){

        var isCustomWdesk = false;

        if(typeof oldWDJason != 'undefined')
        {        
            isCustomWdesk = oldWDJason.IsCustomWdesk;
            if(newWDJason != null){
                isCustomWdesk = newWDJason.IsCustomWdesk;
            }
        }

        var respText = contentLoaderRef.req.responseText;        
        if(fromTab == 'doc'){
            respText = respText.substring(respText.indexOf('<doccontainer>')+14,respText.lastIndexOf('</doccontainer>'));
            
            document.getElementById('docDiv').innerHTML = respText;

            var doccheckoutcount = document.getElementById('wdesk:doccheckoutcount').value;
            window_workdesk.docCheckOut = parseInt(doccheckoutcount);

            if(bAddWin)
            addWindowToParent(windowList,'tableGrid');
            docLoaded = true;
            
            if(!isCustomWdesk && bChangeVisibility){
                if(navigator.appName=='Netscape'){
                    document.getElementById('docDiv').style.display = "block";
                } else {
                    document.getElementById('docDiv').style.display = "inline";
                }
            }
            docIndex = (typeof docIndex == 'undefined')? '': docIndex;
            InitHWDoc(eventType,docIndex);           
            var isDefaultDoc = document.getElementById('IsDefaultDoc').value;
            if(document.getElementById('wdesk:docCombo')){        
                if(document.getElementById('wdesk:docCombo').value==-1){
                     if(isDefaultDoc == "N"){
                        if(document.getElementById('docOptionsDiv') != null){
                            if(navigator.appName=='Netscape'){
                                document.getElementById('docOptionsDiv').style.display = "block";
                            } else {
                                document.getElementById('docOptionsDiv').style.display = "inline";
                            }
                        }
                    }
                    var docList = document.getElementById('wdesk:docOperation') 
                    if(docList != null){
                        docList.style.display = "none";
                    }
                }
             }
            
            adjustEmbMenu(isEmbd);
            
            setDefaultDocTypeColor();
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }                        
        }else if (fromTab == 'form'){
            if(wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"){
                if(isEmbd != 'Y'){
                    if(wDeskLayout.WDeskType == 'E'){
                        //show form broadcast link
                        showHideFormLink();
                    } else {
                        // show interface tab menu
                        var selectedInterfaceTab = getSelectedInterfaceTab(); 
                        hideShowInterfaceTabMenu(selectedInterfaceTab.CollabSelectedIntTab.Left, selectedInterfaceTab.CollabSelectedIntTab.Left);
                        hideShowInterfaceTabMenu(selectedInterfaceTab.CollabSelectedIntTab.Right, selectedInterfaceTab.CollabSelectedIntTab.Right); 
                    }
                } else {
                    showHideFormLink();
                }
            } else
                isFormLoaded=true;
            ngattribute = respText.substring(respText.indexOf('<ngAttributeCont>')+17,respText.lastIndexOf('</ngAttributeCont>'));
            ngattribute=decode_utf8(ngattribute);
            respText = respText.substring(respText.indexOf('<formcontainer>')+15,respText.lastIndexOf('</formcontainer>'));
            
                document.getElementById('formDiv').innerHTML = respText;
            
            
            if(bAddWin)
            addWindowToParent(windowList,'formGrid');
            formLoaded = true;

            if(!isCustomWdesk && bChangeVisibility){
                if((isEmbd == 'Y') && (wdView == "em")){
                    document.getElementById('formDiv').style.display = "inline";
                } else {
                    var isChrome = window.chrome;
                    if(typeof isChrome != 'undefined') {
                        //document.getElementById('formDiv').style.display = "inline";
                        // Bug 45913 - If HTML form is lengthy, scroll bar is not getting enabled in workitem window
                        document.getElementById('formDiv').style.display = "block"; 
                    }else{
                        document.getElementById('formDiv').style.display = "block";
                    }
                }
            }
            
            if((isEmbd == 'Y') && (wdView == "em")){
                initHWForm(heightH,compWidth);
            } else {
                initHWFormNew();
            }
            
            //setTimeout("settingsFn()",0);  //puneet tbd
            if(funcVariantOnload != undefined && funcVariantOnload.length>0) {//added for Variant : 22 Oct 2013
                var funcCallVariantOnload = funcVariantOnload+"()";
                eval(funcCallVariantOnload);
            }//till here for Variant
            
            adjustEmbMenu(isEmbd);
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }
        else if (fromTab == 'exp'){
            respText = respText.substring(respText.indexOf('<expcontainer>')+14,respText.lastIndexOf('</expcontainer>'));            
            document.getElementById('expDiv').innerHTML = respText;
            

            if(bAddWin)
            addWindowToParent(windowList,'exceptionGrid');
            expLoaded = true;
            InitHWExp();

            if(!isCustomWdesk && bChangeVisibility){
                document.getElementById('expDiv').style.display = "inline";
            } 
            
            if(isEmbd == 'Y'){
               /* var FormEle = $("#containerDiv").find(".mCSB_container")[0];
                if(FormEle){            
                    FormEle.style.width = "auto";
                } */   
            }
            
            adjustEmbMenu(isEmbd);
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }
        else if (fromTab == 'todo'){
            respText = respText.substring(respText.indexOf('<todocontainer>')+15,respText.lastIndexOf('</todocontainer>'));            
            document.getElementById('todoDiv').innerHTML = respText;
            

            if(bAddWin)
            addWindowToParent(windowList,'todoGrid');
            todoLoaded = true;
            
            if((isEmbd == "N") || (wdView != "em")){
                InitHWTodoNew();
            }

            if(!isCustomWdesk && bChangeVisibility){
                if((isEmbd == 'Y') && (wdView == "em")){
                    document.getElementById('todoDiv').style.display = "inline";
                } else {
                    document.getElementById('todoDiv').style.display = "block";
                }
            }
            
            if(isEmbd == 'Y'){
               /* var FormEle = $("#containerDiv").find(".mCSB_container")[0];
                if(FormEle){            
                    FormEle.style.width = "auto";
                } */   
            }
            
            adjustEmbMenu(isEmbd);
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }
        else if (fromTab == 'task'){
            if(wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"){
//                if(isEmbd != 'Y'){
//                    if(wDeskLayout.WDeskType == 'E'){
//                        //show form broadcast link
//                        showHideFormLink();
//                    } else {
//                        // show interface tab menu
//                        var selectedInterfaceTab = getSelectedInterfaceTab(); 
//                        hideShowInterfaceTabMenu(selectedInterfaceTab.CollabSelectedIntTab.Left, selectedInterfaceTab.CollabSelectedIntTab.Left);
//                        hideShowInterfaceTabMenu(selectedInterfaceTab.CollabSelectedIntTab.Right, selectedInterfaceTab.CollabSelectedIntTab.Right); 
//                    }
//                } else {
//                    showHideFormLink();
//                }
            } else
                isFormLoaded=true;
            taskngattribute = respText.substring(respText.indexOf('<taskngAttributeCont>')+21,respText.lastIndexOf('</taskngAttributeCont>'));
            taskngattribute=decode_utf8(taskngattribute);
            respText = respText.substring(respText.indexOf('<taskcontainer>')+15,respText.lastIndexOf('</taskcontainer>'));            
            document.getElementById('taskDiv').innerHTML = respText;
            if(bAddWin)
                addWindowToParent(windowList,'taskGrid');
            taskLoaded = true;
            
            if((isEmbd == "N") || (wdView != "em")){
                InitHWTaskNew();
            }

            if(!isCustomWdesk && bChangeVisibility){
                if((isEmbd == 'Y') && (wdView == "em")){
                    document.getElementById('taskDiv').style.display = "inline";
                } else {
                    document.getElementById('taskDiv').style.display = "block";
                }
            }
            
            if(isEmbd == 'Y'){
                /*var FormEle = $("#containerDiv").find(".mCSB_container")[0];
                if(FormEle){            
                    FormEle.style.width = "auto";
                } */   
            }
            
            adjustEmbMenu(isEmbd);
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }
        else if (fromTab == 'SAP0'){            
            document.getElementById('SAP0Div').innerHTML = respText;       
                
            var parentElement = document.getElementById("offsetparent").parentElement;
            parentElement = document.getElementById(parentElement.id).parentElement;     
            var sapDefId = document.getElementById("sapDefId").value;
            document.getElementById('sapviewer'+sapDefId).height =  document.getElementById(parentElement.id).style.height;        
            document.getElementById('sapviewer'+sapDefId).height = parseInt(document.getElementById('sapviewer'+sapDefId).height) - 70 +"px";
                
            if(bAddWin)
            addWindowToParent(windowList,'sap0Grid');
            sap0Loaded = true;

            if(!isCustomWdesk && bChangeVisibility){
                document.getElementById('SAP0Div').style.display = "inline";
            }
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }
        else if (fromTab == 'SAP1'){            
            document.getElementById('SAP1Div').innerHTML = respText;
            
            var parentElement = document.getElementById("offsetparent").parentElement;
            parentElement = document.getElementById(parentElement.id).parentElement;     
            var sapDefId = document.getElementById("sapDefId").value;
            document.getElementById('sapviewer'+sapDefId).height =  document.getElementById(parentElement.id).style.height;        
            document.getElementById('sapviewer'+sapDefId).height = parseInt(document.getElementById('sapviewer'+sapDefId).height) - 70 +"px";
            
            if(bAddWin)
            addWindowToParent(windowList,'sap1Grid');
            sap1Loaded = true;

            if(!isCustomWdesk && bChangeVisibility){
                document.getElementById('SAP1Div').style.display = "inline";
            }
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }
        else if (fromTab == 'SAP2'){            
            document.getElementById('SAP2Div').innerHTML = respText;
            
            var parentElement = document.getElementById("offsetparent").parentElement;
            parentElement = document.getElementById(parentElement.id).parentElement;     
            var sapDefId = document.getElementById("sapDefId").value;
            document.getElementById('sapviewer'+sapDefId).height =  document.getElementById(parentElement.id).style.height;        
            document.getElementById('sapviewer'+sapDefId).height = parseInt(document.getElementById('sapviewer'+sapDefId).height) - 70 +"px";

            if(bAddWin)
            addWindowToParent(windowList,'sap2Grid');
            sap2Loaded = true;

            if(!isCustomWdesk && bChangeVisibility){
                document.getElementById('SAP2Div').style.display = "inline";
            }
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }
        else if (fromTab == 'SAP3'){            
            document.getElementById('SAP3Div').innerHTML = respText;
            
            var parentElement = document.getElementById("offsetparent").parentElement;
            parentElement = document.getElementById(parentElement.id).parentElement;     
            var sapDefId = document.getElementById("sapDefId").value;
            document.getElementById('sapviewer'+sapDefId).height =  document.getElementById(parentElement.id).style.height;        
            document.getElementById('sapviewer'+sapDefId).height = parseInt(document.getElementById('sapviewer'+sapDefId).height) - 70 +"px";

            if(bAddWin)
            addWindowToParent(windowList,'sap3Grid');
            sap3Loaded = true;

            if(!isCustomWdesk && bChangeVisibility){
                document.getElementById('SAP3Div').style.display = "inline";
            }
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        } 
        //Bug 64845
         else if (fromTab == 'SAP'){            
            document.getElementById('SAPDiv').innerHTML = respText;
            
            var parentElement = document.getElementById("offsetparent").parentElement;
            parentElement = document.getElementById(parentElement.id).parentElement;     
            var sapDefId = document.getElementById("sapDefId").value;
            document.getElementById('sapviewer'+sapDefId).height =  document.getElementById(parentElement.id).style.height;        
            document.getElementById('sapviewer'+sapDefId).height = parseInt(document.getElementById('sapviewer'+sapDefId).height) - 70 +"px";

            if(bAddWin)
            addWindowToParent(windowList,'sapGrid');
            sapLoaded = true;

            if(!isCustomWdesk && bChangeVisibility){
                document.getElementById('SAPDiv').style.display = "inline";
            }
            
            //Post Hook
            if(typeof interfacePostHook != 'undefined') {
                interfacePostHook(fromTab);
            }  
        }    

        if(callBackFunction != undefined && callBackFunction.length>0) {
            try {
                eval(callBackFunction);
            }
            catch(e){
            }
        }


    }
    function callBackOnError(){
        if(this.req.status == 250){
            window.location= "/webdesktop/error/errorpage.jsf?msgID=-8002&HeadingID=8002";
        } else if(this.req.status == 350){
            window.parent.parent.initPopUp();
            window.parent.parent.setPopupMask();
            window.parent.parent.createPopUpIFrame('genericErrorIframe','/omniapp/pages/error/genericerror.jsf?msgID=4021&HeadingID=ERROR&Message='+INVALID_REQUEST_IS_MADE,140,435);
            var screenHeight = window.screen.height;
            window.parent.parent.document.getElementById('genericErrorIframe').style.top = screenHeight/6+"px";
            window.parent.parent.document.getElementById('genericErrorIframe').style.zIndex = 300;
        }
       
    }
}


function clearInterfacesDiv(){
    var leftContainerRef = document.getElementById('wdesk:lefttddiv');
    var rightContainerRef = document.getElementById('wdesk:righttd');

    var tempDiv = null;
    var i;
    // Clears interfaces div in left container
    if(leftContainerRef != null) {
        for(i=0; i<leftContainerRef.childNodes.length; i++){
            tempDiv = leftContainerRef.childNodes[i];            
            //tempDiv.innerHTML = "";
            tempDiv.style.display = "none";
        }
    }

    // Clears interfaces div in right container
    if(rightContainerRef != null) {
        for(i=0; i<rightContainerRef.childNodes.length; i++){
            tempDiv = rightContainerRef.childNodes[i];            
            //    tempDiv.innerHTML = "";
            tempDiv.style.display = "none";
        }
    }    
}

function getInterfaceDir(interfaceName){
    var leftContainerRef = document.getElementById('wdesk:lefttddiv');
    var rightContainerRef = document.getElementById('wdesk:righttd');    

    var tempDiv = null;
    var i;
    // find interface div in left container
    if(leftContainerRef != null) {
        for(i=0; i<leftContainerRef.childNodes.length; i++){
            tempDiv = leftContainerRef.childNodes[i];
            if(tempDiv.id == interfaceName+"Div"){
                return {"Dir" : "left", "ElementRef" : tempDiv};
            }
        }
    }

    // find interface div in right container
    if(rightContainerRef != null) {
        for(i=0; i<rightContainerRef.childNodes.length; i++){
            tempDiv = rightContainerRef.childNodes[i];
            if(tempDiv.id == interfaceName+"Div"){
                return {"Dir" : "right", "ElementRef" : tempDiv};
            }
        }
    }
    return null;
}

function adjustInterfacesDiv(wDJason){
    clearInterfacesDiv();
    
    var leftContainerRef = document.getElementById('wdesk:lefttddiv');
    var rightContainerRef = document.getElementById('wdesk:righttd');
    var interfaceJason = null;
    var interfaceName = "";

    for(var i=0; i<4; i++){
        interfaceName = wDJason.Interfaces["Interface"+i];
        interfaceJason = getInterfaceDir(interfaceName);
        if(interfaceJason != null){
            if(i%2 == 0){
                if(interfaceJason.Dir == "right"){
                    rightContainerRef.removeChild(interfaceJason.ElementRef);
                    leftContainerRef.appendChild(interfaceJason.ElementRef);
                }
            } else {
                if(interfaceJason.Dir == "left"){
                    leftContainerRef.removeChild(interfaceJason.ElementRef);
                    rightContainerRef.appendChild(interfaceJason.ElementRef);
                }
            }
        } else {
            // if interface div does not exist in either container.
            if(interfaceName != ""){
                var intDiv = window.document.createElement('Div');
                intDiv.id = 'wdesk:'+interfaceName+'Div';
                
                if(i==0 || i==1) {
                    intDiv.style.display = 'inline';
                } else {
                    intDiv.style.display = 'none';
                }

                if(i%2 == 0){
                    leftContainerRef.appendChild(intDiv);
                } else {
                    rightContainerRef.appendChild(intDiv);
                }
            }
        }
    }

    removeSpareIntDiv(wDJason.Interfaces);
}

function removeSpareIntDiv(interfaceJason){
    var leftContainerRef = document.getElementById('wdesk:lefttddiv');
    var rightContainerRef = document.getElementById('wdesk:righttd');

    var tempDiv = null;
    var i;
    // removes spare div(s) from left container
    if(leftContainerRef != null) {
        for(i=0; i<leftContainerRef.childNodes.length; i++){
            tempDiv = leftContainerRef.childNodes[i];
            if(!isIntDivExists(tempDiv.id, interfaceJason)){
                leftContainerRef.removeChild(tempDiv);
            }
        }
    }

    // removes spare div(s) from right container
    if(rightContainerRef != null) {
        for(i=0; i<rightContainerRef.childNodes.length; i++){
            tempDiv = rightContainerRef.childNodes[i];
            if(!isIntDivExists(tempDiv.id, interfaceJason)){
                rightContainerRef.removeChild(tempDiv);
            }
        }
    }
}

function isIntDivExists(divId, interfaceJason){
    for(var i=0; i<4; i++){        
        if(divId == interfaceJason["Interface"+i]+"Div"){
            return true;
        }
    }
    return false;
}

function reinitFormElement(wdJason){
    // reset window title
    if(document.title != undefined){
        
         var WDeskWinTitle = stractivityName+" : "+pid;
            var WDeskWinTitleCustom = ''; 
            if(typeof wordeskWindowTitle != "undefined") {
                    WDeskWinTitleCustom = wordeskWindowTitle(strprocessname, stractivityName, strQueueName, WDeskWinTitle);
            }
        
        if(wdJason.TaskName == null || wdJason.TaskName == '')
        {
            if(WDeskWinTitleCustom != '')
             document.title = WDeskWinTitleCustom;
            else
             document.title=WDeskWinTitle;
        }
        else
            document.title = wdJason.TaskName+" : "+pid;
        if(wdJason.WIViewMode=="R"){
            document.title += " "+TITLE_READONLY;
        }
    }
    // reset title
    if(document.getElementById("wdesk:title")){
        if(wdJason.TaskName == null || wdJason.TaskName == '')
        {
            if(WDeskWinTitleCustom != '')
             document.getElementById("wdesk:title").innerHTML = WDeskWinTitleCustom;
            else
             document.getElementById("wdesk:title").innerHTML=WDeskWinTitle;
        }
        else
            document.getElementById("wdesk:title").innerHTML = wdJason.TaskName+" : "+pid;
        if(wdJason.WIViewMode=="R"){
            document.getElementById("wdesk:title").innerHTML += " "+TITLE_READONLY;
        }
        if(isEmbd == "N"){
            document.getElementById("wdesk:title").style.display = "block";
            document.getElementById("wdesk:title").style.visibility = "visible";
        }
    }

    document.forms["wdesk"]["wdesk:pid"].value = pid;
    if(document.forms["wdesk"]["ProcessInstanceID"]){
        document.forms["wdesk"]["ProcessInstanceID"].value = pid;
    }
    
    document.forms["wdesk"]["wdesk:wid"].value = wid;
    if(document.forms["wdesk"]["WorkitemID"]){
        document.forms["wdesk"]["WorkitemID"].value = wid;
    }

    document.forms["wdesk"]["wdesk:taskid"].value = taskid;
    if(document.forms["wdesk"]["taskid"]){
        document.forms["wdesk"]["taskid"].value = taskid;
    }

    document.forms["wdesk"]["wdesk:batchflag"].value = batchFlag;
    document.forms["wdesk"]["Left"].value = strLeft;
    
    document.forms["wdesk"]["wdesk:wi_count"].value = wdJason.WICount;
    if(document.forms["wdesk"]["wi_count"]){
        document.forms["wdesk"]["wi_count"].value = wdJason.WICount;
    }
}

function removeChildElements(parentElement){
    if(parentElement !=null && parentElement != undefined) {
        while (parentElement.hasChildNodes()) {
            parentElement.removeChild(parentElement.firstChild);
        }
    }
}

function initTabsNContainers(oldIntJason, newIntJason){
    var element = null;
    var element2 = null;
    var tempJason = oldIntJason;

    if(newIntJason != null && newIntJason != undefined){
        tempJason = newIntJason;
    }

    var intContainer = document.getElementById("wdesk:lefttddiv");
    var sepDiv = document.getElementById("wdesk:sepDiv");
    var sepratorMovePGroup = document.getElementById("wdesk:sepratorMovePGroup");
    var tabPGrid = document.getElementById("wdesk:leftTabPGrid");
    var tabIntPGrid = document.getElementById("wdesk:leftTabInt0PGrid");
    var tabIntPGroup = document.getElementById("wdesk:leftTabInt0PGroup");

    var tabIntPGrid2 = document.getElementById("wdesk:leftTabInt2PGrid");
    var tabIntPGroup1 = document.getElementById("wdesk:leftTabInt2PGroup1");
    var tabIntPGroup2 = document.getElementById("wdesk:leftTabInt2PGroup2");
    var tabIntPGroup3 = document.getElementById("wdesk:leftTabInt2PGroup3");

    tabIntPGrid.style.display = "none";
    tabIntPGrid2.style.display = "none";

    if(tempJason.Interface0 != "") {
        
        removeChildElements(tabIntPGroup);
        removeChildElements(tabIntPGroup1);
        removeChildElements(tabIntPGroup2);
        removeChildElements(tabIntPGroup3);

        // Initializing left tab container
        if(tempJason.Interface0 != "") {
            // interface0 is enabled initially.
            element = window.document.createElement('A');
            element.id = 'wdesk:en'+tempJason.Interface0;
            element.href = "#";

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface0!="SAP0"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface0+"_en_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_en_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup.appendChild(element);
            tabIntPGrid.style.display = 'inline';
        }

        if(tempJason.Interface2 != "") {
            // disable tab for interface0
            element = window.document.createElement('A');
            element.id = 'wdesk:dis'+tempJason.Interface0;
            element.href = "#";
            element.style.display = "none";
            element.onclick = Function("toggleIntFace('"+tempJason.Interface0+"','"+tempJason.Interface2+"','left')");

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface0 !="SAP0"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface0+"_dis_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_dis_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup1.appendChild(element);

            // enable tab for interface2
            element = window.document.createElement('A');
            element.id = 'wdesk:en'+tempJason.Interface2;
            element.href = "#";
            element.style.display = "none";

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface2 !="SAP2"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface2+"_en_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_en_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup2.appendChild(element);

            // disable tab for interface2
            element = window.document.createElement('A');
            element.id = 'wdesk:dis'+tempJason.Interface2;
            element.href = "#";
            element.style.display = "inline";
            element.onclick = Function("toggleIntFace('"+tempJason.Interface2+"','"+tempJason.Interface0+"','left')");

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface2 != "SAP2"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface2+"_dis_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_dis_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup3.appendChild(element);
            tabIntPGrid2.style.display = 'inline';
        }
        //tabPGrid.style.display = "inline";
        //intContainer.style.display = "inline";
    } else {
        //tabPGrid.style.display = "none";
        //intContainer.style.display = "none";
    }

    intContainer = document.getElementById("wdesk:righttd");
    tabPGrid = document.getElementById("wdesk:rightTabPGrid");
    tabIntPGrid = document.getElementById("wdesk:rightTabInt1PGrid");
    tabIntPGrid2 = document.getElementById("wdesk:rightTabInt3PGrid");
    tabIntPGrid.style.display = "none";
    tabIntPGrid2.style.display = "none";
    if(tempJason.Interface1 != "") {        
        tabIntPGroup = document.getElementById("wdesk:rightTabInt1PGroup");
        tabIntPGroup1 = document.getElementById("wdesk:rightTabInt3PGroup1");
        tabIntPGroup2 = document.getElementById("wdesk:rightTabInt3PGroup2");
        tabIntPGroup3 = document.getElementById("wdesk:rightTabInt3PGroup3");
        
        removeChildElements(tabIntPGroup);
        removeChildElements(tabIntPGroup1);
        removeChildElements(tabIntPGroup2);
        removeChildElements(tabIntPGroup3);

        // Initializing right tab container
        if(tempJason.Interface1 != "") {
            // interface1 is enabled initially.
            element = window.document.createElement('A');
            element.id = 'wdesk:en'+tempJason.Interface1;
            element.href = "#";

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface1 != "SAP1"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface1+"_en_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_en_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup.appendChild(element);
            tabIntPGrid.style.display = 'inline';
        }

        if(tempJason.Interface3 != "") {
            // disable tab for interface1
            element = window.document.createElement('A');
            element.id = 'wdesk:dis'+tempJason.Interface1;
            element.href = "#";
            element.style.display = "none";
            element.onclick = Function("toggleIntFace('"+tempJason.Interface1+"','"+tempJason.Interface3+"','right')");

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface1 != "SAP1"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface1+"_dis_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_dis_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup1.appendChild(element);

            // enable tab for interface3
            element = window.document.createElement('A');
            element.id = 'wdesk:en'+tempJason.Interface3;
            element.href = "#";
            element.style.display = "none";

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface3 !="SAP3"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface3+"_en_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_en_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup2.appendChild(element);

            // disable tab for interface3
            element = window.document.createElement('A');
            element.id = 'wdesk:dis'+tempJason.Interface3;
            element.href = "#";
            element.style.display = "inline";
            element.onclick = Function("toggleIntFace('"+tempJason.Interface3+"','"+tempJason.Interface1+"','right')");

            element2 = window.document.createElement('IMG');
            element2.className = "borderwidth0";
            if(tempJason.Interface3 != "SAP3"){
                element2.src = sContextPath+"/resources/"+ln+"images/tab_"+tempJason.Interface3+"_dis_"+sm+".gif";
            }else{
                element2.src = sContextPath+"/resources/"+ln+"images/tab_SAP_dis_"+sm+".gif";
            }
            element.appendChild(element2);
            tabIntPGroup3.appendChild(element);
            tabIntPGrid2.style.display = 'inline';
        }        
        sepratorMovePGroup.style.display = "inline";
        sepDiv.style.display = "inline";
        //tabPGrid.style.display = "inline";
        //intContainer.style.display = "inline";
    } else {
        //tabPGrid.style.display = "none";        
        sepDiv.style.display = "none";
        sepratorMovePGroup.style.display = "none";
        //intContainer.style.display = "none";
    }
}

function workdeskOperations(option)
{
    if(option=='S')
    {
        if(wiproperty.locked != 'Y')
                    if(windowProperty.winloc=='M') mainSave();
                        else (windowProperty.winloc=='N')?window.opener.mainSave():window.opener.opener.mainSave();
    }
   if(option=='I')
    {
         if(wiproperty.locked != 'Y')
                    {
                        if(wiproperty.operationOption== 'done')
                            if(windowProperty.winloc=='M')
                                done();
                            else (windowProperty.winloc=='N')?window.opener.done():window.opener.opener.done();
                        else if(wiproperty.operationOption== 'introduction')
                            if(windowProperty.winloc=='M') introduceWI(); else (windowProperty.winloc==N)?window.opener.introduceWI():window.opener.opener.introduceWI();
                       else if(wiproperty.operationOption == 'audit')
                           if(windowProperty.winloc=='M') introduceWI(); else (windowProperty.winloc==N)?window.opener.introduceWI():window.opener.opener.introduceWI();
                    }
    }
}

function microsoftKeyPress() {
    if(navigator.appName.indexOf("Microsoft") != -1){        
     if (window.event.ctrlKey) {
        if (window.event.keyCode == 13)
	      ToggleFocus("D");
      }
    }
}

function checkIsFormLoaded() {    
    if(wiproperty.formType=="NGFORM" && ngformproperty.type=="applet" && (interFace0 =='form'|| interFace1 =='form')) {
        // isFormLoaded=false;
    } else {
        isFormLoaded =true;
    }
}

function openDocList(event,showDocListInReadMode){
    event = (typeof event == 'undefined')? null: event;
    showDocListInReadMode=(typeof showDocListInReadMode == 'undefined')? false: showDocListInReadMode;
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    var winloc="";
    if(typeof obj == 'undefined')
    {
         if(window.opener.isFormLoaded==false)
            return;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
        winloc='T';

    }
    else
    {
       if(isFormLoaded==false)
            return;
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
        winloc='N';
    }

    url = sContextPath+'/components/workitem/view/document_list_view.jsf';
    url = appendUrlSession(url);
    var wFeatures = 'scrollbars=no,width='+(window1W)+',height='+(windowH+20)+',left='+window1Y+',top='+window1X+',resizable=yes,status=1';
     var listParam=new Array();
    listParam.push(new Array("wid",encode_ParamValue(wid)));
    listParam.push(new Array("pid",encode_ParamValue(pid)));
    listParam.push(new Array("taskid",encode_ParamValue(taskid)));
    listParam.push(new Array("randNum",Math.random()));
    listParam.push(new Array("Action","doc"));
    listParam.push(new Array("winloc",winloc));
    if(showDocListInReadMode)
        listParam.push(new Array("ShowDocListInReadMode","Y"));
    else
        listParam.push(new Array("ShowDocListInReadMode","N"));

    docListwin = openNewWindow(url,'DocumentList',wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);

    docListwin.focus();
    cancelBubble(event);
}

function hideWIFromList(sourceInsId){
        if(typeof strOpenWI=='undefined' || (typeof strOpenWI!='undefined' && strOpenWI!="Y")){
        /*var pid=document.getElementById('wdesk:pid').value;
        var wid=document.getElementById('wdesk:wid').value;
        var wLRowId=pid+"-"+wid;
        if(window.opener && window.opener.document.getElementById(wLRowId)){
            var wLRowRef=window.opener.document.getElementById(wLRowId);
            wLRowRef.style.display="none";
        }*/

        var wiCount = parseInt(document.forms["wdesk"]["wdesk:wi_count"].value);
        var pid=document.getElementById('wdesk:pid').value;
        var wid=document.getElementById('wdesk:wid').value;
        var taskid=document.getElementById('wdesk:taskid').value;
             
        var parentWinRef = null;
        
        if(window.opener != undefined){
            parentWinRef = window.opener; 
            if((typeof prevNextUnlock != 'undefined') && (prevNextUnlock == 'Y')){
                var ref = parentWinRef.document.getElementById('frmworkitemlist:'+pid+"_"+wid);
                if(ref){
                    wiCount = ref.innerHTML - 0;
                }
            }
        } else {
            try{
                sourceInsId = parseInt(sourceInsId);                
                parentWinRef = window.parent.getComponentRef(sourceInsId).contentWindow;                
            } catch(e){}
        }  

        if(parentWinRef != null){
            var wlTable = parentWinRef.document.getElementById("frmworkitemlist:pnlResult");
            if(wlTable !=null && wlTable !=undefined) {
                
                if(wlTable.tBodies[0].rows[wiCount+1]){
                    
                    var jsonOutputWI=parentWinRef.document.getElementById("frmworkitemlist:hidWIJson"+(wiCount+1)).innerHTML;
                    jsonOutputWI=eval("("+jsonOutputWI+")");

                    var tablepid='';
                    var tablewid='';
                    var tabletaskid='';

                    var arrobjJsonOutput= jsonOutputWI.Outputs;
                    for(var i=0;i<arrobjJsonOutput.length;i++){
                    var outputJson=arrobjJsonOutput[i];
                    var objJson=outputJson.Output;
                    if(objJson.Name=='ProcessInstanceID'){
                        tablepid = encode_ParamValue(objJson.Value)
                    }
                    if(objJson.Name=='WorkitemID'){
                        tablewid = encode_ParamValue(objJson.Value)
                    }
                        if(objJson.Name=='taskid'){
                            tabletaskid = encode_ParamValue(objJson.Value)
                        }
                    }

                    if(pid == tablepid && tablewid == tablewid){
                    	wlTable.tBodies[0].rows[wiCount+1].style.display = "none";
                        parentWinRef.initTableHeaderFix();
                    }
                }
            }
        }
    }
}

function showWIInList(sourceInsId){
        if(typeof strOpenWI=='undefined' || (typeof strOpenWI!='undefined' && strOpenWI!="Y")){
        /*var pid=document.getElementById('wdesk:pid').value;
        var wid=document.getElementById('wdesk:wid').value;
        var wLRowId=pid+"-"+wid;
        if(window.opener && window.opener.document.getElementById(wLRowId)){
            var wLRowRef=window.opener.document.getElementById(wLRowId);
            wLRowRef.style.display="none";
        }*/

        var wiCount = parseInt(document.forms["wdesk"]["wdesk:wi_count"].value);
        var pid=document.getElementById('wdesk:pid').value;
        var wid=document.getElementById('wdesk:wid').value;
        var taskid=document.getElementById('wdesk:taskid').value;
                
        var parentWinRef = null;
        
        if(window.opener != undefined){
            parentWinRef = window.opener.document;            
        } else {
            try{
                sourceInsId = parseInt(sourceInsId);                
                parentWinRef = window.parent.getComponentRef(sourceInsId).contentWindow.document;                
            } catch(e){}
        }

        if(parentWinRef != null){
            var wlTable = parentWinRef.getElementById("frmworkitemlist:pnlResult");
            if(wlTable !=null && wlTable !=undefined) {
                var rowCount = wlTable.tBodies[0].rows.length-1;
                if(wlTable.tHead != null){
                    wlTable.tHead.rows[0].style.display = "inline";
                }
                for(var count = 0; count < rowCount;count++){
                    
                    var jsonOutputWI=parentWinRef.getElementById("frmworkitemlist:hidWIJson"+(count+1)).innerHTML;
                    jsonOutputWI=eval("("+jsonOutputWI+")");

                    var tablepid='';
                    var tablewid='';
                    var tabletaskid='';

                    var arrobjJsonOutput= jsonOutputWI.Outputs;
                    for(var i=0;i<arrobjJsonOutput.length;i++){
                    var outputJson=arrobjJsonOutput[i];
                    var objJson=outputJson.Output;
                    if(objJson.Name=='ProcessInstanceID'){
                        tablepid = encode_ParamValue(objJson.Value)
                    }
                    if(objJson.Name=='WorkitemID'){
                        tablewid = encode_ParamValue(objJson.Value)
                    }
                        if(objJson.Name=='taskid'){
                            tabletaskid = encode_ParamValue(objJson.Value)
                        }
                    }

                    if(pid == tablepid && tablewid == tablewid){
                        wlTable.rows[count+2].style.display = "inline";
                    }else{
                        if(wlTable.rows[count+2].style.display != "none"){
                            wlTable.rows[count+2].style.display = "inline";
                        }
                    }
                }
            }
        }
    }
}

function hideTaskFromList(sourceInsId){
    if(typeof strOpenWI=='undefined' || (typeof strOpenWI!='undefined' && strOpenWI!="Y")){
        /*var pid=document.getElementById('wdesk:pid').value;
        var wid=document.getElementById('wdesk:wid').value;
        var wLRowId=pid+"-"+wid;
        if(window.opener && window.opener.document.getElementById(wLRowId)){
            var wLRowRef=window.opener.document.getElementById(wLRowId);
            wLRowRef.style.display="none";
        }*/

        //var wiCount = parseInt(document.forms["wdesk"]["wdesk:wi_count"].value);
        var pid=document.getElementById('wdesk:pid').value;
        var taskid=document.getElementById('wdesk:taskid').value;
                
        var parentWinRef = null;
        
        if(window.opener != undefined){
            parentWinRef = window.opener.document;            
        } else {
            try{
                sourceInsId = parseInt(sourceInsId);                
                parentWinRef = window.parent.getComponentRef(sourceInsId).contentWindow.document;                
            } catch(e){}
        }
        var tablepid,tabletaskid;
        if(parentWinRef != null){
            var formid = "tasklist";
            var wlTable = parentWinRef.getElementById(formid+":TaskList");
            if(wlTable == null){
                formid = "mytasks";
                wlTable = parentWinRef.getElementById(formid+":TaskList");
            }
            if(wlTable !=null && wlTable !=undefined) {
                var rowCount = wlTable.rows.length;
                for(var i = 0; i < rowCount;i++){
                    tablepid = parentWinRef.getElementById(formid+":TaskList:" + i + ":processInstanceId").value;
                    tabletaskid = parentWinRef.getElementById(formid+":TaskList:" + i + ":taskid").value;
                    if(pid== tablepid && taskid==tabletaskid){
                        wlTable.rows[i].style.display = "none";
                    }
                }
            }
        }
    }
}

function hideCaseFromList(sourceInsId){
    if(typeof strOpenWI=='undefined' || (typeof strOpenWI!='undefined' && strOpenWI!="Y")){
        /*var pid=document.getElementById('wdesk:pid').value;
        var wid=document.getElementById('wdesk:wid').value;
        var wLRowId=pid+"-"+wid;
        if(window.opener && window.opener.document.getElementById(wLRowId)){
            var wLRowRef=window.opener.document.getElementById(wLRowId);
            wLRowRef.style.display="none";
        }*/

        //var wiCount = parseInt(document.forms["wdesk"]["wdesk:wi_count"].value);
        var pid=document.getElementById('wdesk:pid').value;
                
        var parentWinRef = null;
        
        if(window.opener != undefined){
            parentWinRef = window.opener.document;            
        } else {
            try{
                sourceInsId = parseInt(sourceInsId);                
                parentWinRef = window.parent.getComponentRef(sourceInsId).contentWindow.document;                
            } catch(e){}
        }
        var tablepid,tabletaskid;
        if(parentWinRef != null){
            var formid = "mytasks";
            var wlTable = parentWinRef.getElementById(formid+":CaseList");

            if(wlTable !=null && wlTable !=undefined) {
                var rowCount = wlTable.rows.length;
                for(var i = 0; i < rowCount;i++){
                    tablepid = parentWinRef.getElementById(formid+":CaseList:" + i + ":processInstanceId").value;
                    if(pid== tablepid){
                        wlTable.rows[i].style.display = "none";
                    }
                }
            }
        }
    }
}

function ReloadInterfaces()
{
    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var formWindow = window_workdesk.getWindowHandler(window_workdesk.windowList, "formGrid");
    var ngformIframe = formWindow.document.getElementById("ngformIframe");    
    if((window_workdesk.wiproperty.formType == "NGFORM") && (window_workdesk.ngformproperty.type == "applet") && !window_workdesk.bDefaultNGForm && !window_workdesk.bAllDeviceForm){
       if(formWindow){
           if(ngformIframe != null){
               // parameter passing           
               var ngformIframeWindow = ngformIframe.contentWindow;
               
               var wiDummySaveStructWinRef = ngformIframeWindow;
                if(formIntegrationApr == "4"){
                    wiDummySaveStructWinRef = window;
                }
                
               wiDummySaveStructWinRef.WiDummySaveStruct.Type = 'ReloadInterfaces';
               wiDummySaveStructWinRef.WiDummySaveStruct.Params.window_workdesk = window_workdesk;               

               if(formIntegrationApr == "4"){
                    ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.fireFormValidation('S','Y')");	                                                                
               } else {						
                    ngformIframeWindow.clickLink('cmdNgFormDummySave');
               }
           }
           return false;
       }
   } else {
       if(formWindow && window_workdesk.bCustomIForm){
            if(ngformIframe != null){
                if(typeof ngformIframe.contentWindow.customIFormHandler != 'undefined'){
                    ngformIframe.contentWindow.customIFormHandler('S','Y');
                }
            }
        }
        
       commonReloadInterfaces(window_workdesk);
   }
   
    /*if(window_workdesk.for_save('dummysave') == 'failure')
    {
        return false;
    }

    ExecuteRules(window_workdesk);
    return true;*/
}

function commonReloadInterfaces(window_workdesk)
{
    if(window_workdesk.for_save('dummysave') == 'failure')
    {
        return false;
    }

    ExecuteRules(window_workdesk);
    return true;
}

function ExecuteRules(window_workdesk){
    var pid=window_workdesk.document.getElementById('wdesk:pid').value;
    var wid=window_workdesk.document.getElementById('wdesk:wid').value;
    var taskid=window_workdesk.document.getElementById('wdesk:taskid').value;
    var ExecRulesObj = new net.ContentLoader('../ajaxExecuteRules.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid),ExecuteRulesHandler,null,"POST","");
}

function ExecuteRulesHandler()
    {
        if(this.req.responseText == 'SUCCESS'){
            ReloadHandler();
        }

    }


function RefreshWiInterfaces()
{

    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;

    var docWindow=getWindowHandler(winList,"tableGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var todoWindow = getWindowHandler(winList,"todoGrid");
    var taskWindow = getWindowHandler(winList,"taskGrid");
    var formWindow = getWindowHandler(winList,"formGrid");

    if(formWindow)
    {
        var url = "";
        if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm) {
            url += "ngform_main_view.jsf";
        } else {
            url += "form_main_view.jsf";
        }
        if(bFetchModifiedFVB){
            url += "?FetchModified=Y";
            bFetchModifiedFVB = false;
        }
        if(formWindow.windowProperty.winloc=="M")
            sendAsynAjaxRequest('form', url);
        else
            formWindow.sendAsynAjaxRequest('form', url);
        
    }
    
    if(todoWindow)
    {
        if(todoWindow.windowProperty.winloc=="M")
            sendAsynAjaxRequest('todo', "todo_main_view.jsf");
        else
            todoWindow.sendAsynAjaxRequest('todo', "todo_main_view.jsf");
    }
    if(taskWindow)
    {
        var url = "";
        if((wiproperty.TaskFormType == "NGFORM") && (taskngformproperty.type == "applet") && !bDefaultNGForm) {
            url += "taskngform_main_view.jsf";
        } else {
            url += "task_main_view.jsf";
        }
        if(taskWindow.windowProperty.winloc=="M")
            sendAsynAjaxRequest('task', url);
        else
            taskWindow.sendAsynAjaxRequest('task', url);
    }
    if(excpWindow)
    {
        if(excpWindow.windowProperty.winloc=="M")
        {
            sendAsynAjaxRequest('exp', "exp_main_view.jsf");
        // InitHWExp();  //puneet tbd
        }
        else
        {
            excpWindow.sendAsynAjaxRequest('exp', "exp_main_view.jsf");
            //excpWindow.InitHWExp();
        }
    }
    if(docWindow)
    {
        if(docWindow.windowProperty.winloc=="M")
        {
            sendAsynAjaxRequest('doc', "doc_main_view.jsf");
            //InitHWDoc();
        }
        else
        {
            docWindow.sendAsynAjaxRequest('doc', "doc_main_view.jsf");
        //docWindow.InitHWDoc();  //puneet tbd
        }
    }
}

function resizeWiInterfaces(workdeskView){
    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;

    var docWindow=getWindowHandler(winList,"tableGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var todoWindow = getWindowHandler(winList,"todoGrid");
    var taskWindow = getWindowHandler(winList,"taskGrid");
    var formWindow = getWindowHandler(winList,"formGrid");
    
    workdeskView = (typeof workdeskView == 'undefined')? "": workdeskView;
    if(workdeskView == "emwd"){
        if(formWindow){
            initHWFormNew();
        }

        if(todoWindow){
            InitHWTodoNew();
        }
    
        if(taskWindow){
            InitHWTaskNew();
        }

        if(excpWindow){
            InitHWExpNew();
        }

        if(docWindow){
            InitHWDoc("Resize");
        }
    }
}

function RefreshEmbWiInterfaces(){
    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;

    var docWindow=getWindowHandler(winList,"tableGrid");   
    var formWindow = getWindowHandler(winList,"formGrid");
    var excpWindow = getWindowHandler(winList,"exceptionGrid");
    var todoWindow = getWindowHandler(winList,"todoGrid");
    
    if(formWindow)
    {
        initHWForm(heightH, compWidth);
    }
  
    if(docWindow)
    {        
        if((isEmbd == 'Y') && (wdView == "em")) {
            var docType = "";
            var docExt = "";
            var docIndex = -1;

            var objCombo = document.getElementById('wdesk:docCombo');                
            if(objCombo){        
                docIndex = objCombo.value - 0;
                if(docIndex > 0){
                    var tmpParsedDocJSON = eval("("+document.getElementById('wdesk:docInfoJSON').value+")");    
                    var docInfoJSON = tmpParsedDocJSON[docIndex];
                    if(typeof docInfoJSON != 'undefined'){
                        docType = docInfoJSON.document[0].DocType;
                        docExt = docInfoJSON.document[0].DocExt;
                    }
                }
            }
            
            var ifrm=document.getElementById("IVApplet");
            if(ifrm == null || ifrm == undefined){
                ifrm=document.IVApplet;
            }
            
            if(ifrm){
                ifrm.height=wiHeight - 55 + "px";
                ifrm.style.width ="100%";     
            }
            
            var isIFrame = (docType != 'I' && docType != '' && docExt != "txt") || (isOpAll == "Y")? true: false;
            
            ifrm = document.getElementById('docviewerDiv'); 
            if(isIFrame){                
                ifrm=document.getElementById('docviewer');  
            }
            
            if(ifrm){                                
                ifrm.style.display = "block";                                
                ifrm.height=wiHeight - 70;

                if(isIFrame){  
                    ifrm.style.height = wiHeight-55 + "px";
                    ifrm.style.width = "100%"; 
                    
                   /* var container = $("#contentDiv").find(".mCSB_container")[0];
                   if(container){   
                       container.style.width = "auto";    
                   }*/
                    
                    if(isOpAll == "Y"){                        
                        opall.PARAM.ViewerHeight=(wiHeight-80-55)+"px";
                        isOnchangeDoc="onchange";
                    }
                } else {
                   //var elementHeightWidth = getElementHeightWidth(ifrm);                
                   //ifrm.style.height = "100%";
                   //ifrm.style.width ="100%";     

                   /*if(elementHeightWidth.Width > 0){
                       var docviewerEle = $("#contentDiv").find(".mCSB_container")[0];
                       if(docviewerEle){   
                           docviewerEle.style.width = elementHeightWidth.Width + "px";    
                       }
                   }*/
                }
            }
        }
        
        if(selectedTab == "doc"){
            initDocInterface();
        }
    }
    
    if(excpWindow){
        /*var container = $("#containerDiv").find(".mCSB_container")[0];
        if(container){
            container.style.width = compWidth + "px";
            container.style.height = "auto";
        }*/
    }
    
    if(todoWindow){
        /*var container = $("#containerDiv").find(".mCSB_container")[0];
        if(container){
            container.style.width = compWidth + "px";
            container.style.height = "auto";
        }*/
    }
}

function initDocInterface(){
    try{
        if(document.getElementById("wdesk:docOperation")){
            if(document.getElementById("docListFrame").style.display == "inline"){
                document.getElementById("docListFrame").style.display = "none";
            }        
        }
    }catch(e){        
    }
}

function initEmbTabs(){
    var obj = null;
    if(formView == 'Y' || formView == 'y' || formView == ''){
        obj = document.getElementById("frmTabDiv");
        obj.style.display = "block";
        obj = document.getElementById("frmSep");
        obj.style.display = "block";
        obj = document.getElementById("wdesk:frmImg");
        obj.style.display = "inline";        
        if(selectedTab == null){
            selectedTab = 'form';
            obj.className = "tabItemSelected";
        }        
    }

    if(todoListView == 'Y' || todoListView == 'y' || todoListView == ''){
        obj = document.getElementById("todoTabDiv");
        obj.style.display = "block";
        obj = document.getElementById("todoSep");
        obj.style.display = "block";
        obj = document.getElementById("wdesk:todoImg");
        obj.style.display = "inline";        
        if(selectedTab == null){
            selectedTab = 'todo';
            obj.className = "tabItemSelected";
        }        
    }

    if(documentView == 'Y' || documentView == 'y' || documentView == ''){
        obj = document.getElementById("docTabDiv");
        obj.style.display = "block";
        obj = document.getElementById("docSep");
        obj.style.display = "block";
        obj = document.getElementById("wdesk:docImg");
        obj.style.display = "inline";        
        if(selectedTab == null){
            selectedTab = 'doc';
            obj.className = "tabItemSelected";
        }        
    }
    
    if(exceptionView == 'Y' || exceptionView == 'y' || exceptionView == ''){
        obj = document.getElementById("expTabDiv");
        obj.style.display = "block";
        obj = document.getElementById("wdesk:expImg");
        obj.style.display = "inline";
        if(selectedTab == null){
            selectedTab = 'exp';
            obj.className = "tabItemSelected";
        }        
    }
}

function initTabEvents(){
    // initialize tab events
    formImgRef.onclick = selectedTab == 'form'? Function("return false;"): Function("enableFrm();return false;");
    todoImgRef.onclick = selectedTab == 'todo'? Function("return false;"): Function("enableTodo();return false;");
    docImgRef.onclick = selectedTab == 'doc'? Function("return false;"): Function("enableDoc();return false;");
    exceptionImgRef.onclick = selectedTab == 'exp'? Function("return false;"): Function("enableExp();return false;");

    // initialize tab image path
    /*var imgContext = sContextPath+"/resources/"+ln+"images/";
    formImgRef.src = imgContext + (selectedTab == 'form'? 'tab_form_en_sm.gif': 'tab_form_dis_sm.gif');
    todoImgRef.src = imgContext + (selectedTab == 'todo'? 'tab_todo_en_sm.gif': 'tab_todo_dis_sm.gif');
    docImgRef.src = imgContext + (selectedTab == 'doc'? 'tab_doc_en_sm.gif': 'tab_doc_dis_sm.gif');
    exceptionImgRef.src = imgContext + (selectedTab == 'exp'? 'tab_exp_en_sm.gif': 'tab_exp_dis_sm.gif');*/
}

function initNLoadEmbInterfaces(){
    if(selectedTab != 'form') {
        if(wiproperty.formType == "NGFORM" && ngformproperty.type == "applet") {
            getInterface('form');
        }
    }

    getInterface(selectedTab);
}

function adjustEmbTabs(){
    if(selectedTab != 'form'){
        document.getElementById("formDiv").style.display = "none";
    } 
    if(selectedTab != 'doc'){
        document.getElementById("docDiv").style.display = "none";
    } 
    if(selectedTab != 'todo'){
        document.getElementById("todoDiv").style.display = "none";
    }
    if(selectedTab != 'task'){
        if(document.getElementById("taskDiv") != null){
            document.getElementById("taskDiv").style.display = "none";
        }
    }
    if(selectedTab != 'exp'){
        document.getElementById("expDiv").style.display = "none";
    }
}

function setDocType(fileName,ArgList,trigWin,todoId)
{
    closeFrm = "ok";
    var window_workdesk='';
    if(trigWin == 'action'){
        if(typeof window.opener.document.forms['wdesk'] == 'undefined')
            window_workdesk = window.opener.opener;
        else
            window_workdesk = window.opener;
    }
    if(trigWin=='exception'){
        if(window.opener.opener.windowProperty.winloc == 'M')
            window_workdesk = window.opener.opener;
        else if(window.opener.opener.windowProperty.winloc == 'T')
             window_workdesk = window.opener.opener.opener.opener;
        else
             window_workdesk = window.opener.opener.opener;
    }
     else if(trigWin=='todolist'){
        if(window.opener.windowProperty.winloc == 'M')
            window_workdesk = window.opener;
        else if(window.opener.windowProperty.winloc == 'T')
            window_workdesk = window.opener.opener.opener;
        else
            window_workdesk = window.opener.opener;
    }
    var winList = window_workdesk.windowList;
    var docWindow = getWindowHandler(winList,"tableGrid");
    var pid = window_workdesk.document.getElementById('wdesk:pid').value;
    var wid = window_workdesk.document.getElementById('wdesk:wid').value;
    var taskid = window_workdesk.document.getElementById('wdesk:taskid').value;

    var docType = document.getElementById('genRespForm:docListMenu').value;
    if(docType != '')
    {
        var url = '../ajaxGenrespObj.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&DocType='+encode_utf8(docType)+'&fileName='+encode_utf8(fileName)+'&ArgList='+encode_utf8(ArgList);
        url = appendUrlSession(url);
        var xhReq;
        if (window.XMLHttpRequest)
            xhReq = new XMLHttpRequest();
        else if (window.ActiveXObject)
            xhReq = new ActiveXObject("Microsoft.XMLHTTP");
        xhReq.open("POST", url, false);
        xhReq.send(null);
        var genResponse = eval("("+xhReq.responseText+")");
        if(genResponse[0].flag == '-1'){
            if(trigWin=='todolist'){
                var assocField = window.opener.document.getElementById("wdesk:todo"+todoId).alt;
                window.opener.document.getElementById("wdesk:todo"+todoId).value = genResponse[0].status;
                window.opener.setFormData(assocField,genResponse[0].status,'m');

            }
            //alert(genResponse[0].trigErr);
        }
        else{
            if(trigWin=='todolist'){
                var assocField = window.opener.document.getElementById("wdesk:todo"+todoId).alt;
                window.opener.document.getElementById("wdesk:todo"+todoId).value = genResponse[0].status;
                window.opener.setFormData(assocField,genResponse[0].status,'m');

            }
            if(docWindow)
               addGenResDoc(genResponse[0].docName,genResponse[0].docIndex,genResponse[0].diskName,docWindow, window_workdesk.DocOrgName);
        }
    }
    if(trigWin=='exception')
        window.opener.close();
    window.close();

}

function getInterfaceKey(intDivId){
    var intArrayRef = interfaceMap.getIterator();      
    for(var i=0; i<intArrayRef.length; i++){        
        if(intArrayRef[i].value.DivId == intDivId){
            return intArrayRef[i].key;
        }        
    }    
    return null;
}

function getCaseInterfaceKey(intDivId){
    var intArrayRef = caseInterfaceMap.getIterator();      
    for(var i=0; i<intArrayRef.length; i++){        
        if(intArrayRef[i].value.DivId == intDivId){
            return intArrayRef[i].key;
        }        
    }    
    return null;
}

function setIntPosition(intJason, divRef){
    intJason.Left = divRef.style.left.split('p')[0];
    intJason.Top = divRef.style.top.split('p')[0];
    intJason.Width = divRef.style.width.split('p')[0];
    intJason.Height = divRef.style.height.split('p')[0];
}
        
var INTERFACE_SEP_MARGIN = 5;        
var INTERFACE_BOTTOM_MARGIN = 5;        

function renderCase() {
    var contDivRef = document.getElementById("containerDiv"); 
    contDivRef.style.display = "block";
    // Resets the document.documentElement.clientHeight
    contDivRef.style.height = "0px";
    contDivRef.style.width = document.body.clientWidth  + "px";                
    contDivRef.style.height = (document.documentElement.clientHeight - document.body.clientHeight) - 2 +"px";                   
    
    var contDivLeft = findPosX(contDivRef);
    var contDivTop = findPosY(contDivRef);
    var screenDimension = getScreenDimension();
    var screenWidth = screenDimension.ScreenWidth;
    var screenHeight = screenDimension.ScreenHeight;
    var caseDeskJason = caseDeskLayout;
    var wDeskJason = wDeskLayout;   
    
    caseInterfaceMap.clear();
    var noOfInterfaces = parseInt(wDeskJason.InterfacesCount);
    if(noOfInterfaces > 0){
        for(var i=0; i<wDeskJason.Interfaces.length; i++){
            if(wDeskJason.Interfaces[i].Interface.Available == "true"){
                caseInterfaceMap.put(wDeskJason.Interfaces[i].Interface.Type, wDeskJason.Interfaces[i].Interface);
            }
        }
    }
    
    // aligning container 1, 2 and 3
    if (!expCollapseFlag) { //Bug 66744
        for (var i = 0; i < caseDeskJason.Interfaces.length; i++) {
            divRef = document.getElementById(caseDeskJason.Interfaces[i].Interface.DivId);
            divRef.style.left = contDivLeft + (((caseDeskJason.Interfaces[i].Interface.Left) * screenWidth) / caseDeskJason.ScreenWidth) -9+ "px";
            divRef.style.top = contDivTop + (((caseDeskJason.Interfaces[i].Interface.Top) * screenHeight) / caseDeskJason.ScreenHeight) + 21 + "px";
            divRef.style.width = (caseDeskJason.Interfaces[i].Interface.Width * screenWidth) / caseDeskJason.ScreenWidth + "px";
            divRef.style.height = (caseDeskJason.Interfaces[i].Interface.Height * screenHeight) / caseDeskJason.ScreenHeight -20+ "px";
        }
    }
}
function renderWDesk(){               
    var divRef = document.getElementById("wdesk:noIntMsg");
    divRef.style.display = "none";
    
    var contDivRef = document.getElementById("containerDiv"); 
    contDivRef.style.display = "block";
    // Resets the document.documentElement.clientHeight
    contDivRef.style.height = "0px";
    contDivRef.style.width = document.body.clientWidth - 2 + "px";                
    contDivRef.style.height = (document.documentElement.clientHeight - document.body.clientHeight) - 2 +"px";                   
       
    var wDeskJason = wDeskLayout;   
    
    var screenDimension = getScreenDimension();
    var screenWidth = screenDimension.ScreenWidth;
    var screenHeight = screenDimension.ScreenHeight;

    var noOfInterfaces = parseInt(wDeskJason.InterfacesCount);
    if(noOfInterfaces <= 0){
        // No interface is associated with the activity
        //contDivRef.style.border = "1px solid #A0A0A0";        
        divRef.style.display = "block";            
    } else {        
        var contDivLeft = findPosX(contDivRef);
        var contDivTop = findPosY(contDivRef);

        for(var i=0; i<wDeskJason.Interfaces.length; i++){
            if(wDeskJason.Interfaces[i].Interface.Available == "true"){
                divRef = document.getElementById("div_C"+(i+1));
                divRef.style.left = contDivLeft + (((wDeskJason.Interfaces[i].Interface.Left)*screenWidth)/wDeskJason.ScreenWidth) + "px";
                divRef.style.top = contDivTop + (((wDeskJason.Interfaces[i].Interface.Top)*screenHeight)/wDeskJason.ScreenHeight) + "px";
                divRef.style.width = (wDeskJason.Interfaces[i].Interface.Width*screenWidth)/wDeskJason.ScreenWidth + "px";
                divRef.style.height = (wDeskJason.Interfaces[i].Interface.Height*screenHeight)/wDeskJason.ScreenHeight + "px";
                setIntPosition(wDeskJason.Interfaces[i].Interface, divRef);

                //Bug 67671 starts
                if(wDeskJason.Interfaces[i].Interface.Url=="SAPGUIAdapter" && oldWDJason.WIViewMode == 'R')
                divRef.style.display = "none"; 
                else
                divRef.style.display = "block";
                //Bug 67671 ends

                wDeskJason.Interfaces[i].Interface.Status = "O";            
                interfaceMap.put("div_C"+(i+1), wDeskJason.Interfaces[i].Interface);
                getInterface(wDeskJason.Interfaces[i].Interface.DivId,wDeskJason.Interfaces[i].Interface.Url,wDeskJason.Interfaces[i].Interface.EXT); 
            }
        }        
    }
    
    setWIHeader();
}

function adjustInterfaces(wdJason, bRenderWDesk){   
    bRenderWDesk = (typeof bRenderWDesk == "undefined")? false: bRenderWDesk;
    
    if(oldWDJason.WDeskLayout.WDeskType == "F"){
        cancelSaveFixedLayout("A"); 
        clearFixedLayout();
    } else {
        cancelSaveLayout();
    }
    
    if(!bRenderWDesk && (oldWDJason.RouteId == newWDJason.RouteId) && (oldWDJason.ActivityId == newWDJason.ActivityId)){
        var key = "";
        var divRef = null;
        for(var i=0; i<newWDJason.WDeskLayout.Interfaces.length; i++){
            getInterface(newWDJason.WDeskLayout.Interfaces[i].Interface.DivId);   
            key = getInterfaceKey(newWDJason.WDeskLayout.Interfaces[i].Interface.DivId);
            
            // updates positioning of interfaces
            divRef = document.getElementById(key);
            setIntPosition(newWDJason.WDeskLayout.Interfaces[i].Interface, divRef);
            
            interfaceMap.put(key, newWDJason.WDeskLayout.Interfaces[i].Interface);
            newWDJason.WDeskLayout.Interfaces[i].Interface.Status = "O";
        }
    } else {
        getInterfaceView();
        interfaceMap.clear();
        renderWDesk();
    }
}

function getInterfaceView(){
    var url = sContextPath+"/components/workitem/view/interface_main_view.jsf";
    var params = "Action=getinterfaceview"+"&pid="+encode_utf8(pid)+"&wid="+encode_utf8(wid)+"&taskid="+encode_utf8(taskid);
    var reqRef = new net.ContentLoader(url, null, null, "POST", params, false);
    if(reqRef.req.status == 200) {
        var respText = reqRef.req.responseText;
        respText = respText.substring(respText.indexOf('<container>')+11,respText.lastIndexOf('</container>'));
        document.getElementById('containerDiv').innerHTML = respText;
    }    
}

var mouseStatus;
var lastPosX;
var lastPosY;
var lastResizePosX;
var lastResizePosY;
var srcElementId;
var moveDir = "";
var isDragMode = false;
var MOVE_STEP = 5;

function setPositionForDrag(e, contDivId){
    if(varEditLayout==0){
        var curEvent = (typeof event == 'undefined'? e: event);       
        
        var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1;
    
        if(typeof window.chrome != 'undefined' || isSafari) {
            lastPosX = curEvent.clientX + document.body.scrollLeft;    
            lastPosY = curEvent.clientY + document.body.scrollTop;
        } else {
            lastPosX = curEvent.clientX + document.documentElement.scrollLeft;    
            lastPosY = curEvent.clientY + document.documentElement.scrollTop;
        }
        
        lastResizePosX = lastPosX;
        lastResizePosY = lastPosY;   
    
        srcElementId = contDivId;
        var ref = document.getElementById(srcElementId);    
        
        showBlankIframe(ref);
        repositionMoveDiv(ref);    
        
        isDragMode = true;    
        mouseStatus='down';
        showPopupMask();
    }
    
}

function setPosition(e, contDivId, direction){
    var curEvent = (typeof event == 'undefined'? e: event); 
    
    var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1;
    
    if(typeof window.chrome != 'undefined' || isSafari) {
        lastPosX = curEvent.clientX + document.body.scrollLeft;    
        lastPosY = curEvent.clientY + document.body.scrollTop;   
    } else {
        lastPosX = curEvent.clientX + document.documentElement.scrollLeft;    
        lastPosY = curEvent.clientY + document.documentElement.scrollTop; 
    }
    
    lastResizePosX = lastPosX;
    lastResizePosY = lastPosY;         
    
    srcElementId = contDivId;
    var ref = document.getElementById(srcElementId);
    
    showBlankIframe(ref);
    repositionMoveDiv(ref);    
    
    moveDir = direction;
    mouseStatus = 'down';
    showPopupMask();
}

function onMouseUpWrapper(e){
    if(wDeskLayout.WDeskType == 'E'){
        getPosition(e);
    } else {
        onMouseUpFixedLayout(e);
    }
}

function getPosition(e){    
    if(mouseStatus == 'down'){
        var ref = document.getElementById(srcElementId);   
        
        if(isDragMode){                        
            ref.style.top = parseInt(ref.style.top.split('p')[0]) + (lastResizePosY - lastPosY) + "px";
            ref.style.left = parseInt(ref.style.left.split('p')[0]) + (lastResizePosX - lastPosX) + "px";            
        }else {
             var newWidth = 0;
             var newHeight = 0;
            if(moveDir == "both") {
                newWidth = parseInt(ref.style.width.split('p')[0]) + (lastResizePosX - lastPosX);
                newHeight = parseInt(ref.style.height.split('p')[0]) + (lastResizePosY - lastPosY);

                ref.style.width = newWidth + "px";
                ref.style.height = newHeight + "px";
                ResetHWInterfaces(srcElementId,newHeight,newWidth);

            } else if(moveDir == "ver") {        

                newWidth = parseInt(ref.style.width.split('p')[0]) + (lastResizePosX - lastPosX);
                ref.style.width = newWidth + "px";

                ResetHWInterfaces(srcElementId,newHeight,newWidth);

            } else if(moveDir == "hor") {
                newHeight = parseInt(ref.style.height.split('p')[0]) + (lastResizePosY - lastPosY);
                ref.style.height = newHeight + "px";
                ResetHWInterfaces(srcElementId,newHeight,newWidth);
            }
        }        
        
        setWIHeader();
        
        ref = document.getElementById("moveDiv");
        ref.style.display = "none";                
        hideBlankIframe();
    }    
    
    isDragMode = false;
    mouseStatus = 'up';
    moveDir = "";        
    
    if(typeof bLaunchMessage == 'undefined'){
        bLaunchMessage = false;
    }
    
    if(!bLaunchMessage){
        hidePopupMask();   
    }
}

function setWIHeader(){
    var screenWidth = window.screen.width;
    var isFirefox = (navigator.appName.indexOf('Netscape') > -1);
    if(isFirefox){
        screenWidth=screenWidth;
    }
    var maxRightPos = getMaxWidth();
    //alert(maxRightPos+":"+screenWidth);
    if(maxRightPos > screenWidth){
        document.getElementById("wdesk:placeHolder").style.width = "100%";
    } else {
        document.getElementById("wdesk:placeHolder").style.width = "100%";
    }
}

function getMaxWidth(){
    var intArrayRef = interfaceMap.getIterator();    
    var ref = null;
    var intRightPos = null;
    var maxRightPos = 0;
    for(var i=0; i<intArrayRef.length; i++){                
        ref = document.getElementById(intArrayRef[i].key)
        if(ref!=null){
            intRightPos = parseInt(ref.style.left.split('p')[0]) + parseInt(ref.style.width);            
            if(maxRightPos < intRightPos){
                maxRightPos = intRightPos;
            }
        }
    }    
    
    return maxRightPos;
}

function ResetHWInterfaces(key,newHeight,newWidth)
{
    var Type = getInterfaceTypeFromKey(key);
    var divId = getInterfaceDivIdFromKey(key);

    if(Type == "Document")
    {
        InitHWDoc('Resize');
    }
    else if(Type == "FormView")
    {
        initHWFormNew();
    }
    else if(Type == "FormExtView")//added for Variant : 22 Oct 2013
    {
        initHWFormNew();
    }
    else if(Type == "Exceptions")
    {
        InitHWExpNew();
    }
    else if(Type == "ToDoList")
    {
        InitHWTodoNew();
    }
    else if(Type == "DynamicCase")
    {
        InitHWTaskNew();
    }else if(divId == "SAP0"){
        InitSAPNew(Type);
    }else if(divId == "SAP1"){
        InitSAPNew(Type);
    }else if(divId == "SAP2"){
        InitSAPNew(Type);
    }else if(divId == "SAP3"){
        InitSAPNew(Type);
    }else{
        InitCustomNew(Type);
    }
}

function InitSAPNew(Type){
    var divID=getInterfaceKeyByType(Type)
    var cWidth = document.getElementById(divID).style.width.split('p')[0];
    var cHeight = document.getElementById(divID).style.height.split('p')[0];

    document.getElementById(divID).style.height = cHeight+"px";
    document.getElementById(divID).style.width = cWidth+"px";
    
    var parentElement = document.getElementById("offsetparent").parentElement;
    parentElement = document.getElementById(parentElement.id).parentElement;     
    var sapDefId = document.getElementById("sapDefId").value;
    document.getElementById('sapviewer'+sapDefId).height =  document.getElementById(parentElement.id).style.height;        
    document.getElementById('sapviewer'+sapDefId).height = parseInt(document.getElementById('sapviewer'+sapDefId).height) - 70 +"px";
}
function InitHWTodoNew()
{
    var cWidth = document.getElementById('todoDiv').parentNode.style.width.split('p')[0];
    var cHeight = document.getElementById('todoDiv').parentNode.style.height.split('p')[0];
    
    if((isEmbd == 'Y') && (wdView == "em")){
        document.getElementById('todoDiv').style.height = (cHeight-32)+"px";
        document.getElementById('todoDiv').style.width = (cWidth-4)+"px";
    } else {
        if(wDeskLayout.WDeskType == 'E'){
            document.getElementById('todoDiv').style.height = (cHeight-22)+"px";
            document.getElementById('todoDiv').style.width = (cWidth)+"px";
        }else {
            document.getElementById('todoDiv').style.height = (cHeight-4)+"px";
            document.getElementById('todoDiv').style.width = (cWidth)+"px";
        }
    }
    
    /*getCustomScrollbar("#todoDiv", true, "yx", true, false, true, true);
    var todoEle = $("#todoDiv").find(".mCSB_container")[0];
    if(todoEle){        
        todoEle.style.width = (cWidth)+"px";             
        var scrollDir = (pageDirection == 'ltr')? 'left': 'right';    
        $("#todoDiv").mCustomScrollbar("scrollTo",scrollDir,{scrollInertia:0});
    }*/
}

function InitHWTaskNew()
{
    var cWidth = document.getElementById('taskDiv').parentNode.style.width.split('p')[0];
    var cHeight = document.getElementById('taskDiv').parentNode.style.height.split('p')[0];
    if(cWidth=='' && cHeight==''){ // Handling for CaseView/TaskView
        cWidth = document.getElementById('taskDiv').parentNode.parentNode.style.width.split('p')[0]-2;
        cHeight = document.getElementById('taskDiv').parentNode.parentNode.style.height.split('p')[0]-5;
    }
    if((isEmbd == 'Y') && (wdView == "em")){
        document.getElementById('taskDiv').style.height = (cHeight-32)+"px";
        document.getElementById('taskDiv').style.width = (cWidth-4)+"px";
    } else {
        if(wDeskLayout.WDeskType == 'E'){
            
            if(typeof stractivityType != 'undefined'){
                if(stractivityType=='32'){
                      document.getElementById('taskDiv').style.height = (cHeight-25)+5+"px";
                }
                else{
                   document.getElementById('taskDiv').style.height = (cHeight-25)+"px";
                }
        	} 	
              else{
                    document.getElementById('taskDiv').style.height = (cHeight-25)+"px";
                }
            
            document.getElementById('taskDiv').style.width = (cWidth)+"px";
        }else {
            document.getElementById('taskDiv').style.height = (cHeight-4)+"px";
            document.getElementById('taskDiv').style.width = (cWidth)+"px";
        }
    }
    
    if((wiproperty.TaskFormType == "NGFORM") && (taskngformproperty.type == "applet") && !bDefaultNGForm) {
        var ngformIframe = document.getElementById("taskngformIframe");	
        if(ngformIframe != null){        
            if((isEmbd == 'Y') && (wdView == "em")){
                ngformIframe.style.height = (cHeight - 28) + "px";
            } else {
                if(wDeskLayout.WDeskType == 'E'){
                    ngformIframe.style.height = (cHeight - 28) + "px";                    
                    ngformIframe.style.width = (cWidth - 4) + "px";     
                    try{
                        ngformIframe.contentWindow.updateNGHTMLFormLoad();
                    } catch(e){}                 
                } else {
                    ngformIframe.style.height = (cHeight - 10) + "px"; 
                    ngformIframe.style.width = (cWidth - 10) + "px"; 
                    ngformIframe.style.marginTop = "5px";
                    try{
                        ngformIframe.contentWindow.updateNGHTMLFormLoad();
                    } catch(e){}    
                }
            }
        }
    }else if(wiproperty.formType=="HTMLFORM"){
        /*getCustomScrollbar("#taskDiv", true, "yx", true, false, true, true);
        var taskEle = $("#taskDiv").find(".mCSB_container")[0];
        if (taskEle) {
            taskEle.style.width = (cWidth) + "px";
            var scrollDir = (pageDirection == 'ltr') ? 'left' : 'right';
            $("#taskDiv").mCustomScrollbar("scrollTo", scrollDir, {
                scrollInertia: 0
            });
        }*/
    }
        
    if(document.getElementById('wdesk:taskGrid') != null){
        document.getElementById('wdesk:taskGrid').style.width = (cWidth)+"px";
    }

    var appletDiv = document.getElementById("appletDiv");
    if(appletDiv != null && appletDiv != undefined) {
        appletDiv.style.width = (cWidth-20)+"px";
        var isChrome = window.chrome;
        if(typeof isChrome != 'undefined') {
            if((isEmbd == 'Y') && (wdView == "em")){
                appletDiv.style.height = (cHeight - 40) + "px";
            } else {
                if(wDeskLayout.WDeskType == 'E'){
                    appletDiv.style.height = (cHeight - 40) + "px";
                } else {
                    appletDiv.style.height = (cHeight - 18) + "px";
                }
            }
        }else{
            if((isEmbd == 'Y') && (wdView == "em")){
                appletDiv.style.height = (cHeight - 45) + "px";
            }else {
                if(wDeskLayout.WDeskType == 'E'){
                    appletDiv.style.height = (cHeight - 45) + "px";
                } else {
                    appletDiv.style.height = (cHeight - 23) + "px";
                }
            }
        }
    }    
}

function InitCustomNew(Type){
    var cWidth=document.getElementById(Type+'Div').parentNode.style.width.split('p')[0];
    var cHeight = document.getElementById(Type+'Div').parentNode.style.height.split('p')[0];
    document.getElementById(Type+'Div').style.height = (cHeight-32)+"px";
    document.getElementById(Type+'Div').style.width = (cWidth-4)+"px";
    
    document.getElementById(Type+'Iframe').style.height=(cHeight-32)+"px";
    document.getElementById(Type+'Iframe').style.width=(cWidth-4)+"px";
}

function onMouseMoveWrapper(e){
    if((typeof wDeskLayout != 'undefined') && (wDeskLayout != null)){
        if(wDeskLayout.WDeskType == 'E'){
            onMouseMove(e);
        } else {
            onMouseMoveFixedLayout(e);
        }
    }
}

function onMouseMove(e){    
    if(mouseStatus == 'down'){
        var curEvent = (typeof event == 'undefined'? e: event);
        var pos = DIF_getEventPosition(curEvent);
        
        var movePixX = (pos.x - lastResizePosX)/MOVE_STEP;
        var movePixY = (pos.y - lastResizePosY)/MOVE_STEP;
        
        movePixX = parseInt(movePixX)*MOVE_STEP;
        movePixY = parseInt(movePixY)*MOVE_STEP;
        
        if(movePixX != 0 || movePixY != 0){         
            var ref = document.getElementById("moveDiv");   
            
            if(isDragMode){                
                var contDivRef = document.getElementById("containerDiv");                   
                var posX = parseInt(ref.style.left.split('p')[0]) + movePixX;                
                var posY = parseInt(ref.style.top.split('p')[0]) + movePixY;                
                var isTopLimit = false; 
                var isLeftLimit = false; 
                
                if(posX >= contDivRef.offsetLeft) {
                    ref.style.left = posX + "px";                
                    isLeftLimit = false;
                } else {
                    isLeftLimit = true;
                }
                
                if(posY >= contDivRef.offsetTop) {
                    ref.style.top = posY + "px";                
                    isTopLimit = false;
                } else {
                    isTopLimit = true;
                }
            } else {
                if(moveDir == "both") {     
                    ref.style.width = parseInt(ref.style.width.split('p')[0]) + movePixX + "px";
                    ref.style.height = parseInt(ref.style.height.split('p')[0]) + movePixY + "px";
                } else if(moveDir == "ver") { 
                    ref.style.width = parseInt(ref.style.width.split('p')[0]) + movePixX + "px";
                } else if(moveDir == "hor") {        
                    ref.style.height = parseInt(ref.style.height.split('p')[0]) + movePixY + "px";
                }
            }
            
            if(!isLeftLimit){
                lastResizePosX += movePixX;
            }
            if(!isTopLimit){
                lastResizePosY += movePixY;
            }
        }
    }
 
}

function repositionMoveDiv(divRef){    
    var ref = document.getElementById("moveDiv");    
    ref.style.width = divRef.style.width;
    ref.style.height = divRef.style.height;
    ref.style.top = divRef.style.top;
    ref.style.left = divRef.style.left;
    ref.style.display = "block";
    ref.style.zIndex = "1000";
}

function changeResizeColor(ref, color){    
    ref.style.backgroundColor = color;
}

function cursorMove(target){
    if(varEditLayout==0)
        target.className = 'cursorMove';
    else
        target.className = 'cursorSimple';
}

function cursorSimple(target){
    if(varEditLayout==1) 
        target.className = 'cursorSimple';
    else
        target.className = 'cursorMove';
}

function openDivElement(interfaceDiv,interfaceType,url,externaltype,event){
    event = (typeof event == 'undefined')? null: event;
    if(varEditLayout==1){
       openInterface(interfaceDiv,interfaceType,url,externaltype,event);//For open interfaces in window
    }else{//For open interfaces in Div
        createDivElement(interfaceDiv,interfaceType);
        getInterface(interfaceDiv,url,externaltype);  
        removeInterfaceMenu(interfaceType);
    }
    prepareWdeskDefaultMenu();
}


//var openInterfaceMap = new HashMap();
var WIDTH = 400;
var DEFAULT_DIV_HEIGHT = 300;
function createDivElement(interfaceDiv,interfaceType){    
    var divLength="";
    var status="";
    var containerDivId="";
    var contId="";
    //Bug 64845
    if((interfaceType.indexOf("tcode")>-1) && compositeSAPView=='Y'){
        var interfaceDivID=getInterfaceKeyy(interfaceType);
        if(interfaceDivID!=null){
            var arrDiv=interfaceDivID.split("_");
            status=getInterfaceKeyByStatus(interfaceDivID);
            divLength=arrDiv[1];
        }
    
        if(status=="C" || status=="I"){
            containerDivId="div_"+divLength;
            contId=divLength;
        }else if(status != "O" && status != "I"){
            divLength = interfaceMap.getIterator().length+1;
            containerDivId="div_C"+divLength;
            contId = "C" + divLength;
        }else {
            return;
        }
    }else {
        var interfaceDivID=getInterfaceKeyByType(interfaceType); 
        if(interfaceDivID!=null){
            var arrDiv=interfaceDivID.split("_");
            status=getInterfaceKeyByStatus(interfaceDivID);
            divLength=arrDiv[1];
        }
    
        if(status=="C"){
            containerDivId="div_"+divLength;
            contId=divLength;
        }else if(status != "O" && status != "I"){
            divLength = interfaceMap.getIterator().length+1;
            containerDivId="div_C"+divLength;
            contId = "C" + divLength;
        }else {
            return;
        }
    }
     if(interfaceDivID==null || compositeSAPView=='Y'){
        var containerDiv=createContainerDiv(containerDivId);
        window.document.getElementById("containerDiv").appendChild(containerDiv);

        var params = "ContId="+contId+"&DivId="+interfaceDiv+"&DisplayName="+getDisplayName(interfaceDiv)+"&InterfaceType="+interfaceType+"&WD_UID="+WD_UID+"";
        var url = sContextPath+"/components/workitem/view/interfacetemplate.jsf";
        var reqRef = new net.ContentLoader(url, null, null, "POST", params, false);
        if(reqRef.req.status == 200) {
            var respText = reqRef.req.responseText;
            document.getElementById(containerDivId).innerHTML = respText;
        
            if(status=="C"){
                setInterfaceStatus(containerDivId,"I");
            }else{
                var interfaceJSON={
                    'Type':interfaceType,
                    'DivId':interfaceDiv,
                    'Status':'I'
                };
                setIntPosition(interfaceJSON, containerDiv);
                interfaceMap.put(containerDivId, interfaceJSON);
            }
        }  
    if(document.getElementById("closeimg_"+contId)!=null && document.getElementById("closeimg_"+contId)!=undefined)
        document.getElementById("closeimg_"+contId).style.display="block";
    
    if(document.getElementById("divResizeVer_"+contId)!=null && document.getElementById("closeimg_"+contId)!=undefined)
        document.getElementById("divResizeVer_"+contId).style.display="block";
    
    if(document.getElementById("divResizeHor_"+contId)!=null && document.getElementById("closeimg_"+contId)!=undefined)
        document.getElementById("divResizeHor_"+contId).style.display="block";
    
    if(document.getElementById("divResize_"+contId)!=null && document.getElementById("closeimg_"+contId)!=undefined)
        document.getElementById("divResize_"+contId).style.display="block";
     }
}

function getDisplayName(divId){
    var displayName = "";
    if (divId == "form") {
        displayName = LABEL_FORM;
    } else if (divId == "todo") {
        displayName = LABEL_TODO;
    } else if (divId == "exp") {
        displayName = LABEL_EXCEPTION;
    } else if (divId == "doc") {
        displayName = LABEL_DOCUMENT;
    } else {
        displayName = divId;
    }    
    return displayName;
}

function getCenterCompPosition(){    
    var screenJson = {
        "left":"",
        "top":""
    }    
    screenJson.left = (window.screen.width - WIDTH)/2 + window.document.documentElement.scrollLeft;
    screenJson.top = (window.document.documentElement.clientHeight - DEFAULT_DIV_HEIGHT)/2 + window.document.documentElement.scrollTop;
     
    return screenJson;
}

function showPopupMask()
{
    if(gPopupMask != null) {
        gPopupMask.style.display = "block";
        
        var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1;
        var top = 0, left = 0;
        if(typeof window.chrome != 'undefined' || isSafari) {
            top = document.body.scrollTop;
            left = document.body.scrollLeft;
        } else {
            top = document.documentElement.scrollTop;
            left = document.documentElement.scrollLeft;
        }
        
        gPopupMask.style.height = window.document.documentElement.clientHeight + top + "px";        
        gPopupMask.style.width = document.documentElement.clientWidth + left + "px";
    }
}

function hidePopupMask()
{
    if(gPopupMask != null) {
        gPopupMask.style.display = "none";        
    }
}

var gPopupMask;
function initPopUp() {
    if(gPopupMask == null) {
        // Add the HTML to the body
        
        theBody = document.getElementsByTagName('BODY')[0];
        popmask = document.createElement('div');
        popmask.id = 'popupMask';
        theBody.appendChild(popmask);
        gPopupMask = document.getElementById("popupMask");
        gPopupMask.style.display = "none";
        gPopupMask.style.position = "absolute";
        gPopupMask.style.zIndex = "1000";
        gPopupMask.style.top = "0px";
        gPopupMask.style.left = "0px";
        gPopupMask.style.width = "100%";
        gPopupMask.style.height = "100%";
        gPopupMask.style.opacity = "0.4";
        gPopupMask.style.filter = "alpha(opacity=40)";
        gPopupMask.style.backgroundColor = "#d8dce0";
    }
}

function closeInterface(divId,interfaceType,interfaceDiv){
    if(document.getElementById(divId).style.display=="block"){
        document.getElementById(divId).style.display="none";
        addInterfaceMenu(interfaceType,interfaceDiv);
        mainSave();
        try{
        document.getElementById("containerDiv").removeChild(document.getElementById(divId));
        }catch(e){
            //alert(e);
        }
        setInterfaceStatus(divId,"C");
        setDefaultValues(interfaceDiv);
    } 
}

var interfacePositionMyMenu=-1;
function setInterfacePositionInMyMenu(){
     var mainMenu=myMenu;
    for(var i=0;i<mainMenu.length;i++){ 
        if(mainMenu[i][3]=="Interfaces"){          
            interfacePositionMyMenu=i;            
            break;
        }
    }

    var copyMenu=myMenuCopy;
    for(var i=0;i<copyMenu.length;i++){
        if(copyMenu[i][3]=="Interfaces"){                                    
        }
        else
        {
            copyMenu.splice(i,1);
            i--;
        }
    }
}

function removeInterfaceMenu(interfaceType){
    var mainMenu=myMenu;
    for(var j=5;j<mainMenu[interfacePositionMyMenu].length;j++){
        if(mainMenu[interfacePositionMyMenu][j][3]==interfaceType){
            myMenu[interfacePositionMyMenu].splice(j,1);
        }
    }
        
    var mainMenuOrig=myMenuOrig;
    for(j=5;j<mainMenuOrig[interfacePositionMyMenu].length;j++){
        if(mainMenuOrig[interfacePositionMyMenu][j][3]==interfaceType){
            myMenuOrig[interfacePositionMyMenu].splice(j,1);
        }
    }
        
    var mainMenuCopy=myMenuCopy;
    for(j=5;j<mainMenuCopy[0].length;j++){
        if(mainMenuCopy[0][j][3]==interfaceType){
            myMenuCopy[0].splice(j,1);
        }
    }
        
}

function addInterfaceMenu(interfaceType,interfaceDiv){
    var arrMenuInterface=new Array();
    if(interfaceType=="Exceptions"){
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_EXCEPTION;
        arrMenuInterface[2]="javascript:ExceptionClick()?openDivElement('exp','Exceptions','',''):'';";
        arrMenuInterface[3]="Exceptions";
        arrMenuInterface[4]="";
    }else if(interfaceType=="Document"){
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_DOCUMENT;
        arrMenuInterface[2]="javascript:DocumentClick()?openDivElement('doc','Document','',''):''";
        arrMenuInterface[3]="Document";
        arrMenuInterface[4]="";
    }else if(interfaceType=="FormView"){
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_FORMVIEW;
        arrMenuInterface[2]="javascript:FormViewClick()?openDivElement('form','FormView','',''):''";
        arrMenuInterface[3]="FormView";
        arrMenuInterface[4]="";
    }else if(interfaceType=="FormExtView"){//added for Variant : 22 Oct 2013
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_FORMEXTVIEW;
        arrMenuInterface[2]="javascript:FormViewClick()?openDivElement('form','FormView','',''):''";
        arrMenuInterface[3]="FormExtView";
        arrMenuInterface[4]="";//added for Variant : 22 Oct 2013 till here
    }else if(interfaceType=="ToDoList"){
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_TODOLIST;
        arrMenuInterface[2]="javascript:ToDoListClick()?openDivElement('todo','ToDoList','',''):''";
        arrMenuInterface[3]="ToDoList";
        arrMenuInterface[4]="";
    }else if(interfaceType=="DynamicCase"){
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_TASK;
        arrMenuInterface[2]="javascript:openDivElement('task','DynamicCase','','')";
        arrMenuInterface[3]="DynamicCase";
        arrMenuInterface[4]="";
    }else if(interfaceDiv=="SAP0"){//interfaceDiv: here interfaceDiv contains the sap def name
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_SAP0;
        arrMenuInterface[2]="javascript:SAPClick()?openDivElement('"+interfaceDiv+"','"+interfaceType+"','','SAP'):''";
        arrMenuInterface[3]=interfaceType;
        arrMenuInterface[4]="";
    }else if(interfaceDiv=="SAP1"){//interfaceDiv: here interfaceDiv contains the sap def name
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_SAP1;
        arrMenuInterface[2]="javascript:SAPClick()?openDivElement('"+interfaceDiv+"','"+interfaceType+"','','SAP'):''";
        arrMenuInterface[3]=interfaceType;
        arrMenuInterface[4]="";
    }else if(interfaceDiv=="SAP2"){//interfaceDiv: here interfaceDiv contains the sap def name
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_SAP2;
        arrMenuInterface[2]="javascript:SAPClick()?openDivElement('"+interfaceDiv+"','"+interfaceType+"','','SAP'):''";
        arrMenuInterface[3]=interfaceType;
        arrMenuInterface[4]="";
    }else if(interfaceDiv=="SAP3"){//interfaceDiv: here interfaceDiv contains the sap def name
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_SAP3;
        arrMenuInterface[2]="javascript:SAPClick()?openDivElement('"+interfaceDiv+"','"+interfaceType+"','','SAP'):''";
        arrMenuInterface[3]=interfaceType;
        arrMenuInterface[4]="";
    }else if(interfaceDiv=="SAP"){//interfaceDiv: here interfaceDiv contains the sap def name
        arrMenuInterface[0]="";
        arrMenuInterface[1]=TOOL_SAP;
        arrMenuInterface[2]="javascript:SAPClick()?openDivElement('"+interfaceDiv+"','"+interfaceType+"','','SAP'):''";
        arrMenuInterface[3]=interfaceType;
        arrMenuInterface[4]="";
    }else{
        var inpFrameSrc=document.getElementById(interfaceType+"Iframe");
        arrMenuInterface[0]="";
        arrMenuInterface[1]=interfaceType;
        arrMenuInterface[2]="javascript:openDivElement('"+interfaceType+"','"+interfaceType+"','"+inpFrameSrc.src+"','Ext')";
        arrMenuInterface[3]=interfaceType;
        arrMenuInterface[4]="";
    }
    addInterfaceToMainMenu(arrMenuInterface);
}

function addInterfaceToMainMenu(arrMenuInterface){
    if(interfacePositionMyMenu==-1){
        var arrInterface=new Array();
        interfacePositionMyMenu=0;
        arrInterface[0]="";
        arrInterface[1]="Interfaces";
        arrInterface[2]="";
        arrInterface[3]="Interfaces";
        arrInterface[4]="";
        myMenuOrig.splice(interfacePositionMyMenu,0,arrInterface);
    }
    if(myMenuCopy.length==0){
        arrInterface=new Array();
        arrInterface[0]="";
        arrInterface[1]="Interfaces";
        arrInterface[2]="";
        arrInterface[3]="Interfaces";
        arrInterface[4]="";
        myMenuCopy.splice(0,0,arrInterface);
    }

    myMenuOrig[interfacePositionMyMenu][myMenuOrig[interfacePositionMyMenu].length] = arrMenuInterface;
    myMenuCopy[0][myMenuCopy[0].length] = arrMenuInterface;
    myMenu=myMenuCopy;
    prepareWdeskDefaultMenu();
}


function setDefaultValues(tabDiv)
{
    var winName="";
    if(tabDiv == 'doc'){
        docLoaded=false;
        winName='tableGrid';
    }
    if(tabDiv == 'form'){
       formLoaded=false;
       winName='formGrid';
    }
    
    if(tabDiv == 'todo'){
       todoLoaded = false;
       winName='todoGrid';
    }
    if(tabDiv == 'exp'){
       expLoaded = false;
       winName='exceptionGrid';
    }
    if(tabDiv == 'SAP0'){
       sap0Loaded = false
    }
    if(tabDiv == 'SAP1'){
       sap1Loaded = false
    }
    if(tabDiv == 'SAP2'){
       sap2Loaded = false
    }
    if(tabDiv == 'SAP3'){
       sap3Loaded = false
    }
    if(tabDiv == 'SAP'){    //Bug 64845
       sapLoaded = false
    }
    removeFromArray(windowList,winName);
    

}

function getInterfaceKeyByType(interfaceType){
    var intArrayRef = interfaceMap.getIterator();      
    for(var i=0; i<intArrayRef.length; i++){        
        if(intArrayRef[i].value.Type == interfaceType){
            return intArrayRef[i].key;
        }        
    }    
    return null;
}
function getInterfaceKeyy(interfaceType){
    var intArrayRef = interfaceMap.getIterator();      
    for(var i=0; i<intArrayRef.length; i++){        
        if((intArrayRef[i].value.Type).indexOf("tcode") > -1){
            return intArrayRef[i].key;
        }        
    }    
    return null;
}

function getInterfaceKeyByStatus(interfaceDiv){
     var intArrayRef = interfaceMap.getIterator();
      for(var i=0; i<intArrayRef.length; i++){        
        if(intArrayRef[i].key == interfaceDiv){
            return intArrayRef[i].value.Status;
        }        
    }
     return null;
}
function createContainerDiv(containerDivId){
    var containerDiv = window.document.createElement("div");
    containerDiv.id = containerDivId;
    containerDiv.style.position = "absolute";
       
    var screenJson = getCenterCompPosition();
    containerDiv.style.left = screenJson.left + "px";
    containerDiv.style.top = screenJson.top + "px";
    containerDiv.style.width = WIDTH + "px";
    containerDiv.style.height = DEFAULT_DIV_HEIGHT+"px";
    containerDiv.style.backgroundColor="white";
    containerDiv.style.overflow="hidden";
    containerDiv.style.display="block";
    containerDiv.style.border= '0px solid green';
    containerDiv.className= 'interfaceShadow interfaceRadius';
    return containerDiv;
}

function setInterfaceStatus(DivId,status){
    var intArrayRef = interfaceMap.getIterator();
      for(var i=0; i<intArrayRef.length; i++){        
        if(intArrayRef[i].key == DivId){
            intArrayRef[i].value.Status=status;
        }        
    }
     return null;
}

function getInterfaceDivIdFromKey(key){
    var intArrayRef = interfaceMap.getIterator();
    for(var i=0; i<intArrayRef.length; i++){
        if(intArrayRef[i].key == key){
            return intArrayRef[i].value.DivId;
        }
    }
    return null;
}

function getInterfaceTypeFromKey(DivId){
    var intArrayRef = interfaceMap.getIterator();
    for(var i=0; i<intArrayRef.length; i++){
        if(intArrayRef[i].key == DivId){
            return intArrayRef[i].value.Type;
        }
    }
    return null;
}
//enable dragging if varEditLayout==1 otherwise disable dragging
var varEditLayout=1;
function editLayout(){    
    var intArrayRef = interfaceMap.getIterator();
    for(var i=0; i<intArrayRef.length; i++){
        var verDivScroll="divResizeVer_";
        var horDivScroll="divResizeHor_";
        var sliderDivScroll="divResize_";
        var closeimg="closeimg_";
        var divId=intArrayRef[i].key;
        var contId=divId.split("_");
        verDivScroll=verDivScroll+contId[1];
        horDivScroll=horDivScroll+contId[1];
        sliderDivScroll=sliderDivScroll+contId[1];
        closeimg=closeimg+contId[1];
       
        if(document.getElementById(verDivScroll)!=null || document.getElementById(verDivScroll)!=undefined){
            document.getElementById(verDivScroll).style.display="block";
        }
        if(document.getElementById(horDivScroll)!=null || document.getElementById(horDivScroll)!=undefined){
            document.getElementById(horDivScroll).style.display="block";
        }
        if(document.getElementById(sliderDivScroll)!=null || document.getElementById(sliderDivScroll)!=undefined){
            document.getElementById(sliderDivScroll).style.display="block";
        }
        if(document.getElementById(closeimg)!=null || document.getElementById(closeimg)!=undefined){
            document.getElementById(closeimg).style.display="block";
        }
       
    }
   
   disableEditLayoutOption();

   myMenu = myMenuCopy;
   prepareWdeskDefaultMenu();

   varEditLayout=0;
   
    if(isCollabServerConnected){
        disableShareOption();
    }   
}

function saveLayout(){
    // Save Interface position
    var intArrayRef = interfaceMap.getIterator();
    var ref = null;    
    
    if(intArrayRef.length <1  && (wDeskLayout.WDeskType == 'F')){
        for(var i=0; i<wDeskLayout.Interfaces.length; i++){
           if(wDeskLayout.Interfaces[i].Interface.Available == "true"){
                wDeskLayout.Interfaces[i].Interface.Status = "O";            
                interfaceMap.put("div_C"+(i+1), wDeskLayout.Interfaces[i].Interface);
           }
        }           
    }
    
    if(wDeskLayout.WDeskType == 'E'){
        for(var i=0; i<intArrayRef.length; i++){
            ref = document.getElementById(intArrayRef[i].key);

            if(intArrayRef[i].value.Status == "I"){
                intArrayRef[i].value.Status = "O";
            } 

            if(ref != null){
                setIntPosition(intArrayRef[i].value, ref);
            }
        }
    }
    
    saveWorkDeskLayout('P');//P for saving preferences only
    
    // updating interfaces edited positioning in FixedWDeskInfo
    if(wDeskLayout.WDeskType == 'F'){
        var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;        
        
        if(!(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces || FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces)){                            
            ref = document.getElementById("div_C1");
            FixedWDeskInfo.WDeskInterfaces.Left.Width = (ref.style.width.split('p')[0]-0);
            
            ref = document.getElementById("div_C"+(wDeskInterfaces.Left.Interfaces.length+1));
            FixedWDeskInfo.WDeskInterfaces.Right.Left = (ref.style.left.split('p')[0]-0);            
            FixedWDeskInfo.WDeskInterfaces.Right.Width = (ref.style.width.split('p')[0]-0);   
            
            ref = document.getElementById("resizer");
            FixedWDeskInfo.WDeskInterfaces.ResizerLeft = (ref.style.left.split('p')[0]-0);  

            ref = document.getElementById("innerResizer");
            FixedWDeskInfo.WDeskInterfaces.InnerResizerLeft = (ref.style.left.split('p')[0]-0);

            ref = document.getElementById('wdesk');
            ref.Left.value = FixedWDeskInfo.WDeskInterfaces.Left.Width;
        }
    }    
    
    saveWDLayoutInfo();
    myMenu = myMenuOrig;
    prepareWdeskDefaultMenu();    
    
    if(oldWDJason.WIViewMode == "R"){
        if(SharingMode){
            enableShareOption();
        }
    } else {        
        if(isCollabServerConnected){
            enableShareOption();        
        }
    }
    cancelSaveEditLayoutHook('S');
}

function restoreIntPosition(intJason, divRef){
    divRef.style.left = intJason.Left + "px";
    divRef.style.top = intJason.Top + "px";
    divRef.style.width = intJason.Width + "px";
    divRef.style.height = intJason.Height + "px";
}

function cancelSaveLayout(){
   // Restore interface position
   if(varEditLayout == 0){
       var intArrayRef = interfaceMap.getIterator();
       var ref = null;    
       for(var i=0; i<intArrayRef.length; i++){
           ref = document.getElementById(intArrayRef[i].key);

           if(intArrayRef[i].value.Status == "I"){
                intArrayRef[i].value.Status = "C";
                ref.style.display = "none";
                addInterfaceMenu(intArrayRef[i].value.Type,intArrayRef[i].value.DivId);
                try{                
                    document.getElementById("containerDiv").removeChild(ref);
                }catch(e){                
                //alert(e);
                }            
                setDefaultValues(intArrayRef[i].value.DivId);
           } else {
               if(ref != null){
                    restoreIntPosition(intArrayRef[i].value, ref);
                    ResetHWInterfaces(intArrayRef[i].key);    
               }
           }
       } 

       myMenu = myMenuOrig;
       prepareWdeskDefaultMenu();       
       saveWDLayoutInfo();

       if(oldWDJason.WIViewMode == "R"){
            if(SharingMode){
                enableShareOption();
            }
        } else {        
            if(isCollabServerConnected){
                enableShareOption();        
            }
        } 
        cancelSaveEditLayoutHook('X');
   }
}

function saveWDLayoutInfo(){
    var intArrayRef = interfaceMap.getIterator();
    for(var i=0; i<intArrayRef.length; i++){
        var verDivScroll="divResizeVer_";
        var horDivScroll="divResizeHor_";
        var sliderDivScroll="divResize_";
        var closeimg="closeimg_";
        var divId=intArrayRef[i].key;
        var contId=divId.split("_");
        verDivScroll=verDivScroll+contId[1];
        horDivScroll=horDivScroll+contId[1];
        sliderDivScroll=sliderDivScroll+contId[1];
        closeimg=closeimg+contId[1];
        if(document.getElementById(verDivScroll)!=null || document.getElementById(verDivScroll)!=undefined){
            document.getElementById(verDivScroll).style.display="none";
        }
        if(document.getElementById(horDivScroll)!=null || document.getElementById(horDivScroll)!=undefined){
            document.getElementById(horDivScroll).style.display="none";
        }
        if(document.getElementById(sliderDivScroll)!=null || document.getElementById(sliderDivScroll)!=undefined){
            document.getElementById(sliderDivScroll).style.display="none";
        }
        if(document.getElementById(closeimg)!=null || document.getElementById(closeimg)!=undefined){
            document.getElementById(closeimg).style.display="none";
        }    
        
        var headerIdRef = document.getElementById("PGHeader_"+contId[1]);       
        if(headerIdRef!=null && headerIdRef!=undefined){
            headerIdRef.className = "cursorSimple";
        }
       
    }
    
    enableEditLayoutOption();
    if(wDeskLayout.WDeskType == 'F'){
        clearFixedLayoutSlider();
    }
    
    varEditLayout=1;
}

function openInterface(interfaceName,interfaceType,url,ext,event){
    event = (typeof event == 'undefined')? null: event;
    if(interfaceType=="Exceptions"){
        openExceptionWin('N',event);
    }else if(interfaceType=="Document"){
        openDocumentWin('N',event);
    }else if(interfaceType=="FormView"){
        openForm('N',event);
    }else if(interfaceType=="FormExtView"){//added for Variant : 22 Oct 2013
        openForm('N',event);
    }else if(interfaceType=="ToDoList"){
         openToDOWin('N',event);
    }else if(interfaceName=="SAP0"){
        eval("openSAPWin('"+interfaceType+"')");
    }else if(interfaceName=="SAP1"){
        eval("openSAPWin('"+interfaceType+"')");
    }else if(interfaceName=="SAP2"){
        eval("openSAPWin('"+interfaceType+"')");
    }else if(interfaceName=="SAP3"){
        eval("openSAPWin('"+interfaceType+"')");
    }else if(interfaceName=="SAP"){
        eval("openSAPWin('"+interfaceType+"')");
    }else if(interfaceType=="DynamicCase"){// Added for Task
        openTaskWin('N',event);
    }else{
        eval("OpenCustomUrl('"+url+"',"+"'"+interfaceType+"')");
    }
}

function createIFrameExt(url,type,parentDiv){
    var iframe=document.createElement("IFRAME");
    iframe.allowtransparency="true";
    iframe.setAttribute("src",url + '&Comp_height='+parseInt(parentDiv.style.height.split('p')[0]) + "&Comp_width=" + parseInt(parentDiv.style.width.split('p')[0]));
    iframe.setAttribute("height",parentDiv.style.height);
    iframe.setAttribute("width",parentDiv.style.width);
    iframe.setAttribute("top",parentDiv.style.top);
    iframe.setAttribute("left",parentDiv.style.left);
    iframe.style.visibility="visible";
    iframe.style.zIndex="1001";
    iframe.setAttribute("name", type+"Iframe");
    iframe.setAttribute("id", type+"Iframe");
    return iframe;
}

function showBlankIframe(ref)
{
    var IfrRef = document.getElementById('DivShim');
    IfrRef.style.width = parseInt(ref.style.width.split('p')[0]) - 1 + "px";
    IfrRef.style.height = parseInt(ref.style.height.split('p')[0]) - 1 + "px";
    IfrRef.style.top =  parseInt(ref.style.top.split('p')[0]) + 1 + "px";
    IfrRef.style.left = parseInt(ref.style.left.split('p')[0]) + 1 + "px";
    IfrRef.style.zIndex = "100";
    
    var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1; 
    if(isSafari){
        IfrRef.setAttribute("src", "javascript:false;");
    }
    
    IfrRef.style.display = "block";
}

function hideBlankIframe(ref)
{  
    var IfrRef = document.getElementById('DivShim');
    IfrRef.style.display = "none";
}
function openOmniScanDocUrl(event) {
    event = (typeof event == 'undefined')? null: event;
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    var uploadLimit = getUploadMaxLength(strprocessname, stractivityName, '');
    if(typeof obj == 'undefined')
    {
        if(window.opener.isFormLoaded==false)
            return;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        if(typeof isFormLoaded == 'undefined'){
            if(typeof window.opener != 'undefined'){
                if(window.opener.isFormLoaded==false)
                return;
            }
        } else {
            if(isFormLoaded==false)
            return;
        }
        
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }    
    var returnval = isDocTypeAddable(pid,wid,'',taskid) ;
    if(returnval == false || returnval == 'false')
        return false;
    if(typeof obj == 'undefined')
        var url = sContextPath+'/components/workitem/document/scan/omniscandoc.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&winloc=T'+'&UploadLimit='+uploadLimit +"&WD_UID="+ WD_UID;
    else
        var url = sContextPath+'/components/workitem/document/scan/omniscandoc.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&UploadLimit='+uploadLimit +"&WD_UID="+ WD_UID;
    url = appendUrlSession(url);
    
    var left = (window.screen.width - windowW)/2;
    var top = (document.documentElement.clientHeight - 348)/2;
    var win = link_popup(url,'omniscandoc','resizable=no,width='+windowW+',height=348,left='+left+',top='+top+',status=yes,resize=yes',windowList,false);
    cancelBubble(event);
}

function openGoogleImportDocWin() {
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    var uploadLimit = getUploadMaxLength(strprocessname, stractivityName,'');
    if(typeof obj == 'undefined')
    {
        if(window.opener.isFormLoaded==false)
            return;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        if(typeof isFormLoaded == 'undefined'){
            if(typeof window.opener != 'undefined'){
                if(window.opener.isFormLoaded==false)
                return;
            }
        } else {
            if(isFormLoaded==false)
            return;
        }
        
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }    
    var returnval = isDocTypeAddable(pid,wid,'',taskid) ;
    if(returnval == false || returnval == 'false')
        return false;
    if(typeof obj == 'undefined')
        var url = sContextPath+'/components/workitem/document/scan/googleimportdoc.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&winloc=T'+'&UploadLimit='+uploadLimit +"&WD_UID="+ WD_UID;
    else
        var url = sContextPath+'/components/workitem/document/scan/googleimportdoc.jsf?wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&UploadLimit='+uploadLimit +"&WD_UID="+ WD_UID;
    url = appendUrlSession(url);
    
    var left = (window.screen.width - windowW)/2;
    var top = (document.documentElement.clientHeight - 348)/2;
    var win = link_popup(url,'omniscandoc','resizable=no,width='+windowW+',height=348,left='+left+',top='+top+',status=yes,resize=yes',windowList,false);
    
}

function openOmniScanUrl()
{
    var pid = document.getElementById('importForm:pid');
    var wid = document.getElementById('importForm:wid');
    var taskid = document.getElementById('importForm:taskid');
    var obj = window.top.opener.document.forms['wdesk'];
   
    var docValue;
     var objCombo1 = document.getElementById('importForm:docListName');
     var docDisplayName="";
    if(objCombo1.options.length!=0){
        docValue=objCombo1.options[objCombo1.options.selectedIndex].value;
        docDisplayName= objCombo1.options[objCombo1.options.selectedIndex].text;
        document.getElementById('importForm:documentId').value=docValue;
        document.getElementById('importForm:hiddocDisplayName').value = docDisplayName;
       }
    if(typeof obj == 'undefined')
    {
        wid.value = window.top.opener.top.opener.document.getElementById('wdesk:wid').value;
        pid.value = window.top.opener.top.opener.document.getElementById('wdesk:pid').value;
        taskid.value = window.top.opener.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        wid.value = window.top.opener.document.getElementById('wdesk:wid').value;
        pid.value = window.top.opener.document.getElementById('wdesk:pid').value;
        taskid.value = window.top.opener.document.getElementById('wdesk:taskid').value;
    }

    var docTypeObj = document.getElementById('importForm:docListMenu');
    if(!docTypeObj){
        docTypeObj = document.getElementById('importForm:labelDoct1');
    }

    var docTypeName = docTypeObj.value;

    var WindowHeight=500;
    var WindowWidth=600;
    var WindowLeft=screen.availWidth/2-250;
    var WindowTop=screen.availHeight/2-250;

    var wFeatures = 'scrollbars=no,width='+WindowWidth+',height='+WindowHeight+',left='+WindowLeft+',top='+WindowTop+',status=yes';
    var addMode = document.getElementById("importForm:mode").value;
    if(typeof addMode == 'undefined' || addMode == ''){
        addMode="new";
    }
    var listParam=new Array();
    
    listParam.push(new Array("pid",encode_utf8(pid.value)));
    listParam.push(new Array("wid",encode_ParamValue(wid.value)));
    listParam.push(new Array("taskid",encode_ParamValue(taskid.value)));
    listParam.push(new Array("DocType",docTypeName));
    listParam.push(new Array("DocIndex",encode_ParamValue(docValue)));
    listParam.push(new Array("Mode",encode_ParamValue(addMode)));
    //listParam.push(new Array("rid",encode_ParamValue(MakeUniqueNumber())));

    var url = "redirectomniscanweburl.jsf"
    openNewWindow(url, "OmniScan", wFeatures, true, '', '', '', '', listParam);
    //window.close();
}

function ReloadDocInterface(broadCastFlag, eventType,docIndex)
{
    eventType = (typeof eventType == 'undefined')? '': eventType;
    docIndex = (typeof docIndex == 'undefined')? '': docIndex;
    var bBroadCastEvent = true;
    if(broadCastFlag != undefined && broadCastFlag == false)
        bBroadCastEvent = false;
    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;
    var docWindow=getWindowHandler(winList,"tableGrid");
    
    if(docWindow)
    {
        if(docWindow.windowProperty.winloc=="M")
        {
            //sendAjaxRequest('doc', "doc_main_view.jsp");
            sendAsynAjaxRequest('doc', "doc_main_view.jsf", '', true, true, eventType,docIndex);
            //InitHWDoc();
        }
        else
        {
            //docWindow.sendAjaxRequest('doc', "doc_main_view.jsp");
            docWindow.sendAsynAjaxRequest('doc', "doc_main_view.jsf", '', true, true, eventType,docIndex);
            //docWindow.InitHWDoc();
        }
    }
    if(window_workdesk.SharingMode && bBroadCastEvent)
    {
        window_workdesk.broadcastScanDocEvent();
    }
}

function launchDocInOD(){
    var objCombo = document.getElementById('wdesk:docCombo');
    var docIndex = objCombo.value;
    var docName = objCombo[objCombo.selectedIndex].text;
    var pid=document.getElementById('wdesk:pid').value;
    var wid=document.getElementById('wdesk:wid').value;
    var taskid=document.getElementById('wdesk:taskid').value;
    
    var windowtopX = (window.screen.availHeight - (window1H+60))/2;
    var windowleftY = (window.screen.availWidth - (window1W+230))/2;
    
    docProperty(docIndex,pid,wid,taskid);  
    var url = sContextPath+"/components/searchdms/opendocinod.jsf";
    url = appendUrlSession(url);    
    
    var left = (window.screen.width - windowW)/2;
    var top = (document.documentElement.clientHeight - 348)/2;
    var wFeatures = 'scrollbars=yes,resizable=yes,width='+windowW+',height=348,left='+left+',top='+top;
 
    var listParam=new Array();
    listParam.push(new Array("Action","1"));
    listParam.push(new Array("rid",encode_ParamValue(MakeUniqueNumber())));
    listParam.push(new Array("WD_UID",encode_ParamValue(WD_UID)));
    listParam.push(new Array("DocName",encode_ParamValue(docName)));
    listParam.push(new Array("docExt",encode_ParamValue(DocExt)));
    listParam.push(new Array("DocId",encode_ParamValue(docIndex)));
    listParam.push(new Array("ProcessInstanceID",encode_ParamValue(pid)));
    listParam.push(new Array("WorkitemID",encode_ParamValue(wid)));
    listParam.push(new Array("taskid",encode_ParamValue(taskid)));
    listParam.push(new Array("Option","ExtendCustomSession"));
    var win = openNewWindow(url,'launchodoc',wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);
}
var checkNgFormRefreshStatus=true;
var checkTaskNgFormRefreshStatus=true;
function checkRequestStatus(data){
    checkNgFormRefreshStatus=true;
    if(data.status=='success'){
        if(formIntegrationApr != "4"){
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
                    return false;
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
                    return false;
                }
            }
        }
        
        var ajaxRef = {};
        var windowRef = window.parent;
        
        if(formIntegrationApr == "4"){
            reqOption = "";
            windowRef = window;
            ajaxRef.IsValidateForm = true;                
        } else {
            ajaxRef = new net.ContentLoader('/webdesktop/ajaxSaveNGFormHandler.jsf',saveNGFormHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encodeURIComponent(pid)+'&opt=S',false);        
        }
        
        if(typeof ajaxRef.IsValidateForm != 'undefined'){
            if(!ajaxRef.IsValidateForm){    
                windowRef.hideProcessing();                 
                checkNgFormRefreshStatus=false;
                return;
            }
            if(reqOption == 'broadCastForm'){
                reqOption = '';
                var returnVal=windowRef.for_save("dummysave","broadCastForm");
                windowRef.broadcastNGHTMLForm();
            }
            else if(reqOption == "broadCastUnshareLock"){
                reqOption = '';
                windowRef.broadcastLock();
                windowRef.renderNavigation(windowRef.oldWDJason.WICount);
                windowRef.disableChat();
                windowRef.closeChat();
                var roomId = (windowRef.pid+"_"+windowRef.wid);  
                windowRef.opener.parent.leaveRoom(windowRef.userIndex,roomId);
            }
            else if(reqOption == "broadCastLock"){
                reqOption = '';
                windowRef.broadcastLock();
            }
            else{
                var returnVal=windowRef.for_save("mainSave",callfromVar); 
                
                if(WiDummySaveStruct.Type == "Nav"){
                    windowRef.prevNextHandlerMain(WiDummySaveStruct.SubType);                    
                } else {
                    if(windowRef.bNgFormRefresh){
                        if(windowRef.wiproperty.IsTask == 'N')// Added to block unlock for task
                            windowRef.sendUnlockReq();
                        windowRef.ngRefreshCallback();
                        if(returnVal=='success'){        
                            if(typeof windowRef.RefreshClientComp != 'undefined'){
                                windowRef.RefreshClientComp();
                            }
                        }
                    }
                    refreshTaskList();
                    if(WiDummySaveStruct.Type == "menuWinClose"){
                        if(windowRef){
                            //Bug ID 60314
                            windowRef.sendUnlockReq();
                            windowRef.top.close();
                        }
                    }

                    if( window.afterSave){
                        window.afterSave();
                    }
                }
            }
        }
    }
}

function checkTaskRequestStatus(data){
    checkTaskNgFormRefreshStatus=true;
    if(data.status=='success'){      
        var ajaxRef = new net.ContentLoader('/webdesktop/ajaxSaveTaskNGFormHandler.jsf',saveTaskNGFormHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&opt=S',false);        
        if(typeof ajaxRef.IsValidateForm != 'undefined'){
            if(!ajaxRef.IsValidateForm){    
                window.parent.hideProcessing();                 
                checkTaskNgFormRefreshStatus=false;
                return;
            }
            if(reqOption == 'broadCastForm'){
                reqOption = '';
                var returnVal=window.parent.for_save("dummysave","broadCastForm");
                window.parent.broadcastNGHTMLForm();
            }
            else if(reqOption == "broadCastUnshareLock"){
                reqOption = '';
                window.parent.broadcastLock();
                window.parent.renderNavigation(window.parent.oldWDJason.WICount);
                window.parent.disableChat();
                window.parent.closeChat();
                var roomId = (window.parent.pid+"_"+window.parent.wid);  
                window.parent.opener.parent.leaveRoom(window.parent.userIndex,roomId);
            }
            else if(reqOption == "broadCastLock"){
                reqOption = '';
                window.parent.broadcastLock();
            }
            else{
                var returnVal=window.parent.for_save("mainSave",callfromVar); 
                
                if(window.parent.bNgFormRefresh){
                    if(window.parent.wiproperty.IsTask == 'N')// Added to block unlock for task
                        window.parent.sendUnlockReq();
                    window.parent.ngRefreshCallback();
                    if(returnVal=='success'){        
                        if(typeof window.parent.RefreshClientComp != 'undefined'){
                            window.parent.RefreshClientComp();
                        }
                    }
                }
                refreshTaskList();
                if(WiDummySaveStruct.Type == "menuWinClose"){
                    if(window.parent){
                        window.parent.top.close();
                    }
                }
            } 
        }
    }
}
function checkInCollaborationHandler(checkInDocJSON){
    var objCombo = document.getElementById('wdesk:docCombo');
    if(DocOrgName == 'Y')
        objCombo[objCombo.selectedIndex].text = checkInDocJSON.DocName + "(" + checkInDocJSON.DiskName + ")";
    else
        objCombo[objCombo.selectedIndex].text = checkInDocJSON.DocName;
    objCombo[objCombo.selectedIndex].value = checkInDocJSON.DocIndex;
    reloadapplet(checkInDocJSON.DocIndex,false);
}
function showHideFormLink(){
    var wdeskWindow=getLocation();
    var winList;
    if(wdeskWindow){
        winList=wdeskWindow.windowList;
    if(winList)
    var formWindow=getWindowHandler(winList,"formGrid");
    if(formWindow){
        var ref = formWindow.document.getElementById("formBroadCast");
        if(ref != null){
            if(SharingMode && wiproperty.formType=="NGFORM" && oldWDJason.WIViewMode != 'R'){        
                ref.style.display="inline";
            }
            else{
                ref.style.display="none";
            }
        }
    }
    }
}
function validateXssTexts(inputTxt)
{
    if(UplCheck.toLowerCase() == "y" && ValidateXssInUpload.toLowerCase() == "y")
    {
        if(inputTxt.indexOf("javascript:") > -1 || inputTxt.indexOf("eval(") > -1 ||  inputTxt.indexOf("alert(") > -1  ||  inputTxt.indexOf("</script") > -1 ||  inputTxt.indexOf("<script") > -1)
            return false;
        else
            return true;
    }
    else
        return true;
}
function openPickList(ref,variableName,elementId){//added for Variant : 17 Oct 13
    var posLeft = findAbsPosX(ref);
    var posTop = findAbsPosY(ref);
    try{
        posTop  -= document.getElementById("scroll").scrollTop;
        posLeft -= document.getElementById("scroll").scrollLeft;
    }catch(e){}
    var iFrameHeight = 245;
    var iFrameWidth = 250;
    var popupDivId = "VariantFormPickList";
    var url="/webdesktop/components/workitem/list/variantFieldDataPicklist.jsf?Action=1&"+encode_utf8("pid")+"="+encode_utf8(pid)+"&"+encode_utf8("wid")+"="+encode_utf8(wid)+"&"+encode_utf8("taskid")+"="+encode_utf8(taskid)+"&PopupDivId="+popupDivId+"&VariableName="+variableName+"&ElementId="+elementId+'&WD_UID='+WD_UID;
    ;
    window.parent.parent.popupIFrameOpener(this, popupDivId, url, iFrameWidth, iFrameHeight, posLeft, posTop, false, true, false, true);
}
function validateInteger(e,textBoxElem){    //added for Variant
    var evtObj = window.event || e;
    var charCode = (evtObj.which) ? evtObj.which : evtObj.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        evtObj.returnValue = false;
    else
        evtObj.returnValue = true;
    
    if(!evtObj.returnValue){
        if(e.stopPropogation)
            e.stopPropogation(true);
        if(e.preventDefault)
            e.preventDefault(true);
        e.cancelBubble = true;
    }
    return evtObj.returnValue;
}
function validateFloat(e,textBoxElem){//added for Variant
    var evtObj = window.event || e;
    var KeyID = evtObj.keyCode || evtObj.which;
    if(((KeyID == 45 || (KeyID >=48 && KeyID <58)))||(KeyID==8)||(KeyID==16)||(KeyID==46) || (KeyID==190) || (KeyID==110)
                    ||(KeyID==37)||(KeyID==39)||(KeyID==36)||(evtObj.ctrlKey==true && (KeyID == 118 || KeyID == 86||KeyID == 120||KeyID == 88)) || (KeyID >=96 && KeyID <106)){
        evtObj.returnValue = true;
    }
    else{
        evtObj.returnValue = false;
    }
    if(!evtObj.returnValue){
        if(e.stopPropogation)
            e.stopPropogation(true);
        if(e.preventDefault)
            e.preventDefault(true);
        e.cancelBubble = true;
    }
    return evtObj.returnValue;
}
//added for Variant : 22 Oct 2013
function executeOnSaveVariant(){
    if(funcVariantOnSave != undefined && funcVariantOnSave.length > 0) {
        var callFuncVariantOnSave = funcVariantOnSave+"()";
        eval(callFuncVariantOnSave);
    }
    return false;
}


function checkIntroduceRequestStatus(data){
    checkNgFormRefreshStatus=true;
	
    if(data.status=='success'){
	if(formIntegrationApr != "4"){	
            var validated=document.getElementById("bValidated").value;           
            if(validated=="false")
            { 
                var evntHdlr="";
                var excpHandlerJson;
				window.parent.hideProcessing(); //Bug 79847 
            	window.parent.hideMasking();
                if(document.getElementById("excpJson").value!=null && document.getElementById("excpJson").value!="")
                {
                    excpHandlerJson = JSON.parse(document.getElementById("excpJson").value);
                    evntHdlr = excpHandlerJson.EventHandler; 
                    return false;
                }
                if( evntHdlr != "" )
                {
                    window.parent.hideProcessing();
                    handleExcp(document.getElementById("excpJson").value);
                    return false;
                }
                else
                {
                    window.parent.hideProcessing();
                    var errMsg=document.getElementById("txtErrorMessage").value;
                    //Bugzilla – Bug 54594
                    var res = errMsg.split("~");
                        bFormValidated=false;
                    if(res.length==2)
                        com.newgen.omniforms.util.showError(res[1],res[0]); 
                    else
                        com.newgen.omniforms.util.showError(document.getElementById("ngform"),errMsg); 
                    return false;
                }
            }
        }
	
        var ajaxRef = {};
        var windowRef = window.parent;
        
        if(formIntegrationApr == "4"){
            windowRef = window;
            ajaxRef.IsValidateForm = true;                
        } else {
            ajaxRef = new net.ContentLoader('/webdesktop/ajaxSaveNGFormHandler.jsf',saveNGFormHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encodeURIComponent(pid)+'&opt=I',false);        
        }
        
        var ret=windowRef.saveReturnOption;	        
        
        if(typeof ajaxRef.IsValidateForm != 'undefined'){
            if(!ajaxRef.IsValidateForm){ 
                windowRef.hideProcessing(); 
                checkNgFormRefreshStatus=false;
                return;
            }
            if(ret == '1'||ret =='2'){
                if(!windowRef.IntroduceClick()){
                    windowRef.hideProcessing();
                    return false;
                }
                var dataFrm=windowRef.document.getElementById('wdesk');
                windowRef.unlockflag="N";
                if (ret == '1'){
                    dataFrm.CloseWindow.value='false';
                    dataFrm.isEmbeddded.value=windowRef.isEmbd;
                }
                else if ( ret == '2'){
                    dataFrm.CloseWindow.value='true';//close window
                    dataFrm.isEmbeddded.value=windowRef.isEmbd;
                }
                var ngParam=windowRef.getNGParam();
                windowRef.hideWIFromList(windowRef.sourceInsId);
                windowRef.clickWiLink("INTRODUCE","wdesk:controller",ngParam);
            }
			
        }
    }
}

function checkDoneRequestStatus(data){
    checkNgFormRefreshStatus=true;
    if(data.status=='success'){
	if(formIntegrationApr != "4"){	
            var validated=document.getElementById("bValidated").value;           
            if(validated=="false")
            { 
                var evntHdlr="";
                var excpHandlerJson;
				window.parent.hideProcessing();
            	window.parent.hideMasking();
                if(document.getElementById("excpJson").value != undefined && document.getElementById("excpJson").value != "undefined"  && document.getElementById("excpJson").value!=null && document.getElementById("excpJson").value!="")
                {
                    excpHandlerJson = JSON.parse(document.getElementById("excpJson").value);
                    evntHdlr = excpHandlerJson.EventHandler; 
                }
                if( evntHdlr != "" )
                {
                    window.parent.hideProcessing();
                    handleExcp(document.getElementById("excpJson").value);
                    return false;
                } else
                {
                    window.parent.hideProcessing();
                    var errMsg=document.getElementById("txtErrorMessage").value;
                    //Bugzilla – Bug 54594
                    var res = errMsg.split("~");
                        bFormValidated=false;
                    if(res.length==2)
                        com.newgen.omniforms.util.showError(res[1],res[0]); 
                    else
                        com.newgen.omniforms.util.showError(document.getElementById("ngform"),errMsg); 
                    return false;
                }
            }
        }
		
        var ajaxRef = {};
        var windowRef = window.parent;
        
        if(formIntegrationApr == "4"){
            windowRef = window;
            ajaxRef.IsValidateForm = true;                
        } else {
            ajaxRef = new net.ContentLoader('/webdesktop/ajaxSaveNGFormHandler.jsf',saveNGFormHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&opt=D',false);        
        }

        var ret=windowRef.saveReturnOption;
        
        if(typeof ajaxRef.IsValidateForm != 'undefined'){
            if(!ajaxRef.IsValidateForm){                
                windowRef.hideProcessing(); 
                checkNgFormRefreshStatus=false;
                return;
            }
        
            //Handling for task ng form
            var taskWindow = windowRef.getWindowHandler(windowRef.windowList, "taskGrid");   /// STUCK HERE--> Start with js debugging on done() function, windowhandler funtion should be picked from parent
            if (typeof taskWindow != 'undefined' && windowRef.wiproperty.TaskFormType == "NGFORM") {
                if ((windowRef.taskngformproperty.type == "applet") && !windowRef.bDefaultNGForm) {
                    try {
                        var taskngformIframe = windowRef.document.getElementById("taskngformIframe");
                        taskngformIframe.contentWindow.clickLink('cmdTaskNgFormDoneRefresh');
                    } catch (e) {

                    }
                } else {
                    var retVal = document.wdgc.SaveData('D');
                    if (retVal != 1) {
                        hideProcessing();
                        return 'false';
                    }
                }
            }
            
            if(!windowRef.DoneClick()){
                windowRef.hideProcessing();
                return false;
            }
            var dataFrm=windowRef.document.getElementById('wdesk');
            windowRef.unlockflag="N";
            if (ret == '1'){
                dataFrm.CloseWindow.value='false';
                dataFrm.isEmbeddded.value=windowRef.isEmbd;
            }
            else if ( ret == '2'){
                dataFrm.CloseWindow.value='true';
                dataFrm.isEmbeddded.value=windowRef.isEmbd;
            }
            var ngParam=windowRef.getNGParam();
            windowRef.hideWIFromList(windowRef.sourceInsId);
            windowRef.clickWiLink("DONE","wdesk:controller",ngParam);
        }
    }
}

function checkTaskDoneRequestStatus(data){
    checkTaskNgFormRefreshStatus=true;
    if(data.status=='success'){
        var ret=window.parent.saveReturnOption;
        var ajaxRef = new net.ContentLoader('/webdesktop/ajaxSaveTaskNGFormHandler.jsf',saveTaskNGFormHandler,null,"POST",'wid='+wid+'&taskid='+taskid+'&pid='+encode_utf8(pid)+'&opt=D',false);        
        if(typeof ajaxRef.IsValidateForm != 'undefined'){
            if(!ajaxRef.IsValidateForm){                
                window.parent.hideProcessing(); 
                checkTaskNgFormRefreshStatus=false;
                return;
            }
            if(!window.parent.DoneClick()){
                window.parent.hideProcessing();
                return false;
            }
            var dataFrm=window.parent.document.getElementById('wdesk');
            window.parent.unlockflag="N";
            if (ret == '1'){
                dataFrm.CloseWindow.value='false';
                dataFrm.isEmbeddded.value=window.parent.isEmbd;
            }
            else if ( ret == '2'){
                dataFrm.CloseWindow.value='true';
                dataFrm.isEmbeddded.value=window.parent.isEmbd;
            }
            window.parent.hideWIFromList(window.parent.sourceInsId);
            window.parent.clickWiLink("DONE","wdesk:controller","");
        }
    }
}

function radioChangeCrop(e){

    var value1;
    if(!isIE())
        value1=e.target.value;
    else
        value1=e.srcElement.value;
    //for omniscan integration
    try{
        document.getElementById('cropForm:mode').value = value1;
    }
    catch(e){}
    //end for omniscan integration
    if(value1=='new'){        
        document.getElementById('cropForm:docPanel').style.display="none";        
    }
    else{
        var objCombo=document.getElementById('cropForm:docListMenu');
        var docType=objCombo.options[objCombo.options.selectedIndex].text;
        if(docList==''){
            var pid = document.getElementById('cropForm:pid');
            var wid = document.getElementById('cropForm:wid');
            var taskid = document.getElementById('cropForm:taskid');
            var objCombo=document.getElementById('cropForm:docListMenu');
            var docType=objCombo.options[objCombo.options.selectedIndex].text;
            var requestString='docType='+encode_utf8(docType)+'&pid='+encode_utf8(pid.value)+'&wid='+wid.value+'&taskid='+taskid.value;
            var ajaxReq;
            if (window.XMLHttpRequest) {
                ajaxReq= new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
            }
            var url = "../ajaxdocList.jsf";
            url = appendUrlSession(url);
            ajaxReq.open("POST", url, false);
            ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            ajaxReq.send(requestString);
            if (ajaxReq.status == 200 && ajaxReq.readyState == 4) {
                docList=ajaxReq.responseText;
                docList=eval("("+docList+")");
            }
            else
            {
                if(ajaxReq.status == 400)
                    alert(INVALID_REQUEST_ERROR);
                else if(ajaxReq.status==12029){
                    alert(ERROR_SERVER);
                    //window.open(sContextPath+"/faces/login/logout.jsp?error=4020",reqSession);
                }
                else
                    alert(ERROR_DATA);
            }
        }

        var objCombo = document.getElementById('cropForm:docListName');
        objCombo.options.length = 0;
        var dName;
        for(var count=0;count<docList.combo.length;count++){
            dName=docList.combo[count].DocName;
            var strDocType=docType+"(";
            if(dName==docType || dName.indexOf(strDocType)==0){
                var displayName = docList.combo[count].DocName;
                if(DocOrgName == "Y")
                    displayName = displayName + "("+ docList.combo[count].DiskName +")";

                var option = new Option(displayName,docList.combo[count].DocIndex);

                objCombo.options.add(option,objCombo.options.length);
            }
        }
        document.getElementById('cropForm:docPanel').style.display="";
    }
}

function orderElement(tableId){    
    var objTable = document.getElementById(tableId);        
    var element = null;
    for(var i=1; i<objTable.rows.length; i++){
        element = objTable.rows[i].cells[0].firstChild;
        element.id = element.name = tableId+"-"+(i-1);        
    }
}

function adjustEmbMenu(isEmb){    
    if(typeof isEmb == 'undefined'){
        isEmb = 'N';
    }
    
    if((isEmb == 'Y') && (wdView == "em")){
        document.getElementById("wdesk:headerPG").style.width = "100%"; //document.getElementById("contentDiv").clientWidth +"px";
    }
}
function checkExpTempStatus(){
      var excpWindow=getWindowHandler(windowList,"exceptionGrid");
         if(typeof excpWindow !='undefined'){
            if(excpWindow.tempExcpTrig.length > 0){
                return false;
            }else
                return true;
       }
        else
          return true;
}

function ChangeCropRdbSel(){
    document.getElementById('cropForm:docPanel').style.display="none";
    if(isIE())
        document.getElementsByName('cropForm:addMode')[0].checked=true;
    else
        document.getElementsByName('cropForm:addMode')[0].checked=true;
    var docListVal = document.getElementById('cropForm:docListMenu').value;
    var window_workdesk="";
    if(windowProperty.winloc=="T")
        window_workdesk=window.opener.opener;
    else
        window_workdesk=window.opener;
    var winList=window_workdesk.windowList;
    var docWindow=getWindowHandler(winList,"tableGrid");
    var pid , wid,taskid;
    var obj = window.top.opener.document.forms['wdesk'];
    if(typeof obj == 'undefined')
    {
        wid = window.top.opener.top.opener.document.getElementById('wdesk:wid').value;
        pid = window.top.opener.top.opener.document.getElementById('wdesk:pid').value;
        taskid = window.top.opener.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    var returnval = isDocTypeAddable(pid,wid,docListVal,taskid);
    
    if(returnval == true   )
    {
        if(newVersion=='Y' && otherdms=='N')
            document.forms['cropForm']['cropForm:addMode'][1].disabled=false;
        if(overWrite=='Y' && otherdms=='N')
            document.forms['cropForm']['cropForm:addMode'][2].disabled=false;
    }
    else
    {
        if(newVersion=='Y' && otherdms=='N')
            document.forms['cropForm']['cropForm:addMode'][1].disabled=true;
        if(overWrite=='Y' && otherdms=='N')
            document.forms['cropForm']['cropForm:addMode'][2].disabled=true;
    }

}
function customCopyDoc(copyXML){
    var pid=document.getElementById('wdesk:pid').value;
    var wid=document.getElementById('wdesk:wid').value;
    var taskid=document.getElementById('wdesk:taskid').value;
    var url=sContextPath+'/webdesktop/ajaxCustomCopyDoc.jsf';
    url = appendUrlSession(url);
    var requestString='pid='+encode_utf8(pid)+'&wid='+wid+'&taskid='+taskid+'&copyXML='+copyXML;
    var docLoader = new net.ContentLoader(url,docHandler,null,"POST",requestString);
}

function displayDocTypeOnly(docIndex)
{
   var objCombo = window.document.getElementById('wdesk:docCombo');
   if(typeof objCombo == 'undefined' || objCombo == null)
    return false;
   for(var i=0;i<objCombo.options.length;i++)
    {
        var tmpLbl= objCombo.options[i].text;
        if(tmpLbl.lastIndexOf("(") != "-1")
         {
        var docType = tmpLbl.substring(0,tmpLbl.lastIndexOf("("))
        if(docIndex == undefined || (docIndex==objCombo.options[i].value))
        objCombo.options[i].text = docType;
         }
    }
}

function openDocListPopup(operation){

    var opt="Y";
    if(typeof operation!='undefined' && operation=="N" )
        opt="N";
    var obj = document.forms['wdesk'];
    var pid,wid,taskid;
    if(typeof obj == 'undefined')
    {
        if(typeof  window.opener.isFormLoaded != 'undefined' && window.opener.isFormLoaded==false)
            return;
        pid = window.top.opener.document.getElementById('wdesk:pid').value;
        wid = window.top.opener.document.getElementById('wdesk:wid').value;
        taskid = window.top.opener.document.getElementById('wdesk:taskid').value;
    }
    else
    {
        if(typeof isFormLoaded != 'undefined' && isFormLoaded==false)
            return;
        pid = document.getElementById('wdesk:pid').value;
        wid = document.getElementById('wdesk:wid').value;
        taskid = document.getElementById('wdesk:taskid').value;
    }

    if(document.getElementById("docListFrame").style.display == "inline")
     {
        document.getElementById("docListFrame").style.display = "none";
     }
     else
     {
        document.getElementById("docListFrame").style.display = "inline";
     }
     
     var ref = document.getElementById("wdesk:docList1");
     var posx = findPosX(ref);
    var posy = findPosY(ref);
    try{
        posy  -= document.getElementById("scroll").scrollTop;
        posx -= document.getElementById("scroll").scrollLeft;
    }catch(e){}
        
    var elementHeightWidth = {'Height': 0, 'Width': 0};
    if(document.getElementById("wdesk:docOperation")){
        elementHeightWidth = getElementHeightWidth(document.getElementById("wdesk:docOperation"));
    }
    
    var width = 300;
    var height = 150;
    var isIEBrowser = (navigator.appName=='Netscape') && (window.navigator.userAgent.indexOf('Trident/') < 0) && (!isChrome)? false: true;
    var isChrome = window.chrome;
    
    if(isIEBrowser){
        document.getElementById("docListFrame").style.top = (posy-height-32)+"px";
    } else {
         if(typeof isChrome != 'undefined') {
            document.getElementById("docListFrame").style.top = (posy-height-22)+"px";
         } else {
            document.getElementById("docListFrame").style.top = (posy-height-22)+"px";
         }
    }
    
    //document.getElementById("docListFrame").style.top = "477px";
    document.getElementById("docListFrame").style.left = (posx-width+elementHeightWidth.Width-75)+"px";//Bug 65276 
    document.getElementById("docListFrame").style.width = width+"px";
    document.getElementById("docListFrame").style.height = height+"px";
    window.frames["docListFrame"].location=sContextPath+'/components/workitem/view/document_list_view_popup.jsf?wid='+encode_ParamValue(wid)+'&taskid='+encode_ParamValue(taskid)+'&pid='+encode_utf8(pid)+'&operations='+encode_ParamValue(opt)+'&rid='+encode_ParamValue(MakeUniqueNumber())+'&WD_UID='+WD_UID+'&Action=doc';
}


// Fixed layout

function renderFixedWDesk(){
    var OUTER_MARGIN_HR = 10;
    var OUTER_MARGIN_VR = 0;
    var OUTER_MARGIN_VR_BOTTOM = 10;
    
    // SANDWICH_MARGIN > RESIZER_WIDTH
    var SANDWICH_MARGIN = 16;
    var RESIZER_WIDTH = 4;
    var RESIZER_MARGIN = 220;
    var TAB_SANDWICH_MARGIN = 5;
    
    var divRef = document.getElementById("wdesk:noIntMsg");
    divRef.style.display = "none"; 
    
    var contDivRef = document.getElementById("fixedContainerDiv");   
    contDivRef.style.display = "block";
    // Resets the document.documentElement.clientHeight
    contDivRef.style.height = "0px";
    
    var CONTAINER_WIDTH = document.body.clientWidth - 2;  
    var CONTAINER_HIGHT = document.documentElement.clientHeight - document.body.clientHeight - 2;    
    var DIVISION_WIDTH = CONTAINER_WIDTH;    
    
    // setting style of main container     
    contDivRef.style.width = CONTAINER_WIDTH + "px";                
    contDivRef.style.height = CONTAINER_HIGHT +"px";  
    
    var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;    
    var iWDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfacesCount;
    
    var screenDimension = getScreenDimension();
    var screenWidth = screenDimension.ScreenWidth;    
    strLeft = (strLeft*screenWidth)/wDeskLayout.ScreenWidth;
        
    if(wDeskInterfaces.Left.Interfaces.length>0 && wDeskInterfaces.Right.Interfaces.length==0){
        FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces = true;
    }
    
    if(wDeskInterfaces.Left.Interfaces.length==0 && wDeskInterfaces.Right.Interfaces.length>0){
        FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces = true;
    }
    
    if(iWDeskInterfaces <= 0){
        // No interface is associated with the activity
        
        //contDivRef.style.border = "1px solid #A0A0A0";        
        divRef.style.display = "inline";            
    } else {    
        
        if(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces || FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces){
            DIVISION_WIDTH = CONTAINER_WIDTH - 2*OUTER_MARGIN_HR;
            hideItems();
        }else {
            DIVISION_WIDTH = (CONTAINER_WIDTH - 2*OUTER_MARGIN_HR - SANDWICH_MARGIN)/2;
            showItems();
            initSliderControl();
        }
        
        // initializes left interface menu position and handler
        initInterfaceTabControl(contDivRef, SANDWICH_MARGIN, OUTER_MARGIN_HR, OUTER_MARGIN_VR, DIVISION_WIDTH, TAB_SANDWICH_MARGIN);

        initFixedInterfaces(CONTAINER_HIGHT, OUTER_MARGIN_VR_BOTTOM, SANDWICH_MARGIN, OUTER_MARGIN_HR, OUTER_MARGIN_VR, DIVISION_WIDTH, RESIZER_WIDTH, RESIZER_MARGIN);        
    }     
}

function initFixedInterfaces(CONTAINER_HIGHT, OUTER_MARGIN_VR_BOTTOM, SANDWICH_MARGIN, OUTER_MARGIN_HR, OUTER_MARGIN_VR, DIVISION_WIDTH, RESIZER_WIDTH, RESIZER_MARGIN){
    var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;    
    var leftInterfaceWidth = strLeft-0;        
        
    if(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces || FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces){
        // If one of the interface container is empty
        leftInterfaceWidth = 0;
    } else {
        if(leftInterfaceWidth > 0) {
            leftInterfaceWidth = DIVISION_WIDTH - leftInterfaceWidth;
        }
    }
    
    var contDivRef = document.getElementById("fixedContainerDiv"); 
    var contDivLeft = findPosX(contDivRef);
    var contDivTop = findPosY(contDivRef);    
    var divRef = null;
    
    var leftTabMenuDiv = document.getElementById("leftTabMenuDiv");  
    
    for(var i=0; i<wDeskInterfaces.Left.Interfaces.length; i++){
        divRef = document.getElementById("div_C"+(i+1));
        divRef.style.left = contDivLeft + OUTER_MARGIN_HR + "px";
        divRef.style.top = contDivTop + OUTER_MARGIN_VR + leftTabMenuDiv.offsetHeight + "px";
        divRef.style.width = DIVISION_WIDTH - leftInterfaceWidth + "px";
        divRef.style.height =  CONTAINER_HIGHT - OUTER_MARGIN_VR - leftTabMenuDiv.offsetHeight - OUTER_MARGIN_VR_BOTTOM + "px";

        if(i==0){
            if(!FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces){                                
                FixedWDeskInfo.WDeskInterfaces.Left.Width = DIVISION_WIDTH - leftInterfaceWidth;
            }
            
            divRef.style.display = "inline";
            //getInterface(wDeskInterfaces.Left.Interfaces[i].Interface.DivId,wDeskInterfaces.Left.Interfaces[i].Interface.Url,wDeskInterfaces.Left.Interfaces[i].Interface.EXT); 
        }                        
        getInterface(wDeskInterfaces.Left.Interfaces[i].Interface.DivId,wDeskInterfaces.Left.Interfaces[i].Interface.Url,wDeskInterfaces.Left.Interfaces[i].Interface.EXT, divRef); 
    }
    
    // setting resizer positioning
    if(!(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces || FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces)){
        var resizer = document.getElementById("resizer");     
        resizer.style.left = contDivLeft + OUTER_MARGIN_HR + DIVISION_WIDTH - leftInterfaceWidth + (SANDWICH_MARGIN-RESIZER_WIDTH)/2 - 2 + "px";
        resizer.style.top = contDivTop + OUTER_MARGIN_VR + leftTabMenuDiv.offsetHeight + RESIZER_MARGIN + "px";
        resizer.style.width = RESIZER_WIDTH + "px";        
        resizer.style.height = CONTAINER_HIGHT - OUTER_MARGIN_VR - leftTabMenuDiv.offsetHeight - OUTER_MARGIN_VR_BOTTOM - 2*RESIZER_MARGIN + "px";
                
        FixedWDeskInfo.WDeskInterfaces.ResizerLeft = contDivLeft + OUTER_MARGIN_HR + DIVISION_WIDTH - leftInterfaceWidth + (SANDWICH_MARGIN-RESIZER_WIDTH)/2 - 2;
        
        var innerResizer = document.getElementById("innerResizer");     
        innerResizer.style.left = contDivLeft + OUTER_MARGIN_HR + DIVISION_WIDTH - leftInterfaceWidth + SANDWICH_MARGIN/2 + "px";
        innerResizer.style.top = contDivTop + OUTER_MARGIN_VR + leftTabMenuDiv.offsetHeight + "px";
        //innerResizer.style.width = RESIZER_WIDTH/2 + "px";
        innerResizer.style.height = CONTAINER_HIGHT - OUTER_MARGIN_VR - leftTabMenuDiv.offsetHeight - OUTER_MARGIN_VR_BOTTOM + "px";
        
        FixedWDeskInfo.WDeskInterfaces.InnerResizerLeft  = contDivLeft + OUTER_MARGIN_HR + DIVISION_WIDTH - leftInterfaceWidth + SANDWICH_MARGIN/2;                
    }

    var rightTabMenuDiv = document.getElementById("rightTabMenuDiv");

    for(i=0; i<wDeskInterfaces.Right.Interfaces.length; i++){
        divRef = document.getElementById("div_C"+(wDeskInterfaces.Left.Interfaces.length+i+1));
        
        if(FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces){
            divRef.style.left = contDivLeft + OUTER_MARGIN_HR + "px";
        } else {
            divRef.style.left = contDivLeft + DIVISION_WIDTH - leftInterfaceWidth + SANDWICH_MARGIN + OUTER_MARGIN_HR + "px";
        }
        
        divRef.style.top = contDivTop + OUTER_MARGIN_VR + rightTabMenuDiv.offsetHeight + "px";
        divRef.style.width = DIVISION_WIDTH + leftInterfaceWidth + "px";
        divRef.style.height =  CONTAINER_HIGHT - OUTER_MARGIN_VR - rightTabMenuDiv.offsetHeight - OUTER_MARGIN_VR_BOTTOM + "px";

        if(i==0){
            if(!FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces){                
                FixedWDeskInfo.WDeskInterfaces.Right.Left = contDivLeft + DIVISION_WIDTH - leftInterfaceWidth + SANDWICH_MARGIN + OUTER_MARGIN_HR;            
                FixedWDeskInfo.WDeskInterfaces.Right.Width = DIVISION_WIDTH + leftInterfaceWidth;
            }            
            
            divRef.style.display = "inline";
            //getInterface(wDeskInterfaces.Right.Interfaces[i].Interface.DivId,wDeskInterfaces.Right.Interfaces[i].Interface.Url,wDeskInterfaces.Right.Interfaces[i].Interface.EXT); 
        }     
        getInterface(wDeskInterfaces.Right.Interfaces[i].Interface.DivId,wDeskInterfaces.Right.Interfaces[i].Interface.Url,wDeskInterfaces.Right.Interfaces[i].Interface.EXT); 
    }    
}

function initInterfaceTabControl(contDivRef, SANDWICH_MARGIN, OUTER_MARGIN_HR, OUTER_MARGIN_VR, DIVISION_WIDTH, TAB_SANDWICH_MARGIN){  
    var contDivLeft = findPosX(contDivRef);
    var contDivTop = findPosY(contDivRef);
    var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;    
    var leftInterfaceWidth = strLeft-0;    
    
    // If one of the interface container is empty
    if(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces || FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces){
        leftInterfaceWidth = 0;
    } else {
        if(leftInterfaceWidth > 0) {
            leftInterfaceWidth = DIVISION_WIDTH - leftInterfaceWidth;
        }
    }    
    
    // initializes left interface menu position
    var leftTabMenuDiv = document.getElementById("leftTabMenuDiv"); 
    if(FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces) {
        leftTabMenuDiv.style.display = "none";
    } else {        
        leftTabMenuDiv.style.left = contDivLeft + OUTER_MARGIN_HR + "px";
        leftTabMenuDiv.style.top = contDivTop + OUTER_MARGIN_VR + "px";
        leftTabMenuDiv.style.width = DIVISION_WIDTH - leftInterfaceWidth + "px";
        leftTabMenuDiv.style.display = "inline";
    }
    
    // initializes right interface menu position
    var rightTabMenuDiv = document.getElementById("rightTabMenuDiv");    
    if(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces) {
        rightTabMenuDiv.style.display = "none";
    } else {
        if(FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces) {
            rightTabMenuDiv.style.left = contDivLeft + OUTER_MARGIN_HR + "px";    
        } else {
            rightTabMenuDiv.style.left = contDivLeft + DIVISION_WIDTH - leftInterfaceWidth + SANDWICH_MARGIN + OUTER_MARGIN_HR + "px";
        }
        
        rightTabMenuDiv.style.top = contDivTop + OUTER_MARGIN_VR + "px";
        rightTabMenuDiv.style.width = DIVISION_WIDTH + leftInterfaceWidth + "px";
        rightTabMenuDiv.style.display = "inline";
    }
    
    var arrInterfaces = [];    
    // renders left interfaces menu handler
    for(var i=0; i<wDeskInterfaces.Left.Interfaces.length; i++){
        arrInterfaces[i] = wDeskInterfaces.Left.Interfaces[i].Interface.DivId;
        
        if(wDeskInterfaces.Left.Interfaces[i].Interface.DivId == 'form'){
            if(wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"){                
                var bDisplay = ((i == 0) && SharingMode && (oldWDJason.WIViewMode != 'R'))? true: false;                    
                initInterfaceTabMenu(leftTabMenuDiv, 'form', bDisplay);
            }
        }        
    }
    if(arrInterfaces.length>0){
        addCSS(arrInterfaces, TAB_SANDWICH_MARGIN);
        addTabHandler(arrInterfaces, "leftTabMenu", "li");               
        
        if(!(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces || FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces)){
            wDeskInterfaces.Left.TabsTotalWidth = getTotalWidth(arrInterfaces);
        }
    }
    
    arrInterfaces = [];
    // renders right interfaces menu handler
    for(var i=0; i<wDeskInterfaces.Right.Interfaces.length; i++){
        arrInterfaces[i] = wDeskInterfaces.Right.Interfaces[i].Interface.DivId;        
        
        if(wDeskInterfaces.Right.Interfaces[i].Interface.DivId == 'form'){
            if(wiproperty.formType=="NGFORM" && ngformproperty.type=="applet"){
                var bDisplay = ((i == 0) && SharingMode && (oldWDJason.WIViewMode != 'R'))? true: false;                 
                initInterfaceTabMenu(leftTabMenuDiv, 'form', bDisplay);
            }
        }
    }
    if(arrInterfaces.length>0){
        addCSS(arrInterfaces, TAB_SANDWICH_MARGIN);
        addTabHandler(arrInterfaces, "rightTabMenu", "li");	
        
        if(!(FixedWDeskInfo.WDeskInterfaces.OnlyLeftInterfaces || FixedWDeskInfo.WDeskInterfaces.OnlyRightInterfaces)){
            wDeskInterfaces.Right.TabsTotalWidth = getTotalWidth(arrInterfaces);
        }
    }    
}

function addCSS(tabNames, TAB_SANDWICH_MARGIN){
    var temp = null;
    
    for(var i=0;i<tabNames.length;i++) {
        temp = document.getElementById(tabNames[i]);
        temp.style.marginLeft = "0px";
        temp.style.marginRight = TAB_SANDWICH_MARGIN + "px";
    }
}

function addTabHandler(tabNames, tabContainer, tagName){
    var activeTabIndex = -1;    
    var tabMenu = null;
    
    if(document.getElementsByClassName){
        tabMenu = document.getElementsByClassName(tabContainer);
    }
    if(document.querySelectorAll){
        tabMenu = document.querySelectorAll("."+tabContainer);
    }    
    
    var childNodes = tabMenu[0].childNodes;
	
    for(var i=0;i<childNodes.length;i++) {
        if(childNodes[i].tagName == tagName.toLowerCase() || childNodes[i].tagName == tagName.toUpperCase()){
            childNodes[i].onclick = function(e) {
                var srcElement = (e != undefined)? e.target: window.event.srcElement;
                var temp = null;
                var selectedTabIndex = -1;
                
                for(var i=0;i<tabNames.length;i++) {
                    if(srcElement.id == tabNames[i]) {
                        activeTabIndex = i;
                    } else {
                        temp = document.getElementById(tabNames[i]);
                        if(hasClass(temp, "active")){
                            // previously active tab
                            
                            selectedTabIndex = i;
                        }
                    }
                }
                
                // selectedTabIndex == -1  ->  active tab is clicked again
                if(selectedTabIndex > -1){     
                    // hide show interface tab and menu
                    hideShowInterfaceTab(tabNames[selectedTabIndex], tabNames[activeTabIndex]);
                    
                    // toggles interface on tab change
                    toggleIntFace(tabNames[activeTabIndex], tabNames[selectedTabIndex]);
                    
                    if(SharingMode){                        
                        fixedWDeskTabChangeEvent(tabNames[selectedTabIndex], tabNames[activeTabIndex], document.getElementById(tabNames[selectedTabIndex]).innerHTML, document.getElementById(tabNames[activeTabIndex]).innerHTML);
                    }
                }
                return false;
            };
        }
    }
}

function adjustFixedInterfaces(wdJason, bRenderWDesk){   
    bRenderWDesk = (typeof bRenderWDesk == "undefined")? false: bRenderWDesk;
    
    if(oldWDJason.WDeskLayout.WDeskType == "E"){        
        cancelSaveLayout();
        clearEditableLayout();                
    }else {
        cancelSaveFixedLayout("A");
    }
    
    if(!bRenderWDesk && (oldWDJason.RouteId == newWDJason.RouteId) && (oldWDJason.ActivityId == newWDJason.ActivityId)){ 
        var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;          
        //var selectedInterface = null;
        //var temp = null;
        
        for(var i=0; i<wDeskInterfaces.Left.Interfaces.length; i++){
            /*temp = document.getElementById(wDeskInterfaces.Left.Interfaces[i].Interface.DivId);
            if(hasClass(temp, "active")){
                selectedInterface = wDeskInterfaces.Left.Interfaces[i];
            }*/
            getInterface(wDeskInterfaces.Left.Interfaces[i].Interface.DivId); 
        }
        
        /*if(selectedInterface != null){
            getInterface(selectedInterface.Interface.DivId);
        }*/
        
        //selectedInterface = null;
        for(i=0; i<wDeskInterfaces.Right.Interfaces.length; i++){
            /*temp = document.getElementById(wDeskInterfaces.Right.Interfaces[i].Interface.DivId);
            if(hasClass(temp, "active")){
                selectedInterface = wDeskInterfaces.Right.Interfaces[i];
            }*/
            getInterface(wDeskInterfaces.Right.Interfaces[i].Interface.DivId); 
        }
        
        /*if(selectedInterface != null){
            getInterface(selectedInterface.Interface.DivId);
        }*/
    } else {
        getFixedInterfaceView();
        renderFixedWDesk();
    }
}

function getFixedInterfaceView(){
    var url = sContextPath+"/components/workitem/view/fixedinterface_main_view.jsf";
    var params = "Action=getinterfaceview"+"&pid="+encode_utf8(pid)+"&wid="+encode_utf8(wid)+"&taskid="+encode_utf8(taskid);
    var reqRef = new net.ContentLoader(url, null, null, "POST", params, false);
    if(reqRef.req.status == 200) {
        var respText = reqRef.req.responseText;
        respText = respText.substring(respText.indexOf('<container>')+11,respText.lastIndexOf('</container>'));
        document.getElementById('fixedContainerDiv').innerHTML = respText;
    }    
}

function editLayoutWrapper(){
    if(wDeskLayout.WDeskType == 'E'){
        editLayout();
    } else {
        editFixedLayout();
    }
}

function saveLayoutWrapper(){
    if(wDeskLayout.WDeskType == 'E'){
        saveLayout();
    } else {
        saveLayout();
    }
}

function editFixedLayout(){   
    disableEditLayoutOption();
    
    // removing interfaces from main header menu
    myMenu = [];
    prepareWdeskDefaultMenu();
    
    var innerResizer = document.getElementById("innerResizer");   
    innerResizer.style.display = "inline";
    var resizer = document.getElementById("resizer");         
    resizer.style.display = "inline";
    
    varEditLayout=0;   
    
    if(isCollabServerConnected){
        disableShareOption();
    }
}

function cancelSaveLayoutWrapper(){
    if(wDeskLayout.WDeskType == 'E'){
        cancelSaveLayout();
    } else {
        cancelSaveFixedLayout();
    }
}

function cancelSaveFixedLayout(type){
    // type == "A" --> Auto
    if(varEditLayout == 0){
        myMenu = myMenuOrig;
        prepareWdeskDefaultMenu();
        
        enableEditLayoutOption();

        clearFixedLayoutSlider();
        
        type = (typeof type == 'undefined')? '': type;
        resetFixedInterfaces(type);

        varEditLayout=1;
        
        if(oldWDJason.WIViewMode == "R"){
            if(SharingMode){
                enableShareOption();
            }
        } else {        
            if(isCollabServerConnected){
                enableShareOption();        
            }
        } 
        cancelSaveEditLayoutHook('X');
    }
}

function getTotalWidth(element){
    if(element instanceof Array){
        var temp = null;
        var width = 0;
        for(var i=0; i< element.length; i++){
            temp = document.getElementById(element[i])
            width += temp.offsetWidth + (temp.style.marginLeft.split('p')[0]-0) + (temp.style.marginRight.split('p')[0]-0);
        }
        return width
    } else {
        return element.offsetWidth + (element.style.marginLeft.split('p')[0]-0) + (element.style.marginRight.split('p')[0]-0);
    }
}

function hideItems(){
    //Bug 66049 starts
    var editRef = document.getElementById("wdesk:editLayoutlbl");
    var navSepRef = document.getElementById("wdesk:navSep");
    if(editRef != null && navSepRef != null){       //Bug 66049 ends
    document.getElementById("wdesk:editLayoutlbl").style.display = "none";
    document.getElementById("wdesk:navSep").style.display="none";
    }
}

function showItems(){
    //Bug 66049 starts
    var editRef = document.getElementById("wdesk:editLayoutlbl");
    var navSepRef = document.getElementById("wdesk:navSep");
    if(editRef != null && navSepRef != null){ //Bug 66049 ends
    document.getElementById("wdesk:editLayoutlbl").style.display = "block";
    document.getElementById("wdesk:navSep").style.display="block";
    }
}


// Slider control
function initSliderControl(){
    var resizer = document.getElementById("resizer");
    resizer.onmousedown = function (event){
        onSliderMouseDown(event);
    }
}

var XLIMIT = 150;
var leftXLimit = XLIMIT;
var rightXLimit = XLIMIT;
var XLIMIT_MARGIN = 50;
function onSliderMouseDown(e,x){
    mouseStatus = 'down';
    
    var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;  
    if(wDeskInterfaces.Left.TabsTotalWidth > XLIMIT) {
        leftXLimit = wDeskInterfaces.Left.TabsTotalWidth + XLIMIT_MARGIN;
    } else {
        leftXLimit = XLIMIT + XLIMIT_MARGIN;
    }
    
    var ref = document.getElementById("div_C1");
    leftXLimit = ref.style.left.split('p')[0]-0 + leftXLimit;
    
    if(wDeskInterfaces.Right.TabsTotalWidth > XLIMIT) {
        rightXLimit = wDeskInterfaces.Right.TabsTotalWidth + XLIMIT_MARGIN;
    } else {
        rightXLimit = XLIMIT + XLIMIT_MARGIN;
    }
    
    ref = document.getElementById("div_C"+(wDeskInterfaces.Left.Interfaces.length+1));
    rightXLimit = (ref.style.left.split('p')[0]-0) + (ref.style.width.split('p')[0]-0) - rightXLimit;
    
    
    ref = document.getElementById("fixedContainerDiv");
    showBlankIframe(ref);
    
    ref = document.getElementById("innerResizer");    
    repositionSliderDiv(ref);
    lastPosX = ref.style.left.split('p')[0]-0;                
    
    stopSelection(gPopupMask);
    showPopupMask();        
}

function onMouseUpFixedLayout(e){    
    if(mouseStatus == 'down'){
        var curEvent = (typeof event == 'undefined'? e: event);
        var pos = DIF_getEventPosition(curEvent);        
        var newX = pos.x;        
        var ref = null;
        
        newX = newX < leftXLimit? leftXLimit: newX;        
        newX = newX > rightXLimit? rightXLimit: newX;        
        
        var pxMove = newX - lastPosX;
        ref = document.getElementById("leftTabMenuDiv");        
        ref.style.width = (ref.style.width.split('p')[0]-0) + pxMove + "px";        

        ref = document.getElementById("rightTabMenuDiv");
        ref.style.left = (ref.style.left.split('p')[0]-0) + pxMove + "px";        
        ref.style.width = (ref.style.width.split('p')[0]-0) - pxMove + "px";        

        var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;        
        for(var i=0; i<wDeskInterfaces.Left.Interfaces.length; i++){
            ref = document.getElementById("div_C"+(i+1));
            ref.style.width = (ref.style.width.split('p')[0]-0) + pxMove + "px";
            
            resetHWFixedInterfaces(wDeskInterfaces.Left.Interfaces[i].Interface.Type, wDeskInterfaces.Left.Interfaces[i].Interface.DivId)
        }

        for(i=0; i<wDeskInterfaces.Right.Interfaces.length; i++){
            ref = document.getElementById("div_C"+(wDeskInterfaces.Left.Interfaces.length+i+1));            
            ref.style.left = (ref.style.left.split('p')[0]-0) + pxMove + "px";            
            ref.style.width = (ref.style.width.split('p')[0]-0) - pxMove + "px";
            
            resetHWFixedInterfaces(wDeskInterfaces.Right.Interfaces[i].Interface.Type, wDeskInterfaces.Right.Interfaces[i].Interface.DivId)
        }

        ref = document.getElementById("resizer");
        ref.style.left = (ref.style.left.split('p')[0]-0) + pxMove + "px";  

        ref = document.getElementById("innerResizer");
        ref.style.left = (ref.style.left.split('p')[0]-0) + pxMove + "px";          
        
        ref = document.getElementById("sliderDiv");
        ref.style.display = "none";                
        hideBlankIframe();
        hidePopupMask();        
        
        ref = document.getElementById('wdesk');
        ref.Left.value = document.getElementById("div_C1").style.width.split('p')[0]-0;
    }
    
    mouseStatus = 'up';        
}

function onMouseMoveFixedLayout(e){    
    if(mouseStatus == 'down'){
        var curEvent = (typeof event == 'undefined'? e: event);
        var pos = DIF_getEventPosition(curEvent);        
        var newX = pos.x;        
        
        if(newX < leftXLimit){
            newX = leftXLimit;
        }
        
        if(newX > rightXLimit){
            newX = rightXLimit;
        }
        
        var ref = document.getElementById("sliderDiv");                   
        ref.style.left = newX + "px"; 
    }
}

function repositionSliderDiv(divRef){    
    var ref = document.getElementById("sliderDiv");    
    ref.style.width = divRef.style.width;
    ref.style.height = divRef.style.height;
    ref.style.top = divRef.style.top;
    ref.style.left = divRef.style.left;
    ref.style.display = "block";
    ref.style.zIndex = "1000";
}

function stopSelection(ref){    
    ref.onselectstart = function(e){
        if(typeof e != 'undefined'){
            if (typeof e.preventDefault != 'undefined' && e.preventDefault != null) {
                e.preventDefault();
            } else if (typeof e.stopPropagation != 'undefined' && e.stopPropagation != null){
                e.stopPropagation();
            } else {
                window.event.cancelBubble=true;
            }  
        }      
            
        return false;
    };
}

function clearSelection() {
    try{
        if ( document.selection ) {
            document.selection.empty();
        } else if ( window.getSelection ) {
            window.getSelection().removeAllRanges();
        }
    } catch(e){        
    }
}

function clearFixedLayout(){
    var ref = document.getElementById("fixedContainerDiv"); 
    ref.style.display = "none";
    removeChildElements(ref);
}

function clearEditableLayout(){    
    var ref = document.getElementById("containerDiv"); 
    ref.style.display = "none";
    removeChildElements(ref);
    
    interfaceMap.clear();
}

function clearFixedLayoutSlider(){
    var ref = document.getElementById("innerResizer"); 
    if(ref.style.display != "none"){
        ref.style.display = "none";

        ref = document.getElementById("resizer"); 
        ref.style.display = "none";
    }
}

function enableEditLayoutOption(){
    var ref = document.getElementById("wdesk:editLayoutlbl");
    if(ref.style.display == 'none'){
        ref.style.display="block";
        document.getElementById("wdesk:saveLayoutlbl").style.display="none";
        document.getElementById("myMenuID").style.display="block";
        document.getElementById("wdesk:cancelsaveLayoutlbl").style.display="none";
        document.getElementById("wdesk:navSep1").style.display="none";
        document.getElementById("wdesk:navigationPG").style.display = "inline-block";    
    }
}

function disableEditLayoutOption(){
    var ref = document.getElementById("wdesk:editLayoutlbl");
    if(ref.style.display != 'none'){
        document.getElementById("wdesk:editLayoutlbl").style.display="none";
        document.getElementById("wdesk:saveLayoutlbl").style.display="block";
        document.getElementById("wdesk:cancelsaveLayoutlbl").style.display="block";
        document.getElementById("wdesk:navSep1").style.display="block";   
        document.getElementById("wdesk:navigationPG").style.display = "none";
    }
}

function resetHWFixedInterfaces(Type, divId)
{
    if(Type == "Document"){
        InitHWDoc("Resize");
    } else if(Type == "FormView"){
        initHWFormNew();
    } else if(Type == "FormExtView") {
        initHWFormNew();
    } else if(Type == "Exceptions") {
        InitHWExpNew();
    } else if(Type == "ToDoList") {
        InitHWTodoNew();
    } else if(Type == "DynamicCase") {
        InitHWTaskNew();
    } else if(divId == "SAP0"){
        InitSAPNew(Type);
    } else if(divId == "SAP1"){
        InitSAPNew(Type);
    } else if(divId == "SAP2"){
        InitSAPNew(Type);
    } else if(divId == "SAP3"){
        InitSAPNew(Type);
    } else if(divId == "SAP"){
        InitSAPNew(Type);
    } else{
        InitCustomNew(Type);
    }
}

function resetFixedInterfaces(type){       
    var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;     
    
    if(typeof type != 'undefined' && type == "A"){
        wDeskInterfaces = oldWDJason.WDeskLayout.WDeskFixedLayout.WDeskInterfaces;
    }
    
    var ref = document.getElementById("leftTabMenuDiv");        
    ref.style.width = FixedWDeskInfo.WDeskInterfaces.Left.Width + "px";        

    ref = document.getElementById("rightTabMenuDiv");
    ref.style.left = FixedWDeskInfo.WDeskInterfaces.Right.Left + "px";        
    ref.style.width = FixedWDeskInfo.WDeskInterfaces.Right.Width + "px";  
        
    for(var i=0; i<wDeskInterfaces.Left.Interfaces.length; i++){
        ref = document.getElementById("div_C"+(i+1));
        ref.style.width = FixedWDeskInfo.WDeskInterfaces.Left.Width + "px";
            
        resetHWFixedInterfaces(wDeskInterfaces.Left.Interfaces[i].Interface.Type, wDeskInterfaces.Left.Interfaces[i].Interface.DivId)
    }

    for(i=0; i<wDeskInterfaces.Right.Interfaces.length; i++){
        ref = document.getElementById("div_C"+(wDeskInterfaces.Left.Interfaces.length+i+1));            
        ref.style.left = FixedWDeskInfo.WDeskInterfaces.Right.Left + "px";            
        ref.style.width = FixedWDeskInfo.WDeskInterfaces.Right.Width + "px";
            
        resetHWFixedInterfaces(wDeskInterfaces.Right.Interfaces[i].Interface.Type, wDeskInterfaces.Right.Interfaces[i].Interface.DivId)
    }

    ref = document.getElementById("resizer");
    ref.style.left = FixedWDeskInfo.WDeskInterfaces.ResizerLeft + "px";  

    ref = document.getElementById("innerResizer");
    ref.style.left = FixedWDeskInfo.WDeskInterfaces.InnerResizerLeft + "px";
    
    ref = document.getElementById('wdesk');
    ref.Left.value = FixedWDeskInfo.WDeskInterfaces.Left.Width;
}

var FixedWDeskInfo = {
    WDeskInterfaces: {
        Left: {Width: 0, TabsTotalWidth: 0},
        Right: {Left: 0, Width: 0, TabsTotalWidth: 0},
        ResizerLeft: 0,
        InnerResizerLeft: 0,
        OnlyLeftInterfaces: false,
        OnlyRightInterfaces: false
    }        
};

function resetFixedWDeskInfo(){
    FixedWDeskInfo = {
        WDeskInterfaces: {
            Left: {Width: 0, TabsTotalWidth: 0},
            Right: {Left: 0, Width: 0, TabsTotalWidth: 0},
            ResizerLeft: 0,
            InnerResizerLeft: 0,
            OnlyLeftInterfaces: false,
            OnlyRightInterfaces: false
        }    
    };
}

function getScreenDimension(){
    var screenWidth = window.screen.width;
    var screenHeight = window.screen.height;

    if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
        //IE 6+ in 'standards compliant mode'        
        screenWidth = document.documentElement.clientWidth;
        screenHeight = document.documentElement.clientHeight;
    } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {        
        //IE 4 compatible
        screenWidth = document.body.clientWidth;
        screenHeight = document.body.clientHeight;
    } 	
    
    return {"ScreenWidth": screenWidth, "ScreenHeight": screenHeight};
}

var isCollabServerConnected = false;
function initCollabServerStatus(){  
    try{
        if(typeof window.opener.parent.isCollabServerConnected != 'undefined'){
            isCollabServerConnected = window.opener.parent.isCollabServerConnected();
        }
    }catch(e){}
}

function initInterfaceTabMenu(parentRef, tabId, bDisplay){    
    var ref = document.getElementById(tabId+"BroadCast");
    
    if(ref == null) {
        ref = window.document.createElement("img");
        ref.setAttribute("id", tabId+"BroadCast");        
        ref.style.padding = "0px";        
        ref.style.width="16px";
        ref.style.height="16px";
        ref.style.cursor="pointer";
        ref.style.position="relative";        
        ref.style.cssFloat="right";
        
        if(bDisplay){
            ref.style.display = "inline";
        } else {
            ref.style.display = "none";
        }

        ref.onclick = function(event){
            broadcastFormEvents(event);
        }      

        ref.src = "/webdesktop/resources/images/send.jpg";
        parentRef.appendChild(ref);  
    }
}

function hideShowInterfaceTabMenu(hideTabId, showTabId){    
    var ref = null;
        
    if(SharingMode){      
        // hide show interface tab menu in collaboration mode
        if(hideTabId != ''){
            ref = document.getElementById(hideTabId+"BroadCast");
            if(ref != null){
                ref.style.display = "none";         
            }
        }
        
        if(showTabId != ''){
            if(oldWDJason.WIViewMode != 'R'){
                ref = document.getElementById(showTabId+"BroadCast");
                if(ref != null){
                    ref.style.display = "inline";
                }     
            }
        }
    } else {
        
    }
}

function hideShowInterfaceTab(hideTabId, showTabId){    
    var ref = null;
    
    // remove active class to previously selected interface tab
    if(hideTabId != '') {
        removeClass(hideTabId, "active");
        removeClass(hideTabId, "headerColor");
        removeClass(hideTabId, "panelstyle");
        
        ref = document.getElementById(hideTabId+"Div");
        if(ref != null){        
            ref.parentNode.style.display = "none";
        }    
    }
    
    if(showTabId != '') {
        // add active class to newly selected interface tab
        addClass(showTabId, "active headerColor panelstyle");  

        ref = document.getElementById(showTabId+"Div");
        if(ref != null){
            ref.parentNode.style.display = "inline"; 
        }
    }
    
    // hide show interface tab menu
    hideShowInterfaceTabMenu(hideTabId, showTabId);
}

function getSelectedInterfaceTab(){
    var ref = null;
    var wDeskInterfaces = wDeskLayout.WDeskFixedLayout.WDeskInterfaces;    
    var selectedInterfaceTab = {CollabSelectedIntTab: {Left: '', Right: ''}};
    
    for(var i=0; i<wDeskInterfaces.Left.Interfaces.length; i++) {
        ref = document.getElementById(wDeskInterfaces.Left.Interfaces[i].Interface.DivId);
        if(hasClass(ref, "active")){
            selectedInterfaceTab.CollabSelectedIntTab.Left = wDeskInterfaces.Left.Interfaces[i].Interface.DivId;
            break;
        }
    }
    
    for(i=0; i<wDeskInterfaces.Right.Interfaces.length; i++) {
        ref = document.getElementById(wDeskInterfaces.Right.Interfaces[i].Interface.DivId);
        if(hasClass(ref, "active")){
            selectedInterfaceTab.CollabSelectedIntTab.Right = wDeskInterfaces.Right.Interfaces[i].Interface.DivId;
            break;
        }
    }
    
    return selectedInterfaceTab;
}

function getCustomScrollbar(selector, enableScrollButtons, axis, autoHideScrollbar, autoExpandHorScroll, updateOnContentResizeFlag, updateOnImageLoadFlag){
    /*enableScrollButtons = (typeof enableScrollButtons == 'undefined'? true: enableScrollButtons);
    axis = (typeof axis == 'undefined'? "yx": axis);
    autoHideScrollbar = (typeof autoHideScrollbar == 'undefined'? true: autoHideScrollbar);
    autoExpandHorScroll = (typeof autoExpandHorScroll == 'undefined'? false: autoExpandHorScroll);
    updateOnContentResizeFlag = (typeof updateOnContentResizeFlag == 'undefined'? false: updateOnContentResizeFlag);
    updateOnImageLoadFlag = (typeof updateOnImageLoadFlag == 'undefined'? false: updateOnImageLoadFlag);*/
    
//    $.mCustomScrollbar.defaults.scrollButtons.enable = enableScrollButtons; //enable scrolling buttons by default
//    $.mCustomScrollbar.defaults.axis = axis;
//    $.mCustomScrollbar.defaults.autoHideScrollbar = autoHideScrollbar;         
//    $(selector).mCustomScrollbar({theme:"dark", advanced:{autoExpandHorizontalScroll: autoExpandHorScroll, updateOnContentResize:updateOnContentResizeFlag, updateOnImageLoad:updateOnImageLoadFlag}});
}

function getDocHeightWidth(doc) {
    doc = doc || document;
    var body = doc.body, html = doc.documentElement;
    
    var height = Math.max( body.scrollHeight, body.offsetHeight, 
        html.clientHeight, html.scrollHeight, html.offsetHeight );
        
    var width = Math.max( body.scrollWidth, body.offsetWidth, 
        html.clientWidth, html.scrollWidth, html.offsetWidth );
        
    return {'Height': height, 'Width': width};
}

function getElementHeightWidth(element) {
    var height = Math.max(element.scrollHeight, element.offsetHeight);        
    var width = Math.max(element.scrollWidth, element.offsetWidth);
    
    return {'Height': height, 'Width': width};
}

function commonAssignToMe()
{
    var dataFrm = document.getElementById('wdesk');
    dataFrm.ToSave.value='true';
    unlockflag="N";
    hideWIFromList();
    clickWiLink("LOCKFORMESAVE","wdesk:controller",'');
}

function commonRelease()
{
   var dataFrm = document.getElementById('wdesk');
    dataFrm.ToSave.value='true';
    unlockflag="N";
    hideWIFromList();
    clickWiLink("RELEASESAVE","wdesk:controller",''); 
}


// Dummy save handling for NGHTML Form 
var WiDummySaveStruct = {
    Type:'',
    Params:{}
};

function getWiDummySaveInfo(){
    return WiDummySaveStruct;
}

function checkDummySaveRequestStatus(data){    
    if(data.status=='success'){
        var wiDummySaveInfoObj = getWiDummySaveInfo();        
        if(wiDummySaveInfoObj.Type == "Action"){
            wiDummySaveInfoObj.Params.window_workdesk.commonExecuteAction(wiDummySaveInfoObj.Params.window_workdesk,wiDummySaveInfoObj.Params.isGenRep,wiDummySaveInfoObj.Params.requestString,wiDummySaveInfoObj.Params.pid,wiDummySaveInfoObj.Params.wid,wiDummySaveInfoObj.Params.taskid);
        } else if(wiDummySaveInfoObj.Type == "Trigger"){
            wiDummySaveInfoObj.Params.sourceWindow.commonFireTrigger(wiDummySaveInfoObj.Params.window_workdesk,wiDummySaveInfoObj.Params.type,wiDummySaveInfoObj.Params.index,wiDummySaveInfoObj.Params.pid,wiDummySaveInfoObj.Params.wid,wiDummySaveInfoObj.Params.taskid);
        } else if(wiDummySaveInfoObj.Type == "SaveData"){
            wiDummySaveInfoObj.Params.window_workdesk.commonSaveData(wiDummySaveInfoObj.Params.window_workdesk, wiDummySaveInfoObj.Params.FormType,wiDummySaveInfoObj.Params.operation);
        } else if(wiDummySaveInfoObj.Type == "SaveDataExp"){
            wiDummySaveInfoObj.Params.window_workdesk.commonSaveDataExp(wiDummySaveInfoObj.Params.window_workdesk, wiDummySaveInfoObj.Params.FormType,wiDummySaveInfoObj.Params.operation);
        } else if(wiDummySaveInfoObj.Type == "UpdateDocWindow"){
            wiDummySaveInfoObj.Params.window_workdesk.commonUpdateDocWindow(wiDummySaveInfoObj.Params.window_workdesk, wiDummySaveInfoObj.Params.pid, wiDummySaveInfoObj.Params.wid, wiDummySaveInfoObj.Params.Name,wiDummySaveInfoObj.Params.taskid);
        } else if(wiDummySaveInfoObj.Type == "SaveZone_CustomCrop"){
            wiDummySaveInfoObj.Params.window_workdesk.commonSaveZone(wiDummySaveInfoObj.Params.url);
        } else if(wiDummySaveInfoObj.Type == "ReloadInterfaces"){
            wiDummySaveInfoObj.Params.window_workdesk.commonReloadInterfaces(wiDummySaveInfoObj.Params.window_workdesk);
        } else if(wiDummySaveInfoObj.Type == "OpenNewDoc"){
            wiDummySaveInfoObj.Params.sourceWindow.commonOpenNewDoc(wiDummySaveInfoObj.Params.window_workdesk,wiDummySaveInfoObj.Params.FormType, wiDummySaveInfoObj.Params.pid, wiDummySaveInfoObj.Params.wid, wiDummySaveInfoObj.Params.docType,wiDummySaveInfoObj.Params.docIndex, wiDummySaveInfoObj.Params.docName, wiDummySaveInfoObj.Params.diskName, wiDummySaveInfoObj.Params.selButton, wiDummySaveInfoObj.Params.deldocId,wiDummySaveInfoObj.Params.taskid);
        } else if(wiDummySaveInfoObj.Type == "AssignToMe"){
            wiDummySaveInfoObj.Params.sourceWindow.commonAssignToMe();
        } else if(wiDummySaveInfoObj.Type == "Release"){
            wiDummySaveInfoObj.Params.sourceWindow.commonRelease();
        } 
    }
}

function setDefaultDocTypeColor(docType){   
    docType = (typeof docType == 'undefined')? '': docType;
    var objCombo = document.getElementById('wdesk:docCombo');
    
    if(objCombo != null){
        var tmpParsedDocJSON = eval("("+document.getElementById('wdesk:docInfoJSON').value+")");
        var docIndex=-1;
        var docInfoJSON = null;
        for(var i=0;i<objCombo.options.length;i++){
            docIndex = objCombo.options[i].value;
            docInfoJSON = tmpParsedDocJSON[docIndex];
            
            if(typeof docInfoJSON != 'undefined'){
                if(docType == ''){
                    if(docInfoJSON.actionPanel.defaultcheck == "1"){
                        objCombo.options[i].style.color = "green";
                    }
                } else {                    
                    if(findDocType(docInfoJSON.document[0].DocName) == docType){
                        objCombo.options[i].style.color = "green";
                        docInfoJSON.actionPanel.defaultcheck = "1";
                    } else {
                        objCombo.options[i].style.color = "";
                        docInfoJSON.actionPanel.defaultcheck = "-1";
                    }
                }
            }
        }
        
        document.getElementById('wdesk:docInfoJSON').value = JSON.stringify(tmpParsedDocJSON);
    }
} 

function callbackInsufficientWindowSize()
{
    var ref = document.getElementById("wdesk:messagePanel:genMessageLabel");
    showGenMessage(ref, RESIZE_COMPONENT_FOR_BETTER_VIEW, 270, 80);
}

function checkTaskDummySaveRequestStatus(data){    
    if(data.status=='success'){
        var wiDummySaveInfoObj = getWiDummySaveInfo();        
        if(wiDummySaveInfoObj.Type == "Action"){
            wiDummySaveInfoObj.Params.window_workdesk.commonExecuteAction(wiDummySaveInfoObj.Params.window_workdesk,wiDummySaveInfoObj.Params.isGenRep,wiDummySaveInfoObj.Params.requestString,wiDummySaveInfoObj.Params.pid,wiDummySaveInfoObj.Params.wid,wiDummySaveInfoObj.Params.taskid);
        } else if(wiDummySaveInfoObj.Type == "Trigger"){
            wiDummySaveInfoObj.Params.sourceWindow.commonFireTrigger(wiDummySaveInfoObj.Params.window_workdesk,wiDummySaveInfoObj.Params.type,wiDummySaveInfoObj.Params.index,wiDummySaveInfoObj.Params.pid,wiDummySaveInfoObj.Params.wid,wiDummySaveInfoObj.Params.taskid);
        } else if(wiDummySaveInfoObj.Type == "SaveData"){
            wiDummySaveInfoObj.Params.window_workdesk.commonSaveData(wiDummySaveInfoObj.Params.window_workdesk, wiDummySaveInfoObj.Params.FormType);
        } else if(wiDummySaveInfoObj.Type == "SaveDataExp"){
            wiDummySaveInfoObj.Params.window_workdesk.commonSaveDataExp(wiDummySaveInfoObj.Params.window_workdesk, wiDummySaveInfoObj.Params.FormType);
        } else if(wiDummySaveInfoObj.Type == "UpdateDocWindow"){
            wiDummySaveInfoObj.Params.window_workdesk.commonUpdateDocWindow(wiDummySaveInfoObj.Params.window_workdesk, wiDummySaveInfoObj.Params.pid, wiDummySaveInfoObj.Params.wid, wiDummySaveInfoObj.Params.Name,wiDummySaveInfoObj.Params.taskid);
        } else if(wiDummySaveInfoObj.Type == "SaveZone_CustomCrop"){
            wiDummySaveInfoObj.Params.window_workdesk.commonSaveZone(wiDummySaveInfoObj.Params.url);
        } else if(wiDummySaveInfoObj.Type == "ReloadInterfaces"){
            wiDummySaveInfoObj.Params.window_workdesk.commonReloadInterfaces(wiDummySaveInfoObj.Params.window_workdesk);
        } else if(wiDummySaveInfoObj.Type == "OpenNewDoc"){
            wiDummySaveInfoObj.Params.sourceWindow.commonOpenNewDoc(wiDummySaveInfoObj.Params.window_workdesk,wiDummySaveInfoObj.Params.FormType, wiDummySaveInfoObj.Params.pid, wiDummySaveInfoObj.Params.wid, wiDummySaveInfoObj.Params.docType,wiDummySaveInfoObj.Params.docIndex, wiDummySaveInfoObj.Params.docName, wiDummySaveInfoObj.Params.diskName, wiDummySaveInfoObj.Params.selButton, wiDummySaveInfoObj.Params.deldocId,wiDummySaveInfoObj.Params.taskid);
        }
    }
}

function openTaskStatusList(height,width,processdefId,workitemId,activityId,processInstanceId,activityName){
    var url = '/webdesktop/components/task/tasklist.jsf';
    url = appendUrlSession(url);

    var left = (window.screen.width - width) / 2;
    var top = (window.screen.height - height) / 2;

    var wFeatures = 'scrollbars=no,status=yes,width='+width+',height='+height+',left=' + left + ',top=' + top;

    var listParam = new Array();
    listParam.push(new Array("RouteID", encode_ParamValue(processdefId)));
    listParam.push(new Array("WorkstageID", encode_ParamValue(activityId)));
    listParam.push(new Array("ProcessInstanceID", encode_ParamValue(processInstanceId)));
    listParam.push(new Array("WorkitemID", encode_ParamValue(workitemId)));
    listParam.push(new Array("ActivityName", encode_ParamValue(activityName)));
    listParam.push(new Array("ShowMyTasks", encode_ParamValue("N")));
    listParam.push(new Array("ShowTaskStatus", encode_ParamValue("Y")));
    listParam.push(new Array("ShowCaseCalendar", encode_ParamValue("N")));
    listParam.push(new Array("ShowCaseVisualization", encode_ParamValue("N")));
    listParam.push(new Array("FromContainer", encode_ParamValue("N")));
    listParam.push(new Array("Comp_height", height));
    listParam.push(new Array("Comp_width", width));
    var win = openNewWindow(url, 'TaskList', wFeatures, true, "Ext1", "Ext2", "Ext3", "Ext4", listParam);
    
}

function holdWI(winloc)
{
    if(winloc == 'S')
        wdesk_win = window;
    else if(winloc == 'T')
        wdesk_win = window.opener;
    if(saveCalled==true) {
       setmessageinDiv(PLEASE_SAVE_FORM_BEFORE_HOLD,"true",3000); 
       return;
    }
    var pid=wdesk_win.document.getElementById('wdesk:pid');
    var wid=wdesk_win.document.getElementById('wdesk:wid');
    url = sContextPath+'/components/workitem/operations/hold.jsf?Action=1&OpenFrom=WI&ProcessInstanceID='+encode_utf8(pid.value)+'&WorkitemID='+encode_utf8(wid.value)+'&RouteName='+strprocessname+'&RouteId='+strRouteId+'&WorkstageId='+stractivityId+'&ActivityName='+stractivityName+'&viewflag=V&winloc='+winloc+'&WD_UID='+WD_UID;
    url = appendUrlSession(url);
    var left = (window.screen.width - 375)/2;
    var top = (document.documentElement.clientHeight - 290)/2;
    popupIFrameOpener(wdesk_win, "hold", url, 400, 300, left, top, false, true, false, false, true); 
}

function unholdWI(winloc)
{
    if(winloc == 'S')
        wdesk_win = window;
    else if(winloc == 'T')
        wdesk_win = window.opener;
    if(saveCalled==true) {
       setmessageinDiv(PLEASE_SAVE_FORM_BEFORE_UNHOLD,"true",3000);
       return;
    }
    var pid=wdesk_win.document.getElementById('wdesk:pid');
    var wid=wdesk_win.document.getElementById('wdesk:wid');
    url = sContextPath+'/components/workitem/operations/unhold.jsf?Action=1&OpenFrom=WI&ProcessInstanceID='+encode_utf8(pid.value)+'&WorkitemID='+encode_utf8(wid.value)+'&QueueType='+strqueueType+'&RouteName='+strprocessname+'&RouteId='+strRouteId+'&WorkstageId='+stractivityId+'&ActivityName='+stractivityName+'&viewflag=V&winloc='+winloc+'&WD_UID='+WD_UID;
    url = appendUrlSession(url);
    var left = (window.screen.width - 375)/2;
    var top = (document.documentElement.clientHeight - 290)/2;
    popupIFrameOpener(wdesk_win, "unhold", url, 400, 320, left, top, false, true, false, false, true); 
}
//Bug Id :60879
function refreshTaskList()
{
    var taskform = window.parent.document.getElementById("taskListFrame");
    if(typeof taskform != 'undefined' && taskform != null) {
        taskform.contentWindow.clickLink("tasklist:cmdbtn_go");
    }
}
// --------------------------

function hideWDSubMenu(){
    document.getElementById('wdsubmenu').style.display='none';
    hideIframe('HiddenMenuDiv');
}

//Bug 64845
function openSAPInterface(interfaceName){
    var interfaceDivID=getInterfaceKeyy(interfaceName);
    status=getInterfaceKeyByStatus(interfaceDivID);
    if(varEditLayout==1 && status!='O'){        
        openInterface("SAP",interfaceName,'',"SAP");
    } else {
        createDivElement("SAP", interfaceName);  
        getInterface("SAP",'',interfaceName); 
        removeInterfaceMenu(interfaceName);
    }
    prepareWdeskDefaultMenu();
}
//Bug 66459 starts
function showMasking(){
    var ref;
    if(isEmbd != 'Y'){
        if(oldWDJason.WDeskLayout.WDeskType == 'E'){
            ref = document.getElementById("containerDiv");
            if(ref != null){
                showBlankIframe(ref);
            }
        } else {
            ref = document.getElementById("fixedContainerDiv");
            if(ref != null){
                showBlankIframe(ref);
            }
        }
        CreateIndicator();
    }
}
function hideMasking(){
    if(isEmbd != 'Y'){
        hideBlankIframe();
        RemoveIndicator();
    }
}
 
function checkRequestStatusWrapper(data){
    if(data.status == 'begin'){
        window.parent.showMasking();
    } 
    if(data.status == 'success') {
        window.parent.hideMasking();
    }
    checkRequestStatus(data);
}

function checkIntroduceRequestStatusWrapper(data){
    if(data.status == 'begin'){
        window.parent.showMasking();
    } 
    if(data.status == 'success') {
        window.parent.hideMasking();
    }
    checkIntroduceRequestStatus(data);
}
function checkDoneRequestStatusWrapper(data){
    if(data.status == 'begin'){
        window.parent.showMasking();
    } 
    if(data.status == 'success') {
        window.parent.hideMasking();
    }
    checkDoneRequestStatus(data);
}
function checkDummySaveRequestStatusWrapper(data){
    if(data.status == 'begin'){
        window.parent.showMasking();
    } 
    if(data.status == 'success') {
        window.parent.hideMasking();
    }
    checkDummySaveRequestStatus(data);
}
function checkTaskRequestStatusWrapper(data){
    if(data.status == 'begin'){
        window.parent.showMasking();
    } 
    if(data.status == 'success') {
        window.parent.hideMasking();
    }
    checkTaskRequestStatus(data);
}
function checkTaskDoneRequestStatusWrapper(data){
    if(data.status == 'begin'){
        window.parent.showMasking();
    } 
    if(data.status == 'success') {
        window.parent.hideMasking();
    }
    checkTaskDoneRequestStatus(data);
}
function checkTaskDummySaveRequestStatusWrapper(data){
    if(data.status == 'begin'){
        window.parent.showMasking();
    } 
    if(data.status == 'success') {
        window.parent.hideMasking();
    }
    checkTaskDummySaveRequestStatus(data);
}

//Bug 66459 ends

function setUnlockWorkitem(bUnlockWi){
    bUnlockWorkitem = bUnlockWi;
}

function getData(pageNo, typeOfData, callbackFunction)
{
    
    
    var printUrl="";
    var URL_Annotation_print = opall.PARAM.URL_Annotation;
    var URL_ImageStampFile_print = opall.PARAM.URL_StampINIPath;
    var URL_Image_print = opall.PARAM.url_ImageFileName;
    if(typeOfData == '1') {
        printUrl=URL_Annotation_print;
    } else if(typeOfData == '2') {
        printUrl=URL_ImageStampFile_print;
    } else {
        printUrl=URL_Image_print;
    }

    var pageNoIndex = printUrl.toUpperCase().indexOf("PAGENO");
    
    var printUrl_1 = printUrl.substring(0,pageNoIndex); 
    printUrl_1 = printUrl_1 + "PrintWithPrintExe=Y&PageNo=" + pageNo;
    
    var printUrl_2 = printUrl.substring(pageNoIndex);
    printUrl = printUrl_1;
    if(printUrl_2.indexOf("&") != "-1") {
        printUrl_2=printUrl_2.substring(printUrl_2.indexOf("&"));
        printUrl += printUrl_2;
    }
    
    

    var printUrlObj = {
        url:printUrl
    };
    callbackFunction(pageNo, typeOfData, printUrlObj);

}

function onWiSaveMsgClick(){    
    var msgref = document.getElementById('saveConfirmMsg');
    var type = '', subType='';
    try{
        type = JSON.parse(msgref.getAttribute('data')).Type;
        if(typeof type == 'undefined'){
            type = '';
        }
        
        subType = JSON.parse(msgref.getAttribute('data')).SubType;
        if(typeof subType == 'undefined'){
            subType = '';
        }
        
        msgref.removeAttribute('data');
    } catch (e){}
    
    if(type == 'Close'){        
        if(subType=="menuWinClose"){
            var closeValue = handleunlockreq(2,subType);
            if(closeValue != undefined && !closeValue){
                return false;
            }
        } else{    
            handleunlockreq(2,subType);
        }
        
        if(wiproperty.locked=="Y"&&strRemovefrommap=="Y"){          
            removefrommap();
        }
    } else if(type == 'Nav'){
        // Save workitem before Navigation (e.g. prev Next)
        mainSave('mainSave', 'Nav', subType);
        if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm && !bAllDeviceForm){
            
        } else {
            prevNextHandlerMain(subType);
        }
    }
    
    hideWISaveMessage();
}

function onWiCloseMsgClick(){    
    var msgref = document.getElementById('saveConfirmMsg');
    var type = '', subType='';
    try{
        type = JSON.parse(msgref.getAttribute('data')).Type;
        if(typeof type == 'undefined'){
            type = '';
        }
        
        subType = JSON.parse(msgref.getAttribute('data')).SubType;
        if(typeof subType == 'undefined'){
            subType = '';
        }
        
        msgref.removeAttribute('data');
    } catch (e){}
    
    if(type == 'Close'){
        if(subType=="menuWinClose"){
            var closeValue = handleunlockreq(1,subType);
            if(closeValue != undefined && !closeValue){
                return false;
            }
        } else{    
            handleunlockreq(1,subType);
        }
        
        if(wiproperty.locked=="Y"&&strRemovefrommap=="Y"){          
            removefrommap();
        }
    } else if(type == 'Nav'){
        saveCalled = false;
        prevNextHandlerMain(subType);
    }
    
    hideWISaveMessage();
}

function hideWISaveMessage(){
    hidePopupMask();
    document.getElementById('saveConfirmMsg').style.display = 'none';
}

function showWISaveMessage(msg, width, height, left, top, data){    
    var type = '';
    try{
        type = data.Type;
        if(typeof type == 'undefined'){
            type = '';
        }
    } catch (e){}
    
    var ref = null;
    if(type=='Close'){
        ref = document.getElementById('wdesk:saveConfirmMsgPanel:wiClosePG');
        if(ref != null){
            ref.style.display = "";
        }
        ref = document.getElementById('wdesk:saveConfirmMsgPanel:wiNavPG');
        if(ref != null){
            ref.style.display = "none";
        }
    } else if(type == 'Nav'){
        ref = document.getElementById('wdesk:saveConfirmMsgPanel:wiNavPG');
        if(ref != null){
            ref.style.display = "";
        }
        ref = document.getElementById('wdesk:saveConfirmMsgPanel:wiClosePG');
        if(ref != null){
            ref.style.display = "none";
        }
    }
    
    setPopupMask();
                
    width = (typeof width == "undefined")? 250: width;
    height = (typeof height == "undefined")? 80: height;    
    
    left = (typeof left == "undefined" || left == null)? (document.documentElement.clientWidth - width)/2 + window.document.body.scrollLeft: left;
    top = (typeof top == "undefined" || top == null)? (document.documentElement.clientHeight - height)/2 + window.document.body.scrollTop: top;                

    var msgref = document.getElementById('saveConfirmMsg');
    msgref.style.width = width+'px';
    msgref.style.height = height+'px';
    msgref.style.top = top+'px';
    msgref.style.left = left+'px';         
    msgref.setAttribute('data', JSON.stringify(data));
    
    document.getElementById("wdesk:saveConfirmMsgPanel:saveConfirmMsgLabel").innerHTML = msg;      
    
    msgref.style.display = 'block';
    
    if((typeof bLaunchMessage != 'undefined') && (typeof SharingMode != 'undefined') && !SharingMode){
        bLaunchMessage = true;
    }  
    ShowIframe(msgref);
}

function checkSaveWIAlert(type, subType){
    ngformSaveCalled();
    
    if(saveCalled){
        showWISaveMessage(ALERT_CLOSE_SAVE_CONFIRM, 270, 80, null, null, {'Type': type, 'SubType': subType});
        return true;
    } else {
        return false;
    }

}

/*Google Drive integration*/
var scope = ['https://www.googleapis.com/auth/drive'];
var pickerApiLoaded = false;
var oauthToken;
var isImport=false;
var isCheckin=false;
var isSavetoRoot=false;
var m_strValidFileTypesDrive="";
var openInDriveFlag=false;
// Use the Google API Loader script to load the google.picker script.
function loadPicker(flag,validFileTypes) {
    flag = (typeof flag == 'undefined') ? '' : flag;
    validFileTypes=(typeof validFileTypes == 'undefined') ? '' : validFileTypes;
    if(flag=='I'){
        isImport=true;
        m_strValidFileTypesDrive=validFileTypes;
    }
    else if(flag=='CI'){
        isCheckin=true;
        m_strValidFileTypesDrive=validFileTypes;
    }
    else if(flag=='SR'){
        isSavetoRoot=true;
    }
    else if(flag=="OpenInDrive"){
        openInDriveFlag=true;
    }
    gapi.load('picker', {'callback': onPickerApiLoad});
}

function onPickerApiLoad() {
    gapi.load('auth', {'callback': onAuthApiLoad});
}


function onAuthApiLoad() {
    window.gapi.auth.authorize(
            {
                'client_id': clientId,
                'scope': scope,
                'immediate': false
            },
            handleAuthResult);
}

function handleAuthResult(authResult) {
    if (authResult && !authResult.error) {
        oauthToken = authResult.access_token;
        if(isSavetoRoot){
            new net.ContentLoader('/webdesktop/ajaxgooglehandler.jsf', null, null, "POST", 'OPR=setauth&token=' + oauthToken, true);
            hidePopupMask();
            RemoveIndicator("application");
            //saveToGoogleDriveHandler(selectedFolderId);
            //self.close();
        }
        else if(openInDriveFlag){
            new net.ContentLoader('/webdesktop/ajaxgooglehandler.jsf', null, null, "POST", 'OPR=setauth&token=' + oauthToken, true);
            hidePopupMask();
            document.getElementById("Token").value=oauthToken;
            clickLink("openInDrive:saveToDrive");
        }
        else
            new net.ContentLoader('/webdesktop/ajaxgooglehandler.jsf', createPicker, null, "POST", 'OPR=setauth&token=' + oauthToken, true);
    }
}

/*function handleAuthResultCB() {
    createPicker();
}*/
// Create and render a Picker object for searching images.
function createPicker() {
    if (oauthToken) {
        if(isImport){
             var view = new google.picker.View(google.picker.ViewId.DOCS);
            view.setMimeTypes(m_strValidFileTypesDrive+',application/vnd.google-apps.folder,application/vnd.google-apps.document,application/vnd.google-apps.presentation,application/vnd.google-apps.spreadsheet');
            var picker = new google.picker.PickerBuilder()
                .enableFeature(google.picker.Feature.NAV_HIDDEN)
                .setAppId(appId)
                .setOAuthToken(oauthToken)
                .addView(view)
                .setCallback(pickerCallbackImport)
                .build();
        }
        else if(isCheckin){
            var view = new google.picker.View(google.picker.ViewId.DOCS);
            view.setMimeTypes(m_strValidFileTypesDrive+',application/vnd.google-apps.folder,application/vnd.google-apps.document,application/vnd.google-apps.presentation,application/vnd.google-apps.spreadsheet');
            var picker = new google.picker.PickerBuilder()
                .enableFeature(google.picker.Feature.NAV_HIDDEN)
                .setAppId(appId)
                .setOAuthToken(oauthToken)
                .addView(view)
                .setCallback(pickerCallbackCheckin)
                .build();
        }
        else{
           var docsView = new google.picker.DocsView()
                .setIncludeFolders(true) 
                .setMimeTypes('application/vnd.google-apps.folder')
                .setSelectFolderEnabled(true)
                .setOwnedByMe(true)
                .setParent('root');
            var picker = new google.picker.PickerBuilder()
                .enableFeature(google.picker.Feature.NAV_HIDDEN)
                .disableFeature(google.picker.Feature.MULTISELECT_ENABLED)
                .enableFeature(google.picker.Feature.MINE_ONLY)
                .setAppId(appId)
                
                .setOAuthToken(oauthToken)
                .addView(docsView)
                .setCallback(pickerCallback)
                .build();
        }
        picker.setVisible(true);
    }
}
function pickerCallback(data) {
    if (data.action == google.picker.Action.PICKED) {
        var folderId = data.docs[0].id;
        selectedFolderId=folderId;
        var doc=data[google.picker.Response.DOCUMENTS][0];
        selectedFolder=doc[google.picker.Document.NAME];
        updateFields();
        //saveToGoogleDriveHandler(folderId);
    }
}
function pickerCallbackImport(data){
    if (data.action == google.picker.Action.PICKED) {                    
        var ref = document.getElementById("importForm:GDocId");                
        if(ref){
            ref.value = data.docs[0].id;
        }
        ref = document.getElementById("importForm:fileuploadDrive");                
        if(ref){
            ref.value = data.docs[0].name;
        }
        ref = document.getElementById("importForm:GDocName");                
        if(ref){
            ref.value = data.docs[0].name;
        }
        ref = document.getElementById("importForm:GDocSize");                
        if(ref){
            ref.value = data.docs[0].sizeBytes;
        }
        ref = document.getElementById("importForm:GDocUrl");                
        if(ref){
            ref.value = data.docs[0].url;
        }
        ref=document.getElementById("importForm:GDocMimeType"); 
        if(ref){
            ref.value=data.docs[0].mimeType;
        }
    }
}
function pickerCallbackCheckin(data)
{
    if (data.action == google.picker.Action.PICKED) {                    
        var ref = document.getElementById("checkInForm:GDocId");                
        if(ref){
            ref.value = data.docs[0].id;
        }
        ref = document.getElementById("checkInForm:fileuploadDrive");                
        if(ref){
            ref.value = data.docs[0].name;
        }
        ref = document.getElementById("checkInForm:GDocName");                
        if(ref){
            ref.value = data.docs[0].name;
        }
        ref = document.getElementById("checkInForm:GDocSize");                
        if(ref){
            ref.value = data.docs[0].sizeBytes;
        }
        ref = document.getElementById("checkInForm:GDocUrl");                
        if(ref){
            ref.value = data.docs[0].url;
        }
        ref=document.getElementById("checkInForm:GDocMimeType"); 
        if(ref){
            ref.value=data.docs[0].mimeType;
        }
    }
}
function saveToDrive(){
    var left = (window.screen.width - window8W) / 2;
    var top = (document.documentElement.clientHeight - window8H) / 2;
    var url = '/webdesktop/components/workitem/document/savetogdriveoption.jsf';
    var wFeatures = 'status=yes,resizable=no,scrollbars=yes,width=' + window8W + ',height=' + window8H + ',left=' + left + ',top=' + top + ',resizable=yes,scrollbars=yes';
    
    var objCombo = document.getElementById('wdesk:docCombo');
    if (objCombo) {
        var docIndex = objCombo.options[objCombo.options.selectedIndex].value
        var tmpParsedDocJSON = eval("(" + document.getElementById('wdesk:docInfoJSON').value + ")");
        var docInfoJSON = tmpParsedDocJSON[docIndex];

        if (docInfoJSON) {
            var listParam = new Array();
            listParam.push(new Array('Action', 1));
            listParam.push(new Array('ImgIndex', encode_ParamValue(docInfoJSON.document[0].ImgIndex)));
            listParam.push(new Array('VolIndex', encode_ParamValue(docInfoJSON.document[0].VolIndex)));
            listParam.push(new Array('DocOrgName', encode_ParamValue(docInfoJSON.document[0].DocOrgName)));
            listParam.push(new Array('DocExt', encode_ParamValue(docInfoJSON.document[0].DocExt)));
            listParam.push(new Array('DocIndex', encode_ParamValue(docInfoJSON.document[0].DocIndex)));
            listParam.push(new Array('DocName', encode_ParamValue(docInfoJSON.document[0].DocName)));
            listParam.push(new Array('ISIndex', encode_ParamValue(docInfoJSON.document[0].ISIndex)));
            listParam.push(new Array('Flag','S'));
            listParam.push(new Array('Token', encode_ParamValue(oauthToken)));
            //listParam.push(new Array('FolderId', encode_ParamValue(folderId)));
            
            openNewWindow(url, 'SaveToGoogleDriveOptions', wFeatures, true, "Ext1", "Ext2", "Ext3", "Ext4", listParam);
        }
    }
}

function saveToGoogleDriveHandler(folderId) {
    var windowW = 600;
    var windowH = 300;
    var left = (window.screen.width - windowW) / 2;
    var top = (window.screen.height - windowH) / 2;
    var url = '/webdesktop/components/workitem/document/savetogdrive.jsf';
    var wFeatures = 'status=yes,resizable=no,scrollbars=yes,width=' + windowW + ',height=' + windowH + ',left=' + left + ',top=' + top + ',resizable=yes,scrollbars=yes';
    var listParam = new Array();
    listParam.push(new Array('Action', 1));
    listParam.push(new Array('ImgIndex', imgIndex));
    listParam.push(new Array('VolIndex',volIndex));
    listParam.push(new Array('DocOrgName',docOrgName));
    listParam.push(new Array('DocExt',docExt));
    listParam.push(new Array('DocIndex',docIndex));
    listParam.push(new Array('DocName',docName));
    listParam.push(new Array('ISIndex',isIndex));
    listParam.push(new Array('Token', encode_ParamValue(oauthToken)));
    listParam.push(new Array('FolderId', encode_ParamValue(folderId)));
    listParam.push(new Array('isCheckOut',saveFlag=='C'?true:false))
    if(saveFlag!=undefined){
        if(saveFlag=="C"){
            window.opener.docProperty(docIndex,window.opener.pid,window.opener.wid,window.opener.taskid);
            window.opener.checkOutDocHook(docIndex,docName);
            if(window.opener.windowProperty.winloc=="T")
                window_workdesk=window.opener.opener.opener;
            else
                window_workdesk=window.opener.opener;
            if(window_workdesk){
                if(window_workdesk.SharingMode)
                    window_workdesk.broadcastCheckOutDocEvent(docName,window.opener.docType);
            }
        }
    }
    window.opener.openNewWindow(url, 'SaveToGoogleDriveId', wFeatures, true, "Ext1", "Ext2", "Ext3", "Ext4", listParam);
}

function openInDrive(){
    var windowW = 600;
    var windowH = 600;
    var left = (window.screen.width - windowW) / 2;
    var top = (window.screen.height - windowH) / 2;
    var url = '/webdesktop/components/workitem/document/openInDrive.jsf';
    var wFeatures = 'status=yes,resizable=no,scrollbars=yes,width=' + windowW + ',height=' + windowH + ',left=' + left + ',top=' + top + ',resizable=yes,scrollbars=yes';
    var listParam = new Array();
    var objCombo = document.getElementById('wdesk:docCombo');
    if (objCombo) {
        var docIndex = objCombo.options[objCombo.options.selectedIndex].value
        var tmpParsedDocJSON = eval("(" + document.getElementById('wdesk:docInfoJSON').value + ")");
        var docInfoJSON = tmpParsedDocJSON[docIndex];
        if (docInfoJSON) {
            listParam.push(new Array('Action', 1));
            listParam.push(new Array('ImgIndex', encode_ParamValue(docInfoJSON.document[0].ImgIndex)));
            listParam.push(new Array('VolIndex',encode_ParamValue(docInfoJSON.document[0].VolIndex)));
            listParam.push(new Array('DocOrgName',encode_ParamValue(docInfoJSON.document[0].DocOrgName)));
            listParam.push(new Array('DocExt',encode_ParamValue(docInfoJSON.document[0].DocExt)));
            listParam.push(new Array('DocIndex',encode_ParamValue(docInfoJSON.document[0].DocIndex)));
            listParam.push(new Array('DocName',encode_ParamValue(docInfoJSON.document[0].DocName)));
            listParam.push(new Array('ISIndex',encode_ParamValue(docInfoJSON.document[0].ISIndex)));
            if(isEmbd=='Y'){
                openNewWindow(url, 'openInDrive:'+document.getElementById("wdesk:pid").value, wFeatures, true, "Ext1", "Ext2", "Ext3", "Ext4", listParam);
            }
            else
                window.opener.openNewWindow(url, 'openInDrive:'+document.getElementById("wdesk:pid").value, wFeatures, true, "Ext1", "Ext2", "Ext3", "Ext4", listParam);
        }
    }
    else{
        listParam.push(new Array('Action', 1));
        listParam.push(new Array('ImgIndex', encode_ParamValue("")));
        listParam.push(new Array('VolIndex',encode_ParamValue("")));
        listParam.push(new Array('DocOrgName',encode_ParamValue(DocOrgName)));
        listParam.push(new Array('DocExt',encode_ParamValue(DocExt)));
        listParam.push(new Array('DocIndex',encode_ParamValue(strDocIndex)));
        listParam.push(new Array('DocName',encode_ParamValue(DocName)));
        listParam.push(new Array('ISIndex',encode_ParamValue(ISIndex)));
        window.opener.openNewWindow(url, 'openInDrive:'+pid, wFeatures, true, "Ext1", "Ext2", "Ext3", "Ext4", listParam);
    }
    return false;
}

function hideIframe(id){
    var IfrRef = document.getElementById(id);
    if(typeof IfrRef!= 'undefined' && IfrRef!=null){
        IfrRef.style.display = "none";
    }
}
function ShowIframe(ref)
{
    var IfrRef = document.getElementById('HiddenDiv');
    if(typeof IfrRef!= 'undefined' && IfrRef!=null){
        IfrRef.style.width = ref.offsetWidth;
        IfrRef.width = ref.offsetWidth;
        IfrRef.style.height = ref.offsetHeight;
        IfrRef.height = ref.offsetHeight;
        IfrRef.style.top = ref.style.top;
        IfrRef.style.left = ref.style.left;
        IfrRef.style.zIndex = ref.style.zIndex - 1;
        IfrRef.style.display = "";
    }
}

function jsfformvalidation(eventType,validated,bringNextWorkItem,isDummySave){
    isDummySave = (typeof isDummySave == 'undefined')? "N": isDummySave;
    var status = (validated == "true")? "success": "failure";
    var data = {"status":status,"EventType":eventType,"Validated":validated,"BringNextWorkItem":bringNextWorkItem};
    
    if((eventType == "S") && (isDummySave == "Y")){        
        checkDummySaveRequestStatus(data);
        WiDummySaveStruct = {Type:'',Params:{}};
    } else if(eventType == "S"){
        checkRequestPostStatus(data);
    } else if(eventType == "I"){
        checkIntroduceRequestPostStatus(data);
    } else if(eventType == "D"){
        checkDoneRequestPostStatus(data);
    }
}

function onNGHTMLFormLoad(){   
    if(formIntegrationApr == "4"){
        try{
            var ngformIframe = document.getElementById("ngformIframe");
            ngformIframe.contentWindow.document.getElementById("ngform").style.width = ngformIframe.style.width;
            ngformIframe.contentWindow.document.getElementById("ngform").style.height = ngformIframe.style.height;

            /*ngformIframe.contentWindow.$.mCustomScrollbar.defaults.scrollButtons.enable = true; //enable scrolling buttons by default
            ngformIframe.contentWindow.$.mCustomScrollbar.defaults.axis = "yx";
            ngformIframe.contentWindow.$.mCustomScrollbar.defaults.autoHideScrollbar = true;                
            ngformIframe.contentWindow.$("#ngform").mCustomScrollbar({theme: "dark"});

            var docEle = ngformIframe.contentWindow.$("#ngform").find(".mCSB_container")[0];
            if(docEle){
                var elementHeightWidth = getElementHeightWidth(ngformIframe.contentWindow.document.getElementById("ngform"));                 
                docEle.style.height = elementHeightWidth.Height + "px";     
                docEle.style.width = elementHeightWidth.Width + "px";
            }*/
        }catch(e){}
    }
}

function checkRequestPostStatus(data){
    if(data.status=='success'){
        var ngformIframe = document.getElementById("ngformIframe");
        ngformIframe.contentWindow.saveForm("S","SPH"); // DPH --> Save Post Hook
    }
}

function checkIntroduceRequestPostStatus(data){
    if(data.status=='success'){
        var ngformIframe = document.getElementById("ngformIframe");
        ngformIframe.contentWindow.saveForm("I","IPH"); // DPH --> Introduce Post Hook
    }
}

function checkDoneRequestPostStatus(data){
    if(data.status=='success'){
        var ngformIframe = document.getElementById("ngformIframe");
        ngformIframe.contentWindow.saveForm("D","DPH"); // DPH --> Done Post Hook
    }
}

function formSaved(eventType, param){
    param = (typeof param == 'undefined')? "": param;
    var data = {"status":"success","EventType":eventType};
    
    if(eventType == "S"){
        checkRequestStatus(data);
    } else if(eventType == "I"){
        checkIntroduceRequestStatus(data);
    } else if(eventType == "D"){
        checkDoneRequestStatus(data);
    }
    
    WiDummySaveStruct = {Type:'',Params:{}};
}

function uploadDocument() {
    var fileSelect = document.getElementById('fileupload');
    var files = fileSelect.files;
    var formData = new FormData();
    var responseOutput;
    var responseJSON;
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        formData.append('filesData', file, file.name);
    }
    var url = sContextPath+"/servlet/filehandler";
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, false);
    xhr.send(formData);
    if(xhr.status == 200 && xhr.readyState == 4) {
        responseOutput = xhr.responseText;
        responseJSON = JSON.parse(responseOutput);
        document.getElementById("importForm:hidFileName").value = responseJSON.FileName;
        document.getElementById("importForm:hidFilePath").value = responseJSON.FilePath;
        document.getElementById("importForm:hidCreateDirectoryStatus").value = responseJSON.CreateDirectoryStatus;
    }
}