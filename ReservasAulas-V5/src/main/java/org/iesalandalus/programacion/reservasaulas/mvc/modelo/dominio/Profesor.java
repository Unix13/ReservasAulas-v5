/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author vivas
 *
 */
public class Profesor implements Serializable {

	private static final String ER_TELEFONO = "[96]\\d{8}";
	private static final String ER_CORREO = ".+@[a-zA-Z]+\\.[a-zA-Z]+";

	private String nombre;
	private String correo;
	private String telefono;

	public Profesor(String nombre, String correo) {

		setNombre(nombre);
		setCorreo(correo);
	}

	public Profesor(String nombre, String correo, String telefono) {

		setNombre(nombre);
		setCorreo(correo);
		setTelefono(telefono);
	}

	public Profesor(Profesor profesor) {

		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede copiar un profesor nulo.");
		}
		setNombre(profesor.getNombre());
		setCorreo(profesor.getCorreo());
		setTelefono(profesor.getTelefono());
	}

	public String getNombre() {
		
		return nombre;
	}

	public void setNombre(String nombre) {

		if (nombre == null) {
			throw new NullPointerException("ERROR: El nombre del profesor no puede ser nulo.");
		}
		if (nombre.trim().equals("")) {
			throw new IllegalArgumentException("ERROR: El nombre del profesor no puede estar vacío.");
		}
		this.nombre = formateaNombre(nombre);
		;
	}

	public String getCorreo() {
		
		return correo;
	}

	public void setCorreo(String correo) {

		if (correo == null) {
			throw new NullPointerException("ERROR: El correo del profesor no puede ser nulo.");
		}
		if (correo.isBlank() || !correo.matches(ER_CORREO)) {
			throw new IllegalArgumentException("ERROR: El correo del profesor no es válido.");
		}
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {

		if (telefono != null && !telefono.matches(ER_TELEFONO)) {
			throw new IllegalArgumentException("ERROR: El teléfono del profesor no es válido.");
		}
		this.telefono = telefono;
	}

	private String formateaNombre(String nombreSinFormato) {

		String nombre = nombreSinFormato.trim().replaceAll("\\s{2,}", " ").toLowerCase();
		char cadenaChar[] = new char[nombre.length()];
		cadenaChar = nombre.toCharArray();
		for (int i = 0; i < cadenaChar.length; ++i) {
			if (cadenaChar[i] == ' ') {
				cadenaChar[i + 1] = Character.toUpperCase(cadenaChar[i + 1]);
			}
		}
		cadenaChar[0] = Character.toUpperCase(cadenaChar[0]);
		nombre = String.valueOf(cadenaChar);
		return nombre;
	}

	public static Profesor getProfesorFicticio(String correo) {
		
		return new Profesor("Peter Francis Macdowell", correo);
	}

	@Override
	public int hashCode() {
		
		return Objects.hash(correo);
	}

	@Override

	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof Profesor))
			return false;
		Profesor other = (Profesor) obj;
		return Objects.equals(correo, other.correo);
	}

	@Override
	public String toString() {
		String cadenaTelefono = (telefono == null) ? "" : ", teléfono=" + telefono;
		return "nombre=" + getNombre() + ", correo=" + getCorreo() + cadenaTelefono;
	}

}