/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author vivas
 *
 */
public class PermanenciaPorTramo extends Permanencia {

	private static final int PUNTOS = 10;
	private Tramo tramo;

	public PermanenciaPorTramo(LocalDate dia, Tramo tramo) {
		
		super(dia);
		setTramo(tramo);
	}

	public PermanenciaPorTramo(PermanenciaPorTramo permanenciaPorTramo) {
		
		super(permanenciaPorTramo);
		setTramo(permanenciaPorTramo.getTramo());
	}

	public Tramo getTramo() {
		
		return tramo;
	}

	private void setTramo(Tramo tramo) {
		
		if (tramo == null) {
			throw new NullPointerException("ERROR: El tramo de una permanencia no puede ser nulo.");
		}
		this.tramo = tramo;
	}

	@Override
	public int getPuntos() {
		
		return PUNTOS;
	}

	@Override
	public int hashCode() {
		
		return Objects.hash(getDia(), tramo);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof PermanenciaPorTramo))
			return false;
		PermanenciaPorTramo other = (PermanenciaPorTramo) obj;
		return Objects.equals(getDia(), other.getDia()) && tramo == other.tramo;
	}

	@Override
	public String toString() {
		
		return super.toString() + ", tramo=" + getTramo();
	}

}
