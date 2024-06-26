<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : Internal 
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
	
<html>

<head>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/en_us/css/RLOS/RLOS_CSS.css" type="text/css" />
	<script src="${pageContext.request.contextPath}/custom/js/json2.js"></script>	
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-1.12.3.js"></script>
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/custom/jquery/jquery-ui.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.min.js"></script>

<script language="javascript" src="/webdesktop/custom/CustomJSP/workdesk.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var upperTable = document.getElementById('table1');
	var lowerTable = document.getElementById('table2');
	executeLogic(upperTable);
	executeLogic(lowerTable);
});
function executeLogic(upperTable){
for(var i=1;i<upperTable.rows.length;i=i+2){
		for(var j=0;j<upperTable.rows[i].cells.length;j++){
		//alert("isNAN: "+upperTable.rows[i].cells[j].id);
		//alert("isNAN: "+upperTable.rows[i].cells[j].name);
		//alert("Hi"+document.getElementById("AgreementId").value);
		if (document.getElementById("AgreementId").value !=	upperTable.rows[i].cells[j].childNodes[0].value){
			var txt=upperTable.rows[i].cells[j].childNodes[0].value;
			  var re1='(^[1-9]\\d*(\\.\\d+)?$)';    
			  var p = new RegExp(re1,["i"]);
			  var m = p.exec(txt);
			if(m != null) {
			var numericField = upperTable.rows[i].cells[j].childNodes[0].value;
				if(numericField.indexOf('.')>-1){
				var newVal=handleFloatAmount(numericField);
				upperTable.rows[i].cells[j].childNodes[0].value = newVal;
				}
				else if(numericField.length>3){
				var newVal=handleIntAmount(numericField);
					upperTable.rows[i].cells[j].childNodes[0].value = newVal;
				
				}
			}
		}
		}
	}

}
function handleFloatAmount(value){
var beforeDecimal = value.split('.')[0];
var newVal;
if(beforeDecimal.length>3){
var commaflag= 1;
var flag = false;
for(var i=beforeDecimal.length-1;i>0;i--){
	if(commaflag==3){
	if(!flag){
	newVal = beforeDecimal.substring(0,i)+','+value.substring(i,value.length);
	flag=true;
	}
	else{
	newVal = newVal.substring(0,i)+','+newVal.substring(i,value.length);
	}
	commaflag=1;
	}
	else{
	commaflag++;
	}
}
return newVal;
}
else{
return value;
}

}

function handleIntAmount(value){
var newVal;
var commaflag= 1;
var flag = false;
for(var i=value.length-1;i>0;i--){
	if(commaflag==3){
	if(!flag){
	newVal = value.substring(0,i)+','+value.substring(i,value.length);
	flag=true;
	}
	else{
	newVal = newVal.substring(0,i)+','+newVal.substring(i,value.length);
	}
	//alert("newVal: "+newVal);
	commaflag=0;
	}
	else{
	commaflag++;
	}
}
return newVal;
}
</script>
</head>
<%
	

	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String AgreementId = "";
	String flag = "";
	String Wi_Name =request.getParameter("Wi_Name");
	String table=request.getParameter("table");
	String agreement_col="";
	if(table.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
	AgreementId = request.getParameter("AgreementId");	
	flag="loan";
	agreement_col="AgreementId";
	}
	else if(table.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
	AgreementId = request.getParameter("AgreementId");
	flag="card";
	agreement_col="CardEmbossNum";
	}
	else if(table.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
	AgreementId = request.getParameter("AgreementId");
	flag="account";
	agreement_col="Acctid";
	}
	String params="";
	String Query_CIF ="select top 1 cifid from "+table+" with (nolock) where child_wi='"+Wi_Name+"' and "+agreement_col+"='"+AgreementId+"'";
	WriteLog("appServerType jsp: result: "+appServerType);
	WriteLog("wrapperIP jsp: result: "+wrapperIP);	
	WriteLog("wrapperPort jsp: result: "+wrapperPort);
	WriteLog("sessionId jsp: result: "+sessionId);
	WriteLog("wrapperPort jsp: result: "+wrapperPort);
	WriteLog("cabinetName jsp: result: "+cabinetName);
	WriteLog("Wi_Name jsp: result: "+Wi_Name);
	WriteLog("AgreementId jsp: result: "+AgreementId);
	//WriteLog("CardEmbossNum jsp: result: "+CardEmbossNum);
	
	XMLParser objXmlParser =null;
	XMLParser xmlParserData =null;
	XMLParser xmlParserData1=null;
	//++below code added by nikhil for PCAS-1298 CR
	XMLParser xmlParserDataAecb=null;
	XMLParser xmlParserDataSum=null;
	XMLParser xmlParserDataprod=null;
	XMLParser xmlParserDatacc=null;
	XMLParser xmlParserData2=null;
	XMLParser xmlParserData_Common=null;
	XMLParser objXmlParser_Common =null;
	//++below code added by nikhil for PCAS-1298 CR
	XMLParser objXmlParser_Aecb =null;
	XMLParser objXmlParser_product =null;
	XMLParser objXmlParser_Sum =null;
	XMLParser objXmlParser_cc =null;
	String inputXML_Common=null;
	//++below code added by nikhil for PCAS-1298 CR
	String inputXML_AECB=null;
	String inputXML_Sum=null;
	String inputXML1=null;
	String outputXML1=null;
	String inputXML2=null;
	String outputXML2=null;
	String inputXML_prod=null;
	String inputXML_cc=null;
	String outputXML_Common=null;
	//++below code added by nikhil for PCAS-1298 CR
	String outputXML_AECB=null;
	String outputXML_prod=null;
	String outputXML_cc=null;
	String outputXML_Sum=null;
	String TotalNoOfInstalments = "-";
	String RemainingInstalments= "-";
	String Loan_Start_Date= "-";
	String Loan_disbursal_date= "-";
	String PaymentMode= "-";
	String TotalAmount="-";
	String LastUpdateDate="-";
	String PaymentsAmt="-";
	String LastRepmtDt = "-";
	String fullname = "-";
	String totOutBal = "-";
	String totOverdue = "-";
	String noDefCont = "-";
	String totExposure = "-";
	String worCurrPayDelay = "-";
	String worsPayDel24 = "-";
	String worStatlast24 = "-";
	String noLiab = "-";
	String Internal_WriteOff_Check="";
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
	String worStat = "-";
	String Bscore = "-";
	String worStatDate = "-";
	String noDayPayDel = "0";
	String mob = "-";
	//++below code added by nikhil for PCAS-1298 CR
	String MobAecb = "- - -";
	String Sum_Total_Out="0";
	String Sum_Total_OverDue="0";
	String Sum_Total_No="0";
	String currFlag = "-";
	String lastRepMonYr = "-";
	String dpd6month = "-";
	String dpd18month = "-";
	String cardsB = "-";
	String schemeCardProd = "-";
	String propVal= "-";
	String marketCode = "-";
	String internalCharge ="-";
	
	String ExpiryDate= "-";
	 String CreditLimit= "-";
	 String OutstandingAmt= "-";
     String OverdueAmt= "-";
	 String CurrMaxUtil= "-";
	 String CurrentBalance= "-";
	  String loanmaturitydate= "-";
	 String WorstStatus= "-";
	  String WorstStatusdate= "-";
	  String Marketing_Code="-";
	 
	 String TotalOutstanding="-";
	String TotalOverdue="-";
	String NoOfContracts="-";
	String Total_Exposure="-";
	String WorstCurrentPaymentDelay="-";
	String Worst_PaymentDelay_Last24Months="-";
	String Worst_Status_Last24Months="-";
	String Nof_Records="-";
	String NoOf_Cheque_Return_Last3="-";
	String Nof_DDES_Return_Last3Months="-";
	String NoOf_Cheque_Return_Last6Months="-";
	String Nof_DDES_Return_Last6Months="-";
	String MarketingCode="-";
	String PropertyValue="-";
	//++below code added by nikhil for PCAS-1298 CR
	String AecbHistory="-";
	String DPD_60_in_last_12_months="-";
	String liability_availed="-";
	String Lending_date="-";
	
	
	  String subXML1="";
	  String subXML2="";
	  String subXML_Common="";
	  //++below code added by nikhil for PCAS-1298 CR
	  String subXML_Aecb="";
	  String subXML_prod="";
	  String subXML_Sum="";
	  String subXML_cc="";
	String mainCodeValue1 ="";
	String mainCodeValue_Common ="";
	String mainCodeValue2 ="";
	//++below code added by nikhil for PCAS-1298 CR
	String mainCodeAecb = "";
	String mainCodeSum = "";
	String mainCodeprod="";
	String mainCodecc=" ";
	String Subproduct=" ";
	String AppCategory=" ";
	
	     //public static final String commonParse(String parseXml,String tagName1,String subTagName1);
		//public static final LinkedHashMap<Integer, String> getTagDataParent(String parseXml,String tagName2,String subTagName2)

	try{
		String sQuery1 = null,sQuery2 = null,sQuery_Common =null,sQuery_AecbHistory=null,sQuery_Sum=null,sQuery_prod=null,sQuery_cc= null;
		
		sQuery_Common = "select isNull(Total_Exposure,'-') as Total_Exposure,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(Nof_Records,'-') as Nof_Records, isNull(TotalOutstanding,'-') as TotalOutstanding,isNull(TotalOverdue,'-') as TotalOverdue,isNull(NoOfContracts,'-')as NoOfContracts,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-90 and GETDATE() and returntype='ICCS' and  Child_Wi='"+Wi_Name+"' and Cifid= ("+Query_CIF+") ) as no_of_chq_ret_3months,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-180 and GETDATE() and returntype='ICCS' and  Child_Wi='"+Wi_Name+"' and Cifid= ("+Query_CIF+")) as no_of_chq_ret_6months,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-90 and GETDATE() and returntype='DDS' and  Child_Wi='"+Wi_Name+"' and Cifid= ("+Query_CIF+")) as no_of_DDS_ret_3months,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-180 and GETDATE() and returntype='DDS' and  Child_Wi='"+Wi_Name+"' and Cifid= ("+Query_CIF+")) as no_of_DDS_ret_6months,AggrExposure as  AggrExposure,isnull((select B_Score from ng_master_CIF_Bscore  where CIF_No=("+Query_CIF+") ),'-') as CardsBscore from  NG_RLOS_CUSTEXPOSE_Derived der with (nolock) INNER JOIN ng_RLOS_Liability_New liab with (nolock) ON (der.Child_Wi=liab.wi_name) where Child_Wi='"+Wi_Name+"' and Request_Type = 'CollectionsSummary'";
		//++below code added by nikhil for PCAS-1298 CR
		sQuery_AecbHistory = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"+ Wi_Name + "' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+ Wi_Name + "') as ext_expoo";
		//below code by sagarika -12/08
	    sQuery_prod="select a.Sub_Product as subproduct,b.applicationCategory as App from NG_CC_EXTTABLE a inner join  NG_RLOS_GR_CompanyDetails b on a.CC_Wi_Name=b.comp_winame where a.CC_Wi_Name='"+Wi_Name+"' and b.applicationCategory is not null";
		//Changed by shivang of PCASP-2960
        sQuery_cc="select top 1 * from ((select case when ("+Query_CIF+") in (select CIF_ID from ng_RLOS_Customer where wi_name='"+Wi_Name+"' union select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame='"+Wi_Name+"' and applicantCategory='Business') then (case when (convert(datetime,CardsApplcnRecvdDate))>=dateadd(month, -6,getdate()) then CardsApplcnRecvdDate else CardsApplcnRecvdDate  END) else 'NA' end as date_val,case when ("+Query_CIF+") in (select CIF_ID from ng_RLOS_Customer where wi_name='"+Wi_Name+"' union select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame='"+Wi_Name+"' and applicantCategory='Business') then (case when (convert(datetime,CardsApplcnRecvdDate))>=dateadd(month, -6,getdate()) then 'Yes' else 'No' END)else 'NA' end as product_date from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where CardType in ('CREDIT CARDS') and Child_Wi='"+Wi_Name+"' union select case when ("+Query_CIF+") in (select CIF_ID from ng_RLOS_Customer where wi_name='"+Wi_Name+"' union select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame='"+Wi_Name+"' and applicantCategory='Business') then (case when (convert(datetime,Loan_Start_Date))>=dateadd(month, -6,getdate()) then Loan_Start_Date else Loan_Start_Date END) else 'NA' end as date_val,case when ("+Query_CIF+") in (select CIF_ID from ng_RLOS_Customer where wi_name='"+Wi_Name+"' union select CompanyCIF from NG_RLOS_GR_CompanyDetails where comp_winame='"+Wi_Name+"' and applicantCategory='Business') then (case when (convert(datetime,Loan_Start_Date))>=dateadd(month, -6,getdate()) then 'Yes' else 'No' END) else 'NA' end as product_date   from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi='"+Wi_Name+"' and (LoanType in ('MURABAHA','AL','AUTO','CC','IM','Instant Money' ,'ML','COLLATERAL MORTGAGE EI','RFL','IJARAH','HOME IN ONE','HOME','MORTGAGE') or SchemeCardProd like '%rakfin%'))) as ext where date_val is not null order by date_val desc";
		sQuery_Sum = "select Sum(Total_liability) as Total_liability,CONVERT(DECIMAL(10,2),cast(sum(cast(TotalOustsatnding as float)) as float)) as TotalOustsatnding,cast(sum(cast(TotalOverdue as real)) as numeric(36,2))  as TotalOverdue from ( select COUNT(child_wi) as Total_liability,SUM(CAST(ISNULL(OutstandingAmt,'0') AS FLOAT)) AS TotalOustsatnding,SUM(CAST(ISNULL(OverdueAmt,'0') AS FLOAT)) AS TotalOverdue  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Child_wi  = '"	+ Wi_Name+ "'  and CardStatus='A' and Cifid= ("+Query_CIF+") union all  select COUNT(child_wi) as Total_liability,SUM(CAST(ISNULL(TotalOutstandingAmt,'0') AS FLOAT)) AS TotalOustsatnding,SUM(CAST(ISNULL(OverdueAmt,'0') AS FLOAT)) AS TotalOverdue  from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_wi  = '"+ Wi_Name + "' and (LoanStat='A' or LoanStat='ACTIVE')  and Cifid= ("+Query_CIF+") union all  select COUNT(child_wi) as Total_liability,SUM(CAST('0' AS FLOAT)) AS TotalOustsatnding,SUM(CAST('0' AS FLOAT)) AS TotalOverdue  from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_wi  = '"+ Wi_Name + "' and AcctStat='ACTIVE' and ODType!='' and Cifid= ("+Query_CIF+")) as Int_expo";
		
		if(AgreementId!=null && flag.equalsIgnoreCase("loan"))
		{
		 /*sQuery1 = "select  (select FirstName+' '+MiddleName+' '+LAstName from  ng_RLOS_Customer where Wi_Name = '"+Wi_Name+"') as Fullname,LoanStat,TotalNoOfInstalments,RemainingInstalments,Loan_Start_Date,Loan_disbursal_date,PaymentMode,TotalAmount,LastUpdateDate,PaymentsAmt from ng_RLOS_CUSTEXPOSE_LoanDetails where AgreementId='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";*/
		 
		sQuery1 = "select  case when liability_type='Corporate_CIF' then (select case when COUNT(ApplicantName)>0 then  ISNULL(ApplicantName,'') else '' end   from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by ApplicantName) else (select case when COUNT(FirstName)>0 then  ( select FirstName+' '+isnull(middlename,'')+' '+LAstName from ng_RLOS_Customer where Wi_Name ='"+Wi_Name+"' and CIF_ID= ("+Query_CIF+"))else (select case when COUNT(AuthSignName)>0 then (select AuthSignName  from ng_rlos_gr_AuthSignDetails where sign_winame ='"+Wi_Name+"' and AuthSignCIFNo=("+Query_CIF+")) else 'Indirect CIF' end   from ng_rlos_gr_AuthSignDetails where sign_winame ='"+Wi_Name+"' and AuthSignCIFNo=("+Query_CIF+")  ) end   from ng_RLOS_Customer where Wi_Name ='"+Wi_Name+"' and CIF_ID= ("+Query_CIF+")   ) end as Fullname,isNull(TotalOutstandingAmt,'-') as TotalOutstandingAmt,isNull(TotalExposure,'-') as TotalExposure,isNull(NoofDefaultContracts,'-') as NoofDefaultContracts,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(WorstPaymentDelayin24Months,'-') as WorstPaymentDelayin24Months,isNull(WorstStatusinlast24Months,'-') as WorstStatusinlast24Months,isNull(LoanType,'-') as LoanType,isNull(AgreementId,'-') as AgreementId,isNull(LoanStat,'-') as LoanStat,'Primary' as RoleofCustomer,(case when loanType='LEVERAGED LOAN' then isNull(LoanApprovedDate,'-') else case when loanType in ('PERFORMANCE GUARANTEES FIXED','BID / TENDER BONDS FIXED','L/C IMPORT SECURED','ACCEPTANCE L/C IMPORT','ADVANCE PAYMENT GUARANTEES FIXED','BID / TENDER BONDS AUTO RENEWABLE','RETENTION GUARANTEES AUTO RENEWABLE','SHORT TERM LOAN AGAINST INVOICE','LOCAL CHEQUE DISCOUNTING','LABOR GUARANTEE','PERFORMANCE GUARANTEES AUTO RENEWABLE') then LimitSactionDate else isNull(Loan_Start_Date,'-') end end) as Loan_Start_Date,SYSDATETIME() as DateLastUpdated,isNull(OutstandingAmt,'-') as OutstandingAmt,isNull(ProductAmt,'-') as ProductAmt,isNull(NextInstallmentAmt,'-') as NextInstallmentAmt,isNull(TotalNoOfInstalments,'-') as TotalNoOfInstalments,isNull(RemainingInstalments,'-') as RemainingInstalments,isNull(OverdueAmt,'-') as OverdueAmt,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(CurrentlyCurrentFlg,'-') as CurrentlyCurrentFlg,isNull(CurrMaxUtil,'-') as CurrMaxUtil,isNull(DPD_30_in_last_6_months,'-') as DPD_30_in_last_6_months,isNull(DPD_60_in_last_18_months,'-') as DPD_60_in_last_18_months,isNull(SchemeCardProd,'-') as SchemeCardProd,isNull(LoanApprovedDate,'-') as Loan_disbursal_date,isNull(loanmaturitydate,'-') as loanmaturitydate,isNull(lastupdatedate,'-') as lastupdatedate,isNull(PaymentsAmt,'-') as PaymentsAmt,isNull(WriteoffStat,'-') as WriteoffStat,isNull(WriteoffStatdt,'-') as WriteoffStatdt,isNull(NofDaysPmtDelay,'0') as NofDaysPmtDelay,isNull(PropertyValue,'-') as PropertyValue,isNull(MarketingCode,'-') as MarketingCode,isNull(LastRepmtDt,'-') as LastRepmtDt,isNull(PaymentMode,'-') as PaymentMode,(case when LoanType='LEVERAGED LOAN' then  isnull(LoanDisbursementAmt,'-') else case when loanType in ('PERFORMANCE GUARANTEES FIXED','BID / TENDER BONDS FIXED','L/C IMPORT SECURED','ACCEPTANCE L/C IMPORT','ADVANCE PAYMENT GUARANTEES FIXED','BID / TENDER BONDS AUTO RENEWABLE','RETENTION GUARANTEES AUTO RENEWABLE','SHORT TERM LOAN AGAINST INVOICE','LOCAL CHEQUE DISCOUNTING','LABOR GUARANTEE','PERFORMANCE GUARANTEES AUTO RENEWABLE') then CumulativeDebitAmt  else isnull(ProductAmt,'-') END end) as LoanAmt,isnull(DPD_60_in_last_12_months,'0') as DPD_60_in_last_12_months,isnull(Internal_WriteOff_Check,'-') as Internal_WriteOff_Check from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where AgreementId='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";
		 
		params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;
		}
		
		if(AgreementId!=null && flag.equalsIgnoreCase("card"))
		{
			//sQuery1 changed by Shivang
		   //sQuery1 = "select ExpiryDate,CreditLimit,OutstandingAmt,OverdueAmt,CurrMaxUtil,CurrentBalance from ng_RLOS_CUSTEXPOSE_CardDetails where CardEmbossNum='"+AgreementId+"' and  //Child_Wi='"+Wi_Name+"'"; 
		  // sQuery1 = "select  case when liability_type='Corporate_CIF' then (select case when COUNT(ApplicantName)>0 then  ISNULL(ApplicantName,'') else '' end   from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by ApplicantName) else (select FirstName+' '+case when middlename is null then '' else MiddleName end+' '+LAstName from  ng_RLOS_Customer with (nolock) where Wi_Name = '"+Wi_Name+"' and ) end as Fullname,isNull(OutstandingAmt,'-') as OutstandingAmt ,isNull(TotalExposure,'-') as TotalExposure,isNull(NoofDefaultContracts,'-') as NoofDefaultContracts,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(WorstPaymentDelayin24Months,'-') as WorstPaymentDelayin24Months ,isNull(WorstStatusinlast24Months,'-') as WorstStatusinlast24Months,isNull(CardType,'-') as CardType,isNull(CardEmbossNum,'-') as CardEmbossNum,isNull(CardStatus,'-') as CardStatus,'Primary' as RoleofCustomer,SYSDATETIME() as DateLastUpdated,isNull(CreditLimit,'-') as CreditLimit,isNull(OverdueAmt,'-') as OverdueAmt,isNull(NofDaysPmtDelay,'0') as NofDaysPmtDelay,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(card_expiry_date,'-') as loanmaturitydate,isNull(CurrMaxUtil,'-') as CurrMaxUtil,isNull(DPD_30_in_last_6_months,'-') as DPD_30_in_last_6_months,isNull(DPD_60_in_last_18_months,'-') as DPD_60_in_last_18_months,isNull(SchemeCardProd,'-') as SchemeCardProd,(CASE WHEN ((CAST(isNULL(CreditLimit,0) AS FLOAT)*3)/100)>100 then '100' else ((CAST(isNULL(CreditLimit,0) AS FLOAT)*3)/100) END) as PaymentsAmount,isNull(WriteoffStat,'-') as WriteoffStat,isNull(WriteoffStatdt,'-') as WriteoffStatdt,isNull(LastRepmtDt,'-') as LastRepmtDt,isNull(PaymentMode,'-') as PaymentMode,isNull(c.ExpiryDate,'-') as ExpiryDate,isNull(CardsApplcnRecvdDate,'-') as CardsApplcnRecvdDate,xyz.RemainingEMI as RemainingEMI,xyz.InstalmentPeriod as TotalNoOfInstalments,isnull(c.DPD_60_in_last_12_months,'0') as DPD_60_in_last_12_months from ng_RLOS_CUSTEXPOSE_CardDetails c with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails xyz with (nolock) on c.CardEmbossNum=xyz.CardNumber and (c.Child_Wi=xyz.Wi_Name or c.Child_Wi=xyz.Child_Wi) where CardEmbossNum='"+AgreementId+"' and (c.Wi_Name = '"+Wi_Name+"' or c.Child_Wi = '"+Wi_Name+"')";
		  //Deepak Query corrected by deepak
		  //modified by nikhil to add write -off amount
		   //sQuery1 = "select (select FirstName+' '+isNull(MiddleName,'')+' '+LAstName from  ng_RLOS_Customer with (nolock) where Wi_Name = '"+Wi_Name+"') as Fullname,isNull(OutstandingAmt,'-') as OutstandingAmt ,isNull(TotalExposure,'-') as TotalExposure,isNull(NoofDefaultContracts,'-') as NoofDefaultContracts,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(WorstPaymentDelayin24Months,'-') as WorstPaymentDelayin24Months ,isNull(WorstStatusinlast24Months,'-') as WorstStatusinlast24Months,isNull(CardType,'-') as CardType,isNull(CardEmbossNum,'-') as CardEmbossNum,isNull(CardStatus,'-') as CardStatus,'Primary' as RoleofCustomer,SYSDATETIME() as DateLastUpdated,isNull(CreditLimit,'-') as CreditLimit,isNull(card_expiry_date,'-') as loanmaturitydate,isNull(OverdueAmt,'-') as OverdueAmt,isNull(NofDaysPmtDelay,'0') as NofDaysPmtDelay,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(CurrentlyCurrentFlg,'-') as CurrentlyCurrentFlg,isNull(CurrMaxUtil,'-') as CurrMaxUtil,isNull(DPD_30_in_last_6_months,'-') as DPD_30_in_last_6_months,isNull(DPD_60_in_last_18_months,'-') as DPD_60_in_last_18_months,isNull(SchemeCardProd,'-') as SchemeCardProd,case when (SchemeCardProd = 'LOC PREFERRED' or SchemeCardProd = 'LOC STANDARD') then isnull(xyz.MonthlyAmount,0) ELSE CASE WHEN ((CAST(isNULL(CreditLimit,0) AS FLOAT)*3)/100)<100 then '100' else ((CAST(isNULL(CreditLimit,0) AS FLOAT)*3)/100) END END as PaymentsAmount ,isNull(WriteoffStat,'-') as WriteoffStat,isNull(WriteoffStatdt,'-') as WriteoffStatdt,isNull(LastRepmtDt,'-') as LastRepmtDt,isNull(PaymentMode,'-') as PaymentMode,isNull(c.ExpiryDate,'-') as ExpiryDate,isNull(CardsApplcnRecvdDate,'-') as CardsApplcnRecvdDate,isnull(xyz.RemainingEMI,'-') as RemainingEMI,isnull(xyz.InstalmentPeriod,'-') as TotalNoOfInstalments,isnull(c.Internal_WriteOff_Check,'-' ) as Internal_WriteOff_Check,isnull(c.DPD_60_in_last_12_months,'0') as DPD_60_in_last_12_months  from ng_RLOS_CUSTEXPOSE_CardDetails c with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails xyz with (nolock) on c.CardEmbossNum=xyz.CardNumber and (c.Child_Wi=xyz.Wi_Name or c.Child_Wi=xyz.Child_Wi) where CardEmbossNum='"+AgreementId+"' and (c.Wi_Name = '' or c.Child_Wi = '"+Wi_Name+"')";
		  //Deepak Query corrected for IM cases.
		   sQuery1 = "select  case when liability_type='Corporate_CIF' then (select case when COUNT(ApplicantName)>0 then  ISNULL(ApplicantName,'') else '' end   from NG_RLOS_GR_CompanyDetails where comp_winame ='"+Wi_Name+"' and applicantCategory='Business' group by ApplicantName) else (select case when COUNT(FirstName)>0 then  ( select FirstName+' '+isnull(middlename,'')+' '+LAstName from ng_RLOS_Customer where Wi_Name ='"+Wi_Name+"' and CIF_ID= ("+Query_CIF+"))else (select case when COUNT(AuthSignName)>0 then (select AuthSignName  from ng_rlos_gr_AuthSignDetails where sign_winame ='"+Wi_Name+"' and AuthSignCIFNo=("+Query_CIF+")) else 'Indirect CIF' end   from ng_rlos_gr_AuthSignDetails where sign_winame ='"+Wi_Name+"' and AuthSignCIFNo=("+Query_CIF+")  ) end   from ng_RLOS_Customer where Wi_Name ='"+Wi_Name+"' and CIF_ID= ("+Query_CIF+")   ) end as Fullname,isNull(OutstandingAmt,'-') as OutstandingAmt ,isNull(TotalExposure,'-') as TotalExposure,isNull(NoofDefaultContracts,'-') as NoofDefaultContracts,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(WorstPaymentDelayin24Months,'-') as WorstPaymentDelayin24Months ,isNull(WorstStatusinlast24Months,'-') as WorstStatusinlast24Months,isNull(CardType,'-') as CardType,isNull(CardEmbossNum,'-') as CardEmbossNum,isNull(CardStatus,'-') as CardStatus,'Primary' as RoleofCustomer,SYSDATETIME() as DateLastUpdated,isNull(CreditLimit,'-') as CreditLimit,isnull(MarketingCode,'-') as Marketing_Code,isNull(OverdueAmt,'-') as OverdueAmt,isNull(NofDaysPmtDelay,'0') as NofDaysPmtDelay,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(card_expiry_date,'-') as loanmaturitydate,isNull(CurrentlyCurrentFlg,'-') as CurrentlyCurrentFlg,isNull(CurrMaxUtil,'-') as CurrMaxUtil,isNull(DPD_30_in_last_6_months,'-') as DPD_30_in_last_6_months,isNull(DPD_60_in_last_18_months,'-') as DPD_60_in_last_18_months,isNull(SchemeCardProd,'-') as SchemeCardProd,case when (SchemeCardProd = 'LOC PREFERRED' or SchemeCardProd = 'LOC STANDARD') then (select sum(cast(MonthlyAmount as float)) from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi ='"+Wi_Name+"' and replace(CardNumber,'I','')=CardEmbossNum) ELSE CASE WHEN ((CAST(isNULL(CreditLimit,0) AS FLOAT)*4)/100)<100 then '100' else ((CAST(isNULL(CreditLimit,0) AS FLOAT)*4)/100) END END as PaymentsAmount ,isNull(WriteoffStat,'-') as WriteoffStat,isNull(WriteoffStatdt,'-') as WriteoffStatdt,isNull(LastRepmtDt,'-') as LastRepmtDt,isNull(PaymentMode,'-') as PaymentMode,isNull(c.ExpiryDate,'-') as ExpiryDate,isNull(CardsApplcnRecvdDate,'-') as CardsApplcnRecvdDate,isnull(xyz.RemainingEMI,'-') as RemainingEMI,isnull(xyz.InstalmentPeriod,'-') as TotalNoOfInstalments,isnull(c.Internal_WriteOff_Check,'-' ) as Internal_WriteOff_Check,isnull(c.DPD_60_in_last_12_months,'0') as DPD_60_in_last_12_months  from ng_RLOS_CUSTEXPOSE_CardDetails c with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails xyz with (nolock) on c.CardEmbossNum=xyz.CardNumber and (c.Child_Wi=xyz.Wi_Name or c.Child_Wi=xyz.Child_Wi) where CardEmbossNum='"+AgreementId+"' and (c.Wi_Name = '' or c.Child_Wi = '"+Wi_Name+"')";
		  params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;	
		} 
		//added by nikhil for OD
		if(AgreementId!=null && flag.equalsIgnoreCase("account"))
		{
				sQuery1 = "select AcctNm as Fullname,isNull(CurOutstandingAmount,'-') as TotalOutstandingAmt,isNull(WorstDelay24Months,'-') as WorstPaymentDelayin24Months,isNull(WorstStatus24Months,'-') as WorstStatusinlast24Months,isNull(ODType,'-') as LoanType,isNull(AcctId,'-') as AgreementId,isNull(AcctStat,'-') as LoanStat,'Primary' as RoleofCustomer,isNull(LimitSactionDate,'-') as Loan_Start_Date,SYSDATETIME() as DateLastUpdated,isNull(ClearBalanceAmount,'-') as OutstandingAmt,isNull(OverdueAmount,'-') as OverdueAmt,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(IsCurrent,'-') as CurrentlyCurrentFlg,isNull(CurUtilRate,'-') as CurrMaxUtil,isNull(DPD30_Last6Months,'-') as DPD_30_in_last_6_months,isNull(DPD60_Last18Months,'-') as DPD_60_in_last_18_months,isNull(LimitExpiryDate,'-') as loanmaturitydate,'-' as WriteoffStat,isNull(WriteoffStatDt,'-') as WriteoffStatdt,CASE WHEN ((CAST(isNULL(SanctionLimit,0) AS FLOAT)*5)/100)<100 then '100' else ((CAST(isNULL(SanctionLimit,0) AS FLOAT)*5)/100) END as PaymentsAmt,isNull(NofDaysPmtDelay,'0') as NofDaysPmtDelay,isNull(LastRepmtDt,'-') as LastRepmtDt,'-' as PaymentMode,'-' as LoanAmt,isnull(SanctionLimit,'-') as CreditLimit,'-' as DPD_60_in_last_12_months from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";
		 
		params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;
		}
		
		WriteLog("sQuery formed is --> "+sQuery1);
		WriteLog("sQuery_Common formed is --> "+sQuery_Common);
		
				inputXML_Common = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_Common + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				//++below code added by nikhil for PCAS-1298 CR
				inputXML_AECB= "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_AecbHistory + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				
				inputXML_Sum= "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_Sum + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				inputXML_prod= "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_prod + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";//sagarika
				inputXML_cc= "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_cc + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
		        inputXML1 = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery1 + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";

				/*inputXML2 = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery1 + "</Query><Params>"+ params +"</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";*/
				
			    try 
				{
					outputXML_Common = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_Common);
					outputXML1 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML1);
					//++below code added by nikhil for PCAS-1298 CR
					outputXML_AECB = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_AECB);
					outputXML_Sum = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_Sum);
				//	outputXML2 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML2);
			       outputXML_prod= NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_prod);
				   outputXML_cc= NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_cc);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}  
				WriteLog("outputXML_Common select Cif --> "+outputXML_Common);
				WriteLog("outputXML1 select Cif --> "+outputXML1);
				WriteLog("outputXML_AECB select Cif --> "+outputXML_AECB);
				WriteLog("outputXML_Sum select Cif --> "+outputXML_Sum);
				//WriteLog("outputXML1 select Cif --> "+outputXML1);
				//out.println(outputXML1);
			//	WriteLog("outputXML2 select Cif --> "+outputXML2);
				//out.println(outputXML2);
				xmlParserData_Common=new XMLParser();
				WriteLog("2");
				xmlParserData1=new XMLParser();
				WriteLog("3");
				xmlParserData2=new XMLParser();
				WriteLog("4");
				//++below code added by nikhil for PCAS-1298 CR
				xmlParserDataAecb=new XMLParser();
				xmlParserDataSum=new XMLParser();
				xmlParserDataprod=new XMLParser();
				xmlParserDatacc=new XMLParser();
				xmlParserData_Common.setInputXML((outputXML_Common));
				xmlParserData1.setInputXML((outputXML1));
				//++below code added by nikhil for PCAS-1298 CR
				xmlParserDataAecb.setInputXML((outputXML_AECB));
				xmlParserDataSum.setInputXML((outputXML_Sum));
				xmlParserDataprod.setInputXML((outputXML_prod));
				xmlParserDatacc.setInputXML((outputXML_cc));
				WriteLog("5");
				//xmlParserData2.setInputXML((outputXML2));
				mainCodeValue_Common = xmlParserData_Common.getValueOf("MainCode");
				int no_retrieved = Integer.parseInt(xmlParserData_Common.getValueOf("TotalRetrieved"));
				mainCodeValue1 = xmlParserData1.getValueOf("MainCode");
			//	mainCodeValue2 = xmlParserData2.getValueOf("MainCode");
				mainCodeAecb = xmlParserDataAecb.getValueOf("MainCode");
				mainCodeSum = xmlParserDataSum.getValueOf("MainCode");
				mainCodeprod = xmlParserDataprod.getValueOf("MainCode");
				mainCodecc = xmlParserDatacc.getValueOf("MainCode");
			WriteLog("6");
				
				if(mainCodeValue_Common.equals("0") && no_retrieved>0){
				WriteLog("6--");
					subXML_Common = xmlParserData_Common.getNextValueOf("Record");
					objXmlParser_Common = new XMLParser(subXML_Common);
					TotalOutstanding = objXmlParser_Common.getValueOf("TotalOutstanding");
					TotalOverdue = objXmlParser_Common.getValueOf("TotalOverdue");
					NoOfContracts = objXmlParser_Common.getValueOf("NoOfContracts");
					Total_Exposure = objXmlParser_Common.getValueOf("AggrExposure");
					WorstCurrentPaymentDelay = objXmlParser_Common.getValueOf("WorstCurrentPaymentDelay");
					Nof_Records = objXmlParser_Common.getValueOf("Nof_Records");
					NoOf_Cheque_Return_Last3 = objXmlParser_Common.getValueOf("no_of_chq_ret_3months");
					Nof_DDES_Return_Last3Months = objXmlParser_Common.getValueOf("no_of_DDS_ret_3months");
					NoOf_Cheque_Return_Last6Months = objXmlParser_Common.getValueOf("no_of_chq_ret_6months");
					Nof_DDES_Return_Last6Months = objXmlParser_Common.getValueOf("no_of_DDS_ret_6months");
					Bscore = objXmlParser_Common.getValueOf("CardsBscore");
					
					
					}
					//++below code added by nikhil for PCAS-1298 CR
					if(mainCodeAecb.equals("0"))
					{
						subXML_Aecb = xmlParserDataAecb.getNextValueOf("Record");
						objXmlParser_Aecb = new XMLParser(subXML_Aecb);
						AecbHistory = objXmlParser_Aecb.getValueOf("AECBHistMonthCnt");
					}
					if(mainCodecc.equals("0"))
					{
						subXML_cc = xmlParserDatacc.getNextValueOf("Record");
						objXmlParser_cc = new XMLParser(subXML_cc);
				}
					if (mainCodeprod.equals("0"))
					{
						subXML_prod=xmlParserDataprod.getNextValueOf("Record");
					  objXmlParser_product = new XMLParser(subXML_prod);
						Subproduct = objXmlParser_product.getValueOf("subproduct");
						AppCategory=objXmlParser_product.getValueOf("App");
						if(("SE".equalsIgnoreCase(Subproduct) && "BAU".equalsIgnoreCase(AppCategory))|| "BTC".equalsIgnoreCase(Subproduct))
						{
						liability_availed = objXmlParser_cc.getValueOf("product_date");
						Lending_date=objXmlParser_cc.getValueOf("date_val");
	                    if(liability_availed.equalsIgnoreCase("no"))
						{
							Lending_date="-";
						}
					}
					}
						//Write.log("ss"+Subproduct);
						//Subproduct = objXmlParser_prod.getValueOf("subproduct");
						//AppCategory=objXmlParser_prod.getValueOf("App");
						/*if(("SE".equalsIgnoreCase(Subproduct) && "BAU".equalsIgnoreCase(AppCategory))|| "BTC".equalsIgnoreCase(Subproduct))
						{
						
	                    if(liability_availed.equalsIgnoreCase("no"))
						{
							Lending_date="-";
						}
					}
					
					}*/
	
					if(mainCodeSum.equals("0"))
					{
						subXML_Sum = xmlParserDataSum.getNextValueOf("Record");
						objXmlParser_Sum = new XMLParser(subXML_Sum);
						Sum_Total_No = objXmlParser_Sum.getValueOf("Total_liability");
						Sum_Total_Out = objXmlParser_Sum.getValueOf("TotalOustsatnding");
					/*	try
						{
						Double Value=Math.round((Double.parseDouble(Sum_Total_Out))*100.0)/100.0;
						Sum_Total_Out=Double.toString(Value);
						}
						catch(Exception ex)
						{
						Sum_Total_Out = objXmlParser_Sum.getValueOf("TotalOustsatnding");
						}*/
						Sum_Total_OverDue = objXmlParser_Sum.getValueOf("TotalOverdue");
					}
					if(mainCodeValue1.equals("0"))
					{	
								WriteLog("7");
								subXML1 = xmlParserData1.getNextValueOf("Record");
								objXmlParser = new XMLParser(subXML1);
								if(flag.equalsIgnoreCase("loan")){
								WriteLog("8");
								TotalNoOfInstalments=objXmlParser.getValueOf("TotalNoOfInstalments");	
								WriteLog("TotalNoOfInstalments"+TotalNoOfInstalments);
								RemainingInstalments= objXmlParser.getValueOf("RemainingInstalments");
								WriteLog("RemainingInstalments"+RemainingInstalments);
								Loan_Start_Date= objXmlParser.getValueOf("Loan_Start_Date");
								WriteLog("Loan_Start_Date"+Loan_Start_Date);
								Loan_disbursal_date= objXmlParser.getValueOf("Loan_disbursal_date");
								WriteLog("Loan_disbursal_date"+Loan_disbursal_date);
								PaymentMode= objXmlParser.getValueOf("PaymentMode");
								WriteLog("PaymentMode"+PaymentMode);
								//TotalAmount= objXmlParser.getValueOf("TotalLoanAmount");
								WriteLog("TotalAmount"+TotalAmount);
								LastUpdateDate= objXmlParser.getValueOf("lastupdatedate");
								WriteLog("LastUpdateDate"+LastUpdateDate);
								PaymentsAmt= objXmlParser.getValueOf("PaymentsAmt");
								WriteLog("PaymentsAmt"+PaymentsAmt);
								fullname = objXmlParser.getValueOf("Fullname");
								WriteLog("fullname is fikll"+fullname);
								
								totOutBal = objXmlParser.getValueOf("TotalOutstandingAmt");
								totExposure = objXmlParser.getValueOf("TotalExposure");
								noDefCont = objXmlParser.getValueOf("NoofDefaultContracts");
								worCurrPayDelay = objXmlParser.getValueOf("WorstCurrentPaymentDelay");
								worsPayDel24 = objXmlParser.getValueOf("WorstPaymentDelayin24Months");
								worStatlast24 = objXmlParser.getValueOf("WorstStatusinlast24Months");
								typeCont = objXmlParser.getValueOf("LoanType");
								agree = objXmlParser.getValueOf("AgreementId");
								phase = objXmlParser.getValueOf("LoanStat");
								if (phase.equalsIgnoreCase("A")){
												phase="Active";
											}
											else if (phase.equalsIgnoreCase("C")){
												phase="Closed";
											}
								role = objXmlParser.getValueOf("RoleofCustomer");
								dateLastUp = objXmlParser.getValueOf("DateLastUpdated");
								 //o= objXmlParser.getValueOf("OutstandingAmt");
								totAmt = objXmlParser.getValueOf("ProductAmt");
								emi = objXmlParser.getValueOf("NextInstallmentAmt");
								OverdueAmt = objXmlParser.getValueOf("OverdueAmt");
								mob = objXmlParser.getValueOf("MonthsOnBook");
								currFlag = objXmlParser.getValueOf("CurrentlyCurrentFlg");
								if (currFlag.equalsIgnoreCase("1") || currFlag.equalsIgnoreCase("Y") ){
								currFlag="Yes";
								}
								else  if (currFlag.equalsIgnoreCase("0") || currFlag.equalsIgnoreCase("N")) {
								currFlag="No";
								}
								CurrMaxUtil = objXmlParser.getValueOf("CurrMaxUtil");
								dpd6month = objXmlParser.getValueOf("DPD_30_in_last_6_months");
								dpd18month = objXmlParser.getValueOf("DPD_60_in_last_18_months");
								schemeCardProd = objXmlParser.getValueOf("SchemeCardProd");
								WriteLog("schemeCardProd is fikll"+schemeCardProd);
								loanmaturitydate= objXmlParser.getValueOf("loanmaturitydate");
								WorstStatus = objXmlParser.getValueOf("WriteoffStat");
								WorstStatusdate= objXmlParser.getValueOf("WriteoffStatdt");
								noDayPayDel = objXmlParser.getValueOf("NofDaysPmtDelay");
								PropertyValue= objXmlParser.getValueOf("PropertyValue");
								LastRepmtDt= objXmlParser.getValueOf("LastRepmtDt");
								MarketingCode = objXmlParser.getValueOf("MarketingCode");
								TotalAmount=objXmlParser.getValueOf("LoanAmt");	
								DPD_60_in_last_12_months=objXmlParser.getValueOf("DPD_60_in_last_12_months");							
								Internal_WriteOff_Check=objXmlParser.getValueOf("Internal_WriteOff_Check");
								}
								
								else if(flag.equalsIgnoreCase("card")){
								WriteLog("9");
								loanmaturitydate= objXmlParser.getValueOf("loanmaturitydate");
								ExpiryDate=objXmlParser.getValueOf("ExpiryDate");
								 CreditLimit= objXmlParser.getValueOf("CreditLimit");
							     OutstandingAmt= objXmlParser.getValueOf("OutstandingAmt");
								 OverdueAmt= objXmlParser.getValueOf("OverdueAmt");
								 CurrMaxUtil= objXmlParser.getValueOf("CurrMaxUtil");
								 CurrentBalance= objXmlParser.getValueOf("CurrentBalance");
								 fullname = objXmlParser.getValueOf("Fullname");
								 totOutBal = objXmlParser.getValueOf("OutstandingAmt");
								 totExposure = objXmlParser.getValueOf("TotalExposure");
								 Total_Exposure = objXmlParser_Common.getValueOf("AggrExposure");
								 noDefCont = objXmlParser.getValueOf("NoofDefaultContracts");
								 worCurrPayDelay = objXmlParser.getValueOf("WorstCurrentPaymentDelay");
								 worsPayDel24 = objXmlParser.getValueOf("WorstPaymentDelayin24Months");
								worStatlast24 = objXmlParser.getValueOf("WorstStatusinlast24Months");
								typeCont = objXmlParser.getValueOf("CardType");
								agree = objXmlParser.getValueOf("CardEmbossNum");
								Marketing_Code=  objXmlParser.getValueOf("Marketing_Code");
								phase = objXmlParser.getValueOf("CardStatus");
								if (phase.equalsIgnoreCase("A")){
												phase="Active";
											}
											else if (phase.equalsIgnoreCase("C")){
												phase="Closed";
											}
								role = objXmlParser.getValueOf("RoleofCustomer");
								dateLastUp = objXmlParser.getValueOf("DateLastUpdated");
								noDayPayDel = objXmlParser.getValueOf("NofDaysPmtDelay");
								mob = objXmlParser.getValueOf("MonthsOnBook");
								dpd6month = objXmlParser.getValueOf("DPD_30_in_last_6_months");
								dpd18month = objXmlParser.getValueOf("DPD_60_in_last_18_months");
								schemeCardProd = objXmlParser.getValueOf("SchemeCardProd");
								WriteLog("schemeCardProd is fikll"+schemeCardProd);
								LastRepmtDt= objXmlParser.getValueOf("LastRepmtDt");
								PaymentsAmt= objXmlParser.getValueOf("PaymentsAmount");
								WorstStatus = objXmlParser.getValueOf("WriteoffStat");
								WorstStatusdate= objXmlParser.getValueOf("WriteoffStatdt");
								Loan_Start_Date= objXmlParser.getValueOf("CardsApplcnRecvdDate");
								RemainingInstalments= objXmlParser.getValueOf("RemainingEMI");
								TotalNoOfInstalments=objXmlParser.getValueOf("TotalNoOfInstalments");
								TotalAmount="-";
								Internal_WriteOff_Check=objXmlParser.getValueOf("Internal_WriteOff_Check");
								currFlag = objXmlParser.getValueOf("CurrentlyCurrentFlg");
								if (currFlag.equalsIgnoreCase("1") || currFlag.equalsIgnoreCase("Y") ){
								currFlag="Yes";
								}
								else  if (currFlag.equalsIgnoreCase("0") || currFlag.equalsIgnoreCase("N")) {
								currFlag="No";
								}
								DPD_60_in_last_12_months=objXmlParser.getValueOf("DPD_60_in_last_12_months");
								}
								//new for OD
								else if(flag.equalsIgnoreCase("account")){
								
								
								Loan_Start_Date= objXmlParser.getValueOf("Loan_Start_Date");
								//loanmaturitydate= objXmlParser.getValueOf("loanmaturitydate");//sagarika
								WriteLog("Loan_Start_Date"+Loan_Start_Date);
								PaymentMode= objXmlParser.getValueOf("PaymentMode");
								WriteLog("PaymentMode"+PaymentMode);
								fullname = objXmlParser.getValueOf("Fullname");
								WriteLog("fullname is fikll"+fullname);
								//PaymentsAmt= objXmlParser.getValueOf("PaymentsAmt");//sagarika
								totOutBal = objXmlParser.getValueOf("OutstandingAmt");
								//worCurrPayDelay = objXmlParser.getValueOf("WorstCurrentPaymentDelay");
								worsPayDel24 = objXmlParser.getValueOf("WorstPaymentDelayin24Months");
								worStatlast24 = objXmlParser.getValueOf("WorstStatusinlast24Months");
								typeCont = objXmlParser.getValueOf("LoanType");
								agree = objXmlParser.getValueOf("AgreementId");
								phase = objXmlParser.getValueOf("LoanStat");
								if (phase.equalsIgnoreCase("A")){
												phase="Active";
											}
											else if (phase.equalsIgnoreCase("C")){
												phase="Closed";
											}
								role = objXmlParser.getValueOf("RoleofCustomer");
								dateLastUp = objXmlParser.getValueOf("DateLastUpdated");
								OverdueAmt = objXmlParser.getValueOf("OverdueAmt");
								mob = objXmlParser.getValueOf("MonthsOnBook");
								currFlag = objXmlParser.getValueOf("CurrentlyCurrentFlg");
								if (currFlag.equalsIgnoreCase("1") || currFlag.equalsIgnoreCase("Y") ){
								currFlag="Yes";
								}
								else  if (currFlag.equalsIgnoreCase("0") || currFlag.equalsIgnoreCase("N")) {
								currFlag="No";
								}
								CurrMaxUtil = objXmlParser.getValueOf("CurrMaxUtil");
								dpd6month = objXmlParser.getValueOf("DPD_30_in_last_6_months");
								dpd18month = objXmlParser.getValueOf("DPD_60_in_last_18_months");
								
								WorstStatus = objXmlParser.getValueOf("WriteoffStat");
								WorstStatusdate= objXmlParser.getValueOf("WriteoffStatdt");
								noDayPayDel = objXmlParser.getValueOf("NofDaysPmtDelay");
								
								LastRepmtDt= objXmlParser.getValueOf("LastRepmtDt");
							
								TotalAmount=objXmlParser.getValueOf("LoanAmt");	
								DPD_60_in_last_12_months=objXmlParser.getValueOf("DPD_60_in_last_12_months");
								CreditLimit= objXmlParser.getValueOf("CreditLimit");
								PaymentsAmt= objXmlParser.getValueOf("PaymentsAmt");
								}
								//++below code added by nikhil for PCAS-1298 CR
								MobAecb=mob+" - "+AecbHistory;//sagarika 
					}
					
	/*				if(mainCodeValue2.equals("0"))
					{	
						subXML2 = xmlParserData2.getNextValueOf("Record");
								
							     objXmlParser = new XMLParser(subXML2);
								 
							     ExpiryDate=objXmlParser.getValueOf("ExpiryDate");
								 CreditLimit= objXmlParser.getValueOf("CreditLimit");
							     OutstandingAmt= objXmlParser.getValueOf("OutstandingAmt");
								 OverdueAmt= objXmlParser.getValueOf("OverdueAmt");
								 CurrMaxUtil= objXmlParser.getValueOf("CurrMaxUtil");
								 CurrentBalance= objXmlParser.getValueOf("CurrentBalance");
					} 
	*/				
	}
                catch (Exception ex) 
				{
				   ex.printStackTrace();
				} 			
%>
 
<form method="post" action="http://www.cs.tut.fi/cgi-bin/run/~jkorpela/echo.cgi">
<fieldset>
  <legend><b>Customer Level Summary (Internal)</b></legend>
  
  <table cellspacing="10" cellpadding="5" id = 'table1'>
	<tr>

		<td style="padding:0 15px 0 15px;"><label><b>Full Name</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Outstanding Balance</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Overdue</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Default Contracts</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Exposure</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name='full_name' style="color:black;"  disabled value='<%=fullname%> ' ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value="<%=Sum_Total_Out%>" ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value='<%=Sum_Total_OverDue%>' ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=noDefCont%> ></td> 
	    <!--<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=NoOfContracts%> ></td> -->
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%="-"%> ></td> 
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Current Payment Delay (Bucket)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Payment Delay in 24 months (Bucket)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst status in last 24 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Number of Liabilities</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Cheque Return(3 Months)</b></label></td>
	</tr>
	<tr>

		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=WorstCurrentPaymentDelay%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=worsPayDel24%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=worStatlast24%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" disabled value='<%=Sum_Total_No%>'></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=NoOf_Cheque_Return_Last3%> ></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>No of DDS Return(3 Months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Cheque Return(6 Months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of DDS Return(6 Months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Product/liability availed within 6 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Lending product availed date</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value=<%=Nof_DDES_Return_Last3Months%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value=<%=NoOf_Cheque_Return_Last6Months%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value=<%=Nof_DDES_Return_Last6Months%> ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value=<%=liability_availed%> ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value=<%=Lending_date%> ></td> 
	</tr>
	
</table>  
</fieldset>
  
  
  
<fieldset>
  <legend><b>Individual Product Level (Internal)</b></legend>
  
  <table cellspacing="10" cellpadding="5" id = 'table2'>
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Type of Contract</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Agreement No/CRN No</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Phase</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Role of the Customer</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Start Date</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" disabled value='<%=typeCont%>' ></td>	
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" id="AgreementId"  disabled value=<%=agree%> ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" disabled value=<%=phase%> ></td>
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" disabled value=<%=role%> ></td> 
	    <td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;"   disabled value=<%=Loan_Start_Date%> ></td> 
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Close date</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Date Last Updated</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Outstanding Balance</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Payments amount (EMI)</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;"    disabled value=<%=loanmaturitydate%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;"  disabled value=<%=dateLastUp%>  ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=totOutBal%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=TotalAmount%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value=<%=PaymentsAmt%>  ></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Method of Payment</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total no of Installments</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Remaining Installments</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Status</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst status date</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=PaymentMode%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=TotalNoOfInstalments%> ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=RemainingInstalments%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=WorstStatus%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday" style="color:black;"  disabled value=<%=WorstStatusdate%> ></td>
	</tr>
	
	<tr>

		<td style="padding:0 15px 0 15px;"><label><b>Credit limit / IM amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Overdue amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of days payment delay</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>MOB - AECB History</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Last Repayment Month & Year (from historical data)</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=CreditLimit%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=OverdueAmt%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value=<%=noDayPayDel%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value="<%=MobAecb%>" ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bdaymonth"   style="color:black;"  disabled value="<%=LastRepmtDt%> "></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Currently Current</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Current  Utilization</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 30+ in last 6 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 60+ in last 18 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Cards B Score</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value="<%=currFlag%>" ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=CurrMaxUtil%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=dpd6month%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"  disabled value=<%=dpd18month%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" disabled value=<%=Bscore%>></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Scheme/Card Product</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Property Value</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Disbursal Date/Approved date</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Marketing code</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Card Expiry Date</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;"   disabled value='<%=schemeCardProd%> ' ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" disabled  value='<%=PropertyValue%> '></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday"  disabled value=<%=Loan_disbursal_date%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" value ='<%=Marketing_Code%>' disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" disabled name="bday" id = 'bogus' style="color:black;"  disabled value=<%=ExpiryDate%> ></td>
	</tr>
	
	<tr>
         <td style="padding:0 15px 0 15px;"><label><b>Internal charge-off / Write-off amount</b></label></td>
		  <td style="padding:0 15px 0 15px;"><label><b>DPD 60+ in Last 12 Month</b></label></td>
		  </tr>
	<tr>
	     <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" value ='<%=Internal_WriteOff_Check%>' disabled></td>
		  <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" style="color:black;" value ='<%=DPD_60_in_last_12_months%>' disabled></td>
		 
	</tr>
	
	
<TABLE BORDER="2"    WIDTH="100%"   CELLPADDING="4" CELLSPACING="3">
   <thead>
   <TR>
      <TH bgcolor="e01414" style="color:#ffffff">Part settlement Date</TH>
      <TH bgcolor="e01414" style="color:#ffffff">Part settlement Amount</TH>
	  <TH bgcolor="e01414" style="color:#ffffff">Part settlement Reason</TH>
   </TR>
   </thead>
   <tbody height="200px" overflow="scroll">
   <TR height="25">
      <TD></TD>
      <TD></TD>
	  <TD></TD>
   </TR>
   <TR height="25">
      <TD></TD>
      <TD></TD>
	  <TD></TD>
   </TR>
   <TR height="25">
      <TD></TD>
      <TD></TD>
	  <TD></TD>
   </TR>
   <TR height="25">
      <TD></TD>
      <TD></TD>
	  <TD></TD>
   </TR>
</TABLE>

</table>  
  
  </fieldset>
  </html>
  

