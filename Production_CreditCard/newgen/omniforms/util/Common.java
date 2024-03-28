package com.newgen.omniforms.util;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.CreditCard;

public class Common extends CommonUtilityMethods {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
           if (!"".equalsIgnoreCase(input)) {
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
                	   CreditCard.logException(e);
                   }

               }

               if (flag == 0) {
                   return null;
               }

               calendar.setLenient(false);
               calendar.setTime(date);
           } else {
               CreditCard.mLogger.info("Empty Field");
               return null;
           }

       } catch (Exception e1) {
           CreditCard.mLogger.info("RLOSCommon convertToDate"+ "Exception:" + e1);
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
       CreditCard.mLogger.info("In Frame Collapsible Adjusment");
       int prevTop = formObject.getTop(childFrameCollapsible[0]);
       int prevHeight = 20;
       int flag = 0;
       CreditCard.mLogger.info(childFrameCollapsible[0] + " prevTop " + prevTop);
       CreditCard.mLogger.info("Child Frame To Expand " + childFrameToExpand);
       CreditCard.mLogger.info("Child Frame To Visible " + childFrameToVis.length);

       if (childFrameToExpand.equals("") && childFrameToVis.length == 0) {
           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   CreditCard.mLogger.info("Child Frame " + childFrameCollapsible[i] + " is Visible");
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " top setted");
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                       CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " moved up");
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;
                   CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               }
           }

           if (!parentFrame.equals("")) {
               CreditCard.mLogger.info("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
               CreditCard.mLogger.info("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }

       } else if (!childFrameToExpand.equals("") && childFrameToVis.length == 0) {
           CreditCard.mLogger.info("In 1 else frameCollapsibleAdjusment");
           CreditCard.mLogger.info("Frame To Expand " + childFrameToExpand);
           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   CreditCard.mLogger.info("Child Frame " + childFrameCollapsible[i] + " is Visible");
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " top setted");
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                       CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " moved up");
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   if (childFrameCollapsible[i].equalsIgnoreCase(childFrameToExpand)) {
                       prevHeight = formObject.getHeight(childFrameCollapsible[i]);

                   } else {
                       prevHeight = 20;
                   }
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;
                   CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               }
           }

           if (!parentFrame.equals("")) {
               CreditCard.mLogger.info("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
               CreditCard.mLogger.info("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }

       } else if (childFrameToExpand.equals("") && childFrameToVis.length > 0) {
           CreditCard.mLogger.info("In 2 else frameCollapsibleAdjusment");

           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " is Visible");
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrame[i]) + 10;
                   CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " prevTop " + prevTop + " prevHeight " + prevHeight);

               } else {
                   CreditCard.mLogger.info("ChildFrame " + childFrameCollapsible[i] + " is InVisible");
                   for (String childToVis : childFrameToVis) {
                       if (childFrameCollapsible[i].equalsIgnoreCase(childToVis)) {
                           formObject.setVisible(childToVis, true);
                           formObject.setTop(childToVis, prevTop + prevHeight + 2);
                           prevTop = formObject.getTop(childToVis);
                           //prevHeight = formObject.getHeight(childToVis) + 10;
                           CreditCard.mLogger.info("ChildFrame To Visible " + childToVis + " prevTop " + prevTop + " prevHeight " + prevHeight);

                       }
                   }
               }
           }

           if (!parentFrame.equals("")) {
               CreditCard.mLogger.info("parent frame" + parentFrame + " set to prevTop " + prevTop + " prevHeight " + prevHeight);
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
               CreditCard.mLogger.info("parent frame " + parentFrame + " height " + formObject.getHeight(parentFrame));
           }
       }

       if (allframes.length > 0 && frag.length > 0) {

           int previousTop = formObject.getTop(allframes[0]);
           int previousHeight = 0;
           int j = 0;
           flag = 0;
           int fragHeight = 0;
           CreditCard.mLogger.info(allframes[0] + " " + formObject.isVisible(allframes[0]) + " previousTop " + previousTop);
           for (int i = 0; i < (allframes.length) && j < (frag.length); i++, j++) {
               if (formObject.isVisible(allframes[i])) {
                   if (flag == 0) {
                       formObject.setTop(allframes[i], previousTop);
                       flag = 1;
                   } else {
                       formObject.setTop(allframes[i], previousTop + previousHeight);
                   }

                   fragHeight = formObject.getHeight(frag[j] + "_Frame1");
                   CreditCard.mLogger.info("fragHeight: " + fragHeight);
                   fragHeight += 5;
                   CreditCard.mLogger.info("fragHeight: " + fragHeight);
                   formObject.setHeight(allframes[i], fragHeight);

                   previousTop = formObject.getTop(allframes[i]);
                   previousHeight = formObject.getHeight(allframes[i]) + 2;
                   CreditCard.mLogger.info("previousTop: " + previousTop);
                   CreditCard.mLogger.info("previousHeight: " + previousHeight);
               }
           }

       }

   }




   public void LoadPickList(String controlname, String query) {
	      FormReference formObject = FormContext.getCurrentInstance()
	              .getFormReference();
	      CreditCard.mLogger.info("LoadPickList Query for " + controlname + ":"
	              + query);
	    List<String> control_Name=new ArrayList<String>();
	    control_Name.add(controlname);
	     formObject.getDataFromDataSource(query, control_Name);
	  }


   }


