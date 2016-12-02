package application;


import java.net.URL;

import java.util.*;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class ControllerSis implements Initializable {
	String[] titulosMatrices = {"Matriz Triangular Superior","Matriz Identidad","Matriz final Montante","Cramer Determinantes","Jacobi","Gauss-Seidel"};
    @FXML
    private Label solucionLabel;
	@FXML
    private VBox inputContainerA;
    @FXML
    private VBox graphPaneContainer;
    @FXML
    private VBox inputContainerB;
    @FXML
    private ScrollPane scrollPane;
	@FXML
    private VBox solucionMatriz;
	@FXML
    private VBox solucionMatrizContainer;
 
    @FXML 
    private HBox regresarBarra;
   
    Boolean matrizGenerada = false;
    VBox solucionActual;
    VBox solucionBActual;
	InputPane numDatos = new InputPane("Numero de Incognitas");
	InputPane errorPerm = new InputPane("Error Permisible");
    InputMatriz inputMatriz;
	int nValores = 5;
	double xBuscado = 0;
	int gradoBuscado =0;
	

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		numDatos.input.setOnAction(new EventHandler<ActionEvent>() {@Override
		    public void handle(ActionEvent e) { Enviar();}});
		errorPerm.cont.setFillWidth(false);
		numDatos.cont.setFillWidth(false);
		numDatos.input.setPromptText("Enter para generar matriz");
		inputContainerA.getChildren().add(0,numDatos.cont);
		solucionMatriz.getChildren().remove(regresarBarra);
		solucionMatriz.setVisible(false);
		if (Main.escenaActual>9){
			inputContainerA.getChildren().add(1,errorPerm.cont);
		}
		if (Main.cargarValores){
			errorPerm.input.setText((Main.MN.errorPermisible!=-Double.MAX_VALUE)?Main.MN.errorPermisible+"":"");
			if (Main.MN.Matriz!=null){
				nValores = Main.MN.Matriz.length;
				numDatos.input.setText(nValores+"");
				ValidarNumeroValores();
			}
		}
		
		
		
		inputContainerB.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				  if (event.getCode() == KeyCode.ENTER){
					  Enviar();
					  }
					}
		});
		regresarBarra.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				  if (event.getCode() == KeyCode.ENTER){
					  Regresar();
					  }
					}
		});
		
	}
	
	public void Regresar(){
		solucionActual.setVisible(false);
		solucionActual.getChildren().remove(regresarBarra);
		solucionBActual.getChildren().clear();
	}
	
	public void ValidarNumeroValores(){
		if (ValidarNValores()==1){
			matrizGenerada = true;
			inputContainerB.getChildren().clear();
			inputMatriz = new InputMatriz(nValores,inputContainerB);
			if (Main.cargarValores){
				if (Main.MN.Matriz!=null && Main.MN.Matriz.length==nValores)
					inputMatriz.fill(Main.MN.Matriz);	
			}
			Main.MN.Matriz = new double[nValores][nValores+1];
			inputMatriz.inputs[0][0].requestFocus();
			
		}
	}
	int ValidarNValores(){
		int valid = 1;
		try{
			nValores = Integer.parseInt(numDatos.input.getText());
			if (nValores>0)
				if (nValores>15){
					numDatos.EntradaInvalida("Demasiado grande, intente menor a 15");
					valid=0;
				} else
					numDatos.EntradaValida();
			else {
				numDatos.EntradaInvalida("El numero tiene que ser mayor a 0");
				valid=0;
			}
		} catch (java.lang.NumberFormatException e) {
			numDatos.EntradaInvalida("Tiene que ser un numero entero");
			valid=0;
		}
		return valid;
	}
	public void Enviar(){
		
		if (Validar()==1){
			ImprimirSolucion(Main.MN.Matriz,"Matriz Original");
			switch(Main.escenaActual){
				case 6:
					solucionActual = solucionMatriz;
					solucionBActual = solucionMatrizContainer;
					ImprimirSolucion(Main.MN.Gauss(solucionLabel));
					break;
				case 7:
					solucionActual = solucionMatriz;
					solucionBActual = solucionMatrizContainer;
					ImprimirSolucion(Main.MN.GaussJordan(solucionLabel));
					break;
				case 8:
					solucionActual = solucionMatriz;
					solucionBActual = solucionMatrizContainer;
					ImprimirSolucion(Main.MN.Montante(solucionLabel));
					break;
				case 9:
					solucionActual = solucionMatriz;
					solucionBActual = solucionMatrizContainer;
					ImprimirCramer(Main.MN.Cramer(solucionLabel));
					break;
				case 10:
					solucionActual = solucionMatriz;
					solucionBActual = solucionMatrizContainer;
					ImprimirSolucionJacobi(Main.MN.Jacobi(solucionLabel));
					break;
				case 11:
					solucionActual = solucionMatriz;
					solucionBActual = solucionMatrizContainer;
					ImprimirSolucionJacobi(Main.MN.GaussSeidel(solucionLabel));
					break;
			}
			abrirSolucion();
		}
	}
	
	void abrirSolucion(){
		solucionActual.setVisible(true);
		
		FadeTransition showFileRootTransition = new FadeTransition(Duration.millis(400), solucionActual);
		showFileRootTransition.setFromValue(0.0);
		showFileRootTransition.setToValue(1.0);
		showFileRootTransition.play();

		solucionActual.getChildren().add(regresarBarra);
		regresarBarra.requestFocus();
	}
	
	private int Validar(){
		int valid = 1;
		//LEE FUNCION
		//LEE PUNTOS
		ValidarNValores();
		if (matrizGenerada == false || nValores!=inputMatriz.inputs.length){
			ValidarNumeroValores();
			return 0;
		}
		for (int i=0;i<nValores;i++){
			for (int j=0;j<=nValores;j++){
				try{
					Main.MN.Matriz[i][j] = Double.parseDouble(inputMatriz.inputs[i][j].getText());
					inputMatriz.EntradaValida(i,j);
				} catch(java.lang.NumberFormatException e){
					inputMatriz.EntradaInvalida(i,j);
					valid=0;
				}
			}
		}
		if (valid!=0 && Main.MN.Determinant(Main.MN.Matriz)==0){
			numDatos.EntradaInvalida("El sistema no tiene solucion");
			for (int i=0;i<nValores;i++){
				for (int j=0;j<=nValores;j++)
					inputMatriz.EntradaInvalida(i,j);
			}
			valid=0;
		}
		if(Main.escenaActual>9){
			try{
				Main.MN.errorPermisible = Double.parseDouble(errorPerm.input.getText());
				errorPerm.EntradaValida();
			} catch(java.lang.NumberFormatException e){
				errorPerm.EntradaInvalida("Ingrese un numero");
				valid=0;
			}
			if (valid!=0 && !Main.MN.TDD()){
				numDatos.EntradaInvalida("Teorema de la Diagonal Dominante no se cumple");
				for (int i=0;i<nValores;i++){
					for (int j=0;j<=nValores;j++)
						inputMatriz.EntradaInvalida(i,j);
				}
				valid=0;
			}
		}
		return valid;	
	}
	
	void ImprimirSolucion(double[][] M){
		ImprimirSolucion(M,titulosMatrices[Main.escenaActual-6]);
	}
	void ImprimirSolucion(double[][] M,String titulo){
		 Label title = new Label(titulo);
		 solucionMatrizContainer.getChildren().add(title);
		 title.getStyleClass().add("input-title");	
		 title.getStyleClass().add("larger-title");
		 Label space = new Label();
		 space.setPrefHeight(20);
		 solucionMatrizContainer.getChildren().add(space);
		 for (int i=0;i<M.length;i++){
			 HBox tempHbox = new HBox();
			 tempHbox.getStyleClass().add("matrix-row");
			 solucionMatrizContainer.getChildren().add( tempHbox);
			 for (int j=0;j<=M.length;j++){
				 Label tempLabel = new Label(M[i][j]+"");
				 tempLabel.getStyleClass().add("output-matriz-label");
				 tempLabel.setPrefWidth(125);
				 tempHbox.getChildren().add(tempLabel);
			 }
			 
		 }
	}
	void ImprimirCramer(double[] dets){
		 Label title = new Label("Cramer Determinantes");
		 solucionMatrizContainer.getChildren().add(title);
		 title.getStyleClass().add("input-title");	
		 title.getStyleClass().add("larger-title");

		 
		 Label detLabel = new Label("El determinante original es: "+dets[0]);
		 detLabel.getStyleClass().add("output-matriz-label");
		 detLabel.setPrefHeight(60);
		 solucionMatrizContainer.getChildren().add(detLabel);
		 for (int i=1;i<dets.length;i++){
			 Label tempLabel = new Label("El determinante DX"+(i)+" es: "+dets[i]);
			 tempLabel.getStyleClass().add("output-matriz-label");
			 tempLabel.setPrefHeight(60);
			 solucionMatrizContainer.getChildren().add(tempLabel);
			 
		 }
	}
	void ImprimirSolucionJacobi(ArrayList<double[]> M){
		 Label title = new Label(titulosMatrices[Main.escenaActual-6]);
		 solucionBActual.getChildren().add(title);
		 title.getStyleClass().add("input-title");	
		 title.getStyleClass().add("larger-title");
		 Label space = new Label();
		 space.setPrefHeight(20);
		 solucionBActual.getChildren().add(space);
		 
		 HBox tempHboxTitles = new HBox();
		 tempHboxTitles.getStyleClass().add("matrix-row");
		 Label tempLabeli = new Label("iteracion");
		 solucionBActual.getChildren().add( tempHboxTitles);
		 tempLabeli.getStyleClass().add("output-matriz-label");
		 tempLabeli.setPrefWidth(125);
		 tempHboxTitles.getChildren().add(tempLabeli);

		 for (int j=0;j<(Main.MN.Matriz.length*2);j++){
			 Label tempLabel = new Label("X"+((j%Main.MN.Matriz.length+1)));
			 tempLabel.getStyleClass().add("output-matriz-label");
			 tempLabel.setPrefWidth(125);
			 tempHboxTitles.getChildren().add(tempLabel);
		 }

		 for (int i=0;i<M.size();i++){
			 HBox tempHbox = new HBox();
			 tempHbox.getStyleClass().add("matrix-row");
			 solucionBActual.getChildren().add( tempHbox);
			 
			 Label tempIte = new Label((i+1)+"");
			 tempIte.getStyleClass().add("output-matriz-label");
			 tempIte.setPrefWidth(125);
			 tempHbox.getChildren().add(tempIte);
			 
			 for (int j=0;j<M.get(i).length;j++){
				 Label tempLabel = new Label(M.get(i)[j]+"");
				 tempLabel.getStyleClass().add("output-matriz-label");
				 tempLabel.setPrefWidth(125);
				 tempLabel.setEllipsisString("");
				 tempHbox.getChildren().add(tempLabel);
			 }
			 
		 }
	}
	
	
}
