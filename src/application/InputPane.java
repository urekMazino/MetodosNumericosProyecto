package application;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InputPane {
	public VBox cont;
	public final Label title;
	public final TextField input;
	public final Label error;
	
	public InputPane(String titulo){
		error = new Label();
		title = new Label(titulo);
		input = new TextField();
		cont = new VBox();
		ini(titulo,"");
	}
	public InputPane(String titulo,String prompt){
		error = new Label();
		title = new Label(titulo);
		input = new TextField();
		cont = new VBox();
		ini(titulo,prompt);
		 
	}
	void ini(String titulo,String prompt){

		 cont.setPrefHeight(VBox.USE_COMPUTED_SIZE);
		 cont.setFillWidth(true);
		 
		 title.getStyleClass().add("input-title");
		 cont.getChildren().add(title);
		 

		 input.getStyleClass().add("input-textfield");
		 input.setPromptText(prompt);
		 input.setMinHeight(42);
		 cont.getChildren().add(input);
		 

		 error.getStyleClass().add("input-error");
		 cont.getChildren().add(error);
	}
	public void EntradaInvalida(String str){
		error.setText(str);
		input.getStyleClass().removeAll("wrong","right");
		input.getStyleClass().add("wrong");
	}
	public void EntradaValida(){
		error.setText("");
		input.getStyleClass().removeAll("wrong","right");
		input.getStyleClass().add("right");
	}
}
