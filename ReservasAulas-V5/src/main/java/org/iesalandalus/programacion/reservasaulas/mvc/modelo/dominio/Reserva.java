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
public class Reserva implements Serializable {

	private Profesor profesor;
	private Aula aula;
	private Permanencia permanencia;

	public Profesor getProfesor() {

		return profesor;
	}

	private void setProfesor(Profesor profesor) {

		if (profesor == null) {
			throw new NullPointerException("ERROR: La reserva debe estar a nombre de un profesor.");
		}
		this.profesor = new Profesor(profesor);
	}

	public Aula getAula() {

		return aula;
	}

	private void setAula(Aula aula) {

		if (aula == null) {
			throw new NullPointerException("ERROR: La reserva debe ser para un aula concreta.");
		}
		this.aula = new Aula(aula);
	}

	public Permanencia getPermanencia() {

		return permanencia;
	}

	private void setPermanencia(Permanencia permanencia) {

		if (permanencia == null) {
			throw new NullPointerException("ERROR: La reserva se debe hacer para una permanencia concreta.");
		}

		if (permanencia instanceof PermanenciaPorTramo) {
			this.permanencia = new PermanenciaPorTramo((PermanenciaPorTramo) permanencia);
			
		} else if (permanencia instanceof PermanenciaPorHora) {
			this.permanencia = new PermanenciaPorHora((PermanenciaPorHora) permanencia);
		}
	}

	public Reserva(Profesor profesor, Aula aula, Permanencia permanencia) {

		setProfesor(profesor);
		setAula(aula);
		setPermanencia(permanencia);
	}

	public Reserva(Reserva reserva) {

		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede copiar una reserva nula.");
		}
		setProfesor(reserva.getProfesor());
		setAula(reserva.getAula());
		setPermanencia(reserva.getPermanencia());
	}

	public static Reserva getReservaFicticia(Aula aula, Permanencia permanencia) {
		
		return new Reserva(Profesor.getProfesorFicticio("Peter@gmail.com"), aula, permanencia);
	}

	
	public float getPuntos() {
		
		return permanencia.getPuntos() + aula.getPuntos();
	}

	
	@Override
	public int hashCode() {
		
		return Objects.hash(aula, permanencia);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof Reserva))
			return false;
		Reserva other = (Reserva) obj;
		return Objects.equals(aula, other.aula) && Objects.equals(permanencia, other.permanencia);
	}

	@Override
	public String toString() {
		
		return String.format("%s, %s, %s, puntos=%.1f", profesor, aula, permanencia, getPuntos());
	}
}