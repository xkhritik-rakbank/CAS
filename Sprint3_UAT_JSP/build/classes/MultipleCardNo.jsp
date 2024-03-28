<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import ="java.io.*" %>
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
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>

<script language="javascript" src="/webdesktop/custom/CustomJS/workdesk.js"></script>

<%
	
	

	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	//svt points start
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("child_wi"), 1000, true) );
	String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	//svt points end
	WriteLog("wi_name inside multiple Cardno jsp : "+wi_name);
	String outputResponse="";
	XMLParser xmlParserData=null;
	String mainCodeData=null;
	String inputXML=null;
	String outputXML=null;
	String cardNo ="";
	String tagName="";
	String subTagName="";
	
	//String sQuery = "select top 1 CardEmbossNum from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where child_wi ='"+wi_name+"'";
	/* String sQuery = "select CIF_ID as CustId from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :child_wi) and Consider_For_Obligations = 'true'";
	String	params="child_wi=="+wi_name;
	 */		  
	 String sQuery ="select distinct CIFID as CustId from(select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi =:wi_name and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')  union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_CardDetails  with (nolock) where Child_Wi =:wi_name and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary' or Request_Type='CARD_INSTALLMENT_DETAILS') union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi =:wi_name and ODType != '' union all select CIF_ID as CustId from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :wi_name) and Consider_For_Obligations = 'true') as Int_expo";
	 String	params="wi_name=="+wi_name;
	 			WriteLog("sMQuery "+sQuery);
				WriteLog("sessionId"+sessionId);
				
				inputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input><option>APSelectWithNamedParam</option><Query>"+sQuery+"</Query><Params>"+params+"</Params><EngineName>"+cabinetName+"</EngineName><SessionID>"+sessionId+"</SessionID></APSelectWithNamedParam_Input>";
				try 
				{
					outputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}  
				WriteLog("outputXML select Cif --> "+outputXML);
				tagName = "Record";
				subTagName = "CustId";
				cardNo = commonParse(outputXML,tagName,subTagName);
				WriteLog("commonParse select cardNo --> "+cardNo);
				outputResponse= cardNo;	
			
			out.clear();
			out.println(outputResponse);
%>
<%!

public static String commonParse(String parseXml,String tagName,String subTagName)
	{
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String cifValues = "";
		
		WriteLog("tagName jsp: commonParse: "+tagName);
		WriteLog("subTagName jsp: commonParse: "+subTagName);
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	  String colValue="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  if(cifValues.equalsIgnoreCase(""))
				  cifValues = valueArr[1];
			  else

				  cifValues =cifValues+","+valueArr[1];
				WriteLog( "tcifValues" + cifValues);
				
		  }
		  return cifValues;
	}
	
	public static Map<Integer, String> getTagDataParent(String parseXml,String tagName,String subTagName){
	  
	  Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>(); 
	   InputStream is = new ByteArrayInputStream(parseXml.getBytes());
	  try {
			WriteLog("getTagDataParent jsp: parseXml: "+parseXml);
			WriteLog("getTagDataParent jsp: tagName: "+tagName);
			WriteLog("getTagDataParent jsp: subTagName: "+subTagName);
		    //InputStream is = new FileInputStream(parseXml);
			
		    WriteLog("getTagDataParent jsp: strOutputXml: "+is);
		  	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName(tagName);
			
			String[] values =subTagName.split(",");
			String value="";
			String subTagDerivedvalue="";
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Node uNode=eElement.getParentNode();
					
					for(int j=0;j<values.length;j++){
						if(eElement.getElementsByTagName(values[j]).item(0) !=null){
							value=value+","+eElement.getElementsByTagName(values[j]).item(0).getTextContent();
							subTagDerivedvalue=subTagDerivedvalue+","+values[j];
						}
						
					}
					value=value.substring(1,value.length());
					subTagDerivedvalue=subTagDerivedvalue.substring(1,subTagDerivedvalue.length());
					tagValuesMap.put(temp+1, subTagDerivedvalue+"~"+value+"~"+uNode.getNodeName());
					value="";
					subTagDerivedvalue="";
				}
			}
			
		    } catch (Exception e) {
		    	
			e.printStackTrace();
			WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
		    }
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		WriteLog("Exception occured in is close:  "+e.getMessage());
			    	}
			}
		    return tagValuesMap;
  }

%>



