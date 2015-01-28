package kz.sdauka.orgamemanager.utils;

import org.apache.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dauletkhan on 26.01.2015.
 */
public class InternetUtil {
    private static final Logger LOG = Logger.getLogger(EmailSenderUtil.class);

    public static boolean checkInternetConnection() {
        Boolean result = false;
        HttpURLConnection con = null;
        try {
            // HttpURLConnection.setFollowRedirects(false);
            // HttpURLConnection.setInstanceFollowRedirects(false)
            con = (HttpURLConnection) new URL("http://www.ya.ru").openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    LOG.error(e);
                }
            }
        }
        return result;
    }
}
