package com.vicherarr.localizadorgsm;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class HttpPost extends AsyncTask<String, Void, String> 
{       
  public Context contexto;
  
  
  public HttpPost(Context context)
  {
	  contexto=context;
  }
  
  @Override
    protected String doInBackground(String... body)   
    {           
        String url = "http://vps119481.ovh.net:8080/servicio/Service";
        
        //url = "https://vps119481.ovh.net:8443/track1/Service";
        
        String result="";
        /*        
        String body="<GTSRequest command=\"eventdata\">\r\n" + 
        		"    <Authorization account=\"vickmatica\" user=\"admin\" password=\"orion\" />\r\n" + 
        		"    <EventData>\r\n" + 
        		"        <Device>touran</Device>\r\n" + 
        		"        <TimeFrom>2014/12/12</TimeFrom>\r\n" + 
        		"        <TimeTo>2014/12/12</TimeTo>\r\n" + 
        		"        <GPSRequired>false</GPSRequired>\r\n" + 
        		"        <Limit type=\"last\">10</Limit>\r\n" + 
        		"        <Ascending>true</Ascending>\r\n" + 
        		"        <!-- specific status code(s) -->\r\n" + 
        		"        <!-- <StatusCode>0xF403</StatusCode> -->\r\n" + 
        		"        <!-- <StatusCode>0xF112</StatusCode> -->\r\n" + 
        		"        <!-- fields retrieved -->\r\n" + 
        		"        <Field name=\"latitude\" />\r\n" + 
        		"        <Field name=\"longitude\" />\r\n" + 
        		"   </EventData>\r\n" + 
        		"</GTSRequest>\r\n" + 
        		"";
        */
        try {
        
		        for (int i=0;i<body.length;i++) 
		        	result=new UtilesWeb().gtsRequest(url, body[i]);
        
        } catch(Exception e) {
            e.printStackTrace();
            
        } 
        return  result;
    }

    protected void onPostExecute(String xml)
    {       
    	        
    	try {
			Main.listmarkeroptionsinternet= new Utiles().getposxml(contexto,xml);
			Main.handle_dibujar_servicioweb.sendEmptyMessage(0);
		} catch (Exception e){
			e.printStackTrace();
		}
		 
        
      
    }   
}   
