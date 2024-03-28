
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
<%@ page import="flexjson.JSONSerializer.*" %>
<%@ page import="flexjson.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="com.newgen.omni.jts.srvr.*"%>
<%@page import="com.newgen.custom.XMLParser"%>
<%@ page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>

<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<% 
response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0); 
	response.setCharacterEncoding ("UTF-8");
	String sCabname=wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort();
	//int iJtsPort = Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort());
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();


	
	//String sUserId=wfsession.getUserName();
	
	String sOutputXML="";
	String sFinalString="";
	XMLParser xmlParser = new XMLParser();
	 //svt points start
	 String sQuery="";

	String Query =  ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("Query"), 1000, true) ;
	WriteLog("Query---"+Query);
	//String Query = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		
	String params = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wparams"), 1000, true);
		WriteLog("params---"+params);
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("pname"), 1000, true) );
	String pname = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3!=null?input3:"");
	WriteLog("pname---"+pname);
	//svt points end
	try{
	if(pname.equalsIgnoreCase("IL"))
	{
		sQuery=internalLiability(Query);
	}
	if(pname.equalsIgnoreCase("EL"))
	{
		sQuery=externalLiability(Query);
	}
	if(pname.equalsIgnoreCase("ELP"))
	{
		sQuery=externalLiabilityLoans(Query);
	}
	if(pname.equalsIgnoreCase("PL"))
	{
		sQuery=pipeline(Query);
	}
	if(pname.equalsIgnoreCase("FAN"))
	{
		sQuery=fundingAccNo(Query);
	}
	if(pname.equalsIgnoreCase("ECL"))
	{
		sQuery=Eligibility_Card_Limit(Query);
	}
	if(pname.equalsIgnoreCase("ECP"))
	{
		sQuery=Eligibility_Card_Product(Query);
	}
	if(pname.equalsIgnoreCase("PE"))
	{
		sQuery=Product_Eligibility(Query);
	}
	if(pname.equalsIgnoreCase("external"))
	{
		sQuery=external(Query);
	}
	if(pname.equalsIgnoreCase("CCTemplateData"))
	{
		sQuery=DocTemplateData_CC(Query);
	}
	if(pname.equalsIgnoreCase("DocTemplateRLOS"))
	{
		sQuery=DocTemplateData_RLOS(Query);
	}
	if(pname.equalsIgnoreCase("DocTemplatePL"))
	{
		sQuery=DocTemplateData_PL(Query);
	}
	if(pname.equalsIgnoreCase("RiskRatingtemp"))
	{
		sQuery=RiskRating_PL(Query);
	}
	if(pname.equalsIgnoreCase("PLjs"))
	{
		sQuery=PL_Js(Query);
	}
	
	}
	catch(Exception ex){
		 WriteLog("Exception in BulkDisplayWI---"+ex.getMessage());
	}
	
	/* StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<WFSelectWithColumnNames_Input>");
	sInputXML.append("<option>APSelectWithColumnNames</option>");
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionID>"+sSessionId+"</SessionID>");
	sInputXML.append("<Query>"+sQry+"</Query>");
	sInputXML.append("</WFSelectWithColumnNames_Input>"); */
	//Added By prabhakar
	StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<APSelectWithNamedParam_Input>");
	sInputXML.append("<option>APSelectWithNamedParam</option>");
	sInputXML.append("<Query>"+sQuery+"</Query>");
	sInputXML.append("<Params>"+params+"</Params>");
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionID>"+sSessionId+"</SessionID>");
	sInputXML.append("</APSelectWithNamedParam_Input>");
	WriteLog("inputXML---"+sInputXML.toString());				
		try	{
		
//		sOutputXML=WFCallBroker.execute(sInputXML.toString(),sJtsIp,iJtsPort, appServerType);
		sOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, sInputXML.toString());
		WriteLog("sOutputXML---"+sOutputXML);
	} catch(Exception exp) {
		WriteLog("Exception while getting data from database : "+exp);

	}%>
	<%!
	public static String internalLiability(String Query){
		String sQry="";
	if(Query.equalsIgnoreCase("CA"))
	{
		sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,CifId,Liability_type,LoanType,SchemeCardProd,AgreementId,ProductAmt,NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'',PreviousLoanTAI,PreviousLoanDBR,Settlement_Flag from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name =:Wi_Name and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat not in ('Pipeline','C') order by Liability_type desc";
		//wparams="Wi_Name=="+winame;
	}
	else if(Query.equalsIgnoreCase("Cr"))
	{
			sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,'', AgreementId,ProductAmt,NextInstallmentAmt,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'','','',Settlement_Flag from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi =:Child_Wi and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')";
	}
	else if(Query.equalsIgnoreCase("PL"))
		{
			sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,CifId,isNULL(Liability_type,'Individual_CIF'),SchemeCardProd, AgreementId,(case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'-') else case when loanType in ('PERFORMANCE GUARANTEES FIXED','BID / TENDER BONDS FIXED','L/C IMPORT SECURED','ACCEPTANCE L/C IMPORT','ADVANCE PAYMENT GUARANTEES FIXED','BID / TENDER BONDS AUTO RENEWABLE','RETENTION GUARANTEES AUTO RENEWABLE','SHORT TERM LOAN AGAINST INVOICE','LOCAL CHEQUE DISCOUNTING','LABOR GUARANTEE') then isnull(CumulativeDebitAmt,'')  else isnull(ProductAmt,'-') END end),NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'',PreviousLoanDBR,PreviousLoanTAI,Settlement_Flag,LoanStat from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = :Child_Wi and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat not in ('Pipeline','C')  and cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi ) and Consider_For_Obligations = 'true' union all select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = :Child_Wi  and applicantcategory='Business' union all  select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi )) and Relation='Joint Holder' and child_wi = :Child_Wi union all select CifId from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = :Child_Wi and isdirect='N') order by Liability_type desc";
			//wparams="Child_Wi=="+winame;
		}
	else if(Query.equalsIgnoreCase("PLE"))
		{
		//changes donre for PCAS-2702
			sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,CifId,isNULL(Liability_type,'Individual_CIF') as Liability_type,SchemeCardProd, AgreementId,(case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'-') else case when loanType in ('PERFORMANCE GUARANTEES FIXED','BID / TENDER BONDS FIXED','L/C IMPORT SECURED','ACCEPTANCE L/C IMPORT','ADVANCE PAYMENT GUARANTEES FIXED','BID / TENDER BONDS AUTO RENEWABLE','RETENTION GUARANTEES AUTO RENEWABLE','SHORT TERM LOAN AGAINST INVOICE','LOCAL CHEQUE DISCOUNTING','LABOR GUARANTEE') then isnull(CumulativeDebitAmt,'')  else isnull(ProductAmt,'-') END end),NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'-' as TypeofOD,PreviousLoanDBR,PreviousLoanTAI,Settlement_Flag,LoanStat from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = :Child_Wi and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat != 'Pipeline' and cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi ) and Consider_For_Obligations = 'true' union all select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = :Child_Wi  and applicantcategory='Business' union all  select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi )) and Relation='Joint Holder' and child_wi = :Child_Wi union all select CifId from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = :Child_Wi and isdirect='N') order by Liability_type desc";
			
			//wparams="Child_Wi=="+winame;
			
		}
	else if(Query.equalsIgnoreCase("fetchCALE"))
		{
			sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,A.CifId,Liability_type as LiabilityType,CardType as ProductType,SchemeCardProd as TypeofCardLoan, CardEmbossNum as AgreementId, CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI , General_Status,Limit_Increase as LimitIncrease,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.OutstandingBalance,0) ELSE ISnull(outstandingAmt,0) End) as OutstandingAmount, Consider_For_Obligations as ConsiderForObligations,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.interestRate,0) ELSE ISnull(a.InterestRate,0) End)  as InterestRate,MonthsOnBook,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then ISNULL((CAST(b.INSTALMENTpERIOD AS INT)-CAST(b.rEMAININGemi AS INT)),'') else ISNULL((CAST(b.INSTALMENTpERIOD AS INT)-CAST(b.rEMAININGemi AS INT)),'') end )  as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR,Settlement_Flag from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Wi_Name =:Wi_Name and CustRoleType !='Secondary' and CardStatus !='C' order by Liability_type desc";
		}
	else if(Query.equalsIgnoreCase("fetchCr"))
		{
			sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi =:Child_Wi and CustRoleType !='Secondary' and A.cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi ) and Consider_For_Obligations = 'true' union all select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = :Child_Wi  and applicantcategory='Business' union all  select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi )) and Relation='Joint Holder' and child_wi = :Child_Wi union all select CifId from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = :Child_Wi and isdirect='N')";
		}
	else if(Query.equalsIgnoreCase("fetchPL"))
	{
		sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi =:Child_Wi and CustRoleType !='Secondary' and CardStatus !='C' and A.cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi ) and Consider_For_Obligations = 'true' union all select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = :Child_Wi  and applicantcategory='Business' union all  select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi )) and Relation='Joint Holder' and child_wi = :Child_Wi union all select CifId from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = :Child_Wi and isdirect='N') order by Liability_type desc"; 
	}
	else if(Query.equalsIgnoreCase("fetchPLE"))
	{
		//sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi =:Child_Wi and CustRoleType !='Secondary' order by Liability_type desc";
		sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  case when SchemeCardProd like '%LOC%' then (select sum(cast(MonthlyAmount as float)) from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi =:Child_Wi and replace(CardNumber,'I','')=CardEmbossNum) else PaymentsAmount end as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,case when SchemeCardProd like '%LOC%' then (select max(InterestRate) from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi =:Child_Wi and replace(CardNumber,'I','')=CardEmbossNum) else '' end as InterestRate,MonthsOnBook,case when SchemeCardProd like 'LOC%' then (select top 1 ISNULL((CAST(max(INSTALMENTpERIOD) AS INT)-CAST(min(rEMAININGemi) AS INT)),'') from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi =:Child_Wi and replace(CardNumber,'I','')=CardEmbossNum ) else '' end as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) where (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary') and a.Child_Wi =:Child_Wi and CustRoleType !='Secondary' and A.cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi ) and Consider_For_Obligations = 'true' union all select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = :Child_Wi  and applicantcategory='Business' union all  select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name  = :Child_Wi )) and Relation='Joint Holder' and child_wi = :Child_Wi union all select CifId from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = :Child_Wi and isdirect='N') order by Liability_type desc";
	}
	else if(Query.equalsIgnoreCase("ODCALE"))
	{
		 sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,cifid,Account_Type,'OverDraft' as ProductType,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name  =:Wi_Name  and ODType != ''";
	}
	else if(Query.equalsIgnoreCase("ODCr"))
	{
		sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,cifid,Account_Type,'OverDraft' as ProductType,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name  =:Wi_Name  and ODType != ''";
	}
	else if(Query.equalsIgnoreCase("ODPL"))
	{
		sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,'OverDraft' as ProductType,cifid,Account_Type,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi  =:Wi_Name  and ODType != ''";
		//wparams="Wi_Name=="+winame;
	}
	else if(Query.equalsIgnoreCase("ODPLE"))
	{
		sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,'OverDraft' as ProductType,cifid,Account_Type,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi  =:Wi_Name  and ODType != '' and custroletype='Main'";
	}
	else
	{
					//QueueData	
					 sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name =:Grid_name and ActivityName=:ActivityName ORDER BY col_seq";
					 
	}
	
	return sQry;
	}%>
	<%!
	public static String externalLiability(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("CA"))
	{
		//below query modified by nikhil for wrong Data in diff. Columns
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType,Liability_type, agreementid,totalamt,case when Pmtfreq like '%30 days%' or Pmtfreq is null then PaymentsAmt when Pmtfreq like '%60 days%' then (cast(isnull(PaymentsAmt,'0') as float)/2) when Pmtfreq like '%90 days%' then (cast(isnull(PaymentsAmt,'0') as float)/3) when Pmtfreq like '%180 days%' then (cast(isnull(PaymentsAmt,'0') as float)/6) else (cast(isnull(PaymentsAmt,'0') as float)/12) end as PaymentsAmt,Take_Over_Indicator,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,'',concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','','',MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name =:Wi_Name and LoanStat not in  ('Pipeline','Closed') and ProviderNo != 'B01' order by Liability_type desc";
		
			//wparams="Wi_Name=="+winame;
	}
	else if(Query.equalsIgnoreCase("Cr"))
	{
	//below query changed for pcsp-26
			sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,case when Pmtfreq like '%30 days%' or Pmtfreq is null then PaymentsAmt when Pmtfreq like '%60 days%' then (cast(isnull(PaymentsAmt,'0') as float)/2) when Pmtfreq like '%90 days%' then (cast(isnull(PaymentsAmt,'0') as float)/3) when Pmtfreq like '%180 days%' then (cast(isnull(PaymentsAmt,'0') as float)/6) else (cast(isnull(PaymentsAmt,'0') as float)/12) end as PaymentsAmt,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi =:Child_Wi and LoanStat != 'Pipeline'  and ProviderNo != 'B01' order by Liability_type desc ";
	}
	else if(Query.equalsIgnoreCase("PL"))
		{
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,case when Pmtfreq like '%30 days%' or Pmtfreq is null then PaymentsAmt when Pmtfreq like '%60 days%' then (cast(isnull(PaymentsAmt,'0') as float)/2) when Pmtfreq like '%90 days%' then (cast(isnull(PaymentsAmt,'0') as float)/3) when Pmtfreq like '%180 days%' then (cast(isnull(PaymentsAmt,'0') as float)/6) else (cast(isnull(PaymentsAmt,'0') as float)/12) end as PaymentsAmt,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = :Child_Wi and LoanStat != 'Pipeline'  and ProviderNo != 'B01' order by Liability_type desc ";
			//wparams="Child_Wi=="+winame;
		}
	else if(Query.equalsIgnoreCase("PL_CSM"))
	{
	sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,case when Pmtfreq like '%30 days%' or Pmtfreq is null then PaymentsAmt when Pmtfreq like '%60 days%' then (cast(isnull(PaymentsAmt,'0') as float)/2) when Pmtfreq like '%90 days%' then (cast(isnull(PaymentsAmt,'0') as float)/3) when Pmtfreq like '%180 days%' then (cast(isnull(PaymentsAmt,'0') as float)/6) else (cast(isnull(PaymentsAmt,'0') as float)/12) end as PaymentsAmt,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = :Child_Wi and LoanStat not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc ";
	
		//wparams="Child_Wi=="+winame;
	}
	else if(Query.equalsIgnoreCase("fetchCA"))
		{
			sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,Liability_type,CardEmbossNum,totalamount,PaymentsAmount as EMI,Take_Over_Indicator,'',CAC_Indicator,QC_Amt,QC_EMI,avg_utilization,CurrentBalance,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',settlement_flag,'',MonthsOnBook from ng_rlos_cust_extexpo_cardDetails with (nolock) where wi_name=:wi_name and CardStatus not in  ('Pipeline','Closed') and ProviderNo != 'B01' order by Liability_type desc";
			//wparams="Child_Wi=="+winame;
			
		}
	
	else if(Query.equalsIgnoreCase("fetchCr"))
		{
			sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,PaymentsAmount as EMI,TakeOverAmount,'',CAC_Indicator,QC_Amt,QC_EMI,avg_utilization,CurrentBalance,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,cardstatus,Settlement_Flag from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus != 'Pipeline' and ProviderNo != 'B01'  order by Liability_type desc";
		}
	else if(Query.equalsIgnoreCase("fetchPL"))
	{
		
			sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,'',CurrentBalance,ISNULL(Consider_For_Obligations,'') as Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,Settlement_Flag,cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus != 'Pipeline' and ProviderNo != 'B01'  order by Liability_type desc";
	
	}
	else if(Query.equalsIgnoreCase("fetchPLNew"))
	{//pcasi-1039	
			sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,ISNULL(Consider_For_Obligations,'') as Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,Settlement_Flag,cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus != 'Pipeline' and ProviderNo != 'B01'  order by Liability_type desc";
	}
	else if(Query.equalsIgnoreCase("fetchPLE"))
	{
		sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,ISNULL(Consider_For_Obligations,'') as Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',Settlement_Flag,MonthsOnBook,cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc";
	}

	else if(Query.equalsIgnoreCase("ODCA"))
	{
		//query changed by nikhil for PCSP-945
		//Provider no check added by deepak on 07/07/2019 for PCAS-1450 //deepak AcctStat flag added to greay out closed OD
		sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,AcctType as TypeOfContract,case when CifId='Corporate' or CifId in (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'NA') else 'NA' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame =:Wi_Name and applicantCategory='Business' group by CompanyCIF) then 'Corporate_CIF' else 'Individual_CIF' end as LiabilityType,AcctId,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag,AcctStat from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Wi_Name =:Wi_Name and AcctStat not in  ('Pipeline','Closed') and ProviderNo !='B01'";
	}
	else if(Query.equalsIgnoreCase("ODCrCC"))
	{
		//query changed by nikhil for PCSP-945
		//Provider no check added by deepak on 07/07/2019 for PCAS-1450
		sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,AcctType as TypeOfContract,AcctId,case when CifId='Corporate' or CifId in (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'NA') else 'NA' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame =:Child_Wi and applicantCategory='Business' group by CompanyCIF) then 'Corporate_CIF' else 'Individual_CIF' end as LiabilityType,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag,AcctStat from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Child_Wi =:Child_Wi and AcctStat != 'Pipeline' and ProviderNo !='B01' ";//sagarika for PCAS-2116
	}
	else if(Query.equalsIgnoreCase("ODPL"))
	{
	//Provider no check added by deepak on 07/07/2019 for PCAS-1450
	sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,'Overdraft' as TypeOfContract,case when CifId='Corporate' or CifId in (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'NA') else 'NA' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame =:Child_Wi and applicantCategory='Business' group by CompanyCIF) then 'Corporate_CIF' else 'Individual_CIF' end as LiabilityType,AcctId,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Child_Wi =:Child_Wi and AcctStat != 'Pipeline' and ProviderNo !='B01'";
	}
	else if (Query.equalsIgnoreCase("SCCA"))
	{
		sQry="select 'ng_rlos_cust_extexpo_ServicesDetails' as table_name,ServiceType as TypeOfContract,'Individual_CIF',ServiceID as LiabilityType,'','' as EMI,'','','','','','','',Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',settlement_flag,'',MonthsOnBook from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where wi_name=:wi_name and ServiceStat not in  ('Pipeline','Closed') and ProviderNo != 'B01'";
	}
	else if (Query.equalsIgnoreCase("SCCrCC") || Query.equalsIgnoreCase("SCPL"))
	{
		sQry="select 'ng_rlos_cust_extexpo_ServicesDetails' as table_name,ServiceType ,ServiceID,'Individual_CIF' as LiabilityType,'','','' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'','' as OutStandingBalance,Consider_For_Obligations as considerforObligation,remarks as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag,ServiceStat from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where Child_Wi =:Child_Wi and ServiceStat != 'Pipeline' and ProviderNo !='B01'";
	}
	else
	{
		//QueueData
		sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'External_Liability' and ActivityName=:ActivityName ORDER BY col_seq";
	}
		return sQry;
	}%>
	<%!
	public static String externalLiabilityLoans(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("CA"))
	{
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType,Liability_type, agreementid,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,'',concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','','',MonthsOnBook,case when LoanMaturityDate  <= (select introduction_Date from ng_rlos_exttable  with (nolock) where WiName =:Wi_Name) then 'Closed' else 'Active' end as statusStat   from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name =:Wi_Name and LoanStat not in  ('Pipeline','Closed') and ProviderNo != 'B01' order by Liability_type desc";		
	}
	else if(Query.equalsIgnoreCase("PL"))
		{
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,Paymentsamt,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,Settlement_Flag,case when LoanMaturityDate  <= (select introduction_Date from ng_pl_exttable  with (nolock) where pl_wi_name = :Child_Wi) then 'Closed' else 'Active' end as statusStat,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = :Child_Wi and LoanStat != 'Pipeline'  and ProviderNo != 'B01' order by Liability_type desc ";
		}
	else if(Query.equalsIgnoreCase("PL_CSM"))
	{
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,Paymentsamt,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,case when LoanMaturityDate  <= (select introduction_Date from ng_pl_exttable  with (nolock) where pl_wi_name = :Child_Wi) then 'Closed' else 'Active' end as statusStat ,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = :Child_Wi and LoanStat not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc ";
	}
	else if(Query.equalsIgnoreCase("fetchCA"))
		{
			sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,Liability_type,CardEmbossNum,totalamount,PaymentsAmount as EMI,Take_Over_Indicator,'',CAC_Indicator,QC_Amt,QC_EMI,avg_utilization,CurrentBalance,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',settlement_flag,'',MonthsOnBook,case when ClosedDate  <= (select introduction_Date from ng_rlos_exttable  with (nolock) where Winame =:wi_name) then 'Closed' else 'Active' end as statusStat  from ng_rlos_cust_extexpo_cardDetails with (nolock) where wi_name=:wi_name and CardStatus not in  ('Pipeline','Closed') and ProviderNo != 'B01' order by Liability_type desc";			
		}
	
	else if(Query.equalsIgnoreCase("fetchPL"))
	{	
			sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,'',CurrentBalance,ISNULL(Consider_For_Obligations,'') as Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,Settlement_Flag,case when ClosedDate  <= (select introduction_Date from ng_pl_exttable  with (nolock)  where pl_wi_name = :Child_wi) then 'Closed' else 'Active' end as statusStat ,cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus != 'Pipeline' and ProviderNo != 'B01'  order by Liability_type desc";
	}
	else if(Query.equalsIgnoreCase("fetchPLE"))
	{
		sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,avg_utilization,CurrentBalance,ISNULL(Consider_For_Obligations,'') as Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,case when ClosedDate  <= (select introduction_Date from ng_pl_exttable  with (nolock) where pl_wi_name =:Child_wi) then 'Closed' else 'Active' end as statusStat ,cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc";
	}

	else if(Query.equalsIgnoreCase("ODCA"))
	{
		sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,AcctType as TypeOfContract,'Individual_CIF' as LiabilityType,AcctId,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,Settlement_Flag,'',MonthsOnBook,case when ClosedDate  <= (select introduction_Date from ng_rlos_exttable   with (nolock) where Winame :Wi_Name) then 'Closed' else 'Active' end as statusStat ,AcctStat from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Wi_Name =:Wi_Name and AcctStat not in  ('Pipeline','Closed') and ProviderNo !='B01'";
	}
	else if(Query.equalsIgnoreCase("ODPL"))
	{
	//Provider no check added by deepak on 07/07/2019 for PCAS-1450
	sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,'Overdraft' as TypeOfContract,AcctId,'Individual_CIF' as LiabilityType,CreditLimit,'' as TakeOverIndicator,PaymentsAmount,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag,case when ClosedDate  <= (select introduction_Date from ng_pl_exttable  with (nolock) where pl_wi_name =:Child_Wi) then 'Closed' else 'Active' end as statusStat ,AcctStat from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Child_Wi =:Child_Wi and AcctStat != 'Pipeline' and ProviderNo !='B01'";
	}
	else if (Query.equalsIgnoreCase("SCCA"))
	{
		sQry="select 'ng_rlos_cust_extexpo_ServicesDetails' as table_name,ServiceType as TypeOfContract,'Individual_CIF',ServiceID as LiabilityType,'','' as EMI,'','','','','','','',Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',settlement_flag,'',MonthsOnBook,case when SvcExpDt  <= (select introduction_Date from ng_rlos_exttable  with (nolock) where Winame =:Wi_Name) then 'Closed' else 'Active' end as statusStat  from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where wi_name=:Wi_Name and ServiceStat not in  ('Pipeline','Closed') and ProviderNo != 'B01'";
	}
	else if ( Query.equalsIgnoreCase("SCPL"))
	{
		sQry="select 'ng_rlos_cust_extexpo_ServicesDetails' as table_name,ServiceType ,ServiceID,'Individual_CIF' as LiabilityType,'','' as TakeOverIndicator,'','' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'','' as OutStandingBalance,Consider_For_Obligations as considerforObligation,remarks as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag,case when SvcExpDt  <= (select introduction_Date from ng_pl_exttable  with (nolock) where pl_wi_name =:Child_Wi) then 'Closed' else 'Active' end as statusStat ,ServiceStat from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where Child_Wi =:Child_Wi and ServiceStat != 'Pipeline' and ProviderNo !='B01'";
	}
	else
	{
		//QueueData
		sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'External_Liability' and ActivityName=:ActivityName ORDER BY col_seq";
	}
		return sQry;
	}%>
	<%!
	public static String pipeline(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("CCCRPL"))
		{
			//query modified by nikhil for PCSP-856
			 //sQry="select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,AgreementId as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_Wi = :child_Wi and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' and ProviderNo !='B01' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardEmbossNum as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_Wi = :child_Wi and (CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline') and ProviderNo !='B01' union Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalloanAmount,RemainingInstalments,NoOfDaysInPipeline,'','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where (child_Wi = :child_Wi or Wi_Name = :child_Wi) and  ProviderNo !='B01' and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' and CifId=:cifId) or (LoanStat = 'AL-Pipeline' and CifId=:cifId)) and ProviderNo !='B01' order by DLP desc";
			 //sQry="select table_name,reference,Product_Type,CustRoleType,DLP,Tenor,Consider_For_Obligations,Liab_desc,LimAmt,EMI,NoOfDaysInPipeline,TKO,TK from (select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,concat(AgreementId,'-', ProviderNo) as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,LoanDesc as Liab_desc,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_Wi = :child_Wi and (LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline') and ProviderNo !='B01' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,concat(CardEmbossNum,'-', ProviderNo) as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,CardTypeDesc as Liab_desc,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_Wi = :child_Wi and (CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline') and ProviderNo !='B01' union Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,SchemeCardProd as Liab_desc,TotalloanAmount as LimAmt,RemainingInstalments as EMI,NoOfDaysInPipeline,'' as TKO,'' as TK from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where (child_Wi = :child_Wi or Wi_Name = :child_Wi)  and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' and CifId=:cifId) or (LoanStat = 'AL-Pipeline' and CifId=:cifId)) and (ProviderNo !='B01' or ProviderNo is null))as ext where format(convert(datetime,DLP),'yyyy-MM-dd') >(DATEADD(MONTH,-6, getdate())) order by DLP desc";
			 //Deepak query correction
			sQry="select table_name,reference,Product_Type,CustRoleType,DLP,Tenor,Consider_For_Obligations,Liab_desc,LimAmt,EMI,NoOfDaysInPipeline,TKO,TK from (select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,concat(AgreementId,'-', ProviderNo) as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,LoanDesc as Liab_desc,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_Wi = :child_Wi and (LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline') and ProviderNo !='B01' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,concat(CardEmbossNum,'-', ProviderNo) as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,CardTypeDesc as Liab_desc,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_Wi = :child_Wi and (CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline') and ProviderNo !='B01')as ext where format(convert(datetime,DLP),'yyyy-MM-dd') >(DATEADD(MONTH,-6, getdate()))  union all Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,SchemeCardProd as Liab_desc,TotalloanAmount as LimAmt,NextInstallmentAmt as EMI,NoOfDaysInPipeline,'' as TKO,'' as TK from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where (child_Wi = :child_Wi or Wi_Name = :child_Wi)  and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline'  ) or (LoanStat = 'AL-Pipeline' and CifId=:cifId )) and (ProviderNo !='B01' or ProviderNo is null) order by DLP desc";
	
		}
		else if(Query.equalsIgnoreCase("!CCCRPL"))
		{
			//query modified by nikhil for PCSP-856
			 sQry="select table_name,reference,Product_Type,CustRoleType,DLP,Tenor,Consider_For_Obligations,Liab_desc,LimAmt,EMI,NoOfDaysInPipeline,TKO,TK from (select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,concat(AgreementId,'-', ProviderNo) as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,LoanDesc as Liab_desc,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name = :Wi_Name and (LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline') and ProviderNo !='B01' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,concat(CardEmbossNum,'-', ProviderNo) as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,CardTypeDesc as Liab_desc,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where Wi_Name = :Wi_Name and (CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline') and ProviderNo !='B01' union Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,SchemeCardProd as Liab_desc,TotalloanAmount as LimAmt,NextInstallmentAmt as EMI,NoOfDaysInPipeline,'' as TKO,'' as TK from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where  Wi_Name = :Wi_Name  and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' ) or (LoanStat = 'AL-Pipeline' and CifId=:CifId )) and (ProviderNo !='B01' or ProviderNo is null))as ext where format(convert(datetime,DLP),'yyyy-MM-dd') >(DATEADD(MONTH,-6, getdate())) order by DLP desc";
		}
		else if(Query.equalsIgnoreCase("fetchCCCRPL"))
		{
			 sQry="Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,'Primary','','',isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmount,'','','','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = :Child_Wi and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' and ProviderNo !='B01'";
		}
		else if(Query.equalsIgnoreCase("!fetchCCCRPL"))
		{
			 sQry="Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,'Primary','','',isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmount,'','','','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name =:Wi_Name and ProviderNo !='B01' and  LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' and ProviderNo !='B01'";
		}
		else
		{
				//QUEUEDATA
			 sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Pipeline' and ActivityName=:ActivityName ORDER BY col_seq";
		}
		return sQry;
	}%>
	<%!
	public static String fundingAccNo(String Query)
	{	
		String sQry="";
		if(Query.equalsIgnoreCase("CA"))
		{
			sQry="SELECT fundingAccount,AcctId,AcctType,AcctNm,AccountOpenDate,AcctSegment,AcctSubSegment,AcctStat,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Wi_Name =:Wi_Name and Request_Type = :Request_Type and (AcctType = 'CURRENT ACCOUNT' or AcctType = 'AMAL CURRENT ACCOUNT' or AcctType = 'SAVINGS ACCOUNT' or AcctType = 'AMAL SAVINGS ACCOUNT' or AcctType = 'AMAL JOOD SAVINGS ACCOUNT') and AcctStat='Active'";
		}
		else if(Query.equalsIgnoreCase("!CA"))
		{
			sQry="SELECT fundingAccount,AcctId,AcctType,AcctNm,AccountOpenDate,AcctSegment,AcctSubSegment,AcctStat,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Child_Wi =:Child_Wi and Request_Type =:Request_Type and (AcctType = 'CURRENT ACCOUNT' or AcctType = 'AMAL CURRENT ACCOUNT' or AcctType ='SAVINGS ACCOUNT' or AcctType = 'AMAL SAVINGS ACCOUNT' or AcctType = 'AMAL JOOD SAVINGS ACCOUNT') and AcctStat='Active'";
		}
		else
		{
			//fetchValidQueueData
			 sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name =:Grid_name and ActivityName=:ActivityName ORDER BY col_seq";
		}
		return sQry;
	}
	%>
	<%!
	public static String Eligibility_Card_Limit(String Query)
	{
		String table_name="ng_rlos_IGR_Eligibility_CardLimit";
		String sQry="";
		if(Query.equalsIgnoreCase("CA"))
		{
			sQry="select distinct Cardproductselect,Card_Product,Eligible_Limit,Final_Limit,CardShield,CC_Waiver,flag,combined_limit FROM  "+table_name+" with (nolock) WHERE wi_name =:wi_name order by Cardproductselect desc";
		}
		else if(Query.equalsIgnoreCase("!CA"))
		{
			sQry="select distinct Cardproductselect,Card_Product,Eligible_Limit,Final_Limit,CardShield,CC_Waiver,flag,combined_limit FROM  "+table_name+" with (nolock) WHERE Child_wi = :Child_wi order by Cardproductselect desc";
		}
		else if(Query.equalsIgnoreCase("CardProduct"))
		{
			sQry = "select distinct CreditShield FROM ng_master_cardproduct with (nolock) where CODE =:Card_Product";
		}
		else
		{
			 sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name =:Grid_name order by col_seq";
		}
		return sQry;
	}
	%>
	<%!
	public static String Eligibility_Card_Product(String Query)
	{
		String sQry="";
		String table_name="ng_rlos_IGR_Eligibility_CardProduct";
		if(Query.equalsIgnoreCase("CA"))
		{//change for pcsp-429
			sQry ="select (row_number() over(order by product)) as s_no,Product,Card_Product,max(isnull(CACCalculatedLimit,0)),Eligible_Card,max(isnull(final_limit,0)),Decision,Declined_Reason,Deviation_Code_Refer,'',Delegation_Authorithy,Score_Grade FROM  "+table_name+" with (nolock) WHERE wi_name = :wi_name group by Product,Card_Product,Card_Product,Decision,Declined_Reason,Deviation_Code_Refer,Eligible_Card,Delegation_Authorithy,Score_Grade";
		}
		else if(Query.equalsIgnoreCase("!CA"))
		{
			 sQry="select (row_number() over(order by product)) as s_no,Product,Card_Product,max(case when CACCalculatedLimit='null' then '0' else isnull (CACCalculatedLimit,'0') end),Eligible_Card,max(isnull(final_limit,0)),Decision,Declined_Reason,Deviation_Code_Refer,'',Delegation_Authorithy,Score_Grade FROM  "+table_name+" with (nolock) WHERE child_wi =:child_wi group by Product,Card_Product,Card_Product,Decision,Declined_Reason,Deviation_Code_Refer,Eligible_Card,Delegation_Authorithy,Score_Grade";
			 
		}
		else
		{
			//QueueData
			sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = :Grid_name and  ActivityName=:ActivityName order by col_seq";
		}
		return sQry;
	}
	%>
	<%!
	public static String Product_Eligibility(String Query)
	{
		String table_name="ng_rlos_IGR_Eligibility_PersonalLoan";
		String sQry="";
		if(Query.equalsIgnoreCase("CA"))
		{
			sQry="select (row_number() over(order by RequestedLoan)) as S_no,RequestedLoan,RequestedAppType,max(isnull(CACCalculatedLimit,0)),EligibleExposure,max(isnull(FinalLoanAmount,0)),Decision,DeclinedReasonCode,ReferReasoncode,DeviationCodeRefer,DelegationAuthorithy FROM  "+table_name+" with (nolock) WHERE wi_name =:wi_name group by RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,ReferReasoncode,DeviationCodeRefer,DelegationAuthorithy";
		}
		else if(Query.equalsIgnoreCase("!CA"))
		{
			sQry="select (row_number() over(order by RequestedLoan)) as S_no,RequestedLoan,RequestedAppType,max(isnull(CACCalculatedLimit,0)),EligibleExposure,max(isnull(FinalLoanAmount,0)),Decision,DeclinedReasonCode,ReferReasoncode,DeviationCodeRefer,DelegationAuthorithy FROM  "+table_name+" with (nolock) WHERE Child_wi =:Child_wi group by RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,ReferReasoncode,DeviationCodeRefer,DelegationAuthorithy"; 
		}
		else
		{
			 sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name =:Grid_name order by col_seq";
		}
		return sQry;
	}
	%>
	<%!
	public static String external(String Query)
	{	
		
		String sQry = "select distinct ContractType,Contract_Role_Type,TotalNo,DataProvidersNo,RequestNo,DeclinedNo,RejectedNo,NotTakenUpNo,ActiveNo,ClosedNo from NG_RLOS_CUSTEXPOSE_RecordDestribution with (nolock) where Child_Wi =:Child_Wi and CifId=case when '"+Query+"'='Corporate_CIF' then (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame =:Child_Wi and applicantCategory='Business' group by CompanyCIF) else (select case when ntb='true' then '' else CIF_ID end from ng_rlos_customer where wi_name =:Child_Wi ) end";
		return sQry;
	}
	
	public static String DocTemplateData_CC(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("PLE"))
		{
			//query change by saurabh for PCSP-23 on 12Feb19 
			//modified by nikhil 12th frb
			sQry="select workstepName,userName,csmNext from (select workstepName,userName,csmNext,Decision,row_number() over (partition by workstepname,username order by insertionOrderId desc) as RN from NG_RLOS_GR_DECISION where workstepName in ('CAD_Analyst1','Cad_Analyst2') and dec_wi_Name =:WiName and (Decision like 'Approve%' or Decision like 'Submit%' or Decision like 'Reject%' or Decision like 'Refer%')) as dummy_DECISION";
		}
		else if(Query.equalsIgnoreCase("OfficeCity") || Query.equalsIgnoreCase("ResidenceCity") || Query.equalsIgnoreCase("HomeCity"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_City with (nolock) WHERE code =:Code";
		}
		//changes for PSCP-23 
		else if(Query.equalsIgnoreCase("OfficeCountry") || Query.equalsIgnoreCase("ResidenceCountry") || Query.equalsIgnoreCase("HomeCountry") || Query.equalsIgnoreCase("Nationality_CC"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code =:Code";
		}
		else if(Query.equalsIgnoreCase("Designation_CC"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_Designation with (nolock) WHERE code =:Code";
		}
		//changes by shivang for PCAS-2744 starts
		else if(Query.equalsIgnoreCase("DectechDecision"))
		{
			sQry="select top 1 isnull(Decision,'') from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi =:WiName";
		}
		else if(Query.equalsIgnoreCase("DectechDelegation"))
		{
			sQry="select top 1 isnull(Delegation_Authorithy,'') from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi =:WiName";
		}
		else if(Query.equalsIgnoreCase("MOB"))
		{
			sQry="select isnull(max(MonthsOnBook),0) as MonthsOnBook from (select max(cast(MonthsOnBook as float)) as MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' union all select max(cast(MonthsOnBook as float)) as MonthsOnBook from ng_rlos_cust_extexpo_AccountDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' union all select max(cast(MonthsOnBook as float)) as MonthsOnBook from ng_rlos_cust_extexpo_CardDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true') t ";
		}
		else if(Query.equalsIgnoreCase("TotalObligation"))
		{
			sQry="select sum (EMI) as obligation from (SELECT DISTINCT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI, Consider_For_Obligations AS ConsiderForObligation,child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_LoanDetails WITH (nolock) where ProviderNo!='B01'and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL  SELECT  CardType AS ExposureScheme, Liability_type AS LiabilityType, CardEmbossNum AS ApplicationNo, (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and  Child_Wi=:WiName UNION ALL SELECT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI,  Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM  ng_rlos_cust_extexpo_LoanDetails WITH (nolock) WHERE ProviderNo!='B01' and LoanStat!='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT  CardType AS ExposureScheme, Liability_type AS LiabilityType, CardEmbossNum AS ApplicationNo, (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_CardDetails WITH (nolock) WHERE ProviderNo!='B01' and CardStatus!='Pipeline' and Consider_For_Obligations='true' and child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_AcctDetails WITH (nolock) WHERE ProviderNo != 'B01' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_AccountDetails WITH (nolock) WHERE ProviderNo != 'B01' and AcctStat !='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName) t"; 
		}
		 else if(Query.equalsIgnoreCase("AECBHistory"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  child_Wi  =:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) from ng_rlos_cust_extexpo_LoanDetails where child_Wi  = :WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true') as ext_expo";
		}
		else if(Query.equalsIgnoreCase("EligibleCardLimit"))
		{
			sQry="select top 1 Eligible_Card from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi=:WiName";
		}
		else if(Query.equalsIgnoreCase("NumOfCardRequested"))
		{
			sQry="select count(Card_Product) from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true'";
		}
		else if(Query.equalsIgnoreCase("IsConventionalSelected"))
		{
			sQry="select count(*) from ng_master_cardproduct where CODE = (select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true') and ReqProduct='Conventional' and isActive='Y'";
		}
		else if(Query.equalsIgnoreCase("FinalLimitForConventional"))
		{
			sQry="select cast(Eligible_Limit as float) as IslamicLimit from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Card_Product in (" +
			"select code from ng_master_cardproduct where code in ( select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true') and ReqProduct='Conventional' and isActive='Y')";
		}
		else if(Query.equalsIgnoreCase("IslamicAmtFetch"))
		{
			sQry="select sum(cast(Eligible_Limit as float)) as IslamicLimit from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Card_Product in (" +
			"select code from ng_master_cardproduct where code in ( select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true') and ReqProduct='Islamic' and isActive='Y')";
		}
		else if(Query.equalsIgnoreCase("OD_LoanEmi"))
		{
			sQry="select sum (EMI) as obligation from (SELECT DISTINCT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI, Consider_For_Obligations AS ConsiderForObligation,child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_LoanDetails WITH (nolock) where isnull(ProviderNo,'')!='B01'and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT  LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI,  Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM  ng_rlos_cust_extexpo_LoanDetails WITH (nolock) WHERE isnull(ProviderNo,'')!='B01' and LoanStat!='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_AcctDetails WITH (nolock) WHERE isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_AccountDetails WITH (nolock) WHERE isnull(ProviderNo,'') != 'B01' and AcctStat !='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL select distinct a.Request_Type as ExposureScheme,a.CardType as LiabilityType ,a.CardEmbossNum AS ApplicationNo,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI,Consider_For_Obligations AS ConsiderForObligation, a.child_wi AS wi_name from ng_RLOS_CUSTEXPOSE_CardDetails a left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on  CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name where a.Child_Wi=:WiName and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS')and (a.SchemeCardProd='LOC STANDARD' or a.SchemeCardProd='LOC PREFERRED') and Consider_For_Obligations='true' and CustRoleType !='Secondary' and CardStatus !='C') t";
			
		}
		else if(Query.equalsIgnoreCase("OD_LoanEmi_IM"))
		{
			sQry="select sum (EMI) as obligation from (SELECT DISTINCT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI, Consider_For_Obligations AS ConsiderForObligation,child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_LoanDetails WITH (nolock) where isnull(ProviderNo,'')!='B01'and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT  LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI,  Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM  ng_rlos_cust_extexpo_LoanDetails WITH (nolock) WHERE isnull(ProviderNo,'')!='B01' and LoanStat!='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_AcctDetails WITH (nolock) WHERE isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' and  child_wi=:WiName) t";
			
		}
		else if(Query.equalsIgnoreCase("cardLimitCalc"))
		{
			sQry="select sum (EMI) as obligation from  (SELECT (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A') and Child_Wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED') UNION ALL SELECT  (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT))* 3) / 100) END) AS EMI FROM ng_rlos_cust_extexpo_CardDetails WITH (nolock) WHERE ProviderNo!='B01' and CardStatus!='Pipeline' and Consider_For_Obligations='true' and  (CardStatus='Active' or CardStatus='A') and child_wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) t";
		}
		//changes by shivang for PCAS-2744 starts end
		//Adding for Pre-Approved Cam
		else if(Query.equalsIgnoreCase("WorstD24"))
		{
			sQry="select Description from ng_master_Aecb_Codes where code in (select Worst_Status_Last24Months from (select derived.Worst_Status_Last24Months from ng_rlos_cust_extexpo_CardDetails card join NG_RLOS_CUSTEXPOSE_Derived derived on card.child_Wi=derived.Child_Wi where derived.Request_Type='ExternalExposure' and card.child_Wi=:WiName union all select WorstStatus24Months from ng_rlos_cust_extexpo_LoanDetails loan join NG_RLOS_CUSTEXPOSE_Derived derived on derived.Child_Wi=loan.child_Wi where derived.Request_Type='ExternalExposure' and loan.child_Wi=:WiName union all select Worst_Status_Last24Months from ng_rlos_cust_extexpo_AccountDetails Account join NG_RLOS_CUSTEXPOSE_Derived derived on derived.Child_Wi=Account.child_Wi where derived.Request_Type='ExternalExposure' and Account.child_Wi=:WiName) as WostStatus where isnull(Worst_Status_Last24Months,'')!='')";
		}
		else if(Query.equalsIgnoreCase("MortgageValue"))
		{
			sQry="select TotalAmt from ng_rlos_cust_extexpo_LoanDetails where Consider_For_Obligations='true' and isnull(ProviderNo,'')!='B01' and LoanType like 'Mortgage%' and child_Wi=:WiName";
		}else if(Query.equalsIgnoreCase("RakEliteValue"))
		{
			sQry="select PropertyValue,* from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi=:WiName and Consider_For_Obligations='true' and LoanStat!='C' and isnull(ProviderNo,'')!='B01' and LoanType like 'Home%'";
		}
		else if(Query.equalsIgnoreCase("DectechEligibility"))
		{
			sQry="select top 1 Eligible_Card from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi=:WiName";
		}
		else if(Query.equalsIgnoreCase("ExternalLiabilityLimit"))
		{
			sQry="select isnull(sum(EMI),0) as ExtCardLim from (select (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT))* 3) / 100) END) AS EMI from ng_rlos_cust_extexpo_CardDetails where child_Wi=:WiName and ProviderNo!='B01' and CardStatus!='Pipeline' and Consider_For_Obligations='true' and  (CardStatus='Active' or CardStatus='A')) t";
		}
		else if(Query.equalsIgnoreCase("InternalIslamicLimit"))
		{
			sQry="select isnull(sum(obligation),0) as EMI from (select  EMI as obligation,SchemeCardProd from  (SELECT (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI,SchemeCardProd FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A') and Child_Wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) t where t.SchemeCardProd in (select CODE from ng_master_cardproduct where code in (select SchemeCardProd from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A')and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) and isActive='Y' and ReqProduct='Islamic')) Conv_Liability";
		}
		else if(Query.equalsIgnoreCase("InternalConvLimit"))
		{
			sQry="select top 1 isnull(IntConv_Liability.obligation,0) as EMI from (select  EMI as obligation,SchemeCardProd from  (SELECT (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI,SchemeCardProd FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A') and Child_Wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) t where t.SchemeCardProd in (select CODE from ng_master_cardproduct where code in (select SchemeCardProd from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A')and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) and isActive='Y' and ReqProduct='Conventional')) IntConv_Liability";
		}
		//change by saurabh on 4 Feb 19 for PCSP-898
		else if(Query.equalsIgnoreCase("CardType"))
		{
			sQry="select stuff((select ','+Card_Product  from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where child_wi=:WiName and Cardproductselect='True' for xml path('')),1,1,'') as cardType";
		}
		//change by saurabh for PPG start
		else if(Query.equalsIgnoreCase("XmlLogIntegration_RLOS"))
		{
			sQry="select OUTPUT_XML from NG_RLOS_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("XmlLogIntegration_CC"))
		{
			sQry="select OUTPUT_XML from NG_CC_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("XmlLogIntegration_CDOB"))
		{
			sQry="select OUTPUT_XML from NG_CDOB_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("XmlLogIntegration_PL"))
		{
			sQry="select OUTPUT_XML from NG_PL_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("Score_Range"))
		{//For SVT
			sQry="select top 1 ReferenceNo,AECB_Score,Range from ( select ReferenceNo as ReferenceNo,AECB_Score as AECB_Score,Range as Range, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock)  where child_Wi = :Child_wi  and Request_Type='ExternalExposure'  and cifId !='Corporate' and cifId not in (select companyCif from NG_RLOS_GR_CompanyDetails where comp_winame =:Child_wi and  companyCif is not null )) as ext order by ReferenceNo desc";
			
		}
				else if(Query.equalsIgnoreCase("Score_header"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = :Wi_Name union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi = :Wi_Name) as ext_expo";
		}
		else if(Query.equalsIgnoreCase("Score_Range_RLOS"))
		{
			sQry="select top 1 ReferenceNo,AECB_Score,Range from ( select ReferenceNo as ReferenceNo,AECB_Score as AECB_Score,Range as Range, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock)  where wi_name = :Wi_Name  and Request_Type='ExternalExposure'  and cifId !='Corporate' and cifId not in (select companyCif from NG_RLOS_GR_CompanyDetails where comp_winame =:Wi_Name and  companyCif is not null )) as ext order by ReferenceNo desc";
		}
		//Added by Shivang for Self-Employed CAM on 10-01-2019
		else if(Query.equalsIgnoreCase("AECBHistSignatory"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi=:WiName  and Liability_type='Individual_CIF' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Individual_CIF') as ext_expo";
		}
		else if(Query.equalsIgnoreCase("AECBHistCompany"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  =:WiName and Liability_type='Corporate_CIF' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Corporate_CIF') as ext_expo";
		}
		else if(Query.equalsIgnoreCase("MOB_Signatory"))
		{//PCASP-2833
		//Below query changed by shivang: as per prod MOB behaviour, tested on WI 46067
			sQry="select isnull(max(MonthsOnBook),0) as MonthsOnBook from ( select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi=:WiName and Liability_type='Individual_CIF' union select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Individual_CIF' union select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook from ng_rlos_cust_extexpo_AccountDetails where child_wi=:WiName and CifId in (select CIF_ID from NG_rlos_customer where wi_name=:WiName) and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true') as ext_expo";

		}
		else if(Query.equalsIgnoreCase("MOB_Company"))
		{//PCASP-2833
		//Below query changed by shivang: as per prod MOB behaviour, tested on WI 46067
			sQry="select isnull(max(MonthsOnBook),0) as MonthsOnBook from ( select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi=:WiName and Liability_type='Corporate_CIF' union select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Corporate_CIF' union select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook from ng_rlos_cust_extexpo_AccountDetails where child_wi=:WiName and CifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:WiName and applicantCategory='Business') and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true') as ext_expo";

		}
		else if(Query.equalsIgnoreCase("WSL24MonthSign"))
		{
			//sQry="select top 1 Worst_Status_Last24Months from NG_rlos_custexpose_Derived where CifId='Corporate' and Child_Wi=:WiName";
			sQry="select  top 1 (select Description from ng_master_aecb_codes with (nolock) where code = Worst_Status_Last24Months) as Worst_Status_Last24Month from NG_rlos_custexpose_Derived where Child_Wi=:WiName and CifId in (select CIF_ID from ng_RLOS_Customer where wi_name=:WiName) order by Worst_Status_Last24Months desc";
		}
		else if(Query.equalsIgnoreCase("WSL24MonthCompany"))
		{
			sQry="select  top 1 (select Description from ng_master_aecb_codes with (nolock) where code = Worst_Status_Last24Months) as Worst_Status_Last24Month from NG_rlos_custexpose_Derived where CifId <> 'Corporate' and Child_Wi=:WiName and Worst_Status_Last24Months is not null";

		}
		//Added By Shivang for Spring 3 CAM
		else if(Query.equalsIgnoreCase("Facility_BookedLast_6Month"))
		{
			sQry="select case  when (select DATEDIFF(month,(select top 1 Loan_Start_Date from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi=:WiName  and Consider_For_Obligations='true' and Loan_Start_Date is not null  order by Loan_Start_Date desc),(select Introduction_Date from NG_CC_EXTTABLE where CC_Wi_Name=:WiName)))<=6 then 'Yes' else 'No' end";
		}
		else if(Query.equalsIgnoreCase("ExposureWithRAK"))
		{
			sQry="select sum(creditLmt) from (select sum (cast (CreditLimit as float)) as creditLmt from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and  Consider_For_Obligations='true' union all select sum(cast (OutstandingAmt as float)) as  creditLmt from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and  Consider_For_Obligations='true' ) as RakExposure";
		}
		else if(Query.equalsIgnoreCase("DisbursalDate")){
			sQry="select top 1 LoanApprovedDate from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true' order by Loan_Start_Date desc";
		}
		else if(Query.equalsIgnoreCase("LoanAmount")){
			sQry="select top 1 (case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'-') else isnull(ProductAmt,'-') END) as TotalLoanAmount from ng_RLOS_CUSTEXPOSE_LoanDetails where child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true' order by Loan_Start_Date desc";
			// original_query="select * from (select TotalLoanAmount,Loan_Start_Date,LoanApprovedDate,Loan_disbursal_date,(case when (select DATEDIFF(DAY,substring(cast(LoanApprovedDate as nvarchar),1,11),(select substring(cast(Introduction_Date as nvarchar),1,11) from NG_CC_EXTTABLE where CC_Wi_Name='CC-0030044064-Process')))>90 then 'false' else 'true' end) as within90days  from ng_RLOS_CUSTEXPOSE_LoanDetails where child_Wi='CC-0030044064-Process' and LoanStat in ('A','Active') and Consider_For_Obligations='true') as t where t.within90days='true' order by TotalLoanAmount desc"
		}//pcasp-2381
		else if(Query.equalsIgnoreCase("FinalFDAmount"))
		{
			//sQry="select isnull(Final_FD_Amount,'') as FDAmount from ng_RLOS_FinacleCore with(nolock) where wi_name=:WiName";
			sQry="select top 1 isnull(t.LienAmount,'') from (select distinct AcctId,LienId,LienAmount,LienRemarks, isnull(LienExpDate,'') as LienExpDate, isnull((select top 1 Consider_for_Obligation from NG_RLOS_GR_LienDet where  Account_Number =AcctId and lienid=LienId and lien_wi_name=:WiName),'') as Consider_for_Obligation from ng_rlos_FinancialSummary_LienDetails  with (nolock) where Child_Wi=:WiName and  cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :WiName) and Consider_For_Obligations = 'true' union all select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = :WiName and applicantcategory='Business' union all  select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :WiName)) and Relation='Joint Holder' and child_wi=:WiName)) as t where t.Consider_for_Obligation='true' order by t.LienExpDate desc";
		}
		else if(Query.equalsIgnoreCase("AvgBal3Month"))
		{
			sQry="select top 1 total_avg_last1year from ng_RLOS_FinacleCore where wi_name=:WiName";
		}
		else if(Query.equalsIgnoreCase("TotalCr3Month"))
		{
			sQry="select top 1 avg_credit_3month from ng_RLOS_FinacleCore where wi_name=:WiName";
		}
		else if(Query.equalsIgnoreCase("CSMNext"))
		{//By shweta for PL CAM
			
			sQry="SELECT top 1 isnull(csmNext,'') as csmNext,isnull(userName,'') as userName FROM NG_RLOS_GR_DECISION with (nolock) WHERE dec_wi_Name =:WiName order by csmNext";
		}
		else if(Query.equalsIgnoreCase("EmpStatus"))
		{
			sQry="select description from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' and Code=:Code";
		}
		else if(Query.equalsIgnoreCase("AECBCheck"))
		{
			sQry="select Corporate from Aecb_Check where Sub_Product=:Code and Employment_type='Self Employed'";
		}
		else if(Query.equalsIgnoreCase("AccountNoforCompany"))
		{//PCASP-2833
			sQry="select stuff ((select ','+AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where Child_Wi=:WiName and  (isDirect='Y' or isDirect=null) and acctstat not in ('C' ,'Closed') and CifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:WiName) for xml path('')),1,1,'') as AcctNumber";
		}
		//PCASP-2833
		else if(Query.equalsIgnoreCase("AccountNoforCustomer"))
		{
			sQry="select stuff ((select ','+AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where Child_Wi=:WiName and acctstat not in ('C' ,'Closed') and CifId  in (select CIF_ID from NG_RLOS_Customer where wi_name=:WiName)  for xml path('')),1,1,'') as AcctNumber";
		}
		else if(Query.equalsIgnoreCase("TotalAUM"))
		{
			sQry="select Total_AUM from ng_RLOS_FinacleCore where wi_name=:WiName";
		}
		else if(Query.equalsIgnoreCase("AuthorizedSign"))
		{//PCASP-2834
			sQry="select  case when count(*) >0 then 'Yes' else 'No' end  from ng_rlos_gr_AuthSignDetails where  ChildMapping = (select parentmapping_auth from NG_RLOS_GR_CompanyDetails with(nolock)  where applicantCategory in ('Authorised signatory') and comp_winame=:WiName)";
		}
		else if(Query.equalsIgnoreCase("Borrowing_Rel_comp"))
		{
			sQry="select count (distinct providerNo) from (select ProviderNo from ng_rlos_cust_extexpo_LoanDetails with (nolock) where  providerNo !='B01' and loanType not in ('Communication Services' , 'cheque returns','guarantor contracts') and  Child_wi=:Child_wi and LoanStat  not in ('Pipeline','CAS-Pipeline') and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi)   union select ProviderNo from ng_rlos_cust_extexpo_CardDetails with (nolock) where   providerNo !='B01' and cardtype  not in ('Communication Services' , 'cheque returns','guarantor contracts') and  Child_wi  =:Child_wi and cardstatus not in ('CAS-Pipeline','Pipeline') and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi)   union select ProviderNo from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where providerNo !='B01' and Accttype not in ('Communication Services' , 'cheque returns','guarantor contracts') and  child_wi = :Child_wi and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi) union select ProviderNo from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where providerNo !='B01' and serviceType not in ('Communication Services' , 'cheque returns','guarantor contracts') and ServiceStat='Active' and child_wi  =:Child_wi and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi )) as ext";
		}
		else if(Query.equalsIgnoreCase("Borrowing_Rel_auth"))
		{
			sQry="select count(distinct providerNo) from (select ProviderNo from ng_rlos_cust_extexpo_LoanDetails with (nolock) where  providerNo !='B01' and loanType not in ('Communication Services' , 'cheque returns','guarantor contracts') and  Child_wi=:Child_wi and LoanStat  not in ('CAS-Pipeline','Pipeline') and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi)   union select ProviderNo from ng_rlos_cust_extexpo_CardDetails with (nolock) where   providerNo !='B01' and cardtype  not in ('Communication Services' , 'cheque returns','guarantor contracts') and  Child_wi  =:Child_wi and cardstatus not in ('CAS-Pipeline','Pipeline') and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi )    union select ProviderNo from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where providerNo !='B01' and Accttype not in ('Communication Services' , 'cheque returns','guarantor contracts') and  child_wi  =:Child_wi and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi ) union select ProviderNo from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where providerNo !='B01' and serviceType not in ('Communication Services' , 'cheque returns','guarantor contracts') and ServiceStat='Active' and child_wi  =:Child_wi and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi )) as ext";
		}
		else if(Query.equalsIgnoreCase("Borrowing_Rel_comp_init"))
		{
			sQry="select count (distinct providerNo) from (select ProviderNo from ng_rlos_cust_extexpo_LoanDetails with (nolock) where  providerNo !='B01' and loanType not in ('Communication Services' , 'cheque returns','guarantor contracts') and  wi_name=:Child_wi and LoanStat  not in ('Pipeline','CAS-Pipeline') and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi)   union select ProviderNo from ng_rlos_cust_extexpo_CardDetails with (nolock) where   providerNo !='B01' and cardtype  not in ('Communication Services' , 'cheque returns','guarantor contracts') and  wi_name  =:Child_wi and cardstatus not in ('CAS-Pipeline','Pipeline') and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi)   union select ProviderNo from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where providerNo !='B01' and Accttype not in ('Communication Services' , 'cheque returns','guarantor contracts') and  wi_name = :Child_wi and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi) union select ProviderNo from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where providerNo !='B01' and serviceType not in ('Communication Services' , 'cheque returns','guarantor contracts') and ServiceStat='Active' and wi_name  =:Child_wi and cifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi )) as ext";
		}
		else if(Query.equalsIgnoreCase("Borrowing_Rel_auth_init"))
		{
			sQry="select count(distinct providerNo) from (select ProviderNo from ng_rlos_cust_extexpo_LoanDetails with (nolock) where  providerNo !='B01' and loanType not in ('Communication Services' , 'cheque returns','guarantor contracts') and  wi_name=:Child_wi and LoanStat  not in ('CAS-Pipeline','Pipeline') and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi)   union select ProviderNo from ng_rlos_cust_extexpo_CardDetails with (nolock) where   providerNo !='B01' and cardtype  not in ('Communication Services' , 'cheque returns','guarantor contracts') and  wi_name  =:Child_wi and cardstatus not in ('CAS-Pipeline','Pipeline') and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi )    union select ProviderNo from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where providerNo !='B01' and Accttype not in ('Communication Services' , 'cheque returns','guarantor contracts') and  wi_name  =:Child_wi and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi ) union select ProviderNo from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where providerNo !='B01' and serviceType not in ('Communication Services' , 'cheque returns','guarantor contracts') and ServiceStat='Active' and wi_name  =:Child_wi and cifId in (select AuthSignCIFNo from ng_rlos_gr_AuthSignDetails where sign_winame=:Child_wi )) as ext";
		}
		else if(Query.equalsIgnoreCase("BTC_Tot_Exposure"))
		{
			sQry =	"select isNull (sum(cast(isnull(TotalOutstandingAmt,0) as float)),0) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where   (LoanType in ('AUTO','AL','MURABAHA') or SchemeCardProd like '%RAKFIN%') and Child_wi =:Child_wi and LoanStat not in ('Pipeline','CAS-Pipeline','C','Closed') and CifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:Child_wi)"; 
		}
		else if(Query.equalsIgnoreCase("BTC_Tot_Exposure_init"))
		{
			sQry =	"select isNull (sum (cast(isnull(TotalOutstandingAmt,0) as float)),0) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where (LoanType in ('AUTO','AL','MURABAHA') or SchemeCardProd like '%RAKFIN%') and wi_name =:WiName and LoanStat not in ('Pipeline','CAS-Pipeline','C','Closed') and CifId in (select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame=:WiName)"; 
		}
		//PCASP-2384
		else if(Query.equalsIgnoreCase("LoanAmount_RFL")){
			sQry="select top 1 TotalLoanAmount,Baltenor,instalmentsdone  from(select cast((case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'') else isnull(ProductAmt,'') END) as float) as TotalLoanAmount,RemainingInstalments as Baltenor,TotalNoOfInstalments as instalmentsdone   from ng_RLOS_CUSTEXPOSE_LoanDetails where SchemeCardProd like '%rakfin%' and child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true') as ext order by TotalLoanAmount desc";
		}
		else if(Query.equalsIgnoreCase("LoanAmount_MOR")){
			sQry="select   sum (cast(ProductAmt as float )) as TotalLoanAmount from ng_RLOS_CUSTEXPOSE_LoanDetails where loantype in ('IJARAH','HOME IN ONE','HOME','MORTGAGE') and child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true'";
		}
		else if(Query.equalsIgnoreCase("PropValueSE"))
		{
			sQry="select sum(cast (PropertyValue as bigint)) as PropertyValue from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi=:WiName and LoanStat not in ('C','Closed') and isnull(ProviderNo,'')!='B01' and LoanType in ('Home','HOME IN ONE','ML','IJARAH','REAL ESTATE MORTGAGE EI') and CifId in (select CIF_ID from ng_RLOS_Customer where wi_name=:WiName)";
		}
		else if(Query.equalsIgnoreCase("Score_Range_Comp"))
		{
			sQry="select top 1 ReferenceNo,AECB_Score,Range from (select ReferenceNo as ReferenceNo,AECB_Score as AECB_Score,Range as Range, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock) where child_wi = :Child_wi  and Request_Type='ExternalExposure' and cifId in (select companyCif from NG_RLOS_GR_CompanyDetails where comp_winame = :Child_wi and  applicantCategory='Business')) as ext order by cifId desc";
		}
		else if(Query.equalsIgnoreCase("Score_Range_Comp_RLOS"))
		{
			sQry="select top 1 ReferenceNo,AECB_Score,Range from (select ReferenceNo as ReferenceNo,AECB_Score as AECB_Score,Range as Range, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock) where wi_name = :Wi_Name  and Request_Type='ExternalExposure' and cifId in (select companyCif from NG_RLOS_GR_CompanyDetails where comp_winame = :Wi_Name and  applicantCategory='Business')) as ext order by ReferenceNo desc";
		}
		else if(Query.equalsIgnoreCase("ToB")){
		    sQry="SELECT top 1 isnull(Description,'') as city FROM NG_MASTER_City with (nolock) WHERE code =:Code or Description=:Code";
		}
		else if(Query.equalsIgnoreCase("RakProtectEnroll")){
			sQry="select isnull(CardShield,'false') as CardShield from ng_rlos_IGR_Eligibility_CardLimit where wi_name=:Wi_Name and Cardproductselect = 'true'";
		}
		else if(Query.equalsIgnoreCase("VerifiedBy")){
		    sQry="select top 1 isnull(UserName,'') from NG_MASTER_SourceCode where UserId=:userid";
		}
		//Deepak Below added for Contract_type_Desc
		else if(Query.equalsIgnoreCase("Contract_type_Desc")){
		    sQry="SELECT top 1 isnull(Description,'') as contract_type from ng_master_contract_type  with (nolock) WHERE code =:Code";
		}
		
		return sQry;
	}
	
	public static String DocTemplateData_RLOS(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("Tenor"))
		{
			
			sQry="SELECT top 1 isnull(OutstandingAmt,'') as OutstandingAmt,isnull(TotalNoOfInstalments,'') as TotalNoOfInstalments,isnull(Consider_For_Obligations,'') as Consider_For_Obligations,isnull(PaymentsAmt,'') as PaymentsAmt FROM ng_rlos_cust_extexpo_LoanDetails with (nolock) WHERE Wi_Name =:WiName AND LoanType = 'Personal Loan' order by loanapproveddate desc";
		}
		else if(Query.equalsIgnoreCase("input"))
		{
			sQry="SELECT top 1 isnull(OutstandingAmt,'') as OutstandingAmt,isnull(TotalNoOfInstalments,'') as TotalNoOfInstalments,isnull(Consider_For_Obligations,'') as Consider_For_Obligations,isnull(PaymentsAmt,'') as PaymentsAmt FROM ng_rlos_cust_extexpo_LoanDetails with (nolock) WHERE Wi_Name =:WiName AND LoanType = 'Car Loan' order by loanapproveddate desc";
		}
		else if(Query.equalsIgnoreCase("NTBCheck"))
		{
			sQry="SELECT isnull(NTB,'false') as NTB FROM NG_RLOS_EXTTABLE with (nolock) WHERE WIname =:WiName";
		}
		//changes done by shivang for Islamic App form changes starts
		else if(Query.equalsIgnoreCase("MurahbaFileDate"))
		{
			sQry="SELECT isnull(File_Generated_Date,'') as File_Generated_Date FROM NG_RLOS_Murabha_Warranty with (nolock) WHERE Murhabha_WIName =:WiName";
		}
		//changes done by shivang for Islamic App form change end
		//changes done by shivang for Islamic App form changes starts
		else if(Query.equalsIgnoreCase("TownOfBirth"))
		{
			sQry="SELECT isnull (Description,'') as Description FROM NG_MASTER_City with (nolock) WHERE Code =:WiName";
		}
		//changes done by shivang for Islamic App form change end
		else if(Query.equalsIgnoreCase("DesignationCheck"))
		{
			sQry="select isnull(Description,'') from NG_MASTER_Designation where Code =:Code";
			//sQry="select arab from Ng_Manishhh where Code=:Code";
		}
		//by saurabh on 10/06/2109
		else if(Query.equalsIgnoreCase("FetchArabic")){
			sQry="select isnull(ArabicDesc,'') as ArabicDesc,isnull(EngTemplate,'') as EngTemplate from ng_master_NoTinReason where EnglishDesc =:Reason";
		}
		else if(Query.equalsIgnoreCase("Country"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code =:Code";
		}
		else if(Query.equalsIgnoreCase("CardTypesList"))
		{
			sQry="select substring((select ','+Card_Product from ng_rlos_IGR_Eligibility_CardLimit where wi_name =:WiName and Cardproductselect='true' for xml path('')),2,9999)";
		}
		//Added by Shivang for card description
		else if(Query.equalsIgnoreCase("CardTypesListForAppForm"))
		{
			sQry=" select substring ((select ','+ description from (select distinct DESCRIPTION from ng_master_carddescription where code in (select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where wi_name =:WiName and Cardproductselect='true')) t for xml path('')),2,9999)";
		}
		else if(Query.equalsIgnoreCase("CardLimitValue"))
		{
			sQry="select limit from (select CashLimit as 'limit',CAC_Indicator from ng_rlos_cust_extexpo_CardDetails where Wi_Name=:WiName union select limit as 'limit',CAC_Indicator from ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName=:WiName ) as temp where temp.CAC_Indicator='true'";
		}
		else if(Query.equalsIgnoreCase("AccountNoForExisting"))
		{
			sQry="select top 1 AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where Wi_Name=:WiName and CIFID in( select CIF_ID from ng_RLOS_Customer where wi_name=:WiName) and AcctStat= 'ACTIVE' order by AccountOpenDate asc";
		}
		else if(Query.equalsIgnoreCase("LoanDeatilsForIMFormEMI"))
		{
			sQry="select CashLimit  from ng_rlos_cust_extexpo_CardDetails with (nolock) where Consider_For_Obligations='true' and    Wi_Name =:Wi_Name union select  PaymentsAmt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where  Consider_For_Obligations='true' and  Wi_Name =:Wi_Name";
		}
		else if(Query.equalsIgnoreCase("LoanDeatilsForIMFormOUTSTA"))
		{
			sQry="select CurrentBalance  from ng_rlos_cust_extexpo_CardDetails with (nolock) where Consider_For_Obligations='true' and    Wi_Name =:Wi_Name union select OutstandingAmt  from ng_rlos_cust_extexpo_LoanDetails with (nolock) where  Consider_For_Obligations='true' and  Wi_Name =:Wi_Name";
		}
		// write this for getting bankName description by deepanshu prashar on 07July2022
		else if (Query.equalsIgnoreCase("bankName")){
			sQry = "select isnull(Description,'') from NG_MASTER_BankName where Code =:Code";
			
		}
		return sQry;
	}
	
	public static String DocTemplateData_PL(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("PLE"))
		{
			//query change by saurabh for PCSP-23 on 12Feb19 
			//modified by nikhil 12th frb
			sQry="select workstepName,userName,csmNext from (select workstepName,userName,csmNext,Decision,row_number() over (partition by workstepname,username order by insertionOrderId desc) as RN from NG_RLOS_GR_DECISION where workstepName in ('CAD_Analyst1','Cad_Analyst2') and dec_wi_Name =:WiName and (Decision like 'Approve%' or Decision like 'Submit%' or Decision like 'Reject%' or Decision like 'Refer%')) as dummy_DECISION";
		}
		else if(Query.equalsIgnoreCase("OfficeCity") || Query.equalsIgnoreCase("ResidenceCity") || Query.equalsIgnoreCase("HomeCity"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_City with (nolock) WHERE code =:Code";
		}
		//changes for PSCP-23 
		else if(Query.equalsIgnoreCase("OfficeCountry") || Query.equalsIgnoreCase("ResidenceCountry") || Query.equalsIgnoreCase("HomeCountry") || Query.equalsIgnoreCase("Nationality_CC"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code =:Code";
		}
		else if(Query.equalsIgnoreCase("Designation_CC"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_Designation with (nolock) WHERE code =:Code";
		}
		//changes by shivang for PCAS-2744 starts
		else if(Query.equalsIgnoreCase("DectechDecision"))
		{
			sQry="select top 1 isnull(Decision,'') from ng_rlos_IGR_Eligibility_PersonalLoan where Child_Wi =:WiName";
		}
		else if(Query.equalsIgnoreCase("DectechDelegation"))
		{
			sQry="select top 1 isnull(DelegationAuthorithy,'') from ng_rlos_IGR_Eligibility_PersonalLoan where Child_Wi =:WiName";
		}
		else if(Query.equalsIgnoreCase("MOB"))
		{
			sQry="select isnull(max(MonthsOnBook),0) as MonthsOnBook from (select max(cast(MonthsOnBook as float)) as MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and (Consider_For_Obligations='true' or Take_Over_Indicator='true' or Settlement_Flag='true') union all select max(cast(MonthsOnBook as float)) as MonthsOnBook from ng_rlos_cust_extexpo_AccountDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and (Consider_For_Obligations='true' or Take_Over_Indicator ='true' or Settlement_Flag='true') union all select max(cast(MonthsOnBook as float)) as MonthsOnBook from ng_rlos_cust_extexpo_CardDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and (Consider_For_Obligations='true' or Take_Over_Indicator='true' or Settlement_Flag='true')) t ";
		}
		else if(Query.equalsIgnoreCase("TotalObligation"))
		{
			sQry="select sum (EMI) as obligation from (SELECT DISTINCT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI, Consider_For_Obligations AS ConsiderForObligation,child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_LoanDetails WITH (nolock) where ProviderNo!='B01'and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL  SELECT  CardType AS ExposureScheme, Liability_type AS LiabilityType, CardEmbossNum AS ApplicationNo, (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and  Child_Wi=:WiName UNION ALL SELECT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI,  Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM  ng_rlos_cust_extexpo_LoanDetails WITH (nolock) WHERE ProviderNo!='B01' and LoanStat!='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT  CardType AS ExposureScheme, Liability_type AS LiabilityType, CardEmbossNum AS ApplicationNo, (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_CardDetails WITH (nolock) WHERE ProviderNo!='B01' and CardStatus!='Pipeline' and Consider_For_Obligations='true' and child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_AcctDetails WITH (nolock) WHERE ProviderNo != 'B01' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_AccountDetails WITH (nolock) WHERE ProviderNo != 'B01' and AcctStat !='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName) t"; 
		}
		 else if(Query.equalsIgnoreCase("AECBHistory"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  child_Wi  =:WiName and isnull(ProviderNo,'') != 'B01' and (Consider_For_Obligations='true' or Take_Over_Indicator='true' or Settlement_Flag='true') union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) from ng_rlos_cust_extexpo_LoanDetails where child_Wi  = :WiName and isnull(ProviderNo,'') != 'B01' and (Consider_For_Obligations='true' or Take_Over_Indicator='true' or Settlement_Flag='true')) as ext_expo";
		}
		else if(Query.equalsIgnoreCase("EligibleCardLimit"))
		{
			sQry="select top 1 Eligible_Card from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi=:WiName";
		}
		else if(Query.equalsIgnoreCase("NumOfCardRequested"))
		{
			sQry="select count(Card_Product) from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true'";
		
		}
		else if(Query.equalsIgnoreCase("IsConventionalSelected"))
		{
			sQry="select count(*) from ng_master_cardproduct where CODE = (select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true') and ReqProduct='Conventional' and isActive='Y'";
		}
		else if(Query.equalsIgnoreCase("FinalLimitForConventional"))
		{
			sQry="select cast(Eligible_Limit as float) as IslamicLimit from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Card_Product in (" +
			"select code from ng_master_cardproduct where code in ( select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true') and ReqProduct='Conventional' and isActive='Y')";
		}
		else if(Query.equalsIgnoreCase("IslamicAmtFetch"))
		{
			sQry="select sum(cast(Eligible_Limit as float)) as IslamicLimit from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Card_Product in (" +
			"select code from ng_master_cardproduct where code in ( select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi=:WiName and Cardproductselect='true') and ReqProduct='Islamic' and isActive='Y')";
		}
		else if(Query.equalsIgnoreCase("OD_LoanEmi"))
		{
			sQry="select sum (EMI) as obligation from (SELECT DISTINCT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI, Consider_For_Obligations AS ConsiderForObligation,child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_LoanDetails WITH (nolock) where isnull(ProviderNo,'')!='B01'and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT  LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI,  Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM  ng_rlos_cust_extexpo_LoanDetails WITH (nolock) WHERE isnull(ProviderNo,'')!='B01' and LoanStat!='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_AcctDetails WITH (nolock) WHERE isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_AccountDetails WITH (nolock) WHERE isnull(ProviderNo,'') != 'B01' and AcctStat !='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL select distinct a.Request_Type as ExposureScheme,a.CardType as LiabilityType ,a.CardEmbossNum AS ApplicationNo,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI,Consider_For_Obligations AS ConsiderForObligation, a.child_wi AS wi_name from ng_RLOS_CUSTEXPOSE_CardDetails a left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on  CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name where a.Child_Wi=:WiName and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS')and (a.SchemeCardProd='LOC STANDARD' or a.SchemeCardProd='LOC PREFERRED') and Consider_For_Obligations='true' and CustRoleType !='Secondary' and CardStatus !='C') t";
	
		}
		else if(Query.equalsIgnoreCase("cardLimitCalc"))
		{
			sQry="select sum (EMI) as obligation from  (SELECT (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A') and Child_Wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED') UNION ALL SELECT  (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT))* 3) / 100) END) AS EMI FROM ng_rlos_cust_extexpo_CardDetails WITH (nolock) WHERE ProviderNo!='B01' and CardStatus!='Pipeline' and Consider_For_Obligations='true' and  (CardStatus='Active' or CardStatus='A') and child_wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) t";

		}
		//changes by shivang for PCAS-2744 starts end
		//Adding for Pre-Approved Cam
		else if(Query.equalsIgnoreCase("WorstD24"))
		{
			sQry="select Description from ng_master_Aecb_Codes where code in (select Worst_Status_Last24Months from (select derived.Worst_Status_Last24Months from ng_rlos_cust_extexpo_CardDetails card join NG_RLOS_CUSTEXPOSE_Derived derived on card.child_Wi=derived.Child_Wi where derived.Request_Type='ExternalExposure' and card.child_Wi=:WiName union all select WorstStatus24Months from ng_rlos_cust_extexpo_LoanDetails loan join NG_RLOS_CUSTEXPOSE_Derived derived on derived.Child_Wi=loan.child_Wi where derived.Request_Type='ExternalExposure' and loan.child_Wi=:WiName union all select Worst_Status_Last24Months from ng_rlos_cust_extexpo_AccountDetails Account join NG_RLOS_CUSTEXPOSE_Derived derived on derived.Child_Wi=Account.child_Wi where derived.Request_Type='ExternalExposure' and Account.child_Wi=:WiName) as WostStatus where isnull(Worst_Status_Last24Months,'')!='')";
		}
		else if(Query.equalsIgnoreCase("MortgageValue"))
		{
			sQry="select TotalAmt from ng_rlos_cust_extexpo_LoanDetails where Consider_For_Obligations='true' and isnull(ProviderNo,'')!='B01' and LoanType like 'Mortgage%' and child_Wi=:WiName";
		}else if(Query.equalsIgnoreCase("RakEliteValue"))
		{
			sQry="select sum(PropertyValue) from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi=:WiName and Consider_For_Obligations='true' and LoanStat not in ('C','Closed') and isnull(ProviderNo,'')!='B01' and LoanType like 'Home%'";
		}
		else if(Query.equalsIgnoreCase("DectechEligibility"))
		{
			sQry="select top 1 EligibleExposure from ng_rlos_IGR_Eligibility_PersonalLoan where Child_Wi=:WiName";
		}
		else if(Query.equalsIgnoreCase("ExternalLiabilityLimit"))
		{
			sQry="select isnull(sum(EMI),0) as ExtCardLim from (select (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT))* 3) / 100) END) AS EMI from ng_rlos_cust_extexpo_CardDetails where child_Wi=:WiName and ProviderNo!='B01' and CardStatus!='Pipeline' and Consider_For_Obligations='true' and  (CardStatus='Active' or CardStatus='A')) t";
		
		}
		else if(Query.equalsIgnoreCase("InternalIslamicLimit"))
		{
			sQry="select isnull(sum(obligation),0) as EMI from (select  EMI as obligation,SchemeCardProd from  (SELECT (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI,SchemeCardProd FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A') and Child_Wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) t where t.SchemeCardProd in (select CODE from ng_master_cardproduct where code in (select SchemeCardProd from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A')and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) and isActive='Y' and ReqProduct='Islamic')) Conv_Liability";
		
		}
		else if(Query.equalsIgnoreCase("InternalConvLimit"))
		{
			sQry="select top 1 isnull(IntConv_Liability.obligation,0) as EMI from (select  EMI as obligation,SchemeCardProd from  (SELECT (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI,SchemeCardProd FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A') and Child_Wi=:WiName and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) t where t.SchemeCardProd in (select CODE from ng_master_cardproduct where code in (select SchemeCardProd from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and Consider_For_Obligations='true' and (CardStatus='Active' or CardStatus='A')and SchemeCardProd not in('LOC STANDARD','LOC PREFERRED')) and isActive='Y' and ReqProduct='Conventional')) IntConv_Liability";

		}
		//change by saurabh on 4 Feb 19 for PCSP-898
		else if(Query.equalsIgnoreCase("CardType"))
		{
			sQry="select stuff((select ','+Card_Product  from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where child_wi=:WiName and Cardproductselect='True' for xml path('')),1,1,'') as cardType";
		}
		//change by saurabh for PPG start
		else if(Query.equalsIgnoreCase("XmlLogIntegration_RLOS"))
		{
			sQry="select OUTPUT_XML from NG_RLOS_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("XmlLogIntegration_CC"))
		{
			sQry="select OUTPUT_XML from NG_CC_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("XmlLogIntegration_CDOB"))
		{
			sQry="select OUTPUT_XML from NG_CDOB_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("XmlLogIntegration_PL"))
		{
			sQry="select OUTPUT_XML from NG_PL_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("Score_Range"))
		{
			sQry="select top 1 ReferenceNo,AECB_Score,Range from ( select ReferenceNo as ReferenceNo,AECB_Score as AECB_Score,Range as Range, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock)  where child_Wi = :Child_wi  and Request_Type='ExternalExposure'  and cifId !='Corporate' and cifId not in (select companyCif from NG_RLOS_GR_CompanyDetails where comp_winame =:Child_wi and  companyCif is not null )) as ext order by ReferenceNo desc";
		
		}
				else if(Query.equalsIgnoreCase("Score_header"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = :Wi_Name union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi = :Wi_Name) as ext_expo";
		}
		else if(Query.equalsIgnoreCase("Score_Range_RLOS"))
		{
			sQry="select top 1 ReferenceNo,AECB_Score,Range from ( select ReferenceNo as ReferenceNo,AECB_Score as AECB_Score,Range as Range, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock)  where wi_name = :Wi_Name  and Request_Type='ExternalExposure'  and cifId !='Corporate' and cifId not in (select companyCif from NG_RLOS_GR_CompanyDetails where comp_winame =:Wi_Name and  companyCif is not null )) as ext order by ReferenceNo desc";
		}
		//Added by Shivang for Self-Employed CAM on 10-01-2019
		else if(Query.equalsIgnoreCase("AECBHistSignatory"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi=:WiName  and Liability_type='Individual_CIF' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Individual_CIF') as ext_expo";
		}
		else if(Query.equalsIgnoreCase("AECBHistCompany"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  =:WiName and Liability_type='Corporate_CIF' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Corporate_CIF') as ext_expo";
		}
		else if(Query.equalsIgnoreCase("MOB_Signatory"))
		{
			sQry="select isnull(max(MonthsOnBook),0) as MonthsOnBook from ( select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi=:WiName and Liability_type='Individual_CIF' union select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Individual_CIF') as ext_expo";
		}
		else if(Query.equalsIgnoreCase("MOB_Company"))
		{
			sQry="select isnull(max(MonthsOnBook),0) as MonthsOnBook from ( select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi=:WiName and Liability_type='Individual_CIF' union select MAX(cast(cast(isnull(MonthsOnBook,'0') as float) as int)) as MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi=:WiName and Liability_type='Individual_CIF') as ext_expo";
		}
		else if(Query.equalsIgnoreCase("WSL24MonthSign"))
		{
			//sQry="select top 1 Worst_Status_Last24Months from NG_rlos_custexpose_Derived where CifId='Corporate' and Child_Wi=:WiName";
			sQry="select  top 1 (select Description from ng_master_aecb_codes with (nolock) where code = Worst_Status_Last24Months) as Worst_Status_Last24Month from NG_rlos_custexpose_Derived where Child_Wi=:WiName and Worst_Status_Last24Months is not null";
		}
		else if(Query.equalsIgnoreCase("WSL24MonthCompany"))
		{
			sQry="select  top 1 (select Description from ng_master_aecb_codes with (nolock) where code = Worst_Status_Last24Months) as Worst_Status_Last24Month from NG_rlos_custexpose_Derived where CifId <> 'Corporate' and Child_Wi=:WiName and Worst_Status_Last24Months is not null";

		}
		//Added By Shivang for Spring 3 CAM
		else if(Query.equalsIgnoreCase("Facility_BookedLast_6Month"))
		{
			sQry="select case  when (select DATEDIFF(month,(select top 1 Loan_Start_Date from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi=:WiName  and Consider_For_Obligations='true' and Loan_Start_Date is not null  order by Loan_Start_Date desc),(select Introduction_Date from NG_PL_EXTTABLE where PL_Wi_Name=:WiName)))<=6 then 'Yes' else 'No' end";
		}
		else if(Query.equalsIgnoreCase("ExposureWithRAK"))
		{
			sQry="select sum(creditLmt) from (select sum (cast (CreditLimit as float)) as creditLmt from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and  Consider_For_Obligations='true' union all select sum(cast (OutstandingAmt as float)) as  creditLmt from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi=:WiName and  Consider_For_Obligations='true' ) as RakExposure";
		}
		else if(Query.equalsIgnoreCase("DisbursalDate")){
			sQry="select top 1 LoanApprovedDate from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true' order by Loan_Start_Date desc";
		}
		else if(Query.equalsIgnoreCase("LoanAmount")){
			sQry="select top 1 (case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'-') else isnull(ProductAmt,'-') END) as TotalLoanAmount from ng_RLOS_CUSTEXPOSE_LoanDetails where child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true' order by Loan_Start_Date desc";
			// original_query="select * from (select TotalLoanAmount,Loan_Start_Date,LoanApprovedDate,Loan_disbursal_date,(case when (select DATEDIFF(DAY,substring(cast(LoanApprovedDate as nvarchar),1,11),(select substring(cast(Introduction_Date as nvarchar),1,11) from NG_CC_EXTTABLE where CC_Wi_Name='CC-0030044064-Process')))>90 then 'false' else 'true' end) as within90days  from ng_RLOS_CUSTEXPOSE_LoanDetails where child_Wi='CC-0030044064-Process' and LoanStat in ('A','Active') and Consider_For_Obligations='true') as t where t.within90days='true' order by TotalLoanAmount desc"
		}//pcasp-2381
		//PCASP-2384
		else if(Query.equalsIgnoreCase("LoanAmount_RFL")){
			sQry="select top 1 (case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'-') else isnull(ProductAmt,'-') END) as TotalLoanAmount,RemainingInstalments as Baltenor,TotalNoOfInstalments as instalmentsdone   from ng_RLOS_CUSTEXPOSE_LoanDetails where SchemeCardProd like '%rakfin%' and child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true' order by TotalLoanAmount asc";
			// original_query="select * from (select TotalLoanAmount,Loan_Start_Date,LoanApprovedDate,Loan_disbursal_date,(case when (select DATEDIFF(DAY,substring(cast(LoanApprovedDate as nvarchar),1,11),(select substring(cast(Introduction_Date as nvarchar),1,11) from NG_CC_EXTTABLE where CC_Wi_Name='CC-0030044064-Process')))>90 then 'false' else 'true' end) as within90days  from ng_RLOS_CUSTEXPOSE_LoanDetails where child_Wi='CC-0030044064-Process' and LoanStat in ('A','Active') and Consider_For_Obligations='true') as t where t.within90days='true' order by TotalLoanAmount desc"
		}//pcasp-2381
		else if(Query.equalsIgnoreCase("LoanAmount_MOR")){
			sQry="select   sum (cast(ProductAmt as float )) as TotalLoanAmount from ng_RLOS_CUSTEXPOSE_LoanDetails where loantype in ('IJARAH','HOME IN ONE','HOME','MORTGAGE') child_Wi=:WiName and LoanStat in ('A','Active') and Consider_For_Obligations='true'";
			// original_query="select * from (select TotalLoanAmount,Loan_Start_Date,LoanApprovedDate,Loan_disbursal_date,(case when (select DATEDIFF(DAY,substring(cast(LoanApprovedDate as nvarchar),1,11),(select substring(cast(Introduction_Date as nvarchar),1,11) from NG_CC_EXTTABLE where CC_Wi_Name='CC-0030044064-Process')))>90 then 'false' else 'true' end) as within90days  from ng_RLOS_CUSTEXPOSE_LoanDetails where child_Wi='CC-0030044064-Process' and LoanStat in ('A','Active') and Consider_For_Obligations='true') as t where t.within90days='true' order by TotalLoanAmount desc"
		}//pcasp-2381
		
		else if(Query.equalsIgnoreCase("FinalFDAmount"))
		{
			//sQry="select isnull(Final_FD_Amount,'') as FDAmount from ng_RLOS_FinacleCore with(nolock) where wi_name=:WiName";
			sQry="select top 1 isnull(t.LienAmount,'') from (select distinct AcctId,LienId,LienAmount,LienRemarks, isnull(LienExpDate,'') as LienExpDate, isnull((select top 1 Consider_for_Obligation from NG_RLOS_GR_LienDet where  Account_Number =AcctId and lienid=LienId and lien_wi_name=:WiName),'') as Consider_for_Obligation from ng_rlos_FinancialSummary_LienDetails  with (nolock) where Child_Wi=:WiName and  cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :WiName) and Consider_For_Obligations = 'true' union all select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = :WiName and applicantcategory='Business' union all  select Main_Cif from ng_rlos_custexpose_LinkedICF where Linked_CIFs in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :WiName)) and Relation='Joint Holder' and child_wi=:WiName)) as t where t.Consider_for_Obligation='true' order by t.LienExpDate desc";
		}
		else if(Query.equalsIgnoreCase("AvgBal3Month"))
		{
			sQry="select top 1 total_avg_last1year from ng_RLOS_FinacleCore where wi_name=:WiName";
		}
		else if(Query.equalsIgnoreCase("TotalCr3Month"))
		{
			sQry="select top 1 avg_credit_3month from ng_RLOS_FinacleCore where wi_name=:WiName";
		}
		else if(Query.equalsIgnoreCase("CSMNext"))
		{//By shweta for PL CAM
			
			sQry="SELECT top 1 isnull(csmNext,'') as csmNext,isnull(userName,'') as userName FROM NG_RLOS_GR_DECISION with (nolock) WHERE dec_wi_Name =:WiName order by csmNext";
		}
		return sQry;
	}
	
	public static String RiskRating_PL(String Query)
	{
		String sQry="";
		
		
		 if(Query.equalsIgnoreCase("CustomerCategory"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_CustomerCategory with (nolock) WHERE code =:Code";
		}
		else if(Query.equalsIgnoreCase("Nationality"))
		{
			sQry="SELECT isnull(Description,'') as city FROM NG_MASTER_Country with (nolock) WHERE code =:Code";
		}
		
		return sQry;
	}
	public static String PL_Js(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("PL_NewCif"))
		{
			sQry="select isnull((select New_Cif_no from ng_rlos_decisionHistory with (nolock) where wi_name =:Wi_Name),(select cif_id from NG_PL_EXTTABLE where PL_wi_name =:Wi_Name))";
		}
		else if(Query.equalsIgnoreCase("PL_AcctId"))
		{
			sQry="select top 1 AcctId,AcctStat from ng_RLOS_CUSTEXPOSE_AcctDetails where (Wi_Name = :Parent_Wi_Name or Child_Wi = :Wi_Name) and FundingAccount = 'true'";
		}
		else if(Query.equalsIgnoreCase("PL_AcctNo"))
		{
			sQry="select distinct New_Account_Number from ng_rlos_decisionHistory where wi_name =:Parent_Wi_Name or wi_name =:Wi_Name";
		}
		else if(Query.equalsIgnoreCase("PL_ReportUrl"))
		{
			sQry="select ReportURL from NG_RLOS_CUSTEXPOSE_Derived where child_wi=:Wi_Name and Request_Type='ExternalExposure' and cifid in(select case when NTB = 'true' then '' else CIF_ID end from ng_RLOS_Customer where wi_name=:Wi_Name)";
		}
		else if(Query.equalsIgnoreCase("PLE_ReportUrl"))
		{
			sQry="select top 1 ReportURL from ( select ReportURL as ReportURL, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock)  where child_Wi = :Wi_Name  and Request_Type='ExternalExposure'  and cifId !='Corporate' and cifId not in (select companyCif from NG_RLOS_GR_CompanyDetails where comp_winame =:Wi_Name and  companyCif is not null )) as ext order by cifId desc";

		}
		else if(Query.equalsIgnoreCase("PL_ReportUrl_company"))
		{
			sQry="select ReportURL from NG_RLOS_CUSTEXPOSE_Derived where child_wi=:Wi_Name and Request_Type='ExternalExposure' and cifid=(select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame =:Wi_Name and applicantCategory='Business' group by CompanyCIF)";
		}
		else if(Query.equalsIgnoreCase("schemecode"))
		{
			sQry="select schemedesc from ng_master_scheme where  schemeid=:schemeid";
		}
		else if(Query.equalsIgnoreCase("FetchData"))
		{
			final ResourceBundle properties = PropertyResourceBundle.getBundle("System_parameters.properties");
			String columns ="";
			String query="";
			Enumeration enuKeys = properties.getKeys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				columns+=key+",";	
			}
			if(columns.endsWith(",")){
				columns=columns.substring(0, columns.length()-1);
			}
			sQry="select "+columns+" FROM NG_RLOS_EXTTABLE with (nolock) where itemindex =:item";
		}
		return sQry;
	}
	
	%>

<%
	try {
		
		xmlParser.setInputXML(sOutputXML);
		WriteLog("outputxml is : "+sOutputXML);
		String mainCode = xmlParser.getValueOf("MainCode");
            if(mainCode.equals("0")) {
		
                int iTotalRetreived = Integer.parseInt(sOutputXML.substring(sOutputXML.indexOf("<TotalRetrieved>") + 16, sOutputXML.indexOf("</TotalRetrieved>")));
                WFXmlResponse objXmlResponse = new WFXmlResponse(sOutputXML);
				WriteLog("total row received : "+iTotalRetreived);
                if (iTotalRetreived == 0) {
                   sFinalString="NODATAFOUND";
                } else {
                    
                    int j = 0;
                    for (WFXmlList objList = objXmlResponse.createList("Records", "Record"); objList.hasMoreElements(); objList.skip()) {
                        String sRowData = objList.getVal("Record");
        
						
                        while (sRowData.indexOf("<") > 0) {
                            String sColName = sRowData.substring(sRowData.indexOf("<") + 1, sRowData.indexOf(">"));
                            String sColValue = objList.getVal(sColName);
                            sColValue = (sColValue.toUpperCase()).equals("NULL") ? "" : sColValue;
							sColValue=sColValue.replaceAll("COMMA:",",");
							sColValue=sColValue.replaceAll("APOSTRO:","'");
							sColValue=sColValue.replaceAll("AMP:","&");
							sColValue=sColValue.replaceAll("DOL:","$");
							sFinalString=sFinalString+sColValue+"#";
                            
                            sRowData = sRowData.substring(sRowData.indexOf("</" + sColName + ">") + sColName.length() + 3, sRowData.length());
                        }
                        
                        sFinalString=sFinalString+"~";
                    }
                }
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

