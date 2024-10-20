package com.dinidu.lk.pmt.utils;

import javafx.application.Platform;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class MailUtil {
    private static String myAccountEmail;
    private static String appPassword;

    static {
        loadEmailProperties();
    }

    public static void notifyPasswordChange(String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR","Error: User email is null or empty."));
            return;
        }
        sendMail(userEmail, 0,"");
    }


    public static void signUpConfirmation(String userName,String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR","Error: User email is null or empty."));
            return;
        }
        sendMail(userEmail, 0,userName);
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

    public static void sendMail(String recipient, int otp, String userName) {
        if (myAccountEmail == null || myAccountEmail.isEmpty() || appPassword == null || appPassword.isEmpty()) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR", "Email credentials are not configured properly."));
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

            Message message = prepareMessage(session, myAccountEmail, recipient, otp, userName);
            if (message != null) {
                Transport.send(message);
                System.out.println("Email sent successfully");
                Platform.runLater(() ->
                        CustomAlert.showAlert("CONFIRMATION", "Email sent successfully"));
            } else {
                throw new MessagingException("Failed to create email message.");
            }
        } catch (Exception ex) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR", "Failed to send email: " + ex.getMessage()));
            ex.printStackTrace();
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient, int otp, String userName) {
        try {
            if (recipient == null || recipient.isEmpty()) {
                throw new MessagingException("Recipient email is null or empty.");
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            if (otp != 0) {
                // Sending OTP email
                message.setSubject("Your OTP for Project Nexues Access");
                String emailBody = "Dear Admin,\n\n" +
                        "Thank you for using Project Nexues, your dedicated project management tool.\n\n" +
                        "To enhance the security of your account, we have generated a One-Time Password (OTP) for you. " +
                        "Please use the following OTP to verify your identity:\n\n" +
                        "               Your OTP: " + otp + "\n\n" +
                        "This OTP is valid for a limited time and is required to access your account securely.\n\n" +
                        "Important: Please do not share this OTP with anyone. If you did not request this OTP, please disregard this message.\n\n" +
                        "For any assistance or inquiries regarding Project Nexues, feel free to reach out to our support team.\n\n" +
                        "Best regards,\n\n" +
                        "Dinidu Sachintha\n" +
                        "Lead Software Engineer\n" +
                        "Project Nexues Team";
                message.setText(emailBody);
            } else if (userName != null) {
                // Sending signup confirmation email
                message.setSubject("Welcome to Project Nexues, " + userName + "!");
                String emailBody = "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<p>Dear <strong>" + userName + "</strong>,</p>" +
                        "<p>Welcome to <strong>Project Nexues</strong>!</p>" +
                        "<p>We are pleased to inform you that your account has been created successfully. " +
                        "Project Nexues is designed to enhance your project management experience, and we are excited to have you on board.</p>" +
                        "<p>Here are a few things you can do to get started:</p>" +
                        "<ul>" +
                        "<li>Log in to your account using your credentials.</li>" +
                        "<li>Explore our comprehensive features tailored for effective project management.</li>" +
                        "<li>Visit our help center for tutorials and support.</li>" +
                        "</ul>" +
                        "<p>If you have any questions or need assistance, please do not hesitate to reach out to our support team.</p>" +
                        "<p>Best regards,</p>" +
                        "<p><strong>Dinidu Sachintha</strong><br>" +
                        "Lead Software Engineer<br>" +
                        "Project Nexues Team</p>" +
                        "</body>" +
                        "</html>";
                message.setContent(emailBody, "text/html");
            } else {
                // Sending password change notification
                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                message.setSubject("IMPORTANT: Password Changed for Project Nexues");
                String emailBodyPw = "<html>" +
                        "<body>" +
                        "<p>Dear Admin,</p>" +
                        "<p>We are notifying you that your password for <strong>Project Nexues</strong> was changed on: <strong>" + currentDateTime + "</strong>.</p>" +
                        "<p>If you initiated this password change, no further action is required.</p>" +
                        "<p style='color: red;'><strong>However, if this change was unauthorized, it is critical that you take immediate action to secure your account.</strong></p>" +
                        "<ol>" +
                        "<li>Reset your password immediately using our account recovery process.</li>" +
                        "<li>Review your account activity to ensure no other unauthorized changes were made.</li>" +
                        "</ol>" +
                        "<p>Please be aware that your account security is of utmost importance to us. If you have any concerns or need assistance, " +
                        "contact our support team immediately.</p>" +
                        "<p>Best regards,<br>" +
                        "Project Nexues Team</p>" +
                        "</body>" +
                        "</html>";
                message.setContent(emailBodyPw, "text/html");
            }
            return message;
        } catch (Exception e) {
            Platform.runLater(() ->
                    CustomErrorAlert.showAlert("ERROR", "Failed to prepare email message: " + e.getMessage()));
            e.printStackTrace();
        }
        return null;
    }
}
