package com.vicherarr.localizadorgsm;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class LanzarServicioBoot extends BroadcastReceiver {

	public static Intent intentservicio;
	//public static Intent intentservicioserversocket;

	
	@Override
	public void onReceive(Context contexto, Intent arg1) {
		// TODO Apéndice de método generado automáticamente
      intentservicio=new Intent(contexto,ServicioRecepcion.class); 
	  
      
      
	  contexto.startService(intentservicio);
	  //contexto.startService(intentservicioserversocket);
	  
	}

}
