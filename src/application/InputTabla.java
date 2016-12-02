package application;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class InputTabla {
	public final Pane tituloCont;
	public final Pane[] containers;
	public final TextField[] inputx;
	public final TextField[] inputy;
	
	public InputTabla(int n,VBox containerVbox){

		 tituloCont = new Pane();
		 //cont.setPrefWidth(411);
		 tituloCont.setPrefHeight(VBox.USE_COMPUTED_SIZE);
		 StackPane sp1 = new StackPane();
		 Label titulo1 = new Label("X");
		 sp1.setPrefWidth(195);
		 
		 StackPane sp2 = new StackPane();
		 Label titulo2 = new Label("Y");

		 sp2.setPrefWidth(195);
		 sp2.setLayoutX(200);	

		 titulo1.getStyleClass().add("input-title");
		 titulo2.getStyleClass().add("input-title");
		 
		 sp1.getChildren().add(titulo1);
		 sp2.getChildren().add(titulo2);
		 
		 tituloCont.getChildren().add(sp1);
		 tituloCont.getChildren().add(sp2);

		 containerVbox.getChildren().add(tituloCont);
		 
		 inputx = new TextField[n];
		 inputy = new TextField[n];
		 containers = new Pane[n];

		 for (int i=0;i<n;i++){
			 containers[i] = new Pane();
			 tituloCont.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			 
			 inputx[i] = new TextField();
			 inputx[i].getStyleClass().add("input-textfield");
			 inputx[i].getStyleClass().add("input-textfield");
			 inputx[i].setPrefWidth(195);
			 inputx[i].setLayoutX(5);
			 inputx[i].setPromptText("x"+(i+1));
			
			 inputy[i] = new TextField();
			 inputy[i].getStyleClass().add("input-textfield");
			 inputy[i].setPrefWidth(195);
			 inputy[i].setLayoutX(215);
			 inputy[i].setPromptText("y"+(i+1));

			 

			 containers[i].getChildren().add(inputx[i]);
			 containers[i].getChildren().add(inputy[i]);
			 containers[i].setPadding(new Insets(0, 0, 10, 0));
			 containerVbox.getChildren().add(containers[i]);
		 }

	}
	public void EntradaInvalida(int n,int xoy){
		if (xoy==0){
			inputx[n].getStyleClass().removeAll("wrong","right");
			inputx[n].getStyleClass().add("wrong");
		}
		else{
			inputy[n].getStyleClass().removeAll("wrong","right");
			inputy[n].getStyleClass().add("wrong");
		}
	}
	public void EntradaValida(int n,int xoy){
		if (xoy==0){
			inputx[n].getStyleClass().removeAll("wrong","right");
			inputx[n].getStyleClass().add("right");
		}
		else{
			inputy[n].getStyleClass().removeAll("wrong","right");
			inputy[n].getStyleClass().add("right");
		}
	}
}
