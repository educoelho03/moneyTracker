package br.com.moneyTracker.service;

import br.com.moneyTracker.exceptions.SendMailException;
import br.com.moneyTracker.interfaces.EmailInterface;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailInterface {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${moneytracker.email.sender}")
    private String sender;

    @Override
    public void sendMail(Message message) throws SendMailException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(message.getTo());
            helper.setSubject(message.getSubject());
            helper.setText(message.getBody(), true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e){
            throw new SendMailException("Error to send email", e);
        }
    }
}
