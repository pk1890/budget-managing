package UI;

import DB.SESSION;
import DB.TransactionList;

import Util.Interval;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

//import java.beans.EventHandler;
import java.net.URL;
import java.sql.SQLException;
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
            TransactionList incomes = SESSION.db.getTransactionsByPredicate("value > 0");
            TransactionList outcomes = SESSION.db.getTransactionsByPredicate("value < 0");
            TransactionList tl = SESSION.db.getTransactions();
            historyChart.getData().add(tl.getPlotData(Interval.MONTH));

    }
}
