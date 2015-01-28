package kz.sdauka.orgamemanager.utils;

import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * Created by Dauletkhan on 21.01.2015.
 */
public class EmailSenderUtil {
    private String sender;
    private String password;
    private Properties props;
    private static final Logger LOG = Logger.getLogger(EmailSenderUtil.class);

    public EmailSenderUtil() {
        this.sender = IniFileUtil.getSetting().getEmailSender();
        this.password = IniFileUtil.getSetting().getEmailPassword();

        props = new Properties();
        props.put("mail.smtp.host", IniFileUtil.getSetting().getSmtp());
        props.put("mail.smtp.socketFactory.port", IniFileUtil.getSetting().getPort());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", IniFileUtil.getSetting().getPort());
    }

    public void sendStartSession(String operator) {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(sender));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(IniFileUtil.getSetting().getEmailAdresat()));
            //тема сообщения
            message.setSubject("Уведомление о начале сессии");
            message.setText("Сессия стартовала. Оператор: " + operator + ".");

            //отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error(e);
        }
    }

    public void sendStopSession(String operator, File file) {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(sender));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(IniFileUtil.getSetting().getEmailAdresat()));
            message.setSubject("Уведомление о завершении сессии");
            BodyPart messageBodyPart = new MimeBodyPart();
            // Now set the actual message
            messageBodyPart.setText("Сессия завершилась. Оператор: " + operator + ".");
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file.getName());
            multipart.addBodyPart(messageBodyPart);
            // Send the complete message parts
            message.setContent(multipart);
            Transport.send(message);
            file.delete();
        } catch (MessagingException e) {
            LOG.error(e);
        }
    }
}
