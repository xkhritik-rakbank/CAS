package com.newgen.omniforms.skutil;

/*------------------------------------------------------------------------------------------------------
NEWGEN SOFTWARE TECHNOLOGIES LIMITED
Author                         : Sanjeev Kumar (sanjeev-kumar@newgen.co.in)
Group                          : AP-2
Project/Product                : Generic
Application                    : RLOS COE
Module                         : RLOS
File Name                      : RLOSCommon.java
Date (DD/MM/YYYY)              : 22-Aug-2016
Description                    : This file contains all the generic menthods which can be used in iBPS processes
------------------------------------------------------------------------------------------------------*/


import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.RLOS;
import com.newgen.omniforms.util.CommonUtilityMethods;

public class Common extends CommonUtilityMethods {

   /**
	 * 
	 */
	private static final long serialVersionUID = 4656185969098326876L;

public Date convertToDate(String input, Locale locale) {
       Calendar calendar = new GregorianCalendar();

       List<String> sampleFormats = new ArrayList<String>();
       sampleFormats.add("dd MMM,yyyy");
       sampleFormats.add("MMM dd,yyyy");

       sampleFormats.add("dd MMM,yy");
       sampleFormats.add("MMM dd,yy");

       sampleFormats.add("dd-MMM-yyyy");
       sampleFormats.add("dd-MMM-yy");

       sampleFormats.add("MMM-dd-yyyy");
       sampleFormats.add("MMM-dd-yy");

       sampleFormats.add("dd-MM-yyyy");
       sampleFormats.add("dd-MM-yy");

       sampleFormats.add("dd.MM.yyyy");
       sampleFormats.add("dd.MM.yy");

       sampleFormats.add("dd.MMM.yyyy");
       sampleFormats.add("dd.MMM.yy");

       sampleFormats.add("MMM.dd.yyyy");
       sampleFormats.add("MMM.dd.yy");

       sampleFormats.add("dd/MMM/yyyy");
       sampleFormats.add("dd/MMM/yy");

       sampleFormats.add("MMM/dd/yyyy");
       sampleFormats.add("MMM/dd/yy");

       sampleFormats.add("dd/MM/yyyy");
       sampleFormats.add("dd/MM/yy");

       int flag = 0;
       Date date = null;
       try {
           if (!"".equalsIgnoreCase(input))//pgarg
           {
               SimpleDateFormat format;//pgarg

               for (String formatTypes : sampleFormats) 
               {                   
	               format = new SimpleDateFormat(formatTypes, locale);
	               format.setLenient(false);
	               date = format.parse(input);
	               flag = 1;	               
	               break;
	           }

               if (flag == 0) 
               {
                   return null;
               }

               calendar.setLenient(false);
               calendar.setTime(date);
           } 
           else 
           {              
               return null;
           }

       } catch (Exception e1) {
           RLOS.mLogger.info( "Exception:" + e1);
           RLOS.logException(e1);
       }
       return date;
   }

   /**
    * @Author sanjeev-kumar@newgen.co.in
    * @Date 22-Aug-2016
    * @Description FUNCTION FOR LOADING ALL COLLAPSIBLE FRAMES
    */
   public void frameCollapsibleAdjusment(String[] childFrameCollapsible, String parentFrame, String childFrameToExpand, String[] childFrameToVis, String[] frag, String[] allframes, FormReference formObject) {
//frameCollapsibleAdjusment(childFrameLRSP, parentFrameLRSP, childFrameToExpLRSP, childFrameToVisLRSP, fragSequenceLRSP, allframesLRSP, formObject);
      
       int prevTop = formObject.getTop(childFrameCollapsible[0]);
       int prevHeight = 20;
       int flag = 0;
      

       if ("".equals(childFrameToExpand) && childFrameToVis.length == 0) 
       {
           for (int i = 0; i < childFrameCollapsible.length; i++) 
           {
               if (formObject.isVisible(childFrameCollapsible[i])) 
               {
      
                   if (flag == 0) 
                   {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       
                       flag = 1;

                   } 
                   else
                   {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                       
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;
                   

               }
           }

           if (!"".equals(parentFrame)) 
		   {               
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);               
           }

       }
       else if (!"".equals(childFrameToExpand) && childFrameToVis.length == 0) 
       {           
           for (int i = 0; i < childFrameCollapsible.length; i++) 
           {
               if (formObject.isVisible(childFrameCollapsible[i])) 
               {                   
                   if (flag == 0) 
                   {
                       formObject.setTop(childFrameCollapsible[i], prevTop);                       
                       flag = 1;
                   }
                   else 
                   {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);                       
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   if (childFrameCollapsible[i].equalsIgnoreCase(childFrameToExpand)) 
                   {
                       prevHeight = formObject.getHeight(childFrameCollapsible[i]);
                   }
                   else 
                   {
                       prevHeight = 20;
                   }
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;                 
               }
           }

           if (!"".equals(parentFrame)) 
           {
              RLOS.mLogger.info("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
              RLOS.mLogger.info("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }

       } 
       else if ("".equals(childFrameToExpand) && childFrameToVis.length > 0) 
       {
          RLOS.mLogger.info("In 2 else frameCollapsibleAdjusment");

           for (int i = 0; i < childFrameCollapsible.length; i++) 
           {
               if (formObject.isVisible(childFrameCollapsible[i])) 
               {
                  RLOS.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " is Visible");
                   if (flag == 0) 
                   {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       flag = 1;

                   }
                   else 
                   {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrame[i]) + 10;
                  RLOS.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               } 
               else 
               {
                  RLOS.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " is InVisible");
                   for (String childToVis : childFrameToVis) 
                   {
                       if (childFrameCollapsible[i].equalsIgnoreCase(childToVis)) 
                       {
                           formObject.setVisible(childToVis, true);
                           formObject.setTop(childToVis, prevTop + prevHeight + 2);
                           prevTop = formObject.getTop(childToVis);
                           //prevHeight = formObject.getHeight(childToVis) + 10;
                          RLOS.mLogger.info("ChildFrame To Visible " + childToVis + " prevTop " + prevTop + " prevHeight " + prevHeight);

                       }
                   }
               }
           }

           if (!"".equals(parentFrame)) 
           {
              RLOS.mLogger.info("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
              RLOS.mLogger.info("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }
       }

       if (allframes.length > 0 && frag.length > 0) 
       {
           int previousTop = formObject.getTop(allframes[0]);
           int previousHeight = 0;
           int j = 0;
           flag = 0;
           int fragHeight;
        
           for (int i = 0; i < (allframes.length) && j < (frag.length); i++, j++) 
           {
               if (formObject.isVisible(allframes[i])) 
               {
                   if (flag == 0) {
                       formObject.setTop(allframes[i], previousTop);
                       flag = 1;
                   } else 
                   {
                       formObject.setTop(allframes[i], previousTop + previousHeight);
                   }

                   fragHeight = formObject.getHeight(frag[j] + "_Frame1");               
                   fragHeight += 5;                  
                   formObject.setHeight(allframes[i], fragHeight);
                   previousTop = formObject.getTop(allframes[i]);
                   previousHeight = formObject.getHeight(allframes[i]) + 2;                  
               }
           }
       }
   }

   /**
    * @Author sanjeev-kumar@newgen.co.in
    * @Date 22-Aug-2016
    * @Description FUNCTION FOR LOADING ALL THE CHILD FRAMES WITHIN A FRAMES
    */
   public void frameAdjusment(String[] childFrame, String parentFrame, String[] childFrameToVis, String[] frag, String[] allframes, FormReference formObject) {

      RLOS.mLogger.info("In frameAdjusment");
       int prevTop = formObject.getTop(childFrame[0]);
       int prevHeight = 0;
       //int flag = 0;
      RLOS.mLogger.info(childFrame[0] + " prevTop " + prevTop);
      RLOS.mLogger.info("Child Frame To Visible " + childFrameToVis.length);

       if (childFrameToVis.length == 0) {
           for (int i = 0; i < childFrame.length; i++) {
               if (formObject.isVisible(childFrame[i])) {
                  RLOS.mLogger.info("ChildFrame " + childFrame[i] + " is Visible");
                   formObject.setTop(childFrame[i], prevTop + prevHeight);
                  RLOS.mLogger.info("ChildFrame " + childFrame[i] + " moved up");

                   prevTop = formObject.getTop(childFrame[i]);
                   prevHeight = formObject.getHeight(childFrame[i]) + 2;
                  RLOS.mLogger.info("ChildFrame " + childFrame[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               }
           }

          RLOS.mLogger.info("parentFrame " + parentFrame + " to be set prevTop " + prevTop + " prevHeight " + prevHeight);
           formObject.setHeight(parentFrame, prevTop + prevHeight + 2);

       } else {
          RLOS.mLogger.info("In else");
           for (int i = 0; i < childFrame.length; i++) {
               if (formObject.isVisible(childFrame[i])) {
                  RLOS.mLogger.info("ChildFrame " + childFrame[i] + " is Visible");
                   formObject.setTop(childFrame[i], prevTop + prevHeight);

                   prevTop = formObject.getTop(childFrame[i]);
                   prevHeight = formObject.getHeight(childFrame[i]) + 2;
                  RLOS.mLogger.info("ChildFrame " + childFrame[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               } else {
                  RLOS.mLogger.info("ChildFrame " + childFrame[i] + " is InVisible");
                   for (String childToVis : childFrameToVis) {
                       if (childFrame[i].equalsIgnoreCase(childToVis)) {
                           formObject.setVisible(childToVis, true);
                           formObject.setTop(childToVis, prevTop + prevHeight);
                           prevTop = formObject.getTop(childToVis);
                           prevHeight = formObject.getHeight(childToVis) + 2;
                          RLOS.mLogger.info("ChildFrame To Visible " + childToVis + " prevTop " + prevTop + " prevHeight " + prevHeight);

                       } else {
                           continue;
                       }
                   }
               }

           }
          RLOS.mLogger.info("parentFrame " + parentFrame + " to be set prevTop " + prevTop + " prevHeight " + prevHeight);
           formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
       }

       if (allframes.length > 0 && frag.length > 0) {

           int previousTop = formObject.getTop(allframes[0]);
           int previousHeight = 0;
           int j = 0;
           //flag = 0;
           int fragHeight;
          RLOS.mLogger.info(allframes[0] + " " + formObject.isVisible(allframes[0]) + " previousTop " + previousTop);
           for (int i = 0; i < (allframes.length) && j < (frag.length); i++, j++) {
               if (formObject.isVisible(allframes[i])) {
                   formObject.setTop(allframes[i], previousTop + previousHeight);

                   fragHeight = formObject.getHeight(frag[j] + "_Frame1");
                  RLOS.mLogger.info("fragHeight: " + fragHeight);
                   fragHeight += 1;
                  RLOS.mLogger.info("fragHeight: " + fragHeight);
                   formObject.setHeight(allframes[i], fragHeight);

                   previousTop = formObject.getTop(allframes[i]);
                   previousHeight = formObject.getHeight(allframes[i]) + 2;
                  RLOS.mLogger.info("previousTop: " + previousTop);
                  RLOS.mLogger.info("previousHeight: " + previousHeight);
               }
           }

       }

   }

   /**
    * @Author sanjeev-kumar@newgen.co.in
    * @Date 22-Aug-2016
    * @param Controls
    * @Description FUNCTION FOR DISABLING LIST OF CONTROLS
    */
   public void disableControls(String Controls) {
       FormReference formObject = FormContext.getCurrentInstance().getFormReference();
       String[] CName = Controls.split(",");
       for (int i = 0; i < CName.length; ++i) {
           formObject.setEnabled(CName[i], false);
           formObject.setNGBackColor(CName[i], Color.LIGHT_GRAY);
       }
   }

   /**
    * @Author sanjeev-kumar@newgen.co.in
    * @Date 22-Aug-2016
    * @param Controls
    * @Description FUNCTION FOR ENABLING LIST OF CONTROLS
    */
   public void enableControls(String Controls) {
       FormReference formObject = FormContext.getCurrentInstance().getFormReference();
       String[] CName = Controls.split(",");
       for (int i = 0; i < CName.length; ++i) {
           formObject.setEnabled(CName[i], true);
           formObject.setNGBackColor(CName[i], Color.WHITE);
       }
   }

   public void LoadPickList(String controlname, String query) {
       FormReference formObject = FormContext.getCurrentInstance()
               .getFormReference();
      RLOS.mLogger.info("LoadPickList Query for " + controlname + ":"
               + query);
       // formObject.getDataFromDataSource(arg0, arg1);
       List<String> control = Arrays.asList(controlname);
       formObject.getDataFromDataSource(query, control);
   }

   /*
   public static String execBRMSWebService(int rule_id, String inputXml, String inputObj) {
       //String strResponseMsg = "";

       FormReference formObject = FormContext.getCurrentInstance().getFormReference();
       List<List<String>> recordList1;
       List<List<String>> recordList2;

       String strResponseMsg = "";
       try {

           String query1 = "select * from RLOS_WSRULECONFIG where RULEID=" + rule_id;
           String query2 = "select * from RLOS_WSPARAMETERMAPPING where RULEID=" + rule_id;

           recordList1 = formObject.getNGDataFromDataCache(query1);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "recordList1:" + recordList1);

           String endURI = "";
           String username = "";
           String password = "";
           String cab_name = "";
           String namespace = "";
           String namespaceUri = "";

           endURI = ((String) ((List) recordList1.get(0)).get(2));
           username = ((String) ((List) recordList1.get(0)).get(3));
           password = ((String) ((List) recordList1.get(0)).get(4));
           cab_name = ((String) ((List) recordList1.get(0)).get(5));
           namespace = ((String) ((List) recordList1.get(0)).get(6));
           namespaceUri = ((String) ((List) recordList1.get(0)).get(7));

           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "endURI:" + endURI);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "username:" + username);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "password:" + password);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "cab_name:" + cab_name);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "namespace:" + namespace);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "namespaceUri:" + namespaceUri);

           //SOAP REQUEST
           javax.xml.soap.SOAPConnectionFactory soapConnectionFactory = javax.xml.soap.SOAPConnectionFactory.newInstance();
           javax.xml.soap.SOAPConnection connection = soapConnectionFactory.createConnection();

           javax.xml.soap.SOAPFactory soapFactory = javax.xml.soap.SOAPFactory.newInstance();

           javax.xml.soap.MessageFactory factory = javax.xml.soap.MessageFactory.newInstance();
           javax.xml.soap.SOAPMessage message = factory.createMessage();

           javax.xml.soap.SOAPBody body = message.getSOAPBody();
           javax.xml.soap.SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

           envelope.addNamespaceDeclaration(namespace, namespaceUri);

           javax.xml.soap.Name executeRuleset = soapFactory.createName("executeRuleset", namespace, namespaceUri);
           javax.xml.soap.SOAPBodyElement executeRulesetElement = body.addBodyElement(executeRuleset);

           javax.xml.soap.Name args0 = soapFactory.createName("args0", namespace, namespaceUri);
           javax.xml.soap.SOAPElement args0Ele = executeRulesetElement.addChildElement(args0);

           javax.xml.soap.Name cabinetNameTag = soapFactory.createName("cabinetName", namespace, namespaceUri);
           javax.xml.soap.SOAPElement cabinetNameEle = args0Ele.addChildElement(cabinetNameTag);
           cabinetNameEle.addTextNode(cab_name);

           javax.xml.soap.Name loginReqdTag = soapFactory.createName("loginReqd", namespace, namespaceUri);
           javax.xml.soap.SOAPElement loginReqdEle = args0Ele.addChildElement(loginReqdTag);
           loginReqdEle.addTextNode("false");

           javax.xml.soap.Name passwordTag = soapFactory.createName("password", namespace, namespaceUri);
           javax.xml.soap.SOAPElement passwordEle = args0Ele.addChildElement(passwordTag);
           passwordEle.addTextNode(password);

           javax.xml.soap.Name userNameTag = soapFactory.createName("userName", namespace, namespaceUri);
           javax.xml.soap.SOAPElement userNameEle = args0Ele.addChildElement(userNameTag);
           userNameEle.addTextNode(username);

           javax.xml.soap.Name input_EligibilityobjTag = soapFactory.createName(inputObj, namespace, namespaceUri);
           javax.xml.soap.SOAPElement input_EligibilityobjEle = args0Ele.addChildElement(input_EligibilityobjTag);

           //RLOS.mLogger.info("Executing query");
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "Executing query for Generic Web Service Invoker");

           //ResultSet rs2 = stmt.executeQuery(query2);
           recordList2 = formObject.getNGDataFromDataCache(query2);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "recordList2:" + recordList2);

           String XML = "";

           XML = inputXml;

           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "XML is" + XML);

            XML= "<?xml version=\"1.0\"?><APWebService_Input><Option>APWebservice</Option><Calltype>Rlos_Eigblity_Call</Calltype><EngineName>rloscab</EngineName><SessionId>-2145567285</SessionId><employerStatus>Approved</employerStatus><incomeType>Salaried</incomeType><nationality>Home</nationality><royalFamilyMember>Yes</royalFamilyMember><salary>45000</salary><sourceOfRepayment>Within Bank</sourceOfRepayment></APWebService_Input>";
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "Creating tags from the table");
          RLOS.mLogger.info("Creating tags from the table");
           //((String) ((List) recordList1.get(0)).get(2))
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "Size of recordList2:" + recordList2.size());

           for (List<String> recordlist : recordList2) {
               RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "List<String> recordlist:" + recordlist.toString());

               if (recordlist.get(2).equals("I") && recordlist.get(6).equals("Y")) {
                   javax.xml.soap.Name fieldTag = soapFactory.createName(recordlist.get(3), namespace, namespaceUri);
                   javax.xml.soap.SOAPElement fieldEle = input_EligibilityobjEle.addChildElement(fieldTag);
                   // fieldEle.addTextNode(rs2.getString(6));
                   //When data will come from the XML then
                   fieldEle.addTextNode(XML.substring(XML.indexOf(recordlist.get(3)) + recordlist.get(3).length() + 1, XML.indexOf("</" + recordlist.get(3))));

               }

           }
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "Setting the URI");
           URL endpoint = new URL(endURI);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "endpoint :" + endpoint);
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "Request Sent");

           message.writeTo(System.out);

           javax.xml.soap.SOAPMessage response = connection.call(message, endpoint);
           connection.close();
           System.out.print("\nResponse Sent");
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "nResponse Request :");
           response.writeTo(System.out);
          RLOS.mLogger.info();

           //Response String
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           response.writeTo(out);
           strResponseMsg = new String(out.toByteArray());
           RLOS.mLogger.info("RLOSCommon execBRMSWebService()", "Setting strResponseMsg URI" + strResponseMsg);
          RLOS.mLogger.info("strResponseMsg" + strResponseMsg);
       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return strResponseMsg;
*/
   }


