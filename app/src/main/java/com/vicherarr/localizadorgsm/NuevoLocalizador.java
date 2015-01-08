package com.vicherarr.localizadorgsm;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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

public class NuevoLocalizador extends Activity {

	private static final int DATE_DIALOG_ID = 0;
    
    //Minimos caracteres para introducir número y nombre	
	private static final int MINCARNUM = 9;
	private static final int MINCARNOM = 1;
	
EditText etnombre, etclave, etnumero, etsaldo, etpvpxsms;
Button guardar;	
Spinner modelo;


protected String modeloseleccionado;
protected String clavefabricaseleccionado;


TextView tvsmsrestantes;
TextView tvfecharecarga;
ImageView imagenmodelo;

protected Basedatos bd;
protected Cursor cursormodelos;


private Context contexto;
private SimpleCursorAdapter adaptador;
private Spinner spinnermodelo;
private Spinner spinnermarca;
private int dia;
private TextView fecharecarga;
private int mes;
private int anio;

private ImageButton atras;

protected String fabricanteseleccionado;

private SimpleCursorAdapter adaptadormarcas;

private Cursor cursormarcas;

protected String marcaseleccionada;


public boolean validar(){
boolean retorno=false;

     if ( !(etnumero.getText().toString().length()>=MINCARNUM)  ) Toast.makeText(this, R.string.minimocaracterestelefono, Toast.LENGTH_LONG).show();
    		 
     if ( !(etnombre.getText().toString().length()>=MINCARNOM)  ) Toast.makeText(this, R.string.deberellenarelnombre, Toast.LENGTH_LONG).show();
     
     if (etclave.getText().toString().length()!=this.clavefabricaseleccionado.length()) Toast.makeText(this, contexto.getResources().getString(R.string.Laclavedebetener) + " " + this.clavefabricaseleccionado.length() + " " + contexto.getResources().getString(R.string.digitosnumericos), Toast.LENGTH_LONG).show();
     
     if ((etnumero.getText().toString().length()>=6) && (etnombre.getText().toString().length()>=1) && etclave.getText().toString().length()==this.clavefabricaseleccionado.length()) retorno=true;
     
return retorno;
}



@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.nuevolocalizador);
    contexto=this;
    
    etnumero=(EditText)findViewById(R.id.editTextNumLocalizadornuevo);
    etnombre=(EditText)findViewById(R.id.editTextnombrelocalizador);
    
    
    //fecharecarga=(TextView)findViewById(R.id.fecharecarganuevo); 
    //tvfecharecarga=(TextView)findViewById(R.id.fecharecarganuevo);
    
    spinnermodelo=(Spinner)findViewById(R.id.spinnermodelonuevo);
    spinnermarca=(Spinner)findViewById(R.id.spinnermarca);
    
    imagenmodelo=(ImageView)findViewById(R.id.imagenmodelonuevo);
    guardar=(Button)findViewById(R.id.botonguardarnuevo);
    atras=(ImageButton)findViewById(R.id.botonatrasnuevo);
    
    etclave=(EditText)findViewById(R.id.editTextClaveNuevo);
    
    
    imagenmodelo.setOnClickListener(new OnClickListener(){

		public void onClick(View v) {
			Utiles.aplicarAnimacionZoom( imagenmodelo, new Animacion()); 
			
		}});
    
	atras.setOnClickListener(new OnClickListener(){

		public void onClick(View v) {
			finish();
			
		}});

	
	

	
	
	
	
	spinnermodelo.setOnItemSelectedListener(new OnItemSelectedListener(){

		
		public void onItemSelected(AdapterView<?> av, View v, int pos,
				long id) {
		
			
			
			modeloseleccionado=cursormodelos.getString(cursormodelos.getColumnIndex("submodelo"));
			clavefabricaseleccionado=cursormodelos.getString(cursormodelos.getColumnIndex("clavefabrica"));
			fabricanteseleccionado=cursormodelos.getString(cursormodelos.getColumnIndex("marca"));
			
			
			//fabricante.setText( v.getContext().getResources().getString(R.string.Fabricante) + ": "+fabricanteseleccionado);
			
			/*
			 * 	String sql1="INSERT INTO Modelos VALUES (1,'TK-102','TK-101','123456','XEXUN')";
	            String sql3="INSERT INTO Modelos VALUES (3,'TK-103','TK-103','123456','XEXUN')";
				String sql4="INSERT INTO Modelos VALUES (4,'TK-102','TK-102','123456','XEXUN')";
				String sql5="INSERT INTO Modelos VALUES (5,'TK-102','TK-102-2','123456','XEXUN')";
				String sql6="INSERT INTO Modelos VALUES (6,'TK-102','TK-201','123456','XEXUN')";
				String sql7="INSERT INTO Modelos VALUES (7,'TK-102','TK-206','123456','XEXUN')";
				String sql2="INSERT INTO Modelos VALUES (2,'TK-110','TK-110','0000','Shenzhen Hongyuan Xintong Technology')";

			 * */
			
			
		    try
		       {
		       imagenmodelo.setImageDrawable(Main.geticonosubmodelo(modeloseleccionado, contexto));
		     }
		     catch (Exception e){e.printStackTrace();}
		   
						
	    	
		    Utiles.aplicarAnimacionZoom( imagenmodelo, new Animacion());
		    
		    
			
			
			try
			{
	        etclave.setText(clavefabricaseleccionado);
			}
			catch (Exception e){e.printStackTrace();}
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
		}});
	
	
spinnermarca.setOnItemSelectedListener(new OnItemSelectedListener(){

		
		

		public void onItemSelected(AdapterView<?> av, View v, int pos,
				long id) {
		
		marcaseleccionada=cursormarcas.getString(cursormarcas.getColumnIndex("marca"));   
			
		rellenarmodelos(marcaseleccionada);
		
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
		}});
	
	    
	    
		guardar.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
			
				
			if (validar()){
			
			
				//"CREATE TABLE IF NOT EXISTS Localizadores (_id INTEGER PRIMARY KEY, nombre TEXT UNIQUE NOT NULL, numero TEXT UNIQUE NOT NULL, clave TEXT, id_modelo INTEGER, saldo FLOAT, pvpxsms FLOAT, fecharecarga DATE DEFAULT (datetime('now','localtime')), FOREIGN KEY (id_modelo) REFERENCES Modelos(_id) )";
				String nombre,numero,clave;
				
				
				
				nombre=etnombre.getText().toString();
				numero=etnumero.getText().toString();
				clave=etclave.getText().toString();
				
				//fecharecarga=tvfecharecarga.getText().toString();
				
				Integer id_modelo=cursormodelos.getInt(0);
				//Float saldo=Float.parseFloat(etsaldo.getText().toString());
				//Float pvpxsms=Float.parseFloat(etsaldo.getText().toString());
				
				try
				{
				bd.insertarnuevolocalizador(nombre,numero,clave,id_modelo,0.f,0.f,"");
				Main.guardar(Main.KEY_NOMBRELOCALIZADOR, nombre);
				finish();
				}
				catch (android.database.sqlite.SQLiteConstraintException e){
				Toast.makeText(contexto, R.string.localizadorduplicado, Toast.LENGTH_SHORT).show();	
				}
		    
			}
			
			}
			
			});
    
}

@Override
public void onResume(){
  super.onResume();

  contexto=this;
  
	  

rellenardatos();
	  
	  
	  
	  
	  
	  
	  
	  
  
}




private void rellenardatos() {
	bd=new Basedatos(contexto);

	  cursormarcas=bd.consultarmarcas();
	  
	  
	  
	  String[] desde = new String[] { "marca" };
	  int[] hacia = new int[] { android.R.id.text1 };
  
	  
	  //Adaptardor para cargar el Spinner con las marcas 
	  adaptadormarcas = new SimpleCursorAdapter (this,android.R.layout.simple_spinner_item, cursormarcas, desde, hacia);
	  
	  adaptadormarcas.setDropDownViewResource
	   		 (android.R.layout.simple_spinner_dropdown_item);
	   
	  spinnermarca.setAdapter(adaptadormarcas);

	  
    bd.cerrarbd();	  

      try
      {
	  bd=new Basedatos(contexto);
	  cursormodelos=bd.consultarmodelos(marcaseleccionada);
	  
	  	  String[] de = new String[] { "submodelo" };
	  	  int[] a = new int[] { android.R.id.text1 };
	    
		  //Adaptardor para cargar el Spinner con los localizadores 
		  adaptador = new SimpleCursorAdapter (this,android.R.layout.simple_spinner_item, cursormodelos, de, a);
		  adaptador.setDropDownViewResource
		   		 (android.R.layout.simple_spinner_dropdown_item);
		   
		  spinnermodelo.setAdapter(adaptador);
	
      }
      catch (Exception e){ e.printStackTrace(); }
	bd.cerrarbd();	  

	
}


private void rellenarmodelos(String marcaseleccionada) {
	bd=new Basedatos(contexto);


      try
      {
	  bd=new Basedatos(contexto);
	  cursormodelos=bd.consultarmodelos(marcaseleccionada);
	  
	  	  String[] de = new String[] { "submodelo" };
	  	  int[] a = new int[] { android.R.id.text1 };
	    
		  //Adaptardor para cargar el Spinner con los localizadores 
		  adaptador = new SimpleCursorAdapter (this,android.R.layout.simple_spinner_item, cursormodelos, de, a);
		  adaptador.setDropDownViewResource
		   		 (android.R.layout.simple_spinner_dropdown_item);
		   
		  spinnermodelo.setAdapter(adaptador);
	
      }
      catch (Exception e){ e.printStackTrace(); }
	bd.cerrarbd();	  

	
}


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
private String fecha;



private void actualizartextviewfecha() {

	fecharecarga.setText(new StringBuilder().append(dia).append("/")
			 .append(mes+1).append("/")
			 .append(anio).append(" "));

	
}

@Override
protected Dialog onCreateDialog(int id){
   switch (id){
     case DATE_DIALOG_ID: 
			 fecha=cursormodelos.getString(cursormodelos.getColumnIndex("fecharecarga"));
			 
			 
			 Calendar calendar = Manejadorfechas.StringtoCalendar(fecha);
			
	
   return new DatePickerDialog(this,datesetlistener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
   }
 return null;	 
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
