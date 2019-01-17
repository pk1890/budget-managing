package UI;

import DB.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;
import java.sql.*;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("UI/History.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setMinHeight(600);
//        primaryStage.setMinWidth(800);
//        Scene mainScene = new Scene(root, 800, 600);
//        primaryStage.setScene(mainScene);

        SceneWrapper.Initialize(primaryStage, this.getClass());
        SceneWrapper.setWindow(WindowsTypes.MAIN);
        primaryStage.show();
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {

        TransactionList res = new TransactionList();
        SESSION.init();
//        try {
//            db = new DataBase();
//            res = db.getTransactions().getTransactionsToDate(new Date(
//                    2018,12,26
//            ));

//        db.addCategory("rachunki");
//        db.addCategory("jedzenie");
//        db.addCategory("dochody stałe");
////
//        db.addTransaction(new Transaction("Opłata za akademik", -312.43f, 2013, 11, 26, "rachunki"));
//        db.addTransaction(new Transaction("Mleko", -2.74f, 2014, 11, 25, "jedzenie"));
//        db.addTransaction(new Transaction("Pieniądze za las", 31223.43f, 2013, 11, 25, "dochody stałe"));
//        db.addTransaction(new Transaction("Opłata za prąd", -302.43f, 2014, 12, 26, "rachunki"));
//        db.addTransaction(new Transaction("Kisiel", -2.43f, 2013, 11, 23, "jedzenie"));

//        for (Transaction t:res.list) {
//            System.out.println(t.getTitle() + ", ");
//        }
        try {
            SESSION.db.addUser("mlekoo", "mleko123");
        } catch (AlreadyExistsException e) {
            System.out.println("Już jest taki user");
        }


        launch(args);
    }
}
