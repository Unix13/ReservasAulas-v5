/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;

/**
 * @author vivas
 *
 */
public class Profesores implements IProfesores {

	private List<Profesor> coleccionProfesores;

	public Profesores() {

		coleccionProfesores = new ArrayList<>();
	}

	public Profesores(IProfesores profesores) {

		setProfesores(profesores);
	}
	
	@Override
	public void comenzar() {

	}

	@Override
	public void terminar() {

	}

	private void setProfesores(IProfesores profesores) {

		if (profesores == null) {
			throw new NullPointerException("ERROR: No se pueden copiar profesores nulos.");
		}
		coleccionProfesores = copiaProfundaProfesores(profesores.getProfesores());
	}

	@Override
	public List<Profesor> getProfesores() {

		List<Profesor> profesoresOrdenados = copiaProfundaProfesores(coleccionProfesores);
		profesoresOrdenados.sort(Comparator.comparing(Profesor::getCorreo));
		return profesoresOrdenados;
	}

	private List<Profesor> copiaProfundaProfesores(List<Profesor> coleccionProfesores) {

		List<Profesor> otrosProfesores = new ArrayList<>();

		for (Iterator<Profesor> it = coleccionProfesores.iterator(); it.hasNext();) {

			Profesor profesor = it.next();
			otrosProfesores.add(new Profesor(profesor));

		}

		return otrosProfesores;

	}

	@Override
	public int getNumProfesores() {

		return coleccionProfesores.size();
	}

	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {

		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede insertar un profesor nulo.");
		}

		if (!coleccionProfesores.contains(profesor)) {

			coleccionProfesores.add(new Profesor(profesor));
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe un profesor con ese correo.");
		}

	}

	@Override
	public Profesor buscar(Profesor profesor) {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede buscar un profesor nulo.");
		}
		int indice = coleccionProfesores.indexOf(profesor);

		if (indice == -1) {
			return null;
		} else {
			return new Profesor(coleccionProfesores.get(indice));
		}
	}

	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {

		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede borrar un profesor nulo.");
		}

		if (coleccionProfesores.contains(profesor)) {

			coleccionProfesores.remove(profesor);

		} else {
			throw new OperationNotSupportedException("ERROR: No existe ningún profesor con ese correo.");
		}
	}

	@Override
	public List<String> representar() {

		List<String> representacion = new ArrayList<>();

		for (Iterator<Profesor> it = getProfesores().iterator(); it.hasNext();) {

			representacion.add(it.next().toString());
		}
		return representacion;
	}

}