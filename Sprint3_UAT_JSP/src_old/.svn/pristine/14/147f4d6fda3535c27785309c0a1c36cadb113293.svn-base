<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.lang.Object"%>
<%@ page import="java.util.LinkedHashMap,java.util.*,java.io.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="XMLParser.XMLParser"%>
<%@ page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<%  String sCabname=wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort();
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	WriteLog("Inside CustomUpdate_svt.jsp");	
	String sOutputXML="";
	String sFinalString="";
	XMLParser xmlParser = new XMLParser();
	String key=request.getParameter("key");
	String columnsNames=request.getParameter("Name");
	String columnsValues=request.getParameter("Value");
	String swhere="";
	String table ="";
	String[] key_arr = key.split("_");
	if("IntLiab".equals(key_arr[0]))
	{
		table = Internal_liability(key , "table");
		swhere = Internal_liability(key , "where");
	
	}
	if("ECP".equals(key_arr[0]))
	{
		table = Eligibility_cardproduct(key , "table");
		swhere = Eligibility_cardproduct(key , "where");
	}
	if("ECL".equals(key_arr[0]))
	{
		table = Eligibility_cardlimit(key , "table");
		swhere = Eligibility_cardlimit(key , "where");
	}
	
	

	StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<APUpdate_Input>");
	sInputXML.append("<option>APUpdate</option>");
	sInputXML.append("<TableName>" + table + "</TableName>");
	sInputXML.append("<ColName>" + columnsNames + "</ColName>");
	sInputXML.append("<Values>" + columnsValues + "</Values>");
	sInputXML.append("<WhereClause>" + swhere + "</WhereClause>");
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionID>"+sSessionId+"</SessionID>");
	sInputXML.append("</APUpdate_Input>");
	WriteLog("inputXML---"+sInputXML.toString());	
	
			try 
			{
				sOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, sInputXML.toString());
				WriteLog("CustomUpdate >> OutPutXML---"+sOutputXML);
			} 
			catch (NGException e) 
			{
				WriteLog("Exception while getting data from database : ");
			  // e.printStackTrace();
			} 
			catch (Exception ex) 
			{
				WriteLog("Exception while getting data from database : ");
			 // ex.printStackTrace();
			}
	%>
<%
	try {
		xmlParser.setInputXML(sOutputXML);
		String mainCode = xmlParser.getValueOf("MainCode");
            if(mainCode.equals("0")) {
				sFinalString="SUCCESS";
            } else {
               sFinalString="ERROR";
            }
        } catch (Exception exp) {
		    out.println(exp);
            sFinalString+=exp;
        }
		out.clear();
	out.println(sFinalString);	
%>
<%!
public static String Internal_liability(String Key , String Output)
	{
		String[] key_arr = Key.split("_");
		String Response = "";
		if("table".equals(Output))
		{
			if(key_arr[1].equals("Loan"))
			{
				Response = "ng_RLOS_CUSTEXPOSE_LoanDetails";
			}
			else if(key_arr[1].equals("Card"))
			{
				Response = "ng_RLOS_CUSTEXPOSE_CardDetails";
			}
			else if(key_arr[1].equals("Acct"))
			{
				Response = "ng_RLOS_CUSTEXPOSE_AcctDetails";
			}
		}
		else if ("where".equals(Output))
		{
			if(!"COB".equals(key_arr[3]))
			{
			if(key_arr[2].equals("P"))
			{
				Response= "Wi_Name = '"+key_arr[4]+"'";
			}
			else if (key_arr[2].equals("C"))
			{
				Response= "Child_Wi = '"+key_arr[4]+"'";	
			}
			//Response += " and ";
			if(key_arr[1].equals("Loan"))
			{
				Response += " and AgreementId = '"+key_arr[3]+"'";
			}
			else if(key_arr[1].equals("Card"))
			{
				Response += " and CardEmbossNum = '"+key_arr[3]+"'";
			}
			else if(key_arr[1].equals("Acct"))
			{
				Response += " and AcctId = '"+key_arr[3]+"'";
			}
			}
			else
			{
				if(key_arr[2].equals("P"))
			{
				Response= "Consider_For_Obligations=Null and cifid in (select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs =(select Cif_Id from ng_RLOS_Customer where wi_name = '"+key_arr[4]+"') and Relation='Joint Holder' and wi_name='"+key_arr[4]+"')";
			}
			else if (key_arr[2].equals("C"))
			{
				Response= "Consider_For_Obligations is Null and cifid in (select Main_Cif from ng_rlos_custexpose_LinkedICF with (nolock) where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = '"+key_arr[4]+"')) and Relation='Joint Holder' and child_wi='"+key_arr[4]+"";	
			}
			}
			
		}
		return Response;
		}
	public static String Eligibility_cardproduct(String Key , String Output)
	{
		String[] key_arr = Key.split("_");
		String Response = "";
		if("table".equals(Output))
		{
		Response= "ng_rlos_IGR_Eligibility_CardProduct";
		}
		else if ("where".equals(Output))
		{
		if(key_arr[1].equals("P"))
			{
				Response= "Wi_Name = '"+key_arr[3]+"'  and s_no ='"+key_arr[2]+"'";
			}
			else if (key_arr[1].equals("C"))
			{
				Response= "Child_wi = '"+key_arr[3]+"'  and s_no ='"+key_arr[2]+"'";	
			}
		}
		return Response;
	}
	public static String Eligibility_cardlimit(String Key , String Output)
	{
	//swhere = "Wi_Name = '"+winame+"'  and Card_Product ='"+document.getElementById(Card_Product).value+"' and Eligible_Limit='"+document.getElementById(EligibleAmount).value+"'";
		String[] key_arr = Key.split("_");
		String Response = "";
		if("table".equals(Output))
		{
		Response= "ng_rlos_IGR_Eligibility_CardLimit";
		}
		else if ("where".equals(Output))
		{
		if(key_arr[1].equals("P"))
			{
				Response= "Wi_Name = '"+key_arr[4]+"'   and Card_Product ='"+key_arr[2]+"' and Eligible_Limit='"+key_arr[3]+"'";
			}
			else if (key_arr[1].equals("C"))
			{
				Response= "Child_wi = '"+key_arr[4]+"'    and Card_Product ='"+key_arr[2]+"' and Eligible_Limit='"+key_arr[3]+"'";	
			}
		}
		return Response;
	}
%>

