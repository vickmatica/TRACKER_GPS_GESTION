/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * 
 *
 * 
 */
package com.vicherarr.localizadorgsm;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.AsyncTask;
import android.os.StrictMode;

public class IpPublica extends AsyncTask<Void, Integer, String> {

	protected String ippublica;

	@Override
	protected String doInBackground(Void... params) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
			        new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}   
		
		try {                                              
	    	Document doc = Jsoup.connect("http://www.checkip.org").get();
	    	
	    	ippublica = doc.getElementById("yourip").select("h1").first().select("span").text();
	    		    
	    
	    } catch (Exception e) {                          
	        e.printStackTrace();
	        
	        ippublica="Error";
	    }                                                  

		return ippublica;
	}

	
}
