package com.leka.blogteashop.controller;

import com.leka.blogteashop.dto.ContactDto;
import com.leka.blogteashop.event.ContactEmailEvent;
import com.leka.blogteashop.service.impl.SessionContextHolder;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ApplicationEventPublisher publisher;
    private final SessionContextHolder sessionContextHolder;

    @GetMapping("/contact")
    public String getContacts(@ModelAttribute("contactDto") ContactDto contactDto, Model model) {
        model.addAttribute("contact", contactDto);
        return "contact";
    }

    @PostMapping("/sendContactMessage")
    public String sendContactMessage(@Valid @ModelAttribute("contactDto") ContactDto contactDto,
                                     BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return getContacts(contactDto, model);
        }
        sessionContextHolder.checkSpamCounterForSession(session);
        publisher.publishEvent(new ContactEmailEvent(contactDto));
        return "redirect:/contact?success";
    }

}
