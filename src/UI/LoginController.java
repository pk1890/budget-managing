package UI;

import DB.SESSION;
import DB.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Pair;

import javax.jws.soap.SOAPBinding;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    VBox usersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (User u: SESSION.db.getUsers()
             ) {
            usersList.getChildren().add(loginPane(u));

        }
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
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getSource() instanceof BorderPane){
                            BorderPane elem = (BorderPane)event.getSource();
                            if(elem.getCenter() instanceof Label)
                                writePassword(
                                        ((Label)elem.getCenter()).getText()
                                );
                        }

                    }
                }
        );

        return bp;
    }

    void writePassword(String login){
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
            if(SESSION.db.logIn(usernamePassword.getKey(), usernamePassword.getValue())){
                SceneWrapper.setWindow(WindowsTypes.MAIN);
            }
        });
    }
}
