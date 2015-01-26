package kz.sdauka.orgamemanager.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import kz.sdauka.orgamemanager.dao.factory.DAOFactory;
import kz.sdauka.orgamemanager.entity.Operator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dauletkhan on 16.01.2015.
 */
public class LoginFormCTRL implements Initializable {
    @FXML
    private ComboBox<String> operatorsComboBox;
    @FXML
    private Label errorLabel;
    private Stage loginDialogStage;
    private boolean okClicked = false;
    private List<Operator> operators;
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    public Stage getLoginDialogStage() {
        return loginDialogStage;
    }

    public void setLoginDialogStage(Stage loginDialogStage) {
        this.loginDialogStage = loginDialogStage;
    }

    @FXML
    private void loginAction(ActionEvent actionEvent) throws IOException {
        if (operatorsComboBox.getValue() != null && !operatorsComboBox.getValue().toString().isEmpty()) {
            GamesFormCTRL.setGeneralOperator(operators.get(operatorsComboBox.getSelectionModel().getSelectedIndex()));
            loginDialogStage.close();
            okClicked = true;
        } else {
            errorLabel.setText("Выберите оператора");
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


    private List<Operator> getOperator() {
        List<Operator> operators = null;
        try {
            operators = DAOFactory.getInstance().getOperatorsDAO().getOperators();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operators;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operators = getOperator();
        for (Operator operator : operators) {
            operatorsComboBox.getItems().add(operator.getName());
        }
    }

}
