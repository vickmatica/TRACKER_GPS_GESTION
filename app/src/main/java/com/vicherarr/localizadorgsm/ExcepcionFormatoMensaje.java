/*
 * AUTOR: VICTOR LEON HERRERA ARRIBAS
 * */
package com.vicherarr.localizadorgsm;

public class ExcepcionFormatoMensaje extends Exception {

	
	/**
	 * Excepción que no es posible extraer números del SMS de
	 * texto pasado como parámetro
	 */
	private static final long serialVersionUID = 7019411604618162152L;

	public ExcepcionFormatoMensaje(String msg){
		super(msg);
	}
}
