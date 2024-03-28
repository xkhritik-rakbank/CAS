/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : Decrypt_key.java
Author                                                                        : Akshay
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.util;

import java.io.File;
import java.io.FileInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;


public class Decrypt_key {

  public  static String User_Decrypt() 
	{
	  String username="";
	 try 
		{
			System.out.println(System.getProperty("user.dir"));
			File desFile = new File("np.dat");
			
			FileInputStream fis;
			CipherInputStream cis;
			// Read the cipher settings
			File KEY_FILE=new File("key.data");
			FileInputStream eksis = new FileInputStream( KEY_FILE );
			byte[] temp = new byte[ (int)KEY_FILE.length()];
			int bytesRead = eksis.read(temp);
						   
			byte[] encodedKeySpec = new byte[bytesRead];
			System.arraycopy(temp, 0, encodedKeySpec, 0, bytesRead);

			// Recreate the secret/symmetric key
			SecretKeySpec secretKey = new SecretKeySpec( encodedKeySpec, "DES");
			// Creation of Cipher objects
			Cipher decrypt =
			Cipher.getInstance("DES/ECB/PKCS5Padding");
			decrypt.init(Cipher.DECRYPT_MODE, secretKey);
			// Open the Encrypted file
			fis = new FileInputStream(desFile);
			cis = new CipherInputStream(fis, decrypt);
			byte[] b = new byte[8];
			int i = cis.read(b);
			String sdata1="";
			while (i != -1) 
			{
				sdata1=sdata1+(new String(b));
				i = cis.read(b);
			}
			String sdataUpper=sdata1.toUpperCase();
			System.out.println("sdataUpper  "+sdataUpper);
			
			//Some extra value was appending in output so for that below code written
			String unInt="",unEnd="";
			unInt="<USERNAME>";
			unEnd="</USERNAME>";
			System.out.println("sdataUpper.indexOf(unInt)  "+sdataUpper.indexOf(unInt));
			System.out.println("sdataUpper.indexOf(unInt)  "+unInt.length());
		 	int o=sdataUpper.indexOf(unInt)+unInt.length();
			int p=sdataUpper.indexOf(unEnd);
			username=sdata1.substring(o,p);
			cis.close();
			fis.close();
			eksis.close();			
		}
		catch(Exception e)
		{
			System.out.println("Error during reading files = " + e);
			e.printStackTrace();				
		}
	 return username;
	}
/*
  public static void main(String args[])
  {
	 
	//String PLAIN_TEXT = "Karan bhati5546370220955089";
  	//System.out.println(PLAIN_TEXT);
	  //User userPwd=new User(selectedCabinet);
  	String key = User_Decrypt();
  	//CIPHER_TEXT = "wRVrc4DZU2d660vBpauTSA==";
  	System.out.println("Decrypt  "+key);
  	//String Decrypt_text = Decrypt(CIPHER_TEXT);
  	//System.out.println("Decrypt  "+Decrypt_text);*/
  }


