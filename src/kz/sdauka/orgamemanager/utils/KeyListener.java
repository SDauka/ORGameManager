package kz.sdauka.orgamemanager.utils;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import javafx.application.Platform;
import kz.sdauka.orgamemanager.controllers.GamesFormCTRL;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;

/**
 * Created by Dauletkhan on 16.01.2015.
 */
public class KeyListener implements NativeKeyListener {
    private static final Logger LOG = Logger.getLogger(KeyListener.class);

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                serviceShutdown();
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_BACKQUOTE) {
            WinDef.HWND hwnd = com.sun.jna.platform.win32.User32.INSTANCE.FindWindow("OBSWindowClass", null);
            if (hwnd == null) {
                try {
                    if (IniFileUtil.getSetting().getObs() != null && !IniFileUtil.getSetting().getObs().isEmpty()) {
                        Runtime.getRuntime().exec(IniFileUtil.getSetting().getObs());

                    } else {
                        Platform.runLater(() -> Dialogs.create().owner(GamesFormCTRL.getStage()).title("Ошибка запуска OBS").message("Не удалось найти OBS").showError());
                    }
                } catch (IOException e1) {
                    LOG.error(e1);
                }
            } else {
                User32.INSTANCE.ShowWindow(hwnd, 9);
                User32.INSTANCE.ShowWindow(hwnd, 3);
                User32.INSTANCE.SetForegroundWindow(hwnd);
                User32.INSTANCE.SetFocus(hwnd);
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public void serviceShutdown() {
        if (IniFileUtil.getSetting().isDisableTaskManager()) {
            OperatorBlockUtil.ctrlAltDelDisable();
        }
        if (IniFileUtil.getSetting().isHideTaskBar()) {
            OperatorBlockUtil.hideTaskBar();
        }
        GameProcessUtil.StopGame(ThumbnailUtil.pid);
        ThumbnailUtil.timeOut.cancel();
        ThumbnailUtil.service.cancel();
    }
}
