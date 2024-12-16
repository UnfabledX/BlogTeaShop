package com.leka.blogteashop.event;

import com.leka.blogteashop.dto.ContactDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class ContactEmailEvent extends ApplicationEvent {

    private final ContactDto contact;

    public ContactEmailEvent(ContactDto contact) {
        super(contact);
        this.contact = contact;
    }
}
