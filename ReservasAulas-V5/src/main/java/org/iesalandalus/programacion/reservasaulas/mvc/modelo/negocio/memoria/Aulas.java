/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;


/**
 * @author vivas
 *
 */
public class Aulas implements IAulas {
	
	private List<Aula> coleccionAulas;

	public Aulas() {
		
		coleccionAulas = new ArrayList<>();
	}

	public Aulas(IAulas aulas) {
		setAulas(aulas);
	}
	
	@Override
	public void comenzar() {

	}

	@Override
	public void terminar() {

	}

	private void setAulas(IAulas aulas) {
		
		if (aulas == null) {
			throw new NullPointerException("ERROR: No se pueden copiar aulas nulas.");
		}
		this.coleccionAulas = copiaProfundaAulas(aulas.getAulas());
	}

	

	@Override
	public List<Aula> getAulas() {
		
		List<Aula> aulasOrdenadas = copiaProfundaAulas(coleccionAulas);
		aulasOrdenadas.sort(Comparator.comparing(Aula::getNombre));
		return aulasOrdenadas;
	}

	private List<Aula> copiaProfundaAulas(List<Aula> aulas) {

		List<Aula> otrasAulas = new ArrayList<>();
		
		for (Iterator<Aula> it = aulas.iterator(); it.hasNext();) {

			Aula aula = it.next();
			otrasAulas.add(new Aula(aula));

		}

		return otrasAulas;

	}

	@Override
	public int getNumAulas() {
		
		return coleccionAulas.size();
	}

	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede insertar un aula nula.");
		}
		
		if (!coleccionAulas.contains(aula)) {
			
			coleccionAulas.add(new Aula(aula));
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe un aula con ese nombre.");
		}

	}

	@Override
	public Aula buscar(Aula aula) {
		
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede buscar un aula nula.");
		}
		
		int indice = coleccionAulas.indexOf(aula);
		
		if (indice == -1) {
			return null;
		} else {
			return new Aula(coleccionAulas.get(indice));
		}
	}

	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede borrar un aula nula.");
		}
		
		if (coleccionAulas.contains(aula)) {
			
			coleccionAulas.remove(aula);

		} else {
			throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
		}
	}

	@Override
	public List<String> representar() {
		
		List<String> representacion = new ArrayList<>();
		
		for (Iterator<Aula> it = getAulas().iterator(); it.hasNext();) {

			representacion.add(it.next().toString());
		}
		return representacion;
	}

}