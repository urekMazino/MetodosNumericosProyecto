package application;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SideMenu {
	public final VBox cont;
	public final Label[] botones;
	
	public SideMenu(String[] titulos,String cssClass){
		 cont = new VBox();
		 cont.setMinWidth(320);
		 cont.setPrefWidth(320);
		 cont.setPrefHeight(Main.height);
		 cont.setFillWidth(true);
		 
		 botones = new Label[titulos.length];
		 for (int i=0;i<titulos.length;i++){
			 botones[i] = new Label(titulos[i]);
			// botones[i].setWrapText(true);
			 botones[i].getStyleClass().add(cssClass);
			 botones[i].setMaxWidth(Double.MAX_VALUE);

			 cont.getChildren().add(botones[i]);
		 }
	}
	
}


