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

public class ControllerRaices implements Initializable {
	String[][] encabezados = {
			{"Iteracion","X1","X2","X2-X1","XM","f(X1)","f(XM)","Error"},
			{"Iteracion","Xi","f(Xi)","Xi-X1","f(Xi)-f(X1)","Xi+1","Error","f(Xi+1)"},
			{"Iteracion","Xi","X(i+1)","f(Xi)","f(Xi+1)","X(i+2)","Error","f(Xi+2)"},
			{"Iteracion","Xi","f(Xi)","f'(i)","X(i+1)","Error"},
			{"Iteracion","Xi","g(Xi)","error","f(Xi)"},
			{"Iteracion","Xi","f(Xi)","f'(i)","f''(Xi)","X(i+1)","Error"}};
    @FXML
    private Label solucionLabel;
	@FXML
    private VBox inputContainerR;
    @FXML
    private StackPane graphContainer;
    //TABLA
    @FXML
    private TableView<Iteracion> tablaGraficas;
    		
    InputPane funcion = new InputPane("Funcion");
    InputPane x1 = new InputPane("x1");
    InputPane x2 = new InputPane("x2");
    InputPane errorPermisible = new InputPane("Error permisible");
    Button send = new Button("Calcular");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		send.setPrefWidth(125);
		send.getStyleClass().add("input-boton-enviar");
		if (Main.cargarValores){
			funcion.input.setText((Main.MN.ultimaFunc.isEmpty())?"":Main.MN.ultimaFunc);
			x1.input.setText((Main.MN.ultimaX1!=-Double.MAX_VALUE)?Main.MN.ultimaX1+"":"");
			x2.input.setText((Main.MN.ultimaX2!=-Double.MAX_VALUE)?Main.MN.ultimaX2+"":"");
			errorPermisible.input.setText((Main.MN.errorPermisible!=-Double.MAX_VALUE)?Main.MN.errorPermisible+"":"");
		}
		inputContainerR.getChildren().add(funcion.cont);
		inputContainerR.getChildren().add(x1.cont);
		if(Main.escenaActual<3)
			inputContainerR.getChildren().add(x2.cont);
		inputContainerR.getChildren().add(errorPermisible.cont);
		Region temp = new Region();
			temp.setPrefHeight(Double.MAX_VALUE);
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
		
		for (int i=0;i<encabezados[Main.escenaActual].length;i++){
			TableColumn<Iteracion,Integer> temp = new TableColumn<>(encabezados[Main.escenaActual][i]);
			temp.setPrefWidth((int)(Main.scene.getWidth()/encabezados[Main.escenaActual].length));
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
				case 0:
					iteraciones = Main.MN.BI(Main.MN.ultimaX1,Main.MN.ultimaX2,solucionLabel);
					break;
				case 1:
					iteraciones = Main.MN.FP(Main.MN.ultimaX1,Main.MN.ultimaX2,solucionLabel);
					break;
				case 2:
					iteraciones = Main.MN.SE(Main.MN.ultimaX1,Main.MN.ultimaX2,solucionLabel);
					break;
				case 3:
					iteraciones = Main.MN.NR(Main.MN.ultimaX1,solucionLabel);
					break;
				case 4:
					iteraciones = Main.MN.AS(Main.MN.ultimaX1,solucionLabel);
					break;
				case 5:
					iteraciones = Main.MN.N20(Main.MN.ultimaX1,solucionLabel);
					break;
			}
			HacerTabla(iteraciones);
		}
	}
	private int Validar(){
		int valid = 1;
		//LEE FUNCION
		if (Main.MN.validarFun(funcion.input.getText())){
			funcion.EntradaValida();
			
			Graph G = new Graph(Main.MN.P,385,290);
			graphContainer.getChildren().clear();
			graphContainer.getChildren().add(G.plot);
			
			if (Main.escenaActual==3 || Main.escenaActual==5){	
				Main.MN.diff = Main.MN.Derivar(Main.MN.P, Main.MN.P.getTopNode());
				//G.Graficar(Main.MN.diff);
				//graphContainer.getChildren().add(G.plot);
				if (Main.escenaActual==5){
					Main.MN.diff2 = Main.MN.Derivar(Main.MN.diff, Main.MN.diff.getTopNode());
					//G.Graficar(Main.MN.diff2);
					//graphContainer.getChildren().add(G.plot);
				}
			}
			

		} else {
			funcion.EntradaInvalida("Funcion invalida, intente de nuevo");
			valid=0;
		}
		//LEE PUNTOS
		try{
			Main.MN.ultimaX1 = Double.parseDouble(x1.input.getText());
			x1.EntradaValida();
		} catch (java.lang.NumberFormatException e) {
			x1.EntradaInvalida("Entrada invalida, inserte un numero");
			valid=0;
		}
		try{
			Main.MN.ultimaX2 = Double.parseDouble(x2.input.getText());
			x2.EntradaValida();
		} catch (java.lang.NumberFormatException e) {
			x2.EntradaInvalida("Entrada invalida, inserte un numero");
			valid=0;
		}
		if (Main.escenaActual==0 || Main.escenaActual==1 && valid==1){
			if (!Main.MN.bolzano(Main.MN.ultimaX1, Main.MN.ultimaX2)){
				x1.EntradaInvalida("Intervalo Invalido");
				x2.EntradaInvalida("Intervalo Invalido");
				valid = 0;
			}
				
		}
		//LEE ERROR
		try{
			Main.MN.errorPermisible = Double.parseDouble(errorPermisible.input.getText());
			errorPermisible.EntradaValida();
		} catch (java.lang.NumberFormatException e) {
			errorPermisible.EntradaInvalida("Entrada invalida, inserte un numero");
			valid=0;
		}
		return valid;
	}
	

	
}
