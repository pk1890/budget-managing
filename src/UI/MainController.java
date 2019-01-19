package UI;

import DB.SESSION;
import DB.SortedTransactionList;
import DB.Transaction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class MainController extends Controller implements Initializable{

    //@FXML
   // private Button happyButton;

    @FXML
    private PieChart mainPie;
    @FXML
    private PieChart lastYearPie;
    @FXML
    private Label saldoLabel;

    @FXML
    private Pane homeButton;

    @FXML
    private Pane historyButton;

    @FXML
    private Button addTransactionButton;

    @FXML
    private Button addCategoryButton;
    @Override
    public void onLoad(){
        updateCharts();
        System.out.println("Działaaaa");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
            super.init(homeButton, historyButton);
            updateCharts();

            addTransactionButton.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // Stwórz popup'a
                        Dialog<Pair<String, String>> dialog = new Dialog<>();
                        dialog.setTitle("Add transaction");
                        dialog.setHeaderText("Adding new transaction");

                        // Dodaj przyciski
                        ButtonType confirmButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                        // Create the username and password labels and fields.
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        TextField title = new TextField();
                        title.setPromptText("Title");

                        DatePicker date = new DatePicker();
                        date.setPromptText("Transaction date");

                        TextField value = new TextField();
                        value.textProperty().addListener(
                                new ChangeListener<String>() {
                                    @Override
                                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                        if (!newValue.matches("^-?\\d*\\.?\\d{0,2}$")) {
                                            value.setText(oldValue);
                                        }
                                    }
                                }
                        );

                        ComboBox category = new ComboBox();
                        category.setPromptText("Set category");

                        category.setItems(SESSION.db.getCategoriesNames());

                        PasswordField password = new PasswordField();
                        password.setPromptText("Password");

                        grid.add(new Label("Title:"), 0, 1);
                        grid.add(title, 1, 1);


                        grid.add(new Label("Value:"), 0, 2);
                        grid.add(value, 1, 2);

                        grid.add(new Label("Date:"), 0, 3);
                        grid.add(date, 1, 3);

                        grid.add(new Label("Category:"), 0, 4);
                        grid.add(category, 1, 4);

                        dialog.getDialogPane().setContent(grid);

                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == confirmButtonType) {
                                return new Pair("data", "yes");
                            }

                            return null;
                        });


                        Optional<Pair<String, String>> result = dialog.showAndWait();

                        result.ifPresent(usernamePassword -> {

                            SESSION.db.addTransaction(new Transaction(
                                        title.getText(),
                                        Float.parseFloat(value.getText()),
                                        date.getValue().getYear(),
                                        date.getValue().getMonthValue()-1,
                                        date.getValue().getDayOfMonth(),
                                        category.getSelectionModel().getSelectedItem().toString(),
                                        SESSION.loggedUser.id
                                ));
                            updateCharts();
                        });

                    }
                }
            );


            addCategoryButton.setOnMouseClicked(
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            // Stwórz popup'a
                            Dialog<Pair<String, String>> dialog = new Dialog<>();
                            dialog.setTitle("Add transaction");
                            dialog.setHeaderText("Adding new Category");

                            // Dodaj przyciski
                            ButtonType confirmButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                            // Create the username and password labels and fields.
                            GridPane grid = new GridPane();
                            grid.setHgap(10);
                            grid.setVgap(10);
                            grid.setPadding(new Insets(20, 150, 10, 10));

                            TextField name = new TextField();
                            name.setPromptText("Name");

                            grid.add(new Label("Category Name:"), 0, 1);
                            grid.add(name, 1, 1);

                            dialog.getDialogPane().setContent(grid);

                            dialog.setResultConverter(dialogButton -> {
                                if (dialogButton == confirmButtonType) {
                                    return new Pair("data", "yes");
                                }

                                return null;
                            });

                            Optional<Pair<String, String>> result = dialog.showAndWait();

                            result.ifPresent(usernamePassword -> {

                                SESSION.db.addCategory(name.getText());
                                updateCharts();
                            });

                        }
                    }
            );

    }
    public void updateCharts(){

        LocalDate now = LocalDate.now();

        SortedTransactionList incomes = SESSION.db.getCurrentUserTransactionsByPredicate("value > 0");
        SortedTransactionList outcomes = SESSION.db.getCurrentUserTransactionsByPredicate("value < 0");

        Date month_start = new Date(
                now.getYear(),
                1,
                1
        );
        Date yearStart = new Date(
                now.getYear(),
                1,
                1
        );

        SortedTransactionList MonthIncomes = incomes.getTransactionsFromDate(month_start);
        SortedTransactionList MonthOutcomes = outcomes.getTransactionsFromDate(month_start);

        SortedTransactionList YearIncomes = incomes.getTransactionsFromDate(yearStart);
        SortedTransactionList YearOutcomes = outcomes.getTransactionsFromDate(yearStart);
        saldoLabel.setText("Saldo: " + new Float(incomes.sum()+ outcomes.sum()).toString());
        mainPie.getData().clear();
        mainPie.setData(UIWrapper.getBalancePieChartData(MonthIncomes.sum(), MonthOutcomes.sum()));
        // lastMonthPie.setData(UIWrapper.getBalancePieChartData(incomes.getTransactionsFromDate(new java.sql.Date(new java.util.Date()))));
        lastYearPie.setData(UIWrapper.getBalancePieChartData(YearIncomes.sum(), YearOutcomes.sum()));
    }


}
