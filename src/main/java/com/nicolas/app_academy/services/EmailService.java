package com.nicolas.app_academy.services;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  public void sendEmail(String to, String subject, String message) throws EmailException {
    SimpleEmail email = new SimpleEmail();

    email.setHostName("smtp-mail.outlook.com");
    email.setSmtpPort(587);
    email.setAuthentication("shshshghghgh23@outlook.com", "teste123321");
    email.setStartTLSRequired(true);
    email.setStartTLSEnabled(true);

    email.setFrom("shshshghghgh23@outlook.com");
    email.setSubject(subject);
    email.setMsg(message);
    email.addTo(to);

    email.send();
  }
}
