<html>
    <head>
        <title></title>
<script type="text/javascript" language="javascript" src="eida_webcomponents.js"></script>
		<script type="text/javascript">
function callEIDA() {
				Initialize();
				DisplayPublicDataEx();
				alert("EID: " +fetchEID());
			}
</script>

    </head>
	
	    <body leftmargin="0" topmargin="0" marginwidth="0" 
          marginheight="0"  class="dark-matter">
		   <!-- Load jQuery and bootstrap datepicker scripts -->
       <applet name="EIDAWebComponent" id="EIDAWebComponent" CODE="emiratesid.ae.webcomponents.EIDAApplet" archive="EIDA_IDCard_Applet.jar" width="0" height="0"></applet>
        <form name="wdesk">
        			
		<input name='EmiratesID' type='button' id='ReadEmiratesID' value='Read' maxlength = '100' class='EWButtonRB' style='width:85px' onclick="callEIDA();return false;">
		<input type="text" name ="emirates_id_val" id="emirates_id_val" value='Read' maxlength = '100' class='EWButtonRB' style='width:85px'></input>
        </form>

    </body>
</html>