package org.iesalandalus.programacion.reservasaulas.mvc.vista.grafica.controladores;

import java.io.IOException;
import java.time.LocalDate;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.grafica.recursos.LocalizadorRecursos;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.grafica.utilidades.Dialogos;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaPrincipal {
	

	private static final String BORRAR_PROFESOR = "Borrar Profesor";
	private static final String BORRAR_AULA = "Borrar Aula";
	private static final String BORRAR_RESERVA = "Borrar Reserva";

	private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
	private ObservableList<Reserva> reservasProfesor = FXCollections.observableArrayList();
	private ObservableList<Aula> aulas = FXCollections.observableArrayList();
	private ObservableList<Reserva> reservasAulas = FXCollections.observableArrayList();
	private ObservableList<Reserva> reservas = FXCollections.observableArrayList();

	private IControlador controladorMVC;

	@FXML
	private TableView<Profesor> tvProfesores;
	@FXML
	private TableColumn<Profesor, String> tcNombre;
	@FXML
	private TableColumn<Profesor, String> tcCorreo;
	@FXML
	private TableColumn<Profesor, String> tcTlf;

	@FXML
	private TableView<Reserva> tvReservasProfesor;
	@FXML
	private TableColumn<Reserva, String> tcpAula;
	@FXML
	private TableColumn<Reserva, String> tcpFecha;
	@FXML
	private TableColumn<Reserva, String> tcpPermanencia;
	@FXML
	private TableColumn<Reserva, String> tcpPuntos;

	@FXML
	private TableView<Aula> tvAulas;
	@FXML
	private TableColumn<Aula, String> tcaNombre;
	@FXML
	private TableColumn<Aula, String> tcaPuestos;
	@FXML
	private TableColumn<Aula, String> tcaAPuntos;

	@FXML
	private TableView<Reserva> tvReservasAulas;
	@FXML
	private TableColumn<Reserva, String> tcaAula;
	@FXML
	private TableColumn<Reserva, String> tcaFecha;
	@FXML
	private TableColumn<Reserva, String> tcaPermanencia;
	@FXML
	private TableColumn<Reserva, String> tcaPuntos;

	@FXML
	private TableView<Reserva> tvReservas;
	@FXML
	private TableColumn<Reserva, String> tcPermanencia;
	@FXML
	private TableColumn<Reserva, String> tcAula;
	@FXML
	private TableColumn<Reserva, String> tcProfesor;
	@FXML
	private TableColumn<Reserva, String> tcPuntos;
	@FXML
	private DatePicker dpMes;
	@FXML
	private CheckBox cbMes;
	@FXML
	private Label primeroESO;

	private Stage anadirProfesor;
	private AnadirProfesor cAnadirProfesor;
	private Stage anadirAula;
	private AnadirAula cAnadirAula;
	private Stage anadirReserva;
	private AnadirReserva cAnadirReserva;

	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	@FXML
	private void initialize() {
		
		this.inicializarProfesor();
		this.inicializarAula();
		this.inicializarReserva();
		this.inicializarReservaProfesor();
	
	}

	

	@FXML
	private void salir() {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?",
				null)) {
			controladorMVC.terminar();
			System.exit(0);
		}
	}

	@FXML
	private void acercaDe() throws IOException {
		VBox contenido = FXMLLoader.load(LocalizadorRecursos.class.getResource("vistas/AcercaDe.fxml"));
		Dialogos.mostrarDialogoInformacionPersonalizado("Acerca De", contenido);
	}

//PROFESOR
	private void inicializarProfesor() {
		tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tcCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
		tcTlf.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		tvProfesores.setItems(profesores);
		tvProfesores.getSelectionModel().selectedItemProperty()
				.addListener((ob, ov, nv) -> mostrarReservasProfesor(nv));
	}
	
	public void actualizaProfesores() {
		reservasAulas.clear();
		tvAulas.getSelectionModel().clearSelection();
		profesores.setAll(controladorMVC.getProfesores());
	}

	@FXML
	void anadirProfesor(ActionEvent event) throws IOException {
		crearAnadirProfesor();
		anadirProfesor.showAndWait();
	}

	@FXML
	void borrarProfesor(ActionEvent event) {
		Profesor profesor = null;
		try {
			profesor = tvProfesores.getSelectionModel().getSelectedItem();
			if (profesor != null && Dialogos.mostrarDialogoConfirmacion(BORRAR_PROFESOR,
					"¿Estás seguro de que quieres borrar el profesor?", null)) {
				controladorMVC.borrarProfesor(profesor);
				profesores.remove(profesor);
				reservasProfesor.clear();
				actualizaProfesores();
				Dialogos.mostrarDialogoInformacion(BORRAR_PROFESOR, "Profesor borrado exitosamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(BORRAR_PROFESOR, e.getMessage());
		}
	}

	public void mostrarReservasProfesor(Profesor profesor) {
		try {
			if (profesor != null) {
				reservasProfesor.setAll(controladorMVC.getReservasProfesor(profesor));
			}
		} catch (IllegalArgumentException e) {
			reservasProfesor.setAll(FXCollections.observableArrayList());
		}
		actualizaReservas();
	}

	private void crearAnadirProfesor() throws IOException {
		if (anadirProfesor == null) {
			anadirProfesor = new Stage();
			FXMLLoader cargadorAnadirProfesor = new FXMLLoader(
					LocalizadorRecursos.class.getResource("vistas/AnadirProfesor.fxml"));
			VBox raizAnadirProfesor = cargadorAnadirProfesor.load();
			cAnadirProfesor = cargadorAnadirProfesor.getController();
			cAnadirProfesor.setControladorMVC(controladorMVC);
			cAnadirProfesor.setProfesores(profesores);
			cAnadirProfesor.inicializa();
			Scene escenaAnadirProfesor = new Scene(raizAnadirProfesor);
			anadirProfesor.setTitle("Añadir Profesor");
			anadirProfesor.initModality(Modality.APPLICATION_MODAL);
			anadirProfesor.setScene(escenaAnadirProfesor);
		} else {
			cAnadirProfesor.inicializa();
		}
	}

	


	private void inicializarAula() {
		tcaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tcaPuestos.setCellValueFactory(new PropertyValueFactory<>("puestos"));
		tvAulas.setItems(aulas);
		tvAulas.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> mostrarReservasAula(nv));
	}

	@FXML
	void anadirAula(ActionEvent event) throws IOException {
		crearAnadirAula();
		anadirAula.showAndWait();
	}

	@FXML
	void borrarAula(ActionEvent event) {
		Aula aula = null;
		try {
			aula = tvAulas.getSelectionModel().getSelectedItem();
			if (aula != null && Dialogos.mostrarDialogoConfirmacion(BORRAR_AULA,
					"¿Estás seguro de que quieres borrar el aula?", null)) {
				controladorMVC.borrarAula(aula);
				aulas.remove(aula);
				reservasAulas.clear();
				actualizaAulas();
				Dialogos.mostrarDialogoInformacion(BORRAR_AULA, "Aula borrada exitosamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(BORRAR_AULA, e.getMessage());
		}
	}

	public void mostrarReservasAula(Aula aula) {
		try {
			if (aula != null) {
				reservasAulas.setAll(controladorMVC.getReservasAula(aula));
			}
		} catch (IllegalArgumentException e) {
			reservasAulas.setAll(FXCollections.observableArrayList());
		}
		actualizaReservas();
	}

	private void crearAnadirAula() throws IOException {
		if (anadirAula == null) {
			anadirAula = new Stage();
			FXMLLoader cargadorAnadirAula = new FXMLLoader(
					LocalizadorRecursos.class.getResource("vistas/AnadirAula.fxml"));
			VBox raizAnadirAula = cargadorAnadirAula.load();
			cAnadirAula = cargadorAnadirAula.getController();
			cAnadirAula.setControladorMVC(controladorMVC);
			cAnadirAula.setAulas(aulas);
			cAnadirAula.inicializa();
			Scene escenaAnadirAula = new Scene(raizAnadirAula);
			anadirAula.setTitle("Añadir Aula");
			anadirAula.initModality(Modality.APPLICATION_MODAL);
			anadirAula.setScene(escenaAnadirAula);
		} else {
			cAnadirAula.inicializa();
		}
	}

	public void actualizaAulas() {
		reservasAulas.clear();
		tvAulas.getSelectionModel().clearSelection();
		profesores.setAll(controladorMVC.getProfesores());
	}



	private void inicializarReserva() {
		tcPermanencia.setCellValueFactory(new PropertyValueFactory<>("permanencia"));
		tcAula.setCellValueFactory(new PropertyValueFactory<>("aula"));
		tcProfesor.setCellValueFactory(new PropertyValueFactory<>("profesor"));
		tcPuntos.setCellValueFactory(
				reserva -> new SimpleStringProperty(String.valueOf(reserva.getValue().getPuntos())));
		tvReservas.setItems(reservas);
	}

	@FXML
	void anadirReserva(ActionEvent event) throws IOException {
		crearAnadirReserva();
		anadirReserva.showAndWait();
	}

	@FXML
	void borrarReserva(ActionEvent event) {
		Reserva reserva = null;
		try {
			reserva = tvReservas.getSelectionModel().getSelectedItem();
			if (reserva != null && Dialogos.mostrarDialogoConfirmacion(BORRAR_RESERVA,
					"¿Estás seguro de que quieres borrar la reserva?", null)) {
				controladorMVC.anularReserva(reserva);
				reservas.remove(reserva);
				reservas.clear();
				actualizaAulas();
				actualizaReservas();
				Dialogos.mostrarDialogoInformacion(BORRAR_RESERVA, "Reserva borrada exitosamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(BORRAR_RESERVA, e.getMessage());
		}
	}

	public void mostrarReservasMes(LocalDate mes) {
		if (mes != null) {
			actualizaReservasPorMes(mes);
			actualizaAulas();
			actualizaReservas();
		}
	}

	private void crearAnadirReserva() throws IOException {
		if (anadirReserva == null) {
			anadirReserva = new Stage();
			FXMLLoader cargadorAnadirReserva = new FXMLLoader(
					LocalizadorRecursos.class.getResource("vistas/AnadirReserva.fxml"));
			VBox raizAnadirReserva = cargadorAnadirReserva.load();
			cAnadirReserva = cargadorAnadirReserva.getController();
			cAnadirReserva.setControladorMVC(controladorMVC);
			cAnadirReserva.setProfesores(profesores);
			cAnadirReserva.setAulas(aulas);
			cAnadirReserva.setPadre(this);
			cAnadirReserva.inicializa();
			Scene escenaAnadirReserva = new Scene(raizAnadirReserva);
			anadirReserva.setTitle("Añadir Reserva");
			anadirReserva.initModality(Modality.APPLICATION_MODAL);
			anadirReserva.setScene(escenaAnadirReserva);
		} else {
			cAnadirReserva.setProfesores(profesores);
			cAnadirReserva.setAulas(aulas);
			cAnadirReserva.inicializa();
		}
	}

	public void actualizaReservas() {
		reservas.setAll(controladorMVC.getReservas());
	}

	public void actualizaReservasPorMes(LocalDate mes) {
		reservas.setAll(controladorMVC.getReservas(mes));
	}


	private void inicializarReservaProfesor() {
		tcpAula.setCellValueFactory(new PropertyValueFactory<>("aula"));
		tcpFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		tcpPermanencia.setCellValueFactory(new PropertyValueFactory<>("tramo"));
		tcpPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
		tcpPuntos.setCellValueFactory(
				reserva -> new SimpleStringProperty(String.valueOf(reserva.getValue().getPuntos())));
	}

	@FXML
	void anadirReservaProfesor(ActionEvent event) throws IOException {
		crearAnadirReservaProfesor();
		anadirReserva.showAndWait();
	}

	@FXML
	void borrarReservaProfesor(ActionEvent event) {
		Reserva reserva = null;
		try {
			reserva = tvReservasProfesor.getSelectionModel().getSelectedItem();
			if (reserva != null && Dialogos.mostrarDialogoConfirmacion(BORRAR_RESERVA,
					"¿Estás seguro de que quieres borrar la reserva?", null)) {
				controladorMVC.anularReserva(reserva);
				reservas.remove(reserva);
				reservas.clear();
				actualizaProfesores();
				Dialogos.mostrarDialogoInformacion(BORRAR_RESERVA, "Reserva borrada exitosamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(BORRAR_RESERVA, e.getMessage());
		}
	}

	public void mostrarReservaProfesor(Profesor profesor) {
		try {
			if (profesor != null) {
				reservasProfesor.setAll(controladorMVC.getReservasProfesor(profesor));
			}
		} catch (IllegalArgumentException e) {
			reservasProfesor.setAll(FXCollections.observableArrayList());
		}
		actualizaReservas();
	}

	private void crearAnadirReservaProfesor() throws IOException {
		if (anadirReserva == null) {
			anadirReserva = new Stage();
			FXMLLoader cargadorAnadirReserva = new FXMLLoader(
					LocalizadorRecursos.class.getResource("vistas/AnadirReserva.fxml"));
			VBox raizAnadirReserva = cargadorAnadirReserva.load();
			cAnadirReserva = cargadorAnadirReserva.getController();
			cAnadirReserva.setControladorMVC(controladorMVC);
			cAnadirReserva.setProfesores(profesores);
			cAnadirReserva.setAulas(aulas);
			cAnadirReserva.setPadre(this);
			cAnadirReserva.inicializa();
			Scene escenaAnadirReserva = new Scene(raizAnadirReserva);
			anadirReserva.setTitle("Añadir Reserva");
			anadirReserva.initModality(Modality.APPLICATION_MODAL);
			anadirReserva.setScene(escenaAnadirReserva);
		} else {
			cAnadirReserva.setProfesoresBool(true);
			cAnadirReserva.setAulasBool(false);
			cAnadirReserva.setProfesores(profesores);
			cAnadirReserva.setAulas(aulas);
			cAnadirReserva.inicializa();
		}
	}

	@FXML
	void anadirReservaAula(ActionEvent event) {

	}

	@FXML
	void borrarReservaAula(ActionEvent event) {

	}

}

