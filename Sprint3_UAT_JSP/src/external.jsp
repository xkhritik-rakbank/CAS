<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : External 
//Author                     : Nikita Singhal	
//DESC						 : To fetch field values from DB and display on form
//Date written (DD/MM/YYYY)  : 29/06/2017
//---------------------------------------------------------------------------------------------------->

<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.Date,java.io.*,java.util.ArrayList,java.util.List" %>
<%@ page import="java.lang.Object"%>
<%@ page import="java.text.*"%>
<%@ page import ="java.util.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.output.*" %>
<%@ page import = "com.lowagie.text.pdf.PdfReader, com.lowagie.text.pdf.codec.TiffImage, com.lowagie.text.pdf.RandomAccessFileOrArray"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.custom.XMLParser"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="java.lang.Iterable"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page  import= "org.w3c.dom.Element"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.util.LinkedHashMap"%>
<%@ page  import= "java.util.Map"%>
<%@ page  import= "java.io.ByteArrayInputStream"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.io.FileInputStream"%>
<%@ page  import= "java.io.FileNotFoundException"%>
<%@ page  import= "java.io.InputStream"%>
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import="java.lang.String.*"%>

<%
	//String Wi_Name = "CAS-0000004182-PROCESS";
		
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String params="";
	String table=request.getParameter("table");
	String Wi_Name =(String) request.getParameter("Wi_Name");
	String AgreementId=(String) request.getParameter("AgreementId");
	String Liability_type=(String) request.getParameter("liability_type");
	String WD_UID = request.getParameter("WD_UID"); 
	WriteLog("appServerType jsp: result: "+appServerType);
	WriteLog("wrapperIP jsp: result: "+wrapperIP);	
	WriteLog("wrapperPort jsp: result: "+wrapperPort);
	WriteLog("sessionId jsp: result: "+sessionId);
	WriteLog("wrapperPort jsp: result: "+wrapperPort);
	WriteLog("cabinetName jsp: result: "+cabinetName);
	WriteLog("Wi_Name jsp: result: "+Wi_Name);
	WriteLog("AgreementId jsp: result: "+AgreementId);	
	XMLParser xmlParserData1=null;
	XMLParser xmlParserData2=null;
	XMLParser xmlParserData_Common=null;
	XMLParser xmlParserData_MAX=null;
	XMLParser xmlParserDataAecb=null;	
	XMLParser objXmlParser =null;
	XMLParser objXmlParser_Common =null;
	XMLParser objXmlParser_MAX =null;
	XMLParser objXmlParser_Aecb =null;
	XMLParser xmlParserData =null;
	String inputXML_AECB=null;
	String inputXML1=null;
	String outputXML_AECB=null;
	String outputXML1=null;
	String outputXML_Common=null;
	String outputXML_MAX=null;
	String inputXML2=null;
	String outputXML2=null;
	String inputXML_Common=null;
	String inputXML_MAX=null;	
	String Loan_Start_Date= "-";
	String Loan_disbursal_date= "-";
	String PaymentMode= "-";
	String TotalAmount="-";
	String LastUpdateDate="-";
	String PaymentsAmt="-";	
	String fullname = "-";
	String totOutBal = "-";
	String totOverdue = "-";
	String noDefCont = "-";
	String totExposure = "-";
	String worCurrPayDelay = "-";
	String worsPayDel24 = "-";
	String worStatlast24 = "-";
	String noLiab = "-";
	String noCheqRet3 = "-";
	String noDdsRet3 = "-";
	String noCheqRet6 = "-";
	String noDdsRet6 = "-";
	String typeCont = "-";
	String phase = "-";
	String role = "-";
	String agree = "-";
	String closeDate = "-";
	String dateLastUp = "-";
	String totAmt = "-";
	String emi = "-";
	String methPay = "-";
	String WriteoffStat = "-";
	String WriteoffStatDt = "-";
	String noDayPayDel = "-";
	String mob = "-";
	String currFlag = "-";
	String lastRepMonYr = "-";
	String dpd6month = "-";
	String dpd18month = "-";
	String cardsB = "-";
	String schemeCardProd = "-";
	String propVal= "-";
	String marketCode = "-";
	String internalCharge ="-";
	String dpd30Last6Months = "-";
	String dpd60Last18Months = "-";
	String externalwriteoffcheck= "0";
	String aecbHistMonthCnt = "-";	
	String totalAmount = "-";
	String paymentsAmount = "-";
	String paymentMode = "-";
	String noofInstallments = "-";
	String LastRepmtDt = "-";
	String isDuplicate = "-";
	String disputeAlert = "-";
	String NofdaysPmtDelay = "-";
	String produc_type ="-";
	String currMaxUtil = "-";
	String NofDaysPaymentDelay = "-";
	String TotalNoOfInstalments = "-";
	String RemainingInstalments= "-";
	String OutStanding_Balance= "-";
	String OverdueAmt= "-";
	String LoanDesc= "-";	
	String StartDate= "-";
	String ClosedDate= "-";
	String DisputeAlert= "-";
    String IsDuplicate= "-";
	String ExternalWriteOffCheck= "0";
	String AECBHistMonthCnt= "-";
	String creditLimit = "-";	 
	String TotalOutstanding="-";
	String TotalOverdue="-";
	String NoOfContracts="-";
	String Total_Exposure="-";
	String AecbHistory="-";
	String WorstCurrentPaymentDelay="-";
	String Worst_PaymentDelay_Last24Months="-";
	String Worst_Status_Last24Months="-";
	String Nof_Records="-";
	String NoOf_Cheque_Return_Last3="-";
	String Nof_DDES_Return_Last3Months="-";
	String Nof_DDES_Return_Last6Months="-";
	String Nof_Cheque_Return_Last6="-";
	String DPD30_Last6Months="-";
	String Nof_Enq_Last30Days = "-";
	String Nof_Enq_Last90Days = "-";
	String Oldest_Contract_Start_Date = "-";
	String product_type ="-";
	String DPD5_Last3Months = "-";
	String MaxavgUtil="-";
	String Maxaecbhist="-";
	String subXML1="";
	String subXML2="";
	String subXML_Common="";
	String subXML_MAX="";
	String subXML_Aecb="";	
	String mainCodeValue1 ="";
	String mainCodeValue_Common ="";
	String mainCodeValue_MAX="";
	String mainCodeValue2 ="";
	String mainCodeAecb = "";
	String Dpd60plus_last12month="-";
	String Dpd5_last12month="-";
	
	  //public static final String commonParse(String parseXml,String tagName1,String subTagName1);
		//public static final LinkedHashMap<Integer, String> getTagDataParent(String parseXml,String tagName2,String subTagName2)

	try{
		//change by surabh for PCSP - 237
		 String sQuery1 = null , sQuery2 = null , sQuery_Common =null, sQuery_Max = null,sQuery_AecbHistory=null;
		 sQuery_Common = "select isNull(TotalOutstanding,'-') as TotalOutstanding,isNull(TotalOverdue,'-') as TotalOverdue,isNull(NoOfContracts,'-') as NoOfContracts,isNull(Total_Exposure,'-') as Total_Exposure,isNull(Oldest_Contract_Start_Date,'-') as Oldest_Contract_Start_Date,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(Worst_PaymentDelay_Last24Months,'-') as Worst_PaymentDelay_Last24Months,(select Description from ng_master_aecb_codes with (nolock) where code = Worst_Status_Last24Months) as Worst_Status_Last24Months, isNull(Nof_Records,'-') as Nof_Records,isNull(NoOf_Cheque_Return_Last3,'-') as NoOf_Cheque_Return_Last3,isNull(Nof_DDES_Return_Last3Months,'-') as Nof_DDES_Return_Last3Months,isNull(Nof_Cheque_Return_Last6,'-') as Nof_Cheque_Return_Last6,isNull(DPD30_Last6Months,'-') as DPD30_Last6Months,isNull(Nof_Enq_Last30Days,'-') as Nof_Enq_Last30Days,isNull(Nof_Enq_Last90Days,'-') as  Nof_Enq_Last90Days,isNull(Nof_DDES_Return_Last6Months,'-') as   Nof_DDES_Return_Last6Months from NG_rlos_custexpose_Derived with (nolock) where Child_Wi='"+Wi_Name+"' and Request_Type = 'ExternalExposure' and CifId=case when '"+Liability_type+"'='Corporate_CIF' then (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by CompanyCIF) else (select case when ntb='true' then '' else CIF_ID end from ng_rlos_customer where wi_name = '"+Wi_Name+"') end";
		 
		 //added by nikhil for PCSP-672 / PCSP-692
		 //updated for PCSP-695
		 sQuery_Max = "select isnull(cast(max(cast(maxutil as float)) as nvarchar),'-') as CurUtilRate ,isnull(max(cast(maxaecbhist as int)),'-') as AECBHistMonthCnt,isnull(max(cast(dpd30 as int)),'-') as dpd30Last6Months,isnull(max(cast(DPD60_Last18Months as int)),'-') as DPD60_Last18Months,isnull(max(cast(ExternalWriteOffCheck as int)),'-') as ExternalWriteOffCheck,isnull(max(cast(DPD5_Last3Months as int)),'-') as DPD5_Last3Months,isnull(max(cast(DPD5_Last12Months as int)),'-') as DPD5_Last12Months,isnull(max(cast(DPD60Plus_Last12Months as int)),'-') as DPD60Plus_Last12Months from (select max(cast(CurUtilRate as float)) as maxutil,max(cast(AECBHistMonthCnt as int)) as maxaecbhist,max(cast(DPD30_Last6Months as int)) as dpd30,max(cast(DPD60_Last18Months as int)) as DPD60_Last18Months,max(cast(ExternalWriteOffCheck as int)) as ExternalWriteOffCheck,max(cast(DPD5_Last3Months as int)) as DPD5_Last3Months,max(cast(DPD5_Last12Months as int)) as DPD5_Last12Months,max(cast(DPD60Plus_Last12Months as int)) as DPD60Plus_Last12Months from ng_rlos_cust_extexpo_AccountDetails with (nolock) where child_wi = '"+Wi_Name+"' and ProviderNo!='B01' and custroletype not in ('Guarantor') and CifId=case when '"+Liability_type+"'='Corporate_CIF' then (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by CompanyCIF) else (select case when ntb='true' then '' else CIF_ID end from ng_rlos_customer where wi_name = '"+Wi_Name+"') end union all select max(cast(CurUtilRate as float)) as maxutil,max(cast(AECBHistMonthCnt as int)) as maxaecbhist,max(cast(DPD30_Last6Months as int)) as dpd30,max(cast(DPD60_Last18Months as int)) as DPD60_Last18Months,max(cast(ExternalWriteOffCheck as int)) as ExternalWriteOffCheck,max(cast(DPD5_Last3Months as int)) as DPD5_Last3Months,max(cast(DPD5_Last12Months as int)) as DPD5_Last12Months,max(cast(DPD60Plus_Last12Months as int)) as DPD60Plus_Last12Months from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_wi  = '"+Wi_Name+"' and ProviderNo!='B01' and custroletype not in ('Guarantor') and CifId=case when '"+Liability_type+"'='Corporate_CIF' then (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by CompanyCIF) else (select case when ntb='true' then '' else CIF_ID end from ng_rlos_customer where wi_name = '"+Wi_Name+"') end union all select max(cast(CurUtilRate as float)) as maxutil,max(cast(AECBHistMonthCnt as int)) as maxaecbhist,max(cast(DPD30_Last6Months as int)) as dpd30,max(cast(DPD60_Last18Months as int)) as DPD60_Last18Months,max(cast(ExternalWriteOffCheck as int)) as ExternalWriteOffCheck,max(cast(DPD5_Last3Months as int)) as DPD5_Last3Months,max(cast(DPD5_Last12Months as int)) as DPD5_Last12Months,max(cast(DPD60Plus_Last12Months as int)) as DPD60Plus_Last12Months from ng_rlos_cust_extexpo_CardDetails with (nolock) where  child_wi = '"+Wi_Name+"' and ProviderNo!='B01' and custroletype not in ('Guarantor') and CifId=case when '"+Liability_type+"'='Corporate_CIF' then (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by CompanyCIF) else (select case when ntb='true' then '' else CIF_ID end from ng_rlos_customer where wi_name = '"+Wi_Name+"') end) as Alias_Ext_exposure";
		 
		 sQuery_AecbHistory = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"+ Wi_Name + "' and cardtype not in ( '85','99','Communication Services','TelCo-Mobile Prepaid','101','Current/Saving Account with negative Balance','58','Overdraft') and custroletype not in ('Co-Contract Holder','Guarantor') and CifId=case when '"+Liability_type+"'='Corporate_CIF' then (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by CompanyCIF) else (select case when ntb='true' then '' else CIF_ID end from ng_rlos_customer where wi_name = '"+Wi_Name+"') end union all select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+ Wi_Name + "' and loantype not in ('85','99','Communication Services','TelCo-Mobile Prepaid','101','Current/Saving Account with negative Balance','58','Overdraft') and custroletype not in ('Co-Contract Holder','Guarantor') and CifId=case when '"+Liability_type+"'='Corporate_CIF' then (select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'Corporate') else 'Corporate' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by CompanyCIF) else (select case when ntb='true' then '' else CIF_ID end from ng_rlos_customer where wi_name = '"+Wi_Name+"') end) as ext_expo";
		
		if(AgreementId!=null && table.equalsIgnoreCase("ng_rlos_cust_extexpo_LoanDetails"))
		{
		 	//change done for PCAS-2070	   
			//Deepak changes for payment frequency in EMI
			//sQuery1 = "select  case when liability_type='Corporate_CIF' then (select case when COUNT(ApplicantName)>0 then  ISNULL(ApplicantName,'') else '' end   from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by ApplicantName) else (select FirstName+' '+case when middlename is null then '' else MiddleName end+' '+LAstName from  ng_RLOS_Customer with (nolock) where Wi_Name = '"+Wi_Name+"') end as Fullname, isNull(OutStanding_Balance,'-') as OutStanding_Balance,isNull(loanType,'-')  as loanType,isNull(ProviderNo,'-') as agreementId,isNull(loanStat,'-') as loanStat,CustRoleType as RoleofCustomer,isNull(LastUpdateDate,'-') as DateLastUpdated,isNull(OutstandingAmt,'-') as OutstandingAmt,isNull(TotalNoOfInstalments,'-') as TotalNoOfInstalments,isNull(RemainingInstalments,'-') as RemainingInstalments,isNull(OverdueAmt,'-') as OverdueAmt,isNull(IsCurrent,'-') as IsCurrent,isNull(CurUtilRate,'-')as CurUtilRate,isNull(DPD30_Last6Months,'-') as DPD30_Last6Months,isNull(DPD60_Last18Months,'-') as DPD60_Last18Months,isNull(AECBHistMonthCnt,'-') as AECBHistMonthCnt,isNull(ExternalWriteoffCheck,'0') as ExternalWriteoffCheck,isNull(LoanApprovedDate,'-') as LoanApprovedDate,isNull(LoanMaturityDate,'-') as LoanMaturityDate,isNull(TotalAmt,'-') as TotalAmt,(select Description from ng_master_aecb_codes with (nolock) where code = WriteoffStat) as WriteoffStat,isNull(WriteoffStatDt,'-') as WriteoffStatDt,isNull(NofdaysPmtDelay,'-') as NofdaysPmtDelay,isNull(PaymentsAmt,'-') as PaymentsAmt,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(DPD5_Last3Months,'-') as DPD5_Last3Months,isNull(LoanDesc,'-') as LoanDesc,(CASE when isDuplicate is null then '-' else case when isDuplicate=1 then 'Yes' else 'No'END END )as IsDuplicate,isNull(PaymentMode,'-') as PaymentMode,isNull(LastRepmtDt,'-') as LastRepmtDt,isnull(DPD60Plus_Last12Months,'-') as Dpd60plus_last12month,ISNULL(DPD5_Last12Months,'-') as Dpd5_last12month from ng_rlos_cust_extexpo_LoanDetails with (nolock) where AgreementId='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";
			sQuery1 = "select  case when liability_type='Corporate_CIF' then (select case when COUNT(ApplicantName)>0 then  ISNULL(ApplicantName,'') else '' end   from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by ApplicantName) else (select FirstName+' '+case when middlename is null then '' else MiddleName end+' '+LAstName from  ng_RLOS_Customer with (nolock) where Wi_Name = '"+Wi_Name+"') end as Fullname, isNull(OutStanding_Balance,'-') as OutStanding_Balance,isNull(loanType,'-')  as loanType,isNull(ProviderNo,'-') as agreementId,isNull(loanStat,'-') as loanStat,CustRoleType as RoleofCustomer,isNull(LastUpdateDate,'-') as DateLastUpdated,isNull(OutstandingAmt,'-') as OutstandingAmt,isNull(TotalNoOfInstalments,'-') as TotalNoOfInstalments,isNull(RemainingInstalments,'-') as RemainingInstalments,isNull(OverdueAmt,'-') as OverdueAmt,isNull(IsCurrent,'-') as IsCurrent,isNull(CurUtilRate,'-')as CurUtilRate,isNull(DPD30_Last6Months,'-') as DPD30_Last6Months,isNull(DPD60_Last18Months,'-') as DPD60_Last18Months,isNull(AECBHistMonthCnt,'-') as AECBHistMonthCnt,isNull(ExternalWriteoffCheck,'0') as ExternalWriteoffCheck,isNull(LoanApprovedDate,'-') as LoanApprovedDate,isNull(LoanMaturityDate,'-') as LoanMaturityDate,isNull(TotalAmt,'-') as TotalAmt,(select Description from ng_master_aecb_codes with (nolock) where code = WriteoffStat) as WriteoffStat,isNull(WriteoffStatDt,'-') as WriteoffStatDt,isNull(NofdaysPmtDelay,'-') as NofdaysPmtDelay,(case when Pmtfreq like '%30 days%' or Pmtfreq is null then isnull(PaymentsAmt,0) when Pmtfreq like '%60 days%' then (cast(isnull(PaymentsAmt,'0') as float)/2) when Pmtfreq like '%90 days%' then (cast(isnull(PaymentsAmt,'0') as float)/3) when Pmtfreq like '%180 days%' then (cast(isnull(PaymentsAmt,'0') as float)/6) else (cast(isnull(PaymentsAmt,'0') as float)/12) end) as PaymentsAmt,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(DPD5_Last3Months,'-') as DPD5_Last3Months,isNull(LoanDesc,'-') as LoanDesc,(CASE when isDuplicate is null then '-' else case when isDuplicate=1 then 'Yes' else 'No'END END )as IsDuplicate,isNull(PaymentMode,'-') as PaymentMode,isNull(LastRepmtDt,'-') as LastRepmtDt,isnull(DPD60Plus_Last12Months,'-') as Dpd60plus_last12month,ISNULL(DPD5_Last12Months,'-') as Dpd5_last12month from ng_rlos_cust_extexpo_LoanDetails with (nolock) where AgreementId='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";
			params = "AgreementId=='"+AgreementId+"'~~Wi_Name=="+Wi_Name;
		}
		if(AgreementId!=null && table.equalsIgnoreCase("ng_rlos_cust_extexpo_CardDetails"))
		{
			//changes for PCSP-696
			sQuery1 = "select  case when liability_type='Corporate_CIF' then (select case when COUNT(ApplicantName)>0 then  ISNULL(ApplicantName,'') else '' end   from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by ApplicantName) else (select FirstName+' '+case when middlename is null then '' else MiddleName end+' '+LAstName from  ng_RLOS_Customer with (nolock) where Wi_Name = '"+Wi_Name+"') end as Fullname,'',isNull(dpd30Last6Months,'-') as dpd30Last6Months,isNull(DPD60_Last18Months,'-') as DPD60_Last18Months,isNull(aecbHistMonthCnt,'-') as aecbHistMonthCnt,isNull(cardtype,'-') as cardtype,isNull(ProviderNo,'-') as cardEmbossNum,isNull(cardStatus,'-') as cardStatus,CustRoleType as RoleofCustomer,isNull(StartDate,'-') as StartDate,isNull(ClosedDate,'-') as ClosedDate,isNull(LastUpdateDate,'-') as DateLastUpdated,'','-' as totalAmount,(CASE WHEN ((CAST(isNULL(CashLimit,0) AS FLOAT)*4)/100)<100 then '100' else ((CAST(isNULL(CashLimit,0) AS FLOAT)*4)/100) END) AS paymentsAmount,isNull(paymentMode,'-') as paymentMode,isNull(noofInstallments,'-') as noofInstallments,isNull(NofdaysPmtDelay,'-') as NofdaysPmtDelay,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(LastRepmtDt,'-') as LastRepmtDt,(CASE when isDuplicate is null then '-' else case when isDuplicate=1 then 'Yes' else 'No'END END )as IsDuplicate,isNull(disputeAlert,'-') as disputeAlert,isNull(product_type,'-') as product_type,isNull(ExternalWriteOffCheck,'0') as ExternalWriteOffCheck,isNull(AECBHistMonthCnt,'-') as AECBHistMonthCnt,isNull(IsCurrent,'-') as IsCurrent,isNull(CurUtilRate,'-') as CurUtilRate,(select Description from ng_master_aecb_codes with (nolock) where code = WriteoffStat) as WriteoffStat,isNull(WriteoffStatDt,'-') as WriteoffStatDt,isNull(OverdueAmount,'-') as OverdueAmount,isNull(DPD5_Last3Months,'-') as DPD5_Last3Months,isnull((select Description from NG_MASTER_contract_type where Code=CardTypeDesc),'-')  as CardTypeDesc,isNull(CurrentBalance,'-') as CurrentBalance,isnull(DPD60Plus_Last12Months,'-') as Dpd60plus_last12month,ISNULL(DPD5_Last12Months,'-') as Dpd5_last12month,isnull(CashLimit,'-') as CashLimit from ng_rlos_cust_extexpo_CardDetails with (nolock) where CardEmbossNum='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";

			params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;	
		}
		
		if(AgreementId!=null && table.equalsIgnoreCase("ng_rlos_cust_extexpo_AccountDetails"))
		{
			//changes for PCSP-696
			//PCAS-1826 nikhil changes
			sQuery1 = "select acctNm as Fullname,'',isNull(dpd30Last6Months,'-') as dpd30Last6Months,isNull(DPD60_Last18Months,'-') as DPD60_Last18Months,isNull(aecbHistMonthCnt,'-') as aecbHistMonthCnt,AcctType as cardtype,isNull(AcctNm,'-') as cardEmbossNum,'' as cardStatus,isNull(custroletype,'-') as RoleofCustomer,isNull(StartDate,'-') as StartDate,isNull(ClosedDate,'-') as ClosedDate,'-' as DateLastUpdated,isNull(CreditLimit,'-') as totalAmount,(CASE WHEN ((CAST(isNULL(CreditLimit,0) AS FLOAT)*5)/100)<100 then '100' else ((CAST(isNULL(CreditLimit,0) AS FLOAT)*5)/100) END) as paymentsAmount,'-' as paymentMode,'' as noofInstallments,'' as NofdaysPmtDelay,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(LastRepmtDt,'-') as LastRepmtDt,(CASE when isDuplicate is null then '-' else case when isDuplicate=1 then 'Yes' else 'No'END END )as IsDuplicate,'' as disputeAlert,'OverDraft' as product_type,isNull(ExternalWriteOffCheck,'0') as ExternalWriteOffCheck,isNull(AECBHistMonthCnt,'-') as AECBHistMonthCnt,isNull(IsCurrent,'-') as IsCurrent,isNull(CurUtilRate,'-') as CurUtilRate,(select Description from ng_master_aecb_codes with (nolock) where code = WriteoffStat) as WriteoffStat,isNull(WriteoffStatDt,'-') as WriteoffStatDt,isNull(OverdueAmount,'-') as OverdueAmount,isNull(DPD5_Last3Months,'-') as DPD5_Last3Months,'-' as CardTypeDesc,'-' as CurrentBalance,ProviderNo as ProviderNo,AcctStat as Accountstatus,isnull(OutStandingBalance,'-') as OutStandingBalance,isnull(DPD60Plus_Last12Months,'-') as Dpd60plus_last12month,ISNULL(DPD5_Last12Months,'-') as Dpd5_last12month  from ng_rlos_cust_extexpo_AccountDetails with (nolock) where AcctId='"+AgreementId+"' and  Child_Wi like '"+Wi_Name+"'";
			params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;	
		}
        //added by nikhil for services details
        if(AgreementId!=null && table.equalsIgnoreCase("ng_rlos_cust_extexpo_ServicesDetails"))
        {
            //changes for PCSP-696
            //PCAS-1826 nikhil changes
            sQuery1 = "select (select FirstName+' '+case when middlename is null then '' else MiddleName end+' '+LAstName from  ng_RLOS_Customer where wi_name = '"+Wi_Name+"') as Fullname,'',isNull(dpd30_Last6Months,'-') as dpd30Last6Months,isNull(DPD60_Last18Months,'-') as DPD60_Last18Months,isNull(aecbHistMonthCnt,'-') as aecbHistMonthCnt,ServiceType as cardtype,isNull(ServiceID,'-') as cardEmbossNum,ServiceStat as cardStatus,isNull(custroletype,'-') as RoleofCustomer,isNull(SubscriptionDt,'-') as StartDate,isNull(SvcExpDt,'-') as ClosedDate,'-' as DateLastUpdated,isNull('-','-') as totalAmount,''  as paymentsAmount,''  as paymentMode,'' as noofInstallments,NofDaysPmtDelay as NofdaysPmtDelay,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(LastRepmtDt,'-') as LastRepmtDt,(CASE when isDuplicate is null then '-' else case when isDuplicate=1 then 'Yes' else 'No'END END )as IsDuplicate,'' as disputeAlert,servicetype as product_type,isNull(ExternalWriteOffCheck,'0') as ExternalWriteOffCheck,isNull(AECBHistMonthCnt,'-') as AECBHistMonthCnt,isNull(IsCurrent,'-') as IsCurrent,isNull(CurUtilRate,'-') as CurUtilRate,(select Description from ng_master_aecb_codes where code = WriteoffStat) as WriteoffStat,isNull(WriteoffStatDt,'-') as WriteoffStatDt,isNull(OverdueAmount,'-') as OverdueAmount,isNull(DPD5_Last3Months,'-') as DPD5_Last3Months,'-' as CardTypeDesc,'-' as CurrentBalance,ProviderNo as ProviderNo,ServiceStat as Accountstatus,isnull(DPD60Plus_Last12Months,'-') as Dpd60plus_last12month,ISNULL(DPD5_Last12Months,'-') as Dpd5_last12month from ng_rlos_cust_extexpo_ServicesDetails with (nolock) where ServiceId='"+AgreementId+"' and  Child_Wi like '"+Wi_Name+"'";
            params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;    
        }
	  
		WriteLog("sQuery1 "+sQuery1);
		WriteLog("sQuery2 "+sQuery2);
	  
		WriteLog("sessionId"+sessionId);
		inputXML_Common = getInputXML(sQuery_Common,params,cabinetName,sessionId);				
		inputXML_MAX = getInputXML(sQuery_Max,params,cabinetName,sessionId);				
		inputXML1 = getInputXML(sQuery1,params,cabinetName,sessionId);
		inputXML_AECB=getInputXML(sQuery_AecbHistory,params,cabinetName,sessionId);
		

		WriteLog("inputXML_MAX "+inputXML_MAX);
		
		try 
		{
			outputXML_Common = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_Common);
			outputXML1 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML1);
			outputXML_MAX= NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_MAX);
			outputXML_AECB = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_AECB);
			
		} 
		catch (NGException e) 
		{
		   e.printStackTrace();
		} 
		catch (Exception ex) 
		{
		   ex.printStackTrace();
		}  
		WriteLog("outputXML1 select Cif --> "+outputXML1);
		xmlParserData_Common=new XMLParser();
		
		xmlParserData1=new XMLParser();
		
		xmlParserData2=new XMLParser();
		xmlParserDataAecb=new XMLParser();
		xmlParserData_MAX=new XMLParser();
		WriteLog("xmlParserData_MAX select Cif --> "+outputXML_MAX);
		xmlParserData_Common.setInputXML((outputXML_Common));
		xmlParserData1.setInputXML((outputXML1));
		xmlParserData_MAX.setInputXML((outputXML_MAX));
		xmlParserDataAecb.setInputXML((outputXML_AECB));
		
		
		mainCodeValue_Common = xmlParserData_Common.getValueOf("MainCode");
		mainCodeValue1 = xmlParserData1.getValueOf("MainCode");
		mainCodeValue_MAX= xmlParserData_MAX.getValueOf("MainCode");
		mainCodeAecb = xmlParserDataAecb.getValueOf("MainCode");
		
		
		if(mainCodeValue_Common.equals("0")){
		
			subXML_Common = xmlParserData_Common.getNextValueOf("Record");
			objXmlParser_Common = new XMLParser(subXML_Common);
			TotalOutstanding = objXmlParser_Common.getValueOf("TotalOutstanding");
			TotalOverdue = objXmlParser_Common.getValueOf("TotalOverdue");
			NoOfContracts = objXmlParser_Common.getValueOf("NoOfContracts");
			Total_Exposure = objXmlParser_Common.getValueOf("Total_Exposure");
			WorstCurrentPaymentDelay = objXmlParser_Common.getValueOf("WorstCurrentPaymentDelay");
			Worst_PaymentDelay_Last24Months = objXmlParser_Common.getValueOf("Worst_PaymentDelay_Last24Months");
			Worst_Status_Last24Months = objXmlParser_Common.getValueOf("Worst_Status_Last24Months");
			Nof_Records = objXmlParser_Common.getValueOf("Nof_Records");
			NoOf_Cheque_Return_Last3 = objXmlParser_Common.getValueOf("NoOf_Cheque_Return_Last3");
			Nof_DDES_Return_Last3Months = objXmlParser_Common.getValueOf("Nof_DDES_Return_Last3Months");
			Nof_DDES_Return_Last6Months = objXmlParser_Common.getValueOf("Nof_DDES_Return_Last6Months");
			Nof_Cheque_Return_Last6 = objXmlParser_Common.getValueOf("Nof_Cheque_Return_Last6");
			DPD30_Last6Months = objXmlParser_Common.getValueOf("DPD30_Last6Months");
			Nof_Enq_Last30Days = objXmlParser_Common.getValueOf("Nof_Enq_Last30Days");
			Nof_Enq_Last90Days = objXmlParser_Common.getValueOf("Nof_Enq_Last90Days");
			Oldest_Contract_Start_Date = objXmlParser_Common.getValueOf("Oldest_Contract_Start_Date");
		}
		if(mainCodeAecb.equals("0"))
					{
						subXML_Aecb = xmlParserDataAecb.getNextValueOf("Record");
						objXmlParser_Aecb = new XMLParser(subXML_Aecb);
						AecbHistory = objXmlParser_Aecb.getValueOf("AECBHistMonthCnt");
					}		
		if(mainCodeValue1.equals("0"))
			{	
				
				//WriteLog("7");
				subXML1 = xmlParserData1.getNextValueOf("Record");
				objXmlParser = new XMLParser(subXML1);
				if(table.equalsIgnoreCase("ng_rlos_cust_extexpo_LoanDetails")){
					TotalNoOfInstalments=objXmlParser.getValueOf("TotalNoOfInstalments");	
					WriteLog("TotalNoOfInstalments"+TotalNoOfInstalments);
					RemainingInstalments= objXmlParser.getValueOf("RemainingInstalments");
					WriteLog("RemainingInstalments"+RemainingInstalments);
				
					fullname = objXmlParser.getValueOf("Fullname");
					WriteLog("fullname:"+fullname);
					typeCont = objXmlParser.getValueOf("LoanType");
					agree = objXmlParser.getValueOf("AgreementId");
					phase = objXmlParser.getValueOf("LoanStat");
					WriteLog("phase:"+phase);
					if (phase.equalsIgnoreCase("A")){
								phase="Active";
							}
							else if (phase.equalsIgnoreCase("C")){
								phase="Closed";
							}
					role = objXmlParser.getValueOf("RoleofCustomer");
					dateLastUp = objXmlParser.getValueOf("DateLastUpdated");
					OverdueAmt = objXmlParser.getValueOf("OverdueAmt");
					OutStanding_Balance= objXmlParser.getValueOf("OutstandingAmt");
					currFlag = objXmlParser.getValueOf("IsCurrent");
					if (currFlag.equalsIgnoreCase("1") || currFlag.equalsIgnoreCase("Y")){
						currFlag="Yes";
					}
					else if (currFlag.equalsIgnoreCase("0") || currFlag.equalsIgnoreCase("N")) {
						currFlag="No";
					}
					currMaxUtil = objXmlParser.getValueOf("CurUtilRate");
					dpd30Last6Months= objXmlParser.getValueOf("DPD30_Last6Months");
					dpd60Last18Months = objXmlParser.getValueOf("DPD60_Last18Months");
					AECBHistMonthCnt = objXmlParser.getValueOf("AECBHistMonthCnt");
					ExternalWriteOffCheck = objXmlParser.getValueOf("ExternalWriteOffCheck");
					StartDate = objXmlParser.getValueOf("LoanApprovedDate");
					ClosedDate = objXmlParser.getValueOf("LoanMaturityDate");
					totalAmount = objXmlParser.getValueOf("TotalAmt");
					product_type = objXmlParser.getValueOf("product_type");
					//Changes done to save extra values 
					WriteoffStat = objXmlParser.getValueOf("WriteoffStat");
					WriteoffStatDt = objXmlParser.getValueOf("WriteoffStatDt");
					paymentMode = objXmlParser.getValueOf("paymentMode");
					NofdaysPmtDelay = objXmlParser.getValueOf("NofdaysPmtDelay");
					paymentsAmount = objXmlParser.getValueOf("PaymentsAmt");
					mob = objXmlParser.getValueOf("MonthsOnBook");
					DPD5_Last3Months = objXmlParser.getValueOf("DPD5_Last3Months");
					LoanDesc = objXmlParser.getValueOf("LoanDesc");
					isDuplicate = objXmlParser.getValueOf("isDuplicate");
					WriteLog("isDuplicate select isDuplicate --> "+isDuplicate);
					//changes for PCAS-2070					
					LastRepmtDt = objXmlParser.getValueOf("LastRepmtDt");
					Dpd60plus_last12month=objXmlParser.getValueOf("Dpd60plus_last12month");
					Dpd5_last12month=objXmlParser.getValueOf("Dpd5_last12month");
				}
			    
				else if(table.equalsIgnoreCase("ng_rlos_cust_extexpo_CardDetails")){
				
					
							 fullname = objXmlParser.getValueOf("Fullname");
							 dpd30Last6Months= objXmlParser.getValueOf("dpd30Last6Months");
							dpd60Last18Months = objXmlParser.getValueOf("DPD60_Last18Months");//changes for PCSP-696
							 
							aecbHistMonthCnt = objXmlParser.getValueOf("aecbHistMonthCnt");								 
							typeCont = objXmlParser.getValueOf("CardType");
							agree = objXmlParser.getValueOf("CardEmbossNum");
							phase = objXmlParser.getValueOf("CardStatus");
							if (phase.equalsIgnoreCase("A")){
								phase="Active";
							}
							else if (phase.equalsIgnoreCase("C")){
								phase="Closed";
							}
							role = objXmlParser.getValueOf("RoleofCustomer");
							StartDate = objXmlParser.getValueOf("StartDate");
							ClosedDate = objXmlParser.getValueOf("ClosedDate");
							dateLastUp = objXmlParser.getValueOf("DateLastUpdated");
						//	noDayPayDel = objXmlParser.getValueOf("NofDaysPmtDelay"); commented because duplicate
							mob = objXmlParser.getValueOf("MonthsOnBook");
							creditLimit = objXmlParser.getValueOf("CashLimit");
							paymentsAmount = objXmlParser.getValueOf("paymentsAmount");
							paymentMode = objXmlParser.getValueOf("paymentMode");
							noofInstallments = objXmlParser.getValueOf("noofInstallments");
							LastRepmtDt = objXmlParser.getValueOf("LastRepmtDt");
							OutStanding_Balance= objXmlParser.getValueOf("CurrentBalance");
							isDuplicate = objXmlParser.getValueOf("isDuplicate");
							disputeAlert = objXmlParser.getValueOf("disputeAlert");
							NofdaysPmtDelay = objXmlParser.getValueOf("NofdaysPmtDelay");
							product_type = objXmlParser.getValueOf("product_type");
							ExternalWriteOffCheck = objXmlParser.getValueOf("ExternalWriteOffCheck");
							AECBHistMonthCnt = objXmlParser.getValueOf("AECBHistMonthCnt");
							currFlag = objXmlParser.getValueOf("IsCurrent");
					if (currFlag.equalsIgnoreCase("1") || currFlag.equalsIgnoreCase("Y")){
						currFlag="Yes";
					}
					else if (currFlag.equalsIgnoreCase("0") || currFlag.equalsIgnoreCase("N")) {
						currFlag="No";
					}
							currMaxUtil = objXmlParser.getValueOf("CurUtilRate");
							//Changes done to save extra values 
							WriteoffStat = objXmlParser.getValueOf("WriteoffStat");
							WriteoffStatDt = objXmlParser.getValueOf("WriteoffStatDt");
							OverdueAmt = objXmlParser.getValueOf("OverdueAmount");
							DPD5_Last3Months = objXmlParser.getValueOf("DPD5_Last3Months");
							LoanDesc = objXmlParser.getValueOf("CardTypeDesc");
							Dpd60plus_last12month=objXmlParser.getValueOf("Dpd60plus_last12month");
					Dpd5_last12month=objXmlParser.getValueOf("Dpd5_last12month");
											
				}
				
				else if(table.equalsIgnoreCase("ng_rlos_cust_extexpo_AccountDetails")){
					
							 fullname = objXmlParser.getValueOf("Fullname");
							 dpd30Last6Months= objXmlParser.getValueOf("dpd30Last6Months");
							dpd60Last18Months = objXmlParser.getValueOf("DPD60_Last18Months");//changes for PCSP-696
							 
							aecbHistMonthCnt = objXmlParser.getValueOf("aecbHistMonthCnt");								 
							typeCont = objXmlParser.getValueOf("CardType");
							agree = objXmlParser.getValueOf("ProviderNo");
							phase = objXmlParser.getValueOf("Accountstatus");
							if (phase.equalsIgnoreCase("A")){
								phase="Active";
							}
							else if (phase.equalsIgnoreCase("C")){
								phase="Closed";
							}
							role = objXmlParser.getValueOf("RoleofCustomer");
							StartDate = objXmlParser.getValueOf("StartDate");
							ClosedDate = objXmlParser.getValueOf("ClosedDate");
							dateLastUp = objXmlParser.getValueOf("DateLastUpdated");
						//	noDayPayDel = objXmlParser.getValueOf("NofDaysPmtDelay"); commented because duplicate
							mob = objXmlParser.getValueOf("MonthsOnBook");
							creditLimit = objXmlParser.getValueOf("totalAmount");
							paymentsAmount = objXmlParser.getValueOf("paymentsAmount");
							paymentMode = objXmlParser.getValueOf("paymentMode");
							noofInstallments = objXmlParser.getValueOf("noofInstallments");
							LastRepmtDt = objXmlParser.getValueOf("LastRepmtDt");
							OutStanding_Balance= objXmlParser.getValueOf("CurrentBalance");
							isDuplicate = objXmlParser.getValueOf("isDuplicate");
							disputeAlert = objXmlParser.getValueOf("disputeAlert");
							NofdaysPmtDelay = "-";
							product_type = objXmlParser.getValueOf("product_type");
							ExternalWriteOffCheck = objXmlParser.getValueOf("ExternalWriteOffCheck");
							AECBHistMonthCnt = objXmlParser.getValueOf("AECBHistMonthCnt");
							currFlag = objXmlParser.getValueOf("IsCurrent");
					if (currFlag.equalsIgnoreCase("1") || currFlag.equalsIgnoreCase("Y")){
						currFlag="Yes";
					}
					else if (currFlag.equalsIgnoreCase("0") || currFlag.equalsIgnoreCase("N")) {
						currFlag="No";
					}
							currMaxUtil = objXmlParser.getValueOf("CurUtilRate");
							//Changes done to save extra values 
							WriteoffStat = objXmlParser.getValueOf("WriteoffStat");
							WriteoffStatDt = objXmlParser.getValueOf("WriteoffStatDt");
							OverdueAmt = objXmlParser.getValueOf("OverdueAmount");
							DPD5_Last3Months = objXmlParser.getValueOf("DPD5_Last3Months");
							LoanDesc =typeCont;
							//Worst_Status_Last24Months="-";
							OutStanding_Balance=objXmlParser.getValueOf("OutStandingBalance");
							Dpd60plus_last12month=objXmlParser.getValueOf("Dpd60plus_last12month");
					Dpd5_last12month=objXmlParser.getValueOf("Dpd5_last12month");
				}
				 //added by nikhil for services
                else if(table.equalsIgnoreCase("ng_rlos_cust_extexpo_ServicesDetails")){
                    
                             fullname = objXmlParser.getValueOf("Fullname");
                             dpd30Last6Months= objXmlParser.getValueOf("dpd30Last6Months");
                            dpd60Last18Months = objXmlParser.getValueOf("DPD60_Last18Months");//changes for PCSP-696
                             
                            aecbHistMonthCnt = objXmlParser.getValueOf("aecbHistMonthCnt");                                 
                            typeCont = objXmlParser.getValueOf("CardType");
                            agree = objXmlParser.getValueOf("ProviderNo");
                            phase = objXmlParser.getValueOf("Accountstatus");
                            if (phase.equalsIgnoreCase("A")){
                                phase="Active";
                            }
                            else if (phase.equalsIgnoreCase("C")){
                                phase="Closed";
                            }
                            role = objXmlParser.getValueOf("RoleofCustomer");
                            StartDate = objXmlParser.getValueOf("StartDate");
                            ClosedDate = objXmlParser.getValueOf("ClosedDate");
                            dateLastUp = objXmlParser.getValueOf("DateLastUpdated");
                        //    noDayPayDel = objXmlParser.getValueOf("NofDaysPmtDelay"); commented because duplicate
                            mob = objXmlParser.getValueOf("MonthsOnBook");
                            creditLimit = objXmlParser.getValueOf("totalAmount");
                            paymentsAmount = objXmlParser.getValueOf("paymentsAmount");
                            paymentMode = objXmlParser.getValueOf("paymentMode");
                            noofInstallments = objXmlParser.getValueOf("noofInstallments");
                            LastRepmtDt = objXmlParser.getValueOf("LastRepmtDt");
                            OutStanding_Balance= objXmlParser.getValueOf("CurrentBalance");
                            isDuplicate = objXmlParser.getValueOf("isDuplicate");
                            disputeAlert = objXmlParser.getValueOf("disputeAlert");
                            NofdaysPmtDelay = objXmlParser.getValueOf("NofdaysPmtDelay");
                            product_type = objXmlParser.getValueOf("product_type");
                            ExternalWriteOffCheck = objXmlParser.getValueOf("ExternalWriteOffCheck");
                            AECBHistMonthCnt = objXmlParser.getValueOf("AECBHistMonthCnt");
                            currFlag = objXmlParser.getValueOf("IsCurrent");
                    if (currFlag.equalsIgnoreCase("1") || currFlag.equalsIgnoreCase("Y")){
                        currFlag="Yes";
                    }
                    else if (currFlag.equalsIgnoreCase("0") || currFlag.equalsIgnoreCase("N")) {
                        currFlag="No";
                    }
                            currMaxUtil = objXmlParser.getValueOf("CurUtilRate");
                            //Changes done to save extra values 
                            WriteoffStat = objXmlParser.getValueOf("WriteoffStat");
                            WriteoffStatDt = objXmlParser.getValueOf("WriteoffStatDt");
                            OverdueAmt = objXmlParser.getValueOf("OverdueAmount");
                            DPD5_Last3Months = objXmlParser.getValueOf("DPD5_Last3Months");
                            LoanDesc = objXmlParser.getValueOf("CardTypeDesc");
							Dpd60plus_last12month=objXmlParser.getValueOf("Dpd60plus_last12month");
					Dpd5_last12month=objXmlParser.getValueOf("Dpd5_last12month");
                
                }
				currFlag="-";//added by nikhil for FLV Liability CR		
			}
			//added by nikhil forPCSP-672/692
		if(mainCodeValue_MAX.equals("0"))
		{
		subXML_MAX = xmlParserData_MAX.getNextValueOf("Record");
		objXmlParser_MAX = new XMLParser(subXML_MAX);
		Maxaecbhist = objXmlParser_MAX.getValueOf("AECBHistMonthCnt");
		MaxavgUtil=objXmlParser_MAX.getValueOf("CurUtilRate");
		//updated for PCSP-695
		dpd30Last6Months=objXmlParser_MAX.getValueOf("dpd30Last6Months");
		dpd60Last18Months=objXmlParser_MAX.getValueOf("DPD60_Last18Months");
		ExternalWriteOffCheck=objXmlParser_MAX.getValueOf("ExternalWriteOffCheck");
		DPD5_Last3Months=objXmlParser_MAX.getValueOf("DPD5_Last3Months");
		Dpd60plus_last12month=objXmlParser_MAX.getValueOf("DPD60Plus_Last12Months");
		Dpd5_last12month=objXmlParser_MAX.getValueOf("DPD5_Last12Months");
		}
									
	}
		
		catch (Exception ex) 
		{
		   ex.printStackTrace();
		} 			
%>
<%!
	public String getInputXML(String query,String params,String cabinetName,String sessionId)
	{
		return "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + query + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
	}	
%>	
<html>

<head>
<script language="javascript" src="/webdesktop/custom/CustomJSP/workdesk.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/en_us/css/RLOS/RLOS_CSS.css" type="text/css" />
<link href="frames.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/custom/jquery/jquery-1.12.3.js"></script>	
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.js"></script>
	<script type="text/javascript">
	
	var WD_UID = '<%=WD_UID%>';
	
	function CallAjax(jspName,params)
{
	var response="";
	try{			
			var xmlReq = null;
			if(window.XMLHttpRequest) xmlReq = new XMLHttpRequest();
			else if(window.ActiveXObject) xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
			if(xmlReq==null) return; // Failed to create the request
				xmlReq.onreadystatechange = function()
			{
				switch(xmlReq.readyState)
				{
				case 0: 
					break;
				case 1: 
					break;
				case 2: 
					break;
				case 3: 
					break;
				case 4: 
					if (xmlReq.status==200) 
					{
					response=xmlReq.responseText;
					}
					else if (xmlReq.status==404)
						alert("External liability : URL doesn't exist!");
					else 
						alert("External liability : Status is "+xmlReq.status);	
					break;
				default:
					//alert(xmlReq.status);
					break;
				}
			}
			
			xmlReq.open('POST',jspName,false);				
			xmlReq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
			xmlReq.send(params);
			return response;
	   }
	
    catch(e)
    {
		alert("External liability : "+e.message);
		//return false;
    }
}

	function getRecordDistrubutionValues(){
	var params="";
	var result="";
	var squery = "<%=Liability_type%>";
	var pname = "external";
	var wparams="Child_Wi=="+"<%=Wi_Name%>";
	//squery = "select ContractType,Contract_Role_Type,TotalNo,DataProvidersNo,RequestNo,DeclinedNo,RejectedNo,NotTakenUpNo,ActiveNo,ClosedNo from NG_RLOS_CUSTEXPOSE_RecordDestribution with (nolock) where Child_Wi = '<%=Wi_Name%>'";
	params="Query="+squery+"&wparams="+wparams+"&pname="+pname+"&WD_UID="+WD_UID;
			
			result=CallAjax("CustomSelect.jsp",params);
					
			if(result.indexOf("FAIL")!=-1)
			{
				alert("Some error Encountered. Please try after some time");
				return;
			}
			else if(result.indexOf("FailedException")!=-1)
			{
				alert("Some Exception Occurred. Please try after some time");
				return;
			}
			else
			{
				if(result.length>6)
					var result_check = result.substring(0,6);
				if(result_check!='NODATA'){
					var result_arr = result.split("~");
					var dhtml = "";
					var rowhtml ="";
					for(var i=0;i<result_arr.length-1;i++){
						var colVal = result_arr[i].split('#');
						rowhtml +="<tr height='25'>";
						for(var j=0;j<colVal.length-1;j++){
						 rowhtml += "<td>"+colVal[j]+"</td>";
						}
						rowhtml +="</tr>";
						//dhtml+= "<tr height='25'>"+rowhtml+"</tr>";
					}
					$("#mytbody").append(rowhtml);
					//document.getElementById('mytbody').innerHTML = rowhtml;
				}
			}
	}
	</script>
</head>

<body onload = "getRecordDistrubutionValues()">
<form method="post" action="http://www.cs.tut.fi/cgi-bin/run/~jkorpela/echo.cgi">
<fieldset>
  <legend><b>Customer Level Summary (External)</b></legend>
  <table cellspacing="10" cellpadding="5">
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Full Name</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Outstanding Balance</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Overdue</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Default Contracts</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Exposure</b></label></td>
	</tr>
	<tr> 
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=fullname%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=TotalOutstanding%>" disabled></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=TotalOverdue%>" disabled></td>
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=NoOfContracts%>" disabled></td> 
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Total_Exposure%>" disabled></td> 
	</tr>
	<tr></tr>
	<tr>

		<td style="padding:0 15px 0 15px;"><label><b>Oldest Contract Start Date</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Current Payment Delay</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Payment Delay last in 24 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst status in last 24 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of records excluding B01</b></label></td><!-- Nikhil CR FLV Liability Changes-->
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;" value="<%=Oldest_Contract_Start_Date%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=WorstCurrentPaymentDelay%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Worst_PaymentDelay_Last24Months%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Worst_Status_Last24Months%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Nof_Records%>" disabled></td>
	</tr>
	<tr></tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Currently Current in All Products</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Current Max Utilization</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No. of Cheque Return (3 Months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No. of DDS return (3 months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No. of Cheque return (6 months)</b></label></td>
		<!--<td style="padding:0 15px 0 15px;"><label><b>DDS return in last 6 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Type</b></label></td>-->
		
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=currFlag%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=MaxavgUtil%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=NoOf_Cheque_Return_Last3%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Nof_DDES_Return_Last3Months%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Nof_Cheque_Return_Last6%>" disabled></td>
		<!--<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  /></td>
		<td style="padding:0 15px 0 15px;"> <select name="type" style="width: 170px;"><option value="volvo">--Select--</option></select></td>-->
		
	</tr>
	<tr></tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>No. of DDS return (6 months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 30+ (6 months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 60+ in last 18 months(Max of all contracts)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No. of enquiries in last 90 days</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No. of enquiries in last 30 days</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Nof_DDES_Return_Last6Months%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=dpd30Last6Months%>" disabled ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=dpd60Last18Months%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Nof_Enq_Last90Days%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Nof_Enq_Last30Days%>" disabled></td>
	</tr>
	<tr></tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>External charge-off / Write-off amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>AECB History (Total Months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Delinquent in the last 3 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 5+ in last 12 Months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 60+ in Last 12 Months</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=ExternalWriteOffCheck%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=AecbHistory%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=DPD5_Last3Months%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Dpd5_last12month%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=Dpd60plus_last12month%>" disabled></td>
		
	</tr>
	<tr></tr>
	
  
</table>  

<TABLE BORDER="2"    WIDTH="100%"   CELLPADDING="4" CELLSPACING="3">
   <thead>
   <TR>
      <TH bgcolor="e01414" style="color:#ffffff">Contract Type</TH>
      <TH bgcolor="e01414" style="color:#ffffff">External roll type</TH>
	  <TH bgcolor="e01414" style="color:#ffffff">Total no.</TH>
	  <TH bgcolor="e01414" style="color:#ffffff">Data Providers No.</TH>
      <TH bgcolor="e01414" style="color:#ffffff">Request No.</TH>
	  <TH bgcolor="e01414" style="color:#ffffff">Declined No.</TH>
	  <TH bgcolor="e01414" style="color:#ffffff">Reject No.</TH>
	  <TH bgcolor="e01414" style="color:#ffffff">Not Taken Up No.</TH>
      <TH bgcolor="e01414" style="color:#ffffff">Active No.</TH>
	  <TH bgcolor="e01414" style="color:#ffffff">Closed No.</TH>
	  
   </TR>
   </thead>
   <tbody height="200px" overflow="scroll" id ='mytbody'>
   <!--<TR height="25">
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
	  <TD></TD>
   </TR>
   <TR height="25">
      <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
   </TR>
   <TR height="25">
      <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
   </TR>
   <TR height="25">
      <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
      <TD></TD>
	  <TD></TD>
	  <TD></TD>
   </TR>-->
  </tbody>
</TABLE>

  
  </fieldset>
  
  
  
<fieldset>
  <legend><b>Individual Product Level (External)</b></legend>
  
  <table cellspacing="10" cellpadding="5">
  
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Type of Contract</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Provider Number</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Phase</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Role of the Customer</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Start Date</b></label></td>
	</tr>
	<tr>
	
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value='<%=typeCont%>' disabled></td>	
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=agree%>" disabled></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=phase%>" disabled></td>
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=role%>" disabled></td> 
	    <td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;" value="<%=StartDate%>" disabled></td> 
	</tr>
	<tr></tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Close date</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Date Last Updated</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Outstanding Balance</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Payments amount (EMI)</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;" value="<%=ClosedDate%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;" value="<%=dateLastUp%>" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=OutStanding_Balance%> " disabled ></td>
	 	<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=totalAmount%> " disabled ></td>
	<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=paymentsAmount%> " disabled ></td>
	</tr>
	<tr></tr>
	<tr>
	 <td style="padding:0 15px 0 15px;"><label><b>Method of Payment</b></label></td>
	 <td style="padding:0 15px 0 15px;"><label><b>Total no of Installments</b></label></td>
	 	<td style="padding:0 15px 0 15px;"><label><b>No of Remaining Installments</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Status</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst status date</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=paymentMode%> " disabled ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=TotalNoOfInstalments%> " disabled ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=RemainingInstalments%> " disabled ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=WriteoffStat%> " disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;" value="<%=WriteoffStatDt%> " disabled ></td>
	</tr>
	<tr></tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Credit Limit</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Overdue amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of days payment delay</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Status Alert (24 Months) - Customer level</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>MOB (Months on Book)</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value="<%=creditLimit%> "></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=OverdueAmt%> " disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=NofdaysPmtDelay%> " disabled ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="-"  disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=mob%> " disabled ></td>
		<!--<td style="padding:0 15px 0 15px;"><input type="month" name="bdaymonth"></td>-->
	</tr>
	<tr></tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Last Repayment Month & Year (from historical data)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Duplicate Flag</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Info disputed Alert</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of days of payment delay-Services</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Type of Product</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=LastRepmtDt%> " disabled ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=isDuplicate%> " disabled ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=DisputeAlert%> "  disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=NofDaysPaymentDelay%> " disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  value="<%=LoanDesc%>" disabled></td>
		
	</tr>
	<tr></tr>
	</fieldset>
	</form>
	</body>
  </html>
  
  
  




