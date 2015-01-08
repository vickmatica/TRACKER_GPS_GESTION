/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * 
 */
package com.vicherarr.localizadorgsm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class Basedatos  {
	
	private static final int VERSIONBD = 32;
	private Modelobd modelobd;
	public SQLiteDatabase db;
	private Context contexto;
	// "CREATE TABLE Historico (id INTEGER PRIMARY KEY AUTOINCREMENT, hora DATETIME, textosms TEXT";

	
	
	//Contructor de la base de datos
	Basedatos(Context context) {
	this.contexto=context;	
	//Abrimos la base de datos 'DBUsuarios' en modo escritura
	 modelobd = new Modelobd(contexto, "BaseDatos", null, VERSIONBD);
	 
	}
	
	
	
	//Actualiza la fecha del localizador pasado como parametro
		public boolean actualizarfecha(String localizador, String fecha){
			boolean aux=true;
			db = modelobd.getWritableDatabase();
			
			    //Si hemos abierto correctamente la base de datos
				if(db != null)
				{
					try {
					//Insertamos los datos en la tabla
					//db.execSQL("UPDATE Localizadores SET clave=\'" + clave + "\' WHERE nombre=\'" + localizador + "\'");
						Log.i("actualizando fecha", "actualizando fecha");
						db.execSQL("UPDATE Localizadores SET fecharecarga=\'"+ fecha +"\' WHERE Localizadores.nombre=\'" + localizador + "\'");
						
					}
					catch (Exception e){Toast.makeText(contexto, "Imposible guardar fecha", Toast.LENGTH_LONG).show();}
				} else aux=false;
				//db.close();
			return aux;		
		}
		
	
	
	//Actualiza la clave del localizador pasado como parametro
	public boolean actualizarclave(String localizador, String clave){
		boolean aux=true;
		db = modelobd.getWritableDatabase();
		
		    //Si hemos abierto correctamente la base de datos
			if(db != null)
			{
				try {
				//Insertamos los datos en la tabla
				//db.execSQL("UPDATE Localizadores SET clave=\'" + clave + "\' WHERE nombre=\'" + localizador + "\'");
					Log.i("actualizando clave", "actualizando clave");
					db.execSQL("UPDATE Localizadores SET clave=\'"+ clave +"\' WHERE Localizadores.nombre=\'" + localizador + "\'");
					
				}
				catch (Exception e){Toast.makeText(contexto, "Imposible guardar clave sms", Toast.LENGTH_LONG).show();}
			} else aux=false;
			//db.close();
		return aux;		
	}
	
	
	public String obtenernumero(String telefono){
	boolean encontrado=false;
	String retorno=null;
	try{
	db = modelobd.getWritableDatabase();
	}
	catch (Exception e){}
	
    Cursor cursor=null;
	if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores", null);
	    
		if (cursor.moveToFirst()){
			try {
			   do{
				  //if (cursor.getString(cursor.getColumnIndex("numero")).endsWith(telefono))
				   if (telefono.endsWith(cursor.getString(cursor.getColumnIndex("numero")))) 
				  encontrado=true;
				  retorno=cursor.getString(cursor.getColumnIndex("numero"));
					  
				  	  
			   }
			   while (cursor.moveToNext() && !encontrado);
			}
		    catch (Exception e){}
	    }	
	
	return retorno;
	}

	
	
	//Actualiza el saldo
		public boolean actualizarsaldo(String numerotfno, float saldo){
			boolean aux=true;
			db = modelobd.getWritableDatabase();
			
			    //Si hemos abierto correctamente la base de datos
				if(db != null)
				{
					try {
					//Insertamos los datos en la tabla
					//db.execSQL("UPDATE Localizadores SET clave=\'" + clave + "\' WHERE nombre=\'" + localizador + "\'");
						Log.i("actualizando clave", "actualizando saldo");
						//db.execSQL("UPDATE Localizadores SET saldo="+ Float.toString(saldo) +" WHERE Localizadores.numero=\'" + numerotfno + "\'");
						db.execSQL("UPDATE Localizadores SET saldo="+ saldo +" WHERE Localizadores.numero=\'" + numerotfno + "\'");
					}
					catch (Exception e){Toast.makeText(contexto, "Imposible guardar saldo sms", Toast.LENGTH_LONG).show();aux=false;}
				} else aux=false;
				//db.close();
			return aux;		
		}
		
		
		//Actualiza el saldo
				public boolean actualizarpvpxsms(String numerotfno, float pvpxsms){
					boolean aux=true;
					db = modelobd.getWritableDatabase();
					
					    //Si hemos abierto correctamente la base de datos
						if(db != null)
						{
							try {
							//Insertamos los datos en la tabla
							//db.execSQL("UPDATE Localizadores SET clave=\'" + clave + "\' WHERE nombre=\'" + localizador + "\'");
								Log.i("actualizando clave", "actualizando pvpxsms");
								//db.execSQL("UPDATE Localizadores SET saldo="+ Float.toString(saldo) +" WHERE Localizadores.numero=\'" + numerotfno + "\'");
								db.execSQL("UPDATE Localizadores SET pvpxsms="+ pvpxsms +" WHERE Localizadores.numero=\'" + numerotfno + "\'");
							}
							catch (Exception e){Toast.makeText(contexto, "Imposible guardar pvpxsms sms", Toast.LENGTH_LONG).show();aux=false;}
						} else aux=false;
						//db.close();
					return aux;		
				}
			
		
	public boolean estaeltelefono(String telefono){
	boolean encontrado=false;
	try{
	db = modelobd.getWritableDatabase();
	}
	catch (Exception e){}
	
    Cursor cursor=null;
	if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores", null);
	    
		if (cursor.moveToFirst()){
			try {
			   do{
				  //if (cursor.getString(cursor.getColumnIndex("numero")).endsWith(telefono))
				   if (telefono.endsWith(cursor.getString(cursor.getColumnIndex("numero")))) 
				  encontrado=true;
				 
					  
				  	  
			   }
			   while (cursor.moveToNext() && !encontrado);
			}
		    catch (Exception e){}
	    }	
	
	return encontrado;
	}
	
    
	
	public int contarlocalizadores(){
		int contador=0;
		try{
		db = modelobd.getWritableDatabase();
		}
		catch (Exception e){}
		
	    Cursor cursor=null;
		if (db!=null) 
		{
			cursor=db.rawQuery("SELECT * FROM Localizadores", null);
		    contador=cursor.getCount();
		}
		
		
		return contador;
		}

	

    //Inserta en historial de historico de sms recibidos
	public boolean insertar(String textosms, String numero){
	boolean aux=true;
	db = modelobd.getWritableDatabase();
	
	    //Si hemos abierto correctamente la base de datos
		if(db != null)
		{
			try {
			//Insertamos los datos en la tabla
			db.execSQL("INSERT INTO Historico (hora, textosms, numero) VALUES (strftime('%d/%m/%Y  %H:%M:%S', 'now', 'localtime'), \'"+ textosms +"\',\'"+ numero +"\')" );
			}
			catch (Exception e){Toast.makeText(contexto, "Imposible guardar historico de recepciones sms", Toast.LENGTH_LONG).show();}
		} else aux=false;
		//db.close();
	return aux;
	}
	
	
    //Inserta en historial de historico de sms recibidos
	public boolean insertarSalida(String textosms, String numero){
	boolean aux=true;
	db = modelobd.getWritableDatabase();
	
	    //Si hemos abierto correctamente la base de datos
		if(db != null)
		{
			try {
			//Insertamos los datos en la tabla
			db.execSQL("INSERT INTO HistoricoSalida (hora, textosms, numero) VALUES (strftime('%d/%m/%Y  %H:%M:%S', 'now', 'localtime'), \'"+ textosms +"\',\'"+ numero +"\')" );
			}
			catch (Exception e){Toast.makeText(contexto, "Imposible guardar historico de recepciones sms", Toast.LENGTH_LONG).show();}
		} else aux=false;
		//db.close();
	return aux;
	}

	
	
	public boolean vaciartablahistorico(){
		boolean aux=true;
		db = modelobd.getWritableDatabase();
		//Si hemos abierto correctamente la base de datos
		if(db != null)
		{
		
		//Insertamos los datos en la tabla Historico
		db.execSQL("DELETE FROM Historico");
		
		
		} else aux=false;
	
	//db.close();		
	return aux;	
	}
	
	
	public boolean vaciartablahistoricoSalida(){
		boolean aux=true;
		db = modelobd.getWritableDatabase();
		//Si hemos abierto correctamente la base de datos
		if(db != null)
		{
		
		//Insertamos los datos en la tabla Historico
		db.execSQL("DELETE FROM HistoricoSalida");
		
		
		} else aux=false;
	
	//db.close();		
	return aux;	
	}
	
	
	public void cerrarbd(){
		//Cerramos la base de datos
	    db.close();
	}
	
	public boolean borrarultimosregistroshistorico(int cantidadquenoseborra){
		boolean aux=true;
		db = modelobd.getWritableDatabase();
		//Si hemos abierto correctamente la base de datos
		if(db != null)
		{
		
		Cursor cursor = db.rawQuery("SELECT * FROM Historico",null);
	    int cantidadregistros = cursor.getCount();
		
			
		//Insertamos los datos en la tabla Usuarios
		if (cantidadregistros>cantidadquenoseborra) db.execSQL("DELETE FROM Historico WHERE _id IN (SELECT _id FROM Historico ORDER BY _id ASC LIMIT " 
		+ Integer.valueOf(cantidadregistros - cantidadquenoseborra)+")");
		
		
		
		} else aux=false;
	
	//db.close();		
	return aux;	
	}
	
	
	public boolean borrarultimosregistroshistoricoSalida(int cantidadquenoseborra){
		boolean aux=true;
		db = modelobd.getWritableDatabase();
		//Si hemos abierto correctamente la base de datos
		if(db != null)
		{
		
		Cursor cursor = db.rawQuery("SELECT * FROM HistoricoSalida",null);
	    int cantidadregistros = cursor.getCount();
		
			
		//Insertamos los datos en la tabla Usuarios
		if (cantidadregistros>cantidadquenoseborra) db.execSQL("DELETE FROM HistoricoSalida WHERE _id IN (SELECT _id FROM HistoricoSalida ORDER BY _id ASC LIMIT " 
		+ Integer.valueOf(cantidadregistros - cantidadquenoseborra)+")");
		
		
		
		} else aux=false;
	
	//db.close();		
	return aux;	
	}
	
	
	public Cursor consultar(){
	Cursor cursor=null;
	db = modelobd.getWritableDatabase();
	
		//String[] campos = new String[] {"_id","hora","textosms"};
	   
		//cursor=db.query("Historico", campos, null, null, null, null, null);
	      //if (db!=null) cursor=db.rawQuery("SELECT * FROM Historico ORDER BY _id DESC", null);
	      if (db!=null) cursor=db.rawQuery("SELECT * FROM Historico INNER JOIN Localizadores ON Localizadores.numero=Historico.numero INNER JOIN Modelos ON Modelos._id=Localizadores.id_modelo  ORDER BY _id DESC", null);
	 //db.close();     
	 return cursor;
	}
	
	
	public Cursor consultarSalida(){
		Cursor cursor=null;
		db = modelobd.getWritableDatabase();
		
		//if (db!=null) cursor=db.rawQuery("SELECT * FROM HistoricoSalida ORDER BY _id DESC", null);
		//if (db!=null) cursor=db.rawQuery("SELECT * FROM HistoricoSalida INNER JOIN Localizadores ON Localizadores.numero=HistoricoSalida.numero ORDER BY _id DESC", null);
		if (db!=null) cursor=db.rawQuery("SELECT * FROM HistoricoSalida INNER JOIN Localizadores ON Localizadores.numero=HistoricoSalida.numero  INNER JOIN Modelos ON Modelos._id=Localizadores.id_modelo ORDER BY _id DESC", null);
		      
		 return cursor;
		}
	
	
	public String consultarTelefono(int id){
	String cadena=null;
	Cursor cursor=db.rawQuery("SELECT * FROM Localizadores WHERE _id=1 ",null);
    
    cadena=cursor.getString(cursor.getColumnIndex("numero")); 
	cadena="666666666";		
	return cadena;
	}
	
	
			
	
	public Cursor consultarLocalizadores(){
		Cursor cursor=null;
		db = modelobd.getWritableDatabase();
		
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores INNER JOIN Modelos ON Localizadores.id_modelo=Modelos._id INNER JOIN Marcas ON Modelos.id_marca=Marcas._id", null);
		      
		 return cursor;
	}
	
	
	
	
	    
	public String consultarModelo(String telefono){
		
		String retorno=null;
		db = modelobd.getWritableDatabase();
		
		Cursor cursor=null;
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores INNER JOIN Modelos ON Localizadores.id_modelo=Modelos._id", null);
		else Log.e("Error: basedatos=null", "basedatos=null");
		cursor.moveToFirst();
		
				if (cursor.moveToFirst()){
				
				    do{	
						if (telefono.endsWith(   cursor.getString(cursor.getColumnIndex("numero")))){ retorno=cursor.getString(cursor.getColumnIndex("modelo"));
						break;
					    }  	
				    } while (cursor.moveToNext());
				}	
			   
        
		return retorno;
		}
	
	
	
public String consultarsubmodelo(String telefono){
		
		String retorno=null;
		db = modelobd.getWritableDatabase();
		
		Cursor cursor=null;
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores INNER JOIN Modelos ON Localizadores.id_modelo=Modelos._id", null);
		else Log.e("Error: basedatos=null", "basedatos=null");
		cursor.moveToFirst();
		
				if (cursor.moveToFirst()){
				
				    do{	
						if (telefono.endsWith(   cursor.getString(cursor.getColumnIndex("numero")))){ retorno=cursor.getString(cursor.getColumnIndex("submodelo"));
						break;
					    }  	
				    } while (cursor.moveToNext());
				}	
			   
        
		return retorno;
		}
	
	
    //Consulta el nombre del localizador 
	public String consultarnombre(String telefono){
		
		String retorno=null;
		db = modelobd.getWritableDatabase();
		
		Cursor cursor=null;
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores", null);
		else Log.e("Error: basedatos=null", "basedatos=null");
		cursor.moveToFirst();
		
				if (cursor.moveToFirst()){
				
				    do{	
						if (telefono.endsWith(   cursor.getString(cursor.getColumnIndex("numero")))){ retorno=cursor.getString(cursor.getColumnIndex("nombre"));
						break;
					    }  	
				    } while (cursor.moveToNext());
				}	
			   
        
		return retorno;
		}
	
	
	
//Si no lo encuentra devuelve -1	
public float consultarsaldo(String telefono){
		
		float retorno = -1;
		db = modelobd.getWritableDatabase();
		
		Cursor cursor=null;
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores", null);
		else Log.e("Error: basedatos consultar saldo", "Error: basedatos consultar saldo");
		cursor.moveToFirst();
		
				if (cursor.moveToFirst()){
				
				    do{	
						if (telefono.endsWith(   cursor.getString(cursor.getColumnIndex("numero")))){ retorno=cursor.getFloat(cursor.getColumnIndex("saldo"));
						break;
					    }  	
				    } while (cursor.moveToNext());
				}	
			   
        
		return retorno;
	}
	


//Consulta fecha recarga	
public String consultarfecharecarga(String telefono){
		
		String retorno = null;
		db = modelobd.getWritableDatabase();
		
		Cursor cursor=null;
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores", null);
		else Log.e("Error: basedatos consultar saldo", "Error: basedatos consultar saldo");
		cursor.moveToFirst();
		
				if (cursor.moveToFirst()){
				
				    do{	
						if (telefono.endsWith(   cursor.getString(cursor.getColumnIndex("numero")))){ retorno=cursor.getString(cursor.getColumnIndex("fecharecarga"));
						break;
					    }  	
				    } while (cursor.moveToNext());
				}	
			   
      
		return retorno;
	}
	
//Si no lo encuentra devuelve -1	
public float consultarpvpxsms(String telefono){
		
		float retorno = -1;
		db = modelobd.getWritableDatabase();
		
		Cursor cursor=null;
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Localizadores", null);
		else Log.e("Error: basedatos consultar pvpxsms", "Error: basedatos consultar pvpxsms");
		cursor.moveToFirst();
		
				if (cursor.moveToFirst()){
				
				    do{	
						if (telefono.endsWith(   cursor.getString(cursor.getColumnIndex("numero")))){ retorno=cursor.getFloat(cursor.getColumnIndex("pvpxsms"));
						break;
					    }  	
				    } while (cursor.moveToNext());
				}	
			   
      
		return retorno;
	}
	
	    //Cuenta el número de registros de salida posteriores a la fecha-hora dada
		public int contarregistros(String fechahora){
		Cursor cursor=null;
		db = modelobd.getWritableDatabase();
		
		if (db!=null) cursor=db.rawQuery("SELECT * FROM Historico WHERE hora>"+fechahora+" ORDER BY _id DESC", null);
        		
		return cursor.getCount();
		}
	
	
	//Cuenta el número de registros de salida posteriores a la fecha-hora dada
	public int contarregistrossalida(String fechahora){
	Cursor cursor=null;
	db = modelobd.getWritableDatabase();
	
	if (db!=null) cursor=db.rawQuery("SELECT * FROM HistoricoSalida WHERE hora>"+fechahora+" ORDER BY _id DESC", null);

	
	
	
	return cursor.getCount();
	}
	
	
	public String consultarultimahoraregistro(){
	String cadena=null;
	Cursor cursor;
	
	cursor = db.rawQuery("SELECT * FROM Historico ORDER BY _id DESC LIMIT 1", null);
	
			if (cursor.moveToFirst()){
			
			int indicecolumna=cursor.getColumnIndex("hora");
			
			cadena=cursor.getString(indicecolumna);	
			}
	
	return cadena;	
	}
	
	
	public String consultarultimahoraregistroSalida(){
		String cadena=null;
		Cursor cursor;
		
		cursor = db.rawQuery("SELECT * FROM HistoricoSalida ORDER BY _id DESC LIMIT 1", null);
		
				if (cursor.moveToFirst()){
				
				int indicecolumna=cursor.getColumnIndex("hora");
				
				cadena=cursor.getString(indicecolumna);	
				}
		
		return cadena;	
		}
	
	
	public String consultartextosms(int which,Cursor cursor){
	String cadena=null;
	
	int indicecolumna=cursor.getColumnIndex("textoSMS");
		if (cursor.moveToPosition(which)){
		cadena=cursor.getString(indicecolumna);	
		}	     	
	
	
	return cadena;
	}
	
	
	public String consultarhorasms(int which,Cursor cursor){
		String cadena=null;
		
		int indicecolumna=cursor.getColumnIndex("hora");
			if (cursor.moveToPosition(which)){
			cadena=cursor.getString(indicecolumna);	
			}	     	
		
		
		return cadena;
		}

	public String consultarnumerosms(int which, Cursor cursor) {
		// TODO Apéndice de método generado automáticamente
String cadena=null;
		
		int indicecolumna=cursor.getColumnIndex("numero");
			if (cursor.moveToPosition(which)){
			cadena=cursor.getString(indicecolumna);	
			}	     	
		
		
		return cadena;
	}



	public boolean borrarlocalizador(String nombre) {
	boolean borrado=false;
	
	db = modelobd.getWritableDatabase();
	
		try
		{
		db.execSQL("DELETE FROM localizadores WHERE nombre=\'" + nombre + "\'");
		borrado=true;
		}
		catch (Exception e){borrado=false;}
	return borrado;	
	}



	public Cursor consultarmodelos() {
		Cursor cursor;
		db = modelobd.getWritableDatabase();
		
		String sql="SELECT * FROM Modelos INNER JOIN Marcas ON Modelos.id_marca=Marcas._id";
		cursor=db.rawQuery(sql, null);
		
	return cursor;
	}



	public void insertarnuevolocalizador(String nombre, String numero,
			String clave, Integer id_modelo, Float saldo, Float pvpxsms,
			String fecharecarga) {
		    
        db = modelobd.getWritableDatabase();
		
		String sql="INSERT INTO Localizadores (nombre, numero, clave, id_modelo) VALUES (\'"+nombre+"\',\'"+numero+"\',\'"+clave+"\',\'"+id_modelo+"\');";
		db.execSQL(sql);
			
		
	}



	public Cursor consultarmarcas() {
		Cursor cursor;
		db = modelobd.getWritableDatabase();
		
		String sql="SELECT * FROM Marcas";
		cursor=db.rawQuery(sql, null);
		
	return cursor;
	}



	public Cursor consultarmodelos(String marca) {
		Cursor cursor;
		db = modelobd.getWritableDatabase();
		
		String sql="SELECT * FROM Modelos INNER JOIN Marcas ON Modelos.id_marca=Marcas._id WHERE marca=\'"+ marca +"\'";
		cursor=db.rawQuery(sql, null);
		
	return cursor;
	}
	


}
