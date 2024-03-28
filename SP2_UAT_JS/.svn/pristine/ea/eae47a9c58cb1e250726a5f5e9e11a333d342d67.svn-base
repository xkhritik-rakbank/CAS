/*
 *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
 *12/12/2014        Mohit Sharma          Bugzilla – Bug 52591 Subform API Not Available for OF 10 HTML
 *8/01/2015         Shaveta Rani          Bugzilla – Bug 52631 SubForm Window container's height is not same as the height of Subform.
 *30/06/2015        Mohit Sharma          Bug 55609 - Messages containing special characters are not thrown in alerts in java code for html forms
 *02/07/2015        Mohit Sharma          Bugzilla – Bug 55615 Messages containing special characters are not thrown in alerts in java code for html forms
 *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/
var wioperation;
var processName;
var activityName;
var username;
function ZoneLostFocus()
{
   // window.parent.ZoneLostFocus();
}

function ZoneGotFocus(x1,y1,x2,y2,name)
{
    //window.parent.ZoneGotFocus(x1,y1,x2,y2,name);
}

function saveForm(type)
{
   wioperation= type;
   document.getElementById("txtEventType").value=type;
   document.getElementById("btnSave").click();   
}

function formSaved(pAjax)
{
    if(pAjax.status == "success")
    {
       // var validated=document.getElementById("bValidated").value;
       // if(validated)
       window.parent.formSaved(wioperation);
    }
}


function setWIAttributes(strprocessname,stractivityName,strUsername)
{
    processName = strprocessname;
    activityName = stractivityName;
    username=strUsername;
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
    excpHandlerJson.Parameters = jsonParam; 
    return excpHandlerJson;
}

//Added for  Bugzilla – Bug 52631
function setSubFormDimensions(pFormName)
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