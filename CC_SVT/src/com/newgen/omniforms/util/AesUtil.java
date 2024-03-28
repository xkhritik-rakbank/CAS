package com.newgen.omniforms.util;
import java.io.File;
import java.io.FileInputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;


public class AesUtil {

  private static final String IV =   "00000000000000000000000000000000";
  private static final String SALT = "00000000000000000000000000000000";
 
  private static final int KEY_SIZE = 128;
  private static final int ITERATION_COUNT = 10000;
  private static final String PASSPHRASE = "THISISASECRETKEY";

 
  /*public static String Encrypt(String PLAIN_TEXT) {
      AesUtilHelper util = new AesUtilHelper(KEY_SIZE, ITERATION_COUNT);
      String encrypt = util.encrypt(SALT, IV, PASSPHRASE, PLAIN_TEXT);        
      System.out.println(encrypt);
      return encrypt;
  }
  
 
  public String  Decrypt(String CIPHER_TEXT) {
      AesUtilHelper util = new AesUtilHelper(KEY_SIZE, ITERATION_COUNT);
      String decrypt = util.decrypt(SALT, IV, PASSPHRASE, CIPHER_TEXT);
		System.out.println(decrypt);
      return decrypt;
  }*/
  
  public static String User_Decrypt() 
	{
	  String sdata="";
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
			System.out.println("bytesRead "+ bytesRead);	   
			byte[] encodedKeySpec = new byte[bytesRead];
			
			System.out.println("encodedKeySpec "+ encodedKeySpec);
			
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
			
			while (i != -1) 
			{
				sdata=sdata+(new String(b));
				i = cis.read(b);
				 System.out.println("sdata "+ sdata);
				 System.out.println("i "+ i);
			}
			
			cis.close();
			fis.close();
			return sdata;
		}
		catch(Exception e)
		{
			System.out.println("Error during reading files = " + e);
			e.printStackTrace();				
		}
		return sdata;
	}

  public static void main(String args[])
  {
	 
	//String PLAIN_TEXT = "Karan bhati5546370220955089";
  	//System.out.println(PLAIN_TEXT);
  	String CIPHER_TEXT = User_Decrypt();
  	//CIPHER_TEXT = "wRVrc4DZU2d660vBpauTSA==";
  	System.out.println("Decrypt  "+CIPHER_TEXT);
  	//String Decrypt_text = Decrypt(CIPHER_TEXT);
  	//System.out.println("Decrypt  "+Decrypt_text);*/
  }
}

