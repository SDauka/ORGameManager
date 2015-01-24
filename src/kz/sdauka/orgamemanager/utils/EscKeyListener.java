package kz.sdauka.orgamemanager.utils;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Created by Dauletkhan on 16.01.2015.
 */
public class EscKeyListener implements NativeKeyListener {
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            if (ThumbnailUtil.process.isAlive()) {
                serviceShutdown(ThumbnailUtil.process);
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public void serviceShutdown(Process process) {
        process.destroy();
        OperatorBlockUtil.blockWindowsKey();
    }
}
