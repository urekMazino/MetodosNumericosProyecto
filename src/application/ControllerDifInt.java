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

public class ControllerDifInt implements Initializable {
	String[][] encabezados = {
			{"Iteracion","h","k","x+h","f(x+h)","f(x)","f(x+h)-f(x)","error"},
			{"Iteracion","y","m","m*y"},
			{"Iteracion","x","y","m","m*y"},
			{"Iteracion","y","m","m*y"},
			{"Iteracion","x","y","m","m*y"}};
    @FXML
    private Label solucionLabel;
	@FXML
    private VBox inputContainerR;
	@FXML
    private VBox inputContainerA;
	@FXML
    private ScrollPane scrollPane;
    @FXML
    private StackPane graphContainer;
    @FXML
    private Pane graphPane;
    @FXML
    private VBox rightPane;
    //TABLA
    @FXML
    private TableView<Iteracion> tablaGraficas;
    
    Boolean tablaGenerada = false;
    int nValores = 0;
    double valorBuscado = 0;
    double h = 0;
    int n = 0;
    InputPane funcion = new InputPane("Funcion");
    InputPane NumeroValores = new InputPane("Numero de Valores");
    InputTablaY tablaInputsY;
    InputPane H = new InputPane("Valor de h");
    InputPane N = new InputPane("Valor de n");
    InputPane xBuscada = new InputPane("x a diferenciar"); 
    InputPane x1 = new InputPane("Valor de a");
    InputPane x2 = new InputPane("Valor de b");
    InputPane errorPermisible = new InputPane("Error permisible");
    Button send = new Button("Calcular");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		send.setPrefWidth(125);
		send.getStyleClass().add("input-boton-enviar");
		send.setOnMouseClicked(new EventHandler<MouseEvent>(){@Override
			public void handle(MouseEvent event){Enviar();}});
		if (Main.cargarValores){
			funcion.input.setText((Main.MN.ultimaFunc.isEmpty())?"":Main.MN.ultimaFunc);
			xBuscada.input.setText((Main.MN.ultimaX!=-Double.MAX_VALUE)?Main.MN.ultimaX+"":"");
			x1.input.setText((Main.MN.ultimaX1!=-Double.MAX_VALUE)?Main.MN.ultimaX1+"":"");
			x2.input.setText((Main.MN.ultimaX2!=-Double.MAX_VALUE)?Main.MN.ultimaX2+"":"");
			errorPermisible.input.setText((Main.MN.errorPermisible!=-Double.MAX_VALUE)?Main.MN.errorPermisible+"":"");
		}
		
		inputContainerA.getChildren().clear();
		if (Main.escenaActual==16 || Main.escenaActual==18 ||  Main.escenaActual==20){
			inputContainerA.getChildren().add(funcion.cont);
			if (Main.escenaActual==16){
				inputContainerA.getChildren().add(xBuscada.cont);
				inputContainerA.getChildren().add(errorPermisible.cont);
			} else {
				inputContainerA.getChildren().add(x1.cont);
				inputContainerA.getChildren().add(x2.cont);
				inputContainerA.getChildren().add(N.cont);
				//rightPane.getChildren().remove(tablaGraficas);
			}
		} else {
			inputContainerA.getChildren().add(NumeroValores.cont);
			//inputContainerA.getChildren().add(H.cont);
			if (Main.cargarValores){
				NumeroValores.input.setText((Main.MN.valoresx != null && Main.MN.valoresx.length>0)?Main.MN.valoresx.length+"":"");
				if (Main.MN.valoresx != null && Main.MN.valoresx.length>0){
					ValidarNumeroValores();
				}
			}
			rightPane.getChildren().clear();
			Label tempLabel = new Label("Solucion");
			tempLabel.getStyleClass().add("label-titulo");
			rightPane.getChildren().add(tempLabel);
			rightPane.getChildren().add(solucionLabel);
			rightPane.getChildren().add(tablaGraficas);
		}
		inputContainerA.getChildren().add(send);
		inputContainerA.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				  if (event.getCode() == KeyCode.ENTER){
					  Enviar();
					  }
					}
		});
		if(Main.escenaActual==16 || Main.escenaActual==18 ||  Main.escenaActual==20){

		} else {
			inputContainerR.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					  if (event.getCode() == KeyCode.ENTER){
						  Enviar();
						  }
						}
			});
		}
		
		
	}
	public void ValidarNumeroValores(){
		

		
		if (ValidarNValores()==1){
			HacerTablaEntrada();
		}
	}
	int ValidarNValores(){
		int valid=1;
		try{
			nValores = Integer.parseInt(NumeroValores.input.getText());
			if (nValores>0 && (nValores%2==1 || Main.escenaActual!=19))
				NumeroValores.EntradaValida();
			else {
				NumeroValores.EntradaInvalida("Ingrese numero impar positivo");
				valid=0;
			}
		} catch (java.lang.NumberFormatException e) {
			NumeroValores.EntradaInvalida("Ingrese numero entero");
			valid=0;
		}
		return valid;
	}
	void HacerTablaEntrada(){	
		tablaGenerada=true;
		inputContainerA.getChildren().remove(scrollPane);
		inputContainerA.getChildren().remove(H.cont);
		inputContainerR.getChildren().clear();
		inputContainerA.getChildren().add(1,H.cont);
		tablaInputsY = new InputTablaY(nValores,inputContainerR);
		if (Main.cargarValores){
			if ((Main.MN.valoresy2 != null && Main.MN.valoresy2.length==nValores))
				for(int i=0;i<nValores;i++){
					tablaInputsY.inputy[i].setText(Main.MN.valoresy2[i]+"");
				}
		}
		inputContainerA.getChildren().add(2,scrollPane);
	}
	void HacerTabla(ObservableList<Iteracion> iteraciones){
		tablaGraficas.getItems().clear();
		tablaGraficas.getColumns().clear();
		tablaGraficas.setItems(iteraciones);
		for (int i=0;i<encabezados[Main.escenaActual-16].length;i++){
			TableColumn<Iteracion,Integer> temp = new TableColumn<>(encabezados[Main.escenaActual-16][i]);
			temp.setPrefWidth((int)Math.floor((tablaGraficas.getWidth()-5)/encabezados[Main.escenaActual-16].length));
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
				case 16:
					iteraciones = Main.MN.DDF(valorBuscado,solucionLabel);
					break;
				case 17:
					iteraciones = Main.MN.ITtabla(h,solucionLabel);
					break;
				case 18:
					iteraciones = Main.MN.IT(Main.MN.ultimaX1,Main.MN.ultimaX2,n,solucionLabel);
					break;
				case 19:
					iteraciones = Main.MN.IStabla(h,solucionLabel);
					break;
				case 20:
					iteraciones = Main.MN.IS(Main.MN.ultimaX1,Main.MN.ultimaX2,n,solucionLabel);
					break;
			}
				HacerTabla(iteraciones);
		}
	}
	private int Validar(){
		int valid = 1;
		//LEE FUNCION
		ValidarNValores();
		if ((tablaGenerada == false || nValores!=tablaInputsY.inputy.length) && (Main.escenaActual==17 || Main.escenaActual==19)){
			ValidarNumeroValores();
			return 0;
		}
		if (Main.escenaActual==16 || Main.escenaActual==18 ||  Main.escenaActual==20){
			if (Main.MN.validarFun(funcion.input.getText())){
				funcion.EntradaValida();
				
				Graph G = new Graph(Main.MN.P,385,290);
				graphContainer.getChildren().clear();
				graphContainer.getChildren().add(G.plot);
				
				/*if (Main.escenaActual==3 || Main.escenaActual==5){	
					Main.MN.diff = Main.MN.Derivar(Main.MN.P, Main.MN.P.getTopNode());
					G.Graficar(Main.MN.diff);
					graphContainer.getChildren().add(G.plot);
					if (Main.escenaActual==5){
						Main.MN.diff2 = Main.MN.Derivar(Main.MN.diff, Main.MN.diff.getTopNode());
						G.Graficar(Main.MN.diff2);
						graphContainer.getChildren().add(G.plot);
					}
				}*/
				
	
			} else {
				funcion.EntradaInvalida("Funcion invalida, intente de nuevo");
				valid=0;
			}
			
			if (Main.escenaActual==16){
				try{
					valorBuscado = Double.parseDouble(xBuscada.input.getText());
					xBuscada.EntradaValida();
				} catch (java.lang.NumberFormatException e) {
					xBuscada.EntradaInvalida("Entrada invalida, inserte un numero");
					valid=0;
				}
				try{
					Main.MN.errorPermisible = Double.parseDouble(errorPermisible.input.getText());
					errorPermisible.EntradaValida();
				} catch (java.lang.NumberFormatException e) {
					errorPermisible.EntradaInvalida("Entrada invalida, inserte un numero");
					valid=0;
				}
			}
			
		}
		//LEE PUNTOS
		if (Main.escenaActual==18 || Main.escenaActual==20){
			try{
				n = Integer.parseInt(N.input.getText());
				if (n>0 && (n%2==1 || Main.escenaActual!=20))
					N.EntradaValida();
				else{
					N.EntradaInvalida("Ingrese un numero impar positivo");
					valid=0;
				}
			} catch (java.lang.NumberFormatException e) {
				N.EntradaInvalida("Ingrese un numero entero positivo");
				valid=0;
			}
			try{
				Main.MN.ultimaX1 = Double.parseDouble(x1.input.getText());
				x1.EntradaValida();
			} catch (java.lang.NumberFormatException e) {
				x1.EntradaInvalida("Ingrese un numero");
				valid=0;
			}
			try{
				Main.MN.ultimaX2 = Double.parseDouble(x2.input.getText());
				x2.EntradaValida();
			} catch (java.lang.NumberFormatException e) {
				x2.EntradaInvalida("Ingrese un numero");
				valid=0;
			}
		}
		//LEE ERROR

		if (Main.escenaActual==19 || Main.escenaActual==17){
			
			try{
				h = Double.parseDouble(H.input.getText());
				H.EntradaValida();
			} catch (java.lang.NumberFormatException e) {
				H.EntradaInvalida("Valor invalido,Ingrese un numero");
				valid=0;
			}
			
			Main.MN.valoresy2 = new double[nValores];
			for(int i=0;i<nValores;i++){
				try{
					Main.MN.valoresy2[i]= Double.parseDouble(tablaInputsY.inputy[i].getText());
					tablaInputsY.EntradaValida(i);
				} catch(java.lang.NumberFormatException e){
					tablaInputsY.EntradaInvalida(i);
					valid=0;
				}
			}
		}
		return valid;
	}
	

	
}
