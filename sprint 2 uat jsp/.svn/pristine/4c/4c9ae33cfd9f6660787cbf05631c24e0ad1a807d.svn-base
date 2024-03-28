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

function showPopupMask()
{
    if(gPopupMask != null) {
        gPopupMask.style.display = "block";
        gPopupMask.style.height = window.document.documentElement.clientHeight + document.documentElement.scrollTop + "px";        
        gPopupMask.style.width = document.documentElement.clientWidth + document.documentElement.scrollLeft + "px";
    }
}

function hidePopupMask()
{
    if(gPopupMask != null) {
        gPopupMask.style.display = "none";        
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
var MOVE_STEP = 1;
function setPositionForDrag(e, contDivId){                
    var curEvent = (typeof event == 'undefined'? e: event);       
    lastPosX = curEvent.clientX + document.documentElement.scrollLeft;    
    lastPosY = curEvent.clientY + document.documentElement.scrollTop;
    lastResizePosX = lastPosX;
    lastResizePosY = lastPosY;   

    srcElementId = contDivId;
    var ref = document.getElementById(srcElementId);    
    repositionMoveDiv(ref);    

    isDragMode = true;    
    mouseStatus='down';
    showPopupMask();
}

function setPosition(e, contDivId, direction){
    var curEvent = (typeof event == 'undefined'? e: event);        
    lastPosX = curEvent.clientX + document.documentElement.scrollLeft;    
    lastPosY = curEvent.clientY + document.documentElement.scrollTop;   
    lastResizePosX = lastPosX;
    lastResizePosY = lastPosY;         

    srcElementId = contDivId;
    var ref = document.getElementById(srcElementId);
    repositionMoveDiv(ref);    

    moveDir = direction;
    mouseStatus = 'down';
    showPopupMask();
}

 function getPosition(e){    
    if(mouseStatus == 'down'){
        var ref = document.getElementById(srcElementId);   

        if(isDragMode){            
            ref.style.top = parseInt(ref.style.top.split('p')[0]) + (lastResizePosY - lastPosY) + "px";
            ref.style.left = parseInt(ref.style.left.split('p')[0]) + (lastResizePosX - lastPosX) + "px";            
        } else {
             var newWidth = 0;
             var newHeight = 0;
            if(moveDir == "both") {
                newWidth = parseInt(ref.style.width.split('p')[0]) + (lastResizePosX - lastPosX);
                newHeight = parseInt(ref.style.height.split('p')[0]) + (lastResizePosY - lastPosY);

                ref.style.width = newWidth + "px";
                ref.style.height = newHeight + "px";
            } else if(moveDir == "ver") {        

                newWidth = parseInt(ref.style.width.split('p')[0]) + (lastResizePosX - lastPosX);
                ref.style.width = newWidth + "px";
            } else if(moveDir == "hor") {
                newHeight = parseInt(ref.style.height.split('p')[0]) + (lastResizePosY - lastPosY);
                ref.style.height = newHeight + "px";
            }
        }        
        
        if(interfaceMap.get(srcElementId).value != null){
            setIntPosition(interfaceMap.get(srcElementId).value, ref); 
        }
        ref = document.getElementById("moveDiv");
        ref.style.display = "none";                        
    }    

    isDragMode = false;
    mouseStatus = 'up';
    moveDir = "";        
    hidePopupMask();
}

var CONT_WIDTH_LIMIT = 150;
var CONT_HEIGHT_LIMIT = 25;
var HORIZONTAL_LIMIT_MARGIN = 2;
var VERTICAL_LIMIT_MARGIN = 0;
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
            var srcElementRef = document.getElementById(srcElementId);   
            var isTopLimit = false; 
            var isLeftLimit = false; 
            var isRightLimit = false; 
            var isBottomLimit = false; 
            var contDivRef = document.getElementById("containerDiv");                   
            
            if(isDragMode){                
                var posX = parseInt(ref.style.left.split('p')[0]) + movePixX;                
                var posY = parseInt(ref.style.top.split('p')[0]) + movePixY;                                

                if(posX >= (contDivRef.offsetLeft+HORIZONTAL_LIMIT_MARGIN)) {
                    isLeftLimit = false;
                } else {
                    isLeftLimit = true;
                }
                
                if((posX + ref.clientWidth) <= (contDivRef.offsetLeft+contDivRef.clientWidth-HORIZONTAL_LIMIT_MARGIN)) {                    
                    isRightLimit = false;
                } else {
                    isRightLimit = true;
                }
                
                if(!isLeftLimit && !isRightLimit){
                    ref.style.left = posX + "px";                
                }

                if(posY >= (contDivRef.offsetTop+VERTICAL_LIMIT_MARGIN)) {                    
                    isTopLimit = false;
                } else {
                    isTopLimit = true;
                }
                
                if((posY + ref.clientHeight) <= (contDivRef.offsetTop+contDivRef.clientHeight-VERTICAL_LIMIT_MARGIN)) {                    
                    isBottomLimit = false;
                } else {
                    isBottomLimit = true;
                }
                
                if(!isTopLimit && !isBottomLimit){
                    ref.style.top = posY + "px";                
                }
            } else {
                var height = parseInt(ref.style.height.split('p')[0]);
                var width = parseInt(ref.style.width.split('p')[0]);
                var srcElemenetTop = parseInt(srcElementRef.style.top.split('p')[0]);
                var srcElemenetLeft = parseInt(srcElementRef.style.left.split('p')[0]);
                
                if(moveDir == "both") {     
                    if(((srcElemenetLeft + width + movePixX) > (srcElemenetLeft + CONT_WIDTH_LIMIT)) && ((srcElemenetLeft + width + movePixX) < (contDivRef.offsetLeft+contDivRef.clientWidth-HORIZONTAL_LIMIT_MARGIN))){                        
                        isLeftLimit = false;
                    } else {
                        isLeftLimit = true;
                    } 
                    
                    if(!isLeftLimit){
                        ref.style.width = width + movePixX + "px";
                    }
                    
                    if(((srcElemenetTop + height + movePixY) > (srcElemenetTop + CONT_HEIGHT_LIMIT)) && ((srcElemenetTop + height + movePixY) < (contDivRef.offsetTop+contDivRef.clientHeight-VERTICAL_LIMIT_MARGIN+1))){                        
                        isTopLimit = false;
                    } else {
                        isTopLimit = true;
                    }
                    
                    if(!isTopLimit){
                        ref.style.height = height + movePixY + "px";
                    }
                } else if(moveDir == "ver") { 
                    if(((srcElemenetLeft + width + movePixX) > (srcElemenetLeft + CONT_WIDTH_LIMIT)) && ((srcElemenetLeft + width + movePixX) < (contDivRef.offsetLeft+contDivRef.clientWidth-HORIZONTAL_LIMIT_MARGIN))){
                        isLeftLimit = false;
                    } else {
                        isLeftLimit = true;
                    }
                    
                    if(!isLeftLimit){
                        ref.style.width = width + movePixX + "px";
                    }
                } else if(moveDir == "hor") {                     
                    if(((srcElemenetTop + height + movePixY) > (srcElemenetTop + CONT_HEIGHT_LIMIT)) && ((srcElemenetTop + height + movePixY) < (contDivRef.offsetTop+contDivRef.clientHeight-VERTICAL_LIMIT_MARGIN))){
                        isTopLimit = false;
                    } else {
                        isTopLimit = true;
                    }
                    
                    if(!isTopLimit){
                        ref.style.height = height + movePixY + "px";  
                    }
                }
            }

            if(!isLeftLimit && !isRightLimit){
                lastResizePosX += movePixX;
            }            
            if(!isTopLimit && !isBottomLimit){
                lastResizePosY += movePixY;
            }
        }
    }
}

function onFixedIntListClick(event, selectedContainer){
    var srcElement = (event != undefined)? event.target: window.event.srcElement;
    showFixedInterfaceList(srcElement.id.split('_')[1], selectedContainer);
}          

function renderLayout(){    
    if((wDeskLayoutJason != undefined) && (wDeskLayoutJason != null) && (wDeskLayoutJason != "")){        
        for(var i=0; i<wDeskLayoutJason.WDeskEditableLayout.Interfaces.length; i++){
            addContainer(wDeskLayoutJason.WDeskEditableLayout.WdeskFlag);
            interfaceMap.put("div_C"+(i+1), wDeskLayoutJason.WDeskEditableLayout.Interfaces[i].Interface);
        }
    }
}

var iMaxContainerNo = 0;
var INT_HEIGHT = 120;
var INT_WIDTH = 210;
function addContainer(wdeskFlag){
    iMaxContainerNo=iMaxContainerNo + 1;
    var containerid=iMaxContainerNo;    

    var contDivRef = document.getElementById("containerDiv");       
    var contDivLeft = findPosX(contDivRef);
    var contDivTop = findPosY(contDivRef);
    
    // Main interface container
    var ref = window.document.createElement("div");   
    ref.setAttribute("id", "div_C"+containerid);
    ref.className = "interface";
    ref.style.overflow= 'hidden';
    ref.style.position="absolute";  
    if((wdeskFlag != undefined) && (wdeskFlag != null) && (wdeskFlag == "U")){
        var screenWidth = "";
        var screenHeight = "";
        if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
            //IE 6+ in 'standards compliant mode'        
            screenWidth = document.documentElement.clientWidth;
            screenHeight = document.documentElement.clientHeight;
        } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {        
            //IE 4 compatible
            screenWidth = document.body.clientWidth;
            screenHeight = document.body.clientHeight;
        }
        
        if(wDeskLayoutJason.WDeskEditableLayout.IsDefaultLayout == "true"){
            wDeskLayoutJason.WDeskEditableLayout.ScreenWidth = screenWidth;
            wDeskLayoutJason.WDeskEditableLayout.ScreenHeight = screenHeight;
            if(wDeskLayoutJason.WDeskEditableLayout.IsTask == "false"){
                if(iMaxContainerNo == 1){
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Left = 0;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Top = 0;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Width = screenWidth/2 - 6;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Height = screenHeight - 35;
                } else if(iMaxContainerNo == 2){
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Left = screenWidth/2 + 4;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Top = 0;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Width = screenWidth/2 - 8;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Height = screenHeight - 35;
                }             
            }else{
                if(iMaxContainerNo == 1){
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Left = screenWidth/3 + 8;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Top = 0;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Width = (screenWidth/3 * 2) - 25;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Height = (screenHeight - 50)/2;
                } else if(iMaxContainerNo == 2){
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Left = screenWidth/3 + 8;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Top = ((screenHeight - 35)/2) + 3;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Width = (screenWidth/3 * 2) - 25;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Height = (screenHeight - 50)/2;
                } else if(iMaxContainerNo == 3){
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Left = 0;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Top = ((screenHeight - 35)/2) + 3;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Width = screenWidth/3;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Height = (screenHeight - 50)/2;
                } else if(iMaxContainerNo == 4){
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Left = 0;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Top = 0;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Width = screenWidth/3;
                    wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Height = (screenHeight - 50)/2;
                }
            }
        }
        
        ref.style.left = contDivLeft + (((wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Left)*screenWidth)/wDeskLayoutJason.WDeskEditableLayout.ScreenWidth) + "px";
        ref.style.top = contDivTop + (((wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Top)*screenHeight)/wDeskLayoutJason.WDeskEditableLayout.ScreenHeight) + "px";
        ref.style.width = (wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Width*screenWidth)/wDeskLayoutJason.WDeskEditableLayout.ScreenWidth + "px";
        ref.style.height = (wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Height*screenHeight)/wDeskLayoutJason.WDeskEditableLayout.ScreenHeight + "px";
        setIntPosition(wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface, ref);
        
        /*ref.style.top = layoutJason.Interfaces[containerid-1].Interface.Top + "px";
        ref.style.left = layoutJason.Interfaces[containerid-1].Interface.Left + "px";
        ref.style.width = layoutJason.Interfaces[containerid-1].Interface.Width + "px";
        ref.style.height = layoutJason.Interfaces[containerid-1].Interface.Height + "px";*/
    } else {
        ref.style.top = contDivTop + "px";
        ref.style.left = contDivLeft + "px";
        ref.style.width = INT_WIDTH + "px";
        ref.style.height = INT_HEIGHT + "px";     
    }
    ref.style.textAlign = 'right';    
    contDivRef.appendChild(ref);    
    
    // Header interface container
    var headerref = window.document.createElement("div");  
    headerref.setAttribute("id", "headerDiv_C"+containerid);
    //headerref.className = "wdheaderclass1";    
    headerref.style.overflow= 'hidden';
    headerref.style.position="absolute";  
    headerref.style.textAlign = 'left';    
    headerref.style.top = "0px";
    headerref.style.left = "0px";
    headerref.style.width = "100%";    
    headerref.style.height = 19 + "px";
    headerref.style.paddingLeft = "5px";
    headerref.style.paddingTop = "3px";
    headerref.style.borderBottom = "1px solid #dddddd";
    
    headerref.onmouseover = function (event){
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        srcElement.className = 'cursorMove';
    }
    
    headerref.onmouseout = function (event){
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        srcElement.className = 'cursorSimple';
    }
    
    if((wdeskFlag != undefined) && (wdeskFlag != null) && (wdeskFlag == "U")){
        headerref.innerHTML = wDeskLayoutJason.WDeskEditableLayout.Interfaces[containerid-1].Interface.Type;
    }
    
    headerref.onmousedown = function(event){
        event = (event != undefined)? event: window.event; 
        setPositionForDrag(event, "div_C"+containerid);
    }      
    ref.appendChild(headerref);       
    addContainerExternal(ref,containerid,wdeskFlag);
}

function addContainerExternal(parentRef,containerid, wdeskFlag){
    // Vertical resize
    var childRef = window.document.createElement("div");
    childRef.setAttribute("id", "divResizeVer_C"+containerid);
    childRef.style.overflow= 'hidden';
    childRef.className='resizablever';    
    childRef.onmousedown = function(event){
        event = (event != undefined)? event: window.event; 
        setPosition(event, "div_C"+containerid, "ver");
    }    
    childRef.onmouseover = function(event){        
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        changeResizeColor(srcElement,'#BFDCFC');
    }        
    childRef.onmouseout = function(event){
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        changeResizeColor(srcElement,'#ededed');
    }
    parentRef.appendChild(childRef);    

    // Horizontal resize
    childRef = window.document.createElement("div");
    childRef.setAttribute("id", "divResizeHor_C"+containerid);
    childRef.style.overflow= 'hidden';
    childRef.className='resizablehor';    
    childRef.onmousedown = function(event){
        event = (event != undefined)? event: window.event; 
        setPosition(event, "div_C"+containerid, "hor");
    }    
    childRef.onmouseover = function(event){               
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        changeResizeColor(srcElement,'#BFDCFC');
    }        
    childRef.onmouseout = function(event){
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        changeResizeColor(srcElement,'#ededed');
    }
    parentRef.appendChild(childRef);  

    // Vertical and Horizontal resize
    childRef = window.document.createElement("div");
    childRef.setAttribute("id", "divResize_C"+containerid);
    childRef.style.overflow= 'hidden';
    childRef.className='resizable';    
    childRef.onmousedown = function(event){
        event = (event != undefined)? event: window.event; 
        setPosition(event, "div_C"+containerid, "both");
    }    
    childRef.onmouseover = function(event){        
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        changeResizeColor(srcElement,'#BFDCFC');
    }        
    childRef.onmouseout = function(event){
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        changeResizeColor(srcElement,'#ededed');
    }
    parentRef.appendChild(childRef);      
    
    // Interface menu
    childRef = window.document.createElement("div");
    childRef.setAttribute("id", "intList_C"+containerid);    
    childRef.style.cursor="pointer";
    childRef.className = "arrowDown";
    
    if((wdeskFlag != undefined) && (wdeskFlag != null) && (wdeskFlag == "U")){
        childRef.style.display="none";
    } else {
        childRef.style.display="inline";
    }
    
    childRef.onclick = function(event){
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        showInterfaceList(srcElement.id.split('_')[1]);
    }          
    parentRef.appendChild(childRef);  
    
    // Close interface
    childRef = window.document.createElement("img");
    childRef.setAttribute("id", "closeimg_C"+containerid);
    childRef.style.paddingRight = "5px";
    childRef.style.paddingBottom = "1px";
    childRef.style.paddingLeft = "4px";
    childRef.style.paddingTop = "5px";
    childRef.style.width="12px";
    childRef.style.height="12px";
    childRef.style.cursor="pointer";
    childRef.style.position="relative";
    childRef.style.cursor="inline-block";
    childRef.onclick = function(event){
        var srcElement = (event != undefined)? event.target: window.event.srcElement;
        closeInterface(srcElement.id.split('_')[1]);
    }      
    childRef.src = "/webdesktop/resources/images/closehw.png";
    parentRef.appendChild(childRef);  
}

function findPosX(obj){
    var curleft = 0;
    if(obj.offsetParent)
        while(1)
        {
            curleft += obj.offsetLeft;
            if(!obj.offsetParent)
                break;
            obj = obj.offsetParent;
        }
    else if(obj.x)
        curleft += obj.x;
    return curleft;
}

function findPosY(obj){
    var curtop = 0;
    if(obj.offsetParent)
        while(1)
        {
            curtop += obj.offsetTop;
            if(!obj.offsetParent)
                break;
            obj = obj.offsetParent;
        }
    else if(obj.y)
        curtop += obj.y;
    return curtop;
}

function changeResizeColor(ref, color){    
    ref.style.backgroundColor = color;
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

function onRowMouseOver(ref)
{
    ref.style.backgroundColor = "#e8e8e8";
    ref.style.cursor = "pointer";
}

function onRowMouseOut(ref)
{
    ref.style.backgroundColor = "";
}

var selectedContId = "";
function showInterfaceList(contId){
    selectedContId = contId;    
    var ref = document.getElementById("intList_"+contId);        
    var listDivRef = document.getElementById("interfaceListDiv");
    listDivRef.style.top = parseInt(findPosY(ref)) + 16 + "px";
    listDivRef.style.left = parseInt(findPosX(ref)) + 6 - parseInt(listDivRef.style.width.split('p')[0]) + "px";    
    listDivRef.style.display = "block";
}

var selectedFixedContId = "";
function showFixedInterfaceList(contId, selectedContainer){  
    selectedFixedContId = contId;
    var ref = document.getElementById("intList_"+contId);        
    var listDivRef = document.getElementById("fixedInterfaceListDiv");
    listDivRef.style.top = parseInt(findPosY(ref)) + 16 + "px";
    listDivRef.style.left = parseInt(findPosX(ref)) + 6 - parseInt(listDivRef.style.width.split('p')[0]) + "px";    
        
    listDivRef.style.display = "block";
    document.getElementById("selectedContainer").value = selectedContainer;
}

function interfaceAddHandler(){
    var selectedIntName = document.getElementById("selectedIntName").value
    // Hides interface menu
    var ref = document.getElementById("intList_"+selectedContId);   
    ref.style.display = "none";
    
    // Names interface header
    ref = document.getElementById("headerDiv_"+selectedContId);   
    ref.innerHTML = selectedIntName;
    
    ref = document.getElementById("div_"+selectedContId);
     var intJason = {
         "Type": selectedIntName,
         "Left": ref.style.left.split('p')[0],
         "Top": ref.style.top.split('p')[0],
         "Width": ref.style.width.split('p')[0],
         "Height": ref.style.height.split('p')[0]
     }

     interfaceMap.put("div_"+selectedContId, intJason);
}

function closeInterface(contId){
    selectedContId = contId;    
    var ref = document.getElementById("headerDiv_"+selectedContId);   
    document.getElementById("selectedIntName").value = ref.innerHTML; 
    if(ref.innerHTML == ""){
        ref = document.getElementById("div_"+selectedContId);  
        try{
            document.getElementById("containerDiv").removeChild(ref);
            interfaceMap.remove("div_"+selectedContId);
        }catch(e){
            //alert(e);
        }
    } else {
        clickLink("closeIntBtn");
    }
}

function interfaceCloseHandler(){
    var ref = document.getElementById("intList_"+selectedContId);   
    ref.style.display = "inline";
    
    ref = document.getElementById("headerDiv_"+selectedContId);   
    ref.innerHTML = "";
    
    interfaceMap.remove("div_"+selectedContId);
}

function onSave(){
    var layoutType = document.getElementById("m_strLayoutType").value;  
    var ref = document.getElementById("containerDiv");
    if(layoutType == "F"){
        ref = document.getElementById("fixedContainerDiv");
    }
    
    var contDivTop = findPosY(ref);
    var contDivLeft = findPosX(ref);
        
    if(!bEditableLayoutLoaded){  
        for(var i=0; i<wDeskLayoutJason.WDeskEditableLayout.Interfaces.length; i++){            
            interfaceMap.put("div_C"+(i+1), wDeskLayoutJason.WDeskEditableLayout.Interfaces[i].Interface);
        }
        
        contDivTop = 0;
        contDivLeft = 0;
    }
    
    var intArrayRef = interfaceMap.getIterator();
    
    var wDeskInterfacesXML = "<WDeskInterfaces>";
    for(var i=0; i<intArrayRef.length; i++){        
        var interfaceXML = "<Interface";        
        interfaceXML = interfaceXML+" Type='"+intArrayRef[i].value.Type+"' Top='"+(intArrayRef[i].value.Top-contDivTop)+"' Left='"+(intArrayRef[i].value.Left-contDivLeft)+
        "' Width='"+intArrayRef[i].value.Width+"' Height='"+intArrayRef[i].value.Height+"'/>"
        wDeskInterfacesXML += interfaceXML;
    }
    
    wDeskInterfacesXML += "</WDeskInterfaces>";
    document.getElementById("wDeskInterfacesXML").value = wDeskInterfacesXML;
    setResolution();
    clickLink("saveBtn");
}

function setIntPosition(intJason, divRef){
    intJason.Left = divRef.style.left.split('p')[0];
    intJason.Top = divRef.style.top.split('p')[0];
    intJason.Width = divRef.style.width.split('p')[0];
    intJason.Height = divRef.style.height.split('p')[0];
}

function setResolution(){
    var width = "";
    var height = "";
    if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
        //IE 6+ in 'standards compliant mode'        
        width = document.documentElement.clientWidth;
        height = document.documentElement.clientHeight;
    } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {        
        //IE 4 compatible
        width = document.body.clientWidth;
        height = document.body.clientHeight;
    }    
    document.getElementById("screenWidth").value = width;
    document.getElementById("screenHeight").value = height;
}

function  hideOnDocumentClick(e){
    var eventTarget = (e && e.target) || (event && event.srcElement);
    var listDivRef = document.getElementById("interfaceListDiv");
    var fixedListDivRef = document.getElementById("fixedInterfaceListDiv");
    if(isRemovable(eventTarget)){    
        if(listDivRef.style.visibility == 'visible'){
            listDivRef.style.visibility = 'hidden';
        } else if((listDivRef.style.display == 'inline') || (listDivRef.style.display == 'block')){
            listDivRef.style.display = 'none';
        }
        
        if(fixedListDivRef.style.visibility == 'visible'){
            fixedListDivRef.style.visibility = 'hidden';
        } else if((fixedListDivRef.style.display == 'inline') || (fixedListDivRef.style.display == 'block')){
            fixedListDivRef.style.display = 'none';
        }
    }
}

function isRemovable(eventTarget) {
    var tempParentObj = eventTarget;
    var ref = document.getElementById("intList_"+selectedContId);   
    var ref2 = document.getElementById("intList_"+selectedFixedContId);   
    while(tempParentObj !=null) {
        if((tempParentObj == ref) || (tempParentObj == ref) || (tempParentObj == ref2) || (tempParentObj == ref2) ){
            return false;
        }
        tempParentObj = tempParentObj.parentNode;
    }
    
    return true;
}

function clickLink(linkId)
{
    if(!linkId)
    {
        return false;
    }

    var fireOnThis = document.getElementById(linkId);
    
    if(fireOnThis != null && (fireOnThis.nodeName.toUpperCase() == "A")){
        if(fireOnThis.href.lastIndexOf("#") > -1){
            fireOnThis.href = "javascript:void(0)";
        }
    }
    
    
    if (document.createEvent)
    {
        //var evObj = document.createEvent('MouseEvents') ;
        //evObj.initEvent( 'click', true, false ) ;
        //fireOnThis.dispatchEvent(evObj) ;
        var evObj = document.createEvent('MouseEvents') ;
        evObj.initMouseEvent("click", false, false, window,0, 0, 0, 0, 0, false, false, false, false, 0, null);
        fireOnThis.dispatchEvent(evObj) ;
    }
    else if (document.createEventObject)
    {
        fireOnThis.click()  ;

    }
    return ;
}

function clickLinkBubbleEvt(linkId)
{
    if(!linkId)
    {
        return false;
    }

    var fireOnThis = document.getElementById(linkId);
    
    if(fireOnThis != null && (fireOnThis.nodeName.toUpperCase() == "A")){
        if(fireOnThis.href.lastIndexOf("#") > -1){
            fireOnThis.href = "javascript:void(0)";
        }
    }
    
    if (document.createEvent)
    {
        //var evObj = document.createEvent('MouseEvents') ;
        //evObj.initEvent( 'click', true, false ) ;
        //fireOnThis.dispatchEvent(evObj) ;
        var evObj = document.createEvent('MouseEvents') ;
        evObj.initMouseEvent("click", true, true, window,0, 0, 0, 0, 0, false, false, false, false, 0, null);
        fireOnThis.dispatchEvent(evObj) ;
    }
    else if (document.createEventObject)
    {
        fireOnThis.click()  ;

    }
    return ;
}

function isIE(){
   return ((navigator.appName=='Netscape')?false:true);
}

function HashMap(){
    this.hashArray = new Array();
    this.getIterator = function (){
        return this.hashArray;
    }
}

HashMap.prototype.put = function (key, value){
    var jasonObj = this.get(key);
    if(jasonObj.value == null){
        this.hashArray[this.hashArray.length] = {"key":key, "value":value};
    } else {
        this.hashArray[jasonObj.index].value = value;
    }
}

HashMap.prototype.get = function (key){
    var value = null;
    var index = 0;
    for(index; index<this.hashArray.length; index++){
        if(this.hashArray[index].key == key){
            value = this.hashArray[index].value;
            break;
        }
    }

    if(index == this.hashArray.length){
        index = -1;
    }

    return {"value":value, "index":index};
}

HashMap.prototype.remove = function (key){
    var value = null;
    var index = 0;
    for(index; index<this.hashArray.length; index++){
        if(this.hashArray[index].key == key){
            value = this.hashArray[index].value;
            this.hashArray.splice(index, 1);            
            break;
        }
    }

    if(index == this.hashArray.length){
        index = -1;
    }

    return {"value":value, "index":index};
}

HashMap.prototype.clear = function (){
    this.hashArray = new Array();
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