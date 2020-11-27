package pl.telephoners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.telephoners.services.MailSenderService;

import javax.mail.MessagingException;

@RestController
public class MailingServiceController {


        private MailSenderService mailSenderService;

    public MailingServiceController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Autowired

        @GetMapping("/sendMail")
        public String sendMail() throws MessagingException {
        mailSenderService.sendMail("telephoners@gmail.com",
                    "Rado",
                    "<h1>RADO Z KOLORADO</h1>", true);
            return "wysłano";
        }
    }


