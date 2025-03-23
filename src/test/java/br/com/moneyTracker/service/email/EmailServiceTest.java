package br.com.moneyTracker.service.email;

import br.com.moneyTracker.dto.EmailDetails;
import br.com.moneyTracker.exceptions.SendMailException;
import br.com.moneyTracker.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.SortedSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// TODO: CORRIGIR TESTES EMAILm

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    EmailDetails emailDetails;
    String email;

    @BeforeEach
    public void setUp() {
        emailDetails = new EmailDetails();
        emailDetails.setTo("teste@email.com");
        emailDetails.setSubject("Test");
        emailDetails.setBody("Test");
    }


    @Test
    void sendMailWithSuccess(){
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class)); // simula o envio de email
        String result = emailService.sendMail(emailDetails);

        assertEquals("Mail sent successfully...", result);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void callExceptionWhenEmailDetailsIsNull(){
        emailDetails.setTo(null);
        emailDetails.setSubject(null);
        emailDetails.setBody(null);

        final SendMailException sendMailException = assertThrows(SendMailException.class, () -> emailService.sendMail(emailDetails));
        assertThat(sendMailException.getCause().getMessage(), is("Email details cannot be null"));
    }

    @Test
    void callExceptionWhenSendMailFailed(){
        doThrow(new RuntimeException("Error to send email")).when(javaMailSender).send(any(SimpleMailMessage.class));
        final SendMailException sendMailException = assertThrows(SendMailException.class, () -> emailService.sendMail(emailDetails));

        assertThat(sendMailException.getCause(), is(instanceOf(RuntimeException.class)));
        assertThat(sendMailException.getCause().getMessage(), is("Error to send email"));

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

    }



    // @Test
    // void sendPasswordResetEmailWithSuccess(){
    //     doNothing().when(javaMailSender).send(any(SimpleMailMessage.class)); // simula o envio de email
    //     String result = emailService.sendPasswordResetEmail(email);
//
    //     assertEquals("Mail sent successfully...", result);
    //     verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    // }

    //@Test
    //void callExceptionWhenEmailInSendPasswordResetEmailIsNull(){
    //    final SendMailException nullEmailException = assertThrows(SendMailException.class, () -> emailService.sendPasswordResetEmail(null));
    //    assertThat(nullEmailException.getCause().getMessage(), is("Email cannot be null or empty"));
//
    //    final SendMailException emptyEmailException = assertThrows(SendMailException.class, () -> emailService.sendPasswordResetEmail(""));
    //    assertThat(emptyEmailException.getCause().getMessage(), is("Email cannot be null or empty"));
//
    //}

    //@Test
    //void CallExceptionWhenSendPasswordResetMailFailed(){
    //    doThrow(new RuntimeException("Error to send password reset email")).when(javaMailSender).send(any(SimpleMailMessage.class));
//
    //    final SendMailException sendMailException = assertThrows(SendMailException.class, () -> emailService.sendPasswordResetEmail(email));
//
    //    // Verifica que a causa da exceção é RuntimeException
    //    assertThat(sendMailException.getCause(), instanceOf(RuntimeException.class));
//
    //    // Verifica a mensagem da exceção original (causa)
    //    //assertThat(sendMailException.getCause().getMessage(), is("Error to send password reset email"));
//
    //    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    //}

}
