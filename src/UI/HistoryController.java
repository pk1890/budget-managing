package UI;

import DB.SESSION;
import DB.SortedTransactionList;

import Util.Interval;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

//import java.beans.EventHandler;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class HistoryController extends Controller implements Initializable {

    @FXML
    AreaChart historyChart;

    @FXML
    Pane homeButton;

    @FXML
    Pane historyButton;

    @FXML
    VBox recordsContainer;

    @FXML
    NumberAxis xAxis;

    @Override
    public void onLoad(){
        initChart();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            super.init(homeButton, historyButton);
//            SortedTransactionList incomes = SESSION.db.getTransactionsByPredicate("value > 0");
//            SortedTransactionList outcomes = SESSION.db.getTransactionsByPredicate("value < 0");
                initChart();


    }

    public void initChart(){
        SortedTransactionList tl = SESSION.db.getCurrentUserTransactions();
        historyChart.getData().clear();
        historyChart.getData().add(tl.getPlotData(Interval.MONTH));
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        xAxis.setTickUnit(1);
    }
}
