package kz.sdauka.orgamemanager.utils;

import org.apache.log4j.Logger;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;

/**
 * Created by Dauletkhan on 16.01.2015.
 */
public class EscKeyListener implements NativeKeyListener {
    private static final Logger LOG = Logger.getLogger(EscKeyListener.class);
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                if (ThumbnailUtil.processBuilder.start().isAlive()) {
                    serviceShutdown(ThumbnailUtil.processBuilder);
                }
            } catch (IOException e1) {
                LOG.error(e1);
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public void serviceShutdown(ProcessBuilder process) {
        try {
            if (process.command().get(0).contains("DirectToRift")) {
                Runtime.getRuntime().exec("taskkill /F /IM " + process.command().get(0).substring(process.command().get(0).lastIndexOf("\\") + 1, process.command().get(0).lastIndexOf("_")) + ".exe");
            } else {
                process.start().destroy();
            }
        } catch (IOException e) {
            LOG.error(e);
        }
        OperatorBlockUtil.enableAllBlocking();
    }
}
