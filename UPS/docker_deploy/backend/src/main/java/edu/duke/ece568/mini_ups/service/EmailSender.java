package edu.duke.ece568.mini_ups.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailSender{
    private GMailer gmailsender;
    //private static final String subject = "test Mail";
    
    @Autowired
    public EmailSender(GMailer gmailsender){
        this.gmailsender = gmailsender;
    }

    public boolean sendMessage(String destination, String subject, String message)throws Exception {
        if (!isValidEmail(destination)) {
            System.out.println("Invalid email address");
            return false;
        }
        System.out.println("Sending message to " + destination + ": " + message);
        // send email
        gmailsender.sendMail(destination, subject, message);
        // finish sending email
        return true;
    }

    public boolean sendEmail(String destination, String subject) throws Exception {
        if (!isValidEmail(destination)) {
            System.out.println("Invalid email address");
            return false;
        }
        System.out.println("Sending email to " + destination);
        // send email
        gmailsender.sendMail(destination, subject, "Your package has been delivered!");
        // finish sending email
        return true;
    }

    private boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}

