package sample;

import DB.DataBase;
import UI.UIWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.awt.*;
import java.awt.event.MouseEvent;

import javafx.scene.control.Label;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable{

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
            float income = db.getTransactionsByPredicate("value > 0").sum();
            float outcome = db.getTransactionsByPredicate("value < 0").sum();
            saldoLabel.setText("Saldo: " + new Float(income+outcome).toString());
            mainPie.setData(UIWrapper.getBalancePieChartData(income, outcome));
            lastMonthPie.setData(UIWrapper.getBalancePieChartData(523, 543));
            lastWeekPie.setData(UIWrapper.getBalancePieChartData(324, 543));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
