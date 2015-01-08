/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * 
 */
package com.vicherarr.localizadorgsm;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SeleccionarLocalizador extends Activity {

protected static final long RETARDO = 3000;



private static final int DATE_DIALOG_ID = 0;




//Variables utilizadas para pulsar boton cancelar y restaurar valores que había
private String backupclave;
private String backupsaldo;
private String backuppvpsms;



EditText edittextclavelocalizador, editTextNumLocalizador, edittextsaldo, edittextpvpxsms;
Spinner spinnerlocalizador;
Button botoneditarguardar; 
Button botoncambiarfecha;
Button botoncancelar;
Button botonborrar;
Button botonconfiguraravisos;
ImageView botonnuevo;

TextView smsrestantes;
TextView fecharecarga;
TextView marcamodelo;

ImageView imagenmodelo;




private Basedatos bd;
private Cursor cursor;

protected float saldo;
protected float pvpxsms;
protected SimpleCursorAdapter mAdapter;
protected String nombre;
protected String clave;
protected String numero;
protected String fecha;
protected static int anio;
protected static int mes;
protected static int dia;



//Para establecer la fecha de recarga
private DatePickerDialog.OnDateSetListener datesetlistener=new DatePickerDialog.OnDateSetListener() {
	
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
	
		anio=year;
		mes=monthOfYear;
		dia=dayOfMonth;
		
		actualizartextviewfecha();
	}

	
};



private SeleccionarLocalizador contexto;



private ImageButton atras;



private void actualizartextviewfecha() {

	fecharecarga.setText(new StringBuilder().append(dia).append("/")
			 .append(mes+1).append("/")
			 .append(anio).append(" "));
       
	
}      
     
         
     
     @Override
     protected Dialog onCreateDialog(int id){
        switch (id){
          case DATE_DIALOG_ID: 
				 fecha=cursor.getString(cursor.getColumnIndex("fecharecarga"));
				 
				 
				 Calendar calendar = Manejadorfechas.StringtoCalendar(fecha);
				
		
        return new DatePickerDialog(this,datesetlistener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        }
      return null;	 
     }

     @Override
     public void onResume(){
     super.onResume();
   
     edittextclavelocalizador.setEnabled(false);
	 
	 edittextsaldo.setEnabled(false);
	 
	 
	 edittextpvpxsms.setEnabled(false);
	 
	 botoncambiarfecha.setEnabled(false);

     botoncancelar.setEnabled(false);
	 
     
     rellenardatos();
     
     //Conecta y abre la base de datos
 	//En evento onpause la cierra
 	bd=new Basedatos(this);
 	
 		
 	
 		    
 	    
 	    
 	//Establece el desplegable en el seleccionado, guardado en la preferencia Main.KEY_NOMBRELOCALIZADOR   
 	try{
	 
	 	String nombre;
	 	for (int i = 0; i < spinnerlocalizador.getCount(); i++) {              
	       
	        
	        Cursor c1=(Cursor)spinnerlocalizador.getItemAtPosition(i);
	        nombre=c1.getString(c1.getColumnIndex("nombre"));
	        //Toast.makeText(this, "Localizador:"+nombre, Toast.LENGTH_LONG).show();
	        if (nombre.contentEquals(Main.leer(Main.KEY_NOMBRELOCALIZADOR))) {
	         spinnerlocalizador.setSelection(i);
	         break;
	         }	
		 }
	 	 } catch (java.lang.NullPointerException e){}
 	

    edittextclavelocalizador.setEnabled(false);
	 
	 edittextsaldo.setEnabled(false);
	 
	 
	 edittextpvpxsms.setEnabled(false);
	 

     
 	 
     }
     
     
      
     @Override
     public void onPause(){
     super.onPause();
    
    	 try 
         {
        	 bd.cerrarbd();
         } 
         catch (java.lang.NullPointerException e){
             	 
         }
     //finish();
     }
     
     

     
         
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.configuracionmultiplev2);
	        setContentView(R.layout.configuracionmultiple);
	        
	        contexto=this;
	    	
	        
	        
	        this.editTextNumLocalizador=(EditText)findViewById(R.id.editTextNumLocalizador);
	        
	        this.edittextclavelocalizador=(EditText)findViewById(R.id.editTextClaveLocalizadorMultiple);
	        
	        this.edittextsaldo=(EditText)findViewById(R.id.editTextsaldoporlocalizador);
	        
	        this.edittextpvpxsms=(EditText)findViewById(R.id.editTextpvpxsmsloc);
	        
	        this.botoneditarguardar=(Button)findViewById(R.id.botonguardarclave);
	        
	        this.botoncambiarfecha=(Button)findViewById(R.id.botoncambiarfecha);
	         
	        this.botoncancelar=(Button)findViewById(R.id.botoncancelar);
	        
	        this.botonborrar=(Button)findViewById(R.id.botonborrar);
	        
	        this.botonconfiguraravisos=(Button)findViewById(R.id.configuraravisos);
	        	        
	        this.botonnuevo=(ImageView)findViewById(R.id.botonnuevo);
	        	        
	        this.spinnerlocalizador=(Spinner)findViewById(R.id.spinnermodelonuevo);
	        
	        this.imagenmodelo=(ImageView)findViewById(R.id.imagenmodelonuevo);
	        
	        this.smsrestantes=(TextView)findViewById(R.id.smsrestantes);
	        
	        this.fecharecarga=(TextView)findViewById(R.id.fecharecarga);
	        
	        this.marcamodelo=(TextView)findViewById(R.id.textomarcamodelo);
	        
	        atras=(ImageButton)findViewById(R.id.botonatras);
	        
	        
	    	atras.setOnClickListener(new OnClickListener(){

	    		public void onClick(View v) {
	    			finish();
	    			
	    		}});

	        
			 
			 
			 
			 imagenmodelo.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					Utiles.aplicarAnimacionZoom( imagenmodelo, new Animation.AnimationListener(){

						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							animation.cancel();
							
						}

						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}}); 
					
				}});
		
				
	        
	        rellenardatos();	        		        
	       
	        		 
	        //Preparamos las preferencias		 
	        Main.preferencias = getSharedPreferences(Main.PREFERENCIAS,Context.MODE_PRIVATE);
	        
	   
	       
	        
	        
	        OnItemSelectedListener listener=new OnItemSelectedListener(){
            
	        
            
				//Para guardar la posición actual y si se esta editando no dejar cambiar el spinner de posición
	        	//Cambiando a donde estaba y mostrando una alerta
				private int posanterior;
			
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int posicion, long id) {
				
			    
					
			    
				if (!edittextclavelocalizador.isEnabled()){
				              	
							 posanterior=posicion;     	
							 
							 numero=cursor.getString(cursor.getColumnIndex("numero"));
							 clave=cursor.getString(cursor.getColumnIndex("clave"));
							 nombre=cursor.getString(cursor.getColumnIndex("nombre"));
							 saldo=cursor.getFloat(cursor.getColumnIndex("saldo"));
							 pvpxsms=cursor.getFloat(cursor.getColumnIndex("pvpxsms"));
							 fecha=cursor.getString(cursor.getColumnIndex("fecharecarga"));
							 
									 							 
							 
							 
							 Calendar calendar = Manejadorfechas.StringtoCalendar(fecha);
							
							 anio=calendar.get(Calendar.YEAR);
							 mes=calendar.get(Calendar.MONTH);
							 dia=calendar.get(Calendar.DAY_OF_MONTH);
							 
							 actualizartextviewfecha();
							 
							 
							 /*
							 try{ 
							 Toast.makeText(arg1.getContext(), fecha, Toast.LENGTH_LONG).show();
							 }
							 catch (Exception e){}
							 */
							 String marca=cursor.getString(cursor.getColumnIndex("marca"));
							 String modelo=cursor.getString(cursor.getColumnIndex("modelo"));
							 String submodelo=cursor.getString(cursor.getColumnIndex("submodelo"));
						     
							 //Toast.makeText(contexto, submodelo, Toast.LENGTH_LONG).show();
							 
							 //Guarda las preferencias del localizador seleccionado, estas son utilizadas en metodo MAIN							 
							 Main.guardar(Main.KEY_NOMBRELOCALIZADOR, nombre);
							 
							 Main.guardar(Main.KEY_NUMTELLOCALIZADOR, numero);
							 Main.guardar(Main.KEY_CLAVELOCALIZADOR, clave);
							 Main.guardar(Main.KEY_MODELO, modelo);
							 Main.guardar(Main.KEY_SUBMODELO, submodelo);
							 
							 
							  try
						       {
						       imagenmodelo.setImageDrawable(Main.geticonosubmodelo(submodelo, contexto));
						       }
						      catch (Exception e){e.printStackTrace();}
						  
							 
							  Utiles.aplicarAnimacionZoom( imagenmodelo, new Animacion()); 
														 
							  		    	
							 
							 
							 
							 editTextNumLocalizador.setText(numero); 
							 edittextclavelocalizador.setText(clave);
							 edittextsaldo.setText(Float.toString(     Utiles.redondear(saldo, 2, BigDecimal.ROUND_HALF_UP )  ));
							 edittextpvpxsms.setText(Float.toString(pvpxsms));
							 
							 
							 marcamodelo.setText(marca+"\n"+submodelo);
				
							 
							 //Actualiza los SMS restantes. 
							 try{
							 
							 if (pvpxsms>0) smsrestantes.setText(arg0.getContext().getResources().getString(R.string.smsrestantes) + " " + Double.valueOf(Math.floor(saldo/pvpxsms)).intValue());
							 else if (pvpxsms==0) smsrestantes.setText(arg0.getContext().getResources().getString(R.string.ilimitados));
							 
							 long milisegundosfechabd;
								Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD, 4);
								long mesescaducidad=Integer.valueOf(Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD, 4)).longValue();
							    long milisegundosmesesbd=mesescaducidad*30*24*60*60*1000;
							    
							 
							 Date fechabd;
					    	    long milisegundosalarma;
					    	    	 
					    		 String cadenafechabd=cursor.getString(cursor.getColumnIndex("fecharecarga"));
					    		 fechabd=Manejadorfechas.deStringToDate(cadenafechabd);
					    		 
					    		 milisegundosfechabd = fechabd.getTime();
					    		 
								 milisegundosalarma=milisegundosfechabd+milisegundosmesesbd;
					    		
							     Date fechacaducidad=new Date(milisegundosalarma);
							     
							     SimpleDateFormat simpledateformat=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
							     
							     
							     
							     smsrestantes.setText(smsrestantes.getText().toString()+ "\n" + contexto.getResources().getString(R.string.fechacaducidad)+ ": " + simpledateformat.format(fechacaducidad));
							 
							 } catch (Exception e){e.printStackTrace();}
				 
					} else
					{
					 spinnerlocalizador.setSelection(posanterior);
					 Toast.makeText(arg1.getContext(),"No se puede cambiar de localizador mientras se está editando",Toast.LENGTH_LONG).show();
					}
				     
								
				}
				

				public void onNothingSelected(AdapterView<?> arg0) {
					imagenmodelo.setImageDrawable(arg0.getContext().getResources().getDrawable(R.drawable.ic_launcher));
					
					
					
				}};
			this.spinnerlocalizador.setOnItemSelectedListener(listener);
	        
			
			
			

			
			
		
		
		
		
			
		
				
	        String claveloc = ""; 
	        claveloc=Main.preferencias.getString(Main.KEY_CLAVELOCALIZADOR, "");
	           
	        
	        
	        
	        
	        
	        
	        this.edittextclavelocalizador.setText(claveloc);
	        
	         
	        this.botonnuevo.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					Intent intent=new Intent(v.getContext(),NuevoLocalizador.class);
					startActivity(intent);
					
				}});
	        
	        
	        
			this.botonconfiguraravisos.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					
					Intent intent=new Intent(v.getContext(),ConfiguracionGasto.class);
					v.getContext().startActivity(intent);					
				}});
	        
	        
	        this.botonborrar.setOnClickListener(new OnClickListener(){

				public void onClick(View view) {
				
					AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
					
					alert.setTitle(contexto.getResources().getString(R.string.eliminar));
					alert.setMessage(contexto.getResources().getString(R.string.deseaeliminarpregunta));
                    alert.setNegativeButton(contexto.getResources().getString(R.string.no), null);
                    
					alert.setPositiveButton(contexto.getResources().getString(R.string.si), new DialogInterface.OnClickListener(){

              
						public void onClick(DialogInterface dialog, int which) {
                            accionborrar();							 
						}});
					
		
					
                  
					alert.show();	
					
								
					
				}

				private void accionborrar() {
				bd=new Basedatos(contexto);	
					
					try
					{
						
					bd.borrarlocalizador(Main.leer(Main.KEY_NOMBRELOCALIZADOR));
					
					rellenardatos();
					}
					catch (Exception e){e.printStackTrace();}
					
					
				bd.cerrarbd();	
				}
				
	        
	        
	        });
		        
		        
		        this.botoncambiarfecha.setOnClickListener(new OnClickListener(){
	
					public void onClick(View view) {
						showDialog(DATE_DIALOG_ID);
						
					}});
		        
		        
		        this.botoncancelar.setOnClickListener(new OnClickListener(){
	
					public void onClick(View v) {
					    /*    
						backupclave=edittextclavelocalizador.getText();
						backupsaldo=edittextclavelocalizador.getText();
						backuppvpsms=edittextclavelocalizador.getText();
						backupdia=dia;
						backupmes=mes;
						backupanio=anio;
						
						*/ 
						
						 edittextclavelocalizador.setEnabled(false);
						 
						 edittextsaldo.setEnabled(false);
						 
						 
						 edittextpvpxsms.setEnabled(false);
						 
						 botoncambiarfecha.setEnabled(false);
						 
						 botonborrar.setEnabled(true);
				
				       
						edittextclavelocalizador.setText(backupclave);
						edittextsaldo.setText(backupsaldo);
						edittextpvpxsms.setText(backuppvpsms);
						
						 fecha=cursor.getString(cursor.getColumnIndex("fecharecarga"));
										 
						 
						 Calendar calendar = Manejadorfechas.StringtoCalendar(fecha);
						
						 anio=calendar.get(Calendar.YEAR);
						 mes=calendar.get(Calendar.MONTH);
						 dia=calendar.get(Calendar.DAY_OF_MONTH);
						 
						 actualizartextviewfecha();
				         botoneditarguardar.setText(v.getContext().getResources().getString(R.string.editar));
							
						 
						botoncancelar.setEnabled(false);

					
				}});
	        
	        
	        
			this.botoneditarguardar.setOnClickListener(new OnClickListener(){
            
				
				

				

				
				public void onClick(View v) {
					    
					
						if (edittextclavelocalizador.isEnabled()){
						
						 int posactual=spinnerlocalizador.getSelectedItemPosition();	
						
						 
						 
						 edittextclavelocalizador.setEnabled(false);
						 
						 edittextsaldo.setEnabled(false);
						 
						 
						 edittextpvpxsms.setEnabled(false);
						 
						 botoncambiarfecha.setEnabled(false);
				
						 botoncancelar.setEnabled(false);
						 
						 botonborrar.setEnabled(true);
						 
						
						 
						 botoneditarguardar.setText(v.getContext().getResources().getString(R.string.editar));
						 Main.preferencias.edit().putString(Main.KEY_CLAVELOCALIZADOR, edittextclavelocalizador.getText().toString()).commit();
						 
						 try {
						 bd.actualizarclave(nombre, edittextclavelocalizador.getText().toString());
						 }
						 catch (Exception e){Toast.makeText(v.getContext(), "Imposible guardar contraseña", Toast.LENGTH_LONG).show();}
						 						 
						 
						 try {
						 bd.actualizarsaldo(bd.obtenernumero(editTextNumLocalizador.getText().toString()), Float.parseFloat(edittextsaldo.getText().toString()));
						 }
						 catch (Exception e){Toast.makeText(v.getContext(), "Imposible guardar saldo", Toast.LENGTH_LONG).show();}
						 
						 try {
						 bd.actualizarpvpxsms(bd.obtenernumero(editTextNumLocalizador.getText().toString()), Float.parseFloat(edittextpvpxsms.getText().toString()));
						 }
						 catch (Exception e){Toast.makeText(v.getContext(), "Imposible guardar precio por sms", Toast.LENGTH_LONG).show();}
						 
						
						 try {
							 //Toast.makeText(v.getContext(), getfecha(), Toast.LENGTH_LONG).show();
							 bd.actualizarfecha(nombre, getfecha());
						 }
						 catch (Exception e){Toast.makeText(v.getContext(), "Imposible guardar fecharecarga", Toast.LENGTH_LONG).show();}
						
						 
						 
						
						 //Intent myIntent = new Intent(v.getContext(), Main.class);

						 //PendingIntent pendingIntent = PendingIntent.getService(v.getContext(), 0, myIntent, 0);
						 
						 //AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

						 new Utiles().resetalarmas(v.getContext());
						 
						 
						 cursor=bd.consultarLocalizadores();
						 
						
					        
					     mAdapter.changeCursor(cursor);
					        
						 
						 
				  		 spinnerlocalizador.setAdapter(mAdapter);
					      
						 
				  		 
				  		 
						 mAdapter.notifyDataSetChanged();             
							
						 
						 spinnerlocalizador.setSelection(posactual);
						 
		        		 
						 
						}
						else
						{
						//Utilizados para cancelar y restablecer valores al pulsar boton cancelar
						backupclave=edittextclavelocalizador.getText().toString();
						backupsaldo=edittextsaldo.getText().toString();
						backuppvpsms=edittextpvpxsms.getText().toString();
					
						 
						
						edittextclavelocalizador.setEnabled(true);
						edittextsaldo.setEnabled(true);
						 edittextpvpxsms.setEnabled(true);
			
						 botoncambiarfecha.setEnabled(true);
						
						 botoncancelar.setEnabled(true);
						 
						 botonborrar.setEnabled(false);
						 
						 botoneditarguardar.setText(v.getContext().getResources().getString(R.string.guardar));
						
						
						
						}
					
					  
				      
	                    
				        
							 
					
					
				     
				     
				     
				     }
					     
				     	
				});
			
			
		
			 this.bd.cerrarbd();
	 }

     //Rellena datos en Spinner
	 private void rellenardatos() {
		bd=new Basedatos(this);       
               
        
		//cursor con los localizadores seleccionados, en este cursor se obtiene la tabla localizadores, inner join con modelos 	
        cursor=bd.consultarLocalizadores();
           	
		
        
        String[] de = new String[] { "nombre" };
        int[] a = new int[] { android.R.id.text1 };
        
       
       //Adaptardor para cargar el Spinner con los localizadores 
       mAdapter = new SimpleCursorAdapter (this,android.R.layout.simple_spinner_item, cursor, de, a);
        		        mAdapter.setDropDownViewResource
        		 (android.R.layout.simple_spinner_dropdown_item);
        
        		 mAdapter.changeCursor(cursor);
        		 spinnerlocalizador.setAdapter(mAdapter);

	bd.cerrarbd();	
	
		if (cursor.getCount()<1){
		 //imagenmodelo.setImageDrawable(contexto.getResources().getDrawable(R.drawable.ic_launcher));
		 imagenmodelo.setImageDrawable(null);
		 new Utiles().confirmarirconfiguracion(contexto);
			
		 
		 botonborrar.setEnabled(false);
	     botoneditarguardar.setEnabled(false);

		 
		 numero="";
		 clave="";
		 nombre="";
		 saldo=0;
		 pvpxsms=0;
		 fecha="2013-01-01 00:00:00";
		 
		  							 
		 
		 
		 Calendar calendar = Manejadorfechas.StringtoCalendar(fecha);
		
		 anio=calendar.get(Calendar.YEAR);
		 mes=calendar.get(Calendar.MONTH);
		 dia=calendar.get(Calendar.DAY_OF_MONTH);
		 
		 		 
		 actualizartextviewfecha();
		 
		 this.editTextNumLocalizador.setText(numero);
		 this.edittextclavelocalizador.setText(clave);
		 this.edittextsaldo.setText("");
		 this.edittextpvpxsms.setText("");
		 this.smsrestantes.setText("");
		 
		 String modelo="";
	     String submodelo="";
		 
		 
		 //Guarda las preferencias del localizador seleccionado, estas son utilizadas en metodo MAIN							 
		 Main.guardar(Main.KEY_NOMBRELOCALIZADOR, nombre);
    	 Main.guardar(Main.KEY_NUMTELLOCALIZADOR, numero);
		 Main.guardar(Main.KEY_CLAVELOCALIZADOR, clave);
		 Main.guardar(Main.KEY_MODELO, modelo);
		 Main.guardar(Main.KEY_SUBMODELO, submodelo);
				     
          			 
			 
		} else {
			 botonborrar.setEnabled(true);
		     botoneditarguardar.setEnabled(true);

		}
		
	}

	 
	
	 


	protected String getfecha() {
		String fecha = null;
	     
	     
	     String cadenaanio=Integer.toString(anio);
	     String cadenames=Integer.toString(mes+1);
	     String cadenadia=Integer.toString(dia);
	     
	     if (mes<10) cadenames="0"+cadenames;
	     if (dia<10) cadenadia="0"+cadenadia;
	     
	     fecha=cadenaanio+"-"+cadenames+"-"+cadenadia+" 12:00:00";
	     
	     return fecha;
	     } 
	
	
	private final class Animacion implements Animation.AnimationListener {

		public void onAnimationEnd(Animation arg0) {
			// TODO Auto-generated method stub
			overridePendingTransition( 0,0 );
			arg0.cancel();
			
		}

		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	 }
    

	
}