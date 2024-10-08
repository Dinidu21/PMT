package com.dinidu.lk.pmt.utils;

import javafx.application.Platform;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailUtil {
    private static String myAccountEmail;
    private static String appPassword;

    static {
        loadEmailProperties();
    }

    private static void loadEmailProperties() {
        Properties properties = new Properties();
        try (InputStream input = MailUtil.class.getClassLoader().getResourceAsStream("env.properties")) {
            if (input == null) {
                Platform.runLater(() ->
                        CustomErrorAlert.showAlert("ERROR","Error: Unable to find env.properties file."));
                return;
            }
            properties.load(input);
            myAccountEmail = properties.getProperty("EMAIL");
            appPassword = properties.getProperty("APP_PASSWORD");

            if (myAccountEmail == null || myAccountEmail.isEmpty() || appPassword == null || appPassword.isEmpty()) {
                Platform.runLater(() ->
                        CustomErrorAlert.showAlert("ERROR","Missing or empty email credentials in env.properties file."));
            }
        } catch (IOException e) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR","Error reading env.properties file: " + e.getMessage()));
        }
    }

    public static void sendMail(String recipient, int otp) {
        if (myAccountEmail == null || myAccountEmail.isEmpty() || appPassword == null || appPassword.isEmpty()) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR","Email credentials are not configured properly."));
            return;
        }

        try {
            System.out.println("Preparing to send email");
            Properties properties = new Properties();

            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, appPassword);
                }
            });

            Message message = prepareMessage(session, myAccountEmail, recipient, otp);
            if (message != null) {
                Transport.send(message);
                System.out.println("Email sent successfully");
                Platform.runLater(() ->
                        CustomAlert.showAlert("CONFIRMATION","Email (OTP) sent successfully"));
            } else {
                throw new MessagingException("Failed to create email message.");
            }
        } catch (Exception ex) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR","Failed to send email: " + ex.getMessage()));
            ex.printStackTrace();
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient, int otp) {
        try {
            if (recipient == null || recipient.isEmpty()) {
                throw new MessagingException("Recipient email is null or empty.");
            }
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Your OTP for Project Nexues Access");

            String emailBody = "Dear Admin,\n\n" +
                    "Thank you for using Project Nexues, your dedicated project management tool.\n\n" +
                    "To enhance the security of your account, we have generated a One-Time Password (OTP) for you. Please use the following OTP to verify your identity:\n\n" +
                    "               Your OTP: " + otp + "\n\n" +
                    "This OTP is valid for a limited time and is required to access your account securely.\n\n" +
                    "Important: Please do not share this OTP with anyone. If you did not request this OTP, please disregard this message.\n\n" +
                    "For any assistance or inquiries regarding Project Nexues, feel free to reach out to our support team.\n\n" +
                    "Best regards,\n\n" +
                    "Dinidu Sachintha\n" +
                    "Lead Software Engineer\n" +
                    "Project Nexues Team";

            message.setText(emailBody);
            return message;
        } catch (Exception e) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR","Failed to prepare email message: " + e.getMessage()));
            e.printStackTrace();
        }
        return null;
    }
}
