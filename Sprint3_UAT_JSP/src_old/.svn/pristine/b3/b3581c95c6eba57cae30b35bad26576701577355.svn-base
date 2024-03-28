<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank
//File Name					 : DeleteSignFromServer.jsp
//Author                     : Mandeep
//Date written (DD/MM/YYYY)  : 18-01-2016
Description                  : File is used to delete signature from server
//---------------------------------------------------------------------------------------------------->
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.custom.*" %>
<%@ page import="java.io.*,java.util.*,java.math.*"%>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import ="java.text.DecimalFormat"%>
<%@ include file="Log.process"%>
<%

	//String filePath = System.getProperty("user.dir")+File.separatorChar+"CustomLog"+File.separatorChar+"RLOS";//request.getParameter("filePath");
	//String filePath = System.getProperty("user.dir")+File.separatorChar+"RLOSSignature";
	
	String filePath =System.getProperty("user.dir")+File.separatorChar+"installedApps"+File.separatorChar+"ant1casapps01Node01Cell"+File.separatorChar+"webdesktop.ear"+File.separatorChar+"webdesktop.war"+File.separatorChar+"CustomForms"+File.separatorChar+"RLOS";
	
	String debt_acc_num = request.getParameter("debt_acc_num");
	String returnedSignatures = request.getParameter("returnedSignatures");

	int totalSign =  Integer.parseInt(returnedSignatures);
	
	WriteLog("filePath inside the DeleteSignFromServer.jsp----"+filePath);
	
	String filePathWithAccNum = filePath + File.separatorChar + debt_acc_num;
	
	WriteLog("filePathWithAccNum----"+filePathWithAccNum);
	
	for (int i = 0;i < totalSign;i++)
	{
		String fileTemp = filePathWithAccNum+"imageCreatedN"+i+".jpg";
		
		try {
			File f = new File(fileTemp);
			if(f.exists())
			{
				WriteLog("Deleting file ----"+fileTemp);
				if(f.delete())					
					WriteLog("Sucessfully deleted file- " + fileTemp);
				else							
					WriteLog("Error in deleting file- " + fileTemp);
			}
		}
		catch (Exception e)
		{			
			WriteLog("Error in deleting file- " + fileTemp);
		}
		
	}
%>