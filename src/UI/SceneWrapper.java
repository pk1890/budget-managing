package UI;

import DB.SESSION;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneWrapper {
     private static WindowsTypes wt;
     private static Stage primaryStage;

     private static Scene mainScene;
     private static Scene historyScene;
     private static Scene loginScene;


     public static void Initialize (Stage stage, Class c) throws IOException {
         wt = WindowsTypes.MAIN;
         primaryStage = stage;

         primaryStage.setTitle("Hello World");
         primaryStage.setMinHeight(600);
         primaryStage.setMinWidth(800);

         mainScene = new Scene(
                 FXMLLoader.load(c.getResource("Main.fxml")),
                 800,
                 600
         );

         historyScene = new Scene(
                 FXMLLoader.load(c.getResource("History.fxml")),
                 800,
                 600
         );

         loginScene = new Scene(
                 FXMLLoader.load(c.getResource("Login.fxml")),
                 600,
                 480
         );

     }

     public static void setWindow(WindowsTypes w){
         wt = w;
         if(SESSION.loggedUser != null) {
             switch (w) {
                 case MAIN:
                     primaryStage.setScene(mainScene);
                     break;
                 case HISTORY:
                     primaryStage.setScene(historyScene);
                     break;
             }
         }
         else{
                     primaryStage.setScene(loginScene);
         }
     }
}


