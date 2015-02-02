package kz.sdauka.orgamemanager.utils;

import kz.sdauka.orgamemanager.entity.Setting;
import org.apache.log4j.Logger;
import org.ini4j.Wini;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.IOException;


/**
 * Created by Dauletkhan on 11.01.2015.
 */
public class IniFileUtil {
    private static final Logger LOG = Logger.getLogger(IniFileUtil.class);
    private static Setting setting = readIniFile();

    private static void createIniFile() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("ormanager");
        try {
            File file = new File(System.getProperty("user.home") + "\\AppData\\Local\\ORManager\\settings.ini");
            if (!file.exists()) {
                file.createNewFile();
            }
            Wini wini = new Wini(new File(System.getProperty("user.home") + "\\AppData\\Local\\ORManager\\settings.ini"));
            wini.put("Access Rights", "hideTaskBar", encryptor.encrypt("true"));
            wini.put("Access Rights", "disableTaskManager", encryptor.encrypt("true"));
            wini.put("Access Rights", "disableKeys", encryptor.encrypt("true"));
            wini.put("Email settings", "openNotification", encryptor.encrypt("false"));
            wini.put("Email settings", "closeNotification", encryptor.encrypt("false"));
            wini.put("Email settings", "emailAdresat", encryptor.encrypt(""));
            wini.put("Email settings", "emailSender", encryptor.encrypt(""));
            wini.put("Email settings", "emailPassword", encryptor.encrypt(""));
            wini.put("Email settings", "smtp", encryptor.encrypt(""));
            wini.put("Email settings", "port", encryptor.encrypt(""));
            wini.put("Ads settings", encryptor.encrypt("ads"), "");
            wini.put("Obs settings", encryptor.encrypt("obs"), "");
            wini.store();
        } catch (IOException ex) {
            LOG.error(" create ini file is failed", ex);
        }
    }

    private static Setting readIniFile() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("ormanager");
        Setting setting1 = new Setting();
        try {
            File file = new File(System.getProperty("user.home") + "\\AppData\\Local\\ORManager\\settings.ini");
            if (!file.exists()) {
                createIniFile();
            }
            Wini wini = new Wini(new File(System.getProperty("user.home") + "\\AppData\\Local\\ORManager\\settings.ini"));
            setting1.setHideTaskBar(Boolean.parseBoolean(encryptor.decrypt(wini.get("Access Rights", "hideTaskBar"))));
            setting1.setDisableTaskManager(Boolean.parseBoolean(encryptor.decrypt(wini.get("Access Rights", "disableTaskManager"))));
            setting1.setDisableKeys(Boolean.parseBoolean(encryptor.decrypt(wini.get("Access Rights", "disableKeys"))));
            setting1.setOpenNotification(Boolean.parseBoolean(encryptor.decrypt(wini.get("Email settings", "openNotification"))));
            setting1.setCloseNotification(Boolean.parseBoolean(encryptor.decrypt(wini.get("Email settings", "closeNotification"))));
            setting1.setEmailAdresat(encryptor.decrypt(wini.get("Email settings", "emailAdresat")));
            setting1.setEmailSender(encryptor.decrypt(wini.get("Email settings", "emailSender")));
            setting1.setEmailPassword(encryptor.decrypt(wini.get("Email settings", "emailPassword")));
            setting1.setSmtp(encryptor.decrypt(wini.get("Email settings", "smtp")));
            setting1.setPort(encryptor.decrypt(wini.get("Email settings", "port")));
            setting1.setAds(encryptor.decrypt(wini.get("Ads settings", "ads")));
            setting1.setObs(encryptor.decrypt(wini.get("Obs settings", "obs")));
        } catch (IOException e) {
            LOG.error(" read ini file is failed", e);
        }
        return setting1;
    }

    public static Setting getSetting() {
        return setting;
    }

}
