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
            <script src="/resources/scripts/wdgeneral.js"/>
            <script>            
                // custom scripts here
                
            </script>
        </head>
        <body>
            Welcome <%=wDUserInfo.getM_strUserName()%>
        </body>
    </f:view>
</html>