<%@ page import="java.util.*"%>
<%@ page import="java.text.*" %>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>


<%
	
try
{
//Code calling for Socket utility to generate the utility	

String attrbList = request.getParameter("attrbList");

attrbList=java.net.URLDecoder.decode(attrbList, "UTF-8");
String userEmail="";
String winame = request.getParameter("wi_name");
String docName = request.getParameter("docName");
String sessionId = request.getParameter("sessionId");
String pvalue="";
String prequired="";
if(null!=request.getParameter("preq")){
	prequired=request.getParameter("preq");
	if("Y".equals(request.getParameter("preq"))){
		pvalue = request.getParameter("pval");
	}
}
if(null!=attrbList && !"".equals(attrbList) && attrbList.contains("@#")){
	userEmail = attrbList.split("@#")[1];
	attrbList = attrbList.split("@#")[0];
}
String docStatus="";
String outputDoc="";
String docIndex="";
String statusFinal="";
String mailStatus="";

int gtPort=0;	
String gtIP="";
String gtPortProperty="";
	try {
			String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
		    File file = new File(prop_file_loc);
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
		
			gtIP= properties.getProperty("gtIP");
			gtPortProperty = properties.getProperty("gtPort");
			gtPort = Integer.parseInt(gtPortProperty);
			
        } 
		catch(Exception e){
			
			 e.printStackTrace();
		}

String status=makeSocketCall(attrbList,winame,docName,sessionId,gtIP,gtPort,prequired,pvalue,userEmail);
String statusarr[] = status.split("~"); 
	for(int i=0;i<statusarr.length;i++)
	{
		if(i==0)
			docStatus=statusarr[i];
		if(i==1)
			outputDoc=statusarr[i];
		if(i==2)
			docIndex=statusarr[i];
		if(i==3)
			mailStatus=statusarr[i];
	}

//System.out.println("Generate_Template.jsp: outputDoc   "+outputDoc);

statusFinal = outputDoc+"~"+docIndex;


	
out.print(status);
}
catch(Exception ex)
{
  ex.printStackTrace();
}

%>


<%! 

public String makeSocketCall( String argumentString, String wi_name, String docName, String sessionId,  String gtIP, int gtPort,String prequired, String pvalue,String userEmail)
	{
		String socketParams=argumentString+"~"+wi_name+"~"+docName+"~"+sessionId+"~"+prequired+"~"+pvalue+"~"+userEmail;
	
	System.out.println("socketParams -- " + socketParams);

		Socket template_socket = null;
		DataOutputStream template_dout=null;
		DataInputStream template_in=null;
		String result="";
		try {
			//Socket write code started
			template_socket  = new Socket(gtIP, gtPort);
			template_dout=new DataOutputStream(template_socket.getOutputStream());
			if (socketParams != null && socketParams.length() > 0) 
			{
				template_dout.write(socketParams.getBytes("UTF-8"));
				template_dout.flush();
			} else {
				notify();
			}
			//Socket write code ended and read code started
			template_socket.setSoTimeout(60*1000);
			template_in = new DataInputStream (new BufferedInputStream(template_socket.getInputStream()));
			byte[] readBuffer = new byte[35000];
			int num = template_in.read(readBuffer);
			if (num > 0) {
				byte[] arrayBytes = new byte[num];
				System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
				result = new String(arrayBytes, "UTF-8");
			}
		} 

		catch (SocketException se) {
			se.printStackTrace();
		} 
		catch (IOException i) {	
			i.printStackTrace();
		}
		catch (Exception io) {
			io.printStackTrace();
		}
		finally{
			try{
				if(template_dout != null){
					template_dout.close();
					template_dout=null;
				}
				if(template_in != null){
					template_in.close();
					template_in=null;
				}
				if(template_socket != null){
					if(!template_socket.isClosed()){
						template_socket.close();
					}
					template_socket=null;
				}
			}catch(Exception e)
			{e.printStackTrace();}
		}
		return result;
	}
%>
 

