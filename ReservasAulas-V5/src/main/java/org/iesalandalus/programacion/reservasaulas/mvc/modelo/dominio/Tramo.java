/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

/**
 * @author vivas
 *
 */
public enum Tramo {

	MANANA("Mañana"), TARDE("Tarde");

	private String cadenaAMostrar;

	private Tramo(String cadenaAMostrar) {
		
		this.cadenaAMostrar = cadenaAMostrar;
	}

	@Override
	public String toString() {
		
		return cadenaAMostrar;
	}
}
