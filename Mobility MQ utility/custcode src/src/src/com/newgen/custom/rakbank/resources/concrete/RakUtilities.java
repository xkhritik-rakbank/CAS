/**--------------------------------------------------------------------------
 *        NEWGEN SOFTWARE TECHNOLOGIES LIMITED
 *    Group                     : AP2
 *    Product / Project         : NEMF 3.2/RAKBANK Implementation
 *    Module                    : Server Module
 *    File Name                 : RakUtilities.java
 *    Author                    : Gaurav Sharma(gaurav-s)
 *    Date written (DD/MM/YYYY) : 25/03/2017
 *    Description               : This Class contains utilities function to be used in Implementation
 * --------------------------------------------------------------------------
 *                   CHANGE HISTORY
 * ---------------------------------------------------------------------------
 * Date           Name      		Comment
 * ---------------------------------------------------------------------------
 * 
 * ---------------------------------------------------------------------------
 */
package com.newgen.custom.rakbank.resources.concrete;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.newgen.mcap.core.external.logging.concrete.LogMe;

public class RakUtilities {
	/**
	 * @author gaurav-s
	 * @param void
	 * @return active Connection object with nemf db datasource
	 */
	
	 public static  Connection getNewConnection(){

		  String dburl="jdbc:sqlserver://192.168.55.128:1433;user=login_ap;password=loginap1234#;database=rakcas_uat";
		  String dbdriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		  PreparedStatement pstmt =null;
		  Connection connection=null;
		  try{
			  Class.forName(dbdriver).newInstance();
		         connection = DriverManager.getConnection(dburl);
		         if(connection!=null){
		         }
		         else{
		         }
				  
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
		    
				
				  return connection;
	}
}
