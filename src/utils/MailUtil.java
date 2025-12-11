package utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com"; // Change if using different provider
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = System.getenv("SMTP_USER") != null 
            ? System.getenv("SMTP_USER") 
            : "your-email@gmail.com";
    private static final String SMTP_PASSWORD = System.getenv("SMTP_PASSWORD") != null 
            ? System.getenv("SMTP_PASSWORD") 
            : "your-app-password";
    
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });
    }

    public static boolean sendVerificationEmail(String toEmail, String name, String verificationLink) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Verify Your RecipeShare Account");
            
            String htmlContent = "<html><body>"
                    + "<h2>Welcome to RecipeShare, " + name + "!</h2>"
                    + "<p>Thank you for registering. Please verify your email address by clicking the link below:</p>"
                    + "<p><a href=\"" + verificationLink + "\" style=\"background-color: #27ae60; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\">Verify Email</a></p>"
                    + "<p>Or copy and paste this link into your browser:</p>"
                    + "<p>" + verificationLink + "</p>"
                    + "<p>This link will expire in 24 hours.</p>"
                    + "<p>If you didn't create an account, please ignore this email.</p>"
                    + "</body></html>";
            
            message.setContent(htmlContent, "text/html");
            
            Transport.send(message);
            Logger.info("Verification email sent to: " + toEmail);
            return true;
        } catch (MessagingException e) {
            Logger.error("Failed to send verification email to: " + toEmail, e);
            return false;
        }
    }
}

