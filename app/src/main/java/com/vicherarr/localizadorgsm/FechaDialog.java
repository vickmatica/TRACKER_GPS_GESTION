package com.vicherarr.localizadorgsm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class FechaDialog extends Dialog {
	  protected Context _context;
	  
	private Button buttonDate;

	  public FechaDialog( Context context,
	                           String  fecha) {
	    super( context );

	    buttonDate = new Button(_context);
	    buttonDate.setText( fecha ); // returns 'mm-dd-yy'

	  } // EditRecordDialog


	// showDatePickerDialog ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void showDatePickerDialog( View view ) {
	  String dateString = "";
	  int    year, month, day;

	  //dateString = buttonDate.getText().toString();
	  month = Integer.valueOf( dateString.substring( 0, 2 ) ) - 1; // month is zero based
	  day   = Integer.valueOf( dateString.substring( 3, 5 ) );
	  year  = Integer.valueOf( "20" + dateString.substring( 6, 8 ) );

	  DatePickerDialog dpd = new DatePickerDialog( _context, dateSetListener, year, month, day );
	  dpd.show();

	} // showDatePickerDialog ----------------------------------------------------


	// OnDateSetListener +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
	  

	public void onDateSet(DatePicker arg0, int year, int month, int day) {
		// TODO Auto-generated method stub
		 buttonDate.setText(  (month+1) + "-" + day + "-" + year );
	}
	}; // OnDateSetListener ------------------------------------------------------
	}