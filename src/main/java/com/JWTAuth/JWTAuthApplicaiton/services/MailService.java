package com.JWTAuth.JWTAuthApplicaiton.services;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendMail(String to, String tokenToSend) {
        try {
            String resetLink =
                    "http://localhost:3000/resetpasswordpage?token=" + tokenToSend;

            String body =
                    "Hello,\n\n" +
                            "We received a request to reset your password.\n\n" +
                            "Click the link below to create a new password:\n" +
                            resetLink + "\n\n" +
                            "This link is valid for a limited time.\n\n" +
                            "If you did not request a password reset, please ignore this email.\n\n" +
                            "Regards,\n" +
                            "JWTAuth Support Team";

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject("Password Reset Request");
            mail.setText(body);
            mail.setFrom("jenilparmar94091@gmail.com");

            mailSender.send(mail);
            return true;
        } catch (Exception e) {
            System.out.println("Error in mail service ---> " + e.getMessage());
            return false;
        }
    }

}
