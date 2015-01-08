package com.vicherarr.localizadorgsm;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;



public class UtilesWeb {

	//Lanza un post al servicio GTSRequest y returna la respuesta
	 public String gtsRequest(String url, String xml){
	  String retorno="";
	  
	  
	  
	  HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(url);
	    try {
	     
	    	post.setHeader("Content-Type", "application/xml");
	    	
	       
	        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
	        post.setEntity(entity);
	        HttpResponse response = client.execute(post);
	        retorno = EntityUtils.toString(response.getEntity());
	      

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  
		return retorno; 
	 }
	 

	 public String gtsRequestSSL(String url, String xml){
		  String retorno="";
		  
		  SchemeRegistry schemeRegistry = new SchemeRegistry();
		  schemeRegistry.register(new Scheme("https", 
		              SSLSocketFactory.getSocketFactory(), 8181));

		  HttpParams params = new BasicHttpParams();

		  SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);

		  		  
		  HttpClient client = new DefaultHttpClient(mgr,params);
		    HttpPost post = new HttpPost(url);
		    try {
		     
		    	post.setHeader("Content-Type", "application/xml");
		    	
		       
		        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
		        post.setEntity(entity);
		        HttpResponse response = client.execute(post);
		        retorno = EntityUtils.toString(response.getEntity());
		      

		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  
			return retorno; 
		 }

	
}
