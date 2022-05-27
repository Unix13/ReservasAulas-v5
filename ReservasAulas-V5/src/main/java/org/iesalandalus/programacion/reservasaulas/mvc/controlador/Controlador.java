package org.iesalandalus.programacion.reservasaulas.mvc.controlador;

import java.util.List;

import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IModelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;
import java.time.LocalDate;

public class Controlador implements IControlador {

	private IModelo modelo;
	private IVista vista;

	public Controlador(IModelo modelo, IVista vista) {
		if (modelo == null) {
			throw new NullPointerException("ERROR: El modelo no puede ser nulo.");
		}
		if (vista == null) {
			throw new NullPointerException("ERROR: La vista no puede ser nula.");
		}
		this.modelo = modelo;
		this.vista = vista;
		this.vista.setControlador(this);
	}

	@Override
	public void comenzar() {
		modelo.comenzar();
		vista.comenzar();
	}

	@Override
	public void terminar() {
		modelo.terminar();
		System.out.println("Fin del programa");
	}

	@Override
	public void insertarAula(Aula aula) throws OperationNotSupportedException {
		modelo.insertarAula(aula);
	}

	@Override
	public void insertarProfesor(Profesor profesor) throws OperationNotSupportedException {
		modelo.insertarProfesor(profesor);
	}

	@Override
	public void borrarAula(Aula aula) throws OperationNotSupportedException {
		modelo.borrarAula(aula);
	}

	@Override
	public void borrarProfesor(Profesor profesor) throws OperationNotSupportedException {
		modelo.borrarProfesor(profesor);
	}

	@Override
	public Aula buscarAula(Aula aula) {
		return modelo.buscarAula(aula);
	}

	@Override
	public Profesor buscarProfesor(Profesor profesor) {
		return modelo.buscarProfesor(profesor);
	}

	@Override
	public List<String> representarAulas() throws OperationNotSupportedException {
		return modelo.representarAulas();
	}

	@Override
	public List<String> representarProfesores() throws OperationNotSupportedException {
		return modelo.representarProfesores();
	}

	@Override
	public List<String> representarReservas() throws OperationNotSupportedException {
		return modelo.representarReservas();
	}

	@Override
	public void realizarReserva(Reserva reserva) throws OperationNotSupportedException {
		modelo.realizarReserva(reserva);
	}

	@Override
	public void anularReserva(Reserva reserva) throws OperationNotSupportedException {
		modelo.anularReserva(reserva);
	}

	@Override
	public List<Reserva> getReservasAula(Aula aula) {
		return modelo.getReservasAula(aula);

	}

	@Override
	public List<Reserva> getReservasProfesor(Profesor profesor) {
		return modelo.getReservasProfesor(profesor);

	}

	@Override
	public List<Reserva> getReservasPremanencia(Permanencia permanencia) {
		try {
			modelo.getReservasPermanencia(permanencia);
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

		return modelo.getReservasPermanencia(permanencia);
	}

	

	@Override
	public List<Profesor> getProfesores() {
		return modelo.getProfesores();
	}

	@Override
	public List<Aula> getAulas() {
		return modelo.getAulas();
	}

	@Override
	public List<Reserva> getReservas() {
		return modelo.getReservas();
	}

	@Override
	public List<Reserva> getReservas(Profesor profesor) {
		return modelo.getReservas(profesor);
	}

	@Override
	public List<Reserva> getReservas(Aula aula) {
		return modelo.getReservas(aula);
	}

	@Override
	public List<Reserva> getReservas(LocalDate fecha) {
		return modelo.getReservas(fecha);
	}
	@Override
	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) throws OperationNotSupportedException  {
		return modelo.consultarDisponibilidad(aula, permanencia);
	}

}