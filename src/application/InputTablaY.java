package application;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InputTablaY {
	public final Pane tituloCont;
	public final Pane[] containers;
	public final TextField[] inputy;
	
	public InputTablaY(int n,VBox containerVbox){

		 tituloCont = new Pane();
		 //cont.setPrefWidth(411);
		 tituloCont.setPrefHeight(VBox.USE_COMPUTED_SIZE);
		 StackPane sp1 = new StackPane();
		 Label titulo1 = new Label("Y");
		 sp1.setPrefWidth(195);
		 

		 titulo1.getStyleClass().add("input-title");
		 
		 sp1.getChildren().add(titulo1);
		 
		 tituloCont.getChildren().add(sp1);

		 containerVbox.getChildren().add(tituloCont);
		 
		 inputy = new TextField[n];
		 containers = new Pane[n];

		 for (int i=0;i<n;i++){
			 containers[i] = new Pane();
			 tituloCont.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			 
			 inputy[i] = new TextField();
			 inputy[i].getStyleClass().add("input-textfield");
			 inputy[i].setPrefWidth(195);
			 inputy[i].setLayoutX(5);
			 inputy[i].setPromptText("y"+(i+1));

			 

			 containers[i].getChildren().add(inputy[i]);
			 containers[i].setPadding(new Insets(0, 0, 10, 0));
			 containerVbox.getChildren().add(containers[i]);
		 }

	}
	public void EntradaInvalida(int n){
		inputy[n].getStyleClass().removeAll("wrong","right");
		inputy[n].getStyleClass().add("wrong");
	}
	public void EntradaValida(int n){
		inputy[n].getStyleClass().removeAll("wrong","right");
		inputy[n].getStyleClass().add("right");
	}
}
