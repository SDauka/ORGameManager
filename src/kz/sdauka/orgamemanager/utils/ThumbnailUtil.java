package kz.sdauka.orgamemanager.utils;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import kz.sdauka.orgamemanager.controllers.GamesFormCTRL;
import kz.sdauka.orgamemanager.dao.factory.DAOFactory;
import kz.sdauka.orgamemanager.entity.Game;
import kz.sdauka.orgamemanager.entity.SessionDetails;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
public class ThumbnailUtil {
    private static final Logger LOG = Logger.getLogger(ExportToExcel.class);
    public final static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    public static ProcessBuilder processBuilder;

    public static Pane createPane() {
        Pane pane = new Pane();
        pane.setPrefWidth(300);
        pane.setPrefHeight(150);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 5.0;");
        pane.setEffect(new DropShadow(5.0, 3.0, 3.0, Color.BLACK));
        return pane;
    }

    public static ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView();
        File imageFile = new File(imagePath);
        try {
            final Image image;
            image = new Image(new FileInputStream(imageFile), 300, 150, true, true);
            imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(150);
        } catch (FileNotFoundException ex) {
            LOG.error(ex);
        }
        return imageView;
    }

    public static Rectangle createRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.DODGERBLUE);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setOpacity(0.7);
        rectangle.setHeight(30);
        rectangle.setWidth(282);
        rectangle.setLayoutX(9);
        rectangle.setLayoutY(101);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);
        return rectangle;
    }

    public static Separator createSeparator() {
        Separator separator = new Separator();
        separator.orientationProperty().setValue(Orientation.VERTICAL);
        separator.setOpacity(0.7);
        separator.setPrefHeight(15);
        separator.setLayoutX(150);
        separator.setLayoutY(109);
        return separator;
    }

    public static Button createButton(final Game game) {
        final Button button = new Button("Играть", new ImageView("img/play2.png"));
        button.setLayoutX(30);
        button.setLayoutY(100);
        button.setStyle("-fx-background-color: transparent;");
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            button.setTextFill(Color.YELLOW);
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            button.setTextFill(Color.WHITE);
        });
        //event ?
        button.setOnAction(event -> {
            OperatorBlockUtil.disableAllBlocking();
            try {
                if (game.getAttribute() == null) {
                    processBuilder = new ProcessBuilder(game.getPath());
                    processBuilder.directory(new File(game.getPath().substring(0, game.getPath().lastIndexOf("\\") + 1)));
                    processBuilder.start();
                } else {
                    List<String> command = new ArrayList<>();
                    command.add(game.getPath());
                    command.addAll(Arrays.asList(game.getAttribute().split(",")));
                    processBuilder = new ProcessBuilder(command);
                    processBuilder.directory(new File(game.getPath().substring(0, game.getPath().lastIndexOf("\\") + 1)));
                    processBuilder.start();
                }
                service.schedule(() -> {
                    try {
                        if (game.getPath().contains("DirectToRift")) {
                            Runtime.getRuntime().exec("taskkill /F /IM " + game.getPath().substring(game.getPath().lastIndexOf("\\") + 1, game.getPath().lastIndexOf("_")) + ".exe");
                        } else {
                            processBuilder.start().destroy();
                        }
                        OperatorBlockUtil.enableAllBlocking();
                    } catch (IOException e) {
                        LOG.error(e);
                    }
                }, game.getTime(), TimeUnit.MINUTES);
            } catch (IOException e) {
                LOG.error(e);
            }
            try {
                SessionDetails sessionDetails = new SessionDetails();
                GamesFormCTRL.generalSession.setCountStart(GamesFormCTRL.generalSession.getCountStart() + 1);
                GamesFormCTRL.generalSession.setSum(GamesFormCTRL.generalSession.getSum() + game.getCost());
                GamesFormCTRL.generalSession.setOperator(GamesFormCTRL.getGeneralOperator().getName());
                sessionDetails.setGameName(game.getName());
                sessionDetails.setSessionBySessionId(GamesFormCTRL.generalSession);
                sessionDetails.setStartTime(new Timestamp(new Date().getTime()));
                DAOFactory.getInstance().getSessionDAO().setSessionDetails(sessionDetails);
            } catch (SQLException e) {
                LOG.error(e);
            }
        });
        return button;
    }

    public static Label createLabel(String cost) {
        Label label = new Label(cost + " тенге");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        label.setTextFill(Color.WHITE);
        label.setLayoutX(170);
        label.setLayoutY(106);
        return label;
    }
}
