package br.com.moneyTracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDetails {

    private String to;
    private String subject;
    private String body;

    public EmailDetails() {

    }

    public EmailDetails(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }


}
