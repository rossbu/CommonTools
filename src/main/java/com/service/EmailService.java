package com.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class EmailService {
    public String sendMail(String email) {
        return String.format("Email successfully sent to %s", email);
    }
}
