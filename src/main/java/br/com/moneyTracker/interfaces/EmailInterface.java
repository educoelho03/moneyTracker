package br.com.moneyTracker.interfaces;

import br.com.moneyTracker.dto.EmailDetails;

public interface EmailInterface {

    String sendMail(EmailDetails emailDetails);

}
