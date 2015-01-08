package com.vicherarr.localizadorgsm;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MiSimpleCursorAdapter extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;

	
	public MiSimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		
       
		this.c = c;
	    this.context = context;
     		
	
	}

	public View getView(int pos, View inView, ViewGroup parent) {
	       View v = inView;
	       if (v == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = inflater.inflate(R.layout.celdalista, null);
	       }
	       this.c.moveToPosition(pos);      
	       String hora = this.c.getString(this.c.getColumnIndex("hora"));
	       String nombre = this.c.getString(this.c.getColumnIndex("nombre"));
	       String submodeloloc = this.c.getString(this.c.getColumnIndex("submodelo"));
	       
	       ImageView iv = (ImageView) v.findViewById(R.id.fotomodelo);
	       
	       try
	       {
	       iv.setImageDrawable(Main.geticonosubmodelo(submodeloloc, context));
	       }
	       catch (Exception e){}
	       /*
		    if (submodeloloc.contains("TK-101")) iv.setImageResource(R.drawable.ic_tk101);
	    	if (submodeloloc.contains("TK-102")) iv.setImageResource(R.drawable.ic_modelotk102);
	    	if (submodeloloc.contains("TK-102-2")) iv.setImageResource(R.drawable.ic_modelotk102);
	    	if (submodeloloc.contains("TK-103")) iv.setImageResource(R.drawable.imagen_tk103);
	    	if (submodeloloc.contains("TK-103")) iv.setImageResource(R.drawable.ic_tk103);
	    	if (submodeloloc.contains("TK-110")) iv.setImageResource(R.drawable.imagen_tk110);
	    	if (submodeloloc.contains("TK-206")) iv.setImageResource(R.drawable.ic_tk206);
	    	if (submodeloloc.contains("TK-201")) iv.setImageResource(R.drawable.ic_201);
	    	if (submodeloloc.contains("GPS 102")) iv.setImageResource(R.drawable.ic_modelotk102);
	    	if (submodeloloc.contains("GPS 102-B")) iv.setImageResource(R.drawable.ic_modelotk102);
	    	if (submodeloloc.contains("GPS 103-A")) iv.setImageResource(R.drawable.ic_gps103a);
	    	if (submodeloloc.contains("GPS 103-B")) iv.setImageResource(R.drawable.ic_gps103b);
	    	if (submodeloloc.contains("GPS 104")) iv.setImageResource(R.drawable.ic_gps104);
	    	if (submodeloloc.contains("GPS 106-A")) iv.setImageResource(R.drawable.ic_106);
	    	if (submodeloloc.contains("GPS 106-B")) iv.setImageResource(R.drawable.ic_106);
	    	*/
		   
		   
		   
		   
	       	        TextView textviewhora = (TextView) v.findViewById(R.id.text1);
	                textviewhora.setText(hora);

	                TextView textviewnombre = (TextView) v.findViewById(R.id.text2);
	                textviewnombre.setText(nombre);

	                
	            return(v);
	}


}
