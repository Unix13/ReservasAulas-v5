package org.iesalandalus.programacion.reservasaulas;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.Controlador;

import org.iesalandalus.programacion.reservasaulas.mvc.vista.FactoriaVista;
import org.iesalandalus.programacion.reservasaulas.mvc.controlador.Controlador;
import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IFuenteDatos;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IModelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.Modelo;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;
public class MainApp {

	public static void main(String[] args) {

		IVista vista = procesarArgumentosVista(args);
		IModelo modelo = new Modelo(procesarArgumentosFuenteDatos(args));
		IControlador controlador = new Controlador(modelo, vista);
		controlador.comenzar();
	}

	private static IVista procesarArgumentosVista(String[] args) {

		IVista vista = FactoriaVista.GRAFICA.crear();

		for (String argumento : args) {

			if (argumento.equalsIgnoreCase("-vgrafica")) {

				vista = FactoriaVista.GRAFICA.crear();

			} else if (argumento.equalsIgnoreCase("-vtexto")) {

				vista = FactoriaVista.TEXTO.crear();
			}
		}

		return vista;
	}

	private static IFuenteDatos procesarArgumentosFuenteDatos(String[] args) {
		IFuenteDatos fuenteDatos = FactoriaFuenteDatos.MONGODB.crear();

		for (String argumento : args) {

			if (argumento.equalsIgnoreCase("-fdficheros")) {

				fuenteDatos = FactoriaFuenteDatos.FICHEROS.crear();

			} else if (argumento.equalsIgnoreCase("-fdmongodb")) {

				fuenteDatos = FactoriaFuenteDatos.MONGODB.crear();
			}
		}

		return fuenteDatos;
	}
}