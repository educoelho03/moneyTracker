package br.com.moneyTracker.service;

import br.com.moneyTracker.dto.EmailDetails;
import br.com.moneyTracker.exceptions.SendMailException;
import br.com.moneyTracker.interfaces.EmailInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailInterface {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendMail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            if (emailDetails.getTo() == null || emailDetails.getSubject() == null || emailDetails.getBody() == null) {
                throw new SendMailException("Email details cannot be null");
            }

            message.setFrom(sender);
            message.setTo(emailDetails.getTo());
            message.setSubject(emailDetails.getSubject());
            message.setText(emailDetails.getBody());

            javaMailSender.send(message);
            return "Mail sent successfully...";
        } catch (Exception e){
            throw new SendMailException("Error to send email", e);
        }
    }

    @Override
    public String sendPasswordResetEmail(String email) {
        try {
            validateEmailIsNotNullOrEmpty(email);
            SimpleMailMessage message = new SimpleMailMessage();

            String resetPasswordLink = "http://localhost:5173/reset-password";
            String emailBody = "Olá,\n\n"
                    + "Você solicitou a redefinição de sua senha. Por favor, clique no link abaixo para redefinir sua senha:\n\n"
                    + resetPasswordLink + "\n\n"
                    + "Atenciosamente,\n"
                    + "Equipe MoneyTracker";

            message.setFrom(sender);
            message.setTo(email);
            message.setSubject("Redefinir Senha");
            message.setText(emailBody);

            javaMailSender.send(message);
            return "Mail sent successfully...";
        } catch (Exception e) {
            throw new SendMailException("Error to send password reset email", e);
        }
    }

    private void validateEmailIsNotNullOrEmpty(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new SendMailException("Email cannot be null or empty");
        }
    }

}
