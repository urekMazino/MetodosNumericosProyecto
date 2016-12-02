package application;
import java.io.IOException;
import java.net.URL;

import org.lsmp.djep.djep.DJep;

import java.util.*;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class Controller implements Initializable {
	private String[] sideMenuTitles = {"Raices","Sistemas","Interpolacion","Diferenciacion\ne Integracion","Ecuaciones"};
	private String[] nombreMetodos= {"Biseccion","Falsa Posicion","Secante","Newton-Rhapson","Aprox. Sucesivas","Newton 2ndo. Orden",
			"Gauss","Gauss-Jordan","Montante","Cramer","Jacobi","Gauss-Seidel",
			"Diferencias Finitas","Newton","LaGrange","Min. Cuadrados",
			"Por Limites","Trapecio(tabla)","Trapecio(funcion)","Simpson(tabla)","Simpson(funcion)",
			"Euler","Euler-Gauss"};
	private Rango[] rangos = {new Rango(0,6),new Rango(6,12),new Rango(12,16),new Rango(16,21),new Rango(21,23)};
	private Map<String,Integer> nombresAInt =  new HashMap<String,Integer>();
	private	Map<String,Label> botonesMetodos= new HashMap<String,Label>();
	@FXML
	private HBox menuContainer;
    @FXML
    private AnchorPane AP;
    @FXML
    private Label titulo;
    @FXML
    private Button button;
    @FXML
    private HBox scrollInside;
    @FXML
    public StackPane escena;
    
    AnchorPane escenaActual;
    
    private Region spacer;
    SideMenu sideMenu1; 
    SideMenu sideMenu2;
    
    Boolean menu2open = false;
	
    public void HideMenu(){  
         AP.getChildren().remove(menuContainer);
         if (menu2open)
        	 HideMenu2();
	}
	public void ShowMenu(){
		AP.getChildren().add(menuContainer);
		FadeTransition showFileRootTransition = new FadeTransition(Duration.millis(200), menuContainer);
		showFileRootTransition.setFromValue(0.0);
		showFileRootTransition.setToValue(1.0);
		showFileRootTransition.play();
	}
	private void ShowMenu2(int a,int b){

		if (menu2open){
			sideMenu2.cont.getChildren().clear();

		} else {
			menuContainer.getChildren().remove(spacer);
			menuContainer.getChildren().add(sideMenu2.cont);
			menuContainer.getChildren().add(spacer);
			FadeTransition showFileRootTransition = new FadeTransition(Duration.millis(200), sideMenu2.cont);
			showFileRootTransition.setFromValue(0.0);
			showFileRootTransition.setToValue(1.0);
			showFileRootTransition.play();
		}
		for (int i =a;i<b;i++){
			sideMenu2.cont.getChildren().add(botonesMetodos.get(nombreMetodos[i]));
		}

		menu2open = true;
	}
	public void HideMenu2(){
		sideMenu2.cont.getChildren().clear();
		menuContainer.getChildren().remove(sideMenu2.cont);
		menu2open = false;
	}
	
	public void AbrirEscena(String str){
		HideMenu();
		try{
			titulo.setText(str);
			Main.escenaActual= nombresAInt.get((str));
			escenaActual = null;
			if (Main.escenaActual>=rangos[0].x1 && Main.escenaActual<rangos[0].x2 ){
				escenaActual = (AnchorPane)FXMLLoader.load(getClass().getResource("fxmlRaices.fxml"));	
			} else if (Main.escenaActual>=rangos[1].x1 && Main.escenaActual<rangos[1].x2 ){
				escenaActual = (AnchorPane)FXMLLoader.load(getClass().getResource("fxmlSistemas.fxml"));	
			} else if (Main.escenaActual>=rangos[2].x1 && Main.escenaActual<rangos[2].x2 ){
				escenaActual = (AnchorPane)FXMLLoader.load(getClass().getResource("fxmlInter.fxml"));	
			} else if (Main.escenaActual>=rangos[3].x1 && Main.escenaActual<rangos[3].x2 ){
				if (Main.escenaActual==16)
					escenaActual = (AnchorPane)FXMLLoader.load(getClass().getResource("fxmlDif.fxml"));	
				else 
					escenaActual = (AnchorPane)FXMLLoader.load(getClass().getResource("fxmlDifInt.fxml"));	
			} else if (Main.escenaActual>=rangos[4].x1 && Main.escenaActual<rangos[4].x2 ){
				escenaActual = (AnchorPane)FXMLLoader.load(getClass().getResource("fxmlEcuaciones.fxml"));	
			}
			ChangeScene(escenaActual);
			
		}catch (IOException e){	
			
		}
	}
	
	

	private void ChangeScene(AnchorPane node){
		escena.getChildren().clear();
		escena.getChildren().add(node);
		FadeTransition showFileRootTransition = new FadeTransition(Duration.millis(600), node);
		showFileRootTransition.setFromValue(0.0);
		showFileRootTransition.setToValue(1.0);
		showFileRootTransition.play();
	}
	
	private void InitMenu(){
		sideMenu1 = new SideMenu(sideMenuTitles,"side-menu-label1");
		sideMenu1.cont.getStyleClass().add("side-menu1");

		Region temp = new Region();
		temp.setPrefWidth(Main.width);
		//temp.setPrefHeight(Main.height);
		temp.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event){HideMenu2();}});
		
		sideMenu1.cont.getChildren().add(temp);
		sideMenu1.cont.setVgrow(temp, Priority.ALWAYS);
		menuContainer.getChildren().add(sideMenu1.cont);
		
		spacer = new Region();
		spacer.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event){HideMenu();}});
		spacer.setPrefWidth(Main.width);
		spacer.setPrefHeight(Main.height);
		menuContainer.getChildren().add(spacer);
		sideMenu2 = new SideMenu(new String[0],"");
		sideMenu2.cont.getStyleClass().add("side-menu2");
		HideMenu();
		
	}
	private void initBotones(){
		sideMenu1.botones[0].setOnMouseEntered(
				new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event){
						ShowMenu2(rangos[0].x1,rangos[0].x2);
					}
				});
		sideMenu1.botones[1].setOnMouseEntered(new EventHandler<MouseEvent>(){@Override
			public void handle(MouseEvent event){ShowMenu2(rangos[1].x1,rangos[1].x2);}});
		sideMenu1.botones[2].setOnMouseEntered(new EventHandler<MouseEvent>(){@Override
			public void handle(MouseEvent event){ShowMenu2(rangos[2].x1,rangos[2].x2);}});
		sideMenu1.botones[3].setOnMouseEntered(new EventHandler<MouseEvent>(){@Override
			public void handle(MouseEvent event){ShowMenu2(rangos[3].x1,rangos[3].x2);}});
		sideMenu1.botones[4].setOnMouseEntered(new EventHandler<MouseEvent>(){@Override
			public void handle(MouseEvent event){ShowMenu2(rangos[4].x1,rangos[4].x2);}});
		
		for (int i=0;i<nombreMetodos.length;i++){
			Label temp = new Label(nombreMetodos[i]);
			temp.setPrefWidth(1000);
			//temp.setPrefHeight(79);
			temp.setAlignment(Pos.CENTER_LEFT);
			temp.getStyleClass().add("side-menu-label2");
			botonesMetodos.put(nombreMetodos[i],temp);
		}
		for(int j=0;j<nombreMetodos.length;j++){
			botonesMetodos.get(nombreMetodos[j]).setOnMouseClicked(new EventHandler<MouseEvent>(){@Override
			public void handle(MouseEvent event){
				String str ="";
				for (int i=0;i<nombreMetodos.length;i++){
					if(botonesMetodos.get(nombreMetodos[i])==event.getSource())
						str = nombreMetodos[i];
				}
				AbrirEscena(str);
			}});
		}

	}
	
	public void addLabel(){
		scrollInside.getChildren().add(new Label("test"));
	}
	public DJep InitParser(){
		DJep Q = new DJep();
		Q.addStandardConstants();
		Q.addStandardFunctions();
		Q.addVariable("x", 0);
		Q.setImplicitMul(true);
		Q.addComplex();
		Q.getAllowUndeclared();
		Q.getAllowAssignment();
		Q.addStandardDiffRules();
	
		return Q;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){

		for (int i=0;i<nombreMetodos.length;i++){
			nombresAInt.put(nombreMetodos[i],i);
		}
		InitMenu();
		initBotones();
	}
}

class Rango{
	public int x1;
	public int x2;
	public Rango(int a,int b){
		x1=a;
		x2=b;
	}
}