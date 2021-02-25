package pl.telephoners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.telephoners.models.MailContact;
import pl.telephoners.services.MailSenderService;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="*")
@RestController
public class MailingServiceController {
    @Value("${telephoners-email}")
    private static String telephonersMail;
    private MailSenderService mailSenderService;

    @Autowired
    public MailingServiceController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }


    @PostMapping("/sendMail")
    public ResponseEntity<String> addNewSubscriber(@RequestBody MailContact mailContact) throws MessagingException {
        if (mailSenderService.findSubscriberMail(mailContact.getMailAddress()) != null)
            return new ResponseEntity<>("Subscriber exists in mailing list", HttpStatus.CONFLICT);
        mailSenderService.addSubscriberMail(mailContact);
        mailSenderService.sendMail("axe9@op.pl",
                "New mailing contact: " + mailContact.getMailAddress(),
                "<h3> New telephoner's content subscriber: " + mailContact.getMailAddress() + " ,please add him to mailing list</h3>", true);

        return new ResponseEntity<>("New subscriber has been added", HttpStatus.CREATED);
    }

    @DeleteMapping("/sendMail/{id}")
    public ResponseEntity<String> deleteSubscriber(@PathVariable long id) throws MessagingException {
        MailContact mailContact = mailSenderService.findSubscriberMailById(id);
        if (mailContact == null)
            return new ResponseEntity<>("Subscriber does not exist in mailing list", HttpStatus.NOT_FOUND);
        mailSenderService.deleteSubscriber(id);
        mailSenderService.sendMail("axe9@op.pl",
                "Delete contact: " + mailContact.getMailAddress(),
                "<h3> subscriber: " + mailContact.getMailAddress() + " , want to be deleted from mailing list</h3>", true);

        return new ResponseEntity<>("Subscriber has been deleted", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sendMail")
    public ResponseEntity<List<MailContact>> findAllSubscriber() {
        return new ResponseEntity<>(mailSenderService.getAllSubscriber(), HttpStatus.OK);
    }

    @PostMapping("/sendMail/template")
    public ResponseEntity<String> sendContactMail(@RequestBody Map<String,String> contact){
        String topic = contact.get("email") + " would like to contact you \n " + contact.get("message");
        if(contact.get("isCopyRequired") == "yes") {
            String copiedMessage = "Message copied from the telephoners website \n + " + contact.get("message");
            String copiedTopic = "Message from the telephoners website: + " + contact.get("topic");
            mailSenderService.sendMailByGoogleMailApi(contact.get("email"),copiedTopic,copiedMessage);
        }
        if(mailSenderService.sendMailByGoogleMailApi(this.telephonersMail,topic,contact.get("message"))) return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

}


