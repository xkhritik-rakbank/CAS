//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group							: Application –Projects 
//Product / Project				: WorkFlow
//Module						: 
//File Name						: ExtDBLog.java	
//Author						: Ankit Gupta
//Date written (DD/MM/YYYY)		: 
//Description					: Used to Write Log for WFCustomBean
//----------------------------------------------------------------------------------------------------
//			CHANGE HISTORY
//----------------------------------------------------------------------------------------------------
// Date			 Change By	 Change Description (Bug No. (If Any))
// (DD/MM/YYYY)
//----------------------------------------------------------------------------------------------------
//
//----------------------------------------------------------------------------------------------------

package com.newgen.omni.jts.txn.cust;

import java.io.*;
import java.text.*;

public class ExtDBLog
    extends PrintStream {
  private PrintStream out;
  private String name;
  private long cur_size;
  private long max_size;
  private int max_nos;
  private int nfile;
  public static ExtDBLog ExtDBLogFile;

  public static void writeLog(String strOutput) {
    StringBuffer str = new StringBuffer();
    str.append(DateFormat.getDateTimeInstance(0, 2).format(new java.util.Date()));
    str.append("\n");
    str.append(strOutput);
    str.append("\n");
    ExtDBLogFile.println(str.toString());
  }

//  private static ExtDBLog ExtDBLogFile;

  public ExtDBLog(String fileName) throws FileNotFoundException {
    super(new FileOutputStream(fileName + "_" + 1 + ".log"), true);
    out = null;
    cur_size = 0;
    max_nos = 5; //Max numbers of Log file
    nfile = 1;
    max_size = 2097152; //Max Size of Log file in Bytes
    out = this;
    name = fileName;
    nfile = nfile < max_nos ? ++nfile : 1;
    cur_size = 0;
  }

  public void println(String x) {
    if (x != null) {
      cur_size += x.length();
      if (cur_size > max_size) {
        try {
          out.close();
          out = new PrintStream(new FileOutputStream(name + "_" + nfile +
              ".log"), true);
          nfile = nfile < max_nos ? ++nfile : 1;
          cur_size = x.length() + 1;
        }
        catch (FileNotFoundException filenotfoundexception) {}
      }
      out.print(x);
      out.println();
      out.flush();
    }
  }

  public void println(char c[]) {
    cur_size += c.length;
    if (cur_size > max_size) {
      try {
        out.close();
        out = new PrintStream(new FileOutputStream(name + "_" + nfile + ".log"), true);
        nfile = nfile < max_nos ? ++nfile : 1;
        cur_size = c.length;
      }
      catch (FileNotFoundException filenotfoundexception) {}
    }
    out.print(c);
    out.println();
    out.flush();
  }

  private static void initilizeLog() 
  {
    StringBuffer strFilePath = null;
    try {
      strFilePath = new StringBuffer(25);
      strFilePath.append(System.getProperty("user.dir"));
      strFilePath.append(File.separatorChar);
      strFilePath.append("CustomLog");
      strFilePath.append(File.separatorChar);
      strFilePath.append("ExtDBLog");
      System.out.println(strFilePath.toString());
      ExtDBLogFile = initializeFile(strFilePath.toString());

    }
    catch (Exception exception) {}
    finally 
    {
      strFilePath = null;
    }
  }

  public static ExtDBLog initializeFile(String fileName) {
    ExtDBLog fileWriter = null;
    StringBuffer strBuff = new StringBuffer();
    strBuff.append("\n*************************************************************\nLog is created On: ");
    strBuff.append(DateFormat.getDateTimeInstance(0,
                   2).format(new java.util.Date()));
    try {
      fileWriter = new ExtDBLog(fileName);
      fileWriter.println(strBuff.toString());
    }
    catch (Exception exception) {}
    finally {
      fileName = null;
      strBuff = null;
      return fileWriter;
    }
  }

  static 
  {
    StringBuffer strFilePath = new StringBuffer(25);
    strFilePath.append(System.getProperty("user.dir"));
    File dir = new File(strFilePath.toString(), "CustomLog");
    if (!dir.exists()) {
      dir.mkdir();
    }
    strFilePath.append(File.separatorChar);
    strFilePath.append("CustomLog");
    strFilePath.append(File.separatorChar);
    try {
      System.setOut(new ExtDBLog(strFilePath.toString() + "CustomLog"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      strFilePath = null;
      dir = null;
    }
    initilizeLog();
  }

}
