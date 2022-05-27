/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import java.util.List;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;

/**
 * @author vivas
 *
 */
public interface IModelo {
	
	void comenzar();

	void terminar();

	List<Aula> getAulas();

	List<String> representarAulas() throws OperationNotSupportedException;

	Aula buscarAula(Aula aula);

	void insertarAula(Aula aula) throws OperationNotSupportedException;

	void borrarAula(Aula aula) throws OperationNotSupportedException;

	List<Profesor> getProfesores();

	List<String> representarProfesores() throws OperationNotSupportedException;

	Profesor buscarProfesor(Profesor profesor);

	void insertarProfesor(Profesor profesor) throws OperationNotSupportedException;

	void borrarProfesor(Profesor profesor) throws OperationNotSupportedException;

	List<Reserva> getReservas();

	int getNumReservas();

	List<String> representarReservas() throws OperationNotSupportedException;

	Reserva buscarReserva(Reserva reserva);

	void realizarReserva(Reserva reserva) throws OperationNotSupportedException;

	void anularReserva(Reserva reserva) throws OperationNotSupportedException;

	List<Reserva> getReservasAula(Aula aula);

	List<Reserva> getReservasProfesor(Profesor profesor);

	List<Reserva> getReservasPermanencia(Permanencia permanencia);

	List<Reserva> getReservas(Profesor profesor);

	List<Reserva> getReservas(Aula aula);

	List<Reserva> getReservas(LocalDate fechaPrestamo);

	boolean consultarDisponibilidad(Aula aula, Permanencia permanencia)  throws OperationNotSupportedException;

}