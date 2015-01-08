package com.vicherarr.localizadorgsm;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;

public class FechaHoraRangoDialog extends AlertDialog  {

private DatePicker fechainicio,fechafin;
private TimePicker horainicio,horafin;
private Button aceptar, cancelar;

	
	protected FechaHoraRangoDialog(Context contexto) {
		super(contexto);
		
        fechainicio=new DatePicker(contexto);
        fechafin=new DatePicker(contexto);
        horainicio=new TimePicker(contexto);
        horafin=new TimePicker(contexto);
        aceptar=new Button(contexto);
        aceptar.setText("Aceptar");
        
        LinearLayout ll=new LinearLayout(contexto);
        
        ll.setOrientation(LinearLayout.VERTICAL);
        
        ll.addView(fechainicio);
        ll.addView(fechafin);
        ll.addView(horainicio);
        ll.addView(horafin);
        ll.addView(aceptar);
        
        
        this.setView(ll);
        
    
          
	}

	
}
