package com.vicherarr.localizadorgsm;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class HttpPostRuta extends AsyncTask<String, Void, String> 
{       
  public Context contexto;
private ProgressDialog pd1;
    AlertDialog alertdialog;


    public HttpPostRuta(Context context)
  {
	  contexto=context;
  }
  
  @Override
  protected void onPreExecute(){
	pd1=new ProgressDialog(contexto);
    pd1.setMessage("Cargando ruta. Espere por favor");
    pd1.show(); 
      
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
		        	//result=new UtilesWeb().gtsRequest(url, body[i]);
                   result=new UtilesWeb().gtsRequestSSL(HttpPost.urlSSL, HttpPost.targetDomain, body[i]);
        
        } catch(Exception e) {
            
        } 
        return  result;
    }

    protected void onPostExecute(String xml)
    {       
    	String pruebas="";
        
    	
    	try {

			Main.listmarkeroptionsruta= new Utiles().getposxmlruta(contexto,xml);
            if (Main.listmarkeroptionsruta.isEmpty()) {
                new Dialogo().mostrar(contexto, "No hay ruta", "No existe ruta en la fecha " + Main.adia + "/" + (Main.ames+1) + "/" + Main.aanio);
                //Toast.makeText(contexto,"No existe ruta en la fecha seleccionada",Toast.LENGTH_LONG).show();
            }
			Main.handle_dibujar_ruta.sendEmptyMessage(0);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
    	
        Toast toast = Toast.makeText(contexto, pruebas , Toast.LENGTH_SHORT);
      //toast.show();
    	pd1.dismiss();
        
    }   
}   
