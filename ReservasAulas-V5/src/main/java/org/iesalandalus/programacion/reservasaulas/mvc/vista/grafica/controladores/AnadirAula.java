package org.iesalandalus.programacion.reservasaulas.mvc.vista.grafica.controladores;
import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.grafica.utilidades.Dialogos;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnadirAula {

	private IControlador controladorMVC;
	private ObservableList<Aula> aulas;

	@FXML
	private TextField tfNombre;
	@FXML
	private TextField tfPuestos;
	@FXML
	private Button btAnadir;
	@FXML
	private Button btCancelar;

	@FXML
	private void initialize() {
		tfNombre.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(tfNombre));
		tfPuestos.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(tfPuestos));
	}

	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	public void setAulas(ObservableList<Aula> aulas) {
		this.aulas = aulas;
	}

	@FXML
	private void anadirAula() {
		Aula aula = null;
		try {
			aula = getAula();
			controladorMVC.insertarAula(aula);
			aulas.setAll(controladorMVC.getAulas());
			Stage propietario = ((Stage) btAnadir.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir Aula", "Aula añadida exitosamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir Aula", e.getMessage());
		}
	}

	@FXML
	private void cancelar() {
		((Stage) btCancelar.getScene().getWindow()).close();
	}

	public void inicializa() {
		tfNombre.setText("");
		compruebaCampoTexto(tfNombre);
		tfPuestos.setText("");
		compruebaCampoTexto(tfPuestos);
	}

	private void compruebaCampoTexto(TextField campoTexto) {
		String texto = campoTexto.getText();
		if (!texto.isEmpty()) {
			campoTexto.setStyle("-fx-border-color: green; -fx-border-radius: 5;");
		} else {
			campoTexto.setStyle("-fx-border-color: red; -fx-border-radius: 5;");
		}
	}

	private Aula getAula() {
		String nombre = tfNombre.getText();
		int puestos = Integer.valueOf(tfPuestos.getText());
		return new Aula(nombre, puestos);
	}
}
