package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import java.util.List;
import java.time.LocalDate;
import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;

public class Modelo implements IModelo {

	
	private IProfesores profesores;
	private IAulas aulas;
	private IReservas reservas;

	public Modelo(IFuenteDatos fuente) {
		if (fuente == null)
			throw new NullPointerException("ERROR: La fuente de datos es nula.");

		profesores = fuente.crearProfesores();
		aulas = fuente.crearAulas();
		reservas = fuente.crearReservas();
	}

	@Override
	public void comenzar() {
		aulas.comenzar();
		profesores.comenzar();
		reservas.comenzar();
	}

	@Override
	public void terminar() {
		aulas.terminar();
		profesores.terminar();
		reservas.terminar();
	}

	// Metodos clase aulas

	@Override
	public List<Aula> getAulas() {
		return aulas.getAulas();
	}

	public int getNumAulas() {
		return aulas.getNumAulas();
	}

	@Override
	public List<String> representarAulas() throws OperationNotSupportedException {
		return aulas.representar();
	}

	@Override
	public Aula buscarAula(Aula aula) {
		return aulas.buscar(aula);
	}

	@Override
	public void insertarAula(Aula aula) throws OperationNotSupportedException {
		aulas.insertar(aula);
	}

	@Override
	public void borrarAula(Aula aula) throws OperationNotSupportedException {
		aulas.borrar(aula);
	}

	// Metodos clase profesores

	@Override
	public List<Profesor> getProfesores() {
		return profesores.getProfesores();
	}

	public int getNumProfesores() {
		return profesores.getNumProfesores();
	}

	@Override
	public List<String> representarProfesores() throws OperationNotSupportedException {
		return profesores.representar();
	}

	@Override
	public Profesor buscarProfesor(Profesor profesor) {
		return profesores.buscar(profesor);
	}

	@Override
	public void insertarProfesor(Profesor profesor) throws OperationNotSupportedException {
		profesores.insertar(profesor);
	}

	@Override
	public void borrarProfesor(Profesor profesor) throws OperationNotSupportedException {
		profesores.borrar(profesor);
	}

	// Metodos clase reservas

	@Override
	public List<Reserva> getReservas() {
		return reservas.getReservas();
	}

	@Override
	public int getNumReservas() {
		return reservas.getNumReservas();
	}

	@Override
	public List<String> representarReservas() throws OperationNotSupportedException {
		return reservas.representar();
	}

	@Override
	public Reserva buscarReserva(Reserva reserva) {
		return reservas.buscar(reserva);
	}

	@Override
	public void realizarReserva(Reserva reserva) throws OperationNotSupportedException {
		reservas.insertar(reserva);
	}

	@Override
	public void anularReserva(Reserva reserva) throws OperationNotSupportedException {
		reservas.borrar(reserva);
	}

	@Override
	public List<Reserva> getReservasAula(Aula aula) {
		return reservas.getReservasAula(aula);
	}

	@Override
	public List<Reserva> getReservasProfesor(Profesor profesor) {
		return reservas.getReservasProfesor(profesor);
	}

	@Override
	public List<Reserva> getReservasPermanencia(Permanencia permanencia) {
		return reservas.getReservasPermanencia(permanencia);
	}

	@Override
	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia)  throws OperationNotSupportedException {
		if (buscarAula(aula) == null)
			throw new IllegalArgumentException(
					"ERROR: No se puede consultar la disponibilidad de un aula que no existe.");

		return reservas.consultarDisponibilidad(aula, permanencia);
	}

	@Override
	public List<Reserva> getReservas(Profesor profesor) {
		
		return null;
	}

	@Override
	public List<Reserva> getReservas(Aula aula) {
		
		return null;
	}

	@Override
	public List<Reserva> getReservas(LocalDate fechaPrestamo) {
		
		return null;
	}

}