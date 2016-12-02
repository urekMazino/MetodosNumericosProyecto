package application;


import java.net.URL;

import java.util.*;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class ControllerInter implements Initializable {
    @FXML
    private Label solucionLabel;
	@FXML
    private VBox inputContainerR;
    @FXML
    private StackPane graphContainer;
    @FXML
    private VBox graphPane;
    @FXML
    private VBox graphPaneContainer;
    @FXML
    private VBox inputContainerA;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox solucionInter;
    @FXML
    private VBox solucionInterContainer;
    @FXML
    private HBox regresarBarra;
    @FXML
    private VBox solucionContainer;

    Boolean solucionAbierta = false;
    Boolean tablaGenerada = false;
    InputTabla tablaInputs;
    InputPane NumeroValores = new InputPane("Numero de Valores");
    InputPane ValorBuscado = new InputPane("Valor Buscado");
    InputPane GradoF = new InputPane("Grado de la funcion");
    Button send = new Button("Calcular");
    
	int nValores = 5;
	double xBuscado = 0;
	int gradoBuscado =0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		send.setPrefWidth(125);
		send.getStyleClass().add("input-boton-enviar");
		send.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event){
				Enviar();
			}
		});
		
		solucionInter.getChildren().remove(regresarBarra);
		solucionInter.setVisible(false);
		
		inputContainerA.getChildren().add(0,NumeroValores.cont);
		inputContainerA.getChildren().add(1,ValorBuscado.cont);
		
		if (Main.cargarValores)
			ValorBuscado.input.setText((Main.MN.ultimaX!=-Double.MAX_VALUE)?Main.MN.ultimaX+"":"");
		
		if (Main.escenaActual==13 || Main.escenaActual==14){
			graphPaneContainer.getChildren().remove(graphPane);
		}else if (Main.escenaActual==15){
			inputContainerA.getChildren().add(1,GradoF.cont);
		}
		//inputContainerR.getChildren().add(send);
		
		NumeroValores.input.setMinHeight(42);
		if (Main.cargarValores){
			NumeroValores.input.setText((Main.MN.valoresx != null && Main.MN.valoresx.length>0)?Main.MN.valoresx.length+"":"");
			if ((Main.MN.valoresx != null && Main.MN.valoresx.length>0)){
				ValidarNumeroValores();
			}
		}
		inputContainerR.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				  if (event.getCode() == KeyCode.ENTER){
					  Enviar();
					  }
					}
		});
		inputContainerA.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				  if (event.getCode() == KeyCode.ENTER){
					  Enviar();
					  }
					}
		});
		//inputContainerR.getChildren().add(NumeroValores.cont);
	}
	public void Regresar(){
		solucionAbierta = false;
		solucionInter.setVisible(false);
		solucionInter.getChildren().remove(regresarBarra);
		solucionInterContainer.getChildren().clear();
	}
	public void ValidarNumeroValores(){

		if (ValidarNValores()==1){
			tablaGenerada = true;
			ValorBuscado.input.requestFocus();
			hacerTabla();
		}
	}
	int ValidarNValores(){
		int valid=1;
		try{
			nValores = Integer.parseInt(NumeroValores.input.getText());
			if (nValores>0)
				NumeroValores.EntradaValida();
			else {
				NumeroValores.EntradaInvalida("Ingrese un numero entero mayor a cero");
				valid=0;
			}
		} catch (java.lang.NumberFormatException e) {
			NumeroValores.EntradaInvalida("Ingrese un numero entero mayor a cero");
			valid=0;
		}
		return valid;
	}
	void hacerTabla(){
		inputContainerA.getChildren().remove(send);
		inputContainerR.getChildren().clear();
		tablaInputs = new InputTabla(nValores,inputContainerR);
		if (Main.cargarValores){
			if ((Main.MN.valoresx!=null && Main.MN.valoresx.length==nValores))
				for(int i=0;i<nValores;i++){
					tablaInputs.inputx[i].setText(Main.MN.valoresx[i]+"");
					tablaInputs.inputy[i].setText(Main.MN.valoresy[i]+"");
				}
		}
		inputContainerA.getChildren().add(send);
	}
	void HacerTabla(ObservableList<Iteracion> iteraciones){
		/*tablaGraficas.getItems().clear();
		tablaGraficas.getColumns().clear();
		tablaGraficas.setItems(iteraciones);
		
		for (int i=0;i<encabezados[Main.escenaActual].length;i++){
			TableColumn<Iteracion,Integer> temp = new TableColumn<>(encabezados[Main.escenaActual][i]);
			temp.setPrefWidth((int)(900/encabezados[Main.escenaActual].length));
			String str = "";
			if(i==0)
				str = "i";
			else
				str = "val"+i;
			temp.setCellValueFactory(new PropertyValueFactory<Iteracion,Integer>(str));
			tablaGraficas.getColumns().add(temp);
		}
		*/
	}
	public void Enviar(){
		if (solucionAbierta)
			return;
		if (Validar()==1){
			String res="";
			switch(Main.escenaActual){
				case 12:
					res = Main.MN.IDF(solucionInterContainer,xBuscado,solucionLabel);
					break;
				case 13:
					res = Main.MN.IN(xBuscado,solucionInterContainer);
					break;
				case 14:
					res = Main.MN.IL(xBuscado,solucionInterContainer);
					break;
				case 15:
					res = Main.MN.IMC(gradoBuscado,xBuscado,solucionInterContainer,solucionLabel);
					break;
			}
			abrirSolucion();
			if(Main.escenaActual==12 || Main.escenaActual==15){
				Main.MN.P.parseExpression(res);
				Graph G = new Graph(Main.MN.P,385,290);
				graphContainer.getChildren().clear();
				graphContainer.getChildren().add(G.plot);
			}  else
				solucionLabel.setText(res);
			
		}
	}
	void abrirSolucion(){
		solucionAbierta = true;
		solucionInter.setVisible(true);
		
		FadeTransition showFileRootTransition = new FadeTransition(Duration.millis(400), solucionInter);
		showFileRootTransition.setFromValue(0.0);
		showFileRootTransition.setToValue(1.0);
		showFileRootTransition.play();

		solucionInter.getChildren().add(regresarBarra);
		regresarBarra.requestFocus();
	}
	private int Validar(){
		int valid = 1;
		ValidarNValores();
		if (tablaGenerada == false || nValores!=tablaInputs.inputx.length){
			ValidarNumeroValores();
			return 0;
		}
		
		try{
			xBuscado = Double.parseDouble(ValorBuscado.input.getText());
			ValorBuscado.EntradaValida();
		} catch(java.lang.NumberFormatException e){
			ValorBuscado.EntradaInvalida("Valor Invalido,Ingrese numero");
			valid=0;
		}
		
		if (Main.escenaActual==15){
			try{
				gradoBuscado = Integer.parseInt(GradoF.input.getText());
				if (gradoBuscado<=0){
					GradoF.EntradaInvalida("Ingrese entero mayor a 0");
					valid=0;
				}else 
					GradoF.EntradaValida();
			} catch(java.lang.NumberFormatException e){
				GradoF.EntradaInvalida("Valor Invalido,Ingrese numero");
				valid=0;
			}
		}
		Main.MN.valoresx = new double[nValores];
		Main.MN.valoresy = new double[nValores];
		for(int i=0;i<nValores;i++){
			try{
				Main.MN.valoresx[i]= Double.parseDouble(tablaInputs.inputx[i].getText());
				tablaInputs.EntradaValida(i, 0);
			} catch(java.lang.NumberFormatException e){
				tablaInputs.EntradaInvalida(i, 0);
				valid=0;
			}
			try{
				Main.MN.valoresy[i]= Double.parseDouble(tablaInputs.inputy[i].getText());
				tablaInputs.EntradaValida(i, 1);
			} catch(java.lang.NumberFormatException e){
				tablaInputs.EntradaInvalida(i, 1);
				valid=0;
			}
		}
		return valid;
	}
	

	
}
