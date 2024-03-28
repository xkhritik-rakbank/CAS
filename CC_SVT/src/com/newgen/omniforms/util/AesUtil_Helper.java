package com.newgen.omniforms.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
//import javax.xml.bind.DatatypeConverter;

 class AesUtilHelper {
    private final int keySize;
    private final int iterationCount;
    private final Cipher cipher;
 //   private static final int KEY_SIZE = 128;
  //  private static final int ITERATION_COUNT = 10000;
  //  private static final String PASSPHRASE = "THISISASECRETKEY";
	
    public AesUtilHelper(int keySize, int iterationCount) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (Exception e) {
            throw fail(e);
        }
    }
    
  /*  public String encrypt(String salt, String iv, String passphrase, String plaintext) {
        try {
            SecretKey key = generateKey(salt, passphrase);
          
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
            return base64(encrypted);
        }
        catch (UnsupportedEncodingException Exception e) {
			e.printStackTrace();
            throw fail(e);
        }
    }
    
    public String decrypt(String salt, String iv, String passphrase, String ciphertext) {
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }
    
    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        }
        catch (Exception e) {
        	e.printStackTrace();
            throw fail(e);
        }
    }
    
    private SecretKey generateKey(String salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }*/
    
   private static String Encrypt(String PLAIN_TEXT)
   {
	   
	   try
       {
           File file = new File("np.dat");
           //File file1 = new File(args[0]);   //---- String 1
			 String formobject = PLAIN_TEXT;
			 
			 System.out.println("formobject :" + formobject);
          // FileInputStream fileinputstream = null;
           
           ByteArrayInputStream fileinputstream = null;
           FileOutputStream fileoutputstream = null;
           CipherInputStream cipherinputstream = null;
           //String s = null;
           
               /*File file2 = new File(args[0]);			//---- String	2
               FileInputStream fileinputstream1 = new FileInputStream(file2);
               byte abyte0[] = new byte[(int)file2.length()];
               fileinputstream1.read(abyte0);
               s = new String(abyte0);
               System.out.println((new StringBuilder()).append("data from file---").append(s).toString());
               fileinputstream1.close();
           }
           catch(FileNotFoundException filenotfoundexception)
           {
               System.out.println("File not Found");
           }
           catch(IOException ioexception)
           {
               System.out.println("IOException :");
				ioexception.printStackTrace();
           }*/
           KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
           SecretKey secretkey = keygenerator.generateKey();
           byte abyte1[] = secretkey.getEncoded();
           SecretKeySpec secretkeyspec = new SecretKeySpec(abyte1, "DES");
           Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
           cipher.init(1, secretkeyspec);
			
			System.out.println("cipher :" + cipher);

			
			
			fileinputstream = new ByteArrayInputStream(formobject.getBytes());
			System.out.println("fileinputstream :" + fileinputstream);
  
           cipherinputstream = new CipherInputStream(fileinputstream, cipher);
           fileoutputstream = new FileOutputStream(file);
           byte abyte2[] = new byte[8];
           for(int i = cipherinputstream.read(abyte2); i != -1; i = cipherinputstream.read(abyte2))
               fileoutputstream.write(abyte2, 0, i);

           fileoutputstream.flush();
           fileoutputstream.close();
           cipherinputstream.close();
           fileinputstream.close();
           byte abyte3[] = secretkeyspec.getEncoded();
           System.out.println((new StringBuilder()).append("key:  ").append(new String(abyte3)).toString());
           File file3 = new File("key.data");
           FileOutputStream fileoutputstream1 = new FileOutputStream(file3);
           fileoutputstream1.write(abyte3);
           fileoutputstream1.close();
           
           return "encrypted";
         
       }
       catch(Exception exception)
       {
           System.out.println("last");
			exception.printStackTrace();
       }
	   
	  return "Nothing";
   }
   
   /* public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }*/
    
   /* public static String base64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }
    
    public static byte[] base64(String str) {
        return DatatypeConverter.parseBase64Binary(str);
    }
    
    public static String hex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
    
    public static byte[] hex(String str) {
        return DatatypeConverter.parseHexBinary(str);
    }*/
    
    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
    public static void main(String args[])
    {
  	 
  	String PLAIN_TEXT = "Newgen Rakbank";
    	System.out.println(PLAIN_TEXT);
    	String CIPHER_TEXT = Encrypt(PLAIN_TEXT);
    	//CIPHER_TEXT = "wRVrc4DZU2d660vBpauTSA==";
    	System.out.println("Encrypted  "+CIPHER_TEXT);
    	//String Decrypt_text = Decrypt(CIPHER_TEXT);
    	//System.out.println("Decrypt  "+Decrypt_text);*/
    }
}
