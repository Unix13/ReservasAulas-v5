package org.iesalandalus.programacion.reservasaulas.mvc.vista.grafica.controladores;

import java.time.LocalDate;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.grafica.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AnadirReserva {

	private IControlador controladorMVC;
	private VentanaPrincipal padre;
	private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
	private ObservableList<Aula> aulas = FXCollections.observableArrayList();

	@FXML
	private ListView<Profesor> lvProfesor;
	@FXML
	private ListView<Aula> lvAula;
	@FXML
	private DatePicker dpReserva;
	@FXML
	private ChoiceBox<Tramo> cbTramo;
	@FXML
	private Button btAnadir;
	@FXML
	private Button btCancelar;

	private boolean profesoresBool, aulasBool;

	private class CeldaProfesor extends ListCell<Profesor> {
		@Override
		public void updateItem(Profesor profesor, boolean vacio) {
			super.updateItem(profesor, vacio);
			if (vacio) {
				setText("");
			} else {
				setText(profesor.getNombre());
			}
		}
	}

	private class CeldaAula extends ListCell<Aula> {
		@Override
		public void updateItem(Aula aula, boolean vacio) {
			super.updateItem(aula, vacio);
			if (vacio) {
				setText("");
			} else {
				setText(aula.getNombre());
			}
		}
	}

	@FXML
	private void initialize() {
		cbTramo.getItems().add(Tramo.MANANA);
		cbTramo.getItems().add(Tramo.TARDE);
		lvProfesor.setItems(profesores);
		lvProfesor.setCellFactory(l -> new CeldaProfesor());
		lvAula.setItems(aulas);
		lvAula.setCellFactory(l -> new CeldaAula());
	}

	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	public void setPadre(VentanaPrincipal padre) {
		this.padre = padre;
	}

	public void setProfesores(ObservableList<Profesor> profesores) {
		this.profesores.setAll(profesores);
	}

	public void setAulas(ObservableList<Aula> aulas) {
		this.aulas.setAll(aulas);
	}

	public void setProfesoresBool(boolean trigger) {
		profesoresBool = trigger;
	}

	public void setAulasBool(boolean trigger) {
		aulasBool = trigger;
	}

	@FXML
	private void anadirReserva() {
		Reserva reserva = null;
		try {
			reserva = getReserva();
			controladorMVC.realizarReserva(reserva);
			padre.actualizaReservas();
			if (profesoresBool) {
				padre.mostrarReservasProfesor(reserva.getProfesor());
				profesoresBool = false;
			}
			if (aulasBool) {
				padre.mostrarReservasAula(reserva.getAula());
				aulasBool = false;
			}
			Stage propietario = ((Stage) btAnadir.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir Reserva", "Reserva añadida exitosamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir Reserva", e.getMessage());
		}
	}

	@FXML
	private void cancelar() {
		((Stage) btCancelar.getScene().getWindow()).close();
	}

	public void inicializa() {
		lvProfesor.getSelectionModel().clearSelection();
		lvAula.getSelectionModel().clearSelection();
		lvProfesor.setItems(profesores);
		lvAula.setItems(aulas);
		dpReserva.setValue(LocalDate.now());
	}

	private Reserva getReserva() {
		Profesor profesor = lvProfesor.getSelectionModel().getSelectedItem();
		Aula aula = lvAula.getSelectionModel().getSelectedItem();
		Permanencia fecha = new PermanenciaPorTramo(dpReserva.getValue(), cbTramo.getValue());
		return new Reserva(profesor, aula, fecha);
	}
}
