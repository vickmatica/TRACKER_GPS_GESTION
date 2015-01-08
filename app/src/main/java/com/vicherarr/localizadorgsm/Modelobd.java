/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * */
package com.vicherarr.localizadorgsm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Modelobd extends SQLiteOpenHelper {

	//Sentencia SQL para crear la tabla de historial
	String sqlCreate = "CREATE TABLE IF NOT EXISTS Historico (_id INTEGER PRIMARY KEY AUTOINCREMENT, hora DATETIME, textoSMS TEXT, numero TEXT)";
	String sqlCreate2 = "CREATE TABLE IF NOT EXISTS HistoricoSalida (_id INTEGER PRIMARY KEY AUTOINCREMENT, hora DATETIME, textoSMS TEXT, numero TEXT)";
	
	String sqlCreate3 = "CREATE TABLE IF NOT EXISTS Localizadores (_id INTEGER PRIMARY KEY, nombre TEXT UNIQUE NOT NULL, numero TEXT UNIQUE NOT NULL, clave TEXT, id_modelo INTEGER, saldo FLOAT DEFAULT 10, pvpxsms FLOAT DEFAULT 0, fecharecarga DATE DEFAULT (datetime('now','localtime')), FOREIGN KEY (id_modelo) REFERENCES Modelos(_id) )";
	String sqlCreate4 = "CREATE TABLE IF NOT EXISTS Modelos (_id INTEGER PRIMARY KEY, modelo TEXT NOT NULL, submodelo TEXT UNIQUE NOT NULL, clavefabrica TEXT, id_marca INTEGER, FOREIGN KEY (id_marca) REFERENCES Marcas(_id) )";
	String sqlCreate5 = "CREATE TABLE IF NOT EXISTS Marcas (_id INTEGER PRIMARY KEY, marca TEXT UNIQUE NOT NULL)";
	
	String sql1="INSERT INTO Modelos VALUES (1,'TK-102','TK-101','123456',1)";
	String sql2="INSERT INTO Modelos VALUES (2,'TK-102','TK-102','123456',1)";
	String sql3="INSERT INTO Modelos VALUES (3,'TK-102','TK-102-2','123456',1)";
	String sql4="INSERT INTO Modelos VALUES (4,'TK-102','TK-201','123456',1)";
	String sql5="INSERT INTO Modelos VALUES (5,'TK-102','TK-206','123456',1)";
	
	String sql6="INSERT INTO Modelos VALUES (6,'TK-103','TK-103','123456',1)";
	String sql7="INSERT INTO Modelos VALUES (7,'TK-103','TK-103-2','123456',1)";
	//String sql8="INSERT INTO Modelos VALUES (8,'TK-103','XT-008','123456',1)";
		
	String sql9="INSERT INTO Modelos VALUES (9,'TK-110','TK-110','0000',4)";
	String sql17="INSERT INTO Modelos VALUES (23,'TK-110','TK-118','0000',4)";
	
	String sql10="INSERT INTO Modelos VALUES (10,'TK-102','GPS 102','123456',2)";
	String sql11="INSERT INTO Modelos VALUES (11,'TK-102','GPS 102-B','123456',2)";
	String sql12="INSERT INTO Modelos VALUES (12,'TK-103','GPS 103-A','123456',2)";
	String sql13="INSERT INTO Modelos VALUES (13,'TK-103','GPS 103-B','123456',2)";
	String sql14="INSERT INTO Modelos VALUES (14,'TK-103','GPS 104','123456',2)";
	String sql15="INSERT INTO Modelos VALUES (15,'TK-103','GPS 106-A','123456',2)";
	String sql16="INSERT INTO Modelos VALUES (16,'TK-103','GPS 106-B','123456',2)";
	
	String sql18="INSERT INTO Modelos VALUES (17,'TK-102','TK102B','123456',3)";
	String sql19="INSERT INTO Modelos VALUES (18,'TK-103','TK103','123456',3)";
	String sql20="INSERT INTO Modelos VALUES (19,'TK-103','TK103B','123456',3)";
	String sql21="INSERT INTO Modelos VALUES (20,'TK-103','TK104','123456',3)";
	String sql22="INSERT INTO Modelos VALUES (21,'TK-103','TK106','123456',3)";
	String sql23="INSERT INTO Modelos VALUES (22,'TK-103','TK106-B','123456',3)";
	
	
	
	
	String sqlmarcas1="INSERT INTO Marcas VALUES (1,'XEXUN')";
	String sqlmarcas2="INSERT INTO Marcas VALUES (2,'COBAN')";
	String sqlmarcas3="INSERT INTO Marcas VALUES (3,'ZY INTERNATIONAL')";
	String sqlmarcas4="INSERT INTO Marcas VALUES (4,'Shenzhen Hongyuan Xintong Technology')";
	
	
	
	
	//'Shenzhen Hongyuan Xintong Technology'
	
	
	
	
	
		

	@SuppressWarnings("unused")
	private void insertarlocalizadores(SQLiteDatabase db){
	
		//Sentencias de creación de localizadores para el cliente
		
		
		//String sqllebara="INSERT INTO Localizadores VALUES (1,'Portatil','634117001','123456',1000,10,12E-2)";
		
		//alfonso
		//String sql4="INSERT INTO Localizadores VALUES (1,'Portatil','644500941','123456',1,10,12E-2,datetime('now','localtime'))";
		//String sql5="INSERT INTO Localizadores VALUES (2,'Moto','222222222','0000',2,10,12E-2,datetime('now','localtime'))";
		//String sql6="INSERT INTO Localizadores VALUES (3,'Coche','333333333','123456',3,11,8E-2,datetime('now','localtime'))";
		
		//mio
		//String sql4="INSERT INTO Localizadores VALUES (1,'Motorbike','634117001','0000',2,10,12E-2,datetime('now','localtime'))";
		//String sql5="INSERT INTO Localizadores VALUES (2,'Portatil','634017358','123456',1,10,12E-2,datetime('now','localtime'))";
		//String sql6="INSERT INTO Localizadores VALUES (3,'Coche','333333333','123456',3,11,8E-2,datetime('now','localtime'))";
		
		String sql4="INSERT INTO Localizadores VALUES (1,'Laptop 1','111111111','0000',1,10,12E-2,datetime('now','localtime'))";
		String sql5="INSERT INTO Localizadores VALUES (2,'Motorbike 1','222222222','123456',2,10,12E-2,datetime('now','localtime'))";
		String sql6="INSERT INTO Localizadores VALUES (3,'Car 1','333333333','123456',3,11,8E-2,datetime('now','localtime'))";
		String sql7="INSERT INTO Localizadores VALUES (4,'Laptop 2','444444444','0000',4,11,8E-2,datetime('now','localtime'))";
		String sql8="INSERT INTO Localizadores VALUES (5,'Motorbike 2','555555555','123456',5,12,9E-2,datetime('now','localtime'))";
		String sql9="INSERT INTO Localizadores VALUES (6,'Car 2','666666666','123456',6,12,9E-2,datetime('now','localtime'))";
		db.execSQL(sql4);
		db.execSQL(sql5);
	    db.execSQL(sql6);
	    db.execSQL(sql7);
		db.execSQL(sql8);
	    db.execSQL(sql9);
			
		
	}
	
	
	public Modelobd(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Apéndice de método generado automáticamente
		//Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreate2);
		db.execSQL(sqlCreate3);
		db.execSQL(sqlCreate4);
		db.execSQL(sqlCreate5);
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(sql5);
		db.execSQL(sql6);
		db.execSQL(sql7);
		//db.execSQL(sql8);
		db.execSQL(sql9);
		db.execSQL(sql10);
		db.execSQL(sql11);
		db.execSQL(sql12);
		db.execSQL(sql13);
		db.execSQL(sql14);
		db.execSQL(sql15);
		db.execSQL(sql16);
		db.execSQL(sql17);
		db.execSQL(sql18);
		db.execSQL(sql19);
		db.execSQL(sql20);
		db.execSQL(sql21);
		db.execSQL(sql22);
		db.execSQL(sql23);
		
		
		db.execSQL(sqlmarcas1);
		db.execSQL(sqlmarcas2);
		db.execSQL(sqlmarcas3);
		db.execSQL(sqlmarcas4);
			
		
	    //insertarlocalizadores(db);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Apéndice de método generado automáticamente
		//NOTA: Por simplicidad del ejemplo aquí utilizamos directamente
		//la opción de eliminar la tabla anterior y crearla de nuevo
		//vacía con el nuevo formato.
		//Sin embargo lo normal será que haya que migrar datos de la
		//tabla antigua a la nueva, por lo que este método debería
		//ser más elaborado.
		//Se elimina la versión anterior de la tabla
		
		
		db.execSQL("DROP TABLE IF EXISTS Historico");
		db.execSQL("DROP TABLE IF EXISTS HistoricoSalida");
		db.execSQL("DROP TABLE IF EXISTS Localizadores");
		db.execSQL("DROP TABLE IF EXISTS Modelos");
		
		
		
		//Se crea la nueva versión de la tabla
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreate2);
		db.execSQL(sqlCreate3);
		db.execSQL(sqlCreate4);
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		
		//insertarlocalizadores(db);
		
		
	}



}
