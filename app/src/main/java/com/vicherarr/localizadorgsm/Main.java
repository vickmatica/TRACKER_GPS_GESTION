/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * 
 */
package com.vicherarr.localizadorgsm;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.jsoup.nodes.Document;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;




public class Main extends FragmentActivity {


		
	public static int INICIOLATTK110=35;
	public static int FINLATTK110=50;
	public static int INICIOLONGTK110=51;
	public static int FINLONGTK110=65;
	
	
	
	//Utilizado para dibujar rectangulo limitar area
	public double latsup, latinf, longsup, longinf;
	
	
	//Para almacenar el historico
	Basedatos basedatos;
	
	//Para dibujar centro del circulo
	public static LatLng coordenadascentro;
	
	//Habilita menu configuración avanzada sin necesidad de introducir numero *4222*
	boolean confavanzada=false;
		
	//Objetos necesarios para manejar gps por internet o integrado
	public static LocationManager locationmanagerGPSintegrado;
	public static LocationListener locationlistenerGPSintegrado;
	public static LocationManager locationmanagerGPSinternet;
	public static LocationListener locationlistenerGPSinternet;
	
	
	//Manejador de proceso al recibir mensaje
	public static  Handler handle;
	
	//Manejador para detectar cuando el rectangulo de "Limitar Area" ha sido dibujada
	public static Handler handle_limitar_area;
	
	//Manejador para detectar cuando el rectangulo de "Limitar Area" ha sido dibujada
	public static Handler handle_dibujar_miubicación;
		
	
	public static int numeroavisosgps=1;
	
	    //Constante para crear Intent para lanzar ventana de configuración
		
		public final static String INTENT_CONFIGURACION_GASTO="intent.action.CONFIGURACIONGASTO";
		
		private final static String INTENT_CONFIGURACIONAVANZADA="intent.action.CONFIGURACIONAVANZADA";
		
		public final static String PREFERENCIAS="Mispreferencias";
			
		private Double latitud;
		private Double longitud;
		private long precision;
		
		
		//MapView mapa;
		GoogleMap mapa;
		
		//Para controlar el mapa (zoom, centrar posición, etc)
		CameraUpdate mapcontroller;
		
		
		
		//ZoomControls zoomcontrol;
		
		Button desplegarmenu;
		
		private Utiles utiles;
		protected Context contexto;
	
		private float scaleDensity;
		

		protected String valortel;

		private ImageView cambiarvista;

		private int id_mensaje;



		protected AlertDialog alerteventorecibido;
		public static float pixelfoto;
	
		//En preferencias Guardamos todo lo relacionado con la configuración: 
		//Número de telefono del localizador, comandos a enviar, etc.,
		public static SharedPreferences preferencias;

		
		//Constantes clave para acceder a cada preferencia
		public static final String KEY_NUMTELLOCALIZADOR="numero_telefono_localizador";
		public static final String KEY_MINUMTEL="mi_numero_telefono";
		public static final String KEY_MODELO="modelo";
		
		public static final String KEY_COMANDOLOCALIZAR = "comando_sms_localizar";
		public static final String KEY_CLAVELOCALIZADOR = "clave_localizador";
		
		//En desplegable de Activity Configuración guarda el id del localizador seleccionado 
		//public static final String KEY_LOCALIZADORSELECCIONADO = "LOCALIZADORSELECCIONADO";
		public static final String KEY_NOMBRELOCALIZADOR = "NOMBRELOCALIZADOR";
		
		
		public static final String KEY_SMS_INICIALIZAR = "SMS_INICIALIZAR";
		public static final String KEY_SMS_AUTORIZAR = "SMS_AUTORIZAR";
		public static final String KEY_SMS_DESAUTORIZAR = "SMS_DESAUTORIZAR";
		public static final String KEY_SMS_DETECCIONMOVIMIENTO_ON = "SMS_DETECCIONMOVIMINENTO_ON";
		public static final String KEY_SMS_DETECCIONMOVIMIENTO_OFF = "SMS_ACTIVARDETECCIONMOVIMINENTO_OFF";
        public static final String KEY_SMS_AUTORASTREO_ON = "SMS_AUTORASTREO_ON";		
        public static final String KEY_SMS_AUTORASTREO_OFF = "SMS_AUTORASTREO_OFF";		
        public static final String KEY_SMS_EXCESOVELOCIDAD_ON = "SMS_EXCESOVELOCIDAD_ON";
        public static final String KEY_SMS_EXCESOVELOCIDAD_OFF = "SMS_EXCESOVELOCIDAD_OFF";
        public static final String KEY_SMS_MODOVOZ_ON = "SMS_MODOVOZ_ON";
        public static final String KEY_SMS_MODOVOZ_OFF = "SMS_MODOVOZ_OFF";
        public static final String KEY_SMS_LIMITARAREA_OFF = "SMS_LIMITARAREA_OFF";
        public static final String KEY_SMS_LIMITARAREA_ON = "SMS_LIMITARAREA_ON";
        public static final String KEY_ORDENLATITUD = "ORDENLATITUD";
        public static final String KEY_ORDENLONGITUD = "ORDENLONGITUD";
        public static final String KEY_SEPARADORCOORDENADAS = "SEPARADORCOORDENADAS";
        
        
        
        public static final String KEY_PRIMERINICIO="PRIMERINICIO";
		public static final String KEY_TIEMPOAUTORASTREO = "TIEMPOAUTORASTREO";
		public static final String KEY_VECESAUTORASTREO = "VECESAUTORASTREO";
		
		public static final String KEY_PRECIOXSMSLOCALIZADOR = "PRECIOXSMSLOCALIZADOR";
		public static final String KEY_SALDOMINIMO_LOCALIZADOR = "SALDOMINIMOLOCALIZADOR";
		public static final String KEY_SALDOLOCALIZADOR = "SALDOLOCALIZADOR";
		
		
		public static final int ORDENLATITUD = 1;
		public static final int ORDENLONGITUD = 2;
		public static final String CLAVELOCALIZADOR = "123456";
		
		//VALOR POR DEFECTO DEL ZOOM
		private static final int ZOOM = 19;
		protected static final String CLAVEMENUAVANZADO = "*4222*";

 
        //SEPARADOR DE COORDENADAS CONFIGURADO POR DEFECTO
		private static final String VALORSEPARADORCOORDENADAS = " ";





		private static final CharSequence SEÑALGPSBAJA = "signal:L";

		private static final long PERIODO_DIBUJARMIUBICACION = 5000;

		private static final long PERIODO_DIBUJARWEBSERVICE = 300000;
		
		
		public static final String KEY_MESES_CADUCIDAD = "MESES CADUCIDAD";
		
	    //Utilizado para vista mover vista 3d	
		protected static final int ANGULOINCREMENTO = 10;
		public static final String KEY_IDIOMA = "IDIOMA";
		public static final String KEY_SUBMODELO = "SUBMODELO";
		static final int MODELODESCONOCIDO = 0;
		static final int MODELOTK110 = 1;
		static final int MODELOTK102 = 2;
		protected static final String KEY_USERINTERNET = "USERINTERNET";
		protected static final String KEY_PASSINTERNET = "PASSINTERNET";
		

		


				
		AlertDialog alertdialogrecibidosmsloc;







        //pixels por defecto para imagenes
		private float dips=58;

		private MyTimerTask myTask;
		
		private MyTimerTaskWebService myTaskWebService;

		private Timer mitimer;

		private String traduccion;

		

		private String modelolocalizador;

		protected int contadormensajes;

		private ImageView locseleccionado;

		private ImageView anterior;

		private ImageView posterior;

		private ImageView vista3d;
		
		//private ImageView down;
		
		private TextView textolocalizador;
		
		protected Cursor cursor;
		protected CameraPosition backupposicioncamara;
		protected float backupbearing;
		protected float backuptilt;
		protected float backupzoom;
		private ImageView agregar;
		private String submodelolocalizador;
		@SuppressWarnings("unused")
		private TextView textodepuracion;
		protected Document doc;
		protected String ippublica;
		protected Socket client;
		protected View contenedor;
		private int REQUEST_CODE_RECOVER_PLAY_SERVICES;
		protected static boolean activado3d=false;
		protected static float ZOOMGRANDE=17;
		protected static float TILTGRANDE=90;
		public static int tilt=0;

		LatLng punto;
		private Timer mitimerwebservice;
		private ImageView updateweb;
		private Spinner spinnerDevices;
		protected boolean seleccionadoamano=false;
		protected ArrayAdapter<String> adapter;
		private ImageView imagenruta;
		


		//private MyLocation mylocation;


		//private LocationResult locationResult;


		



		public static String textoparapruebas;
		public static List<MarkerOptions> listmarkeroptionsinternet=new ArrayList<MarkerOptions>();
		public static List<MarkerOptions> listmarkeroptionsruta=new ArrayList<MarkerOptions>();
		public static List<MarkerOptions> listmarkeroptionssms=new ArrayList<MarkerOptions>();
		public static List<Marker> listmarker=new ArrayList<Marker>();
		public static List<Marker> listmarkerruta=new ArrayList<Marker>();
		
		static Handler handle_dibujar_servicioweb;
		public static Handler handle_dibujar_ruta;
		
	 
		
			
		
		//método para establecer zoom en funcion de la precision
	    protected void zoomautomatico(){
				int zoom=15;	
				if (precision<=30) zoom=18;
			    if (precision>30 && precision<=200) zoom=18;
			    if (precision>200 && precision<=400) zoom=17;
			    if (precision>600 && precision<=800) zoom=16;
				if (precision>800) zoom=14;
				//mapcontroller.setZoom(zoom);
				mapa.animateCamera(CameraUpdateFactory.zoomTo(zoom));
		}
		
	    public void iralocalizador(){
			Basedatos bd=new Basedatos(contexto);
			
			cursor=bd.consultarLocalizadores();
			
			
			
			
			boolean encontrado=false;
			
			
			try
			{
			if (cursor.moveToFirst()){ 
			
			  
			 //Pone el cursor en el localizador seleccionado	
		     do {
			  
				  if (cursor.getString(   cursor.getColumnIndex("nombre")  ).contentEquals(Main.leer(Main.KEY_NOMBRELOCALIZADOR))) 
				  {
				   encontrado=true;
				   break;
				  }
				  
				  
			  }while (cursor.moveToNext() && !encontrado);
			 
			}	
		
			 
				 actualizarlocalizador();	
				
				 
	    } catch (Exception e){  if (cursor.moveToFirst()) actualizarlocalizador(); }
			bd.cerrarbd();
		}    

	    
		
        public void guardarconfigdefecto(){
        
           
        
               
	        if (modelolocalizador.contentEquals("TK-102") || modelolocalizador.contentEquals("TK-103")){	
		        Main.guardar(Main.KEY_SMS_INICIALIZAR, "begin#clave");
		        Main.guardar(Main.KEY_SMS_AUTORIZAR, "admin#clave");
		        Main.guardar(Main.KEY_SMS_DESAUTORIZAR, "noadmin#clave");
		        Main.guardar(Main.KEY_SMS_DETECCIONMOVIMIENTO_ON, "move#clave");
		        Main.guardar(Main.KEY_SMS_DETECCIONMOVIMIENTO_OFF, "nomove#clave");
		        Main.guardar(Main.KEY_TIEMPOAUTORASTREO, 30);
		        Main.guardar(Main.KEY_VECESAUTORASTREO, 3);
		        Main.guardar(Main.KEY_ORDENLATITUD, ORDENLATITUD);
		        Main.guardar(Main.KEY_ORDENLONGITUD, ORDENLONGITUD);
		        
		        Main.guardar(Main.KEY_SMS_AUTORASTREO_ON, "t#tiempos#vecesn#clave");
		        Main.guardar(Main.KEY_SMS_AUTORASTREO_OFF, "notn#clave");
		        Main.guardar(Main.KEY_SMS_MODOVOZ_ON, "monitor#clave");
		        Main.guardar(Main.KEY_SMS_MODOVOZ_OFF, "tracker#clave");
		        
		        
		        Main.guardar(Main.KEY_SMS_EXCESOVELOCIDAD_ON, "speed#clave ");
		        Main.guardar(Main.KEY_SMS_EXCESOVELOCIDAD_OFF, "nospeed#clave");
		        
		        Main.guardar(Main.KEY_SEPARADORCOORDENADAS, VALORSEPARADORCOORDENADAS);
		        
		        
	        }
	        
	        if (modelolocalizador.contentEquals("TK-110")){	
	            
	            //Recepción en formato de sms
	            Main.guardar(Main.KEY_SMS_INICIALIZAR, "#710#");
	            
	            //Send command: #711#call1#call2#call3#User password##
	            Main.guardar(Main.KEY_SMS_AUTORIZAR, "#711#");
	            
	            Main.guardar(Main.KEY_TIEMPOAUTORASTREO, 30);
	            Main.guardar(Main.KEY_VECESAUTORASTREO, 3);
	            
	            //Main.guardar(Main.KEY_ORDENLATITUD, ORDENLATITUD);
	            //Main.guardar(Main.KEY_ORDENLONGITUD, ORDENLONGITUD);
	            
	            Main.guardar(Main.KEY_ORDENLATITUD, ORDENLATITUD);
	            Main.guardar(Main.KEY_ORDENLONGITUD, ORDENLONGITUD);
	            
	            
	            Main.guardar(Main.KEY_SMS_AUTORASTREO_ON, "730##tiempo##veces##clave");
	            Main.guardar(Main.KEY_SMS_AUTORASTREO_OFF, "730##120##1##clave");
	                    
	            
	            Main.guardar(Main.KEY_SEPARADORCOORDENADAS, VALORSEPARADORCOORDENADAS);
	            
	        }
	    
	        
        
        
        }
        //Inserta registros de demostración en la base de datos
        @SuppressWarnings("unused")
		private void insertardatosdemostracion(){
        	basedatos=new Basedatos(contexto);
        	String textosms;
        	//Registra el sms en el historial y borra los registros antiguos	
		    textosms="Latitud: 38.283865  Longitud: -0.717761";
		    basedatos.insertar(textosms,"111111111");
		    
		    //String ejemplotk110="356823033383113 2013/02/28 04:57:46 Lat:+38.284008 Lon:-000.717763 Speed:1.30KM/H http://maps.google.com/maps?q=+38.35632,-000.47422";
	        String ejemplotk102="lat: 38.356581 long: -000.475586 speed: 000.0 09/06/13 00:14   bat:F signal:L  imei:358948017189224";
	        
		    
		    
		    textosms=ejemplotk102;
		    basedatos.insertar(textosms,"444444444");
		    
		    
		    
		    textosms=ejemplotk102;;
		    basedatos.insertar(textosms,"444444444");
		    
		    
		    textosms=ejemplotk102;;
		    basedatos.insertar(textosms,"444444444");
		    
		    
		    
		    textosms="356823033383113 2013/02/28 04:57:46 Lat:+38.284008 Lon:-000.717763 Speed:1.30KM/H http://maps.google.com/maps?q=+38.35632,-000.47422";
            basedatos.insertar(textosms,"555555555");
            
            
		     textosms="356823033383113 2013/02/28 04:57:46 Lat:+38.35632 Lon:-000.47422 Speed:1.30KM/H http://maps.google.com/maps?q=+38.35632,-000.47422";
	         basedatos.insertar(textosms,"666666666");
	              
	    	    
			  textosms="move ok!";
		      basedatos.insertar(textosms,"333333333");
		         
			 
	         
			     textosms="speed ok!";
			     basedatos.insertar(textosms,"333333333");
			     
			 
				    
				     textosms="stockade! Latitud: 20.682688   Longitud: -88.568637";
				     basedatos.insertar(textosms,"333333333");
				     
				    
					     textosms="begin123456";
					     basedatos.insertarSalida(textosms, "777777777");					     
				     
				     
					    
					     textosms="move123456";
					     basedatos.insertarSalida(textosms, "333333333");					     
				     
			     basedatos.borrarultimosregistroshistorico(SMSReceiver.NUMEROREGISTROSHISTORICO);
            
		    
        }
        
        //Metodo para inicializar variable pixelfoto necesaria para escalar el 
        //tamaño de los iconos en función del tamaño de la pantalla del dispositivo
        public void inicializarIconos(){
        	
        	float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        	
        	
            // buscando los pixeles a partir de dips con la densidad
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            switch(metrics.densityDpi)
            {
            case DisplayMetrics.DENSITY_HIGH: //HDPI
                //"Alta Densidad"
                scaleDensity = scale * 240;
                pixelfoto = dips * (scaleDensity / 240);
                break;
            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                //"mediana Densidad"
                scaleDensity = scale * 160;
                pixelfoto = dips * (scaleDensity / 160);
                break;

            case DisplayMetrics.DENSITY_LOW:  //LDPI
                //"baja Densidad"
                scaleDensity = scale * 120;
                pixelfoto = dips * (scaleDensity / 120);
                break;
            }
        	
        	}
         
        	    
			
				
        
        
        public MarkerOptions dibujardesms(String hora, String mensajesms1, String numerosms){
        String coordenadasvacias="lat: long: ";	
        MarkerOptions markeroptionsretorno=null;
        
        Basedatos bd=new Basedatos(contexto);
        String modelo=bd.consultarModelo(numerosms);
        bd.cerrarbd();
    	
        String mensajesms=mensajesms1;
        
         
        
        
        
        
        Toast.makeText(contexto, contexto.getResources().getString(R.string.textodelsms)+ "\n" + mensajesms, Toast.LENGTH_LONG).show();
		
                
                if (!mensajesms.contains(coordenadasvacias)){
        	        try {
		 			   
        	         int modeloasumido=detectarmodelo(mensajesms);
        	        
        	        	//Si no se detecta modelo, el modelo asumido para mensaje es el configurado
 			 		   if (modeloasumido==MODELODESCONOCIDO) 
 			 		   {  	if (modelo.contentEquals("TK-102")) modeloasumido=MODELOTK102;
 			 		   		if (modelo.contentEquals("TK-110")) modeloasumido=MODELOTK110; 
 			 		   }
 			 		   
 			 		   if (modeloasumido!=MODELOTK110){
 			 			   latitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLATITUD),contexto);
 			        	   longitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLONGITUD),contexto);
 			        	   
 		 				   }
 		 				   else
 		 				   {
 	 			                if (validartk110(mensajesms)){ 		  
 		
	
		 			                latitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLATTK110,FINLATTK110), 1,contexto);
		 				        	longitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLONGTK110,FINLONGTK110), 1,contexto);
	 			            } else throw new ExcepcionFormatoMensaje("Mensaje sms no pertenece a tk110"); 
 		 				   }
 			 		  
		 		    
		 		    
		 		    
		 		    
		 		    //Toast.makeText(contexto, "\nlatitud: "+latitud.toString()+"\nlongitud: "+longitud.toString(), Toast.LENGTH_LONG).show();
		 		   
		 		  
		 		    Drawable iconolocalizador=getResources().getDrawable(R.drawable.ic_localizador);
		 		    //Descomentar para redimensionar segun parametro dips
		 		    //iconolocalizador=Utiles.redimensionardrawable(iconolocalizador, new Float(Main.pixelfoto).intValue(),new Float(Main.pixelfoto).intValue());
		 		    
		 		    markeroptionsretorno=utiles.dibujarlocalizador( new LatLng(latitud, longitud), mapa,  numerosms , hora, mensajesms, iconolocalizador, contexto );
		 	        
		 		
		 		    
		            //mapcontroller.setCenter(Utiles.calcularGeoPoint(latitud, longitud));
		 	        //mapcontroller.setZoom(ZOOM);    
		 		    
		 		
		 	     
		 	       mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), ZOOM));
		 	        
		 	       //Toast.makeText(contexto, "Posición del localizador", Toast.LENGTH_SHORT).show();
				    
		 	       
		 	       if (mensajesms.contains(SEÑALGPSBAJA)) 	Toast.makeText(contexto, contexto.getResources().getString(R.string.bajasenalgps), Toast.LENGTH_SHORT).show();
		 	        
		 	       } catch (ExcepcionFormatoMensaje e) {
		 			// TODO Auto-generated catch block
		 	    	  
		 	    	  //Toast.makeText(this, this.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_SHORT).show();
		 		 	   e.printStackTrace();
		 		   } catch (java.lang.NullPointerException e){
		 			  //Toast.makeText(this, this.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_SHORT).show();
		 			   e.printStackTrace();  
		 		   } catch (Exception e){
		 			  //Toast.makeText(this, this.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_SHORT).show();
		 			   e.printStackTrace();
		 		   }
        	      
		     
        	} //else Toast.makeText(contexto, contexto.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_SHORT);
           return markeroptionsretorno;
        }
       
        
        protected void Salir() {
        	super.finish();
        	
        }
        
        
        //Al pulsar botón "ATRAS" 
        @Override
        public void onBackPressed(){
					        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
								
					        	
					        	
								builder.setTitle(contexto.getResources().getString(R.string.salir));
								builder.setMessage(contexto.getResources().getString(R.string.deseacerrar));
							    android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
					
									public void onClick(DialogInterface dialog, int which) {
										// TODO Apéndice de método generado automáticamente
										//Configuracion.saltar=true;
										Salir();		
									}
									
									
								};
							    
								
								
								
								builder.setPositiveButton(contexto.getResources().getString(R.string.si), listener);
							   
							    
					android.content.DialogInterface.OnClickListener listener2 = new android.content.DialogInterface.OnClickListener() {
					
						public void onClick(DialogInterface dialog, int which) {
							// TODO Apéndice de método generado automáticamente
							 
						}
									
									
								};
							    
								builder.setNegativeButton("No", listener2);
							   
								
							    			    
							    builder.show();

}
        
        
        
        @Override
        public void onPause(){
         super.onPause();
         
         locationmanagerGPSintegrado.removeUpdates(Main.locationlistenerGPSintegrado);
         locationmanagerGPSinternet.removeUpdates(Main.locationlistenerGPSinternet);
         
         //mylocationoverlay.disableCompass();
         mitimer.cancel();
         //this.mitimerwebservice.cancel();
         //mylocation.StopLoading();
	         try 
	         {
	        	 basedatos.cerrarbd();
	         } 
	         catch (java.lang.NullPointerException e){
	             	 
	         }
        }
        
                

        
		public static boolean validartk110(String mensaje) {
			// TODO Apéndice de método generado automáticamente
			boolean retorno=false;
			
			if (mensaje.contains("Lat:") && mensaje.contains("Lon:") && mensaje.contains("Speed:") && mensaje.contains("http://maps.google.com")) retorno=true;
			
				 
			return retorno;
		}

        
        @Override
        public void onResume(){
        	super.onResume();
        	
        	
        	//Habilitar para mostrar ip publica del movil
        	//this.textodepuracion.setText(new IpPublica().doInBackground());
        	
        	
        	
			preferencias = getSharedPreferences(PREFERENCIAS,Context.MODE_PRIVATE);
	        
	        
	        String idioma=preferencias.getString(KEY_IDIOMA, "espanol");
	        
	        
	        
	        	if (idioma.contentEquals("espanol"))
	        	{
	        		Resources res = getResources();
			        Configuration newConfig = new Configuration(res.getConfiguration());
			        // newConfig.locale = Locale.TRADITIONAL_CHINESE;
			        
			        Locale spanish = new Locale("es", "ES");;
			        
			        newConfig.locale = spanish;
			        
			        res.updateConfiguration(newConfig, null);
			        
			        
	        	}
	        	
	        	if (idioma.contentEquals("ingles"))
	        	{
	        		Resources res = getResources();
			        Configuration newConfig = new Configuration(res.getConfiguration());
			        // newConfig.locale = Locale.TRADITIONAL_CHINESE;
			        newConfig.locale = Locale.ENGLISH;
			        res.updateConfiguration(newConfig, null);   	
	        	}
       
            
	        	desplegarmenu.setText(contexto.getResources().getString(R.string.Menu));
            
        	modelolocalizador=Main.leer(Main.KEY_MODELO);    	
        	
        	
        	
            iralocalizador();
        	
            actualizarlocalizador();
          
            if (modelolocalizador!=null) guardarconfigdefecto();
            
            
            try 
            {
             basedatos.cerrarbd();	
            }
        	catch (Exception e){}
            
        	//Conecta y abre la base de datos de historial
        	//En evento onpause la cierra
        	basedatos=new Basedatos(contexto);
        	
        	
        	Cursor c1=basedatos.consultarLocalizadores();	
        	
        	if (c1!=null) 
        		//Si no hay ningún localizador
        		if (c1.getCount()<1) 
        		{
        	     this.anterior.setVisibility(ImageView.INVISIBLE);
        	     this.posterior.setVisibility(ImageView.INVISIBLE);
        	     this.locseleccionado.setVisibility(ImageView.INVISIBLE);
        	     this.textolocalizador.setText("");
        	     
        	     AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
        			
				 alert.setTitle(contexto.getResources().getString(R.string.ningunlocalizador));
				 alert.setMessage(contexto.getResources().getString(R.string.deseacrearnuevolocalizador));
                 alert.setNegativeButton(contexto.getResources().getString(R.string.no), null);
                 
					alert.setPositiveButton(contexto.getResources().getString(R.string.si), new DialogInterface.OnClickListener(){

						
				
           
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Log.i("dialogo ¿desea crear nuevo localizador?", "pulsado si.");
							Intent intentnuevoloc=new Intent(contexto,NuevoLocalizador.class);
			        	    startActivity(intentnuevoloc);
			        		               
				            	             
						}});
					
					if (preferencias!=null) 
						if (!preferencias.getBoolean(Main.KEY_PRIMERINICIO, true)) alert.show();
                         
						
					guardar(Main.KEY_PRIMERINICIO,false);     
        		}
        		else {
        	     this.anterior.setVisibility(ImageView.VISIBLE);
           	     this.posterior.setVisibility(ImageView.VISIBLE);
           	     this.locseleccionado.setVisibility(ImageView.VISIBLE);
           	     textolocalizador.setText(cursor.getString(cursor.getColumnIndex("nombre")));
        		}
        	 
        	//Recorre los localizadores y consulta los saldos
        	try{
        	if (c1.moveToFirst())
        	do {
        		Utiles.alertasaldo(contexto,c1.getString(c1.getColumnIndex("numero")));
        		Utiles.alertacaducidad(contexto,c1.getString(c1.getColumnIndex("numero")));
        		     		
        	    
        	}while (c1.moveToNext());
        	} catch (Exception e){Log.i("Error al recorrer y consultar todos los saldos", "Error al consultar todos los saldos");}
            /*
        	if (basedatos.contarlocalizadores()<2) 
        	{this.locseleccionado.setVisibility(ImageView.INVISIBLE);
        	
        	}
        	*/
        
        	
            guardarconfigdefecto();
            
        	 //Registra los locationmanager de cada receptor gps 
			locationmanagerGPSintegrado.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistenerGPSintegrado);
			locationmanagerGPSinternet.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistenerGPSinternet);
		    
			//mylocationoverlay.enableCompass();
			
			
			 //Lanza tarea de dibujar mi posicion periodicamente
			 //Lo hago así puesto que no es conveniente dibujar en cada recepción gps por la velocidad de posiciones que se reciben
			 myTask = new MyTimerTask();
		     mitimer = new Timer();
		     mitimer.scheduleAtFixedRate(myTask, 0, PERIODO_DIBUJARMIUBICACION);
			
		     Utiles.getPosicionGrupo(contexto);
		     
		     /*	   
		     myTaskWebService = new MyTimerTaskWebService();
		     mitimerwebservice = new Timer();
		     
		     preferencias = getSharedPreferences(Main.PREFERENCIAS,Context.MODE_PRIVATE);
				
		     
		     if (!preferencias.getString(Main.KEY_USERINTERNET, "").equalsIgnoreCase(""))
		     {
		    	 
		    	 
		    	 mitimerwebservice.scheduleAtFixedRate(
		    		 myTaskWebService, 
		    		 0, 
		    		 PERIODO_DIBUJARWEBSERVICE);
		    }
		    */
		          
		               
                   	
 	         
	 	       
	 	    //Extraemos los parametros enviados desde otras Activities o Notificaciones.
	        Bundle extras = getIntent().getExtras();
	        
	        if(extras !=null)
	        {
	           String mensaje=extras.getString(SMSReceiver.NOMBRE_MENSAJE);
	           String numero=extras.getString(SMSReceiver.NUMERO_MENSAJE);
	           
	           
	           Basedatos bd=new Basedatos(contexto);
	           String modelo=bd.consultarModelo(numero);
	           bd.cerrarbd();
	       	
	           
	         
		   	   
	           
	           //Muestra alerta de saldo
	            Utiles.alertasaldo(contexto, numero);
	           
		               //Recoge el id del PendingIntent para poder cancelar la notificación pulsada
			           id_mensaje=extras.getInt(SMSReceiver.ID_MENSAJE);
			 	       
			           String hora=extras.getString("hora");
			           
			           try {
				 			   if (!mensaje.startsWith("lat: long:"))
				 			   {
				        	   
				 		          int modeloasumido=detectarmodelo(mensaje); 
					 		    
				 				//Si no se detecta modelo, el modelo asumido para mensaje es el configurado
						 		   if (modeloasumido==MODELODESCONOCIDO) 
						 		   {  	if (modelo.contentEquals("TK-102")) modeloasumido=MODELOTK102;
						 		   		if (modelo.contentEquals("TK-110")) modeloasumido=MODELOTK110; 
						 		   }
						 		   
						 		   if (modeloasumido!=MODELOTK110){
						 			   latitud = Utiles.ExtraerDouble(mensaje, Main.leerint(Main.KEY_ORDENLATITUD),contexto);
						        	   longitud = Utiles.ExtraerDouble(mensaje, Main.leerint(Main.KEY_ORDENLONGITUD),contexto);
					 				   }
					 				   else
					 				   {
				 			                if (validartk110(mensaje)){ 		  
				 			           		
				 			                        latitud = Utiles.ExtraerDouble(mensaje.substring(INICIOLATTK110,FINLATTK110), 1,contexto);
						 				        	longitud = Utiles.ExtraerDouble(mensaje.substring(INICIOLONGTK110,FINLONGTK110), 1,contexto);
					 			                } else throw new ExcepcionFormatoMensaje("Mensaje sms no pertenece a tk110"); 
					 				   }
						 		  
				 				   
				 				   
				 				   
				 				   
				 				   utiles.dibujarlocalizador(new LatLng(latitud, longitud), mapa, numero, hora, mensaje, getResources().getDrawable(R.drawable.ic_localizador), contexto);   
					            
					 		    //mapcontroller.setCenter(Utiles.calcularGeoPoint(latitud, longitud));
					 	        //mapcontroller.setZoom(ZOOM);
					 		   mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), ZOOM));
					 		   
				 			   } //else Toast.makeText(contexto, contexto.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_LONG).show();
			 	        
			 	        //Cancela la notificación correspondiente
			 	        SMSReceiver.mNotificationManager.cancel(id_mensaje);
			 	        
			 	        
			 	       
			 	        
			 	  	 //Toast.makeText(contexto, "Posición del localizador", Toast.LENGTH_SHORT).show();
			 	        
			 	       } catch (ExcepcionFormatoMensaje e) {
			 			
			 	    	  AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
			 	    	 
			 	 	    String titulo=contexto.getResources().getString(R.string.smsrecibido);
						
			 	 	  traduccion=Utiles.traducirentrada(mensaje, contexto);
						
									 	 	    
			 	 	    
			 	 	    builder.setTitle(titulo);
						
			 	 	    String nombreloc=basedatos.consultarnombre(basedatos.obtenernumero(numero));
			 	 	    
			 	 	    String submodeloloc=basedatos.consultarsubmodelo(basedatos.obtenernumero(numero));
			 	 	    
			 	 	    
			 	 	    builder.setIcon(geticonosubmodelo(submodeloloc, contexto));
			 	 	    
			 	 	    
			 	 	    builder.setMessage(contexto.getResources().getString(R.string.recibidode) + nombreloc +" "+ numero + "\n" + traduccion + "\n" + contexto.getResources().getString(R.string.imposibleextraercoordenadas) +  R.string.textodelsms + mensaje);
			 	 	    android.content.DialogInterface.OnClickListener listenerbotoncontinuar = null;
						
			 	 	    builder.setPositiveButton(contexto.getResources().getString(R.string.Continuar), listenerbotoncontinuar);
			 	 	    builder.show();
			 	    	   //Toast.makeText(this, this.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_SHORT).show();
			 	    	   e.printStackTrace();
			 		   } catch (java.lang.NullPointerException e){
			 			  //Toast.makeText(this, traduccion + "\n" + this.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_SHORT).show();
			 	    	   e.printStackTrace();  
			 		   }
			              catch (Exception e){
			            	  AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
					 	    	 
					 	 	    String titulo=contexto.getResources().getString(R.string.smsrecibido);
								
					 	 	  traduccion=Utiles.traducirentrada(mensaje, contexto);
								
											 	 	    
					 	 	    
					 	 	    builder.setTitle(titulo);
								
					 	 	    String nombreloc=basedatos.consultarnombre(basedatos.obtenernumero(numero));
					 	 	    
					 	 	    String modeloloc=basedatos.consultarsubmodelo(basedatos.obtenernumero(numero));
					 	 	    
					 	 	    //if (modeloloc.contains("TK-102")) builder.setIcon(R.drawable.ic_modelotk102);   
					 	 	    //if (modeloloc.contains("TK-110")) builder.setIcon(R.drawable.imagen_tk110);
					 	 	    //if (modeloloc.contains("TK-103")) builder.setIcon(R.drawable.imagen_tk103);
					 	 	    				 	 	  
					 	 	    
					 	 	    builder.setIcon(geticonosubmodelo(modeloloc,contexto));
					 	 	    
					 	 	    
					 	 	  
					 	 	    builder.setMessage(contexto.getResources().getString(R.string.recibidode) + nombreloc + " " + numero + "\n" + traduccion + "\n" + this.getResources().getString(R.string.imposibleextraercoordenadas) + this.getResources().getString(R.string.textodelsms)+mensaje);
					 	 	    android.content.DialogInterface.OnClickListener listenerbotoncontinuar = null;
								builder.setPositiveButton(contexto.getResources().getString(R.string.Continuar), listenerbotoncontinuar);
					 	 	    builder.show();
					 	    	   //Toast.makeText(this, this.getResources().getString(R.string.imposibleextraercoordenadas), Toast.LENGTH_SHORT).show();
					 	    	   e.printStackTrace();
					 		   	  
			           }
			             
			           
			        
			 	       	        
			            
		            
			        extras.clear();
			                
	        }
	  
	        @SuppressWarnings("unused")
			String lattexto = "", lontexto="";
	 	       if (latitud==null) lattexto="";
	 	       else lattexto=this.getResources().getString(R.string.latitud) + " " + latitud.toString();
	 	       
	 	       if (longitud==null) lontexto="";
	 	       else lontexto="\n"+ this.getResources().getString(R.string.longitud)+" "+longitud.toString();
	 	       
	 	       //textopruebas.setText(lattexto + lontexto);
	        
	        
        }
     
        public static Drawable geticonosubmodelo(String submodelolocalizador, Context contexto){
        Drawable icono = null;
        	if (submodelolocalizador.contentEquals("TK-101")) icono=contexto.getResources().getDrawable(R.drawable.ic_tk101);
        	if (submodelolocalizador.contentEquals("TK-102")) icono=contexto.getResources().getDrawable(R.drawable.ic_modelotk102);
        	if (submodelolocalizador.contentEquals("TK-102-2")) icono=contexto.getResources().getDrawable(R.drawable.ic_modelotk102);
        	if (submodelolocalizador.contentEquals("TK-103")) icono=contexto.getResources().getDrawable(R.drawable.imagen_tk103);
        	if (submodelolocalizador.contentEquals("TK-103-2")) icono=contexto.getResources().getDrawable(R.drawable.ic_tk103_2);
        	if (submodelolocalizador.contentEquals("TK-110")) icono=contexto.getResources().getDrawable(R.drawable.imagen_tk110);
        	if (submodelolocalizador.contentEquals("TK-206")) icono=contexto.getResources().getDrawable(R.drawable.ic_tk206);
        	if (submodelolocalizador.contentEquals("TK-201")) icono=contexto.getResources().getDrawable(R.drawable.ic_201);
        	if (submodelolocalizador.contentEquals("GPS 102")) icono=contexto.getResources().getDrawable(R.drawable.ic_modelotk102);
        	if (submodelolocalizador.contentEquals("GPS 102-B")) icono=contexto.getResources().getDrawable(R.drawable.ic_modelotk102);
        	if (submodelolocalizador.contentEquals("GPS 103-A")) icono=contexto.getResources().getDrawable(R.drawable.ic_gps103a);
        	if (submodelolocalizador.contentEquals("GPS 103-B")) icono=contexto.getResources().getDrawable(R.drawable.ic_gps103b);
        	if (submodelolocalizador.contentEquals("GPS 104")) icono=contexto.getResources().getDrawable(R.drawable.ic_gps104);
        	if (submodelolocalizador.contentEquals("GPS 106-A")) icono=contexto.getResources().getDrawable(R.drawable.ic_106);
        	if (submodelolocalizador.contentEquals("GPS 106-B")) icono=contexto.getResources().getDrawable(R.drawable.ic_106);
        	
        	if (submodelolocalizador.contentEquals("TK102B")) icono=contexto.getResources().getDrawable(R.drawable.ic_modelotk102);
        	if (submodelolocalizador.contentEquals("TK103")) icono=contexto.getResources().getDrawable(R.drawable.imagen_tk103);
        	if (submodelolocalizador.contentEquals("TK103B")) icono=contexto.getResources().getDrawable(R.drawable.ic_tk103_2);
        	if (submodelolocalizador.contentEquals("TK104")) icono=contexto.getResources().getDrawable(R.drawable.ic_gps104);
        	if (submodelolocalizador.contentEquals("TK106")) icono=contexto.getResources().getDrawable(R.drawable.ic_106);
        	if (submodelolocalizador.contentEquals("TK106-B")) icono=contexto.getResources().getDrawable(R.drawable.ic_106b);
        	if (submodelolocalizador.contentEquals("TK-118")) icono=contexto.getResources().getDrawable(R.drawable.ic_tk118);
            	
        	
        	return icono;
        
        }
        
       
       
	        
        
	    public void establecerimagenlocalizador() {
			// TODO Apéndice de método generado automáticamente
	    	try{
	        	Drawable icono = geticonosubmodelo(submodelolocalizador, contexto);
	        	
	        	
	        	locseleccionado.setImageDrawable(icono);
	        	}catch (Exception e){}
	       
		}

	    public void actualizarlocalizador(){
	    	 
	    	 if (cursor.getCount()>0){
	    	
	    	 String numero=cursor.getString(cursor.getColumnIndex("numero"));
			 String clave=cursor.getString(cursor.getColumnIndex("clave"));
			 String modelo=cursor.getString(cursor.getColumnIndex("modelo"));
			 String submodelo=cursor.getString(cursor.getColumnIndex("submodelo"));
			 String nombre=cursor.getString(cursor.getColumnIndex("nombre"));
			 
			 				 
			 Main.guardar(Main.KEY_NOMBRELOCALIZADOR, nombre);
			 Main.guardar(Main.KEY_NUMTELLOCALIZADOR, numero);
			 Main.guardar(Main.KEY_CLAVELOCALIZADOR, clave);
			 Main.guardar(Main.KEY_MODELO, modelo);
			 Main.guardar(Main.KEY_SUBMODELO, submodelo);
			 
		     
			 modelolocalizador=modelo; 
			 
			 submodelolocalizador=submodelo;
			 
		     establecerimagenlocalizador();
			 
		     guardarconfigdefecto();
		     
		     textolocalizador.setText(nombre);
		     
		     
		     Toast.makeText(contexto, contexto.getResources().getString(R.string.num_telefono) + ": " + numero + contexto.getResources().getString(R.string.localizador) + " " +  nombre, Toast.LENGTH_SHORT).show();
	    	 }
	    	 else this.locseleccionado.setImageDrawable(null);
        }
	    	    
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        
	        
           preferencias = getSharedPreferences(PREFERENCIAS,Context.MODE_PRIVATE);
	        
	        
	        String idioma=preferencias.getString(KEY_IDIOMA, "espanol");
	        
	        
	        
	        	if (idioma.contentEquals("espanol"))
	        	{
	        		Resources res = getResources();
			        Configuration newConfig = new Configuration(res.getConfiguration());
			        // newConfig.locale = Locale.TRADITIONAL_CHINESE;
			        newConfig.locale = Locale.getDefault();
			        res.updateConfiguration(newConfig, null);   	
	        	}
	        	
	        	if (idioma.contentEquals("ingles"))
	        	{
	        		Resources res = getResources();
			        Configuration newConfig = new Configuration(res.getConfiguration());
			        // newConfig.locale = Locale.TRADITIONAL_CHINESE;
			        newConfig.locale = Locale.ENGLISH;
			        res.updateConfiguration(newConfig, null);   	
	        	}
	        
	              
	        setContentView(R.layout.main);
	          			
	        
	        
	        contexto=this;
	        
	        //MapsInitializer.initialize(contexto);
	        
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapa);
            
		
			
			
			mapa = mapFrag.getMap();
			
			
			 if (mapa != null) {
				// The Map is verified. It is now safe to manipulate the map.
				}else {
				int checkGooglePlayServices = GooglePlayServicesUtil
				              .isGooglePlayServicesAvailable(this);
				       if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
				              // google play services is missing!!!!
				              /*
				               * Returns status code indicating whether there was an error.
				               * Can be one of following in ConnectionResult: SUCCESS,
				               * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
				               * SERVICE_DISABLED, SERVICE_INVALID.
				               */
				              GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
				                     this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
				       }
				}
			
		
	        if (mapa!=null) mapa.setMyLocationEnabled(true);
	        
	        
	        		
	        

	        if (LanzarServicioBoot.intentservicio==null) contexto.startService(new Intent(contexto,ServicioRecepcion.class));
	            try
		        {
		        modelolocalizador=Main.leer(Main.KEY_MODELO);    	
		        }
		        catch (Exception e){e.printStackTrace();}
	        	        
	      
	       	//Obsoleto (api googlemaps antigua)        
	        //mapa=(MapView)findViewById(R.id.mapa);
	        
	 
	        
	  
	        	     
	        
	        
	        OnMarkerClickListener listenermarker=new OnMarkerClickListener(){

				public boolean onMarkerClick(Marker marker) {
					String latitudtexto = Double.valueOf(marker.getPosition().latitude).toString();
					String longitudtexto = Double.valueOf(marker.getPosition().longitude).toString();
					String coordenadas=latitudtexto+","+longitudtexto;
					
					//reiniciartimerwebservice();
				    
					
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.streetview:cbll="+coordenadas));
															
					if (marker.getTitle().contentEquals(contexto.getString(R.string.estoyaqui))) contexto.startActivity(intent);
			        				
					
					//El siguiente código actualiza el spinner
					int pos_spinner=adapter.getPosition(marker.getTitle());
					spinnerDevices.setSelection(pos_spinner);
					pos_spinner=0;
					
					/*
					String body= "<GTSRequest command=\"mapdata\">\r\n" + 
					   		"    <Authorization account=\"" + preferencias.getString(Main.KEY_USERINTERNET, "")
					   		+ "\" user=\"admin\" password=\"" + preferencias.getString(Main.KEY_PASSINTERNET, "") + "\"/>\r\n" + 
					   		"    <MapData>\r\n" + 
					   		"        <DeviceGroup>all</DeviceGroup>\r\n" + 
					   		"    </MapData>\r\n" + 
					   		"</GTSRequest>\r\n" + 
					   		"\r\n" + 
					   		"";
					
				new HttpPost(contexto).execute(body);
				*/	
					
					return false;
				}};
				
				
				
			if (mapa!=null) 
			{
			mapa.setOnMarkerClickListener(listenermarker);
			OnMapClickListener listenermapa=new OnMapClickListener(){

				public void onMapClick(LatLng latlng) {
					
					//reiniciartimerwebservice();
				    
				}};
			mapa.setOnMapClickListener(listenermapa);
			}
			
			
	        
	        
	        //zoomcontrol=(ZoomControls)findViewById(R.id.zoomcontrols);
	        
	        desplegarmenu=(Button)findViewById(R.id.botonmenu);
	        
	        
	        
	        cambiarvista=(ImageView)findViewById(R.id.imagenvistasatelite);
	        
	        
	        updateweb=(ImageView)findViewById(R.id.imagenupdate);
	     
	        imagenruta=(ImageView)findViewById(R.id.imagenruta);
	        
	        utiles=new Utiles();
	        
	        //mapcontroller=mapa.getController();
	        
	        
	        
	        //***************************************************
	        	
	        
	        //basedatos=new Bdhistorial(contexto);
	        
	        //mylocationoverlay=new MyLocationOverlay(contexto,mapa);
	        
	        //miglview=(MiGLView)findViewById(R.id.micubo);
	             
	        locseleccionado=(ImageView)findViewById(R.id.locseleccionado);
	        
	        anterior=(ImageView)findViewById(R.id.anterior);
	        
	        posterior=(ImageView)findViewById(R.id.posterior);
	        
            vista3d=(ImageView)findViewById(R.id.image3d);
            
            agregar=(ImageView)findViewById(R.id.imageViewAgregar);
	        
	        //down=(ImageView)findViewById(R.id.imageViewDown);
	        
	        
	        textolocalizador=(TextView)findViewById(R.id.textolocalizador);
	        
	        textodepuracion=(TextView)findViewById(R.id.textodepuracion);
	        
	        //this.insertardatosdemostracion();
	        
	        spinnerDevices=(Spinner)findViewById(R.id.spinnerDevices); 
			
			
		  	//contexto.startService(new Intent(contexto,ServicioRecepcion.class));        
            
	        agregar.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Apéndice de método generado automáticamente
					Utiles.aplicarAnimacionZoom( agregar, new Animacion());
					
					Intent intent=new Intent(contexto,NuevoLocalizador.class);
					  startActivity(intent);
					  //Salir();
				}});

		        
	        
	        
	        locseleccionado.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Apéndice de método generado automáticamente
					applyRotation( locseleccionado , 0,720, new Animation.AnimationListener(){

						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(contexto,SeleccionarLocalizador.class);
							startActivity(intent);
							animation.cancel();
							
						}

						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}}); 
					
					  //Salir();
				}});
              
	       
	        
	        
	        
	        anterior.setOnClickListener(new OnClickListener(){
             
				public void onClick(View arg0) {
					Utiles.aplicarAnimacionZoom( anterior, new Animacion());
					
					Utiles.aplicarAnimacionZoom( locseleccionado , new Animacion());
						
				   
					if (cursor.moveToPrevious()){
						
						actualizarlocalizador();
						
					} else if (cursor.moveToLast()){
						     
							actualizarlocalizador();				        
					       }
					
					
				}
				
				
				
				});

	        
	        
           
	        posterior.setOnClickListener(new OnClickListener(){
              
	        	
				public void onClick(View arg0) {
					
					Utiles.aplicarAnimacionZoom( locseleccionado , new Animacion());
					Utiles.aplicarAnimacionZoom( posterior , new Animacion());
				
					if (cursor.moveToNext()){
						
						actualizarlocalizador();
						
					} else if (cursor.moveToFirst()){
						     
							actualizarlocalizador();				        
					       }
					
                     }
				});

	        
	        
	        
	        vista3d.setOnClickListener(new OnClickListener(){
				
	        	


				public void onClick(View arg0) {
					Utiles.aplicarAnimacionZoom( vista3d, new Animacion());
						
				    if (activado3d) {
				    	
				    	backupposicioncamara = new CameraPosition.Builder()
					    .target(mapa.getCameraPosition().target)      
					    .bearing(backupbearing)             
					    .zoom(backupzoom)
					    .tilt(backuptilt)
					    .build(); 
				    	
				    	mapa.animateCamera(CameraUpdateFactory.newCameraPosition(backupposicioncamara));
				    	activado3d=false;
				    }
				    else
				    {	
									   					
				    backupbearing=mapa.getCameraPosition().bearing;
				    backuptilt=mapa.getCameraPosition().tilt;
				    backupzoom=mapa.getCameraPosition().zoom;
				
					CameraPosition posicioncamara = new CameraPosition.Builder()
				    .target(mapa.getCameraPosition().target)      
				    .bearing(mapa.getCameraPosition().bearing)             
				    .zoom(ZOOMGRANDE)
				    .tilt(TILTGRANDE)
				    .build();                 
			       
			    
			     
				    
				     mapa.animateCamera(CameraUpdateFactory.newCameraPosition(posicioncamara));
				     activado3d=true;
				    }
				
				}
				
				
				
				});

	        
	        
	        /*
	        down.setOnClickListener(new OnClickListener(){
	             
				public void onClick(View arg0) {
				

				    tilt-=ANGULOINCREMENTO;
				    
				    if (tilt<0) tilt=0;
				    
					
				
					CameraPosition posicioncamara = new CameraPosition.Builder()
				    .target(mapa.getCameraPosition().target)      
				    .bearing(mapa.getCameraPosition().bearing)             
				    .zoom(mapa.getCameraPosition().zoom)
				    .tilt(tilt)                   
				    .build();                 
			       
			    
			     
				    
				mapa.animateCamera(CameraUpdateFactory.newCameraPosition(posicioncamara));
					
				}
				
				
				
				});

	        */
	        
	        this.inicializarIconos();
	      
	        
	        
	        
	        
	        if (preferencias.getBoolean(Main.KEY_PRIMERINICIO, true)) 
	        {	
            Intent intentidioma=new Intent(this,Idioma.class);
            startActivity(intentidioma);
	        }
	        
	        
	        
	        
	        
	        if (modelolocalizador!=null) guardarconfigdefecto();
	        
	        
			
			
			
			
			this.desplegarmenu.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
                
					Utiles.aplicarAnimacionZoom( desplegarmenu, new Animacion());
					
				openOptionsMenu();
                
                
					
				}});
			
			this.updateweb.setOnClickListener(new OnClickListener(){

				public void onClick(View view) {
					 Utiles.getPosicionGrupo(contexto);
				}});
			
			
			this.imagenruta.setOnClickListener(new OnClickListener(){
				public void onClick(View view) {
					
					// new FechaHoraRangoDialog(contexto).show(); 
					
					
				

					
					OnDateSetListener dateSetListener=new OnDateSetListener(){

						public void onDateSet(DatePicker datepicker, int anio,
								int mes, int dia) {
							
							
							String device;
							if (spinnerDevices.getCount()>0)
							{
							device=spinnerDevices.getSelectedItem().toString();
							
							//mitimerwebservice.cancel();
							
							
							String body="<GTSRequest command=\"mapdata\">\r\n" +
									" <Authorization account=\""+ preferencias.getString(Main.KEY_USERINTERNET, "")
							   		+ "\" user=\"admin\" password=\"orion\"/>\r\n" +
									" <MapData>\r\n" +
									" <Device>"+ device +"</Device>\r\n" +
									" <TimeFrom timezone=\"GMT+1\">"+anio+"/"+(mes+1)+"/"+dia+",00:00:01</TimeFrom>\r\n" +
									" <TimeTo timezone=\""+Utiles.getGMT()+"\">"+anio+"/"+(mes+1)+"/" +dia+",23:59:59</TimeTo>\r\n" +
									" <Limit type=\"last\">200</Limit>\r\n" +
									" </MapData>\r\n" +
									"</GTSRequest>";
							new HttpPostRuta(contexto).execute(body);
						    }

						}};
						
						if (spinnerDevices.getCount()>0)
						{	
						Calendar c = Calendar.getInstance();
						int anio = c.get(Calendar.YEAR);
						int mes = c.get(Calendar.MONTH);
						int dia = c.get(Calendar.DAY_OF_MONTH);
					    DatePickerDialog dpd = new DatePickerDialog( contexto, dateSetListener, anio, mes, dia );
					    
					    
					    dpd.setTitle("Mostrar ruta de " + spinnerDevices.getSelectedItem().toString());
				       
					 				    
					    dpd.show();
						} 	else Toast.makeText(contexto, "No existen dispositivos  en servidor", Toast.LENGTH_LONG).show();
						

	     	}});
						
			
			this.spinnerDevices.setOnItemSelectedListener(new OnItemSelectedListener(){

				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					
					//reiniciartimerwebservice();
				    					
					String seleccionado=parent.getItemAtPosition(pos).toString();
					
					for (Marker marker:listmarker){
						if (marker.getTitle().equalsIgnoreCase(seleccionado))
						{
							//La primera vez, no queremos que se acerque a la posición del primero, sino una vista global de todos.
							if (seleccionadoamano) mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM));
							
							seleccionadoamano=true;
						}
					}
					
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}});

			this.cambiarvista.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
                
					
					//applyRotation( cambiarvista ,0,360, new Animacion());
					Utiles.aplicarAnimacionZoom( cambiarvista, new Animacion());
					
			
					
					
					switch (mapa.getMapType()) {
			        case GoogleMap.MAP_TYPE_NORMAL:
			        	mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			            break;
			        case GoogleMap.MAP_TYPE_HYBRID:
			        	mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);		        				           
			            break;
			        case GoogleMap.MAP_TYPE_SATELLITE:
			        	mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			        	break;	
			        case GoogleMap.MAP_TYPE_TERRAIN:
			        	mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			        
		        		   
		             
				}
				
		
				}});

	        
			  //Manejador para dibujar posición periodicamente
              Main.handle_dibujar_miubicación = new Handler() {
            
			
                
			
            	@Override
				public void handleMessage(Message msg) {
	               
            		   
	                   try{                 
	                	
	                	   utiles.dibujardondeestoy(punto,precision,mapa,contexto);	
	                    
	                    }
	                    catch (java.lang.NullPointerException ex){ex.printStackTrace();}
	                    catch (java.lang.IllegalArgumentException ex){ex.printStackTrace();}
	                 	                
	           
				}
              };
			
            
             
              
            //Manejador para dibujar posicion ultima de cada dispositivo
              Main.handle_dibujar_servicioweb = new Handler() {
            
			
                
			
            	@Override
				public void handleMessage(Message msg) {
	               
            		//Variable usada para SpinnerDevice, cuando el timer actualice no queremos ver la posicion del seleccionado, sino de todos en global
            		seleccionadoamano=false;
            		
            	    //En cada recepción de coordenadas del servicio web se borran las anteriores posiciones  
            		//if (mapa!=null) mapa.clear();  		
            		for (int i=0; i<listmarker.size();i++){ 
            			Marker marker=listmarker.get(i);
            			marker.remove();
            			
            		}
            		 	
            		listmarker.clear();
            		    
            		   try{
            			   for (MarkerOptions markeroptions:listmarkeroptionsinternet){           			       
            				 
                           Marker marker=mapa.addMarker(markeroptions);
                           
                           listmarker.add(marker);
                                                        
                            
                            }
            			   
            			   LatLngBounds.Builder builder = new LatLngBounds.Builder();
            			   for (Marker marcador : listmarker) {
            			       builder.include(marcador.getPosition());
            			   }
            			   LatLngBounds bounds = builder.build();
            			   
            			   int padding = 80; // margen en pixels
            			   CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            			   
            			   mapa.animateCamera(cu);
            			   
            			   
            			   
            			   
            		   }
            		   catch (Exception e){e.printStackTrace();}
            		
	                 	  
            		   List<String> lista_devices = new ArrayList<String>();
          		     
            		   
            	       
            		   Collections.sort(lista_devices);
            		 for (Marker marker:listmarker){
          		    	lista_devices.add(marker.getTitle()); 
          		     }
          		     adapter = new ArrayAdapter<String>(contexto, android.R.layout.simple_spinner_item,lista_devices);
          		     
          		     if (adapter.isEmpty()) spinnerDevices.setVisibility(View.INVISIBLE);
          		     else spinnerDevices.setVisibility(View.VISIBLE);
          		     
          		     spinnerDevices.setAdapter(adapter);
          		     
          		     
          		     
	           
				}
              };
		
 	    
              
            //Manejador para dibujar posicion ultima de cada dispositivo
              Main.handle_dibujar_ruta = new Handler() {
            
			
                
			
            	

				@Override
				public void handleMessage(Message msg) {
	               
            		
            	    //En cada recepción de coordenadas del servicio web se borran las anteriores posiciones  
            		//if (mapa!=null) mapa.clear();  		
            		for (int i=0; i<listmarkerruta.size();i++){ 
            			Marker marker=listmarkerruta.get(i);
            			marker.remove();
            			
            		}
            		 	
            		listmarkerruta.clear();
            		    
            		   try{
            			   for (MarkerOptions markeroptions:listmarkeroptionsruta){           			       
            				 
                           Marker marker=mapa.addMarker(markeroptions);
                           
                           listmarkerruta.add(marker);
                                                        
                            
                            }
            			   
            			   LatLngBounds.Builder builder = new LatLngBounds.Builder();
            			   for (Marker marcador : listmarkerruta) {
            			       builder.include(marcador.getPosition());
            			   }
            			   LatLngBounds bounds = builder.build();
            			   
            			   int padding = 80; // margen en pixels
            			   CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            			   
            			   mapa.animateCamera(cu);
            			   
            			   
            			   
            			   
            		   }
            		   catch (Exception e){e.printStackTrace();}
            		
	                 	  
            		 
          		     
          		     
	           
				}
              };
		
              
			
			
			//************************************************
			//** CODIGO MANEJADOR DE PROCESO AL TERMINAR DE DIBUJAR RECTANGULO DE LIMITAR AREA
			//************************************************
			handle_limitar_area = new Handler() {
            
			
                
				@Override
				public void handleMessage(Message msg) {
					
					@SuppressWarnings("unchecked")
					ArrayList<LatLng> puntos = (ArrayList<LatLng>) msg.obj;
					
					boolean moverdearribaabajo=true; 
					boolean moverdeizquierdaderecha=true; 
					
					
					
					
					
	                    if (moverdearribaabajo && moverdeizquierdaderecha){				
						    
	                    	
	                    	
	                    	
	                    	String valorlatitudp1=Double.toString( puntos.get(0).latitude );
							String valorlongitudp1=Double.toString( puntos.get(0).longitude);
							String valorlatitudp2=Double.toString( puntos.get(1).latitude);
							String valorlongitudp2=Double.toString( puntos.get(1).longitude);
	                    	
							
	                    	
							String stringlatitudp1=valorlatitudp1;
							String stringlongitudp1=valorlongitudp1;
							String stringlatitudp2=valorlatitudp2;
							String stringlongitudp2=valorlongitudp2;
							
							
							 @SuppressWarnings("unused")
							String nswelatitud1=""; 		
						     @SuppressWarnings("unused")
							String nswelongitud1="";
						        
						        /*
								if (puntos.get(0).getLatitudeE6()>=0) nswelatitud1="N#";
								if (puntos.get(0).getLatitudeE6()<0) {nswelatitud1="S#"; stringlatitudp1=stringlatitudp1.substring(1);};
								if (puntos.get(0).getLongitudeE6()>=0) nswelongitud1="E#";
								if (puntos.get(0).getLongitudeE6()<0) {nswelongitud1="W#"; stringlongitudp1=stringlongitudp1.substring(1);}
				            	*/		
							//Toast.makeText(contexto, "latitud p1:"+stringlatitudp1+ " longitud p1:"+stringlongitudp1, Toast.LENGTH_LONG).show();
							
							
					 		
							String comandosms="";
					       
					        
							
							if (modelolocalizador.contentEquals("TK-102") || modelolocalizador.contentEquals("TK-103"))
							comandosms=Utiles.decodificar("stockade#clave "+stringlatitudp1+","+stringlongitudp1+";"+stringlatitudp2+","+stringlongitudp2);
							if (modelolocalizador.contentEquals("TK-108") || modelolocalizador.contentEquals("TK-110")){
							   
								/*
								if (puntos.get(0).getLatitudeE6()>=0) nswelatitud1="N#";
								if (puntos.get(0).getLatitudeE6()<0) {nswelatitud1="S#"; stringlatitudp1=stringlatitudp1.substring(0);}
								if (puntos.get(0).getLongitudeE6()>=0) nswelongitud1="E#";
								if (puntos.get(0).getLongitudeE6()<0) {nswelongitud1="W#"; stringlongitudp1=stringlongitudp1.substring(0);}
								*/
							//comandosms=Utiles.decodificar("#751#"+ Math.round(LimiteOverlayCirculo.distance) + "#600#" + stringlongitudp1+ "N#" + stringlatitudp1+ "E#" + "#clave##");
						    //comandosms=Utiles.decodificar("#751#"+ Math.round(LimiteOverlayCirculo.distance) + "#600#" + stringlatitudp1+ "N#" + stringlongitudp1+ "E#" + "#clave##");	
							//comandosms=Utiles.decodificar("#751#"+ Math.round(LimiteOverlayCirculo.distance) + "#600#" + stringlatitudp1+ nswelatitud1 + stringlongitudp1+ nswelongitud1+ "#clave##");
							//comandosms=Utiles.decodificar("#751#"+ Math.round(LimiteOverlayCirculo.distance) + "#600#" + stringlatitudp1+ "N#" + stringlongitudp1+ "E#" + "#clave##");
							}	
								//comandosms=Utiles.decodificar("#751#"+ Math.round(LimiteOverlayCirculo.distance) + "#600#" + stringlatitudp1+ "#" + stringlongitudp1+ "#" + "#clave##");
							
							Utiles utiles=new Utiles();
							
							if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(contexto, 
									contexto.getResources().getString(R.string.limitararea), 
									contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,R.drawable.ic_limitararea);
							else utiles.confirmarirconfiguracion(contexto);
	                    } else Toast.makeText(contexto, "Debe dibujar el rectangulo de ARRIBA a ABAJO y de IZQUIERDA a DERECHA", Toast.LENGTH_LONG).show();
					
				}
			};
			
			
			//************************************************
			//** CODIGO MANEJADOR DE PROCESO AL RECIBIR SMS
			//************************************************
			handle = new Handler() {
            
			
                
		

				@Override
				public void handleMessage(Message msg) {
	                try {
	                
	                Object[] objeto=(Object[]) msg.obj;
					
	                
	                
	                final String hora=(String)objeto[0];	                
	                final String mensajesms=(String)objeto[1];
	                final String numerosms=(String)objeto[2];
					String titulo=contexto.getResources().getString(R.string.eventosmsrecibido);
					Builder builder = new AlertDialog.Builder(contexto);
					
					

								try {
									alertdialogrecibidosmsloc.cancel();           						
								}
								catch (Exception e)
								{
									Log.w("Cancelando AlertDialog anterior","No hay AlertDialog que cancelar");
								}

					
								Utiles.alertasaldo(contexto, numerosms);
				    
				    
				    
				    
				    android.content.DialogInterface.OnClickListener listenerbotonmostrar=new android.content.DialogInterface.OnClickListener(){

					

						public void onClick(DialogInterface arg0, int arg1) {
							
							
							MarkerOptions markeroptionssms=dibujardesms(hora,mensajesms, numerosms);
							
							listmarkeroptionssms.add(markeroptionssms);
							
							SMSReceiver.mNotificationManager.cancel(mensajesms,SMSReceiver.NOTIFICAR_ID);
						
						}};
					
						
						String direccion=Utiles.obtenerdirecciondesms(contexto, mensajesms,numerosms);
						
						traduccion=Utiles.traducirentrada(mensajesms, contexto);
						
                    						
						
						
						
						
						String nombreloc=basedatos.consultarnombre(basedatos.obtenernumero(numerosms));
						String modeloloc=basedatos.consultarModelo(basedatos.obtenernumero(numerosms));
			 	 	    
			 	 	    builder.setIcon(geticonosubmodelo(modeloloc, contexto));
						
						
						String textomensajedialogo=contexto.getResources().getString(R.string.recibidode) + " " + nombreloc + " "+ numerosms + contexto.getResources().getString(R.string.fechahoraderecepcion) + hora + "\n" + direccion + traduccion + contexto.getResources().getString(R.string.textodelsms) + mensajesms;
						
						
					    String textobotonpositivo=contexto.getResources().getString(R.string.mostrarlocalizacion);
					    //String textomensajedialogo=hora + "\n\nTexto del mensaje:\n" + mensajesms;
					    
					
					        //El siguiente bloque try / catch cambia el texto del boton dependiendo de si es posible extraer 
					        //las coordenadas
							try{
								
								Basedatos bd=new Basedatos(contexto);
					            String modelo=bd.consultarModelo(numerosms);
					            bd.cerrarbd();
					 		    
					            
					            
                               
					            int modeloasumido=detectarmodelo(mensajesms);
			        	        
		        	        	//Si no se detecta modelo, el modelo asumido para mensaje es el configurado
		 			 		   if (modeloasumido==MODELODESCONOCIDO) 
		 			 		   {  	if (modelo.contentEquals("TK-102")) modeloasumido=MODELOTK102;
		 			 		   		if (modelo.contentEquals("TK-110")) modeloasumido=MODELOTK110; 
		 			 		   }
		 			 		   
		 			 		   if (modeloasumido!=MODELOTK110){
		 			 			   latitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLATITUD),contexto);
		 			        	   longitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLONGITUD),contexto);
		 		 				   }
		 		 				   else
		 		 				   {
		 	 			                if (validartk110(mensajesms)){ 		  
		 	 			          		    
			 			                						         
		 		 			                latitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLATTK110,FINLATTK110), 1,contexto);
				 				        	longitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLONGTK110,FINLONGTK110), 1,contexto);
		 	 			                } else throw new ExcepcionFormatoMensaje("Mensaje sms no pertenece a tk110"); 
		 		 				   }
		 			 		
					            
					            
					            
					            
					            
					 		    
							}
							catch (ExcepcionFormatoMensaje e) {
					 			// TODO Auto-generated catch block
					               //textomensajedialogo=hora +  "\n\nTexto del mensaje:\n" + mensajesms;
								   textobotonpositivo=contexto.getResources().getString(R.string.Continuar);
					 		 	   e.printStackTrace();
					 		   } catch (java.lang.NullPointerException e){
					 			  textobotonpositivo=contexto.getResources().getString(R.string.Continuar);  
					 			   e.printStackTrace();  
					 		   } catch (Exception e){
					 			  textobotonpositivo=contexto.getResources().getString(R.string.Continuar);
					 			   e.printStackTrace();
					 		   } 
			        
					
					builder.setTitle(titulo);
				    builder.setMessage(textomensajedialogo);
					builder.setPositiveButton(textobotonpositivo, listenerbotonmostrar);
					
				 
			        android.content.DialogInterface.OnClickListener listenerbotoncancelar=new android.content.DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							// TODO Apéndice de método generado automáticamente
							
						}};
					if (textobotonpositivo!=contexto.getResources().getString(R.string.Continuar)) builder.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), listenerbotoncancelar); 
					 
					//********************************************
					alertdialogrecibidosmsloc=builder.create();	
						 
					alertdialogrecibidosmsloc.show();        
	                }
	                catch (Exception e){
	                Log.e("Error", e.getMessage());	
	                }
					    
					    	
					    						
						
						
					
				     
					
					 
					
					
					
					
					 
				}

			};

			


			
			
			//*******************************************************
			//****** CODIGO DE RECEPCION GPS
			locationmanagerGPSintegrado=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
			locationmanagerGPSinternet=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
			
							
			
			
			locationlistenerGPSintegrado=new LocationListener(){

			

				public void onLocationChanged(Location location) {
					latitud=location.getLatitude();
					longitud=location.getLongitude();
			        precision=Float.valueOf(location.getAccuracy()).longValue();
					
					//Objeto necesario para hacer la tarea 
					punto=new LatLng(latitud,longitud); 
					//dibujarmiubicacion(punto,precision);
				}

				public void onProviderDisabled(String provider) {
					if (numeroavisosgps==1) 
						 
						{ 
						
		 
				        	
						
						 try{
						   
						    	
							AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		
							alert.setTitle(contexto.getResources().getString(R.string.gpsdeshabilitado));
							alert.setMessage(contexto.getResources().getString(R.string.accederconfiguracion));
		                    alert.setNegativeButton(contexto.getResources().getString(R.string.no), null);
		                    
							alert.setPositiveButton(contexto.getResources().getString(R.string.si), new DialogInterface.OnClickListener(){
		
								
								private boolean sefueaconfgps=false;
		              
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									Log.i("Dialogos", "pulsado si.");
							            if (!sefueaconfgps){
										Intent intent=new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								        contexto.startActivity(intent);
								        sefueaconfgps=true;
							                       
							            } 
								}});
							if (numeroavisosgps==2) alert.show();
							numeroavisosgps++;
						    }
							catch (Exception e){
								
							}
                          				
						}	
				}

				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					switch (status) {
			        case LocationProvider.TEMPORARILY_UNAVAILABLE:
			        	locationmanagerGPSinternet.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistenerGPSinternet);
			            break;
			        case LocationProvider.OUT_OF_SERVICE:
			        	locationmanagerGPSinternet.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistenerGPSinternet);			           
			            break;
			        case LocationProvider.AVAILABLE:
			        	locationmanagerGPSinternet.removeUpdates(Main.locationlistenerGPSinternet);
			            
					    
			             
					}
				}
					
				
			

			
				
				
				
				
				
				
			


			
			};

			
			locationlistenerGPSinternet=new LocationListener(){

				public void onLocationChanged(Location location) {
			
					latitud=location.getLatitude();
			
					longitud=location.getLongitude();
			        
					precision=Float.valueOf(location.getAccuracy()).longValue();
						        
					
					//Objeto necesario para hacer la tarea 
					punto=new LatLng(latitud,longitud);
					//dibujarmiubicacion(punto,precision);
	
				}

				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
				
				}

				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				}
				
				
				
	
			
			};

			
			
			 //Registra los locationmanager de cada receptor gps 
			//locationmanagerGPSintegrado.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistenerGPSintegrado);
			//locationmanagerGPSinternet.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistenerGPSinternet);
		
			
			//*******************************************************
			
			
		
	      			
			//SOLO PARA PRUEBAS DESHABILITAR SIGUIENTE LINEA 
	        //this.guardarconfigdefecto();
	        
	      //Pruebas
	        //latitud=new Double(38.356482);
	        //longitud=new Double(-0.474687);
	        //latitud=new Double(0);
	        //longitud=new Double(0);
	        
	       
	    	        
	        
	        
	        //textopruebas.setText("");      
	        //textopruebas.setText(Utiles.decodificar("Este es mi numero de telefono: #telefono clave#clave"));
			//textopruebas.setText("Este es mi numero: "+getPhoneNumber());  
	    
			//textoparapruebas=getPhoneNumber();
			
			//Clase para escuchar del socket
           
            
            
			
			
            
			
			
			
           
			
			

        
			
			
	    }
		
		
		

	    protected void reiniciartimerwebservice() {
	    	//Reinicia el timer actualizador de posiciones
			mitimerwebservice.cancel();
			myTaskWebService = new MyTimerTaskWebService();
		    mitimerwebservice = new Timer();
			
		     
		     preferencias = getSharedPreferences(Main.PREFERENCIAS,Context.MODE_PRIVATE);
				
		     
		     if (!preferencias.getString(Main.KEY_USERINTERNET, "").equalsIgnoreCase(""))
		     {
		    	 
		    	 
		    	 mitimerwebservice.scheduleAtFixedRate(
		    		 myTaskWebService, 
		    		 0, 
		    		 PERIODO_DIBUJARWEBSERVICE);
		    }
		}

		@SuppressWarnings("unused")
		private String getPhoneNumber(){
	    	  TelephonyManager mTelephonyManager;
	    	  mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
	    	  return mTelephonyManager.getLine1Number();
	    	}
	    
	    
	   

		
		
	    
		  //Guarda una preferencia de tipo text
		  public static boolean guardar(String key, String valor){
		   return Main.preferencias.edit().putString(key, valor).commit();
		  }
		
		//Guarda una preferencia de tipo int
		  public static boolean guardar(String key, int valor){
		  
			  
		  return Main.preferencias.edit().putInt(key, valor).commit();
		  }
		  
		//Guarda una preferencia de tipo float
		  public static boolean guardar(String key, float valor){
		  
			  
		  return Main.preferencias.edit().putFloat(key, valor).commit();
		  }
		
		  
		//Guarda una preferencia de tipo int
		  public static boolean guardar(String key, boolean valor){
		  
			  
		  return Main.preferencias.edit().putBoolean(key, valor).commit();
		  }
		
		  
		  //Lee una preferencia de tipo String
		  public static String leer(String key){
		   return preferencias.getString(key, ""); 
		  }
		
		  //Lee una preferencia de tipo int
		  public static Integer leerint(String key){
		      preferencias.getInt(key, 0);
			  return preferencias.getInt(key, 0); 
		  }
		  
		  
		//Lee una preferencia de tipo int
		  public static Long leerlong(String key){
		      preferencias.getLong(key, 0);
			  return preferencias.getLong(key, 0); 
		  }
		  
		//Lee una preferencia de tipo int
		  public static Float leerfloat(String key){
		      preferencias.getFloat(key, 0);
			  return preferencias.getFloat(key, 0); 
		  }

		  //Abre el menu automaticamente siempre que cambiamos foco
		  @Override
		  public void onWindowFocusChanged(boolean hasFocusFlag) {
		      super.onWindowFocusChanged(hasFocusFlag);       
		      if (hasFocusFlag) {
		          
		    	  
		      }
		  }	  
		  
		  @Override
		  public void onAttachedToWindow() {
		      super.onAttachedToWindow();
		      //openOptionsMenu();
		  } 
		 
		  
	   @Override	 
	   public boolean onPrepareOptionsMenu(Menu menu){
		   menu.clear();
		   getMenuInflater().inflate(R.menu.menu_principal, menu);
		   
		   
		   menu.findItem(R.id.itemidiomas).setVisible(true);
		       
		      if (!this.confavanzada){  
			       //Oculta el menú avanzado si el numero de telefono del localizador no es *4222*
				   if (!Main.leer(KEY_NUMTELLOCALIZADOR).endsWith(Main.CLAVEMENUAVANZADO)) 
				   menu.findItem(R.id.itemconfiguracion_avanzada).setVisible(false);
				   else menu.findItem(R.id.itemconfiguracion_avanzada).setVisible(true);
		       }
		       
		       if (modelolocalizador.contentEquals("TK-110")){
		    	  
		    	   menu.findItem(R.id.itemdetectarmovimiento).setVisible(true);
		    	   menu.findItem(R.id.itemexcesovelocidad).setVisible(false);
		    	   menu.findItem(R.id.itemconfiguracioninicial).setVisible(false);
		    	   menu.findItem(R.id.itemmodovozon).setVisible(false);
		    	   menu.findItem(R.id.itemmodovozoff).setVisible(false);
		    	   menu.findItem(R.id.itemdesautorizar).setVisible(false);
		    	   menu.findItem(R.id.itemdetenersos).setVisible(false);
		    	   menu.findItem(R.id.itemautorastreo).setVisible(false);
		    	   
		    	   
		    	   menu.findItem(R.id.itempreguntarlimitararea).setVisible(true);
		    	   //menu.findItem(R.id.itemactivaralamaareallamada).setVisible(true);
		    	   
		    	   //menu.findItem(R.id.itemactivaralamaareasms).setVisible(true);
		    	   //menu.findItem(R.id.itemdesactivaralamaarea).setVisible(true);
		    	   menu.findItem(R.id.itemlimitararea).setVisible(true);
		    	   menu.findItem(R.id.itemnumeroprincipal).setVisible(true);
		    	   menu.findItem(R.id.itemlimitararea).setVisible(false);
		    	   menu.findItem(R.id.itemcortarcorriente).setVisible(false);
		    	   
		    	      
				   
		    			    	   
		       } 
		       else
		       {
		    	  
		    	   menu.findItem(R.id.itemdetectarmovimiento).setVisible(true);
		    	   menu.findItem(R.id.itemexcesovelocidad).setVisible(true);
		    	   menu.findItem(R.id.itemconfiguracioninicial).setVisible(true);
		    	   menu.findItem(R.id.itemmodovozon).setVisible(true);
		    	   menu.findItem(R.id.itemmodovozoff).setVisible(true);
		    	   menu.findItem(R.id.itemdesautorizar).setVisible(true);
		    	   menu.findItem(R.id.itemdetenersos).setVisible(true);
		    	   menu.findItem(R.id.itemautorastreo).setVisible(true);
		    	   
		    	   
		    	   menu.findItem(R.id.itempreguntarlimitararea).setVisible(false);
		    	   //menu.findItem(R.id.itemactivaralamaareallamada).setVisible(false);
		    	   //menu.findItem(R.id.itemactivaralamaareasms).setVisible(false);
		    	   //menu.findItem(R.id.itemdesactivaralamaarea).setVisible(false);
		    	   
		    	   menu.findItem(R.id.itemlimitararea).setVisible(true);
		    	   //menu.findItem(R.id.itemmanerasaviso).setVisible(true);	
		    	   menu.findItem(R.id.itemnumeroprincipal).setVisible(false);
		    	   menu.findItem(R.id.itemlimitararea).setVisible(true);
		    	   menu.findItem(R.id.itemcortarcorriente).setVisible(false);
			    	 
			    	   
		    	   
		       }	   
		       
		       if (modelolocalizador.contentEquals("TK-103")){
		    	   menu.findItem(R.id.itemcortarcorriente).setVisible(true);
			    	 
		       }
		       
		       
		       menu.findItem(R.id.itemdondeestoy).setVisible(true);
		             
			          
		   
	   return true;  
	   }
	
	   
	   
		  
	 	//Prepará el menú
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		
				   
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_principal, menu);
			
	          
			
			
			return true;
		}
		
		
		
		// Para cada opción del menú seleccionada ejecuta la acción necesaria
	 	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		String comandosms;
		
		
		
		//String invalidonum=this.getResources().getString(R.string.invalidonumlocalizador);
			switch (item.getItemId()) {
				
		
			/*

Listado de APN o nombre del punto de acceso, usuario y clave de todas las operadores móviles para configurar el acceso a Internet móvil mediante módem 3G. Es válido para GPRS, UMTS, 3G, 3,5G, HSPA, etc. Podemos utilizarlo en cualquier teléfono o dispositivo, incluido iPhone, Android, iPad, etc.
Operador 	APN 	Usuario 	Clave 	¿Ayuda?
APN Amena 	orangeworld 	orange 	orange 	Foro Amena/Orange
APN Carrefour Móvil 	CARREFOURINTERNET 	[vacío] 	[vacío] 	Foro Móvil
APN DigiMobil 	internet.digimobil.es 	digi 	digi 	Foro Móvil
APN Eroski 	gprs.eroskimovil.es 	wap@wap 	wap125 	Foro Móvil
APN Euskaltel 	internet.euskaltel.mobi 	CLIENTE 	EUSKALTEL 	Foro Móvil
APN Jazztel 	jazzinternet 	[vacío] 	[vacío] 	Foro Jazztel
APN Lebara 	gprsmov.lebaramobile.es 	[vacío] 	[vacío] 	Foro Móvil
APN Llamaya 	moreinternet 	[vacío] 	[vacío] 	Foro Móvil
APN LycaMobile 	data.lycamobile.es 	lmes 	plus 	Foro Móvil
APN Movistar 	movistar.es / telefonica.es 	MOVISTAR / TELEFONICA 	MOVISTAR / TELEFONICA 	Foro Movistar
APN Másmovil 	internetmas 	[vacío] 	[vacío] 	Foro Móvil
APN Ono 	internet.ono.com 	[vacío] 	[vacío] 	Foro Móvil
APN Orange 	orangeworld 	orange 	orange 	Foro Orange
APN Pepephone 	gprsmov.pepephone.com 	[vacío] 	[vacío] 	Foro Móvil
APN R 	internet.mundo-r.com 	[vacío] 	[vacío] 	Foro Móvil
APN Simyo 	gprs-service.com 	[vacío] 	[vacío] 	Foro Simyo
APN Telecable 	internet.telecable.es 	telecable 	telecable 	Foro Móvil
APN Tuenti Móvil 	tuenti.com 	tuenti 	tuenti 	Foro Móvil
APN Vodafone 	ac.vodafone.es 	vodafone 	vodafone 	Foro Vodafone
APN Yoigo 	internet 	[vacío] 	[vacío] 	Foro Yoigo

			 * 
			 * */
			
			/* #802#APN letters or digits, 4-20 bits# Log user name letters or digits4-20 bits in # Log password letter or digits 4-20 bits in # terminal password 4 bits ##"
			 * */
			
			case R.id.itemuserypass:
			{
			  Intent intent=new Intent(contexto,UseryPass.class); 	
			  contexto.startActivity(intent);
			}	
			break;
			
			
			/*
			
			case R.id.yoigo:
		
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave internet");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
			//		comandosms=Utiles.decodificar("apnuser#clave tuenti");
			//		if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
			//		comandosms=Utiles.decodificar("apnpasswd#clave tuenti");
			//		if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
			//		else utiles.confirmarirconfiguracion(this);    	
				}
    	
				
				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#internet##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}
				
		     break;
         
			
			case R.id.tuenti:
					if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
						comandosms=Utiles.decodificar("#802#tuenti.com#tuenti#tuenti##clave##");
						if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
						
						else utiles.confirmarirconfiguracion(this);    	
					}
					
					if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
						comandosms=Utiles.decodificar("apn#clave tuenti.com");
						if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
						comandosms=Utiles.decodificar("apnuser#clave tuenti");
						if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
						comandosms=Utiles.decodificar("apnpasswd#clave tuenti");
						if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
						else utiles.confirmarirconfiguracion(this);    	
					}
	    	
	  	     break;
	
			
			
			
			 case R.id.telecable:
						if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
							comandosms=Utiles.decodificar("#802#internet.telecable.es#telecable#telecable##clave##");
							if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
							
							else utiles.confirmarirconfiguracion(this);    	
						}
						
						if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
							comandosms=Utiles.decodificar("apn#clave internet.telecable.es");
							if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
							comandosms=Utiles.decodificar("apnuser#clave telecable");
							if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
							comandosms=Utiles.decodificar("apnpasswd#clave telecable");
							if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
							else utiles.confirmarirconfiguracion(this);    	
						}
		    	    
			  break;
		
	
			
			   case R.id.simyo:
					
					if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
						comandosms=Utiles.decodificar("apn#clave gprs-service.com");
						if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
						else utiles.confirmarirconfiguracion(this);    	
					}
	    	
				   
				   
					if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
						comandosms=Utiles.decodificar("#802#gprs-service.com##clave##");
						if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
						
						else utiles.confirmarirconfiguracion(this);
				    
					}	
				break;
			
			
	
		   case R.id.pepephone:
				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave gprsmov.pepephone.com");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}
   	
			   
			   if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#gprsmov.pepephone.com##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
			    
				}	
			break;
		
			
			case R.id.ono:
		
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave internet.ono.com");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#internet.ono.com##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;
		
			
			case R.id.masmovil:

				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave internetmas");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#internetmas##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;
			

			
			case R.id.LycaMobile:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave data.lycamobile.es");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave lmes");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave plus");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#data.lycamobile.es#lmes#plus##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;

			
			case R.id.llamaya:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave moreinternet");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}
    
				
				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#moreinternet##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;

			
			
			case R.id.jazztel:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave jazzinternet");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}
				
				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#jazzinternet##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;

			
			
			case R.id.euskaltel:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave internet.euskaltel.mobi");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave CLIENTE");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave EUSKALTEL");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#internet.euskaltel.mobi#CLIENTE#EUSKALTEL##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;

			
			
			case R.id.eroski:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave gprs.eroskimovil.es");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave wap");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave wap125");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#gprs.eroskimovil.es#wap@wap#wap125##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;

			
			
			case R.id.Digimobil:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave internet.digimobil.es");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave digi");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave digi");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#internet.digimobil.es#digi#digi##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;

			
			case R.id.orange:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave orangeworld");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave orange");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave orange");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}
				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#orangeworld#orange#orange##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			
			break;
			
			
			case R.id.vodafone:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave ac.vodafone.es");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave vodafone");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave vodafone");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}
			
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#ac.vodafone.es#vodafone#vodafone##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;
						
			
			case R.id.movistar:
				
				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave movistar.es");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave MOVISTAR");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave MOVISTAR");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#movistar.es#MOVISTAR#MOVISTAR##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}	
			break;	
			
			case R.id.amena:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))){
					comandosms=Utiles.decodificar("apn#clave orangeworld");
					 utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnuser#clave orange");
					utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					comandosms=Utiles.decodificar("apnpasswd#clave orange");
					utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					}
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#orangeworld#orange#orange##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}
				
			break;
			
			
			case R.id.lebara:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave gprsmov.lebaramobile.es");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}
	
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#gprsmov.lebaramobile.es##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}
				
			break;
			
			case R.id.carrefour:
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-102") || Main.leer(Main.KEY_MODELO).contentEquals("TK-103")){
					comandosms=Utiles.decodificar("apn#clave CARREFOURINTERNET");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					else utiles.confirmarirconfiguracion(this);    	
				}

				
				if (Main.leer(Main.KEY_MODELO).contentEquals("TK-110") || Main.leer(Main.KEY_MODELO).contentEquals("TK-118")){
					comandosms=Utiles.decodificar("#802#CARREFOURINTERNET##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.conexioninternet), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);    	
				}
				
			break;
			
			
			
			
			case R.id.itemruta:
				
					
				
				
			break;
			*/  
			
              case R.id.itemidiomas:
			    
				Intent intentidiomas=new Intent(this,Idioma.class);
				startActivity(intentidiomas);
				break;
			
			
			case R.id.itemespanol:
			    
				Resources res = getResources();
		        Configuration newConfig = new Configuration(res.getConfiguration());
		        // newConfig.locale = Locale.TRADITIONAL_CHINESE;
		        newConfig.locale = Locale.getDefault();
		        res.updateConfiguration(newConfig, null);        
		        
		        preferencias.edit().putString(Main.KEY_IDIOMA, "espanol").commit();
				
				break;
				
			case R.id.itemingles:
				Resources reso = getResources();
		        Configuration newConfigu = new Configuration(reso.getConfiguration());
		        // newConfig.locale = Locale.TRADITIONAL_CHINESE;
		        newConfigu.locale = Locale.ENGLISH;
		        
		        reso.updateConfiguration(newConfigu, null);        
		        
		        preferencias.edit().putString(Main.KEY_IDIOMA, "ingles").commit();
		        
			    break;
			
			
			    //Lanza el menú de Configuración
				case R.id.itemconfiguracion:
				  Intent intent=new Intent(contexto,SeleccionarLocalizador.class);
				  startActivity(intent);
				  //Salir();
				break;
				
				
				case R.id.itemconfiguraciongasto:
				
					Intent intentconfgasto=new Intent(INTENT_CONFIGURACION_GASTO);
					startActivity(intentconfgasto);
					
					
					break;
					
				case R.id.itemllamar:
					String modelo=Main.leer(Main.KEY_MODELO);
					Intent intent_llamar = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+preferencias.getString(KEY_NUMTELLOCALIZADOR, "")));
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) 
					{
					 if (!modelo.contentEquals("TK-110")) startActivity(intent_llamar);
					 else new Utiles().dialogocontinuarllamando(contexto);
					}
					else utiles.confirmarirconfiguracion(this);
					
				  break;
				
				case R.id.itemdondeestoy:
					
					//En pruebas
					//if (MyLocation.getGeoPointBuenaPrecision(contexto)!=null) punto=MyLocation.getGeoPointBuenaPrecision(contexto);
					//else if (MyLocation.getGeoPointPocaPrecision(contexto)!=null) punto=MyLocation.getGeoPointPocaPrecision(contexto);
					
					
					
					
					if (punto!=null){
					
					utiles.dibujardondeestoy(punto, precision, mapa, contexto);
					mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, ZOOM));
					this.zoomautomatico();
					
					}
					else Toast.makeText(contexto, R.string.esperandogps, Toast.LENGTH_LONG).show();
					
				
					
					
					
				break;
				
				case R.id.itempreguntarlimitararea:
					comandosms=Utiles.decodificar("#752##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.preguntarlimitararea), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
  
				break;
				
				
				case R.id.itemactivarcortarcorriente:
					comandosms=Utiles.decodificar("powercar#clave 00");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) 
						utiles.confirmarenvio(this, contexto.getResources().getString(R.string.activar) + " " + contexto.getResources().getString(R.string.cortarcorriente), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
  
				break;
				
				case R.id.itemdesactivarcortarcorriente:
					comandosms=Utiles.decodificar("powercar#clave 11");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) 
						utiles.confirmarenvio(this, contexto.getResources().getString(R.string.desactivar) + " " + contexto.getResources().getString(R.string.cortarcorriente), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					
					else utiles.confirmarirconfiguracion(this);
  
				break;

	
				
				/*
				case R.id.itemactivaralamaareallamada:
					comandosms=Utiles.decodificar("#720#1##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.activaralarmaarea), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
  
				break;
	
					
				case R.id.itemactivaralamaareasms:
					comandosms=Utiles.decodificar("#720#2##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.activaralarmaarea), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
  
				break;
				
				
	
				case R.id.itemdesactivaralamaarea:
					comandosms=Utiles.decodificar("#720#0##clave##");
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.desactivaralarmaarea), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
					
					
  
				break;
	            */
				
				case R.id.itemconfiguracioninicial:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_INICIALIZAR));
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.resetfabrica), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
  
				break;  
				
				
				case R.id.itemdetenersos:
					comandosms="help me!";
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.detenersos), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,0);
					
					else utiles.confirmarirconfiguracion(this);
  
				break;
				
                
				
				
				
				case R.id.itemconfiguracion_avanzada:
				Intent intent_confavanzada=new Intent(INTENT_CONFIGURACIONAVANZADA);
				startActivity(intent_confavanzada);
					
					
					
				 break;
				 
				 
				 
				 
				case R.id.itemnumeroprincipal:
				
	                  if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))){ 
								
								//
								AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
			
								alert1.setTitle(contexto.getResources().getString(R.string.datorequerido));
								alert1.setMessage(contexto.getResources().getString(R.string.introducirnumeroestetelefono));
								alert1.setIcon(R.drawable.ic_llamar);
			
								// Establece un edittext de entrada para recoger la velocidad
								final EditText input1 = new EditText(this);
								
								input1.setInputType(InputType.TYPE_CLASS_PHONE);
								
								alert1.setView(input1);
			                    
								
								alert1.setPositiveButton(contexto.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
								
								 
								

								
								

								public void onClick(DialogInterface dialog, int whichButton) {
								  valortel = input1.getText().toString();
								  // Do something with value!
								  
								  String mensaje="";
								  String mensaje2=Utiles.decodificar("#720#3##clave##");
								//Ejemplo: #710#13588888888#0000#0000##  
								  
								if (modelolocalizador.contentEquals("TK-110")) mensaje=Utiles.decodificar("#710#"+valortel+"##clave##clave##");
								  
								  
								  if (Utiles.validartelefono(valortel)){
									  new Utiles().confirmarenvio2(contexto, contexto.getResources().getString(R.string.realizarconfinicial), contexto.getResources().getString(R.string.necesario2sms) + mensaje +  "\n\n" + Utiles.decodificar(mensaje2)+ contexto.getResources().getString(R.string.continuar_pregunta), mensaje, R.drawable.ic_llamar);
									  
									  
								  }							  
								  else Toast.makeText(contexto, contexto.getResources().getString(R.string.numeroincorrecto), Toast.LENGTH_LONG).show();
								  
								}
								  
								});
			
							    
								
								alert1.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
								  public void onClick(DialogInterface dialog, int whichButton) {
								    // Canceled.
									
								  }
								});
			
								alert1.show();
			                   
								//
						  }	  else new Utiles().confirmarirconfiguracion(contexto);
							
						
						
											
						
					break;

				 
				case R.id.itemautorizar:
                  if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))){ 
							
							//
							AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
		                    
														
							alert1.setTitle(contexto.getResources().getString(R.string.datorequerido));
							alert1.setMessage(contexto.getResources().getString(R.string.introducirnumeroautorizar));
							alert1.setIcon(R.drawable.ic_llamar);
		
							// Establece un edittext de entrada para recoger la velocidad
							final EditText input1 = new EditText(this);
							
							input1.setInputType(InputType.TYPE_CLASS_PHONE);
							
							alert1.setView(input1);
		                    
							
							
							alert1.setPositiveButton(contexto.getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
							
							 
							

							
							

							public void onClick(DialogInterface dialog, int whichButton) {
							  valortel = input1.getText().toString();
							  // Do something with value!
							  
							  String mensaje="";
							if (modelolocalizador.contentEquals("TK-102") || modelolocalizador.contentEquals("TK-103")) mensaje=Utiles.decodificar(Main.leer(Main.KEY_SMS_AUTORIZAR)+" "+valortel);
							if (modelolocalizador.contentEquals("TK-110")) mensaje=Utiles.decodificar(Main.leer(Main.KEY_SMS_AUTORIZAR)+valortel+"##clave##");
							  
							  
							contadormensajes=0;
							if (contadormensajes==0)
							  if (Utiles.validartelefono(valortel)){
								  new Utiles().confirmarenvio(contexto, contexto.getResources().getString(R.string.autorizartelefono), contexto.getResources().getString(R.string.mensajesmsaenviar) + mensaje +  contexto.getResources().getString(R.string.continuar_pregunta), mensaje, R.drawable.ic_llamar);
							      contadormensajes=contadormensajes+1;
							      
							  }							  
							  else Toast.makeText(contexto, contexto.getResources().getString(R.string.numeroincorrecto), Toast.LENGTH_LONG).show();
							  
							}
							  
							});
		
							alert1.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
							  public void onClick(DialogInterface dialog, int whichButton) {
							    
								
							  }
							});
		
							alert1.show();
		                   
							//
					  }	  else new Utiles().confirmarirconfiguracion(contexto);
						
					
	
					
					
					
					
										
					
				break;
			    
				case R.id.itemdesautorizar:
					
					  if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))){ 
							
							//
							AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
		
							alert1.setTitle(contexto.getResources().getString(R.string.datorequerido));
							alert1.setMessage(contexto.getResources().getString(R.string.introducirnumerodesautorizar));
							alert1.setIcon(R.drawable.ic_llamar);
		
							// Establece un edittext de entrada para recoger la velocidad
							final EditText input1 = new EditText(this);
							
							input1.setInputType(InputType.TYPE_CLASS_PHONE);
							
							alert1.setView(input1);
		                    
							
							alert1.setPositiveButton(contexto.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
							
							 
							

							
							

							public void onClick(DialogInterface dialog, int whichButton) {
							  valortel = input1.getText().toString();
							  // Do something with value!
							  
							  String mensaje="";
							if (modelolocalizador.contentEquals("TK-102") || modelolocalizador.contentEquals("TK-103")) mensaje=Utiles.decodificar(Main.leer(Main.KEY_SMS_DESAUTORIZAR)+" "+valortel);
							//if (modelolocalizador.contentEquals("TK-110")) mensaje=Utiles.decodificar("#"+Main.leer(Main.KEY_SMS_AUTORIZAR)+"#"+valortel+"##clave##");
							  
							  
							
							contadormensajes=0;
							if (contadormensajes==0)
							  if (Utiles.validartelefono(valortel)){
								  
								  new Utiles().confirmarenvio(contexto, contexto.getResources().getString(R.string.desautorizartelefono), contexto.getString(R.string.mensajesmsaenviar) + mensaje +  contexto.getString(R.string.continuar_pregunta), mensaje, R.drawable.ic_llamar);
							      contadormensajes=contadormensajes+1;
							  }							  
							  else Toast.makeText(contexto, contexto.getResources().getString(R.string.numeroincorrecto), Toast.LENGTH_LONG).show();
							  
							}
							  
							});
		
							alert1.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
							  public void onClick(DialogInterface dialog, int whichButton) {
							    // Canceled.
								
							  }
							});
		
							alert1.show();
                            
							
							
							//
					  }	  else new Utiles().confirmarirconfiguracion(contexto);
	
				
				break;
				case R.id.itemactivardetectarmovimiento:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_DETECCIONMOVIMIENTO_ON));
					if (!modelolocalizador.contains("TK-110") )
						if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.activar_detectar_movimiento), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,R.drawable.ic_detectarmovimiento);
						else utiles.confirmarirconfiguracion(this);
					else {
									
					
						  if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))){ 
								
								//
								AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
			
								alert1.setTitle(contexto.getResources().getString(R.string.datorequerido));
								alert1.setMessage(contexto.getResources().getString(R.string.cuandoelvehiculosemuevaysupere));
								alert1.setIcon(R.drawable.ic_llamar);
			
								// Establece un edittext de entrada para recoger la velocidad
								final EditText input1 = new EditText(this);
								
								input1.setInputType(InputType.TYPE_CLASS_PHONE);
								
								alert1.setView(input1);
			                    
								
								alert1.setPositiveButton(contexto.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
								
								 
								

								
								

								public String radio;

								public void onClick(DialogInterface dialog, int whichButton) {
							     radio = input1.getText().toString();
								  // Do something with value!
								  
							      if (Utiles.validarradio(radio)) {
								
								//#750#radius (5 digits)#sampling interval (minutes)#password # #
								
								  
								//mensaje=Utiles.decodificar("#750#" + radio +  "#1440##clave##");
								  
								  
								
							    //new Utiles().confirmarenvio(contexto, "Autorizar teléfono", "\n Mensaje SMS a enviar:\n" + mensaje +  contexto.getResources().getString(R.string.continuar_pregunta), mensaje, R.drawable.ic_llamar);
								
								
								
								
								
													AlertDialog.Builder alert1 = new AlertDialog.Builder(contexto);
													
													alert1.setTitle(contexto.getResources().getString(R.string.datorequerido));
													alert1.setMessage(contexto.getResources().getString(R.string.unavezelvehiculoestefueradelarea));
													alert1.setIcon(R.drawable.ic_llamar);
								
													// Establece un edittext de entrada para recoger la velocidad
													final EditText input1 = new EditText(contexto);
													
													input1.setInputType(InputType.TYPE_CLASS_PHONE);
													
													alert1.setView(input1);
									                
													
													
													alert1.setPositiveButton(contexto.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
														
														 
														

														
														

														public String minutos;

														public void onClick(DialogInterface dialog, int whichButton) {
														  minutos = input1.getText().toString();
														  
														  String mensaje="";
														
							                            
														mensaje=Utiles.decodificar("#750#" + radio +  "#"+minutos+"##clave##");  
														
														
														
														if (Utiles.validarminutos(minutos))
														new Utiles().confirmarenvio(contexto, contexto.getResources().getString(R.string.activar_detectar_movimiento), contexto.getResources().getString(R.string.mensajesmsaenviar) + mensaje +  contexto.getResources().getString(R.string.continuar_pregunta), mensaje, R.drawable.ic_llamar);
														else new Utiles().dialogocontinuar(contexto, contexto.getResources().getString(R.string.valorincorrecto) , contexto.getResources().getString(R.string.elnumerodeminutosdebeestar));
														  
														}
														  
														});
									
														alert1.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
														  public void onClick(DialogInterface dialog, int whichButton) {
														    // Canceled.
															
														  }
														}
							      
														);
							      					
														alert1.show();	
							      
								//fin if validarradio
								} else new Utiles().dialogocontinuar(contexto, "Valor incorrecto", "El radio debe estar comprendido entre 10 y 100000"); 
								
								
								
								}
								  
								});
			
								alert1.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
								  public void onClick(DialogInterface dialog, int whichButton) {
								    // Canceled.
									
								  }
								});
			
								alert1.show();
			                   
								//
						  }	  else new Utiles().confirmarirconfiguracion(contexto);

					
					
					}
				break;
				case R.id.itemdesactivardetectarmovimiento:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_DETECCIONMOVIMIENTO_OFF));
                    if (modelolocalizador.contentEquals("TK-110")) comandosms=Utiles.decodificar("#760##clave##"); 					
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.desactivar_detectar_movimiento), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms , comandosms,R.drawable.ic_detectarmovimiento);
					else utiles.confirmarirconfiguracion(this);
				break;
				case R.id.itemactivarautorastreo:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_AUTORASTREO_ON));
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.Activar_autorastreo), contexto.getResources().getString(R.string.atencionellocalizadorespondera) + " " + Main.leerint(Main.KEY_VECESAUTORASTREO).toString()  +  " SMS" + contexto.getResources().getString(R.string.mensajesmsaenviar) + " " + comandosms, comandosms,R.drawable.ic_autorastreo);
					else utiles.confirmarirconfiguracion(this);
				break;
				case R.id.itemdesactivarautorastreo:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_AUTORASTREO_OFF));
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.desactivar_autorastreo), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,R.drawable.ic_autorastreo);
					else utiles.confirmarirconfiguracion(this);
				break;
				case R.id.itemactivardetectarvelocidad:
					if (Utiles.validartelefono(Main.leer(Main.KEY_NUMTELLOCALIZADOR))){
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_EXCESOVELOCIDAD_ON));
					
					AlertDialog.Builder alert_velocidad = new AlertDialog.Builder(this);

					alert_velocidad.setTitle(contexto.getResources().getString(R.string.datorequerido));
					alert_velocidad.setMessage(contexto.getResources().getString(R.string.introduzcalavelocidad));
					alert_velocidad.setIcon(R.drawable.ic_excesovelocidad);

					// Establece un edittext de entrada para recoger la velocidad
					final EditText inputautorizar = new EditText(this);
					inputautorizar.setInputType(InputType.TYPE_CLASS_NUMBER);
					alert_velocidad.setView(inputautorizar);
                    
					alert_velocidad.setPositiveButton(contexto.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					
					boolean continuar=true;

					public void onClick(DialogInterface dialog, int whichButton) {
					  String valor = inputautorizar.getText().toString();
					  
					  
					  if (continuar)
					  
					  	  if (valor.trim().length()>0 && valor.trim().length()<=3){
					  		  
					  		  if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) new Utiles().confirmarenvio(contexto, "Activar detectar exceso de velocidad", "\n Mensaje SMS a enviar:\n" + Utiles.decodificar(Main.leer(Main.KEY_SMS_EXCESOVELOCIDAD_ON)+valor) +  contexto.getResources().getString(R.string.continuar_pregunta), Utiles.decodificar(Main.leer(Main.KEY_SMS_EXCESOVELOCIDAD_ON+" ")+valor), R.drawable.ic_excesovelocidad);
					  	  }
						  else Toast.makeText(contexto, contexto.getResources().getString(R.string.datoincorrecto), Toast.LENGTH_LONG).show();
					  }
					});

					alert_velocidad.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
					    // Canceled.
						
					  }
					});

					alert_velocidad.show();
					} else utiles.confirmarirconfiguracion(contexto);
					
				break;
			
			  
				
				case R.id.itemdesactivardetectarvelocidad:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_EXCESOVELOCIDAD_OFF));
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.desactivarlimitarvelocidad), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms , comandosms,R.drawable.ic_excesovelocidad);
					else utiles.confirmarirconfiguracion(this);
				break;
				
				case R.id.itemhistorialentrada:
				
		 	         
         	 	      
		 	      AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 	      builder.setTitle(contexto.getResources().getString(R.string.historialrecepcion));
		 	       
		 	        
		 	       
		 	    
		 	       final Cursor cursortabla=basedatos.consultar();
		 	    
				
				
			   // CODIGO EN PRUEBAS: PRETENDO, QUE EL HISTORIAL MUESTRE MAS INFORMACION
			   MiSimpleCursorAdapter adaptador = new MiSimpleCursorAdapter(contexto,R.layout.celdalista, cursortabla,
				        new String[] {"nombre","hora"}, new int[] { R.id.text1, R.id.text2 });

				
				
				
				builder.setAdapter(adaptador, new android.content.DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Apéndice de método generado automáticamente
						String fechahora=basedatos.consultarhorasms(which, cursortabla);	
						String mensaje=basedatos.consultartextosms(which, cursortabla);
						String numerosms=basedatos.consultarnumerosms(which, cursortabla);
							
						
						dibujarcondialogo(fechahora, mensaje, numerosms);
				
					}
				});
				 
		 	      
		 	      
		 	        AlertDialog alert = builder.create();
		 	        
		 	        if (cursortabla.getCount()>0) alert.show();
		 	        else Toast.makeText(contexto, contexto.getResources().getString(R.string.nohayeventosregistrados), Toast.LENGTH_LONG).show();

				break;
				
				
				case R.id.itemhistorialenvio:
					
		 	         
       	 	      
		 	        AlertDialog.Builder builderenvio = new AlertDialog.Builder(this);
		 	        builderenvio.setTitle(contexto.getResources().getString(R.string.historialenvio));
		 	       
		 	        
		 	       
		 	    
		 	       final Cursor cursortablaenvio=basedatos.consultarSalida();
		 				     
		 	     
				
				
				
				
				   // CODIGO EN PRUEBAS: PRETENDO, QUE EL HISTORIAL MUESTRE MAS INFORMACION
				MiSimpleCursorAdapter adaptadorenvio = new MiSimpleCursorAdapter(contexto,R.layout.celdalista, cursortablaenvio,
				        new String[] {"nombre","hora"}, new int[] { R.id.text1, R.id.text2 });

				
				
				
				builderenvio.setAdapter(adaptadorenvio, new android.content.DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Apéndice de método generado automáticamente
						String fechahora=basedatos.consultarhorasms(which, cursortablaenvio);	
						String mensaje=basedatos.consultartextosms(which, cursortablaenvio);
						String numerosms=basedatos.consultarnumerosms(which, cursortablaenvio);
							
						
						traducirsmsenviados(fechahora, mensaje, numerosms, modelolocalizador);
				
					}
				});
				
				
				
				
				
				
		 	      
		 	      
		 	        AlertDialog alertenvio = builderenvio.create();
		 	        
		 	        if (cursortablaenvio.getCount()>0) alertenvio.show();
		 	        else Toast.makeText(contexto, contexto.getResources().getString(R.string.nohayeventosregistrados), Toast.LENGTH_LONG).show();

				break;

				
				case R.id.itemmodovozon:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_MODOVOZ_ON));
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.activarmodoescucha), contexto.getResources().getString(R.string.atencionrecuerdeactivarmodolocalizar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,R.drawable.ic_detectarmovimiento);
					else utiles.confirmarirconfiguracion(this);
				break;
				
				case R.id.itemmodovozoff:
					comandosms=Utiles.decodificar(Main.leer(Main.KEY_SMS_MODOVOZ_OFF));
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(this, contexto.getResources().getString(R.string.activarmodolocalizar), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,R.drawable.ic_detectarmovimiento);
					else utiles.confirmarirconfiguracion(this);
				break;
				
				
case R.id.itemactivarlimitararea:
				    
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))){
					
					CameraPosition posicioncamara = new CameraPosition.Builder()
				    .target(mapa.getCameraPosition().target)      
				    .bearing(0)             
				    .zoom(mapa.getCameraPosition().zoom)
				    .tilt(mapa.getCameraPosition().tilt)
				    .build(); 
			    	
					
					mapa.animateCamera(CameraUpdateFactory.newCameraPosition(posicioncamara));
				
					
					
					builder = new AlertDialog.Builder(contexto);
		 	    	 
		 	 	    String titulo=contexto.getResources().getString(R.string.ayuda);
					builder.setTitle(titulo);
		 	 	    
					if (modelolocalizador.contentEquals("TK-102") || modelolocalizador.contentEquals("TK-103"))
					builder.setMessage(contexto.getResources().getString(R.string.toqueenlapantallasuperiorizquierda));
					if (modelolocalizador.contentEquals("TK-118") || modelolocalizador.contentEquals("TK-110"))
					builder.setMessage(contexto.getResources().getString(R.string.toqueenlapantallacirculo));
		 	 	    
		 	 	    
					android.content.DialogInterface.OnClickListener listenerbotoncontinuar =new android.content.DialogInterface.OnClickListener(){

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Apéndice de método generado automáticamente
							
							//mapa.getOverlays().clear();
							//mapa.clear();
							//LimiteOverlay limiteoverlay;
							//LimiteOverlayCirculo limiteoverlaycirculo;
								if (modelolocalizador.contentEquals("TK-102") || modelolocalizador.contentEquals("TK-103"))
								{//limiteoverlay=new LimiteOverlay();
								
								//limiteoverlay.setEditMode(true);
								
								
					
								
								final OnMapClickListener listenersegundo=new OnMapClickListener(){

									
								
							

									private int REDONDEO=6;

									public void onMapClick(LatLng latitudlongitud) {
										// TODO Auto-generated method stub
										mapa.clear();
										
										
										latinf=latitudlongitud.latitude;
										longinf=latitudlongitud.longitude;
										
										PolygonOptions options=new PolygonOptions();
										options.add(latitudlongitud);
										
										options.add(new LatLng(latsup,longsup));
										
										options.add(new LatLng(latsup,longinf));
										
										options.add(new LatLng(latinf,longinf));
																				
										options.add(new LatLng(latinf,longsup));
										
										options.add(new LatLng(latsup,longsup));
								
										
										
										
										
									
										
										Polygon poligono=mapa.addPolygon(options);
										//poligono.setFillColor(Color.BLUE); // Relleno del polígono
										poligono.setStrokeColor(Color.RED); // Bordes del polígono
										
										String stringlatitudp1=Double.toString(Utiles.redondear(latsup,REDONDEO));
										String stringlongitudp1=Double.toString(Utiles.redondear(longsup,REDONDEO));
										String stringlatitudp2=Double.toString(Utiles.redondear(latinf,REDONDEO));
										String stringlongitudp2=Double.toString(Utiles.redondear(longinf,REDONDEO));
										
										String comandodesms = Utiles.decodificar("stockade#clave "+stringlatitudp1+","+stringlongitudp1+";"+stringlatitudp2+","+stringlongitudp2);
										
										if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(contexto, contexto.getResources().getString(R.string.limitararea), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandodesms +  contexto.getResources().getString(R.string.continuar_pregunta), comandodesms,R.drawable.ic_limitararea);
										else utiles.confirmarirconfiguracion(contexto);
										mapa.setOnMapClickListener(null);
									}};
								
																
								OnMapClickListener listenerprimero=new OnMapClickListener(){

								
								
								

									public void onMapClick(LatLng latitudlongitud) {
										// TODO Auto-generated method stub
										latsup=latitudlongitud.latitude;
										longsup=latitudlongitud.longitude;
										
										Builder builder2 = new AlertDialog.Builder(contexto);
							 	    	 
							 	 	    String titulo="Ayuda";
										builder2.setTitle(titulo);
							 	 	    										
										builder2.setMessage(contexto.getResources().getString(R.string.toqueenlapantallainferiorderecha));
										
										builder2.setPositiveButton(contexto.getResources().getString(R.string.Continuar), null);
										
										builder2.show();
										
										mapa.setOnMapClickListener(listenersegundo);
										
										
									}};
								
								mapa.setOnMapClickListener(listenerprimero);
								
								//mapa.addGroundOverlay(options);
								//mapa.getOverlays().add(limiteoverlay);
								}
								
								if (modelolocalizador.contentEquals("TK-108") || modelolocalizador.contentEquals("TK-110"))
								{//limiteoverlaycirculo=new LimiteOverlayCirculo();
								//limiteoverlaycirculo=new LimiteOverlayCirculo();
								//limiteoverlaycirculo.setEditMode(true);
								
								
								//limiteoverlay=new LimiteOverlay();
								
								//limiteoverlay.setEditMode(true);
								
	                            
								final OnMapClickListener listenersegundo=new OnMapClickListener(){

									
									private int REDONDEO=6;
									private CircleOptions options;

									public void onMapClick(LatLng latitudlongitud) {
										// TODO Auto-generated method stub
										mapa.clear();
										
										
										latinf=latitudlongitud.latitude;
										longinf=latitudlongitud.longitude;
										
										options=new CircleOptions();
										
										
										    
								       options.center(coordenadascentro);
										
										long radio=(long)Math.floor(Utiles.CalcularDistancia(latsup,longsup, latinf,longinf));
										options.radius(radio);
																			
									
										
										
										mapa.addCircle(options).setStrokeColor(Color.RED);
										//poligono.setFillColor(Color.BLUE); // Relleno del polígono
										
										String signonortesur;
										String signoesteoeste;
										if (latsup>=0) signonortesur="N"; else signonortesur="S";
										if (longsup>=0) signoesteoeste="E"; else signoesteoeste="W";
										
										if (latsup<0)latsup=latsup*-1;
										if (longsup<0)longsup=longsup*-1;
										
										
										
										String stringlatitudp1=Double.toString(Utiles.redondear(latsup,REDONDEO));
										String stringlongitudp1=Double.toString(Utiles.redondear(longsup,REDONDEO));
										
										
										//Enviar SMS: # 751 # fence radius # sampling interval # longitude # latitude # user password ##.
										
										
										String comandodesms = Utiles.decodificar("#751#"+radio+"#1#"+stringlatitudp1+signonortesur+"#"+stringlongitudp1+signoesteoeste+"#"+"#clave"+"##");
										
										if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) utiles.confirmarenvio(contexto, contexto.getResources().getString(R.string.limitararea), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandodesms +  contexto.getResources().getString(R.string.continuar_pregunta), comandodesms,R.drawable.ic_limitararea);
										else utiles.confirmarirconfiguracion(contexto);
										mapa.setOnMapClickListener(null);
									}};
								
																
								OnMapClickListener listenerprimero=new OnMapClickListener(){

								
								
								

						

									public void onMapClick(LatLng latitudlongitud) {
										// TODO Auto-generated method stub
										latsup=latitudlongitud.latitude;
										longsup=latitudlongitud.longitude;
										
										coordenadascentro=latitudlongitud;
										
										Builder builder2 = new AlertDialog.Builder(contexto);
							 	    	 
							 	 	    String titulo="Ayuda";
										builder2.setTitle(titulo);
							 	 	    										
										builder2.setMessage(contexto.getResources().getString(R.string.toqueenlapantallacirculo2));
										
										builder2.setPositiveButton(contexto.getResources().getString(R.string.Continuar), null);
										
										builder2.show();
										
										mapa.setOnMapClickListener(listenersegundo);
										
										
									}};
								
								mapa.setOnMapClickListener(listenerprimero);

								
								
								
								
								
								
								//mapa.getOverlays().add(limiteoverlaycirculo);
								}
			                
										
						}};
		 	 	  android.content.DialogInterface.OnClickListener listenerbotoncancelar = null;
					builder.setPositiveButton(contexto.getResources().getString(R.string.Continuar), listenerbotoncontinuar);
					builder.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), listenerbotoncancelar);
		 	 	    builder.show();
		 	    	 
					
					
					
                
                
                
                 
                
				} else new Utiles().confirmarirconfiguracion(contexto);
                
                
                break;
				

				case R.id.itemdesactivarlimitararea:
					
					comandosms="";
					comandosms=Utiles.decodificar("nostockade#clave");
					
					if (modelolocalizador.contentEquals("TK-110")) comandosms=Utiles.decodificar("#760##clave##");
					
					if (Utiles.validartelefono(preferencias.getString(KEY_NUMTELLOCALIZADOR, ""))) 
				    
					utiles.confirmarenvio(this, contexto.getResources().getString(R.string.desactivarlimitar), contexto.getResources().getString(R.string.mensajesmsaenviar) + comandosms +  contexto.getResources().getString(R.string.continuar_pregunta), comandosms,R.drawable.ic_limitararea);
					
					
					else utiles.confirmarirconfiguracion(this);
					
				break;	
				
				
				case R.id.itemacercade:
					Intent intentacercade=new Intent(contexto,AcercadeActivity.class);
				    startActivity(intentacercade);
			        
			}
			return super.onOptionsItemSelected(item);
			}

	
     	
	

    
    
    
    
	
	
	
   public void dibujarcondialogo(String phora, String pmensajesms, String pnumerosms){
	   final String hora=phora;
	   final String mensajesms=pmensajesms;
	   final String numerosms=pnumerosms;
		//dibujardesms(basedatos.consultarhorasms(which, cursortabla),basedatos.consultartextosms(which, cursortabla));				
		traduccion=Utiles.traducirentrada(pmensajesms, contexto);
		
		
		
	   
	   try {
           
        	
        
        
           
      	String titulo=contexto.getResources().getString(R.string.detalledeleventosmsrecibido);
			Builder builder = new AlertDialog.Builder(contexto);
			

						try {
							alertdialogrecibidosmsloc.cancel();           						
						}
						catch (Exception e)
						{
							Log.w("Cancelando AlertDialog anterior","No hay AlertDialog que cancelar");
						}

			
			
		    
		    
		    
		    
		    android.content.DialogInterface.OnClickListener listenerbotonmostrar=new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Apéndice de método generado automáticamente
					//Toast.makeText(contexto, "Posición del localizador", Toast.LENGTH_SHORT).show();
					
					MarkerOptions markeroptionssms=dibujardesms(hora,mensajesms, numerosms);
					
					listmarkeroptionssms.add(markeroptionssms);
					
					
							
					//try{
					//SMSReceiver.mNotificationManager.cancel(mensajesms,ServicioRecepcion.NOTIFICAR_ID);
					//}
					//catch (Exception e){
						
					//}
				}};
			
			String textobotonpositivo=contexto.getResources().getString(R.string.mostrarlocalizacion);
			String direccion = "";
		    direccion=Utiles.obtenerdirecciondesms(contexto, mensajesms,numerosms);
		    String nombreloc=basedatos.consultarnombre(basedatos.obtenernumero(numerosms));
		    String submodeloloc=basedatos.consultarsubmodelo(basedatos.obtenernumero(numerosms));
 	 	   
		    builder.setIcon(geticonosubmodelo(submodeloloc, contexto));
	 	     
 	 	    
 	 	    
 	 	    
 	 	    
 	 	    
			String textomensajedialogo=contexto.getResources().getString(R.string.recibidode) + nombreloc + " "+ numerosms + contexto.getResources().getString(R.string.fechahoraderecepcion)+ hora + "\n" + direccion + "\n"+  traduccion + contexto.getResources().getString(R.string.textodelsms) + mensajesms;
			        
			        //El siguiente bloque try / catch cambia el texto del boton dependiendo de si es posible extraer 
			        //las coordenadas
					try{
						
					   Basedatos bd=new Basedatos(contexto);	
			 		   String modelo=bd.consultarModelo(numerosms);
			 		   bd.cerrarbd();
			 		   
			 		//***************************************************************************************   
			 		//***************************************************************************************   
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		/*    
			 		   
			 		   if (!modelo.contentEquals("TK-110")){
			 			   latitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLATITUD),contexto);
			        	   longitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLONGITUD),contexto);
		 				   }
		 				   else
		 				   {
		 			                if (validartk110(mensajesms)){ 		  
                   					latitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLATTK110,FINLATTK110), 1,contexto);
		 				        	longitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLONGTK110,FINLONGTK110), 1,contexto);
					                } else throw new ExcepcionFormatoMensaje("Mensaje sms no pertenece a tk110"); 
		 				   }
		 			*/	   
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		//***************************************************************************************
			 		  
			 		   
			 		   //Intenta detectar el modelo
			 		   int modeloasumido=detectarmodelo(mensajesms);
			 		   
			 		   if (modelo.contentEquals("TK-102") && modeloasumido==MODELOTK110) 
			 		   {
			 		    Toast.makeText(contexto, R.string.modeloincorrecto, Toast.LENGTH_LONG).show();
			 		    
			 		   }
			 		   
			 		   if (modelo.contentEquals("TK-110") && modeloasumido==MODELOTK102) 
			 		   {
			 		    Toast.makeText(contexto, R.string.modeloincorrecto, Toast.LENGTH_LONG).show();
			 		   }
			 		   
			 		   //Si no se detecta modelo, el modelo asumido para mensaje es el configurado
			 		   if (modeloasumido==MODELODESCONOCIDO) 
			 		   {  	if (modelo.contentEquals("TK-102")) modeloasumido=MODELOTK102;
			 		   		if (modelo.contentEquals("TK-110")) modeloasumido=MODELOTK110; 
			 		   }
			 		   
			 		   if (modeloasumido!=MODELOTK110){
			 			   latitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLATITUD),contexto);
			        	   longitud = Utiles.ExtraerDouble(mensajesms, Main.leerint(Main.KEY_ORDENLONGITUD),contexto);
		 				   }
		 				   else
		 				   {
		 			                if (validartk110(mensajesms)){ 		  
		 			               	
		 			                latitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLATTK110,FINLATTK110), 1,contexto);
		 				        	longitud = Utiles.ExtraerDouble(mensajesms.substring(INICIOLONGTK110,FINLONGTK110), 1,contexto);
	 			                	
		 			                } else throw new ExcepcionFormatoMensaje("Mensaje sms no pertenece a tk110"); 
		 				   }
			 		  
			 		   
			 		   
			 		   
			 		   
					}
					catch (ExcepcionFormatoMensaje e) {
			 			// TODO Auto-generated catch block
			               //textomensajedialogo=hora + "\n" + traduccion  + "\nTexto del mensaje:\n" + mensajesms;
						   textobotonpositivo=contexto.getResources().getString(R.string.Continuar);
			 		 	   e.printStackTrace();
			 		   } catch (java.lang.NullPointerException e){
			 			  textobotonpositivo=contexto.getResources().getString(R.string.Continuar);  
			 			   e.printStackTrace();  
			 		   } catch (Exception e){
			 			  textobotonpositivo=contexto.getResources().getString(R.string.Continuar);
			 			   e.printStackTrace();
			 		   }
	        
		    if (mensajesms.startsWith("lat: long"))	builder.setPositiveButton(textobotonpositivo, listenerbotonmostrar);
			builder.setTitle(titulo);
		    builder.setMessage(textomensajedialogo);
			builder.setPositiveButton(textobotonpositivo, listenerbotonmostrar);
			
		 
	        android.content.DialogInterface.OnClickListener listenerbotoncancelar=new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					// TODO Apéndice de método generado automáticamente
					
				}};
			if (textobotonpositivo!=contexto.getResources().getString(R.string.Continuar)) builder.setNegativeButton(contexto.getResources().getString(R.string.Cancelar), listenerbotoncancelar); 
			 
			//********************************************
			alertdialogrecibidosmsloc=builder.create();	
				 
			alertdialogrecibidosmsloc.show();        
           }
           catch (Exception e){
           Log.e("Error", e.getMessage());	
           }
			    
			    	

 
   }	
	
   
   
   class MyTimerTask extends TimerTask {
		  public void run() {
		  
		   Main.handle_dibujar_miubicación.sendEmptyMessage(0);
		  
		   
		  }
		 };
	 


		 class MyTimerTaskWebService extends TimerTask {
			  public void run() {
			  
			    String body="<GTSRequest command=\"eventdata\">\r\n" + 
		        		"    <Authorization account=\"vickmatica\" user=\"admin\" password=\"orion\" />\r\n" + 
		        		"    <EventData>\r\n" + 
		        		"        <Device>touran</Device>\r\n" + 
		        		"        <TimeFrom>2014/12/12</TimeFrom>\r\n" + 
		        		"        <TimeTo>2014/12/12</TimeTo>\r\n" + 
		        		"        <GPSRequired>false</GPSRequired>\r\n" + 
		        		"        <Limit type=\"last\">10</Limit>\r\n" + 
		        		"        <Ascending>true</Ascending>\r\n" + 
		        		"        <!-- specific status code(s) -->\r\n" + 
		        		"        <!-- <StatusCode>0xF403</StatusCode> -->\r\n" + 
		        		"        <!-- <StatusCode>0xF112</StatusCode> -->\r\n" + 
		        		"        <!-- fields retrieved -->\r\n" + 
		        		"        <Field name=\"latitude\" />\r\n" + 
		        		"        <Field name=\"longitude\" />\r\n" + 
		        		"   </EventData>\r\n" + 
		        		"</GTSRequest>\r\n" + 
		        		"";
		       
			   body="<GTSRequest command=\"mapdata\">\r\n" + 
				   		"    <Authorization account=\"vickmatica\" user=\"admin\" password=\"orion\"/>\r\n" + 
				   		"    <MapData>\r\n" + 
				   		"        <Device>touran</Device>\r\n" + 
				   		"    </MapData>\r\n" + 
				   		"</GTSRequest>";
							   
			     
			   body= "<GTSRequest command=\"mapdata\">\r\n" + 
			   		"    <Authorization account=\"vickmatica\" user=\"admin\" password=\"orion\"/>\r\n" + 
			   		"    <MapData>\r\n" + 
			   		"        <DeviceGroup>all</DeviceGroup>\r\n" + 
			   		"    </MapData>\r\n" + 
			   		"</GTSRequest>\r\n" + 
			   		"\r\n" + 
			   		"";
			   
			   body= "<GTSRequest command=\"mapdata\">\r\n" + 
				   		"    <Authorization account=\"" + preferencias.getString(Main.KEY_USERINTERNET, "")
				   		+ "\" user=\"admin\" password=\"" + preferencias.getString(Main.KEY_PASSINTERNET, "") + "\"/>\r\n" + 
				   		"    <MapData>\r\n" + 
				   		"        <DeviceGroup>all</DeviceGroup>\r\n" + 
				   		"    </MapData>\r\n" + 
				   		"</GTSRequest>\r\n" + 
				   		"\r\n" + 
				   		"";
				   
				    
				   new HttpPost(contexto).execute(body);
			
			  }
			 };

















		   public void traducirsmsenviados(String phora, String pmensajesms, String pnumero, String modelodellocalizador){
			   final String hora=phora;
			   final String mensajesms=pmensajesms;
			   final String numero=pnumero;
				//dibujardesms(basedatos.consultarhorasms(which, cursortabla),basedatos.consultartextosms(which, cursortabla));				
				String traduccion="";
				
			        /*
					if (pmensajesms.startsWith("begin")) traduccion="Reiniciar a configuración de fábrica";
					if (pmensajesms.startsWith("admin")) traduccion="Autorizado número de teléfono de control";
					if (pmensajesms.startsWith("noadmin")) traduccion="Desautorizado número de teléfono de control";
					if (pmensajesms.startsWith("t030s003n")) traduccion="Activar autorrastreo";
					if (pmensajesms.startsWith("monitor")) traduccion="Activar escuchar voz";
					if (pmensajesms.startsWith("tracker")) traduccion="Activar modo localizador";
					if (pmensajesms.startsWith("stockade"))	traduccion="Activar límite de área";
					if (pmensajesms.startsWith("nostockade"))	traduccion="Desactivar límite de área";
					if (pmensajesms.startsWith("speed"))	traduccion="Activar límite de velocidad";
					if (pmensajesms.startsWith("nospeed"))	traduccion="Desactivar límite de velocidad";
					if (pmensajesms.startsWith("move"))	traduccion="Activar detectar movimiento";
					if (pmensajesms.startsWith("nomove"))	traduccion="Desactivar detectar movimiento";
					if (pmensajesms.contains("help me!"))	traduccion="Detener recepciones de SOS";
					
					//Traducciones de localizador TK110
					if (pmensajesms.startsWith("#711#"))	traduccion="Autorizar número de telefono";
					if (pmensajesms.startsWith("#751#"))	traduccion="Activar limitar area";
					if (pmensajesms.startsWith("#720#"))	traduccion="Ajuste de alarma";
					if (pmensajesms.startsWith("#750#"))	traduccion="Activar detectar movimiento";
					if (pmensajesms.startsWith("#751#"))	traduccion="Activar limitar area";
					if (pmensajesms.startsWith("#760#"))	traduccion="Desactivar detectar movimiento";
					*/ 
				if (pmensajesms.startsWith("begin")) traduccion="\n" + contexto.getResources().getString(R.string.resetfabrica) + "\n";
				if (pmensajesms.startsWith("admin")) traduccion="\n" + contexto.getResources().getString(R.string.autorizartelefono) + "\n";
				if (pmensajesms.startsWith("noadmin")) traduccion="\n" + contexto.getResources().getString(R.string.desautorizartelefono) + "\n";
				if (pmensajesms.startsWith("t030s003n")) traduccion="\n" + contexto.getResources().getString(R.string.Activar_autorastreo) + "\n";
				if (pmensajesms.startsWith("monitor")) traduccion="\n" + contexto.getResources().getString(R.string.activarmodoescucha) + "\n";
				if (pmensajesms.startsWith("tracker")) traduccion="\n" + contexto.getResources().getString(R.string.activarmodolocalizar) + "\n";
				if (pmensajesms.startsWith("stockade"))	traduccion="\n" + contexto.getResources().getString(R.string.activarlimitar) + "\n";
				if (pmensajesms.startsWith("nostockade"))	traduccion="\n" + contexto.getResources().getString(R.string.desactivarlimitar) + "\n";
				if (pmensajesms.startsWith("speed"))	traduccion="\n" + contexto.getResources().getString(R.string.activarexcesovelocidad) + "\n";
				if (pmensajesms.startsWith("nospeed"))	traduccion="\n" + contexto.getResources().getString(R.string.desactivarexcesovelocidad) + "\n";
				if (pmensajesms.startsWith("move"))	traduccion="\n" + contexto.getResources().getString(R.string.activardeteccionmovimiento) + "\n";
				if (pmensajesms.startsWith("nomove"))	traduccion="\n" + contexto.getResources().getString(R.string.desactivardeteccionmovimiento);
				if (pmensajesms.contains("help me!"))	traduccion="\n" + contexto.getResources().getString(R.string.detenersos) + "\n";
				
				//Traducciones de localizador TK110
				if (pmensajesms.startsWith("#711#"))	traduccion="\n" + contexto.getResources().getString(R.string.autorizartelefono) + "\n";
				if (pmensajesms.startsWith("#751#"))	traduccion="\n" + contexto.getResources().getString(R.string.activarlimitar) + "\n";
				if (pmensajesms.startsWith("#720#"))	traduccion="\n" + contexto.getResources().getString(R.string.ajustedealarma) + "\n";
				if (pmensajesms.startsWith("#750#"))	traduccion="\n" + contexto.getResources().getString(R.string.desactivardeteccionmovimiento) + "\n";
				if (pmensajesms.startsWith("#751#"))	traduccion="\n" + contexto.getResources().getString(R.string.activarlimitar) + "\n";
				if (pmensajesms.startsWith("#760#"))	traduccion="\n" + contexto.getResources().getString(R.string.desactivardeteccionmovimiento) + "\n";
				
						
					 
					
				
				
			   
			   try {
		           
		        	
		           
		           
		      	String titulo=contexto.getResources().getString(R.string.detalledelsmsenviado);
					Builder builder = new AlertDialog.Builder(contexto);
					

								try {
									alertdialogrecibidosmsloc.cancel();           						
								}
								catch (Exception e)
								{
									Log.w("Cancelando AlertDialog anterior","No hay AlertDialog que cancelar");
								}

					
					
				    
				    
				    
				    
						
					
					String direccion = "";
				    
					String textomensajedialogo=contexto.getResources().getString(R.string.num_telefono) + ": " + numero +contexto.getResources().getString(R.string.fechahoradeenvio)+ hora + "\n" + direccion + "\n"+  traduccion + contexto.getResources().getString(R.string.textodelsms) + mensajesms;
					        
				    				
					builder.setTitle(titulo);
				    builder.setMessage(textomensajedialogo);
				    String modeloloc=basedatos.consultarsubmodelo(basedatos.obtenernumero(numero));
		 	 	    //if (modeloloc.contains("TK-102")) builder.setIcon(R.drawable.ic_modelotk102);   
		 	 	    //if (modeloloc.contains("TK-110")) builder.setIcon(R.drawable.imagen_tk110);
		 	 	    //if (modeloloc.contains("TK-103")) builder.setIcon(R.drawable.imagen_tk103);
		 	 	    
		 	 	    builder.setIcon(geticonosubmodelo(modeloloc,contexto));
				 
			        android.content.DialogInterface.OnClickListener listenerbotoncancelar=new android.content.DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							// TODO Apéndice de método generado automáticamente
							
						}};
					builder.setPositiveButton(contexto.getResources().getString(R.string.Continuar), listenerbotoncancelar);
						
						
					//********************************************
					alertdialogrecibidosmsloc=builder.create();	
						 
					alertdialogrecibidosmsloc.show();        
		           }
		           catch (Exception e){
		           Log.e("Error", e.getMessage());	
		           }
					    
 
		   }

           
		   
		    public static int detectarmodelo(String mensajesms){
		        int modelo=MODELODESCONOCIDO;
		        //ejemplo: tk110
		        //String ejemplotk110="356823033383113 2013/02/28 04:57:46 Lat:+38.284008 Lon:-000.717763 Speed:1.30KM/H http://maps.google.com/maps?q=+38.35632,-000.47422";
		        //String ejemplotk102="lat: 38.356581 long: -000.475586 speed: 000.0 09/06/13 00:14   bat:F signal:L  imei:358948017189224";
		        boolean a,b,c,d,e,f,g,h,i;
		        
		        //DEL TK110
		        a=mensajesms.contains("Lat:");
		        b=mensajesms.contains("Lon:");
		        c=mensajesms.contains("Speed:");
		        
		        
		        //DEL TK102
		        d=mensajesms.startsWith("lat:");
		        e=mensajesms.contains("long:");
		        f=mensajesms.contains("speed:");
		        g=mensajesms.contains("bat:");
		        h=mensajesms.contains("signal:");
		        i=mensajesms.contains("imei:");
		        
		        if (a && b && c && !d) modelo=MODELOTK110; 
		        
		        if (d && e && f && g && h && i) modelo=MODELOTK102;
		        
		        return modelo;
		        }

		    
		    public static void applyRotation( 
					View container, 
					float start, 
					float end,
					Animation.AnimationListener animationListener ) {
				// Find the center of the container
		
				final float centerX = container.getWidth()/2.0f;
		
				final float centerY = container.getHeight()/2.0f;

				// Create a new 3D rotation with the supplied parameter
				// The animation listener is used to trigger the next animation
				final RotateAnimation rotation =
						new RotateAnimation(start, end, centerX, centerY);
				
				
				rotation.setDuration(500);
				rotation.setFillAfter(true);
				rotation.setInterpolator(new AccelerateInterpolator());
				if( animationListener != null )
					rotation.setAnimationListener( animationListener );

				container.startAnimation(rotation);
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