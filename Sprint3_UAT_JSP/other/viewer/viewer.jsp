<%@ page import="ISPack.CPISDocumentTxn" %>
<%@ page import="ISPack.ISUtil.JPISException" %>
<%@ page import="Jdts.Client.*" %>
<%@ page import="Jdts.excp.JtsException" %>
<%@ page import="Jdts.DataObject.JPDBString" %>
<%@ page import="java.io.File" %>
<%
String statusCode = "0";
String errMsg = "";
String []msg= new String[2];
String JtsAddress=request.getParameter("JtsAddress");
short portId = Short.parseShort(request.getParameter("portId"));
String docExt = com.newgen.wfdesktop.util.WDUtility.escapeHtml4(request.getParameter("docExt"));
String documentName = request.getParameter("documentName");
int imageIndex = Integer.parseInt(com.newgen.wfdesktop.util.WDUtility.escapeHtml4(request.getParameter("imageId")));
short volumeId = Short.parseShort(request.getParameter("volIndex"));
short siteId = Short.parseShort(request.getParameter("siteId"));
String cabinetName = request.getParameter("CabinetName");
String userDBid = com.newgen.wfdesktop.util.WDUtility.escapeHtml4(request.getParameter("userDBid")); 
String docDownloadLocation=  System.getProperty("user.home") + File.separator + "viewer" + File.separator +  userDBid + "$" + imageIndex + "." + docExt;
String UserIndex = null;
JPDBString oSiteName = new JPDBString();

try{
	
File directory = new File(System.getProperty("user.home") + File.separator + "viewer");
if (! directory.exists()){
	directory.mkdir();
}
CPISDocumentTxn.GetDocInFile_MT(null, JtsAddress, portId, cabinetName, siteId,
volumeId, imageIndex, UserIndex, docDownloadLocation, oSiteName);
}
catch (JPISException jpEx) {
//jpEx.printStackTrace();
statusCode = "" + jpEx.m_nError;
errMsg = jpEx.m_strMsg;
} catch (Exception Ex) {
//Ex.printStackTrace();
statusCode = "-20WGE11";
errMsg = Ex.getMessage();
}
if (statusCode.equals("0")) {
msg[0] = statusCode;
msg[1] = "File download on "+docDownloadLocation+" successfully!";
} else {
msg[0] = statusCode;
msg[1] = errMsg;
}

%>

<html> 
        <head>
            <script>            
                // custom scripts here
                
            </script>
        </head>
        <body>
         Status code  <%=msg[0]%>
		 Message <%=msg[1]%>
        </body>
</html>