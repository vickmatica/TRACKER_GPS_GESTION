package com.vicherarr.localizadorgsm;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

class MiGLView extends GLSurfaceView {
   private final MiGLRenderer renderer;
   
   public MiGLView(Context context, AttributeSet attrs){
	   super(context, attrs); 
	   
	   
	   
	      	   
	   setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	   getHolder().setFormat(PixelFormat.TRANSLUCENT);
       
	   renderer = new MiGLRenderer(context);
       setRenderer(renderer);
   }
   
   public MiGLView(Context context) {
       super(context);
       
       setEGLConfigChooser(8, 8, 8, 8, 16, 0);
       getHolder().setFormat(PixelFormat.TRANSLUCENT);
       
       renderer = new MiGLRenderer(context);
       setRenderer(renderer);
   
   
   }
   
  
}


