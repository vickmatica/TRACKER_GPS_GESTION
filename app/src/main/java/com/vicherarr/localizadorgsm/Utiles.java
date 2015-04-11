/*AUTOR: VICTOR HERRERA ARRIBAS
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */
package com.vicherarr.localizadorgsm;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


public class Utiles  {
	public static final String tag="TRACKER_GPS_GESTION_Utiles";
	public static final int TIEMPO_MENSAJEALERTA=3000;
	  
	  //Campos configurables variables utilizados en funcion decodificar
	  public final static String IDTELEFONO="#telefono";
      public final static String IDLOCALIZADOR="#localizador";
      public final static String IDCLAVE="#clave";

public static final String IDTRASTREO = "#tiempo";
public static final String IDVECES = "#veces";

protected static final int NUMEROREGISTROSHISTORICOSALIDA = 2000;

private static final long RETARDOALARMA = 5000;

private static final float GROSOR_ARISTA = 0;

private static final int COLOR_FONDO = Color.argb(25, 0, 0, 255);

private static final long DURACION_ANIMACION = 1500;



public static Dialog dialogosaldo;

private PendingIntent operacion;

protected Marker marcadordondeestoy;

private Circle circulo;
private MarkerOptions opcionesmarcador;



protected static Document doc;

//private OverlavLocalizador overlayubicacion;



public void mostrartexto(String texto, Context contexto){
	Toast.makeText(contexto, texto, Toast.LENGTH_LONG).show();
	
}


//Para calcular distancia en api v2 google maps
public static double CalcularDistancia(double lat1,double lon1, double lat2,double lon2) {
  int Radius=6371;//Radio del planeta en kilómetros         
  double dLat = Math.toRadians(lat2-lat1);
  double dLon = Math.toRadians(lon2-lon1);
  double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
  Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
  Math.sin(dLon/2) * Math.sin(dLon/2);
  double c = 2 * Math.asin(Math.sqrt(a));
  double valueResult= Radius*c;
  double km=valueResult/1;
  DecimalFormat newFormat = new DecimalFormat("####");
  Integer kmInDec = Integer.valueOf(newFormat.format(km));
  double meter = valueResult%1000;
  Integer meterInDec = Integer.valueOf(newFormat.format(meter));
  Log.i("Valor del radio",""+valueResult+"   KM  "+kmInDec+" Metros   "+meterInDec);

  return Radius * c * 1000;
}


public static double redondear(double num, int dec) {
	return Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
}



public void notificacion(Context contexto, String titulo, String detalles, long cuando_milisegundos, Intent intent, int id_notificacion){
	NotificationManager notificationmanager = (NotificationManager)contexto.getSystemService(Context.NOTIFICATION_SERVICE);
		
		int icon = R.drawable.ic_launcher;
		PendingIntent pendingintent = PendingIntent.getActivity(contexto, id_notificacion, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		Notification notification =new Notification(icon, titulo, cuando_milisegundos);
		
	   
		
		notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
		
		notification.setLatestEventInfo(
				contexto, titulo,
				detalles, pendingintent);
				
		
		notificationmanager.notify(detalles,id_notificacion, notification);
		
		

}


public void resetalarmas(Context contexto){
	
	
	
	long milisegundosfechabd;
	Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD, 4);
	long mesescaducidad=Integer.valueOf(Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD, 4)).longValue();
    long milisegundosmesesbd=mesescaducidad*30*24*60*60*1000;
    
    
    Intent intent=new Intent(contexto,ServicioAlarma.class);

	Basedatos bd=new Basedatos(contexto);
	
    Cursor cursor=bd.consultarLocalizadores();
    
    //El siguiente bloque cancela todas las alarmas anteriores
    
    try {
    	
      if (cursor.moveToFirst()){
    	  do {
    		  int id=cursor.getInt(cursor.getColumnIndex("_id"));
     		 
     		  PendingIntent pendingintent=PendingIntent.getService(contexto, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
     		  
     		  ServicioRecepcion.alarmmanager.cancel(pendingintent);
     		   
    	  } while (cursor.moveToNext());
      }	
    }catch (Exception e){};
 
    
   //Lanza una alarma por cada localizador
    	if (cursor.moveToFirst()){
    	   	
    		do {
    		Date fechabd;
    	    long milisegundosalarma;
    	    	 
    		 String cadenafechabd=cursor.getString(cursor.getColumnIndex("fecharecarga"));
    		 fechabd=Manejadorfechas.deStringToDate(cadenafechabd);
    		 
    		 milisegundosfechabd=fechabd.getTime();
    		 milisegundosalarma=milisegundosfechabd+milisegundosmesesbd;
    		 
    		 //Alarma de pruebas para simular: desactivar esta línea para funcionamiento normal
    		 //milisegundosalarma=Manejadorfechas.DateActual().getTime()+20000;
    		 
    		 ServicioRecepcion.alarmmanager=(AlarmManager)contexto.getSystemService(Context.ALARM_SERVICE);
    		 
    		 
    		 
    		 String mensaje=contexto.getResources().getString(R.string.hanpasadomasde) + " " + Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD, 4) + " " + contexto.getResources().getString(R.string.mesessinrecargarsaldoenrastreador) + " " + cursor.getString(cursor.getColumnIndex("nombre")) + " " + cursor.getString(cursor.getColumnIndex("numero")) + " " + contexto.getResources().getString(R.string.ypodriaserdadodebaja);
    		 String mensaje2=contexto.getResources().getString(R.string.recarguesaldoen) + " " + cursor.getString(cursor.getColumnIndex("numero"));
    		 int id=cursor.getInt(cursor.getColumnIndex("_id"));
    		 intent.putExtra(ServicioRecepcion.KEY_MENSAJE, mensaje);
    		 intent.putExtra(ServicioRecepcion.KEY_MENSAJE2, mensaje2);
    		 intent.putExtra(ServicioAlarma.KEY_ID,id);
    		 
    		 operacion=PendingIntent.getService(contexto, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    		 
    		 
    		 
    	
			long milisegundosalarmaestablecida=milisegundosalarma;
    		 
    		 //Si la fecha actual es mayor que la fechacaducidad+mesescaducidad, establece alarma inmediatemente en 10 segundos
    		 if (milisegundosalarma<=Manejadorfechas.DateActual().getTime()) milisegundosalarmaestablecida=Manejadorfechas.DateActual().getTime()+RETARDOALARMA; 
    		 
    		 
    		 //Repetición diaria de cada alarma
    		 //ServicioRecepcion.alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, milisegundosalarmaestablecida, 24*60*60*1000 ,operacion);
    		 
    		 //Alarma sin repetición diaria
    		 ServicioRecepcion.alarmmanager.set(AlarmManager.RTC_WAKEUP, milisegundosalarmaestablecida, operacion);
             
    		     		 
    		 
    		}while (cursor.moveToNext());
    	}
    
  
      try {
       bd.cerrarbd();	  
      } catch (Exception e){Log.w("No se pudo cerrar bd", "No se pudo cerrar bd");}

}



public static float redondear(float unrounded, int precision, int roundingMode)
{
    BigDecimal bd = new BigDecimal(unrounded);
    BigDecimal rounded = bd.setScale(precision, roundingMode);
    return rounded.floatValue();
}


public Dialog dialogocontinuarllamando(final Context c)
{
 	
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
 
    builder.setTitle(c.getResources().getString(R.string.importante));
    builder.setMessage(c.getResources().getString(R.string.pulsellamaryrealiceunallamadaperdida));
    
   
   OnClickListener listenercontinuar=new OnClickListener(){



	private int RETARDOLLAMADA=7000;

	public void onClick(DialogInterface arg0, int arg1) {
		Intent intent_llamar = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Main.preferencias.getString(Main.KEY_NUMTELLOCALIZADOR, "")));
		 c.startActivity(intent_llamar);
		  
		new Handler().postDelayed(new Runnable() {
	          	public void run() {
	            
	          	
	          		Toast.makeText(c, c.getResources().getString(R.string.finaliceestallamada), Toast.LENGTH_LONG).show();
	          	
	          	
	          	
	          	};
	          	}, RETARDOLLAMADA);
	  	  
		
	}};
builder.setPositiveButton(c.getResources().getString(R.string.llamada), listenercontinuar).setIcon(R.drawable.ic_llamar);





OnClickListener listenercancelar=new OnClickListener(){

	public void onClick(DialogInterface dialog, int which) {
	}};
builder.setNegativeButton(c.getResources().getString(R.string.Cancelar), listenercancelar);


    builder.show();
    
    return builder.create();
}







public static void reproducirsonido(Context contexto, int resid){
	 MediaPlayer player = MediaPlayer.create(contexto, resid);
	 
	 
	 try {
		player.prepare();
	} catch (IllegalStateException e) {
		// TODO Bloque catch generado automáticamente
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Bloque catch generado automáticamente
		e.printStackTrace();
	}
     player.start();
     
}



//Utilizado para redimensionar la foto
	public static Bitmap redimensionarImagen(Bitmap mBitmap, float newWidth, float newHeigth){
		   //Redimensionamos
		   Bitmap bitmapaux;
		   		   
		   try
		   {
		   int width = mBitmap.getWidth();
		   int height = mBitmap.getHeight();
		   float scaleWidth = newWidth / width;
		   float scaleHeight = newHeigth / height;
		   // create a matrix for the manipulation
		   Matrix matrix = new Matrix();
		   // resize the bit map
		   matrix.postScale(scaleWidth, scaleHeight);
		   // recreate the new Bitmap
		   bitmapaux=Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
		   }
		   catch (Exception e)
		   {
			Log.i("Utiles.java","Imposible redimensionar foto");
			return null;   
		   }
		   return bitmapaux; 
		}
	

//Extrae la dirección mediante geocoding inverso	
public static String obtenerdirecciondesms(Context contexto, String mensajesms, String numerosms){
String direccion="";
String localidad="";
String provincia="";
String retorno="";
Geocoder geocoder = new Geocoder(contexto, Locale.getDefault());

try {
	
	
	double lat;
	double lng;
	
	
	Basedatos bd=new Basedatos(contexto);
    String modelo=bd.consultarModelo(numerosms);
    bd.cerrarbd();
	
    
    int modeloasumido=Main.detectarmodelo(mensajesms);
    
	//Si no se detecta modelo, el modelo asumido para mensaje es el configurado
	   if (modeloasumido==Main.MODELODESCONOCIDO) 
	   {  	if (modelo.contentEquals("TK-102")) modeloasumido=Main.MODELOTK102;
	   		if (modelo.contentEquals("TK-110")) modeloasumido=Main.MODELOTK110; 
	   }
	   
	   if (modeloasumido!=Main.MODELOTK110){
		   lat = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLATITUD),contexto);
 	       lng = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLONGITUD),contexto);
		   }
		   else
		   {
	                if (Main.validartk110(mensajesms)){ 		  
					lat = Utiles.ExtraerDouble(mensajesms.substring(Main.INICIOLATTK110,Main.FINLATTK110), 1,contexto);
		        	lng = Utiles.ExtraerDouble(mensajesms.substring(Main.INICIOLONGTK110,Main.FINLONGTK110), 1,contexto);
	                } else throw new ExcepcionFormatoMensaje("Mensaje sms no pertenece a tk110"); 
		   }
	
    
       
        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
		   
		direccion = addresses.get(0).getAddressLine(0);
		localidad = addresses.get(0).getSubLocality();
		provincia = addresses.get(0).getLocality();
        
        
	
} catch (ExcepcionFormatoMensaje e) {
	// TODO Bloque catch generado automáticamente
	
	
	
	e.printStackTrace();
} catch (IOException e) {
	// TODO Bloque catch generado automáticamente
  e.printStackTrace();
}

catch (Exception e){
  e.printStackTrace();
}
if (direccion!=null) retorno+=direccion;
if (localidad!=null) retorno+="\n"+localidad;
if (provincia!=null) retorno+="\n"+provincia;

return retorno;
}
    
//Extrae la dirección mediante geocoding inverso
public static String obtenerdirecciondelatlng(Context contexto, LatLng puntomapa){
String direccion="";
String localidad="";
String provincia="";
String retorno="";
Geocoder geocoder = new Geocoder(contexto, Locale.getDefault());

try {
	double lat = puntomapa.latitude;
	double lng = puntomapa.longitude;
	List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
	direccion = addresses.get(0).getAddressLine(0);
	localidad = addresses.get(0).getSubLocality();
	provincia = addresses.get(0).getLocality();
	
} 
catch (Exception e) {
	// TODO Bloque catch generado automáticamente
	
	e.printStackTrace();
}
if (direccion!=null) retorno+=direccion;
if (localidad!=null) retorno+="\n"+localidad;
if (provincia!=null) retorno+="\n"+provincia;

return retorno;
}

	
    //AlertDialog para confirmar envío de sms preparado
  	public AlertDialog confirmarenvio(Context contexto, String titulo, String mensaje, String comandosms, int icono)
  	{
  	  
  		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
  	 
  	    builder.setTitle(titulo);
  	    builder.setMessage(mensaje);
  	    if (icono!=0) builder.setIcon(icono);
  		final String comandoSMSaux=comandosms;    
  		final Context contextoaux=contexto;
  	   
  	    builder.setNegativeButton("No", new OnClickListener() {
  		    
  	

  			public void onClick(DialogInterface arg0, int arg1) {
  				// TODO Auto-generated method stub
  				 Log.i("Dialogos", "pulsado no.");
  			        
  		         
  			}
  		    });

  	  
  	    
  	    builder.setPositiveButton(contexto.getResources().getString(R.string.si), new OnClickListener() {
  	    
  	    

  		public void onClick(DialogInterface arg0, int arg1) {
  			// TODO Auto-generated method stub
  			 Log.i("Dialogos", "pulsado si.");
  		       
  			 Utiles.enviarSMS(Main.leer(Main.KEY_NUMTELLOCALIZADOR), comandoSMSaux, contextoaux);        
  		  
  		     
  		}
  	    });
  	    
  	    builder.show();
  	    
  	    return builder.create();
  	}

    
  	 //AlertDialog para confirmar envío de sms preparado
  	//Unicamente utilizado para TK110 (PORQUE ENVIA 2 SMS EN LA CONFIGURACION INICIAL)
  	public AlertDialog confirmarenvio2(Context contexto, String titulo, String mensaje, String comandosms, int icono)
  	{
  	  
  		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
  	 
  	    builder.setTitle(titulo);
  	    builder.setMessage(mensaje);
  	    if (icono!=0) builder.setIcon(icono);
  		final String comandoSMSaux=comandosms;    
  		final Context contextoaux=contexto;
  	   
  	    builder.setNegativeButton(contexto.getResources().getString(R.string.no), new OnClickListener() {
  		    
  	

  			public void onClick(DialogInterface arg0, int arg1) {
  				// TODO Auto-generated method stub
  				 Log.i("Dialogos", "pulsado no.");
  			        
  		         
  			}
  		    });

  	  
  	    
  	    builder.setPositiveButton(contexto.getResources().getString(R.string.si), new OnClickListener() {
  	    
  	    

  		public void onClick(DialogInterface arg0, int arg1) {
  			// TODO Auto-generated method stub
  			 Log.i("Dialogos", "pulsado si.");
  		       
  			Utiles.enviarSMS(Main.leer(Main.KEY_NUMTELLOCALIZADOR), comandoSMSaux, contextoaux);        
  			Utiles.enviarSMS(Main.leer(Main.KEY_NUMTELLOCALIZADOR), Utiles.decodificar("#720#3##clave##"), contextoaux);
  		     
  		}
  	    });
  	    
  	    builder.show();
  	    
  	    return builder.create();
  	}
  	
  	
    //Dialogo para ir al menú de configuración
	public Dialog confirmarirconfiguracion(Context contexto  )
  	{
		String titulo=contexto.getResources().getString(R.string.ningunlocalizador); 
		String mensaje=contexto.getResources().getString(R.string.deseacrearnuevolocalizador);
  	  
  		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
  	 
  	    builder.setTitle(titulo);
  	    builder.setMessage(mensaje);
  	    
  		 
  		final Context contextoaux=contexto;
  	   
  	    builder.setNegativeButton(contexto.getResources().getString(R.string.no), new OnClickListener() {
  		    
  	

  			public void onClick(DialogInterface arg0, int arg1) {
  				// TODO Auto-generated method stub
  				 Log.i("Dialogos", "pulsado no.");
  			        
  		         
  			}
  		    });

  	  
  	    
  	    builder.setPositiveButton(contexto.getResources().getString(R.string.si), new OnClickListener() {
  	    
  	    

  		public void onClick(DialogInterface arg0, int arg1) {
  			// TODO Auto-generated method stub
  			 Log.i("Dialogos", "pulsado si.");
  		       
  			 Intent intent=new Intent(contextoaux,NuevoLocalizador.class);
  			 contextoaux.startActivity(intent);
  		     
  		}
  	    });
  	    
  	    builder.show();
  	    
  	    return builder.create();
  	}

    
	
	//Dialogo para ir al menú de configuración
		public Dialog dialogocontinuar(Context contexto, String titulo, String mensaje  )
	  	{
			
	  		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
	  	 
	  	    builder.setTitle(titulo);
	  	    builder.setMessage(mensaje);
	  	    
	  		 
	  	   
	  	  	  	  
	  	    
	  	    builder.setPositiveButton(contexto.getResources().getString(R.string.Continuar), new OnClickListener() {
	  	    
	  	    

	  		public void onClick(DialogInterface arg0, int arg1) {
	  			// TODO Auto-generated method stub
	  			 Log.i("Dialogos", "pulsado si.");
	  		       
	  		     
	  		}
	  	    });
	  	    
	  	    builder.show();
	  	    
	  	    return builder.create();
	  	}
	
	
	
  	
	  /*METODO OBSOLETO: USADO EN API1 GOOGLE MAPS
      public static GeoPoint calcularGeoPoint(double latitud, double longitud) {
    	    Double latE6 = latitud * 1E6;
    	    Double lngE6 = longitud * 1E6;
    	    return  new GeoPoint(latE6.intValue(), lngE6.intValue());
    	}

      */
      //Redimensionar drawable
      public static Drawable redimensionardrawable(Drawable image, int anchura, int altura) {
    	    Bitmap d = ((BitmapDrawable)image).getBitmap();
    	    Bitmap bitmapOrig = Bitmap.createScaledBitmap(d, anchura, altura, false);
    	    return new BitmapDrawable(bitmapOrig);
    	}
      
      
      public void dibujarlistaposiciones(List<LatLng> listapos,  GoogleMap mapa, Context contexto){
      	
    	  if (this.marcadordondeestoy!=null) this.marcadordondeestoy.remove();
    	  if (this.circulo!=null) this.circulo.remove();
    	  
    	  LatLng pos = null;
		//Esta es la pos que capta mapa google maps. Tambien guardo la recibida por gps, por logueo
    	  for (int i=0; i<listapos.size();i++)
    	  {
    	  pos=listapos.get(i);
    	  
    	  BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.drawable.ic_dondeestoy);
    	
    		MarkerOptions opcionesmarcador = new MarkerOptions();

      		opcionesmarcador.position(pos);

      		
      	    opcionesmarcador.title(contexto.getResources().getString(R.string.estoyaqui)); 

      	    opcionesmarcador.icon(icono);
            /*
      		CircleOptions options = new CircleOptions();
			
			
		    
		       options.center(pos);
				
				
				options.radius(precision);
													
			
				
				
				circulo = mapa.addCircle(options);
				circulo.setStrokeColor(Color.BLUE);
			    circulo.setStrokeWidth(GROSOR_ARISTA);
			    circulo.setFillColor(COLOR_FONDO);
				*/
				marcadordondeestoy=mapa.addMarker(opcionesmarcador);
				marcadordondeestoy.showInfoWindow();
	      		//marcadorstreetview=mapa.addMarker(opcionesmarcadorstreetview);
    	  }
      }

      
      
      public void dibujardondeestoy(LatLng pos, long precision, GoogleMap mapa, Context contexto){
    	
    	  if (this.marcadordondeestoy!=null) this.marcadordondeestoy.remove();
    	  if (this.circulo!=null) this.circulo.remove();
    	  //Esta es la pos que capta mapa google maps. Tambien guardo la recibida por gps, por logueo
    	  pos=new LatLng(mapa.getMyLocation().getLatitude(),mapa.getMyLocation().getLongitude());
    	  
    	  BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.drawable.ic_dondeestoy);
    	
    		MarkerOptions opcionesmarcador = new MarkerOptions();

      		opcionesmarcador.position(pos);

      		
      	    opcionesmarcador.title(contexto.getResources().getString(R.string.estoyaqui)); 

      	    opcionesmarcador.icon(icono);
   
      		CircleOptions options = new CircleOptions();
			
			
		    
		       options.center(pos);
				
				
				options.radius(precision);
													
			
				
				
				circulo = mapa.addCircle(options);
				circulo.setStrokeColor(Color.BLUE);
			    circulo.setStrokeWidth(GROSOR_ARISTA);
			    circulo.setFillColor(COLOR_FONDO);
				
				marcadordondeestoy=mapa.addMarker(opcionesmarcador);
				//marcadordondeestoy.showInfoWindow();
	      		//marcadorstreetview=mapa.addMarker(opcionesmarcadorstreetview);
    	  
      }
      
      //Método que dibuja el localizador 
      public MarkerOptions dibujarlocalizador(LatLng pos, GoogleMap mapa, String numero, String nombre, String mensaje, Drawable iconolocalizador, Context contexto) {
   	    Basedatos bd=new Basedatos(contexto);
    	String modeloseleccionado=bd.consultarsubmodelo(numero);
    	//Drawable icono = iconolocalizador;  
    	
    	BitmapDescriptor icono = null;
    	
    	
		if (modeloseleccionado.contentEquals("TK-101")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_tk101);
    	if (modeloseleccionado.contentEquals("TK-102")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_modelotk102);
    	if (modeloseleccionado.contentEquals("TK102B")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_modelotk102);
    	if (modeloseleccionado.contentEquals("TK-102-2")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_modelotk102);
    	if (modeloseleccionado.contentEquals("TK-103")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_tk103);
    	if (modeloseleccionado.contentEquals("TK103")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_tk103);
    	if (modeloseleccionado.contentEquals("TK-103-2")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_tk103_2);
    	if (modeloseleccionado.contentEquals("TK103-B")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_tk103_2);
    	if (modeloseleccionado.contentEquals("TK-110")) icono=BitmapDescriptorFactory.fromResource(R.drawable.imagen_tk110);
    	if (modeloseleccionado.contentEquals("TK-206")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_tk206);
    	if (modeloseleccionado.contentEquals("TK-201")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_201);
    	if (modeloseleccionado.contentEquals("GPS 102")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_modelotk102);
    	if (modeloseleccionado.contentEquals("GPS 102-B")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_modelotk102);
    	if (modeloseleccionado.contentEquals("GPS 103-A")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_gps103a);
    	if (modeloseleccionado.contentEquals("GPS 103-B")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_gps103b);
    	if (modeloseleccionado.contentEquals("GPS 104")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_gps104);
    	if (modeloseleccionado.contentEquals("TK104")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_gps104);
    	if (modeloseleccionado.contentEquals("GPS 106-A")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_106);
    	if (modeloseleccionado.contentEquals("GPS 106-B")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_106);
    	if (modeloseleccionado.contentEquals("TK106")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_106);
    	if (modeloseleccionado.contentEquals("TK106B")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_106);
    	if (modeloseleccionado.contentEquals("TK-118")) icono=BitmapDescriptorFactory.fromResource(R.drawable.ic_tk118);
    	
    	   	
    	
    	
  		//LocalizadorOverlay markers = new LocalizadorOverlay(icono, mapa);
  		//markers.addPosicion(pos, "Número: "+ numero, "Texto del mensaje: " + nombre);
  		
  		MarkerOptions opcionesmarcador=new MarkerOptions();
  		
  		MarkerOptions opcionesmarcadorstreetview=new MarkerOptions();
  		
  		
  		opcionesmarcador.position(pos);
  		opcionesmarcadorstreetview.position(pos);
  		
  	    opcionesmarcador.title(contexto.getResources().getString(R.string.num_telefono) + ": " + numero); 
  	    opcionesmarcador.snippet(contexto.getResources().getString(R.string.textodelsms) + ": " + mensaje);
  	    opcionesmarcadorstreetview.title(contexto.getResources().getString(R.string.num_telefono) + ": " + numero); 
	    opcionesmarcadorstreetview.snippet(contexto.getResources().getString(R.string.textodelsms) + ": " + mensaje);
		
  	    
  	    if (icono!=null) opcionesmarcador.icon(icono);
  	    if (icono!=null) opcionesmarcadorstreetview.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_streetview));
		
  	    
  	    
  	    mapa.addMarker(opcionesmarcador);
  		mapa.addMarker(opcionesmarcadorstreetview);
  		
  		//mapa.getOverlays().add(markers);
  		//mapa.postInvalidate();
  		bd.cerrarbd();
  		return opcionesmarcador;
  	}

      
      
        
      
      
      
	//En un String que contenga varios números double (reales)
    //devuelve de los números, el número que ocupe la posición indicada
    //Nota: La posicion 1 es el primero. (no el 0)
    public static Double ExtraerDoubleSeparadorComa(String texto, int posicion, Context contexto) throws ExcepcionFormatoMensaje{
        Double retorno=null;
        StringTokenizer st = new StringTokenizer(texto);  
        LinkedList<Double> lista=new LinkedList<Double>();
        
        //Siguiente línea solo para localizador TK102, hay veces que devuelve mensaje sin coordenadas
        if (texto.startsWith("lat: long: ")) throw new ExcepcionFormatoMensaje("ExcepcionFormatoMensaje: lat: long: sin coordenadas");
        
        
        //Inicialmente suponemos que no es numero
        boolean esnumero=false;            
                
        //En esta variable recogeremos el numero a retornar
        Double numero = Double.valueOf(0);
        
        //En nf posteriormente guardaremos el número extraido
        DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        DecimalFormat nf = new DecimalFormat("#.##########");
        nf.setDecimalFormatSymbols(simbolo);
        
        
        
                    //Recorre el texto buscando palabras
			        while (st.hasMoreTokens()) {
			        
			        //Lee el separador de coordenadas configurado: por defecto
			        //Sustituir por coma o el correspondiente 
			        //Util cuando las coordenadas son extraidas de google maps
			        //en formato por ejemplo:  -34.7,0.45
			        //sin que haya ningún espacio entre coordenadas (en este caso asignar "," como separador
			        //en variable Main.VALORSEPARADORCOORDENADAS
			        
			        String SEPARADORaux=",";
			        
			        //Si el separador no esta configurado, establece espacio como separador
			        //de coordenadas por defecto.
			        //Una latitud y longitud validas serían como ejemplo:  -34.7   0.45
			        //Para formato de coordenadas de google maps, establezca coma como separador
			        //por defecto
			        if (SEPARADORaux.contentEquals("")) SEPARADORaux=" ";
			        
			               
			        String palabra=new StringBuffer().append(st.nextToken(SEPARADORaux)).toString();       
			        
			        			        
			        
			        //Quita todo lo que no son números o caracteres . o , o -        
			        palabra=palabra.replaceAll("[^0-9|\\,\\.\\-]", "");        
			        
			        
			        //Convierte comas a puntos: Esto es para que se pueda convertir
			        //a Double sin problemas
			        palabra=palabra.replaceAll("[\\,]", "\\.");        
			        
			        
			        
			        
			        palabra=palabra.trim();
					
			           {
			           
				           //quita todos los . al principio
					       //El número reconocido como tal no puede ser 
			        	   //un solo punto ni comenzar por puntos
				            while (palabra.startsWith(".") && !palabra.contentEquals("."))
					                try {
				            		//Elimina puntos al principio
					                palabra=palabra.substring(1, palabra.length()-1);
					                //Elimina puntos al final
					            	palabra=palabra.substring(0, palabra.lastIndexOf('.'));
					                } 
				                    catch (java.lang.StringIndexOutOfBoundsException e){e.printStackTrace();}
				            	
					        
	                        }
				            //Si solo fuera . lo marcamos como no numero para no añadirlo a lista
			            if (palabra.contentEquals(".")) esnumero=false; 
			        
			        palabra=palabra.trim();
			        
			        
			        Log.i("PALABRA: ", palabra);
			        
			        
			        
			        // Toast.makeText(contexto, "Textonumeroextraido:" + palabra, Toast.LENGTH_LONG).show();
			            try {
			            
			               
			            	numero = nf.parse(palabra).doubleValue();
			            
			            
			            
			            //Toast.makeText(contexto, "numeroextraido:" + numero, Toast.LENGTH_LONG).show();
			            Log.i("Numero extraido:", "Numero: "+Double.toString(numero));
			            esnumero=true;
			            } catch (ParseException e) {
			                
			            	//Log.i("Palabra del SMS no reconocida como número", "Palabra del SMS no reconocida como número");
			            }
			
			                
			            
			          if (esnumero) lista.add(numero); 
			                
			          esnumero=false;  
			         }      
        
        
        if (posicion>lista.size() || posicion<0) throw new ExcepcionFormatoMensaje("ExcepcionFormatoMensaje: No contiene números en posición indicada");
        
        try{
        retorno=lista.get(--posicion);
        }
        catch (Exception e){e.printStackTrace();}
        return retorno;
    }
	
    
    
    
    
    
    public static Double ExtraerDouble(String texto, int posicion, Context contexto) throws ExcepcionFormatoMensaje{
        Double retorno=null;
        StringTokenizer st = new StringTokenizer(texto);  
        LinkedList<Double> lista=new LinkedList<Double>();
        
        //Siguiente línea solo para localizador TK102, hay veces que devuelve mensaje sin coordenadas
        if (texto.startsWith("lat: long: ")) throw new ExcepcionFormatoMensaje("ExcepcionFormatoMensaje: lat: long: sin coordenadas");
        
        
        //Inicialmente suponemos que no es numero
        boolean esnumero=false;            
                
        //En esta variable recogeremos el numero a retornar
        Double numero = Double.valueOf(0);
        
        //En nf posteriormente guardaremos el número extraido
        DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        DecimalFormat nf = new DecimalFormat("#.##########");
        nf.setDecimalFormatSymbols(simbolo);
        
        
        
                    //Recorre el texto buscando palabras
			        while (st.hasMoreTokens()) {
			        
			        //Lee el separador de coordenadas configurado: por defecto
			        //Sustituir por coma o el correspondiente 
			        //Util cuando las coordenadas son extraidas de google maps
			        //en formato por ejemplo:  -34.7,0.45
			        //sin que haya ningún espacio entre coordenadas (en este caso asignar "," como separador
			        //en variable Main.VALORSEPARADORCOORDENADAS
			        String SEPARADOR=Main.leer(Main.KEY_SEPARADORCOORDENADAS);
			        
			        String SEPARADORaux=SEPARADOR;
			        
			        //Si el separador no esta configurado, establece espacio como separador
			        //de coordenadas por defecto.
			        //Una latitud y longitud validas serían como ejemplo:  -34.7   0.45
			        //Para formato de coordenadas de google maps, establezca coma como separador
			        //por defecto
			        if (SEPARADORaux.contentEquals("")) SEPARADORaux=" ";
			        
			               
			        String palabra=new StringBuffer().append(st.nextToken(SEPARADORaux)).toString();       
			        
			        			        
			        
			        //Quita todo lo que no son números o caracteres . o , o -        
			        palabra=palabra.replaceAll("[^0-9|\\,\\.\\-]", "");        
			        
			        
			        //Convierte comas a puntos: Esto es para que se pueda convertir
			        //a Double sin problemas
			        palabra=palabra.replaceAll("[\\,]", "\\.");        
			        
			        
			        
			        
			        palabra=palabra.trim();
					
			           {
			           
				           //quita todos los . al principio
					       //El número reconocido como tal no puede ser 
			        	   //un solo punto ni comenzar por puntos
				            while (palabra.startsWith(".") && !palabra.contentEquals("."))
					                try {
				            		//Elimina puntos al principio
					                palabra=palabra.substring(1, palabra.length()-1);
					                //Elimina puntos al final
					            	palabra=palabra.substring(0, palabra.lastIndexOf('.'));
					                } 
				                    catch (java.lang.StringIndexOutOfBoundsException e){e.printStackTrace();}
				            	
					        
	                        }
				            //Si solo fuera . lo marcamos como no numero para no añadirlo a lista
			            if (palabra.contentEquals(".")) esnumero=false; 
			        
			        palabra=palabra.trim();
			        
			        
			        Log.i("PALABRA: ", palabra);
			        
			        
			        
			        // Toast.makeText(contexto, "Textonumeroextraido:" + palabra, Toast.LENGTH_LONG).show();
			            try {
			            
			               
			            	numero = nf.parse(palabra).doubleValue();
			            
			            
			            
			            //Toast.makeText(contexto, "numeroextraido:" + numero, Toast.LENGTH_LONG).show();
			            Log.i("Numero extraido:", "Numero: "+Double.toString(numero));
			            esnumero=true;
			            } catch (ParseException e) {
			                
			            	//Log.i("Palabra del SMS no reconocida como número", "Palabra del SMS no reconocida como número");
			            }
			
			                
			            
			          if (esnumero) lista.add(numero); 
			                
			          esnumero=false;  
			         }      
        
        
        if (posicion>lista.size() || posicion<0) throw new ExcepcionFormatoMensaje("ExcepcionFormatoMensaje: No contiene números en posición indicada");
        
        try{
        retorno=lista.get(--posicion);
        }
        catch (Exception e){e.printStackTrace();}
        return retorno;
    }
	
    
    
    
    
    
    
    
    //¿Contiene url google maps?
    public boolean esurlgooglemaps(String texto, Context contexto){
    boolean retorno=false;
    StringTokenizer st = new StringTokenizer(texto);  
    
     
    
    
    
                //Recorre el texto buscando palabras
		        while (st.hasMoreTokens()) {
		        
		        String SEPARADORaux=" ";
		        
		        
		               
		        String palabra=new StringBuffer().append(st.nextToken(SEPARADORaux)).toString();       
		        
		        			        
		        
	            palabra=palabra.trim();
				
		        
		        
		        
		        
		
		                
		            
			          if (palabra.startsWith("http://maps.google.com"))
			          {	  
			          retorno=true;
					  break;
			          }      
		            
		         }      

    
    
    
    return retorno;
    
    }
    
    
    //Extrae latitud de url google maps
    public Double Extraerlongitudurlgooglemaps(String texto, Context contexto){
    String aux="";	
    
    Double retorno=null;
    StringTokenizer st = new StringTokenizer(texto);  
    
     
    
    
    
                //Recorre el texto buscando palabras
		        while (st.hasMoreTokens()) {
		        
		        String SEPARADORaux=" ";
		        
		        
		               
		        String palabra=new StringBuffer().append(st.nextToken(SEPARADORaux)).toString();       
		        
		        			        
		        
	            palabra=palabra.trim();
				
		        
		        
		        
		        
		
		                
		            
			          if (palabra.startsWith("http://maps.google.com"))
			          {	  
			          aux=palabra;
						          try {
									retorno=Utiles.ExtraerDoubleSeparadorComa(aux, 2, contexto);
								} catch (ExcepcionFormatoMensaje e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			          break;
			          }      
		            
		         }      

    
    
    
    return retorno;
    
    }

    
    
    //Extrae latitud de url google maps
    public Double Extraerlatitudurlgooglemaps(String texto, Context contexto){
    String aux="";	
    
    Double retorno=null;
    StringTokenizer st = new StringTokenizer(texto);  
    
     
    
    
    
                //Recorre el texto buscando palabras
		        while (st.hasMoreTokens()) {
		        
		        String SEPARADORaux=" ";
		        
		        
		               
		        String palabra=new StringBuffer().append(st.nextToken(SEPARADORaux)).toString();       
		        
		        			        
		        
	            palabra=palabra.trim();
				
		        
		        
		        
		        
		
		                
		            
			          if (palabra.startsWith("http://maps.google.com"))
			          {	  
			          aux=palabra;
						          try {
									retorno=Utiles.ExtraerDoubleSeparadorComa(aux, 1, contexto);
								} catch (ExcepcionFormatoMensaje e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			          break;
			          }      
		            
		         }      

    
   
    
    return retorno;
    
    }
    
    
    public static String cerosaconcatenar(int numero){
    String ceros="";
    int aux=numero;
         int contador=0;
         while (aux>0){ 
         contador++; 
         aux=aux/10; 
         }
         if (contador==3) ceros="";
         if (contador==2) ceros="0";
         if (contador==1) ceros="00";
     return ceros;    
    }
    
  //Utilizado para obtener el comando sms resultante con campos variables
  		//como #telefono, #localizador configurados en configuración
  		//por ejemplo:  localizar #telefono  devolvería: localizar 661864857
  		//si se ha configurado ese número
  		public static String decodificar (String texto){
  			String cadena=texto;
  			StringTokenizer st = new StringTokenizer(texto);  
  			StringBuffer stringbuffer=new StringBuffer();
  			String palabra;
  			  while (st.hasMoreTokens()) 
  			  {   
  				  palabra=st.nextToken().toString();
  				  if (palabra.contains(IDTELEFONO)) palabra=palabra.replace(IDTELEFONO, Main.leer(Main.KEY_MINUMTEL));
  				  if (palabra.contains(IDLOCALIZADOR)) palabra=palabra.replace(IDLOCALIZADOR, Main.leer(Main.KEY_NUMTELLOCALIZADOR));
  				  if (palabra.contains(IDCLAVE)) palabra=palabra.replace(IDCLAVE, Main.leer(Main.KEY_CLAVELOCALIZADOR));
  				  if (palabra.contains(IDTRASTREO))
  				  {	  
  		             try {
  		            	String ceros=cerosaconcatenar(Integer.valueOf(Main.leerint(Main.KEY_TIEMPOAUTORASTREO)));                                       			  
  	  					
  		              palabra=palabra.replace(IDTRASTREO, ceros + Integer.valueOf(Main.leerint(Main.KEY_TIEMPOAUTORASTREO)).toString());
  					  }
  				      catch (Exception e){e.printStackTrace();}
  				  }
  				  if (palabra.contains(IDVECES))
  					  try {
  						String ceros=cerosaconcatenar(Integer.valueOf(Main.leerint(Main.KEY_VECESAUTORASTREO)));
  	  					 palabra=palabra.replace(IDVECES, ceros + Integer.valueOf(Main.leerint(Main.KEY_VECESAUTORASTREO)).toString());
  					  }
  				      catch (Exception e){e.printStackTrace();}
  				  
  				  stringbuffer=stringbuffer.append(palabra+" "); 
  				  
  			  }   
  			 
  			 cadena=stringbuffer.toString();
  			 cadena=cadena.trim();
  			 Log.v("Decodificada cadena", "retorno: "+cadena);
  			 return cadena;
  		  }
	
	//Envía SMS 
	public static void enviarSMS(String numeroTelefono, String mensaje, Context context)
    {   
		String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
	    
	    final String mensajesms=mensaje;
	    final String numerosms=numeroTelefono;
	    
	    Log.v("NumeroTelefono",numeroTelefono);
        Log.v("Mensaje",mensaje);
        
        
        // Crea parametro setIntent
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context,
        0,
        sentIntent,
        0);
        // Create parametro deliveryIntent
        Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context,
        0,
        deliveryIntent,
        0);
        // Registra el Broadcast Receivers
        context.registerReceiver(new BroadcastReceiver() {
        @Override
        public void onReceive(Context _context, Intent _intent) {
        Basedatos basedatos;
		switch (getResultCode()) {
        case Activity.RESULT_OK:
        
        Toast.makeText(_context, _context.getResources().getString(R.string.smsenviadoconexito), Toast.LENGTH_LONG).show();
       //Registra el sms en el historial de salida y borra los registros antiguos	
	    basedatos=new Basedatos(_context);
	    basedatos.insertarSalida(mensajesms,numerosms);
		basedatos.borrarultimosregistroshistoricoSalida(NUMEROREGISTROSHISTORICOSALIDA);
		basedatos.cerrarbd();        
        break;
        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
        Toast.makeText(_context, _context.getResources().getString(R.string.imposibleenviarsms), Toast.LENGTH_LONG).show(); break;
        case SmsManager.RESULT_ERROR_RADIO_OFF:
       	Toast.makeText(_context, _context.getResources().getString(R.string.sincobertura), Toast.LENGTH_LONG).show(); break;
        case SmsManager.RESULT_ERROR_NULL_PDU:
        Toast.makeText(_context, "Error PDU", Toast.LENGTH_LONG).show();  break;
        }
        }
        },
        
        new IntentFilter(SENT_SMS_ACTION));
        
        
        context.registerReceiver(new BroadcastReceiver() {
        @Override
        public void onReceive(Context _context, Intent _intent) {
         Toast.makeText(_context, _context.getResources().getString(R.string.smsentregadocorrectamente), Toast.LENGTH_LONG).show();
        }
        },
        new IntentFilter(DELIVERED_SMS_ACTION));
        
        
       // Create the deliveryIntent parameter  
       SmsManager smsManager = SmsManager.getDefault();
        
        // Send the message
        if (Utiles.validartelefono(numeroTelefono)) smsManager.sendTextMessage(numeroTelefono, null, mensaje, sentPI, deliverPI);
        else Toast.makeText(context, context.getResources().getString(R.string.numeroincorrecto), Toast.LENGTH_LONG).show();
          
        
    }    
	
	
	public static void CancelarNotificacion(Context ctx, int notifyId) {
	    String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
	    nMgr.cancel(notifyId);
	}

	
	
	  public static boolean validartelefono(String telefono){
		    boolean retorno=false;
		    
		    
		    
		    if (telefono.length()>=9 ) retorno=true;
		    
		    
		    return retorno;
		    }
	  
 
	  
	public static void alertasaldo(Context contexto, String numerosms){
		
		String mensajesaldo="";
		Basedatos basedatos=new Basedatos(contexto);
		String numerosmsbd=basedatos.obtenernumero(numerosms);
		Float saldo=basedatos.consultarsaldo(numerosmsbd);
		Main.preferencias.getFloat(Main.KEY_SALDOMINIMO_LOCALIZADOR,1);
		Float saldominimo=Main.preferencias.getFloat(Main.KEY_SALDOMINIMO_LOCALIZADOR,1);
		
		
		
        
		
		if (saldo<=0)
		{mensajesaldo=contexto.getResources().getString(R.string.saldode) + " " + basedatos.consultarnombre(numerosmsbd) + " " + contexto.getResources().getString(R.string.connumero) + " " +  numerosmsbd + " " + contexto.getResources().getString(R.string.agotado);
		
		//dialogosaldo = new Dialogo().mostrar(contexto, "Saldo del localizador", mensajesaldo);
		
		Toast.makeText(contexto, mensajesaldo, Toast.LENGTH_LONG).show();
		
		}
		else
		{
			if (Float.valueOf(saldo) < Float.valueOf(saldominimo)) 
		    {
		    
			mensajesaldo=contexto.getResources().getString(R.string.quedan) + " " + Utiles.redondear(saldo, 2, BigDecimal.ROUND_HALF_UP )+ " " + contexto.getResources().getString(R.string.desaldoen) + " " + basedatos.consultarnombre(numerosmsbd) + " " + contexto.getResources().getString(R.string.connumero) + " " + numerosmsbd + " " + contexto.getResources().getString(R.string.recarguesaldo);
			
			//dialogosaldo=new Dialogo().mostrar(contexto, "Saldo del localizador", mensajesaldo);
			
			Toast.makeText(contexto, mensajesaldo, Toast.LENGTH_LONG).show();
			Toast.makeText(contexto, mensajesaldo, Toast.LENGTH_LONG).show();
			
		    }
		    
		}
		
  	
		
		
		basedatos.cerrarbd();
	    
	}  
	 
	
	
public static void alertacaducidad(Context contexto, String numerosmsbd){
		
	
		Basedatos basedatos=new Basedatos(contexto);
		
		String cadenafecharecarga=basedatos.consultarfecharecarga(numerosmsbd);
		
		Date fecharecarga=Manejadorfechas.deStringToDate(cadenafecharecarga);
				
		Integer mesescaducidad=Main.preferencias.getInt(Main.KEY_MESES_CADUCIDAD,4);
		
		
		int dias=mesescaducidad*30;
		
		Date fechaactual = Manejadorfechas.DateActual();

		String mensaje=contexto.getResources().getString(R.string.advertenciarecarguesimdellocalizador) + " " + basedatos.consultarnombre(numerosmsbd) + ". " + contexto.getResources().getString(R.string.puedeserdadodebaja) + " " + mesescaducidad + " " + contexto.getResources().getString(R.string.mesessinrecargarsaldoenrastreador);
		 
		  if (Manejadorfechas.diferenciasDeFechas(fecharecarga,fechaactual)>=dias) 
			{
			  Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show();
			  Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show();
			}

        basedatos.cerrarbd();
	    
	}  
	
	
	public static boolean validarradio(String radio){
	boolean retorno=false;
	
	//En nf posteriormente guardaremos el número extraido
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator('.');
    DecimalFormat nf = new DecimalFormat("#.##########");
    nf.setDecimalFormatSymbols(simbolo);
    
	
	try {
        
        
    double num=nf.parse(radio).doubleValue();
    
    if (num>=10 && num<100000) retorno=true;
    else retorno=false;
    
    } catch (ParseException e) {
        
    retorno=false;
    	
    }
	
	return retorno;
	}
	
	
	
	public static boolean validarminutos(String radio){
		boolean retorno=false;
		
		//En nf posteriormente guardaremos el número extraido
	    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
	    simbolo.setDecimalSeparator('.');
	    DecimalFormat nf = new DecimalFormat("#.##########");
	    nf.setDecimalFormatSymbols(simbolo);
	    
		
		try {
	        
	        
	    double num=nf.parse(radio).doubleValue();
	    
	    if (num>=5 && num<60) retorno=true;
	    else retorno=false;
	    
	    } catch (ParseException e) {
	        
	    retorno=false;
	    	
	    }
		
		return retorno;
		}
	
	
	
     @SuppressLint("DefaultLocale")
	public static String traducirentrada(String mensaje, Context contexto){
     String traduccion="";
             /* 
             //Traducciones de localizador TK102
             if (mensaje.toLowerCase().contains("low battery")) traduccion="\n¡BATERIA BAJA! RECARGUE SU LOCALIZADOR. Mientras no lo haga recibirá un SMS cada 30 minutos ";
     	     if (mensaje.toLowerCase().startsWith("move ok")) traduccion="\nActivado detectar movimiento";
		     if (mensaje.toLowerCase().startsWith("nomove ok")) traduccion="\nDesactivado detectar movimiento";
		     if (mensaje.toLowerCase().contains("monitor ok")) traduccion="\nLocalizador: Escuchar voz activado. ATENCION: Recuerde que en modo escuchar no puede usarse como localizador. Debe volver a activar 'Modo localizar'";
			 if (mensaje.toLowerCase().contains("tracker ok")) traduccion="\nModo localizador activado";
			 if (mensaje.toLowerCase().contains("begin ok!")) traduccion="\nLocalizador: Reiniciada configuración";
			 if (mensaje.toLowerCase().contains("help me")) traduccion="\nLocalizador: Recibido SOS";
			 if (mensaje.toLowerCase().contains("low battery"))	traduccion="\nBaja batería del localizador";
			 if (mensaje.toLowerCase().contains("stockade!"))	traduccion="\nEl localizador está fuera del área limitada";
			 if (mensaje.toLowerCase().contains("stockade ok"))	traduccion="\nActivado límite de area";
			 if (mensaje.toLowerCase().startsWith("speed ok"))	traduccion="\nActivado límite de velocidad";
			 if (mensaje.toLowerCase().startsWith("nospeed ok"))	traduccion="\nDesactivado límite de velocidad";
			 if (mensaje.toLowerCase().contains("bat:l")) traduccion="\n¡BATERIA BAJA! RECARGUE SU LOCALIZADOR";
			 if (mensaje.toLowerCase().contains("signal:l")) traduccion="\nBaja señal GPS";
			 if (mensaje.toLowerCase().contains("add master ok")) traduccion="Teléfono principal de control añadido al localizador";
			 if (mensaje.toLowerCase().startsWith("admin ok")) traduccion="Teléfono de control añadido al localizador";
			 if (mensaje.toLowerCase().startsWith("noadmin ok")) traduccion="Teléfono desautorizado";
			 if ((mensaje.toLowerCase().contains("lat:")) && (mensaje.toLowerCase().contains("long:")) 
		     && mensaje.toLowerCase().contains("stockade"))  traduccion="\nLocalizador fuera del límite de área";
			 if (mensaje.toLowerCase().contains("t030s003n ok")) traduccion="\nAutorrastreo activado: 3 únicas veces cada 30 segundos";
			 if ((mensaje.toLowerCase().contains("lat:")) && (mensaje.toLowerCase().contains("long:")) 
			 && mensaje.toLowerCase().contains("move")) traduccion="\nEl localizador se ha movido";
			 			 	 			 
			 
			 //Traducciones de localizador TK110
			 if (mensaje.toLowerCase().contains("password err")) traduccion="\nContraseña incorrecta";
			 if (mensaje.toLowerCase().contains("config ok")) traduccion="\nConfiguración establecida correctamente";
			 if (mensaje.toLowerCase().contains("710 config ok")) traduccion="\nNúmero principal establecido correctamente";
			 if (mensaje.toLowerCase().contains("711 config ok")) traduccion="\nNúmero administrador establecido correctamente";
			 if (mensaje.toLowerCase().contains("770 config ok")) traduccion="\nContraseña cambiada correctamente";
			 if (mensaje.toLowerCase().contains("802 config ok")) traduccion="\nAPN de internet establecido correctamente";
			 if (mensaje.toLowerCase().contains("720 config ok")) traduccion="\nAjustes de alarma establecidos correctamente";
			 if (mensaje.toLowerCase().contains("751 config ok")) traduccion="\nActivado limitar área";
			 if (mensaje.toLowerCase().contains("750 config ok")) traduccion="\nActivado detectar movimiento";
			 if (mensaje.toLowerCase().contains("760 config ok")) traduccion="\nDesactivado detectar movimiento";
			 if (mensaje.toLowerCase().contains("out")) traduccion="\nAlarma: el localizador se ha movido";
			 */
			 
     if (mensaje.toLowerCase().contains("low battery")) traduccion=contexto.getResources().getString(R.string.bateriabaja);
	 if (mensaje.toLowerCase().startsWith("move ok")) traduccion=contexto.getResources().getString(R.string.activadodetectarmovimiento);
     if (mensaje.toLowerCase().startsWith("nomove ok")) traduccion=contexto.getResources().getString(R.string.desactivadodetectarmovimiento);
     if (mensaje.toLowerCase().contains("monitor ok")) traduccion=contexto.getResources().getString(R.string.escucharvozactivado);
	 if (mensaje.toLowerCase().contains("tracker ok")) traduccion=contexto.getResources().getString(R.string.modolocalizadoractivado);
	 if (mensaje.toLowerCase().contains("begin ok!")) traduccion=contexto.getResources().getString(R.string.reiniciadaconfiguracion);
	 if (mensaje.toLowerCase().contains("help me")) traduccion=contexto.getResources().getString(R.string.recibidosos);
	 if (mensaje.toLowerCase().contains("low battery"))	traduccion=contexto.getResources().getString(R.string.bateriabaja);
	 if (mensaje.toLowerCase().contains("stockade!"))	traduccion=contexto.getResources().getString(R.string.localizadorfueradearea);
	 if (mensaje.toLowerCase().contains("stockade ok"))	traduccion=contexto.getResources().getString(R.string.activadolimitararea);
	 if (mensaje.toLowerCase().startsWith("speed ok"))	traduccion=contexto.getResources().getString(R.string.activadolimitarvelocidad);
	 if (mensaje.toLowerCase().startsWith("nospeed ok"))	traduccion=contexto.getResources().getString(R.string.desactivadolimitarvelocidad);
	 if (mensaje.toLowerCase().contains("bat:l")) traduccion=contexto.getResources().getString(R.string.bateriabajarecargue);
	 if (mensaje.toLowerCase().contains("signal:l")) traduccion=contexto.getResources().getString(R.string.bajasenalgps);
	 if (mensaje.toLowerCase().contains("add master ok")) traduccion=contexto.getResources().getString(R.string.telefonodecontrol);
	 if (mensaje.toLowerCase().startsWith("admin ok")) traduccion=contexto.getResources().getString(R.string.telefonodecontrol);
	 if (mensaje.toLowerCase().startsWith("noadmin ok")) traduccion=contexto.getResources().getString(R.string.telefonodesautorizado);
	 if ((mensaje.toLowerCase().contains("lat:")) && (mensaje.toLowerCase().contains("long:")) 
     && mensaje.toLowerCase().contains("stockade"))  traduccion=contexto.getResources().getString(R.string.localizadorfueradearea);
	 if (mensaje.toLowerCase().contains("t030s003n ok")) traduccion=contexto.getResources().getString(R.string.autorrastreoactivado);
	 if ((mensaje.toLowerCase().contains("lat:")) && (mensaje.toLowerCase().contains("long:")) 
	 && mensaje.toLowerCase().contains("move")) traduccion=contexto.getResources().getString(R.string.ellocalizadorsehamovido);
	 if (mensaje.toLowerCase().contains("fail")) traduccion=contexto.getResources().getString(R.string.errorgeneral);			 	 			 
	 
	 //Traducciones de localizador TK110
	 if (mensaje.toLowerCase().contains("password err")) traduccion=contexto.getResources().getString(R.string.contrasenaincorrecta);
	 if (mensaje.toLowerCase().contains("config ok")) traduccion=contexto.getResources().getString(R.string.configuracioncorrecta);
	 if (mensaje.toLowerCase().contains("710 config ok")) traduccion=contexto.getResources().getString(R.string.numprincipalcorrecto);
	 if (mensaje.toLowerCase().contains("711 config ok")) traduccion=contexto.getResources().getString(R.string.telefonodecontrol);
	 if (mensaje.toLowerCase().contains("770 config ok")) traduccion=contexto.getResources().getString(R.string.contrasenacambiadaok);;
	 if (mensaje.toLowerCase().contains("802 config ok")) traduccion=contexto.getResources().getString(R.string.apninternetcorrecto);;
	 if (mensaje.toLowerCase().contains("720 config ok")) traduccion=contexto.getResources().getString(R.string.ajustesalarmaok);;
	 if (mensaje.toLowerCase().contains("751 config ok")) traduccion=contexto.getResources().getString(R.string.activadolimitararea);;
	 if (mensaje.toLowerCase().contains("750 config ok")) traduccion=contexto.getResources().getString(R.string.activadodetectarmovimiento);;
	 if (mensaje.toLowerCase().contains("760 config ok")) traduccion=contexto.getResources().getString(R.string.desactivadodetectarmovimiento);;
	 if (mensaje.toLowerCase().contains("out")) traduccion=contexto.getResources().getString(R.string.ellocalizadorsehamovido);;

			 
			     
			 
			 
     return traduccion;
     }
	
     public static void aplicarAnimacionZoom( 
 			View container, 
 			Animation.AnimationListener animationListener ) {
 		// Find the center of the container

 		
 		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
 		
 		scaleAnimation.setFillBefore(true);
 		scaleAnimation.setFillEnabled(true);
 		scaleAnimation.setDuration(DURACION_ANIMACION);	
 		scaleAnimation.setInterpolator(new OvershootInterpolator(6f));

 		container.startAnimation(scaleAnimation);
 	}
     

    public String getErrorXml(Context contexto, String xml) throws XmlPullParserException, IOException {
     String codigoError="NO_ERROR";

     /*
     Códigos de error:
     <Message code="OK0000"><![CDATA[Successful]]></Message>
    <Message code="CM0010"><![CDATA[Command invalid]]></Message>
    <Message code="CM0011"><![CDATA[Command not supported]]></Message>
    <Message code="AU0010"><![CDATA[Authorization failed]]></Message>
    <Message code="AC0020"><![CDATA[Account inactive]]></Message>
    <Message code="AC0030"><![CDATA[Account expired]]></Message>
    <Message code="AC0040"><![CDATA[
    Account not authorized for host]]></Message>
    <Message code="AC0042"><![CDATA[
    Account not authorized for command]]></Message>
    <Message code="US0020"><![CDATA[User inactive]]></Message>
    <Message code="DV0010"><![CDATA[DeviceID invalid]]></Message>
    <Message code="GR0010"><![CDATA[DeviceGroup ID invalid]]></Message>
    <Message code="DT0010"><![CDATA[from/to date invalid]]></Message>
    <Message code="PL0030"><![CDATA[Specified URL not allowed]]></Message>
    <Message code="RQ0010"><![CDATA[Service Request disabled]]></Message>
    <Message code="RQ0020"><![CDATA[Request XML requires 'POST']]></Message>
    <Message code="RQ0030"><![CDATA[Request XML syntax errors]]></Message>
    <Message code="RQ0031"><![CDATA[SOAP XML syntax error]]></Message>
    <Message code="RQ0040"><![CDATA[Request XML is invalid]]></Message>
    <Message code="RQ0050"><![CDATA[Request not supported]]></Message>
    <Message code="DB0010"><![CDATA[Invalid table name]]></Message>
    <Message code="DB0020"><![CDATA[Invalid DBRecordKey]]></Message>
    <Message code="DB0030"><![CDATA[Invalid DBRecord]]></Message>
     <Message code="DB0040"><![CDATA[Record not found]]></Message>
     <Message code="DB0045"><![CDATA[Record already exists]]></Message>
     <Message code="DB0050"><![CDATA[Record read failed]]></Message>
     <Message code="DB0060"><![CDATA[Record update failed]]></Message>
     <Message code="DB0065"><![CDATA[Record insert failed]]></Message>
     <Message code="DB0070"><![CDATA[Record delete failed]]></Message>
     <Message code="PR0010"><![CDATA[Invalid property key]]></Message>
     <Message code="RP0010"><![CDATA[Report not found]]></Message>
     <Message code="RP0030"><![CDATA[Report missing Device/Group]]></Message>
     <Message code="RP0040"><![CDATA[Unable to create report]]></Message>
     <Message code="RP0999"><![CDATA[Unexpected Reporting Error]]></Message>
     <Message code="MP0010"><![CDATA[MapProvider not found]]></Message>

     * */

        String TAGINICIO = null,TAGFIN=null, TEXTO=null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader (xml));
        int eventType = xpp.getEventType();


        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.END_DOCUMENT) {
                System.out.println("End document");
            } else if(eventType == XmlPullParser.START_TAG) {
                TAGINICIO=xpp.getName();

                if (xpp.getName().equals("Message")) codigoError=xpp.getAttributeValue(null,"code");


            } else if(eventType == XmlPullParser.END_TAG) {
                TAGFIN=xpp.getName();








            } else if(eventType == XmlPullParser.TEXT) {
                System.out.println("Text "+xpp.getText());
                if (TAGINICIO.equalsIgnoreCase("P"))
                {
                    TEXTO=xpp.getText();
                }

            }
            eventType = xpp.next();

        }





     return codigoError;
    }

     public List<MarkerOptions> getposxml(Context contexto, String xml) throws XmlPullParserException, IOException{
      
    	 String test = "";
    	 //Sucess: true. //Error: false
    	 boolean result_servicio=false;
    	 boolean respuesta_Dataset=false;

    	 String TAGINICIO = null,TAGFIN=null, TEXTO=null;
    	 
    	 List<MarkerOptions> lista=new ArrayList<MarkerOptions>();
    	
    	 
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader (xml));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
             if(eventType == XmlPullParser.START_DOCUMENT) {
                 System.out.println("Start document");
             } else if(eventType == XmlPullParser.END_DOCUMENT) {
                 System.out.println("End document");
             } else if(eventType == XmlPullParser.START_TAG) {
                 TAGINICIO=xpp.getName();
            	
	                 String resultado=xpp.getAttributeValue("GTSResponse", "result");
	                 if (resultado!=null){ 
	                	 Log.i("Utiles.java","Resultado Servicio Web: resultado");
	                     if (resultado.equalsIgnoreCase("sucess") ){
	                      result_servicio=true;
	                  }
	                } else result_servicio=false;
	                 
	                 
                           
                  
                	
             } else if(eventType == XmlPullParser.END_TAG) {
            	 TAGFIN=xpp.getName();
            	 
            	 
            	 
            	 if (TAGFIN.equalsIgnoreCase("P"))
            	 {
            		 //test+="elemento:" + TEXTO + "\n";
                 	
            		 String[] array=TEXTO.split("\\|");
            		 
            	     String strlatitud,strlongitud,iddevice, fecha, hora,direccion;
            	                	     
            	     strlatitud=array[8];
            	     strlongitud=array[9];
            	     iddevice=array[0];
            	     fecha=array[3];
            	     hora=array[4];
            	     direccion=array[20];
            	     byte[] direccionbytes = direccion.getBytes("UTF8");
            	     direccion = new String(direccionbytes, "UTF8");
            	     
             
            	     
            	     
            	     test="lat: " + strlatitud + " lon: " + strlongitud;
            	     
            		 double latitud, longitud;
            		 
            	 	 latitud=Double.valueOf(strlatitud);
            	 	 longitud=Double.valueOf(strlongitud);
            	 	 LatLng pos=new LatLng(latitud,longitud);
            		 
               	  
            	  BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.drawable.ic_localizador);
            	
            		opcionesmarcador = new MarkerOptions();

            		
              		opcionesmarcador.position(pos);

              		
              	    opcionesmarcador.title(iddevice); 

              	    opcionesmarcador.snippet(fecha + " " +hora );
              	    opcionesmarcador.icon(icono);
              	  
              	  //Si existía algun markeroptions con mismo titulo lo borra, 
              	  //Esto es porque el servicio web devuelve 2 posiciones por dispositivo
              	   // y solo queremos uuna 
              	  //posición. La última es la mas reciente. Por tanto borro la primera si esta 
              	  //añadida en la lista con el siguiente for.
              	  
              	    for (int i=0; i<lista.size(); i++){
            	    	
            	    	if (lista.get(i).getTitle().equalsIgnoreCase(iddevice)){
            	    		lista.remove(i);
            	    	}
            	    }
                   
            	    lista.add(opcionesmarcador);
            	    
            	    
            	    
            	 }        		 
            		 
                         
            	 
            	 
            	 
             } else if(eventType == XmlPullParser.TEXT) {
                 System.out.println("Text "+xpp.getText());
                 if (TAGINICIO.equalsIgnoreCase("P"))
                 {
                	 TEXTO=xpp.getText();
                 }

             }
             eventType = xpp.next();
            
        }
    	
        return lista;
    
     }
     

     public List<MarkerOptions> getposxmlruta(Context contexto, String xml) throws XmlPullParserException, IOException{
         float heading=0;
         String test = "";
    	 //Sucess: true. //Error: false
    	 boolean result_servicio=false;
    	 boolean respuesta_Dataset=false;
    	 
    	 Location locationanterior=null;

    	 String TAGINICIO = null,TAGFIN=null, TEXTO=null;
    	 
    	 List<MarkerOptions> lista=new ArrayList<MarkerOptions>();
    	
    	 
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader (xml));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
             if(eventType == XmlPullParser.START_DOCUMENT) {
                 System.out.println("Start document");
             } else if(eventType == XmlPullParser.END_DOCUMENT) {
                 System.out.println("End document");
             } else if(eventType == XmlPullParser.START_TAG) {
                 TAGINICIO=xpp.getName();
            	
	                 String resultado=xpp.getAttributeValue("GTSResponse", "result");
	                 if (resultado!=null){ 
	                	 Log.i("getposxmlruta:","ServicioWeb: " + resultado);
	                     if (resultado.equalsIgnoreCase("sucess") ){
	                      result_servicio=true;
	                  }
	                } else result_servicio=false;
	                 
	                 
                           
                  
                	
             } else if(eventType == XmlPullParser.END_TAG) {
            	 TAGFIN=xpp.getName();
            	 
            	 
            	 
            	 if (TAGFIN.equalsIgnoreCase("P"))
            	 {
            		 //test+="elemento:" + TEXTO + "\n";
                 	
            		 String[] array=TEXTO.split("\\|");
            		 
            	     String strlatitud,strlongitud,iddevice, fecha, hora,direccion,estado,velocidadstr;
            	                	     
            	     strlatitud=array[8];
            	     strlongitud=array[9];
            	     iddevice=array[0];
            	     fecha=array[3];
            	     hora=array[4];
            	     direccion=array[20];
            	     estado=array[6]; //En movimiento o no
            	     velocidadstr=array[14];
            	     
            	     double latitud=Double.valueOf(strlatitud);
            	 	 double longitud=Double.valueOf(strlongitud);
            	 	            	     
            	 	Location location = new Location("");
         	        location.setLatitude(latitud);
         	        location.setLongitude(longitud);
         	        
         	        
         	        if (locationanterior!=null) heading = locationanterior.bearingTo(location);
            	     
            	     Float velocidad=Float.valueOf(velocidadstr);
            	     
            	     byte[] direccionbytes = direccion.getBytes("UTF8");
            	     direccion = new String(direccionbytes, "UTF8");
            	                	     
            	     
            	     test="lat: " + strlatitud + " lon: " + strlongitud;
            	                		 
            	 	 
            	 	 BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.drawable.ic_posruta);
              	 	
            	 	if (velocidad<8)icono = BitmapDescriptorFactory.fromResource(R.drawable.rojo);
            	 	if (velocidad>=8 && velocidad<32)icono = BitmapDescriptorFactory.fromResource(R.drawable.amarillo);
            	 	if (velocidad>=32)icono = BitmapDescriptorFactory.fromResource(R.drawable.verde);
              	 	 
            	 	/* 
            	 	 String dir=getDireccionString(normalizarGrados(heading));
            	 	 
            	 	if (dir.contentEquals("N")) icono = BitmapDescriptorFactory.fromResource(R.drawable.n);
            	    if (dir.contentEquals("S")) icono = BitmapDescriptorFactory.fromResource(R.drawable.s);
            	 	if (dir.contentEquals("E")) icono = BitmapDescriptorFactory.fromResource(R.drawable.e);
            	 	if (dir.contentEquals("O")) icono = BitmapDescriptorFactory.fromResource(R.drawable.o);
            	 	if (dir.contentEquals("NE")) icono = BitmapDescriptorFactory.fromResource(R.drawable.ne);
            	 	if (dir.contentEquals("NO")) icono = BitmapDescriptorFactory.fromResource(R.drawable.no);
            	 	if (dir.contentEquals("SE")) icono = BitmapDescriptorFactory.fromResource(R.drawable.se);
            	 	if (dir.contentEquals("SO")) icono = BitmapDescriptorFactory.fromResource(R.drawable.so);
              	 	*/             	 	 
            	 	locationanterior = new Location("");
         	        locationanterior.setLatitude(latitud);
         	        locationanterior.setLongitude(longitud);
         	     
            	 	
            	 	LatLng pos=new LatLng(latitud,longitud);
            	     
            	 	
            	
            		opcionesmarcador = new MarkerOptions();

            		
              		opcionesmarcador.position(pos);

              		
              	    opcionesmarcador.title(iddevice); 

              	    opcionesmarcador.snippet(fecha + " " + hora + " " + velocidadstr + " Km/h" );
              	    
              	
              	                 	    
              	    opcionesmarcador.icon(icono);
              	
              	  
         	        
              	    
              	     lista.add(opcionesmarcador);
            	    
            	    
            	    
            	 }        		 
            		 
                         
            	 
            	 
            	 
             } else if(eventType == XmlPullParser.TEXT) {
                 System.out.println("Text "+xpp.getText());
                 if (TAGINICIO.equalsIgnoreCase("P"))
                 {
                	 TEXTO=xpp.getText();
                 }
             }
             eventType = xpp.next();
            
        }
    	
        return lista;
    
     }

     /**
      *** Returns a String representation of the spedified compass heading value
      *** @param heading  The compass heading value to convert to a String representation
      *** @return A String representation of the compass heading (ie. "N", "NE", "E", "SE", "S", "SW", "W", "NW")
      **/
      public static String getDireccionString(float heading)
      {
          if (!Float.isNaN(heading) && (heading >= 0.0)) {
              int h = (int)Math.round(heading / 45.0) % 8;
              //return DIRECTION[(h > 7)? 0 : h];
              switch (h) {
                  case 0: return "N";
                  case 1: return "NE";
                  case 2: return "E";
                  case 3: return "SE";
                  case 4: return "S";
                  case 5: return "SW";
                  case 6: return "W";
                  case 7: return "NW";
              }
              return "N"; // default
          } else {
              return "";
          }
      }

     
public String pruebaxml(Context contexto, String xml) throws XmlPullParserException, IOException {
    	String retorno="";
    	 
    
    	
    	 List<String[]> lista=new ArrayList<String[]>();
    	  
    	 BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.drawable.ic_dondeestoy);
     	
 		MarkerOptions opcionesmarcador = new MarkerOptions();

   		//opcionesmarcador.position(pos);

   		
   	    opcionesmarcador.title(contexto.getResources().getString(R.string.estoyaqui)); 

   	    opcionesmarcador.icon(icono);

    	
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader (xml));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
             if(eventType == XmlPullParser.START_DOCUMENT) {
                 System.out.println("Start document");
                 //retorno+="Inciando documento\n";
             } else if(eventType == XmlPullParser.END_DOCUMENT) {
                 System.out.println("End document");
                 //retorno+="Fin de documento\n";
             } else if(eventType == XmlPullParser.START_TAG) {
                 System.out.println("Iniciando tag "+xpp.getName());
                 //retorno+="Iniciando tag " + xpp.getName()+ "\n";               
                 
                 
                
                    
                	
             } else if(eventType == XmlPullParser.END_TAG) {
                 System.out.println("End tag "+xpp.getName());
                 //retorno+="Fin tag " + xpp.getName() + "\n";               
                 
             } else if(eventType == XmlPullParser.TEXT) {
                 System.out.println("Text "+xpp.getText());
                 //retorno+="Texto= " + xpp.getText()+ "\n";   
                 
                String valorgts = xpp.getText();
                
                
                
                String[] valores=valorgts.split("\\|");
                
                for (int i=1;i<valores.length;i++) retorno+=valores[i]+"\n";               
                    
                
                 
             }
             eventType = xpp.next();
            
        }
            
            
           
    	
        return retorno;
    
     }     

public static void getPosicionGrupo(Context contexto){

	String body= "<GTSRequest command=\"mapdata\">\r\n" + 
	   		"    <Authorization account=\"" + Main.preferencias.getString(Main.KEY_USERINTERNET, "")
	   		+ "\" user=\"admin\" password=\"" + Main.preferencias.getString(Main.KEY_PASSINTERNET, "") + "\"/>\r\n" + 
	   		"    <MapData>\r\n" + 
	   		"        <DeviceGroup>all</DeviceGroup>\r\n" + 
	   		"    </MapData>\r\n" + 
	   		"</GTSRequest>\r\n" + 
	   		"\r\n" + 
	   		"";
	
new HttpPost(contexto).execute(body);	
	
}

public static String getGMT()
{
    String retorno="GMT";
	Calendar calendar = Calendar.getInstance();
    long now = calendar.getTimeInMillis();
    int utc = (int)(now / 1000);
    if (utc!=0) retorno="GMT+" + Integer.toString(utc);
    return retorno;

}

private static float normalizarGrados(float value){
    if(value >= 0.0f && value <= 180.0f){
        return value;
    }else{
        return 180 + (180 + value);
    }
 }   
}

