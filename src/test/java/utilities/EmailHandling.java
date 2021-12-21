package utilities;
import Utilities.LogFileHandling;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static Tests.BasePage.HOME_DIRECTORY;

public class EmailHandling {

    public static void sendEnmail() {

        // Create object of Property file
        Properties props = new Properties();

        // this will set host of server- you can change based on your requirement
        props.put("mail.smtp.host", "smtp.gmail.com");

        // set the port of socket factory
        props.put("mail.smtp.socketFactory.port", "465");

        // set socket factory
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

        // set the authentication to true
        props.put("mail.smtp.auth", "true");

        // set the port of SMTP server
        props.put("mail.smtp.port", "465");

        // This will handle the complete authentication
        Session session = Session.getDefaultInstance(props,

                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication("qaautomation@simplyclub.co.il", "T7SVhyJabM6wW2n");

                    }

                });

        try {

            // Create object of MimeMessage class
            Message message = new MimeMessage(session);
            //Message message2 = new MimeMessage(session);

            // Set the from address
            message.setFrom(new InternetAddress("qaautomation@simplyclub.co.il"));
            //message2.setFrom(new InternetAddress("qaautomation@simplyclub.co.il"));

            // Set the recipient address
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("rnd@simplyclub.co.il"));
            //message2.setRecipients(Message.RecipientType.TO,InternetAddress.parse("rnd@simplyclub.co.il"));

            // Add the subject link
            message.setSubject("Testing Subject_"+java.time.LocalDateTime.now());
            //message2.setSubject("Run log_"+ java.time.LocalDateTime.now());

            // Create object to add multimedia type content
            BodyPart messageBodyPart1 = new MimeBodyPart();
            //BodyPart messageBodyPart1_2 = new MimeBodyPart();

            // Set the body of email
            messageBodyPart1.setText(LogFileHandling.ReadFile("EmailLog"));
            //messageBodyPart1_2.setText("This is an Run log");

            // Create another object to add another content
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
           // MimeBodyPart messageBodyPart2_2 = new MimeBodyPart();

            // Mention the file which you want to send
            String filename = HOME_DIRECTORY+"SimplyTest\\index.html";
            //String LogFileName = "C:\\Users\\User\\IdeaProjects\\SimplyTest\\IntelJRunLog\\RunLog.html";

            // Create data source and pass the filename
            DataSource source = new FileDataSource(filename);
            //DataSource source2 = new FileDataSource(LogFileName);

            // set the handler
            messageBodyPart2.setDataHandler(new DataHandler(source));
            //messageBodyPart2_2.setDataHandler(new DataHandler(source2));

            // set the file
            messageBodyPart2.setFileName(filename);
            //messageBodyPart2_2.setFileName(LogFileName);

            // Create object of MimeMultipart class
            Multipart multipart = new MimeMultipart();
            //Multipart multipart2 = new MimeMultipart();

            // add body part 1
            multipart.addBodyPart(messageBodyPart2);
            //multipart2.addBodyPart(messageBodyPart2_2);

            // add body part 2
            multipart.addBodyPart(messageBodyPart1);
            //multipart2.addBodyPart(messageBodyPart1_2);

            // set the content
            message.setContent(multipart);
            //message2.setContent(multipart2);

            // finally send the email
            Transport.send(message);
            //Transport.send(message2);

            System.out.println(MainFunction.BaseLogStringFunc()+"=====Email Sent=====");

        } catch (MessagingException e) {

            throw new RuntimeException(e);

        }

    }

}
