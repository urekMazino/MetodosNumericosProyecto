package application;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InputPaneSplit {
	public VBox cont;
	public final Label title1;
	public final Label title2;
	public final TextField input1;
	public final TextField input2;
	
	public InputPaneSplit(String titulo1,String titulo2){
		title1 = new Label(titulo1);
		title2 = new Label(titulo2);
		input1 = new TextField();
		input2 = new TextField();
		cont = new VBox();
		ini(titulo1,titulo2,"","");
	}
	public InputPaneSplit(String titulo1,String titulo2,String prompt1,String prompt2){
		title1 = new Label(titulo1);
		title2 = new Label(titulo2);
		input1 = new TextField();
		input2 = new TextField();
		cont = new VBox();
		ini(titulo1,titulo2,prompt1,prompt2);
		 
	}
	void ini(String titulo1,String titulo2,String prompt1,String prompt2){
		Pane titulos = new Pane();
		Pane inputs = new Pane();
		
		titulos.setPrefHeight(Pane.USE_COMPUTED_SIZE);
		inputs.setPrefHeight(Pane.USE_COMPUTED_SIZE);
		
		cont.setPrefWidth(411);
		cont.setPrefHeight(VBox.USE_COMPUTED_SIZE);
	 	cont.fillWidthProperty();
		 
		 
		title1.getStyleClass().add("input-title");
		title1.setPrefWidth(190);
		title2.getStyleClass().add("input-title");
		title2.setPrefWidth(190);
		title2.setLayoutX(200);
		titulos.getChildren().add(title1);
		titulos.getChildren().add(title2);
		
		input1.getStyleClass().add("input-textfield");
		input1.setPromptText(prompt1);
		input1.setPrefWidth(190);
		input2.getStyleClass().add("input-textfield");
		input2.setPromptText(prompt1);
		input2.setPrefWidth(190);
		input2.setLayoutX(200);
		inputs.getChildren().add(input1);
		inputs.getChildren().add(input2);
		
		cont.getChildren().add(titulos);
		cont.getChildren().add(inputs); 
		cont.setPadding(new Insets(0,0,15,0));
	}
	public void EntradaInvalida(int i){
		if (i==1)
			input1.setStyle("-fx-border-color:#e74c3c;");
		else
			input2.setStyle("-fx-border-color:#e74c3c;");
	}
	public void EntradaValida(int i){
		if (i==1)
			input1.setStyle("-fx-border-color:#2ecc71;");
		else
			input2.setStyle("-fx-border-color:#2ecc71;");
	}
}
