/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * 
 */
package com.vicherarr.localizadorgsm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfiguracionAvanzada extends Activity {

Button botonguardarconfavanzada;

Context contexto;
	
EditText etinicializar, etautorizar, etdesautorizar, etmovimientoon, etmovimientooff;
EditText etrastreoon, etrastreooff, etexcesovelocidadon, etexcesovelocidadoff;
EditText  etmodovozon, etmodovozoff, etlimitarareaon, etlimitarareaoff, etordenlatitud, etordenlongitud;
EditText etseparadorcoord;

	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.configuracion_avanzada);
	        
	        contexto=this;
	        botonguardarconfavanzada=(Button)findViewById(R.id.buttonGuardarConfAvanzada);
	        
	        etinicializar=(EditText)findViewById(R.id.editTextconfInicial);
	        etautorizar=(EditText)findViewById(R.id.editTextAutorizar);
	        etdesautorizar=(EditText)findViewById(R.id.editTextDesautorizar);
	        etmovimientoon=(EditText)findViewById(R.id.editTextActivarMovimiento);
	        etmovimientooff=(EditText)findViewById(R.id.editTextDesactivarDetectarMovimiento);
	        etrastreoon=(EditText)findViewById(R.id.editTextRastreoActivar);
	        etrastreooff=(EditText)findViewById(R.id.editTextRastreoDesactivar);
	        etexcesovelocidadon=(EditText)findViewById(R.id.editTextActivarDeteccionVelocidad);
	        etexcesovelocidadoff=(EditText)findViewById(R.id.editTextdesactivarexcesovelocidad);
	        etmodovozon=(EditText)findViewById(R.id.editTextActivarModoEscucharVoz);
	        etmodovozoff=(EditText)findViewById(R.id.editTextDesactivarModoEscucharVoz);
	        etlimitarareaon=(EditText)findViewById(R.id.editTextActivarLimitarSuperficie);
	        etlimitarareaoff=(EditText)findViewById(R.id.editTextDesactivarLimitSuperficie);
	        etordenlatitud=(EditText)findViewById(R.id.editTextOrdenPosLatitud);
	        etordenlongitud=(EditText)findViewById(R.id.editTextOrdenPosLongitud);
	        etseparadorcoord=(EditText)findViewById(R.id.editTextseparadorcoordenadas);;
	        
	        
	        etinicializar.setText(Main.leer(Main.KEY_SMS_INICIALIZAR));
	        etautorizar.setText(Main.leer(Main.KEY_SMS_AUTORIZAR));
	        etdesautorizar.setText(Main.leer(Main.KEY_SMS_DESAUTORIZAR));
	        etmovimientoon.setText(Main.leer(Main.KEY_SMS_DETECCIONMOVIMIENTO_ON));
	        etmovimientooff.setText(Main.leer(Main.KEY_SMS_DETECCIONMOVIMIENTO_OFF));
	        etrastreoon.setText(Main.leer(Main.KEY_SMS_AUTORASTREO_ON));
	        etrastreooff.setText(Main.leer(Main.KEY_SMS_AUTORASTREO_OFF));
	        etexcesovelocidadon.setText(Main.leer(Main.KEY_SMS_EXCESOVELOCIDAD_ON));
	        etexcesovelocidadoff.setText(Main.leer(Main.KEY_SMS_EXCESOVELOCIDAD_OFF));
	        etmodovozon.setText(Main.leer(Main.KEY_SMS_MODOVOZ_ON));
	        etmodovozoff.setText(Main.leer(Main.KEY_SMS_MODOVOZ_OFF));
	        etlimitarareaon.setText(Main.leer(Main.KEY_SMS_LIMITARAREA_ON));
	        etlimitarareaoff.setText(Main.leer(Main.KEY_SMS_LIMITARAREA_ON));
	        etordenlatitud.setText(Integer.valueOf(Main.leerint(Main.KEY_ORDENLATITUD)).toString());
	        etordenlongitud.setText(Integer.valueOf(Main.leerint(Main.KEY_ORDENLONGITUD)).toString());
	        etseparadorcoord.setText(Main.preferencias.getString(Main.KEY_SEPARADORCOORDENADAS, " "));
	        
			botonguardarconfavanzada.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
				
				boolean a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r;
					
				a=Main.guardar(Main.KEY_SMS_INICIALIZAR, etinicializar.getText().toString());
				b=Main.guardar(Main.KEY_SMS_AUTORIZAR, etautorizar.getText().toString());
				c=Main.guardar(Main.KEY_SMS_DESAUTORIZAR, etdesautorizar.getText().toString());
				d=Main.guardar(Main.KEY_SMS_DETECCIONMOVIMIENTO_ON, etmovimientoon.getText().toString());
				e=Main.guardar(Main.KEY_SMS_DETECCIONMOVIMIENTO_OFF, etmovimientooff.getText().toString());
				f=Main.guardar(Main.KEY_SMS_AUTORASTREO_ON, etrastreoon.getText().toString());
				g=Main.guardar(Main.KEY_SMS_AUTORASTREO_OFF, etrastreooff.getText().toString());
				h=Main.guardar(Main.KEY_SMS_EXCESOVELOCIDAD_ON, etexcesovelocidadon.getText().toString());
				i=Main.guardar(Main.KEY_SMS_EXCESOVELOCIDAD_OFF, etexcesovelocidadoff.getText().toString());
				j=Main.guardar(Main.KEY_SMS_MODOVOZ_ON, etmodovozon.getText().toString());
				k=Main.guardar(Main.KEY_SMS_MODOVOZ_OFF, etmodovozoff.getText().toString());
				l=Main.guardar(Main.KEY_SMS_LIMITARAREA_ON, etlimitarareaon.getText().toString());
				m=Main.guardar(Main.KEY_SMS_LIMITARAREA_OFF, etlimitarareaoff.getText().toString());
				
						
				int ordenlatitud = Main.ORDENLATITUD, ordenlongitud=Main.ORDENLONGITUD;
				try{
				ordenlatitud = Integer.parseInt(etordenlatitud.getText().toString());
				ordenlongitud = Integer.parseInt(etordenlongitud.getText().toString());
				}
				catch (java.lang.NumberFormatException numberexception){Toast.makeText(contexto, "Formato de orden de latitud o de orden de longitud incorrecto", Toast.LENGTH_LONG).show();}
				
				n=Main.guardar(Main.KEY_ORDENLATITUD, ordenlatitud);
				o=Main.guardar(Main.KEY_ORDENLONGITUD,ordenlongitud);
				p=Main.guardar(Main.KEY_SEPARADORCOORDENADAS, etseparadorcoord.getText().toString());
				q=Main.guardar(Main.KEY_SMS_MODOVOZ_ON, etmodovozon.getText().toString());
				r=Main.guardar(Main.KEY_SMS_MODOVOZ_OFF, etmodovozoff.getText().toString());
					
				
				if (a && b && c && d && e && f && g && h && i && j && k && l && m && n && o && p && q && r) 
				{
				 Toast.makeText(v.getContext(), v.getResources().getString(R.string.configuracionguardada) , Toast.LENGTH_SHORT).show();
				 finish();
				}
				else Toast.makeText(v.getContext(), "Error: No se pudo guardar la configuración", Toast.LENGTH_LONG).show();
					
				}});
	        
	
	}



	


}
