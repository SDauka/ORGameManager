package kz.sdauka.orgamemanager.utils;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import kz.sdauka.orgamemanager.utils.interfaces.User32Extra;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
public class OperatorBlockUtil {
    private static WinUser.HHOOK hhk;
    private static WinDef.HWND taskBarHWND;
    private static WinDef.HWND hStartBtn;
    private static WinUser.LowLevelKeyboardProc keyboardHook;
    private static com.sun.jna.platform.win32.User32 lib;

    public static void blockWindowsKey() {
        if (isWindows()) {
            new Thread(() -> {
                lib = com.sun.jna.platform.win32.User32.INSTANCE;
                WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
                keyboardHook = (nCode, wParam, info) -> {
                    if (nCode >= 0) {
                        if (info.vkCode == 115) {
                            if (IniFileUtil.getSetting().isDisableAltF4()) {
                                return new WinDef.LRESULT(1);
                            }
                        } else if (info.vkCode == 9) {
                            if (IniFileUtil.getSetting().isDisableAltTab()) {
                                return new WinDef.LRESULT(1);
                            }
                        } else if (info.vkCode == 91 || info.vkCode == 92) {
                            if (IniFileUtil.getSetting().isDisableWin()) {
                                return new WinDef.LRESULT(1);
                            }
                        }
                    }
                    return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
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
        taskBarHWND = User32Extra.instance.FindWindowExA(null, null, "Shell_TrayWnd", null);
        hStartBtn = User32Extra.instance.FindWindowExA(null, null, "Button", null);
        if (hStartBtn != null) {

            User32Extra.instance.SetWindowPos(hStartBtn, 0, 0, 0, 0, 0, 0x0080);
        }
        User32Extra.instance.ShowWindow(taskBarHWND, 0);
    }

    public static void showTaskBar() {
        taskBarHWND = User32Extra.instance.FindWindowExA(null, null, "Shell_TrayWnd", null);
        hStartBtn = User32Extra.instance.FindWindowExA(null, null, "Button", null);
        if (hStartBtn != null) {
            User32Extra.instance.SetWindowPos(hStartBtn, 0, 0, 0, 0, 0, 0x0040);
        }
        User32Extra.instance.ShowWindow(taskBarHWND, 1);
    }

    public static void hideCharms() {
        if (System.getProperty("os.version").equals("6.3") || System.getProperty("os.version").equals("6.2")) {
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\ImmersiveShell\\EdgeUI", "DisableCharmsHint", 1);
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\ImmersiveShell\\EdgeUI", "DisableTRCorner", 1);
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\ImmersiveShell\\EdgeUI", "DisableTLCorner", 1);
        }
    }


    public static void showCharms() {
        if (System.getProperty("os.version").equals("6.3") || System.getProperty("os.version").equals("6.2")) {
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\ImmersiveShell\\EdgeUI", "DisableCharmsHint", 0);
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\ImmersiveShell\\EdgeUI", "DisableTRCorner", 0);
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\ImmersiveShell\\EdgeUI", "DisableTLCorner", 0);
        }
    }

    public static void ctrlAltDelDisable() {
        if (Advapi32Util.registryKeyExists(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System")) {
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System", "DisableTaskMgr", 1);
        } else {
            Advapi32Util.registryCreateKey(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Policies", "System");
            Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System", "DisableTaskMgr", 1);
        }
    }


    public static void ctrlAltDelEnable() {
        Advapi32Util.registrySetIntValue(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System", "DisableTaskMgr", 0);
    }

    public static void disableAllBlocking() {
        unblockWindowsKey();
        showTaskBar();
        showCharms();
        if (IniFileUtil.getSetting().isDisableTaskManager()) {
            ctrlAltDelEnable();
        }
    }

    public static void enableAllBlocking() {
        blockWindowsKey();
        if (IniFileUtil.getSetting().isDisableTaskManager()) {
            ctrlAltDelDisable();
        }
        if (IniFileUtil.getSetting().isHideTaskBar()) {
            hideTaskBar();
            hideCharms();
        }
    }
}
