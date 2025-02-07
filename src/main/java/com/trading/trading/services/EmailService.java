package com.trading.trading.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    public void sendverificationEmail(String email, String otp) throws MessagingException {
        
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
            String subject = "Verification Code";
            String htmlMsg = "<h3>OTP for email verification</h3>"
                    + "<p>Hi, your OTP is: " + otp + "</p>";
            helper.setSubject(subject);
            helper.setText(htmlMsg, true);
            helper.setTo(email);
       
    }
    
}
 