/*
		
		Change History  :

		Problem No        Correction Date	   
		-----------       ----------------                    
                WCL_8.0_047       18/06/2009           
                WCL_8.0_081       12/08/2009            
                WCL_8.0_092       03/09/2009            
                WCL_8.0_093      03/09/2009            
                WCL_8.0_102      11/09/2009             
                WCL_8.0_103      11/09/2009             
                WCL_8.0_106      11/09/2009             
                WCL_8.0_113      24/09/2009            
                WCL_8.0_114      29/09/2009             
                WCL_8.0_119     06/10/2009            
                WCL_8.0_120      06/10/2009           
                WCL_8.0_121      06/10/2009             
                WCL_8.0_136     16/10/2009           
                WCL_8.0_139     22/10/2009              
                WCL_8.0_148     16/11/2009               
                WCL_8.0_149     16/11/2009               
                WCL_8.0_161     16/12/2009           
                WCL_8.0_163    24/12/2009               
                WCL_8.0_176     19/01/2010               
                WCL_8.0_184     02/02/2010              
                WCL_9.0_002     24/09/2010       
                WCL_9.0_021     04/01/2011        
                WCL_9.0_026     11/01/2011              
                WCL_9.0_041     04/02/2011             
                WCL_9.0_051     15/02/2011              
                WCL_9.0_053     23/02/2011            
                Bug 28049       02/09/2011             
                WCL_8.0_321     21/04/2011            
                WCL_8.0_228     11/06/2010              
                WCL_9.0_053     23/02/2011              
                                29/12/2011          29610    
                30543           08/06/2012        
        	Bug 30277      24/01/2012       
                Bug 30946       
                Bug 32773 
                Bug 32179  
                Bug 31564   
                Bug 32608      
                Bug 41792, 27/09/2013           
                Bug 44632       06/06/2014         
*               Bug 42621       30/07/2014         
*               Bug 42678       05/08/2014            
*               PRDPBug 44696 -      06/08/2014        

*               Bug 47668       06/08/2014                  
                Bug 62986 -     22/07/2016    
*               Bug 64280       08/09/2016       
                Bugzilla – Bug 64080  		
                Bug 66390 -     22/12/2016                   
                Bug 66459       27/12/2016   
                Bug 62856       18/01/2017
                Bug 56011       05/01/2017     
                Bug 55372       12/01/2017     
                Bug 68022       20/03/2017  
                Bug 68117       27/03/2017   
                Bug 67762       29/03/2017
                Bug 67103       29/03/2017           
                Bug 68583       13/04/2017
                Bug 68517       26/04/2017
                Bug 72618       17/10/2017
                Bug 73126       02/11/2017
                Bug 73141       02/11/2017
                Bug 73729       22/11/2017 
                Bug 73882       29/11/2017
                Bug 74700       08/01/2018
                Bug 75865       07/02/2018
                Bug 78733       03/07/2018
                Bug 80212       19/09/2018
*/

//latinChars should be comma separated e.g. â,bē
var latinChars=["â"];//Bug 66390
var DOCRLOS;

function executeActionClick(actionName)
{
	return true;
}


function OpenCustomUrl(url,name)
{//todo security
    url = url.replace('\\','\\\\');    
    var src = url;
    customChildCount++;
    
    url = getActionUrlFromURL(src);
    url = appendUrlSession(url);
    
    var wFeatures = 'resizable=yes,scrollbars=1,status=yes,width='+window1W+',height=320,left='+window1Y+',top='+window1X;
    
    var listParam=new Array();
    listParam = getInputParamListFromURL(src);
    customChild[customChildCount] = openNewWindow(url,name,wFeatures, true,"Ext1","Ext2","Ext3","Ext4",listParam);
    
   // url = appendUrlSession(url);
  //  customChild[customChildCount] = window.open(url,name,'resizable=yes,scrollbars=auto,width='+window1W+',height=320,left='+window1Y+',top='+window1X);        
}


function CustomFormReload(loc)
{
	//Just uncomment below line if value to be set is in the form
//	eval(loc).reload();
}

function MoreActionsClick()
{
	return true;
}


function ToolsClick()
{
	return true;
}


function PreferenceClick()
{
	return true;
}


function ReassignClick(wiInfo,from)
{
    /*
      wiInfo              : Selected workitem information 
      from                : WDESK or WLIST or REASSIGN_LINK_CLICK_FROM_WLIST or REASSIGN_LINK_CLICK_FROM_WDESK
      from is 'WDESK'     : when reffered by opening the workitem i,e from workdesk
      from is 'WLIST'     : when reffered without opening workitem i,e from workitem list
      
      ReassignComments    : This tag contain Reassign  Comment 
      ReassignTo          : This tag contain User  to which workitem is going to reassign
      ReassignToUserIndex : This tag contain UserIndex of user to which workitem is going to reassign
      ReassignBY          : This tag contain User who is going to reassign the workitem
      ReassignByUserIndex : This tag contain UserIndex of user who is going to reassign the workitem 
    */
	return true;
}


function LinkedWiClick()
{
	return true;
}


function QueryClick()
{
	return true;
}


function SearchDocClick()
{
	return true;
}


function SearchFolderClick()
{
	return true;
}


function AddConversationClick()
{       
        
	return true;
}


function AddDocClick()
{
	return true;
}


function ImportDocClick()
{
	return true;
}


function ScanDocClick()
{
	return true;
}


function SaveClick()
{
	return true;
}


function IntroduceClick()
{
	return true;
}


function RejectClick()
{
	return true;
}


function AcceptClick()
{
	return true;
}


function DoneClick()
{
	return true;
}

function ValidateDocumentName(documentName,documentType){
 
    return true;
}

function RevokeClick()
{
	return true;
}


function PrevClick()
{
	return true;
}


function NextClick()
{
    return true;
}


function ExceptionClick()
{
	return true;
}


function FormViewClick()
{
	return true;
}


function ToDoListClick()
{
	return true;
}


function DocumentClick()
{
	return true;
}


function ClientInterfaceClick(url)
{
	return true;
}


function NewClick()
{
	return true;
}

function WIPropertiesClick()
{
	return true;
}


function PriorityClick()
{
	return true;
}


function ReminderClick()
{
	return true;
}





function saveFormData()
{
	return true;
}


function NGGeneral(sEventName,sXML)
{
	switch(sEventName)
	{
		case 'ImportDoc' :
			window.parent.parent.frames["wiview_top"].importDoc('S');
			break;
	}
}


function preHook(opType)
{
	return true;
}

function CommentWiClick(){
              return true;
}

function getUploadMaxLength(strprocessname,stractivityName,docType)
{
    /*
         strprocessname :   Name of the Current Process
         stractivityName:   Name of the Current Activity
    */
   /*Bug 42111
         strprocessname :   Name of the Current Process
         stractivityName:   Name of the Current Activity
	 docType	:	DocumentType
    if(strprocessname == 'AttachTest' && stractivityName == 'Work Introduction1' && docType == 'attachmentfree')
    {
        return 5;
    }
    else if(strprocessname == 'AttachTest' && stractivityName == 'Work Introduction1' && docType == 'selfattach')
    {
        alert("In else if ");
        return 15;
    }
    else
    {
        alert("In else");
        return 10;
    }
*/
	return 10;
}

function WFGeneralData(){
       
}
function CustomShow(){
              return true;
}

function setFormFocus(){
 
}

function commitException(){
    return true;
}

function validateUploadDocType(docExt,DocTypeName)// WCL_8.0_081
{ 
	//check doc extension and return false from the function in case of undesired file extension  
	return true;
}

function validateUploadDocTypeName(DocTypeName)// WCL_8.0_081
{
        //make custom check for the Document Type Name 
 	var win_workdesk = window.opener;
        var winList=win_workdesk.windowList;
        var formWindow=getWindowHandler(winList,"formGrid");
        var formFrame; 
        if(formWindow){
            if(win_workdesk.wiproperty.formType=="NGFORM"){
		formFrame = formWindow; 
                // Write custom code here for ngform   
            }
            else if(win_workdesk.wiproperty.formType=="HTMLFORM"){
                formFrame = formWindow;
                //Write custom code here for HTMLForm
            }
           else if(win_workdesk.wiproperty.formType=="CUSTOMFORM"){
                formFrame = formWindow.frames['customform'];
                //Write custom code here for customform
            }
        }

	return true;
}
function ToggleFocus(interfaceFlag)
{
    if(interfaceFlag == "F")
    {
          /*if(typeof  interFace0!='undefined'){
                if(interFace0=="doc" && interFace2!=""){
                     toggleIntFace("doc",interFace2);
                }
                else if(interFace1=="doc" && interFace3!=""){
                     toggleIntFace("doc",interFace3);
                }
                else if(interFace2=="doc" && interFace0!=""){
                     toggleIntFace("doc",interFace0);
                }
                else if(interFace3=="doc" && interFace1!=""){
                    toggleIntFace("doc",interFace1);
                }
          }
           if(document.IVApplet)
            {
               try{
                    document.IVApplet.setIVFocus();
                }catch(ex){
                  }
        }*/
        
        if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm){
            var ngformIframe = document.getElementById("ngformIframe");	
            if(ngformIframe != null){
                if(!bAllDeviceForm){      
                    ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.focus()");	
                }
            }
         } else if((typeof document.wdgc!='undefined') && document.wdgc){
            document.wdgc.NGFocus();
         } else {
                var htmlFormPanel = window.document.getElementById("wdesk:htmlFormPanel")
                if(htmlFormPanel!=null){           
                   try{               
                        htmlFormPanel.rows[0].cells[1].firstChild.focus();
                    } catch(e){}
                }
         }
    }
    else if(interfaceFlag == "D")
     {

           /*if(typeof  interFace0!='undefined'){
                if(interFace0=="form" && interFace2!=""){
                     toggleIntFace("form",interFace2);
                }
                else if(interFace1=="form" && interFace3!=""){
                     toggleIntFace("form",interFace3);
                }
                else if(interFace2=="form" && interFace0!=""){
                     toggleIntFace("form",interFace0);
                }
                else if(interFace3=="form" && interFace1!=""){
                    toggleIntFace("form",interFace1);
                }
          }
          
             try{
                 if((wiproperty.formType == "NGFORM") && (ngformproperty.type == "applet") && !bDefaultNGForm){
                    var ngformIframe = document.getElementById("ngformIframe");	
                    if(ngformIframe != null){
                        ngformIframe.contentWindow.eval("com.newgen.omniforms.formviewer.focus()");	
                    }
                 } else {
                     if((typeof document.wdgc!='undefined') && document.wdgc){
                        document.wdgc.NGFocus();
                     }
                 }
            }catch(ex){
              }*/
        
        try{               
               window.document.getElementById("wdesk:docCombo").focus();
        } catch(e){}        
          
    }else if(interfaceFlag=="W"){
       window.focus();
   } else if(interfaceFlag == "E"){
        if(window.document.getElementById("wdesk:expList")!=null){
            try{
                window.document.getElementById("wdesk:expList").focus();
            } catch(e){}
        }
   } else if(interfaceFlag == "T"){
        var todoListTableRef = window.document.getElementById("wdesk:todoListTable")
        if(todoListTableRef!=null){           
           try{               
                todoListTableRef.rows[0].cells[1].firstChild.focus();
            } catch(e){}
        }
   }
}
function getTempletPref(){ 

    var strTemplatePrefXml=''; 

    return strTemplatePrefXml; 
}
function showPriority(){
        var priorityArray =new Array();
        priorityArray[0]=1;  //Low (replace 1 by 0 for removing it from priority combo)
        priorityArray[1]=1;  //Medium
        priorityArray[2]=1;  //High
        priorityArray[3]=1; //Very High
        return priorityArray;
}
function customValidation(opt){
    // Custom Change Start
	var ret;
	try{
		ret = window.frames["ngformIframe"].contentWindow.customValidation(opt);            // For Chrome
	}
	catch(ex){
		ret = window.frames["ngformIframe"].customValidation(opt);               //// For IE
	}              
	return ret;
	// Custom Change End
}
function enableWLNew(queueName){    

  return true;
}
function validateCropDocTypeName(DocTypeName){ 
  return true;
}

function hideWdeskMenuitems(){
    var wdeskMenu=""; 
    //wdeskMenu=LABEL_SAVE_WDESK+","+LABEL_INTRODUCE_WDESK;
    return wdeskMenu;
}

function hideWdeskSubMenuitems(){
    var wdeskSubMenu="";
   //wdeskSubMenu=LABEL_ADD_DOCUMENT_WDESK+","+LABEL_SCAN_DOCUMENT_WDESK; 
   return wdeskSubMenu;
}

function setAttributeData()
{
    closeFrm = 'ok';
    var inputObject,i;
    var inputObjectValue;
    var inputObjectType;
   // var attributeData = '';
    var tmpObj = document.forms["dataForm"];
   // var status = htmlFormOk(window,"dataForm");
   // alert(document.forms["dataForm"].elements.length);
    for ( i=0; i < document.forms["dataForm"].elements.length ; i++) {		
        if (document.forms["dataForm"].elements[i].type == "text" || document.forms["dataForm"].elements[i].type == "select-one") {
            inputObject = document.forms["dataForm"].elements[i].name;
            inputObjectValue = document.forms["dataForm"].elements[i].value;
            if(document.forms["dataForm"].elements[i].type == "text"){
                inputObjectType = document.forms["dataForm"].elements[i].alt;
            } else {
                inputObjectType = "";
            }

          //  attributeData +=inputObject.substring((inputObject.indexOf(SYMBOL_3) + 1))+SEPERATOR2+inputObjectValue+SEPERATOR2+inputObjectType+SEPERATOR1;
            attributeData +="<"+inputObject.substring((inputObject.indexOf(":")+1))+">"+inputObjectValue+"</"+inputObject.substring((inputObject.indexOf(":")+1))+">";
        }
    }
            
    if(typeof window.opener != 'undefined' && typeof window.opener.customDoneCallBack != 'undefined'){
        window.opener.customDoneCallBack(attributeData,attributeXmlTmp,queueName);
    }
    
    /*
     if(typeof window.opener != 'undefined' && typeof window.opener.customInitiateCallBack != 'undefined'){
        window.opener.customInitiateCallBack(attributeData,attributeXmlTmp,queueName);
     }
    */
    
    window.close();
    return false;
}

function WIWindowName(pidWid,processName,activityName){
    
 return pidWid;
}
function UnassignClick(){

 return true;   
}
function LockForMe(){

 return true;   
}
function setQueueData(queueId,selectedWICount)
{
     var dataXMl="";
    //TODO set your queue variable or external variable return the dataXML in given format--
    // <VaribaleName>variableValue</VaribaleName>
     return dataXMl;
}

function enableCustomButton(strSelQueueName,strFrom){  
     return true;
}
function CustomButtonClick(selectedWIInfo){  
    //Uncomment refresh() for refreshing workitem list.
   //refresh();
   return true;
}

function enableReassign(strSelQueueName,strFrom){   

    /*
         strSelQueueName: selected QueueName (in case of search it is blank) 
         strFrom        : W or F or S
         W              : Workitem list form any queue
         F              : Workitem list form Filter
         S              : Workitem list form Search
    */

     return   true;
}

function enableRefer(strSelQueueName,strFrom){  
   
    /*
         strSelQueueName: selected QueueName (in case of search it is blank)
         strFrom        : W or F or S
         W              : Workitem list form any queue
         F              : Workitem list form Filter
         S              : Workitem list form Search
    */

   return   true;
}
function enableLockForMe(strSelQueueName,strFrom){  
   
    /*
         strSelQueueName: selected QueueName (in case of search it is blank) 
         strFrom        : W or F or S
         W              : Workitem list form any queue
         F              : Workitem list form Filter
         S              : Workitem list form Search
    */

   return   true;
}
function enableRelease(strSelQueueName,strFrom){  
   
    /*
         strSelQueueName: selected QueueName (in case of search it is blank)
         strFrom        : W or F or S
         W              : Workitem list form any queue
         F              : Workitem list form Filter
         S              : Workitem list form Search
    */

   return   true;
}

function enableDelete(strSelQueueName,strFrom){  
   
    /*
         strSelQueueName: selected QueueName (in case of search it is blank)
         strFrom        : W or F or S
         W              : Workitem list form any queue
         F              : Workitem list form Filter
         S              : Workitem list form Search
    */

   return   true;
}
function customFieldForCropping(){ 
  // return the name of string type array variable for displaying combo for it on crop document page.
 return ""; 
}

function customDataDefName(strProcessname, strActivityname, strUsername, strDocType){
 
    /*
         strProcessname :   Name of the Current Process
         strActivityname:   Name of the Current Activity    
         strUsername    :   Current Logged in Username
         strDocType     :   Document type to be associated with Dataclass
         return         :   DataDefinition Output XML string.
    */

     return ("");
}
function OkConversationClick() {

}
function customClick(linkName){
    return true;
  }
function docAndFormAppletHeight(strprocessname,stractivityName){
  var strDocAndFormHeight="";                     // 180 need to be subtracted from screen height for MenuBar.
  // strDocAndFormHeight="<DocHeight>"+strDocHeight+"</DocHeight><FormHeight>"+strFormHeight+"</FormHeight>";
  return strDocAndFormHeight;
}
function enableDone(strSelQueueName,strFromSearch){    
    return true;
}

function enableInitiate(strSelQueueName,strFromSearch){
 return true;

}

function isSaveOnClose(strProcessName,strActivityName) {
return true;
}

function isAlertOnWorkitemClose(strProcessName,strActivityName) {
return true;
}

function LinkedWiClick()
{
	return true;
}

function worklistHandler(from,wiInfo){
    /*
       from             : reassign
       wiInfo           : Selected workitem information
    */
  return true;
}
function validateCropDocTypeName(DocTypeName){
  return true;
}
function UploadDocClick(){
     return true;
}
function isEnableDownloadFlag(strprocessname,stractivityName)
{
return true;
}
function isEnableEditDocFlag(strProcessname,strActivityname )
{
return true;
}
function workitemOperation(opt,ext){
    if(wiproperty.locked != 'Y') {
        if(opt=="S"){
            mainSave();
        }else if(opt=="D"){
            if(wiproperty.operationOption== 'done')
                done();
            else if(wiproperty.operationOption== 'introduction')
                introduceWI();
        }
    }
    if(opt=="C"){
        closeWorkdesk();
    }
}

function getSelectedDocumentDetails()
{
    var dataXMl="";
    var objCombo=document.getElementById('wdesk:docCombo');
    var strDocIndex=objCombo.value;
    var strDocName=objCombo[objCombo.selectedIndex].text;
    dataXMl += '<Document><DocumentIndex>'+strDocIndex+ '</DocumentIndex><DocumentName>'+strDocName+'</DocumentName></Document>';
    return dataXMl;

}

function chkForRaisedExcp(selindx, seltxt)
{

/*   var retExp = getInterfaceData('E');
   for(var i=0;i< retExp.length;i++){
        var exceptionName = retExp[i].name;
        var seqid = retExp[i].seqid;
        var RaiseComment=retExp[i]. RaiseComment;
   }
*/
return true;
}

function ReferClick(commentsRfer,referTo,referBY,from)
{
  /*  commentsRfer    : Refer Comment
      referTo         : User  to which workitem is reffered
      referBY         : User who refer the workitem
      from            : WDESK or WLIST
      from is 'WDESK' : when reffered by opening the workitem i,e from workdesk
      from is 'WLIST' : when reffered without opening workitem i,e from workitem list
  */
   return true;
}

function ReassignClick()
{
	return true;
}

function getDoneInformation(){
    /*   user name can be found in variable : username
                 e.g. alert(username);  */

    /* following loop will get processincstanceid activityName and processName of the selected done workitems */
    var ctrlTable=document.getElementById("frmworkitemlist:pnlResult");
    var checkboxId="frmworkitemlist:checkBox_";
    var rowCount = ctrlTable.tBodies[0].rows.length;
    if(rowCount>0) {
        for(var iCount = 0; iCount < rowCount-1;iCount++)
        {
            var wiClicked=document.getElementById(checkboxId+iCount);
            if(wiClicked.checked){
                    
                var jsonOutput=document.getElementById("frmworkitemlist:hidWIJson"+(iCount+1)).innerHTML;
                jsonOutput= eval("("+jsonOutput+")");
                var arrobjJsonOutput= jsonOutput.Outputs;
                for(var i=0;i<arrobjJsonOutput.length;i++){
                    var outputJson=arrobjJsonOutput[i];
                    var objJson=outputJson.Output;
                    var activityName,processName,processInstanceId;
                    if(objJson.Name=='ActivityName'){
                        //listParam.push(new Array(objJson.Name,encode_ParamValue(objJson.Value)));
                        activityName=+objJson.Value;
                    }
                    if(objJson.Name=='RouteName'){
                        //  listParam.push(new Array(objJson.Name,encode_ParamValue(objJson.Value)));
                        processName=objJson.Value;
                    }
                    if(objJson.Name=='ProcessInstanceID'){
                        //  listParam.push(new Array(objJson.Name,encode_ParamValue(objJson.Value)));
                        processInstanceId=objJson.Value;
                    }
                }
            }
        }
    }
}

function wiOptDoneClick(){//Bug 68258
    /* following loop will get processincstanceid activityName and processName of the selected done workitems
    var ctrlTable=document.getElementById("frmworkitemlist:pnlResult");
    var checkboxId="frmworkitemlist:checkBox_";
    var rowCount = ctrlTable.rows.length;
    if(rowCount>0) {
        for(var iCount = 0; iCount < rowCount-2;iCount++)
        {
            var wiClicked=document.getElementById(checkboxId+iCount);
            if(wiClicked.checked){
                    
                var jsonOutput=document.getElementById("frmworkitemlist:hidWIJson"+(iCount+1)).innerHTML;
                jsonOutput= eval("("+jsonOutput+")");
                var arrobjJsonOutput= jsonOutput.Outputs;
                for(var i=0;i<arrobjJsonOutput.length;i++){
                    var outputJson=arrobjJsonOutput[i];
                    var objJson=outputJson.Output;
                    var activityName,processName,processInstanceId;
                    if(objJson.Name=='ActivityName'){
                        //listParam.push(new Array(objJson.Name,encode_ParamValue(objJson.Value)));
                        activityName=+objJson.Value;
                    }
                    if(objJson.Name=='RouteName'){
                        //  listParam.push(new Array(objJson.Name,encode_ParamValue(objJson.Value)));
                        processName=objJson.Value;
                    }
                    if(objJson.Name=='ProcessInstanceID'){
                        //  listParam.push(new Array(objJson.Name,encode_ParamValue(objJson.Value)));
                        processInstanceId=objJson.Value;
                    }
                }
            }
        }
    }*/
    return true;
}

function isDefaultDocument(processName,activityName){
          return true;
} 

function customDone(opt)
{
    //opt=1 for  done and bring next workitem
    //opt=2 for only done
    if(isFormLoaded==false)
        return;
    showProcessing();
    if(!form_cutomValidation("D")){
        hideProcessing();
        return false;
    }
    if(!saveFormdata('formDataTodo','done')){
        hideProcessing();
        return false;
    }

    handleDoneWI(opt);
}
function customIntroduce(opt)
{
    //opt=1 for  done and bring next workitem
    //opt=2 for only done
    if(isFormLoaded==false)
        return;
    showProcessing();
    if(!form_cutomValidation("D")){
        hideProcessing();
        return false;
    }
    var batchflag=document.getElementById('wdesk:batchflag').value;
    if(!saveFormdata('formDataTodo','introduce')){
        hideProcessing();
        return false;
    }
    handleIntroduceWI(opt);

}

function noClickAfterDone(){

}
function customUserList(strUname){

    /*
         strqueueId       :   Name of the queue selected
         strUname         :   Current Logged in Username
         userPersonalName :   User's personal name (to whom workitems to be reassigned)
         userName         :   User's name (to whom workitems to be reassigned) 
         userIndex        :   User's index (to whom workitems to be reassigned)
         return           :   false
    */  
//         Usage:

//        var queueId = document.getElementById("reassignworkitem:hidQueueId").value;
//        document.getElementById('reassignworkitem:bp:UserName').value=userName;
//        document.getElementById('reassignworkitem:hidUserIndex').value=userIndex;
//        document.getElementById('reassignworkitem:hidUserName').value=userName;
//              
//        return false; 
		   
    return true;
}

function customUserListForSetDiversion(strUname){

    /*
         strqueueId       :   Name of the queue selected
         strUname         :   Current Logged in Username
         userPersonalName :   User's personal name (to whom workitems to be reassigned)
         userName         :   User's name (to whom workitems to be reassigned) 
         userIndex        :   User's index (to whom workitems to be reassigned)
         return           :   false
    */  
//         Usage:

//        document.getElementById('setdiversion:assignedName').value=userName;
//        document.getElementById('setdiversion:hidAssignedToIndex').value=userIndex;
//        document.getElementById('setdiversion:hidAssignedToName').value=userName;
//              
//        return false; 
		   
    return true;
}
function referBtnClick(referTo){
//alert(referTo); 
 return true;
}

function validateUploadFormFieldRestriction(fileName){
    var win_workdesk = window.opener;
    var formBuffer;
    var winList=win_workdesk.windowList;
    var formWindow=getWindowHandler(winList,"formGrid");
    if(typeof formWindow!='undefined'){
        if(win_workdesk.wiproperty.formType=="NGFORM")
        {
            if(!(win_workdesk.ngformproperty.type=="applet"))
                formBuffer = new String(formWindow.document.wdgc.FieldValueBag);
            else if(win_workdesk.ngformproperty.type=="applet")
            {
                if(win_workdesk.bDefaultNGForm){
                    formBuffer=formWindow.document.wdgc.getFieldValueBagEx();
                }
                formBuffer=formBuffer+"";
            }
        }
    }
     // Write custom code here for ngform. Parse formBuffer and use if/else logic to return false if filename and field value doesn't match else return true;
    return true;
}
function excphook(strRaiseComnt ,strRaiseExp, strRaiseExpName ){
    raiseComnt = strRaiseComnt;
    raiseExp   = strRaiseExp;
    raiseExpName = strRaiseExpName;
    raiseExcep_open('Y');
}


function customRaiseExp(){
    return false;
}

function setCustomExpVar()
{
   document.getElementById("raise:comnt").value = window.parent.raiseComnt;
   document.getElementById("raise:Exp").value = window.parent.raiseExp;
   document.getElementById("raise:expName").value = window.parent.raiseExpName;
}

function setColorForDocumentList(pid,wid){
  /*if(pid=="WF-00000000000000006-gen7" && wid=="1"){
        var objCombo = document.getElementById('wdesk:docCombo');
        var arr=["red","blue"];
        for(var index2=0;index2<objCombo.length;index2++){
            
            var docType=objCombo.options[index2].text;
            if ((docType.indexOf("(") != -1))
                docType = docType.substring(0,docType.indexOf("("));
            if(docType =="doc1"){
                objCombo.options[index2].style.color=arr[0];
            }else if(docType =="doc2"){
                objCombo.options[index2].style.color=arr[1];
            }
        }
    }*/
    return true;
}


function ChangeColorOnComboSelect(docindex){
    try{
        var objCombo = document.getElementById('wdesk:docCombo');     
      objCombo.style.color = objCombo.options[docindex].style.color;
    }catch(e){ }
}
function StartSessionActiveTimer()
{
    var interval = 30;   //interval in seconds

    StartCheckIsAdminTimer(interval);
}

function StopSessionActiveTimer()
{
    StopCheckIsAdminTimer();
}
function isZoningRequired()
{
   
 /*
   var objCombo = document.getElementById('wdesk:docCombo');
   var strDocName=objCombo[objCombo.selectedIndex].text;

   activityName : Activity Name
   strDocName   : selected doc name (contains doc type)
   return : false for the Doc types for which no zoning is required
*/
   return true;

}

function customHighlightZone()
{
    /*
     drawExtractZone (int zoneType, int x1, int y1, int x2, int y2, int zoneColor, int thickness, boolean isMutable)
    
Where 
	 zoneType Type of Extract Zone � Permissible values are the following defined constants:
     EXT_ZONE_SOLID_RECT = 1 (draws hollow rectangle)
     EXT_ZONE_HIGHLIGHT = 2 (draws highlighted rectangle)
     x1 x-coordinate of top-left corner of the zone
     y1 y-coordinate of top-left corner of the zone
     x2 x-coordinate of bottom-right corner of the zone
     y2 y-coordinate of bottom-right corner of the zone
     zoneColor Color of extract zone (for e.g., dvBlue = 170, dvYellow = 16776960, dvBlack = 0, dvLightGray = 8421504)
     thickness thickness of the extract zone. Permissible values are integer values between 1 and 5 pixels
     isMutable Specifying whether the extract zone is mutable i.e. it can be selected, moved or resized or not.

For example
	 
     if(document.IVApplet)
        document.IVApplet.drawExtractZone(2, 150, 150, 580, 580, 16776960 , 5,true);
		
    */
    
}

function enableOverwriteInImportDoc(strprocessname,stractivityName ,docType){
    /*

      strprocessname    : Process Name
      stractivityName   : Activity Name
      docType           : Document Type
      return false for the docType  for which overwrite to be disabled , Note that this hook will work only if no modify right on that docType
    */
return true;
}

function  RefreshClientComp(){
    try{
     /*var ref = window.parent.getComponentRef(sourceInsId).contentWindow;
	 ref.reloadPage(pid);  /*implemented by the calling page*/
         }catch(e){}
}
function eventDispatched(pId,pEvent){
    switch(pEvent.type)
    {           
        case 'click':
        {
            switch(pId)
            {
				case 'opt1':alert('');
				break;
                }
            }	
        }
}

function getExtParam(processName, activityName)
{
  var retXML =  "";
   /*
     This function will return the  process variables and these values to be saved in workitem while Save / Done / Introduce operation.
   */
 // retXML =  "<Attributes><Attribute><Name>qname</Name><Value>dssD</Value></Attribute><Attribute><Name>qage</Name><Value>65</Value></Attribute></Attributes>";
  return retXML;
}

/*
 *sample code for field validation (Global Method associated with process variant field)
function validateAccNo(ref){
	var strPrefix = "wdesk";
	var field = ref;
	var bError = false;
	var msg = "";
	var dtime;//time to display the message
	var rem = "true";//to remove the message after specified time or not
	var fieldLength = field.value.length;
	var fieldValue = field.value;
	if(fieldLength>15){
		bError = true;
        msg = "Length of account number should be less than 15";
    }
	if(!bError){
		var i=0;
	    if(fieldValue=='-'){
			bError = true;
			msg = "Invalid Number Entered";
		}
		if(!bError && fieldValue.charAt(0)=='-')
			i=1;

		for (; i < fieldValue.length; i++)
		{   
			var c = fieldValue.charAt(i);
			if (!((c >= "0") && (c <= "9"))){ 
				bError = true;
				msg = "Invalid Number Entered";
				break;
			}
		}

	}
	
	if(bError){
                var workdeskView = (typeof wdView == "undefined")? '': wdView;
		if((isEmbd == 'Y') && (workdeskView == "em") && document.getElementById("wdesk:indicatorPG"))
			document.getElementById("wdesk:indicatorPG").style.display = "inline";

		var messageDiv=document.getElementById("wdesk:messagediv");
        if(messageDiv){
            var messagePG = document.getElementById("wdesk:messagedivPG");
            if(messagePG){                
                var isSafari = navigator.userAgent.toLowerCase().indexOf('safari/') > -1; 
                if(isSafari){
                    messagePG.style.width = "auto";
                    messagePG.style.display = "inline-block";
                } else {
                    messagePG.style.display = "inline-table";
                }
                messageDiv.innerHTML= msg; 
				messagePG.style.display = "inline-table";
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
}
*/

function yesBringNextWI(strprocessname,stractivityName)
{
/* strprocessname : Process Name
 * stractivityName : Activity Name
 * return false will not open Bring Next Workitem in dialog box
 * will work only when there is entry YesBringNextWI=N in webdesktop.ini
*/
 return false;
}
function disableConfirmDone(strprocessname,stractivityName)
{
/* strprocessname : Process Name
 * stractivityName : Activity Name
 * return false will  open confirm done window
 * will work only when there is entry ConfirmDone=N in webdesktop.ini
*/
 
	// Custom Change Start
	if(strprocessname == "PC" || strprocessname == "RAOP" || strprocessname == "CMS" || strprocessname == "CMP" || strprocessname == "CBP" || strprocessname == "ML")
		return true;
	else
		return false;
	// Custom Change End
}

function nameUploadDocument(strDocName,frm,processname, activityname,username)
{

/*
    alert(strDocName);
    alert(frm);
    alert(processname);
    alert(activityname);
    alert(username);
    alert(strDocName); 
*/	
return strDocName;
}

function mandatoryCommentsBeforeReassign(strprocessname,stractivityName)
{
/*  if(document.wdgc != undefined)
    {
    if( document.wdgc.getNGValue("formcontrolname") == "" ) {   
    alert(ENTER_YOUR_MSG_HERE);
    return false;   
    }
    else
    return true;
    }    
*/
    
/*
     *formcontrolname : name of form control on which you want apply validation
     *return true:will open reassign window
     *return false :will not open reassign window
*/
    return true;
}

function isCustomValidException(selectedExcpName)
{
    // if it returns false , the exception will not be raised
    return true;
}

function postHookGenerateResponse(docIndex, docName)
{
 /*
         docIndex: Document Index of the added Document
		 docName: Document Type of the added Document
 */

}

function cropDocTypeList(strprocessname,stractivityName,strPageNo,docName)
{
    /*  strprocessname : Process Name
        stractivityName : Activity Name
     *  strPageNo       : Page No from which image is cropped
     *  docName         : Document Type & Name from which image is cropped
     *  
     *  return the doc types as Comma separated e.g.- "doc1,doc2"
     *  Doc types returned are CASE SENSITIVE. 
     */
    return  "";
}

function importDocumentPrehook(docTypeName)
{
   /* Sample custom code to retrieve Document typon executeActies added in import Document Window combo list as well in workitem window's added documents and
    * Will not upload the documents if this Hook returns false
    *
   var objCombo = window.opener.document.getElementById('wdesk:docCombo');
    if(typeof objCombo != 'undefined')
      {
        for(var index2=0;index2<objCombo.length;index2++){

            var importedDocType=objCombo.options[index2].text;
            if(importedDocType.indexOf(docTypeName)== 0)
             {
                alert("Document Already Added");
                return false;
             }

        }
      }
   */
    return true;
}
function isDebug(strprocessname,stractivityName)
{
/*    if(strprocessname=='TestGen' && stractivityName=='Work Introduction1')
        {
            return true;
        }
*/
    return false;
}

function templateData(){

}

function isDisablePrintScreen(strProcessname, strActivityname)
{
    // return true to disable print screen
    return false;
}

function isNewVersionDoc(processName, activityName){ 
    
    return true;
}
function isOverWriteDoc(processName, activityName){
    
     return true;
}

function deleteDocFromComboList(docIndex)
{
    var objCombo = window.document.getElementById('wdesk:docCombo');
    var isFound= false;
     for(var i=0;i<objCombo.options.length;i++)
       {
        if(objCombo.options[i].value == docIndex)
         {
         objCombo.options[i]=null;
         isFound = true;
         break;
         }
       }
       
       if(isFound == false) {
           alert(NO_MATCHING_DOCUMENT);
       }
}
function isEnableDownloadPrint(strProcessname, strActivityname, strUsername)
{
    // If returned true will allow print and download option even to thos doc types which have no modified rights
    // By default do not display toolbar by making entry in ShowDefaulToolbarFlag=N
    return false;
}

function DocName(docname1){
	DOCRLOS=docname1;
}


function getdocTypeListExt(docListObj)
{
	//below function by saurabh for incoming doc new	
   var docWindowObj="";
   if(window.parent.strprocessname!=''){
	docWindowObj=window.parent;
	}
	else{
		docWindowObj=window.opener.parent;
	}
   //ended
	var pname=docWindowObj.strprocessname;
	
	var docTypeList = new Array();
	var count = -1;
	
	if (pname != 'PC' && pname != 'RAOP' && pname != 'CMS' && pname != 'CMP' && pname != 'CBP' && pname != 'ML')
	{
		// How to Capture Existing Doc List
		docTypeList[0]=window.opener.DOCRLOS;
	}
	return docTypeList;
}
function getdocTypeDefIndex(docListObj) {
    var index = -1;//default value of index --  for select option in the beginning
    /* Code snippet to iterate the list and compare with required value
    var tmpDocTypeList = docListObj;
        for(var i=0 ; i<tmpDocTypeList.length; i++)
        {
            if(tmpDocTypeList[i] == "tabc"){
                index=i;
                break;
            }
        }
    */
    return index;
}
function CustomCrop(strprocessname,stractivityName)
{
/* strprocessname : Process Name
 * stractivityName : Activity Name
 * return false will not open custom Crop Document Window
*/
return true;
}
function croppedByField(strprocessname,stractivityName){
    // return the name of variable
    return "";
}

function isDocOrgName(strprocessname, stractivityName)
{
/* strprocessname : Process Name
 * stractivityName : Activity Name
 * return false will not display 
   document's original name (document file's name) but the
   Doc Type only even if it is cofigured through webdesktop.ini to display its
   original name by entry (DocOrgName=Y)
*/  
 return true;
}

function isAnnotationToolbar(strProcessname,strActivityname )
{
   // if(strProcessname == 'AParentForm' && strActivityname == 'Standard Workdesk1')
   //     return false;
   // else
        return true;
}
function currentSelPageInImage(strPageInImage){
    
    //strPageInImage: contains current page no. of a Multi-page Tiff
    
}
function extractZoneModified(zoneType, x1, y1, x2, y2, createModifyFlag, zoneID, partitionInfo)
{
 /*
  *This method gets called when zone gets drawn or selected or modified
  */   
}
function customDownloadedDocName(docName,docOrgName,DocExt,pid){
    var downloadedDocName="";
   /* alert("docName -> "+docName+" docOrgName -> "+docOrgName+" DocExt -> "+DocExt+" pid -> "+pid)
     docName--> Document type
     docOrgNamge--> disk name of the document
     DocExt--> document exttension
     pid--> processinstanceid
     Note : Add the document extension after the document name with '.' e.g downloadedDocName=docName+pid+"."+DocExt.
   */
   return downloadedDocName;

}

function reloadNewAddedDoc(strprocessname,stractivityName)
{
   /*return
    *true  : it will reload the applet to show newly added doc
    *false : it will not reload the applet to show newly added doc
    **/
   return true;
}

function isUploadedDocType(filepath,docTypeName){
    /*var docJsonList=window.opener.getInterfaceData("D");
    var returnFlag=true;
    for(var i=0;i<docJsonList.length;i++){
        if(docJsonList[i].name.indexOf(docTypeName)>-1 && filepath.indexOf(docJsonList[i].DiskName)>-1){
            returnFlag=false;
            break;
        } 
    } 
    if(returnFlag==false){
        alert("document is already attached");
        return returnFlag;
    }*/
    
    //alert("docJsonList.name"+docJsonList[i].name);
    //alert("docJsonList.DiskName"+docJsonList[i].DiskName);

   return true; 
}

function modifyDocComment(processname,activityname,username,docTypeName){
    return "";
}
function wiLockForMeClick(){
     return true;
}
function wiReleaseClick(){
     return true;
}


function isDocumentAttached(){
    var docAttached = false;
    var objCombo = docWindow.document.getElementById('wdesk:docCombo');
    
    if(objCombo != null){
        if(objCombo.length > 0){
            if(objCombo.length == 1){
                if(objCombo.options[0].value == "-1"){
                    docAttached = false;
                } else {
                    docAttached = true;
                }
            } else {
                docAttached = true;
            }
        }
    }
    
    return docAttached;
}
function getWiFormValues()
{
    //Function returns attributes xml composing of queue variables values present in Custom Introduce form 
    var str = "";
    var valueArr = null;
    var val = "";
    var cmd = "";
    var fobj = frames['NewWIFRAME'].document.forms['wdesk']
    if(typeof fobj == 'undefined')
        return "";
    
    var formid=fobj.id;
    
    var strAttribXML="<Attributes>";
    if(formid=='wdesk'){
        for(var i = 0;i < fobj.elements.length;i++)
        {
            switch(fobj.elements[i].type)
            {
                case "textarea":
                case "text":
                    strAttribXML=strAttribXML+"<"+fobj.elements[i].name+">"+fobj.elements[i].value+"</"+fobj.elements[i].name+">";
                    break;
               /* case "select-one":
                    if(fobj.elements[i].selectedIndex!=-1)
                        strAttribXML=strAttribXML+"<"+fobj.elements[i].name+">"+fobj.elements[i].options[fobj.elements[i].selectedIndex].value+"</"+fobj.elements[i].name+">";
                    break; 
                case "checkbox":
                    if(fobj.elements[i].checked)
                    {
                        strAttribXML=strAttribXML+"<"+fobj.elements[i].name+">"+fobj.elements[i].value+"</"+fobj.elements[i].name+">";
                    
                    }
                    break;
                case "hidden": 
                      strAttribXML=strAttribXML+"<"+fobj.elements[i].name+">"+fobj.elements[i].value+"</"+fobj.elements[i].name+">";
                     break; */
            }
        }
    }
    strAttribXML=strAttribXML+"</Attributes>";
    return strAttribXML;
}

function customCloseMessage()
{
    //ALERT_CLOSE_SAVE_CONFIRM="Do you want to save the workitem before closiung the window?";
    return ALERT_CLOSE_SAVE_CONFIRM; 
}

function handleExcp(excpHandlerJson)
{
excpHandlerJson = customEventHandler(excpHandlerJson);
document.getElementById("excpJson").value = JSON.stringify(excpHandlerJson);
document.getElementById("btnEventHandler").click();   // For executing any custom code in java
} 
// this method is called when you call ValidatorException from java..u will get the parameters specified from java code in this js method.. 
function customEventHandler(excpHandlerJson)
{
    excpHandlerJson=JSON.parse(excpHandlerJson);
    var eventHandlerName = decode_utf8(excpHandlerJson.EventHandler); // 3rd parameter in from Customexceptionhandler constructor
    var params = JSON.parse(excpHandlerJson.Parameters) // 4th parameters which is hashmap in form of JSON 
    var firstParam = decode_utf8(excpHandlerJson.Summary); //1st parameter 
    var secondParam = decode_utf8(excpHandlerJson.Detail); //2nd parameter 
    var jsonParam = {};
    for( var key in params )
    {
        jsonParam[decode_utf8(key)] = decode_utf8(params[key]);
    }
    params = jsonParam;

// custom coding goes here 
	//	Bugzilla – Bug 64080 
    excpHandlerJson.EventHandler = eventHandlerName;
    excpHandlerJson.Summary = firstParam;
    excpHandlerJson.Detail = secondParam;
    excpHandlerJson.Parameters = params; 

return excpHandlerJson;
}
function deleteDocument(processName,activityName){      //Bug 67762
    
    /* processName is name of the process
	activityName is name of the activity
	return false for disable the delete document
    */
    
    return true;
}

function CustomQuickSearchPrefix(prefix,selectedVar){//Bug 66126
    /*if(selectedVar != '0'){
    prefix="*"+prefix+"*";
    }*/
    return prefix;
}

function isDefaultDocumentType(processName,activityName){
    
    return "";
}


function modifyCropDocComment(strprocessname,stractivityName,strDocName,X1,Y1,X2,Y2,currentPageNo){
    /* 
     * This funcion is used to send the text for Comments in Crop Document Window..
     *
     * strprocessname  - Contains the Process  Name
     * stractivityName - Contains the Activity Name
     * strDocName      - conatins the Name of  currently selected Document in Workitem Window
     * X1: start x coordinate of selected dynamic zone(cropped Image). Coordinate will be according to image dimension.
     * Y1: start y coordinate of selected dynamic zone(cropped Image). Coordinate will be according to image dimension.
     * X2: End x coordinate of selected dynamic zone(cropped Image). Coordinate will be according to image dimension.
     * Y2: End y coordinate of selected dynamic zone(cropped Image). Coordinate will be according to image dimension.
     * currentPageNo: Current Page Number of document  
     *   */  
    return "";
}
function getCropDocTypeListExt(docListObj)
{
var docTypeList = new Array();
//var count = -1;

// How to Capture Existing Doc List

    /*var tmpDocTypeList = docListObj;
    for(i=0 ; i<tmpDocTypeList.length; i++)
    {
     alert("Doc Type = "+tmpDocTypeList.options[i].value);
    }
*/

// Return custom Doc List Array as below , It must be from existing above

//   docTypeList[++count] = "tabc";
//   docTypeList[++count] = "txyz";


return docTypeList;
}

function setTodoListValueHook(todoName,todoValue){
    /*
     *This functions is set the value in picklist TodoList.
     *todoName  - Name of Todo
     *todoValue - Value of todoList to be compared against.
     *
     *
     **/
    var window_workdesk="";
    if(windowProperty.winloc=="M")
        window_workdesk=window;
    else
        window_workdesk=window.opener;

    var winList=window_workdesk.windowList;
    var todoWindow = getWindowHandler(winList,"todoGrid");
    var todoId = 'wdesk:'+ todoName
    
    if(winList){
      var objCombo= todoWindow.document.getElementById(todoId);

      if(objCombo){
            for(var count=0;count<objCombo.length;count++)
            {
                if(objCombo[count].value == todoValue)
                {  
                    objCombo.selectedIndex = count; 
                    break;
                }
            }
      }
                
    }   
}
function disableNewBtnIfDocPresent(){
    /*
        return true : disable New Radio button if Document of this docType is present 
        return false: donot disable New Radio button if Document of this docType is present 
    */       
     return false;
    
}


function saveSubFormData(){
    document.getElementById("updateAttrXml").click();
}

function refreshNGFormFromSubForm(){
    window.opener.document.getElementById("cmdFormRefreshFromSub").click();
}

function updateParentForm(data){
    if( data.status == "success"){
        window.opener.document.getElementById("subformdata").value = document.getElementById("subformdata").value;
        window.close();
        refreshNGFormFromSubForm();
    }
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

function removeMasking(){
    setTimeout( function(){
        RemoveIndicator("temp");
        if(document.getElementById("fade")){
            document.getElementById("fade").style.display="none";
        }
    }
    , 10);    
}
 

function documentPostHook(docIndex, eventType, docName, errorMessage)
{
    //eventType == 'checkin' || eventType == 'getdocument' || eventType == 'new' || eventType == 'newversion' || eventType == 'scan'
    //Here if docIndex>0, it means document has been imported successfully else failure has occured.
    //If success then docName consists of name of imported document and errorMessage will be blank.
    //If failure then errorMessage contains the error message and docIndex will be blank.
    //Bug 66587 - Kindly provide Post Hook For Scan and Import Document.
    var contentDiv = document.getElementById("contentDiv");	
    if(contentDiv != null){       
        initCustomEvents(contentDiv);        
    }     
	   
    var docviewer = document.getElementById("docviewer");	
    if(docviewer != null){
        $(docviewer.contentWindow.document).ready(function() {
            docviewer = document.getElementById("docviewer");
            initCustomEvents(docviewer.contentWindow);        
        });         
    }     
	   
    var docDiv = document.getElementById("docDiv");	
    if(docDiv != null){
        initCustomEvents(docDiv);        
    }
	
	//below function by saurabh for incoming doc new	
	   var docWindowObj="";
	   if(window.parent.strprocessname!=''){
		docWindowObj=window.parent;
		}
		else{
			docWindowObj=window.opener.parent;
		}
	   //ended
        var pname=docWindowObj.strprocessname;
    if(pname=='RLOS')
    {
        // ++ below code already present - 09-10-2017 for document index
        if(eventType=='new' || eventType=='scan' )
        {
			//var rowNumber=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('RowNum').value;
            var docIndex_Id="IncomingDocNew_Docindex";
            var docName_Id="IncomingDocNew_DocName";
            var docStatus_Id="IncomingDocNew_Status";
            if(docIndex_Id!=''){
               docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docStatus_Id).value="Received";
            }    
            var docName=docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docName_Id).value;
            if(docName=='AECBconsentform')
            {
				docWindowObj.document.getElementById("ngformIframe").contentWindow.setNGValue('cmplx_Liability_New_AECBconsentAvail',true);
				docWindowObj.document.getElementById("ngformIframe").contentWindow.setEnabled('Liability_New_fetchLiabilities',true);
            }    
            docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docIndex_Id).value=docIndex;
        }        
    }
    //for pl process docindex setting 
     if(pname=='PersonalLoanS')
    {
        if(eventType=='new'|| eventType=='scan')
        {
            /* var rowNum=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('Rownum').value;
            var doc_docIndex="IncomingDoc_Frame2_Reprow"+rowNum+"_Repcolumn11";
           
            docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(doc_docIndex).value=docIndex;
			var docStatus_Id="IncomingDoc_Frame2_Reprow"+rowNum+"_Repcolumn3";
            if(docIndex!=''){
               docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docStatus_Id).value="Received";
            }  */
			
			//var rowNumber=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('RowNum').value;
            var docIndex_Id="IncomingDocNew_Docindex";
            var docName_Id="IncomingDocNew_DocName";
            var docStatus_Id="IncomingDocNew_Status";
            if(docIndex_Id!=''){
               docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docStatus_Id).value="Received";
            }    
            var docName=docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docName_Id).value;
            if(docName=='AECBconsentform')
            {
				docWindowObj.document.getElementById("ngformIframe").contentWindow.setNGValue('cmplx_Liability_New_AECBconsentAvail',true);
				docWindowObj.document.getElementById("ngformIframe").contentWindow.setEnabled('Liability_New_fetchLiabilities',true);
            }    
            docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docIndex_Id).value=docIndex;
           
        }            
    }
    if(pname=='CreditCard' || pname=='DigitalOnBoarding')
    {
        if(eventType=='new'|| eventType=='scan')
        {
            /* var rowNum=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('Rownum').value;
            var doc_docIndex="IncomingDocument_Frame_Reprow"+rowNum+"_Repcolumn11";
			var docName_Id="IncomingDocument_Frame_Reprow"+rowNum+"_Repcolumn0";
            var docStatus_Id="IncomingDocument_Frame_Reprow"+rowNum+"_Repcolumn3";
            if(docIndex!=''){
               docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docStatus_Id).value="Received";
            }    
            document.getElementById("ngformIframe").contentWindow.document.getElementById(doc_docIndex).value=docIndex; */
			
			//var rowNumber=docWindowObj.document.getElementById('ngformIframe').contentWindow.document.getElementById('RowNum').value;
            var docIndex_Id="IncomingDocNew_Docindex";
            var docName_Id="IncomingDocNew_DocName";
            var docStatus_Id="IncomingDocNew_Status";
            if(docIndex_Id!=''){
               docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docStatus_Id).value="Received";
            }    
            var docName=docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docName_Id).value;
            if(docName=='AECBconsentform')
            {
				docWindowObj.document.getElementById("ngformIframe").contentWindow.setNGValue('cmplx_Liability_New_AECBconsentAvail',true);
				docWindowObj.document.getElementById("ngformIframe").contentWindow.setEnabled('Liability_New_fetchLiabilities',true);
            }    
            docWindowObj.document.getElementById("ngformIframe").contentWindow.document.getElementById(docIndex_Id).value=docIndex;
        }            
    }
    //for cc process docindex setting 
    return true;
}

function isOpenWI(strSelQueueName,strFrom,processName,activityName, workitemId, InstanceName){
  
    /*
         strSelQueueName: selected QueueName (in case of search it is blank)
         strFrom        : W or F or S
         W              : Workitem list form any queue
         F              : Workitem list form Filter
         S              : Workitem list form Search
         processName    : Process Name
         activityName   : Activity name
         workitemId     : Workitem ID
         InstanceName   : Registration No 
    */
    return   true;
}


function LaunchAppOnWIClick(pid,wid,activityName){
	
    /*if(activityName=='Verification')
    {
	  
        var divBView = document.getElementById('BView');
        if(divBView == null){
            divBView = document.createElement("div");
            divBView.style.display = "none";
            divBView.id = "BView";
            divBView.name = "BView";
            document.getElementById("mainbody").appendChild(divBView);    
        }    
    
        // Name of application to be launched.
        // var appName = "C:\\Program Files\\OmniExtract 6.1\\OmniExtract Common\\DataVerify.exe";
        // Arguments to be passed to the application.
        var appArgs = pid+"~"+sessionId+"~"+strUserName;
	
        //var serverRef = window.top.location.href;   
        //var serverPath = serverRef.substring(0, serverRef.indexOf(sContextPath));    
    
        try{	
            StartSessionActiveTimer();	
            document.getElementById('BView').innerHTML = '<OBJECT ID="AppLauncherProject.AppLauncher" WIDTH="1" name="AppLauncher" HEIGHT="1"  CLASSID="CLSID:A40626AB-E946-49B6-8E21-4D3B8DC71765"></object>';   
            document.AppLauncher.LaunchApplication(decode_utf8(appArgs));
            return true;
        }
        catch(e)
        {
            return false;
        }
    }*/
    return false;
}

function customSearchValidation(value){
    var returnFlag=true;
    /* var invalidChar=['-','>','<','\'','"'];    
    for(var i=0;i<invalidChar.length;i++){
        if(value.indexOf(invalidChar[i])>-1){
            returnFlag= false;
            break;
        }
    }*/
    return returnFlag;
}

function refreshDMSSearch(processName,activityName){
    // Call the fuction which is getting called after document import
}

function modifyDocName(processname,strDocName,from,activityname,username){
    var customName="";
    if(from == 'crop_document' && typeof nameUploadDocument != 'undefined') {
        return nameUploadDocument(strDocName,from,processname, activityname,username);
    }

    /*Assign value in custmName variable here*/

    if(customName != "")
        return customName;
    else
        return strDocName;
}

function raiseClick(){
    /*
        Custom function to enable exception raising conditionally.
    */       
     return true;
}

function getWaterMarkText(strprocessname,stractivityName,pid){
    var strWaterMarkText="";
    /* 
     * This funcion is used to send the text for WaterMark printing that will be  displayed in the downloaded document.
     * 
     * 
     * strprocessname  - Contains the Process  Name
     * stractivityName - Contains the Activity Name
     * pid             - conatins the currently opened Workitem Number
     * 
     * If returned "" - it will not display any waterMark text on the downloaded document
     * else
     * (for example)return "Mytext" : then "Mytext" will be displayed as waterMark text on the downloaded document
     * 
     * TextSettings for WaterMarkText can be set  using "WaterMarkTextSettings" flag in webdesktop.ini
     * */
    if(typeof docCreationDateTime != "undefined") 
    strWaterMarkText =  docCreationDateTime;  
        
    return strWaterMarkText;
}


function checkOutDocHook(docIndex,docName){//Bug 66696
    return true;
}


function checkInDocHook(docIndex,docName){//Bug 66696
    return true;
}

function linkWiPreHook(linkWiDataXml) {     //Bug 62856
    return true;
}

function delinkWiPreHook(linkWiDataXml) {    //Bug 62856
    return true;
}
//Bug 68022 starts
function disableCaseDone(){
    return false;
}
function disableCaseReassign(){
    return false;
}
function disableCasePriority(){
    return false;
}
function disableCaseSetReminder(){
    return false;
}
function disableTaskDone(){
    return false;
}
function disableTaskPriority(){
    return false;
}
function disableTaskSetReminder(){
    return false;
}
 //Bug 68022 ends
 function  saveExcpPostHook(calledFrom){ //Bug 68117
    //alert(" calledFrom "+calledFrom)
    //if(calledFrom=='Raise' || calledFrom=='Modify' || calledFrom=='Reject'|| calledFrom=='Respond'|| calledFrom=='Undo'|| calledFrom=='Commit'|| calledFrom=='Clear'){
     //do something   
    //}
  return true;  
}
//Bug 67103 starts
function isTiffConversionRequired(strProcessName, strActivityName, selDocType) {
    /**
      *Return true from this function for the document types for which tiff conversion is required while cropping.
      *Returning true from this function will convert the cropped image in tiff && BW that will reduce the cropped image size.
      *Otherwise the cropped image format will be jpg and the image quality will remain unchanged.
      *Return true is recommended only for images that contains text(e.g: signature)
     **/
    return false;
}

function initKeyEvent(){
    /*if (typeof window.event != 'undefined'){ 
        // IE        
        document.onkeydown = function(event) {
            var t=event.srcElement.type;
            var kc=event.keyCode;      
          
            if(event.keyCode==68 && event.ctrlKey){                    
                // Ctr+D
                openDocList();
            }        
        }
    } else {        
        // FireFox/Others
        document.onkeypress = function(e) {     
            var t = e.target.type;
            var kc = e.keyCode;      
            
            if(e.which==100 && e.ctrlKey) {
                // Ctr+D
                openDocList();
            } else {
                if(e.stopPropogation){
                    e.stopPropogation(true);
                }
                if(e.preventDefault){
                    e.preventDefault(true);
                }
                e.cancelBubble = true;
                return false;
            }     
        }
    }*/
    initCustomEvents(document);	
}

//Bug 68517
function wordeskWindowTitle(strprocessname, stractivityName, strQueueName, WDeskWinTitle) {
    // return the wdeskWinTitleCustom to replace the Queue name instead of Activity Name
//    var wdeskWinTitleCustom = "";
//	var t1 = WDeskWinTitle.substring(WDeskWinTitle.indexOf(":")+2,WDeskWinTitle.length);
//    if (strQueueName == '' || strQueueName == "undefined" )
//		{
//		wdeskWinTitleCustom = WDeskWinTitle; 
//		}
//	
//	else
//	{
//    wdeskWinTitleCustom = t1 + " : " + strQueueName ;
//	}
//    return wdeskWinTitleCustom;
        return "";
}
 //Bug 68517
 
 function IsCompleteWorkItem(queueName){
    return true;
}

function enableZonePartition(strprocessname, stractivityName){
    //opall_toolkit.setFormExtractionMode(true);
    return true;
}
function customFormValidation(){
    return true;
}
function customLinkWIHeader() {
    //sample code to receive ProcessInstanceId of selected workitem
    /*
    var ctrlTableId="frmworkitemlist:pnlResult";
    var checkboxId="frmworkitemlist:checkBox_";
    var ctrlTable=document.getElementById(ctrlTableId);
    var pid="";
    var strSelectedIndex="";
    if(ctrlTable!=null)
    {

        var rowCount = ctrlTable.tBodies[0].rows.length;
        if(rowCount>0) {
            for(var iCount = 0; iCount < rowCount-1;iCount++)
            {
            
                var wiClicked=document.getElementById(checkboxId+iCount);
                if(wiClicked.checked){
                    if(strSelectedIndex.length==0){
                        strSelectedIndex=strSelectedIndex+iCount;
                    }else{
                        strSelectedIndex=strSelectedIndex+","+iCount;
                    }
                    var jsonOutput=document.getElementById("frmworkitemlist:hidWIJson"+(iCount+1)).innerHTML;
                    jsonOutput= eval("("+jsonOutput+")");
                    var arrobjJsonOutput= jsonOutput.Outputs;
                    for(var i=0;i<arrobjJsonOutput.length;i++){
                        var outputJson=arrobjJsonOutput[i];
                        var objJson=outputJson.Output;
                        if(objJson.Name=='ProcessInstanceID'){
                            pid =encode_utf8(objJson.Value);
                        }
                    }
                }
            }
        }
    }*/
    return true;
}

function interfacePostHook(interfaceType) {
    /* interfaceType doc for "Document", interfaceType form for "form", interfaceType exp for "exception", interfaceType todo for "Todo" 
       strProcessname :   Name of the Current Process
       strActivityname:   Name of the Current Activity
    */
  
    if(interfaceType == "doc") {
        //code snippet to hide or show addDoc as per workstep (activity Id)
        /*
	if(stractivityId=='6') {
            document.getElementById('wdesk:addDoc').style.display = "none";
            document.getElementById('wdesk:addDoc_nodoc').style.display = "none";
	}
        */
			
        initCustomEvents(document);
		
        var contentDiv = document.getElementById("contentDiv");	
        if(contentDiv != null){       
            initCustomEvents(contentDiv);        
        }           
		   
        var docviewer = document.getElementById("docviewer");	
        if(docviewer != null){                                
            $(docviewer.contentWindow.document).ready(function() {
                docviewer = document.getElementById("docviewer");
                initCustomEvents(docviewer.contentWindow);        
            }); 
        }
		   
        var docDiv = document.getElementById("docDiv");	
        if(docDiv != null){
            initCustomEvents(docDiv);        
        }  
    } else if(interfaceType == "form") {        
        var ngformIframe = document.getElementById("ngformIframe");	       	   
        if(ngformIframe != null){
            $(ngformIframe.contentWindow.document).ready(function() {
                initCustomEvents(ngformIframe.contentWindow); 
            });                                     
        }
    } else if(interfaceType == "exp") {
        initCustomEvents(document); 
    } else if(interfaceType == "todo") {
        initCustomEvents(document); 
    }
}

function deleteDocPostHandler(docIndex) {
    //This is the handler called after deleting a document, here docIndex is being passed where docIndex= -1 represents Error.
}

function DualMonitorWidth() {
    //In this function you need to specify the width of workitem window when it is opened. If it is required to open in half following can be used.
    winWidth = "";
    /*
     winWidth = 2*(window.screen.availWidth-10.01);
    */ 
    return winWidth;
} 
function postSaveFormHook(status,statusCode) {
    // This function will get called after save form on workitem 
    // status: success or failure
    // status code :200 - success, :598,599 : failure
    
    //alert("status:" +status +" statusCode: "+statusCode)
    
    
} 
function preHandleOptionsHook(queueId,queueName,processDefId,oper)
{
// In this function , you can enable disable options like New, Done before loading workitemlist
// parameters queueId, queueName
//oper =1 - on queueclick, 2- set filter, 3- Advanced search, 4- on quick search, 
   // console.log("queueId"+queueId+"queueName"+queueName+"processDefId"+processDefId+"processName"+processName+"oper"+oper)
    
    // sample code snippet for New, Similarly for Done id= frmworkitemlist:disNewDone,frmworkitemlist:NewDone
     /* if(queueId=='188'){
        if(document.getElementById("frmworkitemlist:disNewShow")){
            document.getElementById("frmworkitemlist:disNewShow").style.display="none";
            document.getElementById("frmworkitemlist:disNewShow").parentNode.nextElementSibling.children[0].style.display="none";
        }
         if(document.getElementById("frmworkitemlist:NewShow")){
            document.getElementById("frmworkitemlist:NewShow").style.display="none";
            document.getElementById("frmworkitemlist:disNewShow").parentNode.nextElementSibling.children[0].style.display="none";
        }
    }*/
       
}

function isPdftoolbarenable(strprocessname, stractivityName) {
    /*
     strprocessname    : Process Name
     stractivityName   : Activity Name
     
     return true if you want to enable toolbar on non image pdf document
     */

    return false;
}

function DeleteWIClick()
{
    return true;
}
function cancelSaveEditLayoutHook(fromOp)
{
   // if fromOp='S' then it comes when click on save button of the edit layout, if fromOp='X' then then it comes 
   // when click on cancel button of the edit layout  
    return true;
}
function introduceNDonePostHook(event,option){   
    /*
     * 
     strprocessname    : Process Name
     stractivityName   : Activity Name
     pid               : Process Instance Id
     wid               : Workitem Id
    */
    /* 
        var alertMsg="Workitem "+pid+" has been completed";
        if(event=='closeWI' && (option=='INTRODUCE' || option=='DONE')){
            alert(alertMsg);
        }    
    */
}
function hideConversation(docRef){  
    /*
        if(strprocessname=='appletform'){     
            docRef.getElementById('textForm:LblAddAsNew').style.display='none';
            docRef.getElementById('textForm:ChkAddAsNew').style.display='none';
          return true;
        } 
    */
    return false;
}

function enableDocDownloadFromVersion(strprocessname,stractivityName,userName) {
    return true;
}

function disableDeleteForOldVersion(strprocessname,stractivityName,userName) {
    return false;
}

//function to disable drag and drop on workitem window
function initCustomEvents(element){
    element = (typeof element == "undefined")? null: element;
	   
    if(element == null) {
        element = document;
    }
	   
    element.addEventListener("dragover",function(e){
        e = e || event;
        cancelBubble(e);
    },false);
	 
    element.addEventListener("drop",function(e){
        e = e || event;
        cancelBubble(e); 
    },false);
}

function isChromeOfficeViewer() {
    return false;
}

function refreshParentFormData(data){
    if(data.status == "success"){
        if(window.subFormCloseHook){
            subFormCloseHook();
        }
    }
    
}
function documentListWinHook(strprocessname,stractivityName,strqueuename,strqueueID) {
    return true;
}

function hideAnnotationForWorkstep(strprocessname,stractivityName,strQueueName){
    return true;
}

function openSingleWorkitem(pdefId){
    
    //please return true to restrict opening of  same process WI in new window if WI of that process is already opened
    
    return false;
}
function isCustomWorkitem(PrcDefId,queueId,queueType){
    var customNewWI = false;    
    return customNewWI;
}