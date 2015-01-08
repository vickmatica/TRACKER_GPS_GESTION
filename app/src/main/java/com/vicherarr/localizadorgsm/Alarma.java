/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * 
 */
package com.vicherarr.localizadorgsm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Alarma extends Activity {
TextView textoalarma;
Button botonsalir;

	 private String mensaje;

	
	 @Override 
	 public void onResume(){
	 super.onResume();
	
	 }
	 
	 
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.alarma);
	        
	        textoalarma=(TextView)findViewById(R.id.textViewMensajeAlarma);
	
	        botonsalir=(Button)findViewById(R.id.botonsalir);
	        
	        
	       
				
			botonsalir.setOnClickListener( new OnClickListener(){

				public void onClick(View view) {
					finish();
				}});

	        
            Bundle extras = getIntent().getExtras();
	        
	        if(extras !=null)
	        {
	        mensaje=extras.getString(ServicioRecepcion.KEY_MENSAJE);
		    textoalarma.setText(mensaje);       
	        }
	 } 
}