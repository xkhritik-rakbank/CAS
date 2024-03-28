/**--------------------------------------------------------------------------
 *        NEWGEN SOFTWARE TECHNOLOGIES LIMITED
 *    Group                     : AP2
 *    Product / Project         : NEMF 3.2/RAKBANK Implementation
 *    Module                    : Server Module
 *    File Name                 : Call.java
 *    Author                    : Gaurav Sharma
 *    Date written (DD/MM/YYYY) : 24/09/2016
 *    Description               : This Class deals with HTTP and HTTPS  calls
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import com.newgen.mcap.core.external.logging.concrete.LogMe;

public class Call {
	public String request(String endPoint,String isProxyEnabled,String proxyIP,String proxyport,String proxyuser,String  proxypassword){
		String output;
		StringBuffer result=new StringBuffer();
		HttpURLConnection conn = null; 
		Proxy proxy=null;
		URL url;

		//     String isProxyEnabled="true";
		//     String proxyport="8080"; 
		//     String proxyIP="192.168.55.218"; 
		final  String proxy_user=proxyuser;
		final   String  proxy_password=proxypassword;
		try {
			url = new URL(endPoint);

			if("true".equalsIgnoreCase(isProxyEnabled)){
				int sPort=Integer.parseInt(proxyport);
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP,sPort));
				Authenticator authenticator = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return (new PasswordAuthentication(proxy_user,
								proxy_password.toCharArray()));
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
				BufferedReader br = new BufferedReader(new InputStreamReader(

						(conn.getInputStream())));

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
			//e.printStackTrace();

		} catch (IOException e) {
			result.append("IOException");
			//e.printStackTrace();

		}
		catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		finally{
			conn.disconnect();
		}
		// 	 System.out.println(result);
		return result.toString();
	}

	public  String callHttpPost(String endPoint, String input,String isProxyEnabled,String proxyIP,String proxyport,String proxyUserId,String proxyPassword,String SoapAction,HashMap<String, String> headerprop) throws Exception {
		BufferedReader reader = null;
		String responseString = "";
		String outputString = "";
		final String proxyuser=proxyUserId;
		final String proxypassword=proxyPassword;
		Proxy proxy=null;
		HttpURLConnection httpConnection=null;
		URL url=new URL(endPoint);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\n INSIDE : = callHttpPost");
		if("true".equalsIgnoreCase(isProxyEnabled)){
			int sPort=Integer.parseInt(proxyport);
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP,sPort));
			Authenticator authenticator = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return (new PasswordAuthentication(proxyuser,
							proxypassword.toCharArray()));
				}
			};

			Authenticator.setDefault(authenticator);
			httpConnection = (HttpURLConnection) url.openConnection(proxy);
		}

		else{
			httpConnection = (HttpURLConnection) url.openConnection();
		}

		if(endPoint.toLowerCase().contains("http:")){

		}
		long startTime = System.currentTimeMillis();

		try {
			if(endPoint.toLowerCase().contains("https:")){
				//bypassSSL
				ByPassSSL bypassSSLObject = new ByPassSSL();
				bypassSSLObject.setAcceptAllVerifier((HttpsURLConnection) httpConnection);
				//bypass SSL end
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\nUrl to Hit: = "+endPoint);
			Set<String> keys=new HashSet<String>();
			if(headerprop!=null){
				keys=headerprop.keySet();
				input="";
				for(String key : keys){
					httpConnection.setRequestProperty(key, headerprop.get(key));
					input=input+key+"="+headerprop.get(key)+"&";
				}
				input=input.substring(0, input.lastIndexOf("&"));
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\nINPUT TO WRITE: = "+input);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[input.length()];
			buffer = input.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			//String SOAPAction = "";
			// Set the appropriate HTTP parameters.
			//CommonMethods.write//Log("getData() : Set the appropriate HTTP parameters");
			httpConnection.setReadTimeout(60000);
			httpConnection.setConnectTimeout(60000);
			httpConnection.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConnection.setRequestProperty("SOAPAction", SoapAction);
			httpConnection.setRequestMethod("POST");

			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			OutputStream out = httpConnection.getOutputStream();
			//CommonMethods.write//Log("getData() : Output stream created");
			//Write the content of the request to the outputstream of the HTTP Connection.
			out.write(b);
			out.close();
			//Ready with sending the request.
			//Read the response.
			InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
			//CommonMethods.write//Log("getData() : Read the response");
			BufferedReader in = new BufferedReader(isr);
			//Write the SOAP message response to a String.
			//CommonMethods.write//Log("getData() : Write the SOAP message response to a String");
			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
			//return outputString;

			//CommonMethods.write//Log("getData() : SOAP message response to a String written");
			//Log.e("Respnse",outputString);
			//((MainActivity)JavascriptCallbackHandler.cordovaInstance.getActivity()).writeToFile("FlowMessage","Response of Aadhar>>"+outputString);
			//System.out.println(outputString);
			//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
		} catch (IOException ex) {
			System.out.println(ex);

			if (null != reader) {
				reader.close();
			}
		}

		if (outputString != null) {
			//              break;
		}
		//    }
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\n time taken"+elapsedTime);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\nOUTPUT"+outputString);
		return outputString;
	}

	public  String postCall(String ednpoint,HashMap<String, String> inputparammap){
		String output="";
		try{
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\n INSIDE : = postCall");
			URL url = new URL(ednpoint);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\n ENDPOINT : = "+ednpoint);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			Set<String> keys=new HashSet<String>();
			String input="";
			if(inputparammap!=null){
				keys=inputparammap.keySet();
				// input="";
				for(String key : keys){
					input=input+key+"="+inputparammap.get(key)+"&";
				}
				input=input.substring(0, input.lastIndexOf("&"));
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"\n INPUT FOR CALL : = "+input);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(input);
			writer.flush();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				output=output+line;
			}
			writer.close();
			reader.close();

		}
		catch(Exception e){
			//e.printStackTrace();
		}
		return output;
	}
}
