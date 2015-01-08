/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * */
package com.vicherarr.localizadorgsm;
import com.vicherarr.localizadorgsm.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SMSReceiver extends BroadcastReceiver {

    
	private static final String SMS_RECIBIDO = "android.provider.Telephony.SMS_RECEIVED";
	
	public static final String NOMBRE_MENSAJE = "MENSAJE";

	protected static final String ID_MENSAJE = "ID_MENSAJE";

	public static final int NUMEROREGISTROSHISTORICO = 2000;

	public static final String NUMERO_MENSAJE = "NUMERO_MENSAJE";

	public static int NOTIFICAR_ID=1;

	public static NotificationManager mNotificationManager;

	private String numerosms;

	private String textosms;
	
	private Basedatos basedatos;
	
	
	@Override
	public void onReceive(Context _context, Intent _intent) {
	
	try {	
	
	Main.preferencias = _context.getSharedPreferences(Main.PREFERENCIAS,Context.MODE_PRIVATE);	
	
	Log.v("Recibido SMS ", "Recibido SMS");
		
	//Toast.makeText(_context, "Recibido un mensaje", Toast.LENGTH_LONG).show();
		
	  if (_intent.getAction().equals(SMS_RECIBIDO)) {

	   Bundle bundle = _intent.getExtras();
	   
			if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			
			for (int i = 0; i < pdus.length; i++)
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			
			
			for (SmsMessage message : messages) {
			textosms = message.getMessageBody();
			
			numerosms = message.getOriginatingAddress();
				
	        
			
			
				//****************************************
			    
				//***** CODIGO AL RECIBIR UN MENSAJE
				Log.v("Mensaje de ", numerosms);
				Log.v("Cuerpo del Mensaje", textosms);
				
				basedatos=new Basedatos(_context);
			    
				
				
			     
				if (basedatos.estaeltelefono(numerosms))
				{
				
			    Utiles.reproducirsonido(_context, R.raw.campanas);  
				//Float saldo=Main.preferencias.getFloat(Main.KEY_SALDOLOCALIZADOR,Float.valueOf(10));
			    
			    //Numero guardado en la bd, no es el mismo que se recibe porque el que se guarda en bd puede no tener prefijo
			    String numerosmsbd=basedatos.obtenernumero(numerosms);
			    
			    //Toast.makeText(_context, numerosmsbd, Toast.LENGTH_LONG).show();
			    
			    Float saldo=basedatos.consultarsaldo(basedatos.obtenernumero(numerosms));
				
			    //Toast.makeText(_context, "saldo" + Float.toString(saldo),Toast.LENGTH_LONG).show();
			    
				//Float pvpsms=Main.preferencias.getFloat(Main.KEY_PRECIOXSMSLOCALIZADOR,(float)0.1089);
			    Float pvpsms=basedatos.consultarpvpxsms(basedatos.obtenernumero(numerosms));
				
			    //Toast.makeText(_context, "pvpxsms" + Float.toString(pvpsms),Toast.LENGTH_LONG).show();
			    
			    
			    Float saldominimo=Main.preferencias.getFloat(Main.KEY_SALDOMINIMO_LOCALIZADOR,Float.valueOf(1));
				
			    //Registra el sms en el historial y borra los registros antiguos	
			    basedatos.insertar(textosms,numerosmsbd);
				basedatos.borrarultimosregistroshistorico(NUMEROREGISTROSHISTORICO);
				
				
				saldo=saldo-pvpsms;
				
				if (saldo<0) saldo=Float.valueOf(0);
				
			
				
				//if (Main.preferencias.edit().putFloat(Main.KEY_SALDOLOCALIZADOR, saldo).commit()) 
				if (basedatos.actualizarsaldo(numerosmsbd, saldo))
				{
				
					if (saldo<0) 
					{
					//Toast.makeText(_context, "Su saldo estimado esta a 0. Configurelo", Toast.LENGTH_LONG).show();
					}
				
					if (saldo<saldominimo) 
					{
				    //Toast.makeText(_context, "Le queda poco saldo. Recargue saldo en el localizador", Toast.LENGTH_LONG).show();
					}
				
				}
				else Toast.makeText(_context, "Error al actualizar el saldo en la base de datos", Toast.LENGTH_LONG).show();
			    //Evita desbordamiento
			    if (NOTIFICAR_ID==2147483646) NOTIFICAR_ID=1;
			    
				NOTIFICAR_ID+=1;
			    Log.v("Recibido mensaje del localizador", "Recibido mensaje del localizador");
				
			    Message msg=new Message();
			    

				


			    
				Object[] vector={basedatos.consultarultimahoraregistro(),textosms,numerosms};
				
				  //msg.obj=textosms;
				  msg.obj=vector;
								
						try{
						
						Main.handle.sendMessage(msg);
						
						
						}
						catch (Exception e){
						 Log.w("Excepción: sendMessage a handle","error sendMessage a handle cuando se recibe un mensaje"); 	
						}
						
				
				        mNotificationManager = (NotificationManager)_context.getSystemService(Context.NOTIFICATION_SERVICE);
							
						int icon = R.drawable.ic_launcher;
						CharSequence tickerText = _context.getResources().getString(R.string.smsrecibido);
						long when = System.currentTimeMillis();
						
						
						
						
						Notification notification =new Notification(icon, tickerText, when);
						
				
						
						
						
						
						// Uses the default lighting scheme
						notification.defaults |= Notification.DEFAULT_LIGHTS;

						// Will show lights and make the notification disappear when the presses it
						notification.flags |= Notification.FLAG_AUTO_CANCEL;
						
						notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
						
						
						
		
					
						
						
						Context context = _context.getApplicationContext();
						String contentTitle = _context.getResources().getString(R.string.smsrecibido);
						
						
						
						
						
						String contentText= basedatos.consultarnombre(basedatos.obtenernumero(numerosms));
						
						if (contentText==null) contentText="";
						
						contentText+=" " + Utiles.traducirentrada(textosms, _context);
						
												
						if (contentText.length()<2) contentText = textosms;
					    
						
						
						
						
						
						if (textosms.startsWith("move ok")) contentTitle=context.getResources().getString(R.string.activadodetectarmovimiento);
						//if (textosms.contains("move ok")) contentText="Su localizador se está moviendo";
						if (textosms.contains("monitor ok")) contentTitle=context.getResources().getString(R.string.escucharvozactivado);
						
						
						if (textosms.contains("tracker ok")) contentTitle=context.getResources().getString(R.string.modolocalizadoractivado);;
						
						if (textosms.contains("begin ok")) contentTitle=context.getResources().getString(R.string.reiniciadaconfiguracion);
						if (textosms.contains("help me")) contentTitle="S.O.S";
						//if (textosms.contains("help me")) contentText="Recibido 'Socorro' del localizador";
						
							if (textosms.contains("low battery")) 
							{contentTitle=context.getResources().getString(R.string.bajabateria);
							 contentText=context.getResources().getString(R.string.bateriabajarecargue);
							}
						if (textosms.contains("stockade!")) contentTitle=context.getResources().getString(R.string.fueradearea);
						if (textosms.contains("speed ok")) contentTitle=context.getResources().getString(R.string.activadolimitarvelocidad);	
	
						
						
						
						Intent notificationIntent = new Intent(Intent.ACTION_MAIN);
						notificationIntent.setClass(context, Main.class);
						
						
						
						
						
												
						//notificationIntent.putExtra("mensaje", textosms);
						notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						notificationIntent.putExtra(NOMBRE_MENSAJE, textosms);
						notificationIntent.putExtra(NUMERO_MENSAJE, numerosms);
						notificationIntent.putExtra(ID_MENSAJE, NOTIFICAR_ID);
						notificationIntent.putExtra("hora", basedatos.consultarultimahoraregistro());
						
						basedatos.cerrarbd();
						
						
						PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFICAR_ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
						
						
												
						
						notification.setLatestEventInfo(
						context, contentTitle,
						contentText, contentIntent);
						
						
						
						
						//mNotificationManager.cancelAll();
						
						
						
						
					   
						
						mNotificationManager.notify(textosms,NOTIFICAR_ID, notification);
				
			}
		 }
		 }
		  }
	 }
		 catch (Exception e){
		  Log.i("Error general SMSRECEIVER", "Error general smsreceiver");	 
		 }
	}
}
