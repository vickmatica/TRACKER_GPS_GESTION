package com.vicherarr.localizadorgsm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PresentacionActivity extends Activity {

private static final long SPLASH_DISPLAY_LENGTH = 3000;

@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        MiGLView view = new MiGLView(this);
        setContentView(view);
        
        setContentView(R.layout.presentacion);
        
        new Handler().postDelayed(new Runnable() {
        	public void run() {
        	Intent intent = new Intent(PresentacionActivity.this, Main.class);
        	startActivity(intent);
        	/* Destruye esta actividad*/
        	finish();
        	};
        	}, SPLASH_DISPLAY_LENGTH);
        	}
        
        
}

