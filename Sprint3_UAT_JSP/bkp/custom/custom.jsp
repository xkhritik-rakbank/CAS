<%--
      Custom JSP implementation
--%>
<%@ include file="/generic/wdcustominit.jsp"%>

<%
    //All information will be available through wDSession object. Refer below sample code.
    
    WDCabinetInfo wDCabinetInfo = wDSession.getM_objCabinetInfo();
    WDUserInfo wDUserInfo = wDSession.getM_objUserInfo();
    
    String cabinetName = wDCabinetInfo.getM_strCabinetName();
    String userName = wDUserInfo.getM_strUserName();
%>
<html>
    <f:view>    
        <head>
            <link href='<%=wDSession.getM_strOmniAppServerDetails()%>' rel="stylesheet" type="text/css">
            <script>            
                // custom scripts here
                function onCustomLinkClick(op){
                    var url="";
                    if(op=="1"){
                        url = "custom"+op+".jsp";
                        window.open(url,"custom"+op,'height=400,width=400,resizable=0,status=1,scrollbars=0,top=200,left=200');
                    }
                }
            </script>
        </head>
        <body>
            <span onclick="onCustomLinkClick('1')" class="linkstyle" style="margin:5px">Custom Link1</span>
        </body>
    </f:view>
</html>