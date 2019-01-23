package UI;

import DB.*;


import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

//import java.beans.EventHandler;
import java.net.URL;
import java.sql.Date;
import java.util.*;

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

    @FXML
    ComboBox historySortingComboBox;

    @FXML
    ComboBox chartRangeComboBox;

    @FXML
    Button updateButton;

    @Override
    public void onLoad(){
        initChart();
        setHistory();
    }

    private Interval historyChartInterval;

    private ComparisonMethod historyComparisonMethod;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();

        chartRangeComboBox.getSelectionModel().selectFirst();
        historySortingComboBox.getSelectionModel().selectFirst();

        historyChartInterval = Interval.MONTH;
        historyComparisonMethod = ComparisonMethod.DATE;
        super.init(homeButton, historyButton);
        initChart();

    }

    private void initButtons(){
        List<String> chartRangeOptions = new ArrayList<>();
        for (Interval i: Interval.values()
             ) {
            chartRangeOptions.add(i.getText());

        }
        chartRangeComboBox.setItems(FXCollections.observableArrayList(chartRangeOptions));

        List<String> comparisonOptions = new ArrayList<>();
        for (ComparisonMethod c: ComparisonMethod.values()
             ) {
            comparisonOptions.add(c.getText());
        }
        historySortingComboBox.setItems(FXCollections.observableArrayList(comparisonOptions));

        updateButton.setOnMouseClicked(
            event -> {
                String selectedInterval = chartRangeComboBox.getSelectionModel().getSelectedItem().toString();
                if (selectedInterval.equals(Interval.MONTH.getText())) {
                    historyChartInterval = Interval.MONTH;
                }
                else if (selectedInterval.equals(Interval.YEAR.getText())){
                    historyChartInterval = Interval.YEAR;
                }

                String selectedComparison = historySortingComboBox.getSelectionModel().getSelectedItem().toString();

                if(selectedComparison.equals(ComparisonMethod.DATE.getText())){
                    historyComparisonMethod = ComparisonMethod.DATE;
                }
                else if(selectedComparison.equals(ComparisonMethod.CATEGORY.getText())){
                    historyComparisonMethod = ComparisonMethod.CATEGORY;
                }
                else if(selectedComparison.equals(ComparisonMethod.VALUE.getText())){
                    historyComparisonMethod = ComparisonMethod.VALUE;
                }
                onLoad();
            }
        );
    }

    private void initChart(){
        SortedTransactionList tl = Session.getDb().getCurrentUserTransactions();
        historyChart.getData().clear();
        historyChart.getData().add(tl.getPlotData(historyChartInterval));
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        xAxis.setTickUnit(1.0);
    }

    private void setHistory(){
        SortedTransactionList hist = Session.getDb().getCurrentUserTransactions().sort(historyComparisonMethod);
        recordsContainer.getChildren().clear();
        for (Transaction t : hist.list
             ) {

            recordsContainer.getChildren().add(transactionPane(t));
        }
    }

    private GridPane transactionPane(Transaction transaction){
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
        id.setText(Integer.toString(transaction.getId()));

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
                    Session.getDb().deleteTransaction(Integer.parseInt(id.getText()));
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

        GridPane.setMargin(title, new Insets(10,0,10,0));

        gridPane.add(id, 0, 0);
        gridPane.add(title, 1, 0);
        gridPane.add(value, 2, 0);
        gridPane.add(date, 0, 1);
        gridPane.add(category, 1, 1);
        gridPane.add(deleteButton, 2, 1);
        return gridPane;
    }



}
