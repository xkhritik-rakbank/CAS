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


import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.PersonalLoanS;
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

import org.apache.log4j.Logger;

public class Common extends CommonUtilityMethods {
	private static final long serialVersionUID = 1L;
	Logger mLogger=PersonalLoanS.mLogger;

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
               SimpleDateFormat format;

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
                	   mLogger.info(e.getMessage());
                   }

               }

               if (flag == 0) {
                   return null;
               }

               calendar.setLenient(false);
               calendar.setTime(date);
           } else {
               return null;
           }

       } catch (Exception e1) {
    	   mLogger.info("Exception:" + e1);
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
      

       if (childFrameToExpand.equals("") && childFrameToVis.length == 0) {
           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;

               }
           }

           if (!parentFrame.equals("")) {
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
           }

       } else if (!childFrameToExpand.equals("") && childFrameToVis.length == 0) {
           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   if (childFrameCollapsible[i].equalsIgnoreCase(childFrameToExpand)) {
                       prevHeight = formObject.getHeight(childFrameCollapsible[i]);

                   } else {
                       prevHeight = 20;
                   }
                   //prevHeight = formObject.getHeight(childFrameCollapsible[i]) + 10;

               }
           }

           if (!parentFrame.equals("")) {
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
           }

       } else if (childFrameToExpand.equals("") && childFrameToVis.length > 0) {

           for (int i = 0; i < childFrameCollapsible.length; i++) {
               if (formObject.isVisible(childFrameCollapsible[i])) {
                   if (flag == 0) {
                       formObject.setTop(childFrameCollapsible[i], prevTop);
                       flag = 1;

                   } else {
                       formObject.setTop(childFrameCollapsible[i], prevTop + prevHeight + 2);
                   }

                   prevTop = formObject.getTop(childFrameCollapsible[i]);
                   //prevHeight = formObject.getHeight(childFrame[i]) + 10;

               } else {
                   for (String childToVis : childFrameToVis) {
                       if (childFrameCollapsible[i].equalsIgnoreCase(childToVis)) {
                           formObject.setVisible(childToVis, true);
                           formObject.setTop(childToVis, prevTop + prevHeight + 2);
                           prevTop = formObject.getTop(childToVis);
                           //prevHeight = formObject.getHeight(childToVis) + 10;

                       }
                   }
               }
           }

           if (!parentFrame.equals("")) {
               formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
           }
       }

       if (allframes.length > 0 && frag.length > 0) {

           int previousTop = formObject.getTop(allframes[0]);
           int previousHeight = 0;
           int j = 0;
           flag = 0;
           int fragHeight;
           for (int i = 0; i < (allframes.length) && j < (frag.length); i++, j++) {
               if (formObject.isVisible(allframes[i])) {
                   if (flag == 0) {
                       formObject.setTop(allframes[i], previousTop);
                       flag = 1;
                   } else {
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

       int prevTop = formObject.getTop(childFrame[0]);
       int prevHeight = 0;
       //int flag = 0;

       if (childFrameToVis.length == 0) {
           for (int i = 0; i < childFrame.length; i++) {
               if (formObject.isVisible(childFrame[i])) {
                   formObject.setTop(childFrame[i], prevTop + prevHeight);

                   prevTop = formObject.getTop(childFrame[i]);
                   prevHeight = formObject.getHeight(childFrame[i]) + 2;

               }
           }

           formObject.setHeight(parentFrame, prevTop + prevHeight + 2);

       } else {
           for (int i = 0; i < childFrame.length; i++) {
               if (formObject.isVisible(childFrame[i])) {
                   formObject.setTop(childFrame[i], prevTop + prevHeight);

                   prevTop = formObject.getTop(childFrame[i]);
                   prevHeight = formObject.getHeight(childFrame[i]) + 2;

               } else {
                   for (String childToVis : childFrameToVis) {
                       if (childFrame[i].equalsIgnoreCase(childToVis)) {
                           formObject.setVisible(childToVis, true);
                           formObject.setTop(childToVis, prevTop + prevHeight);
                           prevTop = formObject.getTop(childToVis);
                           prevHeight = formObject.getHeight(childToVis) + 2;

                       } else {
                           continue;
                       }
                   }
               }

           }
           formObject.setHeight(parentFrame, prevTop + prevHeight + 2);
       }

       if (allframes.length > 0 && frag.length > 0) {

           int previousTop = formObject.getTop(allframes[0]);
           int previousHeight = 0;
           int j = 0;
           //flag = 0;
           int fragHeight;
           for (int i = 0; i < (allframes.length) && j < (frag.length); i++, j++) {
               if (formObject.isVisible(allframes[i])) {
                   formObject.setTop(allframes[i], previousTop + previousHeight);

                   fragHeight = formObject.getHeight(frag[j] + "_Frame1");
                   fragHeight += 1;
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
	   try{
		   FormReference formObject = FormContext.getCurrentInstance()
	               .getFormReference();
		   PersonalLoanS.mLogger.info("Query to load data in field : "+controlname+" : "+query);
	       // formObject.getNGDataFromDataCache(arg0, arg1);
	       formObject.getNGDataFromDataCache(query, controlname);   
	   }
	   catch(Exception e){
		   PersonalLoanS.mLogger.info("Expetion occured in LoadPickList for control name: "+controlname+ " query: "+ query);
	   }
       
   }

   }



