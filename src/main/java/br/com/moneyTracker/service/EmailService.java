package br.com.moneyTracker.service;

import br.com.moneyTracker.dto.EmailDetails;
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
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(sender);
        message.setTo(emailDetails.getTo());
        message.setSubject(emailDetails.getSubject());
        message.setText(emailDetails.getBody());

        javaMailSender.send(message);
        return "Mail sent successfully...";
    }
}
