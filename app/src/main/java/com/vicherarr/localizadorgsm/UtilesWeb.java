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
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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
	 

	 public String gtsRequestSSL(String urlToSendRequest, String targetDomain, String xml){
		  String retorno="";




         DefaultHttpClient httpClient = new DefaultHttpClient();

         SchemeRegistry schemeRegistry = new SchemeRegistry();
         schemeRegistry.register(new Scheme("http",    PlainSocketFactory.getSocketFactory(), 8282));
         schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 8443));

         HttpParams params = new BasicHttpParams();
         params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
         params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(1));
         params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
         HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
         HttpProtocolParams.setContentCharset(params, "utf8");
         ClientConnectionManager cm =  new ThreadSafeClientConnManager(params, schemeRegistry);
         httpClient = new DefaultHttpClient(cm, params);

         HttpHost targetHost = new HttpHost(targetDomain, 8443, "https");
         // Using POST here
         HttpPost httpPost = new HttpPost(urlToSendRequest);
         // Make sure the server knows what kind of a response we will accept

         // Also be sure to tell the server what kind of content we are sending
         httpPost.addHeader("Content-Type", "application/xml");



         HttpClient client = new DefaultHttpClient();
         try {


             HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
             httpPost.setEntity(entity);

             CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
             //set the user credentials for our site "example.com"
             credentialsProvider.setCredentials(new AuthScope(targetDomain, AuthScope.ANY_PORT),
                     new UsernamePasswordCredentials("", ""));
             HttpContext context = new BasicHttpContext();
             context.setAttribute("http.auth.credentials-provider", credentialsProvider);

             HttpResponse response = httpClient.execute(httpPost);
             retorno = EntityUtils.toString(response.getEntity());


         } catch (Exception e) {
             e.printStackTrace();
         }



          return retorno;
		 }

	
}
