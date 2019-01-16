package UI;

import DB.DataBase;
import DB.TransactionList;
import Util.Interval;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    StackedAreaChart historyChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            DataBase db = new DataBase();
            TransactionList incomes = db.getTransactionsByPredicate("value > 0");
            TransactionList outcomes = db.getTransactionsByPredicate("value < 0");
            TransactionList tl = db.getTransactions();
            historyChart.getData().add(tl.getPlotData(Interval.MONTH));
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
