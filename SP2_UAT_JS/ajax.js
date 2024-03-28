if( !oforms )
var oforms = {};

if(!oforms.ajax)
{
    oforms.ajax =
    {        
        strResponse:null,
        processRequest:function(pParam,pUrl)
        {       
            this.strResponse = null;
            if( pUrl != undefined && pUrl != "undefined")
                this.sendRequest(pUrl, pParam);            
            else
                this.sendRequest("../action/actionhandler", pParam);            
            return this.strResponse;
        },
        createXMLHttpRequest:function()
        {
            try
            {
                return new ActiveXObject("Msxml2.XMLHTTP");
            } 
            catch(e)
            {}
            try
            {
                return new ActiveXObject("Microsoft.XMLHTTP");
            }
            catch(e)
            {}
            try
            {
                return new XMLHttpRequest();
            } 
            catch(e)
            {}
            alert("XMLHttpRequest not supported");
            return null;
        },

        sendRequest:function(pUrl,pData)
        {
            if(pUrl.indexOf("?")>0) 
            {
                pUrl=pUrl + "&sid="+ Math.random();
            }
            else 
            {
                pUrl=pUrl + "?sid="+ Math.random();
            }
            oforms.ajax.strResponse = "";
            objReq = this.createXMLHttpRequest();
            objReq.open("POST", pUrl, false);
            objReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8"); 
            objReq.onreadystatechange = this.onResponse;
            objReq.send(pData);
        },

        onResponse:function()
        { 
            try 
            {            
                    if (objReq.readyState==4) 
                    {   
                        if (objReq.status==200)
                        {
                            oforms.ajax.strResponse = objReq.responseText;                            
                        }
                        else if ( objReq.status==12029) //Bugzilla – Bug 53510
                        {
                            oforms.util.showError("Unable to connect to server");
                            return;
                        }
                    }
                }
            catch(e)
            {
                alert("Error fetching data.");
            }
        }
    }
}