package com.vicherarr.localizadorgsm;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ServicioRecepcion extends Service {

private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
public static final String KEY_MENSAJE = "MENSAJEALARMA";
public static final String KEY_MENSAJE2 = "MENSAJEALARMANOTIFICACION";
private SMSReceiver yourReceiver;


//Lista de alarmas
public static AlarmManager alarmmanager;	
	
		
	
	@Override
	public void onCreate(){
	super.onCreate();
    
	
	
	Main.preferencias = getSharedPreferences(Main.PREFERENCIAS,Context.MODE_PRIVATE);
	
	final IntentFilter Filtro = new IntentFilter();
    Filtro.addAction(ACTION);
	
	yourReceiver=new SMSReceiver();

    
    //Registra el broadcast para escuchar sms recibidos
    this.registerReceiver(yourReceiver, Filtro);
	
    
    
    new Utiles().resetalarmas(this);    
        
	
	
	
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Apéndice de método generado automáticamente
		
		
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	   
	    //Unicamente se parará cuando se le solicite explicitamente
	    		
		return START_STICKY;
		
	}

}
