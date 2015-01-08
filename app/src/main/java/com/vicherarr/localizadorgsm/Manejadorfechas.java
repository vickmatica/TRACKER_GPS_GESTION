package com.vicherarr.localizadorgsm;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class Manejadorfechas {
    

    //Metodo usado para obtener la fecha actual
    //@return Retorna un <b>STRING</b> con la fecha actual formato "dd-MM-yyyy"
    @SuppressLint("SimpleDateFormat")
	public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);
    }

    //Metodo usado para obtener la hora actual del sistema
    //@return Retorna un <b>STRING</b> con la hora actual formato "hh:mm:ss"
    @SuppressLint("SimpleDateFormat")
	public static String getHoraActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
        return formateador.format(ahora);
    }

    //Sumarle dias a una fecha determinada
    //@param fch La fecha para sumarle los dias
    //@param dias Numero de dias a agregar
    //@return La fecha agregando los dias
    public static java.sql.Date sumarFechasDias(java.sql.Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    //Restarle dias a una fecha determinada
    //@param fch La fecha
    //@param dias Dias a restar
    //@return La fecha restando los dias
    public static synchronized java.sql.Date restarFechasDias(java.sql.Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, -dias);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    //Diferencias entre dos fechas
    //@param fechaInicial La fecha de inicio
    //@param fechaFinal  La fecha de fin
    //@return Retorna el numero de dias entre dos fechas
    public static synchronized int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);
        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = df.format(fechaFinal);
        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException ex) {
        }

        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    //Devuelve un java.util.Date desde un String en formato dd-MM-yyyy
    //@param La fecha a convertir a formato date
    //@return Retorna la fecha en formato Date
    @SuppressLint("SimpleDateFormat")
	public static synchronized java.util.Date deStringToDate(String fecha) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date fechaEnviar = null;
        try {
            fechaEnviar = formatoDelTexto.parse(fecha);
            return fechaEnviar;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

	public static Calendar StringtoCalendar(String fecha) {
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    try {
			cal.setTime(sdf.parse(fecha));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// all done
		return cal;
	}
	
	
	public static Date DateActual(){
	 
		 
		 
		 Calendar cal = Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         sdf.format(cal.getTime());
		 
		 return deStringToDate(    sdf.format(cal.getTime())    );
	}
}
