package application;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Font;


public class Main extends Application {

	static int width = 900;
	static int height = 700; 
	static Scene scene;
	static MetodosNumericos MN = new MetodosNumericos();
	static int escenaActual = -1;
	static int maxMatriz = 10;
	static Boolean cargarValores = true;
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
			Font.loadFont(getClass().getResourceAsStream("Fonts/Montserrat-Light.otf"), 14);
			Font.loadFont(getClass().getResourceAsStream("file:Fonts/Montserrat-UltraLight.otf"), 14);
			Parent root = FXMLLoader.load(getClass().getResource("fxmlmain.fxml"));
			
			scene = new Scene(root,width,height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);

			
			//primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		launch(args);
	}
}
