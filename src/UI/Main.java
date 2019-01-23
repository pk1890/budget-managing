package UI;

import DB.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        SceneWrapper.Initialize(primaryStage, this.getClass());
        SceneWrapper.setWindow(WindowsTypes.MAIN);
        primaryStage.show();
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {

        SortedTransactionList res = new SortedTransactionList();
        Session.init();

        try {
            Session.getDb().addUser("mlekoo", "mleko123");
        } catch (AlreadyExistsException e) {
            System.out.println("Już jest taki user");
        }


        launch(args);
    }
}
