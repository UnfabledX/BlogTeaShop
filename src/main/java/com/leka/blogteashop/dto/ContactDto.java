package com.leka.blogteashop.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDto {

    @NotEmpty(message = "{notEmpty.name}")
    @Size(min = 4, max = 125, message = "{size.name}")
    private String name;

    @NotEmpty(message = "{notNull.email}")
    @Email(message = "{wellFormed.email}")
    private String email;

    @Pattern(regexp = "(^$)|(\\+[0-9]{3}\\s?[0-9]{2}\\s?[0-9]{3}\\s?[0-9]{4})",
            message = "{wrong.format}")
    private String phone;

    @NotEmpty(message = "{notEmpty.message}")
    @Size(min = 50, max = 500, message = "{size.message}")
    private String message;

    @Override
    public String toString() {
        return  "name: " + name + "\n" +
                "email: " + email + "\n" +
                "phone: " + phone + "\n" +
                "message: " + message;
    }
}
