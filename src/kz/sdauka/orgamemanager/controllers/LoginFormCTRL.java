package kz.sdauka.orgamemanager.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kz.sdauka.orgamemanager.dao.factory.DAOFactory;
import kz.sdauka.orgamemanager.entity.Operator;
import kz.sdauka.orgamemanager.utils.OperatorBlockUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dauletkhan on 16.01.2015.
 */
public class LoginFormCTRL implements Initializable {
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    public static Operator operator;

    @FXML
    private void loginAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gamesForm.fxml"));

        if (!login.getText().equals("") && !password.getText().equals("")) {
            operator = getOperator(login.getText());
            if (operator != null && password.getText().equals(operator.getPassword())) {
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Oculus Rift Game manager");
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image("/img/icon.png"));
                stage.setFullScreenExitHint("");
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                stage.setFullScreen(true);

                stage.show();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        OperatorBlockUtil.disableAllBlocking();
                        System.exit(1);
                    }
                });
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

            } else {
                errorLabel.setText("Неправильный пароль/логин");
                errorLabel.setTextFill(Paint.valueOf("#d30f02"));
                service.schedule(new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                errorLabel.setText("");
                            }
                        });
                    }
                }, 2, TimeUnit.SECONDS);
            }
        } else {
            errorLabel.setText("Неправильный пароль/логин");
            errorLabel.setTextFill(Paint.valueOf("#d30f02"));
            service.schedule(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            errorLabel.setText("");
                        }
                    });
                }
            }, 2, TimeUnit.SECONDS);
        }


    }


    private Operator getOperator(String login) {
        Operator operator = null;
        try {
            operator = DAOFactory.getInstance().getOperatorsDAO().findOperatorByLogin(login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
