<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*,java.io.FileWriter"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="XMLParser.XMLParser"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException,com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page  import= "org.w3c.dom.Element"%>
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker"%>
<%@ page  import= "java.util.Map"%>
<%@ page  import= "java.io.ByteArrayInputStream"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.io.FileInputStream"%>
<%@ page  import= "java.io.FileNotFoundException"%>
<%@ page  import= "java.io.InputStream"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException,org.xml.sax.InputSource,org.xml.sax.SAXException" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>


<%!
		String cabinetName="";
		String sInputXML="";
		String sOutputXML="";
		String params="";
		String MainCode="";
		String wrapperPort="";
		String wrapperIP ="";
		String appServerType="";
		String updatedColumns="";    
		String tableName="";
		//String sWhere="";
	String txtFilePath="";	
	 String inputPath = "";				
	 String sourceDestinaton = "";
	 String successPath = "";
	 String errorPath = "";
	 String sourcePath = "";
	 String inProgressPath = "";
	 String movedPath = "";
	 String failPath = "";
	 String filePath="";
	 
%>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	//String sessionId =request.getParameter("sessionId");
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );
	String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
%>

<%	
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
			WriteLog("cabinetName "+cabinetName);
			
			
			prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"Murabha.properties";
			file = new File(prop_file_loc);
			fileInput = new FileInputStream(file);
			 properties.load(fileInput);
			 fileInput.close();
			 //tableName= properties.getProperty("tableName");
			 updatedColumns= properties.getProperty("updatedColumns");
			 txtFilePath=System.getProperty("user.dir")+ File.separatorChar+properties.getProperty("txtFilePath");
			 inputPath=		properties.getProperty("inputPath");
			sourceDestinaton=properties.getProperty("sourceDestination");
			errorPath=properties.getProperty("errorPath");
			successPath=properties.getProperty("successPath");
			WriteLog("tableName "+tableName);
			WriteLog("updatedColumns "+updatedColumns);
			WriteLog("txtFilePath "+txtFilePath);
				
        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
	
	try{
		
		String returnValue=processWI(cabinetName,wrapperIP,wrapperPort,appServerType,sessionId);
		WriteLog("Value returned from processWI(): "+returnValue);
		out.clear();
		out.print(returnValue);
	}
	catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
		
%>



<%!
	public  String processWI(String cabinetName,String wrapperIP,String wrapperPort,String appServerType,String sessionId)
	{
		String updateStatus="";
		String TimeStamp="";
		String newFilename="";
		String returnMsg="";
		try
		{
			Format formatter = new SimpleDateFormat("dd-MMM-yy");
			String sdate = formatter.format(new Date());

			sourcePath = txtFilePath+File.separatorChar+inputPath;
			movedPath = txtFilePath+File.separatorChar+successPath;
			failPath = txtFilePath+File.separatorChar+errorPath;
			inProgressPath = txtFilePath+File.separatorChar+sourceDestinaton;

			 WriteLog("sourcePath "+sourcePath);
			 WriteLog("movedPath "+movedPath);
			 WriteLog("failPath "+failPath);
			 WriteLog("inProgressPath "+inProgressPath);

			File folder = new File(sourcePath);

			filePath = sourcePath;
			WriteLog("@@@@@@@@@@@@ "+filePath);
			
				File files[] = folder.listFiles();
				 WriteLog("Number of files is "+ files.length);	
				if(files.length == 0)
				{
					 returnMsg+="No file in the input folder";
					 return returnMsg;							
				}
				else
				{
					 WriteLog("files is not null");
					 WriteLog("Total files for processing folder: "+files.length);

				for(int i=0;i<files.length;i++)	
				{
					returnMsg+="\n";
					String TempsourcePath = "";
					String TempfailPath = "";
					String TempinProgressPath = "";
					String TempmovedPath = "";
					String file = files[i].getName();
					String msg = getMsgAsString(file);//returns the file content as a String
					if(msg.length()<=0  || msg.equalsIgnoreCase("blank"))
					{
						WriteLog("No Data in File : " + file);
						TempsourcePath = ""+sourcePath+File.separatorChar+file+"";
						TempfailPath = failPath+File.separatorChar+sdate;
						TimeStamp=get_timestamp();
						newFilename = Move(TempfailPath,TempsourcePath,TimeStamp,true);//file is moved to NoDataFile flder
						returnMsg+="file "+file+" is blank...!!!!";
					}
					else if((msg.equalsIgnoreCase("error")))
					{
						 WriteLog("Error in source file : " + file);
						TempsourcePath = ""+sourcePath+File.separatorChar+file+"";
						TempfailPath = failPath+File.separatorChar+sdate;
						TimeStamp=get_timestamp();
						newFilename = Move(TempfailPath,TempsourcePath,TimeStamp,true);//file is moved to NoDataFile flder
						returnMsg+= "Exception occurred while reading file "+file+".....Please contact support!!!!";
					}
					else{
						 WriteLog("Move file to In progress folder: " + file);
						TempinProgressPath = inProgressPath;
						TempsourcePath = ""+sourcePath+File.separatorChar+file+"";
						TimeStamp=get_timestamp();
						newFilename = Move(TempinProgressPath,TempsourcePath,TimeStamp,false);
						 WriteLog(" file moved to In progress folder: " + newFilename);

						String finalSourcePath = TempinProgressPath+File.separatorChar+newFilename;
						 WriteLog(" finalSourcePath: " + finalSourcePath);
						 updateStatus = readTextFile(newFilename,finalSourcePath,cabinetName,wrapperIP,wrapperPort,appServerType,sessionId);
						 WriteLog("updateStatus "+updateStatus);
						if(updateStatus.equalsIgnoreCase("Success"))
						{
							
							 WriteLog("ng_rlos_Murabha_Warranty table successfully updated");

							//TempsourcePath = ""+sourcePath+File.separatorChar+file+"";
							TempmovedPath = movedPath+File.separatorChar+sdate;
							TimeStamp=get_timestamp();
							newFilename = Move(TempmovedPath,finalSourcePath,TimeStamp,true);//file is moved to NoDataFile flder
							returnMsg+="Successfully uploaded file "+file+"...!!!";
						}
						else
						{
							TempfailPath = failPath+File.separatorChar+sdate;
							TimeStamp=get_timestamp();
							newFilename = Move(TempfailPath,finalSourcePath,TimeStamp,true);//file is moved to NoDataFile flder
							returnMsg+=updateStatus;
						}
					}
				}
				return returnMsg;	
			}
		}
			catch(Exception e)
			{				
				 WriteLog("Exception occurred in processWI "+printException(e));
					return "Critical error occurred.....Please contact support!!!!"; 
			}
	}

	public String readTextFile(String fileName,String filePath,String cabinetName,String wrapperIP,String wrapperPort,String appServerType,String sessionId) 
	{
		FileInputStream fstream=null;
		BufferedReader br=null;
		Map<String, String> mymap= new LinkedHashMap<String,String>();
		XMLParser xmlParserData=new XMLParser();
		XMLParser objXmlParser=new XMLParser();
		WriteLog("file readTextFile "+ filePath);

		try 
		{
			fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String[] fields ;
			String[] columnNamearr ;
			String[] values;
			
			String returnMsg="";
			String finalValues="";
			String recordCount="";
			String strInputXML="";
			String strOutputXML ="";
			String subXML="";
			String mainCode="";
			String sQuery="";
			String totalrecords="";
			String[] updatedColumns_array=updatedColumns.split(",")	;
			float warrantAmount=0;
			float purchaseAmount=0;
			String warrantNo="";
			Map<String, String> Warranty_CRNNo_map= new LinkedHashMap<String,String>();
			String Purchase_CRNNo="";
			
			String WIname="";
			String ECRN_no="";
			String CardProduct="";
			String File_id="";
			columnNamearr = ("Login_User_id,RAKBANK,RAK_CUSTOMER,Customer_name,CREDIT_CARD,Null_1,NUll_2,Trade_DATE,Settlement_Date,Warranty_Expiry_Date,Currency,Amount,Commodity_Type,Null_3,Null_4,File_id,Null_5,CRN_no,ECRN_no,Deal_class,Top_up,Null_6,Tp_up_cycle,Null_7,Messenger,Principal_Calc_Type,NULL_8,NULL_9,NULL_10,NULL_11,Return_Code,Return_Deal_key,Return_Deal_no,Warrant_no,Return_Quantity,Return_quantity_with_units,Return_unit_price,Return_unit_price_currency,Return_principal,Return_Location").split(",");
			/* strLine = br.readLine();
			fields = strLine.split("H\\|",2);
			
			columnName=fields[1].split("\\|");
		 */
			//WriteLog("columnName length "+columnName[41]);
			
			while ((strLine = br.readLine()) != null){

				/* if(strLine.substring(0, 1).equalsIgnoreCase("T")){					
					
				}else{ */
					//fields = strLine.split("B\\|",2);
					//WriteLog(fields[1]);
					WriteLog("strLine :"+strLine);
					values=strLine.split("\",\"");
					WriteLog("values length"+values.length);
					WriteLog("values :"+values);
					if(values.length>columnNamearr.length){
						for(int i=0;i<columnNamearr.length;i++){
							mymap.put(columnNamearr[i],values[i]);
						}
						
						File_id = mymap.get("File_id");
						
						if(mymap.get("Warrant_no").equals("") || mymap.get("Warrant_no").equalsIgnoreCase("null")){
							returnMsg="Return Allocation Number not available for all the records in file "+fileName;
							return returnMsg;							
						}
						
						Warranty_CRNNo_map.put(mymap.get("ECRN_no"),mymap.get("CRN_no"));
						warrantAmount+=mymap.get("Amount").equals("")?0:Float.parseFloat(mymap.get("Amount"));
						
						for(int i=0;i<updatedColumns_array.length;i++){
							WriteLog("finalValues "+i+": "+finalValues);
							if(i==0){
								finalValues+= mymap.get(updatedColumns_array[i]);
							}
							else{
								finalValues+= "~"+mymap.get(updatedColumns_array[i]);	
							}
						}
						finalValues=finalValues+"#";	
					}
					else{
						WriteLog("Column length does not match with values provided....Aborting!!!!");
					}                               
			}
		
			recordCount=finalValues.split("#").length+"";
			WriteLog("recordCount: "+recordCount);
			sQuery="SELECT ECRN_no AS WIname_CardProduct,amount,crn_no,card_product,Murhabha_WIName FROM ng_rlos_Murabha_Warranty with(nolock) WHERE File_id='"+File_id+"'";
			strInputXML=ExecuteQuery_APSelectWithColumnNames(sQuery, cabinetName, sessionId);
			WriteLog("strInputXML: "+strInputXML);
			strOutputXML =NGEjbClient.getSharedInstance().makeCall(wrapperIP,wrapperPort,appServerType, strInputXML);
			WriteLog(strOutputXML);
			xmlParserData.setInputXML(strOutputXML);
			mainCode=xmlParserData.getValueOf("MainCode");
			
			if(mainCode.equalsIgnoreCase("0")){
				totalrecords=xmlParserData.getValueOf("TotalRetrieved");
				if(!totalrecords.equalsIgnoreCase(recordCount)){
					returnMsg="Total Number of records is not matching with Purchase File for Warranty file "+fileName;
					return returnMsg;
				}
				
				for(int i=0;i<Integer.parseInt(totalrecords);i++)
				{
					subXML = xmlParserData.getNextValueOf("Record");
					objXmlParser.setInputXML(subXML);
					purchaseAmount+=objXmlParser.getValueOf("amount").equals("")?0:Float.parseFloat(objXmlParser.getValueOf("amount"));
					Purchase_CRNNo=objXmlParser.getValueOf("crn_no");
					ECRN_no=objXmlParser.getValueOf("ECRN_no");
					WIname=objXmlParser.getValueOf("Murhabha_WIName");
					CardProduct=objXmlParser.getValueOf("card_product");
					if(Warranty_CRNNo_map.containsKey(ECRN_no)){
						WriteLog("Warranty_CRNNo_map: "+Warranty_CRNNo_map.get(ECRN_no));
						if(!Purchase_CRNNo.equalsIgnoreCase(Warranty_CRNNo_map.get(ECRN_no))){
							returnMsg="CRN No mismatch in the uploaded file "+fileName;
							return returnMsg;
						}	
						
					}	
				}
				
				if(purchaseAmount!=warrantAmount)
				{
					returnMsg="Total Amount is not matching with Purchase File for Warranty file "+fileName;
					return returnMsg;
				}
				
				WriteLog("session id: "+sessionId);
				strInputXML=ExecuteQuery_APProcedure("ng_rlos_MurhabhaUpload","'"+finalValues+"'",cabinetName,sessionId);
				WriteLog("strInputXML: "+strInputXML);
				
				strOutputXML =NGEjbClient.getSharedInstance().makeCall(wrapperIP,wrapperPort,appServerType, strInputXML);
				WriteLog(strOutputXML);
				objXmlParser.setInputXML(strOutputXML);
				mainCode=objXmlParser.getValueOf("MainCode");
				
				if(mainCode.equalsIgnoreCase("0")){
					sQuery="select status from ng_rlos_Murabha_Warranty with(nolock) WHERE status='File Uploaded' and File_id='"+File_id+"'";
					strInputXML=ExecuteQuery_APSelect(sQuery, cabinetName, sessionId);
					WriteLog("strInputXML "+strInputXML);	
					strOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP,wrapperPort,appServerType, strInputXML);
					WriteLog(strOutputXML);
					objXmlParser.setInputXML(strOutputXML);
					//String mainCode=objXmlParser.substring(strOutputXML.indexOf("<MainCode>")+10,strOutputXML.indexOf("</MainCode>"));
					 mainCode=objXmlParser.getValueOf("MainCode");

					if(mainCode.equalsIgnoreCase("0")){
						//String totalrecords=strOutputXML.substring(strOutputXML.indexOf("<TotalRetrieved>")+16,strOutputXML.indexOf("</TotalRetrieved>"));
						totalrecords=objXmlParser.getValueOf("TotalRetrieved");
						WriteLog(totalrecords+":"+finalValues.split("#").length);
						if(Integer.parseInt(totalrecords)==(finalValues.split("#").length)){
							return "Success";
						}
						else{
							return "Error occurred while reading file "+fileName+".....Please contact support!!!!";
						}
					}
					else{
							return "Error occurred while reading file "+fileName+".....Please contact support!!!!";
						}
				}
				else{
						return "Error occurred while reading file "+fileName+".....Please contact support!!!!";
					}	
			}
			else{
					return "Error occurred while reading file "+fileName+".....Please contact support!!!!";
				}			
		} 
		catch (Exception e) 
		{
			WriteLog("exception occurred in readTextFile " + printException(e));
			return "Error occurred while reading file "+fileName+".....Please contact support!!!!";
		}
		finally{
			try{
				fstream.close();
				br.close();
			}
			catch(Exception ex){WriteLog("Exception occurred while closing file connection!!!"+printException(ex));}
		}
		//return "";
	}

	public static String get_timestamp()
	{
		Date present = new Date();
		Format pformatter = new SimpleDateFormat("dd-MM-yyyy-hhmmss");
		return pformatter.format(present);
	}


	public  String ExecuteQuery_APSelect(String sQuery,String sEngineName,String sSessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>" +
				"<APSelect_Input>" +
				"<Option>APSelect</Option>" +
				"<Query>" + sQuery + "</Query>" +
				"<EngineName>" + sEngineName + "</EngineName>" +
				"<SessionId>" + sSessionId + "</SessionId>" +
				"</APSelect_Input>";
		return sInputXML;
	} 

	public  String ExecuteQuery_APSelectWithColumnNames(String sQuery,String sEngineName,String sSessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>" +
				"<APSelectWithColumnNames_Input>" +
				"<Option>APSelectWithColumnNames</Option>" +
				"<Query>" + sQuery + "</Query>" +
				"<EngineName>" + sEngineName + "</EngineName>" +
				"<SessionId>" + sSessionId + "</SessionId>" +
				"</APSelectWithColumnNames_Input>";
		return sInputXML;
	} 	
	
	
	public static String ExecuteQuery_APProcedure(String ProcName, String Params, String cabinetName ,String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>\n" 
				+"<APProcedure_WithDBO_Input>\n" 
				+"<option>APProcedure_WithDBO</option>\n" 
				+"<ProcName>"+ProcName+"</ProcName>\n" 
				+"<Params>"+Params+"</Params>\n" 
				+"<EngineName>"+cabinetName+"</EngineName>\n"
				+"<SessionId>"+sessionId+"</SessionID>\n"
				+"</APProcedure_WithDBO_Input>";
		return sInputXML;
	}

public  String Move(String pstrDestFolderPath, String pstrFilePathToMove,String append,boolean flag ) 
	{
		 WriteLog("Inside Move  : "+pstrDestFolderPath+" : "+pstrFilePathToMove);
		String lstrExceptionId = "Text_Read.Move";
		String newFilename="";
		try 
		{

			// Destination directory
			File lobjDestFolder = new File(pstrDestFolderPath);

			if (!lobjDestFolder.exists()) 
			{

				lobjDestFolder.mkdirs();

				//delete destination file if it already exists
			
			}
			File lobjFileTemp;
			File lobjFileToMove = new File(pstrFilePathToMove);
			String orgFileName=lobjFileToMove.getName();

			if(flag){
				newFilename=orgFileName.substring(0,orgFileName.indexOf("."))+orgFileName.substring(orgFileName.indexOf("."));
				WriteLog("Before moving----new file name is "+newFilename);
				lobjFileTemp = new File(pstrDestFolderPath + File.separator + newFilename);
			}else{
				 WriteLog("orgFileName::"+orgFileName);
				newFilename=orgFileName;
				lobjFileTemp = new File(pstrDestFolderPath+ File.separator + newFilename );
				 WriteLog("lobjFileTemp::"+lobjFileTemp);
			}
			if (lobjFileTemp.exists()) 
			{
				 WriteLog("lobjFileTemp exists");
				if (!lobjFileTemp.isDirectory()) 
				{

					lobjFileTemp.delete();

				} 
				else 
				{

					deleteDir(lobjFileTemp);

				}
			} 
			else 
			{
				 WriteLog("lobjFileTemp dont exists");
				lobjFileTemp = null;
			}
			File lobjNewFolder ;
			// if(flag){
			lobjNewFolder = new File(lobjDestFolder, newFilename);
			/* }else{
            	 lobjNewFolder = lobjDestFolder;
            }*/


			boolean lbSTPuccess = false;
			try 
			{
				 WriteLog("lobjFileToMove::"+lobjFileToMove);
				 WriteLog("lobjNewFolder::"+lobjNewFolder);
				lbSTPuccess = lobjFileToMove.renameTo(lobjNewFolder);
				 WriteLog("lbSTPuccess::"+lbSTPuccess);
			} 
			catch (SecurityException lobjExp) 
			{

				 WriteLog("SecurityException " + lobjExp.toString());
			} 
			catch (NullPointerException lobjNPExp) 
			{

				 WriteLog("NullPointerException " + lobjNPExp.toString());
			} 
			catch (Exception lobjExp) 
			{

				 WriteLog("Exception " + lobjExp.toString());
			}
			if (!lbSTPuccess) 
			{
				// File was not successfully moved


				 WriteLog("Failure while moving " + lobjFileToMove.getAbsolutePath() + "===" +
						lobjFileToMove.canWrite());
			} 
			else 
			{

				 WriteLog("Success while moving " + lobjFileToMove.getName() + "to" + pstrDestFolderPath);
				 WriteLog("Success while moving " + lobjFileToMove.getName() + "to" + lobjNewFolder);
			}
			lobjDestFolder = null;
			lobjFileToMove = null;
			lobjNewFolder = null;
		} 
		catch (Exception lobjExp) 
		{
			 WriteLog(lstrExceptionId + " : " + "Exception occurred while moving " + pstrFilePathToMove + " to " +
					":" + lobjExp.toString());

		}

		return newFilename;
	}

	
	public  boolean deleteDir(File dir) throws Exception {
        if (dir.isDirectory()) {
            String[] lstrChildren = dir.list();
            for (int i = 0; i < lstrChildren.length; i++) {
                boolean success = deleteDir(new File(dir, lstrChildren[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
	
	
	public static String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;	

	}

	public  String getMsgAsString(String file) throws IOException
	{
		FileReader fr = null;
		BufferedReader br = null;
		String msg = "";
		try
		{
			
			WriteLog("inside get msg as string method");
			char[] c = { 0x0D, 0x0A };
			String crlf = new String(c);
			fr = new FileReader(filePath+File.separatorChar+file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			while(line!=null)
			{
				msg += line.trim()+crlf;
				line = br.readLine();
			}
			int msgLength = msg.length();
			if(msgLength == 0)
			{
				msg = "blank";
			}
			else
			{
				msg = msg.substring(0,msgLength-crlf.length());
			}
			 WriteLog("completion : get msg as string method");
		}
		catch(Exception e)
		{
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			 WriteLog("Exception while converting the file into String : "+result.toString());
			msg = "error";
		}
		finally
		{
			fr.close();
			br.close();
		}
		return msg;
	}	
%>		