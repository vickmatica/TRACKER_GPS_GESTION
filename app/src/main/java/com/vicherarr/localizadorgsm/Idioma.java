package com.vicherarr.localizadorgsm;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;


public class Idioma extends Activity {

RadioButton radioespanol, radioingles;
ImageView imagenespanol, imageningles;
Button botonguardar;

@Override
public void onResume(){
super.onResume();

//Toast.makeText(this, Locale.getDefault().getISO3Language(), Toast.LENGTH_LONG).show();
if (Locale.getDefault().getISO3Language().contentEquals("eng")) 
{
	
	
	 radioingles.setChecked(true);
	 radioespanol.setChecked(false);

}
else 
{
	 radioingles.setChecked(false);
	 radioespanol.setChecked(true);

}


}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Locale.getDefault().getISO3Language().contentEquals("eng"))
		setContentView(R.layout.idiomasprimeroingles);
		else setContentView(R.layout.idiomasprimeroespanol);
		
		setTitle(this.getResources().getString(R.string.app_name));
		
		radioespanol=(RadioButton)findViewById(R.id.radiospanish);
		radioingles=(RadioButton)findViewById(R.id.radioenglish);
		botonguardar=(Button)findViewById(R.id.botonguardaridioma);
		imagenespanol=(ImageView)findViewById(R.id.imagenespanol);
		imageningles=(ImageView)findViewById(R.id.imageningles);
		
		
		if (Locale.getDefault().getISO3Language().contentEquals("eng")) 
		{
			
			
			 radioingles.setChecked(true);
			 radioespanol.setChecked(false);

		}
		else 
		{    
			 radioingles.setChecked(false);
			 radioespanol.setChecked(true);
			 
				  
		}
		
		//Marca por defecto el español
		//radioespanol.setChecked(true);
		
		
		
				botonguardar.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
					
						Main.preferencias = getSharedPreferences(Main.PREFERENCIAS,Context.MODE_PRIVATE);
						
						if (radioespanol.isChecked()) 
						{
						
					        
					        Main.preferencias.edit().putString(Main.KEY_IDIOMA, "espanol").commit();
							
						}
						
						if (radioingles.isChecked()) 
						{
						    
					        Main.preferencias.edit().putString(Main.KEY_IDIOMA, "ingles").commit();
					        
						}
						
						
						finish();
						
					}}
                 );
				
				
				imagenespanol.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
					
				
						radioingles.setChecked(false);
						radioespanol.setChecked(true);
						
					}}
                 );
				
				imageningles.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
				      	
						 radioingles.setChecked(true);
						 radioespanol.setChecked(false);
				
					}}
                 );
				
				radioespanol.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
					    
						radioingles.setChecked(false);
						radioespanol.setChecked(true);
				
						
					}}
                 );
				
				radioingles.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
					
						 radioingles.setChecked(true);
						 radioespanol.setChecked(false);
				
					}}
                 );



		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.idioma, menu);
		return true;
	}

}
