package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Test;


public class PermanenciaPorTramoTest {
	
	private static final String ERROR_DIA_NULO = "ERROR: El día de una permanencia no puede ser nulo.";
	private static final String ERROR_TRAMO_NULO = "ERROR: El tramo de una permanencia no puede ser nulo.";
	private static final String ERROR_COPIAR_PERMANENCIA_NULA = "ERROR: No se puede copiar una permanencia nula.";
	private static final String DIA_INCORRECTO = "Debería haber saltado una excepción indicando que el día es incorrecto";
	private static final String TRAMO_INCORRECTO = "Debería haber saltado una excepción indicando que el tramo es incorrecto";
	private static final String PERMANENCIA_NULA = "Debería haber saltado una excepción indicando que no se puede copiar un profesor nulo.";
	private static final String MENSAJE_NO_CORRECTO = "El mensaje devuelto por la excepción no es correcto.";
	private static final String TIPO_NO_CORRECTO = "El tipo de la excepción no es correcto.";
	private static final String CADENA_NO_ESPERADA = "La cadena devuelta no es la esperada.";
	private static final String PERMANENCIA_NO_ESPERADA = "La permanencia copiada debería ser la misma que la pasada como parámetro.";
	private static final String DIA_NO_ESPERADO = "El día devuelto no es el mismo que el pasado al constructor.";
	private static final String TRAMO_NO_ESPERADO = "El tramo devuelto no es el mismo que el pasado al constructor.";
	private static final String PUNTOS_NO_ESPERADOS = "Los puntos devueltos no son los esperados.";
	private static final String OBJETO_DEBERIA_SER_NULO = "No se debería haber creado el objeto profesor.";
	private static final LocalDate DIA1 = LocalDate.now();
	private static final Tramo TRAMO1 = Tramo.MANANA;
	private static final LocalDate DIA2 = LocalDate.now().plusDays(1);
	private static final Tramo TRAMO2 = Tramo.TARDE;

	@Test
	public void constructorDiaValidoTramoValidoCreaPermanenciaPorTramoCorrectamente() {
		PermanenciaPorTramo permanencia = new PermanenciaPorTramo(DIA1, TRAMO1);
		assertThat(DIA_NO_ESPERADO, permanencia.getDia(), is(DIA1));
		assertThat(TRAMO_NO_ESPERADO, permanencia.getTramo(), is(TRAMO1));
		permanencia = new PermanenciaPorTramo(DIA2, TRAMO2);
		assertThat(DIA_NO_ESPERADO, permanencia.getDia(), is(DIA2));
		assertThat(TRAMO_NO_ESPERADO, permanencia.getTramo(), is(TRAMO2));
	}
	
	@Test
	public void constructorDiaNoValidoTramoValidoLanzaExcepcion() {
		PermanenciaPorTramo permanencia = null;
		try {
			permanencia = new PermanenciaPorTramo(null, TRAMO1);
			fail(DIA_INCORRECTO);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_DIA_NULO));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void constructorDiaValidoTramoNoValidoLanzaExcepcion() {
		PermanenciaPorTramo permanencia = null;
		try {
			permanencia = new PermanenciaPorTramo(DIA1, null);
			fail(TRAMO_INCORRECTO);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_TRAMO_NULO));
			assertThat(OBJETO_DEBERIA_SER_NULO, permanencia, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void constructorPermanenciaValidaCopiaPermanenciaPorTramoCorrectamente() {
		PermanenciaPorTramo permanencia1 = new PermanenciaPorTramo(DIA1, TRAMO1);
		PermanenciaPorTramo permanencia2 = new PermanenciaPorTramo(permanencia1);
		assertThat(PERMANENCIA_NO_ESPERADA, permanencia2, is(permanencia1));
		assertThat(DIA_NO_ESPERADO, permanencia2.getDia(), is(DIA1));
		assertThat(TRAMO_NO_ESPERADO, permanencia2.getTramo(), is(TRAMO1));
	}
	
	@Test
	public void constructorPermanenciaNulaLanzaExcepcion() {
		PermanenciaPorTramo permanencia = null;
		try {
			permanencia = new PermanenciaPorTramo(null);
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
		PermanenciaPorTramo permanencia = new PermanenciaPorTramo(DIA1, TRAMO1);
		assertThat(PUNTOS_NO_ESPERADOS, permanencia.getPuntos(), is(10));
	}
	
	@Test
	public void toStringDevuelveLaCadenaEsperada() {
		PermanenciaPorTramo permanencia = new PermanenciaPorTramo(DIA1, TRAMO1);
		assertThat(CADENA_NO_ESPERADA, permanencia.toString(), is(String.format("día=%s, tramo=%s", DIA1.format(Permanencia.FORMATO_DIA), TRAMO1)));
	}

}