package com.vicherarr.localizadorgsm;

import java.util.Iterator;

import com.google.android.gms.maps.model.LatLng;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

class MyLocation{  
	   /**  
	    * If GPS is enabled.   
	    * Use minimal connected satellites count.  
	    */  
	   private static final int min_gps_sat_count = 5;  
	   /**  
	    * Iteration step time.  
	    */  
	   private static final int iteration_timeout_step = 500;  
	   LocationResult locationResult;  
	   private Location bestLocation = null;  
	   private Handler handler = new Handler();  
	   private LocationManager myLocationManager;   
	   public Context context;  
	   private boolean gps_enabled = false;  
	   private int counts  = 0;  
	   private int sat_count = 0;  
	   private Runnable showTime = new Runnable() {  
	      public void run() {  
	       boolean stop = false;  
	       counts++;  
	       //si timeout excede de (1 min) para  
	       if(counts > 120){  
	         stop = true;  
	       }  
	       //actualiza la última localización  
	       bestLocation = getLocation(context);  
	       //if localización  no está preparada o no existe intentanlo de nuevo  
	       if(bestLocation == null){  
	         handler.postDelayed(this, iteration_timeout_step);  
	       }else{  
	         //if mejor location es conocida, calcula si necesitamso seguir mirando para una mejor localización  
	         //if gps está habilitado y minimo de satelites no han sido conectados o mínimo contador de chequeo es mas pequeño 4 (2 sec)   
	         if(stop == false && !needToStop()){  
	           handler.postDelayed(this, iteration_timeout_step);  
	         }else{  
	           // removing all updates and listeners  
	           myLocationManager.removeUpdates(gpsLocationListener);  
	           myLocationManager.removeUpdates(networkLocationListener);    
	           myLocationManager.removeGpsStatusListener(gpsStatusListener);  
	           sat_count = 0;  
	           // send best location to locationResult  
	           locationResult.gotLocation(bestLocation);  
	         }  
	       }  
	      }

		  
	   };  
	   /**  
	    * Determina si continuar a intentar encontrar una mejor localización  
	    */  
	   private Boolean needToStop(){  
	     if(gps_enabled){  
	       if(counts <= 4){  
	         return false;  
	       }  
	       if(sat_count < min_gps_sat_count){  
	         //if 20-25 sec and 3 satellites found then stop  
	         if(counts >= 40 && sat_count >= 3){  
	           return true;  
	         }  
	         return false;  
	       }  
	     }  
	     return true;  
	   }  
	   //use for stopping retrieving location   
	   public void StopLoading(){  
	     myLocationManager.removeUpdates(gpsLocationListener);  
	     myLocationManager.removeUpdates(networkLocationListener);    
	     myLocationManager.removeGpsStatusListener(gpsStatusListener);  
	     handler.removeCallbacks(showTime);  
	   }  
	   /**  
	    * Best location abstract result class  
	    */  
	   public static abstract class LocationResult{  
	      public abstract void gotLocation(Location location);  
	    }  
	   /**  
	    * Initialize starting values and starting best location listeners  
	    *   
	    * @param Context ctx  
	    * @param LocationResult result  
	    */  
	   public void init(Context ctx, LocationResult result){  
	     context = ctx;  
	     locationResult = result;  
	     myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
	     gps_enabled = (Boolean) myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
	     bestLocation = null;  
	     counts = 0;  
	     // turning on location updates  
	     myLocationManager.requestLocationUpdates("network", 0, 0, networkLocationListener);  
	     myLocationManager.requestLocationUpdates("gps", 0, 0, gpsLocationListener);  
	     myLocationManager.addGpsStatusListener(gpsStatusListener);  
	     // starting best location finder loop  
	     handler.postDelayed(showTime, iteration_timeout_step);  
	   }  
	   /**  
	    * GpsStatus listener. Cuando cambian los satelites conectados  
	    */  
	   public final GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {  
	     public void onGpsStatusChanged(int event) {  
	        if(event == GpsStatus.GPS_EVENT_SATELLITE_STATUS){  
	         try {  
	           // Check number of satellites in list to determine fix state  
	            GpsStatus status = myLocationManager.getGpsStatus(null);
	            
	            Iterable<GpsSatellite>satellites = status.getSatellites();  
	            sat_count = 0;  
	            Iterator<GpsSatellite>satI = satellites.iterator();  
	            while(satI.hasNext()) {  
	              GpsSatellite satellite = satI.next();  
	              Log.i("GPS Localización","Satellite: snr=" + satellite.getSnr() + ", elevación=" + satellite.getElevation());               
	              sat_count++;  
	            }  
	         } catch (Exception e) {  
	           e.printStackTrace();  
	           sat_count = min_gps_sat_count + 1;  
	         }  
	        }  
	      }  
	   };  
	   /**  
	    * Gps location listener.  
	    */  
	   public final LocationListener gpsLocationListener = new LocationListener(){  
	     public void onLocationChanged(Location location){  
	     }  
	      public void onProviderDisabled(String provider){}  
	      public void onProviderEnabled(String provider){}  
	    
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Apéndice de método generado automáticamente
			
		}  
	   };   
	   /**  
	    * Network location listener.  
	    */  
	   public final LocationListener networkLocationListener = new LocationListener(){  
	     public void onLocationChanged(Location location){  
	     }  
	      public void onProviderDisabled(String provider){}  
	      public void onProviderEnabled(String provider){}  
	      public void onStatusChanged(String provider, int status, Bundle extras){}  
	   };   
	   /**  
	    * Retorna la mejor localización buscando LocationManager.getBestProvider()  
	    *   
	    * @param context  
	    * @return Location|null  
	    */  
	   public static LatLng getGeoPointBuenaPrecision(Context context){  
	     // Retorna punto de última localización conocida  
	     try {  
	       LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
	       Criteria criteria = new Criteria();  
	       criteria.setAccuracy(Criteria.ACCURACY_FINE);  
	        criteria.setAltitudeRequired(false);  
	        criteria.setBearingRequired(false);  
	        criteria.setCostAllowed(true);  
	        String strLocationProvider = lm.getBestProvider(criteria, true);  
	        Location location = lm.getLastKnownLocation(strLocationProvider);
	        
	        if(location != null){  
		    int lat = (int) (location.getLatitude() * 1E6);
	        int lng = (int) (location.getLongitude() * 1E6);
	        LatLng point = new LatLng(lat, lng);
	        return point;  
	        }  
	        
	        return null;  
	     } catch (Exception e) {  
	       e.printStackTrace();  
	       return null;  
	     }  
	   }
	   
	   public static LatLng getGeoPointPocaPrecision(Context context){  
		     // fetch last known location and update it  
		     try {  
		       LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
		       Criteria criteria = new Criteria();  
		       criteria.setAccuracy(Criteria.ACCURACY_COARSE);  
		        criteria.setAltitudeRequired(false);  
		        criteria.setBearingRequired(false);  
		        criteria.setCostAllowed(true);  
		        String strLocationProvider = lm.getBestProvider(criteria, true);  
		        Location location = lm.getLastKnownLocation(strLocationProvider);
		        
		        if(location != null){  
			    int lat = (int) (location.getLatitude() * 1E6);
		        int lng = (int) (location.getLongitude() * 1E6);
		        LatLng point = new LatLng(lat, lng);
		        return point;  
		        }  
		        
		        return null;  
		     } catch (Exception e) {  
		       e.printStackTrace();  
		       return null;  
		     }  
		   }  
	   
	 
	   public static Location getLocation(Context context){  
		     // fetch last known location and update it  
		     try {  
		       LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
		       Criteria criteria = new Criteria();  
		       criteria.setAccuracy(Criteria.ACCURACY_FINE);  
		        criteria.setAltitudeRequired(false);  
		        criteria.setBearingRequired(false);  
		        criteria.setCostAllowed(true);  
		        String strLocationProvider = lm.getBestProvider(criteria, true);  
		        Location location = lm.getLastKnownLocation(strLocationProvider);
		        
		        if(location != null){  
		
		        return location;  
		        }  
		        
		        return null;  
		     } catch (Exception e) {  
		       e.printStackTrace();  
		       return null;  
		     }  
		   }





}