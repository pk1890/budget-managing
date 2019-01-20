package UI;

import DB.SESSION;
import DB.SortedTransactionList;

import DB.Transaction;
import DB.User;
import Util.Interval;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

//import java.beans.EventHandler;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;
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
        setHistory();
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
        xAxis.setTickUnit(1.0);
    }

    public void setHistory(){
        SortedTransactionList hist = SESSION.db.getCurrentUserTransactions();
        recordsContainer.getChildren().clear();
        for (Transaction t : hist.list
             ) {

            recordsContainer.getChildren().add(transactionPane(t));
        }
    }

    GridPane transactionPane (Transaction transaction){
        Label title = new Label();
        title.setText(transaction.getTitle());
        title.setFont(Font.font(18.0));

        Label value = new Label();
        value.setText(String.valueOf(transaction.getValue()));
        title.setFont(Font.font(18.0));

        Date d = transaction.getDate();
        d.setYear(d.getYear()-1900);
        d.setMonth(d.getMonth()-1);
        Label date = new Label();
        date.setText(d.toString());

        Label category = new Label();
        category.setText(transaction.getCategory());

        Label id = new Label();
        id.setText(new Integer(transaction.getId()).toString());

        Button deleteButton = new Button("Delete");


        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dialog<Pair<String, String>> alert = new Dialog<>();
                alert.setTitle("Caution");
                alert.setHeaderText("Do you want to delete this transaction?");


                // Dodaj przyciski
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

                alert.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.YES) {
                        return new Pair<>("yes", "yes");
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = alert.showAndWait();

                result.ifPresent(usernamePassword -> {
                    SESSION.db.deleteTransaction(Integer.parseInt(id.getText()));
                    System.out.println("deleted transaction" + id.getText());
                    onLoad();
                });
            }
        });

        GridPane gridPane = new GridPane();


        if(transaction.getValue() > 0)
            gridPane.setStyle("-fx-background-color: green;");
        else
            gridPane.setStyle("-fx-background-color: red;");

        gridPane.setMargin(title, new Insets(10,0,10,0));

        gridPane.add(id, 0, 0);
        gridPane.add(title, 1, 0);
        gridPane.add(value, 2, 0);
        gridPane.add(date, 0, 1);
        gridPane.add(category, 1, 1);
        gridPane.add(deleteButton, 2, 1);
        return gridPane;
    }
}
