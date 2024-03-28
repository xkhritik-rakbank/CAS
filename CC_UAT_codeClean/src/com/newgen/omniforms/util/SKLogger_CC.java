package com.newgen.omniforms.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SKLogger_CC {

    public static void writeLog(String className, String content) {
    	System.out.println("Deepak logger");
        String timeStamp = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss.SSS").format(Calendar.getInstance().getTime());
        int setSize = 10000000;
        String dirPath = System.getProperty("user.dir") + File.separatorChar;
        String fileName = "CC.log";
        String newline = System.getProperty("line.separator");
        File dir = new File(dirPath, "RLOSLogs");
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            dirPath = dirPath + "RLOSLogs" + File.separatorChar;
            // Get file size in bytes
            long size = getFileSize(dirPath + fileName);

            if (size > setSize) {
                moveFileEx(dirPath + fileName);
            }

            // Create file
            Writer output = null;
            File file = new File(dirPath + fileName);
            output = new BufferedWriter(new FileWriter(file, true));
            output.write(newline + timeStamp + "#class#" + className + "--> " + content);

            size = getFileSize(dirPath + fileName);
            output.close();

            output = null;
            file = null;
        } catch (IOException e) {
            //oh noes!
        } finally {
            timeStamp = "";
            dirPath = "";
            fileName = "";
            newline = "";
            dir = null;
        }
    }

    private static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            return -1;
        }
        return file.length();
    }

    private static void moveFileEx(String filename) {
        File f = new File(filename);
        String renamedFile = filename + "_1";
        File f2 = new File(renamedFile);
        if (f.exists()) {
            if (f2.exists()) {
                f2.delete();
            }
            f.renameTo(new File(renamedFile));
        }
        f = null;
        f2 = null;
        renamedFile = "";
    }
}
