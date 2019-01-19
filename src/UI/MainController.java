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
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController extends Controller implements Initializable{

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

    @FXML
    private Pane homeButton;

    @FXML
    private Pane historyButton;

    @FXML
    private Button addTransactionButton;
    @Override
    public void initialize(URL location, ResourceBundle resources){
            super.init(homeButton, historyButton);
            SortedTransactionList incomes = SESSION.db.getTransactionsByPredicate("value > 0");
            SortedTransactionList outcomes = SESSION.db.getTransactionsByPredicate("value < 0");
            saldoLabel.setText("Saldo: " + new Float(incomes.sum()+ outcomes.sum()).toString());
            mainPie.setData(UIWrapper.getBalancePieChartData(incomes.sum(), outcomes.sum()));
           // lastMonthPie.setData(UIWrapper.getBalancePieChartData(incomes.getTransactionsFromDate(new java.sql.Date(new java.util.Date()))));
            lastWeekPie.setData(UIWrapper.getBalancePieChartData(324, 543));

            addTransactionButton.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // Stwórz popup'a
                        Dialog<Pair<String, String>> dialog = new Dialog<>();
                        dialog.setTitle("Add transaction");
                        dialog.setHeaderText("Adding new transaction");

                        // Dodaj przyciski
                        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

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
                            if (dialogButton == loginButtonType) {
                                return new Pair<>("mleko", password.getText());
                            }
                            return null;
                        });


                        Optional<Pair<String, String>> result = dialog.showAndWait();

                        result.ifPresent(usernamePassword -> {
//                            SESSION.db.addTransaction(new Transaction(
//                                    title.getText(),
//                                    Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
//                                    category.
//
//                            ));
                            System.out.println(category.getSelectionModel().getSelectedItem().toString());
                        });

                    }
                }
            );

    }


}
