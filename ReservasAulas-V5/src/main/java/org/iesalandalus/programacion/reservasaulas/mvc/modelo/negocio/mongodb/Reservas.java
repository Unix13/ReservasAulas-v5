package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.MongoCollection;

public class Reservas implements IReservas {

	private static final float MAX_PUNTOS_PROFESOR_MES = 200.0f;
	private static final String COLECCION = "reservas";

	private MongoCollection<Document> coleccionReservas;

	public Reservas() {

	}

	@Override
	public void comenzar() {
		coleccionReservas = MongoDB.getBD().getCollection(COLECCION);
	}

	@Override
	public void terminar() {
		MongoDB.cerrarConexion();
	}

	/*
	 * Obtenemos todos las documentos de reserva ordenados por criterios de
	 * permanencia
	 */

	@Override
	public List<Reserva> getReservas() {
		List<Reserva> reservas = new ArrayList<>();
		for (Document documentoReserva : coleccionReservas.find()) {
			reservas.add(MongoDB.getReserva(documentoReserva));
		}
		ordenarPorPermanencia(reservas);
		return reservas;
	}

	private void ordenarPorPermanencia(List<Reserva> reservas) {
		Comparator<Aula> comparadorAula = Comparator.comparing(Aula::getNombre);
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo) p1).getTramo().ordinal(),
							((PermanenciaPorTramo) p2).getTramo().ordinal());
				} else if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora) p1).getHora().compareTo(((PermanenciaPorHora) p2).getHora());
				}
			} else {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			return comparacion;
		};
		reservas.sort(Comparator.comparing(Reserva::getAula, comparadorAula).thenComparing(Reserva::getPermanencia,
				comparadorPermanencia));
	}

	/*
	 * Devuelve el recuento de documentos que coinciden con la consulta de nuestra
	 * colección de reservas
	 */

	@Override
	public int getNumReservas() {
		return (int) coleccionReservas.countDocuments();
	}

	/*
	 * Insertamos el documento en nuestra colección método insertOne(), que nos
	 * permite insertar un único documento en la colección
	 * 
	 */

	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("No se puede realizar una reserva nula.");
		}
		Reserva reservaExistente = getReservaAulaDia(reserva.getAula(), reserva.getPermanencia().getDia());
		if (reservaExistente != null) {
			if (reservaExistente.getPermanencia() instanceof PermanenciaPorTramo
					&& reserva.getPermanencia() instanceof PermanenciaPorHora) {
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por tramo para este día y aula.");
			}
			if (reservaExistente.getPermanencia() instanceof PermanenciaPorHora
					&& reserva.getPermanencia() instanceof PermanenciaPorTramo) {
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por hora para este día y aula.");
			}
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException(
					"Sólo se pueden hacer reservas para el mes que viene o posteriores.");
		}
		if (getPuntosGastadosReserva(reserva) > MAX_PUNTOS_PROFESOR_MES) {
			throw new OperationNotSupportedException(
					"Esta reserva excede los puntos máximos por mes para dicho profesor.");
		}
		if (buscar(reserva) != null) {
			throw new OperationNotSupportedException("La reserva ya existe.");
		} else {
			coleccionReservas.insertOne(MongoDB.getDocumento(reserva));
		}
	}

	private boolean esMesSiguienteOPosterior(Reserva reserva) {
		LocalDate diaReserva = reserva.getPermanencia().getDia();
		LocalDate dentroDeUnMes = LocalDate.now().plusMonths(1);
		LocalDate primerDiaMesSiguiente = LocalDate.of(dentroDeUnMes.getYear(), dentroDeUnMes.getMonth(), 1);
		return diaReserva.isAfter(primerDiaMesSiguiente) || diaReserva.equals(primerDiaMesSiguiente);
	}

	private float getPuntosGastadosReserva(Reserva reserva) {
		float puntosGastados = 0;
		for (Reserva reservaProfesor : getReservasProfesorMes(reserva.getProfesor(),
				reserva.getPermanencia().getDia())) {
			puntosGastados += reservaProfesor.getPuntos();
		}
		return puntosGastados + reserva.getPuntos();
	}

	private List<Reserva> getReservasProfesorMes(Profesor profesor, LocalDate mes) {
		if (profesor == null) {
			throw new NullPointerException("No se pueden buscar reservas de un profesor nulo.");
		}
		List<Reserva> reservasProfesor = new ArrayList<>();
		for (Reserva reserva : getReservasProfesor(profesor)) {
			LocalDate diaReserva = reserva.getPermanencia().getDia();
			if (reserva.getProfesor().equals(profesor) && diaReserva.getMonthValue() == mes.getMonthValue()
					&& diaReserva.getYear() == mes.getYear()) {
				reservasProfesor.add(new Reserva(reserva));
			}
		}
		return reservasProfesor;
	}

	private Reserva getReservaAulaDia(Aula aula, LocalDate dia) {
		if (dia == null) {
			throw new NullPointerException("No se puede buscar reserva para un día nulo.");
		}
		for (Reserva reserva : getReservasAula(aula)) {
			LocalDate diaReserva = reserva.getPermanencia().getDia();
			Aula aulaReserva = reserva.getAula();
			if (diaReserva.equals(dia) && aulaReserva.equals(aula)) {
				return reserva;
			}
		}
		return null;
	}

	/*
	 * Buscamos el documento de reserva con el método find(), filtrando nombre del aula y la permanencia y quedandonos con el primer
	 * documento para que solamente devuelva uno
	 */

	@Override
	public Reserva buscar(Reserva reserva) {
		Bson filtroPermanencia = null;
		if (reserva.getPermanencia() instanceof PermanenciaPorHora) {
			filtroPermanencia = eq(MongoDB.PERMANENCIA_HORA,
					((PermanenciaPorHora) reserva.getPermanencia()).getHora().format(MongoDB.FORMATO_HORA));
		} else {
			filtroPermanencia = eq(MongoDB.PERMANENCIA_TRAMO,
					((PermanenciaPorTramo) reserva.getPermanencia()).getTramo().name());
		}
		Document documentoReserva = coleccionReservas.find()
				.filter(and(eq(MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
						eq(MongoDB.PERMANENCIA_DIA, reserva.getPermanencia().getDia().format(MongoDB.FORMATO_DIA)),
						filtroPermanencia))
				.first();

		return MongoDB.getReserva(documentoReserva);
	}

	/*
	 * Borramos el documento de nuestra colección siempre que exista con el método
	 * deleteOne(), que nos permite eliminar un único documento en la colección
	 * 
	 */

	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("No se puede anular una reserva nula.");
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("Sólo se pueden anular reservas para el mes que viene o posteriores.");
		}
		if (buscar(reserva) != null) {
			Bson filtroPermanencia = null;
			if (reserva.getPermanencia() instanceof PermanenciaPorHora) {
				filtroPermanencia = eq(MongoDB.PERMANENCIA_HORA, ((PermanenciaPorHora) reserva.getPermanencia()).getHora().format(MongoDB.FORMATO_HORA)); 
			} else {
				filtroPermanencia = eq(MongoDB.PERMANENCIA_TRAMO, ((PermanenciaPorTramo) reserva.getPermanencia()).getTramo().name());
			}
			coleccionReservas.deleteOne(
				and(
						
						eq(MongoDB.AULA_NOMBRE, reserva.getAula().getNombre()),
						eq(MongoDB.PERMANENCIA_DIA, reserva.getPermanencia().getDia().format(MongoDB.FORMATO_DIA)),
						filtroPermanencia
					)
				);
		} else {
			throw new OperationNotSupportedException("La reserva a anular no existe.");

		}
	}

	@Override
	public List<String> representar() {
		List<String> representacion = new ArrayList<>();

		for (Iterator<Reserva> it = getReservas().iterator(); it.hasNext();) {

			representacion.add(it.next().toString());
		}
		return representacion;
	}

	@Override
	public List<Reserva> getReservasProfesor(Profesor profesor) {

		if (profesor == null) {
			throw new NullPointerException("ERROR: El profesor no puede ser nulo.");
		}

		List<Reserva> reservasProfesor = new ArrayList<>();
		// Recorremos todas las reservas
		for (Iterator<Reserva> it = getReservas().iterator(); it.hasNext();) {

			Reserva reserva = it.next();
			// Si el profesor de la reserva es igual que el profesor pasado por parámetro
			if (reserva.getProfesor().equals(profesor)) {
				// Las añadimos
				reservasProfesor.add(new Reserva(reserva));
			}

		}

		Comparator<Aula> comparadorAula = Comparator.comparing(Aula::getNombre);
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo) p1).getTramo().ordinal(),
							((PermanenciaPorTramo) p2).getTramo().ordinal());
				} else if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora) p1).getHora().compareTo(((PermanenciaPorHora) p2).getHora());
				}
			} else {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			return comparacion;
		};
		reservasProfesor.sort(Comparator.comparing(Reserva::getAula, comparadorAula)
				.thenComparing(Reserva::getPermanencia, comparadorPermanencia));
		return reservasProfesor;
	}

	@Override
	public List<Reserva> getReservasAula(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: El aula no puede ser nula.");
		}
		List<Reserva> reservasAula = new ArrayList<>();

		// Recorremos todas las reservas
		for (Iterator<Reserva> it = getReservas().iterator(); it.hasNext();) {

			Reserva reserva = it.next();
			// Si el aula de la reserva es igual que el aula pasada por parámetro
			if (reserva.getAula().equals(aula)) {
				// La añadimos
				reservasAula.add(new Reserva(reserva));
			}

		}
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo) p1).getTramo().ordinal(),
							((PermanenciaPorTramo) p2).getTramo().ordinal());
				} else if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora) p1).getHora().compareTo(((PermanenciaPorHora) p2).getHora());
				}
			} else {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			return comparacion;
		};
		reservasAula.sort(Comparator.comparing(Reserva::getPermanencia, comparadorPermanencia));
		return reservasAula;
	}

	@Override
	public List<Reserva> getReservasPermanencia(Permanencia permanencia) {

		if (permanencia == null) {
			throw new NullPointerException("ERROR: La permanencia no puede ser nula.");
		}

		List<Reserva> reservasPermanencia = new ArrayList<>();
		// Recorremos todas las reservas
		for (Iterator<Reserva> it = getReservas().iterator(); it.hasNext();) {

			Reserva reserva = it.next();
			// Si la permanencia de la reserva es igual que la permanencia pasada por
			// parámetro
			if (reserva.getPermanencia().equals(permanencia)) {

				reservasPermanencia.add(new Reserva(reserva));
			}

		}

		return reservasPermanencia;

	}

	@Override
	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) {

		boolean disponible = true;

		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede consultar la disponibilidad de un aula nula.");
		}

		if (permanencia == null) {
			throw new NullPointerException("ERROR: No se puede consultar la disponibilidad de una permanencia nula.");
		}

		for (Iterator<Reserva> it = getReservas().iterator(); it.hasNext();) {

			Reserva reserva = it.next();

			if ((reserva.getPermanencia() instanceof PermanenciaPorTramo && permanencia instanceof PermanenciaPorHora
					|| reserva.getPermanencia() instanceof PermanenciaPorHora
							&& permanencia instanceof PermanenciaPorTramo)
					&& (reserva.getAula().equals(aula)
							&& reserva.getPermanencia().getDia().equals(permanencia.getDia()))) {
				disponible = false;
			}

			else if ((reserva.getPermanencia() instanceof PermanenciaPorTramo
					&& permanencia instanceof PermanenciaPorTramo
					|| reserva.getPermanencia() instanceof PermanenciaPorHora
							&& permanencia instanceof PermanenciaPorHora)
					&& (reserva.getAula().equals(aula) && reserva.getPermanencia().equals(permanencia))) {
				disponible = false;
			}

		}

		return disponible;
	}

}
