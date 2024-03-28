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
<%@ page import ="ISPack.CPISDocumentTxn"%>
<%@ page import ="ISPack.ISUtil.*"%>
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
<f:view>
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
	if(table.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
	AgreementId = request.getParameter("AgreementId");	
	flag="loan";
	}
	else if(table.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
	AgreementId = request.getParameter("AgreementId");
	flag="card";
	}
	String params="";

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
	XMLParser xmlParserData2=null;
	XMLParser xmlParserData_Common=null;
	XMLParser objXmlParser_Common =null;
	String inputXML_Common=null;
	String inputXML1=null;
	String outputXML1=null;
	String inputXML2=null;
	String outputXML2=null;
	String outputXML_Common=null;
	
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
	String worStatDate = "-";
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
	
	String ExpiryDate= "-";
	 String CreditLimit= "-";
	 String OutstandingAmt= "-";
     String OverdueAmt= "-";
	 String CurrMaxUtil= "-";
	 String CurrentBalance= "-";
	  String loanmaturitydate= "-";
	 String WorstStatus= "-";
	  String WorstStatusdate= "-";
	 
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
	
	
	  String subXML1="";
	  String subXML2="";
	  String subXML_Common="";
	  
	String mainCodeValue1 ="";
	String mainCodeValue_Common ="";
	String mainCodeValue2 ="";
	
	     //public static final String commonParse(String parseXml,String tagName1,String subTagName1);
		//public static final LinkedHashMap<Integer, String> getTagDataParent(String parseXml,String tagName2,String subTagName2)

	try{
		String sQuery1 = null,sQuery2 = null,sQuery_Common =null;
		
		sQuery_Common = "select isNull(Total_Exposure,'-') as Total_Exposure,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(Nof_Records,'-') as Nof_Records, isNull(TotalOutstanding,'-') as TotalOutstanding,isNull(TotalOverdue,'-') as TotalOverdue,isNull(NoOfContracts,'-')as NoOfContracts,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-90 and GETDATE() and returntype='ICCS' and  Child_Wi='"+Wi_Name+"') as no_of_chq_ret_3months,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-180 and GETDATE() and returntype='ICCS' and  Child_Wi='"+Wi_Name+"') as no_of_chq_ret_6months,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-90 and GETDATE() and returntype='DDS' and  Child_Wi='"+Wi_Name+"') as no_of_DDS_ret_3months,(SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) between GETDATE()-180 and GETDATE() and returntype='DDS' and  Child_Wi='"+Wi_Name+"') as no_of_DDS_ret_6months  from  NG_RLOS_CUSTEXPOSE_Derived with (nolock) where Child_Wi='"+Wi_Name+"' and Request_Type = 'CollectionsSummary'";
		if(AgreementId!=null && flag.equalsIgnoreCase("loan"))
		{
		 /*sQuery1 = "select  (select FirstName+' '+MiddleName+' '+LAstName from  ng_RLOS_Customer where Wi_Name = '"+Wi_Name+"') as Fullname,LoanStat,TotalNoOfInstalments,RemainingInstalments,Loan_Start_Date,Loan_disbursal_date,PaymentMode,TotalAmount,LastUpdateDate,PaymentsAmt from ng_RLOS_CUSTEXPOSE_LoanDetails where AgreementId='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";*/
		 
		sQuery1 = "select (select FirstName+' '+isNull(MiddleName,'')+' '+LAstName from  ng_RLOS_Customer with (nolock) where Wi_Name = '"+Wi_Name+"') as Fullname,isNull(TotalOutstandingAmt,'-') as TotalOutstandingAmt,isNull(TotalExposure,'-') as TotalExposure,isNull(NoofDefaultContracts,'-') as NoofDefaultContracts,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(WorstPaymentDelayin24Months,'-') as WorstPaymentDelayin24Months,isNull(WorstStatusinlast24Months,'-') as WorstStatusinlast24Months,isNull(LoanType,'-') as LoanType,isNull(AgreementId,'-') as AgreementId,isNull(LoanStat,'-') as LoanStat,'Primary' as RoleofCustomer,isNull(Loan_Start_Date,'-') as Loan_Start_Date,SYSDATETIME() as DateLastUpdated,isNull(OutstandingAmt,'-') as OutstandingAmt,isNull(ProductAmt,'-') as ProductAmt,isNull(NextInstallmentAmt,'-') as NextInstallmentAmt,isNull(TotalNoOfInstalments,'-') as TotalNoOfInstalments,isNull(RemainingInstalments,'-') as RemainingInstalments,isNull(OverdueAmt,'-') as OverdueAmt,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(CurrentlyCurrentFlg,'-') as CurrentlyCurrentFlg,isNull(CurrMaxUtil,'-') as CurrMaxUtil,isNull(DPD_30_in_last_6_months,'-') as DPD_30_in_last_6_months,isNull(DPD_60_in_last_18_months,'-') as DPD_60_in_last_18_months,isNull(SchemeCardProd,'-') as SchemeCardProd,isNull(Loan_disbursal_date,'-') as Loan_disbursal_date,isNull(loanmaturitydate,'-') as loanmaturitydate,isNull(lastupdatedate,'-') as lastupdatedate,isNull(PaymentsAmt,'-') as PaymentsAmt,isNull(WriteoffStat,'-') as WriteoffStat,isNull(WriteoffStatdt,'-') as WriteoffStatdt,isNull(NofDaysPmtDelay,'-') as NofDaysPmtDelay,isNull(PropertyValue,'-') as PropertyValue,isNull(MarketingCode,'-') as MarketingCode,isNull(LastRepmtDt,'-') as LastRepmtDt,isNull(PaymentMode,'-') as PaymentMode from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where AgreementId='"+AgreementId+"' and  Child_Wi='"+Wi_Name+"'";
		 
		params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;
		}
		
		if(AgreementId!=null && flag.equalsIgnoreCase("card"))
		{
		   //sQuery1 = "select ExpiryDate,CreditLimit,OutstandingAmt,OverdueAmt,CurrMaxUtil,CurrentBalance from ng_RLOS_CUSTEXPOSE_CardDetails where CardEmbossNum='"+AgreementId+"' and  //Child_Wi='"+Wi_Name+"'"; 
		   sQuery1 = "select (select FirstName+' '+isNull(MiddleName,'')+' '+LAstName from  ng_RLOS_Customer with (nolock) where Wi_Name = '"+Wi_Name+"') as Fullname,isNull(OutstandingAmt,'-') as OutstandingAmt ,isNull(TotalExposure,'-') as TotalExposure,isNull(NoofDefaultContracts,'-') as NoofDefaultContracts,isNull(WorstCurrentPaymentDelay,'-') as WorstCurrentPaymentDelay,isNull(WorstPaymentDelayin24Months,'-') as WorstPaymentDelayin24Months ,isNull(WorstStatusinlast24Months,'-') as WorstStatusinlast24Months,isNull(CardType,'-') as CardType,isNull(CardEmbossNum,'-') as CardEmbossNum,isNull(CardStatus,'-') as CardStatus,'Primary' as RoleofCustomer,SYSDATETIME() as DateLastUpdated,isNull(CreditLimit,'-') as CreditLimit,isNull(OverdueAmt,'-') as OverdueAmt,isNull(NofDaysPmtDelay,'-') as NofDaysPmtDelay,isNull(MonthsOnBook,'-') as MonthsOnBook,isNull(CurrMaxUtil,'-') as CurrMaxUtil,isNull(DPD_30_in_last_6_months,'-') as DPD_30_in_last_6_months,isNull(DPD_60_in_last_18_months,'-') as DPD_60_in_last_18_months,isNull(SchemeCardProd,'-') as SchemeCardProd,isNull(PaymentsAmount,'-') as PaymentsAmount,isNull(WriteoffStat,'-') as WriteoffStat,isNull(WriteoffStatdt,'-') as WriteoffStatdt,isNull(LastRepmtDt,'-') as LastRepmtDt,isNull(PaymentMode,'-') as PaymentMode,isNull(c.ExpiryDate,'-') as ExpiryDate,isNull(CardsApplcnRecvdDate,'-') as CardsApplcnRecvdDate,xyz.RemainingEMI as RemainingEMI,xyz.InstalmentPeriod as TotalNoOfInstalments from ng_RLOS_CUSTEXPOSE_CardDetails c with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails xyz with (nolock) on c.CardEmbossNum=xyz.CardNumber and (c.Child_Wi=xyz.Wi_Name or c.Child_Wi=xyz.Child_Wi) where CardEmbossNum='"+AgreementId+"' and (c.Wi_Name = '"+Wi_Name+"' or c.Child_Wi = '"+Wi_Name+"')";
		  params = "AgreementId=="+AgreementId+"~~Wi_Name=="+Wi_Name;	
		} 
		WriteLog("sQuery formed is --> "+sQuery1);
		WriteLog("sQuery_Common formed is --> "+sQuery_Common);
		
				inputXML_Common = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_Common + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				
		        inputXML1 = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery1 + "</Query><Params>"+ params + "</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";

				/*inputXML2 = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery1 + "</Query><Params>"+ params +"</Params><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";*/
				
			    try 
				{
					outputXML_Common = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML_Common);
					outputXML1 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML1);
				//	outputXML2 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML2);
			       
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
				//out.println(outputXML1);
			//	WriteLog("outputXML2 select Cif --> "+outputXML2);
				//out.println(outputXML2);
				xmlParserData_Common=new XMLParser();
				WriteLog("2");
				xmlParserData1=new XMLParser();
				WriteLog("3");
				xmlParserData2=new XMLParser();
				WriteLog("4");
				xmlParserData_Common.setInputXML((outputXML_Common));
				xmlParserData1.setInputXML((outputXML1));
				WriteLog("5");
				//xmlParserData2.setInputXML((outputXML2));
				mainCodeValue_Common = xmlParserData_Common.getValueOf("MainCode");
				mainCodeValue1 = xmlParserData1.getValueOf("MainCode");
			//	mainCodeValue2 = xmlParserData2.getValueOf("MainCode");
			WriteLog("6");
				
				if(mainCodeValue_Common.equals("0")){
				
					subXML_Common = xmlParserData_Common.getNextValueOf("Record");
					objXmlParser_Common = new XMLParser(subXML_Common);
					TotalOutstanding = objXmlParser_Common.getValueOf("TotalOutstanding");
					TotalOverdue = objXmlParser_Common.getValueOf("TotalOverdue");
					NoOfContracts = objXmlParser_Common.getValueOf("NoOfContracts");
					Total_Exposure = objXmlParser_Common.getValueOf("Total_Exposure");
					WorstCurrentPaymentDelay = objXmlParser_Common.getValueOf("WorstCurrentPaymentDelay");
					Nof_Records = objXmlParser_Common.getValueOf("Nof_Records");
					NoOf_Cheque_Return_Last3 = objXmlParser_Common.getValueOf("no_of_chq_ret_3months");
					Nof_DDES_Return_Last3Months = objXmlParser_Common.getValueOf("no_of_DDS_ret_3months");
					NoOf_Cheque_Return_Last6Months = objXmlParser_Common.getValueOf("no_of_chq_ret_6months");
					Nof_DDES_Return_Last6Months = objXmlParser_Common.getValueOf("no_of_DDS_ret_6months");
					
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
							
								}
								
								else if(flag.equalsIgnoreCase("card")){
								WriteLog("9");
								ExpiryDate=objXmlParser.getValueOf("ExpiryDate");
								 CreditLimit= objXmlParser.getValueOf("CreditLimit");
							     OutstandingAmt= objXmlParser.getValueOf("OutstandingAmt");
								 OverdueAmt= objXmlParser.getValueOf("OverdueAmt");
								 CurrMaxUtil= objXmlParser.getValueOf("CurrMaxUtil");
								 CurrentBalance= objXmlParser.getValueOf("CurrentBalance");
								 fullname = objXmlParser.getValueOf("Fullname");
								 totOutBal = objXmlParser.getValueOf("OutstandingAmt");
								 totExposure = objXmlParser.getValueOf("TotalExposure");
								 noDefCont = objXmlParser.getValueOf("NoofDefaultContracts");
								 worCurrPayDelay = objXmlParser.getValueOf("WorstCurrentPaymentDelay");
								 worsPayDel24 = objXmlParser.getValueOf("WorstPaymentDelayin24Months");
								worStatlast24 = objXmlParser.getValueOf("WorstStatusinlast24Months");
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
								}
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
		<td style="padding:0 15px 0 15px;"><input type="text" name='full_name'   disabled value='<%=fullname%> ' ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value='<%=TotalOutstanding%>' ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value='<%=TotalOverdue%>' ></td>
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=NoOfContracts%> ></td> 
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=Total_Exposure%> ></td> 
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Current Payment Delay (Bucket)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Payment Delay in 24 months (Bucket)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst status in last 24 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Number of Liabilities</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Cheque Return(3 Months)</b></label></td>
	</tr>
	<tr>

		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=WorstCurrentPaymentDelay%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=worsPayDel24%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=worStatlast24%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled value=<%=Nof_Records%>></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=NoOf_Cheque_Return_Last3%> ></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>No of DDS Return(3 Months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Cheque Return(6 Months)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of DDS Return(6 Months)</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"   disabled value=<%=Nof_DDES_Return_Last3Months%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"   disabled value=<%=NoOf_Cheque_Return_Last6Months%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"   disabled value=<%=Nof_DDES_Return_Last6Months%> ></td> 
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
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled value='<%=typeCont%>' ></td>	
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" id="AgreementId"  disabled value=<%=agree%> ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled value=<%=phase%> ></td>
	    <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled value=<%=role%> ></td> 
	    <td style="padding:0 15px 0 15px;"><input type="text" name="bday"  disabled value=<%=Loan_Start_Date%> ></td> 
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Close date</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Date Last Updated</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Outstanding Balance</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total Amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Payments amount (EMI)</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday"   disabled value=<%=loanmaturitydate%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday"  disabled value=<%=dateLastUp%>  ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=totOutBal%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=TotalAmount%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"   disabled value=<%=PaymentsAmt%>  ></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Method of Payment</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Total no of Installments</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of Remaining Installments</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst Status</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Worst status date</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=PaymentMode%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=TotalNoOfInstalments%> ></td> 
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=RemainingInstalments%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=WorstStatus%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday"  disabled value=<%=WorstStatusdate%> ></td>
	</tr>
	
	<tr>

		<td style="padding:0 15px 0 15px;"><label><b>Credit limit / IM amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Overdue amount</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>No of days payment delay</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>MOB (Months on Book)</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Last Repayment Month & Year (from historical data)</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=CreditLimit%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=OverdueAmt%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"   disabled value=<%=noDayPayDel%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"   disabled value=<%=mob%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bdaymonth"    disabled value="<%=LastRepmtDt%> "></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Currently Current</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Current  Utilization</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 30+ in last 6 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>DPD 60+ in last 18 months</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Cards B Score</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=currFlag%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=CurrMaxUtil%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=dpd6month%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"  disabled value=<%=dpd18month%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled ></td>
	</tr>
	
	<tr>
		<td style="padding:0 15px 0 15px;"><label><b>Scheme/Card Product</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Property Value</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Disbursal Date/Approved date</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Marketing code</b></label></td>
		<td style="padding:0 15px 0 15px;"><label><b>Card Expiry Date</b></label></td>
	</tr>
	<tr>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name"   disabled value='<%=schemeCardProd%> ' ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled  value='<%=PropertyValue%> '></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="bday"  disabled value=<%=Loan_disbursal_date%> ></td>
		<td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled></td>
		<td style="padding:0 15px 0 15px;"><input type="text" disabled name="bday" id = 'bogus'  disabled value=<%=ExpiryDate%> ></td>
	</tr>
	
	<tr>
         <td style="padding:0 15px 0 15px;"><label><b>Internal charge-off / Write-off amount</b></label></td>
	</tr>
	<tr>
	     <td style="padding:0 15px 0 15px;"><input type="text" name="full_name" disabled></td>
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
  

