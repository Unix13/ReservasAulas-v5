package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Aulas implements IAulas {

	private static final String COLECCION = "aulas";

	private MongoCollection<Document> coleccionAulas;

	@Override
	public void comenzar() {
		coleccionAulas = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}

	/*
	 * Obtenemos todos las documentos de aula ordenados con el método sort, con el
	 * criterio de ordenación ascedente y los añadimos a nuestra colección de aulas.
	 */

	@Override
	public List<Aula> getAulas() {
		List<Aula> aulas = new ArrayList<>();
		for (Document documentoAula : coleccionAulas.find().sort(MongoDB.getCriterioOrdenacionAula())) {
			aulas.add(MongoDB.getAula(documentoAula));
		}
		return aulas;
	}

	/*
	 * Devuelve el recuento de documentos que coinciden con la consulta de nuestra
	 * colección de aulas
	 */

	@Override
	public int getNumAulas() {
		return (int) coleccionAulas.countDocuments();
	}

	/*
	 * Insertamos el documento en nuestra colección siempre que no exista con el
	 * método insertOne(), que nos permite insertar un único documento en la
	 * colección
	 * 
	 */

	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new IllegalArgumentException("No se puede insertar un aula nula.");
		}
		if (buscar(aula) != null) {
			throw new OperationNotSupportedException("El aula ya existe.");
		} else {
			coleccionAulas.insertOne(MongoDB.getDocumento(aula));
		}
	}

	/*
	 * Buscamos el documento de aula con el método find(), filtrando por nombre, y
	 * quedandonos con el primer documento para que solamente devuelva uno
	 */

	@Override
	public Aula buscar(Aula aula) {
		Document documentoAula = coleccionAulas.find().filter(eq(MongoDB.NOMBRE, aula.getNombre())).first();
		return MongoDB.getAula(documentoAula);
	}

	/*
	 * Borramos el documento de nuestra colección siempre que exista con el método
	 * deleteOne(), que nos permite eliminar un único documento en la colección
	 * 
	 */

	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new IllegalArgumentException("No se puede borrar un aula nula.");
		}
		if (buscar(aula) != null) {
			coleccionAulas.deleteOne(eq(MongoDB.NOMBRE, aula.getNombre()));
		} else {
			throw new OperationNotSupportedException("El aula a borrar no existe.");
		}
	}

	@Override
	public List<String> representar() {
		List<String> representacion = new ArrayList<>();
		// Uso el método getAulas me devuelve la lista ordenada.
		for (Iterator<Aula> it = getAulas().iterator(); it.hasNext();) {

			representacion.add(it.next().toString());
		}
		return representacion;
	}

}
