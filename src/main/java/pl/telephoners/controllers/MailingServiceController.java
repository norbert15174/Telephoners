package pl.telephoners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.telephoners.models.MailContact;
import pl.telephoners.services.MailSenderService;

import javax.mail.MessagingException;
import java.util.List;

@RestController
public class MailingServiceController {


        private MailSenderService mailSenderService;

    @Autowired
    public MailingServiceController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }



        @PostMapping("/sendMail")
        public ResponseEntity<String> addNewSubscriber(@RequestBody MailContact mailContact) throws MessagingException {
        if(mailSenderService.findSubscriberMail(mailContact.getMailAddress()) != null) return new ResponseEntity<>("Subscriber exists in mailing list",HttpStatus.CONFLICT);
        mailSenderService.addSubscriberMail(mailContact);
        mailSenderService.sendMail("axe9@op.pl",
                    "New mailing contact: " + mailContact.getMailAddress(),
                    "<h3> New telephoner's content subscriber: " + mailContact.getMailAddress() + " ,please add him to mailing list</h3>", true);

            return new ResponseEntity<>("New subscriber has been added", HttpStatus.CREATED);
        }

    @DeleteMapping("/sendMail/{id}")
    public ResponseEntity<String> deleteSubscriber(@PathVariable long id) throws MessagingException {
        MailContact mailContact = mailSenderService.findSubscriberMailById(id);
        if(mailContact == null) return new ResponseEntity<>("Subscriber does not exist in mailing list",HttpStatus.NOT_FOUND);
        mailSenderService.deleteSubscriber(id);
        mailSenderService.sendMail("axe9@op.pl",
                "Delete contact: " + mailContact.getMailAddress(),
                "<h3> subscriber: " + mailContact.getMailAddress() + " , want to be deleted from mailing list</h3>", true);

        return new ResponseEntity<>("Subscriber has been deleted",HttpStatus.NOT_FOUND);
    }

        @GetMapping("/sendMail")
        public ResponseEntity<List<MailContact>> findAllSubscriber(){
            return new ResponseEntity<>(mailSenderService.getAllSubscriber(),HttpStatus.OK);
        }


    }


