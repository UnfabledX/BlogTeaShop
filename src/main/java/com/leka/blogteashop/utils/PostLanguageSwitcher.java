package com.leka.blogteashop.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component(value = "postLanguageSwitcher")
public class PostLanguageSwitcher {

    public String defineLanguageOf(String description) {
        Locale locale = LocaleContextHolder.getLocale();
        if (description.contains("#&#")) {
            String[] descriptions = description.split("#&#");
            switch (locale.getLanguage()) {
                case "ukr" -> description = descriptions[0];
                case "en" -> description = descriptions[1];
            }
        }
        return description;
    }

    public String showLanguageSpecificContent(String content, String language) {
        if (content.contains("#&#")) {
            String[] descriptions = content.split("#&#");
            switch (language) {
                case "ukr" -> content = descriptions[0];
                case "en" -> content = descriptions[1];
                // if any other string passed to the method argument language
                default -> content = descriptions[0];
            }
        }
        return content;
    }
}
