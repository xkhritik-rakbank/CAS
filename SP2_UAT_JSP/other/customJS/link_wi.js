/*
		

03/11/2011                 28779
18/01/2017                 Bug 62856   

 */
var no_wi_selected=false;
function SaveSelectedIndex(ref){ 
    //Bug 62856 starts
    var linkWiDataXml = "<LinkWiData><ParentWiInfo><ProcessInstanceId>"+pid+"</ProcessInstanceId>";
    if(window.opener.strprocessname)       //define processName in clientwdesk.process
        linkWiDataXml += "<ProcessName>"+window.opener.strprocessname+"</ProcessName>";
    else if(window.opener.opener.strprocessname)
        linkWiDataXml += "<ProcessName>"+window.opener.opener.strprocessname+"</ProcessName>";
    else
        linkWiDataXml += "<ProcessName></ProcessName>";
    if(window.opener.stractivityName)      //define activityName in clientwdesk.process
        linkWiDataXml += "<ActivityName>"+window.opener.stractivityName+"</ActivityName>";
    else if(window.opener.opener.stractivityName)
        linkWiDataXml += "<ActivityName>"+window.opener.opener.stractivityName+"</ActivityName>";
    else
        linkWiDataXml += "<ActivityName></ActivityName>";
    linkWiDataXml += "</ParentWiInfo><LinkWiInfo>";
    //Bug 62856 ends 
    
    var strSelectedIndex="";
    var ctrlTableId="frmlinkedworkitemlist:pnlResult";
    var checkboxId="frmlinkedworkitemlist:checkBox_";
    var ctrlTable=document.getElementById(ctrlTableId);
    var nCount=0;
     if(ctrlTable!=null)
    {

        var rowCount = ctrlTable.tBodies[0].rows.length;
        if(rowCount>0) {
            for(var iCount = 0; iCount < rowCount-1;iCount++)
            {

                var wiClicked=document.getElementById(checkboxId+iCount);
                if(wiClicked.checked){
                    if(strSelectedIndex.length==0){
                        strSelectedIndex=iCount;
                    }else{
                        strSelectedIndex=strSelectedIndex+","+iCount;
                    }
                    //Bug 62856 starts
                    linkWiDataXml += "<LinkWi><ProcessInstanceId>";
                    linkWiDataXml += document.getElementById('frmlinkedworkitemlist:inp_'+iCount+'_processInstanceId').textContent.trim();
                    linkWiDataXml += "</ProcessInstanceId><ProcessName>";
                    linkWiDataXml += document.getElementById('frmlinkedworkitemlist:inp_'+iCount+'_processName').textContent.trim();
                    linkWiDataXml += "</ProcessName><ActivityName>";
                    linkWiDataXml += document.getElementById('frmlinkedworkitemlist:inp_'+iCount+'_activityName').textContent.trim();
                    linkWiDataXml += "</ActivityName></LinkWi>";
                    //Bug 62856 ends
                    nCount++;//changes by Gaurav Asthana 31/10/11
                }
            }
        }
    }
    linkWiDataXml += "</LinkWiInfo></LinkWiData>";//Bug 62856
    if(document.getElementById('frmlinkedworkitemlist:operation').value == 'D' && typeof delinkWiPreHook != 'undefined' && !delinkWiPreHook(linkWiDataXml)) {
        return false;
    }
        
    if(document.getElementById('frmlinkedworkitemlist:operation').value == 'A' && typeof linkWiPreHook != 'undefined' && !linkWiPreHook(linkWiDataXml)) {
        return false;
    }
    //changes by Gaurav Asthana 31/10/11
    if(nCount<1){
        fieldValidator(null, NO_WORKITEM_SELECTED, "absolute", true);
        //alert(NO_WORKITEM_SELECTED);
        return false;
    }
    else{
        document.getElementById('frmlinkedworkitemlist:hidSelectedIndex').value=strSelectedIndex;
        return true;
    }
    //changes end
        
}


