package UI;

import DB.DataBase;
import DB.TransactionList;
import Util.Interval;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class Controller{
    protected DataBase db;
    protected Pane homeButton;
    protected Pane historyButton;

    public void init(Pane homeButton, Pane historyButton){

        this.homeButton = homeButton;
        this.historyButton = historyButton;

        this.homeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Hello World");
                SceneWrapper.setWindow(WindowsTypes.MAIN);

            }
        });

        this.historyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Bye world");
                SceneWrapper.setWindow(WindowsTypes.HISTORY);

            }
        });



    }
}
