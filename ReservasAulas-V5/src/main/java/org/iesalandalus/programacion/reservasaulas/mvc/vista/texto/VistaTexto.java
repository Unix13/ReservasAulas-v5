package org.iesalandalus.programacion.reservasaulas.mvc.vista.texto;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;

public class VistaTexto implements IVista {

	private static final String ERROR = "ERROR: ";
	private static final String NOMBRE_VALIDO = "Nombre válido, el nombre está registrado en el sistema.";
	private static final String CORREO_VALIDO = "Correo válido, el correo está registrado en el sistema.";

	private IControlador controlador;

	public VistaTexto() {
		Opcion.setVista(this);
	}

	@Override
	public void setControlador(IControlador controlador) {
		this.controlador = controlador;
	}

	@Override
	public void comenzar() {
		Consola.mostrarCabecera("Gestión de las Reservas de Aulas del IES Al-Ándalus");
		int ordinalOpcion;
		do {
			Consola.mostrarMenu();
			ordinalOpcion = Consola.elegirOpcion();
			Opcion opcion = Opcion.getOpcionSegunOridnal(ordinalOpcion);
			opcion.ejecutar();
		} while (ordinalOpcion != Opcion.SALIR.ordinal());
	}

	@Override
	public void salir() {
		controlador.terminar();
	}

	public void insertarAula() {
		Consola.mostrarCabecera("Insertar Aula");
		try {
			controlador.insertarAula(Consola.leerAula());
			System.out.println("Aula insertada correctamente.");
			
		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void borrarAula() {
		Consola.mostrarCabecera("Borrar Aula");
		Reserva reserva = null;

		try {

			Aula aula = Consola.leerAulaFicticia();
			
			List<Reserva> reservas = controlador.getReservasAula(aula);
			for (Iterator<Reserva> it = reservas.iterator(); it.hasNext();) {
				reserva = it.next();
			}

			if (reservas.size() > 0 && reserva.getPermanencia().getDia().isAfter(LocalDate.now())) {
				System.out.println("No se puede borrar un aula con reservas en curso.");
			} else {

				controlador.borrarAula(aula);
				System.out.println("Aula borrada correctamente.");
			}

		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void buscarAula() {
		Consola.mostrarCabecera("Buscar Aula");
		Aula aula;
		try {
			aula = controlador.buscarAula(Consola.leerAulaFicticia());
			String mensaje = (aula != null) ? aula.toString() : "Aula no registrada en el sistema.";
			System.out.println(mensaje);
			
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void listarAulas() {
		try {
			System.out.println(controlador.representarAulas());
		} catch (OperationNotSupportedException | NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}
	public void insertarProfesor() {
		Consola.mostrarCabecera("Insertar Profesor");
		try {
			controlador.insertarProfesor(Consola.leerProfesor());
			System.out.println("Profesor insertado correctamente.");
			
		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void borrarProfesor() {
		Consola.mostrarCabecera("Borrar Profesor");
		Reserva reserva = null;
		try {

			Profesor profesor = Consola.leerProfesorFicticio();
			
			List<Reserva> reservas = controlador.getReservasProfesor(profesor);
			for (Iterator<Reserva> it = reservas.iterator(); it.hasNext();) {
				reserva = it.next();
			}

			if (reservas.size() > 0 && reserva.getPermanencia().getDia().isAfter(LocalDate.now())) {
				System.out.println("No se puede borrar un profesor con reservas en curso.");
			} else {

				controlador.borrarProfesor(profesor);
				System.out.println("Profesor borrado correctamente.");
			}

		} catch (NullPointerException | OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void buscarProfesor() {
		Consola.mostrarCabecera("Buscar Profesor");
		Profesor profesor;
		try {
			profesor = controlador.buscarProfesor(Consola.leerProfesorFicticio());
			String mensaje = (profesor != null) ? profesor.toString()
					: ERROR + "El profesor no está registrado en el sistema.";
			System.out.println(mensaje);
			
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void listarProfesores() {

		try {
			System.out.println(controlador.representarProfesores());
		} catch (OperationNotSupportedException | NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void realizarReserva() {

		Consola.mostrarCabecera("Realizar Reserva");

		try {
			Profesor profesor = Consola.leerProfesorFicticio();
			Profesor profesorRegistrado = controlador.buscarProfesor(profesor);

			if (profesorRegistrado != null) {

				Aula aula = Consola.leerAulaFicticia();
				Aula aulaRegistrada = controlador.buscarAula(aula);

				if (aulaRegistrada != null) {

					Permanencia permanencia = Consola.leerPermanencia();
					Reserva reserva = new Reserva(profesorRegistrado, aulaRegistrada, permanencia);

					controlador.realizarReserva(reserva);

					System.out.println("Reserva realizada correctamente.\n" + NOMBRE_VALIDO + "\n" + CORREO_VALIDO);
				} else {
					System.out.println(ERROR + "El aula " + aula.getNombre() + ", no está registrada en el sistema.");
				}
			} else {
				System.out.println(ERROR + "El correo " + profesor.getCorreo() + ", no está registrado en el sistema.");
			}

		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void anularReserva() {

		Consola.mostrarCabecera("Anular Reserva");

		try {

			controlador.anularReserva(Consola.leerReservaFicticia());
			System.out.println("Reserva anulada correctamente.");
			
		} catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void listarReservas() {
		try {
			System.out.println(controlador.representarReservas());
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}

	}

	public void listarReservasAula() {
		Consola.mostrarCabecera("Listado de Reservas por Aula");
		List<Reserva> reservas = controlador.getReservasAula(Consola.leerAulaFicticia());
		if (reservas.size() > 0) {
			for (Iterator<Reserva> it = reservas.iterator(); it.hasNext();) {
				Reserva reserva = it.next();

				System.out.println(reserva);
			}
		} else {
			System.out.println("No hay reservas, para dicha aula.");
		}
	}

	public void listarReservasProfesor() {
		Consola.mostrarCabecera("Listado de Reservas por Profesor");
		List<Reserva> reservas = controlador.getReservasProfesor(Consola.leerProfesorFicticio());
		if (reservas.size() > 0) {
			for (Iterator<Reserva> it = reservas.iterator(); it.hasNext();) {
				Reserva reserva = it.next();

				System.out.println(reserva);
			}
		} else {
			System.out.println("No hay reservas, para dicho profesor.");
		}
	}
	public void consultarDisponibilidad() {
		Aula aula;

		try {
			aula = Consola.leerAula();
			boolean disponible = controlador.consultarDisponibilidad(aula, Consola.leerPermanencia());

			if (disponible == true)
				System.out.println("Sí está disponible.");
			else
				System.out.println("No está disponible.");
		} catch (NullPointerException | IllegalArgumentException | OperationNotSupportedException  e) {
			System.out.println(e.getMessage());
		}

	}
}

	
	

