package UI;

import DB.Session;
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


    private static IOnLoad mainController;
     private static IOnLoad historyController;
     private static IOnLoad loginController;


    static void Initialize(Stage stage, Class c) throws IOException {
         wt = WindowsTypes.MAIN;
         primaryStage = stage;

         primaryStage.setTitle("Hello World");
         primaryStage.setMinHeight(600);
         primaryStage.setMinWidth(800);

        FXMLLoader mainLoader = new FXMLLoader(c.getResource("Main.fxml"));
        FXMLLoader historyLoader = new FXMLLoader(c.getResource("History.fxml"));
        FXMLLoader loginLoader = new FXMLLoader(c.getResource("Login.fxml"));


         mainScene = new Scene(
                 mainLoader.load(),
                 800,
                 600
         );

         historyScene = new Scene(
                 historyLoader.load(),
                 800,
                 600
         );

         loginScene = new Scene(
                 loginLoader.load(),
                 600,
                 480
         );

        mainController =  mainLoader.getController();
        historyController = historyLoader.getController();
        loginController = loginLoader.getController();

    }

     static void setWindow(WindowsTypes w){
         wt = w;
         if(Session.getLoggedUser() != null) {
             switch (w) {
                 case MAIN:
                     primaryStage.setScene(mainScene);
                     mainController.onLoad();
                     break;
                 case HISTORY:
                     primaryStage.setScene(historyScene);
                     historyController.onLoad();
                     break;
             }
         }
         else{
                     primaryStage.setScene(loginScene);
                     loginController.onLoad();
         }
     }

}


