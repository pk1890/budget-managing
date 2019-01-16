package UI;

import DB.DataBase;
import DB.TransactionList;
import UI.UIWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import java.time.LocalDateTime;

import java.awt.*;
import java.awt.event.MouseEvent;

import javafx.scene.control.Label;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    //@FXML
   // private Button happyButton;

    @FXML
    private PieChart mainPie;
    @FXML
    private PieChart lastWeekPie;
    @FXML
    private PieChart lastMonthPie;
    @FXML
    private Label saldoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources){

//        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println("Hello");
//            }
//        };
        try {

            DataBase db = new DataBase();
            TransactionList incomes = db.getTransactionsByPredicate("value > 0");
            TransactionList outcomes = db.getTransactionsByPredicate("value < 0");
            saldoLabel.setText("Saldo: " + new Float(incomes.sum()+ outcomes.sum()).toString());
            mainPie.setData(UIWrapper.getBalancePieChartData(incomes.sum(), outcomes.sum()));
           // lastMonthPie.setData(UIWrapper.getBalancePieChartData(incomes.getTransactionsFromDate(new java.sql.Date(new java.util.Date()))));
            lastWeekPie.setData(UIWrapper.getBalancePieChartData(324, 543));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
