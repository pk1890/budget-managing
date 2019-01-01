package sample;

import DB.DataBase;
import DB.SQLGenerator;
import DB.Transaction;
import DB.TransactionList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        Scene mainScene = new Scene(root, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        DataBase db;
        TransactionList res = new TransactionList();
        try {
            db = new DataBase();
            res = db.getTransactionsByPredicate("1=1");

//        db.addCategory("rachunki");
//        db.addCategory("jedzenie");
//        db.addCategory("dochody stałe");
//
//        db.addTransaction(new Transaction("Opłata za akademik", -312.43f, 2013, 11, 26, "rachunki"));
//        db.addTransaction(new Transaction("Mleko", -2.74f, 2013, 11, 25, "jedzenie"));
//        db.addTransaction(new Transaction("Pieniądze za las", 31223.43f, 2013, 11, 25, "dochody stałe"));
//        db.addTransaction(new Transaction("Opłata za prąd", -302.43f, 2013, 12, 26, "rachunki"));
//        db.addTransaction(new Transaction("Kisiel", -2.43f, 2013, 11, 23, "jedzenie"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Transaction t:res.list) {
            System.out.print(t.getTitle() + ", ");
        }


        launch(args);
    }
}
