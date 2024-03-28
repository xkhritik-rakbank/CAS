<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>

<%
String loanAmount = request.getParameter("loanAmount");
String interest_Rate = request.getParameter("interest_Rate");
String tenure = request.getParameter("tenure");
String firstEMIdate = request.getParameter("firstEMIdate");
String disbursementDate = request.getParameter("disbursementDate");
String repayment_Frequency = request.getParameter("repayment_Frequency");

String wi_name = request.getParameter("wi_name");
String ws_name = request.getParameter("ws_name");
String dda_Refer_no = request.getParameter("dda_Refer_no");
String dda_Status = request.getParameter("dda_Status");
String repayment_Mode = request.getParameter("repayment_Mode");
String funding_acc = request.getParameter("funding_acc");
String installment_plan = request.getParameter("installment_plan");
String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();

 SimpleDateFormat sdf1=new SimpleDateFormat("dd/mm/yyyy");
 SimpleDateFormat sdf2=new SimpleDateFormat("dd-mm-yyyy");
 String DatechangedDisburse=sdf2.format(sdf1.parse(disbursementDate));
 disbursementDate = DatechangedDisburse;
 
  String DatechangedEmi=sdf2.format(sdf1.parse(firstEMIdate));
  firstEMIdate = DatechangedEmi;
  
  if(dda_Status.equalsIgnoreCase("A"))
  {
	dda_Status = "APPROVED";
  }
  if(dda_Status.equalsIgnoreCase("P"))
  {
	dda_Status = "PENDING";
  }
  if(dda_Status.equalsIgnoreCase("D"))
  {
	dda_Status = "DECLINED";
  }
 
   if(installment_plan.equalsIgnoreCase("E"))
  {
	installment_plan = "EQUATED";
  }
	
%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>:: Generate Schedule ::</title>
<!-- Bootstrap core CSS -->
<link href="${pageContext.request.contextPath}/custom/css/bootstrap.min1.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/custom/fonts/css/font-awesome.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/custom/css/animate.min.css" rel="stylesheet">
<!-- Custom styling plus plugins -->
<script src="${pageContext.request.contextPath}/custom/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/custom/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/custom/js/sample.js"></script>
<link href="${pageContext.request.contextPath}/custom/css/jquery-ui.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/custom/js/jquery-ui.js"></script>
    <link href="${pageContext.request.contextPath}/custom/css/custom1.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/custom/js/custom.js"></script>
 <script>
 
		var loanAmount = '<%=loanAmount%>';
		var interest_Rate = '<%=interest_Rate%>';
		var tenure = '<%=tenure%>';
		var firstEMIdate = '<%=firstEMIdate%>';
		var disbursementDate = '<%=disbursementDate%>';
		var wi_name = '<%=wi_name%>';
		var ws_name = '<%=ws_name%>';
		var dda_Refer_no = '<%=dda_Refer_no%>';
		var dda_Status = '<%=dda_Status%>';
		var repayment_Frequency = '<%=repayment_Frequency%>';
		var repayment_Mode = '<%=repayment_Mode%>';
		var funding_acc = '<%=funding_acc%>';
		var installment_plan = '<%=installment_plan%>';
		var sSessionId = '<%=sSessionId%>';
		
		
		//alert('dda_Refer_no '+ dda_Refer_no);
		//alert('dda_Status '+ dda_Status);
		
		
  $(document).ready(
		  
		  /* This is the function that will get executed after the DOM is fully loaded */
		  function () {
			 // alert("document ready");  
		    $( "#Disbursedate" ).datepicker({
		      changeMonth: true,//this option for allowing user to select month
		      changeYear: true,//this option for allowing user to select from year range
		      dateFormat:"dd-mm-yy"
		    });
		    $( "#GracePeriod" ).datepicker({
			      changeMonth: true,//this option for allowing user to select month
			      changeYear: true, //this option for allowing user to select from year range
			      dateFormat:"dd-mm-yy"    
		    });
			
			
	if(ws_name!='DDVT_maker'){
			document.getElementById('Genbtn').innerHTML = "View";
		  }
		    
		  }
		  
		);
			

  </script>
</head>

<body class="nav-md">

	<div class="right_col" role="main">
		<div class="container">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>Repayment Schedule</strong>
				</div>
				<div class="table-responsive" style=" min-height: 350px;">
					<table class="table table-bordered">
						<thead>
							<tr>
								<td colspan="9">
									<div class="col-lg-12">
										<div class="panel-body">
											<form class="form-horizontal" role="form"
												action="LoanSchedule.jsp" method="post">
												<!-- <div
													style="font-weight: bold; text-decoration: underline; font-size: 15px; color: #823b01;">Loan
													Amount Details</div> -->
												<br>
												<div class="form-group">
													
													<label class="control-label col-sm-2">Installment Plan</label>
														 <div class="col-sm-2">
															<input class="form-control" id="RescheduleType" value = <%=installment_plan%> style='text-transform:uppercase' readonly >
														</div>
														
													<label class="control-label col-sm-2">Tenure (in
														months)</label>
													<div class="col-sm-2">
														<input class="form-control" id="Tenure"  value = <%=tenure%> readonly >
													</div>
													
													<label class="control-label col-sm-2">Interest Rate</label>
													<div class="col-sm-2">
														<input class="form-control" id="Rate"  value = <%=interest_Rate%> readonly >
													</div>	
		
												</div>
																				
												<div class="form-group">
												
													<label class="control-label col-sm-2">Loan Amount<span
														class="required"></span> </label>
									 				<div class="col-sm-2">
														<input class="form-control"id="LoanAmount" value = <%=loanAmount%> readonly >
													</div>
													
													<label class="control-label col-sm-2">Disbursement Date</label>
													<div class="col-sm-2">
														<input class="form-control" id="Disbursedate" value =<%=disbursementDate%> readonly >
													</div>
													
													<label class="control-label col-sm-2">FirstEMI Date</label>
													<div class="col-sm-2">
														<input  class="form-control" id="GracePeriod" value = <%=firstEMIdate%> disabled="disabled" >
													</div>
												</div>
													
												<div class="form-group">
														<label class="control-label col-sm-2">DDA Reference No</label>
														<div class="col-sm-2">
															<input class="form-control" id="ddaReferNo" readonly value = <%=dda_Refer_no%>>
														</div>
														<label class="control-label col-sm-2">DDA Status</label>
														<div class="col-sm-2">
															<input  class="form-control" id="ddaStatus" value = <%=dda_Status%> readonly >	
															
														</div>
														<label class="control-label col-sm-2">Repayment Frequency</label>
														<div class="col-sm-2">
															<input  class="form-control" id="repayFrequency" value = <%=repayment_Frequency%> readonly >	
															
														</div>
														
												</div>
												<div class="form-group">
														<label class="control-label col-sm-2">Validation Status</label>
														<div class="col-sm-2">
															<input class="form-control" id="validationStatus" readonly >
														</div>
														<label class="control-label col-sm-2">Out Flow</label>
														<div class="col-sm-2">
															<input  class="form-control" id="outflow" readonly >	
															
														</div>
														<label class="control-label col-sm-2">Funding Account No.</label>
														<div class="col-sm-2">
															<input  class="form-control" id="fundingAccNo" value = <%=funding_acc%> readonly >	
															
														</div>
														
												</div>
												<div class="form-group">
		
													<label class="control-label col-sm-2">Repayment Mode</label>
														<div class="col-sm-2">
															<input  class="form-control" id="repayMode" value = <%=repayment_Mode%> readonly >	
															
														</div>
														<label class="control-label col-sm-2">Account Scheme</label>
														<div class="col-sm-2">
															<input  class="form-control" id="accScheme"  readonly >	
															
														</div>
													
												</div>
													
											
											 <a class="btn btn-primary" href="#"
												style="margin-left: 1200px;" id="Genbtn"
												onclick="FetchLoanSchedule()">Generate</a>
												
												<a class="btn btn-primary" href="#"
												style="left: 1200px;" id="GenSave" onclick="SaveLoanSchedule()">Save</a>
												
												<a class="btn btn-primary" href="#"
												style="left: 1200px;" id="GenClose" onclick = "closeFunction()" >Close</a>
										</form>
										</div>


										<br />
										<div id="step-2">
											<div class="container">
											
											<div class="panel panel-primary">
  
  <div class="panel-heading">
        <h4 class="panel-title" style="display:inline;">
        Installment Plan
       </h4>  
  </div>
    
  <div class="panel-body" id="bidContent">
   <div class="jsgrid" style="position: relative; height: 70%; width: 100%;">
      <div class="jsgrid-grid-header jsgrid-header-scrollbar">
  </div>
  <div class="jsgrid-grid-body" style=" min-height: 50px; max-height: 300px; overflow-x:scroll;overflow-y:auto;">
  <table class="table table-bordered customtable1 table-striped customtableHeight" id="instplntbl" style="overflow-x:scroll;overflow-y:auto;">
	<thead>
	<tr>
        <th class="tdWidth" >Instal No.</th>
        <th  class="tdWidth" >Due Date</th>
        <th  class="tdWidth" >Days</th>
        <th  class="tdWidth" >EMI</th>
        <th  class="tdWidth" >Interest Component</th>
        <th  class="tdWidth" >Principal Component</th>
        <!-- <th  class="tdWidth" >Loan Balance</th>       -->		
        <th  class="tdWidth" >Opening Principle</th>
        <th  class="tdWidth" >Closing Principle</th>
		<th  class="tdWidth" >Life Insurance</th>
        <th  class="tdWidth" >Property Insurance</th>
		<th  class="tdWidth" >Excess Interest</th>
		<th  class="tdWidth" >Total Repayable(EMI + Insurance Premium)</th>
		<th  class="tdWidth" >Adv Flag</th>
		
       </tr>
	</thead>
    <tbody>
    </tbody>
  </table>
  
  </div>
  
  </div>
  </div>
  </div>
											
											
											
											</div>
										</div>
									
										<div id="step-2">
											
										</div>
								
									</div></td>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>



		
	</div>

	<script type="text/javascript">
		var result="";
		var ReturnVal="";
		var universalValue="";
		function FetchLoanSchedule() {
			try {
				
				$("#instplntbl tbody").empty();
		    	 var LoanAmount="";
		         var Rate="";
		         
		         var Tenure="";
		         var MoratoriumPeriod="";
		         var RepaymentFrequency="";// for rescheduleType
		         
		         var DueDay="";// for days360
		         var BalloonAmount="";// for disbursement_Date
		         var GracePeriod="";// for firstEMI_Date
		         var InterestStartDate="";
				 
				 var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}
		         
		         LoanAmount=document.getElementById('LoanAmount').value;
		         
		         
		         
		         Rate=document.getElementById('Rate').value;
		         
		         Tenure=document.getElementById('Tenure').value;
		         // MoratoriumPeriod=document.getElementById('MoratoriumPeriod').value;
		          RepaymentFrequency=document.getElementById('RescheduleType').value;// for rescheduleType
		         
		          /* DueDay=document.getElementById('DueDay').value;// for days360 */
		          BalloonAmount=document.getElementById('Disbursedate').value;// for disbursement_Date
		          GracePeriod=document.getElementById('GracePeriod').value;// for firstEMI_Date
		          /* InterestStartDate=document.getElementById('InterestStartDate').value;// for emi */
		         
		          //alert("LoanAmount"+LoanAmount);
		         if(LoanAmount==null ||LoanAmount=='')
		      	{
		      		alert("LoanAmount Is  mandatory");
		      		document.getElementById('LoanAmount').focus();
		      		return false;
		      	}
		         if(Rate==null ||Rate=='')
		      	{
		      		alert("Rate Is  mandatory");
		      		document.getElementById('Rate').focus();
		      		return false;
		      	}
		         if(Tenure==null ||Tenure=='')
		      	{
		      		alert("Tenure Is  mandatory");
		      		document.getElementById('Tenure').focus();
		      		return false;
		      	}
		       
		         if(BalloonAmount==null ||BalloonAmount=='')
		      	{
		      		alert("Disbursement Date Is  mandatory");
		      		document.getElementById('BalloonAmount').focus();
		      		return false;
		      	}
		         if(GracePeriod==null ||GracePeriod=='')
		      	{
		      		alert("FirstEMI_Date Is  mandatory");
		      		document.getElementById('GracePeriod').focus();
		      		return false;
		      	}
		       
				 //var url="LoanSchedule.jsp?LoanAmount="+LoanAmount+"&Rate="+Rate+"&Tenure="+Tenure+"&RepaymentFrequency="+RepaymentFrequency+"&DueDay="+DueDay+"&BalloonAmount="+BalloonAmount+"&GracePeriod="+GracePeriod+"&InterestStartDate="+InterestStartDate;
				 
				 var url="LoanSchedule.jsp?LoanAmount="+LoanAmount+"&Rate="+Rate+"&Tenure="+Tenure+"&RepaymentFrequency="+RepaymentFrequency+"&DueDay="+DueDay+"&BalloonAmount="+BalloonAmount+"&GracePeriod="+GracePeriod+"&InterestStartDate="+InterestStartDate;
		         var params = "";
				//window.open(url);
				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send(null);
				
				
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);					
				}
				else
				{
					//alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					dataFromAjax="";
				}
				
				ReturnVal = dataFromAjax;
				
		        // ReturnVal=Fun_Ajax(url,params);
				// alert("ReturnVal " + ReturnVal);
				 
				 var fields = ReturnVal.split('@');

				ReturnVal = fields[0];
				var strxmlval = fields[1];
				var emi = fields[2];				
				
				
				 //alert(ReturnVal);
		         result = $.parseJSON(ReturnVal);
		         if(result.status=="success")
		         {
		         	$.each(result.data, function () {
		                 var tr=$("<tr></tr>");
		               /*    tr.append("<td id='docname'>"+this.month+"</td>");
		                  tr.append("<td id='docname'>"+this.date+"</td>");
		                 tr.append("<td id='filename'>"+this.daysInMonth+"</td>");
		                 tr.append("<td id='docname'>"+this.emi+"</td>");
		                 tr.append("<td id='docname'>"+this.monthlyInterest+"</td>");
		                 tr.append("<td id='docname'>"+this.monthlyPrincipal+"</td>");
		                 tr.append("<td id='docname'>"+this.loanBalance+"</td>");
		                */
		                $.each(this, function (name, value) {
		  	    		  var td=$("<td></td>").addClass("tdWidth");	
		  	    		  td.append(value);
		  	    		  tr.append(td);
		      		   });
		                 $("#instplntbl tbody").append(tr);
		              });
		         	//alert(result.message);
		         }
		         else
		         {
		         	//alert(result.message);
		         }
		          universalValue= strxmlval;
				 // window.opener.document.getElementById('cmplx_LoanDetails_loanemi').value = emi;
				  //alert('universalValue' +universalValue);
		       
		     }catch (e) {
				alert("Exception from saveInvoiceData function" + e);
			}

		}
		
		function closeFunction()
		{
			if (confirm("Are you sure you want to close!") == true)
			{
				txt = "true";
			}
			else 
			{
				txt = "false";
			}
			if(txt=="true")
				window.close();
		}
		
		function SaveLoanSchedule()
		{
			var wi_name = '<%=wi_name%>';
			var sSessionId = '<%=sSessionId%>';
			
			//alert('ReturnVal11 '+universalValue);
			//alert ('wi_name ' + wi_name);
			
			//var url="SaveSchedule.jsp?result="+universalValue+"&wi_name="+wi_name;
			var url="SaveSchedule.jsp";
			var params = "result="+universalValue+"&wi_name="+wi_name+"&sSessionId="+sSessionId;
			//window.open(url);
			var ReturnVal=Fun_Ajax(url,params);
		//	alert(' ReturnVal SaveLoanSchedule ' + ReturnVal);
			if(ReturnVal=="Success")
			{
				alert('Schedulment saved succesfully');
			}
			else
			{
				alert('Schedulment Failed to save');
			}
		}

		function Fun_Ajax(url, params) {
			try {
				//params = escape(params);
				var response = "";

				xmlReq = null;
				if (window.XMLHttpRequest)
					xmlReq = new XMLHttpRequest();
				else if (window.ActiveXObject)
					xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
				if (xmlReq == null)
					return; // Failed to create the request
				xmlReq.onreadystatechange = function() {
					switch (xmlReq.readyState) {
					case 0: // Uninitialized
						//alert("Uninitialized");
						break;
					case 1: // Loading
						//alert("Loading");
						break;
					case 2: // Loaded
						//alert("Loaded");
						break;
					case 3: // Interactive
						//alert("Interactive");
						break;
					case 4: // Done!
						if (xmlReq.status == 200) {
							response = xmlReq.responseText;
							response = response.trim();
							console.log(response);
						} else if (xmlReq.status == 404) {
							//alert("URL doesn't exist!");
							response = 'FAIL';
						} else {
							if (xmlReq.status == 500)
								alert("Please RE-LOGIN");
							else
								alert("Status is "+xmlReq.status);

								response = 'FAIL';
						}

						break;
					default:
						//alert(xmlReq.status);
						response = 'FAIL';
						break;
					}
				};
				// Make the request
				//alert ('xmlReq '+ xmlReq);
				//alert ('params '+ params);
				xmlReq.open('POST', url, false);
				xmlReq.setRequestHeader('Content-Type',
						'application/x-www-form-urlencoded');
				xmlReq.send(params);
				//document.getElementById('err').style.display = 'block';
				//alert("response " + response);
				return response;
			} catch (e) {
				//alert("Exception from Fun_Ajax() in UW_Ajax.js : " + e);
			}

		}
	</script>



</body>


</html>