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
import android.widget.EditText;
import android.widget.Toast;

public class ConfiguracionGasto extends Activity {
EditText edittextsaldominimo;
EditText edittextmesescaducidad;

Button botonguardar;
protected Integer mesescaducidad; 


	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.configuraciongasto);
	        
	        
	        this.edittextsaldominimo=(EditText)findViewById(R.id.editTextsaldominimo);
	        
	        this.edittextmesescaducidad=(EditText)findViewById(R.id.editTextmesescaducidad);
	   
	        
	        this.botonguardar=(Button)findViewById(R.id.botonguardargasto);
	        
	   
	        
	        float SALDOMINIMO=0; 

	       mesescaducidad=Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD, 4);
			
	        SALDOMINIMO=Main.preferencias.getFloat(Main.KEY_SALDOMINIMO_LOCALIZADOR, 1);
	    
	        
	         
	       
	        this.edittextsaldominimo.setText(Float.toString(SALDOMINIMO));
	       
	        this.edittextmesescaducidad.setText(Integer.toString(mesescaducidad));
	      
			this.botonguardar.setOnClickListener(new OnClickListener(){

				

				public void onClick(View v) {
					Float saldominimo=Float.valueOf(0);
					
		
				      
				      try{
				      
				      saldominimo=Float.parseFloat(edittextsaldominimo.getText().toString());
				      
				      
				      } catch (Exception e){e.printStackTrace();}
				      
				      try{
				    	  //mesescaducidad=Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD, 4);
				    	  mesescaducidad=Integer.parseInt(edittextmesescaducidad.getText().toString());
					 } catch (Exception e){e.printStackTrace();}
		 
				     
		             
				     boolean a=Main.preferencias.edit().putFloat(Main.KEY_SALDOMINIMO_LOCALIZADOR, saldominimo).commit();
		             boolean b=Main.preferencias.edit().putInt(Main.KEY_MESES_CADUCIDAD, mesescaducidad).commit();
				     
					     if (a && b) 
					     {
					      new Utiles().resetalarmas(v.getContext());
					      finish();
					     }
					     else Toast.makeText(v.getContext(), v.getContext().getResources().getString(R.string.error_guardar_configuracion) , Toast.LENGTH_LONG).show();
				}});
	 
	 } 
}