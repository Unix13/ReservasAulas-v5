package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;


public class PermanenciaPorHoraTest {
	
	private static final String ERROR_DIA_NULO = "ERROR: El día de una permanencia no puede ser nulo.";
	private static final String ERROR_HORA_NULA = "ERROR: La hora de una permanencia no puede ser nula.";
	private static final String ERROR_HORA_NO_VALIDA = "ERROR: La hora de una permanencia no es válida.";
	private static final String ERROR_HORA_NO_EN_PUNTO = "ERROR: La hora de una permanencia debe ser una hora en punto.";
	private static final String ERROR_COPIAR_PERMANENCIA_NULA = "ERROR: No se puede copiar una permanencia nula.";
	private static final String DIA_INCORRECTO = "Debería haber saltado una excepción indicando que el día es incorrecto";
	private static final String HORA_INCORRECTA = "Debería haber saltado una excepción indicando que el tramo es incorrecto";
	private static final String PERMANENCIA_NULA = "Debería haber saltado una excepción indicando que no se puede copiar un profesor nulo.";
	private static final String MENSAJE_NO_CORRECTO = "El mensaje devuelto por la excepción no es correcto.";
	private static final String TIPO_NO_CORRECTO = "El tipo de la excepción no es correcto.";
	private static final String CADENA_NO_ESPERADA = "La cadena devuelta no es la esperada.";
	private static final String PERMANENCIA_NO_ESPERADA = "La permanencia copiada debería ser la misma que la pasada como parámetro.";
	private static final String DIA_NO_ESPERADO = "El día devuelto no es el mismo que el pasado al constructor.";
	private static final String HORA_NO_ESPERADA = "El tramo devuelto no es el mismo que el pasado al constructor.";
	private static final String PUNTOS_NO_ESPERADOS = "Los puntos devueltos no son los esperados.";
	private static final String OBJETO_DEBERIA_SER_NULO = "No se debería haber creado el objeto profesor.";
	private static final LocalDate DIA1 = LocalDate.now();
	private static final LocalTime HORA1 = LocalTime.of(12, 0);
	private static final LocalDate DIA2 = LocalDate.now().plusDays(1);
	private static final LocalTime HORA2 = LocalTime.of(20, 0);

	@Test
	public void constructorDiaValidoHoraValidaCreaPermanenciaPorHoraCorrectamente() {
		PermanenciaPorHora permanencia = new PermanenciaPorHora(DIA1, HORA1);
		assertThat(DIA_NO_ESPERADO, permanencia.getDia(), is(DIA1));
		assertThat(HORA_NO_ESPERADA, permanencia.getHora(), is(HORA1));
		permanencia = new PermanenciaPorHora(DIA2, HORA2);
		assertThat(DIA_NO_ESPERADO, permanencia.getDia(), is(DIA2));
		assertThat(HORA_NO_ESPERADA, permanencia.getHora(), is(HORA2));
	}
	
	@Test
	public void constructorDiaNoValidoHoraValidaLanzaExcepcion() {
		PermanenciaPorHora permanencia = null;
		try {
			permanencia = new PermanenciaPorHora(null, HORA1);
			fail(DIA_INCORRECTO);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_DIA_NULO));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void constructorDiaValidoHoraNoValidaLanzaExcepcion() {
		PermanenciaPorHora permanencia = null;
		try {
			permanencia = new PermanenciaPorHora(DIA1, null);
			fail(HORA_INCORRECTA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_HORA_NULA));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			permanencia = new PermanenciaPorHora(DIA1, LocalTime.of(22, 1));
			fail(HORA_INCORRECTA);
		} catch (IllegalArgumentException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_HORA_NO_VALIDA));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			permanencia = new PermanenciaPorHora(DIA1, LocalTime.of(7, 59));
			fail(HORA_INCORRECTA);
		} catch (IllegalArgumentException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_HORA_NO_VALIDA));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			permanencia = new PermanenciaPorHora(DIA1, LocalTime.of(8, 59));
			fail(HORA_INCORRECTA);
		} catch (IllegalArgumentException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_HORA_NO_EN_PUNTO));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void constructorPermanenciaValidaCopiaPermanenciaCorrectamente() {
		PermanenciaPorHora permanencia1 = new PermanenciaPorHora(DIA1, HORA1);
		PermanenciaPorHora permanencia2 = new PermanenciaPorHora(permanencia1);
		assertThat(PERMANENCIA_NO_ESPERADA, permanencia2, is(permanencia1));
		assertThat(DIA_NO_ESPERADO, permanencia2.getDia(), is(DIA1));
		assertThat(HORA_NO_ESPERADA, permanencia2.getHora(), is(HORA1));
	}
	
	@Test
	public void constructorPermanenciaNulaLanzaExcepcion() {
		PermanenciaPorHora permanencia = null;
		try {
			permanencia = new PermanenciaPorHora(null);
			fail(PERMANENCIA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_COPIAR_PERMANENCIA_NULA));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void getPuntosDevuelveLosPuntosEsperados() {
		PermanenciaPorHora permanencia = new PermanenciaPorHora(DIA1, HORA1);
		assertThat(PUNTOS_NO_ESPERADOS, permanencia.getPuntos(), is(3));
	}
	
	@Test
	public void toStringDevuelveLaCadenaEsperada() {
		PermanenciaPorHora permanencia = new PermanenciaPorHora(DIA1, HORA1);
		assertThat(CADENA_NO_ESPERADA, permanencia.toString(), is(String.format("día=%s, hora=%s", DIA1.format(Permanencia.FORMATO_DIA), HORA1.format(PermanenciaPorHora.FORMATO_HORA))));
	}

}