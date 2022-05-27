package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria.Aulas;
import org.junit.BeforeClass;
import org.junit.Test;

public class AulasTest {

	private static final String ERROR_INSERTAR_AULA_NULA = "ERROR: No se puede insertar un aula nula.";
	private static final String ERROR_BORRAR_AULA_NULA = "ERROR: No se puede borrar un aula nula.";
	private static final String ERROR_BUSCAR_AULA_NULA = "ERROR: No se puede buscar un aula nula.";
	private static final String ERROR_AULA_EXISTE = "ERROR: Ya existe un aula con ese nombre.";
	private static final String ERROR_AULA_BORRAR_NO_EXISTE = "ERROR: No existe ningún aula con ese nombre.";
	private static final String OPERACION_NO_PERMITIDA = "Debería haber saltado una excepción indicando que dicha operación no está permitida.";
	private static final String AULA_NULA = "Debería haber saltado una excepción indicando que no se puede operar con un aula con nombre nulo.";
	private static final String MENSAJE_NO_CORRECTO = "El mensaje devuelto por la excepción no es correcto.";
	private static final String TIPO_NO_CORRECTO = "El tipo de la excepción no es correcto.";
	private static final String EXCEPCION_NO_PROCEDE = "No debería haber saltado la excepción.";
	private static final String REFERENCIA_NO_ESPERADA = "La referencia devuelta es la misma que la pasada.";
	private static final String TAMANO_NO_ESPERADO = "El tamaño devuelto no es el esperado.";
	private static final String AULA_NO_ESPERADA = "El aula devuelta no es la que debería ser.";
	
	private static Aula aula1;
	private static Aula aula2;
	private static Aula aula3;
	private static Aula aulaRepetida;
	
	@BeforeClass
	public static void asignarValoresAtributos() {
		aula1 = new Aula("Aula 1", 10);
		aula2 = new Aula("Aula 2", 20);
		aula3 = new Aula("Aula 3", 30);
		aulaRepetida = new Aula(aula1);
	}
	
	@Test
	public void getDevuelveAulasOrdenadasPorNombre() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula3);
			aulas.insertar(aula2);
			aulas.insertar(aula1);
			List<Aula> copiaAulas = aulas.getAulas();
			assertThat(TAMANO_NO_ESPERADO, copiaAulas.size(), is(3));
			assertThat(AULA_NO_ESPERADA, copiaAulas.get(0), is(aula1));
			assertThat(REFERENCIA_NO_ESPERADA, copiaAulas.get(0), not(sameInstance(aula1)));
			assertThat(AULA_NO_ESPERADA, copiaAulas.get(1), is(aula2));
			assertThat(REFERENCIA_NO_ESPERADA, copiaAulas.get(1), not(sameInstance(aula2)));
			assertThat(AULA_NO_ESPERADA, copiaAulas.get(2), is(aula3));
			assertThat(REFERENCIA_NO_ESPERADA, copiaAulas.get(2), not(sameInstance(aula3)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void insertarAulaValidaConAulasVaciasInsertaAulaCorrectamente() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(1));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(aula1));
			assertThat(REFERENCIA_NO_ESPERADA, aulas.buscar(aula1), not(sameInstance(aula1)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void insertarDosAulasValidasInsertaAulasCorrectamente() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(2));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(aula1));
			assertThat(REFERENCIA_NO_ESPERADA, aulas.buscar(aula1), not(sameInstance(aula1)));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula2), is(aula2));
			assertThat(REFERENCIA_NO_ESPERADA, aulas.buscar(aula2), not(sameInstance(aula2)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void insertarTresAulasValidasInsertaAulasCorrectamente() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			aulas.insertar(aula3);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(3));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(aula1));
			assertThat(REFERENCIA_NO_ESPERADA, aulas.buscar(aula1), not(sameInstance(aula1)));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula2), is(aula2));
			assertThat(REFERENCIA_NO_ESPERADA, aulas.buscar(aula2), not(sameInstance(aula2)));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula3), is(aula3));
			assertThat(REFERENCIA_NO_ESPERADA, aulas.buscar(aula3), not(sameInstance(aula3)));
		} catch (OperationNotSupportedException e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void insertarAulaNulaLanzaExcepcion() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(null);
			fail(AULA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_INSERTAR_AULA_NULA));
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(0));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void insertarAulaRepetidaLanzaExcepcion() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			aulas.insertar(aula3);
			aulas.insertar(aulaRepetida);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_AULA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		aulas = new Aulas();
		try {
			aulas.insertar(aula2);
			aulas.insertar(aula1);
			aulas.insertar(aula3);
			aulas.insertar(aulaRepetida);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_AULA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		aulas = new Aulas();
		try {
			aulas.insertar(aula2);
			aulas.insertar(aula3);
			aulas.insertar(aula1);
			aulas.insertar(aulaRepetida);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_AULA_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(3));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void borrarAulaExistenteBorraAulaCorrectamente() throws OperationNotSupportedException {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.borrar(aula1);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(0));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(nullValue()));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			aulas.borrar(aula1);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(1));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula2), is(aula2));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(nullValue()));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			aulas.borrar(aula2);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(1));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(aula1));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula2), is(nullValue()));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			aulas.insertar(aula3);
			aulas.borrar(aula1);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(2));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(nullValue()));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula2), is(aula2));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula3), is(aula3));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			aulas.insertar(aula3);
			aulas.borrar(aula2);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(2));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula2), is(nullValue()));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(aula1));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula3), is(aula3));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
		aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.insertar(aula2);
			aulas.insertar(aula3);
			aulas.borrar(aula3);
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(2));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula3), is(nullValue()));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula1), is(aula1));
			assertThat(AULA_NO_ESPERADA, aulas.buscar(aula2), is(aula2));
		} catch (Exception e) {
			fail(EXCEPCION_NO_PROCEDE);
		}
	}
	
	@Test
	public void borrarAulaNoExistenteLanzaExcepcion() {
		IAulas citas = new Aulas();
		try {
			citas.insertar(aula1);
			citas.borrar(aula2);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_AULA_BORRAR_NO_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, citas.getNumAulas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
		citas = new Aulas();
		try {
			citas.insertar(aula1);
			citas.insertar(aula2);
			citas.borrar(aula3);
			fail(OPERACION_NO_PERMITIDA);
		} catch (OperationNotSupportedException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_AULA_BORRAR_NO_EXISTE));
			assertThat(TAMANO_NO_ESPERADO, citas.getNumAulas(), is(2));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void borrarAulaNulaLanzaExcepcion() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.borrar(null);
			fail(AULA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_BORRAR_AULA_NULA));
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}
	
	@Test
	public void buscarAulaNulaLanzaExcepcion() {
		IAulas aulas = new Aulas();
		try {
			aulas.insertar(aula1);
			aulas.buscar(null);
			fail(AULA_NULA);
		} catch (NullPointerException e) {
			assertThat(MENSAJE_NO_CORRECTO, e.getMessage(), is(ERROR_BUSCAR_AULA_NULA));
			assertThat(TAMANO_NO_ESPERADO, aulas.getNumAulas(), is(1));
		} catch (Exception e) {
			fail(TIPO_NO_CORRECTO);
		}
	}

}