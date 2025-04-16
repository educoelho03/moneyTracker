package br.com.moneyTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
public class EmailDetails {

    private String to;
    private String subject;
    private String body;
    private String token;

    public EmailDetails() {

    }

    public EmailDetails(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }


}
