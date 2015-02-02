package kz.sdauka.orgamemanager.utils;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinNT;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
public class ThumbnailUtil {
    private static final Logger LOG = Logger.getLogger(ThumbnailUtil.class);
    public static Timer service;
    public static List<Tlhelp32.PROCESSENTRY32.ByReference> processEntry;
    public static int pid;
    public static TimerTask timeOut;


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
            if (IniFileUtil.getSetting().isDisableTaskManager()) {
                OperatorBlockUtil.ctrlAltDelEnable();
                    }
            if (IniFileUtil.getSetting().isHideTaskBar()) {
                OperatorBlockUtil.showTaskBar();
            }
            pid = GameProcessUtil.startGame(game);
            long startTime = System.currentTimeMillis();
            timeOut = new TimerTask() {
                @Override
                public void run() {
                    GameProcessUtil.StopGame(pid);
                    if (IniFileUtil.getSetting().isDisableTaskManager()) {
                        OperatorBlockUtil.ctrlAltDelDisable();
                    }
                    if (IniFileUtil.getSetting().isHideTaskBar()) {
                        OperatorBlockUtil.hideTaskBar();
                    }
                }
            };
            service = new Timer();
            service.schedule(timeOut, game.getTime() * 60 * 1000);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        SessionDetails sessionDetails = new SessionDetails();
                        GamesFormCTRL.generalSession.setCountStart(GamesFormCTRL.generalSession.getCountStart() + 1);
                        GamesFormCTRL.generalSession.setSum(GamesFormCTRL.generalSession.getSum() + game.getCost());
                        GamesFormCTRL.generalSession.setOperator(GamesFormCTRL.getGeneralOperator().getName());
                        sessionDetails.setGameName(game.getName());
                        sessionDetails.setSessionBySessionId(GamesFormCTRL.generalSession);
                        sessionDetails.setStartTime(new Timestamp(new Date().getTime()));
                        LOG.info("Запуск игры: " + game.getName());
                        WinNT.HANDLE gameHandle = Kernel32.INSTANCE.OpenProcess(Kernel32.SYNCHRONIZE, false, pid);
                        Kernel32.INSTANCE.WaitForSingleObject(gameHandle, Kernel32.INFINITE);
                        long timeSpent = System.currentTimeMillis() - startTime;
                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeSpent),
                                TimeUnit.MILLISECONDS.toMinutes(timeSpent) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeSpent)),
                                TimeUnit.MILLISECONDS.toSeconds(timeSpent) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeSpent)));
                        sessionDetails.setWorkTime(hms);
                        DAOFactory.getInstance().getSessionDAO().setSessionDetails(sessionDetails);
                        if (IniFileUtil.getSetting().isDisableTaskManager()) {
                            OperatorBlockUtil.ctrlAltDelDisable();
                        }
                        if (IniFileUtil.getSetting().isHideTaskBar()) {
                            OperatorBlockUtil.hideTaskBar();
                        }
                    } catch (SQLException e) {
                        LOG.error(e);
                    }
                }
            };
            thread.start();
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
