/**--------------------------------------------------------------------------
 *        NEWGEN SOFTWARE TECHNOLOGIES LIMITED
 *    Group                     : AP2
 *    Product / Project         : NEMF 3.2/RakBank Implementation
 *    Module                    : Server Module
 *    File Name                 : ServiceExecuter.java
 *    Author                    : Gaurav Sharma(gaurav-s)
 *    Date written (DD/MM/YYYY) : 21/06/2017
 *    Description               : This file deals with Asynch HTTP calls
 * --------------------------------------------------------------------------
 *                   CHANGE HISTORY
 * ---------------------------------------------------------------------------
 * Date           Name      		Comment
 * ---------------------------------------------------------------------------
 * 
 * ---------------------------------------------------------------------------
 */
package com.newgen.custom.rakbank.resources.concrete;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

//for asynch calling

public class ServiceExecuter implements  Callable<String> {
	String endPoint;
	String isProxyEnabled;
	String proxyIP;
	String proxyport;
	String proxyuser;
	String  proxypassword;

	public ServiceExecuter(String endPoint,String isProxyEnabled,String proxyIP,String proxyport,String proxyuser,String  proxypassword) {
		this.endPoint=endPoint;
		this.isProxyEnabled=isProxyEnabled;
		this.proxyIP=proxyIP;
		this.proxyport=proxyport;
		this.proxyuser=proxyuser;
		this.proxypassword=proxypassword;
	}

	@Override
	public String call() throws Exception {
		String output;
		StringBuffer result=new StringBuffer();
		HttpURLConnection conn = null; 
		Proxy proxy=null;
		URL url;
		final  String proxy_user=proxyuser;
		final   String  proxy_password=proxypassword;
		try {
			url = new URL(endPoint);

			if("true".equalsIgnoreCase(isProxyEnabled)){
				int sPort=Integer.parseInt(proxyport);
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP,sPort));
				Authenticator authenticator = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return (new PasswordAuthentication(proxy_user,proxy_password.toCharArray()));
					}
				};

				Authenticator.setDefault(authenticator);
				conn = (HttpURLConnection) url.openConnection(proxy);
			}
			else{
				conn = (HttpURLConnection) url.openConnection();	  
			}

			if(endPoint.toLowerCase().contains("https:")){
				ByPassSSL bypassSSLObject=new ByPassSSL();
				bypassSSLObject.setAcceptAllVerifier((HttpsURLConnection)conn);
			}
			for (int i = 0; i < 5; ++i) {
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/json");
				OutputStream os = conn.getOutputStream();
				os.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				while ((output = br.readLine()) != null) { 				
					result.append(output.toString());

				}
				br.close();
				if(result.toString()!=null||!result.toString().isEmpty() ){
					break;
				}

			}

		} catch (MalformedURLException e) {
			result.append("MalformedURLException");
			e.printStackTrace();

		} catch (IOException e) {
			result.append("IOException");
			e.printStackTrace();

		}
		catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{

			conn.disconnect();
		}
		// 	 System.out.println(result);
		return result.toString();
		//return null;
	}

	//	public static void main(String args[]){
	//		   final ExecutorService service;
	//	         Future<String>  task;
	//
	//	        service = Executors.newFixedThreadPool(5); 
	//	       
	//	        try {
	//	        	 for(int i=0;i<5;i++){
	//	        		 ServiceExecuter obj=new ServiceExecuter("http://localhost:8002/info","false","192.168.55.218","8080","gaurav-s","stronger123#");
	//	     	        task  = service.submit(obj);
	//	     	       
	//	        		 final String str;
	//	                 // waits the 10 seconds for the Callable.call to finish.
	//	                 str = task.get(); // this raises ExecutionException if thread dies
	//	                 System.out.println(str + " RECORDED "+i);
	//	             }
	//	           
	//	        } catch(final InterruptedException ex) {
	//	            ex.printStackTrace();
	//	        } catch(final ExecutionException ex) {
	//	            ex.printStackTrace();
	//	        }
	//	        service.shutdownNow();
	//	    }


}
