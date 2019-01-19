package UI;

import DB.SESSION;
import DB.SortedTransactionList;

import Util.Interval;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

//import java.beans.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;

public class HistoryController extends Controller implements Initializable {

    @FXML
    StackedAreaChart historyChart;

    @FXML
    Pane homeButton;

    @FXML
    Pane historyButton;

    @FXML
    VBox recordsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            super.init(homeButton, historyButton);
            SortedTransactionList incomes = SESSION.db.getTransactionsByPredicate("value > 0");
            SortedTransactionList outcomes = SESSION.db.getTransactionsByPredicate("value < 0");
            SortedTransactionList tl = SESSION.db.getTransactions();
            historyChart.getData().add(tl.getPlotData(Interval.MONTH));

    }
}
