package UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneWrapper {
     private WindowsTypes wt;
     private Stage primaryStage;

     private final Scene mainScene;
     private final Scene historyScene;


     public SceneWrapper(Stage stage) throws IOException {
         wt = WindowsTypes.MAIN;
         primaryStage = stage;

         primaryStage.setTitle("Hello World");
         primaryStage.setMinHeight(600);
         primaryStage.setMinWidth(800);

         mainScene = new Scene(
                 FXMLLoader.load(getClass().getResource("Main.fxml")),
                 800,
                 600
         );

         historyScene = new Scene(
                 FXMLLoader.load(getClass().getResource("History.fxml")),
                 800,
                 600
         );
     }

     public void setWindow(WindowsTypes w){
         wt = w;
         switch (w){
             case MAIN:
                 primaryStage.setScene(mainScene);
                 break;
             case HISTORY:
                 primaryStage.setScene(historyScene);
                 break;

         }
     }
}


