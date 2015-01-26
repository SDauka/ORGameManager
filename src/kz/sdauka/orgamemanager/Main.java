package kz.sdauka.orgamemanager;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kz.sdauka.orgamemanager.utils.OperatorBlockUtil;

/**
 * Created by Dauletkhan on 16.01.2015.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("/fxml/gamesForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Oculus Rift Game manager");
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("/img/icon.png"));
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                OperatorBlockUtil.disableAllBlocking();
                System.exit(1);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
