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
	 String Query=request.getParameter("Query");
	
	 String params=request.getParameter("wparams");
	 
	 String pname=request.getParameter("pname");
	 
	 String sQuery="";

	try{
	if(pname.equalsIgnoreCase("IL"))
	{
		sQuery=internalLiability(Query);
	}
	if(pname.equalsIgnoreCase("EL"))
	{
		sQuery=externalLiability(Query);
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
			sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,CifId,isNULL(Liability_type,'Individual_CIF'),SchemeCardProd, AgreementId,(case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'') else isnull(ProductAmt,'') END),NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'',PreviousLoanDBR,PreviousLoanTAI,Settlement_Flag,LoanStat from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = :Child_Wi and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat not in ('Pipeline','C') order by Liability_type desc";
			//wparams="Child_Wi=="+winame;
		}
	else if(Query.equalsIgnoreCase("PLE"))
		{
		//changes donre for PCAS-2702
			sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,CifId,isNULL(Liability_type,'Individual_CIF') as Liability_type,SchemeCardProd, AgreementId,(case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'') else isnull(ProductAmt,'') END),NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'-' as TypeofOD,PreviousLoanDBR,PreviousLoanTAI,Settlement_Flag,LoanStat from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = :Child_Wi and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat != 'Pipeline' order by Liability_type desc";
			
			//wparams="Child_Wi=="+winame;
			
		}
	else if(Query.equalsIgnoreCase("fetchCALE"))
		{
			sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,A.CifId,Liability_type as LiabilityType,CardType as ProductType,SchemeCardProd as TypeofCardLoan, CardEmbossNum as AgreementId, CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI , General_Status,Limit_Increase as LimitIncrease,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.OutstandingBalance,0) ELSE ISnull(outstandingAmt,0) End) as OutstandingAmount, Consider_For_Obligations as ConsiderForObligations,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.interestRate,0) ELSE ISnull(a.InterestRate,0) End)  as InterestRate,MonthsOnBook,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then ISNULL((CAST(b.INSTALMENTpERIOD AS INT)-CAST(b.rEMAININGemi AS INT)),'') else ISNULL((CAST(b.INSTALMENTpERIOD AS INT)-CAST(b.rEMAININGemi AS INT)),'') end )  as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR,Settlement_Flag from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Wi_Name =:Wi_Name and CustRoleType !='Secondary' and CardStatus !='C' order by Liability_type desc";
		}
	else if(Query.equalsIgnoreCase("fetchCr"))
		{
			sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi =:Child_Wi and CustRoleType !='Secondary'";
		}
	else if(Query.equalsIgnoreCase("fetchPL"))
	{
		sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi =:Child_Wi and CustRoleType !='Secondary' and CardStatus !='C' order by Liability_type desc"; 
	}
	else if(Query.equalsIgnoreCase("fetchPLE"))
	{
		//sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi =:Child_Wi and CustRoleType !='Secondary' order by Liability_type desc";
		sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  case when SchemeCardProd like '%LOC%' then (select sum(cast(MonthlyAmount as float)) from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi =:Child_Wi and replace(CardNumber,'I','')=CardEmbossNum) else PaymentsAmount end as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,case when SchemeCardProd like '%LOC%' then (select max(InterestRate) from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi =:Child_Wi and replace(CardNumber,'I','')=CardEmbossNum) else '' end as InterestRate,MonthsOnBook,case when SchemeCardProd like 'LOC%' then (select top 1 ISNULL((CAST(max(INSTALMENTpERIOD) AS INT)-CAST(min(rEMAININGemi) AS INT)),'') from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi =:Child_Wi and replace(CardNumber,'I','')=CardEmbossNum ) else '' end as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) where (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary') and a.Child_Wi =:Child_Wi and CustRoleType !='Secondary' order by Liability_type desc";
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
		sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,'OverDraft' as ProductType,cifid,Account_Type,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi  =:Wi_Name  and ODType != ''";
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
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType,Liability_type, agreementid,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,'',concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','','',MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name =:Wi_Name and LoanStat not in  ('Pipeline','Closed') and ProviderNo != 'B01' order by Liability_type desc";
		
			//wparams="Wi_Name=="+winame;
	}
	else if(Query.equalsIgnoreCase("Cr"))
	{
	//below query changed for pcsp-26
			sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,Paymentsamt,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi =:Child_Wi and LoanStat != 'Pipeline'  and ProviderNo != 'B01' order by Liability_type desc ";
	}
	else if(Query.equalsIgnoreCase("PL"))
		{
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,Paymentsamt,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = :Child_Wi and LoanStat != 'Pipeline'  and ProviderNo != 'B01' order by Liability_type desc ";
			//wparams="Child_Wi=="+winame;
		}
	else if(Query.equalsIgnoreCase("PL_CSM"))
	{
	sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,Paymentsamt,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = :Child_Wi and LoanStat not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc ";
	
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
		sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,'',CurrentBalance,ISNULL(Consider_For_Obligations,''),remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,cardstatus,Settlement_Flag from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus != 'Pipeline' and ProviderNo != 'B01'  order by Liability_type desc";
	}
	else if(Query.equalsIgnoreCase("fetchPLE"))
	{
		sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,ISNULL(Consider_For_Obligations,''),remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',Settlement_Flag,MonthsOnBook,cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi=:Child_wi and CardStatus not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc";
	}
	else if(Query.equalsIgnoreCase("ODCA"))
	{
		//query changed by nikhil for PCSP-945
		//Provider no check added by deepak on 07/07/2019 for PCAS-1450
		sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,AcctType as TypeOfContract,'Individual_CIF' as LiabilityType,AcctId,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Wi_Name =:Wi_Name and AcctStat not in  ('Pipeline','Closed') and ProviderNo !='B01'";
	}
	else if(Query.equalsIgnoreCase("ODCrCC"))
	{
		//query changed by nikhil for PCSP-945
		//Provider no check added by deepak on 07/07/2019 for PCAS-1450
		sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,AcctType as TypeOfContract,AcctId,'Individual_CIF' as LiabilityType,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Child_Wi =:Child_Wi and AcctStat != 'Pipeline' and ProviderNo !='B01' ";//sagarika for PCAS-2116
	}
	else if(Query.equalsIgnoreCase("ODPL"))
	{
	//Provider no check added by deepak on 07/07/2019 for PCAS-1450
	sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,'Overdraft' as TypeOfContract,'Individual_CIF' as LiabilityType,AcctId,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Child_Wi =:Child_Wi and AcctStat != 'Pipeline' and ProviderNo !='B01'";
	}
	else if (Query.equalsIgnoreCase("SCCA"))
	{
		sQry="select 'ng_rlos_cust_extexpo_ServicesDetails' as table_name,ServiceType as TypeOfContract,'Individual_CIF',ServiceID as LiabilityType,'','' as EMI,'','','','','','','',Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',settlement_flag,'',MonthsOnBook from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where wi_name=:wi_name and ServiceStat not in  ('Pipeline','Closed') and ProviderNo != 'B01'";
	}
	else if (Query.equalsIgnoreCase("SCCrCC") || Query.equalsIgnoreCase("SCPL"))
	{
		sQry="select 'ng_rlos_cust_extexpo_ServicesDetails' as table_name,ServiceType ,ServiceID,'Individual_CIF' as LiabilityType,'','','' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'','' as OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where Child_Wi =:Child_Wi and ServiceStat != 'Pipeline' and ProviderNo !='B01'";
	}
	else
	{
		//QueueData
		sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'External_Liability' and ActivityName=:ActivityName ORDER BY col_seq";
	}
		return sQry;
	}
	%>
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
			sQry="select table_name,reference,Product_Type,CustRoleType,DLP,Tenor,Consider_For_Obligations,Liab_desc,LimAmt,EMI,NoOfDaysInPipeline,TKO,TK from (select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,concat(AgreementId,'-', ProviderNo) as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,LoanDesc as Liab_desc,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_Wi = :child_Wi and (LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline') and ProviderNo !='B01' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,concat(CardEmbossNum,'-', ProviderNo) as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,CardTypeDesc as Liab_desc,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_Wi = :child_Wi and (CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline') and ProviderNo !='B01')as ext where format(convert(datetime,DLP),'yyyy-MM-dd') >(DATEADD(MONTH,-6, getdate()))  union all Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,SchemeCardProd as Liab_desc,TotalloanAmount as LimAmt,RemainingInstalments as EMI,NoOfDaysInPipeline,'' as TKO,'' as TK from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where (child_Wi = :child_Wi or Wi_Name = :child_Wi)  and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' and CifId=:cifId) or (LoanStat = 'AL-Pipeline' and CifId=:cifId)) and (ProviderNo !='B01' or ProviderNo is null) order by DLP desc";
	
		}
		else if(Query.equalsIgnoreCase("!CCCRPL"))
		{
			//query modified by nikhil for PCSP-856
			 sQry="select table_name,reference,Product_Type,CustRoleType,DLP,Tenor,Consider_For_Obligations,Liab_desc,LimAmt,EMI,NoOfDaysInPipeline,TKO,TK from (select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,concat(AgreementId,'-', ProviderNo) as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,LoanDesc as Liab_desc,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name = :Wi_Name and (LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline') and ProviderNo !='B01' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,concat(CardEmbossNum,'-', ProviderNo) as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,CardTypeDesc as Liab_desc,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where Wi_Name = :Wi_Name and (CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline') and ProviderNo !='B01' union Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true') as Consider_For_Obligations,SchemeCardProd as Liab_desc,TotalloanAmount as LimAmt,RemainingInstalments as EMI,NoOfDaysInPipeline,'' as TKO,'' as TK from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where  Wi_Name = :Wi_Name  and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' ) or (LoanStat = 'AL-Pipeline' and CifId=:CifId )) and (ProviderNo !='B01' or ProviderNo is null))as ext where format(convert(datetime,DLP),'yyyy-MM-dd') >(DATEADD(MONTH,-6, getdate())) order by DLP desc";
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
			sQry="select distinct Cardproductselect,Card_Product,Eligible_Limit,Final_Limit,CC_Waiver,flag,combined_limit FROM  "+table_name+" with (nolock) WHERE wi_name =:wi_name order by Cardproductselect desc";
		}
		else if(Query.equalsIgnoreCase("!CA"))
		{
			sQry="select distinct Cardproductselect,Card_Product,Eligible_Limit,Final_Limit,CC_Waiver,flag,combined_limit FROM  "+table_name+" with (nolock) WHERE Child_wi = :Child_wi order by Cardproductselect desc";
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
			 sQry="select (row_number() over(order by product)) as s_no,Product,Card_Product,max(isnull(CACCalculatedLimit,0)),Eligible_Card,max(isnull(final_limit,0)),Decision,Declined_Reason,Deviation_Code_Refer,'',Delegation_Authorithy,Score_Grade FROM  "+table_name+" with (nolock) WHERE child_wi =:child_wi group by Product,Card_Product,Card_Product,Decision,Declined_Reason,Deviation_Code_Refer,Eligible_Card,Delegation_Authorithy,Score_Grade";
			 
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
			sQry="select S_no,RequestedLoan,RequestedAppType,CACCalculatedlimit,EligibleExposure,FinalLoanAmount,Decision,DeclinedReasonCode,ReferReasoncode,DeviationCodeRefer,DelegationAuthorithy FROM  "+table_name+" with (nolock) WHERE wi_name =:wi_name order by s_no";
		}
		else if(Query.equalsIgnoreCase("!CA"))
		{
			 sQry="select S_no,RequestedLoan,RequestedAppType,CACCalculatedlimit,EligibleExposure,FinalLoanAmount,Decision,DeclinedReasonCode,ReferReasoncode,DeviationCodeRefer,DelegationAuthorithy FROM  "+table_name+" with (nolock) WHERE Child_wi = :Child_wi  order by s_no";
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
		
		String sQry = "select ContractType,Contract_Role_Type,TotalNo,DataProvidersNo,RequestNo,DeclinedNo,RejectedNo,NotTakenUpNo,ActiveNo,ClosedNo from NG_RLOS_CUSTEXPOSE_RecordDestribution with (nolock) where Child_Wi =:Child_Wi and CifId!=''";
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
			sQry="select isnull(max(MonthsOnBook),0) as MonthsOnBook from (select top 1 MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' union all select top 1 MonthsOnBook from ng_rlos_cust_extexpo_AccountDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' union all select top 1 MonthsOnBook from ng_rlos_cust_extexpo_CardDetails where child_Wi=:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true') t ";
		}
		else if(Query.equalsIgnoreCase("TotalObligation"))
		{
			sQry="select sum (EMI) as obligation from (SELECT DISTINCT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI, Consider_For_Obligations AS ConsiderForObligation,child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_LoanDetails WITH (nolock) where ProviderNo!='B01'and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL  SELECT  CardType AS ExposureScheme, Liability_type AS LiabilityType, CardEmbossNum AS ApplicationNo, (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_CardDetails WITH (nolock) WHERE Consider_For_Obligations='true' and  Child_Wi=:WiName UNION ALL SELECT LoanType AS ExposureScheme, Liability_type AS LiabilityType, AgreementId AS ApplicationNo, PaymentsAmt AS EMI,  Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM  ng_rlos_cust_extexpo_LoanDetails WITH (nolock) WHERE ProviderNo!='B01' and LoanStat!='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT  CardType AS ExposureScheme, Liability_type AS LiabilityType, CardEmbossNum AS ApplicationNo, (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 3) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 3) / 100) END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_CardDetails WITH (nolock) WHERE ProviderNo!='B01' and CardStatus!='Pipeline' and Consider_For_Obligations='true' and child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CashLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CashLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_RLOS_CUSTEXPOSE_AcctDetails WITH (nolock) WHERE ProviderNo != 'B01' and Consider_For_Obligations='true' and  child_wi=:WiName UNION ALL SELECT AcctType AS ExposureScheme, 'Individual_CIF' AS LiabilityType, AcctId AS ApplicationNo, (CASE WHEN AcctType = 'Overdraft' THEN (CASE WHEN ((CAST(isNULL(CreditLimit, 0) AS FLOAT) * 5) / 100) < 100 THEN '100' ELSE (((CAST(isNULL(CreditLimit, 0) AS FLOAT)) * 5) / 100) END) ELSE '' END) AS EMI, Consider_For_Obligations AS ConsiderForObligation, child_wi AS wi_name FROM ng_rlos_cust_extexpo_AccountDetails WITH (nolock) WHERE ProviderNo != 'B01' and AcctStat !='Pipeline' and Consider_For_Obligations='true' and  child_wi=:WiName) t"; 
		}
		 else if(Query.equalsIgnoreCase("AECBHistory"))
		{
			sQry="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  child_Wi  =:WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where child_Wi  = :WiName and isnull(ProviderNo,'') != 'B01' and Consider_For_Obligations='true') as ext_expo";
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
		else if(Query.equalsIgnoreCase("XmlLogIntegration_PL"))
		{
			sQry="select OUTPUT_XML from NG_PL_XMLLOG_HISTORY where MESSAGE_ID=:Code";
		}
		else if(Query.equalsIgnoreCase("Score_Range"))
		{
			sQry="select top 1 ReferenceNo,AECB_Score,Range from NG_rlos_custexpose_Derived with (nolock) where child_Wi = :Child_wi  and Request_Type='ExternalExposure'";
		}
		else if(Query.equalsIgnoreCase("Score_Range_RLOS"))
		{
			sQry="select top 1 ReferenceNo,AECB_Score,Range from NG_rlos_custexpose_Derived with (nolock) where Wi_name = :Wi_Name  and Request_Type='ExternalExposure'";
		}
		//change by saurabh for PPG end
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
			sQry=" select substring ((select ','+ description from (select distinct DESCRIPTION from ng_master_cardproduct where code in (select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where wi_name =:WiName and Cardproductselect='true') and isActive='Y') t for xml path('')),2,9999)";
		}
		else if(Query.equalsIgnoreCase("CardLimitValue"))
		{
			sQry="select limit from (select CashLimit as 'limit',CAC_Indicator from ng_rlos_cust_extexpo_CardDetails where Wi_Name=:WiName union select limit as 'limit',CAC_Indicator from ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName=:WiName ) as temp where temp.CAC_Indicator='true'";
		}
		return sQry;
	}
	
	public static String DocTemplateData_PL(String Query)
	{
		String sQry="";
		if(Query.equalsIgnoreCase("CSMNext"))
		{
			
			sQry="SELECT top 1 isnull(csmNext,'') as csmNext,isnull(userName,'') as userName FROM NG_RLOS_GR_DECISION with (nolock) WHERE dec_wi_Name =:WiName order by csmNext";
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
			sQry="select top 1 AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where (Wi_Name = :Parent_Wi_Name or Child_Wi = :Wi_Name) and FundingAccount = 'true'";
		}
		else if(Query.equalsIgnoreCase("PL_AcctNo"))
		{
			sQry="select distinct New_Account_Number from ng_rlos_decisionHistory where wi_name =:Parent_Wi_Name or wi_name =:Wi_Name";
		}
		else if(Query.equalsIgnoreCase("PL_ReportUrl"))
		{
			sQry="select ReportURL from NG_RLOS_CUSTEXPOSE_Derived where child_wi=:Wi_Name and Request_Type='ExternalExposure'";
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
