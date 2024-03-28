<%@ page import="com.newgen.reschedule.Payment" %>
<%@ page import="com.newgen.reschedule.PaymentFactory" %>
<%@ page import="com.newgen.bean.ResponseVO" %>
<%@ page import="com.newgen.bean.LoanSchedule" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.*"%>
<%@ page import="com.newgen.reschedule.XLSXReaderWriter" %>
<%@ page import="java.text.*" %>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.wfdesktop.util.xmlapi.*" %>
<%@ page import="com.newgen.custom.*" %>

<%
	
try
{
	


double loan=Double.parseDouble(request.getParameter("LoanAmount"));
//System.out.println("loan :"+loan);
double rate=Double.parseDouble(request.getParameter("Rate"));
int months=Integer.parseInt(request.getParameter("Tenure"));
// months=years*12;

String moratorium1="";
int morotorium=0;
if(moratorium1.equalsIgnoreCase("") || moratorium1.equalsIgnoreCase(null))
{}
else
	morotorium=Integer.parseInt(moratorium1);
	
	
int mqPort=0;	
String mqIP="";
String mqPortProperty="";
	try {
			String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
		    File file = new File(prop_file_loc);
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
		
			mqIP= properties.getProperty("mqIP");
			mqPortProperty = properties.getProperty("mqPort");
			mqPort = Integer.parseInt(mqPortProperty);
			
        } 
		catch(Exception e){
			
			 e.printStackTrace();
		}


String rescheduleType=request.getParameter("RepaymentFrequency");
Double days360=null;
String disbursement_Date=request.getParameter("BalloonAmount");
String firstEMI_Date=request.getParameter("GracePeriod");
Double emi=null;
int days360Int=0;
int emiInt=0;
String emi_DaysDiff=makeSocketCall(loan,rate,months,disbursement_Date,firstEMI_Date, mqIP, mqPort);
//System.out.println("emi_DaysDiff   "+emi_DaysDiff);
String[] tempArr=emi_DaysDiff.split("~");

for(int i=0;i<tempArr.length;i++)
{
  if(i==0)
	  emi=Double.parseDouble(tempArr[i]);
   if(i==1)
   {
	  days360=Double.parseDouble(tempArr[i]);
      days360Int=days360.intValue();
   }
}
//emiInt=emi.intValue();
//System.out.println("emiInt   "+emiInt);
//System.out.println("days360Int"+days360Int);


String paymentType="Equated";

Payment paymentObj= PaymentFactory.getInstance(paymentType,loan,rate,months,days360Int,emi,disbursement_Date,firstEMI_Date,morotorium,rescheduleType);
ResponseVO<LoanSchedule> responseVO=paymentObj.saveReport("abc.txt");
List<LoanSchedule> resData=new ArrayList<LoanSchedule>();
		resData=responseVO.getData();
		String orgXML="";
		for(int i=0;i<resData.size();i++){
			orgXML=orgXML+"<data>";
			orgXML=orgXML+"<date>"+resData.get(i).getDate()+"</date>";
			orgXML=orgXML+"<daysInMonth>"+resData.get(i).getDaysInMonth()+"</daysInMonth>";
			orgXML=orgXML+"<emi>"+resData.get(i).getEmi()+"</emi>";
			//orgXML=orgXML+"<loanBalance>"+resData.get(i).getLoanBalance()+"</loanBalance>";
			orgXML=orgXML+"<month>"+resData.get(i).getMonth()+"</month>";
			orgXML=orgXML+"<monthlyInterest>"+resData.get(i).getMonthlyInterest()+"</monthlyInterest>";
			orgXML=orgXML+"<monthlyPrincipal>"+resData.get(i).getMonthlyPrincipal()+"</monthlyPrincipal>";		
			orgXML=orgXML+"<openingPrinciple>"+resData.get(i).getOpeningPrinciple()+"</openingPrinciple>";
			orgXML=orgXML+"<closingPrinciple>"+resData.get(i).getClosingPrinciple()+"</closingPrinciple>";
			orgXML=orgXML+"<lifeInsurance>"+resData.get(i).getLifeInsurance()+"</lifeInsurance>";
			orgXML=orgXML+"<propertyInsurance>"+resData.get(i).getPropertyInsurance()+"</propertyInsurance>";
			orgXML=orgXML+"<excessInterest>"+resData.get(i).getExcessInterest()+"</excessInterest>";
			orgXML=orgXML+"<totalRepayable>"+resData.get(i).getTotalRepayable()+"</totalRepayable>";
			orgXML=orgXML+"<advFlag>"+resData.get(i).getAdvFlag()+"</advFlag>";

			orgXML=orgXML+"</data>";
		}
		
		
		String xmlResult1=orgXML;


//out.print(xmlResult1); 


Gson gson = new Gson();
String jsonInString = gson.toJson(responseVO);
	
out.print(jsonInString+"@"+xmlResult1+"@"+emi);
}
catch(Exception ex)
{
  ex.printStackTrace();
}

%>

	

<%! 
public String makeSocketCall(double loanAmount,double rate,int months,String disbursement_Date,String firstEMI_Date, String mqIP, int mqPort )
{
		String socketParams=loanAmount+"~"+rate+"~"+months+"~"+disbursement_Date+"~"+firstEMI_Date;
		//request.getParameter("LoanAmount");
		//System.out.println("socketParams="+socketParams);
		Socket socket = null;
		DataOutputStream dout=null;
		DataInputStream in = null;
		String result="";
		try {
			socket  = new Socket(mqIP, mqPort);
			dout=new DataOutputStream(socket.getOutputStream());
			if (socketParams != null && socketParams.length() > 0) 
			{		dout.write(socketParams.getBytes("UTF-16LE"));
			dout.flush();
			} else {
				notify();
			}
			socket.setSoTimeout(60*1000);
			in = new DataInputStream (new BufferedInputStream(socket.getInputStream()));
			byte[] readBuffer = new byte[3500];
			int num = in.read(readBuffer);
			if (num > 0) {
				byte[] arrayBytes = new byte[num];
				System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
				result = new String(arrayBytes, "UTF-16LE");
				//System.out.println("Result:::"+result);
			}        
		} 
		catch (SocketException se) {
			se.printStackTrace();
		} 
		catch (IOException i) {	
			i.printStackTrace();
		}
		finally	
		{
			try{
				if(socket!=null)
				{
					if(!socket.isClosed()){
						socket.close();
					}
					socket=null;
				}
				if(dout!=null)
				{
					dout.close();
					dout=null;
				}
				if(in!=null)
				{
					in.close();
					in=null;
				}
			}
			catch (Exception e)
			{
			}
		}
		return result;
}
%>
 

