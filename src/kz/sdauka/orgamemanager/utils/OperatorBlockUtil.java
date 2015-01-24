package kz.sdauka.orgamemanager.utils;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import kz.sdauka.orgamemanager.utils.interfaces.User32;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
public class OperatorBlockUtil {
    private static WinUser.HHOOK hhk;
    private static WinDef.HWND taskBarHWND;
    private static WinUser.LowLevelKeyboardProc keyboardHook;
    private static com.sun.jna.platform.win32.User32 lib;

    public static void blockWindowsKey() {
        if (isWindows()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    lib = com.sun.jna.platform.win32.User32.INSTANCE;
                    WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
                    keyboardHook = new WinUser.LowLevelKeyboardProc() {
                        public WinDef.LRESULT callback(int nCode, WinDef.WPARAM wParam, WinUser.KBDLLHOOKSTRUCT info) {
                            if (nCode >= 0) {
                                switch (info.vkCode) {
                                    case WinUser.VK_RMENU:
                                    case WinUser.VK_LMENU:
                                    case 91:
                                    case 92:
                                        return new WinDef.LRESULT(1);
                                    default: //do nothing
                                }
                            }
                            return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
                        }
                    };
                    hhk = lib.SetWindowsHookEx(13, keyboardHook, hMod, 0);

                    // This bit never returns from GetMessage
                    int result;
                    WinUser.MSG msg = new WinUser.MSG();
                    while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
                        if (result == -1) {
                            break;
                        } else {
                            lib.TranslateMessage(msg);
                            lib.DispatchMessage(msg);
                        }
                    }
                    lib.UnhookWindowsHookEx(hhk);
                }
            }).start();
        }
    }

    public static void unblockWindowsKey() {
        if (isWindows() && lib != null) {
            lib.UnhookWindowsHookEx(hhk);
        }
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    public static void hideTaskBar() {
        taskBarHWND = User32.instance.FindWindowExA(null, null, "Shell_TrayWnd", null);
        User32.instance.ShowWindow(taskBarHWND, 0);
    }

    public static void showTaskBar() {
        taskBarHWND = User32.instance.FindWindowExA(null, null, "Shell_TrayWnd", null);
        User32.instance.ShowWindow(taskBarHWND, 1);
    }

    public static void ctrlAltDelDisable() {
        Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System", "DisableTaskMgr", 1);
    }

    public static void ctrlAltDelEnable() {
        Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System", "DisableTaskMgr", 0);
    }

    public static void disableAllBlocking() {
        //ctrlAltDelDisable();
        unblockWindowsKey();
        showTaskBar();
    }

    public static void enableAllBlocking() {
        if (IniFileUtil.getSetting().isDisableKeys()) {
            blockWindowsKey();
        }
        if (IniFileUtil.getSetting().isDisableTaskManager()) {
            ctrlAltDelDisable();
        }
        if (IniFileUtil.getSetting().isHideTaskBar()) {
            hideTaskBar();
        }
    }
}
