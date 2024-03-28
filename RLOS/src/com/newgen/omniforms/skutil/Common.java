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


import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.CommonUtilityMethods;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Common extends CommonUtilityMethods {

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
           if (!input.equalsIgnoreCase("")) {
               SimpleDateFormat format = null;

               for (String formatTypes : sampleFormats) {
                   try {
                       format = new SimpleDateFormat(formatTypes, locale);
                       format.setLenient(false);
                       date = format.parse(input);
                       flag = 1;
                       if (flag != 0) {
                           break;
                       }

                   } catch (ParseException e) {
                   }

               }

               if (flag == 0) {
                   return null;
               }

               calendar.setLenient(false);
               calendar.setTime(date);
           } else {
               System.out.println("Empty Field");
               return null;
           }

       } catch (Exception e1) {
           SKLogger.writeLog("RLOSCommon convertToDate", "Exception:" + e1);
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
       System.out.println("In Frame Collapsible Adjusment");
       int prevTop = formObject.getTop(childFrameCollapsible[0]);
       int prevHeight = 20;
       int flag = 0;
       System.out.println(childFrameCollapsible[0] + " prevTop " + prevTop);
       System.out.println("Child Frame To Expand " + childFrameToExpand);
       System.out.println("Child Frame To Visible " + childFrameToVis.length);

       if (childFrameToExpand.equals("") && childFrameToVis.length == 0) {
           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   System.out.println("Child Frame " + childFrameCollapsible[i] + " is Visible");
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       System.out.println("ChildFrame " + childFrameCollapsible[i] + " top setted");
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                       System.out.println("ChildFrame " + childFrameCollapsible[i] + " moved up");
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;
                   System.out.println("ChildFrame " + childFrameCollapsible[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               }
           }

           if (!parentFrame.equals("")) {
               System.out.println("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
               System.out.println("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }

       } else if (!childFrameToExpand.equals("") && childFrameToVis.length == 0) {
           System.out.println("In 1 else frameCollapsibleAdjusment");
           System.out.println("Frame To Expand " + childFrameToExpand);
           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   System.out.println("Child Frame " + childFrameCollapsible[i] + " is Visible");
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       System.out.println("ChildFrame " + childFrameCollapsible[i] + " top setted");
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                       System.out.println("ChildFrame " + childFrameCollapsible[i] + " moved up");
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   if (childFrameCollapsible[i].equalsIgnoreCase(childFrameToExpand)) {
                       prevHeight = formObject.getHeight(childFrameCollapsible[i]);

                   } else {
                       prevHeight = 20;
                   }
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;
                   System.out.println("ChildFrame " + childFrameCollapsible[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               }
           }

           if (!parentFrame.equals("")) {
               System.out.println("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
               System.out.println("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }

       } else if (childFrameToExpand.equals("") && childFrameToVis.length > 0) {
           System.out.println("In 2 else frameCollapsibleAdjusment");

           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   System.out.println("ChildFrame " + childFrameCollapsible[i] + " is Visible");
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrame[i]) + 10;
                   System.out.println("ChildFrame " + childFrameCollapsible[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               } else {
                   System.out.println("ChildFrame " + childFrameCollapsible[i] + " is InVisible");
                   for (String childToVis : childFrameToVis) {
                       if (childFrameCollapsible[i].equalsIgnoreCase(childToVis)) {
                           formObject.setVisible(childToVis, true);
                           formObject.setTop(childToVis, prevTop + prevHeight + 2);
                           prevTop = formObject.getTop(childToVis);
                           //prevHeight = formObject.getHeight(childToVis) + 10;
                           System.out.println("ChildFrame To Visible " + childToVis + " prevTop " + prevTop + " prevHeight " + prevHeight);

                       }
                   }
               }
           }

           if (!parentFrame.equals("")) {
               System.out.println("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
               System.out.println("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }
       }

       if (allframes.length > 0 && frag.length > 0) {

           int previousTop = formObject.getTop(allframes[0]);
           int previousHeight = 0;
           int j = 0;
           flag = 0;
           int fragHeight = 0;
           System.out.println(allframes[0] + " " + formObject.isVisible(allframes[0]) + " previousTop " + previousTop);
           for (int i = 0; i < (allframes.length) && j < (frag.length); i++, j++) {
               if (formObject.isVisible(allframes[i])) {
                   if (flag == 0) {
                       formObject.setTop(allframes[i], previousTop);
                       flag = 1;
                   } else {
                       formObject.setTop(allframes[i], previousTop + previousHeight);
                   }

                   fragHeight = formObject.getHeight(frag[j] + "_Frame1");
                   System.out.println("fragHeight: " + fragHeight);
                   fragHeight += 5;
                   System.out.println("fragHeight: " + fragHeight);
                   formObject.setHeight(allframes[i], fragHeight);

                   previousTop = formObject.getTop(allframes[i]);
                   previousHeight = formObject.getHeight(allframes[i]) + 2;
                   System.out.println("previousTop: " + previousTop);
                   System.out.println("previousHeight: " + previousHeight);
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

       System.out.println("In frameAdjusment");
       int prevTop = formObject.getTop(childFrame[0]);
       int prevHeight = 0;
       //int flag = 0;
       System.out.println(childFrame[0] + " prevTop " + prevTop);
       System.out.println("Child Frame To Visible " + childFrameToVis.length);

       if (childFrameToVis.length == 0) {
           for (int i = 0; i < childFrame.length; i++) {
               if (formObject.isVisible(childFrame[i])) {
                   System.out.println("ChildFrame " + childFrame[i] + " is Visible");
                   formObject.setTop(childFrame[i], prevTop + prevHeight);
                   System.out.println("ChildFrame " + childFrame[i] + " moved up");

                   prevTop = formObject.getTop(childFrame[i]);
                   prevHeight = formObject.getHeight(childFrame[i]) + 2;
                   System.out.println("ChildFrame " + childFrame[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               }
           }

           System.out.println("parentFrame " + parentFrame + " to be set prevTop " + prevTop + " prevHeight " + prevHeight);
           formObject.setHeight(parentFrame, prevTop + prevHeight + 2);

       } else {
           System.out.println("In else");
           for (int i = 0; i < childFrame.length; i++) {
               if (formObject.isVisible(childFrame[i])) {
                   System.out.println("ChildFrame " + childFrame[i] + " is Visible");
                   formObject.setTop(childFrame[i], prevTop + prevHeight);

                   prevTop = formObject.getTop(childFrame[i]);
                   prevHeight = formObject.getHeight(childFrame[i]) + 2;
                   System.out.println("ChildFrame " + childFrame[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               } else {
                   System.out.println("ChildFrame " + childFrame[i] + " is InVisible");
                   for (String childToVis : childFrameToVis) {
                       if (childFrame[i].equalsIgnoreCase(childToVis)) {
                           formObject.setVisible(childToVis, true);
                           formObject.setTop(childToVis, prevTop + prevHeight);
                           prevTop = formObject.getTop(childToVis);
                           prevHeight = formObject.getHeight(childToVis) + 2;
                           System.out.println("ChildFrame To Visible " + childToVis + " prevTop " + prevTop + " prevHeight " + prevHeight);

                       } else {
                           continue;
                       }
                   }
               }

           }
           System.out.println("parentFrame " + parentFrame + " to be set prevTop " + prevTop + " prevHeight " + prevHeight);
           formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
       }

       if (allframes.length > 0 && frag.length > 0) {

           int previousTop = formObject.getTop(allframes[0]);
           int previousHeight = 0;
           int j = 0;
           //flag = 0;
           int fragHeight = 0;
           System.out.println(allframes[0] + " " + formObject.isVisible(allframes[0]) + " previousTop " + previousTop);
           for (int i = 0; i < (allframes.length) && j < (frag.length); i++, j++) {
               if (formObject.isVisible(allframes[i])) {
                   formObject.setTop(allframes[i], previousTop + previousHeight);

                   fragHeight = formObject.getHeight(frag[j] + "_Frame1");
                   System.out.println("fragHeight: " + fragHeight);
                   fragHeight += 1;
                   System.out.println("fragHeight: " + fragHeight);
                   formObject.setHeight(allframes[i], fragHeight);

                   previousTop = formObject.getTop(allframes[i]);
                   previousHeight = formObject.getHeight(allframes[i]) + 2;
                   System.out.println("previousTop: " + previousTop);
                   System.out.println("previousHeight: " + previousHeight);
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
       System.out.println("LoadPickList Query for " + controlname + ":"
               + query);
       List<String> mylist=new ArrayList<String>();
       mylist.add(controlname);
       formObject.getDataFromDataSource(query, mylist);
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
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "recordList1:" + recordList1);

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

           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "endURI:" + endURI);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "username:" + username);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "password:" + password);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "cab_name:" + cab_name);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "namespace:" + namespace);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "namespaceUri:" + namespaceUri);

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

           // System.out.println("Executing query");
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "Executing query for Generic Web Service Invoker");

           //ResultSet rs2 = stmt.executeQuery(query2);
           recordList2 = formObject.getNGDataFromDataCache(query2);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "recordList2:" + recordList2);

           String XML = "";

           XML = inputXml;

           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "XML is" + XML);

            XML= "<?xml version=\"1.0\"?><APWebService_Input><Option>APWebservice</Option><Calltype>Rlos_Eigblity_Call</Calltype><EngineName>rloscab</EngineName><SessionId>-2145567285</SessionId><employerStatus>Approved</employerStatus><incomeType>Salaried</incomeType><nationality>Home</nationality><royalFamilyMember>Yes</royalFamilyMember><salary>45000</salary><sourceOfRepayment>Within Bank</sourceOfRepayment></APWebService_Input>";
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "Creating tags from the table");
           System.out.println("Creating tags from the table");
           //((String) ((List) recordList1.get(0)).get(2))
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "Size of recordList2:" + recordList2.size());

           for (List<String> recordlist : recordList2) {
               SKLogger.writeLog("RLOSCommon execBRMSWebService()", "List<String> recordlist:" + recordlist.toString());

               if (recordlist.get(2).equals("I") && recordlist.get(6).equals("Y")) {
                   javax.xml.soap.Name fieldTag = soapFactory.createName(recordlist.get(3), namespace, namespaceUri);
                   javax.xml.soap.SOAPElement fieldEle = input_EligibilityobjEle.addChildElement(fieldTag);
                   // fieldEle.addTextNode(rs2.getString(6));
                   //When data will come from the XML then
                   fieldEle.addTextNode(XML.substring(XML.indexOf(recordlist.get(3)) + recordlist.get(3).length() + 1, XML.indexOf("</" + recordlist.get(3))));

               }

           }
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "Setting the URI");
           URL endpoint = new URL(endURI);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "endpoint :" + endpoint);
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "Request Sent");

           message.writeTo(System.out);

           javax.xml.soap.SOAPMessage response = connection.call(message, endpoint);
           connection.close();
           System.out.print("\nResponse Sent");
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "nResponse Request :");
           response.writeTo(System.out);
           System.out.println();

           //Response String
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           response.writeTo(out);
           strResponseMsg = new String(out.toByteArray());
           SKLogger.writeLog("RLOSCommon execBRMSWebService()", "Setting strResponseMsg URI" + strResponseMsg);
           System.out.println("strResponseMsg" + strResponseMsg);
       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return strResponseMsg;
*/
   }


