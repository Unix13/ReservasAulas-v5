package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria.Reservas;
import org.junit.BeforeClass;
import org.junit.Test;


public class ReservasTest {

	private static final String ERROR_INSERTAR_RESERVA_NULA = "ERROR: No se puede insertar una reserva nula.";
	private static final String ERROR_BORRAR_RESERVA_NULA = "ERROR: No se puede borrar una reserva nula.";
	private static final String ERROR_BUSCAR_RESERVA_NULA = "ERROR: No se puede buscar una reserva nula.";
	private static final String ERROR_RESERVA_EXISTE = "ERROR: Ya existe una reserva igual.";
	private static final String ERROR_RESERVA_PERMANENCIA_INCOMPATIBLE = "ERROR: Ya se ha realizado una reserva de otro tipo de permanencia para este día.";
	private static final String ERROR_REALIZAR_RESERVA_NO_MES_SIGUIENTE = "ERROR: Sólo se pueden hacer reservas para el mes que viene o posteriores.";
	private static final String ERROR_ANULAR_RESERVA_NO_MES_SIGUIENTE = "ERROR: Sólo se pueden anular reservas para el mes que viene o posteriores.";
	private static final String ERROR_RESERVA_EXCEDE_PUNTOS = "ERROR: Esta reserva excede los puntos máximos por mes para dicho profesor.";
	private static final String ERROR_RESERVA_BORRAR_NO_EXISTE = "ERROR: No existe ninguna reserva igual.";
	private static final String ERROR_PROFESOR_NULO = "ERROR: El profesor no puede ser nulo.";
	private static final String ERROR_AULA_NULA = "ERROR: El aula no puede ser nula.";
	private static final String OPERACION_NO_PERMITIDA = "Debería haber saltado una excepción indicando que dicha operación no está permitida.";
	private static final String RESERVA_NULA = "Debería haber saltado una excepción indicando que no se puede operar con una reserva nula.";
	private static final String MENSAJE_NO_CORRECTO = "El mensaje devuelto por la excepción no es correcto.";
	private static final String TIPO_NO_CORRECTO = "El tipo de la excepción no es correcto.";
	private static final String EXCEPCION_NO_PROCEDE = "No debería haber saltado la excepción.";
	private static final String OPERACION_NO_REALIZADA = "La operación no la ha realizado correctamente.";
	private static final String REFERENCIA_NO_ESPERADA = "La referencia devuelta es la misma que la pasada.";
	private static final String TAMANO_NO_ESPERADO = "El tamaño devuelto no es el esperado.";
	private static final String RESERVA_NO_ESPERADA = "La reserva devuelta no es la que debería ser.";
	private static final String OBJETO_DEBERIA_SER_NULO = "No se debería haber creado el objeto.";
	
	private static Profesor profesor1;
	private static Profesor profesor2;
	private static Profesor profesor3;
	private static Aula aula1;
	private static Aula aula2;
	private static Aula aula3;
	private static Permanencia permanencia1;
	private static Permanencia permanencia2;
	private static Permanencia permanencia3;
	private static Permanencia permanencia4;
	private static Permanencia permanencia5;
	private static Permanencia permanencia6;
	private static Reserva reserva1;
	private static Reserva reserva2;
	private static Reserva reserva3;
	private static Reserva reserva4;
	private static Reserva reserva5;
	private static Reserva reserva6;
	private static Reserva reserva7;
	private static Reserva reserva8;
	private static Reserva reserva9;
	private static Reserva reservaRepetidaTramo;
	private static Reserva reservaRepetidaHora;
	private static Reserva reservaTramo;
	private static Reserva reservaHora;
	
	@BeforeClass
	public static void asignarValoresAtributos() {
		profesor1 = Profesor.getProfesorFicticio("bob@gmail.com");
		profesor2 = Profesor.getProfesorFicticio("calamardo@gmail.com");
		profesor3 = Profesor.getProfesorFicticio("patricio@gmail.com");
		aula1 = new Aula("Aula 1", 10);
		aula2 = new Aula("Aula 2", 20);
		aula3 = new Aula("Aula 3", 50);
		permanencia1 = new PermanenciaPorTramo(LocalDate.now().plusMonths(1), Tramo.MANANA);
		permanencia2 = new PermanenciaPorTramo(LocalDate.now().plusMonths(1), Tramo.TARDE);
		permanencia3 = new PermanenciaPorTramo(LocalDate.now().plusMonths(1).plusDays(1), Tramo.MANANA);
		permanencia4 = new PermanenciaPorHora(LocalDate.now().plusMonths(1).plusDays(2), LocalTime.of(10, 0));
		permanencia5 = new PermanenciaPorHora(LocalDate.now().plusMonths(1).plusDays(2), LocalTime.of(11, 0));
		permanencia6 = new PermanenciaPorHora(LocalDate.now().plusMonths(1).plusDays(2), LocalTime.of(12, 0));
		reserva1 = new Reserva(profesor1, aula1, permanencia3);
		reserva2 = new Reserva(profesor1, aula2, permanencia3);
		reserva3 = new Reserva(profesor3, aula3, permanencia3);
		reserva4 = new Reserva(profesor1, aula2, permanencia2);
		reserva5 = new Reserva(profesor2, aula2, permanencia1);
		reserva6 = new Reserva(profesor3, aula2, permanencia6);
		reserva7 = new Reserva(profesor2, aula2, permanencia5);
		reserva8 = new Reserva(profesor1, aula2, permanencia4);
		reserva9 = new Reserva(profesor2, aula1, permanencia4);
		reservaRepetidaTramo = new Reserva(profesor1, aula2, permanencia3);
		reservaRepetidaHora = new Reserva(profesor1, aula2, permanencia4);
		reservaTramo = new Reserva(profesor1, aula2, new PermanenciaPorTramo(LocalDate.now().plusMonths(1), Tramo.MANANA));
		reservaHora = new Reserva(profesor1, aula2, new PermanenciaPorHora(LocalDate.now().plusMonths(1), LocalTime.of(12, 0)));
	}
	
	@Test
	public void getDevuelveReservasOrdenadasPorAulaYPermanencia() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva3);
			reservas.insertar(reserva4);
			reservas.insertar(reserva5);
			reservas.insertar(reserva6);
			reservas.insertar(reserva7);
			reservas.insertar(reserva8);
			reservas.insertar(reserva9);
			List<Reserva> copiaReservas = reservas.getReservas();
			assertThat(TAMANO_NO_ESPERADO, copiaReservas.size(), is(9));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(0), is(reserva1));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(0), not(sameInstance(reserva1)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(1), is(reserva9));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(1), not(sameInstance(reserva9)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(2), is(reserva5));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(2), not(sameInstance(reserva5)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(3), is(reserva4));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(3), not(sameInstance(reserva4)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(4), is(reserva2));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(4), not(sameInstance(reserva2)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(5), is(reserva8));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(5), not(sameInstance(reserva8)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(6), is(reserva7));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(6), not(sameInstance(reserva7)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(7), is(reserva6));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(7), not(sameInstance(reserva6)));
			assertThat(RESERVA_NO_ESPERADA, copiaReservas.get(8), is(reserva3));
			assertThat(REFERENCIA_NO_ESPERADA, copiaReservas.get(8), not(sameInstance(reserva3)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void getProfesorValidoDevuelveReservasProfesorOrdenadasPorAulaYPermanencia() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva3);
			reservas.insertar(reserva4);
			reservas.insertar(reserva5);
			reservas.insertar(reserva6);
			reservas.insertar(reserva7);
			reservas.insertar(reserva8);
			reservas.insertar(reserva9);
			List<Reserva> reservasProfesor = reservas.getReservasProfesor(profesor1);
			assertThat(OPERACION_NO_REALIZADA, reservasProfesor.get(0), is(reserva1));
			assertThat(REFERENCIA_NO_ESPERADA, reservasProfesor.get(0), not(sameInstance(reserva1)));
			assertThat(OPERACION_NO_REALIZADA, reservasProfesor.get(1), is(reserva4));
			assertThat(REFERENCIA_NO_ESPERADA, reservasProfesor.get(1), not(sameInstance(reserva4)));
			assertThat(OPERACION_NO_REALIZADA, reservasProfesor.get(2), is(reserva2));
			assertThat(REFERENCIA_NO_ESPERADA, reservasProfesor.get(2), not(sameInstance(reserva2)));
			assertThat(OPERACION_NO_REALIZADA, reservasProfesor.get(3), is(reserva8));
			assertThat(REFERENCIA_NO_ESPERADA, reservasProfesor.get(3), not(sameInstance(reserva8)));
			assertThat(TAMANO_NO_ESPERADO, reservasProfesor.size(), is(4));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void getProfesorNoValidoLanzaExcepcion() {
		IReservas reservas = new Reservas();
		List<Reserva> reservasProfesor = null;
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva3);
			Profesor profesor = null;
			reservasProfesor = reservas.getReservasProfesor(profesor);
			fail(OPERACION_NO_PERMITIDA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_PROFESOR_NULO));
			assertThat(OBJETO_DEBERIA_SER_NULO, reservasProfesor, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void getAulaValidaDevuelveReservasAulaOrdenadasPorPermanencia() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva3);
			reservas.insertar(reserva4);
			reservas.insertar(reserva5);
			reservas.insertar(reserva6);
			reservas.insertar(reserva7);
			reservas.insertar(reserva8);
			reservas.insertar(reserva9);
			List<Reserva> reservasAula = reservas.getReservasAula(aula2);
			assertThat(OPERACION_NO_REALIZADA, reservasAula.get(0), is(reserva5));
			assertThat(REFERENCIA_NO_ESPERADA, reservasAula.get(0), not(sameInstance(reserva5)));
			assertThat(OPERACION_NO_REALIZADA, reservasAula.get(1), is(reserva4));
			assertThat(REFERENCIA_NO_ESPERADA, reservasAula.get(1), not(sameInstance(reserva4)));
			assertThat(OPERACION_NO_REALIZADA, reservasAula.get(2), is(reserva2));
			assertThat(REFERENCIA_NO_ESPERADA, reservasAula.get(2), not(sameInstance(reserva2)));
			assertThat(OPERACION_NO_REALIZADA, reservasAula.get(3), is(reserva8));
			assertThat(REFERENCIA_NO_ESPERADA, reservasAula.get(3), not(sameInstance(reserva8)));
			assertThat(OPERACION_NO_REALIZADA, reservasAula.get(4), is(reserva7));
			assertThat(REFERENCIA_NO_ESPERADA, reservasAula.get(4), not(sameInstance(reserva7)));
			assertThat(OPERACION_NO_REALIZADA, reservasAula.get(5), is(reserva6));
			assertThat(REFERENCIA_NO_ESPERADA, reservasAula.get(5), not(sameInstance(reserva6)));
			assertThat(TAMANO_NO_ESPERADO, reservasAula.size(), is(6));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void getAulaNoValidaLanzaExcepcion() {
		IReservas reservas = new Reservas();
		List<Reserva> reservasAula = null;
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva3);
			Aula aula = null;
			reservasAula = reservas.getReservasAula(aula);
			fail(OPERACION_NO_PERMITIDA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_AULA_NULA));
			assertThat(OBJETO_DEBERIA_SER_NULO, reservasAula, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}

	
	@Test
	public void insertarReservaValidaConReservasVaciasInsertaReservaCorrectamente() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(reserva1));
			assertThat(REFERENCIA_NO_ESPERADA, reservas.buscar(reserva1), not(sameInstance(reserva1)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva6);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(reserva6));
			assertThat(REFERENCIA_NO_ESPERADA, reservas.buscar(reserva6), not(sameInstance(reserva6)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void insertarDosReservasValidasInsertaReservasCorrectamente() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva6);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(2));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(reserva1));
			assertThat(REFERENCIA_NO_ESPERADA, reservas.buscar(reserva1), not(sameInstance(reserva1)));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(reserva6));
			assertThat(REFERENCIA_NO_ESPERADA, reservas.buscar(reserva6), not(sameInstance(reserva6)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void insertarTresReservasValidasInsertaReservasCorrectamente() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva6);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(3));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(reserva1));
			assertThat(REFERENCIA_NO_ESPERADA, reservas.buscar(reserva1), not(sameInstance(reserva1)));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva2), is(reserva2));
			assertThat(REFERENCIA_NO_ESPERADA, reservas.buscar(reserva2), not(sameInstance(reserva2)));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(reserva6));
			assertThat(REFERENCIA_NO_ESPERADA, reservas.buscar(reserva6), not(sameInstance(reserva6)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void insertarReservaNulaLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(null);
			fail(RESERVA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_INSERTAR_RESERVA_NULA));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void insertarReservaRepetidaLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva3);
			reservas.insertar(reservaRepetidaTramo);
			System.out.println(reservas.getReservas());
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva2);
			reservas.insertar(reserva1);
			reservas.insertar(reserva3);
			reservas.insertar(reservaRepetidaTramo);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva2);
			reservas.insertar(reserva3);
			reservas.insertar(reserva1);
			reservas.insertar(reservaRepetidaTramo);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva6);
			reservas.insertar(reserva7);
			reservas.insertar(reserva8);
			reservas.insertar(reservaRepetidaHora);
			System.out.println(reservas.getReservas());
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva7);
			reservas.insertar(reserva6);
			reservas.insertar(reserva8);
			reservas.insertar(reservaRepetidaHora);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva7);
			reservas.insertar(reserva8);
			reservas.insertar(reserva6);
			reservas.insertar(reservaRepetidaHora);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void insertarReservaMismoDiaDiferenteTipoPermanenciaLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reservaTramo);
			reservas.insertar(reservaHora);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_PERMANENCIA_INCOMPATIBLE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reservaHora);
			reservas.insertar(reservaTramo);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_PERMANENCIA_INCOMPATIBLE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void insertarReservaNoMesQueVieneLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(new Reserva(profesor1, aula1, new PermanenciaPorTramo(LocalDate.now(), Tramo.MANANA)));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_REALIZAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			LocalDate ultimoDiaMes = LocalDate.now().plusMonths(1).plusDays(-LocalDate.now().plusMonths(1).getDayOfMonth());
			reservas.insertar(new Reserva(profesor1, aula1, new PermanenciaPorTramo(ultimoDiaMes, Tramo.MANANA)));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_REALIZAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(new Reserva(profesor1, aula1, new PermanenciaPorHora(LocalDate.now(), LocalTime.of(12, 0))));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_REALIZAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			LocalDate ultimoDiaMes = LocalDate.now().plusMonths(1).plusDays(-LocalDate.now().plusMonths(1).getDayOfMonth());
			reservas.insertar(new Reserva(profesor1, aula1, new PermanenciaPorHora(ultimoDiaMes, LocalTime.of(12, 0))));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_REALIZAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void insertarReservaPuntosSobrepasadosProfesorLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(new Reserva(profesor1, aula3, permanencia1));
			reservas.insertar(new Reserva(profesor1, aula3, permanencia2));
			reservas.insertar(new Reserva(profesor1, aula3, permanencia3));
			reservas.insertar(new Reserva(profesor1, aula3, permanencia4));
			reservas.insertar(new Reserva(profesor1, aula3, permanencia5));
			reservas.insertar(new Reserva(profesor1, aula3, permanencia6));
			reservas.insertar(new Reserva(profesor1, aula1, permanencia2));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_EXCEDE_PUNTOS));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(6));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void borrarReservaExistenteBorraReservaCorrectamente() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.borrar(reserva1);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(nullValue()));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva6);
			reservas.borrar(reserva1);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(reserva6));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(nullValue()));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva6);
			reservas.borrar(reserva6);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(reserva1));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(nullValue()));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva6);
			reservas.borrar(reserva1);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(2));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(nullValue()));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva2), is(reserva2));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(reserva6));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva6);
			reservas.borrar(reserva2);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(2));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva2), is(nullValue()));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(reserva1));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(reserva6));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.insertar(reserva2);
			reservas.insertar(reserva6);
			reservas.borrar(reserva6);
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(2));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva6), is(nullValue()));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva1), is(reserva1));
			assertThat(RESERVA_NO_ESPERADA, reservas.buscar(reserva2), is(reserva2));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void borrarReservaNoMesQueVieneLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.borrar(new Reserva(profesor1, aula1, new PermanenciaPorTramo(LocalDate.now(), Tramo.MANANA)));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_ANULAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			LocalDate ultimoDiaMes = LocalDate.now().plusMonths(1).plusDays(-LocalDate.now().plusMonths(1).getDayOfMonth());
			reservas.borrar(new Reserva(profesor1, aula1, new PermanenciaPorTramo(ultimoDiaMes, Tramo.MANANA)));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_ANULAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		reservas = new Reservas();
		try {
			reservas.borrar(new Reserva(profesor1, aula1, new PermanenciaPorHora(LocalDate.now(), LocalTime.of(12, 0))));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_ANULAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			LocalDate ultimoDiaMes = LocalDate.now().plusMonths(1).plusDays(-LocalDate.now().plusMonths(1).getDayOfMonth());
			reservas.borrar(new Reserva(profesor1, aula1, new PermanenciaPorHora(ultimoDiaMes, LocalTime.of(12, 0))));
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_ANULAR_RESERVA_NO_MES_SIGUIENTE));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void borrarReservaNoExistenteLanzaExcepcion() {
		IReservas citas = new Reservas();
		try {
			citas.insertar(reserva1);
			citas.borrar(reserva2);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_BORRAR_NO_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, citas.getNumReservas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		citas = new Reservas();
		try {
			citas.insertar(reserva1);
			citas.insertar(reserva2);
			citas.borrar(reserva6);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_RESERVA_BORRAR_NO_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, citas.getNumReservas(), is(2));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void borrarReservaNulaLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.borrar(null);
			fail(RESERVA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_BORRAR_RESERVA_NULA));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void buscarReservaNulaLanzaExcepcion() {
		IReservas reservas = new Reservas();
		try {
			reservas.insertar(reserva1);
			reservas.buscar(null);
			fail(RESERVA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_BUSCAR_RESERVA_NULA));
			assertThat(TAMANO_NO_ESPERADO, reservas.getNumReservas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}

}