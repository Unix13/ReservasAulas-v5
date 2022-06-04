/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo;


import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria.FactoriaFuenteDatosMemoria;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mongodb.FactoriaFuenteDatosMongoDB;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros.FactoriaFuenteDatosFicheros;
/**
 * @author vivas
 *
 */
public enum FactoriaFuenteDatos {
	
	MEMORIA {
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosMemoria();
		}
	},

	/*
	 * Nos devuelve una interfaz fuente de datos que es capaz de crear aulas,
	 * profesores y reservas de ficheros.
	 */

	FICHEROS {
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosFicheros();
		}

	},

	/*
	 * Nos devuelve una interfaz fuente de datos que es capaz de crear aulas,
	 * profesores y reservas de MongoDB.
	 */

	MONGODB {
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosMongoDB();
		}
	};

	public abstract IFuenteDatos crear();

}