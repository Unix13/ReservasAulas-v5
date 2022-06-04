package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Profesores implements IProfesores {

	private static final String COLECCION = "profesores";

	private MongoCollection<Document> colecccionProfesores;

	@Override
	public void comenzar() {
		colecccionProfesores = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}

	/*
	 * Obtenemos todos las documentos de profesor ordenados con el método sort, con
	 * el criterio de ordenación ascedente y los añadimos a nuestra colección de
	 * profesores.
	 */

	@Override
	public List<Profesor> getProfesores() {
		List<Profesor> profesores = new ArrayList<>();
		for (Document documentoProfesor : colecccionProfesores.find().sort(MongoDB.getCriterioOrdenacionProfesor())) {
			profesores.add(MongoDB.getProfesor(documentoProfesor));
		}
		return profesores;
	}

	/*
	 * Devuelve el recuento de documentos que coinciden con la consulta de nuestra
	 * colección de profesores
	 */

	@Override
	public int getNumProfesores() {
		return (int) colecccionProfesores.countDocuments();
	}

	/*
	 * Insertamos el documento en nuestra colección siempre que no exista con el
	 * método insertOne(), que nos permite insertar un único documento en la
	 * colección
	 * 
	 */

	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("No se puede insertar un profesor nulo.");
		}
		if (buscar(profesor) != null) {
			throw new OperationNotSupportedException("El profesor ya existe.");
		} else {
			colecccionProfesores.insertOne(MongoDB.getDocumento(profesor));
		}
	}

	/*
	 * Buscamos el documento de profesor con el método find(), filtrando por correo,
	 * y quedandonos con el primer documento para que solamente devuelva uno
	 */

	@Override
	public Profesor buscar(Profesor profesor) {
		Document documentoProfesor = colecccionProfesores.find().filter(eq(MongoDB.CORREO, profesor.getCorreo()))
				.first();
		return MongoDB.getProfesor(documentoProfesor);
	}

	/*
	 * Borramos el documento de nuestra colección siempre que exista con el método
	 * deleteOne(), que nos permite eliminar un único documento en la colección
	 * 
	 */

	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("No se puede borrar un profesor nulo.");
		}
		if (buscar(profesor) != null) {
			colecccionProfesores.deleteOne(eq(MongoDB.CORREO, profesor.getCorreo()));
		} else {
			throw new OperationNotSupportedException("El profesor a borrar no existe.");
		}
	}

	@Override
	public List<String> representar() {
		List<String> representacion = new ArrayList<>();

		// Uso el método getProfesores() me devuelve la lista ordenada.
		for (Iterator<Profesor> it = getProfesores().iterator(); it.hasNext();) {

			representacion.add(it.next().toString());
		}
		return representacion;
	}

}
