
function myTrim(x) 
{	
    return x.replace(/^\s+|\s+$/gm,'');
}

function removeAll(data,searchfortxt)
{ 
	var startIndex=0;
	while(data.indexOf(searchfortxt)!=-1)
	{
		data=data.substring(startIndex,data.indexOf(searchfortxt))+data.substring(data.indexOf(searchfortxt)+searchfortxt.length,data.length);
	}	
	
	return data;
}

function replaceAll(data,searchfortxt, replacetxt)
{ 
	var startIndex=0;
	while(data.indexOf(searchfortxt)!=-1)
	{
		data=data.substring(startIndex,data.indexOf(searchfortxt)) + replacetxt + data.substring(data.indexOf(searchfortxt)+searchfortxt.length,data.length);
	}	
	
	return data;
}

function removeallspaces(data)
{   
	return data.replace(/\s/g, ""); 
    	 
}

function removedoublespaces(data)
{   
	return data.replace(/\s{2,}/g, ' ');	

}

function containing(data, searchfortxt)
{ 
	if(data.indexOf(searchfortxt)!=-1)
		return true;
	else 
		{
		return false;}
}

function index_of_substring(data, searchfortxt)
{
    if(data.indexOf(searchfortxt)!=-1)
    	{
    	
    	 return data.indexOf(searchfortxt);
    	}
    
}
function mandatory_document(data, DOCUMENT_VALUE)
{
	if(data != DOCUMENT_VALUE|| data == ""|| data == null)
		{
			alert("please attach the document");
			return false;
		}
	else
		{return true;
		
		}
}
function atleast_one_document(data)
{
	if(data == null|| data == "")
		{
			alert("please attach atleast one  document");
			return false;
		}
	else
		return true;
}

function replaceUrlChars(sUrl)
{	
	return sUrl.split("+").join("ENCODEDPLUS");
}

function extractvalue(data)
{ 
return data.replace(/,/g, "");
	
}

function calculatedays(datefield, datefield1)
{
	var dd1=datefield.substring(0,2);
	var mm1=datefield.substring(3,5);
	var yy1=datefield.substring(6,10);
	var depDate1=yy1+'/'+mm1+'/'+dd1;
	var Cur1Date=datefield1;
	var dd2=Cur1Date.substring(0,2);
	var mm2=Cur1Date.substring(3,5);
	var yy2=Cur1Date.substring(6,10);
	var CurDate1=yy2+'/'+mm2+'/'+dd2;
	var CurDate2=new Date(CurDate1);
	var depDate2=new Date(depDate1);
	var days = ((depDate2.getTime() - CurDate2.getTime())/(1000*60*60*24));	

	return days;

}