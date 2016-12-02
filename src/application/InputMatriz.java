package application;

import javafx.scene.control.*;
import javafx.scene.layout.*;


public class InputMatriz {
	public final Label title = new Label("Matriz Aumentada");
	public final HBox[] containers;
	public final TextField[][] inputs;
	double[][] M;
	Boolean gotM = false;
	public InputMatriz(int n,VBox cont,double[][] M){
		 containers = new HBox[n];
		 inputs = new TextField[n][n+1];
		 this.M = M;
		 gotM = true;
		 ini(n,cont);
	}
	public InputMatriz(int n,VBox cont){
		 containers = new HBox[n];
		 inputs = new TextField[n][n+1];
		 ini(n,cont);
	}
	public void fill(double[][] M){
		for (int i=0;i<M.length;i++){
			for(int j=0;j<M[i].length;j++)
				inputs[i][j].setText(M[i][j]+"");
		}
	}
	
	void ini(int n,VBox cont){
		cont.getChildren().add(title);
		 title.getStyleClass().add("input-title");	
		 Label space = new Label();
		 space.setPrefHeight(20);
		 cont.getChildren().add(space);

		 
		 for (int i=0;i<n;i++){
			 containers[i] = new HBox();
			 containers[i].getStyleClass().add("matrix-row");
			 containers[i].setSpacing(15);
			 cont.getChildren().add(containers[i]);
			 inputs[i] = new TextField[n+1];
			 for (int j=0;j<=n;j++){
				 inputs[i][j] = new TextField();
				 inputs[i][j].getStyleClass().add("input-textfield");
				 inputs[i][j].setPrefWidth(125);
				 if (gotM)
					 inputs[i][j].setText(M[i][j]+"");
				 
				 inputs[i][j].setPromptText((j<n)?("X"+(j+1)):("B"+(i+1)));
				 containers[i].getChildren().add(inputs[i][j]);
			 }
			 
		 }
	}
	
	
	public void EntradaInvalida(int i,int j){
		inputs[i][j].getStyleClass().removeAll("wrong","right");
		inputs[i][j].getStyleClass().add("wrong");

	}
	public void EntradaValida(int i,int j){
		inputs[i][j].getStyleClass().removeAll("wrong","right");
		inputs[i][j].getStyleClass().add("right");

	}
}
