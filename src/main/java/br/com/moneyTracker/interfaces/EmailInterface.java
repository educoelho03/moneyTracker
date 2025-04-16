package br.com.moneyTracker.interfaces;

import lombok.Builder;
import lombok.Getter;

// TODO: DEVO CRIAR UM METODO DE WELCOME BUILD EMAIL
public interface EmailInterface {

    void sendMail(Message message);

    @Getter
    @Builder
    class Message {

        private String to;
        private String subject;
        private String body;

    }
}
