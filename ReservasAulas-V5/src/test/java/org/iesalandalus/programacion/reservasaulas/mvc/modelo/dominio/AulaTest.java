package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class AulaTest {

	private static final String ERROR_NOMBRE_NULO = "ERROR: El nombre del aula no puede ser nulo.";
	private static final String ERROR_NOMBRE_NO_VALIDO = "ERROR: El nombre del aula no puede estar vacío.";
	private static final String ERROR_PUESTOS_NO_VALIDOS = "ERROR: El número de puestos no es correcto.";
	private static final String ERROR_COPIAR_AULA_NULA = "ERROR: No se puede copiar un aula nula.";
	private static final String AULA_NULA = "Debería haber saltado una excepción indicando que no se puede copiar un aula nula.";
	private static final String NOMBRE_INCORRECTO = "Debería haber saltado una excepción indicando que el nombre es incorrecto.";
	private static final String PUESTOS_INCORRECTOS = "Debería haber saltado una excepción indicando que los puestos son incorrectos.";
	private static final String MENSAJE_NO_CORRECTO = "El mensaje devuelto por la excepción no es correcto.";
	private static final String TIPO_NO_CORRECTO = "El tipo de la excepción no es correcto.";
	private static final String CADENA_NO_ESPERADA = "La cadena devuelta no es la esperada.";
	private static final String ALUMNO_NO_ESPERADO = "El aula copiada debería ser la misma que la pasada como parámetro.";
	private static final String NOMBRE_NO_ESPERADO = "El nombre devuelto no es el mismo que el pasado al constructor.";
	private static final String PUESTOS_NO_ESPERADOS = "Los puestos devueltos no son los mismos que los pasados al constructor.";
	private static final String OBJETO_DEBERIA_SER_NULO = "No se debería haber creado el objeto alumno.";
	private static final String NOMBRE_AULA1 = "Aula 1";
	private static final String NOMBRE_AULA2 = "Aula 2";
	private static final int NUM_PUESTOS1 = 10;
	private static final int NUM_PUESTOS2 = 50;
	
	private static Aula aula1;
	private static Aula aula2;
	
	@BeforeClass
	public static void inicializaAtributos() {
		aula1 = new Aula(NOMBRE_AULA1, NUM_PUESTOS1);
		aula2 = new Aula(NOMBRE_AULA2, NUM_PUESTOS2);
	}

	@Test
	public void constructorNombreValidoCreaAulaCorrectamente() {
		assertThat(NOMBRE_NO_ESPERADO, aula1.getNombre(), is(NOMBRE_AULA1));
		assertThat(PUESTOS_NO_ESPERADOS, aula1.getPuestos(), is(NUM_PUESTOS1));
		assertThat(NOMBRE_NO_ESPERADO, aula2.getNombre(), is(NOMBRE_AULA2));
		assertThat(PUESTOS_NO_ESPERADOS, aula2.getPuestos(), is(NUM_PUESTOS2));
	}

	@Test
	public void constructorNombreNoValidoPuestosValidosLanzaExcepcion() {
		Aula aula = null;
		try {
			String nombre = null;
			aula = new Aula(nombre, NUM_PUESTOS1);
			fail(NOMBRE_INCORRECTO);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_NOMBRE_NULO));
			assertThat(OBJETO_DEBERIA_SER_NULO, aula, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			aula = new Aula("", NUM_PUESTOS1);
			fail(NOMBRE_INCORRECTO);
		} catch (IllegalArgumentException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_NOMBRE_NO_VALIDO));
			assertThat(OBJETO_DEBERIA_SER_NULO, aula, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			aula = new Aula("  ", NUM_PUESTOS1);
			fail(NOMBRE_INCORRECTO);
		} catch (IllegalArgumentException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_NOMBRE_NO_VALIDO));
			assertThat(OBJETO_DEBERIA_SER_NULO, aula, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void constructorNombreValidoPuestosNoValidosLanzaExcepcion() {
		Aula aula = null;
		try {
			aula = new Aula(NOMBRE_AULA1, 9);
			fail(PUESTOS_INCORRECTOS);
		} catch (IllegalArgumentException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_PUESTOS_NO_VALIDOS));
			assertThat(OBJETO_DEBERIA_SER_NULO, aula, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		try {
			aula = new Aula(NOMBRE_AULA1, 101);
			fail(PUESTOS_INCORRECTOS);
		} catch (IllegalArgumentException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_PUESTOS_NO_VALIDOS));
			assertThat(OBJETO_DEBERIA_SER_NULO, aula, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void constructorAulaValidaCopiaAulaCorrectamente() {
		Aula aula = new Aula(aula1);
		assertThat(ALUMNO_NO_ESPERADO, aula, is(aula1));
		assertThat(NOMBRE_NO_ESPERADO, aula.getNombre(), is(NOMBRE_AULA1));
		assertThat(PUESTOS_NO_ESPERADOS, aula.getPuestos(), is(NUM_PUESTOS1));
	}
	
	@Test
	public void constructorAulaNulaLanzaExcepcion() {
		Aula aula = null;
		try {
			aula = new Aula(aula);
			fail(AULA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_COPIAR_AULA_NULA));
			assertThat(OBJETO_DEBERIA_SER_NULO, aula, is(nullValue()));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void toStringDevuelveLaCadenaEsperada() {
		assertThat(CADENA_NO_ESPERADA, aula1.toString(), is(String.format("nombre=%s, puestos=%d", NOMBRE_AULA1, NUM_PUESTOS1)));
		assertThat(CADENA_NO_ESPERADA, aula2.toString(), is(String.format("nombre=%s, puestos=%d", NOMBRE_AULA2, NUM_PUESTOS2)));
	}

}