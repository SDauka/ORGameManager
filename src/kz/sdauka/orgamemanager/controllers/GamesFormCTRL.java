package kz.sdauka.orgamemanager.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kz.sdauka.orgamemanager.dao.factory.DAOFactory;
import kz.sdauka.orgamemanager.entity.Game;
import kz.sdauka.orgamemanager.entity.Operator;
import kz.sdauka.orgamemanager.entity.Session;
import kz.sdauka.orgamemanager.entity.SessionDetails;
import kz.sdauka.orgamemanager.utils.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Dauletkhan on 20.01.2015.
 */
public class GamesFormCTRL implements Initializable {
    private static Stage stage;
    @FXML
    private VBox vbox;
    @FXML
    private TilePane gamesPanel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPane;
    private List<Game> gameList;
    private static Operator generalOperator;
    private boolean sessionStart = false;
    public static Session generalSession;
    private EmailSenderUtil emailSenderUtil = new EmailSenderUtil();

    public static Operator getGeneralOperator() {
        return generalOperator;
    }

    public static void setGeneralOperator(Operator generalOperator) {
        GamesFormCTRL.generalOperator = generalOperator;
    }

    public Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        GamesFormCTRL.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        gameList = getAllGames();
        gamesPanel.setPadding(new Insets(15, 15, 15, 15));
        gamesPanel.setHgap(40);
        gamesPanel.setVgap(23);
        gamesPanel.setDisable(true);
        GlobalScreen.getInstance().addNativeKeyListener(new EscKeyListener());
        OperatorBlockUtil.enableAllBlocking();
        for (Game game : gameList) {
            Pane pane = ThumbnailUtil.createPane();
            pane.getChildren().addAll(ThumbnailUtil.createImageView(game.getImage()),
                    ThumbnailUtil.createRectangle(),
                    ThumbnailUtil.createSeparator(),
                    ThumbnailUtil.createButton(game),
                    ThumbnailUtil.createLabel(String.valueOf(game.getCost())));
            gamesPanel.getChildren().addAll(pane);
        }
    }

    private List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        try {
            games.addAll(DAOFactory.getInstance().getGamesDAO().getAllGames());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    public boolean showOperatorDialog() {
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginForm.fxml"));
            Pane page = (Pane) loader.load();
            final Stage dialogStage = new Stage();
            dialogStage.setTitle("Выберите оператора");
            dialogStage.getIcons().add(new Image("/img/icon.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            // Set the person into the controller
            LoginFormCTRL controller = loader.getController();
            controller.setLoginDialogStage(dialogStage);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    dialogStage.close();
                }
            });
            return controller.isOkClicked();
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            return false;
        }
    }

    public void startSession(ActionEvent actionEvent) {
        boolean okClicked = showOperatorDialog();
        if (okClicked) {
            try {
                DAOFactory.getInstance().getSessionDAO().setSession(setStartSessionSettings(new Timestamp(new Date().getTime())));
                generalSession = DAOFactory.getInstance().getSessionDAO().getSession();
                sessionStart = true;
                if (IniFileUtil.getSetting().isOpenNotification() || InternetUtil.checkInternetConnection()) {
                    emailSenderUtil.sendStartSession(generalOperator.getName());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            gamesPanel.setDisable(false);
        }
    }

    public void stopSession(ActionEvent actionEvent) {

        //отправка почты о завершении сессии
        //завершения сессии
        stopGeneralSession();
        gamesPanel.setDisable(true);

    }

    public void closeProgram(ActionEvent actionEvent) {
        if (sessionStart) {
            stopGeneralSession();
        }
        OperatorBlockUtil.disableAllBlocking();
        System.exit(1);
    }

    private void stopGeneralSession() {
        List<SessionDetails> detailses;
        generalSession.setStopTime(new Timestamp(new Date().getTime()));
        try {
            DAOFactory.getInstance().getSessionDAO().updateSession(generalSession);
            detailses = DAOFactory.getInstance().getSessionDAO().getAllSessionDetails(generalSession.getId());
            sessionStart = false;
            if (IniFileUtil.getSetting().isCloseNotification() || InternetUtil.checkInternetConnection()) {
                emailSenderUtil.sendStopSession(generalOperator.getName(), ExportToExcel.exportToExcel(generalSession, detailses));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Session setStartSessionSettings(Timestamp time) {
        Session session = new Session();
        session.setCountStart(0);
        session.setDay(new java.sql.Date(new Date().getTime()));
        session.setOperator(generalOperator.getName());
        session.setStartTime(time);
        session.setStopTime(null);
        return session;
    }

}
