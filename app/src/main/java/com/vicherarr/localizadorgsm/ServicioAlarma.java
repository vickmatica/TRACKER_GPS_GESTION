package com.vicherarr.localizadorgsm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class ServicioAlarma extends Service {

	
		
	
	public static final String KEY_ID = "id_localizador";

	@Override
	public void onCreate(){
       
	
    
    
        
	
	
	
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Apéndice de método generado automáticamente
		
		
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	   
	    //Unicamente se parará cuando se le solicite explicitamente
	   
        Bundle extras = intent.getExtras();
        
        Intent intentalarma=new Intent(this,Alarma.class);
        
        if(extras !=null)
        {
        String mensaje=extras.getString(ServicioRecepcion.KEY_MENSAJE);
        String mensaje2=extras.getString(ServicioRecepcion.KEY_MENSAJE2);
        int id=extras.getInt(KEY_ID);
        intentalarma.putExtra(ServicioRecepcion.KEY_MENSAJE, mensaje);
        new Utiles().notificacion(this, this.getResources().getString(R.string.tarjetasimcaducarapronto), mensaje2, System.currentTimeMillis(), intentalarma, id);       
        Utiles.reproducirsonido(this, R.raw.campanas);
        }

		
		return START_STICKY;
		
	}

}
