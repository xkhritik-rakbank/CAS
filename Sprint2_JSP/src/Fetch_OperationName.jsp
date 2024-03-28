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
	
	
	String acc_type = request.getParameter("acc_type");
	String activityName = request.getParameter("activityName");
	String sub_product = request.getParameter("sub_product");
	String emp_type = request.getParameter("emp_type");
	String application_type = request.getParameter("application_type");
	String appServerType = "";
	String wrapperIP = "";
	String wrapperPort = "";    
	String sessionId = request.getParameter("SeesionId");
	String cabinetName = "";
WriteLog("emp_type jsp: result: "+emp_type);
	WriteLog("acc_type jsp: result: "+acc_type);
	WriteLog("application_type jsp: result: "+application_type);
	String outputResponse="";
	XMLParser xmlParserData=null;
	String mainCodeData=null;
	String inputXML=null;
	String outputXML=null;
	String operation_name ="";
	String tagName="";
	String subTagName="";
	String sQuery="";
	
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
		
		if(activityName.equalsIgnoreCase("DDVT_maker") || activityName.equalsIgnoreCase("CAD_Analyst1")) 
		{
			activityName="DDVT,CA";
		}
		if(emp_type.equalsIgnoreCase("Salaried Pensioner")){
		emp_type="Salaried";
		}
		
		// below changes done by disha 18-7-2016 for Pre-approved cases if else condition added
		if (sub_product.equalsIgnoreCase("PA"))
		{
			if(emp_type.equalsIgnoreCase("Salaried") || acc_type.equalsIgnoreCase(""))//modified by akshay on 2/1/18
			{
				sQuery = "select Operation_name from ng_rlos_Financial_Summary with (nolock) where WorkStep ='"+activityName+"' and Sub_Product = '"+sub_product+"' and Employment = '"+emp_type+"' and Application_Type = '"+application_type+"'";
			}
			else
			{
				sQuery = "select Operation_name from ng_rlos_Financial_Summary with (nolock) where AccountType ='"+acc_type+"' and WorkStep ='"+activityName+"' and Sub_Product = '"+sub_product+"' and Employment = '"+emp_type+"' and Application_Type = '"+application_type+"'";
			}
	    }
		else
		{
			if(emp_type.equalsIgnoreCase("Salaried") || acc_type.equalsIgnoreCase(""))//modified by akshay on 2/1/18
			{
				sQuery = "select Operation_name from ng_rlos_Financial_Summary with (nolock) where WorkStep ='"+activityName+"' and Sub_Product = '"+sub_product+"' and Employment = '"+emp_type+"'";
			}
			else
			{
				sQuery = "select Operation_name from ng_rlos_Financial_Summary with (nolock) where AccountType ='"+acc_type+"' and WorkStep ='"+activityName+"' and Sub_Product = '"+sub_product+"' and Employment = '"+emp_type+"'";
			}
		}
			  WriteLog("sMQuery "+sQuery);
				WriteLog("sessionId : "+sessionId);
				inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				
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
				WriteLog("outputXML select Operation_name --> "+outputXML);
				tagName = "Record";
				subTagName = "Operation_name";
				operation_name = commonParse(outputXML,tagName,subTagName);
				WriteLog("commonParse select Operation_name --> "+operation_name);
				outputResponse= operation_name;	
			
			out.clear();
			out.println(outputResponse);
%>
<%!

public static String commonParse(String parseXml,String tagName,String subTagName)
	{
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String req_name = "";
		
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
			  if(req_name.equalsIgnoreCase(""))
				  req_name = valueArr[1];
			  else
				  req_name =req_name+","+valueArr[1];
				  
				WriteLog( "treq_name : " + req_name);
				
		  }
		  return req_name;
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



