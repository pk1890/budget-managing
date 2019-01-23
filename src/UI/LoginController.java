package UI;

import DB.AlreadyExistsException;
import DB.Session;
import DB.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable, IOnLoad{

    @FXML
    private VBox usersList;

    @FXML
    private Button registerButton;


    @Override
    public void onLoad(){
        usersList.getChildren().clear();
        for (User u: Session.getDb().getUsers()
                ) {
            usersList.getChildren().add(loginPane(u));

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        registerButton.setOnMouseClicked(event -> {
            // Stwórz popup'a
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Login Dialog");
            dialog.setHeaderText("Enter login and password");

            // Dodaj przyciski
            ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField login = new TextField();
            login.setPromptText("Login");

            PasswordField password = new PasswordField();
            password.setPromptText("Password");


            grid.add(new Label("Login:"), 0, 1);
            grid.add(login, 1, 1);

            grid.add(new Label("Password:"), 0, 2);
            grid.add(password, 1, 2);


            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(login.getText(), password.getText());
                }
                return null;
            });


            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(usernamePassword -> {
                try {
                    Session.getDb().addUser(login.getText(), password.getText());
                } catch (AlreadyExistsException e) {
                    Dialog<Pair<String, String>> alert = new Dialog<>();
                    alert.setTitle("Error");
                    alert.setHeaderText("Login already in use");

                    // Dodaj przyciski
                    alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                    alert.show();

                }
                onLoad();
            });
        });

    }

    BorderPane loginPane (User user){
        Label lab = new Label();
        lab.setText(user.login);
        lab.setFont(Font.font(18.0));

        BorderPane bp = new BorderPane();
        bp.setCenter(lab);

        bp.setStyle("-fx-background-color: grey;");

        bp.setMargin(lab, new Insets(10,0,10,0));
        bp.setOnMouseClicked(
            event -> {
                if(event.getSource() instanceof BorderPane){
                    BorderPane elem = (BorderPane)event.getSource();
                    if(elem.getCenter() instanceof Label)
                        writePassword(
                            ((Label)elem.getCenter()).getText()
                        );
                }

            }
        );

        return bp;
    }

    private void writePassword(String login){
        // Stwórz popup'a
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Enter password for " + login);

        // Dodaj przyciski
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(login, password.getText());
            }
            return null;
        });


        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            if(Session.getDb().logIn(usernamePassword.getKey(), usernamePassword.getValue())){
                SceneWrapper.setWindow(WindowsTypes.MAIN);
            }
            else{
                Dialog<Pair<String, String>> alert = new Dialog<>();
                alert.setTitle("Error");
                alert.setHeaderText("Wrong password");

                // Dodaj przyciski
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                alert.show();
            }
        });
    }
}
