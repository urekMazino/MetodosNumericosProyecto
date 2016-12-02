package application;


import java.net.URL;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerEcuaciones implements Initializable {
	String[] encabezado = {"Iteracion","X","Y"};
    @FXML
    private Label solucionLabel;
	@FXML
    private VBox inputContainerR;
    @FXML
    private StackPane graphContainer;
    //TABLA
    @FXML
    private TableView<Iteracion> tablaGraficas;
    double xValor = 0;
    double yValor = 0;	
    double nValor = 0;
    double hValor = 0;
    InputPane funcion = new InputPane("Funcion");
    InputPane x1 = new InputPane("Xo condicion inicial");
    InputPane x2 = new InputPane("Yo condicion inicial");
    InputPane N = new InputPane("Valor de n");
    InputPane H = new InputPane("Valor de h");
    Button send = new Button("Calcular");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		send.setPrefWidth(125);
		send.getStyleClass().add("input-boton-enviar");
		if (Main.cargarValores)
			funcion.input.setText((Main.MN.ultimaFuncY.isEmpty())?"":Main.MN.ultimaFuncY);
		
		inputContainerR.getChildren().add(funcion.cont);
		inputContainerR.getChildren().add(x1.cont);
		inputContainerR.getChildren().add(x2.cont);
		inputContainerR.getChildren().add(N.cont);
		inputContainerR.getChildren().add(H.cont);
		Region temp = new Region();
		temp.setPrefHeight(Main.height);
		inputContainerR.getChildren().add(temp);
		send.setOnMouseClicked(new EventHandler<MouseEvent>(){@Override
			public void handle(MouseEvent event){Enviar();}});
		inputContainerR.getChildren().add(send);
		inputContainerR.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				  if (event.getCode() == KeyCode.ENTER){
					  Enviar();
					  }
					}
		});
		
	}
	void HacerTabla(ObservableList<Iteracion> iteraciones){
		tablaGraficas.getItems().clear();
		tablaGraficas.getColumns().clear();
		tablaGraficas.setItems(iteraciones);
		
		for (int i=0;i<encabezado.length;i++){
			TableColumn<Iteracion,Integer> temp = new TableColumn<>(encabezado[i]);
			temp.setPrefWidth((int)(410/encabezado.length));
			String str = "";
			if(i==0)
				str = "i";
			else
				str = "val"+i;
			temp.setCellValueFactory(new PropertyValueFactory<Iteracion,Integer>(str));
			tablaGraficas.getColumns().add(temp);
		}
		
	}
	public void Enviar(){
		
		if (Validar()==1){
			solucionLabel.setText("");
			ObservableList<Iteracion> iteraciones = FXCollections.observableArrayList();
			switch(Main.escenaActual){
				case 21:
					iteraciones = Main.MN.ME(xValor,yValor,nValor,hValor,solucionLabel);
					break;
				case 22:
					iteraciones = Main.MN.MEG(xValor,yValor,nValor,hValor,solucionLabel);
					break;
			}
			HacerTabla(iteraciones);
		}
	}
	private int Validar(){
		int valid = 1;
		//LEE FUNCION
		if (Main.MN.validarFunY(funcion.input.getText())){
			funcion.EntradaValida();	
		} else {
			funcion.EntradaInvalida("Funcion invalida, intente de nuevo");
			valid=0;
		}
		//LEE PUNTOS
		try{
			xValor = Double.parseDouble(x1.input.getText());
			x1.EntradaValida();
		} catch (java.lang.NumberFormatException e) {
			x1.EntradaInvalida("Entrada invalida, inserte un numero");
			valid=0;
		}
		try{
			yValor = Double.parseDouble(x2.input.getText());
			x2.EntradaValida();
		} catch (java.lang.NumberFormatException e) {
			x2.EntradaInvalida("Entrada invalida, inserte un numero");
			valid=0;
		}
		
		try{
			nValor = Double.parseDouble(N.input.getText());
			N.EntradaValida();
		} catch (java.lang.NumberFormatException e) {
			N.EntradaInvalida("Entrada invalida, inserte un numero");
			valid=0;
		}
		
		try{
			hValor = Double.parseDouble(H.input.getText());
			H.EntradaValida();
		} catch (java.lang.NumberFormatException e) {
			H.EntradaInvalida("Entrada invalida, inserte un numero");
			valid=0;
		}
		
		return valid;
	}
	

	
}
