package com.leka.blogteashop.event.listener;

import com.leka.blogteashop.dto.ContactDto;
import com.leka.blogteashop.event.ContactEmailEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;



@Component
public class ContactEmailEventListener {

    private final String sourceAddress;
    private final String adminEmail;
    private final JavaMailSender mailSender;

    public ContactEmailEventListener(JavaMailSender mailSender,
                                     @Value("${email.adminEmail}") String adminEmail,
                                     @Value("${email.sourceAddress}") String sourceAddress) {
        this.mailSender = mailSender;
        this.adminEmail = adminEmail;
        this.sourceAddress = sourceAddress;
    }

    @EventListener
    public void handleEvent(ContactEmailEvent event) {
        ContactDto contact = event.getContact();
        SimpleMailMessage message = getMessage(contact);
        mailSender.send(message);
    }

    private SimpleMailMessage getMessage(ContactDto contact) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sourceAddress);
        message.setTo(adminEmail);
        message.setSubject("A new contact message from NotkaTea Blog!");
        message.setText(contact.toString());
        return message;
    }
}
