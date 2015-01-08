package com.vicherarr.localizadorgsm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.util.Log;

public class Dialogo {
	private static final long RETARDOCERRARNOTIFICACION = 15000;

	AlertDialog.Builder builder;

	public AlertDialog alertdialog;
	
	public Dialog mostrar(final Context c, String titulo, String mensaje)
	{
	    //Cancela dialogos anteriores.. demasiados unos encima de otros se vuelve pesado
		
		builder = new AlertDialog.Builder(c);
	 
	    builder.setTitle(titulo);
	    builder.setMessage(mensaje);
	    
	   
	    
	    
			builder.setPositiveButton("Continuar", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					// TODO Apéndice de método generado automáticamente
					
				}});
			
			 
		
			
			/*
			builder.setNegativeButton("Configuración de gasto", new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					// TODO Apéndice de método generado automáticamente
					Intent intentconfgasto=new Intent(Main.INTENT_CONFIGURACION_GASTO);
					 c.startActivity(intentconfgasto);
				}}); 
           */    
	    
	    
	  
		alertdialog=builder.show();
	    
	    
	    
	    new Handler().postDelayed(new Runnable() {
          	public void run() {
            
          		try {
        			alertdialog.dismiss();
        		}
        		catch (Exception e){Log.i("Cancelando dialogo saldo", "No hay dialogo saldo que cancelar o se produjo un error al hacerlo");}
        		
        
          		          	    
          	
          	};
          	}, RETARDOCERRARNOTIFICACION);
	    
	    
	    return builder.create();
	}
    
	

}
