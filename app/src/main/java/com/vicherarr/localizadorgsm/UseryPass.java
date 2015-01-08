package com.vicherarr.localizadorgsm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UseryPass extends Activity {

	Button botonguardar;
	EditText user, pass;
	private SharedPreferences preferencias;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userypass);
		
		this.setTitle("Usuario Servicio Web");
		
		botonguardar = (Button)findViewById(R.id.btnguardarusuario);
		user=(EditText)findViewById(R.id.txtEmail);
		pass=(EditText)findViewById(R.id.txtPass);
		preferencias = getSharedPreferences(Main.PREFERENCIAS,Context.MODE_PRIVATE);
		
		user.setText(preferencias.getString(Main.KEY_USERINTERNET, ""));
		pass.setText(preferencias.getString(Main.KEY_PASSINTERNET, ""));
		
		
		botonguardar.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				
			    preferencias.edit().putString(Main.KEY_USERINTERNET, user.getText().toString()).commit();
			    preferencias.edit().putString(Main.KEY_PASSINTERNET, pass.getText().toString()).commit();
				finish();
			}});
	}
}
