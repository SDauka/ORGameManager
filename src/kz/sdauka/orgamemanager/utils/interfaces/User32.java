package kz.sdauka.orgamemanager.utils.interfaces;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Created by Dauletkhan on 08.01.2015.
 */
public interface User32 extends StdCallLibrary {
    final User32 instance = (User32) Native.loadLibrary("user32", User32.class);

    WinDef.HWND FindWindow(String winClass, String title);

    WinDef.HWND FindWindowExA(WinDef.HWND hwndParent, WinDef.HWND childAfter, String className, String windowName);

    //  boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow);
//    boolean SetForegroundWindow(WinDef.HWND hWnd);
    WinDef.BOOL SetWindowPos(WinDef.HWND hwnd, int hWndInsertAfter, int x, int y, int cx, int cy, int wFlags);
    WinDef.HWND ShowWindow(WinDef.HWND hWND, int nCmdShow);

}
