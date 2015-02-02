package kz.sdauka.orgamemanager.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import kz.sdauka.orgamemanager.entity.Game;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dauletkhan on 31.01.2015.
 */
public class GameProcessUtil {
    private static final Logger LOG = Logger.getLogger(GameProcessUtil.class);
    private static int pid = 0;
    private static Process gameProcess = null;
    private static Kernel32 kernel = Kernel32.INSTANCE;

    public static int startGame(Game game) {
        int pr_pid = 0;
        List<String> command = new ArrayList<>();
        command.add(game.getPath());
        command.addAll(Arrays.asList(game.getAttribute().split(",")));
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(game.getPath().substring(0, game.getPath().lastIndexOf("\\") + 1)));
            gameProcess = processBuilder.start();
        } catch (IOException e) {
            LOG.error(e);
        }
        if (gameProcess.getClass().getName().equals("java.lang.Win32Process") ||
                gameProcess.getClass().getName().equals("java.lang.ProcessImpl")) {
            try {
                Field f = gameProcess.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handl = f.getLong(gameProcess);
                WinNT.HANDLE handle = new WinNT.HANDLE();
                handle.setPointer(Pointer.createConstant(handl));
                pid = kernel.GetProcessId(handle);
                LOG.info("pid " + pid);
            } catch (Throwable e) {
                LOG.error(e);
            }
        }
        try {
            Thread.sleep(1800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
        List<Tlhelp32.PROCESSENTRY32.ByReference> processentry32List = new ArrayList<>();
        WinNT.HANDLE snapshot = kernel.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
        try {
            while (kernel.Process32Next(snapshot, processEntry)) {
                processentry32List.add(processEntry);
            }
        } finally {
            kernel.CloseHandle(snapshot);
        }
        for (Tlhelp32.PROCESSENTRY32.ByReference processentry32 : processentry32List) {
            if (processentry32.th32ParentProcessID.intValue() == pid) {
                pr_pid = processentry32.th32ProcessID.intValue();
                break;
            }
            if (processentry32.th32ProcessID.intValue() == pid) {
                pr_pid = processentry32.th32ProcessID.intValue();
                break;
            }
        }
        System.out.println("pr pid " + pr_pid);
        return pr_pid;
    }


    public static void StopGame(int pr_pid) {
        try {
            LOG.info("pr1 pid " + pid);
            LOG.info("pr2 pid " + pr_pid);
            Runtime.getRuntime().exec("taskkill /F /pid " + pr_pid);
                    gameProcess.destroy();
        } catch (IOException e) {
            LOG.error(e);
        }
    }
}


