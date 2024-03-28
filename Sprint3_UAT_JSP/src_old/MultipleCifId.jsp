<%@ include file="Log.process"%>
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

<script language="javascript" src="/webdesktop/custom/CustomJSP/workdesk.js"></script>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	
	String wi_name = request.getParameter("wi_name");
	String req_name = request.getParameter("req_name");
	String activityName = request.getParameter("activity");
	String sessionId = request.getParameter("sessionId");
	String child_wi = "";
	String params="";
	WriteLog("activityName is: "+activityName);
	if(activityName.indexOf("Init")<0 && !activityName.equalsIgnoreCase("Re_Initiate")){
		child_wi = request.getParameter("child_wi");
	}
	String cabinetName ="";
			String wrapperIP = "";
			String wrapperPort = "";
			String appServerType = "";

	WriteLog("wi_name jsp: result: "+wi_name);
	WriteLog("req_name jsp: result: "+req_name);
	
	try {
			String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
		    File file = new File(prop_file_loc);
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
			 cabinetName = properties.getProperty("cabinetName");
			 wrapperIP = properties.getProperty("wrapperIP");
			 wrapperPort = properties.getProperty("wrapperPort");
			 appServerType = properties.getProperty("appServerType");
        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
		
	String outputResponse="";
	XMLParser xmlParserData=null;
	String mainCodeData=null;
	String inputXML=null;
	String outputXML=null;
	String cifId ="";
	String tagName="";
	String subTagName="";
	String sQuery ="";
	
	
	
	if(req_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")){
		//sQuery ="select max(CardEmbossNum) as CardEmbossNum from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where CifId IN (select distinct cifid from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where (Wi_Name =:Wi_Name or Child_Wi =:Wi_Name) and  (SchemeCardProd = 'LOC PREFERRED' or SchemeCardProd = 'LOC STANDARD'  or CardType='LOC PREFERRED' or CardType='LOC STANDARD')) and (Wi_Name =:Wi_Name or Child_Wi =:Wi_Name) group by CifId";
		//Deepak query update by Deepak for PCAS-2767
			sQuery ="select max(CardEmbossNum) as CardEmbossNum from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where (Wi_Name =:Wi_Name or Child_Wi =:Wi_Name) and  (SchemeCardProd = 'LOC PREFERRED' or SchemeCardProd = 'LOC STANDARD'  or CardType='LOC PREFERRED' or CardType='LOC STANDARD') and CardStatus !='C' group by CifId";			
			params="Wi_Name=="+wi_name;
			WriteLog("$$$$$$Squery for Card Installment: "+sQuery);
					
		}
	//added by akshay on 26/3/18 for proc 6430
	else if(req_name.equalsIgnoreCase("ACCOUNT_SUMMARY")){	
				sQuery ="select distinct CifId from (select cifid from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock)where Child_Wi =:Child_Wi and ( schemeCardProd like '%amal%' or SchemeCardProd like '%MURABAHA%' or SchemeCardProd like '%IJARAH%') union select cifid from ng_RLOS_CUSTEXPOSE_loanDetails where  Child_Wi =:Child_Wi and ( schemeCardProd like '%amal%' or SchemeCardProd like '%MURABAHA%' or SchemeCardProd like '%IJARAH%') ) temp";
				params="Child_Wi=="+child_wi;
		}
		//added by nikhil for PCAS-2202 Linked CIF CR
	else if(req_name.equalsIgnoreCase("CollectionsSummary")){	
				//sQuery ="select distinct CIFID as CustId from(select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where (Wi_Name =:wi_name or Child_Wi =:wi_name) and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat not in ('Pipeline','C') union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_CardDetails  with (nolock) where (Wi_Name =:wi_name or Child_Wi =:wi_name) and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary' or Request_Type='CARD_INSTALLMENT_DETAILS') and CardStatus ='A' union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where (Wi_Name =:wi_name or Child_Wi =:wi_name)  and ODType != '' and AcctStat='ACTIVE') as Int_expo";
				if(!"".equalsIgnoreCase(child_wi))
				{
					sQuery ="select distinct CIFID as CustId from(select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi =:wi_name and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')  union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_CardDetails  with (nolock) where Child_Wi =:wi_name and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary' or Request_Type='CARD_INSTALLMENT_DETAILS') union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi =:wi_name and ODType != '' union all select CIF_ID as CustId from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :wi_name) and Consider_For_Obligations = 'true') as Int_expo";
					params="wi_name=="+child_wi;
				}
				else
				{
					sQuery="select distinct CIFID as CustId from(select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name =:wi_name and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')  union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_CardDetails  with (nolock) where Wi_Name =:wi_name and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary' or Request_Type='CARD_INSTALLMENT_DETAILS')  union all select distinct CifId as CIFID from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name =:wi_name and ODType != ''  union all select CustId from ng_rlos_cif_detail with (nolock) where cif_wi_name =:wi_name and cif_SearchType !='External') as Int_expo";
					params="wi_name=="+wi_name;
				}
		}
		
		else {
				if(child_wi.equalsIgnoreCase("")){
				sQuery = "select CustId from ng_rlos_cif_detail with (nolock) where cif_wi_name =:wi_name and cif_SearchType !='External'";
				params="wi_name=="+wi_name;
				}
				else{
					if(child_wi.contains("PL")){
					//winame changed to child_wi on 1st May by saurabh for PROC - 8630
					sQuery = "select CustId from ng_rlos_cif_detail with (nolock) where cif_wi_name :+child_wi and cif_SearchType !='External' union select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :child_wi) and Consider_For_Obligations = 'true' UNION All select CIF_ID from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPLOYER_CODE=( select EmpCode from ng_RLOS_EmpDetails with (nolock) where wi_name = :child_wi) and CIF_ID <> ''";
					params="child_wi=="+child_wi;
					}
					else{
					//++below code added by nikhil for PCAS-2140 CR	
					sQuery = "select CIF_ID as CustId from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = :child_wi) and Consider_For_Obligations = 'true'";
					params="child_wi=="+child_wi;					
					}
				}
			}	
				/*
				WriteLog("sMQuery "+sQuery);
				WriteLog("sessionId"+sessionId);
				*/				
				inputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input><option>APSelectWithNamedParam</option><Query>"+sQuery+"</Query><Params>"+params+"</Params><EngineName>"+cabinetName+"</EngineName><SessionID>"+sessionId+"</SessionID></APSelectWithNamedParam_Input>";
				
				//inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				WriteLog("sMQuery inputXML: "+inputXML);
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
				WriteLog("outputXML  CIF IDs --> "+outputXML);
				
				//added by akshay on 26/3/18
				String totalRetrieved=outputXML.substring(outputXML.indexOf("<TotalRetrieved>")+16,outputXML.indexOf("</TotalRetrieved>"));
				WriteLog("TotalRetrieved CIF IDs --> "+totalRetrieved);
				if(totalRetrieved.equalsIgnoreCase("0")){
					outputResponse= "NO DATA";
				}
				else{	
				tagName = "Record";
				if(req_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")){
					subTagName = "CardEmbossNum";
				}
				else if(req_name.equalsIgnoreCase("ACCOUNT_SUMMARY")){
					subTagName = "CifId";
				}
				else{	
					subTagName = "CustId";
				}
				cifId = commonParse(outputXML,tagName,subTagName);
				WriteLog("commonParse select Cif --> "+cifId);
				outputResponse= cifId;	
				WriteLog("commonParse select Cif -- outputResponse> "+outputResponse);
			}
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
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values: " + entry.getValue());
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


